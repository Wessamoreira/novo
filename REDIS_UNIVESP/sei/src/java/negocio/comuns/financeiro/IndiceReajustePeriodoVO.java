package negocio.comuns.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;

public class IndiceReajustePeriodoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private IndiceReajusteVO indiceReajusteVO;
	private MesAnoEnum mes;
	private String ano;
	private UsuarioVO responsavelVO;
	private Date data;
	private SituacaoExecucaoEnum situacaoExecucao;
	private BigDecimal percentualReajuste;
	private String motivoCancelamento;
	private Date dataCancelamento;
	private List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs;
	private Boolean processamentoAutomatico;
	private String logErroProcessamento;
	private String logAlunosNaoDevemSofrerReajuste;
	private Boolean considerarDescontoSemValidadeCalculoIndiceReajuste;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public IndiceReajusteVO getIndiceReajusteVO() {
		if (indiceReajusteVO == null) {
			indiceReajusteVO = new IndiceReajusteVO();
		}
		return indiceReajusteVO;
	}

	public void setIndiceReajusteVO(IndiceReajusteVO indiceReajusteVO) {
		this.indiceReajusteVO = indiceReajusteVO;
	}

	public UsuarioVO getResponsavelVO() {
		if (responsavelVO == null) {
			responsavelVO = new UsuarioVO();
		}
		return responsavelVO;
	}

	public void setResponsavelVO(UsuarioVO responsavelVO) {
		this.responsavelVO = responsavelVO;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public MesAnoEnum getMes() {
		if (mes == null) {
			mes = MesAnoEnum.JANEIRO;
		}
		return mes;
	}

	public void setMes(MesAnoEnum mes) {
		this.mes = mes;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public SituacaoExecucaoEnum getSituacaoExecucao() {
		if (situacaoExecucao == null) {
			situacaoExecucao = SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoExecucao;
	}

	public void setSituacaoExecucao(SituacaoExecucaoEnum situacaoExecucao) {
		this.situacaoExecucao = situacaoExecucao;
	}

	public String getData_Apresentar() {
		return Uteis.getDataComHora(getData());
	}

	public String getSituacaoExecucao_Apresentar() {
		return getSituacaoExecucao().getDescricao();
	}

	public BigDecimal getPercentualReajuste() {
		if (percentualReajuste == null) {
			percentualReajuste = BigDecimal.ZERO;
		}
		return percentualReajuste;
	}

	public void setPercentualReajuste(BigDecimal percentualReajuste) {
		this.percentualReajuste = percentualReajuste;
	}

	public String getOrdenacaoMes() {
		return getAno() + "_" + getMes().getKey() +"_"+ Uteis.getData(getData(), "YY/MM/DD HH:MM");
	}

	public List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs() {
		if (listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs == null) {
			listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs = new ArrayList<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO>(0);
		}
		return listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs;
	}

	public void setListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs(List<IndiceReajustePeriodoMatriculaPeriodoVencimentoVO> listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs) {
		this.listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs = listaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs;
	}

	public String getMotivoCancelamento() {
		if (motivoCancelamento == null) {
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public Date getDataCancelamento() {
		if (dataCancelamento == null) {
			dataCancelamento = new Date();
		}
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public Boolean getProcessamentoAutomatico() {
		if (processamentoAutomatico == null) {
			processamentoAutomatico = false;
		}
		return processamentoAutomatico;
	}

	public void setProcessamentoAutomatico(Boolean processamentoAutomatico) {
		this.processamentoAutomatico = processamentoAutomatico;
	}
	
	public String getMesAno_Apresentar() {
		return getMes().getMes() + "/" + getAno();
	}

	public String getLogErroProcessamento() {
		if (logErroProcessamento == null) {
			logErroProcessamento = "";
		}
		return logErroProcessamento;
	}

	public void setLogErroProcessamento(String logErroProcessamento) {
		this.logErroProcessamento = logErroProcessamento;
	}

	public String getLogAlunosNaoDevemSofrerReajuste() {
		if (logAlunosNaoDevemSofrerReajuste == null) {
			logAlunosNaoDevemSofrerReajuste = "";
		}
		return logAlunosNaoDevemSofrerReajuste;
	}

	public void setLogAlunosNaoDevemSofrerReajuste(String logAlunosNaoDevemSofrerReajuste) {
		this.logAlunosNaoDevemSofrerReajuste = logAlunosNaoDevemSofrerReajuste;
	}


	private Integer parcelaInicialReajuste;

	public Integer getParcelaInicialReajuste() {
		if(parcelaInicialReajuste == null) {
			parcelaInicialReajuste = 13;
		}
		return parcelaInicialReajuste;
	}

	public void setParcelaInicialReajuste(Integer parcelaInicialReajuste) {
		this.parcelaInicialReajuste = parcelaInicialReajuste;
	}

	public Boolean getConsiderarDescontoSemValidadeCalculoIndiceReajuste() {
		if(considerarDescontoSemValidadeCalculoIndiceReajuste == null) {
			considerarDescontoSemValidadeCalculoIndiceReajuste = false;
		}
		return considerarDescontoSemValidadeCalculoIndiceReajuste;
	}

	public void setConsiderarDescontoSemValidadeCalculoIndiceReajuste(
			Boolean considerarDescontoSemValidadeCalculoIndiceReajuste) {
		this.considerarDescontoSemValidadeCalculoIndiceReajuste = considerarDescontoSemValidadeCalculoIndiceReajuste;
	}

	
	
}
