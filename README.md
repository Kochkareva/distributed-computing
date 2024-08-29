
# Лабораторная работа 3. 

### Задание
**Цель**: изучение современных технологий контейнеризации.

**Задачи**:

- Установить средство контейнеризации docker.
- Изучить применение и принципы docker.
- Изучить утилиту docker-compose и структуру файла docker-compose.yml.
- Развернуть не менее 3х различных сервисов при помощи docker-compose.
- Оформить отчёт в формате Markdown и создать Pull Request в git-репозитории.

### Как запустить лабораторную работу
В директории с файлом характеристик docker-compose.yaml выполнить команду:
```
docker-compose -f docker-compose.yaml up
```
### Описание лабораторной работы
#### Создание базы данных
Каждый сервис реализует CRUD-операции, поэтому были выбраны следующие сущности, соответствующие теме диплома: упражнение и тренировка. Эти сущности связаны отношением один ко многим. Созданные таблицы базы данных:

```sql
-- Создание таблицы тренировок
CREATE TABLE t_training (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     description VARCHAR(255) NOT NULL
);
-- Создание таблицы упражнений
CREATE TABLE t_exercise (
                     id SERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     description VARCHAR(255) NOT NULL,
                     id_training INTEGER,
                     FOREIGN KEY (id_training) REFERENCES t_training(id)
);
```

Также в файле `docker-compose.yaml` создадим соответствующий сервис:
```dockerfile
#database
  postgresql:
    #configuration
    image: postgres:latest
    ports:
      - 5432:5432
    environment:
      POSTGRES_PASSWORD: admin
      POSTGRES_USER: admin
      POSTGRES_DB: traininarium
    volumes:
      - ./database.sql:/docker-entrypoint-initdb.d/database.sql
    restart: always
    networks:
      - mynetwork
```

- `image: postgres:latest` указывает, что мы хотим использовать последнюю версию образа `PostgreSQL`.
- `ports: - 5432:5432` пробрасывает порт 5432 контейнера (стандартный порт PostgreSQL) на порт 5432 хоста, чтобы можно было подключаться к базе данных с внешнего устройства.
- `environment` определяет переменные окружения, которые будут использоваться контейнером. В данном случае, устанавливаются значения для переменных `POSTGRES_PASSWORD`, `POSTGRES_USER` и `POSTGRES_DB`.
- `volumes` указывает путь к файлу `database.sql` в текущей директории, который будет использоваться для инициализации базы данных при запуске контейнера.
- `restart: always` гарантирует, что контейнер будет перезапущен автоматически, если он остановится или перезагрузится.
- `networks` определяет сеть, к которой будет присоединен контейнер. В данном случае, контейнер будет присоединен к сети с именем `mynetwork`.

В итоге будет создан контейнер с базой данных `PostgreSQL`, настроенной с указанными переменными окружения, проброшенным портом и файлом `database.sql` для инициализации базы данных.

#### Создание микросервиса *Упражнения*
После реализации необходимых элементов микросервиса, таких как: controller, model, modelDTO, repository и service, создадим конфигурационный файл Dockerfile, в котором пропишем следующее:
```dockerfile
FROM openjdk:17-jdk

WORKDIR /app
COPY ./exercise-app/build/libs/exercise-app-0.0.1-SNAPSHOT.jar /app/exercise-app-0.0.1-SNAPSHOT.jar
EXPOSE 8081

CMD ["java", "-jar", "exercise-app-0.0.1-SNAPSHOT.jar"]
```
В данном файле мы указываем базовый образ, который будет использован для создания контейнера (*OpenJDK* версии 17 с установленным *JDK*); устанавливаем рабочую директорию внутри контейнера, где будут размещены файлы приложения;  указываем, что приложение внутри контейнера будет слушать на порту 8081 и определяем команду, которая будет выполнена при запуске контейнера, а именно запуск приложения *Java* из файла `exercise-app-0.0.1-SNAPSHOT.jar` внутри контейнера.

