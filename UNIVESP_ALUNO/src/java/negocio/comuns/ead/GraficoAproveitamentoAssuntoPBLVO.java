package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Andrade 09/01/2017
 *
 */
public class GraficoAproveitamentoAssuntoPBLVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5607820349948027123L;

	private MonitorConhecimentoPBLVO monitorConhecimentoPBLVO;
	private String assunto;
	private Integer codigoTemaAssunto;
	private List<GraficoAproveitamentoAssuntoItemPBLVO> graficoItemPizza;
	private List<GraficoAproveitamentoAssuntoItemPBLVO> graficoItemBarra;
	private String graficoPizzaEmJavaScript;
	private String serieGraficoPizzaEmJavaScript;
	private String graficoBarraEmJavaScript;
	private String serieGraficoBarraEmJavaScript;
	private String categoriaGraficoBarraEmJavaScript;
	private String graficoBarraPorNotaEmJavaScript;
	private String serieGraficoBarraPorNotaEmJavaScript;
	private String categoriaGraficoBarraPorNotaEmJavaScript;
	private String graficoPizzaOutraTurmaEmJavaScript;
	private String serieGraficoPizzaOutraTurmaEmJavaScript;
	private String graficoBarraOutraTurmaEmJavaScript;
	private String serieGraficoBarraOutraTurmaEmJavaScript;
	private Integer parametroWidthGrafico;

	public MonitorConhecimentoPBLVO getMonitorConhecimentoPBLVO() {
		if (monitorConhecimentoPBLVO == null) {
			monitorConhecimentoPBLVO = new MonitorConhecimentoPBLVO();
		}
		return monitorConhecimentoPBLVO;
	}

	public void setMonitorConhecimentoPBLVO(MonitorConhecimentoPBLVO monitorConhecimentoPBLVO) {
		this.monitorConhecimentoPBLVO = monitorConhecimentoPBLVO;
	}

	public Integer getParametroWidthGrafico() {
		if (parametroWidthGrafico == null) {
			parametroWidthGrafico = 520;
		}
		return parametroWidthGrafico;
	}

	public void setParametroWidthGrafico(Integer parametroWidthGrafico) {
		this.parametroWidthGrafico = parametroWidthGrafico;
	}

	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public Integer getCodigoTemaAssunto() {
		/*if (codigoTemaAssunto == null) {
			codigoTemaAssunto = 0;
		}*/
		return codigoTemaAssunto;
	}

	public void setCodigoTemaAssunto(Integer codigoTemaAssunto) {
		this.codigoTemaAssunto = codigoTemaAssunto;
	}

	public List<GraficoAproveitamentoAssuntoItemPBLVO> getGraficoItemPizza() {
		if (graficoItemPizza == null) {
			graficoItemPizza = new ArrayList<GraficoAproveitamentoAssuntoItemPBLVO>();
		}
		return graficoItemPizza;
	}

	public void setGraficoItemPizza(List<GraficoAproveitamentoAssuntoItemPBLVO> graficoItemPizza) {
		this.graficoItemPizza = graficoItemPizza;
	}

	public List<GraficoAproveitamentoAssuntoItemPBLVO> getGraficoItemBarra() {
		if (graficoItemBarra == null) {
			graficoItemBarra = new ArrayList<GraficoAproveitamentoAssuntoItemPBLVO>();
		}
		return graficoItemBarra;
	}

	public void setGraficoItemBarra(List<GraficoAproveitamentoAssuntoItemPBLVO> graficoItemBarra) {
		this.graficoItemBarra = graficoItemBarra;
	}

	public String getSerieGraficoPizzaEmJavaScript() {
		if (serieGraficoPizzaEmJavaScript == null) {
			serieGraficoPizzaEmJavaScript = "";
		}
		return serieGraficoPizzaEmJavaScript;
	}

	public void setSerieGraficoPizzaEmJavaScript(String serieGraficoPizzaEmJavaScript) {
		this.serieGraficoPizzaEmJavaScript = serieGraficoPizzaEmJavaScript;
	}

	public String getGraficoPizzaEmJavaScript() {
		if (graficoPizzaEmJavaScript == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"containerPizza").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 10px 20px; position: relative;left: -30px; display: inline-block;\"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsPizza").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			chart: {type: \"pie\", height : 250, spacingLeft : 0, marginTop : 40, renderTo : \"containerPizza").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Por Nível de Desempenho '},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {  enabled: false},");
			grafico.append(" 			credits: {  enabled: false},");
			//grafico.append(" 			plotOptions: {pie: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			plotOptions: {pie: {allowPointSelect:true,cursor:'pointer',point: {events: {click : function(e) {click: consultarAlunoPblGrafico(this.origem, this.codigoParametro, this.conteudo, this.turma, this.disciplina, this.ano, this.semestre, this.tema, this.cupre);}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			// grafico.append(" 			legend:{layout:'vertical',  labelFormatter: function () {return this.name + ': '+ this.y + ' Alunos';}}, ");
			grafico.append(" 			series: [{name: 'Alunos', colorByPoint: true, data: [").append(getSerieGraficoPizzaEmJavaScript()).append("]}]");
			grafico.append(" 	};");
			grafico.append(" 	var chartPizza").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsPizza").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoPizzaEmJavaScript = grafico.toString();
		}
		return graficoPizzaEmJavaScript;
	}

	public void setGraficoPizzaEmJavaScript(String graficoPizzaEmJavaScript) {
		this.graficoPizzaEmJavaScript = graficoPizzaEmJavaScript;
	}

	public String getSerieGraficoBarraEmJavaScript() {
		if (serieGraficoBarraEmJavaScript == null) {
			serieGraficoBarraEmJavaScript = "";
		}
		return serieGraficoBarraEmJavaScript;
	}

	public void setSerieGraficoBarraEmJavaScript(String serieGraficoBarraEmJavaScript) {
		this.serieGraficoBarraEmJavaScript = serieGraficoBarraEmJavaScript;
	}

	public String getCategoriaGraficoBarraEmJavaScript() {
		if (categoriaGraficoBarraEmJavaScript == null) {
			categoriaGraficoBarraEmJavaScript = "";
		}
		return categoriaGraficoBarraEmJavaScript;
	}

	public void setCategoriaGraficoBarraEmJavaScript(String categoriaGraficoBarraEmJavaScript) {
		this.categoriaGraficoBarraEmJavaScript = categoriaGraficoBarraEmJavaScript;
	}

	public void gerarGraficoBarraEmJavaScript() {
		if(Uteis.isAtributoPreenchido(getGraficoItemBarra())){
			for (GraficoAproveitamentoAssuntoItemPBLVO itemGrafico : getGraficoItemBarra()) {
				if (!Uteis.isAtributoPreenchido(getSerieGraficoBarraEmJavaScript())) {
					setSerieGraficoBarraEmJavaScript(itemGrafico.getSerieGraficoBarra());
				} else {
					setSerieGraficoBarraEmJavaScript(getSerieGraficoBarraEmJavaScript() + ", " + itemGrafico.getSerieGraficoBarra());
				}				
			}
			setCategoriaGraficoBarraEmJavaScript(getGraficoItemBarra().get(0).getCategoriaGrafico());	
		}
	}

	public void gerarGraficoBarraOutraTurmaEmJavaScript() {
		for (GraficoAproveitamentoAssuntoItemPBLVO itemGrafico : getGraficoItemBarra()) {  
			if (!Uteis.isAtributoPreenchido(getSerieGraficoBarraOutraTurmaEmJavaScript())) {
				setSerieGraficoBarraOutraTurmaEmJavaScript(itemGrafico.getSerieGraficoBarra());
			} else {
				setSerieGraficoBarraOutraTurmaEmJavaScript(getSerieGraficoBarraOutraTurmaEmJavaScript() + ", " + itemGrafico.getSerieGraficoBarra());
			}
		}
	}
	public void gerarGraficoBarraPorNotaEmJavaScript() {
		if(Uteis.isAtributoPreenchido(getGraficoItemBarra())){
			for (GraficoAproveitamentoAssuntoItemPBLVO itemGrafico : getGraficoItemBarra()) {
				if (!Uteis.isAtributoPreenchido(getSerieGraficoBarraPorNotaEmJavaScript())) {
					setSerieGraficoBarraPorNotaEmJavaScript(itemGrafico.getSerieGraficoBarra());
				} else {
					setSerieGraficoBarraPorNotaEmJavaScript(getSerieGraficoBarraPorNotaEmJavaScript() + ", " + itemGrafico.getSerieGraficoBarra());
				}
				
			}
			setCategoriaGraficoBarraPorNotaEmJavaScript(getGraficoItemBarra().get(0).getCategoriaGrafico());	
		}
		
		
	}

	public String getGraficoBarraEmJavaScript() {
		if (graficoBarraEmJavaScript == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"containerBarra").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 0 auto;position: relative;left: 0px; display: inline-block; \"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsBarra").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			credits: {  enabled: false}, ");
			grafico.append(" 			chart: {type: \"column\", height : 250, spacingLeft : 0, marginTop : 40, renderTo : \"containerBarra").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Por Problema(PBL)'},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' +'<td style=\"padding:0\"><b>{point.y} Alunos</b></td></tr>',footerFormat: '</table>',shared: true,useHTML: true},");
			grafico.append(" 			xAxis: { categories:[").append(getCategoriaGraficoBarraEmJavaScript()).append("], crosshair: true},");
			grafico.append(" 			yAxis: { min: 0 , title: {text: ''}},");
			/*grafico.append(" 			plotOptions: {column: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");*/
			grafico.append(" 			plotOptions: {column: { pointPadding: 0.2,borderWidth: 0}, series: {allowPointSelect:true,cursor:'pointer', point:{events:{click:function(e){click:consultarAlunoPblGrafico(this.options.origem, this.options.codigoParametro, this.options.conteudo, this.options.turma, this.options.disciplina, this.options.ano, this.options.semestre, this.options.tema,this.options.cupre );}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			series: [").append(getSerieGraficoBarraEmJavaScript()).append("]");
			grafico.append(" 	};");
			grafico.append(" 	var chartBarra").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsBarra").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoBarraEmJavaScript = grafico.toString();
		}
		return graficoBarraEmJavaScript;
	}

	public void setGraficoBarraEmJavaScript(String graficoBarraEmJavaScript) {
		this.graficoBarraEmJavaScript = graficoBarraEmJavaScript;
	}

	public String getGraficoBarraPorNotaEmJavaScript() {
		if (graficoBarraPorNotaEmJavaScript == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"containerBarraPorNota").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 0 auto;position: relative;left: 0px; display: inline-block; \"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsBarraPorNota").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			credits: {  enabled: false},");
			grafico.append(" 			chart: {type: \"column\", height : 400, spacingLeft : 0, marginTop : 40, renderTo : \"containerBarraPorNota").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Por Tipo de Nota'},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' +'<td style=\"padding:0\"><b>{point.y} Alunos</b></td></tr>',footerFormat: '</table>',shared: true,useHTML: true},");
			grafico.append(" 			xAxis: { categories:[").append(getCategoriaGraficoBarraPorNotaEmJavaScript()).append("], crosshair: true},");
			grafico.append(" 			yAxis: { min: 0 , title: {text: ''}},");
			//grafico.append(" 			plotOptions: {column: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			plotOptions: {column: { pointPadding: 0.2,borderWidth: 0}, series: {allowPointSelect:true,cursor:'pointer', point:{events:{click:function(e){click:consultarAlunoPblGrafico(this.options.origem, this.options.codigoParametro, this.options.conteudo, this.options.turma, this.options.disciplina, this.options.ano, this.options.semestre, this.options.tema,this.options.cupre );}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			series: [").append(getSerieGraficoBarraPorNotaEmJavaScript()).append("]");
			grafico.append(" 	};");
			grafico.append(" 	var chartBarraPorNota").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsBarraPorNota").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoBarraPorNotaEmJavaScript = grafico.toString();
		}
		return graficoBarraPorNotaEmJavaScript;
	}

	public void setGraficoBarraPorNotaEmJavaScript(String graficoBarraPorNotaEmJavaScript) {
		this.graficoBarraPorNotaEmJavaScript = graficoBarraPorNotaEmJavaScript;
	}

	public String getSerieGraficoBarraPorNotaEmJavaScript() {
		if (serieGraficoBarraPorNotaEmJavaScript == null) {
			serieGraficoBarraPorNotaEmJavaScript = "";
		}
		return serieGraficoBarraPorNotaEmJavaScript;
	}

	public void setSerieGraficoBarraPorNotaEmJavaScript(String serieGraficoBarraPorNotaEmJavaScript) {
		this.serieGraficoBarraPorNotaEmJavaScript = serieGraficoBarraPorNotaEmJavaScript;
	}

	public String getCategoriaGraficoBarraPorNotaEmJavaScript() {
		if (categoriaGraficoBarraPorNotaEmJavaScript == null) {
			categoriaGraficoBarraPorNotaEmJavaScript = "";
		}
		return categoriaGraficoBarraPorNotaEmJavaScript;
	}

	public void setCategoriaGraficoBarraPorNotaEmJavaScript(String categoriaGraficoBarraPorNotaEmJavaScript) {
		this.categoriaGraficoBarraPorNotaEmJavaScript = categoriaGraficoBarraPorNotaEmJavaScript;
	}

	public String getGraficoPizzaOutraTurmaEmJavaScript() {
		if (graficoPizzaOutraTurmaEmJavaScript == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"containerPizzaTurma").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 10px 20px; position: relative;left: -30px; display: inline-block;\"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsPizzaTurma").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			credits: {  enabled: false},");
			grafico.append(" 			chart: {type: \"pie\", height : 250, spacingLeft : 0, marginTop : 40, renderTo : \"containerPizzaTurma").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Nível de Desempenho da Turma '},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {  enabled: false},");
		//	grafico.append(" 			plotOptions: {pie: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			plotOptions: {pie: {allowPointSelect:true,cursor:'pointer',point: {events: {click : function(e) {click: consultarAlunoPblGrafico(this.origem, this.codigoParametro, this.conteudo, this.turma, this.disciplina, this.ano, this.semestre, this.tema, this.cupre);}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			// grafico.append(" 			legend:{layout:'vertical',  labelFormatter: function () {return this.name + ': '+ this.y + ' Alunos';}}, ");
			grafico.append(" 			series: [{name: 'Alunos', colorByPoint: true, data: [").append(getSerieGraficoPizzaEmJavaScript()).append("]}]");
			grafico.append(" 	};");
			grafico.append(" 	var chartPizzaTurma").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsPizzaTurma").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			grafico.append("<div id=\"containerPizzaOutraTurma").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 10px 20px; position: relative;left: -30px; display: inline-block;\"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsPizzaOutraTurma").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			credits: {  enabled: false},");
			grafico.append(" 			chart: {type: \"pie\", height : 250, spacingLeft : 0, marginTop : 40, renderTo : \"containerPizzaOutraTurma").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Nível Desempenho Outras Turmas'},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {  enabled: false},");
		//	grafico.append(" 			plotOptions: {pie: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			plotOptions: {pie: {allowPointSelect:true,cursor:'pointer',point: {events: {click : function(e) {click: consultarAlunoPblGrafico(this.origem, this.codigoParametro, this.conteudo, this.turma, this.disciplina, this.ano, this.semestre, this.tema, this.cupre);}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			// grafico.append(" 			legend:{layout:'vertical',  labelFormatter: function () {return this.name + ': '+ this.y + ' Alunos';}}, ");
			grafico.append(" 			series: [{name: 'Alunos', colorByPoint: true, data: [").append(getSerieGraficoPizzaOutraTurmaEmJavaScript()).append("]}]");
			grafico.append(" 	};");
			grafico.append(" 	var chartPizzaOutraTurma").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsPizzaOutraTurma").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoPizzaOutraTurmaEmJavaScript = grafico.toString();
		}
		return graficoPizzaOutraTurmaEmJavaScript;
	}

	public void setGraficoPizzaOutraTurmaEmJavaScript(String graficoPizzaOutraTurmaEmJavaScript) {
		this.graficoPizzaOutraTurmaEmJavaScript = graficoPizzaOutraTurmaEmJavaScript;
	}

	public String getSerieGraficoPizzaOutraTurmaEmJavaScript() {
		if (serieGraficoPizzaOutraTurmaEmJavaScript == null) {
			serieGraficoPizzaOutraTurmaEmJavaScript = "";
		}
		return serieGraficoPizzaOutraTurmaEmJavaScript;
	}

	public void setSerieGraficoPizzaOutraTurmaEmJavaScript(String serieGraficoPizzaOutraTurmaEmJavaScript) {
		this.serieGraficoPizzaOutraTurmaEmJavaScript = serieGraficoPizzaOutraTurmaEmJavaScript;
	}

	public String getGraficoBarraOutraTurmaEmJavaScript() {
		if (graficoBarraOutraTurmaEmJavaScript == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"containerBarraTurma").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 0 auto;position: relative;left: 0px; display: inline-block; \"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsBarraTurma").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			credits: {  enabled: false},");
			grafico.append(" 			chart: {type: \"column\", height : 250, spacingLeft : 0, marginTop : 40, renderTo : \"containerBarraTurma").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Por Problema(PBL) da Turma'},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' +'<td style=\"padding:0\"><b>{point.y} Alunos</b></td></tr>',footerFormat: '</table>',shared: true,useHTML: true},");
			grafico.append(" 			xAxis: { categories:[").append(getCategoriaGraficoBarraEmJavaScript()).append("], crosshair: true},");
			grafico.append(" 			yAxis: { min: 0 , title: {text: ''}},");
