@echo off
title TheKnife - Server

echo ===================================
echo        AVVIO THE KNIFE SERVER      
echo ===================================

IF EXIST "target\theknife-server-1.0-shaded.jar" GOTO AVVIA
echo.
echo [INFO] File JAR non trovato. Avvio la compilazione con Maven...
call mvn clean package

:AVVIA
echo.
echo [INFO] Avvio in corso...
java -jar target\theknife-server-1.0-shaded.jar