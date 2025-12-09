package webservice.servicos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "processoMatricula")
public class CondicaoPagamentoRSVO {

	private Integer codigo;
	private String nome;
	private Double valorMatricula;
	private Double valorMensalidade;
	private Integer parcelas;
	private String categoria;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if(nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "valorMatricula")
	public Double getValorMatricula() {
		if(valorMatricula == null) {
			valorMatricula = 0.0;
		}
		return valorMatricula;
	}

	public void setValorMatricula(Double valorMatricula) {
		this.valorMatricula = valorMatricula;
	}

	@XmlElement(name = "valorMensalidade")
	public Double getValorMensalidade() {
		if(valorMensalidade == null) {
			valorMensalidade = 0.0;
		}
		return valorMensalidade;
	}

	public void setValorMensalidade(Double valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}

	@XmlElement(name = "parcelas")
	public Integer getParcelas() {
		if(parcelas == null) {
			parcelas = 0;
		}
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}
	
	@XmlElement(name = "categoria")
	public String getCategoria() {
		if(categoria == null) {
			categoria = "";
		}
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}
