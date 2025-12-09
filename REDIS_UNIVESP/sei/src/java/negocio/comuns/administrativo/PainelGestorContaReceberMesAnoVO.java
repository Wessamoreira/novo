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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.utilitarias.Uteis;

public class PainelGestorContaReceberMesAnoVO implements Serializable {

    private String mesAno;
    private String curso;
    private String turma;
    private Integer codigoTurma;
    private String nivelEducacional;
    private Integer codigoCurso;
    private Double provisaoReceberMes;
    private Double provisaoReceberMesMatricula;
    private Double provisaoReceberMesMaterialDidatico;
    private Double provisaoReceberMesMensalidade;
    private Double provisaoReceberMesRequerimento;
    private Double provisaoReceberMesBiblioteca;
    private Double provisaoReceberMesDevolucaoCheque;    
    private Double provisaoReceberMesNegociacao;
    private Double provisaoReceberMesInscricao;
    private Double provisaoReceberMesBolsaCusteada;
    private Double provisaoReceberMesContratoReceita;
    private Double provisaoReceberMesInclusaoReposicao;
    private Double provisaoReceberMesOutros;
    
    private Double provisaoReceberMesOutrosGeral;
    
    
    private Double provisaoReceberMesPrimeiroDescontoProgramado;
    private Double provisaoReceberMesPrimeiroDescontoMatriculaMensalidade;
    private Double provisaoReceberMesPrimeiroDescontoOutras;
    private Double recebidoDoMes;
    private Double recebidoDoMesNoMes;
    private Double recebidoNoMes;
    private Double recebimentoAtrazadosMes;
    private Double recebimentoAdiantadoNoMes;
    private Double saldoReceberValorCheio;
    private Double saldoReceberValorCheioMatricula;
    private Double saldoReceberValorCheioMaterialDidatico;
    private Double saldoReceberValorCheioMensalidade;
    private Double saldoReceberValorCheioRequerimento;
    private Double saldoReceberValorCheioBiblioteca;
    private Double saldoReceberValorCheioDevolucaoCheque;    
    private Double saldoReceberValorCheioNegociacao;
    private Double saldoReceberValorCheioInscricao;
    private Double saldoReceberValorCheioBolsaCusteada;
    private Double saldoReceberValorCheioContratoReceita;
    private Double saldoReceberValorCheioInclusaoReposicao;
    private Double saldoReceberValorCheioOutros;
    
    private Double saldoReceberPrimeiroDesconto;
    private Double saldoReceberPrimeiroDescontoMatriculaMensalidade;
    private Double saldoReceberPrimeiroDescontoOutras;
    private Double totalVencidoNoMes;
    private Double totalVencidoNoMesMatricula;
    private Double totalVencidoNoMesMaterialDidatico;
    private Double totalVencidoNoMesMensalidade;
    private Double totalVencidoNoMesRequerimento;
    private Double totalVencidoNoMesBiblioteca;
    private Double totalVencidoNoMesDevolucaoCheque;
    private Double totalVencidoNoMesNegociacao;
    private Double totalVencidoNoMesBolsaCusteada;
    private Double totalVencidoNoMesInscricao;
    private Double totalVencidoNoMesContratoReceita;
    private Double totalVencidoNoMesInclusaoReposicao;
    private Double totalVencidoNoMesOutros;
    
    private BigDecimal totalValor;
    private BigDecimal totalValorCalculadoPrimeiraFaixa;
    private BigDecimal totalDescontoConvenio;
    private BigDecimal totalDescontoInstituicao;
    private BigDecimal totalDescontoProgressivo;
    private BigDecimal totalDescontoAluno;
    private BigDecimal totalDescontoRateio;
    private BigDecimal totalDescontoRecebimento;
    
    private BigDecimal valorInadimplenciaNoMes;
    private BigDecimal taxaInadimplenciaNoMes;
    private BigDecimal taxaInadimplenciaNoMesSemAcrescimo;
    
    
    private List<ContaReceberPainelGestorVO> contaReceberPainelGestorVOs;
    private String mesAnoApresentar;
    
    private BigDecimal receitaDoMes;
    private BigDecimal descontoDoMes;
    private BigDecimal acrescimoDoMes;
    private BigDecimal juroMultaDoMes;
    private BigDecimal receitaComDescontoAcrescimoDoMes;
    private BigDecimal valorRecebidoDoMes;
    private BigDecimal saldoReceberDoMes;
    
    private BigDecimal receitaDoMesMatricula;
    private BigDecimal receitaDoMesMaterialDidatico;
    private BigDecimal receitaDoMesMensalidade;
    private BigDecimal receitaDoMesRequerimento;
    private BigDecimal receitaDoMesBiblioteca;
    private BigDecimal receitaDoMesDevolucaoCheque;
    private BigDecimal receitaDoMesNegociacao;
    private BigDecimal receitaDoMesBolsaCusteada;
    private BigDecimal receitaDoMesInscricao;
    private BigDecimal receitaDoMesContratoReceita;
    private BigDecimal receitaDoMesInclusaoReposicao;
    private BigDecimal receitaDoMesOutros;
    
    private BigDecimal descontoDoMesMatricula;
    private BigDecimal descontoDoMesMaterialDidatico;
    private BigDecimal descontoDoMesMensalidade;
    private BigDecimal descontoDoMesRequerimento;
    private BigDecimal descontoDoMesBiblioteca;
    private BigDecimal descontoDoMesDevolucaoCheque;
    private BigDecimal descontoDoMesNegociacao;
    private BigDecimal descontoDoMesBolsaCusteada;
    private BigDecimal descontoDoMesInscricao;
    private BigDecimal descontoDoMesContratoReceita;
    private BigDecimal descontoDoMesInclusaoReposicao;
    private BigDecimal descontoDoMesOutros;
    
    private BigDecimal acrescimoDoMesMatricula;
    private BigDecimal acrescimoDoMesMaterialDidatico;
    private BigDecimal acrescimoDoMesMensalidade;
    private BigDecimal acrescimoDoMesRequerimento;
    private BigDecimal acrescimoDoMesBiblioteca;
    private BigDecimal acrescimoDoMesDevolucaoCheque;
    private BigDecimal acrescimoDoMesNegociacao;
    private BigDecimal acrescimoDoMesBolsaCusteada;
    private BigDecimal acrescimoDoMesInscricao;
    private BigDecimal acrescimoDoMesContratoReceita;
    private BigDecimal acrescimoDoMesInclusaoReposicao;
    private BigDecimal acrescimoDoMesOutros;
    
    private BigDecimal receitaComDescontoAcrescimoDoMesMatricula;
    private BigDecimal receitaComDescontoAcrescimoDoMesMaterialDidatico;
    private BigDecimal receitaComDescontoAcrescimoDoMesMensalidade;
    private BigDecimal receitaComDescontoAcrescimoDoMesRequerimento;
    private BigDecimal receitaComDescontoAcrescimoDoMesBiblioteca;
    private BigDecimal receitaComDescontoAcrescimoDoMesDevolucaoCheque;
    private BigDecimal receitaComDescontoAcrescimoDoMesNegociacao;
    private BigDecimal receitaComDescontoAcrescimoDoMesBolsaCusteada;
    private BigDecimal receitaComDescontoAcrescimoDoMesInscricao;
    private BigDecimal receitaComDescontoAcrescimoDoMesContratoReceita;
    private BigDecimal receitaComDescontoAcrescimoDoMesInclusaoReposicao;
    private BigDecimal receitaComDescontoAcrescimoDoMesOutros;
    
    private BigDecimal valorRecebidoDoMesMatricula;
    private BigDecimal valorRecebidoDoMesMaterialDidatico;
    private BigDecimal valorRecebidoDoMesMensalidade;
    private BigDecimal valorRecebidoDoMesRequerimento;
    private BigDecimal valorRecebidoDoMesBiblioteca;
    private BigDecimal valorRecebidoDoMesDevolucaoCheque;
    private BigDecimal valorRecebidoDoMesNegociacao;
    private BigDecimal valorRecebidoDoMesBolsaCusteada;
    private BigDecimal valorRecebidoDoMesInscricao;
    private BigDecimal valorRecebidoDoMesContratoReceita;
    private BigDecimal valorRecebidoDoMesInclusaoReposicao;
    private BigDecimal valorRecebidoDoMesOutros;
    
    private BigDecimal saldoReceberDoMesMatricula;
    private BigDecimal saldoReceberDoMesMaterialDidatico;
    private BigDecimal saldoReceberDoMesMensalidade;
    private BigDecimal saldoReceberDoMesRequerimento;
    private BigDecimal saldoReceberDoMesBiblioteca;
    private BigDecimal saldoReceberDoMesDevolucaoCheque;
    private BigDecimal saldoReceberDoMesNegociacao;
    private BigDecimal saldoReceberDoMesBolsaCusteada;
    private BigDecimal saldoReceberDoMesInscricao;
    private BigDecimal saldoReceberDoMesContratoReceita;
    private BigDecimal saldoReceberDoMesInclusaoReposicao;
    private BigDecimal saldoReceberDoMesOutros;
    
    private BigDecimal totalVencidoDoMes;
    private BigDecimal totalVencidoDoMesMatricula;
    private BigDecimal totalVencidoDoMesMaterialDidatico;
    private BigDecimal totalVencidoDoMesMensalidade;
    private BigDecimal totalVencidoDoMesRequerimento;
    private BigDecimal totalVencidoDoMesBiblioteca;
    private BigDecimal totalVencidoDoMesDevolucaoCheque;
    private BigDecimal totalVencidoDoMesNegociacao;
    private BigDecimal totalVencidoDoMesBolsaCusteada;
    private BigDecimal totalVencidoDoMesInscricao;
    private BigDecimal totalVencidoDoMesContratoReceita;
    private BigDecimal totalVencidoDoMesInclusaoReposicao;
    private BigDecimal totalVencidoDoMesOutros;
    
    private BigDecimal totalValorContareceber;
    private BigDecimal totalValorRecebidoContareceber;
    private BigDecimal totalValorReceberContareceber;
    private BigDecimal totalDescontoConvenioContareceber;
    private BigDecimal totalDescontoInstituicaoContareceber;
    private BigDecimal totalDescontoProgressivoContareceber;
    private BigDecimal totalDescontoAlunoContareceber;
    private BigDecimal totalDescontoRecebimentoContareceber;
    private BigDecimal totalDescontoRateioContareceber;
    private BigDecimal totalAcrescimoContaReceber;
    
    private BigDecimal receitaNoMes;
    private BigDecimal descontoNoMes;
    private BigDecimal acrescimoNoMes;
    
    private BigDecimal receitaNoMesMatricula;
    private BigDecimal receitaNoMesMaterialDidatico;
    private BigDecimal receitaNoMesMensalidade;
    private BigDecimal receitaNoMesRequerimento;
    private BigDecimal receitaNoMesBiblioteca;
    private BigDecimal receitaNoMesDevolucaoCheque;
    private BigDecimal receitaNoMesNegociacao;
    private BigDecimal receitaNoMesBolsaCusteada;
    private BigDecimal receitaNoMesInscricao;
    private BigDecimal receitaNoMesContratoReceita;
    private BigDecimal receitaNoMesInclusaoReposicao;
    private BigDecimal receitaNoMesOutros;
    
    private BigDecimal descontoNoMesMatricula;
    private BigDecimal descontoNoMesMaterialDidatico;
    private BigDecimal descontoNoMesMensalidade;
    private BigDecimal descontoNoMesRequerimento;
    private BigDecimal descontoNoMesBiblioteca;
    private BigDecimal descontoNoMesDevolucaoCheque;
    private BigDecimal descontoNoMesNegociacao;
    private BigDecimal descontoNoMesBolsaCusteada;
    private BigDecimal descontoNoMesInscricao;
    private BigDecimal descontoNoMesContratoReceita;
    private BigDecimal descontoNoMesInclusaoReposicao;
    private BigDecimal descontoNoMesOutros;
    
    private BigDecimal acrescimoNoMesMatricula;
    private BigDecimal acrescimoNoMesMaterialDidatico;
    private BigDecimal acrescimoNoMesMensalidade;
    private BigDecimal acrescimoNoMesRequerimento;
    private BigDecimal acrescimoNoMesBiblioteca;
    private BigDecimal acrescimoNoMesDevolucaoCheque;
    private BigDecimal acrescimoNoMesNegociacao;
    private BigDecimal acrescimoNoMesBolsaCusteada;
    private BigDecimal acrescimoNoMesInscricao;
    private BigDecimal acrescimoNoMesContratoReceita;
    private BigDecimal acrescimoNoMesInclusaoReposicao;
    private BigDecimal acrescimoNoMesOutros;

    private BigDecimal descontoConvenio;
    private BigDecimal descontoInstituicao;
    private BigDecimal valorDescontoProgressivo;
    private BigDecimal descontoRateio;
    private BigDecimal descontoAluno;
    private BigDecimal descontoRecebimento;
    
    
    public static final long serialVersionUID = 1L;

