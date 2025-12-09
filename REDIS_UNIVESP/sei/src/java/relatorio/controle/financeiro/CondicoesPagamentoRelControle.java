package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.CondicoesPagamentoRelVO;
import relatorio.parametroRelatorio.academico.CondicoesPagamentoSuperParametroRelVO;

@Controller("CondicoesPagamentoRelControle")
@Scope("viewScope")
@Lazy
public class CondicoesPagamentoRelControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = -749872076580006140L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private TurmaVO turmaVO;
    private MatriculaVO matriculaVO;
    private String ano;
    private String semestre;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
    
    private List<SelectItem> listaSelectSemestre;
    private List<TurmaVO> listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<MatriculaVO> listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private String tipoLayout;
    
    private CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO;
    private Date dataInicio;
    private Date dataFim;
    
    private List<CondicoesPagamentoRelVO> listaPlanoDesconto;

    public CondicoesPagamentoRelControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        novo();
    }

    public void novo() {
        incializarDados();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void incializarDados() {
        setMatriculaVO(new MatriculaVO());
        montarListaSelectItemUnidadeEnsino();
        setAno(Uteis.getAnoDataAtual());
        setSemestre(Uteis.getSemestreAtual());
        montarListaSelectItemSemestre();
        getFiltroRelatorioAcademicoVO().setDataInicio(null);
        getFiltroRelatorioAcademicoVO().setDataTermino(null);
        
        try {
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), CondicoesPagamentoRelControle.class.getSimpleName(), getUsuarioLogado());
			Map<String, String> valores = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] {"layout", "dataInicioVencimento", "dataTerminoVencimento"},  CondicoesPagamentoRelControle.class.getSimpleName());
			if(valores.containsKey("layout")) {
				setTipoLayout(valores.get("layout"));				
			}
			if(valores.containsKey("dataInicioVencimento")) {
				setDataInicio(Uteis.getDate(valores.get("dataInicioVencimento")));
			}
			if(valores.containsKey("dataTerminoVencimento")) {
				setDataFim(Uteis.getDate(valores.get("dataTerminoVencimento")));
			}
			
		} catch (Exception e) {
			
		}
        
        
    }

    public void imprimirPDF() {

        List<CondicoesPagamentoRelVO> listaObjetos;
        String semestre = "";
        try {
        	setCondicoesPagamentoSuperParametroRelVO(new CondicoesPagamentoSuperParametroRelVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "Condicoes Pagamento Rel RelControle", "Iniciando Impressao PDF", "Emitindo Relatorio");
            getFacadeFactory().getCondicoesPagamentoRelFacade().validarDados(getDataInicio(), getDataFim(), getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO(), getFiltroRelatorioAcademicoVO(), getFiltroRelatorioAcademicoVO().getAno(), getFiltroRelatorioAcademicoVO().getSemestre());
            if (!tipoLayout.equals("AL")) {
            	setListaPlanoDesconto(getFacadeFactory().getCondicoesPagamentoRelFacade().criarListaPlanoDesconto(getDataInicio(), getDataFim(), getFiltroRelatorioAcademicoVO().getAno(), getFiltroRelatorioAcademicoVO().getSemestre(), getUnidadeEnsinoVO(),
                    getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getUnidadeEnsinoCursoVO(), getMatriculaVO(), getCondicoesPagamentoSuperParametroRelVO(), getTipoLayout(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado()));
            }else {
            	setListaPlanoDesconto(new ArrayList<CondicoesPagamentoRelVO>());
            }
            
            listaObjetos = getFacadeFactory().getCondicoesPagamentoRelFacade().criarObjeto(getDataInicio(), getDataFim(), getFiltroRelatorioAcademicoVO().getAno(), getFiltroRelatorioAcademicoVO().getSemestre(), getUnidadeEnsinoVO(),
                    getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getUnidadeEnsinoCursoVO(), getMatriculaVO(), getCondicoesPagamentoSuperParametroRelVO(), getTipoLayout(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getCondicoesPagamentoSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getCondicoesPagamentoRelFacade().designIReportRelatorio(getTipoLayout()));
                getCondicoesPagamentoSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getCondicoesPagamentoSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getCondicoesPagamentoRelFacade().caminhoBaseRelatorio());
                getCondicoesPagamentoSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getCondicoesPagamentoSuperParametroRelVO().setTituloRelatorio("Relação de Condições de Pagamento");
                getCondicoesPagamentoSuperParametroRelVO().setListaObjetos(listaObjetos);
                getCondicoesPagamentoSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getCondicoesPagamentoRelFacade().caminhoBaseRelatorio());
                getCondicoesPagamentoSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getCondicoesPagamentoSuperParametroRelVO().setQtdePagantes(getCondicoesPagamentoSuperParametroRelVO().getQtdeMatriculados() - (getCondicoesPagamentoSuperParametroRelVO().getQtdeNaoAtivos() + getCondicoesPagamentoSuperParametroRelVO().getQtdeBolsas()));
                if (getUnidadeEnsinoVO().getCodigo() > 0) {
                    getCondicoesPagamentoSuperParametroRelVO().setUnidadeEnsino(
                            (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                } else {
                    getCondicoesPagamentoSuperParametroRelVO().setUnidadeEnsino("TODAS");
                }

                if (getUnidadeEnsinoCursoVO().getCodigo() > 0) {
                    getCondicoesPagamentoSuperParametroRelVO().setCurso(
                            (getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getUnidadeEnsinoCursoVO().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNomeCursoTurno());
                } else {
                    getCondicoesPagamentoSuperParametroRelVO().setCurso("TODOS");
                }

                if (getTurmaVO().getIdentificadorTurma() != null && !getTurmaVO().getIdentificadorTurma().equals("")) {
                    getCondicoesPagamentoSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                } else {
                    getCondicoesPagamentoSuperParametroRelVO().setTurma("TODAS");
                }


                if (getSemestre() != null && !getSemestre().equals("")) {
                    if (getSemestre().equals("1")) {
                        semestre = "1º";
                    } else if (getSemestre().equals("2")) {
                        semestre = "2º";
                    }
                }
                                	
                if(!getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().isIntegral()) {                	
                	getCondicoesPagamentoSuperParametroRelVO().setAno(getAno() == null ? "" : getAno());
                }else {
                	getCondicoesPagamentoSuperParametroRelVO().setAno("");
                }
                if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.SEMESTRAL)) {  
                	getCondicoesPagamentoSuperParametroRelVO().setSemestre(semestre);
                }else {
                	getCondicoesPagamentoSuperParametroRelVO().setSemestre("");
                }
                if(getDataInicio() != null) {
                	getCondicoesPagamentoSuperParametroRelVO().setDataInicio(Uteis.getData(getDataInicio()));
                }
                if(getDataFim() != null) {
                	getCondicoesPagamentoSuperParametroRelVO().setDataFim(Uteis.getData(getDataFim()));
                }
                
                if(getMatriculaVO().getAluno().getNome() != null && !getMatriculaVO().getAluno().getNome().equals("")) {
                	getCondicoesPagamentoSuperParametroRelVO().setAluno(getMatriculaVO().getAluno().getNome());
                } else {
                	getCondicoesPagamentoSuperParametroRelVO().adicionarParametro("filtroAcademico", getFiltroRelatorioAcademicoVO().getFiltroAcademicoUtilizado());
                	getCondicoesPagamentoSuperParametroRelVO().setAluno("Todos");
                	if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().isIntegral()) {
                		if(getFiltroRelatorioAcademicoVO().getDataInicio() != null) {
                			getCondicoesPagamentoSuperParametroRelVO().adicionarParametro("dataInicioMatricula", Uteis.getData(getFiltroRelatorioAcademicoVO().getDataInicio()));
                		}
                		if(getFiltroRelatorioAcademicoVO().getDataTermino() != null) {
                			getCondicoesPagamentoSuperParametroRelVO().adicionarParametro("dataTerminoMatricula", Uteis.getData(getFiltroRelatorioAcademicoVO().getDataTermino()));
                		}
                	}
                }
                getCondicoesPagamentoSuperParametroRelVO().setLista( getListaPlanoDesconto());
                getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), CondicoesPagamentoRelControle.class.getSimpleName(), getUsuarioLogado());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("layout", CondicoesPagamentoRelControle.class.getSimpleName(), getTipoLayout(), getUsuario());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("dataInicioVencimento", CondicoesPagamentoRelControle.class.getSimpleName(), Uteis.getDataAno4Digitos(getDataInicio()), getUsuario());
                getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2("dataTerminoVencimento", CondicoesPagamentoRelControle.class.getSimpleName(), Uteis.getDataAno4Digitos(getDataInicio()), getUsuario());
                realizarImpressaoRelatorio(getCondicoesPagamentoSuperParametroRelVO());
                
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "Condicoes Pagamento Rel RelControle", "Finalizando Impressao PDF", "Emitindo Relatorio");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            semestre = null;
            listaObjetos = null;

        }
    }

    public void montarListaSelectItemSemestre() {
        List<SelectItem> lista = new ArrayList<SelectItem>(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        setListaSelectSemestre(lista);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            Uteis.liberarListaMemoria(resultadoConsulta);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void consultarCurso() {
        try {
            List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() throws Exception {
        try {
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
            setUnidadeEnsinoCursoVO(obj);
            getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.getEnumPorValor(obj.getCurso().getPeriodicidade()));
        } catch (Exception e) {
        	
        }
    }

    public void limparCurso() throws Exception {
        try {
            setUnidadeEnsinoCursoVO(null);
        } catch (Exception e) {
        }
    }


    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
     * JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), 0, 0);
            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
            return "Consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "Consultar";
        }
    }

    public void limparMensagemTurma() {
        Uteis.liberarListaMemoria(getListaConsultaTurma());
        setListaConsultaTurma(new ArrayList<>(0));
        setMensagemID("msg_entre_dados");
    }

    public void limparMensagemAluno() {
        Uteis.liberarListaMemoria(getListaConsultaAluno());
        setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
        setMensagemID("msg_entre_dados");
    }

    public void limparDadosTurma() {
        removerObjetoMemoria(getUnidadeEnsinoCursoVO());
        removerObjetoMemoria(getTurmaVO());
        setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
        setTurmaVO(new TurmaVO());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getTurmaVO().setCodigo(obj.getCodigo());
        getTurmaVO().setIdentificadorTurma(obj.getIdentificadorTurma());
        getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.getEnumPorValor(getTurmaVO().getPeriodicidade()));
        getUnidadeEnsinoCursoVO().setCurso(obj.getCurso());
        setCampoConsultaTurma("");
        setValorConsultaTurma("");
        setListaConsultaTurma(new ArrayList<TurmaVO>(0));

    }

    public void consultarAluno() {
        try {
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
        setMatriculaVO(obj);
        getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.getEnumPorValor(obj.getCurso().getPeriodicidade()));
        if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.INTEGRAL)) {
        	getFiltroRelatorioAcademicoVO().setDataInicio(null);
        	getFiltroRelatorioAcademicoVO().setDataTermino(null);
        }
        setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
        getUnidadeEnsinoCursoVO().setCurso(obj.getCurso());
        setUnidadeEnsinoVO(obj.getUnidadeEnsino());
        setTurmaVO(null);
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        
        getListaConsultaAluno().clear();
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMatriculaVO(objAluno);
            getFiltroRelatorioAcademicoVO().setPeriodicidadeEnum(PeriodicidadeEnum.getEnumPorValor(objAluno.getCurso().getPeriodicidade()));
            if(getFiltroRelatorioAcademicoVO().getPeriodicidadeEnum().equals(PeriodicidadeEnum.INTEGRAL)) {
            	getFiltroRelatorioAcademicoVO().setDataInicio(null);
            	getFiltroRelatorioAcademicoVO().setDataTermino(null);
            }
            setTurmaVO(null);
            getUnidadeEnsinoCursoVO().setCurso(objAluno.getCurso());
            setUnidadeEnsinoVO(objAluno.getUnidadeEnsino());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public boolean getTipoCursoNaoPosGraduacao() {
        if (getTurmaVO().getCodigo() == 0) {
            setAno("");
            setSemestre("");
            return false;
        }
        if (!getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("PO") && !getUnidadeEnsinoCursoVO().getCurso().getNivelEducacional().equals("EX")) {
            if (getAno() == null || getAno().equals("")) {
                setAno(Uteis.getAnoDataAtual());
            }
            if (getSemestre() == null || getSemestre().equals("")) {
                setSemestre(Uteis.getSemestreAtual());
            }
            return true;
        }
        setAno("");
        setSemestre("");
        return false;
    }

    public List<SelectItem> tipoConsultaComboAluno;
    public List<SelectItem> getTipoConsultaComboAluno() {
    	if(tipoConsultaComboAluno == null) {
    		tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
    		tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
    		tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
    		tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
    	}
        return tipoConsultaComboAluno;
    }

    public boolean getApresentarAluno() {
        if (getTurmaVO().getCodigo() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getApresentarTurma() {
        if (getMatriculaVO().getMatricula().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(new MatriculaVO());
        setUnidadeEnsinoCursoVO(null);
        setMensagemID("msg_entre_dados");
    }

    public List<SelectItem> tipoConsultaComboTurma;
    public List<SelectItem> getTipoConsultaComboTurma() {
    	if(tipoConsultaComboTurma == null) {
    		tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
    		tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
    		tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
    	}
        return tipoConsultaComboTurma;
    }

    public List<SelectItem> tipoConsultaComboCurso;
    public List<SelectItem> getTipoConsultaComboCurso() {
    	if(tipoComboLayout == null) {
    		tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
    		tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
    		tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
    	}
        return tipoConsultaComboCurso;
    }

    public List<SelectItem> tipoComboLayout;
    public List<SelectItem> getTipoComboLayout() {
    	if(tipoComboLayout == null) {
    		tipoComboLayout = new ArrayList<SelectItem>(0);
    		tipoComboLayout.add(new SelectItem("AL", "Layout - Alunos"));
    		tipoComboLayout.add(new SelectItem("CP", "Layout - Condição Pagamento"));
    	}
    	return tipoComboLayout;
    }
    
    //---------------------------------------------------------------------------------------------------------    
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public Boolean getApresentarBotaoRelatorio() {
            return true;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public List<SelectItem> getListaSelectSemestre() {
        if (listaSelectSemestre == null) {
            listaSelectSemestre = new ArrayList<SelectItem>(0);
        }
        return listaSelectSemestre;
    }

    public void setListaSelectSemestre(List<SelectItem> listaSelectSemestre) {
        this.listaSelectSemestre = listaSelectSemestre;
    }

    public List<TurmaVO> getListaConsultaTurma() {
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    /**
     * @return the valorConsultaTurma
     */
    public String getValorConsultaTurma() {
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public String getCampoConsultaTurma() {
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List<MatriculaVO> getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList<MatriculaVO>(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    /**
     * @return the condicoesPagamentoSuperParametroRelVO
     */
    public CondicoesPagamentoSuperParametroRelVO getCondicoesPagamentoSuperParametroRelVO() {
        if (condicoesPagamentoSuperParametroRelVO == null) {
            condicoesPagamentoSuperParametroRelVO = new CondicoesPagamentoSuperParametroRelVO();
        }
        return condicoesPagamentoSuperParametroRelVO;
    }

    /**
     * @param condicoesPagamentoSuperParametroRelVO the condicoesPagamentoSuperParametroRelVO to set
     */
    public void setCondicoesPagamentoSuperParametroRelVO(CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO) {
        this.condicoesPagamentoSuperParametroRelVO = condicoesPagamentoSuperParametroRelVO;
    }

    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

	public String getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = "AL";
		}
		return tipoLayout;
	}

	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<CondicoesPagamentoRelVO> getListaPlanoDesconto() {
		return listaPlanoDesconto;
	}

	public void setListaPlanoDesconto(List<CondicoesPagamentoRelVO> listaPlanoDesconto) {
		this.listaPlanoDesconto = listaPlanoDesconto;
	}
	
}