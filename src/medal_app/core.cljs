(ns medal-app.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as re-frame]
            [medal-app.events :as events]
            [medal-app.views :as views]
            [medal-app.routes :as routes]
            [medal-app.config :as config]
            [medal-app.medal-edit.views]))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
