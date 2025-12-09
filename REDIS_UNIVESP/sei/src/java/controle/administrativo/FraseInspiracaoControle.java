package controle.administrativo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * cargoForm.jsp cargoCons.jsp) com as funcionalidades da classe <code>Cargo</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Cargo
 * @see CargoVO
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
import negocio.comuns.administrativo.FraseInspiracaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("FraseInspiracaoControle")
@Scope("viewScope")
@Lazy
public class FraseInspiracaoControle extends SuperControle implements
		Serializable {

	private static final long serialVersionUID = -4411120017960575504L;
	private FraseInspiracaoVO fraseInspiracaoVO;

	public FraseInspiracaoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Cargo</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setFraseInspiracaoVO(new FraseInspiracaoVO());
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoForm");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Cargo</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		FraseInspiracaoVO obj = (FraseInspiracaoVO) context().getExternalContext().getRequestMap().get("fraseInspiracaoItem");
		obj = montarDadosFraseInspiracaoVOCompleto(obj);
		obj.setNovoObj(Boolean.FALSE);
		setFraseInspiracaoVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoForm");
	}

	public FraseInspiracaoVO montarDadosFraseInspiracaoVOCompleto(
			FraseInspiracaoVO obj) {
		try {
			return getFacadeFactory().getFraseInspiracaoFacade()
					.consultarPorChavePrimaria(obj.getCodigo(), false,
							Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FraseInspiracaoVO();
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Cargo</code>. Caso o objeto seja novo (ainda não gravado
	 * no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o
	 * objeto não é gravado, sendo re-apresentado para o usuário juntamente com
	 * uma mensagem de erro.
	 */
	public String gravar() {
		try {
			getFraseInspiracaoVO().setResponsavelCadastro(getUsuarioLogadoClone());
			getFraseInspiracaoVO().setDataUltimaExibicao(new Date());

			if (fraseInspiracaoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getFraseInspiracaoFacade().incluir(
						fraseInspiracaoVO, true, getUsuarioLogado());
			} else {
				getFacadeFactory().getFraseInspiracaoFacade().alterar(
						fraseInspiracaoVO);
			}
			setMensagemID("msg_dados_gravados");
			return Uteis
					.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis
					.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoForm");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * CargoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("frase")) {
				objs = getFacadeFactory().getFraseInspiracaoFacade()
						.consultarPorFrase(
								getControleConsulta().getValorConsulta(), true,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
								getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("autor")) {
				objs = getFacadeFactory().getFraseInspiracaoFacade()
						.consultarPorAutor(
								getControleConsulta().getValorConsulta(), true,
								Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
								getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoCons");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis
					.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoCons");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>CargoVO</code> Após a exclusão ela automaticamente aciona a rotina
	 * para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getFraseInspiracaoFacade().excluir(fraseInspiracaoVO);
			setFraseInspiracaoVO(new FraseInspiracaoVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoForm");
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("frase", "Frase"));
		itens.add(new SelectItem("autor", "Autor"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("fraseInspiracaoCons");
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		fraseInspiracaoVO = null;
	}

	/**
	 * @return the fraseInspiracaoVO
	 */
	public FraseInspiracaoVO getFraseInspiracaoVO() {
		if (fraseInspiracaoVO == null) {
			fraseInspiracaoVO = new FraseInspiracaoVO();
		}
		return fraseInspiracaoVO;
	}

	/**
	 * @param fraseInspiracaoVO
	 *            the fraseInspiracaoVO to set
	 */
	public void setFraseInspiracaoVO(FraseInspiracaoVO fraseInspiracaoVO) {
		this.fraseInspiracaoVO = fraseInspiracaoVO;
	}
}
