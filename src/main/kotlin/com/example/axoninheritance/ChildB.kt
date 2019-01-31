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
}

data class CreateBCommand(@TargetAggregateIdentifier var id: UUID)
data class CreatedBEvent(override var id: UUID): CreatedParentEvent

data class UpdateBCommand(@TargetAggregateIdentifier var id: UUID)
data class UpdatedBEvent(override var id: UUID): UpdatedParentEvent

@Saga
class SagaB() {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}
    @Autowired
    lateinit var gateway: CommandGateway
    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    fun on(event: CreatedBEvent) {
        log.info("on $event")
        gateway.sendAndWait<Unit>(UpdateSharedCountCommand(event.id))
    }

    @SagaEventHandler(associationProperty = "id")
    @EndSaga
    fun on(event: UpdatedParentEvent) {
        log.info("finished Saga")
    }
}
