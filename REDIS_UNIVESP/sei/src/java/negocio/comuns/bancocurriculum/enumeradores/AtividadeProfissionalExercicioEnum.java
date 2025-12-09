/**
 * 
 */
package negocio.comuns.bancocurriculum.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Carlos Eugênio
 *
 */
public enum AtividadeProfissionalExercicioEnum {

		EMPREGADO_SELETISTA,
		ESTATUTARIO, 
		SOCIO_OU_PROPRIETARIO_EMPRESARIO_INDIVIDUAL,
		TRABALHO_AUTONOMO;
		
		public String getValorApresentar() {
			return UteisJSF.internacionalizar("enum_AtividadeProfissionalExercicioEnum_" + this.name());
		}

		public static List<SelectItem> getListaSelectItemSituacaoResultadoProcessoSeletivo() {
			List<SelectItem> listaSituacaoResultado = new ArrayList<SelectItem>();
			listaSituacaoResultado.add(new SelectItem("", ""));
			for (AtividadeProfissionalExercicioEnum atividade : AtividadeProfissionalExercicioEnum.values()) {
				listaSituacaoResultado.add(new SelectItem(atividade, atividade.getValorApresentar()));
			}
			return listaSituacaoResultado;
		}
}
