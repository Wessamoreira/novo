package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas planoCursoForm.jsp
 * planoCursoCons.jsp) com as funcionalidades da classe <code>PlanoCurso</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see PlanoCurso
 * @see PlanoCursoVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.PlanoCursoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("PlanoCursoControle")
@Scope("request")
@Lazy
public class PlanoCursoControle extends SuperControle implements Serializable {
	private PlanoCursoVO planoCursoVO;
	private String campoConsultarDisciplina;
	private String valorConsultarDisciplina;
	private List listaConsultarDisciplina;

	public PlanoCursoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PlanoCurso</code> para edição pelo usuário
	 * da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setPlanoCursoVO(new PlanoCursoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PlanoCurso</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		PlanoCursoVO obj = (PlanoCursoVO) context().getExternalContext().getRequestMap().get("planoCurso");
		obj.setNovoObj(Boolean.FALSE);
		setPlanoCursoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PlanoCurso</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (planoCursoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPlanoCursoFacade().incluir(planoCursoVO);
			} else {
				getFacadeFactory().getPlanoCursoFacade().alterar(planoCursoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoCursoCons.jsp. Define o tipo de consulta a
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
				objs = getFacadeFactory().getPlanoCursoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("objetivosEspecificos")) {
				objs = getFacadeFactory().getPlanoCursoFacade().consultarPorObjetivosEspecificos(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("objetivosGerais")) {
				objs = getFacadeFactory().getPlanoCursoFacade().consultarPorObjetivosGerais(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("estrategiasAvaliacao")) {
				objs = getFacadeFactory().getPlanoCursoFacade().consultarPorEstrategiasAvaliacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("metodologia")) {
				objs = getFacadeFactory().getPlanoCursoFacade().consultarPorMetodologia(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getPlanoCursoFacade().consultarPorNomeDisciplina(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>PlanoCursoVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPlanoCursoFacade().excluir(planoCursoVO);
			novo();
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Disciplina</code> por meio dos parametros
	 * informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos
	 * parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarDisciplina().equals("codigo")) {
				if (getValorConsultarDisciplina().equals("")) {
					setValorConsultarDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultarDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultarDisciplina(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultarDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarDisciplina(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List getTipoConsultarComboDisciplina() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("objetivosEspecificos", "Objetivos Específicos"));
		itens.add(new SelectItem("objetivosGerais", "Objetivos Gerais"));
		itens.add(new SelectItem("estrategiasAvaliacao", "Estratégias de Avaliação"));
		itens.add(new SelectItem("metodologia", "Metodologia"));
		itens.add(new SelectItem("nomeDisciplina", "Disciplina"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
	 * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		planoCursoVO = null;
	}

	public String getCampoConsultarDisciplina() {
		return campoConsultarDisciplina;
	}

	public void setCampoConsultarDisciplina(String campoConsultarDisciplina) {
		this.campoConsultarDisciplina = campoConsultarDisciplina;
	}

	public String getValorConsultarDisciplina() {
		return valorConsultarDisciplina;
	}

	public void setValorConsultarDisciplina(String valorConsultarDisciplina) {
		this.valorConsultarDisciplina = valorConsultarDisciplina;
	}

	public List getListaConsultarDisciplina() {
		return listaConsultarDisciplina;
	}

	public void setListaConsultarDisciplina(List listaConsultarDisciplina) {
		this.listaConsultarDisciplina = listaConsultarDisciplina;
	}

	public PlanoCursoVO getPlanoCursoVO() {
		return planoCursoVO;
	}

	public void setPlanoCursoVO(PlanoCursoVO planoCursoVO) {
		this.planoCursoVO = planoCursoVO;
	}
}