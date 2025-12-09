package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.secretaria.HistoricoGradeVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.comuns.utilitarias.Uteis;

public class HistoricoGradeAnteriorAlteradaVO extends SuperVO implements Serializable {

	private Integer codigo;
	private HistoricoGradeVO historicoGradeVO;
	private MatriculaVO matriculaVO;
	private GradeCurricularVO gradeCurricularVO;
	private DisciplinaVO disciplinaVO;
	private Double frequencia;
	private Double mediaFinal;
	private String situacao;
	private String ano;
	private String semestre;
	private Integer cargaHoraria;
	private Integer cargaHorariaCursada;
	private String instituicao;
	private CidadeVO cidadeVO;
	private Date dataAlteracao;
	private UsuarioVO responsavel;
	private String acao;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private String cssAcao;
	private TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO;
	private static final long serialVersionUID = 1L;
	private Boolean isentarMediaFinal;
	private HistoricoVO historicoVO;

	public HistoricoGradeAnteriorAlteradaVO() {
		super();
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

	public GradeCurricularVO getGradeCurricularVO() {
		if (gradeCurricularVO == null) {
			gradeCurricularVO = new GradeCurricularVO();
		}
		return gradeCurricularVO;
	}

	public void setGradeCurricularVO(GradeCurricularVO gradeCurricularVO) {
		this.gradeCurricularVO = gradeCurricularVO;
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

	public Double getFrequencia() {
		if (frequencia == null) {
			frequencia = 0.0;
		}
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public Double getMediaFinal() {
		if (mediaFinal == null) {
			mediaFinal = 0.0;
		}
		return mediaFinal;
	}

	public void setMediaFinal(Double mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public Integer getCargaHorariaCursada() {
		if (cargaHorariaCursada == null) {
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	public String getInstituicao() {
		if (instituicao == null) {
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public CidadeVO getCidadeVO() {
		if (cidadeVO == null) {
			cidadeVO = new CidadeVO();
		}
		return cidadeVO;
	}

	public void setCidadeVO(CidadeVO cidadeVO) {
		this.cidadeVO = cidadeVO;
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

	public HistoricoGradeVO getHistoricoGradeVO() {
		if (historicoGradeVO == null) {
			historicoGradeVO = new HistoricoGradeVO();
		}
		return historicoGradeVO;
	}

	public void setHistoricoGradeVO(HistoricoGradeVO historicoGradeVO) {
		this.historicoGradeVO = historicoGradeVO;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public String getAcao() {
		if (acao == null) {
			acao = "NENHUMA";
		}
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	
	public String getCssAcao() {
		if (cssAcao == null) {
			cssAcao = "";
		}
		return cssAcao;
	}

	public void setCssAcao(String cssAcao) {
		this.cssAcao = cssAcao;
	}

	public Boolean getApresentarBotaoRemover() {
    	return getHistoricoGradeVO().getCodigo().equals(0);
    }
	
	public TransferenciaMatrizCurricularVO getTransferenciaMatrizCurricularVO() {
		if (transferenciaMatrizCurricularVO == null) {
			transferenciaMatrizCurricularVO = new TransferenciaMatrizCurricularVO();
		}
		return transferenciaMatrizCurricularVO;
	}

	public void setTransferenciaMatrizCurricularVO(TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO) {
		this.transferenciaMatrizCurricularVO = transferenciaMatrizCurricularVO;
	}

	public List getTipoConsultaComboAcao() {
		List itens = new ArrayList(0);
		if (Uteis.isAtributoPreenchido(getHistoricoGradeVO()) || Uteis.isAtributoPreenchido(getHistoricoVO())) {
			itens.add(new SelectItem("NENHUMA", "Nenhuma"));
			itens.add(new SelectItem("ALTERACAO", "Alteração"));
			itens.add(new SelectItem("EXCLUSAO", "Exclusão"));
		} else {
			itens.add(new SelectItem("INCLUSAO", "Inclusão"));
		}
		return itens;
	}
	
	public String getNomeDisciplina() {
		return getDisciplinaVO().getNome();
	}

	public Boolean getIsentarMediaFinal() {
		if (isentarMediaFinal == null) {
			isentarMediaFinal = false;
		}
		return isentarMediaFinal;
	}

	public void setIsentarMediaFinal(Boolean isentarMediaFinal) {
		this.isentarMediaFinal = isentarMediaFinal;
	}

	public HistoricoVO getHistoricoVO() {
		if (historicoVO == null) {
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

}
