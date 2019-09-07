#!/bin/bash

PATH=~/maven/apache-maven-3.6.1/bin:$PATH

java -version

mvn --version
# mvn package
# mvn dependency:tree
mvn spring-boot:run
