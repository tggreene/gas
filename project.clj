(defproject gas "0.1.0"
  :description "A simple event-handling framework for ClojureScript projects."
  :url "https://github.com/tggreene/gas"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/core.async "0.4.500"]
                 [bidi "2.1.6"]
                 [com.cemerick/url "0.1.1"]
                 [kibu/pushy "0.3.8"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.9-SNAPSHOT"]]

  :cljsbuild {:builds {:gas {:source-paths ["src"]
                             :compiler {:main gas.core
                                        :asset-path "js/compiled/out"
                                        :output-to "resources/public/js/compiled/gas.js"
                                        :output-dir "resources/public/js/compiled/out"
                                        :optimizations :none}}}}

  :figwheel {:repl true
             :nrepl-port 0})
