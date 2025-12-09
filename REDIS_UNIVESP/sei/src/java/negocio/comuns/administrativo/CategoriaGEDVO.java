package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

public class CategoriaGEDVO extends SuperVO {

	private static final long serialVersionUID = 384974648891046632L;

	private Integer codigo;
	private String descricao;
	private String identificador;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getIdentificador() {
		if (identificador == null) {
			identificador = "";
		}
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

}