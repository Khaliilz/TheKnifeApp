@echo off
title TheKnife - Client

IF EXIST "bin\theknife-Client.jar" GOTO AVVIA

echo.
echo [INFO] File JAR non trovato. Avvio la compilazione con Maven...
call mvn -f src\theKnife-Client\pom.xml clean package

:AVVIA
echo.
echo [INFO] Avvio in corso...
java -jar bin\theknife-Client.jar