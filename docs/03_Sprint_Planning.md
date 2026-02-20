# Sprint Planning

## Sprint Overview

The project is divided into 3 sprints, each 2-3 weeks long, following the Agile Scrum methodology.

---

## Sprint 1: Foundation and Core Models

**Duration:** 2 weeks
**Sprint Goal:** Establish the project foundation including architecture setup, data models, authentication, and the train management module.

### Sprint 1 Backlog

| Task ID | Task Description | Assigned To | Estimated Hours | Status |
|---------|-----------------|-------------|----------------|--------|
| T-001 | Set up project structure (MVC folders, build scripts) | Team Lead | 3 | Done |
| T-002 | Implement User model class | Member 1 | 2 | Done |
| T-003 | Implement Train model class | Member 1 | 2 | Done |
| T-004 | Implement Passenger model class | Member 2 | 2 | Done |
| T-005 | Implement Reservation model class | Member 2 | 2 | Done |
| T-006 | Implement DataStore (file-based persistence) | Member 3 | 6 | Done |
| T-007 | Implement UIStyle utility class | Member 4 | 4 | Done |
| T-008 | Implement AuthController (login/logout logic) | Member 3 | 3 | Done |
| T-009 | Implement LoginFrame (login GUI) | Member 4 | 4 | Done |
| T-010 | Implement MainFrame (tabbed main window) | Member 5 | 4 | Done |
| T-011 | Implement TrainController (CRUD logic) | Member 1 | 4 | Done |
| T-012 | Implement TrainPanel (train management GUI) | Member 5 | 6 | Done |
| T-013 | Write Project Initiation document | Member 6 | 3 | Done |
| T-014 | Write Product Backlog document | Team Lead | 4 | Done |
| T-015 | Testing and bug fixing for Sprint 1 | All Members | 4 | Done |

**Total Estimated Hours:** 53
**Sprint Velocity:** 35 story points

### Sprint 1 Meeting Log

**Meeting 1 - Sprint Planning (Day 1)**
- Discussed project requirements and scope.
- Assigned roles: Team Lead, developers, tester, documentation.
- Agreed on MVC architecture with Java Swing and file-based storage.
- Created initial product backlog and prioritized items.

**Meeting 2 - Daily Standup (Day 4)**
- Models completed (User, Train, Passenger, Reservation).
- DataStore implementation in progress.
- UIStyle utility class completed.
- No blockers reported.

**Meeting 3 - Daily Standup (Day 7)**
- DataStore persistence fully functional.
- Login screen completed and tested.
- Train management panel in progress.
- Blocker: Discussed table selection event handling approach.

**Meeting 4 - Sprint Review (Day 14)**
- All Sprint 1 tasks completed.
- Demonstrated working login and train CRUD operations.
- DataStore saves and loads data across sessions.
- Sprint 1 documentation finalized.

---

## Sprint 2: Passenger Management, Reservations, and System Design

**Duration:** 3 weeks
**Sprint Goal:** Implement passenger management, reservation and ticketing module, system design documentation with UML diagrams, and architectural design documentation.

### Sprint 2 Backlog

| Task ID | Task Description | Assigned To | Estimated Hours | Status |
|---------|-----------------|-------------|----------------|--------|
| T-016 | Implement PassengerController (CRUD logic) | Member 1 | 4 | Done |
| T-017 | Implement PassengerPanel (passenger GUI) | Member 2 | 6 | Done |
| T-018 | Implement ReservationController (booking/cancel logic) | Member 3 | 6 | Done |
| T-019 | Implement ReservationPanel (ticketing GUI) | Member 4 | 8 | Done |
| T-020 | Implement booking confirmation generation | Member 3 | 3 | Done |
| T-021 | Implement seat availability validation and auto-update | Member 3 | 3 | Done |
| T-022 | Create Use Case Diagram | Member 5 | 3 | Done |
| T-023 | Write Use Case Scenarios | Member 5 | 4 | Done |
| T-024 | Create Class Diagram | Member 6 | 3 | Done |
| T-025 | Create Sequence Diagrams | Member 6 | 4 | Done |
| T-026 | Create Activity Diagrams | Member 5 | 3 | Done |
| T-027 | Write Architectural Design document (MVC justification) | Team Lead | 4 | Done |
| T-028 | Integration testing for passenger and reservation modules | All Members | 4 | Done |
| T-029 | Sprint 2 documentation and review | Team Lead | 3 | Done |

