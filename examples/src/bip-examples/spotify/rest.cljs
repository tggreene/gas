(ns bip-examples.spotify.rest
  (:require [cljs-http.client :as http]
            [bip.core :as bip]
            [bip-examples.spotify.messages :as m]))

(defn search-songs
  [term]
  (->> (http/get "https://api.spotify.com/v1/search"
                 {:with-credentials? false
                  :query-params {:q term
                                 :type "track"}})
       (bip/wrap m/map->SearchResults)))
