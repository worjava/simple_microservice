FROM adoptopenjdk/openjdk11:jdk-11.0.8_10-alpine

# Копируем собранный JAR-файл Java-приложения в контейнер
ADD target/*.jar app.jar

# Задаем переменные окружения
ENV EUREKA_HOST=localhost
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n"

# Открываем порты для приложения и удаленной отладки
EXPOSE 8885 8787

# Определяем команду entrypoint для запуска Java-приложения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Duser.country=RU -Duser.language=ru -Dspring.profiles.active=cloud -jar /app.jar --cloud.eureka-host=$EUREKA_HOST"]
