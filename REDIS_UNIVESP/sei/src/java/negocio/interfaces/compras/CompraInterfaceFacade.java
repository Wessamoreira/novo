package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.EstatisticaCompraVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;

public interface CompraInterfaceFacade {

	public void incluir(CompraVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoEntrega(Integer codigo, String situacaoRecebimento) throws Exception;

	public void alterarSituacaoFinanceira(Integer codigo, String situacaoFinanceira) throws Exception;

	public void excluir(CompraVO obj, String motivoExclusao, Boolean controlarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO conf) throws Exception;

	public List<CompraVO> consultarPorDataCompra(Date prmIni, Date prmFim, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalPorDataCompra(Date prmIni, Date prmFim, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;	
	
 

	public List<CompraVO> consultarPorNomeFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalPorNomeFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CompraVO> consultarPorCodigo(Integer valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal,Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalPorCodigo(Integer valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public CompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);

	public EstatisticaCompraVO consultarEstatisticaRecebimentoCompraAtualizada(UsuarioVO usuario) throws Exception;

	List<CompraVO> consultarPorCodigoCotacao(Integer valorConsulta, String situacaoFinanceira, String situacaoRecebimento, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalPorCodigoCotacao(Integer valorConsulta, String situacaoFinanceira, String situacaoRecebimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	CompraVO consultarPorCompraPorCodOrigemContaPagar(String codOrigem, OrigemContaPagar origemContaPagar, UsuarioVO usuario);

	void adicionarRequisicaoCompraDireta(CompraVO obj, List<RequisicaoVO> listaRequisicao, UsuarioVO usuario);

	void removerRequisicaoCompraDireta(CompraVO obj, RequisicaoVO requisicaoVO, UsuarioVO usuario);

	void adicionarObjCompraItemVOs(CompraVO obj, CompraItemVO ci, UsuarioVO usuario);

	void preencherDadosPorCategoriaDespesa(CompraItemVO obj, UsuarioVO usuario) throws Exception;

	void gerarCentroResultadoOrigemPorCompraItem(CompraVO obj, UsuarioVO usuario);

	boolean consultarSeExisteCompraPorCotacao(Integer cotacao, Integer compra, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List consultarPorCnpjFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Integer consultarTotalPorCnpjFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}