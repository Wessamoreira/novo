/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.TipoSituacaoConfiguracaoRankingEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 *
 * @author Paulo Taucci
 */
public class ConfiguracaoRankingVO extends SuperVO {

    protected Integer codigo;
    private String nome;
    protected UnidadeEnsinoVO unidadeEnsino;
    protected CursoVO curso;
    protected TurmaVO turma;
    protected Date periodoInicial;
    protected Date periodoFinal;
    private Double percentualGerente;
    protected TipoSituacaoConfiguracaoRankingEnum situacao;
    private String formulaCalculoComissao;
    private List<PercentualConfiguracaoRankingVO> percentualVOs;
    private Boolean considerarRankingCrmSomenteMatriculAtivo;
    private Boolean desconsiderarRankingCrmAlunoBolsista;
    private Boolean considerarRankingCrmPrimeiraMensalidade;
    private Boolean considerarContratoAssinadoRankingCrm;
    private Integer qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
    private Boolean desconsiderarMatriculaContratoNaoAssinado4Meses;
    private Boolean desconsiderarParcelaEFaltaApartir3Meses;
    private Integer desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm;
    private Boolean considerarAlunoAdimplenteSemContratoAssinadoRankingCrm;
    private Integer qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm;
    private String descricaoRegraComissionamentoCRM;
    private Boolean desconsiderarrankingcrmsomentematriculpr;
    
