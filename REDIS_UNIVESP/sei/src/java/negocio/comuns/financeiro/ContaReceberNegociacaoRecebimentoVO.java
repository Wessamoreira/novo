package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.NegociacaoRecebimento;

/**
 * Reponsável por manter os dados da entidade ContaReceberNegociacaoRecebimento.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see NegociacaoRecebimento
 */
public class ContaReceberNegociacaoRecebimentoVO extends SuperVO {

	private Integer codigo;
	private Integer negociacaoRecebimento;
	private ContaReceberVO contaReceber;
	private Double valorTotal;
	public static final long serialVersionUID = 1L;

	private String nomeResponsavel;
	private String nomeAluno;
	private String matricula;
	private String turma;
	private Integer codigoNegociacaoRecebimento;
	private String parcelasPagasNrDocumentoDataVencimento;
	private String valorTotalRecebimentoPorExtenso;
	private Double valorTotalRecebimento;
	private String ano;
	private String semestre;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private Boolean contaReceberTerceiro;
	private ChequeVO chequeDevolvido;

	/**
	 * Construtor padrão da classe
	 * <code>ContaReceberNegociacaoRecebimento</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public ContaReceberNegociacaoRecebimentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>ContaReceberNegociacaoRecebimentoVO</code>.
	 */
	public static void validarUnicidade(List<ContaReceberNegociacaoRecebimentoVO> lista, ContaReceberNegociacaoRecebimentoVO obj) throws ConsistirException {
		for (ContaReceberNegociacaoRecebimentoVO repetido : lista) {
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ContaReceberNegociacaoRecebimentoVO</code>. Todos os tipos de
	 * consistência de dados são e devem ser implementadas neste método. São
	 * validações típicas: verificação de campos obrigatórios, verificação de
	 * valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public static void validarDados(ContaReceberNegociacaoRecebimentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getContaReceber().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CONTA RECEBER (Conta Receber Negociacao Recebimento) deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
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

	public Integer getNegociacaoRecebimento() {
		if (negociacaoRecebimento == null) {
			negociacaoRecebimento = 0;
		}
		return negociacaoRecebimento;
	}

	public void setNegociacaoRecebimento(Integer negociacaoRecebimento) {
		this.negociacaoRecebimento = negociacaoRecebimento;
	}

	/**
	 * @return the contaReceber
	 */
	public ContaReceberVO getContaReceber() {
		if (contaReceber == null) {
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	/**
	 * @param contaReceber
	 *            the contaReceber to set
	 */
	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the nomeResponsavel
	 */
	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	/**
	 * @param nomeResponsavel
	 *            the nomeResponsavel to set
	 */
	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	/**
	 * @return the nomeAluno
	 */
	public String getNomeAluno() {
		return nomeAluno;
	}

	/**
	 * @param nomeAluno
	 *            the nomeAluno to set
	 */
	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the turma
	 */
	public String getTurma() {
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(String turma) {
		this.turma = turma;
	}

	/**
	 * @return the codigoNegociacaoRecebimento
	 */
	public Integer getCodigoNegociacaoRecebimento() {
		return codigoNegociacaoRecebimento;
	}

	/**
	 * @param codigoNegociacaoRecebimento
	 *            the codigoNegociacaoRecebimento to set
	 */
	public void setCodigoNegociacaoRecebimento(Integer codigoNegociacaoRecebimento) {
		this.codigoNegociacaoRecebimento = codigoNegociacaoRecebimento;
	}

	/**
	 * @return the parcelasPagasNrDocumentoDataVencimento
	 */
	public String getParcelasPagasNrDocumentoDataVencimento() {
		return parcelasPagasNrDocumentoDataVencimento;
	}

	/**
	 * @param parcelasPagasNrDocumentoDataVencimento
	 *            the parcelasPagasNrDocumentoDataVencimento to set
	 */
	public void setParcelasPagasNrDocumentoDataVencimento(String parcelasPagasNrDocumentoDataVencimento) {
		this.parcelasPagasNrDocumentoDataVencimento = parcelasPagasNrDocumentoDataVencimento;
	}

	/**
	 * @return the valorTotalRecebimentoPorExtenso
	 */
	public String getValorTotalRecebimentoPorExtenso() {
		return valorTotalRecebimentoPorExtenso;
	}

	/**
	 * @param valorTotalRecebimentoPorExtenso
	 *            the valorTotalRecebimentoPorExtenso to set
	 */
	public void setValorTotalRecebimentoPorExtenso(String valorTotalRecebimentoPorExtenso) {
		this.valorTotalRecebimentoPorExtenso = valorTotalRecebimentoPorExtenso;
	}

	/**
	 * @return the valorTotalRecebimento
	 */
	public Double getValorTotalRecebimento() {
		return valorTotalRecebimento;
	}

	/**
	 * @param valorTotalRecebimento
	 *            the valorTotalRecebimento to set
	 */
	public void setValorTotalRecebimento(Double valorTotalRecebimento) {
		this.valorTotalRecebimento = valorTotalRecebimento;
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

	public String getAnoSemestre_Apresentar() {
		return getSemestre() + "º / " + getAno();
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

	public Boolean getApresentarDetalhamentoTerceiro() {
		return getContaReceberTerceiro();
	}

	public Boolean getContaReceberTerceiro() {
		if (contaReceberTerceiro == null) {
			contaReceberTerceiro = Boolean.FALSE;
		}
		return contaReceberTerceiro;
	}

	public void setContaReceberTerceiro(Boolean contaReceberTerceiro) {
		this.contaReceberTerceiro = contaReceberTerceiro;
	}
	
	
	private Integer codigoContaReceber;
	private String descricaoPagamento;
	
	public Integer getCodigoContaReceber() {
		if(codigoContaReceber == null) {
			codigoContaReceber = getContaReceber().getCodigo();
		}
		return codigoContaReceber;
	}
	
	public String getDescricaoPagamento() {
		if(descricaoPagamento == null) {
			descricaoPagamento = getContaReceber().getDescricaoPagamento();
		}
		return descricaoPagamento;
	}
	
	private List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs;

	public List<FormaPagamentoNegociacaoRecebimentoVO> getFormaPagamentoNegociacaoRecebimentoVOs() {
		if(formaPagamentoNegociacaoRecebimentoVOs == null) {
			formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>();
		}
		return formaPagamentoNegociacaoRecebimentoVOs;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVOs(
			List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs) {
		this.formaPagamentoNegociacaoRecebimentoVOs = formaPagamentoNegociacaoRecebimentoVOs;
	}
	
	private Date dataPrevistaDCC;

	public Date getDataPrevistaDCC() {
		if(dataPrevistaDCC == null) {
			dataPrevistaDCC = new Date();
			try {
				dataPrevistaDCC = Uteis.getDataPassada(this.getContaReceber().getDataVencimento(), 5);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dataPrevistaDCC;
	}

	public void setDataPrevistaDCC(Date dataPrevistaDCC) {
		this.dataPrevistaDCC = dataPrevistaDCC;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 */
	//Transient
	private Boolean contasProcessadasDCC;

	public Boolean getContasProcessadasDCC() {
		if (contasProcessadasDCC == null) {
			contasProcessadasDCC = false;
		}
		return contasProcessadasDCC;
	}

	public void setContasProcessadasDCC(Boolean contasProcessadasDCC) {
		this.contasProcessadasDCC = contasProcessadasDCC;
	}

	public ChequeVO getChequeDevolvido() {
		if(chequeDevolvido == null ) {
			chequeDevolvido = new ChequeVO();
		}
		return chequeDevolvido;
	}

	public void setChequeDevolvido(ChequeVO chequeDevolvido) {
		this.chequeDevolvido = chequeDevolvido;
	}
}
