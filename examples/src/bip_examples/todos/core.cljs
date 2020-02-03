(ns bip-examples.todos.core
  (:require [bip-examples.todos.bip :as bip]
            [bip-examples.todos.routing :as bip-routing]
            [reagent.core :as reagent]
            [bip-examples.todos.routes :refer [frontend-routes]]
            [bip-examples.todos.view :as view]
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
    (bip/start-message-loop! state
                             render-fn
                             #{(bip-routing/init frontend-routes)})))
