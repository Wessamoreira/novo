package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class MapaPendenciasControleCobrancaPagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4099015368952738836L;
	private Integer codigo;
	private MatriculaVO matriculaVO;
	private ContaPagarVO contaPagarVO;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;
	private Double valorDiferenca;
	private Boolean selecionado;
	private Date dataPagamento;
	private Date dataProcessamento;
	private Double juro;
	private Double multa;

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		if (controleCobrancaPagarVO == null) {
			controleCobrancaPagarVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	public Double getValorDiferenca() {
		if (valorDiferenca == null) {
			valorDiferenca = 0.0;
		}
		return valorDiferenca;
	}

	public void setValorDiferenca(Double valorDiferenca) {
		this.valorDiferenca = valorDiferenca;
	}

	public Boolean getSelecionado() {
		if(selecionado == null){
			selecionado = false;
		}
			
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public String getDataPagamento_Apresentar() {
		return Uteis.getData(getDataPagamento());
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataProcessamento() {
		return dataProcessamento;
	}

	public void setDataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
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
