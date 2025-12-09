# 🏗️ Arquitetura de Cache Multi-Nível com Invalidação Distribuída

## Univep - Portal do Aluno & Portal ADM

---

## 📋 Sumário

1. [Visão Geral](#visão-geral)
2. [Problema Original](#problema-original)
3. [Solução Arquitetural](#solução-arquitetural)
4. [Componentes](#componentes)
5. [Fluxos de Dados](#fluxos-de-dados)
6. [Guia de Implementação](#guia-de-implementação)
7. [Considerações de Performance](#considerações-de-performance)
8. [Troubleshooting](#troubleshooting)

---

## Visão Geral

Esta arquitetura resolve o problema de **consistência de cache em sistemas distribuídos** usando o padrão **Event-Driven Cache Invalidation** via Redis Pub/Sub.

### Características

| Aspecto | Valor |
|---------|-------|
| **Latência L1 (RAM)** | < 1ms |
| **Latência L2 (Redis)** | 1-5ms |
| **Latência L3 (Banco)** | 10-100ms |
| **Tempo de Invalidação** | < 10ms (real-time) |
| **Consistência** | Eventual (ms) + TTL como fallback |

---

## Problema Original

```
┌─────────────────┐         ┌─────────────────┐
│  Portal ADM     │         │  Portal Aluno   │
├─────────────────┤         ├─────────────────┤
│  L1 (RAM) ────────────────── L1 (RAM)       │  ← ISOLADOS!
│       │         │         │       │         │
│       ▼         │         │       ▼         │
│  L2 (Redis) ◄──────────────► L2 (Redis)     │  ← Compartilhado
│       │         │         │       │         │
│       ▼         │         │       ▼         │
│  L3 (Banco) ◄──────────────► L3 (Banco)     │  ← Compartilhado
└─────────────────┘         └─────────────────┘
```

**Cenário problemático:**
1. Aluno acessa ConfiguraçãoGeral → carrega no L1 (RAM)
2. ADM atualiza ConfiguraçãoGeral no banco e Redis
3. Aluno continua vendo dado antigo do L1 até TTL expirar

---

## Solução Arquitetural

### Event-Driven Cache Invalidation

```
┌──────────────────────────────────────────────────────────────────┐
│                         REDIS                                    │
│                                                                  │
│   ┌─────────────────┐    ┌──────────────────────────────────┐   │
│   │   Cache L2      │    │  Pub/Sub Channel                 │   │
│   │   (Dados)       │    │  "univep:cache:invalidation"     │   │
│   └─────────────────┘    └──────────────────────────────────┘   │
│           ▲                           │                          │
│           │                           │                          │
└───────────┼───────────────────────────┼──────────────────────────┘
            │ write                     │ subscribe
            │                           ▼
┌───────────┴─────────┐         ┌─────────────────────┐
│    Portal ADM       │         │    Portal Aluno     │
│    ─────────────    │         │    ──────────────   │
│                     │         │                     │
│  1. Salva DB (L3)   │         │  CacheInvalidation  │
│  2. Salva Redis (L2)│         │  Listener ativo     │
│  3. PUBLISH evento  │────────►│  Invalida L1        │
│                     │         │  instantaneamente   │
└─────────────────────┘         └─────────────────────┘
```

### Por que essa solução?

| Solução | Prós | Contras | Veredicto |
|---------|------|---------|-----------|
| **TTL curto** | Simples | Janela de inconsistência | ❌ Paliativo |
| **API entre serviços** | Controle | Acoplamento, ponto de falha | ❌ Anti-pattern |
| **Pub/Sub Redis** | Real-time, desacoplado | Complexidade inicial | ✅ **Recomendado** |

---

## Componentes

### 1. CacheConfig
Centraliza configurações de TTL e prefixos de chave.

```java
// TTLs por tipo de dado
TTL_LOCAL_CONFIG_GERAL = 30 segundos      // Crítico
TTL_LOCAL_ENTIDADES_ACADEMICAS = 5 minutos // Médio
TTL_LOCAL_DADOS_GEOGRAFICOS = 1 hora       // Estático
```

### 2. LocalCache<V>
Cache L1 thread-safe com TTL configurável.

```java
// Uso
localCache.get(key)                    // Busca
localCache.put(key, value)             // Armazena
localCache.invalidate(key)             // Remove por chave
localCache.invalidateByPrefix(prefix)  // Remove por prefixo
localCache.clear()                     // Limpa tudo
```

### 3. CacheInvalidationEvent
Evento imutável com metadados para rastreabilidade.

```java
// Estrutura
{
  "eventId": "uuid",
  "cacheKey": "univep:config:geral:1",
  "operation": "UPDATE",
  "source": "portal-adm:8080:12345",
  "timestamp": "2024-01-15T10:30:00Z",
  "entityType": "ConfiguracaoGeral",
  "entityId": "1"
}
```

### 4. CacheInvalidationListener
Escuta eventos e despacha para handlers.

```java
// Registro de handlers
listener.registerHandler("univep:config:", event -> {
    configCache.clear();
});
```

### 5. CacheInvalidationPublisher
Publica eventos de invalidação.

```java
// Notificar atualização
publisher.notifyUpdate("univep:config:geral:1");

// Notificar exclusão
publisher.notifyDelete("univep:cidade:123");

// Invalidar por prefixo (operações em massa)
publisher.notifyInvalidatePrefix("univep:cidade:");
```

### 6. MultiLevelCacheService
Orquestra L1/L2/L3 com invalidação automática.

```java
// Leitura (L1 → L2 → L3)
ConfiguracaoGeral config = cacheService.get(
    key,
    ConfiguracaoGeral.class,
    () -> dao.buscarPorId(id)  // Fallback L3
);

// Escrita (L3 → L2 → Pub/Sub)
cacheService.put(key, config, Duration.ofMinutes(10));
```

---

## Fluxos de Dados

### Fluxo de Leitura (Portal do Aluno)

```
┌─────────────────────────────────────────────────────────────────┐
│                    getConfiguracaoGeral()                       │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  L1 (LocalCache)│
                    │  RAM < 1ms      │
                    └────────┬────────┘
                             │
              ┌──────────────┴──────────────┐
              │ HIT?                        │ MISS
              ▼                             ▼
        ┌──────────┐              ┌─────────────────┐
        │ RETORNA  │              │  L2 (Redis)     │
        │ valor    │              │  1-5ms          │
        └──────────┘              └────────┬────────┘
                                           │
                            ┌──────────────┴──────────────┐
                            │ HIT?                        │ MISS
                            ▼                             ▼
                      ┌──────────┐              ┌─────────────────┐
                      │ Atualiza │              │  L3 (Banco)     │
                      │ L1       │              │  10-100ms       │
                      │ RETORNA  │              └────────┬────────┘
                      └──────────┘                       │
                                                         ▼
                                                  ┌──────────┐
                                                  │ Atualiza │
                                                  │ L2 e L1  │
                                                  │ RETORNA  │
                                                  └──────────┘
```

### Fluxo de Escrita (Portal ADM)

```
┌─────────────────────────────────────────────────────────────────┐
│                    salvarConfiguracaoGeral()                    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  1. Salva DB    │
                    │     (L3)        │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  2. Salva Redis │
                    │     (L2)        │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │  3. PUBLISH     │
                    │  evento Pub/Sub │
                    └────────┬────────┘
                             │
          ┌──────────────────┴──────────────────┐
          │                                     │
          ▼                                     ▼
┌─────────────────────┐             ┌─────────────────────┐
│  Portal Aluno #1    │             │  Portal Aluno #2    │
│  Listener recebe    │             │  Listener recebe    │
│  Invalida L1        │             │  Invalida L1        │
│  (< 10ms)           │             │  (< 10ms)           │
└─────────────────────┘             └─────────────────────┘
```

---

## Guia de Implementação

### Passo 1: Adicionar Dependências

```xml
<!-- pom.xml -->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
</dependencies>
```

### Passo 2: Copiar Classes

Copie todas as classes do pacote `controle.arquitetura.cache` para seu projeto:
- `CacheConfig.java`
- `CacheInvalidationEvent.java`
- `LocalCache.java`
- `CacheInvalidationListener.java`
- `CacheInvalidationPublisher.java`
- `RedisConfig.java`
- `MultiLevelCacheService.java`

### Passo 3: Configurar application.yml

```yaml
spring:
  redis:
    host: seu-redis-host
    port: 6379
```

### Passo 4: Adaptar AplicacaoControle (Portal Aluno)

```java
// ANTES
public ConfiguracaoGeral getConfiguracaoGeral() {
    if (cacheLocal != null && !expirou()) {
        return cacheLocal;
    }
    synchronized(this) {
        // ... código complexo ...
    }
}

// DEPOIS
@Autowired
private MultiLevelCacheService cacheService;

public ConfiguracaoGeral getConfiguracaoGeral() {
    return cacheService.get(
        "univep:config:geral:1",
        ConfiguracaoGeral.class,
        () -> dao.buscarPorId(1)
    );
}
```

### Passo 5: Adaptar Facades (Portal ADM)

```java
@Autowired
private CacheInvalidationPublisher publisher;

public void salvar(ConfiguracaoGeral config) {
    dao.salvar(config);
    redisTemplate.opsForValue().set(key, config);
    publisher.notifyUpdate(key);  // ← ADICIONAR ESTA LINHA
}
```

---

## Considerações de Performance

### Benchmark Esperado

| Operação | Latência | Throughput |
|----------|----------|------------|
| L1 Hit | < 1ms | 100.000+ ops/s |
| L2 Hit | 1-5ms | 50.000 ops/s |
| L3 Hit | 10-100ms | 1.000 ops/s |
| Pub/Sub Latência | < 10ms | N/A |

### Otimizações Aplicadas

1. **ReadWriteLock no LocalCache**
   - Leituras paralelas (não bloqueiam)
   - Apenas escritas são exclusivas

2. **Double-check pattern**
   - Evita computação duplicada em cenários de alta concorrência

3. **TTL como defesa em profundidade**
   - Mesmo se Pub/Sub falhar, TTL garante consistência eventual

4. **Evicção lazy**
   - Entradas expiradas são removidas na próxima leitura
   - Evita overhead de threads de limpeza

### Monitoramento

```java
// Verificar métricas
cacheService.logStats();

// Output:
// Cache[config]: size=15, hits=45000, misses=120, hitRate=99.73%
// Cache[academico]: size=500, hits=120000, misses=800, hitRate=99.34%
```

---

## Troubleshooting

### Problema: Cache não está sendo invalidado

**Verificar:**
1. Listener está registrado?
   ```java
   log.info("Instance: {}", listener.getInstanceId());
   ```

2. Canal está correto?
   ```java
   // Deve ser o mesmo em Publisher e Listener
   "univep:cache:invalidation"
   ```

3. Redis Pub/Sub está funcionando?
   ```bash
   redis-cli SUBSCRIBE univep:cache:invalidation
   # Em outro terminal:
   redis-cli PUBLISH univep:cache:invalidation '{"test":"ok"}'
   ```

### Problema: Dados desatualizados mesmo após invalidação

**Verificar:**
1. Source ID é o mesmo? (eventos da própria instância são ignorados)
2. TTL local é muito longo?
3. Há múltiplas instâncias do LocalCache?

### Problema: Alta latência no Redis

**Verificar:**
1. Pool de conexões configurado?
   ```yaml
   spring.redis.jedis.pool.max-active: 50
   ```

2. Timeout adequado?
   ```yaml
   spring.redis.timeout: 2000ms
   ```

3. Redis está sobrecarregado?
   ```bash
   redis-cli INFO stats
   ```

### Problema: Memória crescendo (Memory Leak)

**Verificar:**
1. TTL está configurado?
2. Evicção está funcionando?
   ```java
   // Forçar limpeza de expirados
   localCache.evictExpired();
   ```

---

## Diagrama de Sequência Completo

```
┌─────────┐          ┌─────────┐          ┌─────────┐          ┌─────────┐
│  Aluno  │          │ Portal  │          │  Redis  │          │  Banco  │
│ Browser │          │  Aluno  │          │         │          │         │
└────┬────┘          └────┬────┘          └────┬────┘          └────┬────┘
     │                    │                    │                    │
     │ GET /config        │                    │                    │
     │───────────────────>│                    │                    │
     │                    │                    │                    │
     │                    │ L1.get(key)        │                    │
     │                    │────────┐           │                    │
     │                    │        │ MISS      │                    │
     │                    │<───────┘           │                    │
     │                    │                    │                    │
     │                    │ GET key            │                    │
     │                    │───────────────────>│                    │
     │                    │                    │                    │
     │                    │      value         │                    │
     │                    │<───────────────────│                    │
     │                    │                    │                    │
     │                    │ L1.put(key,value)  │                    │
     │                    │────────┐           │                    │
     │                    │<───────┘           │                    │
     │                    │                    │                    │
     │    ConfigGeral     │                    │                    │
     │<───────────────────│                    │                    │
     │                    │                    │                    │


┌─────────┐          ┌─────────┐          ┌─────────┐          ┌─────────┐
│  Admin  │          │ Portal  │          │  Redis  │          │ Portal  │
│ Browser │          │   ADM   │          │ Pub/Sub │          │  Aluno  │
└────┬────┘          └────┬────┘          └────┬────┘          └────┬────┘
     │                    │                    │                    │
     │ POST /config       │                    │                    │
     │───────────────────>│                    │                    │
     │                    │                    │                    │
     │                    │ dao.save()         │                    │
     │                    │────────────────────┼───────────────────>│
     │                    │                    │                    │
     │                    │ redis.set()        │                    │
     │                    │───────────────────>│                    │
     │                    │                    │                    │
     │                    │ PUBLISH event      │                    │
     │                    │───────────────────>│                    │
     │                    │                    │                    │
     │                    │                    │ onMessage()        │
     │                    │                    │───────────────────>│
     │                    │                    │                    │
     │                    │                    │    L1.invalidate() │
     │                    │                    │                    │────┐
     │                    │                    │                    │<───┘
     │                    │                    │                    │
     │       OK           │                    │                    │
     │<───────────────────│                    │                    │
     │                    │                    │                    │
```

---

## Comparativo: Antes vs Depois

### Código Antes (Problemático)

```java
public class AplicacaoControle {
    private ConfiguracaoGeral cacheLocal;
    private long ultimaAtualizacao;
    private static final long TTL = 300_000; // 5 minutos
    
    public ConfiguracaoGeral getConfiguracaoGeral() {
        // L1 - Verifica cache local
        if (cacheLocal != null && 
            System.currentTimeMillis() - ultimaAtualizacao < TTL) {
            return cacheLocal;
        }
        
        synchronized(this) {
            // Double-check
            if (cacheLocal != null && 
                System.currentTimeMillis() - ultimaAtualizacao < TTL) {
                return cacheLocal;
            }
            
            // L2 - Redis
            ConfiguracaoGeral fromRedis = redisService.get(key);
            if (fromRedis != null) {
                this.cacheLocal = fromRedis;
                this.ultimaAtualizacao = System.currentTimeMillis();
                return fromRedis;
            }
            
            // L3 - Banco
            ConfiguracaoGeral fromDB = dao.buscar();
            redisService.set(key, fromDB);
            this.cacheLocal = fromDB;
            this.ultimaAtualizacao = System.currentTimeMillis();
            return fromDB;
        }
    }
}
```

**Problemas:**
- ❌ Dado desatualizado por até 5 minutos
- ❌ Código verboso e repetitivo
- ❌ Synchronized bloqueia threads
- ❌ Sem métricas de hit/miss
- ❌ Sem invalidação entre serviços

### Código Depois (Solução)

```java
@Component
public class AplicacaoControle {
    
    @Autowired
    private MultiLevelCacheService cacheService;
    
    public ConfiguracaoGeral getConfiguracaoGeral() {
        return cacheService.get(
            "univep:config:geral:1",
            ConfiguracaoGeral.class,
            () -> dao.buscar()
        );
    }
}
```

**Benefícios:**
- ✅ Invalidação instantânea via Pub/Sub
- ✅ Código limpo e declarativo
- ✅ ReadWriteLock (leituras não bloqueiam)
- ✅ Métricas automáticas
- ✅ TTL como defesa em profundidade

---

## Checklist de Implementação

### Portal do Aluno

- [ ] Adicionar dependências no pom.xml
- [ ] Copiar classes do pacote `cache`
- [ ] Configurar `application.yml`
- [ ] Injetar `MultiLevelCacheService` no `AplicacaoControle`
- [ ] Substituir métodos de cache por `cacheService.get()`
- [ ] Testar leitura de dados
- [ ] Verificar logs de invalidação

### Portal ADM

- [ ] Adicionar dependências no pom.xml
- [ ] Copiar classes `CacheConfig`, `CacheInvalidationEvent`, `CacheInvalidationPublisher`
- [ ] Configurar `application.yml`
- [ ] Injetar `CacheInvalidationPublisher` nos Facades
- [ ] Adicionar `publisher.notifyUpdate()` após cada save
- [ ] Testar propagação de eventos

### Validação

- [ ] ADM atualiza dado → Aluno vê atualização em < 1 segundo
- [ ] Métricas de hit rate > 95%
- [ ] Sem memory leaks após 24h
- [ ] Logs de invalidação aparecendo corretamente

---

## Arquivos do Projeto

```
cache-architecture/
├── src/main/java/controle/arquitetura/
│   ├── cache/
│   │   ├── CacheConfig.java              # Configurações e constantes
│   │   ├── CacheInvalidationEvent.java   # Evento de invalidação
│   │   ├── LocalCache.java               # Cache L1 thread-safe
│   │   ├── CacheInvalidationListener.java # Subscriber Pub/Sub
│   │   ├── CacheInvalidationPublisher.java # Publisher Pub/Sub
│   │   ├── RedisConfig.java              # Config Spring Redis
│   │   └── MultiLevelCacheService.java   # Orquestrador L1/L2/L3
│   └── AplicacaoControleExemplo.java     # Exemplo Portal Aluno
├── src/main/java/controle/adm/
│   └── ConfiguracaoGeralFacadeExemplo.java # Exemplo Portal ADM
└── src/main/resources/
    └── application.yml                    # Configurações
```

---

## Conclusão

Esta arquitetura resolve o problema de consistência de cache entre o Portal ADM e Portal do Aluno de forma:

1. **Performática**: L1 em RAM garante latência < 1ms
2. **Consistente**: Pub/Sub invalida em tempo real
3. **Resiliente**: TTL como fallback de segurança
4. **Escalável**: Funciona com N instâncias
5. **Manutenível**: Código limpo e desacoplado

A solução é a recomendada para sistemas distribuídos que compartilham dados e precisam de alta performance com consistência quase real-time.

---

*Documento gerado para Univep - Arquitetura de Cache Multi-Nível*
