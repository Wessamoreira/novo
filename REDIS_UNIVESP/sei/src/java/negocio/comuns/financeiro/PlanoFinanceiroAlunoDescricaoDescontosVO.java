/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

/**
 * Classe utilizada para criar uma descricao ordenada da aplicaçao dos descontos,
 * seja este desconto do aluno, de um convêncio, progressivo ou plano de desconto
 * institucional.
 * @author Edigar
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.utilitarias.Uteis;

public class PlanoFinanceiroAlunoDescricaoDescontosVO implements Serializable, Comparable<PlanoFinanceiroAlunoDescricaoDescontosVO>, Cloneable {

    public static String TIPODESCONTOPROGRESSIVO = "DescontoProgressivo";
    public static String TIPOPLANODESCONTOINSTITUCIONAL = "PlanoDesconto";
    public static String TIPODESCONTOPADRAO = "Padrao";
    public static Integer NRDIASCOMECARACONTARVALIDADEPLANODESCONTO = 2000;
    private Integer diaNrAntesVencimento = 0;
    private Integer diaNrAntesVencimentoDescontoComecaASerAplicado = 0;
    private Date dataLimiteAplicacaoDesconto;
    private Date dataInicioAplicacaoDesconto;
    private Double valorBase = 0.0;
    private Double valorBaseComDescontosJaCalculadosAplicados = 0.0;
    private Double valorDescontoAluno = 0.0;
    private Double valorDescontoConvenio = 0.0;
    private Double valorDescontoProgressivo = 0.0;
    private Double valorDescontoRateio = 0.0;
    private Double valorDescontoInstituicao = 0.0;
    private Double valorCusteadoContaReceber = 0.0;
    private Double valorParaPagamentoDentroDataLimiteDesconto = 0.0;
    private Double valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento = 0.0;
    private String tipoOrigemDesconto = "";
    private DescontoProgressivoVO descontoProgressivoVO;
    private Double percentualDescontoProgressivo = 0.0;
    private String indicadorDescontoProgressivo = "";
    private PlanoDescontoVO planoDescontoVO;
    private Double valorFixoDescontoProgressivo = 0.0;
    //Atibuto criado para controle dos alunos com Desconto 100%
    private Boolean descontoCemPorCento;
    private Boolean apresentarDescricaoBoleto;
    private HashMap<Integer, Double> listaDescontosConvenio;
    private HashMap<Integer, Double> listaDescontosPlanoDesconto;
    private Boolean vencimentoDescontoProgressivoDiaUtil;
    private List<FeriadoVO> listaFeriadoVOs;
    private Integer codigoContaReceber;
    /**
     * Atributo criado por Edigar na versão 5.0.3. O mesmo foi criado para implementar
     * um controle novo de forma que na descricao dos descontos da Conta a Receber/Boleto, também seja apresentado uma
     * descrição de descontos válidos após o vencimento do parcela do aluno. Antes desta versão
     * o SEI só listavas os descontos válidos até a data de vencimento. Assim, caso o aluno tenha
     * dois descontos, um válido até a data de vencimento e outro não, no boleto não ficava claro
     * esta situação. Com este método, verificamos se há um desconto que vence na data de vencimento,
     * caso exista, então geramos a descrição de pagamento após o vencimento. Ficando claro para quem
     * visualiza a conta a receber / boletos, qual o valor a ser pago após o vencimento.
     * ContaReceber.executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno
     */
    private Boolean referentePlanoFinanceiroAposVcto;
    /**
     * Atributo criado por Rodrigo na versão 5.0.3.5 O mesmo foi criado para definir que este 
     * desconto é valido até o vencimento da conta ou seja deve ser atribuido na descrição dos desconto
     * o texto Até, porém este foi criado para ser utilizado na apresentação dos dados na tela de matricula, 
     * renovação de matricula por turma e matricula online
     * 
     * Matricula.calcularTotalDesconto
     */
    private Boolean referentePlanoFinanceiroAteVencimento;
    
    /**
	 * Atributos utilizados apenas para remessa
	 */
	private Double valorDescontoComValidade;
	private Double valorDescontoSemValidade;
	private Double acrescimo;
	
    public static final long serialVersionUID = 1L;

    /**
     * @return the diaNrAntesVencimento
     */
    public Integer getDiaNrAntesVencimento() {
        return diaNrAntesVencimento;
    }

    public Double executarCalculoValorParaPagamentoDentroDataLimiteDesconto() {
        Double valorFinal = valorBase - valorDescontoAluno - valorDescontoConvenio
                - valorDescontoProgressivo - valorDescontoInstituicao - valorCusteadoContaReceber - valorDescontoRateio;
        return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
    }

    public Double executarCalculoValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(Double valorBaseContaAgrupada) {
    	Double valorFinal = valorBaseContaAgrupada - valorDescontoAluno - valorDescontoConvenio
    			- valorDescontoProgressivo - valorDescontoInstituicao - valorCusteadoContaReceber - valorDescontoRateio;
    	return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
    }
    
    public Double executarCalculoValorParaPagamentoDentroDataLimiteDescontoCemPorCento() {
        Double valorFinal = valorBase - valorDescontoAluno - valorDescontoConvenio
                - valorDescontoProgressivo - valorDescontoInstituicao - valorCusteadoContaReceber - valorDescontoRateio;
        return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
    }

    public Double executarCalculoValorParaPagamentoDentroDataLimiteDescontoCemPorCentoContaAgrupada(Double valorBaseContaAgrupada) {
    	Double valorFinal = valorBaseContaAgrupada;
    	return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
    }
    
    public Double executarCalculoValorTotalDesconto() {
        Double valorFinal = valorDescontoAluno + valorDescontoConvenio + valorDescontoProgressivo + valorDescontoInstituicao  + valorDescontoRateio; 
        return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
    }

    public Double executarCalculoValorTotalDescontoCemPorCento() {
        Double valorFinal = valorParaPagamentoDentroDataLimiteDesconto;
        return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
    }

    /**
     * @param diaNrAntesVencimento the diaNrAntesVencimento to set
     */
    public void setDiaNrAntesVencimento(Integer diaNrAntesVencimento) {
        this.diaNrAntesVencimento = diaNrAntesVencimento;
    }

    /**
     * @return the tipoOrigemDesconto
     */
    public String getTipoOrigemDesconto() {
        return tipoOrigemDesconto;
    }

    /**
     * @param tipoOrigemDesconto the tipoOrigemDesconto to set
     */
    public void setTipoOrigemDesconto(String tipoOrigemDesconto) {
        this.tipoOrigemDesconto = tipoOrigemDesconto;
    }

    /**
     * @return the descontoProgressivoVO
     */
    public DescontoProgressivoVO getDescontoProgressivoVO() {
        return descontoProgressivoVO;
    }

    /**
     * @param descontoProgressivoVO the descontoProgressivoVO to set
     */
    public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
        this.descontoProgressivoVO = descontoProgressivoVO;
    }

    /**
     * @return the indicadorDescontoProgressivo
     */
    public String getIndicadorDescontoProgressivo() {
        return indicadorDescontoProgressivo;
    }

    /**
     * @param indicadorDescontoProgressivo the indicadorDescontoProgressivo to set
     */
    public void setIndicadorDescontoProgressivo(String indicadorDescontoProgressivo) {
        this.indicadorDescontoProgressivo = indicadorDescontoProgressivo;
    }

    /**
     * @return the planoDescontoVO
     */
    public PlanoDescontoVO getPlanoDescontoVO() {
        if (planoDescontoVO == null) {
            planoDescontoVO = new PlanoDescontoVO();
        }
        return planoDescontoVO;
    }

    /**
     * @param planoDescontoVO the planoDescontoVO to set
     */
    public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
        this.planoDescontoVO = planoDescontoVO;
    }

    public String getDataLimiteAplicacaoDesconto_Apresentar() {
        if (this.getReferentePlanoFinanceiroAposVcto() || (!getApresentarNrDiasAntesVcto() && !getReferentePlanoFinanceiroAteVencimento() && (getDiaNrAntesVencimento() == null || getDiaNrAntesVencimento().equals(0)) )) {
            // adicionar por Edigar na versao 5.0.3 em 02/12/14.
            // Ista variável controlar um plano que demonstra quanto o aluno irá 
            // pagar após o vcto, quando parte dos descontos é perdida no vcto
            // e outros descontos são mantidos após o vcto.
            return "Após Vcto";
        } 
        String dataStr = "Até " + Uteis.getData(dataLimiteAplicacaoDesconto);
        if (!this.getApresentarNrDiasAntesVcto()) {
            dataInicioAplicacaoDesconto = Uteis.obterDataPassada(dataInicioAplicacaoDesconto, 1);
            if (getVencimentoDescontoProgressivoDiaUtil()) {
            	Date data = Uteis.getNextWorkingDay(dataInicioAplicacaoDesconto, getListaFeriadoVOs());
            	dataStr = "Após " + Uteis.getData(data);
            } else {
            	dataStr = "Após " + Uteis.getData(dataInicioAplicacaoDesconto);
            }
            return dataStr;
        }
        if(dataLimiteAplicacaoDesconto == null){
			dataStr = "";
		}
        return dataStr;
    }
    
    public String getDataLimiteAplicacaoDescontoContaAgrupada_Apresentar() {
    	return Uteis.getData(dataLimiteAplicacaoDesconto);
    }

    /**
     * @return the dataLimiteAplicacaoDesconto
     */
    public Date getDataLimiteAplicacaoDesconto() {
        return dataLimiteAplicacaoDesconto;
    }

    /**
     * @param dataLimiteAplicacaoDesconto the dataLimiteAplicacaoDesconto to set
     */
    public void setDataLimiteAplicacaoDesconto(Date dataLimiteAplicacaoDesconto) {
        this.dataLimiteAplicacaoDesconto = dataLimiteAplicacaoDesconto;
    }

    /**
     * @return the valorParaPagamentoDentroDataLimiteDesconto
     */
    public Double getValorParaPagamentoDentroDataLimiteDesconto(Boolean matricula) {
        if (validarDadosDescontoCemPorcento(matricula)) {
            valorParaPagamentoDentroDataLimiteDesconto = executarCalculoValorParaPagamentoDentroDataLimiteDescontoCemPorCento();
        } else {
            valorParaPagamentoDentroDataLimiteDesconto = executarCalculoValorParaPagamentoDentroDataLimiteDesconto();
        }
        return valorParaPagamentoDentroDataLimiteDesconto;
    }

    public Double getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(Boolean matricula, Double valorBase) {
    	if (validarDadosDescontoCemPorcento(matricula)) {
    		valorParaPagamentoDentroDataLimiteDesconto = executarCalculoValorParaPagamentoDentroDataLimiteDescontoCemPorCentoContaAgrupada(valorBase);
    	} else {
    		valorParaPagamentoDentroDataLimiteDesconto = executarCalculoValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(valorBase);
    	}
    	return valorParaPagamentoDentroDataLimiteDesconto;
    }
    
    /**
     * @param valorParaPagamentoDentroDataLimiteDesconto the valorParaPagamentoDentroDataLimiteDesconto to set
     */
    public void setValorParaPagamentoDentroDataLimiteDesconto(Double valorParaPagamentoDentroDataLimiteDesconto) {
        this.valorParaPagamentoDentroDataLimiteDesconto = valorParaPagamentoDentroDataLimiteDesconto;
    }

    /**
     * @return the valorDescontoAluno
     */
    public Double getValorDescontoAluno() {
        return valorDescontoAluno;
    }

    /**
     * @param valorDescontoAluno the valorDescontoAluno to set
     */
    public void setValorDescontoAluno(Double valorDescontoAluno) {
        this.valorDescontoAluno = valorDescontoAluno;
    }

    /**
     * @return the valorDescontoConvenio
     */
    public Double getValorDescontoConvenio() {
        return valorDescontoConvenio;
    }

    /**
     * @param valorDescontoConvenio the valorDescontoConvenio to set
     */
    public void setValorDescontoConvenio(Double valorDescontoConvenio) {
        this.valorDescontoConvenio = valorDescontoConvenio;
    }

    /**
     * @return the valorBase
     */
    public Double getValorBase() {
        if (valorBase == null) {
            valorBase = 0.0;
        }
        return valorBase;
    }

    /**
     * @param valorBase the valorBase to set
     */
    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
    }

    /**
     * @return the valorDescontoProgressivo
     */
    public Double getValorDescontoProgressivo() {
    	 if (valorDescontoProgressivo == null) {
    		 valorDescontoProgressivo = 0.0;
         }
        return valorDescontoProgressivo;
    }

    /**
     * @param valorDescontoProgressivo the valorDescontoProgressivo to set
     */
    public void setValorDescontoProgressivo(Double valorDescontoProgressivo) {
        this.valorDescontoProgressivo = valorDescontoProgressivo;
    }

    /**
     * @return the valorDescontoInstituicao
     */
    public Double getValorDescontoInstituicao() {
        return valorDescontoInstituicao;
    }

    /**
     * @param valorDescontoInstituicao the valorDescontoInstituicao to set
     */
    public void setValorDescontoInstituicao(Double valorDescontoInstituicao) {
        this.valorDescontoInstituicao = valorDescontoInstituicao;
    }

    public String getDiaNrAntesVencimentoParaOrdenacao() {
        String nrDiaStr = String.valueOf(diaNrAntesVencimento);
        if (nrDiaStr.length() == 1) {
            nrDiaStr = "0" + nrDiaStr;
        }
        return nrDiaStr;
    }

    /*
     * @return the diaNrAntesVencimentoDescontoComecaASerAplicado
     */
    public Integer getDiaNrAntesVencimentoDescontoComecaASerAplicado() {
        return diaNrAntesVencimentoDescontoComecaASerAplicado;
    }

    /**
     * @param diaNrAntesVencimentoDescontoComecaASerAplicado the diaNrAntesVencimentoDescontoComecaASerAplicado to set
     */
    public void setDiaNrAntesVencimentoDescontoComecaASerAplicado(Integer diaNrAntesVencimentoDescontoComecaASerAplicado) {
        this.diaNrAntesVencimentoDescontoComecaASerAplicado = diaNrAntesVencimentoDescontoComecaASerAplicado;
    }

    public boolean getIsAplicavelDia(Integer diaUnificarValorDescontosAplicaveis) {
        if (this.getTipoOrigemDesconto().equals(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO)) {
            return true;
        }
        if (this.getTipoOrigemDesconto().equals(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPROGRESSIVO)) {
            if ((diaUnificarValorDescontosAplicaveis.compareTo(this.getDiaNrAntesVencimentoDescontoComecaASerAplicado()) <= 0)
                    && (diaUnificarValorDescontosAplicaveis.compareTo(this.getDiaNrAntesVencimento()) >= 0)) {
                return true;
            }
        }
        if (this.getTipoOrigemDesconto().equals(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPOPLANODESCONTOINSTITUCIONAL)) {
            if ((this.getDiaNrAntesVencimento() == null) || (this.getDiaNrAntesVencimento().equals(0))) {
                // caso o nr dias antes vcto esteja em branco para o plano de desconto, significa que o mesmo
                // sempre é aplicado. Por isto retornamos true...
                return true;
            }
            if (diaUnificarValorDescontosAplicaveis.compareTo(this.getDiaNrAntesVencimento()) >= 0) {
                return true;
            }
        }
        return false;
    }

