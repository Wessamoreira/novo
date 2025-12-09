package negocio.interfaces.compras;

import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;

public interface RequisicaoItemInterfaceFacade {

	public void atualizarRequisitoItemPorCotacao(ItemCotacaoUnidadeEnsinoVO icue, UsuarioVO usuario) throws Exception;

	void anularVinculoRequisicaoItemComCotacao(Integer cotacao, UsuarioVO usuario) throws Exception;

	public List<RequisicaoItemVO> consultarRapidaRequisicaoItems(RequisicaoVO requisicao, TipoAutorizacaoRequisicaoEnum tipoAutorizacaoRequisicaoEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<RequisicaoItemVO> consultarRequisicaoItems(RequisicaoVO requisicao, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void excluir(RequisicaoItemVO obj) throws Exception;

	public RequisicaoItemVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String idEntidade);	

	void persistir(List<RequisicaoItemVO> lista, UsuarioVO usuarioVO) throws Exception;

	void alterar(RequisicaoItemVO obj, UsuarioVO usuario) throws Exception;

	void anularVinculoRequisicaoItemComCompraItem(List<CompraItemVO> lista, UsuarioVO usuario) throws Exception;

	void atualizarRequisitoItemPorCompraItem(CompraItemVO compraItemVO, UsuarioVO usuario) throws Exception;

	List<RequisicaoItemVO> consultarRequisicaoItemsPorCompraItem(CompraItemVO compraItem, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<RequisicaoItemVO> consultarRequisicaoItemsPorItemCotacaoUnidadeEnsinoVO(List<RequisicaoVO> listaRequisicao, ItemCotacaoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void atualizarRequisitoItemPorEntregaRequisicaoItem(RequisicaoItemVO obj, UsuarioVO usuario) throws Exception;

	public List<RequisicaoItemVO> consultarItemSolicitacaoOrcamentarioPorSolicitacaoPlanoOrcamentario(Integer solicitacaoPlanoOrcamentario, boolean controlarAcesso, UsuarioVO usuario, DepartamentoVO departamento) throws Exception;

	public void validarExisteRequisicaoItemPorPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO) throws Exception;

	List<RequisicaoItemVO> consultarRequisicaoItemConsumidoPlanoOrcamentario(Integer planoOrcamentario,
			Integer solicitacaoPlanoOrcamentario, Integer unidadeEnsino, Integer departamento, Integer categoriaDespesa,
			MesAnoEnum mesAno, String ano) throws Exception;
}
