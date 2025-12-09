/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author EDIGARANTONIO
 */
public final class MatriculaComHistoricoAlunoVO extends SuperVO {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean inicializado;
    private MatriculaVO matriculaVO;
    private GradeCurricularComHistoricoAlunoVO gradeCurricularComHistoricoAlunoVO;
    private ConfiguracaoAcademicoVO configuracaoAcademico;

    private Boolean controlaAtividadeComplementar;
    private Boolean alunoCumpriuAtividadesComplementares;
    private Integer numeroHorasPendendesAtividadesComplementares;
    private Integer numeroHorasRealizadasValidasAtividadesComplementares;
    private Integer numeroHorasTotalHoraGradeAtividadesComplementares;
    private Double percentualEvolucaoAtividadesComplementares;
            
    private Boolean controlaEstagio;
    private Boolean alunoCumpriuEstagio;
    private Integer numeroHorasPendendesEstagio;
    private Integer numeroHorasRealizadasEstagio;
    private Integer numeroHorasEmRealizadasEstagio;
    private Integer numeroHorasTotalEstagioGrade;
    private Double percentualEvolucaoEstagioGrade;
    
    private Integer numeroHorasTotalTCCGrade;
    
    private Boolean alunoCumpriuENADE;
    
    /**
     * Um aluno é considerado em situacao para Matricula Especial
     * quando o mesmo já integralizou a matriz curricular do curso (
     * ou seja, fez todas as disciplinas plajenadas para o mesmo)
     * contudo ainda está com pendência em atividades extracurriculares
     * obrigatoras, como ENADE, Estágio e Atividades Complementares Obrigatorias
     * 
     */
    public Boolean getAlunoEmSituacaoParaMatriculaEspecial() {
        if ((this.getGradeCurricularComHistoricoAlunoVO().getGradeIntegralizada()) &&
            ( (this.getAlunoComPendenciaAtividadeComplementar()) || 
              (this.getAlunoComPendenciaEstagio()) || 
              (this.getAlunoComPendenciaENADE()))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    /**
     * Atualiza todos os dados do aluno com relacao a estagio.
     * Determinando se o mesmo é controlada ou nao na grade curricular,
     * quantas horas o aluno cumpriu, quantas está devendo.
     * @param numeroHorasRealizadasValidasAtividadesComplementares 
     */
    public void atualizarSituacaoAlunoEstagio(Integer numeroHorasRealizadasEstagio, Integer numeroHorasEmRealizacao) {
        this.setNumeroHorasRealizadasEstagio(numeroHorasRealizadasEstagio);
        this.setNumeroHorasEmRealizadasEstagio(numeroHorasEmRealizacao);
        this.setNumeroHorasTotalEstagioGrade(this.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaEstagio());
        this.setNumeroHorasPendendesEstagio(this.getNumeroHorasTotalEstagioGrade() - (getNumeroHorasRealizadasEstagio()+ this.getNumeroHorasEmRealizadasEstagio()));
        
        if (this.getNumeroHorasRealizadasEstagio().intValue() <= 0) {
            this.setNumeroHorasRealizadasEstagio(0);
        }
        if (this.getNumeroHorasEmRealizadasEstagio().intValue() <= 0) {
        	this.setNumeroHorasEmRealizadasEstagio(0);
        }

        if (this.getNumeroHorasTotalEstagioGrade().intValue() > 0) {
            this.setControlaEstagio(Boolean.TRUE);
        } else {
            this.setControlaEstagio(Boolean.FALSE);
        }        
        if (this.getNumeroHorasTotalEstagioGrade() > 0) {
            this.setPercentualEvolucaoEstagioGrade((Double) Uteis.arrendondarForcando2CadasDecimais(getNumeroHorasRealizadasEstagio() * 100 / this.getNumeroHorasTotalEstagioGrade()));
        } else {
            this.setPercentualEvolucaoEstagioGrade(0.0);
        }
        if (this.getPercentualEvolucaoEstagioGrade().doubleValue() > 100) {
            this.setPercentualEvolucaoEstagioGrade(100.0);
        }
        
        if ((this.getControlaEstagio()) &&
            (this.getNumeroHorasPendendesEstagio().intValue() <= 0)) {
            this.setAlunoCumpriuEstagio(Boolean.TRUE);
        } else {
            this.setAlunoCumpriuEstagio(Boolean.FALSE);
        }
    }
    
    public Boolean getAlunoComPendenciaENADE() {
        //if (!this.getControlaENADE()) {
        //    return Boolean.FALSE;
        //}
        return !this.getAlunoCumpriuENADE();
    }
    
    /**
     * Diz se um alnuo está com pendencias em estágio.
     * Mas ele só diz que o aluno está com pendencia se a matriz curricular
     * estiver configurada para controlar estágio (ou seja,
     * foi lancada uma carga horaria obrigatoria)
     * @return 
     */
    public Boolean getAlunoComPendenciaEstagio() {
        if (!this.getControlaEstagio()) {
            return Boolean.FALSE;
        }
        return !this.getAlunoCumpriuEstagio();
    }
    
    
    /**
     * Diz se um alnuo está com pendencias em atividades complementares.
     * Mas ele só diz que o aluno está com pendencia se a matriz curricular
     * estiver configurada para controlar aitivdade complementar (ou seja,
     * foi lancada uma carga horaria obrigatoria)
     * @return 
     */
    public Boolean getAlunoComPendenciaAtividadeComplementar() {
        if (!this.getControlaAtividadeComplementar()) {
            return Boolean.FALSE;
        }
        return !this.getAlunoCumpriuAtividadesComplementares();
    }

    /**
     * Atualiza todos os dados do aluno com relacao a atividade complementar.
     * Determinando se a mesma é controlada ou nao na grade curricular,
     * quantas horas o aluno cumpriu, quantas está devendo.
     * @param numeroHorasRealizadasValidasAtividadesComplementares 
     */
    public void atualizarSituacaoAlunoAtividadeComplementar(Integer numeroHorasRealizadasValidasAtividadesComplementares) {
        this.setNumeroHorasRealizadasValidasAtividadesComplementares(numeroHorasRealizadasValidasAtividadesComplementares);
        this.setNumeroHorasTotalHoraGradeAtividadesComplementares(this.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaAtividadeComplementar());
        this.setNumeroHorasPendendesAtividadesComplementares(getNumeroHorasTotalHoraGradeAtividadesComplementares() - this.getNumeroHorasRealizadasValidasAtividadesComplementares());
        if (this.getNumeroHorasPendendesAtividadesComplementares().intValue() <= 0) {
            this.setNumeroHorasPendendesAtividadesComplementares(0);
        }
        
        if (this.getNumeroHorasTotalHoraGradeAtividadesComplementares().intValue() > 0) {
            this.setControlaAtividadeComplementar(Boolean.TRUE);
        } else {
            this.setControlaAtividadeComplementar(Boolean.FALSE);
        }        
        
        if (this.getNumeroHorasTotalHoraGradeAtividadesComplementares() > 0) {
            this.setPercentualEvolucaoAtividadesComplementares((Double) Uteis.arrendondarForcando2CadasDecimais(
                numeroHorasRealizadasValidasAtividadesComplementares * 100 / getNumeroHorasTotalHoraGradeAtividadesComplementares()));
        } else {
            this.setPercentualEvolucaoAtividadesComplementares(0.0);
        }
        if (this.getPercentualEvolucaoAtividadesComplementares().doubleValue() > 100) {
            this.setPercentualEvolucaoAtividadesComplementares(100.0);
        }
        
        if ((this.getControlaAtividadeComplementar()) &&
            (this.getNumeroHorasPendendesAtividadesComplementares().intValue() <= 0)) {
            this.setAlunoCumpriuAtividadesComplementares(Boolean.TRUE);
        } else {
            this.setAlunoCumpriuAtividadesComplementares(Boolean.FALSE);
        }
    }
    
    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    /**
     * @return the gradeCurricularComHistoricoAlunoVO
     */
    public GradeCurricularComHistoricoAlunoVO getGradeCurricularComHistoricoAlunoVO() {
        if (gradeCurricularComHistoricoAlunoVO == null) {
            gradeCurricularComHistoricoAlunoVO = new GradeCurricularComHistoricoAlunoVO();
        }
        return gradeCurricularComHistoricoAlunoVO;
    }

    /**
     * @param gradeCurricularComHistoricoAlunoVO the gradeCurricularComHistoricoAlunoVO to set
     */
    public void setGradeCurricularComHistoricoAlunoVO(GradeCurricularComHistoricoAlunoVO gradeCurricularComHistoricoAlunoVO) {
        this.gradeCurricularComHistoricoAlunoVO = gradeCurricularComHistoricoAlunoVO;
    }

    /**
     * @return the alunoCumpriuAtividadesComplementares
     */
    public Boolean getAlunoCumpriuAtividadesComplementares() {
        if (alunoCumpriuAtividadesComplementares == null) {
            alunoCumpriuAtividadesComplementares = Boolean.FALSE;
        }
        return alunoCumpriuAtividadesComplementares;
    }

    /**
     * @param alunoCumpriuAtividadesComplementares the alunoCumpriuAtividadesComplementares to set
     */
    public void setAlunoCumpriuAtividadesComplementares(Boolean alunoCumpriuAtividadesComplementares) {
        this.alunoCumpriuAtividadesComplementares = alunoCumpriuAtividadesComplementares;
    }

    /**
     * @return the numeroHorasPendendesAtividadesComplementares
     */
    public Integer getNumeroHorasPendendesAtividadesComplementares() {
        if (numeroHorasPendendesAtividadesComplementares == null) {
            numeroHorasPendendesAtividadesComplementares = 0;
        }
        return numeroHorasPendendesAtividadesComplementares;
    }

    /**
     * @param numeroHorasPendendesAtividadesComplementares the numeroHorasPendendesAtividadesComplementares to set
     */
    public void setNumeroHorasPendendesAtividadesComplementares(Integer numeroHorasPendendesAtividadesComplementares) {
        this.numeroHorasPendendesAtividadesComplementares = numeroHorasPendendesAtividadesComplementares;
    }

    /**
     * @return the alunoCumpriuEstagio
     */
    public Boolean getAlunoCumpriuEstagio() {
        if (alunoCumpriuEstagio == null) {
            alunoCumpriuEstagio = Boolean.FALSE;
        }
        return alunoCumpriuEstagio;
    }

    /**
     * @param alunoCumpriuEstagio the alunoCumpriuEstagio to set
     */
    public void setAlunoCumpriuEstagio(Boolean alunoCumpriuEstagio) {
        this.alunoCumpriuEstagio = alunoCumpriuEstagio;
    }

    /**
     * @return the numeroHorasPendendesEstagio
     */
    public Integer getNumeroHorasPendendesEstagio() {
        if (numeroHorasPendendesEstagio == null) {
            numeroHorasPendendesEstagio = 0;
        }
        return numeroHorasPendendesEstagio;
    }

    /**
     * @param numeroHorasPendendesEstagio the numeroHorasPendendesEstagio to set
     */
    public void setNumeroHorasPendendesEstagio(Integer numeroHorasPendendesEstagio) {
        this.numeroHorasPendendesEstagio = numeroHorasPendendesEstagio;
    }

    /**
     * @return the alunoCumpriuENADE
     */
    public Boolean getAlunoCumpriuENADE() {
        if (alunoCumpriuENADE == null) {
            alunoCumpriuENADE = Boolean.FALSE;
        }
        return alunoCumpriuENADE;
    }

    /**
     * @param alunoCumpriuENADE the alunoCumpriuENADE to set
     */
    public void setAlunoCumpriuENADE(Boolean alunoCumpriuENADE) {
        this.alunoCumpriuENADE = alunoCumpriuENADE;
    }

    /**
     * @return the configuracaoAcademico
     */
    public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
        if (configuracaoAcademico == null) {
            configuracaoAcademico = new ConfiguracaoAcademicoVO();
        }
        return configuracaoAcademico;
    }

    /**
     * @param configuracaoAcademico the configuracaoAcademico to set
     */
    public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
        this.configuracaoAcademico = configuracaoAcademico;
    }

