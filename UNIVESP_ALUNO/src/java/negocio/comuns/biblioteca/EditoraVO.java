package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Editora. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EditoraVO extends SuperVO implements PossuiEndereco {

	private Integer codigo;
	private String nome;
	private String site;
	private String CEP;
	private String endereco;
	private String numero;
	private String setor;
	private String telefone;
	private String telefone1;
	private String fax;
	private String email;
	private String contato;
        public static final long serialVersionUID = 1L;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Cidade </code>.
	 */
	private CidadeVO cidade;

	/**
	 * Construtor padrão da classe <code>Editora</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public EditoraVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>EditoraVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(EditoraVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Editora) deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setNome(getNome().toUpperCase());
		setSite(getSite().toUpperCase());
		setEndereco(getEndereco().toUpperCase());
		setNumero(getNumero().toUpperCase());
		setBairro(getBairro().toUpperCase());
		setTelefone(getTelefone().toUpperCase());
		setTelefone1(getTelefone1().toUpperCase());
		setFax(getFax().toUpperCase());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
		setSite("");
		setEndereco("");
		setNumero("");
		setBairro("");
		setTelefone("");
		setTelefone1("");
		setFax("");
	}

	/**
	 * Retorna o objeto da classe <code>Cidade</code> relacionado com (
	 * <code>Editora</code>).
	 */
	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return (cidade);
	}

	/**
	 * Define o objeto da classe <code>Cidade</code> relacionado com (
	 * <code>Editora</code>).
	 */
	public void setCidade(CidadeVO obj) {
		this.cidade = obj;
	}

	public String getFax() {
		if (fax == null) {
			fax = "";
		}
		return (fax);
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getTelefone1() {
		if (telefone1 == null) {
			telefone1 = "";
		}
		return (telefone1);
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone() {
		if (telefone == null) {
			telefone = "";
		}
		return (telefone);
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getBairro() {
		if (setor == null) {
			setor = "";
		}
		return (setor);
	}

	public void setBairro(String bairro) {
		this.setor = bairro;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return (numero);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return (endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSite() {
		if (site == null) {
			site = "";
		}
		return (site);
	}

	public void setSite(String site) {
		this.site = site;
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

	public String getCEP() {
		return CEP;
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	// Maneira para não precisar refatorar o codigo.
	public String getSetor() {
		return getBairro();
	}

	public void setSetor(String setor) {
		setBairro(setor);
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContato() {
		if (contato == null) {
			contato = "";
		}
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

}