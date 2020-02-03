(ns examples.counter2.core
  (:require [gas.core :as gas]
            [reagent.core :as reagent]
            [examples.counter2.processing]
            [examples.counter2.view :as view]))

(def initial-state
  {:counter 0})

(defonce !app
  (reagent/atom initial-state))

;; figwheel reload-hook
(defn reload-hook
  []
  (swap! !app identity))

(defn render-fn
  [ui-channel app]
  (reagent/render-component [view/root ui-channel app]
                            js/document.body))

(defn ^:export main
  []
  (enable-console-print!)
  (gas/start-message-loop! !app render-fn))