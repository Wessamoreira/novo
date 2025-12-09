package controle.administrativo;

import javax.annotation.PostConstruct;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@Controller("AgrupamentoUnidadeEnsinoControle")
@Scope("viewScope")
@Lazy
public class AgrupamentoUnidadeEnsinoControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5388901315640963676L;
	private static final String TELA_FORM = "agrupamentoUnidadeEnsinoForm.xhtml";
	private static final String TELA_CONS = "agrupamentoUnidadeEnsinoCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "agrupamentoUnidadeEnsinoItens";
	private AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO;

	@PostConstruct
	public void inicializarPostContruct() {
		removerObjetoMemoria(this);
		setAgrupamentoUnidadeEnsinoVO(new AgrupamentoUnidadeEnsinoVO());
		getAgrupamentoUnidadeEnsinoVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.NENHUM);
		consultarUnidadeEnsinoFiltroRelatorio("");
		setMarcarTodasUnidadeEnsino(false);
		marcarTodasUnidadesEnsinoAction();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setAgrupamentoUnidadeEnsinoVO(new AgrupamentoUnidadeEnsinoVO());
		consultarUnidadeEnsinoFiltroRelatorio("");
		setMarcarTodasUnidadeEnsino(false);
		marcarTodasUnidadesEnsinoAction();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			getUnidadeEnsinoVOs().parallelStream().forEach(p->p.setFiltrarUnidadeEnsino(false));
			AgrupamentoUnidadeEnsinoVO obj = (AgrupamentoUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setAgrupamentoUnidadeEnsinoVO(getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getAgrupamentoUnidadeEnsinoVO().getListaAgrupamentoUnidadeEnsinoItemVO().stream()
			.forEach(auei-> {
				getUnidadeEnsinoVOs().stream()
				.filter(p-> p.getCodigo().equals(auei.getUnidadeEnsinoVO().getCodigo()))
				.forEach(p->p.setFiltrarUnidadeEnsino(true));
				});
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().realizarPreenchimentoAgrupamentoUnidadeEnsinoItemVO(getAgrupamentoUnidadeEnsinoVO(), getUnidadeEnsinoVOs(), getUsuarioLogadoClone());
			getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().persistir(getAgrupamentoUnidadeEnsinoVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (ConsistirException ce) {
			setListaMensagemErro(ce.getListaMensagemErro());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void ativarAgrupamentoUnidadeEnsino() {
		try {
			getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().atualizarStatusAgrupamentoUnidadeEnsino(getAgrupamentoUnidadeEnsinoVO(), StatusAtivoInativoEnum.ATIVO, true, getUsuarioLogado());
			getAgrupamentoUnidadeEnsinoVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (ConsistirException ce) {
			setListaMensagemErro(ce.getListaMensagemErro());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inativarAgrupamentoUnidadeEnsino() {
		try {
			getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().atualizarStatusAgrupamentoUnidadeEnsino(getAgrupamentoUnidadeEnsinoVO(), StatusAtivoInativoEnum.INATIVO, true, getUsuarioLogado());
			getAgrupamentoUnidadeEnsinoVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (ConsistirException ce) {
			setListaMensagemErro(ce.getListaMensagemErro());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().excluir(getAgrupamentoUnidadeEnsinoVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (ConsistirException ce) {
			setListaMensagemErro(ce.getListaMensagemErro());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getAgrupamentoUnidadeEnsinoFacade().consultar(getControleConsultaOtimizado(), getAgrupamentoUnidadeEnsinoVO());
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
		setAgrupamentoUnidadeEnsinoVO(new AgrupamentoUnidadeEnsinoVO());
		getAgrupamentoUnidadeEnsinoVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.NENHUM);
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public AgrupamentoUnidadeEnsinoVO getAgrupamentoUnidadeEnsinoVO() {
		if (agrupamentoUnidadeEnsinoVO == null) {
			agrupamentoUnidadeEnsinoVO = new AgrupamentoUnidadeEnsinoVO();
		}
		return agrupamentoUnidadeEnsinoVO;
	}

	public void setAgrupamentoUnidadeEnsinoVO(AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO) {
		this.agrupamentoUnidadeEnsinoVO = agrupamentoUnidadeEnsinoVO;
	}

}
