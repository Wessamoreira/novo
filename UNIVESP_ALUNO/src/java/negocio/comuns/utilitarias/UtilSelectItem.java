package negocio.comuns.utilitarias;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.faces. model.SelectItem;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;

/**
 *
 * @author Diego
 */
public class UtilSelectItem {

	@SuppressWarnings("unchecked")
	public static List<SelectItem> getListaSelectItem(List<?> listaConsulta, String campoValor, String campoDescricao, boolean campoEmBranco) throws Exception {
		List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
		if (campoEmBranco) {
			listaDeSelectItem.add(new SelectItem(0, ""));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		try {
			if (listaConsulta != null && !listaConsulta.isEmpty()) {
				for (Object item : listaConsulta) {
					listaDeSelectItem.add(new SelectItem(UtilReflexao.invocarMetodoGet(item, campoValor), (String) UtilReflexao.invocarMetodoGet(item, campoDescricao)));
					Uteis.removerObjetoMemoria(item);
				}
				Collections.sort(listaDeSelectItem, ordenador);
			}
		} catch (ClassCastException e) {

			throw new ConsistirException("Desenvolvedor, o campo escolhido para montar a lista de SelectItem não existe.");

		} finally {
			Uteis.liberarListaMemoria(listaConsulta);
		}
		return listaDeSelectItem;
	}
	
	@SuppressWarnings("unchecked")
	public static List<SelectItem> getListaSelectItem(List<?> listaConsulta, String campoValor, String campoDescricao, boolean campoEmBranco, boolean utilizarOrdenador) throws Exception {
		List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
		if (campoEmBranco) {
			listaDeSelectItem.add(new SelectItem(0, ""));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		try {
			if (listaConsulta != null) {
				for (Object item : listaConsulta) {
					listaDeSelectItem.add(new SelectItem(UtilReflexao.invocarMetodoGet(item, campoValor), (String) UtilReflexao.invocarMetodoGet(item, campoDescricao)));
					Uteis.removerObjetoMemoria(item);
				}
				if (utilizarOrdenador) {
					Collections.sort(listaDeSelectItem, ordenador);
				}
			}
		} catch (ClassCastException e) {

			throw new ConsistirException("Desenvolvedor, o campo escolhido para montar a lista de SelectItem não existe.");

		} finally {
			Uteis.liberarListaMemoria(listaConsulta);
		}
		return listaDeSelectItem;
	}


	public static List<SelectItem> getListaSelectItem(List<?> listaConsulta, String campoValor, String campoDescricao) throws Exception {
		return getListaSelectItem(listaConsulta, campoValor, campoDescricao, true);
	}

	public static List<SelectItem> getListaSelectItemEnum(Enum[] enumeradores, Obrigatorio obrigatorio) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		if (obrigatorio == Obrigatorio.NAO) {
			lista.add(new SelectItem(null, ""));
		}
		for (Enum enumerador : enumeradores) {
			if (enumerador != null) {
				lista.add(new SelectItem(enumerador, internacionalizarEnum(enumerador)));
			}
		}
		return lista;
	}

	public static String internacionalizarEnum(Enum enumerador) {
		return UteisJSF.internacionalizar("enum_" + enumerador.getClass().getSimpleName() + "_" + enumerador.toString());
	}

}