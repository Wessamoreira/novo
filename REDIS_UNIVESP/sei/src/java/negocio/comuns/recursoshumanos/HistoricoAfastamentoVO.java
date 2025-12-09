package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoAfastamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoAfastamentoEnum;

/**
 * Reponsavel por manter os dados da entidade HistoricoAfastamento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memoria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class HistoricoAfastamentoVO extends SuperVO {

	private static final long serialVersionUID = 5909173651947871477L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private Date dataInicio;
	private Date dataFinal;
	private Date dataRequerimento;
	private Integer quantidade;
	private MotivoAfastamentoEnum motivoAfastamento;
	private TipoAfastamentoEnum tipoAfastamento;
	private Boolean processado;

	public enum EnumCampoConsultaHistoricoAfastamento {
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

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Date getDataRequerimento() {
		return dataRequerimento;
	}

	public void setDataRequerimento(Date dataRequerimento) {
		this.dataRequerimento = dataRequerimento;
	}

	public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public MotivoAfastamentoEnum getMotivoAfastamento() {
		return motivoAfastamento;
	}

	public void setMotivoAfastamento(MotivoAfastamentoEnum motivoAfastamento) {
		this.motivoAfastamento = motivoAfastamento;
	}

	public TipoAfastamentoEnum getTipoAfastamento() {
		return tipoAfastamento;
	}

	public void setTipoAfastamento(TipoAfastamentoEnum tipoAfastamento) {
		this.tipoAfastamento = tipoAfastamento;
	}

	public Boolean getProcessado() {
		if (processado == null) {
			processado = Boolean.TRUE;
		}
		return processado;
	}

	public void setProcessado(Boolean processado) {
		this.processado = processado;
	}

}
