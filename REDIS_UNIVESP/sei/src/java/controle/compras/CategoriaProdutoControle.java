package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas categoriaProdutoForm.jsp
 * categoriaProdutoCons.jsp) com as funcionalidades da classe <code>CategoriaProduto</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaProduto
 * @see CategoriaProdutoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.TramiteCotacaoCompraVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("CategoriaProdutoControle")
@Scope("viewScope")
@Lazy
public class CategoriaProdutoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -1581458533289719577L;

	private CategoriaProdutoVO categoriaProdutoVO;
	private String campoConsultaCategoriaDespesa;
	private String valorConsultaCategoriaDespesa;
	private List<CategoriaDespesaVO> listaConsultaCategoriaDespesa;

	private String campoConsultaCategoriaProdutoPai;
	private String valorConsultaCategoriaProdutoPai;
	protected List<CategoriaProdutoVO> listaConsultaCategoriaProdutoPai;

	private List<SelectItem> listaTramiteCotacaoCompra;
	private List<CategoriaProdutoVO> consultaComHierarquia;
	
	private List<SelectItem> listaSelectItemQuestionario;

	public CategoriaProdutoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaQuestionario();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>CategoriaProduto</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Novo Categoria Produto", "Novo");
		removerObjetoMemoria(this);
		setCategoriaProdutoVO(new CategoriaProdutoVO());
		montarListaTramiteCotacaoCompra();
		montarListaQuestionario();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CategoriaProduto</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Inicializando Editar Categoria Produto", "Editando");
		CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItem");
		obj.setNovoObj(Boolean.FALSE);
		setCategoriaProdutoVO(montarCategoriaProdutoVOCompleto(obj));
		montarListaTramiteCotacaoCompra();
		montarListaQuestionario();
		registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Finalizando Editar Categoria Produto", "Editando");
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoForm");
	}

	private CategoriaProdutoVO montarCategoriaProdutoVOCompleto(CategoriaProdutoVO obj) {
		try {
			return getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new CategoriaProdutoVO();
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CategoriaProduto</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (categoriaProdutoVO.isNovoObj().booleanValue()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Inicializando Incluir Categoria Produto", "Incluindo");
				getFacadeFactory().getCategoriaProdutoFacade().incluir(categoriaProdutoVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Finalizando Incluir Categoria Produto", "Incluindo");
			} else {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Inicializando Alterar Categoria Produto", "Alterando");
				getFacadeFactory().getCategoriaProdutoFacade().alterar(categoriaProdutoVO, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Finalizando Alterar Categoria Produto", "Alterando");
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP CategoriaProdutoCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<CategoriaProdutoVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				int valorInt = 0;
				if (!getControleConsulta().getValorConsulta().equals("")) {
					valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				}
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Inicializando Consultar Categoria Produto", "Consultando");
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Finalizando Consultar Categoria Produto", "Consultando");
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Inicializando Consultar Categoria Produto", "Consultando");
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Finalizando Consultar Categoria Produto", "Consultando");
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoCons");
		} catch (Exception e) {
			e.printStackTrace();
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoCons");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>CategoriaProdutoVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Inicializando Excluir Categoria Produto", "Excluindo");
			getFacadeFactory().getCategoriaProdutoFacade().excluir(categoriaProdutoVO, getUsuarioLogado());
			setCategoriaProdutoVO(new CategoriaProdutoVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaProdutoControle", "Finalizando Excluir Categoria Produto", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoForm");
		}
	}

	public void selecionarCategoriaDespesa() throws Exception {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItem");
		this.getCategoriaProdutoVO().setCategoriaDespesa(obj);
		this.listaConsultaCategoriaDespesa.clear();
		this.valorConsultaCategoriaDespesa = null;
		this.campoConsultaCategoriaDespesa = null;
	}

	public void selecionarCategoriaProdutoPai() throws Exception {
		CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItem");
		this.getCategoriaProdutoVO().setCategoriaProdutoPai(obj);
		this.getListaConsultaCategoriaProdutoPai().clear();
		this.valorConsultaCategoriaProdutoPai = null;
		this.campoConsultaCategoriaProdutoPai = null;
	}

	public List<SelectItem> getTipoConsultaComboCategoriaDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCategoriaProdutoPai() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void consultarCategoriaDespesa() {
		try {
			List<CategoriaDespesaVO> objs = new ArrayList<>(0);
			if (this.campoConsultaCategoriaDespesa.equals("codigo")) {
				if (this.valorConsultaCategoriaDespesa.equals("")) {
					this.valorConsultaCategoriaDespesa = "0";
				}
				int valorInt = Integer.parseInt(this.valorConsultaCategoriaDespesa);
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (this.campoConsultaCategoriaDespesa.equals("identificador")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(this.valorConsultaCategoriaDespesa, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (this.campoConsultaCategoriaDespesa.equals("nome")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(this.valorConsultaCategoriaDespesa, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCategoriaDespesa(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCategoriaDespesa(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCategoriaProdutoPai() {
		try {
			getListaConsultaCategoriaProdutoPai().clear();

			consultaComHierarquia = getFacadeFactory().getCategoriaProdutoFacade().consultaComHierarquia(getCategoriaProdutoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, this.getUsuarioLogado());

			switch (this.getCampoConsultaCategoriaProdutoPai()) {
			case "nome":
				consultaComHierarquia = consultaComHierarquia.stream().filter(p -> p.getNome().toLowerCase().contains(this.getValorConsultaCategoriaProdutoPai().replace("%", "").toLowerCase())).collect(Collectors.toList());
				break;
			default:
				break;
			}
			getListaConsultaCategoriaProdutoPai().addAll(consultaComHierarquia);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCategoriaDespesa(new ArrayList<>(0));
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

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaProdutoCons");
	}

	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public CategoriaProdutoVO getCategoriaProdutoVO() {
		if (categoriaProdutoVO == null) {
			categoriaProdutoVO = new CategoriaProdutoVO();
		}
		return categoriaProdutoVO;
	}

	public List<SelectItem> getListaTramiteCotacaoCompra() {
		this.listaTramiteCotacaoCompra = Optional.ofNullable(this.listaTramiteCotacaoCompra).orElse(new ArrayList<>());
		return listaTramiteCotacaoCompra;
	}

	public void montarListaTramiteCotacaoCompra() throws Exception {
		List<TramiteCotacaoCompraVO> consultarPornome = getFacadeFactory().getTramiteFacade().consultarSituacaoAtiva(false, getUsuarioLogado());
		getListaTramiteCotacaoCompra().clear();
		getListaTramiteCotacaoCompra().add(new SelectItem(0, ""));
		for (TramiteCotacaoCompraVO tramiteCotacaoCompra : consultarPornome) {
			getListaTramiteCotacaoCompra().add(new SelectItem(tramiteCotacaoCompra.getCodigo(), tramiteCotacaoCompra.getNome()));
		}
	}
	
	public void montarListaQuestionario() {
		try {
			List<QuestionarioVO> lista = getFacadeFactory().getQuestionarioFacade().consultarPorDescricao("", EscopoPerguntaEnum.REQUISICAO.getValor(),
					false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemQuestionario().clear();
			getListaSelectItemQuestionario().add(new SelectItem("", ""));
			for (QuestionarioVO questionarioVO : lista) {
				getListaSelectItemQuestionario().add(new SelectItem(questionarioVO.getCodigo(), questionarioVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void setCategoriaProdutoVO(CategoriaProdutoVO categoriaProdutoVO) {
		this.categoriaProdutoVO = categoriaProdutoVO;
	}

	public String getCampoConsultaCategoriaDespesa() {
		return campoConsultaCategoriaDespesa;
	}

	public void setCampoConsultaCategoriaDespesa(String campoConsultaCategoriaDespesa) {
		this.campoConsultaCategoriaDespesa = campoConsultaCategoriaDespesa;
	}

	public List<CategoriaDespesaVO> getListaConsultaCategoriaDespesa() {
		return listaConsultaCategoriaDespesa;
	}

	public void setListaConsultaCategoriaDespesa(List<CategoriaDespesaVO> listaConsultaCategoriaDespesa) {
		this.listaConsultaCategoriaDespesa = listaConsultaCategoriaDespesa;
	}

	public String getValorConsultaCategoriaDespesa() {
		return valorConsultaCategoriaDespesa;
	}

	public void setValorConsultaCategoriaDespesa(String valorConsultaCategoriaDespesa) {
		this.valorConsultaCategoriaDespesa = valorConsultaCategoriaDespesa;
	}

	public void limparTramiteCotacaoCompra() {
		this.getCategoriaProdutoVO().setTramiteCotacaoCompra(new TramiteCotacaoCompraVO());
	}

	public String getCampoConsultaCategoriaProdutoPai() {
		return campoConsultaCategoriaProdutoPai;
	}

	public void setCampoConsultaCategoriaProdutoPai(String campoConsultaCategoriaProdutoPai) {
		this.campoConsultaCategoriaProdutoPai = campoConsultaCategoriaProdutoPai;
	}

	public List<CategoriaProdutoVO> getListaConsultaCategoriaProdutoPai() {
		this.listaConsultaCategoriaProdutoPai = Optional.ofNullable(this.listaConsultaCategoriaProdutoPai).orElse(new ArrayList<>());
		return listaConsultaCategoriaProdutoPai;
	}

	public String getValorConsultaCategoriaProdutoPai() {
		return valorConsultaCategoriaProdutoPai;
	}

	public void setValorConsultaCategoriaProdutoPai(String valorConsultaCategoriaProdutoPai) {
		this.valorConsultaCategoriaProdutoPai = valorConsultaCategoriaProdutoPai;
	}

	public List<SelectItem> getListaSelectItemQuestionario() {
		if (listaSelectItemQuestionario == null) {
			listaSelectItemQuestionario = new ArrayList<>();
		}
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}
	
	public void limparCategoriaDespesa() {
		this.getCategoriaProdutoVO().setCategoriaDespesa(null);
	}
}
