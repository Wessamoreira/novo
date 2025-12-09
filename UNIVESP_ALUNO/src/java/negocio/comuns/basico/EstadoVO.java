package negocio.comuns.basico;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.RegiaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Estado. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "estado")
public class EstadoVO extends SuperVO {

	private Integer codigo;
	private String sigla;
	private String nome;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Paiz </code>.
	 */
	private PaizVO paiz;
	private Integer codigoInep;
	private RegiaoEnum regiao;
	public static final long serialVersionUID = 1L;
    private String codigoIBGE;

	/**
	 * Construtor padrão da classe <code>Estado</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public EstadoVO() {
		super();
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>EstadoVO</code>.
	 */
	public static void validarUnicidade(List<EstadoVO> lista, EstadoVO obj) throws ConsistirException {

	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>EstadoVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(EstadoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		// if (obj.getSigla().equals("")) {
		// throw new
		// ConsistirException("O campo SIGLA (ESTADO) deve ser informado.");
		// }
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (ESTADO) deve ser informado.");
		}
		if ((obj.getPaiz() == null) || (obj.getPaiz().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo PAÍS (ESTADO) deve ser informado.");
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
		setSigla(getSigla().toUpperCase());
		setNome(getNome().toUpperCase());
	}

	/**
	 * Retorna o objeto da classe <code>Paiz</code> relacionado com (
	 * <code>Estado</code>).
	 */
	@XmlTransient
	public PaizVO getPaiz() {
		if (paiz == null) {
			paiz = new PaizVO();
		}
		return (paiz);
	}

	/**
	 * Define o objeto da classe <code>Paiz</code> relacionado com (
	 * <code>Estado</code>).
	 */
	public void setPaiz(PaizVO obj) {
		this.paiz = obj;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "sigla")
	public String getSigla() {
		if (sigla == null) {
			sigla = "";
		}
		return (sigla);
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlTransient
	public Integer getCodigoInep() {
		if (codigoInep == null) {
			codigoInep = 0;
		}
		return codigoInep;
	}

	public void setCodigoInep(Integer codigoInep) {
		this.codigoInep = codigoInep;
	}

	@XmlTransient
	public RegiaoEnum getRegiao() {
		return regiao;
	}

	public void setRegiao(RegiaoEnum regiao) {
		this.regiao = regiao;
	}

	@XmlTransient
	public String getCodigoIBGE() {
		if (codigoIBGE == null) {
			codigoIBGE = "";
		}
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}
    
    
}
