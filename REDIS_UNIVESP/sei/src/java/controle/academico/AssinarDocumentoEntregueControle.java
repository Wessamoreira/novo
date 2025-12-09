package controle.academico;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.AssinarDocumentoEntregueVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Mattheus Nonato
 */
@Controller("AssinarDocumentoEntregueControle")
@Scope("viewScope")
@Lazy
public class AssinarDocumentoEntregueControle extends SuperControle {

	private static final long serialVersionUID = -9150581099310868495L;
	private AssinarDocumentoEntregueVO assinarDocumentoEntregueVO;
	private List<SelectItem> listaSelecItemServidorArquivoOnline;
	private List<SelectItem> listaSelectItemOrigemArquivo;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaProcessadaComSucesso;
	private List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaProcessadaComFalha;
	private ProgressBarVO progressBarVO;
	private String campoConsultarMatricula;
	private String valorConsultarMatricula;
	private List<SelectItem> listaSelectItemTipoConsultaMatricula;
	private List<MatriculaVO> matriculaVOs;
	private String matriculaFiltro;
	private String alunoFiltro;
	private String nomeDocumentoFiltro;
	private Date dataInicioFiltro;
	private Date dataFimFiltro;
	
	public AssinarDocumentoEntregueControle() {
			limparMensagem();
	}
	
	@PostConstruct
	public void init() {
		getAssinarDocumentoEntregueVO().setDataInicioEntrega(Uteis.getDataPrimeiroDiaMes(new Date()));
		getAssinarDocumentoEntregueVO().setDataFimEntrega(Uteis.getDataUltimoDiaMes(new Date()));
		setControleConsulta(new ControleConsulta());
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		inicializarListaDocumentoProcessadoComSucesso();
		montarListaSelectItemServidorArquivoOnlineEnum();
		montarListaOrigemDocumento();
		inicializarDadosListaSelectItemUnidadelEnsino();
		setProgressBarVO(getFacadeFactory().getAssinarDocumentoEntregueInterfaceFacade().consultarProgressBarEmExecucao());
		setAbaAtiva("richTab");
	}
	
	public void realizarAssinaturaDocumento() {
		try {
			limparMensagem();
			progressBarVO.resetar();
			getAplicacaoControle().getMapThreadIndiceReajuste().put("AssinarDocumentoEntregue", progressBarVO);
			progressBarVO.setUsuarioVO(getUsuarioLogado());
			progressBarVO.setAplicacaoControle(getAplicacaoControle());
			progressBarVO.setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			progressBarVO.iniciar(1l, 10, "Iniciando.....", true, this, "assinarDocumentoEntregue");
//			getFacadeFactory().getAssinarDocumentoEntregueInterfaceFacade().assinarDocumentoEntregue(getProgressBarVO(),assinarDocumentoEntregueVO, Uteis.NIVELMONTARDADOS_COMBOBOX, getConfiguracaoGeralPadraoSistema(), progressBarVO.getUsuarioVO());
			
			setAbaAtiva("richTab");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getAplicacaoControle().getMapThreadIndiceReajuste().remove("AssinarDocumentoEntregue");
		}
	}
	
