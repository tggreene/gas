(defproject bip "0.1.0"
  :description "A simple event-handling framework for ClojureScript projects."
  :url "https://github.com/tggreene/bip"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async "0.2.374"]
                 [bidi "1.24.0"]
                 [com.cemerick/url "0.1.1"]
                 [kibu/pushy "0.3.6"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-1"]]

  :cljsbuild {:builds {:bip {:source-paths ["src"]
                                :compiler {:main bip.core
                                           :asset-path "js/compiled/out"
                                           :output-to "resources/public/js/compiled/bip.js"
                                           :output-dir "resources/public/js/compiled/out"
                                           :optimizations :none}}}}

  :figwheel {:repl true
             :nrepl-port 7888})
