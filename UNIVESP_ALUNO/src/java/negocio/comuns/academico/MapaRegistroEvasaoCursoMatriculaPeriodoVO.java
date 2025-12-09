package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;
import negocio.comuns.utilitarias.Uteis;

public class MapaRegistroEvasaoCursoMatriculaPeriodoVO extends SuperVO {

	private static final long serialVersionUID = 6346843269082709429L;
	private Integer codigo;
	private MapaRegistroEvasaoCursoVO mapaRegistroEvasaoCursoVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private TrancamentoVO trancamentoVO;
	private CancelamentoVO cancelamentoVO;
	private SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
	private String erro;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MapaRegistroEvasaoCursoVO getMapaRegistroEvasaoCursoVO() {
		if (mapaRegistroEvasaoCursoVO == null) {
			mapaRegistroEvasaoCursoVO = new MapaRegistroEvasaoCursoVO();
		}		
		return mapaRegistroEvasaoCursoVO;
	}

	public void setMapaRegistroEvasaoCursoVO(MapaRegistroEvasaoCursoVO mapaRegistroEvasaoCursoVO) {
		this.mapaRegistroEvasaoCursoVO = mapaRegistroEvasaoCursoVO;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public TrancamentoVO getTrancamentoVO() {
		if (trancamentoVO == null) {
			trancamentoVO = new TrancamentoVO();
		}
		return trancamentoVO;
	}

	public void setTrancamentoVO(TrancamentoVO trancamentoVO) {
		this.trancamentoVO = trancamentoVO;
	}

	public CancelamentoVO getCancelamentoVO() {
		if (cancelamentoVO == null) {
			cancelamentoVO = new CancelamentoVO();
		}
		return cancelamentoVO;
	}

	public void setCancelamentoVO(CancelamentoVO cancelamentoVO) {
		this.cancelamentoVO = cancelamentoVO;
	}

	public SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum() {
		if (situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum == null) {
			situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum = SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
	}

	public void setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum) {
		this.situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum = situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
	}

	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
	
	public boolean isApresentarEstorno() {
		return getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().isProcessado() && Uteis.isAtributoPreenchido(getCancelamentoVO().getCodigo());
	}
	
	

}
