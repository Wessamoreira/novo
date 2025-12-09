package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO {

	private String matricula;
	private String nomeCurso;
	private String nomeTurma;
	private String nomeAluno;
	private String nomeParceiro;
	private String cpf;
	private String cnpj;
	private String endereco;
	private String isento;
	private String convenio;
	private String bolsista;
	private String cep;
	private String cidade;
	private String estado;
	private String parcela;
	private String origem;
	private Double valorPagamento;
	private Double valorRecebido;
	private Date dataVencimento;
	private Date dataPagamento;
	private String banco;
	private Integer quantidadeGeralParcelas;
	private Double valorTotalRecebido;
	private Double subTotalRecebido;
	private Integer quantidadeParcelas;
	private List<ContasRecebimentoRelVO> contasRecebimentoRelVOs;
	private List<ContaReceberVO> listaResumo;
	private String responsavelFinanceiro;
	private String cpfResponsavelFinanceiro;
	private String legendaFormaPagamento;
	private String numero;
	private String digito;

	public JRDataSource getListaResumoDS() {
		return new JRBeanArrayDataSource(getListaResumo().toArray());
	}

	public JRDataSource getContasRecebimento() {
		return new JRBeanArrayDataSource(getContasRecebimentoRelVOs().toArray());
	}

	public void setContasRecebimentoRelVOs(List<ContasRecebimentoRelVO> contasRecebimentoRelVOs) {
		this.contasRecebimentoRelVOs = contasRecebimentoRelVOs;
	}

	public List<ContasRecebimentoRelVO> getContasRecebimentoRelVOs() {
		if (contasRecebimentoRelVOs == null) {
			contasRecebimentoRelVOs = new ArrayList<ContasRecebimentoRelVO>(0);
		}
		return contasRecebimentoRelVOs;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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

	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		// if (dataPagamento == null) {
		// dataPagamento = new Date();
		// }
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getBanco() {
		if (banco == null) {
			banco = "";
		}
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getNomeTurma() {
		if (nomeTurma == null) {
			nomeTurma = "";
		}
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public Integer getQuantidadeGeralParcelas() {
		return quantidadeGeralParcelas;
	}

	public void setQuantidadeGeralParcelas(Integer quantidadeGeralParcelas) {
		this.quantidadeGeralParcelas = quantidadeGeralParcelas;
	}

	public Double getValorTotalRecebido() {
		return valorTotalRecebido;
	}

	public void setValorTotalRecebido(Double valorTotalRecebido) {
		this.valorTotalRecebido = valorTotalRecebido;
	}

	public Double getSubTotalRecebido() {
		if (subTotalRecebido == null) {
			subTotalRecebido = 0.0;
		}
		return subTotalRecebido;
	}

	public void setSubTotalRecebido(Double subTotalRecebido) {
		this.subTotalRecebido = subTotalRecebido;
	}

	public Integer getQuantidadeParcelas() {
		if (quantidadeParcelas == null) {
			quantidadeParcelas = 0;
		}
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public List<ContaReceberVO> getListaResumo() {
		if (listaResumo == null) {
			listaResumo = new ArrayList<ContaReceberVO>(0);
		}
		return listaResumo;
	}

	public void setListaResumo(List<ContaReceberVO> listaResumo) {
		this.listaResumo = listaResumo;
	}

	public String getIsento() {
		if (isento == null) {
			isento = "Não";
		}
		return isento;
	}

	public void setIsento(String isento) {
		this.isento = isento;
	}

	public String getConvenio() {
		if (convenio == null) {
			convenio = "Não";
		}
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getBolsista() {
		if (bolsista == null) {
			bolsista = "Não";
		}
		return bolsista;
	}

	public void setBolsista(String bolsista) {
		this.bolsista = bolsista;
	}

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		if (estado == null) {
			estado = "";
		}
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getOrdenacao() {
		return getNomeCurso() + getNomeTurma() + getNomeAluno() + Uteis.getData(getDataVencimento());
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

	public String getOrigem() {
		if (origem == null) {
			origem = "";
		}
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = "";
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(String responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public String getCpfResponsavelFinanceiro() {
		if (cpfResponsavelFinanceiro == null) {
			cpfResponsavelFinanceiro = "";
		}
		return cpfResponsavelFinanceiro;
	}

	public void setCpfResponsavelFinanceiro(String cpfResponsavelFinanceiro) {
		this.cpfResponsavelFinanceiro = cpfResponsavelFinanceiro;
	}

	public String getLegendaFormaPagamento() {
		if (legendaFormaPagamento == null) {
			legendaFormaPagamento = "";
		}
		return legendaFormaPagamento;
	}

	public void setLegendaFormaPagamento(String legendaFormaPagamento) {
		this.legendaFormaPagamento = legendaFormaPagamento;
	}

	public String getNomeParceiro() {
		if (nomeParceiro == null) {
			nomeParceiro = "";
		}
		return nomeParceiro;
	}

	public void setNomeParceiro(String nomeParceiro) {
		this.nomeParceiro = nomeParceiro;
	}

	public String getCnpj() {
		if (cnpj == null) {
			cnpj = "";
		}
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDigito() {
		if (digito == null) {
			digito = "";
		}
		return digito;
	}

	public void setDigito(String digito) {
		this.digito = digito;
	}
	
	

}
