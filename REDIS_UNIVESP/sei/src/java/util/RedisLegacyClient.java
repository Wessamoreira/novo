package util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisLegacyClient {

    private static JedisPool jedisPool;
    private static final String CHANNEL_NAME = "univep:cache:invalidation";

    static {
        try {
            System.out.println(">>> [ADM-REDIS] Iniciando Pool de Conexão...");
            
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(10); 
            poolConfig.setMaxIdle(5);
            poolConfig.setMinIdle(1);
            poolConfig.setTestOnBorrow(true); 

            
            jedisPool = new JedisPool(poolConfig, "173.249.30.27", 6379, 2000);
            
            System.out.println(">>> [ADM-REDIS] Conectado com sucesso!");
            
        } catch (Exception e) {
            System.err.println(">>> [ADM-REDIS] ERRO FATAL ao iniciar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método Seguro: Remove do Redis e Avisa o Aluno
     * Não lança exceção para não travar o salvamento do ADM
     */
    public static void invalidarChave(String chave) {
        if (jedisPool == null) {
            System.err.println(">>> [ADM-REDIS] Aviso: Pool não inicializado. Cache não será limpo.");
            return;
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource(); 
            
            jedis.del(chave);
            
            jedis.publish(CHANNEL_NAME, chave);
            
            System.out.println(">>> [ADM-REDIS] Invalidação enviada para chave: " + chave);
            
        } catch (Exception e) {
            System.err.println(">>> [ADM-REDIS] Falha ao invalidar cache (Ignorando para não travar ADM): " + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close(); 
            }
        }
    }
    
    public static void destroy() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
    
    
    /**
     * AÇÃO 2: LEITURA (Monitoramento)
     * Busca um JSON do Redis. Retorna null se não existir ou der erro.
     */
    public static String lerJson(String chave) {
        if (jedisPool == null) return null;

        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(chave);
        } catch (Exception e) {
            System.err.println(">>> [ADM-REDIS] Erro ao ler cache: " + e.getMessage());
            return null;
        }
    }
    
    public static void shutdown() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}