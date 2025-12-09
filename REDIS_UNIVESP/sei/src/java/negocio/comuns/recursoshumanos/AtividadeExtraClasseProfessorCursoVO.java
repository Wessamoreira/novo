package negocio.comuns.recursoshumanos;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class AtividadeExtraClasseProfessorCursoVO extends SuperVO {

	private static final long serialVersionUID = 7096318222297760084L;

	private Integer codigo;
	private Integer horaPrevista;
	private CursoVO cursoVO;
	private AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO;

	// Transiente
	private Integer totalHorasIndeferidas;
	private Integer totalHorasAprovadas;
	private Integer totalHorasAguardandoAprovacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getHoraPrevista() {
		if (horaPrevista == null) {
			horaPrevista = 0;
		}
		return horaPrevista;
	}

	public void setHoraPrevista(Integer horaPrevista) {
		this.horaPrevista = horaPrevista;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public AtividadeExtraClasseProfessorVO getAtividadeExtraClasseProfessorVO() {
		if (atividadeExtraClasseProfessorVO == null) {
			atividadeExtraClasseProfessorVO = new AtividadeExtraClasseProfessorVO();
		}
		return atividadeExtraClasseProfessorVO;
	}

	public void setAtividadeExtraClasseProfessorVO(AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) {
		this.atividadeExtraClasseProfessorVO = atividadeExtraClasseProfessorVO;
	}

	public Integer getTotalHorasIndeferidas() {
		if (totalHorasIndeferidas == null) {
			totalHorasIndeferidas = 0;
		}
		return totalHorasIndeferidas;
	}

	public void setTotalHorasIndeferidas(Integer totalHorasIndeferidas) {
		this.totalHorasIndeferidas = totalHorasIndeferidas;
	}

	public Integer getTotalHorasAprovadas() {
		if (totalHorasAprovadas == null) {
			totalHorasAprovadas = 0;
		}
		return totalHorasAprovadas;
	}

	public void setTotalHorasAprovadas(Integer totalHorasAprovadas) {
		this.totalHorasAprovadas = totalHorasAprovadas;
	}

	public Integer getTotalHorasAguardandoAprovacao() {
		if (totalHorasAguardandoAprovacao == null) {
			totalHorasAguardandoAprovacao = 0;
		}
		return totalHorasAguardandoAprovacao;
	}

	public void setTotalHorasAguardandoAprovacao(Integer totalHorasAguardandoAprovacao) {
		this.totalHorasAguardandoAprovacao = totalHorasAguardandoAprovacao;
	}
}