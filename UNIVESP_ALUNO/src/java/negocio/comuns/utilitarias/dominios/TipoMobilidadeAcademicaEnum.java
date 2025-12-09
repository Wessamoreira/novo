package negocio.comuns.utilitarias.dominios;

import java.util.List;
import static java.util.stream.Stream.of;

import static java.util.stream.Collectors.toList;

import jakarta.faces. model.SelectItem;

public enum TipoMobilidadeAcademicaEnum {
	NENHUMA("", ""), 
	VAZIO("Vazio", ""),
	NACIONAL("Nacional", "1"), 
	INTERNACIONAL("Internacional", "2");

	private final String descricao;
	private final String valor;

	private TipoMobilidadeAcademicaEnum(String descricao, String valor) {
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public static List<SelectItem> getListaSelectItemTipoMobilidadeAcademicaEnum() {
		return of(values()).map(TipoMobilidadeAcademicaEnum::getTipoMobilidadeAcademicaEnumParaSelectItem).collect(toList());
	}

	private static SelectItem getTipoMobilidadeAcademicaEnumParaSelectItem(TipoMobilidadeAcademicaEnum tipoMobilidadeAcademicaEnum) {
		return new SelectItem(tipoMobilidadeAcademicaEnum, tipoMobilidadeAcademicaEnum.getDescricao());
	}

	public boolean isNenhuma() {
		return equals(NENHUMA);
	}

	public boolean isNacional() {
		return equals(NACIONAL);
	}

	public boolean isInternacional() {
		return equals(INTERNACIONAL);
	}
	
	public boolean isVazio() {
		return equals(VAZIO);
	}
}
