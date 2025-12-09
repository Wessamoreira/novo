/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import relatorio.negocio.comuns.financeiro.TipoDescontoRelVO;

/**
 *
 * @author Philippe
 */
public interface TipoDescontoRelInterfaceFacade {

	List<TipoDescontoRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String filtro, Integer unidadeEnsino, Integer curso, Integer turma, Integer aluno, Boolean descontoAluno, String ano, String semestre, Integer convenio, Integer planoDesconto, Integer descontoprogressivo, String financiamentoestudantil) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio();

	String designIReportRelatorioExcel();

}
