(ns leiningen.new.niquola
  (:require [leiningen.new.templates
             :refer [renderer name-to-path ->files
                     sanitize sanitize-ns project-name]]
            [leiningen.core.main :as main]
            [clojure.string :refer [join]]))

(def render (renderer "niquola"))

(defn template-data [name opts]
  {:full-name name
   :name (project-name name)
   :project-goog-module (sanitize (sanitize-ns name))
   :project-ns (sanitize-ns name)
   :sanitized (name-to-path name)})

(defn format-files-args [name opts]
  (let [data (template-data name opts)
        args [data
              ["project.clj" (render "project.clj" data)]
              ["src/clj/{{sanitized}}/core.clj" (render "src/clj/niquola/core.clj" data)]
              ["env/prod/clj/{{sanitized}}/middleware.clj" (render "env/prod/clj/niquola/middleware.clj" data)]
              ["env/dev/clj/{{sanitized}}/middleware.clj" (render "env/dev/clj/niquola/middleware.clj" data)]
              ["src/cljs/{{sanitized}}/core.cljs" (render "src/cljs/niquola/core.cljs" data)]
              ["resources/public/.gitkeep" ""]
              ["src/cljc/{{sanitized}}/util.cljc" (render "src/cljc/niquola/util.cljc" data)]
              ["env/dev/cljs/{{sanitized}}/dev.cljs" (render "env/dev/cljs/niquola/dev.cljs" data)]
              ["env/prod/cljs/{{sanitized}}/prod.cljs" (render "env/prod/cljs/niquola/prod.cljs" data)]
              ["LICENSE" (render "LICENSE" data)]
              ["README.md" (render "README.md" data)]
              [".gitignore" (render "gitignore" data)]
              [".projectile" ""]
              ["system.properties" (render "system.properties" data)]
              ["Procfile" (render "Procfile" data)]]]
    args))

(defn niquola [name & opts]
  (main/info "Generating fresh 'lein new' niquola project.")
  (apply ->files
         (format-files-args name opts)))
