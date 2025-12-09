package controle.recursoshumanos;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.utilitarias.Uteis;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas atualizarValeTrasnporte.xhtml.
 * 
 * @see SuperControle
 */
@Controller("AtualizarValeTransporteControle")
@Scope("viewScope")
public class AtualizarValeTransporteControle extends SuperControle {

	private static final long serialVersionUID = 6501172077967811071L;

	private static final String EVENTO_VALE_TRANPORTE_ATUALIZADO = "msg_EventoValeTransporte_atualizado";

	private Integer quantidadeDiasUteisDe;
	private Integer quantidadeDiasUteisPara;

	private Integer quantidadeDiasUteisMeioExpedienteDe;
	private Integer quantidadeDiasUteisMeioExpedientePara;

	public void consultarCargos() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_COMBOBOX,
					getUsuarioLogado());
			getFacadeFactory().getCargoFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado());
			getControleConsultaOtimizado().getListaConsulta();

			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		this.consultarCargos();
	}

	public void atualizarValeTransporte() {
		try {
			getFacadeFactory().getEventoValeTransporteFuncionarioCargoInterfaceFacade().atualizarValeTransportePorQuantidadeDias(
					getQuantidadeDiasUteisDe(), getQuantidadeDiasUteisPara(), getQuantidadeDiasUteisMeioExpedienteDe(), getQuantidadeDiasUteisMeioExpedientePara(), getUsuarioLogado());
			
			setMensagemID(EVENTO_VALE_TRANPORTE_ATUALIZADO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public Integer getQuantidadeDiasUteisDe() {
		return quantidadeDiasUteisDe;
	}

	public void setQuantidadeDiasUteisDe(Integer quantidadeDiasUteisDe) {
		this.quantidadeDiasUteisDe = quantidadeDiasUteisDe;
	}

	public Integer getQuantidadeDiasUteisPara() {
		return quantidadeDiasUteisPara;
	}

	public void setQuantidadeDiasUteisPara(Integer quantidadeDiasUteisPara) {
		this.quantidadeDiasUteisPara = quantidadeDiasUteisPara;
	}

	public Integer getQuantidadeDiasUteisMeioExpedienteDe() {
		return quantidadeDiasUteisMeioExpedienteDe;
	}

	public void setQuantidadeDiasUteisMeioExpedienteDe(Integer quantidadeDiasUteisMeioExpedienteDe) {
		this.quantidadeDiasUteisMeioExpedienteDe = quantidadeDiasUteisMeioExpedienteDe;
	}

	public Integer getQuantidadeDiasUteisMeioExpedientePara() {
		return quantidadeDiasUteisMeioExpedientePara;
	}

	public void setQuantidadeDiasUteisMeioExpedientePara(Integer quantidadeDiasUteisMeioExpedientePara) {
		this.quantidadeDiasUteisMeioExpedientePara = quantidadeDiasUteisMeioExpedientePara;
	}
}
