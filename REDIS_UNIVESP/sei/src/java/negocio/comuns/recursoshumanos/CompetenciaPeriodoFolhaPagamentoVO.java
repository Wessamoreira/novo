package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsável por manter os dados da entidade CompetenciaFolhaPagamento. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CompetenciaPeriodoFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = 8863379005809287487L;

	private CompetenciaFolhaPagamentoVO competenciaFolhaPagamento;
	private Integer codigo;
	private String descricao;
	private Integer periodo;
	private Boolean itemEmEdicao;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null)
			descricao = "";
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getPeriodo() {
		if (periodo == null)
			periodo = 0;
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	public CompetenciaFolhaPagamentoVO getCompetenciaFolhaPagamento() {
		if (competenciaFolhaPagamento == null)
			competenciaFolhaPagamento = new CompetenciaFolhaPagamentoVO();
		return competenciaFolhaPagamento;
	}

	public void setCompetenciaFolhaPagamento(CompetenciaFolhaPagamentoVO competenciaFolhaPagamento) {
		this.competenciaFolhaPagamento = competenciaFolhaPagamento;
	}

	public static void validarDados(CompetenciaPeriodoFolhaPagamentoVO obj) throws ConsistirException {

		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getDescricao() == null) || (obj.getDescricao().trim().isEmpty())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CompetenciaFolhaPagamento_descricao"));
		}
		if (obj.getPeriodo() == null || obj.getPeriodo() <= 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_CompetenciaFolhaPagamento_periodo"));
		}
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null)
			itemEmEdicao = false;
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public String getPeriodoApresentacao() {
		return getPeriodo() + " - " + getDescricao();
	}

}