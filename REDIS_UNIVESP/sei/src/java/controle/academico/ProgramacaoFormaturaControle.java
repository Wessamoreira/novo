package controle.academico;

import java.io.File;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas programacaoFormaturaForm.jsp
 * programacaoFormaturaCons.jsp) com as funcionalidades da classe <code>ProgramacaoFormatura</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ProgramacaoFormatura
 * @see ProgramacaoFormaturaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaCursoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ProgramacaoFormaturaControle")
@Scope("viewScope")
@Lazy
public class ProgramacaoFormaturaControle extends SuperControle implements Serializable {

    private ProgramacaoFormaturaVO programacaoFormaturaVO;
    protected List listaSelectItemUnidadeEnsino;
    private String campoConsultarCurso;
    private String valorConsultarCurso;
    private List listaConsultarCurso;
    protected List listaSelectItemTurno;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private String campoConsultarAluno;
    private String valorConsultarAluno;
    private List<MatriculaVO> listaConsultarAluno;
    private String campoConsultarRequerimento;
    private String valorConsultarRequerimento;
    private List listaConsultarRequerimento;
    protected List<SelectItem> listaSelectItemColacaoGrau;
    private String responsavelCadastro_Erro;
    private Date valorConsultaDataInicio;
    private Date valorConsultaDataFinal;
    protected List listaSelectItemTipoRequerimentos;
    protected List listaColouGrau;
    protected List<ContaReceberVO> listaContasReceberAluno;
    protected List<DocumetacaoMatriculaVO> listaDocumentosPendentes;
    protected List<DisciplinaVO> listaDisciplinasPendentes;
    private TurmaVO turmaVO;
    private CursoVO cursoVO;
    private TurnoVO turnoVO;
    private RequerimentoVO requerimentoVO;
    private MatriculaVO matriculaVO;
    private ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO;
    private Boolean habilitaUnidadeEnsino;
    private Boolean mostrarSegundoCampo;
    private Boolean mostrarPrimeiroCampo;
    private String aluno_Erro;
    private Date dataConclusaoCurso;
    private List listaSelectItemNivelEducacional;
    private boolean filtrarCurriculoIntegralizado;
    private List<TurnoVO> listaTurnoVOs;
    private String turnoDescricao;
    private Boolean filtrarRegistroEnade;
    private String cursoDescricao;
    private List<CursoVO> listaCursoVOs;
    private Integer textoPadrao;
    private List listaSelectItemTipoTextoPadrao;
    private List<ProgramacaoFormaturaCursoVO> programacaoFormaturaCursoVOs;
    private SuperControleRelatorio controleRel;
    private ImpressaoContratoVO impressaoContratoVO;
    private ImpressaoContratoVO impressaoContratoGravarVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private DocumentoAssinadoVO documentoAssinadoVO;
    private List<DocumentoAssinadoPessoaVO> documentoAssinadoPessoaVOs;
    private ProgramacaoFormaturaCursoVO programacaoFormaturaCursoVO;
    private CursoVO cursoProgramacao;
    private String motivoRejeicao;
    private String ocompleteModal;
    private DataModelo controleCosultaOtimizadoAluno;
    private ControleConsulta controleConsultaAluno;
    private ProgressBarVO progressBarImpressaoAta;
    private Collection<List<ProgramacaoFormaturaAlunoVO>> resultadoProgramacaoFormaturaAlunoVOs;
    private boolean existeConfiguracaoGed = false;
    private boolean permiteAlunoAssinarAta = false;
    private String operacaoRealizarProgressBarImpressaoAta;
    private String avisoProgramacaoFormaturaDuplicada;
    private List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunoExcel;
    private List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosEncontrados;
    private List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosAusentes;
    private List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosNaoEncontrados;
    private String situacao;
    private String nomeArquivo;
    private List<SelectItem> listaSelectItemAcaoNaoComparecido;

    public ProgramacaoFormaturaControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
        setMostrarPrimeiroCampo(true);
        getProgramacaoFormaturaVO().setUnidadeEnsinoDescricao("");
        getControleConsulta().setCampoConsulta("colacaoGrau");
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ProgramacaoFormatura</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() throws Exception {
    	try {
    		setListaCursoVOs(new ArrayList<CursoVO>(0));
    		setListaTurnoVOs(new ArrayList<TurnoVO>(0));
    		removerObjetoMemoria(this);
    		setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
    		getProgramacaoFormaturaVO().setResponsavelCadastro(getUsuarioLogadoClone());
    		inicializarListasSelectItemTodosComboBox();
    		verificarPerfilUsuarioLogado();
    		setCursoVO(new CursoVO());
    		setTurmaVO(new TurmaVO());
    		setMatriculaVO(new MatriculaVO());
    		setRequerimentoVO(new RequerimentoVO());
    		setProgramacaoFormaturaAlunoVO(new ProgramacaoFormaturaAlunoVO());
    		getFacadeFactory().getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade().carregarUnidadeEnsinoNaoSelecionado(getProgramacaoFormaturaVO(), getUnidadeEnsinoLogado().getCodigo());
    		getProgramacaoFormaturaVO().setUnidadeEnsinoDescricao("");
    		montarListaTurno();
    		montarListaCurso();
    		setMensagemID("msg_entre_dados");
    		return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
	    	return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaCons.xhtml");
		}
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ProgramacaoFormatura</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
        setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));        
        obj.setNovoObj(Boolean.FALSE);
        inicializarListasSelectItemTodosComboBox();
        verificarPerfilUsuarioLogado();
        setCursoVO(new CursoVO());
        setTurmaVO(new TurmaVO());
        setMatriculaVO(new MatriculaVO());
        setRequerimentoVO(new RequerimentoVO());
        getProgramacaoFormaturaVO().setUnidadeEnsinoDescricao(null);
        getProgramacaoFormaturaVO().getUnidadeEnsinoDescricao();
        getFacadeFactory().getProgramacaoFormaturaUnidadeEnsinoInterfaceFacade().carregarUnidadeEnsinoNaoSelecionado(getProgramacaoFormaturaVO(), getUnidadeEnsinoLogado().getCodigo());
        verificarTodasUnidadesSelecionadas();
        montarListaCurso();
        montarListaTurno();
        getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().sort(Comparator.comparing(m -> m.getMatricula().getMatricula()));
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>ProgramacaoFormatura</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (programacaoFormaturaVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getProgramacaoFormaturaFacade().incluir(programacaoFormaturaVO,getConfiguracaoFinanceiroPadraoSistema(),getUsuarioLogado());
            } else {
            	getFacadeFactory().getProgramacaoFormaturaFacade().alterar(programacaoFormaturaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ProgramacaoFormaturaCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
        	List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
        	super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorProgramacaoUnidadeEnsino(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("identificadorTurmaTurma")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("colacaoGrau")) {
            	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorColacaoGrau(getControleConsulta().getValorConsulta(), null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeAluno")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeAluno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodoRequerimento") || getControleConsulta().getCampoConsulta().equals("periodoColacaoGrau") || getControleConsulta().getCampoConsulta().equals("periodoCadastro")) {
                objs = validarDataConsulta(objs);
            }
            if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorRegistroAcademico(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaCons.xhtml");
        }
    }

    public List validarDataConsulta(List objs) throws Exception {
        if (getValorConsultaDataFinal() != null && getValorConsultaDataInicio() != null) {
            if (getControleConsulta().getCampoConsulta().equals("periodoRequerimento")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicio(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinal(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodoColacaoGrau")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicio(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinal(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodoCadastro")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicio(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinal(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
        } else {
            throw new ConsistirException("Por favor digite uma data válida.");
        }

        return objs;
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ProgramacaoFormaturaVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getProgramacaoFormaturaFacade().excluir(programacaoFormaturaVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
        }
    }

    private DataModelo controleConsultaProgramacaoAluno;
    
    public DataModelo getControleConsultaProgramacaoAluno() {
		if (controleConsultaProgramacaoAluno == null) {
			controleConsultaProgramacaoAluno = new DataModelo();
		}
    	return controleConsultaProgramacaoAluno;
	}
    
    public void setControleConsultaProgramacaoAluno(DataModelo controleConsultaProgramacaoAluno) {
		this.controleConsultaProgramacaoAluno = controleConsultaProgramacaoAluno;
	}

    public void consultaOtimizada() {
    	try {
    		super.consultar();
    		getControleConsultaProgramacaoAluno().getListaConsulta().clear();
    		getControleConsultaProgramacaoAluno().setLimitePorPagina(10);
    		getControleConsultaProgramacaoAluno().setListaConsulta(getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarProgramacaoFormaturaAluno(getProgramacaoFormaturaVO().getCodigo(), getControleConsultaProgramacaoAluno(), getMatriculaAluno(), getNomeAluno(), getNomeCurso(),  Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));    		
    		setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
    	getControleConsultaProgramacaoAluno().setPaginaAtual(dataScrollEvent.getPage());
    	getControleConsultaProgramacaoAluno().setPage(dataScrollEvent.getPage());
    	consultaOtimizada();
    }


    public void montaListaContasReceberPendentes() throws Exception {
        setListaContasReceberAluno(new ArrayList<ContaReceberVO>(0));
        ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaAlunoItens");
        verificarContasReceberAluno(obj.getMatricula().getMatricula());
    }
    
    public void verificarContasReceberAluno(String matricula) throws Exception {
        setListaContasReceberAluno(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaSituacaoTipoOrigens(matricula, "RE", "'MAT', 'MEN', 'BIB', 'DCH', 'NCR', 'BCC', 'OUT', 'IRE', 'MDI'",  false, getUsuarioLogado()));
    }

    public void montaListaDocumentosPendentes() throws Exception {
        setListaDocumentosPendentes(new ArrayList<DocumetacaoMatriculaVO>(0));
        ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaAlunoItens");
        verificarDocumentosPendentes(obj.getMatricula().getMatricula());
    }    

    public void verificarDocumentosPendentes(String matricula) throws Exception {
        setListaDocumentosPendentes(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorSituacaoMatricula("PE", matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
    }

    public void montaListaDisciplinasPendentes() throws Exception {
        ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaAlunoItens");
        setProgramacaoFormaturaAlunoVO(obj);                      
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ProgramacaoFormaturaAluno</code> para
     * edição pelo usuário.
     */
    public String editarProgramacaoFormaturaAluno() throws Exception {
        ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaAlunoItens");
        setProgramacaoFormaturaAlunoVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ProgramacaoFormaturaAluno</code> do objeto
     * <code>programacaoFormaturaVO</code> da classe <code>ProgramacaoFormatura</code>
     */
    public String removerProgramacaoFormaturaAluno() throws Exception {
        ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaAlunoItens");
        if (Uteis.isAtributoPreenchido(obj) && obj.getColouGrau().equals("NI")) {
        	getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().remove(obj);
            setMensagemID("msg_dados_excluidos");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
//    public void consultarMatriculaPorChavePrimaria() {
//        try {
//            String campoConsulta = programacaoFormaturaAlunoVO.getMatricula().getMatricula();
//            MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_DADOSBASICOS), getUsuarioLogado());
//            programacaoFormaturaAlunoVO.getMatricula().setMatricula(matricula.getMatricula());
//            this.setAluno_Erro("");
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setMensagemID("msg_erro_dadosnaoencontrados");
//            programacaoFormaturaAlunoVO.getMatricula().setMatricula("");
//            programacaoFormaturaAlunoVO.getMatricula().setMatricula("");
//            this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
//        }
//    }

    /**
     * Método responsável por processar a consulta na entidade <code>Usuario</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarUsuarioPorChavePrimaria() {
        try {
            Integer campoConsulta = programacaoFormaturaVO.getResponsavelCadastro().getCodigo();
            UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            programacaoFormaturaVO.getResponsavelCadastro().setNome(usuario.getNome());
            this.setResponsavelCadastro_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            programacaoFormaturaVO.getResponsavelCadastro().setNome("");
            programacaoFormaturaVO.getResponsavelCadastro().setCodigo(0);
            this.setResponsavelCadastro_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>ColacaoGrau</code>.
     */
    public void montarListaSelectItemColacaoGrau(String prm) throws Exception {
    	getListaSelectItemColacaoGrau().clear();
    	getListaSelectItemColacaoGrau().add(new SelectItem(0, ""));
    	consultarColacaoGrauPorSituacao(prm).stream().map(this::getSelecItemColacaoGrau).forEach(getListaSelectItemColacaoGrau()::add);
    }
    
    private SelectItem getSelecItemColacaoGrau(ColacaoGrauVO colacaoGrauVO) {
    	return new SelectItem(colacaoGrauVO.getCodigo(), colacaoGrauVO.getTitulo().toString() + " - " + colacaoGrauVO.getData_Apresentar());
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ColacaoGrau</code>. Buscando todos os
     * objetos correspondentes a entidade <code>ColacaoGrau</code>. Esta rotina não recebe parâmetros para filtragem de
     * dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemColacaoGrau() {
        try {
            montarListaSelectItemColacaoGrau("AB");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>titulo</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarColacaoGrauPorTitulo(String tituloPrm) throws Exception {
        List lista = getFacadeFactory().getColacaoGrauFacade().consultarPorTitulo(tituloPrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    public List<ColacaoGrauVO> consultarColacaoGrauPorSituacao(String tituloPrm) throws Exception {
    	return getFacadeFactory().getColacaoGrauFacade().consultarPorSituacao(tituloPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Requerimento</code> por meio dos parametros
     * informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos
     * parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarRequerimento() {
        try {
        	List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
            List objs = new ArrayList(0);
            if (getCampoConsultarRequerimento().equals("codigo")) {
                if (getValorConsultarRequerimento().equals("")) {
                    setValorConsultarRequerimento("0");
                }
                Integer valorInt = Integer.parseInt(getValorConsultarRequerimento());
                objs = getFacadeFactory().getRequerimentoFacade().consultarPorCodigoSemProgramacaoFormatura(valorInt, "CG", listaProgramacao, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            }
            if (getCampoConsultarRequerimento().equals("data")) {
                Date valorData = Uteis.getDate(getValorConsultarRequerimento());
                objs = getFacadeFactory().getRequerimentoFacade().consultarPorDataSemProgramacaoFormatura(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "CG", listaProgramacao, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            }
            if (getCampoConsultarRequerimento().equals("situacao")) {
                objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacaoSemProgramacaoFormatura(getValorConsultarRequerimento(), "CG", listaProgramacao, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            }
            if (getCampoConsultarRequerimento().equals("nomePessoa")) {
                objs = getFacadeFactory().getRequerimentoFacade().consultarPorResponsavelSemProgramacaoFormatura(getValorConsultarRequerimento(), "CG", listaProgramacao, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            }
            if (getCampoConsultarRequerimento().equals("matricula")) {
                objs = getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaMatriculaSemProgramacaoFormatura(getValorConsultarRequerimento(), "CG", listaProgramacao, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            }
            setListaConsultarRequerimento(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarRequerimento(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarRequerimento() throws Exception {
        RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
        if (getMensagemDetalhada().equals("")) {
            setRequerimentoVO(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarRequerimento());
        this.setValorConsultarRequerimento(null);
        this.setCampoConsultarRequerimento(null);
    }

    public void limparCampoRequerimento() {
        setRequerimentoVO(new RequerimentoVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboRequerimento() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("nomePessoa", "Responsável Protocolo"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio dos parametros informados
     * no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarAluno() {
        try {
        	List<TurnoVO> listaTurnos = getListaTurnoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
        	List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
        	getControleCosultaOtimizadoAluno().getListaConsulta().clear();
        	getControleCosultaOtimizadoAluno().setLimitePorPagina(10);
        	getControleCosultaOtimizadoAluno().setListaConsulta(getFacadeFactory().getMatriculaFacade().consultaRapidaResumidaSemProgramacaoFormatura(getControleConsultaAluno(), getControleCosultaOtimizadoAluno(), listaProgramacao, listaTurnos, false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        if (getMensagemDetalhada().equals("")) {
            setMatriculaVO(obj);
            getMatriculaVO().setCurso(obj.getCurso());
        }
        Uteis.liberarListaMemoria(this.getListaConsultarAluno());
        this.setValorConsultarAluno(null);
        this.setCampoConsultarAluno(null);
    }

    public void limparCampoAluno() {
        setMatriculaVO(new MatriculaVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboAluno() {
        List itens = new ArrayList(0);
       
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        return itens;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Turma</code> por meio dos parametros informados no
     * richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
//    public void consultarTurma() {
//        try {
//            List objs = new ArrayList(0);
//            if (getCampoConsultarTurma().equals("codigo")) {
//                if (getValorConsultarTurma().equals("")) {
//                    setValorConsultarTurma("0");
//                }
//                Integer valorInt = Integer.parseInt(getValorConsultarTurma());
//                objs = getFacadeFactory().getTurmaFacade().consultarPorCodigoNivelEducacionalCurso(valorInt, getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(),getProgramacaoFormaturaVO().getNivelEducacional(),getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//            }
//            if (getCampoConsultarTurma().equals("identificadorTurma")) {
//                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultarTurma(), getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), false,
//                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//            }
//            if (getCampoConsultarTurma().equals("nomeTurno")) {
//                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultarTurma(), getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), false,
//                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//            }
//            setListaConsultarTurma(objs);
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setListaConsultarTurma(new ArrayList(0));
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }
    
    public void consultarTurma() {
        try {
            super.consultar();
            List<TurnoVO> listaTurnos = getListaTurnoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
            List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
            	setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaNivelEducacional(getValorConsultaTurma(), listaProgramacao, getProgramacaoFormaturaVO().getNivelEducacional(), listaTurnos, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
            	setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCursoNivelEducacional(getValorConsultaTurma(), listaProgramacao, getProgramacaoFormaturaVO().getNivelEducacional(), listaTurnos, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setListaConsultaTurma(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }


    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
        if(getCursoVO().getCodigo() != 0L){
            setCursoVO(getTurmaVO().getCurso());
        }
        Uteis.liberarListaMemoria(this.getListaConsultaTurma());
        this.setValorConsultaTurma(null);
        this.setCampoConsultaTurma(null);
    }

    public void limparCampoTurma() {
        setTurmaVO(new TurmaVO());
        setListaConsultaTurma(new ArrayList<>(0));
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    private List<SelectItem> tipoConsultaComboTurma;

    public List<SelectItem> getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList<>(0);
            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
            tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
        }
        return tipoConsultaComboTurma;
    }    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turno</code>.
     */
    public void montarListaSelectItemTurno(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTurnoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            while (i.hasNext()) {
                TurnoVO obj = (TurnoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemTurno(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTurno() {
        try {
            montarListaSelectItemTurno("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarTurnoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Curso</code> por meio dos parametros informados no
     * richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
//    public void consultarCurso() {
//        try {
//        	List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
//            List objs = new ArrayList(0);
//            if (getCampoConsultarCurso().equals("codigo")) {
//                if (getValorConsultarCurso().equals("")) {
//                    setValorConsultarCurso("0");
//                }
//                Integer valorInt = Integer.parseInt(getValorConsultarCurso());
//                objs = getFacadeFactory().getCursoFacade().consultarPorCodigoNivelEducacionalUnidadeEnsino(valorInt,getProgramacaoFormaturaVO().getNivelEducacional(), getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//            }
//            if (getCampoConsultarCurso().equals("nome")) {
//                objs = getFacadeFactory().getCursoFacade().consultarPorNomeUnidadeEnsinoNivelEducacional(getValorConsultarCurso(), getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(),getProgramacaoFormaturaVO().getNivelEducacional(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//            }
//            if (getCampoConsultarCurso().equals("nomeAreaConhecimento")) {
//                objs = getFacadeFactory().getCursoFacade().consultarPorNomeAreaConhecimentoNivelEducacional(getValorConsultarCurso(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,getProgramacaoFormaturaVO().getNivelEducacional(), getUsuarioLogado());
//            }
//            setListaConsultarCurso(objs);
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setListaConsultarCurso(new ArrayList(0));
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }

    public void selecionarCurso() throws Exception {
        CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
        setCursoVO(obj);
        Uteis.liberarListaMemoria(this.getListaConsultarCurso());
        this.setValorConsultarCurso(null);
        this.setCampoConsultarCurso(null);
    }

    public void limparCampoCurso() {
        setCursoVO(new CursoVO());
    }

    public boolean getApresentarAlunos() {
        if (getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs() == null || getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
//        itens.add(new SelectItem("nomeAreaConhecimento", "Área Conhecimento"));
        return itens;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
//    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
//        List resultadoConsulta = null;
//        Iterator i = null;
//        try {
//            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
//            i = resultadoConsulta.iterator();
//            List objs = new ArrayList(0);
//            while (i.hasNext()) {
//                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
//                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//            }
//            Uteis.liberarListaMemoria(resultadoConsulta);
//            setListaSelectItemUnidadeEnsino(objs);
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            Uteis.liberarListaMemoria(resultadoConsulta);
//            i = null;
//        }
//    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
//    public void montarListaSelectItemUnidadeEnsino() {
//        try {
//            montarListaSelectItemUnidadeEnsino("");
//        } catch (Exception e) {
//            //System.out.println("MENSAGEM => " + e.getMessage());;
//        }
//    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
//    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
//        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
//        return lista;
//    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
//        montarListaSelectItemUnidadeEnsino();
//        montarListaSelectItemTurno();
        montarListaSelectItemColacaoGrau();
        montarListaSelectItemNiveEducacional();
    }

    public void verificarPerfilUsuarioLogado() throws Exception {
        if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
            setHabilitaUnidadeEnsino(true);
        } else {            
            setHabilitaUnidadeEnsino(false);
        }
    }

    public boolean getVerificarPerfilUsuarioLogado(boolean habilita) {
        return habilita;
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
//    public String getMascaraConsulta() {
//        return "";
//    }

    public void mostrarSegundoCampo() {
        if (getControleConsulta().getCampoConsulta().equals("periodoRequerimento") || getControleConsulta().getCampoConsulta().equals("periodoColacaoGrau")
                || getControleConsulta().getCampoConsulta().equals("periodoCadastro")) {
            setMostrarSegundoCampo(true);
            setMostrarPrimeiroCampo(false);
            setApresentarUnidadeEnsino(false);
        } else if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
        	setMostrarPrimeiroCampo(false);
        	setMostrarSegundoCampo(false);
        	setApresentarUnidadeEnsino(true);
        }
        else {
            setMostrarPrimeiroCampo(true);
            setApresentarUnidadeEnsino(false);
            setMostrarSegundoCampo(false);
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> tipoConsultaCombo;
    
    public List<SelectItem> getTipoConsultaCombo() {
    	if(tipoConsultaCombo == null) {
	        List<SelectItem> itens = new ArrayList<SelectItem>(0);
	        itens.add(new SelectItem("colacaoGrau", "Colação de Grau"));
	        itens.add(new SelectItem("codigo", "Código"));
	        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
	        itens.add(new SelectItem("nomeCurso", "Curso"));
	        itens.add(new SelectItem("nomeTurno", "Turno"));
	        itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
	        itens.add(new SelectItem("matricula", "Matricula"));
	        itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
	        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
	        itens.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
	        itens.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
	        itens.add(new SelectItem("periodoCadastro", "Período Cadastro"));
	        itens.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
	        tipoConsultaCombo = itens;
    	}
        return tipoConsultaCombo;
    }
    

//    public void reprocessarSituacoesDePendencia() throws Exception {        
//        for (ProgramacaoFormaturaAlunoVO programacaoFormaturaAluno : (List<ProgramacaoFormaturaAlunoVO>) getControleConsultaProgramacaoAluno().getListaConsulta()) {
//            setProgramacaoFormaturaAlunoVO(programacaoFormaturaAluno);
//            verificarContasReceberAluno(getProgramacaoFormaturaAlunoVO().getMatricula().getMatricula());
//            verificarSituacaoFinanceira();
//            verificarDocumentosPendentes(getProgramacaoFormaturaAlunoVO().getMatricula().getMatricula());
//            verificarSituacaoDocumentacao();
//            verificarDisciplinasPendentes(getProgramacaoFormaturaAlunoVO().getMatricula().getMatricula());
//            verificarSituacaoAcademica();
//            getFacadeFactory().getProgramacaoFormaturaAlunoFacade().alterar(getProgramacaoFormaturaAlunoVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
//        }
//
//    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {    
    	removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setControleConsulta(new ControleConsulta());
        setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
        mostrarSegundoCampo();
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        programacaoFormaturaVO = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemTurno);
        Uteis.liberarListaMemoria(listaSelectItemColacaoGrau);
        responsavelCadastro_Erro = null;
        programacaoFormaturaAlunoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemColacaoGrau);
    }

    public String getAluno_Erro() {
        return aluno_Erro;
    }

    public void setAluno_Erro(String aluno_Erro) {
        this.aluno_Erro = aluno_Erro;
    }

    public ProgramacaoFormaturaAlunoVO getProgramacaoFormaturaAlunoVO() {
        return programacaoFormaturaAlunoVO;
    }

    public void setProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
        this.programacaoFormaturaAlunoVO = programacaoFormaturaAlunoVO;
    }

    public String getResponsavelCadastro_Erro() {
        return responsavelCadastro_Erro;
    }

    public void setResponsavelCadastro_Erro(String responsavelCadastro_Erro) {
        this.responsavelCadastro_Erro = responsavelCadastro_Erro;
    }

    public List<SelectItem> getListaSelectItemColacaoGrau() {
        if (listaSelectItemColacaoGrau == null) {
            listaSelectItemColacaoGrau = new ArrayList<>(0);
        }
        return (listaSelectItemColacaoGrau);
    }

    public void setListaSelectItemColacaoGrau(List<SelectItem> listaSelectItemColacaoGrau) {
        this.listaSelectItemColacaoGrau = listaSelectItemColacaoGrau;
    }

    public String getCampoConsultarRequerimento() {
        return campoConsultarRequerimento;
    }

    public void setCampoConsultarRequerimento(String campoConsultarRequerimento) {
        this.campoConsultarRequerimento = campoConsultarRequerimento;
    }

    public String getValorConsultarRequerimento() {
        return valorConsultarRequerimento;
    }

    public void setValorConsultarRequerimento(String valorConsultarRequerimento) {
        this.valorConsultarRequerimento = valorConsultarRequerimento;
    }

    public List getListaConsultarRequerimento() {
        return listaConsultarRequerimento;
    }

    public void setListaConsultarRequerimento(List listaConsultarRequerimento) {
        this.listaConsultarRequerimento = listaConsultarRequerimento;
    }

    public String getCampoConsultarAluno() {
        return campoConsultarAluno;
    }

    public void setCampoConsultarAluno(String campoConsultarAluno) {
        this.campoConsultarAluno = campoConsultarAluno;
    }

    public String getValorConsultarAluno() {
        return valorConsultarAluno;
    }

    public void setValorConsultarAluno(String valorConsultarAluno) {
        this.valorConsultarAluno = valorConsultarAluno;
    }

    public List<MatriculaVO> getListaConsultarAluno() {
        return listaConsultarAluno;
    }

    public void setListaConsultarAluno(List<MatriculaVO> listaConsultarAluno) {
        this.listaConsultarAluno = listaConsultarAluno;
    }

    public String getCampoConsultaTurma() {
    	if (campoConsultaTurma == null) {
    		campoConsultaTurma = "";
    	}
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public String getValorConsultaTurma() {
    	if (valorConsultaTurma == null) {
    		valorConsultaTurma = "";
    	}
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public List<TurmaVO> getListaConsultaTurma() {
    	if (listaConsultaTurma == null) {
    		listaConsultaTurma = new ArrayList<>(0);
    	}
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public List getListaSelectItemTurno() {
        if (listaSelectItemTurno == null) {
            listaSelectItemTurno = new ArrayList(0);
        }
        return (listaSelectItemTurno);
    }

    public void setListaSelectItemTurno(List listaSelectItemTurno) {
        this.listaSelectItemTurno = listaSelectItemTurno;
    }

    public String getCampoConsultarCurso() {
        return campoConsultarCurso;
    }

    public void setCampoConsultarCurso(String campoConsultarCurso) {
        this.campoConsultarCurso = campoConsultarCurso;
    }

    public String getValorConsultarCurso() {
        return valorConsultarCurso;
    }

    public void setValorConsultarCurso(String valorConsultarCurso) {
        this.valorConsultarCurso = valorConsultarCurso;
    }

    public List getListaConsultarCurso() {
        return listaConsultarCurso;
    }

    public void setListaConsultarCurso(List listaConsultarCurso) {
        this.listaConsultarCurso = listaConsultarCurso;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
        return programacaoFormaturaVO;
    }

    public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
        this.programacaoFormaturaVO = programacaoFormaturaVO;
    }

    /**
     * @return the listaSelectItemTipoRequerimentos
     */
    public List getListaSelectItemTipoRequerimentos() {
        if (listaSelectItemTipoRequerimentos == null) {
            listaSelectItemTipoRequerimentos = new ArrayList(0);
        }
        return listaSelectItemTipoRequerimentos;
    }

    /**
     * @param listaSelectItemTipoRequerimentos
     *            the listaSelectItemTipoRequerimentos to set
     */
    public void setListaSelectItemTipoRequerimentos(List listaSelectItemTipoRequerimentos) {
        this.listaSelectItemTipoRequerimentos = listaSelectItemTipoRequerimentos;
    }

    /**
     * @return the turmaVO
     */
    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    /**
     * @param turmaVO
     *            the turmaVO to set
     */
    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    /**
     * @return the cursoVO
     */
    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    /**
     * @param cursoVO
     *            the cursoVO to set
     */
    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
    }

    /**
     * @return the turnoVO
     */
    public TurnoVO getTurnoVO() {
        if (turnoVO == null) {
            turnoVO = new TurnoVO();
        }
        return turnoVO;
    }

    /**
     * @param turnoVO
     *            the turnoVO to set
     */
    public void setTurnoVO(TurnoVO turnoVO) {
        this.turnoVO = turnoVO;
    }

    /**
     * @return the requerimentoVO
     */
    public RequerimentoVO getRequerimentoVO() {
        if (requerimentoVO == null) {
            requerimentoVO = new RequerimentoVO();
        }
        return requerimentoVO;
    }

    /**
     * @param requerimentoVO
     *            the requerimentoVO to set
     */
    public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
        this.requerimentoVO = requerimentoVO;
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO
     *            the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the colacaoGrauVO
     */
//    public ColacaoGrauVO getColacaoGrauVO() {
//        if (colacaoGrauVO == null) {
//            colacaoGrauVO = new ColacaoGrauVO();
//        }
//        return colacaoGrauVO;
//    }

    /**
     * @param colacaoGrauVO
     *            the colacaoGrauVO to set
     */
//    public void setColacaoGrauVO(ColacaoGrauVO colacaoGrauVO) {
//        this.colacaoGrauVO = colacaoGrauVO;
//    }

    /**
     * @return the habilitaUnidadeEnsino
     */
    public Boolean getHabilitaUnidadeEnsino() {
        if (habilitaUnidadeEnsino == null) {
            habilitaUnidadeEnsino = false;
        }
        return habilitaUnidadeEnsino;
    }

    /**
     * @param habilitaUnidadeEnsino
     *            the habilitaUnidadeEnsino to set
     */
    public void setHabilitaUnidadeEnsino(Boolean habilitaUnidadeEnsino) {
        this.habilitaUnidadeEnsino = habilitaUnidadeEnsino;
    }

    /**
     * @return the listaContasReceberAluno
     */
    public List<ContaReceberVO> getListaContasReceberAluno() {
        if (listaContasReceberAluno == null) {
            listaContasReceberAluno = new ArrayList(0);
        }
        return listaContasReceberAluno;
    }

    /**
     * @param listaContasReceberAluno
     *            the listaContasReceberAluno to set
     */
    public void setListaContasReceberAluno(List<ContaReceberVO> listaContasReceberAluno) {
        this.listaContasReceberAluno = listaContasReceberAluno;
    }

    /**
     * @return the listaDocumentosPendentes
     */
    public List<DocumetacaoMatriculaVO> getListaDocumentosPendentes() {
        if (listaDocumentosPendentes == null) {
            listaDocumentosPendentes = new ArrayList(0);
        }
        return listaDocumentosPendentes;
    }

    /**
     * @param listaDocumentosPendentes
     *            the listaDocumentosPendentes to set
     */
    public void setListaDocumentosPendentes(List<DocumetacaoMatriculaVO> listaDocumentosPendentes) {
        this.listaDocumentosPendentes = listaDocumentosPendentes;
    }

    /**
     * @return the listaDisciplinasPendentes
     */
    public List<DisciplinaVO> getListaDisciplinasPendentes() {
        if (listaDisciplinasPendentes == null) {
            listaDisciplinasPendentes = new ArrayList(0);
        }
        return listaDisciplinasPendentes;
    }

    /**
     * @param listaDisciplinasPendentes
     *            the listaDisciplinasPendentes to set
     */
    public void setListaDisciplinasPendentes(List<DisciplinaVO> listaDisciplinasPendentes) {
        this.listaDisciplinasPendentes = listaDisciplinasPendentes;
    }

    /**
     * @return the mostrarSegundoCampo
     */
    public Boolean getMostrarSegundoCampo() {
        if (mostrarSegundoCampo == null) {
            mostrarSegundoCampo = false;
        }
        return mostrarSegundoCampo;
    }

    /**
     * @param mostrarSegundoCampo
     *            the mostrarSegundoCampo to set
     */
    public void setMostrarSegundoCampo(Boolean mostrarSegundoCampo) {
        this.mostrarSegundoCampo = mostrarSegundoCampo;
    }

    /**
     * @return the valorConsultaDataInicio
     */
    public Date getValorConsultaDataInicio() {
        return valorConsultaDataInicio;
    }

    /**
     * @param valorConsultaDataInicio
     *            the valorConsultaDataInicio to set
     */
    public void setValorConsultaDataInicio(Date valorConsultaDataInicio) {
        this.valorConsultaDataInicio = valorConsultaDataInicio;
    }

    /**
     * @return the valorConsultaDataFinal
     */
    public Date getValorConsultaDataFinal() {
        return valorConsultaDataFinal;
    }

    /**
     * @param valorConsultaDataFinal
     *            the valorConsultaDataFinal to set
     */
    public void setValorConsultaDataFinal(Date valorConsultaDataFinal) {
        this.valorConsultaDataFinal = valorConsultaDataFinal;
    }

    /**
     * @return the listaColouGrau
     */
    public List getListaColouGrau() {
        if (listaColouGrau == null) {
            listaColouGrau = new ArrayList(0);
        }
        return listaColouGrau;
    }

    /**
     * @param listaColouGrau
     *            the listaColouGrau to set
     */
    public void setListaColouGrau(List listaColouGrau) {
        this.listaColouGrau = listaColouGrau;
    }

    public void montarListaSelectItemNiveEducacional() {
    	setListaSelectItemNivelEducacional(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, false));
    }
    
    public Date getDataConclusaoCurso() {
        return dataConclusaoCurso;
    }

    
    public void setDataConclusaoCurso(Date dataConclusaoCurso) {
        this.dataConclusaoCurso = dataConclusaoCurso;
    }
    
    public String getApresentarDataConclusaoCurso(){
        return Uteis.getData(getDataConclusaoCurso());
    }

	public List getListaSelectItemNivelEducacional() {
		if (listaSelectItemNivelEducacional == null) {
			listaSelectItemNivelEducacional = new ArrayList(0);
		}
		return listaSelectItemNivelEducacional;
	}

	public void setListaSelectItemNivelEducacional(List listaSelectItemNivelEducacional) {
		this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
	}
	
	public boolean isFiltrarCurriculoIntegralizado() {
		return filtrarCurriculoIntegralizado;
	}

	public void setFiltrarCurriculoIntegralizado(boolean filtrarCurriculoIntegralizado) {
		this.filtrarCurriculoIntegralizado = filtrarCurriculoIntegralizado;
	}

    public void limparConsultaTurma() {
    	setListaConsultaTurma(new ArrayList<>(0));
    }
    
    public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().isEmpty()) {
			return "aceitarSomenteValorNumerico(this)";
		}
		return "";
	}
    
    public String adicionarProgramacaoFormatura() throws Exception {
    	try {
    		getFacadeFactory().getProgramacaoFormaturaFacade().adicionarProgramacaoFormaturaAlunoVO(getProgramacaoFormaturaVO(), getListaTurnoVOs(), getListaCursoVOs(), getTurmaVO().getCodigo(), getMatriculaVO().getMatricula(), getRequerimentoVO().getCodigo(), getFiltrarRegistroEnade(), getFiltrarRegistroTCC(), isFiltrarCurriculoIntegralizado(), getUsuarioLogado());
    		getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().sort(Comparator.comparing(m -> m.getMatricula().getAluno().getNome()));
    		if(Uteis.isAtributoPreenchido(getRequerimentoVO())) {
    			setRequerimentoVO(new RequerimentoVO());
    		}
    		if(Uteis.isAtributoPreenchido(getTurmaVO())) {
    			setTurmaVO(new TurmaVO());
    		}
    		if(Uteis.isAtributoPreenchido(getMatriculaAluno())) {
    			setMatriculaAluno("");
    			setNomeAluno("");
    		}
            setMensagemID("msg_dados_adicionados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("programacaoFormaturaForm.xhtml");
		}
    }
    
    public void marcarTodasUnidadesEnsinoAction() {
    	for (ProgramacaoFormaturaUnidadeEnsinoVO unidade : getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs()) {
			unidade.setSelecionado(getMarcarTodasUnidadeEnsino());
			unidade.setUnidadeSelecionado(getMarcarTodasUnidadeEnsino());
		}
    	verificarTodasUnidadesSelecionadas();
    }
    
    public void verificarTodasUnidadesSelecionadas() {
    	StringBuilder unidade = new StringBuilder();
    	if (getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().size() > 1) {
    		for (ProgramacaoFormaturaUnidadeEnsinoVO obj : getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs()) {
				if (obj.getSelecionado()) {
					unidade.append(obj.getUnidadeEnsinoVO().getNome().trim()).append("; ");
				}
				getProgramacaoFormaturaVO().setUnidadeEnsinoDescricao(unidade.toString());
			}
    	} else {
    		if (!getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().isEmpty()) {
    			if (getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().get(0).getSelecionado()) {
    				getProgramacaoFormaturaVO().setUnidadeEnsinoDescricao(getUnidadeEnsinoVOs().get(0).getNome());
    			}
    		}
    	}
    }
    
    public void marcarTodosOsTurnosAction() {
		for (TurnoVO turno : getListaTurnoVOs()) {
			turno.setSelecionado(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionadas();
	}
    
    public void verificarTodosTurnosSelecionadas() {
    	StringBuilder turno = new StringBuilder();
    	if (getListaTurnoVOs().size() > 1) {
    		for (TurnoVO obj : getListaTurnoVOs()) {
    			if (obj.getSelecionado()) {
    				turno.append(obj.getNome().trim()).append("; ");
    			}
    			setTurnoDescricao(turno.toString());
    		}
    	} else {
    		setTurnoDescricao("");
    	}
    }
    
    public void marcarTodosOsCursoAction() {
    	for (CursoVO curso : getListaCursoVOs()) {
			curso.setSelecionado(getMarcarTodosCursos());
		}
    	verificarTodosCursosSelecionados();
    }
    
    public void verificarTodosCursosSelecionados() {
    	StringBuilder curso = new StringBuilder();
    	if (getListaCursoVOs().size() > 1) {
    		for (CursoVO obj : getListaCursoVOs()) {
				if (obj.getSelecionado()) {
					curso.append(obj.getNome().trim()).append("; ");
				}
				setCursoDescricao(curso.toString());
			}
    	} else {
    		setCursoDescricao("");
    	}
    }
    
    public List<TurnoVO> getListaTurnoVOs() {
		if (listaTurnoVOs == null) {
			listaTurnoVOs = new ArrayList<TurnoVO>(0);
		}
    	return listaTurnoVOs;
	}
    
    public void setListaTurnoVOs(List<TurnoVO> listaTurnoVOs) {
		this.listaTurnoVOs = listaTurnoVOs;
	}
    
    public String getTurnoDescricao() {
		if (turnoDescricao == null) {
			turnoDescricao = "";
		}
    	return turnoDescricao;
	}
    
    public void setTurnoDescricao(String turnoDescricao) {
		this.turnoDescricao = turnoDescricao;
	}
    
    public void montarListaTurno() {
    	try {
    		setListaTurnoVOs(getFacadeFactory().getTurnoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void montarListaCurso() {
    	try {
    		setListaCursoVOs(getFacadeFactory().getCursoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    private Boolean filtrarRegistroTCC;
    
    public Boolean getFiltrarRegistroTCC() {
		if (filtrarRegistroTCC == null) {
			filtrarRegistroTCC = false;
		}
    	return filtrarRegistroTCC;
	}
    
    public void setFiltrarRegistroTCC(Boolean filtrarRegistroTCC) {
		this.filtrarRegistroTCC = filtrarRegistroTCC;
	}
    
    public Boolean getFiltrarRegistroEnade() {
		if (filtrarRegistroEnade == null) {
			filtrarRegistroEnade = false;
		}
    	return filtrarRegistroEnade;
	}
    
    public void setFiltrarRegistroEnade(Boolean filtrarRegistroEnade) {
		this.filtrarRegistroEnade = filtrarRegistroEnade;
	}
    
    public String getCursoDescricao() {
		if (cursoDescricao == null) {
			cursoDescricao = "";
		}
    	return cursoDescricao;
	}
    
    public void setCursoDescricao(String cursoDescricao) {
		this.cursoDescricao = cursoDescricao;
	}
    
    public List<CursoVO> getListaCursoVOs() {
		if (listaCursoVOs == null) {
			listaCursoVOs = new ArrayList<CursoVO>(0);
		}
    	return listaCursoVOs;
	}
    
    public void setListaCursoVOs(List<CursoVO> listaCursoVOs) {
		this.listaCursoVOs = listaCursoVOs;
	}
    
    private String nomeCurso;
    
    public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
    	return nomeCurso;
	}
    
    public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
    
    private String nomeAluno;
    
    public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
    	return nomeAluno;
	}
    
    public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
    
    private String matriculaAluno;
    
    public String getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = "";
		}
    	return matriculaAluno;
	}
    
    public void setMatriculaAluno(String matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}
    
    public void limparFiltroNomeAluno() {
    	setNomeAluno(null);
    	consultaOtimizada();
    }
    
    public void limparFiltroNomeCurso() {
    	setNomeCurso(null);
    	consultaOtimizada();
    }
    
    public void limparFiltroMatriculaAluno() {
    	setMatriculaAluno(null);;
    	consultaOtimizada();
    }
    
    private Boolean apresentarUnidadeEnsino;
    
    public Boolean getApresentarUnidadeEnsino() {
		if (apresentarUnidadeEnsino == null) {
			apresentarUnidadeEnsino = false;
		}
    	return apresentarUnidadeEnsino;
	}
    
    public void setApresentarUnidadeEnsino(Boolean apresentarUnidadeEnsino) {
		this.apresentarUnidadeEnsino = apresentarUnidadeEnsino;
	}
    
    public Boolean getMostrarPrimeiroCampo() {
		if (mostrarPrimeiroCampo == null) {
			mostrarPrimeiroCampo = false;
		}
    	return mostrarPrimeiroCampo;
	}
    
    public void setMostrarPrimeiroCampo(Boolean mostrarPrimeiroCampo) {
		this.mostrarPrimeiroCampo = mostrarPrimeiroCampo;
	}
    
    public Integer getTextoPadrao() {
		if (textoPadrao == null) {
			textoPadrao = 0;
		}
    	return textoPadrao;
	}
    
    public void setTextoPadrao(Integer textoPadrao) {
		this.textoPadrao = textoPadrao;
	}
    
    public List getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<>(0);
		}
    	return listaSelectItemTipoTextoPadrao;
	}
    
    public void setListaSelectItemTipoTextoPadrao(List listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}
    
    @SuppressWarnings("unchecked")
	public void consultarListaSelectItemTipoTextoPadrao() {
        try {
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarTipoAtaColacao(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getListaSelectItemTipoTextoPadrao().add(new SelectItem("", ""));
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<ProgramacaoFormaturaCursoVO> getProgramacaoFormaturaCursoVOs() {
		if (programacaoFormaturaCursoVOs == null) {
			programacaoFormaturaCursoVOs = new ArrayList<>(0);
		}
    	return programacaoFormaturaCursoVOs;
	}
    
    public void setProgramacaoFormaturaCursoVOs(List<ProgramacaoFormaturaCursoVO> programacaoFormaturaCursoVOs) {
		this.programacaoFormaturaCursoVOs = programacaoFormaturaCursoVOs;
	}
    
    public SuperControleRelatorio getControleRel() {
		if (controleRel == null) {
			controleRel = new SuperControleRelatorio() {
			};
		}
    	return controleRel;
	}
    
    public void setControleRel(SuperControleRelatorio controleRel) {
		this.controleRel = controleRel;
	}
    
    public void realizarInicioProgressBarImpressaoAta() {
		try {
			setOncompleteModal("");
			ProgramacaoFormaturaCursoVO obj = (ProgramacaoFormaturaCursoVO) context().getExternalContext().getRequestMap().get("itens");
			if (!Uteis.isAtributoPreenchido(obj.getTextoPadraoDeclaracaoVO().getCodigo())) {
    			throw new Exception("O TEXTO PADRÃO dever ser informado.");
    		}
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado()));
			setProgramacaoFormaturaCursoVO(obj);
			executarValidacaoProgressBarImpressaoAta(false);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}

	private void executarValidacaoProgressBarImpressaoAta(boolean novaAta) throws Exception {
		Map<Boolean, Boolean> existeConfiguracaoeAlunoAssinar = getFacadeFactory().getConfiguracaoGEDFacade().consultarPermitirAlunoAssinarColacaoGrau(getUsuarioLogado());
		setExisteConfiguracaoGed(existeConfiguracaoeAlunoAssinar.containsKey(true));
		setPermiteAlunoAssinarAta(existeConfiguracaoeAlunoAssinar.containsValue(true));
		setProgressBarImpressaoAta(new ProgressBarVO());
		getProgressBarImpressaoAta().setUsuarioVO(getUsuarioLogadoClone());
		if(getProgramacaoFormaturaCursoVO().getAssinarDigitalmente() && isPermiteAlunoAssinarAta()) {
			List<ProgramacaoFormaturaAlunoVO> lista = getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarProgramacaoFormaturaAlunoPorSemDocumentoAssinado(getProgramacaoFormaturaVO().getCodigo(), getProgramacaoFormaturaCursoVO().getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogadoClone());
			final AtomicInteger counter = new AtomicInteger();
			setResultadoProgramacaoFormaturaAlunoVOs(lista.stream().filter(p -> !p.getExisteProgramacaoFormaturaDuplicada()).collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 8)).values());
			Uteis.checkState(getResultadoProgramacaoFormaturaAlunoVOs().size() == 0L, "Não foi encontrado nenhum Aluno para a geração da Ata.");
			getProgressBarImpressaoAta().resetar();
			getProgressBarImpressaoAta().setAplicacaoControle(getAplicacaoControle());
			getProgressBarImpressaoAta().iniciar(0l, (getResultadoProgramacaoFormaturaAlunoVOs().size()), "Iniciando Processamento da(s) operações.", true, this, "imprimirAtaColacaoGrau");
			if(novaAta) {
				setOncompleteModal("RichFaces.$('panelCursoDocumentoAssinado').hide();RichFaces.$('panelImprimirAta').hide();");
			}else {
				setOncompleteModal("RichFaces.$('panelImprimirAta').hide();");	
			}
			
		}else {
			imprimirAtaColacaoGrau();
		}
	}
    
    public void imprimirAtaColacaoGrau() {
    	try {
    		getControleRel().limparMensagem();
    		getControleRel().setFazerDownload(false);
    		getControleRel().setCaminhoRelatorio("");
    		List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
    		if(!getResultadoProgramacaoFormaturaAlunoVOs().isEmpty()) {    			
    			List<File> files = new ArrayList<>();
        		int cont = 1;
        		for (List<ProgramacaoFormaturaAlunoVO> list : getResultadoProgramacaoFormaturaAlunoVOs()) {
        			if(getResultadoProgramacaoFormaturaAlunoVOs().size() == 1) {
        				getControleRel().setCaminhoRelatorio(executarImpressaoAtaColacaoGrau(getProgramacaoFormaturaCursoVO(), list, listaProgramacao));
        				getProgressBarImpressaoAta().incrementar();
        				break;
        			}
        			StringBuilder sb = new StringBuilder();
        			sb.append(" Processando ").append(getProgressBarImpressaoAta().getProgresso()).append(" de ").append(getProgressBarImpressaoAta().getMaxValue());
        			sb.append(" - (Ata Atual Nº = ").append(cont).append(") ");
        			getProgressBarImpressaoAta().setStatus(sb.toString());
        			files.add(new File(executarImpressaoAtaColacaoGrau(getProgramacaoFormaturaCursoVO(), list, listaProgramacao)));
        			getProgressBarImpressaoAta().incrementar();
        			cont++;
    			}
        		getControleRel().setFazerDownload(false);
    		}else {
    			getControleRel().setCaminhoRelatorio(executarImpressaoAtaColacaoGrau(getProgramacaoFormaturaCursoVO(), new ArrayList<>(), listaProgramacao));
    			getControleRel().setFazerDownload(true);
    		}
    		inicializarConsultaImpressaoAta();
		} catch (Exception e) {
			getControleRel().setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			if(isPermiteAlunoAssinarAta()) {
				getProgressBarImpressaoAta().incrementar();
				getProgressBarImpressaoAta().setForcarEncerramento(true);
			}
		}
    }

	private String executarImpressaoAtaColacaoGrau(ProgramacaoFormaturaCursoVO obj, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunoVO, List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao) throws Exception {
		setImpressaoContratoVO(new ImpressaoContratoVO());
		getImpressaoContratoVO().setImpressaoPdf(true);
		getImpressaoContratoGravarVO().setImpressaoPdf(true);
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralPadraoSistema();
		TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(obj.getTextoPadraoDeclaracaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarImpressaoAta().getUsuarioVO());	
		getImpressaoContratoVO().setProgramacaoFormaturaVO(getProgramacaoFormaturaVO());
		getImpressaoContratoVO().getProgramacaoFormaturaVO().getCurso().setCodigo(obj.getCursoVO().getCodigo());
		getImpressaoContratoVO().setProgramacaoFormaturaCursoVO(obj);
		getImpressaoContratoVO().getPrimeiroReconhecimentoCurso().setNome(obj.getPrimeiroConhecimentoCurso());
		getImpressaoContratoVO().getRenovacaoReconhecimentoCurso().setNome(obj.getRenovacaoConhecimentoCurso());
		getImpressaoContratoVO().setProgramacaoFormaturaAlunoVOs(listaProgramacaoFormaturaAlunoVO);
		getImpressaoContratoVO().setGravarImpressaoContrato(false);
		String caminhoRelatorioTemp = getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(texto.getCodigo(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), texto.getTipo(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getProgressBarImpressaoAta().getUsuarioVO(), obj.getAssinarDigitalmente());		
		if(obj.getAssinarDigitalmente() && isExisteConfiguracaoGed()){
			if(!isPermiteAlunoAssinarAta()) {
				getImpressaoContratoVO().getProgramacaoFormaturaAlunoVOs().clear();
			}
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadesVinculadaConfiguracaoGed(listaProgramacao, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));			
			return getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorAtaColacaoGrau(caminhoRelatorioTemp, getProgramacaoFormaturaVO(), obj.getCursoVO(), getImpressaoContratoVO().getProgramacaoFormaturaAlunoVOs(), TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU, getControleRel().getProvedorDeAssinaturaEnum(), "#000000", 80f, 200f, getConfiguracaoGeralPadraoSistema(), getProgressBarImpressaoAta().getUsuarioVO(), getUnidadeEnsinoVO(), getProgramacaoFormaturaVO().getColacaoGrauVO().getPresidenteMesa(), "", "", getProgramacaoFormaturaVO().getColacaoGrauVO().getSecretariaAcademica(), "", "");
		} else if (obj.getAssinarDigitalmente() && !isExisteConfiguracaoGed()) {
			throw new Exception("Não Existe (Configuração Ged) cadastrada, para Assinar Digitalmente.");
		}
		return caminhoRelatorioTemp;
	}
    
    public void inicializarConsultaImpressaoAta() {
    	try {
    		getListaSelectItemTipoTextoPadrao().clear();
    		getProgramacaoFormaturaCursoVOs().clear();
    		consultarListaSelectItemTipoTextoPadrao();
    		setProgramacaoFormaturaCursoVOs(getFacadeFactory().getProgramacaoFormaturaCursoInterfaceFacade().consultarCursosPorCodigoProgramacaoFormatura(getProgramacaoFormaturaVO().getCodigo(), getUsuarioLogado()));
    		setProgramacaoFormaturaCursoVO(new ProgramacaoFormaturaCursoVO());
    		getResultadoProgramacaoFormaturaAlunoVOs().clear();
    		validarExisteProgramacaoFormaturaDuplicada();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public ImpressaoContratoVO getImpressaoContratoVO() {
		if (impressaoContratoVO == null) {
			impressaoContratoVO = new ImpressaoContratoVO();
		}
    	return impressaoContratoVO;
	}
    
    public void setImpressaoContratoVO(ImpressaoContratoVO impressaoContratoVO) {
		this.impressaoContratoVO = impressaoContratoVO;
	}
    
    public ImpressaoContratoVO getImpressaoContratoGravarVO() {
		if (impressaoContratoGravarVO == null) {
			impressaoContratoGravarVO = new ImpressaoContratoVO();
		}
    	return impressaoContratoGravarVO;
	}
    
    public void setImpressaoContratoGravarVO(ImpressaoContratoVO impressaoContratoGravarVO) {
		this.impressaoContratoGravarVO = impressaoContratoGravarVO;
	}
    
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
    	return unidadeEnsinoVO;
	}
    
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
    
    public void realizarDownloadArquivo() {
    	try {
    		ProgramacaoFormaturaCursoVO obj = (ProgramacaoFormaturaCursoVO) context().getExternalContext().getRequestMap().get("itens");
    		List<DocumentoAssinadoVO> lista  = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorCodigoProgramacaoCodigoCurso(obj.getProgramacaoFormaturaVO().getCodigo(), obj.getCursoVO().getCodigo(), Boolean.FALSE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
    		if(!lista.isEmpty() &&  lista.size() == 1 ) {
    			if(lista.get(0).getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
    				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(lista.get(0), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(lista.get(0).getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());	
    			}
                if(lista.get(0).getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
                    getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(lista.get(0), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(lista.get(0).getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
                }
    			realizarDownloadArquivo(lista.get(0).getArquivo());
    		}else if(!lista.isEmpty() &&  lista.size() > 1) {
    			List<File> files = new ArrayList<>();
    			for (DocumentoAssinadoVO da : lista) {
    				ConfiguracaoGeralSistemaVO config = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(da.getUnidadeEnsinoVO().getCodigo());
    				if(da.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
        				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(da, config, getUsuarioLogadoClone());	
    				}
                    if(da.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
                        getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(da, config, getUsuarioLogadoClone());
                    }
    				files.add(new File(config.getLocalUploadArquivoFixo() + File.separator + da.getArquivo().getPastaBaseArquivo() + File.separator + da.getArquivo().getNome()));
				}
    			ArquivoVO zipado = new ArquivoVO();
    			zipado.setNome("AtaColacaoGrau"+new Date().getTime()+".zip");
    			zipado.setExtensao(".zip");
    			zipado.setPastaBaseArquivo(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator);
    			File[] filesArray = new File[files.size()];
    			getFacadeFactory().getArquivoHelper().zip(files.toArray(filesArray), new File(zipado.getPastaBaseArquivo() + zipado.getNome()));
    			context().getExternalContext().getSessionMap().put("arquivoVO", zipado);
    		}    		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
    }

	public Collection<List<ProgramacaoFormaturaAlunoVO>> getResultadoProgramacaoFormaturaAlunoVOs() {
		if(resultadoProgramacaoFormaturaAlunoVOs == null) {
			resultadoProgramacaoFormaturaAlunoVOs = new ArrayList<List<ProgramacaoFormaturaAlunoVO>>();
		}
		return resultadoProgramacaoFormaturaAlunoVOs;
	}

	public void setResultadoProgramacaoFormaturaAlunoVOs(Collection<List<ProgramacaoFormaturaAlunoVO>> resultadoProgramacaoFormaturaAlunoVOs) {		
		this.resultadoProgramacaoFormaturaAlunoVOs = resultadoProgramacaoFormaturaAlunoVOs;
	}

	public ProgressBarVO getProgressBarImpressaoAta() {
		return progressBarImpressaoAta;
	}

	public void setProgressBarImpressaoAta(ProgressBarVO progressBarImpressaoAta) {
		this.progressBarImpressaoAta = progressBarImpressaoAta;
	}

	public boolean isExisteConfiguracaoGed() {
		return existeConfiguracaoGed;
	}

	public void setExisteConfiguracaoGed(boolean existeConfiguracaoGed) {
		this.existeConfiguracaoGed = existeConfiguracaoGed;
	}

	public boolean isPermiteAlunoAssinarAta() {
		return permiteAlunoAssinarAta;
	}

	public void setPermiteAlunoAssinarAta(boolean permiteAlunoAssinarAta) {
		this.permiteAlunoAssinarAta = permiteAlunoAssinarAta;
	}

	public String getOperacaoRealizarProgressBarImpressaoAta() {
		if(getControleRel().getFazerDownload()) {
			return getControleRel().getDownload();
		}else {
			return getOncompleteModal();
		}
	}

	public void setOperacaoRealizarProgressBarImpressaoAta(String operacaoRealizarProgressBarImpressaoAta) {
		this.operacaoRealizarProgressBarImpressaoAta = operacaoRealizarProgressBarImpressaoAta;
	}

	public void inicializarConsultaParaNovoDocumentoAssinado() {
    	ProgramacaoFormaturaCursoVO obj = (ProgramacaoFormaturaCursoVO) context().getExternalContext().getRequestMap().get("itens");
    	setOcompleteModal("");
    	try {
    		setProgramacaoFormaturaCursoVO(obj);
    		if (!Uteis.isAtributoPreenchido(getProgramacaoFormaturaCursoVO().getTextoPadraoDeclaracaoVO().getCodigo())) {
    			throw new Exception("O TEXTO PADRÃO dever ser informado.");
    		}
    		setOcompleteModal("RichFaces.$('panelCursoDocumentoAssinado').show()");
    		setCursoProgramacao(obj.getCursoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setOcompleteModal("");
		}
    }
    
    public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
    	return documentoAssinadoVO;
	}
    
    public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}
    
    public List<DocumentoAssinadoPessoaVO> getDocumentoAssinadoPessoaVOs() {
		if (documentoAssinadoPessoaVOs == null) {
			documentoAssinadoPessoaVOs = new ArrayList<DocumentoAssinadoPessoaVO>(0);
		}
    	return documentoAssinadoPessoaVOs;
	}
    
    public void setDocumentoAssinadoPessoaVOs(List<DocumentoAssinadoPessoaVO> documentoAssinadoPessoaVOs) {
		this.documentoAssinadoPessoaVOs = documentoAssinadoPessoaVOs;
	}
    
    public ProgramacaoFormaturaCursoVO getProgramacaoFormaturaCursoVO() {
		if (programacaoFormaturaCursoVO == null) {
			programacaoFormaturaCursoVO = new ProgramacaoFormaturaCursoVO();
		}
    	return programacaoFormaturaCursoVO;
	}
    
    public void setProgramacaoFormaturaCursoVO(ProgramacaoFormaturaCursoVO programacaoFormaturaCursoVO) {
		this.programacaoFormaturaCursoVO = programacaoFormaturaCursoVO;
	}
    
    public CursoVO getCursoProgramacao() {
		if (cursoProgramacao == null) {
			cursoProgramacao = new CursoVO();
		}
    	return cursoProgramacao;
	}
    
    public void setCursoProgramacao(CursoVO cursoProgramacao) {
		this.cursoProgramacao = cursoProgramacao;
	}
    
    public void limparDadosProgramacaoFormaturaCurso() {
    	setProgramacaoFormaturaCursoVO(new ProgramacaoFormaturaCursoVO());
    	setMotivoRejeicao("");
    	setCursoProgramacao(new CursoVO());
    }
    
    public void imprimirNovaAta() {
    	try {
    		if (!Uteis.isAtributoPreenchido(getMotivoRejeicao())) {
    			throw new Exception("O Motivo Rejeição dever ser informado.");
    		}
    		if (!Uteis.isAtributoPreenchido(getProgramacaoFormaturaCursoVO().getTextoPadraoDeclaracaoVO().getCodigo())) {
    			throw new Exception("O TEXTO PADRÃO dever ser informado.");
    		}
    		cancelarDocumentoAssinadoPendente(getMotivoRejeicao());
    		executarValidacaoProgressBarImpressaoAta(true);
    		setMotivoRejeicao("");
//    		Map<Boolean, Boolean> existeConfiguracaoeAlunoAssinar = getFacadeFactory().getConfiguracaoGEDFacade().consultarPermitirAlunoAssinarColacaoGrau(getUsuarioLogado());
//    		Boolean existeConfiguracaoGed = existeConfiguracaoeAlunoAssinar.containsKey(true);
//    		Boolean permitirAlunoAssinar = existeConfiguracaoeAlunoAssinar.containsValue(true);
//    		getControleRel().limparMensagem();
//    		getControleRel().setFazerDownload(false);
//    		getControleRel().setCaminhoRelatorio("");
//    		getImpressaoContratoVO().setImpressaoPdf(true);
//			getImpressaoContratoGravarVO().setImpressaoPdf(true);
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
//			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralPadraoSistema();
//    		TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getProgramacaoFormaturaCursoVO().getTextoPadraoDeclaracaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//    		getImpressaoContratoVO().setProgramacaoFormaturaVO(getProgramacaoFormaturaVO());
//    		getImpressaoContratoVO().getProgramacaoFormaturaVO().getCurso().setCodigo(getProgramacaoFormaturaCursoVO().getCursoVO().getCodigo());
//    		getImpressaoContratoVO().setProgramacaoFormaturaCursoVO(getProgramacaoFormaturaCursoVO());
//    		getImpressaoContratoVO().getPrimeiroReconhecimentoCurso().setNome(getProgramacaoFormaturaCursoVO().getPrimeiroConhecimentoCurso());
//    		getImpressaoContratoVO().getRenovacaoReconhecimentoCurso().setNome(getProgramacaoFormaturaCursoVO().getRenovacaoConhecimentoCurso());
//    		getControleRel().setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(texto.getCodigo(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), texto.getTipo(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado()));
//    		if(getProgramacaoFormaturaCursoVO().getAssinarDigitalmente() && existeConfiguracaoGed){
//    			List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao = getProgramacaoFormaturaVO().getProgramacaoFormaturaUnidadeEnsinoVOs().stream().filter(pf -> pf.getSelecionado()).collect(Collectors.toList());
//        		setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadesVinculadaConfiguracaoGed(listaProgramacao, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//        		getProgramacaoFormaturaCursoVO().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getProgramacaoFormaturaCursoVO().getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado()));
//    			getControleRel().setCaminhoRelatorio(getFacadeFactory().getDocumentoAssinadoFacade().realizarInclusaoDocumentoAssinadoPorAtaColacaoGrau(getControleRel().getCaminhoRelatorio(), getProgramacaoFormaturaVO(), getProgramacaoFormaturaCursoVO().getCursoVO(), getImpressaoContratoVO().getProgramacaoFormaturaAlunoVOs(), TipoOrigemDocumentoAssinadoEnum.ATA_COLACAO_GRAU, getControleRel().getProvedorDeAssinaturaEnum(), "#000000", 80f, 200f, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado(), getUnidadeEnsinoVO(), getProgramacaoFormaturaVO().getColacaoGrauVO().getPresidenteMesa(), "", "", getProgramacaoFormaturaVO().getColacaoGrauVO().getSecretariaAcademica(), "", ""));
//			} else if (getProgramacaoFormaturaCursoVO().getAssinarDigitalmente() && !existeConfiguracaoGed) {
//				throw new Exception("Não Existe (Configuração Ged) cadastrada, para Assinar Digitalmente.");
//			}
//    		setProgramacaoFormaturaCursoVO(new ProgramacaoFormaturaCursoVO());
//    		setMotivoRejeicao("");
//    		getControleRel().setFazerDownload(true);
		} catch (Exception e) {
			getControleRel().setFazerDownload(false);	
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void cancelarDocumentoAssinadoPendente(String motivoRejeicao) {
    	try {
    		if(getProgramacaoFormaturaCursoVO().getCursoProgramacaoDocumentoAssinado()) {
    			List<DocumentoAssinadoVO> lista  = getFacadeFactory().getDocumentoAssinadoFacade().consultarPorCodigoProgramacaoCodigoCurso(getProgramacaoFormaturaCursoVO().getProgramacaoFormaturaVO().getCodigo(), getProgramacaoFormaturaCursoVO().getCursoVO().getCodigo(), Boolean.TRUE, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone());
    			for (DocumentoAssinadoVO doc : lista) {
    				if (doc.getListaDocumentoAssinadoPessoa().isEmpty()) {
    	    			doc.setListaDocumentoAssinadoPessoa(getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorDocumentoAssinado(doc, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    				}
    	    		if (doc.getProvedorDeAssinaturaEnum().isProvedorSei()) {
    	    			if (doc.getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())) {
    						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(doc, getMotivoRejeicao(), getUsuarioLogado());
    					}
    	    			doc.setDocumentoAssinadoInvalido(true);
    	    			doc.setMotivoDocumentoAssinadoInvalido(getMotivoRejeicao());
    	    			getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(doc.getCodigo(), doc.isDocumentoAssinadoInvalido(), doc.getMotivoDocumentoAssinadoInvalido(), getUsuarioLogado());
    	    		} else if (doc.getProvedorDeAssinaturaEnum().isProvedorCertisign() || doc.getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()) {
    	    			ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
    					if (doc.getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())) {
    						getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(doc, getMotivoRejeicao(), getUsuarioLogado());
    					}
    					doc.setDocumentoAssinadoInvalido(true);
    					doc.setMotivoDocumentoAssinadoInvalido(getMotivoRejeicao());
    					getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocument(doc, configGedVO, getUsuarioLogadoClone());
    					getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(doc.getCodigo(), doc.isDocumentoAssinadoInvalido(), doc.getMotivoDocumentoAssinadoInvalido(), getUsuarioLogado());
    	    		}
                    else if (doc.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
                        ConfiguracaoGEDVO configGedVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(doc.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
                        if (doc.getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().isPendente())) {
                            getFacadeFactory().getDocumentoAssinadoPessoaFacade().atualizarSituacaoPendenteDocumentoAssinadoPessoaParaRejeitado(doc, getMotivoRejeicao(), getUsuarioLogado());
                        }
                        doc.setDocumentoAssinadoInvalido(true);
                        doc.setMotivoDocumentoAssinadoInvalido(getMotivoRejeicao());
                        getFacadeFactory().getDocumentoAssinadoFacade().realizarBloqueioDocumentTechCert(doc, configGedVO, getUsuarioLogadoClone());
                        getFacadeFactory().getDocumentoAssinadoFacade().atualizarDocumentoAssinadoInvalido(doc.getCodigo(), doc.isDocumentoAssinadoInvalido(), doc.getMotivoDocumentoAssinadoInvalido(), getUsuarioLogado());
                    }
				}
    		}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public String getMotivoRejeicao() {
		if (motivoRejeicao == null) {
			motivoRejeicao = "";
		}
    	return motivoRejeicao;
	}
    
    public void setMotivoRejeicao(String motivoRejeicao) {
		this.motivoRejeicao = motivoRejeicao;
	}
    
    public String getOcompleteModal() {
		if (ocompleteModal == null) {
			ocompleteModal = "";
		}
    	return ocompleteModal;
	}
    
    public void setOcompleteModal(String ocompleteModal) {
		this.ocompleteModal = ocompleteModal;
	}
    
    public DataModelo getControleCosultaOtimizadoAluno() {
		if (controleCosultaOtimizadoAluno == null) {
			controleCosultaOtimizadoAluno = new DataModelo();
		}
    	return controleCosultaOtimizadoAluno;
	}
    
    public void setControleCosultaOtimizadoAluno(DataModelo controleCosultaOtimizadoAluno) {
		this.controleCosultaOtimizadoAluno = controleCosultaOtimizadoAluno;
	}
    
    public ControleConsulta getControleConsultaAluno() {
		if (controleConsultaAluno == null) {
			controleConsultaAluno = new ControleConsulta();
		}
    	return controleConsultaAluno;
	}
    
    public void setControleConsultaAluno(ControleConsulta controleConsultaAluno) {
		this.controleConsultaAluno = controleConsultaAluno;
	}
    
    public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) throws Exception {
    	getControleCosultaOtimizadoAluno().setPaginaAtual(dataScrollEvent.getPage());
    	getControleCosultaOtimizadoAluno().setPage(dataScrollEvent.getPage());
    	consultarAluno();
    }

    public String getAvisoProgramacaoFormaturaDuplicada() {
		if (avisoProgramacaoFormaturaDuplicada == null) {
			avisoProgramacaoFormaturaDuplicada = Constantes.EMPTY;
		}
    	return avisoProgramacaoFormaturaDuplicada;
	}
    
    public void setAvisoProgramacaoFormaturaDuplicada(String avisoProgramacaoFormaturaDuplicada) {
		this.avisoProgramacaoFormaturaDuplicada = avisoProgramacaoFormaturaDuplicada;
	}
    
    public void validarExisteProgramacaoFormaturaDuplicada() {
    	setAvisoProgramacaoFormaturaDuplicada(Constantes.EMPTY);
    	if (Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs()) && getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream().anyMatch(ProgramacaoFormaturaAlunoVO::getExisteProgramacaoFormaturaDuplicada)) {
    		setAvisoProgramacaoFormaturaDuplicada("Existe " + getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream().filter(ProgramacaoFormaturaAlunoVO::getExisteProgramacaoFormaturaDuplicada).count() + " matrícula(s) com duplicidade nas programações de formatura. Não será gerada a ATA de COLAÇÃO DE GRAU para os alunos que possuem duplicidade. Para gerar a ATA para esses alunos, é necessário se direcionar para a programação de formatura em que estão pendentes para colar grau ou já colaram grau");
    	}
    }
    
    public void downloadLayoutPadraoImportacaoProgramacaoFormatura() throws Exception {
		try {
			File arquivo = new File(UteisJSF.getCaminhoWeb() + "arquivos" +  File.separator + "ModeloImportacaoProgramacaoFormatura.xlsx");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public List<SelectItem> getListaSelectItemAcaoNaoComparecido() {
		if (listaSelectItemAcaoNaoComparecido == null) {
			listaSelectItemAcaoNaoComparecido = new ArrayList<SelectItem>(0);
			listaSelectItemAcaoNaoComparecido.add(new SelectItem("AUSENTE", "Marcador Como Não Comparecido"));
			listaSelectItemAcaoNaoComparecido.add(new SelectItem("EXCLUSAO", "Excluir da Programação de Formatura"));
			
		}
		return listaSelectItemAcaoNaoComparecido;
	}
    
    
	public void realizarProcessamentoArquivoExcelProgramacaoFormatura(FileUploadEvent uploadEvent) {
		try {
			setNomeArquivo(uploadEvent.getUploadedFile().getName());
			setListaProgramacaoFormaturaAlunoExcel(getFacadeFactory().getProgramacaoFormaturaFacade().realizarProcessamentoExcelPlanilha(uploadEvent));
			realizarDistribuicaoAlunos();
			if(!getListaProgramacaoFormaturaAlunoExcel().isEmpty()) {
				setMensagemID("msg_dados_importados");		
			} else {
				throw new ConsistirException("Não foram encontrados resultados com o arquivo informado");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarDistribuicaoAlunos() {
	    List<ProgramacaoFormaturaAlunoVO> alunosEncontrados = getListaProgramacaoFormaturaAlunoExcel().stream().filter(p1 -> getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream()
               .anyMatch(p2 -> p1.getMatricula().getMatricula().equals(p2.getMatricula().getMatricula()))).collect(Collectors.toList());
	    List<ProgramacaoFormaturaAlunoVO> alunosAusentes = getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream().filter(p1 -> getListaProgramacaoFormaturaAlunoExcel().stream()
               .noneMatch(p2 -> p1.getMatricula().getMatricula().equals(p2.getMatricula().getMatricula()))).collect(Collectors.toList());
	    List<ProgramacaoFormaturaAlunoVO> alunosNaoEncontrados = getListaProgramacaoFormaturaAlunoExcel().stream().filter(p1 -> getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream()
               .noneMatch(p2 -> p1.getMatricula().getMatricula().equals(p2.getMatricula().getMatricula()))).collect(Collectors.toList());
	    alunosEncontrados.forEach(ae ->ae.setColouGrau("SI"));
	    setListaProgramacaoFormaturaAlunosEncontrados(alunosEncontrados);
	    setListaProgramacaoFormaturaAlunosAusentes(alunosAusentes);
	    setListaProgramacaoFormaturaAlunosNaoEncontrados(alunosNaoEncontrados);
	}
	
	public void realizarPresencaColacaoGrauEmLote(){
		getFacadeFactory().getProgramacaoFormaturaFacade().realizarProcessamentoAlunosAusentes(getSituacao(), getProgramacaoFormaturaVO(), getListaProgramacaoFormaturaAlunosAusentes(), getListaProgramacaoFormaturaAlunosEncontrados());
		gravar();
	}
	public List<ProgramacaoFormaturaAlunoVO> getListaProgramacaoFormaturaAlunoExcel() {
		if(listaProgramacaoFormaturaAlunoExcel == null) {
			listaProgramacaoFormaturaAlunoExcel = new ArrayList<ProgramacaoFormaturaAlunoVO>(0);
		}
		return listaProgramacaoFormaturaAlunoExcel;
	}

	public void setListaProgramacaoFormaturaAlunoExcel(
			List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunoExcel) {
		this.listaProgramacaoFormaturaAlunoExcel = listaProgramacaoFormaturaAlunoExcel;
	}

	public String getSituacao() {
		if(situacao == null) {
			situacao= "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNomeArquivo() {
		if(nomeArquivo == null) {
			nomeArquivo= "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public List<ProgramacaoFormaturaAlunoVO> getListaProgramacaoFormaturaAlunosEncontrados() {
		if(listaProgramacaoFormaturaAlunosEncontrados == null) {
			listaProgramacaoFormaturaAlunosEncontrados = new ArrayList<ProgramacaoFormaturaAlunoVO>(0);
		}
		return listaProgramacaoFormaturaAlunosEncontrados;
	}

	public void setListaProgramacaoFormaturaAlunosEncontrados(
			List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosEncontrados) {
		this.listaProgramacaoFormaturaAlunosEncontrados = listaProgramacaoFormaturaAlunosEncontrados;
	}

	public List<ProgramacaoFormaturaAlunoVO> getListaProgramacaoFormaturaAlunosAusentes() {
		if(listaProgramacaoFormaturaAlunosAusentes == null) {
			listaProgramacaoFormaturaAlunosAusentes = new ArrayList<ProgramacaoFormaturaAlunoVO>(0);
		}
		return listaProgramacaoFormaturaAlunosAusentes;
	}

	public void setListaProgramacaoFormaturaAlunosAusentes(
			List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosAusentes) {
		this.listaProgramacaoFormaturaAlunosAusentes = listaProgramacaoFormaturaAlunosAusentes;
	}

	public List<ProgramacaoFormaturaAlunoVO> getListaProgramacaoFormaturaAlunosNaoEncontrados() {
		if(listaProgramacaoFormaturaAlunosNaoEncontrados == null) {
			listaProgramacaoFormaturaAlunosNaoEncontrados = new ArrayList<ProgramacaoFormaturaAlunoVO>(0);
		}
		return listaProgramacaoFormaturaAlunosNaoEncontrados;
	}

	public void setListaProgramacaoFormaturaAlunosNaoEncontrados(
			List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAlunosNaoEncontrados) {
		this.listaProgramacaoFormaturaAlunosNaoEncontrados = listaProgramacaoFormaturaAlunosNaoEncontrados;
	}
}
