package negocio.comuns.processosel;

import java.util.Objects;

import negocio.comuns.academico.EixoCursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class ProcSeletivoUnidadeEnsinoEixoCursoVO extends SuperVO {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6047411434925857280L;
	private Integer codigo;
	private ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsino;
	private EixoCursoVO eixoCurso;
	private Integer nrVagasEixoCurso;	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public EixoCursoVO getEixoCurso() {
		if (eixoCurso == null) {
			eixoCurso = new EixoCursoVO();
		}
		return eixoCurso;
	}

	public void setEixoCurso(EixoCursoVO eixoCurso) {
		this.eixoCurso = eixoCurso;
	}

	public Integer getNrVagasEixoCurso() {
		if (nrVagasEixoCurso == null) {
			nrVagasEixoCurso = 0;
		}
		return nrVagasEixoCurso;
	}

	public void setNrVagasEixoCurso(Integer nrVagasEixoCurso) {
		this.nrVagasEixoCurso = nrVagasEixoCurso;
	}

	public ProcSeletivoUnidadeEnsinoVO getProcSeletivoUnidadeEnsino() {
		if(procSeletivoUnidadeEnsino == null) {
			procSeletivoUnidadeEnsino = new ProcSeletivoUnidadeEnsinoVO();
		}
		return procSeletivoUnidadeEnsino;
	}

	public void setProcSeletivoUnidadeEnsino(ProcSeletivoUnidadeEnsinoVO procSeletivoUnidadeEnsino) {
		this.procSeletivoUnidadeEnsino = procSeletivoUnidadeEnsino;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eixoCurso);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcSeletivoUnidadeEnsinoEixoCursoVO other = (ProcSeletivoUnidadeEnsinoEixoCursoVO) obj;
		return Objects.equals(eixoCurso, other.eixoCurso);
	}


}