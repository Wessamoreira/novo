package controle.compras;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;

@Controller("RecebimentoCompraControle")
@Scope("viewScope")
@Lazy
public class RecebimentoCompraControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5492911099778773576L;
	private RecebimentoCompraVO recebimentoCompraVO;
	private RecebimentoCompraItemVO recebimentoCompraItemVO;
	protected List listaSelectItemUnidadeEnsino;
	private String tabAtiva = "";
	private RecebimentoCompraItemVO recebimentoCompraItemExcluir;

	public String getTabAtiva() {
		return tabAtiva;
	}

	public void setTabAtiva(String tabAtiva) {
		this.tabAtiva = tabAtiva;
	}

	public RecebimentoCompraControle()  {
		setControleConsulta(new ControleConsulta());
		this.getControleConsulta().setSituacao("PR");
		verificarRecebimentoCompraApartitDaCompra();
		this.setTabAtiva("form:segundatab");
		inicializarMensagemVazia();
	}

	public void verificarRecebimentoCompraApartitDaCompra() {
		try {
			RecebimentoCompraControle compra = (RecebimentoCompraControle) context().getExternalContext().getSessionMap().get("RecebimentoCompraControle");
			if (compra != null) {
				Integer recebimento = compra.getRecebimentoCompraVO().getCodigo();
				setRecebimentoCompraVO(getFacadeFactory().getRecebimentoCompraFacade().consultarPorChavePrimaria(recebimento, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				inicializarListasSelectItemTodosComboBox();
				apresentarPrimeiro = new UICommand();
				apresentarAnterior = new UICommand();
				apresentarPosterior = new UICommand();
				apresentarUltimo = new UICommand();
			}
		} catch (Exception e) {
			setMensagemDetalhada("Não foi possivel receber esta compra.");
		}
	}

	public void inicializarResponsavel() {
		try {
			recebimentoCompraVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>RecebimentoCompra</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Novo Recebimento Compra", "Novo");
		removerObjetoMemoria(this);
		setRecebimentoCompraVO(new RecebimentoCompraVO());
		inicializarListasSelectItemTodosComboBox();
		setRecebimentoCompraItemVO(new RecebimentoCompraItemVO());
		inicializarResponsavel();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>RecebimentoCompra</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Inicializando Editar Recebimento Compra", "Editando");
		RecebimentoCompraVO obj = (RecebimentoCompraVO) context().getExternalContext().getRequestMap().get("recebimentoCompraItens");
		obj = montarDadosRecebimentoParaEdicao(obj);
		obj.setNovoObj(Boolean.FALSE);
		setRecebimentoCompraVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setRecebimentoCompraItemVO(new RecebimentoCompraItemVO());
		montarListaSelectItemUnidadeEnsino();
		registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Finalizando Editar Recebimento Compra", "Editando");
		this.setTabAtiva("form:primeiratab");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraForm.xhtml");
	}

	/**
	 * Metodo responsavel por carregar todos os dados dos recembimento de compras.
	 *
	 * @param obj
	 * @return
	 */
	private RecebimentoCompraVO montarDadosRecebimentoParaEdicao(RecebimentoCompraVO obj) {
		try {
			return getFacadeFactory().getRecebimentoCompraFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new RecebimentoCompraVO();
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>RecebimentoCompra</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto
	 * não é gravado, sendo re-apresentado para o usuário juntamente com uma
	 * mensagem de erro.
	 */
	public void gravar() {
		try {
			if (recebimentoCompraVO.getSituacao().equals("PR")) {
				if (recebimentoCompraVO.isNovoObj().booleanValue()) {
					registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Inicializando Incluir Recebimento Compra", "Incluindo");
					getFacadeFactory().getRecebimentoCompraFacade().persistir(recebimentoCompraVO, true, getUsuarioLogado());
					registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Finalizando Incluir Recebimento Compra", "Incluindo");
				} else {
					recebimentoCompraVO.setSituacao("EF");
					inicializarResponsavel();
					registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Inicializando Alterar Recebimento Compra", "Alterando");
					getFacadeFactory().getRecebimentoCompraFacade().persistir(recebimentoCompraVO, true, getUsuarioLogado());
					registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Finalizando Alterar Recebimento Compra", "Alterando");
				}
			} else {
				throw new Exception("Não é possivel Alterar um Recebimento já efetivado");
			}

			setMensagemID("msg_dados_gravados");

		} catch (Exception e) {
			recebimentoCompraVO.setSituacao("PR");
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * RecebimentoCompraCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Inicializando Consultar Recebimento Compra", "Consultando");
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRecebimentoCompraFacade().consultarPorCodigo(new Integer(valorInt), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoCompra")) {
				int valorInt = 0;
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Uteis.getValorInteiro(getControleConsulta().getValorConsulta());
				}
				objs = getFacadeFactory().getRecebimentoCompraFacade().consultarPorCodigoCompra(new Integer(valorInt), this.getUnidadeEnsinoLogado().getCodigo(), getControleConsulta().getSituacao(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("numeroNotaFiscalEntrada")) {
				int valorInt = 0;
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Uteis.getValorInteiro(getControleConsulta().getValorConsulta());
				}
				objs = getFacadeFactory().getRecebimentoCompraFacade().consultarPorNumeroNotaFiscal(new Integer(valorInt), this.getUnidadeEnsinoLogado().getCodigo(), getControleConsulta().getSituacao(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("fornecedor")) {
				objs = getFacadeFactory().getRecebimentoCompraFacade().consultarPorNomeFornecedor(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), getControleConsulta().getSituacao(), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Finalizando Consultar Recebimento Compra", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>RecebimentoCompraVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Inicializando Excluir Recebimento Compra", "Excluindo");
			getFacadeFactory().getRecebimentoCompraFacade().excluir(recebimentoCompraVO, true, getUsuarioLogado());
			setRecebimentoCompraVO(new RecebimentoCompraVO());
			setRecebimentoCompraItemVO(new RecebimentoCompraItemVO());
			inicializarResponsavel();
			registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Finalizando Excluir Recebimento Compra", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraForm.xhtml");
		}
	}
	
	public void estornar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Inicializando estorno Recebimento Compra", "Estornar");
			setRecebimentoCompraVO(getFacadeFactory().getRecebimentoCompraFacade().estornar(getRecebimentoCompraVO(), getUsuarioLogado()));
			registrarAtividadeUsuario(getUsuarioLogado(), "RecebimentoCompraControle", "Finalizando estorno Recebimento Compra", "Estornar");
			setMensagemID("msg_dados_estornados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void adicionarRecebimentoCompraItem() {
		try {
			recebimentoCompraItemVO.setRecebimentoCompraVO(getRecebimentoCompraVO());
			getRecebimentoCompraVO().adicionarObjRecebimentoCompraItemVOs(getRecebimentoCompraItemVO());
			this.setRecebimentoCompraItemVO(new RecebimentoCompraItemVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	public void removerRecebimentoCompraItem() {
		try {			
			getRecebimentoCompraVO().excluirObjRecebimentoCompraItemVOs(getRecebimentoCompraItemExcluir().getCompraItem().getCodigo());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		

	}

	public void verPecaRecebimentoCompraItem()  {
		try {
			RecebimentoCompraItemVO obj = (RecebimentoCompraItemVO) context().getExternalContext().getRequestMap().get("recebimentoCompraItemItens");
			setRecebimentoCompraItemVO(obj);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public void reCalcularItemCotado() {
		getRecebimentoCompraVO().atualizarValorTotal();
	}
	
	public boolean isSituacaoFiltroEfetifada(){
		return Uteis.isAtributoPreenchido(getControleConsulta().getSituacao()) && getControleConsulta().getSituacao().equals("EF"); 
	}

	public List getListaSelectItemSituacaoRecebimentoCompra() throws Exception {
		List objs = new ArrayList(0);
		Hashtable listaTipoSacado = (Hashtable) Dominios.getSituacaoRecebimentoCompra();
		Enumeration keys = listaTipoSacado.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) listaTipoSacado.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
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
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if (this.getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				getListaSelectItemUnidadeEnsino().add(new SelectItem(this.getUnidadeEnsinoLogado().getCodigo(), this.getUnidadeEnsinoLogado().getNome()));
				getRecebimentoCompraVO().getUnidadeEnsino().setCodigo(this.getUnidadeEnsinoLogado().getCodigo());
				return;
			}
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
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
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores
	 * (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("fornecedor", "Fornecedor"));
		itens.add(new SelectItem("numeroNotaFiscalEntrada", "Nº Nota Fiscal Entrada"));
		itens.add(new SelectItem("codigoCompra", "Compra"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Verifica se o campo selecionado para consulta é do tipo data.
	 *
	 * @return boolean
	 */
	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("recebimentoCompraCons.xhtml");
	}

	public RecebimentoCompraItemVO getRecebimentoCompraItemVO() {
		return recebimentoCompraItemVO;
	}

	public void setRecebimentoCompraItemVO(RecebimentoCompraItemVO recebimentoCompraItemVO) {
		this.recebimentoCompraItemVO = recebimentoCompraItemVO;
	}

	public RecebimentoCompraVO getRecebimentoCompraVO() {
		if (recebimentoCompraVO == null) {
			recebimentoCompraVO = new RecebimentoCompraVO();
		}
		return recebimentoCompraVO;
	}

	public void setRecebimentoCompraVO(RecebimentoCompraVO recebimentoCompraVO) {
		this.recebimentoCompraVO = recebimentoCompraVO;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		recebimentoCompraVO = null;
		recebimentoCompraItemVO = null;

	}	
	
	public RecebimentoCompraItemVO getRecebimentoCompraItemExcluir() {
		if(recebimentoCompraItemExcluir == null) {
			recebimentoCompraItemExcluir = new RecebimentoCompraItemVO(); 
		}
		return recebimentoCompraItemExcluir;
	}

	public void setRecebimentoCompraItemExcluir(RecebimentoCompraItemVO recebimentoCompraItemExcluir) {
		this.recebimentoCompraItemExcluir = recebimentoCompraItemExcluir;
	}

	public void setarRecebimentoCompraItemExcluir() {
		setRecebimentoCompraItemExcluir((RecebimentoCompraItemVO) context().getExternalContext().getRequestMap().get("recebimentoCompraItemItens"));
	}	
	
}
