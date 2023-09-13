# execirse-carparkinglot

An execirse for finding nearest available car park locations

**APIs**

* Find nearest available car parks    
  http://localhost:8080/carparks/nearest?latitude=1.3271974323385998&longitude=103.72106615550845&page=1&per_page=3
* Sync car parks availability info
  http://localhost:8080/carparks/availability-sync

**Coding strategy**

Apply the Clean Architecture guideline

Apply CQRS pattern

- Postgresql for Command
- Redis for Query

All data will be stored in Postgresql, whenever a task to get car parks availability is done, populate data to Redis for
querying

* Pros
    - High throughput (query in cache)
    - Freely to use any databases (Not limited to DBs which can support geospatial query)

* Cons
    - Resource for Redis server
    - Data in cache have to be populated periodically

**Local setup**

Java 17 , Maven 3.9.2

**Testing**

Conducted manual test by myself

Does not have enough time to write unit test (spend too much time on design and clean code)

