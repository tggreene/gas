(ns examples.hydra.core
  (:require [gas.core :as gas]
            [reagent.core :as reagent]
            [examples.counter.core :as counter]
            [examples.counter2.core :as counter2]
            [examples.spotify.core :as spotify]
            [examples.hydra.processing]
            [examples.hydra.view :as view]))

(def initial-state
  {:counter    counter/initial-state
   :counter2a counter2/initial-state
   :counter2b counter2/initial-state
   :spotify    spotify/initial-state})

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
