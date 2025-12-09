/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

/**
 *
 * @author RODRIGO
 */
public class LogAplicacaoMetodo {// extends SuperArquitetura implements AfterReturningAdvice, MethodBeforeAdvice, Serializable {


//    public void afterReturning(Object o, Method method, Object[] os, Object o1) throws Throwable {
//        try {
//            if (context() != null && getUsuarioLogado() != null && !o1.getClass().getSimpleName().contains("CGLIB")) {
//                setLogger(Logger.getLogger(o1.getClass().getSimpleName()));
//                //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//                getLogger().info("Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " Finalizou o metodo \" " + method.getName() + "() \"");
//                if (os.length > 0) {
//                    getLogger().info("Paramentros do metodo: ");
//                    for (int i = 0; i < os.length; i++) {
//                        getLogger().info(os[i].toString() + "\n");
//                    }
//                }
//                getLogger().setLevel(Level.INFO);
//
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//
//    public void before(Method method, Object[] os, Object o) throws Throwable {
//        try {
//            if (context() != null && getUsuarioLogado() != null && !o.getClass().getSimpleName().contains("CGLIB")) {
//                setLogger(Logger.getLogger(o.getClass().getSimpleName()));
//                //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//                getLogger().info("Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " Iniciou o metodo \" " + method.getName() + "() \"");
//                if (os.length > 0) {
//                    getLogger().info("Paramentros do metodo: ");
//                    for (int i = 0; i < os.length; i++) {
//                        getLogger().info(os[i].toString() + "\n");
//                    }
//                }
//
//                getLogger().setLevel(Level.INFO);
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//    }
}
