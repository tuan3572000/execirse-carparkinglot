# execirse-carparkinglot

An execirse for finding nearest available car park locations

**Coding approach**

There are 2 approaches

    - Query/Write in same database
        Pros 
            Easy to implement
        Cons
            Limitted in number of databases that support geospatial query
            Bottleneck in DB when high volume of query

    - CQRS pattern, Write in DB and Query in other place
        Pros
            Optimize for query
            Flexible in choosing database
        Cons
            More efforts to implement

Decision:

Follow the Clean Architecture guideline

Apply CQRS pattern
- Postgresql for Command
- Redis for Query

All data will be stored in Postgresql, whenever a task to get car parks availability is done, populate data to Redis for
querying

* Pros
    - High throughput
    - Redis could be leveraged to cache other data

* Cons
    - Resource for Redis server
    - Data in cache have to be populated periodically

**Local setup**

Java 17 , Maven 3.9.2

**How to run**

    1. Clean and package project (make sure carparking-0.0.1-SNAPSHOT.jar exists in target folder)
    2. Navigate to docker folder
    3. Run command 'docker-compose up --build'

**APIs**

* Find nearest available car parks    
  http://localhost:8080/carparks/nearest?latitude=1.3271974323385998&longitude=103.72106615550845&page=1&per_page=3
* Sync car parks availability info
  http://localhost:8080/carparks/availability-sync

**Testing**

Conducted manual test by myself

Does not have enough time to write unit test (spend too much time on design and clean code)

