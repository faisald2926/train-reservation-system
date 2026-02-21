#!/bin/bash
echo "==================================================="
echo " Train Schedule and Reservation Management System"
echo " Building and Running..."
echo "==================================================="
echo

# Create output directories
mkdir -p bin data

# Locate javac and matching java from the same JDK
JAVAC_EXE="$(command -v javac)"
if [ -z "$JAVAC_EXE" ]; then
    echo "ERROR: javac not found. Install a JDK and make sure it is on PATH."
    exit 1
fi

JAVA_EXE="${JAVAC_EXE%/javac}/java"
if [ ! -x "$JAVA_EXE" ]; then
    echo "ERROR: Could not find java next to javac."
    echo "javac: $JAVAC_EXE"
    exit 1
fi

echo "Using compiler:"
"$JAVAC_EXE" -version
echo "Using runtime:"
"$JAVA_EXE" -version
echo

# Compile all Java files
echo "Compiling Java source files..."
"$JAVAC_EXE" -d bin src/model/*.java src/util/*.java src/controller/*.java src/view/*.java src/Main.java

if [ $? -ne 0 ]; then
    echo
    echo "ERROR: Compilation failed! Make sure JDK is installed."
    exit 1
fi

echo "Compilation successful!"
echo

# Run the application
echo "Starting application..."
cd bin
"$JAVA_EXE" Main
