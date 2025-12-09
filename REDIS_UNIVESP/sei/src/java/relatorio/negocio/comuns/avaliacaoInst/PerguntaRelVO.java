/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.avaliacaoInst;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.junit.Test;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.TipoLayoutApresentacaoResultadoPerguntaEnum;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * 
 * @author Rodrigo
 */
public class PerguntaRelVO {

	protected String nome;
	protected String tipoResposta;
	protected Integer codigo;
	protected List<RespostaRelVO> respostaTexto;
	protected DefaultPieDataset graficoRespostaPizza;
	protected DefaultPieDataset graficoPergunta;
	protected DefaultCategoryDataset graficoRespostaBarra;
	protected List<RespostaPerguntaVO> respostaPerguntaVOs;
	protected Integer totalPessoas;
	protected Integer nrPergunta;
	private TipoLayoutApresentacaoResultadoPerguntaEnum tipoResultadoGrafico;
	private String respostaTextual;
	public boolean respostaObrigatoria = false;
	public boolean agruparReposta = false;

	public PerguntaRelVO() {
		inicializarDados();
	}

	public void inicializarDados() {
		setCodigo(0);
		setTipoResposta("");
		setNome("");
		setGraficoRespostaPizza(new DefaultPieDataset());
		setGraficoRespostaBarra(new DefaultCategoryDataset());
		setGraficoPergunta(new DefaultPieDataset());
		setRespostaTexto(new ArrayList<RespostaRelVO>(0));
		setRespostaPerguntaVOs(new ArrayList<RespostaPerguntaVO>(0));
	}

	public String consultarNomeResposta(RespostaRelVO respostaRelVO) {
		for (RespostaPerguntaVO obj : respostaPerguntaVOs) {
			String codigoRes = "[" + obj.getCodigo() + "]";
			if (codigoRes.equals(respostaRelVO.getResposta())) {
				if (Uteis.isAtributoPreenchido(obj.getDescricao()) && !Uteis.isAtributoPreenchido(respostaRelVO.getListaRespostasAdicionais())) {
					respostaRelVO.setApresentarDescricaoResposta(false);
				}
				return obj.getDescricao();
			}
		}
		return "Não Responderam";
	}

	public String consultarSiglaResposta(String codigoRespondido) {
		int index = 0;
		for (RespostaPerguntaVO obj : respostaPerguntaVOs) {
			String codigoRes = "[" + obj.getCodigo() + "]";
			if (codigoRes.equals(codigoRespondido)) {
				return getString(index);
			}
			index++;
		}
		return "";
	}

	public Integer consultarOrdemResposta(String codigoRespondido) {
		int maior = 0;
		for (RespostaPerguntaVO obj : respostaPerguntaVOs) {
			String codigoRes = "[" + obj.getCodigo() + "]";
			if (codigoRes.equals(codigoRespondido)) {
				return obj.getOrdem();
			}
			if (obj.getOrdem() > maior) {
				maior = obj.getOrdem();
			}
		}
		return maior + 1;
	}
	
	public Integer consultarAgrupadorResposta(String codigoRespondido) {
		for (RespostaPerguntaVO obj : respostaPerguntaVOs) {
			String codigoRes = "[" + obj.getCodigo() + "]";
			if (codigoRes.equals(codigoRespondido)) {
				return obj.getAgrupador();
			}
		}
		return 0;
	}
	public String consultarCodigoResposta(String codigoRespondido) {
		for (RespostaPerguntaVO obj : respostaPerguntaVOs) {
			String codigoRes = "[" + obj.getCodigo() + "]";
			if (codigoRes.equals(codigoRespondido)) {
				return obj.getCodigo().toString();
			}
		}
		return "-1";
	}

