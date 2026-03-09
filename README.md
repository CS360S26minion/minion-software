# Product Backlog: Counseling Appointment System

This document contains the initial product backlog for the Counseling Appointment System, prioritized by core booking flow, notifications, and progressive features.

### 1. Core Booking Flow & Availability

| ID | Title | User Story | Story Points | Risk Level | Checkpoint Release |
| :--- | :--- | :--- | :---: | :---: | :---: |
| **US01** | View Counselor List | **As a student**, I want to view a list of available counselors and their specialties so that I can choose one that fits my needs. | 2 | Low | Yes |
| **US02** | View Available Slots | **As a student**, I want to view a calendar of available time slots for a specific counselor so that I can find a time that works with my schedule. | 5 | Medium | Yes |
| **US03** | Book Time Slot | **As a student**, I want to select and book an available time slot so that I can secure a counseling session. | 8 | High | Yes |
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


## CRC Tables

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
