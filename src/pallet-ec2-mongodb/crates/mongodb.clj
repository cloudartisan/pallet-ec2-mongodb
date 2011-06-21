(ns staging.crate.mongodb
  (:require
    [pallet.action.exec-script :as exec-script]
    [pallet.action.directory :as directory]
    [pallet.action.file :as file]
    [pallet.action.package :as package]
    [pallet.action.service :as service]
    [pallet.script :as script]))


(def default-configuration
  {:dbpath "/var/lib/mongodb"
   :logpath "/var/log/mongodb/mongodb.log"
   :logappend true
   :port 27017
   :cpu false
   :noauth true
   :auth false
   :verbose false
   :objcheck false
   :quota false
   :diaglog 0
   :nocursors false
   :nohints false
   :nohttpinterface true
   :noscripting false
   :notablescan false
   :noprealloc false
   :master false
   :slave false
   :journal true})


(defn install
  "Install MongoDB server (from the 10gen repository)" 
  [session] 
  (-> session 
    (package/package-source 
      "10gen" 
      :aptitude {:source-type "deb"
                 :url "http://downloads-distro.mongodb.org/repo/ubuntu-upstart"
                 :release "dist"
                 :scopes ["10gen"]
                 :key-id "7F0CEB10"} )
    (package/package-manager :update) 
    (package/package "mongodb-10gen" :action :install)
    (service/service "mongodb" :action :restart)))


(defn start
  "Start MongoDB service"
  [session]
  (-> session
    (service/service "mongodb" :action :start)))


(defn stop
  "Stop MongoDB service"
  [session]
  (-> session
    (service/service "mongodb" :action :stop)))


(defn restart
  "Restart MongoDB service"
  [session]
  (-> session
    (service/service "mongodb" :action :restart)))
