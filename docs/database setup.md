# 데이터베이스(MySQL 8) 수동 생성 명령어
```
DROP USER triplog@'localhost';
DROP USER triplog@'%';
FLUSH PRIVILEGES;

CREATE DATABASE triplog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER triplog@'localhost' IDENTIFIED BY 'triplog123#';
CREATE USER triplog@'%' IDENTIFIED BY 'triplog123#';

GRANT ALL PRIVILEGES ON triplog.* TO 'triplog'@'localhost';
GRANT ALL PRIVILEGES ON triplog.* TO 'triplog'@'%';
```
