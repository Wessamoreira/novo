/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.enumerador.TipoIntervaloParcelaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;

/**
 * 
 * @author rodrigo
 */
public class NegociacaoContaPagarVO extends SuperVO {

	private Integer codigo;
	private UsuarioVO responsavel;
	private UnidadeEnsinoVO unidadeEnsino;
	private Date data;
	private TipoSacado tipoSacado;	
	private PessoaVO pessoa;
	private Double valor;
	private Double valorTotal;
	private Double juro;
	private Double multa;
	private Double desconto;
	private Double valorEntrada;
	private Integer nrParcela;
	private Integer intervaloParcela;
	private Date dataBaseParcela;
	private TipoIntervaloParcelaEnum tipoIntervaloParcelaEnum;
	private List<ContaPagarNegociadoVO> contaPagarNegociadoVOs;
	private CategoriaDespesaVO categoriaDespesaVO;	
	private MatriculaVO matriculaAluno;
	private FuncionarioVO funcionario;	
	private ParceiroVO parceiro;
	private FornecedorVO fornecedor;
	private OperadoraCartaoVO operadoraCartaoVO;
	private BancoVO bancoVO;
	private List<ContaPagarVO> contaPagarGeradaVOs;
	private List<LancamentoContabilVO> lancamentoContabilCreditoVOs;
	private List<LancamentoContabilVO> lancamentoContabilDebitoVOs;
	private String justificativa;	
	/**
	 * Transientes
	 */
	private Double juroPorParcela;
	private Double multaPorParcela;
	private Double descontoPorParcela;
	private Double valorTotalPrevisto; 
	private ConfiguracaoContabilVO configuracaoContabilVO;
	
	public static final long serialVersionUID = 1L;

