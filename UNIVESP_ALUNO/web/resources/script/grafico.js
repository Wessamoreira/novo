		(function($) {
			$.ajaxSetup({cache: false});
			
			
			$(document).ready(function() {																
				
			});
			
			var tipoGrafico;
			var tituloGrafico;
			var tituloXGrafico;
			var tituloYGrafico;
			var apresentarLegenda;
			var altura;
			var largura;
			var tipoGraficoRE;
			var tituloGraficoRE;
			var tituloXGraficoRE;
			var tituloYGraficoRE;
			var apresentarLegendaRE;
			
			
			
			definirGraficoApresentarComTamanho =  function(idGrafico, conteudo, width, height){
				altura = height;
				largura = width;
				
				if(conteudo == 'true' ){
					  tipoGrafico = &quot;#{session.getAttribute("tipoGrafico")}&quot;
					  tituloGrafico = &quot;#{session.getAttribute("tituloGrafico")}&quot;															  
					  apresentarLegenda = &quot;#{session.getAttribute("apresentarLegenda")}&quot;
					  tituloXGrafico = &quot;#{session.getAttribute("tituloXGrafico")}&quot;
					  tituloYGrafico = &quot;#{session.getAttribute("tituloYGrafico")}&quot;
					  
					  if(tipoGrafico == "pie"){
							gerarGraficoPizza(idGrafico);																
						}else if(tipoGrafico != "pie"){
							gerarGrafico(idGrafico);																
						}
					}else{
						tipoGraficoRE = &quot;#{session.getAttribute("tipoGraficoRE")}&quot;
						tituloGraficoRE = &quot;#{session.getAttribute("tituloGraficoRE")}&quot;
						apresentarLegendaRE = #{session.getAttribute("apresentarLegendaRE")};
						tituloXGraficoRE = &quot;#{session.getAttribute("tituloXGraficoRE")}&quot;
						tituloYGraficoRE = &quot;#{session.getAttribute("tituloYGraficoRE")}&quot;
						if(tipoGraficoRE == "pie"){
							gerarGraficoPizzaRE(idGrafico);																
						}else if(tipoGraficoRE != "pie"){
							gerarGraficoRE(idGrafico);																
						}
					}
			};
			
			definirGraficoApresentar =  function(idGrafico, conteudo){
				altura = 320;
				largura = 540;
				if(conteudo == 'true' ){
					  tipoGrafico = &quot;#{session.getAttribute("tipoGrafico")}&quot;
					  tituloGrafico = &quot;#{session.getAttribute("tituloGrafico")}&quot;															  
					  apresentarLegenda = &quot;#{session.getAttribute("apresentarLegenda")}&quot;
					  tituloXGrafico = &quot;#{session.getAttribute("tituloXGrafico")}&quot;
					  tituloYGrafico = &quot;#{session.getAttribute("tituloYGrafico")}&quot;
				  if(tipoGrafico == "pie"){
						gerarGraficoPizza(idGrafico);																
					}else if(tipoGrafico != "pie"){
						gerarGrafico(idGrafico);																
					}
				  
				}else{
					tipoGraficoRE = &quot;#{session.getAttribute("tipoGraficoRE")}&quot;
					tituloGraficoRE = &quot;#{session.getAttribute("tituloGraficoRE")}&quot;
					apresentarLegendaRE = #{session.getAttribute("apresentarLegendaRE")};
					tituloXGraficoRE = &quot;#{session.getAttribute("tituloXGraficoRE")}&quot;
					tituloYGraficoRE = &quot;#{session.getAttribute("tituloYGraficoRE")}&quot;
					if(tipoGraficoRE == "pie"){
						gerarGraficoPizzaRE(idGrafico);																
					}else if(tipoGraficoRE != "pie"){
						gerarGraficoRE(idGrafico);																
					}
				}
				
				
			};
			
			gerarGraficoRE = function(idGrafico) {
				
				var options2 = {
					chart : {
						type : tipoGraficoRE,
						renderTo : idGrafico,
						backgroundColor : "transparent",
						marginTop : 45,
						width : largura,
						height : altura,
						spacingLeft : 0,
					},
					credits : {
						enabled : false
					},
					plotOptions: {
						column: {
		                    allowPointSelect: true,
		                    cursor: 'pointer',
		                    dataLabels: {
		                        enabled: apresentarLegendaRE
		                    },
		                    showInLegend: apresentarLegendaRE
		                },
		                line: {
		                    allowPointSelect: true,
		                    cursor: 'pointer',
		                    dataLabels: {
		                        enabled: apresentarLegendaRE
		                    },
		                    showInLegend: apresentarLegendaRE
		                },
		                bar: {
		                    allowPointSelect: true,
		                    cursor: 'pointer',
		                    dataLabels: {
		                        enabled: apresentarLegendaRE
		                    },
		                    showInLegend: apresentarLegendaRE
		                },
		                series: {													                    
		                    showInLegend: apresentarLegendaRE
		                },
		            },
					legend : {
						enabled : apresentarLegendaRE
					},
					 xAxis: {
			                categories: [ #{session.getAttribute("categoriaGraficoRE")}],
			                title: {
			                    text: tituloXGraficoRE
			                }
			            },
			            yAxis: {
			                title: {
			                    text: tituloYGraficoRE
			                },
			                labels: {
					                formatter: function() {
					                     return ''+Highcharts.numberFormat((this.value),0,',','.' )   
					                }
			                }
			            },
					title : {
						text : '<h1 style="width:'+largura+'px;z-index:1;font-size:12px;text-align:center" class="tituloCampos">' + tituloGraficoRE + '</h1>',
						align : 'left',
						useHTML : true,
						y : 5
					},	
					loading: {
						hideDuration: 150,																	
						showDuration: 150																	
					},
					tooltip : {
						enabled: true,
						formatter : function() {
								return '<b>' + this.series.name + '</b><br/>' 
								+ this.x + ': ' + Highcharts.numberFormat((this.y), 0, ',', '.')
							}
						},

						series : [#{session.getAttribute("valorGraficoRE")} ]
					};
					
					var chart = new Highcharts.Chart(options2);

				};

				gerarGraficoPizzaRE = function(idGrafico) {
					var options = {

						chart : {
							type : 'pie',
							renderTo : idGrafico,
							backgroundColor : "transparent",
							marginTop : 45,
							width : largura,
							height : altura,
							spacingLeft : 0,
						},
						credits : {
							enabled : false
						},
						plotOptions: {
			                pie: {
			                    allowPointSelect: true,
			                    cursor: 'pointer',
			                    dataLabels: {
			                        enabled: apresentarLegendaRE
			                    },
			                    showInLegend: apresentarLegendaRE
			                }
			            },
			            legend:{
			            	enabled:apresentarLegendaRE
			            },
						title : {
							text : '<h1 style="width:'+largura+'px;z-index:1;font-size:12px;text-align:center" class="tituloCampos">' + tituloGraficoRE + '</h1>',
							align : 'left',
							useHTML : true,
							y : 5
						},																
						tooltip : {
							formatter : function() {
								return '<b>' + this.point.name + ' - ' + Highcharts.numberFormat((this.point.config[1]),0,',','.'+'</b>' );
							}																	
						},
						loading: {
							hideDuration: 150,																	
							showDuration: 150																	
						},
						series : [
						          	{data: [#{session.getAttribute("valorGraficoRE")} ]
						} ]
					};
					var chart2 = new Highcharts.Chart(options);
				};
			
			gerarGrafico = function(idGrafico) {
																		
				var options2 = {
					chart : {
						type : tipoGrafico,
						renderTo : idGrafico,
						backgroundColor : "transparent",
						marginTop : 45,
						width : largura,
						height : altura,
						spacingLeft : 0,
					},
					credits : {
						enabled : false
					},
					legend : {
						enabled : apresentarLegenda
					},
					 xAxis: {
			                categories: [ #{session.getAttribute("categoriaGrafico")}],
			                title: {
			                    text: tituloXGrafico
			                }
			            },
			            yAxis: {
			                title: {
			                    text: tituloYGrafico
			                },
			                labels: {
					                formatter: function() {
					                     return ''+Highcharts.numberFormat((this.value),0,',','.' )   
					                }
			                }
			            },
					title : {
						text : '<h1 style="width:'+largura+'px;z-index:1;font-size:12px;text-align:center" class="tituloCampos">' + tituloGrafico + '</h1>',
						align : 'left',
						useHTML : true,
						y : 5
					},
					plotOptions: {
						column: {
		                    allowPointSelect: true,
		                    cursor: 'pointer',
		                    dataLabels: {
		                        enabled: apresentarLegenda
		                    },
		                    showInLegend: apresentarLegenda
		                },
		                line: {
		                    allowPointSelect: true,
		                    cursor: 'pointer',
		                    dataLabels: {
		                        enabled: apresentarLegenda
		                    },
		                    showInLegend: apresentarLegenda
		                },
		                bar: {
		                    allowPointSelect: true,
		                    cursor: 'pointer',
		                    dataLabels: {
		                        enabled: apresentarLegenda
		                    },
		                    showInLegend: apresentarLegenda
		                },
		                series: {													                    
		                    showInLegend: apresentarLegenda
		                },
		            },
					loading: {
						hideDuration: 150,																	
						showDuration: 150																	
					},
					tooltip : {
						enabled: true,
						formatter : function() {
								return '<b>' + this.series.name + '</b><br/>' 
								+ this.x + ': ' + Highcharts.numberFormat((this.y), 0, ',', '.')
							}
						},

						series : [#{session.getAttribute("valorGrafico")} ]
					};
					var chart = new Highcharts.Chart(options2);

				};

				gerarGraficoPizza = function(idGrafico) {
					var options = {

						chart : {
							type : 'pie',
							renderTo : idGrafico,
							backgroundColor : "transparent",
							marginTop : 45,
							width : largura,
							height : altura,
							spacingLeft : 0,
						},
						credits : {
							enabled : false
						},
						plotOptions: {
			                pie: {
			                    allowPointSelect: true,
			                    cursor: 'pointer',
			                    dataLabels: {
			                        enabled: apresentarLegenda
			                    },
			                    showInLegend: apresentarLegenda
			                }
			            },
			            legend:{
			            	enabled:apresentarLegenda
			            },
						title : {
							text : '<h1 style="width:'+largura+'px;z-index:1;font-size:12px;text-align:center" class="tituloCampos">' + tituloGrafico + '</h1>',
							align : 'left',
							useHTML : true,
							y : 5
						},	
						loading: {
							hideDuration: 150,																	
							showDuration: 150																	
						},
						tooltip : {
							formatter : function() {
								return '<b>' + this.point.name + ' - ' + Highcharts.numberFormat((this.point.config[1]),0,',','.'+'</b>' );
							}																	
						},
						series : [
						          	{data: [#{session.getAttribute("valorGrafico")} ]
						} ]
					};
					var chart2 = new Highcharts.Chart(options);
				};
		}(jQuery));