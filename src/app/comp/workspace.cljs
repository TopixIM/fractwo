
(ns app.comp.workspace
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [phlox.core :refer [defcomp >> container hslx text rect circle create-list]]
            [app.config :refer [dev?]]
            [app.schema :as schema]
            [app.config :as config]
            [phlox.comp.drag-point :refer [comp-drag-point]]
            [phlox.comp.button :refer [comp-button]]))

(defcomp
 comp-workspace
 (states points)
 (let [cursor (:cursor states), state (or (:data states) {:point [80 40]})]
   (container
    {}
    (comp-button
     {:text "Add", :position [320 20], :on-click (fn [e d!] (d! :workspace/add-point nil))})
    (rect
     {:position [0 0],
      :size [120 40],
      :line-style {:color (hslx 0 80 60), :alpha 1, :width 2}}
     (text
      {:text "drag here to remove..",
       :position [4 10],
       :style {:fill (hslx 0 80 100), :font-size 12}}))
    (create-list
     :container
     {}
     (->> points
          (map
           (fn [[k point]]
             [k
              (container
               {}
               (comp-drag-point
                (>> states k)
                {:position point,
                 :radius 4,
                 :hide-text? true,
                 :on-change (fn [position d!] (d! :workspace/update-point [k position]))}))])))))))
