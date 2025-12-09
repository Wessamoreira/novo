package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoFaltaEnum;

public class FaltasFuncionarioVO extends SuperVO {

	private static final long serialVersionUID = -7366085873637979482L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Date dataInicio;
	private TipoFaltaEnum tipoFalta;
	private String motivo;
	private Boolean integral;

	private Boolean debitado;
	
	// Transiente
	private Boolean itemEmEdicao;
	private Integer totalFaltasfuncionario;

	public enum EnumCampoConsultaFaltaFuncionario {
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

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public TipoFaltaEnum getTipoFalta() {
		if (tipoFalta == null) {
			tipoFalta = TipoFaltaEnum.JUSTIFICADA;
		}
		return tipoFalta;
	}

	public void setTipoFalta(TipoFaltaEnum tipoFalta) {
		this.tipoFalta = tipoFalta;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Boolean getIntegral() {
		if (integral == null) {
			integral = Boolean.TRUE;
		}
		return integral;
	}

	public void setIntegral(Boolean integral) {
		this.integral = integral;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = Boolean.FALSE;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}

	public Integer getTotalFaltasfuncionario() {
		if (totalFaltasfuncionario == null) {
			totalFaltasfuncionario = 0;
		}
		return totalFaltasfuncionario;
	}

	public void setTotalFaltasfuncionario(Integer totalFaltasfuncionario) {
		this.totalFaltasfuncionario = totalFaltasfuncionario;
	}

	public Boolean getDebitado() {
		if (debitado == null)
			debitado = false;
		return debitado;
	}

	public void setDebitado(Boolean debitado) {
		this.debitado = debitado;
	}
}