package controle.estagio;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.estagio.TipoConcedenteVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("TipoConcedenteControle")
@Scope("viewScope")
@Lazy
public class TipoConcedenteControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8097598435408377820L;
	private static final String TELA_FORM = "tipoConcedenteForm.xhtml";
	private static final String TELA_CONS = "tipoConcedenteCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "tipoConcedenteItens";
	private TipoConcedenteVO tipoConcedenteVO;

	public String novo() {
		removerObjetoMemoria(this);
		setTipoConcedenteVO(new TipoConcedenteVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			TipoConcedenteVO obj = (TipoConcedenteVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setTipoConcedenteVO(getFacadeFactory().getTipoConcedenteFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getTipoConcedenteFacade().persistir(getTipoConcedenteVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getTipoConcedenteFacade().excluir(getTipoConcedenteVO(), true, getUsuarioLogado());
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
			getFacadeFactory().getTipoConcedenteFacade().consultar(getControleConsultaOtimizado(), getTipoConcedenteVO());
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

	public TipoConcedenteVO getTipoConcedenteVO() {
		if (tipoConcedenteVO == null) {
			tipoConcedenteVO = new TipoConcedenteVO();
		}
		return tipoConcedenteVO;
	}

	public void setTipoConcedenteVO(TipoConcedenteVO tipoConcedenteVO) {
		this.tipoConcedenteVO = tipoConcedenteVO;
	}

}
