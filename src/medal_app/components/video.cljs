(ns medal-app.components.video
  (:require [re-frame.core :as re-frame]
            ["video.js" :as videojs]
            [reagent.core :as r]
            [reagent.dom :as rdom]))
(defn video [options]
    (r/create-class
     {:component-did-update (fn [component] (videojs(:id options)))
      :reagent-render (fn [options]
                        [:video {:id (:id options)
                                 :class (:class options)
                                 :controls (:controls options)
                                 :preload (:preload options)
                                 :width (:width options)
                                 :height (:height options)
                                 :poster (:poster options)
                                 :data-setup (:data-setup options)}
                                 :autoplay true
                         [:source {:src (:mp4 options) :type "video/mp4"}]
                         [:source {:src (:webm options) :type "video/webm"}]])}))