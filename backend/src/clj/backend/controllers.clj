(ns backend.controllers
  (:require [backend.db.core :as db]))

;; function for password strength-check
(defn strong-password? [password] (> (count password) 4))


;; function for checking user-name availability
(defn username-available? [username]
  (not (.contains (or (db/get-user-names) []) username)))


;; function to sign up  user
(defn create-user [user-name pass]
  (let [{:keys [id]} (db/create-user! {:user-name user-name :pass pass})]
    (db/create-user-text! {:user-id id :text "Welcome to the middle of nowhere"})
    {:user-id id :text "Welcome to the middle of nowhere"}))


;; function to check if user exists
(defn user-in-system [user-name password]
  (db/get-user {:user-name user-name :pass password}))


;; function to update user text
(defn update-user-text [user-id text]
  (db/update-user-text! {:user-id user-id :text text}))

(defn get-user-text [user-id]
  (db/get-text {:user-id user-id}))
