package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ResultadoProcessoSeletivo. Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "resultadoProcessoSeletivoVO")
public class ResultadoProcessoSeletivoVO extends SuperVO {

	protected Integer codigo;
	private String resultadoPrimeiraOpcao;
	private String resultadoSegundaOpcao;
	private String resultadoTerceiraOpcao;
	private String respostaProvaProcessoSeletivo;
	private Double mediaNotasProcSeletivo;
	private Double mediaPonderadaNotasProcSeletivo;
	private Double somatorioAcertos;
	private Date dataRegistro;
	private Integer qtdeColuna;
	private Integer quantidadeQuestao;
	private Double notaRedacao;
	private String redacao;
	/**
	 * Atributo responsável por manter os objetos da classe <code>ResultadoDisciplinaProcSeletivo</code>.
	 */
	private List<ResultadoDisciplinaProcSeletivoVO> resultadoDisciplinaProcSeletivoVOs;
	private List<ResultadoProcessoSeletivoGabaritoRespostaVO> resultadoProcessoSeletivoGabaritoRespostaVOs;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Inscricao </code>.
	 */
	private InscricaoVO inscricao;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
	 */
	private UsuarioVO responsavel;

	private List<ColunaGabaritoVO> colunaGabaritoVOs;
	private Boolean enviaMensagemResultadoProcessoSeletivo;
	private Boolean enviaMensagemAprovacaoProcessoseletivo;
	private GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO;
	private List<ResultadoProcessoSeletivoProvaRespostaVO> resultadoProcessoSeletivoProvaRespostaVOs;
	/*
	 * transient
	 */
	private Integer classificacao;
	private String descricaoClassificacaoCand;
	private boolean algumaNotaAbaixoMinimo = false;
	private ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO;
	private Double somatorioErros;
	private String navegadorAcesso;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>ResultadoProcessoSeletivo</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public ResultadoProcessoSeletivoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>ResultadoProcessoSeletivoVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos
	 * para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(ResultadoProcessoSeletivoVO obj) throws ConsistirException {
		if ((obj.getInscricao() == null) || (obj.getInscricao().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo INSCRIÇÃO (Resultado Processo Seletivo) deve ser informado.");
		}
		if (obj.getResultadoPrimeiraOpcao() == null || (obj.getResultadoPrimeiraOpcao().equals(""))) {
			throw new ConsistirException("O campo RESULTADO PRIMEIRA OPÇÃO (Resultado Processo Seletivo) deve ser informado.");
		}
		if ((obj.getInscricao().getCursoOpcao2() != null) && (obj.getInscricao().getCursoOpcao2().getCodigo().intValue() != 0) && ((obj.getResultadoSegundaOpcao() == null || (obj.getResultadoSegundaOpcao().equals(""))))) {
			throw new ConsistirException("O campo RESULTADO SEGUNDA OPÇÃO (Resultado Processo Seletivo) deve ser informado para esta inscrição.");
		}
		if ((obj.getInscricao().getCursoOpcao3() != null) && (obj.getInscricao().getCursoOpcao3().getCodigo().intValue() != 0) && (((obj.getResultadoTerceiraOpcao() == null || obj.getResultadoTerceiraOpcao().equals(""))))) {
			throw new ConsistirException("O campo RESULTADO TERCEIRA OPÇÃO (Resultado Processo Seletivo) deve ser informado para esta inscrição.");
		}
		if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL (Resultado Processo Seletivo) deve ser informado.");
		}
		if (obj.getDataRegistro() == null) {
			throw new ConsistirException("O campo DATA REGISTRO RESULTADO (Resultado Processo Seletivo) deve ser informado.");
		}
		if (obj.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA") && Uteis.isAtributoPreenchido(obj.getInscricao().getGabaritoVO().getCodigo())) {
			if (obj.getResultadoProcessoSeletivoGabaritoRespostaVOs().isEmpty()) {
				throw new ConsistirException("O Gabarito não foi preenchido, o mesmo deverá ser preenchido e realizar o cálculo da média.");
			}
		}
		// if ((obj.inscricao == null) ||
		// (!obj.inscricao.getSituacao().equals("CO"))) {
		// throw new
		// ConsistirException("Esta inscrição ainda não está confirmada (Situação Inscrição: "
		// + obj.inscricao.getSituacao_Apresentar() + ").");
		// }
		// try {

		// if ((obj.inscricao.getFormaAcessoProcSeletivo().equals("VE")) &&
		// (obj.inscricao.getProcSeletivo().getProcSeletivoDisciplinasProcSeletivoVOs()
		// != null)) {
		// Integer nrDisciplina =
		// verificarQtdeDisciplinas(obj.inscricao.getProcSeletivo().getProcSeletivoDisciplinasProcSeletivoVOs());
		// if (nrDisciplina !=
		// obj.getResultadoDisciplinaProcSeletivoVOs().size()) {
		// throw new
		// ConsistirException("Devem ser informadas as notas para todas as disciplinas do Processo Seletivo (Forma de Acesso: Vestibular) referente a
		// esta inscrição (Proc. Seletivo: "
		// + obj.inscricao.getProcSeletivo().getDescricao() +
		// " - Nr. Disciplinas: " + nrDisciplina + ").");
		// }
		// }

		if ((obj.inscricao.getFormaAcessoProcSeletivo().equals("VE")) && (obj.inscricao.getProcSeletivo().getProcSeletivoDisciplinasProcSeletivoVOs() != null)) {
			for (ResultadoDisciplinaProcSeletivoVO resultadoDisciplina : obj.getResultadoDisciplinaProcSeletivoVOs()) {
				if (obj.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertos")) {
					if (resultadoDisciplina.getQuantidadeAcertos().equals(0)) {
						// throw new
						// ConsistirException("Devem ser informadas as notas para todas as disciplinas do Processo Seletivo (Forma de Acesso:
						// Vestibular) referente a esta inscrição (Proc. Seletivo: "
						// + obj.inscricao.getProcSeletivo().getDescricao() +
						// " ).");
					}
				} else {
					// if (resultadoDisciplina.getNota().equals(0.0)) {
					// throw new
					// ConsistirException("Devem ser informadas as notas para todas as disciplinas do Processo Seletivo (Forma de Acesso: Vestibular)
					// referente a esta inscrição (Proc. Seletivo: "
					// + obj.inscricao.getProcSeletivo().getDescricao() +
					// " ).");
					// }
				}
			}
		}
		// } catch (Exception e) {
		// throw new
		// ConsistirException("Devem ser informadas as notas para todas as disciplinas do Processo Seletivo referente a esta inscrição (Proc.
		// Seletivo: "
		// +
		// obj.inscricao.procSeletivo.getDescricao() + " - Nr. Disciplinas: " +
		// obj.inscricao.procSeletivo.getProcSeletivoDisciplinasProcSeletivoVOs().size()
		// + ").");
		// }
	}

