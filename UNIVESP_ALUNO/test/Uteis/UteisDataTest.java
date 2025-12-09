package Uteis;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class UteisDataTest extends TestManager implements Serializable {

	private static final long serialVersionUID = 1L;

	@Test
	public void obterDataMeioAno() {
//		Date data = UteisData.getData30JunhoAnoAtual();
//
//		assertTrue(data.getDay() == 30 );
	}
	
	/*@Test
	public void getMesDataAtual() {
		Date retorno;
		try {
//			getFacadeFactory().setFeriadoFacade(new Feriado());
//			retorno = getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(Uteis.getData("2018-11-01", "yyyy-MM-dd"), 3, 0, false, false, ConsiderarFeriadoEnum.FINANCEIRO);
//			System.out.println(retorno);			
			int diaUtil = 1;
			int diaUtilLimite = 20;
			while(diaUtil<=diaUtilLimite) {
				int qtdeMesesSimular = 36;
				int dia = 28;
				int mes = 1;
				int ano = 2018;
				int i = 1;
				System.out.println("============= TESTE COM "+diaUtil+" DIA UTIL ======================");
				Date dataBase = Uteis.gerarDataDiaMesAno(dia, mes, ano);
				while(!UteisData.getValidaDiaUtil(dataBase)) {
					dataBase = Uteis.obterDataAvancada(dataBase, 1);
				}
				while( i<=qtdeMesesSimular) {
					retorno = Uteis.getDataAvancandoNumeroEspecificoDiasUteisInicioMes(dataBase, diaUtil, true);			
					System.out.println("DATA BASE: "+Uteis.getData(dataBase)+" = DATA RETORNO: "+Uteis.getData(retorno));
					assertTrue(UteisData.getValidaDiaUtil(retorno));	
					assertTrue(Uteis.getCalculaDiasUteis(Uteis.gerarDataInicioMes(mes, ano), retorno).equals(diaUtil));
					i++;
					mes++;
					if(mes > 12) {
						mes = 1;
						ano++;
					}
					dataBase = Uteis.gerarDataDiaMesAno(dia, mes, ano);
					while(!UteisData.getValidaDiaUtil(dataBase)) {
						dataBase = Uteis.obterDataAvancada(dataBase, 1);
					}
				}
				diaUtil++;
				if(diaUtil>diaUtilLimite && diaUtilLimite < 20) {
					diaUtil = 1;
					diaUtilLimite++;
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	//@Test
	public void getUltimoDiaMes() {
		Date data = new Date();
		int dia= UteisData.getUltimoDiaMes(data);
		System.out.println(dia);
	}

	//@Test
	public void dataPorExtensao() {
		Date data = new Date();
		String dataFormatada = UteisData.getDataPorExtenso(data);
		System.out.println("Data Formatada:"+dataFormatada);
	}
	
	//@Test
	public void getDataAplicandoFormatacao() {
		System.out.println(UteisData.getDataAplicandoFormatacao(new Date(), "dd/MM/yyyy"));
	}
	
	//@Test
	public void teste() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println(UteisData.getDataAplicandoFormatacao( UteisData.calcularAnoPelaQuantidadeMeses(new Date(), 36) , "dd/MM/yyyy") );
	}
	
	//@Test
	public void teste1() {
		System.out.println( new SimpleDateFormat("MM").format(new Date()));

		System.out.println(  new SimpleDateFormat("YYYY").format(new Date()));
		
		System.out.println(  new SimpleDateFormat("dd-MM-YYYY").format(new Date()).toString().substring(3, 10));
		
	}
	
	@Test
	public void getDataIinicioOufimDaSemana() {
		try {
			Date data = UteisData.getData("06/08/2018");
			
			System.out.println("Primeiro Dia Semana => " + UteisData.getDataIinicioOufimDaSemana(data, true));
		
			System.out.println("Ultimo Dia Semana => " + UteisData.getDataIinicioOufimDaSemana(data, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
