(ns examples.multicounters.core
  (:require [gas.core :as gas]
            [reagent.core :as reagent]
            [examples.counter2.core :as counter2]
            [examples.multicounters.processing]
            [examples.multicounters.view :as view]))

(def initial-state
  {:counters [counter2/initial-state
              counter2/initial-state]})

(defonce !app
  (reagent/atom initial-state))

;; figwheel reload-hook
(defn reload-hook
  []
  (swap! !app identity))

(defn render-fn
  [ui-channel app]
  (reagent/render-component [view/root ui-channel app]
                            (js/document.getElementById "app")))

(defn ^:export main
  []
  (enable-console-print!)
  (gas/start-message-loop! !app render-fn))
