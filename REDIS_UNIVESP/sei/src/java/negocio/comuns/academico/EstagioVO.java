package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.estagio.ConcedenteVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.TipoConcedenteVO;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.estagio.enumeradores.TipoConsultaComboSituacaoAproveitamentoEnum;
import negocio.comuns.estagio.enumeradores.TipoSituacaoAproveitamentoEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author marco tulio
 *
 */
public class EstagioVO extends ConcedenteVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 30849252381058993L;

	private MatriculaVO matriculaVO;
	private ConcedenteVO concedenteVO;
	private GradeCurricularEstagioVO gradeCurricularEstagioVO;
	private DisciplinaVO disciplina;
	private DocumentoAssinadoVO documentoAssinadoVO;
	private GrupoPessoaItemVO grupoPessoaItemVO;
	private TipoEstagioEnum tipoEstagio;
	private SituacaoEstagioEnum situacaoEstagioEnum;
	private SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum;
	private String ano;
	private String semestre;
	private Integer cargaHoraria;
	private Integer cargaHorariaDeferida;
	private Integer cargaHorariaDiaria;
	private Date dataInicioVigencia;
	private Date dataFinalVigencia;
	private Date dataInicioRenovacaoVigencia;
	private Date dataFinalRenovacaoVigencia;
	private Date dataEnvioAssinaturaPendente;
	private Date dataEnvioAnalise;
	private Date dataLimiteAnalise;
	private Date dataLimiteAnaliseNotificacao;
	private Date dataEnvioCorrecao;
	private Date dataLimiteCorrecao;
	private Date dataLimiteCorrecaoNotificacao;
	private String motivo;
	private List<QuestionarioRespostaOrigemVO> listaQuestionarioRespostaOrigemVO;
	private TipoConsultaComboSituacaoAproveitamentoEnum tipoSituacaoAproveitamentoEnum;
	private UsuarioVO responsavelDeferimento;
	private Date dataDeferimento;
	private UsuarioVO responsavelIndeferimento;
	private Date dataIndeferimento;
	private String rgBeneficiario;
	private String cpfBeneficiario;
	private String nomeBeneficiario;
	private String emailBeneficiario;
	private String telefoneBeneficiario;
	private String cepBeneficiario;
	private String cidadeBeneficiario;
	private String estadoBeneficiario;
	private String numeroBeneficiario;
	private String enderecoBeneficiario;
	private String complementoBeneficiario;
	private String setorBeneficiario;
	private String sqlMensagem;
	private PessoaVO docenteResponsavelEstagio;

	/**
	 * transient
	 */
	
	private QuestionarioRespostaOrigemVO questionarioRespostaOrigemUltimaVersao;
	private boolean existeVersoeEstagio = false;
	private  Boolean selecionado;
	private String nomeSupervisor;
	private String cpfSupervisor;
	
	public void carregarConcedenteVO() {
		this.getConcedenteVO().setTipoConcedenteVO(getTipoConcedenteVO());
		this.getConcedenteVO().setCnpj(getCnpj());
		this.getConcedenteVO().setConcedente(getConcedente());
		this.getConcedenteVO().setTelefone(getTelefone());
		this.getConcedenteVO().setCep(getCep());
		this.getConcedenteVO().setEndereco(getEndereco());
		this.getConcedenteVO().setNumero(getNumero());
		this.getConcedenteVO().setBairro(getBairro());
		this.getConcedenteVO().setComplemento(getComplemento());
		this.getConcedenteVO().setCidade(getCidade());
		this.getConcedenteVO().setResponsavelConcedente(getResponsavelConcedente());
		this.getConcedenteVO().setCpfResponsavelConcedente(getCpfResponsavelConcedente());
		this.getConcedenteVO().setEmailResponsavelConcedente(getEmailResponsavelConcedente());
		this.getConcedenteVO().setTelefoneResponsavelConcedente(getTelefoneResponsavelConcedente());
		this.getConcedenteVO().setCodigoEscolaMEC(getCodigoEscolaMEC()); 
		this.getConcedenteVO().setSituacao(getSituacao());

	}

	public void carregarConcedente(ConcedenteVO obj) {
		setConcedenteVO(obj);
		setTipoConcedenteVO(obj.getTipoConcedenteVO());
		setCnpj(obj.getCnpj());
		setConcedente(obj.getConcedente());
		setTelefone(obj.getTelefone());
		setCep(obj.getCep());
		setEndereco(obj.getEndereco());
		setNumero(obj.getNumero());
		setBairro(obj.getBairro());
		setComplemento(obj.getComplemento());
		setCidade(obj.getCidade());
		setResponsavelConcedente(obj.getResponsavelConcedente());
		setCpfResponsavelConcedente(obj.getCpfResponsavelConcedente());
		setEmailResponsavelConcedente(obj.getEmailResponsavelConcedente());
		setTelefoneResponsavelConcedente(obj.getTelefoneResponsavelConcedente());
		getTipoConcedenteVO().setPermitirCadastroConcedente(obj.getTipoConcedenteVO().getPermitirCadastroConcedente());
		setCodigoEscolaMEC(obj.getCodigoEscolaMEC());

	}

	public void limparConcedente() {
		setConcedenteVO(new ConcedenteVO());
		setTipoConcedenteVO(new TipoConcedenteVO());
		setCnpj("");
		setConcedente("");
		setTelefone("");
		setCep("");
		setEndereco("");
		setNumero("");
		setBairro("");
		setComplemento("");
		setCidade("");
		setResponsavelConcedente("");
		setCpfResponsavelConcedente("");
		setEmailResponsavelConcedente("");
		setTelefoneResponsavelConcedente("");
		getTipoConcedenteVO().setPermitirCadastroConcedente(Boolean.TRUE);

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

	public ConcedenteVO getConcedenteVO() {
		if (concedenteVO == null) {
			concedenteVO = new ConcedenteVO();
		}
		return concedenteVO;
	}

	public void setConcedenteVO(ConcedenteVO concedenteVO) {
		this.concedenteVO = concedenteVO;
	}

	public GradeCurricularEstagioVO getGradeCurricularEstagioVO() {
		if (gradeCurricularEstagioVO == null) {
			gradeCurricularEstagioVO = new GradeCurricularEstagioVO();
		}
		return gradeCurricularEstagioVO;
	}

	public void setGradeCurricularEstagioVO(GradeCurricularEstagioVO gradeCurricularEstagioVO) {
		this.gradeCurricularEstagioVO = gradeCurricularEstagioVO;
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

	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	public String getDataInicioVigencia_Apresentar() {
		if (dataInicioVigencia == null) {
			return "";
		}
		return Uteis.getData(dataInicioVigencia);
	}

	public void setDataInicioVigencia(Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	public Date getDataFinalVigencia() {
		return dataFinalVigencia;
	}

	public String getDataFinalVigencia_Apresentar() {
		if (dataFinalVigencia == null) {
			return "";
		}
		return Uteis.getData(dataFinalVigencia);
	}

	public void setDataFinalVigencia(Date dataFinalVigencia) {
		this.dataFinalVigencia = dataFinalVigencia;
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

	public Integer getCargaHorariaDiaria() {
		if (cargaHorariaDiaria == null) {
			cargaHorariaDiaria = 0;
		}
		return cargaHorariaDiaria;
	}

	public void setCargaHorariaDiaria(Integer cargaHorariaDiaria) {
		this.cargaHorariaDiaria = cargaHorariaDiaria;
	}

	public TipoEstagioEnum getTipoEstagio() {
		if (tipoEstagio == null) {
			tipoEstagio = TipoEstagioEnum.NAO_OBRIGATORIO;
		}
		return tipoEstagio;
	}

	public void setTipoEstagio(TipoEstagioEnum tipoEstagio) {
		this.tipoEstagio = tipoEstagio;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public Date getDataInicioRenovacaoVigencia() {
		return dataInicioRenovacaoVigencia;
	}

	public void setDataInicioRenovacaoVigencia(Date dataInicioRenovacaoVigencia) {
		this.dataInicioRenovacaoVigencia = dataInicioRenovacaoVigencia;
	}

	public Date getDataFinalRenovacaoVigencia() {
		return dataFinalRenovacaoVigencia;
	}

	public void setDataFinalRenovacaoVigencia(Date dataFinalRenovacaoVigencia) {
		this.dataFinalRenovacaoVigencia = dataFinalRenovacaoVigencia;
	}

	public SituacaoEstagioEnum getSituacaoEstagioEnum() {
		if (situacaoEstagioEnum == null) {
			situacaoEstagioEnum = SituacaoEstagioEnum.AGUARDANDO_ASSINATURA;
		}
		return situacaoEstagioEnum;
	}

	public void setSituacaoEstagioEnum(SituacaoEstagioEnum situacaoEstagioEnum) {
		this.situacaoEstagioEnum = situacaoEstagioEnum;
	}

	public SituacaoAdicionalEstagioEnum getSituacaoAdicionalEstagioEnum() {
		if (situacaoAdicionalEstagioEnum == null) {
			situacaoAdicionalEstagioEnum = SituacaoAdicionalEstagioEnum.PENDENTE_SOLICITACAO_ASSINATURA;
		}
		return situacaoAdicionalEstagioEnum;
	}

	public void setSituacaoAdicionalEstagioEnum(SituacaoAdicionalEstagioEnum situacaoAdicionalEstagioEnum) {
		this.situacaoAdicionalEstagioEnum = situacaoAdicionalEstagioEnum;
	}

	public Date getDataEnvioAssinaturaPendente() {		
		return dataEnvioAssinaturaPendente;
	}

	public void setDataEnvioAssinaturaPendente(Date dataEnvioAssinaturaPendente) {
		this.dataEnvioAssinaturaPendente = dataEnvioAssinaturaPendente;
	}

	public Date getDataEnvioAnalise() {
		return dataEnvioAnalise;
	}

	public void setDataEnvioAnalise(Date dataEnvioAnalise) {
		this.dataEnvioAnalise = dataEnvioAnalise;
	}

	public Date getDataLimiteAnalise() {
		return dataLimiteAnalise;
	}

	public String getDataLimiteAnalise_Apresentar() {
		return Uteis.getData(getDataLimiteAnalise());
	}

	public void setDataLimiteAnalise(Date dataLimiteAnalise) {
		this.dataLimiteAnalise = dataLimiteAnalise;
	}

	public Date getDataEnvioCorrecao() {
		return dataEnvioCorrecao;
	}

	public void setDataEnvioCorrecao(Date dataEnvioCorrecao) {
		this.dataEnvioCorrecao = dataEnvioCorrecao;
	}

	public Date getDataLimiteCorrecao() {
		return dataLimiteCorrecao;
	}

	public String getDataLimiteCorrecao_Apresentar() {
		return Uteis.getData(getDataLimiteCorrecao());
	}

	public void setDataLimiteCorrecao(Date dataLimiteCorrecao) {
		this.dataLimiteCorrecao = dataLimiteCorrecao;
	}

	public DocumentoAssinadoVO getDocumentoAssinadoVO() {
		if (documentoAssinadoVO == null) {
			documentoAssinadoVO = new DocumentoAssinadoVO();
		}
		return documentoAssinadoVO;
	}

	public void setDocumentoAssinadoVO(DocumentoAssinadoVO documentoAssinadoVO) {
		this.documentoAssinadoVO = documentoAssinadoVO;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public GrupoPessoaItemVO getGrupoPessoaItemVO() {
		if (grupoPessoaItemVO == null) {
			grupoPessoaItemVO = new GrupoPessoaItemVO();
		}
		return grupoPessoaItemVO;
	}

	public void setGrupoPessoaItemVO(GrupoPessoaItemVO grupoPessoaItemVO) {
		this.grupoPessoaItemVO = grupoPessoaItemVO;
	}

	public List<QuestionarioRespostaOrigemVO> getListaQuestionarioRespostaOrigemVO() {
		if (listaQuestionarioRespostaOrigemVO == null) {
			listaQuestionarioRespostaOrigemVO = new ArrayList<>();
		}
		return listaQuestionarioRespostaOrigemVO;
	}

	public void setListaQuestionarioRespostaOrigemVO(List<QuestionarioRespostaOrigemVO> listaQuestionarioRespostaOrigemVO) {
		this.listaQuestionarioRespostaOrigemVO = listaQuestionarioRespostaOrigemVO;
	}

	public QuestionarioRespostaOrigemVO getQuestionarioRespostaOrigemUltimaVersao() {
		if (questionarioRespostaOrigemUltimaVersao == null) {
			questionarioRespostaOrigemUltimaVersao = new QuestionarioRespostaOrigemVO();
		}
		return questionarioRespostaOrigemUltimaVersao;
	}

	public void setQuestionarioRespostaOrigemUltimaVersao(QuestionarioRespostaOrigemVO questionarioRespostaOrigemUltimaVersao) {
		this.questionarioRespostaOrigemUltimaVersao = questionarioRespostaOrigemUltimaVersao;
	}

	public Integer getCargaHorariaDeferida() {
		if (cargaHorariaDeferida == null) {
			cargaHorariaDeferida = 0;
		}
		return cargaHorariaDeferida;
	}

	public void setCargaHorariaDeferida(Integer cargaHorariaDeferida) {
		this.cargaHorariaDeferida = cargaHorariaDeferida;
	}

	public UsuarioVO getResponsavelDeferimento() {
		if (responsavelDeferimento == null) {
			responsavelDeferimento = new UsuarioVO();
		}
		return responsavelDeferimento;
	}

	public void setResponsavelDeferimento(UsuarioVO responsavelDeferimento) {
		this.responsavelDeferimento = responsavelDeferimento;
	}

	public Date getDataDeferimento() {
		return dataDeferimento;
	}

	public void setDataDeferimento(Date dataDeferimento) {
		this.dataDeferimento = dataDeferimento;
	}

	public UsuarioVO getResponsavelIndeferimento() {
		if (responsavelIndeferimento == null) {
			responsavelIndeferimento = new UsuarioVO();
		}
		return responsavelIndeferimento;
	}

	public void setResponsavelIndeferimento(UsuarioVO responsavelIndeferimento) {
		this.responsavelIndeferimento = responsavelIndeferimento;
	}

	public Date getDataIndeferimento() {
		return dataIndeferimento;
	}

	public void setDataIndeferimento(Date dataIndeferimento) {
		this.dataIndeferimento = dataIndeferimento;
	}

	public boolean isExisteVersoeEstagio() {
		return existeVersoeEstagio;
	}

	public void setExisteVersoeEstagio(boolean existeVersoeEstagio) {
		this.existeVersoeEstagio = existeVersoeEstagio;
	}

	public boolean isApresentarAssinaturaAluno() {
		return getSituacaoEstagioEnum().isAguardandoAssinatura() && getSituacaoAdicionalEstagioEnum().isAssinaturaPendente() && getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getTipoPessoa().isMembroComunidade() && p.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado()) && getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().anyMatch(p -> p.getTipoPessoa().isAluno() && p.getSituacaoDocumentoAssinadoPessoaEnum().isPendente());
	}

	public boolean isApresentarCancelamentoAluno() {
		return (getTipoEstagio().isTipoObrigatorio() && getSituacaoEstagioEnum().isAguardandoAssinatura() && getDocumentoAssinadoVO().getListaDocumentoAssinadoPessoa().stream().noneMatch(p -> p.getSituacaoDocumentoAssinadoPessoaEnum().isAssinado()))
				|| ((getTipoEstagio().isTipoObrigatorioAproveitamento() || getTipoEstagio().isTipoObrigatorioEquivalencia()) && !getSituacaoEstagioEnum().isDeferido() && !getSituacaoEstagioEnum().isIndeferido());
	}

	public TipoConsultaComboSituacaoAproveitamentoEnum getTipoSituacaoAproveitamentoEnum() {
		if (tipoSituacaoAproveitamentoEnum == null) {
			tipoSituacaoAproveitamentoEnum = TipoConsultaComboSituacaoAproveitamentoEnum.NENHUM;
		}
		return tipoSituacaoAproveitamentoEnum;
	}

	public void setTipoSituacaoAproveitamentoEnum(TipoConsultaComboSituacaoAproveitamentoEnum tipoSituacaoAproveitamentoEnum) {
		this.tipoSituacaoAproveitamentoEnum = tipoSituacaoAproveitamentoEnum;
	}

	public String getRgBeneficiario() {
		return rgBeneficiario;
	}

	public void setRgBeneficiario(String rgBeneficiario) {
		this.rgBeneficiario = rgBeneficiario;
	}

	public String getCpfBeneficiario() {
		return cpfBeneficiario;
	}

	public void setCpfBeneficiario(String cpfBeneficiario) {
		this.cpfBeneficiario = cpfBeneficiario;
	}

	public String getNomeBeneficiario() {
		if (nomeBeneficiario == null) {
			nomeBeneficiario = "";
		}
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getEmailBeneficiario() {
		if (emailBeneficiario == null) {
			emailBeneficiario = "";
		}
		return emailBeneficiario;
	}

	public void setEmailBeneficiario(String emailBeneficiario) {
		this.emailBeneficiario = emailBeneficiario;
	}

	public String getTelefoneBeneficiario() {
		if (telefoneBeneficiario == null) {
			telefoneBeneficiario = "";
		}
		return telefoneBeneficiario;
	}

	public void setTelefoneBeneficiario(String telefoneBeneficiario) {
		this.telefoneBeneficiario = telefoneBeneficiario;
	}

	public String getCepBeneficiario() {
		if (cepBeneficiario == null) {
			cepBeneficiario = "";
		}
		return cepBeneficiario;
	}

	public void setCepBeneficiario(String cepBeneficiario) {
		this.cepBeneficiario = cepBeneficiario;
	}

	public String getCidadeBeneficiario() {
		if (cidadeBeneficiario == null) {
			cidadeBeneficiario = "";
		}
		return cidadeBeneficiario;
	}

	public void setCidadeBeneficiario(String cidadeBeneficiario) {
		this.cidadeBeneficiario = cidadeBeneficiario;
	}

	public String getEstadoBeneficiario() {
		if (estadoBeneficiario == null) {
			estadoBeneficiario = "";
		}
		return estadoBeneficiario;
	}

	public void setEstadoBeneficiario(String estadoBeneficiario) {
		this.estadoBeneficiario = estadoBeneficiario;
	}

	public String getNumeroBeneficiario() {
		if (numeroBeneficiario == null) {
			numeroBeneficiario = "";
		}
		return numeroBeneficiario;
	}

	public void setNumeroBeneficiario(String numeroBeneficiario) {
		this.numeroBeneficiario = numeroBeneficiario;
	}

	public String getEnderecoBeneficiario() {
		if (enderecoBeneficiario == null) {
			enderecoBeneficiario = "";
		}
		return enderecoBeneficiario;
	}

	public void setEnderecoBeneficiario(String enderecoBeneficiario) {
		this.enderecoBeneficiario = enderecoBeneficiario;
	}

	public String getComplementoBeneficiario() {
		if (complementoBeneficiario == null) {
			complementoBeneficiario = "";
		}
		return complementoBeneficiario;
	}

	public void setComplementoBeneficiario(String complementoBeneficiario) {
		this.complementoBeneficiario = complementoBeneficiario;
	}

	public String getSetorBeneficiario() {
		if (setorBeneficiario == null) {
			setorBeneficiario = "";
		}
		return setorBeneficiario;
	}

	public void setSetorBeneficiario(String setorBeneficiario) {
		this.setorBeneficiario = setorBeneficiario;
	}

	public Date getDataLimiteAnaliseNotificacao() {
		return dataLimiteAnaliseNotificacao;
	}

	public void setDataLimiteAnaliseNotificacao(Date dataLimiteAnaliseNotificacao) {
		this.dataLimiteAnaliseNotificacao = dataLimiteAnaliseNotificacao;
	}

	public Date getDataLimiteCorrecaoNotificacao() {
		return dataLimiteCorrecaoNotificacao;
	}

	public void setDataLimiteCorrecaoNotificacao(Date dataLimiteCorrecaoNotificacao) {
		this.dataLimiteCorrecaoNotificacao = dataLimiteCorrecaoNotificacao;
	}

	public String getSqlMensagem() {
		if (sqlMensagem == null) {
			sqlMensagem = "";
		}
		return sqlMensagem;
	}

	public void setSqlMensagem(String sqlMensagem) {
		this.sqlMensagem = sqlMensagem;
	}
	
	
	
	public Boolean getSelecionado() {
	if(selecionado == null) {
		selecionado =  false;
	}
	return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	
     public EstagioVO getClonePorNovoEstagioAproveitamento() {
    	 EstagioVO clone = new EstagioVO();
    	 clone = (EstagioVO) Uteis.clonar(this);
    	 clone.setCodigo(0);     	
    	 clone.getListaQuestionarioRespostaOrigemVO().clear();
    	 for (QuestionarioRespostaOrigemVO quesExistente : getListaQuestionarioRespostaOrigemVO()) {
    		  QuestionarioRespostaOrigemVO quesClone = quesExistente.getClonePorEstagioAproveitamento();
    		  quesClone.setEstagioVO(clone); 			
 			  clone.getListaQuestionarioRespostaOrigemVO().add(quesClone);
 		 }   	 
    	 return clone ;
     }

		public PessoaVO getDocenteResponsavelEstagio() {
			if (docenteResponsavelEstagio == null) {
				docenteResponsavelEstagio = new PessoaVO();
			}
			return docenteResponsavelEstagio;
		}

	public void setDocenteResponsavelEstagio(PessoaVO docenteResponsavelEstagio) {
		this.docenteResponsavelEstagio = docenteResponsavelEstagio;
	}
	
	public String getNomeSupervisor() {
		if (nomeSupervisor == null) {
			nomeSupervisor = "";
		}
		return nomeSupervisor;
	}

	public void setNomeSupervisor(String nomeSupervisor) {
		this.nomeSupervisor = nomeSupervisor;
	}

	public String getCpfSupervisor() {
		if (cpfSupervisor == null) {
			cpfSupervisor = "";
		}
		return cpfSupervisor;
	}

	public void setCpfSupervisor(String cpfSupervisor) {
		this.cpfSupervisor = cpfSupervisor;
	}
}