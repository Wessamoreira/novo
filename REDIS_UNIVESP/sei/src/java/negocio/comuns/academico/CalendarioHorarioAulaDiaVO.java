/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Otimize-Not
 */
public class CalendarioHorarioAulaDiaVO implements Serializable {

    private Date dia;
    private Integer numeroAulaDefinidas;
    private Integer numeroAulaDisponivel;
    public static final long serialVersionUID = 1L;
}
