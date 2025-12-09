package negocio.comuns.segmentacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.enumeradores.TipoLayoutApresentacaoResultadoSegmentacaoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class SegmentacaoProspectVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Integer codigo;
	private String descricao;
	private List<SegmentacaoOpcaoVO> segmentacaoOpcaoVOs;
	private String ativaSegmentacao;
	private Boolean verificaMudancaDescricaoSegmentacao;
	private Integer quantidadeTotal;
	private TipoLayoutApresentacaoResultadoSegmentacaoEnum tipoLayoutApresentacaoResultadoSegmentacaoEnum;

	/* Atributo para controle de Tela */
	private SegmentacaoOpcaoVO segmentacaoOpcao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}

		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {

		if (descricao == null) {
			descricao = "";
		}

		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<SegmentacaoOpcaoVO> getSegmentacaoOpcaoVOs() {
		if (segmentacaoOpcaoVOs == null) {
			segmentacaoOpcaoVOs = new ArrayList<SegmentacaoOpcaoVO>(0);
		}

		return segmentacaoOpcaoVOs;
	}

	public void setSegmentacaoOpcaoVOs(List<SegmentacaoOpcaoVO> segmentacaoOpcaoVOs) {
		this.segmentacaoOpcaoVOs = segmentacaoOpcaoVOs;
	}

	public SegmentacaoOpcaoVO getSegmentacaoOpcao() {

		if (segmentacaoOpcao == null) {
			segmentacaoOpcao = new SegmentacaoOpcaoVO();
		}

		return segmentacaoOpcao;
	}

	public void setSegmentacaoOpcao(SegmentacaoOpcaoVO segmentacaoOpcao) {
		this.segmentacaoOpcao = segmentacaoOpcao;
	}

	public String getAtivaSegmentacao() {
		if (ativaSegmentacao == null) {
			ativaSegmentacao = "";
		}

		return ativaSegmentacao;
	}

	public void setAtivaSegmentacao(String ativaSegmentacao) {
		this.ativaSegmentacao = ativaSegmentacao;
	}

	public Boolean getVerificaMudancaDescricaoSegmentacao() {

		if (verificaMudancaDescricaoSegmentacao == null) {
			verificaMudancaDescricaoSegmentacao = Boolean.FALSE;
		}

		return verificaMudancaDescricaoSegmentacao;
	}

	public void setVerificaMudancaDescricaoSegmentacao(Boolean verificaMudancaDescricaoSegmentacao) {
		this.verificaMudancaDescricaoSegmentacao = verificaMudancaDescricaoSegmentacao;
	}

	public List<SelectItem> listaComboBoxSegmentacaoOpcao;

	public List<SelectItem> getListaComboBoxSegmentacaoOpcao() throws Exception {
		if (listaComboBoxSegmentacaoOpcao == null) {
			List itens = new ArrayList(0);
			itens.add(new SelectItem("", ""));
			for (SegmentacaoOpcaoVO segmentacao : getSegmentacaoOpcaoVOs()) {
				itens.add(new SelectItem(segmentacao.getCodigo(), segmentacao.getDescricao()));
			}
			listaComboBoxSegmentacaoOpcao = itens;
		}
		return listaComboBoxSegmentacaoOpcao;
	}

	public Integer getQuantidadeTotal() {
		if (quantidadeTotal == null) {
			quantidadeTotal = 0;
			for (SegmentacaoOpcaoVO segmentacaoOpcaoVO : getSegmentacaoOpcaoVOs()) {
				quantidadeTotal += segmentacaoOpcaoVO.getQuantidade();
			}
		}
		return quantidadeTotal;
	}

	public void setQuantidadeTotal(Integer quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	private StringBuilder graficoBarra;

	public String getGraficoBarra() {
		if (graficoBarra == null) {
			graficoBarra = new StringBuilder();
			graficoBarra.append("<div id=\"container").append(getCodigo()).append("\" class=\"col-md-12\"  /> ");
			graficoBarra.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			graficoBarra.append("	(function($) { ");
			graficoBarra.append("	$.ajaxSetup({");
			graficoBarra.append("		cache : false");
			graficoBarra.append("	});");
			graficoBarra.append("		var options").append(getCodigo()).append(" = {");
			graficoBarra.append(" 			chart : {");
			graficoBarra.append(" 				type : \"bar\",");
			graficoBarra.append(" 				renderTo : \"container").append(getCodigo()).append("\",");
			graficoBarra.append(" 				backgroundColor : \"#FFF\", ");
			graficoBarra.append(" 			},");
			graficoBarra.append(" 			title : {");
			graficoBarra.append(" 				text : ''");
			graficoBarra.append(" 			},");
			graficoBarra.append(" 		credits : {enabled : false},");
			graficoBarra.append(" 		exporting : {enabled : false},");
			graficoBarra.append(" 			tooltip : {");
			graficoBarra.append(" 				pointFormat : '<b>{series.name}</b>'");
			graficoBarra.append(" 			},");
			graficoBarra.append(" 			xAxis:{");
			graficoBarra.append(" 				categories: [ ' ' ], ");
			graficoBarra.append(" 			},");
			graficoBarra.append(" 			yAxis: {");
			graficoBarra.append(" 				allowDecimals: false,");
			graficoBarra.append(" 				title:{");
			graficoBarra.append(" 					text:''");
			graficoBarra.append(" 				},");
			graficoBarra.append(" 				marginLeft:0");
			graficoBarra.append(" 			},");
			graficoBarra.append(" 			plotOptions : {");
			graficoBarra.append(" 				series : {");
			graficoBarra.append("					cursor: 'pointer',");
			graficoBarra.append("					dataLabels:{");
			graficoBarra.append("						enabled : true,");
			graficoBarra.append("						format : '{point.y}'");
			graficoBarra.append("					},");
			graficoBarra.append("					point : {");
			graficoBarra.append("						events : {");
			graficoBarra.append("							click : function (event) {");			
			graficoBarra.append("								selecionarLegendaPorSegmentacao(this.options.id);");
			graficoBarra.append("							}");			
			graficoBarra.append("					}");
			graficoBarra.append(" 				},");
			graficoBarra.append(" 				showInLegend : true");
			graficoBarra.append(" 			}");
			graficoBarra.append(" 		},");
			graficoBarra.append("		legend : {");
			graficoBarra.append("			layout : 'vertical',");
			graficoBarra.append("			align: 'center',");
			graficoBarra.append("			verticalAlign: 'bottom',");
			graficoBarra.append("			itemWidth: 400,");
			graficoBarra.append("			maxHeight: 120,");
			graficoBarra.append("			labelFormatter : function() {");
			graficoBarra.append("					return this.name;");
			graficoBarra.append("			}");
			graficoBarra.append("		}, ");
			graficoBarra.append(" 		series : [ ");
			Boolean virgula = false;			
			for (SegmentacaoOpcaoVO obj : getSegmentacaoOpcaoVOs()) {
				if (virgula) {
					graficoBarra.append(", ");
				}
				BigDecimal bd = new BigDecimal(obj.getPercentual()).setScale(2, RoundingMode.HALF_EVEN);
				graficoBarra.append("	{name : '").append(obj.getDescricao() + " - " + obj.getQuantidade() + ": " + (Uteis.formatarDecimalDuasCasas(bd.doubleValue()))).append("%', ");
				graficoBarra.append("	events:{ click:  function (event) {"); 
				graficoBarra.append("		selecionarLegendaPorSegmentacao(").append(obj.getCodigo()).append("); "); 
				graficoBarra.append("	}}, ");	
				graficoBarra.append("   data : [{y: ").append(obj.getQuantidade()).append(", id: ").append(obj.getCodigo()).append(" }]}");
				virgula = true;
			}
			graficoBarra.append(" 		]");
			graficoBarra.append(" 	};");
			graficoBarra.append(" 	var chart").append(getCodigo()).append(" = new Highcharts.Chart(options").append(getCodigo()).append(");");
			graficoBarra.append(" }(jQuery));");
			graficoBarra.append(" </script>");
		}
		return graficoBarra.toString();
	}

	private StringBuilder graficoColuna;

	public String getGraficoColuna() {
		if (graficoColuna == null) {
			graficoColuna = new StringBuilder();
			graficoColuna.append("<div id=\"container").append(getCodigo()).append("\" class=\"col-md-12\" /> ");
			graficoColuna.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			graficoColuna.append("	(function($) { ");
			graficoColuna.append("	$.ajaxSetup({");
			graficoColuna.append("		cache : false");
			graficoColuna.append("	});");
			graficoColuna.append("		var options").append(getCodigo()).append(" = {");
			graficoColuna.append(" 			chart : {");
			graficoColuna.append(" 				type : \"column\",");
			graficoColuna.append(" 				renderTo : \"container").append(getCodigo()).append("\",");
			graficoColuna.append(" 				backgroundColor : \"#FFF\", ");
			graficoColuna.append(" 			},");
			graficoColuna.append(" 			title : {");
			graficoColuna.append(" 				text : ''");
			graficoColuna.append(" 			},");
			graficoColuna.append(" 		credits : {enabled : false},");
			graficoColuna.append(" 		exporting : {enabled : false},");
			graficoColuna.append(" 			tooltip : {");
			graficoColuna.append(" 				pointFormat : '<b>{series.name}</b>'");			
			graficoColuna.append(" 			},");
			graficoColuna.append(" 			xAxis:{");
			graficoColuna.append(" 				categories: [ ' ' ], ");
			graficoColuna.append(" 			},");
			graficoColuna.append(" 			yAxis: {");
			graficoColuna.append(" 				allowDecimals: false,");
			graficoColuna.append(" 				title:{");
			graficoColuna.append(" 					text:''");
			graficoColuna.append(" 				},");
			graficoColuna.append(" 				marginLeft:0");
			graficoColuna.append(" 			},");
			graficoColuna.append(" 			plotOptions : {");
			graficoColuna.append(" 				series : {");
			graficoColuna.append("					cursor: 'pointer',");
			graficoColuna.append("					dataLabels:{");
			graficoColuna.append("						enabled : true,");
			graficoColuna.append("						format : '{point.y}'");
			graficoColuna.append("					},");
			graficoColuna.append("					point : {");
			graficoColuna.append("						events : {");
			graficoColuna.append("							click : function (event) {");			
			graficoColuna.append("								selecionarLegendaPorSegmentacao(this.options.id);");
			graficoColuna.append("							} ");			
			graficoColuna.append("					}");
			graficoColuna.append(" 				},");
			graficoColuna.append(" 				showInLegend : true");
			graficoColuna.append(" 			}");
			graficoColuna.append(" 		},");
			graficoColuna.append("		legend : {");
			graficoColuna.append("			layout : 'vertical',");
			graficoColuna.append("			align: 'center',");
			graficoColuna.append("			verticalAlign: 'bottom',");
			graficoColuna.append("			itemWidth: 400,");
			graficoColuna.append("			maxHeight: 120,");
			graficoColuna.append("			labelFormatter : function() {");
			graficoColuna.append("					return this.name;");
			graficoColuna.append("			}");
			graficoColuna.append("		}, ");
			graficoColuna.append(" 		series : [ ");
			Boolean virgula = false;			
			for (SegmentacaoOpcaoVO obj : getSegmentacaoOpcaoVOs()) {
				if (virgula) {
					graficoColuna.append(", ");
				}
				BigDecimal bd = new BigDecimal(obj.getPercentual()).setScale(2, RoundingMode.HALF_EVEN);
				graficoColuna.append("	{name : '").append(obj.getDescricao() + " - " + obj.getQuantidade() + ": " + (Uteis.formatarDecimalDuasCasas(bd.doubleValue()))).append("%', ");
				graficoColuna.append("	events:{ click:  function (event) {"); 
				graficoColuna.append("		selecionarLegendaPorSegmentacao(").append(obj.getCodigo()).append("); "); 
				graficoColuna.append("	}}, ");
				graficoColuna.append("  data : [{y: ").append(obj.getQuantidade()).append(", id: ").append(obj.getCodigo()).append("}]}");
				virgula = true;
			}
			graficoColuna.append(" 		]");
			graficoColuna.append(" 	};");
			graficoColuna.append(" 	var chart").append(getCodigo()).append(" = new Highcharts.Chart(options").append(getCodigo()).append(");");
			graficoColuna.append(" }(jQuery));");
			graficoColuna.append(" </script>");
		}
		return graficoColuna.toString();
	}

	private StringBuilder graficoPizza;

	public String getGraficoPizza() {
		if (graficoPizza == null) {
			graficoPizza = new StringBuilder();
			graficoPizza.append("<div id=\"container").append(getCodigo()).append("\" class=\"col-md-12\"  /> ");
			graficoPizza.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			graficoPizza.append("	(function($) { ");
			graficoPizza.append("	$.ajaxSetup({");
			graficoPizza.append("		cache : false");
			graficoPizza.append("	});");
			graficoPizza.append("		var options").append(getCodigo()).append(" = {");
			graficoPizza.append(" 			chart : {");
			graficoPizza.append(" 				type : \"pie\",");
			graficoPizza.append(" 				renderTo : \"container").append(getCodigo()).append("\",");
			graficoPizza.append(" 				className : 'autosize', ");
			graficoPizza.append(" 				backgroundColor : \"#FFF\" ");
			graficoPizza.append(" 			},");
			graficoPizza.append(" 			title : {");
			graficoPizza.append(" 				text : ''");
			graficoPizza.append(" 			},");
			graficoPizza.append(" 		credits : {enabled : false},");
			graficoPizza.append(" 		exporting : {enabled : false},");
			graficoPizza.append(" 			tooltip : {");
			graficoPizza.append(" 				pointFormat : '<b>{series.name}</b>:{point.percentage:.1f}% - {point.y}'");						
			graficoPizza.append(" 			},");
			graficoPizza.append(" 			plotOptions : {");
			graficoPizza.append(" 				pie : {");
			graficoPizza.append(" 					allowPointSelect : true,");
			graficoPizza.append(" 					cursor : 'pointer',");
			graficoPizza.append(" 					dataLabels : {");
			graficoPizza.append(" 						enabled : true,");
			graficoPizza.append(" 						color : '#000000',");
			graficoPizza.append(" 						connectorColor : '#000000',");
			graficoPizza.append(" 						format : '<b>{point.name}</b>:{point.percentage:.1f}% - {point.y}'");
			graficoPizza.append(" 					}, ");
			graficoPizza.append("					point : {");
			graficoPizza.append("						events : {");
			graficoPizza.append("							click : function (event) {");
			graficoPizza.append("								selecionarLegendaPorSegmentacao(this.id);");
			graficoPizza.append("							},");
			graficoPizza.append("						legendItemClick: function (event) {");
			graficoPizza.append("							click: selecionarLegendaPorSegmentacao(this.id)");
			graficoPizza.append("						}");
			graficoPizza.append("					}");
			graficoPizza.append(" 				},");
			graficoPizza.append(" 				showInLegend : true");
			graficoPizza.append(" 			}");
			graficoPizza.append(" 		},");
			graficoPizza.append("		legend : {");
			graficoPizza.append("			layout : 'vertical',");
			graficoPizza.append("			align: 'center',");
			graficoPizza.append("			verticalAlign: 'bottom',");
			graficoPizza.append("			itemWidth: 400,");
			graficoPizza.append("			maxHeight: 200,");
			graficoPizza.append("			labelFormat : '<b>{name}</b>:{percentage:.1f}% - {y}'");			
			graficoPizza.append("		}, ");
			graficoPizza.append(" 		series : [ {");
			graficoPizza.append(" 			type : 'pie',");
			graficoPizza.append(" 			name : 'Segmentação',");
			graficoPizza.append(" 			data : [ ");
			Boolean virgula = false;			
			for (SegmentacaoOpcaoVO segmentacaoOpcaoVO : getSegmentacaoOpcaoVOs()) {
				if (virgula) {
					graficoPizza.append(", ");
				}
				//BigDecimal bd = new BigDecimal(segmentacaoOpcaoVO.getPercentual()).setScale(2, RoundingMode.HALF_EVEN);
				graficoPizza.append(" {name: '").append(segmentacaoOpcaoVO.getDescricao()).append("', y:").append(segmentacaoOpcaoVO.getQuantidade()).append(", id:" + segmentacaoOpcaoVO.getCodigo() + "} ");
				virgula = true;
			}
			graficoPizza.append(" 			]");
			graficoPizza.append(" 		} ]");
			graficoPizza.append(" 	};");
			graficoPizza.append(" 	var chart").append(getCodigo()).append(" = new Highcharts.Chart(options").append(getCodigo()).append(");");
			graficoPizza.append(" }(jQuery));");
			graficoPizza.append(" </script>");
		}
		return graficoPizza.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SegmentacaoProspectVO other = (SegmentacaoProspectVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public TipoLayoutApresentacaoResultadoSegmentacaoEnum getTipoLayoutApresentacaoResultadoSegmentacaoEnum() {
		if (tipoLayoutApresentacaoResultadoSegmentacaoEnum == null) {
			tipoLayoutApresentacaoResultadoSegmentacaoEnum = TipoLayoutApresentacaoResultadoSegmentacaoEnum.LISTA;
		}
		return tipoLayoutApresentacaoResultadoSegmentacaoEnum;
	}

	public void setTipoLayoutApresentacaoResultadoSegmentacaoEnum(TipoLayoutApresentacaoResultadoSegmentacaoEnum tipoLayoutApresentacaoResultadoSegmentacaoEnum) {
		this.tipoLayoutApresentacaoResultadoSegmentacaoEnum = tipoLayoutApresentacaoResultadoSegmentacaoEnum;
	}

	public Boolean getIsApresentarGraficoPizza() {
		return getTipoLayoutApresentacaoResultadoSegmentacaoEnum().equals(TipoLayoutApresentacaoResultadoSegmentacaoEnum.GRAFICO_PIZZA);
	}

	public Boolean getIsApresentarGraficoColuna() {
		return getTipoLayoutApresentacaoResultadoSegmentacaoEnum().equals(TipoLayoutApresentacaoResultadoSegmentacaoEnum.GRAFICO_COLUNA);
	}

	public Boolean getIsApresentarLista() {
		return getTipoLayoutApresentacaoResultadoSegmentacaoEnum().equals(TipoLayoutApresentacaoResultadoSegmentacaoEnum.LISTA);
	}

	public Boolean getIsApresentarGraficoBarra() {
		return getTipoLayoutApresentacaoResultadoSegmentacaoEnum().equals(TipoLayoutApresentacaoResultadoSegmentacaoEnum.GRAFICO_BARRA);
	}

	public void calcularPercentualSegmentacaoOpcao() {
		for (SegmentacaoOpcaoVO segmentacaoOpcaoVO2 : getSegmentacaoOpcaoVOs()) {
			segmentacaoOpcaoVO2.setPercentual((segmentacaoOpcaoVO2.getQuantidade().doubleValue() / getQuantidadeTotal().doubleValue() * 100));
		}
	}
}
