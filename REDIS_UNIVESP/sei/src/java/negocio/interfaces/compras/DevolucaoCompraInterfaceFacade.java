package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CreditoFornecedorVO;
import negocio.comuns.compras.DevolucaoCompraVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface DevolucaoCompraInterfaceFacade {

	public DevolucaoCompraVO novo() throws Exception;

	public void incluir(DevolucaoCompraVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception;

	public void alterar(DevolucaoCompraVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(DevolucaoCompraVO obj, UsuarioVO usuarioVO) throws Exception;

	public DevolucaoCompraVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DevolucaoCompraVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DevolucaoCompraVO> consultarPorCodigoCompra(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DevolucaoCompraVO> consultarPorNomeFornecedor(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public String sugerirValorParcelas(DevolucaoCompraVO devolucaoCompraVO, CreditoFornecedorVO creditoFornecedorVO) throws Exception;

	public void calcularValorCredito(DevolucaoCompraVO devolucaoCompraVO, CreditoFornecedorVO creditoFornecedorVO);
}