This project demonstrates an issue I've been having using inheritance within the Axon Framework.

# Problem
The problem is that CommandHandler processing replays the wrong Event Sourced Aggregate leading to 
`org.axonframework.eventsourcing.IncompatibleAggregateException: Aggregate identifier must be non-null after applying an event. Make sure the aggregate identifier is initialized at the latest when handling the creation event.`

It seems like I'm missing some config (E.g. an AggregateFactory implementation)

# Solution
I've solved it (in the master branch) by making the subclass Aggregates part of the same AggregateRepository.
You can still replicate the problem in the problem branch.

# To reproduce:
* start an Axon Server
* Build the project `mvn install`
* run the project `java -jar target/axon-inheritance-0.0.1-SNAPSHOT.jar`
* Post a command to `http://localhost:8080/createB` with body `{ "id": "fea98d54-57c1-4810-8def-4194eb040d7e" }`
