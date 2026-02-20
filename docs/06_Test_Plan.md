# Test Plan and Test Cases

## 1. Test Plan Overview

### 1.1 Objective
To verify that all implemented features of the Train Schedule and Reservation Management System function correctly, handle edge cases gracefully, and meet the specified requirements.

### 1.2 Testing Approach
The project follows Agile testing practices where development and testing are interleaved. Testing was performed incrementally at the end of each sprint. This document covers all test cases across all three sprints.

### 1.3 Test Environment
- **Operating System:** Windows 10/11
- **Java Version:** JDK 21
- **GUI Framework:** Java Swing
- **Storage:** File-based serialization (data/ folder)

### 1.4 Types of Testing Performed
- **Unit Testing:** Individual methods in controllers were tested for correct behavior.
- **Integration Testing:** End-to-end flows (e.g., register passenger then book ticket) were tested.
- **Functional Testing:** All use cases were tested against expected outcomes.
- **UI Testing:** All panels, buttons, dialogs, and data displays were verified.
- **Boundary Testing:** Edge cases like zero seats, empty fields, and duplicate data were tested.

---

## 2. Test Cases

### 2.1 Authentication Module

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-001 | Login with valid admin credentials | Username: admin, Password: admin123 | Login successful, MainFrame displayed with all tabs | MainFrame displayed with all tabs | PASS |
| TC-002 | Login with valid staff credentials | Username: staff, Password: staff123 | Login successful, MainFrame displayed | MainFrame displayed | PASS |
| TC-003 | Login with invalid username | Username: wronguser, Password: admin123 | Error: "Invalid username or password" | Error displayed correctly | PASS |
| TC-004 | Login with invalid password | Username: admin, Password: wrongpass | Error: "Invalid username or password" | Error displayed correctly | PASS |
| TC-005 | Login with empty username | Username: (empty), Password: admin123 | Error: "Invalid username or password" | Error displayed correctly | PASS |
| TC-006 | Login with empty password | Username: admin, Password: (empty) | Error: "Invalid username or password" | Error displayed correctly | PASS |
| TC-007 | Logout functionality | Click Logout button, confirm Yes | Return to LoginFrame | Returned to LoginFrame | PASS |
| TC-008 | Cancel logout | Click Logout button, confirm No | Remain on MainFrame | Remained on MainFrame | PASS |

### 2.2 Train Management Module

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-009 | Add train with valid data | Name: Express 1, Route: Riyadh-Jeddah, Stations: Riyadh/Jeddah, Times: 08:00/12:00, Seats: 200, Price: 150.00 | Train added, appears in table with auto-generated ID | Train added successfully | PASS |
| TC-010 | Add train with empty name | Name: (empty), other fields filled | Error: "All fields are required." | Error displayed | PASS |
| TC-011 | Add train with zero seats | Seats: 0 | Error: "Total seats must be greater than 0." | Error displayed | PASS |
| TC-012 | Add train with negative seats | Seats: -5 | Error: "Total seats must be greater than 0." | Error displayed | PASS |
| TC-013 | Add train with negative price | Price: -50 | Error: "Price cannot be negative." | Error displayed | PASS |
| TC-014 | Add train with non-numeric seats | Seats: "abc" | Error: "Seats must be integer, Price must be numeric." | Error displayed | PASS |
| TC-015 | Update train details | Select train, change name to "Express 2", click Update | Train name updated in table | Updated correctly | PASS |
| TC-016 | Update train - reduce seats below booked | Train has 50 booked seats, change total to 30 | Error: "Cannot set total seats below booked count (50)." | Error displayed | PASS |
| TC-017 | Delete train with no reservations | Select train with no bookings, click Delete, confirm | Train removed from table | Deleted successfully | PASS |
| TC-018 | Delete train with active reservations | Select train with confirmed bookings, click Delete | Error: "Cannot delete train with active reservations." | Error displayed | PASS |
| TC-019 | Clear form | Click Clear button | All form fields cleared, table selection cleared | Form cleared | PASS |
| TC-020 | Update without selection | Click Update without selecting a train | Warning: "Select a train to update." | Warning displayed | PASS |

### 2.3 Passenger Management Module

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-021 | Register passenger with valid data | Name: Ahmad Ali, Email: ahmad@email.com, Phone: 0551234567, NatID: 1234567890 | Passenger added with auto-generated ID | Added successfully | PASS |
| TC-022 | Register with empty fields | Name: (empty) | Error: "All fields are required." | Error displayed | PASS |
| TC-023 | Register with invalid email | Email: "notanemail" | Error: "Invalid email format." | Error displayed | PASS |
| TC-024 | Register with duplicate national ID | NatID: 1234567890 (already exists) | Error: "A passenger with this National ID already exists." | Error displayed | PASS |
| TC-025 | Update passenger details | Select passenger, change phone, click Update | Phone number updated in table | Updated correctly | PASS |
| TC-026 | Update with duplicate national ID | Change NatID to one belonging to another passenger | Error: "Another passenger with this National ID already exists." | Error displayed | PASS |
| TC-027 | Delete passenger with no reservations | Select passenger with no bookings, click Delete, confirm | Passenger removed from table | Deleted successfully | PASS |
| TC-028 | Delete passenger with active reservations | Select passenger with confirmed bookings | Error: "Cannot delete passenger with active reservations." | Error displayed | PASS |
| TC-029 | Registration date auto-set | Add new passenger | Registration date set to current date | Date set correctly | PASS |

