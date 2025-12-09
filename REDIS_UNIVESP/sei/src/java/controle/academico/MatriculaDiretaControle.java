package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas matriculaForm.jsp
 * matriculaCons.jsp) com as funcionalidades da classe <code>Matricula</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Matricula
 * @see MatriculaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import negocio.comuns.utilitarias.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.financeiro.MapaLancamentoFuturoControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisponibilidadeHorarioAlunoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioAlunoDiaItemVO;
import negocio.comuns.academico.HorarioAlunoDiaVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.HorarioAlunoVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConvenioCursoVO;
import negocio.comuns.financeiro.ConvenioTurnoVO;
import negocio.comuns.financeiro.ConvenioUnidadeEnsinoVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.financeiro.BoletoBancarioSV;

@Controller("MatriculaDiretaControle")
@Scope("request")
@Lazy
public class MatriculaDiretaControle extends SuperControle implements Serializable {

    private Boolean apresentarPlanoFinanceiroCurso = false;
    private MatriculaVO matriculaVO;
    private String aluno_Erro;
    private String curso_Erro;
    private String inscricao_Erro;
    private String responsavelLiberacaoMatricula_Erro;
    private String usuario_Erro;
    private List listaSelectItemProcessoMatricula;
    private List listaSelectItemTipoMidiaCaptacao;
    private List listaSelectItemTurma;
    private List listaGradeDisciplinas;
    private List listaSelectItemPeriodoLetivo;
    private List listaSelectItemGradeCurricular;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemTurno;
    private List listaSelectItemCurso;
    private List listaSelectItemDescontoAlunoMatricula;
    private List listaSelectItemDescontoAlunoParcela;
    private List listaSelectItemPeriodoLetivoMatricula;
    private List listaConsultaCurso;
    private List listaConsultaConvenio;
    private List listaConsultaPlanoDesconto;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaAluno;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private Integer valorConsultaCandidato;
    private String userName;
    private String senha;
    private PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO;
    private PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
    private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO;
    private MapaLancamentoFuturoVO mapaLancamentoFuturoVO;
    private ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO;
    private List listaSelectItemConvenio;
    private List listaSelectItemPlanoDesconto;
    private List ordemDesconto;
    private List listaSelectItemDescontoProgresivo;
    private ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO;
    private Integer codigoUnidadeEnsinoCurso;
    private boolean validarCadastrarAluno;
    private boolean liberarAvancar;
    private boolean pedirLiberacaoMatricula;
    private boolean exibirMatricula;
    private boolean novaMatriculaPeriodoVencimento;
    private boolean editarParcela;
    private boolean permitirFecharRichModalMatriculaPeriodoVencimento;
    private DocumetacaoMatriculaVO documetacaoMatriculaVO;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private OrdemDescontoVO ordemDescontoVO;
    private String responsavelRenovacaoMatricula_Erro;
    private String turma_Erro;
    private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = null;
    private Boolean turmaComVagasPreenchidas;
    private Boolean turmaComLotacaoPreenchida;
    private Boolean imprimir;
    private Boolean imprimirContrato;
    private Boolean matriculaForaPrazo;
    private List listaSelectItemPlanoFinanceiroCurso;
    private Integer periodoLetivoAdicionar;
    private MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVOAdicionar;
    private List listaSelectItemPeriodoLetivoAdicionar;
    private List listaSelectItemTurmaAdicionar;
    private List listaSelectItemTurmaDisciplinaEquivalenteAdicionar;
    private List listaSelectItemDisciplinaAdicionar;
    private List listaSelectItemDisciplinaEquivalenteAdicionar;
    private HorarioAlunoTurnoVO horarioAlunoTurnoVO;
    private HorarioAlunoVO horarioAlunoVO;
    private Integer gradeDisciplinaAdicionar;
    private Integer gradeDisciplinaEquivalenteAdicionar;
    private MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVoEditada;
    private boolean candidatoEncontrado;
    private boolean cursoAprovado;
    private boolean matriculandoCandidato;

