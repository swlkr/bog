(ns bog.schemas.user-schema
  (:require [schema.core :as s]))

(def User {:id s/Int
           (s/optional-key :iss) s/Str
           (s/optional-key :exp) s/Num
           (s/optional-key :iat) s/Num})