### 2.4 Reservation and Ticketing Module

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-030 | Book ticket with valid data | Passenger: PSG-0001, Train: TRN-0001 (200 seats, SAR 150), Date: 2026-04-01, Seats: 2 | Reservation created, total = SAR 300, seats reduced to 198 | Booked, confirmation shown | PASS |
| TC-031 | Book ticket - insufficient seats | Request 300 seats on train with 200 available | Error: "Not enough seats available. Only 200 seats left." | Error displayed | PASS |
| TC-032 | Book ticket - zero seats | Seats: 0 | Error: "Seat count must be at least 1." | Error displayed | PASS |
| TC-033 | Book ticket - empty travel date | Travel date: (empty) | Error: "Travel date is required." | Error displayed | PASS |
| TC-034 | Book ticket - non-numeric seat count | Seats: "two" | Error: "Seat count must be a number." | Error displayed | PASS |
| TC-035 | Available seats display | Select a train in dropdown | Available seats and price per seat shown | Displayed correctly | PASS |
| TC-036 | Cancel confirmed reservation | Select confirmed reservation, click Cancel, confirm | Status changed to CANCELLED, seats restored | Cancelled, seats restored | PASS |
| TC-037 | Cancel already-cancelled reservation | Select cancelled reservation, click Cancel | Error: "This reservation is already cancelled." | Error displayed | PASS |
| TC-038 | View booking confirmation | Select reservation, click View Confirmation | Confirmation dialog with all details shown | Confirmation displayed | PASS |
| TC-039 | Booking auto-updates train availability | Book 5 seats on train with 200 available | Train available seats = 195 | Available seats = 195 | PASS |
| TC-040 | Cancellation restores seats | Cancel reservation of 5 seats | Train available seats increased by 5 | Seats restored | PASS |
| TC-041 | No passenger selected for booking | No passenger in dropdown, click Book | Warning: "Please select a passenger and a train." | Warning displayed | PASS |
| TC-042 | Refresh button updates dropdowns | Add new passenger/train, click Refresh | New items appear in dropdowns | Dropdowns updated | PASS |

### 2.5 Dashboard Module

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-043 | Dashboard stats display | Navigate to Dashboard tab | Total trains, active trains, passengers, bookings, revenue shown | All stats displayed | PASS |
| TC-044 | Dashboard stats accuracy | Add 3 trains, 5 passengers, 4 bookings (total SAR 600) | Stats: 3 trains, 5 passengers, 4 bookings, SAR 600 | Values match | PASS |
| TC-045 | Occupancy bar chart | Trains exist with varying occupancy | Bar chart shows bars with correct percentages | Chart displayed correctly | PASS |
| TC-046 | Reservation pie chart | Mix of confirmed and cancelled reservations | Pie chart with correct proportions and legend | Chart displayed correctly | PASS |
| TC-047 | Recent reservations table | Multiple reservations exist | Last 5 reservations shown in table | Displayed correctly | PASS |
| TC-048 | Dashboard refresh on tab switch | Switch away and back to Dashboard | Data refreshed with latest values | Refreshed correctly | PASS |

### 2.6 Reports Module

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-049 | Generate Booking Report | Date range covering existing bookings | Summary stats + detailed table displayed | Report generated | PASS |
| TC-050 | Booking Report - date filtering | Set narrow date range | Only bookings within range shown | Filtered correctly | PASS |
| TC-051 | Generate Revenue Report | Click Revenue Report | Revenue per train with bar chart and table | Report generated | PASS |
| TC-052 | Revenue Report accuracy | 3 bookings on Train A (SAR 450), 2 on Train B (SAR 300) | Train A: 450, Train B: 300, Total: 750 | Values match | PASS |
| TC-053 | Generate Utilization Report | Click Utilization Report | All trains with occupancy rates displayed | Report generated | PASS |
| TC-054 | Utilization Report - high utilization flag | Train with >75% occupancy | Shown in "High Utilization" count | Counted correctly | PASS |
| TC-055 | Generate Passenger Report | Click Passenger Report | Per-passenger booking counts and spending | Report generated | PASS |
| TC-056 | Report with no data | Generate report with no reservations | Report displays with zeros or empty table | Handled gracefully | PASS |

### 2.7 Data Persistence

| Test ID | Test Case | Input | Expected Result | Actual Result | Status |
|---------|-----------|-------|----------------|---------------|--------|
| TC-057 | Data saved on add | Add a train, close and reopen app | Train still exists | Data persisted | PASS |
| TC-058 | Data saved on update | Update a passenger, close and reopen app | Changes preserved | Data persisted | PASS |
| TC-059 | Data saved on delete | Delete a record, close and reopen app | Record no longer exists | Data persisted | PASS |
| TC-060 | First launch creates defaults | Delete data/ folder, launch app | Default admin and staff users created | Defaults created | PASS |
| TC-061 | ID counter persistence | Add 3 trains (TRN-0001 to 0003), restart, add another | Next train is TRN-0004 | Counter persisted | PASS |

---

## 3. Test Summary

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

## 4. Defects Found and Resolved

| Defect ID | Description | Sprint Found | Resolution | Sprint Fixed |
|-----------|-------------|-------------|------------|-------------|
| DEF-001 | ComboBox not refreshing after adding new passenger | Sprint 2 | Added refresh call after successful add operation | Sprint 2 |
| DEF-002 | Seat count not restoring properly on cancellation | Sprint 2 | Fixed seat restoration logic in cancelReservation() | Sprint 2 |
| DEF-003 | Dashboard stats not updating when switching tabs | Sprint 3 | Implemented Refreshable interface with tab change listener | Sprint 3 |
| DEF-004 | Report date filter including out-of-range bookings | Sprint 3 | Fixed date comparison logic to use String.compareTo() | Sprint 3 |
