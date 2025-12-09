package negocio.interfaces.academico;

import java.util.List;

//import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.TextoEnadeVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface TextoEnadeInterfaceFacade {
	
//	void incluirTextoEnadeVOs(EnadeVO enadeVO) throws Exception;
//	
//	void alterarTextoEnadeVOs(EnadeVO enadeVO) throws Exception;
//	
//	void excluirTextoEnadeVOs(EnadeVO enadeVO) throws Exception;
	
	List<TextoEnadeVO> consultarPorEnade(Integer enade) throws Exception;
	
	TextoEnadeVO consultarPorChavePrimaria(Integer textoEnade) throws Exception;
	
	void validarDados(TextoEnadeVO textoEnadeVO) throws ConsistirException;

	List<TextoEnadeVO> consultarUltimosTextosCadastrados() throws Exception;
	
//	public void excluirTextoEnadePorEnade(EnadeVO enadeVO) throws Exception;

}
