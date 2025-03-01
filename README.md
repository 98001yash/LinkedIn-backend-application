LinkedIn Application Backend
This project is the backend for a real-world LinkedIn clone application built using microservices architecture. It provides core functionalities like user registration, profile management, messaging, and job listings. The backend uses Neo4j as the database for efficiently managing relationships between users, connections, and job applications. The project is designed to be scalable and highly maintainable.

Features
User Registration & Authentication: Allows users to sign up, log in, and authenticate using JWT (JSON Web Tokens).
Profile Management: Users can create, update, and manage their professional profiles, including skills, education, and experience.
Connections: Users can send, accept, and manage connection requests to build their professional network.
Messaging: Users can send and receive messages to/from their connections.
Job Listings: View, apply, and manage job listings.
Neo4j Database: Utilizes Neo4j to handle relationships between users, connections, and job applications efficiently.
Microservices Architecture: Different functionalities (authentication, profile management, messaging, etc.) are separated into individual microservices communicating through REST APIs.
Kafka Messaging: Uses Kafka for event-driven communication between microservices.
PostgreSQL: For storing non-relational data related to job postings and user data.
Tech Stack
Backend: Spring Boot (Java)
Authentication: JWT (JSON Web Tokens)
Database: Neo4j (for handling relationships), PostgreSQL
Messaging: Kafka (for communication between microservices)
Microservices Architecture: Spring Boot services communicating over REST APIs
Containerization: Docker (for building and running microservices)



Clone the project
git clone https://github.com/98001yash/linkdInApplication.git
cd linkdInApplication
