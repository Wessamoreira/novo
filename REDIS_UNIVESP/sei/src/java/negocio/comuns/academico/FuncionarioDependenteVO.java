package negocio.comuns.academico;

import java.math.BigDecimal;
import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.EstadoCivilEnum;
import negocio.comuns.administrativo.enumeradores.GrauParentescoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public class FuncionarioDependenteVO extends SuperVO {

	private static final long serialVersionUID = 7028497133310011767L;

	private Integer codigo;
	private FuncionarioVO funcionarioVO;
	private String cpf;
	private String parentesco;
	private String sexo;
	private String estadoCivil;
	private Date dataNascimento;
	private String localNascimento;
	private String cartorio;
	private String registro;
	private String livro;
	private String folha;
	private Date dataEntregaCertidao;
	private String numeroSus;
	private Boolean universitarioEscolaTecnica;
	private Boolean cartaoVacina;
	private Boolean frequenciaEscolar;
	private String telefone;
	private String email;
	private String nome;

	private Boolean pensao;
	private Boolean inss;
	private Boolean irrf;
	private Boolean salarioFamilia;
	private String tipo;
	private BigDecimal percentual;
	private String banco;
	private String agencia;
	private String conta;
	private String operacao;
	private String cpfResponsavel;
	private String nomeResponsavel;
	private Date dataInicioDesconto;

	private String inss_Apresentar;
	private String irrf_Apresentar;
	private String salarioFamilia_Apresentar;
	private String pensao_Apresentar;
	private FormulaFolhaPagamentoVO formulaCalculo;
	
	private EventoFolhaPagamentoVO eventoFolhaPagamento;

	private Boolean itemEmEdicao;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null)
			funcionarioVO = new FuncionarioVO();
		return funcionarioVO;
	}

	public String getCpf() {
		if (cpf == null)
			cpf = "";
		return cpf;
	}

	public String getParentesco() {
		if (parentesco == null)
			parentesco = "";
		return parentesco;
	}

	public String getParentesco_Apresentar() {
		switch (getParentesco()) {
		case "FILHO_VALIDO":
			return GrauParentescoEnum.FILHO_VALIDO.getValorApresentar();
		case "FILHO_INVALIDO":
			return GrauParentescoEnum.FILHO_INVALIDO.getValorApresentar();
		case "CONJUGE":
			return GrauParentescoEnum.CONJUGE.getValorApresentar();
		case "SOGRO":
			return GrauParentescoEnum.SOGRO.getValorApresentar();
		case "OUTROS":
			return GrauParentescoEnum.OUTROS.getValorApresentar();
		case "AVO":
			return GrauParentescoEnum.AVO.getValorApresentar();
		case "COMPANHEIRO":
			return GrauParentescoEnum.COMPANHEIRO.getValorApresentar();
		case "ENTEADO":
			return GrauParentescoEnum.ENTEADO.getValorApresentar();
		case "EX_CONJUGE":
			return GrauParentescoEnum.EX_CONJUGE.getValorApresentar();
		case "IRMA_VALIDA":
			return GrauParentescoEnum.IRMA_VALIDA.getValorApresentar();
		case "IRMA_INVALIDA":
			return GrauParentescoEnum.IRMA_INVALIDA.getValorApresentar();
		case "EX_COMPANHEIRO":
			return GrauParentescoEnum.EX_COMPANHEIRO.getValorApresentar();
		case "EX_SOGRO":
			return GrauParentescoEnum.EX_SOGRO.getValorApresentar();
		case "NETO":
			return GrauParentescoEnum.NETO.getValorApresentar();
		case "EX_ENTEADO":
			return GrauParentescoEnum.EX_ENTEADO.getValorApresentar();
		case "PAI" :
			return GrauParentescoEnum.PAI.getValorApresentar();
		case "MAE" :
			return GrauParentescoEnum.MAE.getValorApresentar();
		default:
			break;
		}
		return "";
	}

	public String getSexo() {
		if (sexo == null)
			sexo = "";
		return sexo;
	}

	public String getEstadoCivil() {
		if (estadoCivil == null)
			estadoCivil = "";
		return estadoCivil;
	}

	public String getEstadoCivil_Apresentar() {
		switch (getEstadoCivil()) {
		case "SOLTEIRO":
			return EstadoCivilEnum.SOLTEIRO.getValorApresentar();
		case "CASADO":
			return EstadoCivilEnum.CASADO.getValorApresentar();
		case "DIVORCIADO":
			return EstadoCivilEnum.DIVORCIADO.getValorApresentar();
		case "VIUVO":
			return EstadoCivilEnum.VIUVO.getValorApresentar();
		case "SEPARADO":
			return EstadoCivilEnum.SEPARADO.getValorApresentar();
		default:
			break;
		}
		return "";
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public String getDataNascimento_Apresentar() {
		if (getDataNascimento() != null)
			return (Uteis.getData(getDataNascimento()));
		else
			return "";
	}

	public String getLocalNascimento() {
		if (localNascimento == null)
			localNascimento = "";
		return localNascimento;
	}

	public String getCartorio() {
		if (cartorio == null)
			cartorio = "";
		return cartorio;
	}

	public String getRegistro() {
		if (registro == null)
			registro = "";
		return registro;
	}

	public String getLivro() {
		if (livro == null)
			livro = "";
		return livro;
	}

	public String getFolha() {
		if (folha == null)
			folha = "";
		return folha;
	}

	public Date getDataEntregaCertidao() {
		return dataEntregaCertidao;
	}

	public String getNumeroSus() {
		if (numeroSus == null)
			numeroSus = "";
		return numeroSus;
	}

	public Boolean getUniversitarioEscolaTecnica() {
		if (universitarioEscolaTecnica == null)
			universitarioEscolaTecnica = false;
		return universitarioEscolaTecnica;
	}

	public Boolean getCartaoVacina() {
		if (cartaoVacina == null)
			cartaoVacina = false;
		return cartaoVacina;
	}

	public Boolean getFrequenciaEscolar() {
		if (frequenciaEscolar == null)
			frequenciaEscolar = false;
		return frequenciaEscolar;
	}

	public String getTelefone() {
		if (telefone == null)
			telefone = "";
		return telefone;
	}

	public String getEmail() {
		if (email == null)
			email = "";
		return email;
	}

	public String getNome() {
		if (nome == null)
			nome = "";
		return nome;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public void setLocalNascimento(String localNascimento) {
		this.localNascimento = localNascimento;
	}

	public void setCartorio(String cartorio) {
		this.cartorio = cartorio;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public void setLivro(String livro) {
		this.livro = livro;
	}

	public void setFolha(String folha) {
		this.folha = folha;
	}

	public void setDataEntregaCertidao(Date dataEntregaCertidao) {
		this.dataEntregaCertidao = dataEntregaCertidao;
	}

	public void setNumeroSus(String numeroSus) {
		this.numeroSus = numeroSus;
	}

	public void setUniversitarioEscolaTecnica(Boolean universitarioEscolaTecnica) {
		this.universitarioEscolaTecnica = universitarioEscolaTecnica;
	}

	public void setCartaoVacina(Boolean cartaoVacina) {
		this.cartaoVacina = cartaoVacina;
	}

	public void setFrequenciaEscolar(Boolean frequenciaEscolar) {
		this.frequenciaEscolar = frequenciaEscolar;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof FuncionarioDependenteVO) {
			FuncionarioDependenteVO objComparado = (FuncionarioDependenteVO) obj;

			if (getCpf().equals(objComparado.getCpf()) && getNome().equals(objComparado.getNome())
					&& getFuncionarioVO().getCodigo().equals(objComparado.getFuncionarioVO().getCodigo()))
				return true;

		}
		return false;
	}

	public Boolean getPensao() {
		if (pensao == null)
			pensao = false;
		return pensao;
	}

	public void setPensao(Boolean pensao) {
		this.pensao = pensao;
	}

	public Boolean getInss() {
		if (inss == null)
			inss = false;
		return inss;
	}

	public void setInss(Boolean inss) {
		this.inss = inss;
	}

	public Boolean getIrrf() {
		if (irrf == null)
			irrf = false;
		return irrf;
	}

	public void setIrrf(Boolean irrf) {
		this.irrf = irrf;
	}

	public Boolean getSalarioFamilia() {
		if (salarioFamilia == null)
			salarioFamilia = false;
		return salarioFamilia;
	}

	public void setSalarioFamilia(Boolean salarioFamilia) {
		this.salarioFamilia = salarioFamilia;
	}

	public String getTipo() {
		if (tipo == null)
			tipo = "";
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getPercentual() {
		if (percentual == null)
			percentual = new BigDecimal(0);
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public String getBanco() {
		if (banco == null)
			banco = "";
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		if (agencia == null)
			agencia = "";
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		if (conta == null)
			conta = "";
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getOperacao() {
		if (operacao == null)
			operacao = "";
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getNomeResponsavel() {
		if (nomeResponsavel == null)
			nomeResponsavel = "";
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public Date getDataInicioDesconto() {
		if (dataInicioDesconto == null)
			dataInicioDesconto = new Date();
		return dataInicioDesconto;
	}

	public void setDataInicioDesconto(Date dataInicioDesconto) {
		this.dataInicioDesconto = dataInicioDesconto;
	}

	public String getCpfResponsavel() {
		if (cpfResponsavel == null)
			cpfResponsavel = "";
		return cpfResponsavel;
	}

	public void setCpfResponsavel(String cpfResponsavel) {
		this.cpfResponsavel = cpfResponsavel;
	}

	public String getPensao_Apresentar() {
		if (getPensao()) {
			pensao_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Sim");
		} else {
			pensao_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Nao");
		}

		return pensao_Apresentar;
	}

	public void setPensao_Apresentar(String pensao_Apresentar) {
		this.pensao_Apresentar = pensao_Apresentar;
	}

	public String getInss_Apresentar() {
		if (getInss()) {
			inss_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Sim");
		} else {
			inss_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Nao");
		}
		return inss_Apresentar;
	}

	public void setInss_Apresentar(String inss_Apresentar) {
		this.inss_Apresentar = inss_Apresentar;
	}

	public String getIrrf_Apresentar() {
		if (getIrrf()) {
			irrf_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Sim");
		} else {
			irrf_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Nao");
		}
		return irrf_Apresentar;
	}

	public void setIrrf_Apresentar(String irrf_Apresentar) {
		this.irrf_Apresentar = irrf_Apresentar;
	}

	public String getSalarioFamilia_Apresentar() {
		if (getSalarioFamilia()) {
			salarioFamilia_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Sim");
		} else {
			salarioFamilia_Apresentar = UteisJSF.internacionalizar("prt_TextoPadrao_Nao");
		}
		return salarioFamilia_Apresentar;
	}

	public void setSalarioFamilia_Apresentar(String salarioFamilia_Apresentar) {
		this.salarioFamilia_Apresentar = salarioFamilia_Apresentar;
	}

	public Boolean getItemEmEdicao() {
		if (itemEmEdicao == null) {
			itemEmEdicao = false;
		}
		return itemEmEdicao;
	}

	public void setItemEmEdicao(Boolean itemEmEdicao) {
		this.itemEmEdicao = itemEmEdicao;
	}
	
	public FormulaFolhaPagamentoVO getFormulaCalculo() {
		if (formulaCalculo == null)
			formulaCalculo = new FormulaFolhaPagamentoVO();
		return formulaCalculo;
	}
	public void setFormulaCalculo(FormulaFolhaPagamentoVO formulaCalculo) {
		this.formulaCalculo = formulaCalculo;
	}
	
	public EventoFolhaPagamentoVO getEventoFolhaPagamento() {
		if(eventoFolhaPagamento == null)
			eventoFolhaPagamento = new EventoFolhaPagamentoVO();
		return eventoFolhaPagamento;
	}

	public void setEventoFolhaPagamento(EventoFolhaPagamentoVO eventoFolhaPagamento) {
		this.eventoFolhaPagamento = eventoFolhaPagamento;
	}
}