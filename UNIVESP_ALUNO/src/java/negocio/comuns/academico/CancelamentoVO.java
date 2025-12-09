package negocio.comuns.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoCancelamentoTrancamentoEnum;

/**
 * Reponsável por manter os dados da entidade Cancelamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CancelamentoVO extends SuperVO {

	private Integer codigo;
	private Date data;
	private String descricao;
	private String situacao;
	private String justificativa;
	// private String tipoJustificativa;
	private MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento;
	private String titulacaoInstituicao;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Matricula </code>.
	 */
	private MatriculaVO matricula;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Requerimento </code>.
	 */
	private RequerimentoVO codigoRequerimento;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private UsuarioVO responsavelAutorizacao;
	private String turma;
	/**
	 * Transient
	 */
	private List<HistoricoVO> historicoVOs;
	private Date dataEstorno;
	private UsuarioVO responsavelEstorno;
	private Boolean inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional;
	private Boolean inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional;
	private Boolean cancelamentoPorOutraMatriculaOnline;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Cancelamento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public CancelamentoVO() {
		super();
	}

	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return responsavelAutorizacao;
	}

	public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
		this.responsavelAutorizacao = responsavelAutorizacao;
	}

	/**
	 * Retorna o objeto da classe <code>Requerimento</code> relacionado com (
	 * <code>Cancelamento</code>).
	 */
	public RequerimentoVO getCodigoRequerimento() {
		if (codigoRequerimento == null) {
			codigoRequerimento = new RequerimentoVO();
		}
		return (codigoRequerimento);
	}

	/**
	 * Define o objeto da classe <code>Requerimento</code> relacionado com (
	 * <code>Cancelamento</code>).
	 */
	public void setCodigoRequerimento(RequerimentoVO obj) {
		this.codigoRequerimento = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>Cancelamento</code>).
	 */
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return (matricula);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>Cancelamento</code>).
	 */
	public void setMatricula(MatriculaVO obj) {
		this.matricula = obj;
	}

	// public String getTipoJustificativa() {
	// if (tipoJustificativa == null) {
	// tipoJustificativa = TipoJustificativaCancelamento.OUTROS.getValor();
	// }
	// return (tipoJustificativa);
	// }

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	// public String getTipoJustificativa_Apresentar() {
	// return
	// Dominios.getTipoJustificativaAlteracaoMatricula().get(getTipoJustificativa()).toString();
	// }

	// public void setTipoJustificativa(String tipoJustificativa) {
	// this.tipoJustificativa = tipoJustificativa;
	// }

	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return (justificativa);
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 **/
	public String getSituacao_Apresentar() {
		if (getCodigoRequerimento().getCodigo() == 0 && !getNovoObj()) {
			getCodigoRequerimento().setSituacao(getSituacao());
		}
		if (!getSituacao().equals("")) {
			SituacaoCancelamentoTrancamentoEnum situacaoCancelamento = SituacaoCancelamentoTrancamentoEnum.getEnum(getSituacao());
			return situacaoCancelamento.getDescricao();
		} else {
			return getCodigoRequerimento().getSituacao_Apresentar();
		}
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return (situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return (data);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getData(getData()));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public String getTitulacaoInstituicao() {
		if (titulacaoInstituicao == null) {
			titulacaoInstituicao = "Instituição";
		}
		return titulacaoInstituicao;
	}

	public void setTitulacaoInstituicao(String titulacaoInstituicao) {
		this.titulacaoInstituicao = titulacaoInstituicao;
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
			historicoVOs = new java.util.ArrayList<HistoricoVO>(0);
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

	public Boolean getInativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() {
		if(inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional == null ) {
			inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional = Boolean.TRUE;
		}
		return inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional;
	}

	public void setInativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional(
			Boolean inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional) {
		this.inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional = inativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional;
	}

	public Boolean getInativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() {
		if(inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional == null ) {
			inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional = Boolean.TRUE;
		}
		return inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional;
	}

	public void setInativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional(
			Boolean inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional) {
		this.inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional = inativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional;
	}

	public Boolean getCancelamentoPorOutraMatriculaOnline() {
		if(cancelamentoPorOutraMatriculaOnline == null ) {
			cancelamentoPorOutraMatriculaOnline =Boolean.FALSE;
		}
		return cancelamentoPorOutraMatriculaOnline;
	}

	public void setCancelamentoPorOutraMatriculaOnline(Boolean cancelamentoPorOutraMatriculaOnline) {
		this.cancelamentoPorOutraMatriculaOnline = cancelamentoPorOutraMatriculaOnline;
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
}
