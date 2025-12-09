package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class TurmaDisciplinaNotaTituloVO extends SuperVO {

	private Integer codigo;
	private TipoNotaConceitoEnum nota;
	private String titulo;
	private String tituloOriginal;
	private String tituloConfiguracao;
	private String variavelConfiguracao;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private Date data;
	private UsuarioVO usuarioVO;
	/**
	 * usando para turmas agrupdas, pois precisa replicar a informação para a turma base do aluno
	 */
	private List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs;
	
	private Boolean possuiFormula;
	private String formula;
	/*
	 * transiente
	 */
	private List<TurmaDisciplinaNotaParcialVO> turmaDisciplinaNotaParcialVOs;

	public TurmaDisciplinaNotaTituloVO() {
		super();		
		getTituloOriginal();
	}

	public TurmaDisciplinaNotaTituloVO(TipoNotaConceitoEnum nota, TurmaVO turmaVO, DisciplinaVO disciplinaVO,
			String ano, String semestre, ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		super();
		this.nota = nota;
		this.turmaVO = turmaVO;
		this.disciplinaVO = disciplinaVO;
		this.ano = ano;
		this.semestre = semestre;
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
		getTituloOriginal();
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoNotaConceitoEnum getNota() {
		if (nota == null) {
			nota = TipoNotaConceitoEnum.NOTA_1;
		}
		return nota;
	}

	public void setNota(TipoNotaConceitoEnum nota) {
		this.nota = nota;
	}

	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public UsuarioVO getUsuarioVO() {
		if(usuarioVO == null){
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public Date getData() {
		if(data == null){
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((configuracaoAcademicoVO == null) ? 0 : configuracaoAcademicoVO.getCodigo().hashCode());
		result = prime * result + ((disciplinaVO == null) ? 0 : disciplinaVO.getCodigo().hashCode());
		result = prime * result + ((nota == null) ? 0 : nota.hashCode());
		result = prime * result + ((semestre == null) ? 0 : semestre.hashCode());
		result = prime * result + ((turmaVO == null) ? 0 : turmaVO.getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TurmaDisciplinaNotaTituloVO other = (TurmaDisciplinaNotaTituloVO) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (configuracaoAcademicoVO == null) {
			if (other.configuracaoAcademicoVO != null)
				return false;
		} else if (!configuracaoAcademicoVO.getCodigo().equals(other.configuracaoAcademicoVO.getCodigo()))
			return false;
		if (disciplinaVO == null) {
			if (other.disciplinaVO != null)
				return false;
		} else if (!disciplinaVO.getCodigo().equals(other.disciplinaVO.getCodigo()))
			return false;
		if (nota != other.nota)
			return false;
		if (semestre == null) {
			if (other.semestre != null)
				return false;
		} else if (!semestre.equals(other.semestre))
			return false;
		if (turmaVO == null) {
			if (other.turmaVO != null)
				return false;
		} else if (!turmaVO.getCodigo().equals(other.turmaVO.getCodigo()))
			return false;
		return true;
	}

	public List<TurmaDisciplinaNotaTituloVO> getTurmaDisciplinaNotaTituloVOs() {
		if(turmaDisciplinaNotaTituloVOs == null){
			turmaDisciplinaNotaTituloVOs = new ArrayList<TurmaDisciplinaNotaTituloVO>(0);
		}
		return turmaDisciplinaNotaTituloVOs;
	}

	public void setTurmaDisciplinaNotaTituloVOs(List<TurmaDisciplinaNotaTituloVO> turmaDisciplinaNotaTituloVOs) {
		this.turmaDisciplinaNotaTituloVOs = turmaDisciplinaNotaTituloVOs;
	}
	
	

	public String getTituloConfiguracao() {
		if(tituloConfiguracao == null){
			tituloConfiguracao = "";
		}
		return tituloConfiguracao;
	}

	public void setTituloConfiguracao(String tituloConfiguracao) {
		this.tituloConfiguracao = tituloConfiguracao;
	}

	public String getTituloNotaApresentar(){
		if(!getTitulo().trim().isEmpty()){
			return getTitulo();
		}
		return getTituloConfiguracao();
	}

	public String getTituloOriginal() {
		if(tituloOriginal == null){
			tituloOriginal = getTitulo();
		}
		return tituloOriginal;
	}

	public void setTituloOriginal(String tituloOriginal) {
		this.tituloOriginal = tituloOriginal;
	}

	public String getVariavelConfiguracao() {
		if(variavelConfiguracao == null){
			variavelConfiguracao = "";
		}
		return variavelConfiguracao;
	}

	public void setVariavelConfiguracao(String variavelConfiguracao) {
		this.variavelConfiguracao = variavelConfiguracao;
	}

	public Boolean getPossuiFormula() {
		if(possuiFormula == null) {
			possuiFormula = Boolean.FALSE;
		}
		return possuiFormula;
	}

	public void setPossuiFormula(Boolean possuiFormula) {
		this.possuiFormula = possuiFormula;
	}

	public String getFormula() {
		if(formula == null) {
			formula = "";
		}
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public List<TurmaDisciplinaNotaParcialVO> getTurmaDisciplinaNotaParcialVOs() {
		if(turmaDisciplinaNotaParcialVOs == null) {
			turmaDisciplinaNotaParcialVOs = new ArrayList<TurmaDisciplinaNotaParcialVO>(0);
		}
		return turmaDisciplinaNotaParcialVOs;
	}

	public void setTurmaDisciplinaNotaParcialVOs(List<TurmaDisciplinaNotaParcialVO> turmaDisciplinaNotaParcialVOs) {
		this.turmaDisciplinaNotaParcialVOs = turmaDisciplinaNotaParcialVOs;
	}		
	
}
