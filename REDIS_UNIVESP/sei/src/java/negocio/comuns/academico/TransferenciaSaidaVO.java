package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade TransferenciaSaida. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TransferenciaSaidaVO extends SuperVO {

	private Integer codigo;
	private Date data;
	private String descricao;	
	private RequerimentoVO codigoRequerimento;
	private String instituicaoDestino;
	private String cursoDestino;
	private String justiticativa;
	private String tipoJustificativa;
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
	private List<HistoricoVO> historicoVOs;
	private String motivoEstorno;
	private UsuarioVO responsavelEstorno;
	private Date dataEstorno;
	private Boolean estornado;
	private String turma;
	
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>TransferenciaSaida</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public TransferenciaSaidaVO() {
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
		setInstituicaoDestino("");
		setCursoDestino("");
		setJustiticativa("");
		setTipoJustificativa("");
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>TransferenciaSaida</code>).
	 */
	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return (responsavelAutorizacao);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>TransferenciaSaida</code>).
	 */
	public void setResponsavelAutorizacao(UsuarioVO obj) {
		this.responsavelAutorizacao = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>TransferenciaSaida</code>).
	 */
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return (matricula);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>TransferenciaSaida</code>).
	 */
	public void setMatricula(MatriculaVO obj) {
		this.matricula = obj;
	}

	public String getTipoJustificativa() {
		if (tipoJustificativa == null) {
			tipoJustificativa = "";
		}
		return (tipoJustificativa);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	public String getTipoJustificativa_Apresentar() {
		if (getTipoJustificativa().equals("FT")) {
			return "Falta Tempo";
		}
		if (getTipoJustificativa().equals("FI")) {
			return "Infra-estrutura Fraca";
		}
		if (getTipoJustificativa().equals("BA")) {
			return "Baixa Qualidade Acadêmica";
		}
		if (getTipoJustificativa().equals("DD")) {
			return "Deficiência Administração";
		}
		if (getTipoJustificativa().equals("DC")) {
			return "Desmotivação Carreira";
		}
		if (getTipoJustificativa().equals("DA")) {
			return "Deficiência Atendimento";
		}
		if (getTipoJustificativa().equals("IP")) {
			return "Insatisfação Professores";
		}
		if (getTipoJustificativa().equals("DF")) {
			return "Dificuldade Financeira";
		}
		if (getTipoJustificativa().equals("OU")) {
			return "Outros";
		}
		return (getTipoJustificativa());
	}

	public void setTipoJustificativa(String tipoJustificativa) {
		this.tipoJustificativa = tipoJustificativa;
	}

	public String getJustiticativa() {
		return (justiticativa);
	}

	public void setJustiticativa(String justiticativa) {
		this.justiticativa = justiticativa;
	}

	public String getCursoDestino() {
		return (cursoDestino);
	}

	public void setCursoDestino(String cursoDestino) {
		this.cursoDestino = cursoDestino;
	}

	public String getInstituicaoDestino() {
		return (instituicaoDestino);
	}

	public void setInstituicaoDestino(String instituicaoDestino) {
		this.instituicaoDestino = instituicaoDestino;
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
	

	public String getMotivoEstorno() {
		if(motivoEstorno == null){
			motivoEstorno = "";
		}
		return motivoEstorno;
	}

	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = motivoEstorno;
	}

	public UsuarioVO getResponsavelEstorno() {
		if(responsavelEstorno == null){
			responsavelEstorno = new UsuarioVO();
		}
		return responsavelEstorno;
	}

	public void setResponsavelEstorno(UsuarioVO responsavelEstorno) {
		this.responsavelEstorno = responsavelEstorno;
	}

	public Date getDataEstorno() {
		if(dataEstorno == null){
			dataEstorno = new Date();
		}
		return dataEstorno;
	}

	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}

	public Boolean getEstornado() {
		if(estornado == null){
			estornado = false;
		}
		return estornado;
	}

	public void setEstornado(Boolean estornado) {
		this.estornado = estornado;
	}
	
	public String getJustificativa(){
		return getJustiticativa();
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
}
