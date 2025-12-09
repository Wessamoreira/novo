package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.bancocurriculum.VagaEstadoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface VagaEstadoInterfaceFacade {

	void incluirVagaEstado(VagasVO vagasVO) throws Exception;
	void alterarVagaEstado(VagasVO vagasVO) throws Exception;
	List<VagaEstadoVO> consultarPorVaga(Integer vaga) throws Exception;
	void validarDados(VagaEstadoVO vagaEstadoVO) throws ConsistirException;
	
}
