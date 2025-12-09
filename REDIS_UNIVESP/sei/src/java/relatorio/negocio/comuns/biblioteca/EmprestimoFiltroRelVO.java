package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Catalogo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class EmprestimoFiltroRelVO extends SuperVO implements Serializable {

	private Date dataInicio;
	private Date dataFim;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private BibliotecaVO bibliotecaVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private CatalogoVO catalogoVO;
	private TurmaVO turmaVO;
	private String tipoEmprestimo;
	private String situacaoEmprestimo;
	private String tipoPessoa;
	private MatriculaVO matriculaVO;
	private FuncionarioVO professorVO;
	private FuncionarioVO funcionarioVO;
	private PessoaVO pessoa;
	private String ordenarPor;
	private Boolean considerarSubTiposCatalogo;
	

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public BibliotecaVO getBibliotecaVO() {
		if (bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
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

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public CatalogoVO getCatalogoVO() {
		if (catalogoVO == null) {
			catalogoVO = new CatalogoVO();
		}
		return catalogoVO;
	}

	public void setCatalogoVO(CatalogoVO catalogoVO) {
		this.catalogoVO = catalogoVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getTipoEmprestimo() {
		if (tipoEmprestimo == null) {
			tipoEmprestimo = "";
		}
		return tipoEmprestimo;
	}

	public void setTipoEmprestimo(String tipoEmprestimo) {
		this.tipoEmprestimo = tipoEmprestimo;
	}

	public String getSituacaoEmprestimo() {
		if (situacaoEmprestimo == null) {
			situacaoEmprestimo = "";
		}
		return situacaoEmprestimo;
	}

	public void setSituacaoEmprestimo(String situacaoEmprestimo) {
		this.situacaoEmprestimo = situacaoEmprestimo;
	}

	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public FuncionarioVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new FuncionarioVO();
		}
		return professorVO;
	}

	public void setProfessorVO(FuncionarioVO professorVO) {
		this.professorVO = professorVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public String getOrdenarPor() {
		if (ordenarPor == null) {
			ordenarPor = "dataEmprestimo";
		}
		return ordenarPor;
	}

	public void setOrdenarPor(String ordenarPor) {
		this.ordenarPor = ordenarPor;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public Boolean getConsiderarSubTiposCatalogo() {
		if(considerarSubTiposCatalogo == null) {
			return false;
		}
		return considerarSubTiposCatalogo;
	}

	public void setConsiderarSubTiposCatalogo(Boolean considerarSubTiposCatalogo) {
		this.considerarSubTiposCatalogo = considerarSubTiposCatalogo;
	}
	
}