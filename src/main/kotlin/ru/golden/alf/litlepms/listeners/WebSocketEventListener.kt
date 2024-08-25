package ru.golden.alf.litlepms.listeners

import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import ru.golden.alf.litlepms.controllers.MessageControllers

@Component
class WebSocketEventListener(
    val messageTemplate: SimpMessageSendingOperations, //Интерфейс, отправляет сообщения по WS. @EnableWebSocketMessageBroker в конфиг классе, скажет, что нужно заинжектить этот бин
    val messageController: MessageControllers
) {
    private val logger = KotlinLogging.logger {}

    @EventListener
    fun onDisconnectEvent(event: SessionDisconnectEvent) {
        //С помощью wrap создаём экземпляр StompHeaderAccessor из сообщения. Так безопасно можно работать с заголовками, менять и т.п. Не повредим оригинал
        //StompHeaderAccessor предоставит доступ к заголовкам. Заголовки это метаданные в протоколе Stomp как и в HTTP
        val stompAccessorHeader: StompHeaderAccessor = StompHeaderAccessor.wrap(event.message)
        //sessionAttributes хранилище данных связанных с сессией пользователя. Могу положить туда любые данные в стиле: ключ - значение
        val login: String = stompAccessorHeader.sessionAttributes?.get("login") as String

        if (login.isNotEmpty()) {
            messageController.deleteClientLogin(login)
            messageTemplate.convertAndSend("/topic/loginDisconnect", login)
            logger.info { "Пользователь $login покинул чат" }
        }
    }

    @EventListener
    fun onConnectEvent(event: SessionConnectEvent) {
        val stompAccessorHeader: StompHeaderAccessor = StompHeaderAccessor.wrap(event.message)
        val login: String = stompAccessorHeader.getFirstNativeHeader("login")!!

        stompAccessorHeader.sessionAttributes?.put("login", login)
        messageController.addLogin(login)
        messageTemplate.convertAndSend("/topic/loginConnected", login)
        logger.info { "$login присоединился к чату" }
    }
}