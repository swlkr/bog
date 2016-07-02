(ns bog.schemas.comment-schema
  (:require [schema.core :as s]
            [schema.coerce :as coerce]))

(def CommentRequest {:post-id s/Int
                     :name s/Str
                     :content s/Str})

(def parse-request
  (coerce/coercer CommentRequest coerce/json-coercion-matcher))
