package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Victor Hugo 10/10/2014
 */
public class AvaliacaoOnlineMatriculaRespostaQuestaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO;
	private OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO;
	public Boolean marcada;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public AvaliacaoOnlineMatriculaQuestaoVO getAvaliacaoOnlineMatriculaQuestaoVO() {
		if (avaliacaoOnlineMatriculaQuestaoVO == null) {
			avaliacaoOnlineMatriculaQuestaoVO = new AvaliacaoOnlineMatriculaQuestaoVO();
		}
		return avaliacaoOnlineMatriculaQuestaoVO;
	}

	public void setAvaliacaoOnlineMatriculaQuestaoVO(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO) {
		this.avaliacaoOnlineMatriculaQuestaoVO = avaliacaoOnlineMatriculaQuestaoVO;
	}

	public OpcaoRespostaQuestaoVO getOpcaoRespostaQuestaoVO() {
		if (opcaoRespostaQuestaoVO == null) {
			opcaoRespostaQuestaoVO = new OpcaoRespostaQuestaoVO();
		}
		return opcaoRespostaQuestaoVO;
	}

	public void setOpcaoRespostaQuestaoVO(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) {
		this.opcaoRespostaQuestaoVO = opcaoRespostaQuestaoVO;
	}

	public Boolean getMarcada() {
		if (marcada == null) {
			marcada = false;
		}
		return marcada;
	}

	public void setMarcada(Boolean marcada) {
		this.marcada = marcada;
	}

//	public Integer getSomarQuestoesMarcadas() {
//		if (getMarcada() && getAvaliacaoOnlineMatriculaQuestaoVO().getQuestaoVO().getTipoQuestaoEnum().equals(TipoQuestaoEnum.UNICA_ESCOLHA)) {
//			return avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getQuantidadeQuestaoMarcadas() + 1;
//		} else if (getMarcada() == false && getAvaliacaoOnlineMatriculaQuestaoVO().getQuestaoVO().getTipoQuestaoEnum().equals(TipoQuestaoEnum.UNICA_ESCOLHA)) {
//			return avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getQuantidadeQuestaoMarcadas() - 1;
//		} else {
//			for (AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO : avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaRespostaQuestaoVOs()) {
//				if (getMarcada()) {
//					return avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getQuantidadeQuestaoMarcadas();
//				} else {
//					return avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaVO().getQuantidadeQuestaoMarcadas() + 1;
//				}
//			}
//		}
//		return 0;
//	}
}
