package negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.TipoPeriodoLetivoEnum;

/**
 * 
 * @author Manoel
 */
public class PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO extends SuperVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer politicaDivulgacaoMatriculaOnline;
	private String publicoAlvo;
	private UnidadeEnsinoVO unidadeEnsino;
	private Boolean alunoAtivo;
	private Boolean alunoFormado;
	private String nivelEducacional;
	private CursoVO curso;
	private TurnoVO turno;
	private TurmaVO turma;
	private TipoPeriodoLetivoEnum tipoPeriodoLetivoEnum;
	private Integer periodoLetivoDe;
	private Integer periodoLetivoAte;
	private DepartamentoVO departamento;
	private CargoVO cargo;
	private String escolaridade;
	private Date dataCadastro;
	private UsuarioVO usuario;
	private String dataCadastroStr;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getPoliticaDivulgacaoMatriculaOnline() {
		if (politicaDivulgacaoMatriculaOnline == null) {
			politicaDivulgacaoMatriculaOnline = 0;
		}
		return politicaDivulgacaoMatriculaOnline;
	}

	public void setPoliticaDivulgacaoMatriculaOnline(Integer politicaDivulgacaoMatriculaOnline) {
		this.politicaDivulgacaoMatriculaOnline = politicaDivulgacaoMatriculaOnline;
	}

	public String getPublicoAlvo() {
		if (publicoAlvo == null) {
			publicoAlvo = "";
		}
		return publicoAlvo;
	}

	public void setPublicoAlvo(String publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getAlunoAtivo() {
		if (alunoAtivo == null) {
			alunoAtivo = true;
		}
		return alunoAtivo;
	}

	public void setAlunoAtivo(Boolean alunoAtivo) {
		this.alunoAtivo = alunoAtivo;
	}

	public Boolean getAlunoFormado() {
		if (alunoFormado == null) {
			alunoFormado = false;
		}
		return alunoFormado;
	}

	public void setAlunoFormado(Boolean alunoFormado) {
		this.alunoFormado = alunoFormado;
	}

	public String getNivelEducacional() {
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public TurnoVO getTurno() {
		if (turno == null) {
			turno = new TurnoVO();
		}
		return turno;
	}

	public void setTurno(TurnoVO turno) {
		this.turno = turno;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}

	public CargoVO getCargo() {
		if (cargo == null) {
			cargo = new CargoVO();
		}
		return cargo;
	}

	public void setCargo(CargoVO cargo) {
		this.cargo = cargo;
	}

	public String getEscolaridade() {
		if(escolaridade == null) {
			escolaridade = "";
		}
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioVO getUsuario() {
		if (usuario == null) {
			usuario = new UsuarioVO();
		}
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public String getDataCadastroStr() {
		return dataCadastroStr;
	}

	public void setDataCadastroStr(String dataCadastroStr) {
		this.dataCadastroStr = dataCadastroStr;
	}

	public TipoPeriodoLetivoEnum getTipoPeriodoLetivoEnum() {
		if(tipoPeriodoLetivoEnum == null) {
			tipoPeriodoLetivoEnum = TipoPeriodoLetivoEnum.TODOS;
		}
		return tipoPeriodoLetivoEnum;
	}

	public void setTipoPeriodoLetivoEnum(TipoPeriodoLetivoEnum tipoPeriodoLetivoEnum) {
		this.tipoPeriodoLetivoEnum = tipoPeriodoLetivoEnum;
	}

	public Integer getPeriodoLetivoDe() {
		if(periodoLetivoDe == null) {
			periodoLetivoDe = 0;
		}
		return periodoLetivoDe;
	}

	public void setPeriodoLetivoDe(Integer periodoLetivoDe) {
		this.periodoLetivoDe = periodoLetivoDe;
	}

	public Integer getPeriodoLetivoAte() {
		if(periodoLetivoAte == null) {
			periodoLetivoAte = 0;
		}
		return periodoLetivoAte;
	}

	public void setPeriodoLetivoAte(Integer periodoLetivoAte) {
		this.periodoLetivoAte = periodoLetivoAte;
	}
}
