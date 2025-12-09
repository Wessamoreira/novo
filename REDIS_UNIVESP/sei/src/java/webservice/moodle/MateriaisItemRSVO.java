package webservice.moodle;

import com.google.gson.annotations.SerializedName;

public class MateriaisItemRSVO {
	
	@SerializedName("cpf")
	private String cpf;
	@SerializedName("curso_codigo")
	private Integer curso_codigo;
	@SerializedName("turma_codigo")
	private Integer turma_codigo;
	@SerializedName("modulo_codigo")
	private Integer modulo_codigo;
	@SerializedName("codigo_download")
	private Integer codigo_download;
	@SerializedName("material_codigo")
	private Long material_codigo;
	@SerializedName("material_titulo")
	private String material_titulo;
	@SerializedName("material_descricao")
	private String material_descricao;
	@SerializedName("material_extensao")
	private String material_extensao;
	@SerializedName("material_datacricao")
	private Long  material_datacriacao;
	@SerializedName("material_dataatualizacao")
	private Long  material_dataatualizacao;
	@SerializedName("material_dataremocao")
	private Long  material_dataremocao;
	
	public String getCpf() {
		if (cpf == null)
			cpf = "";
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public Integer getCurso_codigo() {
		if (curso_codigo == null) {
			curso_codigo = 0;
		}
		return curso_codigo;
	}
	
	public void setCurso_codigo(Integer curso_codigo) {
		this.curso_codigo = curso_codigo;
	}
	
	public Integer getTurma_codigo() {
		if (turma_codigo == null) {
			turma_codigo = 0;
		}
		return turma_codigo;
	}
	
	public void setTurma_codigo(Integer turma_codigo) {
		this.turma_codigo = turma_codigo;
	}

	public Integer getModulo_codigo() {
		if (modulo_codigo == null) {
			modulo_codigo = 0;
		}
		return modulo_codigo;
	}
	
	public void setModulo_codigo(Integer modulo_codigo) {
		this.modulo_codigo = modulo_codigo;
	}
	
	public Long getMaterial_codigo() {
		if (material_codigo == null) {
			material_codigo = 0L;
		}
		return material_codigo;
	}

	public void setCodigo_download(Integer codigo_download) {
		this.codigo_download = codigo_download;
	}
	
	public Integer getCodigo_download() {
		if (codigo_download == null) {
			codigo_download = 0;
		}
		return codigo_download;
	}
	
	public void setMaterial_codigo(Long material_codigo) {
		this.material_codigo = material_codigo;
	}
	
	public String getMaterial_titulo() {
		if (material_titulo == null) {
			material_titulo = "";
		}
		return material_titulo;
	}
	
	public void setMaterial_titulo(String material_titulo) {
		this.material_titulo = material_titulo;
	}
	
	public String getMaterial_descricao() {
		if (material_descricao == null) {
			material_descricao = "";
		}
		return material_descricao;
	}
	
	public void setMaterial_descricao(String material_descricao) {
		this.material_descricao = material_descricao;
	}
	
	public String getMaterial_extensao() {
		if (material_extensao == null) {
			material_extensao = "";
		}
		return material_extensao;
	}

	public void setMaterial_extensao(String material_extensao) {
		this.material_extensao = material_extensao;
	}
	
	public Long getMaterial_datacriacao() {
		if (material_datacriacao == null) {
			material_datacriacao = 0L;
		}
		return material_datacriacao;
	}
	
	public void setMaterial_datacriacao(Long material_datacriacao) {
		this.material_datacriacao = material_datacriacao;
	}
	
	public Long getMaterial_dataatualizacao() {
		if (material_dataatualizacao == null) {
			material_dataatualizacao = 0L;
		}
		return material_dataatualizacao;
	}
	
	public void setMaterial_dataatualizacao(Long material_dataatualizacao) {
		this.material_dataatualizacao = material_dataatualizacao;
	}
	
	public Long getMaterial_dataremocao() {
		if (material_dataremocao == null) {
			material_dataremocao = 0L;
		}
		return material_dataremocao;
	}
	public void setMaterial_dataremocao(Long material_dataremocao) {
		this.material_dataremocao = material_dataremocao;
	}
	
}