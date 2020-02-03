(ns examples.spotify.core
  (:require [gas.core :as gas]
            [reagent.core :as reagent]
            [examples.spotify.processing]
            [examples.spotify.view :as view]))

(def initial-state
  {:term ""})

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
