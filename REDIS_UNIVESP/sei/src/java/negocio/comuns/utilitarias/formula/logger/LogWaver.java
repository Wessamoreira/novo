package negocio.comuns.utilitarias.formula.logger;

import java.util.HashMap;
import java.util.Map;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.comuns.utilitarias.formula.chain.ChainLinkTemplate;
import negocio.comuns.utilitarias.formula.chain.annotations.Logger;
import negocio.comuns.utilitarias.formula.common.StringFormula;

@Logger
class LogWaver extends ChainLinkTemplate  {

	private static final long serialVersionUID = 7533201008773629065L;

	private static final String VAR = "var";
	private static final String FUNCOES = "funcoes";
	private static final String MAPA = "mapa";
	private static final String EVENTOS = "eventos";
	private static final String IF = "if";
	private static final String ELSE = "else";
	private static final String CONTEXTO = "contexto";
	private static final String RETORNO = "return";
	
	public String ifFormula = "";

	@Override
	public StringFormula execute(StringFormula script) {
		return new StringFormula();
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario) {
		return this.execute(script, funcionario, null);
	}

	@Override
	public StringFormula execute(StringFormula script, FuncionarioCargoVO funcionario, Map<String, Object> eventos) {
		Map<String, Object> valoresVariaveis = new HashMap<>();
		StringFormula logger = new StringFormula();
		
		String novoScript = montarNovoScriptFormatado(script);

		String[] formulaArray = novoScript.toString().split(";");
		int contador = 1;
		for (String linhaFormula : formulaArray) {
			if (Uteis.isAtributoPreenchido(linhaFormula)) {
				// ';' define a quebra de linha do log. 
				logger.append("logger.append(';//Processamento Linha " + contador + "' + ';');");
				logger.append(processarStringFormula(linhaFormula, funcionario, valoresVariaveis));
				contador++;
			}
		}

		return logger.replaceAll(":", "{").replaceAll("@", "}");
	}

	private String montarNovoScriptFormatado(StringFormula script) {
		String novoScript = script.toString().trim().replaceAll("(\\r\\n|\\n)", "");
		int i = 0;
		if (novoScript.contains("{") || novoScript.contains("}")) {
			char value[] = new char[novoScript.length()];
			
			for (char caracter: novoScript.toCharArray()) {
				if (caracter != ' ') {
					value[i] = caracter;
				}
				i ++;
			}
			
			i= 0;
			while(i <= (value.length - 1)) {
				if (value[i] == '{' ) {
					novoScript = novoScript.replace(novoScript.substring(i, i+ 1), ":");
				}else if (value[i] == '}') {
					novoScript = novoScript.replace(novoScript.substring(i, i+ 1), "@");
				}
				i++;
			}
		}

		return novoScript;
	}

	/**
	 * Processa as linhas da formula informado para geraçção do Log da Formaula.
	 * 
	 * @param linhaScript
	 * @param funcionarioCargo
	 * @param valoresVariaveis
	 * @return
	 */
	private String processarStringFormula(String linhaScript, FuncionarioCargoVO funcionarioCargo, Map<String, Object> valoresVariaveis) {
		linhaScript = linhaScript.trim();
		StringBuilder logger = new StringBuilder("");

		substituirQuebrasLinhas(linhaScript, logger);

		String[] partesLinhaScript = linhaScript.split("\\b\\s\\=");
		Boolean processado = false;
		
		for(String parte : partesLinhaScript) {
			montarStringLogVar(logger, parte);
			montarStringLogMapa(linhaScript, logger, parte);
			if (!processado && parte.contains(FUNCOES)) {
				montarStringLogFuncoes(logger, parte);
				processado = true;
			}
			montarStringLogEventos(logger, parte);
			montarStringLogContexto(logger, parte);
			//logger = montarStringLogCondicoes(linhaScript, logger);
		}

		if (linhaScript.contains(RETORNO)) {
			logger.append(linhaScript.endsWith(";") ? linhaScript : linhaScript + ";");
		}
		
		return logger.toString().replaceAll(";;", ";");
	}

	private void montarStringLogContexto(StringBuilder logger, String parte) {
		int quantidadePalavrasRepetidas;
		if(parte.contains(CONTEXTO)) {
			quantidadePalavrasRepetidas = UteisTexto.quantidadePalavrasRepetidas(parte, VAR);

			for(int i=0; i < quantidadePalavrasRepetidas;i++) {
				String contexto = recuperaStringAPartirValor(parte, "contexto");
				logger.append("logger.append('" +contexto + "= ') + ';' ;" );
				logger.append("logger.append(" +contexto + "().toString()); + ';'" );
			}
		}
	}

