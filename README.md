# CineVerse 🎬

![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=black)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-%23404d59.svg?logo=spring-boot&logoColor=%2361DAFB)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?logo=hibernate&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?logo=elasticsearch&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-3448C5?logo=cloudinary&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?logo=postgresql&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?logo=postman&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black)

A modern, feature-rich movie and TV series tracking application built with Spring Boot. CineVerse allows users to
discover, track, and review their favorite movies and TV shows with a beautiful, responsive interface.

🌐 **[Frontend Repository](https://github.com/wael-gamil/cineverse)** | 🚀 **[Live Demo](https://cineverse-xi.vercel.app/)**

---

## 📚 Table of Contents

- [🌟 Features](#-features)
    - [🎬 Content Discovery](#-content-discovery)
    - [👤 User Management](#-user-management)
    - [📋 Watchlist Features](#-watchlist-features)
    - [⭐ Review System](#-review-system)
    - [🎭 Cast & Crew](#-cast--crew)
    - [🎥 Infrastructure](#-infrastructure)
- [📖 API Documentation](#-api-documentation)
    - [🚀 Postman](#-postman)
    - [📚 Swagger](#-swagger)
- [🗄️ Database Schema](#️-database-schema)
- [⚙️ Installation & Getting Started](#️-installation--getting-started)
    - [⚡ Prerequisites](#-prerequisites)
    - [🐳 Docker Setup](#-docker-setup)
    - [🚀 Quick Start with Docker](#-quick-start-with-docker)
    - [🐳 Docker Commands for Reference](#-docker-commands-for-reference)
- [📄 License](#-license)
- [👥 Authors](#-authors)

---

## 🌟 Features

### 🎬 Content Discovery

- 🗃️ **PostgreSQL Integration**: Efficient storage and retrieval of movie and TV series data
- 🔍 **Elasticsearch**: Advanced search functionality with fuzzy matching and relevance scoring
- 🎯 **Smart Filtering**: Complex query processing for multi-criteria content filtering
- 📊 **Content Details**: RESTful endpoints for comprehensive content information
- 📺 **Seasons & Episodes**: Hierarchical data structure for TV series management

### 👤 User Management

- 🔒 **JWT Authentication**: Secure token-based authentication system
- 🔑 **OAuth2 Integration**: Google authentication implementation
- 👤 **Profile System**: User profile management with Cloudinary image hosting
- 🔄 **Password Recovery**: Secure password reset flow with email integration
- ✉️ **Email Service**: Verification system using Spring Email

### 📋 Watchlist Features

- 📝 **Watchlist API**: CRUD operations for personal content lists
- 🔄 **Status Tracking**: Watch status management system
- 🔍 **Query Optimization**: Efficient database queries for list management

### ⭐ Review System

- ✍️ **Review Management**: Full CRUD functionality for user reviews
- 🏆 **Rating System**: Sophisticated rating calculation algorithms
- 👍 **Interaction Handling**: Like/dislike functionality with real-time updates
- 🛡️ **Content Moderation**: Spoiler protection implementation
- 📊 **Analytics**: Review metrics and user engagement tracking

### 🎭 Cast & Crew

- 👥 **Crew Member Entity**: Comprehensive data model for cast and crew
- 📊 **Relationship Mapping**: Complex entity relationships handling
- 🔍 **Search Integration**: Advanced crew member search functionality
- 🔗 **External APIs**: Integration with TMDb for crew member data

### 🎥 Infrastructure

- 🎥 **YouTube API**: Trailer integration service
- 🖼️ **Image Processing**: Dynamic image URL generation
- 🐳 **Docker Support**: Containerization for easy deployment

---

## 📖 API Documentation

### 🚀 Postman

Easily test and interact with the API documentation using Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://documenter.getpostman.com/view/18543155/2sB3BBqBuh)

---

### 📚 Swagger

[Swagger](https://swagger.io/) UI is available at `/swagger-ui/index.html` to explore the API endpoints and their
details.

---

## 🗄️ Database Schema

![Schema](https://github.com/user-attachments/assets/52f296ee-c45d-42a1-871c-5a6d7e1e9522)

---

## ⚙️ Installation & Getting Started

### ⚡ Prerequisites

- ✅ Docker & Docker Compose installed.

### 🐳 Docker Setup


The easiest way to run the application is with **Docker Compose**, which sets up the API server and the PostgreSQL
database automatically.

---

### 🚀 Quick Start with Docker

1. **Clone the repository**

   ```bash
   git clone https://github.com/MahmoudAbdulfattah1/CineVerse.git
   cd cineverse
   ```

2. **Create environment file**

   ```bash
   cp env.properties.example env.properties
   ```

3. **Build and start the application**

   ```bash
   docker-compose up --build
   ```

## 🐳 Docker Commands for Reference

```bash
# Start the application in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop the application
docker-compose down

# Rebuild and start
docker-compose up --build

# Remove containers and volumes
docker-compose down -v
```
## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors
- **[Mahmoud Abdelfattah](https://www.linkedin.com/in/mahmoud-a-fattah)** - Backend Developer
- **[Wael Gamil](https://www.linkedin.com/in/wael-gamil/)** - Frontend Developer