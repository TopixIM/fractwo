
(ns app.client
  (:require ["pixi.js" :as PIXI]
            [phlox.core :refer [render!]]
            ["fontfaceobserver" :as FontFaceObserver]
            [phlox.cursor :refer [update-states]]
            [app.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [app.schema :as schema]
            [app.config :as config]
            [ws-edn.client :refer [ws-connect! ws-send!]]
            [recollect.patch :refer [patch-twig]]
            [cumulo-util.core :refer [on-page-touch]]
            ["url-parse" :as url-parse]
            [applied-science.js-interop :as j])
  (:require-macros [clojure.core.strint :refer [<<]]))

(declare dispatch!)

(declare connect!)

(declare simulate-login!)

(defonce *states (atom {:states {:cursor []}}))

(defonce *store (atom nil))

(defn simulate-login! []
  (let [raw (.getItem js/localStorage (:storage-key config/site))]
    (if (some? raw)
      (do (println "Found storage.") (dispatch! :user/log-in (read-string raw)))
      (do (println "Found no storage.")))))

(defn dispatch! [op op-data]
  (when (and config/dev? (not= op :states)) (comment println "Dispatch" op op-data))
  (case op
    :states (reset! *states (update-states @*states op-data))
    :effect/connect (connect!)
    (ws-send! {:kind :op, :op op, :data op-data})))

(defn connect! []
  (let [url-obj (url-parse js/location.href true)
        host (or (j/get-in url-obj [:query :host]) js/location.hostname)
        port (or (j/get-in url-obj [:query :port]) (:port config/site))]
    (ws-connect!
     (<< "ws://~{host}:~{port}")
     {:on-open (fn [] (simulate-login!)),
      :on-close (fn [event] (reset! *store nil) (js/console.error "Lost connection!")),
      :on-data (fn [data]
        (case (:kind data)
          :patch
            (let [changes (:data data)]
              (when config/dev? (comment js/console.log "Changes" (clj->js changes)))
              (reset! *store (patch-twig @*store changes)))
          (println "unknown kind:" data)))})))

(def global-fonts
  (js/Promise.all
   (array (.load (FontFaceObserver. "Josefin Sans")) (.load (FontFaceObserver. "Hind")))))

(defn render-app! [swap?]
  (render! (comp-container (:states @*states) @*store) dispatch! {:swap? swap?}))

(defn main! []
  (println "Running mode:" (if config/dev? "dev" "release"))
  (-> global-fonts (.then (fn [] (render-app! false))))
  (add-watch *store :change (fn [] (render-app! false)))
  (add-watch *states :change (fn [] (render-app! false)))
  (connect!)
  (on-page-touch #(if (nil? @*store) (connect!)))
  (println "App started!"))

(def mount-target (.querySelector js/document ".app"))

(defn reload! [] (render-app! true) (println "Code updated."))
