package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas membroComissaoCientificaForm.jsp
 * membroComissaoCientificaCons.jsp) com as funcionalidades da classe <code>MembroComissaoCientifica</code>. Implemtação
 * da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see MembroComissaoCientifica
 * @see MembroComissaoCientificaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.eventos.MembroComissaoCientificaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("MembroComissaoCientificaControle")
@Scope("request")
@Lazy
public class MembroComissaoCientificaControle extends SuperControle implements Serializable {

	private MembroComissaoCientificaVO membroComissaoCientificaVO;
	private String avaliador_Erro;
	private String evento_Erro;

	public MembroComissaoCientificaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>MembroComissaoCientifica</code> para edição
	 * pelo usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setMembroComissaoCientificaVO(new MembroComissaoCientificaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>MembroComissaoCientifica</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		MembroComissaoCientificaVO obj = (MembroComissaoCientificaVO) context().getExternalContext().getRequestMap().get("membroComissaoCientifica");
		obj.setNovoObj(Boolean.FALSE);
		setMembroComissaoCientificaVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>MembroComissaoCientifica</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (membroComissaoCientificaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getMembroComissaoCientificaFacade().incluir(membroComissaoCientificaVO);
			} else {
				getFacadeFactory().getMembroComissaoCientificaFacade().alterar(membroComissaoCientificaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP MembroComissaoCientificaCons.jsp. Define o tipo
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
				objs = getFacadeFactory().getMembroComissaoCientificaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getMembroComissaoCientificaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeEvento")) {
				objs = getFacadeFactory().getMembroComissaoCientificaFacade().consultarPorNomeEvento(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>MembroComissaoCientificaVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getMembroComissaoCientificaFacade().excluir(membroComissaoCientificaVO);
			setMembroComissaoCientificaVO(new MembroComissaoCientificaVO());
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
	 * Método responsável por processar a consulta na entidade <code>Evento</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarEventoPorChavePrimaria() {
		try {
			Integer campoConsulta = membroComissaoCientificaVO.getEvento().getCodigo();
			EventoVO evento = getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			membroComissaoCientificaVO.getEvento().setNome(evento.getNome());
			this.setEvento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			membroComissaoCientificaVO.getEvento().setNome("");
			membroComissaoCientificaVO.getEvento().setCodigo(0);
			this.setEvento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = membroComissaoCientificaVO.getAvaliador().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
			membroComissaoCientificaVO.getAvaliador().setNome(pessoa.getNome());
			this.setAvaliador_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			membroComissaoCientificaVO.getAvaliador().setNome("");
			membroComissaoCientificaVO.getAvaliador().setCodigo(0);
			this.setAvaliador_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomePessoa", "Avaliador"));
		itens.add(new SelectItem("nomeEvento", "Evento"));
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

	public String getAvaliador_Erro() {
		return avaliador_Erro;
	}

	public void setAvaliador_Erro(String avaliador_Erro) {
		this.avaliador_Erro = avaliador_Erro;
	}

	public MembroComissaoCientificaVO getMembroComissaoCientificaVO() {
		return membroComissaoCientificaVO;
	}

	public void setMembroComissaoCientificaVO(MembroComissaoCientificaVO membroComissaoCientificaVO) {
		this.membroComissaoCientificaVO = membroComissaoCientificaVO;
	}
}
