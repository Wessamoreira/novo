/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaCobrancaRelVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * 
 * @author Bruno Henrique
 */
public interface CartaCobrancaRelInterfaceFacade {

	public String getDescricaoFiltros();

	String designIReportRelatorio(String tipoLayout);

	String caminhoBaseRelatorio();

	public List<CartaCobrancaRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, CartaCobrancaRelVO cartaCobranca, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, String aluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs, String centroReceita) throws Exception;

	List<SelectItem> montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception;

	void validarDados(UnidadeEnsinoVO unidadeEnsino, CursoVO curso, Date dataFim) throws ConsistirException;

}
