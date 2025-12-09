package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import negocio.facade.jdbc.academico.GradeCurricular;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade GradeDisciplina. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see GradeCurricular
 */
@XmlRootElement(name = "gradeDisciplinaVO")
public class GradeDisciplinaVO extends SuperVO {

	private Integer periodoLetivo;
	private Integer codigo;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Disciplina </code>.
	 */
	private DisciplinaVO disciplina;
	private DisciplinaVO disciplinaEixoTematico;
	private List<DisciplinaPreRequisitoVO> disciplinaRequisitoVOs;
	private Boolean preRequisito;
	private Boolean disciplinaTCC;
	private ModalidadeDisciplinaEnum modalidadeDisciplina;
	// Transiente
	private PeriodoLetivoVO periodoLetivoVO;
	public static final long serialVersionUID = 1L;
	private Integer ordem;
	private Integer cargaHoraria;
	private Integer cargaHorariaPratica;
	private Integer cargaHorariaTeorica;
	private Integer cargaHorariaTotalPraticaTeorica;
	private Integer nrCreditos;
	private Integer horaAula;
	private Double nrCreditoFinanceiro;
	private String tipoDisciplina;
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	private Boolean diversificada;
	private Boolean disciplinaComposta;
	private List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs;
	/**
	 * TRANSIENTE UTILIZADO NA RENOVACAO DE MATRICULA PARA INDICAR SE A MESMA JA
	 * FOI INCLUIDA OU NAO PELO USUARIO
	 */
	private Boolean jaIncluidaRenovacao;

	/**
	 * TRANSIENTE UTILIZADO PARA MONTAR DADOS NA TELA DE APROVEITAMENTO DE
	 * DISCIPLINA
	 */
	private DisciplinasAproveitadasVO disciplinasAproveitadasVO;
	private Boolean selecionadoAproveitamento;
	private Boolean aproveitamentoIncluidoPorMapaEquivalencia;
	private HistoricoVO historicoAtualAluno;

	/**
	 * TRANSIENTE UTILIZADO PARA MONTAR DADOS NA TELA DE INCLUSÃO DE DISCIPLINA
	 * FORA DO PRAZO
	 */
	private Boolean selecionado;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO;
	private List<HistoricoVO> historicosDuplicadosAluno;
	/**
	 * Este campo define qual regra será aplicada no calculo da nota da
	 * disciplina composta(MAE)
	 */
	private FormulaCalculoNotaEnum formulaCalculoNota;
	/**
	 * Este campo é utilizado quando a formulaCalculoNota for do tipo Formula de
	 * Calculo
	 */
	private String formulaCalculo;
	
	/**
	 * Este campo é utilizado para Prova presencial
	 */
	
	/**
	 * @Transient
	 * Campo não persistido no banco, criado para atender UniRV,
	 * pois existem casos em que se deve exibir zero e outros casos que deve exibir vazio, quando valor estiver nulo no banco.
	 * @return
	 */
	private String nrCreditosSemSingleton;
	
	/**
	 * Campo criado para diferenciar nome da disciplina no layout 6 do Histórico Aluno e no layout 3 do relatório de Disciplinas Matriz Curricular.
	 */
	private String nomeChancela;
	/**
	 * Utilizado apenas quando o atributo disciplinaComposta for true.
	 */
	private TipoControleComposicaoEnum tipoControleComposicao;
	/**
	 * Utilizado apenas quando o <code>TipoControleComposicaoEnum</code> for ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA e o atributo disciplinaComposta for
	 * true.
	 */
	private Integer numeroMaximoDisciplinaComposicaoEstudar;
	private Integer numeroMinimoDisciplinaComposicaoEstudar;
	/**
	 * Utilizado apenas quando o atributo disciplinaComposta for true.
	 */
	private Boolean validarPreRequisitoDisciplinaFazParteComposicao;
	
