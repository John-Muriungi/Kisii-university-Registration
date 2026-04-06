# Kisii University - Student Registration System 🎓

A full-stack, database-driven desktop application designed for managing student enrollments, course catalogs, and academic transcript tracking. Built using Java Swing for a responsive Graphical User Interface (GUI) and MySQL via XAMPP for secure data persistence.



## 📌 Project Overview
This software was developed to simulate a multi-department university registration platform. It utilizes the **Model-View-Controller (MVC)** architectural pattern to cleanly decouple database operations, system logic, and interactive display frames.

## 🚀 Key Features

* **Centralized Faculty Portal:** A master dashboard routing users to specific office modules.
* **Admissions Module:** A GUI form allowing quick inserts of newly admitted students directly into the secure database.
* **Intelligent Advisor Panel:** Auto-checks student records for:
    * Good academic standing.
    * Prerequisite verification.
    * System-wide schedule conflicts to block double-booking.
* **Registrar Dashboard:** Uses a tabbed navigation structure to enter newly authorized courses or live-stream current student class enrollments via relational SQL joins.
* **Enrollment & Transcript Tracker:** Dynamically fetches full student profiles and computes real-time progression toward graduation requirements.



## 🛠️ Tech Stack & Dependencies

* **Language:** Java 17+
* **Framework:** Java Swing (Desktop GUI)
* **Database:** MySQL (Hosted via XAMPP)
* **Bridge Driver:** MySQL Connector/J JDBC Driver



## 🗃️ Database Architecture
The system operates on a relational database named `kisii_university` using three primary tables:

| Table | Primary Key | Description |
| :--- | :--- | :--- |
| **`students`** | `student_number` | Stores ID, name, and academic probation standings. |
| **`courses`** | `course_id` | Stores subject details and physical/virtual time slots. |
| **`enrollments`** | Composite Key | Acts as a bridge handling active relationships between students and registered courses. |



## ⚙️ Installation & Setup

Follow these steps to deploy and run the system locally on your machine:

### 1. Database Setup
1. Launch the **XAMPP Control Panel**.
2. Start the **MySQL** module.
3. Open your browser and navigate to `localhost/phpmyadmin`.
4. Create a new database named `kisii_university`.
5. Import or run the following base SQL query to generate the skeleton tables:

```sql
CREATE TABLE students (
    student_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50),
    academic_standing VARCHAR(20)
);

CREATE TABLE courses (
    course_id VARCHAR(20) PRIMARY KEY,
    description VARCHAR(100),
    schedule_slot VARCHAR(50)
);

CREATE TABLE enrollments (
    student_number VARCHAR(20),
    course_id VARCHAR(20),
    PRIMARY KEY (student_number, course_id),
    FOREIGN KEY (student_number) REFERENCES students(student_number),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
