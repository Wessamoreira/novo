package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Banco. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class BancoEnumVO extends SuperVO {

	private Integer codigo;
	private String descricao;
	private String nrBanco;
	private Boolean possuiRemessa;
	private Boolean possuiRetorno;
	private Boolean possuiRetornoRemessa;
	private Boolean possuiRemessaContaPagar;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Banco</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public BancoEnumVO() {
		super();
	}

	public String getNrBanco() {
		if (nrBanco == null) {
			nrBanco = "";
		}
		return (nrBanco);
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}

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

	public Boolean getPossuiRemessa() {
		if (possuiRemessa == null) {
			possuiRemessa = Boolean.FALSE;
		}
		return possuiRemessa;
	}

	public void setPossuiRemessa(Boolean possuiRemessa) {
		this.possuiRemessa = possuiRemessa;
	}

	public Boolean getPossuiRetorno() {
		if (possuiRetorno == null) {
			possuiRetorno = Boolean.FALSE;
		}
		return possuiRetorno;
	}

	public void setPossuiRetorno(Boolean possuiRetorno) {
		this.possuiRetorno = possuiRetorno;
	}

	public Boolean getPossuiRetornoRemessa() {
		if (possuiRetornoRemessa == null) {
			possuiRetornoRemessa = Boolean.FALSE;
		}
		return possuiRetornoRemessa;
	}

	public void setPossuiRetornoRemessa(Boolean possuiRetornoRemessa) {
		this.possuiRetornoRemessa = possuiRetornoRemessa;
	}

	public Boolean getPossuiRemessaContaPagar() {
		if (possuiRemessaContaPagar == null) {
			possuiRemessaContaPagar = Boolean.FALSE;
		}
		return possuiRemessaContaPagar;
	}

	public void setPossuiRemessaContaPagar(Boolean possuiRemessaContaPagar) {
		this.possuiRemessaContaPagar = possuiRemessaContaPagar;
	}

}
