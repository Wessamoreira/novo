package controle.pesquisa;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas pesquisadorConvidadoForm.jsp
 * pesquisadorConvidadoCons.jsp) com as funcionalidades da classe <code>PesquisadorConvidado</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see PesquisadorConvidado
 * @see PesquisadorConvidadoVO
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
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.pesquisa.PesquisadorConvidadoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("PesquisadorConvidadoControle")
@Scope("request")
@Lazy
public class PesquisadorConvidadoControle extends SuperControle implements Serializable {

	private PesquisadorConvidadoVO pesquisadorConvidadoVO;
	private String areaConhecimento_Erro;
	private String pessoa_Erro;

	public PesquisadorConvidadoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>PesquisadorConvidado</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setPesquisadorConvidadoVO(new PesquisadorConvidadoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>PesquisadorConvidado</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		PesquisadorConvidadoVO obj = (PesquisadorConvidadoVO) context().getExternalContext().getRequestMap().get("pesquisadorConvidado");
		obj.setNovoObj(Boolean.FALSE);
		setPesquisadorConvidadoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>PesquisadorConvidado</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (pesquisadorConvidadoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPesquisadorConvidadoFacade().incluir(pesquisadorConvidadoVO);
			} else {
				getFacadeFactory().getPesquisadorConvidadoFacade().alterar(pesquisadorConvidadoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PesquisadorConvidadoCons.jsp. Define o tipo de
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
				objs = getFacadeFactory().getPesquisadorConvidadoFacade().consultarPorCodigo(new Integer(valorInt), true,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>PesquisadorConvidadoVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getPesquisadorConvidadoFacade().excluir(pesquisadorConvidadoVO);
			setPesquisadorConvidadoVO(new PesquisadorConvidadoVO());
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
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = pesquisadorConvidadoVO.getPessoa().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			pesquisadorConvidadoVO.getPessoa().setNome(pessoa.getNome());
			this.setPessoa_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorConvidadoVO.getPessoa().setNome("");
			pesquisadorConvidadoVO.getPessoa().setCodigo(0);
			this.setPessoa_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>AreaConhecimento</code> por meio de sua respectiva
	 * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
	 * primária da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarAreaConhecimentoPorChavePrimaria() {
		try {
			Integer campoConsulta = pesquisadorConvidadoVO.getAreaConhecimento().getCodigo();
			AreaConhecimentoVO areaConhecimento = getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(campoConsulta,getUsuarioLogado());
			pesquisadorConvidadoVO.getAreaConhecimento().setNome(areaConhecimento.getNome());
			this.setAreaConhecimento_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			pesquisadorConvidadoVO.getAreaConhecimento().setNome("");
			pesquisadorConvidadoVO.getAreaConhecimento().setCodigo(0);
			this.setAreaConhecimento_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
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

	public String getPessoa_Erro() {
		return pessoa_Erro;
	}

	public void setPessoa_Erro(String pessoa_Erro) {
		this.pessoa_Erro = pessoa_Erro;
	}

	public String getAreaConhecimento_Erro() {
		return areaConhecimento_Erro;
	}

	public void setAreaConhecimento_Erro(String areaConhecimento_Erro) {
		this.areaConhecimento_Erro = areaConhecimento_Erro;
	}

	public PesquisadorConvidadoVO getPesquisadorConvidadoVO() {
		return pesquisadorConvidadoVO;
	}

	public void setPesquisadorConvidadoVO(PesquisadorConvidadoVO pesquisadorConvidadoVO) {
		this.pesquisadorConvidadoVO = pesquisadorConvidadoVO;
	}
}