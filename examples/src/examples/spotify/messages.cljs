(ns examples.spotify.messages)

(defrecord ChangeSearchTerm [term])

(defrecord Search [])

(defrecord SearchResults [body])