    /**
     * @return the numeroHorasRealizadasValidasAtividadesComplementares
     */
    public Integer getNumeroHorasRealizadasValidasAtividadesComplementares() {
        if (numeroHorasRealizadasValidasAtividadesComplementares == null) {
            numeroHorasRealizadasValidasAtividadesComplementares = 0;
        }
        return numeroHorasRealizadasValidasAtividadesComplementares;
    }

    /**
     * @param numeroHorasRealizadasValidasAtividadesComplementares the numeroHorasRealizadasValidasAtividadesComplementares to set
     */
    public void setNumeroHorasRealizadasValidasAtividadesComplementares(Integer numeroHorasRealizadasValidasAtividadesComplementares) {
        this.numeroHorasRealizadasValidasAtividadesComplementares = numeroHorasRealizadasValidasAtividadesComplementares;
    }
    

    public Integer getNumeroHorasEmRealizadasEstagio() {
		if (numeroHorasEmRealizadasEstagio == null) {
			numeroHorasEmRealizadasEstagio = 0;
		}
		return numeroHorasEmRealizadasEstagio;
	}

	public void setNumeroHorasEmRealizadasEstagio(Integer numeroHorasEmRealizadasEstagio) {
		this.numeroHorasEmRealizadasEstagio = numeroHorasEmRealizadasEstagio;
	}

