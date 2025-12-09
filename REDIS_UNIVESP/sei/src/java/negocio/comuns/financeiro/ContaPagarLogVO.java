package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ContaPagarLogVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String codOrigem;
    private String tipoOrigem;
    private String situacao;
    private Date dataVencimento;
    private Date dataFatoGerador;
    private Double valor;
    private Double valorPago;
    private Double valorPagamento;
    private Double juro;
    private Double multa;
    private Double desconto;
    private String nrDocumento;
    private String codigoBarra;
    private String parcela;
    private Integer origemRenegociacaoPagar;
    private Integer fornecedor;
    private Integer funcionario;
    private Integer banco;
    private Integer pessoa;
    private Integer responsavelFinanceiro;
    private Integer parceiro;
    private String matricula;
    private Integer centroDespesa;
    private Integer contaCorrente;
    private Integer unidadeEnsino;
    private Integer funcionarioCentroCusto;
    private Integer departamento;
    private Integer turma;
    private String tipoSacado;
    private String centroCusto;
    private String descricao;
    private Integer curso;
    private Integer turno;
    private Integer grupoContaPagar;
    private Integer responsavel;
    private String operacao;
    public static final long serialVersionUID = 1L;

    public ContaPagarLogVO() {
        super();
    }

    public String getCodOrigem() {
        if (codOrigem == null) {
            codOrigem = "";
        }
        return codOrigem;
    }

    public void setCodOrigem(String codOrigem) {
        this.codOrigem = codOrigem;
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

    public String getCodigoBarra() {
        if (codigoBarra == null) {
            codigoBarra = "";
        }
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public Integer getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = 0;
        }
        return contaCorrente;
    }

    public void setContaCorrente(Integer contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getFuncionario() {
        if (funcionario == null) {
            funcionario = 0;
        }
        return funcionario;
    }

    public void setFuncionario(Integer funcionario) {
        this.funcionario = funcionario;
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Integer getParceiro() {
        if (parceiro == null) {
            parceiro = 0;
        }
        return parceiro;
    }

    public void setParceiro(Integer parceiro) {
        this.parceiro = parceiro;
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public Integer getPessoa() {
        if (pessoa == null) {
            pessoa = 0;
        }
        return pessoa;
    }

    public void setPessoa(Integer pessoa) {
        this.pessoa = pessoa;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
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

    public Integer getResponsavel() {
        if (responsavel == null) {
            responsavel = 0;
        }
        return responsavel;
    }

    public void setResponsavel(Integer responsavel) {
        this.responsavel = responsavel;
    }

    public String getOperacao() {
        if (operacao == null) {
            operacao = "";
        }
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    
    public Integer getResponsavelFinanceiro() {
        if(responsavelFinanceiro == null){
            responsavelFinanceiro = 0;
        }
        return responsavelFinanceiro;
    }

    
    public void setResponsavelFinanceiro(Integer responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }

    /**
     * @return the tipoOrigem
     */
    public String getTipoOrigem() {
        if(tipoOrigem == null){
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
     * @return the dataFatoGerador
     */
    public Date getDataFatoGerador() {
        return dataFatoGerador;
    }

    /**
     * @param dataFatoGerador the dataFatoGerador to set
     */
    public void setDataFatoGerador(Date dataFatoGerador) {
        this.dataFatoGerador = dataFatoGerador;
    }

    /**
     * @return the valorPago
     */
    public Double getValorPago() {
        if (valorPago == null) {
            valorPago = 0.0;
        }
        return valorPago;
    }

    /**
     * @param valorPago the valorPago to set
     */
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    /**
     * @return the valorPagamento
     */
    public Double getValorPagamento() {
        if (valorPagamento == null) {
            valorPagamento = 0.0;
        }
        return valorPagamento;
    }

    /**
     * @param valorPagamento the valorPagamento to set
     */
    public void setValorPagamento(Double valorPagamento) {
        this.valorPagamento = valorPagamento;
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
     * @return the origemRenegociacaoPagar
     */
    public Integer getOrigemRenegociacaoPagar() {
        if (origemRenegociacaoPagar == null) {
            origemRenegociacaoPagar = 0;
        }
        return origemRenegociacaoPagar;
    }

    /**
     * @param origemRenegociacaoPagar the origemRenegociacaoPagar to set
     */
    public void setOrigemRenegociacaoPagar(Integer origemRenegociacaoPagar) {
        this.origemRenegociacaoPagar = origemRenegociacaoPagar;
    }

    /**
     * @return the fornecedor
     */
    public Integer getFornecedor() {
        if (fornecedor == null) {
            fornecedor = 0;
        }
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(Integer fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * @return the banco
     */
    public Integer getBanco() {
        if (fornecedor == null) {
            fornecedor = 0;
        }
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the centroDespesa
     */
    public Integer getCentroDespesa() {
        if (centroDespesa == null) {
            centroDespesa = 0;
        }
        return centroDespesa;
    }

    /**
     * @param centroDespesa the centroDespesa to set
     */
    public void setCentroDespesa(Integer centroDespesa) {
        this.centroDespesa = centroDespesa;
    }

    /**
     * @return the funcionarioCentroCusto
     */
    public Integer getFuncionarioCentroCusto() {
        if (funcionarioCentroCusto == null) {
            funcionarioCentroCusto = 0;
        }
        return funcionarioCentroCusto;
    }

    /**
     * @param funcionarioCentroCusto the funcionarioCentroCusto to set
     */
    public void setFuncionarioCentroCusto(Integer funcionarioCentroCusto) {
        this.funcionarioCentroCusto = funcionarioCentroCusto;
    }

    /**
     * @return the departamento
     */
    public Integer getDepartamento() {
        if (departamento == null) {
            departamento = 0;
        }
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the turma
     */
    public Integer getTurma() {
        if (turma == null) {
            turma = 0;
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    /**
     * @return the tipoSacado
     */
    public String getTipoSacado() {
        if (tipoSacado == null) {
            tipoSacado = "";
        }
        return tipoSacado;
    }

    /**
     * @param tipoSacado the tipoSacado to set
     */
    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
    }

    /**
     * @return the centroCusto
     */
    public String getCentroCusto() {
        if (centroCusto == null) {
            centroCusto = "";
        }
        return centroCusto;
    }

    /**
     * @param centroCusto the centroCusto to set
     */
    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the cursoVO
     */
    public Integer getCurso() {
        if (curso == null) {
            curso = 0;
        }
        return curso;
    }

    /**
     * @param cursoVO the cursoVO to set
     */
    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    /**
     * @return the turnoVO
     */
    public Integer getTurno() {
        if (turno == null) {
            turno = 0;
        }
        return turno;
    }

    /**
     * @param turnoVO the turnoVO to set
     */
    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    /**
     * @return the grupoContaPagar
     */
    public Integer getGrupoContaPagar() {
        if (grupoContaPagar == null) {
            grupoContaPagar = 0;
        }
        return grupoContaPagar;
    }

    /**
     * @param grupoContaPagar the grupoContaPagar to set
     */
    public void setGrupoContaPagar(Integer grupoContaPagar) {
        this.grupoContaPagar = grupoContaPagar;
    }

    
}
