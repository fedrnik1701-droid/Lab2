# Лабораторная работа — Вариант 15 «Бюро находок»

## Требования
- Java 17+
- IntelliJ IDEA
- Maven (обычно IntelliJ импортирует его автоматически)

## Запуск
1. Распакуйте архив.
2. В IntelliJ IDEA выберите **Open** и укажите папку `lost-found-lab`.
3. Дождитесь импорта Maven.
4. Запустите `LostFoundApplication.java`.
5. API будет доступно по адресу `http://localhost:8080`.

Данные хранятся в памяти процесса: при перезапуске приложения они очищаются. Это упрощённая версия для лабораторной, без внешней БД.

## Эндпоинты
Для каждого ресурса доступны:
- `GET /api/{resource}` — список, `200`
- `GET /api/{resource}/{id}` — один объект, `200`
- `POST /api/{resource}` — создать, `201`
- `PATCH /api/{resource}/{id}` — частично обновить, `200`
- `DELETE /api/{resource}/{id}` — удалить, `204`

Ресурсы: `users`, `found-items`, `loss-reports`, `item-claims`, `handover-records`.

## Примеры запросов (Postman / IntelliJ HTTP Client)

Создать пользователя:
```http
POST http://localhost:8080/api/users
Content-Type: application/json

{"name":"Иван Иванов","email":"ivan@example.com"}
```

Создать найденный предмет:
```http
POST http://localhost:8080/api/found-items
Content-Type: application/json

{"description":"Чёрный кошелёк","category":"личные вещи","foundLocation":"Библиотека","status":"FOUND"}
```

Создать заявление (нужен существующий userId):
```http
POST http://localhost:8080/api/loss-reports
Content-Type: application/json

{"userId":1,"description":"Потерян кошелёк","category":"личные вещи","status":"OPEN"}
```

Создать претензию (нужны существующие lossReportId и foundItemId):
```http
POST http://localhost:8080/api/item-claims
Content-Type: application/json

{"lossReportId":1,"foundItemId":1,"evidence":"Внутри была карта","status":"PENDING"}
```

Создать акт передачи (нужен существующий itemClaimId):
```http
POST http://localhost:8080/api/handover-records
Content-Type: application/json

{"itemClaimId":1,"confirmed":true}
```

`handoverDate` назначается сервером автоматически и не принимается из тела запроса.

## Архитектура
- `controller` — HTTP-маршруты, коды ответов и преобразование входного JSON.
- `service` — бизнес-правила, проверка связей и каскадное удаление зависимых объектов.
- `repository` — хранение коллекций в памяти.
- `model` — бизнес-сущности.

Удаление пользователя удаляет его заявления, связанные претензии и акты передачи. Удаление найденного предмета или заявления удаляет связанные претензии и акты; удаление претензии удаляет связанные акты. Поэтому не остаются недействительные ссылки.
