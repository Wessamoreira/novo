package negocio.comuns.academico;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces.model.SelectItem;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.StringUtils;


import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.ConfiguracaoTCCVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.financeiro.TextoPadraoVO;

import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TituloCursoPos;
import negocio.facade.jdbc.arquitetura.AtributoComparacao;
import webservice.servicos.objetos.enumeradores.TagsTextoFinalizacaoMatriculaEnum;

/**
 * Reponsável por manter os dados da entidade Curso. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
@XmlRootElement(name = "curso")
public class CursoVO extends SuperVO implements Cloneable {

    private Integer codigo;
    private String nome;
    private String nomeDocumentacao;
    private String objetivos;
    private String descricao;
    private String publicoAlvo;
    private String perfilEgresso;
    private String nivelEducacional;
    private Integer numeroExemplaresBibliograficoBasica;
    private Integer numeroExemplaresBibliograficoComplementar;
    private Integer tempoDefasagemPadraoCatalogoBibliograficaBasica;
    private Integer tempoDefasagemPadraoCatalogoBibliograficaComplementar;
    private Integer quantidadeDisciplinasOptativasExpedicaoDiploma;
    private String script; // Para uso na interação
    /**
     * Data criação corresponde a data autorização
     * Numero registro interno corresponde a Autorização
     */
    private String nrRegistroInterno;
    private Date dataCriacao;
    /**
     * Corresponde a data de Reconhecimento
     * Base legal corresponde ao numero de reconhecimento
     */
    private Date dataPublicacaoDO;
    private Integer nrPeriodoLetivo;
    private String periodicidade;
    private String regimeAprovacao;
    private String regime;
    private Integer idCursoInep;
    private String habilitacao;
    private String titulo;
    private String abreviatura;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>DocumentacaoCurso</code>.
     */
    @ExcluirJsonAnnotation
    private List<DocumentacaoCursoVO> documentacaoCursoVOs;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>CursoCoordenador</code>.
     */
    @ExcluirJsonAnnotation
    private List<CursoCoordenadorVO> cursoCoordenadorVOs;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>CursoTurno</code>.
     */
    @ExcluirJsonAnnotation
    private List<CursoTurnoVO> cursoTurnoVOs;
    @ExcluirJsonAnnotation
    private List<GradeCurricularVO> gradeCurricularVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
   
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ConfiguracaoAcademico </code>.
     */
    @ExcluirJsonAnnotation
    private ConfiguracaoAcademicoVO configuracaoAcademico;
    /**
     * Atributo que vai dizer se este curso será selecionado para participar do Processo seletivo
     */
    private boolean inserirNoProcessoMatricula;
    /**
     * Atributo que vai ser impresso no diploma do aluno depois do texto "confere o grau de "
     */
    private String titulacaoDoFormando;
    private String titulacaoDoFormandoFeminino;
    /**
     * Atributo que permite curso ter uma lista de AutorizacaoCursoVO
     */
    @ExcluirJsonAnnotation
    private List<AutorizacaoCursoVO> autorizacaoCursoVOs;
    @ExcluirJsonAnnotation
    private List<MaterialCursoVO> materialCursoVOs;
    private Boolean limitarQtdeDiasMaxDownload;
    private Integer qtdeMaxDiasDownload;
    @ExcluirJsonAnnotation
    private FuncionarioVO funcionarioRespPreInscricao;
    private Boolean liberarRegistroAulaEntrePeriodo;
    private Boolean apresentarHomePreInscricao = Boolean.TRUE;
    @ExcluirJsonAnnotation
    private ConfiguracaoTCCVO configuracaoTCC;
    private Boolean cursoSelecionado;
    private Integer percentualEvolucaoAcademicaIngressanteEnade; 
    private Integer percentualEvolucaoAcademicaConcluinteEnade; 
    private String anoMudancaLegislacao;
    public static final long serialVersionUID = 1L;
    private String preposicaoNomeCurso;
    private String titulacaoMasculinoApresentarDiploma;
    private String titulacaoFemininoApresentarDiploma;
    private String codigoContabil;
    private String nomeContabil;
    private Boolean apresentarCursoBiblioteca;
    private String nivelContabil;
    private String notificações;
    @ExcluirJsonAnnotation
    private GrupoPessoaVO grupoPessoaAnaliseRelatorioFinalEstagioVO;
    @ExcluirJsonAnnotation
    private GrupoPessoaVO grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO;
    @ExcluirJsonAnnotation
    private GrupoPessoaVO grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO;
    @ExcluirJsonAnnotation
    private FuncionarioVO funcionarioResponsavelAssinaturaTermoEstagioVO;
    private String idConteudoMasterBlackboardEstagio;    
    
    
    /**
     * Usado na classe {@link RelatorioSEIDecidirControle}
     */
    @ExcluirJsonAnnotation
	private Boolean filtrarCursoVO;
	@ExcluirJsonAnnotation
	private Timestamp dataUltimaAlteracao;
	// Campo criado para definir no curso qual a modalidade do curso -> ONLINE/PRESENCIAL.
	// Esse campo será apresentado baseado em permissão de acesso.
	private ModalidadeDisciplinaEnum modalidadeCurso;

	// Se campo marcado no curso, não será apresentado para o aluno a opção de estudar on-line no SEI.
	@ExcluirJsonAnnotation
	private Boolean utilizarRecursoAvaTerceiros;
	@ExcluirJsonAnnotation
	private QuestionarioVO questionarioVO;
    private ConfiguracaoLdapVO configuracaoLdapVO;
    private String textoDeclaracaoPPI ;
    private String textoDeclaracaoBolsasAuxilios;  
    private String textoDeclaracaoEscolaridadePublica;
    private String textoConfirmacaoNovaMatricula;
    private String urlDeclaracaoNormasMatricula;
    private Boolean habilitarMensagemNotificacaoNovaMatricula;    
    private PersonalizacaoMensagemAutomaticaVO mensagemConfirmacaoNovaMatricula;
    private Boolean habilitarMensagemNotificacaoRenovacaoMatricula;
    private PersonalizacaoMensagemAutomaticaVO mensagemRenovacaoMatricula;
    private Boolean habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento;
    private PersonalizacaoMensagemAutomaticaVO mensagemAtivacaoPreMatriculaEntregaDocumento;
	private Boolean ativarPreMatriculaAposEntregaDocumentosObrigatorios;
    private Boolean permitirAssinarContratoPendenciaDocumentacao;
    private  Boolean selecionado;
    private EixoCursoVO eixoCursoVO;
	
	private List<SelectItem> listaSelectItemTemplateMensagemAutomaticaEnum;
    private Integer count;		
    private Boolean ativarMatriculaAposAssinaturaContrato;
    private TextoPadraoVO textoPadraoContratoMatriculaCalouro;
	private List listaSelectItemTextoPadraoContratoMatriculaCalouro;   
    private Boolean permitirOutraMatriculaMesmoAluno;
    private Boolean possuiCodigoEMEC;
	private Integer codigoEMEC;
	private Integer numeroProcessoEMEC;
	private String tipoProcessoEMEC;
	private Date dataCadastroEMEC;
	private Date dataProtocoloEMEC;
	private TipoAutorizacaoCursoEnum tipoAutorizacaoCursoEnum;
	private String numeroAutorizacao;
	private Date dataCredenciamento;
	private String veiculoPublicacao;
	private Integer secaoPublicacao;
	private Integer paginaPublicacao;
	private Integer numeroDOU;
	private Boolean autorizacaoResolucaoEmTramitacao;
	private String numeroProcessoAutorizacaoResolucao;
	private String tipoProcessoAutorizacaoResolucao;
	private Date dataCadastroAutorizacaoResolucao;
	private Date dataProtocoloAutorizacaoResolucao;
	private Date dataHabilitacao;
    
    
    
	public enum enumCampoConsultaCurso {
		CODIGO, NOME; 
	}
	
    /**
     * Construtor padrão da classe <code>Curso</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public CursoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CursoVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CursoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Curso) deve ser informado.");
        }
        if (obj.getNivelEducacional() == null || obj.getNivelEducacional().equals("") || obj.getNivelEducacional().equals("0")) {
            throw new ConsistirException("O campo NÍVEL EDUCACIONAL (Dados Básicos) deve ser informado.");
        }
        if (obj.getTitulacaoDoFormando() == null || obj.getTitulacaoDoFormando().equals("")) {
            throw new ConsistirException("O campo TITULAÇÃO DO FORMANDO MASCULINO (Dados Básicos) deve ser informado.");
        }
        if (obj.getTitulacaoDoFormandoFeminino() == null || obj.getTitulacaoDoFormandoFeminino().equals("")) {
        	throw new ConsistirException("O campo TITULAÇÃO DO FORMANDO FEMININO (Dados Básicos) deve ser informado.");
        }
        if (obj.getRegimeAprovacao() == null || obj.getRegimeAprovacao().equals("")) {
            throw new ConsistirException("O campo REGIME APROVAÇÃO (Dados Básicos) deve ser informado.");
        }
        if (obj.getRegime() == null || obj.getRegime().equals("")) {
            throw new ConsistirException("O campo REGIME (Dados Básicos) deve ser informado.");
        }
//        if ((obj.getAreaConhecimento() == null) || (obj.getAreaConhecimento().getCodigo().intValue() == 0)) {
//            throw new ConsistirException("O campo ÁREA CONHECIMENTO (Dados Básicos) deve ser informado.");
//        }
        if ((obj.getConfiguracaoAcademico() == null) || (obj.getConfiguracaoAcademico().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONFIGURAÇÃO ACADÊMICO (Dados Básicos) deve ser informado.");
        }
		
		if (Uteis.isAtributoPreenchido(obj.getConfiguracaoAcademico().getCodigo()) && Uteis.isAtributoPreenchido(obj.getConfiguracaoAcademico().getMascaraPadraoGeracaoMatricula())) {
        	Integer quantidadeConfiguracaoAcademica = StringUtils.countOccurrencesOf(obj.getConfiguracaoAcademico().getMascaraPadraoGeracaoMatricula(), "B");
        	if (quantidadeConfiguracaoAcademica > 0 && quantidadeConfiguracaoAcademica < obj.getAbreviatura().length()) {
				throw new ConsistirException("A máscara de matrícula " + obj.getConfiguracaoAcademico().getMascaraPadraoGeracaoMatricula() +  " vinculado a configuração acadêmica possui " + quantidadeConfiguracaoAcademica +  " letras B que corresponde a abreviatura do curso, portanto o campo de abreviatura do curso não pode exceder a quantidade de caracteres da máscara.");
        	}
        	
        }
		validarDadosDiplomaCurso(obj);
    }

    public static void validarDadosIncluirPeriodoLetivo(CursoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Curso) deve ser informado.");
        }

        if (obj.getRegimeAprovacao() == null || obj.getRegimeAprovacao().equals("")) {
            throw new ConsistirException("O campo REGIME APROVAÇÃO (Dados Básicos) deve ser informado.");
        }
        if (obj.getRegime() == null || obj.getRegime().equals("")) {
            throw new ConsistirException("O campo REGIME (Dados Básicos) deve ser informado.");
        }
//        if (obj.getRegime().equals("SE")) {
//            if (obj.getCargaHoraria().intValue() == 0) {
//                throw new ConsistirException("O campo CARGA HORÁRIA (Dados Básicos) deve ser informado.");
//            }
//        } else {
//            if (obj.getCreditos().intValue() == 0) {
//                throw new ConsistirException("O campo CRÉDITOS (Dados Básicos) deve ser informado.");
//            }
//        }

    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome(Constantes.EMPTY);
        setObjetivos(Constantes.EMPTY);
        setDescricao(Constantes.EMPTY);
        setPublicoAlvo(Constantes.EMPTY);
        setPerfilEgresso(Constantes.EMPTY);
        setNrPeriodoLetivo(0);
//        setBaseLegal(Constantes.EMPTY);
        setDataPublicacaoDO(new Date());
        setNrRegistroInterno(Constantes.EMPTY);
        setDataCriacao(new Date());
        setPeriodicidade(Constantes.EMPTY);
        setRegimeAprovacao(Constantes.EMPTY);
        setIdCursoInep(0);
//        setCargaHoraria(0);
//        setCreditos(0);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>CursoTurnoVO</code> ao List <code>cursoTurnoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>CursoTurno</code> -
     * getTurno().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>CursoTurnoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjCursoTurnoVOs(CursoTurnoVO obj) throws Exception {
        CursoTurnoVO.validarDados(obj);
        int index = 0;
        Iterator<CursoTurnoVO> i = getCursoTurnoVOs().iterator();
        while (i.hasNext()) {
            CursoTurnoVO objExistente = (CursoTurnoVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) {
                getCursoTurnoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getCursoTurnoVOs().add(obj);
    }

    public void atualizarTotalCargaHoraria() throws Exception {
        Iterator<GradeCurricularVO> i = this.getGradeCurricularVOs().iterator();
        while (i.hasNext()) {
            GradeCurricularVO grade = (GradeCurricularVO) i.next();
            for (PeriodoLetivoVO p : grade.getPeriodoLetivosVOs()) {
                p.atualizarTotalCargaHoraria();
            }
        }
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>CursoTurnoVO</code> no List <code>cursoTurnoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>CursoTurno</code> -
     * getTurno().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param turno
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjCursoTurnoVOs(Integer turno) throws Exception {
        int index = 0;
        Iterator<CursoTurnoVO> i = getCursoTurnoVOs().iterator();
        while (i.hasNext()) {
            CursoTurnoVO objExistente = (CursoTurnoVO) i.next();
            if (objExistente.getTurno().getCodigo().equals(turno)) {
                getCursoTurnoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>CursoTurnoVO</code> no List <code>cursoTurnoVOs</code>. Utiliza o
     * atributo padrão de consulta da classe <code>CursoTurno</code> -
     * getTurno().getCodigo() - como identificador (key) do objeto no List.
     *
     * @param turno
     *            Parâmetro para localizar o objeto do List.
     */
    public CursoTurnoVO consultarObjCursoTurnoVO(Integer turno) {
        Iterator<CursoTurnoVO> i = getCursoTurnoVOs().iterator();
        while (i.hasNext()) {
            CursoTurnoVO objExistente =  i.next();
            if (objExistente.getTurno().getCodigo().equals(turno)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>DocumentacaoCursoVO</code> ao List
     * <code>documentacaoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>DocumentacaoCurso</code> - getTipoDocumento() - como
     * identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>DocumentacaoCursoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjDocumentacaoCursoVOs(DocumentacaoCursoVO obj) throws Exception {
        DocumentacaoCursoVO.validarDados(obj);
        int index = 0;
        Iterator<DocumentacaoCursoVO> i = getDocumentacaoCursoVOs().iterator();
        while (i.hasNext()) {
            DocumentacaoCursoVO objExistente = (DocumentacaoCursoVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(obj.getTipoDeDocumentoVO().getCodigo())) {
                getDocumentacaoCursoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getDocumentacaoCursoVOs().add(obj);
        // adicionarObjSubordinadoOC
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>DocumentacaoCursoVO</code> no List
     * <code>documentacaoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>DocumentacaoCurso</code> - getTipoDocumento() - como
     * identificador (key) do objeto no List.
     *
     * @param tipoDocumento
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjDocumentacaoCursoVOs(Integer tipoDeDocumento) throws Exception {
        int index = 0;
        Iterator<DocumentacaoCursoVO> i = getDocumentacaoCursoVOs().iterator();
        while (i.hasNext()) {
            DocumentacaoCursoVO objExistente = (DocumentacaoCursoVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(tipoDeDocumento)) {
                getDocumentacaoCursoVOs().remove(index);
                return;
            }
            index++;
        }
        // excluirObjSubordinadoOC
    }
    // TODO Alberto 13/12/10 Acrescentado coordenador curso

	public void adicionarObjCursoCoordenadorVOs(CursoCoordenadorVO obj) throws Exception {
		UtilReflexao.adicionarObjetoLista(getCursoCoordenadorVOs(), obj, new AtributoComparacao().add("curso.codigo", obj.getCurso().getCodigo()).add("turma.codigo", obj.getTurma().getCodigo()).add("unidadeEnsino.codigo", obj.getUnidadeEnsino().getCodigo()).add("tipoCoordenadorCurso.name", obj.getTipoCoordenadorCurso().name()).add("funcionario.codigo", obj.getFuncionario().getCodigo()));
	}

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>DocumentacaoCursoVO</code> no List
     * <code>documentacaoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>DocumentacaoCurso</code> - getTipoDocumento() - como
     * identificador (key) do objeto no List.
     *
     * @param tipoDocumento
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjCursoCoordenadorVOs(CursoCoordenadorVO obj) throws Exception {
        int index = 0;
        Iterator<CursoCoordenadorVO> i = getCursoCoordenadorVOs().iterator();
        while (i.hasNext()) {
            CursoCoordenadorVO objExistente = (CursoCoordenadorVO) i.next();
            if (objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())   && objExistente.getTipoCoordenadorCurso().equals(obj.getTipoCoordenadorCurso()) && objExistente.getFuncionario().getCodigo().equals(obj.getFuncionario().getCodigo()) && objExistente.getTurma().getCodigo().equals(obj.getTurma().getCodigo())) {
                getCursoCoordenadorVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>DocumentacaoCursoVO</code> no List
     * <code>documentacaoCursoVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>DocumentacaoCurso</code> - getTipoDocumento() - como
     * identificador (key) do objeto no List.
     *
     * @param tipoDocumento
     *            Parâmetro para localizar o objeto do List.
     */
    public DocumentacaoCursoVO consultarObjDocumentacaoCursoVO(Integer tipoDeDocumento) throws Exception {
        Iterator<DocumentacaoCursoVO> i = getDocumentacaoCursoVOs().iterator();
        while (i.hasNext()) {
            DocumentacaoCursoVO objExistente = (DocumentacaoCursoVO) i.next();
            if (objExistente.getTipoDeDocumentoVO().getCodigo().equals(tipoDeDocumento)) {
                return objExistente;
            }
        }
        return null;
        // consultarObjSubordinadoOC
    }

    public void validarDadosMatrizCurricular(GradeCurricularVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Matriz Curricular) deve ser informado.");
        }
        if (obj.getCargaHoraria() == 0) {
            throw new ConsistirException("O campo CARGA HORÁRIA (Matriz Curricular) deve ser informado.");
        }
//        if (obj.getCreditos() == 0) {
//            throw new ConsistirException("O campo CRÉDITOS (Matriz Curricular) deve ser informado.");
//        }
    }

	public void adicionarObjGradeCurricularVOs(GradeCurricularVO obj) throws Exception {
	validarDadosMatrizCurricular(obj);
	Iterator<GradeCurricularVO> i = getGradeCurricularVOs().iterator();
	while (i.hasNext()) {
		GradeCurricularVO objExistente = (GradeCurricularVO) i.next();
		if (objExistente.getNome().trim().toLowerCase().equals(obj.getNome().trim().toLowerCase()) && !objExistente.getCodigo().equals(obj.getCodigo())) {
			throw new Exception("Já existe uma matriz curricular cadastrada com este nome.");
		}
	}
	int index = 0;
	boolean achou = false;
	i = getGradeCurricularVOs().iterator();
	while (i.hasNext()) {
		GradeCurricularVO objExistente = (GradeCurricularVO) i.next();
		if (objExistente.getCodigo().equals(obj.getCodigo())) {
			getGradeCurricularVOs().set(index, obj);
			achou = true;
			break;
		}
		index++;
	}
	if (!achou) {
		getGradeCurricularVOs().add(obj);
	}
	Ordenacao.ordenarLista(getGradeCurricularVOs(), "situacao");
}

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>GradeCurricularVO</code> no List <code>gradeCurricularVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>GradeCurricular</code> - getNome() - como identificador (key) do
     * objeto no List.
     *
     * @param nome
     *            Parâmetro para localizar o objeto do List.
     */
    public GradeCurricularVO consultarObjGradeCurricularVO(String nome) throws Exception {
        Iterator<GradeCurricularVO> i = getGradeCurricularVOs().iterator();
        while (i.hasNext()) {
            GradeCurricularVO objExistente = (GradeCurricularVO) i.next();
            if (objExistente.getNome().equals(nome)) {
                return objExistente;
            }
        }
        return null;
    }

    public Boolean getSemestral() {
        if (getPeriodicidade().equals("SE")) {
            return true;
        }
        return false;
    }

    public Boolean getAnual() {
        if (getPeriodicidade().equals("AN")) {
            return true;
        }
        return false;
    }

    public Boolean getIntegral() {
        if (getPeriodicidade() != null && getPeriodicidade().equals("IN")) {
            return true;
        }
        return false;
    }

    public List<GradeCurricularVO> getGradeCurricularVOs() {
        if (gradeCurricularVOs == null) {
            gradeCurricularVOs = new ArrayList<GradeCurricularVO>();
        }
        return gradeCurricularVOs;
    }

    public void setGradeCurricularVOs(List<GradeCurricularVO> gradeCurricularVOs) {
        this.gradeCurricularVOs = gradeCurricularVOs;
    }

    /**
     * Retorna o objeto da classe <code>ConfiguracaoAcademico</code> relacionado
     * com (<code>Curso</code>).
     */
    @XmlElement(name = "configuracaoAcademico")
    public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
        if (configuracaoAcademico == null) {
            configuracaoAcademico = new ConfiguracaoAcademicoVO();
        }
        return (configuracaoAcademico);
    }

    /**
     * Define o objeto da classe <code>ConfiguracaoAcademico</code> relacionado
     * com (<code>Curso</code>).
     */
    public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO obj) {
        this.configuracaoAcademico = obj;
    }

//    /**
//     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
//     * (<code>Curso</code>).
//     */
//    @XmlElement(name = "areaConhecimento")
//    public AreaConhecimentoVO getAreaConhecimento() {
//        if (areaConhecimento == null) {
//            areaConhecimento = new AreaConhecimentoVO();
//        }
//        return (areaConhecimento);
//    }
//
//    /**
//     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
//     * <code>Curso</code>).
//     */
//    public void setAreaConhecimento(AreaConhecimentoVO obj) {
//        this.areaConhecimento = obj;
//    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>DocumentacaoCurso</code>.
     */
    public List<DocumentacaoCursoVO> getDocumentacaoCursoVOs() {
        if (documentacaoCursoVOs == null) {
            documentacaoCursoVOs = new ArrayList<>();
        }
        return (documentacaoCursoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>DocumentacaoCurso</code>.
     */
    public void setDocumentacaoCursoVOs(List<DocumentacaoCursoVO> documentacaoCursoVOs) {
        this.documentacaoCursoVOs = documentacaoCursoVOs;
    }

    public String getRegime() {
        return (regime);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getRegime_Apresentar() {
        if (regime == null) {
            return Constantes.EMPTY;
        }
        if (regime.equals("CR")) {
            return "Crédito";
        }
        if (regime.equals("SE")) {
            return "Seriado";
        }
        return (regime);
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getRegimeAprovacao() {
        return (regimeAprovacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getRegimeAprovacao_Apresentar() {
        if (regimeAprovacao.equals("IN")) {
            return "Integral";
        }
        if (regimeAprovacao.equals("PE")) {
            return "Período Letivo";
        }
        if (regimeAprovacao.equals("DI")) {
            return "Disciplina";
        }
        return (regimeAprovacao);
    }

    public void setRegimeAprovacao(String regimeAprovacao) {
        this.regimeAprovacao = regimeAprovacao;
    }

    @XmlElement(name = "periodicidade")
    public String getPeriodicidade() {
    	if (periodicidade == null) {
    		periodicidade = Constantes.EMPTY;
    	}
        return (periodicidade);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getPeriodicidade_Apresentar() {
        if (periodicidade.equals("IN")) {
            return "Integral";
        }
        if (periodicidade.equals("AN")) {
            return "Anual";
        }
        if (periodicidade.equals("SE")) {
            return "Semestral";
        }
        return (periodicidade);
    }

    public void setPeriodicidade(String periodicidade) {
        this.periodicidade = periodicidade;
    }

    /**
     * Data criação corresponde a data autorização
     */
    public Date getDataCriacao() {
        return (dataCriacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataCriacao_Apresentar() {
        return (Uteis.getData(dataCriacao));
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * Numero registro interno corresponde a Autorização
     */
    public String getNrRegistroInterno() {
        return (nrRegistroInterno);
    }

    public void setNrRegistroInterno(String nrRegistroInterno) {
        this.nrRegistroInterno = nrRegistroInterno;
    }

    public Date getDataPublicacaoDO() {
        return (dataPublicacaoDO);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataPublicacaoDO_Apresentar() {
        return (Uteis.getData(dataPublicacaoDO));
    }

    public void setDataPublicacaoDO(Date dataPublicacaoDO) {
        this.dataPublicacaoDO = dataPublicacaoDO;
    }

    public Integer getNrPeriodoLetivo() {
        if (getIntegral()) {
            nrPeriodoLetivo = new Integer(1);
        }
        return (nrPeriodoLetivo);
    }

    public void setNrPeriodoLetivo(Integer nrPeriodoLetivo) {
        this.nrPeriodoLetivo = nrPeriodoLetivo;
    }

    @XmlElement(name = "nivelEducacional")
    public String getNivelEducacional() {
        if (nivelEducacional == null) {
            nivelEducacional = Constantes.EMPTY;
        }
        return (nivelEducacional);
    }

    public Boolean utilizaProcessoMatriculaCalendario() {
        if (getNivelEducacional().equals("PO")) {
            return false;
        }
        return true;
    }
    
    public Boolean getNivelEducacionalComProgramacaoAulaSemanal() {
        if (getNivelEducacional().equals(TipoNivelEducacional.SUPERIOR.getValor())) {
            return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor())) {
            return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.MESTRADO.getValor())) {
            return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.SEQUENCIAL.getValor())) {
            return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.MEDIO.getValor())) {
            return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.BASICO.getValor())) {
            return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.INFANTIL.getValor())) {
        	return true;
        }
        if (getNivelEducacional().equals(TipoNivelEducacional.PROFISSIONALIZANTE.getValor())) {
        	return true;
        }
        return false;
    }

    public Boolean getNivelEducacionalGraduacao() {
        if (getNivelEducacional().equals("SU")) {
            return true;
        }
        return false;
    }

    public Boolean getNivelEducacionalPosGraduacao() {
        if (getNivelEducacional().equals("PO") || getNivelEducacional().equals("EX") || getNivelEducacional().equals("MT")) {
            return true;
        }
        return false;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getNivelEducacional_Apresentar() {
        return TipoNivelEducacional.getDescricao(getNivelEducacional());
    }

    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }

    public String getPerfilEgresso() {
        return (perfilEgresso);
    }

    public void setPerfilEgresso(String perfilEgresso) {
        this.perfilEgresso = perfilEgresso;
    }

    public String getPublicoAlvo() {
        return (publicoAlvo);
    }

    public void setPublicoAlvo(String publicoAlvo) {
        this.publicoAlvo = publicoAlvo;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObjetivos() {
        return (objetivos);
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

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

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>CursoTurno</code>.
     */
    public List<CursoTurnoVO> getCursoTurnoVOs() {
        if (cursoTurnoVOs == null) {
            cursoTurnoVOs = new ArrayList<>(0);
        }
        return (cursoTurnoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>CursoTurno</code>.
     */
    public void setCursoTurnoVOs(List<CursoTurnoVO> cursoTurnoVOs) {
        this.cursoTurnoVOs = cursoTurnoVOs;
    }

    public Integer getIdCursoInep() {
        if (idCursoInep == null) {
            idCursoInep = 0;
        }
        return idCursoInep;
    }

    public void setIdCursoInep(Integer idCursoInep) {
        this.idCursoInep = idCursoInep;
    }

    /**
     * @return the habilitacao
     */
    public String getHabilitacao() {
        if (habilitacao == null) {
            habilitacao = Constantes.EMPTY;
        }
        return habilitacao;
    }

    /**
     * @param habilitacao
     *            the habilitacao to set
     */
    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }

    public String getTitulo_Apresentar() {
        if (getTitulo().equals("NO")) {
            return "Normal";
        }
        if (getTitulo().equals("TE")) {
            return "Técnico";
        }
        if (getTitulo().equals("TC")) {
            return "Tecnólogo";
        }
        if (getTitulo().equals("LS")) {
            return "Lato Sensu";
        }
        if (getTitulo().equals("SS")) {
            return "Stricto Sensu";
        }
        if (getTitulo().equals("BA")) {
            return "Bacharelado";
        }
        if (getTitulo().equals("LI")) {
            return "Licenciatura";
        }
        if (getTitulo().equals("SE")) {
            return "Sequencial";
        }
        if (getTitulo().equals(TituloCursoPos.RESIDENCIA_MEDICA.getValor())) {
        	return TituloCursoPos.RESIDENCIA_MEDICA.getDescricao();
        }
        if (getTitulo().equals("")) {
            return Constantes.EMPTY;
        }
        return (titulo);
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        if (titulo == null) {
            titulo = Constantes.EMPTY;
        }
        return titulo;
    }

    /**
     * @param titulo
     *            the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isInserirNoProcessoMatricula() {
        return inserirNoProcessoMatricula;
    }

    public void setInserirNoProcessoMatricula(boolean inserirNoProcessoMatricula) {
        this.inserirNoProcessoMatricula = inserirNoProcessoMatricula;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getAbreviatura() {
        if (abreviatura == null) {
            abreviatura = Constantes.EMPTY;
        }
        return abreviatura;
    }

    public List<CursoCoordenadorVO> getCursoCoordenadorVOs() {
        if (cursoCoordenadorVOs == null) {
            cursoCoordenadorVOs = new ArrayList<>(0);
        }
        return cursoCoordenadorVOs;
    }

    public void setCursoCoordenadorVOs(List<CursoCoordenadorVO> cursoCoordenadorVOs) {
        this.cursoCoordenadorVOs = cursoCoordenadorVOs;
    }

    public String getTitulacaoDoFormando() {
        if (titulacaoDoFormando == null) {
            titulacaoDoFormando = Constantes.EMPTY;
        }
        return titulacaoDoFormando;
    }

    public void setTitulacaoDoFormando(String titulacaoDoFormando) {
        this.titulacaoDoFormando = titulacaoDoFormando;
    }

    public List<AutorizacaoCursoVO> getAutorizacaoCursoVOs() {
        if (autorizacaoCursoVOs == null) {
            autorizacaoCursoVOs = new ArrayList<>(0);
        }
        return autorizacaoCursoVOs;
    }

    public void setAutorizacaoCursoVOs(List<AutorizacaoCursoVO> autorizacaoCursoVOs) {
        this.autorizacaoCursoVOs = autorizacaoCursoVOs;
    }

    public List<MaterialCursoVO> getMaterialCursoVOs() {
        if (materialCursoVOs == null) {
            materialCursoVOs = new ArrayList<MaterialCursoVO>(0);
        }
        return materialCursoVOs;
    }

    public void setMaterialCursoVOs(List<MaterialCursoVO> materialCursoVOs) {
        this.materialCursoVOs = materialCursoVOs;
    }

    public Integer getNumeroExemplaresBibliograficoBasica() {
        if (numeroExemplaresBibliograficoBasica == null) {
            numeroExemplaresBibliograficoBasica = 0;
        }
        return numeroExemplaresBibliograficoBasica;
    }

    public void setNumeroExemplaresBibliograficoBasica(
            Integer numeroExemplaresBibliograficoBasica) {
        this.numeroExemplaresBibliograficoBasica = numeroExemplaresBibliograficoBasica;
    }

    public Integer getNumeroExemplaresBibliograficoComplementar() {
        if (numeroExemplaresBibliograficoComplementar == null) {
            numeroExemplaresBibliograficoComplementar = 0;
        }
        return numeroExemplaresBibliograficoComplementar;
    }

    public void setNumeroExemplaresBibliograficoComplementar(
            Integer numeroExemplaresBibliograficoComplementar) {
        this.numeroExemplaresBibliograficoComplementar = numeroExemplaresBibliograficoComplementar;
    }

    public Integer getTempoDefasagemPadraoCatalogoBibliograficaBasica() {
        if (tempoDefasagemPadraoCatalogoBibliograficaBasica == null) {
            tempoDefasagemPadraoCatalogoBibliograficaBasica = 0;
        }
        return tempoDefasagemPadraoCatalogoBibliograficaBasica;
    }

    public void setTempoDefasagemPadraoCatalogoBibliograficaBasica(
            Integer tempoDefasagemPadraoCatalogoBibliograficaBasica) {
        this.tempoDefasagemPadraoCatalogoBibliograficaBasica = tempoDefasagemPadraoCatalogoBibliograficaBasica;
    }

    public Integer getTempoDefasagemPadraoCatalogoBibliograficaComplementar() {
        if (tempoDefasagemPadraoCatalogoBibliograficaComplementar == null) {
            tempoDefasagemPadraoCatalogoBibliograficaComplementar = 0;
        }
        return tempoDefasagemPadraoCatalogoBibliograficaComplementar;
    }

    public void setTempoDefasagemPadraoCatalogoBibliograficaComplementar(
            Integer tempoDefasagemPadraoCatalogoBibliograficaComplementar) {
        this.tempoDefasagemPadraoCatalogoBibliograficaComplementar = tempoDefasagemPadraoCatalogoBibliograficaComplementar;
    }

    public Boolean getLimitarQtdeDiasMaxDownload() {
        if (limitarQtdeDiasMaxDownload == null) {
            limitarQtdeDiasMaxDownload = false;
        }
        return limitarQtdeDiasMaxDownload;
    }

    public void setLimitarQtdeDiasMaxDownload(Boolean limitarQtdeDiasMaxDownload) {
        this.limitarQtdeDiasMaxDownload = limitarQtdeDiasMaxDownload;
    }

    public Integer getQtdeMaxDiasDownload() {
        if (qtdeMaxDiasDownload == null) {
            qtdeMaxDiasDownload = 0;
        }
        return qtdeMaxDiasDownload;
    }

    public void setQtdeMaxDiasDownload(Integer qtdeMaxDiasDownload) {
        this.qtdeMaxDiasDownload = qtdeMaxDiasDownload;
    }

    public CursoVO getClone() throws Exception {
        return (CursoVO) super.clone();
    }

    /**
     * @return the funcionarioRespPreInscricao
     */
    public FuncionarioVO getFuncionarioRespPreInscricao() {
        if (funcionarioRespPreInscricao == null) {
            funcionarioRespPreInscricao = new FuncionarioVO();
        }
        return funcionarioRespPreInscricao;
    }

    /**
     * @param funcionarioRespPreInscricao the funcionarioRespPreInscricao to set
     */
    public void setFuncionarioRespPreInscricao(FuncionarioVO funcionarioRespPreInscricao) {
        this.funcionarioRespPreInscricao = funcionarioRespPreInscricao;
    }

    public String getScript() {
        if (script == null) {
            script = Constantes.EMPTY;
        }
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Integer getQuantidadeDisciplinasOptativasExpedicaoDiploma() {
        if (quantidadeDisciplinasOptativasExpedicaoDiploma == null) {
            quantidadeDisciplinasOptativasExpedicaoDiploma = 0;
        }
        return quantidadeDisciplinasOptativasExpedicaoDiploma;
    }

    public void setQuantidadeDisciplinasOptativasExpedicaoDiploma(Integer quantidadeDisciplinasOptativasExpedicaoDiploma) {
        this.quantidadeDisciplinasOptativasExpedicaoDiploma = quantidadeDisciplinasOptativasExpedicaoDiploma;
    }

    public Boolean getLiberarRegistroAulaEntrePeriodo() {
        if (liberarRegistroAulaEntrePeriodo == null) {
            liberarRegistroAulaEntrePeriodo = Boolean.FALSE;
        }
        return liberarRegistroAulaEntrePeriodo;
    }

    public void setLiberarRegistroAulaEntrePeriodo(
            Boolean liberarRegistroAulaEntrePeriodo) {
        this.liberarRegistroAulaEntrePeriodo = liberarRegistroAulaEntrePeriodo;
    }

    /**
     * @return the apresentarHomePreInscricao
     */
    public Boolean getApresentarHomePreInscricao() {
        return apresentarHomePreInscricao;
    }

    /**
     * @param apresentarHomePreInscricao the apresentarHomePreInscricao to set
     */
    public void setApresentarHomePreInscricao(Boolean apresentarHomePreInscricao) {
        this.apresentarHomePreInscricao = apresentarHomePreInscricao;
    }

    public ConfiguracaoTCCVO getConfiguracaoTCC() {
        if (configuracaoTCC == null) {
            configuracaoTCC = new ConfiguracaoTCCVO();
        }
        return configuracaoTCC;
    }

    public void setConfiguracaoTCC(ConfiguracaoTCCVO configuracaoTCC) {
        this.configuracaoTCC = configuracaoTCC;
    }

    public Boolean getCursoSelecionado() {
        if (cursoSelecionado == null) {
            cursoSelecionado = Boolean.FALSE;
        }
        return cursoSelecionado;
    }

    public void setCursoSelecionado(Boolean cursoSelecionado) {
        this.cursoSelecionado = cursoSelecionado;
    }

    /**
     * Parte diversifica é um controle utilizado no ensimo fundamental e médio
     * Nestes níveis eduacaionais existem disciplinas que são do núcleo comum
     * e disciplinas que são denomiadas diversificadas. Que fogem das disciplinas
     * padrões do nível determinada pelo MEC. Informática por exemplo, é um exemplo
     * de disciplina que é tratada como diversificada. No final, a diferença fica
     * por conta do histórico, que lista estas disciplinas em lugares separados.
     */
    public Boolean getUtilizaDisciplinaParteDiversificada() {
        if (this.getNivelEducacional().equals("IN")
                || this.getNivelEducacional().equals("BA")
                || this.getNivelEducacional().equals("ME")) {
            return true;
        }
        return false;
    }

	public String getTitulacaoDoFormandoFeminino() {
		if (titulacaoDoFormandoFeminino == null) {
			titulacaoDoFormandoFeminino = Constantes.EMPTY;
		}
		return titulacaoDoFormandoFeminino;
	}

	public void setTitulacaoDoFormandoFeminino(String titulacaoDoFormandoFeminino) {
		this.titulacaoDoFormandoFeminino = titulacaoDoFormandoFeminino;
	}

	public Integer getPercentualEvolucaoAcademicaIngressanteEnade() {
		if (percentualEvolucaoAcademicaIngressanteEnade == null) {
			percentualEvolucaoAcademicaIngressanteEnade = 0;
		}
		return percentualEvolucaoAcademicaIngressanteEnade;
	}

	public void setPercentualEvolucaoAcademicaIngressanteEnade(Integer percentualEvolucaoAcademicaIngressanteEnade) {
		this.percentualEvolucaoAcademicaIngressanteEnade = percentualEvolucaoAcademicaIngressanteEnade;
	}

	public Integer getPercentualEvolucaoAcademicaConcluinteEnade() {
		if (percentualEvolucaoAcademicaConcluinteEnade == null) {
			percentualEvolucaoAcademicaConcluinteEnade = 0;
		}
		return percentualEvolucaoAcademicaConcluinteEnade;
	}

	public void setPercentualEvolucaoAcademicaConcluinteEnade(Integer percentualEvolucaoAcademicaConcluinteEnade) {
		this.percentualEvolucaoAcademicaConcluinteEnade = percentualEvolucaoAcademicaConcluinteEnade;
	}

	public String getAnoMudancaLegislacao() {
		if (anoMudancaLegislacao == null) {
			anoMudancaLegislacao = Constantes.EMPTY;
		}
		return anoMudancaLegislacao;
	}

	public void setAnoMudancaLegislacao(String anoMudancaLegislacao) {
		this.anoMudancaLegislacao = anoMudancaLegislacao;
	}

	public String getPreposicaoNomeCurso() {
		if (preposicaoNomeCurso == null) {
			preposicaoNomeCurso = "de";
		}
		return preposicaoNomeCurso;
	}

	public void setPreposicaoNomeCurso(String preposicaoNomeCurso) {
		this.preposicaoNomeCurso = preposicaoNomeCurso;
	}

	public Boolean getFiltrarCursoVO() {
		if (filtrarCursoVO == null) {
			filtrarCursoVO = false;
		}
		return filtrarCursoVO;
	}

	public void setFiltrarCursoVO(Boolean filtrarCursoVO) {
		this.filtrarCursoVO = filtrarCursoVO;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CursoVO other = (CursoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public String getNomeDocumentacao() {
		if (nomeDocumentacao == null) {
			nomeDocumentacao = Constantes.EMPTY;
		}
		return nomeDocumentacao;
	}

	public void setNomeDocumentacao(String nomeDocumentacao) {
		this.nomeDocumentacao = nomeDocumentacao;
	}
	
	public String getTitulacaoMasculinoApresentarDiploma() {
		if (titulacaoMasculinoApresentarDiploma == null) {
			titulacaoMasculinoApresentarDiploma = Constantes.EMPTY;
		}
		return titulacaoMasculinoApresentarDiploma;
	}

	public void setTitulacaoMasculinoApresentarDiploma(
			String titulacaoMasculinoApresentarDiploma) {
		this.titulacaoMasculinoApresentarDiploma = titulacaoMasculinoApresentarDiploma;
	}

	public String getTitulacaoFemininoApresentarDiploma() {
		if (titulacaoFemininoApresentarDiploma == null) {
			titulacaoFemininoApresentarDiploma = Constantes.EMPTY;
		}
		return titulacaoFemininoApresentarDiploma;
	}

	public void setTitulacaoFemininoApresentarDiploma(
			String titulacaoFemininoApresentarDiploma) {
		this.titulacaoFemininoApresentarDiploma = titulacaoFemininoApresentarDiploma;
	}
	
	public String getCodigoContabil() {
		if (codigoContabil == null) {
			codigoContabil = Constantes.EMPTY;
		}
		return codigoContabil;
	}

	public void setCodigoContabil(String codigoContabil) {
		this.codigoContabil = codigoContabil;
	}

	public String getNomeContabil() {
		if (nomeContabil == null) {
			nomeContabil = Constantes.EMPTY;
		}
		return nomeContabil;
	}

	public void setNomeContabil(String nomeContabil) {
		this.nomeContabil = nomeContabil;
	}

	public String getNomeCursoEmRelatorio(){
		return Uteis.isAtributoPreenchido(getNomeDocumentacao()) && !getNomeDocumentacao().trim().isEmpty() ? getNomeDocumentacao() : getNome();
	}
	
	public Timestamp getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null)
			dataUltimaAlteracao = Uteis.getDataJDBCTimestamp(new Date());
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Timestamp dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	
	public Boolean getApresentarCursoBiblioteca() {
		if (apresentarCursoBiblioteca == null) {
			apresentarCursoBiblioteca = Boolean.TRUE;
		}
		return apresentarCursoBiblioteca;
	}
	
	public void setApresentarCursoBiblioteca(Boolean apresentarCursoBiblioteca) {
		this.apresentarCursoBiblioteca = apresentarCursoBiblioteca;
	}
	
	public String getNivelContabil() {
		if (nivelContabil == null) {
			nivelContabil = Constantes.EMPTY;
		}
		return nivelContabil;
	}
	
	public void setNivelContabil(String nivelContabil) {
		this.nivelContabil = nivelContabil;
	}
	
	public String getNotificações() {
		if (notificações == null) {
			notificações = Constantes.EMPTY;
		}
		return notificações;
	}
	
	public void setNotificações(String notificações) {
		this.notificações = notificações;
	}
	
	

	public GrupoPessoaVO getGrupoPessoaAnaliseRelatorioFinalEstagioVO() {
		if (grupoPessoaAnaliseRelatorioFinalEstagioVO == null) {
			grupoPessoaAnaliseRelatorioFinalEstagioVO = new GrupoPessoaVO();
		}
		return grupoPessoaAnaliseRelatorioFinalEstagioVO;
	}

	public void setGrupoPessoaAnaliseRelatorioFinalEstagioVO(GrupoPessoaVO grupoPessoaAnaliseRelatorioFinalEstagioVO) {
		this.grupoPessoaAnaliseRelatorioFinalEstagioVO = grupoPessoaAnaliseRelatorioFinalEstagioVO;
	}

	public GrupoPessoaVO getGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO() {
		if (grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO == null) {
			grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO = new GrupoPessoaVO();
		}
		return grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO;
	}

	public void setGrupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO(GrupoPessoaVO grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO) {
		this.grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO = grupoPessoaAnaliseAproveitamentoEstagioObrigatorioVO;
	}

	public GrupoPessoaVO getGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO() {
		if (grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO == null) {
			grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO = new GrupoPessoaVO();
		}
		return grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO;
	}

	public void setGrupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO(GrupoPessoaVO grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO) {
		this.grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO = grupoPessoaAnaliseEquivalenciaEstagioObrigatorioVO;
	}

	public FuncionarioVO getFuncionarioResponsavelAssinaturaTermoEstagioVO() {
		if (funcionarioResponsavelAssinaturaTermoEstagioVO == null) {
			funcionarioResponsavelAssinaturaTermoEstagioVO = new FuncionarioVO();
		}
		return funcionarioResponsavelAssinaturaTermoEstagioVO;
	}

	public void setFuncionarioResponsavelAssinaturaTermoEstagioVO(FuncionarioVO funcionarioResponsavelAssinaturaTermoEstagioVO) {
		this.funcionarioResponsavelAssinaturaTermoEstagioVO = funcionarioResponsavelAssinaturaTermoEstagioVO;
	}

	public String getIdConteudoMasterBlackboardEstagio() {
		if (idConteudoMasterBlackboardEstagio == null) {
			idConteudoMasterBlackboardEstagio = Constantes.EMPTY;
		}
		return idConteudoMasterBlackboardEstagio;
	}

	public void setIdConteudoMasterBlackboardEstagio(String idConteudoMasterBlackboardEstagio) {
		this.idConteudoMasterBlackboardEstagio = idConteudoMasterBlackboardEstagio;
	}

	public ModalidadeDisciplinaEnum getModalidadeCurso() {
		if (modalidadeCurso == null) {
			modalidadeCurso = ModalidadeDisciplinaEnum.PRESENCIAL;
		}
		return modalidadeCurso;
	}

	public void setModalidadeCurso(ModalidadeDisciplinaEnum modalidadeCurso) {
		this.modalidadeCurso = modalidadeCurso;
	}

	public Boolean getUtilizarRecursoAvaTerceiros() {
		if(utilizarRecursoAvaTerceiros == null) {
			utilizarRecursoAvaTerceiros = false;
		}
		return utilizarRecursoAvaTerceiros;
	}

	public void setUtilizarRecursoAvaTerceiros(Boolean utilizarRecursoAvaTerceiros) {
		this.utilizarRecursoAvaTerceiros = utilizarRecursoAvaTerceiros;
	}

    public QuestionarioVO getQuestionarioVO() {
        if (questionarioVO == null) {
            questionarioVO = new QuestionarioVO();
        }
        return questionarioVO;
    }

    public void setQuestionarioVO(QuestionarioVO questionarioVO) {
        this.questionarioVO = questionarioVO;
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

	public String getTextoDeclaracaoPPI() {
		if(textoDeclaracaoPPI == null ) {
			textoDeclaracaoPPI = Constantes.EMPTY;
		}
		return textoDeclaracaoPPI;
	}

	public void setTextoDeclaracaoPPI(String textoDeclaracaoPPI) {
		this.textoDeclaracaoPPI = textoDeclaracaoPPI;
	}

	
	public String getTextoDeclaracaoEscolaridadePublica() {
		if(textoDeclaracaoEscolaridadePublica == null ) {
			textoDeclaracaoEscolaridadePublica =Constantes.EMPTY;
		}
		return textoDeclaracaoEscolaridadePublica;
	}

	public void setTextoDeclaracaoEscolaridadePublica(
			String textoDeclaracaoEscolaridadePublica) {
		this.textoDeclaracaoEscolaridadePublica = textoDeclaracaoEscolaridadePublica;
	}

	public String getUrlDeclaracaoNormasMatricula() {
		if(urlDeclaracaoNormasMatricula == null ) {
			urlDeclaracaoNormasMatricula =Constantes.EMPTY;
		}
		return urlDeclaracaoNormasMatricula;
	}

	public void setUrlDeclaracaoNormasMatricula(String urlDeclaracaoNormasMatricula) {
		this.urlDeclaracaoNormasMatricula = urlDeclaracaoNormasMatricula;
	}
	
	public PersonalizacaoMensagemAutomaticaVO getMensagemConfirmacaoNovaMatricula() {
		if(mensagemConfirmacaoNovaMatricula == null ) {
			mensagemConfirmacaoNovaMatricula = new PersonalizacaoMensagemAutomaticaVO(); 
		}
		return mensagemConfirmacaoNovaMatricula;
	}
	

	public void setMensagemConfirmacaoNovaMatricula(PersonalizacaoMensagemAutomaticaVO mensagemConfirmacaoNovaMatricula) {
		this.mensagemConfirmacaoNovaMatricula = mensagemConfirmacaoNovaMatricula;
	}
	
	public Boolean getHabilitarMensagemNotificacaoNovaMatricula() {
        if (habilitarMensagemNotificacaoNovaMatricula == null) {
        	habilitarMensagemNotificacaoNovaMatricula = false;
        }
        return habilitarMensagemNotificacaoNovaMatricula;
    }

    public void setHabilitarMensagemNotificacaoNovaMatricula(Boolean habilitarMensagemNotificacaoNovaMatricula) {
        this.habilitarMensagemNotificacaoNovaMatricula = habilitarMensagemNotificacaoNovaMatricula;
    }
	
	public PersonalizacaoMensagemAutomaticaVO getMensagemRenovacaoMatricula() {
		if(mensagemRenovacaoMatricula == null ) {
			mensagemRenovacaoMatricula = new PersonalizacaoMensagemAutomaticaVO(); 
		}
		return mensagemRenovacaoMatricula;
	}

	public void setMensagemRenovacaoMatricula(PersonalizacaoMensagemAutomaticaVO mensagemRenovacaoMatricula) {
		this.mensagemRenovacaoMatricula = mensagemRenovacaoMatricula;
	}
	
	public Boolean getHabilitarMensagemNotificacaoRenovacaoMatricula() {
        if (habilitarMensagemNotificacaoRenovacaoMatricula == null) {
        	habilitarMensagemNotificacaoRenovacaoMatricula = false;
        }
        return habilitarMensagemNotificacaoRenovacaoMatricula;
    }

    public void setHabilitarMensagemNotificacaoRenovacaoMatricula(Boolean habilitarMensagemNotificacaoRenovacaoMatricula) {
        this.habilitarMensagemNotificacaoRenovacaoMatricula = habilitarMensagemNotificacaoRenovacaoMatricula;
    }
	
	public String getTextoConfirmacaoNovaMatricula() {
		if(textoConfirmacaoNovaMatricula == null ) {
			textoConfirmacaoNovaMatricula =Constantes.EMPTY; 
		}
		return textoConfirmacaoNovaMatricula;
	}

	public void setTextoConfirmacaoNovaMatricula(String textoConfirmacaoNovaMatricula) {
		this.textoConfirmacaoNovaMatricula = textoConfirmacaoNovaMatricula;
	}
	
	
	  public List<SelectItem> getListaSelectItemTemplateMensagemAutomaticaEnum() {
	        if (listaSelectItemTemplateMensagemAutomaticaEnum == null) {
	        	listaSelectItemTemplateMensagemAutomaticaEnum = new ArrayList<>();
	        }
	        return listaSelectItemTemplateMensagemAutomaticaEnum;
	    }

	    public void setListaSelectItemTemplateMensagemAutomaticaEnum(List<SelectItem> listaSelectItemTemplateMensagemAutomaticaEnum) {
	        this.listaSelectItemTemplateMensagemAutomaticaEnum = listaSelectItemTemplateMensagemAutomaticaEnum;
	    }

	public String getTextoDeclaracaoBolsasAuxilios() {
		if(textoDeclaracaoBolsasAuxilios == null ) {
			textoDeclaracaoBolsasAuxilios =Constantes.EMPTY;
		}
		return textoDeclaracaoBolsasAuxilios;
	}

	public void setTextoDeclaracaoBolsasAuxilios(
			String textoDeclaracaoBolsasAuxilios) {
		this.textoDeclaracaoBolsasAuxilios = textoDeclaracaoBolsasAuxilios;
	}
	
    public Boolean getAtivarPreMatriculaAposEntregaDocumentosObrigatorios() {
    	if(ativarPreMatriculaAposEntregaDocumentosObrigatorios == null) {
    		ativarPreMatriculaAposEntregaDocumentosObrigatorios = Boolean.FALSE;
    	}
		return ativarPreMatriculaAposEntregaDocumentosObrigatorios;
	}

	public void setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(
			Boolean ativarPreMatriculaAposEntregaDocumentosObrigatorios) {
		this.ativarPreMatriculaAposEntregaDocumentosObrigatorios = ativarPreMatriculaAposEntregaDocumentosObrigatorios;
	}

	public Boolean getPermitirAssinarContratoPendenciaDocumentacao() {
		if(permitirAssinarContratoPendenciaDocumentacao == null ) {
			permitirAssinarContratoPendenciaDocumentacao = Boolean.TRUE;
		}
		return permitirAssinarContratoPendenciaDocumentacao;
	}

	public void setPermitirAssinarContratoPendenciaDocumentacao(Boolean permitirAssinarContratoPendenciaDocumentacao) {
		this.permitirAssinarContratoPendenciaDocumentacao = permitirAssinarContratoPendenciaDocumentacao;
	}
	
	public Integer getCount() {
        if (count == null) {
        	count = 0;
        }
        return count;
    }

    public void setCount(Integer Count) {
        this.count = Count;
    }
	
	
	public String getTags_Apresentar() {
		StringBuilder tagsApresentar = null;
        if (tagsApresentar == null) {
            tagsApresentar = new StringBuilder(Constantes.EMPTY);
            String virgula = Constantes.EMPTY;
            for (TagsTextoFinalizacaoMatriculaEnum tag : TagsTextoFinalizacaoMatriculaEnum.values()) {
            	if(!tag.equals(TagsTextoFinalizacaoMatriculaEnum.CURSO_ALUNO) && 
            	   !tag.equals(TagsTextoFinalizacaoMatriculaEnum.MATRICULA_ALUNO) && 
            	   !tag.equals(TagsTextoFinalizacaoMatriculaEnum.EMAIL_ALUNO)) {

                tagsApresentar.append(virgula).append(tag.toString());
                virgula = ", ";
                }
            }
        }
        return tagsApresentar.toString();

    }

	public EixoCursoVO getEixoCursoVO() {
		if(eixoCursoVO == null) {
			eixoCursoVO = new EixoCursoVO();
		}
		return eixoCursoVO;
	}

	public void setEixoCursoVO(EixoCursoVO eixoCursoVO) {
		this.eixoCursoVO = eixoCursoVO;
	}public Boolean getSelecionado() {
	if(selecionado == null) {
		selecionado =  false;
	}
	return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Boolean getAtivarMatriculaAposAssinaturaContrato() {
		if(ativarMatriculaAposAssinaturaContrato == null ) {
			ativarMatriculaAposAssinaturaContrato=Boolean.FALSE;
		}
		return ativarMatriculaAposAssinaturaContrato;
	}

	public void setAtivarMatriculaAposAssinaturaContrato(Boolean ativarMatriculaAposAssinaturaContrato) {
		this.ativarMatriculaAposAssinaturaContrato = ativarMatriculaAposAssinaturaContrato;
	}

	public Boolean getHabilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento() {
		if(habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento == null) {
			habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento = Uteis.isAtributoPreenchido(getMensagemAtivacaoPreMatriculaEntregaDocumento());
		}
		return habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento;
	}

	public void setHabilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento(
			Boolean habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento) {
		this.habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento = habilitarMensagemNotificacaoAtivacaoPreMatriculaEntregaDocumento;
	}

	public PersonalizacaoMensagemAutomaticaVO getMensagemAtivacaoPreMatriculaEntregaDocumento() {
		if(mensagemAtivacaoPreMatriculaEntregaDocumento == null) {
			mensagemAtivacaoPreMatriculaEntregaDocumento =  new PersonalizacaoMensagemAutomaticaVO();
		}
		return mensagemAtivacaoPreMatriculaEntregaDocumento;
	}

	public void setMensagemAtivacaoPreMatriculaEntregaDocumento(
			PersonalizacaoMensagemAutomaticaVO mensagemAtivacaoPreMatriculaEntregaDocumento) {
		this.mensagemAtivacaoPreMatriculaEntregaDocumento = mensagemAtivacaoPreMatriculaEntregaDocumento;
	}

	public TextoPadraoVO getTextoPadraoContratoMatriculaCalouro() {
		if (textoPadraoContratoMatriculaCalouro == null) {
			 textoPadraoContratoMatriculaCalouro = new TextoPadraoVO();
	    }	      
		return textoPadraoContratoMatriculaCalouro;
	}

	public void setTextoPadraoContratoMatriculaCalouro(TextoPadraoVO textoPadraoContratoMatriculaCalouro) {
		this.textoPadraoContratoMatriculaCalouro = textoPadraoContratoMatriculaCalouro;
	}
	
	public List getListaSelectItemTextoPadraoContratoMatriculaCalouro() {
        if (listaSelectItemTextoPadraoContratoMatriculaCalouro == null) {
     	   listaSelectItemTextoPadraoContratoMatriculaCalouro = new ArrayList(0);
        }
        return listaSelectItemTextoPadraoContratoMatriculaCalouro;
    }

  
    public void setListaSelectItemTextoPadraoContratoMatriculaCalouro(List listaSelectItemTextoPadraoContratoMatriculaCalouro) {
        this.listaSelectItemTextoPadraoContratoMatriculaCalouro = listaSelectItemTextoPadraoContratoMatriculaCalouro;
    }
	


	public Boolean getPermitirOutraMatriculaMesmoAluno() {
		if(permitirOutraMatriculaMesmoAluno == null ) {
			permitirOutraMatriculaMesmoAluno=Boolean.FALSE;
		}
		return permitirOutraMatriculaMesmoAluno;
	}

	public void setPermitirOutraMatriculaMesmoAluno(Boolean permitirOutraMatriculaMesmoAluno) {
		this.permitirOutraMatriculaMesmoAluno = permitirOutraMatriculaMesmoAluno;
	}
	
	public Boolean getNivelEducacionalGraduacaoGraduacaoTecnologica() {
		if (getNivelEducacional().equals("SU") || getNivelEducacional().equals("GT")) {
			return true;
		}
		return false;
	}
	
	public Boolean getNivelEducacionalGraduacaoTecnologica() {
		if (getNivelEducacional().equals("GT")) {
			return true;
		}
		return false;
	}
	
	public Boolean getPossuiCodigoEMEC() {
		if (possuiCodigoEMEC == null) {
			possuiCodigoEMEC = true;
		}
		return possuiCodigoEMEC;
	}

	public void setPossuiCodigoEMEC(Boolean possuiCodigoEMEC) {
		this.possuiCodigoEMEC = possuiCodigoEMEC;
	}

	public Integer getCodigoEMEC() {
		if (codigoEMEC == null) {
			codigoEMEC = 0;
		}
		return codigoEMEC;
	}

	public void setCodigoEMEC(Integer codigoEMEC) {
		this.codigoEMEC = codigoEMEC;
	}

	public Integer getNumeroProcessoEMEC() {
		if (numeroProcessoEMEC == null) {
			numeroProcessoEMEC = 0;
		}
		return numeroProcessoEMEC;
	}

	public void setNumeroProcessoEMEC(Integer numeroProcessoEMEC) {
		this.numeroProcessoEMEC = numeroProcessoEMEC;
	}

	public String getTipoProcessoEMEC() {
		if (tipoProcessoEMEC == null) {
			tipoProcessoEMEC = "";
		}
		return tipoProcessoEMEC;
	}

	public void setTipoProcessoEMEC(String tipoProcessoEMEC) {
		this.tipoProcessoEMEC = tipoProcessoEMEC;
	}

	public Date getDataCadastroEMEC() {
		return dataCadastroEMEC;
	}

	public void setDataCadastroEMEC(Date dataCadastroEMEC) {
		this.dataCadastroEMEC = dataCadastroEMEC;
	}

	public Date getDataProtocoloEMEC() {
		return dataProtocoloEMEC;
	}

	public void setDataProtocoloEMEC(Date dataProtocoloEMEC) {
		this.dataProtocoloEMEC = dataProtocoloEMEC;
	}

	public TipoAutorizacaoCursoEnum getTipoAutorizacaoCursoEnum() {
		return tipoAutorizacaoCursoEnum;
	}

	public void setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum tipoAutorizacaoCursoEnum) {
		this.tipoAutorizacaoCursoEnum = tipoAutorizacaoCursoEnum;
	}

	public String getNumeroAutorizacao() {
		if (numeroAutorizacao == null) {
			numeroAutorizacao = Constantes.EMPTY;
		}
		return numeroAutorizacao;
	}

	public void setNumeroAutorizacao(String numeroAutorizacao) {
		this.numeroAutorizacao = numeroAutorizacao;
	}
	
	public Date getDataCredenciamento() {
		return dataCredenciamento;
	}
	
	public void setDataCredenciamento(Date dataCredenciamento) {
		this.dataCredenciamento = dataCredenciamento;
	}
	
	public String getVeiculoPublicacao() {
		if (veiculoPublicacao == null) {
			veiculoPublicacao = Constantes.EMPTY;
		}
		return veiculoPublicacao;
	}
	
	public void setVeiculoPublicacao(String veiculoPublicacao) {
		this.veiculoPublicacao = veiculoPublicacao;
	}
	
	public Integer getSecaoPublicacao() {
		if (secaoPublicacao == null) {
			secaoPublicacao = 0;
		}
		return secaoPublicacao;
	}
	
	public void setSecaoPublicacao(Integer secaoPublicacao) {
		this.secaoPublicacao = secaoPublicacao;
	}
	
	public Integer getPaginaPublicacao() {
		if (paginaPublicacao == null) {
			paginaPublicacao = 0;
		}
		return paginaPublicacao;
	}

	public void setPaginaPublicacao(Integer paginaPublicacao) {
		this.paginaPublicacao = paginaPublicacao;
	}

	public Integer getNumeroDOU() {
		if (numeroDOU == null) {
			numeroDOU = 0;
		}
		return numeroDOU;
	}

	public void setNumeroDOU(Integer numeroDOU) {
		this.numeroDOU = numeroDOU;
	}
	
	public Boolean getAutorizacaoResolucaoEmTramitacao() {
		if (autorizacaoResolucaoEmTramitacao == null) {
			autorizacaoResolucaoEmTramitacao = Boolean.FALSE;
		}
		return autorizacaoResolucaoEmTramitacao;
	}

	public void setAutorizacaoResolucaoEmTramitacao(Boolean autorizacaoResolucaoEmTramitacao) {
		this.autorizacaoResolucaoEmTramitacao = autorizacaoResolucaoEmTramitacao;
	}

	public String getNumeroProcessoAutorizacaoResolucao() {
		if (numeroProcessoAutorizacaoResolucao == null) {
			numeroProcessoAutorizacaoResolucao = Constantes.EMPTY;
		}
		return numeroProcessoAutorizacaoResolucao;
	}

	public void setNumeroProcessoAutorizacaoResolucao(String numeroProcessoAutorizacaoResolucao) {
		this.numeroProcessoAutorizacaoResolucao = numeroProcessoAutorizacaoResolucao;
	}

	public String getTipoProcessoAutorizacaoResolucao() {
		if (tipoProcessoAutorizacaoResolucao == null) {
			tipoProcessoAutorizacaoResolucao = Constantes.EMPTY;
		}
		return tipoProcessoAutorizacaoResolucao;
	}

	public void setTipoProcessoAutorizacaoResolucao(String tipoProcessoAutorizacaoResolucao) {
		this.tipoProcessoAutorizacaoResolucao = tipoProcessoAutorizacaoResolucao;
	}

	public Date getDataCadastroAutorizacaoResolucao() {
		return dataCadastroAutorizacaoResolucao;
	}

	public void setDataCadastroAutorizacaoResolucao(Date dataCadastroAutorizacaoResolucao) {
		this.dataCadastroAutorizacaoResolucao = dataCadastroAutorizacaoResolucao;
	}

	public Date getDataProtocoloAutorizacaoResolucao() {
		return dataProtocoloAutorizacaoResolucao;
	}

	public void setDataProtocoloAutorizacaoResolucao(Date dataProtocoloAutorizacaoResolucao) {
		this.dataProtocoloAutorizacaoResolucao = dataProtocoloAutorizacaoResolucao;
	}
	
	public Date getDataHabilitacao() {
		return dataHabilitacao;
	}
			
	public void setDataHabilitacao(Date dataHabilitacao) {
		this.dataHabilitacao = dataHabilitacao;
	}
	
	public static void validarDadosDiplomaCurso(CursoVO obj) {
		if (obj != null) {
			if (obj.getAutorizacaoResolucaoEmTramitacao()) {
				obj.setNrRegistroInterno(null);
				obj.setTipoAutorizacaoCursoEnum(null);
				obj.setDataCredenciamento(null);
				obj.setDataPublicacaoDO(null);
				obj.setNumeroAutorizacao(null);
				obj.setVeiculoPublicacao(null);
				obj.setSecaoPublicacao(null);
				obj.setPaginaPublicacao(null);
				obj.setNumeroDOU(null);
			} else {
				obj.setNumeroProcessoAutorizacaoResolucao(null);
				obj.setTipoProcessoAutorizacaoResolucao(null);
				obj.setDataCadastroAutorizacaoResolucao(null);
				obj.setDataProtocoloAutorizacaoResolucao(null);
			}
			if (obj.getPossuiCodigoEMEC()) {
				obj.setTipoProcessoEMEC(null);
				obj.setNumeroProcessoEMEC(null);
				obj.setDataCadastroEMEC(null);
				obj.setDataProtocoloEMEC(null);
			} else {
				obj.setCodigoEMEC(null);
			}
		}
	}
}