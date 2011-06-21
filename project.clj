(defproject pallet-ec2-mongodb "0.0.1"
  :description "pallet-ec2-mongodb"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [org.cloudhoist/pallet "0.6.1"]
                 [org.cloudhoist/pallet-crates-all "0.5.0"]
                 [org.jclouds.provider/aws-ec2 "1.0.0"]
                 [org.jclouds.provider/aws-s3 "1.0.0"]
                 [org.jclouds.driver/jclouds-jsch "1.0.0"]
                 [org.jclouds.driver/jclouds-log4j "1.0.0"]
                 [log4j/log4j "1.2.14"]]
  :dev-dependencies [[org.cloudhoist/pallet-lein "0.4.0"]]
  :repositories {"sonatype-snapshot" "https://oss.sonatype.org/content/repositories/snapshots/"
                 "sonatype" "https://oss.sonatype.org/content/repositories/releases"})