	/**
	 * Utiliza regex para substituir as quebras de linhas vindo da formula.
	 * 
	 * @param linhaScript
	 * @param logger
	 */
	private void substituirQuebrasLinhas(String linhaScript, StringBuilder logger) {
		if (!linhaScript.contains(RETORNO)) {
			logger.append(linhaScript.endsWith(";") ? linhaScript : linhaScript + ";");
		}

		if (linhaScript.contains(IF) || linhaScript.contains(ELSE)) {
			if (linhaScript.contains(ELSE)) {
				logger.append("logger.append(\"Linha Fórmula= ").append(ifFormula).append(linhaScript.replaceAll("(\\r\\n|\\n)", " ")).append(" \" + ").append(" '};' ); ");
			} else {
				logger.append("logger.append('").append(ifFormula.replaceAll("(\\r\\n|\\n)", " ")).append("') + ';' ;");
			}
		} else {
			logger.append("logger.append(\"Linha Fórmula= ").append(linhaScript).append(" \" + ").append(" ';' ); ");
		}
	}

	/**
	 * Montar string de log referente as variaveis da formula
	 * 
	 * @param logger
	 * @param parte
	 */
	private void montarStringLogVar(StringBuilder logger, String parte) {
		int quantidadePalavrasRepetidas;
		if(parte.contains(VAR)) {
			quantidadePalavrasRepetidas = UteisTexto.quantidadePalavrasRepetidas(parte, VAR);

			for(int i=0; i < quantidadePalavrasRepetidas;i++) {
				String mapa = processaStringVar(parte);
				logger.append("logger.append('").append(mapa).append("= '); ");
				if(parte.contains("=")) {
					logger.append("logger.append(").append(mapa).append(".toString()").append(" + ';');");
				} else {
					logger.append("logger.append(").append(mapa).append(" + ';');");
				}
			}
		}
	}

	/**
	 * Montar string de log referente ao mapa da formula
	 * 
	 * @param linhaScript
	 * @param logger
	 * @param parte
	 */
	private void montarStringLogMapa(String linhaScript, StringBuilder logger, String parte) {
		if (parte.contains(MAPA)) {
			String[] mapas = getMapaString(parte);

			for(int j=0; j < mapas.length; j++) {
				if (mapas[j].contains("VIGENCIA")) {
					logger.append("logger.append('").append(mapas[j]).append("= '); ");
					logger.append("logger.append(UteisData.getDataFormatada(" + mapas[j] + ") + ';' );");
				} else {
					logger.append("logger.append('").append(mapas[j]).append("= '); ");
					logger.append("logger.append(").append(mapas[j]).append(".toString()").append(" + ';');");
				}
			}
		
		}
	}

	/**
	 * Montar string de log referente as funcoes da formula.
	 * 
	 * @param logger
	 * @param parte
	 */
	private void montarStringLogFuncoes(StringBuilder logger, String parte) {
		int quantidadePalavrasRepetidas;
		if (parte.contains(FUNCOES)) {
			quantidadePalavrasRepetidas = UteisTexto.quantidadePalavrasRepetidas(parte, FUNCOES);

			for (int i = 0; i < quantidadePalavrasRepetidas; i++) {
				String funcoes = parte.substring(parte.indexOf(FUNCOES), parte.length());
				logger.append("logger.append(").append("'Função= '").append("); ");
				logger.append("logger.append(").append(funcoes).append("").append(" +  ';');");
			}
		}
	}

	/**
	 * Montar string de log referente aos eventos da formula.
	 * 
	 * @param logger
	 * @param parte
	 */
	private void montarStringLogEventos(StringBuilder logger, String parte) {
		if (parte.contains(EVENTOS)) {
			String[] eventos = getEventosString(parte);

			for(int i=0; i < eventos.length; i++) {
				logger.append("logger.append('").append(eventos[i]).append("= ');");					
				logger.append("logger.append(").append(eventos[i]).append(".toString() +  ';');");					
			}
		}
	}

