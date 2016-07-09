(ns bog.schemas
  (:require [schema.core :as s]
            [schema.coerce :as coerce]))

(def CommentRequest {:post-id s/Int
                     :name s/Str
                     :content s/Str})

(def parse-comment-request
  (coerce/coercer CommentRequest coerce/json-coercion-matcher))

(def RequiredEnvVariables {:database-url s/Str
                           :secret s/Str})

(def Token {:id s/Int
            (s/optional-key :iss) s/Str
            (s/optional-key :exp) s/Num
            (s/optional-key :iat) s/Num})

(def PostRequest {:user Token
                  :title s/Str
                  :content s/Str
                  :type (s/enum :post :quote :video :slideshow)})

(def parse-post-request
 (coerce/coercer PostRequest coerce/json-coercion-matcher))

(def User {:id s/Int
           :email s/Str
           :password s/Str
           (s/optional-key :created_at) s/Str})

(def LoginRequest {:email s/Str
                   :password s/Str})

(def SignUpRequest {:email s/Str
                    :password s/Str
                    :password-confirm s/Str})