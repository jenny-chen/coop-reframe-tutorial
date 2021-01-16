(ns frontend.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [frontend.events :as events]
   [frontend.views :as views]
   [frontend.config :as config]
   ))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (mount-root))
