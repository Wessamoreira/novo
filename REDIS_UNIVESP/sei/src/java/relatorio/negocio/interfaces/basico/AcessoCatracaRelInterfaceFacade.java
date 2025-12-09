/**
 * 
 */
package relatorio.negocio.interfaces.basico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.OrdenadorVO;
import relatorio.negocio.comuns.basico.AcessoCatracaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * @author Carlos Eugênio
 *
 */
public interface AcessoCatracaRelInterfaceFacade {
	public List<AcessoCatracaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, List<CursoVO> listaCursoVOs, List<TurnoVO> listaTurnoVOs, Date periodoAcessoInicio, Date periodoAcessoFinal, Integer turma, String matricula, String ano, String semestre, String campoFiltro, String tipoRelatorio, Integer catraca, String periodicidade, List<OrdenadorVO> listaOrdenadorVOs, String cursoApresentar, String turnoApresentar, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception;
}
