(ns medal-app.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [medal-app.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::update-name
  (fn [db [_ val]]
    (assoc db :name val)))

(re-frame/reg-event-fx                             ;; note the trailing -fx
  ::fetch-medals                                    ;; usage:  (dispatch [:handler-with-http])
  (fn [{:keys [db]} _]                             ;; the first param will be "world"
    {:db   (assoc db :loading true)                ;; causes the twirly-waiting-dialog to show??
     :http-xhrio {:method          :get
                  :uri             "./server/medals.json"
                  :timeout         8000                                           ;; optional see API docs
                  :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                  :on-success      [::fetch-medals-success]
                  :on-failure      [::fetch-medals-failure]}}))

(re-frame/reg-event-db
  ::fetch-medals-success
  (fn [db [_ data]]
    (def medalData (map (fn [d]
      (let [{:keys [gold silver bronze]} d]
        (assoc d :total (+ gold silver bronze))
    )) data))
    (-> db
      (assoc :loading false)
      (assoc :medals (reverse(sort-by (juxt :gold :silver) medalData))))))

(re-frame/reg-event-db
  ::fetch-medals-failure
  (fn [db [_ data]]
    (println "data")
    db))

(re-frame/reg-event-db
 ::sort-medals
 (fn [db [_ val data]]
  (def tieBreaker (if (= val "gold") (keyword "silver") (keyword "gold")))
  (-> db
    (assoc :sortOrder val)
    (assoc :medals (reverse (sort-by (juxt (keyword val) tieBreaker)  data))))))