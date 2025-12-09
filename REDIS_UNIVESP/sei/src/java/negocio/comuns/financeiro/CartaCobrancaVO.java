package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public class CartaCobrancaVO extends SuperVO {

	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private String aluno;
	private String matricula;
	private Date dataGeracao;
	private Date dataInicioFiltro;
	private Date dataFimFiltro;
	private UsuarioVO usuarioVO;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;
	private String centroReceitaApresentar;
	private Boolean editar;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getAluno() {
		if (aluno == null) {
			aluno = "";
		}
		return aluno;
	}
	public void setAluno(String aluno) {
		this.aluno = aluno;
	}
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	public void setmatricula(String matricula) {
		this.matricula = matricula;
	}
	public Date getDataGeracao() {
		return dataGeracao;
	}
	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}
	public Date getDataInicioFiltro() {
		return dataInicioFiltro;
	}
	public void setDataInicioFiltro(Date dataInicioFiltro) {
		this.dataInicioFiltro = dataInicioFiltro;
	}
	public Date getDataFimFiltro() {
		return dataFimFiltro;
	}
	public void setDataFimFiltro(Date dataFimFiltro) {
		this.dataFimFiltro = dataFimFiltro;
	}

	public String getDataGeracao_Apresentar() {
		if (dataGeracao == null) {
			return "";
		}
		return (Uteis.getData(dataGeracao));
	}

	public String getDataInicioFiltro_Apresentar() {
		if (dataInicioFiltro == null) {
			return "";
		}
		return (Uteis.getData(dataInicioFiltro));
	}
	public String getDataFimFiltro_Apresentar() {
		if (dataFimFiltro == null) {
			return "";
		}
		return (Uteis.getData(dataFimFiltro));
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}
	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}
	public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
		if (filtroRelatorioFinanceiroVO == null) {
			filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(false);
		}
		return filtroRelatorioFinanceiroVO;
	}
	public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
		this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
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
	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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
	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
	
	public String getCentroReceitaApresentar() {
		if (centroReceitaApresentar == null) {
			centroReceitaApresentar = "";
		}
		return centroReceitaApresentar;
	}
	
	public void setCentroReceitaApresentar(String centroReceita) {
		this.centroReceitaApresentar = centroReceita;
	}
	
	public Boolean getEditar() {
		if (editar == null) {
			editar = Boolean.TRUE;
		}
		return editar;
	}
	
	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

}
