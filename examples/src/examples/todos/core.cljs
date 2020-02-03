(ns examples.todos.core
  (:require [examples.todos.gas :as gas]
            [examples.todos.routing :as gas-routing]
            [reagent.core :as reagent]
            [examples.todos.routes :refer [frontend-routes]]
            [examples.todos.view :as view]
            [cljs.core.async :as async]))

(def initial-state
  {:view nil
   :todos nil})

(defonce state
  (reagent/atom initial-state))

;; figwheel reload-hook
(defn reload-hook
  []
  (swap! state identity))

(defn render-fn
  [ui-channel current-state]
  (reagent/render-component
   [view/root ui-channel current-state]
   (js/document.getElementById "app")))

(defn ^:export main
  []
  (enable-console-print!)
  (let [ui-channel (async/chan)]
    (gas/start-message-loop! state
                             render-fn
                             #{(gas-routing/init frontend-routes)})))
