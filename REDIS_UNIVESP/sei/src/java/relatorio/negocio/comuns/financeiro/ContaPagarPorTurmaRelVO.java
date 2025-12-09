package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ContaPagarPorTurmaRelVO {

    private Integer codigoContaPagar;
    private Date dataContaPagar;
    private String situacao;
    private Date dataVencimento;
    private Double valorContaPagar;
    private Double valorPago;
    private String tipoSacado;
    private String descricaoCentroDespesa;
    private Integer unidadeEnsino;
    private String nomeUnidadeEnsino;
    private String nomeTurma;
    private String funcionario;
    private String fornecedor;
    private String favorecido;
    private String banco;
    private String numeroDocumento;
    private Double juro;
    private Double multa;
    private Double desconto;
    private String quebra;
    private Date dataInicio;
    private Date dataFim;
    private String categoriaDespesa;
    private Integer codigoCategoriaDespesa;
    private String nomeFuncionario;
    private String nomeFornecedor;
    private String nomeBanco;
    private Integer codigoFuncionario;
    private Integer codigoFornecedor;
    private Integer codigoBanco;
    private String fornecedorCpfCnpj;
    private Integer categoriaDespesaPrincipal;
    private String identificadorCategoriaDespesa;
    private String filtroData;
    private String ordernarPor;
    private List<ContaPagarCategoriaDespesaRelVO> listaContaPagarCategoriaDespesaRelVO;
    private String nomeAluno;
    private String matriculaAluno;
    private String nomeResponsavelFinanceiro;
    private String nomeParceiro;
    private String aluno;
    private String responsavelFinanceiro;
    private String parceiro;
    private Integer codigoResponsavelFinanceiro;
    private Integer codigoParceiro;
    private Integer codigoAluno;

    public ContaPagarPorTurmaRelVO() {
        setBanco("");
        setCodigoContaPagar(0);
        setDataContaPagar(new Date());
        setDesconto(0.0);
        setFornecedor("");
        setFuncionario("");
        setJuro(0.0);
        setMulta(0.0);
        setNumeroDocumento("");
        setTipoSacado("");
        setUnidadeEnsino(0);
        setNomeUnidadeEnsino("");
        setValorPago(0.0);
        setDataVencimento(new Date());
        setDescricaoCentroDespesa("");
        setNomeTurma("");
        setSituacao("");
        setValorContaPagar(0.0);
        setQuebra("");
        setBanco("");
        setFavorecido("");
    }

    public String getTipoSacado() {
        return tipoSacado;
    }

    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public String getNomeUnidadeEnsino() {
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Date getDataContaPagar() {
        return dataContaPagar;
    }

    public void setDataContaPagar(Date dataContaPagar) {
        this.dataContaPagar = dataContaPagar;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Integer getCodigoContaPagar() {
        return codigoContaPagar;
    }

    public void setCodigoContaPagar(Integer codigoContaPagar) {
        this.codigoContaPagar = codigoContaPagar;
    }

    public Double getJuro() {
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getMulta() {
        return multa;
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getValorContaPagar() {
        return valorContaPagar;
    }

    public void setValorContaPagar(Double valorContaPagar) {
        this.valorContaPagar = valorContaPagar;
    }

    public String getDescricaoCentroDespesa() {
        return descricaoCentroDespesa;
    }

    public void setDescricaoCentroDespesa(String descricaoCentroDespesa) {
        this.descricaoCentroDespesa = descricaoCentroDespesa;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public void setQuebra(String quebra) {
        this.quebra = quebra;
    }

    public String getQuebra() {
        return quebra;
    }

    public JRDataSource getListaContaPagarCategoriaDespesaRelVOJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaContaPagarCategoriaDespesaRelVO().toArray());
        return jr;
    }

    public List<ContaPagarCategoriaDespesaRelVO> getListaContaPagarCategoriaDespesaRelVO() {
        if (listaContaPagarCategoriaDespesaRelVO == null) {
            listaContaPagarCategoriaDespesaRelVO = new ArrayList<ContaPagarCategoriaDespesaRelVO>(0);
        }
        return listaContaPagarCategoriaDespesaRelVO;
    }

    public void setListaContaPagarCategoriaDespesaRelVO(List<ContaPagarCategoriaDespesaRelVO> listaContaPagarCategoriaDespesaRelVO) {
        this.listaContaPagarCategoriaDespesaRelVO = listaContaPagarCategoriaDespesaRelVO;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getCategoriaDespesa() {
        return categoriaDespesa;
    }

    public void setCategoriaDespesa(String categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public Integer getCodigoFuncionario() {
        return codigoFuncionario;
    }

    public void setCodigoFuncionario(Integer codigoFuncionario) {
        this.codigoFuncionario = codigoFuncionario;
    }

    public Integer getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(Integer codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    public Integer getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(Integer codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getFornecedorCpfCnpj() {
        return fornecedorCpfCnpj;
    }

    public void setFornecedorCpfCnpj(String fornecedorCpfCnpj) {
        this.fornecedorCpfCnpj = fornecedorCpfCnpj;
    }

    public Integer getCodigoCategoriaDespesa() {
        return codigoCategoriaDespesa;
    }

    public void setCodigoCategoriaDespesa(Integer codigoCategoriaDespesa) {
        this.codigoCategoriaDespesa = codigoCategoriaDespesa;
    }

    public Integer getCategoriaDespesaPrincipal() {
        return categoriaDespesaPrincipal;
    }

    public void setCategoriaDespesaPrincipal(Integer categoriaDespesaPrincipal) {
        this.categoriaDespesaPrincipal = categoriaDespesaPrincipal;
    }

    public String getIdentificadorCategoriaDespesa() {
        return identificadorCategoriaDespesa;
    }

    public void setIdentificadorCategoriaDespesa(String identificadorCategoriaDespesa) {
        this.identificadorCategoriaDespesa = identificadorCategoriaDespesa;
    }

    /**
     * @return the favorecido
     */
    public String getFavorecido() {
        return favorecido;
    }

    /**
     * @param favorecido the favorecido to set
     */
    public void setFavorecido(String favorecido) {
        this.favorecido = favorecido;
    }

    public String getFiltroData() {
        if (filtroData == null) {
            filtroData = "";
        }
        return filtroData;
    }

    public void setFiltroData(String filtroData) {
        this.filtroData = filtroData;
    }

    public String getOrdernarPor() {
        if (ordernarPor == null) {
            ordernarPor = "";
        }
        return ordernarPor;
    }

    public void setOrdernarPor(String ordernarPor) {
        this.ordernarPor = ordernarPor;
    }
    
    
    public String getResponsavelFinanceiro() {
        return responsavelFinanceiro;
    }

    
    public void setResponsavelFinanceiro(String responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }

    
    public String getParceiro() {
        return parceiro;
    }

    
    public void setParceiro(String parceiro) {
        this.parceiro = parceiro;
    }

    
    public Integer getCodigoResponsavelFinanceiro() {
        return codigoResponsavelFinanceiro;
    }

    
    public void setCodigoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro) {
        this.codigoResponsavelFinanceiro = codigoResponsavelFinanceiro;
    }

    
    public Integer getCodigoParceiro() {
        return codigoParceiro;
    }

    
    public void setCodigoParceiro(Integer codigoParceiro) {
        this.codigoParceiro = codigoParceiro;
    }

    
    public String getAluno() {
        return aluno;
    }

    
    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    
    public Integer getCodigoAluno() {
        return codigoAluno;
    }

    
    public void setCodigoAluno(Integer codigoAluno) {
        this.codigoAluno = codigoAluno;
    }

    
    public String getNomeAluno() {
        return nomeAluno;
    }

    
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    
    public String getMatriculaAluno() {
        return matriculaAluno;
    }

    
    public void setMatriculaAluno(String matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    
    public String getNomeResponsavelFinanceiro() {
        return nomeResponsavelFinanceiro;
    }

    
    public void setNomeResponsavelFinanceiro(String nomeResponsavelFinanceiro) {
        this.nomeResponsavelFinanceiro = nomeResponsavelFinanceiro;
    }

    
    public String getNomeParceiro() {
        return nomeParceiro;
    }

    
    public void setNomeParceiro(String nomeParceiro) {
        this.nomeParceiro = nomeParceiro;
    }
    
    
}
