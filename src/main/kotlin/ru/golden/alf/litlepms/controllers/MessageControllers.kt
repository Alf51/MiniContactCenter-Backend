package ru.golden.alf.litlepms.controllers

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import ru.golden.alf.litlepms.model.Message
import ru.golden.alf.litlepms.model.OutputMessage
import kotlin.jvm.Throws

@Controller
@CrossOrigin(origins = ["http://localhost:3000"])
class MessageControllers {

    @SendTo("/topic/message")
    @MessageMapping("/message")
    @Throws(Exception::class)
    fun send(message: Message) : OutputMessage {
        println("Получено следующие сообщений от клиента: $message")
        return OutputMessage("Ответ от сервера", "Привет из бэкенда")
    }
}