	public Boolean getTipoAluno() {
		return getTipoSacado().equals(TipoSacado.ALUNO) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());		
	}

	public Boolean getTipoResponsavelFinanceiro() {
		return getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO)  && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}
	
	public Boolean getTipoFornecedor() {
		return getTipoSacado().equals(TipoSacado.FORNECEDOR) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}

	public Boolean getTipoParceiro() {
		return getTipoSacado().equals(TipoSacado.PARCEIRO) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}

	public Boolean getTipoFuncionario() {
		return getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}
	
	public Boolean getTipoBanco() {		
		return getTipoSacado().equals(TipoSacado.BANCO) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}
	
	public Boolean getTipoOperadoraCartao() {		
		return getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());
	}
	
	public Boolean getTipoCandidato() {
		return getTipoSacado().equals(TipoSacado.CANDIDATO) && Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo());		
	}
    	
	public String getMatricula() {
		if (getTipoAluno()) {
			return getMatriculaAluno().getMatricula();
		}else if(getTipoFuncionario()) {
			return getFuncionario().getMatricula();
		}
		return "";
	}

	public Boolean getExisteContaPagar() {
		return !getContaPagarNegociadoVOs().isEmpty();
	}

	

	public String sacado;
	public String getSacado() {
		if(sacado == null) {
		if (getTipoAluno()) {
			sacado = getTipoSacado().getDescricao() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
		} else if (getTipoFuncionario()) {
			sacado = getTipoSacado().getDescricao() + " - " + getFuncionario().getPessoa().getNome() + " - " + getFuncionario().getMatricula();
		} else if (getTipoResponsavelFinanceiro()) {
			sacado = getTipoSacado().getDescricao().toUpperCase() + " - " + getPessoa().getNome();
		} else if (getTipoFornecedor()) {
			sacado = getTipoSacado().getDescricao().toUpperCase() + " - " + getFornecedor().getNome();
		} else if (getTipoParceiro()) {
			sacado = getTipoSacado().getDescricao().toUpperCase() + " - " + getParceiro().getNome();		
		} else if (getTipoBanco()) {
			sacado = getTipoSacado().getDescricao().toUpperCase() + " - " + getBancoVO().getNome();		
		} else if (getTipoOperadoraCartao()) {
			sacado = getTipoSacado().getDescricao().toUpperCase() + " - " + getOperadoraCartaoVO().getNome();		
		} else if (getTipoCandidato()) {
			sacado = getTipoSacado().getDescricao().toUpperCase() + " - " + getPessoa().getNome();
		}
		}
		return sacado;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public String getData_Apresentar() {
		return Uteis.getData(getData());
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
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
	
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Integer getIntervaloParcela() {
		if (intervaloParcela == null) {
			intervaloParcela = 0;
		}
		return intervaloParcela;
	}

	public void setIntervaloParcela(Integer intervaloParcela) {
		this.intervaloParcela = intervaloParcela;
	}

	public Integer getNrParcela() {
		if (nrParcela == null) {
			nrParcela = 0;
		}
		return nrParcela;
	}

	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	public Double getValorEntrada() {
		if (valorEntrada == null) {
			valorEntrada = 0.0;
		}
		return valorEntrada;
	}

	public void setValorEntrada(Double valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public MatriculaVO getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = new MatriculaVO();
		}
		return matriculaAluno;
	}

	public void setMatriculaAluno(MatriculaVO matriculaAluno) {
		this.matriculaAluno = matriculaAluno;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}


	/**
	 * @return the parceiro
	 */
	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	/**
	 * @param parceiro
	 *            the parceiro to set
	 */
	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}
	

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Date getDataBaseParcela() {		
		return dataBaseParcela;
	}

	public void setDataBaseParcela(Date dataBaseParcela) {
		this.dataBaseParcela = dataBaseParcela;
	}

	public TipoIntervaloParcelaEnum getTipoIntervaloParcelaEnum() {
		if(tipoIntervaloParcelaEnum == null){
			tipoIntervaloParcelaEnum = TipoIntervaloParcelaEnum.DATA_BASE;
		}
		return tipoIntervaloParcelaEnum;
	}

	public void setTipoIntervaloParcelaEnum(TipoIntervaloParcelaEnum tipoIntervaloParcelaEnum) {
		this.tipoIntervaloParcelaEnum = tipoIntervaloParcelaEnum;
	}

	public TipoSacado getTipoSacado() {
		if (tipoSacado == null) {
			tipoSacado = TipoSacado.FORNECEDOR;
		}
		return tipoSacado;
	}

	public void setTipoSacado(TipoSacado tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public List<ContaPagarNegociadoVO> getContaPagarNegociadoVOs() {
		if (contaPagarNegociadoVOs == null) {
			contaPagarNegociadoVOs = new ArrayList<ContaPagarNegociadoVO>();
		}
		return contaPagarNegociadoVOs;
	}

	public void setContaPagarNegociadoVOs(List<ContaPagarNegociadoVO> contaPagarNegociadoVOs) {
		this.contaPagarNegociadoVOs = contaPagarNegociadoVOs;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public BancoVO getBancoVO() {
		if (bancoVO == null) {
			bancoVO = new BancoVO();
		}
		return bancoVO;
	}

	public void setBancoVO(BancoVO bancoVO) {
		this.bancoVO = bancoVO;
	}

	public List<ContaPagarVO> getContaPagarGeradaVOs() {
		if (contaPagarGeradaVOs == null) {
			contaPagarGeradaVOs = new ArrayList<ContaPagarVO>(0);
		}
		return contaPagarGeradaVOs;
	}

	public void setContaPagarGeradaVOs(List<ContaPagarVO> contaPagarGeradaVOs) {
		this.contaPagarGeradaVOs = contaPagarGeradaVOs;
	}


	public Boolean getPossuiSacado() {
		
		if (getTipoAluno()) {
			return Uteis.isAtributoPreenchido(getMatriculaAluno().getMatricula());
		} else if (getTipoFuncionario()) {
			return Uteis.isAtributoPreenchido(getFuncionario().getCodigo());			
		} else if (getTipoResponsavelFinanceiro()) {
			return Uteis.isAtributoPreenchido(getPessoa().getCodigo());			
		} else if (getTipoFornecedor()) {
			return Uteis.isAtributoPreenchido(getFornecedor().getCodigo());
		} else if (getTipoParceiro()) {
			return Uteis.isAtributoPreenchido(getParceiro().getCodigo());		
		} else if (getTipoBanco()) {
			return Uteis.isAtributoPreenchido(getBancoVO().getCodigo());		
		} else if (getTipoOperadoraCartao()) {
			return Uteis.isAtributoPreenchido(getOperadoraCartaoVO().getCodigo());		
		} else if (getTipoCandidato()) {
			return Uteis.isAtributoPreenchido(getMatriculaAluno().getAluno());
		}
		return false;
		
	}
	
	public Integer getCodigoSacado() {		
		if (getTipoAluno() || getTipoFuncionario() || getTipoResponsavelFinanceiro() || getTipoCandidato()) {
			return getPessoa().getCodigo();		 		
		} else if (getTipoFornecedor()) {
			return getFornecedor().getCodigo();
		} else if (getTipoParceiro()) {
			return getParceiro().getCodigo();		
		} else if (getTipoBanco()) {
			return getBancoVO().getCodigo();		
		} else if (getTipoOperadoraCartao()) {
			return getOperadoraCartaoVO().getCodigo();		
		}
		return 0;		
	}
	
	public Boolean getEditar() {
		if (getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public Double getJuroPorParcela() {
		if (juroPorParcela == null) {
			juroPorParcela = 0.0;
		}
		return juroPorParcela;
	}

	public void setJuroPorParcela(Double juroPorParcela) {
		this.juroPorParcela = juroPorParcela;
	}

	public Double getMultaPorParcela() {
		if (multaPorParcela == null) {
			multaPorParcela = 0.0;
		}
		return multaPorParcela;
	}

	public void setMultaPorParcela(Double multaPorParcela) {
		this.multaPorParcela = multaPorParcela;
	}

	public Double getDescontoPorParcela() {
		if (descontoPorParcela == null) {
			descontoPorParcela = 0.0;
		}
		return descontoPorParcela;
	}

	public void setDescontoPorParcela(Double descontoPorParcela) {
		this.descontoPorParcela = descontoPorParcela;
	}

	public Double getValorTotalPrevisto() {
		if (valorTotalPrevisto == null) {
			valorTotalPrevisto = 0.0;
		}
		return valorTotalPrevisto;
	}

	public void setValorTotalPrevisto(Double valorTotalPrevisto) {
		this.valorTotalPrevisto = valorTotalPrevisto;
	}

	public ConfiguracaoContabilVO getConfiguracaoContabilVO() {
		if (configuracaoContabilVO == null) {
			configuracaoContabilVO = new ConfiguracaoContabilVO();
		}
		return configuracaoContabilVO;
	}

	public void setConfiguracaoContabilVO(ConfiguracaoContabilVO configuracaoContabilVO) {
		this.configuracaoContabilVO = configuracaoContabilVO;
	}
	

	public Integer getCodigoPessoaSacado() {
		
		if (getTipoSacado() != null) {
			switch (getTipoSacado()) {
			case ALUNO:
				return getPessoa().getCodigo();
			case RESPONSAVEL_FINANCEIRO:
				return getPessoa().getCodigo();
			case FUNCIONARIO_PROFESSOR:
				return getFuncionario().getCodigo();
			case PARCEIRO:
				return getParceiro().getCodigo();
			case FORNECEDOR:
				return getFornecedor().getCodigo();
			case BANCO:
				return getBancoVO().getCodigo();
			case OPERADORA_CARTAO:
				return getOperadoraCartaoVO().getCodigo();
			}
		}
		return 0;
	}

	public List<LancamentoContabilVO> getLancamentoContabilCreditoVOs() {
		if (lancamentoContabilCreditoVOs == null) {
			lancamentoContabilCreditoVOs = new ArrayList<LancamentoContabilVO>(0);
		}
		return lancamentoContabilCreditoVOs;
	}

	public void setLancamentoContabilCreditoVOs(List<LancamentoContabilVO> lancamentoContabilCreditoVOs) {
		this.lancamentoContabilCreditoVOs = lancamentoContabilCreditoVOs;
	}

	public List<LancamentoContabilVO> getLancamentoContabilDebitoVOs() {
		if (lancamentoContabilDebitoVOs == null) {
			lancamentoContabilDebitoVOs = new ArrayList<LancamentoContabilVO>(0);
		}
		return lancamentoContabilDebitoVOs;
	}

	public void setLancamentoContabilDebitoVOs(List<LancamentoContabilVO> lancamentoContabilDebitoVOs) {
		this.lancamentoContabilDebitoVOs = lancamentoContabilDebitoVOs;
	}
	
	public Boolean getApresentarDadosContabeis() {
		return (getJuro() > 0 || getMulta() > 0 || getDesconto() > 0);
	}
	

}
