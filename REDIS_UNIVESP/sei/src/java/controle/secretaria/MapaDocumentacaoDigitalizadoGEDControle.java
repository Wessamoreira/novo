package controle.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoGEDVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.facade.jdbc.academico.DocumentacaoGED;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.aws.s3.ServidorArquivoOnlineS3RS;

@Controller("MapaDocumentacaoDigitalizadoGEDControle")
@Scope("viewScope")
public class MapaDocumentacaoDigitalizadoGEDControle extends SuperControle {

	private static final long serialVersionUID = 3032617156881773313L;

	private DocumentacaoGEDVO documentacaoGED;
	private TipoDocumentoVO tipoDocumento;	
	
	private List<MatriculaVO> listaConsultaAluno;
	private List<TipoDocumentoVO> listaConsultaTipoDocumento;
	private List<TipoDocumentoGEDVO> listaTipoDocumentoGEDAnterior;
	private List<DocumetacaoMatriculaVO> listaDocumetacaoMatricula;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	
	private String campoConsultaTipoDocumento;
	private String valorConsultaTipoDocumento;
	
	private String valorConsultaSituacao;
	
	private List<SelectItem> listaSelectItemCategoriaGED;
	
	public MapaDocumentacaoDigitalizadoGEDControle() {
		novo();
		getControleConsultaOtimizado().setLimitePorPagina(10);
		getControleConsultaOtimizado().setOffset(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
		consultar();
	}
	
	public void novo() {
		removerObjetoMemoria(this);
		limparMensagem();
	}

	public void persistir() {
		try {	
			setOncompleteModal("");
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().persistirComUploadArquivo(documentacaoGED, listaTipoDocumentoGEDAnterior, getConfiguracaoGeralPadraoSistema(), Boolean.TRUE, getUsuarioLogado());
			setOncompleteModal("RichFaces.$('panelCategoriaGED').hide()");
			setMensagemID("msg_dados_gravados");
			this.consultar();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}

	@SuppressWarnings("unchecked")
	public void persistirComUploadArquivo() {
		try {
			documentacaoGED.setListaTipoDocumentoGED(getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().consultarPorDocumentacaoGED(false, getUsuarioLogado(), documentacaoGED.getCodigo()));
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().persistirComUploadArquivo(documentacaoGED, documentacaoGED.getListaTipoDocumentoGED(),  getConfiguracaoGeralPadraoSistema(), Boolean.TRUE, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			this.consultar();
		} catch (Exception e) {
			documentacaoGED.setSituacao("Erro");
			documentacaoGED.setArquivo(new ArquivoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorFiltro(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true, getUsuarioLogado(), getValorConsultaSituacao(), getControleConsultaOtimizado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarTotal(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), false, getUsuarioLogado(), getValorConsultaSituacao()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>DocumentacaoGED</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */	
	public void editar() {
		setDocumentacaoGED(new DocumentacaoGEDVO());
		DocumentacaoGEDVO obj = (DocumentacaoGEDVO) context().getExternalContext().getRequestMap().get("mapaDocumentoGED");		
		try {
			setDocumentacaoGED(getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS));
			setListaTipoDocumentoGEDAnterior(new ArrayList<TipoDocumentoGEDVO>(0));
			for (TipoDocumentoGEDVO object : getDocumentacaoGED().getListaTipoDocumentoGED()) {
				getListaTipoDocumentoGEDAnterior().add(object);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		if (getDocumentacaoGED().getSituacao().equals("Erro")) {
			setMensagemDetalhada(obj.getMensagem());
		} else {
			setMensagemDetalhada("");
		}
		getDocumentacaoGED().setUsuario(getUsuarioLogadoClone());
		
	}

	public String excluir(DocumentacaoGEDVO documentacaoGEDVO) {
		try {
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().excluir(documentacaoGEDVO, true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			consultar();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaDocumentacaoDigitalizadoGEDCons");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("mapaDocumentacaoDigitalizadoGEDCons");
		}
	}

	/**
	 * Rotina responsavel por realizar a paginação da pagina de mapaDocuentacaoDigitalizadoGED.xhtml
	 * 
	 * @param DataScrollEvent
	 * @throws Exception
	 */
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	
	private List<SelectItem> tipoConsultaCombo;
	
	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
			tipoConsultaCombo.add(new SelectItem("matricula", "Matricula"));
			tipoConsultaCombo.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaCombo.add(new SelectItem("categoriaged", "Categoria GED"));
		}
		return tipoConsultaCombo;
	}
	
	private List<SelectItem> comboSituacaoDocumentoGED;
	
	public List<SelectItem> getComboSituacaoDocumentoGED() {
		if (comboSituacaoDocumentoGED == null) {
			comboSituacaoDocumentoGED = new ArrayList<>(0);
			comboSituacaoDocumentoGED.add(new SelectItem("todos", "Todos"));
			comboSituacaoDocumentoGED.add(new SelectItem("sucesso", "Sucesso"));
			comboSituacaoDocumentoGED.add(new SelectItem("erro", "Erro"));
		}
		return comboSituacaoDocumentoGED;
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().length() < 2) {
				throw new Exception("Digite no mínimo 2 digitos para consultar");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		MatriculaVO matricula = new MatriculaVO();
		try {
			matricula = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(this.getDocumentacaoGED().getMatricula().getMatricula(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (matricula.getMatricula().equals("")) {
				limparDadosMatricula();
				throw new Exception("Aluno de matrícula " + this.getDocumentacaoGED().getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.getDocumentacaoGED().setMatricula(matricula);
			this.getDocumentacaoGED().setPessoa(matricula.getAluno());
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().realizarLocalizacaoDocumentacaoMatriculaVincularDocumentoGED(getDocumentacaoGED());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
	}
	
	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		getDocumentacaoGED().setMatricula(obj);
		getDocumentacaoGED().setPessoa(obj.getAluno());
		getFacadeFactory().getDocumentacaoGEDInterfaceFacade().realizarLocalizacaoDocumentacaoMatriculaVincularDocumentoGED(getDocumentacaoGED());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	private List<SelectItem> tipoConsultaComboAluno;
	
	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}

	public void consultarTipoDocumento() {
		try {
			listaConsultaTipoDocumento = new ArrayList<TipoDocumentoVO>(0);
			
			if (getCampoConsultaTipoDocumento().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}

			if (!Uteis.isAtributoPreenchido(documentacaoGED.getCategoriaGED().getCodigo())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_DocumentacaoGED_categoriaGED"));
			}

			listaConsultaTipoDocumento = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorIdentificadorGEDCategoriaGED(getCampoConsultaTipoDocumento(), getValorConsultaTipoDocumento(), documentacaoGED, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarObjetoDocumentacaoGED(DocumentacaoGEDVO documentacaoGED) {
		try {
			this.documentacaoGED = documentacaoGED;
			setMensagemID("msg_entre_dados");
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			documentacaoGED.getArquivo().setCpfAlunoDocumentacao(documentacaoGED.getPessoa().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, documentacaoGED.getArquivo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP, getUsuarioLogado());
			getDocumentacaoGED().setUploadNovoArquivo(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public void adicionarArquivoDocumentacaoGED() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DocumentacaoGED", "Iniciando Adicionar Arquivo Documentação GED", "Uploading");
			documentacaoGED.getArquivo().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			documentacaoGED.getArquivo().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			documentacaoGED.getArquivo().setCodigo(getUsuarioLogado().getCodigo());
			
			documentacaoGED.getArquivo().setDataUpload(new Date());
			documentacaoGED.getArquivo().setManterDisponibilizacao(true);
			documentacaoGED.getArquivo().setDataDisponibilizacao(documentacaoGED.getArquivo().getDataUpload());
			documentacaoGED.getArquivo().setDataIndisponibilizacao(null);
			documentacaoGED.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
			documentacaoGED.getArquivo().setOrigem(OrigemArquivo.DOCUMENTO_GED.getValor());

			documentacaoGED.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
			registrarAtividadeUsuario(getUsuarioLogado(), "DocumentacaoGED", "Finalizando Adicionar Arquivo Documentação GED", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDadosTipoDocumento() {
		int x = 0;
		for(TipoDocumentoVO tipoDocumentoVO: getlistaConsultaTipoDocumento()) {
			if(tipoDocumentoVO.getCodigo().equals(getTipoDocumento().getCodigo())) {
				getlistaConsultaTipoDocumento().remove(x);
				break;
			}
			x++;
		}
		this.setTipoDocumento(new TipoDocumentoVO());
		setMensagemDetalhada("");
		setMensagemID("msg_entre_dados");
	}
	
	public void limparMensagem() {
		setMensagemDetalhada("");
		setMensagem("");
	}

	public void selecionarTipoDocumento() {
		TipoDocumentoVO obj = (TipoDocumentoVO) context().getExternalContext().getRequestMap().get("tipoDocumentoItens");
		this.setTipoDocumento(obj);
		adicionarTipoDocumento();
		valorConsultaTipoDocumento = "";
		campoConsultaTipoDocumento = "";
	}
	
	private List<SelectItem> tipoConsultaComboTipoDocumento;
	
	public List<SelectItem> getTipoConsultaComboTipoDocumento() {
		if (tipoConsultaComboTipoDocumento == null) {
			tipoConsultaComboTipoDocumento = new ArrayList<>(0);
			tipoConsultaComboTipoDocumento.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboTipoDocumento.add(new SelectItem("identificadorGED", "Identificador GED"));
			tipoConsultaComboTipoDocumento.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboTipoDocumento;
	}

	/**
	 * Limpa o campo de matricula .
	 */
	public void limparDadosMatricula(){
		getDocumentacaoGED().setMatricula(null);
		getDocumentacaoGED().getListaTipoDocumentoGED().forEach(item -> item.setDocumetacaoMatricula(null));
		setMensagemDetalhada("");
	}
	
	public void adicionarTipoDocumento() {
		try {
			setMensagemDetalhada("");			
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().adicionarTipoDocumento(getDocumentacaoGED(), getTipoDocumento(), getUsuarioLogado());
			limparDadosTipoDocumento();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagem("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	public void remover(TipoDocumentoGEDVO obj) {
		setMensagemDetalhada("");
		Iterator iterator = documentacaoGED.getListaTipoDocumentoGED().iterator();
		while(iterator.hasNext()) {
			TipoDocumentoGEDVO tipoDocumentoGEDVO = (TipoDocumentoGEDVO) iterator.next();
			if (tipoDocumentoGEDVO.getCodigo().equals(obj.getCodigo()) 
					&& tipoDocumentoGEDVO.getTipoDocumento().getCodigo().equals(obj.getTipoDocumento().getCodigo())) {
				iterator.remove();
				setMensagemID("msg_dados_removidos");
			}
		}
	}
	
	public String getCaminhoServidorDownloadUploadGED() {
		try {
			return documentacaoGED.getArquivo().obterUrlParaDownload(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED);
			//return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + PastaBaseArquivoEnum.DIGITALIZACAO_GED.getValue() + File.separator + UteisData.getDataMesAnoConcatenado() + File.separator + documentacaoGED.getArquivo().getNome();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemCategoriaGED() {
		if (listaSelectItemCategoriaGED == null || listaSelectItemCategoriaGED.isEmpty()) {
			listaSelectItemCategoriaGED = new ArrayList<>();
			listaSelectItemCategoriaGED.add(new SelectItem(0, ""));
			try {
				List<CategoriaGEDVO> listaCategoriaGED = getFacadeFactory().getCategoriaGEDInterfaceFacade().consultar(Boolean.FALSE, getUsuarioLogado());
				if (!listaCategoriaGED.isEmpty()) {
					for (CategoriaGEDVO categoriaGEDVO : listaCategoriaGED) {
						listaSelectItemCategoriaGED.add(new SelectItem(categoriaGEDVO.getCodigo(), categoriaGEDVO.getDescricao()));
					}
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemCategoriaGED;
	}

	public void setListaSelectItemCategoriaGED(List<SelectItem> listaSelectItemCategoriaGED) {
		this.listaSelectItemCategoriaGED = listaSelectItemCategoriaGED;
	}

	public DocumentacaoGEDVO getDocumentacaoGED() {
		if (documentacaoGED == null) {
			documentacaoGED = new DocumentacaoGEDVO();
		}
		return documentacaoGED;
	}

	public void setDocumentacaoGED(DocumentacaoGEDVO documentacaoGED) {
		this.documentacaoGED = documentacaoGED;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public TipoDocumentoVO getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoVO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public List<TipoDocumentoVO> getlistaConsultaTipoDocumento() {
		return listaConsultaTipoDocumento;
	}

	public void setlistaConsultaTipoDocumento(List<TipoDocumentoVO> listaConsultaTipoDocumento) {
		this.listaConsultaTipoDocumento = listaConsultaTipoDocumento;
	}

	public String getCampoConsultaTipoDocumento() {
		return campoConsultaTipoDocumento;
	}

	public void setCampoConsultaTipoDocumento(String campoConsultaTipoDocumento) {
		this.campoConsultaTipoDocumento = campoConsultaTipoDocumento;
	}

	public String getValorConsultaTipoDocumento() {
		return valorConsultaTipoDocumento;
	}

	public void setValorConsultaTipoDocumento(String valorConsultaTipoDocumento) {
		this.valorConsultaTipoDocumento = valorConsultaTipoDocumento;
	}

	public List<TipoDocumentoGEDVO> getListaTipoDocumentoGEDAnterior() {
		return listaTipoDocumentoGEDAnterior;
	}

	public void setListaTipoDocumentoGEDAnterior(List<TipoDocumentoGEDVO> listaTipoDocumentoGEDAnterior) {
		this.listaTipoDocumentoGEDAnterior = listaTipoDocumentoGEDAnterior;
	}

	public List<DocumetacaoMatriculaVO> getListaDocumetacaoMatricula() {
		return listaDocumetacaoMatricula;
	}

	public void setListaDocumetacaoMatricula(List<DocumetacaoMatriculaVO> listaDocumetacaoMatricula) {
		this.listaDocumetacaoMatricula = listaDocumetacaoMatricula;
	}	
	
	public boolean getApresentarPaginadorTipoDocumento() {
		return documentacaoGED.getListaTipoDocumentoGED().size() >= 5;
	}

	public void realizarAtualizacaoMapa() {
		try {
			DocumentacaoGED.processarArquivosDocumentacaoGED(Boolean.FALSE, getUsuarioLogado());
			getControleConsulta().setValorConsulta("");
			getControleConsulta().setCampoConsulta("nome");
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void rotacionar90GrausParaEsquerda() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumentacaoGED().getArquivo(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumentacaoGED().getArquivo(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumentacaoGED().getArquivo(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void executarZoomIn() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumentacaoGED().getArquivo(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOut() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumentacaoGED().getArquivo(), getConfiguracaoGeralPadraoSistema());
	}
	
	public void recortarImagem() {
		try {
			if (getLargura() == 0f && getAltura() == 0f && getX() == 0f && getY() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumentacaoGED().getArquivo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED, getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}
	
	public void realizarLocalizacaoDocumentacaoMatriculaVincularDocumentoGED() {
		try {
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().realizarLocalizacaoDocumentacaoMatriculaVincularDocumentoGED(getDocumentacaoGED());
			setMensagemDetalhada("msg_dados_consultados", getDocumentacaoGED().getMensagem());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getUrlDonloadSV() {
		return "location.href='../../DownloadSV'";
	}
	
	public void realizarVisualizacaoPdf() {
		try {
			DocumentacaoGEDVO obj = (DocumentacaoGEDVO) context().getExternalContext().getRequestMap().get("mapaDocumentoGED");
			setCaminhoPreview("");
			if (Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getArquivo())) {
				ArquivoVO arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimariaConsultaCompleta(obj.getArquivo().getCodigo(), 0, getUsuarioLogado());
				if (arquivoVO.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
					ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesDiretorioUpload();
					ServidorArquivoOnlineS3RS servidorArquivoOnlineS3RS = new ServidorArquivoOnlineS3RS(configuracaoGeralSistemaVO.getUsuarioAutenticacao(), configuracaoGeralSistemaVO.getSenhaAutenticacao(), configuracaoGeralSistemaVO.getNomeRepositorio());
					String nomeArquivoUsar = arquivoVO.getDescricao().contains(".") ? arquivoVO.getDescricao() : arquivoVO.getDescricao() + (arquivoVO.getNome().substring(arquivoVO.getNome().lastIndexOf("."), arquivoVO.getNome().length()));
					if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
						setCaminhoPreview(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
					} else {
						nomeArquivoUsar = arquivoVO.getDescricao();
						if (servidorArquivoOnlineS3RS.consultarSeObjetoExiste(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar))) {
							setCaminhoPreview(servidorArquivoOnlineS3RS.getUrlParaDownloadDoArquivo(arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar)));
						} else {
							setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Não foi encontrado no repositório da AMAZON o aquivo no caminho " + arquivoVO.recuperarNomeArquivoServidorExterno(arquivoVO.getPastaBaseArquivo(), configuracaoGeralSistemaVO.getLocalUploadArquivoTemp(), nomeArquivoUsar) + ".");
						}
					}
					realizarDownloadArquivo(arquivoVO);
				} else {
					setCaminhoPreview(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + arquivoVO.getPastaBaseArquivo() + "/" + arquivoVO.getNome() + "?embedded=true");
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setCaminhoPreview("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
}