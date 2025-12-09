/**
 * 
 */
package negocio.comuns.crm.enumerador;


/**
 * @author Carlos Eugênio
 *
 */
public enum PoliticaRedistribuicaoProspectAgendaEnum {
	TODOS("TODOS"),
	PROSPECT_SEM_AGENDA("PROSPECT SEM AGENDA"),
	PROSPECT_COM_AGENDA_NAO_REALIZADA("PROSPECT COM AGENDA NÃO REALIZADA");
	
	String descricao;
	
	PoliticaRedistribuicaoProspectAgendaEnum(String descricao) {
		this.descricao = descricao;
	}
	
	public static PoliticaRedistribuicaoProspectAgendaEnum getEnum(String valor) {
		PoliticaRedistribuicaoProspectAgendaEnum[] valores = values();
        for (PoliticaRedistribuicaoProspectAgendaEnum obj : valores) {
            if (obj.getDescricao().equals(valor)) {
                return obj;
            }
        }
        return null;
    }
	
	public static String getDescricao(String valor) {
		PoliticaRedistribuicaoProspectAgendaEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
