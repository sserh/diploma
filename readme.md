### Дипломная работа. Автор: Сергей Трунов JD-56
Согласно заданию на дипломную работу разработан Backend-сервис, реализующую простейшую логику работы файлового "облака".

Реализовано:
1. Авторизация и аутентификация пользователей.
2. Предоставление клиентам токенов аутентификации формата JWT.
3. Загрузка файлов в "облако".
4. Удаление файлов из "облака".
5. Переименование файлов в "облаке".
6. Скачивание файлов из "облака".

_**Полезная информация:**_ БД для пользователей создаются посредством работы Hibernate, 
скрипты создания пользователей в БД доступны в файле **src/main/resources/data.sql**