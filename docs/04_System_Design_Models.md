# System Design Models

## 1. Use Case Diagram

### Actors
- **Administrator**: Has full system access including train management, dashboard, and reports.
- **Staff**: Has access to passenger management, reservations, and viewing trains.

### Use Cases

```
+-----------------------------------------------------+
|          Train Reservation System                    |
|                                                      |
|   [Login]                                            |
|   [Logout]                                           |
|   [Manage Trains]        ← extends →  [Add Train]   |
|                          ← extends →  [Update Train] |
|                          ← extends →  [Delete Train] |
|   [Manage Passengers]    ← extends →  [Add Passenger]|
|                          ← extends →  [Update Passenger]|
|                          ← extends →  [Delete Passenger]|
|   [Manage Reservations]  ← extends →  [Book Ticket] |
|                          ← extends →  [Cancel Ticket]|
|                          ← extends →  [View Confirmation]|
|   [View Dashboard]                                   |
|   [Generate Reports]     ← extends →  [Booking Report]|
|                          ← extends →  [Revenue Report]|
|                          ← extends →  [Utilization Report]|
|                          ← extends →  [Passenger Report]|
+-----------------------------------------------------+

Actor: Administrator ──── Login, Logout, Manage Trains, Manage Passengers,
                          Manage Reservations, View Dashboard, Generate Reports

Actor: Staff ──────────── Login, Logout, Manage Passengers,
                          Manage Reservations, View Dashboard
```

---

## 2. Use Case Scenarios

### Use Case 1: Login

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-001 |
| **Use Case Name** | Login |
| **Actor** | Administrator, Staff |
| **Precondition** | The system is running and the login screen is displayed. |
| **Main Flow** | 1. User enters username and password. 2. User clicks "Login" button. 3. System validates credentials against stored user data. 4. System creates a session and displays the main application window. |
| **Alternative Flow** | 3a. Credentials are invalid: System displays "Invalid username or password" error. User remains on login screen. |
| **Postcondition** | User is logged in and the main window is displayed with appropriate tabs based on role. |

### Use Case 2: Add Train

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-002 |
| **Use Case Name** | Add Train |
| **Actor** | Administrator |
| **Precondition** | User is logged in. User is on the Trains tab. |
| **Main Flow** | 1. User fills in the train form: name, route, departure station, arrival station, departure time, arrival time, total seats, and price. 2. User clicks "Add" button. 3. System validates that all fields are filled, seats > 0, and price >= 0. 4. System generates a unique Train ID (TRN-XXXX). 5. System saves the train and refreshes the table. 6. System displays "Train added successfully!" message. |
| **Alternative Flow** | 3a. Validation fails: System displays specific error message. Train is not added. |
| **Postcondition** | New train record exists in the system with status "Active" and available seats equal to total seats. |

### Use Case 3: Register Passenger

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-003 |
| **Use Case Name** | Register Passenger |
| **Actor** | Staff |
| **Precondition** | User is logged in. User is on the Passengers tab. |
| **Main Flow** | 1. User enters full name, email, phone, and national ID. 2. User clicks "Add" button. 3. System validates all fields are filled and email contains "@" and ".". 4. System checks that no existing passenger has the same national ID. 5. System generates a unique Passenger ID (PSG-XXXX) and records registration date. 6. System saves the passenger and refreshes the table. |
| **Alternative Flow** | 4a. Duplicate national ID found: System displays "A passenger with this National ID already exists." |
| **Postcondition** | New passenger profile is stored in the system. |

### Use Case 4: Book Ticket

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-004 |
| **Use Case Name** | Book Ticket |
| **Actor** | Staff |
| **Precondition** | User is logged in. At least one passenger and one active train exist in the system. |
| **Main Flow** | 1. User selects a passenger from the dropdown. 2. User selects a train from the dropdown. System displays available seats and price per seat. 3. User enters travel date and number of seats. 4. User clicks "Book Ticket". 5. System validates: train is active, seat count > 0, enough seats available, travel date provided. 6. System calculates total price = price per seat x seat count. 7. System creates reservation with status CONFIRMED, deducts seats from train availability. 8. System displays booking confirmation dialog. |
| **Alternative Flow** | 5a. Not enough seats: System displays "Not enough seats available. Only X seats left." |
| **Postcondition** | Reservation is created. Train available seats are reduced. |

### Use Case 5: Cancel Reservation

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-005 |
| **Use Case Name** | Cancel Reservation |
| **Actor** | Staff |
| **Precondition** | User is logged in. A confirmed reservation exists. |
| **Main Flow** | 1. User selects a reservation from the table. 2. User clicks "Cancel Selected". 3. System asks for confirmation. 4. User confirms cancellation. 5. System sets reservation status to CANCELLED. 6. System restores seats to the train's available count. 7. System displays "Reservation cancelled. Seats have been released." |
| **Alternative Flow** | 5a. Reservation already cancelled: System displays error message. |
| **Postcondition** | Reservation status is CANCELLED. Train available seats are increased. |

