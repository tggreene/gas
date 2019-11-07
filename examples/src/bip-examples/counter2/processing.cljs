(ns bip-examples.counter2.processing
  (:require [bip.core :refer [Message]]
            [bip-examples.counter2.messages :as m]))

(extend-protocol Message
  m/ResetCounter
  (process-message [_ app]
    (assoc app :counter 0))

  m/ModifyCounter
  (process-message [{n :n} app]
    (update app :counter #(+ % n))))
