(ns app.form
  (:require
   [refx.alpha :as refx]
   [uix.core :refer [$ defui]]
   ["@mui/material" :as mui]
   ["@rjsf/validator-ajv6" :default validator]
   ["@rjsf/mui" :default Form]

   [malli.json-schema :as json-schema]))

(def cols [12, 6, 4, 3, 2, 2])

(defn object-field-template
  [^js js-props]
  (let [properties (.-properties js-props)
        xs         (or (get-in cols [(dec (or (count properties) 1))]) 12)]
    ($ mui/Grid {:container true :spacing 0}
       (map-indexed
        (fn [i property]
          ($ mui/Grid
             {:key (str "grid-el-" i) :item true :xs xs}
             (.-content property)))
        properties))))

(refx/reg-sub
 ::character-fields
 (fn [db [_ id fields]]
   (select-keys (get-in db [:characters id]) fields)))

(defui grid-form
  [{:keys [id id-prefix fields validator schema]}]
  (let [data (refx/use-sub [::character-fields id fields])]
    ($ Form
       {:idPrefix        id-prefix
        :fields          fields
        :formData        data
        :schema          schema
        :validator       validator
        :templates       #js {:ObjectFieldTemplate object-field-template}
        :onChange        #() ;;#(refx/dispatch [::events/update :character id (.-formData %)])
        :liveValidate    true
        :noHtml5Validate true
        :showErrorList   false}
       ($ :div))))

(defn sub-schema [schema fields]
  (into [:map]
        (filter
         (fn [x]
           (when (seqable? x)
             (some
              #(= % (first x)) fields))) schema)))

(defui multi-section
  [{:keys [id disabled schema sections]}]
  ($ :div
     (for [{:keys [auto-complete fields]} sections]
       (let [id-prefix (str id "-" (-> fields first name))]
         ($ mui/Box {:key   (str "form-section-" id-prefix)
                     :style {:margin-bottom "20px"}}
            ($ grid-form
               {:id            id
                :id-prefix     id-prefix
                :disabled      disabled
                :auto-complete auto-complete
                :schema        (json-schema/transform (sub-schema schema fields))
                :spacing       2
                :fields        fields
                :validator     validator}))))))



