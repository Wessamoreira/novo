package negocio.interfaces.planoorcamentario;

import java.util.Date;
import java.util.List;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemMensalDetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;

/**
 *
 * @author Carlos
 */
public interface ItemMensalDetalhamentoPlanoOrcamentoInterfaceFacade {

    public void incluir(final ItemMensalDetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(final ItemMensalDetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirItemMensalDetalhamentoPlanoOrcamentario(Integer detalhamentoPlano, 
    		List<ItemMensalDetalhamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception;

    public void alterarItemMensalDetalhamentoPlanoOrcamentario(Integer detalhamentoPlano, 
    		List<ItemMensalDetalhamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception;

    public void excluirItemMensalDetalhamentoPlanoOrcamentario(Integer detalhamentoPlano, UsuarioVO usuario) throws Exception;

    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> consultarItemMensalDetalhamentoPorDetalhamentoPlanoOrcamentario(Integer detalhamentoPlanoOrcamentario, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> executarDistribuicaoValorItemMensal(DetalhamentoPlanoOrcamentarioVO obj,
    		Date dataInicio, Date dataFim) throws Exception;

    public void validarDadosValorItemMensalDetalhamentoPlanoOrcamentario(Double valorDetalhamento, List<ItemMensalDetalhamentoPlanoOrcamentarioVO> listaItemMensal, List<DetalhamentoPlanoOrcamentarioVO> listaDetalhamentoPlanoOrcamentarioVOs, List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentoVOs) throws Exception;

    public Double consultarTotalMesAutorizacaoRequisicao(Date data, Integer departamento, Integer unidadeEnsino, String mes);

    public void atualizarValorConsumidoMes(Double valorTotalProdutos, Integer departamento, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    
    public ItemMensalDetalhamentoPlanoOrcamentarioVO consultarPorDetalhamentoPlanoOrcamentarioMes(Integer detalhamentoPlano, String mes, UsuarioVO usuario) throws Exception;

    public void atualizarEstornoValorConsumidoMes(Double valorTotal, Integer codigo, Integer codigo2, UsuarioVO usuario) throws Exception;
}
