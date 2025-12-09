package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCancelamentoTrancamentoEnum;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;

/**
 * Reponsável por manter os dados da entidade Trancamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TrancamentoVO extends SuperVO {

	private Integer codigo;
	private Date data;
	private String descricao;
	private RequerimentoVO codigoRequerimento;
	private String justificativa;
	private MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento;
//	private Boolean trancamentoRelativoAbondonoCurso;
	private String situacao;
	private Date dataPossivelRetorno;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Matricula </code>.
	 */
	private MatriculaVO matricula;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private UsuarioVO responsavelAutorizacao;
	private String turma;
	private Date dataEstorno;
	private UsuarioVO responsavelEstorno;
	private String ano;
	private String semestre;
	private MatriculaPeriodoVO matriculaPeriodoVO;	
	public static final long serialVersionUID = 1L;
	/**
	 * Transient
	 */
	private List<HistoricoVO> historicoVOs;
	private String mensagemConfirmacao;
	private MatriculaPeriodoVO ultimaMatriculaPeriodoVO;
	private String tipoTrancamento;
	private boolean validarProcessoMatriculaCalendarioVO = false;

	/**
	 * Construtor padrão da classe <code>Trancamento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public TrancamentoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setData(new Date());
		setDescricao("");
		setJustificativa("");
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Trancamento</code>).
	 */
	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return responsavelAutorizacao;
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>Trancamento</code>).
	 */
	public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
		this.responsavelAutorizacao = responsavelAutorizacao;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>Trancamento</code>).
	 */
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return (matricula);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>Trancamento</code>).
	 */
	public void setMatricula(MatriculaVO obj) {
		this.matricula = obj;
	}

	public String getJustificativa() {
		return (justificativa);
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	// public String getSituacao() {
	// return (situacao);
	// }
	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	// public String getSituacao_Apresentar() {
	// if (situacao.equals("DA")) {
	// return "Deferido - Aguardando Quitação";
	// }
	// if (situacao.equals("DE")) {
	// return "Deferido";
	// }
	// if (situacao.equals("AV")) {
	// return "Em Avaliação";
	// }
	// if (situacao.equals("IN")) {
	// return "Indeferido";
	// }
	//
	// return (situacao);
	// }
	// public void setSituacao(String situacao) {
	// this.situacao = situacao;
	// }
	public String getDescricao() {
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return (data);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public RequerimentoVO getCodigoRequerimento() {
		if (codigoRequerimento == null) {
			codigoRequerimento = new RequerimentoVO();
		}
		return codigoRequerimento;
	}

	public void setCodigoRequerimento(RequerimentoVO codigoRequerimento) {
		this.codigoRequerimento = codigoRequerimento;
	}

	/**
	 * @return the trancamentoRelativoAbondonoCurso
	 */
//	public Boolean getTrancamentoRelativoAbondonoCurso() {
//		return trancamentoRelativoAbondonoCurso;
//	}

	/**
	 * @param trancamentoRelativoAbondonoCurso
	 *            the trancamentoRelativoAbondonoCurso to set
	 */
//	public void setTrancamentoRelativoAbondonoCurso(Boolean trancamentoRelativoAbondonoCurso) {
//		this.trancamentoRelativoAbondonoCurso = trancamentoRelativoAbondonoCurso;
//	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getSituacao_Apresentar() {
		if (getCodigoRequerimento().getCodigo() == 0 && !this.getNovoObj()) {
			getCodigoRequerimento().setSituacao(getSituacao());
		}
		if (!getSituacao().equals("")) {
			SituacaoCancelamentoTrancamentoEnum situacaoTrancamento = SituacaoCancelamentoTrancamentoEnum.getEnum(getSituacao());
			return situacaoTrancamento.getDescricao();
		} else {
			return getCodigoRequerimento().getSituacao_Apresentar();
		}
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Date getDataPossivelRetorno() {
		if (dataPossivelRetorno == null) {
			dataPossivelRetorno = new Date();
		}
		return dataPossivelRetorno;
	}

	public void setDataPossivelRetorno(Date dataPossivelRetorno) {
		this.dataPossivelRetorno = dataPossivelRetorno;
	}

	/**
	 * @return the motivoCancelamentoTrancamento
	 */
	public MotivoCancelamentoTrancamentoVO getMotivoCancelamentoTrancamento() {
		if (motivoCancelamentoTrancamento == null) {
			motivoCancelamentoTrancamento = new MotivoCancelamentoTrancamentoVO();
		}
		return motivoCancelamentoTrancamento;
	}

	/**
	 * @param motivoCancelamentoTrancamento
	 *            the motivoCancelamentoTrancamento to set
	 */
	public void setMotivoCancelamentoTrancamento(MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento) {
		this.motivoCancelamentoTrancamento = motivoCancelamentoTrancamento;
	}

	/**
	 * @return the historicoVOs
	 */
	public List<HistoricoVO> getHistoricoVOs() {
		if (historicoVOs == null) {
			historicoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoVOs;
	}

	/**
	 * @param historicoVOs
	 *            the historicoVOs to set
	 */
	public void setHistoricoVOs(List<HistoricoVO> historicoVOs) {
		this.historicoVOs = historicoVOs;
	}
	
	public Date getDataEstorno() {
		return dataEstorno;
	}

	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}

	public UsuarioVO getResponsavelEstorno() {
		if (responsavelEstorno == null) {
			responsavelEstorno = new UsuarioVO();
		}
		return responsavelEstorno;
	}

	public void setResponsavelEstorno(UsuarioVO responsavelEstorno) {
		this.responsavelEstorno = responsavelEstorno;
	}
	
	public String getDataPossivelRetorno_Apresentar() {
		return (Uteis.getData(dataPossivelRetorno));
	}
	
	public String getAno() {
		if(ano == null){
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if(semestre == null){
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null){
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public String getMensagemConfirmacao() {
		if(mensagemConfirmacao == null){
			mensagemConfirmacao = "";
		}
		return mensagemConfirmacao;
	}

	public void setMensagemConfirmacao(String mensagemConfirmacao) {
		this.mensagemConfirmacao = mensagemConfirmacao;
	}

	public MatriculaPeriodoVO getUltimaMatriculaPeriodoVO() {
		if(ultimaMatriculaPeriodoVO == null){
			ultimaMatriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return ultimaMatriculaPeriodoVO;
	}

	public void setUltimaMatriculaPeriodoVO(MatriculaPeriodoVO ultimaMatriculaPeriodoVO) {
		this.ultimaMatriculaPeriodoVO = ultimaMatriculaPeriodoVO;
	}
	
	

	public boolean isValidarProcessoMatriculaCalendarioVO() {		
		return validarProcessoMatriculaCalendarioVO;
	}

	public void setValidarProcessoMatriculaCalendarioVO(boolean validarProcessoMatriculaCalendarioVO) {
		this.validarProcessoMatriculaCalendarioVO = validarProcessoMatriculaCalendarioVO;
	}

	public String getTipoTrancamento() {
		if(tipoTrancamento == null) {
			tipoTrancamento = "";
		}
		return tipoTrancamento;
	}

	public void setTipoTrancamento(String tipoTrancamento) {
		this.tipoTrancamento = tipoTrancamento;
	}
	
	public String getTipoTrancamento_Apresentar() {
		if (!getTipoTrancamento().equals("")) {
			TipoTrancamentoEnum tipoTrancamentoEnum = TipoTrancamentoEnum.getEnum(getTipoTrancamento());
			return tipoTrancamentoEnum.getDescricao();
		} else {
			return getCodigoRequerimento().getSituacao_Apresentar();
		}
	}
}
