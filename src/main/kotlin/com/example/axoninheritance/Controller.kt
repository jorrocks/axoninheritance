package com.example.axoninheritance

import org.axonframework.commandhandling.gateway.CommandGateway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class Controller(@Autowired val gateway: CommandGateway) {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}
    @PostMapping("/createB")
    fun create(@RequestBody command: CreateBCommand): ResponseEntity<UUID> {
        log.info("create $command")
        gateway.send<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.CREATED)
    }
    @PostMapping("/createC")
    fun create(@RequestBody command: CreateCCommand): ResponseEntity<UUID> {
        log.info("create $command")
        gateway.send<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.CREATED)
    }
}