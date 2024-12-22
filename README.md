
# Certificate Web App

## Technologies

### Programming Languages
- Core Java 17

### Frameworks
- Spring Boot
- Spring Security
- Spring Batch
- Spring Framework
- Spring Retry
- Spring AOP

### Design Patterns
- Strategy
- Factory
- Observer
- Adapter
- Null Handler

### Databases
- Firebase Database
- PostgreSQL

### Messaging Queues
- Kafka
- RabbitMQ

### Frontend Technologies
- React.js
- Redux

### Scripting
- Google Apps Script
- Google Calendar

### Code Quality
- SonarQube

### Rules Engine
- Drools

### Compression
- Gzip

### Logging and Monitoring
- ELK (Elasticsearch, Logstash, Kibana)
- Log4J

### Containerization
- Docker

### API Documentation
- Swagger

### Libraries
- iTextPDF
- Apache POI

### Video Communication
- OpenVidu

### Reporting
- Jasper Reports

### Caching
- Redis

### Web Servers
- Jetty

---

## Kafka Setup

### Step 1: Download and Unzip Kafka
1. Download Apache Kafka from the [official website](https://kafka.apache.org/downloads).
2. Unzip Kafka to your desired directory (e.g., `C:\kafka`).

### Step 2: Start Zookeeper
1. Open Command Prompt as Administrator.
2. Navigate to the Kafka directory:
   ```bash
   cd C:\kafka\bin\windows
   ```
3. Start Zookeeper:
   ```bash
   .\zookeeper-server-start.bat C:\kafka\config\zookeeper.properties
   ```

### Step 3: Start Kafka Server
1. Open a new Administrator Command Prompt window.
2. Navigate to the Kafka directory:
   ```bash
   cd C:\kafka\bin\windows
   ```
3. Start the Kafka server:
   ```bash
   .\kafka-server-start.bat C:\kafka\config\server.properties
   ```

### Step 4: Create a Kafka Topic
1. Open the Command Prompt and run:
   ```bash
   kafka-topics.bat --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic logs
   ```

---

## Redis Setup

### Step 1: Download and Install Redis
1. Download Redis for Windows from [Microsoft's GitHub repository](https://github.com/microsoftarchive/redis/releases).
2. Unzip the folder to your desired directory (e.g., `C:\Redis`).

### Step 2: Start Redis Server
1. Open Command Prompt and navigate to the Redis directory:
   ```bash
   cd C:\Redis
   ```
2. Start Redis:
   ```bash
   redis-server.exe
   ```

---

## RabbitMQ Setup

### Step 1: Install RabbitMQ
1. Download RabbitMQ from the [official website](https://www.rabbitmq.com/download.html).
2. Install RabbitMQ along with Erlang (dependency).

### Step 2: Start RabbitMQ Server
1. Open Command Prompt and navigate to the RabbitMQ directory:
   ```bash
   cd C:\Program Files\RabbitMQ Server\rabbitmq_server-<version>\sbin
   ```
2. Start RabbitMQ:
   ```bash
   rabbitmq-server.bat
   ```
3. Enable the RabbitMQ management plugin:
   ```bash
   rabbitmq-plugins enable rabbitmq_management
   ```
   Access the management UI at [http://localhost:15672](http://localhost:15672) with default credentials:
   - Username: `guest`
   - Password: `guest`

---

## Firebase Push Notification Setup

### Step 1: Create a Firebase Account
1. Visit the [Firebase Console](https://console.firebase.google.com/).
2. Sign in with your Google account.

### Step 2: Create a Firebase Project
1. Click "Add Project".
2. Follow the steps to create a project (e.g., `PushNotificationsApp`).

### Step 3: Integrate Firebase into Your App
1. Add Firebase SDK configuration to your web app's JavaScript code:
   ```javascript
   import { initializeApp } from "firebase/app";
   import { getMessaging } from "firebase/messaging";

   const firebaseConfig = {
       apiKey: "your-api-key",
       authDomain: "your-project-id.firebaseapp.com",
       projectId: "your-project-id",
       storageBucket: "your-project-id.appspot.com",
       messagingSenderId: "your-sender-id",
       appId: "your-app-id"
   };

   const app = initializeApp(firebaseConfig);
   const messaging = getMessaging(app);
   ```

---

## SonarQube Setup

### Step 1: Install SonarQube Locally
1. Download SonarQube Community Edition from [here](https://www.sonarsource.com/).
2. Ensure Java 11 or higher is installed.
3. Extract the archive and run the appropriate script:
   - Windows: `StartSonar.bat`
   - Linux/Mac: `./sonar.sh start`
4. Access SonarQube at [http://localhost:9000](http://localhost:9000).

### Step 2: Configure Maven
Add the following to your `pom.xml`:
```xml
<properties>
   <sonar.login>YOUR_SONAR_TOKEN</sonar.login>
   <java.version>17</java.version>
</properties>
<plugins>
   <plugin>
       <groupId>org.sonarsource.scanner.maven</groupId>
       <artifactId>sonar-maven-plugin</artifactId>
       <version>3.7.0.1746</version>
   </plugin>
</plugins>
```

### Step 3: Run Sonar Analysis
1. In your project directory, run:
   ```bash
   mvn clean install sonar:sonar
   ```

---

## ELK Stack Setup

### Step 1: Install ELK Components
1. Download Elasticsearch, Logstash, and Kibana from [Elastic](https://www.elastic.co/downloads/).
2. Extract each archive to a separate directory.

### Step 2: Start ELK Components
1. Start Elasticsearch:
   ```bash
   ./elasticsearch
   ```
2. Start Kibana:
   ```bash
   ./kibana
   ```
3. Configure and start Logstash with your configuration file:
   ```bash
   ./bin/logstash -f logstash.conf
   ```

---

## Frontend App Setup

1. Navigate to the app directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the app:
   ```bash
   npm start
   ```

---

## Google Apps Script Setup

### Step 1: Create a New Google Apps Script Project
1. Open [Google Apps Script](https://script.google.com/).
2. Click "New Project".
3. Deploy the script and store the URL in your database.

---

This README provides detailed setup instructions for the Certificate Web App and its associated technologies.