//    public Date obterDataInicioAplicacaoPlano(Date dataVencimento) throws Exception {
//        if (this.getDiaNrAntesVencimentoDescontoComecaASerAplicado().equals(Integer.MAX_VALUE)) {
//            return Uteis.getDate("01/01/2000");
//        }
//        return Uteis.obterDataPassada(dataVencimento, this.getDiaNrAntesVencimentoDescontoComecaASerAplicado());
//    }
//
//    public Date obterDataLimiteAplicacaoPlano(Date dataVencimento) throws Exception {
//        return Uteis.obterDataPassada(dataVencimento, this.getDiaNrAntesVencimentoDescontoComecaASerAplicado());
//    }
    public boolean getIsPlanoDescontoPadrao() {
        if (this.getTipoOrigemDesconto().equals(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO)) {
            return true;
        }
        return false;
    }

    public boolean getIsAplicavelDataParaQuitacao(Date dataVencimento, Date dataReferenciaQuitacao) throws Exception {
        Date dataInicioAplicacaoPlano = this.getDataInicioAplicacaoDesconto();//obterDataInicioAplicacaoPlano(dataVencimento);
        Date dataLimiteAplicacaoPlano = this.getDataLimiteAplicacaoDesconto();//obterDataLimiteAplicacaoPlano(dataVencimento);
        boolean descontoSempreValido = false;
        if(!getReferentePlanoFinanceiroAteVencimento() && getDiaNrAntesVencimento().equals(0)) {
			if(getValorDescontoProgressivo().equals(0.0)) {
				descontoSempreValido = true;
			}			
		}
        if ((((Uteis.getData(dataReferenciaQuitacao, "dd/MM/yyyy").equals(Uteis.getData(dataInicioAplicacaoPlano, "dd/MM/yyyy")))
                || dataReferenciaQuitacao.after(dataInicioAplicacaoPlano))
                && ((Uteis.getData(dataReferenciaQuitacao, "dd/MM/yyyy").equals(Uteis.getData(dataLimiteAplicacaoPlano, "dd/MM/yyyy")))
                || dataReferenciaQuitacao.before(dataLimiteAplicacaoPlano))) || descontoSempreValido) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String planoDesc = "";
        if (this.getTipoOrigemDesconto().equals("PlanoDesconto")) {
            planoDesc = " - Plano Desconto: " + this.getPlanoDescontoVO().getNome();
        }
        return getDescricaoResumida() + planoDesc;
    }

    public String getDescricaoDetalhadaComDataLimites() {
		StringBuilder strResumida = new StringBuilder();
		valorParaPagamentoDentroDataLimiteDesconto = Uteis.arrendondarForcando2CadasDecimais(executarCalculoValorParaPagamentoDentroDataLimiteDesconto()+getAcrescimo());
		Double valorTotalDesconto = executarCalculoValorTotalDesconto();
		if (valorBase != null && (valorTotalDesconto.equals(valorBase) || valorTotalDesconto > valorBase)) {
			valorParaPagamentoDentroDataLimiteDesconto = 0.0;
		}
		if (this.getReferentePlanoFinanceiroAposVcto()) {
			strResumida.append("Após Vencimento Desconto de R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
			if(Uteis.isAtributoPreenchido(getAcrescimo())){
				strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
			}
			strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
		} else {
			if (!this.getApresentarNrDiasAntesVcto()) {
				strResumida.append("Desconto de R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" (").append(this.getDiaNrAntesVencimento()).append(")").append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");				
				if(Uteis.isAtributoPreenchido(getAcrescimo())){
					strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
				}
				strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
			} else {
				boolean apresentarDataInicio = true;
				int anoLimite = Uteis.getAnoData(this.getDataLimiteAplicacaoDesconto());
				int anoInicio = Uteis.getAnoData(dataInicioAplicacaoDesconto);
				if (anoInicio < anoLimite) {
					// se a data de início de aplicacao é muito anterior ao
					// vecto do desconto
					// nao justifica apresentá-lo na descrição. Geralmente
					// trata-se de desconto progressivo,
					// que pode ser simplesmente apresentado até quando o mesmo
					// irá valer.
					apresentarDataInicio = false;
				}
				if (apresentarDataInicio) {
					strResumida.append("De ").append(Uteis.getData(dataInicioAplicacaoDesconto)).append(" até ").append(Uteis.getData(this.getDataLimiteAplicacaoDesconto())).append(" (").append(this.getDiaNrAntesVencimento()).append(")").append(" Desconto R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
					if(Uteis.isAtributoPreenchido(getAcrescimo())){
						strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
					}
					strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
				} else {
					strResumida.append("Até ").append(Uteis.getData(this.getDataLimiteAplicacaoDesconto())).append(" (").append(this.getDiaNrAntesVencimento()).append(")").append(" Desconto R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
					if(Uteis.isAtributoPreenchido(getAcrescimo())){
						strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
					}
					strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
				}
			}
		}
		return strResumida.toString();
	}

	public String getDescricaoDetalhada() {
		StringBuilder strResumida = new StringBuilder();
		valorParaPagamentoDentroDataLimiteDesconto = Uteis.arrendondarForcando2CadasDecimais(executarCalculoValorParaPagamentoDentroDataLimiteDesconto()+getAcrescimo());
		Double valorTotalDesconto = executarCalculoValorTotalDesconto();
		if (valorBase != null && (valorTotalDesconto.equals(valorBase) || valorTotalDesconto > valorBase)) {
			valorParaPagamentoDentroDataLimiteDesconto = 0.0;
		}
		if (this.getReferentePlanoFinanceiroAposVcto()) {
			strResumida.append("Após Vencimento ").append(" - Valor Total Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
			if(Uteis.isAtributoPreenchido(getAcrescimo())){
				strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
			}
			strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
		} else if (this.getReferentePlanoFinanceiroAteVencimento()) {
			strResumida.append("Até Vencimento ").append(" - Valor Total Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
			if(Uteis.isAtributoPreenchido(getAcrescimo())){
				strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
			}
			strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
		} else {
			if (!this.getApresentarNrDiasAntesVcto()) {
				strResumida.append("Valor Total Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
				if(Uteis.isAtributoPreenchido(getAcrescimo())){
					strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
				}
				strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
			} else {
				strResumida.append("Até ").append(getDiaNrAntesVencimentoParaOrdenacao()).append(" Dias Antes - Valor Total Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno)).append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio)).append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao)).append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo)).append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio)).append("]");
				if(Uteis.isAtributoPreenchido(getAcrescimo())){
					strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
				}
				strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
			}
		}
		return strResumida.toString();
	}

    public Boolean getApresentarNrDiasAntesVcto() {
        if (((getTipoOrigemDesconto().equals(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO))
                || (getDiaNrAntesVencimento().equals(0) && getValorDescontoProgressivo().equals(0.0))
                || (getDiaNrAntesVencimento().equals(0) && getValorDescontoInstituicao() > 0 && getPlanoDescontoVO().getDescontoValidoAteDataVencimento()))
                && !getReferentePlanoFinanceiroAteVencimento()
                // Rodrigo comentou, para que a descricao do desconto progressivo no boleto com o nr de dias antes do vencimento = 0 apresente o texto até (Realizei pesquisa de impactos com Lucas e Carlos e não obtive nenhuma objeção) 
                ) {
            return false;
        } else {
            return true;
        }
    }

    public String getDescricaoResumida() {
		StringBuilder strResumida = new StringBuilder();
		Double valorTotalDesconto = 0.0;
		// if (getDescontoCemPorCento()) {
		// valorParaPagamentoDentroDataLimiteDesconto =
		// executarCalculoValorParaPagamentoDentroDataLimiteDescontoCemPorCento();
		// valorTotalDesconto = executarCalculoValorTotalDescontoCemPorCento();
		// if (valorBase != null && (valorTotalDesconto.equals(valorBase) ||
		// valorTotalDesconto > valorBase)) {
		// valorParaPagamentoDentroDataLimiteDesconto = 0.0;
		// }
		// } else {
		// COMENTAO POR EDIGAR - VERSAO 5.0 - POIS ESTAVA DANDO PROBLEMA QUANDO
		// O DESCONTO DE CONVENIO ERA DE 100% DO VALOR DA PARCELA
		// }
		valorParaPagamentoDentroDataLimiteDesconto = Uteis.arrendondarForcando2CadasDecimais(executarCalculoValorParaPagamentoDentroDataLimiteDesconto()+getAcrescimo());
		valorTotalDesconto = executarCalculoValorTotalDesconto();
		if (valorBase != null && (valorTotalDesconto.equals(valorBase) || valorTotalDesconto > valorBase)) {
			valorParaPagamentoDentroDataLimiteDesconto = 0.0;
		}
		if (this.getReferentePlanoFinanceiroAposVcto()) {
			strResumida.append("Após Vencimento ").append(" - Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto));
			if(Uteis.isAtributoPreenchido(getAcrescimo())){
				strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
			}
			strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
		} else if (this.getReferentePlanoFinanceiroAteVencimento()) {
			strResumida.append("Até Vencimento ").append(" - Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto));
			if(Uteis.isAtributoPreenchido(getAcrescimo())){
				strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
			}
			strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
		} else {
			if (!this.getApresentarNrDiasAntesVcto()) {
				strResumida.append("Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto));
				if(Uteis.isAtributoPreenchido(getAcrescimo())){
					strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
				}
				strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
			} else {
				String textoInicial = "Até " + getDiaNrAntesVencimentoParaOrdenacao() + " Dias Antes";
				if (getDiaNrAntesVencimentoParaOrdenacao().equals("0") || getDiaNrAntesVencimentoParaOrdenacao().equals("00")) {
					textoInicial = "Até Vencimento";
				}
				strResumida.append(textoInicial).append(" - Desconto: ").append(Uteis.getDoubleFormatado(valorTotalDesconto));
				if(Uteis.isAtributoPreenchido(getAcrescimo())){
					strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
				}
				strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
			}
		}
		return strResumida.toString();
	}

    /**
     * @return the percentualDescontoProgressivo
     */
    public Double getPercentualDescontoProgressivo() {
        if (percentualDescontoProgressivo == null) {
            percentualDescontoProgressivo = 0.0;
        }
        return percentualDescontoProgressivo;
    }

    /**
     * @param percentualDescontoProgressivo the percentualDescontoProgressivo to set
     */
    public void setPercentualDescontoProgressivo(Double percentualDescontoProgressivo) {
        this.percentualDescontoProgressivo = percentualDescontoProgressivo;
    }

    /**
     * @return the valorBaseComDescontosJaCalculadosAplicados
     */
    public Double getValorBaseComDescontosJaCalculadosAplicados() {
        return valorBaseComDescontosJaCalculadosAplicados;
    }

    /**
     * @param valorBaseComDescontosJaCalculadosAplicados the valorBaseComDescontosJaCalculadosAplicados to set
     */
    public void setValorBaseComDescontosJaCalculadosAplicados(Double valorBaseComDescontosJaCalculadosAplicados) {
        this.valorBaseComDescontosJaCalculadosAplicados = valorBaseComDescontosJaCalculadosAplicados;
    }

    public static Double obterValorPadraoDescontoConvenio(List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroDescricao) {
        PlanoFinanceiroAlunoDescricaoDescontosVO planoPadrao = obterPlanoFinanceiroDescricaoDescontosPadrao(listaPlanoFinanceiroDescricao);
        if (planoPadrao != null) {
            return planoPadrao.getValorDescontoConvenio();
        }
        return 0.0;
    }

    public static PlanoFinanceiroAlunoDescricaoDescontosVO obterPlanoFinanceiroDescricaoDescontosPadrao(
            List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroDescricao) {
    	if (!listaPlanoFinanceiroDescricao.isEmpty()) {    		
    		PlanoFinanceiroAlunoDescricaoDescontosVO obj = listaPlanoFinanceiroDescricao.get(listaPlanoFinanceiroDescricao.size()-1);
            if (obj.getDiaNrAntesVencimento().equals(0)) {
                return obj;
            } else {
            	return null;
            }
    	}
        return null;
    }

    /**
     * @return the dataInicioAplicacaoDesconto
     */
    public Date getDataInicioAplicacaoDesconto() {
        return dataInicioAplicacaoDesconto;
    }

    /**
     * @param dataInicioAplicacaoDesconto the dataInicioAplicacaoDesconto to set
     */
    public void setDataInicioAplicacaoDesconto(Date dataInicioAplicacaoDesconto) {
        this.dataInicioAplicacaoDesconto = dataInicioAplicacaoDesconto;
    }
    // TODO Alberto 08/12/10 acrescentado valor fixo desconto progressivo

    public Double getValorFixoDescontoProgressivo() {
        if (valorFixoDescontoProgressivo == null) {
            valorFixoDescontoProgressivo = 0.0;
        }
        return valorFixoDescontoProgressivo;
    }

    public void setValorFixoDescontoProgressivo(Double valorFixoDescontoProgressivo) {
        this.valorFixoDescontoProgressivo = valorFixoDescontoProgressivo;
    }

    /**
     * @return the valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento
     */
    public Double getValorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento() {
        if (valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento == null) {
            valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento = new Double(0.0);
        }
        return valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento;
    }

    /**
     * @param valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento the valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento to set
     */
    public void setValorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento(Double valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento) {
        this.valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento = valorParaPagamentoDentroDataLimiteDescontoAlunosDescontoCemPorCento;
    }
    // TODO Alberto 08/12/10 acrescentado valor fixo desconto progressivo

    public Boolean validarDadosDescontoCemPorcento(Boolean matricula) {
        Double valorFinal = 0.0;
        if (matricula) {
            valorFinal = getPlanoDescontoVO().getPercDescontoMatricula() / 100;
        } else {
            valorFinal = getPlanoDescontoVO().getPercDescontoParcela() / 100;
        }

        if (valorFinal == 1) {
            return true;
        }
        return false;
    }

    /**
     * @return the descontoCemPorCento
     */
    public Boolean getDescontoCemPorCento() {
        if (descontoCemPorCento == null) {
            descontoCemPorCento = Boolean.FALSE;
        }
        return descontoCemPorCento;
    }

    /**
     * @param descontoCemPorCento the descontoCemPorCento to set
     */
    public void setDescontoCemPorCento(Boolean descontoCemPorCento) {
        this.descontoCemPorCento = descontoCemPorCento;
    }

    public HashMap<Integer, Double> getListaDescontosConvenio() {
        if (listaDescontosConvenio == null) {
            listaDescontosConvenio = new HashMap<Integer, Double>();
        }
        return listaDescontosConvenio;
    }

    public void setListaDescontosConvenio(HashMap<Integer, Double> listaDescontosConvenio) {
        this.listaDescontosConvenio = listaDescontosConvenio;
    }

    public HashMap<Integer, Double> getListaDescontosPlanoDesconto() {
        if (listaDescontosPlanoDesconto == null) {
            listaDescontosPlanoDesconto = new HashMap<Integer, Double>();
        }
        return listaDescontosPlanoDesconto;
    }

    public void setListaDescontosPlanoDesconto(HashMap<Integer, Double> listaDescontosPlanoDesconto) {
        this.listaDescontosPlanoDesconto = listaDescontosPlanoDesconto;
    }

    public Boolean getApresentarDescricaoBoleto() {
        if (apresentarDescricaoBoleto == null) {
            apresentarDescricaoBoleto = Boolean.FALSE;
        }
        return apresentarDescricaoBoleto;
    }

    public void setApresentarDescricaoBoleto(Boolean apresentarDescricaoBoleto) {
        this.apresentarDescricaoBoleto = apresentarDescricaoBoleto;
    }

	public Boolean getVencimentoDescontoProgressivoDiaUtil() {
		if (vencimentoDescontoProgressivoDiaUtil == null) {
			vencimentoDescontoProgressivoDiaUtil = Boolean.FALSE;
		}
		return vencimentoDescontoProgressivoDiaUtil;
	}

	public void setVencimentoDescontoProgressivoDiaUtil(
			Boolean vencimentoDescontoProgressivoDiaUtil) {
		this.vencimentoDescontoProgressivoDiaUtil = vencimentoDescontoProgressivoDiaUtil;
	}

	public List<FeriadoVO> getListaFeriadoVOs() {
		if (listaFeriadoVOs == null) {
			listaFeriadoVOs = new ArrayList<FeriadoVO>(0);
		}
		return listaFeriadoVOs;
	}

	public void setListaFeriadoVOs(List<FeriadoVO> listaFeriadoVOs) {
		this.listaFeriadoVOs = listaFeriadoVOs;
	}

	public Integer getCodigoContaReceber() {
		if (codigoContaReceber == null) {
			codigoContaReceber = 0;
		}
		return codigoContaReceber;
	}

	public void setCodigoContaReceber(Integer codigoContaReceber) {
		this.codigoContaReceber = codigoContaReceber;
	}

    /**
     * @return the valorCusteadoContaReceber
     */
    public Double getValorCusteadoContaReceber() {
        if (valorCusteadoContaReceber == null) {
            valorCusteadoContaReceber = 0.0;
        }
        return valorCusteadoContaReceber;
    }

    /**
     * @param valorCusteadoContaReceber the valorCusteadoContaReceber to set
     */
    public void setValorCusteadoContaReceber(Double valorCusteadoContaReceber) {
        this.valorCusteadoContaReceber = valorCusteadoContaReceber;
    }
    
    public boolean getIsConvenio() {
        return (this.getTipoOrigemDesconto().equals("PlanoDesconto"));
    }

    public int compareTo(PlanoFinanceiroAlunoDescricaoDescontosVO o) {
        Integer ordemPrioridadeAplicacaoThis = Integer.MAX_VALUE;
        if (this.getTipoOrigemDesconto().equals("PlanoDesconto")) {
            ordemPrioridadeAplicacaoThis = this.getPlanoDescontoVO().getOrdemPrioridadeParaCalculo();
        } 
        Integer ordemPrioridadeObjetoComparacao = Integer.MAX_VALUE;
        if (this.getTipoOrigemDesconto().equals("PlanoDesconto")) {
            ordemPrioridadeObjetoComparacao = o.getPlanoDescontoVO().getOrdemPrioridadeParaCalculo();
        }
        return ordemPrioridadeAplicacaoThis.compareTo(ordemPrioridadeObjetoComparacao);
    }
    
    public String getValorCusteadoContaReceber_Apresentar() {
        return Uteis.formatarDecimalDuasCasas(this.getValorCusteadoContaReceber());
    }

    /**
     * @return the referentePlanoFinanceiroAposVcto
     */
    public Boolean getReferentePlanoFinanceiroAposVcto() {
        if (referentePlanoFinanceiroAposVcto == null) { 
            referentePlanoFinanceiroAposVcto = Boolean.FALSE;
        }
        return referentePlanoFinanceiroAposVcto;
    }

    /**
     * @param referentePlanoFinanceiroAposVcto the referentePlanoFinanceiroAposVcto to set
     */
    public void setReferentePlanoFinanceiroAposVcto(Boolean referentePlanoFinanceiroAposVcto) {
        this.referentePlanoFinanceiroAposVcto = referentePlanoFinanceiroAposVcto;
    }

	public Boolean getReferentePlanoFinanceiroAteVencimento() {
		if(referentePlanoFinanceiroAteVencimento == null){
			referentePlanoFinanceiroAteVencimento = false;
		}
		return referentePlanoFinanceiroAteVencimento;
	}

	public void setReferentePlanoFinanceiroAteVencimento(Boolean referentePlanoFinanceiroAteVencimento) {
		this.referentePlanoFinanceiroAteVencimento = referentePlanoFinanceiroAteVencimento;
	}
    

	@Override
	public PlanoFinanceiroAlunoDescricaoDescontosVO clone() throws CloneNotSupportedException {
		return (PlanoFinanceiroAlunoDescricaoDescontosVO) super.clone();
	}

	public Double getValorDescontoComValidade() {
		if (valorDescontoComValidade == null) {
			valorDescontoComValidade = 0.0;
		}
		return valorDescontoComValidade;
	}

	public void setValorDescontoComValidade(Double valorDescontoComValidade) {
		this.valorDescontoComValidade = valorDescontoComValidade;
	}

	public Double getValorDescontoSemValidade() {
		if (valorDescontoSemValidade == null) {
			valorDescontoSemValidade = 0.0;
		}
		return valorDescontoSemValidade;
	}

	public void setValorDescontoSemValidade(Double valorDescontoSemValidade) {
		this.valorDescontoSemValidade = valorDescontoSemValidade;
	}
	

	public Double getValorDescontoRateio() {
		if(valorDescontoRateio == null){
			valorDescontoRateio = 0.0;
		}
		return valorDescontoRateio;
	}

	public void setValorDescontoRateio(Double valorDescontoRateio) {
		this.valorDescontoRateio = valorDescontoRateio;
	}
	

	public String getDescricaoDetalhadaComDataLimitesEDescontosValidos() {
		StringBuilder strResumida = new StringBuilder();
		valorParaPagamentoDentroDataLimiteDesconto = Uteis.arrendondarForcando2CadasDecimais(executarCalculoValorParaPagamentoDentroDataLimiteDesconto()+ getAcrescimo());
		Double valorTotalDesconto = executarCalculoValorTotalDesconto();
		if (valorBase != null && (valorTotalDesconto.equals(valorBase) || valorTotalDesconto > valorBase)) {
			valorParaPagamentoDentroDataLimiteDesconto = 0.0;
		}
		StringBuilder strDescontos = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(this.valorDescontoAluno)){
			strDescontos.append("Aluno: ").append(Uteis.getDoubleFormatado(this.valorDescontoAluno));
		}
		if(Uteis.isAtributoPreenchido(this.valorDescontoConvenio)){
			strDescontos.append(" Conv: ").append(Uteis.getDoubleFormatado(this.valorDescontoConvenio));	
		}
		if(Uteis.isAtributoPreenchido(this.valorDescontoInstituicao)){
			strDescontos.append(" Inst: ").append(Uteis.getDoubleFormatado(this.valorDescontoInstituicao));
		}
		if(Uteis.isAtributoPreenchido(this.valorDescontoProgressivo)){
			if(Uteis.isAtributoPreenchido(percentualDescontoProgressivo)){
				strDescontos.append(" Prog(%").append(Uteis.getDoubleFormatado(this.percentualDescontoProgressivo)).append("): ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo));
			}else{
				strDescontos.append(" Prog: ").append(Uteis.getDoubleFormatado(this.valorDescontoProgressivo));
			}
		}
		if(Uteis.isAtributoPreenchido(this.valorDescontoRateio)){
			strDescontos.append(" RAT: ").append(Uteis.getDoubleFormatado(this.valorDescontoRateio));
		}
		if (this.getReferentePlanoFinanceiroAposVcto()) {
			strResumida.append("Após Vencimento Desconto de R$").append(Uteis.getDoubleFormatado(valorTotalDesconto));
			if(valorTotalDesconto > 0){
				strResumida.append(" [Descontos=").append(strDescontos).append("]");
			}
			strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
		} else {
			if (!this.getApresentarNrDiasAntesVcto()) {
				strResumida.append("Desconto de R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" (").append(this.getDiaNrAntesVencimento()).append(")").append(" [Descontos=").append(strDescontos).append("]");
				if(Uteis.isAtributoPreenchido(getAcrescimo())){
					strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
				}
				strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
			} else {
				boolean apresentarDataInicio = true;
				int anoLimite = Uteis.getAnoData(this.getDataLimiteAplicacaoDesconto());
				int anoInicio = Uteis.getAnoData(dataInicioAplicacaoDesconto);
				if (anoInicio < anoLimite) {
					// se a data de início de aplicacao é muito anterior ao
					// vecto do desconto
					// nao justifica apresentá-lo na descrição. Geralmente
					// trata-se de desconto progressivo,
					// que pode ser simplesmente apresentado até quando o mesmo
					// irá valer.
					apresentarDataInicio = false;
				}
				if (apresentarDataInicio) {
					strResumida.append("De ").append(Uteis.getData(dataInicioAplicacaoDesconto)).append(" até ").append(Uteis.getData(this.getDataLimiteAplicacaoDesconto())).append(" (").append(this.getDiaNrAntesVencimento()).append(")").append(" Desconto R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append(strDescontos).append("]");
					if(Uteis.isAtributoPreenchido(getAcrescimo())){
						strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
					}
					strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
				} else {
					strResumida.append("Até ").append(Uteis.getData(this.getDataLimiteAplicacaoDesconto())).append(" (").append(this.getDiaNrAntesVencimento()).append(")").append(" Desconto R$").append(Uteis.getDoubleFormatado(valorTotalDesconto)).append(" [Descontos=").append(strDescontos).append("]");
					if(Uteis.isAtributoPreenchido(getAcrescimo())){
						strResumida.append(" Acresc.: R$").append(Uteis.getDoubleFormatado(getAcrescimo()));
					}
					strResumida.append(" - Valor a Pagar: ").append(Uteis.getDoubleFormatado(this.valorParaPagamentoDentroDataLimiteDesconto));
				}
			}
		}
		return strResumida.toString();
	}

	public Integer getCodigoPlanoDesconto() {
    	return this.getPlanoDescontoVO().getCodigo();
    }

	public Double getAcrescimo() {
		if(acrescimo == null){
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}
	
}
