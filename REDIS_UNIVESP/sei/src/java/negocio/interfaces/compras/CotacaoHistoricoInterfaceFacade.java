package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;

public interface CotacaoHistoricoInterfaceFacade {

	

	void incluir(CotacaoHistoricoVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	CotacaoHistoricoVO consultarPorCotacao(CotacaoVO cotacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	boolean avancarTramiteCotacao(CotacaoHistoricoVO cotacaoHistorico, UsuarioVO selecionado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception;

	boolean retornarTramiteCotacao(CotacaoHistoricoVO cotacaoHistorico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception;

	CotacaoHistoricoVO iniciarTramiteCotacao(CotacaoVO cotacao, UsuarioVO selecionado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void alterar(CotacaoHistoricoVO obj, Boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception;

	boolean finalizarTramiteCotacao(CotacaoHistoricoVO cotacaoHistorico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralPadraoSistema) throws Exception;

	public DepartamentoTramiteCotacaoCompraVO buscarProximoDepartamentoTramiteCotacaoCompra(CotacaoHistoricoVO cotacaoHistoricoVO);

	public boolean historicoAnteriorPossuiOrdemMaiorQueAtual(CotacaoHistoricoVO cotacaoHistorico);

	boolean isTramiteIniciado(CotacaoVO cotacao);

	List<CotacaoHistoricoVO> consultaRapidaPorCotacao(CotacaoVO cotacao, UsuarioVO usuario);

	void atualizarResponsavelCotacaoHistorio(CotacaoHistoricoVO obj, UsuarioVO usuario);
}