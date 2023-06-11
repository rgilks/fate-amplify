(ns app.character-parser
  (:require [app.character-sample :as sample]
            [app.schema :as schema]
            [app.util :as util]
            [cljs.pprint :refer [pprint]]
            [clojure.string :as str]
            [instaparse.core :as insta]
            [malli.core :as m]))

(def character-parser
  (insta/parser
   "character = name aspects skills stunts stresses fate background
      name = <'**'>? #\"[^*\n]+\" <'**'>? <'\n'>? <'\n'> 
      aspects = aspect* <'\n'>
      aspect = <'- **'> type <':** *'> phrase <'*\n'>
      <type> = 'High Concept' | 'Trouble' | 'Relationship' | 'Other Aspects'
      <phrase> = #\"[^*]+\"
      skills = <'**Skills:**\n'> rank* <'\n'>
      rank = <'- **'> rank-name <' (+'> <rank-value> <'):** '> (skill-name (<', '> skill-name)*) <'\n'>
      <rank-name> = #\"[^ ]+\"
      <rank-value> = #\"[^\\)]+\"
      <skill-names> = skill-name (<', '> skill-name)*
      <skill-name> = #\"[^\n,]+\"
      stunts = <'**Stunts:**\n'> stunt* <'\n'>
      stunt = <'- **'> stunt-name <'**: '> stunt-desc <'\n'>
      <stunt-name> = #\"[^*]+\" 
      <stunt-desc> = #\"[^*\n]+\" 
      stresses = <'**Stress:**\n'> stress* <'\n'>
      stress = <'- **'> stress-type <'**: '> stress-amount <'/'> stress-amount <'\n'>
      <stress-type> = 'Physical' | 'Mental' 
      <stress-amount> = #'\\d'
      fate = <'**FATE:** '> fate-amount <'/'> fate-amount <'\n\n'>
      <fate-amount> = #'\\d'
      background = <'**Background:**\n\n'> #\"[^\\z]+\""))

(defn transform-character [char]
  (let [[_ [_ name] aspects skills stunts stresses fate background] char]
    {:name  name
     :aspects (mapv
               #(hash-map :type (util/kebab-case (second %)) :phrase (last %))
               (rest aspects))
     :skills (vec
              (apply concat
                     (map #(map
                            (fn [skill] {:name skill :rating (second %)})
                            (drop 2 %))
                          (rest skills))))
     :stunts (mapv #(hash-map :name (second %) :description (last %))
                   (rest stunts))
     :stress (apply merge
                    (map
                     #(hash-map
                       (-> % second str/lower-case keyword)
                       {:current (util/parse-int (nth % 2))
                        :total (util/parse-int (last %))})
                     (rest stresses)))
     :fate {:current (util/parse-int (second fate))
            :refresh (util/parse-int (last fate))}
     :background (second background)}))

(def misha
  "**Mikhail \"Misha\" Petrov**

- **High Concept:** *Disillusioned Ex-KGB Operative*
- **Trouble:** *Haunted by Old Demons*
- **Relationship:** *Guardian Bear of the Bookworm*
- **Other Aspects:** *Tough as Siberian Winter*

**Skills:**
- **Great (+4):** Fight
- **Good (+3):** Investigate, Will
- **Fair (+2):** Athletics, Notice, Physique
- **Average (+1):** Deceive, Stealth, Provoke, Academics

**Stunts:**
- **Iron Fist**: When Misha successfully lands a hit using Fight, he can spend a fate point to add 2 more shifts to the result, representing the brutal impact of his blows.
- **Petrov's Grit**: Once per session, when Misha would be taken out, he can stay in the fight with 1 physical stress remaining. This represents his stubbornness and resilience, refusing to go down easily.
- **Cold War Interrogator**: Misha uses his Fight skill instead of Provoke for intimidating in interrogation scenes, representing his aggressive and physically intimidating style.

**Stress:**
- **Physical**: 4/4
- **Mental**: 6/6

**FATE:** 3/3

**Background:**

Mikhail Petrov, better known as Misha, is an ex-KGB operative. With the collapse of the USSR, he became a man without a country, a relic of the old world trying to find his place in the new one. In this new world, he found an unexpected partner in Luc Chevalier, a French intellectual with a mind as sharp as Misha's fists are strong. Despite their stark differences, they've found a unique balance — Misha handles the field, while Luc deciphers codes and provides crucial intel from the safety of his office.

Haunted by the ghosts of the Cold War, Misha has found comfort in his relationship with Luc, whose view of the world is starkly different from his own. Their partnership has taught him to appreciate the quieter, more cerebral side of their work, even if he sometimes finds himself missing the adrenaline of the frontline. Their mission, however complex, seems more manageable knowing Luc's brilliant mind is backing him up.")

(comment
  (-> misha character-parser pprint)

  (-> misha character-parser transform-character pprint)

  (m/validate
   schema/character
   (-> misha character-parser transform-character)))
