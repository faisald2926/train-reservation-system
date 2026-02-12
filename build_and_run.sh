#!/bin/bash
echo "==================================================="
echo " Train Schedule and Reservation Management System"
echo " Building and Running..."
echo "==================================================="
echo

# Create output directories
mkdir -p bin data

# Compile all Java files
echo "Compiling Java source files..."
javac -d bin src/model/*.java src/util/*.java src/controller/*.java src/view/*.java src/Main.java

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
java Main
