package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas pesquisadorLinhaPesquisaForm.jsp
 * pesquisadorLinhaPesquisaCons.jsp) com as funcionalidades da classe <code>PesquisadorLinhaPesquisa</code>. Implemtação
 * da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PesquisadorLinhaPesquisa
 * @see PesquisadorLinhaPesquisaVO
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
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.LinhaPesquisaVO;
import negocio.comuns.pesquisa.PesquisadorConvidadoVO;
import negocio.comuns.pesquisa.PesquisadorLinhaPesquisaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
 @Controller("PesquisadorLinhaPesquisaControle")
@Scope("request")
@Lazy
public class PesquisadorLinhaPesquisaControle extends SuperControle implements Serializable {

	private PesquisadorLinhaPesquisaVO pesquisadorLinhaPesquisaVO;
	private String pesquisadorProfessor_Erro;
	private String pesquisadorAluno_Erro;
	private String pesquisadorConvidado_Erro;
	private String linhaPesquisa_Erro;

	public PesquisadorLinhaPesquisaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PesquisadorLinhaPesquisa</code> para edição
	 * pelo usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setPesquisadorLinhaPesquisaVO(new PesquisadorLinhaPesquisaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PesquisadorLinhaPesquisa</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		PesquisadorLinhaPesquisaVO obj = (PesquisadorLinhaPesquisaVO) context().getExternalContext().getRequestMap().get("pesquisadorLinhaPesquisa");
		obj.setNovoObj(Boolean.FALSE);
		setPesquisadorLinhaPesquisaVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>PesquisadorLinhaPesquisa</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (pesquisadorLinhaPesquisaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPesquisadorLinhaPesquisaFacade().incluir(pesquisadorLinhaPesquisaVO);
			} else {
				getFacadeFactory().getPesquisadorLinhaPesquisaFacade().alterar(pesquisadorLinhaPesquisaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PesquisadorLinhaPesquisaCons.jsp. Define o tipo
	 * de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
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
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoPesquisador")) {
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorTipoPesquisador(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoPesquisadorConvidado")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorCodigoPesquisadorConvidado(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFiliacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorDataFiliacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeLinhaPesquisa")) {
				objs = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorNomeLinhaPesquisa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>PesquisadorLinhaPesquisaVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPesquisadorLinhaPesquisaFacade().excluir(pesquisadorLinhaPesquisaVO);
			setPesquisadorLinhaPesquisaVO(new PesquisadorLinhaPesquisaVO());
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
	 * <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoPesquisadorLinhaPesquisa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoPesquisadorLinhaPesquisas = (Hashtable) Dominios.getSituacaoPesquisadorLinhaPesquisa();
		Enumeration keys = situacaoPesquisadorLinhaPesquisas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoPesquisadorLinhaPesquisas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoPesquisador</code>
	 */
	public List getListaSelectItemTipoPesquisadorPesquisadorLinhaPesquisa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPesquisadorPesquisadorLinhaPesquisas = (Hashtable) Dominios.getTipoPesquisadorPesquisadorLinhaPesquisa();
		Enumeration keys = tipoPesquisadorPesquisadorLinhaPesquisas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPesquisadorPesquisadorLinhaPesquisas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>LinhaPesquisa</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarLinhaPesquisaPorChavePrimaria() {
		try {
			Integer campoConsulta = pesquisadorLinhaPesquisaVO.getLinhaPesquisa().getCodigo();
			LinhaPesquisaVO linhaPesquisa = getFacadeFactory().getLinhaPesquisaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			pesquisadorLinhaPesquisaVO.getLinhaPesquisa().setNome(linhaPesquisa.getNome());
			this.setLinhaPesquisa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorLinhaPesquisaVO.getLinhaPesquisa().setNome("");
			pesquisadorLinhaPesquisaVO.getLinhaPesquisa().setCodigo(0);
			this.setLinhaPesquisa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>PesquisadorConvidado</code> por meio de sua
	 * respectiva chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela
	 * chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPesquisadorConvidadoPorChavePrimaria() {
		try {
			Integer campoConsulta = pesquisadorLinhaPesquisaVO.getPesquisadorConvidado().getCodigo();
			PesquisadorConvidadoVO pesquisadorConvidado = getFacadeFactory().getPesquisadorConvidadoFacade().consultarPorChavePrimaria(campoConsulta,getUsuarioLogado());
			pesquisadorLinhaPesquisaVO.getPesquisadorConvidado().setCodigo(pesquisadorConvidado.getCodigo());
			this.setPesquisadorConvidado_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorLinhaPesquisaVO.getPesquisadorConvidado().setCodigo(0);
			pesquisadorLinhaPesquisaVO.getPesquisadorConvidado().setCodigo(0);
			this.setPesquisadorConvidado_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			String campoConsulta = pesquisadorLinhaPesquisaVO.getPesquisadorAluno().getMatricula();
			MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			pesquisadorLinhaPesquisaVO.getPesquisadorAluno().setMatricula(matricula.getMatricula());
			this.setPesquisadorAluno_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorLinhaPesquisaVO.getPesquisadorAluno().setMatricula("");
			pesquisadorLinhaPesquisaVO.getPesquisadorAluno().setMatricula("");
			this.setPesquisadorAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = pesquisadorLinhaPesquisaVO.getPesquisadorProfessor().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			pesquisadorLinhaPesquisaVO.getPesquisadorProfessor().setNome(pessoa.getNome());
			this.setPesquisadorProfessor_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorLinhaPesquisaVO.getPesquisadorProfessor().setNome("");
			pesquisadorLinhaPesquisaVO.getPesquisadorProfessor().setCodigo(0);
			this.setPesquisadorProfessor_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("tipoPesquisador", "Tipo Pesquisador"));
		itens.add(new SelectItem("nomePessoa", "Pesquisador Professor"));
		itens.add(new SelectItem("matriculaMatricula", "Pesquisador Aluno"));
		itens.add(new SelectItem("codigoPesquisadorConvidado", "Pesquisador Convidado"));
		itens.add(new SelectItem("dataFiliacao", "Data Filiação"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("nomeLinhaPesquisa", "Linha de Pesquisa"));
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

	public String getLinhaPesquisa_Erro() {
		return linhaPesquisa_Erro;
	}

	public void setLinhaPesquisa_Erro(String linhaPesquisa_Erro) {
		this.linhaPesquisa_Erro = linhaPesquisa_Erro;
	}

	public String getPesquisadorConvidado_Erro() {
		return pesquisadorConvidado_Erro;
	}

	public void setPesquisadorConvidado_Erro(String pesquisadorConvidado_Erro) {
		this.pesquisadorConvidado_Erro = pesquisadorConvidado_Erro;
	}

	public String getPesquisadorAluno_Erro() {
		return pesquisadorAluno_Erro;
	}

	public void setPesquisadorAluno_Erro(String pesquisadorAluno_Erro) {
		this.pesquisadorAluno_Erro = pesquisadorAluno_Erro;
	}

	public String getPesquisadorProfessor_Erro() {
		return pesquisadorProfessor_Erro;
	}

	public void setPesquisadorProfessor_Erro(String pesquisadorProfessor_Erro) {
		this.pesquisadorProfessor_Erro = pesquisadorProfessor_Erro;
	}

	public PesquisadorLinhaPesquisaVO getPesquisadorLinhaPesquisaVO() {
		return pesquisadorLinhaPesquisaVO;
	}

	public void setPesquisadorLinhaPesquisaVO(PesquisadorLinhaPesquisaVO pesquisadorLinhaPesquisaVO) {
		this.pesquisadorLinhaPesquisaVO = pesquisadorLinhaPesquisaVO;
	}
}