### Use Case 6: Generate Booking Report

| Field | Description |
|-------|-------------|
| **Use Case ID** | UC-006 |
| **Use Case Name** | Generate Booking Report |
| **Actor** | Administrator |
| **Precondition** | User is logged in. User is on the Reports tab. |
| **Main Flow** | 1. User sets the "From" and "To" date fields. 2. User clicks "Booking Report" button. 3. System filters reservations by booking date within the range. 4. System calculates summary: total bookings, confirmed, cancelled, and total revenue. 5. System displays summary statistics cards and a detailed table of all matching reservations. |
| **Postcondition** | Report is displayed on screen. |

---

## 3. Class Diagram

```
+------------------+       +--------------------+       +----------------------+
|      User        |       |       Train        |       |     Passenger        |
+------------------+       +--------------------+       +----------------------+
| - userId: String |       | - trainId: String  |       | - passengerId: String|
| - username: String|      | - trainName: String|       | - fullName: String   |
| - password: String|      | - route: String    |       | - email: String      |
| - role: Role     |       | - departureStation |       | - phone: String      |
| - fullName: String|      | - arrivalStation   |       | - nationalId: String |
+------------------+       | - departureTime    |       | - registrationDate   |
| + getters/setters|       | - arrivalTime      |       +----------------------+
| + toString()     |       | - totalSeats: int  |       | + getters/setters    |
+------------------+       | - availableSeats   |       | + toString()         |
        |                  | - ticketPrice      |       +----------------------+
        |                  | - status: String   |               |
        |                  +--------------------+               |
        |                  | + getOccupancyRate()|              |
        |                  | + getters/setters   |              |
        |                  | + toString()        |              |
        |                  +--------------------+               |
        |                          |                            |
        v                          v                            v
+------------------+       +--------------------+       +----------------------+
| AuthController   |       |  TrainController   |       | PassengerController  |
+------------------+       +--------------------+       +----------------------+
| - dataStore      |       | - dataStore        |       | - dataStore          |
+------------------+       +--------------------+       +----------------------+
| + login()        |       | + addTrain()       |       | + addPassenger()     |
| + logout()       |       | + updateTrain()    |       | + updatePassenger()  |
| + isAdmin()      |       | + deleteTrain()    |       | + deletePassenger()  |
+------------------+       | + getAllTrains()    |       | + getAllPassengers() |
                           +--------------------+       +----------------------+

+----------------------+       +--------------------+
|    Reservation       |       | ReservationController|
+----------------------+       +--------------------+
| - reservationId      |       | - dataStore        |
| - passengerId        |       +--------------------+
| - passengerName      |       | + bookTicket()     |
| - trainId            |       | + cancelReservation()|
| - trainName          |       | + getAllReservations()|
| - bookingDate        |       | + getBookingConfirmation()|
| - travelDate         |       +--------------------+
| - seatCount: int     |
| - totalPrice: double |       +--------------------+
| - status: Status     |       |    DataStore       |
+----------------------+       | <<Singleton>>      |
| + getters/setters    |       +--------------------+
| + toString()         |       | - users: List      |
+----------------------+       | - trains: List     |
                               | - passengers: List |
                               | - reservations: List|
                               +--------------------+
                               | + loadAll()        |
                               | + saveAll()        |
                               | + authenticate()   |
                               | + CRUD methods     |
                               | + getStatistics()  |
                               +--------------------+
```

### Relationships:
- **DataStore** ←uses← All Controllers (Singleton dependency)
- **Reservation** has reference to **Passenger** (passengerId) and **Train** (trainId)
- **User** has Role enum (ADMIN, STAFF)
- **Reservation** has Status enum (CONFIRMED, CANCELLED, PENDING)
- Each **View panel** uses its corresponding **Controller**

---

## 4. Sequence Diagrams

### Sequence Diagram 1: Login Process

```
User        LoginFrame       AuthController      DataStore
 |              |                  |                  |
 |--enters credentials-->         |                  |
 |              |                  |                  |
 |--clicks Login-->               |                  |
 |              |--login(user,pass)-->               |
 |              |                  |--authenticate()-->
 |              |                  |                  |
 |              |                  |<--User object----|
 |              |                  |                  |
 |              |<--User object----|                  |
 |              |                  |                  |
 |              |--dispose()       |                  |
 |              |--new MainFrame(user)-->             |
 |<--MainFrame displayed-----------|                  |
```

### Sequence Diagram 2: Book Ticket

