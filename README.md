# JPA Domain Modeling

A focused deep-dive into **Spring Data JPA** and **Hibernate** for mastering entity design, relationships, and persistence layer concepts in a school/academic domain.

## Project Goal

- Explore **domain modeling** with JPA entities
- Understand **relationships**: OneToOne, OneToMany, ManyToMany
- Practice **cascading**, **fetch strategies**, and **entity lifecycle**
- Write clean **equals/hashCode**, constructors, and proper entity design
- Master **JPQL** and custom repository queries

## What’s Inside

- **Entities**: Student, Teacher, Guardian, Course, CourseDetail  
- **Relationships**:  
  - OneToOne (e.g., Student ↔ Guardian)  
  - OneToMany (e.g., Teacher → Courses)  
  - ManyToMany (e.g., Student ↔ Courses)  
- **Repositories**: Spring Data JPA repositories with CRUD and custom queries  
- **Services & Controllers**: Minimal — focus is on persistence layer  
- **Tests**: Basic unit tests for repository operations  
- **DB**: MariaDB database for experimentation

## Tech Stack

- Java 17+  
- Spring Boot 3+  
- Spring Data JPA  
- Hibernate ORM  
- MariaDB Database  
- JUnit 5  

*(No Lombok used in entities — full explicit constructors, getters/setters, equals/hashCode)*

## Learning Outcomes

By exploring this project, you will:

- Gain **hands-on experience** with JPA entity design  
- Understand **relationship mapping nuances**  
- Learn **how to write robust persistence layers**  
- Practice **clean domain modeling for backend systems**

## How to Run

1. Clone the repo  
   ```bash
   git clone https://github.com/devcavin/jpa-domain-modeling.git
