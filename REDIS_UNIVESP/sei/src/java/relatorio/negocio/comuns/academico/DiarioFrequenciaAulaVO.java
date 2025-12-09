/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Otimize-TI
 */
public class DiarioFrequenciaAulaVO implements Serializable, Cloneable{
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -9218643838559263310L;
	protected MatriculaVO matricula;
    private String dataMatriculaPeriodo;
    private String situacaoMatriculaPeriodo;
    private Boolean apresentarSituacaoMatricula;
    
    
    protected String frequencia1;
    protected String frequencia2;
    protected String frequencia3;
    protected String frequencia4;
    protected String frequencia5;
    protected String frequencia6;
    protected String frequencia7;
    protected String frequencia8;
    protected String frequencia9;
    protected String frequencia10;
    protected String frequencia11;
    protected String frequencia12;
    protected String frequencia13;
    protected String frequencia14;
    protected String frequencia15;
    protected String frequencia16;
    protected String frequencia17;
    protected String frequencia18;
    protected String frequencia19;
    protected String frequencia20;
    protected String frequencia21;
    protected String frequencia22;
    protected String frequencia23;
    protected String frequencia24;
    protected String frequencia25;
    protected String frequencia26;
    protected String frequencia27;
    protected String frequencia28;
    protected String frequencia29;
    protected String frequencia30;

    protected String turma;
            
    protected Double totalFaltas;
    protected String totalFaltasStr;
    protected String faltasGeralStr;
    private String label1;
    private String label2;
    private String label3;
    private Date dataAula;    
    private Integer ordemLinha;
    private Integer ordemColuna;
    private Integer ordemColuna2;    
    
    protected HistoricoVO historico;

    public DiarioFrequenciaAulaVO(){        
        inicializarDados();
    }
    
    
    public void inicializarDados(){
        setFrequencia1("");
        setFrequencia2("");
        setFrequencia3("");
        setFrequencia4("");
        setFrequencia5("");
        setFrequencia6("");
        setFrequencia7("");
        setFrequencia8("");
        setFrequencia9("");
        setFrequencia10("");
        setFrequencia11("");
        setFrequencia12("");
        setFrequencia13("");
        setFrequencia14("");
        setFrequencia15("");
        setFrequencia16("");
        setFrequencia17("");
        setFrequencia18("");
        setFrequencia19("");
        setFrequencia20("");
        setFrequencia21("");
        setFrequencia22("");
        setFrequencia23("");
        setFrequencia24("");
        setFrequencia25("");
        setFrequencia26("");
        setFrequencia27("");
        setFrequencia28("");
        setFrequencia29("");
        setFrequencia30("");
        
        setTotalFaltas(0.0);
        setTotalFaltasStr("0.0");
        
        
        
                
        
    }
    
    
    
    public String getFrequencia1() {
        return frequencia1;
    }

    public void setFrequencia1(String frequencia1) {
        this.frequencia1 = frequencia1;
    }

    public String getFrequencia10() {
        return frequencia10;
    }

    public void setFrequencia10(String frequencia10) {
        this.frequencia10 = frequencia10;
    }

    public String getFrequencia11() {
        return frequencia11;
    }

    public void setFrequencia11(String frequencia11) {
        this.frequencia11 = frequencia11;
    }

    public String getFrequencia12() {
        return frequencia12;
    }

    public void setFrequencia12(String frequencia12) {
        this.frequencia12 = frequencia12;
    }

    public String getFrequencia13() {
        return frequencia13;
    }

    public void setFrequencia13(String frequencia13) {
        this.frequencia13 = frequencia13;
    }

    public String getFrequencia14() {
        return frequencia14;
    }

    public void setFrequencia14(String frequencia14) {
        this.frequencia14 = frequencia14;
    }

    public String getFrequencia15() {
        return frequencia15;
    }

    public void setFrequencia15(String frequencia15) {
        this.frequencia15 = frequencia15;
    }

