package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class ConciliacaoContaCorrenteDiaVO extends SuperVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8270171144265876311L;
	private Integer codigo;
	private Date data;
	private ConciliacaoContaCorrenteVO conciliacaoContaCorrente;
	private List<ConciliacaoContaCorrenteDiaExtratoVO> listaConciliacaoContaCorrenteExtrato;
	private Double saldoAnteriorOfx;
	private Double saldoFinalOfx;
	private Double saldoAnteriorSei;
	private Double saldoFinalSei;

	private Double totalValorOfxDebito;
	private Double totalValorOfxCredito;
	private Double totalValorSeiDebito;
	private Double totalValorSeiCredito;

	private String listaCodigoExtratoContaCorrenteExistente;
	private List<ConciliacaoContaCorrenteDiaExtratoVO> listaConciliacaoContaCorrenteExtratoExcluir;
	
	boolean isUltimoExtratoTransacaoDia = true;
	
	
	
	public boolean isUltimoExtratoTransacaoDia() {
		return isUltimoExtratoTransacaoDia;
	}

	public void setUltimoExtratoTransacaoDia(boolean isUltimoExtratoTransacaoDia) {
		this.isUltimoExtratoTransacaoDia = isUltimoExtratoTransacaoDia;
	}

	public void zeraTotalizadoresCreditoDebitoOFX(){
		setTotalValorOfxDebito(0.0);
		setTotalValorOfxCredito(0.0);
	}
	
	public void zeraTotalizadoresCreditoDebitoSEI(){
		setTotalValorSeiDebito(0.0);
		setTotalValorSeiCredito(0.0);
	}

	public ConciliacaoContaCorrenteVO getConciliacaoContaCorrente() {
		if (conciliacaoContaCorrente == null) {
			conciliacaoContaCorrente = new ConciliacaoContaCorrenteVO();
		}
		return conciliacaoContaCorrente;
	}

	public void setConciliacaoContaCorrente(ConciliacaoContaCorrenteVO conciliacaoContaCorrente) {
		this.conciliacaoContaCorrente = conciliacaoContaCorrente;
	}

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
	

	public String getDiaComMes_Apresentar() {
		if(Uteis.isAtributoPreenchido(getData())){
			return UteisData.getDiaMesData(getData()) + " / " + Uteis.getMesReferenciaAbreviadoExtenso(UteisData.getMesData(getData()));	
		}
		return "";
		
	}

	
	public Integer getTamanhoListaConciliacaoContaCorrenteExtrato(){
		return getListaConciliacaoContaCorrenteExtrato().size();
	}

	public List<ConciliacaoContaCorrenteDiaExtratoVO> getListaConciliacaoContaCorrenteExtrato() {
		if (listaConciliacaoContaCorrenteExtrato == null) {
			listaConciliacaoContaCorrenteExtrato = new ArrayList<ConciliacaoContaCorrenteDiaExtratoVO>();
		}
		return listaConciliacaoContaCorrenteExtrato;
	}

	public void setListaConciliacaoContaCorrenteExtrato(List<ConciliacaoContaCorrenteDiaExtratoVO> listaExtratoContaCorrente) {
		this.listaConciliacaoContaCorrenteExtrato = listaExtratoContaCorrente;
	}

	public Double getTotalValorOfxDebito() {
		if (totalValorOfxDebito == null) {
			totalValorOfxDebito = 0.0;
		}
		return totalValorOfxDebito;
	}

	public void setTotalValorOfxDebito(Double totalValorOfxDebito) {
		this.totalValorOfxDebito = totalValorOfxDebito;
	}

	public Double getTotalValorOfxCredito() {
		if (totalValorOfxCredito == null) {
			totalValorOfxCredito = 0.0;
		}
		return totalValorOfxCredito;
	}

	public void setTotalValorOfxCredito(Double totalValorOfxCredito) {
		this.totalValorOfxCredito = totalValorOfxCredito;
	}

	public Double getTotalValorSeiDebito() {
		if (totalValorSeiDebito == null) {
			totalValorSeiDebito = 0.0;
		}
		return totalValorSeiDebito;
	}

	public void setTotalValorSeiDebito(Double totalValorSeiDebito) {
		this.totalValorSeiDebito = totalValorSeiDebito;
	}

	public Double getTotalValorSeiCredito() {
		if (totalValorSeiCredito == null) {
			totalValorSeiCredito = 0.0;
		}
		return totalValorSeiCredito;
	}

	public void setTotalValorSeiCredito(Double totalValorSeiCredito) {
		this.totalValorSeiCredito = totalValorSeiCredito;
	}

	public String getListaCodigoExtratoContaCorrenteExistente() {
		if (listaCodigoExtratoContaCorrenteExistente == null) {
			listaCodigoExtratoContaCorrenteExistente = "0";
		}
		return listaCodigoExtratoContaCorrenteExistente;
	}

	public void setListaCodigoExtratoContaCorrenteExistente(String listaCodigoExtratoContaCorrenteExistente) {
		this.listaCodigoExtratoContaCorrenteExistente = listaCodigoExtratoContaCorrenteExistente;
	}

	public Double getSaldoAnteriorOfx() {
		if (saldoAnteriorOfx == null) {
			saldoAnteriorOfx = 0.0;
		}
		return saldoAnteriorOfx;
	}

	public void setSaldoAnteriorOfx(Double saldoAnteriorOfx) {
		this.saldoAnteriorOfx = saldoAnteriorOfx;
	}

	public Double getSaldoFinalOfx() {
		if (saldoFinalOfx == null) {
			saldoFinalOfx = 0.0;
		}
		return saldoFinalOfx;
	}

	public void setSaldoFinalOfx(Double saldoFinalOfx) {
		this.saldoFinalOfx = saldoFinalOfx;
	}

	public Double getSaldoAnteriorSei() {
		if (saldoAnteriorSei == null) {
			saldoAnteriorSei = 0.0;
		}
		return saldoAnteriorSei;
	}

	public void setSaldoAnteriorSei(Double saldoAnteriorSei) {
		this.saldoAnteriorSei = saldoAnteriorSei;
	}

	public Double getSaldoFinalSei() {
		if (saldoFinalSei == null) {
			saldoFinalSei = 0.0;
		}
		return saldoFinalSei;
	}

	public void setSaldoFinalSei(Double saldoFinalSei) {
		this.saldoFinalSei = saldoFinalSei;
	}

	public List<ConciliacaoContaCorrenteDiaExtratoVO> getListaConciliacaoContaCorrenteExtratoExcluir() {
		if (listaConciliacaoContaCorrenteExtratoExcluir == null) {
			listaConciliacaoContaCorrenteExtratoExcluir = new ArrayList<>();
		}
		return listaConciliacaoContaCorrenteExtratoExcluir;
	}

	public void setListaConciliacaoContaCorrenteExtratoExcluir(List<ConciliacaoContaCorrenteDiaExtratoVO> listaConciliacaoContaCorrenteExtratoExcluir) {
		this.listaConciliacaoContaCorrenteExtratoExcluir = listaConciliacaoContaCorrenteExtratoExcluir;
	}
	
	
	
	

}