	/**
     * @return the numeroHorasRealizadasEstagio
     */
    public Integer getNumeroHorasRealizadasEstagio() {
        if (numeroHorasRealizadasEstagio == null) {
            numeroHorasRealizadasEstagio = 0;
        }
        return numeroHorasRealizadasEstagio;
    }

    /**
     * @param numeroHorasRealizadasEstagio the numeroHorasRealizadasEstagio to set
     */
    public void setNumeroHorasRealizadasEstagio(Integer numeroHorasRealizadasEstagio) {
        this.numeroHorasRealizadasEstagio = numeroHorasRealizadasEstagio;
    }

    /**
     * @return the controlaAtividadeComplementar
     */
    public Boolean getControlaAtividadeComplementar() {
        if (controlaAtividadeComplementar == null) {
            controlaAtividadeComplementar = Boolean.FALSE;
        }
        return controlaAtividadeComplementar;
    }

    /**
     * @param controlaAtividadeComplementar the controlaAtividadeComplementar to set
     */
    public void setControlaAtividadeComplementar(Boolean controlaAtividadeComplementar) {
        this.controlaAtividadeComplementar = controlaAtividadeComplementar;
    }

    /**
     * @return the numeroHorasTotalEstagioGrade
     */
    public Integer getNumeroHorasTotalEstagioGrade() {
        if (numeroHorasTotalEstagioGrade == null) {
            numeroHorasTotalEstagioGrade = 0;
        }
        return numeroHorasTotalEstagioGrade;
    }

    /**
     * @param numeroHorasTotalEstagioGrade the numeroHorasTotalEstagioGrade to set
     */
    public void setNumeroHorasTotalEstagioGrade(Integer numeroHorasTotalEstagioGrade) {
        this.numeroHorasTotalEstagioGrade = numeroHorasTotalEstagioGrade;
    }
    
    

    public Integer getNumeroHorasTotalTCCGrade() {
    	  if (numeroHorasTotalTCCGrade == null) {
    		  numeroHorasTotalTCCGrade = 0;
          }
		return numeroHorasTotalTCCGrade;
	}

	public void setNumeroHorasTotalTCCGrade(Integer numeroHorasTotalTCCGrade) {
		this.numeroHorasTotalTCCGrade = numeroHorasTotalTCCGrade;
	}

	/**
     * @return the controlaEstagio
     */
    public Boolean getControlaEstagio() {
        if (controlaEstagio == null) {
            controlaEstagio = Boolean.FALSE;
        }
        return controlaEstagio;
    }

    /**
     * @param controlaEstagio the controlaEstagio to set
     */
    public void setControlaEstagio(Boolean controlaEstagio) {
        this.controlaEstagio = controlaEstagio;
    }

    /**
     * @return the inicializado
     */
    public Boolean getInicializado() {
        if (inicializado == null) {
            inicializado = Boolean.FALSE;
        }
        return inicializado;
    }
    
    public Boolean getIsInicializado() {
        return getInicializado();
    }

    /**
     * @param inicializado the inicializado to set
     */
    public void setInicializado(Boolean inicializado) {
        this.inicializado = inicializado;
    }

    /**
     * @return the percentualEvolucaoAtividadesComplementares
     */
    public Double getPercentualEvolucaoAtividadesComplementares() {
        if (percentualEvolucaoAtividadesComplementares == null) {
            percentualEvolucaoAtividadesComplementares = 0.0;
        }
        return percentualEvolucaoAtividadesComplementares;
    }

    /**
     * @param percentualEvolucaoAtividadesComplementares the percentualEvolucaoAtividadesComplementares to set
     */
    public void setPercentualEvolucaoAtividadesComplementares(Double percentualEvolucaoAtividadesComplementares) {
        this.percentualEvolucaoAtividadesComplementares = percentualEvolucaoAtividadesComplementares;
    }

    /**
     * @return the percentualEvolucaoEstagioGrade
     */
    public Double getPercentualEvolucaoEstagioGrade() {
        if (percentualEvolucaoEstagioGrade == null) {
            percentualEvolucaoEstagioGrade = 0.0;
        }
        return percentualEvolucaoEstagioGrade;
    }

    /**
     * @param percentualEvolucaoEstagioGrade the percentualEvolucaoEstagioGrade to set
     */
    public void setPercentualEvolucaoEstagioGrade(Double percentualEvolucaoEstagioGrade) {
        this.percentualEvolucaoEstagioGrade = percentualEvolucaoEstagioGrade;
    }

    /**
     * @return the numeroHorasTotalHoraGradeAtividadesComplementares
     */
    public Integer getNumeroHorasTotalHoraGradeAtividadesComplementares() {
        if (numeroHorasTotalHoraGradeAtividadesComplementares == null) {
            numeroHorasTotalHoraGradeAtividadesComplementares = 0;
        }
        return numeroHorasTotalHoraGradeAtividadesComplementares;
    }

    /**
     * @param numeroHorasTotalHoraGradeAtividadesComplementares the numeroHorasTotalHoraGradeAtividadesComplementares to set
     */
    public void setNumeroHorasTotalHoraGradeAtividadesComplementares(Integer numeroHorasTotalHoraGradeAtividadesComplementares) {
        this.numeroHorasTotalHoraGradeAtividadesComplementares = numeroHorasTotalHoraGradeAtividadesComplementares;
    }
    
    /**
     * Método responsável por limpar todos os dados e listas 
     * de controle existentes no MatriculaComHistoricoAlunoVO
     * relativos a histórico. Permitindo que uma nova lista de histórico
     * (devidamente atualizada) possa ser processada no mesmo
     * objeto de MatriculaComHistoricoAlunoVO
     */
    public void limparDadosHistoricos() {
        this.getGradeCurricularComHistoricoAlunoVO().limparDadosHistoricos();

    }
    
