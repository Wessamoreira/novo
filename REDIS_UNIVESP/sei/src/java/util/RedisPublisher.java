package util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPublisher {

    private static JedisPool pool;
    private static final String CANAL_INVALIDACAO = "univep:cache:invalidation";

    // Inicialização Lazy (Só conecta quando precisar, evita travar o sistema na subida)
    private static synchronized JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(10); // Poucas conexões, é só pra avisar
            config.setMaxIdle(2);
            // IMPORTANTE: Timeout baixo (2s) para não travar o ADM se o Redis cair
            pool = new JedisPool(config, "173.249.30.27", 6379, 2000); 
        }
        return pool;
    }

    public static void notificarAlteracao(String chave) {
        // Bloco try-catch silencioso: Se o Redis morrer, o ADM continua salvando no banco
        try (Jedis jedis = getPool().getResource()) {
            
            // 1. Remove do Redis (Limpa o L2)
            jedis.del(chave);
            
            // 2. Avisa o Portal do Aluno (Limpa o L1 - RAM)
            // Mensagem ex: "app:cidade:21"
            jedis.publish(CANAL_INVALIDACAO, chave);
            
            System.out.println("[REDIS-ADM] Invalidado e Notificado: " + chave);
            
        } catch (Exception e) {
            System.err.println("[REDIS-ADM-ERRO] Falha ao notificar cache (Sistema segue normal): " + e.getMessage());
            // Opcional: invalidar o pool se der erro grave
        }
    }
}