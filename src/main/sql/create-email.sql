USE db;

CREATE TABLE email(
code NVARCHAR(6) PRIMARY KEY,
subject NVARCHAR(100),
date DATE ,
sender NVARCHAR(30),
body NVARCHAR(255)
);