	private Boolean disciplinaEstagio;
	
	
    /**
     * Habilita o recurso de controle de recuperação das disciplinas filhas com base nas regras da disciplina mae
     */
	private Boolean controlarRecuperacaoPelaDisciplinaPrincipal;
	/**
	 * A informação deste campo deve existir nas configurações academicas vinculadas as disciplinas filhas da composição e na disciplina mae
	 */
	private String variavelNotaRecuperacao;
	/**
	 * Deve ser utilizado como variavel nesta formula a variavel informada na gradedisciplinacomposta, o sistema
	 * irá substituir pela nota informada no variavel definida no campo variavelNotaCondicaoUsoRecuperacao, 
	 * Esta formula verifica se o aluno deverá ficar de recuperação ou não nas disciplinas filhas.
	 * 
	 */
	private String condicaoUsoRecuperacao;
	/**
	 * Corresponde a variável da nota que será obtida o valor a ser substituida na condicaoUsoRecuperacao
	 */
	private String variavelNotaCondicaoUsoRecuperacao;
	/**
	 * Deve ser utilizado como variavel nesta formula a variavel informada na gradedisciplinacomposta, o sistema
	 * irá substituir pela nota informada no variavel definida no campo variavelNotaFormulaCalculoNotaRecuperada, 
	 * esta formula tem a finalidade para definir se o aluno recuperou a nota ou não
	 */
	private String formulaCalculoNotaRecuperada;
	/**
	 * Corresponde a variável da nota que será obtida o valor a ser substituida na formulaCalculoNotaRecuperada
	 */
	private String variavelNotaFormulaCalculoNotaRecuperada;
	/**
	 * Deve ser utilizado como nesta formula a variavel informada na gradedisciplinacomposta, o sistema
	 * irá substituir pela nota informada no variavel definida no campo variavelNotaRecuperacao, 
	 * esta formula tem a finalidade para definir a nota que será lançada na nota REC da disciplina mãe
	 */
	private String formulaCalculoNotaRecuperacao;
	
	private Boolean sofreuAlteracaoMatrizAtivaInativa;
	private Boolean deveValidarImpactoExclusao;
	private List<HistoricoVO> listaHistoricoImpactoAlteracaoVOs;
	private List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs;
	private Integer bimestre;
	
	private boolean gradeDisciplinaOfertada = false;
	private Boolean utilizarEmissaoXmlDiploma;