Также в файле `docker-compose.yaml` создадим соответствующий сервис:
```dockerfile
 exercise-service:
    build:
      context: .
      dockerfile: ./exercise-app/Dockerfile
    ports:
      - 8081:8081
    environment:
      DATASOURCE_URL: jdbc:postgresql://postgresql:5432/traininarium
      DATASOURCE_USERNAME: admin
      DATASOURCE_PASSWORD: admin
    restart: always
    #wait build database
    depends_on:
      - postgresql
    networks:
      - mynetwork
```
- `exercise-service` является именем сервиса, который будет создан и запущен в контейнере.
- `context: .` указывает, что контекстом для сборки Docker-образа является текущая директория.
- `dockerfile: ./exercise-app/Dockerfile` указывает путь к `Dockerfile`, который будет использоваться для сборки образа.
- `ports` определяет проброс портов между хостом и контейнером.
`8081:8081` указывает, что порт 8081 на хосте будет проброшен на порт 8081 внутри контейнера.
- `environment` определяет переменные окружения, которые будут доступны внутри контейнера.
`DATASOURCE_URL: jdbc:postgresql://postgresql:5432/traininarium` определяет URL для подключения к базе данных `PostgreSQL`.
`DATASOURCE_USERNAME: admin` определяет имя пользователя для подключения к базе данных.
`DATASOURCE_PASSWORD: admin` определяет пароль для подключения к базе данных.
- `restart: always` указывает, что контейнер будет автоматически перезапущен в случае его остановки.
depends_on определяет зависимость данного сервиса от другого сервиса.
- `postgresql` указывает, что данный сервис зависит от сервиса с именем `postgresql`.
- `mynetwork` указывает, что контейнер будет присоединен к сети с именем `mynetwork`.

#### Создание микросервиса *Тренировки*
Аналогично реализуем controller, model, modelDTO, repository и service для микросервиса *Тренировки*, но дополняя кодом отправки запроса к сервису *Упражнений*. Пример запроса к сервису *Упражнений*:

```java
public List<TrainingDto> findAllTraining() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
        List<TrainingDto> trainingDtos = new ArrayList<>();
        for (Training training : trainingRepository.findAll()) {
            TrainingDto trainingDto = new TrainingDto();
            trainingDto.setId(training.getId());
            trainingDto.setName(training.getName());
            trainingDto.setDescription(training.getDescription());

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://" + exercise_service_host + "/exercise/training/" + trainingDto.getId(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                List<ExerciseDto> exerciseDtos;
                try {
                    exerciseDtos = objectMapper.readValue(responseBody, ArrayList.class);
                    trainingDto.setExercises(exerciseDtos);
                } catch (JsonProcessingException e) {
                    throw new Exception("Не удалось десериализовать тело запроса в объект");
                }
            } else {
                throw new Exception("Ошибка получения объекта");
            }
            trainingDtos.add(trainingDto);
        }
        return trainingDtos;
    }
```
Теперь создадим конфигурационный файл Dockerfile, в котором пропишем следующее:
```dockerfile
FROM openjdk:17-jdk

WORKDIR /app
COPY ./training-app/build/libs/training-app-0.0.1-SNAPSHOT.jar /app/training-app-0.0.1-SNAPSHOT.jar
EXPOSE 8082

CMD ["java", "-jar", "training-app-0.0.1-SNAPSHOT.jar"]
```
В данном файле мы указываем базовый образ, который будет использован для создания контейнера (*OpenJDK* версии 17 с установленным *JDK*); устанавливаем рабочую директорию внутри контейнера, где будут размещены файлы приложения;  указываем, что приложение внутри контейнера будет слушать на порту 8082; копируем файл `training-app-0.0.1-SNAPSHOT.jar` из локальной директории `./training-app/build/libs/` внутрь контейнера в папку `/app` и определяем команду, которая будет выполнена при запуске контейнера, а именно запуск приложения Java из файла `training-app-0.0.1-SNAPSHOT.jar` внутри контейнера.

Также в файле `docker-compose.yaml` создадим соответствующий сервис:
```dockerfile
training-service:
    build:
      context: .
      dockerfile: ./training-app/Dockerfile
    ports:
      - 8082:8082
    environment:
      EXERCISE_SERVICE_HOST: exercise-service:8081
      DATASOURCE_URL: jdbc:postgresql://postgresql:5432/traininarium
      DATASOURCE_USERNAME: admin
      DATASOURCE_PASSWORD: admin
    restart: always
    #wait build database
    depends_on:
      - postgresql
    networks:
      - mynetwork
```