	public static Integer verificarQtdeDisciplinas(List lista) {
		Integer qtde = 0;
		int idioma = 0;
		Iterator i = lista.iterator();
		while (i.hasNext()) {
			ProcSeletivoDisciplinasProcSeletivoVO obj = (ProcSeletivoDisciplinasProcSeletivoVO) i.next();
			if (!obj.getDisciplinasProcSeletivo().getDisciplinaIdioma()) {
				qtde += 1;
			} else {
				idioma = 1;
			}
		}
		/* considerando que sempre existe uma lingua estrangeira */
		return qtde + idioma;
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setResultadoPrimeiraOpcao("RE");
		setResultadoSegundaOpcao("RE");
		setResultadoTerceiraOpcao("RE");
		setMediaNotasProcSeletivo(0.0);
		setMediaPonderadaNotasProcSeletivo(0.0);
		setDataRegistro(new Date());
	}

	public int obterPesoDisciplinaProcSeletivo(DisciplinasProcSeletivoVO disciplinasProcSeletivoObterPeso) {
		List disciplinasProcSeletivoList = getInscricao().getProcSeletivo().getProcSeletivoDisciplinasProcSeletivoVOs();
		int peso = getInscricao().getProcSeletivo().getProcSeletivoDisciplinasProcSeletivoVOs().size();
		Iterator i = disciplinasProcSeletivoList.iterator();
		while (i.hasNext()) {
			ProcSeletivoDisciplinasProcSeletivoVO disciplinasProcSeletivo = (ProcSeletivoDisciplinasProcSeletivoVO) i.next();
			if (disciplinasProcSeletivo.getDisciplinasProcSeletivo().getCodigo().equals(disciplinasProcSeletivoObterPeso.getCodigo())) {
				return peso;
			}
			peso--;
		}
		return 1;
	}

