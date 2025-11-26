package pe.edu.upeu.turismospringboot.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("WebSocketStompConfig - Pruebas Unitarias")
class WebSocketStompConfigTest {

    @Mock
    private HandshakeInterceptor handshakeInterceptor;

    @Mock
    private HandshakeHandler handshakeHandler;

    @Mock
    private MessageBrokerRegistry messageBrokerRegistry;

    @Mock
    private StompEndpointRegistry stompEndpointRegistry;

    @Mock
    private StompWebSocketEndpointRegistration endpointRegistration;

    @InjectMocks
    private WebSocketStompConfig webSocketStompConfig;

    @BeforeEach
    void setUp() {
        // Configurar comportamiento de mocks para tests de endpoints
        when(stompEndpointRegistry.addEndpoint(anyString()))
                .thenReturn(endpointRegistration);
        when(endpointRegistration.addInterceptors(any(HandshakeInterceptor.class)))
                .thenReturn(endpointRegistration);
        when(endpointRegistration.setHandshakeHandler(any(HandshakeHandler.class)))
                .thenReturn(endpointRegistration);
        when(endpointRegistration.setAllowedOrigins(anyString()))
                .thenReturn(endpointRegistration);
    }

    @Test
    @DisplayName("Debe configurar el Message Broker correctamente")
    void testConfigureMessageBroker() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry, times(1))
                .enableSimpleBroker("/queue", "/topic");
        verify(messageBrokerRegistry, times(1))
                .setApplicationDestinationPrefixes("/app");
        verify(messageBrokerRegistry, times(1))
                .setUserDestinationPrefix("/user");
    }

    @Test
    @DisplayName("Debe registrar el endpoint STOMP con interceptor y handler")
    void testRegisterStompEndpoints() {
        // When
        webSocketStompConfig.registerStompEndpoints(stompEndpointRegistry);

        // Then
        verify(stompEndpointRegistry, times(1))
                .addEndpoint("/ws-chat");
        verify(endpointRegistration, times(1))
                .addInterceptors(handshakeInterceptor);
        verify(endpointRegistration, times(1))
                .setHandshakeHandler(handshakeHandler);
        verify(endpointRegistration, times(1))
                .setAllowedOrigins("*");
    }

    @Test
    @DisplayName("Debe inyectar el HandshakeInterceptor correctamente")
    void testHandshakeInterceptorInjection() {
        // When
        webSocketStompConfig.registerStompEndpoints(stompEndpointRegistry);

        // Then
        verify(endpointRegistration).addInterceptors(handshakeInterceptor);
    }

    @Test
    @DisplayName("Debe inyectar el HandshakeHandler correctamente")
    void testHandshakeHandlerInjection() {
        // When
        webSocketStompConfig.registerStompEndpoints(stompEndpointRegistry);

        // Then
        verify(endpointRegistration).setHandshakeHandler(handshakeHandler);
    }

    @Test
    @DisplayName("Debe configurar prefijos de destino correctamente")
    void testApplicationDestinationPrefixes() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry).setApplicationDestinationPrefixes("/app");
    }

    @Test
    @DisplayName("Debe habilitar simple broker para queue y topic")
    void testSimpleBrokerConfiguration() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry).enableSimpleBroker("/queue", "/topic");
    }

    @Test
    @DisplayName("Debe configurar prefijo de destino de usuario")
    void testUserDestinationPrefix() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry).setUserDestinationPrefix("/user");
    }

    @Test
    @DisplayName("Debe permitir todos los orígenes (CORS)")
    void testAllowedOrigins() {
        // When
        webSocketStompConfig.registerStompEndpoints(stompEndpointRegistry);

        // Then
        verify(endpointRegistration).setAllowedOrigins("*");
    }

    @Test
    @DisplayName("Debe registrar solo un endpoint /ws-chat")
    void testSingleEndpointRegistration() {
        // When
        webSocketStompConfig.registerStompEndpoints(stompEndpointRegistry);

        // Then
        verify(stompEndpointRegistry, times(1)).addEndpoint(anyString());
        verify(stompEndpointRegistry).addEndpoint("/ws-chat");
    }

    @Test
    @DisplayName("Debe configurar broker sin opciones adicionales")
    void testSimpleBrokerWithoutAdditionalOptions() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry, never()).setPreservePublishOrder(anyBoolean());
        verify(messageBrokerRegistry, never()).setPathMatcher(any());
    }

    @Test
    @DisplayName("Debe usar prefijo /app para mensajes de aplicación")
    void testApplicationPrefixIsApp() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry).setApplicationDestinationPrefixes("/app");
        verify(messageBrokerRegistry, never()).setApplicationDestinationPrefixes("/topic");
        verify(messageBrokerRegistry, never()).setApplicationDestinationPrefixes("/queue");
    }

    @Test
    @DisplayName("Debe configurar destinos de broker correctamente")
    void testBrokerDestinations() {
        // When
        webSocketStompConfig.configureMessageBroker(messageBrokerRegistry);

        // Then
        verify(messageBrokerRegistry).enableSimpleBroker("/queue", "/topic");
        verify(messageBrokerRegistry, times(1)).enableSimpleBroker(anyString(), anyString());
    }
}