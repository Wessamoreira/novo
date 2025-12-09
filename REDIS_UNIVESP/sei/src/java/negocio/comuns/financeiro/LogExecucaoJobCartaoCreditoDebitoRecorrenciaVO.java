package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;


public class LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO;
	private Integer contaReceber;
	private String nossoNumero;
	private String parcela;
	private Integer matriculaPeriodo;
	private String tipoOrigem;
	private Date dataVencimento;
	private Date data;
	private Boolean execucaoManual;
	private UsuarioVO responsavelVO;
	private String observacao;
	private Boolean erro;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CartaoCreditoDebitoRecorrenciaPessoaVO getCartaoCreditoDebitoRecorrenciaPessoaVO() {
		if (cartaoCreditoDebitoRecorrenciaPessoaVO == null) {
			cartaoCreditoDebitoRecorrenciaPessoaVO = new CartaoCreditoDebitoRecorrenciaPessoaVO();
		}
		return cartaoCreditoDebitoRecorrenciaPessoaVO;
	}
	public void setCartaoCreditoDebitoRecorrenciaPessoaVO(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO) {
		this.cartaoCreditoDebitoRecorrenciaPessoaVO = cartaoCreditoDebitoRecorrenciaPessoaVO;
	}
	public Integer getContaReceber() {
		if (contaReceber == null) {
			contaReceber = 0;
		}
		return contaReceber;
	}
	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
	}
	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}
	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	public Integer getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = 0;
		}
		return matriculaPeriodo;
	}
	public void setMatriculaPeriodo(Integer matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}
	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}
	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
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
	public Boolean getExecucaoManual() {
		if (execucaoManual == null) {
			execucaoManual = false;
		}
		return execucaoManual;
	}
	public void setExecucaoManual(Boolean execucaoManual) {
		this.execucaoManual = execucaoManual;
	}
	public UsuarioVO getResponsavelVO() {
		if (responsavelVO == null) {
			responsavelVO = new UsuarioVO();
		}
		return responsavelVO;
	}
	public void setResponsavelVO(UsuarioVO responsavelVO) {
		this.responsavelVO = responsavelVO;
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
	public Boolean getErro() {
		if (erro == null) {
			erro = false;
		}
		return erro;
	}
	public void setErro(Boolean erro) {
		this.erro = erro;
	}
	
}
