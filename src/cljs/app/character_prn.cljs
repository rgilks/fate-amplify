(ns app.character-prn
  (:require [clojure.string :as str]))

(defn prn-aspects [aspect-type aspects]
  (str/join
   ", "
   (map #(str "*" (:phrase %) "*")
        (filter #(= aspect-type (:type %)) aspects))))

(defn prn-skills [skills]
  (let [ratings {"Legendary" "8" "Epic" "7" "Fantastic" "6" "Superb" "5" "Great" "4" "Good" "3" "Fair" "2" "Average" "1"}]
    (str/join
     "\n"
     (for [[rating value] ratings
           :let [skill-names (->> skills
                                  (filter #(= rating (:rating %)))
                                  (map :name)
                                  (str/join ", "))]
           :when (not-empty skill-names)]
       (str "- **" rating " (+" value "):** " skill-names)))))

(defn prn-stunts [stunts]
  (str/join
   ""
   (map #(str "- **" (:name %) "**: " (:description %) "\n")
        stunts)))

(defn prn-stress [stress]
  (str
   "- **Physical**: "
   (:current (:physical stress)) "/" (:total (:physical stress)) "\n"
   "- **Mental**: "
   (:current (:mental stress)) "/" (:total (:mental stress)) "\n"))

(defn prn-fate [fate]
  (str (:current fate) "/" (:refresh fate)))

(defn prn-character [character]
  (let [{:keys [name aspects skills stunts stress fate background notes]} character]
    (str
     "**" name "**\n"
     "\n- **High Concept:** " (prn-aspects "high-concept" aspects)
     "\n- **Trouble:** " (prn-aspects "trouble" aspects)
     "\n- **Relationship:** " (prn-aspects "relationship" aspects)
     "\n- **Other Aspects:** " (prn-aspects "other" aspects)
     "\n\n**Skills:**\n" (prn-skills skills)
     "\n\n**Stunts:**\n" (prn-stunts stunts)
     "\n**Stress:**\n" (prn-stress stress)
     "\n**FATE:** " (prn-fate fate)
     "\n\n"
     (when background
       (str "**Background:**\n\n" background "\n\n"))
     (when notes
       (str "**Notes:**\n\n" notes "\n\n")))))

(comment
  (def mikhail-petrov
    {:id #uuid "550e8400-e29b-41d4-a716-446655440000"
     :name "Mikhail \"Misha\" Petrov"
     :aspects [{:type "high-concept" :phrase "Disillusioned Ex-KGB Operative"}
               {:type "trouble" :phrase "Haunted by Old Demons"}
               {:type "relationship" :phrase "Guardian Bear of the Bookworm"}
               {:type "other" :phrase "Tough as Siberian Winter"}
               {:type "other" :phrase "\"The Pen is Mightier than the Sword, but I Prefer My Fist\""}]
     :skills [{:name "Fight" :rating "Great"}
              {:name "Investigate" :rating "Good"}
              {:name "Will" :rating "Good"}
              {:name "Athletics" :rating "Fair"}
              {:name "Notice" :rating "Fair"}
              {:name "Physique" :rating "Fair"}
              {:name "Deceive" :rating "Average"}
              {:name "Stealth" :rating "Average"}
              {:name "Provoke" :rating "Average"}
              {:name "Academics" :rating "Average"}]
     :stunts [{:name "Iron Fist" :description "When Misha successfully lands a hit using Fight, he can spend a fate point to add 2 more shifts to the result, representing the brutal impact of his blows."}
              {:name "Petrov's Grit" :description "Once per session, when Misha would be taken out, he can stay in the fight with 1 physical stress remaining. This represents his stubbornness and resilience, refusing to go down easily."}
              {:name "Cold War Interrogator" :description "Misha uses his Fight skill instead of Provoke for intimidating in interrogation scenes, representing his aggressive and physically intimidating style."}]
     :stress {:physical {:current 4 :total 4}
              :mental {:current 6 :total 6}}
     :fate {:current 3 :refresh 3}
     :background "Mikhail Petrov, better known as Misha, is an ex-KGB operative. With the collapse of the USSR, he became a man without a country, a relic of the old world trying to find his place in the new one. In this new world, he found an unexpected partner in Luc Chevalier, a French intellectual with a mind as sharp as Misha's fists are strong. Despite their stark differences, they've found a unique balance — Misha handles the field, while Luc deciphers codes and provides crucial intel from the safety of his office.\n\nHaunted by the ghosts of the Cold War, Misha has found comfort in his relationship with Luc, whose view of the world is starkly different from his own. Their partnership has taught him to appreciate the quieter, more cerebral side of their work, even if he sometimes finds himself missing the adrenaline of the frontline. Their mission, however complex, seems more manageable knowing Luc's brilliant mind is backing him up."})

  (println (prn-character mikhail-petrov)))