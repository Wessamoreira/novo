package relatorio.negocio.comuns.academico;


public class HistoricoAlunoAtividadeComplementarRelVO {

	private String tipoAtividadeComplementar;
	private String cargaHorariaObrigatoriaAtividadeComplementar;
	private String cargaHorariaRealizadaAtividadeComplementar;
	
	public String getTipoAtividadeComplementar() {
		if (tipoAtividadeComplementar == null) {
			tipoAtividadeComplementar = "";
		}
		return tipoAtividadeComplementar;
	}

	public void setTipoAtividadeComplementar(String tipoAtividadeComplementar) {
		this.tipoAtividadeComplementar = tipoAtividadeComplementar;
	}

	public String getCargaHorariaObrigatoriaAtividadeComplementar() {
		if (cargaHorariaObrigatoriaAtividadeComplementar == null) {
			cargaHorariaObrigatoriaAtividadeComplementar = "--";
		}
		return cargaHorariaObrigatoriaAtividadeComplementar;
	}

	public void setCargaHorariaObrigatoriaAtividadeComplementar(String cargaHorariaObrigatoriaAtividadeComplementar) {
		this.cargaHorariaObrigatoriaAtividadeComplementar = cargaHorariaObrigatoriaAtividadeComplementar;
	}

	public String getCargaHorariaRealizadaAtividadeComplementar() {
		if (cargaHorariaRealizadaAtividadeComplementar == null) {
			cargaHorariaRealizadaAtividadeComplementar = "--";
		}
		return cargaHorariaRealizadaAtividadeComplementar;
	}

	public void setCargaHorariaRealizadaAtividadeComplementar(String cargaHorariaRealizadaAtividadeComplementar) {
		this.cargaHorariaRealizadaAtividadeComplementar = cargaHorariaRealizadaAtividadeComplementar;
	}

}