	/**
	 * Operação responsável por calcular a média e a média ponderada do processo seletivo. Isto com base nas notas especificas em cada disciplina do
	 * processo seletivo. A média ponderada é calculada considerando a ordem na qual as disciplinas estão dispostas no ProcessoSeletivo. Ou seja, a
	 * primeira disciplina tem peso superior a segunda, e assim por diante.
	 */
	public void calcularMedias() {
		setAlgumaNotaAbaixoMinimo(false);
		double media = 0;
		double media2 = 0;
		double mediaPonderada = 0;
		double somaPeso = 0;
		String formula = this.getGrupoDisciplinaProcSeletivoVO().getFormulaCalculoAprovacao();
		// int peso = getResultadoDisciplinaProcSeletivoVOs().size();
		Iterator<ResultadoDisciplinaProcSeletivoVO> i = getResultadoDisciplinaProcSeletivoVOs().iterator();
		while (i.hasNext()) {
			ResultadoDisciplinaProcSeletivoVO resultadoDisciplina = (ResultadoDisciplinaProcSeletivoVO) i.next();
			if(!getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina") && resultadoDisciplina.isEditavel() 
					&& !Uteis.isAtributoPreenchido(getInscricao().getProvaProcessoSeletivoVO().getCodigo()) && !Uteis.isAtributoPreenchido(getInscricao().getGabaritoVO().getCodigo())){
				resultadoDisciplina.setNota(resultadoDisciplina.getQuantidadeAcertos()*getInscricao().getProcSeletivo().getValorPorAcerto());
				setSomatorioAcertos(getSomatorioAcertos()+resultadoDisciplina.getQuantidadeAcertos());
			}
			int peso = obterPesoDisciplinaProcSeletivo(resultadoDisciplina.getDisciplinaProcSeletivo());
			if(resultadoDisciplina.getNota() == null ) {
				resultadoDisciplina.setNota(0.0);
			}
			media = media + resultadoDisciplina.getNota().doubleValue();
			mediaPonderada = mediaPonderada + (peso * resultadoDisciplina.getNota().doubleValue());
			somaPeso = somaPeso + peso;
			peso--;
			if (formula.contains(resultadoDisciplina.getVariavelNota())) {
				formula = formula.replaceAll(resultadoDisciplina.getVariavelNota(), resultadoDisciplina.getNota().toString());
			}
			if (resultadoDisciplina.getNotaMinimaReprovadoImediato() != null) {
				if (resultadoDisciplina.getNota().doubleValue() <= resultadoDisciplina.getNotaMinimaReprovadoImediato().doubleValue()) {
					setAlgumaNotaAbaixoMinimo(true);
				}
			}
		}
		try {
			media2 = Uteis.realizarCalculoFormulaCalculo(formula);
		} catch (Exception e) {

		}
		media = (media / getResultadoDisciplinaProcSeletivoVOs().size());
		// this.setMediaNotasProcSeletivo(new Double(Uteis.arredondarDecimal(media, 2)));
		this.setMediaNotasProcSeletivo(new Double(Uteis.arredondarDecimal(media2, 2)));
		mediaPonderada = (mediaPonderada / somaPeso);
		this.setMediaPonderadaNotasProcSeletivo(new Double(Uteis.arredondarDecimal(mediaPonderada, 2)));
		realizarCalculoAprovacaoCandidatoPorProva();
	}

