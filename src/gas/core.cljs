(ns gas.core
  (:require [cljs.core.async :as async :refer [alts! put! pipe chan <! >!]]
            [clojure.set :as set])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn wrap
  "Apply a function to every element that comes out of a channel"
  [f in]
  (pipe in (chan 1 (map f))))

(defn forward
  "Apply a function to every element that goes into a channel"
  [f from]
  (let [to (chan)]
    (go-loop []
      (when-let [message (<! to)]
        (>! from (f message))
        (recur)))
    to))

(defprotocol Message
  (process-message [message app]
                   "Given a message, take the current app state and
                   return the new one. In essense this is a reducing
                   function."))

(defprotocol EventSource
  (watch-channels [message app]))

(defn process-submessage
  [submessage app path]
  (when (satisfies? Message submessage)
    (update-in app path #(process-message submessage %))))

(defn watch-subchannels
  [submessage app path wrapper]
  (when (satisfies? EventSource submessage)
    (->> (get-in app path)
         (watch-channels submessage)
         (map #(wrap wrapper %)))))

(defn- get-event-value
  "Given a DOM event, return the value it yields. This abstracts over
  the needless inconsistencies of the DOM."
  [event]
  (let [target (.-target event)
        type (.-type target)]
    (condp contains? type
      #{"checkbox"}
      (.-checked target)

      #{"text" "email" "password" "number" "radio" "textarea" "select-one" "select-multiple"
        "date" "datetime" "datetime-local" "week" "month"
        "range" "search" "tel" "time" "url" "color"}
      (.-value target))))

(defn send!
  "Send information from the user to the message queue.
  The message must be a record which implements the Message protocol."
  [channel message]
  (fn [dom-event]
    (put! channel message)
    (.stopPropagation dom-event)))

(defn send-value!
  "Send information from the user to the message queue.

  Similar to `send!`, except the message-fn will be called with the message's value first."
  [channel message-fn]
  (fn [dom-event]
    (->> dom-event
         get-event-value
         message-fn
         (put! channel))
    (.stopPropagation dom-event)))

(defn start-message-loop!
  [intial-channels data-atom]
  (let [channels (atom intial-channels)]
    (async/go-loop []
      (when-let [cs (seq @channels)]
        (let [[message channel] (alts! cs)]

          (try
            (when (nil? message)
              (swap! channels disj channel))

            (when (satisfies? Message message)
              (swap! data-atom #(process-message message %)))

            (when (satisfies? EventSource message)
              (swap! channels clojure.set/union (watch-channels message @data-atom)))
            (catch js/Error e
              (js/console.log "Gas Error! Somewhere a message implementation is broken!")
              (js/console.log message)
              (js/console.log e))))
        (recur)))))

(declare ->Submessage)
(defrecord Submessage [path message]
  Message
  (process-message [_ state]
    (process-submessage message state path))
  EventSource
  (watch-channels [_ state]
    (watch-subchannels message state path #(->Submessage path %))))

(defn submessage
  "Wrap a message as a submessage with a given path within the application
  state"
  [path message]
  (->Submessage path message))

(defn submessenger
  "Create a function which wraps messages as submessages to a particular path
  within the application state"
  [path]
  (fn [message]
    (submessage path message)))

(defn forward-to
  "Wrap incoming messages in a submessage"
  [path channel]
  (forward (submessenger path) channel))

(defn subcomponent
  "Wrap a component using gas.core/forward-to such that any messages passed via
  a component are wrapped to a particular path within the application state"
  [component ui-channel path & args]
  (into [component (forward-to path ui-channel)] args))
