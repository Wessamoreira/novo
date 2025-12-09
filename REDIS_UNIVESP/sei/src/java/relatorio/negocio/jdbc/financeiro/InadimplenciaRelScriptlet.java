/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.financeiro;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

/**
 *
 * @author Alberto
 */
public class InadimplenciaRelScriptlet extends JRDefaultScriptlet {
    
    public static Map<String, String> hashMapParceiro;
    public static Map<String, String> hashMapMatriculaAluno;
    public static Map<String, String> hashMapNossoNumero;
    public int qtdeParceiros;
    public int qtdeAlunos;
    public int qtdeContasAlunos;
    public int qtdeContasParceiros;
    public static Double somaGeralAlunos;
    public static Double somaGeralParceiros;


    public void calcularQuantidadeParceirosAlunos() throws JRScriptletException {
        String parceiro = (String) this.getFieldValue("parceiroNome");
        String matricula = (String) this.getFieldValue("matricula");
        if (!hashMapParceiro.containsKey(parceiro) && !parceiro.equals("")) {
            qtdeParceiros ++;
        }        
        if (!hashMapMatriculaAluno.containsKey(matricula) && parceiro.equals("") && !matricula.equals("")) {
            qtdeAlunos ++;
        }
        hashMapParceiro.put(parceiro,parceiro);
        hashMapMatriculaAluno.put(matricula,matricula);
        this.setVariableValue("quantidadeParceiros", qtdeParceiros);
        this.setVariableValue("quantidadeAlunos", qtdeAlunos);
    }

    public void calcularSomaGeralParceirosAlunos() throws JRScriptletException {
        String parceiro = (String) this.getFieldValue("parceiroNome");
        String nossoNumero = (String) this.getFieldValue("nossoNumero");
        if (!hashMapNossoNumero.containsKey(nossoNumero) && parceiro.equals("")) {
            qtdeContasAlunos++;
            somaGeralAlunos += (Double)this.getFieldValue("total");
        }        
        if (!hashMapNossoNumero.containsKey(nossoNumero) && !parceiro.equals("")) {
            qtdeContasParceiros++;
            somaGeralParceiros += (Double)this.getFieldValue("total");
        }
        this.setVariableValue("somaGeralAlunos", somaGeralAlunos);
        this.setVariableValue("somaGeralParceiros", somaGeralParceiros);
        this.setVariableValue("qtdeContasAlunos", qtdeContasAlunos);
        this.setVariableValue("qtdeContasParceiros", qtdeContasParceiros);
        hashMapNossoNumero.put(nossoNumero, nossoNumero);
    }

    @Override
    public void afterGroupInit(String groupName) throws JRScriptletException {
        if(groupName.equals("AGRUPAMENTO_ALUNO")){
            calcularQuantidadeParceirosAlunos();
        }
        calcularSomaGeralParceirosAlunos();
    }

    @Override
    public void beforeReportInit() throws JRScriptletException {
        hashMapParceiro = new HashMap<String, String>();
        qtdeParceiros = 0;
        hashMapMatriculaAluno = new HashMap<String, String>();
        qtdeAlunos = 0;
        hashMapNossoNumero = new HashMap<String, String>();
        somaGeralAlunos = 0.0;
        somaGeralParceiros = 0.0;
        qtdeContasAlunos = 0;
        qtdeContasParceiros = 0;
    }
}
