package recursosHumanos;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import negocio.comuns.recursoshumanos.SalarioCompostoVO;
import negocio.facade.jdbc.recursoshumanos.SalarioComposto;

public class SalarioCompostoTest {
	
	public SalarioCompostoTest() {
		try {
			salarioComposto = new SalarioComposto();	
		}catch (Exception e) {
			System.out.println("Deu ruim");
		}
		
	}
	
	SalarioComposto salarioComposto;
	
	List<SalarioCompostoVO> listaSalarioComposto;
	
	private List<SalarioCompostoVO> salarioCompostoFactory(Integer[] jornadas, BigDecimal[] valores){
		SalarioCompostoVO salarioCompostoVO;
		Integer cont = 0;
		while(jornadas.length > cont) {
			salarioCompostoVO = new SalarioCompostoVO();
			salarioCompostoVO.setJornada(jornadas[cont]);
			salarioCompostoVO.setValorMensal(valores[cont]);
			cont++;
			listaSalarioComposto.add(salarioCompostoVO);
		}
		
		return listaSalarioComposto;
	}
	
	@Before
    public void before() throws Exception {
		listaSalarioComposto = new ArrayList<>();
    }
	
    @After
    public void after() throws Exception {
    }
	
	@Test
	public void testSomaDosValoresMensaisDaListaDeSalarioComposto() throws Exception {
		Integer[] jornadas = {40,20,100,40};
		BigDecimal[] valores = {new BigDecimal(300), new BigDecimal(150), new BigDecimal(750), new BigDecimal(300)};
		
		BigDecimal salarioTotal = salarioComposto.realizarSomaDoValorMensalDoSalarioComposto(salarioCompostoFactory(jornadas, valores));
		
		assertEquals(salarioTotal, new BigDecimal(1500));
	}
	
	@Test
	public void testSomaDasJornadasDaListaDeSalarioComposto() throws Exception {
		Integer[] jornadas = {40,20,100,40};
		BigDecimal[] valores = {new BigDecimal(300), new BigDecimal(150), new BigDecimal(750), new BigDecimal(300)};
		
		Integer jornadaTotal = salarioComposto.realizarSomaDasJornadasDoSalarioComposto(salarioCompostoFactory(jornadas, valores));
		
		assertEquals(jornadaTotal.longValue(), 200l);
	}
}