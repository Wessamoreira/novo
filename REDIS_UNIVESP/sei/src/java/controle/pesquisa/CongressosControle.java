package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas congressosForm.jsp
 * congressosCons.jsp) com as funcionalidades da classe <code>Congressos</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Congressos
 * @see CongressosVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.pesquisa.CongressosVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("CongressosControle")
@Scope("request")
@Lazy
public class CongressosControle extends SuperControle implements Serializable {

	private CongressosVO congressosVO;

	public CongressosControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Congressos</code> para edição pelo usuário
	 * da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setCongressosVO(new CongressosVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Congressos</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		CongressosVO obj = (CongressosVO) context().getExternalContext().getRequestMap().get("congressos");
		obj.setNovoObj(Boolean.FALSE);
		setCongressosVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Congressos</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (congressosVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getCongressosFacade().incluir(congressosVO);
			} else {
				getFacadeFactory().getCongressosFacade().alterar(congressosVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP CongressosCons.jsp. Define o tipo de consulta a
	 * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
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
				objs = getFacadeFactory().getCongressosFacade().consultarPorCodigo(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getCongressosFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicialRealizacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCongressosFacade().consultarPorDataInicialRealizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalRealizacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCongressosFacade().consultarPorDataFinalRealizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicialInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCongressosFacade().consultarPorDataInicialInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCongressosFacade().consultarPorDataFinalInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("promotor")) {
				objs = getFacadeFactory().getCongressosFacade().consultarPorPromotor(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("site")) {
				objs = getFacadeFactory().getCongressosFacade().consultarPorSite(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("localRealizacao")) {
				objs = getFacadeFactory().getCongressosFacade().consultarPorLocalRealizacao(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>CongressosVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getCongressosFacade().excluir(congressosVO);
			setCongressosVO(new CongressosVO());
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

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("dataInicialRealizacao", "Data Inícial Realização"));
		itens.add(new SelectItem("dataFinalRealizacao", "Data Final Realização"));
		itens.add(new SelectItem("dataInicialInscricao", "Data Inícial Inscrição"));
		itens.add(new SelectItem("dataFinalInscricao", "Data Final Inscrição"));
		itens.add(new SelectItem("promotor", "Promotor"));
		itens.add(new SelectItem("site", "Site"));
		itens.add(new SelectItem("localRealizacao", "Local Realização"));
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

	public CongressosVO getCongressosVO() {
		return congressosVO;
	}

	public void setCongressosVO(CongressosVO congressosVO) {
		this.congressosVO = congressosVO;
	}
}