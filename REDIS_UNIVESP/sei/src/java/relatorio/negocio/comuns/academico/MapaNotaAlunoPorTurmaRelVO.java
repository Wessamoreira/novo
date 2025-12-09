package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import relatorio.negocio.comuns.arquitetura.CrosstabVO;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.utilitarias.Uteis;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class MapaNotaAlunoPorTurmaRelVO {

    private String ano;
    private String turma;
    private String semestre;
    private String nomeUnidadeEnsino;
    private List<DisciplinaVO> listaDisciplinas;
    private List<MapaNotaAlunoPorTurmaAlunoRelVO> mapaNotaAlunoPorTurmaAlunoRelVOs;
    private String nome;
    private String matricula;
    private String disciplina1;
    private String disciplina2;
    private String disciplina3;
    private String disciplina4;
    private String disciplina5;
    private String disciplina6;
    private String disciplina7;
    private String disciplina8;
    private String disciplina9;
    private String disciplina10;
    private String disciplina11;
    private String disciplina12;
    private String disciplina13;
    private String disciplina14;
    private String disciplina15;
    private String disciplina16;
    private String disciplina17;
    private String disciplina18;
    private String disciplina19;
    private String disciplina20;
    private String disciplina21;
    private String disciplina22;
    private String disciplina23;
    private String disciplina24;
    private String situacao1;
    private String situacao2;
    private String situacao3;
    private String situacao4;
    private String situacao5;
    private String situacao6;
    private String situacao7;
    private String situacao8;
    private String situacao9;
    private String situacao10;
    private String situacao11;
    private String situacao12;
    private String situacao13;
    private String situacao14;
    private String situacao15;
    private String situacao16;
    private String situacao17;
    private String situacao18;
    private String situacao19;
    private String situacao20;
    private String situacao21;
    private String situacao22;
    private String situacao23;
    private String situacao24;
    private Date data1;
    private Date data2;
    private Date data3;
    private Date data4;
    private Date data5;
    private Date data6;
    private Date data7;
    private Date data8;
    private Date data9;
    private Date data10;
    private Date data11;
    private Date data12;
    private Date data13;
    private Date data14;
    private Date data15;
    private Date data16;
    private Date data17;
    private Date data18;
    private Date data19;
    private Date data20;
    private Date data21;
    private Date data22;
    private Date data23;
    private Date data24;
    private Double nota1;
    private Double nota2;
    private Double nota3;
    private Double nota4;
    private Double nota5;
    private Double nota6;
    private Double nota7;
    private Double nota8;
    private Double nota9;
    private Double nota10;
    private Double nota11;
    private Double nota12;
    private Double nota13;
    private Double nota14;
    private Double nota15;
    private Double nota16;
    private Double nota17;
    private Double nota18;
    private Double nota19;
    private Double nota20;
    private Double nota21;
    private Double nota22;
    private Double nota23;
    private Double nota24;
    private String conceito1;
    private String conceito2;
    private String conceito3;
    private String conceito4;
    private String conceito5;
    private String conceito6;
    private String conceito7;
    private String conceito8;
    private String conceito9;
    private String conceito10;
    private String conceito11;
    private String conceito12;
    private String conceito13;
    private String conceito14;
    private String conceito15;
    private String conceito16;
    private String conceito17;
    private String conceito18;
    private String conceito19;
    private String conceito20;
    private String conceito21;
    private String conceito22;
    private String conceito23;
    private String conceito24;
    private Double frequencia1;
    private Double frequencia2;
    private Double frequencia3;
    private Double frequencia4;
    private Double frequencia5;
    private Double frequencia6;
    private Double frequencia7;
    private Double frequencia8;
    private Double frequencia9;
    private Double frequencia10;
    private Double frequencia11;
    private Double frequencia12;
    private Double frequencia13;
    private Double frequencia14;
    private Double frequencia15;
    private Double frequencia16;
    private Double frequencia17;
    private Double frequencia18;
    private Double frequencia19;
    private Double frequencia20;
    private Double frequencia21;
    private Double frequencia22;
    private Double frequencia23;
    private Double frequencia24;
    //Usado para apresentar uma única vez a disciplina
    private String disciplinaAnterior;
    private String situacaoAnterior;
    private Boolean voltarIndex;
    private Boolean pularIndex;
    
    private String curso;
    private String turno;
    private String disciplina;
    private String sala;
    private String professor;
    private List<CrosstabVO> crosstabVOs;
    private List<HistoricoVO> historicoVOs;
    private String nomeMatrizCurricular;
    private String periodoLetivo;
    private String dataModulo;

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public List<DisciplinaVO> getListaDisciplinas() {
		if (listaDisciplinas == null) {
			listaDisciplinas = new ArrayList<DisciplinaVO>(0);
		}
        return listaDisciplinas;
    }

    public void setListaDisciplinas(List<DisciplinaVO> listaDisciplinas) {
        this.listaDisciplinas = listaDisciplinas;
    }

    public JRDataSource getListaDisciplinasRelatorio() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaDisciplinas().toArray());
        return jr;
    }

    public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null) {
            nomeUnidadeEnsino = "";
        }
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    public void setMapaNotaAlunoPorTurmaAlunoRelVOs(List<MapaNotaAlunoPorTurmaAlunoRelVO> mapaNotaAlunoPorTurmaAlunoRelVOs) {
        this.mapaNotaAlunoPorTurmaAlunoRelVOs = mapaNotaAlunoPorTurmaAlunoRelVOs;
    }

    public List<MapaNotaAlunoPorTurmaAlunoRelVO> getMapaNotaAlunoPorTurmaAlunoRelVOs() {
        if (mapaNotaAlunoPorTurmaAlunoRelVOs == null) {
            mapaNotaAlunoPorTurmaAlunoRelVOs = new ArrayList<MapaNotaAlunoPorTurmaAlunoRelVO>(0);
        }
        return mapaNotaAlunoPorTurmaAlunoRelVOs;
    }

    public JRDataSource getListaAlunosRelatorio() {
        return new JRBeanArrayDataSource(getMapaNotaAlunoPorTurmaAlunoRelVOs().toArray());
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getTurma() {
    	 if (turma == null) {
    		 turma = "";
    	 }
        return turma;
    }

    /**
     * @return the disciplina1
     */
    public String getDisciplina1() {
        if (disciplina1 == null) {
            disciplina1 = "";
        }
        return disciplina1;
    }

    /**
     * @param disciplina1 the disciplina1 to set
     */
    public void setDisciplina1(String disciplina1) {
        this.disciplina1 = disciplina1;
    }

    /**
     * @return the disciplina2
     */
    public String getDisciplina2() {
        if (disciplina2 == null) {
            disciplina2 = "";
        }
        return disciplina2;
    }

    /**
     * @param disciplina2 the disciplina2 to set
     */
    public void setDisciplina2(String disciplina2) {
        this.disciplina2 = disciplina2;
    }

    /**
     * @return the disciplina3
     */
    public String getDisciplina3() {
        if (disciplina3 == null) {
            disciplina3 = "";
        }
        return disciplina3;
    }

    /**
     * @param disciplina3 the disciplina3 to set
     */
    public void setDisciplina3(String disciplina3) {
        this.disciplina3 = disciplina3;
    }

    /**
     * @return the disciplina4
     */
    public String getDisciplina4() {
        if (disciplina4 == null) {
            disciplina4 = "";
        }
        return disciplina4;
    }

    /**
     * @param disciplina4 the disciplina4 to set
     */
    public void setDisciplina4(String disciplina4) {
        this.disciplina4 = disciplina4;
    }

    /**
     * @return the disciplina5
     */
    public String getDisciplina5() {
        if (disciplina5 == null) {
            disciplina5 = "";
        }
        return disciplina5;
    }

    /**
     * @param disciplina5 the disciplina5 to set
     */
    public void setDisciplina5(String disciplina5) {
        this.disciplina5 = disciplina5;
    }

    /**
     * @return the disciplina6
     */
    public String getDisciplina6() {
        if (disciplina6 == null) {
            disciplina6 = "";
        }
        return disciplina6;
    }

    /**
     * @param disciplina6 the disciplina6 to set
     */
    public void setDisciplina6(String disciplina6) {
        this.disciplina6 = disciplina6;
    }

    /**
     * @return the disciplina7
     */
    public String getDisciplina7() {
        if (disciplina7 == null) {
            disciplina7 = "";
        }
        return disciplina7;
    }

    /**
     * @param disciplina7 the disciplina7 to set
     */
    public void setDisciplina7(String disciplina7) {
        this.disciplina7 = disciplina7;
    }

    /**
     * @return the disciplina8
     */
    public String getDisciplina8() {
        if (disciplina8 == null) {
            disciplina8 = "";
        }
        return disciplina8;
    }

    /**
     * @param disciplina8 the disciplina8 to set
     */
    public void setDisciplina8(String disciplina8) {
        this.disciplina8 = disciplina8;
    }

    /**
     * @return the disciplina9
     */
    public String getDisciplina9() {
        if (disciplina9 == null) {
            disciplina9 = "";
        }
        return disciplina9;
    }

    /**
     * @param disciplina9 the disciplina9 to set
     */
    public void setDisciplina9(String disciplina9) {
        this.disciplina9 = disciplina9;
    }

    /**
     * @return the disciplina10
     */
    public String getDisciplina10() {
        if (disciplina10 == null) {
            disciplina10 = "";
        }
        return disciplina10;
    }

    /**
     * @param disciplina10 the disciplina10 to set
     */
    public void setDisciplina10(String disciplina10) {
        this.disciplina10 = disciplina10;
    }

    /**
     * @return the disciplina11
     */
    public String getDisciplina11() {
        if (disciplina11 == null) {
            disciplina11 = "";
        }
        return disciplina11;
    }

    /**
     * @param disciplina11 the disciplina11 to set
     */
    public void setDisciplina11(String disciplina11) {
        this.disciplina11 = disciplina11;
    }

    /**
     * @return the disciplina12
     */
    public String getDisciplina12() {
        if (disciplina12 == null) {
            disciplina12 = "";
        }
        return disciplina12;
    }

    /**
     * @param disciplina12 the disciplina12 to set
     */
    public void setDisciplina12(String disciplina12) {
        this.disciplina12 = disciplina12;
    }

    /**
     * @return the disciplina13
     */
    public String getDisciplina13() {
        if (disciplina13 == null) {
            disciplina13 = "";
        }
        return disciplina13;
    }

    /**
     * @param disciplina13 the disciplina13 to set
     */
    public void setDisciplina13(String disciplina13) {
        this.disciplina13 = disciplina13;
    }

    /**
     * @return the disciplina14
     */
    public String getDisciplina14() {
        if (disciplina14 == null) {
            disciplina14 = "";
        }
        return disciplina14;
    }

    /**
     * @param disciplina14 the disciplina14 to set
     */
    public void setDisciplina14(String disciplina14) {
        this.disciplina14 = disciplina14;
    }

    /**
     * @return the disciplina15
     */
    public String getDisciplina15() {
        if (disciplina15 == null) {
            disciplina15 = "";
        }
        return disciplina15;
    }

    /**
     * @param disciplina15 the disciplina15 to set
     */
    public void setDisciplina15(String disciplina15) {
        this.disciplina15 = disciplina15;
    }

    /**
     * @return the disciplina16
     */
    public String getDisciplina16() {
        if (disciplina16 == null) {
            disciplina16 = "";
        }
        return disciplina16;
    }

    /**
     * @param disciplina16 the disciplina16 to set
     */
    public void setDisciplina16(String disciplina16) {
        this.disciplina16 = disciplina16;
    }

    /**
     * @return the disciplina17
     */
    public String getDisciplina17() {
        if (disciplina17 == null) {
            disciplina17 = "";
        }
        return disciplina17;
    }

    /**
     * @param disciplina17 the disciplina17 to set
     */
    public void setDisciplina17(String disciplina17) {
        this.disciplina17 = disciplina17;
    }

    /**
     * @return the disciplina18
     */
    public String getDisciplina18() {
        if (disciplina18 == null) {
            disciplina18 = "";
        }
        return disciplina18;
    }

    /**
     * @param disciplina18 the disciplina18 to set
     */
    public void setDisciplina18(String disciplina18) {
        this.disciplina18 = disciplina18;
    }

    /**
     * @return the disciplina19
     */
    public String getDisciplina19() {
        if (disciplina19 == null) {
            disciplina19 = "";
        }
        return disciplina19;
    }

    /**
     * @param disciplina19 the disciplina19 to set
     */
    public void setDisciplina19(String disciplina19) {
        this.disciplina19 = disciplina19;
    }

    /**
     * @return the disciplina20
     */
    public String getDisciplina20() {
        if (disciplina20 == null) {
            disciplina20 = "";
        }
        return disciplina20;
    }

    /**
     * @param disciplina20 the disciplina20 to set
     */
    public void setDisciplina20(String disciplina20) {
        this.disciplina20 = disciplina20;
    }

    /**
     * @return the disciplina21
     */
    public String getDisciplina21() {
        if (disciplina21 == null) {
            disciplina21 = "";
        }
        return disciplina21;
    }

    /**
     * @param disciplina21 the disciplina21 to set
     */
    public void setDisciplina21(String disciplina21) {
        this.disciplina21 = disciplina21;
    }

    /**
     * @return the disciplina22
     */
    public String getDisciplina22() {
        if (disciplina22 == null) {
            disciplina22 = "";
        }
        return disciplina22;
    }

    /**
     * @param disciplina22 the disciplina22 to set
     */
    public void setDisciplina22(String disciplina22) {
        this.disciplina22 = disciplina22;
    }

    /**
     * @return the disciplina23
     */
    public String getDisciplina23() {
        if (disciplina23 == null) {
            disciplina23 = "";
        }
        return disciplina23;
    }

    /**
     * @param disciplina23 the disciplina23 to set
     */
    public void setDisciplina23(String disciplina23) {
        this.disciplina23 = disciplina23;
    }

    /**
     * @return the disciplina24
     */
    public String getDisciplina24() {
        if (disciplina24 == null) {
            disciplina24 = "";
        }
        return disciplina24;
    }

    /**
     * @param disciplina24 the disciplina24 to set
     */
    public void setDisciplina24(String disciplina24) {
        this.disciplina24 = disciplina24;
    }

    /**
     * @return the data1
     */
    public Date getData1() {
        return data1;
    }

    /**
     * @param data1 the data1 to set
     */
    public void setData1(Date data1) {
        this.data1 = data1;
    }

    /**
     * @return the data2
     */
    public Date getData2() {
        return data2;
    }

    /**
     * @param data2 the data2 to set
     */
    public void setData2(Date data2) {
        this.data2 = data2;
    }

    /**
     * @return the data3
     */
    public Date getData3() {
        return data3;
    }

    /**
     * @param data3 the data3 to set
     */
    public void setData3(Date data3) {
        this.data3 = data3;
    }

    /**
     * @return the data4
     */
    public Date getData4() {
        return data4;
    }

    /**
     * @param data4 the data4 to set
     */
    public void setData4(Date data4) {
        this.data4 = data4;
    }

    /**
     * @return the data5
     */
    public Date getData5() {
        return data5;
    }

    /**
     * @param data5 the data5 to set
     */
    public void setData5(Date data5) {
        this.data5 = data5;
    }

    /**
     * @return the data6
     */
    public Date getData6() {
        return data6;
    }

    /**
     * @param data6 the data6 to set
     */
    public void setData6(Date data6) {
        this.data6 = data6;
    }

    /**
     * @return the data7
     */
    public Date getData7() {
        return data7;
    }

    /**
     * @param data7 the data7 to set
     */
    public void setData7(Date data7) {
        this.data7 = data7;
    }

    /**
     * @return the data8
     */
    public Date getData8() {
        return data8;
    }

    /**
     * @param data8 the data8 to set
     */
    public void setData8(Date data8) {
        this.data8 = data8;
    }

    /**
     * @return the data9
     */
    public Date getData9() {
        return data9;
    }

    /**
     * @param data9 the data9 to set
     */
    public void setData9(Date data9) {
        this.data9 = data9;
    }

    /**
     * @return the data10
     */
    public Date getData10() {
        return data10;
    }

    /**
     * @param data10 the data10 to set
     */
    public void setData10(Date data10) {
        this.data10 = data10;
    }

    /**
     * @return the data11
     */
    public Date getData11() {
        return data11;
    }

    /**
     * @param data11 the data11 to set
     */
    public void setData11(Date data11) {
        this.data11 = data11;
    }

    /**
     * @return the data12
     */
    public Date getData12() {
        return data12;
    }

    /**
     * @param data12 the data12 to set
     */
    public void setData12(Date data12) {
        this.data12 = data12;
    }

    /**
     * @return the data13
     */
    public Date getData13() {
        return data13;
    }

    /**
     * @param data13 the data13 to set
     */
    public void setData13(Date data13) {
        this.data13 = data13;
    }

    /**
     * @return the data14
     */
    public Date getData14() {
        return data14;
    }

    /**
     * @param data14 the data14 to set
     */
    public void setData14(Date data14) {
        this.data14 = data14;
    }

    /**
     * @return the data15
     */
    public Date getData15() {
        return data15;
    }

    /**
     * @param data15 the data15 to set
     */
    public void setData15(Date data15) {
        this.data15 = data15;
    }

    /**
     * @return the data16
     */
    public Date getData16() {
        return data16;
    }

    /**
     * @param data16 the data16 to set
     */
    public void setData16(Date data16) {
        this.data16 = data16;
    }

    /**
     * @return the data17
     */
    public Date getData17() {
        return data17;
    }

    /**
     * @param data17 the data17 to set
     */
    public void setData17(Date data17) {
        this.data17 = data17;
    }

    /**
     * @return the data18
     */
    public Date getData18() {
        return data18;
    }

    /**
     * @param data18 the data18 to set
     */
    public void setData18(Date data18) {
        this.data18 = data18;
    }

    /**
     * @return the data19
     */
    public Date getData19() {
        return data19;
    }

    /**
     * @param data19 the data19 to set
     */
    public void setData19(Date data19) {
        this.data19 = data19;
    }

    /**
     * @return the data20
     */
    public Date getData20() {
        return data20;
    }

    /**
     * @param data20 the data20 to set
     */
    public void setData20(Date data20) {
        this.data20 = data20;
    }

    /**
     * @return the data21
     */
    public Date getData21() {
        return data21;
    }

    /**
     * @param data21 the data21 to set
     */
    public void setData21(Date data21) {
        this.data21 = data21;
    }

    /**
     * @return the data22
     */
    public Date getData22() {
        return data22;
    }

    /**
     * @param data22 the data22 to set
     */
    public void setData22(Date data22) {
        this.data22 = data22;
    }

    /**
     * @return the data23
     */
    public Date getData23() {
        return data23;
    }

    /**
     * @param data23 the data23 to set
     */
    public void setData23(Date data23) {
        this.data23 = data23;
    }

    /**
     * @return the data24
     */
    public Date getData24() {
        return data24;
    }

    public void setData24(Date data24) {
        this.data24 = data24;
    }

    public Double getNota1() {
        return nota1;
    }

    public void setNota1(Double nota1) {
        this.nota1 = nota1;
    }

    public Double getNota2() {
        return nota2;
    }

    public void setNota2(Double nota2) {
        this.nota2 = nota2;
    }

    public Double getNota3() {
        return nota3;
    }

    public void setNota3(Double nota3) {
        this.nota3 = nota3;
    }

    public Double getNota4() {
        return nota4;
    }

    public void setNota4(Double nota4) {
        this.nota4 = nota4;
    }

    public Double getNota5() {
        return nota5;
    }

    public void setNota5(Double nota5) {
        this.nota5 = nota5;
    }

    public Double getNota6() {
        return nota6;
    }

    public void setNota6(Double nota6) {
        this.nota6 = nota6;
    }

    public Double getNota7() {
        return nota7;
    }

    public void setNota7(Double nota7) {
        this.nota7 = nota7;
    }

    public Double getNota8() {
        return nota8;
    }

    public void setNota8(Double nota8) {
        this.nota8 = nota8;
    }

    public Double getNota9() {
        return nota9;
    }

    public void setNota9(Double nota9) {
        this.nota9 = nota9;
    }

    public Double getNota10() {
        return nota10;
    }

    public void setNota10(Double nota10) {
        this.nota10 = nota10;
    }

    public Double getNota11() {
        return nota11;
    }

    public void setNota11(Double nota11) {
        this.nota11 = nota11;
    }

    public Double getNota12() {
        return nota12;
    }

    public void setNota12(Double nota12) {
        this.nota12 = nota12;
    }

    public Double getNota13() {
        return nota13;
    }

    public void setNota13(Double nota13) {
        this.nota13 = nota13;
    }

    public Double getNota14() {
        return nota14;
    }

    public void setNota14(Double nota14) {
        this.nota14 = nota14;
    }

    public Double getNota15() {
        return nota15;
    }

    public void setNota15(Double nota15) {
        this.nota15 = nota15;
    }

    public Double getNota16() {
        return nota16;
    }

    public void setNota16(Double nota16) {
        this.nota16 = nota16;
    }

    public Double getNota17() {
        return nota17;
    }

    public void setNota17(Double nota17) {
        this.nota17 = nota17;
    }

    public Double getNota18() {
        return nota18;
    }

    public void setNota18(Double nota18) {
        this.nota18 = nota18;
    }

    public Double getNota19() {
        return nota19;
    }

    public void setNota19(Double nota19) {
        this.nota19 = nota19;
    }

    public Double getNota20() {
        return nota20;
    }

    public void setNota20(Double nota20) {
        this.nota20 = nota20;
    }

    public Double getNota21() {
        return nota21;
    }

    public void setNota21(Double nota21) {
        this.nota21 = nota21;
    }

    public Double getNota22() {
        return nota22;
    }

    public void setNota22(Double nota22) {
        this.nota22 = nota22;
    }

    public Double getNota23() {
        return nota23;
    }

    public void setNota23(Double nota23) {
        this.nota23 = nota23;
    }

    public Double getNota24() {
        return nota24;
    }

    public void setNota24(Double nota24) {
        this.nota24 = nota24;
    }

    public Double getFrequencia1() {
        return frequencia1;
    }

    public void setFrequencia1(Double frequencia1) {
        this.frequencia1 = frequencia1;
    }

    public Double getFrequencia2() {
        return frequencia2;
    }

    public void setFrequencia2(Double frequencia2) {
        this.frequencia2 = frequencia2;
    }

    public Double getFrequencia3() {
        return frequencia3;
    }

    public void setFrequencia3(Double frequencia3) {
        this.frequencia3 = frequencia3;
    }

    public Double getFrequencia4() {
        return frequencia4;
    }

    public void setFrequencia4(Double frequencia4) {
        this.frequencia4 = frequencia4;
    }

    public Double getFrequencia5() {
        return frequencia5;
    }

    public void setFrequencia5(Double frequencia5) {
        this.frequencia5 = frequencia5;
    }

    public Double getFrequencia6() {
        return frequencia6;
    }

    public void setFrequencia6(Double frequencia6) {
        this.frequencia6 = frequencia6;
    }

    public Double getFrequencia7() {
        return frequencia7;
    }

    public void setFrequencia7(Double frequencia7) {
        this.frequencia7 = frequencia7;
    }

    public Double getFrequencia8() {
        return frequencia8;
    }

    public void setFrequencia8(Double frequencia8) {
        this.frequencia8 = frequencia8;
    }

    public Double getFrequencia9() {
        return frequencia9;
    }

    public void setFrequencia9(Double frequencia9) {
        this.frequencia9 = frequencia9;
    }

    public Double getFrequencia10() {
        return frequencia10;
    }

    public void setFrequencia10(Double frequencia10) {
        this.frequencia10 = frequencia10;
    }

    public Double getFrequencia11() {
        return frequencia11;
    }

    public void setFrequencia11(Double frequencia11) {
        this.frequencia11 = frequencia11;
    }

    public Double getFrequencia12() {
        return frequencia12;
    }

    public void setFrequencia12(Double frequencia12) {
        this.frequencia12 = frequencia12;
    }

    public Double getFrequencia13() {
        return frequencia13;
    }

    public void setFrequencia13(Double frequencia13) {
        this.frequencia13 = frequencia13;
    }

    public Double getFrequencia14() {
        return frequencia14;
    }

    public void setFrequencia14(Double frequencia14) {
        this.frequencia14 = frequencia14;
    }

    public Double getFrequencia15() {
        return frequencia15;
    }

    public void setFrequencia15(Double frequencia15) {
        this.frequencia15 = frequencia15;
    }

    public Double getFrequencia16() {
        return frequencia16;
    }

    public void setFrequencia16(Double frequencia16) {
        this.frequencia16 = frequencia16;
    }

    public Double getFrequencia17() {
        return frequencia17;
    }

    public void setFrequencia17(Double frequencia17) {
        this.frequencia17 = frequencia17;
    }

    public Double getFrequencia18() {
        return frequencia18;
    }

    public void setFrequencia18(Double frequencia18) {
        this.frequencia18 = frequencia18;
    }

    public Double getFrequencia19() {
        return frequencia19;
    }

    public void setFrequencia19(Double frequencia19) {
        this.frequencia19 = frequencia19;
    }

    public Double getFrequencia20() {
        return frequencia20;
    }

    public void setFrequencia20(Double frequencia20) {
        this.frequencia20 = frequencia20;
    }

    public Double getFrequencia21() {
        return frequencia21;
    }

    public void setFrequencia21(Double frequencia21) {
        this.frequencia21 = frequencia21;
    }

    public Double getFrequencia22() {
        return frequencia22;
    }

    public void setFrequencia22(Double frequencia22) {
        this.frequencia22 = frequencia22;
    }

    public Double getFrequencia23() {
        return frequencia23;
    }

    public void setFrequencia23(Double frequencia23) {
        this.frequencia23 = frequencia23;
    }

    public Double getFrequencia24() {
        return frequencia24;
    }

    public void setFrequencia24(Double frequencia24) {
        this.frequencia24 = frequencia24;
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

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getData1_Apresentar() {
        return (Uteis.getData(data1));
    }

    public String getData2_Apresentar() {
        return (Uteis.getData(data2));
    }

    public String getData3_Apresentar() {
        return (Uteis.getData(data3));
    }

    public String getData4_Apresentar() {
        return (Uteis.getData(data4));
    }

    public String getData5_Apresentar() {
        return (Uteis.getData(data5));
    }

    public String getData6_Apresentar() {
        return (Uteis.getData(data6));
    }

    public String getData7_Apresentar() {
        return (Uteis.getData(data7));
    }

    public String getData8_Apresentar() {
        return (Uteis.getData(data8));
    }

    public String getData9_Apresentar() {
        return (Uteis.getData(data9));
    }

    public String getData10_Apresentar() {
        return (Uteis.getData(data10));
    }

    public String getData11_Apresentar() {
        return (Uteis.getData(data11));
    }

    public String getData12_Apresentar() {
        return (Uteis.getData(data12));
    }

    public String getData13_Apresentar() {
        return (Uteis.getData(data13));
    }

    public String getData14_Apresentar() {
        return (Uteis.getData(data14));
    }

    public String getData15_Apresentar() {
        return (Uteis.getData(data15));
    }

    public String getData16_Apresentar() {
        return (Uteis.getData(data16));
    }

    public String getData17_Apresentar() {
        return (Uteis.getData(data17));
    }

    public String getData18_Apresentar() {
        return (Uteis.getData(data18));
    }

    public String getData19_Apresentar() {
        return (Uteis.getData(data19));
    }

    public String getData20_Apresentar() {
        return (Uteis.getData(data20));
    }

    public String getData21_Apresentar() {
        return (Uteis.getData(data21));
    }

    public String getData22_Apresentar() {
        return (Uteis.getData(data22));
    }

    public String getData23_Apresentar() {
        return (Uteis.getData(data23));
    }

    public String getData24_Apresentar() {
        return (Uteis.getData(data24));
    }

    public String getSituacao1() {
        return situacao1;
    }

    public void setSituacao1(String situacao1) {
        this.situacao1 = situacao1;
    }

    public String getSituacao2() {
        return situacao2;
    }

    public void setSituacao2(String situacao2) {
        this.situacao2 = situacao2;
    }

    public String getSituacao3() {
        return situacao3;
    }

    public void setSituacao3(String situacao3) {
        this.situacao3 = situacao3;
    }

    public String getSituacao4() {
        return situacao4;
    }

    public void setSituacao4(String situacao4) {
        this.situacao4 = situacao4;
    }

    public String getSituacao5() {
        return situacao5;
    }

    public void setSituacao5(String situacao5) {
        this.situacao5 = situacao5;
    }

    public String getSituacao6() {
        return situacao6;
    }

    public void setSituacao6(String situacao6) {
        this.situacao6 = situacao6;
    }

    public String getSituacao7() {
        return situacao7;
    }

    public void setSituacao7(String situacao7) {
        this.situacao7 = situacao7;
    }

    public String getSituacao8() {
        return situacao8;
    }

    public void setSituacao8(String situacao8) {
        this.situacao8 = situacao8;
    }

    public String getSituacao9() {
        return situacao9;
    }

    public void setSituacao9(String situacao9) {
        this.situacao9 = situacao9;
    }

    public String getSituacao10() {
        return situacao10;
    }

    public void setSituacao10(String situacao10) {
        this.situacao10 = situacao10;
    }

    public String getSituacao11() {
        return situacao11;
    }

    public void setSituacao11(String situacao11) {
        this.situacao11 = situacao11;
    }

    public String getSituacao12() {
        return situacao12;
    }

    public void setSituacao12(String situacao12) {
        this.situacao12 = situacao12;
    }

    public String getSituacao13() {
        return situacao13;
    }

    public void setSituacao13(String situacao13) {
        this.situacao13 = situacao13;
    }

    public String getSituacao14() {
        return situacao14;
    }

    public void setSituacao14(String situacao14) {
        this.situacao14 = situacao14;
    }

    public String getSituacao15() {
        return situacao15;
    }

    public void setSituacao15(String situacao15) {
        this.situacao15 = situacao15;
    }

    public String getSituacao16() {
        return situacao16;
    }

    public void setSituacao16(String situacao16) {
        this.situacao16 = situacao16;
    }

    public String getSituacao17() {
        return situacao17;
    }

    public void setSituacao17(String situacao17) {
        this.situacao17 = situacao17;
    }

    public String getSituacao18() {
        return situacao18;
    }

    public void setSituacao18(String situacao18) {
        this.situacao18 = situacao18;
    }

    public String getSituacao19() {
        return situacao19;
    }

    public void setSituacao19(String situacao19) {
        this.situacao19 = situacao19;
    }

    public String getSituacao20() {
        return situacao20;
    }

    public void setSituacao20(String situacao20) {
        this.situacao20 = situacao20;
    }

    public String getSituacao21() {
        return situacao21;
    }

    public void setSituacao21(String situacao21) {
        this.situacao21 = situacao21;
    }

    public String getSituacao22() {
        return situacao22;
    }

    public void setSituacao22(String situacao22) {
        this.situacao22 = situacao22;
    }

    public String getSituacao23() {
        return situacao23;
    }

    public void setSituacao23(String situacao23) {
        this.situacao23 = situacao23;
    }

    public String getSituacao24() {
        return situacao24;
    }

    public void setSituacao24(String situacao24) {
        this.situacao24 = situacao24;
    }

    public String getDisciplinaAnterior() {
        return disciplinaAnterior;
    }

    public void setDisciplinaAnterior(String disciplinaAnterior) {
        this.disciplinaAnterior = disciplinaAnterior;
    }

    public String getSituacaoAnterior() {
        return situacaoAnterior;
    }

    public void setSituacaoAnterior(String situacaoAnterior) {
        this.situacaoAnterior = situacaoAnterior;
    }

    public Boolean getPularIndex() {
        if(pularIndex == null){
            pularIndex = false;
        }
        return pularIndex;
    }

    public void setPularIndex(boolean pularIndex) {
        this.pularIndex = pularIndex;
    }

    public Boolean getVoltarIndex() {
        if(voltarIndex == null){
            voltarIndex = false;
        }
        return voltarIndex;
    }

    public void setVoltarIndex(boolean voltarIndex) {
        this.voltarIndex = voltarIndex;
    }

    
    public String getConceito1() {
        return conceito1;
    }

    
    public void setConceito1(String conceito1) {
        this.conceito1 = conceito1;
    }

    
    public String getConceito2() {
        return conceito2;
    }

    
    public void setConceito2(String conceito2) {
        this.conceito2 = conceito2;
    }

    
    public String getConceito3() {
        return conceito3;
    }

    
    public void setConceito3(String conceito3) {
        this.conceito3 = conceito3;
    }

    
    public String getConceito4() {
        return conceito4;
    }

    
    public void setConceito4(String conceito4) {
        this.conceito4 = conceito4;
    }

    
    public String getConceito5() {
        return conceito5;
    }

    
    public void setConceito5(String conceito5) {
        this.conceito5 = conceito5;
    }

    
    public String getConceito6() {
        return conceito6;
    }

    
    public void setConceito6(String conceito6) {
        this.conceito6 = conceito6;
    }

    
    public String getConceito7() {
        return conceito7;
    }

    
    public void setConceito7(String conceito7) {
        this.conceito7 = conceito7;
    }

    
    public String getConceito8() {
        return conceito8;
    }

    
    public void setConceito8(String conceito8) {
        this.conceito8 = conceito8;
    }

    
    public String getConceito9() {
        return conceito9;
    }

    
    public void setConceito9(String conceito9) {
        this.conceito9 = conceito9;
    }

    
    public String getConceito10() {
        return conceito10;
    }

    
    public void setConceito10(String conceito10) {
        this.conceito10 = conceito10;
    }

    
    public String getConceito11() {
        return conceito11;
    }

    
    public void setConceito11(String conceito11) {
        this.conceito11 = conceito11;
    }

    
    public String getConceito12() {
        return conceito12;
    }

    
    public void setConceito12(String conceito12) {
        this.conceito12 = conceito12;
    }

    
    public String getConceito13() {
        return conceito13;
    }

    
    public void setConceito13(String conceito13) {
        this.conceito13 = conceito13;
    }

    
    public String getConceito14() {
        return conceito14;
    }

    
    public void setConceito14(String conceito14) {
        this.conceito14 = conceito14;
    }

    
    public String getConceito15() {
        return conceito15;
    }

    
    public void setConceito15(String conceito15) {
        this.conceito15 = conceito15;
    }

    
    public String getConceito16() {
        return conceito16;
    }

    
    public void setConceito16(String conceito16) {
        this.conceito16 = conceito16;
    }

    
    public String getConceito17() {
        return conceito17;
    }

    
    public void setConceito17(String conceito17) {
        this.conceito17 = conceito17;
    }

    
    public String getConceito18() {
        return conceito18;
    }

    
    public void setConceito18(String conceito18) {
        this.conceito18 = conceito18;
    }

    
    public String getConceito19() {
        return conceito19;
    }

    
    public void setConceito19(String conceito19) {
        this.conceito19 = conceito19;
    }

    
    public String getConceito20() {
        return conceito20;
    }

    
    public void setConceito20(String conceito20) {
        this.conceito20 = conceito20;
    }

    
    public String getConceito21() {
        return conceito21;
    }

    
    public void setConceito21(String conceito21) {
        this.conceito21 = conceito21;
    }

    
    public String getConceito22() {
        return conceito22;
    }

    
    public void setConceito22(String conceito22) {
        this.conceito22 = conceito22;
    }

    
    public String getConceito23() {
        return conceito23;
    }

    
    public void setConceito23(String conceito23) {
        this.conceito23 = conceito23;
    }

    
    public String getConceito24() {
        return conceito24;
    }

    
    public void setConceito24(String conceito24) {
        this.conceito24 = conceito24;
    }
    

    public Integer getTotalAlunos(){
    	return getMapaNotaAlunoPorTurmaAlunoRelVOs().size(); 
    }
    
    public Double getMediaGeral() {
    	Double nota = 0.0;
    	for (MapaNotaAlunoPorTurmaAlunoRelVO alunoPorTurmaAlunoRelVO : getMapaNotaAlunoPorTurmaAlunoRelVOs()) {
			nota = nota + alunoPorTurmaAlunoRelVO.getNotaDouble();
		}
    	if (getMapaNotaAlunoPorTurmaAlunoRelVOs().isEmpty()) {
    		return 0.0;
    	}
    	return Uteis.truncar(nota / getMapaNotaAlunoPorTurmaAlunoRelVOs().size(), 2);
    }

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getDisciplina() {
		if (disciplina == null) {
			disciplina = "";
		}
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getSala() {
		if (sala == null) {
			sala = "";
		}
		return sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getProfessor() {
		if (professor == null) {
			professor = "";
		}
		return professor;
	}

	public void setProfessor(String professor) {
		this.professor = professor;
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

	public List<HistoricoVO> getHistoricoVOs() {
		if (historicoVOs == null) {
			historicoVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoVOs;
	}

	public void setHistoricoVOs(List<HistoricoVO> historicoVOs) {
		this.historicoVOs = historicoVOs;
	}

	public String getNomeMatrizCurricular() {
		if (nomeMatrizCurricular == null) {
			nomeMatrizCurricular = "";
		}
		return nomeMatrizCurricular;
	}

	public void setNomeMatrizCurricular(String nomeMatrizCurricular) {
		this.nomeMatrizCurricular = nomeMatrizCurricular;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}
	
	public String getDataModulo() {
		if(dataModulo == null) {
			dataModulo = "";
		}
		return dataModulo;
	}

	public void setDataModulo(String dataModulo) {
		this.dataModulo = dataModulo;
	}
}
