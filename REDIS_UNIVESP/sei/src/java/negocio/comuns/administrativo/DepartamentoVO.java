package negocio.comuns.administrativo;

import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Departamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "departamentoVO")
public class DepartamentoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Departamento </code>.
	 */
	private DepartamentoVO departamentoSuperior;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private PessoaVO responsavel;
	private UnidadeEnsinoVO unidadeEnsino;
	private String codigoContabil;
	private String nomeContabil;
	private String nivelContabil;
	// Atributo responsável por definir se o professor poderá enviar Comunicado
	// Interno para tal Departamento
	private Boolean faleConosco;
	private CentroResultadoVO centroResultadoVO;
	private ConfiguracaoLdapVO configuracaoLdapVO;
	private boolean controlaEstoque = false;
	public static final long serialVersionUID = 1L;
	
	private boolean filtrarDepartamento;
	
	public enum EnumCampoConsultaDepartamento {
		NOME, 
		NOME_PESSOA,
		CODIGO;
	}

	/**
	 * Construtor padrão da classe <code>Departamento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public DepartamentoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Construtor padrão da classe <code>Departamento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 *
	 * @param inicializarRecursivamente
	 *            Indica que a inicialização deve ignorar atributos que
	 *            implementem auto relacionamento (recursividade).
	 */
	public DepartamentoVO(boolean inicializarRecursivamente) {
		super();
		if (inicializarRecursivamente) {
			inicializarDadosComRecursividade();
		} else {
			inicializarDados();
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>DepartamentoVO</code>. Todos os tipos de consistência de dados são
	 * e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(DepartamentoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Departamento) deve ser informado.");
		}
		if (obj.getDepartamentoSuperior().getCodigo().intValue() == obj.getCodigo() && obj.getDepartamentoSuperior().getCodigo().intValue() != 0) {
			throw new ConsistirException("Não é possível selecionar o próprio departamento como seu departamento superior.");
		}
		// if(obj.getUnidadeEnsino()== null
		// ||obj.getUnidadeEnsino().getCodigo().intValue() ==0){
		// throw new
		// ConsistirException("O campo UNIDADE ENSINO (Departamento) deve ser
		// informado.");
		// }
	}

	/**
	 * Operação reponsável por inicializar os atributos de classes que utilizam
	 * recursividade. <code>Departamento</code> é recursiva em função de um auto
	 * relacionamento.
	 */
	public void inicializarDadosComRecursividade() {
		setCodigo(0);
		setNome("");
		setDepartamentoSuperior(null);
		setResponsavel(new PessoaVO());
		setUnidadeEnsino(new UnidadeEnsinoVO());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Departamento</code>).
	 */
	public PessoaVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new PessoaVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Departamento</code>).
	 */
	public void setResponsavel(PessoaVO obj) {
		this.responsavel = obj;
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

	/**
	 * Retorna o objeto da classe <code>Departamento</code> relacionado com (
	 * <code>Departamento</code>).
	 */
	public DepartamentoVO getDepartamentoSuperior() {
		if (departamentoSuperior == null) {
			departamentoSuperior = new DepartamentoVO();
		}
		return (departamentoSuperior);
	}

	/**
	 * Define o objeto da classe <code>Departamento</code> relacionado com (
	 * <code>Departamento</code>).
	 */
	public void setDepartamentoSuperior(DepartamentoVO obj) {
		this.departamentoSuperior = obj;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			return "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Boolean getFaleConosco() {
		if (faleConosco == null) {
			faleConosco = Boolean.TRUE;
		}
		return faleConosco;
	}

	public void setFaleConosco(Boolean faleConosco) {
		this.faleConosco = faleConosco;
	}
	
	public CentroResultadoVO getCentroResultadoVO() {
		centroResultadoVO = Optional.ofNullable(centroResultadoVO).orElse(new CentroResultadoVO());
		return centroResultadoVO;
	}

	public void setCentroResultadoVO(CentroResultadoVO centroResultadoVO) {
		this.centroResultadoVO = centroResultadoVO;
	}

	public String getCodigoContabil() {
		if (codigoContabil == null) {
			codigoContabil = "";
		}
		return codigoContabil;
	}

	public void setCodigoContabil(String codigoContabil) {
		this.codigoContabil = codigoContabil;
	}

	public String getNomeContabil() {
		if (nomeContabil == null) {
			nomeContabil = "";
		}
		return nomeContabil;
	}

	public void setNomeContabil(String nomeContabil) {
		this.nomeContabil = nomeContabil;
	}
	
	public String getNivelContabil() {
		if (nivelContabil == null) {
			nivelContabil = "";
		}
		return nivelContabil;
	}
	
	public void setNivelContabil(String nivelContabil) {
		this.nivelContabil = nivelContabil;
	}

	public boolean isControlaEstoque() {
		return controlaEstoque;
	}

	public void setControlaEstoque(boolean controlaEstoque) {
		this.controlaEstoque = controlaEstoque;
	}

	public boolean isFiltrarDepartamento() {
		return filtrarDepartamento;
	}

	public void setFiltrarDepartamento(boolean filtrarDepartamento) {
		this.filtrarDepartamento = filtrarDepartamento;
	}
	
	public ConfiguracaoLdapVO getConfiguracaoLdapVO() {
        if (configuracaoLdapVO == null) {
            configuracaoLdapVO = new ConfiguracaoLdapVO();
        }
        return configuracaoLdapVO;
    }

    public void setConfiguracaoLdapVO(ConfiguracaoLdapVO configuracaoLdapVO) {
        this.configuracaoLdapVO = configuracaoLdapVO;
    }
	
}
