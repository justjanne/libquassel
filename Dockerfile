FROM openjdk:8 as builder
RUN mkdir /build && \
    mkdir /build/.gradle
COPY . /build/src
WORKDIR /build/src/
ENV GRADLE_USER_HOME=/build/.gradle
RUN /build/src/gradlew dokkaHtmlMultiModule

FROM nginx:stable-alpine
COPY --from=builder /build/src/build/dokka/htmlMultiModule/ /usr/share/nginx/html/javadoc
EXPOSE 80
