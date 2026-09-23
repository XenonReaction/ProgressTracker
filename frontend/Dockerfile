# Build stage: compile the Angular app
FROM node:22-alpine AS build
WORKDIR /app
# Install dependencies first so they're cached until package*.json changes
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY . .
RUN npm run build

# Runtime stage: nginx serves the built files and forwards /api to the backend
FROM nginx:1.29-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/progression-tracker-frontend/browser /usr/share/nginx/html
EXPOSE 80
