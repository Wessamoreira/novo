package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;


public class ConteudoRegistroAcessoVO extends SuperVO {

    private String matricula;       
    private Date dataAcesso;
    private Integer conteudo;    
    private Integer codigo;    
    private Integer conteudoUnidadePagina;
    private Integer unidadeConteudo;
    private Integer matriculaPeriodoTurmaDisciplinaVO;
    
    /**
	 * @author Victor Hugo 17/12/2014
	 */
    /*
     * Transient
     */
	private Double totalAcumulado;
	private Double porcentagemPonto;
	private Double ponto;
	private Double totalPagina;
	private Double pagina;
	private String nomeAluno;
	private String graficoRegistroAcesso;
	private String graficoRegistroAcessoPontos;
    
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public boolean isApresentarDataAcesso(){
		return getDataAcesso() != null ? true:false; 
	}
       
    
    public Date getDataAcesso() {
        return dataAcesso;
    }
    
    public void setDataAcesso(Date dataAcesso) {
        this.dataAcesso = dataAcesso;
    }
    
    
    
    public Integer getConteudo() {
        return conteudo;
    }
    
    public void setConteudo(Integer conteudo) {
        this.conteudo = conteudo;
    }
    
    public Integer getConteudoUnidadePagina() {
        return conteudoUnidadePagina;
    }
    
    public void setConteudoUnidadePagina(Integer conteudoUnidadePagina) {
        this.conteudoUnidadePagina = conteudoUnidadePagina;
    }

    
    public Integer getCodigo() {
        return codigo;
    }

    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    
    public Integer getUnidadeConteudo() {
        return unidadeConteudo;
    }

    
    public void setUnidadeConteudo(Integer unidadeConteudo) {
        this.unidadeConteudo = unidadeConteudo;
    }