    public MatriculaDiretaControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setImprimir(false);
        setImprimirContrato(false);
        setMensagemID("msg_entre_prmconsulta");
        setValidarCadastrarAluno(false);
    }

    public void matriculaApartirTranferenciaEntrada(TransferenciaEntradaVO transferenciaEntradaVO) {
        try {
            novo();
            if (transferenciaEntradaVO.getMatriculado() == null || transferenciaEntradaVO.getMatricula().getMatricula().equals("")) {
                getFacadeFactory().getMatriculaFacade().incializarDadosAPartirTransferenciaEntrada(getMatriculaVO(), transferenciaEntradaVO, getMatriculaPeriodoVO(), getUsuarioLogado());
            } else {
                editarComMatricula(transferenciaEntradaVO.getMatricula());
                getMatriculaVO().setTransferenciaEntrada(transferenciaEntradaVO);
                getMatriculaPeriodoVO().setTranferenciaEntrada(transferenciaEntradaVO.getCodigo());
            }
            inicializarListasSelectItemTodosComboBox();
            apresentarPrimeiro = new UICommand();
            apresentarAnterior = new UICommand();
            apresentarPosterior = new UICommand();
            apresentarUltimo = new UICommand();
            navegarAbaDadosBasicos();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Matricula</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setMatriculaForaPrazo(false);
        apresentarPlanoFinanceiroCurso = false;
        setTurmaComLotacaoPreenchida(Boolean.FALSE);
        setTurmaComVagasPreenchidas(Boolean.FALSE);
        setMatriculaVO(new MatriculaVO());
        setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
        getMatriculaVO().getAluno().setNome("Selecione o aluno que deseja matricular...");
        setPlanoFinanceiroAlunoVO(new PlanoFinanceiroAlunoVO());
        setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
        setMapaLancamentoFuturoVO(new MapaLancamentoFuturoVO());
        setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
        setMatriculaPeriodoVO(new MatriculaPeriodoVO());
        setOrdemDescontoVO(new OrdemDescontoVO());
        ordemDesconto = new ArrayList(0);
        setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
        inicializarUsuarioResponsavelMatriculaUsuarioLogado();
        setListaDisciplinasGradeCurricular(new ArrayList(0));
        setListaConsultaConvenio(new ArrayList(0));
        setListaConsultaPlanoDesconto(new ArrayList(0));
        gradeDisciplinaAdicionar = 0;
        gradeDisciplinaEquivalenteAdicionar = 0;
        inicializarListasSelectItemTodosComboBox();
        getMatriculaVO().setGuiaAba("Inicio");
        setMensagemID("msg_entre_dados");
        setValidarCadastrarAluno(Boolean.FALSE);
        setLiberarAvancar(Boolean.FALSE);
        setPedirLiberacaoMatricula(Boolean.FALSE);
        setExibirMatricula(Boolean.FALSE);
        setMatriculandoCandidato(false);
        return "editar";
    }

    public void inicializarUsuarioResponsavelMatriculaUsuarioLogado() {
        try {
            getMatriculaVO().setUsuario(getUsuarioLogadoClone());
            planoFinanceiroAlunoVO.setResponsavel(getUsuarioLogadoClone());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Matricula</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado());
        getPreencherDadosMatricula(obj);
        apresentarPlanoFinanceiroCurso = true;
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    public void preparaIncluirDisciplina() {
        setMatriculaPeriodoTurmaDisciplinaVOAdicionar(new MatriculaPeriodoTurmaDisciplinaVO());
        this.gradeDisciplinaAdicionar = 0;
        this.gradeDisciplinaEquivalenteAdicionar = 0;
        montarListaSelectItemPeriodoLetivoAdicionar();
        montarListaSelectItemDisciplina();
        setListaSelectItemTurmaAdicionar(new ArrayList(0));
        setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(new ArrayList(0));
        setMensagemID("msg_dados_editar");
    }

    public void getPreencherDadosMatricula(MatriculaVO obj) throws Exception {
        setMatriculaForaPrazo(false);
        setTurmaComLotacaoPreenchida(Boolean.FALSE);
        setTurmaComVagasPreenchidas(Boolean.FALSE);
        obj.setNovoObj(Boolean.FALSE);
        setMatriculaVO(obj);
        setPlanoFinanceiroAlunoVO(obj.getPlanoFinanceiroAluno());
        setOrdemDesconto(this.getPlanoFinanceiroAlunoVO().obterOrdemAplicacaoDescontosPadraoAtual());
        if (obj.getMatriculaPeriodoVOs() != null && !obj.getMatriculaPeriodoVOs().isEmpty()) {
            setMatriculaPeriodoVO((MatriculaPeriodoVO) obj.getMatriculaPeriodoVOs().get(0));
        }
        inicializarListasSelectItemTodosComboBox();
        inicializarResultadoProcSeletivoInscricao();
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
        montarListasPlanoDescontoConvenio();
        if (!getMatriculaVO().getSituacao().equals("PL") && !getMatriculaVO().getSituacao().equals("ID")) {
            getMatriculaVO().setGuiaAba("DadosBasicos");
        } else {
            navegarAbaDocumentacao();
            navegarAbaPlanoFinanceiroAluno();
        }
    }

    public String subirItemLista() {
        OrdemDescontoVO ordemSubir = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem");
        setOrdemDescontoVO(ordemSubir);
        this.getPlanoFinanceiroAlunoVO().alterarOrdemAplicacaoDescontosSubindoItem(this.ordemDesconto, ordemSubir);
        calcularTotalDesconto();
        return "";
    }

    public String descerItemLista() {
        setOrdemDescontoVO((OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem"));
        this.getPlanoFinanceiroAlunoVO().alterarOrdemAplicacaoDescontosDescentoItem(this.ordemDesconto, getOrdemDescontoVO());
        calcularTotalDesconto();
        return "";
    }

    public String editarComMatricula(MatriculaVO obj) throws Exception {
        setMatriculaForaPrazo(false);
        setTurmaComLotacaoPreenchida(Boolean.FALSE);
        setTurmaComVagasPreenchidas(Boolean.FALSE);
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado());
        obj.setNovoObj(Boolean.FALSE);
        setMatriculaVO(obj);
        setPlanoFinanceiroAlunoVO(obj.getPlanoFinanceiroAluno());
        if (obj.getMatriculaPeriodoVOs() != null && !obj.getMatriculaPeriodoVOs().isEmpty()) {
            setMatriculaPeriodoVO((MatriculaPeriodoVO) obj.getMatriculaPeriodoVOs().get(0));
        }
        inicializarListasSelectItemTodosComboBox();
        inicializarResultadoProcSeletivoInscricao();
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    public void gravarForaPrazo() {
        try {
            verificaPermisaoMatriculaForaPrazo();
            setMatriculaForaPrazo(false);
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
            getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
            adicionarMatriculaPeriodo();
            getMatriculaVO().setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
            if (getMatriculaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            } else {
                inicializarUsuarioResponsavelMatriculaUsuarioLogado();
                getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMatriculaForaPrazo(false);
            setUserName("");
            setSenha("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void gravarPagamentoMatriculaForaPrazo() {
        try {
            verificaPermisaoMatriculaForaPrazo();
            setMatriculaForaPrazo(false);
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
            getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
            adicionarMatriculaPeriodo();
            getMatriculaVO().setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
            if (getMatriculaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            } else {
                inicializarUsuarioResponsavelMatriculaUsuarioLogado();
                getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMatriculaForaPrazo(false);
            setUserName("");
            setSenha("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void verificaPermisaoMatriculaForaPrazo() throws Exception {
        UsuarioVO usuario = Usuario.verificarLoginUsuario(getUserName(), getSenha(), false, Uteis.NIVELMONTARDADOS_TODOS);
        Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "MatriculaForaPrazo");
        getMatriculaPeriodoVO().setResponsavelMatriculaForaPrazo(usuario);
    }

    public void verificarPermissaoLiberarDescontoMatricula() throws Exception {
        if ((getMatriculaPeriodoVO().getValorDescontoMatricula().doubleValue() != 0.0) || (getMatriculaPeriodoVO().getValorDescontoMensalidade().doubleValue() != 0.0)) {
            try {
                if (isPedirLiberacaoMatricula()) {
                    try {
                        UsuarioVO usuario = Usuario.verificarLoginUsuario(getMatriculaVO().getUsuarioLiberacaoDesconto().getUsername(), getMatriculaVO().getUsuarioLiberacaoDesconto().getSenha(),
                                false, Uteis.NIVELMONTARDADOS_TODOS);
                        Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
                        getMatriculaVO().setUsuarioLiberacaoDesconto(usuario);
                        gravar();
                        getMatriculaVO().setUsuarioLiberacaoDesconto(new UsuarioVO());
                        setPedirLiberacaoMatricula(Boolean.FALSE);
                    } catch (Exception e) {
                        setMensagemDetalhada("msg_erro", e.getMessage());
                    }
                } else {
                    UsuarioVO usuario = Usuario.verificarLoginUsuario(this.getUsuarioLogado().getUsername(), this.getUsuarioLogado().getSenha(), false, Uteis.NIVELMONTARDADOS_TODOS);
                    Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
                    getMatriculaVO().setUsuarioLiberacaoDesconto(usuario);
                    getFacadeFactory().getMatriculaFacade().excluirMatriculaDePendenciaFinanceira(matriculaVO, getUsuarioLogado());
                    gravar();
                    getMatriculaVO().setUsuarioLiberacaoDesconto(new UsuarioVO());
                    setPedirLiberacaoMatricula(Boolean.FALSE);
                }
            } catch (Exception e) {
                setMensagemDetalhada("msg_erro", e.getMessage());
                setPedirLiberacaoMatricula(Boolean.TRUE);
            }
        } else {
            gravar();
        }
    }

    public void indeferirMatricula() {
        try {
            UsuarioVO usuario = Usuario.verificarLoginUsuario(this.getUsuarioLogado().getUsername(), this.getUsuarioLogado().getSenha(), false, Uteis.NIVELMONTARDADOS_TODOS);
            Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
            getMatriculaVO().setSituacao("ID");
            enviarComunicacaoAluno(usuario);
            enviarComunicacaoFuncionario(usuario);
            getFacadeFactory().getMatriculaFacade().excluirMatriculaDePendenciaFinanceira(matriculaVO, getUsuarioLogado());
            gravar();
        } catch (Exception e) {
            getMatriculaVO().setSituacao("PL");
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void enviarComunicacaoAluno(UsuarioVO usuario) throws Exception {
        ComunicacaoInternaVO com = new ComunicacaoInternaVO();
        com.setAssunto("Matrícula Indeferida");
        com.setData(new Date());
        com.setEnviarEmail(false);
        com.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
        com.setMensagem("A solicitação de desconto para a matrícula " + getMatriculaVO().getMatricula() + " foi indeferida pelo departamento financeiro");
        com.setResponsavel(usuario.getPessoa());
        com.setTipoDestinatario("AL");
        com.setComunicadoInternoDestinatarioVOs(montarListaDestinatarioAluno());
        getFacadeFactory().getComunicacaoInternaFacade().incluir(com, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
    }

    public void enviarComunicacaoFuncionario(UsuarioVO usuario) throws Exception {
        ComunicacaoInternaVO com = new ComunicacaoInternaVO();
        com.setAssunto("Matrícula Indeferida");
        com.setData(new Date());
        com.setEnviarEmail(false);
        com.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
        com.setMensagem("A solicitação de desconto para a matrícula " + getMatriculaVO().getMatricula() + " foi indeferida pelo departamento financeiro.\n" + "Entre em contato com o aluno: "
                + getMatriculaVO().getAluno().getNome() + " pelo fone: " + getMatriculaVO().getAluno().getTelefoneRes());
        com.setResponsavel(usuario.getPessoa());
        com.setTipoDestinatario("FU");
        com.setComunicadoInternoDestinatarioVOs(montarListaDestinatarioFuncionario());
        getFacadeFactory().getComunicacaoInternaFacade().incluir(com, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
    }

    public List<ComunicadoInternoDestinatarioVO> montarListaDestinatarioAluno() {
        try {
            List lista = new ArrayList(0);
            ComunicadoInternoDestinatarioVO CID = new ComunicadoInternoDestinatarioVO();
            CID.setDestinatario(getMatriculaVO().getAluno());
            CID.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
            CID.setTipoComunicadoInterno("AL");
            lista.add(CID);
            return lista;
        } catch (Exception e) {
            return new ArrayList(0);
        }
    }

    public List<ComunicadoInternoDestinatarioVO> montarListaDestinatarioFuncionario() {
        try {
            List lista = new ArrayList(0);
            ComunicadoInternoDestinatarioVO CID = new ComunicadoInternoDestinatarioVO();
            UsuarioVO responsavelMatricula = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(getMatriculaVO().getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            CID.setDestinatario(responsavelMatricula.getPessoa());
            CID.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
            CID.setTipoComunicadoInterno("FU");
            lista.add(CID);
            return lista;
        } catch (Exception e) {
            return new ArrayList(0);
        }
    }

    public void enviarMatriculaMapaPendencia() throws Exception {
        getMatriculaVO().setUsuarioLiberacaoDesconto(new UsuarioVO());
        if (getMatriculaVO().getSituacao().equals("ID")) {
            getMatriculaVO().setMatriculaIndeferida(Boolean.TRUE);
        }
        getMatriculaVO().setSituacao("PL");
        gravar();
        getFacadeFactory().getMapaLancamentoFuturoFacade().incluir(getFacadeFactory().getMatriculaFacade().popularMapaLancamentoFuturo(matriculaVO, getProcessoCalendarioMatriculaVO()), getUsuarioLogado());
        setPedirLiberacaoMatricula(Boolean.FALSE);
        getMatriculaVO().setMatriculaIndeferida(Boolean.FALSE);
    }

    public void fecharForaPrazo() {
        setMatriculaForaPrazo(Boolean.FALSE);
        setUserName("");
        setSenha("");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Matricula</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            // setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(getMatriculaPeriodoVO().getUnidadeEnsinoCurso()));
            if (getMatriculaForaPrazo()) {
                return "editar";
            }
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
            getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
            adicionarMatriculaPeriodo();
            getMatriculaVO().setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
            getMatriculaVO().setValorMatricula(getMatriculaPeriodoVO().getValorFinalMatricula());
            if (getMatriculaVO().isNovoObj().booleanValue()) {
                getMatriculaVO().setDataInicioCurso(getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
                getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            } else {
                inicializarUsuarioResponsavelMatriculaUsuarioLogado();
                getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            navegarAbaDisciplinaMatriculado();
            return "editar";
        } catch (Exception e) {
            // getMatriculaVO().setMatricula("");
            // getMatriculaPeriodoVO().setNrDocumento("");
            setMatriculaForaPrazo(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public String gravarTransferenciaEntrada() {
        try {
            inicializarGradeCurricularMatriculaPeriodo();
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
            adicionarMatriculaPeriodo();
            getMatriculaVO().setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
            if (getMatriculaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, verificarProcessoMatriculaDentroPrazo(getMatriculaPeriodoVO().getUnidadeEnsinoCurso()),
                        getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            } else {
                inicializarUsuarioResponsavelMatriculaUsuarioLogado();
                getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            }
            inicializarControleTransferenciaEntrada();
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public String navegarParaCadastroAluno() {
        return "cadastrarAluno";
    }

    public void navegarAbaDadosBasicos() {
        getMatriculaVO().setGuiaAba("DadosBasicos");
    }

    public void navegarAbaLiberadaMatricula() {
        getMatriculaVO().setGuiaAba("LiberadaMatricula");
    }

    public String acessarMatriculaParaImpressaoAcompanhamento() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado());
        getPreencherDadosMatricula(obj);
        apresentarPlanoFinanceiroCurso = false;
        setMensagemID("msg_dados_consultados");
        getMatriculaVO().setGuiaAba("DisciplinaMatriculado");
        return "editar";
    }

    // public void inicializarPlanoFinanceiroAluno
    public void navegarAbaPlanoFinanceiroAluno() {
        try {
            if (!getMatriculaVO().getMatriculaJaRegistrada()) {
                getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(matriculaVO, getMatriculaPeriodoVO(), getPlanoFinanceiroAlunoVO(), true, getUsuarioLogado());
                montarListasPlanoDescontoConvenio();
            }
            montarListaSelectItemDescontoProgressivo();
            montarListaSelectItemConvenio();
            setMensagemID("");
            setMensagemDetalhada("");
            getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
            calcularTotalDesconto();
            getMatriculaVO().setGuiaAba("PlanoFinanceiroAluno");
        } catch (Exception e) {
            navegarAbaDisciplinas();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    @SuppressWarnings({"static-access", "static-access"})
    public void navegarAbaDisciplinas() {
        try {
            getMatriculaVO().setGuiaAba("Disciplinas");
            // if (getMatriculaVO().getMatricula().equals("")) {
            // getFacadeFactory().getMatriculaPeriodoFacade().inicializarDescontoProgressivoPadraoMatriculaPeriodo(matriculaVO,
            // matriculaPeriodoVO);
            // }
            getFacadeFactory().getMatriculaPeriodoFacade().inicializarMatriculaPeriodoTurmaDisciplinaGradeCurso(matriculaVO, matriculaPeriodoVO, getUsuarioLogado(), null, false, false);
            if (getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() != 0) {
                getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(
                        getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(),
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matriculaVO, matriculaPeriodoVO, getUsuarioLogado());
            }

            // getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(this.getUsuarioLogadoClone());
            // if (!getMatriculaVO().getSituacao().equals("PL")) {
            // getMatriculaVO().setUsuario(getUsuarioLogadoClone());
            // }
            // getMatriculaVO().validarDados(getMatriculaVO());
            // getMatriculaPeriodoVO().validarDados(getMatriculaPeriodoVO());
            // setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(getMatriculaPeriodoVO().getUnidadeEnsinoCurso()));
            // getMatriculaPeriodoVO().validarVagaNaTurma(getMatriculaPeriodoVO(), getProcessoCalendarioMatriculaVO());

            // getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(),
            // Uteis.NIVELMONTARDADOS_TODOS));

            setMensagemID("");
            setMensagemDetalhada("");
        } catch (Exception e) {
            navegarAbaDocumentacao();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    @SuppressWarnings({"static-access", "static-access"})
    public void navegarAbaDocumentacao() {
        try {
            getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(this.getUsuarioLogadoClone());
            if (!getMatriculaVO().getSituacao().equals("PL")) {
                getMatriculaVO().setUsuario(getUsuarioLogadoClone());
            }
            getMatriculaVO().validarDados(getMatriculaVO());
            getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(matriculaVO, false, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(),false, false);
            getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaVO, matriculaPeriodoVO, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            getMatriculaPeriodoVO().setData(this.getMatriculaVO().getData());
            MatriculaPeriodoVO.validarDados(getMatriculaPeriodoVO());
            MatriculaPeriodoVO.validarProcessoMatriculaCalendario(matriculaVO, matriculaPeriodoVO, isPermitirMatriculaForaPrazo(getUsuarioLogado()));

            TurmaVO turma = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTurma().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            Integer nrMatricula = getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeAlunoMatriculadoTurma(turma.getCodigo().intValue(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getProcessoMatricula(), false, getUsuarioLogado());
            MatriculaPeriodoVO.validarVagaNaTurma(matriculaPeriodoVO, nrMatricula, turma);

            getMatriculaVO().setGuiaAba("Documentacao");
            setMensagemID("");
            setMensagemDetalhada("");
        } catch (Exception e) {
            navegarAbaDadosBasicos();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<TransferenciaEntradaDisciplinasAproveitadasVO> getDisciplinasAproveitamento() throws Exception {
        if (getMatriculaPeriodoVO().getTranferenciaEntrada() != 0) {
            TransferenciaEntradaVO transferenciaEntradaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTranferenciaEntrada(),
                    Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs() != null || transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs().size() != 0) {
                return transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs();
            } else {
                return new ArrayList(0);
            }
        }
        return new ArrayList(0);
    }

    public void navegarAbaDisciplinaMatriculado() {
        getMatriculaVO().setGuiaAba("DisciplinaMatriculado");
    }

    public void navegarAbaInicio() {
        getMatriculaVO().setGuiaAba("Inicio");
    }

    public String navegarCadastrarAluno() {
        try {
            AlunoControle alunoControle = null;
            alunoControle = (AlunoControle) context().getExternalContext().getSessionMap().get(AlunoControle.class.getSimpleName());
            if (alunoControle == null) {
                alunoControle = new AlunoControle();
                context().getExternalContext().getSessionMap().put(AlunoControle.class.getSimpleName(), alunoControle);
            }
            alunoControle.novo();
            return "cadastrarAluno";
        } catch (Exception e) {
            return "";
        }
    }

    public String navegarMapaLancamentoFuturo() {
        try {
            return navegarPara(MapaLancamentoFuturoControle.class.getSimpleName(), "abaMatricula", "mapa");
        } catch (Exception e) {
            return "";
        }
    }

    public String getForaPrazo() {
        if (getMatriculaForaPrazo()) {
            return "RichFaces.$('panelForaPrazo').show()";
        }
        return "RichFaces.$('panelForaPrazo').hide()";
    }

    public String getPagamentoMatriculaForaPrazo() {
        if (getMatriculaForaPrazo()) {
            return "RichFaces.$('panelPagamentoMatriculaForaPrazo').show()";
        }
        return "RichFaces.$('panelPagamentoMatriculaForaPrazo').hide()";
    }

    public ProcessoMatriculaCalendarioVO verificarProcessoMatriculaDentroPrazo(Integer codigoUnidadeEnsinoCurso) throws Exception {
        try {
            setUserName("");
            setSenha("");
//            List listaPeriodoMatricula = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarProcessoMatriculaUnidadeEnsinoCursoDentroPrazo(codigoUnidadeEnsinoCurso, new Date(), false,
//                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            setMatriculaForaPrazo(false);
            return null;

        } catch (Exception e) {
//            TRY {
//                PROCESSOMATRICULACALENDARIOVO OBJ = VERIFICARPROCESSOMATRICULAFORAPRAZO(CODIGOUNIDADEENSINOCURSO);
//                SETMATRICULAFORAPRAZO(TRUE);
//                RETURN OBJ;
//            } CATCH (EXCEPTION X) {
//                SETMATRICULAFORAPRAZO(FALSE);
//                THROW X;
//            }
        }
        return null;

    }

//    public ProcessoMatriculaCalendarioVO verificarProcessoMatriculaForaPrazo(Integer codigoUnidadeEnsinoCurso) throws Exception {
//        List listaPeriodoMatricula = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarProcessoMatriculaUnidadeEnsinoCursoForaPrazo(codigoUnidadeEnsinoCurso, new Date(), false,
//                Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//        return MatriculaVO.validarPeriodoProcessoMatricula(listaPeriodoMatricula);
//    }

    public void inicializarControleTransferenciaEntrada() {
        TransferenciaEntradaControle transferenciaEntradaControle = (TransferenciaEntradaControle) context().getExternalContext().getSessionMap().get("TransferenciaEntradaControle");
        transferenciaEntradaControle.getTransferenciaEntradaVO().setMatricula(this.getMatriculaVO());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP MatriculaCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                if (!getControleConsulta().getValorConsulta().isEmpty()) {
                    objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
                } else {
                    setMensagemID("msg_entre_dados");
                }
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("cpf")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCPF(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), this.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
            }
//            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
//                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
//            }
//            if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
//                objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacaoFinanceira(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
//                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
//            }
            if (getControleConsulta().getCampoConsulta().equals("nomeResponsavel")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }
            setListaConsulta(objs);
            if (!objs.isEmpty()) {
                setMensagemID("msg_dados_consultados");
            }
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>MatriculaVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            if (getMatriculaVO().getSituacaoFinanceira().equals("PF")) {
                getMatriculaVO().setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
                getFacadeFactory().getMatriculaFacade().excluir(matriculaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                novo();
                setMensagemID("msg_dados_excluidos");
            } else {
                setMensagemDetalhada("Não é possivel Excluir uma Matrícula já Quitada");
            }
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					throw new Exception("Informe um código para realização da consulta!");
					// setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getMatriculaVO().getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getMatriculaVO().getUnidadeEnsino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

    public void montarConsultaCurso() {
        setListaConsultaCurso(new ArrayList(0));
    }

    public void selecionarCurso() throws Exception {
        try {
            UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocurso");
            getMatriculaVO().setCurso(unidadeEnsinoCurso.getCurso());
            getMatriculaVO().setTurno(unidadeEnsinoCurso.getTurno());
            setMensagemDetalhada("");
            montarListaSelectItemGradeCurricular();
            listaSelectItemPlanoFinanceiroCurso = new ArrayList(0);
            getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(getMatriculaVO(), getUsuarioLogado());
            getMatriculaPeriodoVO().setUnidadeEnsinoCurso(unidadeEnsinoCurso.getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosMatriculaVo() {
        setMatriculandoCandidato(false);
        setMatriculaVO(new MatriculaVO());
        inicializarUsuarioResponsavelMatriculaUsuarioLogado();
    }

    public String iniciarMatriculaCandidato(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) throws Exception {
        setResultadoProcessoSeletivoVO(resultadoProcessoSeletivoVO);
        return iniciarMatriculaCandidato();
    }

    public String iniciarMatriculaCandidato() throws Exception {
        try {
            setMatriculandoCandidato(true);
            getMatriculaVO().setAluno(getResultadoProcessoSeletivoVO().getInscricao().getCandidato());
            GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCurso(getMatriculaVO().getCurso().getCodigo(), "AT", false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (gradeCurricularVO.getCodigo() == 0) {
                throw new Exception("Não existe uma Grade Curricular Ativa para este curso.");
            }
            setMensagemDetalhada("");
            montarListaSelectItemGradeCurricular();
            setListaSelectItemPlanoFinanceiroCurso(new ArrayList(0));
            getMatriculaVO().setFormaIngresso("PS");
            getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(getMatriculaVO(), getUsuarioLogado());
            montarListaProcessoMatricula();

            setMensagemID("msg_entre_dados");
            getMatriculaVO().setGuiaAba("DadosBasicos");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void iniciarConsultaCandidado() {
        setCandidatoEncontrado(false);
        setCursoAprovado(false);
    }

    public void consultarCandidato() {
        try {
            setMatriculandoCandidato(false);
            setCandidatoEncontrado(false);
            setResultadoProcessoSeletivoVO((getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(getValorConsultaCandidato(), false,
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado())));
            setCandidatoEncontrado(true);
            montarCursoAprovado();
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarCursoAprovado() throws Exception {
        if (getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
            getMatriculaVO().setCurso(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao1().getCurso());
            getMatriculaVO().setUnidadeEnsino(
                    getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao1().getUnidadeEnsino(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            getMatriculaVO().setTurno(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao1().getTurno());
            getMatriculaPeriodoVO().setUnidadeEnsinoCurso(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao1().getCodigo());
            setCursoAprovado(true);
        } else {
            if (getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("AP")) {
                getMatriculaVO().setCurso(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao2().getCurso());
                getMatriculaVO().setUnidadeEnsino(
                        getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao2().getUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                getMatriculaVO().setTurno(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao1().getTurno());
                getMatriculaPeriodoVO().setUnidadeEnsinoCurso(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao2().getCodigo());
                setCursoAprovado(true);
            } else {
                if (getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("AP")) {
                    getMatriculaVO().setCurso(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao3().getCurso());
                    getMatriculaVO().setUnidadeEnsino(
                            getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao3().getUnidadeEnsino(), false,
                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
                    getMatriculaVO().setTurno(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao1().getTurno());
                    getMatriculaPeriodoVO().setUnidadeEnsinoCurso(getResultadoProcessoSeletivoVO().getInscricao().getCursoOpcao3().getCodigo());
                    setCursoAprovado(true);
                } else {
                    setCursoAprovado(false);
                    throw new ConsistirException("O candidato não foi aprovado no Processo Seletivo.");
                }
            }
        }
    }

    public void consultarGradeDisciplinas() throws Exception {
        getMatriculaPeriodoVO().getPeridoLetivo().setGradeDisciplinaVOs(
                getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo(), false, getUsuarioLogado(), null));
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaAluno().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaAluno(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaAluno(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("RG")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaAluno(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }

            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarConsultaAluno() {
        setListaConsultaAluno(new ArrayList(0));
    }

    public void selecionarAluno() throws Exception {
        setMatriculandoCandidato(false);
        PessoaVO aluno = (PessoaVO) context().getExternalContext().getRequestMap().get("aluno");
        getMatriculaVO().setAluno(aluno);
//        getMatriculaVO().getAluno().setNome(aluno.getNome());
//        getMatriculaVO().getAluno().setCPF(aluno.getCPF());
//        getMatriculaVO().getAluno().setRG(aluno.getRG());
        if (aluno.getDataNasc() == null) {
            setMensagemDetalhada("msg_erro", "Não é possível matricular o aluno " + aluno.getNome().toUpperCase() + ", pois o mesmo não possui uma data de nascimento informada.");
            getMatriculaVO().setGuiaAba("Inicio");
        } else {
            setMensagemID("msg_entre_dados");
            getMatriculaVO().setGuiaAba("DadosBasicos");
        }

    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    public List getTipoConsultaComboCandidato() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("inscricao", "Inscrição"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("RG", "RG"));
        return itens;
    }

    public boolean getExisteUnidadeEnsino() {
        if (getMatriculaVO().getUnidadeEnsino().getCodigo().intValue() != 0 || !(getMatriculaVO().getUnidadeEnsino().getNome().equals(""))) {
            return true;
        } else {
            return false;
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>MatriculaPeriodo</code> para o objeto
     * <code>matriculaVO</code> da classe <code>Matricula</code>
     */
    public void adicionarMatriculaPeriodo() throws Exception {
        // if (getMatriculaPeriodoVO().getPeridoLetivo().getCodigo().intValue() != 0) {
        // Integer campoConsulta = getMatriculaPeriodoVO().getPeridoLetivo().getCodigo();
        // PeriodoLetivoVO periodoLetivo =
        // getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(campoConsulta);
        // getMatriculaPeriodoVO().setPeridoLetivo(periodoLetivo);
        // }
        // if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() != 0) {
        // Integer campoGradeCurricular = getMatriculaPeriodoVO().getGradeCurricular().getCodigo();
        // GradeCurricularVO gradeCurricular =
        // getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(campoGradeCurricular,
        // Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        // getMatriculaPeriodoVO().setGradeCurricular(gradeCurricular);
        // }
        if (getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().getCodigo().intValue() == 0) {
            getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
        }
        getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
        getMatriculaVO().adicionarObjMatriculaPeriodoVOs(getMatriculaPeriodoVO());

        setMensagemID("msg_dados_adicionados");
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>MatriculaPeriodo</code> para edição pelo
     * usuário.
     */
    public String editarMatriculaPeriodo() throws Exception {
        MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodo");
        setMatriculaPeriodoVO(obj);
        return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>MatriculaPeriodo</code> do objeto
     * <code>matriculaVO</code> da classe <code>Matricula</code>
     */
    public String removerMatriculaPeriodo() throws Exception {
        MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodo");
        getMatriculaVO().excluirObjMatriculaPeriodoVOs(obj.getPeridoLetivo().getCodigo());
        setMensagemID("msg_dados_excluidos");
        return "editar";
    }

    public void adicionarItemPlanoFinanceiroAlunoPlanoDesconto() throws Exception {
        try {
            if (!getPlanoFinanceiroAlunoVO().getCodigo().equals(0)) {
                getItemPlanoFinanceiroAlunoVO().setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO().getCodigo());
            }
            if (getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo().intValue() != 0) {
                getItemPlanoFinanceiroAlunoVO().setTipoItemPlanoFinanceiro("PD");
                Integer campoConsulta = getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo();
                PlanoDescontoVO planoDesconto = getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                getItemPlanoFinanceiroAlunoVO().setPlanoDesconto(planoDesconto);
            }            
            getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(getPlanoFinanceiroAlunoVO(), getItemPlanoFinanceiroAlunoVO());
            this.setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
            calcularTotalDesconto();
            montarListasPlanoDescontoConvenio();
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void adicionarItemPlanoFinanceiroAlunoConvenio() throws Exception {
        try {
            if (!getPlanoFinanceiroAlunoVO().getCodigo().equals(0)) {
                itemPlanoFinanceiroAlunoVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO().getCodigo());
            }
            if (getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo().intValue() != 0) {
                getItemPlanoFinanceiroAlunoVO().setTipoItemPlanoFinanceiro("CO");
                Integer campoConsulta = getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo();
                ConvenioVO convenio = getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
                getItemPlanoFinanceiroAlunoVO().setConvenio(convenio);
            }            
            getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(getPlanoFinanceiroAlunoVO(), getItemPlanoFinanceiroAlunoVO());
            this.setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
            calcularTotalDesconto();
            montarListasPlanoDescontoConvenio();
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void montarListasPlanoDescontoConvenio() {
        setListaConsultaConvenio(new ArrayList(0));
        setListaConsultaPlanoDesconto(new ArrayList(0));
        int indexConvenio = 0;
        int indexPlanoDesconto = 0;
        Iterator i = getPlanoFinanceiroAlunoVO().getItemPlanoFinanceiroAlunoVOs().iterator();
        while (i.hasNext()) {
            ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
            if (objExistente.getTipoPlanoFinanceiroConvenio().booleanValue()) {
                listaConsultaConvenio.add(indexConvenio, objExistente);
                indexConvenio++;
            } else {
                listaConsultaPlanoDesconto.add(indexPlanoDesconto, objExistente);
                indexPlanoDesconto++;
            }
        }
    }

    public void atualizarValorCheioPlanoFinanceiroAluno() {
        OrdemDescontoVO ordemClicou = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem");
        this.getPlanoFinanceiroAlunoVO().atualizarSituacaoValorCheioOrdemDesconto(ordemClicou);
        calcularTotalDesconto();
    }

    public void calcularTotalDesconto() {
        getMatriculaPeriodoVO().descontoTotalMatricula(getPlanoFinanceiroCursoVO(), getPlanoFinanceiroAlunoVO(), getMatriculaPeriodoVO().getValorMatriculaCheio(), getOrdemDesconto(),
                getCondicaoPagamentoPlanoFinanceiroCursoVO());
        getMatriculaPeriodoVO().descontoTotalMensalidade(getPlanoFinanceiroCursoVO(), getPlanoFinanceiroAlunoVO(), getMatriculaPeriodoVO().getValorMensalidadeCheio(), getOrdemDesconto(),
                getCondicaoPagamentoPlanoFinanceiroCursoVO());
    }

    // public void valorCheioTodosDescontos() {
    // getMatriculaVO().getPlanoFinanceiroAluno().setOrdemDescontoAluno(0);
    // getMatriculaVO().getPlanoFinanceiroAluno().setOrdemPlanoDesconto(0);
    // getMatriculaVO().getPlanoFinanceiroAluno().setOrdemConvenio(0);
    // }
    public void montarListaOrdemDesconto() throws Exception {
        if (this.ordemDesconto == null) {
            ordemDesconto = new ArrayList(0);
        }
        if (!this.ordemDesconto.isEmpty()) {
            return;
        }
        if (this.getMatriculaVO().getMatriculaJaRegistrada()) {
            setOrdemDesconto(this.getPlanoFinanceiroAlunoVO().obterOrdemAplicacaoDescontosPadraoAtual());
        } else {
            setOrdemDesconto(this.getPlanoFinanceiroAlunoVO().inicializarOrdemAplicacaoDescontosPadrao());
        }
    }

    public void alterarTipoDescontoAlunoMatricula() {
        if (getPlanoFinanceiroAlunoVO().getTipoDescontoMatricula().equals("PO")) {
            getPlanoFinanceiroAlunoVO().setPercDescontoMatricula(getPlanoFinanceiroAlunoVO().getValorDescontoMatricula());
            getPlanoFinanceiroAlunoVO().setValorDescontoMatricula(0.0);
        } else if (getPlanoFinanceiroAlunoVO().getTipoDescontoMatricula().equals("VA")) {
            getPlanoFinanceiroAlunoVO().setValorDescontoMatricula(getPlanoFinanceiroAlunoVO().getPercDescontoMatricula());
            getPlanoFinanceiroAlunoVO().setPercDescontoMatricula(0.0);
        }
        calcularTotalDesconto();
    }

    public void alterarTipoDescontoAlunoParcela() {
        if (getPlanoFinanceiroAlunoVO().getTipoDescontoParcela().equals("PO")) {
            getPlanoFinanceiroAlunoVO().setPercDescontoParcela(getPlanoFinanceiroAlunoVO().getValorDescontoParcela());
            getPlanoFinanceiroAlunoVO().setValorDescontoParcela(0.0);
        } else if (getPlanoFinanceiroAlunoVO().getTipoDescontoParcela().equals("VA")) {
            getPlanoFinanceiroAlunoVO().setValorDescontoParcela(getPlanoFinanceiroAlunoVO().getPercDescontoParcela());
            getPlanoFinanceiroAlunoVO().setPercDescontoParcela(0.0);
        }
        calcularTotalDesconto();
    }

    public void montarListaOrdemDescontoComMatricula() throws Exception {
        if (!this.ordemDesconto.isEmpty()) {
            return;
        }
        if (this.getMatriculaVO().getMatriculaJaRegistrada()) {
            setOrdemDesconto(this.getPlanoFinanceiroAlunoVO().obterOrdemAplicacaoDescontosPadraoAtual());
        } else {
            setOrdemDesconto(this.getPlanoFinanceiroAlunoVO().inicializarOrdemAplicacaoDescontosPadrao());
        }
    }

    // public void montarListaOrdemDescontoComMatriculaValorTotal() {
    // OrdemDescontoVO ordem = new OrdemDescontoVO();
    // List obj = new ArrayList(0);
    // for (int i = 0; i <= 2; i++) {
    // if (this.getPlanoFinanceiroAlunoVO().getOrdemDescontoAluno().intValue() == i + 3) {
    // ordem = new OrdemDescontoVO();
    // ordem.setDescricaoDesconto("Desconto Aluno");
    // ordem.setValorCheio(Boolean.TRUE);
    // obj.set(i, ordem);
    // }
    // if (this.getPlanoFinanceiroAlunoVO().getOrdemPlanoDesconto().intValue() == i + 3) {
    // ordem = new OrdemDescontoVO();
    // ordem.setDescricaoDesconto("Plano Desconto");
    // ordem.setValorCheio(Boolean.TRUE);
    // obj.set(i, ordem);
    // }
    // if (this.getPlanoFinanceiroAlunoVO().getOrdemConvenio().intValue() == i + 3) {
    // ordem = new OrdemDescontoVO();
    // ordem.setDescricaoDesconto("Convênio");
    // ordem.setValorCheio(Boolean.TRUE);
    // obj.set(i, ordem);
    // }
    // }
    // setOrdemDesconto(obj);
    // }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>ItemPlanoFinanceiroAluno</code> para
     * edição pelo usuário.
     */
    public void editarItemPlanoFinanceiroAluno() throws Exception {
        ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAluno");
        setItemPlanoFinanceiroAlunoVO(obj);

    }

    /*
     * Método responsável por remover um novo objeto da classe <code>ItemPlanoFinanceiroAluno</code> do objeto
     * <code>planoFinanceiroAlunoVO</code> da classe <code>PlanoFinanceiroAluno</code>
     */
    public void removerItemPlanoFinanceiroAluno() throws Exception {
        ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAluno");
        getPlanoFinanceiroAlunoVO().excluirObjItemPlanoFinanceiroAlunoVOs(obj.getPlanoDesconto().getCodigo());
        montarListasPlanoDescontoConvenio();
        calcularTotalDesconto();
        setMensagemID("msg_dados_excluidos");
    }

    /*
     * Método responsável por adicionar um novo objeto da classe <code>DocumetacaoMatricula</code> para o objeto
     * <code>matriculaVO</code> da classe <code>Matricula</code>
     */
    public String adicionarDocumetacaoMatricula() throws Exception {
        try {
            if (!getMatriculaVO().getMatricula().equals("")) {
                getDocumetacaoMatriculaVO().setMatricula(getMatriculaVO().getMatricula());
            }
            if (getDocumetacaoMatriculaVO().isEntregue()) {
                getDocumetacaoMatriculaVO().setUsuario(getUsuarioLogadoClone());
                getDocumetacaoMatriculaVO().setDataEntrega(new Date());
            }
            getDocumetacaoMatriculaVO().setTipoDeDocumentoVO(
                    getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            getMatriculaVO().adicionarObjDocumetacaoMatriculaVOs(getDocumetacaoMatriculaVO());
            this.setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
            setMensagemID("msg_dados_adicionados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /*
     * Método responsável por disponibilizar dados de um objeto da classe <code>DocumetacaoMatricula</code> para edição
     * pelo usuário.
     */
    public String editarDocumetacaoMatricula() throws Exception {
        DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatricula");
        setDocumetacaoMatriculaVO(obj);
        return "editar";
    }

    /*
     * Método responsável por remover um novo objeto da classe <code>DocumetacaoMatricula</code> do objeto
     * <code>matriculaVO</code> da classe <code>Matricula</code>
     */
    public String removerDocumetacaoMatricula() throws Exception {
        DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatricula");
        getMatriculaVO().excluirObjDocumetacaoMatriculaVOs(obj.getTipoDeDocumentoVO().getCodigo());
        setMensagemID("msg_dados_excluidos");
        return "editar";
    }

    public void irPaginaInicial() throws Exception {
        // controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        getControleConsulta().setPaginaAtual(getControleConsulta().getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        getControleConsulta().setPaginaAtual(getControleConsulta().getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        getControleConsulta().setPaginaAtual(getControleConsulta().getNrTotalPaginas());
        this.consultar();
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PlanoDesconto</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PlanoDesconto</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemPlanoDesconto() {
        try {
            List resultadoConsulta = consultarPlanoDescontoPorNome("");
            setListaSelectItemPlanoDesconto(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPlanoDescontoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPlanoDescontoFacade().consultarPorNome(nomePrm, getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado(), 0, 0);
        return lista;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoItemPlanoFinanceiro</code>
     */
    public List getListaSelectItemTipoItemPlanoFinanceiroItemPlanoFinanceiroAluno() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoItemPlanoFinanceiroAlunos = (Hashtable) Dominios.getTipoItemPlanoFinanceiroAluno();
        Enumeration keys = tipoItemPlanoFinanceiroAlunos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoItemPlanoFinanceiroAlunos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Convenio</code>.
     */
    public void montarListaSelectItemConvenio(MatriculaVO matricula, String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarConvenioPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ConvenioVO obj = (ConvenioVO) i.next();
                if (validarConvenioUnidadeEnsino(matricula, obj) && validarConvenioCurso(matricula, obj) && validarConvenioTurno(matricula, obj)) {
                    objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                }
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemConvenio(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Responsavel por validar se a Uniadade de Ensino da Matricula esta inserida no Convenio
     *
     * @param matricula
     * @param convenio
     * @return
     */
    public boolean validarConvenioUnidadeEnsino(MatriculaVO matricula, ConvenioVO convenio) {
        if (convenio.isValidoParaTodaUnidadeEnsino().booleanValue()) {
            return true;
        } else {
            Iterator i = convenio.getConvenioUnidadeEnsinoVOs().iterator();
            while (i.hasNext()) {
                ConvenioUnidadeEnsinoVO unidadeEnsino = (ConvenioUnidadeEnsinoVO) i.next();
                if (matricula.getUnidadeEnsino().getCodigo().intValue() == unidadeEnsino.getUnidadeEnsino().getCodigo().intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Responsavel por validar se o Curso da Matricula esta inserida no Convenio
     *
     * @param matricula
     * @param convenio
     * @return
     */
    public boolean validarConvenioCurso(MatriculaVO matricula, ConvenioVO convenio) {
        if (convenio.isValidoParaTodoCurso().booleanValue()) {
            return true;
        } else {
            Iterator i = convenio.getConvenioCursoVOs().iterator();
            while (i.hasNext()) {
                ConvenioCursoVO curso = (ConvenioCursoVO) i.next();
                if (matricula.getCurso().getCodigo().intValue() == curso.getCurso().getCodigo().intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Responsavel por validar se o Turno da Matricula esta inserida no Convenio
     *
     * @param matricula
     * @param convenio
     * @return
     */
    public boolean validarConvenioTurno(MatriculaVO matricula, ConvenioVO convenio) {
        if (convenio.isValidoParaTodoTurno().booleanValue()) {
            return true;
        } else {
            Iterator i = convenio.getConvenioTurnoVOs().iterator();
            while (i.hasNext()) {
                ConvenioTurnoVO turno = (ConvenioTurnoVO) i.next();
                if (matricula.getTurno().getCodigo().intValue() == turno.getTurno().getCodigo().intValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Convenio</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Convenio</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto
     * é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemConvenio() {
        try {
            montarListaSelectItemConvenio(getMatriculaVO(), "");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarConvenioPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getConvenioFacade().consultarPorAtivoVigenteESituacao(true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivoMatricula</code>. Buscando
     * todos os objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemPeriodoLetivo() {
        List resultadoConsulta = null;
        try {
            if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() == 0) {
                setListaSelectItemPeriodoLetivoMatricula(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarPeriodoLetivoPorGradeCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
            setListaSelectItemPeriodoLetivoMatricula(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao", false));
            if (getMatriculaPeriodoVO().getPeridoLetivo().getCodigo().intValue() == 0 && getListaSelectItemPeriodoLetivoMatricula().size() > 0) {
                getMatriculaPeriodoVO().getPeridoLetivo().setCodigo((Integer) ((SelectItem) getListaSelectItemPeriodoLetivoMatricula().get(0)).getValue());
            }
            montarListaSelectItemTurma();
        } catch (Exception e) {
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>sigla</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPeriodoLetivoPorGradeCurricular(Integer gradeCurricular) throws Exception {
        List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>PeriodoLetivo</code>.
     */
    public void montarListaSelectItemGradeCurricular() throws Exception {
        try {
            if (!getMatriculaVO().getIsUnidadeEnsinoCursoSelecionado()) {
                setListaSelectItemGradeCurricular(new ArrayList(0));
                montarListaSelectItemPeriodoLetivo();
                return;
            }
            List listaGrades = consultarGradeCurricularPorCursoSituacaoAtiva();
            setListaSelectItemGradeCurricular(UtilSelectItem.getListaSelectItem(listaGrades, "codigo", "nome", false));
            if (getListaSelectItemGradeCurricular().size() > 0) {
                getMatriculaPeriodoVO().getGradeCurricular().setCodigo((Integer) ((SelectItem) getListaSelectItemGradeCurricular().get(0)).getValue());
            }
            montarListaSelectItemPeriodoLetivo();
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarGradeCurricularPorCursoSituacaoAtiva() throws Exception {
        List lista = getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCursoLista(getMatriculaVO().getCurso().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoMatriculaPeriodo() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable matriculaPeriodoSituacaos = (Hashtable) Dominios.getMatriculaPeriodoSituacao();
        Enumeration keys = matriculaPeriodoSituacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) matriculaPeriodoSituacaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoDocumetacaoMatricula() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaoEntregaDocumentacaos = (Hashtable) Dominios.getSituacaoEntregaDocumentacao();
        Enumeration keys = situacaoEntregaDocumentacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoEntregaDocumentacaos.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>tipoDocumento</code>
     */
    public List getListaSelectItemTipoDocumentoDocumetacaoMatricula() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoDocumentoDocumentacaoMatriculas = (Hashtable) Dominios.getTipoDocumentoDocumentacaoMatricula();
        Enumeration keys = tipoDocumentoDocumentacaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoDocumentoDocumentacaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /*
     * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
     * <code>situacao</code>
     */
    public List getListaSelectItemSituacaoMatricula() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoMatricula();
        Enumeration keys = situacaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemSituacaoFinanceiraMatricula() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoFinanceiraMatricula();
        Enumeration keys = situacaoMatriculas.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoMatriculas.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>TipoMidiaCaptacao</code>. Buscando todos
     * os objetos correspondentes a entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemTipoMidiaCaptacao() {
        try {
            List resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia("");
            setListaSelectItemTipoMidiaCaptacao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nomeMidia"));
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeMidia</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
        List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turno</code>.
     */
    public void montarListaSelectItemTurno(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTurnoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CursoTurnoVO obj = (CursoTurnoVO) i.next();
                objs.add(new SelectItem(obj.getTurno().getCodigo(), obj.getTurno().getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemTurno(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
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

        List lista = new ArrayList(0);
        if ((getMatriculaVO() != null) && (getMatriculaVO().getCurso().getCodigo().intValue() != 0)) {
            return getMatriculaVO().getCurso().getCursoTurnoVOs();
        }
        return lista;

    }

    public void atualizarCurso() throws Exception {
        try {
            Integer curso = getMatriculaVO().getCurso().getCodigo();
            getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
            montarListaSelectItemTurno();
            if (getMatriculaVO().getDocumetacaoMatriculaVOs().size() == 0) {
                getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(this.getMatriculaVO(), getUsuarioLogado());
            }
        } catch (Exception e) {
        }
    }

    public void inicializarResultadoProcSeletivoInscricao() {
        try {
            ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVOBuscar = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(
                    getMatriculaVO().getInscricao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            this.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivoVOBuscar);
        } catch (Exception e) {
            this.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
        }
    }

    public void inicializarGradeCurricularMatriculaPeriodo() throws Exception {
        // PeriodoLetivoVO gradeCurricularVO = new
        // PeriodoLetivo().consultarPorCurso_PeriodoLetivo(getMatriculaVO().getCurso().getCodigo(),
        // getMatriculaPeriodoVO().getPeriodoLetivoMatricula().getCodigo());
        // if (gradeCurricularVO == null) {
        // throw new Exception("Não existe uma grade curricular cadastrada para esse curso (" +
        // getMatriculaVO().getCurso().getNome() + ") nesse período letivo (1° Período).");
        // } else {
        // this.getMatriculaPeriodoVO().setGradeCurricular(gradeCurricularVO);
        // }
    }

    public void inicializarMatriculaComDadosInscricao() throws Exception {
        getMatriculaVO().setUnidadeEnsino(getMatriculaVO().getInscricao().getUnidadeEnsino());
        if (getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 1) {
            getMatriculaVO().getInscricao().setCursoOpcao1(
                    getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getInscricao().getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            getMatriculaVO().setCurso(getMatriculaVO().getInscricao().getCursoOpcao1().getCurso());
            getMatriculaVO().setTurno(getMatriculaVO().getInscricao().getCursoOpcao1().getTurno());
        } else {
            if (getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 2) {
                getMatriculaVO().getInscricao().setCursoOpcao1(
                        getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getInscricao().getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                getMatriculaVO().setCurso(getMatriculaVO().getInscricao().getCursoOpcao2().getCurso());
                getMatriculaVO().setTurno(getMatriculaVO().getInscricao().getCursoOpcao2().getTurno());
            } else {
                if (getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 3) {
                    getMatriculaVO().getInscricao().setCursoOpcao1(
                            getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getInscricao().getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                    getMatriculaVO().setCurso(getMatriculaVO().getInscricao().getCursoOpcao3().getCurso());
                    getMatriculaVO().setTurno(getMatriculaVO().getInscricao().getCursoOpcao3().getTurno());
                }
            }
        }
        getMatriculaVO().setAluno(getMatriculaVO().getInscricao().getCandidato());
        getMatriculaPeriodoVO().setData(getMatriculaVO().getData());
        getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getMatriculaVO().getUsuario());
        getMatriculaPeriodoVO().setSituacao(getMatriculaVO().getSituacaoFinanceira());
        montarListaSelectItemGradeCurricular();
        if (getMatriculaVO().getDocumetacaoMatriculaVOs().size() == 0) {
            getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(getMatriculaVO(), getUsuarioLogado());
        }
    }

    public void inicializarDisciplinasPeriodoLetivo() throws Exception {
        try {
            Integer prm = getMatriculaPeriodoVO().getPeridoLetivo().getCodigo();
            List l = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(prm, false, getUsuarioLogado(), null);
            getMatriculaPeriodoVO().getPeridoLetivo().setGradeDisciplinaVOs(l);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void inicializarInscricaoEDadosRelacionados() {
        getMatriculaVO().setInscricao(new InscricaoVO());
        getMatriculaVO().setUnidadeEnsino(new UnidadeEnsinoVO());
        getMatriculaVO().setCurso(new CursoVO());
        getMatriculaVO().setTurno(new TurnoVO());
        getMatriculaVO().setAluno(new PessoaVO());
        setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Inscricao</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarInscricaoPorChavePrimaria() throws Exception {
        try {
            Integer campoConsulta = getMatriculaVO().getInscricao().getCodigo();
            InscricaoVO inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getMatriculaVO().setInscricao(inscricao);
            inicializarResultadoProcSeletivoInscricao();
            if (getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
                // Não encontrou nenhum resultado para a inscrição
                inicializarInscricaoEDadosRelacionados();
                this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
            } else {
                if (!getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
                    inicializarInscricaoEDadosRelacionados();
                    this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
                } else {
                    inicializarMatriculaComDadosInscricao();
                    this.setInscricao_Erro("");
                    setMensagemID("msg_dados_consultados");
                }
            }
        } catch (Exception e) {
            novo();
            setTurma_Erro("");
            setMensagemID("");
            setMensagemDetalhada("msg_erro", "");
            inicializarInscricaoEDadosRelacionados();
            this.setInscricao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarCursoPorChavePrimaria() {
        try {
            Integer campoConsulta = getMatriculaVO().getCurso().getCodigo();
            CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
            getMatriculaVO().setCurso(curso);
            getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(this.getMatriculaVO(), getUsuarioLogado());
            this.setCurso_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getMatriculaVO().setCurso(new CursoVO());
            this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public void liberarMatricula() {
        try {
            // setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(getMatriculaPeriodoVO().getUnidadeEnsinoCurso()));
            // if (getMatriculaForaPrazo()) {
            // return;
            // }
            getFacadeFactory().getMatriculaPeriodoFacade().liberarPagamentoMatricula(getMatriculaVO(), getMatriculaPeriodoVO(), getProcessoCalendarioMatriculaVO(), getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            setMensagemID("msg_matricula_liberarMatricula");
        } catch (Exception e) {
            setMatriculaForaPrazo(false);
            getMatriculaPeriodoVO().setResponsavelLiberacaoMatricula(new UsuarioVO());
            getMatriculaPeriodoVO().setDataLiberacaoMatricula(null);
            getMatriculaPeriodoVO().setSituacao("PF");
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void emitirBoletoMatricula() {
        try {
            if (getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoReferenteMatricula().getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA)) {
                getFacadeFactory().getMatriculaPeriodoFacade().emitirBoletoMatricula(getMatriculaPeriodoVO(), getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()));
                imprimirBoleto();
                setImprimir(true);
                setMensagemID("msg_inscricao_emitirBoletoPagamento");
            } else {
                if (getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoReferenteMatricula().getSituacao().equals(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA_EPAGA)) {
                    setMensagemID("msg_matricula_matriculaJaQuitadaFinanceiramente");
                    setImprimir(false);
                } else {
                    setMensagemDetalhada("msg_erro", "Conta a receber referente a matrícula ainda não foi gerada!");
                    setImprimir(false);
                }
            }
        } catch (Exception e) {
            setMatriculaForaPrazo(false);
            getMatriculaPeriodoVO().setNrDocumento("");
            //getMatriculaPeriodoVO().setContaReceber(0);
            getMatriculaPeriodoVO().setDataEmissaoBoletoMatricula(null);
            getMatriculaPeriodoVO().setResponsavelEmissaoBoletoMatricula(new UsuarioVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void imprimirContrato(String tipoContrato) throws Exception {
        Matricula.montarDadosUnidadeEnsino(getMatriculaVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
        PlanoFinanceiroCursoVO planoFin = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), "", Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        PlanoFinanceiroAlunoVO plano = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        String ano = Constantes.EMPTY;
        String semestre = Constantes.EMPTY;
        if (!getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()){
            ano = getMatriculaPeriodoVO().getAno();
            semestre = getMatriculaPeriodoVO().getSemestre();
        }
        getMatriculaPeriodoVO().setDataInicioAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarPrimeiraDataAulaPorTurmaAgrupada(getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
        getMatriculaPeriodoVO().setDataFinalAula(getFacadeFactory().getHorarioTurmaDiaFacade().consultarUltimaDataAulaPorTurmaAgrupada(getMatriculaPeriodoVO().getTurma().getCodigo(), ano, semestre));
        DadosComerciaisVO dc = getFacadeFactory().getDadosComerciaisFacade().consultarEmpregoAtualPorCodigoPessoa(getMatriculaVO().getAluno().getCodigo(), getUsuarioLogado());
        String contratoPronto = Constantes.EMPTY;
        if (tipoContrato.equals("MA")) {
            contratoPronto = planoFin.getTextoPadraoContratoMatricula().substituirTagsTextoPadraoContratoMatricula(null, getMatriculaVO(),  getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), getMatriculaPeriodoVO(), plano, new ArrayList<PlanoDescontoVO>(), dc, getUsuarioLogado());
        }
        if (tipoContrato.equals("FI")) {
            contratoPronto =  planoFin.getTextoPadraoContratoFiador().substituirTagsTextoPadraoContratoFiador(getMatriculaVO(),  getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoVOs(), getMatriculaPeriodoVO(), plano, new ArrayList<PlanoDescontoVO>(), dc, getUsuarioLogado());
        }
        HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
        request.getSession().setAttribute("textoRelatorio", contratoPronto);
    }

    public void emitirContratoMatricula() {
        try {
            if (getMatriculaPeriodoVO().getContratoMatricula().getCodigo().equals(0)) {
                setMensagemID("msg_matricula_semcontratodefinido");
                setImprimirContrato(false);
                return;
            } else {
                setImprimirContrato(true);
                imprimirContrato("MA");
            }
        } catch (Exception e) {
            setImprimirContrato(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void emitirContratoFiador() {
        try {
            if (getMatriculaPeriodoVO().getContratoFiador().getCodigo().equals(0)) {
                setMensagemID("msg_matricula_semcontratodefinido");
                setImprimirContrato(false);
                return;
            } else {
                setImprimirContrato(true);
                imprimirContrato("FI");
            }
        } catch (Exception e) {
            setImprimirContrato(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getContrato() {
        if (getImprimirContrato()) {
            return "abrirPopup('faces/VisualizarContrato', 'RelatorioContrato', 730, 545)";
        }
        return "";
    }

    public String getBoleto() {
        if (getImprimir()) {
            return "abrirPopup('BoletoBancarioSV?codigoContaReceber=" + getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo()
                    + "&titulo=matricula', 'boletoMatricula', 780, 585)";
        }
        return "";
    }

    public void imprimirBoleto() throws Exception {
        BoletoBancarioSV ser = new BoletoBancarioSV();
        throw new Exception("Error por favor informe o administrador");
        //ser.setCodigoContaReceber(getMatriculaPeriodoVO().getMatriculaPeriodoVencimentoReferenteMatricula().getContaReceber().getCodigo());
    }

    public void montarListaSelectItemDescontoProgressivo(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemDescontoProgresivo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemDescontoProgressivo() {
        try {
            montarListaSelectItemDescontoProgressivo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>DescontoAlunoMatricula</code>.
     */
    public void montarListaSelectItemDescontoAlunoMatricula() {
        setListaSelectItemDescontoAlunoMatricula(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, false));
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>DescontoAlunoParcela</code>.
     */
    public void montarListaSelectItemDescontoAlunoParcela() {
        setListaSelectItemDescontoAlunoParcela(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, false));
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void emitirDadosMatricula() {
        try {
            // imprimirMatricula();
            setExibirMatricula(true);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    // public void imprimirMatricula() throws Exception {
    // DadosMatriculaSV mat = new DadosMatriculaSV();
    // mat.setMatricula(getMatriculaVO().getMatricula());
    // }
    public String getRelatorioMatricula() {
        if (isExibirMatricula()) {
            return "abrirPopup('DadosMatriculaSV?matricula=" + getMatriculaVO().getMatricula() + "&titulo=matricula', 'dadosMatricula', 780, 585)";
        }
        return "";
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemTipoMidiaCaptacao();
        montarListaSelectItemGradeCurricular();
        montarListaSelectItemTurma();
        montarListaSelectItemConvenio();
        montarListaSelectItemPlanoDesconto();
        setListaSelectItemDescontoProgresivo(new ArrayList(0));
        montarListaOrdemDesconto();
        montarListaSelectItemDescontoAlunoMatricula();
        montarListaSelectItemDescontoAlunoParcela();
        montarListaSelectItemPlanoFinanceiroCurso();
        montarListaSelectItemPeriodoLetivoAdicionar();
        montarListaSelectItemDisciplina();
        montarListaProcessoMatricula();
        setListaSelectItemTurmaAdicionar(new ArrayList(0));
        setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(new ArrayList(0));
        setListaSelectItemDisciplinaEquivalenteAdicionar(new ArrayList(0));
    }

    public List consultarProcessoMatriculaPorUnidadeEnsino(Integer codigoUnidade) throws Exception {
        List lista = getFacadeFactory().getProcessoMatriculaFacade().consultarPorNomeUnidadeEnsino("", codigoUnidade, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaProcessoMatricula() {
        try {
            List objs = new ArrayList(0);
            if (this.getMatriculaVO().getUnidadeEnsino().getCodigo() == null || this.getMatriculaVO().getUnidadeEnsino().getCodigo() == 0) {
                setListaSelectItemProcessoMatricula(objs);
                return;
            }
            List resultadoConsulta = consultarProcessoMatriculaPorUnidadeEnsino(this.getMatriculaVO().getUnidadeEnsino().getCodigo());
            Iterator i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcessoMatriculaVO obj = (ProcessoMatriculaVO) i.next();
                if (obj.ativoParaMatriculaEPreMatricula(new Date())) {
                    objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                }
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemProcessoMatricula(objs);
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    //
    // public void montarDadosDisciplinaSelecionadaMatriculaPeriodoTurmaDisciplina() {
    // }
    public void montarDisciplinasEquivalentesETurmasDisciplinaAdicionar() {
        montarListaSelectItemTurmaAdicionar();
        montarListaSelectItemDisciplinaEquivalenteAdicionar();
    }

    public void montarListaSelectItemDisciplinaEquivalenteAdicionar() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getPeriodoLetivoAdicionar() == null || getPeriodoLetivoAdicionar().intValue() == 0 || getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo() == 0) {
                setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarDisciplinasEquivalentesPorDisciplina();
            List objs = new ArrayList(0);
            i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
            }
            setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(objs);
        } catch (Exception e) {
            setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemTurmaAdicionar() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getPeriodoLetivoAdicionar() == null || getPeriodoLetivoAdicionar().intValue() == 0 || getGradeDisciplinaAdicionar() == 0) {
                setListaSelectItemTurmaAdicionar(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarPorDisciplinaSituacaoNrVagas();
            List objs = new ArrayList(0);
            i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TurmaVO obj = (TurmaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma() + " - Vagas Disponíveis: "
                        + obj.getNrVagasDisponiveis(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getCodigo())));
            }
            if (objs.isEmpty()) {
                throw new Exception("Não existe turma cadastrada para o curso: " + getMatriculaVO().getCurso().getNome().toUpperCase() + " no período: "
                        + getMatriculaVO().getTurno().getNome().toUpperCase());
            }
            setListaSelectItemTurmaAdicionar(objs);
        } catch (Exception e) {
            setListaSelectItemTurmaAdicionar(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public Boolean getApresentarDisciplinasIncluir() {
        if (!getListaSelectItemDisciplinaAdicionar().isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarDisciplinasEquivalentesIncluir() {
        if (!getListaSelectItemDisciplinaEquivalenteAdicionar().isEmpty()) {
            return true;
        }
        return false;
    }

    public void montarListaSelectItemDisciplina() {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            setListaSelectItemTurmaAdicionar(new ArrayList(0));
            if (getPeriodoLetivoAdicionar().intValue() == 0) {
                setListaSelectItemDisciplinaAdicionar(new ArrayList(0));
                return;
            }
            resultadoConsulta = consultarDisciplinaPorPeriodoLetivo();
            List objs = new ArrayList(0);
            i = resultadoConsulta.iterator();
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getNome()));
            }
            setListaSelectItemDisciplinaAdicionar(objs);
        } catch (Exception e) {
            setListaSelectItemDisciplinaAdicionar(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }

    }

    // montarListaSelectItemPeriodoLetivo
    public void montarListaSelectItemPeriodoLetivoAdicionar() {
        try {
            if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() == 0) {
                setListaSelectItemPeriodoLetivoAdicionar(new ArrayList(0));
                return;
            }
            List resultadoConsulta = consultarPeriodoLetivoPorGradeCurricular(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
            Iterator i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemPeriodoLetivoAdicionar(objs);
            setPeriodoLetivoAdicionar(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo());
        } catch (Exception e) {
            setListaSelectItemPeriodoLetivoAdicionar(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List consultarPorDisciplinaSituacaoNrVagas() throws Exception {
        DisciplinaVO obj = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoGradeDisciplina(gradeDisciplinaAdicionar, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplina(obj);
        List listaResultado = getFacadeFactory().getTurmaFacade().consultarPorDisciplinaSituacaoNrVagasIncluindoTurmasLotadas(obj.getCodigo(), "AB", getMatriculaVO().getUnidadeEnsino().getCodigo(),
                false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return listaResultado;
    }

    public List consultarTurmaPorDisciplinaEquivalenteSituacao() throws Exception {
        DisciplinaVO obj = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoGradeDisciplina(gradeDisciplinaEquivalenteAdicionar, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().setDisciplinaEquivalente(obj);
        List listaResultado = getFacadeFactory().getTurmaFacade().consultarPorDisciplinaEquivalenteSituacaoUnidadeEnsinoCurso(obj.getCodigo(), "AB", getMatriculaVO().getUnidadeEnsino().getCodigo(),
                false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return listaResultado;
    }

    public List consultarDisciplinaPorPeriodoLetivo() throws Exception {
        List listaResultado = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(getPeriodoLetivoAdicionar(), false, getUsuarioLogado(), null);
        return listaResultado;
    }

    public List consultarDisciplinasEquivalentesPorDisciplina() throws Exception {
        List listaResultado = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinasEquivalentes(this.getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplinaEquivalente().getCodigo(), false, getUsuarioLogado());
        return listaResultado;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = getMatriculaVO().getAluno().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getMatriculaVO().setAluno(pessoa);
            this.setAluno_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getMatriculaVO().getAluno().setCPF("");
            getMatriculaVO().getAluno().setNome("");
            getMatriculaVO().getAluno().setCodigo(0);
            this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public void consultarAlunoPorCPF() {
        try {
            String campoConsulta = getMatriculaVO().getAluno().getCPF();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (pessoa != null) {
                getMatriculaVO().setAluno(pessoa);
            }
            this.setAluno_Erro("");
            if (getMatriculaVO().getAluno().getNome().equals("")) {
                setValidarCadastrarAluno(true);
            } else {
                setValidarCadastrarAluno(false);
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getMatriculaVO().getAluno().setCPF("");
            getMatriculaVO().getAluno().setNome("");
            getMatriculaVO().getAluno().setCodigo(0);
            this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarUsuarioMatriculaPorChavePrimaria() {
        try {
            Integer campoConsulta = getMatriculaVO().getUsuario().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getMatriculaVO().getUsuario().setNome(pessoa.getNome());
            setUsuario_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getMatriculaVO().getUsuario().setNome("");
            getMatriculaVO().getUsuario().setCodigo(0);
            setUsuario_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarResponsavelRenovacaoMatriculaPorChavePrimaria() {
        try {
            Integer campoConsulta = getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setNome(pessoa.getNome());
            setResponsavelRenovacaoMatricula_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setNome("");
            getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().setCodigo(0);
            setResponsavelRenovacaoMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    public Boolean getApresentarComboBoxSituacao() {

        if (getControleConsulta().getCampoConsulta().equals("situacao")) {
            return true;
        }
        return false;
    }

    public Boolean getSituacaoFinanceira() {

        if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
            return true;
        }
        return false;
    }

    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("data")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
        }
        if (getControleConsulta().getCampoConsulta().equals("cpf")) {
            return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event);";
        }
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);        
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("cpf", "CPF"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("data", "Data"));
//        itens.add(new SelectItem("situacao", "Situação"));
//        itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        // setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        // definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public List getListaSelectItemDescontoProgresivo() {
        if (listaSelectItemDescontoProgresivo == null) {
            listaSelectItemDescontoProgresivo = new ArrayList(0);
        }
        return listaSelectItemDescontoProgresivo;
    }

    public void setListaSelectItemDescontoProgresivo(List listaSelectItemDescontoProgresivo) {
        this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
    }

    public ItemPlanoFinanceiroAlunoVO getItemPlanoFinanceiroAlunoVO() {
        if (itemPlanoFinanceiroAlunoVO == null) {
            itemPlanoFinanceiroAlunoVO = new ItemPlanoFinanceiroAlunoVO();
        }
        return itemPlanoFinanceiroAlunoVO;
    }

    public void setItemPlanoFinanceiroAlunoVO(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO) {
        this.itemPlanoFinanceiroAlunoVO = itemPlanoFinanceiroAlunoVO;
    }

    public List getListaSelectItemConvenio() {
        if (listaSelectItemConvenio == null) {
            listaSelectItemConvenio = new ArrayList(0);
        }
        return listaSelectItemConvenio;
    }

    public void setListaSelectItemConvenio(List listaSelectItemConvenio) {
        this.listaSelectItemConvenio = listaSelectItemConvenio;
    }

    public List getListaSelectItemPlanoDesconto() {
        if (listaSelectItemPlanoDesconto == null) {
            listaSelectItemPlanoDesconto = new ArrayList(0);
        }
        return listaSelectItemPlanoDesconto;
    }

    public void setListaSelectItemPlanoDesconto(List listaSelectItemPlanoDesconto) {
        this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
    }

    public PlanoFinanceiroAlunoVO getPlanoFinanceiroAlunoVO() {
        if (planoFinanceiroAlunoVO == null) {
            planoFinanceiroAlunoVO = new PlanoFinanceiroAlunoVO();
        }
        return planoFinanceiroAlunoVO;
    }

    public void setPlanoFinanceiroAlunoVO(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO) {
        this.planoFinanceiroAlunoVO = planoFinanceiroAlunoVO;
    }

    public List getListaSelectItemPeriodoLetivoMatricula() {
        if (listaSelectItemPeriodoLetivoMatricula == null) {
            listaSelectItemPeriodoLetivoMatricula = new ArrayList(0);
        }
        return (listaSelectItemPeriodoLetivoMatricula);
    }

    public void setListaSelectItemPeriodoLetivoMatricula(List listaSelectItemPeriodoLetivoMatricula) {
        this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
    }

    public String getResponsavelRenovacaoMatricula_Erro() {
        return responsavelRenovacaoMatricula_Erro;
    }

    public void setResponsavelRenovacaoMatricula_Erro(String responsavelRenovacaoMatricula_Erro) {
        this.responsavelRenovacaoMatricula_Erro = responsavelRenovacaoMatricula_Erro;
    }

    public List getListaSelectItemGradeCurricular() {
        if (listaSelectItemGradeCurricular == null) {
            listaSelectItemGradeCurricular = new ArrayList(0);
        }
        return (listaSelectItemGradeCurricular);
    }

    public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
        this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if (matriculaPeriodoVO == null) {
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

    public DocumetacaoMatriculaVO getDocumetacaoMatriculaVO() {
        if (documetacaoMatriculaVO == null) {
            documetacaoMatriculaVO = new DocumetacaoMatriculaVO();
        }
        return documetacaoMatriculaVO;
    }

    public void setDocumetacaoMatriculaVO(DocumetacaoMatriculaVO documetacaoMatriculaVO) {
        this.documetacaoMatriculaVO = documetacaoMatriculaVO;
    }

    public List getListaSelectItemTipoMidiaCaptacao() {
        if (listaSelectItemTipoMidiaCaptacao == null) {
            listaSelectItemTipoMidiaCaptacao = new ArrayList(0);
        }
        return (listaSelectItemTipoMidiaCaptacao);
    }

    public void setListaSelectItemTipoMidiaCaptacao(List listaSelectItemTipoMidiaCaptacao) {
        this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
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

    public String getUsuario_Erro() {
        return usuario_Erro;
    }

    public void setUsuario_Erro(String usuario_Erro) {
        this.usuario_Erro = usuario_Erro;
    }

    public String getInscricao_Erro() {
        return inscricao_Erro;
    }

    public void setInscricao_Erro(String inscricao_Erro) {
        this.inscricao_Erro = inscricao_Erro;
    }

    public String getCurso_Erro() {
        return curso_Erro;
    }

    public void setCurso_Erro(String curso_Erro) {
        this.curso_Erro = curso_Erro;
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

    public String getAluno_Erro() {
        return aluno_Erro;
    }

    public void setAluno_Erro(String aluno_Erro) {
        this.aluno_Erro = aluno_Erro;
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    @Override
    public String getSenha() {
        return senha;
    }

    @Override
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
        if (resultadoProcessoSeletivoVO == null) {
            resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
        }
        return resultadoProcessoSeletivoVO;
    }

    public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
        this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
    }

    public String getTurma_Erro() {
        return turma_Erro;
    }

    public void setTurma_Erro(String turma_Erro) {
        this.turma_Erro = turma_Erro;
    }

    public List getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    /**
     * Método responsável por consultar dados da entidade
     * <code><code> e montar o atributo <code>identificadorTurma</code> Este atributo é uma lista (<code>List</code>)
     * utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTurmaPorIdentificadorTurma() throws Exception {
        List listaResultado = getFacadeFactory().getTurmaFacade().consultarPorPeriodoLetivoUnidadeEnsinoCursoTurno(getMatriculaPeriodoVO().getPeridoLetivo().getCodigo(),
                getMatriculaVO().getUnidadeEnsino().getCodigo(), getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return listaResultado;
    }

    /*
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>Turma</code>.
     */
    public void montarListaSelectItemTurma(String prm) throws Exception {
        List objs = new ArrayList(0);
        if ((getMatriculaVO().getCurso() == null) || (getMatriculaVO().getCurso().getCodigo().intValue() == 0)) {

            setListaSelectItemTurma(objs);
            return;
        }
        if ((getMatriculaVO().getTurno() == null) || (getMatriculaVO().getTurno().getCodigo().intValue() == 0)) {

            setListaSelectItemTurma(objs);
            return;
        }
        if ((getMatriculaPeriodoVO().getPeridoLetivo() == null) || (getMatriculaPeriodoVO().getPeridoLetivo().getCodigo().intValue() == 0)) {

            setListaSelectItemTurma(objs);
            return;
        }

        List resultadoConsulta = consultarTurmaPorIdentificadorTurma();
        setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
        if (getListaSelectItemTurma().isEmpty()) {
            this.setTurma_Erro("Não existe turma cadastrada para o curso: " + getMatriculaVO().getCurso().getNome().toUpperCase() + " no período: "
                    + getMatriculaVO().getTurno().getNome().toUpperCase());
        } else {
            this.setTurma_Erro("");
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turma</code>. Buscando todos os objetos
     * correspondentes a entidade <code>Turma</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTurma() {
        try {
            montarListaSelectItemTurma("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List getListaDisciplinasGradeCurricular() {
        if (listaGradeDisciplinas == null) {
            listaGradeDisciplinas = new ArrayList(0);
        }
        return listaGradeDisciplinas;
    }

    public String getCampoConsultaCurso() {
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public String getValorConsultaCurso() {
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public void setListaDisciplinasGradeCurricular(List listaGradeCurricularDisciplinas) {
        this.listaGradeDisciplinas = listaGradeCurricularDisciplinas;
    }

    public String getResponsavelLiberacaoMatricula_Erro() {
        return responsavelLiberacaoMatricula_Erro;
    }

    public Integer getCodigoUnidadeEnsinoCurso() {
        return codigoUnidadeEnsinoCurso;
    }

    public void setCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) {
        this.codigoUnidadeEnsinoCurso = codigoUnidadeEnsinoCurso;
    }

    public void setResponsavelLiberacaoMatricula_Erro(String responsavelLiberacaoMatricula_Erro) {
        this.responsavelLiberacaoMatricula_Erro = responsavelLiberacaoMatricula_Erro;
    }

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public ProcessoMatriculaCalendarioVO getProcessoCalendarioMatriculaVO() {
        return processoCalendarioMatriculaVO;
    }

    public void setProcessoCalendarioMatriculaVO(ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO) {
        this.processoCalendarioMatriculaVO = processoCalendarioMatriculaVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        matriculaVO = null;
        aluno_Erro = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        curso_Erro = null;
        inscricao_Erro = null;
        responsavelLiberacaoMatricula_Erro = null;
        usuario_Erro = null;
        Uteis.liberarListaMemoria(listaSelectItemTurno);
        Uteis.liberarListaMemoria(listaSelectItemTipoMidiaCaptacao);
        Uteis.liberarListaMemoria(listaSelectItemTurma);
        Uteis.liberarListaMemoria(listaSelectItemCurso);
        Uteis.liberarListaMemoria(listaGradeDisciplinas);

        documetacaoMatriculaVO = null;
        matriculaPeriodoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemGradeCurricular);
        responsavelRenovacaoMatricula_Erro = null;
        Uteis.liberarListaMemoria(listaSelectItemPeriodoLetivoMatricula);
        turma_Erro = null;
        resultadoProcessoSeletivoVO = null;

    }

    public List getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public Boolean getTurmaComVagasPreenchidas() {
        return turmaComVagasPreenchidas;
    }

    public void setTurmaComVagasPreenchidas(Boolean turmaComVagasPreenchidas) {
        this.turmaComVagasPreenchidas = turmaComVagasPreenchidas;
    }

    public Boolean getTurmaComLotacaoPreenchida() {
        return turmaComLotacaoPreenchida;
    }

    public void setTurmaComLotacaoPreenchida(Boolean turmaComLotacaoPreenchida) {
        this.turmaComLotacaoPreenchida = turmaComLotacaoPreenchida;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public Boolean getMatriculaForaPrazo() {
        return matriculaForaPrazo;
    }

    public void setMatriculaForaPrazo(Boolean matriculaForaPrazo) {
        this.matriculaForaPrazo = matriculaForaPrazo;
    }

    /**
     * @return the validarCadastrarAluno
     */
    public boolean isValidarCadastrarAluno() {
        return validarCadastrarAluno;
    }

    /**
     * @param validarCadastrarAluno
     *            the validarCadastrarAluno to set
     */
    public void setValidarCadastrarAluno(boolean validarCadastrarAluno) {
        this.validarCadastrarAluno = validarCadastrarAluno;
    }

    /**
     * @return the planoFinanceiroCursoVO
     */
    public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
        return planoFinanceiroCursoVO;
    }

    /**
     * @param planoFinanceiroCursoVO
     *            the planoFinanceiroCursoVO to set
     */
    public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
        this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
    }

    /**
     * @return the listaConsultaConvenio
     */
    public List getListaConsultaConvenio() {
        return listaConsultaConvenio;
    }

    /**
     * @param listaConsultaConvenio
     *            the listaConsultaConvenio to set
     */
    public void setListaConsultaConvenio(List listaConsultaConvenio) {
        this.listaConsultaConvenio = listaConsultaConvenio;
    }

    /**
     * @return the listaConsultaPlanoDesconto
     */
    public List getListaConsultaPlanoDesconto() {
        return listaConsultaPlanoDesconto;
    }

    /**
     * @param listaConsultaPlanoDesconto
     *            the listaConsultaPlanoDesconto to set
     */
    public void setListaConsultaPlanoDesconto(List listaConsultaPlanoDesconto) {
        this.listaConsultaPlanoDesconto = listaConsultaPlanoDesconto;
    }

    /**
     * @return the liberarAvancar
     */
    public boolean isLiberarAvancar() {
        return liberarAvancar;
    }

    /**
     * @param liberarAvancar
     *            the liberarAvancar to set
     */
    public void setLiberarAvancar(boolean liberarAvancar) {
        this.liberarAvancar = liberarAvancar;
    }

    /**
     * @return the pedirLiberacaoMatricula
     */
    public boolean isPedirLiberacaoMatricula() {
        return pedirLiberacaoMatricula;
    }

    /**
     * @param pedirLiberacaoMatricula
     *            the pedirLiberacaoMatricula to set
     */
    public void setPedirLiberacaoMatricula(boolean pedirLiberacaoMatricula) {
        this.pedirLiberacaoMatricula = pedirLiberacaoMatricula;
    }

    /**
     * @return the mapaLancamentoFututroVO
     */
    public MapaLancamentoFuturoVO getMapaLancamentoFuturoVO() {
        return mapaLancamentoFuturoVO;
    }

    /**
     * @param mapaLancamentoFututroVO
     *            the mapaLancamentoFututroVO to set
     */
    public void setMapaLancamentoFuturoVO(MapaLancamentoFuturoVO mapaLancamentoFuturoVO) {
        this.mapaLancamentoFuturoVO = mapaLancamentoFuturoVO;
    }

    /**
     * @return the ordemDesconto
     */
    public List<OrdemDescontoVO> getOrdemDesconto() {
        return ordemDesconto;
    }

    /**
     * @param ordemDesconto
     *            the ordemDesconto to set
     */
    public void setOrdemDesconto(List ordemDesconto) {
        this.ordemDesconto = ordemDesconto;
    }

    /**
     * @return the ordemDescontoVO
     */
    public OrdemDescontoVO getOrdemDescontoVO() {
        return ordemDescontoVO;
    }

    /**
     * @param ordemDescontoVO
     *            the ordemDescontoVO to set
     */
    public void setOrdemDescontoVO(OrdemDescontoVO ordemDescontoVO) {
        this.ordemDescontoVO = ordemDescontoVO;
    }

    /**
     * @return the listaSelectItemDescontoAlunoMatricula
     */
    public List getListaSelectItemDescontoAlunoMatricula() {
        return listaSelectItemDescontoAlunoMatricula;
    }

    /**
     * @param listaSelectItemDescontoAlunoMatricula
     *            the listaSelectItemDescontoAlunoMatricula to set
     */
    public void setListaSelectItemDescontoAlunoMatricula(List listaSelectItemDescontoAlunoMatricula) {
        this.listaSelectItemDescontoAlunoMatricula = listaSelectItemDescontoAlunoMatricula;
    }

    /**
     * @return the listaSelectItemDescontoAlunoParcela
     */
    public List getListaSelectItemDescontoAlunoParcela() {
        return listaSelectItemDescontoAlunoParcela;
    }

    /**
     * @param listaSelectItemDescontoAlunoParcela
     *            the listaSelectItemDescontoAlunoParcela to set
     */
    public void setListaSelectItemDescontoAlunoParcela(List listaSelectItemDescontoAlunoParcela) {
        this.listaSelectItemDescontoAlunoParcela = listaSelectItemDescontoAlunoParcela;
    }

    /**
     * @return the exibirMatricula
     */
    public boolean isExibirMatricula() {
        return exibirMatricula;
    }

    /**
     * @param exibirMatricula
     *            the exibirMatricula to set
     */
    public void setExibirMatricula(boolean exibirMatricula) {
        this.exibirMatricula = exibirMatricula;
    }

    /**
     * @return the imprimirContrato
     */
    public Boolean getImprimirContrato() {
        return imprimirContrato;
    }

    /**
     * @param imprimirContrato
     *            the imprimirContrato to set
     */
    public void setImprimirContrato(Boolean imprimirContrato) {
        this.imprimirContrato = imprimirContrato;
    }

    /**
     * @return the condicaoPagamentoPlanoFinanceiroCursoVO
     */
    public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVO() {
        if (condicaoPagamentoPlanoFinanceiroCursoVO == null) {
            condicaoPagamentoPlanoFinanceiroCursoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        }
        return condicaoPagamentoPlanoFinanceiroCursoVO;
    }

    /**
     * @param condicaoPagamentoPlanoFinanceiroCursoVO
     *            the condicaoPagamentoPlanoFinanceiroCursoVO to set
     */
    public void setCondicaoPagamentoPlanoFinanceiroCursoVO(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) {
        this.condicaoPagamentoPlanoFinanceiroCursoVO = condicaoPagamentoPlanoFinanceiroCursoVO;
    }

    public List consultarPlanoFinanceiroCurso(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorDescricao(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemPlanoFinanceiroCurso(String prm) throws Exception {
        List objs = new ArrayList(0);
        if ((this.getMatriculaVO().getUnidadeEnsino().getCodigo().equals(0)) || (this.getMatriculaVO().getCurso().getCodigo().equals(0)) || (this.getMatriculaVO().getTurno().getCodigo().equals(0))
                || (this.getMatriculaPeriodoVO().getTurma().getCodigo().equals(0))) {
            this.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(new PlanoFinanceiroCursoVO());
            setListaSelectItemPlanoFinanceiroCurso(objs);
            return;
        }
        try {
            getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            this.getMatriculaPeriodoVO().setPlanoFinanceiroCurso(new PlanoFinanceiroCursoVO());
            setListaSelectItemPlanoFinanceiroCurso(objs);
        }

        List resultadoConsulta = this.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCondicaoPagamentoPlanoFinanceiroCursoVOs(); // consultarPlanoFinanceiroCurso(prm);
        Iterator i = resultadoConsulta.iterator();
        String planoFinanceiroDesc = this.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getDescricao();
        if (!planoFinanceiroDesc.equals("")) {
            planoFinanceiroDesc = planoFinanceiroDesc + " - ";
        }
        // objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), planoFinanceiroDesc + obj.getDescricao()));
            apresentarPlanoFinanceiroCurso = true;
        }

        setListaSelectItemPlanoFinanceiroCurso(objs);
    }

    public void processarDadosPermitinentesTurmaSelecionada() {
        atualizarSituacaoMatriculaPeriodo();
        inicializarListaSelectItemPlanoFinanceiroCursoParaTurma();
    }

    public void inicializarListaSelectItemPlanoFinanceiroCursoParaTurma() {
        if ((this.getMatriculaPeriodoVO().getTurma() != null) && (!this.getMatriculaPeriodoVO().getTurma().getCodigo().equals(0))) {
            apresentarPlanoFinanceiroCurso = true;
        }
        montarListaSelectItemPlanoFinanceiroCurso();
    }

    public Boolean getApresentarPlanoFinanceiroCurso() {
        return apresentarPlanoFinanceiroCurso;
        //
        // if ((this.getMatriculaPeriodoVO().getTurma() == null) ||
        // (this.getMatriculaPeriodoVO().getTurma().getCodigo().equals(0))) {
        // return false;
        // }
        // return true;
    }

    public void montarListaSelectItemPlanoFinanceiroCurso() {
        try {
            montarListaSelectItemPlanoFinanceiroCurso("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * @return the listaSelectItemPlanoFinanceiroCurso
     */
    public List getListaSelectItemPlanoFinanceiroCurso() {
        return listaSelectItemPlanoFinanceiroCurso;
    }

    /**
     * @param listaSelectItemPlanoFinanceiroCurso
     *            the listaSelectItemPlanoFinanceiroCurso to set
     */
    public void setListaSelectItemPlanoFinanceiroCurso(List listaSelectItemPlanoFinanceiroCurso) {
        this.listaSelectItemPlanoFinanceiroCurso = listaSelectItemPlanoFinanceiroCurso;
    }

    /**
     * @return the matriculaPeriodoTurmaDisciplinaVOAdicionar
     */
    public MatriculaPeriodoTurmaDisciplinaVO getMatriculaPeriodoTurmaDisciplinaVOAdicionar() {
        if (matriculaPeriodoTurmaDisciplinaVOAdicionar == null) {
            matriculaPeriodoTurmaDisciplinaVOAdicionar = new MatriculaPeriodoTurmaDisciplinaVO();
        }
        return matriculaPeriodoTurmaDisciplinaVOAdicionar;
    }

    /**
     * @param matriculaPeriodoTurmaDisciplinaVOAdicionar
     *            the matriculaPeriodoTurmaDisciplinaVOAdicionar to set
     */
    public void setMatriculaPeriodoTurmaDisciplinaVOAdicionar(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVOAdicionar) {
        this.matriculaPeriodoTurmaDisciplinaVOAdicionar = matriculaPeriodoTurmaDisciplinaVOAdicionar;
    }

    /**
     * @return the periodoLetivoAdicionar
     */
    public Integer getPeriodoLetivoAdicionar() {
        if (periodoLetivoAdicionar == null) {
            periodoLetivoAdicionar = 0;
        }
        return periodoLetivoAdicionar;
    }

    /**
     * @param periodoLetivoAdicionar
     *            the periodoLetivoAdicionar to set
     */
    public void setPeriodoLetivoAdicionar(Integer periodoLetivoAdicionar) {
        this.periodoLetivoAdicionar = periodoLetivoAdicionar;
    }

    /**
     * @return the listaSelectItemPeriodoLetivoAdicionar
     */
    public List getListaSelectItemPeriodoLetivoAdicionar() {
        return listaSelectItemPeriodoLetivoAdicionar;
    }

    /**
     * @param listaSelectItemPeriodoLetivoAdicionar
     *            the listaSelectItemPeriodoLetivoAdicionar to set
     */
    public void setListaSelectItemPeriodoLetivoAdicionar(List listaSelectItemPeriodoLetivoAdicionar) {
        this.listaSelectItemPeriodoLetivoAdicionar = listaSelectItemPeriodoLetivoAdicionar;
    }

    /**
     * @return the listaSelectItemTurmaAdicionar
     */
    public List getListaSelectItemTurmaAdicionar() {
        return listaSelectItemTurmaAdicionar;
    }

    /**
     * @param listaSelectItemTurmaAdicionar
     *            the listaSelectItemTurmaAdicionar to set
     */
    public void setListaSelectItemTurmaAdicionar(List listaSelectItemTurmaAdicionar) {
        this.listaSelectItemTurmaAdicionar = listaSelectItemTurmaAdicionar;
    }

    /**
     * @return the listaSelectItemDisciplinaAdicionar
     */
    public List getListaSelectItemDisciplinaAdicionar() {
        if (listaSelectItemDisciplinaAdicionar == null) {
            listaSelectItemDisciplinaAdicionar = new ArrayList(0);
        }
        return listaSelectItemDisciplinaAdicionar;
    }

    /**
     * @param listaSelectItemDisciplinaAdicionar
     *            the listaSelectItemDisciplinaAdicionar to set
     */
    public void setListaSelectItemDisciplinaAdicionar(List listaSelectItemDisciplinaAdicionar) {
        this.listaSelectItemDisciplinaAdicionar = listaSelectItemDisciplinaAdicionar;
    }

    /**
     * @return the listaSelectItemDisciplinaEquivalenteAdicionar
     */
    public List getListaSelectItemDisciplinaEquivalenteAdicionar() {
        if (listaSelectItemDisciplinaEquivalenteAdicionar == null) {
            listaSelectItemDisciplinaEquivalenteAdicionar = new ArrayList(0);
        }
        return listaSelectItemDisciplinaEquivalenteAdicionar;
    }

    /**
     * @param listaSelectItemDisciplinaEquivalenteAdicionar
     *            the listaSelectItemDisciplinaEquivalenteAdicionar to set
     */
    public void setListaSelectItemDisciplinaEquivalenteAdicionar(List listaSelectItemDisciplinaEquivalenteAdicionar) {
        this.listaSelectItemDisciplinaEquivalenteAdicionar = listaSelectItemDisciplinaEquivalenteAdicionar;
    }

    /**
     * @return the listaSelectItemTurmaDisciplinaEquivalenteAdicionar
     */
    public List getListaSelectItemTurmaDisciplinaEquivalenteAdicionar() {
        if (listaSelectItemTurmaDisciplinaEquivalenteAdicionar == null) {
            listaSelectItemTurmaDisciplinaEquivalenteAdicionar = new ArrayList(0);
        }
        return listaSelectItemTurmaDisciplinaEquivalenteAdicionar;
    }

    /**
     * @param listaSelectItemTurmaDisciplinaEquivalenteAdicionar
     *            the listaSelectItemTurmaDisciplinaEquivalenteAdicionar to set
     */
    public void setListaSelectItemTurmaDisciplinaEquivalenteAdicionar(List listaSelectItemTurmaDisciplinaEquivalenteAdicionar) {
        this.listaSelectItemTurmaDisciplinaEquivalenteAdicionar = listaSelectItemTurmaDisciplinaEquivalenteAdicionar;
    }


    public void adicionarDisciplinaNaDisponibilidadeHorarioAluno() throws Exception {
        List<HorarioTurmaVO> horarioTurmaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaPeloCodigoTurmaTrazendoTurmaAgrupada(
                getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        List<MatriculaPeriodoTurmaDisciplinaVO> objs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
        objs.add(getMatriculaPeriodoTurmaDisciplinaVOAdicionar());
        if (!getFacadeFactory().getHorarioAlunoFacade().montarHorarioAluno(getHorarioAlunoVO(), horarioTurmaVOs, objs, getUsuarioLogado())) {
            throw new Exception("Não existe no Horário da Turma (" + getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getIdentificadorTurma() + ") a Disciplina ("
                    + getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getDisciplina().getNome() + ") cadastrado para montar o Horário do Aluno");
        }
        montarDadosProfessor();
        objs = null;
        horarioTurmaVOs = null;
    }

    public void montarDadosProfessor() throws Exception {
        for (HorarioAlunoTurnoVO horarioAlunoTurnoVOTmp : getHorarioAlunoVO().getHorarioAlunoTurnoVOs()) {
            montarDadosProfessorHorarioSemanal(horarioAlunoTurnoVOTmp);
            montarDadosProfessorHorarioDiario(horarioAlunoTurnoVOTmp);
        }
    }

    private void montarDadosProfessorHorarioDiario(HorarioAlunoTurnoVO horarioAlunoTurnoVO) throws Exception {
        for (HorarioAlunoDiaVO horarioAlunoDiaVO : horarioAlunoTurnoVO.getHorarioAlunoDiaVOs()) {
            for (HorarioAlunoDiaItemVO horarioAlunoDiaItemVO : horarioAlunoDiaVO.getHorarioAlunoDiaItemVOs()) {
                horarioAlunoDiaItemVO.setProfessor(consultarProfessor(horarioAlunoDiaItemVO.getProfessor().getCodigo()));
            }
        }
    }

    private void montarDadosProfessorHorarioSemanal(HorarioAlunoTurnoVO horarioAlunoTurnoVO) throws Exception {
        for (DisponibilidadeHorarioAlunoVO disponibilidadeHorarioAlunoVO : horarioAlunoTurnoVO.getDisponibilidadeHorarioAlunoVOs()) {
            disponibilidadeHorarioAlunoVO.setProfessorDomingo(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorDomingo().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorSegunda(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorSegunda().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorTerca(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorTerca().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorQuarta(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorQuarta().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorQuinta(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorQuinta().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorSexta(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorSexta().getCodigo()));
            disponibilidadeHorarioAlunoVO.setProfessorSabado(consultarProfessor(disponibilidadeHorarioAlunoVO.getProfessorSabado().getCodigo()));
        }
    }

    public PessoaVO consultarProfessor(Integer codigo) throws Exception {
        if (codigo.intValue() > 0) {
            PessoaVO prof = null;
            prof = getHorarioAlunoVO().getProfessor(codigo);
            if (prof != null) {
                return prof;
            }
            return getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(codigo, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        }
        return new PessoaVO();
    }

    public void verificarAlunoJaAprovadoNaDisciplina(MatriculaPeriodoVO mat, GradeDisciplinaVO grade) throws Exception {
        List<HistoricoVO> lista = getFacadeFactory().getHistoricoFacade().consultarPorMatricula(mat.getMatricula(), OrdemHistoricoDisciplina.ANO_SEMESTRE.getValor(), false,
                Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
        for (HistoricoVO obj : lista) {
            if (obj.getDisciplina().getCodigo().intValue() == grade.getDisciplina().getCodigo().intValue()) {
                if (obj.getSituacao().equals("AA") || obj.getSituacao().equals("AP")) {
                    throw new Exception("Aluno já aprovado nessa disciplina.");
                }
            }
        }
    }

    public void verificarDisciplinaNaGradeAluno(MatriculaPeriodoVO mat, GradeDisciplinaVO obj) throws Exception {
        List<MatriculaPeriodoTurmaDisciplinaVO> lista = mat.getMatriculaPeriodoTumaDisciplinaVOs();
        for (MatriculaPeriodoTurmaDisciplinaVO objeto : lista) {
            if (objeto.getDisciplina().getCodigo().intValue() == obj.getDisciplina().getCodigo().intValue()) {
                throw new Exception("Disciplina já incluída na matrícula deste período.");
            }
        }
    }

    public void verHorarioTurnoAluno() {
        HorarioAlunoTurnoVO obj = (HorarioAlunoTurnoVO) context().getExternalContext().getRequestMap().get("turno");
        setHorarioAlunoTurnoVO(obj);
    }

    public HorarioTurmaVO consultarHorarioTurma() throws Exception {
        HorarioTurmaVO obj = new HorarioTurmaVO();
        //HorarioTurmaVO obj = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(getMatriculaPeriodoTurmaDisciplinaVOAdicionar().getTurma().getCodigo(), false,
        //   Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
        return obj;
    }


    // public void verificarAlteracaoDesconto(PlanoFinanceiroAlunoVO planoFinanceiroAlunoTela) throws Exception{
    // PlanoFinanceiroAlunoVO planoFinanceiroAlunoBanco =
    // getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(planoFinanceiroAlunoTela.getMatricula(),
    // false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
    // if(planoFinanceiroAlunoTela.equals(planoFinanceiroAlunoBanco)){
    // getMatriculaPeriodoVO().setFinanceiroManual(true);
    // }
    // }
    /**
     * @return the horarioAlunoTurnoVO
     */
    public HorarioAlunoTurnoVO getHorarioAlunoTurnoVO() {
        return horarioAlunoTurnoVO;
    }

    /**
     * @param horarioAlunoTurnoVO
     *            the horarioAlunoTurnoVO to set
     */
    public void setHorarioAlunoTurnoVO(HorarioAlunoTurnoVO horarioAlunoTurnoVO) {
        this.horarioAlunoTurnoVO = horarioAlunoTurnoVO;
    }

    /**
     * @return the horarioAlunoVO
     */
    public HorarioAlunoVO getHorarioAlunoVO() {
        return horarioAlunoVO;
    }

    /**
     * @param horarioAlunoVO
     *            the horarioAlunoVO to set
     */
    public void setHorarioAlunoVO(HorarioAlunoVO horarioAlunoVO) {
        this.horarioAlunoVO = horarioAlunoVO;
    }

    /**
     * @return the gradeDisciplinaAdicionar
     */
    public Integer getGradeDisciplinaAdicionar() {
        if (gradeDisciplinaAdicionar == null) {
            gradeDisciplinaAdicionar = 0;
        }
        return gradeDisciplinaAdicionar;
    }

    /**
     * @param gradeDisciplinaAdicionar
     *            the gradeDisciplinaAdicionar to set
     */
    public void setGradeDisciplinaAdicionar(Integer gradeDisciplinaAdicionar) {
        this.gradeDisciplinaAdicionar = gradeDisciplinaAdicionar;
    }

    /**
     * @return the gradeDisciplinaEquivalenteAdicionar
     */
    public Integer getGradeDisciplinaEquivalenteAdicionar() {
        if (gradeDisciplinaEquivalenteAdicionar == null) {
            gradeDisciplinaEquivalenteAdicionar = 0;
        }
        return gradeDisciplinaEquivalenteAdicionar;
    }

    /**
     * @param gradeDisciplinaEquivalenteAdicionar
     *            the gradeDisciplinaEquivalenteAdicionar to set
     */
    public void setGradeDisciplinaEquivalenteAdicionar(Integer gradeDisciplinaEquivalenteAdicionar) {
        this.gradeDisciplinaEquivalenteAdicionar = gradeDisciplinaEquivalenteAdicionar;
    }

    public String ativarPreMatriculaPeriodo() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");

            obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado());
            getPreencherDadosMatricula(obj);

            getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerAtivada(matriculaVO, matriculaPeriodoVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaVOParaAtivada(matriculaVO, matriculaPeriodoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());

            navegarAbaDisciplinaMatriculado();
            setMensagemID("msg_ConfirmacaoPreMatricula_ativaRealizada");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void atualizarSituacaoMatriculaPeriodo() {
        try {
            if ((getMatriculaPeriodoVO().getProcessoMatricula() == null) || (getMatriculaPeriodoVO().getProcessoMatricula().equals(0))) {
                return;
            }
            if (getMatriculaPeriodoVO().getTurma().getCodigo() == 0) {
                return;
            }
            getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(matriculaVO, matriculaPeriodoVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getMatriculaPeriodoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//            getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(this.matriculaVO, this.matriculaPeriodoVO, getUsuarioLogado());
            setMensagemID("msg_dados_selecionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String alterarControleFinanceiroMatriculaParaManual() throws Exception {
        getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodoFinanceiroManual(getMatriculaPeriodoVO(), getUsuarioLogado().getCodigo(), true);
        return "";
    }

    public String editarMatriculaPeriodoVencimento() throws Exception {
        try {
            matriculaPeriodoVencimentoVoEditada = (MatriculaPeriodoVencimentoVO) context().getExternalContext().getRequestMap().get("vctos");
            setMatriculaPeriodoVencimentoVoEditada(getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVencimentoVoEditada().getCodigo().intValue(),
                    false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            setNovaMatriculaPeriodoVencimento(false);
            if (getMatriculaPeriodoVencimentoVoEditada().getParcela().equals("MA")) {
                setEditarParcela(true);
            } else {
                setEditarParcela(false);
            }
            setMensagemID("msg_dados_editar");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public String adicionarMatriculaPeriodoVencimento() {
        setMatriculaPeriodoVencimentoVoEditada(new MatriculaPeriodoVencimentoVO());
        getMatriculaPeriodoVencimentoVoEditada().setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_NAO_GERADA);
        setNovaMatriculaPeriodoVencimento(true);
        setEditarParcela(false);
        return "";
    }

    
    public void gravarMatriculaPeriodoVencimento() throws Exception {
        try {
        	/*Devido a rotina gerar falhas nas contas receber pois nao segue todas as regras que foram criadas para a mesma foi removido o  recurso de dentro da renovacao matricula e passou a ser direto na tela de conta receber. Pedro Andrade
            getFacadeFactory().getMatriculaPeriodoVencimentoFacade().gravarMatriculaPeriodoVencimento(getMatriculaPeriodoVO(), getMatriculaPeriodoVencimentoVoEditada(),getNovaMatriculaPeriodoVencimento(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            */
            setPermitirFecharRichModalMatriculaPeriodoVencimento(true);
        } catch (Exception e) {
            setPermitirFecharRichModalMatriculaPeriodoVencimento(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getFecharRichModalMatriculaPeriodoVencimento() {
        if (getPermitirFecharRichModalMatriculaPeriodoVencimento()) {
            return "RichFaces.$('panelMatriculaPeriodoVencimento').hide()";
        } else {
            return "";
        }
    }

    public Boolean getPermitirGerarContaReceber() throws Exception {
        return getFacadeFactory().getMatriculaPeriodoFacade().getPermitirGerarContaReceber(getMatriculaPeriodoVO());
    }

    public String gerarContasReceber() throws Exception {
        try {
            boolean possuiContasReceberParaGerar = getFacadeFactory().getMatriculaPeriodoFacade().verificarMatriculaPeriodoPossuiContasReceberParaGerar(getMatriculaPeriodoVO().getCodigo(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            if (possuiContasReceberParaGerar) {
                getFacadeFactory().getMatriculaPeriodoFacade().processarGeracaoContasReceberAposConfirmacaoPagamentoMatricula(getMatriculaVO(), getMatriculaPeriodoVO().getCodigo(), null, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getMatriculaPeriodoVO().getBloqueioPorFechamentoMesLiberado(), getUsuarioLogado());
                setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
                setMensagemID("msg_dados_gravados");
            } else {
                setMensagemDetalhada("msg_erro", "Não existem contas para serem geradas.");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    /**
     * @return the listaSelectItemProcessoMatricula
     */
    public List getListaSelectItemProcessoMatricula() {
        return listaSelectItemProcessoMatricula;
    }

    /**
     * @param listaSelectItemProcessoMatricula
     *            the listaSelectItemProcessoMatricula to set
     */
    public void setListaSelectItemProcessoMatricula(List listaSelectItemProcessoMatricula) {
        this.listaSelectItemProcessoMatricula = listaSelectItemProcessoMatricula;
    }

    /**
     * @return the matriculaPeriodoVencimentoVoEditada
     */
    public MatriculaPeriodoVencimentoVO getMatriculaPeriodoVencimentoVoEditada() {
        if (matriculaPeriodoVencimentoVoEditada == null) {
            matriculaPeriodoVencimentoVoEditada = new MatriculaPeriodoVencimentoVO();
        }
        return matriculaPeriodoVencimentoVoEditada;
    }

    /**
     * @param matriculaPeriodoVencimentoVoEditada
     *            the matriculaPeriodoVencimentoVoEditada to set
     */
    public void setMatriculaPeriodoVencimentoVoEditada(MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVoEditada) {
        this.matriculaPeriodoVencimentoVoEditada = matriculaPeriodoVencimentoVoEditada;
    }

    /**
     * @return the novaMatriculaPeriodoVencimento
     */
    public boolean getNovaMatriculaPeriodoVencimento() {
        return novaMatriculaPeriodoVencimento;
    }

    /**
     * @param novaMatriculaPeriodoVencimento
     *            the novaMatriculaPeriodoVencimento to set
     */
    public void setNovaMatriculaPeriodoVencimento(boolean novaMatriculaPeriodoVencimento) {
        this.novaMatriculaPeriodoVencimento = novaMatriculaPeriodoVencimento;
    }

    /**
     * @return the editarParcela
     */
    public boolean getEditarParcela() {
        return editarParcela;
    }

    /**
     * @param editarParcela
     *            the editarParcela to set
     */
    public void setEditarParcela(boolean editarParcela) {
        this.editarParcela = editarParcela;
    }

    /**
     * @return the fecharRichModalMatriculaPeriodoVencimento
     */
    public boolean getPermitirFecharRichModalMatriculaPeriodoVencimento() {
        return permitirFecharRichModalMatriculaPeriodoVencimento;
    }

    /**
     * @param fecharRichModalMatriculaPeriodoVencimento
     *            the fecharRichModalMatriculaPeriodoVencimento to set
     */
    public void setPermitirFecharRichModalMatriculaPeriodoVencimento(boolean permitirFecharRichModalMatriculaPeriodoVencimento) {
        this.permitirFecharRichModalMatriculaPeriodoVencimento = permitirFecharRichModalMatriculaPeriodoVencimento;
    }

    /**
     * @return the valorConsultaCandidato
     */
    public Integer getValorConsultaCandidato() {
        if (valorConsultaCandidato == null) {
            valorConsultaCandidato = 0;
        }
        return valorConsultaCandidato;
    }

    /**
     * @param valorConsultaCandidato
     *            the valorConsultaCandidato to set
     */
    public void setValorConsultaCandidato(Integer valorConsultaCandidato) {
        this.valorConsultaCandidato = valorConsultaCandidato;
    }

    /**
     * @return the candidatoEncontrado
     */
    public boolean getIsCandidatoEncontrado() {
        return candidatoEncontrado;
    }

    /**
     * @param candidatoEncontrado
     *            the candidatoEncontrado to set
     */
    public void setCandidatoEncontrado(boolean candidatoEncontrado) {
        this.candidatoEncontrado = candidatoEncontrado;
    }

    /**
     * @return the cursosAprovado
     */
    public boolean getIsCursoAprovado() {
        return cursoAprovado;
    }

    /**
     * @param cursosAprovado
     *            the cursosAprovado to set
     */
    public void setCursoAprovado(boolean cursoAprovado) {
        this.cursoAprovado = cursoAprovado;
    }

    /**
     * @return the matriculandoCandidato
     */
    public boolean getIsMatriculandoCandidato() {
        return matriculandoCandidato;
    }

    /**
     * @param matriculandoCandidato
     *            the matriculandoCandidato to set
     */
    public void setMatriculandoCandidato(boolean matriculandoCandidato) {
        this.matriculandoCandidato = matriculandoCandidato;
    }
    
    private boolean isPermitirMatriculaForaPrazo(UsuarioVO usuarioVO) {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_FORA_PRAZO.getValor(), usuarioVO);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
