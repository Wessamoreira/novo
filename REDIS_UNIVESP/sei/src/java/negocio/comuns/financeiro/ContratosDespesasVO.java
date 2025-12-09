package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContratosDespesas;
import negocio.comuns.utilitarias.dominios.TipoContratosDespesas;
import negocio.comuns.utilitarias.dominios.TipoSacado;

/**
 * Reponsável por manter os dados da entidade ContratosDespesas. Classe do tipo VO - Value Object composta pelos
 * atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ContratosDespesasVO extends SuperVO {

    private Integer codigo;
    private String tipoContrato;
    private String descricao;
    private Date dataInicio;
    private Date dataTermino;
    private Date dataPrimeiraParcela;
    private Date dataAprovacao;
    private Date dataCancelamento;
    private String motivoCancelamento;    
    private UsuarioVO responsavelAprovacao;    
    private UsuarioVO responsavelCancelamento;    
    private Boolean contratoIndeterminado;
    private String situacao;
    private String mesVencimento;
    private String diaVencimento;
    private Double valorParcela;
    private String centroCusto;
    /**
     * Atributo responsável por manter os objetos da classe <code>ContratoDespesaEspecifico</code>.
     */
    private List<ContratoDespesaEspecificoVO> contratoDespesaEspecificoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Fornecedor </code>.
     */
    private FornecedorVO fornecedor;
    private BancoVO banco;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>CategoriaDespesa </code>.
     */
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs;
//    private CategoriaDespesaVO categoriaDespesa;
//    private CategoriaDespesaVO centroDespesa;
    private ContaCorrenteVO contaCorrente;
    private UnidadeEnsinoVO unidadeEnsino;
    private String tipoSacado;
//    private DepartamentoVO departamento;
    private FuncionarioVO funcionario;
//    private FuncionarioVO funcionarioCentroCusto;
//    private TurmaVO turma;
    private PessoaVO pessoa;
