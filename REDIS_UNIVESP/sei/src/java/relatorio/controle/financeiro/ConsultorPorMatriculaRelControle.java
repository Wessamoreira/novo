package relatorio.controle.financeiro;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.crm.ConsultorPorMatriculaRelVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.administrativo.Funcionario;
import negocio.facade.jdbc.administrativo.PainelGestorMonitoramentoCRM;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ImpostosRetidosContaReceberRelVO;
import controle.academico.ExpedicaoDiplomaControle;

@Controller("ConsultorPorMatriculaRelControle")
@Scope("viewScope")
@Lazy
public class ConsultorPorMatriculaRelControle extends SuperControleRelatorio {

	private UnidadeEnsinoVO unidadeEnsino;
	private Date dataInicio;	
	private Date dataFim;	
	private List listaSelectItemUnidadeEnsino;
	private FuncionarioVO consultor;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private String htmlRelatorioConsultorPorMatricula;
	private String situacaoMatricula;
	private String situacaoProspectAtIn;
	private List listaSelectItemFuncionario;
	private List listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private boolean matRecebida = false;
	private boolean matAReceber= false;
	private boolean matVencida = false;
	private boolean matAVencer = false;
	private boolean matNaoGerada = false;
	private boolean ativa = true;
	private boolean preMatricula = false;
	private boolean cancelada = false;
	private boolean excluida = false;
	private boolean transferidaDe = false;
	private boolean transferidaPara = false;
	private boolean considerarConsultorVinculadoProspect;
	private String situacaoAlunoCurso;
	private String cursoApresentar;
	private String unidadeEnsinoApresentar;
	Map<String, Object> mapaResultado = new HashMap<String, Object>();
	public List<ConsultorPorMatriculaRelVO> listaRelatorioAnalitico;	
	private Date dataInicioPagamentoMatricula;	
	private Date dataFimPagamentoMatricula;	
	private boolean considerarApenasTurmaComAlunosMatriculados;
	private boolean desabilitarSelecionarUnidadeEnsinoCurso = false;
	private boolean trazerAlunosSemTutor;
	private String turmaApresentar;
	private List<TurmaVO> turmaVOs;
	private Boolean marcarTodasTurmas;
	private boolean apresentarSumarioComTotalizadorPorConsultor;
	private List listaNivelEducacional;
	private String tipoNivelEducacional;
	private boolean trazerAlunosBolsistas;

	private static final long serialVersionUID = 1L;

	public ConsultorPorMatriculaRelControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
	}

	public String consultarDadosMonitoramentoConsultorPorMatricula() {
		try {
			setHtmlRelatorioConsultorPorMatricula("");
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}

	public String consultarDadosMonitoramentoConsultorPorMatriculaHashMap() {
		try {
			setHtmlRelatorioConsultorPorMatricula("");

			mapaResultado = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().consultarDadosPainelGestorMonitoramentoConsultorPorMatricula(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()),obterListaCursoSelecionado(getCursoVOs()), getDataInicio(), getDataFim(), obterListaTurmaSelecionada(turmaVOs), getConsultor(), 
					getMatRecebida(), getMatAReceber(), getMatVencida(), getMatAVencer(), getExcluida(), getTransferidaDe(), getTransferidaPara(),
					getSituacaoProspectAtIn(),considerarConsultorVinculadoProspect, getFiltroRelatorioAcademicoVO(), getSituacaoAlunoCurso(), getListaRelatorioAnalitico(), getDataInicioPagamentoMatricula(), getDataFimPagamentoMatricula(), isConsiderarApenasTurmaComAlunosMatriculados(), isTrazerAlunosSemTutor(), getMatNaoGerada(), isTrazerAlunosBolsistas());
            		

			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());			
			return "";
		}
	}
	
	public void imprimirRelatorioTela() {
			
			try {
				validarDados();
				consultarDadosMonitoramentoConsultorPorMatriculaHashMap();
				
				if(mapaResultado.containsKey("html")){
					setHtmlRelatorioConsultorPorMatricula(mapaResultado.get("html").toString());
					setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
				}else if(mapaResultado.containsKey("semRegistros")){
					setMensagemID("msg_relatorio_sem_dados", Uteis.SUCESSO);
				}
				gravarDadosPadroesEmissaoRelatorio();
				getListaRelatorioAnalitico().clear();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			

	}
	
	public void imprimirPDF() {

	        try {
	        	validarDados();
	        	consultarDadosMonitoramentoConsultorPorMatriculaHashMap();
	        	if(!criarLista().isEmpty()) {
		            String titulo = "Consultor Por Matricula";
		            String design = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().designRelatorio();
		            montarDadosDoFiltroRelatorio();
		            getSuperParametroRelVO().setTituloRelatorio(titulo);
		            getSuperParametroRelVO().setNomeDesignIreport(design);
		            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
		            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
		            getSuperParametroRelVO().setListaObjetos(criarLista());
		            adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
		            getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().caminhoSubReport());
		            
		            realizarImpressaoRelatorio();
		            
		            setMensagemID("msg_relatorio_ok");
		            gravarDadosPadroesEmissaoRelatorio();
		            //setMarcarTodasUnidadeEnsino(true);
					//marcarTodasUnidadesEnsinoAction();
		            getListaRelatorioAnalitico().clear();
	        	}
	        	else {
					setMensagemID("msg_relatorio_sem_dados");
					setFazerDownload(false);
				}
				
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        } finally {
	        	removerControleMemoriaTela("ConsultorPorMatriculaRelControle");
//	        	removerControleMemoriaFlashTela("ConsultorPorMatriculaRelControle");
	        }

	    }
		
	public void imprimirPDFAnalitico() {
		try {
			validarDados();
			consultarDadosMonitoramentoConsultorPorMatriculaHashMap();
			if(!getListaRelatorioAnalitico().isEmpty()) {
				montarListaTotalizadorConsultor();
				String titulo = "Consultor Por Matricula Analítico";
				String design = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().designRelatorioAnalitico();
				montarDadosDoFiltroRelatorio();
				getSuperParametroRelVO().adicionarParametro("tipoRelatorio", "pdf");
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
	//			getSuperParametroRelVO().setListaObjetos(criarLista());
				getSuperParametroRelVO().setListaObjetos(getListaRelatorioAnalitico());
				adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().caminhoSubReport());
				getSuperParametroRelVO().adicionarParametro("apresentarSumarioComTotalizadorPorConsultor", isApresentarSumarioComTotalizadorPorConsultor());
	
				realizarImpressaoRelatorio();
				
				setMensagemID("msg_relatorio_ok");
				gravarDadosPadroesEmissaoRelatorio();
				//setMarcarTodasUnidadeEnsino(true);
				//marcarTodasUnidadesEnsinoAction();
				getListaRelatorioAnalitico().clear();
			}
        	else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			removerControleMemoriaTela("ConsultorPorMatriculaRelControle");
		}
		
	}
	
		public void imprimirExcel() {
	        try {
	        	validarDados();
	        	consultarDadosMonitoramentoConsultorPorMatriculaHashMap();
	        	if(!criarLista().isEmpty()) {
		            String titulo = "Consultor Por Matricula";
		            String design = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().designRelatorioExcel();
		            montarDadosDoFiltroRelatorio();	            
		            getSuperParametroRelVO().setTituloRelatorio(titulo);
		            getSuperParametroRelVO().setNomeDesignIreport(design);
		            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
		            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
		            getSuperParametroRelVO().setListaObjetos(criarLista());
		            adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
		            getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().caminhoSubReport());
		            
		            realizarImpressaoRelatorio();
		            
		            setMensagemID("msg_relatorio_ok");
		            gravarDadosPadroesEmissaoRelatorio();
		           // setMarcarTodasUnidadeEnsino(true);
					//marcarTodasUnidadesEnsinoAction();
		            getListaRelatorioAnalitico().clear();
	        	}
	        	else {
					setMensagemID("msg_relatorio_sem_dados");
					setFazerDownload(false);
				}
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        } finally {
	        	removerControleMemoriaTela("ConsultorPorMatriculaRelControle");
	        }
	    }

		public void imprimirExcelAnalitico() {
			try {
				validarDados();
				consultarDadosMonitoramentoConsultorPorMatriculaHashMap();
				if(!getListaRelatorioAnalitico().isEmpty()) {
					montarListaTotalizadorConsultor();
					String titulo = "Consultor Por Matricula Analítico"; 
					String design = getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().designRelatorioExcelAnalitico();
					montarDadosDoFiltroRelatorio();
					getSuperParametroRelVO().adicionarParametro("tipoRelatorio", "excel");
					getSuperParametroRelVO().setTituloRelatorio(titulo);
					getSuperParametroRelVO().setNomeDesignIreport(design);
					getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
					getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
					getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
					getSuperParametroRelVO().setListaObjetos(listaRelatorioAnalitico);
					adicionarFiltroSituacaoAcademica(getSuperParametroRelVO(), getFiltroRelatorioAcademicoVO());
					getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getPainelGestorMonitoramentoCRMFacade().caminhoSubReport());
					getSuperParametroRelVO().adicionarParametro("apresentarSumarioComTotalizadorPorConsultor", isApresentarSumarioComTotalizadorPorConsultor());								
					
					realizarImpressaoRelatorio();
					
					setMensagemID("msg_relatorio_ok");
					gravarDadosPadroesEmissaoRelatorio();
					//setMarcarTodasUnidadeEnsino(true);
					//marcarTodasUnidadesEnsinoAction();
					getListaRelatorioAnalitico().clear();
				}
	        	else {
					setMensagemID("msg_relatorio_sem_dados");
					setFazerDownload(false);
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				removerControleMemoriaTela("ConsultorPorMatriculaRelControle");
			}
		}
		
		public void montarDadosDoFiltroRelatorio() {
	    	getSuperParametroRelVO().adicionarParametro("unidadeensino", getUnidadeEnsinoApresentar());
	    	getSuperParametroRelVO().adicionarParametro("curso", getCursoApresentar());
	    	getSuperParametroRelVO().adicionarParametro("turma", getTurmaApresentar());
	    	getSuperParametroRelVO().adicionarParametro("consultor", getConsultor().getPessoa().getNome());
	    	getSuperParametroRelVO().adicionarParametro("situacaoalunocurso", getSituacaoAlunoCurso());
	    	
	    	getSuperParametroRelVO().adicionarParametro("filtroFinanceiraMatAReceber", getMatAReceber());
	    	getSuperParametroRelVO().adicionarParametro("filtroFinanceiraMatAVencer", getMatAVencer());
	    	getSuperParametroRelVO().adicionarParametro("filtroFinanceiraMatRecebida", getMatRecebida());
	    	getSuperParametroRelVO().adicionarParametro("filtroFinanceiraMatVencida", getMatVencida());
	    	getSuperParametroRelVO().adicionarParametro("filtroFinanceiraMatNaoGerada", getMatNaoGerada());
	    	getSuperParametroRelVO().adicionarParametro("formaingresso", getFiltroRelatorioAcademicoVO().getFormaIngresso().getDescricao());
		}
		
		public boolean getIsApresentarBotaoImprimir(){
			/*if(mapaResultado.containsKey("crossTable")){
				return true;
			}*/
			return false;
		}
		
		public List<ConsultorPorMatriculaRelVO> criarLista(){
			List<ConsultorPorMatriculaRelVO> list = new ArrayList<ConsultorPorMatriculaRelVO>(0);
	        ConsultorPorMatriculaRelVO consultorPorMatriculaRelVO = new ConsultorPorMatriculaRelVO();
	        ArrayList<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>();
	        
	        if(mapaResultado.containsKey("crossTable")) {
		        crosstabVOs.addAll((Collection<? extends CrosstabVO>) mapaResultado.get("crossTable"));
		        
		        consultorPorMatriculaRelVO.getCrosstabVOs().addAll(crosstabVOs);
		        list.add(consultorPorMatriculaRelVO);
	        }
	         
	        return list;
		}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemNivelEducacionalCurso();
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getUnidadeEnsinoLogado().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				return;
			}
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
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
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsino().getCodigo(),
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsino().getCodigo(),
                        false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsino().getCodigo(),
                        false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU",
                        this.getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU",
                        this.getUnidadeEnsino().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objs = null;
        }
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

    public void selecionarFuncionario() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        setHtmlRelatorioConsultorPorMatricula("");
        try {
            setConsultor(obj);
        } finally {

        }
    }
    
    public void limparFuncionario() throws Exception {
        setConsultor(new FuncionarioVO());
    }

	public void limparCurso() throws Exception {
		try {
			setCursoVO(null);
			setTurmaVO(null);
		} catch (Exception e) {
		}
	}

	public void limparConsultor() throws Exception {
		try {
			setConsultor(null);
		} catch (Exception e) {
		}
	}
	
	public void limparTurma() {
		try {
			setTurmaVO(null);
			setCursoVO(null);
			setUnidadeEnsino(null);
			setCursoApresentar(null);
			setUnidadeEnsinoApresentar(null);
			setDesabilitarSelecionarUnidadeEnsinoCurso(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void consultarTurma() {
		try {
			// if (getUnidadeEnsinoVO().getCodigo() == 0) {
			// throw new Exception("Informe a Unidade de Ensino.");
			// }
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setHtmlRelatorioConsultorPorMatricula("");
			setTurmaVO(obj);	
			
			setCursoVO(obj.getCurso());
			//cursoVO.setFiltrarCursoVO(true);
			StringBuilder curso = new StringBuilder();		
			curso.append(cursoVO.getCodigo()).append(" - ");
			curso.append(cursoVO.getNome()).append("; ");
			setCursoApresentar(curso.toString());
			
			for (CursoVO cursoVO : getCursoVOs()) {
				if(cursoVO.equals(this.cursoVO)) {
					cursoVO.setFiltrarCursoVO(true);
				}
				else {
					cursoVO.setFiltrarCursoVO(false);
				}
			}
			
			setUnidadeEnsino(obj.getUnidadeEnsino());
			//unidadeEnsino.setFiltrarUnidadeEnsino(true);			
			StringBuilder unidade = new StringBuilder();
			unidade.append(unidadeEnsino.getNome().trim()).append("; ");
			setUnidadeEnsinoApresentar(unidade.toString());
			
			for (UnidadeEnsinoVO unidadeEnsinoVO : getUnidadeEnsinoVOs()) {
				if(unidadeEnsinoVO.equals(unidadeEnsino)) {
					unidadeEnsinoVO.setFiltrarUnidadeEnsino(true);
				}
				else {
					unidadeEnsinoVO.setFiltrarUnidadeEnsino(false);
				}
					
			}
			
			setDesabilitarSelecionarUnidadeEnsinoCurso(true);
			
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setHtmlRelatorioConsultorPorMatricula("");
			setCursoVO(obj.getCurso());
			limparTurma();
			getListaConsultaTurma().clear();
		} catch (Exception e) {
		}
	}

	public void consultarCurso() {
		try {
			// if (getProcessoMatriculaVO().getCodigo() == 0) {
			// throw new Exception("Informe o Processo de Matrícula.");
			// }
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getHtmlRelatorioConsultorPorMatricula() {
		if (htmlRelatorioConsultorPorMatricula == null) {
			htmlRelatorioConsultorPorMatricula = "";
		}
		return htmlRelatorioConsultorPorMatricula;
	}

	public void setHtmlRelatorioConsultorPorMatricula(String htmlRelatorioConsultorPorMatricula) {
		this.htmlRelatorioConsultorPorMatricula = htmlRelatorioConsultorPorMatricula;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public FuncionarioVO getConsultor() {
		if (consultor == null) {
			consultor = new FuncionarioVO();
		}
		return consultor;
	}

	public void setConsultor(FuncionarioVO consultor) {
		this.consultor = consultor;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getSituacaoMatricula() {
		if (situacaoMatricula == null) {
			situacaoMatricula = "";
		}
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public String getSituacaoProspectAtIn() {
		if (situacaoProspectAtIn == null) {
			situacaoProspectAtIn = "";
		}
		return situacaoProspectAtIn;
	}

	public void setSituacaoProspectAtIn(String situacaoProspectAtIn) {
		this.situacaoProspectAtIn = situacaoProspectAtIn;
	}

	public List getListaSelectItemFuncionario() {
		if (listaSelectItemFuncionario == null) {
			listaSelectItemFuncionario = new ArrayList();
		}
		return listaSelectItemFuncionario;
	}

	public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
		this.listaSelectItemFuncionario = listaSelectItemFuncionario;
	}

	public List getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList();
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
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
	
    public List getTipoConsultaComboSituacaoMatricula() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "Todos"));
        itens.add(new SelectItem("AT", "Ativas"));
        itens.add(new SelectItem("PR", "Pré-Matrícula"));
        itens.add(new SelectItem("CA", "Cancelada"));
        itens.add(new SelectItem("EX", "Excluída"));
        itens.add(new SelectItem("TD", "Transferência de"));
        itens.add(new SelectItem("TP", "Transferência para"));
        return itens;
    }

    public List getTipoConsultaComboSituacaoProspect() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "Todos"));
        itens.add(new SelectItem("AT", "Ativo"));
        itens.add(new SelectItem("IN", "Inativo"));
        return itens;
    }
    
	
	
    public void gravarDadosPadroesEmissaoRelatorio() {
	try {
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getExcluida()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "excluida", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTransferidaPara()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "transferenciaPara", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTransferidaDe()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "tranferenciaDe", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getMatRecebida()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "matRecebida", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getMatVencida()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "matVencida", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getMatAVencer()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "matAVencer", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getMatAReceber()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "matAReceber", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getMatNaoGerada()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "matNaoGerada", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(isConsiderarConsultorVinculadoProspect()+"", PainelGestorMonitoramentoCRM.class.getSimpleName(), "considerarConsultorVinculadoProspect", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getDataAno4Digitos(getDataInicio()), PainelGestorMonitoramentoCRM.class.getSimpleName(), "dataInicio", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(Uteis.getDataAno4Digitos(getDataFim()), PainelGestorMonitoramentoCRM.class.getSimpleName(), "dataFim", getUsuarioLogado());
		getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), PainelGestorMonitoramentoCRM.class.getSimpleName(), getUsuarioLogado());
	} catch (Exception e) {
		e.printStackTrace();
	}
}

