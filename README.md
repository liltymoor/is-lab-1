# Информационная система управления лабораторными работами

## Описание

Информационная система для управления объектами класса LabWork, разработанная с использованием Java EE, JPA/Hibernate, JSF и PostgreSQL. Система предоставляет полный функционал для создания, просмотра, редактирования и удаления лабораторных работ с поддержкой real-time обновлений.

## Технологии

- **Java 17+**
- **Jakarta EE 11**
- **JPA/Hibernate 7.0**
- **JSF 4.0**
- **PostgreSQL**
- **Maven**
- **WebSocket для real-time обновлений**

## Структура проекта

```
src/
├── main/
│   ├── java/
│   │   └── com/crudoshlep/islab1/
│   │       ├── model/          # Модели данных (LabWork, Person, Discipline, etc.)
│   │       ├── dao/            # DAO слой для работы с БД
│   │       ├── service/        # Бизнес-логика
│   │       ├── rest/           # REST API контроллеры
│   │       ├── bean/           # JSF Managed Beans
│   │       └── websocket/      # WebSocket для real-time обновлений
│   ├── resources/
│   │   └── META-INF/
│   │       └── persistence.xml # Конфигурация JPA
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── web.xml        # Конфигурация веб-приложения
│       │   └── faces-config.xml # Конфигурация JSF
│       ├── resources/
│       │   ├── css/           # Стили
│       │   └── js/            # JavaScript
│       └── *.xhtml            # JSF страницы
└── test/
```

## Модели данных

### LabWork (Основная модель)
- `id` - уникальный идентификатор (автогенерация)
- `name` - название (обязательное)
- `coordinates` - координаты (обязательные)
- `creationDate` - дата создания (автогенерация)
- `description` - описание (опциональное)
- `difficulty` - сложность (опциональное)
- `discipline` - дисциплина (обязательная)
- `minimalPoint` - минимальные баллы (обязательное, > 0)
- `personalQualitiesMaximum` - макс. баллы за личные качества (обязательное, > 0)
- `author` - автор (опциональный)

### Вспомогательные модели
- **Coordinates** - координаты (x > -579, y ≤ 101)
- **Discipline** - дисциплина (название, часы лекций/практики/самостоятельной работы)
- **Person** - автор (имя, цвет глаз/волос, местоположение, дата рождения, национальность)
- **Location** - местоположение (x, y, z координаты, название)

### Перечисления
- **Difficulty** - HARD, IMPOSSIBLE, INSANE, HOPELESS, TERRIBLE
- **Color** - GREEN, YELLOW, ORANGE
- **Country** - RUSSIA, VATICAN, ITALY, THAILAND, JAPAN

## Функциональность

### Основные операции
- ✅ Создание новой лабораторной работы
- ✅ Просмотр информации об объекте по ID
- ✅ Обновление объекта (модификация атрибутов)
- ✅ Удаление объекта
- ✅ Связывание с объектами вспомогательных классов

### Веб-интерфейс
- ✅ Главный экран с таблицей объектов
- ✅ Пагинация для больших объемов данных
- ✅ Фильтрация и сортировка по строковым колонкам
- ✅ Отдельные окна для операций CRUD
- ✅ Валидация пользовательского ввода
- ✅ Информативные сообщения об ошибках

### Специальные операции
- ✅ Удаление всех объектов с определенным minimalPoint
- ✅ Подсчет объектов с определенным personalQualitiesMaximum
- ✅ Подсчет объектов с автором меньше заданного
- ✅ Повышение/понижение сложности на указанное число шагов

### Real-time обновления
- ✅ WebSocket для автоматических обновлений
- ✅ Уведомления о создании/обновлении/удалении
- ✅ Синхронизация между пользователями

## Настройка и развертывание

### 1. Требования
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Application Server (WildFly, GlassFish, или TomEE)

### 2. Настройка базы данных
```sql
-- Создание базы данных
CREATE DATABASE studs;

-- Создание пользователя (если необходимо)
CREATE USER studs WITH PASSWORD 'studs';
GRANT ALL PRIVILEGES ON DATABASE studs TO studs;
```

