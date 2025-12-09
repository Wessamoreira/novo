package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade DescontoProgressivo. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class DescontoProgressivoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private Integer diaLimite1;
	private Double percDescontoLimite1;
	private Double valorDescontoLimite1;
	private Integer diaLimite2;
	private Double percDescontoLimite2;
	private Double valorDescontoLimite2;
	private Integer diaLimite3;
	private Double percDescontoLimite3;
	private Double valorDescontoLimite3;
	private Integer diaLimite4;
	private Double percDescontoLimite4;
	private Double valorDescontoLimite4;
	/**
	 * Atributo utilizado no relatório ListagemDescontosAlunos
	 */
	private Double valorTotalDescontoMatriculaPorDescontoProgressivoCalculado;
	private Double valorTotalDescontoParcelaPorDescontoProgressivoCalculado;
	private Boolean ativado;
	private Date dataAtivacao;
	private UsuarioVO responsavelAtivacao;
	private Date dataInativacao;
	private UsuarioVO responsavelInativacao;
	private Boolean utilizarDiaFixo;
	private Boolean utilizarDiaUtil;
	private Boolean nrDiasAntesVcto;

	private Date dataBaseValidade1;
	private Double valorBaseCalculo1;
	private Double valorDescontoCalculado1;
	private Date dataBaseValidade2;
	private Double valorBaseCalculo2;
	private Double valorDescontoCalculado2;
	private Date dataBaseValidade3;
	private Double valorBaseCalculo3;
	private Double valorDescontoCalculado3;
	private Date dataBaseValidade4;
	private Double valorBaseCalculo4;
	private Double valorDescontoCalculado4;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>DescontoProgressivo</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public DescontoProgressivoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>DescontoProgressivoVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(DescontoProgressivoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Desconto Progressivo Mensalidades) deve ser informado.");
		}

		if (!obj.getUtilizarDiaFixo() && !obj.getUtilizarDiaUtil()) {
			// se entrar aqui é por que utiliza-se dias antes do vcto

			// validando se o diaLimite1 é maior que zero
			if ((obj.getDiaLimite1() == null)) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 1 (Desconto Progressivo Mensalidades) deve ser informado. Para descontos válidos até a data de vencimento deve ser utilizado o nr dias 0.");
			}
			// validando se o diaLimite2 é maior que zero
			if (((obj.getPercDescontoLimite2().doubleValue() > 0) || (obj.getValorDescontoLimite2().doubleValue() > 0)) && obj.getDiaLimite2() == null) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 2 (Desconto Progressivo Mensalidades) deve ser informado. Para descontos válidos até a data de vencimento deve ser utilizado o nr dias 0.");
			}
			// validando se o diaLimite3 é maior que zero
			if (((obj.getPercDescontoLimite3().doubleValue() > 0) || (obj.getValorDescontoLimite3().doubleValue() > 0)) && obj.getDiaLimite3() == null) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 3 (Desconto Progressivo Mensalidades) deve ser informado. Para descontos válidos até a data de vencimento deve ser utilizado o nr dias 0.");
			}
			// validando se o diaLimite4 é maior que zero
			if (((obj.getPercDescontoLimite4().doubleValue() > 0) || (obj.getValorDescontoLimite4().doubleValue() > 0)) && obj.getDiaLimite4() == null) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 4 (Desconto Progressivo Mensalidades) deve ser informado. Para descontos válidos até a data de vencimento deve ser utilizado o nr dias 0.");
			}

			if (obj.getPercDescontoLimite2().doubleValue() > 0
					&& obj.getDiaLimite1().intValue() <= obj.getDiaLimite2().intValue()) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 1 (Desconto Progressivo Mensalidades) deve ser maior que o campo NR. DIAS ANTES VCTO 2.");
			}
			if (obj.getPercDescontoLimite3().doubleValue() > 0
					&& obj.getDiaLimite2().intValue() <= obj.getDiaLimite3().intValue()) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 2 (Desconto Progressivo Mensalidades) deve ser maior que o campo NR. DIAS ANTES VCTO 3.");
			}
			if (obj.getPercDescontoLimite4().doubleValue() > 0
					&& obj.getDiaLimite3().intValue() <= obj.getDiaLimite4().intValue()) {
				throw new ConsistirException("O campo NR. DIAS ANTES VCTO 3 (Desconto Progressivo Mensalidades) deve ser maior que o campo NR. DIAS ANTES VCTO 4.");
			}

			if (obj.getDiaLimite1().intValue() < obj.getDiaLimite2().intValue()
					|| obj.getDiaLimite1().intValue() < obj.getDiaLimite3().intValue()
					|| obj.getDiaLimite1().intValue() < obj.getDiaLimite4().intValue()) {
				throw new ConsistirException("O desconto progressivo deve ser cadastrado em ordem decrescente, sendo assim o campo NR. DIAS ANTES VCTO 1 (Desconto Progressivo Mensalidades) deve ser maior que os demais campos (NR. DIAS ANTES VCTO 2, 3 e 4 ).");
			}
			if (obj.getDiaLimite2().intValue() > obj.getDiaLimite1().intValue()
					|| obj.getDiaLimite2().intValue() < obj.getDiaLimite3().intValue()
					|| obj.getDiaLimite2().intValue() < obj.getDiaLimite4().intValue()) {
				throw new ConsistirException("O desconto progressivo deve ser cadastrado em ordem decrescente, sendo assim o campo NR. DIAS ANTES VCTO 2 (Desconto Progressivo Mensalidades) deve ser maior que os campos NR. DIAS ANTES VCTO 3 e 4 e menor que NR. DIAS ANTES VCTO 1.");
			}
			if (obj.getDiaLimite3().intValue() > obj.getDiaLimite1().intValue()
					|| obj.getDiaLimite3().intValue() > obj.getDiaLimite2().intValue()
					|| obj.getDiaLimite3().intValue() < obj.getDiaLimite4().intValue()) {
				throw new ConsistirException("O desconto progressivo deve ser cadastrado em ordem decrescente, sendo assim o campo NR. DIAS ANTES VCTO 3 (Desconto Progressivo Mensalidades) deve ser maior que o campo NR. DIAS ANTES VCTO 4 e menor que os campos NR. DIAS ANTES VCTO 1 e 2.");
			}
			if (obj.getDiaLimite4().intValue() > obj.getDiaLimite1().intValue()
					|| obj.getDiaLimite4().intValue() > obj.getDiaLimite2().intValue()
					|| obj.getDiaLimite4().intValue() > obj.getDiaLimite3().intValue()) {
				throw new ConsistirException("O desconto progressivo deve ser cadastrado em ordem decrescente, sendo assim o campo NR. DIAS ANTES VCTO 4 (Desconto Progressivo Mensalidades) deve ser menor que os campos NR. DIAS ANTES VCTO 1, 2 e 3.");
			}

		}

		if (obj.getUtilizarDiaFixo() && !obj.getUtilizarDiaUtil()) {
			if (obj.getPercDescontoLimite2().doubleValue() > 0
					&& obj.getDiaLimite1().intValue() >= obj.getDiaLimite2().intValue()) {
				throw new ConsistirException("O campo DIA VENCIMENTO 1 (Desconto Progressivo Mensalidades) deve ser MENOR que o campo DIA VENCIMENTO 2.");
			}
			if (obj.getPercDescontoLimite3().doubleValue() > 0
					&& obj.getDiaLimite2().intValue() >= obj.getDiaLimite3().intValue()) {
				throw new ConsistirException("O campo DIA VENCIMENTO 2 (Desconto Progressivo Mensalidades) deve ser MENOR que o campo DIA VENCIMENTO 3.");
			}
			if (obj.getPercDescontoLimite4().doubleValue() > 0
					&& obj.getDiaLimite3().intValue() >= obj.getDiaLimite4().intValue()) {
				throw new ConsistirException("O campo DIA VENCIMENTO 3 (Desconto Progressivo Mensalidades) deve ser MENOR que o campo DIA VENCIMENTO 4.");
			}
		}

		if (!obj.getUtilizarDiaFixo() && obj.getUtilizarDiaUtil()) {
			if (obj.getPercDescontoLimite2().doubleValue() > 0
					&& obj.getDiaLimite1().intValue() >= obj.getDiaLimite2().intValue()) {
				throw new ConsistirException("O campo DIA ÚTIL 1 (Desconto Progressivo Mensalidades) deve ser MENOR que o campo DIA ÚTIL 2.");
			}
			if (obj.getPercDescontoLimite3().doubleValue() > 0
					&& obj.getDiaLimite2().intValue() >= obj.getDiaLimite3().intValue()) {
				throw new ConsistirException("O campo DIA ÚTIL 2 (Desconto Progressivo Mensalidades) deve ser MENOR que o campo DIA ÚTIL 3.");
			}
			if (obj.getPercDescontoLimite4().doubleValue() > 0
					&& obj.getDiaLimite3().intValue() >= obj.getDiaLimite4().intValue()) {
				throw new ConsistirException("O campo DIA ÚTIL 3 (Desconto Progressivo Mensalidades) deve ser MENOR que o campo DIA ÚTIL 4.");
			}
		}
	}

	public boolean getNovoObjeto() {
		if (codigo.intValue() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
		setDiaLimite1(0);
		setPercDescontoLimite1(0.0);
		setDiaLimite2(0);
		setPercDescontoLimite2(0.0);
		setDiaLimite3(0);
		setPercDescontoLimite3(0.0);
		setDiaLimite4(0);
		setPercDescontoLimite4(0.0);
	}

	public Double getPercDescontoLimite4() {
		return (percDescontoLimite4);
	}

	public void setPercDescontoLimite4(Double percDescontoLimite4) {
		this.percDescontoLimite4 = percDescontoLimite4;
	}

	public Integer getDiaLimite4() {
		return (diaLimite4);
	}

	public void setDiaLimite4(Integer diaLimite4) {
		this.diaLimite4 = diaLimite4;
	}

	public Double getPercDescontoLimite3() {
		return (percDescontoLimite3);
	}

	public void setPercDescontoLimite3(Double percDescontoLimite3) {
		this.percDescontoLimite3 = percDescontoLimite3;
	}

	public Integer getDiaLimite3() {
		return (diaLimite3);
	}

	public void setDiaLimite3(Integer diaLimite3) {
		this.diaLimite3 = diaLimite3;
	}

	public Double getPercDescontoLimite2() {
		return (percDescontoLimite2);
	}

	public void setPercDescontoLimite2(Double percDescontoLimite2) {
		this.percDescontoLimite2 = percDescontoLimite2;
	}

	public Integer getDiaLimite2() {
		return (diaLimite2);
	}

	public void setDiaLimite2(Integer diaLimite2) {
		this.diaLimite2 = diaLimite2;
	}

	public Double getPercDescontoLimite1() {
		return (percDescontoLimite1);
	}

	public void setPercDescontoLimite1(Double percDescontoLimite1) {
		this.percDescontoLimite1 = percDescontoLimite1;
	}

	public Integer getDiaLimite1() {
		return (diaLimite1);
	}

	public void setDiaLimite1(Integer diaLimite1) {
		this.diaLimite1 = diaLimite1;
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

	public Double getValorDescontoLimite1() {
		if (valorDescontoLimite1 == null) {
			valorDescontoLimite1 = 0.0;
		}
		return valorDescontoLimite1;
	}

	public void setValorDescontoLimite1(Double valorDescontoLimite1) {
		this.valorDescontoLimite1 = valorDescontoLimite1;
	}

	public Double getValorDescontoLimite2() {
		if (valorDescontoLimite2 == null) {
			valorDescontoLimite2 = 0.0;
		}
		return valorDescontoLimite2;
	}

	public void setValorDescontoLimite2(Double valorDescontoLimite2) {
		this.valorDescontoLimite2 = valorDescontoLimite2;
	}

	public Double getValorDescontoLimite3() {
		if (valorDescontoLimite3 == null) {
			valorDescontoLimite3 = 0.0;
		}
		return valorDescontoLimite3;
	}

	public void setValorDescontoLimite3(Double valorDescontoLimite3) {
		this.valorDescontoLimite3 = valorDescontoLimite3;
	}

	public Double getValorDescontoLimite4() {
		if (valorDescontoLimite4 == null) {
			valorDescontoLimite4 = 0.0;
		}
		return valorDescontoLimite4;
	}

	public void setValorDescontoLimite4(Double valorDescontoLimite4) {
		this.valorDescontoLimite4 = valorDescontoLimite4;
	}

	public Boolean getPodeSerAlterado() {
		if (getCodigo().equals(0)) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = new Integer(0);
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean getIsUsarValorFixo() {
		if (getPercDescontoLimite1().equals(0.0) && getPercDescontoLimite2().equals(0.0) && getPercDescontoLimite3().equals(0.0) && getPercDescontoLimite4().equals(0.0)) {
			return true;
		}
		return false;
	}

	public Double getValorTotalDescontoMatriculaPorDescontoProgressivoCalculado() {
		if (valorTotalDescontoMatriculaPorDescontoProgressivoCalculado == null) {
			valorTotalDescontoMatriculaPorDescontoProgressivoCalculado = 0.0;
		}
		return valorTotalDescontoMatriculaPorDescontoProgressivoCalculado;
	}

	public void setValorTotalDescontoMatriculaPorDescontoProgressivoCalculado(Double valorTotalDescontoMatriculaPorDescontoProgressivoCalculado) {
		this.valorTotalDescontoMatriculaPorDescontoProgressivoCalculado = valorTotalDescontoMatriculaPorDescontoProgressivoCalculado;
	}

	public Double getValorTotalDescontoParcelaPorDescontoProgressivoCalculado() {
		if (valorTotalDescontoParcelaPorDescontoProgressivoCalculado == null) {
			valorTotalDescontoParcelaPorDescontoProgressivoCalculado = 0.0;
		}
		return valorTotalDescontoParcelaPorDescontoProgressivoCalculado;
	}

	public void setValorTotalDescontoParcelaPorDescontoProgressivoCalculado(Double valorTotalDescontoParcelaPorDescontoProgressivoCalculado) {
		this.valorTotalDescontoParcelaPorDescontoProgressivoCalculado = valorTotalDescontoParcelaPorDescontoProgressivoCalculado;
	}

	/**
	 * @return the ativado
	 */
	public Boolean getAtivado() {
		if (ativado == null) {
			ativado = Boolean.FALSE;
		}
		return ativado;
	}

	/**
	 * @param ativado
	 *            the ativado to set
	 */
	public void setAtivado(Boolean ativado) {
		this.ativado = ativado;
	}

	/**
	 * @return the dataAtivacao
	 */
	public Date getDataAtivacao() {
		return dataAtivacao;
	}

	/**
	 * @param dataAtivacao
	 *            the dataAtivacao to set
	 */
	public void setDataAtivacao(Date dataAtivacao) {
		this.dataAtivacao = dataAtivacao;
	}

	/**
	 * @return the responsavelAtivacao
	 */
	public UsuarioVO getResponsavelAtivacao() {
		if (responsavelAtivacao == null) {
			responsavelAtivacao = new UsuarioVO();
		}
		return responsavelAtivacao;
	}

	/**
	 * @param responsavelAtivacao
	 *            the responsavelAtivacao to set
	 */
	public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
		this.responsavelAtivacao = responsavelAtivacao;
	}

	/**
	 * @return the dataInativacao
	 */
	public Date getDataInativacao() {
		return dataInativacao;
	}

	/**
	 * @param dataInativacao
	 *            the dataInativacao to set
	 */
	public void setDataInativacao(Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}

	/**
	 * @return the responsavelInativacao
	 */
	public UsuarioVO getResponsavelInativacao() {
		if (responsavelInativacao == null) {
			responsavelInativacao = new UsuarioVO();
		}
		return responsavelInativacao;
	}

	/**
	 * @param responsavelInativacao
	 *            the responsavelInativacao to set
	 */
	public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
		this.responsavelInativacao = responsavelInativacao;
	}

	public Boolean getUtilizarDiaFixo() {
		if (utilizarDiaFixo == null) {
			utilizarDiaFixo = false;
		}
		return utilizarDiaFixo;
	}

	public void setUtilizarDiaFixo(Boolean utilizarDiaFixo) {
		this.utilizarDiaFixo = utilizarDiaFixo;
	}

	/**
	 * @return the utilizarDiaUtil
	 */
	public Boolean getUtilizarDiaUtil() {
		if (utilizarDiaUtil == null) {
			utilizarDiaUtil = Boolean.FALSE;
		}
		return utilizarDiaUtil;
	}

	/**
	 * @param utilizarDiaUtil
	 *            the utilizarDiaUtil to set
	 */
	public void setUtilizarDiaUtil(Boolean utilizarDiaUtil) {
		this.utilizarDiaUtil = utilizarDiaUtil;
	}

	/**
	 * @return the nrDiasAntesVcto
	 */
	public Boolean getNrDiasAntesVcto() {
		if (!getUtilizarDiaUtil().booleanValue() && !getUtilizarDiaFixo().booleanValue()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param nrDiasAntesVcto
	 *            the nrDiasAntesVcto to set
	 */
	public void setNrDiasAntesVcto(Boolean nrDiasAntesVcto) {
		this.nrDiasAntesVcto = nrDiasAntesVcto;
	}

	/**
	 * INICIO MERGE EDIGAR - 29-03-2018
	 */
	/**
	 * Atributo transient - utilizado para indicar que um desconto progressivo foi modificado na conta a receber. Desta maneira. Os dados do mesmo nao devem ser recarregados no momento de calcular valores dos descontos e afins no método espirita da conta a receber.
	 */
	private Boolean descontoProgressivoEditadoManualmenteContaReceber;

	public Boolean getDescontoProgressivoEditadoManualmenteContaReceber() {
		if (descontoProgressivoEditadoManualmenteContaReceber == null) {
			descontoProgressivoEditadoManualmenteContaReceber = Boolean.FALSE;
		}
		return descontoProgressivoEditadoManualmenteContaReceber;
	}

	public void setDescontoProgressivoEditadoManualmenteContaReceber(Boolean descontoProgressivoEditadoManualmenteContaReceber) {
		this.descontoProgressivoEditadoManualmenteContaReceber = descontoProgressivoEditadoManualmenteContaReceber;
	}

	public Date getDataBaseValidade1() {
		return dataBaseValidade1;
	}

	public void setDataBaseValidade1(Date dataBaseValidade1) {
		this.dataBaseValidade1 = dataBaseValidade1;
	}

	public Double getValorBaseCalculo1() {
		return valorBaseCalculo1;
	}

	public void setValorBaseCalculo1(Double valorBaseCalculo1) {
		this.valorBaseCalculo1 = valorBaseCalculo1;
	}

	public Double getValorDescontoCalculado1() {
		return valorDescontoCalculado1;
	}

	public void setValorDescontoCalculado1(Double valorDescontoCalculado1) {
		this.valorDescontoCalculado1 = valorDescontoCalculado1;
	}

	public Date getDataBaseValidade2() {
		return dataBaseValidade2;
	}

	public void setDataBaseValidade2(Date dataBaseValidade2) {
		this.dataBaseValidade2 = dataBaseValidade2;
	}

	public Double getValorBaseCalculo2() {
		return valorBaseCalculo2;
	}

	public void setValorBaseCalculo2(Double valorBaseCalculo2) {
		this.valorBaseCalculo2 = valorBaseCalculo2;
	}

	public Double getValorDescontoCalculado2() {
		return valorDescontoCalculado2;
	}

	public void setValorDescontoCalculado2(Double valorDescontoCalculado2) {
		this.valorDescontoCalculado2 = valorDescontoCalculado2;
	}

	public Date getDataBaseValidade3() {
		return dataBaseValidade3;
	}

	public void setDataBaseValidade3(Date dataBaseValidade3) {
		this.dataBaseValidade3 = dataBaseValidade3;
	}

	public Double getValorBaseCalculo3() {
		return valorBaseCalculo3;
	}

	public void setValorBaseCalculo3(Double valorBaseCalculo3) {
		this.valorBaseCalculo3 = valorBaseCalculo3;
	}

	public Double getValorDescontoCalculado3() {
		return valorDescontoCalculado3;
	}

	public void setValorDescontoCalculado3(Double valorDescontoCalculado3) {
		this.valorDescontoCalculado3 = valorDescontoCalculado3;
	}

	public Date getDataBaseValidade4() {
		return dataBaseValidade4;
	}

	public void setDataBaseValidade4(Date dataBaseValidade4) {
		this.dataBaseValidade4 = dataBaseValidade4;
	}

	public Double getValorBaseCalculo4() {
		return valorBaseCalculo4;
	}

	public void setValorBaseCalculo4(Double valorBaseCalculo4) {
		this.valorBaseCalculo4 = valorBaseCalculo4;
	}

	public Double getValorDescontoCalculado4() {
		return valorDescontoCalculado4;
	}

	public void setValorDescontoCalculado4(Double valorDescontoCalculado4) {
		this.valorDescontoCalculado4 = valorDescontoCalculado4;
	}

}
