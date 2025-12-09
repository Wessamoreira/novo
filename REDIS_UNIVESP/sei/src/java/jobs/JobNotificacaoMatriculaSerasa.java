package jobs;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Service
@Lazy
public class JobNotificacaoMatriculaSerasa extends SuperFacadeJDBC implements Runnable, Serializable {

    public void run() {
        try {
            //System.out.print("Iniciou Processamento SERASA");
            UsuarioVO usuResp = getFacadeFactory().getUsuarioFacade().consultarUsuarioUnicoDMParaMatriculaCRM(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
            ConfiguracaoGeralSistemaVO conf = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
            try {
                //System.out.print("SERASA => Consultar vagas que estão proxima de atingir o periodo de expiração, consultando baseado nos dias para notificação de expiração. Caso o usuario não prolongue o periodo a mesma será expirada na data.");
                // Consultar vagas que estão proxima de atingir o periodo de expiração, consultando baseado nos dias para notificação de expiração. Caso o usuario não prolongue o periodo a mesma será expirada na data.
                List listaNotificar = getFacadeFactory().getNegociacaoRecebimentoFacade().consultaRapidaMatriculaNegativadaSerasaQueRealizouPagamento(Uteis.getDataPassada(new Date(),5), null, usuResp);

                //System.out.print("SERASA => NOTIFICAR LISTA");

                // NOTIFICAR LISTA...
                try {
                    realizarEnvioNotificacao(listaNotificar, usuResp, conf);
                    Iterator i = listaNotificar.iterator();
                    while (i.hasNext()) {
                        NegociacaoRecebimentoVO neg = (NegociacaoRecebimentoVO)i.next();
                        getFacadeFactory().getContaReceberNegociacaoRecebimentoFacade().alterarSituacaoParaNotificacaoSerasa(Boolean.TRUE, neg.getParceiroVO().getCodigo(), usuResp);
                        //System.out.print("SERASA => NOTIFICOU ALUNO (codigo ContaReceberNegociacaoRecebimento )=> " + neg.getParceiroVO().getCodigo());
                    }
                    //System.out.print("SERASA => NOTIFICOU");
                } catch (Exception e) {
                    //System.out.print("Erro Envio Email (1) => " + e.getMessage());
                }

            } catch (Exception e) {
                //System.out.print("Erro Envio Email (2) => " + e.getMessage());
            }
        } catch (Exception e) {
            //System.out.print("Erro Envio Email (3) => " + e.getMessage());
        }
    }

    public void realizarEnvioNotificacao(List listaNotificar, UsuarioVO usuResp, ConfiguracaoGeralSistemaVO conf) {
        try {
            Iterator e = listaNotificar.iterator();
            while (e.hasNext()) {
                NegociacaoRecebimentoVO neg = (NegociacaoRecebimentoVO) e.next();
                ComunicacaoInternaVO obj = new ComunicacaoInternaVO();
                PessoaVO pessoa = new PessoaVO();
                pessoa.setCodigo(neg.getCodigo());
                getFacadeFactory().getPessoaFacade().carregarDados(pessoa, NivelMontarDados.TODOS, usuResp);
                getFacadeFactory().getPessoaFacade().carregarDados(neg.getPessoa(), NivelMontarDados.BASICO, usuResp);
                obj.setMensagem(getMensagemLayout1(neg, pessoa));
                //obj.setMensagem(obj.getMensagem().replace("DIGITE O TEXTO AQUI", getMensagemLayout1(neg, pessoa)));
                //obj.setMensagem(Uteis.trocarHashTag("#NOMEALUNO", neg.getPessoa().getNome(), obj.getMensagem()));
                getFacadeFactory().getComunicacaoInternaFacade().executarNotificacaoMatriculaSerasa(neg, pessoa, obj.getMensagem(), usuResp, conf);
            }
        } catch (Exception e) {
            //System.out.print("Erro 4...");
        }
    }

    public String getMensagemLayout1(NegociacaoRecebimentoVO obj, PessoaVO func) {
        StringBuilder sb = new StringBuilder();
		PessoaVO respFin = func.getResponsavelFinanceiroAluno();
		if (respFin.getCodigo().intValue() > 0) {
			sb.append(" <p> "+ respFin.getNome() +", </p> ");
		} else {
			sb.append(" <p> "+ func.getNome() +", </p> ");
		}
		if (obj.getPessoa().getCPF().equals("")) {
			sb.append(" <p> O aluno " + obj.getPessoa().getNome() + ", de matrícula " + obj.getMatricula() + ", </p> ");
		} else {
			sb.append(" <p> O aluno " + obj.getPessoa().getNome() + ", portador de CPF sob o n° " + obj.getPessoa().getCPF() + ", de matrícula " + obj.getMatricula() + ", </p> ");        
		}
        sb.append(" <p> realizou o pagamento de boleto(s) pendente(s). </p>");
        return sb.toString();
    }

}