    public Integer getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = 0;
        }
        return codigoCurso;
    }

    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public Integer getCodigoTurma() {
        if (codigoTurma == null) {
            codigoTurma = 0;
        }
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getMesAnoApresentar() {
        if ((mesAnoApresentar == null || mesAnoApresentar.isEmpty()) && !getMesAno().isEmpty()) {
            mesAnoApresentar = MesAnoEnum.getEnum(mesAno.substring(0, mesAno.indexOf("/"))).getMesAbreviado() + "/" + getMesAno().substring(getMesAno().length() - 2, getMesAno().length());
        }
        return mesAnoApresentar;
    }

    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public Double getRecebidoDoMesNoMes() {
        if (recebidoDoMesNoMes == null) {
            recebidoDoMesNoMes = 0.0;
        }
        return recebidoDoMesNoMes;
    }

    public void setRecebidoDoMesNoMes(Double recebidoDoMesNoMes) {
        this.recebidoDoMesNoMes = recebidoDoMesNoMes;
    }

    public List<ContaReceberPainelGestorVO> getContaReceberPainelGestorVOs() {
        if (contaReceberPainelGestorVOs == null) {
            contaReceberPainelGestorVOs = new ArrayList<ContaReceberPainelGestorVO>(0);
        }
        return contaReceberPainelGestorVOs;
    }

    public void setContaReceberPainelGestorVOs(List<ContaReceberPainelGestorVO> contaReceberPainelGestorVOs) {
        this.contaReceberPainelGestorVOs = contaReceberPainelGestorVOs;
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

    public Double getProvisaoReceberMes() {
        if (provisaoReceberMes == null) {
            provisaoReceberMes = 0.0;
        }
        return provisaoReceberMes;
    }

    public void setProvisaoReceberMes(Double provisaoReceberMes) {
        this.provisaoReceberMes = provisaoReceberMes;
    }

    public Double getProvisaoReceberMesPrimeiroDescontoProgramado() {
        if (provisaoReceberMesPrimeiroDescontoProgramado == null) {
            provisaoReceberMesPrimeiroDescontoProgramado = 0.0;
        }
        return provisaoReceberMesPrimeiroDescontoProgramado;
    }

    public void setProvisaoReceberMesPrimeiroDescontoProgramado(Double provisaoReceberMesPrimeiroDescontoProgramado) {
        this.provisaoReceberMesPrimeiroDescontoProgramado = provisaoReceberMesPrimeiroDescontoProgramado;
    }

    public Double getRecebidoDoMes() {
        if (recebidoDoMes == null) {
            recebidoDoMes = 0.0;
        }
        return recebidoDoMes;
    }

    public void setRecebidoDoMes(Double recebidoDoMes) {
        this.recebidoDoMes = recebidoDoMes;
    }

    public Double getRecebidoNoMes() {
        if (recebidoNoMes == null) {
            recebidoNoMes = 0.0;
        }
        return recebidoNoMes;
    }

    public void setRecebidoNoMes(Double recebidoNoMes) {
        this.recebidoNoMes = recebidoNoMes;
    }

    public Double getRecebimentoAtrazadosMes() {
        if (recebimentoAtrazadosMes == null) {
            recebimentoAtrazadosMes = 0.0;
        }
        return recebimentoAtrazadosMes;
    }

    public void setRecebimentoAtrazadosMes(Double recebimentoAtrazadosMes) {
        this.recebimentoAtrazadosMes = recebimentoAtrazadosMes;
    }

    /**
     * Método responsável em retornar a taxa de inadimplecia no mês em questão isso dá uma visão de qual foi ou é a taxa de inadimplência do mês
     *
     * @return
     */
//    public Double getTaxaInadimplenciaNoMes() {
//        try {
//            /**
//             * Este Valida a taxa de inadimplência do mês que já passou
//             */
//            Double inadimplencia = 0.0;
//            if (getReceitaNoMes().compareTo(BigDecimal.ZERO) > 0
//                    && (getCodigoCurso() == 0 && Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) < Uteis.getMesData(new Date())
//                    && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) <= Uteis.getAnoData(new Date()))
//                    || (getReceitaNoMes().compareTo(BigDecimal.ZERO) > 0 && getCodigoCurso() == 0 && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) < Uteis.getAnoData(new Date()))
//                    || (getReceitaNoMes().compareTo(BigDecimal.ZERO) > 0 && getCodigoCurso() > 0)) {
//                inadimplencia = Uteis.arrendondarForcando2CadasDecimais(100 - (getReceitaNoMes().doubleValue() * 100 / getProvisaoReceberMes()));
//            } /**
//             * Este Valida a taxa de inadimplência do mês atual, portanto ele valida com base no total já vencido do mês
//             */
//            else if (getTotalVencidoNoMes() > 0
//                    && (getCodigoCurso() == 0 && Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) == Uteis.getMesData(new Date())
//                    && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) == Uteis.getAnoData(new Date()))
//                    || (getTotalVencidoNoMes() > 0 && getCodigoCurso() > 0)) {
//                inadimplencia = Uteis.arrendondarForcando2CadasDecimais((getTotalVencidoNoMes() * 100 / getProvisaoReceberMes()));
//            }
//            if (inadimplencia < 0.0) {
//                inadimplencia = 0.0;
//            }
//            return inadimplencia;
//        } catch (Exception e) {
//            return 0.0;
//        }
//    }

    /**
     * Método responsável em retornar a taxa de inadimplecia no mês em questão nos dias atuais
     *
     * @return
     */
    public Double getTaxaInadimplenciaDoMes() {
        try {
            Double inadimplencia = 0.0;
            if ((getTotalVencidoDoMes().compareTo(BigDecimal.ZERO) > 0 
                    && (getCodigoCurso() == 0  && getCodigoTurma() == 0 && Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) <= Uteis.getMesData(new Date())
                    && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) <= Uteis.getAnoData(new Date())))
                    || (getTotalVencidoDoMes().compareTo(BigDecimal.ZERO) > 0 && getCodigoCurso() == 0 && getCodigoTurma() == 0 && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) < Uteis.getAnoData(new Date()))
                    || (getTotalVencidoDoMes().compareTo(BigDecimal.ZERO) > 0 && getCodigoCurso() > 0)
                    || (getTotalVencidoDoMes().compareTo(BigDecimal.ZERO) > 0 && getCodigoTurma() > 0)) {
            	
                inadimplencia = Uteis.arrendondarForcando2CadasDecimais(getTotalVencidoDoMes().doubleValue() * 100) / (getReceitaComDescontoAcrescimoDoMes()).doubleValue();
            }
            if (inadimplencia < 0.0) {
                inadimplencia = 0.0;
            }
            return inadimplencia;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getTaxaInadimplenciaDoMesSemAcrescimo() {
    	try {
    		Double inadimplencia = 0.0;
    		if ((getTotalVencidoDoMesSemAcrescimo().compareTo(BigDecimal.ZERO) > 0 
    				&& (getCodigoCurso() == 0  && getCodigoTurma() == 0 && Integer.valueOf(getMesAno().substring(0, getMesAno().indexOf("/"))) <= Uteis.getMesData(new Date())
    				&& Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) <= Uteis.getAnoData(new Date())))
    				|| (getTotalVencidoDoMesSemAcrescimo().compareTo(BigDecimal.ZERO) > 0 && getCodigoCurso() == 0 && getCodigoTurma() == 0 && Integer.valueOf(getMesAno().substring(getMesAno().indexOf("/") + 1, getMesAno().length())) < Uteis.getAnoData(new Date()))
    				|| (getTotalVencidoDoMesSemAcrescimo().compareTo(BigDecimal.ZERO) > 0 && getCodigoCurso() > 0)
    				|| (getTotalVencidoDoMesSemAcrescimo().compareTo(BigDecimal.ZERO) > 0 && getCodigoTurma() > 0)) {
    			
    			inadimplencia = Uteis.arrendondarForcando2CadasDecimais(getTotalVencidoDoMesSemAcrescimo().doubleValue() * 100) / (getReceitaComDescontoAcrescimoDoMes().subtract(getAcrescimoDoMes())).doubleValue();
    		}
    		if (inadimplencia < 0.0) {
    			inadimplencia = 0.0;
    		}
    		return inadimplencia;
    	} catch (Exception e) {
    		return 0.0;
    	}
    }
    
    public Double getSaldoReceberPrimeiroDesconto() {
        if (saldoReceberPrimeiroDesconto == null) {
            saldoReceberPrimeiroDesconto = 0.0;
        }
        return saldoReceberPrimeiroDesconto;
    }

    public void setSaldoReceberPrimeiroDesconto(Double saldoReceberPrimeiroDesconto) {
        this.saldoReceberPrimeiroDesconto = saldoReceberPrimeiroDesconto;
    }

    public Double getSaldoReceberValorCheio() {
        if (saldoReceberValorCheio == null) {
            saldoReceberValorCheio = 0.0;
        }
        return saldoReceberValorCheio;
    }

    public void setSaldoReceberValorCheio(Double saldoReceberValorCheio) {
        this.saldoReceberValorCheio = saldoReceberValorCheio;
    }

    /**
     * Este armazena o total vencido do mês onde se estivermos no mês atual só será incrementado aki os valores já vencidos
     * @return
     */
    public Double getTotalVencidoNoMes() {
        if (totalVencidoNoMes == null) {
            totalVencidoNoMes = 0.0;
        }
        return totalVencidoNoMes;
    }

    public void setTotalVencidoNoMes(Double totalVencidoNoMes) {
        this.totalVencidoNoMes = totalVencidoNoMes;
    }

    public Double getRecebimentoAdiantadoNoMes() {
        if (recebimentoAdiantadoNoMes == null) {
            recebimentoAdiantadoNoMes = 0.0;
        }
        return recebimentoAdiantadoNoMes;
    }

    public void setRecebimentoAdiantadoNoMes(Double recebimentoAdiantadoNoMes) {
        this.recebimentoAdiantadoNoMes = recebimentoAdiantadoNoMes;
    }

    public String getNivelEducacional() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        return nivelEducacional;
    }

    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }
