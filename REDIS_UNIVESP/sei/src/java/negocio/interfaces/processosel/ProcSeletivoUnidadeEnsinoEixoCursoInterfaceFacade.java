package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;

public interface ProcSeletivoUnidadeEnsinoEixoCursoInterfaceFacade {

	
	public ProcSeletivoUnidadeEnsinoEixoCursoVO novo() throws Exception;

	
	public void incluir(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	
	public void alterar(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	
	public void excluir(ProcSeletivoUnidadeEnsinoEixoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

	public ProcSeletivoUnidadeEnsinoEixoCursoVO consultarPorChavePrimaria(Integer procSeletivoUnidadeEnsino,
			Integer eixoCurso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	public void incluirProcSeletivoEixoCursoVO(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;
	
	public void alterarProcSeletivoUnidadeEnsinoEixoCurso(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception;


	ProcSeletivoUnidadeEnsinoEixoCursoVO consultarPorInscricaoUnidadeEnsinoEixoCurso(Integer inscricao,Integer unidadeEnsino, Integer eixoCurso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
}
