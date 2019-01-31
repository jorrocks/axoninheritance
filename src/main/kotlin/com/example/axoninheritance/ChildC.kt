package com.example.axoninheritance

import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.axonframework.modelling.saga.EndSaga
import org.axonframework.modelling.saga.SagaEventHandler
import org.axonframework.modelling.saga.StartSaga
import org.axonframework.spring.stereotype.Aggregate
import org.axonframework.spring.stereotype.Saga
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

@Aggregate(repository = "repo")
class AggregateC(): ParentAggregate() {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}

    @CommandHandler
    constructor(command: CreateCCommand): this() {
        log.info("cntr $command")
        AggregateLifecycle.apply(CreatedCEvent(command.id))
    }

    @EventSourcingHandler
    fun on(event: CreatedCEvent) {
        log.info("on $event")
        apply { id = event.id }
    }
}

data class CreateCCommand(@TargetAggregateIdentifier var id: UUID)
data class CreatedCEvent(override var id: UUID): CreatedParentEvent
data class UpdateCCommand(@TargetAggregateIdentifier var id: UUID)
data class UpdatedCEvent(override var id: UUID): UpdatedParentEvent

@Saga
class SagaC() {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}

    @Autowired
    lateinit var gateway: CommandGateway
    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    fun on(event: CreatedCEvent) {
        log.info("on $event")
        gateway.sendAndWait<Unit>(UpdateSharedCountCommand(event.id))
    }

    @SagaEventHandler(associationProperty = "id")
    @EndSaga
    fun on(event: UpdatedParentEvent) {
        log.info("finished Saga")
    }
}
