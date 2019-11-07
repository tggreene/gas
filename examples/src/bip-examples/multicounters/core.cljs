(ns bip-examples.multicounters.core
  (:require [bip.core :as bip]
            [reagent.core :as reagent]
            [bip-examples.counter2.core :as counter2]
            [bip-examples.multicounters.processing]
            [bip-examples.multicounters.view :as view]))

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
  (bip/start-message-loop! !app render-fn))
