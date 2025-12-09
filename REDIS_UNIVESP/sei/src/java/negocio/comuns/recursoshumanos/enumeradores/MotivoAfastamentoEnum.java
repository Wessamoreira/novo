package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum MotivoAfastamentoEnum {

	DOENCA_PESSOA_FAMILIA("DOENCA_PESSOA_FAMILIA", "Doença Pesso da Familía"),
	MOTIVO_DESCONHECIDO("MOTIVO_DESCONHECIDO", "Motivo Desconhecido"), 
	CAPACITACAO("CAPACITACAO", "Capacitação"),
	INTERESSE_PARTICULAR("INTERESSE_PARTICULAR", "Interesse Particular"),
	LICENCA_PATERNIDADE("LICENCA_PATERNIDADE", "Licença Paternidade"),
	LICENCA_PREMIO("LICENCA_PREMIO", "Licença  Prêmio"),
	AFASTAMENTO_ATIVIDADE_POLITICA("AFASTAMENTO_ATIVIDADE_POLITICA", "Afastamento Atividade Política"),
	EXERCICIO_MANDATO_ELETIVO("EXERCICIO_MANDATO_ELETIVO", "Exercício Mandato Eletivo"),
	LICENCA_MEDICA("LICENCA_MEDICA", "Licença Médica"),
	DISPOSICAO("DISPOSICAO", "Disposição"),
	LICENCA_MATERNIDADE("LICENCA_MATERNIDADE", "Licença Maternidade"),
	LICENCA_MATERNIDADE_PRORROGACAO("LICENCA_MATERNIDADE_PRORROGACAO", "Licença Maternidade - Prorrogação"),
	ATIVO("ATIVO", "Ativo"),
	LICENCA_INSS("LICENCA_INSS", "Licença INSS"),
	VACANCIA("VACANCIA", "Vacância"),
	VOLTA_LICENCA("VOLTA_LICENCA", "Volta Licença"),
	OUTROS("OUTROS", "Outros");

	public static List<SelectItem> getValorMotivoAfastamentoEnum() {
		return montarListaSelectItem(MotivoAfastamentoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(MotivoAfastamentoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (MotivoAfastamentoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private MotivoAfastamentoEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_MotivoAfastamentoEnum_" + this.name());
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_MotivoAfastamentoEnum_" + this.valor);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}