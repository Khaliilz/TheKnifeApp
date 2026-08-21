@echo off
call mvn clean package
java -jar target/theknife-client-1.0-shaded.jar