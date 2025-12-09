package negocio.comuns.compras;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;

/**
 * Reponsável por manter os dados da entidade CompetenciaFolhaPagamento. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class RequisicaoItemSolicitacaoOrcamentoPlanoOrcamentarioVO extends SuperVO {

	private static final long serialVersionUID = -5035863420157486544L;

	private Integer codigo;
	private RequisicaoItemVO requisicaoItemVO;
	private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RequisicaoItemVO getRequisicaoItemVO() {
		if (requisicaoItemVO == null) {
			requisicaoItemVO = new RequisicaoItemVO();
		}
		return requisicaoItemVO;
	}

	public void setRequisicaoItemVO(RequisicaoItemVO requisicaoItemVO) {
		this.requisicaoItemVO = requisicaoItemVO;
	}

	public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO getItemSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			itemSolicitacaoOrcamentoPlanoOrcamentarioVO = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(
			ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.itemSolicitacaoOrcamentoPlanoOrcamentarioVO = itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	}
}