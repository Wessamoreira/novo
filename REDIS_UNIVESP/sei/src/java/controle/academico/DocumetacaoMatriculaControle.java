package controle.academico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TipoDocumentoEquivalenteVO;
import negocio.comuns.academico.TipoDocumentoGEDVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.facade.jdbc.academico.DocumetacaoMatricula;
import negocio.facade.jdbc.academico.TipoDocumento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.DebitoDocumentosAlunoRelControle;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas tipoDocumentoForm.jsp tipoDocumentoCons.jsp) com as
 * funcionalidades da classe <code>TipoDocumento</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see TipoDocumento
 * @see TipoDocumentoVO
 */

@Controller("DocumetacaoMatriculaControle")
@Scope("viewScope")
public class DocumetacaoMatriculaControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private DocumetacaoMatriculaVO documetacaoMatriculaVO;
	protected MatriculaVO matriculaVO;
	protected List<MatriculaVO> listaConsultaAluno;
	protected String campoConsultaAluno;
	protected String valorConsultaAluno;
	protected Boolean emitirComprovante = false;
	private String turma;
	protected Boolean exibirMensagemRelatorio;
	private Boolean permiteAnexarImagemDocumentosEntregues;
	private String nomeArquivo;
	private Boolean selecionarTudo;
	private List<DocumentacaoGEDVO> listaDocumentacaoGED;
	protected List<SelectItem> listaSelectItemCategoriaGED;
	protected List<SelectItem> listaSelectItemTipoDocumento;
	private List<TipoDocumentoGEDVO> listaTipoDocumentoGEDAnterior;
	private TipoDocumentoGEDVO tipoDocumentoGED;
	private TipoDocumentoVO tipoDocumento;

	private Boolean arquivoGED = false;
	
	private DocumentacaoGEDVO documentacaoGED;

	private String tabAtiva;
	private List<TipoDocumentoVO> listaConsultaTipoDocumento;
	private String campoConsultaTipoDocumento;
	private String valorConsultaTipoDocumento;
	private String abrirModalInclusaoArquivoVerso;
	private DocumetacaoMatriculaVO documetacaoMatriculaVOAux;
	
	private List<SelectItem> tipoConsultaComboFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs;
	private List<FuncionarioVO> listaConsultaFuncionarioDocumentoGED;

	/**
	 * Interface <code>TipoDocumentoInterfaceFacade</code> responsável pela interconexão da camada de controle com a camada de negócio. Criando uma
	 * independência da camada de controle com relação a tenologia de persistência dos dados (DesignPatter: Façade).
	 */
	public DocumetacaoMatriculaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setEmitirComprovante(false);
		setMensagemID("msg_entre_prmconsulta");
		setListaSelectItemCategoriaGED(new ArrayList<>(0));
	}
	
	@PostConstruct
	public void realizarCarregamentoDocumentacaoMatriculaVindoTelaFichaAluno() {
		MatriculaVO matriculaVO = (MatriculaVO) context().getExternalContext().getSessionMap().get("matriculaFichaAluno");
		if (matriculaVO != null && !matriculaVO.getMatricula().equals("")) {
			try {
				setMatriculaVO(matriculaVO);
				getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
				@SuppressWarnings("deprecation")
				MatriculaPeriodoVO ultimaMatriculaPeriodo = (MatriculaPeriodoVO) getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				setTurma(ultimaMatriculaPeriodo.getTurma().getIdentificadorTurma());
				validarDocumentoEntregue();
				setExibirMensagemRelatorio(false);
				executarVerificacaoUusuarioPodeAnexarImagemDocumentosEntregues();
				listaDocumentacaoGED = getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("matriculaFichaAluno");
			}

		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TipoDocumento</code>. Caso o objeto seja novo (ainda
	 * não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			ControleAcesso.alterar("EntregaDocumento", true, getUsuarioLogado());
			getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumetacaoMatriculas(matriculaVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), Boolean.TRUE);
			setEmitirComprovante(true);
			setMensagemID("msg_dados_gravados");
			setExibirMensagemRelatorio(false);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setEmitirComprovante(Boolean.FALSE);
			setExibirMensagemRelatorio(false);
		}
		return "";
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TipoDocumentoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				objs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculas(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorNomeDoAluno(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			setExibirMensagemRelatorio(false);
			return Uteis.getCaminhoRedirecionamentoNavegacao("entregaDocumentoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			setExibirMensagemRelatorio(false);
			return Uteis.getCaminhoRedirecionamentoNavegacao("entregaDocumentoCons.xhtml");
		}
	}

	public void emitirRelatorio() {
		try {
			DebitoDocumentosAlunoRelControle debitoDocumentosAlunoRelControle = (DebitoDocumentosAlunoRelControle) getControlador("DebitoDocumentosAlunoRelControle");
			debitoDocumentosAlunoRelControle.getDebitoDocumentosAlunoRelVO().setMatriculaVO(getMatriculaVO());
			debitoDocumentosAlunoRelControle.setPreMatricula(true);			
			debitoDocumentosAlunoRelControle.emitirComprovante();
			setExibirMensagemRelatorio(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setExibirMensagemRelatorio(true);
		}
	}

	@PostConstruct
	public void executarVerificacaoUusuarioPodeAnexarImagemDocumentosEntregues() {
		Boolean liberar = false;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("EntregaDocumentoPermiteAnexarArquivo", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		setPermiteAnexarImagemDocumentosEntregues(liberar);
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(uploadEvent, null, getDocumetacaoMatriculaVO(), getMatriculaVO().getAluno(), !getArquivoGED(), getArquivoGED(), getDocumentacaoGED(), getUsuarioLogado(), getUsuarioLogado().getVisaoLogar());
			setOncompleteModal("RichFaces.$('panelUpload').hide(); RichFaces.$('panelImagemDocumentosEntregues').show();");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setOncompleteModal("RichFaces.$('panelUpload').hide(); RichFaces.$('panelImagemDocumentosEntregues').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	public String getCaminhoPDF() {
		return getDocumetacaoMatriculaVO().getArquivoVO().getPastaBaseArquivoWeb()+"?embedded=true";
	}
	
	public String getCaminhoPDFVerso() {
		return getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivoWeb()+"?embedded=true";
	}
	
	public void upLoadArquivoVerso(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarUploadArquivo(uploadEvent, null, getDocumetacaoMatriculaVO(), getMatriculaVO().getAluno(), false, false, getDocumentacaoGED(), getUsuarioLogado(), getUsuarioLogado().getVisaoLogar());
			setOncompleteModal("RichFaces.$('panelUploadVerso').hide(); RichFaces.$('panelImagemDocumentosEntreguesVerso').show();");
		} catch (Exception e) {
			setOncompleteModal("RichFaces.$('panelUploadVerso').hide(); RichFaces.$('panelImagemDocumentosEntreguesVerso').hide();");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void criarNomeArquivo() {
		setTam(100);
		setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
	}

	public void upLoadArquivoScanner() {
		try {

			getDocumetacaoMatriculaVO().getArquivoVO().setCpfAlunoDocumentacao(getMatriculaVO().getAluno().getCPF());
			getDocumetacaoMatriculaVO().setUsuario(getUsuarioLogadoClone());
			if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue()) {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.TRUE);
			} else {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.FALSE);
			}
			// if
			// (context().getExternalContext().getSessionMap().get("nomeArquivo")
			// != null) {
			getDocumetacaoMatriculaVO().getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoScanner(getNomeArquivo(), getDocumetacaoMatriculaVO().getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());

			// } else {
			// throw new
			// Exception("Não foi possível obter o arquivo do scanner.");
			// }
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void upLoadArquivoScannerVerso() {
		try {

			getDocumetacaoMatriculaVO().getArquivoVOVerso().setCpfAlunoDocumentacao(getMatriculaVO().getAluno().getCPF());
			getDocumetacaoMatriculaVO().setUsuario(getUsuarioLogadoClone());
			if (!getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getControlaAprovacaoDocEntregue()) {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.TRUE);
			} else {
				getDocumetacaoMatriculaVO().setArquivoAprovadoPeloDep(Boolean.FALSE);
			}

			getDocumetacaoMatriculaVO().getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			getFacadeFactory().getArquivoHelper().upLoadDocumentacaoScanner(getNomeArquivo(), getDocumetacaoMatriculaVO().getArquivoVOVerso(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public void adicionarArquivoDocumentacaoMatricula() {
		try {
			setAbrirModalInclusaoArquivoVerso("");
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Adicionar Arquivo Documentação Matrícula", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getUsuario().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getArquivoVO().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoVO().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoVO().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVO().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoVO().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());

			getDocumetacaoMatriculaVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			diminuirResolucaoImagem(getDocumetacaoMatriculaVO().getArquivoVO());
			if (!getDocumetacaoMatriculaVO().getEntregue() && getDocumetacaoMatriculaVO().getTipoDeDocumentoVO().getDocumentoFrenteVerso()) {
				setAbrirModalInclusaoArquivoVerso("RichFaces.$('panelIncluirArquivoVerso').show()");
			} else {
				setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Adicionar Arquivo Documentação Matrícula", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarArquivoDocumentacaoGED() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaDocumentosGED", "Iniciando Adicionar Arquivo Documentação GED", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoGED().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getArquivoGED().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getUsuario().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getArquivoGED().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoGED().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoGED().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVO().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoGED().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoGED().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoGED().setOrigem(OrigemArquivo.DOCUMENTO_GED.getValor());

			getDocumetacaoMatriculaVO().getArquivoGED().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DIGITALIZACAO_GED);
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			registrarAtividadeUsuario(getUsuarioLogado(), "EntregaDocumentosGED", "Finalizando Adicionar Arquivo Documentação GED", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	

	public void adicionarArquivoDocumentacaoMatriculaVerso() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Adicionar Arquivo Documentação Matrícula", "Uploading");
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getArquivoVO().getResponsavelUpload().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
			getDocumetacaoMatriculaVO().getUsuario().setNome(getUsuarioLogado().getNome());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataUpload(new Date());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setManterDisponibilizacao(true);
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataDisponibilizacao(getDocumetacaoMatriculaVO().getArquivoVO().getDataUpload());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setDataIndisponibilizacao(null);
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setOrigem(OrigemArquivo.DOCUMENTACAO_MATRICULA.getValor());
			// getDocumetacaoMatriculaVO().getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
			getDocumetacaoMatriculaVO().setExcluirArquivo(false);
			diminuirResolucaoImagem(getDocumetacaoMatriculaVO().getArquivoVOVerso());
			getDocumetacaoMatriculaVO().getArquivoVOVerso().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum("APACHE"));
			int index = 0;
			Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
				if (documetacaoMatriculaVO.getCodigo().equals(getDocumetacaoMatriculaVO().getCodigo())) {
					getMatriculaVO().getDocumetacaoMatriculaVOs().set(index,getDocumetacaoMatriculaVO());
				}
				index++;
			}
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Adicionar Arquivo Documentação Matrícula", "Uploading");
			setMensagemID("msg_sucesso_upload");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void diminuirResolucaoImagem(ArquivoVO obj) throws IOException {
		if(obj.getIsImagem()) {
			File fileTemp = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + obj.getPastaBaseArquivo() + File.separator + obj.getNome());
			if(Uteis.isAtributoPreenchido(fileTemp.getName()) && fileTemp.exists()) {
				getFacadeFactory().getArquivoHelper().diminuirResolucaoImagem(fileTemp, 0.5f);
			}
		}
	}

	public void selecionarEntregue() {
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
		realizarValidacaoDocumentoEquivalenteEntregue(obj);
	}

	public void realizarValidacaoDocumentoEquivalenteEntregue(DocumetacaoMatriculaVO obj) {
		obj.setUsuario(getUsuarioLogadoClone());
		if (!obj.getTipoDeDocumentoVO().getTipoDocumentoEquivalenteVOs().isEmpty()) {
			Iterator<TipoDocumentoEquivalenteVO> j = obj.getTipoDeDocumentoVO().getTipoDocumentoEquivalenteVOs().iterator();
			while (j.hasNext()) {
				TipoDocumentoEquivalenteVO tipo = (TipoDocumentoEquivalenteVO) j.next();
				Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
				while (i.hasNext()) {
					DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO) i.next();
					if (tipo.getTipoDocumentoEquivalente().getCodigo().intValue() == doc.getTipoDeDocumentoVO().getCodigo().intValue()) {
						if (obj.getEntregue()) {
							if (!doc.getEntregue()) {
								doc.setEntregue(Boolean.TRUE);
								doc.setDataEntrega(new Date());
								doc.setEntreguePorEquivalencia(Boolean.TRUE);
							} else {
								if (doc.getEntreguePorEquivalencia()) {
									doc.setEntregue(Boolean.FALSE);
									doc.setDataEntrega(null);
									doc.setEntreguePorEquivalencia(Boolean.FALSE);
								}
							}
						} else {
							if (doc.getEntreguePorEquivalencia()) {
								doc.setEntregue(Boolean.FALSE);
								doc.setDataEntrega(null);
								doc.setEntreguePorEquivalencia(Boolean.FALSE);
							}
						}
					}
				}
			}
		}
	}

	public void selecionarObjetoDocumentacaoMatriculaVerso() throws CloneNotSupportedException {
		setOncompleteModal("");
		try {			
			setTam(100);
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");			
			setDocumetacaoMatriculaVO((DocumetacaoMatriculaVO) obj.clone());
			getFacadeFactory().getDocumetacaoMatriculaFacade().validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(getMatriculaVO() , getDocumetacaoMatriculaVO(),getUsuarioLogado());

			setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
			if (obj.getArquivoVOVerso().getNome().equals("")) {
				getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao(obj.getTipoDeDocumentoVO().getNome()+"_VERSO");
			}
			setOncompleteModal("RichFaces.$('panelUploadVerso').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarObjetoDocumentacaoMatricula() {
		setOncompleteModal("");
		try {
			setTam(100);
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			if (obj == null) {
				obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documentacao");
			}
			if (obj != null) {
				setDocumetacaoMatriculaVO(obj);
				getFacadeFactory().getDocumetacaoMatriculaFacade().validarPermissaoPermiteUploadDocumentoIndeferidoForaPrazoParaMatriculaProcessoSeletivo(getMatriculaVO() ,getDocumetacaoMatriculaVO() ,getUsuarioLogado());

				if (obj != null && obj.getCodigo().equals(0) && getDocumetacaoMatriculaVOAux() == null) {
					try {
						setDocumetacaoMatriculaVOAux((DocumetacaoMatriculaVO) obj.clone());
					} catch (Exception e) {
					}
				}
				setNomeArquivo(getUsuarioLogado().getCodigo() + "_" + new Date().getTime() + ".jpg");
				if (obj.getArquivoVO().getNome().equals("")) {
					getDocumetacaoMatriculaVO().setArquivoVO(new ArquivoVO());
					getDocumetacaoMatriculaVO().getArquivoVO().setDescricao(obj.getTipoDeDocumentoVO().getNome());
				}
				setArquivoGED(Boolean.FALSE);
			}
			setOncompleteModal("RichFaces.$('panelUpload').show()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public void selecionarObjetoDocumentacaoGED(DocumentacaoGEDVO documentacaoGED) {
		try {
			setDocumentacaoGED(documentacaoGED);
			List<DocumetacaoMatriculaVO> listaDocumetacaoMatricula = getFacadeFactory().getDocumetacaoMatriculaFacade().consultaRapidaPorMatricula(documentacaoGED.getMatricula().getMatricula(), false, getUsuario());
			if (!listaDocumetacaoMatricula.isEmpty()) {				
				setDocumetacaoMatriculaVO(listaDocumetacaoMatricula.get(0));
			}
			tipoDocumentoGED.setDocumentacaoGED(documentacaoGED);
			montarListaSelectItemCategoriaGED();
			setArquivoGED(Boolean.TRUE);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void selecionarArquivoDocumentoEntregueExclusao() {
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		if (!getArquivoGED()) {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			setDocumetacaoMatriculaVO(obj);
		}
	}
	
	public void selecionarArquivoDocumentoEntregueExclusaoGED(DocumentacaoGEDVO documentacaoGED) {
		setDocumentacaoGED(documentacaoGED);
	}
	
	public void realizarDownloadArquivoAssinado()  {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			DocumentoAssinadoVO doc = getFacadeFactory().getDocumentoAssinadoFacade().consultarDocumentoAssinadoPorArquivo(obj.getArquivoVOAssinado().getCodigo(), getUsuarioLogadoClone());
			if(Uteis.isAtributoPreenchido(doc) && doc.getProvedorDeAssinaturaEnum().isProvedorCertisign()) {
				doc.setArquivo(obj.getArquivoVOAssinado());
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorCertisign(doc, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(doc.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());	
			}
			if(Uteis.isAtributoPreenchido(doc) && doc.getProvedorDeAssinaturaEnum().isProvedorTechCert()) {
				doc.setArquivo(obj.getArquivoVOAssinado());
				getFacadeFactory().getDocumentoAssinadoFacade().realizarDownloadArquivoProvedorTechCert(doc, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(doc.getUnidadeEnsinoVO().getCodigo()), getUsuarioLogadoClone());
			}
			realizarDownloadArquivo(obj.getArquivoVOAssinado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getCaminhoServidorDownload() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVO(), obj.getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadGED() {
		try {
			
			DocumentacaoGEDVO obj = (DocumentacaoGEDVO) context().getExternalContext().getRequestMap().get("documentacao");
			ArquivoVO arquivo = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivo().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			return arquivo.obterUrlParaDownload(getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadVerso() {
		try {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatriculaItens");
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivoVOVerso(), obj.getArquivoVOVerso().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadDocumentacao() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getDocumetacaoMatriculaVO().getArquivoVO(), getDocumetacaoMatriculaVO().getArquivoVO().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema()) + "?UID=" + new Date().getTime();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadDocumentacaoVerso() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivoEnum(), getConfiguracaoGeralPadraoSistema()) + "?UID=" + new Date().getTime();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void rotacionar90GrausParaEsquerda() {
		try {
			if (arquivoGED) {				
				getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoGED(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			}
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireita() {
		try {
			if (arquivoGED) {				
				getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoGED(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
			}
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180Graus() {
		try {
			if (arquivoGED) {
				getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoGED(), getConfiguracaoGeralPadraoSistema());
			} else {
				getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());				
			}
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaEsquerdaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaEsquerda(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar90GrausParaDireitaVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar90GrausParaDireita(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void rotacionar180GrausVerso() {
		try {
			getFacadeFactory().getArquivoHelper().rotacionar180Graus(getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
			limparMensagem();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void executarZoomIn() {
		if (arquivoGED) {
			getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoGED(), getConfiguracaoGeralPadraoSistema());			
		} else {
			getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		}
	}

	public void executarZoomOut() {
		if (arquivoGED) {
			getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoGED(), getConfiguracaoGeralPadraoSistema());
		} else {
			getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema());
		}
	}

	public void executarZoomInVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("in", getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void executarZoomOutVerso() {
		getFacadeFactory().getArquivoHelper().executarZoomImagemRequerimento("out", getDocumetacaoMatriculaVO().getArquivoVOVerso(), getConfiguracaoGeralPadraoSistema());
	}

	public void recortarImagem() {
		try {
			if (getLargura() == 0f && getAltura() == 0f && getX() == 0f && getY() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}

			if (!arquivoGED) {
				getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoVO(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY(), getUsuarioLogado());
			} else {
				getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoGED(), PastaBaseArquivoEnum.DIGITALIZACAO_GED, getConfiguracaoGeralPadraoSistema(), getLargura(), getAltura(), getX(), getY(), getUsuarioLogado());				
			}
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void recortarImagemVerso() {
		try {
			if (getLarguraVerso() == 0f && getAlturaVerso() == 0f && getXcropVerso() == 0f && getYcropVerso() == 0f) {
				throw new Exception("Clique e arraste sobre a imagem para selecionar a área que deve ser recortada.");
			}
			getFacadeFactory().getArquivoHelper().recortarImagem(getDocumetacaoMatriculaVO().getArquivoVOVerso(), PastaBaseArquivoEnum.DOCUMENTOS_TMP, getConfiguracaoGeralPadraoSistema(), getLarguraVerso(), getAlturaVerso(), getXcropVerso(), getYcropVerso(), getUsuarioLogado());
			limparMensagem();
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
		}
	}

	public void removerArquivoDocumentacao() throws Exception {
		try {
			if(!arquivoGED) {
				registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Iniciando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
				if ((getDocumetacaoMatriculaVO().getArquivoVO() != null && Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVO())) || (getDocumetacaoMatriculaVO().getArquivoVOVerso() != null && Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso())) || (getDocumetacaoMatriculaVO().getArquivoVOAssinado() != null && Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOAssinado()))) {
					getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumentacaoMatricula(getDocumetacaoMatriculaVO(), getConfiguracaoGeralPadraoSistema(), true, getUsuarioLogado());
				}
				getDocumetacaoMatriculaVO().setEntregue(false);
				getDocumetacaoMatriculaVO().setDataEntrega(null);
				getDocumetacaoMatriculaVO().setUsuario(null);
				getDocumetacaoMatriculaVO().setArquivoVO(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVO().setDescricao("");
				getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVOVerso().setDescricao("");
				getDocumetacaoMatriculaVO().setArquivoVOAssinado(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoVOAssinado().setDescricao("");
				registrarAtividadeUsuario(getUsuarioLogado(), "RenovarMatriculaControle", "Finalizando Remover Arquivo Documentação Matrícula ", "Downloading - Removendo");
				setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			} else {				
				getDocumetacaoMatriculaVO().setArquivoGED(new ArquivoVO());
				getDocumetacaoMatriculaVO().getArquivoGED().setDescricao("");				
				setTabAtiva("form:segundatab");
			}
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void cancelarExclusaoArquivoDocumentoEntregue() {
		if (getDocumetacaoMatriculaVO() != null && !getDocumetacaoMatriculaVO().getArquivoVO().getNome().equals("") && !getDocumetacaoMatriculaVO().getEntregue()) {
			getDocumetacaoMatriculaVO().setEntregue(true);
		}
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
	}

	public Boolean getPermiteAnexarImagemDocumentosEntregues() {
		if (permiteAnexarImagemDocumentosEntregues == null) {
			permiteAnexarImagemDocumentosEntregues = false;
		}
		return permiteAnexarImagemDocumentosEntregues;
	}

	public void setPermiteAnexarImagemDocumentosEntregues(Boolean permiteAnexarImagemDocumentosEntregues) {
		this.permiteAnexarImagemDocumentosEntregues = permiteAnexarImagemDocumentosEntregues;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setEmitirComprovante(Boolean.FALSE);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("entregaDocumentoCons.xhtml");
	}

	public boolean isConsultaComResultado() {
		return getListaConsultaAluno() != null && !getListaConsultaAluno().isEmpty();
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getCampoConsultaAluno().equals("nome")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("CPF")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorCPF(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				if (getValorConsultaAluno().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public String selecionarAluno() {
		setMatriculaVO((MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens"));
		try {
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
			MatriculaPeriodoVO ultimaMatriculaPeriodo = (MatriculaPeriodoVO) getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setTurma(ultimaMatriculaPeriodo.getTurma().getIdentificadorTurma());
			validarDocumentoEntregue();
			setExibirMensagemRelatorio(false);
			executarVerificacaoUusuarioPodeAnexarImagemDocumentosEntregues();
			limparMensagem();
			listaDocumentacaoGED = getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			
			/*for (DocumentacaoGEDVO documentacaoGED : listaDocumentacaoGED) {
				documentacaoGED.setListaTipoDocumentoGED(getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().consultarPorDocumentacaoGED(false, getUsuarioLogado(), documentacaoGED.getCodigo()));
			}*/
			return Uteis.getCaminhoRedirecionamentoNavegacao("entregaDocumentoForm.xhtml");	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("entregaDocumentoCons.xhtml");	
		}
	}

	public void validarDocumentoEntregue() {
		try {
			Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
			while (i.hasNext()) {
				DocumetacaoMatriculaVO objExistente = (DocumetacaoMatriculaVO) i.next();
				if (objExistente.getEntregue()) {
					setEmitirComprovante(true);
					break;
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void atualizarListaDocumentosMatriculaPeloCurso() throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(getMatriculaVO(), getUsuarioLogado());
			listaDocumentacaoGED = getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemCategoriaGED() {
		try {
			List resultadoConsulta;
			resultadoConsulta = getFacadeFactory().getCategoriaGEDInterfaceFacade().consultar(false, getUsuarioLogado());
			Iterator i = resultadoConsulta.iterator();

			List<SelectItem> objs = new ArrayList<>(0);

			while (i.hasNext()) {
				CategoriaGEDVO obj = (CategoriaGEDVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
			setListaSelectItemCategoriaGED(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemTipoDocumento() {
		try {
			List resultadoConsulta = getFacadeFactory().getTipoDeDocumentoFacade().consultarPorCategoriaGED(tipoDocumentoGED.getDocumentacaoGED().getCategoriaGED().getCodigo());
			Iterator i = resultadoConsulta.iterator();

			List<SelectItem> objs = new ArrayList<>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TipoDocumentoVO obj = (TipoDocumentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemTipoDocumento(objs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarDadosUploadGED() {
		try {
			if (!Uteis.isAtributoPreenchido(tipoDocumento.getCodigo())) {
				throw new Exception("O campo TIPO DE DOCUMENTO é obrigatório para fazer upload.");
			}
			tipoDocumentoGED.setTipoDocumento(tipoDocumento);
			getFacadeFactory().getTipoDocumentoGEDInterfaceFacade().validarDados(tipoDocumentoGED);
			setTabAtiva("form:segundatab");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
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

	/**
	 * @return the listaConsultaAluno
	 */
	public List<MatriculaVO> getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {

		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the matriculaVO
	 */
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			setMatriculaVO(new MatriculaVO());
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
	 * @return the emitirComprovante
	 */
	public boolean isEmitirComprovante() {
		return emitirComprovante;
	}

	/**
	 * @param emitirComprovante
	 *            the emitirComprovante to set
	 */
	public void setEmitirComprovante(boolean emitirComprovante) {
		this.emitirComprovante = emitirComprovante;
	}

	public String getMascaraConsulta() {
		if (getCampoConsultaAluno().equals("CPF")) {
			return "return mascara(this.form,'form:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	public String getTamanhoMaximoCPF() {
		if (getCampoConsultaAluno().equals("CPF")) {
			return "14";
		}
		return "150";
	}

	/**
	 * @return the turma
	 */
	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Boolean getExibirMensagemRelatorio() {
		if (exibirMensagemRelatorio == null) {
			exibirMensagemRelatorio = false;
		}
		return exibirMensagemRelatorio;
	}

	public void setExibirMensagemRelatorio(Boolean exibirMensagemRelatorio) {
		this.exibirMensagemRelatorio = exibirMensagemRelatorio;
	}

	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public Boolean getSelecionarTudo() {
		if (selecionarTudo == null) {
			selecionarTudo = false;
		}
		return selecionarTudo;
	}

	public void setSelecionarTudo(Boolean selecionarTudo) {
		this.selecionarTudo = selecionarTudo;
	}

	public void selecionarTodos() {
		Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
		while (i.hasNext()) {
			DocumetacaoMatriculaVO m = (DocumetacaoMatriculaVO) i.next();
			m.setEntregue(getSelecionarTudo());
			realizarValidacaoDocumentoEquivalenteEntregue(m);
		}
	}
	
	public void validarUploadGED() {
		setArquivoGED(Boolean.TRUE);
		
	}

	public List<DocumentacaoGEDVO> getListaDocumentacaoGED() {
		return listaDocumentacaoGED;
	}

	public void setListaDocumentacaoGED(List<DocumentacaoGEDVO> listaDocumentacaoGED) {
		this.listaDocumentacaoGED = listaDocumentacaoGED;
	}

	public Boolean getArquivoGED() {
		if (arquivoGED == null) {
			arquivoGED = Boolean.FALSE;
		}
		return arquivoGED;
	}

	public void setArquivoGED(Boolean arquivoGED) {
		this.arquivoGED = arquivoGED;
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemCategoriaGED() {
		return listaSelectItemCategoriaGED;
	}

	public void setListaSelectItemCategoriaGED(List<SelectItem> listaSelectItemCategoriaGED) {
		this.listaSelectItemCategoriaGED = listaSelectItemCategoriaGED;
	}

	public TipoDocumentoGEDVO getTipoDocumentoGED() {
		if (tipoDocumentoGED == null) {
			tipoDocumentoGED = new TipoDocumentoGEDVO();
		}
		return tipoDocumentoGED;
	}

	public void setTipoDocumentoGED(TipoDocumentoGEDVO tipoDocumentoGED) {
		this.tipoDocumentoGED = tipoDocumentoGED;
	}

	public List<SelectItem> getListaSelectItemTipoDocumento() {
		return listaSelectItemTipoDocumento;
	}

	public void setListaSelectItemTipoDocumento(List<SelectItem> listaSelectItemTipoDocumento) {
		this.listaSelectItemTipoDocumento = listaSelectItemTipoDocumento;
	}

	public TipoDocumentoVO getTipoDocumento() {
		if (tipoDocumento == null) {
			tipoDocumento = new TipoDocumentoVO();
		}
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoVO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTabAtiva() {
		if (tabAtiva == null) {
			tabAtiva = "";
		}
		return tabAtiva;
	}

	public void setTabAtiva(String tabAtiva) {
		this.tabAtiva = tabAtiva;
	}
	
	public DocumentacaoGEDVO getDocumentacaoGED() {
		return documentacaoGED;
	}

	public void setDocumentacaoGED(DocumentacaoGEDVO documentacaoGED) {
		this.documentacaoGED= documentacaoGED;
	}
		
		
	public void novoDocumentacaoGed() {
		setDocumentacaoGED(new DocumentacaoGEDVO());
		getDocumentacaoGED().setMatricula(getMatriculaVO());
		getDocumentacaoGED().setPessoa(getMatriculaVO().getAluno());
		getDocumentacaoGED().getUsuario().setCodigo(getUsuarioLogado().getCodigo());
		getListaTipoDocumentoGEDAnterior().clear();		
		montarListaSelectItemCategoriaGED();
		limparMensagem();
	}
	
	public void editarDocumentacaoGed() {
		try {
		setDocumentacaoGED(new DocumentacaoGEDVO());
		DocumentacaoGEDVO obj = (DocumentacaoGEDVO) context().getExternalContext().getRequestMap().get("documentacao");
		
			obj.getArquivo().setListaFuncionarioAssinarDigitalmenteVOs(getFacadeFactory().getFuncionarioFacade().consultarPorArquivoConsiderandoDocumentoAssinado(obj.getArquivo()));
		
		montarListaSelectItemCategoriaGED();
		obj.setNovoObj(Boolean.FALSE);
		setListaTipoDocumentoGEDAnterior(new ArrayList<TipoDocumentoGEDVO>(0));
		for (TipoDocumentoGEDVO object : obj.getListaTipoDocumentoGED()) {
			getListaTipoDocumentoGEDAnterior().add(object);
		}

		if (obj.getSituacao().equals("Erro")) {
			setMensagemDetalhada(obj.getMensagem());
		} else {
			setMensagemDetalhada("");
		}		
		try {
			setDocumentacaoGED((DocumentacaoGEDVO)obj.getClone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getDocumentacaoGED().setUsuario(getUsuarioLogadoClone());
		limparMensagem();
		} catch (Exception e1) {
			setMensagemDetalhada("msg_erro",  e1.getMessage());
		}
	}
	

	public void cancelarAlteracaoDocumentacaoGed() {
		try {
			if (getDocumentacaoGED().getArquivo().getPastaBaseArquivoEnum() != null && getDocumentacaoGED().getArquivo().getPastaBaseArquivoEnum().equals(PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP)) {
				File arquivoTMP = new File(getDocumentacaoGED().getArquivo().obterLocalFisico(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo(), PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP));
				if (arquivoTMP.exists()) {
					arquivoTMP.delete();
				}
			}			
			setDocumentacaoGED(null);
			setListaTipoDocumentoGEDAnterior(new ArrayList<TipoDocumentoGEDVO>(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluirDocumentoGed() {
		try {
			DocumetacaoMatricula.excluir("EntregaDocumento", getUsuarioLogado());
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().excluir(getDocumentacaoGED(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			int index = 0;
			for(DocumentacaoGEDVO documentacaoGEDVO2 : getListaDocumentacaoGED()) {
				if(documentacaoGEDVO2.getCodigo().equals(getDocumentacaoGED().getCodigo())) {
					getListaDocumentacaoGED().remove(index);
					break;
				}
				index++;
			}
			for(DocumetacaoMatriculaVO documetacaoMatriculaVO: getMatriculaVO().getDocumetacaoMatriculaVOs()) {
				if(documetacaoMatriculaVO.getArquivoGED().getCodigo().equals(getDocumentacaoGED().getArquivo().getCodigo())) {
					documetacaoMatriculaVO.setArquivoGED(null);
					documetacaoMatriculaVO.setEntregue(false);
					documetacaoMatriculaVO.setDataEntrega(null);
					documetacaoMatriculaVO.setUsuario(null);
				}
				if(documetacaoMatriculaVO.getArquivoVOAssinado().getCodigo().equals(getDocumentacaoGED().getArquivo().getCodigo())) {
					documetacaoMatriculaVO.setArquivoVOAssinado(null);
					documetacaoMatriculaVO.setEntregue(false);
					documetacaoMatriculaVO.setDataEntrega(null);
					documetacaoMatriculaVO.setUsuario(null);
				}
			}
			setMensagemID("msg_dados_excluidos");
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			
		}
	}

	public List<TipoDocumentoGEDVO> getListaTipoDocumentoGEDAnterior() {
		if (listaTipoDocumentoGEDAnterior == null) {
			listaTipoDocumentoGEDAnterior = new ArrayList<TipoDocumentoGEDVO>(0);
		}
		return listaTipoDocumentoGEDAnterior;
	}

	public void setListaTipoDocumentoGEDAnterior(List<TipoDocumentoGEDVO> listaTipoDocumentoGEDAnterior) {
		this.listaTipoDocumentoGEDAnterior = listaTipoDocumentoGEDAnterior;
	}
	
	@SuppressWarnings("rawtypes")
	public void removerTipoDocumento(TipoDocumentoGEDVO obj) {
		setMensagemDetalhada("");
		Iterator iterator = getDocumentacaoGED().getListaTipoDocumentoGED().iterator();
		while(iterator.hasNext()) {
			TipoDocumentoGEDVO tipoDocumentoGEDVO = (TipoDocumentoGEDVO) iterator.next();
			if (tipoDocumentoGEDVO.getCodigo().equals(obj.getCodigo()) 
					&& tipoDocumentoGEDVO.getTipoDocumento().getCodigo().equals(obj.getTipoDocumento().getCodigo())) {
				iterator.remove();			
				setMensagemID("msg_dados_removidos");
				break;
			}
		}
	}
	
	public void persistirDocumentoGed() {
		try {
			setOncompleteModal("");
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().persistirComUploadArquivo(getDocumentacaoGED(), getListaTipoDocumentoGEDAnterior(), getConfiguracaoGeralPadraoSistema(), Boolean.FALSE, getUsuarioLogado());
			for (TipoDocumentoGEDVO tipoDocumentoGEDVO : getDocumentacaoGED().getListaTipoDocumentoGED()) {
				for (DocumetacaoMatriculaVO documetacaoMatriculaVO : getMatriculaVO().getDocumetacaoMatriculaVOs()) {
					if (Uteis.isAtributoPreenchido(tipoDocumentoGEDVO.getDocumetacaoMatricula().getCodigo())  && documetacaoMatriculaVO.getCodigo().equals(tipoDocumentoGEDVO.getDocumetacaoMatricula().getCodigo())) {
						DocumetacaoMatriculaVO documetacaoMatriculaVO2 = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(tipoDocumentoGEDVO.getDocumetacaoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
						documetacaoMatriculaVO.setEntregue(documetacaoMatriculaVO2.getEntregue());
						documetacaoMatriculaVO.setArquivoGED(documetacaoMatriculaVO2.getArquivoGED());
						documetacaoMatriculaVO.setDataEntrega(documetacaoMatriculaVO2.getDataEntrega());
						documetacaoMatriculaVO.setUsuario(documetacaoMatriculaVO2.getUsuario());
					}
				}
			}
			setOncompleteModal("RichFaces.$('panelDocumentosEntreguesGED').hide()");
			setMensagemID("msg_dados_gravados");
			listaDocumentacaoGED = getFacadeFactory().getDocumentacaoGEDInterfaceFacade().consultarPorMatricula(getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public void adicionarTipoDocumento() {
		try {
			setMensagemDetalhada("");			
			TipoDocumentoVO obj = (TipoDocumentoVO) context().getExternalContext().getRequestMap().get("tipoDocumentoItens");
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().adicionarTipoDocumento(getDocumentacaoGED(), obj, getUsuarioLogado());
			int x = 0;
			for(TipoDocumentoVO tipoDocumentoVO: getListaConsultaTipoDocumento()) {
				if(tipoDocumentoVO.getCodigo().equals(obj.getCodigo())) {
					getListaConsultaTipoDocumento().remove(x);
					break;
				}
				x++;
			}

			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagem("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboTipoDocumento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("identificadorGED", "Identificador GED"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
		

	public List<TipoDocumentoVO> getListaConsultaTipoDocumento() {
		if (listaConsultaTipoDocumento == null) {
			listaConsultaTipoDocumento = new ArrayList<TipoDocumentoVO>(0);
		}
		return listaConsultaTipoDocumento;
	}

	public void setListaConsultaTipoDocumento(List<TipoDocumentoVO> listaConsultaTipoDocumento) {
		this.listaConsultaTipoDocumento = listaConsultaTipoDocumento;
	}

	public String getCampoConsultaTipoDocumento() {
		if (campoConsultaTipoDocumento == null) {
			campoConsultaTipoDocumento = "";
		}
		return campoConsultaTipoDocumento;
	}

	public void setCampoConsultaTipoDocumento(String campoConsultaTipoDocumento) {
		this.campoConsultaTipoDocumento = campoConsultaTipoDocumento;
	}

	public String getValorConsultaTipoDocumento() {
		if (valorConsultaTipoDocumento == null) {
			valorConsultaTipoDocumento = "";
		}
		return valorConsultaTipoDocumento;
	}

	public void setValorConsultaTipoDocumento(String valorConsultaTipoDocumento) {
		this.valorConsultaTipoDocumento = valorConsultaTipoDocumento;
	}
	
	public void upLoadArquivoGed(FileUploadEvent uploadEvent) {
		try {		
			getDocumentacaoGED().getArquivo().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getDocumentacaoGED().getArquivo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.DIGITALIZACAO_GED_TMP, getUsuarioLogado());
			getDocumentacaoGED().setUploadNovoArquivo(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
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

	public Boolean getControlaAprovacaoDocEntregue() {
		try {
			return getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaVO().getUnidadeEnsino().getCodigo()).getControlaAprovacaoDocEntregue();
		} catch (Exception e) {
			return false;
		}
	}

	public String getAbrirModalInclusaoArquivoVerso() {
		if (abrirModalInclusaoArquivoVerso == null) {
			abrirModalInclusaoArquivoVerso = "";
		}
		return abrirModalInclusaoArquivoVerso;
	}

	public void setAbrirModalInclusaoArquivoVerso(String abrirModalInclusaoArquivoVerso) {
		this.abrirModalInclusaoArquivoVerso = abrirModalInclusaoArquivoVerso;
	}
	
	public DocumetacaoMatriculaVO getDocumetacaoMatriculaVOAux() {
		return documetacaoMatriculaVOAux;
	}

	public void setDocumetacaoMatriculaVOAux(DocumetacaoMatriculaVO documetacaoMatriculaVOAux) {
		this.documetacaoMatriculaVOAux = documetacaoMatriculaVOAux;
	}

	public void limparDocumentacaoMatriculaVO() {
		try {
			int index = 0;
			if (getDocumetacaoMatriculaVO().getCodigo().equals(0) && getDocumetacaoMatriculaVOAux() != null) {
				Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
				while (i.hasNext()) {
					DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
					if (documetacaoMatriculaVO == getDocumetacaoMatriculaVO()) {
						getDocumetacaoMatriculaVOAux().setArquivoVO(new ArquivoVO());
						getDocumetacaoMatriculaVOAux().setArquivoVOVerso(new ArquivoVO());
						getMatriculaVO().getDocumetacaoMatriculaVOs().set(index, getDocumetacaoMatriculaVOAux());
						break;
					}
					index++;
				}
				setDocumetacaoMatriculaVOAux(null);
			} else {
				Iterator<DocumetacaoMatriculaVO> i = getMatriculaVO().getDocumetacaoMatriculaVOs().iterator();
				while (i.hasNext()) {
					DocumetacaoMatriculaVO documetacaoMatriculaVO = i.next();
					if (documetacaoMatriculaVO.getCodigo().equals(getDocumetacaoMatriculaVO().getCodigo())) {
						getMatriculaVO().getDocumetacaoMatriculaVOs().set(index,
								getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorChavePrimaria(getDocumetacaoMatriculaVO().getCodigo(), 
										Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
						break;
					}
					index++;
				}
			}
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparDocumentacaoMatriculaVerso() {
		try {
			if (!Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso().getCodigo()) && Uteis.isAtributoPreenchido(getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome())) {
				String arquivoExterno = "";
				arquivoExterno = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getDocumetacaoMatriculaVO().getArquivoVOVerso().getPastaBaseArquivo() + File.separator + (getDocumetacaoMatriculaVO().getArquivoVOVerso().getNome());
				File arquivo = new File(arquivoExterno);
				if (arquivo.exists()) {
					arquivo.delete();
				}
			}
			getDocumetacaoMatriculaVO().setArquivoVOVerso(new ArquivoVO());
			setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaFuncionario().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionarioDocumentoGED(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void adicionarFuncionario() throws Exception {
		getFacadeFactory().getUploadArquivosComunsFacade().adicionarProfessorListaAssinaturaDigital(getListaConsultaFuncionarioDocumentoGED(), getDocumentacaoGED().getArquivo().getListaFuncionarioAssinarDigitalmenteVOs(), getUsuarioLogado());
	}
	
	public void removerFuncionario() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
			getFacadeFactory().getDocumetacaoMatriculaFacade().removerProfessorListaAssinaturaDigital(getDocumentacaoGED().getArquivo(), obj, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("NOME", "Nome"));
			tipoConsultaComboFuncionario.add(new SelectItem("MATRICULA", "Matrícula"));
			tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFuncionario.add(new SelectItem("CARGO", "Cargo"));
			tipoConsultaComboFuncionario.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		}
		return tipoConsultaComboFuncionario;
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
	
	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}
	
	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}
	
	public List<FuncionarioVO> getListaFuncionarioAssinarDigitalmenteVOs() {
		if (listaFuncionarioAssinarDigitalmenteVOs == null) {
			listaFuncionarioAssinarDigitalmenteVOs = new ArrayList<FuncionarioVO>(0);
		}
		return listaFuncionarioAssinarDigitalmenteVOs;
	}

	public void setListaFuncionarioAssinarDigitalmenteVOs(List<FuncionarioVO> listaFuncionarioAssinarDigitalmenteVOs) {
		this.listaFuncionarioAssinarDigitalmenteVOs = listaFuncionarioAssinarDigitalmenteVOs;
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarAluno();
	}
	
	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}
	public List<FuncionarioVO> getListaConsultaFuncionarioDocumentoGED() {
		if (listaConsultaFuncionarioDocumentoGED == null) {
			listaConsultaFuncionarioDocumentoGED = new ArrayList<FuncionarioVO>();
		}
		return listaConsultaFuncionarioDocumentoGED;
	}

	public void setListaConsultaFuncionarioDocumentoGED(List<FuncionarioVO> listaConsultaFuncionarioDocumentoGED) {
		this.listaConsultaFuncionarioDocumentoGED = listaConsultaFuncionarioDocumentoGED;
	}
}
