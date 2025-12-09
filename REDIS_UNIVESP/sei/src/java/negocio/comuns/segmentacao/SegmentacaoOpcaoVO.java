package negocio.comuns.segmentacao;

import negocio.comuns.arquitetura.SuperVO;

public class SegmentacaoOpcaoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer segmentacaoprospect;
	private String descricao;
	private SegmentacaoProspectVO segmentacaoProspectVO;
	private Integer quantidade;
	private Double percentual;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getSegmentacaoprospect() {
		if (segmentacaoprospect == null) {
			segmentacaoprospect = 0;
		}
		return segmentacaoprospect;
	}

	public void setSegmentacaoprospect(Integer segmentacaoprospect) {
		this.segmentacaoprospect = segmentacaoprospect;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public SegmentacaoProspectVO getSegmentacaoProspectVO() {
		if (segmentacaoProspectVO == null) {
			segmentacaoProspectVO = new SegmentacaoProspectVO();
		}
		return segmentacaoProspectVO;
	}

	public void setSegmentacaoProspectVO(SegmentacaoProspectVO segmentacaoProspectVO) {
		this.segmentacaoProspectVO = segmentacaoProspectVO;
	}

	public Integer getQuantidade() {
		if (quantidade == null) {
			quantidade = 0;
		}
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SegmentacaoOpcaoVO other = (SegmentacaoOpcaoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public Double getPercentual() {
		if (percentual == null) {
			percentual = 0.0;
		}
		return percentual;
	}

	public void setPercentual(Double percentual) {
		this.percentual = percentual;
	}
}
