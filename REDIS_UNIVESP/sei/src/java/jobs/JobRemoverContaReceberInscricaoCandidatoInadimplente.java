package jobs;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobRemoverContaReceberInscricaoCandidatoInadimplente extends SuperFacadeJDBC implements Runnable {

	private static final long serialVersionUID = 1L;

	public void run() {
		try {
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO(), null);
			if (configuracaoFinanceiroVO.getCancelarContaReceberCandidatoInadimplenteAposDataProva()) {
				List<ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberInscricaoCandidatoInadimplente(configuracaoFinanceiroVO.getQtdeDiasAposDataProvaRemoverContaReceberCandidatoInadimplente());
				for (ContaReceberVO contaReceberVO : contaReceberVOs) {
					getFacadeFactory().getContaReceberFacade().alterarSituacao(contaReceberVO.getCodigo(), SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor(), new UsuarioVO());
					getFacadeFactory().getInscricaoFacade().alterarSituacaoPorCandidato(contaReceberVO.getCodOrigem(), SituacaoInscricaoEnum.CANCELADO.name(), new UsuarioVO());
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
