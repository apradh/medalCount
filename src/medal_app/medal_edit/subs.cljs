(ns medal-app.medal-edit.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::medal
 (fn [db [_ country-code]]
   (first (filter (fn [m] (= (:code m) country-code)) (:medals db)))))