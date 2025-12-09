package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoMudancaCargoEnum;

/**
 * Reponsavel por manter os dados da entidade HistoricoSituacao. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class HistoricoSituacaoVO extends SuperVO {

	private static final long serialVersionUID = 8067858669317158839L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Date dataMudanca;
	private MotivoMudancaCargoEnum motivoMudanca;
	private SituacaoFuncionarioEnum situacao;

	// Transient
	private Boolean gerarHistorico;

	public enum EnumCampoConsultaHistoricoSituacao {
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

	public MotivoMudancaCargoEnum getMotivoMudanca() {
		return motivoMudanca;
	}

	public void setMotivoMudanca(MotivoMudancaCargoEnum motivoMudanca) {
		this.motivoMudanca = motivoMudanca;
	}

	public SituacaoFuncionarioEnum getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoFuncionarioEnum situacao) {
		this.situacao = situacao;
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
