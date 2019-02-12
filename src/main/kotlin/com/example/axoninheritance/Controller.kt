package com.example.axoninheritance

import org.axonframework.commandhandling.gateway.CommandGateway
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class Controller(@Autowired val gateway: CommandGateway) {
    companion object {private val log = LoggerFactory.getLogger(javaClass)}
    @PostMapping("/createB")
    fun create(@RequestBody command: CreateBCommand): ResponseEntity<UUID> {
        log.info("create $command")
        gateway.sendAndWait<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.CREATED)
    }
    @PostMapping("/createC")
    fun create(@RequestBody command: CreateCCommand): ResponseEntity<UUID> {
        log.info("create $command")
        gateway.sendAndWait<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.CREATED)
    }

    @PutMapping("/updateB")
    fun update(@RequestBody command: UpdateBCommand): ResponseEntity<UUID> {
        log.info("update $command")
        gateway.sendAndWait<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.OK)
    }

    @PutMapping("/updateC")
    fun update(@RequestBody command: UpdateCCommand): ResponseEntity<UUID> {
        log.info("update $command")
        gateway.sendAndWait<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.OK)
    }

    @PutMapping("/updateShared")
    fun update(@RequestBody command: UpdateSharedCountCommand): ResponseEntity<UUID> {
        log.info("update $command")
        gateway.sendAndWait<Unit>(command)
        return ResponseEntity(command.id, HttpStatus.OK)
    }
}