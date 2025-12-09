package recursosHumanos;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.recursoshumanos.CalculoContraCheque;

public class CalculoContraChequeTest {

	@Test
	public void testNumeroDiasTrabalhados() {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		calculo.calcularNumeroDiasTrabalhados();
		assertTrue(calculo.getNumeroDiasTrabalhados() == 30);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataInicioEntreDoisMeses() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/02/12", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/03/13", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(2);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 17);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataFimEntreDoisMeses() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/02/12", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/03/13", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(3);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 13);
	}

	
	@Test
	public void testCalcularDiasFeriasMesComparaDataInicioNoMesmoMes() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/01", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/03/30", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(3);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 30);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataInicioNoMesmoMes2() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/02", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/03/31", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(3);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 30);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataInicioEntreDoisMeses2() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/26", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/04/04", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(3);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 6);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataFimEntreDoisMeses2() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/26", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/04/24", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(4);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 24);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataFimEntreDoisMeses3() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/05", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/04/03", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(3);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 27);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataFimEntreDoisMeses4() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/05", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/04/03", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(4);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 3);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataInicioEntreDoisMesesEAnoDiferente() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/12/17", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2019/01/15", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(12);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 15);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComparaDataFimEntreDoisMesesEAnoDiferente() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/12/17", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2019/01/15", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2019);
		calculo.setMesCompetencia(1);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 15);
	}
	
	@Test
	public void testCalcularDiasFeriasMesComAbono() throws Exception {
		
		CalculoContraCheque calculo = new CalculoContraCheque();
		Date dataInicialGozo = UteisData.getData("2018/03/01", "yyyy/MM/dd");
		Date dataFinalGozo = UteisData.getData("2018/03/20", "yyyy/MM/dd");
		
		calculo.setFuncionarioCargoVO(new FuncionarioCargoVO());
		calculo.getFuncionarioCargoVO().setInicioGozoFerias(dataInicialGozo);
		calculo.getFuncionarioCargoVO().setFinalGozoFerias(dataFinalGozo);
		calculo.setAnoCompetencia(2018);
		calculo.setMesCompetencia(3);
		
		calculo.calcularNumeroDiasFerias();
		assertTrue(calculo.getDiasFeriasMes() == 20);
	}
}