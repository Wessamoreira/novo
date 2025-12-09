package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 30/03/2015
 *
 */
public class MonitorConhecimentoVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO;
	private List<ParametrosGraficoComparativoAvaliacoesOnlinesVO> graficoComparativoAvaliacoesOnlinesVOs;
	private String graficoComparativoAvaliacoesOnlines;
	private Integer parametroWidthGraficoComparativoAvaliacoesOnlines;
	private String graficoComparativoMeusColegas;
	private List<ParametrosGraficoComparativoMeusColegasVO> parametrosGraficoComparativoMeusColegasVOs;
	private Integer parametroWidthGraficoComparativoMeusColegas;


	public GraficoAproveitamentoAvaliacaoVO getGraficoAproveitamentoAvaliacaoVO() {
		if (graficoAproveitamentoAvaliacaoVO == null) {
			graficoAproveitamentoAvaliacaoVO = new GraficoAproveitamentoAvaliacaoVO();
		}
		return graficoAproveitamentoAvaliacaoVO;
	}

	public void setGraficoAproveitamentoAvaliacaoVO(GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO) {
		this.graficoAproveitamentoAvaliacaoVO = graficoAproveitamentoAvaliacaoVO;
	}

	public List<ParametrosGraficoComparativoAvaliacoesOnlinesVO> getGraficoComparativoAvaliacoesOnlinesVOs() {
		if (graficoComparativoAvaliacoesOnlinesVOs == null) {
			graficoComparativoAvaliacoesOnlinesVOs = new ArrayList<ParametrosGraficoComparativoAvaliacoesOnlinesVO>();
		}
		return graficoComparativoAvaliacoesOnlinesVOs;
	}

	public void setGraficoComparativoAvaliacoesOnlinesVOs(List<ParametrosGraficoComparativoAvaliacoesOnlinesVO> graficoComparativoAvaliacoesOnlinesVOs) {
		this.graficoComparativoAvaliacoesOnlinesVOs = graficoComparativoAvaliacoesOnlinesVOs;
	}

	public String getGraficoComparativoAvaliacoesOnlines() {
		if (graficoComparativoAvaliacoesOnlines == null) {
			StringBuilder grafico = new StringBuilder();
			int id = (int) (Math.random() * 150000);
			grafico.append("<div id=\"container").append(id).append("\" style=\"width: " + getParametroWidthGraficoComparativoAvaliacoesOnlines() + "px; height: 270px; margin: 0 auto\" /> ");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var options").append(id).append(" = {");
			grafico.append(" 		chart: {type: \"line\", renderTo : \"container").append(id).append("\", marginTop:5, zoomType : 'x'},");
			grafico.append("		title: {");
			grafico.append("            text: ' ',");
			grafico.append("            x: -20");
			grafico.append("        },");
			grafico.append("        xAxis: {");
			grafico.append("            type : 'datetime'");
			grafico.append("        },");
			grafico.append(" 		credits : {enabled : false},");
			grafico.append(" 		exporting : {enabled : false},");
			grafico.append("        yAxis: {");
			grafico.append("            title: {");
			grafico.append("                text: 'Percentagem (%)'");
			grafico.append("            }, max: 100, min: 0, ");
			grafico.append("            plotLines: [{");
			grafico.append("                value: 0,");
			grafico.append("                width: 1,");
			grafico.append("                color: '#808080'");
			grafico.append("            }]");
			grafico.append("        },");
			grafico.append("        tooltip: {");
			grafico.append("            formatter : function() {");
			grafico.append(" 			return '' + (Highcharts.dateFormat('%e/ %b', this.x)) + ' / ' + Highcharts.dateFormat('%Y', this.x) + ' - ' + Highcharts.numberFormat((this.y), 0, ',', '.') + '%';}");
			grafico.append("        },");
			grafico.append("        legend: {");
			grafico.append("            enabled: false");
			grafico.append("        },");
			grafico.append("        series: [{");
			grafico.append("            name: 'Avaliação On-line',");
			Boolean virgula = false;
			grafico.append("            data: [");
			for (ParametrosGraficoComparativoAvaliacoesOnlinesVO graficoComparativoAvaliacoesOnlinesVO : getGraficoComparativoAvaliacoesOnlinesVOs()) {
				if (virgula) {
					grafico.append(", ");
				}
				Calendar cal = new GregorianCalendar();
				cal.setTime(graficoComparativoAvaliacoesOnlinesVO.getDataTermino());
				grafico.append("[Date.UTC(").append(cal.get(Calendar.YEAR) + ", ").append(cal.get(Calendar.MONTH) + ", ").append(cal.get(Calendar.DAY_OF_MONTH) + "),").append(graficoComparativoAvaliacoesOnlinesVO.getPercentuaisEvolucaoAvaliacoesOnline());
				virgula = true;
				grafico.append("]");
			}
			grafico.append("]");
			grafico.append("		}]");
			grafico.append(" 	};");
			grafico.append(" 	var chart").append(id).append(" = new Highcharts.Chart(options").append(id).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");
			graficoComparativoAvaliacoesOnlines = grafico.toString();
		}
		return graficoComparativoAvaliacoesOnlines;
	}

	public void setGraficoComparativoAvaliacoesOnlines(String graficoComparativoAvaliacoesOnlines) {
		this.graficoComparativoAvaliacoesOnlines = graficoComparativoAvaliacoesOnlines;
	}

	public Integer getParametroWidthGraficoComparativoAvaliacoesOnlines() {
		if (parametroWidthGraficoComparativoAvaliacoesOnlines == null) {
			parametroWidthGraficoComparativoAvaliacoesOnlines = 0;
		}
		return parametroWidthGraficoComparativoAvaliacoesOnlines;
	}

	public void setParametroWidthGraficoComparativoAvaliacoesOnlines(Integer parametroWidthGraficoComparativoAvaliacoesOnlines) {
		this.parametroWidthGraficoComparativoAvaliacoesOnlines = parametroWidthGraficoComparativoAvaliacoesOnlines;
	}

	public String getGraficoComparativoMeusColegas() {
		if (graficoComparativoMeusColegas == null) {
			StringBuilder grafico = new StringBuilder();
			Integer id = (int) (Math.random() * 150000);
			grafico.append("<div id=\"container").append(id).append("\" style=\"width: " + getParametroWidthGraficoComparativoMeusColegas() + "px; height: 270px; margin: 0 auto;right: -13px;position: relative;\" /> ");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var options").append(id).append(" = {");
			grafico.append(" 		chart: {type: \"column\", renderTo : \"container").append(id).append("\", marginTop:5},");
			grafico.append("        title: {");
			grafico.append("            text: ' '");
			grafico.append("        },");
			grafico.append("        subtitle: {");
			grafico.append("            text: ' '");
			grafico.append("        },");
			grafico.append(" 		credits : {enabled : false},");
			grafico.append(" 		exporting : {enabled : false},");
			grafico.append("        xAxis: {");
			grafico.append("            categories: [");
			grafico.append("                'Turma',");
			grafico.append("                'Curso',");
			grafico.append("                'Todas Unidades',");
			grafico.append("            ],");
			grafico.append("            crosshair: true");
			grafico.append("        },");
			grafico.append("        yAxis: {");
			grafico.append("            min: 0,");
			grafico.append("                max: 100,");
			grafico.append("            title: {");
			grafico.append("                text: 'Percentagem (%)'");
			grafico.append("            }");
			grafico.append("        },");
			grafico.append("        tooltip: {");
			grafico.append("            headerFormat: '<span style=\"font-size:10px\">{point.key}</span><table>',");
			grafico.append("            pointFormat: '<tr><td style=\"color:{series.color};padding:0\">{series.name}: </td>' + ");
			grafico.append("                '<td style=\"padding:0\"><b>{point.y} %</b></td></tr>',");
			grafico.append("            footerFormat: '</table>',");
			grafico.append("            shared: true,");
			grafico.append("            useHTML: true");
			grafico.append("        },");
			grafico.append("        plotOptions: {");
			grafico.append("            column: {");
			grafico.append("                pointPadding: 0.2,");
			grafico.append("                borderWidth: 0");
			grafico.append("            }");
			grafico.append("        },");
			grafico.append("        series: [{");
			grafico.append("            name: 'Eu',");
			grafico.append("            data: [").append(getGraficoAproveitamentoAvaliacaoVO().getPercentualAcerto()).append(",").append(getGraficoAproveitamentoAvaliacaoVO().getPercentualAcerto()).append(",").append(getGraficoAproveitamentoAvaliacaoVO().getPercentualAcerto()).append("]");
			grafico.append("        }, {");
			grafico.append("            name: 'Meus Colegas',");
			Boolean virgula = false;
			grafico.append("            data: [");
			for (ParametrosGraficoComparativoMeusColegasVO parametrosGraficoComparativoMeusColegasVO : getParametrosGraficoComparativoMeusColegasVOs()) {
				if (virgula) {
					grafico.append(", ");
				}
				grafico.append(parametrosGraficoComparativoMeusColegasVO.getPercentualAcertou());
				virgula = true;
			}
			grafico.append("]");			
			grafico.append("        }]");
			grafico.append(" 	};");
			grafico.append(" 	var chart").append(id).append(" = new Highcharts.Chart(options").append(id).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");
			graficoComparativoMeusColegas = grafico.toString();
		}
		return graficoComparativoMeusColegas;
	}

	public void setGraficoComparativoMeusColegas(String graficoComparativoMeusColegas) {
		this.graficoComparativoMeusColegas = graficoComparativoMeusColegas;
	}

	public List<ParametrosGraficoComparativoMeusColegasVO> getParametrosGraficoComparativoMeusColegasVOs() {
		if(parametrosGraficoComparativoMeusColegasVOs == null) {
			parametrosGraficoComparativoMeusColegasVOs = new ArrayList<ParametrosGraficoComparativoMeusColegasVO>();
		}
		return parametrosGraficoComparativoMeusColegasVOs;
	}

	public void setParametrosGraficoComparativoMeusColegasVOs(List<ParametrosGraficoComparativoMeusColegasVO> parametrosGraficoComparativoMeusColegasVOs) {
		this.parametrosGraficoComparativoMeusColegasVOs = parametrosGraficoComparativoMeusColegasVOs;
	}

	public Integer getParametroWidthGraficoComparativoMeusColegas() {
		if(parametroWidthGraficoComparativoMeusColegas == null) {
			parametroWidthGraficoComparativoMeusColegas = 0;
		}
		return parametroWidthGraficoComparativoMeusColegas;
	}

	public void setParametroWidthGraficoComparativoMeusColegas(Integer parametroWidthGraficoComparativoMeusColegas) {
		this.parametroWidthGraficoComparativoMeusColegas = parametroWidthGraficoComparativoMeusColegas;
	}
}
