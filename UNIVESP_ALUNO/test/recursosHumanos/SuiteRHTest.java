package recursosHumanos;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	AfastamentoFuncionarioTest.class,
	UteisCalculoFolhaPagamentoTest.class,
   CompetenciaFolhaPagamentoTest.class,
   ControleMarcacaoFeriasTest.class,
   FaltasFuncionarioTest.class,
   HistoricoDependentesTest.class,
   HistoricoFuncaoTest.class,
   HistoricoSalarialTest.class,
   HistoricoSecaoTest.class,
   HistoricoSituacaoTest.class,
   PeriodoAquisitivoFeriasControleTest.class,
   PeriodoAquisitivoFeriasTest.class,
   SalarioCompostoTest.class   
})
public class SuiteRHTest {

	public class FeatureTestSuite {
	  // the class remains empty,
	  // used only as a holder for the above annotations
	}	
}