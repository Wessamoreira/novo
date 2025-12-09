/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;

/**
 *
 * @author Edigar
 */
public class SelectItemOrdemValor extends BeanComparator {
    
    public SelectItemOrdemValor() {
        super("label", new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((String)o1).compareTo((String)o2);
            }});
    }
}