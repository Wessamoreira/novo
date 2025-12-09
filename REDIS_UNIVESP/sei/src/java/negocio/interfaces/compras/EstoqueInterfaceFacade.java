package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.EstatisticaEstoqueVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface EstoqueInterfaceFacade {    

    public void incluir(EstoqueVO obj, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception;

    public void incluirSemValidarDados(EstoqueVO obj) throws Exception;

    public void alterar(EstoqueVO obj, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception;

    public void alterarEstoqueMinimo(EstoqueVO obj) throws Exception;

    public void excluir(EstoqueVO obj, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception;

    public EstoqueVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorQuantidade(Double valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorNomeProduto(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorCodigoProduto(Integer produto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorCodigoProdutoComQuantidade(Integer produto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<EstoqueVO>  consultarPorNomeUnidadeEnsinoAgrupado(String valorConsulta, Integer unidadeEnsino, String tipoEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorNomeProdutoAgrupado(String valorConsulta, Integer unidadeEnsino, String tipoEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorNomeCategoriaProdutoAgrupado(String valorConsulta, Integer unidadeEnsino, String tipoEstoque, boolean agruparPorEstoque, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorNomeProdutoAbaixoEstoqueMinimo(String nomeProduto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO>  consultarPorCodigoProdutoAbaixoEstoqueMinimo(Integer codigoProduto, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public EstatisticaEstoqueVO consultarEstatisticaEstoqueAtualizada(UsuarioVO usuario) throws Exception;

    public EstoqueVO consultarEstoquerPorProdutoPorUnidadeValidandoEstoqueMinino(Integer codigo, Integer codigo2, int nivelmontardadosDadosbasicos) throws Exception;    

    public EstoqueVO consultarPorUnidadeEnsinoProduto(Integer unidadeEnsino, Double precoUnitario, Integer produto, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO> consultarPorUnidadeEnsinoProduto(Integer unidadeEnsino, Double precoUnitario, Date data, Integer produto, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;
    
    public List<EstoqueVO> consultarPorUnidadeEnsinoProdutoQueNaoTenhaVinculoComOperacaoEstoque(Integer unidadeEnsino, Double precoUnitario, Date data, Integer produto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public Boolean existeEstoqueProduto(Integer unidadeEnsino, Integer produto, Double quantidade, UsuarioVO usuario) throws Exception;

    public List<ItemCotacaoUnidadeEnsinoVO> consultarQtdeMinimaPorUnidadeEnsinoProduto(List<ItemCotacaoUnidadeEnsinoVO> listaItemCotacaoUnidadeEnsinoVO, Integer produto, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<EstoqueVO> consultarPorProdutoPorUnidadeEnsinoAgrupado(Integer produto, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void excluir(List<EstoqueVO> lista, UsuarioVO usuarioVO, Boolean controlarAcesso) throws Exception;

	void atualizarEstoqueCampoQuantidade(EstoqueVO obj, UsuarioVO usuario) throws Exception;
}