//    public Double getSaldoReceber() {
//        if (getProvisaoReceberMesPrimeiroDescontoProgramado() > getRecebidoDoMes()) {
//            return getProvisaoReceberMesPrimeiroDescontoProgramado() - getRecebidoDoMes();
//        }
//        return 0.0;
//    }
//    public Double getTotalRecebido() {
//        return getRecebidoDoMes() + getRecebimentoAtrazadosMes();
//    }

	public Double getProvisaoReceberMesPrimeiroDescontoMatriculaMensalidade() {
		if(provisaoReceberMesPrimeiroDescontoMatriculaMensalidade == null){
			provisaoReceberMesPrimeiroDescontoMatriculaMensalidade = 0.0;
		}
		return provisaoReceberMesPrimeiroDescontoMatriculaMensalidade;
	}

	public void setProvisaoReceberMesPrimeiroDescontoMatriculaMensalidade(Double provisaoReceberMesPrimeiroDescontoMatriculaMensalidade) {
		this.provisaoReceberMesPrimeiroDescontoMatriculaMensalidade = provisaoReceberMesPrimeiroDescontoMatriculaMensalidade;
	}

	public Double getProvisaoReceberMesPrimeiroDescontoOutras() {
		if(provisaoReceberMesPrimeiroDescontoOutras == null){
			provisaoReceberMesPrimeiroDescontoOutras = 0.0;
		}
		return provisaoReceberMesPrimeiroDescontoOutras;
	}

	public void setProvisaoReceberMesPrimeiroDescontoOutras(Double provisaoReceberMesPrimeiroDescontoOutras) {
		this.provisaoReceberMesPrimeiroDescontoOutras = provisaoReceberMesPrimeiroDescontoOutras;
	}

	public Double getSaldoReceberPrimeiroDescontoMatriculaMensalidade() {
		if(saldoReceberPrimeiroDescontoMatriculaMensalidade == null){
			saldoReceberPrimeiroDescontoMatriculaMensalidade = 0.0;
		}
		return saldoReceberPrimeiroDescontoMatriculaMensalidade;
	}

	public void setSaldoReceberPrimeiroDescontoMatriculaMensalidade(Double saldoReceberPrimeiroDescontoMatriculaMensalidade) {
		this.saldoReceberPrimeiroDescontoMatriculaMensalidade = saldoReceberPrimeiroDescontoMatriculaMensalidade;
	}

	public Double getSaldoReceberPrimeiroDescontoOutras() {
		if(saldoReceberPrimeiroDescontoOutras == null){
			saldoReceberPrimeiroDescontoOutras = 0.0;
		}
		return saldoReceberPrimeiroDescontoOutras;
	}

	public void setSaldoReceberPrimeiroDescontoOutras(Double saldoReceberPrimeiroDescontoOutras) {
		this.saldoReceberPrimeiroDescontoOutras = saldoReceberPrimeiroDescontoOutras;
	}

	public Double getProvisaoReceberMesMatricula() {
		if(provisaoReceberMesMatricula == null){
			provisaoReceberMesMatricula = 0.0;
		}
		return provisaoReceberMesMatricula;
	}

	public void setProvisaoReceberMesMatricula(Double provisaoReceberMesMatricula) {
		this.provisaoReceberMesMatricula = provisaoReceberMesMatricula;
	}

	public Double getProvisaoReceberMesMensalidade() {
		if(provisaoReceberMesMensalidade == null){
			provisaoReceberMesMensalidade = 0.0;
		}
		return provisaoReceberMesMensalidade;
	}

	public void setProvisaoReceberMesMensalidade(Double provisaoReceberMesMensalidade) {
		this.provisaoReceberMesMensalidade = provisaoReceberMesMensalidade;
	}

	public Double getProvisaoReceberMesRequerimento() {
		if(provisaoReceberMesRequerimento == null){
			provisaoReceberMesRequerimento = 0.0;
		}
		return provisaoReceberMesRequerimento;
	}

	public void setProvisaoReceberMesRequerimento(Double provisaoReceberMesRequerimento) {
		this.provisaoReceberMesRequerimento = provisaoReceberMesRequerimento;
	}

	public Double getProvisaoReceberMesBiblioteca() {
		if(provisaoReceberMesBiblioteca == null){
			provisaoReceberMesBiblioteca = 0.0;
		}
		return provisaoReceberMesBiblioteca;
	}

	public void setProvisaoReceberMesBiblioteca(Double provisaoReceberMesBiblioteca) {
		this.provisaoReceberMesBiblioteca = provisaoReceberMesBiblioteca;
	}

	public Double getProvisaoReceberMesDevolucaoCheque() {
		if(provisaoReceberMesDevolucaoCheque == null){
			provisaoReceberMesDevolucaoCheque = 0.0;
		}
		return provisaoReceberMesDevolucaoCheque;
	}

	public void setProvisaoReceberMesDevolucaoCheque(Double provisaoReceberMesDevolucaoCheque) {
		this.provisaoReceberMesDevolucaoCheque = provisaoReceberMesDevolucaoCheque;
	}

	public Double getProvisaoReceberMesNegociacao() {
		if(provisaoReceberMesNegociacao == null){
			provisaoReceberMesNegociacao = 0.0;
		}
		return provisaoReceberMesNegociacao;
	}

	public void setProvisaoReceberMesNegociacao(Double provisaoReceberMesNegociacao) {
		this.provisaoReceberMesNegociacao = provisaoReceberMesNegociacao;
	}

	public Double getProvisaoReceberMesInscricao() {
		if(provisaoReceberMesInscricao == null){
			provisaoReceberMesInscricao = 0.0;
		}
		return provisaoReceberMesInscricao;
	}

	public void setProvisaoReceberMesInscricao(Double provisaoReceberMesInscricao) {
		this.provisaoReceberMesInscricao = provisaoReceberMesInscricao;
	}

	public Double getProvisaoReceberMesBolsaCusteada() {
		if(provisaoReceberMesBolsaCusteada == null){
			provisaoReceberMesBolsaCusteada = 0.0;
		}
		return provisaoReceberMesBolsaCusteada;
	}

	public void setProvisaoReceberMesBolsaCusteada(Double provisaoReceberMesBolsaCusteada) {
		this.provisaoReceberMesBolsaCusteada = provisaoReceberMesBolsaCusteada;
	}

	public Double getProvisaoReceberMesContratoReceita() {
		if(provisaoReceberMesContratoReceita == null){
			provisaoReceberMesContratoReceita = 0.0;
		}
		return provisaoReceberMesContratoReceita;
	}

	public void setProvisaoReceberMesContratoReceita(Double provisaoReceberMesContratoReceita) {
		this.provisaoReceberMesContratoReceita = provisaoReceberMesContratoReceita;
	}

	public Double getProvisaoReceberMesInclusaoReposicao() {
		if(provisaoReceberMesInclusaoReposicao == null){
			provisaoReceberMesInclusaoReposicao = 0.0;
		}
		return provisaoReceberMesInclusaoReposicao;
	}

	public void setProvisaoReceberMesInclusaoReposicao(Double provisaoReceberMesInclusaoReposicao) {
		this.provisaoReceberMesInclusaoReposicao = provisaoReceberMesInclusaoReposicao;
	}

	public Double getProvisaoReceberMesOutros() {
		if(provisaoReceberMesOutros == null){
			provisaoReceberMesOutros = 0.0;
		}
		return provisaoReceberMesOutros;
	}

	public void setProvisaoReceberMesOutros(Double provisaoReceberMesOutros) {
		this.provisaoReceberMesOutros = provisaoReceberMesOutros;
	}

	public Double getTotalVencidoNoMesMatricula() {
		if(totalVencidoNoMesMatricula == null){
			totalVencidoNoMesMatricula = 0.0;
		}
		return totalVencidoNoMesMatricula;
	}

	public void setTotalVencidoNoMesMatricula(Double totalVencidoNoMesMatricula) {
		this.totalVencidoNoMesMatricula = totalVencidoNoMesMatricula;
	}

	public Double getTotalVencidoNoMesMensalidade() {
		if(totalVencidoNoMesMensalidade == null){
			totalVencidoNoMesMensalidade = 0.0;
		}
		return totalVencidoNoMesMensalidade;
	}

	public void setTotalVencidoNoMesMensalidade(Double totalVencidoNoMesMensalidade) {
		this.totalVencidoNoMesMensalidade = totalVencidoNoMesMensalidade;
	}

	public Double getTotalVencidoNoMesRequerimento() {
		if(totalVencidoNoMesRequerimento == null){
			totalVencidoNoMesRequerimento = 0.0;
		}
		return totalVencidoNoMesRequerimento;
	}

	public void setTotalVencidoNoMesRequerimento(Double totalVencidoNoMesRequerimento) {
		this.totalVencidoNoMesRequerimento = totalVencidoNoMesRequerimento;
	}

	public Double getTotalVencidoNoMesBiblioteca() {
		if(totalVencidoNoMesBiblioteca == null){
			totalVencidoNoMesBiblioteca = 0.0;
		}
		return totalVencidoNoMesBiblioteca;
	}

	public void setTotalVencidoNoMesBiblioteca(Double totalVencidoNoMesBiblioteca) {
		this.totalVencidoNoMesBiblioteca = totalVencidoNoMesBiblioteca;
	}

	public Double getTotalVencidoNoMesDevolucaoCheque() {
			if(totalVencidoNoMesDevolucaoCheque == null){
				totalVencidoNoMesDevolucaoCheque = 0.0;
			}
		return totalVencidoNoMesDevolucaoCheque;
	}

	public void setTotalVencidoNoMesDevolucaoCheque(Double totalVencidoNoMesDevolucaoCheque) {
		this.totalVencidoNoMesDevolucaoCheque = totalVencidoNoMesDevolucaoCheque;
	}

	public Double getTotalVencidoNoMesNegociacao() {
		if(totalVencidoNoMesNegociacao == null){
			totalVencidoNoMesNegociacao = 0.0;
		}
		return totalVencidoNoMesNegociacao;
	}

	public void setTotalVencidoNoMesNegociacao(Double totalVencidoNoMesNegociacao) {
		this.totalVencidoNoMesNegociacao = totalVencidoNoMesNegociacao;
	}

	public Double getTotalVencidoNoMesBolsaCusteada() {
		if(totalVencidoNoMesBolsaCusteada == null){
			totalVencidoNoMesBolsaCusteada = 0.0;
		}
		return totalVencidoNoMesBolsaCusteada;
	}

	public void setTotalVencidoNoMesBolsaCusteada(Double totalVencidoNoMesBolsaCusteada) {
		this.totalVencidoNoMesBolsaCusteada = totalVencidoNoMesBolsaCusteada;
	}

	public Double getTotalVencidoNoMesInscricao() {
		if(totalVencidoNoMesInscricao == null){
			totalVencidoNoMesInscricao = 0.0;
		}
		return totalVencidoNoMesInscricao;
	}

	public void setTotalVencidoNoMesInscricao(Double totalVencidoNoMesInscricao) {
		this.totalVencidoNoMesInscricao = totalVencidoNoMesInscricao;
	}

	public Double getTotalVencidoNoMesContratoReceita() {
		if(totalVencidoNoMesContratoReceita == null){
			totalVencidoNoMesContratoReceita = 0.0;
		}
		return totalVencidoNoMesContratoReceita;
	}

	public void setTotalVencidoNoMesContratoReceita(Double totalVencidoNoMesContratoReceita) {
		this.totalVencidoNoMesContratoReceita = totalVencidoNoMesContratoReceita;
	}

	public Double getTotalVencidoNoMesInclusaoReposicao() {
		if(totalVencidoNoMesInclusaoReposicao == null){
			totalVencidoNoMesInclusaoReposicao = 0.0;
		}
		return totalVencidoNoMesInclusaoReposicao;
	}

	public void setTotalVencidoNoMesInclusaoReposicao(Double totalVencidoNoMesInclusaoReposicao) {
		this.totalVencidoNoMesInclusaoReposicao = totalVencidoNoMesInclusaoReposicao;
	}

	public Double getSaldoReceberValorCheioMatricula() {
		if(saldoReceberValorCheioMatricula == null){
			saldoReceberValorCheioMatricula = 0.0;
		}
		return saldoReceberValorCheioMatricula;
	}

	public void setSaldoReceberValorCheioMatricula(Double saldoReceberValorCheioMatricula) {
		this.saldoReceberValorCheioMatricula = saldoReceberValorCheioMatricula;
	}

	public Double getSaldoReceberValorCheioMensalidade() {
		if(saldoReceberValorCheioMensalidade == null){
			saldoReceberValorCheioMensalidade = 0.0;
		}
		return saldoReceberValorCheioMensalidade;
	}

	public void setSaldoReceberValorCheioMensalidade(Double saldoReceberValorCheioMensalidade) {
		this.saldoReceberValorCheioMensalidade = saldoReceberValorCheioMensalidade;
	}

	public Double getSaldoReceberValorCheioRequerimento() {
		if(saldoReceberValorCheioRequerimento == null){
			saldoReceberValorCheioRequerimento = 0.0;
		}
		return saldoReceberValorCheioRequerimento;
	}

	public void setSaldoReceberValorCheioRequerimento(Double saldoReceberValorCheioRequerimento) {
		this.saldoReceberValorCheioRequerimento = saldoReceberValorCheioRequerimento;
	}

	public Double getSaldoReceberValorCheioBiblioteca() {
		if(saldoReceberValorCheioBiblioteca == null){
			saldoReceberValorCheioBiblioteca = 0.0;
		}
		return saldoReceberValorCheioBiblioteca;
	}

	public void setSaldoReceberValorCheioBiblioteca(Double saldoReceberValorCheioBiblioteca) {
		this.saldoReceberValorCheioBiblioteca = saldoReceberValorCheioBiblioteca;
	}

	public Double getSaldoReceberValorCheioDevolucaoCheque() {
		if(saldoReceberValorCheioDevolucaoCheque == null){
			saldoReceberValorCheioDevolucaoCheque = 0.0;
		}
		return saldoReceberValorCheioDevolucaoCheque;
	}

	public void setSaldoReceberValorCheioDevolucaoCheque(Double saldoReceberValorCheioDevolucaoCheque) {
		this.saldoReceberValorCheioDevolucaoCheque = saldoReceberValorCheioDevolucaoCheque;
	}

	public Double getSaldoReceberValorCheioNegociacao() {
		if(saldoReceberValorCheioNegociacao == null){
			saldoReceberValorCheioNegociacao = 0.0;
		}
		return saldoReceberValorCheioNegociacao;
	}

	public void setSaldoReceberValorCheioNegociacao(Double saldoReceberValorCheioNegociacao) {
		this.saldoReceberValorCheioNegociacao = saldoReceberValorCheioNegociacao;
	}

	public Double getSaldoReceberValorCheioInscricao() {
		if(saldoReceberValorCheioInscricao == null){
			saldoReceberValorCheioInscricao = 0.0;
		}
		return saldoReceberValorCheioInscricao;
	}

	public void setSaldoReceberValorCheioInscricao(Double saldoReceberValorCheioInscricao) {
		this.saldoReceberValorCheioInscricao = saldoReceberValorCheioInscricao;
	}

	public Double getSaldoReceberValorCheioBolsaCusteada() {
		if(saldoReceberValorCheioBolsaCusteada == null){
			saldoReceberValorCheioBolsaCusteada = 0.0;
		}
		return saldoReceberValorCheioBolsaCusteada;
	}

	public void setSaldoReceberValorCheioBolsaCusteada(Double saldoReceberValorCheioBolsaCusteada) {
		this.saldoReceberValorCheioBolsaCusteada = saldoReceberValorCheioBolsaCusteada;
	}

	public Double getSaldoReceberValorCheioContratoReceita() {
		if(saldoReceberValorCheioContratoReceita == null){
			saldoReceberValorCheioContratoReceita = 0.0;
		}
		return saldoReceberValorCheioContratoReceita;
	}

	public void setSaldoReceberValorCheioContratoReceita(Double saldoReceberValorCheioContratoReceita) {
		this.saldoReceberValorCheioContratoReceita = saldoReceberValorCheioContratoReceita;
	}

	public Double getSaldoReceberValorCheioInclusaoReposicao() {
		if(saldoReceberValorCheioInclusaoReposicao == null){
			saldoReceberValorCheioInclusaoReposicao = 0.0;
		}
		return saldoReceberValorCheioInclusaoReposicao;
	}

	public void setSaldoReceberValorCheioInclusaoReposicao(Double saldoReceberValorCheioInclusaoReposicao) {
		this.saldoReceberValorCheioInclusaoReposicao = saldoReceberValorCheioInclusaoReposicao;
	}

	public Double getSaldoReceberValorCheioOutros() {
		if(saldoReceberValorCheioOutros == null){
			saldoReceberValorCheioOutros = 0.0;
		}
		return saldoReceberValorCheioOutros;
	}

	public void setSaldoReceberValorCheioOutros(Double saldoReceberValorCheioOutros) {
		this.saldoReceberValorCheioOutros = saldoReceberValorCheioOutros;
	}

	public Double getTotalVencidoNoMesOutros() {
		if(totalVencidoNoMesOutros == null){
			totalVencidoNoMesOutros = 0.0;
		}
		return totalVencidoNoMesOutros;
	}

	public void setTotalVencidoNoMesOutros(Double totalVencidoNoMesOutros) {
		this.totalVencidoNoMesOutros = totalVencidoNoMesOutros;
	}

	public Double getProvisaoReceberMesOutrosGeral() {
		if (provisaoReceberMesOutrosGeral == null) {
			provisaoReceberMesOutrosGeral = 0.0;
		}
		return provisaoReceberMesOutrosGeral;
	}

	public void setProvisaoReceberMesOutrosGeral(Double provisaoReceberMesOutrosGeral) {
		this.provisaoReceberMesOutrosGeral = provisaoReceberMesOutrosGeral;
	}


	public BigDecimal getTotalValor() {
		if (totalValor == null) {
			totalValor = BigDecimal.ZERO;
		}
		return totalValor;
	}

	public void setTotalValor(BigDecimal totalValor) {
		this.totalValor = totalValor;
	}

	public BigDecimal getTotalValorCalculadoPrimeiraFaixa() {
		if (totalValorCalculadoPrimeiraFaixa == null) {
			totalValorCalculadoPrimeiraFaixa = BigDecimal.ZERO;
		}
		return totalValorCalculadoPrimeiraFaixa;
	}

	public void setTotalValorCalculadoPrimeiraFaixa(BigDecimal totalValorCalculadoPrimeiraFaixa) {
		this.totalValorCalculadoPrimeiraFaixa = totalValorCalculadoPrimeiraFaixa;
	}

	public BigDecimal getTotalDescontoConvenio() {
		if (totalDescontoConvenio == null) {
			totalDescontoConvenio = BigDecimal.ZERO;
		}
		return totalDescontoConvenio;
	}

	public void setTotalDescontoConvenio(BigDecimal totalDescontoConvenio) {
		this.totalDescontoConvenio = totalDescontoConvenio;
	}

	public BigDecimal getTotalDescontoInstituicao() {
		if (totalDescontoInstituicao == null) {
			totalDescontoInstituicao = BigDecimal.ZERO;
		}
		return totalDescontoInstituicao;
	}

	public void setTotalDescontoInstituicao(BigDecimal totalDescontoInstituicao) {
		this.totalDescontoInstituicao = totalDescontoInstituicao;
	}

	public BigDecimal getTotalDescontoProgressivo() {
		if (totalDescontoProgressivo == null) {
			totalDescontoProgressivo = BigDecimal.ZERO;
		}
		return totalDescontoProgressivo;
	}

	public void setTotalDescontoProgressivo(BigDecimal totalDescontoProgressivo) {
		this.totalDescontoProgressivo = totalDescontoProgressivo;
	}

	public BigDecimal getTotalDescontoAluno() {
		if (totalDescontoAluno == null) {
			totalDescontoAluno = BigDecimal.ZERO;
		}
		return totalDescontoAluno;
	}

	public void setTotalDescontoAluno(BigDecimal totalDescontoAluno) {
		this.totalDescontoAluno = totalDescontoAluno;
	}

	public BigDecimal getTotalDescontoRecebimento() {
		if (totalDescontoRecebimento == null) {
			totalDescontoRecebimento = BigDecimal.ZERO;
		}
		return totalDescontoRecebimento;
	}

	public void setTotalDescontoRecebimento(BigDecimal totalDescontoRecebimento) {
		this.totalDescontoRecebimento = totalDescontoRecebimento;
	}

	public BigDecimal getReceitaDoMes() {
		if (receitaDoMes == null) {
			receitaDoMes = BigDecimal.ZERO;
		}
		return receitaDoMes;
	}

	public void setReceitaDoMes(BigDecimal receitaDoMes) {
		this.receitaDoMes = receitaDoMes;
	}

	public BigDecimal getDescontoDoMes() {
		if (descontoDoMes == null) {
			descontoDoMes = BigDecimal.ZERO;
		}
		return descontoDoMes;
	}

	public void setDescontoDoMes(BigDecimal descontoDoMes) {
		this.descontoDoMes = descontoDoMes;
	}

	public BigDecimal getAcrescimoDoMes() {
		if (acrescimoDoMes == null) {
			acrescimoDoMes = BigDecimal.ZERO;
		}
		return acrescimoDoMes;
	}

	public void setAcrescimoDoMes(BigDecimal acrescimoDoMes) {
		this.acrescimoDoMes = acrescimoDoMes;
	}

	public BigDecimal getJuroMultaDoMes() {
		if (juroMultaDoMes == null) {
			juroMultaDoMes = BigDecimal.ZERO;
		}
		return juroMultaDoMes;
	}
	
	public void setJuroMultaDoMes(BigDecimal juroMultaDoMes) {
		this.juroMultaDoMes = juroMultaDoMes;
	}
	
	public BigDecimal getReceitaComDescontoAcrescimoDoMes() {
		if (receitaComDescontoAcrescimoDoMes == null) {
			receitaComDescontoAcrescimoDoMes = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMes;
	}

	public void setReceitaComDescontoAcrescimoDoMes(BigDecimal receitaComDescontoAcrescimoDoMes) {
		this.receitaComDescontoAcrescimoDoMes = receitaComDescontoAcrescimoDoMes;
	}

	public BigDecimal getValorRecebidoDoMes() {
		if (valorRecebidoDoMes == null) {
			valorRecebidoDoMes = BigDecimal.ZERO;
		}
		return valorRecebidoDoMes;
	}

	public void setValorRecebidoDoMes(BigDecimal valorRecebidoDoMes) {
		this.valorRecebidoDoMes = valorRecebidoDoMes;
	}

	public BigDecimal getSaldoReceberDoMes() {
		if (saldoReceberDoMes == null) {
			saldoReceberDoMes = BigDecimal.ZERO;
		}
		return saldoReceberDoMes;
	}

	public void setSaldoReceberDoMes(BigDecimal saldoReceberDoMes) {
		this.saldoReceberDoMes = saldoReceberDoMes;
	}

	public BigDecimal getReceitaDoMesMatricula() {
		if (receitaDoMesMatricula == null) {
			receitaDoMesMatricula = BigDecimal.ZERO;
		}
		return receitaDoMesMatricula;
	}

	public void setReceitaDoMesMatricula(BigDecimal receitaDoMesMatricula) {
		this.receitaDoMesMatricula = receitaDoMesMatricula;
	}

	public BigDecimal getReceitaDoMesMensalidade() {
		if (receitaDoMesMensalidade == null) {
			receitaDoMesMensalidade = BigDecimal.ZERO;
		}
		return receitaDoMesMensalidade;
	}

	public void setReceitaDoMesMensalidade(BigDecimal receitaDoMesMensalidade) {
		this.receitaDoMesMensalidade = receitaDoMesMensalidade;
	}

	public BigDecimal getReceitaDoMesRequerimento() {
		if (receitaDoMesRequerimento == null) {
			receitaDoMesRequerimento = BigDecimal.ZERO;
		}
		return receitaDoMesRequerimento;
	}

	public void setReceitaDoMesRequerimento(BigDecimal receitaDoMesRequerimento) {
		this.receitaDoMesRequerimento = receitaDoMesRequerimento;
	}

	public BigDecimal getReceitaDoMesBiblioteca() {
		if (receitaDoMesBiblioteca == null) {
			receitaDoMesBiblioteca = BigDecimal.ZERO;
		}
		return receitaDoMesBiblioteca;
	}

	public void setReceitaDoMesBiblioteca(BigDecimal receitaDoMesBiblioteca) {
		this.receitaDoMesBiblioteca = receitaDoMesBiblioteca;
	}

	public BigDecimal getReceitaDoMesDevolucaoCheque() {
		if (receitaDoMesDevolucaoCheque == null) {
			receitaDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return receitaDoMesDevolucaoCheque;
	}

	public void setReceitaDoMesDevolucaoCheque(BigDecimal receitaDoMesDevolucaoCheque) {
		this.receitaDoMesDevolucaoCheque = receitaDoMesDevolucaoCheque;
	}

	public BigDecimal getReceitaDoMesNegociacao() {
		if (receitaDoMesNegociacao == null) {
			receitaDoMesNegociacao = BigDecimal.ZERO;
		}
		return receitaDoMesNegociacao;
	}

	public void setReceitaDoMesNegociacao(BigDecimal receitaDoMesNegociacao) {
		this.receitaDoMesNegociacao = receitaDoMesNegociacao;
	}

	public BigDecimal getReceitaDoMesBolsaCusteada() {
		if (receitaDoMesBolsaCusteada == null) {
			receitaDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return receitaDoMesBolsaCusteada;
	}

	public void setReceitaDoMesBolsaCusteada(BigDecimal receitaDoMesBolsaCusteada) {
		this.receitaDoMesBolsaCusteada = receitaDoMesBolsaCusteada;
	}

	public BigDecimal getReceitaDoMesInscricao() {
		if (receitaDoMesInscricao == null) {
			receitaDoMesInscricao = BigDecimal.ZERO;
		}
		return receitaDoMesInscricao;
	}

	public void setReceitaDoMesInscricao(BigDecimal receitaDoMesInscricao) {
		this.receitaDoMesInscricao = receitaDoMesInscricao;
	}

	public BigDecimal getReceitaDoMesContratoReceita() {
		if (receitaDoMesContratoReceita == null) {
			receitaDoMesContratoReceita = BigDecimal.ZERO;
		}
		return receitaDoMesContratoReceita;
	}

	public void setReceitaDoMesContratoReceita(BigDecimal receitaDoMesContratoReceita) {
		this.receitaDoMesContratoReceita = receitaDoMesContratoReceita;
	}

	public BigDecimal getReceitaDoMesInclusaoReposicao() {
		if (receitaDoMesInclusaoReposicao == null) {
			receitaDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return receitaDoMesInclusaoReposicao;
	}

	public void setReceitaDoMesInclusaoReposicao(BigDecimal receitaDoMesInclusaoReposicao) {
		this.receitaDoMesInclusaoReposicao = receitaDoMesInclusaoReposicao;
	}

	public BigDecimal getReceitaDoMesOutros() {
		if (receitaDoMesOutros == null) {
			receitaDoMesOutros = BigDecimal.ZERO;
		}
		return receitaDoMesOutros;
	}

	public void setReceitaDoMesOutros(BigDecimal receitaDoMesOutros) {
		this.receitaDoMesOutros = receitaDoMesOutros;
	}

	public BigDecimal getDescontoDoMesMatricula() {
		if (descontoDoMesMatricula == null) {
			descontoDoMesMatricula = BigDecimal.ZERO;
		}
		return descontoDoMesMatricula;
	}

	public void setDescontoDoMesMatricula(BigDecimal descontoDoMesMatricula) {
		this.descontoDoMesMatricula = descontoDoMesMatricula;
	}

	public BigDecimal getDescontoDoMesMensalidade() {
		if (descontoDoMesMensalidade == null) {
			descontoDoMesMensalidade = BigDecimal.ZERO;
		}
		return descontoDoMesMensalidade;
	}

	public void setDescontoDoMesMensalidade(BigDecimal descontoDoMesMensalidade) {
		this.descontoDoMesMensalidade = descontoDoMesMensalidade;
	}

	public BigDecimal getDescontoDoMesRequerimento() {
		if (descontoDoMesRequerimento == null) {
			descontoDoMesRequerimento = BigDecimal.ZERO;
		}
		return descontoDoMesRequerimento;
	}

	public void setDescontoDoMesRequerimento(BigDecimal descontoDoMesRequerimento) {
		this.descontoDoMesRequerimento = descontoDoMesRequerimento;
	}

	public BigDecimal getDescontoDoMesBiblioteca() {
		if (descontoDoMesBiblioteca == null) {
			descontoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return descontoDoMesBiblioteca;
	}

	public void setDescontoDoMesBiblioteca(BigDecimal descontoDoMesBiblioteca) {
		this.descontoDoMesBiblioteca = descontoDoMesBiblioteca;
	}

	public BigDecimal getDescontoDoMesDevolucaoCheque() {
		if (descontoDoMesDevolucaoCheque == null) {
			descontoDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return descontoDoMesDevolucaoCheque;
	}

	public void setDescontoDoMesDevolucaoCheque(BigDecimal descontoDoMesDevolucaoCheque) {
		this.descontoDoMesDevolucaoCheque = descontoDoMesDevolucaoCheque;
	}

	public BigDecimal getDescontoDoMesNegociacao() {
		if (descontoDoMesNegociacao == null) {
			descontoDoMesNegociacao = BigDecimal.ZERO;
		}
		return descontoDoMesNegociacao;
	}

	public void setDescontoDoMesNegociacao(BigDecimal descontoDoMesNegociacao) {
		this.descontoDoMesNegociacao = descontoDoMesNegociacao;
	}

	public BigDecimal getDescontoDoMesBolsaCusteada() {
		if (descontoDoMesBolsaCusteada == null) {
			descontoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return descontoDoMesBolsaCusteada;
	}

	public void setDescontoDoMesBolsaCusteada(BigDecimal descontoDoMesBolsaCusteada) {
		this.descontoDoMesBolsaCusteada = descontoDoMesBolsaCusteada;
	}

	public BigDecimal getDescontoDoMesInscricao() {
		if (descontoDoMesInscricao == null) {
			descontoDoMesInscricao  = BigDecimal.ZERO;
		}
		return descontoDoMesInscricao;
	}

	public void setDescontoDoMesInscricao(BigDecimal descontoDoMesInscricao) {
		this.descontoDoMesInscricao = descontoDoMesInscricao;
	}

	public BigDecimal getDescontoDoMesContratoReceita() {
		if (descontoDoMesContratoReceita == null) {
			descontoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return descontoDoMesContratoReceita;
	}

	public void setDescontoDoMesContratoReceita(BigDecimal descontoDoMesContratoReceita) {
		this.descontoDoMesContratoReceita = descontoDoMesContratoReceita;
	}

	public BigDecimal getDescontoDoMesInclusaoReposicao() {
		if (descontoDoMesInclusaoReposicao == null) {
			descontoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return descontoDoMesInclusaoReposicao;
	}

	public void setDescontoDoMesInclusaoReposicao(BigDecimal descontoDoMesInclusaoReposicao) {
		this.descontoDoMesInclusaoReposicao = descontoDoMesInclusaoReposicao;
	}

	public BigDecimal getDescontoDoMesOutros() {
		if (descontoDoMesOutros == null) {
			descontoDoMesOutros = BigDecimal.ZERO;
		}
		return descontoDoMesOutros;
	}

	public void setDescontoDoMesOutros(BigDecimal descontoDoMesOutros) {
		this.descontoDoMesOutros = descontoDoMesOutros;
	}

	public BigDecimal getAcrescimoDoMesMatricula() {
		if (acrescimoDoMesMatricula == null) {
			acrescimoDoMesMatricula = BigDecimal.ZERO;
		}
		return acrescimoDoMesMatricula;
	}

	public void setAcrescimoDoMesMatricula(BigDecimal acrescimoDoMesMatricula) {
		this.acrescimoDoMesMatricula = acrescimoDoMesMatricula;
	}

	public BigDecimal getAcrescimoDoMesMensalidade() {
		if (acrescimoDoMesMensalidade == null) {
			acrescimoDoMesMensalidade = BigDecimal.ZERO;
		}
		return acrescimoDoMesMensalidade;
	}

	public void setAcrescimoDoMesMensalidade(BigDecimal acrescimoDoMesMensalidade) {
		this.acrescimoDoMesMensalidade = acrescimoDoMesMensalidade;
	}

	public BigDecimal getAcrescimoDoMesRequerimento() {
		if (acrescimoDoMesRequerimento == null) {
			acrescimoDoMesRequerimento = BigDecimal.ZERO;
		}
		return acrescimoDoMesRequerimento;
	}

	public void setAcrescimoDoMesRequerimento(BigDecimal acrescimoDoMesRequerimento) {
		this.acrescimoDoMesRequerimento = acrescimoDoMesRequerimento;
	}

	public BigDecimal getAcrescimoDoMesBiblioteca() {
		if (acrescimoDoMesBiblioteca == null) {
			acrescimoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return acrescimoDoMesBiblioteca;
	}

	public void setAcrescimoDoMesBiblioteca(BigDecimal acrescimoDoMesBiblioteca) {
		this.acrescimoDoMesBiblioteca = acrescimoDoMesBiblioteca;
	}

	public BigDecimal getAcrescimoDoMesDevolucaoCheque() {
		if (acrescimoDoMesDevolucaoCheque == null) {
			acrescimoDoMesDevolucaoCheque =  BigDecimal.ZERO;
		}
		return acrescimoDoMesDevolucaoCheque;
	}

	public void setAcrescimoDoMesDevolucaoCheque(BigDecimal acrescimoDoMesDevolucaoCheque) {
		this.acrescimoDoMesDevolucaoCheque = acrescimoDoMesDevolucaoCheque;
	}

	public BigDecimal getAcrescimoDoMesNegociacao() {
		if (acrescimoDoMesNegociacao == null) {
			acrescimoDoMesNegociacao = BigDecimal.ZERO;
		}
		return acrescimoDoMesNegociacao;
	}

	public void setAcrescimoDoMesNegociacao(BigDecimal acrescimoDoMesNegociacao) {
		this.acrescimoDoMesNegociacao = acrescimoDoMesNegociacao;
	}

	public BigDecimal getAcrescimoDoMesBolsaCusteada() {
		if (acrescimoDoMesBolsaCusteada == null) {
			acrescimoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return acrescimoDoMesBolsaCusteada;
	}

	public void setAcrescimoDoMesBolsaCusteada(BigDecimal acrescimoDoMesBolsaCusteada) {
		this.acrescimoDoMesBolsaCusteada = acrescimoDoMesBolsaCusteada;
	}

	public BigDecimal getAcrescimoDoMesInscricao() {
		if (acrescimoDoMesInscricao == null) {
			acrescimoDoMesInscricao = BigDecimal.ZERO;
		}
		return acrescimoDoMesInscricao;
	}

	public void setAcrescimoDoMesInscricao(BigDecimal acrescimoDoMesInscricao) {
		this.acrescimoDoMesInscricao = acrescimoDoMesInscricao;
	}

	public BigDecimal getAcrescimoDoMesContratoReceita() {
		if (acrescimoDoMesContratoReceita == null) {
			acrescimoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return acrescimoDoMesContratoReceita;
	}

	public void setAcrescimoDoMesContratoReceita(BigDecimal acrescimoDoMesContratoReceita) {
		this.acrescimoDoMesContratoReceita = acrescimoDoMesContratoReceita;
	}

	public BigDecimal getAcrescimoDoMesInclusaoReposicao() {
		if (acrescimoDoMesInclusaoReposicao== null) {
			acrescimoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return acrescimoDoMesInclusaoReposicao;
	}

	public void setAcrescimoDoMesInclusaoReposicao(BigDecimal acrescimoDoMesInclusaoReposicao) {
		this.acrescimoDoMesInclusaoReposicao = acrescimoDoMesInclusaoReposicao;
	}

	public BigDecimal getAcrescimoDoMesOutros() {
		if (acrescimoDoMesOutros == null) {
			acrescimoDoMesOutros = BigDecimal.ZERO;
		}
		return acrescimoDoMesOutros;
	}

	public void setAcrescimoDoMesOutros(BigDecimal acrescimoDoMesOutros) {
		this.acrescimoDoMesOutros = acrescimoDoMesOutros;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesMatricula() {
		if (receitaComDescontoAcrescimoDoMesMatricula == null) {
			receitaComDescontoAcrescimoDoMesMatricula = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesMatricula;
	}

	public void setReceitaComDescontoAcrescimoDoMesMatricula(BigDecimal receitaComDescontoAcrescimoDoMesMatricula) {
		this.receitaComDescontoAcrescimoDoMesMatricula = receitaComDescontoAcrescimoDoMesMatricula;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesMensalidade() {
		if (receitaComDescontoAcrescimoDoMesMensalidade == null) {
			receitaComDescontoAcrescimoDoMesMensalidade = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesMensalidade;
	}

	public void setReceitaComDescontoAcrescimoDoMesMensalidade(BigDecimal receitaComDescontoAcrescimoDoMesMensalidade) {
		this.receitaComDescontoAcrescimoDoMesMensalidade = receitaComDescontoAcrescimoDoMesMensalidade;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesRequerimento() {
		if (receitaComDescontoAcrescimoDoMesRequerimento == null) {
			receitaComDescontoAcrescimoDoMesRequerimento= BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesRequerimento;
	}

	public void setReceitaComDescontoAcrescimoDoMesRequerimento(BigDecimal receitaComDescontoAcrescimoDoMesRequerimento) {
		this.receitaComDescontoAcrescimoDoMesRequerimento = receitaComDescontoAcrescimoDoMesRequerimento;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesBiblioteca() {
		if (receitaComDescontoAcrescimoDoMesBiblioteca == null) {
			receitaComDescontoAcrescimoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesBiblioteca;
	}

	public void setReceitaComDescontoAcrescimoDoMesBiblioteca(BigDecimal receitaComDescontoAcrescimoDoMesBiblioteca) {
		this.receitaComDescontoAcrescimoDoMesBiblioteca = receitaComDescontoAcrescimoDoMesBiblioteca;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesDevolucaoCheque() {
		if (receitaComDescontoAcrescimoDoMesDevolucaoCheque == null) {
			receitaComDescontoAcrescimoDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesDevolucaoCheque;
	}

	public void setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(BigDecimal receitaComDescontoAcrescimoDoMesDevolucaoCheque) {
		this.receitaComDescontoAcrescimoDoMesDevolucaoCheque = receitaComDescontoAcrescimoDoMesDevolucaoCheque;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesNegociacao() {
		if (receitaComDescontoAcrescimoDoMesNegociacao == null) {
			receitaComDescontoAcrescimoDoMesNegociacao = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesNegociacao;
	}

	public void setReceitaComDescontoAcrescimoDoMesNegociacao(BigDecimal receitaComDescontoAcrescimoDoMesNegociacao) {
		this.receitaComDescontoAcrescimoDoMesNegociacao = receitaComDescontoAcrescimoDoMesNegociacao;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesBolsaCusteada() {
		if (receitaComDescontoAcrescimoDoMesBolsaCusteada == null) {
			receitaComDescontoAcrescimoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesBolsaCusteada;
	}

	public void setReceitaComDescontoAcrescimoDoMesBolsaCusteada(BigDecimal receitaComDescontoAcrescimoDoMesBolsaCusteada) {
		this.receitaComDescontoAcrescimoDoMesBolsaCusteada = receitaComDescontoAcrescimoDoMesBolsaCusteada;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesInscricao() {
		if (receitaComDescontoAcrescimoDoMesInscricao == null) {
			receitaComDescontoAcrescimoDoMesInscricao = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesInscricao;
	}

	public void setReceitaComDescontoAcrescimoDoMesInscricao(BigDecimal receitaComDescontoAcrescimoDoMesInscricao) {
		this.receitaComDescontoAcrescimoDoMesInscricao = receitaComDescontoAcrescimoDoMesInscricao;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesContratoReceita() {
		if (receitaComDescontoAcrescimoDoMesContratoReceita == null) {
			receitaComDescontoAcrescimoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesContratoReceita;
	}

	public void setReceitaComDescontoAcrescimoDoMesContratoReceita(BigDecimal receitaComDescontoAcrescimoDoMesContratoReceita) {
		this.receitaComDescontoAcrescimoDoMesContratoReceita = receitaComDescontoAcrescimoDoMesContratoReceita;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesInclusaoReposicao() {
		if (receitaComDescontoAcrescimoDoMesInclusaoReposicao == null) {
			receitaComDescontoAcrescimoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesInclusaoReposicao;
	}

	public void setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(BigDecimal receitaComDescontoAcrescimoDoMesInclusaoReposicao) {
		this.receitaComDescontoAcrescimoDoMesInclusaoReposicao = receitaComDescontoAcrescimoDoMesInclusaoReposicao;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesOutros() {
		if (receitaComDescontoAcrescimoDoMesOutros == null) {
			receitaComDescontoAcrescimoDoMesOutros = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesOutros;
	}

	public void setReceitaComDescontoAcrescimoDoMesOutros(BigDecimal receitaComDescontoAcrescimoDoMesOutros) {
		this.receitaComDescontoAcrescimoDoMesOutros = receitaComDescontoAcrescimoDoMesOutros;
	}

	public BigDecimal getValorRecebidoDoMesMatricula() {
		if (valorRecebidoDoMesMatricula == null) {
			valorRecebidoDoMesMatricula = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesMatricula;
	}

	public void setValorRecebidoDoMesMatricula(BigDecimal valorRecebidoDoMesMatricula) {
		this.valorRecebidoDoMesMatricula = valorRecebidoDoMesMatricula;
	}

	public BigDecimal getValorRecebidoDoMesMensalidade() {
		if (valorRecebidoDoMesMensalidade == null) {
			valorRecebidoDoMesMensalidade = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesMensalidade;
	}

	public void setValorRecebidoDoMesMensalidade(BigDecimal valorRecebidoDoMesMensalidade) {
		this.valorRecebidoDoMesMensalidade = valorRecebidoDoMesMensalidade;
	}

	public BigDecimal getValorRecebidoDoMesRequerimento() {
		if (valorRecebidoDoMesRequerimento == null) {
			valorRecebidoDoMesRequerimento = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesRequerimento;
	}

	public void setValorRecebidoDoMesRequerimento(BigDecimal valorRecebidoDoMesRequerimento) {
		this.valorRecebidoDoMesRequerimento = valorRecebidoDoMesRequerimento;
	}

	public BigDecimal getValorRecebidoDoMesBiblioteca() {
		if (valorRecebidoDoMesBiblioteca == null) {
			valorRecebidoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesBiblioteca;
	}

	public void setValorRecebidoDoMesBiblioteca(BigDecimal valorRecebidoDoMesBiblioteca) {
		this.valorRecebidoDoMesBiblioteca = valorRecebidoDoMesBiblioteca;
	}

	public BigDecimal getValorRecebidoDoMesDevolucaoCheque() {
		if (valorRecebidoDoMesDevolucaoCheque == null) {
			valorRecebidoDoMesDevolucaoCheque= BigDecimal.ZERO;
		}
		return valorRecebidoDoMesDevolucaoCheque;
	}

	public void setValorRecebidoDoMesDevolucaoCheque(BigDecimal valorRecebidoDoMesDevolucaoCheque) {
		this.valorRecebidoDoMesDevolucaoCheque = valorRecebidoDoMesDevolucaoCheque;
	}

	public BigDecimal getValorRecebidoDoMesNegociacao() {
		if (valorRecebidoDoMesNegociacao == null) {
			valorRecebidoDoMesNegociacao = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesNegociacao;
	}

	public void setValorRecebidoDoMesNegociacao(BigDecimal valorRecebidoDoMesNegociacao) {
		this.valorRecebidoDoMesNegociacao = valorRecebidoDoMesNegociacao;
	}

	public BigDecimal getValorRecebidoDoMesBolsaCusteada() {
		if (valorRecebidoDoMesBolsaCusteada == null) {
			valorRecebidoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesBolsaCusteada;
	}

	public void setValorRecebidoDoMesBolsaCusteada(BigDecimal valorRecebidoDoMesBolsaCusteada) {
		this.valorRecebidoDoMesBolsaCusteada = valorRecebidoDoMesBolsaCusteada;
	}

	public BigDecimal getValorRecebidoDoMesInscricao() {
		if (valorRecebidoDoMesInscricao == null) {
			valorRecebidoDoMesInscricao = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesInscricao;
	}

	public void setValorRecebidoDoMesInscricao(BigDecimal valorRecebidoDoMesInscricao) {
		this.valorRecebidoDoMesInscricao = valorRecebidoDoMesInscricao;
	}

	public BigDecimal getValorRecebidoDoMesContratoReceita() {
		if (valorRecebidoDoMesContratoReceita == null) {
			valorRecebidoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesContratoReceita;
	}

	public void setValorRecebidoDoMesContratoReceita(BigDecimal valorRecebidoDoMesContratoReceita) {
		this.valorRecebidoDoMesContratoReceita = valorRecebidoDoMesContratoReceita;
	}

	public BigDecimal getValorRecebidoDoMesInclusaoReposicao() {
		if (valorRecebidoDoMesInclusaoReposicao == null) {
			valorRecebidoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesInclusaoReposicao;
	}

	public void setValorRecebidoDoMesInclusaoReposicao(BigDecimal valorRecebidoDoMesInclusaoReposicao) {
		this.valorRecebidoDoMesInclusaoReposicao = valorRecebidoDoMesInclusaoReposicao;
	}

	public BigDecimal getValorRecebidoDoMesOutros() {
		if (valorRecebidoDoMesOutros == null) {
			valorRecebidoDoMesOutros = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesOutros;
	}

	public void setValorRecebidoDoMesOutros(BigDecimal valorRecebidoDoMesOutros) {
		this.valorRecebidoDoMesOutros = valorRecebidoDoMesOutros;
	}

	public BigDecimal getSaldoReceberDoMesMatricula() {
		if (saldoReceberDoMesMatricula == null) {
			saldoReceberDoMesMatricula = BigDecimal.ZERO;
		}
		return saldoReceberDoMesMatricula;
	}

	public void setSaldoReceberDoMesMatricula(BigDecimal saldoReceberDoMesMatricula) {
		this.saldoReceberDoMesMatricula = saldoReceberDoMesMatricula;
	}

	public BigDecimal getSaldoReceberDoMesMensalidade() {
		if (saldoReceberDoMesMensalidade == null) {
			saldoReceberDoMesMensalidade = BigDecimal.ZERO;
		}
		return saldoReceberDoMesMensalidade;
	}

	public void setSaldoReceberDoMesMensalidade(BigDecimal saldoReceberDoMesMensalidade) {
		this.saldoReceberDoMesMensalidade = saldoReceberDoMesMensalidade;
	}

	public BigDecimal getSaldoReceberDoMesRequerimento() {
		if (saldoReceberDoMesRequerimento == null) {
			saldoReceberDoMesRequerimento = BigDecimal.ZERO;
		}
		return saldoReceberDoMesRequerimento;
	}

	public void setSaldoReceberDoMesRequerimento(BigDecimal saldoReceberDoMesRequerimento) {
		this.saldoReceberDoMesRequerimento = saldoReceberDoMesRequerimento;
	}

	public BigDecimal getSaldoReceberDoMesBiblioteca() {
		if (saldoReceberDoMesBiblioteca == null) {
			saldoReceberDoMesBiblioteca = BigDecimal.ZERO;
		}
		return saldoReceberDoMesBiblioteca;
	}

	public void setSaldoReceberDoMesBiblioteca(BigDecimal saldoReceberDoMesBiblioteca) {
		this.saldoReceberDoMesBiblioteca = saldoReceberDoMesBiblioteca;
	}

	public BigDecimal getSaldoReceberDoMesDevolucaoCheque() {
		if (saldoReceberDoMesDevolucaoCheque == null) {
			saldoReceberDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return saldoReceberDoMesDevolucaoCheque;
	}

	public void setSaldoReceberDoMesDevolucaoCheque(BigDecimal saldoReceberDoMesDevolucaoCheque) {
		this.saldoReceberDoMesDevolucaoCheque = saldoReceberDoMesDevolucaoCheque;
	}

	public BigDecimal getSaldoReceberDoMesNegociacao() {
		if (saldoReceberDoMesNegociacao == null) {
			saldoReceberDoMesNegociacao = BigDecimal.ZERO;
		}
		return saldoReceberDoMesNegociacao;
	}

	public void setSaldoReceberDoMesNegociacao(BigDecimal saldoReceberDoMesNegociacao) {
		this.saldoReceberDoMesNegociacao = saldoReceberDoMesNegociacao;
	}

	public BigDecimal getSaldoReceberDoMesBolsaCusteada() {
		if (saldoReceberDoMesBolsaCusteada == null) {
			saldoReceberDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return saldoReceberDoMesBolsaCusteada;
	}

	public void setSaldoReceberDoMesBolsaCusteada(BigDecimal saldoReceberDoMesBolsaCusteada) {
		this.saldoReceberDoMesBolsaCusteada = saldoReceberDoMesBolsaCusteada;
	}

	public BigDecimal getSaldoReceberDoMesInscricao() {
		if (saldoReceberDoMesInscricao == null) {
			saldoReceberDoMesInscricao = BigDecimal.ZERO;
		}
		return saldoReceberDoMesInscricao;
	}

	public void setSaldoReceberDoMesInscricao(BigDecimal saldoReceberDoMesInscricao) {
		this.saldoReceberDoMesInscricao = saldoReceberDoMesInscricao;
	}

	public BigDecimal getSaldoReceberDoMesContratoReceita() {
		if (saldoReceberDoMesContratoReceita == null) {
			saldoReceberDoMesContratoReceita = BigDecimal.ZERO;
		}
		return saldoReceberDoMesContratoReceita;
	}

	public void setSaldoReceberDoMesContratoReceita(BigDecimal saldoReceberDoMesContratoReceita) {
		this.saldoReceberDoMesContratoReceita = saldoReceberDoMesContratoReceita;
	}

	public BigDecimal getSaldoReceberDoMesInclusaoReposicao() {
		if (saldoReceberDoMesInclusaoReposicao == null) {
			saldoReceberDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return saldoReceberDoMesInclusaoReposicao;
	}

	public void setSaldoReceberDoMesInclusaoReposicao(BigDecimal saldoReceberDoMesInclusaoReposicao) {
		this.saldoReceberDoMesInclusaoReposicao = saldoReceberDoMesInclusaoReposicao;
	}

	public BigDecimal getSaldoReceberDoMesOutros() {
		if (saldoReceberDoMesOutros== null) {
			saldoReceberDoMesOutros = BigDecimal.ZERO;
		}
		return saldoReceberDoMesOutros;
	}

	public void setSaldoReceberDoMesOutros(BigDecimal saldoReceberDoMesOutros) {
		this.saldoReceberDoMesOutros = saldoReceberDoMesOutros;
	}

	public BigDecimal getTotalVencidoDoMesMatricula() {
		if (totalVencidoDoMesMatricula == null) {
			totalVencidoDoMesMatricula = BigDecimal.ZERO;
		}
		return totalVencidoDoMesMatricula;
	}

	public void setTotalVencidoDoMesMatricula(BigDecimal totalVencidoDoMesMatricula) {
		this.totalVencidoDoMesMatricula = totalVencidoDoMesMatricula;
	}

	public BigDecimal getTotalVencidoDoMesMensalidade() {
		if (totalVencidoDoMesMensalidade == null) {
			totalVencidoDoMesMensalidade = BigDecimal.ZERO;
		}
		return totalVencidoDoMesMensalidade;
	}

	public void setTotalVencidoDoMesMensalidade(BigDecimal totalVencidoDoMesMensalidade) {
		this.totalVencidoDoMesMensalidade = totalVencidoDoMesMensalidade;
	}

	public BigDecimal getTotalVencidoDoMesRequerimento() {
		if (totalVencidoDoMesRequerimento == null) {
			totalVencidoDoMesRequerimento = BigDecimal.ZERO;
		}
		return totalVencidoDoMesRequerimento;
	}

	public void setTotalVencidoDoMesRequerimento(BigDecimal totalVencidoDoMesRequerimento) {
		this.totalVencidoDoMesRequerimento = totalVencidoDoMesRequerimento;
	}

	public BigDecimal getTotalVencidoDoMesBiblioteca() {
		if (totalVencidoDoMesBiblioteca == null) {
			totalVencidoDoMesBiblioteca = BigDecimal.ZERO;
		}
		return totalVencidoDoMesBiblioteca;
	}

	public void setTotalVencidoDoMesBiblioteca(BigDecimal totalVencidoDoMesBiblioteca) {
		this.totalVencidoDoMesBiblioteca = totalVencidoDoMesBiblioteca;
	}

	public BigDecimal getTotalVencidoDoMesDevolucaoCheque() {
		if (totalVencidoDoMesDevolucaoCheque == null) {
			totalVencidoDoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return totalVencidoDoMesDevolucaoCheque;
	}

	public void setTotalVencidoDoMesDevolucaoCheque(BigDecimal totalVencidoDoMesDevolucaoCheque) {
		this.totalVencidoDoMesDevolucaoCheque = totalVencidoDoMesDevolucaoCheque;
	}

	public BigDecimal getTotalVencidoDoMesNegociacao() {
		if (totalVencidoDoMesNegociacao == null) {
			totalVencidoDoMesNegociacao = BigDecimal.ZERO;
		}
		return totalVencidoDoMesNegociacao;
	}

	public void setTotalVencidoDoMesNegociacao(BigDecimal totalVencidoDoMesNegociacao) {
		this.totalVencidoDoMesNegociacao = totalVencidoDoMesNegociacao;
	}

	public BigDecimal getTotalVencidoDoMesBolsaCusteada() {
		if (totalVencidoDoMesBolsaCusteada == null) {
			totalVencidoDoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return totalVencidoDoMesBolsaCusteada;
	}

	public void setTotalVencidoDoMesBolsaCusteada(BigDecimal totalVencidoDoMesBolsaCusteada) {
		this.totalVencidoDoMesBolsaCusteada = totalVencidoDoMesBolsaCusteada;
	}

	public BigDecimal getTotalVencidoDoMesInscricao() {
		if (totalVencidoDoMesInscricao == null) {
			totalVencidoDoMesInscricao = BigDecimal.ZERO;
		}
		return totalVencidoDoMesInscricao;
	}

	public void setTotalVencidoDoMesInscricao(BigDecimal totalVencidoDoMesInscricao) {
		this.totalVencidoDoMesInscricao = totalVencidoDoMesInscricao;
	}

	public BigDecimal getTotalVencidoDoMesContratoReceita() {
		if (totalVencidoDoMesContratoReceita == null) {
			totalVencidoDoMesContratoReceita = BigDecimal.ZERO;
		}
		return totalVencidoDoMesContratoReceita;
	}

	public void setTotalVencidoDoMesContratoReceita(BigDecimal totalVencidoDoMesContratoReceita) {
		this.totalVencidoDoMesContratoReceita = totalVencidoDoMesContratoReceita;
	}

	public BigDecimal getTotalVencidoDoMesInclusaoReposicao() {
		if (totalVencidoDoMesInclusaoReposicao == null) {
			totalVencidoDoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return totalVencidoDoMesInclusaoReposicao;
	}

	public void setTotalVencidoDoMesInclusaoReposicao(BigDecimal totalVencidoDoMesInclusaoReposicao) {
		this.totalVencidoDoMesInclusaoReposicao = totalVencidoDoMesInclusaoReposicao;
	}

	public BigDecimal getTotalVencidoDoMesOutros() {
		if (totalVencidoDoMesOutros == null) {
			totalVencidoDoMesOutros = BigDecimal.ZERO;
		}
		return totalVencidoDoMesOutros;
	}

	public void setTotalVencidoDoMesOutros(BigDecimal totalVencidoDoMesOutros) {
		this.totalVencidoDoMesOutros = totalVencidoDoMesOutros;
	}

	public BigDecimal getTotalVencidoDoMes() {
		if (totalVencidoDoMes == null) {
			totalVencidoDoMes = BigDecimal.ZERO;
		}
		return totalVencidoDoMes;
	}
	
	public BigDecimal getTotalVencidoDoMesSemAcrescimo() {
		return getTotalVencidoDoMes().subtract(getAcrescimoDoMes());
	}

	public void setTotalVencidoDoMes(BigDecimal totalVencidoDoMes) {
		this.totalVencidoDoMes = totalVencidoDoMes;
	}

	public BigDecimal getTotalValorContareceber() {
		if (totalValorContareceber == null) {
			totalValorContareceber = BigDecimal.ZERO;
		}
		return totalValorContareceber;
	}

	public void setTotalValorContareceber(BigDecimal totalValorContareceber) {
		this.totalValorContareceber = totalValorContareceber;
	}

	public BigDecimal getTotalDescontoConvenioContareceber() {
		if (totalDescontoConvenioContareceber == null) {
			totalDescontoConvenioContareceber = BigDecimal.ZERO;
		}
		return totalDescontoConvenioContareceber;
	}

	public void setTotalDescontoConvenioContareceber(BigDecimal totalDescontoConvenioContareceber) {
		this.totalDescontoConvenioContareceber = totalDescontoConvenioContareceber;
	}

	public BigDecimal getTotalDescontoInstituicaoContareceber() {
		if (totalDescontoInstituicaoContareceber == null) {
			totalDescontoInstituicaoContareceber = BigDecimal.ZERO;
		}
		return totalDescontoInstituicaoContareceber;
	}

	public void setTotalDescontoInstituicaoContareceber(BigDecimal totalDescontoInstituicaoContareceber) {
		this.totalDescontoInstituicaoContareceber = totalDescontoInstituicaoContareceber;
	}

	public BigDecimal getTotalDescontoProgressivoContareceber() {
		if (totalDescontoProgressivoContareceber == null) {
			totalDescontoProgressivoContareceber = BigDecimal.ZERO;
		}
		return totalDescontoProgressivoContareceber;
	}

	public void setTotalDescontoProgressivoContareceber(BigDecimal totalDescontoProgressivoContareceber) {
		this.totalDescontoProgressivoContareceber = totalDescontoProgressivoContareceber;
	}

	public BigDecimal getTotalDescontoAlunoContareceber() {
		if (totalDescontoAlunoContareceber == null) {
			totalDescontoAlunoContareceber = BigDecimal.ZERO;
		}
		return totalDescontoAlunoContareceber;
	}

	public void setTotalDescontoAlunoContareceber(BigDecimal totalDescontoAlunoContareceber) {
		this.totalDescontoAlunoContareceber = totalDescontoAlunoContareceber;
	}

	public BigDecimal getTotalDescontoRecebimentoContareceber() {
		if (totalDescontoRecebimentoContareceber == null) {
			totalDescontoRecebimentoContareceber = BigDecimal.ZERO;
		}
		return totalDescontoRecebimentoContareceber;
	}

	public void setTotalDescontoRecebimentoContareceber(BigDecimal totalDescontoRecebimentoContareceber) {
		this.totalDescontoRecebimentoContareceber = totalDescontoRecebimentoContareceber;
	}

	public BigDecimal getTotalAcrescimoContaReceber() {
		if (totalAcrescimoContaReceber == null) {
			totalAcrescimoContaReceber = BigDecimal.ZERO;
		}
		return totalAcrescimoContaReceber;
	}

	public void setTotalAcrescimoContaReceber(BigDecimal totalAcrescimoContaReceber) {
		this.totalAcrescimoContaReceber = totalAcrescimoContaReceber;
	}
	
	

	public Double getProvisaoReceberMesMaterialDidatico() {
		if (provisaoReceberMesMaterialDidatico == null) {
			provisaoReceberMesMaterialDidatico = 0.0;
		}
		return provisaoReceberMesMaterialDidatico;
	}

	public void setProvisaoReceberMesMaterialDidatico(Double provisaoReceberMesMaterialDidatico) {
		this.provisaoReceberMesMaterialDidatico = provisaoReceberMesMaterialDidatico;
	}

	public Double getSaldoReceberValorCheioMaterialDidatico() {
		if (saldoReceberValorCheioMaterialDidatico == null) {
			saldoReceberValorCheioMaterialDidatico = 0.0;
		}
		return saldoReceberValorCheioMaterialDidatico;
	}

	public void setSaldoReceberValorCheioMaterialDidatico(Double saldoReceberValorCheioMaterialDidatico) {
		this.saldoReceberValorCheioMaterialDidatico = saldoReceberValorCheioMaterialDidatico;
	}

	public Double getTotalVencidoNoMesMaterialDidatico() {
		if (totalVencidoNoMesMaterialDidatico == null) {
			totalVencidoNoMesMaterialDidatico = 0.0;
		}
		return totalVencidoNoMesMaterialDidatico;
	}

	public void setTotalVencidoNoMesMaterialDidatico(Double totalVencidoNoMesMaterialDidatico) {
		this.totalVencidoNoMesMaterialDidatico = totalVencidoNoMesMaterialDidatico;
	}

	public BigDecimal getReceitaDoMesMaterialDidatico() {
		if (receitaDoMesMaterialDidatico == null) {
			receitaDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return receitaDoMesMaterialDidatico;
	}

	public void setReceitaDoMesMaterialDidatico(BigDecimal receitaDoMesMaterialDidatico) {
		this.receitaDoMesMaterialDidatico = receitaDoMesMaterialDidatico;
	}

	public BigDecimal getDescontoDoMesMaterialDidatico() {
		if (descontoDoMesMaterialDidatico == null) {
			descontoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return descontoDoMesMaterialDidatico;
	}

	public void setDescontoDoMesMaterialDidatico(BigDecimal descontoDoMesMaterialDidatico) {
		this.descontoDoMesMaterialDidatico = descontoDoMesMaterialDidatico;
	}

	public BigDecimal getAcrescimoDoMesMaterialDidatico() {
		if (acrescimoDoMesMaterialDidatico == null) {
			acrescimoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return acrescimoDoMesMaterialDidatico;
	}

	public void setAcrescimoDoMesMaterialDidatico(BigDecimal acrescimoDoMesMaterialDidatico) {
		this.acrescimoDoMesMaterialDidatico = acrescimoDoMesMaterialDidatico;
	}

	public BigDecimal getReceitaComDescontoAcrescimoDoMesMaterialDidatico() {
		if (receitaComDescontoAcrescimoDoMesMaterialDidatico == null) {
			receitaComDescontoAcrescimoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return receitaComDescontoAcrescimoDoMesMaterialDidatico;
	}

	public void setReceitaComDescontoAcrescimoDoMesMaterialDidatico(BigDecimal receitaComDescontoAcrescimoDoMesMaterialDidatico) {
		this.receitaComDescontoAcrescimoDoMesMaterialDidatico = receitaComDescontoAcrescimoDoMesMaterialDidatico;
	}

	public BigDecimal getValorRecebidoDoMesMaterialDidatico() {
		if (valorRecebidoDoMesMaterialDidatico == null) {
			valorRecebidoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return valorRecebidoDoMesMaterialDidatico;
	}

	public void setValorRecebidoDoMesMaterialDidatico(BigDecimal valorRecebidoDoMesMaterialDidatico) {
		this.valorRecebidoDoMesMaterialDidatico = valorRecebidoDoMesMaterialDidatico;
	}

	public BigDecimal getSaldoReceberDoMesMaterialDidatico() {
		if (saldoReceberDoMesMaterialDidatico == null) {
			saldoReceberDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return saldoReceberDoMesMaterialDidatico;
	}

	public void setSaldoReceberDoMesMaterialDidatico(BigDecimal saldoReceberDoMesMaterialDidatico) {
		this.saldoReceberDoMesMaterialDidatico = saldoReceberDoMesMaterialDidatico;
	}

	public BigDecimal getTotalVencidoDoMesMaterialDidatico() {
		if (totalVencidoDoMesMaterialDidatico == null) {
			totalVencidoDoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return totalVencidoDoMesMaterialDidatico;
	}

	public void setTotalVencidoDoMesMaterialDidatico(BigDecimal totalVencidoDoMesMaterialDidatico) {
		this.totalVencidoDoMesMaterialDidatico = totalVencidoDoMesMaterialDidatico;
	}

	public BigDecimal getReceitaNoMesMaterialDidatico() {
		if (receitaNoMesMaterialDidatico == null) {
			receitaNoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return receitaNoMesMaterialDidatico;
	}

	public void setReceitaNoMesMaterialDidatico(BigDecimal receitaNoMesMaterialDidatico) {
		this.receitaNoMesMaterialDidatico = receitaNoMesMaterialDidatico;
	}

	public BigDecimal getDescontoNoMesMaterialDidatico() {
		if (descontoNoMesMaterialDidatico == null) {
			descontoNoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return descontoNoMesMaterialDidatico;
	}

	public void setDescontoNoMesMaterialDidatico(BigDecimal descontoNoMesMaterialDidatico) {
		this.descontoNoMesMaterialDidatico = descontoNoMesMaterialDidatico;
	}

	public BigDecimal getAcrescimoNoMesMaterialDidatico() {
		if (acrescimoNoMesMaterialDidatico == null) {
			acrescimoNoMesMaterialDidatico = BigDecimal.ZERO;
		}
		return acrescimoNoMesMaterialDidatico;
	}

	public void setAcrescimoNoMesMaterialDidatico(BigDecimal acrescimoNoMesMaterialDidatico) {
		this.acrescimoNoMesMaterialDidatico = acrescimoNoMesMaterialDidatico;
	}

	public BigDecimal getReceitaNoMes() {
		if (receitaNoMes == null) {
			receitaNoMes = BigDecimal.ZERO;
		}
		return receitaNoMes;
	}

	public void setReceitaNoMes(BigDecimal receitaNoMes) {
		this.receitaNoMes = receitaNoMes;
	}

	public BigDecimal getDescontoNoMes() {
		if (descontoNoMes == null) {
			descontoNoMes = BigDecimal.ZERO;
		}
		return descontoNoMes;
	}

	public void setDescontoNoMes(BigDecimal descontoNoMes) {
		this.descontoNoMes = descontoNoMes;
	}

	public BigDecimal getAcrescimoNoMes() {
		if (acrescimoNoMes == null) {
			acrescimoNoMes = BigDecimal.ZERO;
		}
		return acrescimoNoMes;
	}

	public void setAcrescimoNoMes(BigDecimal acrescimoNoMes) {
		this.acrescimoNoMes = acrescimoNoMes;
	}

	public BigDecimal getReceitaNoMesMatricula() {
		if (receitaNoMesMatricula == null) {
			receitaNoMesMatricula = BigDecimal.ZERO;
		}
		return receitaNoMesMatricula;
	}

	public void setReceitaNoMesMatricula(BigDecimal receitaNoMesMatricula) {
		this.receitaNoMesMatricula = receitaNoMesMatricula;
	}

	public BigDecimal getReceitaNoMesMensalidade() {
		if (receitaNoMesMensalidade == null) {
			receitaNoMesMensalidade = BigDecimal.ZERO;
		}
		return receitaNoMesMensalidade;
	}

	public void setReceitaNoMesMensalidade(BigDecimal receitaNoMesMensalidade) {
		this.receitaNoMesMensalidade = receitaNoMesMensalidade;
	}

	public BigDecimal getReceitaNoMesRequerimento() {
		if (receitaNoMesRequerimento == null) {
			receitaNoMesRequerimento = BigDecimal.ZERO;
		}
		return receitaNoMesRequerimento;
	}

	public void setReceitaNoMesRequerimento(BigDecimal receitaNoMesRequerimento) {
		this.receitaNoMesRequerimento = receitaNoMesRequerimento;
	}

	public BigDecimal getReceitaNoMesBiblioteca() {
		if (receitaNoMesBiblioteca == null) {
			receitaNoMesBiblioteca = BigDecimal.ZERO;
		}
		return receitaNoMesBiblioteca;
	}

	public void setReceitaNoMesBiblioteca(BigDecimal receitaNoMesBiblioteca) {
		this.receitaNoMesBiblioteca = receitaNoMesBiblioteca;
	}

	public BigDecimal getReceitaNoMesDevolucaoCheque() {
		if (receitaNoMesDevolucaoCheque == null) {
			receitaNoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return receitaNoMesDevolucaoCheque;
	}

	public void setReceitaNoMesDevolucaoCheque(BigDecimal receitaNoMesDevolucaoCheque) {
		this.receitaNoMesDevolucaoCheque = receitaNoMesDevolucaoCheque;
	}

	public BigDecimal getReceitaNoMesNegociacao() {
		if (receitaNoMesNegociacao == null) {
			receitaNoMesNegociacao = BigDecimal.ZERO;
		}
		return receitaNoMesNegociacao;
	}

	public void setReceitaNoMesNegociacao(BigDecimal receitaNoMesNegociacao) {
		this.receitaNoMesNegociacao = receitaNoMesNegociacao;
	}

	public BigDecimal getReceitaNoMesBolsaCusteada() {
		if (receitaNoMesBolsaCusteada == null) {
			receitaNoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return receitaNoMesBolsaCusteada;
	}

	public void setReceitaNoMesBolsaCusteada(BigDecimal receitaNoMesBolsaCusteada) {
		this.receitaNoMesBolsaCusteada = receitaNoMesBolsaCusteada;
	}

	public BigDecimal getReceitaNoMesInscricao() {
		if (receitaNoMesInscricao == null) {
			receitaNoMesInscricao = BigDecimal.ZERO;
		}
		return receitaNoMesInscricao;
	}

	public void setReceitaNoMesInscricao(BigDecimal receitaNoMesInscricao) {
		this.receitaNoMesInscricao = receitaNoMesInscricao;
	}

	public BigDecimal getReceitaNoMesContratoReceita() {
		if (receitaNoMesContratoReceita == null) {
			receitaNoMesContratoReceita = BigDecimal.ZERO;
		}
		return receitaNoMesContratoReceita;
	}

	public void setReceitaNoMesContratoReceita(BigDecimal receitaNoMesContratoReceita) {
		this.receitaNoMesContratoReceita = receitaNoMesContratoReceita;
	}

	public BigDecimal getReceitaNoMesInclusaoReposicao() {
		if (receitaNoMesInclusaoReposicao == null) {
			receitaNoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return receitaNoMesInclusaoReposicao;
	}

	public void setReceitaNoMesInclusaoReposicao(BigDecimal receitaNoMesInclusaoReposicao) {
		this.receitaNoMesInclusaoReposicao = receitaNoMesInclusaoReposicao;
	}

	public BigDecimal getReceitaNoMesOutros() {
		if (receitaNoMesOutros == null) {
			receitaNoMesOutros = BigDecimal.ZERO;
		}
		return receitaNoMesOutros;
	}

	public void setReceitaNoMesOutros(BigDecimal receitaNoMesOutros) {
		this.receitaNoMesOutros = receitaNoMesOutros;
	}

	public BigDecimal getDescontoNoMesMatricula() {
		if (descontoNoMesMatricula == null) {
			descontoNoMesMatricula = BigDecimal.ZERO;
		}
		return descontoNoMesMatricula;
	}

	public void setDescontoNoMesMatricula(BigDecimal descontoNoMesMatricula) {
		this.descontoNoMesMatricula = descontoNoMesMatricula;
	}

	public BigDecimal getDescontoNoMesMensalidade() {
		if (descontoNoMesMensalidade == null) {
			descontoNoMesMensalidade = BigDecimal.ZERO;
		}
		return descontoNoMesMensalidade;
	}

	public void setDescontoNoMesMensalidade(BigDecimal descontoNoMesMensalidade) {
		this.descontoNoMesMensalidade = descontoNoMesMensalidade;
	}

	public BigDecimal getDescontoNoMesRequerimento() {
		if (descontoNoMesRequerimento == null) {
			descontoNoMesRequerimento = BigDecimal.ZERO;
		}
		return descontoNoMesRequerimento;
	}

	public void setDescontoNoMesRequerimento(BigDecimal descontoNoMesRequerimento) {
		this.descontoNoMesRequerimento = descontoNoMesRequerimento;
	}

	public BigDecimal getDescontoNoMesBiblioteca() {
		if (descontoNoMesBiblioteca == null) {
			descontoNoMesBiblioteca = BigDecimal.ZERO;
		}
		return descontoNoMesBiblioteca;
	}

	public void setDescontoNoMesBiblioteca(BigDecimal descontoNoMesBiblioteca) {
		this.descontoNoMesBiblioteca = descontoNoMesBiblioteca;
	}

	public BigDecimal getDescontoNoMesDevolucaoCheque() {
		if (descontoNoMesDevolucaoCheque == null) {
			descontoNoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return descontoNoMesDevolucaoCheque;
	}

	public void setDescontoNoMesDevolucaoCheque(BigDecimal descontoNoMesDevolucaoCheque) {
		this.descontoNoMesDevolucaoCheque = descontoNoMesDevolucaoCheque;
	}

	public BigDecimal getDescontoNoMesNegociacao() {
		if (descontoNoMesNegociacao == null) {
			descontoNoMesNegociacao = BigDecimal.ZERO;
		}
		return descontoNoMesNegociacao;
	}

	public void setDescontoNoMesNegociacao(BigDecimal descontoNoMesNegociacao) {
		this.descontoNoMesNegociacao = descontoNoMesNegociacao;
	}

	public BigDecimal getDescontoNoMesBolsaCusteada() {
		if (descontoNoMesBolsaCusteada == null) {
			descontoNoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return descontoNoMesBolsaCusteada;
	}

	public void setDescontoNoMesBolsaCusteada(BigDecimal descontoNoMesBolsaCusteada) {
		this.descontoNoMesBolsaCusteada = descontoNoMesBolsaCusteada;
	}

	public BigDecimal getDescontoNoMesInscricao() {
		if (descontoNoMesInscricao == null) {
			descontoNoMesInscricao = BigDecimal.ZERO;
		}
		return descontoNoMesInscricao;
	}

	public void setDescontoNoMesInscricao(BigDecimal descontoNoMesInscricao) {
		this.descontoNoMesInscricao = descontoNoMesInscricao;
	}

	public BigDecimal getDescontoNoMesContratoReceita() {
		if (descontoNoMesContratoReceita == null) {
			descontoNoMesContratoReceita = BigDecimal.ZERO;
		}
		return descontoNoMesContratoReceita;
	}

	public void setDescontoNoMesContratoReceita(BigDecimal descontoNoMesContratoReceita) {
		this.descontoNoMesContratoReceita = descontoNoMesContratoReceita;
	}

	public BigDecimal getDescontoNoMesInclusaoReposicao() {
		if (descontoNoMesInclusaoReposicao == null) {
			descontoNoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return descontoNoMesInclusaoReposicao;
	}

	public void setDescontoNoMesInclusaoReposicao(BigDecimal descontoNoMesInclusaoReposicao) {
		this.descontoNoMesInclusaoReposicao = descontoNoMesInclusaoReposicao;
	}

	public BigDecimal getDescontoNoMesOutros() {
		if (descontoNoMesOutros == null) {
			descontoNoMesOutros = BigDecimal.ZERO;
		}
		return descontoNoMesOutros;
	}

	public void setDescontoNoMesOutros(BigDecimal descontoNoMesOutros) {
		this.descontoNoMesOutros = descontoNoMesOutros;
	}

	public BigDecimal getAcrescimoNoMesMatricula() {
		if (acrescimoNoMesMatricula == null) {
			acrescimoNoMesMatricula = BigDecimal.ZERO;
		}
		return acrescimoNoMesMatricula;
	}

	public void setAcrescimoNoMesMatricula(BigDecimal acrescimoNoMesMatricula) {
		this.acrescimoNoMesMatricula = acrescimoNoMesMatricula;
	}

	public BigDecimal getAcrescimoNoMesMensalidade() {
		if (acrescimoNoMesMensalidade == null) {
			acrescimoNoMesMensalidade = BigDecimal.ZERO;
		}
		return acrescimoNoMesMensalidade;
	}

	public void setAcrescimoNoMesMensalidade(BigDecimal acrescimoNoMesMensalidade) {
		this.acrescimoNoMesMensalidade = acrescimoNoMesMensalidade;
	}

	public BigDecimal getAcrescimoNoMesRequerimento() {
		if (acrescimoNoMesRequerimento == null) {
			acrescimoNoMesRequerimento = BigDecimal.ZERO;
		}
		return acrescimoNoMesRequerimento;
	}

	public void setAcrescimoNoMesRequerimento(BigDecimal acrescimoNoMesRequerimento) {
		this.acrescimoNoMesRequerimento = acrescimoNoMesRequerimento;
	}

	public BigDecimal getAcrescimoNoMesBiblioteca() {
		if (acrescimoNoMesBiblioteca == null) {
			acrescimoNoMesBiblioteca = BigDecimal.ZERO;
		}
		return acrescimoNoMesBiblioteca;
	}

	public void setAcrescimoNoMesBiblioteca(BigDecimal acrescimoNoMesBiblioteca) {
		this.acrescimoNoMesBiblioteca = acrescimoNoMesBiblioteca;
	}

	public BigDecimal getAcrescimoNoMesDevolucaoCheque() {
		if (acrescimoNoMesDevolucaoCheque == null) {
			acrescimoNoMesDevolucaoCheque = BigDecimal.ZERO;
		}
		return acrescimoNoMesDevolucaoCheque;
	}

	public void setAcrescimoNoMesDevolucaoCheque(BigDecimal acrescimoNoMesDevolucaoCheque) {
		this.acrescimoNoMesDevolucaoCheque = acrescimoNoMesDevolucaoCheque;
	}

	public BigDecimal getAcrescimoNoMesNegociacao() {
		if (acrescimoNoMesNegociacao == null) {
			acrescimoNoMesNegociacao = BigDecimal.ZERO;
		}
		return acrescimoNoMesNegociacao;
	}

	public void setAcrescimoNoMesNegociacao(BigDecimal acrescimoNoMesNegociacao) {
		this.acrescimoNoMesNegociacao = acrescimoNoMesNegociacao;
	}

	public BigDecimal getAcrescimoNoMesBolsaCusteada() {
		if (acrescimoNoMesBolsaCusteada == null) {
			acrescimoNoMesBolsaCusteada = BigDecimal.ZERO;
		}
		return acrescimoNoMesBolsaCusteada;
	}

	public void setAcrescimoNoMesBolsaCusteada(BigDecimal acrescimoNoMesBolsaCusteada) {
		this.acrescimoNoMesBolsaCusteada = acrescimoNoMesBolsaCusteada;
	}

	public BigDecimal getAcrescimoNoMesInscricao() {
		if (acrescimoNoMesInscricao == null) {
			acrescimoNoMesInscricao = BigDecimal.ZERO;
		}
		return acrescimoNoMesInscricao;
	}

	public void setAcrescimoNoMesInscricao(BigDecimal acrescimoNoMesInscricao) {
		this.acrescimoNoMesInscricao = acrescimoNoMesInscricao;
	}

	public BigDecimal getAcrescimoNoMesContratoReceita() {
		if (acrescimoNoMesContratoReceita == null) {
			acrescimoNoMesContratoReceita = BigDecimal.ZERO;
		}
		return acrescimoNoMesContratoReceita;
	}

	public void setAcrescimoNoMesContratoReceita(BigDecimal acrescimoNoMesContratoReceita) {
		this.acrescimoNoMesContratoReceita = acrescimoNoMesContratoReceita;
	}

	public BigDecimal getAcrescimoNoMesInclusaoReposicao() {
		if (acrescimoNoMesInclusaoReposicao == null) {
			acrescimoNoMesInclusaoReposicao = BigDecimal.ZERO;
		}
		return acrescimoNoMesInclusaoReposicao;
	}

	public void setAcrescimoNoMesInclusaoReposicao(BigDecimal acrescimoNoMesInclusaoReposicao) {
		this.acrescimoNoMesInclusaoReposicao = acrescimoNoMesInclusaoReposicao;
	}

	public BigDecimal getAcrescimoNoMesOutros() {
		if (acrescimoNoMesOutros == null) {
			acrescimoNoMesOutros = BigDecimal.ZERO;
		}
		return acrescimoNoMesOutros;
	}

	public void setAcrescimoNoMesOutros(BigDecimal acrescimoNoMesOutros) {
		this.acrescimoNoMesOutros = acrescimoNoMesOutros;
	}

	public BigDecimal getDescontoConvenio() {
		if (descontoConvenio == null) {
			descontoConvenio = BigDecimal.ZERO;
		}
		return descontoConvenio;
	}

	public void setDescontoConvenio(BigDecimal descontoConvenio) {
		this.descontoConvenio = descontoConvenio;
	}

	public BigDecimal getDescontoInstituicao() {
		if (descontoInstituicao == null) {
			descontoInstituicao = BigDecimal.ZERO;
		}
		return descontoInstituicao;
	}

	public void setDescontoInstituicao(BigDecimal descontoInstituicao) {
		this.descontoInstituicao = descontoInstituicao;
	}

	public BigDecimal getValorDescontoProgressivo() {
		if (valorDescontoProgressivo == null) {
			valorDescontoProgressivo = BigDecimal.ZERO;
		}
		return valorDescontoProgressivo;
	}

	public void setValorDescontoProgressivo(BigDecimal valorDescontoProgressivo) {
		this.valorDescontoProgressivo = valorDescontoProgressivo;
	}

	public BigDecimal getDescontoAluno() {
		if (descontoAluno == null) {
			descontoAluno = BigDecimal.ZERO;
		}
		return descontoAluno;
	}

	public void setDescontoAluno(BigDecimal descontoAluno) {
		this.descontoAluno = descontoAluno;
	}

	public BigDecimal getDescontoRecebimento() {
		if (descontoRecebimento == null) {
			descontoRecebimento = BigDecimal.ZERO;
		}
		return descontoRecebimento;
	}

	public void setDescontoRecebimento(BigDecimal descontoRecebimento) {
		this.descontoRecebimento = descontoRecebimento;
	}

	public BigDecimal getTotalValorRecebidoContareceber() {
		if (totalValorRecebidoContareceber == null) {
			totalValorRecebidoContareceber = BigDecimal.ZERO;
		}
		return totalValorRecebidoContareceber;
	}

	public void setTotalValorRecebidoContareceber(BigDecimal totalValorRecebidoContareceber) {
		this.totalValorRecebidoContareceber = totalValorRecebidoContareceber;
	}

	public BigDecimal getTaxaInadimplenciaNoMes() {
		if (taxaInadimplenciaNoMes == null) {
			taxaInadimplenciaNoMes = BigDecimal.ZERO;
		}
		return taxaInadimplenciaNoMes;
	}

	public void setTaxaInadimplenciaNoMes(BigDecimal taxaInadimplenciaNoMes) {
		this.taxaInadimplenciaNoMes = taxaInadimplenciaNoMes;
	}

	public BigDecimal getTaxaInadimplenciaNoMesSemAcrescimo() {
		if (taxaInadimplenciaNoMesSemAcrescimo == null) {
			taxaInadimplenciaNoMesSemAcrescimo = BigDecimal.ZERO;
		}
		return taxaInadimplenciaNoMesSemAcrescimo;
	}

	public void setTaxaInadimplenciaNoMesSemAcrescimo(BigDecimal taxaInadimplenciaNoMesSemAcrescimo) {
		this.taxaInadimplenciaNoMesSemAcrescimo = taxaInadimplenciaNoMesSemAcrescimo;
	}

    /**
     * @return the valorInadimplenciaNoMes
     */
    public BigDecimal getValorInadimplenciaNoMes() {
        if (valorInadimplenciaNoMes == null) {
            valorInadimplenciaNoMes = BigDecimal.ZERO;
        }
        return valorInadimplenciaNoMes;
    }

    /**
     * @param valorInadimplenciaNoMes the valorInadimplenciaNoMes to set
     */
    public void setValorInadimplenciaNoMes(BigDecimal valorInadimplenciaNoMes) {
        this.valorInadimplenciaNoMes = valorInadimplenciaNoMes;
    }

	
	public BigDecimal getTotalDescontoRateio() {
		if(totalDescontoRateio == null){
			totalDescontoRateio = BigDecimal.ZERO;
		}
		return totalDescontoRateio;
	}

	public void setTotalDescontoRateio(BigDecimal totalDescontoRateio) {
		this.totalDescontoRateio = totalDescontoRateio;
	}

	public BigDecimal getDescontoRateio() {
		if(descontoRateio == null){
			descontoRateio = BigDecimal.ZERO; 
		}
		return descontoRateio;
	}

	public void setDescontoRateio(BigDecimal descontoRateio) {
		this.descontoRateio = descontoRateio;
	}

	public BigDecimal getTotalDescontoRateioContareceber() {
		if(totalDescontoRateioContareceber == null){
			totalDescontoRateioContareceber =  BigDecimal.ZERO;
		}
		return totalDescontoRateioContareceber;
	}

	public void setTotalDescontoRateioContareceber(BigDecimal totalDescontoRateioContareceber) {
		this.totalDescontoRateioContareceber = totalDescontoRateioContareceber;
	}

	public BigDecimal getTotalValorReceberContareceber() {
		if(totalValorReceberContareceber == null){
			totalValorReceberContareceber = BigDecimal.ZERO; 
		}
		return totalValorReceberContareceber;
	}

	public void setTotalValorReceberContareceber(BigDecimal totalValorReceberContareceber) {
		this.totalValorReceberContareceber = totalValorReceberContareceber;
	} 
	
	
}
