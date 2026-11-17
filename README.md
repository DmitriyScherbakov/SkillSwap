<div align="center">

# 🎓 SkillSwap

**A modern, production-ready skill-sharing platform built on a microservices architecture.**

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2025.1-blue?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-cloud)
[![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-4.0-black?style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-316192?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)](https://www.docker.com/)

Users can offer their skills (guitar, Linux, English) and request other people's skills. Instead of money, time is exchanged through the platform.

</div>

---

## 📖 Table of Contents
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Quick Start](#-quick-start)
- [API Endpoints](#-api-endpoints)
- [Test Users](#-test-users)
- [End-to-End Test](#-end-to-end-test)
- [Project Structure](#-project-structure)
- [Implemented Features](#-implemented-features)
- [Future Improvements](#-future-improvements)

---

## 🏗 Architecture

The system is split into independent microservices communicating via **REST (API Gateway)** and **Events (Kafka)**. 

```
       Client
         │
         ▼
  [ API Gateway ] :8080
         │
         ▼
  [ Eureka Server ] :8761 (Service Discovery)
         │
         ├─────────────────────────┐
         ▼                         ▼
 [ skill-service ] :8081     [ notification-service ] :8082
         │                         ▲
         ▼                         │
  [ PostgreSQL ] :5432       [ Apache Kafka ] :9092

```
