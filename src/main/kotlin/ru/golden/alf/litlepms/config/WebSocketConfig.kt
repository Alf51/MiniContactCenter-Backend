package ru.golden.alf.litlepms.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        //todo withStockJS ?
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*") //обычный эндпоинт, по которому мы будем подписываться
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic") //врубаем простой брокер, который будет рассылать инфу всем слушателям, которые подписаны на /topic/+чёт ещё
        registry.setApplicationDestinationPrefixes("/app") //говорим серверу, что по этому префиксу будут получено сообщение от клиента и его нужно обработать
    }
}