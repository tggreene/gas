(ns bip-examples.counter.core
  (:require [bip.core :as bip]
            [reagent.core :as reagent]
            [bip-examples.counter.processing]
            [bip-examples.counter.view :as view]))

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
  (bip/start-message-loop! !app render-fn))