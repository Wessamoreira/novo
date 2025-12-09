package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.faces. model.SelectItem;

/**
 * @author Felipi Alves
 */
public enum TipoAlunoEnum {
	AMBOS("AMBOS", "Ambos"), CALOURO("CALOURO", "Calouro"), VETERANO("VETERANO", "Veterano");

	TipoAlunoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	private final String valor;
	private final String descricao;

	public String getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static List<SelectItem> getListaSelectItemTipoAlunoEnum() {
		return new ArrayList<>(Arrays.asList(values())).stream().map(tipoAluno -> new SelectItem(tipoAluno, tipoAluno.getDescricao())).collect(Collectors.toList());
	}
}