package com.example.axoninheritance

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.slf4j.LoggerFactory
import java.util.UUID

open class ParentAggregate() {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}
    @AggregateIdentifier
    lateinit var id: UUID
    var counter: Int = 0

    fun on(event: CreatedParentEvent) {
        log.info("on $event")
        apply {
            id = event.id
        }
    }

    @CommandHandler
    fun handle(command: UpdateSharedCountCommand) {
        log.info("handle $command")
        AggregateLifecycle.apply(SharedCounterUpdatedEvent(command.id, ++counter))
    }

    @EventSourcingHandler
    fun on(event: SharedCounterUpdatedEvent) {
        log.info("on $event")
        apply { counter = event.counter }
    }
}

interface CreatedParentEvent {
    val id: UUID
}
data class UpdateSharedCountCommand(@TargetAggregateIdentifier val id: UUID)
data class SharedCounterUpdatedEvent(var id: UUID, val counter: Int)
