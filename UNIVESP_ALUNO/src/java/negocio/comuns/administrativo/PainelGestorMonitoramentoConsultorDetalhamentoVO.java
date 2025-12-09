/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Otimize-Not
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PainelGestorMonitoramentoConsultorDetalhamentoVO implements Serializable {

    private String aluno;
    private String matricula;
    private List listaDocumentos;
    public static final long serialVersionUID = 1L;

	public String getNomeResumido() {
        List<String> preposicoes2 = new ArrayList(0);
        preposicoes2.add("dos");
        preposicoes2.add("das");
        preposicoes2.add("de");
        preposicoes2.add("da");
        preposicoes2.add("e");
        preposicoes2.add("a");
        preposicoes2.add("i");
        preposicoes2.add("o");
        preposicoes2.add("DOS");
        preposicoes2.add("DAS");
        preposicoes2.add("DE");
        preposicoes2.add("DA");
        preposicoes2.add("E");
        preposicoes2.add("A");
        preposicoes2.add("I");
        preposicoes2.add("O");
        String[] nomes = getAluno().split(" ");
        String nomeResumido = "";
        Integer indice = 1;
        nomeResumido += nomes[0] + " ";
        while (indice < nomes.length - 1) {
            if (preposicoes2.contains(nomes[indice])) {
                nomes[indice] = "";
            } else if (nomes[indice].length() > 2) {
                nomes[indice] = nomes[indice].substring(0, 1).concat(".");
            }
            nomeResumido += nomes[indice] + " ";
            indice = indice + 1;
        }
        preposicoes2 = null;
        nomeResumido += nomes[nomes.length - 1] + " ";
        return nomeResumido;
    }

    public String getNomeResumidoNomeSobrenome() {
        String[] nomes = getNomeResumido().split(" ");
        String nomeResumido = "";
        for (int i = 0; i < nomes.length; i++) {
            if (i == 0) {
                nomeResumido = nomes[i];
            } else if (i == 2) {
                nomeResumido += " " + nomes[i];
            }
        }
        return nomeResumido;
    }

	public String getAluno() {
		if (aluno == null) {
			aluno = "";
		}
		return aluno;
	}

	public void setAluno(String aluno) {
		this.aluno = aluno;
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

	public List getListaDocumentos() {
		if (listaDocumentos == null) {
			listaDocumentos = new ArrayList();
		}
		return listaDocumentos;
	}

	public void setListaDocumentos(List listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}	
}
