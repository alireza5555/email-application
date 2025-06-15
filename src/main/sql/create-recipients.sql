USE db;

CREATE TABLE recipients(
id INT AUTO_INCREMENT PRIMARY KEY ,
code NVARCHAR(6) ,
email NVARCHAR(30),
status NVARCHAR(10),
   FOREIGN KEY(code) REFERENCES email(code),
   FOREIGN KEY(email) REFERENCES user(email)

)