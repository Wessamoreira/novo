package negocio.comuns.estagio;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.estagio.enumeradores.MotivosPadroesEstagioCasoUsoEnum;

public class MotivosPadroesEstagioVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3436352223865464207L;
	private Integer codigo;
	private String descricao;
	private StatusAtivoInativoEnum statusAtivoInativoEnum;
	/**
	 * Estagio Obrigatorio
	 */
	private boolean retornoSolicitacaoAproveitamento = false;
	private boolean indeferimentoSolicitacaoAproveitamento = false;
	private boolean retornoSolicitacaoEquivalencia = false;
	private boolean indeferimentoSolicitacaoEquivalencia = false;

	/**
	 * Estagio Não Obrigatorio
	 */
	private boolean retornoAvaliacaoTermo = false;
	private boolean indeferimentoAvaliacaoTermo = false;
	private boolean retornoAvaliacaoRelatorioFinal = false;
	private boolean indeferimentoAvaliacaoRelatorioFinal = false;
	private boolean retornoAditivos = false;
	private boolean indeferimentoAditivos = false;
	private boolean retornoRecisao = false;
	private boolean indeferimentoRecisao = false;

	private MotivosPadroesEstagioCasoUsoEnum motivosPadroesEstagioCasoUsoEnumFiltro;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public StatusAtivoInativoEnum getStatusAtivoInativoEnum() {
		if (statusAtivoInativoEnum == null) {
			statusAtivoInativoEnum = StatusAtivoInativoEnum.NENHUM;
		}
		return statusAtivoInativoEnum;
	}

	public void setStatusAtivoInativoEnum(StatusAtivoInativoEnum statusAtivoInativoEnum) {
		this.statusAtivoInativoEnum = statusAtivoInativoEnum;
	}

	public boolean isRetornoSolicitacaoAproveitamento() {
		return retornoSolicitacaoAproveitamento;
	}

	public void setRetornoSolicitacaoAproveitamento(boolean retornoSolicitacaoAproveitamento) {
		this.retornoSolicitacaoAproveitamento = retornoSolicitacaoAproveitamento;
	}

	public boolean isIndeferimentoSolicitacaoAproveitamento() {
		return indeferimentoSolicitacaoAproveitamento;
	}

	public void setIndeferimentoSolicitacaoAproveitamento(boolean indeferimentoSolicitacaoAproveitamento) {
		this.indeferimentoSolicitacaoAproveitamento = indeferimentoSolicitacaoAproveitamento;
	}

	public boolean isRetornoSolicitacaoEquivalencia() {
		return retornoSolicitacaoEquivalencia;
	}

	public void setRetornoSolicitacaoEquivalencia(boolean retornoSolicitacaoEquivalencia) {
		this.retornoSolicitacaoEquivalencia = retornoSolicitacaoEquivalencia;
	}

	public boolean isIndeferimentoSolicitacaoEquivalencia() {
		return indeferimentoSolicitacaoEquivalencia;
	}

	public void setIndeferimentoSolicitacaoEquivalencia(boolean indeferimentoSolicitacaoEquivalencia) {
		this.indeferimentoSolicitacaoEquivalencia = indeferimentoSolicitacaoEquivalencia;
	}

	public boolean isRetornoAvaliacaoTermo() {
		return retornoAvaliacaoTermo;
	}

	public void setRetornoAvaliacaoTermo(boolean retornoAvaliacaoTermo) {
		this.retornoAvaliacaoTermo = retornoAvaliacaoTermo;
	}

	public boolean isIndeferimentoAvaliacaoTermo() {
		return indeferimentoAvaliacaoTermo;
	}

	public void setIndeferimentoAvaliacaoTermo(boolean indeferimentoAvaliacaoTermo) {
		this.indeferimentoAvaliacaoTermo = indeferimentoAvaliacaoTermo;
	}

	public boolean isRetornoAvaliacaoRelatorioFinal() {
		return retornoAvaliacaoRelatorioFinal;
	}

	public void setRetornoAvaliacaoRelatorioFinal(boolean retornoAvaliacaoRelatorioFinal) {
		this.retornoAvaliacaoRelatorioFinal = retornoAvaliacaoRelatorioFinal;
	}

	public boolean isIndeferimentoAvaliacaoRelatorioFinal() {
		return indeferimentoAvaliacaoRelatorioFinal;
	}

	public void setIndeferimentoAvaliacaoRelatorioFinal(boolean indeferimentoAvaliacaoRelatorioFinal) {
		this.indeferimentoAvaliacaoRelatorioFinal = indeferimentoAvaliacaoRelatorioFinal;
	}

	public boolean isRetornoAditivos() {
		return retornoAditivos;
	}

	public void setRetornoAditivos(boolean retornoAditivos) {
		this.retornoAditivos = retornoAditivos;
	}

	public boolean isIndeferimentoAditivos() {
		return indeferimentoAditivos;
	}

	public void setIndeferimentoAditivos(boolean indeferimentoAditivos) {
		this.indeferimentoAditivos = indeferimentoAditivos;
	}

	public boolean isRetornoRecisao() {
		return retornoRecisao;
	}

	public void setRetornoRecisao(boolean retornoRecisao) {
		this.retornoRecisao = retornoRecisao;
	}

	public boolean isIndeferimentoRecisao() {
		return indeferimentoRecisao;
	}

	public void setIndeferimentoRecisao(boolean indeferimentoRecisao) {
		this.indeferimentoRecisao = indeferimentoRecisao;
	}

	public MotivosPadroesEstagioCasoUsoEnum getMotivosPadroesEstagioCasoUsoEnumFiltro() {
		if (motivosPadroesEstagioCasoUsoEnumFiltro == null) {
			motivosPadroesEstagioCasoUsoEnumFiltro = MotivosPadroesEstagioCasoUsoEnum.NENHUM;
		}
		return motivosPadroesEstagioCasoUsoEnumFiltro;
	}

	public void setMotivosPadroesEstagioCasoUsoEnumFiltro(MotivosPadroesEstagioCasoUsoEnum motivosPadroesEstagioCasoUsoEnumFiltro) {
		this.motivosPadroesEstagioCasoUsoEnumFiltro = motivosPadroesEstagioCasoUsoEnumFiltro;
	}
	
	

}
