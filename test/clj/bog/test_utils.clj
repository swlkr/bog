(ns bog.test-utils
  (:require [ring.mock.request :as mock]
            [clojure.data.json :as json]))

(defn build-request [& {:keys [url method body]}]
  (-> (mock/request method url)
      (mock/body (json/write-str body))
      (mock/content-type "application/json")))
