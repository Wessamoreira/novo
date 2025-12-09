package webservice.boletoonline.bancoBrasil.comuns;

public class BeneficiarioVO {
	
	private Integer tipoInscricao;
	private String numeroInscricao;
	private String nome;
	
	//usado no retorno registro
	private String agencia;
	private String contaCorrente;
	private String tipoEndereco;
	private String logradouro;
	private String bairro;
	private String cidade;
	private String codigoCidade;
	private String uf;
	private String cep;
	private String indicadorComprovacao;
	
	
	
	public Integer getTipoInscricao() {
		if(tipoInscricao ==null) {
			tipoInscricao =0;
		}
		return tipoInscricao;
	}
	public void setTipoInscricao(Integer tipoInscricao) {
		this.tipoInscricao = tipoInscricao;
	}
	public String getNumeroInscricao() {
		if(numeroInscricao ==null) {
			numeroInscricao="";
		}
		return numeroInscricao;
	}
	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}
	public String getNome() {
		if(nome ==null) {
			nome ="";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getAgencia() {
		if(agencia ==null) {
			agencia ="";
		}
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getContaCorrente() {
		if(contaCorrente ==null) {
			contaCorrente ="";
		}
		return contaCorrente;
	}
	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}
	public String getTipoEndereco() {
		if(tipoEndereco ==null) {
			tipoEndereco ="";
		}
		return tipoEndereco;
	}
	public void setTipoEndereco(String tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}
	public String getLogradouro() {
		if(logradouro ==null) {
			logradouro ="";
		}
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getBairro() {
		if(bairro ==null) {
			bairro ="";
		}
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		if(cidade ==null) {
			cidade ="";
		}
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getCodigoCidade() {
		if(codigoCidade ==null) {
			codigoCidade ="";
		}
		return codigoCidade;
	}
	public void setCodigoCidade(String codigoCidade) {
		this.codigoCidade = codigoCidade;
	}
	public String getUf() {
		if(uf ==null) {
			uf ="";
		}
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCep() {
		if(cep ==null) {
			cep ="";
		}
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getIndicadorComprovacao() {
		if(indicadorComprovacao ==null) {
			indicadorComprovacao ="";
		}
		return indicadorComprovacao;
	}
	public void setIndicadorComprovacao(String indicadorComprovacao) {
		this.indicadorComprovacao = indicadorComprovacao;
	}
	
}
