package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas exemplarForm.jsp
 * exemplarCons.jsp) com as funcionalidades da classe <code>Exemplar</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Exemplar
 * @see ExemplarVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.HistoricoExemplarVO;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.EstadoHistoricoExemplar;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.comuns.utilitarias.dominios.TipoExemplar;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ExemplarControle")
@Scope("request")
@Lazy
public class ExemplarControle extends SuperControle implements Serializable {

	private ExemplarVO exemplarVO;
	protected List listaSelectItemBiblioteca;
	private List listaSelectItemSecao;
	private String campoConsultarCatalogo;
	private String valorConsultarCatalogo;
	private List listaConsultarCatalogo;
	private HistoricoExemplarVO historicoExemplarVO;
	private List listaSelectItemEstadoExemplar;
	private int numeroExemplaresAGerar;
	private List<ExemplarVO> listaExemplaresGerados;
	protected List listaSelectItemTipoExemplar;
	protected List listaSelectCatalogo;
	private Integer tipoEtiqueta;
	private Boolean exibirCatalogo;
	private String campoConsultarPeriodico;
	private String valorConsultarPeriodico;
	private List<CatalogoVO> listaConsultarPeriodico;
	
	public ExemplarControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		getControleConsulta().setCampoConsulta("tipoExemplar");
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Exemplar</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		try {
			setExemplarVO(new ExemplarVO());
			setListaExemplaresGerados(new ArrayList<ExemplarVO>(0));
			setNumeroExemplaresAGerar(0);
			inicializarListasSelectItemTodosComboBox();
			setHistoricoExemplarVO(new HistoricoExemplarVO());		
			setMensagemID("msg_entre_dados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Exemplar</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			ExemplarVO obj = (ExemplarVO) context().getExternalContext().getRequestMap().get("exemplar");
			setExemplarVO(obj);
			getFacadeFactory().getExemplarFacade().carregarDados(obj, getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			inicializarListasSelectItemTodosComboBox();
			setHistoricoExemplarVO(new HistoricoExemplarVO());
			setMensagemID("msg_dados_editar");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Exemplar</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		// try {
		// if (exemplarVO.isNovoObj().booleanValue()) {
		// getFacadeFactory().getExemplarFacade().incluir(exemplarVO);
		// } else {
		// getFacadeFactory().getExemplarFacade().alterar(exemplarVO);
		// }
		// setMensagemID("msg_dados_gravados");
		// return "editar";
		// } catch (Exception e) {
		// setMensagemDetalhada("msg_erro", e.getMessage());
		// return "editar";
		// }
		try {
			if (exemplarVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getExemplarFacade().incluirListaExemplares(getListaExemplaresGerados(), getUsuarioLogado());
			} else {
				if (getExemplarVO().getTipoExemplar().equals(TipoExemplar.PERIODICO.getValor())) {
					getFacadeFactory().getExemplarFacade().alterar(exemplarVO, true, getUsuarioLogado());
				} else {
					getFacadeFactory().getExemplarFacade().alterar(exemplarVO, false, getUsuarioLogado());
				}
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ExemplarCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), null, null, true, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultaTotalDeRegistroRapidaPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeBiblioteca")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorCodigoBiblioteca(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultaTotalDeRegistroRapidaPorCodigoBiblioteca(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("catalogo")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorTituloCatalogo(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultaTotalDeRegistroRapidaPorTituloCatalogo(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoBarra")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorCodigoBarra(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultaTotalDeRegistroRapidaPorCodigoBarra(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("situacaoAtual")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorSituacaoAtual(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultaTotalDeRegistroRapidaPorSituacaoAtual(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoExemplar")) {
				objs = getFacadeFactory().getExemplarFacade().consultaRapidaPorTipoExemplar(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), null, null, true, getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getExemplarFacade().consultaTotalDeRegistroRapidaPorTipoExemplar(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			}
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}
	
    public List getTipoConsultarComboPeriodico() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }
	
	public void selecionarPeriodico() throws Exception {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("periodico");
		
		if (getMensagemDetalhada().equals("")) {
		   getExemplarVO().setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, getUsuarioLogado()));
		}
		Uteis.liberarListaMemoria(this.getListaConsultarPeriodico());
		this.setValorConsultarPeriodico(null);
		this.setCampoConsultarPeriodico(null);
	}
	
	public void consultarPeriodico(){
		try {
			List objs = new ArrayList(0);
			
			if(getCampoConsultarPeriodico().equals("nome")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getValorConsultarPeriodico(), "", null, null, false, true, 0, getUsuarioLogado());
			}
			setListaConsultarPeriodico(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarPeriodico(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
	
    public List getComboboxTipoEtiqueta() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem(0, "Catalogo"));
        itens.add(new SelectItem(1, "Periodico"));
        return itens;
    }

	public void selecionaTipoEtiqueta(){
		if (getTipoEtiqueta() == 0) {
			setExibirCatalogo(true);
		} else {
			setExibirCatalogo(false);
		}
	}
	
	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ExemplarVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(exemplarVO, SituacaoExemplar.INUTILIZADO.getValor(), getUsuarioLogado());
			setMensagemID("msg_exemplar_inutilizados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>HistoricoExemplar</code> para o objeto <code>exemplarVO</code> da
	 * classe <code>Exemplar</code>
	 */
	public String adicionarHistoricoExemplar() throws Exception {
		try {
			if (!getExemplarVO().getCodigo().equals(0)) {
				historicoExemplarVO.setExemplar(getExemplarVO().getCodigo());
			}
			if (getHistoricoExemplarVO().getResponsavel().getCodigo().intValue() != 0) {
				Integer campoResponsavel = getHistoricoExemplarVO().getResponsavel().getCodigo();
				// UsuarioVO usuario =
				// getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(campoResponsavel,
				// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				UsuarioVO usuario = getUsuarioLogado();
				getHistoricoExemplarVO().setResponsavel(usuario);
			}
			getExemplarVO().adicionarObjHistoricoExemplarVOs(getHistoricoExemplarVO());
			this.setHistoricoExemplarVO(new HistoricoExemplarVO());
			setMensagemID("msg_dados_adicionados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>HistoricoExemplar</code> para edição pelo usuário.
	 */
	public String editarHistoricoExemplar() throws Exception {
		HistoricoExemplarVO obj = (HistoricoExemplarVO) context().getExternalContext().getRequestMap().get("historicoExemplar");
		setHistoricoExemplarVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>HistoricoExemplar</code> do objeto <code>exemplarVO</code> da
	 * classe <code>Exemplar</code>
	 */
	public String removerHistoricoExemplar() throws Exception {
		HistoricoExemplarVO obj = (HistoricoExemplarVO) context().getExternalContext().getRequestMap().get("historicoExemplar");
		getExemplarVO().excluirObjHistoricoExemplarVOs(obj.getSituacao());
		setMensagemID("msg_dados_excluidos");
		return "editar";
	}

	public List<SelectItem> getListaSelectItemSituacaoExemplar() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoExemplar.class);
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>formaEntrada</code>
	 */
	public List getListaSelectItemFormaEntradaExemplar() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoEntradaAcervo.class);
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoExemplar</code>
	 */
	public List getListaSelectItemTipoExemplarExemplar() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoExemplar.class);
	}

	public void montarListaSelectItemBiblioteca() throws Exception {
		try {
			List<BibliotecaVO> resultadoConsulta = consultarBibliotecaPorNome("");
			setListaSelectItemBiblioteca(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<BibliotecaVO> consultarBibliotecaPorNome(String nomePrm) throws Exception {
		List<BibliotecaVO> lista = getFacadeFactory().getBibliotecaFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemSecao() throws Exception {
		try {
			List<SecaoVO> resultadoConsulta = consultarSecaoPorNome("");
			setListaSelectItemSecao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SecaoVO> consultarSecaoPorNome(String nomePrm) throws Exception {
		List<SecaoVO> lista = getFacadeFactory().getSecaoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void consultarCatalogo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarCatalogo().equals("codigo")) {
				if (getValorConsultarCatalogo().equals("")) {
					setValorConsultarCatalogo("0");
				}
				int valorInt = Integer.parseInt(getValorConsultarCatalogo());
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultarCatalogo().equals("titulo")) {
				objs = getFacadeFactory().getCatalogoFacade().consultaRapidaPorTituloCatalogo(getValorConsultarCatalogo(), false, getUsuarioLogado());
			}
			setListaConsultarCatalogo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarCatalogo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCatalogo() throws Exception {
		CatalogoVO obj = (CatalogoVO) context().getExternalContext().getRequestMap().get("catalogo");
		if (getMensagemDetalhada().equals("")) {
			this.getExemplarVO().setCatalogo(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarCatalogo());
		this.setValorConsultarCatalogo(null);
		this.setCampoConsultarCatalogo(null);
	}

	public void limparCampoCatalogo() {
		this.getExemplarVO().setCatalogo(new CatalogoVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da
	 * telas.
	 */
	public List getTipoConsultarComboCatalogo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("titulo", "Título"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 * 
	 * @throws Exception
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemBiblioteca();
		montarListaSelectItemSecao();
		montarListaSelectItemEstadoExemplar();
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("tipoExemplar", "Tipo Exemplar"));
		itens.add(new SelectItem("nomeBiblioteca", "Biblioteca"));
		itens.add(new SelectItem("catalogo", "Catalogo"));
		itens.add(new SelectItem("codigoBarra", "Código de Barra"));
		itens.add(new SelectItem("situacaoAtual", "Situação Atual"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void montarListaSelectItemEstadoExemplar() {
		setListaSelectItemEstadoExemplar(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(EstadoHistoricoExemplar.class));
	}

	public void gerarExemplares() {
		try {
			List<ExemplarVO> listaExemplaresASeremGerados = new ArrayList<ExemplarVO>(0);
	        try {
	            getFacadeFactory().getExemplarFacade().validarDadosGeracaoExemplares(getNumeroExemplaresAGerar(), getExemplarVO(), false);
	            getExemplarVO().setBiblioteca(
	                    getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(getExemplarVO().getBiblioteca().getCodigo(),
	                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	            getExemplarVO().setSecao(
	                    getFacadeFactory().getSecaoFacade().consultarPorChavePrimaria(getExemplarVO().getSecao().getCodigo(),
	                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
	            getExemplarVO().setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(getExemplarVO().getCatalogo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, getUsuarioLogado()));
	            getFacadeFactory().getExemplarFacade().gerarExemplares(getExemplarVO().getCatalogo(), getExemplarVO(), listaExemplaresASeremGerados,
	                    getNumeroExemplaresAGerar(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade());
	            getExemplarVO().getCatalogo().getExemplarVOs().addAll(listaExemplaresASeremGerados);
	            getListaExemplaresGerados().addAll(listaExemplaresASeremGerados);
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        } finally {
	            listaExemplaresASeremGerados = null;
	        }
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		getControleConsultaOtimizado().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do
	 * backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A
	 * mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		exemplarVO = null;
		Uteis.liberarListaMemoria(listaSelectItemBiblioteca);
		historicoExemplarVO = null;
	}

	public HistoricoExemplarVO getHistoricoExemplarVO() {
		return historicoExemplarVO;
	}

	public void setHistoricoExemplarVO(HistoricoExemplarVO historicoExemplarVO) {
		this.historicoExemplarVO = historicoExemplarVO;
	}

	public List getListaSelectItemBiblioteca() throws Exception {
		if (listaSelectItemBiblioteca == null) {
			listaSelectItemBiblioteca = new ArrayList<SelectItem>(0);
			List<BibliotecaVO> listaBiblioteca = new ArrayList<BibliotecaVO>(0);
			listaBiblioteca = getFacadeFactory().getBibliotecaFacade().consultarPorNomeBiblioteca("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			listaSelectItemBiblioteca.add(new SelectItem(0, ""));
			for (BibliotecaVO biblioteca : listaBiblioteca) {
				listaSelectItemBiblioteca.add(new SelectItem(biblioteca.getCodigo(), biblioteca.getNome()));
			}

		}
		return listaSelectItemBiblioteca;
	}

	public void setListaSelectItemBiblioteca(List listaSelectItemBiblioteca) {
		this.listaSelectItemBiblioteca = listaSelectItemBiblioteca;
	}

	public ExemplarVO getExemplarVO() {
		if (exemplarVO == null) {
			exemplarVO = new ExemplarVO();
		}
		return exemplarVO;
	}

	public void setExemplarVO(ExemplarVO exemplarVO) {
		this.exemplarVO = exemplarVO;
	}

	public List getListaSelectItemSecao() {
		if (listaSelectItemSecao == null) {
			listaSelectItemSecao = new ArrayList(0);
		}
		return (listaSelectItemSecao);
	}

	public void setListaSelectItemSecao(List listaSelectItemSecao) {
		this.listaSelectItemSecao = listaSelectItemSecao;
	}

	public String getCampoConsultarCatalogo() {
		return campoConsultarCatalogo;
	}

	public void setCampoConsultarCatalogo(String campoConsultarCatalogo) {
		this.campoConsultarCatalogo = campoConsultarCatalogo;
	}

	public String getValorConsultarCatalogo() {
		return valorConsultarCatalogo;
	}

	public void setValorConsultarCatalogo(String valorConsultarCatalogo) {
		this.valorConsultarCatalogo = valorConsultarCatalogo;
	}

	public List getListaConsultarCatalogo() {
		return listaConsultarCatalogo;
	}

	public void setListaConsultarCatalogo(List listaConsultarCatalogo) {
		this.listaConsultarCatalogo = listaConsultarCatalogo;
	}

	public List getListaSelectItemEstadoExemplar() {
		if (listaSelectItemEstadoExemplar == null) {
			listaSelectItemEstadoExemplar = new ArrayList(0);
		}
		return listaSelectItemEstadoExemplar;
	}

	public void setListaSelectItemEstadoExemplar(List listaSelectItemEstadoExemplar) {
		this.listaSelectItemEstadoExemplar = listaSelectItemEstadoExemplar;
	}

	public void setNumeroExemplaresAGerar(int numeroExemplaresAGerar) {
		this.numeroExemplaresAGerar = numeroExemplaresAGerar;
	}

	public int getNumeroExemplaresAGerar() {
		return numeroExemplaresAGerar;
	}

	public void setListaExemplaresGerados(List<ExemplarVO> listaExemplaresGerados) {
		this.listaExemplaresGerados = listaExemplaresGerados;
	}

	public List<ExemplarVO> getListaExemplaresGerados() {
		if (listaExemplaresGerados == null) {
			listaExemplaresGerados = new ArrayList<ExemplarVO>(0);
		}
		return listaExemplaresGerados;
	}

	public boolean getIsExemplarPeriodico() {
		if (getExemplarVO().getTipoExemplar().equals(TipoExemplar.PERIODICO.getValor())) {
			return true;
		}
		return false;
	}

	
	public Boolean getApresentarTipoExemplar() {
		return getControleConsulta().getCampoConsulta().equals("tipoExemplar");
	}

	public Boolean getApresentarBiblioteca() {
		return getControleConsulta().getCampoConsulta().equals("nomeBiblioteca");
	}

	public Boolean getApresentarSituacaoAtual() {
		return getControleConsulta().getCampoConsulta().equals("situacaoAtual");
	}

	public Boolean getApresentarCampoNumerico() {
		return getControleConsulta().getCampoConsulta().equals("codigoBarra") || getControleConsulta().getCampoConsulta().equals("codigo") || getControleConsulta().getCampoConsulta().equals("catalogo");
	}

	public List getListaSelectItemTipoExemplar() {

		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoExemplar.class);
	}

	public void setListaSelectItemTipoExemplar(List listaSelectItemTipoExemplar) {
		this.listaSelectItemTipoExemplar = listaSelectItemTipoExemplar;
	}

	public List getListaSelectCatalogo() throws Exception {
		if (listaSelectCatalogo == null) {
			listaSelectCatalogo = new ArrayList<SelectItem>(0);
			List<CatalogoVO> listaCatalogo = new ArrayList<CatalogoVO>(0);
			listaCatalogo = getFacadeFactory().getCatalogoFacade().consultarPorCodigo(0, false,Uteis.NIVELMONTARDADOS_COMBOBOX , 0, getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
			listaSelectCatalogo.add(new SelectItem(0, ""));
			for (CatalogoVO catalogo : listaCatalogo) {
				listaSelectCatalogo.add(new SelectItem(catalogo.getCodigo(), catalogo.getTitulo()));
			}
		}
		return listaSelectCatalogo;
	}

	public void setListaSelectCatalogo(List listaSelectCatalogo) {
		this.listaSelectCatalogo = listaSelectCatalogo;
	}
	
	public void limparConsulta() {		 
		getControleConsulta().setValorConsulta("");
	}

	public Integer getTipoEtiqueta() {
		if(tipoEtiqueta== null){
			tipoEtiqueta = 0;
		}
		return tipoEtiqueta;
	}

	public void setTipoEtiqueta(Integer tipoEtiqueta) {
		this.tipoEtiqueta = tipoEtiqueta;
	}

	public Boolean getExibirCatalogo() {
		if(exibirCatalogo == null){
			exibirCatalogo = Boolean.TRUE;
		}
		return exibirCatalogo;
	}

	public void setExibirCatalogo(Boolean exibirCatalogo) {
		this.exibirCatalogo = exibirCatalogo;
	}

	public String getCampoConsultarPeriodico() {
		if(campoConsultarPeriodico == null){
			campoConsultarPeriodico = "";
		}
		return campoConsultarPeriodico;
	}

	public void setCampoConsultarPeriodico(String campoConsultarPeriodico) {
		this.campoConsultarPeriodico = campoConsultarPeriodico;
	}

	public String getValorConsultarPeriodico() {
		if(valorConsultarPeriodico == null) {
			valorConsultarPeriodico = "";
		}
		return valorConsultarPeriodico;
	}

	public void setValorConsultarPeriodico(String valorConsultarPeriodico) {
		this.valorConsultarPeriodico = valorConsultarPeriodico;
	}

	public List<CatalogoVO> getListaConsultarPeriodico() {
		return listaConsultarPeriodico;
	}

	public void setListaConsultarPeriodico(List<CatalogoVO> listaConsultarPeriodico) {
		this.listaConsultarPeriodico = listaConsultarPeriodico;
	}
	

}
