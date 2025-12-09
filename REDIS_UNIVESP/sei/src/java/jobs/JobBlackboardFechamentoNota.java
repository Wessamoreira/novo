package jobs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.AssuntoDebugEnum;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.blackboard.BlackboardFechamentoNotaOperacaoVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

@Component
public class JobBlackboardFechamentoNota extends SuperControle {
	
	private static final long serialVersionUID = -8209188938102811018L;
	private final long SEGUNDO = 1000; 
    private final long MINUTO = SEGUNDO * 60; 
    private final long HORA = MINUTO * 60;
	public static final String TIME_ZONE = "America/Sao_Paulo";
	private List<BlackboardFechamentoNotaOperacaoVO> operacoes = new ArrayList<>(0);
	
	public List<BlackboardFechamentoNotaOperacaoVO> getOperacoes() {
		if (operacoes == null) {
			operacoes = new ArrayList<>();
		}
		return operacoes;
	}
	
	public void setOperacoes(List<BlackboardFechamentoNotaOperacaoVO> operacoes) {
		this.operacoes = operacoes;
	}
	
	@Scheduled(fixedDelay = HORA, zone = TIME_ZONE)
	public void run() {
		try {
			if (!Uteis.isVersaoDev() && getAplicacaoControle() != null && !getAplicacaoControle().getProgressBarFechamentoNota().getAtivado()) {
				List<BlackboardFechamentoNotaOperacaoVO> operacoes = getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().consultarFechamentoNotaOperacaoNaoExecutado();
				if (Uteis.isAtributoPreenchido(operacoes)) {
					setOperacoes(operacoes);
					UsuarioVO usuarioVO = new UsuarioVO();
					PessoaVO pessoaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getUsuarioResponsavelOperacoesExternas();
					if (Uteis.isAtributoPreenchido(pessoaVO)) {
						usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(pessoaVO.getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new UsuarioVO());
					}
					getAplicacaoControle().setProgressBarFechamentoNota(new ProgressBarVO());
					getAplicacaoControle().getProgressBarFechamentoNota().resetar();
					getAplicacaoControle().getProgressBarFechamentoNota().setUsuarioVO(usuarioVO);
					getAplicacaoControle().getProgressBarFechamentoNota().setConfiguracaoGeralSistemaVO(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null));
					getAplicacaoControle().getProgressBarFechamentoNota().setCaminhoWebRelatorio(this.context() != null ? getCaminhoPastaWeb() : Constantes.EMPTY);
					getAplicacaoControle().getProgressBarFechamentoNota().setSuperControle(this);
					getAplicacaoControle().getProgressBarFechamentoNota().iniciar(0l, operacoes.size(), "Iniciando Operação", Boolean.TRUE, this, "executarOperacoes");
				}
			}
		} catch (Exception e) {
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.JOB_FECHAMENTO_NOTA, e);
		}
	}
	
	public void executarOperacoes() {
		try {
			if (!Uteis.isVersaoDev() && getAplicacaoControle() != null) {
				if (Uteis.isAtributoPreenchido(getOperacoes())) {
					getFacadeFactory().getBlackboardFechamentoNotaOperacaoInterfaceFacade().executarOperacaoFechamentoNotaBlackboard(getOperacoes(), getAplicacaoControle().getProgressBarFechamentoNota(), this);
				} else {
					getAplicacaoControle().getProgressBarFechamentoNota().setForcarEncerramento(Boolean.TRUE);
				}
			}
		} catch (Exception e) {
			getAplicacaoControle().getProgressBarFechamentoNota().setForcarEncerramento(Boolean.TRUE);
			AplicacaoControle.realizarEscritaErroDebug(AssuntoDebugEnum.JOB_FECHAMENTO_NOTA, e);
		}
	}
}
