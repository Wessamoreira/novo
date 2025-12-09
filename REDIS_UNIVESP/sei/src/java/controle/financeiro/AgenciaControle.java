package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas agenciaForm.jsp agenciaCons.jsp)
 * com as funcionalidades da classe <code>Agencia</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Agencia
 * @see AgenciaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.Agencia; @Controller("AgenciaControle")
@Scope("viewScope")
@Lazy
public class AgenciaControle extends SuperControle implements Serializable {

	private AgenciaVO agenciaVO;
	protected List listaSelectItemBanco;
	protected List listaSelectItemCidade;
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected List listaConsultaCidade;

	public AgenciaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Agencia</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setAgenciaVO(new AgenciaVO());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Agencia</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			AgenciaVO obj = (AgenciaVO) context().getExternalContext().getRequestMap().get("agenciaItens");
			Agencia.montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			setAgenciaVO(obj);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaCons.xhtml");
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Agencia</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (agenciaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getAgenciaFacade().incluir(agenciaVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getAgenciaFacade().alterar(agenciaVO, getUsuarioLogado());
				getAplicacaoControle().atualizarAgenciaContaCorrente(agenciaVO);
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP AgenciaCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
	 * disponibiliza um List com os objetos selecionados na sessao da pagina.
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
				objs = getFacadeFactory().getAgenciaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getAgenciaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("razaoSocial")) {
				objs = getFacadeFactory().getAgenciaFacade().consultarPorRazaoSocial(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("numeroAgencia")) {
				objs = getFacadeFactory().getAgenciaFacade().consultarPorNumeroAgencia(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("gerente")) {
				objs = getFacadeFactory().getAgenciaFacade().consultarPorGerente(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeBanco")) {
				objs = getFacadeFactory().getAgenciaFacade().consultarPorNomeBanco(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaCons.xhtml");
		}
	}
	
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>AgenciaVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getAgenciaFacade().excluir(agenciaVO, getUsuarioLogado());
			setAgenciaVO(new AgenciaVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaForm.xhtml");
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

	public String consultarCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");
			return "";

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getAgenciaVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	public void limparConsultaCidade() {
		getAgenciaVO().setCidade(new CidadeVO());
	}

	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
	 */

	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Banco</code>.
	 */
	public void montarListaSelectItemBanco(String prm) throws Exception {
            List resultadoConsulta = null;
            Iterator i = null;
            try {
                resultadoConsulta = consultarBancoPorNome(prm);
                i = resultadoConsulta.iterator();
                List objs = new ArrayList(0);
                objs.add(new SelectItem(0, ""));
                while (i.hasNext()) {
                    BancoVO obj = (BancoVO) i.next();
                    objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
                }
                setListaSelectItemBanco(objs);
            } catch (Exception e) {
                throw e;
            } finally {
                Uteis.liberarListaMemoria(resultadoConsulta);
                i = null;
            }
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Banco</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Banco</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemBanco() {
		try {
			montarListaSelectItemBanco("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarBancoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getBancoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(agenciaVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemBanco();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("numeroAgencia", "Número Agência"));
		itens.add(new SelectItem("gerente", "Gerente"));
		itens.add(new SelectItem("nomeBanco", "Banco"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("agenciaCons.xhtml");
	}

	public List getListaSelectItemCidade() {
		return listaSelectItemCidade;
	}

	public void setListaSelectItemCidade(List listaSelectItemCidade) {
		this.listaSelectItemCidade = listaSelectItemCidade;
	}

	public List getListaSelectItemBanco() {
		return (listaSelectItemBanco);
	}

	public void setListaSelectItemBanco(List listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}

	public AgenciaVO getAgenciaVO() {
		if(agenciaVO == null) {
			agenciaVO = new AgenciaVO();
		}
		return agenciaVO;
	}

	public void setAgenciaVO(AgenciaVO agenciaVO) {
		this.agenciaVO = agenciaVO;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		agenciaVO = null;
		Uteis.liberarListaMemoria(listaSelectItemBanco);
		Uteis.liberarListaMemoria(listaConsultaCidade);
		campoConsultaCidade = null;
		valorConsultaCidade = null;
	}

	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade
	 *            the campoConsultaCidade to set
	 */
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	/**
	 * @return the valorConsultaCidade
	 */
	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}

	/**
	 * @param valorConsultaCidade
	 *            the valorConsultaCidade to set
	 */
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	/**
	 * @return the listaConsultaCidade
	 */
	public List getListaConsultaCidade() {
		if(listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<>();
		}
		return listaConsultaCidade;
	}

	/**
	 * @param listaConsultaCidade
	 *            the listaConsultaCidade to set
	 */
	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}
}
