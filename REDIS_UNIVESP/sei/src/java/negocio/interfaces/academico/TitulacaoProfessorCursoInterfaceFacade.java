/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TitulacaoProfessorCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

/**
 *
 * @author Philippe
 */
public interface TitulacaoProfessorCursoInterfaceFacade {
    public void incluir(final TitulacaoProfessorCursoVO obj) throws Exception;
    public void alterar(final TitulacaoProfessorCursoVO obj) throws Exception;
    public void excluir(TitulacaoProfessorCursoVO obj, UsuarioVO usuario) throws Exception;
    public List consultar(String campoConsulta, String valorConsulta, UsuarioVO usuario) throws Exception;
    public TitulacaoProfessorCursoVO consultarPorChavePrimaria(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public TitulacaoProfessorCursoVO consultarPorCodigoCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public TitulacaoProfessorCursoVO consultarTitulacaoProfessorCursoPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void removerVagasTitulacaoProfessorCurso(List<PessoaVO> listaProfessores, Integer professorExcluido, TitulacaoProfessorCursoVO titulacaoProfessorCursoVO);
    public void preencherVagasTitulacaoProfessorCurso(List<PessoaVO> professor, TitulacaoProfessorCursoVO titulacaoProfessorCursoVO);
    public void preencherListaTitulacaoQuantidadeFuncionarios(TitulacaoProfessorCursoVO titulacaoProfessorCursoVO);
}
