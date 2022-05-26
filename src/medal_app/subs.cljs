(ns medal-app.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::loading
 (fn [db]
   (:loading db)))

(re-frame/reg-sub
 ::medals
 (fn [db]
   (:medals db)))

(re-frame/reg-sub
 ::sortOrder
 (fn [db]
   (:sortOrder db)))
