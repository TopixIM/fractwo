
(ns app.updater
  (:require [app.updater.session :as session]
            [app.updater.user :as user]
            [app.updater.router :as router]
            [app.schema :as schema]
            [respo-message.updater :refer [update-messages]]
            [app.updater.workspace :as workspace]))

(defn updater [db op op-data sid op-id op-time]
  (let [f (case op
            :session/connect session/connect
            :session/disconnect session/disconnect
            :session/remove-message session/remove-message
            :user/log-in user/log-in
            :user/sign-up user/sign-up
            :user/log-out user/log-out
            :router/change router/change
            :workspace/add-line workspace/add-line
            :workspace/move-line-point workspace/move-line-point
            :workspace/move-line-base workspace/move-line-base
            :workspace/add-line-point workspace/add-line-point
            :workspace/reduce-line-point workspace/reduce-line-point
            :workspace/toggle-line-width workspace/toggle-line-width
            (do (println "Unknown op:" op) identity))]
    (f db op-data sid op-id op-time)))
