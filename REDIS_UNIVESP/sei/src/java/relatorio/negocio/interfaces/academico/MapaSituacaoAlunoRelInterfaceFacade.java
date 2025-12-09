/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaSituacaoAlunoRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 *
 * @author Carlos
 */
public interface MapaSituacaoAlunoRelInterfaceFacade {

    public void validarDados(MatriculaVO matricula, TurmaVO turma, CursoVO curso, Integer unidadeEnsino, String ano, String semestre, String layout) throws ConsistirException;

    public List<MapaSituacaoAlunoRelVO> executarGeracaoRelatorio(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, Integer curso, Integer turma, MatriculaVO matriculaVO, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

    List<MapaSituacaoAlunoRelVO> executarGeracaoRelatorioMapaAluno(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, Integer curso, Integer turma, MatriculaVO matriculaVO, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public List<HistoricoAlunoRelVO> executarGeracaoDocumentoIntegralizacao(List<MatriculaVO> matriculaVOs, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiraVO, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

}
