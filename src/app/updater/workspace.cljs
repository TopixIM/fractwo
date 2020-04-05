
(ns app.updater.workspace
  (:require [bisection-key.util :refer [assoc-append]]
            [bisection-key.core :as bisection]
            [app.schema :as schema]
            [app.math :refer [add-path]]))

(defn add-line [db op-data sid op-id op-time]
  (assoc-in
   db
   [:lines op-id]
   (merge schema/line-config {:base [(+ 200 (rand-int 20)) (+ 100 (rand-int 20))]})))

(defn add-line-point [db op-data sid op-id op-time]
  (let [line-id op-data]
    (update-in
     db
     [:lines line-id :points]
     (fn [points]
       (conj points (add-path (peek points) [(+ 20 (rand-int 20)) (+ 12 (rand-int 20))]))))))

(defn move-line-base [db op-data sid op-id op-time]
  (let [[line-id position] op-data] (update-in db [:lines line-id :base] (fn [_] position))))

(defn move-line-point [db op-data sid op-id op-time]
  (let [[k idx position] op-data] (update-in db [:lines k :points idx] (fn [_] position))))

(defn reduce-line-point [db op-data sid op-id op-time]
  (let [line-id op-data, that-points (get-in db [:lines line-id :points])]
    (if (>= (count that-points) 1)
      (update-in db [:lines line-id :points] (fn [points] (pop points)))
      (update db :lines (fn [lines] (dissoc lines line-id))))))

(defn toggle-line-width [db op-data sid op-id op-time] db)
