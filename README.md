Milou Email Service
Milou is a lightweight and simple email service built with Java, Hibernate, and JavaFX. Users can sign up, log in, send, view, reply, and forward emails. The service is designed with a simple and intuitive user interface and backend architecture using Hibernate for database management.

Features
Sign Up & Login: Users can create an account with a unique email and password, or log into an existing account.

Send Emails: Users can send emails with recipients, subject, and body content.

View Emails: Users can view their unread, all, or sent emails. Emails are sorted by the date received.

Reply to Emails: Users can reply to emails, with automatic handling of the subject by appending "[Re]".

Forward Emails: Users can forward emails to other recipients with an automatic "[Fw]" prefix in the subject.

Email Management: Users can view unread emails, all emails, and sent emails by using simple commands.

Error Handling: Includes validation checks such as password length and duplicate emails.

Tech Stack
Backend: Java, Hibernate ORM

Database: MySQL (configured via Hibernate)

Frontend: JavaFX for graphical user interface

Installation

Set up the MySQL database and update the database credentials in the hibernate.cfg.xml file:

xml
Copy
Edit
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">YOUR_DB_URL</property>
        <property name="hibernate.connection.username">YOUR_USERNAME</property>
        <property name="hibernate.connection.password">YOUR_PASSWORD</property>
    </session-factory>
</hibernate-configuration>
Build the project using your IDE or Maven/Gradle.

Run the application.

Usage
After running the program, you will be prompted to sign up or log in. Based on your input, you can perform the following actions:

Sign Up: Enter your name, email, and password.

Login: Enter your registered email and password to access your account.

Once logged in, you can:

Send Email with the command S.

View Emails with the command V.

Reply to an Email using the R command.

Forward an Email using the F command.

Configuration
The hibernate.cfg.xml file is configured to connect to a MySQL database and map the following entities:

User

Email

Recipients

Code

<img width="1944" height="738" alt="image" src="https://github.com/user-attachments/assets/917ce239-f716-47ed-a9df-bb44c58ca8cc" />

Make sure to update the database connection details as per your setup.


