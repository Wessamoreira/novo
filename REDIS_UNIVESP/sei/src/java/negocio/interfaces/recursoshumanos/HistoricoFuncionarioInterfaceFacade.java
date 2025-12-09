package negocio.interfaces.recursoshumanos;

import controle.arquitetura.DataModelo;

public interface HistoricoFuncionarioInterfaceFacade {
	
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception;

	public void consultarPorEnumCampoConsultaSomenteProfessores(DataModelo dataModelo, String situacaoFuncionario, boolean consultarSomenteProfessores) throws Exception;
}
