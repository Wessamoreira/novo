/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

/**
 *
 * @author Otimize-TI
 */
public class DiarioRegistroAulaVO extends SuperVO{
    
    protected TurmaVO turma;
    protected DisciplinaVO disciplina;
    protected PessoaVO professor;
    protected ConfiguracaoAcademicoVO configuracaoAcademico;
    protected Integer indice;
    
    protected String data1;
    protected String data2;
    protected String data3;
    protected String data4;
    protected String data5;
    protected String data6;
    protected String data7;
    protected String data8;
    protected String data9;
    protected String data10;
    protected String data11;
    protected String data12;
    protected String data13;
    protected String data14;
    protected String data15;
    protected String data16;
    protected String data17;
    protected String data18;
    protected String data19;
    protected String data20;
    protected String data21;
    protected String data22;
    protected String data23;
    protected String data24;
    protected String data25;
    protected String data26;
    protected String data27;
    protected String data28;
    protected String data29;
    protected String data30;
    protected Integer objeto;
    protected String dataEmissao;
    protected Double cargaHoraria;
    protected Integer cargaHorariaTotal;
    protected String conteudo;
    protected String cargaHorariaStr;
    protected String situacaoApresentar;	
    private Integer cargaHorariaDisciplina;
    protected List<DiarioFrequenciaAulaVO> diarioFrequenciaVOs;

    //Dados para geração relatório
    private String diaSemanaData1;
    private String diaSemanaData2;
    private String diaSemanaData3;
    private String diaSemanaData4;
    private String mesCorrespondente;
    private Boolean apresentarSituacaoMatricula;  
    private Integer qtdAulaPrevista;
    private Integer qtdAulaMinistrada;

    private List<CrosstabVO> crosstabVOs;
    
    public DiarioRegistroAulaVO(){
        
        setProfessor(new PessoaVO());
        setDisciplina(new DisciplinaVO());
        setConfiguracaoAcademico(new ConfiguracaoAcademicoVO());
        setTurma(new TurmaVO());
               
        setData1("");
        setData2("");
        setData3("");
        setData4("");
        setData5("");
        setData6("");
        setData7("");
        setData8("");
        setData9("");
        setData10("");
        setData11("");
        setData12("");
        setData13("");
        setData14("");
        setData15("");
        setData16("");
        setData17("");
        setData18("");
        setData19("");
        setData20("");
        setData21("");
        setData22("");
        setData23("");
        setData24("");
        setData25("");
        setData26("");
        setData27("");
        setData28("");
        setData29("");
        setData30("");
        setDataEmissao("");
        setCargaHoraria(0.0);
        setConteudo("");
        setObjeto(0);
        
        setDiarioFrequenciaVOs(new ArrayList(0));
    }
    
