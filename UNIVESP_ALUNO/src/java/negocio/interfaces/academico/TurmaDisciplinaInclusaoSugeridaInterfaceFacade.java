package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaDisciplinaInclusaoSugeridaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TurmaDisciplinaInclusaoSugeridaInterfaceFacade {

	public void adicionarTurmaDisciplinaInclusaoSugerida(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs, List<TurmaDisciplinaVO> turmaDisciplinaVOs);

	public void removerTurmaDisciplinaInclusaoSugerida(List<TurmaDisciplinaInclusaoSugeridaVO> turmaDisciplinaInclusaoSugeridaVOs, TurmaDisciplinaInclusaoSugeridaVO obj);

	public void validarDadosSelecaoTurma(TurmaVO turmaPrincipal, TurmaVO turmaSugerida) throws Exception;

	public void incluirTurmaDisciplinaInclusaoSugeridaVOs(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception;

	public void alterarTurmaDisciplinaInclusaoSugeridaVOs(TurmaVO turma, List objetos, UsuarioVO usuario) throws Exception;

	public void excluirPorTurma(TurmaVO obj, UsuarioVO usuario) throws Exception;
	
	public void carregarDados(TurmaVO obj, UsuarioVO usuario) throws Exception;
	
	public List<TurmaDisciplinaInclusaoSugeridaVO> consultaRapidaPorTurma(Integer turma, UsuarioVO usuarioVO);
}
