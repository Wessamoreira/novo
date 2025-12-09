package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaCobrancaAlunoVO;

public interface CartaCobrancaAlunoInterfaceFacade {

	void incluir(CartaCobrancaAlunoVO obj) throws Exception;

	List<CartaCobrancaAlunoVO> consultarPorCartaCobranca(Integer codigoCarta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception;

}
