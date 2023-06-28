(ns app.editor
  (:require
   ["@tiptap/core" :as tiptap-core]
   ["@tiptap/extension-collaboration" :as collaboration]
   ["@tiptap/react" :as tiptap]
   ["@tiptap/starter-kit" :as starter-kit]
   ["y-webrtc" :as y-webrtc]
   ["yjs" :as y]
   [uix.core :refer [$ defui]]))

(def ydoc (y/Doc.))

(y-webrtc/WebrtcProvider.
 "example-document" ydoc
 (clj->js {:signaling ["wss://35em0ihz5c.execute-api.eu-west-1.amazonaws.com/production"]}))

(def editor
  (tiptap-core/Editor.
   #js {:extensions
        [(.configure starter-kit/default #js {:history false})
         (.configure collaboration/default #js {:document ydoc})]}))

(defui view []
  ($ :div
     {:style {:border "1px solid red"}}
     ($ tiptap/EditorContent {:editor editor})))
