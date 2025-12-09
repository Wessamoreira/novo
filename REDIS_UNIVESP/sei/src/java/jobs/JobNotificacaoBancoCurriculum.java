package jobs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.TextoPadraoBancoCurriculumVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificacaoBancoCurriculum extends SuperFacadeJDBC implements Runnable, Serializable {

    public void run() {
        try {
            UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
            ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
            try {
                // Consultar vagas que ultrapassaram em 1 dia o periodo definido para expirar. e realizar a expiração da mesma na base de dados.
                List lista = getFacadeFactory().getVagasFacade().consultarVagasExpiramQtdDias(conf.getQtdDiasExpiracaoVagaBancoCurriculum(), usuResp);
                getFacadeFactory().getVagasFacade().alterarSituacaoEDataDeVagasExpiracao(lista, usuResp);

                // Consultar vagas que estão proxima de atingir o periodo de expiração, consultando baseado nos dias para notificação de expiração. Caso o usuario não prolongue o periodo a mesma será expirada na data.
                List listaNotificar = getFacadeFactory().getVagasFacade().consultarVagasParaNotificacaoDeExpiracaoBaseadoDiasParamento(conf.getQtdDiasNotificacaoExpiracaoVagaBancoCurriculum(), usuResp);
                // NOTIFICAR LISTA DE VAGAS...
                realizarEnvioNotificacaoParceiro(listaNotificar, usuResp, conf);
            } catch (Exception e) {
                //System.out.print("Erro 2...");
            }
        } catch (Exception e) {
            //System.out.print("Erro 3...");
        }
    }

    public void realizarEnvioNotificacaoParceiro(List listaNotificar, UsuarioVO usuResp, ConfiguracaoGeralSistemaVO conf) {
        try {
            List<TextoPadraoBancoCurriculumVO> listaTexto = getFacadeFactory().getTextoPadraoBancoCurriculumFacade().consultarPorTipo("iminenciaExpiracaoVaga", false, "AT",Uteis.NIVELMONTARDADOS_TODOS, usuResp);
            if (listaTexto == null || listaTexto.isEmpty()) {
                throw new Exception(" Não existe Texto Padrão cadastro para o tipo: Iminência de Expiração de Vaga.");
            }
            TextoPadraoBancoCurriculumVO textoPadrao = listaTexto.get(0);
            String texto = textoPadrao.getTexto();

            Iterator e = listaNotificar.iterator();
            while (e.hasNext()) {
                VagasVO obj = (VagasVO) e.next();
                TextoPadraoBancoCurriculumVO textoPadraoEnviar = new TextoPadraoBancoCurriculumVO();
                textoPadraoEnviar = textoPadrao;
                textoPadraoEnviar.substituirTagsTextoPadrao(textoPadrao, obj);
                getFacadeFactory().getComunicacaoInternaFacade().executarNotificacaoMensagemPreDefinidaTextoPadrao(textoPadraoEnviar, null, obj.getParceiro(), usuResp, conf);
                textoPadrao.setTexto(texto);
            }
        } catch (Exception e) {
            //System.out.print("Erro 4...");
        }
    }

}
