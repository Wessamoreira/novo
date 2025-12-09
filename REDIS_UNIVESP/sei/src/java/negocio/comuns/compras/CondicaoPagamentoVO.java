package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CondicaoPagamento. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CondicaoPagamentoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private Integer nrParcela;
	private Integer intervaloParcela;
	private Boolean entrada;
	/**
	 * Atributo responsável por manter os objetos da classe <code>ParcelaCondicaoPagamento</code>.
	 */
	private List<ParcelaCondicaoPagamentoVO> parcelaCondicaoPagamentoVOs;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>CondicaoPagamento</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public CondicaoPagamentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>CondicaoPagamentoVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(CondicaoPagamentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), "O campo NOME (Condição de Pagamento) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNrParcela()), "O campo NR. DE PARCELAS (Condição de Pagamento) deve ser informado.");
		Uteis.checkState(obj.getIntervaloParcela().intValue() == 0 && !obj.getNrParcela().equals(1), "Como existe mais de uma parcela então o  intervalo de parcelas não pode ser zerado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getParcelaCondicaoPagamentoVOs()), "Deve ser adicionado as parcelas da condição de pagamento.");		
		Uteis.checkState(obj.getTotalPercentualValor() != 100.00, "A soma dos VALORES PARCELAS (Condição de Pagamento) estão diferente de 100%");
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados() {
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>ParcelaCondicaoPagamentoVO</code> ao List <code>parcelaCondicaoPagamentoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ParcelaCondicaoPagamento</code> - getNumeroParcela() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>ParcelaCondicaoPagamentoVO</code> que será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjParcelaCondicaoPagamentoVOs(ParcelaCondicaoPagamentoVO obj) throws Exception {
		// ParcelaCondicaoPagamentoVO.validarDados(obj);
		int index = 0;
		Iterator i = getParcelaCondicaoPagamentoVOs().iterator();
		while (i.hasNext()) {
			ParcelaCondicaoPagamentoVO objExistente = (ParcelaCondicaoPagamentoVO) i.next();
			if (objExistente.getNumeroParcela().equals(obj.getNumeroParcela())) {
				getParcelaCondicaoPagamentoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getParcelaCondicaoPagamentoVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>ParcelaCondicaoPagamentoVO</code> no List <code>parcelaCondicaoPagamentoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ParcelaCondicaoPagamento</code> - getNumeroParcela() - como identificador (key) do objeto no List.
	 *
	 * @param numeroParcela
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjParcelaCondicaoPagamentoVOs(Integer numeroParcela) throws Exception {
		int index = 0;
		Iterator i = getParcelaCondicaoPagamentoVOs().iterator();
		while (i.hasNext()) {
			ParcelaCondicaoPagamentoVO objExistente = (ParcelaCondicaoPagamentoVO) i.next();
			if (objExistente.getNumeroParcela().equals(numeroParcela)) {
				getParcelaCondicaoPagamentoVOs().remove(index);
				setNrParcela(getNrParcela() - 1);
				remontarListaParcelaCondicaoPagamento();
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe <code>ParcelaCondicaoPagamentoVO</code> no List <code>parcelaCondicaoPagamentoVOs</code>. Utiliza o atributo padrão de consulta da classe <code>ParcelaCondicaoPagamento</code> - getNumeroParcela() - como identificador (key) do objeto no List.
	 *
	 * @param numeroParcela
	 *            Parâmetro para localizar o objeto do List.
	 */
	public ParcelaCondicaoPagamentoVO consultarObjParcelaCondicaoPagamentoVO(Integer numeroParcela) throws Exception {
		Iterator i = getParcelaCondicaoPagamentoVOs().iterator();
		while (i.hasNext()) {
			ParcelaCondicaoPagamentoVO objExistente = (ParcelaCondicaoPagamentoVO) i.next();
			if (objExistente.getNumeroParcela().equals(numeroParcela)) {
				return objExistente;
			}
		}
		return null;
	}

	public void montarListaParcelaCondicaoPagamento() throws Exception {
		double porcentagemSugerida = Uteis.arrendondarForcando2CadasDecimais(100 / getNrParcela().intValue());
		int contadorParcela = 1;
		int contadorIntervalo = 0;
		setParcelaCondicaoPagamentoVOs(new ArrayList());
		while (contadorParcela <= getNrParcela().intValue()) {
			ParcelaCondicaoPagamentoVO obj = new ParcelaCondicaoPagamentoVO();
			obj.setNumeroParcela(contadorParcela);
			if (getEntrada() && contadorParcela == 1 && !getNrParcela().equals(new Integer(1))) {
				contadorIntervalo = contadorIntervalo;
			} else {
				contadorIntervalo = contadorIntervalo + getIntervaloParcela().intValue();
			}
			if (contadorParcela == getNrParcela().intValue()) {
				porcentagemSugerida = Uteis.arrendondarForcando2CadasDecimais(100 - (porcentagemSugerida * (contadorParcela - 1)));
			}
			obj.setPercentualValor(new Double(porcentagemSugerida));
			obj.setIntervalo(new Integer(contadorIntervalo));
			adicionarObjParcelaCondicaoPagamentoVOs(obj);
			contadorParcela++;
		}

	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe <code>ParcelaCondicaoPagamento</code>.
	 */
	public List<ParcelaCondicaoPagamentoVO> getParcelaCondicaoPagamentoVOs() {
		if (parcelaCondicaoPagamentoVOs == null) {
			parcelaCondicaoPagamentoVOs = new ArrayList<>();
		}
		return (parcelaCondicaoPagamentoVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe <code>ParcelaCondicaoPagamento</code>.
	 */
	public void setParcelaCondicaoPagamentoVOs(List<ParcelaCondicaoPagamentoVO> parcelaCondicaoPagamentoVOs) {
		this.parcelaCondicaoPagamentoVOs = parcelaCondicaoPagamentoVOs;
	}

	public Double getTotalPercentualValor() {
		return getParcelaCondicaoPagamentoVOs().stream().mapToDouble(ParcelaCondicaoPagamentoVO::getPercentualValor).sum();
	}
	
	public Boolean getEntrada() {
		if (entrada == null) {
			entrada = Boolean.FALSE;
		}
		return (entrada);
	}

	public Boolean isEntrada() {
		if (entrada == null) {
			entrada = Boolean.FALSE;
		}
		return (entrada);
	}

	public void setEntrada(Boolean entrada) {
		this.entrada = entrada;
	}

	public Integer getIntervaloParcela() {
		if (intervaloParcela == null) {
			intervaloParcela = 0;
		}
		return (intervaloParcela);
	}

	public void setIntervaloParcela(Integer intervaloParcela) {
		this.intervaloParcela = intervaloParcela;
	}

	public Integer getNrParcela() {
		if (nrParcela == null) {
			nrParcela = 0;
		}
		return (nrParcela);
	}

	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
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
	
	public static void validarDadosAdicaoParcelaCondicaoPagamento(CondicaoPagamentoVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNome()), "O campo NOME (Condição de Pagamento) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNrParcela()), "O campo Nr. de Parcelas (Condição de Pagamento) deve ser informado.");
	}
	
	public void remontarListaParcelaCondicaoPagamento() {
		if (getNrParcela() == 0) {
			return;
		}
		double porcentagemSugerida = Uteis.arrendondarForcando2CadasDecimais(100 / getNrParcela().intValue());
		int contadorParcela = 1;
		for (ParcelaCondicaoPagamentoVO parcelaCondicaoPagamentoVO : getParcelaCondicaoPagamentoVOs()) {
			parcelaCondicaoPagamentoVO.setNumeroParcela(contadorParcela);
			if (contadorParcela == getNrParcela().intValue()) {
				porcentagemSugerida = Uteis.arrendondarForcando2CadasDecimais(100 - (porcentagemSugerida * (contadorParcela - 1)));
			}
			parcelaCondicaoPagamentoVO.setPercentualValor(new Double(porcentagemSugerida));
			contadorParcela++;
		}
	}
}
