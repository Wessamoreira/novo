package controle.compras;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.MovimentacaoEstoqueVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("MovimentacaoEstoqueControle")
@Scope("viewScope")
@Lazy
public class MovimentacaoEstoqueControle extends SuperControle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7615115499672363577L;
	private MovimentacaoEstoqueVO movimentacaoEstoqueVO;
	private List listaSelectItemUnidadeEnsino;
	private String campoConsultaProduto;
	private String valorConsultaProduto;
	private List listaConsultaProduto;
	private int valorConsultaUnidadeEnsino;
	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoOrigem = true;
	private List<FuncionarioCargoVO> listaConsultaFuncionarioCentroCusto;
	private String campoConsultaFuncionarioCentroCusto;
	private String valorConsultaFuncionarioCentroCusto;

	public MovimentacaoEstoqueControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		inicializarListasSelectItemTodosComboBox();
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>MovimentacaoEstoque</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Novo Movimentação Estoque", "Novo");
		removerObjetoMemoria(this);
		setMovimentacaoEstoqueVO(new MovimentacaoEstoqueVO());
		inicializarListasSelectItemTodosComboBox();
		inicializarResponsavel();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoEstoqueForm");
	}

	public void inicializarResponsavel() {
		try {
			getMovimentacaoEstoqueVO().setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			getMovimentacaoEstoqueVO().setResponsavel(new UsuarioVO());
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>MovimentacaoEstoque</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Inicializando Editar Movimentação Estoque", "Editando");
		MovimentacaoEstoqueVO obj = (MovimentacaoEstoqueVO) context().getExternalContext().getRequestMap().get("movimentacaoEstoqueItem");
		obj.setNovoObj(Boolean.FALSE);
		setMovimentacaoEstoqueVO(obj);
		inicializarListasSelectItemTodosComboBox();
		registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Finalizando Editar Movimentação Estoque", "Editando");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoEstoqueForm");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>MovimentacaoEstoque</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (!Uteis.isAtributoPreenchido(getMovimentacaoEstoqueVO())) {
				registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Inicializando Incluir Movimentação Estoque", "Incluindo");
				getFacadeFactory().getMovimentacaoEstoqueFacade().incluir(movimentacaoEstoqueVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Finalizando Incluir Movimentação Estoque", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Inicializando Alterar Movimentação Estoque", "Alterando");
				getFacadeFactory().getMovimentacaoEstoqueFacade().alterar(movimentacaoEstoqueVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Finalizando Alterar Movimentação Estoque", "Alterando");
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP MovimentacaoEstoqueCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Inicializando Consultar Movimentação Estoque", "Consultando");
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getMovimentacaoEstoqueFacade().consultarPorCodigo(new Integer(valorInt), getControleConsulta().getDataIni(), getControleConsulta().getDataFim(),
						getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoMovimentacao")) {
				objs = getFacadeFactory().getMovimentacaoEstoqueFacade().consultarPorTipoMovimentacao(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(),
						getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeProduto")) {
				objs = getFacadeFactory().getMovimentacaoEstoqueFacade().consultarPorNomeProduto(getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(),
						getControleConsulta().getDataFim(), getValorUnidadeEnsino(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "MovimentacaoEstoqueControle", "Finalizando Consultar Movimentação Estoque", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoEstoqueCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoEstoqueCons");
		}
	}
	

	public void validarDadosTipoMovimentacao() {
		try {
			if(getMovimentacaoEstoqueVO().isTipoMovimentacaoEntrada() || getMovimentacaoEstoqueVO().isTipoMovimentacaoSaida()){
				getMovimentacaoEstoqueVO().setUnidadeEnsinoDestino(new UnidadeEnsinoVO());
				getMovimentacaoEstoqueVO().setCentroResultadoEstoqueDestino(new CentroResultadoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarCentroResultadoEstoque() {
		try {
			if(Uteis.isAtributoPreenchido(getMovimentacaoEstoqueVO().getUnidadeEnsino())){
				getMovimentacaoEstoqueVO().setCentroResultadoEstoque(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoEstoquePorUnidadeEnsino(getMovimentacaoEstoqueVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}else{
				getMovimentacaoEstoqueVO().setCentroResultadoEstoque(new CentroResultadoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void validarCentroResultadoEstoqueDestino() {
		try {
			if(Uteis.isAtributoPreenchido(getMovimentacaoEstoqueVO().getUnidadeEnsinoDestino())){
				getMovimentacaoEstoqueVO().setCentroResultadoEstoqueDestino(getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoEstoquePorUnidadeEnsino(getMovimentacaoEstoqueVO().getUnidadeEnsinoDestino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}else{
				getMovimentacaoEstoqueVO().setCentroResultadoEstoqueDestino(new CentroResultadoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getValorUnidadeEnsino() throws Exception {
		if (this.getUnidadeEnsinoLogado().getCodigo() == 0) {
			return getValorConsultaUnidadeEnsino();
		} else {
			return this.getUnidadeEnsinoLogado().getCodigo();
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo <code>tipoMovimentacao</code>
	 */
	public List getListaSelectItemTipoMovimentacaoMovimentacaoEstoque() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipoMovimentacaoEstoques = (Hashtable) Dominios.getTipoMovimentacaoEstoque();
		Enumeration keys = tipoMovimentacaoEstoques.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoMovimentacaoEstoques.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>EnderecoEstoqueNovo</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			if ((getUnidadeEnsinoLogado().getCodigo().intValue() != 0) && (getUsuarioLogado().getPerfilAdministrador().equals(false))) {
				setListaSelectItemUnidadeEnsino(new ArrayList(0));
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getMovimentacaoEstoqueVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
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
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>EnderecoEstoqueNovo</code>. Buscando todos os objetos correspondentes a entidade <code>EnderecoEstoque</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void selecionarProduto() {
		try {
			ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produto");
			Uteis.checkState(obj.getTipoProdutoServicoEnum().isServico(), "O Produto do tipo Serviço não é permitido.");
			this.getMovimentacaoEstoqueVO().setProduto(obj);
			if (!isExigeCentroCustoRequisitante()) {
				limparFuncionarioCargo();
			}
			this.listaConsultaProduto.clear();
			this.valorConsultaProduto = null;
			this.campoConsultaProduto = null;
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("categoriaProduto", "Categoria Produto"));
		return itens;
	}

	public void consultarProduto() {

		try {

			List<ProdutoServicoVO> objs = new ArrayList(0);
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProduto(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("categoriaProduto")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeCategoriaProduto(getValorConsultaProduto(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("tipoUnidade")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorTipoUnidade(getValorConsultaProduto(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			if (isCentroResultadoOrigem()) {
				getMovimentacaoEstoqueVO().setCentroResultadoEstoque(obj);
			} else {
				getMovimentacaoEstoqueVO().setCentroResultadoEstoqueDestino(obj);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarConsultarCentroResultadoOrigem() {
		try {
			setCentroResultadoOrigem(true);
			setCentroResultadoDataModelo(new DataModelo());
			getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void inicializarConsultarCentroResultadoDestino() {
		try {
			setCentroResultadoOrigem(false);
			setCentroResultadoDataModelo(new DataModelo());
			getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, null, null, null, getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public boolean isCentroResultadoOrigem() {
		return centroResultadoOrigem;
	}

	public void setCentroResultadoOrigem(boolean centroResultadoOrigem) {
		this.centroResultadoOrigem = centroResultadoOrigem;
	}

	public String getCampoConsultaProduto() {
		return campoConsultaProduto;
	}

	public void setCampoConsultaProduto(String campoConsultaProduto) {
		this.campoConsultaProduto = campoConsultaProduto;
	}

	public List getListaConsultaProduto() {
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
	}

	public String getValorConsultaProduto() {
		return valorConsultaProduto;
	}

	public void setValorConsultaProduto(String valorConsultaProduto) {
		this.valorConsultaProduto = valorConsultaProduto;
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {

		montarListaSelectItemUnidadeEnsino();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeProduto", "Produto"));
		itens.add(new SelectItem("tipoMovimentacao", "Tipo Movimentação"));
		return itens;
	}

	/**
	 * Metodo que verifica se o campo selecionado precisa de um combobox
	 *
	 * @return boolean
	 */
	public boolean isCampoComboBox() {
		if (getControleConsulta().getCampoConsulta().equals("tipoMovimentacao")) {
			return true;
		}
		return false;
	}

	/**
	 * Metodo que verifica se o campo selecionado para consulta é do tipo data.
	 *
	 * @return boolean;
	 */
	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		inicializarListasSelectItemTodosComboBox();
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("movimentacaoEstoqueCons");
	}

	public List getListaSelectItemUnidadeEnsino() {
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public MovimentacaoEstoqueVO getMovimentacaoEstoqueVO() {
		return movimentacaoEstoqueVO;
	}

	public void setMovimentacaoEstoqueVO(MovimentacaoEstoqueVO movimentacaoEstoqueVO) {
		this.movimentacaoEstoqueVO = movimentacaoEstoqueVO;
	}

	/**
	 * @return the valorConsultaUnidadeEnsino
	 */
	public int getValorConsultaUnidadeEnsino() {
		return valorConsultaUnidadeEnsino;
	}

	/**
	 * @param valorConsultaUnidadeEnsino
	 *            the valorConsultaUnidadeEnsino to set
	 */
	public void setValorConsultaUnidadeEnsino(int valorConsultaUnidadeEnsino) {
		this.valorConsultaUnidadeEnsino = valorConsultaUnidadeEnsino;
	}
	
	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_MOVIMENTACAO_ESTOQUE, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermiteAlterarCentroResultadoDestino() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_DESTINO_MOVIMENTACAO_ESTOQUE, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<FuncionarioCargoVO> getListaConsultaFuncionarioCentroCusto() {
		if (listaConsultaFuncionarioCentroCusto == null) {
			listaConsultaFuncionarioCentroCusto = new ArrayList<>();
		}
		return listaConsultaFuncionarioCentroCusto;
	}

	public void setListaConsultaFuncionarioCentroCusto(List<FuncionarioCargoVO> listaConsultaFuncionarioCentroCusto) {
		this.listaConsultaFuncionarioCentroCusto = listaConsultaFuncionarioCentroCusto;
	}

	public String getCampoConsultaFuncionarioCentroCusto() {
		if (campoConsultaFuncionarioCentroCusto == null) {
			campoConsultaFuncionarioCentroCusto = "nomeFuncionario";
		}
		return campoConsultaFuncionarioCentroCusto;
	}

	public void setCampoConsultaFuncionarioCentroCusto(String campoConsultaFuncionarioCentroCusto) {
		this.campoConsultaFuncionarioCentroCusto = campoConsultaFuncionarioCentroCusto;
	}

	public String getValorConsultaFuncionarioCentroCusto() {
		if (valorConsultaFuncionarioCentroCusto == null) {
			valorConsultaFuncionarioCentroCusto = "";
		}
		return valorConsultaFuncionarioCentroCusto;
	}

	public void setValorConsultaFuncionarioCentroCusto(String valorConsultaFuncionarioCentroCusto) {
		this.valorConsultaFuncionarioCentroCusto = valorConsultaFuncionarioCentroCusto;
	}

	public List<SelectItem> getTipoConsultaComboFuncionarioCentroCusto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomeFuncionario", "Nome"));
		itens.add(new SelectItem("nomeCargo", "Cargo"));
		return itens;
	}
	
	public void consultarFuncionarioCentroCusto() {
		try {
			if (!Uteis.isAtributoPreenchido(getMovimentacaoEstoqueVO().getUnidadeEnsino())) {
				throw new Exception("Informe a Unidade de Ensino.");
			}
			if (getCampoConsultaFuncionarioCentroCusto().equals("nomeFuncionario")) {
				setListaConsultaFuncionarioCentroCusto(getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioUnidadeEnsinoSituacao(
						getValorConsultaFuncionarioCentroCusto(), getMovimentacaoEstoqueVO().getUnidadeEnsino().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			} else if (getCampoConsultaFuncionarioCentroCusto().equals("nomeCargo")) {
				setListaConsultaFuncionarioCentroCusto(getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnidadeEnsinoSituacao(
						getValorConsultaFuncionarioCentroCusto(), getMovimentacaoEstoqueVO().getUnidadeEnsino().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaFuncionarioCentroCusto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarFuncionarioCentroCusto() {
		try {
			FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCentroCustoItens");
			if (Uteis.isAtributoPreenchido(obj)) {
				getMovimentacaoEstoqueVO().setFuncionarioCargoVO(obj);
				Uteis.liberarListaMemoria(getListaConsultaFuncionarioCentroCusto());
				setCampoConsultaFuncionarioCentroCusto("");
				setValorConsultaFuncionarioCentroCusto("");
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			}
		} catch (Exception e) {
			setListaConsultaFuncionarioCentroCusto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean isExigeCentroCustoRequisitante() {
		return !Uteis.isAtributoPreenchido(getMovimentacaoEstoqueVO().getProduto()) ? false : getMovimentacaoEstoqueVO().getProduto().getCategoriaProduto().getCategoriaDespesa().getExigeCentroCustoRequisitante();
	}
	
	public void limparFuncionarioCargo() {
		getMovimentacaoEstoqueVO().setFuncionarioCargoVO(new FuncionarioCargoVO());
	}
	
	public void limparListaConsultaFuncionarioCentroCusto() {
		Uteis.liberarListaMemoria(getListaConsultaFuncionarioCentroCusto());
	}
}
