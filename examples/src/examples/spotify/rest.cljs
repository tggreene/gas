(ns examples.spotify.rest
  (:require [cljs-http.client :as http]
            [gas.core :as gas]
            [examples.spotify.messages :as m]))

(defn search-songs
  [term]
  (->> (http/get "https://api.spotify.com/v1/search"
                 {:with-credentials? false
                  :query-params {:q term
                                 :type "track"}})
       (gas/wrap m/map->SearchResults)))
