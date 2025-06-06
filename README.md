# Note

Note — это простое веб‑приложение для создания, редактирования и удаления личных заметок. Пользователи могут зарегистрироваться, войти в систему и управлять своими заметками через удобный интерфейс на базе Spring Boot и Thymeleaf.

## Оглавление

- [Описание проекта](#описание-проекта)  
- [Ключевые возможности](#ключевые-возможности)  
- [Технологический стек](#технологический-стек)  
- [Установка и запуск](#установка-и-запуск)  
  - [Требования](#требования)  
  - [Настройка окружения](#настройка-окружения)  
  - [Запуск через Docker Compose](#запуск-через-docker-compose)  
- [Пример использования](#пример-использования)  
- [Тестирование](#тестирование)  

## Описание проекта

Note предоставляет следующий функционал:

- Регистрация новых пользователей с автоматическим созданием приветственной заметки.  
- Аутентификация через Spring Security.  
- CRUD‑операции над заметками (создать, просмотреть, изменить, удалить).  
- Кэширование списка заметок в Redis для повышения производительности.  
- Глобальная обработка ошибок через `@ControllerAdvice`.  

## Ключевые возможности

- **Регистрация и авторизация**: отдельные страницы для входа и регистрации, защита маршрутов.  
- **Управление заметками**: вывод списка, создание новой, редактирование существующей, удаление.  
- **Персонализация**: каждая заметка привязана к конкретному пользователю.  
- **Кэширование Redis**: часто запрашиваемые данные кэшируются, снижая нагрузку на базу.  
- **Обработка ошибок**: дружелюбные страницы ошибок при недоступных ресурсах или внутренних сбоях.  

## Технологический стек

- **Язык и платформа**: Java 21, Maven  
- **Фреймворки и библиотеки**: Spring Boot, Spring MVC, Spring Data JPA, Spring Security, Spring Cache (Redis), Thymeleaf  
- **Базы данных**: PostgreSQL, Redis  
- **Контейнеризация**: Docker, Docker Compose  
- **Тестирование**: JUnit 5, Mockito  

## Установка и запуск

### Требования

- [Docker & Docker Compose](https://docs.docker.com/)

### Настройка окружения

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/AnastasiyaGubsky/note.git
   cd Note
   ```

### Запуск через Docker Compose

В этом проекте есть три сервиса: PostgreSQL, Redis и само приложение. Запустите их командой:

```bash
docker-compose up --build
```
- Контейнер **postgres** (PostgreSQL) будет доступен на порту **5432**.  
- Контейнер **redis** (Redis) — на порту **6379**.  
- Контейнер **app** — на порту **8083**.

## Пример использования

1. Перейдите в браузере по адресу `http://localhost:8083/register` и зарегистрируйтесь.  
2. После регистрации вы попадёте на страницу входа: `http://localhost:8083/login`.  
3. Войдите и увидите список своих заметок (появится первая приветственная заметка).  
4. Нажмите **Создать новую заметку**, введите текст и сохраните.  
5. Для редактирования или удаления заметки воспользуйтесь соответствующими ссылками рядом с ней.  
6. Для выхода из аккаунта нажмите кнопку **Выйти** в шапке.

## Тестирование

Для запуска всех юнит‑ и интеграционных тестов:

```bash
mvn test
```

- **NoteServiceTest**, **UserServiceTest** — проверяют логику сервисов (Mockito).  
- **NoteControllerTest**, **UserControllerTest** — тестируют контроллеры через Spring MVC Test.  
- **CustomUserDetailsServiceTest** — проверяет интеграцию с `UserDetailsService`.  
- **GlobalExceptionHandlerTest** — гарантирует корректную работу обработки ошибок.
