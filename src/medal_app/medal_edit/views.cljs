(ns medal-app.medal-edit.views
  (:require [re-frame.core :as re-frame]
            [medal-app.medal-edit.subs :as subs]
            [medal-app.subs :as root-subs]
            [medal-app.routes :as routes]))


(defn medal-edit []
  (let [route-params @(re-frame/subscribe [::root-subs/route-params])
        medal @(re-frame/subscribe [::subs/medal (:id route-params)])]
    [:div
     [:h1 (str "Edit medals" )]
     [:form
      [:lable (str "Country: " (:code medal))]
      [:br]
      [:lable "Gold "]
      [:input {:type "number" :value (:gold medal)}]
      [:br]
      [:lable "Silver "]
      [:input {:type "number" :value (:silver medal)}]
      [:br]
      [:lable "Bronze "]
      [:input {:type "number" :value (:bronze medal)}]
      [:br]
      [:input {:type "submit" :value "Submit"} ]]]))

(defmethod routes/panels :medal-edit-panel [] [medal-edit])
