(ns bog.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<! >! chan]]
            [bog.app :refer [dispatch! add-action]]))

(defn on-error [state body]
  (let [{:keys [message]} body]
    (assoc state :error message :loading false :submitting false)))

(add-action :on-error on-error)

(defn add-default-headers [state headers]
  (let [{:keys [access-token]} state]
    (if (not (empty? access-token))
      (merge (or headers {}) {"Authorization" access-token})
      (or headers {}))))

(defn send [state params]
  (let [{:keys [url method body headers]} params
        all-headers (add-default-headers state headers)]
    (condp = method
      :upload (http/post url {:multipart-params body :headers all-headers})
      :post   (http/post url {:json-params body :headers all-headers})
      :put    (http/put url {:json-params body :headers all-headers})
      :get    (http/get url {:headers all-headers})
      :delete (http/delete url {:headers all-headers}))))
