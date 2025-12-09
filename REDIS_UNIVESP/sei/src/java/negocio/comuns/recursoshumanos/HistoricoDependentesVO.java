package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsavel por manter os dados da entidade HistoricoDependentes. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class HistoricoDependentesVO extends SuperVO {

	private static final long serialVersionUID = 982419729195326541L;

	private Integer codigo;
	private FuncionarioVO funcionarioVO;
	private Date dataMudanca;
	private Integer numeroDependentesIRRF;
	private Integer numeroDependentesSalarioFamilia;
	
	public enum EnumCampoConsultaHistoricoDependentes {
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

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public Date getDataMudanca() {
		if (dataMudanca == null) {
			dataMudanca = new Date();
		}
		return dataMudanca;
	}

	public void setDataMudanca(Date dataMudanca) {
		this.dataMudanca = dataMudanca;
	}

	public Integer getNumeroDependentesIRRF() {
		if (numeroDependentesIRRF == null) {
			numeroDependentesIRRF = 0;
		}
		return numeroDependentesIRRF;
	}

	public void setNumeroDependentesIRRF(Integer numeroDependentesIRRF) {
		this.numeroDependentesIRRF = numeroDependentesIRRF;
	}

	public Integer getNumeroDependentesSalarioFamilia() {
		if (numeroDependentesSalarioFamilia == null) {
			numeroDependentesSalarioFamilia = 0;
		}
		return numeroDependentesSalarioFamilia;
	}

	public void setNumeroDependentesSalarioFamilia(Integer numeroDependentesSalarioFamilia) {
		this.numeroDependentesSalarioFamilia = numeroDependentesSalarioFamilia;
	}

}
