/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.interfaces.biblioteca;

import java.util.List;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import relatorio.negocio.comuns.biblioteca.TicketRelVO;

/**
 *
 * @author Alessandro
 */
public interface TicketRelInterface {

    String caminhoBaseRelatorio();

    String designerTicketEmprestimo();

    public List<TicketRelVO> criarObjeto(EmprestimoVO emprestimoVO,  List<ItemEmprestimoVO> listaItensEmprestimo, String textoPadraoEmprestimo, String textoPadraoDevolucao, String textoPadraoRenovacao, String matriculaAluno, String matriculaFuncionario) throws Exception;

}
