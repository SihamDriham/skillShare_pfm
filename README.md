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

### 1.Technologies Used:
- Langage : Java.
- Framework : Android SDK.

# Backend:
The backend provides a RESTful API with:
Framework: Spring Boot
Authentication: Implemented using Spring Security with JWT.

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
  - [JDK Installation](https://adoptopenjdk.net/)

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
   - Ensure the backend is running by visiting [http://localhost:8080](http://localhost:8080) in your browser.

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



