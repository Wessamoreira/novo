package relatorio.negocio.comuns.academico;

import java.util.Date;

public class QuadroMatriculaDataQtdeMatriculasVO implements Cloneable {

    private Date data;
    private Integer qtdeMatriculas;

    public QuadroMatriculaDataQtdeMatriculasVO() {
        
    }

    /**
     * @return the qtde
     */
    public Integer getQtdeMatriculas() {
        if (qtdeMatriculas == null) {
            qtdeMatriculas = 0;
        }
        return qtdeMatriculas;
    }

    /**
     * @param qtde the qtde to set
     */
    public void setQtdeMatriculas(Integer qtdeMatriculas) {
        this.qtdeMatriculas = qtdeMatriculas;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }
}