**Total Estimated Hours:** 58
**Sprint Velocity:** 42 story points

### Sprint 2 Meeting Log

**Meeting 5 - Sprint Planning (Day 1)**
- Reviewed Sprint 1 deliverables.
- Planned passenger management and reservation module tasks.
- Divided UML diagram responsibilities among team members.

**Meeting 6 - Daily Standup (Day 5)**
- Passenger CRUD fully functional.
- Reservation controller in progress.
- Use case diagram drafted.
- Blocker: ComboBox population timing needed discussion.

**Meeting 7 - Daily Standup (Day 10)**
- Reservation module completed with seat validation.
- Booking confirmations working correctly.
- UML diagrams under review.
- Integration testing started.

**Meeting 8 - Sprint Review (Day 21)**
- All Sprint 2 features implemented.
- Demonstrated full booking flow: register passenger, select train, book ticket, view confirmation, cancel ticket.
- UML diagrams and architectural document finalized.

---

## Sprint 3: Dashboard, Reports, Testing, and Final Release

**Duration:** 2 weeks
**Sprint Goal:** Implement the administrative dashboard, reporting module, prepare comprehensive test cases, and deliver the first release.

### Sprint 3 Backlog

| Task ID | Task Description | Assigned To | Estimated Hours | Status |
|---------|-----------------|-------------|----------------|--------|
| T-030 | Implement DashboardPanel (stats, charts) | Member 4 | 6 | Done |
| T-031 | Implement occupancy bar chart on dashboard | Member 4 | 3 | Done |
| T-032 | Implement reservation pie chart on dashboard | Member 4 | 3 | Done |
| T-033 | Implement ReportsPanel (report controls) | Member 5 | 4 | Done |
| T-034 | Implement Booking Report generation | Member 5 | 4 | Done |
| T-035 | Implement Revenue Analysis Report | Member 5 | 4 | Done |
| T-036 | Implement Utilization Report | Member 6 | 3 | Done |
| T-037 | Implement Passenger Activity Report | Member 6 | 3 | Done |
| T-038 | Prepare Test Plan and Test Cases | Member 1 | 6 | Done |
| T-039 | Execute test cases and document results | Member 2 | 4 | Done |
| T-040 | Final integration and regression testing | All Members | 4 | Done |
| T-041 | Prepare GitHub repository and first release | Team Lead | 3 | Done |
| T-042 | Prepare presentation slides | Member 3 | 4 | Done |
| T-043 | Write README and final documentation | Team Lead | 3 | Done |

**Total Estimated Hours:** 54
**Sprint Velocity:** 33 story points

### Sprint 3 Meeting Log

**Meeting 9 - Sprint Planning (Day 1)**
- Reviewed Sprint 2 deliverables.
- Planned dashboard, reports, testing, and release tasks.
- Assigned presentation responsibilities to all team members.

**Meeting 10 - Daily Standup (Day 4)**
- Dashboard with stats cards completed.
- Bar chart and pie chart implemented.
- Reports panel framework set up.
- Test plan drafted.

**Meeting 11 - Daily Standup (Day 8)**
- All four report types completed.
- Test cases being executed.
- GitHub repository created.
- Minor UI polish in progress.

**Meeting 12 - Sprint Review (Day 14)**
- All Sprint 3 features completed.
- Full system demo with all modules.
- Test results documented.
- First release uploaded to GitHub.
- Presentation slides prepared.

---

## Sprint Velocity Summary

| Sprint | Story Points Planned | Story Points Completed | Velocity |
|--------|---------------------|----------------------|----------|
| Sprint 1 | 35 | 35 | 35 |
| Sprint 2 | 42 | 42 | 42 |
| Sprint 3 | 33 | 33 | 33 |
| **Total** | **110** | **110** | **Avg: 37** |