//			grafico.append(" 			plotOptions: {column: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			plotOptions: {column: { pointPadding: 0.2,borderWidth: 0}, series: {allowPointSelect:true,cursor:'pointer', point:{events:{click:function(e){click:consultarAlunoPblGrafico(this.options.origem, this.options.codigoParametro, this.options.conteudo, this.options.turma, this.options.disciplina, this.options.ano, this.options.semestre, this.options.tema,this.options.cupre );}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			series: [").append(getSerieGraficoBarraEmJavaScript()).append("]");
			grafico.append(" 	};");
			grafico.append(" 	var chartBarraTurma").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsBarraTurma").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");
			grafico.append("<div id=\"containerBarraOutraTurma").append(getCodigoTemaAssunto()).append("\" style=\"width: " + getParametroWidthGrafico() + "px; margin: 0 auto;position: relative;left: 0px; display: inline-block; \"> </div>");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var optionsBarraOutraTurma").append(getCodigoTemaAssunto()).append(" = {");
			grafico.append(" 			credits: {  enabled: false},");
			grafico.append(" 			chart: {type: \"column\", height : 250, spacingLeft : 0, marginTop : 40, renderTo : \"containerBarraOutraTurma").append(getCodigoTemaAssunto()).append("\"},");
			grafico.append(" 			title: {text: 'Por Problema(PBL) Outras Turmas'},");
			grafico.append(" 			exporting : {type : 'image/jpeg',buttons : {exportButton : {y : 5},printButton : {y : 5}}},");
			grafico.append(" 			tooltip: {headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' +'<td style=\"padding:0\"><b>{point.y} Alunos</b></td></tr>',footerFormat: '</table>',shared: true,useHTML: true},");
			grafico.append(" 			xAxis: { categories:[").append(getCategoriaGraficoBarraEmJavaScript()).append("], crosshair: true},");
			grafico.append(" 			yAxis: { min: 0 , title: {text: ''}},");
		//	grafico.append(" 			plotOptions: {column: {allowPointSelect:true,cursor:'pointer', dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			plotOptions: {column: { pointPadding: 0.2,borderWidth: 0}, series: {allowPointSelect:true,cursor:'pointer', point:{events:{click:function(e){click:consultarAlunoPblGrafico(this.options.origem, this.options.codigoParametro, this.options.conteudo, this.options.turma, this.options.disciplina, this.options.ano, this.options.semestre, this.options.tema,this.options.cupre );}}},  dataLabels:{enabled: true,format:'<b>{point.y}</b>'}, showInLegend: true}}, ");
			grafico.append(" 			series: [").append(getSerieGraficoBarraOutraTurmaEmJavaScript()).append("]");
			grafico.append(" 	};");
			grafico.append(" 	var chartBarraOutraTurma").append(getCodigoTemaAssunto()).append(" = new Highcharts.Chart(optionsBarraOutraTurma").append(getCodigoTemaAssunto()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoBarraOutraTurmaEmJavaScript = grafico.toString();
		}

		return graficoBarraOutraTurmaEmJavaScript;
	}

	public void setGraficoBarraOutraTurmaEmJavaScript(String graficoBarraOutraTurmaEmJavaScript) {
		this.graficoBarraOutraTurmaEmJavaScript = graficoBarraOutraTurmaEmJavaScript;
	}

	public String getSerieGraficoBarraOutraTurmaEmJavaScript() {
		if (serieGraficoBarraOutraTurmaEmJavaScript == null) {
			serieGraficoBarraOutraTurmaEmJavaScript = "";
		}
		return serieGraficoBarraOutraTurmaEmJavaScript;
	}

	public void setSerieGraficoBarraOutraTurmaEmJavaScript(String serieGraficoBarraOutraTurmaEmJavaScript) {
		this.serieGraficoBarraOutraTurmaEmJavaScript = serieGraficoBarraOutraTurmaEmJavaScript;
	}

}
