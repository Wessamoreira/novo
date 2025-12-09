package controle.financeiro;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Euripedes Doutor
 */
public class ImpressaoMatricialSV extends HttpServlet {

    protected List lista;

    @SuppressWarnings("SizeReplaceableByIsEmpty")
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (getLista().size() != 0) {
                ObjectOutputStream output = new ObjectOutputStream(response.getOutputStream());
                output.writeUnshared(getLista());
                output.flush();
                setLista(new ArrayList(0));
            }else{
                ObjectOutputStream output = new ObjectOutputStream(response.getOutputStream());
                output.writeUnshared(new ArrayList(0));
                output.flush();
            }
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            setLista((List) request.getAttribute("lista"));
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }
}


