package negocio.interfaces.planoorcamentario;

import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */

public interface ItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade {

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO novo() throws Exception;

	public void incluir(final ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(final ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterar(final ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario, boolean remanejamento) throws Exception;

	public void excluir(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void excluirItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer planoOrcamentario, List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception;

	public void alterarItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer planoOrcamentario, List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception;

	public void incluirItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer planoOrcamentarioPrm, List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception;

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarItemSolicitacaoOrcamentarioPorPlanoOrcamentarioDepartamentoUnidadeEnsino(Integer planoOrcamentario, Integer departamento, Integer unidadeEnsino, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception;

	
	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO consultarItemSolicitacaoOrcamentoPlanoOrcamentarioPorValorSolicitadoUnidadeEnsinoCategoriaDespesaDepartamento(Double valorSolicitado, UnidadeEnsinoVO unidadeEnsinoVO, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesaVO, Date mesAnoConsiderar, UsuarioVO usuario) throws Exception;

	void alterarValorAprovado(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO,
			UsuarioVO usuarioVO) throws Exception;

	void calcularValorRemanejamento(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemRemanejado,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemRemanejar , boolean executarDistribuicao) throws Exception;

	void persistir(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception;

	void persistirTodos(List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> itemSolicitacaoOrcamentoPlanoOrcamentarioVOs, UsuarioVO usuario) throws Exception;

	public void gravarItemSolicitacao(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO,
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuarioVO, boolean realizarCalculoValorAprovado) throws Exception;

	public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarItemSolicitacaoOrcamentoPlanoOrcamentario(
			Integer codigo, boolean validarAcesso, UsuarioVO usuario) throws Exception;

}
