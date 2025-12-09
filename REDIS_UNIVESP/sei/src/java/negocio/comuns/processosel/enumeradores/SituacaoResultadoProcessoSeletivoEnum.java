/**
 * 
 */
package negocio.comuns.processosel.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum SituacaoResultadoProcessoSeletivoEnum {
	APROVADO,
	APROVADO_1_OPCAO,
	APROVADO_2_OPCAO,
	APROVADO_3_OPCAO,
	REPROVADO,
	TODOS; 
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoResultadoProcessoSeletivoEnum_" + this.name());
	}

	public static List<SelectItem> getSituacaoResultadoProcessoSeletivo() {
		return getListaSelectItemSituacaoResultadoProcessoSeletivo();
	}
	
	private static List<SelectItem> getListaSelectItemSituacaoResultadoProcessoSeletivo() {
		List<SelectItem> listaSituacaoResultado = new ArrayList<SelectItem>();
		for (SituacaoResultadoProcessoSeletivoEnum situacao : SituacaoResultadoProcessoSeletivoEnum.values()) {
			listaSituacaoResultado.add(new SelectItem(situacao, situacao.getValorApresentar()));
		}
		return listaSituacaoResultado;
	}

}
