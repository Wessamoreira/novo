package controle.extensao;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas classificaoCursoExtensaoForm.jsp
 * classificaoCursoExtensaoCons.jsp) com as funcionalidades da classe <code>ClassificaoCursoExtensao</code>. Implemtação
 * da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ClassificaoCursoExtensao
 * @see ClassificaoCursoExtensaoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.extensao.ClassificaoCursoExtensaoVO;
import negocio.comuns.utilitarias.ControleConsulta; @Controller("ClassificaoCursoExtensaoControle")
@Scope("request")
@Lazy
public class ClassificaoCursoExtensaoControle extends SuperControle implements Serializable {
	
	private ClassificaoCursoExtensaoVO classificaoCursoExtensaoVO;

	public ClassificaoCursoExtensaoControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ClassificaoCursoExtensao</code> para edição
	 * pelo usuário da aplicação.
	 */
	public String novo() {         removerObjetoMemoria(this);
		setClassificaoCursoExtensaoVO(new ClassificaoCursoExtensaoVO());
		setMensagemID("msg_entre_dados");
		return "editar";
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ClassificaoCursoExtensao</code> para
	 * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
	 * possa disponibilizá-lo para edição.
	 */
	public String editar() {
		ClassificaoCursoExtensaoVO obj = (ClassificaoCursoExtensaoVO) context().getExternalContext().getRequestMap().get("classificaoCursoExtensao");
		obj.setNovoObj(Boolean.FALSE);
		setClassificaoCursoExtensaoVO(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
	 * <code>ClassificaoCursoExtensao</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (classificaoCursoExtensaoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getClassificacaoCursoExtensaoFacade().incluir(classificaoCursoExtensaoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getClassificacaoCursoExtensaoFacade().alterar(classificaoCursoExtensaoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ClassificaoCursoExtensaoCons.jsp. Define o tipo
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
				objs = getFacadeFactory().getClassificacaoCursoExtensaoFacade().consultarPorCodigo(new Integer(valorInt), true,getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getClassificacaoCursoExtensaoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true,getUsuarioLogado());
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
	 * Operação responsável por processar a exclusão um objeto da classe <code>ClassificaoCursoExtensaoVO</code> Após a
	 * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getClassificacaoCursoExtensaoFacade().excluir(classificaoCursoExtensaoVO, getUsuarioLogado());
			setClassificaoCursoExtensaoVO(new ClassificaoCursoExtensaoVO());
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
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
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

	public ClassificaoCursoExtensaoVO getClassificaoCursoExtensaoVO() {
		return classificaoCursoExtensaoVO;
	}

	public void setClassificaoCursoExtensaoVO(ClassificaoCursoExtensaoVO classificaoCursoExtensaoVO) {
		this.classificaoCursoExtensaoVO = classificaoCursoExtensaoVO;
	}
}