(ns frontend.views
(:require
   [re-frame.core :as re-frame]
   [frontend.events :as events]
   [frontend.subs :as subs]
   ))


;; dispatch , event-handling , effect handling
(defn navigation [visibility]
  [:ul
   (if (= visibility :sign-up)
     [:li {:on-click #(re-frame/dispatch [::events/toggle-visibility])} "Login"]
     [:li {:on-click #(re-frame/dispatch [::events/toggle-visibility])} "Sign-up"])])


(defn login []
  [:div
   [:h2 "Login Page"]
   [:div
    [:div
     [:label "User name : "]
     [:input {:id "username" :type "text" :required true}]]
    [:div
     [:label "Password : "]
     [:input {:id "password" :type "text" :required true}]]
    [:input {:type "button" :value "Login"}]]])


(defn sign-up []
  [:div
   [:div
    [:h2 "Sign up Page"]
    [:div
     [:div
      [:label "User name : "]
      [:input {:id "username" :type "text" :required true}]]
     [:div
      [:label "Password : "]
      [:input {:id "password" :type "text" :required true}]]
     [:input {:type "button" :value "Sign-up"}]]]])


(defn text []
  [:div
   [:label "Write something"]
   [:textarea {:id "text"}]])


;; The page contains 3 components
(defn main-panel []
  (let [visibility @(re-frame/subscribe [::subs/page-state])]
    [:div
     [navigation visibility]
     (condp = visibility
       :sign-up [sign-up]
       :login   [login]
       [text])]))
