# TanksWiki

TanksWiki is web application built for managing data on military tanks and their associated modules. The application allows users to securely log in and perform CRUD (Create, Read, Update, Delete) operations on tanks and modules, with real-time updates powered by WebSockets. The frontend is built using **React**, while the backend is developed with **Spring Boot**. **Spring Security** and **JWT** (JSON Web Token) are used for secure authentication and authorization.

---

## Project Overview

### Main Features:
- **Secure User Authentication**: Uses **JWT tokens** and **Spring Security** to protect the application. Users are required to log in to access and modify data.
- **CRUD Operations**: The application allows users to create, view, update, and delete both tanks and modules dynamically. Data integrity and security are maintained through server-side validation.
- **Real-time Updates**: Changes made in the system (like adding or updating tanks/modules) are pushed to the frontend in real-time using **WebSockets**.
- **Frontend Guarding**: React’s route guards ensure that only authenticated users can access certain pages. Unauthorized users are automatically redirected to the login page.
- **Global State Management**: React's **Context API** is used to manage the global state of the application, ensuring consistent access to authentication data and other stateful information across the app.

---

## Technology Stack

### Frontend:
- **React**: Provides a dynamic, component-based user interface for the application.
- **React Router**: Handles navigation and page routing.
- **Context API**: Manages global application state (e.g., user authentication).
- **JWT (JSON Web Token)**: The token is stored in **local storage** and used for subsequent API requests.
- **WebSockets**: Allows the application to receive updates in real-time when new data is added or modified.

### Backend:
- **Spring Boot**: Powers the RESTful API that manages the tank and module data.
- **Spring Security**: Protects routes and handles JWT-based authentication and authorization.
- **JWT (JSON Web Token)**: Tokens are generated and validated on every API call to ensure user authentication.
- **WebSockets**: Provides real-time communication between the backend and frontend for updates.
- **JUnit and Mockito**: Used for unit testing and mocking dependencies in unit tests.
- **Maven**: Build automation tool for managing dependencies and packaging the application.
- **Database**: SQLServer DB stores tank and module data.

---

## Security

The project uses **Spring Security** to implement robust authentication and authorization. **JWT** tokens are generated upon successful login and are stored in the client’s local storage. These tokens are included in the `Authorization: Bearer <JWT_TOKEN>` header for subsequent requests to protected routes.

### Key Security Features:
- **Login Endpoint**: Handles user authentication and returns a JWT token.
- **Token Validation**: JWT tokens are validated on every API request to ensure the user’s identity.
- **Route Guards (Frontend)**: React guards ensure only authenticated users can access certain routes/pages.
- **Protected Routes (Backend)**: The backend API restricts access to specific endpoints based on the user’s authentication status.

---

## Real-time Functionality

TanksWiki uses **WebSockets** to push updates to the frontend as they happen. For instance, if a new tank or module is added, or an existing one is updated, the frontend will receive the updated data in real-time without the need for manual refreshing.

This is particularly useful in scenarios where multiple users are interacting with the system, ensuring that everyone has the most up-to-date information at any given time.

---

## Testing

The backend of the application is thoroughly tested to ensure robustness and reliability. **JUnit** and **Mockito** are used to write unit tests for service layers, particularly focusing on mocking repository interactions.

Here’s an example of a test for the `ModuleService` class:

```java
public class ModuleServiceTest {

    @Mock
    ModuleRepository moduleRepository;

    @InjectMocks
    ModuleService moduleService;

    @Test
    public void testAddModule() throws RepositoryException {
        Module newModule = new Module(1L, 60L, "Gun variation 1", "gun", 55, 30, 20, 155);

        when(moduleRepository.save(any(Module.class))).thenReturn(newModule);
        assert(moduleService.addModule(newModule) == 1L);
    }
}
