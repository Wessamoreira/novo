package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas linhaPesquisaForm.jsp
 * linhaPesquisaCons.jsp) com as funcionalidades da classe <code>LinhaPesquisa</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see LinhaPesquisa
 * @see LinhaPesquisaVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.GrupoPesquisaVO;
import negocio.comuns.pesquisa.LinhaPesquisaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("LinhaPesquisaControle")
@Scope("request")
@Lazy
public class LinhaPesquisaControle extends SuperControle implements Serializable {

	private LinhaPesquisaVO linhaPesquisaVO;
	private String lider_Erro;
	private String grupoPesquisa_Erro;
	private String areaConhecimento_Erro;

	public LinhaPesquisaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>LinhaPesquisa</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setLinhaPesquisaVO(new LinhaPesquisaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>LinhaPesquisa</code> para alteração.
	 * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		LinhaPesquisaVO obj = (LinhaPesquisaVO) context().getExternalContext().getRequestMap().get("linhaPesquisa");
		obj.setNovoObj(Boolean.FALSE);
		setLinhaPesquisaVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>LinhaPesquisa</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (linhaPesquisaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getLinhaPesquisaFacade().incluir(linhaPesquisaVO);
			} else {
				getFacadeFactory().getLinhaPesquisaFacade().alterar(linhaPesquisaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP LinhaPesquisaCons.jsp. Define o tipo de consulta
	 * a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
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
				objs = getFacadeFactory().getLinhaPesquisaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getLinhaPesquisaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getLinhaPesquisaFacade().consultarPorCodigoFuncionario(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeGrupoPesquisa")) {
				objs = getFacadeFactory().getLinhaPesquisaFacade().consultarPorNomeGrupoPesquisa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				objs = getFacadeFactory().getLinhaPesquisaFacade().consultarPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>LinhaPesquisaVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getLinhaPesquisaFacade().excluir(linhaPesquisaVO);
			setLinhaPesquisaVO(new LinhaPesquisaVO());
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
	 * Método responsável por processar a consulta na entidade <code>AreaConhecimento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarAreaConhecimentoPorChavePrimaria() {
		try {
			Integer campoConsulta = linhaPesquisaVO.getAreaConhecimento().getCodigo();
			AreaConhecimentoVO areaConhecimento = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(campoConsulta,getUsuarioLogado());
			linhaPesquisaVO.getAreaConhecimento().setNome(areaConhecimento.getNome());
			this.setAreaConhecimento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			linhaPesquisaVO.getAreaConhecimento().setNome("");
			linhaPesquisaVO.getAreaConhecimento().setCodigo(0);
			this.setAreaConhecimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>GrupoPesquisa</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarGrupoPesquisaPorChavePrimaria() {
		try {
			Integer campoConsulta = linhaPesquisaVO.getGrupoPesquisa().getCodigo();
			GrupoPesquisaVO grupoPesquisa = getFacadeFactory().getGrupoPesquisaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			linhaPesquisaVO.getGrupoPesquisa().setNome(grupoPesquisa.getNome());
			this.setGrupoPesquisa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			linhaPesquisaVO.getGrupoPesquisa().setNome("");
			linhaPesquisaVO.getGrupoPesquisa().setCodigo(0);
			this.setGrupoPesquisa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Funcionario</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarFuncionarioPorChavePrimaria() {
		try {
			Integer campoConsulta = linhaPesquisaVO.getLider().getCodigo();
			FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			linhaPesquisaVO.getLider().setCodigo(funcionario.getCodigo());
			this.setLider_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			linhaPesquisaVO.getLider().setCodigo(0);
			linhaPesquisaVO.getLider().setCodigo(0);
			this.setLider_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigoFuncionario", "Lider"));
		itens.add(new SelectItem("nomeGrupoPesquisa", "Grupo de Pesquisa"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área de Conhecimento"));
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

	public String getAreaConhecimento_Erro() {
		return areaConhecimento_Erro;
	}

	public void setAreaConhecimento_Erro(String areaConhecimento_Erro) {
		this.areaConhecimento_Erro = areaConhecimento_Erro;
	}

	public String getGrupoPesquisa_Erro() {
		return grupoPesquisa_Erro;
	}

	public void setGrupoPesquisa_Erro(String grupoPesquisa_Erro) {
		this.grupoPesquisa_Erro = grupoPesquisa_Erro;
	}

	public String getLider_Erro() {
		return lider_Erro;
	}

	public void setLider_Erro(String lider_Erro) {
		this.lider_Erro = lider_Erro;
	}

	public LinhaPesquisaVO getLinhaPesquisaVO() {
		return linhaPesquisaVO;
	}

	public void setLinhaPesquisaVO(LinhaPesquisaVO linhaPesquisaVO) {
		this.linhaPesquisaVO = linhaPesquisaVO;
	}
}