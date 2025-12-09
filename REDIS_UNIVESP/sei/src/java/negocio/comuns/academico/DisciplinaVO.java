package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.ModeloGeracaoSalaBlackboardEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.dominios.ModeloApresentacaoDashboardEnum;

/**
 * Reponsável por manter os dados da entidade Disciplina. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "disciplina")
public class DisciplinaVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer cont;
	private String nome;
	private String abreviatura;
	private String nivelEducacional;
	private String descricaoComplementar;
	// private Integer cargaHoraria;
	// private Integer nrCreditos;
	// private String tipoDisciplina;

	// private String ementa;
	// private String competencia;
	private String listaDisciplinaRemovidasImportacao;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>DisciplinaEquivalente</code>.
	 */
	private List<DisciplinaEquivalenteVO> disciplinaEquivalenteVOs;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>DisciplinaPreRequisito</code>.
	 */
	// private List disciplinaPreRequisitoVOs;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>ReferenciaBibliografica</code>.
	 */
	private List referenciaBibliograficaVOs;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>ConteudoPlanejamento</code>.
	 */
	private List conteudoPlanejamentoVOs;
	private List conteudoVOs;
	private AreaConhecimentoVO areaConhecimento;
	private Boolean selecionado;
	private PlanoCursoVO planoCursoVO;
	private Integer periodoLetivo;
	private String descricaoPeriodoLetivo;
	/**
	 * NomeGrade nao persistido no banco, serve para facilitar algumas consultas
	 * por disciplina
	 */
	private String nomeGrade;
	private String nomeMinhasNotasAluno;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>ConfiguracaoAcademico </code>.
	 */
	// private ConfiguracaoAcademicoVO configuracaoAcademico = null;
	private List<DisciplinaCompostaVO> disciplinaCompostaVOs;
	private Boolean disciplinaComposta;
	// Transiet
	private Boolean excluirDisciplina;
	private Integer diaInicio;
	private Integer diaFim;
	private String frequencia;
	private String mes;
	private String ano;
	private String semestre;
	private Integer cargaHorariaForaGrade;
	private String dataModulo;


	private ModalidadeDisciplinaEnum modalidadeDisciplina;
	private ClassificacaoDisciplinaEnum classificacaoDisciplina;
