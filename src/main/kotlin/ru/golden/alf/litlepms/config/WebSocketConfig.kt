package ru.golden.alf.litlepms.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import ru.golden.alf.litlepms.security.JwtChannelInterceptor

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(private val jwtChannelInterceptor: JwtChannelInterceptor) : WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        //todo withStockJS ?
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*") //обычный эндпоинт, по которому мы будем подписываться
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic") //врубаем простой брокер, который будет рассылать инфу всем слушателям, которые подписаны на /topic/+чёт ещё
        registry.setApplicationDestinationPrefixes("/app") //говорим серверу, что по этому префиксу будут получено сообщение от клиента и его нужно обработать
    }

    //todo грокнуть Добавление вашего интерцептора для аутентификации
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(jwtChannelInterceptor)
    }
}