    public MatriculaComHistoricoAlunoVO() {
    	super();
	}
    
    
    public void gerarDadosGraficoAtividadeComplementar() throws Exception {
		Integer totalCHGrade = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasTotalHoraGradeAtividadesComplementares();
		if(totalCHGrade > 0){
		StringBuilder graficoAtividadeComplementar = new StringBuilder();
		Integer totalCHAluno = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasRealizadasValidasAtividadesComplementares();
		Double percRealizacao = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getPercentualEvolucaoAtividadesComplementares();
		Double saldoRealizarPerc = Uteis.arrendondarForcando2CadasDecimais(100 - percRealizacao);
		Integer saldoRealizarHoras = totalCHGrade - totalCHAluno;
		if (saldoRealizarHoras < 0) {
			saldoRealizarHoras = 0;
		}
		String tituloGrafico = "Atividade Complementar (" + percRealizacao + "% de " + totalCHGrade + "h)";
		
//		String subTituloGrafico = "Horas Obrigatórias Atividades Complementar (" + totalCHGrade + "h)";
				
		graficoAtividadeComplementar.append("<div id=\"graficoAtividadeComplementar\" class=\"pn col-md-12\" style=\"height: 200px; margin: 0 auto\" >");
		graficoAtividadeComplementar.append(" <script type=\"text/javascript\" charset=\"UTF-8\">");
		graficoAtividadeComplementar.append("                     (function($) {");
		graficoAtividadeComplementar.append("                             var options = {");
		graficoAtividadeComplementar.append("                                 chart: {type: 'bar', renderTo : \"graficoAtividadeComplementar\"},");
		graficoAtividadeComplementar.append("                                 title: {text: '").append(tituloGrafico).append("',  style:{\"color\": \"#929599\", \"font-size\":\"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"700\",\"line-height\": \"1.788\"}},");		
		graficoAtividadeComplementar.append("                                 exporting: { enabled: false },");
		graficoAtividadeComplementar.append("                                 legend: {layout: 'vertical',align: 'right',verticalAlign: 'middle',borderWidth: 0},");
		graficoAtividadeComplementar.append("                                 credits : {enabled : false},");
		graficoAtividadeComplementar.append("                                 xAxis: {categories: null, visible:false},");
		graficoAtividadeComplementar.append("                                 yAxis: {min: 0, max:100, title: {text: '', style:{\"color\": \"#929599\",\"font-size\":\"10px\"}}},");		
		graficoAtividadeComplementar.append("                                 legend: {reversed:true, borderWidth: 0, itemStyle:{\"color\": \"#929599\", \"font-size\": \"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"400\",\"line-height\": \"1.788\"}},");
		graficoAtividadeComplementar.append("                                 tooltip: {");
		graficoAtividadeComplementar.append("                                     pointFormat: '<span style=\"color:{series.color}\">{series.name}</span><b>{point.y}%</b><br/> <br/>',");
		graficoAtividadeComplementar.append("                                     shared: false");
		graficoAtividadeComplementar.append("                                 },");
		graficoAtividadeComplementar.append("                                 plotOptions: {bar: {stacking: 'normal'}},");
		graficoAtividadeComplementar.append("                                 series: [");
		// REALIZADA...
				graficoAtividadeComplementar.append("{name: 'Realizada (").append(totalCHAluno).append("h").append(")', data: [").append(percRealizacao).append("], color: '").append("#11a8bb").append("', index: 1, zIndex: 1 } ");
				// PENDENTE...
				graficoAtividadeComplementar.append(", {name: 'Pendente (").append(saldoRealizarHoras).append("h").append(")', data: [").append(saldoRealizarPerc).append("], color: '").append("#cccccc").append("', index: 0, zIndex: 100 } ");
		graficoAtividadeComplementar.append(" 								]");
		graficoAtividadeComplementar.append("                             };");
		graficoAtividadeComplementar.append("                             var chart6 = new Highcharts.Chart(options);");		
		graficoAtividadeComplementar.append("                     }(jQuery));");
		graficoAtividadeComplementar.append("                 </script></div>");		
		
		this.graficoAtividadeComplementar = graficoAtividadeComplementar.toString();
		}
	}

