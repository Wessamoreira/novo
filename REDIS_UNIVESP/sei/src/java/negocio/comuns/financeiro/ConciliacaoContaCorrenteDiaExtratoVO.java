package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.TotalizadorPorFormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.TipoTransacaoOFXEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;

public class ConciliacaoContaCorrenteDiaExtratoVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7112144254759139300L;
	private Integer codigo;
	private ConciliacaoContaCorrenteDiaVO conciliacaoContaCorrenteDia;
	private TipoTransacaoOFXEnum tipoTransacaoOFXEnum;
	private Integer codigoOfx;
	private String identificadorOfx;
	private Date dataOfx;
	private Double valorOfx;
	private String lancamentoOfx;
	private String documentoOfx;
	private Double saldoRegistroOfx;
	private String codigoSei;
	private Double valorSei;
	private String lancamentoSei;
	private Date dataSei;
	private TipoMovimentacaoFinanceira tipoMovimentacaoFinanceiraSei;
	private Double saldoRegistroSei;
	private List<ConciliacaoContaCorrenteDiaExtratoConjuntaVO> listaConciliacaoContaConjuntaVO;
	private List<ExtratoContaCorrenteVO> listaExtratoContaCorrente;	
	private List<TotalizadorPorFormaPagamentoVO> listaTotalizadorPorFormaPagamento;
	private Double desagruparValor;
	private Integer desagruparFormaPagamento;
	private Map<String, Double> mapaCodigoExtratoPorValor;
	private Map<String, List<ExtratoContaCorrenteVO>> mapaExtratoContaCorrentePorValor;

	public void anularDadosSei() {
		setLancamentoSei(null);
		setTipoMovimentacaoFinanceiraSei(null);
		setSaldoRegistroSei(null);
		setDataSei(null);
		setValorSei(null);
		setListaExtratoContaCorrente(null);
		setCodigoSei(null);
	}

	public void preencherDadosSei(ConciliacaoContaCorrenteDiaExtratoVO conciliacaoExtratoSei) {
		setLancamentoSei(conciliacaoExtratoSei.getLancamentoSei());
		setTipoMovimentacaoFinanceiraSei(conciliacaoExtratoSei.getTipoMovimentacaoFinanceiraSei());
		setSaldoRegistroSei(conciliacaoExtratoSei.getSaldoRegistroSei());
		setDataSei(conciliacaoExtratoSei.getDataSei());
		setValorSei(conciliacaoExtratoSei.getValorSei());
		setListaExtratoContaCorrente(conciliacaoExtratoSei.getListaExtratoContaCorrente());
		setCodigoSei(conciliacaoExtratoSei.getCodigoSei());
	}
	
	/*public void preencherDadosConciliacaoConjunta() {
		Double valorTotalExtratoOperacao = 0.0;
		Double valorExtratoPositivo = 0.0;
		Double valorOperacao = getValorOfx() < 0.0 ? getValorOfx() * -1 : getValorOfx();
		for (ExtratoContaCorrenteVO extrato : getListaExtratoContaCorrente()) {
			valorExtratoPositivo = extrato.getValor() < 0.0 ? extrato.getValor() * -1 : extrato.getValor();
			if(!extrato.getValorLancamentoConjuntoUtilizado().equals(valorExtratoPositivo)) {
				valorTotalExtratoOperacao = Uteis.arrendondarForcando2CadasDecimais(valorTotalExtratoOperacao + (valorExtratoPositivo - extrato.getValorLancamentoConjuntoUtilizado()));
				extrato.setApresentarLancamentoConjunto(extrato.getApresentarLancamentoConjunto().isEmpty() ? getLancamentoOfx() : extrato.getApresentarLancamentoConjunto() + "," + getLancamentoOfx());
				if (valorOperacao >= valorTotalExtratoOperacao) {
					extrato.setValorLancamentoConjuntoUtilizado(valorExtratoPositivo);
				}else {
					extrato.setValorLancamentoConjuntoUtilizado(Uteis.arrendondarForcando2CadasDecimais((valorExtratoPositivo -(valorTotalExtratoOperacao - valorOperacao))));
					break;
				}	
			}
		}
	}
	
	public void removerDadosConciliacaoConjunta() {
		Double valorTotalExtratoOperacao = 0.0;
		Double valorOperacao = getValorOfx() < 0.0 ? getValorOfx() * -1 : getValorOfx();
		Comparator<ExtratoContaCorrenteVO> compareByName = Comparator.comparing(ExtratoContaCorrenteVO::getQtdLancamentoConjunto).thenComparing(ExtratoContaCorrenteVO::getValorLancamentoConjuntoUtilizado);
		List<ExtratoContaCorrenteVO> lista = getListaExtratoContaCorrente()
		.stream()
		.filter(p-> p.getApresentarLancamentoConjunto().contains(getLancamentoOfx()))
		.sorted(compareByName)
		.collect(Collectors.toList());
		for (ExtratoContaCorrenteVO extrato : lista) {
			valorTotalExtratoOperacao = Uteis.arrendondarForcando2CadasDecimais(valorTotalExtratoOperacao + extrato.getValorLancamentoConjuntoUtilizado());
			extrato.setApresentarLancamentoConjunto(Uteis.removeNomeDentroListaDeString(extrato.getApresentarLancamentoConjunto(), getLancamentoOfx()));
			if (valorOperacao <= valorTotalExtratoOperacao) {
				extrato.setValorLancamentoConjuntoUtilizado(Uteis.arrendondarForcando2CadasDecimais(valorTotalExtratoOperacao - valorOperacao));
				break;
			}else {
				extrato.setValorLancamentoConjuntoUtilizado(0.0);
			}
		}
	}*/

	public void removerExtratoContaCorrente(ExtratoContaCorrenteVO extratoContaCorrente) {
		Iterator<ExtratoContaCorrenteVO> i = getListaExtratoContaCorrente().iterator();
		while (i.hasNext()) {
			ExtratoContaCorrenteVO objExistente = i.next();
			if (objExistente.getCodigo().equals(extratoContaCorrente.getCodigo())) {
				i.remove();
				break;
			}
		}
	}

	public Boolean isExisteExtratoContaCorrente(ExtratoContaCorrenteVO extratoContaCorrente) {
		String[] listaCodigosTemp = getCodigoSei().split(",");
		for (int i = 0; i < listaCodigosTemp.length; i++) {
			if (listaCodigosTemp[i].equals(extratoContaCorrente.getCodigo().toString())) {
				return true;
			}
		}
		return false;
	}

	public TipoTransacaoOFXEnum getTipoTransacaoOFXEnum() {
		return tipoTransacaoOFXEnum;
	}

	public void setTipoTransacaoOFXEnum(TipoTransacaoOFXEnum tipoTransacaoOFXEnum) {
		this.tipoTransacaoOFXEnum = tipoTransacaoOFXEnum;
	}

	public Integer getCodigoOfx() {
		if (codigoOfx == null) {
			codigoOfx = 0;
		}
		return codigoOfx;
	}

	public void setCodigoOfx(Integer codigoOfx) {
		this.codigoOfx = codigoOfx;
	}
	

	public String getIdentificadorOfx() {
		if (identificadorOfx == null) {
			identificadorOfx = "";
		}
		return identificadorOfx;
	}

	public void setIdentificadorOfx(String identificadorOfx) {
		this.identificadorOfx = identificadorOfx;
	}

	public Date getDataOfx() {
		return dataOfx;
	}

	public void setDataOfx(Date dataTransacaoOFX) {
		this.dataOfx = dataTransacaoOFX;
	}

	public Double getValorOfx() {
		if (valorOfx == null) {
			valorOfx = 0.0;
		}
		return valorOfx;
	}

	public void setValorOfx(Double valorTransacaoOFX) {
		this.valorOfx = valorTransacaoOFX;
	}

	public String getLancamentoOfx() {
		if (lancamentoOfx == null) {
			lancamentoOfx = "";
		}
		return lancamentoOfx;
	}

	public void setLancamentoOfx(String lancamentoTransacaoOFX) {
		this.lancamentoOfx = lancamentoTransacaoOFX;
	}

	public String getDocumentoOfx() {
		if (documentoOfx == null) {
			documentoOfx = "";
		}
		return documentoOfx;
	}

	public void setDocumentoOfx(String documentoTransacaoOFX) {
		this.documentoOfx = documentoTransacaoOFX;
	}

	public ConciliacaoContaCorrenteDiaVO getConciliacaoContaCorrenteDia() {
		if (conciliacaoContaCorrenteDia == null) {
			conciliacaoContaCorrenteDia = new ConciliacaoContaCorrenteDiaVO();
		}
		return conciliacaoContaCorrenteDia;
	}

	public void setConciliacaoContaCorrenteDia(ConciliacaoContaCorrenteDiaVO conciliacaoContaCorrenteDia) {
		this.conciliacaoContaCorrenteDia = conciliacaoContaCorrenteDia;
	}

	public Double getSaldoRegistroOfx() {
		if (saldoRegistroOfx == null) {
			saldoRegistroOfx = 0.0;
		}
		return saldoRegistroOfx;
	}

	public void setSaldoRegistroOfx(Double saldoRegistroTransacao) {
		this.saldoRegistroOfx = saldoRegistroTransacao;
	}

	public Double getValorSei() {
		if (valorSei == null) {
			valorSei = 0.0;
		}
		return valorSei;
	}

	public void setValorSei(Double valorSei) {
		this.valorSei = valorSei;
	}
	
	public String getLancamentoSei() {
		if (lancamentoSei == null) {
			lancamentoSei = "";
		}
		return lancamentoSei;
	}

	public void setLancamentoSei(String lancamentoSei) {
		this.lancamentoSei = lancamentoSei;
	}

	public Double getSaldoRegistroSei() {
		if (saldoRegistroSei == null) {
			saldoRegistroSei = 0.0;
		}
		return saldoRegistroSei;
	}

	public void setSaldoRegistroSei(Double saldoRegistroSei) {
		this.saldoRegistroSei = saldoRegistroSei;
	}

	public TipoMovimentacaoFinanceira getTipoMovimentacaoFinanceiraSei() {
		return tipoMovimentacaoFinanceiraSei;
	}

	public void setTipoMovimentacaoFinanceiraSei(TipoMovimentacaoFinanceira tipoMovimentacaoFinanceiraSei) {
		this.tipoMovimentacaoFinanceiraSei = tipoMovimentacaoFinanceiraSei;
	}

	public Date getDataSei() {
		return dataSei;
	}

	public void setDataSei(Date dataSei) {
		this.dataSei = dataSei;
	}

	public String getCodigoSei() {
		if (codigoSei == null) {
			codigoSei = "";
		}
		return codigoSei;
	}

	public void setCodigoSei(String codigoSei) {
		this.codigoSei = codigoSei;
	}

	public List<ConciliacaoContaCorrenteDiaExtratoConjuntaVO> getListaConciliacaoContaConjuntaVO() {
		if (listaConciliacaoContaConjuntaVO == null) {
			listaConciliacaoContaConjuntaVO = new ArrayList<>();
		}
		return listaConciliacaoContaConjuntaVO;
	}

	public void setListaConciliacaoContaConjuntaVO(List<ConciliacaoContaCorrenteDiaExtratoConjuntaVO> listaConciliacaoContaConjuntaVO) {
		this.listaConciliacaoContaConjuntaVO = listaConciliacaoContaConjuntaVO;
	}
	
	public boolean isExisteConciliacaoConjunta() {
		return Uteis.isAtributoPreenchido(getListaConciliacaoContaConjuntaVO());
	}

	public List<ExtratoContaCorrenteVO> getListaExtratoContaCorrente() {
		if (listaExtratoContaCorrente == null) {
			listaExtratoContaCorrente = new ArrayList<>();
		}
		return listaExtratoContaCorrente;
	}

	public void setListaExtratoContaCorrente(List<ExtratoContaCorrenteVO> listaExtratoContaCorrente) {
		this.listaExtratoContaCorrente = listaExtratoContaCorrente;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getDesagruparValor() {
		if (desagruparValor == null) {
			desagruparValor = 0.0;
		}
		return desagruparValor;
	}

	public void setDesagruparValor(Double desagruparValor) {
		this.desagruparValor = desagruparValor;
	}

	public Integer getDesagruparFormaPagamento() {
		if (desagruparFormaPagamento == null) {
			desagruparFormaPagamento = 0;
		}
		return desagruparFormaPagamento;
	}

	public void setDesagruparFormaPagamento(Integer desagruparFormaPagamento) {
		this.desagruparFormaPagamento = desagruparFormaPagamento;
	}	

	public Map<String, List<ExtratoContaCorrenteVO>> getMapaExtratoContaCorrentePorValor() {
		if (mapaExtratoContaCorrentePorValor == null) {
			mapaExtratoContaCorrentePorValor = new HashMap<>();
		}
		return mapaExtratoContaCorrentePorValor;
	}

	public void setMapaExtratoContaCorrentePorValor(Map<String, List<ExtratoContaCorrenteVO>> mapaExtratoContaCorrentePorValor) {
		this.mapaExtratoContaCorrentePorValor = mapaExtratoContaCorrentePorValor;
	}

	public Map<String, Double> getMapaCodigoExtratoPorValor() {
		if (mapaCodigoExtratoPorValor == null) {
			mapaCodigoExtratoPorValor = new HashMap<>();
		}
		return mapaCodigoExtratoPorValor;
	}

	public void setMapaCodigoExtratoPorValor(Map<String, Double> mapaCodigoExtratoPorValor) {
		this.mapaCodigoExtratoPorValor = mapaCodigoExtratoPorValor;
	}

	public boolean isApresentarListaTotalizadorPorFormaPagamento() {
		return Uteis.isAtributoPreenchido(getListaTotalizadorPorFormaPagamento()) && getListaTotalizadorPorFormaPagamento().size() > 1;
	}

	public List<TotalizadorPorFormaPagamentoVO> getListaTotalizadorPorFormaPagamento() {
		if (listaTotalizadorPorFormaPagamento == null) {
			listaTotalizadorPorFormaPagamento = new ArrayList<>();
		}
		return listaTotalizadorPorFormaPagamento;
	}

	public void setListaTotalizadorPorFormaPagamento(List<TotalizadorPorFormaPagamentoVO> listaTotalizadorPorFormaPagamento) {
		this.listaTotalizadorPorFormaPagamento = listaTotalizadorPorFormaPagamento;
	}

	public void atualizarListaTotalizadores() {
		getListaTotalizadorPorFormaPagamento().clear();
		Map<String, List<ExtratoContaCorrenteVO>> map = getListaExtratoContaCorrente().stream().collect(Collectors.groupingBy(p -> p.getFormaPagamentoConciliacao_key()));
		for (Map.Entry<String, List<ExtratoContaCorrenteVO>> mapa : map.entrySet()) {
			TotalizadorPorFormaPagamentoVO tpfp = new TotalizadorPorFormaPagamentoVO();
			tpfp.setFormaPagamentoVO(mapa.getValue().get(0).getFormaPagamento());
			tpfp.setOperadoraCartaoVO(mapa.getValue().get(0).getOperadoraCartaoVO());
			tpfp.setNomeApresentacao(mapa.getValue().get(0).getFormaPagamentoConciliacao());
			mapa.getValue().stream().forEach(p -> {
				if (getTipoMovimentacaoFinanceiraSei().isMovimentacaoEntrada()) {
					tpfp.setValor(Uteis.arrendondarForcando2CadasDecimais(tpfp.getValor() + p.getValor()));
				} else {
					tpfp.setValor(Uteis.arrendondarForcando2CadasDecimais(tpfp.getValor() - p.getValor()));
				}
			});
			if (getTipoMovimentacaoFinanceiraSei().isMovimentacaoSaida()) {
				tpfp.setValor(tpfp.getValor() * -1);
			}
			getListaTotalizadorPorFormaPagamento().add(tpfp);
		}
	}
	
	public Double getTotalValorExtratoContaCorrente() {
		return getListaExtratoContaCorrente().stream().mapToDouble(p-> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	/*public Double getTotalValorLancamentoConjuntoUtilizado() {
		return getListaExtratoContaCorrente().stream().mapToDouble(p-> p.getValorLancamentoConjuntoUtilizado()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}*/
	
	public Double getTotalValorConciliacaoConjunta() {
		return getListaConciliacaoContaConjuntaVO().stream().mapToDouble(p-> p.getValorOfx()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public String getDragType() {
		return getTipoMovimentacaoFinanceiraSei().getValor();
	}	
	

	public boolean isSomenteUmExtratoContaCorrente() {
		if (!Uteis.isAtributoPreenchido(getCodigoSei())) {
			return false;
		}
		return (StringUtils.countMatches(getCodigoSei(), ",") + 1) == 1;
	}

	public boolean isExtratoTransacaoExistente() {
		return getCodigoOfx() != null && getCodigoOfx() != 0;
	}

	public boolean isExtratoSeiExistente() {
		return Uteis.isAtributoPreenchido(getCodigoSei());
	}

	public boolean isExtratoConciliado() {
		return isExtratoTransacaoExistente() && isExtratoSeiExistente();
	}

	public boolean isTipoTransacaoEntrada() {
		return Uteis.isAtributoPreenchido(getTipoTransacaoOFXEnum()) && (getTipoTransacaoOFXEnum().isTipoCredito() || getTipoTransacaoOFXEnum().isTipoDeposito() || getTipoTransacaoOFXEnum().isTipoDepositoDireto() || getTipoTransacaoOFXEnum().isTipoPagamentoEletronico() || ((getTipoTransacaoOFXEnum().isTipoVerificar() || getTipoTransacaoOFXEnum().isTipoTransferir() || getTipoTransacaoOFXEnum().isTipoOutros()) && getValorOfx() >= 0.0));
	}

	public boolean isTipoTransacaoSaida() {
		return Uteis.isAtributoPreenchido(getTipoTransacaoOFXEnum()) && (getTipoTransacaoOFXEnum().isTipoDebito() || getTipoTransacaoOFXEnum().isTipoRetiradaDinheiro() || getTipoTransacaoOFXEnum().isTipoTaxaFinanceira() || getTipoTransacaoOFXEnum().isTipoTaxaServico() || ((getTipoTransacaoOFXEnum().isTipoVerificar() || getTipoTransacaoOFXEnum().isTipoTransferir() || getTipoTransacaoOFXEnum().isTipoOutros()) && getValorOfx() < 0.0));
	}
	
	@Override
    public String toString() {
        return "ConciliacaoContaCorrenteDiaExtratoVO [codigo=" + getCodigo() + ", codigoOFX=" + getCodigoOfx() +", identificadorOFX="+getIdentificadorOfx()+ ", lancamentoOFX=" + getLancamentoOfx() + ", codigoSei=" + getCodigoSei()+ "]";
    }

}
