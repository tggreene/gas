(ns examples.pages.routes
  (:require [gas.core :refer [Message process-message]]
            [gas.routing :refer [UrlHistoryEvent]]))

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
   (gas.routing/href-for frontend-routes handler args)))
