# Skill Share

The SkillShare application connects service providers with users, allowing them to discover, book, and review services while managing their profiles. It features a secure, scalable architecture with a RESTful backend, user-friendly interfaces, and real-time notifications for a seamless experience.

# Software architecture

The application follows a client-server architecture, with the backend server implemented using the Spring Boot framework and the mobile client built for the Android platform. The system facilitates secure and efficient communication between users and the server via REST APIs.

---
# Docker Image

---
# Frontend:
The mobile application features:
- User Authentication: Signup and login with JWT-based security.
- Service Discovery: Browse and filter services by city, category, or price.
- Reservations: Book, accept, or decline service requests.
- Feedback System: Write and view feedback for services.
- Profile Management: Update user information, manage services, and view categories.

# Technologies Used:
- Langage : Java.
- Framework : Android SDK.

# Backend:
The backend provides a RESTful API with:
Framework: Spring Boot
Authentication: Implemented using Spring Security with JWT.

# Structure;
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
