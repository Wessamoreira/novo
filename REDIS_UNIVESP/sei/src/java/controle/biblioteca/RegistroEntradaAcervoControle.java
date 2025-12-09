package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas registroEntradaAcervoForm.jsp
 * registroEntradaAcervoCons.jsp) com as funcionalidades da classe <code>RegistroEntradaAcervo</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see RegistroEntradaAcervo
 * @see RegistroEntradaAcervoVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemRegistroEntradaAcervoVO;
import negocio.comuns.biblioteca.RegistroEntradaAcervoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.comuns.utilitarias.dominios.TipoExemplar;

@Controller("RegistroEntradaAcervoControle")
@Scope("viewScope")
@Lazy
public class RegistroEntradaAcervoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -3344477950563596418L;
	private RegistroEntradaAcervoVO registroEntradaAcervoVO;
	private List<SelectItem> listaSelectItemBiblioteca;
	private ItemRegistroEntradaAcervoVO itemRegistroEntradaAcervoVO;	
	private List<SelectItem> listaSelectItemTipoEntradaItemRegistroEntradaAcervo;
	private ExemplarVO exemplarConsulta;

	public RegistroEntradaAcervoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>RegistroEntradaAcervo</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		try {
			setRegistroEntradaAcervoVO(new RegistroEntradaAcervoVO());
			inicializarListasSelectItemTodosComboBox();
			setItemRegistroEntradaAcervoVO(new ItemRegistroEntradaAcervoVO());
			getFacadeFactory().getRegistroEntradaAcervoFacade().inicializarDadosRegistroEntradaAcervoNovo(getRegistroEntradaAcervoVO(), getUsuarioLogado());
			setMensagemID("msg_entre_dados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>RegistroEntradaAcervo</code> para alteração. O objeto desta classe
	 * é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			RegistroEntradaAcervoVO obj = (RegistroEntradaAcervoVO) context().getExternalContext().getRequestMap().get("registroEntradaAcervoItens");
			obj.setNovoObj(Boolean.FALSE);
			obj.setItemRegistroEntradaAcervoVOs(getFacadeFactory().getItemRegistroEntradaAcervoFacade().consultarItemRegistroEntradaAcervos(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setRegistroEntradaAcervoVO(obj);
			inicializarListasSelectItemTodosComboBox();
			setItemRegistroEntradaAcervoVO(new ItemRegistroEntradaAcervoVO());
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>RegistroEntradaAcervo</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (registroEntradaAcervoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getRegistroEntradaAcervoFacade().incluir(registroEntradaAcervoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getRegistroEntradaAcervoFacade().alterar(registroEntradaAcervoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * RegistroEntradaAcervoCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoEntrada")) {
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorTipoEntrada(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoBarra")) {
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorCodigoBarra(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("catalogo")) {
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorCatalogo(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeBiblioteca")) {
				objs = getFacadeFactory().getRegistroEntradaAcervoFacade().consultarPorNomeBiblioteca(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoCons.xhtml");
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>RegistroEntradaAcervoVO</code> Após a exclusão ela automaticamente
	 * aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getRegistroEntradaAcervoFacade().excluir(registroEntradaAcervoVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoForm.xhtml");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>ItemRegistroEntradaAcervo</code> para o objeto
	 * <code>registroEntradaAcervoVO</code> da classe
	 * <code>RegistroEntradaAcervo</code>
	 */
	public void adicionarItemRegistroEntradaAcervo(){
		try {
			if (!getRegistroEntradaAcervoVO().getCodigo().equals(0)) {
				getItemRegistroEntradaAcervoVO().setRegistroEntradaAcervo(getRegistroEntradaAcervoVO().getCodigo());
			}
			if (getItemRegistroEntradaAcervoVO().getExemplar().getCodigo().intValue() != 0) {
				getFacadeFactory().getExemplarFacade().carregarDados(getItemRegistroEntradaAcervoVO().getExemplar(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			} else {
				throw new ConsistirException("Não foi encontrado exemplar para o código informado!");
			}
			getRegistroEntradaAcervoVO().adicionarObjItemRegistroEntradaAcervoVOs(getItemRegistroEntradaAcervoVO());
			this.setItemRegistroEntradaAcervoVO(new ItemRegistroEntradaAcervoVO());
			setMensagemID("msg_dados_adicionados");		
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());			
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>ItemRegistroEntradaAcervo</code> para edição pelo usuário.
	 */
	public void editarItemRegistroEntradaAcervo() throws Exception {
		ItemRegistroEntradaAcervoVO obj = (ItemRegistroEntradaAcervoVO) context().getExternalContext().getRequestMap().get("itemRegistroEntradaAcervo");
		setItemRegistroEntradaAcervoVO(obj);		
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ItemRegistroEntradaAcervo</code> do objeto
	 * <code>registroEntradaAcervoVO</code> da classe <code>RegistroEntradaAcervo</code>
	 */
	public void removerItemRegistroEntradaAcervo() throws Exception {
		ItemRegistroEntradaAcervoVO obj = (ItemRegistroEntradaAcervoVO) context().getExternalContext().getRequestMap().get("itemRegistroEntradaAcervoItens");
		getRegistroEntradaAcervoVO().excluirObjItemRegistroEntradaAcervoVOs(obj.getExemplar().getCodigo());
		setMensagemID("msg_dados_excluidos");		
	}
	
	
	
	public void consultarExemplarPeloCodigoBarras() {
		try {
			if (getItemRegistroEntradaAcervoVO().getExemplar().getCodigoBarra() != null && !getItemRegistroEntradaAcervoVO().getExemplar().getCodigoBarra().trim().isEmpty()) {				
				getItemRegistroEntradaAcervoVO().setExemplar(getFacadeFactory().getExemplarFacade().consultarPorCodigoBarrasUnicoBiblioteca(getItemRegistroEntradaAcervoVO().getExemplar().getCodigoBarra(), getRegistroEntradaAcervoVO().getBiblioteca().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				adicionarItemRegistroEntradaAcervo();				
			}
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {			
			
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Exemplar</code> por meio dos parametros informados no richmodal.
	 * Esta rotina é utilizada fundamentalmente por requisições Ajax, que
	 * realizam busca pelos parâmentros informados no richModal montando
	 * automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarExemplar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(5);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getExemplarFacade().consultar("('BA', 'EX', 'CE', 'DE', 'ER', 'IT')", null, getExemplarConsulta().getCatalogo().getTitulo(), getRegistroEntradaAcervoVO().getBiblioteca().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, null, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultarTotalRegistro("('BA', 'EX', 'CE', 'DE', 'ER', 'IT')", null, getExemplarConsulta().getCatalogo().getTitulo(), getRegistroEntradaAcervoVO().getBiblioteca().getCodigo(), getUnidadeEnsinoLogado().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void scrollListenerExemplar(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarExemplar();
	}

	public void selecionarExemplar(){
		ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplarItens");		
		this.getItemRegistroEntradaAcervoVO().setExemplar(obj);		
		adicionarItemRegistroEntradaAcervo();		
	} 

	public void limparCampoExemplar() {
		this.getItemRegistroEntradaAcervoVO().setExemplar(new ExemplarVO());
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoEntrada</code>
	 */
	public void montarListaSelectItemTipoEntradaItemRegistroEntradaAcervo() throws Exception {
		setListaSelectItemTipoEntradaItemRegistroEntradaAcervo(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoEntradaAcervo.class, false));
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoEntrada</code>
	 */
	// public List getListaSelectItemTipoEntradaRegistroEntradaAcervo() throws
	// Exception {
	// List objs = new ArrayList(0);
	// objs.add(new SelectItem("", ""));
	// Hashtable tipoEntradaAcervos = (Hashtable)
	// Dominios.getTipoEntradaAcervo();
	// Enumeration keys = tipoEntradaAcervos.keys();
	// while (keys.hasMoreElements()) {
	// String value = (String) keys.nextElement();
	// String label = (String) tipoEntradaAcervos.get(value);
	// objs.add(new SelectItem(value, label));
	// }
	// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
	// Collections.sort((List) objs, ordenador);
	// return objs;
	// }

	/**
	 * Métodos que montam a comboBox de Bibliotecas.
	 */

	public void montarListaSelectItemBiblioteca() throws Exception {
		try {
			List<BibliotecaVO> resultadoConsulta = consultarBibliotecaPorNome("");
			setListaSelectItemBiblioteca(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private List<BibliotecaVO> consultarBibliotecaPorNome(String nomePrm) throws Exception {
		List<BibliotecaVO> lista = getFacadeFactory().getBibliotecaFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 * 
	 * @throws Exception
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemBiblioteca();
		montarListaSelectItemTipoEntradaItemRegistroEntradaAcervo();
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo == null){
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("codigoBarra", "Tombo"));
			tipoConsultaCombo.add(new SelectItem("catalogo", "Catálogo"));
			tipoConsultaCombo.add(new SelectItem("nomeBiblioteca", "Biblioteca"));
			tipoConsultaCombo.add(new SelectItem("data", "Data"));
			tipoConsultaCombo.add(new SelectItem("nomeUsuario", "Funcionário"));
			tipoConsultaCombo.add(new SelectItem("codigo", "Código"));
			
		}
		return tipoConsultaCombo;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaAcervoCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		registroEntradaAcervoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemBiblioteca);
		itemRegistroEntradaAcervoVO = null;
	}
	
	public ItemRegistroEntradaAcervoVO getItemRegistroEntradaAcervoVO() {
		if (itemRegistroEntradaAcervoVO == null) {
			itemRegistroEntradaAcervoVO = new ItemRegistroEntradaAcervoVO();
		}
		return itemRegistroEntradaAcervoVO;
	}

	public void setItemRegistroEntradaAcervoVO(ItemRegistroEntradaAcervoVO itemRegistroEntradaAcervoVO) {
		this.itemRegistroEntradaAcervoVO = itemRegistroEntradaAcervoVO;
	}

	public List<SelectItem> getListaSelectItemBiblioteca() {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemBiblioteca);
	}

	public void setListaSelectItemBiblioteca(List<SelectItem> listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

	public RegistroEntradaAcervoVO getRegistroEntradaAcervoVO() {
		if (registroEntradaAcervoVO == null) {
			registroEntradaAcervoVO = new RegistroEntradaAcervoVO();
		}
		return registroEntradaAcervoVO;
	}

	public void setRegistroEntradaAcervoVO(RegistroEntradaAcervoVO registroEntradaAcervoVO) {
		this.registroEntradaAcervoVO = registroEntradaAcervoVO;
	}

	public void setListaSelectItemTipoEntradaItemRegistroEntradaAcervo(List<SelectItem> listaSelectItemTipoEntradaItemRegistroEntradaAcervo) {
		this.listaSelectItemTipoEntradaItemRegistroEntradaAcervo = listaSelectItemTipoEntradaItemRegistroEntradaAcervo;
	}

	public List<SelectItem> getListaSelectItemTipoEntradaItemRegistroEntradaAcervo() {
		if (listaSelectItemTipoEntradaItemRegistroEntradaAcervo == null) {
			listaSelectItemTipoEntradaItemRegistroEntradaAcervo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoEntradaItemRegistroEntradaAcervo;
	}
	
	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}
	
	public ExemplarVO getExemplarConsulta() {
		if(exemplarConsulta == null){
			exemplarConsulta = new ExemplarVO();
		}
		return exemplarConsulta;
	}

	public void setExemplarConsulta(ExemplarVO exemplarConsulta) {
		this.exemplarConsulta = exemplarConsulta;
	}
	

	public List<SelectItem> getListaSelectItemTipoExemplar() {
		return TipoExemplar.getListaSelectItemTipoExemplar();
	}
}