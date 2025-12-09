package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas eventoForm.jsp eventoCons.jsp)
 * com as funcionalidades da classe <code>Evento</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Evento
 * @see EventoVO
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
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("EventoControle")
@Scope("request")
@Lazy
public class EventoControle extends SuperControle implements Serializable {

	private EventoVO eventoVO;
	private String responsavel_Erro;
	private String curso_Erro;
	private String unidadeEnsino_Erro;

	public EventoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Evento</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setEventoVO(new EventoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Evento</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		EventoVO obj = (EventoVO) context().getExternalContext().getRequestMap().get("evento");
		obj.setNovoObj(Boolean.FALSE);
		setEventoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Evento</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (eventoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getEventoFacade().incluir(eventoVO);
			} else {
				getFacadeFactory().getEventoFacade().alterar(eventoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP EventoCons.jsp. Define o tipo de consulta a ser
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
				objs = getFacadeFactory().getEventoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicioRealizacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorDataInicioRealizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalRealizacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorDataFinalRealizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicioInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorDataInicioInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalInscricao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorDataFinalInscricao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorAluno")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorAluno(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorProfessor")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorProfessor(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorFuncionario(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoInscricao")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorTipoInscricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrVagas")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorNrVagas(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrMaximoVagasExcedentes")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorNrMaximoVagasExcedentes(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("regrasFormatacaoTrabalho")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorRegrasFormatacaoTrabalho(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataInicialSubmissao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorDataInicialSubmissao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataFinalSubmissao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorDataFinalSubmissao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoSubmissao")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorTipoSubmissao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorSubmissaoAluno")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorSubmissaoAluno(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorSubmissaoProfessor")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorSubmissaoProfessor(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorSubmissaoFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorSubmissaoFuncionario(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorSubmissaoComunidade")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getEventoFacade().consultarPorValorSubmissaoComunidade(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getEventoFacade().consultarPorSituacaoFinanceira(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>EventoVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getEventoFacade().excluir(eventoVO);
			setEventoVO(new EventoVO());
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
	 * <code>situacaoFinanceira</code>
	 */
	public List getListaSelectItemSituacaoFinanceiraEvento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoFinanceiraEventos = (Hashtable) Dominios.getSituacaoFinanceiraEvento();
		Enumeration keys = situacaoFinanceiraEventos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFinanceiraEventos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoSubmissao</code>
	 */
	public List getListaSelectItemTipoSubmissaoEvento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoSubmissaoEventos = (Hashtable) Dominios.getTipoSubmissaoEvento();
		Enumeration keys = tipoSubmissaoEventos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoSubmissaoEventos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>formaInscricao</code>
	 */
	public List getListaSelectItemFormaInscricaoEvento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable formasInscricaoEventos = (Hashtable) Dominios.getFormasInscricaoEvento();
		Enumeration keys = formasInscricaoEventos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) formasInscricaoEventos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoInscricao</code>
	 */
	public List getListaSelectItemTipoInscricaoEvento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoInscricaoEventos = (Hashtable) Dominios.getTipoInscricaoEvento();
		Enumeration keys = tipoInscricaoEventos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoInscricaoEventos.get(value);
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
			Integer campoConsulta = eventoVO.getUnidadeEnsino().getCodigo();
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			eventoVO.getUnidadeEnsino().setNome(unidadeEnsino.getNome());
			this.setUnidadeEnsino_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			eventoVO.getUnidadeEnsino().setNome("");
			eventoVO.getUnidadeEnsino().setCodigo(0);
			this.setUnidadeEnsino_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = eventoVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
			eventoVO.getCurso().setNome(curso.getNome());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			eventoVO.getCurso().setNome("");
			eventoVO.getCurso().setCodigo(0);
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = eventoVO.getResponsavel().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			eventoVO.getResponsavel().setNome(pessoa.getNome());
			this.setResponsavel_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			eventoVO.getResponsavel().setNome("");
			eventoVO.getResponsavel().setCodigo(0);
			this.setResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("dataInicioRealizacao", "Data Início Realização"));
		itens.add(new SelectItem("dataFinalRealizacao", "Data Final Realização"));
		itens.add(new SelectItem("dataInicioInscricao", "Data Início Inscrição"));
		itens.add(new SelectItem("dataFinalInscricao", "Data Final Inscrição"));
		itens.add(new SelectItem("valorAluno", "Valor Aluno"));
		itens.add(new SelectItem("valorProfessor", "Valor Professor"));
		itens.add(new SelectItem("valorFuncionario", "Valor Funcionário"));
		itens.add(new SelectItem("nomePessoa", "Responsável"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("tipoInscricao", "Tipo Inscrição"));
		itens.add(new SelectItem("nrVagas", "Número de Vagas"));
		itens.add(new SelectItem("nrMaximoVagasExcedentes", "Número Máximo de Vagas Excedentes"));
		itens.add(new SelectItem("regrasFormatacaoTrabalho", "Regras Formatação Trabalho"));
		itens.add(new SelectItem("dataInicialSubmissao", "Data Inicial Submissão"));
		itens.add(new SelectItem("dataFinalSubmissao", "Data Final Submissão"));
		itens.add(new SelectItem("tipoSubmissao", "Tipo Submissão"));
		itens.add(new SelectItem("valorSubmissaoAluno", "Valor Submissão Aluno"));
		itens.add(new SelectItem("valorSubmissaoProfessor", "Valor Submissão Professor"));
		itens.add(new SelectItem("valorSubmissaoFuncionario", "Valor Submissão Funcionário"));
		itens.add(new SelectItem("valorSubmissaoComunidade", "Valor Submissão Comunidade"));
		itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
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

	public String getUnidadeEnsino_Erro() {
		return unidadeEnsino_Erro;
	}

	public void setUnidadeEnsino_Erro(String unidadeEnsino_Erro) {
		this.unidadeEnsino_Erro = unidadeEnsino_Erro;
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public String getResponsavel_Erro() {
		return responsavel_Erro;
	}

	public void setResponsavel_Erro(String responsavel_Erro) {
		this.responsavel_Erro = responsavel_Erro;
	}

	public EventoVO getEventoVO() {
		return eventoVO;
	}

	public void setEventoVO(EventoVO eventoVO) {
		this.eventoVO = eventoVO;
	}
}