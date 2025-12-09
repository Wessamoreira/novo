package negocio.interfaces.compras;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.RecebimentoCompraVO;


public interface RecebimentoCompraInterfaceFacade {		

	public void excluir(RecebimentoCompraVO obj, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public RecebimentoCompraVO consultarPorSituacaoPrevisao(Integer compra, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;	

	public RecebimentoCompraVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoCompra(Integer valorConsulta, Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoCompra(Integer valorConsulta, Integer unidadeEnsino, String situacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorNomeFornecedor(String valorConsulta, Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	void atualizarCampoValortotal(RecebimentoCompraVO obj, UsuarioVO usuario) throws Exception;

	void persistir(RecebimentoCompraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<RecebimentoCompraVO> consultarPorNumeroNotaFiscal(Integer valorConsulta, Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	Boolean consultarSeExisteRecebimentoCompraPorCompraPorSituacao(Integer compra, String situacaoRecebimentoCompra, UsuarioVO usuario) throws Exception;	

	void excluirPorCodigoCompra(CompraVO compra, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;		

	Boolean consultarSeExisteRecebimentoCompraPorCompraSemVinculoComNotaFiscalEntrada(Integer compra, UsuarioVO usuario) ;
	
	RecebimentoCompraVO consultarPorCompraPorSituacaoSemVinculoComNotaFiscalEntradaComCodigoDiferente(Integer recebimentoCompra, Integer compra, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	RecebimentoCompraVO estornar(RecebimentoCompraVO obj, UsuarioVO usuarioVO) ;

	Boolean consultarSeExisteRecebimentoCompraPorRecebiemntoCompraPorSituacao(Integer recebimentoCompra, String situacaoRecebimentoCompra);

	void validarGeracaoNovoRecebimentoCompraOuAtualizacao(RecebimentoCompraVO obj, boolean atualizarRecebimentoItem, UsuarioVO usuario);
	
	void atualizacaoRecebimentoCompra(RecebimentoCompraVO obj, RecebimentoCompraVO recebimentoCompraNovo, UsuarioVO usuario);

	
		
}