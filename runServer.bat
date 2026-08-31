@echo off
title TheKnife - Server

IF EXIST "bin\theknife-Server.jar" GOTO AVVIA
echo.
echo [INFO] File JAR non trovato. Avvio la compilazione con Maven...
call mvn clean package

:AVVIA
echo.
echo [INFO] Avvio in corso...
java -jar bin\theknife-Server.jar