/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Alessandro
 */
public interface DisciplinaGradeRelInterfaceFacade {

	List<GradeCurricularVO> criarObjeto(Integer codigoGradeCurricular, Boolean apresentarDisciplinaComposta, String tipoLayout) throws Exception;

	void validarDados(String tipoRelatorio, Integer unidadeEnsinoCurso, Integer curso, Integer turma, Integer gradeCurricular) throws Exception;

	Map<String, Object> criarObjetoLayout2(Integer gradeCurricular, Integer unidadeEnsinoCurso, Integer curso, Integer turma, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO) throws Exception;

}
