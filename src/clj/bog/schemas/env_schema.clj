(ns bog.schemas.env-schema
  (:require [schema.core :as s]))

(def RequiredEnvVariables {:database-url s/Str
                           :secret s/Str})