@PostConstruct
public void montarDadosPadroesEmissaoRelatorio() {
	try {
		Map<String, String> retorno = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "ativa", "tranferenciaDe", "matVencida", "preMatricula", "transferenciaPara", "matAVencer", "cancelada", "matRecebida", "excluida", "matAReceber", "considerarConsultorVinculadoProspect", "dataInicio", "dataFim"}, PainelGestorMonitoramentoCRM.class.getSimpleName());
		for (String key : retorno.keySet()) {
			if (key.equals("considerarConsultorVinculadoProspect")) {
				if(retorno.get(key).equals("true")){
					setConsiderarConsultorVinculadoProspect(true);
				}else{
					setConsiderarConsultorVinculadoProspect(false);
				}
			} else if (key.equals("dataInicio") && retorno.get(key) != null && !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
				setDataInicio(Uteis.getData(retorno.get(key), "dd/MM/yyyy"));
			} else if (key.equals("dataFim") && retorno.get(key) != null && !retorno.get(key).equalsIgnoreCase("null") && !retorno.get(key).trim().isEmpty()) {
				setDataFim(Uteis.getData(retorno.get(key), "dd/MM/yyyy"));
			} else if (key.equals("tranferenciaDe")) {
				if(retorno.get(key).equals("true")){
					setTransferidaDe(true);
				}else{
					setTransferidaDe(false);
				}
			} else if (key.equals("matVencida")) {
				if(retorno.get(key).equals("true")){
					setMatVencida(true);
				}else{
					setMatVencida(false);
				}
			} else if (key.equals("transferenciaPara")) {
				if(retorno.get(key).equals("true")){
					setTransferidaPara(true);
				}else{
					setTransferidaPara(false);
				}
			} else if (key.equals("matAVencer")) {
				if(retorno.get(key).equals("true")){
					setMatAVencer(true);
				}else{
					setMatAVencer(false);
				}
			} else if (key.equals("matAVencer")) {
				if(retorno.get(key).equals("true")){
					setMatAVencer(true);
				}else{
					setMatAVencer(false);
				}
			} else if (key.equals("matAReceber")) {
				if(retorno.get(key).equals("true")){
					setMatRecebida(true);
				}else{
					setMatRecebida(false);
				}
			} else if (key.equals("excluida")) {
				if(retorno.get(key).equals("true")){
					setExcluida(true);
				}else{
					setExcluida(false);
				}
			}
			else if (key.equals("matNaoGerada")) {
				if(retorno.get(key).equals("true")){
					setMatNaoGerada(true);
				}else{
					setMatNaoGerada(false);
				}
			}
		}
		getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), PainelGestorMonitoramentoCRM.class.getSimpleName(), getUsuarioLogado());
	} catch (Exception e) {
		e.printStackTrace();
	}
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

	public boolean getMatRecebida() {
		return matRecebida;
	}

	public void setMatRecebida(boolean matRecebida) {
		this.matRecebida = matRecebida;
	}

	public boolean getMatAReceber() {
		return matAReceber;
	}

	public void setMatAReceber(boolean matAReceber) {
		this.matAReceber = matAReceber;
	}

	public boolean getMatVencida() {
		return matVencida;
	}

	public void setMatVencida(boolean matVencida) {
		this.matVencida = matVencida;
	}

	public boolean getMatAVencer() {
		return matAVencer;
	}

	public void setMatAVencer(boolean matAVencer) {
		this.matAVencer = matAVencer;
	}

	public boolean getAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

	public boolean getPreMatricula() {
		return preMatricula;
	}

	public void setPreMatricula(boolean preMatricula) {
		this.preMatricula = preMatricula;
	}

	public boolean getCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public boolean getExcluida() {
		return excluida;
	}

	public void setExcluida(boolean excluida) {
		this.excluida = excluida;
	}

	public boolean getTransferidaDe() {
		return transferidaDe;
	}

	public void setTransferidaDe(boolean transferidaDe) {
		this.transferidaDe = transferidaDe;
	}

	public boolean getTransferidaPara() {
		return transferidaPara;
	}

	public void setTransferidaPara(boolean transferidaPara) {
		this.transferidaPara = transferidaPara;
	}
	
	public boolean isConsiderarConsultorVinculadoProspect() {
		return considerarConsultorVinculadoProspect;
	}

	public void setConsiderarConsultorVinculadoProspect(boolean considerarConsultorVinculadoProspect) {
		this.considerarConsultorVinculadoProspect = considerarConsultorVinculadoProspect;
	}
	public List<SelectItem> getListaSelectItemSituacaoAlunoCurso() {
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		lista.add(new SelectItem("ambos", "Ambos"));
		lista.add(new SelectItem("calouro", "Calouro"));
		lista.add(new SelectItem("veterano", "Veterano"));
		return lista;
	}

	public String getSituacaoAlunoCurso() {
		if (situacaoAlunoCurso == null) {
			situacaoAlunoCurso = "ambos";
		}
		return situacaoAlunoCurso;
	}

	public void setSituacaoAlunoCurso(String situacaoAlunoCurso) {
		this.situacaoAlunoCurso = situacaoAlunoCurso;
	}

	
	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		if(cursoApresentar.length() > 85) {
			return cursoApresentar.substring(0, 84) + "...";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		if(unidadeEnsinoApresentar.length() > 80) {
			return unidadeEnsinoApresentar.substring(0, 79) + "...";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}
	
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
				}
			}
			setUnidadeEnsinoApresentar(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
		setCursoApresentar("");
		limparCursos();
		//consultarCursoFiltroRelatorio("");
		consultarTurnoFiltroRelatorio();
		consultarTurmaFiltroRelatorio();
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados(){
		StringBuilder curso = new StringBuilder();
		if (getCursoVOs().size() > 1) {
			for (CursoVO obj : getCursoVOs()) {
				if (obj.getFiltrarCursoVO()) {
					curso.append(obj.getCodigo()).append(" - ");
					curso.append(obj.getNome()).append("; ");
				}
			}
			setCursoApresentar(curso.toString());
		} else {
			if (!getCursoVOs().isEmpty()) {
				if (getCursoVOs().get(0).getFiltrarCursoVO()) {
					setCursoApresentar(getCursoVOs().get(0).getNome());
				}
			}
		}
	}
	
	public List<ConsultorPorMatriculaRelVO> getListaRelatorioAnalitico() {
		if (listaRelatorioAnalitico == null) {
			listaRelatorioAnalitico = new ArrayList<ConsultorPorMatriculaRelVO>();
		}
		return listaRelatorioAnalitico;
	}

	public void setListaRelatorioAnalitico(List<ConsultorPorMatriculaRelVO> listaRelatorioAnalitico) {
		this.listaRelatorioAnalitico = listaRelatorioAnalitico;
	}
	
	public void montarListaTotalizadorConsultor() {
		List<FuncionarioVO> listaTemp = new ArrayList<FuncionarioVO>();
		for (ConsultorPorMatriculaRelVO consultorPorMatriculaRelVO : getListaRelatorioAnalitico()) {
			for (FuncionarioVO funcionarioVO : consultorPorMatriculaRelVO.getConsultor()) {
				FuncionarioVO funcionario = new FuncionarioVO();
				funcionario = funcionarioVO;
				funcionario.setTotalProspectPorConsultor(funcionarioVO.getQtdeAlunoVinculadosConsultor()) ;
				listaTemp.add(funcionario);
			}
		}
		Ordenacao.ordenarLista(listaTemp, "pessoa.nome");
		
		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO>();
		String nomeFuncionario = "";
		int total = 0;
		int contador = 0;
		FuncionarioVO funcionario = new FuncionarioVO();
		for(FuncionarioVO funcionarioVO : listaTemp) {
			if (!nomeFuncionario.equals(funcionarioVO.getPessoa().getNome())) {
				
				if (!nomeFuncionario.isEmpty()) {
					funcionario.setTotalProspectPorConsultor(total);
					lista.add(funcionario);
				}
				
				funcionario = new FuncionarioVO();
				funcionario.setPessoa(funcionarioVO.getPessoa());
				total = 0;
			}
			total += funcionarioVO.getTotalProspectPorConsultor();
			nomeFuncionario = funcionarioVO.getPessoa().getNome();
			contador++;
			
			if (contador == listaTemp.size()) {
				funcionario.setTotalProspectPorConsultor(total);
				lista.add(funcionario);
			}

		}
		getSuperParametroRelVO().adicionarParametro("listaTotalizadorConsultor", lista);
	}

	public Date getDataInicioPagamentoMatricula() {
		return dataInicioPagamentoMatricula;
	}

	public void setDataInicioPagamentoMatricula(Date dataInicioPagamentoMatricula) {
		this.dataInicioPagamentoMatricula = dataInicioPagamentoMatricula;
	}

	public Date getDataFimPagamentoMatricula() {
		return dataFimPagamentoMatricula;
	}

	public void setDataFimPagamentoMatricula(Date dataFimPagamentoMatricula) {
		this.dataFimPagamentoMatricula = dataFimPagamentoMatricula;
	}

	public boolean isConsiderarApenasTurmaComAlunosMatriculados() {
		return considerarApenasTurmaComAlunosMatriculados;
	}

	public void setConsiderarApenasTurmaComAlunosMatriculados(boolean considerarApenasTurmaComAlunosMatriculados) {
		this.considerarApenasTurmaComAlunosMatriculados = considerarApenasTurmaComAlunosMatriculados;
	}
	
	public List<UnidadeEnsinoVO> obterListaUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		unidadeEnsinoVOs.forEach(obj->{
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public List<CursoVO> obterListaCursoSelecionado(List<CursoVO> CursoVOs) {
		List<CursoVO> objs = new ArrayList<CursoVO>(0);
		CursoVOs.forEach(obj->{
			if (obj.getFiltrarCursoVO()) {
				objs.add(obj);
			}
		});
		return objs;
	}

	public boolean isDesabilitarSelecionarUnidadeEnsinoCurso() {
		return desabilitarSelecionarUnidadeEnsinoCurso;
	}

	public void setDesabilitarSelecionarUnidadeEnsinoCurso(boolean desabilitarSelecionarUnidadeEnsinoCurso) {
		this.desabilitarSelecionarUnidadeEnsinoCurso = desabilitarSelecionarUnidadeEnsinoCurso;
	}

	public boolean isTrazerAlunosSemTutor() {
		return trazerAlunosSemTutor;
	}

	public void setTrazerAlunosSemTutor(boolean trazerAlunosSemTutor) {
		this.trazerAlunosSemTutor = trazerAlunosSemTutor;
	}

	public String getTurmaApresentar() {
		if (turmaApresentar == null) {
			turmaApresentar = "";
		}
		if(turmaApresentar.length() > 80) {
			return turmaApresentar.substring(0, 79) + "...";
		}
		return turmaApresentar;
	}

	public void setTurmaApresentar(String turmaApresentar) {
		this.turmaApresentar = turmaApresentar;
	}

	public List<TurmaVO> getTurmaVOs() {
		if (turmaVOs == null) {
			turmaVOs = new ArrayList<TurmaVO>(0);
		}
		return turmaVOs;
	}

	public void setTurmaVOs(List<TurmaVO> turmaVOs) {
		this.turmaVOs = turmaVOs;
	}
	
	public void realizarMarcacaoTurmas() {
		for (TurmaVO turmaVO : getTurmaVOs()) {
			turmaVO.setFiltrarTurmaVO(getMarcarTodasTurmas());
		}
		verificarTodasTurmasSelecionadas();
	}

	public Boolean getMarcarTodasTurmas() {
		if (marcarTodasTurmas == null) {
			marcarTodasTurmas = false;
		}
		return marcarTodasTurmas;
	}

	public void setMarcarTodasTurmas(Boolean marcarTodasTurmas) {
		this.marcarTodasTurmas = marcarTodasTurmas;
	}
	
	public void limparTurmas(){
		setMarcarTodasTurmas(false);
		realizarMarcacaoTurmas();
	}
	
	public void verificarTodasTurmasSelecionadas() {
		setTurmaApresentar("");
		StringBuilder turma = new StringBuilder();
		if (getTurmaVOs().size() > 1) {
			for (TurmaVO obj : getTurmaVOs()) {
				if (obj.getFiltrarTurmaVO()) {
					turma.append(obj.getCodigo()).append(" - ");
					turma.append(obj.getIdentificadorTurma()).append("; ");
				}
			}
			setTurmaApresentar(turma.toString());
		} else {
			if (!getTurmaVOs().isEmpty()) {
				if (getTurmaVOs().get(0).getFiltrarTurmaVO()) {
					setTurmaApresentar(getTurmaVOs().get(0).getIdentificadorTurma());
				}
			} else {
				setTurmaApresentar(turma.toString());
			}
		}
	}
	
	public void consultarTurmaFiltroRelatorio() {
		try {
			setTurmaApresentar("");			
			if (getUnidadeEnsinoVOs().isEmpty()) {
				setTurmaVOs(null);
				return;
			}
			setTurmaVOs(getFacadeFactory().getTurmaFacade().consultarTurmaPorCursoVOsEUnidadeEnsinoVOs(
					getUnidadeEnsinoVOs().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).collect(Collectors.toCollection(ArrayList::new)),
					getCursoVOs().stream().filter(CursoVO::getFiltrarCursoVO).collect(Collectors.toCollection(ArrayList::new)),
					getUsuarioLogado()));
		} catch (Exception e) {
			setTurmaVOs(null);
		}
	}
	
	public List<TurmaVO> obterListaTurmaSelecionada(List<TurmaVO> turmaVOs) {
		List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
		turmaVOs.forEach(obj->{
			if (obj.getFiltrarTurmaVO()) {
				objs.add(obj);
			}
		});
		return objs;
	}

	public boolean isApresentarSumarioComTotalizadorPorConsultor() {
		return apresentarSumarioComTotalizadorPorConsultor;
	}

	public void setApresentarSumarioComTotalizadorPorConsultor(boolean apresentarSumarioComTotalizadorPorConsultor) {
		this.apresentarSumarioComTotalizadorPorConsultor = apresentarSumarioComTotalizadorPorConsultor;
	}

	public boolean getMatNaoGerada() {
		return matNaoGerada;
	}

	public void setMatNaoGerada(boolean matNaoGerada) {
		this.matNaoGerada = matNaoGerada;
	}
	
	public String getTipoNivelEducacional() {
		return tipoNivelEducacional;
	}

	public void setTipoNivelEducacional(String tipoNivelEducacional) {
		this.tipoNivelEducacional = tipoNivelEducacional;
	}

	public void setListaNivelEducacional(List listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public List getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList(0);
		}
		return listaNivelEducacional;
	}
	
	public List getListaSelectItemNivelEducacionalCurso() throws Exception {
		listaNivelEducacional = new ArrayList<>();
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			if (!item.getValue().equals("SE")) {
				getListaNivelEducacional().add(item);
			}
		}
		return listaNivelEducacional;
	}
	
	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>nivelEducacional</code>
	 */
	public void montarListaSelectItemNivelEducacionalCurso() throws Exception {
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			if (!item.getValue().equals("SE")) {
				getListaNivelEducacional().add(item);
			}
		}
	}
	
	public void consultarCursoRelatorio() {
		try {
			setTurmaApresentar("");
			limparTurmas();
			limparCursos();
			if (getUnidadeEnsinoVOs().isEmpty()) {
				setTurmaVOs(null);
				return;
			}
			setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNivelEducacionalEUnidadeEnsinoVOs(getTipoNivelEducacional(), getUnidadeEnsinoVOs(), getUsuario()));
		} catch (Exception e) {
			setTurmaVOs(null);
			
		}
	}
	

	public void validarDados() throws Exception {

		if (!getAtiva() && !getPreMatricula() && !getCancelada() && !getExcluida() && !getTransferidaDe() && !getTransferidaPara()) {
			if (getMatRecebida() || getMatAReceber() || getMatVencida() || getMatAVencer() ) {
				throw new Exception("Selecionar ao menos 1 (uma) situação de matrícula para emissão do relatório!");
			}				
		}
		if(!Uteis.isAtributoPreenchido(getDataInicio()) || !Uteis.isAtributoPreenchido(getDataFim())) {
			throw new Exception("O período de inclusão de matriculas deve estar preenchido para emissão do relatório!");
		}
		if(!Uteis.isAtributoPreenchido(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs())) ) {
			throw new Exception("Deve ser escolhida ao menos uma unidade de ensino para emissão do relatório!");
			
		}
	}

	public boolean isTrazerAlunosBolsistas() {
		return trazerAlunosBolsistas;
	}

	public void setTrazerAlunosBolsistas(boolean trazerAlunosBolsistas) {
		this.trazerAlunosBolsistas = trazerAlunosBolsistas;
	}
	
	
		
}
