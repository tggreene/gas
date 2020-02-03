(ns examples.hydra.view
  (:require [gas.core :refer [send! send-value! forward]]
            [examples.hydra.messages :as m]
            [examples.counter.view :as counter]
            [examples.counter2.view :as counter2]
            [examples.spotify.view :as spotify]))

(defn root
  [ui-channel app]
  [:div.container
   [:h1 "Everything"]

   [:div.row
    [:div.col-xs-12.col-sm-6
     [counter/root  (forward m/->Counter   ui-channel) (:counter app)]
     [counter2/root (forward m/->Counter2A ui-channel) (:counter2a app)]
     [counter2/root (forward m/->Counter2B ui-channel) (:counter2b app)]]

    [:div.col-xs-12.col-sm-6
     [spotify/root  (forward m/->Spotify   ui-channel) (:spotify app)]]]])
