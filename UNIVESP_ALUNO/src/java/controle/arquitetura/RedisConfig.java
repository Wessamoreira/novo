package controle.arquitetura;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host:173.249.30.27}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;
    
    public static final String CANAL_INVALIDACAO = "univep:cache:invalidation";


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        System.out.println(">>> REDIS CONFIG: Conectando em " + host + ":" + port);
        
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
    
 
    @Bean
    public ChannelTopic cacheInvalidationTopic() {
        return new ChannelTopic("univep:cache:invalidation");
    }

    // Adaptador para garantir compatibilidade de interface
    @Bean
    public MessageListenerAdapter listenerAdapter(CacheInvalidationListener listener) {
        return new MessageListenerAdapter(listener);
    }

    // O Container que mantém a conexão aberta ouvindo
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            JedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, cacheInvalidationTopic());
        return container;
    }
    
    
    
 // ... seus beans de conexão existentes ...

    @Bean
    public org.springframework.data.redis.listener.ChannelTopic topic() {
        return new org.springframework.data.redis.listener.ChannelTopic("univep:cache:invalidation");
    }

    @Bean
    public org.springframework.data.redis.listener.RedisMessageListenerContainer redisContainer(
            org.springframework.data.redis.connection.jedis.JedisConnectionFactory connectionFactory, 
            CacheListener listener) { // Injeta o listener criado acima
        
        var container = new org.springframework.data.redis.listener.RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, topic());
        return container;
    }
}




