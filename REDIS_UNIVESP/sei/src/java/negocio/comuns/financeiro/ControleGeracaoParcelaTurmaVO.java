package negocio.comuns.financeiro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PerfilEconomico. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ControleGeracaoParcelaTurmaVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private Date dataVencimentoMatricula;
    private Date dataCompetenciaMatricula;
    private Date dataCompetenciaMensalidade;
    private Integer mesVencimentoPrimeiraMensalidade;
    private Integer diaVencimentoPrimeiraMensalidade;
    private Integer anoVencimentoPrimeiraMensalidade;
    private Boolean usarDataVencimentoDataMatricula;
    private Integer qtdeDiasAvancarDataVencimentoMatricula;
    private Boolean mesSubsequenteMatricula;
    private Boolean mesDataBaseGeracaoParcelas;
    private Boolean zerarValorDescontoPlanoFinanceiroAluno;
    private Boolean utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual;
    private Boolean considerarCompetenciaVctoMatricula;
    private Boolean considerarCompetenciaVctoMensalidade;
    private Boolean utilizarOrdemDescontoConfiguracaoFinanceira;
    private Boolean acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PerfilEconomico</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ControleGeracaoParcelaTurmaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ControleGeracaoParcelaTurmaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ControleGeracaoParcelaTurmaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Controle Geração Parcela Turma) deve ser informado.");
        }

        // A data de vencimento da matrícula somente é validado se o campo que indica se o vencimento será o próprio
        // dia da matrícula estiver desmarcado.
        if (!obj.getUsarDataVencimentoDataMatricula()) {
            if (obj.getDataVencimentoMatricula() == null) {
                throw new ConsistirException("O campo DATA VENCIMENTO MATRÍCULA (Controle Geração Parcela Turma) deve ser informado.");
            }
            if(Uteis.getAnoData(obj.getDataVencimentoMatricula()) < 1997){
            	throw new ConsistirException("O campo ANO  da DATA DE VENCIMENTO 1ª MENSALIDADE (Controle Geração Parcela Turma) deve ser maior que 1997.");
            }
        }
        // O mês e ano da primeira mensalidade somente será validado se o campo que indica se a primeira parcela da matrícula
        // será jogada para o mês subsequente da matrícula.
        if ((!obj.getMesSubsequenteMatricula()) && (!obj.getMesDataBaseGeracaoParcelas())) {
            if (obj.getMesVencimentoPrimeiraMensalidade() == null || obj.getMesVencimentoPrimeiraMensalidade().intValue() == 0) {
                throw new ConsistirException("O campo MÊS da DATA DE VENCIMENTO 1ª MENSALIDADE  (Controle Geração Parcela Turma) deve ser informado.");
            }
            if(Integer.valueOf(obj.getMesVencimentoPrimeiraMensalidade()) < 1 || Integer.valueOf(obj.getMesVencimentoPrimeiraMensalidade()) > 12){
            	throw new ConsistirException("O campo MÊS  da DATA DE VENCIMENTO 1ª MENSALIDADE  (Controle Geração Parcela Turma) deve ser informado entre 01 e 12.");
            }
            if (obj.getAnoVencimentoPrimeiraMensalidade() == null || obj.getMesVencimentoPrimeiraMensalidade().intValue() == 0) {
                throw new ConsistirException("O campo ANO da DATA DE VENCIMENTO DA 1ª MENSALIDADE  (Controle Geração Parcela Turma) deve ser informado.");
            }
            if (obj.getAnoVencimentoPrimeiraMensalidade().toString().length() != 4) {
            	throw new ConsistirException("O campo ANO da DATA DE VENCIMENTO DA 1ª MENSALIDADE deve conter quatro dígitos.");
            }
            if(Integer.valueOf(obj.getAnoVencimentoPrimeiraMensalidade()) < 1997){
            	throw new ConsistirException("O campo ANO  da DATA DE VENCIMENTO 1ª MENSALIDADE  (Controle Geração Parcela Turma) deve ser maior que 1997.");
            }
        }
        if (!obj.getMesDataBaseGeracaoParcelas()) {
            if (obj.getDiaVencimentoPrimeiraMensalidade() == null) {
                throw new ConsistirException("O campo DIA DE VENCIMENTO PRIMEIRA MENSALIDADE (Controle Geração Parcela Turma) deve ser informado.");
            }
            if(Integer.valueOf(obj.getDiaVencimentoPrimeiraMensalidade())<1 || Integer.valueOf(obj.getDiaVencimentoPrimeiraMensalidade())>31){
            	 throw new ConsistirException("O campo DIA DE VENCIMENTO PRIMEIRA MENSALIDADE (Controle Geração Parcela Turma) deve ser informado entre 01 e 31.");
            }
        }
        if ((!obj.getMesSubsequenteMatricula()) && !obj.getMesDataBaseGeracaoParcelas()) {
        	try {
        		Date dataBase = Uteis.getData(obj.getDiaVencimentoPrimeiraMensalidade()+"/"+obj.getMesVencimentoPrimeiraMensalidade().toString()+"/"+obj.getAnoVencimentoPrimeiraMensalidade(), "dd/MM/yyy");
				if(dataBase.compareTo(new GregorianCalendar(1997, Calendar.OCTOBER, 7, 0, 0).getTime()) < 0){
					throw new ConsistirException("O campo DATA DE VENCIMENTO PRIMEIRA MENSALIDADE (Controle Geração Parcela Turma) deve ser maior que 07/10/1997.");
				}
			} catch (ParseException e) {				
				throw new ConsistirException("O campo DATA DE VENCIMENTO PRIMEIRA MENSALIDADE (Controle Geração Parcela Turma) é inválido.");
			}
        }
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the dataVencimentoMatricula
     */
    public Date getDataVencimentoMatricula() {
        return dataVencimentoMatricula;
    }

    /**
     * @param dataVencimentoMatricula the dataVencimentoMatricula to set
     */
    public void setDataVencimentoMatricula(Date dataVencimentoMatricula) {
        this.dataVencimentoMatricula = dataVencimentoMatricula;
    }

    /**
     * @return the mesVencimentoPrimeiraMensalidade
     */
    public Integer getMesVencimentoPrimeiraMensalidade() {
        return mesVencimentoPrimeiraMensalidade;
    }

    /**
     * @param mesVencimentoPrimeiraMensalidade the mesVencimentoPrimeiraMensalidade to set
     */
    public void setMesVencimentoPrimeiraMensalidade(Integer mesVencimentoPrimeiraMensalidade) {
        this.mesVencimentoPrimeiraMensalidade = mesVencimentoPrimeiraMensalidade;
    }

    /**
     * @return the diaVencimentoPrimeiraMensalidade
     */
    public Integer getDiaVencimentoPrimeiraMensalidade() {
        return diaVencimentoPrimeiraMensalidade;
    }

    /**
     * @param diaVencimentoPrimeiraMensalidade the diaVencimentoPrimeiraMensalidade to set
     */
    public void setDiaVencimentoPrimeiraMensalidade(Integer diaVencimentoPrimeiraMensalidade) {
        this.diaVencimentoPrimeiraMensalidade = diaVencimentoPrimeiraMensalidade;
    }

    /**
     * @return the anoVencimentoPrimeiraMensalidade
     */
    public Integer getAnoVencimentoPrimeiraMensalidade() {
        return anoVencimentoPrimeiraMensalidade;
    }

    /**
     * @param anoVencimentoPrimeiraMensalidade the anoVencimentoPrimeiraMensalidade to set
     */
    public void setAnoVencimentoPrimeiraMensalidade(Integer anoVencimentoPrimeiraMensalidade) {
        this.anoVencimentoPrimeiraMensalidade = anoVencimentoPrimeiraMensalidade;
    }

    /**
     * @return the usarDataVencimentoDataMatricula
     */
    public Boolean getUsarDataVencimentoDataMatricula() {
        if (usarDataVencimentoDataMatricula == null) {
            usarDataVencimentoDataMatricula = Boolean.FALSE;
        }
        return usarDataVencimentoDataMatricula;
    }

    /**
     * @param usarDataVencimentoDataMatricula the usarDataVencimentoDataMatricula to set
     */
    public void setUsarDataVencimentoDataMatricula(Boolean usarDataVencimentoDataMatricula) {
        this.usarDataVencimentoDataMatricula = usarDataVencimentoDataMatricula;
    }

    /**
     * @return the qtdeDiasAvancarDataVencimentoMatricula
     */
    public Integer getQtdeDiasAvancarDataVencimentoMatricula() {
        if (qtdeDiasAvancarDataVencimentoMatricula == null) {
            qtdeDiasAvancarDataVencimentoMatricula = 0;
        }
        return qtdeDiasAvancarDataVencimentoMatricula;
    }

    /**
     * @param qtdeDiasAvancarDataVencimentoMatricula the qtdeDiasAvancarDataVencimentoMatricula to set
     */
    public void setQtdeDiasAvancarDataVencimentoMatricula(Integer qtdeDiasAvancarDataVencimentoMatricula) {
        this.qtdeDiasAvancarDataVencimentoMatricula = qtdeDiasAvancarDataVencimentoMatricula;
    }

    /**
     * @return the mesSubsequenteMatricula
     */
    public Boolean getMesSubsequenteMatricula() {
        if (mesSubsequenteMatricula == null) {
            mesSubsequenteMatricula = Boolean.FALSE;
        }
        return mesSubsequenteMatricula;
    }

    /**
     * @param mesSubsequenteMatricula the mesSubsequenteMatricula to set
     */
    public void setMesSubsequenteMatricula(Boolean mesSubsequenteMatricula) {
        this.mesSubsequenteMatricula = mesSubsequenteMatricula;
    }

    public Date getDataVencimentoPrimeiraMensalidade() throws ParseException {
        String dataVencimentoPrimeiraMensalidade = getDiaVencimentoPrimeiraMensalidade() + "/" + getMesVencimentoPrimeiraMensalidade() + "/" + getAnoVencimentoPrimeiraMensalidade();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        return formatador.parse(dataVencimentoPrimeiraMensalidade);
    }

    /**
     * @return the mesDataBaseGeracaoParcelas
     */
    public Boolean getMesDataBaseGeracaoParcelas() {
        if (mesDataBaseGeracaoParcelas == null) {
            mesDataBaseGeracaoParcelas = Boolean.FALSE;
        }
        return mesDataBaseGeracaoParcelas;
    }

    /**
     * @param mesDataBaseGeracaoParcelas the mesDataBaseGeracaoParcelas to set
     */
    public void setMesDataBaseGeracaoParcelas(Boolean mesDataBaseGeracaoParcelas) {
        this.mesDataBaseGeracaoParcelas = mesDataBaseGeracaoParcelas;
    }

    /**
     * @return the zerarValorDescontoPlanoFinanceiroAluno
     */
    public Boolean getZerarValorDescontoPlanoFinanceiroAluno() {
        if (zerarValorDescontoPlanoFinanceiroAluno == null) {
            zerarValorDescontoPlanoFinanceiroAluno = Boolean.FALSE;
        }
        return zerarValorDescontoPlanoFinanceiroAluno;
    }

    /**
     * @param zerarValorDescontoPlanoFinanceiroAluno the zerarValorDescontoPlanoFinanceiroAluno to set
     */
    public void setZerarValorDescontoPlanoFinanceiroAluno(Boolean zerarValorDescontoPlanoFinanceiroAluno) {
        this.zerarValorDescontoPlanoFinanceiroAluno = zerarValorDescontoPlanoFinanceiroAluno;
    }

    /**
     * @return the utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual
     */
    public Boolean getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual() {
        if (utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual == null) {
            utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual = Boolean.FALSE;
        }
        return utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual;
    }

    /**
     * @param utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual the utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual to set
     */
    public void setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(Boolean utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual) {
        this.utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual = utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual;
    }

	public Date getDataCompetenciaMatricula() {
		if (dataCompetenciaMatricula == null) {
			dataCompetenciaMatricula = new Date();
		}
		return dataCompetenciaMatricula;
	}

	public void setDataCompetenciaMatricula(Date dataCompetenciaMatricula) {
		this.dataCompetenciaMatricula = dataCompetenciaMatricula;
	}

	public Date getDataCompetenciaMensalidade() {
		if (dataCompetenciaMensalidade == null) {
			dataCompetenciaMensalidade = new Date();
		}
		return dataCompetenciaMensalidade;
	}

	public void setDataCompetenciaMensalidade(Date dataCompetenciaMensalidade) {
		this.dataCompetenciaMensalidade = dataCompetenciaMensalidade;
	}

	public Boolean getConsiderarCompetenciaVctoMatricula() {
		if (considerarCompetenciaVctoMatricula == null) {
			considerarCompetenciaVctoMatricula = Boolean.TRUE;
		}
		return considerarCompetenciaVctoMatricula;
	}

	public void setConsiderarCompetenciaVctoMatricula(Boolean considerarCompetenciaVctoMatricula) {
		this.considerarCompetenciaVctoMatricula = considerarCompetenciaVctoMatricula;
	}

	public Boolean getConsiderarCompetenciaVctoMensalidade() {
		if (considerarCompetenciaVctoMensalidade == null) {
			considerarCompetenciaVctoMensalidade = Boolean.TRUE;
		}
		return considerarCompetenciaVctoMensalidade;
	}

	public void setConsiderarCompetenciaVctoMensalidade(Boolean considerarCompetenciaVctoMensalidade) {
		this.considerarCompetenciaVctoMensalidade = considerarCompetenciaVctoMensalidade;
	}
	
	public Boolean getUtilizarOrdemDescontoConfiguracaoFinanceira() {
		if (utilizarOrdemDescontoConfiguracaoFinanceira == null) {
			utilizarOrdemDescontoConfiguracaoFinanceira = false;
		}
		return utilizarOrdemDescontoConfiguracaoFinanceira;
	}

	public void setUtilizarOrdemDescontoConfiguracaoFinanceira(Boolean utilizarOrdemDescontoConfiguracaoFinanceira) {
		this.utilizarOrdemDescontoConfiguracaoFinanceira = utilizarOrdemDescontoConfiguracaoFinanceira;
	}
	
	public Boolean getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno() {
		if (acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno == null) {
			acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno = Boolean.FALSE;
		}
		return acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno;
	}

	public void setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(Boolean acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno) {
		this.acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno = acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno;
	}
}
