package negocio.interfaces.planoorcamentario;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoRemanejamentoPlanoOrcamentarioInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void remanejarItemSolicitacaoOrcamentoPlanoOrcamentario(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioSelecionado,
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioRemanejado, 
			UsuarioVO usuarioLogado, SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, String motivo) throws Exception;

	public void consultarPorEnumCampoConsulta(DataModelo dataModeloHistoricoRemanejamento, UsuarioVO usuarioLogado) throws Exception;

}
