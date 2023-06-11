(ns app.editor
  (:require
   ["@tiptap/react" :as tiptap]
   ["@tiptap/starter-kit" :as starter-kit]
   [uix.core :refer [$ defui]]))

(defui view []
  (let [editor (tiptap/useEditor
                #js {:extensions [starter-kit/default]
                     :content "<p>Hello World!</p>"})]
    ($ tiptap/EditorContent {:editor editor})))