package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas palestraEventoForm.jsp
 * palestraEventoCons.jsp) com as funcionalidades da classe <code>PalestraEvento</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see PalestraEvento
 * @see PalestraEventoVO
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
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.eventos.PalestraEventoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("PalestraEventoControle")
@Scope("request")
@Lazy
public class PalestraEventoControle extends SuperControle implements Serializable {

	private PalestraEventoVO palestraEventoVO;
	private String evento_Erro;

	public PalestraEventoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PalestraEvento</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setPalestraEventoVO(new PalestraEventoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PalestraEvento</code> para alteração.
	 * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() {
		PalestraEventoVO obj = (PalestraEventoVO) context().getExternalContext().getRequestMap().get("palestraEvento");
		obj.setNovoObj(Boolean.FALSE);
		setPalestraEventoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>PalestraEvento</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (palestraEventoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPalestraEventoFacade().incluir(palestraEventoVO);
			} else {
				getFacadeFactory().getPalestraEventoFacade().alterar(palestraEventoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PalestraEventoCons.jsp. Define o tipo de consulta
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
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeEvento")) {
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorNomeEvento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("palestrante")) {
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorPalestrante(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoPalestra")) {
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorTipoPalestra(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrVagasPalestra")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorNrVagasPalestra(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrMaximoVagasExcedentes")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorNrMaximoVagasExcedentes(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("localPalestra")) {
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorLocalPalestra(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorAluno")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorValorAluno(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorProfessor")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorValorProfessor(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorFuncionario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorValorFuncionario(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorComunidade")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPalestraEventoFacade().consultarPorValorComunidade(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>PalestraEventoVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPalestraEventoFacade().excluir(palestraEventoVO);
			setPalestraEventoVO(new PalestraEventoVO());
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
	 * <code>tipoPalestra</code>
	 */
	public List getListaSelectItemTipoPalestraPalestraEvento() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoPalestraPalestraEventos = (Hashtable) Dominios.getTipoPalestraPalestraEvento();
		Enumeration keys = tipoPalestraPalestraEventos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoPalestraPalestraEventos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Evento</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = palestraEventoVO.getEvento().getCodigo();
			EventoVO evento = getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			palestraEventoVO.getEvento().setNome(evento.getNome());
			this.setEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			palestraEventoVO.getEvento().setNome("");
			palestraEventoVO.getEvento().setCodigo(0);
			this.setEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("nomeEvento", "Evento"));
		itens.add(new SelectItem("palestrante", "Palestrante"));
		itens.add(new SelectItem("tipoPalestra", "Tipo Palestra"));
		itens.add(new SelectItem("nrVagasPalestra", "Número de Vagas Palestra"));
		itens.add(new SelectItem("nrMaximoVagasExcedentes", "Número Máximo de Vagas Excedentes"));
		itens.add(new SelectItem("localPalestra", "Local Palestra"));
		itens.add(new SelectItem("valorAluno", "Valor Aluno"));
		itens.add(new SelectItem("valorProfessor", "Valor Professor"));
		itens.add(new SelectItem("valorFuncionario", "Valor Funcionário"));
		itens.add(new SelectItem("valorComunidade", "Valor Comunidade"));
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

	public String getEvento_Erro() {
		return evento_Erro;
	}

	public void setEvento_Erro(String evento_Erro) {
		this.evento_Erro = evento_Erro;
	}

	public PalestraEventoVO getPalestraEventoVO() {
		return palestraEventoVO;
	}

	public void setPalestraEventoVO(PalestraEventoVO palestraEventoVO) {
		this.palestraEventoVO = palestraEventoVO;
	}
}