package negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Carlos
 */
public class RankingTurmaVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6501637833032772791L;
	private TurmaVO turmaVO;
    private Integer qtdeAluno;
    private Integer qtdeAlunoDesconsiderados;
    private List<RankingVO> rankingVOs;
    private ComissionamentoTurmaVO comissionamentoTurmaVO;
    private Double valorTotalComissao;
    private Double valorBaseCalculo;
    
    
    public Integer getQtdeConsultor(){
    	return getRankingVOs().size();
    }

    public Integer getQtdeConsultorConsiderar(){
    	Integer qtd = 0;
    	if (!getRankingVOs().isEmpty()) {
    		List<RankingVO> lista = getRankingVOs(); 
    		Iterator<RankingVO> i = lista.iterator();
    		while (i.hasNext()) {
    			RankingVO ra = (RankingVO)i.next();
    			if (ra.getConsultorAtingiuMeta()) {
    				qtd++;
    			}
    		}
    	}
    	return qtd;
    }
    
    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public Integer getQtdeAluno() {
        if (qtdeAluno == null) {
            qtdeAluno = 0;
        }
        return qtdeAluno;
    }

    public void setQtdeAluno(Integer qtdeAluno) {
        this.qtdeAluno = qtdeAluno;
    }

	public Integer getQtdeAlunoDesconsiderados() {
		if(qtdeAlunoDesconsiderados== null){
			qtdeAlunoDesconsiderados = 0;
		}
		return qtdeAlunoDesconsiderados;
	}

	public void setQtdeAlunoDesconsiderados(Integer qtdeAlunoDesconsiderados) {
		this.qtdeAlunoDesconsiderados = qtdeAlunoDesconsiderados;
	}

	public List<RankingVO> getRankingVOs() {
		if(rankingVOs == null){
			rankingVOs = new ArrayList<RankingVO>(0);
		}
		return rankingVOs;
	}

	public void setRankingVOs(List<RankingVO> rankingVOs) {
		this.rankingVOs = rankingVOs;
	}

	public ComissionamentoTurmaVO getComissionamentoTurmaVO() {
		if(comissionamentoTurmaVO == null){
			comissionamentoTurmaVO = new ComissionamentoTurmaVO();
		}
		return comissionamentoTurmaVO;
	}

	public void setComissionamentoTurmaVO(ComissionamentoTurmaVO comissionamentoTurmaVO) {
		this.comissionamentoTurmaVO = comissionamentoTurmaVO;
	}

	public Double getValorTotalComissao() {
		if(valorTotalComissao == null){
			valorTotalComissao = 0.0;
		}
		return valorTotalComissao;
	}

	public void setValorTotalComissao(Double valorTotalComissao) {
		this.valorTotalComissao = valorTotalComissao;
	}

	public Double getValorBaseCalculo() {
		if(valorBaseCalculo == null){
			valorBaseCalculo = 0.0;
		}
		return valorBaseCalculo;
	}

	public void setValorBaseCalculo(Double valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}
    
	
    
}
