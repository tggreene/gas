(ns petrol-examples.counter2.core
  (:require [petrol.core :as petrol]
            [reagent.core :as reagent]
            [petrol-examples.counter2.events]
            [petrol-examples.counter2.view :as view]))

(defonce !app
  (reagent/atom {:counter 0}))

;; figwheel reload-hook
(defn reload-hook
  []
  (swap! !app identity))

(defn render-fn
  [ui-events app]
  (reagent/render-component [view/root ui-events app]
                            js/document.body))

(defn ^:export main
  []
  (enable-console-print!)
  (petrol/start-event-loop !app render-fn))