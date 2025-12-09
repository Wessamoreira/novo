/**
 * 
 */
package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraRelVO;

/**
 * @author Rodrigo Wind
 *
 */
public interface OperacaoFinanceiraCaixaRelInterfaceFacade {
	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */
	
	public List<OperacaoFinanceiraRelVO> realizarGeracaoRelatorio(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout, UsuarioVO usuarioLogadoVO) throws Exception;
	
}
