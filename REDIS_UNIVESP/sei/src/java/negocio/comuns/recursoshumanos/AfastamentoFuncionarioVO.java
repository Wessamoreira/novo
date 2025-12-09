package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.MotivoAfastamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoAfastamentoEnum;

public class AfastamentoFuncionarioVO extends SuperVO {

	private static final long serialVersionUID = -6626163883737977139L;

	private Integer codigo;
	private FuncionarioCargoVO funcionarioCargo;
	private TipoAfastamentoEnum tipoAfastamento;
	private MotivoAfastamentoEnum motivoAfastamento;
	private Date dataInicio;
	private Date dataFinal;
	private ArquivoVO arquivo;
	private Integer quantidadeDiasAfastado;

	public enum EnumCampoConsultaAfastamentoFuncionario {
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

	public TipoAfastamentoEnum getTipoAfastamento() {
		return tipoAfastamento;
	}

	public void setTipoAfastamento(TipoAfastamentoEnum tipoAfastamento) {
		this.tipoAfastamento = tipoAfastamento;
	}

	public MotivoAfastamentoEnum getMotivoAfastamento() {
		return motivoAfastamento;
	}

	public void setMotivoAfastamento(MotivoAfastamentoEnum motivoAfastamento) {
		this.motivoAfastamento = motivoAfastamento;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		if (dataFinal == null) {
			dataFinal = new Date();
		}
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public ArquivoVO getArquivo() {
		if (arquivo == null) {
			arquivo = new ArquivoVO();
		}
		return arquivo;
	}

	public void setArquivo(ArquivoVO arquivo) {
		this.arquivo = arquivo;
	}

	public Integer getQuantidadeDiasAfastado() {
		if (quantidadeDiasAfastado == null) {
			quantidadeDiasAfastado = 0;
		}
		return quantidadeDiasAfastado;
	}

	public void setQuantidadeDiasAfastado(Integer quantidadeDiasAfastado) {
		this.quantidadeDiasAfastado = quantidadeDiasAfastado;
	}	
}
