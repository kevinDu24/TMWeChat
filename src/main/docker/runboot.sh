sleep 100
java -Djava.security.egd=file:/dev/./urandom -jar -XX:+UseCompressedClassPointers -XX:CompressedClassSpaceSize=2G /app/app.jar