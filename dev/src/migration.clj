(ns migration
  (:require [clojure.string :as string]
            [clojure.java.jdbc :as jdbc]
            [ragtime.jdbc]
            [ragtime.repl :as ragtime])
  )

(def conn
  "DB 연결 정보"
  {:dbtype   "mysql"
   :dbname   "ragtime_test"
   :user     "root"
   :password "0000"
   :host     "localhost"
   :port     "3306"})

(defn get-migration-config
  []
  {:datastore  (ragtime.jdbc/sql-database conn)
   :migrations (ragtime.jdbc/load-resources "migrations")})

(defn migrate-up!
  "실행되지 않은 모든 마이그레이션을 실행"
  []
  (ragtime/migrate (get-migration-config)))

(defn rollback!
  "가장 최근에 실행된 마이그레이션 1개를 롤백"
  []
  (ragtime/rollback (get-migration-config)))

(defn get-migration-count
  "이전에 몇개의 마이그레이션이 실행되었는지 확인"
  []
  (try
    (let [query ["SELECT COUNT(id) AS count FROM ragtime_migrations"]
          result (jdbc/query conn query)]
      (:count (first result)))
    (catch java.sql.SQLSyntaxErrorException e
      (println (class e))
      (if (string/includes? (.getMessage e) "ragtime_migrations' doesn't exist")
        0
        (throw e)))))

(defn migrate-down!
  "모든 마이그레이션을 롤백"
  []
  (let [num-migrations (get-migration-count)]
    (dotimes [_ num-migrations]
      (rollback!))))