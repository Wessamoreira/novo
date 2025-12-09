package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;

public class MapaRemessaLoteContaPagarVO {

	private TipoServicoContaPagarEnum tipoServicoContaPagar;
	private TipoLancamentoContaPagarEnum tipoLancamentoContaPagar;
	private List<ContaPagarControleRemessaContaPagarVO> listaRemessaContaPagar;
	private Double totalRemessaContaPagar;

	public List<ContaPagarControleRemessaContaPagarVO> getListaRemessaContaPagar() {
		if (listaRemessaContaPagar == null) {
			listaRemessaContaPagar = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
		}
		return listaRemessaContaPagar;
	}

	public void setListaRemessaContaPagar(List<ContaPagarControleRemessaContaPagarVO> listaRemessaContaPagar) {
		this.listaRemessaContaPagar = listaRemessaContaPagar;
	}

	public Double getTotalRemessaContaPagar() {
		if (totalRemessaContaPagar == null) {
			totalRemessaContaPagar = 0.0;
		}
		return totalRemessaContaPagar;
	}

	public void setTotalRemessaContaPagar(Double totalRemessaContaPagar) {
		this.totalRemessaContaPagar = totalRemessaContaPagar;
	}

	public TipoServicoContaPagarEnum getTipoServicoContaPagar() {
		return tipoServicoContaPagar;
	}

	public void setTipoServicoContaPagar(TipoServicoContaPagarEnum tipoServicoContaPagar) {
		this.tipoServicoContaPagar = tipoServicoContaPagar;
	}

	public TipoLancamentoContaPagarEnum getTipoLancamentoContaPagar() {
		return tipoLancamentoContaPagar;
	}

	public void setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum tipoLancamentoContaPagar) {
		this.tipoLancamentoContaPagar = tipoLancamentoContaPagar;
	}

	public boolean equalsMapaRemessaLoteContaPagarVO(MapaRemessaLoteContaPagarVO obj) {
		return getTipoServicoContaPagar().equals(obj.getTipoServicoContaPagar())
				&& getTipoLancamentoContaPagar().equals(obj.getTipoLancamentoContaPagar());
	}

	public boolean equalsMapaRemessaLoteContaPagarVO(TipoServicoContaPagarEnum tipoServicoContaPagarEnum,
			TipoLancamentoContaPagarEnum tipoLancamentoContaPagarEnum) {
		return getTipoServicoContaPagar().equals(tipoServicoContaPagarEnum)
				&& getTipoLancamentoContaPagar().equals(tipoLancamentoContaPagarEnum);
	}

	public String getNumeroVersaoLote() {
		if (getTipoLancamentoContaPagar().isCreditoContaCorrente()
				|| getTipoLancamentoContaPagar().isCreditoContaPoupanca()
				|| getTipoLancamentoContaPagar().isTransferencia()
				|| getTipoLancamentoContaPagar().isCaixaAutenticacao()
				|| getTipoLancamentoContaPagar().isOrdemPagamento()) {
			return "031";
		} else if (getTipoLancamentoContaPagar().isOrdemCreditoTeleprocessamento()
				|| getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSantander()
				|| getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco()) {
			return "030";
		} else if (getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra()
				|| getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra()
				|| getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()
				|| getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()
				|| getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra()
				|| getTipoLancamentoContaPagar().isGpsSemCodigoBarra() || getTipoLancamentoContaPagar().isDpvatRenavam()
				|| getTipoLancamentoContaPagar().isIpvaComRenavam()
				|| getTipoLancamentoContaPagar().isLicenciamentoComRenavam()
				|| getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()) {
			return "010";
		} else if (getTipoLancamentoContaPagar().isPagamentoSalario()) {
			return "041";
		}
		return "";
	}

}
