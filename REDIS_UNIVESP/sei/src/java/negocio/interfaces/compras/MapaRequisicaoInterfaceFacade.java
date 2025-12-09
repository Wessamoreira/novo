package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EstatisticaRequisicaoVO;
import negocio.comuns.compras.MapaRequisicaoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

public interface MapaRequisicaoInterfaceFacade {

	public void setIdEntidade(String aIdEntidade);

	public void autorizarTodas(List<MapaRequisicaoVO> lista, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception;

	public void autorizar(MapaRequisicaoVO obj, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception;

	public void indeferir(MapaRequisicaoVO obj, UsuarioVO responsavel, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception;

	public EstatisticaRequisicaoVO consultarEstatisticaRequisicoesAtualizada(UsuarioVO usuario) throws Exception;

	List<MapaRequisicaoVO> consultar(RequisicaoVO requisicaoFiltro, Date dataIni, Date dataFim, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void desfazerAutorizar(MapaRequisicaoVO obj, ConfiguracaoFinanceiroVO confFinanceiro, UsuarioVO usuario) throws Exception;
}