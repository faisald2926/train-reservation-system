# Train Schedule and Reservation Management System

## Complete Project Report

---

### Imam Mohammad Ibn Saud Islamic University
### College of Computer and Information Sciences
### Computer Science Department

**Course:** CS1350 - Software Engineering 1

**Semester:** Second Semester, 2026

**Project Title:** Train Schedule and Reservation Management System

---

## Table of Contents

1. Project Initiation
2. Product Backlog
3. Sprint Planning
4. System Design Models
5. Architectural Design
6. Implementation
7. Test Plan and Test Cases
8. Conclusion

---

# 1. Project Initiation

## 1.1 Project Scope

The Train Schedule and Reservation Management System is a centralized desktop application designed to manage train schedules, passenger reservations, and ticket allocation for railway operations. The system provides structured data management capabilities through a secure, user-friendly graphical interface built with Java Swing.

### In Scope
- Train schedule creation, modification, and deletion (CRUD operations).
- Passenger registration and profile management with identification validation.
- Ticket reservation processing with real-time seat availability validation.
- Ticket cancellation with automatic seat reallocation.
- Booking confirmation generation for each reservation.
- Administrative dashboard displaying key performance indicators.
- Reporting and analytics module covering bookings, revenue, utilization, and passenger activity.
- Role-based access control with Administrator and Staff roles.
- Persistent file-based data storage using Java serialization.

### Out of Scope
- Online payment gateway integration.
- Real-time GPS tracking of trains.
- Multi-language support (English only in current release).
- Mobile application (desktop only in current release).

## 1.2 Objectives

1. Develop a fully functional train reservation management system with complete CRUD operations for trains, passengers, and reservations.
2. Implement seat availability validation, automatic capacity updates, and booking confirmations.
3. Provide an administrative dashboard with visual charts and summary statistics.
4. Build a reporting module for booking, revenue, utilization, and passenger analytics.
5. Enforce role-based access control for data security.
6. Follow MVC architecture for maintainability and scalability.

## 1.3 Target Audience

- **Railway Administrators**: Full access to all modules including reports and dashboard.
- **Station Staff**: Access to passenger management, reservations, and viewing trains.
- **Operations Managers**: Use reporting module for performance analysis and decision-making.

## 1.4 Vision

The system aims to enhance operational efficiency by automating schedule management and booking processes, improve data accuracy through validation rules, and support decision-making through visual dashboards and analytical reports. Built on MVC architecture, the system is designed for easy extension and future enhancements.

---

# 2. Product Backlog

## 2.1 Backlog Summary

The complete product backlog contains 39 user stories organized across 7 modules:

| Module | User Stories | Story Points |
|--------|-------------|-------------|
| Authentication and Security | 5 | 11 |
| Train and Schedule Management | 7 | 18 |
| Passenger Information Management | 7 | 16 |
| Reservation and Ticketing | 9 | 21 |
| Administrative Dashboard | 4 | 13 |
| Reporting and Analytics | 4 | 16 |
| Data Persistence | 3 | 9 |
| **Total** | **39** | **110** |

## 2.2 Key User Stories

**US-006**: As an administrator, I want to add a new train with its name, route, stations, times, seat capacity, and price so that it becomes available for booking. (Priority: High, 5 SP)

**US-020**: As a staff member, I want to book a ticket by selecting a passenger, a train, a travel date, and the number of seats so that a reservation is created. (Priority: High, 5 SP)

**US-021**: As a staff member, I want the system to validate seat availability before confirming a booking so that overbooking is prevented. (Priority: High, 3 SP)

**US-023**: As a staff member, I want to cancel a reservation so that seats are released back to the train. (Priority: High, 3 SP)

**US-029**: As an administrator, I want to see total trains, active trains, total passengers, active bookings, and total revenue on a dashboard. (Priority: High, 5 SP)

**US-033**: As an administrator, I want to generate a booking report filtered by date range. (Priority: High, 5 SP)

(Full backlog with all 39 user stories is available in the separate Product Backlog document.)

---

# 3. Sprint Planning

## 3.1 Sprint 1: Foundation and Core Models (2 weeks)

**Goal**: Establish project foundation including architecture, data models, authentication, and train management module.

**Key Deliverables**: Project structure, all 4 model classes, DataStore with persistence, UIStyle utility, LoginFrame, MainFrame, TrainController, TrainPanel.

**Velocity**: 35 story points

## 3.2 Sprint 2: Passengers, Reservations, and Design (3 weeks)

**Goal**: Implement passenger management, reservation and ticketing module, and complete system design documentation.

**Key Deliverables**: PassengerController, PassengerPanel, ReservationController, ReservationPanel, booking confirmations, UML diagrams, architectural design document.

