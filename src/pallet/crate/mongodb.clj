(ns pallet.crate.mongodb
  "Installs and configures MongoDB.

   Tested on Ubuntu 11.04"
  (:require
    [pallet.action.exec-script :as exec-script]
    [pallet.action.directory :as directory]
    [pallet.action.file :as file]
    [pallet.action.package :as package]
    [pallet.script :as script]))


(def mongodb-db-path "/mnt/mongodb/db") 
(def mongodb-log "/var/log/mongodb.log") 


(script/defscript mongod []) 
(script/defimpl mongod :default [] 
  (sudo mongod --fork --dbpath ~mongodb-db-path --logpath ~mongodb-log --logappend)) 


(defn mongodb-10gen 
  "Install MongoDB server (from the 10gen repository)" 
  [request] 
  (-> request 
    (package/package-source 
      "10gen" 
      :aptitude {:source-type "deb"
                 :url "http://downloads-distro.mongodb.org/repo/ubuntu-upstart"
                 :scopes ["dist" "10gen"]
                 :key-id "7F0CEB10"} )
    (package/package-manager :update) 
    (package/package "mongodb-10gen" :action :install) 
    ;(directory/directories [mongodb-db-path] 
    ;  :owner "ubuntu:ubuntu" 
    ;  :p true) 
    ;(file/file mongodb-log 
    ;  :action :touch 
    ;  :owner "ubuntu:ubuntu") 
    (exec-script/exec-checked-script 
      "Start MongoDB server" 
      (mongod)))) 
