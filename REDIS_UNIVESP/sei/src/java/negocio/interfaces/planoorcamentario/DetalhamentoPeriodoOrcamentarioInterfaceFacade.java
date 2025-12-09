package negocio.interfaces.planoorcamentario;

import java.util.Date;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 *
 * @author Carlos
 */
public interface DetalhamentoPeriodoOrcamentarioInterfaceFacade {
    public void incluir(final DetalhamentoPeriodoOrcamentoVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(final DetalhamentoPeriodoOrcamentoVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(DetalhamentoPeriodoOrcamentoVO obj, UsuarioVO usuario) throws Exception;    
    public List<DetalhamentoPeriodoOrcamentoVO> consultarDetalhamentoPorPlanoOrcamentario(Integer planoOrcamentario,  int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void incluirDetalhamentoPeriodoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuario) throws Exception;
    public void alterarDetalhamentoPeriodoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuario) throws Exception;
    public List<DetalhamentoPeriodoOrcamentoVO> executarDistribuicaoValorItemMensal(Double orcamentoTotalPrevisto, Date dataInicio, Date dataFim) throws Exception;
    public void validarDadosValorOrcamentoTotalPrevisto(Double valorOrcamentoTotal) throws Exception;
    public void validarDadosValorMensalOrcamentoTotalPrevisto(Double valorDetalhamento, List<DetalhamentoPeriodoOrcamentoVO> listaItemMensal) throws ConsistirException;
    List<DetalhamentoPeriodoOrcamentoVO> executarRedistribuicaoValorItemMensal(List<DetalhamentoPeriodoOrcamentoVO> detalhamentoPeriodoOrcamentoVOs,
			Double orcamentoTotalPrevisto) throws Exception;
	List<DetalhamentoPeriodoOrcamentoVO> consultarDetalhamentoPorItemSolicitacaoPlanoOrcamentario(Integer itemSolicitacaoPlanoOrcamentario, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
	void realizarGeracaoDetalhamentoPorPeriodoPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO)
			throws Exception;
	void alterarValorPorMesDeAcordoComValorAprovado(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO,
			UsuarioVO usuarioVO) throws Exception;
}
