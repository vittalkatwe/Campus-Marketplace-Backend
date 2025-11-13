# Campus Marketplace App – Backend (Spring Boot)

The **Campus Marketplace App** is a platform exclusively for students of a specific university campus.  
It allows students to **buy/sell items**, **chat with other users**, and participate in **community threads**.

---

## Features

### Authentication
- JWT-based secure login and signup.
- Every user has the same role (student).
- Only campus-verified users can join.

### Marketplace
Students can:
- View all products.
- List products for sale.
- Add descriptions, images, price, and condition.
- Search and filter easily.

### Real-time Chat
- Students can directly message buyers/sellers.
- All chat and messages stored securely in the backend.

### Community Threads
Predefined communities such as:
- Studies  
- Campus Events  
- Lost & Found  
- Announcements
- Confession (Anonymous)
- Help (Anonymous)


### Campus-Verified Users
- Only students of the same campus are allowed.
- Verification via Email (Spring Mail).

### Database Structure
PostgreSQL entities:
- Users  
- Products  
- Chats
- Messages  
- Communities  
- Items  

---

## Tech Stack

| Layer | Technology |
|------|------------|
| Backend | Spring Boot (Web, JPA, Security, Mail) |
| Database | PostgreSQL |
| Authentication | JWT |
| Build Tool | Maven |
| Frontend (Integration) | React Native |

---

# Configuration (`application.properties`)

Before running the backend, update the following fields.

---

## JWT Configuration
```properties
spring.application.name=CampusMarketplace

jwt.secret=your-secret-key-here
jwt.expiration=172800000

spring.datasource.url=jdbc:postgresql://localhost:5432/socialmedia
spring.datasource.username=postgres
spring.datasource.password=YOUR-PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

- Replace your-secret-key-here with any secure 64-character hex value.
- Replace the url with your postgres url
- Replace the postgres the username and password
- Replace with your email and email app password


## Steps to run
```bash
git clone https://github.com/vittalkatwe/Campus-Marketplace-Backend.git
cd CampusMarketplace
mvn clean install
mvn spring-boot:run
```

## Demo Video
https://drive.google.com/file/d/1ckBlF9rwtwAIqBsh_rMxb9Xu8ZlAK_ZH/view?usp=drive_link

