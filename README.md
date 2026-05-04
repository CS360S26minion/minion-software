# Product Backlog: Counseling Appointment System

This document contains the initial product backlog for the Counseling Appointment System, prioritized by core booking flow, notifications, and progressive features.

### 1. Core Booking Flow & Availability

| ID | Title | User Story | Story Points | Risk Level | Checkpoint Release |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **US01** | View Counselor List | **As a student**, I want to view a list of available counselors and their specialties so that I can choose one that fits my needs. | 2 | Low | Yes |
| **US02** | View Available Slots | **As a student**, I want to view available time slots for a specific counselor so that I can find a time that works with my schedule. | 5 | Medium | Yes |
| **US03** | Book Time Slot | **As a student**, I want to select and book an available time slot so that I can secure a counseling session. | 5 | High | Yes |
| **US04** | Cancel or Reschedule | **As a student**, I want to cancel or reschedule an upcoming appointment so that I can free up the slot if my plans change. | 5 | Medium | Yes |
| **US05** | Set Availability | **As a counselor**, I want to set my available working hours and recurring breaks so that students only book slots when I am actually available. | 5 | Medium | Yes |
| **US06** | View Appointments | **As a counselor**, I want to view my daily and weekly appointment schedule on a dashboard so that I can prepare for my upcoming sessions. | 3 | Low | Yes |

### 2. Notifications & Tracking

| ID | Title | User Story | Story Points | Risk Level | Checkpoint Release |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **US07** | Student Booking Confirmation | **As a student**, I want to receive an automated email confirmation when I book an appointment so that I have a reliable record of the schedule. | 3 | Medium | Yes |
| **US08** | Student Appointment Reminder | **As a student**, I want to receive an automated reminder 24 hours before my appointment so that I don't forget and miss the session. | 5 | High | Yes |
| **US09** | Counselor Booking Confirmation| **As a counselor**, I want to receive an automated email confirmation when someone books an apointment with me so that I have a reliable record of the schedule. | 3 | Medium | Yes |
| **US10** | Counselor Appt Reminder | **As a counselor**, I want to receive an automated reminder 24 hours before the appointment booked with me so that I don't forget and miss the session. | 5 | High | Yes |
| **US11** | Mark No-Show | **As a counselor**, I want to be able to mark an appointment as a "no-show" directly from my dashboard so that the office can track attendance. | 2 | Low | No |
| **US12** | View Appointment History | **As a student**, I want to view my past and upcoming appointment history so that I can keep track of my counseling journey. | 3 | Low | No |

### 3. Progressive Features

| ID | Title | User Story | Story Points | Risk Level | Checkpoint Release |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **US13** | Submit Intake Form | **As a student**, I want to fill out a secure pre-session intake form during the booking process so that my counselor has context before we meet. | 5 | High | No |
| **US14** | Submit Session Feedback | **As a student**, I want to submit an anonymous post-session feedback form so that I can evaluate the helpfulness of the service. | 3 | Low | No |
| **US15** | View Admin Analytics | **As an admin**, I want to view aggregate no-show tracking and analytics so that I can identify trends and improve clinic efficiency. | 8 | Medium | No |
| **US16** | Manage Counselor Profiles | **As an admin**, I want to manage counselor profiles, system access, and roles so that the platform remains secure and up to date. | 3 | Low | No |

### Screenshot of backlog:
<img width="446" height="620" alt="image" src="https://github.com/user-attachments/assets/b7d11172-dcf1-4435-bec2-6fd2d540563e" />


## Project Structure

