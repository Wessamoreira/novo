package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas publicacaoPesquisaForm.jsp
 * publicacaoPesquisaCons.jsp) com as funcionalidades da classe <code>PublicacaoPesquisa</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PublicacaoPesquisa
 * @see PublicacaoPesquisaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.pesquisa.LinhaPesquisaVO;
import negocio.comuns.pesquisa.ProjetoPesquisaVO;
import negocio.comuns.pesquisa.PublicacaoPesquisaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados; @Controller("PublicacaoPesquisaControle")
@Scope("request")
@Lazy
public class PublicacaoPesquisaControle extends SuperControle implements Serializable {

	private PublicacaoPesquisaVO publicacaoPesquisaVO;
	private String autorProfessor_Erro;
	private String autorAluno_Erro;
	private String orientador_Erro;
	private String curso_Erro;
	private String unidadeEnsinso_Erro;
	private String projetoPesquisa_Erro;
	private String linhaPesquisa_Erro;

	public PublicacaoPesquisaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PublicacaoPesquisa</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setPublicacaoPesquisaVO(new PublicacaoPesquisaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PublicacaoPesquisa</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		PublicacaoPesquisaVO obj = (PublicacaoPesquisaVO) context().getExternalContext().getRequestMap().get("publicacaoPesquisa");
		obj.setNovoObj(Boolean.FALSE);
		setPublicacaoPesquisaVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PublicacaoPesquisa</code>
	 * . Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário
	 * é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (publicacaoPesquisaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPublicacaoPesquisaFacade().incluir(publicacaoPesquisaVO);
			} else {
				getFacadeFactory().getPublicacaoPesquisaFacade().alterar(publicacaoPesquisaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PublicacaoPesquisaCons.jsp. Define o tipo de
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
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matriculaMatricula")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tituloPublicacao")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorTituloPublicacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoPesquisador")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorTipoPesquisador(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoPublicacao")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorTipoPublicacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeProjetoPesquisa")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorNomeProjetoPesquisa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeLinhaPesquisa")) {
				objs = getFacadeFactory().getPublicacaoPesquisaFacade().consultarPorNomeLinhaPesquisa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>PublicacaoPesquisaVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPublicacaoPesquisaFacade().excluir(publicacaoPesquisaVO);
			setPublicacaoPesquisaVO(new PublicacaoPesquisaVO());
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
	 * <code>tipoPublicacao</code>
	 */
	public List getListaSelectItemTipoPublicacaoPublicacaoPesquisa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPublicacaoPublicacaoPesquisas = (Hashtable) Dominios.getTipoPublicacaoPublicacaoPesquisa();
		Enumeration keys = tipoPublicacaoPublicacaoPesquisas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPublicacaoPublicacaoPesquisas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoPesquisador</code>
	 */
	public List getListaSelectItemTipoPesquisadorPublicacaoPesquisa() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPesquisadorPublicacaos = (Hashtable) Dominios.getTipoPesquisadorPublicacao();
		Enumeration keys = tipoPesquisadorPublicacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPesquisadorPublicacaos.get(value);
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
			Integer campoConsulta = publicacaoPesquisaVO.getLinhaPesquisa().getCodigo();
			LinhaPesquisaVO linhaPesquisa = getFacadeFactory().getLinhaPesquisaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			publicacaoPesquisaVO.getLinhaPesquisa().setNome(linhaPesquisa.getNome());
			this.setLinhaPesquisa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			publicacaoPesquisaVO.getLinhaPesquisa().setNome("");
			publicacaoPesquisaVO.getLinhaPesquisa().setCodigo(0);
			this.setLinhaPesquisa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>ProjetoPesquisa</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarProjetoPesquisaPorChavePrimaria() {
		try {
			Integer campoConsulta = publicacaoPesquisaVO.getProjetoPesquisa().getCodigo();
			ProjetoPesquisaVO projetoPesquisa = getFacadeFactory().getProjetoPesquisaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			publicacaoPesquisaVO.getProjetoPesquisa().setNome(projetoPesquisa.getNome());
			this.setProjetoPesquisa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			publicacaoPesquisaVO.getProjetoPesquisa().setNome("");
			publicacaoPesquisaVO.getProjetoPesquisa().setCodigo(0);
			this.setProjetoPesquisa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>UnidadeEnsino</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarUnidadeEnsinoPorChavePrimaria() {
		try {
			Integer campoConsulta = publicacaoPesquisaVO.getUnidadeEnsinso().getCodigo();
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			publicacaoPesquisaVO.getUnidadeEnsinso().setNome(unidadeEnsino.getNome());
			this.setUnidadeEnsinso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			publicacaoPesquisaVO.getUnidadeEnsinso().setNome("");
			publicacaoPesquisaVO.getUnidadeEnsinso().setCodigo(0);
			this.setUnidadeEnsinso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = publicacaoPesquisaVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
			publicacaoPesquisaVO.getCurso().setNome(curso.getNome());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			publicacaoPesquisaVO.getCurso().setNome("");
			publicacaoPesquisaVO.getCurso().setCodigo(0);
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			String campoConsulta = publicacaoPesquisaVO.getAutorAluno().getMatricula();
			MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			publicacaoPesquisaVO.getAutorAluno().setMatricula(matricula.getMatricula());
			this.setAutorAluno_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			publicacaoPesquisaVO.getAutorAluno().setMatricula("");
			publicacaoPesquisaVO.getAutorAluno().setMatricula("");
			this.setAutorAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = publicacaoPesquisaVO.getAutorProfessor().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			publicacaoPesquisaVO.getAutorProfessor().setNome(pessoa.getNome());
			this.setAutorProfessor_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			publicacaoPesquisaVO.getAutorProfessor().setNome("");
			publicacaoPesquisaVO.getAutorProfessor().setCodigo(0);
			this.setAutorProfessor_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomePessoa", "Autor Professor"));
		itens.add(new SelectItem("matriculaMatricula", "Autor Aluno"));
		itens.add(new SelectItem("nomePessoa", "Orientador"));
		itens.add(new SelectItem("tituloPublicacao", "Título Publicação"));
		itens.add(new SelectItem("tipoPesquisador", "Tipo Pesquisador"));
		itens.add(new SelectItem("tipoPublicacao", "Tipo Publicação"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensinso"));
		itens.add(new SelectItem("nomeProjetoPesquisa", "Projeto de Pesquisa"));
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

	public String getProjetoPesquisa_Erro() {
		return projetoPesquisa_Erro;
	}

	public void setProjetoPesquisa_Erro(String projetoPesquisa_Erro) {
		this.projetoPesquisa_Erro = projetoPesquisa_Erro;
	}

	public String getUnidadeEnsinso_Erro() {
		return unidadeEnsinso_Erro;
	}

	public void setUnidadeEnsinso_Erro(String unidadeEnsinso_Erro) {
		this.unidadeEnsinso_Erro = unidadeEnsinso_Erro;
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public String getOrientador_Erro() {
		return orientador_Erro;
	}

	public void setOrientador_Erro(String orientador_Erro) {
		this.orientador_Erro = orientador_Erro;
	}

	public String getAutorAluno_Erro() {
		return autorAluno_Erro;
	}

	public void setAutorAluno_Erro(String autorAluno_Erro) {
		this.autorAluno_Erro = autorAluno_Erro;
	}

	public String getAutorProfessor_Erro() {
		return autorProfessor_Erro;
	}

	public void setAutorProfessor_Erro(String autorProfessor_Erro) {
		this.autorProfessor_Erro = autorProfessor_Erro;
	}

	public PublicacaoPesquisaVO getPublicacaoPesquisaVO() {
		return publicacaoPesquisaVO;
	}

	public void setPublicacaoPesquisaVO(PublicacaoPesquisaVO publicacaoPesquisaVO) {
		this.publicacaoPesquisaVO = publicacaoPesquisaVO;
	}
}