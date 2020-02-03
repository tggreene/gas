(ns examples.pages.core
  (:require [gas.core :as gas]
            [gas.routing :as gas-routing]
            [reagent.core :as reagent]
            [examples.pages.routes :refer [frontend-routes]]
            [examples.pages.view :as view]))

(def initial-state
  {:view nil})

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
  (gas/start-message-loop! !app
                              render-fn
                              #{(gas-routing/init frontend-routes)}))
