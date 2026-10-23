# Automated Freight Railway Marshalling Yard Simulator

A Java and SQL based simulation of a freight railway marshalling yard, developed as a semester project for Database Systems.

The project focuses on managing trains, wagons, tracks and yard operations through a Java application connected to a relational database using JDBC. It was mainly built to understand how database concepts work when they are used as part of an actual application rather than only through standalone SQL queries.

## Technologies

- Java
- JDBC
- SQL
- Maven
- MySQL
- Git

## What the Project Does

The simulator represents the basic operations involved in managing a freight marshalling yard.

The application keeps track of trains, their wagons, available tracks and the movement or allocation of resources within the yard. Operations performed through the Java application are reflected in the SQL database.

Some of the main operations include:

- Managing train information
- Managing wagon and freight information
- Assigning tracks to trains
- Updating train and wagon status
- Retrieving information using SQL queries
- Maintaining relationships between trains, wagons and tracks
- Handling multi-step yard operations through database transactions

## Database

The database was designed around the entities involved in the railway yard and their relationships.

The project makes use of concepts such as:

- Primary keys and foreign keys
- Referential integrity
- Relational table design
- Constraints
- SQL joins
- Insert, update, delete and select operations
- Normalization

The database schema and SQL setup are available in:

```text
freight_railway_db.sql
```

## Java and JDBC

The application uses JDBC to connect the Java program to the SQL database.

The general flow of the application is:

```text
Java Application
       |
       v
     JDBC
       |
       v
   SQL Database
       |
       v
    Results
```

Through JDBC, the application can execute SQL queries, retrieve results and update database records based on the operations being performed.

This was also where I worked with concepts such as database connections, prepared statements, result sets and exception handling.

## Transactions

One of the important parts of the project was understanding and implementing database transactions.

Some operations require multiple changes to the database. For example, an operation involving a train and its assigned track may require more than one table to be updated.

Instead of treating each query independently, these operations can be handled as a single transaction:

```text
Begin Transaction
       |
       +-- Update train
       |
       +-- Update track
       |
       +-- Update allocation
       |
       v
     Commit
```

If something goes wrong during the operation, the transaction can be rolled back so that the database does not remain in a partially updated state.

This helped me understand the practical use of concepts such as atomicity, consistency, commit and rollback.

## Project Structure

```text
Automated-Freight-Railway-Marshalling-Yard-Simulator/
│
├── src/
│   └── main/
│       └── java/
│
├── freight_railway_db.sql
├── pom.xml
├── .gitignore
└── README.md
```

## Running the Project

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/Automated-Freight-Railway-Marshalling-Yard-Simulator.git
```

Open the project in IntelliJ IDEA, Eclipse or another Java IDE.

Set up the SQL database using:

```text
freight_railway_db.sql
```

Configure the JDBC connection with your local database URL, username and password.

The project uses Maven for dependency management and can be built using:

```bash
mvn clean install
```

## What I Learned

This project was one of my first opportunities to work with a database as part of a complete application.

Some of the main things I worked with and learned were:

- Designing relational databases for a real-world problem
- Writing SQL queries beyond basic CRUD operations
- Connecting Java applications to databases using JDBC
- Working with prepared statements and result sets
- Managing database connections and exceptions
- Using transactions with commit and rollback
- Understanding how multiple related tables need to be updated together
- Maintaining data consistency through database constraints
- Connecting object-oriented Java code with relational database structures

## Team Project

This was developed as a collaborative semester project for Database Systems.

The original project repository is:

https://github.com/the-ishan-effect/Automated-Freight-Railway-Marshalling-Yard-Simulator

This repository contains my version of the project and represents my work as part of the team.

## Course

**Database Systems — Semester Project**

**Main areas:** Java, JDBC, SQL, Relational Database Design, Transactions
