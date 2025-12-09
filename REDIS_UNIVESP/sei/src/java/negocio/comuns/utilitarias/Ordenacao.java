/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.comuns.utilitarias;

/**
 * 
 * @author OPTIMIZE 3
 */
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

@SuppressWarnings("rawtypes")
public class Ordenacao implements Comparator, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3549079085251317992L;
    private String campo;

    // private static Ordenacao instance;

    // private static Ordenacao getInstance() {
    // if (instance == null) {
    // instance = new Ordenacao();
    // }
    // return instance;
    // }

    /**
     * Construtor privado somente para prevenir tentativas de instância direta da classe
     * 
     */
    public Ordenacao() {
        super();
    }

    public int compare(Object primeiro, Object segundo) {
        Object o1 = null, o2 = null;
        Method getMethod = null;
        try {
            if (!(primeiro == null || segundo == null)) {
                if (primeiro instanceof String) {
                    return (Uteis.retirarAcentuacao((String) primeiro).compareToIgnoreCase(Uteis.retirarAcentuacao((String) segundo)));
                }
                String[] campos = campo.split("\\.");
        		String campoComparar = campo;
        		if(campos.length > 1){
        			int tam = campos.length;
        			int x = 1;
        			for(String bean:campos){
        				if(x == tam){
        					campoComparar = bean;
        					break;
        				}
        				primeiro = (SuperVO)UtilReflexao.invocarMetodoGet(primeiro, bean);
        				segundo = (SuperVO)UtilReflexao.invocarMetodoGet(segundo, bean);
        				x++;
        			}
        		}
        		
                getMethod = primeiro.getClass().getDeclaredMethod("get".concat(campoComparar.substring(0, 1).toUpperCase()).concat(campoComparar.substring(1)));
                
                o1 = getMethod.invoke(primeiro);
                o2 = getMethod.invoke(segundo);
                if (o1 == null && o2 == null){
                	return 0;
                }
                if (o1 != null && o2 == null){
                	return 1;
                }
                if (o1 == null && o2 != null){
                	return -1;
                }

                if (o1 != null && o2 != null && o1 instanceof Integer || o1 instanceof Float || o1 instanceof Double || o1 instanceof Byte || o1 instanceof Long) {

                    Double numero = new Double(String.valueOf(o1));
                    return numero.compareTo(new Double(String.valueOf(o2)));
                }
                if (o1 != null && o2 != null && o1 instanceof String && o2 instanceof String) {
                    return (Uteis.retirarAcentuacao(String.valueOf(o1)).compareToIgnoreCase(Uteis.retirarAcentuacao(String.valueOf(o2))));
                }
                if (o1 != null && o2 != null && o1 instanceof Date && o2 instanceof Date) {
                    Date data = (Date) o1;
                    return data.compareTo((Date) o2);
                }
                if (o1 != null && o2 != null && o1 instanceof Boolean && o2 instanceof Boolean) {
                    Boolean data = (Boolean) o1;
                    return data.compareTo((Boolean) o2);
                }
            }
        } catch (NoSuchMethodException metodo) {
            throw new RuntimeException(metodo);
        } catch (InvocationTargetException invoke) {
            throw new RuntimeException(invoke);
        } catch (IllegalAccessException access) {
            throw new RuntimeException(access);

        } catch (Exception access) {
            throw new RuntimeException(access);
        }
        return -1;
    }

    /**
     * Método responsável por retorna uma lista de Object ordenada pelo nome do campo informado
     * 
     * @param lista
     * @param campo
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    public static List ordenarLista(List lista, String campo) {
        Ordenacao ordenacao = null;
        try {
            if ((lista != null) && (!lista.isEmpty()) && (campo != null) && (!(campo.trim().length() == 0))) {
                ordenacao = new Ordenacao();
                ordenacao.campo = (campo);
                Collections.sort(lista, ordenacao);
            }
            return lista;
        } finally {
            ordenacao = null;
        }
    }

    
    public static List ordenarListaDecrescente(List lista, String campo) {
        if ((lista != null) && (!lista.isEmpty()) && (campo != null) && (!(campo.trim().length() == 0))) {
            Collections.reverse(ordenarLista(lista, campo));
        }
        return lista;
    }

	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}
    
    
}
