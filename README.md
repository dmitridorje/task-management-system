# Разработка системы управления задачами

## Использованные технологии
- Spring Boot
- Java 17
- PostgreSQL
- Docker
- Liquibase
- MapStruct

## Инструкция по локальному запуску

### 1. Клонирование репозитория
Клонируйте репозиторий с помощью команды:
```bash
git clone https://github.com/dmitridorje/task-management-system.git
```

### 2. Запуск Docker-контейнера с PostgreSQL

#### Способ 1: Запуск через скрипт
В корне проекта выполните следующий скрипт:
```bash
./run.sh
```

#### Способ 2: Ручной запуск
Если вы хотите настроить контейнер вручную:
```bash
docker-compose build
docker-compose up -d
```
PostgreSQL будет доступен на `localhost:5432`.

### 3. Сборка приложения
Выполните команду:
```bash
./gradlew build
```

### 4. Запуск приложения
Запустите собранный JAR-файл:
```bash
java -jar build/libs/TaskManagementSystem.jar
```

### 5. Доступ к приложению
При первом запуске в базу данных добавятся два пользователя:

- **Admin**:
  - Логин: `admin@example.com`
  - Пароль: `admin`
- **User**:
  - Логин: `user@example.com`
  - Пароль: `password`

Приложение доступно по следующим адресам:

- [Swagger-UI](http://localhost:8080/swagger-ui/index.html)
- Работа через Postman или любой другой инструмент.

### 6. Авторизация
Для работы с защищёнными точками API необходимо сгенерировать токен:

#### Шаг 1: Авторизация
Выполните запрос:
```http
POST /api/v1/auth/login
```
Тело запроса:
```json
{
  "username": "yourUsername",
  "password": "yourPassword"
}
```

#### Шаг 2: Использование токена
В ответе вы получите токен, который можно использовать для доступа к защищённым точкам API.
- **Postman**: добавьте токен в раздел "Authorization" с типом "Bearer Token".
- **Swagger-UI**: нажмите на кнопку "Authorize" и введите токен в соответствующее поле.



