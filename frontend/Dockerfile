FROM node:23.4.0-alpine3.20 AS frontend-build

# Устанавливаем git в контейнере
RUN apk add --no-cache git

# Клонируем репозиторий фронтенда
WORKDIR /usr/src/app/frontend
RUN git clone https://github.com/BaronGalileo/Act-books

# Переходим в директорию клонированного репозитория
WORKDIR /usr/src/app/frontend/Act-books

# Устанавливаем зависимости и собираем фронтенд
RUN npm install && npm run build
RUN npm start
CMD ["tail", "-f", "/dev/null"]