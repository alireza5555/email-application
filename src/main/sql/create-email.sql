USE db;

CREATE TABLE email(
code NVARCHAR(30) PRIMARY KEY,
subject NVARCHAR(30),
date DATE ,
sender NVARCHAR(),
body NVARCHAR(MAX)
);

