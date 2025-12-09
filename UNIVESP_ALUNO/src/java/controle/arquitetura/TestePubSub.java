package controle.arquitetura;

import redis.clients.jedis.Jedis;

public class TestePubSub {

    public static void main(String[] args) {
        System.out.println(">>> INICIANDO TESTE DE PUB/SUB VIA JAVA <<<");
        
        // 1. Tenta Conectar
        try (Jedis jedis = new Jedis("173.249.30.27", 6379)) {
            System.out.println(" 1. Conectado ao Redis com sucesso!");
            
            // 2. Envia o comando de limpar (PUBLISH)
            // Esse é o mesmo comando que o ADM vai dar
            String canal = "univep:cache:invalidation";
            String mensagem = "app:configuracaogeral:unidade:0";
            
            System.out.println(" 2. Publicando no canal: " + canal);
            Long quantosOuviram = jedis.publish(canal, mensagem);
            
            // 3. Resultado
            System.out.println(" 3. Mensagem enviada!");
            System.out.println("   ---> Quantos sistemas receberam? " + quantosOuviram);
            
            if (quantosOuviram > 0) {
                System.out.println("    SUCESSO! O Portal do Aluno está ouvindo.");
            } else {
                System.out.println("    ALERTA: Ninguém ouviu. Verifique se o Portal do Aluno está rodando.");
            }
            
        } catch (Exception e) {
            System.err.println(" ERRO FATAL: Não foi possível conectar no Redis.");
            System.err.println("   Motivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}