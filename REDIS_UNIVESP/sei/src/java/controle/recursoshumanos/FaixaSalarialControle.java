package controle.recursoshumanos;

import java.io.Serializable;
import java.util.ArrayList;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.FaixaSalarialVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO.EnumCampoConsultaNivelSalarial;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Gilberto Nery
 *
 */
@Controller("FaixaSalarialControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings({"rawtypes"})
public class FaixaSalarialControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -6923956977408427363L;
	
	private final String TELA_FORM = "faixaSalarialForm";
	private final String TELA_CONS = "faixaSalarialCons";
	private final String CONTEXT_PARA_EDICAO = "itemPesquisado";

	private FaixaSalarialVO faixaSalarialVO;
	
	public FaixaSalarialControle() {
		setControleConsultaOtimizado(new DataModelo());
		inicializarConsultar();
	}

	public String novo() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			faixaSalarialVO = (FaixaSalarialVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setFaixaSalarialVO(faixaSalarialVO);
			setControleConsultaOtimizado(new DataModelo());
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getFaixaSalarialInterfaceFacade().persistir(getFaixaSalarialVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarDados();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}


	@Override
	public void consultarDados() {
		try {
			super.consultar();
			if(getControleConsultaOtimizado().getCampoConsulta().equals(EnumCampoConsultaNivelSalarial.VALOR.name())) {
				if(getControleConsultaOtimizado().getValorConsulta().trim().isEmpty() || !Uteis.getIsValorNumerico(getControleConsultaOtimizado().getValorConsulta())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_NivelSalarial_ConsultaCampoValorInvalido"));
				}
			}
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getFacadeFactory().getFaixaSalarialInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	

	public String excluir() {
		try {
			getFacadeFactory().getFaixaSalarialInterfaceFacade().excluir(getFaixaSalarialVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void irPaginaInicial() throws Exception {
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
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setCampoConsulta(EnumCampoConsultaNivelSalarial.DESCRICAO.name());
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public FaixaSalarialVO getFaixaSalarialVO() {
		if (faixaSalarialVO == null)
			faixaSalarialVO = new FaixaSalarialVO();
		return faixaSalarialVO;
	}

	public void setFaixaSalarialVO(FaixaSalarialVO nivelSalarialVO) {
		this.faixaSalarialVO = nivelSalarialVO;
	}

}