//	private ModeloGeracaoSalaBlackboardEnum modeloGeracaoSalaBlackboard;
	private Boolean dividirSalaEmGrupo;
	private Integer nrMaximoAlunosPorAmbientacao;
	private Integer nrMaximoAulosPorSala;
	private Integer nrMinimoAlunosPorSala;
	private Integer nrMaximoAulosPorGrupo;
	private Integer nrMinimoAlunosPorGrupo;
	private String fonteDeDadosBlackboard;
	private String idConteudoMasterBlackboard;
	private Double percentualMinimoCargaHorariaAproveitamento;
	private Integer qtdeMinimaDeAnosAproveitamento;

	/**
	 * TRANSIENT desde a versão 5.0, utilizado somente para algumas telas, para
	 * facilidar apresentação de dados. A carga horária oficial de uma
	 * disciplina fica na MatrizCurricular
	 */
	private Integer cargaHorariaPrevista;
	private Integer numeroCreditoPrevisto;
	private Boolean preRequisito;
	private GrupoPessoaVO grupoPessoaVO;
	
	/**
	 * TRANSIENTE
	 */
	private List<Integer> disciplinasEquivalentes;

	/**
	 * Construtor padrão da classe <code>Disciplina</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public DisciplinaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>DisciplinaVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(DisciplinaVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Disciplina) deve ser informado.");
		}
		if (obj.getDisciplinaComposta()) {
			if (obj.getDisciplinaCompostaVOs().isEmpty()) {
				throw new ConsistirException("Deve ser informado as disciplinas que compõem a Disciplina Composta.");
			}
		}
		if (obj.getNivelEducacional().equals("")) {
			throw new ConsistirException("O campo NIVEL EDUCACIONAL (Disciplina) deve ser informado.");
		}
		// if (obj.getEmenta().equals("")) {
		// throw new
		// ConsistirException("O campo EMENTA (Disciplina) deve ser informado.");
		// }
		// if (obj.getAreaConhecimento().getCodigo().intValue() == 0 ||
		// obj.getAreaConhecimento() == null) {
		// throw new
		// ConsistirException("O campo ÁREA CONHECIMENTO (Disciplina) deve ser informado.");
		// }
	}

	public DisciplinaVO clone() throws CloneNotSupportedException {
		DisciplinaVO obj = (DisciplinaVO) super.clone();
		return obj;
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setCont(0);
		setNome("");
		// setEmenta("");
		// setCompetencia("");
		setSelecionado(false);
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>ConteudoPlanejamentoVO</code> ao List
	 * <code>conteudoPlanejamentoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ConteudoPlanejamento</code> - getConteudo() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConteudoPlanejamentoVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarConteudoPlanejamentoVOs(ConteudoPlanejamentoVO obj) throws Exception {
		int index = 0;
		Iterator i = getConteudoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
			if (objExistente.getConteudo().equals(obj.getConteudo())) {
				getConteudoVOs().remove(index);
				return;
			}
			index++;
		}
		if (obj.getClassificacao().equals("CO")) {
			adicionarObjConteudoVOs(obj);
		}
		adicionarObjConteudoPlanejamentoVOs(obj);

	}

	public void adicionarObjConteudoPlanejamentoVOs(ConteudoPlanejamentoVO obj) throws Exception {
		ConteudoPlanejamentoVO.validarDados(obj);

		int index = 0;
		Iterator i = getConteudoPlanejamentoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
			if (objExistente.getConteudo().equals(obj.getConteudo())) {
				getConteudoPlanejamentoVOs().set(index, obj);

				return;
			}
			index++;
		}
		getConteudoPlanejamentoVOs().add(obj);

	}

	public void adicionarObjConteudoVOs(ConteudoPlanejamentoVO obj) throws Exception {
		ConteudoPlanejamentoVO.validarDados(obj);

		int index = 0;
		Iterator i = getConteudoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
			if (objExistente.getConteudo().equals(obj.getConteudo())) {
				getConteudoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getConteudoVOs().add(obj);

	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>ConteudoPlanejamentoVO</code> no List
	 * <code>conteudoPlanejamentoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ConteudoPlanejamento</code> - getConteudo() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param conteudo
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void ecluirConteudoPlanejamentoVOs(ConteudoPlanejamentoVO obj, Integer codigo) throws Exception {
		if (obj.getClassificacao().equals("CO")) {
			excluirObjConteudoVOs(codigo);
		}
		excluirObjConteudoPlanejamentoVOs(codigo);

	}

	public void excluirObjConteudoPlanejamentoVOs(Integer codigo) throws Exception {
		int index = 0;
		Iterator i = getConteudoPlanejamentoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
			if (objExistente.getCodigo().equals(codigo)) {
				getConteudoPlanejamentoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public void excluirObjConteudoVOs(Integer codigo) throws Exception {
		int index = 0;
		Iterator i = getConteudoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
			if (objExistente.getCodigo().equals(codigo)) {
				getConteudoVOs().remove(index);
				return;
			}
			index++;
		}

	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>ConteudoPlanejamentoVO</code> no List
	 * <code>conteudoPlanejamentoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ConteudoPlanejamento</code> - getConteudo() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param conteudo
	 *            Parâmetro para localizar o objeto do List.
	 */
	public ConteudoPlanejamentoVO consultarObjConteudoPlanejamentoVO(Integer codigo) throws Exception {
		Iterator i = getConteudoPlanejamentoVOs().iterator();
		while (i.hasNext()) {
			ConteudoPlanejamentoVO objExistente = (ConteudoPlanejamentoVO) i.next();
			if (objExistente.getCodigo().equals(codigo)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>ReferenciaBibliograficaVO</code> ao List
	 * <code>referenciaBibliograficaVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ReferenciaBibliografica</code> - getTitulo() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ReferenciaBibliograficaVO</code> que
	 *            será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjReferenciaBibliograficaVOs(ReferenciaBibliograficaVO obj) throws Exception {
		ReferenciaBibliograficaVO.validarDados(obj);
		int index = 0;
		Iterator i = getReferenciaBibliograficaVOs().iterator();
		while (i.hasNext()) {
			ReferenciaBibliograficaVO objExistente = (ReferenciaBibliograficaVO) i.next();
			if (objExistente.getCatalogo().getTitulo().equals(obj.getCatalogo().getTitulo())) {
				getReferenciaBibliograficaVOs().set(index, obj);
				return;
			}
			index++;
		}
		getReferenciaBibliograficaVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>ReferenciaBibliograficaVO</code> no List
	 * <code>referenciaBibliograficaVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ReferenciaBibliografica</code> - getTitulo() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param titulo
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjReferenciaBibliograficaVOs(String titulo) throws Exception {
		int index = 0;
		Iterator i = getReferenciaBibliograficaVOs().iterator();
		while (i.hasNext()) {
			ReferenciaBibliograficaVO objExistente = (ReferenciaBibliograficaVO) i.next();
			if (objExistente.getCatalogo().getTitulo().equals(titulo)) {
				getReferenciaBibliograficaVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>ReferenciaBibliograficaVO</code> no List
	 * <code>referenciaBibliograficaVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>ReferenciaBibliografica</code> - getTitulo() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param titulo
	 *            Parâmetro para localizar o objeto do List.
	 */
	public ReferenciaBibliograficaVO consultarObjReferenciaBibliograficaVO(String titulo) throws Exception {
		Iterator i = getReferenciaBibliograficaVOs().iterator();
		while (i.hasNext()) {
			ReferenciaBibliograficaVO objExistente = (ReferenciaBibliograficaVO) i.next();
			if (objExistente.getCatalogo().getTitulo().equals(titulo)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>DisciplinaEquivalenteVO</code> no List
	 * <code>disciplinaEquivalenteVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinaEquivalente</code> -
	 * getEquivalente().getCodigo() - como identificador (key) do objeto no
	 * List.
	 * 
	 * @param equivalente
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjDisciplinaEquivalenteVOs(Integer equivalente) throws Exception {
		int index = 0;
		Iterator i = getDisciplinaEquivalenteVOs().iterator();
		while (i.hasNext()) {
			DisciplinaEquivalenteVO objExistente = (DisciplinaEquivalenteVO) i.next();
			if (objExistente.getEquivalente().getCodigo().equals(equivalente)) {
				getDisciplinaEquivalenteVOs().remove(index);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>DisciplinaEquivalenteVO</code> no List
	 * <code>disciplinaEquivalenteVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinaEquivalente</code> -
	 * getEquivalente().getCodigo() - como identificador (key) do objeto no
	 * List.
	 * 
	 * @param equivalente
	 *            Parâmetro para localizar o objeto do List.
	 */
	public DisciplinaEquivalenteVO consultarObjDisciplinaEquivalenteVO(Integer equivalente) throws Exception {
		Iterator i = getDisciplinaEquivalenteVOs().iterator();
		while (i.hasNext()) {
			DisciplinaEquivalenteVO objExistente = (DisciplinaEquivalenteVO) i.next();
			if (objExistente.getEquivalente().getCodigo().equals(equivalente)) {
				return objExistente;
			}
		}
		return null;
		// consultarObjSubordinadoOC
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>ReferenciaBibliografica</code>.
	 */
	public List getReferenciaBibliograficaVOs() {
		if (referenciaBibliograficaVOs == null) {
			referenciaBibliograficaVOs = new ArrayList();
		}
		return (referenciaBibliograficaVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>ReferenciaBibliografica</code>.
	 */
	public void setReferenciaBibliograficaVOs(List referenciaBibliograficaVOs) {
		this.referenciaBibliograficaVOs = referenciaBibliograficaVOs;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>DisciplinaEquivalente</code>.
	 */
	public List<DisciplinaEquivalenteVO> getDisciplinaEquivalenteVOs() {
		if (disciplinaEquivalenteVOs == null) {
			disciplinaEquivalenteVOs = new ArrayList<DisciplinaEquivalenteVO>();
		}
		return (disciplinaEquivalenteVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>DisciplinaEquivalente</code>.
	 */
	public void setDisciplinaEquivalenteVOs(List<DisciplinaEquivalenteVO> disciplinaEquivalenteVOs) {
		this.disciplinaEquivalenteVOs = disciplinaEquivalenteVOs;
	}

	// public String getCompetencia() {
	// if (competencia == null) {
	// competencia = Constantes.EMPTY;
	// }
	// return (competencia);
	// }
	//
	// public void setCompetencia(String competencia) {
	// this.competencia = competencia;
	// }
	//
	// public String getEmenta() {
	// if (ementa == null) {
	// ementa = Constantes.EMPTY;
	// }
	// return (ementa);
	// }
	//
	// public void setEmenta(String ementa) {
	// this.ementa = ementa;
	// }

	// public String getTipoDisciplina() {
	// if (tipoDisciplina == null) {
	// tipoDisciplina = Constantes.EMPTY;
	// }
	// return (tipoDisciplina);
	// }

	public AreaConhecimentoVO getAreaConhecimento() {
		if (areaConhecimento == null) {
			areaConhecimento = new AreaConhecimentoVO();
		}
		return areaConhecimento;
	}

	public void setAreaConhecimento(AreaConhecimentoVO areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	// public String getTipoDisciplina_Apresentar() {
	// return TipoDisciplina.getDescricao(tipoDisciplina);
	// }
	//
	// public void setTipoDisciplina(String tipoDisciplina) {
	// this.tipoDisciplina = tipoDisciplina;
	// }
	//
	// public Integer getNrCreditos() {
	// if (nrCreditos == null) {
	// nrCreditos = 0;
	// }
	// return (nrCreditos);
	// }
	//
	// public void setNrCreditos(Integer nrCreditos) {
	// this.nrCreditos = nrCreditos;
	// }
	//
	// public Integer getCargaHoraria() {
	// if (cargaHoraria == null) {
	// cargaHoraria = 0;
	// }
	// return (cargaHoraria);
	// }
	//
	// public void setCargaHoraria(Integer cargaHoraria) {
	// this.cargaHoraria = cargaHoraria;
	// }

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = Constantes.EMPTY;
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

	public List getConteudoVOs() {
		if (conteudoVOs == null) {
			conteudoVOs = new ArrayList();
		}
		return conteudoVOs;
	}

	public void setConteudoVOs(List ConteudoVOs) {
		this.conteudoVOs = ConteudoVOs;
	}

	public List getConteudoPlanejamentoVOs() {
		if (conteudoPlanejamentoVOs == null) {
			conteudoPlanejamentoVOs = new ArrayList();
		}
		return conteudoPlanejamentoVOs;
	}

	public void setConteudoPlanejamentoVOs(List conteudoPlanejamentoVOs) {
		this.conteudoPlanejamentoVOs = conteudoPlanejamentoVOs;
	}

	public Integer getCont() {
		return cont;
	}

	public void setCont(Integer cont) {
		this.cont = cont;
	}

	/**
	 * @return the selecionado
	 */
	public Boolean getSelecionado() {
		return selecionado;
	}

	/**
	 * @param selecionado
	 *            the selecionado to set
	 */
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	/**
	 * @return the planoCursoVO
	 */
	public PlanoCursoVO getPlanoCursoVO() {
		if (planoCursoVO == null) {
			planoCursoVO = new PlanoCursoVO();
		}
		return planoCursoVO;
	}

	/**
	 * @param planoCursoVO
	 *            the planoCursoVO to set
	 */
	public void setPlanoCursoVO(PlanoCursoVO planoCursoVO) {
		this.planoCursoVO = planoCursoVO;
	}

	// /**
	// * @return the configuracaoAcademico
	// */
	// public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
	// if (configuracaoAcademico == null) {
	// configuracaoAcademico = new ConfiguracaoAcademicoVO();
	// }
	// return configuracaoAcademico;
	// }
	//
	// /**
	// * @param configuracaoAcademico
	// * the configuracaoAcademico to set
	// */
	// public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO
	// configuracaoAcademico) {
	// this.configuracaoAcademico = configuracaoAcademico;
	// }
	//
	// public Boolean getUtilizarConfiguracaoAcademicoEspecifica() {
	// if ((this.configuracaoAcademico == null) ||
	// (this.configuracaoAcademico.getCodigo().equals(0))) {
	// return false;
	// } else {
	// return true;
	// }
	// }

	public void setNomeGrade(String nomeGrade) {
		this.nomeGrade = nomeGrade;
	}

	public String getNomeGrade() {
		if (nomeGrade == null) {
			nomeGrade = Constantes.EMPTY;
		}
		return nomeGrade;
	}

	public String getNomeDisciplinaGrade() {
		if (!getNomeGrade().equals("")) {
			return getNome() + " - " + getNomeGrade();
		}
		return getNome();
	}

	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getDescricaoPeriodoLetivo() {
		if (descricaoPeriodoLetivo == null) {
			descricaoPeriodoLetivo = Constantes.EMPTY;
		}
		return descricaoPeriodoLetivo;
	}

	public void setDescricaoPeriodoLetivo(String descricaoPeriodoLetivo) {
		this.descricaoPeriodoLetivo = descricaoPeriodoLetivo;
	}

	public String getPeriodoLetivoDisciplina() {
		return getDescricaoPeriodoLetivo() + " - " + getNome();
	}

	@Override
	public String toString() {
		return "Disciplina: " + this.getCodigo() + " Nome: " + this.getNome();
	}

	/**
	 * @return the listaDisciplinaRemovidasImportacao
	 */
	public String getListaDisciplinaRemovidasImportacao() {
		return listaDisciplinaRemovidasImportacao;
	}

	/**
	 * @param listaDisciplinaRemovidasImportacao
	 *            the listaDisciplinaRemovidasImportacao to set
	 */
	public void setListaDisciplinaRemovidasImportacao(String listaDisciplinaRemovidasImportacao) {
		this.listaDisciplinaRemovidasImportacao = listaDisciplinaRemovidasImportacao;
	}

	/**
	 * @return the excluirDisciplina
	 */
	public Boolean getExcluirDisciplina() {
		if (excluirDisciplina == null) {
			excluirDisciplina = Boolean.FALSE;
		}
		return excluirDisciplina;
	}

	/**
	 * @param excluirDisciplina
	 *            the excluirDisciplina to set
	 */
	public void setExcluirDisciplina(Boolean excluirDisciplina) {
		this.excluirDisciplina = excluirDisciplina;
	}

	public Boolean getDisciplinaComposta() {
		if (disciplinaComposta == null) {
			disciplinaComposta = Boolean.FALSE;
		}
		return disciplinaComposta;
	}

	public void setDisciplinaComposta(Boolean disciplinaComposta) {
		this.disciplinaComposta = disciplinaComposta;
	}

	public List<DisciplinaCompostaVO> getDisciplinaCompostaVOs() {
		if (disciplinaCompostaVOs == null) {
			disciplinaCompostaVOs = new ArrayList<DisciplinaCompostaVO>(0);
		}
		return disciplinaCompostaVOs;
	}

	public void setDisciplinaCompostaVOs(List<DisciplinaCompostaVO> disciplinaCompostaVOs) {
		this.disciplinaCompostaVOs = disciplinaCompostaVOs;
	}

	public String getNomeMinhasNotasAluno() {
		if (nomeMinhasNotasAluno == null) {
			nomeMinhasNotasAluno = Constantes.EMPTY;
		}
		return nomeMinhasNotasAluno;
	}

	public void setNomeMinhasNotasAluno(String nomeMinhasNotasAluno) {
		this.nomeMinhasNotasAluno = nomeMinhasNotasAluno;
	}

	/**
	 * @return the diaInicio
	 */
	public Integer getDiaInicio() {
		if (diaInicio == null) {
			diaInicio = 0;
		}
		return diaInicio;
	}

	/**
	 * @param diaInicio
	 *            the diaInicio to set
	 */
	public void setDiaInicio(Integer diaInicio) {
		this.diaInicio = diaInicio;
	}

	/**
	 * @return the diaFim
	 */
	public Integer getDiaFim() {
		if (diaFim == null) {
			diaFim = 0;
		}
		return diaFim;
	}

	/**
	 * @param diaFim
	 *            the diaFim to set
	 */
	public void setDiaFim(Integer diaFim) {
		this.diaFim = diaFim;
	}

	/**
	 * @return the frequencia
	 */
	public String getFrequencia() {
		if (frequencia == null) {
			frequencia = Constantes.EMPTY;
		}
		return frequencia;
	}

	/**
	 * @param frequencia
	 *            the frequencia to set
	 */
	public void setFrequencia(String frequencia) {
		this.frequencia = frequencia;
	}

	/**
	 * @return the mes
	 */
	public String getMes() {
		if (mes == null) {
			mes = Constantes.EMPTY;
		}
		return mes;
	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = Constantes.EMPTY;
		}
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	public ModalidadeDisciplinaEnum getModalidadeDisciplina() {
		if (modalidadeDisciplina == null) {
			modalidadeDisciplina = ModalidadeDisciplinaEnum.PRESENCIAL;
		}
		return modalidadeDisciplina;
	}

	public void setModalidadeDisciplina(ModalidadeDisciplinaEnum modalidadeDisciplina) {
		this.modalidadeDisciplina = modalidadeDisciplina;
	}


	/**
	 * @return the cargaHorariaPrevista
	 */
	public Integer getCargaHorariaPrevista() {
		if (cargaHorariaPrevista == null) {
			cargaHorariaPrevista = 0;
		}
		return cargaHorariaPrevista;
	}

	/**
	 * @param cargaHorariaPrevista
	 *            the cargaHorariaPrevista to set
	 */
	public void setCargaHorariaPrevista(Integer cargaHorariaPrevista) {
		this.cargaHorariaPrevista = cargaHorariaPrevista;
	}

	public Integer getCargaHorariaForaGrade() {
		if (cargaHorariaForaGrade == null) {
			cargaHorariaForaGrade = 0;
		}
		return cargaHorariaForaGrade;
	}

	public void setCargaHorariaForaGrade(Integer cargaHorariaForaGrade) {
		this.cargaHorariaForaGrade = cargaHorariaForaGrade;
	}

	public Integer getNumeroCreditoPrevisto() {
		if (numeroCreditoPrevisto == null) {
			numeroCreditoPrevisto = 0;
		}
		return numeroCreditoPrevisto;
	}

	public void setNumeroCreditoPrevisto(Integer numeroCreditoPrevisto) {
		this.numeroCreditoPrevisto = numeroCreditoPrevisto;
	}

	public String getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional = Constantes.EMPTY;
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	
	public String getNomeDisciplinaPeriodoLetivo() {
		if (!getDescricaoPeriodoLetivo().equals("")) {
			return getNome() + " - " + getDescricaoPeriodoLetivo();
		}
		return getNome();
	}

	/**
	 * @return the semestre
	 */
	public String getSemestre() {
		if (semestre == null) {
			semestre = Constantes.EMPTY;
		}
		return semestre;
	}

	/**
	 * @param semestre the semestre to set
	 */
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	public String getAbreviatura() {
		if (abreviatura == null) {
			abreviatura = Constantes.EMPTY;
		}
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public Boolean getPreRequisito() {
		if(preRequisito == null){
			preRequisito = false;
		}
		return preRequisito;
	}

	public void setPreRequisito(Boolean preRequisito) {
		this.preRequisito = preRequisito;
	}

	public String getDescricaoComplementar() {
		if(descricaoComplementar == null) {
			descricaoComplementar = Constantes.EMPTY;
		}
		return descricaoComplementar;
	}

	public void setDescricaoComplementar(String descricaoComplementar) {
		this.descricaoComplementar = descricaoComplementar;
	}
	
	

	public String getDataModulo() {
		return dataModulo;
	}

	public void setDataModulo(String dataModulo) {
		if(dataModulo == null) {
			dataModulo = Constantes.EMPTY;
		}
		this.dataModulo = dataModulo;
	}

	/**
	 * Implementação do equals e hashCode
	 * Importante para o uso do .distinct() da API Stream
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return Boolean.FALSE;
		}
		if (getCodigo().equals(0)) {
			return Boolean.FALSE;
		}
		DisciplinaVO disciplinaVO = (DisciplinaVO) obj;
		return getCodigo().equals(disciplinaVO.getCodigo());
	}
	
	@Override
	public int hashCode() {
		return getCodigo();
	}
	
	public String getDescricaoParaCombobox() {
		return new StringBuilder().append(getCodigo()).append(" - ").append(getNome()).toString();
	}
	
	public String descricaoAbreviaturaNome;
	public String getDescricaoAbreviaturaNome() {
		if(descricaoAbreviaturaNome == null) {
			descricaoAbreviaturaNome = getAbreviatura()+" - "+getNome();
		}
		return descricaoAbreviaturaNome;
	}


	public Boolean getDividirSalaEmGrupo() {
		if(dividirSalaEmGrupo == null) {
			dividirSalaEmGrupo = false;
		}
		return dividirSalaEmGrupo;
	}

	public void setDividirSalaEmGrupo(Boolean dividirSalaEmGrupo) {
		this.dividirSalaEmGrupo = dividirSalaEmGrupo;
	}

	public Integer getNrMaximoAlunosPorAmbientacao() {
		if (nrMaximoAlunosPorAmbientacao == null) {
			nrMaximoAlunosPorAmbientacao = 5000;
		}
		return nrMaximoAlunosPorAmbientacao;
	}

	public void setNrMaximoAlunosPorAmbientacao(Integer nrMaximoAlunosPorAmbientacao) {
		this.nrMaximoAlunosPorAmbientacao = nrMaximoAlunosPorAmbientacao;
	}

	public Integer getNrMaximoAulosPorSala() {
		if(nrMaximoAulosPorSala == null) {
			nrMaximoAulosPorSala = 1000;
		}
		return nrMaximoAulosPorSala;
	}

	public void setNrMaximoAulosPorSala(Integer nrMaximoAulosPorSala) {
		this.nrMaximoAulosPorSala = nrMaximoAulosPorSala;
	}
	
	public Integer getNrMinimoAlunosPorSala() {
		if (nrMinimoAlunosPorSala == null) {
			nrMinimoAlunosPorSala = 0;
		}
		return nrMinimoAlunosPorSala;
	}

	public void setNrMinimoAlunosPorSala(Integer nrMinimoAlunosPorSala) {
		this.nrMinimoAlunosPorSala = nrMinimoAlunosPorSala;
	}
	
	

//	public ModeloGeracaoSalaBlackboardEnum getModeloGeracaoSalaBlackboard() {
//		if(modeloGeracaoSalaBlackboard == null) {
//			modeloGeracaoSalaBlackboard = ModeloGeracaoSalaBlackboardEnum.DISCIPLINA;
//		}
//		return modeloGeracaoSalaBlackboard;
//	}
//
//	public void setModeloGeracaoSalaBlackboard(ModeloGeracaoSalaBlackboardEnum modeloGeracaoSalaBlackboard) {
//		this.modeloGeracaoSalaBlackboard = modeloGeracaoSalaBlackboard;
//	}

	

	public Double getPercentualMinimoCargaHorariaAproveitamento() {
		if (percentualMinimoCargaHorariaAproveitamento == null) {
			percentualMinimoCargaHorariaAproveitamento = 0.0;
		}
		return percentualMinimoCargaHorariaAproveitamento;
	}

	public void setPercentualMinimoCargaHorariaAproveitamento(Double percentualMinimoCargaHorariaAproveitamento) {
		this.percentualMinimoCargaHorariaAproveitamento = percentualMinimoCargaHorariaAproveitamento;
	}

	public Integer getQtdeMinimaDeAnosAproveitamento() {
		if (qtdeMinimaDeAnosAproveitamento == null) {
			qtdeMinimaDeAnosAproveitamento = 0;
		}
		return qtdeMinimaDeAnosAproveitamento;
	}

	public void setQtdeMinimaDeAnosAproveitamento(Integer qtdeMinimaDeAnosAproveitamento) {
		this.qtdeMinimaDeAnosAproveitamento = qtdeMinimaDeAnosAproveitamento;
	}

	public ClassificacaoDisciplinaEnum getClassificacaoDisciplina() {
		if(classificacaoDisciplina == null) {
			classificacaoDisciplina = ClassificacaoDisciplinaEnum.NENHUMA;
		}		
		return classificacaoDisciplina;
	}

	public void setClassificacaoDisciplina(ClassificacaoDisciplinaEnum classificacaoDisciplina) {
		this.classificacaoDisciplina = classificacaoDisciplina;
	}


	public Integer getNrMaximoAulosPorGrupo() {
		if(nrMaximoAulosPorGrupo == null) {
			nrMaximoAulosPorGrupo =  0;
		}
		return nrMaximoAulosPorGrupo;
	}

	public void setNrMaximoAulosPorGrupo(Integer nrMaximoAulosPorGrupo) {
		this.nrMaximoAulosPorGrupo = nrMaximoAulosPorGrupo;
	}

	public Integer getNrMinimoAlunosPorGrupo() {
		if (nrMinimoAlunosPorGrupo == null) {
			nrMinimoAlunosPorGrupo = 0;
		}
		return nrMinimoAlunosPorGrupo;
	}

	public void setNrMinimoAlunosPorGrupo(Integer nrMinimoAlunosPorGrupo) {
		this.nrMinimoAlunosPorGrupo = nrMinimoAlunosPorGrupo;
	}

	public String getFonteDeDadosBlackboard() {
		if (fonteDeDadosBlackboard == null) {
			fonteDeDadosBlackboard = Constantes.EMPTY;
		}
		return fonteDeDadosBlackboard;
	}

	public void setFonteDeDadosBlackboard(String fonteDeDadosBlackboard) {
		this.fonteDeDadosBlackboard = fonteDeDadosBlackboard;
	}
	
	public String getIdConteudoMasterBlackboard() {
		if (idConteudoMasterBlackboard == null) {
			idConteudoMasterBlackboard = Constantes.EMPTY;
		}
		return idConteudoMasterBlackboard;
	}

	public void setIdConteudoMasterBlackboard(String idConteudoMasterBlackboard) {
		this.idConteudoMasterBlackboard = idConteudoMasterBlackboard;
	}

	public boolean isApresentarCamposNrSalaGrupos() {
		return getClassificacaoDisciplina().isTcc() || getClassificacaoDisciplina().isProjetoIntegrador(); 
	}

	public GrupoPessoaVO getGrupoPessoaVO() {
		if (grupoPessoaVO == null) {
			grupoPessoaVO = new GrupoPessoaVO();
		}
		return grupoPessoaVO;
	}

	public void setGrupoPessoaVO(GrupoPessoaVO grupoPessoaVO) {
		this.grupoPessoaVO = grupoPessoaVO;
	}
	
	public List<Integer> getDisciplinasEquivalentes() {
		if (disciplinasEquivalentes == null) {
			disciplinasEquivalentes = new ArrayList<>(0);
		}
		return disciplinasEquivalentes;
	}
	
	public void setDisciplinasEquivalentes(List<Integer> disciplinasEquivalentes) {
		this.disciplinasEquivalentes = disciplinasEquivalentes;
	}
}
