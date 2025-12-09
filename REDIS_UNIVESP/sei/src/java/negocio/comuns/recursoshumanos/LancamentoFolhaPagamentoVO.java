package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsavel por manter os dados da entidade LancamentoFolhaPagamentoVO. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class LancamentoFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private Date dataCompetencia;
	private CompetenciaPeriodoFolhaPagamentoVO periodo;
	private GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento;
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento;
	private boolean rascunho;

	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO;
	
	private Boolean ativo;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataCompetencia() {
		if (dataCompetencia == null) {
			dataCompetencia = new Date();
		}
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getPeriodo() {
		if (periodo == null) {
			periodo = new CompetenciaPeriodoFolhaPagamentoVO();
		}
		return periodo;
	}

	public void setPeriodo(CompetenciaPeriodoFolhaPagamentoVO periodo) {
		this.periodo = periodo;
	}

	public GrupoLancamentoFolhaPagamentoVO getGrupoLancamentoFolhaPagamento() {
		if (grupoLancamentoFolhaPagamento == null) {
			grupoLancamentoFolhaPagamento = new GrupoLancamentoFolhaPagamentoVO();
		}
		return grupoLancamentoFolhaPagamento;
	}

	public void setGrupoLancamentoFolhaPagamento(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento) {
		this.grupoLancamentoFolhaPagamento = grupoLancamentoFolhaPagamento;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamento() {
		if (templateLancamentoFolhaPagamento == null) {
			templateLancamentoFolhaPagamento = new TemplateLancamentoFolhaPagamentoVO();
		}
		return templateLancamentoFolhaPagamento;
	}

	public void setTemplateLancamentoFolhaPagamento(
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento) {
		this.templateLancamentoFolhaPagamento = templateLancamentoFolhaPagamento;
	}

	public boolean getRascunho() {
		return rascunho;
	}

	public void setRascunho(boolean rascunho) {
		this.rascunho = rascunho;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamentoVO() {
		if (competenciaFolhaPagamentoVO == null)
			competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();
		return competenciaFolhaPagamentoVO;
	}

	public void setCompetenciaFolhaPagamentoVO(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		this.competenciaFolhaPagamentoVO = competenciaFolhaPagamentoVO;
	}

	public Boolean getAtivo() {
		if (ativo == null)
			ativo = false;
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getAtivoAprensentar() {
		if(getAtivo())
			return UteisJSF.internacionalizar("prt_TextoPadrao_Sim");
		else
			return UteisJSF.internacionalizar("prt_TextoPadrao_Nao");
	}
	
}