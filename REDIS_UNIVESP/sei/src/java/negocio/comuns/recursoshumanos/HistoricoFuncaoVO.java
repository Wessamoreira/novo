package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoContratacaoComissionadoEnum;

/**
 * Reponsavel por manter os dados da entidade HistoricoFuncao. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class HistoricoFuncaoVO extends SuperVO {

	private static final long serialVersionUID = -8522424817800173984L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Date dataMudanca;
	private TipoContratacaoComissionadoEnum motivoMudanca;
	private CargoVO cargo;
	private NivelSalarialVO nivelSalarial;
	private FaixaSalarialVO faixaSalarial;
	
	//Transient
	private Boolean gerarHistorico;

	public enum EnumCampoConsultaHistoricoFuncao {
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

	public TipoContratacaoComissionadoEnum getMotivoMudanca() {
		return motivoMudanca;
	}

	public void setMotivoMudanca(TipoContratacaoComissionadoEnum motivoMudanca) {
		this.motivoMudanca = motivoMudanca;
	}

	public CargoVO getCargo() {
		if (cargo == null) {
			cargo = new CargoVO();
		}
		return cargo;
	}

	public void setCargo(CargoVO cargo) {
		this.cargo = cargo;
	}

	public NivelSalarialVO getNivelSalarial() {
		if (nivelSalarial == null) {
			nivelSalarial = new NivelSalarialVO();
		}
		return nivelSalarial;
	}

	public void setNivelSalarial(NivelSalarialVO nivelSalarial) {
		this.nivelSalarial = nivelSalarial;
	}

	public FaixaSalarialVO getFaixaSalarial() {
		if (faixaSalarial == null) {
			faixaSalarial = new FaixaSalarialVO();
		}
		return faixaSalarial;
	}

	public void setFaixaSalarial(FaixaSalarialVO faixaSalarial) {
		this.faixaSalarial = faixaSalarial;
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
