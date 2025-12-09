package negocio.comuns.recursoshumanos;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoMarcacaoFeriasEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class MarcacaoFeriasVO extends SuperVO {

	private static final long serialVersionUID = 6170740246110455573L;
	
	private Integer codigo;
	private PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO;
	private FuncionarioCargoVO funcionarioCargoVO;
	private Date dataInicioGozo;
	private Date dataFinalGozo;
	private Integer qtdDias;
	private Boolean abono;
	private Integer qtdDiasAbono;
	private Boolean pagarParcela13;
	private Date dataPagamento;
	private Date dataInicioAviso;
	private SituacaoMarcacaoFeriasEnum situacaoMarcacao;
	private String informacoesAdicionais;
	
	private Date dataLimiteGozo;
	private Integer qtdFaltasPeriodo;
	
	public enum EnumCampoConsultaFuncionarioMarcacaoFerias{
		MATRICULA_CARGO, FUNCIONARIO;
	}
	
	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public PeriodoAquisitivoFeriasVO getPeriodoAquisitivoFeriasVO() {
		if (periodoAquisitivoFeriasVO == null)
			periodoAquisitivoFeriasVO = new PeriodoAquisitivoFeriasVO();
		return periodoAquisitivoFeriasVO;
	}
	public void setPeriodoAquisitivoFeriasVO(PeriodoAquisitivoFeriasVO periodoAquisitivoFeriasVO) {
		this.periodoAquisitivoFeriasVO = periodoAquisitivoFeriasVO;
	}
	
	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null)
			funcionarioCargoVO = new FuncionarioCargoVO();
		return funcionarioCargoVO;
	}
	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}
	
	public Date getDataInicioGozo() {
		if (dataInicioGozo == null)
			dataInicioGozo = new Date();
		return dataInicioGozo;
	}
	public void setDataInicioGozo(Date dataInicioGozo) {
		this.dataInicioGozo = dataInicioGozo;
	}
	
	public Date getDataFinalGozo() {
		if (dataFinalGozo == null)
			dataFinalGozo = UteisData.adicionarDiasEmData(new Date(), getQtdDias()-getQtdDiasAbono());
		return dataFinalGozo;
	}
	public void setDataFinalGozo(Date dataFinalGozo) {
		this.dataFinalGozo = dataFinalGozo;
	}
	
	public Integer getQtdDias() {
		if (qtdDias == null)
			qtdDias = 30;
		return qtdDias;
	}
	public void setQtdDias(Integer qtdDias) {
		this.qtdDias = qtdDias;
	}
	
	public Boolean getAbono() {
		if (abono == null)
			abono = false;
		return abono;
	}
	public void setAbono(Boolean abono) {
		this.abono = abono;
	}
	
	public Integer getQtdDiasAbono() {
		if (qtdDiasAbono == null)
			qtdDiasAbono = 0;
		return qtdDiasAbono;
	}
	public void setQtdDiasAbono(Integer qtdDiasAbono) {
		this.qtdDiasAbono = qtdDiasAbono;
	}
	
	public Boolean getPagarParcela13() {
		if (pagarParcela13 == null)
			pagarParcela13 = false;
		return pagarParcela13;
	}
	public void setPagarParcela13(Boolean pagarParcela13) {
		this.pagarParcela13 = pagarParcela13;
	}
	
	public Date getDataPagamento() {
		if (dataPagamento == null)
			dataPagamento = new Date();
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	public Date getDataInicioAviso() {
		if (dataInicioAviso == null)
			dataInicioAviso = new Date();
		return dataInicioAviso;
	}
	public void setDataInicioAviso(Date dataInicioAviso) {
		this.dataInicioAviso = dataInicioAviso;
	}
	
	public SituacaoMarcacaoFeriasEnum getSituacaoMarcacao() {
		return situacaoMarcacao;
	}
	public void setSituacaoMarcacao(SituacaoMarcacaoFeriasEnum situacaoMarcacao) {
		this.situacaoMarcacao = situacaoMarcacao;
	}
	public Date getDataLimiteGozo() {
		if (dataLimiteGozo == null)
			dataLimiteGozo = UteisData.obterDataFuturaAdicionandoMes(getPeriodoAquisitivoFeriasVO().getFinalPeriodo(), 12);
		return dataLimiteGozo;
	}
	public void setDataLimiteGozo(Date dataLimiteGozo) {
		this.dataLimiteGozo = dataLimiteGozo;
	}
	public Integer getQtdFaltasPeriodo() {
		if (qtdFaltasPeriodo == null)
			qtdFaltasPeriodo = 0;
		return qtdFaltasPeriodo;
	}
	public void setQtdFaltasPeriodo(Integer qtdFaltasPeriodo) {
		this.qtdFaltasPeriodo = qtdFaltasPeriodo;
	}
	public String getInformacoesAdicionais() {
		if (informacoesAdicionais == null)
			informacoesAdicionais = "";
		return informacoesAdicionais;
	}
	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}
}