package ru.golden.alf.litlepms.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import ru.golden.alf.litlepms.model.Message
import ru.golden.alf.litlepms.model.OutputMessage
import kotlin.jvm.Throws

@Controller
@CrossOrigin(origins = ["http://localhost:3000"])
class MessageControllers {
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate

    //В целом метод: и принимает сообщение и потом что-то отправляет всем подписчикам
    @SendTo("/topic/message") //Нужно указывать точно, каким слушателям отправлять и неважно, что префикс один в конфиге
    @MessageMapping("/message") //Не нужно указывать /app так как префикс один в конфиге (не будет работать, если указать)
    @Throws(Exception::class)
    fun send(message: Message) : OutputMessage {
        println("Получено следующие сообщений от клиента: $message")
        sendPrivate(message)
        return OutputMessage("Server", "Привет ${message.from}")
    }

    @MessageMapping("app/message/private")
    fun sendPrivate(message: Message) {
        val login = message.to
        //todo пока отправляем тоже сообщение
        println("логин куда отправим $login")
        messagingTemplate.convertAndSend("/topic/message/private-$login", getFakeMessage())
    }

    private fun getFakeMessage(): Message {
        return Message("Server", "Я отправляю приватное сообщение", "fake")
    }
}