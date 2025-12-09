package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;


public class MatriculaIntegralizacaoCurricularVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	
	private String  matricula;
	private Integer cargaHorariaTotal;
	private Integer cargaHorariaDisciplinaObrigatorioExigida;
	private Integer cargaHorariaDisciplinaObrigatorioCumprida;
	private Integer cargaHorariaDisciplinaOptativaExigida;
	private Integer quantidadeMinimaDisciplinaOptativaExigida;
	private Integer cargaHorariaDisciplinaOptativaCumprida;
	private Integer quantidadeDisciplinaOptativaCumprida;
	private Integer cargaHorariaEstagioExigido;
	private Integer cargaHorariaEstagioCumprido;
	private Integer cargaHorariaAtividadeComplementarExigido;
	private Integer cargaHorariaAtividadeComplementarCumprido;
	private Integer cargaHorariaCumprido;
	private Integer cargaHorariaPendente;
	private Double  percentualIntegralizado;
	private Double  percentualNaoIntegralizado;
	private Double  percentualPermitirIniciarEstagio;
	private Integer cargaHorariaExigidaLiberarEstagio;
	private Integer cargaHorariaCumpridaLiberarEstagio;
	private Double  percentualCumpridoLiberarEstagio;
	private Double  percentualPendenteLiberarEstagio;
	private Double  percentualPermitirIniciarTcc;
	private Integer cargaHorariaExigidaLiberarTcc;
	private Integer cargaHorariaCumpridaLiberarTcc;
	private Double  percentualCumpridoLiberarTcc;
	private Double  percentualPendenteLiberarTcc;
	
	
	public String getMatricula() {
		if(matricula == null) {
			matricula = Constantes.EMPTY;
		}
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public Integer getCargaHorariaTotal() {
		return cargaHorariaTotal;
	}
	public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}
	public Integer getCargaHorariaDisciplinaObrigatorioExigida() {
		return cargaHorariaDisciplinaObrigatorioExigida;
	}
	public void setCargaHorariaDisciplinaObrigatorioExigida(Integer cargaHorariaDisciplinaObrigatorioExigida) {
		this.cargaHorariaDisciplinaObrigatorioExigida = cargaHorariaDisciplinaObrigatorioExigida;
	}
	public Integer getCargaHorariaDisciplinaObrigatorioCumprida() {
		return cargaHorariaDisciplinaObrigatorioCumprida;
	}
	public void setCargaHorariaDisciplinaObrigatorioCumprida(Integer cargaHorariaDisciplinaObrigatorioCumprida) {
		this.cargaHorariaDisciplinaObrigatorioCumprida = cargaHorariaDisciplinaObrigatorioCumprida;
	}
	public Integer getCargaHorariaDisciplinaOptativaExigida() {
		return cargaHorariaDisciplinaOptativaExigida;
	}
	public void setCargaHorariaDisciplinaOptativaExigida(Integer cargaHorariaDisciplinaOptativaExigida) {
		this.cargaHorariaDisciplinaOptativaExigida = cargaHorariaDisciplinaOptativaExigida;
	}
	public Integer getQuantidadeMinimaDisciplinaOptativaExigida() {
		return quantidadeMinimaDisciplinaOptativaExigida;
	}
	public void setQuantidadeMinimaDisciplinaOptativaExigida(Integer quantidadeMinimaDisciplinaOptativaExigida) {
		this.quantidadeMinimaDisciplinaOptativaExigida = quantidadeMinimaDisciplinaOptativaExigida;
	}
	public Integer getCargaHorariaDisciplinaOptativaCumprida() {
		return cargaHorariaDisciplinaOptativaCumprida;
	}
	public void setCargaHorariaDisciplinaOptativaCumprida(Integer cargaHorariaDisciplinaOptativaCumprida) {
		this.cargaHorariaDisciplinaOptativaCumprida = cargaHorariaDisciplinaOptativaCumprida;
	}
	public Integer getQuantidadeDisciplinaOptativaCumprida() {
		return quantidadeDisciplinaOptativaCumprida;
	}
	public void setQuantidadeDisciplinaOptativaCumprida(Integer quantidadeDisciplinaOptativaCumprida) {
		this.quantidadeDisciplinaOptativaCumprida = quantidadeDisciplinaOptativaCumprida;
	}
	public Integer getCargaHorariaEstagioExigido() {
		return cargaHorariaEstagioExigido;
	}
	public void setCargaHorariaEstagioExigido(Integer cargaHorariaEstagioExigido) {
		this.cargaHorariaEstagioExigido = cargaHorariaEstagioExigido;
	}
	public Integer getCargaHorariaEstagioCumprido() {
		return cargaHorariaEstagioCumprido;
	}
	public void setCargaHorariaEstagioCumprido(Integer cargaHorariaEstagioCumprido) {
		this.cargaHorariaEstagioCumprido = cargaHorariaEstagioCumprido;
	}
	public Integer getCargaHorariaAtividadeComplementarExigido() {
		return cargaHorariaAtividadeComplementarExigido;
	}
	public void setCargaHorariaAtividadeComplementarExigido(Integer cargaHorariaAtividadeComplementarExigido) {
		this.cargaHorariaAtividadeComplementarExigido = cargaHorariaAtividadeComplementarExigido;
	}
	public Integer getCargaHorariaAtividadeComplementarCumprido() {
		return cargaHorariaAtividadeComplementarCumprido;
	}
	public void setCargaHorariaAtividadeComplementarCumprido(Integer cargaHorariaAtividadeComplementarCumprido) {
		this.cargaHorariaAtividadeComplementarCumprido = cargaHorariaAtividadeComplementarCumprido;
	}
	public Integer getCargaHorariaCumprido() {
		return cargaHorariaCumprido;
	}
	public void setCargaHorariaCumprido(Integer cargaHorariaCumprido) {
		this.cargaHorariaCumprido = cargaHorariaCumprido;
	}
	public Integer getCargaHorariaPendente() {
		return cargaHorariaPendente;
	}
	public void setCargaHorariaPendente(Integer cargaHorariaPendente) {
		this.cargaHorariaPendente = cargaHorariaPendente;
	}
	public Double getPercentualIntegralizado() {
		return percentualIntegralizado;
	}
	public void setPercentualIntegralizado(Double percentualIntegralizado) {
		this.percentualIntegralizado = percentualIntegralizado;
	}
	public Double getPercentualNaoIntegralizado() {
		return percentualNaoIntegralizado;
	}
	public void setPercentualNaoIntegralizado(Double percentualNaoIntegralizado) {
		this.percentualNaoIntegralizado = percentualNaoIntegralizado;
	}
	public Double getPercentualPermitirIniciarEstagio() {
		return percentualPermitirIniciarEstagio;
	}
	public void setPercentualPermitirIniciarEstagio(Double percentualPermitirIniciarEstagio) {
		this.percentualPermitirIniciarEstagio = percentualPermitirIniciarEstagio;
	}
	public Integer getCargaHorariaExigidaLiberarEstagio() {
		return cargaHorariaExigidaLiberarEstagio;
	}
	public void setCargaHorariaExigidaLiberarEstagio(Integer cargaHorariaExigidaLiberarEstagio) {
		this.cargaHorariaExigidaLiberarEstagio = cargaHorariaExigidaLiberarEstagio;
	}
	public Integer getCargaHorariaCumpridaLiberarEstagio() {
		return cargaHorariaCumpridaLiberarEstagio;
	}
	public void setCargaHorariaCumpridaLiberarEstagio(Integer cargaHorariaCumpridaLiberarEstagio) {
		this.cargaHorariaCumpridaLiberarEstagio = cargaHorariaCumpridaLiberarEstagio;
	}
	public Double getPercentualCumpridoLiberarEstagio() {
		return percentualCumpridoLiberarEstagio;
	}
	public void setPercentualCumpridoLiberarEstagio(Double percentualCumpridoLiberarEstagio) {
		this.percentualCumpridoLiberarEstagio = percentualCumpridoLiberarEstagio;
	}
	public Double getPercentualPendenteLiberarEstagio() {
		return percentualPendenteLiberarEstagio;
	}
	public void setPercentualPendenteLiberarEstagio(Double percentualPendenteLiberarEstagio) {
		this.percentualPendenteLiberarEstagio = percentualPendenteLiberarEstagio;
	}
	public Double getPercentualPermitirIniciarTcc() {
		return percentualPermitirIniciarTcc;
	}
	public void setPercentualPermitirIniciarTcc(Double percentualPermitirIniciarTcc) {
		this.percentualPermitirIniciarTcc = percentualPermitirIniciarTcc;
	}
	public Integer getCargaHorariaExigidaLiberarTcc() {
		return cargaHorariaExigidaLiberarTcc;
	}
	public void setCargaHorariaExigidaLiberarTcc(Integer cargaHorariaExigidaLiberarTcc) {
		this.cargaHorariaExigidaLiberarTcc = cargaHorariaExigidaLiberarTcc;
	}
	public Integer getCargaHorariaCumpridaLiberarTcc() {
		return cargaHorariaCumpridaLiberarTcc;
	}
	public void setCargaHorariaCumpridaLiberarTcc(Integer cargaHorariaCumpridaLiberarTcc) {
		this.cargaHorariaCumpridaLiberarTcc = cargaHorariaCumpridaLiberarTcc;
	}
	public Double getPercentualCumpridoLiberarTcc() {
		return percentualCumpridoLiberarTcc;
	}
	public void setPercentualCumpridoLiberarTcc(Double percentualCumpridoLiberarTcc) {
		this.percentualCumpridoLiberarTcc = percentualCumpridoLiberarTcc;
	}
	public Double getPercentualPendenteLiberarTcc() {
		return percentualPendenteLiberarTcc;
	}
	public void setPercentualPendenteLiberarTcc(Double percentualPendenteLiberarTcc) {
		this.percentualPendenteLiberarTcc = percentualPendenteLiberarTcc;
	}
		
}
