(ns bip-examples.spotify.processing
  (:require [bip.core :refer [Message EventSource]]
            [bip-examples.spotify.messages :as m]
            [bip-examples.spotify.rest :as rest]))

(extend-protocol Message
  m/ChangeSearchTerm
  (process-message [{:keys [term]} app]
    (assoc app :term term)))

(extend-protocol Message
  m/Search
  (process-message [_ app] app))

(extend-protocol EventSource
  m/Search
  (watch-channels [_ {:keys [term]
             :as app}]
    #{(rest/search-songs term)}))

(extend-protocol Message
  m/SearchResults
  (process-message [response app]
    (assoc app :tracks (-> response :body :tracks :items))))
