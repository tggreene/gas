(ns examples.todos.gas
  (:require [cljs.core.async :as async]
            [clojure.set]
            [reagent.core :as reagent]
            [reagent.core :as r]))

(defn pad-vector
  ([v n]
   (pad-vector v n nil))
  ([v n value]
   (let [current (count v)]
     (into [] (concat v (replicate (- n current) value))))))

(defn safe-update-in
  "Like update-in but proactively pads vectors with nils if they're referenced"
  ([m [k & ks] f]
   (if (and (vector? m) (int? k) (> k (count m)))
     (let [new-m (pad-vector m (inc k))]
       (if ks
         (assoc new-m k (safe-update-in (get new-m k) ks f))
         (assoc new-m k (f (get new-m k)))))
     (if ks
       (assoc m k (safe-update-in (get m k) ks f))
       (assoc m k (f (get m k))))))
  ([m [k & ks] f a]
   (if (and (vector? m) (int? k) (> k (count m)))
     (let [new-m (pad-vector m (inc k))]
       (if ks
         (assoc new-m k (safe-update-in (get new-m k) ks f a))
         (assoc new-m k (f (get new-m k) a))))
     (if ks
       (assoc m k (safe-update-in (get m k) ks f a))
       (assoc m k (f (get m k) a)))))
  ([m [k & ks] f a b]
   (if (and (vector? m) (int? k) (> k (count m)))
     (let [new-m (pad-vector m (inc k))]
       (if ks
         (assoc new-m k (safe-update-in (get new-m k) ks f a b))
         (assoc new-m k (f (get new-m k) a b))))
     (if ks
       (assoc m k (safe-update-in (get m k) ks f a b))
       (assoc m k (f (get m k) a b)))))
  ([m [k & ks] f a b c]
   (if (and (vector? m) (int? k) (> k (count m)))
     (let [new-m (pad-vector m (inc k))]
       (if ks
         (assoc new-m k (safe-update-in (get new-m k) ks f a b c))
         (assoc new-m k (f (get new-m k) a b c))))
     (if ks
       (assoc m k (safe-update-in (get m k) ks f a b c))
       (assoc m k (f (get m k) a b c)))))
  ([m [k & ks] f a b c & args]
   (if (and (vector? m) (int? k) (> k (count m)))
     (let [new-m (pad-vector m (inc k))]
       (if ks
         (assoc new-m k (apply safe-update-in (get new-m k) ks f a b c args))
         (assoc new-m k (apply f (get new-m k) a b c args))))
     (if ks
       (assoc m k (apply safe-update-in (get m k) ks f a b c args))
       (assoc m k (apply f (get m k) a b c args))))))

(defprotocol Message
  (process-message [message app]
    "Given a message, take the current app state and
                   return the new one. In essense this is a reducing
                   function."))

(defprotocol EventSource
  (watch-channels [message app]))

(defn wrap
  "Apply a function to every element that comes out of a channel.

  (This is fmap for channels)."
  [f in]
  (async/pipe in (async/chan 1 (map f))))

(defn forward
  "Apply a function to every element that goes into a channel.

  (This is contramap for channels)."
  [f from]
  (let [to (async/chan)]
    (async/go-loop []
      (when-let [message (async/<! to)]
        (async/>! from (f message))
        (recur)))
    to))

(defn start-message-loop!
  ([state render-fn]
   (start-message-loop! state render-fn #{}))

  ([state render-fn initial-channels]
   (let [ui-channel (async/chan)
         channels (atom (conj initial-channels ui-channel))]

     (add-watch state
                :render
                (fn [_ _ old-state new-state]
                  (render-fn ui-channel new-state)))

     (swap! state identity)

     (async/go-loop []
       (when-let [cs (seq @channels)]
         ;; (js/console.log cs)
         (let [[message channel] (async/alts! cs)]
           (js/console.log message)

           (try
             (when (nil? message)
               (swap! channels disj channel))

             (when (satisfies? Message message)
               (swap! state #(process-message message %)))

             (when (satisfies? EventSource message)
               (swap! channels clojure.set/union (watch-channels message @state)))
             (catch js/Error e
               (js/console.log "Gas Error! Somewhere a message implementation is broken!")
               (js/console.log message)
               (js/console.log e))))
         (recur))))))

(defn start-channel-loop!
  "An implementation of the nip message loop without implicit
  creation of the core ui-channel"
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
    (async/put! channel message)
    (.stopPropagation dom-event)))

(defn send-value!
  "Send information from the user to the message queue.

  Similar to `send!`, except the message-fn will be called with the message's value first."
  [channel message-fn]
  (fn [dom-event]
    (->> dom-event
         get-event-value
         message-fn
         (async/put! channel))
    (.stopPropagation dom-event)))


(defn process-submessage
  [submessage state path]
  (if (satisfies? Message submessage)
    (safe-update-in state path #(process-message submessage %))
    state))

(defn watch-subchannels
  [submessage state path wrapper]
  (when (satisfies? EventSource submessage)
    (->> (get-in state path)
         (watch-channels submessage)
         (map #(wrap wrapper %)))))

(declare ->Submessage)
(defrecord Submessage [path message]
  Message
  (process-message [_ state]
    (process-submessage message state path))
  EventSource
  (watch-channels [_ state]
    (watch-subchannels message state path #(->Submessage path %))))

(defn submessage
  [path message]
  (->Submessage path message))

(defn submessenger
  [path]
  (fn [message]
    (submessage path message)))

(defn subcomponent
  [component ui-channel path & args]
  (into [component (forward (partial ->Submessage path) ui-channel)] args))

(defn wrap-to
  "Wrap outbound messages in a submessage"
  [path channel]
  (wrap (submessenger path) channel))

(defn forward-to
  "Wrap incoming messages in a submessage"
  [path channel]
  (forward (submessenger path) channel))

