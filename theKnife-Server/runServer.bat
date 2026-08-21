@echo off
call mvn clean package
java -jar target/theknife-server-1.0-shaded.jar