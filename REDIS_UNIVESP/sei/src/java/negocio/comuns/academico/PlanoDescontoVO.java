package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;

/**
 * Reponsável por manter os dados da entidade PlanoDesconto. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PlanoDescontoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String tipoDescontoParcela;
    private Double percDescontoParcela;
    private String tipoDescontoMatricula;
    private Double percDescontoMatricula;
    private Integer diasValidadeVencimento;
    private String requisitos;
    private String descricao;
    private Boolean somente1PeriodoLetivoParcela;
    private Boolean somente1PeriodoLetivoMatricula;
    
    /**
     * Indica se o plano de desconto sempre será calculado sobre o valor total da conta
     * a receber, independente da aplicação de outros descontos, como progressivo, convênio,...
     */
    private Boolean aplicarSobreValorCheio;
    
    /**
     * Esta variável só poderá ser marcada, quando o usuário indicar que o plano não é 
     * calculado sobre o valor cheio (atributo acima). Neste caso, temos que determinar
     * se o Plano de Desconto será sempre calculado sobre a Base de Cálculo Inicial do Desconto
     * Institucional, ou se o mesmo será aplicado sobre esta Base de Cálculo Inicial do Desconto
     * Instituciona, já dezudido outros planos de descontos, que já tenham sido calculados
     * conforme a ordem de prioridade de aplicação (ver atributo abaixo - ordemPrioridadeParaCalculo).
     * Exemplo (A): aplicarSobreValorCheio (true) aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto (false)
     *              Ordem de aplicação dos descontos: progressivo -> institucional
     *                Valor da Parcela: R$500,00
     *                Valor Desc. Progressivo (Ordem Aplicação 1): R$ 50,00 (10%)
     *                Valor Plano Desconto A (Ordem Prioridade Calculo 1): R$ 100,00 (20% sobre R$ 500,00)
     *                Valor Plano Desconto B (Ordem Prioridade Calculo 2): R$ 100,00 (20% sobre R$ 500,00)
     *                     * neste caso nem a ordem dos descontos (convenio, progressivo, institucional) nem
     *                        a ordem de prioridade de cálculo do plano de desconto em si fazem diferença para
     *                        o cálculo do valor do desconto de cada plano, pois ambos são sobre o valor cheio da 
     *                        conta a receber
     *         
     * Exemplo (B): aplicarSobreValorCheio (false) aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto (false)
     *              Ordem de aplicação dos descontos: progressivo -> institucional
     *                Valor da Parcela: R$500,00
     *                Valor Desc. Progressivo (Ordem Aplicação 1): R$ 50,00 (10% sobre R$ 500,00)
     *                Valor Plano Desconto A (Ordem Prioridade Calculo 1): R$ 90,00 (20% sobre R$ 450,00)
     *                Valor Plano Desconto B (Ordem Prioridade Calculo 2): R$ 90,00 (20% sobre R$ 450,00)
     *                     * neste caso a ordem dos descontos (convenio, progressivo, institucional) influência
     *                        no cálculo pois o desconto progressivo deve ser aplicado primeiro. Já ordem
     *                        de prioridade de cálculo do plano de desconto não impacta no cálculo, pois 
     *                        como a opção aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto está falso
     *                        para ambos os planos, os dois serão aplicados sobre o valor base inicial de 
     *                        plano de desconto.
     * 
     * Exemplo (C): aplicarSobreValorCheio (false) aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto (true) 
     *              Ordem de aplicação dos descontos: progressivo -> institucional
     *                Valor da Parcela: R$500,00
     *                Valor Desc. Progressivo (Ordem Aplicação 1): R$ 50,00 (10% sobre R$ 500,00)
     *                Valor Plano Desconto A (Ordem Prioridade Calculo 1): R$ 90,00 (20% sobre R$ 450,00)
     *                Valor Plano Desconto B (Ordem Prioridade Calculo 2): R$ 72,00 (20% sobre R$ 360,00)
     *                     * neste caso a ordem dos descontos (convenio, progressivo, institucional) influência
     *                        no cálculo pois o desconto progressivo deve ser aplicado primeiro. O mesmo ocorre com a 
     *                        ordem de prioridade de cálculo do plano de desconto (ou seja, impacta no cálculo), pois 
     *                        como a opção aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto está (true)
     *                        para ambos os planos, o plano de prioridade 1 será aplicado primeiro e o plano de prioridade
     *                        2 será aplicado posteriormente, com o valor do primeiro sendo abatido da base inicial 
     *                        de cálculo.
     * 
     */
    private Boolean aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto; 
    
    /**
     * Ordem de Prioridade de Cálculo define uma ordem na aplicação de um conjunto
     * de planos de desconto. Ou seja, caso seja adicionado mais de um plano de desconto
     * para um aluno, o SEI utilizará esta variável para determinar qual plano deverá
     * ser aplicado primeiramente e qual deverá ser processado depois.
     */
    private Integer ordemPrioridadeParaCalculo;
    
    /**
     * Atributo utilizado para categorizar (gerenciar como um grupo)
     * uma lista de planos de descontos. É importante, pois as instituições
     * possuem muitos planos de desconto de um determinado tipo. Por exemplo,
     * existe o FIES 100%, FIES 90%, FIES 85%, ... Todos estes planos de descontos
     * serão da CATEGORIA - FIES. Assim, no Painel gestor e em algusn relatórios
     * do SEI poderíamos totalizar por tipo de desconto.
     */
    private CategoriaDescontoVO categoriaDescontoVO;
    private Boolean utilizarDiaUtil;
    private Boolean utilizarDiaFixo;
    private Boolean utilizarAvancoDiaUtil;
    
    private UnidadeEnsinoVO unidadeEnsinoVO;
    /**
     * Atributo utilizado no relatório ListagemDescontosAlunos
     */
    private Double valorTotalDescontoMatriculaPorPlanoDescontoCalculado;
    private Double valorTotalDescontoParcelaPorPlanoDescontoCalculado;
    private Boolean ativo;
    private Date dataAtivacao;
    private UsuarioVO responsavelAtivacao;
    private Date dataInativacao;
    private UsuarioVO responsavelInativacao;
    private Boolean descontoValidoAteDataVencimento;
    /**
     * Atributo utilizado para controlar os descontos filantrópicos dados por uma instituição
     */
    private Boolean bolsaFilantropia;
    
    private Boolean removerDescontoRenovacao;
    private Integer aplicarDescontoApartirParcela;
    
    private Boolean considerarApenasCalculoComBaseDescontosCalculados;   
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoDesconto</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PlanoDescontoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PlanoDescontoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PlanoDescontoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Plano Desconto) deve ser informado.");
        }
