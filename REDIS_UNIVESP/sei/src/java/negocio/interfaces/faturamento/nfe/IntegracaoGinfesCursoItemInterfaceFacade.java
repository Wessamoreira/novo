package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoItemVO;
import negocio.comuns.faturamento.nfe.IntegracaoGinfesCursoVO;

public interface IntegracaoGinfesCursoItemInterfaceFacade {

	void incluirCursos(final IntegracaoGinfesCursoVO obj, List<IntegracaoGinfesCursoItemVO> cursos, UsuarioVO usuario) throws Exception;

	void excluirCursos(IntegracaoGinfesCursoVO obj, UsuarioVO usuario) throws Exception;
	
	List<IntegracaoGinfesCursoItemVO> gerarCursos(Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	List<IntegracaoGinfesCursoItemVO> consultarPorIntegracao(Integer integracao, UsuarioVO usuario) throws Exception;

}
