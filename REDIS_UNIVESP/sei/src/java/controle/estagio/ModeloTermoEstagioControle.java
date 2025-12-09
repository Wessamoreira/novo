package controle.estagio;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.financeiro.CartaCobrancaRelVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.MarcadorVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import relatorio.controle.arquitetura.SuperControleRelatorio;

@Controller("ModeloTermoEstagioControle")
@Scope("viewScope")
@Lazy
public class ModeloTermoEstagioControle extends SuperControleRelatorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4853124798816883714L;
	private static final String TELA_FORM = "modeloTermoEstagioForm.xhtml";
	private static final String TELA_CONS = "modeloTermoEstagioCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "modeloTermoEstagioItens";
	private TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
	private List listaContaReceber;
	private ArquivoVO arquivoVO;
	private ArquivoVO arquivoIreport;
	private ArquivoVO arquivoIreportSelecionado;
	private List<ArquivoVO> arquivoVOs;
	private StringBuilder stb;

	public ModeloTermoEstagioControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		removerObjetoMemoria(this);
		setTextoPadraoDeclaracaoVO(new TextoPadraoDeclaracaoVO());
		getTextoPadraoDeclaracaoVO().setUnidadeEnsino(new UnidadeEnsinoVO());
		getTextoPadraoDeclaracaoVO().setNivelEducacional("");
		getTextoPadraoDeclaracaoVO().setTipo("ES");
		getTextoPadraoDeclaracaoVO().setTipoDesigneTextoEnum(TipoDesigneTextoEnum.PDF);
		getTextoPadraoDeclaracaoVO().setAssinarDigitalmenteTextoPadrao(true);
		getTextoPadraoDeclaracaoVO().setResponsavelDefinicao(getUsuarioLogadoClone());
		montarListaArquivosImagem();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			TextoPadraoDeclaracaoVO obj = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setTextoPadraoDeclaracaoVO(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			montarListaArquivosImagem();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			if (textoPadraoDeclaracaoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTextoPadraoDeclaracaoFacade().incluir(getTextoPadraoDeclaracaoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			} else {
				getFacadeFactory().getTextoPadraoDeclaracaoFacade().alterar(getTextoPadraoDeclaracaoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			}
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String clonar() {
		try {
			textoPadraoDeclaracaoVO.clonar(getUsuarioLogadoClone());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getTextoPadraoDeclaracaoFacade().excluir(textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), Arrays.asList("'ES'"), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Arrays.asList("'ES'"), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataDefinicao")) {
				objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorDataDefinicao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataIni(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), Arrays.asList("'ES'"), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("responsavelDefinicao")) {
				objs = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorResponsavelDefinicao(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Arrays.asList("'ES'"), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
		}
	}

	public void excluirArquivo() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO) getRequestMap().get("imagemItens");
			arquivoVO.setDataIndisponibilizacao(new Date());
			arquivoVO.setManterDisponibilizacao(false);
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(arquivoVO, getUsuarioLogado());
//			getFacadeFactory().getArquivoFacade().excluir(arquivoVO, false, "", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			montarListaArquivosImagem();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosUpLoadArquivoIreport() throws Exception {
		setArquivoIreport(new ArquivoVO());
	}

	public void montarListaArquivosImagem() {
		try {
			setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarArquivosPorPastaBaseArquivo("imagemTextoPadrao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void upLoadArquivoIreport(FileUploadEvent uploadEvent) {
		try {
			getArquivoIreport().setOrigem(OrigemArquivo.TEXTO_PADRAO_DECLARACAO.getValor());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoIreport(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IREPORT_TMP, getUsuarioLogado());
			File file = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getArquivoIreport().getPastaBaseArquivo() + File.separator + getArquivoIreport().getNome());
			getStb().append(getTextoPadraoDeclaracaoVO().getTexto());
			getStb().append(UteisTextoPadrao.carregarTagsTextoPadraoPorTipoDesignerIreport(file));
			getTextoPadraoDeclaracaoVO().setTexto(stb.toString());
			verificarExisteArquivoIreportPrincipal();
			stb = new StringBuilder();
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.IMAGEM_TEXTOPADRAO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
			if (!getArquivoVO().getNome().equals("")) {
				try {
					getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		}
	}

	public void verificarExisteArquivoIreportPrincipal() {
		setOncompleteModal("");
		if (getTextoPadraoDeclaracaoVO().getListaArquivoIreport().isEmpty() || !getTextoPadraoDeclaracaoVO().possuiArquivoIreportPrincipal()) {
			setOncompleteModal("RichFaces.$('panelDefinirArquivoIreportPrincipal').show()");
			return;
		}
		adicionarArquivoIreport();
	}

	public void adicionarArquivoIreport() {
		try {
			getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarArquivoIreport(getTextoPadraoDeclaracaoVO().getListaArquivoIreport(), getArquivoIreport(), OrigemArquivo.TEXTO_PADRAO_DECLARACAO.getValor());
			setArquivoIreport(new ArquivoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerArquivoIreport() {
		try {
			getFacadeFactory().getArquivoFacade().excluir(getArquivoIreportSelecionado(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			getTextoPadraoDeclaracaoVO().getListaArquivoIreport().remove(getArquivoIreportSelecionado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void downloadArquivoJasperIreport() throws Exception {
		try {
			File arquivo = new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp() + File.separator + getTextoPadraoDeclaracaoVO().getArquivoIreport().getPastaBaseArquivoEnum().getValue() + File.separator + getTextoPadraoDeclaracaoVO().getArquivoIreport().getNome());
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

	public void downloadTemplateIreport() throws Exception {
		try {
			File arquivo = null;
			if (getTextoPadraoDeclaracaoVO().getTipo().equals("CO")) {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator + "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator + "template_textoPadraoCartaCobranca_ireport.zip");
			} else if (getTextoPadraoDeclaracaoVO().getTipo().equals("AM")) {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator + "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator + "template_professor_ireport.rar");
			} else if (getTextoPadraoDeclaracaoVO().getTipo().equals("DQ")) {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator + "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator + "template_DeclaracaoImpostoRendaRel_ireport.rar");
			} else {
				arquivo = new File(UteisJSF.getCaminhoBase() + File.separator + "relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator + "template_textoPadrao_ireport.rar");
			}
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
	
	public void realizarVisualizacaoTexto() {
    	try {    	
    		limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			ImpressaoContratoVO impressao = new ImpressaoContratoVO();
			impressao.setGerarNovoArquivoAssinado(true);
			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressao, getTextoPadraoDeclaracaoVO(), "", false, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setFazerDownload(true);
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

	/**
	 * limpa os campos da tela se selecionar uma opcao diferente de data de definição os campos serão limpos
	 * 
	 * @return
	 */
	public String realizarLimparCampos() {
		if (!getControleConsulta().getCampoConsulta().equals("dataDefinicao")) {
			getControleConsulta().setValorConsulta("");
		}
		return "";
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public List<SelectItem> listaSelectItemOrientacaoDaPagina;

	public List<SelectItem> getListaSelectItemOrientacaoDaPagina() {
		if (listaSelectItemOrientacaoDaPagina == null) {
			listaSelectItemOrientacaoDaPagina = new ArrayList<SelectItem>(0);
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("RE", "Retrato"));
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("PA", "Paisagem"));
		}
		return listaSelectItemOrientacaoDaPagina;
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("dataDefinicao", "Data Definição"));
		itens.add(new SelectItem("responsavelDefinicao", "Responsável Definição"));
		return itens;
	}

	public List<SelectItem> getListaSelectItemTipoNivelEducacional() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class);
	}

	public List<SelectItem> getListaSelectTipoDesigneTextoEnum() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
//		if (getIsTipoTextoPadraoCartaCobranca()) {
//			lista.add(new SelectItem(TipoDesigneTextoEnum.PDF, UteisJSF.internacionalizar("enum_" + TipoDesigneTextoEnum.PDF.getClass().getSimpleName() + "_" + TipoDesigneTextoEnum.PDF.toString())));
//		} else {
//			for (Enum enumerador : TipoDesigneTextoEnum.values()) {
//				lista.add(new SelectItem(enumerador, UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString())));
//			}
//		}
		for (Enum enumerador : TipoDesigneTextoEnum.values()) {
			lista.add(new SelectItem(enumerador, UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString())));
		}
		return lista;
	}

	public List getListaSelectItemMarcadoContaReceber() throws Exception {
		Hashtable plano = (Hashtable) Dominios.getMarcadoContaReceber();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = plano.keys();
		listaContaReceber = new ArrayList(0);
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) plano.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			listaContaReceber.add(marcador);
			marcador = new MarcadorVO();
		}
		return listaContaReceber;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>situacao</code>
	 */
	public List getListaSelectItemMarcadoAluno() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoAluno();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoProfessor() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoProfessor();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoUnidadeEnsino() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoUnidadeEnsino();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoOutras() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoOutras();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoMatricula() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoMatricula();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoEstagio() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoEstagio();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoInscProcSeletivo() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoInscProcSeletivo();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoDisciplina() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoDisciplina();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoCurso() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoCurso();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public List getListaSelectItemMarcadoDisciplinaDeclaracao() throws Exception {
		List objs = new ArrayList(0);
		Hashtable cliente = (Hashtable) Dominios.getMarcadoDisciplinaDeclaracao();
		MarcadorVO marcador = new MarcadorVO();
		Enumeration keys = cliente.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) cliente.get(value);
			marcador.setTag(value);
			marcador.setNome(label);
			objs.add(marcador);
			marcador = new MarcadorVO();
		}
		return objs;
	}

	public Boolean getIsTipoTextoPadraoEstagio() {
		if ((getTextoPadraoDeclaracaoVO().getTipo().equals("ES")) || (getTextoPadraoDeclaracaoVO().getTipo().equals("EO")) || (getTextoPadraoDeclaracaoVO().getTipo().equals("EC")) || (getTextoPadraoDeclaracaoVO().getTipo().equals("EN"))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getIsMostrarBotoes() {
		return getTextoPadraoDeclaracaoVO().getTipo().equals("OT") || getTextoPadraoDeclaracaoVO().getTipo().equals("TT") || getTextoPadraoDeclaracaoVO().getTipo().equals("CE");
	}

	public Boolean getIsTipoTextoPadraoAdvertencia() {
		return getTextoPadraoDeclaracaoVO().getTipo().equals("AD");
	}

	public Boolean getIsTipoTextoPadraoCartaCobranca() {
		if (getTextoPadraoDeclaracaoVO().getTipo().equals("CO")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
		if (textoPadraoDeclaracaoVO == null) {
			textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracaoVO;
	}

	public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
		this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
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

	public List<ArquivoVO> getArquivoVOs() {
		if (arquivoVOs == null) {
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}

	public ArquivoVO getArquivoIreport() {
		if (arquivoIreport == null) {
			arquivoIreport = new ArquivoVO();
		}
		return arquivoIreport;
	}

	public void setArquivoIreport(ArquivoVO arquivoIreport) {
		this.arquivoIreport = arquivoIreport;
	}

	public ArquivoVO getArquivoIreportSelecionado() {
		if (arquivoIreportSelecionado == null) {
			arquivoIreportSelecionado = new ArquivoVO();
		}
		return arquivoIreportSelecionado;
	}

	public void setArquivoIreportSelecionado(ArquivoVO arquivoIreportSelecionado) {
		this.arquivoIreportSelecionado = arquivoIreportSelecionado;
	}

	public StringBuilder getStb() {
		if (stb == null) {
			stb = new StringBuilder();
		}
		return stb;
	}

	public void setStb(StringBuilder stb) {
		this.stb = stb;
	}

}
