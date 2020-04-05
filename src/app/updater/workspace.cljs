
(ns app.updater.workspace
  (:require [bisection-key.util :refer [assoc-append]] [bisection-key.core :as bisection]))

(defn add-point [db op-data sid op-id op-time]
  (if (empty? (:points db))
    (assoc db :points {bisection/mid-id [320 80]})
    (update
     db
     :points
     (fn [points] (assoc-append points [(+ 320 (rand-int 40)) (+ 80 (rand-int 30))])))))

(defn update-point [db op-data sid op-id op-time]
  (let [[k point] op-data]
    (update
     db
     :points
     (fn [points]
       (cond
         (and (< (first point) 120) (< (peek point) 40)) (dissoc points k)
         (some? (get points k)) (assoc points k point)
         :else points)))))
