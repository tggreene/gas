(ns bip-examples.counter.processing
  (:require [bip.core :refer [Message]]
            [bip-examples.counter.messages :as m]))

(extend-protocol Message
  m/Decrement
  (process-message [_ app]
    (update app :counter dec))

  m/Increment
  (process-message [_ app]
    (update app :counter inc)))
