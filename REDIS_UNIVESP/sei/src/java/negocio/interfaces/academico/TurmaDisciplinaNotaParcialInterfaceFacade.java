package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaNotaParcialVO;
import negocio.comuns.academico.TurmaDisciplinaNotaTituloVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TurmaDisciplinaNotaParcialInterfaceFacade {	
	
	void excluir(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, UsuarioVO usuarioVO) throws Exception;
	void incluir(final TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, final UsuarioVO usuarioVO) throws Exception;
	void alterar(final TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, final UsuarioVO usuarioVO) throws Exception;	
	List<TurmaDisciplinaNotaParcialVO>consultarPorTurmaDisciplinaNotaTitulo(TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo, UsuarioVO usuario, int nivelMontarDados)throws Exception;
	public List<TurmaDisciplinaNotaParcialVO> consultarPorTurmaDisciplina(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer configuracaoAcademico, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception;
	public List<TurmaDisciplinaNotaParcialVO> consultarPorTurmaDisciplinaTipoNota(TurmaVO turma, DisciplinaVO disciplina, String tipoNota, String ano, String semestre, Integer configuracaoAcademico, UsuarioVO usuarioVO, int nivelMontarDados)throws Exception;
	public void adicionarTurmaDisciplinaNotaParcialItem(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo)throws Exception;
	public void removerTurmaDisciplinaNotaParcialItem(TurmaDisciplinaNotaParcialVO turmaDisciplinaNotaParcialVO, TurmaDisciplinaNotaTituloVO turmaDisciplinaNotaTitulo, UsuarioVO usuarioVO) throws Exception;
	
}
