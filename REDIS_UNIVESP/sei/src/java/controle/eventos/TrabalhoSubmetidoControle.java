package controle.eventos;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas trabalhoSubmetidoForm.jsp
 * trabalhoSubmetidoCons.jsp) com as funcionalidades da classe <code>TrabalhoSubmetido</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see TrabalhoSubmetido
 * @see TrabalhoSubmetidoVO
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
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.AutorTrabalhoSubmetidoVO;
import negocio.comuns.eventos.TrabalhoSubmetidoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("TrabalhoSubmetidoControle")
@Scope("request")
@Lazy
public class TrabalhoSubmetidoControle extends SuperControle implements Serializable {

	private TrabalhoSubmetidoVO trabalhoSubmetidoVO;
	private AutorTrabalhoSubmetidoVO autorTrabalhoSubmetidoVO;
	private String pessoaAutorTrabalhoSubmetido_Erro;

	public TrabalhoSubmetidoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>TrabalhoSubmetido</code> para edição pelo
	 * usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setTrabalhoSubmetidoVO(new TrabalhoSubmetidoVO());
		setAutorTrabalhoSubmetidoVO(new AutorTrabalhoSubmetidoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TrabalhoSubmetido</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		TrabalhoSubmetidoVO obj = (TrabalhoSubmetidoVO) context().getExternalContext().getRequestMap().get("trabalhoSubmetido");
		obj.setNovoObj(Boolean.FALSE);
		setTrabalhoSubmetidoVO(obj);
		setAutorTrabalhoSubmetidoVO(new AutorTrabalhoSubmetidoVO());
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TrabalhoSubmetido</code>.
	 * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (trabalhoSubmetidoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTrabalhoSubmetidoFacade().incluir(trabalhoSubmetidoVO);
			} else {
				getFacadeFactory().getTrabalhoSubmetidoFacade().alterar(trabalhoSubmetidoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP TrabalhoSubmetidoCons.jsp. Define o tipo de
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
				objs = getFacadeFactory().getTrabalhoSubmetidoFacade().consultarPorCodigo(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("evento")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTrabalhoSubmetidoFacade().consultarPorEvento(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataSubmissao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTrabalhoSubmetidoFacade().consultarPorDataSubmissao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>TrabalhoSubmetidoVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getTrabalhoSubmetidoFacade().excluir(trabalhoSubmetidoVO);
			setTrabalhoSubmetidoVO(new TrabalhoSubmetidoVO());

			setAutorTrabalhoSubmetidoVO(new AutorTrabalhoSubmetidoVO());
			setMensagemID("msg_dados_excluidos");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>AutorTrabalhoSubmetido</code> para o objeto
	 * <code>trabalhoSubmetidoVO</code> da classe <code>TrabalhoSubmetido</code>
	 */
	public String adicionarAutorTrabalhoSubmetido() throws Exception {
		try {
			if (!getTrabalhoSubmetidoVO().getCodigo().equals(0)) {
				autorTrabalhoSubmetidoVO.setTrabalhoSubmetido(getTrabalhoSubmetidoVO().getCodigo());
			}
			if (getAutorTrabalhoSubmetidoVO().getPessoaAutorTrabalhoSubmetido().getCodigo().intValue() != 0) {
				Integer campoConsulta = getAutorTrabalhoSubmetidoVO().getPessoaAutorTrabalhoSubmetido().getCodigo();
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getAutorTrabalhoSubmetidoVO().setPessoaAutorTrabalhoSubmetido(pessoa);
			}
			getTrabalhoSubmetidoVO().adicionarObjAutorTrabalhoSubmetidoVOs(getAutorTrabalhoSubmetidoVO());
			this.setAutorTrabalhoSubmetidoVO(new AutorTrabalhoSubmetidoVO());
			setMensagemID("msg_dados_adicionados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>AutorTrabalhoSubmetido</code> para
	 * edição pelo usuário.
	 */
	public String editarAutorTrabalhoSubmetido() throws Exception {
		AutorTrabalhoSubmetidoVO obj = (AutorTrabalhoSubmetidoVO) context().getExternalContext().getRequestMap().get("autorTrabalhoSubmetido");
		setAutorTrabalhoSubmetidoVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>AutorTrabalhoSubmetido</code> do objeto
	 * <code>trabalhoSubmetidoVO</code> da classe <code>TrabalhoSubmetido</code>
	 */
	public String removerAutorTrabalhoSubmetido() throws Exception {
		AutorTrabalhoSubmetidoVO obj = (AutorTrabalhoSubmetidoVO) context().getExternalContext().getRequestMap().get("autorTrabalhoSubmetido");
		getTrabalhoSubmetidoVO().excluirObjAutorTrabalhoSubmetidoVOs(obj.getPessoaAutorTrabalhoSubmetido().getCodigo());
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
			Integer campoConsulta = autorTrabalhoSubmetidoVO.getPessoaAutorTrabalhoSubmetido().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			autorTrabalhoSubmetidoVO.getPessoaAutorTrabalhoSubmetido().setNome(pessoa.getNome());
			this.setPessoaAutorTrabalhoSubmetido_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			autorTrabalhoSubmetidoVO.getPessoaAutorTrabalhoSubmetido().setNome("");
			autorTrabalhoSubmetidoVO.getPessoaAutorTrabalhoSubmetido().setCodigo(0);
			this.setPessoaAutorTrabalhoSubmetido_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("evento", "Evento"));
		itens.add(new SelectItem("dataSubmissao", "Data Submissão"));
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

	public String getPessoaAutorTrabalhoSubmetido_Erro() {
		return pessoaAutorTrabalhoSubmetido_Erro;
	}

	public void setPessoaAutorTrabalhoSubmetido_Erro(String pessoaAutorTrabalhoSubmetido_Erro) {
		this.pessoaAutorTrabalhoSubmetido_Erro = pessoaAutorTrabalhoSubmetido_Erro;
	}

	public AutorTrabalhoSubmetidoVO getAutorTrabalhoSubmetidoVO() {
		return autorTrabalhoSubmetidoVO;
	}

	public void setAutorTrabalhoSubmetidoVO(AutorTrabalhoSubmetidoVO autorTrabalhoSubmetidoVO) {
		this.autorTrabalhoSubmetidoVO = autorTrabalhoSubmetidoVO;
	}

	public TrabalhoSubmetidoVO getTrabalhoSubmetidoVO() {
		return trabalhoSubmetidoVO;
	}

	public void setTrabalhoSubmetidoVO(TrabalhoSubmetidoVO trabalhoSubmetidoVO) {
		this.trabalhoSubmetidoVO = trabalhoSubmetidoVO;
	}
}