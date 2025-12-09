package controle.compras;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas previsaoCustosForm.jsp
 * previsaoCustosCons.jsp) com as funcionalidades da classe <code>PrevisaoCustos</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see PrevisaoCustos
 * @see PrevisaoCustosVO
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
import negocio.comuns.compras.ClassificaoCustosVO;
import negocio.comuns.compras.PrevisaoCustosVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.extensao.CursoExtensaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("PrevisaoCustosControle")
@Scope("request")
@Lazy
public class PrevisaoCustosControle extends SuperControle implements Serializable {

	private PrevisaoCustosVO previsaoCustosVO;
	private String classificaoCustos_Erro;
	private String responsavelRequisicao_Erro;
	private String autorizacaoCustos_Erro;
	private String curso_Erro;
	private String evento_Erro;
	private String cursoExtensao_Erro;
	private String unidadeEnsino_Erro;

	public PrevisaoCustosControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PrevisaoCustos</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setPrevisaoCustosVO(new PrevisaoCustosVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PrevisaoCustos</code> para alteração.
	 * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		PrevisaoCustosVO obj = (PrevisaoCustosVO) context().getExternalContext().getRequestMap().get("previsaoCustos");
		obj.setNovoObj(Boolean.FALSE);
		setPrevisaoCustosVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PrevisaoCustos</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (previsaoCustosVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPrevisaoCustosFacade().incluir(previsaoCustosVO);
			} else {
				getFacadeFactory().getPrevisaoCustosFacade().alterar(previsaoCustosVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PrevisaoCustosCons.jsp. Define o tipo de consulta
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
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricaoClassificaoCustos")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorDescricaoClassificaoCustos(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeEvento")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorNomeEvento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCursoExtensao")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorNomeCursoExtensao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorEstimado")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorValorEstimado(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorGasto")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorValorGasto(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoDestinacaoCusto")) {
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorTipoDestinacaoCusto(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cargaHoraria")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorCargaHoraria(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorPagamentoHora")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPrevisaoCustosFacade().consultarPorValorPagamentoHora(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>PrevisaoCustosVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPrevisaoCustosFacade().excluir(previsaoCustosVO);
			setPrevisaoCustosVO(new PrevisaoCustosVO());
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
	 * <code>tipoDestinacaoCusto</code>
	 */
	public List getListaSelectItemTipoDestinacaoCustoPrevisaoCustos() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoDestinacaoCustosPrevisaoCustoss = (Hashtable) Dominios.getTipoDestinacaoCustosPrevisaoCustos();
		Enumeration keys = tipoDestinacaoCustosPrevisaoCustoss.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDestinacaoCustosPrevisaoCustoss.get(value);
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
			Integer campoConsulta = previsaoCustosVO.getUnidadeEnsino().getCodigo();
			UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			previsaoCustosVO.getUnidadeEnsino().setNome(unidadeEnsino.getNome());
			this.setUnidadeEnsino_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			previsaoCustosVO.getUnidadeEnsino().setNome("");
			previsaoCustosVO.getUnidadeEnsino().setCodigo(0);
			this.setUnidadeEnsino_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>CursoExtensao</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursoExtensaoPorChavePrimaria() {
		try {
			Integer campoConsulta = previsaoCustosVO.getCursoExtensao().getCodigo();
			CursoExtensaoVO cursoExtensao = getFacadeFactory().getCursoExtensaoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			previsaoCustosVO.getCursoExtensao().setNome(cursoExtensao.getNome());
			this.setCursoExtensao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			previsaoCustosVO.getCursoExtensao().setNome("");
			previsaoCustosVO.getCursoExtensao().setCodigo(0);
			this.setCursoExtensao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Evento</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = previsaoCustosVO.getEvento().getCodigo();
			EventoVO evento = getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			previsaoCustosVO.getEvento().setNome(evento.getNome());
			this.setEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			previsaoCustosVO.getEvento().setNome("");
			previsaoCustosVO.getEvento().setCodigo(0);
			this.setEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = previsaoCustosVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
			previsaoCustosVO.getCurso().setNome(curso.getNome());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			previsaoCustosVO.getCurso().setNome("");
			previsaoCustosVO.getCurso().setCodigo(0);
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
			Integer campoConsulta = previsaoCustosVO.getResponsavelRequisicao().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			previsaoCustosVO.getResponsavelRequisicao().setNome(pessoa.getNome());
			this.setResponsavelRequisicao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			previsaoCustosVO.getResponsavelRequisicao().setNome("");
			previsaoCustosVO.getResponsavelRequisicao().setCodigo(0);
			this.setResponsavelRequisicao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>ClassificaoCustos</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarClassificaoCustosPorChavePrimaria() {
		try {
			Integer campoConsulta = previsaoCustosVO.getClassificaoCustos().getCodigo();
			ClassificaoCustosVO classificaoCustos = getFacadeFactory().getClassificacaoCustosFacade().consultarPorChavePrimaria(campoConsulta, false, getUsuarioLogado());
			previsaoCustosVO.getClassificaoCustos().setDescricao(classificaoCustos.getDescricao());
			this.setClassificaoCustos_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			previsaoCustosVO.getClassificaoCustos().setDescricao("");
			previsaoCustosVO.getClassificaoCustos().setCodigo(0);
			this.setClassificaoCustos_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("descricaoClassificaoCustos", "Classifição Custos"));
		itens.add(new SelectItem("nomePessoa", "Responsável Requisição"));
		itens.add(new SelectItem("nomePessoa", "Autorização Custos"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("nomeEvento", "Evento"));
		itens.add(new SelectItem("nomeCursoExtensao", "Curso de Extensão"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("valorEstimado", "Valor Estimado"));
		itens.add(new SelectItem("valorGasto", "Valor Gasto"));
		itens.add(new SelectItem("tipoDestinacaoCusto", "Tipo Destinação Custo"));
		itens.add(new SelectItem("cargaHoraria", "Carga Horária"));
		itens.add(new SelectItem("valorPagamentoHora", "Valor Pagamento por Hora"));
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

	public String getCursoExtensao_Erro() {
		return cursoExtensao_Erro;
	}

	public void setCursoExtensao_Erro(String cursoExtensao_Erro) {
		this.cursoExtensao_Erro = cursoExtensao_Erro;
	}

	public String getEvento_Erro() {
		return evento_Erro;
	}

	public void setEvento_Erro(String evento_Erro) {
		this.evento_Erro = evento_Erro;
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public String getAutorizacaoCustos_Erro() {
		return autorizacaoCustos_Erro;
	}

	public void setAutorizacaoCustos_Erro(String autorizacaoCustos_Erro) {
		this.autorizacaoCustos_Erro = autorizacaoCustos_Erro;
	}

	public String getResponsavelRequisicao_Erro() {
		return responsavelRequisicao_Erro;
	}

	public void setResponsavelRequisicao_Erro(String responsavelRequisicao_Erro) {
		this.responsavelRequisicao_Erro = responsavelRequisicao_Erro;
	}

	public String getClassificaoCustos_Erro() {
		return classificaoCustos_Erro;
	}

	public void setClassificaoCustos_Erro(String classificaoCustos_Erro) {
		this.classificaoCustos_Erro = classificaoCustos_Erro;
	}

	public PrevisaoCustosVO getPrevisaoCustosVO() {
		return previsaoCustosVO;
	}

	public void setPrevisaoCustosVO(PrevisaoCustosVO previsaoCustosVO) {
		this.previsaoCustosVO = previsaoCustosVO;
	}
}