package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class GradeCurricularTipoAtividadeComplementarVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer cargaHoraria;
	private TipoAtividadeComplementarVO tipoAtividadeComplementarVO;
	private GradeCurricularVO gradeCurricularVO;
	private boolean controlarHoraMaximaPorPeriodoLetivo = false;
	private boolean permiteCadastrarAtividadeParaAluno = true;
	private Integer horaMaximaPorPeriodoLetivo;
	private Integer horasMinimasExigida;

	public GradeCurricularTipoAtividadeComplementarVO() {
		super();
	}

	public Integer getCodigo() {
		if (this.codigo == null) {
			this.codigo = new Integer(0);
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCargaHoraria() {
		if (this.cargaHoraria == null) {
			this.cargaHoraria = new Integer(0);
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public TipoAtividadeComplementarVO getTipoAtividadeComplementarVO() {
		if (this.tipoAtividadeComplementarVO == null) {
			this.tipoAtividadeComplementarVO = new TipoAtividadeComplementarVO();
		}
		return tipoAtividadeComplementarVO;
	}

	public void setTipoAtividadeComplementarVO(TipoAtividadeComplementarVO tipoAtividadeComplementarVO) {
		this.tipoAtividadeComplementarVO = tipoAtividadeComplementarVO;
	}

	public GradeCurricularVO getGradeCurricularVO() {
		if (this.gradeCurricularVO == null) {
			this.gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
	}
	
	public boolean isControlarHoraMaximaPorPeriodoLetivo() {		
		return controlarHoraMaximaPorPeriodoLetivo;
	}

	public void setControlarHoraMaximaPorPeriodoLetivo(boolean controlarHoraMaximaPorPeriodoLetivo) {
		this.controlarHoraMaximaPorPeriodoLetivo = controlarHoraMaximaPorPeriodoLetivo;
	}

	public Integer getHoraMaximaPorPeriodoLetivo() {
		if (this.horaMaximaPorPeriodoLetivo == null) {
			this.horaMaximaPorPeriodoLetivo =0;
		}
		return horaMaximaPorPeriodoLetivo;
	}

	public void setHoraMaximaPorPeriodoLetivo(Integer horaMaximaPorPeriodoLetivo) {
		this.horaMaximaPorPeriodoLetivo = horaMaximaPorPeriodoLetivo;
	}
	
	public boolean isPermiteCadastrarAtividadeParaAluno() {
		return permiteCadastrarAtividadeParaAluno;
	}

	public void setPermiteCadastrarAtividadeParaAluno(boolean permiteCadastrarAtividadeParaAluno) {
		this.permiteCadastrarAtividadeParaAluno = permiteCadastrarAtividadeParaAluno;
	}

	public Integer getHorasMinimasExigida() {
		if (horasMinimasExigida == null) {
			horasMinimasExigida = 0;
		}
		return horasMinimasExigida;
	}

	public void setHorasMinimasExigida(Integer horasMinimasExigida) {
		this.horasMinimasExigida = horasMinimasExigida;
	}
	
	

}
