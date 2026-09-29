# SupplyChain Processor Task

For this task we are asked to implement a tree processor.

Main problems I have discovered and tried to solve:
1. Returning a deeply nested JSON response with a Jackson mapper. If for server we can disable the default nest limit of 500, 
then for client - we can't be sure that their deserialize mapper would support such deep structures.
Thus, there can be a solution involving a flat response structure. But the client would need to reconstruct the tree itself.

2. The OutOfMemory problem. Well, basically I've implemented streaming endpoint which fetches chunks of data via pg cursor.

3. Tree constraints. One of the main constraints in this task is that we need to implement the Tree structures instead of Graph.
Tree is basically an asyclic graph, thus we need to maintain a validation that there are no cycles during the edge insert. 

4. Edge insert concurrency problems. I have added a transaction table-lock for create Edge endpoint.
In this way we remove the problem of concurrent ancestor checks where there is a possibility that we will add edges concurrently, cycle check will pass for each request, because not all data is commited
and then we would have a cyclic graph in database. To remove this problem I've added a pessimistic lock. In this case I think it's okay, becuase the inserts in edge table
are simple and fast.

## Project setup
Two ways of launching the code: via Docker compose; locally. The steps require you to have either locally deployed postgres. Or installed docker/docker compose.

## Build jar locally & Launch in Docker
> ./gradlew bootJar
> docker compose up --build

## Run & Build locally
### First launch Postgres database container in docker
> docker run -p 5432:5432 \
-e POSTGRES_PASSWORD=postgres \
-e POSTGRES_USER=postgres \
-e POSTGRES_DB=postgres \
postgres:17

### If you have a local pgdb setup - change credentials in resources/application.properties

### Compile the jar via gradle
> ./gradlew bootJar

### Launch jar (Important! Should be Java version 25)
> java -jar build/libs/app.jar

## Test the app
I've included simple Postman requests to test the endpoints in file Prewave.postman_collection.json in the root of the project.
Api for edges is located at /api/v1/edge.
Api for tree is located at /api/v1/tree.
Default port for backend service is 8080
