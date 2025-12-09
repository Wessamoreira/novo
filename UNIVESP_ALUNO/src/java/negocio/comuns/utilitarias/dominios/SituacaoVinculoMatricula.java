package negocio.comuns.utilitarias.dominios;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

/**
 * De acordo com o censo:
 * 
  	1 – Provável formando
	2 – Cursando
	3 – Matrícula trancada
	4 – Desvinculado do curso
	5  Transferido para outro curso da mesma IES
	6 – Formado
	7 – Falecido
	8  Possivel aluno
 *
 * 0 - Controle do sei.
 *  Utilizado para filtrar as matriculas que não podem ser incluidas no censo.
 *  @author Diego
 */
public enum SituacaoVinculoMatricula {
    ABANDONO_CURSO("AC", "Abandono de Curso",3 ),
    PREMATRICULA("PR", "Pré-matrícula",2 ),
    DESLIGADO("DE", "Desligado", 4),
    INATIVA("IN", "Inativa",4 ),
    ATIVA("AT", "Ativa",2 ),
    CANCELADA("CA", "Cancelada",4 ),
    CANCELADA_FINANCEIRO("CF", "Cancelada Financeiro", 3),
    JUBILADO("JU", "Jubilado",3 ),
    TRANSFERIDA("TS", "Transferida",4 ),
    TRANCADA("TR", "Trancada",3 ),
    FINALIZADA("FI", "Finalizada",2),
    PROVAVEL_FORMANDO("PF", "Provável Formando",2 ),
    FORMADO("FO", "Formado",6),
    TRANSFERENCIA_INTERNA("TI", "Transferida Internamente", 5),
    INDEFINIDA("ER", "Problema ao Tentar Definir Situação - Problema Importação",0);
    
    String valor;
    String descricao;
    int codigoCenso;

    SituacaoVinculoMatricula(String valor, String descricao, int codigoCenso) {
        this.valor = valor;
        this.descricao = descricao;
        this.codigoCenso = codigoCenso;
    }

    public static SituacaoVinculoMatricula getEnum(String valor) {
    	SituacaoVinculoMatricula[] valores = values();
        for (SituacaoVinculoMatricula obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	SituacaoVinculoMatricula obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public static List<SelectItem> getListaSelectItemSituacaoVinculoMatricula(Boolean vazio) {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	if (vazio) {
    		itens.add(new SelectItem("", ""));
    	}
    	for (SituacaoVinculoMatricula situacaoVinculoMatricula : SituacaoVinculoMatricula.values()) {
    		itens.add(new SelectItem(situacaoVinculoMatricula, situacaoVinculoMatricula.getDescricao()));
    	}
    	return itens;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

	public int getCodigoCenso() {
		return codigoCenso;
	}

	public void setCodigoCenso(int codigoCenso) {
		this.codigoCenso = codigoCenso;
	}
}
