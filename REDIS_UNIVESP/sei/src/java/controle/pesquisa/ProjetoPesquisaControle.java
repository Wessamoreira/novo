package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas projetoPesquisaForm.jsp
 * projetoPesquisaCons.jsp) com as funcionalidades da classe <code>ProjetoPesquisa</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ProjetoPesquisa
 * @see ProjetoPesquisaVO
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.PesquisadorLinhaPesquisaVO;
import negocio.comuns.pesquisa.PesquisadorProjetoPesquisaVO;
import negocio.comuns.pesquisa.ProjetoPesquisaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("ProjetoPesquisaControle")
@Scope("request")
@Lazy
public class ProjetoPesquisaControle extends SuperControle implements Serializable {

	private ProjetoPesquisaVO projetoPesquisaVO;
	private String areaConhecimento_Erro;
	private PesquisadorProjetoPesquisaVO pesquisadorProjetoPesquisaVO;
	private String pesquisadorLinhaPesquisa_Erro;

	public ProjetoPesquisaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ProjetoPesquisa</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setProjetoPesquisaVO(new ProjetoPesquisaVO());
		setPesquisadorProjetoPesquisaVO(new PesquisadorProjetoPesquisaVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ProjetoPesquisa</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		ProjetoPesquisaVO obj = (ProjetoPesquisaVO) context().getExternalContext().getRequestMap().get("projetoPesquisa");
		obj.setNovoObj(Boolean.FALSE);
		setProjetoPesquisaVO(obj);
		setPesquisadorProjetoPesquisaVO(new PesquisadorProjetoPesquisaVO());
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ProjetoPesquisa</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (projetoPesquisaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getProjetoPesquisaFacade().incluir(projetoPesquisaVO);
			} else {
				getFacadeFactory().getProjetoPesquisaFacade().alterar(projetoPesquisaVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ProjetoPesquisaCons.jsp. Define o tipo de
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
				objs = getFacadeFactory().getProjetoPesquisaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getProjetoPesquisaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("linhaPesquisa")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProjetoPesquisaFacade().consultarPorLinhaPesquisa(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataCriacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProjetoPesquisaFacade().consultarPorDataCriacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("duracaoPrevista")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProjetoPesquisaFacade().consultarPorDuracaoPrevista(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>ProjetoPesquisaVO</code> Após a exclusão
	 * ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getProjetoPesquisaFacade().excluir(projetoPesquisaVO);
			setProjetoPesquisaVO(new ProjetoPesquisaVO());

			setPesquisadorProjetoPesquisaVO(new PesquisadorProjetoPesquisaVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>PesquisadorProjetoPesquisa</code> para o objeto
	 * <code>projetoPesquisaVO</code> da classe <code>ProjetoPesquisa</code>
	 */
	public String adicionarPesquisadorProjetoPesquisa() throws Exception {
		try {
			if (!getProjetoPesquisaVO().getCodigo().equals(0)) {
				pesquisadorProjetoPesquisaVO.setProjetoPesquisa(getProjetoPesquisaVO().getCodigo());
			}
			if (getPesquisadorProjetoPesquisaVO().getPesquisadorLinhaPesquisa().getCodigo().intValue() != 0) {
				Integer campoConsulta = getPesquisadorProjetoPesquisaVO().getPesquisadorLinhaPesquisa().getCodigo();
				PesquisadorLinhaPesquisaVO pesquisadorLinhaPesquisa = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
				getPesquisadorProjetoPesquisaVO().setPesquisadorLinhaPesquisa(pesquisadorLinhaPesquisa);
			}
			getProjetoPesquisaVO().adicionarObjPesquisadorProjetoPesquisaVOs(getPesquisadorProjetoPesquisaVO());
			this.setPesquisadorProjetoPesquisaVO(new PesquisadorProjetoPesquisaVO());
			setMensagemID("msg_dados_adicionados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>PesquisadorProjetoPesquisa</code> para
	 * edição pelo usuário.
	 */
	public String editarPesquisadorProjetoPesquisa() throws Exception {
		PesquisadorProjetoPesquisaVO obj = (PesquisadorProjetoPesquisaVO) context().getExternalContext().getRequestMap().get("pesquisadorProjetoPesquisa");
		setPesquisadorProjetoPesquisaVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>PesquisadorProjetoPesquisa</code> do objeto
	 * <code>projetoPesquisaVO</code> da classe <code>ProjetoPesquisa</code>
	 */
	public String removerPesquisadorProjetoPesquisa() throws Exception {
		PesquisadorProjetoPesquisaVO obj = (PesquisadorProjetoPesquisaVO) context().getExternalContext().getRequestMap().get("pesquisadorProjetoPesquisa");
		getProjetoPesquisaVO().excluirObjPesquisadorProjetoPesquisaVOs(obj.getPesquisadorLinhaPesquisa().getCodigo());
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
	 * Método responsável por processar a consulta na entidade <code>PesquisadorLinhaPesquisa</code> por meio de sua
	 * respectiva chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela
	 * chave primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPesquisadorLinhaPesquisaPorChavePrimaria() {
		try {
			Integer campoConsulta = pesquisadorProjetoPesquisaVO.getPesquisadorLinhaPesquisa().getCodigo();
			PesquisadorLinhaPesquisaVO pesquisadorLinhaPesquisa = getFacadeFactory().getPesquisadorLinhaPesquisaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS,getUsuarioLogado());
			pesquisadorProjetoPesquisaVO.getPesquisadorLinhaPesquisa().setCodigo(pesquisadorLinhaPesquisa.getCodigo());
			this.setPesquisadorLinhaPesquisa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorProjetoPesquisaVO.getPesquisadorLinhaPesquisa().setCodigo(0);
			pesquisadorProjetoPesquisaVO.getPesquisadorLinhaPesquisa().setCodigo(0);
			this.setPesquisadorLinhaPesquisa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>AreaConhecimento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarAreaConhecimentoPorChavePrimaria() {
		try {
			Integer campoConsulta = projetoPesquisaVO.getAreaConhecimento().getCodigo();
			AreaConhecimentoVO areaConhecimento = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(campoConsulta,getUsuarioLogado());
			projetoPesquisaVO.getAreaConhecimento().setNome(areaConhecimento.getNome());
			this.setAreaConhecimento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			projetoPesquisaVO.getAreaConhecimento().setNome("");
			projetoPesquisaVO.getAreaConhecimento().setCodigo(0);
			this.setAreaConhecimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("linhaPesquisa", "Linha de Pesquisa"));
		itens.add(new SelectItem("dataCriacao", "Data Criação"));
		itens.add(new SelectItem("duracaoPrevista", "Duração Prevista"));
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

	public String getPesquisadorLinhaPesquisa_Erro() {
		return pesquisadorLinhaPesquisa_Erro;
	}

	public void setPesquisadorLinhaPesquisa_Erro(String pesquisadorLinhaPesquisa_Erro) {
		this.pesquisadorLinhaPesquisa_Erro = pesquisadorLinhaPesquisa_Erro;
	}

	public PesquisadorProjetoPesquisaVO getPesquisadorProjetoPesquisaVO() {
		return pesquisadorProjetoPesquisaVO;
	}

	public void setPesquisadorProjetoPesquisaVO(PesquisadorProjetoPesquisaVO pesquisadorProjetoPesquisaVO) {
		this.pesquisadorProjetoPesquisaVO = pesquisadorProjetoPesquisaVO;
	}

	public String getAreaConhecimento_Erro() {
		return areaConhecimento_Erro;
	}

	public void setAreaConhecimento_Erro(String areaConhecimento_Erro) {
		this.areaConhecimento_Erro = areaConhecimento_Erro;
	}

	public ProjetoPesquisaVO getProjetoPesquisaVO() {
		return projetoPesquisaVO;
	}

	public void setProjetoPesquisaVO(ProjetoPesquisaVO projetoPesquisaVO) {
		this.projetoPesquisaVO = projetoPesquisaVO;
	}
}