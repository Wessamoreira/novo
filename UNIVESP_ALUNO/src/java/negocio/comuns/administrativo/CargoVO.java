package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Cargo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CargoVO extends SuperVO {

	private static final long serialVersionUID = -1909709512908040667L;

	private Integer codigo;
	private String nome;
	private String descricao;
	private String portariaCargo;
	private String responsabilidades;
	private DepartamentoVO departamento;
	private Boolean controlaNivelExperiencia;
	private Boolean consultorVendas;
	private String legislacao;

	// Classificacao Brasileira de Ocupacoes
	private String cbo;


	/**
	 * Construtor padrão da classe <code>Cargo</code>. Cria uma nova instância desta
	 * entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public CargoVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CargoVO</code>. Todos os tipos de consistência de dados são e devem ser
	 * implementadas neste método. São validações típicas: verificação de campos
	 * obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(CargoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Cargo) deve ser informado.");
		}
	}

	public String getResponsabilidades() {
		if (responsabilidades == null) {
			responsabilidades = "";
		}
		return (responsabilidades);
	}

	public void setResponsabilidades(String responsabilidades) {
		this.responsabilidades = responsabilidades;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	/**
	 * @return the departamento
	 */
	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	/**
	 * @param departamento
	 *            the departamento to set
	 */
	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}

	public Boolean getControlaNivelExperiencia() {
		if (controlaNivelExperiencia == null) {
			controlaNivelExperiencia = Boolean.FALSE;
		}
		return controlaNivelExperiencia;
	}

	public void setControlaNivelExperiencia(Boolean controlaNivelExperiencia) {
		this.controlaNivelExperiencia = controlaNivelExperiencia;
	}

	public Boolean getConsultorVendas() {
		if (consultorVendas == null) {
			consultorVendas = Boolean.FALSE;
		}
		return consultorVendas;
	}

	public void setConsultorVendas(Boolean consultorVendas) {
		this.consultorVendas = consultorVendas;
	}

	public String getPortariaCargo() {
		if (portariaCargo == null) {
			portariaCargo = "";
		}
		return portariaCargo;
	}

	public void setPortariaCargo(String portariaCargo) {
		this.portariaCargo = portariaCargo;
	}

	public String getCbo() {
		if (cbo == null)
			cbo = "";
		return cbo;
	}

	public void setCbo(String cbo) {
		this.cbo = cbo;
	}



	public String getLegislacao() {
		if (legislacao == null) {
			legislacao = "";
		}
		return legislacao;
	}

	public void setLegislacao(String legislacao) {
		this.legislacao = legislacao;
	}
}
