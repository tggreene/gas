(ns bip-examples.pages.routes
  (:require [bip.core :refer [Message process-message]]
            [bip.routing :refer [UrlHistoryEvent]]))

(def frontend-routes
  ["" {"#" {"" :frontpage
            "foo" :foo
            "bar" :bar}}])

(extend-protocol Message
  UrlHistoryEvent
  (process-message [{view :view} app]
    (assoc app :view view)))

(defn href-for
  ([handler]
   (href-for handler {}))
  ([handler args]
   (bip.routing/href-for frontend-routes handler args)))
