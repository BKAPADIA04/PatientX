# PatientX 
**A Microservices-based Patient Management System**

---

## 📌 Overview  
PatientX is a **cloud-native microservices application** designed to handle **patient management, billing, analytics, and authentication** in a scalable and modular way.  
It leverages **gRPC** for efficient inter-service communication and **Apache Kafka** for reliable asynchronous event streaming.  

This project demonstrates modern **distributed system design** with focus on:  
- Loose coupling between services  
- Scalability through event-driven architecture  
- Resilient infrastructure setup with Docker and LocalStack  

---

## ⚙️ Tech Stack  
- **Languages/Frameworks**: Java / Spring Boot  
- **API Gateway**: Central routing for microservices  
- **Auth**: JWT-based authentication  
- **gRPC**: Synchronous communication between services  
- **Kafka**: Asynchronous event streaming  
- **Docker & Docker Compose**: Containerized deployment  
- **LocalStack**: Local AWS cloud service emulation  
- **Integration Tests**: Service interaction validation  

---

## 🏗️ Microservices  
- **Auth Service** → Handles JWT authentication & user identity  
- **Patient Service** → Manages patient records and details  
- **Billing Service** → Processes billing requests via gRPC  
- **Analytics Service** → Listens to Kafka topics for insights and reporting  
- **API Gateway** → Routes external requests to internal services  
- **Infrastructure** → Docker, LocalStack, and deployment configs  
- **Integration Tests** → End-to-end test suite  

---

## 🔌 Communication Flow  
- **gRPC** → Used between `patient-service` and `billing-service` for synchronous billing operations  
- **Kafka** → Used for **event-driven communication**, especially between `patient-service` and `analytics-service`  

---

## 🚀 Getting Started  

### Prerequisites  
- Docker & Docker Compose  
- Java 17+  
- Maven/Gradle  
