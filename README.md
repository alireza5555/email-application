# Milou Email Service  

Milou is a lightweight and simple email service built with **Java, Hibernate, and JavaFX**.  
Users can sign up, log in, send, view, reply, and forward emails.  
The service provides an intuitive user interface and a clean backend architecture powered by Hibernate for database management.  

---

## Features  

- **Sign Up & Login**: Create an account with a unique email and password, or log in to an existing one.  
- **Send Emails**: Compose and send emails with recipients, subject, and body content.  
- **View Emails**: Browse unread, all, or sent emails. Emails are sorted by the date received.  
- **Reply to Emails**: Reply with automatic `[Re]` appended to the subject.  
- **Forward Emails**: Forward emails with `[Fw]` automatically prefixed to the subject.  
- **Email Management**: Simple commands to manage unread, all, and sent emails.  
- **Error Handling**: Validation checks for password length, duplicate emails, and input handling.  

---

## Tech Stack  

- **Backend:** Java, Hibernate ORM  
- **Database:** MySQL (configured via Hibernate)  
- **Frontend:** JavaFX  

---

## Installation  

1. Set up a MySQL database and update the credentials in `hibernate.cfg.xml`:  

    ```xml
    <hibernate-configuration>
        <session-factory>
            <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
            <property name="hibernate.connection.url">YOUR_DB_URL</property>
            <property name="hibernate.connection.username">YOUR_USERNAME</property>
            <property name="hibernate.connection.password">YOUR_PASSWORD</property>
        </session-factory>
    </hibernate-configuration>
    ```

2. Build the project using your IDE or Maven/Gradle.  
3. Run the application.  

---

## Usage  

After launching the program, you will be prompted to **sign up** or **log in**.  

- **Sign Up** → Enter your name, email, and password.  
- **Login** → Enter your registered email and password to access your account.  

### Commands  

- `S` → Send an email  
- `V` → View emails  
- `R` → Reply to an email  
- `F` → Forward an email  

---

## Configuration  

The `hibernate.cfg.xml` file maps the following entities to the MySQL database:  

- **User**  
- **Email**  
- **Recipients**  

---

## Database  

<img width="1944" height="738" alt="image" src="https://github.com/user-attachments/assets/db4644c5-b875-4314-9a92-aa569fbb2598" />

