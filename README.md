-----

# Hostel Management System (HMS-UOP)

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Renderer-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-Styling-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

## 📌 Project Overview

The **Hostel Management System (HMS-UOP)** is a comprehensive web-based platform developed for the **University of Peradeniya**. It is designed to digitize and streamline the complex administration of the university's residential facilities, which serve over 12,000 students across 19 halls of residence.

Currently, the university relies on manual workflows for room allocation, fee collection, and maintenance reporting, leading to data fragmentation and inefficiencies. HMS-UOP addresses these challenges by providing a centralized, role-based system that automates **student allocation (based on distance policy)**, **payment verification**, **disciplinary record keeping**, and **grievance handling**.

This project was developed by **Group L** as part of the **CSC3213** coursework at the Department of Statistics and Computer Science.

-----

## 🚀 Key Features by Module

The system is divided into functional modules tailored to specific university stakeholders:

### 🎓 1. Student Portal

  * **Dashboard:** A personalized view showing current residence status and profile details.
  * **Digital History:** View complete records of past **Residence**, **Payments**, and **Damages/Fines**.
  * **Service Desk:** A digital ticketing system to submit maintenance complaints directly to Sub-Wardens or the Accommodation Division.
  * **Payment Tracking:** View payment history and status of hostel fees.

### 🏢 2. Accommodation Division (Director & Management Assistant)

  * **Automated Allocation:** An intelligent algorithm that calculates student eligibility based on the **Distance Policy** (e.g., \>50km for 1st years, \>40km for 2nd years) using Google Maps API logic.
  * **User Management:** Add/Remove system users and manage roles.
  * **Data Ingestion:** Import student batches via Excel (SRB Data Integration).
  * **Notifications:** Broadcast announcements to students and wardens.

### 🏠 3. Sub-Warden Portal

  * **Room Management:** Handle daily room allocation and deallocation.
  * **Payment Verification:** Verify physical/online payment slips submitted by students.
  * **Discipline & Damages:** Record student behavioral issues or property damage fines directly into the system.

### 💰 4. Bursar Dashboard

  * **Financial Oversight:** Instant search capability by **Student Registration Number** (e.g., `S/20/390`).
  * **Fee Reconciliation:** View real-time payment status to grant clearance for exams or future hostel applications.

### 🏛️ 5. Executive Dashboard (Dean / VC / DVC)

  * **Faculty Views:** Deans can view hostel data specific to students in their faculty (Science, Engineering, Arts, etc.).
  * **Analytics:** High-level statistical views of hostel occupancy and student distribution.

-----

## 🛠️ Technical Architecture

The project follows a **Monolithic Architecture** using the **Model-View-Controller (MVC)** design pattern to ensure robustness and ease of deployment on the university intranet.

### Technology Stack

| Layer | Technology | Description |
| :--- | :--- | :--- |
| **Backend** | **Spring Boot** | Handles core business logic, REST APIs, and Security. |
| **Frontend** | **Thymeleaf** | Server-side rendering for dynamic HTML content. |
| **Styling** | **Tailwind CSS** | Utility-first CSS framework for a responsive, modern UI. |
| **Interactivity**| **Alpine.js** | Lightweight JavaScript for handling UI state (tabs, modals). |
| **Database** | **MySQL** | Relational database management. |
| **Security** | **Spring Security** | RBAC (Role-Based Access Control) and CSRF protection. |
| **Build Tool** | **Maven** | Dependency management. |

### Security & Routing

  * **RBAC:** Strict access control ensures students cannot access administrative data.
  * **Domain-Based Routing:** The system automatically routes users to their specific dashboard based on their email domain (e.g., `@sci.pdn.ac.lk` -\> Science Dean, `@eng.pdn.ac.lk` -\> Engineering Dean).


-----

## ⚡ Installation & Setup Guide

### Prerequisites

  * Java 17 or higher
  * Maven 3.6+
  * MySQL Server

### Steps to Run

1.  **Clone the Repository**

    ```bash
    git clone https://github.com/UHDNMJayalath/HMS-UOP.git
    cd HMS-UOP
    ```

2.  **Configure Database**
    Create a MySQL database named `hms_db`. Open `src/main/resources/application.properties` and update your credentials:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/hms_db
    spring.datasource.username=your_root_user
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Build the Project**

    ```bash
    mvn clean install
    ```

4.  **Run the Application**

    ```bash
    mvn spring-boot:run
    ```

5.  **Access the System**
    Open your browser and navigate to: `http://localhost:8080`

-----

## 👥 Development Team (Group L)

This project was collaboratively developed using the **Agile/Scrum** methodology.

  * **Thilini Athukorala** - *Full Stack Developer*
  * **Nishaka Mahesh** - *Full Stack Developer*
  * **Nimna Anjana** - *Full Stack Developer*
  * **Damindu Sandaruwan** - *Full Stack Developer*

-----

## 📄 License

This project is open-source and available under the(LICENSE). Developed for the Department of Statistics and Computer Science, University of Peradeniya.
