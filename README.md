### How To Run

```
$ export JDK_JAVA_OPTIONS="-XX:+UseZGC -Dfile.encoding=UTF-8" \
  export LOG_TEMP="$HOME/app_logs" \
  java -XX:+UseShenandoahGC -DFoo=Bar -Dfile.encoding=UTF-8 -jar *.jar --spring.profiles.active=dev
```
