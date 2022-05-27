(ns medal-app.views
  (:require
   [re-frame.core :as re-frame]
   [medal-app.subs :as subs]
   [medal-app.events :as events]
   ))

(defn medal-list [i {:keys [code gold silver bronze]}]
  [:tr {:key code}
   [:td
    [:span (+ i 1)]
    [:span
     [:span {:class (str "flag-" code)}]]
    [:span {:class "country"} code]]
   [:td gold]
   [:td silver]
   [:td bronze]
   [:td (+ gold silver bronze)]])

(defn medal-body []
  (let [loading (re-frame/subscribe [::subs/loading])
        medals (re-frame/subscribe [::subs/medals])
        sortOrder (re-frame/subscribe [::subs/sortOrder])]
    [:div
      (when @loading "Loading...")
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
              [:th {:on-click #(re-frame/dispatch [::events/sort-medals "total" @medals]) :class (if (= @sortOrder "total") "active" "")} "Total"]]]
          [:tbody  
      (map-indexed medal-list @medals)]]]]))

(defn main-panel []
  (re-frame/dispatch [::events/fetch-medals])
  [medal-body])