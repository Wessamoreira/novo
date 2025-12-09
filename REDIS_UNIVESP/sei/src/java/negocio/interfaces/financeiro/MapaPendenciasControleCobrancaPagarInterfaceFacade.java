package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaPagarVO;

public interface MapaPendenciasControleCobrancaPagarInterfaceFacade {

	void incluir(MapaPendenciasControleCobrancaPagarVO mapaPendenciasControleCobrancaVO, UsuarioVO usuarioVO) throws Exception;

	List<MapaPendenciasControleCobrancaPagarVO> consultarPorControleCobrancaPagarSelecionado(Integer controleCobranca, Integer qtde, Integer inicio, Boolean selecionado, UsuarioVO usuario) throws Exception;

	Integer consultarQtdeMapaPendenciaPorControleCobranca(Integer controleCobranca, UsuarioVO usuario) throws Exception;

	void alterarSelecionado(Integer codigo, Boolean selecionado, UsuarioVO usuario) throws Exception;

	void alterarSelecionadoPorControleCobrancaPagar(Integer controleCobrancaPagar, Boolean selecionado, UsuarioVO usuario) throws Exception;

}
