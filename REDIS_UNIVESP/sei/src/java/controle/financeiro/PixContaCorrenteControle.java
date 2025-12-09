package controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.PixContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.SituacaoPixEnum;
import negocio.comuns.financeiro.enumerador.StatusPixEnum;
import negocio.comuns.utilitarias.Uteis;

@Controller("PixContaCorrenteControle")
@Scope("viewScope")
@Lazy
public class PixContaCorrenteControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1310845931922776729L;
	private PixContaCorrenteVO pixContaCorrenteVO;
	private List<SelectItem> listaSelectItemContaCorrente;
	private String jsonConsultaPix;

	public PixContaCorrenteControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(null);
		montarDadosContaCorrente();
		getPixContaCorrenteVO().setSituacaoPixEnum(SituacaoPixEnum.NENHUM);
		getPixContaCorrenteVO().setStatusPixEnum(StatusPixEnum.NENHUM);
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name(), Uteis.ALERTA);
	}
	
	public String editar() {
		try {
			PixContaCorrenteVO obj = (PixContaCorrenteVO) context().getExternalContext().getRequestMap().get("pixContaCorrenteItens");
			setPixContaCorrenteVO(getFacadeFactory().getPixContaCorrenteFacade().consultarPorChavePrimaria(obj.getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
			return Uteis.getCaminhoRedirecionamentoNavegacao("pixContaCorrenteForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("pixContaCorrenteCons.xhtml");
		}
	}
	
	public void consultarJsonPix() {
		try {
			setJsonConsultaPix(getFacadeFactory().getPixContaCorrenteFacade().consultarJsonPix(getPixContaCorrenteVO(), getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_atualizados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}		
	}
	
	public void verificarBaixa() {
		try {
			setPixContaCorrenteVO(getFacadeFactory().getPixContaCorrenteFacade().realizarVerificacaoBaixaPixContaCorrente(getPixContaCorrenteVO(), true, getUsuarioLogadoClone()));
			setMensagemID(MSG_TELA.msg_dados_atualizados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}		
	}
	
	public void cancelar() {
		try {
			getFacadeFactory().getPixContaCorrenteFacade().realizarCancelamentoPix(getPixContaCorrenteVO(),  getUsuarioLogadoClone());
			setOncompleteModal("RichFaces.$('panelMotivoCancelamento').hide()");
			setMensagemID(MSG_TELA.msg_dados_atualizados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}		
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

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getPixContaCorrenteFacade().consultar(getControleConsultaOtimizado(), getPixContaCorrenteVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("pixContaCorrenteCons.xhtml");
	}
	
	public String getRealizarNavegacaoParaContaReceber() {
		try {
			ContaReceberVO contaReceberVO = new ContaReceberVO();
			contaReceberVO = getPixContaCorrenteVO().getContaReceberVO();
			context().getExternalContext().getSessionMap().put("contaReceberLancamentoContabil", contaReceberVO);
			removerControleMemoriaFlash("ContaReceberControle");
			removerControleMemoriaTela("ContaReceberControle");
			return "popup('../financeiro/contaReceberForm.xhtml', 'contaReceberForm' , 1024, 800)";
		} catch (Exception e) {
			setMensagemID(MSG_TELA.msg_erro.name());
			return "";
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setPixContaCorrenteVO(new PixContaCorrenteVO());
		getPixContaCorrenteVO().setSituacaoPixEnum(SituacaoPixEnum.NENHUM);
		getPixContaCorrenteVO().setStatusPixEnum(StatusPixEnum.NENHUM);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(null);
		montarDadosContaCorrente();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao("pixContaCorrenteCons.xhtml");
	}

	public void montarDadosContaCorrente() {
		try {
			List<ContaCorrenteVO> contaCorrenteVOs  = new ArrayList<>();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				contaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteHabilitadoPix(getUnidadeEnsinoLogadoClone().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}else {
				contaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteHabilitadoPix(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} 
			getListaSelectItemContaCorrente().clear();
			getListaSelectItemContaCorrente().add(new SelectItem(0, ""));
			for (ContaCorrenteVO contaCorrenteVO : contaCorrenteVOs) {
				if (!contaCorrenteVO.getContaCaixa()) {
					getListaSelectItemContaCorrente().add(new SelectItem(contaCorrenteVO.getCodigo(), contaCorrenteVO.getDescricaoCompletaConta()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PixContaCorrenteVO getPixContaCorrenteVO() {
		if (pixContaCorrenteVO == null) {
			pixContaCorrenteVO = new PixContaCorrenteVO();
		}
		return pixContaCorrenteVO;
	}

	public void setPixContaCorrenteVO(PixContaCorrenteVO pixContaCorrenteVO) {
		this.pixContaCorrenteVO = pixContaCorrenteVO;
	}

	public List<SelectItem> getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList<>();
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public String getJsonConsultaPix() {
		if (jsonConsultaPix == null) {
			jsonConsultaPix ="";
		}
		return jsonConsultaPix;
	}

	public void setJsonConsultaPix(String jsonConsultaPix) {
		this.jsonConsultaPix = jsonConsultaPix;
	}
	
	

}
