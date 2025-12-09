package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;

 public class LinguaPublicacaoCatalogoVO extends SuperVO  {

	private Integer codigo;
	private String nome;
	private String marcCode;	
    public static final long serialVersionUID = 1L;

	public LinguaPublicacaoCatalogoVO() {
		super();
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getMarcCode() {
		if (marcCode == null) {
			marcCode = "";
		}
		return marcCode;
	}

	public void setMarcCode(String marcCode) {
		this.marcCode = marcCode;
	}
}