package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface TurmaContratoInterfaceFacade {

	void incluirTurmaContratoVOs(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception;
	void incluir(TurmaContratoVO turmaContratoVO, UsuarioVO usuarioVO) throws Exception;
	void alterarTurmaContratoVOs(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception;
	void alterar(TurmaContratoVO turmaContratoVO, UsuarioVO usuarioVO) throws Exception;
	void excluirTurmaContratoVOs(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception;
	void excluir(TurmaContratoVO turmaContratoVO, UsuarioVO usuarioVO) throws Exception;
	void adicionarTurmaContratoVOs(TurmaVO turmaVO, TurmaContratoVO turmaContratoAdicionarVO, UsuarioVO usuarioVO) throws Exception;
	void removerTurmaContratoVOs(TurmaVO turmaVO, TurmaContratoVO turmaContratoRemoverVO, UsuarioVO usuarioVO) throws Exception;
	List<TurmaContratoVO> consultarPorTurma(Integer turma, UsuarioVO usuarioVO) throws Exception;
	TurmaContratoVO consultarChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception;
	List<TurmaContratoVO> consultarTurmaTipoContratoMatricula(Integer turma, TipoContratoMatriculaEnum tipoContratoMatriculaEnum, boolean trazerApenasPadrao, UsuarioVO usuarioVO) throws Exception;
	void validarDados(TurmaContratoVO turmaContratoVO) throws ConsistirException;
}
