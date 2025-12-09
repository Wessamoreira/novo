package negocio.comuns.utilitarias.dominios;

import java.util.List;
import static java.util.stream.Stream.of;

import static java.util.stream.Collectors.toList;

import javax.faces.model.SelectItem;

public enum JustificativaCensoEnum {
	NENHUMA("", ""),
	JUSTIFICATIVA_1("O aluno reingressou no mesmo curso.", "01"),
	JUSTIFICATIVA_2("O aluno reingressou no mesmo curso e o semestre de ingresso foi informado incorretamente no Censo anterior.", "02"),
	JUSTIFICATIVA_3("Situação do vínculo do aluno informada errada no Censo anterior.", "03"),
	JUSTIFICATIVA_4("Situação do vínculo e semestre de ingresso informados incorretamente no Censo anterior.", "04"),
	JUSTIFICATIVA_5("Curso ou local de oferta não carregado para o Censo anterior.", "05"),
	JUSTIFICATIVA_6("Curso em que o aluno estava vinculado não existe no Censo atual.", "06"),
	JUSTIFICATIVA_7("A IES não vinculou o aluno no Censo anterior.", "07"),
	JUSTIFICATIVA_8("Semestre de ingresso informado errado no Censo anterior.", "08"),
	JUSTIFICATIVA_9("Situação de vínculo informada errada, aluno formado no curso BI/LI correspondente no Censo Anterior.", "09"),
	JUSTIFICATIVA_10("Aluno não informado no curso BI/LI correspondente no Censo Anterior.", "10"),
	JUSTIFICATIVA_11("Aluno concluiu o curso BI/LI correspondente até o ano de <ano censo atual -2>.", "11"),
	JUSTIFICATIVA_12("O aluno reingressou no mesmo curso. Situação de vínculo e semestre de ingresso informados errados no Censo anterior.", "12");

	private final String descricao;
	private final String valor;

	private JustificativaCensoEnum(String descricao, String valor) {
		this.descricao = descricao;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getValor() {
		return valor;
	}

	public static List<SelectItem> getListaSelectItemJustificativaCensoEnum() {
		return of(values()).map(JustificativaCensoEnum::getJustificativaCensoEnumParaSelectItem).collect(toList());
	}

	private static SelectItem getJustificativaCensoEnumParaSelectItem(JustificativaCensoEnum justificativaCensoEnum) {
		return new SelectItem(justificativaCensoEnum, justificativaCensoEnum.getDescricao());
	}
	
	public boolean isNenhuma() {
		return equals(NENHUMA);
	}
}
