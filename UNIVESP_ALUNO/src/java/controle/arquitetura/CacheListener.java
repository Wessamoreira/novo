package controle.arquitetura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class CacheListener implements MessageListener {

    @Autowired
    private LocalCacheService localCache;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Converte bytes para String (Ex: "app:cidade:21")
        String chaveRecebida = new String(message.getBody(), StandardCharsets.UTF_8);
        
        System.out.println(" SINAL RECEBIDO DO ADM: Limpar " + chaveRecebida);
        
        // Remove da memória RAM imediatamente
        localCache.remove(chaveRecebida);
    }
}