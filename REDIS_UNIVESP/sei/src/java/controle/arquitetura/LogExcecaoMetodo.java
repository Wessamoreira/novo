/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.arquitetura;

/**
 *
 * @author RODRIGO
 */
//@Service
//@Aspect
public class LogExcecaoMetodo { //extends SuperArquitetura implements ThrowsAdvice, Serializable {

//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//    public void afterThrowing(Method method,
//            Object[] args,
//            Object target,
//            Exception subclass)
//            throws Throwable {
//
//        if (context() != null && getUsuarioLogado() != null) {
//            setLogger(Logger.getLogger(target.getClass().getSimpleName()));
//            //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma exececao no metodo " + method.getName(), subclass);
//            //getLogger().debug("Exceção: (" + subclass.getMessage()+")", subclass);
//            getLogger().setLevel(Level.FATAL);
//        }
//        throw subclass;
//    }
//
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//    public void afterThrowing(Method method,
//            Object[] args,
//            Object target,
//            Throwable subclass)
//            throws Throwable {
//
//        if (context() != null && getUsuarioLogado() != null) {
//            setLogger(Logger.getLogger(target.getClass().getSimpleName()));
//            //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//            getLogger().info(" Usuário - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma execeção no método " + method.getName(), subclass);
//            //getLogger().debug("Exceção: (" + subclass.getMessage()+")", subclass);
//            getLogger().setLevel(Level.FATAL);
//        }
//        throw subclass;
//    }
//
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//    public void afterThrowing(Method method,
//            Object[] args,
//            Object target,
//            DataAccessException subclass)
//            throws Throwable {
//
//        if (context() != null && getUsuarioLogado() != null) {
//            setLogger(Logger.getLogger(target.getClass().getSimpleName()));
//            //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//            getLogger().info(" Usuário - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma execeção no método " + method.getName(), subclass);
//            //getLogger().debug("Exceção: (" + subclass.getMessage()+")", subclass);
//            getLogger().setLevel(Level.FATAL);
//        }
//        throw subclass;
//    }
//
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//    public void afterThrowing(Method method,
//            Object[] args,
//            Object target,
//            SQLException subclass)
//            throws Throwable {
//
//        if (context() != null && getUsuarioLogado() != null) {
//            setLogger(Logger.getLogger(target.getClass().getSimpleName()));
//            //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//            getLogger().info(" Usuário - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma execeção no método " + method.getName(), subclass);
//            //getLogger().debug("Exceção: (" + subclass.getMessage()+")", subclass);
//            getLogger().setLevel(Level.FATAL);
//        }
//        throw subclass;
//    }
//
//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//    public void afterThrowing(Method method,
//            Object[] args,
//            Object target,
//            Runtime subclass)
//            throws Throwable {
//
//        if (context() != null && getUsuarioLogado() != null) {
//            setLogger(Logger.getLogger(target.getClass().getSimpleName()));
//            //getLogger().setResourceBundle(Uteis.getBundleLog4J());
//            getLogger().info(" Usuário - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma execeção no método " + method.getName() + " Runtime");
//            //getLogger().debug("Exceção: (" + subclass.getMessage()+")", subclass);
//            getLogger().setLevel(Level.FATAL);
//        }
//        throw new Exception(subclass.getRuntime().toString());
//    }
}
