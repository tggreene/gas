(ns examples.todos.messages
  (:require [examples.todos.gas :refer [Message EventSource]]))

(defrecord AddTodo [title]
  Message
  (process-message [_ app]
    (update app :items conj {:title text
                             :completed false
                             :editing false})))

(defrecord UpdateNewTodo [text]
  Message
  (process-message [_ app]
    (assoc app :new-todo text)))

(defrecord ToggleAll [checked]
  Message
  (process-message [_ app]
    (update app :items (fn [items]
                         (map #(update % :completed checked) items)))))
