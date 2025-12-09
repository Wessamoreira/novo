package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.basico.PessoaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class HorarioDaTurmaPrincipalRelVO {

    private String turma;
    private Integer curso;
    private String observacao;
    private List<HorarioDaTurmaRelVO> listaHorarioDaTurmaRelVO;
    private TitulacaoCursoVO titulacaoCursoVO;
    private HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade;
    private HashMap<Integer, String> hashMapProfessoresNaoDuplicados;
    private List<ItemTitulacaoCursoVO> listaQtdeTitulacao;

    public JRDataSource getListaHorarioDaTurmaRelVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaHorarioDaTurmaRelVO().toArray());
        return jr;
    }

    public JRDataSource getListaItemTitulacaoCursoVOs() {
        JRDataSource jr = new JRBeanArrayDataSource(getTitulacaoCursoVO().getItemTitulacaoCursoVOs().toArray());
        return jr;
    }

    public String getListaQtdeTotalTitulacao() {
    	StringBuilder texto = new StringBuilder("");
    	String espaco = "";
        for(ItemTitulacaoCursoVO obj: getListaQtdeTitulacao()){
        	texto.append(espaco).append(obj.getTitulacao()).append(" ").append(obj.getQuantidade());
        	espaco = "      ";
        }
        return texto.toString();
    }

    public boolean getExisteTitulacaoCurso() {
        if (!getTitulacaoCursoVO().getCodigo().equals(0)) {
            return true;
        }
        return false;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public Integer getCurso() {
        if (curso == null) {
            curso = 0;
        }
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<HorarioDaTurmaRelVO> getListaHorarioDaTurmaRelVO() {
        if (listaHorarioDaTurmaRelVO == null) {
            listaHorarioDaTurmaRelVO = new ArrayList<HorarioDaTurmaRelVO>(0);
        }
        return listaHorarioDaTurmaRelVO;
    }

    public void setListaHorarioDaTurmaRelVO(List<HorarioDaTurmaRelVO> listaHorarioDaTurmaRelVO) {
        this.listaHorarioDaTurmaRelVO = listaHorarioDaTurmaRelVO;
    }

    public TitulacaoCursoVO getTitulacaoCursoVO() {
        if (titulacaoCursoVO == null) {
            titulacaoCursoVO = new TitulacaoCursoVO();
        }
        return titulacaoCursoVO;
    }

    public void setTitulacaoCursoVO(TitulacaoCursoVO titulacaoCursoVO) {
        this.titulacaoCursoVO = titulacaoCursoVO;
    }

    public HashMap<Integer, Integer> getHashMapQtdeNivelEscolaridade() {
        if (hashMapQtdeNivelEscolaridade == null) {
            hashMapQtdeNivelEscolaridade = new HashMap<Integer, Integer>(0);
        }
        return hashMapQtdeNivelEscolaridade;
    }

    public void setHashMapQtdeNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade) {
        this.hashMapQtdeNivelEscolaridade = hashMapQtdeNivelEscolaridade;
    }

    public HashMap<Integer, String> getHashMapProfessoresNaoDuplicados() {
        if (hashMapProfessoresNaoDuplicados == null) {
            hashMapProfessoresNaoDuplicados = new HashMap<Integer, String>(0);
        }
        return hashMapProfessoresNaoDuplicados;
    }

    public void setHashMapProfessoresNaoDuplicados(HashMap<Integer, String> hashMapProfessoresNaoDuplicados) {
        this.hashMapProfessoresNaoDuplicados = hashMapProfessoresNaoDuplicados;
    }

    public List<ItemTitulacaoCursoVO> getListaQtdeTitulacao() {
        if (listaQtdeTitulacao == null) {
            listaQtdeTitulacao = new ArrayList<ItemTitulacaoCursoVO>(0);
        }
        return listaQtdeTitulacao;
    }

    public void setListaQtdeTitulacao(List<ItemTitulacaoCursoVO> listaQtdeTitulacao) {
        this.listaQtdeTitulacao = listaQtdeTitulacao;
    }
}
