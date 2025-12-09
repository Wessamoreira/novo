package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas inscricaoEventoForm.jsp
 * inscricaoEventoCons.jsp) com as funcionalidades da classe <code>InscricaoEvento</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see InscricaoEvento
 * @see InscricaoEventoVO
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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.CursosEventoVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.eventos.InscricaoCursoEventoVO;
import negocio.comuns.eventos.InscricaoEventoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("InscricaoEventoControle")
@Scope("request")
@Lazy
public class InscricaoEventoControle extends SuperControle implements Serializable {

	private InscricaoEventoVO inscricaoEventoVO;
	private String evento_Erro;
	private String pessoaInscricao_Erro;
	private InscricaoCursoEventoVO inscricaoCursoEventoVO;
	private String cursosEvento_Erro;

	public InscricaoEventoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>InscricaoEvento</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setInscricaoEventoVO(new InscricaoEventoVO());
		setInscricaoCursoEventoVO(new InscricaoCursoEventoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>InscricaoEvento</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		InscricaoEventoVO obj = (InscricaoEventoVO) context().getExternalContext().getRequestMap().get("inscricaoEvento");
		obj.setNovoObj(Boolean.FALSE);
		setInscricaoEventoVO(obj);
		setInscricaoCursoEventoVO(new InscricaoCursoEventoVO());
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>InscricaoEvento</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (inscricaoEventoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getInscricaoEventoFacade().incluir(inscricaoEventoVO);
			} else {
				getFacadeFactory().getInscricaoEventoFacade().alterar(inscricaoEventoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP InscricaoEventoCons.jsp. Define o tipo de
	 * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("nrInscricao")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoEventoFacade().consultarPorNrInscricao(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeEvento")) {
				objs = getFacadeFactory().getInscricaoEventoFacade().consultarPorNomeEvento(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoEventoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorTotal")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoEventoFacade().consultarPorValorTotal(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoInscricao")) {
				objs = getFacadeFactory().getInscricaoEventoFacade().consultarPorTipoInscricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>InscricaoEventoVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getInscricaoEventoFacade().excluir(inscricaoEventoVO);
			setInscricaoEventoVO(new InscricaoEventoVO());

			setInscricaoCursoEventoVO(new InscricaoCursoEventoVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>InscricaoCursoEvento</code> para o objeto
	 * <code>inscricaoEventoVO</code> da classe <code>InscricaoEvento</code>
	 */
	public String adicionarInscricaoCursoEvento() throws Exception {
		try {
			if (!getInscricaoEventoVO().getNrInscricao().equals(0)) {
				inscricaoCursoEventoVO.setInscricaoEvento(getInscricaoEventoVO().getNrInscricao());
			}
			if (getInscricaoCursoEventoVO().getCursosEvento().getCodigo().intValue() != 0) {
				Integer campoConsulta = getInscricaoCursoEventoVO().getCursosEvento().getCodigo();
				CursosEventoVO cursosEvento = getFacadeFactory().getCursosEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getInscricaoCursoEventoVO().setCursosEvento(cursosEvento);
			}
			getInscricaoEventoVO().adicionarObjInscricaoCursoEventoVOs(getInscricaoCursoEventoVO());
			this.setInscricaoCursoEventoVO(new InscricaoCursoEventoVO());
			setMensagemID("msg_dados_adicionados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>InscricaoCursoEvento</code> para edição
	 * pelo usuário.
	 */
	public String editarInscricaoCursoEvento() throws Exception {
		InscricaoCursoEventoVO obj = (InscricaoCursoEventoVO) context().getExternalContext().getRequestMap().get("inscricaoCursoEvento");
		setInscricaoCursoEventoVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>InscricaoCursoEvento</code> do objeto
	 * <code>inscricaoEventoVO</code> da classe <code>InscricaoEvento</code>
	 */
	public String removerInscricaoCursoEvento() throws Exception {
		InscricaoCursoEventoVO obj = (InscricaoCursoEventoVO) context().getExternalContext().getRequestMap().get("inscricaoCursoEvento");
		getInscricaoEventoVO().excluirObjInscricaoCursoEventoVOs(obj.getCursosEvento().getCodigo());
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
	 * Método responsável por processar a consulta na entidade <code>CursosEvento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursosEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = inscricaoCursoEventoVO.getCursosEvento().getCodigo();
			CursosEventoVO cursosEvento = getFacadeFactory().getCursosEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			inscricaoCursoEventoVO.getCursosEvento().setCodigo(cursosEvento.getCodigo());
			this.setCursosEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			inscricaoCursoEventoVO.getCursosEvento().setCodigo(0);
			inscricaoCursoEventoVO.getCursosEvento().setCodigo(0);
			this.setCursosEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoInscricao</code>
	 */
	public List getListaSelectItemTipoInscricaoInscricaoEvento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPessoaInscricaoEventos = (Hashtable) Dominios.getTipoPessoaInscricaoEvento();
		Enumeration keys = tipoPessoaInscricaoEventos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPessoaInscricaoEventos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = inscricaoEventoVO.getPessoaInscricao().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			inscricaoEventoVO.getPessoaInscricao().setNome(pessoa.getNome());
			this.setPessoaInscricao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			inscricaoEventoVO.getPessoaInscricao().setNome("");
			inscricaoEventoVO.getPessoaInscricao().setCodigo(0);
			this.setPessoaInscricao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Evento</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = inscricaoEventoVO.getEvento().getCodigo();
			EventoVO evento = getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			inscricaoEventoVO.getEvento().setNome(evento.getNome());
			this.setEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			inscricaoEventoVO.getEvento().setNome("");
			inscricaoEventoVO.getEvento().setCodigo(0);
			this.setEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nrInscricao", "Número Inscrição"));
		itens.add(new SelectItem("nomeEvento", "Evento"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("valorTotal", "Valor Total"));
		itens.add(new SelectItem("tipoInscricao", "Tipo Inscrição"));
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

	public String getCursosEvento_Erro() {
		return cursosEvento_Erro;
	}

	public void setCursosEvento_Erro(String cursosEvento_Erro) {
		this.cursosEvento_Erro = cursosEvento_Erro;
	}

	public InscricaoCursoEventoVO getInscricaoCursoEventoVO() {
		return inscricaoCursoEventoVO;
	}

	public void setInscricaoCursoEventoVO(InscricaoCursoEventoVO inscricaoCursoEventoVO) {
		this.inscricaoCursoEventoVO = inscricaoCursoEventoVO;
	}

	public String getPessoaInscricao_Erro() {
		return pessoaInscricao_Erro;
	}

	public void setPessoaInscricao_Erro(String pessoaInscricao_Erro) {
		this.pessoaInscricao_Erro = pessoaInscricao_Erro;
	}

	public String getEvento_Erro() {
		return evento_Erro;
	}

	public void setEvento_Erro(String evento_Erro) {
		this.evento_Erro = evento_Erro;
	}

	public InscricaoEventoVO getInscricaoEventoVO() {
		return inscricaoEventoVO;
	}

	public void setInscricaoEventoVO(InscricaoEventoVO inscricaoEventoVO) {
		this.inscricaoEventoVO = inscricaoEventoVO;
	}
}