**Velocity**: 42 story points

## 3.3 Sprint 3: Dashboard, Reports, Testing, and Release (2 weeks)

**Goal**: Implement dashboard, reporting module, prepare test cases, and deliver first release.

**Key Deliverables**: DashboardPanel with charts, ReportsPanel with 4 report types, 61 test cases, GitHub release.

**Velocity**: 33 story points

## 3.4 Velocity Summary

| Sprint | Planned SP | Completed SP |
|--------|-----------|-------------|
| Sprint 1 | 35 | 35 |
| Sprint 2 | 42 | 42 |
| Sprint 3 | 33 | 33 |
| **Total** | **110** | **110** |

(Full sprint planning details including task breakdowns and meeting logs are available in the separate Sprint Planning document.)

---

# 4. System Design Models

## 4.1 Use Case Diagram

### Actors
- **Administrator**: Full system access including train management, dashboard, and reports.
- **Staff**: Access to passenger management, reservations, and train viewing.

### Use Cases
- Login / Logout
- Manage Trains (Add, Update, Delete)
- Manage Passengers (Add, Update, Delete)
- Manage Reservations (Book Ticket, Cancel Ticket, View Confirmation)
- View Dashboard
- Generate Reports (Booking, Revenue, Utilization, Passenger)

## 4.2 Use Case Scenarios

### Book Ticket (UC-004)

| Field | Description |
|-------|-------------|
| **Actor** | Staff |
| **Precondition** | Logged in. At least one passenger and one active train exist. |
| **Main Flow** | 1. Select passenger. 2. Select train. 3. Enter travel date and seat count. 4. Click Book Ticket. 5. System validates availability. 6. System creates reservation, deducts seats. 7. Confirmation displayed. |
| **Alt Flow** | Not enough seats: Error displayed. |
| **Postcondition** | Reservation created. Train seats reduced. |

### Cancel Reservation (UC-005)

| Field | Description |
|-------|-------------|
| **Actor** | Staff |
| **Precondition** | Logged in. Confirmed reservation exists. |
| **Main Flow** | 1. Select reservation. 2. Click Cancel. 3. Confirm. 4. Status set to CANCELLED. 5. Seats restored. |
| **Alt Flow** | Already cancelled: Error displayed. |
| **Postcondition** | Status CANCELLED. Seats restored. |

## 4.3 Class Diagram

The system consists of 4 model classes, 4 controller classes, 7 view classes, and 2 utility classes:

**Models**: User (with Role enum), Train, Passenger, Reservation (with Status enum)

**Controllers**: AuthController, TrainController, PassengerController, ReservationController

**Views**: LoginFrame, MainFrame, DashboardPanel, TrainPanel, PassengerPanel, ReservationPanel, ReportsPanel

**Utilities**: DataStore (Singleton), UIStyle

**Key Relationships**:
- All Controllers depend on DataStore (Singleton)
- Reservation references Passenger (passengerId) and Train (trainId)
- Each View panel uses its corresponding Controller

## 4.4 Sequence Diagrams

### Login Sequence
User -> LoginFrame -> AuthController -> DataStore -> (returns User) -> AuthController -> LoginFrame -> MainFrame

### Book Ticket Sequence
Staff -> ReservationPanel -> ReservationController -> DataStore (validate passenger, train, seats) -> Create Reservation -> Update Train seats -> Save -> ReservationPanel (show confirmation)

### Cancel Reservation Sequence
Staff -> ReservationPanel -> ReservationController -> DataStore (get reservation, get train) -> Restore seats -> Set status CANCELLED -> Save -> ReservationPanel (show success)

## 4.5 Activity Diagrams

### Booking Activity
Start -> Select Passenger -> Select Train -> Validate Active -> Enter Date/Seats -> Validate Seats > 0 -> Validate Availability -> Calculate Price -> Create Reservation -> Deduct Seats -> Save -> Show Confirmation -> End

### Login Activity
Start -> Display Login -> Enter Credentials -> Validate Not Empty -> Authenticate -> [Valid: Set Session -> Open MainFrame] / [Invalid: Show Error -> Return to Login] -> End

(Full UML diagrams with detailed text representations are available in the separate System Design Models document.)

---

# 5. Architectural Design

## 5.1 Chosen Pattern: MVC (Model-View-Controller)

The system adopts the MVC architectural pattern, separating the application into three interconnected layers.

## 5.2 Justification

| Criteria | How MVC Addresses It |
|----------|---------------------|
| **Functionality** | Separates business logic from data and interface. |
| **Scalability** | New features added without modifying existing code. |
| **Maintainability** | GUI changes do not affect business logic. |
| **Testability** | Controllers testable independently of GUI. |
| **Team Collaboration** | Team members work on different layers simultaneously. |

