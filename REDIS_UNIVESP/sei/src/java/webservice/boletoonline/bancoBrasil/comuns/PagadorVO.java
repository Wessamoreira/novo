package webservice.boletoonline.bancoBrasil.comuns;

public class PagadorVO {

	private Integer tipoInscricao;
	private String numeroInscricao;
	private String nome;
	private String endereco;
	private String cep;
	private String cidade;
	private String bairro;
	private String uf;
	private String telefone;
	
	
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
	public String getEndereco() {
		if(endereco ==null) {
			endereco ="";
		}
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
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
	public String getCidade() {
		if(cidade ==null) {
			cidade ="";
		}
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getBairro() {
		if(bairro == null) {
			bairro ="";
		}
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
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
	public String getTelefone() {
		if(telefone ==null) {
			telefone ="";
		}
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
		

}
