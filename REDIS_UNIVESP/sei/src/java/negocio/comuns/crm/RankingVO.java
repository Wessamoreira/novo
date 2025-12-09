package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Carlos
 */
public class RankingVO extends SuperVO {

    private FuncionarioVO consultor;
    private RankingTurmaVO rankingTurmaVO;
    private ConfiguracaoRankingVO configuracaoRankingVO;    
    private String posicao;
    private Double valorComissao;
    private Double valor;
    private Boolean gerente;
    private Integer qtdeInicialAluno;
    private Integer qtdeFinalAluno;
    private Integer qtdeAlunoTurma;
    private Integer qtdeMatriculado;
    private List<RankingTurmaConsultorAlunoVO> rankingTurmaConsultorAlunoAptoVOs;
    private List<RankingTurmaConsultorAlunoVO> rankingTurmaConsultorAlunoInaptoVOs;
    private Integer qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
    private Boolean consultorConsiderar;


    public FuncionarioVO getConsultor() {
        if (consultor == null) {
            consultor = new FuncionarioVO();
        }
        return consultor;
    }

    public void setConsultor(FuncionarioVO consultor) {
        this.consultor = consultor;
    }

    public RankingTurmaVO getRankingTurmaVO() {
        if (rankingTurmaVO == null) {
            rankingTurmaVO = new RankingTurmaVO();
        }
        return rankingTurmaVO;
    }

    public void setRankingTurmaVO(RankingTurmaVO rankingTurmaVO) {
        this.rankingTurmaVO = rankingTurmaVO;
    }
    
    

    public Integer getQtdeMatriculado() {
    	if(qtdeMatriculado == null){
    		qtdeMatriculado = 0;
    	}
		return qtdeMatriculado;
	}

	public void setQtdeMatriculado(Integer qtdeMatriculado) {
		this.qtdeMatriculado = qtdeMatriculado;
	}

	public Integer getQtdeMatriculadoContabilizado() {        
        return getRankingTurmaConsultorAlunoAptoVOs().size();
    }
    
    public Integer getQtdeMatriculadoNaoContabilizado() {        
    	return getRankingTurmaConsultorAlunoInaptoVOs().size();
    }

    

    public String getPosicao() {
        if (posicao == null) {
            posicao = "";
        }
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
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

    public ConfiguracaoRankingVO getConfiguracaoRankingVO() {
        if (configuracaoRankingVO == null) {
            configuracaoRankingVO = new ConfiguracaoRankingVO();
        }
        return configuracaoRankingVO;
    }

    public void setConfiguracaoRankingVO(ConfiguracaoRankingVO configuracaoRankingVO) {
        this.configuracaoRankingVO = configuracaoRankingVO;
    }

    public String getValor_Apresentar() {
    	if(getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() > 0 && getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() > getQtdeMatriculadoContabilizado()){
    		return "Qtde Min. Matr. não Alcançada ("+getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm()+")";
    	}
    	return Uteis.getDoubleFormatado(getValor());
    }
    
    public Boolean getConsultorAtingiuMeta() {
    	if(getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() > 0 && getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() > getQtdeMatriculadoContabilizado()){
    		return false;
    	}
    	return true;
    }
    
    public Double getValorComissao() {
        if (valorComissao == null) {
            valorComissao = 0.0;
        }
        return valorComissao;
    }

    public void setValorComissao(Double valorComissao) {
        this.valorComissao = valorComissao;
    }

    public Boolean getGerente() {
        if (gerente == null) {
            gerente = Boolean.FALSE;
        }
        return gerente;
    }

    public void setGerente(Boolean gerente) {
        this.gerente = gerente;
    }

    public Integer getQtdeInicialAluno() {
        if (qtdeInicialAluno == null) {
            qtdeInicialAluno = 0;
        }
        return qtdeInicialAluno;
    }

    public void setQtdeInicialAluno(Integer qtdeInicialAluno) {
        this.qtdeInicialAluno = qtdeInicialAluno;
    }

    public Integer getQtdeFinalAluno() {
        if (qtdeFinalAluno == null) {
            qtdeFinalAluno = 0;
        }
        return qtdeFinalAluno;
    }

    public void setQtdeFinalAluno(Integer qtdeFinalAluno) {
        this.qtdeFinalAluno = qtdeFinalAluno;
    }

    public Integer getQtdeAlunoTurma() {
        if (qtdeAlunoTurma == null) {
            qtdeAlunoTurma = 0;
        }
        return qtdeAlunoTurma;
    }

    public void setQtdeAlunoTurma(Integer qtdeAlunoTurma) {
        this.qtdeAlunoTurma = qtdeAlunoTurma;
    }

	public List<RankingTurmaConsultorAlunoVO> getRankingTurmaConsultorAlunoAptoVOs() {
		if(rankingTurmaConsultorAlunoAptoVOs==null){
			rankingTurmaConsultorAlunoAptoVOs = new ArrayList<RankingTurmaConsultorAlunoVO>(0);
		}
		return rankingTurmaConsultorAlunoAptoVOs;
	}

	public void setRankingTurmaConsultorAlunoAptoVOs(List<RankingTurmaConsultorAlunoVO> rankingTurmaConsultorAlunoAptoVOs) {
		this.rankingTurmaConsultorAlunoAptoVOs = rankingTurmaConsultorAlunoAptoVOs;
	}

	public List<RankingTurmaConsultorAlunoVO> getRankingTurmaConsultorAlunoInaptoVOs() {
		if(rankingTurmaConsultorAlunoInaptoVOs==null){
			rankingTurmaConsultorAlunoInaptoVOs = new ArrayList<RankingTurmaConsultorAlunoVO>(0);
		}
		return rankingTurmaConsultorAlunoInaptoVOs;
	}

	public void setRankingTurmaConsultorAlunoInaptoVOs(List<RankingTurmaConsultorAlunoVO> rankingTurmaConsultorAlunoInaptoVOs) {
		this.rankingTurmaConsultorAlunoInaptoVOs = rankingTurmaConsultorAlunoInaptoVOs;
	}

	public Integer getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm() {
		if(qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm == null){
			qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm = 0;
		}
		return qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
	}

	public void setQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm(Integer qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm) {
		this.qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm = qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm;
	}

	public Boolean getConsultorConsiderar() {
		if (consultorConsiderar == null) {
			consultorConsiderar = Boolean.FALSE;
		}
		return consultorConsiderar;
	}

	public void setConsultorConsiderar(Boolean consultorConsiderar) {
		this.consultorConsiderar = consultorConsiderar;
	}

	
    
    
}
