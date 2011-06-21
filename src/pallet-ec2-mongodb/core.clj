(ns staging.core
  (:require 
    [pallet.core :as core]
    [pallet.compute :as compute]
    [pallet.phase :as phase]
    [pallet.crate.automated-admin-user :as automated-admin-user]
    [staging.nodes :as staging-nodes]
    [staging.crate.mongodb :as mongodb]))


(def ec2-service
  (compute/compute-service-from-config-file :ec2))


; FIXME
;
; Does not work.  Need a way to specify existing SSH keypair before launch,
; preferably at the cluster-spec level.
; 
; http://code.google.com/p/jclouds/issues/detail?id=602
;
;(def ubuntu-admin-user
;  (core/admin-user "ubuntu"
;    :private-key-path "~/.ssh/assistly/staging.pem"))


; FIXME
;
; I want to ensure the "staging" security group is created before any
; instance is launched and have all instances "inherit" that security group.
;
; If all instances are destroyed I want to destroy the "staging" security
; group.
;
; I want to automatically create security groups at the server-spec level
; and have all instances "inherit" those security groups at the group-spec
; level.
;
; Eg, if a group-spec extends "with-redis" and "with-mongodb" I want to make
; sure any instance launched from that group-spec is in both the "mongodb"
; and "redis" security groups.  If all instances in the "redis" security
; group are destroyed I want to destroy the "redis" security group.
;
; I also want to be able to specify security groups as members of other
; security groups.  Eg, so that only instances in the "web" security group
; are able to talk to instances in the "database" security group.
;
; I have no idea where to call these... 
;
; (.createSecurityGroupInRegion ec2-security-group-client "us-east-1" "sg" "desc")
; (.deleteSecurityGroupInRegion ec2-security-group-client "us-east-1" "sg")
;
(def ec2-security-group-client
  (..
    (clojure.contrib.reflect/get-field
      org.jclouds.ec2.compute.EC2ComputeService "ec2Client" (.compute ec2-service))
    getSecurityGroupServices))


; With an automatically-created admin user account for the current user
;
; FIXME
;
; Get rid of this as soon as this bug is fixed:
; http://code.google.com/p/jclouds/issues/detail?id=602
;
(def with-automated-admin-user
  (core/server-spec
    :phases {:bootstrap (phase/phase-fn (automated-admin-user/automated-admin-user))}))


; With MongoDB from the 10gen repository
;
; We use the 10gen-provided MongoDB to get the most recent packaged version.
; 
(def with-mongodb-10gen
  (core/server-spec
   :phases {:configure (phase/phase-fn (mongodb/install))
            :start-mongodb (phase/phase-fn (mongodb/start))
            :stop-mongodb (phase/phase-fn (mongodb/stop))
            :restart-mongodb (phase/phase-fn (mongodb/restart))}))


; Recommended EC2 configuration:
; http://www.mongodb.org/display/DOCS/Amazon+EC2
; "use a 64 bit instance as this is required for all MongoDB databases of
; significant size"
;
(def mongodb-us-east-1a
  (core/group-spec
    "mongodb"
    :extends [with-automated-admin-user
              with-mongodb-10gen]
    :node-spec staging-nodes/ubuntu-1104-m1-large-us-east-1a))


; Recommended EC2 configuration:
; http://www.mongodb.org/display/DOCS/Amazon+EC2
; "use a 64 bit instance as this is required for all MongoDB databases of
; significant size"
;
(def mongodb-us-east-1b
  (core/group-spec
    "mongodb"
    :extends [with-automated-admin-user
              with-mongodb-10gen]
    :node-spec staging-nodes/ubuntu-1104-m1-large-us-east-1b))


(def mongodb-cluster
  (core/cluster-spec
    "mongodb"
    :groups [mongodb-us-east-1a
             mongodb-us-east-1b]))
