(ns medal-app.medal-edit.views
  (:require [re-frame.core :as re-frame]
            [medal-app.medal-edit.subs :as subs]
            [medal-app.subs :as root-subs]
            [medal-app.routes :as routes]
            [medal-app.events :as events]))


(defn medal-edit []
  (let [route-params @(re-frame/subscribe [::root-subs/route-params])
        medal @(re-frame/subscribe [::subs/medal (:id route-params)])
        gold @(re-frame/subscribe [::subs/form :gold])
        silver @(re-frame/subscribe [::subs/form :silver])
        bronze @(re-frame/subscribe [::subs/form :bronze])]
    [:div.container
     [:div.form-container
      [:h1 (str "Edit medals")]
      [:form {:method "POST"}
       [:lable (str "Country: ")]
       [:span
        [:span {:class (str "flag-" (:code medal))}]]
       [:span {:class "country"} (:code medal)]
       [:br]
       [:lable "Gold "]
       [:input {:type "number" :id "gold" :name "gold"
                :value (if (not= gold nil) gold (:gold medal))
                :on-change #(re-frame/dispatch [::events/update-form :gold (-> % .-target .-value)])}]
       [:br]
       [:lable "Silver "]
       [:input {:type "number" :id "silver" :name "silver"
                :value (if (not= silver nil) silver (:silver medal))
                :on-change #(re-frame/dispatch [::events/update-form :silver (-> % .-target .-value)])}]
       [:br]
       [:lable "Bronze "]
       [:input {:type "number" :id "bronze" :name "bronze"
                :value (if (not= bronze nil) bronze (:bronze medal))
                :on-change #(re-frame/dispatch [::events/update-form :bronze (-> % .-target .-value)])}]
       [:br]
       [:input {:type "submit" :value "Submit"
                :on-click #(re-frame/dispatch [::events/fetch-medals])}]]]]))

(defmethod routes/panels :medal-edit-panel [] [medal-edit])
