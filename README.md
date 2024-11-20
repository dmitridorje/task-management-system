<h2>Разработка системы управления задачами</h2>

<h3>Использованные технологии:</h3>
Spring Boot,<br>
Java 17,<br>
PostgreSQL,<br>
Docker,<br>
Liquibase,<br>
Mapstruct

<h4>Инструкция по локальному запуску:</h4> 
1) Клонируйте репозиторий <br>
   Выполните следующую команду в терминале:<br>
   git clone https://github.com/dmitridorje/task-management-system.git
   2) Запустите докер-контейнер с PostgreSQL<br>
      Существует два способа запуска: вручную или с помощью скриптов.

   а) Запуск через скрипт:<br>
   В корне проекта есть скрипт run.sh. Просто выполните в консоли:<br>
   ./run.sh<br>
   б) Ручной запуск:<br>
   Если вы хотите запустить проект вручную:

   docker-compose build<br>
   docker-compose up -d

   PostgreSQL будет доступен на localhost:5432.
3) Выполните сборку приложения:<br>
   ./gradlew build
4) Запустите приложение:<br>
   java -jar build/libs/TaskManagementSystem.jar

При первом запуске в  базу данных будут добавлены два пользователя:<br>
username: admin@example.com<br>
password: admin<br>

username: user@example.com<br>
password: password<br>

5) Далее можно перейти либо в Swagger-UI по адресу http://localhost:8080/swagger-ui/index.html, либо работать с приложением через Postman.
В любом случае сначала необходимо сгенерировать токен:<br>
   POST /api/v1/auth/login<br>
В теле запроса нужно как раз передать данные одного из пользователей, созданного при запуске:<br>
   {
      "username": "yourUsername",
      "password": "yourPassword"
   }<br>
В ответе вы получите токен, который затем можно использовать для доступа к защищённым точкам.
В Postman необходимо его добавлять через Authorization Bearer Token, в Swagger-UI настроена авторизация через поле Authorize.

