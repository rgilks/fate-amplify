(ns app.character
  (:require
   ["@rjsf/mui" :default Form]
   ["@rjsf/validator-ajv6" :default validator]
   ["models" :as models]
   ["react-router-dom" :as router]
   [app.cofx :as cofx]
   [app.schema :as schema]
   [malli.json-schema :as json-schema]
   [refx.alpha :as refx]
   [uix.core :refer [$ defui]]))

(refx/reg-event-fx
 ::new-character
 [(refx/inject-cofx ::cofx/uuid)]
 (fn [{:keys [db uuid]} [_ item]]
   (println item)
   {:update-item
    [models/Character
     (assoc item
            :id "7c8bc344-7a9b-411b-9dae-c43523a64532"
            :owner (:username db)
            :gameID (:slug db))]}))

(defui form []
  (let [id (.-id (router/useParams))]
    (println "id: " id)
    ($ :div
       {:style {:overflow-y "scroll" :height "90%" :border "1px solid red"}}
       ($ :div
          {:key "id"
           :style {:color "white"
                   :margin "18px"
                   :height "1100px"}}
          ($ Form
             {:schema         (clj->js (json-schema/transform schema/character))
              :validator       validator
              :onChange        #(refx/dispatch
                                 [::new-character
                                  (js->clj (.-formData %) :keywordize-keys true)])
              :liveValidate    true
              :noHtml5Validate true
              :showErrorList   false}
             ($ :div))))))