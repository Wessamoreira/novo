package negocio.comuns.utilitarias.faturamento.nfe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemanaJob;

public class UteisData {

    public static boolean isHojeIndependenteDaHora(Date data) throws Exception {
        Date hoje = new Date();
        return ((getDateHoraInicialDia(hoje).before(data) || getDateHoraInicialDia(hoje).equals(data))
                && (getDateHoraFinalDia(hoje).after(data) || getDateHoraFinalDia(hoje).equals(data)));
    }

    public static java.util.Date getDateHoraInicialDia(Date dataPrm) throws Exception {
        java.util.Date valorData = null;
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        valorData = formatador.parse(UteisData.getData(dataPrm));
        Calendar cal = Calendar.getInstance();

        cal.setTime(valorData);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }
    
    

    public static java.util.Date getDateHoraFinalDia(Date dataPrm) throws Exception {
        java.util.Date valorData = null;
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        valorData = formatador.parse(UteisData.getDataFormatMedium(dataPrm,""));
        Calendar cal = Calendar.getInstance();

        cal.setTime(valorData);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        return cal.getTime();
    }
    
    public static boolean isDiaSegunda(Date data) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(data);
        int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);
        switch (diaDaSemana) {
        	case Calendar.MONDAY:
        		return true;
        }
        return false;
    }
    
    public static Date getData30JunhoAnoAtual() {
    	Calendar calendar = new GregorianCalendar(getAnoData(new Date()), Calendar.JUNE ,30);
    	return calendar.getTime();
    }

    public static boolean isDiaSabado(Date data) {
        boolean retorno = false;
        Calendar cal = new GregorianCalendar();
        try {
            cal.setTime(data);
            int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);
            switch (diaDaSemana) {
                case Calendar.SATURDAY:
                    retorno = true;
            }
            return retorno;
        } finally {
            cal = null;
        }
    }

    @SuppressWarnings("static-access")
	public static Date getDataFuturaAvancandoHora(Date data, int hora) {
    	Calendar cal = new GregorianCalendar();
		cal.setTime(data);
    	cal.add(cal.HOUR, hora);
		return cal.getTime();
    }

    @SuppressWarnings("static-access")
    public static Date getDataFuturaAvancandoMinuto(Date data, int minuto) {
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(data);
    	cal.add(cal.MINUTE, minuto);
    	return cal.getTime();
    }

    /**
     * Método responsável por validar se data informada é dia da semana.
     * @param data
     * @return
     */
    public static boolean isDiaDaSemana(final Date data,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil) {
        boolean retorno = true;
        Calendar cal = new GregorianCalendar();
        try {
            cal.setTime(data);
            int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);
            if(diaDaSemana == Calendar.SUNDAY && !considerarDomingoDiaUtil) {
            	retorno = false;
            }
            if(diaDaSemana == Calendar.SATURDAY && !considerarSabadoDiaUtil) {
            	retorno = false;
            }           
            return retorno;
        } finally {
            cal = null;
        }
    }

    /**
     * Método responsável por verificar se data é dia útil.
     * @param date
     * @param feriados
     * @return
     * @throws Exception
     */
    public static boolean isDiaUtil(final Date date,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, List<FeriadoVO> feriados) throws Exception {
        if (!isDiaDaSemana(date, considerarSabadoDiaUtil, considerarDomingoDiaUtil)) {
            ////System.out.println(date.toString() + " é final de semana.");
            return false;
        }
        if (isFeriado(date, feriados)) {
            ////System.out.println(date.toString() + " é feriado.");
            return false;
        }
        return true;
    }

    /**
     * Método responsável por obter número de dias nao úteis de um período.
     * @param dataInicial
     * @param dataFinal
     * @param feriados
     * @return
     * @throws Exception
     */
    public static long obterDiasUteisNoPeriodo(final Date dataInicial, final Date dataFinal,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, List<FeriadoVO> feriados) throws Exception {
        Calendar dataInicio = Calendar.getInstance();
        Calendar dataFim = Calendar.getInstance();
        dataInicio.setTime(dataInicial);
        dataFim.setTime(dataFinal);

        long diasCorrido = nrDiasEntreDatas(dataFinal, dataInicial);
        long diasNaoUteis = obterDiasNaoUteisNoPeriodo(dataInicio, dataFim, considerarSabadoDiaUtil, considerarDomingoDiaUtil, feriados);


        return diasCorrido - diasNaoUteis;
    }

    public static long obterDiasNaoUteisNoPeriodo(final Calendar dataInicial, final Calendar dataFinal,  Boolean considerarSabadoDiaUtil,  Boolean considerarDomingoDiaUtil, List<FeriadoVO> feriados) throws Exception {
        Calendar dataInicio = Calendar.getInstance();
        Calendar dataFim = Calendar.getInstance();
        dataInicio.setTime(dataInicial.getTime());
        dataFim.setTime(dataFinal.getTime());

        long diasNaoUteis = 0l;
        if (dataInicio.equals(dataFim)) {
            if (!isDiaUtil(dataInicio.getTime(), considerarSabadoDiaUtil, considerarDomingoDiaUtil, feriados)) {
                return 1;
            } else {
                return 0;
            }
        } else if (dataFim.before(dataInicio)) {
            while (getCompareData(dataInicio.getTime(), dataFim.getTime()) >= 0) {
                if (!isDiaUtil(dataInicio.getTime(), considerarSabadoDiaUtil, considerarDomingoDiaUtil, feriados)) {
                    diasNaoUteis++;
                }
                dataInicio.add(Calendar.DAY_OF_MONTH, -1);
            }
        } else {
            while (getCompareData(dataInicio.getTime(), dataFim.getTime()) <= 0) {
                if (!isDiaUtil(dataInicio.getTime(), considerarSabadoDiaUtil, considerarDomingoDiaUtil, feriados)) {
                    diasNaoUteis++;
                }
                dataInicio.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return diasNaoUteis;
    }

    /**
     * Rotina depreciado por favor usar a mesma rotina que esta no facade de Feriado.
     * caso nrDiasProgredir seja positivo retornará data futura,
     * caso nrDiasProgredir seja negativo retornará data retroativa.
     * @param dataInicial
     * @param nrDiasProgredir
     * @param feriados
     * @return
     * @throws Exception
     */
//    @Deprecated 
//    public static Date obterDataFuturaOuRetroativaValidandoApenasDiasUteis(final Date dataInicial, final int nrDiasProgredir, List<FeriadoVO> feriados) throws Exception {
//        Calendar dataCalendarInicial = Calendar.getInstance();
//        Calendar dataCalendarFinal = Calendar.getInstance();
//        dataCalendarInicial.setTime(dataInicial);
//        dataCalendarFinal.setTime(dataInicial);
//
//        if (nrDiasProgredir >= 0) {
//            /* Obtendo data futura contando dias úteis. */
//            dataCalendarFinal.add(Calendar.DAY_OF_MONTH, nrDiasProgredir);
//            long quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, feriados);
//            while (quantidadeDiasNaoUteis > 0l) {
//                dataCalendarInicial.setTime(dataCalendarFinal.getTime());
//                dataCalendarInicial.add(Calendar.DAY_OF_MONTH, 1);
//                dataCalendarFinal.add(Calendar.DAY_OF_MONTH, (int) quantidadeDiasNaoUteis);
//                quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, feriados);
//            }
//            return dataCalendarFinal.getTime();
//        } else {
//            /* Obtendo data retroativa contando dias úteis. */
//            dataCalendarInicial.add(Calendar.DAY_OF_MONTH, nrDiasProgredir);
//            long quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, feriados);
//            while (quantidadeDiasNaoUteis > 0l) {
//                dataCalendarFinal.setTime(dataCalendarInicial.getTime());
//                dataCalendarFinal.add(Calendar.DAY_OF_MONTH, -1);
//                dataCalendarInicial.add(Calendar.DAY_OF_MONTH, (int) quantidadeDiasNaoUteis * -1);
//                quantidadeDiasNaoUteis = obterDiasNaoUteisNoPeriodo(dataCalendarInicial, dataCalendarFinal, feriados);
//            }
//            return dataCalendarInicial.getTime();
//        }
//    }

    public static Boolean getCompararDatas(Date data1, Date data2)  {
        if(data1 == null && data2 == null) {
        	return true;
        }else if((data1 == null && data2 != null)  || (data1 != null && data2 == null)) {
        	return false;
        }
    	int mes1 = getMesData(data1);
        int ano1 = getAnoData(data1);
        int dia1 = getDiaMesData(data1);

        int mes2 = getMesData(data2);
        int ano2 = getAnoData(data2);
        int dia2 = getDiaMesData(data2);

        if ((dia1 == dia2) && (mes1 == mes2) && (ano1 == ano2)) {
            return true;
        }
        return false;
    }
    
    /**
     *  Se o a primeira data for igual  a -1 a mesma e menor que a segunda data,
     *  se o a primeira data for igual  a 0 a mesma e igual que a segunda data,
     *  se o a primeira data for igual  a 1 a mesma e maior que a segunda data.
     *  
     * @param data1
     * @param data2
     * @return
     * @throws ParseException
     */
    public static Integer getCompareDataComHora(Date data1, Date data2) throws ParseException {
        if (data1.compareTo(data2) > 0) {
            return 1;
        } else if (data1.compareTo(data2) == 0) {
            return 0;
        } else if (data1.compareTo(data2) < 0) {
            return -1;
        }
        return null;
    }

    public static Integer getCompareData(Date data1, Date data2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String a = format.format(data1);
        String b = format.format(data2);
        data1 = format.parse(a);
        data2 = format.parse(b);
        if (data1.compareTo(data2) > 0) {
            return 1;
        } else if (data1.compareTo(data2) == 0) {
            return 0;
        } else if (data1.compareTo(data2) < 0) {
            return -1;
        }
        return null;
    }
    
    public static boolean isDataDentroDoPeriodo(Date periodoInicial, Date periodoFinal, Date data) throws ParseException {
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    	periodoInicial = format.parse(format.format(periodoInicial));
    	periodoFinal = format.parse(format.format(periodoFinal));
    	data = format.parse(format.format(data));
    	if(data.before(periodoInicial) || data.after(periodoFinal)) {
    		return false;
    	}
    	return true;
    }

    /**
     * Método responsável por verificar se data informada faz parte de algum feriado.
     * @param date
     * @param feriados
     * @return
     * @throws Exception
     */
    public static boolean isFeriado(final Date date, List<FeriadoVO> feriados) throws Exception {
        for (FeriadoVO feriado : feriados) {
            if (validarSeDataCoincideComFeriado(date, feriado)) {
                return true;
            }
//            if (getCompareData(date, feriado.getData()) == 0) {
//                return true;
//            }
        }
        return false;
    }

    public static boolean isFeriadoFinanceiro(final Date date, List<FeriadoVO> feriados) throws Exception {
    	for (FeriadoVO feriado : feriados) {
    		if (feriado.getConsiderarFeriadoFinanceiro() && validarSeDataCoincideComFeriado(date, feriado)) {
    			return true;
    		}
    	}
    	return false;
    }

    public static boolean validarSeDataCoincideComFeriado(Date date, FeriadoVO feriado) throws Exception {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Calendar feriadoCalendar = Calendar.getInstance();
        feriadoCalendar.setTime(feriado.getData());
        if (dateCalendar.get(Calendar.DAY_OF_MONTH) == feriadoCalendar.get(Calendar.DAY_OF_MONTH)
                && dateCalendar.get(Calendar.MONTH) == feriadoCalendar.get(Calendar.MONTH)
                && (dateCalendar.get(Calendar.YEAR) == feriadoCalendar.get(Calendar.YEAR) || feriado.getRecorrente())) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean getValidaDiaUtil(Date dataInicial) throws Exception {
        Calendar calInicial = Calendar.getInstance();
        calInicial.setTime(dataInicial);
        if (calInicial.get(Calendar.DAY_OF_WEEK) == 1 || calInicial.get(Calendar.DAY_OF_WEEK) == 7) {
            return false;
        }
        return true;
    }

    public static String getData(java.util.Date dataConverter, String padrao) {
        if (dataConverter == null) {
            return ("");
        }
        String dataStr;
        if (padrao.equals("bd")) {
            Locale aLocale = new Locale("pt", "BR");
            SimpleDateFormat formatador = new SimpleDateFormat("yyyy.MM.dd", aLocale);
            dataStr = formatador.format(dataConverter);
        } else {
            DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
            dataStr = formatador.format(dataConverter);
        }
        return (dataStr);
    }
    
    
    public static String getDataFormatMedium(java.util.Date dataConverter, String padrao) {
        if (dataConverter == null) {
            return ("");
        }
        String dataStr;
        if (padrao.equals("bd")) {
            Locale aLocale = new Locale("pt", "BR");
            SimpleDateFormat formatador = new SimpleDateFormat("yyyy.MM.dd", aLocale);
            dataStr = formatador.format(dataConverter);
        } else {
            DateFormat formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
            dataStr = formatador.format(dataConverter);
        }
        return (dataStr);
    }

    public static String getData(java.util.Date dataConverter) {
        return (getData(dataConverter, ""));
    }

    public static String getDataFormatoBD(java.util.Date dataConverter) {
        return getData(dataConverter, "bd");
    }

    public static String getAnoMes(Date data) {
        String dataStr = getData(data);
        dataStr = UteisTexto.removerMascara(dataStr);
        String mes = dataStr.substring(2, 4);
        String ano = dataStr.substring(4, dataStr.length());
        return ano + mes;
    }

    public static String getAnoMesDia(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        String mes = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (mes.length() == 1) {
            mes = "0" + mes;
        }
        String ano = String.valueOf(calendar.get(Calendar.YEAR));
        String dia = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (dia.length() == 1) {
            dia = "0" + dia;
        }
        return ano + "-" + mes + "-" + dia;
    }

    public static int getDiaMesData(Date dataPrm) {
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(dataPrm);

        int dia = dataCalendar.get(Calendar.DAY_OF_MONTH);
        return dia;
    }

    public static int getMesData(Date dataPrm) {
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(dataPrm);

        int mes = dataCalendar.get(Calendar.MONTH) + 1;
        return mes;
    }

    public static String getMesDataZeroEsquerda(Date dataPrm) {
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(dataPrm);
        return String.format("%02d", dataCalendar.get(Calendar.MONTH) + 1);
    }
    
    public static int getAnoData(Date dataPrm) {
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(dataPrm);

        int ano = dataCalendar.get(Calendar.YEAR);
        return ano;
    }
    
    public static String getAnoDataString(Date dataPrm) {
    	return String.valueOf(getAnoData(dataPrm));
    }

    public static Date obterDataFuturaMesContabilConsiderandoDiaInicial(Date dataInicial, int nrTotalDiasProgredir) throws Exception {
    	
    	Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_MONTH, nrTotalDiasProgredir);
		Date dataFutura = calendario.getTime();
        return dataFutura;
    }

    public static Date obterDataFuturaMesContabilConsiderandoDiaInicial(Date dataInicial, int nrTotalDiasProgredir, int diasMes) throws Exception {
        if (dataInicial == null) {
            return null;
        }
        int nrMesesProgredir = nrTotalDiasProgredir / diasMes;
        int nrDiasProgredir = nrTotalDiasProgredir - (diasMes * nrMesesProgredir);

        int dia = getDiaMesData(dataInicial);
        int mes = getMesData(dataInicial);
        int ano = getAnoData(dataInicial);

        // PROGREDINDO OS ANOS
        if (nrMesesProgredir > 12) {
            while (nrMesesProgredir > 12) {
                ano++;
                nrMesesProgredir += -12;
            }
        }

        // PROGREDINDO OS MESES
        mes += nrMesesProgredir;
        if (mes > 12) {
            mes -= 12;
            ano++;
        }

        // PROGREDINDO OS DIAS;
        boolean incrementarMes = false;
        if ((dia + nrDiasProgredir) > diasMes) {
            int diasMesPassado = obterNrDiasNoMes(dataInicial);
            dia = (dia + nrDiasProgredir) - diasMesPassado;
            incrementarMes = true;
        } else {
            dia = dia + nrDiasProgredir;
        }

        if (incrementarMes) {
            mes++;
            if (mes == 13) {
                mes = 1;
                ano++;
            }
        }
        dia--;
        Date dataFutura = getDate(dia + "/" + mes + "/" + ano);
        return dataFutura;
    }

    public static Date getDataVencimentoPadrao(Integer diaPadrao, Date dataPadrao, int nrMesesProgredir) throws Exception {
        int mes = getMesData(dataPadrao);
        int ano = getAnoData(dataPadrao);
        String diaFinal = String.valueOf(diaPadrao);

        mes = mes + nrMesesProgredir;
        if (mes > 12) {
            mes = mes - 12;
            ano = ano + 1;
        }

        String mesFinal = String.valueOf(mes);
        String anoFinal = String.valueOf(ano);

        if (String.valueOf(mesFinal).length() == 1) {
            mesFinal = "0" + mesFinal;
        }
        if (String.valueOf(diaFinal).length() == 1) {
            diaFinal = "0" + diaFinal;
        }
        String dataFinal = diaFinal + "/" + mesFinal + "/" + anoFinal;
        return getDate(dataFinal);
    }

    public static java.util.Date getDate(String data) throws Exception {
        java.util.Date valorData = null;
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        valorData = formatador.parse(data);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);
        int segundo = cal.get(Calendar.SECOND);

        cal.setTime(valorData);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, segundo);

        return cal.getTime();
    }
    
	public static boolean isDataValida(String data) {
		try {
			java.util.Date valorData = null;
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
			valorData = formatador.parse(data);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			int hora = cal.get(Calendar.HOUR_OF_DAY);
			int minuto = cal.get(Calendar.MINUTE);
			int segundo = cal.get(Calendar.SECOND);
			
			cal.setTime(valorData);
			cal.set(Calendar.HOUR_OF_DAY, hora);
			cal.set(Calendar.MINUTE, minuto);
			cal.set(Calendar.SECOND, segundo);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

    public static java.util.Date getDate(String data, Locale local) throws Exception {
        java.util.Date valorData = new Date();
        if (local == null) {
            DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
            valorData = formatador.parse(data);
        } else {
            DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT, local);
            valorData = formatador.parse(data);
        }
        return valorData;
    }

    public static int obterNrDiasNoMes(Date dataPrm) {
    	if (dataPrm == null) {
    		dataPrm = new Date();
    	}
        Calendar dataCalendario = Calendar.getInstance();
        dataCalendario.setTime(dataPrm);
        int numeroDias = dataCalendario.getActualMaximum(Calendar.DAY_OF_MONTH);
        return numeroDias;
    }

    public static String getDataAno4Digitos(java.util.Date dataConverter) {
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String dataStr = formatador.format(dataConverter);
        return dataStr;
    }

    public static Date getDataSemHora(java.util.Date dataConverter) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dataConverter);
        cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static String getDataComHora(java.util.Date dataConverter) {
    	DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
    	String dataStr = formatador.format(dataConverter);
    	
    	DateFormat formatadorHora = DateFormat.getTimeInstance(DateFormat.SHORT);
    	dataStr += " " + formatadorHora.format(dataConverter);
    	
    	return dataStr;
    }
    
    public static String getDataFormatada(java.util.Date dataConverter) {
    	if (Uteis.isAtributoPreenchido(dataConverter)) {
	    	DateFormat formatador = new SimpleDateFormat("dd-MM-yyyy");
	    	String dataStr = formatador.format(dataConverter);
	    	return dataStr;
    	} else {
    		return "";
    	}
    }
    
    public static String getDataComHoraCompleta(java.util.Date dataConverter) {
    	
        DateFormat formatador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        String dataStr = formatador.format(dataConverter);
        return dataStr;
    }

    /**
     * Retorna a data passada como parametro utilizando o pattern (dd/MM/yyyy HH:mm:ss).
     * 
     * @param data
     * @return - A data formatada.
     */
    public static String getDataComHoraMinutoSegundo(Date data) {
    	DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	return formatador.format(data);
    }
    
    public static String getDataSomenteHora(java.util.Date dataConverter) {
        DateFormat formatadorHora = DateFormat.getTimeInstance(DateFormat.SHORT);
        return formatadorHora.format(dataConverter);
    }

    public static String obterDataComHoraMinutoParaNomeArquivo(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            StringBuilder dataStr = new StringBuilder("");
            DateFormat formatadorHora = DateFormat.getTimeInstance(DateFormat.SHORT);
            dataStr.append(sdf.format(data)).append(formatadorHora.format(data).trim().replaceAll(":", ""));
            return dataStr.toString();
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String obterDataComHoraMinutoSegundoParaNomeArquivo(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
            StringBuilder dataStr = new StringBuilder("");
            dataStr.append(sdf.format(data));
            return dataStr.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAnoDataAtual() {
        Locale aLocale = new Locale("pt", "BR");
        Date hoje;
        String hojeStr;
        DateFormat formatador;
        formatador = DateFormat.getDateInstance(DateFormat.SHORT, aLocale);
        hoje = new Date();
        hojeStr = formatador.format(hoje);
        return (hojeStr.substring(hojeStr.lastIndexOf("/") + 1));
    }

    public static String getDataAtual() {
        Date hoje = new Date();
        return (UteisData.getData(hoje));
    }
    
    public static Date getDataAtualSomandoOuSubtraindoMinutos(Integer minutos) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, minutos);
        return cal.getTime();
    }

    public static String getDataComHoraAtual() {
        return (getDataComHora(new Date()));
    }

    public static String getHoraAtual() {
        Locale aLocale = new Locale("pt", "BR");
        Date hoje;
        DateFormat formatador;
        formatador = DateFormat.getTimeInstance(DateFormat.SHORT, aLocale);
        hoje = new Date();

        String horaStr;
        horaStr = formatador.format(hoje);
        return (horaStr);
    }

    public static String getDataDiaMesAnoConcatenado() {
        String dataAtual = getDataAtual();
        String ano = "";
        String mes = "";
        String dia = "";
        int cont = 1;
        while (cont != 3) {
            int posicao = dataAtual.lastIndexOf("/");
            if (posicao != -1) {
                cont++;
                if (cont == 2) {
                    ano = dataAtual.substring(posicao + 1);
                    dataAtual = dataAtual.substring(0, posicao);
                } else if (cont == 3) {
                    mes = dataAtual.substring(posicao + 1);
                    dia = dataAtual.substring(0, posicao);
                }
            }
        }
        return dia + mes + ano;
    }

    /**
     * Contabilizado a partir da data inicial
     * 
     * Ex.: dataInicial=2018-11-19 && dataFinal=2018-11-30
     * 			return -11
     *   
     * @param dataInicial
     * @param dataFinal
     * @return
     */
    public static long nrDiasEntreDatas(Date dataInicial, Date dataFinal) {
        long dias = (dataInicial.getTime() - dataFinal.getTime()) / (1000 * 60 * 60 * 24);
        return dias;
    }

    public static Date obterDataFutura(Date dataInicial, long nrDiasProgredir) {
        long dataEmDias = dataInicial.getTime() / (1000 * 60 * 60 * 24);
        dataEmDias = dataEmDias + nrDiasProgredir;
        Date dataFutura = new Date(dataEmDias * (1000 * 60 * 60 * 24));
        return dataFutura;
    }

    public static Date obterDataFuturaConsiderandoDiaAtual(Date dataInicial, long nrDiasProgredir) {
        if (nrDiasProgredir > 1) {
            long dataEmDias = dataInicial.getTime() / (1000 * 60 * 60 * 24);
            dataEmDias = dataEmDias + nrDiasProgredir;
            Date dataFutura = new Date(dataEmDias * (1000 * 60 * 60 * 24));
            return dataFutura;
        }
        return dataInicial;
    }

    public static Date obterDataFuturaUsandoCalendar(Date dataInicial, int nrDiasProgredir) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dataInicial);
        gc.add(GregorianCalendar.DAY_OF_MONTH, nrDiasProgredir);
        return gc.getTime();
    }
    
    public static Date obterDataFuturaAdicionandoMes(Date dataInicial, int nrMesProgredir) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataInicial);

        calendar.add(Calendar.MONTH, nrMesProgredir);
        calendar.setTime(calendar.getTime());
        
        calendar.getActualMaximum(Calendar.MONTH);
        
        return calendar.getTime();
    }
    
    public static Date obterDataFuturaUsandoCalendarProgredirPorSegundos(Date dataInicial, int segundosProgredir) {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(dataInicial);
    	gc.add(GregorianCalendar.SECOND, segundosProgredir);
    	return gc.getTime();
    }

    public static String getDataAplicandoFormatacao(Date data, String mascara) {
        if (data == null) {
            return "";
        }
        Locale aLocale = new Locale("pt", "BR");
        SimpleDateFormat formatador = new SimpleDateFormat(mascara, aLocale);
        String dataStr = formatador.format(data);
        return dataStr;
    }

    /* Defini-se a mascarA! a ser aplicada a data atual
     * de acordo com o padrALo - dd/mm/yyyy ou MM.yy.dd e assim por diante
     */
    public static String getDataAtualAplicandoFormatacao(String mascara) {
        Date hoje = new Date();
        return getDataAplicandoFormatacao(hoje, mascara);
    }

    public static String getDataMesAnoConcatenado() {
        // return MM/AAAA
        int mesAtual = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        String mesAtualStr = String.valueOf(mesAtual);
        if (mesAtualStr.length() == 1) {
            mesAtualStr = "0" + mesAtualStr;
        }
        return mesAtualStr + "/" + anoAtual;
        /*        String dataAtual = Uteis.getDataAtual();
        String ano = "";
        String mes = "";
        int cont = 1;
        while (cont != 3) {
        int posicao = dataAtual.lastIndexOf("/");
        if (posicao != -1) {
        cont++;
        if (cont == 2) {
        ano = dataAtual.substring(posicao + 1);
        dataAtual = dataAtual.substring(0, posicao);
        } else if (cont == 3) {
        mes = dataAtual.substring(posicao + 1);
        }
        }
        }
        return (mes + "/" + ano);*/
    }
    
    public static String obterDataFormatoTexto_MM(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM");
            return sdf.format(data);
        } catch (Exception e) {
            return "";
        }
    }

    public static String obterDataFormatoTexto_dd_MM(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            return sdf.format(data);
        } catch (Exception e) {
            return "";
        }
    }

    public static String obterDataFormatoTexto_MM_yyyy(Date data) {
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
    		return sdf.format(data);
    	} catch (Exception e) {
    		return "";
    	}
    }

    public static String obterDataFormatoTexto_dd_MM_yyyy(Date data) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(data);
        } catch (Exception e) {
            return "";
        }
    }

    public static String getHoraHHMMSS(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minuto = String.valueOf(cal.get(Calendar.MINUTE));
        String segundo = String.valueOf(cal.get(Calendar.SECOND));

        return hora + minuto + segundo;
    }
    
    public static String getHoraHHMM(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String minuto = String.valueOf(cal.get(Calendar.MINUTE));
        
        if (hora.length() == 1) {
            hora = "0" + hora;
        }
        if (minuto.length() == 1) {
        	minuto = "0" + minuto;
        }
        
        return hora + ":" + minuto;
    }

    public static String getHoraDate(Date dataPrm) {
        Locale aLocale = new Locale("pt", "BR");
        DateFormat formatador;
        formatador = DateFormat.getTimeInstance(DateFormat.SHORT, aLocale);
        String horaStr;
        horaStr = formatador.format(dataPrm);
        return (horaStr);
    }

    public static Date getDateTime(Date data, int hora, int minuto, int segundo) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, segundo);

        return cal.getTime();
    }

    public static java.sql.Timestamp getDataJDBCTimestamp(java.util.Date dataConverter) {
        if (dataConverter == null) {
            return null;
        }
        java.sql.Timestamp dataSQL = new java.sql.Timestamp(dataConverter.getTime());
        return dataSQL;
    }

    public static java.sql.Date getDataJDBC(java.util.Date dataConverter) {
        if (dataConverter == null) {
            return null;
            //dataConverter = new Date();
        }
        java.sql.Date dataSQL = new java.sql.Date(dataConverter.getTime());
        return dataSQL;
    }

    public static java.util.Date getPrimeiroDataMes(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    
    public static java.util.Date getPrimeiroDataMes(Integer mes, Integer ano) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	cal.set(Calendar.MONTH, (mes-1));
    	cal.set(Calendar.YEAR, ano);
    	return cal.getTime();
    }
    
    public static java.util.Date getPrimeiroDiaProximaSemana(Date dataBase) {    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataBase);
        int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);        
        return obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataBase), 8 - diaDaSemana);        	        
    }
    
    public static java.util.Date getPrimeiroDiaSemanaPassada(Date dataBase) {    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dataBase);
    	int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);    	
    	return obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataBase),(6+diaDaSemana)*-1);     	            
    }
    
    public static java.util.Date getPrimeiroDiaSemana(Date dataBase) {    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataBase);
        int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if(diaDaSemana > 1){
        	return obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataBase), (diaDaSemana-1)*-1);
        	//cal.set(Calendar.DAY_OF_MONTH, diaDaSemana*-1);
        }        
        return cal.getTime();
    }
    
    public static java.util.Date getUltimoDiaSemana(Date dataBase) {    	
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataBase);
        int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);
        if(diaDaSemana < 7){
        	return obterDataFutura(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataBase), 7 - diaDaSemana);        	
        }        
        return cal.getTime();
    }

    public static java.util.Date getUltimaDataMes(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
    public static java.util.Date getUltimaDataMes(Integer mes, Integer ano) {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.MONTH, (mes-1));
    	cal.set(Calendar.YEAR, ano);
    	cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    	return cal.getTime();
    }

