package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class MapaPendenciasControleCobrancaVO extends SuperVO {

	private Integer codigo;
	private MatriculaVO matricula;
	private ContaReceberVO contaReceber;
	private Double valorDiferenca;
	private Boolean selecionado;
	private Date dataPagamento;
	private Date dataProcessamento;
	private Double juro;
	private Double multa;
	private Date dataRecebimento;
	public static final long serialVersionUID = 1L;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}

	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	/**
	 * @return the valorDiferenca
	 */
	public Double getValorDiferenca() {
		if (valorDiferenca == null) {
			valorDiferenca = 0.0;
		}
		return valorDiferenca;
	}

	/**
	 * @param valorDiferenca
	 *            the valorDiferenca to set
	 */
	public void setValorDiferenca(Double valorDiferenca) {
		this.valorDiferenca = valorDiferenca;
	}

	public Date getDataPagamento() {
		if (dataPagamento == null) {
			dataPagamento = new Date();
		}
		return dataPagamento;
	}

	public String getDataPagamento_Apresentar() {
		if (dataPagamento == null) {
			return "";
		}
		return (Uteis.getData(dataPagamento));
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the dataProcessamento
	 */
	public Date getDataProcessamento() {
		if (dataProcessamento == null) {
			dataProcessamento = new Date();
		}
		return dataProcessamento;
	}

	/**
	 * @param dataProcessamento
	 *            the dataProcessamento to set
	 */
	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public Date getDataRecebimento() {
		if (dataRecebimento == null) {
			dataRecebimento = new Date();
		}
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}
}
