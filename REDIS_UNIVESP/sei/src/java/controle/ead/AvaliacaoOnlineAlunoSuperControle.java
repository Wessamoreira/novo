package controle.ead;

import java.util.Date;

import controle.arquitetura.SuperControle;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class AvaliacaoOnlineAlunoSuperControle extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3450492237677745518L;
	private AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO;
	
	public AvaliacaoOnlineMatriculaVO getAvaliacaoOnlineMatriculaVO() {
		if(avaliacaoOnlineMatriculaVO == null) {
			avaliacaoOnlineMatriculaVO = new AvaliacaoOnlineMatriculaVO();
		}
		return avaliacaoOnlineMatriculaVO;
	}

	public void setAvaliacaoOnlineMatriculaVO(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) {
		this.avaliacaoOnlineMatriculaVO = avaliacaoOnlineMatriculaVO;
	}

	public void realizarVerificacaoQuestaoUnicaEscolhaAvaliacaoOnline() {
		try {
			AvaliacaoOnlineMatriculaRespostaQuestaoVO orq = (AvaliacaoOnlineMatriculaRespostaQuestaoVO) context().getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (orq.getMarcada()) {
				orq.setMarcada(false);
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarCalculoQuantidadePerguntasRespondidas(getAvaliacaoOnlineMatriculaVO());
			} else {
				orq.setMarcada(true);
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVerificacaoQuestaoUnicaEscolha(getAvaliacaoOnlineMatriculaVO(), orq, getUsuarioLogado());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarCorrecaoAvaliacaoOnline() {
		realizarFinalizacaoAvaliacaoOnline(true);
	}

	public void realizarFinalizacaoAvaliacaoOnline(boolean validarPerguntasRespondidas) {
		try {
			getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().executarCorrecaoAvaliacaoOnline(getAvaliacaoOnlineMatriculaVO(), null, validarPerguntasRespondidas, getUsuarioLogado(), true);
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			setApresentarResultadoAvaliacaoOnline(true);
			setMostrarGabarito(true);
			
		} catch (ConsistirException e) {
			setApresentarResultadoAvaliacaoOnline(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarResultadoAvaliacaoOnline(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	

	public Boolean mostrarGabarito;
	
	public Boolean getMostrarGabarito() {
		if (mostrarGabarito == null) {
			mostrarGabarito = false;
		}
		return mostrarGabarito;
	}

	public void setMostrarGabarito(Boolean mostrarGabarito) {
		this.mostrarGabarito = mostrarGabarito;
	}
	
	private Boolean apresentarResultadoAvaliacaoOnline;
	
	public Boolean getApresentarResultadoAvaliacaoOnline() {
		if (apresentarResultadoAvaliacaoOnline == null) {
			apresentarResultadoAvaliacaoOnline = false;
		}
		return apresentarResultadoAvaliacaoOnline;
	}

	public void setApresentarResultadoAvaliacaoOnline(Boolean apresentarResultadoAvaliacaoOnline) {
		this.apresentarResultadoAvaliacaoOnline = apresentarResultadoAvaliacaoOnline;
	}
	
	
	public Integer totalQuestoes;
	public Integer getTotalQuestoes() {
		if(totalQuestoes == null) {
			totalQuestoes = getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs().size();
		}
		return totalQuestoes;
	}

	public void setTotalQuestoes(Integer totalQuestoes) {
		this.totalQuestoes = totalQuestoes;
	}

	public String getAbrirModalFinalizacaoAvaliacaoOnline() {
		if (getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino().compareTo(new Date()) <= 0 && getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
			return "RichFaces.$('panelFinalizacao').show()";
		}
		return "";
	}

	public String getTempoApresentar() {
		return "Faltam " + Uteis.pegarTempoEntreDuasDatas(getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino(), new Date());
	}

	public boolean isHabilitarTempo() {
		return getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO) && getAvaliacaoOnlineMatriculaVO().getDataLimiteTerminoTemporizador() - getDataAtual() > 0;
	}

	public Long tempoRestanteAvaliacaoOnline;
	public Long getTempoRestanteAvaliacaoOnline() {
		if (tempoRestanteAvaliacaoOnline == null) {
			tempoRestanteAvaliacaoOnline = getAvaliacaoOnlineMatriculaVO().getDataLimiteTerminoTemporizador() - getDataAtual();
		}
		return tempoRestanteAvaliacaoOnline;
	}

	public void setTempoRestanteAvaliacaoOnline(Long tempoRestanteAvaliacaoOnline) {
		this.tempoRestanteAvaliacaoOnline = tempoRestanteAvaliacaoOnline;
	}
	
	public long getDataAtual() {
		return new Date().getTime();
	}

}