	public void gerarDadosGraficoEstagio() throws Exception {		
		Integer totalCHGrade = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasTotalEstagioGrade();
		if(totalCHGrade > 0){
			StringBuilder graficoEstagio = new StringBuilder();
			
			Integer totalCHAluno = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasRealizadasEstagio();
			Double percRealizado = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getPercentualEvolucaoEstagioGrade();
			
			Integer totalCHEmRealizacao = getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasEmRealizadasEstagio();
			Double percEmRealizacaoHoras = Uteis.arrendondarForcando2CadasDecimais(getNumeroHorasEmRealizadasEstagio() * 100 / this.getNumeroHorasTotalEstagioGrade());
			
			Double saldoRealizarPerc = Uteis.arrendondarForcando2CadasDecimais(100 - (percRealizado + percEmRealizacaoHoras));
			Integer saldoRealizarHoras = totalCHGrade - (totalCHAluno + totalCHEmRealizacao) ;
			
			if (saldoRealizarHoras < 0) {
				saldoRealizarHoras = 0;
			}
			String tituloGrafico = "Estágio (" + (percRealizado + percEmRealizacaoHoras) + "% de " + totalCHGrade + "h)";
	//		String subTituloGrafico = "Horas Obrigatórias Estágio (" + totalCHGrade + "h)";
			
			graficoEstagio = new StringBuilder("");
			graficoEstagio.append("<div id=\"graficoEstagio\"  class=\"pn col-md-12\" style=\"height: 130px; margin: 0 auto\" > ");
			graficoEstagio.append("	<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			graficoEstagio.append("	                    (function($) { ");
			graficoEstagio.append("	                            var options = { ");
			graficoEstagio.append("	                                chart: {type: 'bar', renderTo : \"graficoEstagio\"}, ");
			graficoEstagio.append("	                                title: {x:0, text: '").append(tituloGrafico).append("',  style:{\"color\": \"#929599\", \"width\":\"100%\", \"font-size\":\"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"700\",\"line-height\": \"1.788\"} }, ");
			graficoEstagio.append("	                                exporting: { enabled: false }, ");
			graficoEstagio.append("	                                credits : {enabled : false}, ");
			graficoEstagio.append("	                                xAxis: {categories: null, visible:false}, ");
			graficoEstagio.append("	                                yAxis: {min: 0, max:100, title: {text: '', style:{\"color\": \"#929599\", \"font-size\":\"10px\"}}}, ");
			graficoEstagio.append("	                                legend: {reversed:true, borderWidth: 0, itemStyle:{\"color\": \"#929599\", \"font-size\": \"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"400\",\"line-height\": \"1.788\"}}, ");
			graficoEstagio.append("	                                tooltip: { ");
			graficoEstagio.append("	                                    pointFormat: '<span style=\"color:{series.color}\">{series.name}</span><b>{point.y}%</b><br/> <br/>', ");
			graficoEstagio.append("	                                    shared: false ");
			graficoEstagio.append("	                                }, ");
			graficoEstagio.append("	                                plotOptions: {bar: {stacking: 'normal'}}, ");
			graficoEstagio.append("	                                series: [ ");
			// REALIZADA...
			graficoEstagio.append(" {name: 'Realizada (").append(totalCHAluno).append("h").append(")', data: [").append(percRealizado).append("], color: '").append("#11a8bb").append("', index: 2, zIndex: 1 } ");
			// Em Realizacao...
			graficoEstagio.append(", {name: 'Em Realização (").append(totalCHEmRealizacao).append("h").append(")', data: [").append(percEmRealizacaoHoras).append("], color: '").append("#f6e843").append("', index: 1, zIndex: 100 } ");
			// PENDENTE...
			graficoEstagio.append(", {name: 'Pendente (").append(saldoRealizarHoras).append("h").append(")', data: [").append(saldoRealizarPerc).append("], color: '").append("#cccccc").append("', index: 0, zIndex: 1000 } ");
			graficoEstagio.append("									] ");
			graficoEstagio.append("	                            }; ");
			graficoEstagio.append("	                            var chart7 = new Highcharts.Chart(options); ");
			
			graficoEstagio.append("	                    }(jQuery)); ");
			graficoEstagio.append("	                </script></div> ");
			this.graficoEstagio = graficoEstagio.toString();
		}
	}
	
	public void gerarDadosGraficoEstagioNaoLiberado() throws Exception {
		
		if(getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaEstagio() > 0){
			
			Double totalHorasLiberarEstagio = Uteis.arrendondarForcando2CadasDecimais(((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getCargaHoraria()-(getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaEstagio() + getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasTotalTCCGrade())) * getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPercentualPermitirIniciarEstagio()) / 100 );
			Double percRealizado = Uteis.arrendondarForcando2CadasDecimais((getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaCursadaAluno() * 100) / totalHorasLiberarEstagio);
			
			Double pendenteHoras = Uteis.arrendondarForcando2CadasDecimais(totalHorasLiberarEstagio -  getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaCursadaAluno());
			Double percHoras = Uteis.arrendondarForcando2CadasDecimais(100 -  percRealizado);
			
			String tituloGrafico = "Quantidade de Horas para Liberação do Estágio " + Uteis.getParteInteiraDouble(totalHorasLiberarEstagio)  + "h.";
			
			StringBuilder graficoEstagio = new StringBuilder();
			graficoEstagio.append("<div id=\"graficoEstagioNaoLiberado\"  class=\"pn col-md-12\" style=\"height: 130px; margin: 0 auto\" > ");
			graficoEstagio.append("	<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			graficoEstagio.append("	                    (function($) { ");
			graficoEstagio.append("	                            var options = { ");
			graficoEstagio.append("	                                chart: {type: 'bar', renderTo : \"graficoEstagioNaoLiberado\"}, ");
			graficoEstagio.append("	                                title: {x:0, text: '").append(tituloGrafico).append("',  style:{\"color\": \"#929599\", \"width\":\"100%\", \"font-size\":\"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"700\",\"line-height\": \"1.788\"} }, ");
			graficoEstagio.append("	                                exporting: { enabled: false }, ");
			graficoEstagio.append("	                                credits : {enabled : false}, ");
			graficoEstagio.append("	                                xAxis: {categories: null, visible:false}, ");
			graficoEstagio.append("	                                yAxis: {min: 0, max:100, title: {text: '', style:{\"color\": \"#929599\", \"font-size\":\"10px\"}}}, ");
			graficoEstagio.append("	                                legend: {reversed:true, borderWidth: 0, itemStyle:{\"color\": \"#929599\", \"font-size\": \"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"400\",\"line-height\": \"1.788\"}}, ");
			graficoEstagio.append("	                                tooltip: { ");
			graficoEstagio.append("	                                    pointFormat: '<span style=\"color:{series.color}\">{series.name}</span><b>{point.y}%</b><br/> <br/>', ");
			graficoEstagio.append("	                                    shared: false ");
			graficoEstagio.append("	                                }, ");
			graficoEstagio.append("	                                plotOptions: {bar: {stacking: 'normal'}}, ");
			graficoEstagio.append("	                                series: [ ");
			
			// REALIZADA...
			graficoEstagio.append(" {name: 'Realizada (").append(getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaCursadaAluno()).append("h").append(")', data: [").append(percRealizado).append("], color: '").append("#11a8bb").append("', index: 2, zIndex: 1 } ");
			// Em Realizacao...
			graficoEstagio.append(", {name: 'Pendente  (").append( Uteis.getParteInteiraDouble(pendenteHoras)).append("h").append(")', data: [").append(percHoras).append("], color: '").append("#f6e843").append("', index: 1, zIndex: 100 } ");
			// PENDENTE...
//			graficoEstagio.append(", {name: 'Total de Horas para Liberação do Estagio (").append(totalHorasLiberarEstagio).append("h").append(")', data: [").append(100).append("], color: '").append("#cccccc").append("', index: 0, zIndex: 1000 } ");
			
			graficoEstagio.append("									] ");
			graficoEstagio.append("	                            }; ");
			graficoEstagio.append("	                            var chart7 = new Highcharts.Chart(options); ");
			
			graficoEstagio.append("	                    }(jQuery)); ");
			graficoEstagio.append("	                </script></div> ");
			this.graficoEstagioNaoLiberado = graficoEstagio.toString();
		}
	}
	
	public boolean isEstagioLiberado() {
		//(880 * 100 / 3520 - 400 - 200) > 50		
		  Integer primeiroNumero = (getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaCursadaAluno() * 100) ; 
		  Integer segundoNumero = (getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getCargaHoraria() - (getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaEstagio() + getMatriculaVO().getMatriculaComHistoricoAlunoVO().getNumeroHorasTotalTCCGrade()));
		  return  segundoNumero > 0  && ((primeiroNumero / segundoNumero ) >=  getMatriculaVO().getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getPercentualPermitirIniciarEstagio());
	}

	public void gerarDadosGraficoEvolucaoAcademicaAluno() throws Exception {
		gerarDadosGraficoSituacaoMatriculaGradeCurricular();
		gerarDadosGraficoAtividadeComplementar();
        //gerarDadosGraficoIntegralizacaoCurricular();
		if(isEstagioLiberado()) {
			gerarDadosGraficoEstagio();	
		}else {
			gerarDadosGraficoEstagioNaoLiberado();	
		}
	}

	private String graficoSituacaoAcademicaAluno;
	private String graficoSituacaoAcademicaAluno2;
	private String graficoEstagio;
	private String graficoEstagioNaoLiberado;
	private String graficoAtividadeComplementar;	
	private String graficoTcc;
    private String graficoIntegralizacaoCurricular;

	/**
	 * @return the graficoSituacaoAcademicaAluno
	 */
	public String getGraficoSituacaoAcademicaAluno() {
		if (graficoSituacaoAcademicaAluno == null) {
			graficoSituacaoAcademicaAluno = "";
		}
		return graficoSituacaoAcademicaAluno;
	}

	/**
	 * @param graficoSituacaoAcademicaAluno the graficoSituacaoAcademicaAluno to set
	 */
	public void setGraficoSituacaoAcademicaAluno(String graficoSituacaoAcademicaAluno) {
		this.graficoSituacaoAcademicaAluno = graficoSituacaoAcademicaAluno;
	}

	/**
	 * @return the graficoSituacaoAcademicaAluno2
	 */
	public String getGraficoSituacaoAcademicaAluno2() {
		if (graficoSituacaoAcademicaAluno2 == null) {
			graficoSituacaoAcademicaAluno2 = "";
		}
		return graficoSituacaoAcademicaAluno2;
	}

	/**
	 * @param graficoSituacaoAcademicaAluno2 the graficoSituacaoAcademicaAluno2 to set
	 */
	public void setGraficoSituacaoAcademicaAluno2(String graficoSituacaoAcademicaAluno2) {
		this.graficoSituacaoAcademicaAluno2 = graficoSituacaoAcademicaAluno2;
	}

	/**
	 * @return the graficoEstagio
	 */
	public String getGraficoEstagio() {
		if (graficoEstagio == null) {
			graficoEstagio = "";
		}
		return graficoEstagio;
	}

	/**
	 * @param graficoEstagio the graficoEstagio to set
	 */
	public void setGraficoEstagio(String graficoEstagio) {
		this.graficoEstagio = graficoEstagio;
	}

	public String getGraficoEstagioNaoLiberado() {
		if (graficoEstagioNaoLiberado == null) {
			graficoEstagioNaoLiberado = "";
		}
		return graficoEstagioNaoLiberado;
	}

	public void setGraficoEstagioNaoLiberado(String graficoEstagioNaoLiberado) {
		this.graficoEstagioNaoLiberado = graficoEstagioNaoLiberado;
	}

	/**
	 * @return the graficoAtividadeComplementar
	 */
	public String getGraficoAtividadeComplementar() {
		if (graficoAtividadeComplementar == null) {
			graficoAtividadeComplementar = "";
		}
		return graficoAtividadeComplementar;
	}

	/**
	 * @param graficoAtividadeComplementar the graficoAtividadeComplementar to set
	 */
	public void setGraficoAtividadeComplementar(String graficoAtividadeComplementar) {
		this.graficoAtividadeComplementar = graficoAtividadeComplementar;
	}

	public void gerarDadosGraficoSituacaoMatriculaGradeCurricular() throws Exception {
		
		StringBuilder graficoSituacaoAcademicaAluno = new StringBuilder();
		Integer totalCHGrade = getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaDisciplinasObrigatorias() + getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCargaHorariaOptativaExigida();
		Integer totalCHAlunoCursada = getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaCursadaAluno();
		Integer totalCHAlunoCursando = getGradeCurricularComHistoricoAlunoVO().getTotalCargaHorariaAlunoEstaCursandoAtualmente();
		Integer totalCrGrade = getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCreditoDisciplinasObrigatorias() + getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().getTotalCreditoOptativaExigida();
		Integer totalCrAlunoCursada = getGradeCurricularComHistoricoAlunoVO().getTotalCreditosCursadosAluno();
		Integer totalCrAlunoCursando = getGradeCurricularComHistoricoAlunoVO().getTotalCreditosAlunoEstaCursandoAtualmente();

		Double saldoPara100Porcento = Uteis.arrendondarForcando2CadasDecimais(100.0 - getGradeCurricularComHistoricoAlunoVO().getPercenutalCargaHorariaCursada() - getGradeCurricularComHistoricoAlunoVO().getPercenutalCargaHorariaAlunoEstaCursandoAtualmente());

		Integer saldoCHPendente = totalCHGrade - totalCHAlunoCursada - totalCHAlunoCursando;
		if (saldoCHPendente < 0) {
			saldoCHPendente = 0;
		}
		Integer saldoCreditosPendente = totalCrGrade - totalCrAlunoCursada - totalCrAlunoCursando;
		if (saldoCreditosPendente < 0) {
			saldoCreditosPendente = 0;
		}
//		String tituloGraficoSituacaoAcademica = "Evolução Acadêmica (" + getGradeCurricularComHistoricoAlunoVO().getPercentualIntegralizacaoPorCargaHoraria() + "%)";
		
		String strIntegralizada = "Não Integralizada";
		if (getGradeCurricularComHistoricoAlunoVO().getGradeIntegralizada()) {
			strIntegralizada = "Integralizada";
		}
		String subTituloGraficoSituacaoAcademica = "Matriz " + strIntegralizada + " (" + totalCHGrade + "h)";
		
		
		graficoSituacaoAcademicaAluno = new StringBuilder();
		graficoSituacaoAcademicaAluno.append("<div id=\"graficoSituacaoAcademicaAluno\" class=\"pn col-md-12\" style=\"height: 150px; margin: 0 auto\" > ");
		graficoSituacaoAcademicaAluno.append(" <script type=\"text/javascript\" charset=\"UTF-8\">"); 
		graficoSituacaoAcademicaAluno.append("                     (function($) {"); 
		graficoSituacaoAcademicaAluno.append("                             var options = {"); 
		graficoSituacaoAcademicaAluno.append("                                 chart: {type: 'bar', renderTo : \"graficoSituacaoAcademicaAluno\"},"); 
		graficoSituacaoAcademicaAluno.append("                                 credits : {enabled : false},"); 
		graficoSituacaoAcademicaAluno.append("	                                title: {userHtml:true, x:0, text: '").append(subTituloGraficoSituacaoAcademica).append("',  style:{\"color\": \"#929599\", \"width\":\"100%\", \"display\":\"block\", \"font-size\":\"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"700\",\"line-height\": \"1.788\"} }, ");
		graficoSituacaoAcademicaAluno.append("                                 exporting: { enabled: false },"); 
		graficoSituacaoAcademicaAluno.append("                                 xAxis: {categories: null, visible:false},"); 
		graficoSituacaoAcademicaAluno.append("                                 yAxis: {min: 0, max: 100, title: {text: '', style:{\"color\": \"#929599\", \"font-size\":\"10px\"}}},"); 
		graficoSituacaoAcademicaAluno.append("                                 legend: {borderWidth: 0, itemStyle:{\"color\": \"#929599\", \"font-size\": \"10px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"400\",\"line-height\": \"1.788\"}},"); 
		graficoSituacaoAcademicaAluno.append("                                 tooltip: {"); 
		graficoSituacaoAcademicaAluno.append("                                     pointFormat: '<span style=\"color:{series.color}\">{series.name}</span><b>{point.y}%</b><br/> <br/>',"); 
		graficoSituacaoAcademicaAluno.append("                                     shared: false"); 
		graficoSituacaoAcademicaAluno.append("                                 },"); 
		graficoSituacaoAcademicaAluno.append("                                 plotOptions: {bar: {stacking: 'normal'}},"); 
		graficoSituacaoAcademicaAluno.append("                                 series: ["); 		
		// REALIZADA...
		graficoSituacaoAcademicaAluno.append("{name: 'Realizada (").append(totalCHAlunoCursada).append("h)', data: [").append(getGradeCurricularComHistoricoAlunoVO().getPercenutalCargaHorariaCursada()).append("], color: '").append("#1cc88a").append("', index: 3, zIndex: 1000 } ");
		// REALIZANDO...
		graficoSituacaoAcademicaAluno.append(", {name: 'Cursando (").append(totalCHAlunoCursando).append("h)', data: [").append(getGradeCurricularComHistoricoAlunoVO().getPercenutalCargaHorariaAlunoEstaCursandoAtualmente()).append("], color: '").append("#36b9cc").append("', index: 2, zIndex: 2000 }");
		// PENDENTE...
		graficoSituacaoAcademicaAluno.append(", {name: 'Pendente (").append(saldoCHPendente).append("h)', data: [").append(saldoPara100Porcento).append("], color: '").append("#ededed").append("', index: 1, zIndex: 3000 } ");
		graficoSituacaoAcademicaAluno.append(" 									]"); 		
		graficoSituacaoAcademicaAluno.append("                             };"); 
		graficoSituacaoAcademicaAluno.append("                             var chart5 = new Highcharts.Chart(options);"); 
		graficoSituacaoAcademicaAluno.append("                     }(jQuery));"); 
		graficoSituacaoAcademicaAluno.append("                 </script></div>"); 
		
		this.graficoSituacaoAcademicaAluno = graficoSituacaoAcademicaAluno.toString();
		graficoSituacaoAcademicaAluno2 = this.graficoSituacaoAcademicaAluno.replaceAll("graficoSituacaoAcademicaAluno", "graficoSituacaoAcademicaAluno2");
	}


	public void gerarDadosGraficoTcc(MatriculaIntegralizacaoCurricularVO matriculaIntegralizacaoCurricularVO) throws Exception {
		if(matriculaIntegralizacaoCurricularVO != null){
			StringBuilder graficoTcc = new StringBuilder();
			Integer cargaHorariaPendente = null;
			Integer percentualPendente = null;
			if(matriculaIntegralizacaoCurricularVO.getCargaHorariaCumpridaLiberarTcc() > matriculaIntegralizacaoCurricularVO.getCargaHorariaExigidaLiberarTcc()) {
				cargaHorariaPendente = 0;
			}
			if(matriculaIntegralizacaoCurricularVO.getCargaHorariaCumpridaLiberarTcc().equals(0)) {
				percentualPendente = 100;
			}
			
			String tituloGrafico = "Você cumpriu "+matriculaIntegralizacaoCurricularVO.getPercentualCumpridoLiberarTcc()+"% para liberação do TCC (" + Uteis.arrendondarForcando2CadasDecimais(matriculaIntegralizacaoCurricularVO.getPercentualCumpridoLiberarTcc()) + "% de " + matriculaIntegralizacaoCurricularVO.getCargaHorariaExigidaLiberarTcc() + "h)";
			
			graficoTcc = new StringBuilder("");
			graficoTcc.append("<div id=\"graficoTcc\"  class=\"pn col-md-12\" style=\"height: 130px; margin: 0 auto\" > ");
			graficoTcc.append("	<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			graficoTcc.append("	                    (function($) { ");
			graficoTcc.append("	                            var options = { ");
			graficoTcc.append("	                                chart: {type: 'bar', renderTo : \"graficoTcc\"}, ");
			graficoTcc.append("	                                title: {x:0, text: '").append(tituloGrafico).append("',  style:{\"color\": \"#929599\", \"width\":\"100%\", \"font-size\":\"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"700\",\"line-height\": \"1.788\"} }, ");
			graficoTcc.append("	                                exporting: { enabled: false }, ");
			graficoTcc.append("	                                credits : {enabled : false}, ");
			graficoTcc.append("	                                xAxis: {categories: null, visible:false}, ");
			graficoTcc.append("	                                yAxis: {min: 0, max:100, title: {text: '', style:{\"color\": \"#929599\", \"font-size\":\"10px\"}}}, ");
			graficoTcc.append("	                                legend: {reversed:true, borderWidth: 0, itemStyle:{\"color\": \"#929599\", \"font-size\": \"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"400\",\"line-height\": \"1.788\"}}, ");
			graficoTcc.append("	                                tooltip: { ");
			graficoTcc.append("	                                    pointFormat: '<span style=\"color:{series.color}\">{series.name}</span><b>{point.y}%</b><br/> <br/>', ");
			graficoTcc.append("	                                    shared: false ");
			graficoTcc.append("	                                }, ");
			graficoTcc.append("	                                plotOptions: {bar: {stacking: 'normal'}}, ");
			graficoTcc.append("	                                series: [ ");
			// REALIZADA...
			graficoTcc.append(" {name: 'CH Cumprida para Liberação (").append(matriculaIntegralizacaoCurricularVO.getCargaHorariaCumpridaLiberarTcc()).append("h").append(")', data: [").append(Uteis.arrendondarForcando2CadasDecimais(matriculaIntegralizacaoCurricularVO.getPercentualCumpridoLiberarTcc())).append("], color: '").append("#11a8bb").append("', index: 2, zIndex: 1 } ");
			// PENDENTE...
			graficoTcc.append(", {name: 'CH Pendente para Liberação (").append(cargaHorariaPendente != null ? cargaHorariaPendente : matriculaIntegralizacaoCurricularVO.getCargaHorariaExigidaLiberarTcc() - matriculaIntegralizacaoCurricularVO.getCargaHorariaCumpridaLiberarTcc()).append("h").append(")', data: [").append(percentualPendente != null ? Uteis.arrendondarForcando2CadasDecimais(percentualPendente) :  Uteis.arrendondarForcando2CadasDecimais(matriculaIntegralizacaoCurricularVO.getPercentualPendenteLiberarTcc())).append("], color: '").append("#cccccc").append("', index: 0, zIndex: 1000 } ");
			graficoTcc.append("									] ");
			graficoTcc.append("	                            }; ");
			graficoTcc.append("	                            var chart7 = new Highcharts.Chart(options); ");
			
			graficoTcc.append("	                    }(jQuery)); ");
			graficoTcc.append("	                </script></div> ");
			this.graficoTcc = graficoTcc.toString();
		}
	}
	
	
	public String getGraficoTcc() {
		if(graficoTcc == null) {
			graficoTcc = Constantes.EMPTY;
		}
		return graficoTcc;
	}

    public String getGraficoIntegralizacaoCurricular() {
        if(graficoIntegralizacaoCurricular == null){
            graficoIntegralizacaoCurricular = "";
        }
        return graficoIntegralizacaoCurricular;
    }

    public void setGraficoIntegralizacaoCurricular(String graficoIntegralizacaoCurricular) {
        this.graficoIntegralizacaoCurricular = graficoIntegralizacaoCurricular;
    }

    public void gerarDadosGraficoIntegralizacaoCurricular(MatriculaIntegralizacaoCurricularVO matriculaIntegralizacaoCurricularVO) throws Exception {

        StringBuilder graficoIntegralizacaoCurricular = new StringBuilder();
        Integer cargaHorariaTotal = matriculaIntegralizacaoCurricularVO.getCargaHorariaTotal();
        Integer cargaHorariaCumprido = matriculaIntegralizacaoCurricularVO.getCargaHorariaCumprido();
        Integer cargaHorariaPendente = matriculaIntegralizacaoCurricularVO.getCargaHorariaPendente();

        Double saldoPercentualNaoIntegralizado = matriculaIntegralizacaoCurricularVO.getPercentualNaoIntegralizado();
        Double saldoPercentualIntegralizado = matriculaIntegralizacaoCurricularVO.getPercentualIntegralizado();
        String strIntegralizada = "Integralizada";

        String subTituloGraficoSituacaoAcademica = "Matriz  (" + saldoPercentualIntegralizado + "%) " + strIntegralizada + " de ("+ cargaHorariaTotal +"h)" ;


        graficoIntegralizacaoCurricular = new StringBuilder();
        graficoIntegralizacaoCurricular.append("<div id=\"graficoIntegralizacaoCurricular\" class=\"pn col-md-12\" style=\"height: 150px; margin: 0 auto\" > ");
        graficoIntegralizacaoCurricular.append(" <script type=\"text/javascript\" charset=\"UTF-8\">");
        graficoIntegralizacaoCurricular.append("                     (function($) {");
        graficoIntegralizacaoCurricular.append("                             var options = {");
        graficoIntegralizacaoCurricular.append("                                 chart: {type: 'bar', renderTo : \"graficoIntegralizacaoCurricular\"},");
        graficoIntegralizacaoCurricular.append("                                 credits : {enabled : false},");
        graficoIntegralizacaoCurricular.append("	                                title: {userHtml:true, x:0, text: '").append(subTituloGraficoSituacaoAcademica).append("',  style:{\"color\": \"#929599\", \"width\":\"100%\", \"display\":\"block\", \"font-size\":\"12px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"700\",\"line-height\": \"1.788\"} }, ");
        graficoIntegralizacaoCurricular.append("                                 exporting: { enabled: false },");
        graficoIntegralizacaoCurricular.append("                                 xAxis: {categories: null, visible:false},");
        graficoIntegralizacaoCurricular.append("                                 yAxis: {min: 0, max: 100, title: {text: '', style:{\"color\": \"#929599\", \"font-size\":\"10px\"}}},");
        graficoIntegralizacaoCurricular.append("                                 legend: {borderWidth: 0, itemStyle:{\"color\": \"#929599\", \"font-size\": \"10px\",\"font-family\": \"'Oxygen', Helvetica, Arial, sans-serif\",\"font-weight\": \"400\",\"line-height\": \"1.788\"}},");
        graficoIntegralizacaoCurricular.append("                                 tooltip: {");
        graficoIntegralizacaoCurricular.append("                                     headerFormat: '<b>{series.name}</b><br/>', pointFormat: '<b>{point.y}%</b><br/> <br/>',");
        graficoIntegralizacaoCurricular.append("                                     shared: false");
        graficoIntegralizacaoCurricular.append("                                 },");
        graficoIntegralizacaoCurricular.append("                                 plotOptions: {bar: {stacking: 'normal'}},");
        graficoIntegralizacaoCurricular.append("                                 series: [");
           // REALIZADA...
        graficoIntegralizacaoCurricular.append("{name: 'Realizada (").append(cargaHorariaCumprido).append("h)', data: [").append(saldoPercentualIntegralizado).append("], color: '").append("#1cc88a").append("', index: 2, zIndex: 1000 } ");
          // PENDENTE...
        graficoIntegralizacaoCurricular.append(", {name: 'Pendente (").append(cargaHorariaPendente).append("h)', data: [").append(saldoPercentualNaoIntegralizado).append("], color: '").append("#ededed").append("', index: 1, zIndex: 2000 } ");
        graficoIntegralizacaoCurricular.append(" 									]");
        graficoIntegralizacaoCurricular.append("                             };");
        graficoIntegralizacaoCurricular.append("                             var chart5 = new Highcharts.Chart(options);");
        graficoIntegralizacaoCurricular.append("                     }(jQuery));");
        graficoIntegralizacaoCurricular.append("                 </script></div>");

        this.graficoIntegralizacaoCurricular = graficoIntegralizacaoCurricular.toString();
    }

}
