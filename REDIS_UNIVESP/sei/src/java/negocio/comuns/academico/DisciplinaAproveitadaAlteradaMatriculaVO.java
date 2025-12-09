package negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.academico.enumeradores.TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

public class DisciplinaAproveitadaAlteradaMatriculaVO extends SuperVO implements Serializable {

	private Integer codigo;
	private MatriculaVO matriculaVO;
	private GradeCurricularVO gradeCurricularVO;
	private DisciplinaVO disciplinaVO;
	private String situacao;
	private Double media;
	private Double frequencia;
	private Integer cargaHoraria;
	private String ano;
	private String semestre;
	private CidadeVO cidadeVO;
	private String instituicao;
	private Integer codigoOrigem;
	private TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum tipoOrigemDisciplinaAproveitadaAlteradaMatricula;
	private Date dataAlteracao;
	private UsuarioVO responsavel;
	private String acao;
	private Integer codigoHistorico;
	private Integer codigoDisciplinaForaGrade;
	private String cssAcao;
	private Integer cargaHorariaCursada;
	private Boolean isentarMediaFinal;
	private String tipoHistorico;
    private Boolean utilizaNotaConceito;
    private String mediaFinalConceito;
    private Boolean apresentarAprovadoHistorico;
    private String nomeProfessor;
	private String titulacaoProfessor;
	private String sexoProfessor;
	private Date dataInicioAula;
	private Date dataFimAula;
	private static final long serialVersionUID = 1L;

	public DisciplinaAproveitadaAlteradaMatriculaVO() {
		super();
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

	public Double getMedia() {
		if (media == null) {
			media = 0.0;
		}
		return media;
	}

	public void setMedia(Double media) {
		this.media = media;
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

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
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

	public CidadeVO getCidadeVO() {
		if (cidadeVO == null) {
			cidadeVO = new CidadeVO();
		}
		return cidadeVO;
	}

	public void setCidadeVO(CidadeVO cidadeVO) {
		this.cidadeVO = cidadeVO;
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

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum getTipoOrigemDisciplinaAproveitadaAlteradaMatricula() {
		if (tipoOrigemDisciplinaAproveitadaAlteradaMatricula == null) {
			tipoOrigemDisciplinaAproveitadaAlteradaMatricula = TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.APROVEITAMENTO_DISCIPLINA;
		}
		return tipoOrigemDisciplinaAproveitadaAlteradaMatricula;
	}

	public void setTipoOrigemDisciplinaAproveitadaAlteradaMatricula(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum tipoOrigemDisciplinaAproveitadaAlteradaMatricula) {
		this.tipoOrigemDisciplinaAproveitadaAlteradaMatricula = tipoOrigemDisciplinaAproveitadaAlteradaMatricula;
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

	public Integer getCodigoHistorico() {
		if (codigoHistorico == null) {
			codigoHistorico = 0;
		}
		return codigoHistorico;
	}

	public void setCodigoHistorico(Integer codigoHistorico) {
		this.codigoHistorico = codigoHistorico;
	}

	public Integer getCodigoDisciplinaForaGrade() {
		if (codigoDisciplinaForaGrade == null) {
			codigoDisciplinaForaGrade = 0;
		}
		return codigoDisciplinaForaGrade;
	}

	public void setCodigoDisciplinaForaGrade(Integer codigoDisciplinaForaGrade) {
		this.codigoDisciplinaForaGrade = codigoDisciplinaForaGrade;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}
	
	public String getSituacao_Apresentar() {
        return SituacaoHistorico.getDescricao(situacao);
    }
	
	public Boolean getIsPermitirAlterarSituacao() {
		return getTipoOrigemDisciplinaAproveitadaAlteradaMatricula().equals(TipoOrigemDisciplinaAproveitadaAlteradaMatriculaEnum.DISCIPLINA_FORA_GRADE);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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

	public Integer getCargaHorariaCursada() {
		if (cargaHorariaCursada == null) {
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
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

	public String getTipoHistorico() {
		if (tipoHistorico == null) {
			tipoHistorico = "";
		}
		return tipoHistorico;
	}

	public void setTipoHistorico(String tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	public Boolean getUtilizaNotaConceito() {
		if (utilizaNotaConceito == null) {
			utilizaNotaConceito = false;
		}
		return utilizaNotaConceito;
	}

	public void setUtilizaNotaConceito(Boolean utilizaNotaConceito) {
		this.utilizaNotaConceito = utilizaNotaConceito;
	}

	public String getMediaFinalConceito() {
		if (mediaFinalConceito == null) {
			mediaFinalConceito = "";
		}
		return mediaFinalConceito;
	}

	public void setMediaFinalConceito(String mediaFinalConceito) {
		this.mediaFinalConceito = mediaFinalConceito;
	}

	public Boolean getApresentarAprovadoHistorico() {
		if(apresentarAprovadoHistorico == null){
			apresentarAprovadoHistorico = false;
		}
		return apresentarAprovadoHistorico;
	}

	public void setApresentarAprovadoHistorico(Boolean apresentarAprovadoHistorico) {
		this.apresentarAprovadoHistorico = apresentarAprovadoHistorico;
	}

	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}

	public String getTitulacaoProfessor() {
		if (titulacaoProfessor == null) {
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}

	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}

	public String getSexoProfessor() {
		if (sexoProfessor == null) {
			sexoProfessor = "";
		}
		return sexoProfessor;
	}

	public void setSexoProfessor(String sexoProfessor) {
		this.sexoProfessor = sexoProfessor;
	}

	public Date getDataInicioAula() {
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}

	public Date getDataFimAula() {
		return dataFimAula;
	}

	public void setDataFimAula(Date dataFimAula) {
		this.dataFimAula = dataFimAula;
	}
	
	
}

