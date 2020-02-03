(ns examples.todos.routes
  (:require [bidi.bidi :as bidi]
            [examples.todos.gas :refer [Message process-message]]
            [examples.todos.routing :as gas.routing :refer [UrlHistoryEvent]]))

(def frontend-routes
  ["/" {"" :frontpage
        "foo" :foo
        "bar" :bar
        "todos" :todos}])

(extend-protocol Message
  UrlHistoryEvent
  (process-message [{view :view} app]
    (prn 'UrlHistoryEvent view)
    (assoc app :view view)))

(defn href-for
  ([handler]
   (href-for handler {}))
  ([handler args]
   (str "#" (gas.routing/href-for frontend-routes handler args))))
