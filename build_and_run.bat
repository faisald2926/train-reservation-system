@echo off
setlocal
echo ===================================================
echo  Train Schedule and Reservation Management System
echo  Building and Running...
echo ===================================================
echo.

REM Create output directories
if not exist bin mkdir bin
if not exist data mkdir data

REM Locate javac and matching java from the same JDK
set "JAVAC_EXE="
for /f "delims=" %%i in ('where javac 2^>nul') do (
    set "JAVAC_EXE=%%~fi"
    goto :javac_found
)

:javac_found
if not defined JAVAC_EXE (
    echo ERROR: javac not found. Install a JDK and make sure it is on PATH.
    echo Download JDK from: https://adoptium.net/
    pause
    exit /b 1
)

for %%i in ("%JAVAC_EXE%") do set "JAVA_EXE=%%~dpijava.exe"
if not exist "%JAVA_EXE%" (
    echo ERROR: Could not find java.exe next to javac.exe.
    echo javac: %JAVAC_EXE%
    pause
    exit /b 1
)

echo Using compiler:
"%JAVAC_EXE%" -version
echo Using runtime:
"%JAVA_EXE%" -version
echo.

REM Compile all Java files
echo Compiling Java source files...
if exist sources.txt del sources.txt
dir /s /b src\*.java > sources.txt
"%JAVAC_EXE%" -d bin @sources.txt
set "COMPILE_RC=%errorlevel%"
del sources.txt

if %COMPILE_RC% neq 0 (
    echo.
    echo ERROR: Compilation failed! Make sure JDK is installed.
    echo Download JDK from: https://adoptium.net/
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Run the application
echo Starting application...
pushd bin
"%JAVA_EXE%" Main
popd

pause
endlocal
