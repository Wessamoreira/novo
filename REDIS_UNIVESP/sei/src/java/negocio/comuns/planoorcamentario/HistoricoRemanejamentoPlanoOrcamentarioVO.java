package negocio.comuns.planoorcamentario;

import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;

/**
 * Reponsável por manter os dados da entidade ItensProvisao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class HistoricoRemanejamentoPlanoOrcamentarioVO extends SuperVO {

	private static final long serialVersionUID = 5619065151265536938L;

	private Integer codigo;
	private Date data;
	private BigDecimal valor;
	private UsuarioVO usuarioVO;
	private CategoriaDespesaVO categoriaDespesaVO;
	private SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO;
	private ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO;
	private String motivo;
	private BigDecimal valorRemanejado;
	private CategoriaDespesaVO categoriaDespesaRemanejado;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		if (valor == null) {
			valor = BigDecimal.ZERO;
		}
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public SolicitacaoOrcamentoPlanoOrcamentarioVO getSolicitacaoOrcamentoPlanoOrcamentarioVO() {
		if (solicitacaoOrcamentoPlanoOrcamentarioVO == null) {
			solicitacaoOrcamentoPlanoOrcamentarioVO = new SolicitacaoOrcamentoPlanoOrcamentarioVO();
		}
		return solicitacaoOrcamentoPlanoOrcamentarioVO;
	}

	public void setSolicitacaoOrcamentoPlanoOrcamentarioVO(
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO) {
		this.solicitacaoOrcamentoPlanoOrcamentarioVO = solicitacaoOrcamentoPlanoOrcamentarioVO;
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

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public BigDecimal getValorRemanejado() {
		if (valorRemanejado == null) {
			valorRemanejado = BigDecimal.ZERO;
		}
		return valorRemanejado;
	}

	public void setValorRemanejado(BigDecimal valorRemanejado) {
		this.valorRemanejado = valorRemanejado;
	}

	public CategoriaDespesaVO getCategoriaDespesaRemanejado() {
		if (categoriaDespesaRemanejado == null) {
			categoriaDespesaRemanejado = new CategoriaDespesaVO();
		}
		return categoriaDespesaRemanejado;
	}

	public void setCategoriaDespesaRemanejado(CategoriaDespesaVO categoriaDespesaRemanejado) {
		this.categoriaDespesaRemanejado = categoriaDespesaRemanejado;
	}
}
