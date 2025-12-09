package relatorio.negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class ParametroRelatorioVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String entidade;
	private String campo;
	private Boolean apresentarCampo;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Boolean getApresentarCampo() {
		if (apresentarCampo == null) {
			apresentarCampo = false;
		}
		return apresentarCampo;
	}

	public void setApresentarCampo(Boolean apresentarCampo) {
		this.apresentarCampo = apresentarCampo;
	}

	public String getEntidade() {
		if (entidade == null) {
			entidade = "";
		}
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

}
