package negocio.comuns.recursoshumanos.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum MotivoMudancaCargoEnum {

	PROGRESSAO("PROGRESSAO", "Progressão Salárial"),
	CONCURSO("CONCURSO", "Concurso"),
	SALARIO("SALARIO", "Salário"), 
	ADEQUACAO_TABELA_SALARIAL("ADEQUACAO_TABELA_SALARIAL", "Adequação Tabela Salárial"),
	MUDANCA_CARGO("MUDANCA_CARGO", "Mudança de Cargo"),
	NOVA_TITULACAO("NOVA_TITULACAO", "Nova Titulação"),
	MUDANCA_CARGA_HORARIA("MUDANCA_CARGA_HORARIA", "Mudança Carga Horária"),
	ADMISSAO("ADMISSAO", "Adimissão"),
	REAJUSTE_SALARIAL("REAJUSTE_SALARIAL", "Reajuste Salárial"),
	SALARIO_PROFESSOR("SALARIO_PROFESSOR", "Salário Professor"),
	NOMEACAO("NOMEACAO", "Nomeação"),
	DOENCA_PESSOA_FAMILIA("DOENCA_PESSOA_FAMILIA", "Doença Pessoa da Família"),
	DEMISSAO("DEMISSAO", "Demissão"),
	EXONERACAO_PEDIDO("EXONERACAO_PEDIDO", "Exoneração Pedido"),
	EXONERACAO("EXONERACAO", "Exoneração"),
	MOTIVO_DESCONHECIDO("MOTIVO_DESCONHECIDO", "Motivo Desconhecido"),
	AFASTAMENTO_ATIVIDADE_POLITICA("AFASTAMENTO_ATIVIDADE_POLITICA", "Afastamento Atividade Política"),
	EXERCICIO_MANDATO_ELETIVO("EXERCICIO_MANDATO_ELETIVO", "Exercício Mandato Eletivo"),
	LICENCA_MEDICA("LICENCA_MEDICA", "Licença Médica"),
	FERIAS("FERIAS", "Férias"),
	ATIVO("ATIVO", "Ativo"),
	VOLTA_LICENCA("VOLTA_LICENCA", "Volta Licença"),
	VACANCIA("VACANCIA", "Vacância"),
	CAPACITACAO("CAPACITACAO", "Capacitação"),
	INTERESSE_PARTICULAR("INTERESSE_PARTICULAR", "Interesse Particular"),
	LICENCA_PREMIO("LICENCA_PREMIO", "Licença Prêmio"),
	DISPOSICAO("DISPOSICAO", "Disposição"),
	LICENCA_MATERNIDADE("LICENCA_MATERNIDADE", "Licença Maternidade"),
	LICENCA_PATERNIDADE("LICENCA_PATERNIDADE", "Licença Paternidade"),
	LICENCA_INSS("LICENCA_INSS", "Licença INSS"),
	OUTROS("OUTROS", "Outros");

	public static List<SelectItem> getValorMotivoAfastamentoEnum() {
		return montarListaSelectItem(MotivoMudancaCargoEnum.values());
	}

	private static List<SelectItem> montarListaSelectItem(MotivoMudancaCargoEnum[] modulos) {
		List<SelectItem> tagList = new ArrayList<SelectItem>();
		tagList.add(new SelectItem("", ""));
		for (MotivoMudancaCargoEnum tag : modulos) {
			tagList.add(new SelectItem(tag, tag.getValorApresentar()));
		}
		return tagList;
	}

	String valor;
	String descricao;

	private MotivoMudancaCargoEnum(String valor, String descricao) {
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
		return UteisJSF.internacionalizar("enum_MotivoAfastamentoEnum_" + this.name());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}