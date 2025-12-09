package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas grupoPesquisaForm.jsp
 * grupoPesquisaCons.jsp) com as funcionalidades da classe <code>GrupoPesquisa</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see GrupoPesquisa
 * @see GrupoPesquisaVO
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
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.GrupoPesquisaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("GrupoPesquisaControle")
@Scope("request")
@Lazy
public class GrupoPesquisaControle extends SuperControle implements Serializable {

	private GrupoPesquisaVO grupoPesquisaVO;
	private String liderPrincipal_Erro;
	private String liderSecundario_Erro;
	private String areaConhecimento_Erro;
	private String unidadeEnsino_Erro;
	private String curso_Erro;

	public GrupoPesquisaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>GrupoPesquisa</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setGrupoPesquisaVO(new GrupoPesquisaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>GrupoPesquisa</code> para alteração.
	 * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		GrupoPesquisaVO obj = (GrupoPesquisaVO) context().getExternalContext().getRequestMap().get("grupoPesquisa");
		obj.setNovoObj(Boolean.FALSE);
		setGrupoPesquisaVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>GrupoPesquisa</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (grupoPesquisaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getGrupoPesquisaFacade().incluir(grupoPesquisaVO);
			} else {
				getFacadeFactory().getGrupoPesquisaFacade().alterar(grupoPesquisaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP GrupoPesquisaCons.jsp. Define o tipo de consulta
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
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataCriacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorDataCriacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorCodigoFuncionario(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorCodigoFuncionario(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeAreaConhecimento")) {
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorNomeAreaConhecimento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getGrupoPesquisaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>GrupoPesquisaVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getGrupoPesquisaFacade().excluir(grupoPesquisaVO);
			setGrupoPesquisaVO(new GrupoPesquisaVO());
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
	 * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = grupoPesquisaVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			grupoPesquisaVO.getCurso().setNome(curso.getNome());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			grupoPesquisaVO.getCurso().setNome("");
			grupoPesquisaVO.getCurso().setCodigo(0);
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>UnidadeEnsino</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarUnidadeEnsinoPorChavePrimaria() {
		try {
			Integer campoConsulta = grupoPesquisaVO.getUnidadeEnsino().getCodigo();
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			grupoPesquisaVO.getUnidadeEnsino().setNome(unidadeEnsino.getNome());
			this.setUnidadeEnsino_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			grupoPesquisaVO.getUnidadeEnsino().setNome("");
			grupoPesquisaVO.getUnidadeEnsino().setCodigo(0);
			this.setUnidadeEnsino_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>AreaConhecimento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarAreaConhecimentoPorChavePrimaria() {
		try {
			Integer campoConsulta = grupoPesquisaVO.getAreaConhecimento().getCodigo();
			AreaConhecimentoVO areaConhecimento = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(campoConsulta, getUsuarioLogado());
			grupoPesquisaVO.getAreaConhecimento().setNome(areaConhecimento.getNome());
			this.setAreaConhecimento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			grupoPesquisaVO.getAreaConhecimento().setNome("");
			grupoPesquisaVO.getAreaConhecimento().setCodigo(0);
			this.setAreaConhecimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Funcionario</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarFuncionarioPorChavePrimaria() {
		try {
			Integer campoConsulta = grupoPesquisaVO.getLiderPrincipal().getCodigo();
			FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			grupoPesquisaVO.getLiderPrincipal().setCodigo(funcionario.getCodigo());
			this.setLiderPrincipal_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			grupoPesquisaVO.getLiderPrincipal().setCodigo(0);
			grupoPesquisaVO.getLiderPrincipal().setCodigo(0);
			this.setLiderPrincipal_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("dataCriacao", "Data Criação"));
		itens.add(new SelectItem("codigoFuncionario", "Lider Principal"));
		itens.add(new SelectItem("codigoFuncionario", "Lider Secundario"));
		itens.add(new SelectItem("nomeAreaConhecimento", "Área de Conhecimento"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
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

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public String getUnidadeEnsino_Erro() {
		return unidadeEnsino_Erro;
	}

	public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
		this.unidadeEnsino_Erro = unidadeEnsino_Erro;
	}

	public String getAreaConhecimento_Erro() {
		return areaConhecimento_Erro;
	}

	public void setAreaConhecimento_Erro(String areaConhecimento_Erro) {
		this.areaConhecimento_Erro = areaConhecimento_Erro;
	}

	public String getLiderSecundario_Erro() {
		return liderSecundario_Erro;
	}

	public void setLiderSecundario_Erro(String liderSecundario_Erro) {
		this.liderSecundario_Erro = liderSecundario_Erro;
	}

	public String getLiderPrincipal_Erro() {
		return liderPrincipal_Erro;
	}

	public void setLiderPrincipal_Erro(String liderPrincipal_Erro) {
		this.liderPrincipal_Erro = liderPrincipal_Erro;
	}

	public GrupoPesquisaVO getGrupoPesquisaVO() {
		return grupoPesquisaVO;
	}

	public void setGrupoPesquisaVO(GrupoPesquisaVO grupoPesquisaVO) {
		this.grupoPesquisaVO = grupoPesquisaVO;
	}
}