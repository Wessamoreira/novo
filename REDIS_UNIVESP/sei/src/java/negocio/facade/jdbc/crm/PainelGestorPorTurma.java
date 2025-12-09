/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.facade.jdbc.crm;

import java.util.List;

/**
 *
 * @author Philippe
 */
public class PainelGestorPorTurma {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public PainelGestorPorTurma() throws Exception {
        super();
        setIdEntidade("PainelGestorPorTurma");
    }

    public List<PainelGestorPorTurma> consultarTurmasCursoInteracao() {
        StringBuilder sqlsb = new StringBuilder();
        return null;
    }

}
