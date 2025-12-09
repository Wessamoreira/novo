package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas devolucaoCompraForm.jsp
 * devolucaoCompraCons.jsp) com as funcionalidades da classe <code>DevolucaoCompra</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see DevolucaoCompra
 * @see DevolucaoCompraVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.CreditoFornecedorVO;
import negocio.comuns.compras.DevolucaoCompraItemImagemVO;
import negocio.comuns.compras.DevolucaoCompraItemVO;
import negocio.comuns.compras.DevolucaoCompraVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@Controller("DevolucaoCompraControle")
@Scope("viewScope")
@Lazy
public class DevolucaoCompraControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private DevolucaoCompraVO devolucaoCompraVO;
	private ContaPagarVO contaPagarVO;
	private CreditoFornecedorVO creditoFornecedorVO;
	private String campoConsultaCompra;
	private String valorConsultaCompra;
	private String sugestaoValorParcelas;
	private List<ContaPagarVO> listaConsultaContaPagar;
	private List<CompraVO> listaConsultaCompra;
	private List<CompraItemVO> compraItemVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaFormaReceber;
	private Boolean novoObj;
	private DevolucaoCompraItemVO devolucaoCompraItemVO;
	private DevolucaoCompraItemVO devolucaoCompraItemVOTemp;
	private Boolean abrirPainelContaPagar;
	private Date valorConsultaCompraData;

	public DevolucaoCompraControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_prmconsulta");
		setListaConsultaContaPagar(new ArrayList<ContaPagarVO>(0));
	}

	public void inicializarResponsavel() {
		try {
			devolucaoCompraVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>DevolucaoCompra</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Novo Compra", "Novo");
		removerObjetoMemoria(this);
		setDevolucaoCompraVO(new DevolucaoCompraVO());
		setDevolucaoCompraItemVO(new DevolucaoCompraItemVO());
		setDevolucaoCompraItemVOTemp(new DevolucaoCompraItemVO());
		setCreditoFornecedorVO(new CreditoFornecedorVO());
		setContaPagarVO(new ContaPagarVO());
		setNovoObj(Boolean.TRUE);
		setListaConsultaCompra(new ArrayList<CompraVO>(0));
		setListaConsultaContaPagar(new ArrayList<ContaPagarVO>(0));
		inicializarResponsavel();
		inicializarListasSelectItemTodosComboBox();
		getDevolucaoCompraVO().setValorTotalCredito(0.0);
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>DevolucaoCompra</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Inicializando Editar Compra", "Editando");
		DevolucaoCompraVO obj = (DevolucaoCompraVO) context().getExternalContext().getRequestMap().get("devolucaoCompraItens");
		obj.setNovoObj(Boolean.FALSE);
		setDevolucaoCompraVO(obj);
		setNovoObj(Boolean.FALSE);
		setListaConsultaCompra(new ArrayList<CompraVO>(0));
		inicializarListasSelectItemTodosComboBox();
		setDevolucaoCompraItemVO(new DevolucaoCompraItemVO());
		setDevolucaoCompraItemVOTemp(new DevolucaoCompraItemVO());
		registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Finalizando Editar Compra", "Editando");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>DevolucaoCompra</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto
	 * não é gravado, sendo re-apresentado para o usuário juntamente com uma
	 * mensagem de erro.
	 */
	public void gravar() {
		try {
			if (devolucaoCompraVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Inicializando Incluir Compra", "Incluindo");
				getFacadeFactory().getDevolucaoCompraFacade().incluir(devolucaoCompraVO, getUsuarioLogado(), true);
				registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Finalizando Incluir Compra", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Inicializando Alterar Compra", "Alterando");
				getFacadeFactory().getDevolucaoCompraFacade().alterar(devolucaoCompraVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Finalizando Alterar Compra", "Alterando");
			}
			setNovoObj(Boolean.FALSE);
			setMensagemID("msg_dados_gravados");

		} catch (Exception e) {
			if (devolucaoCompraVO.getFormaReceber().equals("alterar")) {
				for (ContaPagarVO contaPagar : devolucaoCompraVO.getConsultaContaPagar()) {
					try {
						ContaPagarVO contaPagarOriginal = (ContaPagarVO) getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(contaPagar.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
						contaPagar.setValor(contaPagarOriginal.getValor());
					} catch (Exception e1) {

					}

				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * DevolucaoCompraCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Inicializando Consultar Compra", "Consultar");
			super.consultar();
			List<DevolucaoCompraVO> objs = new ArrayList<>(0);
			int valorInt = 0;
			if (getControleConsulta().getCampoConsulta().equals("fornecedor")) {
				objs = getFacadeFactory().getDevolucaoCompraFacade().consultarPorNomeFornecedor(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				}
				objs = getFacadeFactory().getDevolucaoCompraFacade().consultarPorCodigo(new Integer(valorInt), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoCompra")) {
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				}
				objs = getFacadeFactory().getDevolucaoCompraFacade().consultarPorCodigoCompra(new Integer(valorInt), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Finalizando Consultar Compra", "Consultar");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<SelectItem>(0));
			if (e.getMessage() != null && e.getMessage().contains("For input string") && (getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("codigoCompra"))) {
				setMensagemDetalhada("msg_erro", "Por favor, informe somente números.");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>DevolucaoCompraVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Inicializando Excluir Compra", "Excluindo");
			getFacadeFactory().getDevolucaoCompraFacade().excluir(devolucaoCompraVO, getUsuarioLogado());
			setDevolucaoCompraVO(new DevolucaoCompraVO());
			setDevolucaoCompraItemVO(new DevolucaoCompraItemVO());
			setDevolucaoCompraItemVOTemp(new DevolucaoCompraItemVO());
			inicializarResponsavel();
			setNovoObj(Boolean.TRUE);
			setListaConsultaCompra(new ArrayList<CompraVO>(0));
			registrarAtividadeUsuario(getUsuarioLogado(), "DevolucaoCompraControle", "Finalizando Excluir Compra", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraForm.xhtml");
		}
	}

	public void consultarCompraPorChavePrimaria() {
		try {
			selecionarCompra(getFacadeFactory().getCompraFacade().consultarPorChavePrimaria(getDevolucaoCompraVO().getCompra().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			getDevolucaoCompraVO().setCompra(new CompraVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCompra() {
		try {
			CompraVO obj = (CompraVO) context().getExternalContext().getRequestMap().get("compraItens");
			getListaConsultaCompra().clear();
			if (Uteis.isAtributoPreenchido(obj)) {
				selecionarCompra(getFacadeFactory().getCompraFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}
			setValorConsultaCompra("");
			setCampoConsultaCompra("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void selecionarCompra(CompraVO compraVO) throws Exception {
		if (!compraVO.getSituacaoRecebimento().equals("PE")) {
			getDevolucaoCompraVO().setCompra(compraVO);
			getDevolucaoCompraVO().getUnidadeEnsino().setCodigo(compraVO.getUnidadeEnsino().getCodigo());
			getDevolucaoCompraVO().getDevolucaoCompraItemVOs().clear();
			getDevolucaoCompraVO().setValorTotalCredito(0.0);
			montarListaCompraItem();
			setMensagemID("msg_dados_consultados");
		} else {
			throw new Exception("Nenhum PRODUTO da compra informada foi entregue.");
		}
	}

	public void selecionarCompraItem() throws Exception {
		CompraItemVO obj = (CompraItemVO) context().getExternalContext().getRequestMap().get("compraItemItens");
		this.getDevolucaoCompraItemVO().setCompraItem(obj);
		this.getDevolucaoCompraItemVO().setQuantidade(obj.getQuantidadeRecebida().doubleValue());
	}

	public void consultarContaPagarPorCodigoOrigem() {
		try {
			List<ContaPagarVO> contaPagar = getFacadeFactory().getContaPagarFacade().consultarPorCodigoCompra(getDevolucaoCompraVO().getCompra().getCodigo(), 
					getDevolucaoCompraVO().getCompra().getTipoCriacaoContaPagarEnum(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			setListaConsultaContaPagar(contaPagar);
			getDevolucaoCompraVO().setConsultaContaPagar(contaPagar);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaContaPagar(new ArrayList<ContaPagarVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getCaminhoServidorDownload() throws Exception {
		try {
			DevolucaoCompraItemVO obj = (DevolucaoCompraItemVO) context().getExternalContext().getRequestMap().get("devolucaoCompraItemItens");
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivo(), PastaBaseArquivoEnum.DEVOLUCAO_COMPRA, getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void montarListaCompraItem() {
		setCompraItemVO(new ArrayList<CompraItemVO>(0));
		Iterator<CompraItemVO> i = getDevolucaoCompraVO().getCompra().getCompraItemVOs().iterator();
		while (i.hasNext()) {
			CompraItemVO obj = (CompraItemVO) i.next();
			if (obj.getQuantidadeRecebida().doubleValue() > 0.0) {
				getCompraItemVO().add(obj);
			}
		}

	}

	public void montarListaFormaReceber() {
		List<SelectItem> obj = new ArrayList<SelectItem>(0);
		obj.add(new SelectItem("mercadoria", "Receber em Mercadoria"));
		obj.add(new SelectItem("acordar", "Acordar com Cliente"));
//		obj.add(new SelectItem("alterar", "Alterar Contas à Pagar"));
		setListaFormaReceber(obj);
	}

	/*
	 * public void upload(FileUploadEvent upload) { // UploadItem item =
	 * upload.getUploadItem(); // File item1 = item.getFile(); UploadedFile item =
	 * upload.getUploadedFile();
	 * 
	 * String nomeImagem = null; File item2 = null; try { // nomeImagem =
	 * item.getFileName().substring(item.getFileName().lastIndexOf(File.separator) +
	 * 1); nomeImagem =
	 * item.getName().substring(item.getName().lastIndexOf(File.separator) + 1);
	 * nomeImagem = Uteis.retirarSinaisSimbolosEspacoString(nomeImagem); nomeImagem
	 * = Uteis.retirarAcentuacao(nomeImagem); item2 = new
	 * File(obterCaminhoWebImagem() + File.separator
	 * +PastaBaseArquivoEnum.DEVOLUCAO_COMPRA_TMP + File.separator+ nomeImagem);
	 * item2.delete(); // item1.renameTo(new File(obterCaminhoWebFotos() +
	 * File.separator + nomeImagem)); File item1 = new File(obterCaminhoWebImagem()
	 * + File.separator +PastaBaseArquivoEnum.DEVOLUCAO_COMPRA + File.separator+
	 * nomeImagem); DevolucaoCompraItemImagemVO imagemVO = new
	 * DevolucaoCompraItemImagemVO(); imagemVO.setImagem(obterCaminhoWebImagem() +
	 * File.separator + nomeImagem); imagemVO.setImagemTemp("./imagens/" +
	 * nomeImagem); imagemVO.setNomeImagem(nomeImagem);
	 * getDevolucaoCompraItemVO().adicionarObjDevolucaoCompraItemImagemVOs(imagemVO)
	 * ; } catch (Exception e) { setMensagemDetalhada("msg_erro", e.getMessage()); }
	 * finally { upload = null; item = null; nomeImagem = null; item2 = null; } }
	 */

	public void upload(FileUploadEvent upload) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(upload, getDevolucaoCompraItemVO().getArquivo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.DEVOLUCAO_COMPRA, getUsuarioLogado());
			this.setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			upload = null;
		}
	}

	public int getQtdeImagem() {
		if (getDevolucaoCompraItemVO().getDevolucaoCompraItemImagemVOs().size() > 7) {
			return 7;
		} else {
			return getDevolucaoCompraItemVO().getDevolucaoCompraItemImagemVOs().size();
		}
	}

	public void removerDevolucaoCompraItemImagemTemp() throws Exception {
		DevolucaoCompraItemImagemVO obj = (DevolucaoCompraItemImagemVO) context().getExternalContext().getRequestMap().get("imagemItens");
		getDevolucaoCompraItemVOTemp().excluirObjDevolucaoCompraItemImagemVOs(obj.getNomeImagem());
	}

	public void removerDevolucaoCompraItemImagem() throws Exception {
		DevolucaoCompraItemImagemVO obj = (DevolucaoCompraItemImagemVO) context().getExternalContext().getRequestMap().get("imagemsItens");
		getDevolucaoCompraItemVO().excluirObjDevolucaoCompraItemImagemVOs(obj.getNomeImagem());
	}

	public List<SelectItem> getTipoConsultaComboCompra() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomeFornecedor", "Fornecedor"));
		itens.add(new SelectItem("data", "Data Compra"));
		return itens;
	}

	public void consultarCompra() {
		if (!getValorConsultaCompra().equals("") || (Uteis.isAtributoPreenchido(getValorConsultaCompraData()) && getCampoConsultaCompra().equals("data"))) {
			try {

				List<CompraVO> objs = new ArrayList<CompraVO>(0);
				if (getCampoConsultaCompra().equals("codigo")) {
					int valorInt = Integer.parseInt(getValorConsultaCompra());
					objs = getFacadeFactory().getCompraFacade().consultarPorCodigo(new Integer(valorInt), "", "", "PA", "FI", getDevolucaoCompraVO().getUnidadeEnsino().getCodigo().intValue(), true, 0,0 ,false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaCompra().equals("nomeFornecedor")) {
					objs = getFacadeFactory().getCompraFacade().consultarPorNomeFornecedor(getValorConsultaCompra(), "", "", "PA", "FI", getDevolucaoCompraVO().getUnidadeEnsino().getCodigo().intValue(), true, 0,0 ,false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaCompra().equals("data")) {
					Date valorData = getValorConsultaCompraData();
					objs = getFacadeFactory().getCompraFacade().consultarPorDataCompra(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "", "", "PA", "FI", getDevolucaoCompraVO().getUnidadeEnsino().getCodigo().intValue(), true, 0,0 , false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaCompra(objs);
				setMensagemID("msg_dados_consultados");

			} catch (Exception e) {
				setListaConsultaCompra(new ArrayList<CompraVO>(0));
				if (e.getMessage() != null && e.getMessage().contains("For input string") && getCampoConsultaCompra().equals("codigo")) {
					setMensagemDetalhada("msg_erro", "Por favor, informe somente números.");
				} else {
					setMensagemDetalhada("msg_erro", e.getMessage());
				}
			}
		} else {
			setMensagemID("msg_dados_parametroConsulta");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>DevolucaoCompraItem</code> para o objeto <code>devolucaoCompraVO</code>
	 * da classe <code>DevolucaoCompra</code>
	 */
	public void adicionarDevolucaoCompraItem() throws Exception {
		try {
			if (!getDevolucaoCompraVO().getCodigo().equals(0)) {
				devolucaoCompraItemVO.setDevolucaoCompra(getDevolucaoCompraVO().getCodigo());
			}
			getDevolucaoCompraVO().adicionarObjDevolucaoCompraItemVOs(getDevolucaoCompraItemVO());
			this.setDevolucaoCompraItemVO(new DevolucaoCompraItemVO());
			setMensagemID("msg_dados_adicionados");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
		calcularValorCredito();
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>DevolucaoCompraItem</code> para edição pelo usuário.
	 */
	public void editarDevolucaoCompraItem() throws Exception {
		DevolucaoCompraItemVO obj = (DevolucaoCompraItemVO) context().getExternalContext().getRequestMap().get("devolucaoCompraItemItens");
		setDevolucaoCompraItemVO(obj);

	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>DevolucaoCompraItem</code> do objeto <code>devolucaoCompraVO</code> da
	 * classe <code>DevolucaoCompra</code>
	 */
	public void removerDevolucaoCompraItem() throws Exception {
		DevolucaoCompraItemVO obj = (DevolucaoCompraItemVO) context().getExternalContext().getRequestMap().get("devolucaoCompraItemItens");
		getDevolucaoCompraVO().excluirObjDevolucaoCompraItemVOs(obj.getCompraItem().getCodigo());
		setMensagemID("msg_dados_excluidos");
		calcularValorCredito();
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

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>GrupoTrabalho</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			if (this.getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(this.getUnidadeEnsinoLogado().getCodigo(), this.getUnidadeEnsinoLogado().getNome()));
				getDevolucaoCompraVO().getUnidadeEnsino().setCodigo(this.getUnidadeEnsinoLogado().getCodigo());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>GrupoTrabalho</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>GrupoTrabalho</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela
	 * para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista
	 * (<code>List</code>) utilizada para definir os valores a serem apresentados no
	 * ComboBox correspondente
	 */
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaFormaReceber();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("fornecedor", "Fornecedor"));
		itens.add(new SelectItem("codigo", "Número Devolução"));
		itens.add(new SelectItem("codigoCompra", "Número Compra"));
		return itens;
	}

	/**
	 * Verifica se o campo selecionado para consulta e do tipo data.
	 *
	 * @return
	 */
	public boolean isCampoData() {
		return getControleConsulta().getCampoConsulta().equals("data");
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<SelectItem>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("devolucaoCompraCons.xhtml");
	}

	/**
	 * Operação que calcula o valor do Crédito aquirido com a devolução de itens de
	 * compra.
	 */
	public void calcularValorCredito() {
		try {
			getFacadeFactory().getDevolucaoCompraFacade().calcularValorCredito(getDevolucaoCompraVO(), getCreditoFornecedorVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação que retira do Valor de Credito de acordo com as alterções das Contas
	 * a Pagar.
	 */
	public void calcularValorCreditoRestante() {
		try {
			calcularValorCredito();
			sugerirValorParcelas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação que sugere o valor das contas a pagar após as alterações nas contas.
	 * Está operação mantém o valor total de credito que no caso na tela de compra e
	 * o valor do Preço total que varia de acordo com os itens selecionados para a
	 * devolução, com isso o valorCredito aumenta. A sugestão de parcelas ocorre a
	 * partir do valorCredito que sempre e dividido pelo numero de parcelas, caso o
	 * usuário opte por não ter nenhuma parcela, então o valorCredito passa a ser
	 * uma única parcela.
	 */
	public void sugerirValorParcelas() {
		try {
			setAbrirPainelContaPagar(false);
//			if (!Uteis.isAtributoPreenchido(getDevolucaoCompraVO().getConsultaContaPagar())) {
//				setMensagemDetalhada("msg_erro", "Não foi possível localizar a Conta a Pagar para a realização do abatimento.");
//				return;
//			}
//			setSugestaoValorParcelas(getFacadeFactory().getDevolucaoCompraFacade().sugerirValorParcelas(getDevolucaoCompraVO(), getCreditoFornecedorVO()));
//			setAbrirPainelContaPagar(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSugestaoValorParcelas("");
			getDevolucaoCompraVO().getConsultaContaPagar().clear();
			setAbrirPainelContaPagar(false);
		}
	}

	public String getAbrirPanelContaPagar() {
		if (getAbrirPainelContaPagar())
			return "RichFaces.$('panelReceber').hide(), RichFaces.$('panelContaPagar').show()";
		return "";
	}

	public boolean isValidarExcluirContaPagar() throws Exception {
		Double valorParcelaAbater = 0.0;
		Iterator<ContaPagarVO> i = getDevolucaoCompraVO().getConsultaContaPagar().iterator();
		while (i.hasNext()) {
			ContaPagarVO contaPagar = (ContaPagarVO) i.next();
			if (contaPagar.isExcluirContaPagar() || contaPagar.getSituacao().equals("PA")) {
				valorParcelaAbater += contaPagar.getValor();
			}
		}
		ContaPagarVO conta = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
		return (valorParcelaAbater + conta.getValor() > getDevolucaoCompraVO().getValorTotalCredito()) && !conta.isExcluirContaPagar() && !conta.getSituacao().equals("PA");
	}

	public DevolucaoCompraItemVO getDevolucaoCompraItemVO() {
		if (devolucaoCompraItemVO == null) {
			devolucaoCompraItemVO = new DevolucaoCompraItemVO();
		}
		return devolucaoCompraItemVO;
	}

	public void setDevolucaoCompraItemVO(DevolucaoCompraItemVO devolucaoCompraItemVO) {
		this.devolucaoCompraItemVO = devolucaoCompraItemVO;
	}

	public String getCampoConsultaCompra() {
		this.campoConsultaCompra = Optional.ofNullable(this.campoConsultaCompra).orElse("");
		return campoConsultaCompra;
	}

	public void setCampoConsultaCompra(String campoConsultaCompra) {
		this.campoConsultaCompra = campoConsultaCompra;
	}

	public List<CompraVO> getListaConsultaCompra() {
		if (listaConsultaCompra == null) {
			listaConsultaCompra = new ArrayList<CompraVO>(0);
		}
		return listaConsultaCompra;
	}

	public void setListaConsultaCompra(List<CompraVO> listaConsultaCompra) {
		this.listaConsultaCompra = listaConsultaCompra;
	}

	public Boolean getNovoObj() {
		return novoObj;
	}

	public void setNovoObj(Boolean novoObj) {
		this.novoObj = novoObj;
	}

	public String getValorConsultaCompra() {
		return valorConsultaCompra;
	}

	public void setValorConsultaCompra(String valorConsultaCompra) {
		this.valorConsultaCompra = valorConsultaCompra;
	}

	public DevolucaoCompraVO getDevolucaoCompraVO() {
		if (devolucaoCompraVO == null) {
			devolucaoCompraVO = new DevolucaoCompraVO();
		}
		return devolucaoCompraVO;
	}

	public void setDevolucaoCompraVO(DevolucaoCompraVO devolucaoCompraVO) {
		this.devolucaoCompraVO = devolucaoCompraVO;
	}

	public DevolucaoCompraItemVO getDevolucaoCompraItemVOTemp() {
		if (devolucaoCompraItemVOTemp == null) {
			devolucaoCompraItemVOTemp = new DevolucaoCompraItemVO();
		}
		return devolucaoCompraItemVOTemp;
	}

	public void setDevolucaoCompraItemVOTemp(DevolucaoCompraItemVO devolucaoCompraItemVOTemp) {
		this.devolucaoCompraItemVOTemp = devolucaoCompraItemVOTemp;
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

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		devolucaoCompraVO = null;
		campoConsultaCompra = null;
		valorConsultaCompra = null;
		Uteis.liberarListaMemoria(getListaConsultaCompra());
		Uteis.liberarListaMemoria(getCompraItemVO());
		novoObj = null;
		devolucaoCompraItemVO = null;
		devolucaoCompraItemVOTemp = null;
	}
	
	public void realizarDownload() {
		DevolucaoCompraItemVO obj = (DevolucaoCompraItemVO) context().getExternalContext().getRequestMap().get("devolucaoCompraItemItens");
		context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivo());
	}

	/**
	 * @return the contaPagarVO
	 */
	public ContaPagarVO getContaPagarVO() {
		if (contaPagarVO == null) {
			contaPagarVO = new ContaPagarVO();
		}
		return contaPagarVO;
	}

	/**
	 * @param contaPagarVO
	 *            the contaPagarVO to set
	 */
	public void setContaPagarVO(ContaPagarVO contaPagarVO) {
		this.contaPagarVO = contaPagarVO;
	}

	/**
	 * @return the listaConsultaContaPagar
	 */
	public List<ContaPagarVO> getListaConsultaContaPagar() {
		if (listaConsultaContaPagar == null) {
			listaConsultaContaPagar = new ArrayList<ContaPagarVO>(0);
		}
		return listaConsultaContaPagar;
	}

	/**
	 * @param listaConsultaContaPagar
	 *            the listaConsultaContaPagar to set
	 */
	public void setListaConsultaContaPagar(List<ContaPagarVO> listaConsultaContaPagar) {
		this.listaConsultaContaPagar = listaConsultaContaPagar;
	}

	/**
	 * @return the listaFormaReceber
	 */
	public List<SelectItem> getListaFormaReceber() {
		if (listaFormaReceber == null) {
			listaFormaReceber = new ArrayList<SelectItem>(0);
		}
		return listaFormaReceber;
	}

	/**
	 * @param listaFormaReceber
	 *            the listaFormaReceber to set
	 */
	public void setListaFormaReceber(List<SelectItem> listaFormaReceber) {
		this.listaFormaReceber = listaFormaReceber;
	}

	/**
	 * @return the creditoFornecedorVO
	 */
	public CreditoFornecedorVO getCreditoFornecedorVO() {
		if (creditoFornecedorVO == null) {
			creditoFornecedorVO = new CreditoFornecedorVO();
		}
		return creditoFornecedorVO;
	}

	/**
	 * @param creditoFornecedorVO
	 *            the creditoFornecedorVO to set
	 */
	public void setCreditoFornecedorVO(CreditoFornecedorVO creditoFornecedorVO) {
		this.creditoFornecedorVO = creditoFornecedorVO;
	}

	/**
	 * @return the sugestaoValorParcelas
	 */
	public String getSugestaoValorParcelas() {
		return sugestaoValorParcelas;
	}

	/**
	 * @param sugestaoValorParcelas
	 *            the sugestaoValorParcelas to set
	 */
	public void setSugestaoValorParcelas(String sugestaoValorParcelas) {
		this.sugestaoValorParcelas = sugestaoValorParcelas;
	}

	public Boolean getAbrirPainelContaPagar() {
		if (abrirPainelContaPagar == null) {
			abrirPainelContaPagar = false;
		}
		return abrirPainelContaPagar;
	}

	public void setAbrirPainelContaPagar(Boolean abrirPainelContaPagar) {
		this.abrirPainelContaPagar = abrirPainelContaPagar;
	}

	public List<CompraItemVO> getCompraItemVO() {
		if (compraItemVO == null) {
			compraItemVO = new ArrayList<CompraItemVO>(0);
		}
		return compraItemVO;
	}

	public void setCompraItemVO(List<CompraItemVO> compraItemVO) {
		this.compraItemVO = compraItemVO;
	}

	public Date getValorConsultaCompraData() {
		return valorConsultaCompraData;
	}

	public void limparCampoConsulta() {
		this.setValorConsultaCompra("");
		this.setValorConsultaCompraData(null);
	}

	public boolean isConsultaComboCompraData() {
		return this.getCampoConsultaCompra().toLowerCase().contains("data");
	}

	public void setValorConsultaCompraData(Date valorConsultaCompraData) {
		this.valorConsultaCompraData = valorConsultaCompraData;
	}

	public void limparListaConsultaCompra() {
		setListaConsultaCompra(new ArrayList<>(0));
	}
}
