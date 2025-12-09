package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class QuadroMatriculaCursoRelVO implements Cloneable {

    private Integer codigoCurso;
    private String nomeCurso;
    private List<QuadroMatriculaTurmaRelVO> listaQuadroMatriculaTurmaRelVO;
    private List<Integer> listaTotalDia;

    public QuadroMatriculaCursoRelVO() {
        //setListaCursoVO(new ArrayList<CursoVO>(0));
    }

    public JRDataSource getListaQuadroMatriculaTurmaRelVOs(){
        return new JRBeanArrayDataSource(getListaQuadroMatriculaTurmaRelVO().toArray());
    }

    public JRDataSource getListaTotalDias(){
        return new JRBeanArrayDataSource(getListaTotalDia().toArray());
    }
    /**
     * @return the nomeCurso
     */
    public String getNomeCurso() {
        return nomeCurso;
    }

    /**
     * @param nomeCurso the nomeCurso to set
     */
    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    /**
     * @return the listaQuadroMatriculaTurmaRelVO
     */
    public List<QuadroMatriculaTurmaRelVO> getListaQuadroMatriculaTurmaRelVO() {
        if (listaQuadroMatriculaTurmaRelVO == null) {
            listaQuadroMatriculaTurmaRelVO = new ArrayList<QuadroMatriculaTurmaRelVO>(0);
        }
        return listaQuadroMatriculaTurmaRelVO;
    }

    /**
     * @param listaQuadroMatriculaTurmaRelVO the listaQuadroMatriculaTurmaRelVO to set
     */
    public void setListaQuadroMatriculaTurmaRelVO(List<QuadroMatriculaTurmaRelVO> listaQuadroMatriculaTurmaRelVO) {
        this.listaQuadroMatriculaTurmaRelVO = listaQuadroMatriculaTurmaRelVO;
    }

    /**
     * @return the codigoCurso
     */
    public Integer getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = 0;
        }
        return codigoCurso;
    }

    /**
     * @param codigoCurso the codigoCurso to set
     */
    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    /**
     * @return the listaTotalDia
     */
    public List<Integer> getListaTotalDia() {
        if(listaTotalDia == null){
            listaTotalDia = new ArrayList<Integer>();
        }
        return listaTotalDia;
    }

    /**
     * @param listaTotalDia the listaTotalDia to set
     */
    public void setListaTotalDia(List<Integer> listaTotalDia) {
        this.listaTotalDia = listaTotalDia;
    }
//    public JRDataSource getHistoricoVO() {
//        JRDataSource jr = new JRBeanArrayDataSource(getHistorico().toArray());
//        return jr;
//    }
//
//    public JRDataSource getHistoricoAuxVO() {
//        JRDataSource jr = new JRBeanArrayDataSource(getHistoricoAux().toArray());
//        return jr;
//    }
}
