package controle.extensao;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas inscricaoCursoExtensaoForm.jsp
 * inscricaoCursoExtensaoCons.jsp) com as funcionalidades da classe <code>InscricaoCursoExtensao</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see InscricaoCursoExtensao
 * @see InscricaoCursoExtensaoVO
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
import negocio.comuns.extensao.InscricaoCursoExtensaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("InscricaoCursoExtensaoControle")
@Scope("request")
@Lazy
public class InscricaoCursoExtensaoControle extends SuperControle implements Serializable {

	private InscricaoCursoExtensaoVO inscricaoCursoExtensaoVO;
	private String pessoaInscricaoCursoExtensao_Erro;

	public InscricaoCursoExtensaoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>InscricaoCursoExtensao</code> para edição
	 * pelo usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setInscricaoCursoExtensaoVO(new InscricaoCursoExtensaoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>InscricaoCursoExtensao</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		InscricaoCursoExtensaoVO obj = (InscricaoCursoExtensaoVO) context().getExternalContext().getRequestMap().get("inscricaoCursoExtensao");
		obj.setNovoObj(Boolean.FALSE);
		setInscricaoCursoExtensaoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>InscricaoCursoExtensao</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (inscricaoCursoExtensaoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getInscricaoCursoExtensaoFacade().incluir(inscricaoCursoExtensaoVO);
			} else {
				getFacadeFactory().getInscricaoCursoExtensaoFacade().alterar(inscricaoCursoExtensaoVO);
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP InscricaoCursoExtensaoCons.jsp. Define o tipo de
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
				objs = getFacadeFactory().getInscricaoCursoExtensaoFacade().consultarPorNrInscricao(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cursoExtensao")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoCursoExtensaoFacade().consultarPorCursoExtensao(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoCursoExtensaoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("hora")) {
				objs = getFacadeFactory().getInscricaoCursoExtensaoFacade().consultarPorHora(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("valorTotal")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				double valorDouble = Double.parseDouble(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getInscricaoCursoExtensaoFacade().consultarPorValorTotal(new Double(valorDouble), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoInscricao")) {
				objs = getFacadeFactory().getInscricaoCursoExtensaoFacade().consultarPorTipoInscricao(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>InscricaoCursoExtensaoVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getInscricaoCursoExtensaoFacade().excluir(inscricaoCursoExtensaoVO);
			setInscricaoCursoExtensaoVO(new InscricaoCursoExtensaoVO());
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
	 * <code>tipoInscricao</code>
	 */
	public List getListaSelectItemTipoInscricaoInscricaoCursoExtensao() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoInscricaoInscricaoCursoExtensaos = (Hashtable) Dominios.getTipoInscricaoInscricaoCursoExtensao();
		Enumeration keys = tipoInscricaoInscricaoCursoExtensaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoInscricaoInscricaoCursoExtensaos.get(value);
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
			Integer campoConsulta = inscricaoCursoExtensaoVO.getPessoaInscricaoCursoExtensao().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			inscricaoCursoExtensaoVO.getPessoaInscricaoCursoExtensao().setNome(pessoa.getNome());
			this.setPessoaInscricaoCursoExtensao_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			inscricaoCursoExtensaoVO.getPessoaInscricaoCursoExtensao().setNome("");
			inscricaoCursoExtensaoVO.getPessoaInscricaoCursoExtensao().setCodigo(0);
			this.setPessoaInscricaoCursoExtensao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nrInscricao", "Número Inscrição"));
		itens.add(new SelectItem("cursoExtensao", "Curso Extensão"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("hora", "Hora"));
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

	public String getPessoaInscricaoCursoExtensao_Erro() {
		return pessoaInscricaoCursoExtensao_Erro;
	}

	public void setPessoaInscricaoCursoExtensao_Erro(String pessoaInscricaoCursoExtensao_Erro) {
		this.pessoaInscricaoCursoExtensao_Erro = pessoaInscricaoCursoExtensao_Erro;
	}

	public InscricaoCursoExtensaoVO getInscricaoCursoExtensaoVO() {
		return inscricaoCursoExtensaoVO;
	}

	public void setInscricaoCursoExtensaoVO(InscricaoCursoExtensaoVO inscricaoCursoExtensaoVO) {
		this.inscricaoCursoExtensaoVO = inscricaoCursoExtensaoVO;
	}
}