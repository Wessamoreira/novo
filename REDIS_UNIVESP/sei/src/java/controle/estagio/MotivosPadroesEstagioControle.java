package controle.estagio;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.estagio.MotivosPadroesEstagioVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("MotivosPadroesEstagioControle")
@Scope("viewScope")
@Lazy
public class MotivosPadroesEstagioControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1865124700175494446L;
	private static final String TELA_FORM = "motivosPadroesEstagioForm.xhtml";
	private static final String TELA_CONS = "motivosPadroesEstagioCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "motivosPadroesEstagioItens";
	private MotivosPadroesEstagioVO motivosPadroesEstagioVO;

	public String novo() {
		removerObjetoMemoria(this);
		setMotivosPadroesEstagioVO(new MotivosPadroesEstagioVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			MotivosPadroesEstagioVO obj = (MotivosPadroesEstagioVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setMotivosPadroesEstagioVO(getFacadeFactory().getMotivosPadroesEstagioFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getMotivosPadroesEstagioFacade().persistir(getMotivosPadroesEstagioVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getMotivosPadroesEstagioFacade().excluir(getMotivosPadroesEstagioVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getMotivosPadroesEstagioFacade().consultar(getControleConsultaOtimizado(), getMotivosPadroesEstagioVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public MotivosPadroesEstagioVO getMotivosPadroesEstagioVO() {
		if (motivosPadroesEstagioVO == null) {
			motivosPadroesEstagioVO = new MotivosPadroesEstagioVO();
		}
		return motivosPadroesEstagioVO;
	}

	public void setMotivosPadroesEstagioVO(MotivosPadroesEstagioVO motivosPadroesEstagioVO) {
		this.motivosPadroesEstagioVO = motivosPadroesEstagioVO;
	}

}
