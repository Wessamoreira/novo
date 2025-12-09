package negocio.comuns.financeiro;

 
import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.LayoutArquivoIntegracaoFinanceiraEnum;

public class ProcessamentoIntegracaoFinanceiraDetalheVO extends SuperVO {
	private Integer codigo;
	private String codigoPessoaFinanceiro;
	private String nossoNumero;
	private String controleCliente;
	private String tipoOrigem;
	private String nome;
	private String curso;
	private String endereco;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private String cpf;
	private String mes;
	private String ano;
	private Double valor;
	private Double bolsa;
	private Date dataVencimentoBolsa;
	private Double descontoPontualidade1;
	private Date dataVencimentoDescPontualidade1;
	private Double descontoPontualidade2;
	private Date dataVencimentoDescPontualidade2;
	private Double descontoPontualidade3;
	private Date dataVencimentoDescPontualidade3;
	private Double descontoPontualidade4;
	private Date dataVencimentoDescPontualidade4;
	private Double desconto;
	private Double juro;
	private Double multa;
	private Double acrescimo;
	private Double valorReceber;
	private Date dataMaximaPagamento;
	private Date dataVencimento;
	private Date dataCompetencia;
	private String codigoSicredi;
	private Boolean possuiPendenciasFinanceirasExternas;	
	private String conteudoLinhaProcessada;
	private boolean contaReceberProcessada;
	private Boolean contaReceberProcessadaErro;
	private String motivoErroProcessamento;
    private ProcessamentoIntegracaoFinanceiroVO integracaoFinanceiro;
    private Integer contaReceber;
    private Boolean mudouCodigoFinanceiro;
    private Boolean possuiFies;
    private String jurosApresentar;
    private String multaApresentar;
    private LayoutArquivoIntegracaoFinanceiraEnum tipoLayoutArquivo;
    
    
	public static final long serialVersionUID = 1L;

	public ProcessamentoIntegracaoFinanceiraDetalheVO() {

	}
	
	

	/**
	 * @return the codigoPessoaFinanceiro
	 */
	public String getCodigoPessoaFinanceiro() {
		if (codigoPessoaFinanceiro == null) {
			codigoPessoaFinanceiro = "";
		}
		return codigoPessoaFinanceiro;
	}



	/**
	 * @param codigoPessoaFinanceiro the codigoPessoaFinanceiro to set
	 */
	public void setCodigoPessoaFinanceiro(String codigoPessoaFinanceiro) {
		this.codigoPessoaFinanceiro = codigoPessoaFinanceiro;
	}



