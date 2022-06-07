(ns medal-app.medal-edit.views
  (:require [re-frame.core :as re-frame]
            [medal-app.medal-edit.subs :as subs]
            [medal-app.subs :as root-subs]
            [medal-app.routes :as routes]))


(defn medal-edit []
  (let [route-params @(re-frame/subscribe [::root-subs/route-params])
        medal @(re-frame/subscribe [::subs/medal (:id route-params)])]
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
       [:input {:type "number" :value (:gold medal) :id "gold" :name "gold"}]
       [:br]
       [:lable "Silver "]
       [:input {:type "number" :value (:silver medal) :id "silver" :name "silver"}]
       [:br]
       [:lable "Bronze "]
       [:input {:type "number" :value (:bronze medal) :id "bronze" :name "bronze"}]
       [:br]
       [:input {:type "submit" :value "Submit"}]]]]))

(defmethod routes/panels :medal-edit-panel [] [medal-edit])
