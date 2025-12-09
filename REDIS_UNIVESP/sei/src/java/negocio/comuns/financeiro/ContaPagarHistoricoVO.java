package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class ContaPagarHistoricoVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9002656628426878256L;
	private Integer codigo;
	private Integer contaPagar;
	private Integer controleCobrancaPagar;
	private Long nossoNumero;
	private Double valorPagamento;	
	private Integer arquivo;
	private Date data;
	private UsuarioVO responsavel;
	private String motivo;
	
	

	public Integer getControleCobrancaPagar() {
		if (controleCobrancaPagar == null) {
			controleCobrancaPagar = 0;
		}
		return controleCobrancaPagar;
	}

	public void setControleCobrancaPagar(Integer controleCobrancaPagar) {
		this.controleCobrancaPagar = controleCobrancaPagar;
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

	public Integer getContaPagar() {
		if (contaPagar == null) {
			contaPagar = 0;
		}
		return contaPagar;
	}

	public void setContaPagar(Integer contaPagar) {
		this.contaPagar = contaPagar;
	}

	public Long getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = 0L;
		}
		return nossoNumero;
	}

	public void setNossoNumero(Long nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public Double getValorPagamento() {
		if (valorPagamento == null) {
			valorPagamento = 0.0;
		}
		return valorPagamento;
	}

	public void setValorPagamento(Double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}	

	public Integer getArquivo() {
		if (arquivo == null) {
			arquivo = 0;
		}
		return arquivo;
	}

	public void setArquivo(Integer arquivo) {
		this.arquivo = arquivo;
	}

	public String getData_Apresentar() {
		return Uteis.getData(getData());
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
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

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}
