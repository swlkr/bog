(ns bog.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! chan]]
            [bidi.bidi :as bidi]))

(defn input-ch [state m]
  (merge state m))

(defn login-ch [state val]
  (let [{:keys [email password login-res-ch]} val]
    (go
      (let [res (<! (http/post "/api/tokens" {:json-params {:email email :password password}}))]
        (>! login-res-ch res)))
    (assoc state :info "Logging in...")))

(defn login-res-ch [state response]
  (let [{:keys [status body]} response]
    (if (= status 200)
      (do
        (. js/window.history (pushState "" "" "/drafts"))
        (.setItem js/localStorage "bog.core.access-token" (:access-token body))
        (assoc state :access-token (:access-token body)
                     :email ""
                     :password ""
                     :message ""
                     :info ""
                     :view :draft-list))
      (assoc state :message (:message body)
                   :info ""))))

(defonce auth-routes #{:new-draft
                       :draft-list})

(defn route-ch
  "Given an application state, set the current view based on the URL token"
  [state val]
  (let [{:keys [token routes]} val
        {:keys [handler route-params]} (bidi/match-route routes token)]
    (if (and
          (nil? (:access-token state))
          (contains? auth-routes handler))
      (do
        (. js/window.history (pushState "" "" "/login"))
        (assoc state :view :login))
      (assoc state :view handler :route-params route-params))))

(defn send [params]
  (let [{:keys [url method body headers]} params]
    (cond
      (= :post method) (http/post url {:json-params body :headers (or headers {})})
      (= :get method) (http/get url {:headers (or headers {})})
      (= :delete method) (http/delete url {:headers (or headers {})})
      (= :put method) (http/put url {:json-params body :headers (or headers {})}))))

(defn req-ch [state val]
  (go
    (let [{:keys [url method body headers res-ch]} val
          all-headers (merge (or headers {}) {"Authorization" (:access-token state)})
          res (<! (send {:url url
                         :method method
                         :body body
                         :headers all-headers}))]
      (>! res-ch (assoc res :res-ch res-ch))))
  (assoc state :info "Loading..."))

(defn res-ch [state res]
  (let [{:keys [status body res-ch]} res]
    (cond
      (= status 401) (do
                      (. js/window.history (pushState "" "" "/login"))
                      (.setItem js/localStorage "bog.core.access-token" "")
                      (assoc state :access-token nil
                                   :message (:message body)
                                   :view :login
                                   :info "You were logged out"))
      (= status 500) (assoc state :message (:message body) :info "")
      (= status 200) (do
                      (go (>! res-ch {:status status :body body}))
                      (assoc state :info "")))))

(defn draft-list-res-ch [state res]
  (assoc state :drafts (:body res)))

(defn create-draft-ch [state val]
  (let [{:keys [content title req-ch res-ch]} val
        draft {:title title
               :content content
               :id (str (random-uuid))
               :sort_order 0
               :published false
               :type :post}]
    (go (>! req-ch {:url "/api/drafts"
                    :method :post
                    :body draft
                    :res-ch res-ch}))
    (assoc state :info "Creating draft...")))

(defn create-draft-res-ch [state res]
  (let [{:keys [status body]} res]
    (if (= status 200)
      (do
        (. js/window.history (pushState "" "" "/drafts"))
        (-> state
            (assoc :info "" :view :draft-list)
            (update-in [:drafts] conj body)))
      (assoc state :info "" :message (:message body)))))
