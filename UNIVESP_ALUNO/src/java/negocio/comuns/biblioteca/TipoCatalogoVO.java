package negocio.comuns.biblioteca;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade TipoCatalogo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoCatalogoVO extends SuperVO  {

	private Integer codigo;
	private String nome;
	private String sigla;
	private Integer subdivisao;
	private TreeNodeCustomizado arvoreTipoCatalogo;
    public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>TipoCatalogo</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public TipoCatalogoVO() {
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

	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public Integer getSubdivisao() {
		if(subdivisao == null) {
			subdivisao = 0;
		}
		return subdivisao;
	}
	
	public void setSubdivisao(Integer subdivisao) {
		this.subdivisao = subdivisao;
	}
	
	public TreeNodeCustomizado getArvoreTipoCatalogo() {
		return arvoreTipoCatalogo;
	}
	
	public void setArvoreTipoCatalogo(TreeNodeCustomizado arvoreTipoCatalogo) {
		this.arvoreTipoCatalogo = arvoreTipoCatalogo;
	}
}