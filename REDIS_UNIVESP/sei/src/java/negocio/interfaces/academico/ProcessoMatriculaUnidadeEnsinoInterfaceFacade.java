package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ProcessoMatriculaUnidadeEnsinoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface ProcessoMatriculaUnidadeEnsinoInterfaceFacade {
	
	void persistir(ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuarioVO) throws Exception;
	void incluir(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;	
	void excluir(ProcessoMatriculaUnidadeEnsinoVO processoMatriculaUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	Boolean consultarProcessoMatriculaUnidadeEnsinoVinculadoMatricula(Integer  processoMatricula, Integer unidadeEnsino) throws Exception;
	List<ProcessoMatriculaUnidadeEnsinoVO> consultarPorProcessoMatricula(Integer processoMatricula) throws Exception;
	void carregarUnidadeEnsinoNaoSelecionado(ProcessoMatriculaVO processoMatriculaVO, Integer unidadeEnsinoLogado) throws Exception;

}
