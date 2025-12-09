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

public class PainelGestorMonitoramentoConsultorVO implements Serializable {

    private Integer codConsultor;
    private String consultor;
    private Integer codTurma;
    private String turma;
    private Integer qtdMatRecebida;
    private Integer qtdMatAReceber;
    private Integer qtdMatVencida;
    private Integer qtdMatAVencer;
    private Integer qtdMatPre;
    private Integer qtdMatAtivo;
    private Integer qtdMatCancelado;
    private Integer qtdMatExcluido;    
    private Integer qtdMatExtensao;
    private Integer qtdMatPendenciaDoc;    
    public static final long serialVersionUID = 1L;

	public Integer getCodConsultor() {
		if(codConsultor == null){
			codConsultor = 0;
		}		
		return codConsultor;
	}

	public void setCodConsultor(Integer codConsultor) {
		this.codConsultor = codConsultor;
	}

	public String getConsultor() {
		if(consultor == null){
			consultor = "";
		}		
		return consultor;
	}
	
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
        String[] nomes = getConsultor().split(" ");
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

	public void setConsultor(String consultor) {
		this.consultor = consultor;
	}

	public Integer getCodTurma() {
		if(codTurma == null){
			codTurma = 0;
		}		
		return codTurma;
	}

	public void setCodTurma(Integer codTurma) {
		this.codTurma = codTurma;
	}

	public String getTurma() {
		if(turma == null){
			turma = "";
		}		
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public Integer getQtdMatRecebida() {
		if(qtdMatRecebida == null){
			qtdMatRecebida = 0;
		}		
		return qtdMatRecebida;
	}

	public void setQtdMatRecebida(Integer qtdMatRecebida) {
		this.qtdMatRecebida = qtdMatRecebida;
	}

	public Integer getQtdMatAReceber() {
		if(qtdMatAReceber == null){
			qtdMatAReceber = 0;
		}		
		return qtdMatAReceber;
	}

	public void setQtdMatAReceber(Integer qtdMatAReceber) {
		this.qtdMatAReceber = qtdMatAReceber;
	}

	public Integer getQtdMatVencida() {
		if(qtdMatVencida == null){
			qtdMatVencida = 0;
		}		
		return qtdMatVencida;
	}

	public void setQtdMatVencida(Integer qtdMatVencida) {
		this.qtdMatVencida = qtdMatVencida;
	}

	public Integer getQtdMatAVencer() {
		if(qtdMatAVencer == null){
			qtdMatAVencer = 0;
		}		
		return qtdMatAVencer;
	}

	public void setQtdMatAVencer(Integer qtdMatAVencer) {
		this.qtdMatAVencer = qtdMatAVencer;
	}

	public Integer getQtdMatPre() {
		if(qtdMatPre == null){
			qtdMatPre = 0;
		}		
		return qtdMatPre;
	}

	public void setQtdMatPre(Integer qtdMatPre) {
		this.qtdMatPre = qtdMatPre;
	}

	public Integer getQtdMatAtivo() {
		if(qtdMatAtivo == null){
			qtdMatAtivo = 0;
		}		
		return qtdMatAtivo;
	}

	public void setQtdMatAtivo(Integer qtdMatAtivo) {
		this.qtdMatAtivo = qtdMatAtivo;
	}

	public Integer getQtdMatCancelado() {
		if(qtdMatCancelado == null){
			qtdMatCancelado = 0;
		}		
		return qtdMatCancelado;
	}

	public void setQtdMatCancelado(Integer qtdMatCancelado) {
		this.qtdMatCancelado = qtdMatCancelado;
	}

	public Integer getQtdMatExcluido() {
		if(qtdMatExcluido == null){
			qtdMatExcluido = 0;
		}		
		return qtdMatExcluido;
	}

	public void setQtdMatExcluido(Integer qtdMatExcluido) {
		this.qtdMatExcluido = qtdMatExcluido;
	}

	public Integer getQtdMatExtensao() {
		if(qtdMatExtensao == null){
			qtdMatExtensao = 0;
		}		
		return qtdMatExtensao;
	}

	public void setQtdMatExtensao(Integer qtdMatExtensao) {
		this.qtdMatExtensao = qtdMatExtensao;
	}

	public Integer getQtdMatPendenciaDoc() {
		if(qtdMatPendenciaDoc == null){
			qtdMatPendenciaDoc = 0;
		}		
		return qtdMatPendenciaDoc;
	}

	public void setQtdMatPendenciaDoc(Integer qtdMatPendenciaDoc) {
		this.qtdMatPendenciaDoc = qtdMatPendenciaDoc;
	}

}