	public String getString(int x) {
		switch (x) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "G";
		case 7:
			return "H";
		case 8:
			return "I";
		case 9:
			return "J";
		case 10:
			return "K";
		case 11:
			return "L";
		case 12:
			return "M";
		case 13:
			return "N";
		case 14:
			return "O";
		case 15:
			return "P";
		case 16:
			return "Q";
		case 17:
			return "R";
		case 18:
			return "S";
		case 19:
			return "T";
		case 20:
			return "U";
		case 21:
			return "V";
		case 22:
			return "X";
		case 23:
			return "Z";
		case 24:
			return "W";
		default:
			return "";
		}
	}

	public void inicializarDadosRespostaQuestionario(SqlRowSet rs) throws SQLException {
		for (RespostaRelVO objExistente : getRespostaTexto()) {
			if (objExistente.getNomeResposta().equals(rs.getString("RespostaPergunta_descricao"))) {
				return;
			}
		}
		RespostaRelVO respostaRelVO = new RespostaRelVO();
		respostaRelVO.setNomeResposta(rs.getString("RespostaPergunta_descricao"));
		respostaRelVO.setQuantidadePessoa(0);
		respostaRelVO.setTotalPessoas(0);
		respostaRelVO.setOrdem(rs.getInt("ordem"));
		if (!respostaRelVO.getNomeResposta().equals("")) {
			respostaTexto.add(respostaRelVO);
		}

	}
	
	public void adicionarRespostaPergunta(RespostaRelVO obj, String respostaAdicional) {
		if(Uteis.isAtributoPreenchido(respostaAdicional) && respostaAdicional.contains(obj.getResposta())) {
			String[] respostasADD = respostaAdicional.split("\\[");
			for(String respostaAdd : respostasADD) {
				if(respostaAdd.startsWith(obj.getResposta().replace("[", ""))) {
					respostaAdd = respostaAdd.substring(respostaAdd.indexOf("{")+1, respostaAdd.lastIndexOf("}"));
					RespostaRelVO resAdd = new RespostaRelVO();
					resAdd.setResposta(respostaAdd);				
					obj.getListaRespostasAdicionais().add(resAdd);
				}		
			}
		}
		if(isAgruparReposta() && Uteis.isAtributoPreenchido(obj.getAgruparResposta())){
			realizarMontagemRespostaComAgrupamento(obj);
		}else{
			realizarMontagemRespostaSemAgrupamento(obj);
		}
	}
	
	public void realizarMontagemRespostaSemAgrupamento(RespostaRelVO obj){
		int x = 0;
		if (!getTipoResposta().equals("TE")) {
			int index = 0;

			for (RespostaRelVO objExistente : respostaTexto) {
				if (objExistente.getResposta().equals(obj.getResposta())) {
					objExistente.setQuantidadePessoa(objExistente.getQuantidadePessoa() + obj.getQuantidadePessoa());
					setTotalPessoas(getTotalPessoas() + obj.getQuantidadePessoa());
					objExistente.getListaRespostasAdicionais().addAll(obj.getListaRespostasAdicionais());
					respostaTexto.set(index, objExistente);
					x = 1;					
				}
				//objExistente.setTotalPessoas(objExistente.getTotalPessoas() + obj.getQuantidadePessoa());
				index++;
			}
			if (x == 0) {
				preencherRespostaRElVO(obj);
			}
		}
		/*
		 * @author Wendel Rodrigues - 15/08/2014
		 * Removido a regra !obj.getNomeResposta().trim().isEmpty() na expressão abaixo para corrigir o chamado de código 5805,
		 * onde as respostas textuais do Questionario do Processo Seletivo não estava aparecendo.
		 */
		if (x == 0) {
			if(obj.getNomeResposta().equals("Não Responderam")) {
				int index = 0;
				for (RespostaRelVO objExistente : respostaTexto) {
					if(objExistente.getNomeResposta().equals(obj.getNomeResposta())) {
						objExistente.setQuantidadePessoa(objExistente.getQuantidadePessoa() + obj.getQuantidadePessoa());
						setTotalPessoas(getTotalPessoas() + obj.getQuantidadePessoa());
						respostaTexto.set(index, objExistente);
						return;
					}
					index++;
				}
			}
			respostaTexto.add(obj);			
		}
	}
	public void realizarMontagemRespostaComAgrupamento(RespostaRelVO obj){
		int x = 0;
		if (!getTipoResposta().equals("TE")) {
			int index = 0;						
			for (RespostaRelVO objExistente : respostaTexto) {
				if (objExistente.getAgruparResposta().equals(obj.getAgruparResposta())) {
					if(!objExistente.getListaRespostaAgrupadas().contains(obj.getListaRespostaAgrupadas())){
						objExistente.setListaRespostaAgrupadas(objExistente.getListaRespostaAgrupadas() + ", "+obj.getListaRespostaAgrupadas());	
					}
					String nomeResposta = consultarNomeResposta(obj);
					if(!objExistente.getNomeResposta().contains(nomeResposta)){
						objExistente.setNomeResposta(objExistente.getNomeResposta() + " / "+nomeResposta);	
					}
					objExistente.setQuantidadePessoa(objExistente.getQuantidadePessoa() + obj.getQuantidadePessoa());
					setTotalPessoas(getTotalPessoas() + obj.getQuantidadePessoa());
					respostaTexto.set(index, objExistente);
					x = 1;					
				}
				//objExistente.setTotalPessoas(objExistente.getTotalPessoas() + obj.getQuantidadePessoa());
				index++;
			}
			if (x == 0) {
				
				preencherRespostaRElVO(obj);
			}
		}
		/*
		 * @author Wendel Rodrigues - 15/08/2014
		 * Removido a regra !obj.getNomeResposta().trim().isEmpty() na expressão abaixo para corrigir o chamado de código 5805,
		 * onde as respostas textuais do Questionario do Processo Seletivo não estava aparecendo.
		 */
		if (x == 0) {
			if(obj.getNomeResposta().equals("Não Responderam")) {
				int index = 0;
				for (RespostaRelVO objExistente : respostaTexto) {
					if(objExistente.getNomeResposta().equals(obj.getNomeResposta())) {
						objExistente.setQuantidadePessoa(objExistente.getQuantidadePessoa() + obj.getQuantidadePessoa());
						setTotalPessoas(getTotalPessoas() + obj.getQuantidadePessoa());
						respostaTexto.set(index, objExistente);
						return;
					}
					index++;
				}
			}
			respostaTexto.add(obj);
		}
	}
	
	public void preencherRespostaRElVO(RespostaRelVO obj){
		setTotalPessoas(obj.getQuantidadePessoa());
		obj.setTotalPessoas(getTotalPessoas());
		obj.setSiglaResposta(consultarSiglaResposta(obj.getResposta()));
		obj.setNomeResposta(consultarNomeResposta(obj));
		obj.setOrdem(consultarOrdemResposta(obj.getResposta()));
	}

	public void adicionarPerguntaMultiplaEscolha(String resposta, Integer quantidade, String respostaAdicional) {
		while (resposta.startsWith("[")) {
			int posFinal = resposta.indexOf("]") + 1;
			String resp = resposta.substring(0, posFinal);
			RespostaRelVO obj = new RespostaRelVO();
			obj.setResposta(resp);
			obj.setListaRespostaAgrupadas(consultarCodigoResposta(obj.getResposta()));
			obj.setAgruparResposta(consultarAgrupadorResposta(obj.getResposta()));
			obj.setQuantidadePessoa(quantidade);
			adicionarRespostaPergunta(obj, respostaAdicional);
			if (posFinal + 1 >= resposta.length()) {
				return;
			}
			resposta = resposta.substring(posFinal, resposta.length());
		}
		if ("Não Responderam".equals(resposta)) {
			RespostaRelVO obj = new RespostaRelVO();
			obj.setResposta(resposta);
			obj.setListaRespostaAgrupadas(consultarCodigoResposta(obj.getResposta()));
			obj.setAgruparResposta(consultarAgrupadorResposta(obj.getResposta()));
			obj.setNomeResposta(resposta);
			obj.setQuantidadePessoa(quantidade);
			adicionarRespostaPergunta(obj, respostaAdicional);
		}
	}

	public JRDataSource getRespostasJR() {
		return new JRBeanArrayDataSource(getRespostaTexto().toArray());
	}

	public Boolean getTipoSimplesEscolha() {
		if (tipoResposta.equals("SE")) {
			return true;
		}
		return false;
	}

	public Boolean getTipoMultiplaEscolha() {
		if (tipoResposta.equals("ME")) {
			return true;
		}
		return false;
	}

	public Boolean getTipoTexto() {
		if (tipoResposta.equals("TE")) {
			return true;
		}
		return false;
	}

	public void criarGraficoResposta() {

		try {
			Integer qtdeTotal = 0;
			for (RespostaRelVO obj : respostaTexto) {
				qtdeTotal += obj.getQuantidadePessoa();
			}
			if (getTipoMultiplaEscolha()) {
				setGraficoRespostaBarra(new DefaultCategoryDataset());
				for (RespostaRelVO obj : respostaTexto) {
					getGraficoRespostaBarra().addValue(obj.getQuantidadePessoa(), obj.getNomeResposta() + " - " + obj.getQuantidadePessoa() + "(" + Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais((obj.getQuantidadePessoa().doubleValue() * 100 / qtdeTotal.doubleValue()))) + "%)", obj.getQuantidadePessoa());
				}
			}
			if (getTipoSimplesEscolha()) {
				setGraficoRespostaPizza(new DefaultPieDataset());
				for (RespostaRelVO obj : respostaTexto) {
					getGraficoRespostaPizza().setValue(obj.getNomeResposta() + " - " + obj.getQuantidadePessoa() + "(" + Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais((obj.getQuantidadePessoa().doubleValue() * 100 / qtdeTotal.doubleValue()))) + "%)", obj.getQuantidadePessoa());
				}
			}

		} catch (Exception se) {
			throw new RuntimeException(se);
		}
	}

	public void criarGraficoPergunta(SqlRowSet dadosSql) {

		try {
			setGraficoPergunta(new DefaultPieDataset());
			while (dadosSql.next()) {
				Integer pesoPergunta = new Integer(dadosSql.getInt("pesoPergunta"));
				Integer peso = new Integer(dadosSql.getInt("peso"));
				if (pesoPergunta.intValue() == 1) {
					getGraficoPergunta().setValue("Sem Importancia", peso);
				}
				if (pesoPergunta.intValue() == 2) {
					getGraficoPergunta().setValue("Pouco Importante", peso);
				}
				if (pesoPergunta.intValue() == 3) {
					getGraficoPergunta().setValue("Importante", peso);
				}
				if (pesoPergunta.intValue() == 4) {
					getGraficoPergunta().setValue("Muito Importante", peso);
				}
				if (pesoPergunta.intValue() == 5) {
					getGraficoPergunta().setValue("Importantíssimo", peso);
				}

			}

		} catch (Exception se) {
			throw new RuntimeException(se);
		}
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public DefaultPieDataset getGraficoPergunta() {
		return graficoPergunta;
	}

	public void setGraficoPergunta(DefaultPieDataset graficoPergunta) {
		this.graficoPergunta = graficoPergunta;
	}

	public DefaultPieDataset getGraficoRespostaPizza() {
		return graficoRespostaPizza;
	}

	public void setGraficoRespostaPizza(DefaultPieDataset graficoRespostaPizza) {
		this.graficoRespostaPizza = graficoRespostaPizza;
	}

	public List<RespostaPerguntaVO> getRespostaPerguntaVOs() {
		return respostaPerguntaVOs;
	}

	public void setRespostaPerguntaVOs(List<RespostaPerguntaVO> respostaPerguntaVOs) {
		this.respostaPerguntaVOs = respostaPerguntaVOs;
	}

	public List<RespostaRelVO> getRespostaTexto() {
		return respostaTexto;
	}

	public void setRespostaTexto(List<RespostaRelVO> respostaTexto) {
		this.respostaTexto = respostaTexto;
	}

	public String getTipoResposta() {
		return tipoResposta;
	}

	public void setTipoResposta(String tipoResposta) {
		this.tipoResposta = tipoResposta;
	}

	public DefaultCategoryDataset getGraficoRespostaBarra() {
		return graficoRespostaBarra;
	}

	public void setGraficoRespostaBarra(DefaultCategoryDataset graficoRespostaBarra) {
		this.graficoRespostaBarra = graficoRespostaBarra;
	}

	public Integer getTotalPessoas() {
		if (totalPessoas == null) {
			totalPessoas = 0;
		}
		return totalPessoas;
	}

	public void setTotalPessoas(Integer totalPessoas) {
		this.totalPessoas = totalPessoas;
	}

	public Integer getNrPergunta() {
		if (nrPergunta == null) {
			nrPergunta = 1;
		}
		return nrPergunta;
	}

	public void setNrPergunta(Integer nrPergunta) {
		this.nrPergunta = nrPergunta;
	}

	public String graficoPizza;

	public String getGraficoPizza() {
		if (graficoPizza == null) {
			StringBuilder grafico = new StringBuilder("");
			Integer altura = (getRespostaTexto().size()*20)+300;
			if(altura < 400){
				altura = 400;
			}
			grafico.append("<div id=\"container").append(getCodigo()).append("_pizza\" class=\"autosize\" style=\"height: "+altura+"px;\" /> ");

			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");			
			grafico.append("		var options").append(getCodigo()).append("_pizza = {");
			grafico.append(" 			chart : {");
			grafico.append(" 				type : 'pie',");
			grafico.append(" 				renderTo : 'container").append(getCodigo()).append("_pizza',");
			grafico.append(" 				backgroundColor : '#FFFFFF', ");
			grafico.append(" 				className : 'autosize', ");
			grafico.append(" 				width : (($(window).width() / 2) - 20) ");
			grafico.append(" 			},");
			grafico.append(" 			title : {");
			grafico.append(" 				text : ' '");
			grafico.append(" 			},");
			grafico.append(" 			credits : {");
			grafico.append(" 				enabled : false");
			grafico.append(" 			},");
			grafico.append(" 			exporting: { enabled: false }, ");
			grafico.append(" 			tooltip : {");
			grafico.append(" 				pointFormat: '{series.name}: <b>{point.percentage:.1f}% - {point.qtde}</b>' ");			
			grafico.append(" 				}, ");			
			grafico.append(" 			plotOptions : {");
			grafico.append(" 				pie : {");
			grafico.append(" 					allowPointSelect : true,");
			grafico.append(" 					cursor : 'pointer',");
			grafico.append(" 					dataLabels : {");
			grafico.append(" 						enabled : true,");
			grafico.append(" 						format: '<b>{point.name}</b>: {point.percentage:.1f}% - {point.qtde}'");
			grafico.append(" 					}, ");
			grafico.append(" 					showInLegend: true");
			grafico.append(" 				}");
			grafico.append(" 			},");

			grafico.append("			legend : {");
//			grafico.append("				layout : 'horizontal',");			
//			grafico.append("				align: 'bottom',");
//			grafico.append("				enabled: true,");
//			grafico.append("				useHTML: false,");
//			grafico.append("				verticalAlign: 'bottom',");	
			grafico.append(" 				width : (($(window).width() / 2) - 20), ");
			grafico.append("				maxHeight: 100,");					
			grafico.append("				labelFormat: '<b>{name}</b>: {percentage:.1f}% - {qtde}'");
			grafico.append("			}, ");

			grafico.append(" 		series : [ {");			
			grafico.append(" 			name : 'Resposta',");
			grafico.append(" 			colorByPoint: true,");
			grafico.append(" 			data : [ ");
			Boolean virgula = false;
			StringBuilder data = new StringBuilder("");
			
			for (RespostaRelVO obj : respostaTexto) {
				if (virgula) {
					data.append(", ");
					grafico.append(", ");
				}				
				data.append(" {name: '").append(obj.getNomeResposta()).append("', y:").append(obj.getPercentual()).append(", qtde:").append(obj.getQuantidadePessoa()).append("} ");
				grafico.append(" {name: '").append(obj.getNomeResposta()).append("', y:").append(obj.getPercentual()).append(", qtde:").append(obj.getQuantidadePessoa()).append("} ");
				virgula = true;
			}
			grafico.append(" 			]");
			grafico.append(" 		} ]");
			grafico.append(" 	};");
			grafico.append(" 	var chart").append(getCodigo()).append("_pizza = new Highcharts.chart(options").append(getCodigo()).append("_pizza);");
			grafico.append(" }(jQuery));");
			grafico.append(" </script>");
			graficoPizza = grafico.toString();
		}
		return graficoPizza;
	}
	
	
	public String graficoBarra;

	public String getGraficoBarra() {
		if (graficoBarra == null) {
			StringBuilder grafico = new StringBuilder("");
			Integer altura = (getRespostaTexto().size()*20)+300;
			if(altura < 400){
				altura = 400;
			}
			grafico.append("<div id=\"container").append(getCodigo()).append("\" class=\"autosize\" style=\"width: 98%; height: "+altura+"px;\" /> ");

			grafico.append("<script type=\"text/javascript\" charset=\"UTF-8\"> ");
			grafico.append("	(function($) { ");
			
			grafico.append("	$.ajaxSetup({");
			grafico.append("		cache : false");
			grafico.append("	});");
			
			grafico.append("		var options").append(getCodigo()).append(" = {");
			grafico.append(" 			chart : {");
			grafico.append(" 				type : \"column\",");
			grafico.append(" 				renderTo : \"container").append(getCodigo()).append("\",");
			grafico.append(" 				backgroundColor : \"#FFF\", ");	
			grafico.append(" 				className : 'autosize', ");
			grafico.append(" 				width : (($(window).width() / 2) - 20) ");
			grafico.append(" 			},");
			grafico.append(" 			title : {");
			grafico.append(" 				text : ''");
			grafico.append(" 			},");
			grafico.append(" 			credits : {");
			grafico.append(" 				enabled : false");
			grafico.append(" 			},");
			grafico.append(" 			exporting: { enabled: false }, ");
			grafico.append(" 			tooltip : {");
			grafico.append(" 				formatter : function() {");
			grafico.append("					return this.series.name;");
			grafico.append(" 				}");
			grafico.append(" 			},");
			grafico.append(" 			xAxis:{");
			grafico.append(" 				categories: [ ' ' ], ");									      
			grafico.append(" 			},");															
			grafico.append(" 			yAxis: {");
			grafico.append(" 				allowDecimals: false,");
			grafico.append(" 				title:{");
			grafico.append(" 					text:''");
			grafico.append(" 				},");
			grafico.append(" 				labels: {");
			grafico.append(" 					formatter: function() {");
			grafico.append(" 						return this.value;");   
			grafico.append(" 					},");															      
			grafico.append(" 					style:{");
			grafico.append(" 						fontSize: '10px'");															        	
			grafico.append(" 					}");			        
			grafico.append(" 				},");
			grafico.append(" 				marginLeft:0");		           														                
			grafico.append(" 			},");	
			grafico.append("		legend : {");
			grafico.append("			width : (($(window).width() / 2) - 20) ");																
			grafico.append("		}, ");

			grafico.append(" 		series : [ ");					
			
			Boolean virgula = false;
			
			for (RespostaRelVO obj : respostaTexto) {
				if (virgula) {
					grafico.append(", ");
				}
				grafico.append(" 		{ name : '").append(obj.getNomeResposta()+" - "+obj.getQuantidadePessoa()+": "+obj.getPercentual()).append("%', ");
				grafico.append(" 		  data : [ ");
				grafico.append(" ").append(obj.getQuantidadePessoa()).append(" ");
				virgula = true;
				grafico.append(" 			]}");
			}
			
			grafico.append(" 		]");
			grafico.append(" 	};");
			grafico.append(" 	var chart").append(getCodigo()).append(" = new Highcharts.Chart(options").append(getCodigo()).append(");");
			grafico.append(" }(jQuery));");
			grafico.append(" </script>");
			graficoBarra = grafico.toString();
		}
		return graficoBarra;
	}

	public Boolean getApresentarGraficoPizza() {		
		return !getTipoTexto() && getTipoResultadoGrafico().equals(TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_PIZZA);
	}
	
	public Boolean getApresentarGraficoColuna() {		
		return !getTipoTexto() && getTipoResultadoGrafico().equals(TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_COLUNA);
	}
	
	public Boolean getApresentarLista() {		
		return !getTipoTexto() && getTipoResultadoGrafico().equals(TipoLayoutApresentacaoResultadoPerguntaEnum.LISTA);
	}

	public TipoLayoutApresentacaoResultadoPerguntaEnum getTipoResultadoGrafico() {
		if(tipoResultadoGrafico == null){
			if(getTipoSimplesEscolha()){
				tipoResultadoGrafico = TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_PIZZA;
			}
			if(getTipoMultiplaEscolha()){
				tipoResultadoGrafico = TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_COLUNA;
			}
		}
		return tipoResultadoGrafico;
	}

	public void setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum tipoResultadoGrafico) {
		this.tipoResultadoGrafico = tipoResultadoGrafico;
	}

	public String getRespostaTextual() {
		if(respostaTextual == null){
			respostaTextual = "";
		}
		return respostaTextual;
	}

	public void setRespostaTextual(String respostaTextual) {
		this.respostaTextual = respostaTextual;
	}

	public boolean isRespostaObrigatoria() {
		return respostaObrigatoria;
	}

	public void setRespostaObrigatoria(boolean respostaObrigatoria) {
		this.respostaObrigatoria = respostaObrigatoria;
	}
	
	public boolean isAgruparReposta() {
		return agruparReposta;
	}

	public void setAgruparReposta(boolean agruparRepostas) {
		this.agruparReposta = agruparRepostas;
	}
	
}
