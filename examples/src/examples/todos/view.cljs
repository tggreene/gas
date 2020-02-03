(ns examples.todos.view
  (:require [examples.todos.gas :as gas]
            [examples.todos.messages :as messages]
            [examples.todos.routes :refer [href-for]]
            [clojure.string :as str]
            [reagent.core :as reagent]
            [cljs.core.async :as async]))

(defn todo
  [ui-channel state]
  [:li {:class (str/join " "
                         [(when (:completed state) "completed")
                          (when (:editing state) "editing")])}
   [:div {:class "view"}
    [:input {:class "toggle"
             :type "checkbox"
             :checked (:completed state)
             :onChange nil}]
    [:label {:onDoubleClick nil}
     (:title state)]
    [:button {:class "destroy"
              :onClick nil}]]
   [:input {:ref "editField"
            :class "edit"
            :value (:edit-text state)
            :onBlur nil
            :onChange nil
            :onKeyDown nil}]])

(defn footer
  [ui-channel state]
  (let [item-term (if (< 1 (count (:items state))) "items" "item")]
    [:footer {:class "footer"}
     [:span {:class "todo-count"}
      [:strong (str (count (:items state)) " " item-term " left")]]
     [:ul {:class "filters"}
      [:li [:a {:href "#/todos"} "All"]]
      [:li [:a {:href "#/todos"} "Active"]]
      [:li [:a {:href "#/todos"} "Completed"]]]
     (when-not (zero? (count (map :completed (:items state))))
       [:button {:class "clear-completed"
                 :onClick nil}
        "Clear Completed"])]))

(defn keyboard-submit
  [submit-fn]
  (fn [key-event]
    (case (aget key-event "keyCode")
      13 (submit-fn key-event)
      nil)))

(defn todos
  [ui-channel state]
  [:div {:id "todoapp"}
   [:header {:class "header"}
    [:h1 "todos"]
    [:input {:class "new-todo"
             :placeholder "What needs to be done?"
             :value (:new-todo state)
             :on-key-down (keyboard-submit #(async/put! ui-channel
                                                        (messages/->AddTodo (:new-todo state))))
             :on-change (gas/send-value! ui-channel messages/->UpdateNewTodo)
             :auto-focus true}]]
   [:section {:class "main"}
    [:input {:id "toggle-all"
             :class "toggle-all"
             :type "checkbox"
             :on-change nil
             :checked (some-fn true? (map :completed (:items state)))}]
    [:label {:htmlFor "toggle-all"}]
    [:ul {:class "todo-list"}
     (for [todo (:items state)]
       ^{:key (:title todo)}
       [todo ui-channel todo])]
    [footer {:ui-channel ui-channel
             :state state}]]])

(defn root
  [ui-channel state]
  [:div.container
   [:h1 "Routing Demo"]

   [:div.row
    [:div.col-xs-12.col-sm-4
     [:ul.list-group (for [[link title] [[(href-for :frontpage) "Home"]
                                         [(href-for :foo) "Foo"]
                                         [(href-for :bar) "Bar"]
                                         [(href-for :todos) "Todos"]]]
                       (with-meta
                         [:li.list-group-item [:a {:href link} title]]
                         {:key title}))]]

    [:div.col-xs-12.col-sm-8
     [:div.well
      (case (-> state :view :handler)
        :foo "This is the Foo page."
        :bar "This is the Bar page."
        :todos [gas/subcomponent todos ui-channel [:todos] (:todos state)]
        "Home")]]]


   [:h2 "Debugging app state:"]
   [:div [:code (pr-str state)]]])
