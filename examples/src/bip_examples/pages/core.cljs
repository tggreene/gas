(ns bip-examples.pages.core
  (:require [bip.core :as bip]
            [bip.routing :as bip-routing]
            [reagent.core :as reagent]
            [bip-examples.pages.routes :refer [frontend-routes]]
            [bip-examples.pages.view :as view]))

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
  (bip/start-message-loop! !app
                              render-fn
                              #{(bip-routing/init frontend-routes)}))
