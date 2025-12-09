package webservice.moodle;

import java.sql.Timestamp;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

import negocio.comuns.utilitarias.Uteis;

public class TurmaItemRSVO {
	
	@SerializedName("turma_codigo")
	private Integer codigo;
	@SerializedName("turma_nome")
	private String nome;
	@SerializedName("turma_inicio")
	private Long inicio;
	@SerializedName("turma_fim")
	private Long fim;
	@SerializedName("turma_periodoLetivo")
	private Integer periodoLetivo;
	
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
			inicio = 00000l;
		return inicio;
	}
	public void setInicio(Long inicio) {
		this.inicio = inicio;
	}
	
	public Long getFim() {
		if (fim == null)
			fim = 00000l;
		return fim;
	}
	public void setFim(Long fim) {
		this.fim = fim;
	}
	public Timestamp getDataInicio() {
//		if (dataInicio == null)
//			dataInicio = Uteis.getDataJDBCTimestamp(new Date());
		return dataInicio;
	}
	public void setDataInicio(Timestamp dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Timestamp getDataFim() {
//		if (dataFim == null)
//			dataFim = Uteis.getDataJDBCTimestamp(new Date());
		return dataFim;
	}
	public void setDataFim(Timestamp dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}
}