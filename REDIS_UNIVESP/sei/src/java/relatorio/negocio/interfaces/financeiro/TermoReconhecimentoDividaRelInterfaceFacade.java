/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.TermoReconhecimentoDividaRelVO;

/**
 *
 * @author Philippe
 */
public interface TermoReconhecimentoDividaRelInterfaceFacade {

	public List<TermoReconhecimentoDividaRelVO> criarObjeto(Integer aluno, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, Boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, String observacaoComplementar) throws Exception;

	public List<TermoReconhecimentoDividaRelVO> criarObjetoLayout2(Integer aluno, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, Boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<ContaReceberVO> consultarNovaContaReceber(NegociacaoContaReceberVO negociacaoContaReceberVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public String designIReportRelatorio();

	public String designIReportRelatorioLayout2();

	public String caminhoBaseRelatorio();

	public List<TermoReconhecimentoDividaContaReceberRelVO> criarObjetoTermoReconhecimentoDividaContaReceber(ContaReceberVO contaReceberVO, String tipoContaReceber, UsuarioVO usuarioVO , FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

	public String designIReportRelatorioContaReceber();

	/**
	 * @author Wellington Rodrigues - 11/06/2015
	 * @return
	 */
	String designIReportRelatorioLayout3();

	/**
	 * @author Wellington Rodrigues - 11/06/2015
	 * @param aluno
	 * @param negociacaoContaReceberVO
	 * @param observacaoHistorico
	 * @param controlarAcesso
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<TermoReconhecimentoDividaRelVO> criarObjetoLayout3(Integer aluno, NegociacaoContaReceberVO negociacaoContaReceberVO, String observacaoHistorico, Boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	String imprimirPorTextoPadrao(MatriculaVO matriculaVO, TermoReconhecimentoDividaRelVO termoReconhecimento, TextoPadraoDeclaracaoVO textoPadraoDeclaracao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

}
