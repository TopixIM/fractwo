
(ns app.comp.container
  (:require [respo-ui.core :as ui]
            [phlox.core :refer [defcomp container >> hslx text rect circle]]
            [app.config :refer [dev?]]
            [app.schema :as schema]
            [app.config :as config]
            [app.comp.workspace :refer [comp-workspace]]
            [app.comp.reel :refer [comp-reel comp-status]]))

(defcomp
 comp-container
 (states store)
 (let [state (:data states)
       session (:session store)
       router (:router store)
       router-data (:data router)]
   (container
    {}
    (comp-status store [40 (- js/window.innerHeight 52)])
    (when dev?
      (comp-reel {:position [200 (- js/window.innerHeight 60)], :size (:reel-length store)}))
    (if (nil? store)
      (text
       {:text "Client is offline...",
        :position [120 60],
        :style {:font-family ui/font-fancy,
                :fill (hslx 0 0 50),
                :font-size 40,
                :font-weight 300}})
      (container {} (comp-workspace (>> states :workspace) (:lines store)))))))
