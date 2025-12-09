package negocio.comuns.utilitarias;

import javax.servlet.http.HttpServletRequest;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;

public class UteisHTML {

	/**
	 * Este método cria uma nova página no texto html de um editor de texto
	 * 
	 * @param textoPadrao
	 * @param orientacaoPaginaEnum
	 * @return
	 */
	public static String realizarCriacaoNovaPaginaTexto(String textoPadrao, OrientacaoPaginaEnum orientacaoPaginaEnum) {
		StringBuilder sb = new StringBuilder();
		if (orientacaoPaginaEnum.equals(OrientacaoPaginaEnum.PAISAGEM)) {
			sb.append("<div class='page' style='width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;'>");
			sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;'><p>Nova Página</p></div>");
			sb.append("</div></body>");
		} else {
			sb.append("<div class='page' style='padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;'>");
			sb.append("<div class='subpage' style='border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;'><p>Nova Página</p></div>");
			sb.append("</div></body>");
		}
		return textoPadrao.replaceAll("</body>", sb.toString());
	}

	/**
	 * Este método adiciona na pagina do editor de texto html as margens
	 * personalizadas
	 * 
	 * @param textoPadrao
	 * @param marginSuperior
	 * @param marginInferior
	 * @param marginEsquerda
	 * @param marginDireita
	 * @param orientacaoPagina
	 * @return
	 */
	public static String realizarAlteracaoMargensPagina(String textoPadrao, String marginSuperior, String marginInferior, String marginEsquerda, String marginDireita, OrientacaoPaginaEnum orientacaoPagina, float alturaTopo, float alturaRodape, boolean corpo, boolean topo, boolean rodape) {
		StringBuilder sb = new StringBuilder();
		sb.append("padding-top: ").append(marginSuperior).append("cm; ");
		sb.append("padding-bottom: ").append(marginInferior).append("cm; ");
		sb.append("padding-left: ").append(marginEsquerda).append("cm; ");
		sb.append("padding-right: ").append(marginDireita).append("cm; ");
		if (orientacaoPagina.equals(OrientacaoPaginaEnum.PAISAGEM)) {
			sb.append("height: 150mm; ");
		} else {
			if (corpo) {
				sb.append("height: 237mm; ");
			} else if (topo) {
				sb.append("height: " + alturaTopo + "mm; ");
			} else if (rodape) {
				sb.append("height: " + alturaRodape + "mm; ");
			}
		}
		return realizarSubstituicaoValorAtribuidoClass(textoPadrao, ".page", sb.toString());
	}

	public static String realizarSubstituicaoValorAtribuidoClass(String texto, String classe, String valor) {
		String parte1, parte2, novaClasse = "";
		parte1 = texto.substring(texto.indexOf(classe), texto.length());
		parte2 = parte1.substring(0, parte1.indexOf("}") + 1);
		novaClasse = classe + " { " + valor + " } ";
		return texto.replace(parte2, novaClasse);
	}
	
	public static String realizarSubstituicaoValorAtribuidoStyle(String texto, String tagStart, int tagStartAvancar, String tagEnd, int tagEndAvancar, String valor) {
		String parte1, parte2;
		parte1 = texto.substring(texto.indexOf(tagStart)+tagStartAvancar, texto.length());
		parte2 = parte1.substring(0, parte1.indexOf(tagEnd) + tagEndAvancar);		
		return texto.replace(parte2, valor);
	}

