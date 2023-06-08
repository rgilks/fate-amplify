(ns app.character-parser
  (:require [app.character-sample :as sample]
            [app.schema :as schema]
            [cljs.pprint :refer [pprint]]
            [instaparse.core :as insta]
            [malli.core :as m]))

(def character-grammar
  "character = name aspects-list skills-list stunts-list stress-list fate-point
   name = '**' #\"[^*]+\" '**\n\n'
   aspects-list = aspect* '\n'
   aspect = '- **' aspect-type ':** *' aspect-desc '*\n'
   aspect-type = 'High Concept' | 'Trouble' | 'Relationship' | 'Other Aspects'
   aspect-desc = #\"[^*]+\" 
   skills-list = '**Skills:**\n' skill* '\n'
   skill = '- **' skill-level ':** ' skill-name '\n'
   skill-level = 'Great (+4)' | 'Good (+3)' | 'Fair (+2)' | 'Average (+1)'
   skill-name = #\"[^,]+\" <',' skill-name>?
   stunts-list = 'Stunts:' '\n' stunt*
   stunt = '- **' stunt-name ':** ' stunt-desc '\n'
   stunt-name = #\"[^:]+\" 
   stunt-desc = #\"[^*]+\" 
   stress-list = 'Stress:' '\n' stress*
   stress = '- **' stress-type ':** ' stress-amount '/4' '\n'
   stress-type = 'Physical' | 'Mental' 
   stress-amount = #'\\d'
   fate-point = '**FATE:**' fate-amount '/' fate-amount '+'
   fate-amount = #'\\d'")

(def character-parser
  (insta/parser character-grammar))

(defn parse-markdown [markdown-str]
  (let [parsed-data (character-parser markdown-str)]
    (if (insta/failure? parsed-data)
      (println (insta/get-failure parsed-data))
      parsed-data)))

(comment
  (def ex
    (insta/parser
     "sentence = token (<whitespace> token)*
     <token> = keyword / identifier
     whitespace = #'\\s+'
     identifier = #'[a-zA-Z]+'
     keyword = 'cond' | 'defn'"))

  (def c
    (insta/parser
     "character = name aspects skills
      name = <'**'> #\"[^*]+\" <'**\n\n'>
      aspects = aspect* <'\n'>
      aspect = <'- **'> type <':** *'> phrase <'*\n'>
      type = 'High Concept' | 'Trouble' | 'Relationship' | 'Other Aspects'
      phrase = #\"[^*]+\"
      skills = <'**Skills:**\n'> skill* <'\n'>
      skill = <'- **'> skill-level <':** '> skill-name <'\n'>
      skill-level = 'Great (+4)' | 'Good (+3)' | 'Fair (+2)' | 'Average (+1)'
      skill-name = #\"[^\n]+\""))

  (pprint (c "**Mikhail \"Misha\" Petrov**

- **High Concept:** *Disillusioned Ex-KGB Operative*
- **Trouble:** *Haunted by Old Demons*
- **Relationship:** *Guardian Bear of the Bookworm*
- **Other Aspects:** *Tough as Siberian Winter*

**Skills:**
- **Great (+4):** Fight
- **Good (+3):** Investigate, Will
- **Fair (+2):** Athletics, Notice, Physique
- **Average (+1):** Deceive, Stealth, Provoke, Academics

"))

  (let [character (parse-markdown sample/misha)]
    (pprint character)
    ;; (m/validate schema/character character)
    ))