package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessamentoIntegracaoFinanceiroVO {
	private Integer codigo;
	private Date dataProcessamento;
	private String nomeArquivo;
	private String caminhoArquivoIntegracaoFinanceiro;
	private List<ProcessamentoIntegracaoFinanceiraDetalheVO> integracaoFinanceiroContaReceberVOs;

	public ProcessamentoIntegracaoFinanceiroVO() {

	}

	public Date getdataProcessamento() {
		if (dataProcessamento == null) {
			dataProcessamento = new Date();
		}
		return dataProcessamento;
	}

	public void setdataProcessamento(Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	public String getnomeArquivo() {
		return nomeArquivo;
	}

	public void setnomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getCaminhoArquivoIntegracaoFinanceiro() {
		return caminhoArquivoIntegracaoFinanceiro;

	}

	public void setCaminhoArquivoIntegracaoFinanceiro(String caminhoArquivoIntegracaoFinanceiro) {
		this.caminhoArquivoIntegracaoFinanceiro = caminhoArquivoIntegracaoFinanceiro;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<ProcessamentoIntegracaoFinanceiraDetalheVO> getIntegracaoFinanceiroContaReceberVOs() {
		if (integracaoFinanceiroContaReceberVOs == null) {
			integracaoFinanceiroContaReceberVOs = new ArrayList<ProcessamentoIntegracaoFinanceiraDetalheVO>(0);
		}
		return integracaoFinanceiroContaReceberVOs;
	}

	public void setIntegracaoFinanceiroContaReceberVOs(List<ProcessamentoIntegracaoFinanceiraDetalheVO> integracaoFinanceiroContaReceberVOs) {
		this.integracaoFinanceiroContaReceberVOs = integracaoFinanceiroContaReceberVOs;
	}
}
