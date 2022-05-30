(ns medal-app.views
  (:require [re-frame.core :as re-frame]
            [medal-app.subs :as subs]
            [medal-app.events :as events]))

(defn medal-row [i {:keys [code gold silver bronze]}]
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
  (let [loading? (re-frame/subscribe [::subs/loading?])
        medals (re-frame/subscribe [::subs/medals])
        sort-order (re-frame/subscribe [::subs/sort-order])]
    [:div
      (when @loading? "Loading...")
        [:div {:class "container"}
         [:h2 "MEDAL COUNT"]
         [:table  
          [:thead  
           [:tr  
            [:th]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "gold" @medals])
                  :class (if (= @sort-order "gold") "active" "")}
             [:div.legend-gold]]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "silver" @medals])
                  :class (if (= @sort-order "silver") "active" "")}
             [:div.legend-silver]]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "bronze" @medals])
                  :class (if (= @sort-order "bronze") "active" "")}
             [:div.legend-bronze]]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "total" @medals])
                  :class (if (= @sort-order "total") "active" "")}
              "Total"]]]
          [:tbody
           (map-indexed medal-row @medals)]]]]))

(defn main-panel []
  (re-frame/dispatch [::events/fetch-medals])
  [medal-body])