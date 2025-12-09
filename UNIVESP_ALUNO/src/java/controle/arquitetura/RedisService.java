package controle.arquitetura;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; 
import org.springframework.stereotype.Service; 
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.concurrent.TimeUnit;

@Service 
public class RedisService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    @Qualifier("gson") 
    private Gson gson; 
    
    public <T> T getObjeto(String chave, Class<T> tipo) {
        try {
            String json = redisTemplate.opsForValue().get(chave);
            
            if (json == null || json.isEmpty()) {
                return null; 
            }
            
            return gson.fromJson(json, tipo);
            
        } catch (JsonSyntaxException e) {
            LOG.error("JSON Quebrado na chave {}: {}", chave, e.getMessage());
            remover(chave); 
            return null; 
        } catch (Exception e) {
            LOG.error("Erro Redis GET {}: {}", chave, e.getMessage());
            return null;
        }
    }

    public void setObjeto(String chave, Object objeto) {
        try {
            if (objeto == null) return;
            String json = gson.toJson(objeto);
            redisTemplate.opsForValue().set(chave, json);
        } catch (Exception e) {
            LOG.error("Erro Redis SET {}: {}", chave, e.getMessage());
        }
    }

    public void setObjeto(String chave, Object objeto, long tempo, TimeUnit unidade) {
        try {
            if (objeto == null) return;
            String json = gson.toJson(objeto);
            redisTemplate.opsForValue().set(chave, json, tempo, unidade);
        } catch (Exception e) {
            LOG.error("Erro Redis SET TTL {}: {}", chave, e.getMessage());
        }
    }
    
    public void remover(String chave) {
        try {
            redisTemplate.delete(chave);
        } catch (Exception e) {
            LOG.error("Erro Redis DEL {}: {}", chave, e.getMessage());
        }
    }
}