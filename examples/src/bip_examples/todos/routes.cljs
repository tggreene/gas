(ns bip-examples.todos.routes
  (:require [bidi.bidi :as bidi]
            [bip-examples.todos.bip :refer [Message process-message]]
            [bip-examples.todos.routing :as bip.routing :refer [UrlHistoryEvent]]))

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
   (str "#" (bip.routing/href-for frontend-routes handler args))))
