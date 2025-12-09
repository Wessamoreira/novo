package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas inscricaoPalestraEventoForm.jsp
 * inscricaoPalestraEventoCons.jsp) com as funcionalidades da classe <code>InscricaoPalestraEvento</code>. Implemtação
 * da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see InscricaoPalestraEvento
 * @see InscricaoPalestraEventoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.eventos.InscricaoEventoVO;
import negocio.comuns.eventos.InscricaoPalestraEventoVO;
import negocio.comuns.eventos.PalestraEventoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("InscricaoPalestraEventoControle")
@Scope("request")
@Lazy
public class InscricaoPalestraEventoControle extends SuperControle implements Serializable {
	private InscricaoPalestraEventoVO inscricaoPalestraEventoVO;
	private String inscricaoEvento_Erro;
	private String palestraEvento_Erro;

	public InscricaoPalestraEventoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>InscricaoPalestraEvento</code> para edição
	 * pelo usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setInscricaoPalestraEventoVO(new InscricaoPalestraEventoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>InscricaoPalestraEvento</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		InscricaoPalestraEventoVO obj = (InscricaoPalestraEventoVO) context().getExternalContext().getRequestMap().get("inscricaoPalestraEvento");
		obj.setNovoObj(Boolean.FALSE);
		setInscricaoPalestraEventoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>InscricaoPalestraEvento</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (inscricaoPalestraEventoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getInscricaoPalestraEventoFacade().incluir(inscricaoPalestraEventoVO);
			} else {
				getFacadeFactory().getInscricaoPalestraEventoFacade().alterar(inscricaoPalestraEventoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP InscricaoPalestraEventoCons.jsp. Define o tipo de
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
				objs = getFacadeFactory().getInscricaoPalestraEventoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nrInscricaoInscricaoEvento")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoPalestraEventoFacade().consultarPorNrInscricaoInscricaoEvento(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoPalestraEvento")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoPalestraEventoFacade().consultarPorCodigoPalestraEvento(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorInscricao")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoPalestraEventoFacade().consultarPorValorInscricao(new Double(valorDouble), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>InscricaoPalestraEventoVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getInscricaoPalestraEventoFacade().excluir(inscricaoPalestraEventoVO);
			setInscricaoPalestraEventoVO(new InscricaoPalestraEventoVO());
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
	 * Método responsável por processar a consulta na entidade <code>PalestraEvento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPalestraEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = inscricaoPalestraEventoVO.getPalestraEvento().getCodigo();
			PalestraEventoVO palestraEvento = getFacadeFactory().getPalestraEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			inscricaoPalestraEventoVO.getPalestraEvento().setCodigo(palestraEvento.getCodigo());
			this.setPalestraEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			inscricaoPalestraEventoVO.getPalestraEvento().setCodigo(0);
			inscricaoPalestraEventoVO.getPalestraEvento().setCodigo(0);
			this.setPalestraEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>InscricaoEvento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarInscricaoEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = inscricaoPalestraEventoVO.getInscricaoEvento().getNrInscricao();
			InscricaoEventoVO inscricaoEvento = getFacadeFactory().getInscricaoEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			inscricaoPalestraEventoVO.getInscricaoEvento().setNrInscricao(inscricaoEvento.getNrInscricao());
			this.setInscricaoEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			inscricaoPalestraEventoVO.getInscricaoEvento().setNrInscricao(0);
			inscricaoPalestraEventoVO.getInscricaoEvento().setNrInscricao(0);
			this.setInscricaoEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nrInscricaoInscricaoEvento", "Inscrição Evento"));
		itens.add(new SelectItem("codigoPalestraEvento", "Palestra Evento"));
		itens.add(new SelectItem("valorInscricao", "Valor Inscrição"));
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

	public String getPalestraEvento_Erro() {
		return palestraEvento_Erro;
	}

	public void setPalestraEvento_Erro(String palestraEvento_Erro) {
		this.palestraEvento_Erro = palestraEvento_Erro;
	}

	public String getInscricaoEvento_Erro() {
		return inscricaoEvento_Erro;
	}

	public void setInscricaoEvento_Erro(String inscricaoEvento_Erro) {
		this.inscricaoEvento_Erro = inscricaoEvento_Erro;
	}

	public InscricaoPalestraEventoVO getInscricaoPalestraEventoVO() {
		return inscricaoPalestraEventoVO;
	}

	public void setInscricaoPalestraEventoVO(InscricaoPalestraEventoVO inscricaoPalestraEventoVO) {
		this.inscricaoPalestraEventoVO = inscricaoPalestraEventoVO;
	}
}