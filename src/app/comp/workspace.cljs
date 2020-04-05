
(ns app.comp.workspace
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [phlox.core
             :refer
             [defcomp >> container hslx text rect circle create-list graphics g]]
            [app.config :refer [dev?]]
            [app.schema :as schema]
            [app.config :as config]
            [phlox.comp.drag-point :refer [comp-drag-point]]
            [phlox.comp.button :refer [comp-button]]
            [app.math :refer [add-path subtract-path]]))

(defcomp
 comp-workspace
 (states lines)
 (let [cursor (:cursor states), state (or (:data states) {:point [80 40]})]
   (container
    {}
    (comp-button
     {:text "Add", :position [320 20], :on-click (fn [e d!] (d! :workspace/add-line nil))})
    (create-list
     :container
     {}
     (->> lines
          (map
           (fn [[k line-config]]
             [k
              (let [base-point (or (:base line-config) [100 0])]
                (container
                 {:position base-point}
                 (comp-drag-point
                  (>> states (str :base k))
                  {:position base-point,
                   :hide-text? true,
                   :radius 4,
                   :unit 0.5,
                   :on-change (fn [position d!]
                     (println "p" position)
                     (d! :workspace/move-line-base [k position]))})
                 (create-list
                  :container
                  {}
                  (->> (:points line-config)
                       (map-indexed
                        (fn [idx point]
                          [idx
                           (comp-drag-point
                            (>> states (str k :point idx))
                            {:position (add-path base-point point),
                             :hide-text? true,
                             :radius 4,
                             :on-change (fn [position d!]
                               (d!
                                :workspace/move-line-point
                                [k idx (subtract-path position base-point)]))})]))))
                 (graphics
                  {:position base-point,
                   :ops (concat
                         [(g :move-to [0 0])
                          (g
                           :line-style
                           {:color (hslx 0 0 90), :width (:width line-config), :alpha 1})]
                         (->> (:points line-config) (map (fn [point] (g :line-to point)))))})))])))))))
