package negocio.comuns.biblioteca;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.EstadoHistoricoExemplar;
import negocio.comuns.utilitarias.dominios.SituacaoHistoricoExemplar;
import negocio.facade.jdbc.biblioteca.Exemplar;

/**
 * Reponsável por manter os dados da entidade HistoricoExemplar. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Exemplar
 */
 public class HistoricoExemplarVO extends SuperVO  {

	private Integer codigo;
	private Integer exemplar;
	private Date data;
	private String situacao;
	private String motivo;
	private String estado;
        public static final long serialVersionUID = 1L;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Usuario </code>.
	 */
	private UsuarioVO responsavel;

	/**
	 * Construtor padrão da classe <code>HistoricoExemplar</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public HistoricoExemplarVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>HistoricoExemplarVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(HistoricoExemplarVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Histórico Exemplar) deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setEstado(getEstado().toUpperCase());
		setSituacao(getSituacao().toUpperCase());
		setMotivo(getMotivo().toUpperCase());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setData(new Date());
		setSituacao("");
		setMotivo("");
	}

	/**
	 * Retorna o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>HistoricoExemplar</code>).
	 */
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>HistoricoExemplar</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return (motivo);
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return (situacao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	public String getSituacao_Apresentar() {
		return SituacaoHistoricoExemplar.getDescricao(situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
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
		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getExemplar() {
		if (exemplar == null) {
			exemplar = 0;
		}
		return (exemplar);
	}

	public void setExemplar(Integer exemplar) {
		this.exemplar = exemplar;
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

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		if (estado == null) {
			estado = "";
		}
		return estado;
	}

	public String getEstado_Apresentar() {
		return EstadoHistoricoExemplar.getDescricao(estado);
	}
}