/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.*;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.Constantes;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import webservice.servicos.MatriculaRSVO;

/**
 *
 * @author Philippe
 */
@Controller("MapaControleEntregaDocumentoUploadControle")
@Scope("viewScope")
@Lazy
public class MapaControleEntregaDocumentoUploadControle extends SuperControleRelatorio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 262523028925819204L;
	private MatriculaVO matricula;
    private MatriculaVO matriculaVisualizar;
    private DocumetacaoMatriculaVO documetacaoMatricula;
    private TurmaVO turma;
    private UnidadeEnsinoVO unidadeEnsino;
    private Boolean apresentarPesquisaPorAluno;
    private Boolean apresentarPesquisaPorTurma;
    private String filtro;
    private Date dataConclusao;
    private List<TurmaVO> listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<MatriculaVO> listaConsultaMatricula;
    private String valorConsultaMatricula;
    private String campoConsultaMatricula;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<DocumetacaoMatriculaVO> listaDocumento;
    private Boolean deletarArquivo;
    private Boolean trazerDocumentosIndeferidos;
    private Boolean trazerDocumentosDeferidos;
    private Boolean trazerDocumentosPendentes;
    private ArquivoVO arquivoVO;
    private Integer chamada;    
    private Boolean confirmarVoltarSituacaoPreMatricula;
    protected String valorConsultaProcessoSeletivo;
	protected String campoConsultaProcessoSeletivo;
	protected List<ProcSeletivoVO> listaConsultaProcessoSeletivo;
	private ProcSeletivoVO procSeletivoVO;
	private List<SelectItem> listaSelectItemNivelEducacional;
	private String nivelEducacional;
    private List<SelectItem> listaSelectItemMotivosIndeferimentoDocumentoAluno;
    private String caminhoPreviewPdfA;
    
    public MapaControleEntregaDocumentoUploadControle() {
    	getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
    	getControleConsultaOtimizado().setDataFim(new Date());
        montarListaSelectItemUnidadeEnsino();
        setHeigth(100);
    }

    public void consultarMatriculasAtualizacao() {
        try {       
        	getControleConsultaOtimizado().setLimitePorPagina(10);
	        getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorTurmaMatriculaDocumentoPendente(getControleConsultaOtimizado(), getUnidadeEnsino().getCodigo(), getTurma().getCodigo(), getMatricula().getMatricula(), getTrazerDocumentosIndeferidos(), getTrazerDocumentosDeferidos(), getTrazerDocumentosPendentes(), getChamada(), getProcSeletivoVO(), getNivelEducacional(), getUsuarioLogado());
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarMatriculasAtualizacao();		
	}

    public void consultarAluno() {
        try {
            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
            if (getValorConsultaMatricula().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaMatricula().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaSituacaoDadosCompletos(getValorConsultaMatricula(), getUnidadeEnsino().getCodigo(), false, "", getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaMatricula().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), getUnidadeEnsino().getCodigo(), false, "","", getUsuarioLogado());
            }
            if (getCampoConsultaMatricula().equals("registroAcademico")) {
            	objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaMatricula(), getUnidadeEnsino().getCodigo(), false, "","", getUsuarioLogado());
            }
            setListaConsultaMatricula(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaMatricula(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

   
	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatricula(objAluno);
			setUnidadeEnsino(getMatricula().getUnidadeEnsino());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setMatricula(new MatriculaVO());
		}
	}

    public void selecionarAluno() throws Exception {
    	try {
	        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
	        MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), 0, NivelMontarDados.TODOS, getUsuarioLogado());
	        if (objCompleto.getMatricula().equals("")) {
	        	throw new Exception ("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
	        }
	        setMatricula(objCompleto);
	        setTurma(null);
	        obj = null;
	        objCompleto = null;
	        setValorConsultaMatricula("");
	        setCampoConsultaMatricula("");
	        getListaConsultaMatricula().clear();
	    } catch (Exception e) {	    	
	        setValorConsultaMatricula("");
	        setCampoConsultaMatricula("");
	        getListaConsultaMatricula().clear();	    	
	        setMensagemDetalhada("msg_erro", e.getMessage());
	    }        
    }

    public void visualizarDocumento() throws Exception {
        try {
            MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
            obj.getMatriculaVO().getMatriculaPeriodoVOs().add(obj);
            setMatriculaVisualizar(obj.getMatriculaVO());
            getFacadeFactory().getCursoFacade().carregarDados(obj.getMatriculaVO().getCurso(), getUsuarioLogado());
            obj.getMatriculaVO().getCurso().setTextoConfirmacaoNovaMatricula(MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(obj.getMatriculaVO().getCurso().getTextoConfirmacaoNovaMatricula(), getMatriculaVisualizar().getAluno().getNome(), getMatriculaVisualizar().getAluno().getRG(), getMatriculaVisualizar().getAluno().getCPF()));
            obj.getMatriculaVO().getCurso().setTextoDeclaracaoBolsasAuxilios(MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(obj.getMatriculaVO().getCurso().getTextoDeclaracaoBolsasAuxilios(), getMatriculaVisualizar().getAluno().getNome(), getMatriculaVisualizar().getAluno().getRG(), getMatriculaVisualizar().getAluno().getCPF()));
            obj.getMatriculaVO().getCurso().setTextoDeclaracaoEscolaridadePublica(MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(obj.getMatriculaVO().getCurso().getTextoDeclaracaoEscolaridadePublica(), getMatriculaVisualizar().getAluno().getNome(), getMatriculaVisualizar().getAluno().getRG(), getMatriculaVisualizar().getAluno().getCPF()));
            obj.getMatriculaVO().getCurso().setTextoDeclaracaoPPI(MatriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(obj.getMatriculaVO().getCurso().getTextoDeclaracaoPPI(), getMatriculaVisualizar().getAluno().getNome(), getMatriculaVisualizar().getAluno().getRG(), getMatriculaVisualizar().getAluno().getCPF()));
            setListaDocumento(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentoPendenteAprovacao(obj.getMatricula(), getTrazerDocumentosIndeferidos(), getTrazerDocumentosDeferidos(), getTrazerDocumentosPendentes()));
            limparMensagem();
        } catch (Exception e) {
            setListaDocumento(null);
            setMatriculaVisualizar(null);
        }
    }

    public void aprovar() {
    	DocumetacaoMatriculaVO obj = null;
    	setCaminhoPreviewPdfA(Constantes.EMPTY);
    	setOncompleteModal(Constantes.EMPTY);
        try {
            obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documentoMatriculaItens");            
            obj = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            obj.setRespAprovacaoDocDep(getUsuarioLogadoClone());
                      
            boolean assinarDocumentacao = assinarDocumentacao(obj.getMatricula());
            if(assinarDocumentacao) {
                obj.setFileAssinar(getFacadeFactory().getDocumetacaoMatriculaFacade().unificarFrenteVersoDocumentoMatricula(obj, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
                setOncompleteModal("RichFaces.$('panelPreviewPdfA').show()");
                obj.setValidarPdfA(Boolean.TRUE);
            }
            getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(obj, getMatriculaVisualizar(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVisualizar().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
            if (assinarDocumentacao) {
            	setCaminhoPreviewPdfA(obj.getCaminhoPreviewPdfA());
            	setDocumetacaoMatricula((DocumetacaoMatriculaVO) Uteis.clonar(obj));
            } else {
            	obj = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            	atualizarDocumento(obj);
            }
            setMensagemDetalhada("");
            setMensagemID("msg_dados_aprovados");
        } catch (Exception e) {
        	obj.getFileAssinar().delete();
        	setOncompleteModal(Constantes.EMPTY);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    private boolean assinarDocumentacao(String matricula) throws Exception {
    	UnidadeEnsinoVO unidadeEnsinoAluno = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
    	ConfiguracaoGEDVO configGEDVO = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(unidadeEnsinoAluno.getCodigo(), false, getUsuario());
    	return Uteis.isAtributoPreenchido(configGEDVO.getCodigo()) ? configGEDVO.getConfiguracaoGedDocumentoAlunoVO().getAssinarDocumento() : false;
	}

    public void atualizarDocumento(DocumetacaoMatriculaVO obj) {
        try {
            Iterator<DocumetacaoMatriculaVO> i = getListaDocumento().iterator();
            int x = 0;
            while (i.hasNext()) {
                DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO)i.next();
                if (doc.getCodigo().equals(obj.getCodigo().intValue())) {
                	getListaDocumento().set(x, obj);
                    break;
                }
                x++;
            }
        } catch (Exception e) {
        }
    }

    public void notificarAprovados() {
        try {
            List<DocumetacaoMatriculaVO> listaNotificar = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentoAprovadoPendenteNotificar(getMatriculaVisualizar().getMatricula());
            if(listaNotificar.isEmpty()) {
            	throw new Exception("Nenhum documento aprovado foi encontrado para notificar o aluno.");
            }
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunoDocumentoDeferido(getMatriculaVisualizar(), listaNotificar, getUsuarioLogado());
            setMensagemID("msg_dados_Enviados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void negar() {
        try {
            setOncompleteModal("");
            String justificativaNegacao = getDocumetacaoMatricula().getJustificativaNegacao();
            if(!Uteis.isAtributoPreenchido(justificativaNegacao)) {
                throw new Exception("O campo JUSTIFICATIVA deve ser informado.");
            }
            MotivoIndeferimentoDocumentoAlunoVO motivo = getDocumetacaoMatricula().getMotivoIndeferimentoDocumentoAlunoVO();
            if (!Uteis.isAtributoPreenchido(motivo.getCodigo())) {
                throw new Exception(getMensagemInternalizacao("msg_MapaControleEntregaDocumentoUploadControle_MotivoVazio"));
            }
            getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().carregarMotivoIndeferimentoDocumentoAluno(motivo, false, getUsuarioLogado());
            DocumetacaoMatriculaVO doc = new DocumetacaoMatriculaVO();
            doc = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(getDocumetacaoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            doc.setEntregue(Boolean.FALSE);
            doc.setJustificativaNegacao(justificativaNegacao);
            doc.setRespNegarDocDep(getUsuarioLogadoClone());
            doc.setDeletarArquivo(false);
            doc.setMotivoIndeferimentoDocumentoAlunoVO(motivo);
            getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaNegadoPeloDep(doc, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            try {
            	getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAlunoDocumentoIndeferido(doc, getUsuarioLogado());
            } catch (Exception e) {
            }
            doc = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(getDocumetacaoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            atualizarDocumento(doc);   
            if(getConfirmarVoltarSituacaoPreMatricula()) {
    			getFacadeFactory().getMatriculaPeriodoFacade().realizarVerificacaoMatriculaPrecisaVoltarSituacaoPrematriculaAposIndeferirDocumentosObrigatorios(getMatriculaVisualizar().getMatricula(),   true , false , getUsuarioLogado()) ;
    			setConfirmarVoltarSituacaoPreMatricula(Boolean.FALSE);
            }
            
            setMensagemDetalhada("");
            setOncompleteModal("RichFaces.$('panelJustificativa').hide();");
            setMensagemID("msg_dados_indeferido");
        } catch (Exception e) {
            reverterSituacaoEntregaDocumentoDocumentacaoMatricula();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void visualizarNegar() {
        try {
             DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documentoMatriculaItens");
             setOncompleteModal(Constantes.EMPTY);
 			getFacadeFactory().getDocumetacaoMatriculaFacade().validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(getMatricula() ,obj,getUsuarioLogado());
             /*obj.setEntregue(Boolean.FALSE);
             obj.getRespNegarDocDep().setCodigo(getUsuarioLogado().getCodigo());*/
            montarListaSelectItemMotivoIndeferimentoDocumentoAlunoAtivo(true);
            setDocumetacaoMatricula(obj);      
            realizarValidacaoMatriculaAptaVoltarSituacaoPreMatricula();
            getDocumetacaoMatricula().setValorEntregueAnterior(getDocumetacaoMatricula().getEntregue());
            getDocumetacaoMatricula().setEntregue(false);
            setMensagemID(MSG_TELA.msg_entre_dados.name());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getCaminhoServidorDownload() {
        try {
            DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documentoMatriculaItens");
            return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVO(), obj.getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public String getCaminhoServidorDownloadVerso() {
    	try {
    		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documentoMatriculaItens");
    		return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVOVerso(), obj.getArquivoVOVerso().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
    	} catch (Exception ex) {
    		setMensagemDetalhada("msg_erro", ex.getMessage());
    	}
    	return "";
    }
    
    public void limparDadosMatricula() throws Exception {
    	setMensagemID("msg_entre_dados");
    	setMatricula(null);
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), 0, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), 0, 0);
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), 0, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), 0, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setListaConsultaTurma(new ArrayList<TurmaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurma(obj);
            if (getUnidadeEnsino().getCodigo() == 0) {
                setUnidadeEnsino(getTurma().getUnidadeEnsino());
            }
            getListaConsultaTurma().clear();
            this.setValorConsultaTurma("");
            this.setCampoConsultaTurma("");
            setMensagemID("", "");
        } catch (Exception e) {
        }
    }

    public void limparTurma() throws Exception {
        try {
        	setMensagemID("msg_entre_dados");
            setTurma(null);
        } catch (Exception e) {
        }
    }

    public List<SelectItem> tipoConsultaComboTurma;
    public List<SelectItem> getTipoConsultaComboTurma() {
    	if(tipoConsultaComboTurma == null) {
    	tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
    	tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
    	tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
    	}
        return tipoConsultaComboTurma;
    }

    public List<SelectItem> tipoConsultaComboMatricula;
    public List<SelectItem> getTipoConsultaComboMatricula() {
    	if(tipoConsultaComboMatricula == null) {
    		tipoConsultaComboMatricula = new ArrayList<SelectItem>(0);
    		tipoConsultaComboMatricula.add(new SelectItem("nomePessoa", "Aluno"));
    		tipoConsultaComboMatricula.add(new SelectItem("matricula", "Matrícula"));
    		tipoConsultaComboMatricula.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
    	}
        return tipoConsultaComboMatricula;
    }

    public List<SelectItem> tipoFiltroCombo;
    public List<SelectItem> getTipoFiltroCombo() {
    	if(tipoFiltroCombo == null) {
    		tipoFiltroCombo = new ArrayList<SelectItem>(0);
    		tipoFiltroCombo.add(new SelectItem("", ""));
    		tipoFiltroCombo.add(new SelectItem("turma", "Turma"));
    		tipoFiltroCombo.add(new SelectItem("aluno", "Aluno"));
    	}
        return tipoFiltroCombo;
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            // System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
                objs.add(new SelectItem(0, ""));
            }
            i = resultadoConsulta.iterator();
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
    	return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());        
    }

    public void apresentarMetodoPesquisa() {
        if (getFiltro().equals("turma") && getUnidadeEnsino().getCodigo() != 0) {
            setApresentarPesquisaPorAluno(Boolean.FALSE);
            setApresentarPesquisaPorTurma(Boolean.TRUE);
        } else if (getFiltro().equals("aluno") && getUnidadeEnsino().getCodigo() != 0) {
            setApresentarPesquisaPorAluno(Boolean.TRUE);
            setApresentarPesquisaPorTurma(Boolean.FALSE);
        } else {
            setApresentarPesquisaPorAluno(Boolean.FALSE);
            setApresentarPesquisaPorTurma(Boolean.FALSE);
        }
        setMatricula(null);
        setTurma(null);        
    }
	

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public Boolean getApresentarPesquisaPorAluno() {
        if (apresentarPesquisaPorAluno == null) {
            apresentarPesquisaPorAluno = Boolean.FALSE;
        }
        return apresentarPesquisaPorAluno;
    }

    public void setApresentarPesquisaPorAluno(Boolean apresentarPesquisaPorAluno) {
        this.apresentarPesquisaPorAluno = apresentarPesquisaPorAluno;
    }

    public Boolean getApresentarPesquisaPorTurma() {
        if (apresentarPesquisaPorTurma == null) {
            apresentarPesquisaPorTurma = Boolean.FALSE;
        }
        return apresentarPesquisaPorTurma;
    }

    public void setApresentarPesquisaPorTurma(Boolean apresentarPesquisaPorTurma) {
        this.apresentarPesquisaPorTurma = apresentarPesquisaPorTurma;
    }

    public String getFiltro() {
        if (filtro == null) {
            filtro = "";
        }
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Date getDataConclusao() {
        if (dataConclusao == null) {
            dataConclusao = new Date();
        }
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }


    public Boolean getApresentarBotaoConsultar() {
        if (getTurma().getCodigo() != 0 || getMatricula().getAluno().getCodigo() != 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getApresentarConsulta() {
        return getControleConsultaOtimizado().getApresentarListaConsulta();
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return matricula;
    }

    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    public List<MatriculaVO> getListaConsultaMatricula() {
        if (listaConsultaMatricula == null) {
            listaConsultaMatricula = new ArrayList<MatriculaVO>(0);
        }
        return listaConsultaMatricula;
    }

    public void setListaConsultaMatricula(List<MatriculaVO> listaConsultaMatricula) {
        this.listaConsultaMatricula = listaConsultaMatricula;
    }

    public String getValorConsultaMatricula() {
        if (valorConsultaMatricula == null) {
            valorConsultaMatricula = "";
        }
        return valorConsultaMatricula;
    }

    public void setValorConsultaMatricula(String valorConsultaMatricula) {
        this.valorConsultaMatricula = valorConsultaMatricula;
    }

    public String getCampoConsultaMatricula() {
        if (campoConsultaMatricula == null) {
            campoConsultaMatricula = "";
        }
        return campoConsultaMatricula;
    }

    public void setCampoConsultaMatricula(String campoConsultaMatricula) {
        this.campoConsultaMatricula = campoConsultaMatricula;
    }

    /**
     * @return the listaDocumento
     */
    public List<DocumetacaoMatriculaVO> getListaDocumento() {
        if (listaDocumento == null) {
            listaDocumento = new ArrayList<DocumetacaoMatriculaVO>();
        }
        return listaDocumento;
    }

    /**
     * @param listaDocumento the listaDocumento to set
     */
    public void setListaDocumento(List<DocumetacaoMatriculaVO> listaDocumento) {
        this.listaDocumento = listaDocumento;
    }

    /**
     * @return the matriculaVisualizar
     */
    public MatriculaVO getMatriculaVisualizar() {
        if (matriculaVisualizar == null) {
            matriculaVisualizar = new MatriculaVO();
        }
        return matriculaVisualizar;
    }

    /**
     * @param matriculaVisualizar the matriculaVisualizar to set
     */
    public void setMatriculaVisualizar(MatriculaVO matriculaVisualizar) {
        this.matriculaVisualizar = matriculaVisualizar;
    }

    /**
     * @return the documetacaoMatricula
     */
    public DocumetacaoMatriculaVO getDocumetacaoMatricula() {
        if (documetacaoMatricula == null) {
            documetacaoMatricula = new DocumetacaoMatriculaVO();
        }
        return documetacaoMatricula;
    }

    /**
     * @param documetacaoMatricula the documetacaoMatricula to set
     */
    public void setDocumetacaoMatricula(DocumetacaoMatriculaVO documetacaoMatricula) {
        this.documetacaoMatricula = documetacaoMatricula;
    }

	public Boolean getDeletarArquivo() {
		if(deletarArquivo == null) {
			deletarArquivo = false;
		}
		return deletarArquivo;
	}

	public void setDeletarArquivo(Boolean deletarArquivo) {
		this.deletarArquivo = deletarArquivo;
	}

	public Boolean getTrazerDocumentosIndeferidos() {
		if(trazerDocumentosIndeferidos == null) {
			trazerDocumentosIndeferidos = false;
		}
		return trazerDocumentosIndeferidos;
	}

	public void setTrazerDocumentosIndeferidos(Boolean trazerDocumentosIndeferidos) {
		this.trazerDocumentosIndeferidos = trazerDocumentosIndeferidos;
	}

	public Boolean getTrazerDocumentosDeferidos() {
		if(trazerDocumentosDeferidos == null) {
			trazerDocumentosDeferidos = false;
		}
		return trazerDocumentosDeferidos;
	}

	public void setTrazerDocumentosDeferidos(Boolean trazerDocumentosDeferidos) {
		this.trazerDocumentosDeferidos = trazerDocumentosDeferidos;
	}

	public Boolean getTrazerDocumentosPendentes() {
		if(trazerDocumentosPendentes == null) {
			trazerDocumentosPendentes = false;
		}
		return trazerDocumentosPendentes;
	}

	public void setTrazerDocumentosPendentes(Boolean trazerDocumentosPendentes) {
		this.trazerDocumentosPendentes = trazerDocumentosPendentes;
	}

	public ArquivoVO getArquivoVO() {
		if(arquivoVO == null) {
			arquivoVO =  new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}
	
	public void realizarPreparacaoPreview(ArquivoVO arquivoVO) {
		setArquivoVO(arquivoVO);
		getArquivoVO().obterUrlParaDownload(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo(), arquivoVO.getPastaBaseArquivoEnum());
	}

	public Integer getChamada() {
		if(chamada == null) {
			chamada = 0;
		}
		return chamada;
	}

	public void setChamada(Integer chamada) {
		this.chamada = chamada;
	}
    
	 public void realizarValidacaoMatriculaAptaVoltarSituacaoPreMatricula() {
        try {
        	setConfirmarVoltarSituacaoPreMatricula(Boolean.FALSE);
        	setOncompleteModal("RichFaces.$('panelJustificativa').show();");	        	
        	if(getDocumetacaoMatricula().getGerarSuspensaoMatricula().equals(Boolean.TRUE) && 
        			getFacadeFactory().getMatriculaPeriodoFacade().realizarVerificacaoMatriculaPrecisaVoltarSituacaoPrematriculaAposIndeferirDocumentosObrigatorios(getMatriculaVisualizar().getMatricula(),  false, true , getUsuarioLogado())) {
        		setOncompleteModal("RichFaces.$('panelAvisoMatriculaAtiva').show();");  
        	}
        	
        } catch (Exception e) {
        	setOncompleteModal("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
	 }
	 
	 
	 public void confirmarVoltarSituacaoMatricula() {
		 try {
			 setOncompleteModal("RichFaces.$('panelAvisoMatriculaAtiva').hide();RichFaces.$('panelJustificativa').show()");
			 setConfirmarVoltarSituacaoPreMatricula(Boolean.TRUE);			
		 } catch (Exception e) {
			 setConfirmarVoltarSituacaoPreMatricula(Boolean.FALSE);	
			 setOncompleteModal("");
			 setMensagemDetalhada("msg_erro", e.getMessage());
		 }
	 }
	

	public Boolean getConfirmarVoltarSituacaoPreMatricula() {
		if(confirmarVoltarSituacaoPreMatricula == null ) {
			confirmarVoltarSituacaoPreMatricula = Boolean.FALSE;
		}
		return confirmarVoltarSituacaoPreMatricula;
	}

	public void setConfirmarVoltarSituacaoPreMatricula(Boolean confirmarVoltarSituacaoPreMatricula) {
		this.confirmarVoltarSituacaoPreMatricula = confirmarVoltarSituacaoPreMatricula;
	}
	
	public String getValorConsultaProcessoSeletivo() {
		if (valorConsultaProcessoSeletivo == null) {
			valorConsultaProcessoSeletivo = "";
		}
		return valorConsultaProcessoSeletivo;
	}

	public void setValorConsultaProcessoSeletivo(String valorConsultaProcessoSeletivo) {
		this.valorConsultaProcessoSeletivo = valorConsultaProcessoSeletivo;
	}
	
	public String getCampoConsultaProcessoSeletivo() {
		if (campoConsultaProcessoSeletivo == null) {
			campoConsultaProcessoSeletivo = "descricao";
		}
		return campoConsultaProcessoSeletivo;
	}

	public void setCampoConsultaProcessoSeletivo(String campoConsultaProcessoSeletivo) {
		this.campoConsultaProcessoSeletivo = campoConsultaProcessoSeletivo;
	}

	public List<ProcSeletivoVO> getListaConsultaProcessoSeletivo() {
		if (listaConsultaProcessoSeletivo == null) {
			listaConsultaProcessoSeletivo = new ArrayList(0);
		}
		return listaConsultaProcessoSeletivo;
	}

	public void setListaConsultaProcessoSeletivo(List<ProcSeletivoVO> listaConsultaProcessoSeletivo) {
		this.listaConsultaProcessoSeletivo = listaConsultaProcessoSeletivo;
	}


	public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcessoSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcessoSeletivo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcessoSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcessoSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcessoSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcessoSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcessoSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcessoSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcessoSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcessoSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void montarListaUltimosProcSeletivos() {
		try {
			setListaConsultaProcessoSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcessoSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public List<SelectItem> tipoConsultaComboProcSeletivo;
	public List<SelectItem> getTipoConsultaComboProcSeletivo() {
		if(tipoConsultaComboProcSeletivo == null) {
		tipoConsultaComboProcSeletivo = new ArrayList<SelectItem>(0);
		tipoConsultaComboProcSeletivo.add(new SelectItem("descricao", "Descrição"));
		tipoConsultaComboProcSeletivo.add(new SelectItem("dataInicio", "Data Início"));
		tipoConsultaComboProcSeletivo.add(new SelectItem("dataFim", "Data Fim"));
		tipoConsultaComboProcSeletivo.add(new SelectItem("dataProva", "Data Prova"));
		}
		return tipoConsultaComboProcSeletivo;
	}
	

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	public void selecionarProcSeletivo() {
		ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
		setProcSeletivoVO(obj);		
	}
	
	public List<SelectItem> getListaSelectItemNivelEducacional() {
		if (listaSelectItemNivelEducacional == null) {
			listaSelectItemNivelEducacional = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, "valor", "descricao", true);
		}
		return listaSelectItemNivelEducacional;
	}

	public void setListaSelectItemNivelEducacional(List<SelectItem> listaSelectItemNivelEducacional) {
		this.listaSelectItemNivelEducacional = listaSelectItemNivelEducacional;
	}
	
	public void limparDadoProcSeletivo() {
		setProcSeletivoVO(new ProcSeletivoVO());
	}

	public String getNivelEducacional() {
		if(nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}
	

	public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcessoSeletivo().equals("dataInicio") || getCampoConsultaProcessoSeletivo().equals("dataFim") || getCampoConsultaProcessoSeletivo().equals("dataProva")) {
			return "return mascara(this.form,'formProcSeletivo:valorConsultaProcSeletivo','99/99/9999',event);";
		}
		return "";
	}

    
    public List<SelectItem> getListaSelectItemMotivosIndeferimentoDocumentoAluno() {
        if (listaSelectItemMotivosIndeferimentoDocumentoAluno == null) {
            listaSelectItemMotivosIndeferimentoDocumentoAluno = new ArrayList<>();
        }
        return listaSelectItemMotivosIndeferimentoDocumentoAluno;
    }

    public void setListaSelectItemMotivosIndeferimentoDocumentoAluno(List<SelectItem> listaSelectItemMotivosIndeferimentoDocumentoAluno) {
        this.listaSelectItemMotivosIndeferimentoDocumentoAluno = listaSelectItemMotivosIndeferimentoDocumentoAluno;
    }

    private void montarListaSelectItemMotivoIndeferimentoDocumentoAlunoAtivo(Boolean isLancarErroMotivosNaoEncontrados) throws Exception {
        getListaSelectItemMotivosIndeferimentoDocumentoAluno().clear();
        List<MotivoIndeferimentoDocumentoAlunoVO> resultado = getFacadeFactory().getMotivoIndeferimentoDocumentoAlunoInterfaceFacade().consultarMotivoIndeferimentoDocumentoAlunoPorSituacao(StatusAtivoInativoEnum.ATIVO, false, getUsuarioLogado());
        if (resultado.isEmpty() && isLancarErroMotivosNaoEncontrados) {
            throw new Exception(getMensagemInternalizacao("msg_MapaControleEntregaDocumentoUploadControle_ListaMotivoIndeferimentoDocumentoAlunoVazia"));
        }
        getListaSelectItemMotivosIndeferimentoDocumentoAluno().add(new SelectItem("", ""));
        for (MotivoIndeferimentoDocumentoAlunoVO motivo : resultado) {
            getListaSelectItemMotivosIndeferimentoDocumentoAluno().add(new SelectItem(motivo.getCodigo(), motivo.getNome()));
        }
    }
    
    public void reverterSituacaoEntregaDocumentoDocumentacaoMatricula() {
        try {
            getDocumetacaoMatricula().setEntregue(getDocumetacaoMatricula().getValorEntregueAnterior());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public String getCaminhoPreviewPdfA() {
		if (caminhoPreviewPdfA == null) {
			caminhoPreviewPdfA = Constantes.EMPTY;
		}
    	return caminhoPreviewPdfA;
	}
    
    public void setCaminhoPreviewPdfA(String caminhoPreviewPdfA) {
		this.caminhoPreviewPdfA = caminhoPreviewPdfA;
	}
    
    public void realizarConversaoPdfAImagem() {
		try {
			getDocumetacaoMatricula().setArquivoAssinado(new ArquivoVO());
			if (getDocumetacaoMatricula().getFileAssinar().exists()) {
				getDocumetacaoMatricula().getFileAssinar().delete();
			}
			getDocumetacaoMatricula().setRespAprovacaoDocDep(getUsuarioLogadoClone());
			if (assinarDocumentacao(getDocumetacaoMatricula().getMatricula())) {
				getDocumetacaoMatricula().setFileAssinar(getFacadeFactory().getDocumetacaoMatriculaFacade().unificarFrenteVersoDocumentoMatricula(getDocumetacaoMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			}
			getDocumetacaoMatricula().setValidarPdfA(Boolean.TRUE);
			getDocumetacaoMatricula().setRealizarConversaoPdfAImagem(Boolean.TRUE);
			getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(getDocumetacaoMatricula(), getMatriculaVisualizar(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVisualizar().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setCaminhoPreviewPdfA(getDocumetacaoMatricula().getCaminhoPreviewPdfA());
			setDocumetacaoMatricula(getDocumetacaoMatricula());
		} catch (Exception e) {
        	getDocumetacaoMatricula().getFileAssinar().delete();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void aprovarDocumentacaoMatricula() {
		try {
			getDocumetacaoMatricula().setRespAprovacaoDocDep(getUsuarioLogadoClone());
			getDocumetacaoMatricula().setValidarPdfA(Boolean.FALSE);
			getDocumetacaoMatricula().setRealizarConversaoPdfAImagem(Boolean.FALSE);
			getDocumetacaoMatricula().setMontarDadosArquivo(Boolean.FALSE);
			getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumentoMatriculaAprovaPeloDep(getDocumetacaoMatricula(), getMatriculaVisualizar(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVisualizar().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			getDocumetacaoMatricula().setArquivoAssinado(null);
			getDocumetacaoMatricula().setCaminhoPreviewPdfA(Constantes.EMPTY);
			if (getDocumetacaoMatricula().getFileAssinar().exists()) {
				getDocumetacaoMatricula().getFileAssinar().delete();
			}
			setDocumetacaoMatricula(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(getDocumetacaoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        	atualizarDocumento(getDocumetacaoMatricula());
			setMensagemDetalhada("");
			setMensagemID("Documento Deferido com Sucesso");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void cancelarDeferimentoDocumentacaoMatricula() { 
		try {
			getDocumetacaoMatricula().setArquivoAssinado(new ArquivoVO());
			if (getDocumetacaoMatricula().getFileAssinar().exists()) {
            	getDocumetacaoMatricula().getFileAssinar().delete();
            }
			setCaminhoPreviewPdfA(Constantes.EMPTY);
			atualizarDocumento(getDocumetacaoMatricula());
			setDocumetacaoMatricula(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
