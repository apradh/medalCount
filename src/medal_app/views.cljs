(ns medal-app.views
  (:require
   [re-frame.core :as re-frame]
   [medal-app.subs :as subs]
   [medal-app.events :as events]
   ))

(defn medal-body [{:keys [code gold silver bronze]}]
  [:tr {:key code}
   [:td
    [:span "1"]
    [:span
     [:span {:class (str "flag-" code)}]]
    [:span {:class "country"} code]]
   [:td gold]
   [:td silver]
   [:td bronze]
   [:td (+ gold silver bronze)]])

(defn main-panel []
  (let [loading (re-frame/subscribe [::subs/loading])
        ; name (re-frame/subscribe [::subs/name])
        medals (re-frame/subscribe [::subs/medals])
        sortOrder (re-frame/subscribe [::subs/sortOrder])]
    [:div
    ;  [:h1 "Hello from " @name]
      (when @loading "Loading...")
      ; (println @medals)
      [:div {:class "container"}
        [:h2 "MEDAL COUNT"]
        [:table  
          [:thead  
            [:tr  
              [:th]
              [:th {:on-click #(re-frame/dispatch [::events/sort-medals "gold" @medals]) :class (if (= @sortOrder "gold") "active" "")}
              [:div.legend-gold]]
              [:th {:on-click #(re-frame/dispatch [::events/sort-medals "silver" @medals]) :class (if (= @sortOrder "silver") "active" "")}
              [:div.legend-silver]]
              [:th {:on-click #(re-frame/dispatch [::events/sort-medals "bronze" @medals]) :class (if (= @sortOrder "bronze") "active" "")}
              [:div.legend-bronze]]
              [:th 
              ; {:on-click #(re-frame/dispatch [::events/sort-medals "total" @medals]) :class (if (= @sortOrder "total") "active" "")} 
              "Total"]]]
          [:tbody  
      (map medal-body @medals)]]
      [:button {:on-click #(re-frame/dispatch [::events/fetch-medals])} "Make API Call"]
      ; [:button {:on-click #(re-frame/dispatch [::events/update-name "Asit"])} "Update Name"]
      ]]))
