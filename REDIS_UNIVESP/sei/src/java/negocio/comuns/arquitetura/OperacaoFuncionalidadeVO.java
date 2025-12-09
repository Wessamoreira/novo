package negocio.comuns.arquitetura;

import java.util.Date;

import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Wellington - 25 de set de 2015
 *
 */
public class OperacaoFuncionalidadeVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private OrigemOperacaoFuncionalidadeEnum origem;
	private String codigoOrigem;
	private OperacaoFuncionalidadeEnum operacao;
	private UsuarioVO responsavel;
	private Date data;
	private String observacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public OrigemOperacaoFuncionalidadeEnum getOrigem() {
		return origem;
	}

	public void setOrigem(OrigemOperacaoFuncionalidadeEnum origem) {
		this.origem = origem;
	}

	public String getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = "";
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(String codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public OperacaoFuncionalidadeEnum getOperacao() {
		return operacao;
	}

	public void setOperacao(OperacaoFuncionalidadeEnum operacao) {
		this.operacao = operacao;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}
	

	public String getDataApresentar() {
		return Uteis.getDataAplicandoFormatacao(getData(), "dd/MM/yyyy HH:mm");
	}
	
	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
