(ns bog.schemas.post-schema
  (:require [schema.core :as s]
            [schema.coerce :as coerce]
            [bog.schemas.user-schema :as user-schema]))

(def PostRequest {:user user-schema/User
                  :title s/Str
                  :content s/Str
                  :type (s/enum :post :quote :video :slideshow)
                  :sort-order s/Int})

(def parse-request
  (coerce/coercer PostRequest coerce/json-coercion-matcher))