//    public static Integer getCalculaDiasUteis(Date dataInicial, Date dataFinal) throws Exception {
//        Calendar calInicial = Calendar.getInstance();
//        Calendar calFinal = Calendar.getInstance();
//        calInicial.setTime(dataInicial);
//        calFinal.setTime(dataFinal);
//        calInicial.set(Calendar.HOUR, 0);
//        calInicial.set(Calendar.MINUTE, 0);
//        calInicial.set(Calendar.SECOND, 0);
//        calInicial.set(Calendar.MILLISECOND, 0);
//        calFinal.set(Calendar.HOUR, 23);
//        calFinal.set(Calendar.MINUTE, 59);
//        calFinal.set(Calendar.SECOND, 59);
//        calFinal.set(Calendar.MILLISECOND, 0);
//        Integer diasUteis = 0;
//        Integer dias = 0;
//        for (int i = 1; calInicial.compareTo(calFinal) <= 0; calInicial.add(Calendar.DATE, i)) {
//            if (calInicial.get(Calendar.DAY_OF_WEEK) != 1 && calInicial.get(Calendar.DAY_OF_WEEK) != 7) {  // sabado ou domingo
//                diasUteis++;
//            }
//            dias++;
//        }
//
//        return diasUteis;
//    }
//
//    public static Integer getCalculaDiasNaoUteis(Date dataInicial, Date dataFinal) throws Exception {
//        Calendar calInicial = Calendar.getInstance();
//        Calendar calFinal = Calendar.getInstance();
//        calInicial.setTime(dataInicial);
//        calFinal.setTime(dataFinal);
//        calInicial.set(Calendar.HOUR, 0);
//        calInicial.set(Calendar.MINUTE, 0);
//        calInicial.set(Calendar.SECOND, 0);
//        calInicial.set(Calendar.MILLISECOND, 0);
//        calFinal.set(Calendar.HOUR, 23);
//        calFinal.set(Calendar.MINUTE, 59);
//        calFinal.set(Calendar.SECOND, 59);
//        calFinal.set(Calendar.MILLISECOND, 0);
//        Integer diasNaoUteis = 0;
//        for (int i = 1; calInicial.compareTo(calFinal) <= 0; calInicial.add(Calendar.DATE, i)) {
//            if (calInicial.get(Calendar.DAY_OF_WEEK) == 1 || calInicial.get(Calendar.DAY_OF_WEEK) == 7) {  // sabado ou domingo
//                diasNaoUteis++;
//            }
//        }
//        return diasNaoUteis;
//    }
//    
//    /**
//     * Método responsável por calcular o número de dias NAO ÚTEIS. Estes dias podem ser SÁBADO ou DOMINGO, mas nao podem ser feriado.
//     * @param dataInicial
//     * @param dataFinal
//     * @param feriados
//     * @return
//     * @throws Exception
//     */
//    public static Integer getNumeroDiasFinalSemanaDesconsiderandoFeriados(Date dataInicial, Date dataFinal, List<FeriadoVO> feriados) throws Exception {
//        Calendar calInicial = Calendar.getInstance();
//        Calendar calFinal = Calendar.getInstance();
//        calInicial.setTime(dataInicial);
//        calFinal.setTime(dataFinal);
//        calInicial.set(Calendar.HOUR, 0);
//        calInicial.set(Calendar.MINUTE, 0);
//        calInicial.set(Calendar.SECOND, 0);
//        calInicial.set(Calendar.MILLISECOND, 0);
//        calFinal.set(Calendar.HOUR, 0);
//        calFinal.set(Calendar.MINUTE, 0);
//        calFinal.set(Calendar.SECOND, 0);
//        calFinal.set(Calendar.MILLISECOND, 0);
//        Integer diasNaoUteis = 0;
//        List<String> mapFeriados = new ArrayList<String>();
//        for (FeriadoVO feriado : feriados) {
//        	if (feriado.getRecorrente()) {
//        		mapFeriados.add(UteisData.getDiaMesData(feriado.getData()) + " " + UteisData.getMesData(feriado.getData()));
//        	} else {
//        		mapFeriados.add(UteisData.getDiaMesData(feriado.getData()) + " " + UteisData.getMesData(feriado.getData()) + " " + UteisData.getAnoData(feriado.getData()));
//        	}
//        }
//        for (int i = 1; calInicial.compareTo(calFinal) <= 0; calInicial.add(Calendar.DATE, i)) {
//            if (calInicial.get(Calendar.DAY_OF_WEEK) == 1 || calInicial.get(Calendar.DAY_OF_WEEK) == 7) {  // sabado ou domingo
//            	diasNaoUteis++;
//            } else if (mapFeriados.contains(UteisData.getDiaMesData(calInicial.getTime()) + " " + UteisData.getMesData(calInicial.getTime())) || 
//        			mapFeriados.contains(UteisData.getDiaMesData(calInicial.getTime()) + " " + UteisData.getMesData(calInicial.getTime()) + " " + UteisData.getAnoData(calInicial.getTime()))) {
//        		diasNaoUteis++;
//            }
//        }
//        return diasNaoUteis;
//    }
    
    /**
     * Método responsável por calcular número de dias ÚTEIS. Estes dias nao podem ser SÁBADO ou DOMINGO nem FERIADO.
     * @param dataInicial
     * @param dataFinal
     * @param feriados
     * @return
     * @throws Exception
     */
    public static Integer getNumeroDiasUteisConsiderandoFeriados(Date dataInicial, Date dataFinal, List<FeriadoVO> feriados) throws Exception {
        Calendar calInicial = Calendar.getInstance();
        Calendar calFinal = Calendar.getInstance();
        calInicial.setTime(dataInicial);
        calFinal.setTime(dataFinal);
        calInicial.set(Calendar.HOUR, 0);
        calInicial.set(Calendar.MINUTE, 0);
        calInicial.set(Calendar.SECOND, 0);
        calInicial.set(Calendar.MILLISECOND, 0);
        calFinal.set(Calendar.HOUR, 0);
        calFinal.set(Calendar.MINUTE, 0);
        calFinal.set(Calendar.SECOND, 0);
        calFinal.set(Calendar.MILLISECOND, 0);
        Integer diasUteis = 0;
        List<String> mapFeriados = new ArrayList<String>();
        for (FeriadoVO feriado : feriados) {
        	if (feriado.getRecorrente()) {
        		mapFeriados.add(UteisData.getDiaMesData(feriado.getData()) + " " + UteisData.getMesData(feriado.getData()));
        	} else {
        		mapFeriados.add(UteisData.getDiaMesData(feriado.getData()) + " " + UteisData.getMesData(feriado.getData()) + " " + UteisData.getAnoData(feriado.getData()));
        	}
        }
        for (int i = 1; calInicial.compareTo(calFinal) <= 0; calInicial.add(Calendar.DATE, i)) {
            if (calInicial.get(Calendar.DAY_OF_WEEK) != 1 && calInicial.get(Calendar.DAY_OF_WEEK) != 7) {  // sabado ou domingo
            	if (!mapFeriados.contains(UteisData.getDiaMesData(calInicial.getTime()) + " " + UteisData.getMesData(calInicial.getTime())) && 
            			!mapFeriados.contains(UteisData.getDiaMesData(calInicial.getTime()) + " " + UteisData.getMesData(calInicial.getTime()) + " " + UteisData.getAnoData(calInicial.getTime()))) {
            		diasUteis++;
                }
            }
        }
        return diasUteis;
    }
    
    public static double diferencaEmMinutosEntreDatas(Date dataInicial, Date dataFinal) {
        double result = 0;
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        double diferencaEmMinutos = (diferenca / 1000) / 60; //resultado A© diferenA§a entre as datas em minutos
        long segundosRestantes = (diferenca / 1000) % 60; //calcula os segundos restantes
        result = diferencaEmMinutos + (segundosRestantes / 60d); //transforma os segundos restantes em minutos
        return result;
    }
    
    /** 
     * Calcula a diferença de duas datas em horas 
     * <br> 
     * <b>Importante:</b> Quando realiza a diferença em horas entre duas datas, este método considera os minutos restantes e os converte em fraçao de horas. 
     * @param dataInicial 
     * @param dataFinal 
     * @return quantidade de horas existentes entre a dataInicial e dataFinal. 
     */  
    public static double diferencaEmHoras(Date dataInicial, Date dataFinal) {  
        double result = 0;  
        long diferenca = dataFinal.getTime() - dataInicial.getTime();  
        long diferencaEmHoras = (diferenca /1000) / 60 / 60;  
        long minutosRestantes = (diferenca / 1000)/60 %60;  
        double horasRestantes = minutosRestantes / 60d;  
        result = diferencaEmHoras + (horasRestantes);  
          
        return result;  
    }  
    
    public static long getCalculaDiferencaEmDias(Date inicio, Date fim) {
		long diffInMillies = Math.abs(fim.getTime() - inicio.getTime());
		return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

	}
    
	public static int calcularQuantidadeMesesEntreDatas(Date dataInicial, Date dataFinal) {
		Calendar inicial = Calendar.getInstance();
		inicial.setTime(dataInicial);

		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.setTime(dataFinal);

		int count = 0;

		while ( inicial.get(Calendar.MONTH) != calendarFinal.get(Calendar.MONTH) || 
				inicial.get(Calendar.YEAR) != calendarFinal.get(Calendar.YEAR)) {
			inicial.add(Calendar.MONTH, 1);
			count++;
		}

		return count;
	}

	/**
	 * Metodo que calcula o valor a partir da data informado somando a quantidade de meses informado.
	 * 
	 * @param data
	 * @param quantidadeMesesAdicionar
	 * @return
	 */
	public static Date calcularAnoPelaQuantidadeMeses(Date data, int quantidadeMesesAdicionar) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.MONTH, quantidadeMesesAdicionar);

		return calendar.getTime();
	}
    
    public static String getObterTempoEntreDuasData(Date dataFinal, Date dataInicial) {
		long sec = (dataFinal.getTime() - dataInicial.getTime());
		// variáveis auxiliares
		long second = 1000;
		long minute = 60 * second;
		long hour = 60 * minute;
		//long day = 24 * hour;
//		// resultado em horas decimais
//		double rdh = Math.ceil(sec / hour);
//		// resultado em minutos decimais
//		double rdm = Math.ceil(sec / minute);
//		// resultado em segundos
//		double rds = Math.ceil(sec / second);
		/*Double days = Math.floor(sec / day);
		sec -= days * day;*/
		Double hours = Math.floor(sec / hour);
		sec -= hours * hour;
		Double minutes = Math.floor(sec / minute);
		// Caso queira saber o segunto
		sec -= minutes * minute;
		Double seconds = Math.floor(sec / second);
		
		String resultado = "";
		//Integer dia = days.intValue();
		Integer hora = hours.intValue();
		Integer minuto = minutes.intValue();
		Integer segundo = seconds.intValue();
		
		/*if(dia < 0){
			dia = days.intValue() * (-1);
		}*/
		if(hora < 0){
			hora = hours.intValue() * (-1);
		}
		if(minuto < 0){
			minuto = minutes.intValue() * (-1);
		}
		if(segundo < 0){
			segundo = seconds.intValue() * (-1);
		}
		
		/*if(dia > 0){
			if(dia == 1){
				resultado = "0" + dia + " Dia e ";
			}else if(dia < 10){
				resultado = "0" + dia + " Dias e ";
			}else{
				resultado = dia + " Dias e ";
			}
		}*/
		
		if(hora < 10){
			resultado = resultado + "0" + hora + ":";
		}else{
			resultado = resultado + hora + ":";
		}if(minuto < 10){
			resultado = resultado + "0" + minuto + ":";
		}else{
			resultado = resultado + minuto + ":";
		}if(segundo < 10){
			resultado = resultado + "0" + segundo;
		}else{
			resultado = resultado + segundo;
		}

		return resultado;

	}
    
