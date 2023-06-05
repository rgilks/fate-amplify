(ns app.character
  (:require
   [app.form :as form]
   [app.schema :as schema]
   [uix.core :refer [$ defui]]
   [uix.dom]
   [promesa.core :as p]
   ["react-router-dom" :as router]))

(defui form []
  (let [id (.-id (router/useParams))]
    (println "id: " id)
    ($ :div
       {:style {:overflow-y "scroll"}}
       ($ :div
          {:key "id" :style {:color "white" :margin "18px" :height "1000px"}}
          (str "id: " id)
        ;; ($ form/multi-section
        ;;    {:id       id
        ;;     :schema   schema/character
        ;;     :disabled false
        ;;     :sections [{:fields []}]})
          ))))