```text
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/seproj/
│   │   │   ├── model/                # Core data models
│   │   │   │   ├── AppNotification
│   │   │   │   ├── AppointmentSlot
│   │   │   │   ├── Availability
│   │   │   │   ├── Counselor
│   │   │   │   └── Student
│   │   │   │
│   │   │   ├── repository/           # Firestore/database access layer
│   │   │   │   ├── AppointmentSlotRepository
│   │   │   │   ├── AvailabilityRepository
│   │   │   │   ├── CounselorRepository
│   │   │   │   ├── NotificationRepository
│   │   │   │   └── StudentRepository
│   │   │   │
│   │   │   ├── service/              # Business logic layer
│   │   │   │   ├── AvailabilityService
│   │   │   │   ├── BookingService
│   │   │   │   ├── NotificationService
│   │   │   │   └── ReminderSchedulerService
│   │   │   │
│   │   │   ├── notifications/        # Notification helper utilities
│   │   │   │   └── NotificationHelper
│   │   │   │
│   │   │   ├── workers/              # Background reminder workers
│   │   │   │   ├── CounselorReminderWorker
│   │   │   │   └── StudentReminderWorker
│   │   │   │
│   │   │   ├── ui/                   # User interface layer
│   │   │   │   ├── common/           # Shared activities, adapters, custom views
│   │   │   │   ├── counselor/        # Counselor-facing screens
│   │   │   │   └── student/          # Student-facing screens
│   │   │   │
│   │   │   └── utils/                # Utility classes/interfaces
│   │   │       └── FirestoreCallback
│   │   │
│   │   ├── res/                      # Layouts, drawables, strings, etc.
│   │   └── AndroidManifest.xml
│   │
│   ├── test/
│   │   └── java/com/example/seproj/  # Local unit tests (run on JVM)
│   │       ├── AppNotificationTest
│   │       ├── AppointmentSlotTest
│   │       ├── AvailabilityTest
│   │       ├── CounselorTest
│   │       └── StudentTest
│   │
│   └── androidTest/
│       └── java/com/example/seproj/  # Instrumentation/UI/integration tests
│           ├── ReminderIntegrationTest
│           ├── ReminderWorkerTest
│           ├── CounselorListActivityTest
│           ├── StudentAppointmentsActivityTest
│           ├── NotificationsActivityTest
│           └── ...

```

## CRC Tables

Each card identifies one of the system's core classes, describes what it is responsible for, and lists the other classes it must interact with to carry out those responsibilities. User story references are included for traceability back to the product backlog. We group them in 3 layers.

### Actors CRCs

Student, Counselor, Admin: the human roles that interact with the system.

![Actors CRC](CRC/actors.png)

### Core Domain CRCs

AppointmentSlot, Availability, BookingSystem: the central scheduling logic.

![Core-Domain CRC](CRC/core-domain.png)

### Support Services CRCs

IntakeForm, FeedbackForm, NoShowRecord: features that support the core flow.

![Support-Services CRC](CRC/support-services.png)



Sc


