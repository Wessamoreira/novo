package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Reponsavel por manter os dados da entidade HistoricoMarcacaoFeriasColetiva.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class HistoricoMarcacaoFeriasColetivaVO extends SuperVO {

	private static final long serialVersionUID = 922283881518328286L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private MarcacaoFeriasVO marcacaoFerias;
	private MarcacaoFeriasColetivasVO marcacaoFeriasColetivas;
	private String cargo;
	private String situacao;
	private String formaContratacao;
	private String matriculaCargo;
	private String nomeFuncionario;
	private Date dataHistorico;
	
	private SituacaoMarcacaoFeriasEnum situacaoMarcacaoFerias;
	private Boolean lancadoAdiantamento;
	private Boolean lancadoReciboNoContraCheque;

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

	public MarcacaoFeriasVO getMarcacaoFerias() {
		if (marcacaoFerias == null) {
			marcacaoFerias = new MarcacaoFeriasVO();
		}
		return marcacaoFerias;
	}

	public void setMarcacaoFerias(MarcacaoFeriasVO marcacaoFerias) {
		this.marcacaoFerias = marcacaoFerias;
	}

	public MarcacaoFeriasColetivasVO getMarcacaoFeriasColetivas() {
		if (marcacaoFeriasColetivas == null) {
			marcacaoFeriasColetivas = new MarcacaoFeriasColetivasVO();
		}
		return marcacaoFeriasColetivas;
	}

	public void setMarcacaoFeriasColetivas(MarcacaoFeriasColetivasVO marcacaoFeriasColetivas) {
		this.marcacaoFeriasColetivas = marcacaoFeriasColetivas;
	}

	public String getCargo() {
		if (cargo == null) {
			cargo = "";
		}
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getFormaContratacao() {
		if (formaContratacao == null) {
			formaContratacao = "";
		}
		return formaContratacao;
	}

	public void setFormaContratacao(String formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public String getMatriculaCargo() {
		if (matriculaCargo == null) {
			matriculaCargo = "";
		}
		return matriculaCargo;
	}

	public void setMatriculaCargo(String matriculaCargo) {
		this.matriculaCargo = matriculaCargo;
	}

	public String getNomeFuncionario() {
		if (nomeFuncionario == null) {
			nomeFuncionario = "";
		}
		return nomeFuncionario;
	}

	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}

	public Date getDataHistorico() {
		if (dataHistorico == null) {
			dataHistorico = UteisData.getDataComMinutos(new Date());
		}
		return dataHistorico;
	}

	public void setDataHistorico(Date dataHistorico) {
		this.dataHistorico = dataHistorico;
	}

	public SituacaoMarcacaoFeriasEnum getSituacaoMarcacaoFerias() {
		if (situacaoMarcacaoFerias == null)
			situacaoMarcacaoFerias = SituacaoMarcacaoFeriasEnum.MARCADA;
		return situacaoMarcacaoFerias;
	}

	public void setSituacaoMarcacaoFerias(SituacaoMarcacaoFeriasEnum situacaoMarcacaoFerias) {
		this.situacaoMarcacaoFerias = situacaoMarcacaoFerias;
	}

	public Boolean getLancadoAdiantamento() {
		if (lancadoAdiantamento == null)
			lancadoAdiantamento = false;
		return lancadoAdiantamento;
	}

	public void setLancadoAdiantamento(Boolean lancadoAdiantamento) {
		this.lancadoAdiantamento = lancadoAdiantamento;
	}

	public Boolean getLancadoReciboNoContraCheque() {
		if (lancadoReciboNoContraCheque == null)
			lancadoReciboNoContraCheque = false;
		return lancadoReciboNoContraCheque;
	}

	public void setLancadoReciboNoContraCheque(Boolean lancadoReciboNoContraCheque) {
		this.lancadoReciboNoContraCheque = lancadoReciboNoContraCheque;
	}
}