## 5.3 Architecture Diagram

```
[View Layer: Swing GUI]
     LoginFrame | MainFrame | DashboardPanel | TrainPanel
     PassengerPanel | ReservationPanel | ReportsPanel
                          |
                          v
[Controller Layer: Business Logic]
     AuthController | TrainController
     PassengerController | ReservationController
                          |
                          v
[Model/Data Layer]
     User | Train | Passenger | Reservation
                          |
                          v
[Persistence Layer]
     DataStore (Singleton) --> File System (data/*.dat)
```

## 5.4 Design Patterns Used

- **MVC**: Entire application structure.
- **Singleton**: DataStore for centralized data access.
- **Observer**: Tab change listeners for data refresh.
- **Factory Method**: UIStyle for consistent UI component creation.

(Full architectural design details are available in the separate Architectural Design document.)

---

# 6. Implementation

## 6.1 Technology Stack

- **Language**: Java 11+ (JDK 21 recommended)
- **GUI Framework**: Java Swing
- **Persistence**: Java Object Serialization (file-based)
- **Architecture**: MVC with Singleton DataStore

## 6.2 Project Structure

```
TrainSystem/
├── src/
│   ├── Main.java
│   ├── model/       (User, Train, Passenger, Reservation)
│   ├── view/        (LoginFrame, MainFrame, 5 Panels)
│   ├── controller/  (Auth, Train, Passenger, Reservation)
│   └── util/        (DataStore, UIStyle)
├── data/            (auto-created .dat files)
├── docs/            (documentation)
├── build_and_run.bat
├── build_and_run.sh
└── README.md
```

## 6.3 How to Build and Run

1. Install JDK 11+ from https://adoptium.net/
2. Extract the project ZIP file.
3. Run `build_and_run.bat` (Windows) or `./build_and_run.sh` (Linux/Mac).
4. Login with admin/admin123 or staff/staff123.

## 6.4 Key Implementation Highlights

- **Auto-generated IDs**: TRN-XXXX, PSG-XXXX, RES-XXXX format with persistent counters.
- **Seat validation**: Real-time availability check before booking confirmation.
- **Seat reallocation**: Automatic seat restoration on cancellation.
- **Visual dashboard**: Custom-painted bar charts and pie charts using Graphics2D.
- **Data persistence**: Automatic save on every data operation using Java serialization.
- **Consistent UI**: Centralized styling through UIStyle utility class.

## 6.5 Default Accounts

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| staff | staff123 | STAFF |

---

# 7. Test Plan and Test Cases

## 7.1 Testing Approach

Agile testing with development and testing interleaved across all three sprints. Testing covered unit, integration, functional, UI, and boundary tests.

## 7.2 Test Summary

| Module | Total Tests | Passed | Failed | Pass Rate |
|--------|------------|--------|--------|-----------|
| Authentication | 8 | 8 | 0 | 100% |
| Train Management | 12 | 12 | 0 | 100% |
| Passenger Management | 9 | 9 | 0 | 100% |
| Reservation & Ticketing | 13 | 13 | 0 | 100% |
| Dashboard | 6 | 6 | 0 | 100% |
| Reports | 8 | 8 | 0 | 100% |
| Data Persistence | 5 | 5 | 0 | 100% |
| **Total** | **61** | **61** | **0** | **100%** |

## 7.3 Key Test Cases

- **TC-001 to TC-008**: Authentication (valid/invalid login, logout, empty fields)
- **TC-009 to TC-020**: Train CRUD (valid add, validation errors, delete with active reservations)
- **TC-021 to TC-029**: Passenger CRUD (valid add, duplicate national ID, invalid email)
- **TC-030 to TC-042**: Reservations (book, cancel, seat validation, confirmation view)
- **TC-043 to TC-048**: Dashboard (stats accuracy, chart display, refresh)
- **TC-049 to TC-056**: Reports (all 4 report types with date filtering)
- **TC-057 to TC-061**: Persistence (save/load across sessions, default creation)

(Full test cases with detailed inputs and expected/actual results are available in the separate Test Plan document.)

---

# 8. Conclusion

The Train Schedule and Reservation Management System has been successfully developed following Agile Scrum methodology across three sprints. The system implements all required features: train and schedule management, passenger information management, reservation and ticketing with seat validation and reallocation, an administrative dashboard with visual charts, and comprehensive reporting and analytics. The MVC architecture ensures clean separation of concerns, maintainability, and extensibility. All 61 test cases pass with a 100% success rate, and the system is ready for deployment as a first release.

---

**GitHub Repository**: [Insert GitHub link here]
