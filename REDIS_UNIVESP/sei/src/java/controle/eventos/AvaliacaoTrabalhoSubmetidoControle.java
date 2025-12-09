package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * avaliacaoTrabalhoSubmetidoForm.jsp avaliacaoTrabalhoSubmetidoCons.jsp) com as funcionalidades da classe
 * <code>AvaliacaoTrabalhoSubmetido</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see AvaliacaoTrabalhoSubmetido
 * @see AvaliacaoTrabalhoSubmetidoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.eventos.AvaliacaoTrabalhoSubmetidoVO;
import negocio.comuns.eventos.MembroComissaoCientificaVO;
import negocio.comuns.eventos.TrabalhoSubmetidoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("AvaliacaoTrabalhoSubmetidoControle")
@Scope("request")
@Lazy
public class AvaliacaoTrabalhoSubmetidoControle extends SuperControle implements Serializable {

	private AvaliacaoTrabalhoSubmetidoVO avaliacaoTrabalhoSubmetidoVO;
	private String membroComissaoCientifica_Erro;
	private String trabalhoSubmetido_Erro;

	public AvaliacaoTrabalhoSubmetidoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>AvaliacaoTrabalhoSubmetido</code> para
	 * edição pelo usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setAvaliacaoTrabalhoSubmetidoVO(new AvaliacaoTrabalhoSubmetidoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>AvaliacaoTrabalhoSubmetido</code>
	 * para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		AvaliacaoTrabalhoSubmetidoVO obj = (AvaliacaoTrabalhoSubmetidoVO) context().getExternalContext().getRequestMap().get("avaliacaoTrabalhoSubmetido");
		obj.setNovoObj(Boolean.FALSE);
		setAvaliacaoTrabalhoSubmetidoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>AvaliacaoTrabalhoSubmetido</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (avaliacaoTrabalhoSubmetidoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getAvaliacaoTrabalhoSubmetidoFacade().incluir(avaliacaoTrabalhoSubmetidoVO);
			} else {
				getFacadeFactory().getAvaliacaoTrabalhoSubmetidoFacade().alterar(avaliacaoTrabalhoSubmetidoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP AvaliacaoTrabalhoSubmetidoCons.jsp. Define o tipo
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
				objs = getFacadeFactory().getAvaliacaoTrabalhoSubmetidoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoMembroComissaoCientifica")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAvaliacaoTrabalhoSubmetidoFacade().consultarPorCodigoMembroComissaoCientifica(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("eventoTrabalhoSubmetido")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getAvaliacaoTrabalhoSubmetidoFacade().consultarPorEventoTrabalhoSubmetido(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> Após
	 * a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getAvaliacaoTrabalhoSubmetidoFacade().excluir(avaliacaoTrabalhoSubmetidoVO);
			setAvaliacaoTrabalhoSubmetidoVO(new AvaliacaoTrabalhoSubmetidoVO());
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
	 * Método responsável por processar a consulta na entidade <code>TrabalhoSubmetido</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarTrabalhoSubmetidoPorChavePrimaria() {
		try {
			Integer campoConsulta = avaliacaoTrabalhoSubmetidoVO.getTrabalhoSubmetido().getCodigo();
			TrabalhoSubmetidoVO trabalhoSubmetido = getFacadeFactory().getTrabalhoSubmetidoFacade().consultarPorChavePrimaria(campoConsulta,getUsuarioLogado());
			avaliacaoTrabalhoSubmetidoVO.getTrabalhoSubmetido().setEvento(trabalhoSubmetido.getEvento());
			this.setTrabalhoSubmetido_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			avaliacaoTrabalhoSubmetidoVO.getTrabalhoSubmetido().setEvento(0);
			avaliacaoTrabalhoSubmetidoVO.getTrabalhoSubmetido().setCodigo(0);
			this.setTrabalhoSubmetido_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>MembroComissaoCientifica</code> por meio de sua
	 * respectiva chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela
	 * chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarMembroComissaoCientificaPorChavePrimaria() {
		try {
			Integer campoConsulta = avaliacaoTrabalhoSubmetidoVO.getMembroComissaoCientifica().getCodigo();
			MembroComissaoCientificaVO membroComissaoCientifica = getFacadeFactory().getMembroComissaoCientificaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			avaliacaoTrabalhoSubmetidoVO.getMembroComissaoCientifica().setCodigo(membroComissaoCientifica.getCodigo());
			this.setMembroComissaoCientifica_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			avaliacaoTrabalhoSubmetidoVO.getMembroComissaoCientifica().setCodigo(0);
			avaliacaoTrabalhoSubmetidoVO.getMembroComissaoCientifica().setCodigo(0);
			this.setMembroComissaoCientifica_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("codigoMembroComissaoCientifica", "Membro Comissão Científica"));
		itens.add(new SelectItem("eventoTrabalhoSubmetido", "Trabalho Submetido"));
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

	public String getTrabalhoSubmetido_Erro() {
		return trabalhoSubmetido_Erro;
	}

	public void setTrabalhoSubmetido_Erro(String trabalhoSubmetido_Erro) {
		this.trabalhoSubmetido_Erro = trabalhoSubmetido_Erro;
	}

	public String getMembroComissaoCientifica_Erro() {
		return membroComissaoCientifica_Erro;
	}

	public void setMembroComissaoCientifica_Erro(String membroComissaoCientifica_Erro) {
		this.membroComissaoCientifica_Erro = membroComissaoCientifica_Erro;
	}

	public AvaliacaoTrabalhoSubmetidoVO getAvaliacaoTrabalhoSubmetidoVO() {
		return avaliacaoTrabalhoSubmetidoVO;
	}

	public void setAvaliacaoTrabalhoSubmetidoVO(AvaliacaoTrabalhoSubmetidoVO avaliacaoTrabalhoSubmetidoVO) {
		this.avaliacaoTrabalhoSubmetidoVO = avaliacaoTrabalhoSubmetidoVO;
	}
}