	/**
	 * Construtor padrão da classe <code>GradeDisciplina</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public GradeDisciplinaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>GradeDisciplinaVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(GradeDisciplinaVO obj, String regime, String situacaoGradeCurricular) throws ConsistirException {
		if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo DISCIPLINA (Disciplinas Grade Curricular) deve ser informado.");
		}

		// if (regime.equals("CR")) {
		// if (obj.getNrCreditos().intValue() == 0) {
		// throw new
		// ConsistirException("A DISCIPLINA a ser adicionada não pode ter o nrº de créditos igual a zero (Disciplinas Grade Curricular).");
		// }
		// }
		if (regime.equals("CH")) {
			if (obj.getCargaHoraria().intValue() == 0) {
				throw new ConsistirException("O campo CARGA HORÁRIA (Disciplina: " + obj.getDisciplina().getCodigo() + " - " + obj.getDisciplina().getNome() + ") do PERÍODO LETIVO " + obj.getPeriodoLetivoVO().getDescricao() + " deve ser informado.");
			}
		}
		if (obj.getDisciplinaComposta() && obj.getGradeDisciplinaCompostaVOs().isEmpty()) {
			throw new ConsistirException("O campo DISCIPLINA COMPOSTA deve ser informado para a disciplina " + obj.getDisciplina().getNome() + ".");
		} else if (obj.getDisciplinaComposta() && obj.getGradeDisciplinaCompostaVOs().size() == 1) {
			if (!situacaoGradeCurricular.equals("")) {
				if (situacaoGradeCurricular.equals("AT") || situacaoGradeCurricular.equals("CO")) {
					throw new ConsistirException("Deve ser informado no mínimo 2 DISCIPLINAS COMPOSTAS para a disciplina " + obj.getDisciplina().getNome() + ".");
				}
			}
			// throw new
			// ConsistirException("Deve ser informado no mínimo 2 DISCIPLINAS COMPOSTAS para a disciplina "
			// + obj.getDisciplina().getNome() + ".");
		} else if (!obj.getDisciplinaComposta()) {
			obj.getGradeDisciplinaCompostaVOs().clear();
		}
		if (obj.getDisciplinaComposta() && TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA.equals(obj.getTipoControleComposicao())) {
			if(obj.getNumeroMinimoDisciplinaComposicaoEstudar() == 0){
				throw new ConsistirException("Deve ser informado a quantidade mínima de DISCIPLINAS COMPOSTAS para a disciplina " + obj.getDisciplina().getNome() + ".");
			}
			if(obj.getNumeroMaximoDisciplinaComposicaoEstudar() == 0){
				throw new ConsistirException("Deve ser informado a quantidade máxima de DISCIPLINAS COMPOSTAS para a disciplina " + obj.getDisciplina().getNome() + ".");
			}
			if (obj.getNumeroMaximoDisciplinaComposicaoEstudar() < obj.getNumeroMinimoDisciplinaComposicaoEstudar()) {
				throw new ConsistirException("A quantidade máxima de DISCIPLINAS COMPOSTAS a ser estudada deve ser MAIOR ou IGUAL a quantidade mínima as ser estudada para a disciplina " + obj.getDisciplina().getNome() + ".");
			}
			if (obj.getNumeroMaximoDisciplinaComposicaoEstudar() > obj.getGradeDisciplinaCompostaVOs().size()) {
				throw new ConsistirException(
						UteisJSF.internacionalizar("msg_GradeDisciplina_numeroMaximoDisciplinaComposicaoEstudarMaiorDisciplinasCompostas")
						.replace("{0}", obj.getPeriodoLetivoVO().getDescricao())
						.replace("{1}", obj.getDisciplina().getNome())
						.replace("{2}", String.valueOf(obj.getGradeDisciplinaCompostaVOs().size()))
						.replace("{3}", String.valueOf(obj.getNumeroMaximoDisciplinaComposicaoEstudar())));
			}
		}
		if(!obj.getDisciplinaComposta() || (obj.getDisciplinaComposta() && (obj.getTipoControleComposicao().equals(TipoControleComposicaoEnum.ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA)))){
			obj.setControlarRecuperacaoPelaDisciplinaPrincipal(false);
			obj.setCondicaoUsoRecuperacao("");
			obj.setVariavelNotaCondicaoUsoRecuperacao("");
			obj.setFormulaCalculoNotaRecuperada("");
			obj.setVariavelNotaRecuperacao("");
			obj.setVariavelNotaFormulaCalculoNotaRecuperada("");
		}
		if(obj.getControlarRecuperacaoPelaDisciplinaPrincipal()){
			if(obj.getVariavelNotaRecuperacao().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNotaRecuperacao").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getCondicaoUsoRecuperacao().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_condicaoUsoRecuperacao").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getVariavelNotaCondicaoUsoRecuperacao().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNotaCondicaoUsoRecuperacao").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getFormulaCalculoNotaRecuperada().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_formulaCalculoNotaRecuperada").replace("{0}",  obj.getDisciplina().getNome()));
			}
			if(obj.getVariavelNotaFormulaCalculoNotaRecuperada().trim().isEmpty()){
				throw new ConsistirException(UteisJSF.internacionalizar("msg_GradeDisciplinaComposta_variavelNotaFormulaCalculoNotaRecuperada").replace("{0}",  obj.getDisciplina().getNome()));
			}
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setPreRequisito(Boolean.FALSE);
		setCodigo(0);
		setPreRequisito(Boolean.FALSE);
	}

	public void adicionarObjDisciplinaPreRequisitoVOs(DisciplinaPreRequisitoVO obj) throws Exception {
		DisciplinaPreRequisitoVO.validarDados(obj);
		int index = 0;
		Iterator i = getDisciplinaRequisitoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getDisciplinaRequisitoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getDisciplinaRequisitoVOs().add(obj);

		// adicionarObjSubordinadoOC
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>DisciplinaPreRequisitoVO</code> no List
	 * <code>disciplinaPreRequisitoVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>DisciplinaPreRequisito</code> -
	 * getPreRequisito().getCodigo() - como identificador (key) do objeto no
	 * List.
	 * 
	 * @param preRequisito
	 *            Parâmetro para localizar o objeto do List.
	 */
	public DisciplinaPreRequisitoVO consultarObjDisciplinaPreRequisitoVO(Integer preRequisito) throws Exception {
		Iterator i = getDisciplinaRequisitoVOs().iterator();
		while (i.hasNext()) {
			DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(preRequisito)) {
				return objExistente;
			}
		}
		return null;
		// consultarObjSubordinadoOC
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<DisciplinaPreRequisitoVO> getDisciplinaRequisitoVOs() {
		if (disciplinaRequisitoVOs == null) {
			disciplinaRequisitoVOs = new ArrayList<DisciplinaPreRequisitoVO>();
		}
		return disciplinaRequisitoVOs;
	}

