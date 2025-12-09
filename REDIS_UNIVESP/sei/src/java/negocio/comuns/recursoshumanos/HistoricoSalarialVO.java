package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCargoEnum;

/**
 * Reponsavel por manter os dados da entidade HistoricoSalarial. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class HistoricoSalarialVO extends SuperVO {

	private static final long serialVersionUID = -7739466627740488803L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Date dataMudanca;
	private Integer jornada;
	private BigDecimal salario;
	private MotivoMudancaCargoEnum motivoMudanca;
	private BigDecimal percentualVariacaoSalarial;
	private BigDecimal salarioHora;
	
	//Transient
	private Boolean gerarHistorico;

	public enum EnumCampoConsultaHistoricoSalarial {
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

	public Date getDataMudanca() {
		return dataMudanca;
	}

	public void setDataMudanca(Date dataMudanca) {
		this.dataMudanca = dataMudanca;
	}

	public Integer getJornada() {
		if (jornada == null) {
			jornada = 0;
		}
		return jornada;
	}

	public void setJornada(Integer jornada) {
		this.jornada = jornada;
	}

	public BigDecimal getSalario() {
		if (salario == null) {
			salario = BigDecimal.ZERO;
		}
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public MotivoMudancaCargoEnum getMotivoMudanca() {
		return motivoMudanca;
	}

	public void setMotivoMudanca(MotivoMudancaCargoEnum motivoMudanca) {
		this.motivoMudanca = motivoMudanca;
	}

	public BigDecimal getPercentualVariacaoSalarial() {
		if (percentualVariacaoSalarial == null) {
			percentualVariacaoSalarial = BigDecimal.ZERO;
		}
		return percentualVariacaoSalarial;
	}

	public void setPercentualVariacaoSalarial(BigDecimal percentualVariacaoSalarial) {
		this.percentualVariacaoSalarial = percentualVariacaoSalarial;
	}

	public BigDecimal getSalarioHora() {
		if (salarioHora == null) {
			salarioHora = BigDecimal.ZERO;
		}
		return salarioHora;
	}

	public void setSalarioHora(BigDecimal salarioHora) {
		this.salarioHora = salarioHora;
	}

	public Boolean getGerarHistorico() {
		if (gerarHistorico == null) {
			gerarHistorico = Boolean.FALSE;
		}
		return gerarHistorico;
	}

	public void setGerarHistorico(Boolean gerarHistorico) {
		this.gerarHistorico = gerarHistorico;
	}
}