```
Staff     ReservationPanel   ReservationController   DataStore      Train
 |              |                    |                   |            |
 |--selects passenger,train-->      |                   |            |
 |--enters date, seats-->           |                   |            |
 |--clicks Book Ticket-->           |                   |            |
 |              |--bookTicket()---->|                   |            |
 |              |                   |--getPassengerById()-->         |
 |              |                   |<--Passenger--------|           |
 |              |                   |--getTrainById()--------------->|
 |              |                   |<--Train------------------------| 
 |              |                   |                   |            |
 |              |                   |  [validate: active, seats]     |
 |              |                   |                   |            |
 |              |                   |--calculate price  |            |
 |              |                   |--create Reservation|           |
 |              |                   |                   |            |
 |              |                   |--train.setAvailableSeats()---->|
 |              |                   |--updateTrain()---->|           |
 |              |                   |--addReservation()-->           |
 |              |                   |                   |--saveAll() |
 |              |                   |                   |            |
 |              |<--success---------|                   |            |
 |              |--show confirmation|                   |            |
 |<--confirmation dialog------------|                   |            |
```

### Sequence Diagram 3: Cancel Reservation

```
Staff     ReservationPanel   ReservationController   DataStore      Train
 |              |                    |                   |            |
 |--selects reservation-->          |                   |            |
 |--clicks Cancel-->                |                   |            |
 |              |--confirm dialog-->|                   |            |
 |--confirms Yes-->                 |                   |            |
 |              |--cancelReservation()-->               |            |
 |              |                   |--getReservationById()-->       |
 |              |                   |<--Reservation------|           |
 |              |                   |--getTrainById()--------------->|
 |              |                   |<--Train------------------------|
 |              |                   |--train.setAvailableSeats(+N)-->|
 |              |                   |--updateTrain()---->|           |
 |              |                   |--reservation.setStatus(CANCELLED)|
 |              |                   |--updateReservation()-->        |
 |              |                   |                   |--saveAll() |
 |              |<--success---------|                   |            |
 |<--success message----------------|                   |            |
```

---

## 5. Activity Diagrams

### Activity Diagram 1: Booking Process

```
[Start]
   |
   v
(Select Passenger from dropdown)
   |
   v
(Select Train from dropdown)
   |
   v
<Is train active?> ---No---> [Display error: "Train is not active"] --> [End]
   |
  Yes
   |
   v
(Enter travel date and seat count)
   |
   v
(Click "Book Ticket")
   |
   v
<Seat count > 0?> ---No---> [Display error: "Seat count must be at least 1"] --> [End]
   |
  Yes
   |
   v
<Enough seats available?> ---No---> [Display error: "Not enough seats"] --> [End]
   |
  Yes
   |
   v
<Travel date provided?> ---No---> [Display error: "Travel date required"] --> [End]
   |
  Yes
   |
   v
(Calculate total price = price * seats)
   |
   v
(Create Reservation with CONFIRMED status)
   |
   v
(Deduct seats from train availability)
   |
   v
(Save to DataStore)
   |
   v
(Display booking confirmation)
   |
   v
(Refresh tables)
   |
   v
[End]
```

### Activity Diagram 2: Login Process

```
[Start]
   |
   v
(Display Login Screen)
   |
   v
(User enters username and password)
   |
   v
(User clicks Login)
   |
   v
<Username empty?> ---Yes---> [Display error] --> (Return to login)
   |
  No
   |
   v
<Password empty?> ---Yes---> [Display error] --> (Return to login)
   |
  No
   |
   v
(Authenticate against stored users)
   |
   v
<Credentials valid?> ---No---> [Display "Invalid username or password"] --> (Return to login)
   |
  Yes
   |
   v
(Set current user session)
   |
   v
(Close login window)
   |
   v
(Open MainFrame with appropriate tabs)
   |
   v
[End]
```

### Activity Diagram 3: Generate Report

```
[Start]
   |
   v
(User selects date range: From and To)
   |
   v
(User clicks a report button)
   |
   v
<Which report?>
   |          |            |              |
Booking    Revenue    Utilization    Passenger
   |          |            |              |
   v          v            v              v
(Filter    (Filter     (Get all      (Get all
reservations reservations trains)    passengers)
by date)   by date)       |              |
   |          |            |              |
   v          v            v              v
(Calculate (Group by   (Calculate   (Calculate
summary    train,      occupancy    bookings &
stats)     compute     per train)   spending
           revenue)                  per passenger)
   |          |            |              |
   v          v            v              v
(Display   (Display    (Display     (Display
stats +    chart +     stats +      stats +
table)     table)      table)       table)
   |          |            |              |
   +----------+------------+--------------+
              |
              v
           [End]
```
