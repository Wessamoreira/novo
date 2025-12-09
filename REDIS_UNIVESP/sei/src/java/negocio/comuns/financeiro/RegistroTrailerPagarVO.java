package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class RegistroTrailerPagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543292751104157097L;
	private Integer codigo;
	private String codigoBanco;
	private String loteServico;
	private Integer tipoRegistro;
	private Integer quantidadeLote;
	private Integer quantidadeRegistro;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;

	/**
	 * Construtor padrão da classe <code>RegistroTrailer</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public RegistroTrailerPagarVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>RegistroTrailerVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(RegistroTrailerPagarVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}
	
	

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		if (controleCobrancaPagarVO == null) {
			controleCobrancaPagarVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
	}

	public String getLoteServico() {
		if (loteServico == null) {
			loteServico = "";
		}
		return loteServico;
	}

	public void setLoteServico(String loteServico) {
		this.loteServico = loteServico;
	}

	public Integer getQuantidadeRegistro() {
		if (quantidadeRegistro == null) {
			quantidadeRegistro = 0;
		}
		return (quantidadeRegistro);
	}

	public void setQuantidadeRegistro(Integer quantidadeRegistro) {
		this.quantidadeRegistro = quantidadeRegistro;
	}

	public Integer getQuantidadeLote() {
		if (quantidadeLote == null) {
			quantidadeLote = 0;
		}
		return (quantidadeLote);
	}

	public void setQuantidadeLote(Integer quantidadeLote) {
		this.quantidadeLote = quantidadeLote;
	}

	public Integer getTipoRegistro() {
		if (tipoRegistro == null) {
			tipoRegistro = 0;
		}
		return (tipoRegistro);
	}

	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodigoBanco() {
		if (codigoBanco == null) {
			codigoBanco = "";
		}
		return (codigoBanco);
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
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

}
