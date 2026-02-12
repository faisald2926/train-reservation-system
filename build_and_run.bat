@echo off
echo ===================================================
echo  Train Schedule and Reservation Management System
echo  Building and Running...
echo ===================================================
echo.

REM Create output directories
if not exist bin mkdir bin
if not exist data mkdir data

REM Compile all Java files
echo Compiling Java source files...
javac -d bin src\model\*.java src\util\*.java src\controller\*.java src\view\*.java src\Main.java

if %errorlevel% neq 0 (
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
cd bin
java Main
cd ..

pause
