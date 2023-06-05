(ns app.routing
  (:require
   [refx.alpha :as refx]))

(refx/reg-event-fx
 ::check-access-by-slug
 (fn [{:keys [db]} [_ slug]]
   (let [username (:username db)]
     (println "CHECK ACCESS" slug username)
     {:db           (assoc db :slug slug)
      :check-access [slug username]})))
