package negocio.interfaces.compras;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ProdutoServicoInterfaceFacade {

    public void incluir(ProdutoServicoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(ProdutoServicoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(ProdutoServicoVO obj, UsuarioVO usuarioVO) throws Exception;
    public ProdutoServicoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorCodigoECategoriaProduto(Integer valorConsulta, Integer codCategoriaProduto, Boolean exigeCotacao,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNome(String valorConsulta, Boolean controlarEstoque, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNome(String valorConsulta, Boolean controlarEstoque, TipoProdutoServicoEnum tipoProdutoServico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNome(String valorConsulta, Boolean controlarEstoque, boolean exigeCompraCotacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNomeCategoriaProduto(String valorConsulta, Boolean controlarEstoque,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNomeCategoriaProduto(String valorConsulta, Boolean controlarEstoque, TipoProdutoServicoEnum tipoProdutoServicoEnum, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNomeECategoriaProduto(String nomeProduto, Integer codCategoriaProduto, Boolean exigeCotacao,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorCodigoCategoriaProduto(Integer valorConsulta, Boolean controlarEstoque,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarProdutoQueExigemCotacaoPorCategoriaProduto(Integer valorConsulta,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorTipoUnidade(String valorConsulta, Boolean controlarEstoque, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void atualizarValorUltimaCompraProdutoServico(final Double valorUltimaCompra, final Integer codigo) throws Exception;
    public Double consultarTotalQuantidadeAutorizadaPorPrecoUnitarioProduto();
    public List<ProdutoServicoVO> consultarPorCodigoQueExigemCotacaoPorCategoriaProduto(Integer produtoServico, Integer categoriaProduto, UsuarioVO usuarioVO);
    public List<ProdutoServicoVO> consultarPorNomeQueExigemCotacaoPorCategoriaProduto(String valorConsulta, Integer categoriaProduto, UsuarioVO usuarioVO);
    public List<ProdutoServicoVO> consultarPorNomeCategoriaProdutoQueExigemCotacaoPorCategoriaProduto(String valorConsulta, Integer categoriaProduto, UsuarioVO usuarioVO);
	List<ProdutoServicoVO> consultarProdutoServicoExigeCompraCotacao(Integer codigoCategoriaProduto,UsuarioVO usuarioVO) throws Exception;
	List<ProdutoServicoVO> consultarPorListaRequisicao(List<RequisicaoVO> listaRequisicao, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, boolean controlaAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public List<ProdutoServicoVO> consultarPorFiltros(String nomeProdutoPesquisa, Integer categoriaProdutoPesquisa, String tipoProdutoPesquisa, String situacaoProdutoPesquisa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, DataModelo dataModelo) throws Exception;
	public Integer consultarTotalPorFiltros(String nomeProdutoPesquisa, Integer categoriaProdutoPesquisa, String tipoProdutoPesquisa, String situacaoProdutoPesquisa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorCodigoECategoriaProdutoAtivo(Integer valorConsulta, Integer codCategoriaProduto, Boolean exigeCotacao,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List<ProdutoServicoVO> consultarPorNomeECategoriaProdutoAtivo(String nomeProduto, Integer codCategoriaProduto, Boolean exigeCotacao,boolean controlaAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
}
