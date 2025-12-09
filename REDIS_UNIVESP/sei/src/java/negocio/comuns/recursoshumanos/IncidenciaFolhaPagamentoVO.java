package negocio.comuns.recursoshumanos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.recursoshumanos.enumeradores.LocalIncidenciaEnum;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * Reponsavel por manter os dados da entidade IncidenciaFolhaPagamento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os metodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class IncidenciaFolhaPagamentoVO extends SuperVO {

	private static final long serialVersionUID = -3742958447890982214L;
	
	private Integer codigo;
	private String descricao;
	private Boolean incidenciaImposto;
	private ImpostoVO imposto;
	private LocalIncidenciaEnum localIncidencia;
	private FormulaFolhaPagamentoVO formula;
	private AtivoInativoEnum situacao;
	
	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public String getDescricao() {
		if (descricao == null)
			descricao = "";
		return descricao;
	}
	public Boolean getIncidenciaImposto() {
		if (incidenciaImposto == null)
			incidenciaImposto = false;
		return incidenciaImposto;
	}
	public ImpostoVO getImposto() {
		if (imposto == null)
			imposto = new ImpostoVO();
		return imposto;
	}
	public FormulaFolhaPagamentoVO getFormula() {
		if (formula == null)
			formula = new FormulaFolhaPagamentoVO();
		return formula;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setIncidenciaImposto(Boolean incidenciaImposto) {
		this.incidenciaImposto = incidenciaImposto;
		this.setImposto(null);
	}
	public void setImposto(ImpostoVO imposto) {
		this.imposto = imposto;
	}
	public void setFormula(FormulaFolhaPagamentoVO formula) {
		this.formula = formula;
	}
	public LocalIncidenciaEnum getLocalIncidencia() {
		if(localIncidencia == null) {
			localIncidencia = LocalIncidenciaEnum.FOLHA_NORMAL;
		}
		return localIncidencia;
	}
	public AtivoInativoEnum getSituacao() {
		if(situacao == null) {
			situacao = AtivoInativoEnum.INATIVO;
		}
		return situacao;
	}
	public void setLocalIncidencia(LocalIncidenciaEnum localIncidencia) {
		this.localIncidencia = localIncidencia;
	}
	public void setSituacao(AtivoInativoEnum situacao) {
		this.situacao = situacao;
	}
	
	public String getSituacao_Apresentar() {
		if(situacao == null) {
			return "";
		}
		if(situacao.equals(AtivoInativoEnum.ATIVO))
			return UteisJSF.internacionalizar("prt_TextoPadrao_Ativo");
		else
			return UteisJSF.internacionalizar("prt_TextoPadrao_Inativo");
	}
	
	public String getLocalIncidencia_Apresentar() {
		if(localIncidencia == null) {
			return "";
		} else if(localIncidencia.equals(LocalIncidenciaEnum.FERIAS)) {
			return UteisJSF.internacionalizar("prt_LocalIncidenciaEnum_FERIAS"); 
		}else if(localIncidencia.equals(LocalIncidenciaEnum.DECIMO_TERCEIRO)) {
			return UteisJSF.internacionalizar("prt_LocalIncidenciaEnum_DECIMO_TERCEIRO");
		}else {
			return UteisJSF.internacionalizar("prt_LocalIncidenciaEnum_FOLHA_NORMAL");
		}
	}
	
	public String getIncidenciaImposto_Apresentar() {
		if(getIncidenciaImposto())
			return UteisJSF.internacionalizar("prt_TextoPadrao_Sim");
		else
			return UteisJSF.internacionalizar("prt_TextoPadrao_Nao");
	}
}