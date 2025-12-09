package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import relatorio.negocio.comuns.financeiro.EntregaBoletosRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface EntregaBoletosRelInterfaceFacade {

	@Deprecated
	public List<EntregaBoletosRelVO> criarObjeto(List<MatriculaVO> listaMatriculaVO, boolean relatorioPorTurma, boolean apresentarCampoData, TurmaVO turma, int periodoLetivo, Boolean periodoLetivoControle, String tipoDocumento, String ano, String semestre) throws Exception;
	/**
	 * @author Rodrigo Wind - 16/02/2016
	 * @param tipoDocumento
	 * @param periodoLetivoControle
	 * @param filtrarPorTurma
	 * @param turma
	 * @param matriculaVO
	 * @param periodoLetivo
	 * @param ano
	 * @param semestre
	 * @param tipoAluno
	 * @param filtroRelatorioAcademicoVO
	 * @return
	 * @throws Exception
	 */
	List<EntregaBoletosRelVO> realizarGeracaoRelatorio(String tipoDocumento, Boolean periodoLetivoControle, Boolean filtrarPorTurma, TurmaVO turma, MatriculaVO matriculaVO, Integer periodoLetivo, String ano, String semestre, String tipoAluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,DisciplinaVO disciplinaVO, Boolean carregarFotoAluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

}