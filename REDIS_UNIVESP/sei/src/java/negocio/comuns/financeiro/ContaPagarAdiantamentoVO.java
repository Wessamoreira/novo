package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;

/**
 * Reponsável por manter os dados dos adiantamentos utilizados em uma determinada conta a pagar
 * 
 * @see SuperVO
 */
public class ContaPagarAdiantamentoVO extends SuperVO {

	private Integer codigo;
	private Date dataUsoAdiantamento;
	private Double valorUtilizado;
	private Double percenutalContaPagarUtilizada;
	private ContaPagarVO contaPagar;
	private ContaPagarVO contaPagarUtilizada;
	private UsuarioVO responsavel;
	
	public static final long serialVersionUID = 1L;

	public ContaPagarAdiantamentoVO() {
		super();
	}

	public static void validarDados(ContaPagarAdiantamentoVO obj) {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getValorUtilizado().doubleValue() <= 0) {
			throw new StreamSeiException("O campo VALOR UTILIZADO (Adiantamento Conta à Pagar) deve ser informado.");
		}
		if (obj.getContaPagarUtilizada().getCodigo().intValue() <= 0) {
			throw new StreamSeiException("Conta a pagar utilizada para esse adiantamento NÃO FOI INFORMADA (Adiantamento Conta à Pagar).");
		}
		if (obj.getContaPagar().getCodigo().intValue() <= 0) {
			throw new StreamSeiException("Conta a pagar NÃO FOI INFORMADA (Adiantamento Conta à Pagar).");
		}
		if (!obj.getContaPagarUtilizada().getQuitada()) {
			throw new StreamSeiException("Conta a pagar utilizada deve estar BAIXADA (Adiantamento Conta à Pagar). Não é possível utilizar uma conta a pagar que ainda não quitada.");
		}
		
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

	public Date getDataUsoAdiantamento() {
		if (dataUsoAdiantamento == null) {
			dataUsoAdiantamento = new Date();
		}
		return dataUsoAdiantamento;
	}

	public void setDataUsoAdiantamento(Date dataUsoAdiantamento) {
		this.dataUsoAdiantamento = dataUsoAdiantamento;
	}

	public Double getValorUtilizado() {
		if (valorUtilizado == null) {
			valorUtilizado = 0.0;
		}
		return valorUtilizado;
	}

	public void setValorUtilizado(Double valorUtilizado) {
		this.valorUtilizado = valorUtilizado;
	}

	public Double getPercenutalContaPagarUtilizada() {
		if (percenutalContaPagarUtilizada == null) {
			percenutalContaPagarUtilizada = 0.0;
		}
		return percenutalContaPagarUtilizada;
	}

	public void setPercenutalContaPagarUtilizada(Double percenutalContaPagarUtilizada) {
		this.percenutalContaPagarUtilizada = percenutalContaPagarUtilizada;
	}

	public ContaPagarVO getContaPagarUtilizada() {
		if (contaPagarUtilizada == null) {
			contaPagarUtilizada = new ContaPagarVO();
		}
		return contaPagarUtilizada;
	}

	public void setContaPagarUtilizada(ContaPagarVO contaPagarUtilizada) {
		this.contaPagarUtilizada = contaPagarUtilizada;
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

	public ContaPagarVO getContaPagar() {
		if (contaPagar == null) {
			contaPagar = new ContaPagarVO();
		}
		return contaPagar;
	}

	public void setContaPagar(ContaPagarVO contaPagar) {
		this.contaPagar = contaPagar;
	}	
}
