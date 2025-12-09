package controle.processosel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ImportarCandidatoInscricaoProcessoSeletivoControle")
@Scope("viewScope")
@Lazy
public class ImportarCandidatoInscricaoProcessoSeletivoControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;

	private PessoaVO pessoaFiltroVO;
	private PessoaVO pessoaFiltroErroVO;
	private PessoaVO pessoaFiltroObservacaoVO;
	private String numeroInscricao;
	private FileUploadEvent fileUploadEvent;
	private List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoVOs;
	private List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoErroVOs;
	private List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoObservacaoVOs;
	private List<ProcSeletivoVO> listaProcessoSeletivoNaoEncontradoVOs;
	private List<UnidadeEnsinoVO> listaUnidadeEnsinoNaoEncontradoVOs;
	private List<CursoVO> listaCursoNaoEncontradoVOs;
	private List<TurnoVO> listaTurnoNaoEncontradoVOs;
	private ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoProcessoSeletivoVO;
	private Boolean opcaoSelecionarDesmarcarTodos;
	private List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoOriginalVOs;
	private List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs;
	private List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs;
	private String tipoLayout ;

	public ImportarCandidatoInscricaoProcessoSeletivoControle() {
		super();
	}

	public String novo() {
		Uteis.removerObjetoMemoria(this);
		
		setMensagemID("msg_entre_dados");
		return "importarCandidatoInscricaoProcessoSeletivoForm?faces-redirect=true";
	}
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<Object>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("importarCandidatoInscricaoProcessoSeletivoCons.xhtml");
	}
	

	
	public void inicializarDadosProgressBar(FileUploadEvent uploadEvent) {
		if (uploadEvent.getUploadedFile() != null) {
			getAplicacaoControle().getProgressBarImportarCandidatoVO().resetar();		
			getAplicacaoControle().getProgressBarImportarCandidatoVO().setUsuarioVO(getUsuarioLogado());
			getAplicacaoControle().getProgressBarImportarCandidatoVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			setFileUploadEvent(uploadEvent);
//			getAplicacaoControle().getProgressBarImportarCandidatoVO().setAssincrono(true);
			try {
				getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().inicializarDadosArquivoImportarCandidatoInscricaoProcessoSeletivo(getImportarCandidatoInscricaoProcessoSeletivoVO(), uploadEvent, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				e.printStackTrace();
			}
			getAplicacaoControle().getProgressBarImportarCandidatoVO().iniciar(0l, 100000, "Carregando o arquivo....", true, this, "realizarProcessamentoArquivoExcelCandidadoInscricaoProcessoSeletivo");
//			realizarProcessamentoArquivoExcelCandidadoInscricaoProcessoSeletivo();
		} else {			
			setMensagemDetalhada("msg_erro", "Selecione um arquivo para a prosseguir com a importação.");
		}
	}
	
	public void realizarProcessamentoArquivoExcelCandidadoInscricaoProcessoSeletivo() {
		try {
			setListaCandidatoInscricaoProcessoSeletivoVOs(getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().realizarProcessamentoExcelCandidadoInscricaoProcessoSeletivo(getFileUploadEvent(), getImportarCandidatoInscricaoProcessoSeletivoVO(), getPessoaFiltroVO(), getNumeroInscricao(), getListaCandidatoInscricaoProcessoSeletivoErroVOs(), getListaCandidatoInscricaoProcessoSeletivoObservacaoVOs(), getListaProcessoSeletivoNaoEncontradoVOs(), getListaUnidadeEnsinoNaoEncontradoVOs(), getListaCursoNaoEncontradoVOs(), getListaTurnoNaoEncontradoVOs(), getAplicacaoControle().getProgressBarImportarCandidatoVO().getConfiguracaoGeralSistemaVO(), false, getAplicacaoControle().getProgressBarImportarCandidatoVO(), getTipoLayout(),getAplicacaoControle().getProgressBarImportarCandidatoVO().getUsuarioVO()));				
			setListaCandidatoInscricaoProcessoSeletivoOriginalVOs(getListaCandidatoInscricaoProcessoSeletivoVOs());
			setListaCandidatoInscricaoProcessoSeletivoOriginalErroVOs(getListaCandidatoInscricaoProcessoSeletivoErroVOs());
			setListaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs(getListaCandidatoInscricaoProcessoSeletivoObservacaoVOs());
			getAplicacaoControle().getProgressBarImportarCandidatoVO().getSuperControle().setMensagemID("msg_dados_importados");			
			getAplicacaoControle().getProgressBarImportarCandidatoVO().setForcarEncerramento(true);
		} catch (Exception e) {
			getAplicacaoControle().getProgressBarImportarCandidatoVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
//			getAplicacaoControle().getProgressBarImportarCandidatoVO().getSuperControle().setMensagemDetalhada("msg_erro", e.getMessage());
			getAplicacaoControle().getProgressBarImportarCandidatoVO().setForcarEncerramento(true);
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		realizarProcessamentoExcelCandidadoInscricaoProcessoSeletivo();
	}

	public String realizarProcessamentoExcelCandidadoInscricaoProcessoSeletivo() {
		try {
			setListaCandidatoInscricaoProcessoSeletivoVOs(getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().realizarProcessamentoExcelCandidadoInscricaoProcessoSeletivo(getFileUploadEvent(), getImportarCandidatoInscricaoProcessoSeletivoVO(), getPessoaFiltroVO(), getNumeroInscricao(), getListaCandidatoInscricaoProcessoSeletivoErroVOs(), getListaCandidatoInscricaoProcessoSeletivoObservacaoVOs(), getListaProcessoSeletivoNaoEncontradoVOs(), getListaUnidadeEnsinoNaoEncontradoVOs(), getListaCursoNaoEncontradoVOs(), getListaTurnoNaoEncontradoVOs(), getConfiguracaoGeralPadraoSistema(), false, getAplicacaoControle().getProgressBarImportarCandidatoVO(), getTipoLayout(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "importacaoCons?faces-redirect=true";
	}

	public String irTelaConsulta() {
		getListaConsulta().clear();
		getControleConsulta().setValorConsulta("");
		setMensagemID("msg_entre_prmconsulta");
		return "importacaoCons?faces-redirect=true";
	}
	
	public void inicializarDadosProgressBar() {
		setListaCandidatoInscricaoProcessoSeletivoVOs(getListaCandidatoInscricaoProcessoSeletivoOriginalVOs());
		getAplicacaoControle().getProgressBarImportarCandidatoVO().resetar();		
		getAplicacaoControle().getProgressBarImportarCandidatoVO().setUsuarioVO(getUsuarioLogado());
		getAplicacaoControle().getProgressBarImportarCandidatoVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
		getAplicacaoControle().getProgressBarImportarCandidatoVO().iniciar(0l, (getListaCandidatoInscricaoProcessoSeletivoOriginalVOs().size()*2)+2, "Iniciando gravação dos dados...", true, this, "persistir");
		
	}

	public void persistir() {
		try {
//			inicializarDadosProgressBar();
			getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().persistir(getImportarCandidatoInscricaoProcessoSeletivoVO(), getListaCandidatoInscricaoProcessoSeletivoVOs(), true, getConfiguracaoGeralPadraoSistema(), getAplicacaoControle().getProgressBarImportarCandidatoVO(), getTipoLayout(), getAplicacaoControle().getProgressBarImportarCandidatoVO().getUsuarioVO());
			getAplicacaoControle().getProgressBarImportarCandidatoVO().getSuperControle().setMensagemID("msg_dados_gravados");
			getAplicacaoControle().getProgressBarImportarCandidatoVO().setForcarEncerramento(true);
		} catch (Exception e) {
			getPessoaFiltroVO().setNome("");
			getPessoaFiltroErroVO().setNome("");
			getPessoaFiltroVO().setNome("");
			getPessoaFiltroObservacaoVO().setNome("");
			getAplicacaoControle().getProgressBarImportarCandidatoVO().getSuperControle().setMensagemDetalhada("", e.getMessage());
			getAplicacaoControle().getProgressBarImportarCandidatoVO().setForcarEncerramento(true);
		}
	}
	
	public void realizarDownload() {
		ImportarCandidatoInscricaoProcessoSeletivoVO obj = (ImportarCandidatoInscricaoProcessoSeletivoVO) context().getExternalContext().getRequestMap().get("importarItens");
		try {
			try {
				obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			realizarDownloadArquivo(obj.getArquivoVO());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void downloadLayoutPadraoExcel() throws Exception {
		try {
			String xmlModeloUtilizar = "ModeloCandidatoInscricaoProcSeletivo.xlsx";
			if(getTipoLayout().equals("layout2")) {
				  xmlModeloUtilizar   ="ModeloCandidatoInscricaoProcSeletivo2.xlsx";
			}			
			File arquivo = new File(UteisJSF.getCaminhoWeb() + "arquivos" +  File.separator + xmlModeloUtilizar);
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
	
	public String consultar() {
		try {
			setListaConsulta(getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public PessoaVO getPessoaFiltroVO() {
		if (pessoaFiltroVO == null) {
			pessoaFiltroVO = new PessoaVO();
		}
		return pessoaFiltroVO;
	}

	public void setPessoaFiltroVO(PessoaVO pessoaFiltroVO) {
		this.pessoaFiltroVO = pessoaFiltroVO;
	}

	public FileUploadEvent getFileUploadEvent() {
		return fileUploadEvent;
	}

	public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
		this.fileUploadEvent = fileUploadEvent;
	}

	public String getNumeroInscricao() {
		if (numeroInscricao == null) {
			numeroInscricao = "";
		}
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> getListaCandidatoInscricaoProcessoSeletivoVOs() {
		if (listaCandidatoInscricaoProcessoSeletivoVOs == null) {
			listaCandidatoInscricaoProcessoSeletivoVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		}
		return listaCandidatoInscricaoProcessoSeletivoVOs;
	}

	public void setListaCandidatoInscricaoProcessoSeletivoVOs(List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoVOs) {
		this.listaCandidatoInscricaoProcessoSeletivoVOs = listaCandidatoInscricaoProcessoSeletivoVOs;
	}

	public List<ProcSeletivoVO> getListaProcessoSeletivoNaoEncontradoVOs() {
		if (listaProcessoSeletivoNaoEncontradoVOs == null) {
			listaProcessoSeletivoNaoEncontradoVOs = new ArrayList<ProcSeletivoVO>(0);
		}
		return listaProcessoSeletivoNaoEncontradoVOs;
	}

	public void setListaProcessoSeletivoNaoEncontradoVOs(List<ProcSeletivoVO> listaProcessoSeletivoNaoEncontradoVOs) {
		this.listaProcessoSeletivoNaoEncontradoVOs = listaProcessoSeletivoNaoEncontradoVOs;
	}

	public List<UnidadeEnsinoVO> getListaUnidadeEnsinoNaoEncontradoVOs() {
		if (listaUnidadeEnsinoNaoEncontradoVOs == null) {
			listaUnidadeEnsinoNaoEncontradoVOs = new ArrayList<UnidadeEnsinoVO>(0);
		}
		return listaUnidadeEnsinoNaoEncontradoVOs;
	}

	public void setListaUnidadeEnsinoNaoEncontradoVOs(List<UnidadeEnsinoVO> listaUnidadeEnsinoNaoEncontradoVOs) {
		this.listaUnidadeEnsinoNaoEncontradoVOs = listaUnidadeEnsinoNaoEncontradoVOs;
	}

	public List<CursoVO> getListaCursoNaoEncontradoVOs() {
		if (listaCursoNaoEncontradoVOs == null) {
			listaCursoNaoEncontradoVOs = new ArrayList<CursoVO>(0);
		}
		return listaCursoNaoEncontradoVOs;
	}

	public void setListaCursoNaoEncontradoVOs(List<CursoVO> listaCursoNaoEncontradoVOs) {
		this.listaCursoNaoEncontradoVOs = listaCursoNaoEncontradoVOs;
	}

	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> getListaCandidatoInscricaoProcessoSeletivoErroVOs() {
		if (listaCandidatoInscricaoProcessoSeletivoErroVOs == null) {
			listaCandidatoInscricaoProcessoSeletivoErroVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		}
		return listaCandidatoInscricaoProcessoSeletivoErroVOs;
	}

	public void setListaCandidatoInscricaoProcessoSeletivoErroVOs(List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoErroVOs) {
		this.listaCandidatoInscricaoProcessoSeletivoErroVOs = listaCandidatoInscricaoProcessoSeletivoErroVOs;
	}

	public Integer getQuantidadeCandidatos() {
		return getListaCandidatoInscricaoProcessoSeletivoOriginalVOs().size();
	}
	
	public Integer getQuantidadeCandidatosAptosImportacao() {
		Integer qtde = 0;
		for (ImportarCandidatoInscricaoProcessoSeletivoVO importarVO : getListaCandidatoInscricaoProcessoSeletivoOriginalVOs()) {
			if (!importarVO.getPossuiErro()) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public Integer getQuantidadeCandidatoErro() {
		Integer qtde = 0;
		for (ImportarCandidatoInscricaoProcessoSeletivoVO importarVO : getListaCandidatoInscricaoProcessoSeletivoOriginalErroVOs()) {
			if (importarVO.getPossuiErro()) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public Integer getQuantidadeCandidatoObservacao() {
		Integer qtde = 0;
		for (ImportarCandidatoInscricaoProcessoSeletivoVO importarVO : getListaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs()) {
			if (importarVO.getPossuiObservacao()) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public Integer getQuantidadeCandidatoSelecionado() {
		Integer qtde = 0;
		for (ImportarCandidatoInscricaoProcessoSeletivoVO importarVO : getListaCandidatoInscricaoProcessoSeletivoOriginalVOs()) {
			if (importarVO.getSelecionado()) {
				qtde++;
			}
		}
		return qtde;
	}
	
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("responsavel", "Responsável"));
		itens.add(new SelectItem("data", "Data"));
		return itens;
	}

	public List<TurnoVO> getListaTurnoNaoEncontradoVOs() {
		if (listaTurnoNaoEncontradoVOs == null) {
			listaTurnoNaoEncontradoVOs = new ArrayList<TurnoVO>(0);
		}
		return listaTurnoNaoEncontradoVOs;
	}

	public void setListaTurnoNaoEncontradoVOs(List<TurnoVO> listaTurnoNaoEncontradoVOs) {
		this.listaTurnoNaoEncontradoVOs = listaTurnoNaoEncontradoVOs;
	}

	public ImportarCandidatoInscricaoProcessoSeletivoVO getImportarCandidatoInscricaoProcessoSeletivoVO() {
		if (importarCandidatoInscricaoProcessoSeletivoVO == null) {
			importarCandidatoInscricaoProcessoSeletivoVO = new ImportarCandidatoInscricaoProcessoSeletivoVO();
		}
		return importarCandidatoInscricaoProcessoSeletivoVO;
	}

	public void setImportarCandidatoInscricaoProcessoSeletivoVO(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoInscricaoProcessoSeletivoVO) {
		this.importarCandidatoInscricaoProcessoSeletivoVO = importarCandidatoInscricaoProcessoSeletivoVO;
	}
	
	public Boolean getApresentarData() {
		return getControleConsulta().getCampoConsulta().equals("data");
	}
	
	
	public void selecionarTodosDesmarcarTodos() {
		for (ImportarCandidatoInscricaoProcessoSeletivoVO imp : getListaCandidatoInscricaoProcessoSeletivoVOs()) {
			if (imp.getInscricaoExistente()) {
				continue;
			}
			imp.setSelecionado(getOpcaoSelecionarDesmarcarTodos());
		}
	}

	public Boolean getOpcaoSelecionarDesmarcarTodos() {
		if (opcaoSelecionarDesmarcarTodos == null) {
			opcaoSelecionarDesmarcarTodos = true;
		}
		return opcaoSelecionarDesmarcarTodos;
	}

	public void setOpcaoSelecionarDesmarcarTodos(Boolean opcaoSelecionarDesmarcarTodos) {
		this.opcaoSelecionarDesmarcarTodos = opcaoSelecionarDesmarcarTodos;
	}

	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> getListaCandidatoInscricaoProcessoSeletivoOriginalVOs() {
		if (listaCandidatoInscricaoProcessoSeletivoOriginalVOs == null) {
			listaCandidatoInscricaoProcessoSeletivoOriginalVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		}
		return listaCandidatoInscricaoProcessoSeletivoOriginalVOs;
	}

	public void setListaCandidatoInscricaoProcessoSeletivoOriginalVOs(List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoOriginalVOs) {
		this.listaCandidatoInscricaoProcessoSeletivoOriginalVOs = listaCandidatoInscricaoProcessoSeletivoOriginalVOs;
	}
	
	public void consultarPorNomeCandidatos() {
		if (getPessoaFiltroVO().getNome().equals("")) {
			setListaCandidatoInscricaoProcessoSeletivoVOs(getListaCandidatoInscricaoProcessoSeletivoOriginalVOs());
		} else {
			setListaCandidatoInscricaoProcessoSeletivoVOs(getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().consultarPorNomeCandidatos(getPessoaFiltroVO().getNome(), getListaCandidatoInscricaoProcessoSeletivoOriginalVOs(), getUsuarioLogado()));
		}
	}
	
	public void consultarPorNomeCandidatosErro() {
		if (getPessoaFiltroErroVO().getNome().equals("")) {
			setListaCandidatoInscricaoProcessoSeletivoErroVOs(getListaCandidatoInscricaoProcessoSeletivoOriginalErroVOs());
		} else {
			setListaCandidatoInscricaoProcessoSeletivoErroVOs(getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().consultarPorNomeCandidatos(getPessoaFiltroErroVO().getNome(), getListaCandidatoInscricaoProcessoSeletivoOriginalErroVOs(), getUsuarioLogado()));
		}
	}
	
	public void consultarPorNomeCandidatosObservacao() {
		if (getPessoaFiltroObservacaoVO().getNome().equals("")) {
			setListaCandidatoInscricaoProcessoSeletivoObservacaoVOs(getListaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs());
		} else {
			setListaCandidatoInscricaoProcessoSeletivoObservacaoVOs(getFacadeFactory().getImportarCandidatoInscricaoProcessoSeletivoFacade().consultarPorNomeCandidatos(getPessoaFiltroObservacaoVO().getNome(), getListaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs(), getUsuarioLogado()));
		}
	}

	public PessoaVO getPessoaFiltroErroVO() {
		if (pessoaFiltroErroVO == null) {
			pessoaFiltroErroVO = new PessoaVO();
		}
		return pessoaFiltroErroVO;
	}

	public void setPessoaFiltroErroVO(PessoaVO pessoaFiltroErroVO) {
		this.pessoaFiltroErroVO = pessoaFiltroErroVO;
	}

	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> getListaCandidatoInscricaoProcessoSeletivoOriginalErroVOs() {
		if (listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs == null) {
			listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		}
		return listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs;
	}

	public void setListaCandidatoInscricaoProcessoSeletivoOriginalErroVOs(List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs) {
		this.listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs = listaCandidatoInscricaoProcessoSeletivoOriginalErroVOs;
	}

	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> getListaCandidatoInscricaoProcessoSeletivoObservacaoVOs() {
		if (listaCandidatoInscricaoProcessoSeletivoObservacaoVOs == null) {
			listaCandidatoInscricaoProcessoSeletivoObservacaoVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		}
		return listaCandidatoInscricaoProcessoSeletivoObservacaoVOs;
	}

	public void setListaCandidatoInscricaoProcessoSeletivoObservacaoVOs(List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoObservacaoVOs) {
		this.listaCandidatoInscricaoProcessoSeletivoObservacaoVOs = listaCandidatoInscricaoProcessoSeletivoObservacaoVOs;
	}

	public PessoaVO getPessoaFiltroObservacaoVO() {
		if (pessoaFiltroObservacaoVO == null) {
			pessoaFiltroObservacaoVO = new PessoaVO();
		}
		return pessoaFiltroObservacaoVO;
	}

	public void setPessoaFiltroObservacaoVO(PessoaVO pessoaFiltroObservacaoVO) {
		this.pessoaFiltroObservacaoVO = pessoaFiltroObservacaoVO;
	}

	public List<ImportarCandidatoInscricaoProcessoSeletivoVO> getListaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs() {
		if (listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs == null) {
			listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs = new ArrayList<ImportarCandidatoInscricaoProcessoSeletivoVO>(0);
		}
		return listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs;
	}

	public void setListaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs(List<ImportarCandidatoInscricaoProcessoSeletivoVO> listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs) {
		this.listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs = listaCandidatoInscricaoProcessoSeletivoOriginalObservacaoVOs;
	}

	public List<SelectItem> getTipoLayoutConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("layout1", "Layout 1 - Uma opção de Unidade de Ensino"));
		itens.add(new SelectItem("layout2", "Layout 2 - Cinco opções de Unidade de Ensino"));
		return itens;
	}
	
	
	public String getTipoLayout() {
		if(tipoLayout ==null ) {
			tipoLayout ="";
		}
		return tipoLayout;
	}
	
	public void setTipoLayout(String tipoLayout) {
		this.tipoLayout = tipoLayout ;
	}
	
}
