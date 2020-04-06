
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
 comp-control
 (display position on-click)
 (rect
  {:position position, :size [12 12], :fill (hslx 0 0 20), :on {:click on-click}}
  (text
   {:position [2 0],
    :text display,
    :style {:fill (hslx 0 0 50), :font-size 12, :line-height 12, :font-family ui/font-code}})))

(defcomp
 comp-workspace
 (states lines)
 (let [cursor (:cursor states), state (or (:data states) {:point [80 40]})]
   (container
    {}
    (comp-button
     {:text "Add",
      :position [320 20],
      :on {:click (fn [e d!] (d! :workspace/add-line nil))}})
    (create-list
     :container
     {}
     (->> lines
          (map
           (fn [[k line-config]]
             [k
              (let [base-point (or (:base line-config) [100 0])
                    points (:points line-config)
                    head-key (first (sort (keys points)))
                    last-key (last (sort (keys points)))
                    head-point (get points head-key)
                    tail-point (get points last-key)]
                (container
                 {:position [0 0]}
                 (comp-drag-point
                  (>> states (str :base k))
                  {:position base-point,
                   :hide-text? true,
                   :radius 6,
                   :fill (hslx 0 80 20),
                   :on-change (fn [position d!] (d! :workspace/move-line-base [k position]))})
                 (comp-control
                  "+"
                  (add-path [8 -12] base-point)
                  (fn [e d!] (d! :workspace/add-line-point [k nil])))
                 (create-list
                  :container
                  {:position base-point}
                  (->> points
                       (map
                        (fn [[point-key point]]
                          [point-key
                           (container
                            {:position [0 0]}
                            (comp-drag-point
                             (>> states (str k :point point-key))
                             {:position point,
                              :hide-text? true,
                              :radius 4,
                              :on-change (fn [position d!]
                                (d! :workspace/move-line-point [k point-key position]))})
                            (comp-control
                             "+"
                             (add-path [8 -12] point)
                             (fn [e d!] (d! :workspace/add-line-point [k point-key])))
                            (comp-control
                             "-"
                             (add-path [8 8] point)
                             (fn [e d!] (d! :workspace/reduce-line-point [k point-key]))))]))))
                 (graphics
                  {:position base-point,
                   :ops (concat
                         [(g :move-to [0 0])
                          (g
                           :line-style
                           {:color (hslx 0 0 50), :width (:width line-config), :alpha 0.2})
                          (g :line-to head-point)
                          (g
                           :line-style
                           {:color (hslx 0 0 90), :width (:width line-config), :alpha 1})]
                         (->> points
                              (sort-by first)
                              (map (fn [[point-key point]] (g :line-to point)))))})))])))))))
