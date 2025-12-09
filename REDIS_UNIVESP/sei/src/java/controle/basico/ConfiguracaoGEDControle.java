package controle.basico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.dominios.IntegracaoTechCertEnum;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import controle.academico.ExpedicaoDiplomaControle;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.ConfiguracaoGedOrigemVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ConfiguracaoGEDControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoGEDControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private ConfiguracaoGEDVO configuracaoGEDVO;
	private Boolean realizouUpload;
	private FuncionarioVO funcionario1VO;
	private FuncionarioVO funcionario2VO;
	private FuncionarioVO funcionario3VO;
	private CargoVO cargoFuncionario1VO;
	private CargoVO cargoFuncionario2VO;
	private CargoVO cargoFuncionario3VO;
	private String tituloFuncionario1;
	private String tituloFuncionario2;
	private String tituloFuncionario3;
	private Integer funcionarioSelecionar;
	private List<SelectItem> listaSelectItemCargoFuncionario1;
	private List<SelectItem> listaSelectItemCargoFuncionario2;
	private List<SelectItem> listaSelectItemCargoFuncionario3;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;

	public ConfiguracaoGEDControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");

	}

	public String novo() throws Exception {
		removerObjetoMemoria(getConfiguracaoGEDVO());
		setConfiguracaoGEDVO(new ConfiguracaoGEDVO());
		getConfiguracaoGEDVO().setNovoObj(true);
		getConfiguracaoGEDVO().getConfiguracaoGedAtaResultadosFinaisVO();
		getConfiguracaoGEDVO().getConfiguracaoGedBoletimAcademicoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedCertificadoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedContratoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedDeclaracaoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedDiarioVO();
		getConfiguracaoGEDVO().getConfiguracaoGedDiplomaVO();
		getConfiguracaoGEDVO().getConfiguracaoGedDocumentoAlunoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedDocumentoProfessorVO();
		getConfiguracaoGEDVO().getConfiguracaoGedHistoricoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedImpostoRendaVO();
		getConfiguracaoGEDVO().getConfiguracaoGedRequerimentoVO();
		getConfiguracaoGEDVO().getConfiguracaoGedUploadInstitucionalVO();
		getConfiguracaoGEDVO().getConfiguracaoGedEstagioVO();
		getConfiguracaoGEDVO().getConfiguracaoGedAtaColacaoGrauVO();
		getConfiguracaoGEDVO().getConfiguracaoGedPlanoEnsinoVO();
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGEDForm");
	}

	public String editar() {
		try {
			ConfiguracaoGEDVO obj = (ConfiguracaoGEDVO) context().getExternalContext().getRequestMap().get("configuracaoGEDItem");
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getConfiguracaoGEDFacade().carregarDados(obj, getUsuarioLogado());
			setConfiguracaoGEDVO(obj);
			// Adicionado a configuracao Upload Institucional na edicao por causa do legado.
			getConfiguracaoGEDVO().getConfiguracaoGedUploadInstitucionalVO();
			getConfiguracaoGEDVO().getConfiguracaoGedEstagioVO();
			getConfiguracaoGEDVO().getConfiguracaoGedAtaColacaoGrauVO();
			montarListaSelectItemUnidadeEnsino();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGEDForm");
		} catch (Exception e) {

			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGEDCons");
		}

	}

	public void persistir() {
		try {
			getFacadeFactory().getConfiguracaoGEDFacade().persistir(getConfiguracaoGEDVO(),
					getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setRealizouUpload(false);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CidadeCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("CODIGO")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getConfiguracaoGEDFacade().consultaRapidaPorCodigo(new Integer(valorInt),
						true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("NOME")) {
				objs = getFacadeFactory().getConfiguracaoGEDFacade()
						.consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public List<SelectItem> comboboxProvedorAssinaturaPadrao(TipoOrigemDocumentoAssinadoEnum tipoDocumento) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(ProvedorDeAssinaturaEnum.SEI, ProvedorDeAssinaturaEnum.SEI.toString()));
		if (getConfiguracaoGEDVO().getHabilitarIntegracaoCertisign()) {
			lista.add(
					new SelectItem(ProvedorDeAssinaturaEnum.CERTISIGN, ProvedorDeAssinaturaEnum.CERTISIGN.toString()));
		}
		if (getConfiguracaoGEDVO().getHabilitarIntegracaoImprensaOficial()) {
			lista.add(new SelectItem(ProvedorDeAssinaturaEnum.IMPRENSAOFICIAL,
					ProvedorDeAssinaturaEnum.IMPRENSAOFICIAL.toString()));
		}
		if (getConfiguracaoGEDVO().getHabilitarIntegracaoTechCert() &&
				getFacadeFactory().getDocumentoAssinadoFacade()
						.isHabilitadoTipoDocumentoTechCert(tipoDocumento)) {
			lista.add(new SelectItem(ProvedorDeAssinaturaEnum.TECHCERT, ProvedorDeAssinaturaEnum.TECHCERT.toString()));
		}
		return lista;
	}

	public void excluir() {
		try {
			getFacadeFactory().getConfiguracaoGEDFacade().excluir(getConfiguracaoGEDVO(), getUsuarioLogado(),
					getConfiguracaoGeralPadraoSistema());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void realizarValidacaoTokenProvedorDeAssinatura() {
		try {
			getFacadeFactory().getConfiguracaoGEDFacade()
					.realizarValidacaoTokenProvedorDeAssinatura(getConfiguracaoGEDVO(), getUsuarioLogadoClone());
			setMensagemID("msg_dados_tokenAutenticado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("NOME", "Nome"));
		itens.add(new SelectItem("CODIGO", "Código"));
		return itens;
	}

	public void realizarUploadCertificadoUnidadeEnsino(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile()
					.getSize() > (getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() * 1024 * 1024)) {
				throw new Exception(
						"Seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem.");
			}
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent,
					getConfiguracaoGEDVO().getCertificadoDigitalUnidadeEnsinoVO(), getConfiguracaoGeralPadraoSistema(),
					PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP, getUsuarioLogado());
			getConfiguracaoGEDVO().getCertificadoDigitalUnidadeEnsinoVO()
					.setOrigem(OrigemArquivo.CERTIFICADO_GED.getValor());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void realizarUploadCertificadoUnidadeCertificadora(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile()
					.getSize() > (getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() * 1024 * 1024)) {
				throw new Exception(
						"Seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem.");
			}
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent,
					getConfiguracaoGEDVO().getCertificadoDigitalUnidadeCertificadora(),
					getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP,
					getUsuarioLogado());
			getConfiguracaoGEDVO().getCertificadoDigitalUnidadeCertificadora()
					.setOrigem(OrigemArquivo.CERTIFICADO_GED.getValor());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void realizarUploadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile()
					.getSize() > (getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() * 1024 * 1024)) {
				throw new Exception(
						"Seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem.");
			}
			getFacadeFactory().getArquivoHelper().validarTamanhoSelo(uploadEvent);
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent,
					getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO(), getConfiguracaoGeralPadraoSistema(),
					PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP, getUsuarioLogado());
			getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().setOrigem(OrigemArquivo.SELO_GED.getValor());
			setRealizouUpload(true);
			setApresentarMensagemErro(Boolean.FALSE);
		} catch (Exception e) {
			setApresentarMensagemErro(Boolean.TRUE);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			uploadEvent = null;
		}
	}

	public Boolean apresentarMensagemErro;

	public Boolean getApresentarMensagemErro() {
		if (apresentarMensagemErro == null) {
			apresentarMensagemErro = false;
		}
		return apresentarMensagemErro;
	}

	public void setApresentarMensagemErro(Boolean apresentarMensagemErro) {
		this.apresentarMensagemErro = apresentarMensagemErro;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGEDCons");
	}

	public ConfiguracaoGEDVO getConfiguracaoGEDVO() {
		if (configuracaoGEDVO == null) {
			configuracaoGEDVO = new ConfiguracaoGEDVO();
		}
		return configuracaoGEDVO;
	}

	public void setConfiguracaoGEDVO(ConfiguracaoGEDVO configuracaoGEDVO) {
		this.configuracaoGEDVO = configuracaoGEDVO;
	}

	public String getUrlSeloApresentar() {
		if (getConfiguracaoGEDVO().getExisteSelo()) {
			try {
				if (getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getCodigo().equals(0)
						|| !getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getCodigo().equals(0)
								&& getRealizouUpload()) {
					return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
							+ PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP.getValue() + "/"
							+ getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getNome();
				}
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/"
						+ PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS.getValue() + "/"
						+ getConfiguracaoGEDVO().getSeloAssinaturaEletronicaVO().getNome();
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public void limparDadosSelo() {
		try {
			getFacadeFactory().getConfiguracaoGEDFacade().alterarSeloAssinaturaEletronica(getConfiguracaoGEDVO(),
					getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getRealizouUpload() {
		if (realizouUpload == null) {
			realizouUpload = false;
		}
		return realizouUpload;
	}

	public void setRealizouUpload(Boolean realizouUpload) {
		this.realizouUpload = realizouUpload;
	}

	public void realizarDesmarcarTodos(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO) {
		configuracaoGedOrigemVO.setAssinarDocumento(false);
		configuracaoGedOrigemVO.setApresentarSelo(false);
		configuracaoGedOrigemVO.setApresentarQrCode(false);
		if (configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado().equals(TipoOrigemDocumentoAssinadoEnum.DIARIO)) {
			configuracaoGedOrigemVO.setAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada(false);
		}
		if (configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
				.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)
				|| configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
						.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)) {
			configuracaoGedOrigemVO.setAssinaturaUnidadeEnsino(false);
			configuracaoGedOrigemVO.setAssinarDocumentoFuncionarioResponsavel(false);
		}
		if (!configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
				.equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO)
				&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
						.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)
				&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
						.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)
				&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
						.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED)) {
			configuracaoGedOrigemVO.setApresentarAssinaturaDigitalizadoFuncionario(false);
		}
	}

	public void realizarMarcarTodos(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO) {

		if (configuracaoGedOrigemVO.getAssinarDocumento()) {
			configuracaoGedOrigemVO.setApresentarSelo(true);
			configuracaoGedOrigemVO.setApresentarQrCode(true);
			if (configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
					.equals(TipoOrigemDocumentoAssinadoEnum.DIARIO)) {
				configuracaoGedOrigemVO.setAssinarDigitalmenteDiarioPorTodosProfessoresComAulaProgramada(true);
			}
			if (configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
					.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)
					|| configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
							.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)) {
				configuracaoGedOrigemVO.setAssinaturaUnidadeEnsino(true);
				configuracaoGedOrigemVO.setAssinarDocumentoFuncionarioResponsavel(true);
			}
			if (!configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
					.equals(TipoOrigemDocumentoAssinadoEnum.EMISSAO_CERTIFICADO)
					&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
							.equals(TipoOrigemDocumentoAssinadoEnum.CONTRATO)
					&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
							.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_ALUNO)
					&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
							.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_PROFESSOR)
					&& !configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado()
							.equals(TipoOrigemDocumentoAssinadoEnum.DOCUMENTO_GED)) {
				configuracaoGedOrigemVO.setApresentarAssinaturaDigitalizadoFuncionario(true);
			}
		} else {
			realizarDesmarcarTodos(configuracaoGedOrigemVO);
		}
	}

	private ConfiguracaoGedOrigemVO configuracaoGedOrigemVO;

	public ConfiguracaoGedOrigemVO getConfiguracaoGedOrigemVO() {
		if (configuracaoGedOrigemVO == null) {
			configuracaoGedOrigemVO = new ConfiguracaoGedOrigemVO(TipoOrigemDocumentoAssinadoEnum.NENHUM);
		}
		return configuracaoGedOrigemVO;
	}

	public void setConfiguracaoGedOrigemVO(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO) {
		this.configuracaoGedOrigemVO = configuracaoGedOrigemVO;
	}

	private boolean previewPaisagem = false;

	public boolean isPreviewPaisagem() {
		return previewPaisagem;
	}

	public void setPreviewPaisagem(boolean previewPaisagem) {
		this.previewPaisagem = previewPaisagem;
	}

	public void realizarGeracaoDocumentooPreVisualizacao(ConfiguracaoGedOrigemVO configuracaoGedOrigemVO,
			boolean validarFuncionario, boolean previewPaisagem) {
		setConfiguracaoGedOrigemVO(configuracaoGedOrigemVO);
		setPreviewPaisagem(previewPaisagem);
		if (validarFuncionario && ((getConfiguracaoGedOrigemVO().getApresentarAssinaturaFuncionario1()
				&& getConfiguracaoGedOrigemVO().getApresentarAssinaturaFuncionario1())
				|| (getConfiguracaoGedOrigemVO().getApresentarAssinaturaFuncionario2()
						&& getConfiguracaoGedOrigemVO().getApresentarAssinaturaFuncionario2())
				|| (getConfiguracaoGedOrigemVO().getApresentarAssinaturaFuncionario3()
						&& getConfiguracaoGedOrigemVO().getApresentarAssinaturaFuncionario3()))) {
			setOncompleteModal("RichFaces.$('panelFuncionarioAssinatura').show()");
			return;
		}

		setFazerDownload(false);
		setCaminhoRelatorio("");
		String arquivoOriginal = "";
		Document document = null;
		PdfWriter writer = null;
		File file = null;
		try {
			arquivoOriginal = UteisJSF.getCaminhoWeb() + "relatorio" + File.separator
					+ configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado().name() + ".pdf";
			file = new File(arquivoOriginal);
			if (file.exists()) {
				file.delete();
			}
			document = new Document(
					isPreviewPaisagem() ? com.lowagie.text.PageSize.A4.rotate() : com.lowagie.text.PageSize.A4);
			writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			document.add(new com.lowagie.text.Paragraph("Primeira Pagina"));
			document.newPage();
			document.add(new com.lowagie.text.Paragraph("Última Pagina"));

		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return;
		} finally {
			if (document != null) {
				document.close();
				document = null;
			}
			if (writer != null) {

				writer.flush();
				writer.close();
				writer = null;
			}

		}

		DocumentoAssinadoVO documentoAssinadoVO = new DocumentoAssinadoVO();
		try {

			getFacadeFactory().getDocumentoAssinadoFacade().adicionarSeloPDF(
					configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado(), arquivoOriginal,
					getConfiguracaoGeralSistemaVO(), getConfiguracaoGEDVO());
			getFacadeFactory().getDocumentoAssinadoFacade().adicionarQRCodePDF(
					configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado(), arquivoOriginal,
					getConfiguracaoGeralSistemaVO().getUrlAcessoExternoAplicacao()
							+ "/visaoAdministrativo/academico/documentoAssinado.xhtml?tipoDoc="
							+ configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado() + "&dados=0",
					getConfiguracaoGeralSistemaVO(), getConfiguracaoGEDVO());
			documentoAssinadoVO.setUsuario(getUsuarioLogado());
			documentoAssinadoVO
					.setTipoOrigemDocumentoAssinadoEnum(configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado());
			documentoAssinadoVO.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP);
			documentoAssinadoVO.getArquivo()
					.setPastaBaseArquivo(PastaBaseArquivoEnum.DOCUMENTOS_ASSINADOS_TMP.getValue());
			documentoAssinadoVO.getArquivo().setNome("preViewConfGed_"
					+ configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado().name() + "_Assinado.pdf");
			documentoAssinadoVO.getArquivo().setDescricao("preViewConfGed_"
					+ configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado().name() + "_Assinado.pdf");
			documentoAssinadoVO.getArquivo().setExtensao(".pdf");
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoVO())) {
				getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(getUnidadeEnsinoVO(), NivelMontarDados.BASICO,
						getUsuarioLogado());
				documentoAssinadoVO.setUnidadeEnsinoVO(getUnidadeEnsinoVO());
			}
			documentoAssinadoVO.setCodigo(123456789);
			documentoAssinadoVO.setDataRegistro(new Date());
			getFacadeFactory().getDocumentoAssinadoFacade().preencherAssinadorDigitalDocumentoPdf(arquivoOriginal,
					getConfiguracaoGEDVO().getCertificadoDigitalUnidadeEnsinoVO(),
					getConfiguracaoGEDVO().getSenhaCertificadoDigitalUnidadeEnsino(), documentoAssinadoVO, null,
					"#000000", Float.valueOf(configuracaoGedOrigemVO.getAlturaAssinaturaUnidadeEnsino()),
					Float.valueOf(configuracaoGedOrigemVO.getLarguraAssinaturaUnidadeEnsino()), 8,
					configuracaoGedOrigemVO.getPosicaoXAssinaturaUnidadeEnsino(),
					configuracaoGedOrigemVO.getPosicaoYAssinaturaUnidadeEnsino(), 0, 0,
					configuracaoGedOrigemVO.isUltimaPaginaAssinaturaUnidadeEnsino(),
					configuracaoGedOrigemVO.getApresentarAssinaturaUnidadeEnsino(), getConfiguracaoGeralSistemaVO(),
					true, false);
			if (configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario1()
					&& configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario1()
					&& Uteis.isAtributoPreenchido(getFuncionario1VO())) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaFuncionarioPreVisualizacao(
						documentoAssinadoVO, configuracaoGEDVO, configuracaoGedOrigemVO, funcionario1VO,
						getCargoFuncionario1VO(), getTituloFuncionario1(), 1);
			}
			if (configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario2()
					&& configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario2()
					&& Uteis.isAtributoPreenchido(getFuncionario2VO())) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaFuncionarioPreVisualizacao(
						documentoAssinadoVO, configuracaoGEDVO, configuracaoGedOrigemVO, funcionario2VO,
						getCargoFuncionario2VO(), getTituloFuncionario1(), 2);
			}
			if (configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario3()
					&& configuracaoGedOrigemVO.getApresentarAssinaturaFuncionario3()
					&& Uteis.isAtributoPreenchido(getFuncionario3VO())) {
				getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaFuncionarioPreVisualizacao(
						documentoAssinadoVO, configuracaoGEDVO, configuracaoGedOrigemVO, funcionario3VO,
						getCargoFuncionario3VO(), getTituloFuncionario1(), 3);
			}
			getFacadeFactory().getArquivoHelper()
					.disponibilizarArquivoAssinadoParaDowload(
							getConfiguracaoGeralSistemaVO().getLocalUploadArquivoFixo() + File.separator
									+ documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator
									+ documentoAssinadoVO.getArquivo().getNome(),
							documentoAssinadoVO.getArquivo().getNome());
			setFazerDownload(true);
			setCaminhoRelatorio("preViewConfGed_" + configuracaoGedOrigemVO.getTipoOrigemDocumentoAssinado().name()
					+ "_Assinado.pdf");
			setOncompleteModal("RichFaces.$('panelFuncionarioAssinatura').hide()");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			try {
				File fileAssinar = new File(getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, getUsuarioLogado())
						.getLocalUploadArquivoFixo() + File.separator
						+ documentoAssinadoVO.getArquivo().getPastaBaseArquivo() + File.separator
						+ documentoAssinadoVO.getArquivo().getNome());
				if (fileAssinar.exists()) {
					fileAssinar.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (file != null && file.exists()) {
				file.delete();
				file = null;
			}
			documentoAssinadoVO = null;
		}

	}

	public void limparFuncionario(Integer nrFuncionario) throws Exception {
		if (nrFuncionario.equals(1)) {
			setFuncionario1VO(null);
			setCargoFuncionario1VO(null);
			setTituloFuncionario1(null);
		} else if (nrFuncionario.equals(2)) {
			setFuncionario2VO(null);
			setCargoFuncionario2VO(null);
			setTituloFuncionario2(null);
		} else if (nrFuncionario.equals(3)) {
			setFuncionario3VO(null);
			setCargoFuncionario3VO(null);
			setTituloFuncionario3(null);
		}
	}

	public void consultarFuncionarioPorMatricula(Integer nrFuncionario) throws Exception {
		try {
			if (nrFuncionario.equals(1)) {
				if (!getFuncionario1VO().getMatricula().isEmpty()) {
					setFuncionario1VO(getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(
							getFuncionario1VO().getMatricula(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX,
							getUsuarioLogado()));
					setListaSelectItemCargoFuncionario1(
							montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
									.consultarCargoPorCodigoFuncionario(getFuncionario1VO().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					if (!getListaSelectItemCargoFuncionario1().isEmpty()) {
						setCargoFuncionario1VO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
								(Integer) getListaSelectItemCargoFuncionario1().get(0).getValue(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					} else {
						setCargoFuncionario1VO(null);
					}
				} else {
					setFuncionario1VO(null);
					setCargoFuncionario1VO(null);
					setTituloFuncionario1(null);
				}
			} else if (nrFuncionario.equals(2)) {
				if (!getFuncionario2VO().getMatricula().isEmpty()) {
					setFuncionario2VO(getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(
							getFuncionario2VO().getMatricula(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX,
							getUsuarioLogado()));
					setListaSelectItemCargoFuncionario2(
							montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
									.consultarCargoPorCodigoFuncionario(getFuncionario2VO().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					if (!getListaSelectItemCargoFuncionario2().isEmpty()) {
						setCargoFuncionario2VO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
								(Integer) getListaSelectItemCargoFuncionario2().get(0).getValue(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					} else {
						setCargoFuncionario2VO(null);
					}
				} else {
					setFuncionario2VO(null);
					setCargoFuncionario2VO(null);
					setTituloFuncionario2(null);
				}

			} else {
				if (!getFuncionario3VO().getMatricula().isEmpty()) {
					setFuncionario3VO(getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(
							getFuncionario3VO().getMatricula(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX,
							getUsuarioLogado()));
					setListaSelectItemCargoFuncionario3(
							montarComboCargoFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
									.consultarCargoPorCodigoFuncionario(getFuncionario3VO().getCodigo(), false,
											Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())));
					if (!getListaSelectItemCargoFuncionario3().isEmpty()) {
						setCargoFuncionario3VO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(
								(Integer) getListaSelectItemCargoFuncionario3().get(0).getValue(), false,
								Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
					} else {
						setCargoFuncionario3VO(null);
					}
				} else {
					setFuncionario3VO(null);
					setCargoFuncionario3VO(null);
					setTituloFuncionario3(null);
				}
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		try {
			if (cargos != null && !cargos.isEmpty()) {
				List<SelectItem> selectItems = new ArrayList<SelectItem>();
				for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
					selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(),
							funcionarioCargoVO.getCargo().getNome() + " - "
									+ funcionarioCargoVO.getUnidade().getNome()));
					removerObjetoMemoria(funcionarioCargoVO);
				}
				return selectItems;
			} else {
				setMensagemDetalhada("O Funcionário selecionado não possui cargo configurado");
			}
			return null;
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(cargos);
		}

	}

	public FuncionarioVO getFuncionario1VO() {
		if (funcionario1VO == null) {
			funcionario1VO = new FuncionarioVO();
		}
		return funcionario1VO;
	}

	public void setFuncionario1VO(FuncionarioVO funcionario1vo) {
		funcionario1VO = funcionario1vo;
	}

	public FuncionarioVO getFuncionario2VO() {
		if (funcionario2VO == null) {
			funcionario2VO = new FuncionarioVO();
		}
		return funcionario2VO;
	}

	public void setFuncionario2VO(FuncionarioVO funcionario2vo) {
		funcionario2VO = funcionario2vo;
	}

	public FuncionarioVO getFuncionario3VO() {
		if (funcionario3VO == null) {
			funcionario3VO = new FuncionarioVO();
		}
		return funcionario3VO;
	}

	public void setFuncionario3VO(FuncionarioVO funcionario3vo) {
		funcionario3VO = funcionario3vo;
	}

	public CargoVO getCargoFuncionario1VO() {
		if (cargoFuncionario1VO == null) {
			cargoFuncionario1VO = new CargoVO();
		}
		return cargoFuncionario1VO;
	}

	public void setCargoFuncionario1VO(CargoVO cargoFuncionario1VO) {
		this.cargoFuncionario1VO = cargoFuncionario1VO;
	}

	public CargoVO getCargoFuncionario2VO() {
		if (cargoFuncionario2VO == null) {
			cargoFuncionario2VO = new CargoVO();
		}
		return cargoFuncionario2VO;
	}

	public void setCargoFuncionario2VO(CargoVO cargoFuncionario2VO) {
		this.cargoFuncionario2VO = cargoFuncionario2VO;
	}

	public CargoVO getCargoFuncionario3VO() {
		if (cargoFuncionario3VO == null) {
			cargoFuncionario3VO = new CargoVO();
		}
		return cargoFuncionario3VO;
	}

	public void setCargoFuncionario3VO(CargoVO cargoFuncionario3VO) {
		this.cargoFuncionario3VO = cargoFuncionario3VO;
	}

	public String getTituloFuncionario1() {
		if (tituloFuncionario1 == null) {
			tituloFuncionario1 = "";
		}
		return tituloFuncionario1;
	}

	public void setTituloFuncionario1(String tituloFuncionario1) {
		this.tituloFuncionario1 = tituloFuncionario1;
	}

	public String getTituloFuncionario2() {
		if (tituloFuncionario2 == null) {
			tituloFuncionario2 = "";
		}
		return tituloFuncionario2;
	}

	public void setTituloFuncionario2(String tituloFuncionario2) {
		this.tituloFuncionario2 = tituloFuncionario2;
	}

	public String getTituloFuncionario3() {
		if (tituloFuncionario3 == null) {
			tituloFuncionario3 = "";
		}
		return tituloFuncionario3;
	}

	public void setTituloFuncionario3(String tituloFuncionario3) {
		this.tituloFuncionario3 = tituloFuncionario3;
	}

	public Integer getFuncionarioSelecionar() {
		if (funcionarioSelecionar == null) {
			funcionarioSelecionar = 1;
		}
		return funcionarioSelecionar;
	}

	public void setFuncionarioSelecionar(Integer funcionarioSelecionar) {
		this.funcionarioSelecionar = funcionarioSelecionar;
	}

	public List<SelectItem> getListaSelectItemCargoFuncionario1() {
		if (listaSelectItemCargoFuncionario1 == null) {
			listaSelectItemCargoFuncionario1 = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCargoFuncionario1;
	}

	public void setListaSelectItemCargoFuncionario1(List<SelectItem> listaSelectItemCargoFuncionario1) {
		this.listaSelectItemCargoFuncionario1 = listaSelectItemCargoFuncionario1;
	}

	public List<SelectItem> getListaSelectItemCargoFuncionario2() {
		if (listaSelectItemCargoFuncionario2 == null) {
			listaSelectItemCargoFuncionario2 = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCargoFuncionario2;
	}

	public void setListaSelectItemCargoFuncionario2(List<SelectItem> listaSelectItemCargoFuncionario2) {
		this.listaSelectItemCargoFuncionario2 = listaSelectItemCargoFuncionario2;
	}

	public List<SelectItem> getListaSelectItemCargoFuncionario3() {
		if (listaSelectItemCargoFuncionario3 == null) {
			listaSelectItemCargoFuncionario3 = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCargoFuncionario3;
	}

	public void setListaSelectItemCargoFuncionario3(List<SelectItem> listaSelectItemCargoFuncionario3) {
		this.listaSelectItemCargoFuncionario3 = listaSelectItemCargoFuncionario3;
	}

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(),
						"", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(
						getValorConsultaFuncionario(), 0, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(),
						0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "",
						0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(),
						0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(
						getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(
						getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
						getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID",
					"msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		if (getFuncionarioSelecionar().equals(1)) {
			getFuncionario1VO().setMatricula(obj.getMatricula());
		} else if (getFuncionarioSelecionar().equals(2)) {
			getFuncionario2VO().setMatricula(obj.getMatricula());
		} else {
			getFuncionario3VO().setMatricula(obj.getMatricula());
		}
		consultarFuncionarioPorMatricula(getFuncionarioSelecionar());
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = null;
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
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

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsinoPorNome(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsinoPorNome("");
			}
			setMensagemID("");
		} catch (Exception e) {

		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade()
				.consultarUnidadeEnsinoComboBox(super.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsinoPorNome(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
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

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());

				return true;
			}
		} catch (Exception ex) {
			return false;
		}
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
}