//  public static void validarSeHojeEFeriado(Calendar dataCalendar) throws Exception {
//            Date dataFinal = dataCalendar.getTime();
//            while (Feriado.verificarFeriadoNesteDia(dataFinal, null)) {
//                dataCalendar.add(Calendar.DAY_OF_MONTH, 1);
//                dataFinal = dataCalendar.getTime();
//            }
//        }
    
    public static Date adicionarHoraEmData(Date data, String horario) {
    	try {
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(data);
	    	int separador = horario.indexOf(":");
	    	
	    	calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horario.substring(0, separador)));
	    	calendar.set(Calendar.MINUTE, Integer.parseInt(horario.substring(separador + 1, horario.length())));
	    	
	    	return calendar.getTime();
    	} catch (Exception e) {
    		return data;
    	}
    }
    
    public static String obterDataFormatoEnvioCartaCorrecaoNFe(Date data, boolean horarioVerao) {
        try {
            SimpleDateFormat parte1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat parte2 = new SimpleDateFormat("HH:mm:ss");
            String tzd = "-03:00";
            if (horarioVerao) {
            	tzd = "-02:00";
            }
            return parte1.format(data) + "T" + parte2.format(data) + tzd;
        } catch (Exception e) {
            return "";
        }
    }
    
    public static boolean validarSeDataInicioMenorQueDataFinalDesconsiderandoAno(Date dataInicio, Date dataFinal) throws Exception {
    	Calendar calInicio = Calendar.getInstance();
    	Calendar calFim = Calendar.getInstance();
    	calInicio.setTime(dataInicio);
    	calFim.setTime(dataFinal);
    	calInicio.set(Calendar.YEAR, 1970);
    	calFim.set(Calendar.YEAR, 1970);
    	return calInicio.before(calFim);
    }

    public static boolean validarSeDataInicioMenorOuIgualQueDataFinalDesconsiderandoDias(Date dataInicio, Date dataFinal) throws Exception {
    	int anoDataInicio = UteisData.getAnoData(dataInicio);
		int mesDataInicio = UteisData.getMesData(dataInicio);

		int anoDataFinal = UteisData.getAnoData(dataFinal);
		int mesDataFinal = UteisData.getMesData(dataFinal);

		//Valida que o inicio das ferias do funcionario esta no mes da competencia do contra cheque
		return anoDataInicio == anoDataFinal && mesDataInicio == mesDataFinal;
    }
    
    public static boolean validarSeDataInicioMenorQueDataFinalDesconsiderandoDias(Date dataInicio, Date dataFinal) throws Exception {
    	int anoDataInicio = UteisData.getAnoData(dataInicio);
		int mesDataInicio = UteisData.getMesData(dataInicio);

		int anoDataFinal = UteisData.getAnoData(dataFinal);
		int mesDataFinal = UteisData.getMesData(dataFinal);

		return (anoDataInicio <= anoDataFinal  && mesDataInicio <= mesDataFinal) || anoDataFinal > anoDataInicio;
    }

    
    public static Date getData(String data, String pattern) throws ParseException {
        try {
            if (data != null) {
              DateFormat formatador = new SimpleDateFormat(pattern);
                return formatador.parse(data);
            }
        } catch (Exception e) {
           // //System.out.println("Uteis Erro:" + e.getMessage());
        }
        return null;
    }
    
    public static Date getDataSemMilisegundos(Date data) {
        try {
            if (data != null) {
              DateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return formatador.parse(data.toString());
            }
        } catch (Exception e) {
           // //System.out.println("Uteis Erro:" + e.getMessage());
        }
        return null;
    }
    
    
    public static Date getDataComMinutos(Date data) {
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(data);
    	return cal.getTime();
    }
    
    /**
     * Calcula a quantidade de dias somando ou subtraindo os dias pela data informada.
     * 
     * @param data
     * @param dias
     * @return
     */
    public static Date getAdicionarDataAtualSomandoOuSubtraindo(Date data, Integer dias) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(data);
    	cal.add(Calendar.DATE, dias);
    	return cal.getTime();
    }
    
    public static Date getAdicionarDataAtualSomandoOuSubtraindoMinutos(Date data, Integer minutos) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.add(Calendar.MINUTE, minutos);
        return cal.getTime();
    }
    
    public static String getNumeroDoisDigitos(Integer numero) {
    	String retorno = numero.toString();
    	if (numero.intValue() < 10) {
    		retorno = "0" + numero;
    	}
    	return retorno;
    }
    
    public static Date getData(String data) {
		try {
			if (data != null) {
				DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
				return (java.util.Date) formatador.parse(data);
			}
		} catch (Exception e) {
		}
		return null;
	}
    
    public static Boolean validarDataInicialMaiorFinal(Date data1, Date data2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String a = format.format(data1);
        String b = format.format(data2);
        data1 = format.parse(a);
        data2 = format.parse(b);
        if (data1.compareTo(data2) > 0) {
        	return Boolean.TRUE;
        }
        return Boolean.FALSE;
       
    }

    public static Boolean validarDataInicialMaiorFinalComHora(Date data1, Date data2) throws ParseException {
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	String a = format.format(data1);
    	String b = format.format(data2);
    	data1 = format.parse(a);
    	data2 = format.parse(b);
    	if (data1.compareTo(data2) >= 0) {
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    	
    }
    
    public static String getPeriodoPorExtenso(Date dataInicio, Date dataTermino){
    	return  getData(dataInicio) + " até " + getData(dataTermino);
    	
    }
    
	public static boolean isFinalDeSemanaComSextaFeira(final Date data, boolean considerarSextaFeira) {
		boolean retorno = false;
		Calendar cal = new GregorianCalendar();
		try {
			cal.setTime(data);
			int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);

			if (considerarSextaFeira) {
				switch (diaDaSemana) {
				case Calendar.FRIDAY:
				case Calendar.SUNDAY:
				case Calendar.SATURDAY:
					retorno = true;
				}
			} else {
				switch (diaDaSemana) {
				case Calendar.SUNDAY:
				case Calendar.SATURDAY:
					retorno = true;
				}
			}
			return retorno;
		} finally {
			cal = null;
		}
	}
	
	public static int obterQuantidadeMesesPeriodo(Date startDate, Date endDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		return diffMonth + 1;
	}
	
	public static int obterQuantidadeAnosPeriodo(Date startDate, Date endDate) {
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		return diffYear + 1;
	}
	
    public static Date adicionarDiasEmData(Date data, Integer quantidadeDias) {
    	try {
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(data);
	    	calendar.add(Calendar.DATE, quantidadeDias);
	    	return calendar.getTime();
    	} catch (Exception e) {
    		return data;
    	}
    }
    
    /**
     * Metodo que calcula é retorna a idade pela data de nascimento informada
     *  
     * @param dataNascimento
     * @return
     */
	public static long calcularIdade(Date dataNascimento) {
		return (new Date().getTime() - dataNascimento.getTime()) / (31536000000L);
    }
	
	/**
	 * Recupera a data do mes anterior ao atual pegando o primeiro dia do mes
	 * 
	 * @return Date
	 */
	public static Date getDataMesAnterior() {
		Calendar calendar = Calendar.getInstance();

		calendar.add(java.util.Calendar.MONTH, -1);
		calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMinimum(java.util.Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static boolean ValidarMesEAnoDatasIguais(Date primeiraData, Date segundaData) {
		Calendar calendarPrimeiraData = Calendar.getInstance();
		calendarPrimeiraData.setTime(primeiraData);
		
		Calendar calendarSegundaData = Calendar.getInstance();
		calendarSegundaData.setTime(segundaData);
		
		return (calendarPrimeiraData.get(Calendar.MONTH) == calendarSegundaData.get(Calendar.MONTH)) && 
				(calendarPrimeiraData.get(Calendar.YEAR) == calendarSegundaData.get(Calendar.YEAR)) ;
	}
	
	
	/**
	 * Retorna a data formatada.
	 * Ex.: 01/09/2018 retorna 2018-09
	 * @param dataConverter
	 * @return
	 */
	public static String getDataFormatadaPorAnoMes(java.util.Date dataConverter) {
    	DateFormat formatador = new SimpleDateFormat("yyyy-MM");
    	String dataStr = formatador.format(dataConverter);
    	return dataStr;
    }
	
    public static String getAnoAnteriorDataString(Date dataPrm) {
        Calendar dataCalendar = Calendar.getInstance();
        dataCalendar.setTime(dataPrm);

        Integer ano = dataCalendar.get(Calendar.YEAR) - 1;
        return ano.toString();
    }
    
    public static String getDataPorExtenso(Date data){
        DateFormat dfmt = new SimpleDateFormat("d 'de' MMMM 'de' yyyy");
        return dfmt.format(data);
   }
   public static String diaSemana() {
    	LocalDate localDate = LocalDate.now();
    	DayOfWeek dayOfWeek = localDate.getDayOfWeek();
    	return dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pt","BR")).toUpperCase();
    }
    
    public static String obterDataAntiga(Date dataBase, Integer nrDiasRegredir) {
		Calendar calendar = new GregorianCalendar();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			nrDiasRegredir = (-1) * nrDiasRegredir;
			calendar.add(Calendar.DAY_OF_MONTH, nrDiasRegredir);
			return format.format(calendar.getTime());
		} finally {
			calendar = null;
		}
	}
    
    /**
     * Valida se a primeira da e maior que a segunda contando Dia, Mês é Ano.
     * 
     * @param data1
     * @param data2
     * @return
     */
    public static Boolean getPrimeiraDataMaior(Date data1, Date data2)  {
    	int mes1 = getMesData(data1);
    	int ano1 = getAnoData(data1);
    	int dia1 = getDiaMesData(data1);
    	
    	int mes2 = getMesData(data2);
    	int ano2 = getAnoData(data2);
    	int dia2 = getDiaMesData(data2);
    	
    	if (ano1 > ano2) return true;
    	if ( (ano1== ano2) && mes1 > mes2) return true;
    	if ( ( (ano1== ano2) && mes1 == mes2) && dia1 > dia2) return true;
    	return false;
    }
    
    /**
	 * Retorna o ultimo dia do mês de acordo com a data especificada.
	 * 
	 * @param date
	 * @return
	 */
	public static int getUltimoDiaMes(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
    
    public static Map<String ,Date> getDataInicialFinalPeriodoSemestral(int ano ,int semestre ) {
    	Map<String, Date> resultado  = new HashMap<String, Date>();
    	Calendar  dataInicial =  Calendar.getInstance();
    	Calendar  dataFinal =  Calendar.getInstance();
    	
    	if(semestre ==1) {
    		dataInicial.set(Calendar.DAY_OF_MONTH, 1);
    		dataInicial.set(Calendar.MONTH, 0);
    		dataInicial.set(Calendar.YEAR, ano);
    		
    		dataFinal.set(Calendar.DAY_OF_MONTH, 20);
    		dataFinal.set(Calendar.MONTH, 6);
    		dataFinal.set(Calendar.YEAR, ano);
    		
    		resultado.put("dataInicial",dataInicial.getTime());
    		resultado.put("dataFinal", dataFinal.getTime());
    	}else if(semestre == 2) {
    		
    		dataInicial.set(Calendar.DAY_OF_MONTH, 20);
    		dataInicial.set(Calendar.MONTH, 6);
    		dataInicial.set(Calendar.YEAR, ano);
    		
    		dataFinal.set(Calendar.DAY_OF_MONTH, 31);
    		dataFinal.set(Calendar.MONTH, 11);
    		dataFinal.set(Calendar.YEAR, ano);
    		
    		resultado.put("dataInicial",dataInicial.getTime());
    		resultado.put("dataFinal", dataFinal.getTime());
    	}
    	return resultado;
    }
    
    public static Date ultimoDiaAnoAtual() {
    	Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        return cal.getTime();
    }

    public static Date primeiroDiaAnoAtual() {
    	Calendar cal = Calendar.getInstance();

    	cal.setTime(new Date());
    	cal.set(Calendar.MONTH, 0);
    	cal.set(Calendar.DAY_OF_MONTH, 1);
    	return cal.getTime();
    }
        
    
    /**
     * Esse metodo recupera o primeiro ou ultimo dia da semana do campo data informado.
     * Se o o parametro informado isPrimeiro for TRUE o metodo retornará o primeiro dia da semana caso for 
     * FALSE ele retorna o ultimo dia da semana.
     * 
     * @param data
     * @param isPrimeiro
     * @return
     */
    public static String getDataIinicioOufimDaSemana(Date data, boolean isPrimeiro) {
        GregorianCalendar calendar = new GregorianCalendar();
        //Define que a semana começa no domingo.
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        //Define a data atual.
        calendar.setTime(data);

        if (isPrimeiro) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        } else {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        }

        return getDataFormatada(calendar.getTime());
    }
   public static Date quintoDiaUtilProximoMes(Date proximoMes, List<FeriadoVO> feriados) throws Exception {
    	if (getMesData(proximoMes) == 12) {
    		proximoMes = getPrimeiroDataMes(1, getAnoData(new Date()) + 1);
    	} else {
    		proximoMes = getPrimeiroDataMes(getMesData(proximoMes) + 1, getAnoData(new Date()));    		
    	}

    	int quantidadeDiasUteis = 0;
    	int quintoDiaUtil = 5;

    	while (quantidadeDiasUteis < quintoDiaUtil) {
	    	if (isDiaUtil(proximoMes, false, false, feriados)) {
	    		quantidadeDiasUteis++;
	    	}
	    	proximoMes = setDay(proximoMes, getDiaMesData(proximoMes)+ 1);
    	}

    	return proximoMes;
    }
    
    public static Date setDay(Date date, int day) {
        if (date == null) {
          return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
      }

      public static void setMonth(Date date, int month) {
        if (date == null)
          return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, month - 1);
      }

      public static void setYear(Date date, int year) {
        if (date == null)
          return;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
      }
      
      public static long diferencaEmSegundosEntreDatas(Date dataInical, Date dataFinal) {
    	  long segundosRestantes = (dataFinal.getTime() - dataInical.getTime()) /1000;
          return segundosRestantes;
      }
      
      public static String getDataCompletaFormatoUTC(Date date) {
    	  if(date == null) {
    		  return Constantes.EMPTY;
    	  }
    	  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    	  sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    	  String time = sdf.format(date);
    	  return time;
      }
      
      public static Date getConverterStringParaDataToString(String data) throws ParseException {
    	  if (data == null || data.trim().isEmpty()) {
    		  return null;
    	  }
    	    String date = data.trim();
    	    try {
    	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    	        return simpleDateFormat.parse(date);
    	    } catch (Exception e1) {
    	        try {
    	            SimpleDateFormat SimpleDateFormat2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    	            return SimpleDateFormat2.parse(date);
    	        } catch (Exception e2) {
    	            return null;
    	        }
    	}
      }
}