    /**
     * Construtor padrão da classe <code>ConfiguracaoRanking</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ConfiguracaoRankingVO() {
        super();
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

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public Date getPeriodoFinal() {
        if (periodoFinal == null) {
            periodoFinal = new Date();
        }
        return periodoFinal;
    }

    public void setPeriodoFinal(Date periodoFinal) {
        this.periodoFinal = periodoFinal;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getPeriodoFinal_Apresentar() {
        return (Uteis.getData(getPeriodoFinal()));
    }

    public Date getPeriodoInicial() {
        if (periodoInicial == null) {
            periodoInicial = new Date();
        }
        return periodoInicial;
    }

    public void setPeriodoInicial(Date periodoInicial) {
        this.periodoInicial = periodoInicial;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getPeriodoInicial_Apresentar() {
        return (Uteis.getData(getPeriodoInicial()));
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    public TipoSituacaoConfiguracaoRankingEnum getSituacao() {
        if (situacao == null) {
            situacao = TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO;
        }
        return situacao;
    }

    public void setSituacao(TipoSituacaoConfiguracaoRankingEnum situacao) {
        this.situacao = situacao;
    }

    public String getSituacao_Apresentar() {
        if (situacao == null || situacao.equals(TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO)) {
            return UteisJSF.internacionalizar("enum_TipoSituacaoConfiguracaoRankingEnum_" + TipoSituacaoConfiguracaoRankingEnum.EM_CONSTRUCAO.toString());
        }
        if (situacao.equals(TipoSituacaoConfiguracaoRankingEnum.ATIVO)) {
            return UteisJSF.internacionalizar("enum_TipoSituacaoConfiguracaoRankingEnum_" + TipoSituacaoConfiguracaoRankingEnum.ATIVO.toString());
        }
        if (situacao.equals(TipoSituacaoConfiguracaoRankingEnum.INATIVO)) {
            return UteisJSF.internacionalizar("enum_TipoSituacaoConfiguracaoRankingEnum_" + TipoSituacaoConfiguracaoRankingEnum.INATIVO.toString());
        }
        
        return "";
    }

    public List<PercentualConfiguracaoRankingVO> getPercentualVOs() {
        if (percentualVOs == null) {
            percentualVOs = new ArrayList<PercentualConfiguracaoRankingVO>(0);
        }
        return percentualVOs;
    }

    public void setPercentualVOs(List<PercentualConfiguracaoRankingVO> percentual) {
        this.percentualVOs = percentual;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPercentualGerente() {
        if (percentualGerente == null) {
            percentualGerente = 0.0;
        }
        return percentualGerente;
    }

    public void setPercentualGerente(Double percentualGerente) {
        this.percentualGerente = percentualGerente;
    }

	public String getFormulaCalculoComissao() {
		if(formulaCalculoComissao == null){
			formulaCalculoComissao = "";
		}
		return formulaCalculoComissao;
	}

	public void setFormulaCalculoComissao(String formulaCalculoComissao) {
		this.formulaCalculoComissao = formulaCalculoComissao;
	}
    

	public Boolean getConsiderarRankingCrmSomenteMatriculAtivo() {
		if(considerarRankingCrmSomenteMatriculAtivo == null){
			considerarRankingCrmSomenteMatriculAtivo = true;
		}
		return considerarRankingCrmSomenteMatriculAtivo;
	}

	public void setConsiderarRankingCrmSomenteMatriculAtivo(Boolean considerarRankingCrmSomenteMatriculAtivo) {
		this.considerarRankingCrmSomenteMatriculAtivo = considerarRankingCrmSomenteMatriculAtivo;
	}

	public Boolean getDesconsiderarRankingCrmAlunoBolsista() {
		if(desconsiderarRankingCrmAlunoBolsista == null){
			desconsiderarRankingCrmAlunoBolsista = true;
		}
		return desconsiderarRankingCrmAlunoBolsista;
	}

	public void setDesconsiderarRankingCrmAlunoBolsista(Boolean desconsiderarRankingCrmAlunoBolsista) {
		this.desconsiderarRankingCrmAlunoBolsista = desconsiderarRankingCrmAlunoBolsista;
	}

	public Boolean getConsiderarRankingCrmPrimeiraMensalidade() {
		if(considerarRankingCrmPrimeiraMensalidade == null){
			considerarRankingCrmPrimeiraMensalidade = true;
		}
		return considerarRankingCrmPrimeiraMensalidade;
	}

	public void setConsiderarRankingCrmPrimeiraMensalidade(Boolean considerarRankingCrmPrimeiraMensalidade) {
		this.considerarRankingCrmPrimeiraMensalidade = considerarRankingCrmPrimeiraMensalidade;
	}

	public Boolean getConsiderarContratoAssinadoRankingCrm() {
		if(considerarContratoAssinadoRankingCrm == null){
			considerarContratoAssinadoRankingCrm = true;
		}
		return considerarContratoAssinadoRankingCrm;
	}

	public void setConsiderarContratoAssinadoRankingCrm(Boolean considerarContratoAssinadoRankingCrm) {
		this.considerarContratoAssinadoRankingCrm = considerarContratoAssinadoRankingCrm;
	}

	public Integer getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() {
		if(qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm == null){
			qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm = 2;
		}
		return qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
	}

	public void setQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm(Integer qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm) {
		this.qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm = qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
	}

	public Integer getDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm() {
		if(desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm == null){
			desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm = 4;
		}
		return desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm;
	}

	public void setDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm(Integer desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm) {
		this.desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm = desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm;
	}

	public Boolean getConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm() {
		if(considerarAlunoAdimplenteSemContratoAssinadoRankingCrm == null){
			considerarAlunoAdimplenteSemContratoAssinadoRankingCrm = true;
		}
		return considerarAlunoAdimplenteSemContratoAssinadoRankingCrm;
	}

	public void setConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm(Boolean considerarAlunoAdimplenteSemContratoAssinadoRankingCrm) {
		this.considerarAlunoAdimplenteSemContratoAssinadoRankingCrm = considerarAlunoAdimplenteSemContratoAssinadoRankingCrm;
	}

	public Integer getQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm() {
		if(qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm == null){
			qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm = 2;
		}
		return qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm;
	}

	public void setQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm(Integer qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm) {
		this.qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm = qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm;
	}
    
	public Boolean getDesconsiderarMatriculaContratoNaoAssinado4Meses() {
		if (desconsiderarMatriculaContratoNaoAssinado4Meses == null) {
			desconsiderarMatriculaContratoNaoAssinado4Meses = Boolean.FALSE;
		}
		return desconsiderarMatriculaContratoNaoAssinado4Meses;
	}

	public void setDesconsiderarMatriculaContratoNaoAssinado4Meses(Boolean desconsiderarMatriculaContratoNaoAssinado4Meses) {
		this.desconsiderarMatriculaContratoNaoAssinado4Meses = desconsiderarMatriculaContratoNaoAssinado4Meses;
	}

	public Boolean getDesconsiderarParcelaEFaltaApartir3Meses() {
		if (desconsiderarParcelaEFaltaApartir3Meses == null) {
			desconsiderarParcelaEFaltaApartir3Meses = Boolean.FALSE;
		}
		return desconsiderarParcelaEFaltaApartir3Meses;
	}

	public void setDesconsiderarParcelaEFaltaApartir3Meses(Boolean desconsiderarParcelaEFaltaApartir3Meses) {
		this.desconsiderarParcelaEFaltaApartir3Meses = desconsiderarParcelaEFaltaApartir3Meses;
	}

	public String getDescricaoRegraComissionamentoCRM() {
		if(descricaoRegraComissionamentoCRM == null){
			descricaoRegraComissionamentoCRM = "";
		}
		return descricaoRegraComissionamentoCRM;
	}

	public void setDescricaoRegraComissionamentoCRM(String descricaoRegraComissionamentoCRM) {
		this.descricaoRegraComissionamentoCRM = descricaoRegraComissionamentoCRM;
	}

	public Boolean getDesconsiderarrankingcrmsomentematriculpr() {
		if (desconsiderarrankingcrmsomentematriculpr == null) {
			desconsiderarrankingcrmsomentematriculpr = Boolean.FALSE;
		}
		return desconsiderarrankingcrmsomentematriculpr;
	}

	public void setDesconsiderarrankingcrmsomentematriculpr(Boolean desconsiderarrankingcrmsomentematriculpr) {
		this.desconsiderarrankingcrmsomentematriculpr = desconsiderarrankingcrmsomentematriculpr;
	}


}