	public Integer getMatriculaPeriodoTurmaDisciplinaVO() {
		if (matriculaPeriodoTurmaDisciplinaVO == null) {
			matriculaPeriodoTurmaDisciplinaVO = 0;
		}
		return matriculaPeriodoTurmaDisciplinaVO;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVO(Integer matriculaPeriodoTurmaDisciplinaVO) {
		this.matriculaPeriodoTurmaDisciplinaVO = matriculaPeriodoTurmaDisciplinaVO;
	}
	
	
	
	public Double getTotalAcumulado() {
		if (totalAcumulado == null) {
			totalAcumulado = 0.0;
		}
		return totalAcumulado;
	}

	public void setTotalAcumulado(Double totalAcumulado) {
		this.totalAcumulado = totalAcumulado;
	}
	
	public Double getPorcentagemPonto() {
		if (porcentagemPonto == null) {
			porcentagemPonto = 0.0;
		}
		return porcentagemPonto;
	}

	public void setPorcentagemPonto(Double porcentagemPonto) {
		this.porcentagemPonto = porcentagemPonto;
	}

	public Double getPonto() {
		if (ponto == null) {
			ponto = 0.0;
		}
		return ponto;
	}

	public void setPonto(Double ponto) {
		this.ponto = ponto;
	}
	
	public Double getTotalPagina() {
		if (totalPagina == null) {
			totalPagina = 0.0;
		}
		return totalPagina;
	}

	public void setTotalPagina(Double totalPagina) {
		this.totalPagina = totalPagina;
	}

	public Double getPagina() {
		if (pagina == null) {
			pagina = 0.0;
		}
		return pagina;
	}

	public void setPagina(Double pagina) {
		this.pagina = pagina;
	}
	
	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getGraficoRegistroAcesso() {
		if (graficoRegistroAcesso == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"container").append(getMatriculaPeriodoTurmaDisciplinaVO()).append("\" style=\"width: 350px; height: 65px; margin: 0 auto;position: relative;left: -5px;\" /> ");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
//			grafico.append("	$.ajaxSetup({");
//			grafico.append("		cache : false");
//			grafico.append("	});");
//			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var options").append(getMatriculaPeriodoTurmaDisciplinaVO()).append(" = {");
			grafico.append(" 			chart: {type: \"bar\", renderTo : \"container").append(getMatriculaPeriodoTurmaDisciplinaVO()).append("\", marginTop:0, marginRight:45},");
			grafico.append(" 			title: {text: ' '},");
			grafico.append(" 			credits : {enabled : false},");
			grafico.append(" 			exporting : {enabled : false},");
			grafico.append("            xAxis: { categories: [' ']},");
			grafico.append("			yAxis: { min: 0, max: 100, title: {text: ' '}},");
			grafico.append(" 			legend: { enabled: false}, ");
			grafico.append(" 			plotOptions: {bar: {stacking: 'percent'}}, ");
			grafico.append(" 			tooltip: { ");
			grafico.append(" 			pointFormat: '").append("<span style=\"color:{series.color};\">{series.name}</span>', ");
			grafico.append(" 			shared: false,  useHTML: true, valueSuffix : '%'}, ");
			grafico.append(" 			series: [");
			grafico.append("  			{ name: '").append("Pendente" + " " + (100 - getPagina()) + " %").append("', ").append("color: '#AA4643', ").append("data: [").append(100 - getPagina()).append("]}");
			grafico.append("			, { name: '").append("Estudado " + (getPagina()) + " % - " + getTotalPagina() + " pag. ").append("', ").append("color: '#4572A7', ").append("data: [").append(getPagina()).append("]}");
			grafico.append("			]");
			grafico.append(" 	};");
			grafico.append(" 	var chart").append(getMatriculaPeriodoTurmaDisciplinaVO()).append(" = new Highcharts.Chart(options").append(getMatriculaPeriodoTurmaDisciplinaVO()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoRegistroAcesso = grafico.toString();
		}
		return graficoRegistroAcesso;
	}

	public void setGraficoRegistroAcesso(String graficoRegistroAcesso) {
		this.graficoRegistroAcesso = graficoRegistroAcesso;
	}
	
	public String getGraficoRegistroAcessoPontos() {
		if (graficoRegistroAcessoPontos == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"containerPontos").append(getMatriculaPeriodoTurmaDisciplinaVO()).append("\" style=\"width: 350px; height: 65px; margin: 0 auto;position: relative;left: -5px;\" /> ");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
//			grafico.append("	$.ajaxSetup({");
//			grafico.append("		cache : false");
//			grafico.append("	});");
//			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsPontos").append(getMatriculaPeriodoTurmaDisciplinaVO()).append(" = {");
			grafico.append(" 			chart: {type: \"bar\", renderTo : \"containerPontos").append(getMatriculaPeriodoTurmaDisciplinaVO()).append("\", marginTop:0, marginRight:45},");
			grafico.append(" 			title: {text: ' '},");
			grafico.append(" 			credits : {enabled : false},");
			grafico.append(" 			exporting : {enabled : false},");
			grafico.append("            xAxis: { categories: [' ']},");
			grafico.append("			yAxis: { min: 0, max: 100, title: {text: ' '}},");
			grafico.append(" 			legend: { enabled: false}, ");
			grafico.append(" 			plotOptions: {bar: {stacking: 'percent'}}, ");
			grafico.append(" 			tooltip: { ");
			grafico.append(" 			pointFormat: '").append("<span style=\"color:{series.color};\">{series.name}</span>', ");
			grafico.append(" 			shared: false,  useHTML: true, valueSuffix : '%'}, ");
			grafico.append(" 			series: [");
			grafico.append("  			{ name: '").append("Pendente" + " " + (100 - getPorcentagemPonto()) + " %").append("', ").append("color: '#AA4643', ").append("data: [").append(100 - getPorcentagemPonto()).append("]}");
			grafico.append("			, { name: '").append("Estudado " + (getPorcentagemPonto()) + " % -  " + getPonto() + " / " + getTotalAcumulado() + " pontos. ").append("', ").append("color: '#4572A7', ").append("data: [").append(getPorcentagemPonto()).append("]}");
			grafico.append("			]");
			grafico.append(" 	};");
			grafico.append(" 	var chartPontos").append(getMatriculaPeriodoTurmaDisciplinaVO()).append(" = new Highcharts.Chart(optionsPontos").append(getMatriculaPeriodoTurmaDisciplinaVO()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoRegistroAcessoPontos = grafico.toString();
		}
		return graficoRegistroAcessoPontos;
	}

	public void setGraficoRegistroAcessoPontos(String graficoRegistroAcessoPontos) {
		this.graficoRegistroAcessoPontos = graficoRegistroAcessoPontos;
	}
}