    public String getFrequencia16() {
        return frequencia16;
    }

    public void setFrequencia16(String frequencia16) {
        this.frequencia16 = frequencia16;
    }

    public String getFrequencia17() {
        return frequencia17;
    }

    public void setFrequencia17(String frequencia17) {
        this.frequencia17 = frequencia17;
    }

    public String getFrequencia18() {
        return frequencia18;
    }

    public void setFrequencia18(String frequencia18) {
        this.frequencia18 = frequencia18;
    }

    public String getFrequencia19() {
        return frequencia19;
    }

    public void setFrequencia19(String frequencia19) {
        this.frequencia19 = frequencia19;
    }

    public String getFrequencia2() {
        return frequencia2;
    }

    public void setFrequencia2(String frequencia2) {
        this.frequencia2 = frequencia2;
    }

    public String getFrequencia20() {
        return frequencia20;
    }

    public void setFrequencia20(String frequencia20) {
        this.frequencia20 = frequencia20;
    }

    public String getFrequencia21() {
        return frequencia21;
    }

    public void setFrequencia21(String frequencia21) {
        this.frequencia21 = frequencia21;
    }

    public String getFrequencia22() {
        return frequencia22;
    }

    public void setFrequencia22(String frequencia22) {
        this.frequencia22 = frequencia22;
    }

    public String getFrequencia23() {
        return frequencia23;
    }

    public void setFrequencia23(String frequencia23) {
        this.frequencia23 = frequencia23;
    }

    public String getFrequencia24() {
        return frequencia24;
    }

    public void setFrequencia24(String frequencia24) {
        this.frequencia24 = frequencia24;
    }

    public String getFrequencia25() {
        return frequencia25;
    }

    public void setFrequencia25(String frequencia25) {
        this.frequencia25 = frequencia25;
    }

    public String getFrequencia26() {
        return frequencia26;
    }

    public void setFrequencia26(String frequencia26) {
        this.frequencia26 = frequencia26;
    }

    public String getFrequencia27() {
        return frequencia27;
    }

    public void setFrequencia27(String frequencia27) {
        this.frequencia27 = frequencia27;
    }

    public String getFrequencia28() {
        return frequencia28;
    }

    public void setFrequencia28(String frequencia28) {
        this.frequencia28 = frequencia28;
    }

    public String getFrequencia29() {
        return frequencia29;
    }

    public void setFrequencia29(String frequencia29) {
        this.frequencia29 = frequencia29;
    }

    public String getFrequencia3() {
        return frequencia3;
    }

    public void setFrequencia3(String frequencia3) {
        this.frequencia3 = frequencia3;
    }

    public String getFrequencia30() {
        return frequencia30;
    }

    public void setFrequencia30(String frequencia30) {
        this.frequencia30 = frequencia30;
    }

    public String getFrequencia4() {
        return frequencia4;
    }

    public void setFrequencia4(String frequencia4) {
        this.frequencia4 = frequencia4;
    }

    public String getFrequencia5() {
        return frequencia5;
    }

    public void setFrequencia5(String frequencia5) {
        this.frequencia5 = frequencia5;
    }

    public String getFrequencia6() {
        return frequencia6;
    }

    public void setFrequencia6(String frequencia6) {
        this.frequencia6 = frequencia6;
    }

    public String getFrequencia7() {
        return frequencia7;
    }

    public void setFrequencia7(String frequencia7) {
        this.frequencia7 = frequencia7;
    }

    public String getFrequencia8() {
        return frequencia8;
    }

    public void setFrequencia8(String frequencia8) {
        this.frequencia8 = frequencia8;
    }

    public String getFrequencia9() {
        return frequencia9;
    }

    public void setFrequencia9(String frequencia9) {
        this.frequencia9 = frequencia9;
    }

    public HistoricoVO getHistorico() {
    	if(historico == null){
    		historico = new HistoricoVO();
    		
    	}
        return historico;
    }

