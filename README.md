# Skill Share

The SkillShare application connects service providers with users, allowing them to discover, book, and review services while managing their profiles. It features a secure, scalable architecture with a RESTful backend, user-friendly interfaces, and real-time notifications for a seamless experience.

## Table of Contents

1. [Introduction](#introduction)  
2. [Software Architecture](#software-architecture)  
3. [Docker Image](#docker-image)  
4. [Frontend Features](#frontend-features)  
   - [Technologies Used](#1technologies-used)  
5. [Backend Features](#backend-features)  
   - [Technologies Used](#1structure)  
   - [Code Structure](#code-structure)  
6. [Getting Started](#getting-started)  
   - [Prerequisites](#prerequisites)  
   - [Backend Setup](#backend-setup)  
   - [Frontend Setup](#frontend-setup)  
   - [Testing the Application](#testing-the-application)  
7. [Video Demonstration](#video-demonstration)  
8. [Contributors](#contributors)

# Software architecture

![architecture](https://github.com/user-attachments/assets/e4a1d9ee-a1ea-4835-b020-f85ac6dd0219)

The application follows a client-server architecture, with the backend server implemented using the Spring Boot framework and the mobile client built for the Android platform. The system facilitates secure and efficient communication between users and the server via REST APIs.

---
# Docker Image
```bash
services:
  mysql_database:
    image: mysql:8.0
    container_name: mysql_container
    environment:
      MYSQL_DATABASE: skillShare_db
      MYSQL_ROOT_PASSWORD: ""
      MYSQL_ALLOW_EMPTY_PASSWORD: "yes"
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - app-network

  backend:
    build:
      context: ./SkillShare
      dockerfile: Dockerfile
    container_name: backend_container
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql_database:3306/skillShare_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: ""
      MYSQL_ALLOW_EMPTY_PASSWORD: "yes"
    ports:
      - "3601:3600"
    depends_on:
      - mysql_database
    entrypoint: ["sh", "-c", "while ! nc -z mysql_database 3306; do sleep 2; done; java -jar app.jar"]
    networks:
    - app-network

volumes:
  mysql_data:

networks:
  app-network:
    driver: bridge
   ```
---
# Frontend:
The mobile application features:
- User Authentication: Signup and login with JWT-based security.
- Service Discovery: Browse and filter services by city, category, or price.
- Reservations: Book, accept, or decline service requests.
- Feedback System: Write and view feedback for services.
- Profile Management: Update user information, manage services, and view categories.

### 1.Technologies Used:
- Langage : Java.
- Framework : Android SDK.

# Backend:
The backend provides a RESTful API with:
- Framework: Spring Boot
- Authentication: Implemented using Spring Security with JWT.

### 1.Structure:
The backend code follows a modular and organized structure, leveraging the power of Spring Boot for building a robust and scalable application.
- com.example.application
Main Application Class: Application.java serves as the entry point for the Spring Boot application. It includes the main method to start the application.
- com.example.controller
- Controller Classes: The controller package contains classes responsible for handling incoming HTTP requests. Each controller class is dedicated to a specific feature or entity, exposing RESTful endpoints. These classes interact with the services to process requests and return appropriate responses.
- com.example.model
Entity Classes: The model package includes classes representing data entities in the application. These classes are annotated with JPA annotations, defining the structure of the database tables. Each entity typically corresponds to a table in the MySQL database.
- com.example.repository
Repository Interfaces: The repository package contains interfaces that extend Spring Data JPA repositories. These interfaces provide methods for basic CRUD operations and are used by services to interact with the database.

---
# Getting Started

Follow these steps to set up and run the SkillShare project locally:

**Prerequisites:**

- **Docker and Docker Compose:** Ensure you have Docker and Docker Compose installed on your machine.
  - [Docker Installation](https://www.docker.com/get-started)
  - [Docker Compose Installation](https://docs.docker.com/compose/install/)
  
- **JDK 17 or higher:** The backend requires JDK 17 or later.

- **Android Studio:** For frontend mobile development, make sure you have Android Studio installed.
  - [Android Studio Installation](https://developer.android.com/studio)

---

### **Backend Setup:**

1. **Clone the Project:**
   - Open a terminal and run:
   ```bash
   git clone <repository_url>
   cd <project_folder>
   ```

2. **Docker Image:**
   - Ensure Docker is running.
   - Build the Docker image for the backend:
   ```bash
   docker-compose build
   ```

3. **Run the Backend:**
   - Start the backend with Docker Compose:
   ```bash
   docker-compose up
   ```
   - The Spring Boot backend will automatically start, and the database will be created.

4. **Verify Backend:**
   - Ensure the backend is running.

---

### **Frontend Setup:**

1. **Install Android Studio:**
   - Open Android Studio and import the project into your workspace.

2. **Set up Android SDK:**
   - Ensure the required Android SDK version is installed.

3. **Install Frontend Dependencies:**
   - Open a terminal in the frontend project directory and run:
   ```bash
   ./gradlew build
   ```

4. **Run the Frontend:**
   - In Android Studio, run the project on an emulator or physical device.

5. **Verify Frontend:**
   - The mobile app should be running, allowing you to interact with the backend.

---

### **Testing the Application:**
- Once both the backend and frontend are running, you can:
  - Sign up or log in through the mobile app.
  - Explore the services, make reservations, and leave feedback.
  - Manage profiles and services.

Now your SkillShare full-stack application is up and running locally. If you encounter any issues, check the console logs for error messages, ensure all dependencies are installed, and verify that Docker and the necessary services are running.

## Video Demonstration
https://github.com/user-attachments/assets/5034e09e-93fb-4fb1-bc5d-d1f5c7685b85

---

## Contributors
- DRIHAM Siham [Github](https://github.com/SihamDriham)
- ZENNOURI Nassima [Github](https://github.com/NassimaZENNOURI)
- LACHGAR Mohammed [ResearchGate](https://www.researchgate.net/profile/Mohamed-Lachgar)

