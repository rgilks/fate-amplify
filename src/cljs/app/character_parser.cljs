(ns app.character-parser
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [cljs.reader :as reader]
            [clojure.string :as str]
            [malli.core :as m]))

(defn parse-aspects [aspects-str]
  (let [[_ high-concept trouble other-aspects] (re-matches #"^\*\*High Concept:\*\* \*(.*)\*\n\*\*Trouble:\*\* \*(.*)\*\n\*\*Other Aspects:\*\* \*(.*)\*" aspects-str)
        other-aspects-list (clojure.string/split other-aspects #", ")]
    (concat
     [{:type "high-concept" :phrase high-concept}
      {:type "trouble" :phrase trouble}]
     (map (fn [aspect] {:type "other" :phrase aspect}) other-aspects-list))))

(defn parse-skills [skills-str]
  (let [skill-lines (clojure.string/split skills-str #"\n")
        skill-regex #"- (.*): (.*)"]
    (map (fn [skill-line]
           (let [[_ level skills] (re-matches skill-regex skill-line)
                 skills-list (clojure.string/split skills #", ")]
             (map (fn [skill] {:name skill :rating level}) skills-list))) skill-lines)))

(defn parse-stunts [stunts-str]
  (let [stunt-lines (clojure.string/split stunts-str #"\n")
        stunt-regex #"- (.*)\: (.*)"]
    (map (fn [stunt-line]
           (let [[_ name description] (re-matches stunt-regex stunt-line)]
             {:name name :description description})) stunt-lines)))

(defn parse-stress [stress-str]
  (let [[_ physical mental] (re-matches #"^\*\*Stress:\*\* Physical (.*)\, Mental (.*)" stress-str)]
    {:physical {:total physical :current physical}
     :mental {:total mental :currentÍ mental}}))

(defn parse-character [markdown-str]
  (let [[_ name aspects-str skills-str stunts-str stress-str refresh-str] (re-matches #"^\*\*(.*)\*\*\n\n(.*)\n\n\*\*Skills:\*\*\n\n(.*)\n\n\*\*Stunts:\*\*\n\n(.*)\n\n(.*)\n\n\*\*Refresh:\*\* (.*)" markdown-str)]
    {:name name
     :aspects (parse-aspects aspects-str)
     :skills (flatten (parse-skills skills-str))
     :stunts (parse-stunts stunts-str)
     :stress (parse-stress stress-str)
     :fate {:refresh (read-string refresh-str) :current (read-string refresh-str)}}))

(defn fetch-and-parse [url]
  (go
    (let [response (<! (http/get url))
          markdown-str (:body response)
          character (parse-character markdown-str)]
      (if (m/validate character-schema character)
        character
        (throw (js/Error. "Invalid character data"))))))
