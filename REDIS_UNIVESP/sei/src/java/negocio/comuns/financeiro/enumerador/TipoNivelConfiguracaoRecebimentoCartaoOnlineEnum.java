package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Victor Hugo de Paula Costa
 * 
 *	Nível Configuração:
        Geral - será válido para todas as unidades de ensino
        Unidade Ensino - permite uma configuração especifica para uma determinada unidade de ensino
        Nível Educacional - poderá informar ou não uma unidade de ensino, onde se informado valerá para a unidade e nivel educacional 
        combinada, se não valerá para todas as unidades que possuem curso no nível educacional informado.
        Curso - irá vincular a um curso específico, podendo ou não vincular a uma unidade de ensino, ou seja, se informado valerá para o 
        curso e unidade informado se não para todas as unidade que possui o curso.
        Turma -  irá vincular uma turma específica permitindo assim uma determinada turma ter uma regra especifica.
 *
 *
 */
public enum TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum {

	GERAL, UNIDADE_ENSINO, NIVEL_EDUCACIONAL, CURSO, TURMA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoNivelConfiguracaoRecebimentoCartaoOnlineEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
}
