package negocio.comuns.financeiro;

/**
 * Reponsável por manter os dados da entidade Parceiro. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see EmpresaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.bancocurriculum.AreaProfissionalParceiroVO;
import negocio.comuns.basico.EmpresaVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "parceiroVO")
public class ParceiroVO extends EmpresaVO implements PossuiEndereco, Serializable {

    private String descricaoEmpresa;
    private String tipoParceiro;
    private Boolean participaBancoCurriculum;
    private String inscricaoMunicipal;
    private Boolean mostrarModalSucesso; //Controlar tela
    private Boolean empresaSigilosaParaVaga;
    private Date dataAceitouParticiparBancoCurriculum; 
    private Boolean isentarTaxaBoleto;
    private boolean isentarJuro = false;
    private boolean isentarMulta = false;
    private boolean isentarReajusteParcelaVencida = false;
    private boolean financiamentoProprio = false;
    private boolean emitirBoletoEmNomeBeneficiado = false;
    private Boolean permiteEmitirBoletoAlunoVinculadoParceiro;
    private Boolean permiteRemessaBoletoAlunoVinculadoParceiro;
    private Integer numeroParcelaVencidasSuspenderFinanciamento;
    private List<ParceiroUnidadeEnsinoContaCorrenteVO> listaParceiroUnidadeEnsinoContaCorrente;
    private FormaPagamentoVO formaPagamentoRecebimento;
    private CategoriaDespesaVO categoriaDespesa;
    private FormaPagamentoVO formaPagamento;
    
    // Campo que indicar que a empresa possui convenio de estágio assinado com
    // com a instituicao de ensino. Por isto, pode ser utilizada no módulo de estágio
    // (no Academico) para registro de estágios dos alunos
    private Boolean conveniadaParaVagasEstagio;
    private Date dataInicioConvenioEstagio;
    private Date dataFinalConvenioEstagio;
    
    private Integer nrAcordoConvenio;
    private String registroConselho;
    
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ContatoParceiro</code>.
     */
    private List<ContatoParceiroVO> contatoParceiroVOs;
    private Boolean emitirNotaFiscalParaBeneficiario;
    private String orgaoRegistroConselho;
    private List<AreaProfissionalParceiroVO> areaProfissionalParceiroVOs;
    
    private String nomeBanco;
	private String numeroBancoRecebimento;
	private String numeroAgenciaRecebimento;
	private String digitoAgenciaRecebimento;
	private String contaCorrenteRecebimento;
	private String digitoCorrenteRecebimento;
	private boolean validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula = false;
	private boolean custeaParcelasMaterialDidatico = false;
	private Boolean considerarValorDescontoDeclaracaoImpostoRendaAluno;
	private Boolean realizarReajustePrecoComBaseIndiceReajuste;
	private Boolean considerarParcelasMaterialDidaticoReajustePreco;
	private boolean validarDebitoFinanceiroAoIncluirConvenioMatricula=false;
	private String chaveEnderecamentoPix;
	private TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum;	
	
	public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Parceiro</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ParceiroVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ParceiroVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ParceiroVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getParceiroJuridico()) {
            if (!Uteis.isAtributoPreenchido(obj.getTipoParceiro())) {
                throw new ConsistirException("O campo TIPO PARCEIRO (Documentos Pessoais) deve ser informado.");
            }
        }
        if (obj.getParceiroJuridico().booleanValue() && obj.getCNPJ().equals("")) {
        	throw new ConsistirException("O campo CNPJ (Documentos Pessoais) deve ser informado.");
        }
        if (obj.getParceiroFisico() && !Uteis.isAtributoPreenchido(obj.getRegistroConselho())) {
        	throw new ConsistirException("O campo REGISTRO CONSELHO (Parceiro) deve ser informado.");
        }
        if (obj.getParceiroFisico() && !Uteis.isAtributoPreenchido(obj.getOrgaoRegistroConselho())) {
        	throw new ConsistirException("O campo ORGÃO REGISTRO CONSELHO (Parceiro) deve ser informado.");
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Documentos Pessoais) deve ser informado.");
        }
        if (obj.getParceiroJuridico().booleanValue() && obj.getRazaoSocial().equals("")) {
            throw new ConsistirException("O campo RAZÃO SOCIAL (Documentos Pessoais) deve ser informado.");
        }
        if (obj.getEndereco().equals("")) {
            throw new ConsistirException("O campo ENDEREÇO (Dados de Endereço) deve ser informado.");
        }
        if (obj.getSetor().equals("")) {
            throw new ConsistirException("O campo BAIRRO/SETOR (Dados de Endereço) deve ser informado.");
        }
        if (obj.getCidade().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CIDADE (Dados de Endereço) deve ser informado.");
        }
        if (obj.getTelComercial1().equals("")) {
            throw new ConsistirException("O campo TELEFONE COMERCIAL (Parceiro) deve ser informado.");
        }