### 3. Конфигурация подключения
Файл `src/main/resources/META-INF/persistence.xml` содержит настройки подключения:
```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://pg:5432/studs"/>
<property name="jakarta.persistence.jdbc.user" value="studs"/>
<property name="jakarta.persistence.jdbc.password" value="studs"/>
```

### 4. Сборка проекта
```bash
# Клонирование репозитория
git clone <repository-url>
cd is-lab1

# Сборка проекта
mvn clean package

# Запуск тестов
mvn test
```

### 5. Развертывание
```bash
# Развертывание на WildFly
cp target/is-lab1-1.0-SNAPSHOT.war $WILDFLY_HOME/standalone/deployments/

# Или развертывание на GlassFish
asadmin deploy target/is-lab1-1.0-SNAPSHOT.war
```

## Использование

### Веб-интерфейс
1. Откройте браузер и перейдите по адресу: `http://localhost:8080/is-lab1`
2. Используйте меню для навигации между разделами
3. Создавайте, редактируйте и удаляйте лабораторные работы
4. Используйте фильтры для поиска нужных записей
5. Выполняйте специальные операции в соответствующем разделе

### REST API
Система предоставляет REST API для программного доступа:

#### Лабораторные работы
- `GET /api/labworks` - получить список с пагинацией и фильтрацией
- `GET /api/labworks/{id}` - получить по ID
- `POST /api/labworks` - создать новую
- `PUT /api/labworks/{id}` - обновить
- `DELETE /api/labworks/{id}` - удалить

#### Специальные операции
- `DELETE /api/labworks/by-minimal-point/{minimalPoint}` - удалить по минимальным баллам
- `GET /api/labworks/count/by-personal-qualities/{value}` - подсчитать по личным качествам
- `GET /api/labworks/count/by-author-less-than/{authorId}` - подсчитать по автору
- `PUT /api/labworks/{id}/increase-difficulty?steps={steps}` - повысить сложность
- `PUT /api/labworks/{id}/decrease-difficulty?steps={steps}` - понизить сложность

### WebSocket
Для real-time обновлений подключитесь к WebSocket endpoint:
```
ws://localhost:8080/is-lab1/labwork-updates
```

## Архитектура

### Слои приложения
1. **Presentation Layer** - JSF страницы и Managed Beans
2. **Business Layer** - Service классы с бизнес-логикой
3. **Data Access Layer** - DAO классы для работы с БД
4. **Model Layer** - JPA сущности

### Принципы
- **Separation of Concerns** - четкое разделение ответственности
- **Dependency Injection** - использование CDI
- **Validation** - валидация на уровне модели и представления
- **Error Handling** - централизованная обработка ошибок

## Особенности реализации

### Валидация
- Все ограничения полей реализованы через JPA аннотации
- Дополнительная валидация через Bean Validation
- Клиентская валидация через JSF

### Производительность
- Пагинация для больших объемов данных
- Lazy loading для связанных объектов
- Индексы в базе данных

### Безопасность
- Валидация входных данных
- Обработка ошибок без раскрытия внутренней информации
- Защита от SQL-инъекций через JPA

## Разработка

### Добавление новых функций
1. Создайте модель в пакете `model`
2. Добавьте DAO в пакет `dao`
3. Создайте сервис в пакете `service`
4. Добавьте REST контроллер в пакет `rest`
5. Создайте JSF страницы в `webapp`

### Тестирование
```bash
# Запуск всех тестов
mvn test

# Запуск с покрытием
mvn test jacoco:report
```

## Устранение неполадок

### Проблемы с подключением к БД
1. Проверьте настройки в `persistence.xml`
2. Убедитесь, что PostgreSQL запущен
3. Проверьте права доступа пользователя

### Проблемы с WebSocket
1. Убедитесь, что сервер приложений поддерживает WebSocket
2. Проверьте настройки прокси/файрвола
3. Откройте консоль браузера для отладки

### Проблемы с JSF
1. Проверьте настройки в `faces-config.xml`
2. Убедитесь, что все Managed Beans правильно аннотированы
3. Проверьте логи сервера приложений

## Лицензия

Этот проект создан в учебных целях.

## Контакты

Для вопросов и предложений обращайтесь к разработчику.
