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
    //В целом метод: и принимает сообщение и потом что-то отправляет всем подписчикам
    @SendTo("/topic/message") //Нужно указывать точно, каким слушателям отправлять и неважно, что префикс один в конфиге
    @MessageMapping("/message") //Можно не указывать /app так как префикс один в конфиге
    @Throws(Exception::class)
    fun send(message: Message) : OutputMessage {
        println("Получено следующие сообщений от клиента: $message")
        return OutputMessage("Server", "Привет из бэкенда")
    }
}