
(ns app.updater.workspace
  (:require [bisection-key.util :refer [assoc-append]]
            [bisection-key.core :as bisection]
            [app.schema :as schema]))

(defn add-line [db op-data sid op-id op-time]
  (assoc-in
   db
   [:lines op-id]
   (merge schema/line-config {:base [(+ 200 (rand-int 20)) (+ 100 (rand-int 20))]})))

(defn add-line-point [db op-data sid op-id op-time] db)

(defn move-line-base [db op-data sid op-id op-time]
  (let [[line-id position] op-data] (update-in db [:lines line-id :base] (fn [_] position))))

(defn move-line-point [db op-data sid op-id op-time]
  (let [[k idx position] op-data] (update-in db [:lines k :points idx] (fn [_] position))))

(defn reduce-line-point [db op-data sid op-id op-time] db)

(defn toggle-line-width [db op-data sid op-id op-time] db)
