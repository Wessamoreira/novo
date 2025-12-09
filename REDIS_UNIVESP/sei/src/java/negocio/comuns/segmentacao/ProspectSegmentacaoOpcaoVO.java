package negocio.comuns.segmentacao;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.ProspectsVO;

public class ProspectSegmentacaoOpcaoVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private Long codigo;

	private SegmentacaoOpcaoVO segmentacaoOpcao;

	private ProspectsVO prospect;

	public Long getCodigo() {

		if (codigo == null) {
			codigo = 0l;
		}

		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public SegmentacaoOpcaoVO getSegmentacaoOpcao() {

		if (segmentacaoOpcao == null) {
			segmentacaoOpcao = new SegmentacaoOpcaoVO();
		}

		return segmentacaoOpcao;
	}

	public void setSegmentacaoOpcao(SegmentacaoOpcaoVO segmentacaoOpcao) {
		this.segmentacaoOpcao = segmentacaoOpcao;
	}

	public ProspectsVO getProspect() {

		if (prospect == null) {
			prospect = new ProspectsVO();
		}

		return prospect;
	}

	public void setProspect(ProspectsVO prospect) {
		this.prospect = prospect;
	}

}
