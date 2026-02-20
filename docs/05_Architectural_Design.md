# Architectural Design

## 1. Chosen Architecture: MVC (Model-View-Controller)

The system adopts the **Model-View-Controller (MVC)** architectural pattern, which separates the application into three interconnected layers. This pattern is widely recognized as the most suitable approach for GUI-based applications with structured data management requirements.

## 2. Architecture Justification

### Why MVC?

| Criteria | How MVC Addresses It |
|----------|---------------------|
| **Functionality** | Separates business logic (controllers) from data (models) and interface (views), allowing each component to be developed and tested independently. |
| **Scalability** | New features (e.g., new report types, new entity types) can be added by creating new model, controller, and view classes without modifying existing ones. |
| **Maintainability** | Changes to the GUI (e.g., switching from Swing to JavaFX) do not affect business logic or data models. Bug fixes in one layer do not ripple to others. |
| **Testability** | Controllers can be unit tested independently of the GUI. Models can be validated without requiring a running application. |
| **Team Collaboration** | Different team members can work on models, views, and controllers simultaneously with minimal merge conflicts. |

### Alternatives Considered

| Architecture | Reason for Rejection |
|-------------|---------------------|
| **Layered Architecture** | While suitable for enterprise apps, it introduces unnecessary complexity for a desktop application. MVC provides sufficient separation with less overhead. |
| **Client-Server** | The current scope does not require network communication. File-based storage with a single-user desktop app does not benefit from client-server separation. |
| **Microservices** | Overkill for a desktop application. Introduces networking and deployment complexity that is not warranted for this project scale. |

## 3. System Components

### 3.1 Model Layer

**Responsibility:** Defines data structures and encapsulates application state.

| Component | Description |
|-----------|-------------|
| `User.java` | Represents system users with username, password, role (ADMIN/STAFF), and full name. Implements Serializable for persistence. |
| `Train.java` | Represents train schedules with ID, name, route, stations, times, capacity, pricing, status, and occupancy calculation. |
| `Passenger.java` | Represents passenger profiles with ID, name, contact info, national ID, and registration date. |
| `Reservation.java` | Represents bookings with IDs linking passenger and train, dates, seat count, price, and status (CONFIRMED/CANCELLED/PENDING). |

### 3.2 View Layer

**Responsibility:** Renders the GUI and captures user input. All views are Java Swing components.

| Component | Description |
|-----------|-------------|
| `LoginFrame.java` | Login screen with username/password fields and authentication trigger. |
| `MainFrame.java` | Main application window with tabbed navigation (Dashboard, Trains, Passengers, Reservations, Reports). Includes top bar with user info and logout. |
| `DashboardPanel.java` | Displays summary statistics, occupancy bar chart, reservation pie chart, and recent activity table. |
| `TrainPanel.java` | Train management with data table (left) and CRUD form (right). Supports add, update, delete, and clear operations. |
| `PassengerPanel.java` | Passenger management with data table (left) and CRUD form (right). Includes national ID duplicate validation. |
| `ReservationPanel.java` | Booking form with passenger/train dropdowns, seat info display, and booking/cancellation/confirmation actions. |
| `ReportsPanel.java` | Report generation interface with date range filtering and four report types with visual charts and data tables. |

### 3.3 Controller Layer

**Responsibility:** Contains business logic, validation rules, and coordinates data flow between models and views.

| Component | Description |
|-----------|-------------|
| `AuthController.java` | Handles login authentication, session management, and role checking. |
| `TrainController.java` | Validates and processes train CRUD operations. Enforces business rules (e.g., cannot delete train with active reservations). |
| `PassengerController.java` | Validates and processes passenger CRUD operations. Enforces duplicate national ID prevention. |
| `ReservationController.java` | Handles ticket booking with seat validation, cancellation with seat restoration, and confirmation generation. |

### 3.4 Utility Layer

**Responsibility:** Provides shared services used across all layers.

| Component | Description |
|-----------|-------------|
| `DataStore.java` | Singleton class managing all data in-memory with file-based persistence (Java Object Serialization). Handles loading, saving, ID generation, and statistics. |
| `UIStyle.java` | Centralized styling constants (colors, fonts, component factories) ensuring consistent UI appearance across all panels. |

## 4. Component Interactions

```
                    +-------------------+
                    |    LoginFrame     |
                    |     (View)        |
                    +--------+----------+
                             |
                             v
                    +-------------------+
                    |  AuthController   |
                    |  (Controller)     |
                    +--------+----------+
                             |
                             v
+------------------------------------------------------------------+
|                        MainFrame                                  |
|                     (View Container)                              |
|                                                                   |
|  +------------+ +----------+ +------------+ +---------+ +-------+ |
|  | Dashboard  | | Train    | | Passenger  | |Reserv.  | |Reports| |
|  | Panel      | | Panel    | | Panel      | |Panel    | |Panel  | |
|  | (View)     | | (View)   | | (View)     | |(View)   | |(View) | |
|  +-----+------+ +----+-----+ +-----+------+ +----+----+ +---+---+ |
|        |              |             |             |           |    |
+------------------------------------------------------------------+
         |              |             |             |           |
         v              v             v             v           v
    (Statistics)  TrainController PassengerCtrl  ReservationCtrl (Statistics)
         |              |             |             |           |
         +--------------+------+------+-------------+-----------+
                               |
                               v
                      +------------------+
                      |    DataStore     |
                      |   (Singleton)    |
                      +--------+---------+
                               |
                               v
                      +------------------+
                      |  File System     |
                      | (data/*.dat)     |
                      +------------------+
```

## 5. Data Flow

### Typical Operation Flow:

1. **User Action**: User interacts with a View component (e.g., clicks "Add" on TrainPanel).
2. **View to Controller**: View collects input data and calls the appropriate Controller method.
3. **Controller Validation**: Controller validates input data and applies business rules.
4. **Controller to DataStore**: Controller calls DataStore methods to read or modify data.
5. **DataStore Persistence**: DataStore updates in-memory lists and serializes to .dat files.
6. **Controller Response**: Controller returns success (null) or error message (String) to View.
7. **View Update**: View displays success/error message and refreshes displayed data.

## 6. Design Patterns Used

| Pattern | Where Used | Purpose |
|---------|-----------|---------|
| **MVC** | Entire application | Separation of concerns between data, logic, and presentation. |
| **Singleton** | DataStore | Ensures a single data access point across all controllers and views. |
| **Observer** | JTabbedPane ChangeListener | Refreshes panels when tabs are switched to show up-to-date data. |
| **Factory Method** | UIStyle component creators | Centralizes the creation of styled UI components for consistency. |
