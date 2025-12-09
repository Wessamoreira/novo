
											(function($) {
												$.ajaxSetup({
													cache : false
												});
												
												$(document).ready(function() {
													 criarGraficoReceitaDespesaLine();													
												});
												criarGraficoReceitaDespesaLine = 	function() {														
														var options = {		
																exporting:{
															    	type: 'image/jpeg',
															    	buttons:{
															    		exportButton:{
															    			 y:5														    			
															    		},
																		printButton:{														    			
												    					    y:5
												    					}
															    	}
															    },
															legend:{															
																marginTop: 5,
																style:{
																	fontSize:10
																}
															},
															chart : {
																type : "column",
																renderTo : "form\:graficoReceitaDespesaLine",
																backgroundColor : "#FFF",																
																width:930,
																height:300,
																spacingLeft: 0,												
																marginTop: 40
															},
															credits : {
																enabled : false
															},
															title : {
																text : '<h1 style="width:870px;z-index=1;" class="title1">Receita X Despesa - Consolidado</h1>',
																	align:'left',
																	useHTML:true,
																	y:5																	
															},																														
															xAxis:{
 																type: 'datetime', 																
 																 labels: {
 													                formatter: function() {
 													                     return (Highcharts.dateFormat('%b', this.value) == 'Apr'?'Abr': 													                    	
 													                          Highcharts.dateFormat('%b', this.value) == 'May'?'Mai': 													                        	  
 													                          Highcharts.dateFormat('%b', this.value) == 'Aug'?'Ago':
 													                          Highcharts.dateFormat('%b', this.value) == 'Sep'?'Set':
 													                          Highcharts.dateFormat('%b', this.value) == 'Oct'?'Out':
 													                          Highcharts.dateFormat('%b', this.value) == 'Dec'?'Dez':
 													                          Highcharts.dateFormat('%b', this.value))
 													                          +'/'+Highcharts.dateFormat('%y', this.value);    
 													                }, 													                
															        rotation: -75.0,															        
															        step: 0,
															        style:{
															        	fontSize: '10px'															        	
															        },
															        align:'right'
 													            }, 	
 													            
 													            maxZoom: ( 30 )* 24 * 3600 * 1000,
 													            tickInterval: 30* 24 * 3600 * 1000															   
															},															
															yAxis: {
												               title:{
												            	   text:''
												               },
												                labels: {
 													                formatter: function() {
 													                     return 'R$'+Highcharts.numberFormat((this.value),2,',', '.');    
 													                },															      
															        style:{
															        	fontSize: '10px'															        	
															        }
															        
 													            },
 													            marginLeft:0,
 													           
												                tickInterval: 500000														                
												            },															
															tooltip : {
																formatter: function() {
											                         return ''+										                         											                         											                         
											                         (Highcharts.dateFormat('%b', this.x) == 'Apr'?'Abr':
											                         Highcharts.dateFormat('%b', this.x) == 'May'?'Mai':
											                         Highcharts.dateFormat('%b', this.x) == 'Aug'?'Ago':
											                         Highcharts.dateFormat('%b', this.x) == 'Sep'?'Set':
											                         Highcharts.dateFormat('%b', this.x) == 'Oct'?'Out':
											                         Highcharts.dateFormat('%b', this.x) == 'Dec'?'Dez':
											                         Highcharts.dateFormat('%b', this.x))
											                         +'/'+Highcharts.dateFormat('%y', this.x) +' - R$'+ Highcharts.numberFormat((this.y),2,',', '.');											                    
											                         }
															},
																													
															series : [ {
 																		name:'<%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 1 ? ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getLegenda() : "Receitas"%>',
 																		color:'#3399FF',	
 																		pointInterval: 30* 24 * 3600 * 1000,
 																		visible: <%=(((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 1 && ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getValor() > 0.0)%>,
 																		showInLegend: <%=(((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 1 && ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(0).getValor() > 0.0)%>,
 																		data :[<%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getListaValoresReceitas()%>]
																	}, 																
																	{
																		name:'<%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2 ? ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getLegenda() : "Despesas"%>',
																		color:'#D20000',
																		pointInterval: 30* 24 * 3600 * 1000,
																		visible: <%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2 && ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getValor() > 0.0%>,
																		showInLegend: <%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 2 && ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(1).getValor() > 0.0%>,
																		data :[<%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getListaValoresDespesas()%>]																															  
																	}, 																
																	{
																		name:'<%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 3? ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(2).getLegenda() : "Saldo"%>',
																		color : '#FDFD00',
																		pointInterval : 30 * 24 * 3600 * 1000,
																		visible : <%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 3 && ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(2).getValor() > 0.0%>,
																		showInLegend : <%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().size() >= 3 && ((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getLegendaGraficoReceitaDespesaVOs().get(2).getValor() > 0.0%>,									
																		data : [<%=((PainelGestorControle) session.getAttribute("PainelGestorControle")).getPainelGestorVO().getListaValoresReceitaDespesasSaldo()%>]

																	}
																]

											};
											var chart = new Highcharts.Chart(options);

										}

									}(jQuery));
								