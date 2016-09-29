@echo off
rem Author: Rolyer Luo
rem Date: 15/01/2016
rem Version: 1.0
rem
rem ---------------------------------------------------------------------------
rem
rem Auto Compile Script

rem Get project path
set BASE_HOME=%~dp0%
set MOD_PLATFORM=%BASE_HOME%\platform
set MOD_STATIC=%MOD_PLATFORM%\static
set MOD_COMMON=%MOD_PLATFORM%\common
set MOD_REST=%BASE_HOME%\prcsteel-rest


echo Start build static module
cd %MOD_STATIC%
start mvn clean install
echo Start mvn clean install
echo After sub window finished, close it, and press any key to continue. & pause>nul
echo.

echo Start build common module
cd %MOD_COMMON%
start mvn clean install
echo Start mvn clean install
echo After sub window finished, close it, and press any key to continue. & pause>nul
echo.

echo Start build REST module
cd %MOD_REST%
start mvn clean install
echo Start mvn clean install
echo After sub window finished, close it, and press any key to continue. & pause>nul
echo.

echo Start build platform module
cd %MOD_PLATFORM%
start mvn clean install
echo Start mvn clean install
echo After sub window finished, close it, and press any key to continue. & pause>nul
echo.

echo Build Finished

cd %BASE_HOME%

