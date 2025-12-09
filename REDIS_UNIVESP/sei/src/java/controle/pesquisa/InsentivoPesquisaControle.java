package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas insentivoPesquisaForm.jsp
 * insentivoPesquisaCons.jsp) com as funcionalidades da classe <code>InsentivoPesquisa</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see InsentivoPesquisa
 * @see InsentivoPesquisaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.pesquisa.InsentivoPesquisaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("InsentivoPesquisaControle")
@Scope("request")
@Lazy
public class InsentivoPesquisaControle extends SuperControle implements Serializable {

	private InsentivoPesquisaVO insentivoPesquisaVO;

	public InsentivoPesquisaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>InsentivoPesquisa</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setInsentivoPesquisaVO(new InsentivoPesquisaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>InsentivoPesquisa</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		InsentivoPesquisaVO obj = (InsentivoPesquisaVO) context().getExternalContext().getRequestMap().get("insentivoPesquisa");
		obj.setNovoObj(Boolean.FALSE);
		setInsentivoPesquisaVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>InsentivoPesquisa</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (insentivoPesquisaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getIncentivoPesquisaFacade().incluir(insentivoPesquisaVO);
			} else {
				getFacadeFactory().getIncentivoPesquisaFacade().alterar(insentivoPesquisaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP InsentivoPesquisaCons.jsp. Define o tipo de
	 * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
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
				objs = getFacadeFactory().getIncentivoPesquisaFacade().consultarPorCodigo(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getIncentivoPesquisaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getIncentivoPesquisaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("orgaoPromotor")) {
				objs = getFacadeFactory().getIncentivoPesquisaFacade().consultarPorOrgaoPromotor(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicialInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getIncentivoPesquisaFacade().consultarPorDataInicialInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getIncentivoPesquisaFacade().consultarPorDataFinalInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>InsentivoPesquisaVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getIncentivoPesquisaFacade().excluir(insentivoPesquisaVO);
			setInsentivoPesquisaVO(new InsentivoPesquisaVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
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

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoInsentivo</code>
	 */
	public List getListaSelectItemTipoInsentivoInsentivoPesquisa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoInsentivoInsentivoPesquisas = (Hashtable) Dominios.getTipoInsentivoInsentivoPesquisa();
		Enumeration keys = tipoInsentivoInsentivoPesquisas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoInsentivoInsentivoPesquisas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("orgaoPromotor", "Orgão Promotor"));
		itens.add(new SelectItem("dataInicialInscricao", "Data Inícial Inscrição"));
		itens.add(new SelectItem("dataFinalInscricao", "Data Final Inscrição"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public InsentivoPesquisaVO getInsentivoPesquisaVO() {
		return insentivoPesquisaVO;
	}

	public void setInsentivoPesquisaVO(InsentivoPesquisaVO insentivoPesquisaVO) {
		this.insentivoPesquisaVO = insentivoPesquisaVO;
	}
}