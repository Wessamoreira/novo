package webservice.moodle;

import com.google.gson.annotations.SerializedName;

public class CursoItemRSVO {
	
	@SerializedName("curso_codigo")
	private Integer codigo;
	@SerializedName("curso_nome")
	private String nome;
	@SerializedName("modalidade_codigo")
	private Integer codigoModalidade;
	@SerializedName("modalidade_nome")
	private String nomeModalidade;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		if(nome == null)
			nome = "";
		
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Integer getCodigoModalidade() {
		if (codigoModalidade == null)
			codigoModalidade = 0;
		return codigoModalidade;
	}
	public void setCodigoModalidade(Integer codigoModalidade) {
		this.codigoModalidade = codigoModalidade;
	}
	
	public String getNomeModalidade() {
		if (nomeModalidade == null)
			nomeModalidade = "";
		return nomeModalidade;
	}
	public void setNomeModalidade(String nomeModalidade) {
		this.nomeModalidade = nomeModalidade;
	}

}