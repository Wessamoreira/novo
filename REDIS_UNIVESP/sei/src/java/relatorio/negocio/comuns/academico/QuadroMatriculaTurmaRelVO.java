package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class QuadroMatriculaTurmaRelVO implements Cloneable {

    private Integer codigoTurma;
    private String identificadorTurma;
    private List<QuadroMatriculaDataQtdeMatriculasVO> listaQuadroMatriculaDataQtdeMatriculasVO;

    public QuadroMatriculaTurmaRelVO() {
        //setListaCursoVO(new ArrayList<CursoVO>(0));
    }

     public JRDataSource getListaQuadroMatriculaDataQtdeMatriculasVOs(){
        return new JRBeanArrayDataSource(getListaQuadroMatriculaDataQtdeMatriculasVO().toArray());
    }

    /**
     * @return the turma
     */
    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    /**
     * @param turma the turma to set
     */
    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }  

    /**
     * @return the codigoTurma
     */
    public Integer getCodigoTurma() {
        if (codigoTurma == null) {
            codigoTurma = 0;
        }
        return codigoTurma;
    }

    /**
     * @param codigoTurma the codigoTurma to set
     */
    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    /**
     * @return the listaQuadroMatriculaDataQtdeMatriculasVO
     */
    public List<QuadroMatriculaDataQtdeMatriculasVO> getListaQuadroMatriculaDataQtdeMatriculasVO() {
        if(listaQuadroMatriculaDataQtdeMatriculasVO==null){
            listaQuadroMatriculaDataQtdeMatriculasVO = new ArrayList<QuadroMatriculaDataQtdeMatriculasVO>(0);
        }
        return listaQuadroMatriculaDataQtdeMatriculasVO;
    }

    /**
     * @param listaQuadroMatriculaDataQtdeMatriculasVO the listaQuadroMatriculaDataQtdeMatriculasVO to set
     */
    public void setListaQuadroMatriculaDataQtdeMatriculasVO(List<QuadroMatriculaDataQtdeMatriculasVO> listaQuadroMatriculaDataQtdeMatriculasVO) {
        this.listaQuadroMatriculaDataQtdeMatriculasVO = listaQuadroMatriculaDataQtdeMatriculasVO;
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
