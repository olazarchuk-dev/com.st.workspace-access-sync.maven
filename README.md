#Spring Boot MongoDB Migrations using Mongock

* `Tutorial-1`: https://omaryaya.com/2022/02/04/1-mongock-steps-database-changelog-mongodb-nosql.html#what-we-will-do
* `Tutorial-2`: https://programmingtechie.com/2021/01/16/spring-boot-mongodb-migrations-using-mongock
* `Official Mongock Docs`: https://docs.mongock.io
* `Repo`: https://github.com/SaiUpadhyayula/spring-boot-mongodb-tutorial

- SDK: `Java-15`
- Framework: `Spring Boot`

+ Database: `MongoDB`
  - Host: `localhost`
  - Port: `27017`
  - Collection: `expense-tracker`
  - Document: `stage`

* [Import Documents into MongoDB from an Array of JSON Documents](https://database.guide/import-documents-into-mongodb-from-an-array-of-json-documents)
    ```shell
    > mongoimport --db=expense-tracker --collection=stage --jsonArray --file=workspaces_stage.json
    ```

```js
db.createCollection("stage")
```
```js
db.migration_users.find({
    updatedAt: {
        $lt: ISODate("2022-05-14T00:21:57.343Z")
    }
})
```

1. Just open terminal and type this command
   `sudo chmod 666 /var/run/docker.sock`
2. Make sure your Docker engine is up and running
   `docker ps`
3. Run all tests

Source code for the Spring Data Mongodb Tutorial 
* Youtube Tutorial - https://www.youtube.com/watch?v=orVwJTzsk3w
* Written Tutorial - https://programmingtechie.com/2021/01/06/spring-data-mongodb-tutorial/

Если вам часто приходится запускать сценарии в MongoDB для синхронизации данных, эта статья может быть для вас.
Я расскажу как настроить и настроить Mongock для запуска с вашим SpringBoot приложением.


---

# использовали инструмент Mongobee для автоматических миграций данных с mongoDB и Spring Boot

+ `Tutorial`: https://habr.com/ru/post/451798
  - Mongobee работал прекрасно до тех пор, пока мы не столкнулись с ситуацией, когда мы хотели добавить новое поле с уникальным индексом.
+ `Repo`: https://github.com/KuliginStepan/mongration

* `Jackson Unrecognized field, not marked as ignorable`: https://tedblob.com/jackson-unrecognizedpropertyexception-unrecognized-field-not-marked-as-ignorable-2