	/**
	 * @return the nome
	 */
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}



	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}



	/**
	 * @return the curso
	 */
	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}



	/**
	 * @param curso the curso to set
	 */
	public void setCurso(String curso) {
		this.curso = curso;
	}



	/**
	 * @return the endereco
	 */
	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}



	/**
	 * @param endereco the endereco to set
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}



	/**
	 * @return the numero
	 */
	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}



	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}



	/**
	 * @return the complemento
	 */
	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}



	/**
	 * @param complemento the complemento to set
	 */
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}



	/**
	 * @return the bairro
	 */
	public String getBairro() {
		if (bairro == null) {
			bairro = "";
		}
		return bairro;
	}



	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}



	/**
	 * @return the cidade
	 */
	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}



	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}



	/**
	 * @return the uf
	 */
	public String getUf() {
		if (uf == null) {
			uf = "";
		}
		return uf;
	}



	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}



	/**
	 * @return the cep
	 */
	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}



	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}



	/**
	 * @return the mes
	 */
	public String getMes() {
		if (mes == null) {
			mes = "";
		}
		return mes;
	}



	/**
	 * @param mes the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}



	/**
	 * @return the ano
	 */
	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}



	/**
	 * @param ano the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}



	/**
	 * @return the valor
	 */
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}



	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}



	/**
	 * @return the bolsa
	 */
	public Double getBolsa() {
		if (bolsa == null) {
			bolsa = 0.0;
		}
		return bolsa;
	}



	/**
	 * @param bolsa the bolsa to set
	 */
	public void setBolsa(Double bolsa) {
		this.bolsa = bolsa;
	}



	/**
	 * @return the desconto
	 */
	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}



	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}



	/**
	 * @return the juro
	 */
	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return juro;
	}



	/**
	 * @param juro the juro to set
	 */
	public void setJuro(Double juro) {
		this.juro = juro;
	}



	/**
	 * @return the multa
	 */
	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return multa;
	}



	/**
	 * @param multa the multa to set
	 */
	public void setMulta(Double multa) {
		this.multa = multa;
	}



	/**
	 * @return the acrescimo
	 */
	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}



	/**
	 * @param acrescimo the acrescimo to set
	 */
	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}



	/**
	 * @return the valorReceber
	 */
	public Double getValorReceber() {
		if (valorReceber == null) {
			valorReceber = 0.0;
		}
		return valorReceber;
	}



	/**
	 * @param valorReceber the valorReceber to set
	 */
	public void setValorReceber(Double valorReceber) {
		this.valorReceber = valorReceber;
	}



	/**
	 * @return the dataMaximaPagamento
	 */
	public Date getDataMaximaPagamento() {		
		return dataMaximaPagamento;
	}



	/**
	 * @param dataMaximaPagamento the dataMaximaPagamento to set
	 */
	public void setDataMaximaPagamento(Date dataMaximaPagamento) {
		this.dataMaximaPagamento = dataMaximaPagamento;
	}



	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {		
		return dataVencimento;
	}



	/**
	 * @param dataVencimento the dataVencimento to set
	 */
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}



	/**
	 * @return the codigoSicredi
	 */
	public String getCodigoSicredi() {
		if (codigoSicredi == null) {
			codigoSicredi = "";
		}
		return codigoSicredi;
	}



	/**
	 * @param codigoSicredi the codigoSicredi to set
	 */
	public void setCodigoSicredi(String codigoSicredi) {
		this.codigoSicredi = codigoSicredi;
	}



	/**
	 * @return the suspenderMatricula
	 */
	public Boolean getPossuiPendenciasFinanceirasExternas() {
		if (possuiPendenciasFinanceirasExternas == null) {
			possuiPendenciasFinanceirasExternas = false;
		}
		return possuiPendenciasFinanceirasExternas;
	}



	/**
	 * @param suspenderMatricula the suspenderMatricula to set
	 */
	public void setPossuiPendenciasFinanceirasExternas(Boolean suspenderMatricula) {
		this.possuiPendenciasFinanceirasExternas = suspenderMatricula;
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
 
	public ProcessamentoIntegracaoFinanceiroVO getIntegracaoFinanceiro() {
		if (integracaoFinanceiro == null) {
			integracaoFinanceiro = new ProcessamentoIntegracaoFinanceiroVO();
		}
		return integracaoFinanceiro;
	}

	public void setIntegracaoFinanceiro(
			ProcessamentoIntegracaoFinanceiroVO integracaoFinanceiro) {
		this.integracaoFinanceiro = integracaoFinanceiro;
	}
	public String getConteudoLinhaProcessada() {
		return conteudoLinhaProcessada;
	}

	public void setConteudoLinhaProcessada(String conteudoLinhaProcessada) {
		this.conteudoLinhaProcessada = conteudoLinhaProcessada;
	}

	public boolean isContaReceberProcessada() {
		return contaReceberProcessada;
	}

	public void setContaReceberProcessada(boolean contaReceberProcessada) {
		this.contaReceberProcessada = contaReceberProcessada;
	}



	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}



	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}



	/**
	 * @return the tipoOrigem
	 */
	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}



	/**
	 * @param tipoOrigem the tipoOrigem to set
	 */
	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}



	/**
	 * @return the dataCompetencia
	 */
	public Date getDataCompetencia() {		
		return dataCompetencia;
	}



	/**
	 * @param dataCompetencia the dataCompetencia to set
	 */
	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}



	/**
	 * @return the cpf
	 */
	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}



	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	/**
	 * @return the contaReceberProcessadaErro
	 */
	public Boolean getContaReceberProcessadaErro() {
		if (contaReceberProcessadaErro == null) {
			contaReceberProcessadaErro = false;
		}
		return contaReceberProcessadaErro;
	}



	/**
	 * @param contaReceberProcessadaErro the contaReceberProcessadaErro to set
	 */
	public void setContaReceberProcessadaErro(Boolean contaReceberProcessadaErro) {
		this.contaReceberProcessadaErro = contaReceberProcessadaErro;
	}



	/**
	 * @return the motivoErroProcessamento
	 */
	public String getMotivoErroProcessamento() {
		if (motivoErroProcessamento == null) {
			motivoErroProcessamento = "";
		}
		return motivoErroProcessamento;
	}



	/**
	 * @param motivoErroProcessamento the motivoErroProcessamento to set
	 */
	public void setMotivoErroProcessamento(String motivoErroProcessamento) {
		this.motivoErroProcessamento = motivoErroProcessamento;
	}
	
	public String getSemestre(){
		return "01, 02, 03, 04, 05, 06".contains(getMes())? "1" : "2";
	}



	/**
	 * @return the contaReceber
	 */
	public Integer getContaReceber() {
		if (contaReceber == null) {
			contaReceber = 0;
		}
		return contaReceber;
	}



	/**
	 * @param contaReceber the contaReceber to set
	 */
	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
	}



	/**
	 * @return the mudouCodigoFinanceiro
	 */
	public Boolean getMudouCodigoFinanceiro() {
		if (mudouCodigoFinanceiro == null) {
			mudouCodigoFinanceiro = false;
		}
		return mudouCodigoFinanceiro;
	}



	/**
	 * @param mudouCodigoFinanceiro the mudouCodigoFinanceiro to set
	 */
	public void setMudouCodigoFinanceiro(Boolean mudouCodigoFinanceiro) {
		this.mudouCodigoFinanceiro = mudouCodigoFinanceiro;
	}
	


	public String getControleCliente() {
		if(controleCliente == null){
			controleCliente = "";
		}
		return controleCliente;
	}



	public void setControleCliente(String controleCliente) {
		this.controleCliente = controleCliente;
	}



	public Date getDataVencimentoBolsa() {		
		return dataVencimentoBolsa;
	}



	public void setDataVencimentoBolsa(Date dataVencimentoBolsa) {
		this.dataVencimentoBolsa = dataVencimentoBolsa;
	}



	public Double getDescontoPontualidade1() {
		if(descontoPontualidade1 == null){
			descontoPontualidade1 = 0.0;
		}
		return descontoPontualidade1;
	}



	public void setDescontoPontualidade1(Double descontoPontualidade1) {
		this.descontoPontualidade1 = descontoPontualidade1;
	}



	public Date getDataVencimentoDescPontualidade1() {
		return dataVencimentoDescPontualidade1;
	}



	public void setDataVencimentoDescPontualidade1(Date dataVencimentoDescPontualidade1) {
		this.dataVencimentoDescPontualidade1 = dataVencimentoDescPontualidade1;
	}



	public Double getDescontoPontualidade2() {
		if(descontoPontualidade2 == null){
			descontoPontualidade2 = 0.0;
		}
		return descontoPontualidade2;
	}



	public void setDescontoPontualidade2(Double descontoPontualidade2) {
		this.descontoPontualidade2 = descontoPontualidade2;
	}



	public Date getDataVencimentoDescPontualidade2() {
		return dataVencimentoDescPontualidade2;
	}



	public void setDataVencimentoDescPontualidade2(Date dataVencimentoDescPontualidade2) {
		this.dataVencimentoDescPontualidade2 = dataVencimentoDescPontualidade2;
	}



	public Double getDescontoPontualidade3() {
		if(descontoPontualidade3 == null){
			descontoPontualidade3 = 0.0;
		}
		return descontoPontualidade3;
	}



	public void setDescontoPontualidade3(Double descontoPontualidade3) {
		this.descontoPontualidade3 = descontoPontualidade3;
	}



	public Date getDataVencimentoDescPontualidade3() {
		return dataVencimentoDescPontualidade3;
	}



	public void setDataVencimentoDescPontualidade3(Date dataVencimentoDescPontualidade3) {
		this.dataVencimentoDescPontualidade3 = dataVencimentoDescPontualidade3;
	}



	public Double getDescontoPontualidade4() {
		if(descontoPontualidade4 == null){
			descontoPontualidade4 = 0.0;
		}
		return descontoPontualidade4;
	}



	public void setDescontoPontualidade4(Double descontoPontualidade4) {
		this.descontoPontualidade4 = descontoPontualidade4;
	}



	public Date getDataVencimentoDescPontualidade4() {
		return dataVencimentoDescPontualidade4;
	}



	public void setDataVencimentoDescPontualidade4(Date dataVencimentoDescPontualidade4) {
		this.dataVencimentoDescPontualidade4 = dataVencimentoDescPontualidade4;
	}



	public String getJurosApresentar() {
		if(jurosApresentar == null){
			jurosApresentar = "";
		}
		return jurosApresentar;
	}



	public void setJurosApresentar(String jurosApresentar) {
		this.jurosApresentar = jurosApresentar;
	}



	public String getMultaApresentar() {
		if(multaApresentar == null){
			multaApresentar = "";
		}
		return multaApresentar;
	}



	public void setMultaApresentar(String multaApresentar) {
		this.multaApresentar = multaApresentar;
	}



	public LayoutArquivoIntegracaoFinanceiraEnum getTipoLayoutArquivo() {
		if(tipoLayoutArquivo == null){
			tipoLayoutArquivo = LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_GERAL;	
		}		
		return tipoLayoutArquivo;
	}



	public void setTipoLayoutArquivo(LayoutArquivoIntegracaoFinanceiraEnum tipoLayoutArquivo) {
		this.tipoLayoutArquivo = tipoLayoutArquivo;
	}

	public Boolean getPossuiFies() {
		if (possuiFies == null) {
			possuiFies = Boolean.FALSE;
		}
		return possuiFies;
	}

	public void setPossuiFies(Boolean possuiFies) {
		this.possuiFies = possuiFies;
	}
	
	
	
		
	
}
