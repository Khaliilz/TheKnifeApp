@echo off
title TheKnife - Client

echo ===================================
echo        AVVIO THE KNIFE CLIENT      
echo ===================================

IF EXIST "target\theknife-client-1.0-shaded.jar" GOTO AVVIA

echo.
echo [INFO] File JAR non trovato. Avvio la compilazione con Maven...
call mvn clean package

:AVVIA
echo.
echo [INFO] Avvio in corso...
java -jar target\theknife-client-1.0-shaded.jar