//    private CursoVO cursoVO;
//    private TurnoVO turnoVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContratosDespesas</code>. Cria uma nova instância desta entidade, inicializando
     * automaticamente seus atributos (Classe VO).
     */
    public ContratosDespesasVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ContratosDespesasVO</code>. Todos os tipos
     * de consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o
     *                erro ocorrido.
     */
    public static void validarDados(ContratosDespesasVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		 if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
	            throw new ConsistirException("O campo UNIDADE DE ENSINO (Contratos de Despesas) deve ser informado.");
	        }
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo DESCRIÇÃO (Contrato de Despesa) deve ser informado.");
		}
		if (obj.getTipoSacado().equals("FO")) {
			obj.setBanco(null);
			obj.setFuncionario(null);
			if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {
				obj.setFornecedor(null);
				throw new ConsistirException("O campo FORNECEDOR (Contratos de Despesas) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("BA")) {
			obj.setFornecedor(null);
			obj.setFuncionario(null);
			if (obj.getBanco() == null || obj.getBanco().getCodigo().intValue() == 0) {
				obj.setBanco(null);
				throw new ConsistirException("O campo BANCO (Contratos de Despesas) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("FU")) {
			obj.setFornecedor(null);
			obj.setBanco(null);
			if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
				obj.setFuncionario(null);
				throw new ConsistirException("O campo FUNCIONÁRIO (Contratos de Despesas) deve ser informado.");
			}
		}
        if (obj.getTipoContrato().equals("")) {
            throw new ConsistirException("O campo TIPO CONTRATO (Contrato de Despesa) deve ser informado.");
        }
        if (!obj.getTipoContrato().equals("ES")) {
            if (obj.getDataInicio() == null) {
                throw new ConsistirException("O campo DATA INÍCIO (Contrato de Despesa) deve ser informado.");
            }
        }
        if (!obj.getContratoIndeterminado() && !obj.getTipoContrato().equals("ES")) {
            if (obj.getDataTermino() == null) {
                throw new ConsistirException("O campo DATA TÉRMINO (Contrato de Despesa) deve ser informado.");
            }
            if (obj.getDataInicio().after(obj.getDataTermino())
                    || obj.getDataInicio().compareTo(obj.getDataTermino()) > 0) {
                throw new ConsistirException("O campo DATA TERMINO deve ser maior que a DATA INÍCIO (Contrato de Despesa).");
            }
        }

        if ((obj.getSituacao().equals("IN")) && (obj.getDataTermino() == null)) {
            throw new ConsistirException("Para o INDEFERIMENTO de um contrato é obrigatório informar o campo DATA TÉRMINO (Contrato de Despesa).");
        }
        if (obj.getSituacao().equals("IN")) {
            obj.setContratoIndeterminado(false);
        }

        if (!obj.getTipoContrato().equals("ES")) {
            if (obj.getDiaVencimento().equals("")) {
                throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) deve ser informado.");
            }
            if (Integer.parseInt(obj.getDiaVencimento()) > 31 || Integer.parseInt(obj.getDiaVencimento()) < 1) {
                throw new ConsistirException("O campo DIA VENCIMENTO (Contrato de Despesa) é inválido.");
            }
        }

        if (obj.getTipoContrato().equals("AN")) {
            if (obj.getMesVencimento().equals("")) {
                throw new ConsistirException("O campo MÊS VENCIMENTO (Contrato de Despesa) deve ser informado.");
            }
            if (Integer.parseInt(obj.getMesVencimento()) > 12 || Integer.parseInt(obj.getMesVencimento()) < 1) {
                throw new ConsistirException("O campo MÊS VENCIMENTO (Contrato de Despesa) é inválido.");
            }
        }

        if (obj.getTipoContrato().equals("ES")) {
            if (obj.getContratoDespesaEspecificoVOs().isEmpty()) {
                throw new ConsistirException("Deve ser informado ao menos um contrato de despesa específico (Contrato de Despesa).");
            }
        }

//        if ((obj.getCategoriaDespesa() == null) || (obj.getCategoriaDespesa().getCodigo().intValue() == 0)) {
//            throw new ConsistirException("O campo CATEGORIA DE DESPESA (Contratos de Despesas) deve ser informado.");
//        }

       
        
        Uteis.checkState(obj.getListaCentroResultadoOrigemVOs().isEmpty(), "Deve ser informado pelo menos um Centro de Resultado Movimentação.");
        
		Uteis.checkState(!obj.getPorcentagemCentroResultadoTotal().equals(100.0), "O PERCENTUAL do Centro de Resultado Movimetação do CONTRATO DE DESPESA deve ser igual a 100%.");

//        if ((!obj.getCategoriaDespesa().getNivelCategoriaDespesa().equals("UE"))
//                && ((obj.getDepartamento() == null) || (obj.departamento.getCodigo().intValue() == 0))) {
//            throw new ConsistirException("O campo DEPARTAMENTO (Contratos de Despesas) deve ser informado.");
//        }
//
//        if ((obj.getCategoriaDespesa().getNivelCategoriaDespesa().equals("FU"))
//                && ((obj.getFuncionarioCentroCusto() == null) || (obj.getFuncionarioCentroCusto().getCodigo().intValue() == 0))) {
//            throw new ConsistirException("O campo FUNCIONÁRIO CENTRO CUSTO (Contratos de Despesas) deve ser informado.");
//        }
//        if (obj.getCategoriaDespesa().getInformarTurma().equals("CU") && (obj.getCursoVO().getCodigo() == null || obj.getCursoVO().getCodigo() == 0)) {
//           throw new ConsistirException("O campo CURSO (Conta à Pagar) deve ser informado.");
//        }
//        if (obj.getCategoriaDespesa().getInformarTurma().equals("CT") && (obj.getTurnoVO().getCodigo() == null || obj.getTurnoVO().getCodigo() == 0)) {
//           throw new ConsistirException("O campo CURSO/TURNO (Conta à Pagar) deve ser informado.");
//        }
//        if (obj.getCategoriaDespesa().getInformarTurma().equals("TU") && (obj.getTurma().getCodigo() == null || obj.getTurma().getCodigo() == 0)) {
//           throw new ConsistirException("O campo TURMA (Conta à Pagar) deve ser informado.");
//        }
    }

//    public boolean getApresentarDepartamento() {
//        try {
//            return ((getUnidadeEnsino().getCodigo() != 0) && (getCategoriaDespesa().getApresentarDepartamento()));
//        } catch (Exception e) {
//            return false;
//        }
//    }

//    public boolean getApresentaFuncionario() {
//        if (getUnidadeEnsino().getCodigo() != 0 && getCategoriaDespesa().getApresentarFuncionario()
//                && getDepartamento().getCodigo() != 0) {
//            return true;
//        }
//        return false;
//    }

    public boolean getApresentarContratoIndeterminado() {
        if (getSituacao().equals(SituacaoContratosDespesas.INDEFERIDO.getValor())) {
            return false;
        }
        if (getTipoContrato().equals(TipoContratosDespesas.ESPECIFICO.getValor())) {
            return false;
        }
        return true;
    }

//    public Boolean getApresentarTurma() throws Exception {
//        try {
//            return ((getUnidadeEnsino().getCodigo() != 0) && (getCategoriaDespesa().getApresentarTurma()));
//        } catch (Exception e) {
//            return false;
//        }
//    }

    public void inicializarUnidadeEnsinoLogado(UnidadeEnsinoVO unidadeEnsinoLogada) {
        if (unidadeEnsinoLogada.getCodigo().intValue() != 0) {
            setUnidadeEnsino(unidadeEnsinoLogada);
        }
    }

//    /**
//     * Retorna o objeto da classe <code>CategoriaDespesa</code> relacionado com (<code>ContratosDespesas</code>).
//     */
//    public CategoriaDespesaVO getCategoriaDespesa() {
//        if (categoriaDespesa == null) {
//            categoriaDespesa = new CategoriaDespesaVO();
//        }
//        return (categoriaDespesa);
//    }
//
//    /**
//     * Define o objeto da classe <code>CategoriaDespesa</code> relacionado com ( <code>ContratosDespesas</code>).
//     */
//    public void setCategoriaDespesa(CategoriaDespesaVO obj) {
//        this.categoriaDespesa = obj;
//    }
//
//    public CategoriaDespesaVO getCentroDespesa() {
//        if (centroDespesa == null) {
//            centroDespesa = new CategoriaDespesaVO();
//        }
//        return (centroDespesa);
//    }
//
//    /**
//     * Define o objeto da classe <code>CentroDespesa</code> relacionado com ( <code>ContaPagar</code>).
//     */
//    public void setCentroDespesa(CategoriaDespesaVO obj) {
//        this.centroDespesa = obj;
//    }

    /**
     * Operação responsável por adicionar um novo objeto da classe <code>ContratoDespesaEspecificoVO</code> ao List
     * <code>contratoDespesaEspecificoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>ContratoDespesaEspecifico</code> - getNrParcela() - como identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ContratoDespesaEspecificoVO</code> que será adiocionado ao Hashtable
     *            correspondente.
     */
    public void adicionarObjContratoDespesaEspecificoVOs(ContratoDespesaEspecificoVO obj) throws Exception {

        ContratoDespesaEspecificoVO.validarDados(obj);
        String dataVenc = String.valueOf(obj.getDataVencimento().getDate()) + "/"
                + String.valueOf(obj.getDataVencimento().getMonth()) + "/"
                + String.valueOf(obj.getDataVencimento().getYear());
        String dataIni = String.valueOf(getDataInicio().getDate()) + "/" + String.valueOf(getDataInicio().getMonth())
                + "/" + String.valueOf(getDataInicio().getYear());
        if (!dataVenc.equals(dataIni)) {
            if (obj.getDataVencimento().compareTo(getDataInicio()) == -1) {
                throw new ConsistirException("O campo DATA VENCIMENTO (Vencimento Específico) não pode ser menor que a data de inicio de contrato.");
            }
        }
        Iterator i = getContratoDespesaEspecificoVOs().iterator();
        while (i.hasNext()) {
            ContratoDespesaEspecificoVO objExistente = (ContratoDespesaEspecificoVO) i.next();
            if (objExistente.getDataVencimento().compareTo(obj.getDataVencimento()) == 0) {
                // getContratoDespesaEspecificoVOs().set(index, obj);
                throw new Exception("Já existe uma parcela para esta data de vencimento.");
            }

        }
        getContratoDespesaEspecificoVOs().add(obj);
        Ordenacao.ordenarLista(getContratoDespesaEspecificoVOs(), "ordenacao");
    }

    /**
     * Operação responsável por excluir um objeto da classe <code>ContratoDespesaEspecificoVO</code> no List
     * <code>contratoDespesaEspecificoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>ContratoDespesaEspecifico</code> - getNrParcela() - como identificador (key) do objeto no List.
     *
     * @param nrParcela
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjContratoDespesaEspecificoVOs(Integer nrParcela) throws Exception {
        int index = 0;
        Iterator<ContratoDespesaEspecificoVO> i = getContratoDespesaEspecificoVOs().iterator();
        while (i.hasNext()) {
            ContratoDespesaEspecificoVO objExistente = (ContratoDespesaEspecificoVO) i.next();
            if (objExistente.getNrParcela().equals(nrParcela)) {
                getContratoDespesaEspecificoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>ContratoDespesaEspecificoVO</code> no List
     * <code>contratoDespesaEspecificoVOs</code>. Utiliza o atributo padrão de consulta da classe
     * <code>ContratoDespesaEspecifico</code> - getNrParcela() - como identificador (key) do objeto no List.
     *
     * @param nrParcela
     *            Parâmetro para localizar o objeto do List.
     */
    public ContratoDespesaEspecificoVO consultarObjContratoDespesaEspecificoVO(Integer nrParcela) throws Exception {
        Iterator<ContratoDespesaEspecificoVO> i = getContratoDespesaEspecificoVOs().iterator();
        while (i.hasNext()) {
            ContratoDespesaEspecificoVO objExistente = (ContratoDespesaEspecificoVO) i.next();
            if (objExistente.getNrParcela().equals(nrParcela)) {
                return objExistente;
            }
        }
        return null;
    }

    public Boolean getAnual() {
        if (getTipoContrato().equals(TipoContratosDespesas.ANUAL.getValor())) {
            return true;
        }
        return false;
    }

    public Boolean getMensal() {
        if (getTipoContrato().equals(TipoContratosDespesas.MENSAL.getValor())) {
            return true;
        }
        return false;
    }

    public Boolean getEspecifico() {
        if (getTipoContrato().equals(TipoContratosDespesas.ESPECIFICO.getValor())) {
            return true;
        }
        return false;
    }

    public Boolean getAprovado() {
        if (getSituacao().equals(SituacaoContratosDespesas.APROVADO.getValor())) {
            return true;
        }
        return false;
    }
    public Boolean getCancelado() {
    	if (getSituacao().equals(SituacaoContratosDespesas.INDEFERIDO.getValor())) {
    		return true;
    	}
    	return false;
    }

    /**
     * Retorna o objeto da classe <code>Fornecedor</code> relacionado com ( <code>ContratoDespesa</code>).
     */
    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return (fornecedor);
    }

    public BancoVO getBanco() {
        if (banco == null) {
            banco = new BancoVO();
        }
        return (banco);
    }

