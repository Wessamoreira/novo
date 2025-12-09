package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoPeriodoAquisitivoEnum;

public class PeriodoAquisitivoFeriasVO extends SuperVO {

	private static final long serialVersionUID = 3645909146889077560L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Date inicioPeriodo;
	private Date finalPeriodo;
	private SituacaoPeriodoAquisitivoEnum situacao;
	private String informacoesAdicionais;

	private Integer qtdFalta;
	
	// Transiente
	private Boolean itemEmEdicao;

	public enum EnumCampoConsultaPeriodoAquisitivoFuncionario {
		FUNCIONARIO, MATRICULA_CARGO, MATRICULA_FUNCIONARIO, CARGO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public FuncionarioCargoVO getFuncionarioCargo() {
		if (funcionarioCargo == null) {
			funcionarioCargo = new FuncionarioCargoVO();
		}
		return funcionarioCargo;
	}

	public void setFuncionarioCargo(FuncionarioCargoVO funcionarioCargo) {
		this.funcionarioCargo = funcionarioCargo;
	}

	public Date getInicioPeriodo() {
		if (inicioPeriodo == null)
			inicioPeriodo = new Date();
		return inicioPeriodo;
	}

	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}

	public Date getFinalPeriodo() {
		if (finalPeriodo == null)
			finalPeriodo = new Date();
		return finalPeriodo;
	}

	public void setFinalPeriodo(Date finalPeriodo) {
		this.finalPeriodo = finalPeriodo;
	}

	public SituacaoPeriodoAquisitivoEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPeriodoAquisitivoEnum situacao) {
		this.situacao = situacao;
	}

	public String getInformacoesAdicionais() {
		if (informacoesAdicionais == null) {
			informacoesAdicionais = "";
		}
		return informacoesAdicionais;
	}

	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null)
			itemEmEdicao = false;
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getQtdFalta() {
		if (qtdFalta == null)
			qtdFalta = 0;
		return qtdFalta;
	}

	public void setQtdFalta(Integer qtdFalta) {
		this.qtdFalta = qtdFalta;
	}
}