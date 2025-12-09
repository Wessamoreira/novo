package controle.arquitetura;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * perfilAcessoForm.jsp perfilAcessoCons.jsp) com as funcionalidades da classe <code>PerfilAcesso</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see PerfilAcesso
 * @see PerfilAcessoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;


import negocio.interfaces.arquitetura.Filter;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoVO;
import negocio.comuns.arquitetura.enumeradores.OpcaoPermissaoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

@Controller("PerfilAcessoControle")
@Scope("viewScope")
@Lazy
public class PerfilAcessoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 7039047596637105277L;
	private PerfilAcessoModuloEnum modulo;
	private PerfilAcessoSubModuloEnum subModulo;
	private List<PerfilAcessoSubModuloEnum> listaPerfilAcessoSubModuloEnums;
	private PerfilAcessoVO perfilAcessoVO;
	private PerfilAcessoVO perfilAcessoCarregar;
	protected List<SelectItem> listaSelectItemCodPerfilAcesso;
	private PermissaoVO permissaoVO;
	private List<SelectItem> listaSelectItemAmbientes;
	private String filtroCadastro;
	private String filtroFuncionalidade;
	private String filtroPermissao;
	public List<SelectItem> listaSelectItemFuncionalidade;
	public List<SelectItem> listaSelectItemFiltrarPermissoes;

	public PerfilAcessoControle() throws Exception {
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>PerfilAcesso</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setPerfilAcessoVO(new PerfilAcessoVO());
		getPerfilAcessoVO().setResponsavel(getUsuarioLogado().getCodigo());
		setPermissaoVO(new PermissaoVO());
		montarListaSelectItemCodPerfilAcesso();
		setMensagemID("msg_entre_dados");
		getFacadeFactory().getPermissaoFacade().realizarGeracaoListaPermissaoPorAmbiente(getPerfilAcessoVO(), getPerfilAcessoVO().getAmbiente(), true, getAplicacaoControle().getCliente());
		return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>PerfilAcesso</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try{
			PerfilAcessoVO obj = (PerfilAcessoVO) context().getExternalContext().getRequestMap().get("perfilAcessoItem");
			obj = getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimariaUnica(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());		
			obj.setNovoObj(Boolean.FALSE);
			setPerfilAcessoVO(obj);
			getPerfilAcessoVO().setResponsavel(getUsuarioLogado().getCodigo());
			getFacadeFactory().getPermissaoFacade().realizarGeracaoListaPermissaoPorAmbiente(getPerfilAcessoVO(), getPerfilAcessoVO().getAmbiente(), false, getAplicacaoControle().getCliente());		
			setPermissaoVO(new PermissaoVO());
			montarListaSelectItemCodPerfilAcesso();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoForm");
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoCons");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>PerfilAcesso</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (perfilAcessoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPerfilAcessoFacade().incluir(perfilAcessoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getPerfilAcessoFacade().alterar(perfilAcessoVO, getUsuarioLogado());
			}
			getAplicacaoControle().removerPerfilAcesso(perfilAcessoVO.getCodigo());
			if(getPerfilAcessoVO().getCodigo().equals(getUsuarioLogado().getPerfilAcesso().getCodigo())) {
				getUsuarioLogado().setPerfilAcesso((PerfilAcessoVO) getPerfilAcessoVO().clone());
				getLoginControle().montarPermissoesMenu(getPerfilAcessoVO());
			}
			if(getPerfilAcessoVO().getAmbiente().equals(TipoVisaoEnum.ALUNO)) {
				getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(null, null, false, true);
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String clonar() {
		try {
			getPerfilAcessoVO().setCodigo(new Integer(0));
			getPerfilAcessoVO().setNovoObj(Boolean.TRUE);
			getPerfilAcessoVO().setNome(getPerfilAcessoVO().getNome() + " - Clonada");
			for (PermissaoVO p : getPerfilAcessoVO().getPermissaoVOs()) {
				p.setCodPerfilAcesso(new Integer(0));
				p.setNovoObj(Boolean.TRUE);
				for(PermissaoVO func: p.getFuncionalidades()){
					func.setCodPerfilAcesso(new Integer(0));
					func.setNovoObj(Boolean.TRUE);
				}
			}
			setMensagemID("msg_dados_clonados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * PerfilAcessoCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<PerfilAcessoVO> objs = new ArrayList<PerfilAcessoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPerfilAcessoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<PerfilAcessoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>PerfilAcessoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPerfilAcessoFacade().excluir(perfilAcessoVO, getUsuarioLogado());
			setPerfilAcessoVO(new PerfilAcessoVO());

			setPermissaoVO(new PermissaoVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoForm");
		}
	}

	public List<PerfilAcessoVO> consultarPerfilAcessoPorNome(String nomePrm) throws Exception {
		List<PerfilAcessoVO> lista = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>CodPerfilAcesso</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>PerfilAcesso</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCodPerfilAcesso() {
		try {
			setListaSelectItemCodPerfilAcesso(UtilSelectItem.getListaSelectItem(consultarPerfilAcessoPorNome(""), "codigo", "nome"));
		} catch (Exception e) {

		}
	}

	public void atualizarPermissaoPerfilAcesso() throws Exception {
		try {
			setPerfilAcessoCarregar(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(getPerfilAcessoCarregar().getCodigo(), getUsuarioLogado()));
			getFacadeFactory().getPermissaoFacade().realizarGeracaoListaPermissaoPorAmbiente(getPerfilAcessoCarregar(), getPerfilAcessoCarregar().getAmbiente(), false, getAplicacaoControle().getCliente());
			getPerfilAcessoVO().montarPermissoes(getPerfilAcessoCarregar().getPermissaoVOs());
		} catch (Exception e) {
			System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
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
	public List<SelectItem> tipoConsultaCombo;

	public List<SelectItem> getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList<SelectItem>(0);
			tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
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
		setListaConsulta(new ArrayList<PerfilAcessoVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("perfilAcessoCons");
	}

	public PermissaoVO getPermissaoVO() {
		if (permissaoVO == null) {
			permissaoVO = new PermissaoVO();
		}
		return permissaoVO;
	}

	public void setPermissaoVO(PermissaoVO permissaoVO) {
		this.permissaoVO = permissaoVO;
	}

	public PerfilAcessoVO getPerfilAcessoVO() {
		if (perfilAcessoVO == null) {
			perfilAcessoVO = new PerfilAcessoVO();
		}
		return perfilAcessoVO;
	}

	public void setPerfilAcessoVO(PerfilAcessoVO perfilAcessoVO) {
		this.perfilAcessoVO = perfilAcessoVO;
	}

	public void scrollerListener() throws Exception {

		consultar();
	}

	public List<SelectItem> getListaSelectItemCodPerfilAcesso() {
		if (listaSelectItemCodPerfilAcesso == null) {
			listaSelectItemCodPerfilAcesso = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemCodPerfilAcesso);
	}

	public void setListaSelectItemCodPerfilAcesso(List<SelectItem> listaSelectItemCodPerfilAcesso) {
		this.listaSelectItemCodPerfilAcesso = listaSelectItemCodPerfilAcesso;
	}

	/**
	 * @return the perfilAcessoCarregar
	 */
	public PerfilAcessoVO getPerfilAcessoCarregar() {
		if (perfilAcessoCarregar == null) {
			perfilAcessoCarregar = new PerfilAcessoVO();
		}
		return perfilAcessoCarregar;
	}

	/**
	 * @param perfilAcessoCarregar
	 *            the perfilAcessoCarregar to set
	 */
	public void setPerfilAcessoCarregar(PerfilAcessoVO perfilAcessoCarregar) {
		this.perfilAcessoCarregar = perfilAcessoCarregar;
	}

	public List<SelectItem> getListaSelectItemAmbientes() {
		if (listaSelectItemAmbientes == null) {
			listaSelectItemAmbientes = new ArrayList<SelectItem>(0);
			montarListaSelectItemAmbiente();
		}
		return listaSelectItemAmbientes;
	}

	public void setListaSelectItemAmbientes(List<SelectItem> listaSelectItemAmbientes) {
		this.listaSelectItemAmbientes = listaSelectItemAmbientes;
	}

	public void montarListaSelectItemAmbiente() {
		getListaSelectItemAmbientes().clear();
		for (TipoVisaoEnum ambiente : TipoVisaoEnum.values()) {
			getListaSelectItemAmbientes().add(new SelectItem(ambiente, ambiente.getValorApresentar()));
		}
	}

	public String getFiltroCadastro() {
		if (filtroCadastro == null) {
			filtroCadastro = "";
		}
		return filtroCadastro;
	}

	public void setFiltroCadastro(String filtroCadastro) {
		this.filtroCadastro = filtroCadastro;
	}

	public String getFiltroFuncionalidade() {
		if (filtroFuncionalidade == null) {
			filtroFuncionalidade = "";
		}
		return filtroFuncionalidade;
	}

	public void setFiltroFuncionalidade(String filtroFuncionalidade) {
		this.filtroFuncionalidade = filtroFuncionalidade;
	}

	public Integer getElement() {
		return getListaPerfilAcessoSubModuloEnums().size();
	}

	public Integer getColumn() {
		if (getListaPerfilAcessoSubModuloEnums().size() > 6) {
			return 6;
		}
		return getListaPerfilAcessoSubModuloEnums().size();
	}

	public void realizarGeracaoListaPermissaoPorAmbiente() {
		getPerfilAcessoVO().setPermissaoVOs(new ArrayList<>(0));
		getPermissaoVO().setTotal(false);
		getPermissaoVO().setTotalSemExcluir(false);
		getFacadeFactory().getPermissaoFacade().realizarGeracaoListaPermissaoPorAmbiente(getPerfilAcessoVO(), getPerfilAcessoVO().getAmbiente(), true, getAplicacaoControle().getCliente());
		setModulo(PerfilAcessoModuloEnum.TODOS);
		setSubModulo(PerfilAcessoSubModuloEnum.TODOS);
	}

	public PerfilAcessoModuloEnum getModulo() {
		if (modulo == null) {
			modulo = PerfilAcessoModuloEnum.TODOS;
		}
		return modulo;
	}

	public void setModulo(PerfilAcessoModuloEnum modulo) {
		this.modulo = modulo;
	}

	public PerfilAcessoSubModuloEnum getSubModulo() {
		if (subModulo == null) {
			subModulo = PerfilAcessoSubModuloEnum.TODOS;
		}
		return subModulo;
	}

	public void setSubModulo(PerfilAcessoSubModuloEnum subModulo) {
		this.subModulo = subModulo;
	}

	public List<PerfilAcessoSubModuloEnum> getListaPerfilAcessoSubModuloEnums() {
		if (listaPerfilAcessoSubModuloEnums == null) {
			listaPerfilAcessoSubModuloEnums = new ArrayList<PerfilAcessoSubModuloEnum>(0);
		}
		return listaPerfilAcessoSubModuloEnums;
	}

	public void setListaPerfilAcessoSubModuloEnums(List<PerfilAcessoSubModuloEnum> listaPerfilAcessoSubModuloEnums) {
		this.listaPerfilAcessoSubModuloEnums = listaPerfilAcessoSubModuloEnums;
	}

	public void montarListaPerfilAcessoSubModuloEnum() {
		getListaPerfilAcessoSubModuloEnums().clear();
		limparMensagem();
		if (!getModulo().equals(PerfilAcessoModuloEnum.TODOS)) {
			if(getFacadeFactory().getPermissaoFacade().validarPermissaoModuloHabilitadoCliente(getAplicacaoControle().getCliente(), getModulo())) {
				setListaPerfilAcessoSubModuloEnums(PerfilAcessoSubModuloEnum.getListaPerfilAcessoSubModuloEnumPorModulo(getModulo()));
			}else {
				setModulo(PerfilAcessoModuloEnum.TODOS);
				setMensagemDetalhada("msg_erro", "Este Módulo Não Está Disponível Em Seu Pacote", Uteis.ERRO);
				limparMensagem();
			}
		}
	}

	public Filter<?> getFiltrarEntidade() {
		return new Filter<PermissaoVO>() {
			public boolean accept(PermissaoVO item) {
				// Filtra o Modulo Selecionado caso esteja no Ambiente
				// adiminstrativo
				if (getPerfilAcessoVO().getAmbiente().equals(TipoVisaoEnum.ADMINISTRATIVA) && !getModulo().equals(PerfilAcessoModuloEnum.TODOS) 
						&& !getFacadeFactory().getPermissaoFacade().validarPermissaoEstaNoModulo(getAplicacaoControle().getCliente(), (PerfilAcessoPermissaoEnumInterface)item.getPermissao(), getModulo())) {
					return false;
				}
				// Filtra o Sub-Modulo Selecionado caso esteja no Ambiente
				// adiminstrativo
				if (getPerfilAcessoVO().getAmbiente().equals(TipoVisaoEnum.ADMINISTRATIVA) && !getModulo().equals(PerfilAcessoModuloEnum.TODOS) && !getSubModulo().equals(PerfilAcessoSubModuloEnum.TODOS) 
						&& !getFacadeFactory().getPermissaoFacade().validarPermissaoEstaNoSubModulo(getAplicacaoControle().getCliente(),(PerfilAcessoPermissaoEnumInterface)item.getPermissao(), getSubModulo())) {
					return false;
				}
				// Filtra o Entidade conforme digitado no input da tabela
				if (!getFiltroCadastro().trim().isEmpty() && !Uteis.removerAcentos(item.getTitulo().toUpperCase()).contains(Uteis.removerAcentos(getFiltroCadastro().trim().toUpperCase()))) {
					return false;
				}

				// Filtra o Funcionalidade conforme digitado no input da tabela
				if (!getFiltroFuncionalidade().trim().isEmpty() && !item.getDescricaoFuncinalidades().toString().contains(Uteis.removerAcentos(getFiltroFuncionalidade().trim().toUpperCase()))) {
					return false;
				}
				return true;
			}
		};
	}

	public Filter<?> getFiltrarFuncionalidade() {
		return new Filter<PermissaoVO>() {
			public boolean accept(PermissaoVO item) {
				// Filtra o Entidade conforme digitado no input da tabela
				return !(!getFiltroFuncionalidade().trim().isEmpty() && !Uteis.removerAcentos(item.getTitulo().toUpperCase()).contains(Uteis.removerAcentos(getFiltroFuncionalidade().trim().toUpperCase())));
			}
		};
	}
	
	public Filter<?> getFiltrarPermissao() {
		return new Filter<PermissaoVO>() {
			public boolean accept(PermissaoVO item) {
				// Filtra a Permissão conforme escolhido na combobox na tela
				if (!item.getTotal().equals(true) && getFiltroPermissao().equals("totalSelecionado")) {
					return false;
				}
				if (!item.getTotal().equals(false) && getFiltroPermissao().equals("totalNaoSelecionado")) {
					return false;
				}
				return true;
			}
		};
	}

	public void realizarReplicacaoPermissao() {
		getFacadeFactory().getPerfilAcessoFacade().realizarReplicacaoPermissao(getPerfilAcessoVO(), getModulo(), getSubModulo(), getPermissaoVO(), getFiltroCadastro(), getFiltroFuncionalidade(), getAplicacaoControle().getCliente(), getUsuarioLogado());
	}

	public void realizarMarcacaoPermissaoItem() {
		PermissaoVO permissaoVO = (PermissaoVO) getRequestMap().get("permissaoItem");
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoPermissao(permissaoVO, null, getUsuarioLogado());
	}

	public void realizarMarcacaoPermissaoItemTotal() {
		PermissaoVO permissaoVO = (PermissaoVO) getRequestMap().get("permissaoItem");
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoPermissao(permissaoVO, OpcaoPermissaoEnum.TOTAL, getUsuarioLogado());
	}

	public void realizarMarcacaoPermissaoItemTotalSemExcluir() {
		PermissaoVO permissaoVO = (PermissaoVO) getRequestMap().get("permissaoItem");
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoPermissao(permissaoVO, OpcaoPermissaoEnum.TOTAL_SEM_EXCLUIR, getUsuarioLogado());
	}

	public void realizarMarcacaoPermissao() {
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoPermissao(getPermissaoVO(), null, getUsuarioLogado());
	}

	public void realizarMarcacaoPermissaoTotal() {
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoPermissao(getPermissaoVO(), OpcaoPermissaoEnum.TOTAL, getUsuarioLogado());
	}

	public void realizarMarcacaoPermissaoTotalSemExcluir() {
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoPermissao(getPermissaoVO(), OpcaoPermissaoEnum.TOTAL_SEM_EXCLUIR, getUsuarioLogado());
	}
	
	public void realizarMarcacaoFuncionalidadesPorEntidade() {
		PermissaoVO permissaoVO = (PermissaoVO) getRequestMap().get("permissaoItem");
		getFacadeFactory().getPerfilAcessoFacade().realizarMarcacaoFuncionalidadesPorEntidade(permissaoVO, permissaoVO.getMarcarFuncionalidadesPorEntidade(), getUsuarioLogado());
	}

	public List<SelectItem> getListaSelectItemFuncionalidade() {
		if (listaSelectItemFuncionalidade == null) {
			listaSelectItemFuncionalidade = new ArrayList<SelectItem>(0);
			listaSelectItemFuncionalidade.add(new SelectItem("naoAlterar", "Não Alterar"));
			listaSelectItemFuncionalidade.add(new SelectItem("sim", "Marcar Todos"));
			listaSelectItemFuncionalidade.add(new SelectItem("nao", "Desmarcar Todos"));
		}
		return listaSelectItemFuncionalidade;
	}
	
	public List<SelectItem> getListaSelectItemFiltrarPermissoes() {
		if (listaSelectItemFiltrarPermissoes == null) {
			listaSelectItemFiltrarPermissoes = new ArrayList<SelectItem>(0);
			listaSelectItemFiltrarPermissoes.add(new SelectItem("todos", "Todos"));
			listaSelectItemFiltrarPermissoes.add(new SelectItem("totalSelecionado", "Total Selecionado"));
			listaSelectItemFiltrarPermissoes.add(new SelectItem("totalNaoSelecionado", "Total Não Selecionado"));
		}
		return listaSelectItemFiltrarPermissoes;
	}

	public String getFiltroPermissao() {
		if (filtroPermissao == null) {
			filtroPermissao = "todos";
		}
		return filtroPermissao;
	}

	public void setFiltroPermissao(String filtroPermissao) {
		this.filtroPermissao = filtroPermissao;
	}
}
