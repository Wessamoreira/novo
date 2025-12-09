package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

public class PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO extends SuperVO {

	private static final long serialVersionUID = -1909709512908040667L;

	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsino;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomatica;

	/**
	 * Construtor padrão da classe <code>Cargo</code>. Cria uma nova instância desta
	 * entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO() {
		super();
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

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomatica() {
		if (personalizacaoMensagemAutomatica == null) {
			personalizacaoMensagemAutomatica = new PersonalizacaoMensagemAutomaticaVO();
		}
		return personalizacaoMensagemAutomatica;
	}

	public void setPersonalizacaoMensagemAutomatica(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomatica) {
		this.personalizacaoMensagemAutomatica = personalizacaoMensagemAutomatica;
	}

}