//    public DepartamentoVO getDepartamento() {
//        if (departamento == null) {
//            departamento = new DepartamentoVO();
//        }
//        return (departamento);
//    }

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return (funcionario);
    }

//    public TurmaVO getTurma() {
//        if (turma == null) {
//            turma = new TurmaVO();
//        }
//        return (turma);
//    }

    /**
     * Define o objeto da classe <code>Fornecedor</code> relacionado com ( <code>ContratoDespesa</code>).
     */
    public void setFornecedor(FornecedorVO obj) {
        this.fornecedor = obj;
    }

    public void setBanco(BancoVO obj) {
        this.banco = obj;
    }

//    public void setDepartamento(DepartamentoVO obj) {
//        this.departamento = obj;
//    }
//
//    public void setTurma(TurmaVO obj) {
//        this.turma = obj;
//    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe <code>ContratoDespesaEspecifico</code>.
     */
    public List<ContratoDespesaEspecificoVO> getContratoDespesaEspecificoVOs() {
        if (contratoDespesaEspecificoVOs == null) {
            contratoDespesaEspecificoVOs = new ArrayList<ContratoDespesaEspecificoVO>();
        }
        return (contratoDespesaEspecificoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe <code>ContratoDespesaEspecifico</code>.
     */
    public void setContratoDespesaEspecificoVOs(List<ContratoDespesaEspecificoVO> contratoDespesaEspecificoVOs) {
        this.contratoDespesaEspecificoVOs = contratoDespesaEspecificoVOs;
    }

    public Double getValorParcela() {
        if (valorParcela == null) {
            valorParcela = 0.0;
        }
        return (valorParcela);
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getDiaVencimento() {
        if (diaVencimento == null) {
            diaVencimento = "";
        }
        return (diaVencimento);
    }

    public void setDiaVencimento(String diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public String getMesVencimento() {
        if (mesVencimento == null) {
            mesVencimento = "";
        }
        return (mesVencimento);
    }

    public void setMesVencimento(String mesVencimento) {
        this.mesVencimento = mesVencimento;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    public Date getDataAprovacao() {
        if (dataAprovacao == null) {
            dataAprovacao = new Date();
        }
        return dataAprovacao;
    }

    public void setDataAprovacao(Date dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public UsuarioVO getResponsavelAprovacao() {
        if (responsavelAprovacao == null) {
            responsavelAprovacao = new UsuarioVO();
        }
        return responsavelAprovacao;
    }

    public void setResponsavelAprovacao(UsuarioVO responsavelAprovacao) {
        this.responsavelAprovacao = responsavelAprovacao;
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no
     * valor de armazenamento do atributo esta função é capaz de retornar o de apresentação correspondente. Útil para
     * campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        return SituacaoContratosDespesas.getDescricao(situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getContratoIndeterminado() {
        if (contratoIndeterminado == null) {
            contratoIndeterminado = Boolean.FALSE;
        }
        return (contratoIndeterminado);
    }

    public Boolean isContratoIndeterminado() {
        return (contratoIndeterminado);
    }

    public void setContratoIndeterminado(Boolean contratoIndeterminado) {
        this.contratoIndeterminado = contratoIndeterminado;
    }

    public Date getDataTermino() {
        if (dataTermino == null) {
            dataTermino = new Date();
        }
        return (dataTermino);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataTermino_Apresentar() {
        return (Uteis.getData(dataTermino));
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return (dataInicio);
    }

    public Date getDataPrimeiraParcela() {
        if (dataPrimeiraParcela == null) {
            dataPrimeiraParcela = new Date();
        }
        return dataPrimeiraParcela;
    }

    public void setDataPrimeiraParcela(Date dataPrimeiraParcela) {
        this.dataPrimeiraParcela = dataPrimeiraParcela;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataInicio_Apresentar() {
        return (Uteis.getData(dataInicio));
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoContrato() {
        if (tipoContrato == null) {
            tipoContrato = "";
        }
        return (tipoContrato);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no
     * valor de armazenamento do atributo esta função é capaz de retornar o de apresentação correspondente. Útil para
     * campos como sexo, escolaridade, etc.
     */
    public String getTipoContrato_Apresentar() {
        return TipoContratosDespesas.getDescricao(tipoContrato);
    }

    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrenteVO contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Boolean getHabilitarComboBoxUnidadeEnsino(Integer unidadeEnsino) {
        if (unidadeEnsino == 0) {
            return false;
        }
        return true;
    }

    /**
     * @return the tipoSacado
     */
    public String getTipoSacado() {
        if (tipoSacado == null) {
            tipoSacado = TipoSacado.FORNECEDOR.getValor();
        }
        return tipoSacado;
    }

    /**
     * @param tipoSacado
     *            the tipoSacado to set
     */
    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
    }

    /**
     * @param funcionario
     *            the funcionario to set
     */
    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    /**
     * @param turma
     *            the turma to set
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public boolean getDadosBasicosDisponiveisSomenteConsulta() {
        if (this.getSituacao().equals(SituacaoContratosDespesas.APROVADO.getValor())
                || this.getSituacao().equals(SituacaoContratosDespesas.INDEFERIDO.getValor())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isValorDaParcelaAlteravel() {
        return SituacaoContratosDespesas.APROVADO.getValor().equals(getSituacao()) && !isNovoObj();
    }

//    public void setCursoVO(CursoVO cursoVO) {
//        this.cursoVO = cursoVO;
//    }
//
//    public CursoVO getCursoVO() {
//        if (cursoVO == null) {
//            cursoVO = new CursoVO();
//        }
//        return cursoVO;
//    }
//
//    public TurnoVO getTurnoVO() {
//        if (turnoVO == null) {
//            turnoVO = new TurnoVO();
//        }
//        return turnoVO;
//    }
//
//    public void setTurnoVO(TurnoVO turnoVO) {
//        this.turnoVO = turnoVO;
//    }

//    public String getCursoTurno() {
//        return (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) && (getTurnoVO().getCodigo() != null && getTurnoVO().getCodigo() != 0) ? getCursoVO().getNome().concat("-").concat(getTurnoVO().getNome()) : "";
//    }

	public String getMotivoCancelamento() {
		if(motivoCancelamento == null){
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public UsuarioVO getResponsavelCancelamento() {
		if(responsavelCancelamento == null){
			responsavelCancelamento = new UsuarioVO();
		}
		return responsavelCancelamento;
	}

	public void setResponsavelCancelamento(UsuarioVO responsavelCancelamento) {
		this.responsavelCancelamento = responsavelCancelamento;
	}

	public String getCentroCusto() {
		if(centroCusto == null){
			centroCusto = "";
		}
		return centroCusto;
	}

	public void setCentroCusto(String centroCusto) {
		this.centroCusto = centroCusto;
	}

//	public FuncionarioVO getFuncionarioCentroCusto() {
//		if(funcionarioCentroCusto == null){
//			funcionarioCentroCusto = new FuncionarioVO();
//		}
//		return funcionarioCentroCusto;
//	}
//
//	public void setFuncionarioCentroCusto(FuncionarioVO funcionarioCentroCusto) {
//		this.funcionarioCentroCusto = funcionarioCentroCusto;
//	}
//    
	@Override
	public String toString() {
		return "Contratos de Despesas [" + this.getCodigo() + "]: " + 
                        " Descrição: " + this.getDescricao() + 
                        " Situação: " + this.getSituacao_Apresentar() + 
                        " Tipo Sacado: " + this.getTipoSacado() +
                        " Fornecedor: " + this.getFornecedor().getCodigo() + " " +  this.getFornecedor().getNome() +
                        " Funcionario: " + this.getFuncionario().getCodigo() + 
                        " Valor Parcela: " + Uteis.getDoubleFormatado(this.getValorParcela());
	}

	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigemVOs() {
		if(listaCentroResultadoOrigemVOs == null){
			listaCentroResultadoOrigemVOs = new ArrayList<CentroResultadoOrigemVO>(0);
		}
		return listaCentroResultadoOrigemVOs;
	}

	public void setListaCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs) {
		this.listaCentroResultadoOrigemVOs = listaCentroResultadoOrigemVOs;
	}	
	
	
	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).sum();
	}

	public Double getPorcentagemCentroResultadoTotal() {
		return Uteis.arrendondarForcandoCadasDecimais(getListaCentroResultadoOrigemVOs().stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8)), 8);
	}

	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}    
}