	public static String realizarAlteracaoOrientacaoPagina(String textoPadrao, OrientacaoPaginaEnum orientacaoPaginaEnum, float alturaTopo, float alturaRodape, boolean corpo, boolean topo, boolean rodape) {

		if (orientacaoPaginaEnum.equals(OrientacaoPaginaEnum.PAISAGEM)) {
			textoPadrao = textoPadrao.replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;");
			textoPadrao = textoPadrao.replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;");
			textoPadrao = realizarSubstituicaoValorAtribuidoClass(textoPadrao, ".page", "width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto;");
			textoPadrao = realizarSubstituicaoValorAtribuidoClass(textoPadrao, ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm;");
			textoPadrao = realizarSubstituicaoValorAtribuidoClass(textoPadrao, "@page", "size: A4 landscape; margin: 0;");
		} else {
			textoPadrao = textoPadrao.replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;");
			textoPadrao = textoPadrao.replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;");
			textoPadrao = realizarSubstituicaoValorAtribuidoClass(textoPadrao, ".page", "width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto;");
			textoPadrao = realizarSubstituicaoValorAtribuidoClass(textoPadrao, ".subpage", "padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm;");
			textoPadrao = realizarSubstituicaoValorAtribuidoClass(textoPadrao, "@page", "size: A4; margin: 0;");
		}
		return textoPadrao;
	}

	/**
	 * Formata a página de acordo com a orientacao (Paisagem, Retrato) e tambem
	 * de acordo com a margem;
	 * 
	 * @param textoPadrao
	 * @param marginSuperior
	 * @param marginInferior
	 * @param marginEsquerda
	 * @param marginDireita
	 * @param orientacaoPagina
	 * @return
	 */
	public static String realizarFormatacaoLayoutPagina(String textoPadrao, String marginSuperior, String marginInferior, String marginEsquerda, String marginDireita, OrientacaoPaginaEnum orientacaoPagina, float alturaTopo, float alturaRodape, boolean corpo, boolean topo, boolean rodape) {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
//		html.append("<head>");
//		html.append("<style type='text/css'>");
//		html.append(" body { margin: 0; padding: 0; font-size:11px; } ");
//		html.append(" th { font-weight: normal; } ");
//		html.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
		String cssPage = "";
		if (orientacaoPagina.equals(OrientacaoPaginaEnum.RETRATO)) {
			if (corpo) {
				cssPage = "width: 21cm; min-height: "+(29.7-alturaRodape-alturaTopo)+"cm; height: "+(29.7-alturaRodape-alturaTopo)+"cm; margin-left: " + marginEsquerda + "cm; margin-top:" + marginSuperior + "cm; margin-right:" + marginDireita + "cm; margin-bottom:" + marginInferior + "cm;";				
			} else if (topo) {
				cssPage = "width: 21cm; height: " + alturaTopo + "cm; min-height: " + alturaTopo + "cm; margin-left: " + marginEsquerda + "cm; margin-top:" + marginSuperior + "cm; margin-right:" + marginDireita + "cm;";				
			} else if (rodape) {
				cssPage = "width: 21cm; height: " + alturaRodape + "cm; min-height: " + alturaRodape + "cm; margin-left: " + marginEsquerda + "cm; margin-bottom:" + marginInferior + "cm; margin-right:" + marginDireita + "cm;";
			}
//			html.append(" @page { size: A4; margin: 0; } ");
		} else {
			if (corpo) {
				cssPage = "width: "+(29.7-alturaRodape-alturaTopo)+"cm; min-height: 21cm; height: 21cm; margin-left: " + marginEsquerda + "cm; margin-top:" + marginSuperior + "cm; margin-right:" + marginDireita + "cm; margin-bottom:" + marginInferior + "cm; ";
			} else if (topo) {
				cssPage = " width:" + alturaTopo + "cm ; min-height: 21cm; height: 21cm; margin-left: " + marginEsquerda + "cm; margin-top:" + marginSuperior + "cm; margin-right:" + marginDireita + "cm;  ";
			} else if (rodape) {
				cssPage = "width: " + alturaRodape + "cm; min-height: 21cm; height: 21cm; margin-left: " + marginEsquerda + "cm; margin-bottom:" + marginInferior + "cm; margin-right:" + marginDireita + "cm;  ";
			}
//			html.append(" @page { size: A4 landscape; margin: 0; } ");
		}
//		html.append(" .page { "+cssPage+" } ");

//		html.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
//		html.append("</style>");
//		html.append("</head>");
		html.append("<body>");

		String parte1, parte2, texto = "";
		texto = textoPadrao;
		parte1 = texto.substring(texto.indexOf("<body>") + 6, texto.length());
		parte2 = parte1.substring(0, parte1.indexOf("</body>"));
		texto = parte2;
		if (!(texto.contains("<div id=\"conteudo\""))) {
			html.append("<div id=\"conteudo\" style=\""+cssPage+"border: 1px #CCCCCC dashed;\">");
			html.append(texto);
			html.append("</div>");
		} else {
			parte1 = texto.substring(texto.indexOf("<div id=\"conteudo\" style=\"") + 26, texto.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);
			texto = texto.toString().replace(parte2, cssPage+"border: 1px #CCCCCC dashed;\">");
			html.append(texto);
		}
		
		html.append("</body>");
		html.append("</html>");

		textoPadrao = html.toString();
		
		
//		textoPadrao = realizarAlteracaoOrientacaoPagina(textoPadrao, orientacaoPagina, alturaTopo, alturaRodape, corpo, topo, rodape);
//		textoPadrao = realizarAlteracaoMargensPagina(textoPadrao, marginSuperior, marginInferior, marginEsquerda, marginDireita, orientacaoPagina, alturaTopo, alturaRodape, corpo, topo, rodape);

		return textoPadrao;
	}

	public static String realizarRemocaoBordaDaPagina(String texto) {
		texto = texto.replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "");
		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "");

