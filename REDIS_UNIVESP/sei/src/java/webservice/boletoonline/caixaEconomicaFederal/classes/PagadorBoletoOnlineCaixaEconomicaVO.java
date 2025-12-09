package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class PagadorBoletoOnlineCaixaEconomicaVO {
	
	@XStreamAlias("CPF")
	private String cpf ;
	
	@XStreamAlias("NOME")
	private String nome;
	
	@XStreamAlias("CNPJ")
	private String cnpj ;
	
	@XStreamAlias("RAZAO_SOCIAL")
	private String razaoSocial;
	
	
	@XStreamAlias("ENDERECO")
	private EnderecoBoletoOnlineCaixaEconomicaVO endereco;
	
	public PagadorBoletoOnlineCaixaEconomicaVO() {
		
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EnderecoBoletoOnlineCaixaEconomicaVO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoBoletoOnlineCaixaEconomicaVO endereco) {
		this.endereco = endereco;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
   
	
	
	
	
}