	public void realizarCalculoAprovacaoCandidatoPorProva() {
		setResultadoSegundaOpcao("RE");
		setResultadoTerceiraOpcao("RE");
		if (isAlgumaNotaAbaixoMinimo() && !this.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao") ) {
			setResultadoPrimeiraOpcao("RE");
		} else {
			if (getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")) {
				if (getMediaNotasProcSeletivo() >= getInscricao().getProcSeletivo().getMediaMinimaAprovacao()) {
					setResultadoPrimeiraOpcao("AP");
				} else {
					setResultadoPrimeiraOpcao("RE");
				}
			} else if (getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertos")) {
				if (getSomatorioAcertos() >= getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) {
					setResultadoPrimeiraOpcao("AP");
				} else {
					setResultadoPrimeiraOpcao("RE");
				}
			} else {
				if ((getSomatorioAcertos() >= getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) && ((Uteis.isAtributoPreenchido(getNotaRedacao()) || (getNotaRedacao() != null && getNotaRedacao().equals(0.0))) && getNotaRedacao() >= getInscricao().getProcSeletivo().getNotaMinimaRedacao())) {
					setResultadoPrimeiraOpcao("AP");
				} else {
					setResultadoPrimeiraOpcao("RE");
				}
			}
			if ((this.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao")) && this.getNotaRedacao() == null) {
				this.setResultadoPrimeiraOpcao("AG");
				this.setResultadoSegundaOpcao("AG");
				this.setResultadoTerceiraOpcao("AG");
			}
		}
	}

	public void somarNotas() {
		Iterator i = getResultadoDisciplinaProcSeletivoVOs().iterator();
		setSomatorioAcertos(0.0);
		setAlgumaNotaAbaixoMinimo(false);
		while (i.hasNext()) {
			ResultadoDisciplinaProcSeletivoVO resultadoDisciplina = (ResultadoDisciplinaProcSeletivoVO) i.next();
			setSomatorioAcertos(getSomatorioAcertos() + resultadoDisciplina.getQuantidadeAcertos());
		}
		realizarCalculoAprovacaoCandidatoPorProva();
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> ao List
	 * <code>resultadoDisciplinaProcSeletivoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ResultadoDisciplinaProcSeletivo</code> -
	 * getDisciplinaProcSeletivo().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjResultadoDisciplinaProcSeletivoVOs(ResultadoDisciplinaProcSeletivoVO obj, InscricaoVO inscricaoVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(getInscricao())) {
			setInscricao(inscricaoVO);
		}
		ResultadoDisciplinaProcSeletivoVO.validarDados(obj);
		int index = 0;
		Iterator i = getResultadoDisciplinaProcSeletivoVOs().iterator();
		while (i.hasNext()) {
			ResultadoDisciplinaProcSeletivoVO objExistente = (ResultadoDisciplinaProcSeletivoVO) i.next();
			if (objExistente.getDisciplinaProcSeletivo().getCodigo().equals(obj.getDisciplinaProcSeletivo().getCodigo())) {
				getResultadoDisciplinaProcSeletivoVOs().set(index, obj);
				calcularMedias();
				return;
			}
			index++;
		}
		getResultadoDisciplinaProcSeletivoVOs().add(obj);
		calcularMedias();
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> ao List
	 * <code>resultadoDisciplinaProcSeletivoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ResultadoDisciplinaProcSeletivo</code> -
	 * getDisciplinaProcSeletivo().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjResultadoProcessoSeletivoVOs(ResultadoProcessoSeletivoVO obj, List<ResultadoProcessoSeletivoVO> listaResultadoProcessoSeletivo) throws Exception {
		ResultadoProcessoSeletivoVO.validarDados(obj);
		int index = 0;
		Iterator i = listaResultadoProcessoSeletivo.iterator();
		while (i.hasNext()) {
			ResultadoProcessoSeletivoVO objExistente = (ResultadoProcessoSeletivoVO) i.next();
			if (objExistente.getInscricao().getCodigo().equals(obj.getInscricao().getCodigo())) {
				listaResultadoProcessoSeletivo.set(index, obj);
				calcularMedias();
				return;
			}
			index++;
		}
		listaResultadoProcessoSeletivo.add(obj);
	}

	public void excluirObjResultadoProcessoSeletivoVOs(Integer inscricao, List<ResultadoProcessoSeletivoVO> listaResultadoProcessoSeletivo) throws Exception {
		int index = 0;
		Iterator i = listaResultadoProcessoSeletivo.iterator();
		while (i.hasNext()) {
			ResultadoProcessoSeletivoVO objExistente = (ResultadoProcessoSeletivoVO) i.next();
			if (objExistente.getInscricao().getCodigo().equals(inscricao)) {
				listaResultadoProcessoSeletivo.remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> no List
	 * <code>resultadoDisciplinaProcSeletivoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ResultadoDisciplinaProcSeletivo</code> -
	 * getDisciplinaProcSeletivo().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param disciplinaProcSeletivo
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjResultadoDisciplinaProcSeletivoVOs(Integer disciplinaProcSeletivo) throws Exception {
		int index = 0;
		if (getResultadoDisciplinaProcSeletivoVOs().size() == 1) {
			throw new Exception("Deve ser mantido pelo menos uma disciplina.");
		}
		Iterator i = getResultadoDisciplinaProcSeletivoVOs().iterator();
		while (i.hasNext()) {
			ResultadoDisciplinaProcSeletivoVO objExistente = (ResultadoDisciplinaProcSeletivoVO) i.next();
			if (objExistente.getDisciplinaProcSeletivo().getCodigo().equals(disciplinaProcSeletivo)) {
				getResultadoDisciplinaProcSeletivoVOs().remove(index);
				calcularMedias();
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe <code>ResultadoDisciplinaProcSeletivoVO</code> no List
	 * <code>resultadoDisciplinaProcSeletivoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ResultadoDisciplinaProcSeletivo</code> -
	 * getDisciplinaProcSeletivo().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param disciplinaProcSeletivo
	 *            Parâmetro para localizar o objeto do List.
	 */
	public ResultadoDisciplinaProcSeletivoVO consultarObjResultadoDisciplinaProcSeletivoVO(Integer disciplinaProcSeletivo) throws Exception {
		Iterator i = getResultadoDisciplinaProcSeletivoVOs().iterator();
		while (i.hasNext()) {
			ResultadoDisciplinaProcSeletivoVO objExistente = (ResultadoDisciplinaProcSeletivoVO) i.next();
			if (objExistente.getDisciplinaProcSeletivo().getCodigo().equals(disciplinaProcSeletivo)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>ResultadoProcessoSeletivo</code>).
	 */
	 @XmlElement(name = "responsavel")
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>ResultadoProcessoSeletivo</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Inscricao</code> relacionado com ( <code>ResultadoProcessoSeletivo</code>).
	 */
	public InscricaoVO getInscricao() {
		if (inscricao == null) {
			inscricao = new InscricaoVO();
		}
		return (inscricao);
	}

	/**
	 * Define o objeto da classe <code>Inscricao</code> relacionado com ( <code>ResultadoProcessoSeletivo</code>).
	 */
	public void setInscricao(InscricaoVO obj) {
		this.inscricao = obj;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe <code>ResultadoDisciplinaProcSeletivo</code>.
	 */
	@XmlElement(name = "resultadoDisciplinaProcSeletivoVOs")
	public List<ResultadoDisciplinaProcSeletivoVO> getResultadoDisciplinaProcSeletivoVOs() {
		if (resultadoDisciplinaProcSeletivoVOs == null) {
			resultadoDisciplinaProcSeletivoVOs = new ArrayList<ResultadoDisciplinaProcSeletivoVO>(0);
		}
		return (resultadoDisciplinaProcSeletivoVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe <code>ResultadoDisciplinaProcSeletivo</code>.
	 */
	public void setResultadoDisciplinaProcSeletivoVOs(List<ResultadoDisciplinaProcSeletivoVO> resultadoDisciplinaProcSeletivoVOs) {
		this.resultadoDisciplinaProcSeletivoVOs = resultadoDisciplinaProcSeletivoVOs;
	}

	public Date getDataRegistro() {
		return (dataRegistro);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getDataRegistro_Apresentar() {
		return (Uteis.getData(dataRegistro));
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	@XmlElement(name = "mediaPonderadaNotasProcSeletivo")
	public Double getMediaPonderadaNotasProcSeletivo() {
		return (mediaPonderadaNotasProcSeletivo);
	}

	public void setMediaPonderadaNotasProcSeletivo(Double mediaPonderadaNotasProcSeletivo) {
		this.mediaPonderadaNotasProcSeletivo = mediaPonderadaNotasProcSeletivo;
	}

	@XmlElement(name = "mediaNotasProcSeletivo")
	public Double getMediaNotasProcSeletivo() {
		return (mediaNotasProcSeletivo);
	}

	public void setMediaNotasProcSeletivo(Double mediaNotasProcSeletivo) {
		this.mediaNotasProcSeletivo = mediaNotasProcSeletivo;
	}

	@XmlElement(name = "resultadoTerceiraOpcao")
	public String getResultadoTerceiraOpcao() {
		if (resultadoTerceiraOpcao == null) {
			resultadoTerceiraOpcao = "";
		}
		return (resultadoTerceiraOpcao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do
	 * atributo esta função é capaz de retornar o de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
	 */
	public String getResultadoTerceiraOpcao_Apresentar() {
		if (getResultadoTerceiraOpcao().equals("A3")) {
			return "Aprovado Terceira Opção";
		}
		if (getResultadoTerceiraOpcao().equals("A2")) {
			return "Aprovado Segunda Opção";
		}
		if (getResultadoTerceiraOpcao().equals("AP")) {
			return "Aprovado";
		}
		if (getResultadoTerceiraOpcao().equals("RE")) {
			return "Reprovado";
		}
		return (getResultadoTerceiraOpcao());
	}

	public void setResultadoTerceiraOpcao(String resultadoTerceiraOpcao) {
		this.resultadoTerceiraOpcao = resultadoTerceiraOpcao;
	}

	@XmlElement(name = "resultadoSegundaOpcao")
	public String getResultadoSegundaOpcao() {
		if (resultadoSegundaOpcao == null) {
			resultadoSegundaOpcao = "";
		}
		return (resultadoSegundaOpcao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do
	 * atributo esta função é capaz de retornar o de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
	 */
	public String getResultadoSegundaOpcao_Apresentar() {
		if (getResultadoSegundaOpcao().equals("A3")) {
			return "Aprovado Terceira Opção";
		}
		if (getResultadoSegundaOpcao().equals("A2")) {
			return "Aprovado Segunda Opção";
		}
		if (getResultadoSegundaOpcao().equals("AP")) {
			return "Aprovado";
		}
		if (getResultadoSegundaOpcao().equals("RE")) {
			return "Reprovado";
		}
		return (resultadoSegundaOpcao);
	}

	public void setResultadoSegundaOpcao(String resultadoSegundaOpcao) {
		this.resultadoSegundaOpcao = resultadoSegundaOpcao;
	}

	@XmlElement(name = "resultadoPrimeiraOpcao")
	public String getResultadoPrimeiraOpcao() {
		if (resultadoPrimeiraOpcao == null) {
			resultadoPrimeiraOpcao = "";
		}
		return (resultadoPrimeiraOpcao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do
	 * atributo esta função é capaz de retornar o de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
	 */
	@XmlElement(name = "resultadoPrimeiraOpcao_Apresentar")
	public String getResultadoPrimeiraOpcao_Apresentar() {
		if (getResultadoPrimeiraOpcao().equals("A3")) {
			return "Aprovado Terceira Opção";
		}
		if (getResultadoPrimeiraOpcao().equals("A2")) {
			return "Aprovado Segunda Opção";
		}
		if (getResultadoPrimeiraOpcao().equals("AP")) {
			return "Aprovado";
		}
		if (getResultadoPrimeiraOpcao().equals("RE")) {
			return "Reprovado";
		}
		if (getResultadoPrimeiraOpcao().equals("AG")) {
			return "Aguardando Nota";
		}
		return (getResultadoPrimeiraOpcao());
	}

	public void setResultadoPrimeiraOpcao(String resultadoPrimeiraOpcao) {
		this.resultadoPrimeiraOpcao = resultadoPrimeiraOpcao;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getSituacaoAprovacaoGeralProcessoSeletivo() {
		String situacaoPadrao = "";
		if (getInscricao().getSituacaoInscricao().equals(SituacaoInscricaoEnum.NAO_COMPARECEU)) {
			return getInscricao().getSituacaoInscricao().getValorApresentar();
		}
		if (getInscricao().getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO)) {
			return getInscricao().getSituacaoInscricao().getValorApresentar();
		}
		if (getResultadoPrimeiraOpcao().equals("AG")) {
			return "Aguardando Nota";
		}
		if (getResultadoSegundaOpcao().equals("AG")) {
			return "Aguardando Nota";
		}
		if (getResultadoTerceiraOpcao().equals("AG")) {
			return "Aguardando Nota";
		}
		if (getResultadoPrimeiraOpcao().equals("AP")) {
			return "Aprovado (Primeira Opção)";
		}
		if (getResultadoSegundaOpcao().equals("AP")) {
			return "Aprovado (Segunda Opção)";
		}
		if (getResultadoTerceiraOpcao().equals("AP")) {
			return "Aprovado (Terceira Opção)";
		}
		if (getResultadoPrimeiraOpcao().equals("RE")) {
			return "Reprovado (Primeira Opção)";
		}
		if (getResultadoSegundaOpcao().equals("RE")) {
			return "Reprovado (Segunda Opção)";
		}
		if (getResultadoTerceiraOpcao().equals("RE")) {
			return "Reprovado (Terceira Opção)";
		}
		if (getResultadoPrimeiraOpcao().equals("") && getResultadoSegundaOpcao().equals("") && getResultadoTerceiraOpcao().equals("")) {
			return "Não Lançado";
		}
		return situacaoPadrao;
	}

	public int getOpcaoCursoAprovadoProcessoSeletivo() {
		if (getResultadoPrimeiraOpcao().equals("AP")) {
			return 1;
		}
		if (getResultadoSegundaOpcao().equals("AP")) {
			return 2;
		}
		if (getResultadoTerceiraOpcao().equals("AP")) {
			return 3;
		}
		return 0;
	}

	public boolean isAprovadoProcessoSeletivo() {
		if ((!getResultadoPrimeiraOpcao().equals("AP")) && (!getResultadoSegundaOpcao().equals("AP")) && (!getResultadoTerceiraOpcao().equals("AP"))) {
			return false;
		} else {
			return true;
		}
	}

	public String getRespostaProvaProcessoSeletivo() {
		if (respostaProvaProcessoSeletivo == null) {
			respostaProvaProcessoSeletivo = "";
		}
		return respostaProvaProcessoSeletivo;
	}

	public void setRespostaProvaProcessoSeletivo(String respostaProvaProcessoSeletivo) {
		this.respostaProvaProcessoSeletivo = respostaProvaProcessoSeletivo;
	}

	public List<ColunaGabaritoVO> getColunaGabaritoVOs() {
		if (colunaGabaritoVOs == null) {
			colunaGabaritoVOs = new ArrayList<ColunaGabaritoVO>(0);
		}
		return colunaGabaritoVOs;
	}

	public void setColunaGabaritoVOs(List<ColunaGabaritoVO> colunaGabaritoVOs) {
		this.colunaGabaritoVOs = colunaGabaritoVOs;
	}

	public List<ResultadoProcessoSeletivoGabaritoRespostaVO> getResultadoProcessoSeletivoGabaritoRespostaVOs() {
		if (resultadoProcessoSeletivoGabaritoRespostaVOs == null) {
			resultadoProcessoSeletivoGabaritoRespostaVOs = new ArrayList<ResultadoProcessoSeletivoGabaritoRespostaVO>(0);
		}
		return resultadoProcessoSeletivoGabaritoRespostaVOs;
	}

	public void setResultadoProcessoSeletivoGabaritoRespostaVOs(List<ResultadoProcessoSeletivoGabaritoRespostaVO> resultadoProcessoSeletivoGabaritoRespostaVOs) {
		this.resultadoProcessoSeletivoGabaritoRespostaVOs = resultadoProcessoSeletivoGabaritoRespostaVOs;
	}

	public Integer getQtdeColuna() {
		return getColunaGabaritoVOs().size();
	}

	public void setQtdeColuna(Integer qtdeColuna) {
		this.qtdeColuna = qtdeColuna;
	}

	public Integer getQuantidadeQuestao() {
		if (quantidadeQuestao == null) {
			quantidadeQuestao = 0;
		}
		return quantidadeQuestao;
	}

	public void setQuantidadeQuestao(Integer quantidadeQuestao) {
		this.quantidadeQuestao = quantidadeQuestao;
	}
	
	@XmlElement(name = "notaRedacao")
	public Double getNotaRedacao() {
		return notaRedacao;
	}

	public void setNotaRedacao(Double notaRedacao) {
		this.notaRedacao = notaRedacao;
	}

	public Boolean getEnviaMensagemResultadoProcessoSeletivo() {
		if (enviaMensagemResultadoProcessoSeletivo == null) {
			enviaMensagemResultadoProcessoSeletivo = Boolean.FALSE;
		}
		return enviaMensagemResultadoProcessoSeletivo;
	}

	public void setEnviaMensagemResultadoProcessoSeletivo(Boolean enviaMensagemResultadoProcessoSeletivo) {
		this.enviaMensagemResultadoProcessoSeletivo = enviaMensagemResultadoProcessoSeletivo;
	}

	public Boolean getEnviaMensagemAprovacaoProcessoseletivo() {
		if (enviaMensagemAprovacaoProcessoseletivo == null) {
			enviaMensagemAprovacaoProcessoseletivo = Boolean.FALSE;
		}
		return enviaMensagemAprovacaoProcessoseletivo;
	}

	public void setEnviaMensagemAprovacaoProcessoseletivo(Boolean enviaMensagemAprovacaoProcessoseletivo) {
		this.enviaMensagemAprovacaoProcessoseletivo = enviaMensagemAprovacaoProcessoseletivo;
	}

	@XmlElement(name = "somatorioAcertos")
	public Double getSomatorioAcertos() {
		if (somatorioAcertos == null) {
			somatorioAcertos = 0.0;
		}
		return somatorioAcertos;
	}

	public void setSomatorioAcertos(Double somatorioAcertos) {
		this.somatorioAcertos = somatorioAcertos;
	}

	@XmlElement(name = "grupoDisciplinaProcSeletivoVO")
	public GrupoDisciplinaProcSeletivoVO getGrupoDisciplinaProcSeletivoVO() {
		if (grupoDisciplinaProcSeletivoVO == null) {
			grupoDisciplinaProcSeletivoVO = new GrupoDisciplinaProcSeletivoVO();
		}
		return grupoDisciplinaProcSeletivoVO;
	}

	public void setGrupoDisciplinaProcSeletivoVO(GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO) {
		this.grupoDisciplinaProcSeletivoVO = grupoDisciplinaProcSeletivoVO;
	}

	public boolean getSituacaoAprovacaoGeralProcessoSeletivoENaoLancado() {
		if (getSituacaoAprovacaoGeralProcessoSeletivo().equals("Não Lançado")) {
			return true;
		}
		return false;
	}

	public Integer getClassificacao() {
		if (classificacao == null) {
			classificacao = 0;
		}
		return classificacao;
	}

	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	@XmlElement(name = "resultadoProcessoSeletivoProvaRespostaVOs")
	public List<ResultadoProcessoSeletivoProvaRespostaVO> getResultadoProcessoSeletivoProvaRespostaVOs() {
		if (resultadoProcessoSeletivoProvaRespostaVOs == null) {
			resultadoProcessoSeletivoProvaRespostaVOs = new ArrayList<ResultadoProcessoSeletivoProvaRespostaVO>(0);
		}
		return resultadoProcessoSeletivoProvaRespostaVOs;
	}

	public void setResultadoProcessoSeletivoProvaRespostaVOs(List<ResultadoProcessoSeletivoProvaRespostaVO> resultadoProcessoSeletivoProvaRespostaVOs) {
		this.resultadoProcessoSeletivoProvaRespostaVOs = resultadoProcessoSeletivoProvaRespostaVOs;
	}

	public boolean isAlgumaNotaAbaixoMinimo() {
		return algumaNotaAbaixoMinimo;
	}

	public void setAlgumaNotaAbaixoMinimo(boolean algumaNotaAbaixoMinimo) {
		this.algumaNotaAbaixoMinimo = algumaNotaAbaixoMinimo;
	}

	@XmlElement(name = "descricaoClassificacaoCand")
	public String getDescricaoClassificacaoCand() {
		if (descricaoClassificacaoCand == null) {
			descricaoClassificacaoCand = "";
		}
		return descricaoClassificacaoCand;
	}

	public void setDescricaoClassificacaoCand(String descricaoClassificacaoCand) {
		this.descricaoClassificacaoCand = descricaoClassificacaoCand;
	}

	public ResultadoDisciplinaProcSeletivoVO getResultadoDisciplinaProcSeletivoVO() {
		if(resultadoDisciplinaProcSeletivoVO == null){
			resultadoDisciplinaProcSeletivoVO = new ResultadoDisciplinaProcSeletivoVO();
		}
		return resultadoDisciplinaProcSeletivoVO;
	}

	public void setResultadoDisciplinaProcSeletivoVO(ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO) {
		this.resultadoDisciplinaProcSeletivoVO = resultadoDisciplinaProcSeletivoVO;
	}
	
	public Boolean getCalculoMedia() {
		return getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina");
	}
	
	public String getCssSomatorioAcerto() {
		if (getDesbloquearSomatorioAcerto()) {
			return "camposObrigatorios";
		}
		return "camposSomenteLeitura";
	}
	
	public String getCssMediaNota(){
		if (getDesbloquearMediaNota()) {
			return "camposObrigatorios";
		}
		return "camposSomenteLeitura";
	}
	
	public boolean getDesbloquearMediaNota(){
		return !getInscricao().getCodigo().equals(0) && !getApresentarAbaNotasPorDisciplina() && !getApresentarAbaGabarito() && !getApresentarAbaProvaProcessoSeletivo() && getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina");
	}
	
	public boolean getDesbloquearSomatorioAcerto(){
		return !getInscricao().getCodigo().equals(0) && !getApresentarAbaNotasPorDisciplina() && !getApresentarAbaGabarito()  && !getApresentarAbaProvaProcessoSeletivo() && !getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina");
	}
	
	public boolean getApresentarAbaGabarito(){
		return !getInscricao().getCodigo().equals(0) && (getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA") && Uteis.isAtributoPreenchido(getInscricao().getGabaritoVO().getCodigo()));
	}
	
	public boolean getApresentarAbaNotasPorDisciplina(){
		return !getInscricao().getCodigo().equals(0) && !getResultadoDisciplinaProcSeletivoVOs().isEmpty();
	}
	
	public boolean getApresentarAbaProvaProcessoSeletivo(){
		return !getInscricao().getCodigo().equals(0) && getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("PR") && Uteis.isAtributoPreenchido(getInscricao().getProvaProcessoSeletivoVO().getCodigo());
	}
	
	public Boolean getOpcao1() {
		return Uteis.isAtributoPreenchido(getInscricao().getCursoOpcao1().getCodigo());
	}

	public Boolean getOpcao2() {
		return Uteis.isAtributoPreenchido(getInscricao().getCursoOpcao2().getCodigo());
	}

	public Boolean getOpcao3() {
		return Uteis.isAtributoPreenchido(getInscricao().getCursoOpcao3().getCodigo());
	}
	
	public String getOrdenacaoCandidato(){
		return getInscricao().getCandidato().getNome();
	}
	
	@XmlElement(name = "redacao")
	public String getRedacao() {
		if (redacao == null) {
			redacao = "";	
		}
		return redacao;
	}

	public void setRedacao(String redacao) {
		this.redacao = redacao;
	}
	
	@XmlElement(name = "somatorioErros")
	public Double getSomatorioErros() {
		if (somatorioErros == null) {
			somatorioErros = 0.0;
		}
		return somatorioErros;
	}

	public void setSomatorioErros(Double somatorioErros) {
		this.somatorioErros = somatorioErros;
	}

	public String getNavegadorAcesso() {
		if(navegadorAcesso == null) {
			navegadorAcesso ="";
		}
		return navegadorAcesso;
	}

	public void setNavegadorAcesso(String navegadorAcesso) {
		this.navegadorAcesso = navegadorAcesso;
	}

}
