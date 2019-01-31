package com.example.axoninheritance

import org.axonframework.eventhandling.DomainEventMessage
import org.axonframework.eventsourcing.EventSourcingRepository
import org.axonframework.eventsourcing.GenericAggregateFactory
import org.axonframework.eventsourcing.eventstore.EventStore
import org.axonframework.modelling.command.Repository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AxonSimpleApplication {
	@Bean
    fun repo(eventStore: EventStore): Repository<ParentAggregate> {
        return EventSourcingRepository.builder(ParentAggregate::class.java).
                aggregateFactory(MyAggregateFactory()).
                eventStore(eventStore).
                build<EventSourcingRepository<ParentAggregate>>()
    }
}

fun main(args: Array<String>) {
	runApplication<AxonSimpleApplication>(*args)
}

class MyAggregateFactory() : GenericAggregateFactory<ParentAggregate>(ParentAggregate::class.java) {
	override fun doCreateAggregate(aggregateIdentifier: String?, firstEvent: DomainEventMessage<*>?): ParentAggregate {
		firstEvent!!.let {
			return when (it.payloadType.name) {
                CreatedBEvent::class.java.name -> AggregateB()
                CreatedCEvent::class.java.name -> AggregateC()
				else -> throw RuntimeException("Payload type not found for first event")
			}
		}
	}
}