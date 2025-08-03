# 📱 Fall Detection - Backend

Backend for the **Fall Detection** application, designed to detect falls in elderly people and automatically notify a registered close contact.  
Built with **Spring Boot** and **PostgreSQL**

---

## 👥 Contributors

- [rachu033](https://github.com/rachu033) – Creator of the backend for the Fall Detection application.  
- [Fifikula](https://github.com/Fifikula) – Developed the frontend in collaboration with rachu033. The frontend repository link will be shared here soon.

---

## 🚀 Features

- Detects user falls (data analysis from the mobile app)
- Sends push notifications to a registered close contact
- Authentication and registration with:
  - **JWT** (authentication & authorization)
  - **Google Firebase Authentication** (Google account login)
  - Passing the token to the backend for second-level login
- Secure server-client communication with `.p12` keys
- Data persistence with **PostgreSQL**
- Clean architecture with:
  - **Model** (entity layer)
  - **DTO** (data transfer objects)
  - **Mapper** (model <-> DTO mapping)
  - **Service** layer (business logic)
  - **Controller** layer (REST API endpoints)
  - Use of **interfaces** for service abstraction and easier testing

---

## 🔐 Authentication

1. **JWT**
   - Token generation on login
   - Token validation for each request
   - Token refresh
2. **Google Firebase + Google Account**
   - Login in the mobile application
   - Sending the token to the backend
   - Token verification using Firebase Admin SDK
3. **`.p12` Keys**
   - Secure communication between server and client
   - Configured in Spring Boot Security

---

## 📡 Push Notifications

- Triggered by the backend when a fall is detected
- Integration with **Firebase Cloud Messaging (FCM)**
- Device-specific token management
- Notifications sent to the registered close contact

---

## 🛠 Development Environment

This project was developed using **IntelliJ IDEA**.

---

## ⚠️ Important Notice

The `firebaseAdminKey.json` file is **not included** in the repository for security reasons.  
You need to create and download your own Firebase service account key from your Firebase Console and place it in the `src/main/resources/` directory as `firebaseAdminKey.json`.
