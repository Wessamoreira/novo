package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;

import negocio.comuns.arquitetura.SuperVO;

public class ProgressaoSalarialItemVO extends SuperVO {

	private static final long serialVersionUID = -8912760322205857171L;

	private Integer codigo;
	private ProgressaoSalarialVO progressaoSalarialVO;
	private NivelSalarialVO nivelSalarialVO;
	private FaixaSalarialVO faixaSalarialVO;
	private BigDecimal valor;

	// Transiente
	private Boolean itemEdicao;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}

	public ProgressaoSalarialVO getProgressaoSalarialVO() {
		if (progressaoSalarialVO == null)
			progressaoSalarialVO = new ProgressaoSalarialVO();
		return progressaoSalarialVO;
	}

	public NivelSalarialVO getNivelSalarialVO() {
		if (nivelSalarialVO == null)
			nivelSalarialVO = new NivelSalarialVO();
		return nivelSalarialVO;
	}

	public FaixaSalarialVO getFaixaSalarialVO() {
		if (faixaSalarialVO == null)
			faixaSalarialVO = new FaixaSalarialVO();
		return faixaSalarialVO;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setProgressaoSalarialVO(ProgressaoSalarialVO progressaoSalarialVO) {
		this.progressaoSalarialVO = progressaoSalarialVO;
	}

	public void setNivelSalarialVO(NivelSalarialVO nivelSalarialVO) {
		this.nivelSalarialVO = nivelSalarialVO;
	}

	public void setFaixaSalarialVO(FaixaSalarialVO faixaSalarialVO) {
		this.faixaSalarialVO = faixaSalarialVO;
	}

	public BigDecimal getValor() {
		if (valor == null)
			valor = new BigDecimal(0);
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getItemEdicao() {
		if (itemEdicao == null) {
			itemEdicao = Boolean.FALSE;
		}
		return itemEdicao;
	}

	public void setItemEdicao(Boolean itemEdicao) {
		this.itemEdicao = itemEdicao;
	}

}