		texto = texto.replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "");
		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "");
		return texto;
	}

	public static String realizarInclusaoStyleFormatoPaginaTextoPadrao(String texto, OrientacaoPaginaEnum orientacaoPagina) {

		StringBuilder sb = new StringBuilder();
		if (!texto.contains("<style type='text/css'>")) {
			if (orientacaoPagina.equals(OrientacaoPaginaEnum.RETRATO)) {
				sb.append("<head>");
				sb.append("<style type='text/css'>");
				sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
				sb.append(" th { font-weight: normal; } ");
				sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
				sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
				sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
				sb.append(" @page { size: A4; margin: 0; } ");
				sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
				sb.append("</style>");

				texto = texto.replace("<head>", sb.toString());
			} else {
				sb.append("<head>");
				sb.append("<style type='text/css'>");
				sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
				sb.append(" th { font-weight: normal; } ");
				sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
				sb.append(" .page { width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto; } ");
				sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm; } ");
				sb.append(" @page { size: A4 landscape; margin: 0; } ");
				sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
				sb.append("</style>");

				texto = texto.replace("<head>", sb.toString());
			}
		}

		return texto;
	}

	public static String realizarVisualizacaoTextoPadrao(String textoPadrao, OrientacaoPaginaEnum orientacaoPagina, HttpServletRequest request) {
		textoPadrao = realizarRemocaoBordaDaPagina(textoPadrao);
		textoPadrao = realizarInclusaoStyleFormatoPaginaTextoPadrao(textoPadrao, orientacaoPagina);
		request.getSession().setAttribute("textoRelatorio", textoPadrao);
		return "abrirPopup('faces/VisualizarContrato', 'RelatorioContrato', 730, 545);";
	}

	public static String realizarRemocaoTodasTagsHTML(String htmlString) {
		String noHTMLString = htmlString.replaceAll("<.*?>", "");
		noHTMLString = noHTMLString.replaceAll("r", "<br/>");
		noHTMLString = noHTMLString.replaceAll("\n", " ");
		noHTMLString = noHTMLString.replaceAll("'", "&#39;");
		noHTMLString = noHTMLString.replaceAll("\"/", "&quot;");
		return noHTMLString;
	}

	public static String adicionarImagem(String texto, String url, Double altura, Double largura) {
		int parametro = texto.lastIndexOf("</p>");
		if (parametro == -1) {
			parametro = texto.lastIndexOf("</body>");
		}
		String marcador = "<img src=\"" + url + "\"></img>";
		if (altura > 0 && largura > 0) {
			marcador = "<img src=\"" + url + "\" width=\"" + largura + " cm\" height=\"" + altura + " cm\" ></img>";
		} else if (largura > 0) {
			marcador = "<img src=\"" + url + "\" width=\"" + largura + " cm\" ></img>";
		} else if (altura > 0) {
			marcador = "<img src=\"" + url + "\" height=\"" + altura + " cm\" ></img>";
		}
		String textoAntes = texto.substring(0, parametro);
		String textoDepois = texto.substring(parametro, texto.length());
		texto = textoAntes + " " + marcador + textoDepois;

		return texto;
	}

	public static String adicionarImagemRodape(String texto, String url, Double altura, Double largura) {
		int parametro = texto.lastIndexOf("</p>");
		if (parametro == -1) {
			parametro = texto.lastIndexOf("</body>");
		}
		String marcador = "<img src=\"" + url + "\"></img>";
		if (altura > 0 && largura > 0) {
			marcador = "<img src=\"" + url + "\" width=\"" + largura + " cm\" height=\"" + altura + " cm\" ></img>";
		} else if (largura > 0) {
			marcador = "<img src=\"" + url + "\" width=\"" + largura + " cm\" ></img>";
		} else if (altura > 0) {
			marcador = "<img src=\"" + url + "\" height=\"" + altura + " cm\" ></img>";
		}
		String textoAntes = texto.substring(0, parametro);
		String textoDepois = texto.substring(parametro, texto.length());
		texto = textoAntes + " " + marcador + textoDepois;

		return texto;
	}

	public static String adicionarImagemTopo(String texto, String url, Double altura, Double largura) {
		int parametro = texto.lastIndexOf("</p>");
		if (parametro == -1) {
			parametro = texto.lastIndexOf("</body>");
		}
		String marcador = "<img src=\"" + url + "\"></img>";
		if (altura > 0 && largura > 0) {
			marcador = "<img src=\"" + url + "\" width=\"" + largura + " cm\" height=\"" + altura + " cm\" ></img>";
		} else if (largura > 0) {
			marcador = "<img src=\"" + url + "\" width=\"" + largura + " cm\" ></img>";
		} else if (altura > 0) {
			marcador = "<img src=\"" + url + "\" height=\"" + altura + " cm\" ></img>";
		}
		String textoAntes = texto.substring(0, parametro);
		String textoDepois = texto.substring(parametro, texto.length());
		texto = textoAntes + " " + marcador + textoDepois;

		return texto;
	}
	
	public static String realizarFormatacaoTopo(String topo, OrientacaoPaginaEnum orientacaoPagina, Float alturaPagina, Float larguraPagina, Float tamanhoTopo, Float marginEsquerda, Float marginDireita) throws Exception{
		if(topo.trim().isEmpty()){
			topo = htmlPadrao;			
		}
		alturaPagina = UteisTextoPadrao.converterPontosParaCentimetro(alturaPagina);
		larguraPagina = UteisTextoPadrao.converterPontosParaCentimetro(larguraPagina);
		Float width = (orientacaoPagina.equals(OrientacaoPaginaEnum.PAISAGEM)?alturaPagina:larguraPagina) -marginDireita-marginEsquerda;		
		String body = "<body style=\"width:"+width+"cm;height:"+tamanhoTopo+"cm\">";
		if(topo.contains("<body")){
			int inicio = topo.indexOf("<body");
			int termino = topo.substring(inicio, topo.length()).indexOf(">");		
			return topo.replace(topo.substring(inicio, inicio+termino+1), body);
		}
		throw new Exception("Não foi localizada a tag <body> no texto do topo, o mesmo deve ser incluido.");
		
	}
	
	public static String realizarFormatacaoRodape(String rodape, OrientacaoPaginaEnum orientacaoPagina, Float alturaPagina, Float larguraPagina, Float tamanhoRodape, Float marginEsquerda, Float marginDireita) throws Exception{
		if(rodape.trim().isEmpty()){
			rodape = htmlPadrao;			
		}
		alturaPagina = UteisTextoPadrao.converterPontosParaCentimetro(alturaPagina);
		larguraPagina = UteisTextoPadrao.converterPontosParaCentimetro(larguraPagina);
		Float width = (orientacaoPagina.equals(OrientacaoPaginaEnum.PAISAGEM)?alturaPagina:larguraPagina) -marginDireita-marginEsquerda;		
		String body = "<body style=\"width:"+width+"cm;height:"+tamanhoRodape+"cm\">";
		if(rodape.contains("<body")){
			int inicio = rodape.indexOf("<body");
			int termino = rodape.substring(inicio, rodape.length()).indexOf(">");		
			return rodape.replace(rodape.substring(inicio, inicio+termino+1), body);
		}
		throw new Exception("Não foi localizada a tag <body> no texto do rodapé, o mesmo deve ser incluido.");
		
	}
	
	public static String realizarFormatacaoBody(String corpo, OrientacaoPaginaEnum orientacaoPagina, Float alturaPagina, Float larguraPagina, Float tamanhoTopo, Float tamanhoRodape, Float marginEsquerda, Float marginDireita, Float marginTopo, Float marginRodape) throws Exception{
		if(corpo.trim().isEmpty()){
			corpo = htmlPadrao;			
		}
		alturaPagina = UteisTextoPadrao.converterPontosParaCentimetro(alturaPagina);
		larguraPagina = UteisTextoPadrao.converterPontosParaCentimetro(larguraPagina);
		Float width = (orientacaoPagina.equals(OrientacaoPaginaEnum.PAISAGEM)?alturaPagina:larguraPagina) -marginDireita-marginEsquerda;
		Float height = (orientacaoPagina.equals(OrientacaoPaginaEnum.PAISAGEM)?larguraPagina:alturaPagina) -marginTopo-marginRodape-tamanhoRodape-tamanhoTopo;
		String body = "<body style=\"width:"+width+"cm;height:"+height+"cm\">";
		if(corpo.contains("<body")){
			int inicio = corpo.indexOf("<body");
			int termino = corpo.substring(inicio, corpo.length()).indexOf(">");		
			return corpo.replace(corpo.substring(inicio, inicio+termino+1), body);
		}
		throw new Exception("Não foi localizada a tag body no texto do corpo, o mesmo deve ser incluido.");		
	}
	
	public static String htmlPadrao = "<html><body></body></html>";
	
	public static Rectangle getOrientacaoPagina(OrientacaoPaginaEnum orientacaoPaginaEnum){
		if (orientacaoPaginaEnum.equals(OrientacaoPaginaEnum.RETRATO)) {
			return PageSize.A4;
		} else {
			return new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
		}	
	}
	
	public static String realizarRemocaoTagsPageAndSubPageHTML(String texto) {		
		String parte1, parte2;
		if(texto.contains("<div class=\"page\"")){
			parte1 = texto.substring(texto.indexOf("<div class=\"page\""), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);		
			texto = texto.replaceAll(parte2, "<div>");	
		}
		if(texto.contains("<div class='page'")){
			parte1 = texto.substring(texto.indexOf("<div class='page'"), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);		
			texto = texto.replaceAll(parte2, "<div>");	
		}
		if(texto.contains("<div class=\"subpage\"")){
			parte1 = texto.substring(texto.indexOf("<div class=\"subpage\" "), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);
			texto = texto.replaceAll(parte2, "<div >");	
		}
		if(texto.contains("<div class='subpage'")){
			parte1 = texto.substring(texto.indexOf("<div class='subpage' "), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);
			texto = texto.replaceAll(parte2, "<div >");	
		}
		return texto;
	}
	
	public static String realizarValidacaoBackgroundConteudo(String texto) {		
		if(texto.contains("<body") && !texto.contains("<body style=\"background-color:transparent !important;background:transparent !important;\">")){
			String parte1, parte2;
			parte1 = texto.substring(texto.indexOf("<body"), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf(">") + 1);		
			texto = texto.replaceAll(parte2, "<body style=\"background-color:transparent !important;background:transparent !important;\">");	
		}
		return texto;
	}
	
}
