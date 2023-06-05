(ns app.schema
  (:require
   [clojure.pprint :refer [pprint]]
   [clojure.test.check.generators :as gen]
   [malli.generator :as mg]
   [malli.json-schema :as json-schema]
   [miner.strgen :as sg]))

(def game
  [:map
   {:title "Game"}
   [:id {:title "Id"} uuid?]
   [:slug {:title   "Slug"
           :gen/gen (sg/string-generator #"^[0-9a-z]{1}[-0-9a-z]{2,35}$")
           :json-schema/errorMessage
           #js {:type      "Must be a string."
                :minLength "Must be at least 3 characters long."
                :maxLength "Must be shorter than 36 characters."
                :pattern   "Must be all lowercase separated by dashes."}}
    [:and [:string {:min 3, :max 36}] [:re #"^[0-9a-z](-?[0-9a-z])*$"]]]
   [:name {:title "Name"} string?]
   [:owner {:title "Owner"} string?]
   [:allowSpectators {:title "Allow Spectators"} boolean?]])

(def player
  [:map
   {:title "Player"}
   [:id {:title "Id"} uuid?]
   [:owner {:title "Owner"} string?]
   [:gameID {:title "Game ID"} uuid?]])

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

(def character
  [:map
   {:title "Character"}
   [:id {:title "Id"} uuid?]
   [:name
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
  (mg/generate player)
  (json-schema/transform player)

  (mg/generate game)
  (json-schema/transform game)

  (gen/sample (sg/string-generator #"^[0-9a-z](-?[0-9a-z])*$"))
  (gen/sample (sg/string-generator #"^[0-9a-z]{1}[-0-9a-z]{2,35}$"))

  (pprint (mg/generate character))
  (js/JSON.stringify (clj->js (json-schema/transform character))))