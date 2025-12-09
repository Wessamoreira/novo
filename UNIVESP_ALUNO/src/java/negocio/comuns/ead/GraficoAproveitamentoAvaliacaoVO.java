package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;


public class GraficoAproveitamentoAvaliacaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String assunto;
	private Integer codigoTemaAssunto;
	private Integer quantidadeQuestoes;
	private Integer quantidadeAcertos;
	private Integer quantidadeErros;
	private Double percentualAcerto;
	private String descricaoParametro;
	private String corGrafico;
	private String corLetraGrafico;
	public String graficoAproveitamento;
	private Integer ordem;
	private Boolean geral;
	private String cssNivelAproveitamento;
	private Integer parametroWidthGrafico;
	private Integer codigoAvaliacaoOnlineMatricula;
	private Double pontos;

	public GraficoAproveitamentoAvaliacaoVO() {

	}

	public String getAssunto() {
		if(assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public Integer getQuantidadeQuestoes() {
		if(quantidadeQuestoes == null) {
			quantidadeQuestoes = 0;
		}
		return quantidadeQuestoes;
	}

	public void setQuantidadeQuestoes(Integer quantidadeQuestoes) {
		this.quantidadeQuestoes = quantidadeQuestoes;
	}

	public Integer getQuantidadeAcertos() {
		if(quantidadeAcertos == null) {
			quantidadeAcertos = 0;
		}
		return quantidadeAcertos;
	}

	public void setQuantidadeAcertos(Integer quantidadeAcertos) {
		this.quantidadeAcertos = quantidadeAcertos;
	}

	public Integer getQuantidadeErros() {
		if(quantidadeErros == null) {
			quantidadeErros = 0;
		}
		return quantidadeErros;
	}

	public void setQuantidadeErros(Integer quantidadeErros) {
		this.quantidadeErros = quantidadeErros;
	}

	public Double getPercentualAcerto() {
		if(percentualAcerto == null) {
			percentualAcerto = 0.0;
		}
		return percentualAcerto;
	}

	public void setPercentualAcerto(Double percentualAcerto) {
		this.percentualAcerto = percentualAcerto;
	}

	public String getDescricaoParametro() {
		if(descricaoParametro == null) {
			descricaoParametro = "";
		}
		return descricaoParametro;
	}

	public void setDescricaoParametro(String descricaoParametro) {
		this.descricaoParametro = descricaoParametro;
	}

	public String getCorGrafico() {
		if(corGrafico == null) {
			corGrafico = "";
		}
		return corGrafico;
	}

	public void setCorGrafico(String corGrafico) {
		this.corGrafico = corGrafico;
	}

	public String getCorLetraGrafico() {
		if(corLetraGrafico == null) {
			corLetraGrafico = "";
		}
		return corLetraGrafico;
	}

	public void setCorLetraGrafico(String corLetraGrafico) {
		this.corLetraGrafico = corLetraGrafico;
	}
	
	public String getGraficoAproveitamento() {
		if (graficoAproveitamento == null) {
			StringBuilder grafico = new StringBuilder();
			grafico.append("<div id=\"container").append((getGeral()? "G" : "") + getOrdem()).append("\" style=\"width: 100%; height: 65px;\" /> ");
			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
//			grafico.append("     $.ajax({cache : false});");
			grafico.append("	var options").append((getGeral()? "G" : "") +getOrdem()).append(" = {");
			grafico.append(" 			chart: {type: \"bar\", renderTo : \"container").append((getGeral()? "G" : "") + getOrdem()).append("\", marginTop:0, marginRight:45},");
			grafico.append(" 			title: {text: ' '},");
			grafico.append(" 			credits : {enabled : false},");
			grafico.append(" 			exporting : {enabled : false},");
			grafico.append("            xAxis: { categories: [' ']},");
			grafico.append("			yAxis: { min: 0, max: 100, title: {text: ' '}, stackLabels: {style: {color: 'black'}, enabled: true, formatter: function () {return "+getPontos()+" + \" pts.\";} }},");
			grafico.append(" 			legend: { enabled: false}, ");
			grafico.append(" 			plotOptions: {bar: {stacking: 'percent'}}, ");
			grafico.append(" 			tooltip: { ");
			grafico.append(" 			pointFormat: '").append("<span style=\"color:{series.color};\">{series.name}</span>', ");
			grafico.append(" 			shared: false,  useHTML: true, valueSuffix : '%'}, ");
			grafico.append(" 			series: [");
			grafico.append("  			{ name: '").append("Ruim"+" "+(100 - getPercentualAcerto())+" %").append("', ").append("color: '#AA4643', ").append("data: [").append(100 - getPercentualAcerto()).append("]}");
			grafico.append("			, { name: '").append(getDescricaoParametro()+" "+(getPercentualAcerto())+" % - "+getPontos()+" pts. ").append("', ").append("color: '#4572A7', ").append("data: [").append(getPercentualAcerto()).append("]}");
			grafico.append("			]");
			grafico.append(" 	};");
			grafico.append(" 	var chart").append((getGeral()? "G" : "") + getOrdem()).append(" = new Highcharts.Chart(options").append((getGeral()? "G" : "") + getOrdem()).append(");");
			grafico.append(" 	}(jQuery));");
			grafico.append("</script>");

			graficoAproveitamento = grafico.toString();
		}
		return graficoAproveitamento;
	}
	
	public void setGraficoAproveitamento(String graficoAproveitamento) {
		this.graficoAproveitamento = graficoAproveitamento;
	}

	public Integer getOrdem() {
		if(ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Boolean getGeral() {
		if(geral == null) {
			geral = false;
		}
		return geral;
	}

	public void setGeral(Boolean geral) {
		this.geral = geral;
	}

	public String getCssNivelAproveitamento() {
		if(cssNivelAproveitamento == null) {
			cssNivelAproveitamento = "display: inline-block;background-color: "+getCorGrafico()+"; color: "+getCorLetraGrafico()+"; padding: 5px;width:93%;font-size: 12px;font-weight: bold;";
		}
		return cssNivelAproveitamento;
	}

	public void setCssNivelAproveitamento(String cssNivelAproveitamento) {
		this.cssNivelAproveitamento = cssNivelAproveitamento;
	}

	public Integer getParametroWidthGrafico() {
		if(parametroWidthGrafico == null) {
			parametroWidthGrafico = 0;
		}
		return parametroWidthGrafico;
	}

	public void setParametroWidthGrafico(Integer parametroWidthGrafico) {
		this.parametroWidthGrafico = parametroWidthGrafico;
	}

	public Integer getCodigoTemaAssunto() {
		if(codigoTemaAssunto == null) {
			codigoTemaAssunto = 0;
		}
		return codigoTemaAssunto;
	}

	public void setCodigoTemaAssunto(Integer codigoTemaAssunto) {
		this.codigoTemaAssunto = codigoTemaAssunto;
	}

	public Integer getCodigoAvaliacaoOnlineMatricula() {
		if(codigoAvaliacaoOnlineMatricula == null) {
			codigoAvaliacaoOnlineMatricula = 0;
		}
		return codigoAvaliacaoOnlineMatricula;
	}

	public void setCodigoAvaliacaoOnlineMatricula(Integer codigoAvaliacaoOnlineMatricula) {
		this.codigoAvaliacaoOnlineMatricula = codigoAvaliacaoOnlineMatricula;
	}

	public Double getPontos() {
		if(pontos == null) {
			pontos = 0.0;
		}
		return pontos;
	}

	public void setPontos(Double pontos) {
		this.pontos = pontos;
	}
}
