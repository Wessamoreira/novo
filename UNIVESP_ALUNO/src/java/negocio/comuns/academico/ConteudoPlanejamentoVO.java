package negocio.comuns.academico;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Disciplina;

/**
 * Reponsável por manter os dados da entidade ConteudoPlanejamento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Disciplina
 */
public class ConteudoPlanejamentoVO extends SuperVO {

	private static final long serialVersionUID = -8526545446141558973L;

	private Integer codigo;
	private String conteudo;
	private String habilidade;
	private String atitude;
	private String metodologia;
	private BigDecimal cargahoraria;
	private Integer ordem;
	private String classificacao;
	private Integer disciplina;
	private PlanoEnsinoVO planoEnsino;
	private String praticaSupervisionada;

	/**
	 * Construtor padrão da classe <code>ConteudoPlanejamento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ConteudoPlanejamentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ConteudoPlanejamentoVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(ConteudoPlanejamentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getConteudo().equals("")) {
			throw new ConsistirException("O campo CONTEÚDO (Conteúdo/Planejamento) deve ser informado.");
		}
		// if (obj.getHabilidade().equals("")) {
		// throw new ConsistirException("O campo HABILIDADE (Conteúdo/Planejamento) deve
		// ser informado.");
		// }
		// if (obj.getAtitude().equals("")) {
		// throw new ConsistirException("O campo ATITUDE (Conteúdo/Planejamento) deve
		// ser informado.");
		// }
		// if (obj.getMetodologia().equals("")) {
		// throw new ConsistirException("O campo METODOLOGIA (Conteúdo/Planejamento)
		// deve ser informado.");
		// }
		// if (obj.getCargahoraria() == null || obj.getCargahoraria().intValue() == 0) {
		// throw new ConsistirException("O campo CARGA HORÁRIA (Conteúdo/Planejamento)
		// deve ser informado.");
		// }
		if (obj.getClassificacao().equals("")) {
			throw new ConsistirException("O campo CLASSIFICAÇÃO (Conteúdo/Planejamento) deve ser informado.");
		}
	}

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public String getClassificacao() {
		if (classificacao == null) {
			classificacao = "";
		}
		return (classificacao);
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public BigDecimal getCargahoraria() {
		if (cargahoraria == null) {
			cargahoraria = BigDecimal.ZERO;
		}
		return cargahoraria;
	}

	public void setCargahoraria(BigDecimal cargahoraria) {
		this.cargahoraria = cargahoraria;
	}

	public String getMetodologia() {
		if (metodologia == null) {
			metodologia = "";
		}
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public String getAtitude() {
		if (atitude == null) {
			atitude = "";
		}
		return atitude;
	}

	public void setAtitude(String atitude) {
		this.atitude = atitude;
	}

	public String getHabilidade() {
		if (habilidade == null) {
			habilidade = "";
		}
		return habilidade;
	}

	public void setHabilidade(String habilidade) {
		this.habilidade = habilidade;
	}

	public String getConteudo() {
		if (conteudo == null) {
			conteudo = "";
		}
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public PlanoEnsinoVO getPlanoEnsino() {
		if (planoEnsino == null) {
			planoEnsino = new PlanoEnsinoVO();
		}
		return planoEnsino;
	}

	public void setPlanoEnsino(PlanoEnsinoVO planoEnsino) {
		this.planoEnsino = planoEnsino;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getPraticaSupervisionada() {
		if (praticaSupervisionada == null) {
			praticaSupervisionada = "";
		}
		return praticaSupervisionada;
	}

	public void setPraticaSupervisionada(String praticaSupervisionada) {
		this.praticaSupervisionada = praticaSupervisionada;
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com
	 * um domínio específico. Com base no valor de armazenamento do atributo esta
	 * função é capaz de retornar o de apresentação correspondente. Útil para campos
	 * como sexo, escolaridade, etc.
	 */
	public String getClassificacao_Apresentar() {
		if (getClassificacao().equals("EV")) {
			return "Evento";
		}
		if (getClassificacao().equals("CO")) {
			return "Contéudo";
		}
		if (getClassificacao().equals("AV")) {
			return "Avaliação";
		}
		if (getClassificacao().equals("TE")) {
			return "Visita Técnica";
		}
		if (getClassificacao().equals("PR")) {
			return "Prático";
		}
		return (getClassificacao());
	}
}