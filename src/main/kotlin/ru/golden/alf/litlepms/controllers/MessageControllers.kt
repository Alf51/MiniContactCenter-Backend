package ru.golden.alf.litlepms.controllers

import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import ru.golden.alf.litlepms.model.Message
import ru.golden.alf.litlepms.model.OutputMessage

@Controller
@CrossOrigin(origins = ["http://localhost:3000"])
class MessageControllers(var messagingTemplate: SimpMessageSendingOperations) {
    //todo позже заменить на Redis
    private val clientLoginSet: MutableSet<String> = mutableSetOf()
    //todo логирование добавить
    private val logger = KotlinLogging.logger {}
    fun deleteClientLogin(login: String) {
        this.clientLoginSet.remove(login)
    }

    fun addLogin(login: String) {
        this.clientLoginSet.add(login)
    }

    //В целом метод: и принимает сообщение и потом что-то отправляет всем подписчикам
    @SendTo("/topic/message") //Нужно указывать точно, каким слушателям отправлять и неважно, что префикс один в конфиге
    @MessageMapping("/message") //Не нужно указывать /app так как префикс один в конфиге (не будет работать, если указать)
    @Throws(Exception::class)
    fun send(message: Message): OutputMessage {
        println("Получено следующие сообщений от клиента: $message")
        return OutputMessage("Сообщение всем от ${message.from}", message.text)
    }

    @MessageMapping("/message/private") //Указал без префикса /app. Так как не работало. Спринг добавляет. Получается /app/app
    fun sendPrivate(message: Message) {
        val login = message.to
        messagingTemplate.convertAndSend("/topic/message/private-$login", message)

    }

    @MessageMapping("/requestOnlineLogins")
    fun requestOnlineLogins(simpMessageHeaderAccessor: SimpMessageHeaderAccessor) {
        val login = simpMessageHeaderAccessor.sessionAttributes?.get("login") as String
        messagingTemplate.convertAndSend( "/topic/onlineLogins/${login}", this.clientLoginSet)
    }
}