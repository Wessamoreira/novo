package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Wellington - 25 de set de 2015
 */
public enum OrigemOperacaoFuncionalidadeEnum {

	MATRICULA_PERIODO, 
	MATRICULA,
	PARCEIRO,
	CODIGO_ALUNO,
	ALUNO,
	PROSPECT,
	MAPA_PENDENCIA_MOVIMENTACAO_FINACEIRA,
	REQUERIMENTO,
	REQUISICAO,
	TRANSFERENCIA_SAIDA,
	ITEM_PROCESSO_SELETIVO_DATA_PROVA,
	REGISTRO_ATIVIDADE_COMPLEMENTAR_MATRICULA,
	REATIVACAO_MATRICULA,
	CONTA_RECEBER,
	NEGOCIACAO_CONTA_RECEBER,
	PLANO_DESCONTO_CONTA_RECEBER,
	PROGRAMACAO_AULA,
	TURMA,
	MOVIMENTACAO_FINANCEIRA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_OrigemOperacaoFuncionalidadeEnum_" + this.name());
	}

}
