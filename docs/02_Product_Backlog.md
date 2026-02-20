# Product Backlog

## Overview

This document presents the complete product backlog for the Train Schedule and Reservation Management System. Each backlog item is assigned a priority (High, Medium, Low), estimated effort in story points, and categorized by module.

---

## Backlog Items

### Module 1: Authentication and Security

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-001 | As an administrator, I want to log in with my username and password so that I can access the system securely. | High | 3 | Done |
| US-002 | As a staff member, I want to log in with my credentials so that I can process bookings and manage passengers. | High | 2 | Done |
| US-003 | As a user, I want to log out of the system so that my session is terminated securely. | High | 1 | Done |
| US-004 | As an administrator, I want the system to have default admin and staff accounts so that the system is usable on first launch. | High | 2 | Done |
| US-005 | As an administrator, I want role-based access control so that staff members only access authorized modules. | Medium | 3 | Done |

### Module 2: Train and Schedule Management

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-006 | As an administrator, I want to add a new train with its name, route, stations, times, seat capacity, and price so that it becomes available for booking. | High | 5 | Done |
| US-007 | As an administrator, I want to update train details so that schedule changes are reflected in the system. | High | 3 | Done |
| US-008 | As an administrator, I want to delete a train that has no active reservations so that outdated schedules are removed. | High | 3 | Done |
| US-009 | As a staff member, I want to view all trains in a table so that I can quickly find train information. | High | 2 | Done |
| US-010 | As an administrator, I want the system to prevent deletion of trains with active reservations so that bookings are not lost. | High | 2 | Done |
| US-011 | As an administrator, I want the system to prevent reducing total seats below the number of booked seats so that existing reservations remain valid. | Medium | 2 | Done |
| US-012 | As a user, I want to see train status (Active, Cancelled, Completed) so that I know which trains are available. | Medium | 1 | Done |

### Module 3: Passenger Information Management

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-013 | As a staff member, I want to register a new passenger with their full name, email, phone, and national ID so that their profile is stored in the system. | High | 5 | Done |
| US-014 | As a staff member, I want to update passenger information so that records stay current. | High | 3 | Done |
| US-015 | As a staff member, I want to delete a passenger who has no active reservations so that unused records are cleaned up. | Medium | 3 | Done |
| US-016 | As a staff member, I want the system to validate that no two passengers share the same national ID so that data integrity is maintained. | High | 2 | Done |
| US-017 | As a staff member, I want to view all passengers in a table format so that I can search and select passengers easily. | High | 2 | Done |
| US-018 | As a staff member, I want the system to validate email format during registration so that contact details are accurate. | Medium | 1 | Done |
| US-019 | As a staff member, I want the system to automatically record the registration date when a passenger is added. | Low | 1 | Done |

### Module 4: Reservation and Ticketing

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-020 | As a staff member, I want to book a ticket by selecting a passenger, a train, a travel date, and the number of seats so that a reservation is created. | High | 5 | Done |
| US-021 | As a staff member, I want the system to validate seat availability before confirming a booking so that overbooking is prevented. | High | 3 | Done |
| US-022 | As a staff member, I want the system to automatically update remaining seat capacity after a booking so that availability is accurate. | High | 3 | Done |
| US-023 | As a staff member, I want to cancel a reservation so that seats are released back to the train. | High | 3 | Done |
| US-024 | As a staff member, I want to see a booking confirmation with all reservation details after a successful booking. | High | 2 | Done |
| US-025 | As a staff member, I want to view all reservations in a table showing reservation ID, passenger, train, dates, seats, price, and status. | High | 2 | Done |
| US-026 | As a staff member, I want to view the booking confirmation for any existing reservation so that I can provide it to passengers on request. | Medium | 2 | Done |
| US-027 | As a staff member, I want to see available seats and price per seat when selecting a train so that I can inform the passenger before booking. | Medium | 2 | Done |
| US-028 | As a staff member, I want the system to prevent booking on inactive trains so that only valid schedules are used. | Medium | 1 | Done |

### Module 5: Administrative Dashboard

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-029 | As an administrator, I want to see total trains, active trains, total passengers, active bookings, and total revenue on a dashboard so that I have an operational overview. | High | 5 | Done |
| US-030 | As an administrator, I want to see a bar chart of occupancy rates per train so that I can identify underutilized or overbooked trains. | Medium | 3 | Done |
| US-031 | As an administrator, I want to see a pie chart of reservation statuses (Confirmed, Cancelled, Pending) so that I can monitor booking trends. | Medium | 3 | Done |
| US-032 | As an administrator, I want to see the most recent reservations on the dashboard so that I can track recent activity. | Low | 2 | Done |

### Module 6: Reporting and Analytics

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-033 | As an administrator, I want to generate a booking report filtered by date range showing total, confirmed, cancelled bookings and revenue so that I can evaluate performance over a period. | High | 5 | Done |
| US-034 | As an administrator, I want to generate a revenue analysis report showing revenue per train with a bar chart so that I can identify the most profitable routes. | High | 5 | Done |
| US-035 | As an administrator, I want to generate a train utilization report showing occupancy rates, booked seats, and available seats per train so that I can optimize capacity. | Medium | 3 | Done |
| US-036 | As an administrator, I want to generate a passenger activity report showing total bookings, active bookings, and total spending per passenger so that I can identify frequent travelers. | Medium | 3 | Done |

### Module 7: Data Persistence

| ID | User Story | Priority | Story Points | Status |
|----|-----------|----------|-------------|--------|
| US-037 | As a user, I want all data to be saved automatically so that information persists between application sessions. | High | 5 | Done |
| US-038 | As a user, I want the system to create default accounts on first launch so that the system is immediately usable. | High | 2 | Done |
| US-039 | As a user, I want unique auto-generated IDs for trains, passengers, and reservations so that records are uniquely identifiable. | High | 2 | Done |

---

## Backlog Summary

| Priority | Total Items | Completed |
|----------|------------|-----------|
| High | 24 | 24 |
| Medium | 12 | 12 |
| Low | 3 | 3 |
| **Total** | **39** | **39** |

**Total Story Points Estimated: 110**
**Total Story Points Completed: 110**
