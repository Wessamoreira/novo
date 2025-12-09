/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Otimize-04
 */
public class ComissionamentoTurmaVO extends SuperVO implements Serializable {
    private Integer codigo;
    private TurmaVO turmaVO;
    private Integer qtdeParcela;
    private Double valorTotalAReceberTurma;
    private Double ticketMedio;
    private Integer totalAlunosPagantes;
    private Date dataPrimeiroPagamento;
    private Date dataUltimoPagamento;
    private ConfiguracaoRankingVO configuracaoRankingVO;
    private List<ComissionamentoTurmaFaixaValorVO> listaComissionamentoPorTurmaFaixaValorVOs;
    private Boolean considerarTicketMedio;	

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public Integer getQtdeParcela() {
        if (qtdeParcela == null) {
            qtdeParcela = 0;
        }
        return qtdeParcela;
    }

    public void setQtdeParcela(Integer qtdeParcela) {
        this.qtdeParcela = qtdeParcela;
    }

    public Date getDataPrimeiroPagamento() {
    	if (dataPrimeiroPagamento == null) {
    		dataPrimeiroPagamento = new Date();
    	}
        return dataPrimeiroPagamento;
    }
    
    public String getDataPrimeiroPagamento_Apresentar() {    	
    	return Uteis.getData(dataPrimeiroPagamento);
    }

    public void setDataPrimeiroPagamento(Date dataPrimeiroPagamento) {
        this.dataPrimeiroPagamento = dataPrimeiroPagamento;
    }

    public Date getDataUltimoPagamento() {
    	if (dataUltimoPagamento == null) {
    		dataUltimoPagamento = new Date();
    	}
        return dataUltimoPagamento;
    }

    public void setDataUltimoPagamento(Date dataUltimoPagamento) {
        this.dataUltimoPagamento = dataUltimoPagamento;
    }

    public List<ComissionamentoTurmaFaixaValorVO> getListaComissionamentoPorTurmaFaixaValorVOs() {
        if (listaComissionamentoPorTurmaFaixaValorVOs == null) {
            listaComissionamentoPorTurmaFaixaValorVOs = new ArrayList<ComissionamentoTurmaFaixaValorVO>(0);
        }
        return listaComissionamentoPorTurmaFaixaValorVOs;
    }

    public void setListaComissionamentoPorTurmaFaixaValorVOs(List<ComissionamentoTurmaFaixaValorVO> listaComissionamentoPorTurmaFaixaValorVOs) {
        this.listaComissionamentoPorTurmaFaixaValorVOs = listaComissionamentoPorTurmaFaixaValorVOs;
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

    public ConfiguracaoRankingVO getConfiguracaoRankingVO() {
        if (configuracaoRankingVO == null) {
            configuracaoRankingVO = new ConfiguracaoRankingVO();
        }
        return configuracaoRankingVO;
    }

    public void setConfiguracaoRankingVO(ConfiguracaoRankingVO configuracaoRankingVO) {
        this.configuracaoRankingVO = configuracaoRankingVO;
    }

	public Double getValorTotalAReceberTurma() {
		if (valorTotalAReceberTurma == null) {
			valorTotalAReceberTurma = new Double(0.0);
		}
		return valorTotalAReceberTurma;
	}

	public void setValorTotalAReceberTurma(Double valorTotalAReceberTurma) {
		this.valorTotalAReceberTurma = valorTotalAReceberTurma;
	}

	public Integer getTotalAlunosPagantes() {
		if (totalAlunosPagantes == null) {
			totalAlunosPagantes = 0;
		}
		return totalAlunosPagantes;
	}

	public void setTotalAlunosPagantes(Integer totalAlunosPagantes) {
		this.totalAlunosPagantes = totalAlunosPagantes;
	}

	public Double getTicketMedio() {
		if (ticketMedio == null) {
			ticketMedio = 0.0;
		}
		return ticketMedio;
	}

	public void setTicketMedio(Double ticketMedio) {
		this.ticketMedio = ticketMedio;
	}
	
	public Boolean getConsiderarTicketMedio() {
		if (considerarTicketMedio == null) {
			considerarTicketMedio = Boolean.TRUE;
		}
		return considerarTicketMedio;
	}

	public void setConsiderarTicketMedio(Boolean considerarTicketMedio) {
		this.considerarTicketMedio = considerarTicketMedio;
	}
	
}
