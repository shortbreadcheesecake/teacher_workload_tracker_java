# Teacher Workload Tracker Java

API-First приложение для учёта выполненной нагрузки преподавателей с интеграцией расписания (Java, Spring Boot, H2 Database).

## Стек технологий
- Java 17+
- Spring Boot 3.3.0
- H2 Database (встроенная)
- Spring Data JPA
- Spring MVC
- Thymeleaf (для веб-интерфейса)
- springdoc-openapi (Swagger UI)
- JUnit 5 и Mockito (для тестирования)
- Apache Commons CSV (для импорта/экспорта данных)
- iCal4j (для работы с календарями)

## Быстрый старт
1. Клонируйте репозиторий
2. Соберите и запустите проект:
   ```bash
   mvn spring-boot:run
   ```
   Если не получилось, то соберите и запустите проект так:
   ```bash
   mvn clean package
   java -jar target\teacher-workload-tracker-0.1.0.jar
   ```
3. Откройте в браузере:
   - Веб-интерфейс: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Консоль H2: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:file:./data/teacher_workload`, User: `sa`, без пароля)

## Тестирование
Проект включает модульные и интеграционные тесты для API и сервисного слоя.

Для запуска тестов:
```bash
mvn test
```

## Основные возможности через API
- Управление преподавателями (создание, редактирование, удаление)
- Учёт нагрузки (планирование и отслеживание выполнения)
- Управление расписанием занятий
- Импорт/экспорт данных в CSV формате
- Генерация календарей в формате iCalendar

## Структура проекта
- `controller` — REST API и веб-контроллеры
- `service` — бизнес-логика
- `repository` — JPA-репозитории
- `model` — сущности JPA
- `dto` — DTO и мапперы для API
- `mapper` — конвертеры между моделями и DTO
- `util` — вспомогательные классы
- `test` — тесты API и сервисов
