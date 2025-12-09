package relatorio.negocio.interfaces.financeiro;

import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.financeiro.BalanceteRelVO;

public interface BalanceteRelInterfaceFacade {

	public List<BalanceteRelVO> consultarContasPagasRecebidas(BalanceteRelVO obj, UsuarioVO usuarioVO);

	public List<BalanceteRelVO> consultarTodasContas(BalanceteRelVO obj);

	public SqlRowSet consultarIdentificadorCategoriaDespesa() throws Exception;

	public SqlRowSet consultarIdentificadorCentroReceita() throws Exception;

}