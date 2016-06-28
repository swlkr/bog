(ns bog.db
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [environ.core :refer [env]]
            [clojure.string :refer [split]]
            [bog.utils :refer [dissoc-nil-values]]))

(defn get-database-uri [database-url]
  (if (nil? database-url)
    nil
    (java.net.URI. database-url)))

(defn get-user-and-password [database-uri]
  (if (nil? database-uri)
    nil
    (let [user-info (.getUserInfo database-uri)]
      (if (nil? user-info)
        nil
        (split user-info #":")))))

(defn get-database-spec [database-url]
  (let [database-uri (get-database-uri database-url)
        user-and-password (get-user-and-password database-uri)]
    (if (nil? database-uri)
      nil
      (dissoc-nil-values
        {:classname "org.postgresql.Driver"
         :subprotocol "postgresql"
         :user (get user-and-password 0)
         :password (get user-and-password 1)
         :subname (if (= -1 (.getPort database-uri))
                    (format "//%s%s" (.getHost database-uri) (.getPath database-uri))
                    (format "//%s:%s%s" (.getHost database-uri) (.getPort database-uri) (.getPath database-uri)))}))))

; database migrations
(defn load-config []
  (let [db-spec (get-database-spec (env :database-url))]
    {:datastore  (jdbc/sql-database db-spec)
     :migrations (jdbc/load-resources "migrations")}))

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))