## 🔗 Figma Project
Access the interactive high-fidelity prototype here:  
**[Figma Design File](https://www.figma.com/design/hfGdG7uPsVATekhhlydlSo/SE-Project?node-id=0-1&t=TYQRLZJySw3304cT-1)**

---

# 🎬 Project Storyboard & User Journey

This section demonstrates the user flow and transitions between different states of the UI, covering all **16 User Stories**.

### 📱 UI Gallery (Overview)

| Student Dashboard | Counselor Search | Booking Flow |
| :---: | :---: | :---: |
| <img src="UI/student_page.png" width="180"> | <img src="UI/counselor_discovery.png" width="180"> | <img src="UI/booking_flow.png" width="180"> |
| **Intake Form** | **Appt Details** | **Counselor Hub** |
| <img src="UI/intake_form.png" width="180"> | <img src="UI/appointment_detail.png" width="180"> | <img src="UI/counselor_hub.png" width="180"> |
| **Admin Center** | **Notifications** | **Feedback Form** |
| <img src="UI/admin_center.png" width="180"> | <img src="UI/notification.png" width="180"> | <img src="UI/feedback_form.png" width="180"> |

---

### Step 1: Student Dashboard (US12)
*   **Initial State:** User logs in to the mobile app.
*   **Action:** User views upcoming sessions and history.
*   **Transition:** User clicks "Schedule New Session" to find a guide.

<img src="UI/student_page.png" width="300" alt="Student Page" />

---

### Step 2: Counselor Discovery (US01)
*   **Initial State:** Search directory of available counselors.
*   **Action:** User filters by specialty and selects "Dr. Sarah Jenkins".
*   **Transition:** System loads the counselor's private booking calendar.

<img src="UI/counselor_discovery.png" width="300" alt="Counselor Discovery" />

---

### Step 3: Booking Flow (US02, US03)
*   **Initial State:** Calendar view with available slots.
*   **Action:** User selects a date and a 10:30 AM time slot.
*   **Transition:** State changes to "Data Intake" to collect pre-session info.

<img src="UI/booking_flow.png" width="300" alt="Booking Flow" />

---

### Step 4: Pre-Session Intake (US13)
*   **Initial State:** Form asking for current mood and goals.
*   **Action:** User fills out the form and clicks "Confirm & Finish".
*   **Transition:** Data is recorded, and system triggers confirmation notifications.

<img src="UI/intake_form.png" width="300" alt="Intake Form" />

---

### Step 5: Appointment Management (US04)
*   **Initial State:** Confirmation/Detail view.
*   **Action:** User can view active details or choose to Reschedule/Cancel.
*   **Transition:** User returns to dashboard or modifies the appointment.

<img src="UI/appointment_detail.png" width="300" alt="Appointment Detail" />

---

### Step 6: Session Feedback (US14)
*   **Initial State:** Post-session reflection screen.
*   **Action:** User provides a star rating and anonymous comments.
*   **Transition:** Feedback is stored for admin analytics and quality control.

<img src="UI/feedback_form.png" width="300" alt="Feedback Form" />

---

### Step 7: Counselor Hub (US05, US06, US11)
*   **Initial State:** Counselor's daily agenda.
*   **Action:** Counselor toggles availability or marks a "No-Show".
*   **Transition:** System updates real-time availability for students.

<img src="UI/counselor_hub.png" width="300" alt="Counselor Hub" />

---

### Step 8: Admin Center (US15, US16)
*   **Initial State:** Clinic overview dashboard.
*   **Action:** Admin monitors no-show trends and manages staff profiles.
*   **Transition:** Administrative updates propagate throughout the system.

<img src="UI/admin_center.png" width="300" alt="Admin Center" />

---

### Step 9: System Notifications (US07, US08, US09, US10)
*   **Result:** Automated emails sent to both Student and Counselor.
*   **Content:** Confirmation details and 24-hour reminders.

<img src="UI/notification.png" width="300" alt="Notification" />
---

## Meeting Minutes

### Meeting 1 — February 20, 2026
| Field | Info |
|-------|------|
| Date | February 20, 2026 |
| Time | 7:34 PM – 7:39 PM |
| Duration | 5 minutes |
| Meeting With | TA |

**Attendees:** Iyan Aamir Aslam, Dayyan Ali Akhtar, Qayyum Ahmed, Shaheer Ahmad, Eman Nabeel

**Discussion:**
The team met with the TA to discuss the initial phase of the Counseling Appointment System project. We went over how to set up our GitHub Organization, repository structure, and Wiki for managing project artifacts. The meeting also covered the basics of building our product backlog, which would later outline the core features of our Counseling Appointment System, designed to streamline session booking between students and counselors.

## Meeting Minutes

### Meeting 2 — March 5, 2026
| Field | Info |
|-------|------|
| Date | March 5, 2026 |
| Time | 3:00 PM – 3:10 PM |
| Duration | 10 minutes |
| Meeting With | TA |

**Attendees:** Iyan Aamir Aslam, Dayyan Ali Akhtar, Qayyum Ahmed, Shaheer Ahmad, Eman Nabeel

**Discussion:**  
The team met with the TA to discuss the product backlog, the requirements for the first deliverable, and questions related to the CRC tables. The TA reviewed the team’s progress and clarified the structure and purpose of the backlog items and class responsibilities.

### Meeting 3 — March 18, 2026
| Field | Info |
|-------|------|
| Date | March 18, 2026 |
| Time | 7:25 PM – 7:40 PM |
| Duration | 15 minutes |
| Meeting With | TA |

**Attendees:** Iyan Aamir Aslam, Dayyan Ali Akhtar, Qayyum Ahmed, Shaheer Ahmad

**Discussion:**  
The team met with the TA to present and discuss the CRC tables and the UI gallery. The TA reviewed the class responsibilities, interactions, and the overall UI flow shown in the gallery, and provided feedback on the consistency between the backlog, CRC tables, and prototype screens.## Meeting Minutes

# Part 3
## Part 3 Sprint Planning

### 📅 Sprint Overview

The sprint ran across **three weeks**, with each week targeting a specific layer of the system. Planning was done upfront and reviewed iteratively with the TA.

---

### 📆 Week 1 — Foundation & Core UI (March 20 – March 26, 2026)

**Goal:** Set up the project skeleton, authentication flow, and student-facing read views.

| Team Member | Planned Task | User Story |
|-------------|-------------|------------|
| Dayyan | Implement Counselor List screen with Firestore query | US01 |
| Dayyan | Implement Available Slots screen with real-time slot loading | US02 |
| Qayyum | Build Counselor Hub dashboard UI (daily/weekly agenda view) | US06 |
| Shaheer | Set up Firestore schema for AppointmentSlot and Availability models | US03, US04 |
| Ayan | Set up email notification infrastructure (templates + triggers) | US07, US09 |
| Eman | Research WorkManager API and design reminder scheduling architecture | US08, US10 |

---

### 📆 Week 2 — Core Transactions & Counselor Features (March 27 – April 2, 2026)

**Goal:** Implement the core booking/cancellation logic, counselor availability management, and event-driven notifications.

| Team Member | Planned Task | User Story |
|-------------|-------------|------------|
| Shaheer | Implement Book Time Slot with concurrency/double-booking prevention | US03 |
| Shaheer | Implement Cancel and Reschedule logic with slot state management | US04 |
| Qayyum | Implement Set Availability screen with recurring schedule support | US05 |
| Qayyum | Wire counselor dashboard to live Firestore appointment data | US06 |
| Ayan | Trigger student confirmation email on successful booking | US07 |
| Ayan | Trigger counselor confirmation email on new booking event | US09 |
| Eman | Implement StudentReminderWorker with 24h WorkManager scheduling | US08 |
| Eman | Implement CounselorReminderWorker with 24h WorkManager scheduling | US10 |
| Dayyan | Connect Available Slots UI to live BookingService data | US02 |

---

### 📆 Week 3 — Integration, Testing & Sprint Review (April 3 – April 6, 2026)

**Goal:** End-to-end integration, bug fixing, internal testing, and demo preparation for Sprint Review 2.

| Team Member | Planned Task | User Story |
|-------------|-------------|------------|
| Shaheer | End-to-end testing of booking and cancellation flows | US03, US04 |
| Dayyan | Integration testing of counselor list and slot views with live data | US01, US02 |
| Qayyum | Validate availability and dashboard sync across student/counselor views | US05, US06 |
| Ayan | Verify confirmation email delivery for both student and counselor | US07, US09 |
| Eman | Verify 24-hour reminder triggers fire correctly end-to-end | US08, US10 |
| All | Polish UI, fix review feedback from Sprint Review 1, prepare demo | — |

## Part 3 Sprint Planning

This sprint focuses on implementing core functionality for the counseling appointment system, including booking logic, user interfaces, and notification mechanisms. Tasks are distributed based on system layers and complexity to ensure balanced workload and parallel development.

### 👤 Shaheer — Core Transactional Logic (13 pts)
Responsible for handling critical database operations and ensuring system consistency. This includes managing concurrency to prevent issues such as double-booking.

- **US03: Book Time Slot** (5 pts, High Risk)  
- **US04: Cancel or Reschedule Appointment** (5 pts, Medium Risk)

### 👤 Dayyan — Student Discovery & Read Views (7 pts)
Focuses on student-facing features, including UI components and database queries for browsing available counselors and time slots.

- **US01: View Counselor List** (2 pts, Low Risk)  
- **US02: View Available Slots** (5 pts, Medium Risk)

### 👤 Qayyum — Counselor State & Dashboards (8 pts)
Manages counselor-side functionality, including setting availability and viewing scheduled appointments.

- **US05: Set Availability** (5 pts, Medium Risk)  
- **US06: View Appointments** (3 pts, Low Risk)

### 👤 Ayan — Event-Driven Notifications (6 pts)
Implements real-time notifications triggered by booking and cancellation events.

- **US07: Student Booking Confirmation** (3 pts, Medium Risk)  
- **US09: Counselor Booking Confirmation** (3 pts, Medium Risk)

### 👤 Eman — Scheduled Background Tasks (10 pts)
Handles time-based features using background workers to trigger reminders for upcoming appointments.

- **US08: Student Appointment Reminder (24h prior)** (5 pts, High Risk)  
- **US10: Counselor Appointment Reminder (24h prior)** (5 pts, High Risk)

### 📊 Summary

| Team Member | Responsibility Area                | Story Points |
|------------|----------------------------------|-------------|
| Shaheer    | Core Transaction Logic           | 10 pts      |
| Dayyan     | Student UI & Data Retrieval      | 7 pts       |
| Qayyum     | Counselor Features & Dashboard   | 8 pts       |
| Ayan       | Event-Driven Notifications       | 6 pts       |
| Eman       | Background Scheduling & Reminders| 10 pts      |

**Total Sprint Load: 44 Story Points**

### 🎯 Sprint Goals

- Ensure reliable booking and cancellation logic  
- Provide intuitive UI for both students and counselors  
- Deliver real-time and scheduled notification features  
- Maintain data consistency and prevent conflicts (e.g., double-booking)

## Part 3 Sprint Reviews

### Sprint Review 1 — April 1, 2026
| Field | Info |
|-------|------|
| Date | April 1, 2026 |
| Time | 9:00 PM – 9:30 PM |
| Duration | 30 minutes |
| Meeting With | TA |

**Attendees:** Iyan Aamir Aslam, Dayyan Ali Akhtar, Qayyum Ahmed, Shaheer Ahmad, Eman Nabeel  

**Discussion:**  
The team met with the TA to discuss the second deliverable. The session focused on clarifying requirements and resolving outstanding questions. The team also presented their implementation plan, and the TA provided feedback to ensure alignment with project expectations.

### Sprint Review 2 — April 6, 2026
| Field | Info |
|-------|------|
| Date | April 6, 2026 |
| Time | 12:00 AM – 12:20 AM |
| Duration | 20 minutes |
| Meeting With | TA |

**Attendees:** Iyan Aamir Aslam, Dayyan Ali Akhtar, Qayyum Ahmed, Shaheer Ahmad, Eman Nabeel  

**Discussion:**  
The team met with the TA to discuss the second deliverable. The session focused on reviewing working app with its core features. The team dicussed last minute issues that they were solving. The team also shared that they had conducted a user study with LUMS students to guage whether they would be interested and comforable with a few new features the team was trying to add.

## Part 3 Updated Backlog
<img width="1108" height="586" alt="image" src="https://github.com/user-attachments/assets/b5b0b30a-9fa0-400a-93a0-dc468cf939a8" />


## Part 3 Updated UML Diagrams

![UML Diagram](UI/uml-diagram-p3.png)

## Part 3 Updated UI Mockups
<img width="1514" height="1339" alt="image" src="https://github.com/user-attachments/assets/bd466bba-6be4-40c7-b035-6a6f17971a8d" />
<img width="1301" height="653" alt="image" src="https://github.com/user-attachments/assets/b3acd511-96a8-4175-a469-5b74470b2451" />

With Arrows:
<img width="640" height="601" alt="image" src="https://github.com/user-attachments/assets/128e8202-43d8-406c-b291-4932b8ad0782" />
Miro Board Link: https://miro.com/app/board/uXjVGmd8e-s=/

## Part 3: The Extra Mile

To better understand user needs and validate potential improvements, we conducted a user study with LUMS students focusing on their experiences with counseling appointment systems. The study gathered feedback on common pain points, preferred interfaces, and desired features.

The results highlighted several recurring issues, including difficulty in finding suitable time slots, confusing booking processes, lack of clear confirmations, and challenges with rescheduling. A significant number of participants emphasized the importance of viewing counselor availability before booking and expressed a strong preference for simpler, more intuitive user interfaces.

### 📊 Key Insights

- ~85% of users found **appointment reminders** very helpful  
- ~60% of users preferred a **calendar-based view** for booking  
- ~80% of users expressed interest in **AI-based insights** (e.g., summaries)  
- ~75% of users highlighted the need for a **simpler and more intuitive UI**

### 🔗 Mapping Insights to Existing Features

These findings validate several features already implemented in our system:
- Reminder notifications directly address missed appointments  
- Availability viewing aligns with user demand for transparency before booking  
- Simplified UI flows address confusion in the booking process  

### 🚀 Planned Enhancements

Based on the study, we will extend the system by introducing:
- A new user story for **AI-based insights on the user dashboard**  
- An **optional calendar view** for browsing and booking available time slots  

These additions aim to directly address user pain points and further improve usability and engagement.

![User Study Insights](UI/userstudy.png)

# Final Deliverable
## Final Deliverable Sprint Planning

## Final Deliverable Sprint Review

## Final Deliverable Updated Backlog

## Final Deliverable Updated UI Mockups

## Final Deliverable Extra Mile
