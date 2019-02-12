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
class AggregateB(): ParentAggregate() {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}
    var bCounter: Int = 0

    @CommandHandler
    constructor(command: CreateBCommand): this() {
        log.info("cntr $command")
        AggregateLifecycle.apply(CreatedBEvent(command.id))
    }

    @EventSourcingHandler
    fun on(event: CreatedBEvent) {
        log.info("on $event")
        super.on(event)
    }

    @CommandHandler
    fun handle(command: UpdateBCommand) {
        log.info("handle $command")
        AggregateLifecycle.apply(UpdatedBEvent(command.id, ++bCounter))
    }

    @EventSourcingHandler
    fun on(event: UpdatedBEvent) {
        log.info("on $event")
        apply { counter = event.bCounter }
    }
}

data class CreateBCommand(@TargetAggregateIdentifier var id: UUID)
data class CreatedBEvent(override var id: UUID): CreatedParentEvent

data class UpdateBCommand(@TargetAggregateIdentifier var id: UUID)
data class UpdatedBEvent(var id: UUID, var bCounter: Int)

@Saga
class SagaB() {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}
    @Autowired
    lateinit var gateway: CommandGateway
    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    fun on(event: CreatedBEvent) {
        log.info("on $event")
    }

    @SagaEventHandler(associationProperty = "id")
    fun on(event: UpdatedBEvent) {
        log.info("on $event")
    }

    @SagaEventHandler(associationProperty = "id")
    fun on(event: SharedCounterUpdatedEvent) {
        log.info("on $event")
    }
}
