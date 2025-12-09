/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.crm.enumerador;

import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.utilitarias.UteisJSF;

/**
 *
 * @author Rogerio
 */
public enum TipoSituacaoWorkflowEnum {
    EM_CONSTRUCAO("EM_CONSTRUCAO", UteisJSF.internacionalizar("msg_WorkFlow_emConstrucao")), ATIVO("ATIVO", "Ativo"), INATIVO("INATIVO", "Inativo");

	private String valor;
	private String descricao;

	private TipoSituacaoWorkflowEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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

	public static TipoSituacaoWorkflowEnum getEnumPorValor(String valor) {
		for (TipoSituacaoWorkflowEnum pe : TipoSituacaoWorkflowEnum.values()) {
			if (pe.getValor().equals(valor)) {
				return pe;
			}
		}
		return null;
	}    
}