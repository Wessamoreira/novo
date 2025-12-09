package negocio.interfaces.academico;

import controle.arquitetura.DataModelo;
//import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;


public interface CalendarioLancamentoPlanoEnsinoInterfaceFacade<T extends SuperVO>  {

	void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	void consultar(DataModelo dataModelo) throws Exception;

//	CalendarioLancamentoPlanoEnsinoVO consultarCalendarioLancamentoPorPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, boolean visaoProfessor) throws Exception;

	
}
