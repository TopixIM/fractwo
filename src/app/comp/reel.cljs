
(ns app.comp.reel
  (:require [phlox.core :refer [defcomp container >> hslx text rect circle]]
            [respo-ui.core :as ui]))

(defcomp
 comp-reel
 (props)
 (let [position (or (:position props) [200 (- js/window.innerHeight 60)])
       size (:size props)]
   (container
    {:position position}
    (text
     {:text (str "Length: " size),
      :position [0 0],
      :style {:fill (hslx 0 0 100), :font-size 14, :font-family ui/font-fancy}})
    (rect
     {:position [90 0],
      :size [50 20],
      :fill (hslx 200 80 50),
      :on {:click (fn [e d!] (d! :reel/reset nil))}}
     (text
      {:text "Reset",
       :position [6 2],
       :style {:fill (hslx 0 0 100), :font-size 14, :font-family ui/font-fancy}}))
    (rect
     {:position [150 0],
      :size [50 20],
      :fill (hslx 200 80 50),
      :on {:click (fn [e d!] (d! :reel/merge nil))}}
     (text
      {:text "Merge",
       :position [6 2],
       :style {:fill (hslx 0 0 100), :font-size 14, :font-family ui/font-fancy}}))
    (rect
     {:position [210 0],
      :size [50 20],
      :fill (hslx 200 80 50),
      :on {:click (fn [e d!] (d! :effect/persist nil))}}
     (text
      {:text "Persist",
       :position [6 2],
       :style {:fill (hslx 0 0 100), :font-size 14, :font-family ui/font-fancy}})))))

(defcomp
 comp-status
 (store position)
 (container
  {:position (or position [40 (- js/window.innerHeight 52)])}
  (circle
   {:position [0 0],
    :radius 8,
    :fill (or (:color store) (hslx 0 0 32)),
    :on {:click (fn [e d!] (js/console.log (clj->js store)))}})
  (text
   {:position [20 -10],
    :text (str (:count store) " users online."),
    :style {:font-family ui/font-fancy,
            :fill (hslx 0 0 80),
            :font-size 16,
            :font-weight 500}})))
