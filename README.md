# Train Schedule and Reservation Management System

## CS1350 - Software Engineering 1
### Imam Mohammad Ibn Saud Islamic University

---

## Project Overview

A centralized software solution for managing train schedules, passenger reservations, and ticket allocation. Built using Java Swing with MVC architecture and file-based persistence.

## Architecture

The system follows the **MVC (Model-View-Controller)** architectural pattern:

```
TrainSystem/
├── src/
│   ├── Main.java                    # Application entry point
│   ├── model/                       # Data Models
│   │   ├── User.java                # User authentication model
│   │   ├── Train.java               # Train and schedule data
│   │   ├── Passenger.java           # Passenger profile data
│   │   └── Reservation.java         # Booking/reservation data
│   ├── view/                        # Swing GUI (View Layer)
│   │   ├── LoginFrame.java          # Login screen
│   │   ├── MainFrame.java           # Main application window
│   │   ├── DashboardPanel.java      # Dashboard with stats & charts
│   │   ├── TrainPanel.java          # Train CRUD management
│   │   ├── PassengerPanel.java      # Passenger CRUD management
│   │   ├── ReservationPanel.java    # Booking & ticketing
│   │   └── ReportsPanel.java        # Reports & analytics
│   ├── controller/                  # Business Logic (Controller Layer)
│   │   ├── AuthController.java      # Authentication logic
│   │   ├── TrainController.java     # Train management logic
│   │   ├── PassengerController.java # Passenger management logic
│   │   └── ReservationController.java # Reservation logic
│   └── util/                        # Utilities
│       ├── DataStore.java           # File-based data persistence
│       └── UIStyle.java             # UI styling constants
├── data/                            # Serialized data files (auto-created)
├── build_and_run.bat                # Windows build script
├── build_and_run.sh                 # Linux/Mac build script
└── README.md                        # This file
```

## Features

### 1. Train & Schedule Management
- Create, update, and delete train schedules
- Define routes, departure/arrival stations and times
- Specify seat capacity and ticket pricing
- Track train status (Active/Cancelled/Completed)

### 2. Passenger Information Management
- Register and manage passenger profiles
- Store identification (National ID) and contact details
- Duplicate ID validation
- View registration history

### 3. Reservation & Ticketing Module
- Process ticket reservations with seat validation
- Automatically update remaining seat capacity
- Support ticket cancellation with seat reallocation
- Generate booking confirmations
- View reservation details

### 4. Administrative Dashboard
- Summary statistics (total trains, bookings, revenue)
- Visual occupancy rate bar chart
- Reservation status pie chart
- Recent bookings activity feed

### 5. Reporting & Analytics
- **Booking Report**: Filtered by date range with summary stats
- **Revenue Report**: Revenue breakdown by train with bar chart
- **Utilization Report**: Seat occupancy analysis per train
- **Passenger Report**: Passenger activity and spending summary

### 6. Security
- Role-based access control (Administrator / Staff)
- Secure login authentication
- Session management with logout

## How to Build and Run

### Prerequisites
- **Java JDK 11+** installed (JDK 17 or 21 recommended)
- Download from: https://adoptium.net/

### Windows
```
Double-click build_and_run.bat
```
Or in Command Prompt:
```cmd
cd TrainSystem
javac -d bin src\model\*.java src\util\*.java src\controller\*.java src\view\*.java src\Main.java
cd bin
java Main
```

### Linux / macOS
```bash
cd TrainSystem
chmod +x build_and_run.sh
./build_and_run.sh
```

## Default Login Credentials

| Username | Password   | Role  |
|----------|------------|-------|
| admin    | admin123   | ADMIN |
| staff    | staff123   | STAFF |

## Data Storage

All data is stored in serialized `.dat` files in the `data/` folder:
- `users.dat` - User accounts
- `trains.dat` - Train schedules
- `passengers.dat` - Passenger profiles
- `reservations.dat` - Booking records
- `counters.dat` - ID counters

Data persists between application sessions automatically.

## Technologies Used

- **Language**: Java 11+
- **GUI Framework**: Java Swing
- **Architecture**: MVC (Model-View-Controller)
- **Persistence**: Java Object Serialization (File-based)
- **Development Methodology**: Agile/Scrum
