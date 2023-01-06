# Домашнее задание №5
## Создать приложение хранящее информацию о книгах в библиотеке

**Цель:** использовать возможности Spring JDBC и spring-boot-starter-jdbc для подключения к реляционным базам данных.

**Описание задания:**
Использовать Spring JDBC и реляционную базу (можно H2).
Предусмотреть таблицы авторов, книг и жанров.
Предполагаются отношения многие-к-одному (у книги один автор, но у автора может быть несколько книг, то же касается
книг и жанров).
Интерфейс выполняется на Spring Shell (CRUD книги обязателен, операции с авторами и жанрами - как будет удобно).
Скрипт создания таблиц и скрипт заполнения данными должны автоматически запускаться с помощью spring-boot-starter-jdbc.
Покрыть тестами, насколько это возможно.

**Результат:** приложение с хранением данных в реляционной БД, которое в дальнейшем будем развивать