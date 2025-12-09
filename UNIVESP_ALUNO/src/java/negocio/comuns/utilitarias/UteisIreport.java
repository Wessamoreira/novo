package negocio.comuns.utilitarias;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class UteisIreport extends Uteis{

	public static String valorExtenso(String... obj) {

		Double valorExtenso = 0.0;

		for (int I = 0; I < obj.length; I++) {
			String variavel = obj[I];

			double valorObj = Double.parseDouble(variavel.replace(".", "").replaceAll(",", "."));
			valorExtenso = valorExtenso + (valorObj);

		}

		Extenso extenso = new Extenso();

		extenso.setNumber(valorExtenso);
		return extenso.toString().substring(0, 1).toUpperCase() + extenso.toString().substring(1);
	}

	public static String preposicaoEstado(String obj) {

		String preposicao = obj;

		switch (preposicao) {

		case "AC":
			preposicao = "do Acre";
			break;
		case "AL":
			preposicao = "de Alagoas";
			break;
		case "AP":
			preposicao = "do Amapá";
			break;
		case "AM":
			preposicao = "do Amazonas";
			break;
		case "BA":
			preposicao = "da Bahia";
			break;
		case "CE":
			preposicao = "do Ceará";
			break;
		case "DF":
			preposicao = "do Distrito Federal";
			break;
		case "ES":
			preposicao = "do Espírito Santo";
			break;
		case "GO":
			preposicao = "de Goiás";
			break;
		case "MA":
			preposicao = "do Maranhão";
			break;
		case "MT":
			preposicao = "do Mato Grosso";
			break;
		case "MS":
			preposicao = "do Mato Grosso do Sul";
			break;
		case "MG":
			preposicao = "de Minas Gerais";
			break;
		case "PA":
			preposicao = "do Pará";
			break;
		case "PB":
			preposicao = "da Paraíba";
			break;
		case "PR":
			preposicao = "do Paraná";
			break;
		case "PE":
			preposicao = "de Pernambuco";
			break;
		case "PI":
			preposicao = "do Piauí";
			break;
		case "RJ":
			preposicao = "do Rio de Janeiro";
			break;
		case "RN":
			preposicao = "do Rio Grande do Norte";
			break;
		case "RS":
			preposicao = "do Rio Grande do Sul";
			break;
		case "RO":
			preposicao = "de Rondônia";
			break;
		case "RR":
			preposicao = "de Roraima";
			break;
		case "SC":
			preposicao = "de Santa Catarina";
			break;
		case "SP":
			preposicao = "de São Paulo";
			break;
		case "SE":
			preposicao = "do Sergipe";
			break;
		case "TO":
			preposicao = "do Tocantins";
			break;

		default:
			break;
		}

		return preposicao;
	}

	public static String primeiraLetraMaiuscula(String obj) {
		if (Uteis.isAtributoPreenchido(obj)) {
			String[] partes = obj.toLowerCase().split(" ");
			StringBuilder sb = new StringBuilder();
			boolean primeiro = true;
			for (String variavel : partes) {
				if (variavel.equals("do") || variavel.equals("da") || variavel.equals("de") || variavel.equals("di")
						|| variavel.equals("dos") || variavel.equals("das") || variavel.equals("e") || variavel.equals("")) {
					sb.append(primeiro ? "" : " ").append(variavel);
				} else {
					String[] novo = variavel.split("'");
					if (novo.length > 1) {
						sb.append(primeiro ? "" : " ");
						for (int i = 0; i < novo.length; i++) {
							sb.append((novo[i].length() > 1 ? novo[i].substring(0, 1).toUpperCase() + novo[i].substring(1) : novo[i]) + (i + 1 == novo.length ? "" : "'"));
						}
					} else {
						variavel = variavel.substring(0, 1).toUpperCase() + variavel.substring(1);
						sb.append(primeiro ? "" : " ").append(variavel);
					}
				}
				primeiro = false;
			}
			return sb.toString();
		}
		return "";
	}
	
//	public static String dataExtenso(Date dataFormatada) {
//		return UteisData.getDataPorExtenso(dataFormatada);
//	}
	
//	public static String dataExtenso(String obj) {
//
//		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
//		Date dataFormatada = null;
//		try {
//			if(!Uteis.isAtributoPreenchido(obj)) {
//				return "";
//			}
//			dataFormatada = formato.parse(obj);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return UteisData.getDataPorExtenso(dataFormatada);
//	}

	public static String somarValoresInteiros(String... obj) {

		int resultadoSoma = 0;

		for (int I = 0; I < obj.length; I++) {

			Integer variavel = Integer.parseInt(obj[I]);

			resultadoSoma = resultadoSoma + (variavel);

		}

		return String.valueOf(resultadoSoma).toString();
	}

	public static String valorInteiroExtenso(String... obj) {

		Double valorExtenso = Double.parseDouble(somarValoresInteiros(obj));

		Extenso extenso = new Extenso();

		extenso.setNumber(valorExtenso);
		return extenso.toString().substring(0, 1).toUpperCase() + extenso.toString().substring(1);
	}

	public static String calcularValores(boolean resultadoComValorExtenso, String operador, String... objs) {

		Double resultafoFinal = 0.0;
		Double numeros = 0.0;

		for (String valor : objs) {

			if (valor.contains("R$")) {
				numeros = Double.parseDouble(valor.replace("R$ ", "").replaceAll(",", "."));
			} else {
				numeros = Double.parseDouble(valor.replace(".", "").replace(",", "."));
			}

			if (resultafoFinal == 0.0) {
				resultafoFinal = numeros;
			} else {

				switch (operador) {

				case "+":
					resultafoFinal = resultafoFinal + numeros;
					break;
				case "-":
					resultafoFinal = resultafoFinal - numeros;
					break;
				case "*":
					resultafoFinal = resultafoFinal * numeros;
					break;
				case "/":
					resultafoFinal = resultafoFinal / numeros;
					break;
				}
			}
		}

		if (resultadoComValorExtenso) {
			Extenso extenso = new Extenso();
			extenso.setNumber(resultafoFinal);

			NumberFormat format = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
			format.setMinimumFractionDigits(2);

			String valorFormatado = format.format(resultafoFinal);

			return valorFormatado + "(" + extenso.toString().substring(0, 1).toUpperCase()
					+ extenso.toString().substring(1) + ")";

		} else {

			NumberFormat format = DecimalFormat.getCurrencyInstance(new Locale("pt", "BR"));
			format.setMinimumFractionDigits(2);

			String valorFormatado = format.format(resultafoFinal);

			return valorFormatado;
		}
	}
	
	

//	public static String getDataIinicioOufimDaSemana(String dataString, boolean isPrimeiro) throws Exception {
//		try {
//			Date data = UteisData.getData(dataString.toString());
//			
//			return UteisData.getDataIinicioOufimDaSemana(data, isPrimeiro);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	public static String getData(Date data, String pattern) {
		if (data != null) {
			SimpleDateFormat formatador = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
			return formatador.format(data);
		}
		return "";
	}
	
	public static Date getData(String data, String pattern) throws ParseException {
		try {
			if (data != null) {
				SimpleDateFormat formatador = new SimpleDateFormat(pattern);
				return formatador.parse(data);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String getAno(Date data) {
		return getData(data, "yyyy");
	}
	
	public static int getMesData(Date dataPrm) {
		Calendar dataCalendar = Calendar.getInstance();
		dataCalendar.setTime(dataPrm);

		int mes = dataCalendar.get(Calendar.MONTH) + 1;
		return mes;
	}
}

