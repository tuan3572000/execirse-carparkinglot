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

All data will be stored in Postgresql, whenever a task to get car parks availability is done, populate data to Redis
cache for query

* Pros
    - High throughput (query in cache)
    - Not limited only to DBs which can support geospatial query

* Cons
    - Resource for Redis server
    - Data in cache have to be populated periodically

**Local setup**

* Java 17
* Maven 3.9.2