- `training-service` является именем сервиса, который будет создан и запущен в контейнере.
- `context: .` указывает, что контекстом для сборки Docker-образа является текущая директория.
- `dockerfile: ./training-app/Dockerfile` указывает путь к `Dockerfile`, который будет использоваться для сборки образа.
- `8082:8082` указывает, что порт 8082 на хосте будет проброшен на порт 8082 внутри контейнера.
- `environment` определяет переменные окружения, которые будут доступны внутри контейнера.
`EXERCISE_SERVICE_HOST: exercise-service:8081` определяет хост и порт для подключения к сервису `exercise-service`.
`DATASOURCE_URL: jdbc:postgresql://postgresql:5432/traininarium` определяет URL для подключения к базе данных `PostgreSQL`.
`DATASOURCE_USERNAME: admin` определяет имя пользователя для подключения к базе данных.
`DATASOURCE_PASSWORD: admin` определяет пароль для подключения к базе данных.
- `restart: always` указывает, что контейнер будет автоматически перезапущен в случае его остановки.
- `depends_on` определяет зависимость данного сервиса от другого сервиса, данный сервис зависит от сервиса с именем `postgresql`.
- `mynetwork` указывает, что контейнер будет присоединен к сети с именем `mynetwork`.

#### Реализация gateway при помощи *nginx*

Для того, чтобы использовать *Nginx* для обработки HTTP-запросов и маршрутизации их к соответствующим сервисам, создадим файл конфигурации *Nginx*:

```
events {
    worker_connections  1024;
}

http {

    upstream exercise-service {
        server exercise-service:8081;
    }
    upstream training-service {
        server training-service:8082;
    }

    server {
        
        listen 80;
        listen      [::]:80;
        server_name localhost;
        
        location /exercise-service/ {
            proxy_pass http://exercise-service/;
        }
        
        location /training-service/ {
            proxy_pass http://training-service/;
        }
    }
}
```
- `events` определяет настройки событий для сервера *Nginx*.
  - `worker_connections 1024` указывает максимальное количество одновременных соединений, которые могут быть обработаны сервером.
- `http` определяет настройки HTTP-сервера *Nginx*.
  - `upstream` определяет группу серверов, которые могут обрабатывать запросы.
`exercise-service` определяет группу серверов с именем `exercise-service`, в которой находится только один сервер `exercise-service:8081`.
`training-service` определяет группу серверов с именем `training-service`, в которой находится только один сервер `training-service:8082`.
  - `server` определяет настройки для конкретного виртуального сервера.
    - `listen 80` указывает на порт, на котором сервер будет слушать входящие HTTP-запросы.
    - `listen [::]:80` указывает на IPv6-адрес и порт, на котором сервер будет слушать входящие HTTP-запросы.
    - `server_name localhost` указывает имя сервера.
    - `location /exercise-service/` определяет местоположение для обработки запросов, которые начинаются с `/exercise-service/`.
`proxy_pass http://exercise-service/` указывает, что все запросы, начинающиеся с `/exercise-service/`, должны быть перенаправлены на группу серверов `exercise-service`.
    - `location /training-service/` определяет местоположение для обработки запросов, которые начинаются с `/training-service/`.
`proxy_pass http://training-service/` указывает, что все запросы, начинающиеся с `/training-service/`, должны быть перенаправлены на группу серверов `training-service`.


Таким образом, при запуске сервера *Nginx* с использованием этого конфигурационного файла, сервер будет слушать входящие HTTP-запросы на порту 80 и маршрутизировать запросы, начинающиеся с `/exercise-service/`, на группу серверов `exercise-service`, а запросы, начинающиеся с `/training-service/`, на группу серверов `training-service`.

Далее в файле `docker-compose.yaml` создадим соответствующий сервис:

```dockerfile
nginx:
    #configuration
    image: nginx:latest
    ports:
      - 80:80
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    restart: always
    depends_on:
      - training-service
      - exercise-service
    networks:
      - mynetwork
```

- `nginx` - это название сервиса, который будет запущен в контейнере.
- `image: nginx:latest` указывает на использование последней версии образа *Nginx* из *Docker Hub*.
- `80:80` пробрасывает порт 80 контейнера на порт 80 хостовой машины.
- `volumes` определяет привязку тома между контейнером и хостовой машиной.
  - `./nginx.conf:/etc/nginx/nginx.conf` привязывает файл `nginx.conf` из текущего рабочего каталога хостовой машины к файлу `nginx.conf` внутри контейнера *Nginx*. Это позволяет настроить *Nginx* с помощью внешнего файла конфигурации.
- `restart: always` указывает, что контейнер должен быть автоматически перезапущен при его остановке или падении.
- `depends_on` указывает на зависимость этого сервиса от других сервисов.
  - `training-service` указывает, что контейнер Nginx должен быть запущен после контейнера `training-service`.
  - `exercise-service` указывает, что контейнер Nginx должен быть запущен после контейнера `exercise-service`.
- `networks` определяет сети, к которым будет присоединен контейнер.
  - `mynetwork` добавляет контейнер в сеть с именем `mynetwork`.

  ### Видео

https://disk.yandex.ru/i/OYPw_Tzl0QrzIw