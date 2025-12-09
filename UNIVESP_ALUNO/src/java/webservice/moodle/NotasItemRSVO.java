package webservice.moodle;

import com.google.gson.annotations.SerializedName;

public class NotasItemRSVO {
	
	@SerializedName("cpf")
	private String cpf;
	@SerializedName("email")
	private String email;
	@SerializedName("ano")
	private String ano;
	@SerializedName("semestre")
	private String semestre;
	@SerializedName("bimestre")
	private String bimestre;
	@SerializedName("curso_codigo")
	private Integer cursoCodigo;
	@SerializedName("turma_codigo")
	private Integer turmaCodigo;
	@SerializedName("modulo_codigo")
	private Integer moduloCodigo;
	@SerializedName("nota")
	private Double nota;
	@SerializedName("frequencia")
	private Double frequencia;
	@SerializedName("variavel_nota")
	private String variavelNota;
	@SerializedName("calcular_media")
	private Boolean calcularMedia;
	
	public String getCpf() {
		if (cpf == null)
			cpf = "";
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Integer getCursoCodigo() {
		if (cursoCodigo == null)
			cursoCodigo = 0;
		return cursoCodigo;
	}
	public void setCursoCodigo(Integer cursoCodigo) {
		this.cursoCodigo = cursoCodigo;
	}
	public Integer getTurmaCodigo() {
		if (turmaCodigo == null)
			turmaCodigo = 0;
		return turmaCodigo;
	}
	public void setTurmaCodigo(Integer turmaCodigo) {
		this.turmaCodigo = turmaCodigo;
	}
	public Integer getModuloCodigo() {
		if (moduloCodigo == null)
			moduloCodigo = 0;
		return moduloCodigo;
	}
	public void setModuloCodigo(Integer moduloCodigo) {
		this.moduloCodigo = moduloCodigo;
	}
	public Double getNota() {		
		return nota;
	}
	public void setNota(Double nota) {
		this.nota = nota;
	}
	public Double getFrequencia() {	
		return frequencia;
	}
	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}
	
	public String getVariavelNota() {
		if (variavelNota == null) {
			variavelNota = "";
		}
		return variavelNota;
	}
	
	public void setVariavelNota(String variavelNota) {
		this.variavelNota = variavelNota;
	}
	
	public Boolean getCalcularMedia() {
		if (calcularMedia == null) {
			calcularMedia = true;
		}
		return calcularMedia;
	}
	
	public void setCalcularMedia(Boolean calcularMedia) {
		this.calcularMedia = calcularMedia;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	public String getBimestre() {
		return bimestre;
	}
	public void setBimestre(String bimestre) {
		this.bimestre = bimestre;
	}
	
	
}