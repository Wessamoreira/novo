package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.InclusaoHistoricoAlunoDisciplinaVO;
import negocio.comuns.academico.InclusaoHistoricoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface InclusaoHistoricoAlunoDisciplinaInterfaceFacade {

	void incluirInclusaoHistoricoAlunoDisciplinaVOs(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<InclusaoHistoricoAlunoDisciplinaVO> consultarPorInclusaoHistoricoAluno(Integer InclusaoHistoricoAluno, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, InclusaoHistoricoAlunoDisciplinaVO obj) throws ConsistirException;

	void excluir(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO, InclusaoHistoricoAlunoDisciplinaVO inclusaoHistoricoAlunoDisciplinaVO, UsuarioVO usuarioVO)
			throws Exception;
}