    @Override
    public DiarioRegistroAulaVO clone() throws CloneNotSupportedException {
    	return (DiarioRegistroAulaVO) super.clone();
    }
    
    
     public void adicionarObjFrequenciaAulaVOs(DiarioFrequenciaAulaVO obj) throws Exception {        
        int index = 0;
        Iterator i = getDiarioFrequenciaVOs().iterator();
        while (i.hasNext()) {
            DiarioFrequenciaAulaVO objExistente = (DiarioFrequenciaAulaVO)i.next();
            if (objExistente.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula())) {
                getDiarioFrequenciaVOs().set( index , obj );
                return;
            }
            index++;
        }
        getDiarioFrequenciaVOs().add( obj );
    }

    public DiarioFrequenciaAulaVO consultarObjFrequenciaAulaVO(String matricula) throws Exception {
        Iterator i = getDiarioFrequenciaVOs().iterator();
        while (i.hasNext()) {
            DiarioFrequenciaAulaVO objExistente = (DiarioFrequenciaAulaVO)i.next();
            if (objExistente.getMatricula().getMatricula().equals(matricula)) {
                return objExistente;
            }
        }
        return new DiarioFrequenciaAulaVO();
    }

    public Integer getObjeto() {
        return objeto;
    }

    public void setObjeto(Integer objeto) {
        this.objeto = objeto;
    }

    
    public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
        return configuracaoAcademico;
    }

    public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
        this.configuracaoAcademico = configuracaoAcademico;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData10() {
        return data10;
    }

    public void setData10(String data10) {
        this.data10 = data10;
    }

    public String getData11() {
        return data11;
    }

    public void setData11(String data11) {
        this.data11 = data11;
    }

    public String getData12() {
        return data12;
    }

    public void setData12(String data12) {
        this.data12 = data12;
    }

    public String getData13() {
        return data13;
    }

    public void setData13(String data13) {
        this.data13 = data13;
    }

    public String getData14() {
        return data14;
    }

    public void setData14(String data14) {
        this.data14 = data14;
    }

    public String getData15() {
        return data15;
    }

    public void setData15(String data15) {
        this.data15 = data15;
    }

    public String getData16() {
        return data16;
    }

    public void setData16(String data16) {
        this.data16 = data16;
    }

    public String getData17() {
        return data17;
    }

    public void setData17(String data17) {
        this.data17 = data17;
    }

    public String getData18() {
        return data18;
    }

    public void setData18(String data18) {
        this.data18 = data18;
    }

    public String getData19() {
        return data19;
    }

    public void setData19(String data19) {
        this.data19 = data19;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData20() {
        return data20;
    }

    public void setData20(String data20) {
        this.data20 = data20;
    }

    public String getData21() {
        return data21;
    }

    public void setData21(String data21) {
        this.data21 = data21;
    }

    public String getData22() {
        return data22;
    }

    public void setData22(String data22) {
        this.data22 = data22;
    }

    public String getData23() {
        return data23;
    }

    public void setData23(String data23) {
        this.data23 = data23;
    }

    public String getData24() {
        return data24;
    }

    public void setData24(String data24) {
        this.data24 = data24;
    }

    public String getData25() {
        return data25;
    }

    public void setData25(String data25) {
        this.data25 = data25;
    }

    public String getData26() {
        return data26;
    }

    public void setData26(String data26) {
        this.data26 = data26;
    }

    public String getData27() {
        return data27;
    }

    public void setData27(String data27) {
        this.data27 = data27;
    }

    public String getData28() {
        return data28;
    }

    public void setData28(String data28) {
        this.data28 = data28;
    }

    public String getData29() {
        return data29;
    }

    public void setData29(String data29) {
        this.data29 = data29;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData30() {
        return data30;
    }

    public void setData30(String data30) {
        this.data30 = data30;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public String getData6() {
        return data6;
    }

    public void setData6(String data6) {
        this.data6 = data6;
    }

    public String getData7() {
        return data7;
    }

    public void setData7(String data7) {
        this.data7 = data7;
    }

    public String getData8() {
        return data8;
    }

    public void setData8(String data8) {
        this.data8 = data8;
    }

    public String getData9() {
        return data9;
    }

    public void setData9(String data9) {
        this.data9 = data9;
    }

    public DisciplinaVO getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public PessoaVO getProfessor() {
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public TurmaVO getTurma() {
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public List<DiarioFrequenciaAulaVO> getDiarioFrequenciaVOs() {
        return diarioFrequenciaVOs;
    }

    public void setDiarioFrequenciaVOs(List<DiarioFrequenciaAulaVO> diarioFrequenciaVOs) {
        this.diarioFrequenciaVOs = diarioFrequenciaVOs;
    }
    
    public JRDataSource getListaFrequencia(){
        JRDataSource jr = new JRBeanArrayDataSource(getDiarioFrequenciaVOs().toArray());
        return jr;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    /**
     * @return the cargaHoraria
     */
    public Double getCargaHoraria() {
        return cargaHoraria;
    }

    /**
     * @param cargaHoraria the cargaHoraria to set
     */
    public void setCargaHoraria(Double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getCargaHorariaStr() {
        return cargaHorariaStr;
    }

    public void setCargaHorariaStr(String cargaHorariaStr) {
        this.cargaHorariaStr = cargaHorariaStr;
    }

    /**
     * @return the diaSemanaData1
     */
    public String getDiaSemanaData1() {
        if(diaSemanaData1 == null){
            diaSemanaData1 = "";
        }
        return diaSemanaData1;
    }

    /**
     * @param diaSemanaData1 the diaSemanaData1 to set
     */
    public void setDiaSemanaData1(String diaSemanaData1) {
        this.diaSemanaData1 = diaSemanaData1;
    }

    /**
     * @return the diaSemanaData2
     */
    public String getDiaSemanaData2() {
        if(diaSemanaData2 == null){
            diaSemanaData2 = "";
        }
        return diaSemanaData2;
    }

    /**
     * @param diaSemanaData2 the diaSemanaData2 to set
     */
    public void setDiaSemanaData2(String diaSemanaData2) {
        this.diaSemanaData2 = diaSemanaData2;
    }

    /**
     * @return the diaSemanaData3
     */
    public String getDiaSemanaData3() {
        if(diaSemanaData3 == null){
            diaSemanaData3 = "";
        }
        return diaSemanaData3;
    }

    /**
     * @param diaSemanaData3 the diaSemanaData3 to set
     */
    public void setDiaSemanaData3(String diaSemanaData3) {
        this.diaSemanaData3 = diaSemanaData3;
    }

    /**
     * @return the diaSemanaData4
     */
    public String getDiaSemanaData4() {
        if(diaSemanaData4 == null){
            diaSemanaData4 = "";
        }
        return diaSemanaData4;
    }

    /**
     * @param diaSemanaData4 the diaSemanaData4 to set
     */
    public void setDiaSemanaData4(String diaSemanaData4) {
        this.diaSemanaData4 = diaSemanaData4;
    }

	public String getMesCorrespondente() {
		if(mesCorrespondente == null){
			mesCorrespondente = "";
		}
		return mesCorrespondente;
	}

	public void setMesCorrespondente(String mesCorrespondente) {
		this.mesCorrespondente = mesCorrespondente;
	}

	public Integer getCargaHorariaTotal() {
		if (cargaHorariaTotal == null) {
			cargaHorariaTotal = 0;
		}
		return cargaHorariaTotal;
	}

	public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}

	public Integer getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = 0;
		}
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	public List<CrosstabVO> getCrosstabVOs() {
		if (crosstabVOs == null) {
			crosstabVOs = new ArrayList<CrosstabVO>(0);
		}
		return crosstabVOs;
	}

	public void setCrosstabVOs(List<CrosstabVO> crosstabVOs) {
		this.crosstabVOs = crosstabVOs;
	}

	public Integer getIndice() {
		if (indice == null) {
			indice = 1;
		}
		return indice;
	}

	public void setIndice(Integer indice) {
		this.indice = indice;
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

	public String getSituacaoApresentar() {
		if (situacaoApresentar == null) {
			situacaoApresentar = "";
		}
		return situacaoApresentar;
	}

	public void setSituacaoApresentar(String situacaoApresentar) {
		this.situacaoApresentar = situacaoApresentar;
	}

	public Integer getQtdAulaPrevista() {
		if(qtdAulaPrevista == null) {
			qtdAulaPrevista= 0;
		}
		return qtdAulaPrevista;
	}

	public void setQtdAulaPrevista(Integer qtdAulaPrevista) {
		this.qtdAulaPrevista = qtdAulaPrevista;
	}

	public Integer getQtdAulaMinistrada() {
		if(qtdAulaMinistrada == null) {
			qtdAulaMinistrada= 0;
		}
		return qtdAulaMinistrada;
	}

	public void setQtdAulaMinistrada(Integer qtdAulaMinistrada) {
		this.qtdAulaMinistrada = qtdAulaMinistrada;
	}	
	    
}
