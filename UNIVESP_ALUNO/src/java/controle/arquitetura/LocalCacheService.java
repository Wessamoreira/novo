package controle.arquitetura;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class LocalCacheService {

    // Cache L1 (RAM) compartilhado por todas as threads da instância
    private final Map<String, Object> cacheMemoria = new ConcurrentHashMap<>();

    public <T> T get(String chave) {
        return (T) cacheMemoria.get(chave);
    }

    public void put(String chave, Object valor) {
        if (valor != null) {
            cacheMemoria.put(chave, valor);
        }
    }

    public void remove(String chave) {
        cacheMemoria.remove(chave);
        System.out.println("[CACHE-L1] Removido da RAM: " + chave);
    }
    
    public void limparTudo() {
        cacheMemoria.clear();
    }
}