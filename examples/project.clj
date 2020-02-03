(defproject bip-examples "0.1.0"
  :description "bip examples"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/core.async "0.4.500"]
                 [bidi "2.1.6"]
                 [com.cemerick/url "0.1.1"]
                 [kibu/pushy "0.3.8"]
                 [bip "0.1.0"]
                 [reagent "0.9.0-rc2"]
                 [cljs-http "0.1.46"]
                 [cider/piggieback "0.4.2"]
                 [figwheel-sidecar "0.5.19" :exclusions [nrepl]]
                 [binaryage/devtools "0.9.10"]]

  :source-paths ["src"]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.19"]]

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :cljsbuild {:builds {:counter {:source-paths ["src"]
                                 :figwheel {:on-jsload "bip-examples.counter.core/reload-hook"}
                                 :compiler {:main bip-examples.counter.core
                                            :asset-path "js/counter/compiled/out"
                                            :output-to "resources/public/js/counter/compiled/counter.js"
                                            :output-dir "resources/public/js/counter/compiled/out"
                                            :optimizations :none}}
                       :counter2 {:source-paths ["src"]
                                  :figwheel {:on-jsload "bip-examples.counter2.core/reload-hook"}
                                  :compiler {:main bip-examples.counter2.core
                                             :asset-path "js/counter2/compiled/out"
                                             :output-to "resources/public/js/counter2/compiled/counter2.js"
                                             :output-dir "resources/public/js/counter2/compiled/out"
                                             :optimizations :none}}
                       :spotify {:source-paths ["src"]
                                 :figwheel {:on-jsload "bip-examples.spotify.core/reload-hook"}
                                 :compiler {:main bip-examples.spotify.core
                                            :asset-path "js/spotify/compiled/out"
                                            :output-to "resources/public/js/spotify/compiled/spotify.js"
                                            :output-dir "resources/public/js/spotify/compiled/out"
                                            :optimizations :none}}
                       :hydra {:source-paths ["src"]
                               :figwheel {:on-jsload "bip-examples.hydra.core/reload-hook"}
                               :compiler {:main bip-examples.hydra.core
                                          :asset-path "js/hydra/compiled/out"
                                          :output-to "resources/public/js/hydra/compiled/hydra.js"
                                          :output-dir "resources/public/js/hydra/compiled/out"
                                          :optimizations :none}}
                       :pages {:source-paths ["src"]
                               :figwheel {:on-jsload "bip-examples.pages.core/reload-hook"}
                               :compiler {:main bip-examples.pages.core
                                          :asset-path "js/pages/compiled/out"
                                          :output-to "resources/public/js/pages/compiled/pages.js"
                                          :output-dir "resources/public/js/pages/compiled/out"
                                          :optimizations :none}}
                       :todos {:source-paths ["src"]
                               :figwheel {:on-jsload "bip-examples.todos.core/reload-hook"}
                               :compiler {:main bip-examples.todos.core
                                          :preloads [devtools.preload]
                                          :asset-path "js/todos/compiled/out"
                                          :output-to "resources/public/js/todos/compiled/todos.js"
                                          :output-dir "resources/public/js/todos/compiled/out"
                                          :optimizations :none}}
                       :multicounters {:source-paths ["src"]
                                       :figwheel {:on-jsload "bip-examples.multicounters.core/reload-hook"}
                                       :compiler {:main bip-examples.multicounters.core
                                                  :asset-path "js/multicounters/compiled/out"
                                                  :output-to "resources/public/js/multicounters/compiled/multicounters.js"
                                                  :output-dir "resources/public/js/multicounters/compiled/out"
                                                  :optimizations :none}}}})
