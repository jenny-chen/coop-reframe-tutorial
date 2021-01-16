(ns frontend.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::page-state
 #(:visibility %))
