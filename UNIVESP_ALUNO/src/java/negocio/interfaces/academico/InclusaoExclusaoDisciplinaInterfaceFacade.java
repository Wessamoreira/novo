/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.interfaces.academico;

import java.util.HashMap;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * 
 * @author Carlos
 */
public interface InclusaoExclusaoDisciplinaInterfaceFacade {

    public List consultarAlunosPorMatricula(String matricula, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public MatriculaVO consultarAlunoPorMatricula(String matricula, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception;

    public void consultarTurmaPorChavePrimaria(TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuario) throws Exception;

    public List consultarAlunosPorTurma(TurmaVO turmaVO, CursoVO cursoApresentar, String ano, String semestre, Integer unidadeEnsino, String situacaoMatricula, UsuarioVO usuarioVO) throws Exception;

    public List consultarAluno(String valorConsultaAluno, String campoConsultaAluno, UsuarioVO usuario) throws Exception;

    public List executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva(List<MatriculaVO> listaHistoricoAlunos, Integer codigoUnidadeEnsino, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception;

    public void adicionarTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, List listaTurmaDisciplinasIncluida) throws Exception;

    public void persistir(List<DisciplinaVO> listaExclusaoDisciplina, List<MatriculaVO> listaAluno, List<MatriculaPeriodoTurmaDisciplinaVO> listaInclusaoTurmaDisciplina,
            List<MatriculaPeriodoTurmaDisciplinaVO> listaExclusaoAposPersistir, Integer periodoLetivo, String tipoBusca, String ano, String semestre, Integer turmaConsulta, String nivelEducacionalCurso, UsuarioVO usuario) throws Exception;

    public void excluirObjsTurmaDisciplinaIncluidaVOs(MatriculaPeriodoTurmaDisciplinaVO obj, List listaTurmaDisciplinaIncluidaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> listaExclusaoAposPersistir) throws Exception;

    public List consultarDisciplinaIncluidaRich(HashMap<Integer, MatriculaPeriodoVO> hashMap, String campoConsultaDisciplinaIncluida, String valorConsultaDisciplinaIncluida, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List consultarDisciplinaIncluidaRich(MatriculaPeriodoVO matriculaPeriodoVO, String campoConsultaDisciplinaIncluida, String valorConsultaDisciplinaIncluida, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina, List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasJaNaListaInclusao, List<DisciplinaVO> listaDisciplinasAtuais, TurmaVO turmaAtual, String semestre, String ano, UsuarioVO usuario) throws Exception;

    public List realizarMontagemListaPeriodoLetivo(MatriculaVO matriculaVO, UsuarioVO usuario, Boolean campoObrigatorio) throws Exception;

    public List executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva(List<MatriculaVO> listaAlunos, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception;

    public List executarInsercaoDisciplinasPorListaMatricula(List<MatriculaVO> listaAlunos, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception;
}