//        if (obj.getContatoParceiroVOs().isEmpty()) {
//            throw new ConsistirException("Deve-se adicionar ao menos 1 CONTATO EMPRESA (Contato Empresa).");
//        }
        if (obj.getParceiroFisico()) {
        	obj.setCNPJ("");
        	obj.setTipoParceiro("");
        	obj.setInscEstadual("");
        } else if (obj.getParceiroJuridico()) {
        	obj.setCPF("");
        	obj.setRG("");
        	obj.setRegistroConselho("");
        	obj.setOrgaoRegistroConselho("");
        }
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ContatoParceiroVO</code> ao List <code>contatoParceiroVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ContatoParceiro</code> - getNome() - como identificador (key) do
     * objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ContatoParceiroVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjContatoParceiroVOs(ContatoParceiroVO obj) throws Exception {
        ContatoParceiroVO.validarDados(obj);
        int index = 0;
        Iterator i = getContatoParceiroVOs().iterator();
        while (i.hasNext()) {
            ContatoParceiroVO objExistente = (ContatoParceiroVO) i.next();
            if (objExistente.getNome().equals(obj.getNome())) {
                getContatoParceiroVOs().set(index, obj);
                return;
            }
            index++;
        }
        getContatoParceiroVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ContatoParceiroVO</code> no List <code>contatoParceiroVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ContatoParceiro</code> - getNome() - como identificador (key) do
     * objeto no List.
     *
     * @param nome
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjContatoParceiroVOs(String nome) throws Exception {
        int index = 0;
        Iterator i = getContatoParceiroVOs().iterator();
        while (i.hasNext()) {
            ContatoParceiroVO objExistente = (ContatoParceiroVO) i.next();
            if (objExistente.getNome().equals(nome)) {
                getContatoParceiroVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ContatoParceiroVO</code> no List <code>contatoParceiroVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ContatoParceiro</code> - getNome() - como identificador (key) do
     * objeto no List.
     *
     * @param nome
     *            Parâmetro para localizar o objeto do List.
     */
    public ContatoParceiroVO consultarObjContatoParceiroVO(String nome) throws Exception {
        Iterator i = getContatoParceiroVOs().iterator();
        while (i.hasNext()) {
            ContatoParceiroVO objExistente = (ContatoParceiroVO) i.next();
            if (objExistente.getNome().equals(nome)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ContatoParceiro</code>.
     */
    public List<ContatoParceiroVO> getContatoParceiroVOs() {
        if (contatoParceiroVOs == null) {
            contatoParceiroVOs = new ArrayList<ContatoParceiroVO>(0);
        }
        return (contatoParceiroVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ContatoParceiro</code>.
     */
    public void setContatoParceiroVOs(List contatoParceiroVOs) {
        this.contatoParceiroVOs = contatoParceiroVOs;
    }

    public String getTipoParceiro() {
        if (tipoParceiro == null) {
            tipoParceiro = "";
        }
        return (tipoParceiro);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoParceiro_Apresentar() {
        if (tipoParceiro.equals("PR")) {
            return "Privado";
        }
        if (tipoParceiro.equals("ON")) {
            return "ONG";
        }
        if (tipoParceiro.equals("PU")) {
            return "Público";
        }
        return (tipoParceiro);
    }

    public void setTipoParceiro(String tipoParceiro) {
        this.tipoParceiro = tipoParceiro;
    }

    public Boolean getParceiroFisico() {
        if (getTipoEmpresa().equals("FI")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getParceiroJuridico() {
        if (getTipoEmpresa().equals("JU")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getParticipaBancoCurriculum() {
        if (participaBancoCurriculum == null) {
            participaBancoCurriculum = Boolean.FALSE;
        }
        return participaBancoCurriculum;
    }

    public void setParticipaBancoCurriculum(Boolean participaBancoCurriculum) {
        this.participaBancoCurriculum = participaBancoCurriculum;
    }

    public String getInscricaoMunicipal() {
        if (inscricaoMunicipal == null) {
            inscricaoMunicipal = "";
        }
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getDescricaoEmpresa() {
        if (descricaoEmpresa == null) {
            descricaoEmpresa = "";
        }
        return descricaoEmpresa;
    }

    public void setDescricaoEmpresa(String descricaoEmpresa) {
        this.descricaoEmpresa = descricaoEmpresa;
    }

    public Boolean getMostrarModalSucesso() {
        if(mostrarModalSucesso == null) {
            mostrarModalSucesso = Boolean.FALSE;
        }
        return mostrarModalSucesso;
    }

    public void setMostrarModalSucesso(Boolean mostrarModalSucesso) {
        this.mostrarModalSucesso = mostrarModalSucesso;
    }

    /**
     * @return the empresaSigilosaParaVaga
     */
    public Boolean getEmpresaSigilosaParaVaga() {
        if (empresaSigilosaParaVaga == null) {
            empresaSigilosaParaVaga = Boolean.FALSE;
        }
        return empresaSigilosaParaVaga;
    }

    /**
     * @param empresaSigilosaParaVaga the empresaSigilosaParaVaga to set
     */
    public void setEmpresaSigilosaParaVaga(Boolean empresaSigilosaParaVaga) {
        this.empresaSigilosaParaVaga = empresaSigilosaParaVaga;
    }
    
	public Date getDataAceitouParticiparBancoCurriculum() {
		return dataAceitouParticiparBancoCurriculum;
	}

	public void setDataAceitouParticiparBancoCurriculum(Date dataAceitouParticiparBancoCurriculum) {
		this.dataAceitouParticiparBancoCurriculum = dataAceitouParticiparBancoCurriculum;
	}

	public Boolean getIsentarTaxaBoleto() {
		if(isentarTaxaBoleto == null){
			isentarTaxaBoleto = true;
		}
		return isentarTaxaBoleto;
	}

	public void setIsentarTaxaBoleto(Boolean isentarTaxaBoleto) {
		this.isentarTaxaBoleto = isentarTaxaBoleto;
	}

	public boolean isIsentarJuro() {		
		return isentarJuro;
	}

	public void setIsentarJuro(boolean isentarJuro) {
		this.isentarJuro = isentarJuro;
	}
	
	
    
	public boolean isIsentarMulta() {
		return isentarMulta;
	}

	public void setIsentarMulta(boolean isentarMulta) {
		this.isentarMulta = isentarMulta;
	}

	public boolean isIsentarReajusteParcelaVencida() {		
		return isentarReajusteParcelaVencida;
	}

	public void setIsentarReajusteParcelaVencida(boolean isentarReajusteParcelaVencida) {
		this.isentarReajusteParcelaVencida = isentarReajusteParcelaVencida;
	}

	public boolean isFinanciamentoProprio() {
		return financiamentoProprio;
	}

	public void setFinanciamentoProprio(boolean financiamentoProprio) {
		this.financiamentoProprio = financiamentoProprio;
	}	

	public boolean isEmitirBoletoEmNomeBeneficiado() {		
		return emitirBoletoEmNomeBeneficiado;
	}

	public void setEmitirBoletoEmNomeBeneficiado(boolean emitirBoletoEmNomeBeneficiado) {
		this.emitirBoletoEmNomeBeneficiado = emitirBoletoEmNomeBeneficiado;
	}

	public Integer getNumeroParcelaVencidasSuspenderFinanciamento() {
		if (numeroParcelaVencidasSuspenderFinanciamento == null) {
			numeroParcelaVencidasSuspenderFinanciamento = 0;
		}
		return numeroParcelaVencidasSuspenderFinanciamento;
	}

	public void setNumeroParcelaVencidasSuspenderFinanciamento(Integer numeroParcelaVencidasSuspenderFinanciamento) {
		this.numeroParcelaVencidasSuspenderFinanciamento = numeroParcelaVencidasSuspenderFinanciamento;
	}



	/**
	 * @author Victor Hugo de Paula Costa 20/07/2015 08:49
	 */
	private Boolean possuiAliquotaEmissaoNotaEspecifica;
	private Double issqn;
	private Double pis;
	private Double cofins;
	private Double inss;
	private Double csll;

	public Boolean getPossuiAliquotaEmissaoNotaEspecifica() {
		if(possuiAliquotaEmissaoNotaEspecifica == null) {
			possuiAliquotaEmissaoNotaEspecifica = false;
		}
		return possuiAliquotaEmissaoNotaEspecifica;
	}

	public void setPossuiAliquotaEmissaoNotaEspecifica(Boolean possuiAliquotaEmissaoNotaEspecifica) {
		this.possuiAliquotaEmissaoNotaEspecifica = possuiAliquotaEmissaoNotaEspecifica;
	}

	public Double getIssqn() {
		if(issqn == null) {
			issqn = 0.0;
		}
		return issqn;
	}

	public void setIssqn(Double issqn) {
		this.issqn = issqn;
	}

	public Double getPis() {
		if(pis == null) {
			pis = 0.0;
		}
		return pis;
	}

	public void setPis(Double pis) {
		this.pis = pis;
	}

	public Double getCofins() {
		if(cofins == null) {
			cofins = 0.0;
		}
		return cofins;
	}

	public void setCofins(Double cofins) {
		this.cofins = cofins;
	}

	public Double getInss() {
		if(inss == null) {
			inss = 0.0;
		}
		return inss;
	}

	public void setInss(Double inss) {
		this.inss = inss;
	}

	public Double getCsll() {
		if(csll == null) {
			csll = 0.0;
		}
		return csll;
	}

	public void setCsll(Double csll) {
		this.csll = csll;
	}

    /**
     * @return the conveniadaParaVagasEstagio
     */
    public Boolean getConveniadaParaVagasEstagio() {
        if (conveniadaParaVagasEstagio == null) {
            conveniadaParaVagasEstagio = Boolean.FALSE;
        }
        return conveniadaParaVagasEstagio;
    }

    /**
     * @param conveniadaParaVagasEstagio the conveniadaParaVagasEstagio to set
     */
    public void setConveniadaParaVagasEstagio(Boolean conveniadaParaVagasEstagio) {
        this.conveniadaParaVagasEstagio = conveniadaParaVagasEstagio;
    }

    /**
     * @return the dataInicioConvenioEstagio
     */
    public Date getDataInicioConvenioEstagio() {
        return dataInicioConvenioEstagio;
    }

    /**
     * @param dataInicioConvenioEstagio the dataInicioConvenioEstagio to set
     */
    public void setDataInicioConvenioEstagio(Date dataInicioConvenioEstagio) {
        this.dataInicioConvenioEstagio = dataInicioConvenioEstagio;
    }

    /**
     * @return the dataFinalConvenioEstagio
     */
    public Date getDataFinalConvenioEstagio() {
        return dataFinalConvenioEstagio;
    }

    /**
     * @param dataFinalConvenioEstagio the dataFinalConvenioEstagio to set
     */
    public void setDataFinalConvenioEstagio(Date dataFinalConvenioEstagio) {
        this.dataFinalConvenioEstagio = dataFinalConvenioEstagio;
    }

    /**
     * @return the nrAcordoConvenio
     */
    public Integer getNrAcordoConvenio() {
        if (nrAcordoConvenio == null) { 
            nrAcordoConvenio = 0;
        }
        return nrAcordoConvenio;
    }

    /**
     * @param nrAcordoConvenio the nrAcordoConvenio to set
     */
    public void setNrAcordoConvenio(Integer nrAcordoConvenio) {
        this.nrAcordoConvenio = nrAcordoConvenio;
    }

    /**
     * @return the registroConselho
     */
    public String getRegistroConselho() {
        if (registroConselho == null) {
            registroConselho = "";
        }
        return registroConselho;
    }

    /**
     * @param registroConselho the registroConselho to set
     */
    public void setRegistroConselho(String registroConselho) {
        this.registroConselho = registroConselho;
    }

	/**
	 * @return the emitirNotaFiscalParaBeneficiario
	 */
	public Boolean getEmitirNotaFiscalParaBeneficiario() {
		if (emitirNotaFiscalParaBeneficiario == null) {
			emitirNotaFiscalParaBeneficiario = true;
		}
		return emitirNotaFiscalParaBeneficiario;
	}

	/**
	 * @param emitirNotaFiscalParaBeneficiario the emitirNotaFiscalParaBeneficiario to set
	 */
	public void setEmitirNotaFiscalParaBeneficiario(Boolean emitirNotaFiscalParaBeneficiario) {
		this.emitirNotaFiscalParaBeneficiario = emitirNotaFiscalParaBeneficiario;
	}

	public String getOrgaoRegistroConselho() {
		if (orgaoRegistroConselho == null) {
			orgaoRegistroConselho = "";
		}
		return orgaoRegistroConselho;
	}

	public void setOrgaoRegistroConselho(String orgaoRegistroConselho) {
		this.orgaoRegistroConselho = orgaoRegistroConselho;
	}

	public List<AreaProfissionalParceiroVO> getAreaProfissionalParceiroVOs() {
		if (areaProfissionalParceiroVOs == null) {
			areaProfissionalParceiroVOs = new ArrayList<AreaProfissionalParceiroVO>(0);
		}
		return areaProfissionalParceiroVOs;
	}

	public void setAreaProfissionalParceiroVOs(List<AreaProfissionalParceiroVO> areaProfissionalParceiroVOs) {
		this.areaProfissionalParceiroVOs = areaProfissionalParceiroVOs;
	}
	
	public List<ParceiroUnidadeEnsinoContaCorrenteVO> getListaParceiroUnidadeEnsinoContaCorrente() {
		if(listaParceiroUnidadeEnsinoContaCorrente == null){
			listaParceiroUnidadeEnsinoContaCorrente = new ArrayList<ParceiroUnidadeEnsinoContaCorrenteVO>();
		}
		return listaParceiroUnidadeEnsinoContaCorrente;
	}

	public void setListaParceiroUnidadeEnsinoContaCorrente(
			List<ParceiroUnidadeEnsinoContaCorrenteVO> listaParceiroUnidadeEnsinoContaCorrente) {
		this.listaParceiroUnidadeEnsinoContaCorrente = listaParceiroUnidadeEnsinoContaCorrente;
	}	
	
	public ContaCorrenteVO getContaCorrenteDaUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino){
		for (ParceiroUnidadeEnsinoContaCorrenteVO obj : getListaParceiroUnidadeEnsinoContaCorrente()) {
			if(obj.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsino.getCodigo())){
				return obj.getContaCorrenteVO();
			}
		}
		return new ContaCorrenteVO();
	}
	

	public CategoriaDespesaVO getCategoriaDespesa() {
		if(categoriaDespesa == null){
			categoriaDespesa = new CategoriaDespesaVO();
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}
	
	
	public FormaPagamentoVO getFormaPagamentoRecebimento() {
		if(formaPagamentoRecebimento == null){
			formaPagamentoRecebimento = new FormaPagamentoVO();
		}
		return formaPagamentoRecebimento;
	}

	public void setFormaPagamentoRecebimento(FormaPagamentoVO formaPagamentoRecebimento) {
		this.formaPagamentoRecebimento = formaPagamentoRecebimento;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if(formaPagamento == null){
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}
	
	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	/**
	 * Aliquotas de impostos específicas para emissão de nota
	 */
	private Double IRRF;

	public Double getIRRF() {
		if (IRRF == null) {
			IRRF = 0.0;
		}
		return IRRF;
	}

	public void setIRRF(Double iRRF) {
		IRRF = iRRF;
	}
	
	

	public boolean isCusteaParcelasMaterialDidatico() {
		return custeaParcelasMaterialDidatico;
	}

	public void setCusteaParcelasMaterialDidatico(boolean custeaParcelasMaterialDidatico) {
		this.custeaParcelasMaterialDidatico = custeaParcelasMaterialDidatico;
	}

	public String getNomeBanco() {
		if (nomeBanco == null) {
			nomeBanco = "";
		}
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getNumeroBancoRecebimento() {
		if (numeroBancoRecebimento == null) {
			numeroBancoRecebimento = "";
		}
		return numeroBancoRecebimento;
	}

	public void setNumeroBancoRecebimento(String numeroBancoRecebimento) {
		this.numeroBancoRecebimento = numeroBancoRecebimento;
	}

	public String getNumeroAgenciaRecebimento() {
		if (numeroAgenciaRecebimento == null) {
			numeroAgenciaRecebimento = "";
		}
		return numeroAgenciaRecebimento;
	}

	public void setNumeroAgenciaRecebimento(String numeroAgenciaRecebimento) {
		this.numeroAgenciaRecebimento = numeroAgenciaRecebimento;
	}

	public String getDigitoAgenciaRecebimento() {
		if (digitoAgenciaRecebimento == null) {
			digitoAgenciaRecebimento = "";
		}
		return digitoAgenciaRecebimento;
	}

	public void setDigitoAgenciaRecebimento(String digitoAgenciaRecebimento) {
		this.digitoAgenciaRecebimento = digitoAgenciaRecebimento;
	}

	public String getContaCorrenteRecebimento() {
		if (contaCorrenteRecebimento == null) {
			contaCorrenteRecebimento = "";
		}
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public String getDigitoCorrenteRecebimento() {
		if (digitoCorrenteRecebimento == null) {
			digitoCorrenteRecebimento = "";
		}
		return digitoCorrenteRecebimento;
	}

	public void setDigitoCorrenteRecebimento(String digitoCorrenteRecebimento) {
		this.digitoCorrenteRecebimento = digitoCorrenteRecebimento;
	}
	
	public Boolean getConsiderarValorDescontoDeclaracaoImpostoRendaAluno() {
		return considerarValorDescontoDeclaracaoImpostoRendaAluno;
	}

	public void setConsiderarValorDescontoDeclaracaoImpostoRendaAluno(
			Boolean considerarValorDescontoDeclaracaoImpostoRendaAluno) {
		this.considerarValorDescontoDeclaracaoImpostoRendaAluno = considerarValorDescontoDeclaracaoImpostoRendaAluno;
	}

	public Boolean getPermiteEmitirBoletoAlunoVinculadoParceiro() {
		if (permiteEmitirBoletoAlunoVinculadoParceiro == null) {
			permiteEmitirBoletoAlunoVinculadoParceiro = Boolean.TRUE;
		}
		return permiteEmitirBoletoAlunoVinculadoParceiro;
	}

	public void setPermiteEmitirBoletoAlunoVinculadoParceiro(Boolean permiteEmitirBoletoAlunoVinculadoParceiro) {
		this.permiteEmitirBoletoAlunoVinculadoParceiro = permiteEmitirBoletoAlunoVinculadoParceiro;
	}

	public Boolean getPermiteRemessaBoletoAlunoVinculadoParceiro() {
		if (permiteRemessaBoletoAlunoVinculadoParceiro == null) {
			permiteRemessaBoletoAlunoVinculadoParceiro = Boolean.TRUE;
		}
		return permiteRemessaBoletoAlunoVinculadoParceiro;
	}

	public void setPermiteRemessaBoletoAlunoVinculadoParceiro(Boolean permiteRemessaBoletoAlunoVinculadoParceiro) {
		this.permiteRemessaBoletoAlunoVinculadoParceiro = permiteRemessaBoletoAlunoVinculadoParceiro;
	}

	public Boolean getRealizarReajustePrecoComBaseIndiceReajuste() {
		if(realizarReajustePrecoComBaseIndiceReajuste == null) {
			realizarReajustePrecoComBaseIndiceReajuste = true;
		}
		return realizarReajustePrecoComBaseIndiceReajuste;
	}

	public void setRealizarReajustePrecoComBaseIndiceReajuste(Boolean realizarReajustePrecoComBaseIndiceReajuste) {
		this.realizarReajustePrecoComBaseIndiceReajuste = realizarReajustePrecoComBaseIndiceReajuste;
	}

	public Boolean getConsiderarParcelasMaterialDidaticoReajustePreco() {
		if(considerarParcelasMaterialDidaticoReajustePreco == null) {
			considerarParcelasMaterialDidaticoReajustePreco = true;
		}
		return considerarParcelasMaterialDidaticoReajustePreco;
	}

	public void setConsiderarParcelasMaterialDidaticoReajustePreco(Boolean considerarParcelasMaterialDidaticoReajustePreco) {
		this.considerarParcelasMaterialDidaticoReajustePreco = considerarParcelasMaterialDidaticoReajustePreco;
	}

	public boolean isValidarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula() {
		return validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula;
	}

	public void setValidarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula(boolean validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula) {
		this.validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula = validarInadimplenciaAlunoParcelaParceiroRenovacaoMatricula;
	}

	public boolean isValidarDebitoFinanceiroAoIncluirConvenioMatricula() {
		return validarDebitoFinanceiroAoIncluirConvenioMatricula;
	}

	public void setValidarDebitoFinanceiroAoIncluirConvenioMatricula(boolean validarDebitoFinanceiroAoIncluirConvenioMatricula) {
		this.validarDebitoFinanceiroAoIncluirConvenioMatricula = validarDebitoFinanceiroAoIncluirConvenioMatricula;
	}
	
	
	

	public String getChaveEnderecamentoPix() {
		if(chaveEnderecamentoPix == null ) {
			chaveEnderecamentoPix = "" ;
		}
		return chaveEnderecamentoPix;
	}

	public void setChaveEnderecamentoPix(String chaveEnderecamentoPix) {
		this.chaveEnderecamentoPix = chaveEnderecamentoPix;
	}

	public TipoIdentificacaoChavePixEnum getTipoIdentificacaoChavePixEnum() {
		if(tipoIdentificacaoChavePixEnum == null ) {
			tipoIdentificacaoChavePixEnum = TipoIdentificacaoChavePixEnum.CPF_CNPJ;
		}
		return tipoIdentificacaoChavePixEnum;
	}

	public void setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum) {
		this.tipoIdentificacaoChavePixEnum = tipoIdentificacaoChavePixEnum;
	}	
	
	
	
	
}
