(ns medal-app.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::loading?
 (fn [db]
   (:loading? db)))

(re-frame/reg-sub
 ::medals
 (fn [db]
   (:medals db)))

;(re-frame/reg-sub
; ::sorted-medals
; (fn [db]
;   (let [medals (re-frame/subscribe [::medals])
;         sort-order (re-frame/subscribe [::sort-order])
;         tie-breaker (if (= @sort-order "gold") :silver :gold)]
; (:sorted-medals db (reverse (sort-by (juxt (keyword @sort-order) tie-breaker)  @medals))))))

(re-frame/reg-sub
 ::sorted-medals
 :<- [::medals]
 :<- [::sort-order]
 (fn [[medals sort-order] _]
   (let [tie-breaker (if (= sort-order "gold") :silver :gold)]
     (reverse (sort-by (juxt (keyword sort-order) tie-breaker)  medals)))))

(re-frame/reg-sub
 ::sort-order
 (fn [db]
   (:sort-order db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (get-in db [:route :panel])))

(re-frame/reg-sub
 ::route-params
 (fn [db _]
   (get-in db [:route :route :route-params])))