	/**
	 * Montar string de log referente as condições if e else
	 * 
	 * @param linhaScript
	 * @param logger
	 * @return
	 */
	private StringBuilder montarStringLogCondicoes(String linhaScript, StringBuilder logger) {
		if (linhaScript.contains(IF)) {
			ifFormula = linhaScript.replaceAll("(\\r\\n|\\n)".trim(), " ");
			String condicao = processaStringCondicoes(linhaScript, IF);
			String blocoCodigo = processaStringCondicoes(linhaScript + ";", "{").replace("{", "");
			String processando = logger.toString().replace(blocoCodigo, "");
			logger = new StringBuilder().append(processando);

			logger.append("logger.append('Retornou: ');");
			logger.append("logger.append( ").append(condicao.replace(IF, "")).append(".toString());");
			logger.append(blocoCodigo + ";");
		} else if(linhaScript.contains(ELSE)) {
			String blocoCodigo = processaStringCondicoes(linhaScript + ";", "{").replace("{", "");
			String processando = logger.toString().replace(blocoCodigo + ";", "");
			logger = new StringBuilder().append(processando);

			logger.append("logger.append('Retornou: False');");
			if (!blocoCodigo.equals("{")) {
				logger.append(blocoCodigo + ";");
			}
		}
		return logger;
	}

	private String processaStringVar(String script) {
		StringBuilder sb = new StringBuilder();

		if (script.contains("var ")) {
			int contador = 1;
			int primeiraPosicaoVazio = 0;
			for (char caracter: script.toCharArray()) {
				if (caracter == ' ' || caracter ==  '=' || ++contador == script.length()) {
					if (primeiraPosicaoVazio != 0) {
						return script.substring(primeiraPosicaoVazio, contador);
					}
					primeiraPosicaoVazio = contador;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Recupera uma string de um determinado indice a partir do valor pretentido, a string
	 * e percorrida até encontrar um caracter especial.
	 * ex: recuperaStringAPartirValor('var salario = mapa.BASE_IRRF;', 'mapa').
	 * retorno = mapa.BASE_IRRF
	 * 
	 * @param script
	 * @param valor
	 * @return
	 */
	private String recuperaStringAPartirValor(String script , String valor) {
		StringBuilder sb = new StringBuilder();
		if (script.contains(valor)) {
			int index = script.indexOf(valor);
			char value[] = new char[script.length()];
			int contador = 0;
			for (char caracter: script.toCharArray()) {
				value[contador] = caracter;
				contador ++;
			}
			contador = index;

			while(contador <= value.length) {
				if (contador == value.length) {
					return script.substring(index, contador);
				}
				if (value[contador] == ',' || value[contador] == ';' || value[contador] == ' '|| value[contador] == '+' ||
						value[contador] == '-' || value[contador] == '/' || value[contador] == '*' || value[contador] == '('
						|| value[contador] == ')') {
					return script.substring(index, contador);
				}
				contador++;
			}
		}
		return sb.toString();
	}
	
	private String processaStringCondicoes(String script , String valor) {
		StringBuilder sb = new StringBuilder();
		if (script.contains(valor)) {
			int index = script.indexOf(valor);
			char value[] = new char[script.length()];
			int contador = 0;
			for (char caracter: script.toCharArray()) {
				value[contador] = caracter;
				contador ++;
			}
			contador = index;
			contador++;
			while(contador <= value.length) {
				if (contador == value.length) {
					return script.substring(index, contador);
				}
				if (value[contador] == ';' || value[contador] == '{' ) {
					return script.substring(index, contador);
				}
				contador++;
			}
		}
		return sb.toString();
	}

	/**
	 * Calcula a quantidade de vezes que uma palavra 'eventos' se repete em uma String.
	 * 
	 * @param texto
	 * @param palavraChave
	 * @return
	 */
	public String[] getEventosString(String texto) {
		String [] eventos = new String[UteisTexto.quantidadePalavrasRepetidas(texto, "eventos")];
		String [] arrayString = texto.replace("+", " ").replace("-", " ").replace("*", " ").replace("/", " ").split("[\\(\\)\\-\\ ]");
		int contadorEventos = 0;
		for(int i=0;i<arrayString.length;i++){
			if(arrayString[i].contains("eventos")){
				if ("eventos".equals( arrayString[i].substring(0, "eventos".length() ))) {
					eventos[contadorEventos] = arrayString[i].replace(")", "");
				} else {
					eventos[contadorEventos] = recuperaStringAPartirValor(arrayString[i], EVENTOS);
				}
				contadorEventos++;
			}
		}
		return eventos;
	}

	/**
	 * Calcula a quantidade de vezes que uma palavra 'mapa' se repete em uma String.
	 * 
	 * @param texto
	 * @param palavraChave
	 * @return
	 */
	public String[] getMapaString(String texto) {
		String [] mapas = new String[UteisTexto.quantidadePalavrasRepetidas(texto, "mapa")];
		String [] arrayString = texto.split(" ");
		int contadorEventos = 0;
		for(int i=0;i<arrayString.length;i++){
			if(arrayString[i].contains("mapa")){
				mapas[contadorEventos] = recuperaStringAPartirValor(arrayString[i], "mapa");
				contadorEventos++;
			}
		}
		return mapas;
	}
}
