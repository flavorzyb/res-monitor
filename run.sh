#!/bin/sh
java -cp libs/jnotify-0.94.jar:target/resource-monitor-1.0-SNAPSHOT.jar -Duser.config=./src/main/resource/config.xml com.zhuyanbin.app.App
