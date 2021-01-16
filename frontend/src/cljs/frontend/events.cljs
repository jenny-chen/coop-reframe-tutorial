(ns frontend.events
  (:require
   [re-frame.core :as re-frame]
   [frontend.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))


;; write event
(re-frame/reg-event-db
 ::toggle-visibility
 (fn [{:keys [visibility] :as db}  _]
   (cond-> db
     (= visibility :sign-up) (assoc :visibility :login)
     (= visibility :login)   (assoc :visibility :sign-up))))
