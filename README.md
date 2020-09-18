### How To Run
export JDK_JAVA_OPTIONS=-XX:+UseZGC
export LOG_TEMP=$HOME/app_logs
java -XX:+UseShenandoahGC -DFoo=Bar -Dfile.encoding=UTF-8 -jar *.jar --spring.profiles.active=dev
