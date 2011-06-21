(ns staging.nodes
  (:require 
    [pallet.core :as core]))


; Defines an EC2 m1.large instance in us-east-1a using the Ubuntu
; 11.04 AMI
;
(def ubuntu-1104-m1-large-us-east-1a
  (core/node-spec
   :image {:os-family :ubuntu
           :os-description-matches "11.04"
           :os-64-bit true
           :image-id "us-east-1/ami-1aad5273"}
   :hardware {:hardware-id "m1.large"}
   :network {:location-id "us-east-1a"
             :inbound-ports [22]
             ; FIXME
             ; Need to make sure these security groups are pre-created
             ;:security-groups ["staging"]
             ; FIXME
             ; Looks like this is broken...
             ; http://code.google.com/p/jclouds/issues/detail?id=602
             ;:key-pair "us-east-1a_staging"
             }))


; Defines an EC2 m1.large instance in us-east-1b using the Ubuntu
; 11.04 AMI
;
(def ubuntu-1104-m1-large-us-east-1b
  (core/node-spec
   :image {:os-family :ubuntu
           :os-description-matches "11.04"
           :os-64-bit true
           :image-id "us-east-1/ami-1aad5273"}
   :hardware {:hardware-id "m1.large"}
   :network {:location-id "us-east-1b"
             :inbound-ports [22]
             ; FIXME
             ; Need to make sure these security groups are pre-created
             ;:security-groups ["staging"]
             ; FIXME
             ; Looks like this is broken...
             ; http://code.google.com/p/jclouds/issues/detail?id=602
             ;:key-pair "us-east-1a_staging"
             }))
