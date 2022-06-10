(ns medal-app.events
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [medal-app.db :as db]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::update-name
  (fn [db [_ val]]
    (assoc db :name val)))

(re-frame/reg-event-fx                             ;; note the trailing -fx
  ::fetch-medals                                   ;; usage:  (dispatch [:handler-with-http])
  (fn [{:keys [db]} _]                             ;; the first param will be "world"
    {:db   (assoc db :loading? true)               ;; causes the twirly-waiting-dialog to show??
     :http-xhrio {:method          :get
                  :uri             "http://localhost:8280/server/medals.json"
                  :timeout         8000                                           ;; optional see API docs
                  :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                  :on-success      [::fetch-medals-success]
                  :on-failure      [::fetch-medals-failure]}}))

(re-frame/reg-event-db
  ::fetch-medals-success
  (fn [db [_ data]]
    (let [medal-data (map (fn [d]
                       (let [{:keys [gold silver bronze]} d]
                         (assoc d :total (+ gold silver bronze)))) data)]
      (-> db
        (assoc :loading? false)
        (assoc :medals (reverse (sort-by (juxt :gold :silver) medal-data)))))))

(re-frame/reg-event-db
  ::fetch-medals-failure
  (fn [db [_ data]]
    (assoc db :loading? false)
    (println data)
    db))

(re-frame/reg-event-fx
 ::http-post
 (fn [db [_ req-data]]
   (println (str "req-data" req-data))
   {:db   (assoc db :loading? true)
    :http-xhrio {:method          :post
                 :uri             "http://localhost:8280/server/medals.json"
                 :params          req-data
                 :timeout         5000
                 :format          (ajax/json-request-format)
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [::success-post-result]
                 :on-failure      [::failure-post-result]}}))

(re-frame/reg-event-db
 ::success-post-result
 (fn [db [_ data]]
     (assoc db :loading? false)
     (println data)))

(re-frame/reg-event-db
 ::failure-post-result
 (fn [db [_ data]]
   (assoc db :loading? false)
   (println data)
   db))

(re-frame/reg-event-db
 ::sort-medals
 (fn [db [_ val data]]
  (let [tie-breaker (if (= val "gold") :silver :gold)]
    (-> db
      (assoc :sort-order val)
      (assoc :medals (reverse (sort-by (juxt (keyword val) tie-breaker)  data)))))))

; Events for Routes

(re-frame/reg-event-fx
 ::navigate
 (fn-traced [_ [_ handler]]
            {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
            {:db (assoc db :active-panel active-panel)}))

(re-frame/reg-event-fx
 ::set-route
 (fn-traced [{:keys [db]} [_ route]]
            {:db (assoc db :route route)}))


(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (assoc-in db [:form id] val)))