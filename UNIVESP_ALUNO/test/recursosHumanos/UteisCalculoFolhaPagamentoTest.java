package recursosHumanos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import arquitetura.TestManager;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.UteisCalculoFolhaPagamento;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class UteisCalculoFolhaPagamentoTest extends TestManager {

	private static final long serialVersionUID = 7115063483578052249L;
	
	private Date dataInicialAno;
	
	protected FuncionarioCargoVO fabricaDeFuncionarioCargo (Date dataInicialAno, Date dataAdmissao, Date dataDemissao) {
		
		FuncionarioCargoVO funcionarioCargo = new FuncionarioCargoVO();
		
		funcionarioCargo.setDataAdmissao(dataAdmissao);
		funcionarioCargo.setDataDemissao(dataDemissao);
		
		return funcionarioCargo;
	}
	
	@Before
	public void before() throws Exception {
		dataInicialAno = UteisData.getData("2018/01/01", "yyyy/MM/dd");
	}

	@After
	public void after() throws Exception {
	}

	
	///////////////////////////////////////////////// TESTE 13 COM DEMISSAO////////////////////////////////////////////
	@Test
	public void test13ComDemissaoAvoInicialIncluido() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/03/01", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/09/08", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 6);
	}
	
	@Test
	public void test13ComDemissaoDoisAvoIncluidos() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/03/15", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/09/15", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 7);
	}
	
	@Test
	public void test13ComDemissaoNenhumAvoIncluidoEmMesesDiferentes() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/04/16", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/08/02", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 3);
	}
	
	@Test
	public void test13ComDemissaoNenhumAvoIncluidoNoMesmoMes() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/10/02", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/10/16", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 1);
	}
	
	@Test
	public void test13ComDemissaoUmAvoIncluidoNoMesmoMes() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/10/02", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/10/17", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 1);
	}
	
	@Test
	public void test13ComDemissaoNenhumAvoIncluidoNoMesmoMesDatasProximas() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/10/10", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/10/16", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 0);
	}
	
	@Test
	public void test13ComDemissaoAdmissaoAnoAnteriorAvoIncluido() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2017/10/10", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/10/16", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 10);
	}
	
	
	@Test
	public void test13ComDemissaoAdmissaoAnoAnteriorAvoIncluido2() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2017/10/10", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/08/16", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 8);
	}
	
	@Test
	public void test13ComDemissaoAdmissaoAnoAnteriorAvoDoMesNaoIncluido() {
		
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2017/10/10", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/09/12", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 8);
	}
	
	
	///////////////////////////////////////////////// TESTE 13 SEM DEMISSAO////////////////////////////////////////////
	@Test
	public void test13SemDemissaoAvoInicialIncluido() {
	
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/11/16", "yyyy/MM/dd");
			Date dataDemissao = null;
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 1);
	}
	
	@Test
	public void test13SemDemissaoAvoDoMes() {
	
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/04/02", "yyyy/MM/dd");
			Date dataDemissao = null;
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 9);
	}
	
	@Test
	public void test13SemDemissaoAvoDoMesDataLimite() {
	
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/03/15", "yyyy/MM/dd");
			Date dataDemissao = null;
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 10);
	}
	
	@Test
	public void test13SemDemissaoAvoInicial() {
	
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2018/01/02", "yyyy/MM/dd");
			Date dataDemissao = null;
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 12);
	}
	
	@Test
	public void test13SemDemissaoDataAdmissaoNoAnoAnteriorAvo() {
	
		int teste = 0;
		try {
			Date dataAdmissao = UteisData.getData("2017/01/02", "yyyy/MM/dd");
			Date dataDemissao = null;
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionais13(dataInicialAno, dataAdmissao, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 12);
	}
	
	
	///////////////////////////////////////////////// TESTE FERIAS ////////////////////////////////////////////

	@Test
	public void testFeriasProporcionalComAvos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/11/16", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2019/03/25", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 4);
	}

	@Test
	public void testFeriasProporcionalComAvosCompletos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/11/16", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2019/11/15", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 12);
	}
	
	@Test
	public void testFeriasProporcionalMesmoMesSemAvos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/02/08", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/02/20", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 0);
	}
	
	@Test
	public void testFeriasProporcionalMesmoMesComAvos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/02/08", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/02/22", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 1);
	}
	
	@Test
	public void testFeriasProporcionalMesDiferenteComAvos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/03/01", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/04/05", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 1);
	}
	
	@Test
	public void testFeriasProporcionalMesesProximosComAvos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/03/29", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/04/12", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 1);
	}
	
	@Test
	public void testFeriasProporcionalMesesProximosSemAvos() {
	
		int teste = 0;
		try {
			Date dataInicioPA = UteisData.getData("2018/03/29", "yyyy/MM/dd");
			Date dataDemissao = UteisData.getData("2018/04/11", "yyyy/MM/dd");
			teste = negocio.comuns.utilitarias.UteisCalculoFolhaPagamento.realizarCalculoAvosProporcionaisFerias(dataInicioPA, dataDemissao, 0);
		} catch (ParseException e) {
			fail();
		}
		
		assertEquals(teste, 0);
	}
	
	@Test
	public void testRealizarCalculoDiasAfastado() {
		try {
			Date dataInicio = UteisData.getData("2018/12/02", "yyyy/MM/dd");
			Date dataFinal = UteisData.getData("2019/01/22", "yyyy/MM/dd");
			Date dataComparacao = UteisData.getData("2019/01/05", "yyyy/MM/dd");

			int quantidadeDiasAfastado = UteisCalculoFolhaPagamento.realizarCalculoDiasAfastamento(dataInicio, dataFinal, dataComparacao);
			System.out.println("Quantidade Dias Afastado: " + quantidadeDiasAfastado);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}