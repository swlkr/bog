(ns bog.controllers.files-controller
  (:require [bog.utils :as utils]
            [environ.core :refer [env]]
            [aws.sdk.s3 :as s3]))

(defn upload! [file access-key secret-key]
  (let [{:keys [filename content-type tempfile]} file
        cred {:access-key access-key :secret-key secret-key}
        _ (s3/put-object cred "com.adventurewalker.photos" filename tempfile {:content-type (or content-type "image/jpeg")} (s3/grant :all-users :read))]
    {:url (str "https://s3.amazonaws.com/com.adventurewalker.photos/" filename)}))


(defn create! [params]
  (-> params
      :file
      (upload! (env :access-key) (env :secret-key))
      (utils/ring-response)))