    public void setHistorico(HistoricoVO historico) {
        this.historico = historico;
    }

    public MatriculaVO getMatricula() {
    	if(matricula == null){
    		matricula = new MatriculaVO();
    		
    	}
        return matricula;
    }

    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    public Double getTotalFaltas() {
        return totalFaltas;
    }

    public void setTotalFaltas(Double totalFaltas) {
        this.totalFaltas = totalFaltas;
    }

    public String getTotalFaltasStr() {
        return totalFaltasStr.replace(".0", "");
    }

    public void setTotalFaltasStr(String totalFaltasStr) {
        this.totalFaltasStr = totalFaltasStr;
    }
   
    public String getOrdenacao(){
        return Uteis.removerAcentos(getMatricula().getAluno().getNome());
    }

    /**
     * @return the turma
     */
    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    /**
     * @param turma the turma to set
     */
    public void setTurma(String turma) {
        this.turma = turma;
    }

	public String getFaltasGeralStr() {
		if (faltasGeralStr == null) {
			faltasGeralStr = "";
		}
		return faltasGeralStr;
	}

	public void setFaltasGeralStr(String faltasGeralStr) {
		this.faltasGeralStr = faltasGeralStr;
	}


	public String getDataMatriculaPeriodo() {
		if (dataMatriculaPeriodo == null) {
			dataMatriculaPeriodo = "";
		}
		return dataMatriculaPeriodo;
	}


	public void setDataMatriculaPeriodo(String dataMatriculaPeriodo) {
		this.dataMatriculaPeriodo = dataMatriculaPeriodo;
	}
	
	public String getSituacaoMatriculaPeriodo() {
		if (situacaoMatriculaPeriodo == null) {
			situacaoMatriculaPeriodo = "";
		}
		return situacaoMatriculaPeriodo;
	}


	public void setSituacaoMatriculaPeriodo(String situacaoMatriculaPeriodo) {
		this.situacaoMatriculaPeriodo = situacaoMatriculaPeriodo;
	}


	public Boolean getApresentarSituacaoMatricula() {
		if (apresentarSituacaoMatricula == null) {
			apresentarSituacaoMatricula = Boolean.FALSE;
		}
		return apresentarSituacaoMatricula;
	}


	public void setApresentarSituacaoMatricula(Boolean apresentarSituacaoMatricula) {
		this.apresentarSituacaoMatricula = apresentarSituacaoMatricula;
	}


	public Date getDataAula() {
		return dataAula;
	}


	public void setDataAula(Date dataAula) {
		this.dataAula = dataAula;
	}

	public String getLabel1() {
		return label1;
	}


	public void setLabel1(String label1) {
		this.label1 = label1;
	}


	public String getLabel2() {
		return label2;
	}


	public void setLabel2(String label2) {
		this.label2 = label2;
	}


	public Integer getOrdemLinha() {
		if(ordemLinha == null) {
			ordemLinha = 0;
		}
		return ordemLinha;
	}


	public void setOrdemLinha(Integer ordemLinha) {
		this.ordemLinha = ordemLinha;
	}


	public Integer getOrdemColuna() {
		if(ordemColuna == null) {
			ordemColuna = 0;
		}
		return ordemColuna;
	}


	public void setOrdemColuna(Integer ordemColuna) {
		this.ordemColuna = ordemColuna;
	}	

	public String getLabel3() {
		return label3;
	}


	public void setLabel3(String label3) {
		this.label3 = label3;
	}

	public Integer getOrdemColuna2() {
		return ordemColuna2;
	}


	public void setOrdemColuna2(Integer ordemColuna2) {
		this.ordemColuna2 = ordemColuna2;
	}


	public DiarioFrequenciaAulaVO clone() throws CloneNotSupportedException {
		return (DiarioFrequenciaAulaVO) super.clone();		
	}
}
