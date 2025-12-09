/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Otimize-Not
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.utilitarias.Uteis;

public class PainelGestorMonitoramentoDescontoMesAnoVO implements Serializable {

    private String mesAno;
    private Double valorFaturadoMes;
    private Double valorRecebidoMes;
    private Double totalDescontoMes;
    private Double totalDescontoProgressivo;
    private Double totalDescontoAluno;
    private Double totalDescontoInstituicao;
    private Double totalDescontoConvenio;
    private Double totalDescontoRateio;
    private Double totalDescontoRecebimento;
    private Integer totalAlunoReceberamDesconto;
    private String mesAnoApresentar;
    private List<PainelGestorMonitoramentoDescontoNivelEducacionalVO> painelGestorMonitoramentoDescontoNivelEducacionalVOs;
    public static final long serialVersionUID = 1L;

    public Double getPercentualDescontoEfetivado() {
        return Uteis.arrendondarForcando2CadasDecimais(getTotalDescontoMes() * 100 / getValorFaturadoMes());
    }

    public Double getValorFaturadoMes() {
        if (valorFaturadoMes == null) {
            valorFaturadoMes = 0.0;
        }
        return valorFaturadoMes;
    }

    public void setValorFaturadoMes(Double valorFaturadoMes) {
        this.valorFaturadoMes = valorFaturadoMes;
    }

    public Double getValorRecebidoMes() {
        if (valorRecebidoMes == null) {
            valorRecebidoMes = 0.0;
        }
        return valorRecebidoMes;
    }

    public void setValorRecebidoMes(Double valorRecebidoMes) {
        this.valorRecebidoMes = valorRecebidoMes;
    }

    public String getMesAnoApresentar() {
        if ((mesAnoApresentar == null || mesAnoApresentar.isEmpty()) && !getMesAno().isEmpty()) {
            mesAnoApresentar = MesAnoEnum.getEnum(mesAno.substring(0, mesAno.indexOf("/"))).getMesAbreviado() + "/" + getMesAno().substring(getMesAno().length() - 2, getMesAno().length());
        }
        return mesAnoApresentar;
    }

    public Integer getQuantidadeNiveis() {
        return getPainelGestorMonitoramentoDescontoNivelEducacionalVOs().size();
    }

    public List<PainelGestorMonitoramentoDescontoNivelEducacionalVO> getPainelGestorMonitoramentoDescontoNivelEducacionalVOs() {
        if (painelGestorMonitoramentoDescontoNivelEducacionalVOs == null) {
            painelGestorMonitoramentoDescontoNivelEducacionalVOs = new ArrayList<PainelGestorMonitoramentoDescontoNivelEducacionalVO>(0);
        }
        return painelGestorMonitoramentoDescontoNivelEducacionalVOs;
    }

    public void setPainelGestorMonitoramentoDescontoNivelEducacionalVOs(List<PainelGestorMonitoramentoDescontoNivelEducacionalVO> painelGestorMonitoramentoDescontoNivelEducacionalVOs) {
        this.painelGestorMonitoramentoDescontoNivelEducacionalVOs = painelGestorMonitoramentoDescontoNivelEducacionalVOs;
    }

    public String getMesAno() {
        if (mesAno == null) {
            mesAno = "";
        }
        return mesAno;
    }

    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    public Integer getTotalAlunoReceberamDesconto() {
        if (totalAlunoReceberamDesconto == null) {
            totalAlunoReceberamDesconto = 0;
        }
        return totalAlunoReceberamDesconto;
    }

    public void setTotalAlunoReceberamDesconto(Integer totalAlunoReceberamDesconto) {
        this.totalAlunoReceberamDesconto = totalAlunoReceberamDesconto;
    }

    public Double getTotalDescontoMes() {
        if (totalDescontoMes == null) {
            totalDescontoMes = 0.0;
        }
        return totalDescontoMes;
    }

    public void setTotalDescontoMes(Double totalDesconto) {
        this.totalDescontoMes = totalDesconto;
    }

    public Double getTotalDescontoAluno() {
        if (totalDescontoAluno == null) {
            totalDescontoAluno = 0.0;
        }
        return totalDescontoAluno;
    }

    public void setTotalDescontoAluno(Double totalDescontoAluno) {
        this.totalDescontoAluno = totalDescontoAluno;
    }

    public Double getTotalDescontoConvenio() {
        if (totalDescontoConvenio == null) {
            totalDescontoConvenio = 0.0;
        }
        return totalDescontoConvenio;
    }

    public void setTotalDescontoConvenio(Double totalDescontoConvenio) {
        this.totalDescontoConvenio = totalDescontoConvenio;
    }

    public Double getTotalDescontoInstituicao() {
        if (totalDescontoInstituicao == null) {
            totalDescontoInstituicao = 0.0;
        }
        return totalDescontoInstituicao;
    }

    public void setTotalDescontoInstituicao(Double totalDescontoInstituicao) {
        this.totalDescontoInstituicao = totalDescontoInstituicao;
    }

    public Double getTotalDescontoProgressivo() {
        if (totalDescontoProgressivo == null) {
            totalDescontoProgressivo = 0.0;
        }
        return totalDescontoProgressivo;
    }

    public void setTotalDescontoProgressivo(Double totalDescontoProgressivo) {
        this.totalDescontoProgressivo = totalDescontoProgressivo;
    }

    public Double getTotalDescontoRecebimento() {
        if (totalDescontoRecebimento == null) {
            totalDescontoRecebimento = 0.0;
        }
        return totalDescontoRecebimento;
    }

    public void setTotalDescontoRecebimento(Double totalDescontoRecebimento) {
        this.totalDescontoRecebimento = totalDescontoRecebimento;
    }
    

	public Double getTotalDescontoRateio() {
		if(totalDescontoRateio == null){
			totalDescontoRateio = 0.0;
		}
		return totalDescontoRateio;
	}

	public void setTotalDescontoRateio(Double totalDescontoRateio) {
		this.totalDescontoRateio = totalDescontoRateio;
	}
    
    
}
