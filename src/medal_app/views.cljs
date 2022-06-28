(ns medal-app.views
  (:require [re-frame.core :as re-frame]
            [medal-app.subs :as subs]
            [medal-app.routes :as routes]
            [medal-app.events :as events]))

(defn medal-row [i {:keys [code gold silver bronze]}]
  [:tr {:key code
        :on-click #(re-frame/dispatch [::events/navigate [:medal-edit :id code]])}
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
        sort-order @(re-frame/subscribe [::subs/sort-order])
        sorted-medals @(re-frame/subscribe [::subs/sorted-medals])]
    [:div
      (when @loading? "Loading...")
        [:div
         [:table  
          [:thead  
           [:tr  
            [:th]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "gold"])
                  :class (if (= @sort-order "gold") "active" "")}
             [:div.legend-gold]]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "silver"])
                  :class (if (= @sort-order "silver") "active" "")}
             [:div.legend-silver]]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "bronze"])
                  :class (if (= @sort-order "bronze") "active" "")}
             [:div.legend-bronze]]
            [:th {:on-click #(re-frame/dispatch [::events/sort-medals "total"])
                  :class (if (= @sort-order "total") "active" "")}
              "Total"]]]
          [:tbody
           (map-indexed medal-row @sorted-medals)]]]]))


;; nav

(defn navbar []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [:div
     [:ul.nav
      [:li {:class (if (= @active-panel :home-panel) "current" "") }
       [:a {:on-click #(re-frame/dispatch [::events/navigate [:home]])} "Home"]]
      [:li {:class (if (= @active-panel :medals-panel) "current" "")}
       [:a {:on-click #(re-frame/dispatch [::events/navigate [:medals]])} "Medals"]]]]))

;; medal

(defn medals-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.container
     [:h2 @name]
     [medal-body]]))

(defmethod routes/panels :medals-panel [] [medals-panel])

;; home

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.container
     [:h2 "This is the Home Screen of Medal count widget"]]))

(defmethod routes/panels :home-panel [] [home-panel])

;; (defn main-panel []
;;   (re-frame/dispatch [::events/fetch-medals])
;;   [medal-body])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
  (re-frame/dispatch [::events/fetch-medals])
    [:div
     [navbar]
    [:div (routes/panels @active-panel)]]))