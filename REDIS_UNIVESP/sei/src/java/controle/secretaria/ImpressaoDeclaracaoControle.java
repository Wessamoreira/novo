package controle.secretaria;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.GraduacaoPosGraduacaoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 *
 * @author Carlos
 */
@Controller("ImpressaoDeclaracaoControle")
@Scope("viewScope")
@Lazy
public class ImpressaoDeclaracaoControle extends SuperControleRelatorio implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5690633971256131464L;
	private ImpressaoContratoVO impressaoContratoVO;
    private String tipoContrato;
    private Integer textoPadraoDeclaracao;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private List listaConsultaAluno;
    private List<InscricaoVO> listaConsultaInscricao;
    private String campoConsultaInscricao;
    private String valorConsultaInscricao;
    private List listaSelectItemTipoTextoPadrao;
    private Boolean imprimirContrato;
    private String mensagemQuantidadeImpressa;
    private ImpressaoContratoVO impressaoContratoGravarVO;
    private List listaConsultaFuncionario;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List listaCargoFuncionarioPrincipal;
    private List listaCargoFuncionarioSecundario;
    private List listaSelectItemFuncionario;
    private Boolean funcionarioPrincipal;
    private String tipoDeclaracao;
    private Boolean adicionarAssinatura;
    private FuncionarioVO funcionarioPrincipalVO;
    private FuncionarioVO funcionarioSecundarioVO;
    private CargoVO cargoFuncionarioPrincipal;
    private CargoVO cargoFuncionarioSecundario;
    private String campoConsultaProfessor;
    private String valorConsultaProfessor;
    private List listaConsultaProfessor;
    private List<SelectItem> tipoConsultaComboProfessor;
    private FuncionarioVO professor;
   // private List<ImpressaoContratoVO> listaAlunoTurmaVOs;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private String campoConsultaDisciplinaTurma;
    private String valorConsultaDisciplinaTurma;
    private List listaConsultaDisciplinaTurma;
    private Boolean alunoSelecionado;
    private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
    private Boolean isDownloadContrato;
    private ArquivoVO arquivoVO;
    private String campoConsultaProgramacaoFormatura;
    private Boolean mostrarSegundoCampoProgramacaoFormatura;
    private String valorConsultaProgramacaoFormatura;
    private Date valorConsultaDataInicioProgramacaoFormatura;
    private Date valorConsultaDataFinalProgramacaoFormatura;
    private String filtroAlunosPresentesColacaoGrau;
    private List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura;
    private Boolean apresentarProgramacaoFormatura ;
    private ProgramacaoFormaturaVO programacaoFormaturaVO;
    private Boolean apresentarResultadoConsultaMatriculaGerarContrato;  
	
	private ProgressBarVO progressBarVO;
    private List<File> listaArquivos = new ArrayList<File>(0);
    private File arquivoZip;
    private List<ImpressaoContratoVO> listaAlunoGerarContrato; 
    private List<ImpressaoContratoVO> listaAlunoGerarContratoErro; 
    private Boolean painelALunosGerarAberto  ;
    private Boolean apresentarListaErros;
    private Boolean painelALunosGerarErroAberto  ;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    
    

    public ImpressaoDeclaracaoControle() {
    	
    }
    
    @PostConstruct
    public void inicializarDadosRequisicao(){    	
    	try {
    		String matricula = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("matricula");
        	
        	if(matricula != null  && !matricula.trim().isEmpty()){    	
        		String textoPadrao = ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("textoPadrao");
        		setImpressaoContratoVO(new ImpressaoContratoVO());
        		getImpressaoContratoVO().getMatriculaVO().setMatricula(matricula);    		
        		consultarAlunoPorMatricula();
        		if(textoPadrao != null  && !textoPadrao.trim().isEmpty() && !textoPadrao.equals("0")){    	
        			setTextoPadraoDeclaracao(Integer.valueOf(textoPadrao));    			
        		}
        		String textoRequerimento= ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("requerimento");
        		if(Uteis.isAtributoPreenchido(textoRequerimento)){    	
        			getImpressaoContratoVO().setRequerimentoVO(new RequerimentoVO());
            		getImpressaoContratoVO().getRequerimentoVO().setCodigo(Integer.valueOf(textoRequerimento));
            		getFacadeFactory().getRequerimentoFacade().carregarDados(getImpressaoContratoVO().getRequerimentoVO(),NivelMontarDados.TODOS, null, getUsuarioLogado());
            		getImpressaoContratoVO().setDisciplinaVO(getImpressaoContratoVO().getRequerimentoVO().getDisciplina());
        		}
        	}
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage(), e, getUsuarioLogadoClone(), "", "", true);
		}
    }


    public void consultarAlunoPorMatricula() {
        try {
            getFacadeFactory().getImpressaoContratoFacade().consultarAlunoPorMatricula(getImpressaoContratoVO(), getUnidadeEnsinoLogado().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), getUsuarioLogado());
            consultarListaSelectItemTipoTextoPadrao(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            this.setImpressaoContratoVO(new ImpressaoContratoVO());
        }
    }

    public void novo() {
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDescricao("", unidadeEnsino, false, Arrays.asList(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            if (!getListaSelectItemTipoTextoPadrao().isEmpty()) {
            	setTextoPadraoDeclaracao(Integer.valueOf(getListaSelectItemTipoTextoPadrao().get(0).getValue().toString()));
            	verificarTextoPadraoAssinarDigitalmente();
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void verificarTextoPadraoAssinarDigitalmente() {
    	if (Uteis.isAtributoPreenchido(getTextoPadraoDeclaracao())) {
    		try {
    			setAdicionarAssinatura(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarTextoPadraoAssinarDigitalmente(getTextoPadraoDeclaracao()));
    			if (!getAdicionarAssinatura()) {
    				limparDadosFuncionarioPrincipal();
    				limparDadosFuncionarioSecundario();
    			}
			} catch (Exception e) {
				setAdicionarAssinatura(Boolean.FALSE);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
    	}
    }

    public void consultarListaSelectItemTipoTextoPadrao(String tipo, Integer unidadeEnsino) {
    	try {
    		getListaSelectItemTipoTextoPadrao().clear();
    		List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo(tipo, unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    		for (TextoPadraoDeclaracaoVO objeto : lista) {
    			getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
    		}
    		if (!getListaSelectItemTipoTextoPadrao().isEmpty()) {
            	setTextoPadraoDeclaracao(Integer.valueOf(getListaSelectItemTipoTextoPadrao().get(0).getValue().toString()));
            	verificarTextoPadraoAssinarDigitalmente();
            }
    		setMensagemID("msg_dados_consultados");
    	} catch (Exception e) {
    		setListaConsultaAluno(new ArrayList(0));
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void consultarListaSelectItemTextoPadraoPorTipoRequerimento(String tipo, Integer unidadeEnsino) {
    	try {
    		getListaSelectItemTipoTextoPadrao().clear();
    		List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipoRequerimento(tipo, unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    		for (TextoPadraoDeclaracaoVO objeto : lista) {
    			getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
    		}
    		if (!getListaSelectItemTipoTextoPadrao().isEmpty()) {
            	setTextoPadraoDeclaracao(Integer.valueOf(getListaSelectItemTipoTextoPadrao().get(0).getValue().toString()));
            	verificarTextoPadraoAssinarDigitalmente();
            }
    		setMensagemID("msg_dados_consultados");
    	} catch (Exception e) {
    		setListaConsultaAluno(new ArrayList(0));
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
	
    public void consultarAluno() {
        try {
            setListaConsultaAluno(getFacadeFactory().getImpressaoContratoFacade().consultarAluno(getValorConsultaAluno(), getCampoConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarDisciplinaTurma() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaTurmaItens");
        getImpressaoContratoVO().setDisciplinaVO(obj);
        //montarListaTurmaDisciplina();
        setListaConsultaDisciplinaTurma(new ArrayList(0));
        obj = null;
        setValorConsultaDisciplinaTurma("");
        setCampoConsultaDisciplinaTurma("");
        getListaConsultaDisciplinaTurma().clear();
    }

    public void limparDadosDisciplinaTurma() throws Exception {
        getImpressaoContratoVO().setDisciplinaVO(new DisciplinaVO());
        setMensagemID("msg_entre_prmconsulta");
    }

    public void consultarDisciplinaTurmaRich() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaDisciplinaTurma().equals("codigo")) {
                if (getValorConsultaDisciplinaTurma().equals("")) {
                    setValorConsultaDisciplinaTurma("0");
                }
                if (getValorConsultaDisciplinaTurma().trim() != null || !getValorConsultaDisciplinaTurma().trim().isEmpty()) {
                    Uteis.validarSomenteNumeroString(getValorConsultaDisciplinaTurma().trim());
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplinaTurma());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), getImpressaoContratoVO().getMatriculaVO().getCurso().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getTurma().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplinaTurma().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplinaUnidadeEnsinoCodigoCursoCodigoTurma(getValorConsultaDisciplinaTurma(), getUnidadeEnsinoLogado().getCodigo(), getImpressaoContratoVO().getMatriculaVO().getCurso().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getTurma().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaDisciplinaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplinaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() {
        try {
            MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
            getImpressaoContratoVO().setMatriculaVO(obj);
            getFacadeFactory().getImpressaoContratoFacade().consultarAlunoPorMatricula(getImpressaoContratoVO(), getUnidadeEnsinoLogado().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), getUsuarioLogado());
            consultarListaSelectItemTipoTextoPadrao(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo());
            setMensagemID("msg_dados_selecionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Boolean getApresentarAno() {
		return getImpressaoContratoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("AN") || getImpressaoContratoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
	}
	
	public Boolean getApresentarSemestre() {
		return getImpressaoContratoVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
	}

	public void consultarInscricao() {
        try {
            setListaConsultaInscricao(new ArrayList(0));
            List<InscricaoVO> objs = new ArrayList<InscricaoVO>(0);
            if (getCampoConsultaInscricao().equals("codigo")) {
                if (getValorConsultaInscricao().equals("")) {
                    throw new ConsistirException("Por favor informe o CÓDIGO desejado.");
                }
                int valorInt = Integer.parseInt(getValorConsultaInscricao());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaInscricao().equals("nomePessoa")) {
                if (getValorConsultaInscricao().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
                }
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getValorConsultaInscricao(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaInscricao(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaInscricao(new ArrayList<InscricaoVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarInscricao() throws Exception {
    	try {
    		InscricaoVO inscricao = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
            inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(inscricao.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            inscricao.getResultadoProcessoSeletivoVO().setClassificacao(inscricao.getClassificacao());
    		getImpressaoContratoVO().setInscricaoVO(new InscricaoVO());
    		getImpressaoContratoVO().getInscricaoVO().setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
    		getImpressaoContratoVO().setInscricaoVO(inscricao);
    		getImpressaoContratoVO().getInscricaoVO().setResultadoProcessoSeletivoVO(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(inscricao.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    		getImpressaoContratoVO().getInscricaoVO().getResultadoProcessoSeletivoVO().setInscricao(inscricao);
            consultarListaSelectItemTipoTextoPadrao("PS", 0);
            setListaConsultaInscricao(new ArrayList<>(0));
		} catch (Exception e) {
			getImpressaoContratoVO().setInscricaoVO(new InscricaoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

    }	
    
    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurmaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado
     * campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }

            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }

    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getImpressaoContratoVO().setTurmaVO(obj);        
        consultarListaSelectItemTipoTextoPadrao(obj.getUnidadeEnsino().getCodigo());
        setListaAlunoGerarContrato(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarAlunosTurmaVOs(obj.getCodigo(), getUsuarioLogado()));
        if(!getListaAlunoGerarContrato().isEmpty()) {
        	setApresentarResultadoConsultaMatriculaGerarContrato(Boolean.TRUE);
        }
    }

    public void limparDadosAluno() {
        setImpressaoContratoVO(new ImpressaoContratoVO());
        setImprimirContrato(false);
    }

    public void limparDadosInscProcSeletivo() {
    	getImpressaoContratoVO().setInscricaoVO(new InscricaoVO());
    	setImprimirContrato(false);
    }
	
    public void limparDadosTurma() {
        setImpressaoContratoVO(new ImpressaoContratoVO());
        getListaAlunoGerarContrato().clear();
        setImprimirContrato(false);
    }

    public List<SelectItem> getListaSelectItemTipoTextoPadrao() throws Exception {
        if (listaSelectItemTipoTextoPadrao == null) {
            listaSelectItemTipoTextoPadrao = new ArrayList();
        }
        return listaSelectItemTipoTextoPadrao;
    }
    
    public void realizarImpressaoTextoPadraoHtml() {
		try {
			validarImpressaoContrato();
			validarPeriodoInformadoMatriculaPeriodo();
			limparMensagem();	
			setImprimirContrato(false);
			this.setCaminhoRelatorio("");				
			getImpressaoContratoVO().setImpressaoPdf(false);
			getImpressaoContratoGravarVO().setImpressaoPdf(false);
			getImpressaoContratoVO().setImpressaoDoc(false);
			getImpressaoContratoGravarVO().setImpressaoDoc(false);
			getImpressaoContratoGravarVO().setGerarNovoArquivoAssinado(true);
			getImpressaoContratoVO().setFuncionarioPrincipalVO(this.getFuncionarioPrincipalVO());
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralPadraoSistema();
			if(getTipoDeclaracao().equals("AL")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("TU")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("PS")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("PF")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
			}
			if (this.getCargoFuncionarioPrincipal().getCodigo().intValue() > 0) {
				CargoVO cargo = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getImpressaoContratoVO().setCargoFuncionarioPrincipal(cargo);	
			}
			getImpressaoContratoVO().setFuncionarioSecundarioVO(this.getFuncionarioSecundarioVO());
			if (this.getCargoFuncionarioSecundario().getCodigo().intValue() > 0) {
				CargoVO cargo = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getImpressaoContratoVO().setCargoFuncionarioSecundario(cargo);
			}
			if (getTipoDeclaracao().equals("PS")) {
        		if (getImpressaoContratoVO().getInscricaoVO().getCodigo().intValue() == 0) {
        			throw new Exception("Informe a Inscrição para emitir a declaração desejada!"); 
        		}
        		setImprimirContrato(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracaoProcSeletivo(getTextoPadraoDeclaracao(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoFinanceiroVO, getUsuarioLogado()));
        	} else {
            	if (getImpressaoContratoVO().getMatriculaVO().getMatricula().equals("") && !getTipoDeclaracao().equals("TU")  && !getTipoDeclaracao().equals("PF") ) {
            		throw new Exception("Informe a Matrícula para emitir a declaração desejada!");
            	}
        		if (!getImpressaoContratoVO().getMatriculaPeriodoVO().getAno().equals("") || !getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre().equals("")) {
	        		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(impressaoContratoVO.getMatriculaVO().getMatricula(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), ""));
	        		impressaoContratoVO.getMatriculaPeriodoVO().setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        		}
	        	if (impressaoContratoVO.getMatriculaPeriodoVO() == null || impressaoContratoVO.getMatriculaPeriodoVO().getCodigo().equals(0)) {
	        		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	        	}
	            if (getTipoDeclaracao().equals("TU") ||  getTipoDeclaracao().equals("PF")) {
	                for (ImpressaoContratoVO impressaoContrato : getListaAlunoGerarContrato()) {
	                    if (impressaoContrato.getAlunoSelecionado()) {
	                    	impressaoContrato.setImpressaoDoc(Boolean.FALSE);
	                    	impressaoContrato.setImpressaoPdf(Boolean.FALSE);
	                    	setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), impressaoContrato, getImpressaoContratoGravarVO(), "", getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado(), true));
	                        break;
	                    }
	                }
	            } else {
	            	setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado(), true));
	            }
        	}
			if(Uteis.isAtributoPreenchido(getCaminhoRelatorio())){
				setImprimirContrato(true);	
			}	
		} catch (Exception e) {
			setImprimirContrato(false);	
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}
    
    public List<SelectItem> getComboboxProvedorAssinaturaPadrao(){
    	Integer codigoUnidadeEnsino = 0;
    	if(getTipoDeclaracao().equals("AL")){
    		codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()) ? getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo() : 0;
		}else if(getTipoDeclaracao().equals("TU")){
			codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo()) ? getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo() : 0;
		}else if(getTipoDeclaracao().equals("PS")){
			codigoUnidadeEnsino = Uteis.isAtributoPreenchido(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo()) ? getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo() : 0;
		}
		if(!Uteis.isAtributoPreenchido(codigoUnidadeEnsino) || !getAdicionarAssinatura()){
			setProvedorDeAssinaturaEnum(null);
			return new ArrayList<SelectItem>();
		}
		return this.getComboboxProvedorAssinaturaPadrao(codigoUnidadeEnsino, TipoOrigemDocumentoAssinadoEnum.DECLARACAO);
	}

    public void realizarImpressaoTextoPadrao() {
        try {
        	if (getTipoDeclaracao().equals("PR") && !Uteis.isAtributoPreenchido(getUnidadeEnsinoVO().getCodigo()) && getAdicionarAssinatura()) {
        		throw new Exception("Informe a Unidade de Ensino para emitir a declaração desejada!");
			}
        	validarImpressaoContrato();
        	validarPeriodoInformadoMatriculaPeriodo();
        	limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			getImpressaoContratoVO().setImpressaoPdf(true);
			getImpressaoContratoVO().setImpressaoDoc(false);
			getImpressaoContratoGravarVO().setImpressaoPdf(true);
			getImpressaoContratoGravarVO().setImpressaoDoc(false);
			getImpressaoContratoGravarVO().setGerarNovoArquivoAssinado(true);
			getImpressaoContratoGravarVO().setControleGeracaoAssinatura(getAdicionarAssinatura() ? "" : "NAO_GERAR_DOC_ASSINADO");
			getImpressaoContratoVO().setFuncionarioPrincipalVO(this.getFuncionarioPrincipalVO());
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralPadraoSistema();		
			
			if(getTipoDeclaracao().equals("PR")){
	        	getImpressaoContratoVO().getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().clear();
	        	TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getTextoPadraoDeclaracao(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (texto.getTexto().contains("Lista_AulasMinistradasProfessor")) {
					Map<GraduacaoPosGraduacaoEnum, List<HorarioProfessorDiaItemVO>> mapaHorario = null;
		            mapaHorario = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorariosProfessorSeparadoPorNivelEducacional(getImpressaoContratoVO().getProfessor().getPessoa().getCodigo(),
		            		null, null, getImpressaoContratoVO().getTurmaVO().getCodigo(), getImpressaoContratoVO().getDisciplinaVO().getCodigo(), null, null, false, "", getUsuarioLogado(), "data", false, null);
		            if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.GRADUACAO)) {
		            	getImpressaoContratoVO().getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.GRADUACAO));
		            }
		            if (mapaHorario.containsKey(GraduacaoPosGraduacaoEnum.POS_GRADUACAO)) {
		            	getImpressaoContratoVO().getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().addAll(mapaHorario.get(GraduacaoPosGraduacaoEnum.POS_GRADUACAO));
		            }
				}
			}
			
			if(getTipoDeclaracao().equals("AL")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("TU")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("PS")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("PF")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
			}
			if (this.getCargoFuncionarioPrincipal().getCodigo().intValue() > 0) {
				CargoVO cargo = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getImpressaoContratoVO().setCargoFuncionarioPrincipal(cargo);	
			}
			getImpressaoContratoVO().setFuncionarioSecundarioVO(this.getFuncionarioSecundarioVO());
			if (this.getCargoFuncionarioSecundario().getCodigo().intValue() > 0) {
				CargoVO cargo = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getImpressaoContratoVO().setCargoFuncionarioSecundario(cargo);
			}			

        	if (getTipoDeclaracao().equals("PS")) {
        		if (getImpressaoContratoVO().getInscricaoVO().getCodigo().intValue() == 0) {
        			throw new Exception("Informe a Inscrição para emitir a declaração desejada!"); 
        		}
				setImprimirContrato(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracaoProcSeletivo(getTextoPadraoDeclaracao(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoFinanceiroVO, getUsuarioLogado()));
        	} else {
        		if (getImpressaoContratoVO().getMatriculaVO().getMatricula().equals("") && !getTipoDeclaracao().equals("TU") && !getTipoDeclaracao().equals("PR") && !getTipoDeclaracao().equals("RE")  && !getTipoDeclaracao().equals("PF")) {
            		throw new Exception("Informe a Matrícula para emitir a declaração desejada!");
            	}
        		if (!getImpressaoContratoVO().getMatriculaPeriodoVO().getAno().equals("") || !getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre().equals("")) {
	        		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(impressaoContratoVO.getMatriculaVO().getMatricula(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), ""));
	        		impressaoContratoVO.getMatriculaPeriodoVO().setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
	        	}
	        	if (impressaoContratoVO.getMatriculaPeriodoVO() == null || impressaoContratoVO.getMatriculaPeriodoVO().getCodigo().equals(0)) {
	        		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	        	}
	        	
	            if (getTipoDeclaracao().equals("TU")  ||getTipoDeclaracao().equals("PF")) {
	                for (ImpressaoContratoVO impressaoContrato : getListaAlunoGerarContrato()) {
	                    if (impressaoContrato.getAlunoSelecionado()) {
	                    	impressaoContrato.setImpressaoPdf(Boolean.TRUE);
	                    	impressaoContrato.setFuncionarioPrincipalVO(this.getFuncionarioPrincipalVO());
	                    	impressaoContrato.setCargoFuncionarioPrincipal(getImpressaoContratoVO().getCargoFuncionarioPrincipal());	
	                    	impressaoContrato.setFuncionarioSecundarioVO(this.getFuncionarioSecundarioVO());
	                    	impressaoContrato.setCargoFuncionarioSecundario(getImpressaoContratoVO().getCargoFuncionarioSecundario());	
	                    	impressaoContrato.setImpressaoDoc(Boolean.FALSE);
	                    	impressaoContrato.setImpressaoPdf(Boolean.TRUE);
	                    	setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), impressaoContrato, getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado(), true));
	                        break;
	                    }
	                }
	            } else {
	            	setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado(), true));
	            	getImpressaoContratoVO().setListaPlanoEnsino(new ArrayList<PlanoEnsinoVO>());
	            }
			}
        	setImprimirContrato(false);
			setFazerDownload(true);			
		} catch (Exception e) {
			setFazerDownload(false);	
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
    }
    
    public void imprimirContratoDOC() {
    	try {
    		validarImpressaoContrato();
    		validarPeriodoInformadoMatriculaPeriodo();
    		getImpressaoContratoVO().setImpressaoPdf(false);
    		getImpressaoContratoVO().setImpressaoDoc(true);
    		getImpressaoContratoGravarVO().setImpressaoDoc(Boolean.TRUE);	
    		getImpressaoContratoGravarVO().setImpressaoPdf(Boolean.FALSE);	
    		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralPadraoSistema();			
			if(getTipoDeclaracao().equals("AL")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("TU")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("PS")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getInscricaoVO().getUnidadeEnsino().getCodigo());
			}else if(getTipoDeclaracao().equals("PF")){
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
				configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
			}
			getImpressaoContratoVO().setFuncionarioPrincipalVO(this.getFuncionarioPrincipalVO());
			if (this.getCargoFuncionarioPrincipal().getCodigo().intValue() > 0) {
				CargoVO cargo = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getImpressaoContratoVO().setCargoFuncionarioPrincipal(cargo);	
			}
			getImpressaoContratoVO().setFuncionarioSecundarioVO(this.getFuncionarioSecundarioVO());
			if (this.getCargoFuncionarioSecundario().getCodigo().intValue() > 0) {
				CargoVO cargo = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getImpressaoContratoVO().setCargoFuncionarioSecundario(cargo);
			}    		
			if (getTipoDeclaracao().equals("PS")) {
        		setImprimirContrato(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracaoProcSeletivo(getTextoPadraoDeclaracao(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoFinanceiroVO, getUsuarioLogado()));
        	} else {
        		if (getTipoDeclaracao().equals("TU") || getTipoDeclaracao().equals("PF")) {
	                for (ImpressaoContratoVO impressaoContrato : getListaAlunoGerarContrato()) {
	                    if (impressaoContrato.getAlunoSelecionado()) {
	                    	if (!impressaoContrato.getMatriculaPeriodoVO().getAno().equals("") || !impressaoContrato.getMatriculaPeriodoVO().getSemestre().equals("")) {
	                    		impressaoContrato.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(impressaoContrato.getMatriculaVO().getMatricula(), impressaoContrato.getMatriculaPeriodoVO().getAno(), impressaoContrato.getMatriculaPeriodoVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "ativo"));
	                    	} 
	                    	if (impressaoContrato.getMatriculaPeriodoVO() == null || impressaoContrato.getMatriculaPeriodoVO().getCodigo().equals(0)) {
	                    		impressaoContrato.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(impressaoContrato.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	                    	}
	                    	impressaoContrato.getMatriculaPeriodoVO().setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(impressaoContrato.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
	                    	impressaoContrato.setImpressaoDoc(Boolean.TRUE);	
	                    	impressaoContrato.setImpressaoPdf(Boolean.FALSE);	
	                    	String arquivo = getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), impressaoContrato, getImpressaoContratoGravarVO(), getTipoDeclaracao(), impressaoContrato.getTurmaVO(), impressaoContrato.getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado(), true);	                    	
	                    	setCaminhoRelatorio(arquivo);
	                    	setIsDownloadContrato(Uteis.isAtributoPreenchido(arquivo));	
	                    	break;
	                    }
	                }
        		} else {
        			if (!getImpressaoContratoVO().getMatriculaPeriodoVO().getAno().equals("") || !getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre().equals("")) {
                		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(impressaoContratoVO.getMatriculaVO().getMatricula(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "ativo"));
                	} 
                	if (impressaoContratoVO.getMatriculaPeriodoVO() == null || impressaoContratoVO.getMatriculaPeriodoVO().getCodigo().equals(0)) {
                		impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                	}
                	impressaoContratoVO.getMatriculaPeriodoVO().setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
                	String arquivo = getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), getImpressaoContratoVO(), getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getUsuarioLogado(), true);                	
                	setCaminhoRelatorio(arquivo);
                	setIsDownloadContrato(Uteis.isAtributoPreenchido(arquivo));
                	
        		}
        	}	
			
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				String textoHTML = (String) request.getSession().getAttribute("textoRelatorio");
				if(Uteis.isAtributoPreenchido(textoHTML)) {
					ArquivoHelper.criarArquivoDOC(textoHTML, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
					textoHTML = null;
				}
			setImprimirContrato(false);
			setFazerDownload(true);	
    	} catch (Exception e) {
    		setIsDownloadContrato(Boolean.FALSE);
			setImprimirContrato(false);
            setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
	public String getDownloadContrato() {
		try {
			if (getIsDownloadContrato()) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoVO().getNome());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getArquivoVO().getPastaBaseArquivo());
				context().getExternalContext().getSessionMap().put("deletarArquivo", Boolean.TRUE);
				return "location.href='../../DownloadSV'";
			}
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public void impressaoDeclaracaoContratoJaGerada() {
		ImpressaoDeclaracaoVO obj = (ImpressaoDeclaracaoVO) context().getExternalContext().getRequestMap().get("impressaoDeclaracaoItens");
    	try {
    		validarImpressaoContrato();
    		limparMensagem();
			setFazerDownload(false);
			setImprimirContrato(false);
			this.setCaminhoRelatorio("");
			ImpressaoContratoVO contrato = new ImpressaoContratoVO();
			ImpressaoContratoVO contratoGravar = new ImpressaoContratoVO();
			contrato.setMatriculaVO(obj.getMatricula());
			contratoGravar.setMatriculaVO(contrato.getMatriculaVO());
			contratoGravar.setGerarNovoArquivoAssinado(false);
			contrato.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(contrato.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if(Uteis.isAtributoPreenchido(obj.getRequerimentoVO())){
				contrato.setRequerimentoVO(obj.getRequerimentoVO());
	    		getFacadeFactory().getRequerimentoFacade().carregarDados(contrato.getRequerimentoVO(),NivelMontarDados.BASICO, null, getUsuarioLogado());	
			}
    		contrato.setDisciplinaVO(obj.getDisciplina());
    		contrato.setTurmaVO(obj.getTurma());
    		TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(obj.getTextoPadraoDeclaracao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if(texto.getTipoDesigneTextoEnum().isHtml()){
				contrato.setImpressaoPdf(false);
				contratoGravar.setImpressaoPdf(false);
			}else{
				contrato.setImpressaoPdf(true);
				contratoGravar.setImpressaoPdf(true);	
			}
			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(obj.getTextoPadraoDeclaracao().getCodigo(), contrato, contratoGravar, getTipoDeclaracao(), contrato.getTurmaVO(), contrato.getDisciplinaVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), true));			
			if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && !contrato.isImpressaoPdf()){
				setImprimirContrato(true);
				setFazerDownload(false);
			}
			if(Uteis.isAtributoPreenchido(getCaminhoRelatorio()) && contrato.isImpressaoPdf()){
				setFazerDownload(true);	
				setImprimirContrato(false);
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void visualizarImpressaoDeclaracao() {
    	ImpressaoContratoVO obj = (ImpressaoContratoVO) context().getExternalContext().getRequestMap().get("itensContrato");
    	try {
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarImpressaoDeclaracaoPorTipoDeclaracao(obj.getMatriculaVO().getMatricula(), getProfessor().getCodigo(), 0, getTipoDeclaracao(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void consultarImpressaoDeclaracaoPorMatricula() {
    	try {
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarImpressaoDeclaracaoPorTipoDeclaracao(getImpressaoContratoVO().getMatriculaVO().getMatricula(), getProfessor().getCodigo(), getTextoPadraoDeclaracao(), getTipoDeclaracao(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
    
    public void alterarImpressaoDeclaracao() {
    	try {
			getFacadeFactory().getImpressaoDeclaracaoFacade().alterarImpressaoDeclaracaoSituacaoEntregue(getListaImpressaoDeclaracaoVOs(), getTipoDeclaracao(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public String getContrato() {
        if (getImprimirContrato()) {
            return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545); RichFaces.$('panelMensagemQuantidadeImpressa').hide()";
        }else if(getFazerDownload()){
        	return getDownload();
        }
        return "";
    }
    
    public void limparDadosFuncionarioPrincipal() throws Exception {
        setFuncionarioPrincipalVO(new FuncionarioVO());
        setListaConsultaFuncionario(new ArrayList(0));
        setListaCargoFuncionarioPrincipal(new ArrayList(0));
    }

    public void limparDadosFuncionarioSecundario() throws Exception {
        setFuncionarioSecundarioVO(new FuncionarioVO());
        setListaConsultaFuncionario(new ArrayList(0));
        setListaCargoFuncionarioSecundario(new ArrayList(0));
    }

    public void limparDadosTipoDeclaracao() throws Exception {
        //if (getTipoDeclaracao().equals("AL")) {
            limparDadosAluno();
        //} else if (getTipoDeclaracao().equals("PR")) {
            limparDadosProfessor();
        //} else if (getTipoDeclaracao().equals("TU")) {
            limparDadosTurma();
            limparCampoProgramacaoFormatura();
        //}
    }

    public void limparDadosProfessor() throws Exception {
        setProfessor(new FuncionarioVO());
        setListaConsultaProfessor(new ArrayList(0));
        setImprimirContrato(false);
    }

    public void consultarFuncionario() {
        List objs = new ArrayList(0);
        try {
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(),
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(),
                        false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(),
                        false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU",
                        this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU",
                        this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            executarMetodoControle(ImpressaoDeclaracaoControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
    }

    public void consultarFuncionarioPorMatricula() throws Exception {
        try {
            setFuncionarioPrincipal(true);
            FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioPrincipalVO().getMatricula(),
                    getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (objFuncionario.getMatricula().equals("")) {
                setMensagemDetalhada("Funcionário não encontrado.");
            }
            this.setFuncionarioPrincipalVO(objFuncionario);
            montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioPrincipalVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setFuncionarioPrincipalVO(new FuncionarioVO());
            getListaCargoFuncionarioPrincipal().clear();
        }
    }

    public void consultarFuncionarioSecundarioPorMatricula() throws Exception {
        try {
            setFuncionarioPrincipal(false);
            FuncionarioVO objFuncionario = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getFuncionarioSecundarioVO().getMatricula(),
                    getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (objFuncionario.getMatricula().equals("")) {
                setMensagemDetalhada("Funcionário não encontrado.");
            }
            this.setFuncionarioSecundarioVO(objFuncionario);
            montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade().consultarCargoPorCodigoFuncionario(getFuncionarioSecundarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setFuncionarioSecundarioVO(new FuncionarioVO());
            getListaCargoFuncionarioSecundario().clear();
        }
    }

    public void selecionarFuncionario() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        FuncionarioVO objCompleto = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        try {
            if (isFuncionarioPrincipal()) {
                setFuncionarioPrincipalVO(objCompleto);
                montarComboCargoFuncionario(getFuncionarioPrincipalVO().getFuncionarioCargoVOs());
            } else {
                setFuncionarioSecundarioVO(objCompleto);
                montarComboCargoFuncionario(getFuncionarioSecundarioVO().getFuncionarioCargoVOs());
            }
        } finally {
            obj = null;
            objCompleto = null;
            setCampoConsultaFuncionario(null);
            setValorConsultaFuncionario(null);
            setListaConsultaFuncionario(null);
        }
    }

    public void montarComboCargoFuncionario(List cargos) throws Exception {
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        List<FuncionarioCargoVO> lista = cargos;
        for (FuncionarioCargoVO funcionarioCargoVO : lista) {
            selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(), funcionarioCargoVO.getCargo().getNome() + " - "
                    + funcionarioCargoVO.getUnidade().getNome()));
        }
        if (isFuncionarioPrincipal()) {
            setListaCargoFuncionarioPrincipal(selectItems);
        } else {
            setListaCargoFuncionarioSecundario(selectItems);
        }
    }
    
    
    public void selecionarRequerimento() {
		try {
			RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
			if(!obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())){
				throw new Exception("Não é possível imprimir um requerimento que não esteja com a situação " + SituacaoRequerimento.FINALIZADO_DEFERIDO.getDescricao());
			}
			obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			if (obj.getMatricula() == null || (!Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula()))) {
				throw new Exception("Só possivel realizar uma Impressão de Declaração de Requerimentos vinculados a Alunos!");
			}
			getImpressaoContratoGravarVO().setRequerimentoVO(obj);
			getImpressaoContratoVO().setRequerimentoVO(obj);
			getImpressaoContratoVO().setMatriculaVO(obj.getMatricula());
            getFacadeFactory().getImpressaoContratoFacade().consultarAlunoPorMatricula(getImpressaoContratoVO(), getUnidadeEnsinoLogado().getCodigo(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), getUsuarioLogado());
            getImpressaoContratoVO().setDisciplinaVO(getImpressaoContratoVO().getRequerimentoVO().getDisciplina());
            getImpressaoContratoVO().setTurmaVO(getImpressaoContratoVO().getMatriculaPeriodoVO().getTurma());
            consultarListaSelectItemTextoPadraoPorTipoRequerimento(getImpressaoContratoVO().getRequerimentoVO().getTipoRequerimento().getTipo(), getUnidadeEnsinoLogado().getCodigo());
            setTextoPadraoDeclaracao(getImpressaoContratoVO().getRequerimentoVO().getTipoRequerimento().getTextoPadrao().getCodigo());
			getListaConsultaRequerimento().clear();
			setCampoConsultaRequerimento("");
			setValorConsultaRequerimento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void limparDadosRequerimento() throws Exception {
        setImpressaoContratoGravarVO(new ImpressaoContratoVO());
        setImpressaoContratoVO(new ImpressaoContratoVO());
        setTextoPadraoDeclaracao(0);
        getListaSelectItemTipoTextoPadrao().clear();
        getListaConsultaRequerimento().clear();
		setCampoConsultaRequerimento("");
		setValorConsultaRequerimento("");
    }
    

    public void consultarProfessor() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaProfessor(), "PR", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaProfessor().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaProfessor(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaProfessor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarProfessor() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
        getFacadeFactory().getFuncionarioFacade().carregarDados(obj, getUsuarioLogado());
        setProfessor(obj);
        getImpressaoContratoVO().setProfessor(obj);
        consultarListaSelectItemTipoTextoPadrao(0);
        obj = null;
        setValorConsultaProfessor("");
        setCampoConsultaProfessor("");
        montarListaSelectItemUnidadeEnsino("");
        getListaConsultaProfessor().clear();
    }

    public List<SelectItem> getTipoConsultaComboProfessor() {
        if (tipoConsultaComboProfessor == null) {
            tipoConsultaComboProfessor = new ArrayList<SelectItem>();
            tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboProfessor.add(new SelectItem("matricula", "Matrícula"));
        }
        return tipoConsultaComboProfessor;
    }

    /**
     * @return the listaConsultaProfessor
     */
    public List getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList(0);
        }
        return listaConsultaProfessor;
    }

    /**
     * @param listaConsultaProfessor
     *            the listaConsultaProfessor to set
     */
    public void setListaConsultaProfessor(List listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;
    }

    /**
     * @return the campoConsultaProfessor
     */
    public String getCampoConsultaProfessor() {
        if (campoConsultaProfessor == null) {
            campoConsultaProfessor = "";
        }
        return campoConsultaProfessor;
    }

    /**
     * @param campoConsultaProfessor
     *            the campoConsultaProfessor to set
     */
    public void setCampoConsultaProfessor(String campoConsultaProfessor) {
        this.campoConsultaProfessor = campoConsultaProfessor;
    }

    /**
     * @return the valorConsultaProfessor
     */
    public String getValorConsultaProfessor() {
        if (valorConsultaProfessor == null) {
            valorConsultaProfessor = "";
        }
        return valorConsultaProfessor;
    }

    /**
     * @param valorConsultaProfessor
     *            the valorConsultaProfessor to set
     */
    public void setValorConsultaProfessor(String valorConsultaProfessor) {
        this.valorConsultaProfessor = valorConsultaProfessor;
    }

    public void controlarFuncionarioPrincipal() {
        setFuncionarioPrincipal(true);
    }

    public void controlarFuncionarioSecundario() {
        setFuncionarioPrincipal(false);
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));

        return itens;
    }

    public List getListaTipoDeclaracao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("AL", "Aluno"));
        itens.add(new SelectItem("PR", "Professor"));
        itens.add(new SelectItem("TU", "Turma"));
        itens.add(new SelectItem("PS", "Processo Seletivo"));		
        itens.add(new SelectItem("RE", "Requerimento"));		
        itens.add(new SelectItem("PF", "Programação de Formatura"));
        return itens;
    }
    
    public List<SelectItem> getTipoConsultaComboTipoRequerimentoComTextoPadrao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		List<TipoRequerimentoVO> lista = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoDoTipoRequerimentoComTextoPadraoComSituacaoAtiva();
		for (TipoRequerimentoVO tipoRequerimentoVO : lista) {
			itens.add(new SelectItem(tipoRequerimentoVO.getTipo(), TiposRequerimento.getDescricao(tipoRequerimentoVO.getTipo())));
		}
		return itens;
	}

    public List getTipoConsultaComboDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getListaSelectItemFuncionario() {
        if (listaSelectItemFuncionario == null) {
            listaSelectItemFuncionario = new ArrayList(0);
        }
        return listaSelectItemFuncionario;
    }

    public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
        this.listaSelectItemFuncionario = listaSelectItemFuncionario;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList(0);
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public List getListaCargoFuncionarioPrincipal() {
        if (listaCargoFuncionarioPrincipal == null) {
            listaCargoFuncionarioPrincipal = new ArrayList(0);
        }
        return listaCargoFuncionarioPrincipal;
    }

    public void setListaCargoFuncionarioPrincipal(List listaCargoFuncionarioPrincipal) {
        this.listaCargoFuncionarioPrincipal = listaCargoFuncionarioPrincipal;
    }

    public Boolean isFuncionarioPrincipal() {
        if (funcionarioPrincipal == null) {
            funcionarioPrincipal = false;
        }
        return funcionarioPrincipal;
    }

    public void setFuncionarioPrincipal(Boolean funcionarioPrincipal) {
        this.funcionarioPrincipal = funcionarioPrincipal;
    }

    public List getListaCargoFuncionarioSecundario() {
        if (listaCargoFuncionarioSecundario == null) {
            listaCargoFuncionarioSecundario = new ArrayList(0);
        }
        return listaCargoFuncionarioSecundario;
    }

    public void setListaCargoFuncionarioSecundario(List listaCargoFuncionarioSecundario) {
        this.listaCargoFuncionarioSecundario = listaCargoFuncionarioSecundario;
    }

    public FuncionarioVO getFuncionarioPrincipalVO() {
        if (funcionarioPrincipalVO == null) {
            funcionarioPrincipalVO = new FuncionarioVO();
        }
        return funcionarioPrincipalVO;
    }

    public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
        this.funcionarioPrincipalVO = funcionarioPrincipalVO;
    }

    public FuncionarioVO getFuncionarioSecundarioVO() {
        if (funcionarioSecundarioVO == null) {
            funcionarioSecundarioVO = new FuncionarioVO();
        }
        return funcionarioSecundarioVO;
    }

    public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
        this.funcionarioSecundarioVO = funcionarioSecundarioVO;
    }

    public CargoVO getCargoFuncionarioPrincipal() {
        if (cargoFuncionarioPrincipal == null) {
            cargoFuncionarioPrincipal = new CargoVO();
        }
        return cargoFuncionarioPrincipal;
    }

    public void setCargoFuncionarioPrincipal(CargoVO cargoFuncionarioPrincipal) {
        this.cargoFuncionarioPrincipal = cargoFuncionarioPrincipal;
    }

    public CargoVO getCargoFuncionarioSecundario() {
        if (cargoFuncionarioSecundario == null) {
            cargoFuncionarioSecundario = new CargoVO();
        }
        return cargoFuncionarioSecundario;
    }

    public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
        this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
    }
    
    public String getAbrirModalQuantitativo() {
    	if ((getTipoDeclaracao().equals("TU") || getTipoDeclaracao().equals("PF") ) && getAlunoSelecionado()) {
    		return "RichFaces.$('panelMensagemQuantidadeImpressa').show()";
    	} else if ((getTipoDeclaracao().equals("TU") || getTipoDeclaracao().equals("PF") ) && !getAlunoSelecionado()) {
    		return "";
    	} else if (getTipoDeclaracao().equals("PS") && getImpressaoContratoVO().getInscricaoVO().getCodigo().intValue() == 0) {
    		return "";
    	} else if (!getTipoDeclaracao().equals("TU") && !getTipoDeclaracao().equals("PF") && !getTipoDeclaracao().equals("PS") && !getTipoDeclaracao().equals("PR") && getImpressaoContratoVO().getMatriculaVO().getMatricula().equals("")) {
    		return "";
    	}
    	return "RichFaces.$('panelMensagemQuantidadeImpressa').show()";
    }

    public void verificarQuantidadeImpressa() {
        try {
        	if (getTipoDeclaracao().equals("PS")&& getImpressaoContratoVO().getInscricaoVO().getCodigo().intValue() == 0) {
        		throw new Exception("Informe a Inscrição para emitir a declaração desejada!"); 
        	}else if (getTipoDeclaracao().equals("RE")&& getImpressaoContratoVO().getRequerimentoVO().getCodigo().intValue() == 0) {
        		throw new Exception("Informe o Requerimento para emitir a declaração desejada!");
        	}else if(getTipoDeclaracao().equals("PF") &&  getProgramacaoFormaturaVO().getCodigo().intValue() == 0) {
        		throw new Exception("Informe a Programação Formatura para emitir a declaração desejada!");
        	} else if (!getTipoDeclaracao().equals("TU") && !getTipoDeclaracao().equals("PR") && !getTipoDeclaracao().equals("PF") && getImpressaoContratoVO().getMatriculaVO().getMatricula().equals("")) {
	        	throw new Exception("Informe a Matrícula para emitir a declaração desejada!");
        	} 
        	if (getTipoDeclaracao().equals("TU") || getTipoDeclaracao().equals("PF")) {
        		setAlunoSelecionado(Boolean.FALSE);
        		for (ImpressaoContratoVO impressaoVO : getListaAlunoGerarContrato()) {
					if (impressaoVO.getAlunoSelecionado()) {
						setImpressaoContratoGravarVO(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaTextoPadrao(impressaoVO.getMatriculaVO().getMatricula(), getTextoPadraoDeclaracao(), getImpressaoContratoVO().getProfessor().getCodigo()));
						getImpressaoContratoGravarVO().getMatriculaVO().setMatricula(impressaoVO.getMatriculaVO().getMatricula());						
						setAlunoSelecionado(Boolean.TRUE);
						break;
					}
				}
        		if (!getAlunoSelecionado()) {
        			throw new Exception("Nenhum aluno foi selecionado!");
        		}
        	} else {
        		setImpressaoContratoGravarVO(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaTextoPadrao(getImpressaoContratoVO().getMatriculaVO().getMatricula(), getTextoPadraoDeclaracao(), getImpressaoContratoVO().getProfessor().getCodigo()));
        		if (getAdicionarAssinatura()) {
        			getImpressaoContratoGravarVO().getDocumentoAssinado().setUnidadeEnsinoVO(getUnidadeEnsinoVO());
				}
        		getImpressaoContratoGravarVO().getMatriculaVO().setMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula());
        	}
        	if (getImpressaoContratoGravarVO().getNovoObj()) {
        		getImpressaoContratoGravarVO().setTextoPadraoDeclaracao(getTextoPadraoDeclaracao());
        		setMensagemQuantidadeImpressa("Primeira impressão da declaração. Deseja continuar a impressão?");
        	} else {
        		if (getImpressaoContratoGravarVO().getQuantidade().equals(1)) {
        			setMensagemQuantidadeImpressa("Já foi gerada " + getImpressaoContratoGravarVO().getQuantidade() + " impressão deste tipo de declaração. Deseja imprimir novamente?");
        		} else {
        			setMensagemQuantidadeImpressa("Já foram geradas " + getImpressaoContratoGravarVO().getQuantidade() + " impressões deste tipo de declaração. Deseja imprimir novamente?");
        		}
        	}
        	getImpressaoContratoGravarVO().setProvedorDeAssinaturaEnum(getProvedorDeAssinaturaEnum());
        } catch (Exception ex) {
            setImprimirContrato(false);
            setMensagemDetalhada("msg_erro", ex.getMessage());
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

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
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

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public String getTipoContrato() {
        if (tipoContrato == null) {
            tipoContrato = "";
        }
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Boolean getImprimirContrato() {
        if (imprimirContrato == null) {
            imprimirContrato = Boolean.FALSE;
        }
        return imprimirContrato;
    }

    public void setImprimirContrato(Boolean imprimirContrato) {
        this.imprimirContrato = imprimirContrato;
    }

    /**
     * @param listaSelectItemTipoTextoPadrao the listaSelectItemTipoTextoPadrao to set
     */
    public void setListaSelectItemTipoTextoPadrao(List listaSelectItemTipoTextoPadrao) {
        this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
    }

    /**
     * @return the textoPadraoDeclaracao
     */
    public Integer getTextoPadraoDeclaracao() {
        if (textoPadraoDeclaracao == null) {
            textoPadraoDeclaracao = 0;
        }
        return textoPadraoDeclaracao;
    }

    /**
     * @param textoPadraoDeclaracao the textoPadraoDeclaracao to set
     */
    public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
        this.textoPadraoDeclaracao = textoPadraoDeclaracao;
    }

    public String getMensagemQuantidadeImpressa() {
        if (mensagemQuantidadeImpressa == null) {
            mensagemQuantidadeImpressa = "";
        }
        return mensagemQuantidadeImpressa;
    }

    public void setMensagemQuantidadeImpressa(String mensagemQuantidadeImpressa) {
        this.mensagemQuantidadeImpressa = mensagemQuantidadeImpressa;
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

    /**
     * @return the adicionarAssinatura
     */
    public Boolean getAdicionarAssinatura() {
        if (adicionarAssinatura == null) {
            adicionarAssinatura = Boolean.FALSE;
        }
        return adicionarAssinatura;
    }

    /**
     * @param adicionarAssinatura the adicionarAssinatura to set
     */
    public void setAdicionarAssinatura(Boolean adicionarAssinatura) {
        this.adicionarAssinatura = adicionarAssinatura;
    }

    /**
     * @return the professor
     */
    public FuncionarioVO getProfessor() {
        if (professor == null) {
            professor = new FuncionarioVO();
        }
        return professor;
    }

    /**
     * @param professor the professor to set
     */
    public void setProfessor(FuncionarioVO professor) {
        this.professor = professor;
    }

    public String getTipoDeclaracao() {
        if (tipoDeclaracao == null) {
            tipoDeclaracao = "AL";
        }
        return tipoDeclaracao;
    }

    public void setTipoDeclaracao(String tipoDeclaracao) {
        this.tipoDeclaracao = tipoDeclaracao;
    }

  /*  public List<ImpressaoContratoVO> getListaAlunoTurmaVOs() {
        if (listaAlunoTurmaVOs == null) {
            listaAlunoTurmaVOs = new ArrayList<ImpressaoContratoVO>(0);
        }
        return listaAlunoTurmaVOs;
    }

    public void setListaAlunoTurmaVOs(List<ImpressaoContratoVO> listaAlunoTurmaVOs) {
        this.listaAlunoTurmaVOs = listaAlunoTurmaVOs;
    }*/

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
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

    public List<TurmaVO> getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList<TurmaVO>(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public boolean getApresentarDadosAluno() {
        return getTipoDeclaracao().equals("AL");
    }

    public boolean getApresentarDadosProfessor() {
        return getTipoDeclaracao().equals("PR");
    }
    
    public boolean getApresentarDadosRequerimento() {
    	return getTipoDeclaracao().equals("RE");
    }

    public boolean getApresentarDadosTurma() {
        return getTipoDeclaracao().equals("TU");
    }

  /*  public boolean getApresentarResultadoConsultaTurma() {
        return getTipoDeclaracao().equals("TU") && !getListaAlunoTurmaVOs().isEmpty();
    }*/

    /**
     * @return the campoConsultaDisciplinaTurma
     */
    public String getCampoConsultaDisciplinaTurma() {
        return campoConsultaDisciplinaTurma;
    }

    /**
     * @param campoConsultaDisciplinaTurma
     *            the campoConsultaDisciplinaTurma to set
     */
    public void setCampoConsultaDisciplinaTurma(String campoConsultaDisciplinaTurma) {
        this.campoConsultaDisciplinaTurma = campoConsultaDisciplinaTurma;
    }

    /**
     * @return the valorConsultaDisciplinaTurma
     */
    public String getValorConsultaDisciplinaTurma() {
        return valorConsultaDisciplinaTurma;
    }

    /**
     * @param valorConsultaDisciplinaTurma
     *            the valorConsultaDisciplinaTurma to set
     */
    public void setValorConsultaDisciplinaTurma(String valorConsultaDisciplinaTurma) {
        this.valorConsultaDisciplinaTurma = valorConsultaDisciplinaTurma;
    }

    /**
     * @return the listaConsultaDisciplinaTurma
     */
    public List getListaConsultaDisciplinaTurma() {
        return listaConsultaDisciplinaTurma;
    }

    /**
     * @param listaConsultaDisciplinaTurma
     *            the listaConsultaDisciplinaTurma to set
     */
    public void setListaConsultaDisciplinaTurma(List listaConsultaDisciplinaTurma) {
        this.listaConsultaDisciplinaTurma = listaConsultaDisciplinaTurma;
    }

	public Boolean getAlunoSelecionado() {
		if (alunoSelecionado == null) {
			alunoSelecionado = Boolean.FALSE;
		}
		return alunoSelecionado;
	}

	public void setAlunoSelecionado(Boolean alunoSelecionado) {
		this.alunoSelecionado = alunoSelecionado;
	}

	public List<ImpressaoDeclaracaoVO> getListaImpressaoDeclaracaoVOs() {
		if (listaImpressaoDeclaracaoVOs == null) {
			listaImpressaoDeclaracaoVOs = new ArrayList<ImpressaoDeclaracaoVO>(0);
		}
		return listaImpressaoDeclaracaoVOs;
	}

	public void setListaImpressaoDeclaracaoVOs(List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs) {
		this.listaImpressaoDeclaracaoVOs = listaImpressaoDeclaracaoVOs;
	}
	
	public Boolean getIsApresentarBotaoVisualizarImpressaoDeclaracao() {
		return ((getTipoDeclaracao().equals("AL") || getTipoDeclaracao().equals("RE")) && !getImpressaoContratoVO().getMatriculaVO().getMatricula().equals("") 
				|| getTipoDeclaracao().equals("PR") && !getProfessor().getCodigo().equals(0));
	}

	public Boolean getIsDownloadContrato() {
		if (isDownloadContrato == null) {
			isDownloadContrato = Boolean.FALSE;
		}
		return isDownloadContrato;
	}

	public void setIsDownloadContrato(Boolean isDownloadContrato) {
		this.isDownloadContrato = isDownloadContrato;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}
	
    public boolean getApresentarDadosProcSeletivo() {
    	return getTipoDeclaracao().equals("PS");
    }

    public void setListaConsultaInscricao(List<InscricaoVO> listaConsultaInscricao) {
        this.listaConsultaInscricao = listaConsultaInscricao;
    }

    public List<InscricaoVO> getListaConsultaInscricao() {
        if (listaConsultaInscricao == null) {
            listaConsultaInscricao = new ArrayList<InscricaoVO>(0);
        }
        return listaConsultaInscricao;
    }

    public List getTipoConsultaComboInscricao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Nome do Candidato"));
        itens.add(new SelectItem("codigo", "Número da Inscrição"));
        return itens;
    }


    public void setCampoConsultaInscricao(String campoConsultaInscricao) {
        this.campoConsultaInscricao = campoConsultaInscricao;
    }

    public String getCampoConsultaInscricao() {
        return campoConsultaInscricao;
    }

    public void setValorConsultaInscricao(String valorConsultaInscricao) {
        this.valorConsultaInscricao = valorConsultaInscricao;
    }    

    public String getValorConsultaInscricao() {
        return valorConsultaInscricao;
    }	
    
    public void validarImpressaoContrato() throws Exception {
    		TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getTextoPadraoDeclaracao(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getFacadeFactory().getImpressaoDeclaracaoFacade().validarDadosPermissaoImpressaoContrato(texto, getUsuarioLogado());
    }
    
    public void imprimirDeclaracaoAluno() {
    	ImpressaoContratoVO obj = (ImpressaoContratoVO) context().getExternalContext().getRequestMap().get("itensContrato");
    	getFacadeFactory().getImpressaoDeclaracaoFacade().realizarGarantiaMarcacaoUnicoCheck(getListaAlunoGerarContrato(), obj);
    	verificarQuantidadeImpressa();
    }
    

    
   
    
    public File getArquivoZip() {
		return arquivoZip;
	}

	public void setArquivoZip(File arquivoZip) {
		this.arquivoZip = arquivoZip;
	}
	
	public List<File> getListaArquivos() {
		if(listaArquivos == null ) {
			listaArquivos = new ArrayList<File>();
		}
		return listaArquivos;
	}
	
	
	
	
	public String getCampoConsultaProgramacaoFormatura() {
    	if(campoConsultaProgramacaoFormatura == null) {
    		campoConsultaProgramacaoFormatura = "codigo";
    	}
		return campoConsultaProgramacaoFormatura;
	}

	public void setCampoConsultaProgramacaoFormatura(String campoConsultaProgramacaoFormatura) {
		this.campoConsultaProgramacaoFormatura = campoConsultaProgramacaoFormatura;
	}
	
	
	public Boolean getMostrarSegundoCampoProgramacaoFormatura() {
        if (mostrarSegundoCampoProgramacaoFormatura == null) {
        	mostrarSegundoCampoProgramacaoFormatura = false;
        }
        return mostrarSegundoCampoProgramacaoFormatura;
    }

    /**
     * @param mostrarSegundoCampoProgramacaoFormatura
     *            the mostrarSegundoCampoProgramacaoFormatura to set
     */
    public void setMostrarSegundoCampoProgramacaoFormatura(Boolean mostrarSegundoCampoProgramacaoFormatura) {
        this.mostrarSegundoCampoProgramacaoFormatura = mostrarSegundoCampoProgramacaoFormatura;
    }
    
    
    public String getValorConsultaProgramacaoFormatura() {
    	if(valorConsultaProgramacaoFormatura == null) {
    		valorConsultaProgramacaoFormatura = "";
    	}
		return valorConsultaProgramacaoFormatura;
	}

	public void setValorConsultaProgramacaoFormatura(String valorConsultaProgramacaoFormatura) {
		this.valorConsultaProgramacaoFormatura = valorConsultaProgramacaoFormatura;
	}
    
	
	/**
     * @return the valorConsultaDataInicioProgramacaoFormatura
     */
    public Date getValorConsultaDataInicioProgramacaoFormatura() {
        return valorConsultaDataInicioProgramacaoFormatura;
    }

    /**
     * @param valorConsultaDataInicioProgramacaoFormatura
     *            the valorConsultaDataInicioProgramacaoFormatura to set
     */
    public void setValorConsultaDataInicioProgramacaoFormatura(Date valorConsultaDataInicioProgramacaoFormatura) {
        this.valorConsultaDataInicioProgramacaoFormatura = valorConsultaDataInicioProgramacaoFormatura;
    }

    /**
     * @return the valorConsultaDataFinalProgramacaoFormatura
     */
    public Date getValorConsultaDataFinalProgramacaoFormatura() {
        return valorConsultaDataFinalProgramacaoFormatura;
    }

    /**
     * @param valorConsultaDataFinalProgramacaoFormatura
     *            the valorConsultaDataFinalProgramacaoFormatura to set
     */
    public void setValorConsultaDataFinalProgramacaoFormatura(Date valorConsultaDataFinalProgramacaoFormatura) {
        this.valorConsultaDataFinalProgramacaoFormatura = valorConsultaDataFinalProgramacaoFormatura;
    }
    
    public String getFiltroAlunosPresentesColacaoGrau() {
		if(filtroAlunosPresentesColacaoGrau == null) {
			filtroAlunosPresentesColacaoGrau = "AM";
		}
	
		return filtroAlunosPresentesColacaoGrau;
	}

	public void setFiltroAlunosPresentesColacaoGrau(String filtroAlunosPresentesColacaoGrau) {
		this.filtroAlunosPresentesColacaoGrau = filtroAlunosPresentesColacaoGrau;
	}
	
	public List<ProgramacaoFormaturaVO> getListaConsultaProgramacaoFormatura() {
		if (listaConsultaProgramacaoFormatura == null) {
			listaConsultaProgramacaoFormatura = new ArrayList<ProgramacaoFormaturaVO>(0);
		}
		return listaConsultaProgramacaoFormatura;
	}

	public void setListaConsultaProgramacaoFormatura(List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormatura) {
		this.listaConsultaProgramacaoFormatura = listaConsultaProgramacaoFormatura;
	} 
	
	public Boolean getApresentarProgramacaoFormatura() {
		if(apresentarProgramacaoFormatura == null ) {
			apresentarProgramacaoFormatura = Boolean.FALSE;
		}
		return apresentarProgramacaoFormatura;
	}

	public void setApresentarProgramacaoFormatura(Boolean apresentarProgramacaoFormatura) {
		this.apresentarProgramacaoFormatura = apresentarProgramacaoFormatura;
	}
	
	

	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}

	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	
	 public boolean getIsFiltrarPorProgramacaoFormatura() {
	        return getTipoDeclaracao().equals("PF");
	    }

    
    
    public void mostrarSegundoCampoProgramacaoFormatura() {
        if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
                || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
        	setMostrarSegundoCampoProgramacaoFormatura(true);
        } else {
        	setMostrarSegundoCampoProgramacaoFormatura(false);
        }
        setValorConsultaProgramacaoFormatura("");
        getListaConsultaProgramacaoFormatura().clear();
    }
    
	
	 public void selecionarProgramacaoFormatura() throws Exception {		
	        ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
	        obj.setNovoObj(Boolean.FALSE);
	        setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
	        
			setApresentarProgramacaoFormatura(true);
			setValorConsultaProgramacaoFormatura("");
			setCampoConsultaProgramacaoFormatura("");
	        getListaConsultaProgramacaoFormatura().clear();
	        getListaAlunoGerarContrato().clear();
	        setApresentarResultadoConsultaMatriculaGerarContrato(Boolean.FALSE);
	        montarListaImpressaoContratoVOPorProgramacaoFormatura(getProgramacaoFormaturaVO());
	        consultarListaSelectItemTipoTextoPadrao(impressaoContratoVO.getMatriculaVO().getUnidadeEnsino().getCodigo());
       
	        
	    }  
	
	 
	 private void montarListaImpressaoContratoVOPorProgramacaoFormatura(ProgramacaoFormaturaVO programacaoFormaturaVO) throws Exception {	  
		 if(!programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs().isEmpty()) {
			 setApresentarResultadoConsultaMatriculaGerarContrato(Boolean.TRUE);
            for(ProgramacaoFormaturaAlunoVO obj : programacaoFormaturaVO.getProgramacaoFormaturaAlunoVOs()) {    
            	ImpressaoContratoVO objContrato = new ImpressaoContratoVO();
            	objContrato.setMatriculaVO(obj.getMatricula());
                getFacadeFactory().getImpressaoContratoFacade().consultarAlunoPorMatricula(objContrato, programacaoFormaturaVO.getUnidadeEnsino().getCodigo() , "","" , getUsuarioLogado());           	
                getListaAlunoGerarContrato().add(objContrato);
            }
		 }
		
	}

	public List getTipoConsultaComboProgramacaoFormatura() {
	        List itens = new ArrayList(0);
	        itens.add(new SelectItem("codigo", "Código"));
	        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
	        itens.add(new SelectItem("nomeCurso", "Curso"));
	        itens.add(new SelectItem("nomeTurno", "Turno"));
	        itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
	        itens.add(new SelectItem("matriculaMatricula", "Matricula"));
	        itens.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
	        itens.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
	        itens.add(new SelectItem("periodoCadastro", "Período Cadastro"));
	        itens.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
	        return itens;
	    }
	 
	  public List getComboFiltroAlunosPresentesColacaoGrau() {
	        List itens = new ArrayList(0);
	        itens.add(new SelectItem("SI", "Presentes"));
	        itens.add(new SelectItem("NO", "Ausentes"));
	        itens.add(new SelectItem("NI", "Não Informado"));
	        itens.add(new SelectItem("AM", "Ambos"));
	        return itens;
	    }
	  
	  public String consultarProgramacaoFormatura() {
	        try {
	            super.consultar();
	            List objs = new ArrayList(0);
				

		            if (getCampoConsultaProgramacaoFormatura().equals("codigo")) {
		                if (getValorConsultaProgramacaoFormatura().equals("")) {
		                    setValorConsultaProgramacaoFormatura("0");
		                }
		                if (getValorConsultaProgramacaoFormatura().trim() != null || !getValorConsultaProgramacaoFormatura().trim().isEmpty()) {
		                    Uteis.validarSomenteNumeroString(getValorConsultaProgramacaoFormatura().trim());
		                }
		                int valorInt = Integer.parseInt(getValorConsultaProgramacaoFormatura());
		                if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {               	
		                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		                }else {
		                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigoFiltroAlunosPresentesColacaoGrau(new Integer(valorInt), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		                }
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeUnidadeEnsino")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorProgramacaoUnidadeEnsino(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUnidadeEnsinoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getFiltroAlunosPresentesColacaoGrau(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeCurso")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCursoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeTurno")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurnoFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("identificadorTurmaTurma")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("matriculaMatricula")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), false,
		                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		                	objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatriculaFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false,
		                            Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("nomeUsuario")) {
		            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {            
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getValorConsultaProgramacaoFormatura(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		            	}else {
		            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuarioFiltroAlunosPresentesColacaoGrau(getValorConsultaProgramacaoFormatura(), getFiltroAlunosPresentesColacaoGrau(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()); 
		            	}
		            }
		            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento") || getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")
		                    || getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
		                objs = validarDataConsultaProgramacaoFormatura(objs);
		            }
		            setListaConsultaProgramacaoFormatura(objs);	           
		            setMensagemID("msg_dados_consultados");
		            return "consultar";

	        } catch (Exception e) {
	        	setListaConsultaProgramacaoFormatura(new ArrayList(0));
	            setMensagemDetalhada("msg_erro", e.getMessage());
	            return "consultar";
	        }
	    }
	    
	  
	  public boolean getApresentarResultadoConsultaProgramacaoFormatura() {
			if (this.getListaConsultaProgramacaoFormatura() == null || this.getListaConsultaProgramacaoFormatura().size() == 0) {
				return false;
			}
			return true;
		}
	  
	  public List validarDataConsultaProgramacaoFormatura(List objs) throws Exception {
	        if (getValorConsultaDataFinalProgramacaoFormatura() != null && getValorConsultaDataInicioProgramacaoFormatura() != null) {
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoRequerimento")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {          		            	
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimentoFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoColacaoGrau")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrauFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	            if (getCampoConsultaProgramacaoFormatura().equals("periodoCadastro")) {
	            	if(getFiltroAlunosPresentesColacaoGrau().equals("AM")) {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                        Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}else {
	            		objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastroFiltroAlunosPresentesColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicioProgramacaoFormatura(), 0, 0, 0),
	                            Uteis.getDateTime(getValorConsultaDataFinalProgramacaoFormatura(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), getFiltroAlunosPresentesColacaoGrau(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            	}
	            }
	        } else {
	            throw new ConsistirException("Por favor digite uma data válida.");
	        }

	        return objs;
	    }
	  
	  
		public void limparCampoProgramacaoFormatura() {
			setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
			getListaAlunoGerarContrato().clear();
			setApresentarListaErros(Boolean.FALSE);
			getListaAlunoGerarContratoErro().clear();
			setApresentarResultadoConsultaMatriculaGerarContrato(Boolean.FALSE);
			setApresentarProgramacaoFormatura(Boolean.FALSE);
			setImprimirContrato(false);
			limparMensagem();
			setMensagemID("msg_entre_dados");
		}
		
		

		public ProgressBarVO getProgressBarVO() {
			return progressBarVO;
		}

		public void setProgressBarVO(ProgressBarVO progressBarVO) {
			this.progressBarVO = progressBarVO;
		}
	
		
		
		public void imprimirPDFZipado() {
			try {
				validarImpressaoContratoLote();
				validarImpressaoContrato();
				limparMensagem();
				setFazerDownload(false);
				this.setCaminhoRelatorio("");
				getListaArquivos().clear();
				getListaAlunoGerarContratoErro().clear();
				registrarAtividadeUsuario(getUsuarioLogado(), "ImpressaoDeclaracaoControle", "Inicializando Geração de Impressão de Contrato", "Emitindo Relatório");
				setProgressBarVO(new ProgressBarVO());
				getProgressBarVO().setUsuarioVO(getUsuarioLogadoClone());
				getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
				getProgressBarVO().setUnidadeEnsinoVO(getUnidadeEnsinoLogado());
				getProgressBarVO().setCaminhoWebRelatorio(getCaminhoPastaWeb());
				getProgressBarVO().resetar();
				getProgressBarVO().iniciar(0l, getListaAlunoGerarContrato().size(), "Carregando Matriculas", true, this, "realizarImpressaoTextoPadraoEmLote");
				registrarAtividadeUsuario(getUsuarioLogado(), "ImpressaoDeclaracaoControle", "Finalizando Geração de Impressão de Contrato", "Emitindo Relatório");
			} catch (Exception e) {
				getProgressBarVO().setForcarEncerramento(Boolean.TRUE);
				setArquivoZip(null);
				setFazerDownload(Boolean.FALSE);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}

		private void validarImpressaoContratoLote() throws Exception {			
			if(getListaAlunoGerarContrato().isEmpty()) {			 
				 throw new Exception("Não existem dados a serem gerados .");
			}
			if(!Uteis.isAtributoPreenchido(getTextoPadraoDeclaracao()) ) {
				 throw new Exception("Necessário informar o campo Texto Padrão Declaração .");
			}
			if(getTipoDeclaracao().equals("PF")  && !Uteis.isAtributoPreenchido(getProgramacaoFormaturaVO())) {
				 throw new Exception("Necessário informar  Programação Formatura .");
			}
			if(getTipoDeclaracao().equals("TU")  && !Uteis.isAtributoPreenchido(getImpressaoContratoVO().getTurmaVO())) {
				throw new Exception("Necessário informar  Turma .");
			}
		}
		

		
		public void removerImpressaoDeclaracao() {
			
			ImpressaoContratoVO obj = (ImpressaoContratoVO) context().getExternalContext().getRequestMap().get("itensContrato");
			Iterator it = getListaAlunoGerarContrato().iterator();
			while(it.hasNext()) {
				ImpressaoContratoVO exp = (ImpressaoContratoVO) it.next();
				if(exp.getMatriculaVO().getMatricula().equals(obj.getMatriculaVO().getMatricula())) {
					it.remove();
				}
				
			}
			
			
		}
		
		
	

		public Boolean getApresentarResultadoConsultaMatriculaGerarContrato() {
			if(apresentarResultadoConsultaMatriculaGerarContrato == null ) {
				apresentarResultadoConsultaMatriculaGerarContrato = Boolean.FALSE;
			}
			return apresentarResultadoConsultaMatriculaGerarContrato;
		}

		public void setApresentarResultadoConsultaMatriculaGerarContrato(
				Boolean apresentarResultadoConsultaMatriculaGerarContrato) {
			this.apresentarResultadoConsultaMatriculaGerarContrato = apresentarResultadoConsultaMatriculaGerarContrato;
		}
		
		
		 
		 
		

		public Boolean getPainelALunosGerarAberto() {
			if(painelALunosGerarAberto == null ) {
				painelALunosGerarAberto = Boolean.TRUE;
			}
			return painelALunosGerarAberto;
		}

		public void setPainelALunosGerarAberto(Boolean painelALunosGerarAberto) {
			this.painelALunosGerarAberto = painelALunosGerarAberto;
		}

		public List<ImpressaoContratoVO> getListaAlunoGerarContrato() {
			if(listaAlunoGerarContrato == null ) {
				listaAlunoGerarContrato =   new ArrayList<ImpressaoContratoVO>(0);
			}
			return listaAlunoGerarContrato;
		}

		public void setListaAlunoGerarContrato(List<ImpressaoContratoVO> listaAlunoGerarContrato) {
			this.listaAlunoGerarContrato = listaAlunoGerarContrato;
		}
		
		
		public void realizarImpressaoTextoPadraoEmLote() {
			try {
				ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
				ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralPadraoSistema();
				CargoVO cargoFuncionarioPrimario = new CargoVO();
				CargoVO cargoFuncionarioSecundario = new CargoVO();
				if (this.getCargoFuncionarioPrincipal().getCodigo().intValue() > 0) {
					cargoFuncionarioPrimario = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioPrincipal().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getUsuarioVO());
				}
				if (this.getCargoFuncionarioSecundario().getCodigo().intValue() > 0) {
					cargoFuncionarioSecundario = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(this.getCargoFuncionarioSecundario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getUsuarioVO());
				}
				TextoPadraoDeclaracaoVO texto = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(getTextoPadraoDeclaracao(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getProgressBarVO().getUsuarioVO());
				if (getTipoDeclaracao().equals("TU")) {
					configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
					configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo());
					validarNecessidadeAssinatura(getImpressaoContratoVO().getTurmaVO().getUnidadeEnsino().getCodigo(), texto, getProgressBarVO().getUsuarioVO());
				} else if (getTipoDeclaracao().equals("PF")) {
					configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
					configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo());
					validarNecessidadeAssinatura(getProgramacaoFormaturaVO().getUnidadeEnsino().getCodigo(), texto, getProgressBarVO().getUsuarioVO());
				}
				for (ImpressaoContratoVO impressaoContratoGerar : getListaAlunoGerarContrato()) {
					try {
						getProgressBarVO().setStatus("Processando Matricula " + (getProgressBarVO().getProgresso() + 1) + " de " + (getProgressBarVO().getMaxValue()) + " ");
						impressaoContratoGerar.setImpressaoDoc(false);
						impressaoContratoGerar.setGerarNovoArquivoAssinado(texto.getAssinarDigitalmenteTextoPadrao() && getAdicionarAssinatura());
						impressaoContratoGerar.setImpressaoPdf(Boolean.TRUE);
						impressaoContratoGerar.setFuncionarioPrincipalVO(this.getFuncionarioPrincipalVO());
						impressaoContratoGerar.setCargoFuncionarioPrincipal(cargoFuncionarioPrimario);
						impressaoContratoGerar.setFuncionarioSecundarioVO(this.getFuncionarioSecundarioVO());
						impressaoContratoGerar.setCargoFuncionarioSecundario(cargoFuncionarioSecundario);
						impressaoContratoGerar.setObservacao(getImpressaoContratoVO().getObservacao());
						impressaoContratoGerar.setProgramacaoFormaturaVO(getProgramacaoFormaturaVO());
						getImpressaoContratoGravarVO().setImpressaoPdf(true);
						getImpressaoContratoGravarVO().setGerarNovoArquivoAssinado(texto.getAssinarDigitalmenteTextoPadrao() && getAdicionarAssinatura());
						getImpressaoContratoGravarVO().setMatriculaVO(impressaoContratoGerar.getMatriculaVO());
						if (!getImpressaoContratoVO().getMatriculaPeriodoVO().getAno().equals("") || !getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre().equals("")) {
							impressaoContratoGerar.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(impressaoContratoVO.getMatriculaVO().getMatricula(), getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(), getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), 0, "", false, Uteis.NIVELMONTARDADOS_TODOS, getProgressBarVO().getUsuarioVO(), ""));
							impressaoContratoGerar.getMatriculaPeriodoVO().setExpedicaoDiplomaVO(getFacadeFactory().getExpedicaoDiplomaFacade().consultarPorMatricula(getImpressaoContratoVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getProgressBarVO().getUsuarioVO()));
						}
						if (impressaoContratoGerar.getMatriculaPeriodoVO() == null || impressaoContratoGerar.getMatriculaPeriodoVO().getCodigo().equals(0)) {
							impressaoContratoGerar.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatriculaOrdenandoPorAnoSemestrePeriodoLetivo(impressaoContratoVO.getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getProgressBarVO().getUsuarioVO()));
						}
						String caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(getTextoPadraoDeclaracao(), impressaoContratoGerar, getImpressaoContratoGravarVO(), getTipoDeclaracao(), getImpressaoContratoVO().getTurmaVO(), getImpressaoContratoVO().getDisciplinaVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, getProgressBarVO().getUsuarioVO(), impressaoContratoGerar.getGerarNovoArquivoAssinado());
						ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(impressaoContratoGerar.getMatriculaVO().getUnidadeEnsino().getCodigo(), getProgressBarVO().getUsuarioVO());
						/*
						 * if( configGEDVO.getProvedorDeAssinaturaEnum().isProvedorCertisign() &&
						 * texto.getAssinarDigitalmenteTextoPadrao() &&
						 * texto.getTipoDesigneTextoEnum().isPdf()) { DocumentoAssinadoVO da =
						 * impressaoContratoGerar.getDocumentoAssinado() ; ConfiguracaoGeralSistemaVO
						 * config =
						 * getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(da.getUnidadeEnsinoVO()
						 * .getCodigo()); getFacadeFactory().getDocumentoAssinadoFacade().
						 * realizarDownloadArquivoProvedorCertisign(da, config,
						 * getProgressBarVO().getUsuarioVO()); getListaArquivos().add(new
						 * File(config.getLocalUploadArquivoFixo() + File.separator +
						 * da.getArquivo().getPastaBaseArquivo() + File.separator +
						 * da.getArquivo().getNome())); }else {
						 */
						File file = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + caminhoRelatorio);
						if (file.exists()) {
							getListaArquivos().add(file);
						}
					} catch (Exception e) {
						impressaoContratoGerar.setPossuiErro(Boolean.TRUE);
						impressaoContratoGerar.setMotivoErro(e.getMessage());
						getListaAlunoGerarContratoErro().add(impressaoContratoGerar);
					} finally {
						getProgressBarVO().incrementar();

					}
				}
				if (getListaArquivos().size() >= 1) {
					setArquivoZip(new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator + new Date().getTime() + ".zip"));
					getFacadeFactory().getArquivoHelper().zip(getListaArquivos().toArray(new File[getListaArquivos().size()]), getArquivoZip());
				}
				if (!getListaAlunoGerarContratoErro().isEmpty()) {
					setApresentarListaErros(Boolean.TRUE);
				}
				setFazerDownload(false);
				setImprimirContrato(false);
				getProgressBarVO().incrementar();
				getProgressBarVO().setForcarEncerramento(true);
			} catch (Exception e) {
				setArquivoZip(null);
				getProgressBarVO().setForcarEncerramento(true);
				setFazerDownload(false);
				setMensagemDetalhada("msg_erro", e.getMessage());

			}
		}
		 
		 
		 public String getDownloadContratoCompactadas() {
				try {
					if (getArquivoZip() != null && !getArquivoZip().getAbsolutePath().equals("")) {
						context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoZip().getName());
						context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getArquivoZip().getParent());
						context().getExternalContext().getSessionMap().put("deletarArquivo", true);
						return "location.href='../../DownloadSV'";
					} else {
						return "";
					}
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
					return "";
				}
			}
		 
			public void validarNecessidadeAssinatura(Integer unidadeEnsino, TextoPadraoDeclaracaoVO texto, UsuarioVO usuarioLogado) throws Exception {
				getFacadeFactory().getImpressaoDeclaracaoFacade().validarDadosPermissaoImpressaoContrato(texto, getUsuarioLogado());
				if (texto.getAssinarDigitalmenteTextoPadrao() && texto.getTipoDesigneTextoEnum().isPdf()) {
					ConfiguracaoGEDVO configGEDVO = getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(unidadeEnsino, usuarioLogado);
					if (Uteis.isAtributoPreenchido(configGEDVO) && configGEDVO.getConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.DECLARACAO).getAssinarDocumento()) {
						if (getFuncionarioPrincipalVO().getCodigo() == null || getFuncionarioPrincipalVO().getCodigo() == 0) {
							throw new Exception("O Funcionario 1 deve ser informado para geração do relatório.");
						}
						if (getFuncionarioSecundarioVO().getCodigo() == null || getFuncionarioSecundarioVO().getCodigo() == 0) {
							throw new Exception("O Funcionario 2 deve ser informado para geração do relatório.");
						}
						if (getCargoFuncionarioPrincipal().getCodigo() == null || getCargoFuncionarioPrincipal().getCodigo() == 0) {
							throw new Exception("O Cargo do Funcionario 1 deve ser informado para geração do relatório.");
						}
						if (getCargoFuncionarioSecundario().getCodigo() == null || getCargoFuncionarioSecundario().getCodigo() == 0) {
							throw new Exception("O Cargo do Funcionario 2 deve ser informado para geração do relatório.");
						}

					}
				}
			}

		public List<ImpressaoContratoVO> getListaAlunoGerarContratoErro() {
			if(listaAlunoGerarContratoErro == null ) {
				listaAlunoGerarContratoErro =  new ArrayList<ImpressaoContratoVO>(0);
			}
			return listaAlunoGerarContratoErro;
		}

		public void setListaAlunoGerarContratoErro(List<ImpressaoContratoVO> listaAlunoGerarContratoErro) {
			this.listaAlunoGerarContratoErro = listaAlunoGerarContratoErro;
		}

		public Boolean getApresentarListaErros() {
			if(apresentarListaErros == null ) {
				apresentarListaErros = Boolean.FALSE;
			}
			return apresentarListaErros;
		}

		public void setApresentarListaErros(Boolean apresentarListaErros) {
			this.apresentarListaErros = apresentarListaErros;
		}

		public Boolean getPainelALunosGerarErroAberto() {
			if(painelALunosGerarErroAberto == null ) {
				painelALunosGerarErroAberto = Boolean.FALSE; 
			}
			return painelALunosGerarErroAberto;
		}

		public void setPainelALunosGerarErroAberto(Boolean painelALunosGerarErroAberto) {
			this.painelALunosGerarErroAberto = painelALunosGerarErroAberto;
		}
		
	public void consultarMatriculaPeriodoPorAnoSemestre() {
		try {
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(getImpressaoContratoVO().getMatriculaVO().getMatricula(),
							getImpressaoContratoVO().getMatriculaPeriodoVO().getAno(),getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre(), 0, "", false,	Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "");
			if (Uteis.isAtributoPreenchido(matriculaPeriodo)) {
				getImpressaoContratoVO().setMatriculaPeriodoVO(matriculaPeriodo);
				getImpressaoContratoGravarVO().setMatriculaPeriodoVO(matriculaPeriodo);
			} else {
				throw new Exception("Não foi encontrado uma Matrícula Período para esse filtro de ano/semestre");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarPeriodoInformadoMatriculaPeriodo() throws Exception {
		if (getApresentarAno() && Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO()) && !Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getAno())) {
			throw new Exception("Deve ser informado o ano.");
		}
		if (getApresentarSemestre() && Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO()) && !Uteis.isAtributoPreenchido(getImpressaoContratoVO().getMatriculaPeriodoVO().getSemestre())) {
			throw new Exception("Deve ser informado o semestre");
		}
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			getListaSelectItemUnidadeEnsino().clear();
			if (!resultadoConsulta.isEmpty()) {
				for (UnidadeEnsinoVO obj : resultadoConsulta) {
					if (Uteis.isAtributoPreenchido(obj)) {
						getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List consultarUnidadeEnsinoPorNome(String nomePrm) {
		List lista = new ArrayList<>();
		try {
			lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm,
					getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado());
		} catch (Exception e) {
		}
		return lista;
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
}
