package negocio.interfaces.academico;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CalendarioLancamentoPlanoEnsinoVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface CalendarioLancamentoPlanoEnsinoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	void consultar(DataModelo dataModelo, CalendarioLancamentoPlanoEnsinoVO obj) throws Exception;

	CalendarioLancamentoPlanoEnsinoVO consultarCalendarioLancamentoPorPlanoEnsino(PlanoEnsinoVO planoEnsinoVO, boolean visaoProfessor) throws Exception;

	
}
