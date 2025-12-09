package controle.extensao;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas cursoExtensaoForm.jsp
 * cursoExtensaoCons.jsp) com as funcionalidades da classe <code>CursoExtensao</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see CursoExtensao
 * @see CursoExtensaoVO
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
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.extensao.ClassificaoCursoExtensaoVO;
import negocio.comuns.extensao.CursoExtensaoVO;
import negocio.comuns.extensao.ProfessorCursoExtensaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("CursoExtensaoControle")
@Scope("request")
@Lazy
public class CursoExtensaoControle extends SuperControle implements Serializable {

	private CursoExtensaoVO cursoExtensaoVO;
	private String classificacaoCursoExtensao_Erro;
	private String unidadeEnsino_Erro;
	private ProfessorCursoExtensaoVO professorCursoExtensaoVO;
	private String pessoaProfessorCursoExtensao_Erro;

	public CursoExtensaoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>CursoExtensao</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setCursoExtensaoVO(new CursoExtensaoVO());
		setProfessorCursoExtensaoVO(new ProfessorCursoExtensaoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CursoExtensao</code> para alteração.
	 * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		CursoExtensaoVO obj = (CursoExtensaoVO) context().getExternalContext().getRequestMap().get("cursoExtensao");
		obj.setNovoObj(Boolean.FALSE);
		setCursoExtensaoVO(obj);
		setProfessorCursoExtensaoVO(new ProfessorCursoExtensaoVO());
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CursoExtensao</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (cursoExtensaoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getCursoExtensaoFacade().incluir(cursoExtensaoVO);
			} else {
				getFacadeFactory().getCursoExtensaoFacade().alterar(cursoExtensaoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP CursoExtensaoCons.jsp. Define o tipo de consulta
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
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cargaHoraria")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorCargaHoraria(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicial")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorDataInicial(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinal")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorDataFinal(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorSituacaoFinanceira(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeClassificaoCursoExtensao")) {
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorNomeClassificaoCursoExtensao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicialInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorDataInicialInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorDataFinalInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorComunidade")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorComunidade(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorAluno")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorAluno(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorFuncionario(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorProfessor")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorProfessor(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorInscricaoComunidade")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorInscricaoComunidade(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorInscricaoAluno")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorInscricaoAluno(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorInscricaoFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorInscricaoFuncionario(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorInscricaoProfessor")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorValorInscricaoProfessor(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getCursoExtensaoFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>CursoExtensaoVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getCursoExtensaoFacade().excluir(cursoExtensaoVO);
			setCursoExtensaoVO(new CursoExtensaoVO());

			setProfessorCursoExtensaoVO(new ProfessorCursoExtensaoVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>ProfessorCursoExtensao</code> para o objeto
	 * <code>cursoExtensaoVO</code> da classe <code>CursoExtensao</code>
	 */
	public String adicionarProfessorCursoExtensao() throws Exception {
		try {
			if (!getCursoExtensaoVO().getCodigo().equals(0)) {
				professorCursoExtensaoVO.setCursoExtensao(getCursoExtensaoVO().getCodigo());
			}
			if (getProfessorCursoExtensaoVO().getPessoaProfessorCursoExtensao().getCodigo().intValue() != 0) {
				Integer campoConsulta = getProfessorCursoExtensaoVO().getPessoaProfessorCursoExtensao().getCodigo();
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getProfessorCursoExtensaoVO().setPessoaProfessorCursoExtensao(pessoa);
			}
			getCursoExtensaoVO().adicionarObjProfessorCursoExtensaoVOs(getProfessorCursoExtensaoVO());
			this.setProfessorCursoExtensaoVO(new ProfessorCursoExtensaoVO());
			setMensagemID("msg_dados_adicionados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ProfessorCursoExtensao</code> para
	 * edição pelo usuário.
	 */
	public String editarProfessorCursoExtensao() throws Exception {
		ProfessorCursoExtensaoVO obj = (ProfessorCursoExtensaoVO) context().getExternalContext().getRequestMap().get("professorCursoExtensao");
		setProfessorCursoExtensaoVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ProfessorCursoExtensao</code> do objeto
	 * <code>cursoExtensaoVO</code> da classe <code>CursoExtensao</code>
	 */
	public String removerProfessorCursoExtensao() throws Exception {
		ProfessorCursoExtensaoVO obj = (ProfessorCursoExtensaoVO) context().getExternalContext().getRequestMap().get("professorCursoExtensao");
		getCursoExtensaoVO().excluirObjProfessorCursoExtensaoVOs(obj.getPessoaProfessorCursoExtensao().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return "editar";
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
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = professorCursoExtensaoVO.getPessoaProfessorCursoExtensao().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			professorCursoExtensaoVO.getPessoaProfessorCursoExtensao().setNome(pessoa.getNome());
			this.setPessoaProfessorCursoExtensao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			professorCursoExtensaoVO.getPessoaProfessorCursoExtensao().setNome("");
			professorCursoExtensaoVO.getPessoaProfessorCursoExtensao().setCodigo(0);
			this.setPessoaProfessorCursoExtensao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoProfessor</code>
	 */
	public List getListaSelectItemTipoProfessorProfessorCursoExtensao() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoProfessorProfessorCursoExtensaos = (Hashtable) Dominios.getTipoProfessorProfessorCursoExtensao();
		Enumeration keys = tipoProfessorProfessorCursoExtensaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoProfessorProfessorCursoExtensaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>situacaoFinanceira</code>
	 */
	public List getListaSelectItemSituacaoFinanceiraCursoExtensao() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFinanceiraExtensaos = (Hashtable) Dominios.getSituacaoFinanceiraExtensao();
		Enumeration keys = situacaoFinanceiraExtensaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFinanceiraExtensaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoCursoExtensao() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoCursoExtensaos = (Hashtable) Dominios.getSituacaoCursoExtensao();
		Enumeration keys = situacaoCursoExtensaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoCursoExtensaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>UnidadeEnsino</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarUnidadeEnsinoPorChavePrimaria() {
		try {
			Integer campoConsulta = cursoExtensaoVO.getUnidadeEnsino().getCodigo();
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			cursoExtensaoVO.getUnidadeEnsino().setNome(unidadeEnsino.getNome());
			this.setUnidadeEnsino_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			cursoExtensaoVO.getUnidadeEnsino().setNome("");
			cursoExtensaoVO.getUnidadeEnsino().setCodigo(0);
			this.setUnidadeEnsino_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>ClassificaoCursoExtensao</code> por meio de sua
	 * respectiva chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela
	 * chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarClassificaoCursoExtensaoPorChavePrimaria() {
		try {
			Integer campoConsulta = cursoExtensaoVO.getClassificacaoCursoExtensao().getCodigo();
			ClassificaoCursoExtensaoVO classificaoCursoExtensao = getFacadeFactory().getClassificacaoCursoExtensaoFacade().consultarPorChavePrimaria(campoConsulta,getUsuarioLogado());
			cursoExtensaoVO.getClassificacaoCursoExtensao().setNome(classificaoCursoExtensao.getNome());
			this.setClassificacaoCursoExtensao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			cursoExtensaoVO.getClassificacaoCursoExtensao().setNome("");
			cursoExtensaoVO.getClassificacaoCursoExtensao().setCodigo(0);
			this.setClassificacaoCursoExtensao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cargaHoraria", "Carga Horária"));
		itens.add(new SelectItem("dataInicial", "Data Inicial"));
		itens.add(new SelectItem("dataFinal", "Data Final"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
		itens.add(new SelectItem("nomeClassificaoCursoExtensao", "Classificação Curso Extensão"));
		itens.add(new SelectItem("dataInicialInscricao", "Data Inicial Inscrição"));
		itens.add(new SelectItem("dataFinalInscricao", "Data Final Inscrição"));
		itens.add(new SelectItem("valorComunidade", "Valor Comunidade"));
		itens.add(new SelectItem("valorAluno", "Valor Aluno"));
		itens.add(new SelectItem("valorFuncionario", "Valor Funcionário"));
		itens.add(new SelectItem("valorProfessor", "Valor Professor"));
		itens.add(new SelectItem("valorInscricaoComunidade", "Valor Inscrição Comunidade"));
		itens.add(new SelectItem("valorInscricaoAluno", "Valor Inscrição Aluno"));
		itens.add(new SelectItem("valorInscricaoFuncionario", "Valor Inscrição Funcionário"));
		itens.add(new SelectItem("valorInscricaoProfessor", "Valor Inscrição Professor"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
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

	public String getPessoaProfessorCursoExtensao_Erro() {
		return pessoaProfessorCursoExtensao_Erro;
	}

	public void setPessoaProfessorCursoExtensao_Erro(String pessoaProfessorCursoExtensao_Erro) {
		this.pessoaProfessorCursoExtensao_Erro = pessoaProfessorCursoExtensao_Erro;
	}

	public ProfessorCursoExtensaoVO getProfessorCursoExtensaoVO() {
		return professorCursoExtensaoVO;
	}

	public void setProfessorCursoExtensaoVO(ProfessorCursoExtensaoVO professorCursoExtensaoVO) {
		this.professorCursoExtensaoVO = professorCursoExtensaoVO;
	}

	public String getUnidadeEnsino_Erro() {
		return unidadeEnsino_Erro;
	}

	public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
		this.unidadeEnsino_Erro = unidadeEnsino_Erro;
	}

	public String getClassificacaoCursoExtensao_Erro() {
		return classificacaoCursoExtensao_Erro;
	}

	public void setClassificacaoCursoExtensao_Erro(String classificacaoCursoExtensao_Erro) {
		this.classificacaoCursoExtensao_Erro = classificacaoCursoExtensao_Erro;
	}

	public CursoExtensaoVO getCursoExtensaoVO() {
		return cursoExtensaoVO;
	}

	public void setCursoExtensaoVO(CursoExtensaoVO cursoExtensaoVO) {
		this.cursoExtensaoVO = cursoExtensaoVO;
	}
}