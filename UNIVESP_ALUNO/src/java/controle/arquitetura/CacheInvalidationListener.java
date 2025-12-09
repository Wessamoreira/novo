package controle.arquitetura;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component; 
import java.nio.charset.StandardCharsets;

@Component
public class CacheInvalidationListener implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInvalidationListener.class);

    @Autowired
    private ApplicationContext context;

    private AplicacaoControle aplicacaoControle;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String chaveParaLimpar = new String(message.getBody(), StandardCharsets.UTF_8);
            LOG.info(" EVENTO PUB/SUB: Ordem de limpeza recebida para: {}", chaveParaLimpar);

            // Lazy Load do Controller para evitar Circular Dependency na inicialização
            if (aplicacaoControle == null) {
                aplicacaoControle = context.getBean(AplicacaoControle.class);
            }

            // Roteamento da Limpeza
            if (chaveParaLimpar.contains("configuracaogeral")) {
                aplicacaoControle.forcarLimpezaCacheLocalConfiguracao();
            } 
            else if (chaveParaLimpar.contains("cidade")) {
                // Extrai o ID da string 
                String idStr = chaveParaLimpar.substring(chaveParaLimpar.lastIndexOf(":") + 1);
                try {
                    aplicacaoControle.removerCidade(Integer.parseInt(idStr));
                } catch (NumberFormatException e) {
                    LOG.warn("ID de cidade inválido no evento: {}", chaveParaLimpar);
                }
            }
            // Adicione outros 'else if' para Disciplina, etc.

        } catch (Exception e) {
            LOG.error("Erro ao processar mensagem de invalidação de cache", e);
        }
    }
}