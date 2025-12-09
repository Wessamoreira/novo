package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.arquitetura.SuperVO;

public class OfertaDisciplinaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4608041297272595465L;
	private Integer codigo;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private BimestreEnum periodo;
	private Integer qtdeAlunoMatriculados;
	private String configuracaoAcademicoImportacao;
	private String periodoImportacao;
	private String erroImportacao;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public DisciplinaVO getDisciplinaVO() {
		if(disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	public String getAno() {
		if(ano == null) {
			ano = "";
		}
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		if(semestre == null) {
			semestre = "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if(configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}
	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}
	public BimestreEnum getPeriodo() {
		if(periodo == null) {
			periodo = BimestreEnum.SEMESTRE_1;
		}
		return periodo;
	}
	public void setPeriodo(BimestreEnum periodo) {
		this.periodo = periodo;
	}
	public Integer getQtdeAlunoMatriculados() {
		if(qtdeAlunoMatriculados == null) {
			qtdeAlunoMatriculados =  0;
		}
		return qtdeAlunoMatriculados;
	}
	public void setQtdeAlunoMatriculados(Integer qtdeAlunoMatriculados) {
		this.qtdeAlunoMatriculados = qtdeAlunoMatriculados;
	}
	public String getConfiguracaoAcademicoImportacao() {
		return configuracaoAcademicoImportacao;
	}
	public void setConfiguracaoAcademicoImportacao(String configuracaoAcademicoImportacao) {
		this.configuracaoAcademicoImportacao = configuracaoAcademicoImportacao;
	}
	public String getPeriodoImportacao() {
		return periodoImportacao;
	}
	public void setPeriodoImportacao(String periodoImportacao) {
		this.periodoImportacao = periodoImportacao;
	}
	public String getErroImportacao() {
		if(erroImportacao == null) {
			erroImportacao =  "";
		}		
		return erroImportacao;
	}
	public void setErroImportacao(String erroImportacao) {
		this.erroImportacao = erroImportacao;
	}
	
	

}
