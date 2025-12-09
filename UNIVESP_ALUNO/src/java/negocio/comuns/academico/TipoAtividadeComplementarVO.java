package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class TipoAtividadeComplementarVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private TipoAtividadeComplementarVO tipoAtividadeComplementarSuperior;	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Campos transient
	 */
	private Integer cargaHorasPermitidasPeriodoLetivo;
	private Integer cargaHorasJaRealizadaPeriodoLetivo;
	
	public TipoAtividadeComplementarVO(){
		super();
	}
	
	public Integer getCodigo(){
		if(this.codigo == null){
			this.codigo = 0;
		}
		return this.codigo;
	}
	
	public void setCodigo(Integer codigo){
		this.codigo = codigo;
	}
	
	public String getNome(){
		if(this.nome == null){
			this.nome = "";
		}
		return this.nome;
	}
	
	public void setNome(String nome){
		this.nome = nome;
	}
	
	public TipoAtividadeComplementarVO getTipoAtividadeComplementarSuperior() {
		if (tipoAtividadeComplementarSuperior == null) {
			tipoAtividadeComplementarSuperior = new TipoAtividadeComplementarVO();
		}
		return tipoAtividadeComplementarSuperior;
	}

	public void setTipoAtividadeComplementarSuperior(
			TipoAtividadeComplementarVO tipoAtividadeComplementarSuperior) {
		this.tipoAtividadeComplementarSuperior = tipoAtividadeComplementarSuperior;
	}
	
	public Integer getCargaHorasPermitidasPeriodoLetivo() {
		if(this.cargaHorasPermitidasPeriodoLetivo == null){
			this.cargaHorasPermitidasPeriodoLetivo = 0;
		}
		return cargaHorasPermitidasPeriodoLetivo;
	}

	public void setCargaHorasPermitidasPeriodoLetivo(Integer cargaHorasPermitidasPeriodoLetivo) {
		this.cargaHorasPermitidasPeriodoLetivo = cargaHorasPermitidasPeriodoLetivo;
	}

	public Integer getCargaHorasJaRealizadaPeriodoLetivo() {
		if(this.cargaHorasJaRealizadaPeriodoLetivo == null){
			this.cargaHorasJaRealizadaPeriodoLetivo = 0;
		}
		return cargaHorasJaRealizadaPeriodoLetivo;
	}

	public void setCargaHorasJaRealizadaPeriodoLetivo(Integer cargaHorasJaRealizadaPeriodoLetivo) {
		this.cargaHorasJaRealizadaPeriodoLetivo = cargaHorasJaRealizadaPeriodoLetivo;
	}	

}