	public void setDisciplinaRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaRequisitoVOs) {
		this.disciplinaRequisitoVOs = disciplinaRequisitoVOs;
	}
	
	public JRDataSource getGradeDisciplinaCompostaVOsJRDataSource() {
		return new JRBeanArrayDataSource(getGradeDisciplinaCompostaVOs().toArray());
	}

	/**
	 * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
	 * <code>GradeDisciplina</code>).
	 */
	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return (disciplina);
	}

	public Boolean getPreRequisito() {
		return preRequisito;
	}

	public void setPreRequisito(Boolean preRequisito) {
		this.preRequisito = preRequisito;
	}

	/**
	 * Define o objeto da classe <code>Disciplina</code> relacionado com (
	 * <code>GradeDisciplina</code>).
	 */
	public void setDisciplina(DisciplinaVO obj) {
		this.disciplina = obj;
	}

	public Integer getPeriodoLetivo() {
		return (periodoLetivo);
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public String getOrdenacao() {
		return getDisciplina().getNome();
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

	public Boolean getDisciplinaTCC() {
		if (disciplinaTCC == null) {
			disciplinaTCC = false;
		}
		return disciplinaTCC;
	}

	public void setDisciplinaTCC(Boolean disciplinaTCC) {
		this.disciplinaTCC = disciplinaTCC;
	}

	public DisciplinaVO getDisciplinaEixoTematico() {
		if (disciplinaEixoTematico == null) {
			disciplinaEixoTematico = new DisciplinaVO();
		}
		return disciplinaEixoTematico;
	}

	public void setDisciplinaEixoTematico(DisciplinaVO disciplinaEixoTematico) {
		this.disciplinaEixoTematico = disciplinaEixoTematico;
	}

	public PeriodoLetivoVO getPeriodoLetivoVO() {
		if (periodoLetivoVO == null) {
			periodoLetivoVO = new PeriodoLetivoVO();
		}
		return periodoLetivoVO;
	}

	public void setPeriodoLetivoVO(PeriodoLetivoVO periodoLetivoVO) {
		this.periodoLetivoVO = periodoLetivoVO;
	}

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Integer getCargaHorariaPratica() {
		if (cargaHorariaPratica == null) {
			cargaHorariaPratica = 0;
		}
		return cargaHorariaPratica;
	}

	public void setCargaHorariaPratica(Integer cargaHorariaPratica) {
		this.cargaHorariaPratica = cargaHorariaPratica;
	}

	public Integer getNrCreditos() {
		if (nrCreditos == null) {
			nrCreditos = 0;
		}
		return nrCreditos;
	}
	
	public void setNrCreditos(Integer nrCreditos) {
		this.nrCreditos = nrCreditos;
	}
	
	public String getNrCreditosSemSingleton() {
//		if (nrCreditosSemSingleton == null) {
//		return "";
//	}
//	return nrCreditosSemSingleton;
	return getNrCreditos().toString();
	}
	
	public void setNrCreditosSemSingleton(String nrCreditosSemSingleton) {
		this.nrCreditosSemSingleton = nrCreditosSemSingleton;
	}

	public String getTipoDisciplina() {
		if (tipoDisciplina == null) {
			tipoDisciplina = TipoDisciplina.OBRIGATORIA.getValor();
		}
		return tipoDisciplina;
	}

	public void setTipoDisciplina(String tipoDisciplina) {
		this.tipoDisciplina = tipoDisciplina;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}

	public Boolean getDiversificada() {
		if (diversificada == null) {
			diversificada = Boolean.FALSE;
		}
		return diversificada;
	}

	public void setDiversificada(Boolean diversificada) {
		this.diversificada = diversificada;
	}

	public Integer getCargaHorariaTeorica() {
		if (cargaHorariaTeorica == null) {
			cargaHorariaTeorica = 0;
		}
		return cargaHorariaTeorica;
	}

	public void setCargaHorariaTeorica(Integer cargaHorariaTeorica) {
		this.cargaHorariaTeorica = cargaHorariaTeorica;
	}

	public String getTipoDisciplina_Apresentar() {
		return TipoDisciplina.getDescricao(tipoDisciplina);
	}

	public Boolean getIsDisciplinaOptativa() {
		if ((getTipoDisciplina().equals("LO")) || (getTipoDisciplina().equals("OP"))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Double getNrCreditoFinanceiro() {
		if (nrCreditoFinanceiro == null) {
			nrCreditoFinanceiro = 0.0;
		}
		return nrCreditoFinanceiro;
	}

	public void setNrCreditoFinanceiro(Double nrCreditoFinanceiro) {
		this.nrCreditoFinanceiro = nrCreditoFinanceiro;
	}

	public List<GradeDisciplinaCompostaVO> getGradeDisciplinaCompostaVOs() {
		if (gradeDisciplinaCompostaVOs == null) {
			gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
		}
		return gradeDisciplinaCompostaVOs;
	}

	public void setGradeDisciplinaCompostaVOs(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) {
		this.gradeDisciplinaCompostaVOs = gradeDisciplinaCompostaVOs;
	}

	public Boolean getDisciplinaComposta() {
		if (disciplinaComposta == null) {
			disciplinaComposta = false;
		}
		return disciplinaComposta;
	}

	public void setDisciplinaComposta(Boolean disciplinaComposta) {
		this.disciplinaComposta = disciplinaComposta;
	}

	/**
	 * @return the jaIncluidaRenovacao
	 */
	public Boolean getJaIncluidaRenovacao() {
		if (jaIncluidaRenovacao == null) {
			jaIncluidaRenovacao = Boolean.FALSE;
		}
		return jaIncluidaRenovacao;
	}

	/**
	 * @param jaIncluidaRenovacao
	 *            the jaIncluidaRenovacao to set
	 */
	public void setJaIncluidaRenovacao(Boolean jaIncluidaRenovacao) {
		this.jaIncluidaRenovacao = jaIncluidaRenovacao;
	}

	public DisciplinasAproveitadasVO getDisciplinasAproveitadasVO() {
		if (disciplinasAproveitadasVO == null) {
			disciplinasAproveitadasVO = new DisciplinasAproveitadasVO();
		}
		return disciplinasAproveitadasVO;
	}

	public void setDisciplinasAproveitadasVO(DisciplinasAproveitadasVO disciplinasAproveitadasVO) {
		this.disciplinasAproveitadasVO = disciplinasAproveitadasVO;
	}

	public Boolean getSelecionadoAproveitamento() {
		if (selecionadoAproveitamento == null) {
			selecionadoAproveitamento = Boolean.FALSE;
		}
		return selecionadoAproveitamento;
	}

	public void setSelecionadoAproveitamento(Boolean selecionadoAproveitamento) {
		this.selecionadoAproveitamento = selecionadoAproveitamento;
	}

	/**
	 * @return the historicoAtualAluno
	 */
	public HistoricoVO getHistoricoAtualAluno() {
		if (historicoAtualAluno == null) {
			historicoAtualAluno = new HistoricoVO();
		}
		return historicoAtualAluno;
	}

	/**
	 * @param historicoAtualAluno
	 *            the historicoAtualAluno to set
	 */
	public void setHistoricoAtualAluno(HistoricoVO historicoAtualAluno) {
		this.historicoAtualAluno = historicoAtualAluno;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = new MatriculaPeriodoTurmaDisciplinaVO();
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}

	public String getTurma_Apresentar() {
		return getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getIdentificadorTurma();
	}

	public String getPreRequisitoApresentar() {
		String valor = "";
		Ordenacao.ordenarLista(getDisciplinaRequisitoVOs(), "codigo");
		for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : getDisciplinaRequisitoVOs()) {
			if (valor.trim().isEmpty()) {
				valor = disciplinaPreRequisitoVO.getDisciplina().getCodigo() + " - " + disciplinaPreRequisitoVO.getDisciplina().getNome();
			} else {
				valor += ", " + disciplinaPreRequisitoVO.getDisciplina().getCodigo() + " - " + disciplinaPreRequisitoVO.getDisciplina().getNome();
			}
		}
		return valor;
	}
	
	/**
	 * Método elaborado para atender layout 3, que exibe apenas a abreviatura da disciplina
	 * @return
	 */
	public String getAbreviaturaPreRequisitoApresentar() {
		String valor = "";
		Ordenacao.ordenarLista(getDisciplinaRequisitoVOs(), "codigo");
		String abrev = "";
		for (DisciplinaPreRequisitoVO disciplinaPreRequisitoVO : getDisciplinaRequisitoVOs()) {
			abrev = disciplinaPreRequisitoVO.getDisciplina().getAbreviatura();
			if (abrev.trim().isEmpty()) {
				abrev = disciplinaPreRequisitoVO.getDisciplina().getCodigo().toString();
			}
			if (valor.trim().isEmpty()) {
				valor = abrev;
			} else {
				valor += ", " + abrev;
			}
		}
		return valor;
	}

	public List<HistoricoVO> getHistoricosDuplicadosAluno() {
		if (historicosDuplicadosAluno == null) {
			historicosDuplicadosAluno = new ArrayList<HistoricoVO>(0);
		}
		return historicosDuplicadosAluno;
	}

	public void setHistoricosDuplicadosAluno(List<HistoricoVO> historicosDuplicadosAluno) {
		this.historicosDuplicadosAluno = historicosDuplicadosAluno;
	}

	/**
	 * @return the formulaCalculoNota
	 */
	public FormulaCalculoNotaEnum getFormulaCalculoNota() {
		if (formulaCalculoNota == null) {
			formulaCalculoNota = FormulaCalculoNotaEnum.MEDIA;
		}
		return formulaCalculoNota;
	}

	/**
	 * @param formulaCalculoNota
	 *            the formulaCalculoNota to set
	 */
	public void setFormulaCalculoNota(FormulaCalculoNotaEnum formulaCalculoNota) {
		this.formulaCalculoNota = formulaCalculoNota;
	}

	public String getFormulaCalculo() {
		if (formulaCalculo == null) {
			formulaCalculo = "";
		}
		return formulaCalculo;
	}

	public void setFormulaCalculo(String formulaCalculo) {
		this.formulaCalculo = formulaCalculo;
	}

	public Boolean getIsUtilizaFormulaCalculo(){
		return getDisciplinaComposta() && getFormulaCalculoNota().equals(FormulaCalculoNotaEnum.FORMULA_CALCULO);
	}

//	public AreaConhecimentoVO getAreaConhecimentoVO() {
//		if (areaConhecimentoVO == null) {
//			areaConhecimentoVO = new AreaConhecimentoVO();
//		}
//		return areaConhecimentoVO;
//	}
//
//	public void setAreaConhecimentoVO(AreaConhecimentoVO areaConhecimentoVO) {
//		this.areaConhecimentoVO = areaConhecimentoVO;
//	}

	public Integer getHoraAula() {
		if (horaAula == null) {
			horaAula = 0;
		}
		return horaAula;
	}

	public void setHoraAula(Integer horaAula) {
		this.horaAula = horaAula;
	}

	public String getNomeChancela() {
		if (nomeChancela == null) {
			nomeChancela = "";
		}
		return nomeChancela;
	}

	public void setNomeChancela(String nomeChancela) {
		this.nomeChancela = nomeChancela;
	}

	public TipoControleComposicaoEnum getTipoControleComposicao() {
		if (tipoControleComposicao == null) {
			tipoControleComposicao = TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS;
		}
		return tipoControleComposicao;
	}

	public void setTipoControleComposicao(TipoControleComposicaoEnum tipoControleComposicao) {
		this.tipoControleComposicao = tipoControleComposicao;
	}

	public Integer getNumeroMaximoDisciplinaComposicaoEstudar() {
		if (numeroMaximoDisciplinaComposicaoEstudar == null) {
			numeroMaximoDisciplinaComposicaoEstudar = 0;
		}
		return numeroMaximoDisciplinaComposicaoEstudar;
	}

	public void setNumeroMaximoDisciplinaComposicaoEstudar(Integer numeroMaximoDisciplinaComposicaoEstudar) {
		this.numeroMaximoDisciplinaComposicaoEstudar = numeroMaximoDisciplinaComposicaoEstudar;
	}

	public Boolean getValidarPreRequisitoDisciplinaFazParteComposicao() {
		if (validarPreRequisitoDisciplinaFazParteComposicao == null) {
			validarPreRequisitoDisciplinaFazParteComposicao = false;
		}
		return validarPreRequisitoDisciplinaFazParteComposicao;
	}

	public void setValidarPreRequisitoDisciplinaFazParteComposicao(Boolean validarPreRequisitoDisciplinaFazParteComposicao) {
		this.validarPreRequisitoDisciplinaFazParteComposicao = validarPreRequisitoDisciplinaFazParteComposicao;
	}
	
	public Boolean getDisciplinaEstagio() {
		if (disciplinaEstagio == null) {
			disciplinaEstagio = false;
		}
		return disciplinaEstagio;
	}

	public void setDisciplinaEstagio(Boolean disciplinaEstagio) {
		this.disciplinaEstagio = disciplinaEstagio;
	}

	public Boolean getAproveitamentoIncluidoPorMapaEquivalencia() {
		if (aproveitamentoIncluidoPorMapaEquivalencia == null) {
			aproveitamentoIncluidoPorMapaEquivalencia = Boolean.FALSE;
		}
		return aproveitamentoIncluidoPorMapaEquivalencia;
	}

	public void setAproveitamentoIncluidoPorMapaEquivalencia(Boolean aproveitamentoIncluidoPorMapaEquivalencia) {
		this.aproveitamentoIncluidoPorMapaEquivalencia = aproveitamentoIncluidoPorMapaEquivalencia;
	}


	public Boolean getControlarRecuperacaoPelaDisciplinaPrincipal() {
		if(controlarRecuperacaoPelaDisciplinaPrincipal == null){
			controlarRecuperacaoPelaDisciplinaPrincipal = false;
		}
		return controlarRecuperacaoPelaDisciplinaPrincipal;
	}

	public void setControlarRecuperacaoPelaDisciplinaPrincipal(Boolean controlarRecuperacaoPelaDisciplinaPrincipal) {
		this.controlarRecuperacaoPelaDisciplinaPrincipal = controlarRecuperacaoPelaDisciplinaPrincipal;
	}

	public String getCondicaoUsoRecuperacao() {
		if(condicaoUsoRecuperacao == null){
			condicaoUsoRecuperacao = "";
		}
		return condicaoUsoRecuperacao;
	}

	public void setCondicaoUsoRecuperacao(String condicaoUsoRecuperacao) {
		this.condicaoUsoRecuperacao = condicaoUsoRecuperacao;
	}

	public String getVariavelNotaCondicaoUsoRecuperacao() {
		if(variavelNotaCondicaoUsoRecuperacao == null){
			variavelNotaCondicaoUsoRecuperacao = "";
		}
		return variavelNotaCondicaoUsoRecuperacao;
	}

	public void setVariavelNotaCondicaoUsoRecuperacao(String variavelNotaCondicaoUsoRecuperacao) {
		this.variavelNotaCondicaoUsoRecuperacao = variavelNotaCondicaoUsoRecuperacao;
	}

	public String getFormulaCalculoNotaRecuperada() {
		if(formulaCalculoNotaRecuperada == null){
			formulaCalculoNotaRecuperada = "";
		}
		return formulaCalculoNotaRecuperada;
	}

	public void setFormulaCalculoNotaRecuperada(String formulaCalculoNotaRecuperada) {
		this.formulaCalculoNotaRecuperada = formulaCalculoNotaRecuperada;
	}

	public String getVariavelNotaFormulaCalculoNotaRecuperada() {
		if(variavelNotaFormulaCalculoNotaRecuperada == null){
			variavelNotaFormulaCalculoNotaRecuperada = "";
		}
		return variavelNotaFormulaCalculoNotaRecuperada;
	}

	public void setVariavelNotaFormulaCalculoNotaRecuperada(String variavelNotaFormulaCalculoNotaRecuperada) {
		this.variavelNotaFormulaCalculoNotaRecuperada = variavelNotaFormulaCalculoNotaRecuperada;
	}

	public String getVariavelNotaRecuperacao() {
		if(variavelNotaRecuperacao == null){
			variavelNotaRecuperacao = "";
		}
		return variavelNotaRecuperacao;
	}

	public void setVariavelNotaRecuperacao(String variavelNotaRecuperacao) {
		this.variavelNotaRecuperacao = variavelNotaRecuperacao;
	}
		
	public Boolean getApresentarOpcaoRecuperacao(){
		return getDisciplinaComposta();
	}
	
	
	public String getFormulaCalculoNotaRecuperacao() {
		if(formulaCalculoNotaRecuperacao == null){
			formulaCalculoNotaRecuperacao = "";
		}
		return formulaCalculoNotaRecuperacao;
	}

	public void setFormulaCalculoNotaRecuperacao(String formulaCalculoNotaRecuperacao) {
		this.formulaCalculoNotaRecuperacao = formulaCalculoNotaRecuperacao;
	}
	

	public Integer getNumeroMinimoDisciplinaComposicaoEstudar() {
		if(numeroMinimoDisciplinaComposicaoEstudar == null){
			numeroMinimoDisciplinaComposicaoEstudar = 1;
		}
		return numeroMinimoDisciplinaComposicaoEstudar;
	}

	public void setNumeroMinimoDisciplinaComposicaoEstudar(Integer numeroMinimoDisciplinaComposicaoEstudar) {
		this.numeroMinimoDisciplinaComposicaoEstudar = numeroMinimoDisciplinaComposicaoEstudar;
	}

	public Integer getOrdem() {
		if(ordem == null){
			ordem = 0;
		}
		return ordem;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public Boolean getApresentarSetaCima() {
		if (this.getOrdem() > 1) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "Grade Disciplina: " + this.getCodigo() + " Periodo Letivo: " + this.getPeriodoLetivoVO().getDescricao();
	}

	public Integer getCargaHorariaTotalPraticaTeorica() {
		if(cargaHorariaTotalPraticaTeorica == null) {
			cargaHorariaTotalPraticaTeorica = 0;
		}
		return cargaHorariaTotalPraticaTeorica;
	}

	public void setCargaHorariaTotalPraticaTeorica(Integer cargaHorariaTotalPraticaTeorica) {
		this.cargaHorariaTotalPraticaTeorica = cargaHorariaTotalPraticaTeorica;
	}
		
	public Boolean getSofreuAlteracaoMatrizAtivaInativa() {
		if (sofreuAlteracaoMatrizAtivaInativa == null) {
			sofreuAlteracaoMatrizAtivaInativa = false;
		}
		return sofreuAlteracaoMatrizAtivaInativa;
	}

	public void setSofreuAlteracaoMatrizAtivaInativa(Boolean sofreuAlteracaoMatrizAtivaInativa) {
		this.sofreuAlteracaoMatrizAtivaInativa = sofreuAlteracaoMatrizAtivaInativa;
	}

	public Boolean getDeveValidarImpactoExclusao() {
		if (deveValidarImpactoExclusao == null) {
			deveValidarImpactoExclusao = true;
		}
		return deveValidarImpactoExclusao;
	}

	public void setDeveValidarImpactoExclusao(Boolean deveValidarImpactoExclusao) {
		this.deveValidarImpactoExclusao = deveValidarImpactoExclusao;
	}
	
	public String validarAlteracaoMatrizCurricularAtivaInativa(GradeDisciplinaVO gradeDisciplinaAntigaVO) {
		StringBuilder sb = new StringBuilder();
		
		if(!gradeDisciplinaAntigaVO.getDisciplina().getCodigo().equals(this.getDisciplina().getCodigo())) {
			sb.append("\n Alterado Código: ").append(gradeDisciplinaAntigaVO.getDisciplina().toString()).append(" -> ").append(this.getDisciplina().toString());
		}
		if(!gradeDisciplinaAntigaVO.getCargaHoraria().equals(this.getCargaHoraria())) {
			sb.append("\n Alterado CH Total ").append(gradeDisciplinaAntigaVO.getCargaHoraria()).append("h").append(" -> ").append(this.getCargaHoraria()).append("h");
		}
		if(!gradeDisciplinaAntigaVO.getHoraAula().equals(this.getHoraAula())) {
			sb.append("\n Alterado Hora Aula ").append(gradeDisciplinaAntigaVO.getHoraAula()).append("h").append(" -> ").append(this.getHoraAula()).append("h");
		}
		if(!gradeDisciplinaAntigaVO.getNrCreditos().equals(this.getNrCreditos())) {
			sb.append("\n Alterado Nr. de Créditos ").append(gradeDisciplinaAntigaVO.getNrCreditos()).append(" -> ").append(this.getNrCreditos());
		}

		if(!gradeDisciplinaAntigaVO.getDisciplina().getCodigo().equals(this.getDisciplina().getCodigo())) {
			sb.append("\n Alterado Disciplina ").append(gradeDisciplinaAntigaVO.getDisciplina().getNome()).append(" -> ").append(this.getDisciplina());
		}
		
		return sb.toString();
	}
	
	public String getDescricaoCodigoNomeDisciplina() {
		return this.getDisciplina().getCodigo() + " - " + this.getDisciplina().getNome();
	}

	public List<HistoricoVO> getListaHistoricoImpactoAlteracaoVOs() {
		if (listaHistoricoImpactoAlteracaoVOs == null) {
			listaHistoricoImpactoAlteracaoVOs = new ArrayList<HistoricoVO>(0);
		}
		return listaHistoricoImpactoAlteracaoVOs;
	}

	public void setListaHistoricoImpactoAlteracaoVOs(List<HistoricoVO> listaHistoricoImpactoAlteracaoVOs) {
		this.listaHistoricoImpactoAlteracaoVOs = listaHistoricoImpactoAlteracaoVOs;
	}

	public List<LogImpactoMatrizCurricularVO> getListaLogImpactoGradeDisciplinaVOs() {
		if (listaLogImpactoGradeDisciplinaVOs == null) {
			listaLogImpactoGradeDisciplinaVOs = new ArrayList<LogImpactoMatrizCurricularVO>(0);
		}
		return listaLogImpactoGradeDisciplinaVOs;
	}

	public void setListaLogImpactoGradeDisciplinaVOs(List<LogImpactoMatrizCurricularVO> listaLogImpactoGradeDisciplinaVOs) {
		this.listaLogImpactoGradeDisciplinaVOs = listaLogImpactoGradeDisciplinaVOs;
	}

	public Integer getBimestre() {
		if(bimestre == null) {
			bimestre = 0;
		}
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}

	public boolean isGradeDisciplinaOfertada() {
		return gradeDisciplinaOfertada;
	}

	public void setGradeDisciplinaOfertada(boolean gradeDisciplinaOfertada) {
		this.gradeDisciplinaOfertada = gradeDisciplinaOfertada;
	}
	
	public Boolean getUtilizarEmissaoXmlDiploma() {
		if (utilizarEmissaoXmlDiploma == null) {
			utilizarEmissaoXmlDiploma = Boolean.TRUE;
		}
		return utilizarEmissaoXmlDiploma;
	}
	
	public void setUtilizarEmissaoXmlDiploma(Boolean utilizarEmissaoXmlDiploma) {
		this.utilizarEmissaoXmlDiploma = utilizarEmissaoXmlDiploma;
	}
	
	
}
