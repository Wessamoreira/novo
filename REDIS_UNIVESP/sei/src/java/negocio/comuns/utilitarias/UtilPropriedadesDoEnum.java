package negocio.comuns.utilitarias;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.PropertyUtils; public class UtilPropriedadesDoEnum implements Serializable{

    @SuppressWarnings("unchecked")
    public static List getDadosDoEnum(Class classe) {
        ArrayList<HashMap<String, Object>> lista = new ArrayList<HashMap<String, Object>>();
        EnumSet<?> enumSet = EnumSet.allOf(classe);
        Object[] enums = enumSet.toArray();
        
        for (int i = 0; i < enums.length; i++) {
            List<String> propriedadesEnum = getColecaoDasPropriedades(PropertyUtils.getPropertyDescriptors(enums[i]));
            HashMap<String, Object> itemEnum = new HashMap<String, Object>();

            for (String propriedadeEnum : propriedadesEnum) {
                if (!propriedadeEnum.equals("declaringClass")) {
                    Object valorPropriedadeEnum = invocarMetodoGet(enums[i], propriedadeEnum);
                    itemEnum.put(propriedadeEnum, valorPropriedadeEnum);
                }
            }
            lista.add(itemEnum);
        }
        return lista;
    }

    public static List getListaDeValuesDoEnum(Class classe, String campoDesejado){
        List lista = new ArrayList();
        List<HashMap> listaDeDados = getDadosDoEnum(classe);
        for (HashMap mapa: listaDeDados){
            lista.add(mapa.get(campoDesejado));
        }
        return lista;
    }

    public static List<SelectItem> getListaSelectItemDoEnum(Class classe){
    	return getListaSelectItemDoEnum(classe, "valor", "descricao", false);
    }

    public static List<SelectItem> getListaSelectItemDoEnum(Class classe, boolean emBranco){
    	return getListaSelectItemDoEnum(classe, "valor", "descricao", emBranco);
    }

    public static List<SelectItem> getListaSelectItemDoEnumTodos(Class classe, boolean todos){
    	return getListaSelectItemDoEnumTodos(classe, "valor", "descricao", todos);
    }

    public static List<SelectItem> getListaSelectItemDoEnum(Class classe, String value, String label, boolean emBranco){
        List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
        if (emBranco) {
            listaDeSelectItem.add(new SelectItem("", ""));
        }
        for (Map<String, String> item : (List<Map<String,String>>) getDadosDoEnum(classe)) {
        	listaDeSelectItem.add(new SelectItem(item.get(value), item.get(label)));
        }
        
        return listaDeSelectItem;
    }

    public static List<SelectItem> getListaSelectItemDoEnumTodos(Class classe, String value, String label, boolean todos){
        List<SelectItem> listaDeSelectItem = new ArrayList<SelectItem>();
        if (todos) {
            listaDeSelectItem.add(new SelectItem("", "Todos"));
        }
        for (Map<String, String> item : (List<Map<String,String>>) getDadosDoEnum(classe)) {
        	listaDeSelectItem.add(new SelectItem(item.get(value), item.get(label)));
        }

        return listaDeSelectItem;
    }

    public static Object invocarMetodoGet(Object objetoDoMetodo, String nomeDaPropriedade) {
        try {
            Method metodo = objetoDoMetodo.getClass().getMethod(getNomeDoMetodoGet(nomeDaPropriedade), (Class[]) null);
            return metodo.invoke(objetoDoMetodo, (Object[]) null);
        } catch (Throwable e) {
            return null;
        }
    }

    public static String getNomeDoMetodoGet(String nomeDoAtributo) {
        return "get" + nomeDoAtributo.substring(0, 1).toUpperCase() + nomeDoAtributo.substring(1);
    }

    public static Object invocarMetodoGet(Object objetoDoMetodo, Method metodo) {
        try {
            return metodo.invoke(objetoDoMetodo, (Object[]) null);
        } catch (Throwable e) {
            return null;
        }
    }

    private static List<String> getColecaoDasPropriedades(PropertyDescriptor[] descriptors) {
        List<String> listaDeNomes = new ArrayList<String>();
        for (PropertyDescriptor descriptor : descriptors) {
            if (!descriptor.getName().equals("class")) {
                listaDeNomes.add(descriptor.getName());
            }
        }
        return listaDeNomes;
    }
}