	public void assinarDocumentoEntregue() {
		try {
			
			getFacadeFactory().getAssinarDocumentoEntregueInterfaceFacade().assinarDocumentoEntregue(getProgressBarVO(),getAssinarDocumentoEntregueVO(), Uteis.NIVELMONTARDADOS_COMBOBOX, getProgressBarVO().getConfiguracaoGeralSistemaVO(), getProgressBarVO().getUsuarioVO());
		} catch (Exception e) {
			getProgressBarVO().setForcarEncerramento(true);
//			getProgressBarVO().encerrar(); 
			getProgressBarVO().getAplicacaoControle().getMapThreadIndiceReajuste().remove("AssinarDocumentoEntregue");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/* Inicio Dados Documentação Matrícula */
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		inicializarListaDocumentoProcessadoComSucesso();
	}
	
	public void scrollerListenerComErro(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		inicializarListaDocumentoProcessadoComErro();
	}
	
	public void inicializarListaDocumentoProcessadoSucesso() {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		setAbaAtiva("richTab");
		setDataInicioFiltro(new Date());
		setDataFimFiltro(new Date());
		setMatriculaFiltro(getAssinarDocumentoEntregueVO().getMatriculaVO().getMatricula());
		inicializarListaDocumentoProcessadoComSucesso();
	}
	
	public void inicializarListaDocumentoProcessadoComSucesso() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorProcessadoComErro(Boolean.FALSE, getMatriculaFiltro(), getAlunoFiltro(), getNomeDocumentoFiltro(), getDataInicioFiltro(), getDataFimFiltro(), 
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, getControleConsultaOtimizado(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	public void inicializarListaDocumentoProcessadoErro() {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		inicializarListaDocumentoProcessadoComErro();
	}
	
	public void inicializarListaDocumentoProcessadoComErro() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorProcessadoComErro(Boolean.TRUE, getMatriculaFiltro(), getAlunoFiltro(), getNomeDocumentoFiltro(), getDataInicioFiltro(), getDataFimFiltro(), 
					Uteis.NIVELMONTARDADOS_DADOSBASICOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, getControleConsultaOtimizado() ,getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	/* Fim Dados Documentação Matrícula */
	
	/* Inicio Dados Documentação GED */
	
	public void scrollerListenerGED(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		inicializarListaDocumentoProcessadoComSucesso();
	}
	
	public void scrollerListenerGEDComErro(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		inicializarListaDocumentoProcessadoComErro();
	}
	
	public void inicializarListaDocumentoGEDProcessadoSucesso() {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		inicializarListaDocumentoGEDProcessadoComSucesso();
	}
	
	public void inicializarListaDocumentoGEDProcessadoComSucesso() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarDocumentoGEDPorStatusProcessamento(Boolean.FALSE, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarTotalDocumentosProcessados(Boolean.FALSE, false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	public void inicializarListaDocumentoGEGProcessadoErro() {
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		inicializarListaDocumentoGEDProcessadoComErro();
	}
	
	public void inicializarListaDocumentoGEDProcessadoComErro() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarDocumentoGEDPorStatusProcessamento(Boolean.TRUE, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarTotalDocumentosProcessados(Boolean.TRUE, false, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}	
	}
	
	/* FIM Dados Documentação GED */

	private void montarListaOrigemDocumento() {
		getListaSelectItemOrigemArquivo().clear();
		getListaSelectItemOrigemArquivo().add(new SelectItem("DM", "Documentação Matricula"));
		getListaSelectItemOrigemArquivo().add(new SelectItem("DC", "Documentação GED"));
	}

	public void  montarListaSelectItemServidorArquivoOnlineEnum() {
		getListaSelecItemServidorArquivoOnline().clear();
		ServidorArquivoOnlineEnum[] valores = ServidorArquivoOnlineEnum.values();
		for (ServidorArquivoOnlineEnum obj : valores) {
			getListaSelecItemServidorArquivoOnline().add(new SelectItem(obj.name(), obj.getDescricao()));
		}
	}
	
	   public void inicializarDadosListaSelectItemUnidadelEnsino() {
	        try {
	            List<UnidadeEnsinoVO> listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaResultado, "codigo", "nome"));
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        }
	    }
	   
	   public void atualizarStatusProcessamento() {
		   ProgressBarVO pb = getFacadeFactory().getAssinarDocumentoEntregueInterfaceFacade().consultarProgressBarEmExecucao();
		   if(Uteis.isAtributoPreenchido(pb)) {
			   setProgressBarVO(pb);   
		   }else {
			   setControleConsulta(new ControleConsulta());
			   getControleConsultaOtimizado().setPaginaAtual(1);
			   getControleConsultaOtimizado().setPage(1);
			   inicializarListaDocumentoProcessadoComSucesso();
			   setProgressBarVO(null);
		   }
	   }

	/*INICIO GETTERS AND SETTER */
	public List<SelectItem> getListaSelectItemOrigemArquivo() {
		if(listaSelectItemOrigemArquivo == null) {
			listaSelectItemOrigemArquivo = new ArrayList<SelectItem>();
		}
		return listaSelectItemOrigemArquivo;
	}

	public void setListaSelectItemOrigemArquivo(List<SelectItem> listaSelectItemOrigemArquivo) {
		this.listaSelectItemOrigemArquivo = listaSelectItemOrigemArquivo;
	}

	public List<SelectItem> getListaSelecItemServidorArquivoOnline() {
		if(listaSelecItemServidorArquivoOnline == null) {
			listaSelecItemServidorArquivoOnline = new ArrayList<SelectItem>();
		}
		return listaSelecItemServidorArquivoOnline;
	}

	public void setListaSelecItemServidorArquivoOnline(List<SelectItem> listaSelecItemServidorArquivoOnline) {
		this.listaSelecItemServidorArquivoOnline = listaSelecItemServidorArquivoOnline;
	}

	public AssinarDocumentoEntregueVO getAssinarDocumentoEntregueVO() {
		if(assinarDocumentoEntregueVO == null) {
			assinarDocumentoEntregueVO = new AssinarDocumentoEntregueVO();
		}
		return assinarDocumentoEntregueVO;
	}

	public void setAssinarDocumentoEntregueVO(AssinarDocumentoEntregueVO assinarDocumentoEntregueVO) {
		this.assinarDocumentoEntregueVO = assinarDocumentoEntregueVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<DocumetacaoMatriculaVO> getListaDocumentacaoMatriculaProcessadaComSucesso() {
		if(listaDocumentacaoMatriculaProcessadaComSucesso == null) {
			listaDocumentacaoMatriculaProcessadaComSucesso = new ArrayList<DocumetacaoMatriculaVO>();
		}
		return listaDocumentacaoMatriculaProcessadaComSucesso;
	}

	public void setListaDocumentacaoMatriculaProcessadaComSucesso(
			List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaProcessadaComSucesso) {
		this.listaDocumentacaoMatriculaProcessadaComSucesso = listaDocumentacaoMatriculaProcessadaComSucesso;
	}

	public List<DocumetacaoMatriculaVO> getListaDocumentacaoMatriculaProcessadaComFalha() {
		if(listaDocumentacaoMatriculaProcessadaComFalha == null) {
			listaDocumentacaoMatriculaProcessadaComFalha = new ArrayList<DocumetacaoMatriculaVO>();
		}
		return listaDocumentacaoMatriculaProcessadaComFalha;
	}

	public void setListaDocumentacaoMatriculaProcessadaComFalha(List<DocumetacaoMatriculaVO> listaDocumentacaoMatriculaProcessadaComFalha) {
		this.listaDocumentacaoMatriculaProcessadaComFalha = listaDocumentacaoMatriculaProcessadaComFalha;
	}

	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	public String getCampoConsultarMatricula() {
		if (campoConsultarMatricula == null) {
			campoConsultarMatricula = "nomePessoa";
		}
		return campoConsultarMatricula;
	}

	public void setCampoConsultarMatricula(String campoConsultarMatricula) {
		this.campoConsultarMatricula = campoConsultarMatricula;
	}

	public String getValorConsultarMatricula() {
		if (valorConsultarMatricula == null) {
			valorConsultarMatricula = "";
		}
		return valorConsultarMatricula;
	}

	public void setValorConsultarMatricula(String valorConsultarMatricula) {
		this.valorConsultarMatricula = valorConsultarMatricula;
	}

	public List<SelectItem> getListaSelectItemTipoConsultaMatricula() {
		if (listaSelectItemTipoConsultaMatricula == null) {
			listaSelectItemTipoConsultaMatricula = Arrays.asList(
					new SelectItem("nomePessoa", "Aluno"),
					new SelectItem("matricula", "Matrícula"), 
					new SelectItem("nomeCurso", "Curso"));
		}
		return listaSelectItemTipoConsultaMatricula;
	}

	public void setListaSelectItemTipoConsultaMatricula(List<SelectItem> listaSelectItemTipoConsultaMatricula) {
		this.listaSelectItemTipoConsultaMatricula = listaSelectItemTipoConsultaMatricula;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<>();
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}
	
	public void consultarMatricula() {
		try {
			if (getValorConsultarMatricula().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultarMatricula().equals("matricula")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultarMatricula(),
						getAssinarDocumentoEntregueVO().getCodigoUnidadeEnsino(), false, getUsuarioLogado()));
			} else if (getCampoConsultarMatricula().equals("nomePessoa")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultarMatricula(), 
						getAssinarDocumentoEntregueVO().getCodigoUnidadeEnsino(), false, getUsuarioLogado()));
			} else if (getCampoConsultarMatricula().equals("nomeCurso")) {
				setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultarMatricula(),
						getAssinarDocumentoEntregueVO().getCodigoUnidadeEnsino(), false, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaVOs(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarMatricula() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		if (obj != null) {
			selecionarMatricula(obj);
		}
	}
	
	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getAssinarDocumentoEntregueVO().getMatriculaVO().getMatricula(), 
					getAssinarDocumentoEntregueVO().getCodigoUnidadeEnsino(), NivelMontarDados.BASICO, getUsuarioLogado());
			selecionarMatricula(objAluno);
		} catch (Exception e) {
			limparCampoMatricula();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void selecionarMatricula(MatriculaVO objAluno) {
		getAssinarDocumentoEntregueVO().setMatriculaVO(objAluno);
		if (!Uteis.isAtributoPreenchido(getAssinarDocumentoEntregueVO().getCodigoUnidadeEnsino())) {
			getAssinarDocumentoEntregueVO().setCodigoUnidadeEnsino(getAssinarDocumentoEntregueVO().getMatriculaVO().getUnidadeEnsino().getCodigo());
		}
	}
	
	public void limparCampoMatricula() {
		getAssinarDocumentoEntregueVO().setMatriculaVO(new MatriculaVO());
	}
	
	public void limparCampoConsultaMatricula() {
		setMatriculaVOs(new ArrayList<>());
		setValorConsultarMatricula("");
	}
	
	public void realizarValidacaoTrocaUnidadeEnsino() {
		if (Uteis.isAtributoPreenchido(getAssinarDocumentoEntregueVO().getMatriculaVO().getMatricula()) 
				&& !getAssinarDocumentoEntregueVO().getCodigoUnidadeEnsino().equals(getAssinarDocumentoEntregueVO().getMatriculaVO().getUnidadeEnsino().getCodigo())) {
			getAssinarDocumentoEntregueVO().setMatriculaVO(new MatriculaVO());
		}
	}
	public String getMatriculaFiltro() {
		if (matriculaFiltro == null) {
			matriculaFiltro = "";
		}
		return matriculaFiltro;
	}

	public void setMatriculaFiltro(String matriculaFiltro) {
		this.matriculaFiltro = matriculaFiltro;
	}

	public String getAlunoFiltro() {
		if (alunoFiltro == null) {
			alunoFiltro = "";
		}
		return alunoFiltro;
	}

	public void setAlunoFiltro(String alunoFiltro) {
		this.alunoFiltro = alunoFiltro;
	}

	public String getNomeDocumentoFiltro() {
		if (nomeDocumentoFiltro == null) {
			nomeDocumentoFiltro = "";
		}
		return nomeDocumentoFiltro;
	}

	public void setNomeDocumentoFiltro(String nomeDocumentoFiltro) {
		this.nomeDocumentoFiltro = nomeDocumentoFiltro;
	}

	public Date getDataInicioFiltro() {
		return dataInicioFiltro;
	}

	public void setDataInicioFiltro(Date dataInicioFiltro) {
		this.dataInicioFiltro = dataInicioFiltro;
	}

	public Date getDataFimFiltro() {
		return dataFimFiltro;
	}

	public void setDataFimFiltro(Date dataFimFiltro) {
		this.dataFimFiltro = dataFimFiltro;
	}

	public void consultarArquivoProcessadoPorFiltro() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorProcessadoComErro(Boolean.FALSE,
							getMatriculaFiltro(), getAlunoFiltro(), getNomeDocumentoFiltro(), getDataInicioFiltro(), getDataFimFiltro(), 
							Uteis.NIVELMONTARDADOS_DADOSBASICOS, getControleConsultaOtimizado().getLimitePorPagina(),
							getControleConsultaOtimizado().getOffset(), false, getControleConsultaOtimizado(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
