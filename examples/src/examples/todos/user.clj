(ns examples.todos.user
  (:require [figwheel-sidecar.repl-api]))

(comment
  (figwheel-sidecar.repl-api/start-figwheel! "todos")
  (figwheel-sidecar.repl-api/cljs-repl "todos"))