//        if ((obj.getDiasValidadeVencimento().intValue() > 31) || (obj.getDiasValidadeVencimento().intValue() <= 0)) {
//            throw new ConsistirException("O campo NR. DIAS ANTES VCTO DESCONTO (Plano Desconto) não pode ser menor que zero(0) ou maior que trinta e um(31) dias.");
//        }
        if (obj.getDescontoValidoAteDataVencimento()) {
            if (obj.getDiasValidadeVencimento() != null) {
                if (obj.getDiasValidadeVencimento().intValue() > 0) {
                    throw new ConsistirException("Não é possível cadastrar um plano de desconto utilizando Nr. Dias Antes Vcto Desconto e também Desconto válido até a data vencimento, deve ser informado apenas uma das duas opções!");
                }
            }
        } else {
            if (obj.getDiasValidadeVencimento().intValue() != 0) {
                if (obj.getDiasValidadeVencimento().intValue() > 31) {
                    throw new ConsistirException("O campo Nr. Dias Antes Vecto Desconto não pode ser maior que 31 dias");
                }
            }
        }
        if (obj.getTipoDescontoParcela().equals("PO")) {
            if (obj.getPercDescontoParcela().doubleValue() > 100.0) {
                throw new ConsistirException("Para Tipo de Desconto Parcela igual a PORCENTAGEM o valor de desconto da parcela não pode ser superior a 100%.");
            }
        }
        if (obj.getTipoDescontoMatricula().equals("PO")) {
            if (obj.getPercDescontoMatricula().doubleValue() > 100.0) {
                throw new ConsistirException("Para Tipo de Desconto Matrícula igual a PORCENTAGEM o valor de desconto da matrícula não pode ser superior a 100%.");
            }
        }
        obj.getAtivo();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setPercDescontoParcela(0.0);
        setPercDescontoMatricula(0.0);
        setRequisitos("");
        setDescricao("");
        setSomente1PeriodoLetivoMatricula(Boolean.FALSE);
        setSomente1PeriodoLetivoParcela(Boolean.FALSE);
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRequisitos() {
        return (requisitos);
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public Double getPercDescontoMatricula() {
        if (percDescontoMatricula == null) {
            percDescontoMatricula = 0.0;
        }
        return (percDescontoMatricula);
    }

    public void setPercDescontoMatricula(Double percDescontoMatricula) {
        this.percDescontoMatricula = percDescontoMatricula;
    }

    public Double getPercDescontoParcela() {
        return (percDescontoParcela);
    }

    public void setPercDescontoParcela(Double percDescontoParcela) {
        this.percDescontoParcela = percDescontoParcela;
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Boolean getSomente1PeriodoLetivoParcela() {
        return somente1PeriodoLetivoParcela;
    }

    public void setSomente1PeriodoLetivoParcela(Boolean somente1PeriodoLetivoParcela) {
        this.somente1PeriodoLetivoParcela = somente1PeriodoLetivoParcela;
    }

    public Boolean getSomente1PeriodoLetivoMatricula() {
        return somente1PeriodoLetivoMatricula;
    }

    public void setSomente1PeriodoLetivoMatricula(Boolean somente1PeriodoLetivoMatricula) {
        this.somente1PeriodoLetivoMatricula = somente1PeriodoLetivoMatricula;
    }

    /**
     * @return the tipoDescontoParcela
     */
    public String getTipoDescontoParcela() {
        if (tipoDescontoParcela == null) {
            tipoDescontoParcela = "PO";
        }
        return tipoDescontoParcela;
    }

    /**
     * @param tipoDescontoParcela the tipoDescontoParcela to set
     */
    public void setTipoDescontoParcela(String tipoDescontoParcela) {
        this.tipoDescontoParcela = tipoDescontoParcela;
    }

    /**
     * @return the tipoDescontoMatricula
     */
    public String getTipoDescontoMatricula() {
        if (tipoDescontoMatricula == null) {
            tipoDescontoMatricula = "PO";
        }
        return tipoDescontoMatricula;
    }

    /**
     * @param tipoDescontoMatricula the tipoDescontoMatricula to set
     */
    public void setTipoDescontoMatricula(String tipoDescontoMatricula) {
        this.tipoDescontoMatricula = tipoDescontoMatricula;
    }

    public String getTipoDescParcela() {
        return TipoDescontoAluno.getSimbolo(getTipoDescontoParcela());
    }

    public String getTipoDescMatricula() {
        return TipoDescontoAluno.getSimbolo(getTipoDescontoMatricula());
    }

    /**
     * @return the diasValidadeVencimento
     */
    public Integer getDiasValidadeVencimento() {
        if ((diasValidadeVencimento == null) || (diasValidadeVencimento == 0)) {
            diasValidadeVencimento = 0;
        }
        return diasValidadeVencimento;
    }

    /**
     * @param diasValidadeVencimento the diasValidadeVencimento to set
     */
    public void setDiasValidadeVencimento(Integer diasValidadeVencimento) {
        this.diasValidadeVencimento = diasValidadeVencimento;
    }

    public Double getValorTotalDescontoMatriculaPorPlanoDescontoCalculado() {
        if (valorTotalDescontoMatriculaPorPlanoDescontoCalculado == null) {
            valorTotalDescontoMatriculaPorPlanoDescontoCalculado = 0.0;
        }
        return valorTotalDescontoMatriculaPorPlanoDescontoCalculado;
    }

    public void setValorTotalDescontoMatriculaPorPlanoDescontoCalculado(Double valorTotalDescontoMatriculaPorPlanoDescontoCalculado) {
        this.valorTotalDescontoMatriculaPorPlanoDescontoCalculado = valorTotalDescontoMatriculaPorPlanoDescontoCalculado;
    }

    public Double getValorTotalDescontoParcelaPorPlanoDescontoCalculado() {
        if (valorTotalDescontoParcelaPorPlanoDescontoCalculado == null) {
            valorTotalDescontoParcelaPorPlanoDescontoCalculado = 0.0;
        }
        return valorTotalDescontoParcelaPorPlanoDescontoCalculado;
    }

    public void setValorTotalDescontoParcelaPorPlanoDescontoCalculado(Double valorTotalDescontoParcelaPorPlanoDescontoCalculado) {
        this.valorTotalDescontoParcelaPorPlanoDescontoCalculado = valorTotalDescontoParcelaPorPlanoDescontoCalculado;
    }

    public Boolean getAtivo() {
        if (ativo == null) {
            ativo = false;
        }
        return ativo;
    }

    public String getAtivo_Apresentar() {
        return getAtivo() ? UteisJSF.internacionalizar("msg_sim") : UteisJSF.internacionalizar("msg_nao");
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDataAtivacao() {
        return dataAtivacao;
    }

    public String getDataAtivacao_Apresentar() {
        return Uteis.getData(dataAtivacao);
    }

    public void setDataAtivacao(Date dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public Date getDataInativacao() {
        return dataInativacao;
    }

    public String getDataInativacao_Apresentar() {
        return Uteis.getData(dataInativacao);
    }

    public void setDataInativacao(Date dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    public UsuarioVO getResponsavelAtivacao() {
        if (responsavelAtivacao == null) {
            responsavelAtivacao = new UsuarioVO();
        }
        return responsavelAtivacao;
    }

    public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
        this.responsavelAtivacao = responsavelAtivacao;
    }

    public UsuarioVO getResponsavelInativacao() {
        if (responsavelInativacao == null) {
            responsavelInativacao = new UsuarioVO();
        }
        return responsavelInativacao;
    }

    public void setResponsavelInativacao(UsuarioVO responsavelInativacao) {
        this.responsavelInativacao = responsavelInativacao;
    }

    public Boolean getDescontoValidoAteDataVencimento() {
        if (descontoValidoAteDataVencimento == null) {
            descontoValidoAteDataVencimento = Boolean.FALSE;
        }
        return descontoValidoAteDataVencimento;
    }

    public void setDescontoValidoAteDataVencimento(Boolean descontoValidoAteDataVencimento) {
        this.descontoValidoAteDataVencimento = descontoValidoAteDataVencimento;
    }

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public Boolean getAplicarSobreValorCheio() {
		if(aplicarSobreValorCheio == null){
			aplicarSobreValorCheio = false;
		}
		return aplicarSobreValorCheio;
	}

	public void setAplicarSobreValorCheio(Boolean aplicarSobreValorCheio) {
		this.aplicarSobreValorCheio = aplicarSobreValorCheio;
	}

	public Boolean getUtilizarDiaUtil() {
		if(utilizarDiaUtil == null){
			utilizarDiaUtil = false;
		}
		return utilizarDiaUtil;
	}

	public void setUtilizarDiaUtil(Boolean utilizarDiaUtil) {
		this.utilizarDiaUtil = utilizarDiaUtil;
	}
	
	public Boolean getUtilizarAvancoDiaUtil() {
		if (utilizarAvancoDiaUtil == null) {
			utilizarAvancoDiaUtil = false;
		}
		return utilizarAvancoDiaUtil;
	}

	public void setUtilizarAvancoDiaUtil(Boolean utilizarAvancoDiaUtil) {
		this.utilizarAvancoDiaUtil = utilizarAvancoDiaUtil;
	}

	public Boolean getUtilizarDiaFixo() {
		if(utilizarDiaFixo == null){
			utilizarDiaFixo = false;
		}
		return utilizarDiaFixo;
	}

	public void setUtilizarDiaFixo(Boolean utilizarDiaFixo) {
		this.utilizarDiaFixo = utilizarDiaFixo;
	}

    /**
     * @return the aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto
     */
    public Boolean getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto() {
        if (aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto == null) {
            aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto = Boolean.TRUE;
        }
        return aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto;
    }

    /**
     * @param aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto the aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto to set
     */
    public void setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(Boolean aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto) {
        this.aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto = aplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto;
    }

    /**
     * @return the ordemPrioridadeParaCalculo
     */
    public Integer getOrdemPrioridadeParaCalculo() {
        if (ordemPrioridadeParaCalculo == null) {
            ordemPrioridadeParaCalculo = 0;
        }
        return ordemPrioridadeParaCalculo;
    }

    /**
     * @param ordemPrioridadeParaCalculo the ordemPrioridadeParaCalculo to set
     */
    public void setOrdemPrioridadeParaCalculo(Integer ordemPrioridadeParaCalculo) {
        this.ordemPrioridadeParaCalculo = ordemPrioridadeParaCalculo;
    }

    /**
     * @return the categoriaDescontoVO
     */
    public CategoriaDescontoVO getCategoriaDescontoVO() {
        if (categoriaDescontoVO == null) {
            categoriaDescontoVO = new CategoriaDescontoVO();
        }
        return categoriaDescontoVO;
    }

    /**
     * @param categoriaDescontoVO the categoriaDescontoVO to set
     */
    public void setCategoriaDescontoVO(CategoriaDescontoVO categoriaDescontoVO) {
        this.categoriaDescontoVO = categoriaDescontoVO;
    }

	public Boolean getBolsaFilantropia() {
		if (bolsaFilantropia == null) {
			bolsaFilantropia = false;
		}
		return bolsaFilantropia;
	}

	public void setBolsaFilantropia(Boolean bolsaFilantropia) {
		this.bolsaFilantropia = bolsaFilantropia;
	}

	public Boolean getRemoverDescontoRenovacao() {
		if (removerDescontoRenovacao == null) {
			removerDescontoRenovacao = false;
		}
		return removerDescontoRenovacao;
	}

	public void setRemoverDescontoRenovacao(Boolean removerDescontoRenovacao) {
		this.removerDescontoRenovacao = removerDescontoRenovacao;
	}	
	
	public Integer getAplicarDescontoApartirParcela() {
		if(aplicarDescontoApartirParcela == null){
			aplicarDescontoApartirParcela = 0;
		}
		return aplicarDescontoApartirParcela;
	}

	public void setAplicarDescontoApartirParcela(Integer aplicarDescontoApartirParcela) {
		this.aplicarDescontoApartirParcela = aplicarDescontoApartirParcela;
	}
	
	public Boolean getConsiderarApenasCalculoComBaseDescontosCalculados() {
		if(considerarApenasCalculoComBaseDescontosCalculados == null){
			considerarApenasCalculoComBaseDescontosCalculados = false;
		}
		return considerarApenasCalculoComBaseDescontosCalculados;
	}

	public void setConsiderarApenasCalculoComBaseDescontosCalculados(
			Boolean considerarApenasCalculoComBaseDescontosCalculados) {
		this.considerarApenasCalculoComBaseDescontosCalculados = considerarApenasCalculoComBaseDescontosCalculados;
	}
	


	public String tipoDescParcelaApresentar;
	public String getTipoDescParcelaApresentar(){
		if(tipoDescParcelaApresentar == null){
			if(TipoDescontoAluno.getEnum(getTipoDescontoParcela()).equals(TipoDescontoAluno.PORCENTO)){
				tipoDescParcelaApresentar=  UteisNfe.formatarStringDouble(getPercDescontoParcela().toString(), 2)+"%";
			}else{
				tipoDescParcelaApresentar = "R$ "+UteisNfe.formatarStringDouble(getPercDescontoParcela().toString(), 2);
			}
		}
		return tipoDescParcelaApresentar;
	}
	
	public String tipoDescMatriculaApresentar;
	public String getTipoDescMatriculaApresentar(){
		if(tipoDescMatriculaApresentar == null){
			if(TipoDescontoAluno.getEnum(getTipoDescontoMatricula()).equals(TipoDescontoAluno.PORCENTO)){
				tipoDescMatriculaApresentar=  UteisNfe.formatarStringDouble(getPercDescontoMatricula().toString(), 2)+"%";
			}else{
				tipoDescMatriculaApresentar = "R$ "+UteisNfe.formatarStringDouble(getPercDescontoMatricula().toString(), 2);
			}
		}
		return tipoDescMatriculaApresentar;
	}

public String descricaoValidade;
	
	public String getDescricaoValidade(){
		if(descricaoValidade == null){
			criarDescricaoValidade(false);
		}
		return descricaoValidade;
	}
	
	public String criarDescricaoValidade(boolean comDataLimite) {
		
			if(getDataLimiteAplicarDesconto() != null && comDataLimite) {
				descricaoValidade=  Uteis.getData(getDataLimiteAplicarDesconto());				
			}else if(getUtilizarDiaFixo()){
				descricaoValidade=  "Dia Fixo ("+getDiasValidadeVencimento()+")";				
			}else if(getUtilizarDiaUtil()){
				descricaoValidade=  "Dia Útil ("+getDiasValidadeVencimento()+")";				
			}else if(getDescontoValidoAteDataVencimento()){
				descricaoValidade=  "Até Vencimento";				
			}else if(getDiasValidadeVencimento() > 0){
				descricaoValidade=  getDiasValidadeVencimento()+" Dias Antes Vencimento";				
			}else{
				descricaoValidade=  "Sem Validade";
			}
		
		return descricaoValidade;
	}
	public String getDescricaoValidadeComDataLimite(){
		if(descricaoValidade == null){
			criarDescricaoValidade(true);
		}
		return descricaoValidade;
	}
	
	public Boolean getUtilizarDescontoSemLimiteValidade() {
		if ((!getUtilizarDiaFixo()) &&
			(!getUtilizarDiaUtil()) &&
			(!getDescontoValidoAteDataVencimento()) &&
			(getDiasValidadeVencimento() == 0)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Boolean getUtilizarDiasAntesVctoPadrao() {
		if ((!getUtilizarDiaFixo()) &&
		    (!getUtilizarDiaUtil()) &&
		    (!getDescontoValidoAteDataVencimento()) &&
		    (getDiasValidadeVencimento() > 0)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public String valorBaseAplicarDesconto;
	
	public void setValorBaseAplicarDesconto(String novoValor){
		valorBaseAplicarDesconto = novoValor;
	}
	
	public String getValorBaseAplicarDesconto(){
		if(valorBaseAplicarDesconto == null){			
			if(getAplicarSobreValorCheio() && getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto()){
				valorBaseAplicarDesconto = "Aplicar Sobre Valor Cheio Deduzido Outros Planos de Descontos";
			} else if(getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto()){
				valorBaseAplicarDesconto = "Aplicar Sobre Valor Líquido Deduzido Outros Planos de Descontos"; 
			} else if(getAplicarSobreValorCheio() && !getAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto()){
				valorBaseAplicarDesconto = "Aplicar Sobre Valor Cheio";
			} else {
				valorBaseAplicarDesconto = "Aplicar Sobre Valor Líquido";
			}
			if(getAplicarDescontoApartirParcela() > 0){
				valorBaseAplicarDesconto += "("+getAplicarDescontoApartirParcela()+"º Parcela Em Diante)";  
			}
		}
		return valorBaseAplicarDesconto;
	}
	
	public String getOrdenacao(){
		return getOrdemPrioridadeParaCalculo()+"_"+getCodigo();
	}
	
	public void setDescricaoValidade(String desc){
		descricaoValidade = desc;
	}

	private Date dataLimiteAplicarDesconto;

	public Date getDataLimiteAplicarDesconto() {
		/**
		 * nunca inicializar com singleton
		 */
		return dataLimiteAplicarDesconto;
	}

	public void setDataLimiteAplicarDesconto(Date dataLimiteAplicarDesconto) {
		this.dataLimiteAplicarDesconto = dataLimiteAplicarDesconto;
	}
	
	
}
