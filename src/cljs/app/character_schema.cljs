(ns app.character-schema
  (:require
   [clojure.pprint :refer [pprint]]
   [malli.generator :as mg]
   [malli.json-schema :as json-schema]))

(def skill-names
  [:enum
   {:title "Skill Names"
    :json-schema/errorMessage
    #js {:enum "Must be one of the predefined Fate Condensed skills."}}
   "Athletics"
   "Burglary"
   "Contacts"
   "Crafts"
   "Deceive"
   "Drive"
   "Empathy"
   "Fight"
   "Investigate"
   "Lore"
   "Notice"
   "Physique"
   "Provoke"
   "Rapport"
   "Resources"
   "Shoot"
   "Stealth"
   "Will"])

(def character-schema
  [:map
   {:title "Character"}
   [:id {:title "Id"} uuid?]
   [:character-name
    {:title "Character Name"
     :json-schema/errorMessage
     #js {:minLength "Must be at least 3 character long."
          :maxLength "Must be shorter than 80 characters."}}
    [:string {:min 3 :max 80}]]
   [:aspects
    [:vector
     {:title "Aspects"
      :json-schema/errorMessage #js {:type "Must be a list of aspects."}}
     [:map
      [:type
       [:enum
        {:title "Aspect Type"
         :json-schema/errorMessage
         #js {:enum "Must be 'high-concept', 'trouble', 'relationship', or 'other'."}}
        "high-concept" "trouble" "relationship" "other"]]
      [:phrase
       {:title "Aspect Phrase"
        :json-schema/errorMessage
        #js {:minLength "Must be at least 1 character long."
             :maxLength "Must be shorter than 120 characters."}}
       [:string {:min 1 :max 120}]]]]]
   [:skills
    [:vector
     {:title "Skills"
      :json-schema/errorMessage
      #js {:type "Must be a list of skills."}}
     [:map
      [:name skill-names]
      [:rating
       [:enum
        {:title "Rating"
         :json-schema/errorMessage
         #js {:enum "Must be 'Legendary', 'Epic', 'Fantastic', 'Superb', 'Great', 'Good', 'Fair', 'Average'."}}
        "Legendary" "Epic" "Fantastic" "Superb" "Great" "Good" "Fair" "Average"]]]]]
   [:stunts
    [:vector
     {:title "Stunts"
      :json-schema/errorMessage #js {:type "Must be a list of stunts."}}
     [:map
      [:name
       {:title "Stunt Name"
        :json-schema/errorMessage
        #js {:minLength "Must be at least 1 character long."
             :maxLength "Must be shorter than 100 characters."}}
       [:string {:min 1 :max 100}]]
      [:description
       {:title "Stunt Description"
        :json-schema/errorMessage
        #js {:minLength "Must be at least 1 character long."
             :maxLength "Must be shorter than 300 characters."}}
       [:string {:min 1 :max 300}]]]]]
   [:stress
    [:map
     {:title "Stress"
      :json-schema/errorMessage #js {:type "Stress is required."}}
     [:physical
      [:map
       {:title "Physical"
        :json-schema/errorMessage
        #js {:type "Physical stress is required."}}
       [:total
        {:title "Total"
         :json-schema/errorMessage
         #js {:minimum "Must not be less than 1."
              :maximum "Must not exceed 10."}}
        [:int {:min 1 :max 10}]]
       [:current
        {:title "Current"
         :json-schema/errorMessage
         #js {:minimum "Must not be less than 0."
              :maximum "Must not exceed 10."}}
        [:int {:min 0 :max 10}]]]]
     [:mental
      [:map
       {:title "Mental"
        :json-schema/errorMessage #js {:type "Mental stress is required."}}
       [:total
        {:title "Total"
         :json-schema/errorMessage
         #js {:minimum "Must not be less than 1."
              :maximum "Must not exceed 10."}}
        [:int {:min 1 :max 10}]]
       [:current
        {:title "Current"
         :json-schema/errorMessage
         #js {:minimum "Must not be less than 0."
              :maximum "Must not exceed 10."}}
        [:int {:min 0 :max 10}]]]]]]
   [:fate
    [:map
     {:title "Fate Points"
      :json-schema/errorMessage
      #js {:type "Fate points are required."}}
     [:refresh
      {:title "Refresh"
       :json-schema/errorMessage
       #js {:minimum "Must not be less than 1."
            :maximum "Must not exceed 10."}}
      [:int {:min 1 :max 10}]]
     [:current
      {:title "Current Fate Points"
       :json-schema/errorMessage
       #js {:minimum "Must not be less than 0."
            :maximum "Must not exceed 10."}}
      [:int {:min 0 :max 10}]]]]
   [:background
    {:title "Background"
     :json-schema/errorMessage
     #js {:maxLength "Must be shorter than 2000 characters."}}
    [:string {:max 2000}]]
   [:notes
    {:title "Notes"
     :json-schema/errorMessage
     #js {:maxLength "Must be shorter than 2000 characters."}}
    [:string {:max 2000}]]])

(comment
  (pprint (mg/generate character-schema))
  (js/JSON.stringify (clj->js (json-schema/transform character-schema))))