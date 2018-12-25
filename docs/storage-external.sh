java \
 -Xmx256m \
 -Dfile.encoding=UTF-8 \
 -jar storage-external-1.1.1.jar \
 --spring.profiles.active.uri=default \
 --spring.cloud.config.uri=http://10.0.20.125:8700 \
 --spring.cloud.config.label=9e640b3
