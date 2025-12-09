package negocio.comuns.academico.enumeradores;

import jakarta.faces.model.SelectItem;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * enum com a finalidade de informar todos os sistemas integrados com SEI para
 * importar conteúdos no módulo EAD
 * chamado 39853
 * 
 * @author Felipi Alves
 */
public enum TipoIntegradorConteudoEnum {

	AUREA("SISTEMA_AUREA", "Áurea");

	private final String valor;
	private final String nome;

	TipoIntegradorConteudoEnum(String valor, String nome) {
		this.valor = valor;
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public String getNome() {
		return nome;
	}
	
	public static TipoIntegradorConteudoEnum getTipoIntegradorConteudoPorValor(String valor) {
		if (Uteis.isAtributoPreenchido(valor)) {
			switch (valor) {
			case "SISTEMA_AUREA":
				return TipoIntegradorConteudoEnum.AUREA;
			default:
				return null;
			}
		}
		return null;
	}

	/**
	 * get que retorna uma lista de select item com todos os integradores
	 * disponíveis no enum, retornando no valor do SelectItem o PRÓPRIO ENUM
	 * TipoIntegradorConteudoEnum, e na label o nome do integrador
	 * chamado 39853
	 * 
	 * @author Felipi Alves
	 * @return List<SelectItem>
	 */
	public static List<SelectItem> getListaSelectItemTipoIntegradorConteudo(Boolean campoNull) {
		if (campoNull) {
			List<SelectItem> selectItems = new ArrayList<>();
			selectItems.add(new SelectItem(null, Constantes.EMPTY));
			new ArrayList<>(Arrays.asList(TipoIntegradorConteudoEnum.values())).stream().map(tipoIntegradorConteudoEnum -> new SelectItem(tipoIntegradorConteudoEnum, tipoIntegradorConteudoEnum.getNome())).forEach(selectItems::add);
			return selectItems;
		} else {
			return new ArrayList<>(Arrays.asList(TipoIntegradorConteudoEnum.values())).stream().map(tipoIntegradorConteudoEnum -> new ArrayList<>(Arrays.asList(new SelectItem(tipoIntegradorConteudoEnum, tipoIntegradorConteudoEnum.getNome())))).findAny().get();
		}
	}
	
	public Boolean isSistemaAurea() {
		return equals(TipoIntegradorConteudoEnum.AUREA);
	}
}
