package webservice.moodle;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

import negocio.comuns.utilitarias.Uteis;

public class ModuloItemRSVO {
	
	@SerializedName("modulo_codigo")
	private Integer codigo;
	@SerializedName("modulo_nome")
	private String nome;
	@SerializedName("modulo_modalidade_codigo")
	private Integer modulo_modalidade_codigo;
	@SerializedName("modulo_modalidade_nome")
	private String modulo_modalidade_nome;
	@SerializedName("modulo_inicio")
	private Long inicio;
	@SerializedName("modulo_fim")
	private Long fim;
	@SerializedName("modulo_ano")
	private String ano;
	@SerializedName("modulo_semestre")
	private String semestre;
	
	private transient Timestamp dataInicio;
	private transient Timestamp dataFim;

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
	
	public Long getInicio() {
		if (inicio == null)
			inicio = 0l;
		return inicio;
	}
	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}
	
	public Long getFim() {
		if (fim == null)
			fim = 0l;
		return fim;
	}
	public void setFim(Long fim) {
		this.fim = fim;
	}
	
	public Timestamp getDataInicio() {
		if (dataInicio == null)
			dataInicio = Uteis.getDataJDBCTimestamp(new Date());
		return dataInicio;
	}
	
	public void setDataInicio (Timestamp dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Timestamp getDataFim() {
		if (dataFim == null)
			dataFim = Uteis.getDataJDBCTimestamp(new Date());
		return dataFim;
	}
	public void setDataFim(Timestamp dataFim) {
		this.dataFim = dataFim;
	}
	public Integer getModulo_modalidade_codigo() {
		if (modulo_modalidade_codigo == null) {
			modulo_modalidade_codigo = 0;
		}
		return modulo_modalidade_codigo;
	}
	public void setModulo_modalidade_codigo(Integer modulo_modalidade_codigo) {
		this.modulo_modalidade_codigo = modulo_modalidade_codigo;
	}
	public String getModulo_modalidade_nome() {
		if (modulo_modalidade_nome == null) {
			modulo_modalidade_nome = "Presencial";
		}
		return modulo_modalidade_nome;
	}
	public void setModulo_modalidade_nome(String modulo_modalidade_nome) {
		this.modulo_modalidade_nome = modulo_modalidade_nome;
	}
	
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
}