package controle.arquitetura;

import java.io.Serializable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aspecto responsável por realizar auditagem em métodos definidos por determinada anotação.
 * 
 * @author Alessandro
 */
@Aspect
public class Aspecto extends SuperControle implements Serializable {

    /**
     * Pointcut responsável por interceptar métodos com anotação Auditavel. Observações: O nome do argumento descrito no Poingcut deve ser idêntico ao descrito
     * no método. Exemplo: anotacao. Dentro de um aspecto não deve existir mais de um pointcut que possa interceptar o mesmo método, pois neste caso não é
     * possível interpretar qual pointcut será escolhido. Neste caso deve-se criar outro aspecto.
     * 
     * @param anotacao
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Pointcut("execution(* negocio..incluir*(..)) || execution(* negocio..alterar*(..)) || execution(* negocio..excluir*(..))")
    public void metodosInterceptados() {
    }

//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
//     @Around("controle.arquitetura.Aspecto.metodosInterceptados()")
//    public Object durante(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
//        try {
//                    return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
//
//        } catch (Exception e) {
//            setLogger(Logger.getLogger(proceedingJoinPoint.getTarget().getClass().getSimpleName()));
//            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma excecao no metodo " +proceedingJoinPoint.getSignature().getName(), e);
//            getLogger().setLevel(Level.FATAL);
//            throw e;
//        } catch (Throwable t) {
//            setLogger(Logger.getLogger(proceedingJoinPoint.getTarget().getClass().getSimpleName()));
//            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma excecao no metodo " +proceedingJoinPoint.getSignature().getName(), t);
//            getLogger().setLevel(Level.FATAL);
//            throw new Exception(t);
//        }
//
//    }
    /**
     * Método que será executado no lugar do método interceptado.
     * 
     * @param proceedingJoinPoint
     * @param anotacao
     * @return
     * @throws Throwable
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @Before("controle.arquitetura.Aspecto.metodosInterceptados()")
    public void before(JoinPoint proceedingJoinPoint) throws Exception {
        try {
            if (context() != null && getUsuarioLogado() != null) {
                
                setLogger(Logger.getLogger(proceedingJoinPoint.getTarget().getClass().getSimpleName()));
                //getLogger().setResourceBundle(Uteis.getBundleLog4J());
                getLogger().info("Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " Iniciou o metodo \" " + proceedingJoinPoint.getSignature().getName() + "() \"");
//                if (os.length > 0) {
//                    getLogger().info("Paramentros do metodo: ");
//                    for (int i = 0; i < os.length; i++) {
//                        getLogger().info(os[i].toString() + "\n");
//                    }
//                }
                getLogger().setLevel(Level.INFO);

            }
        } catch (Exception e) {
            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma exececao no metodo " +proceedingJoinPoint.getSignature().getName(), e);
            getLogger().setLevel(Level.FATAL);
            throw e;
        } catch (Throwable t) {
            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma exececao no metodo " +proceedingJoinPoint.getSignature().getName(), t);
            getLogger().setLevel(Level.FATAL);
            throw new Exception(t);
        }
        //    return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    @AfterReturning("controle.arquitetura.Aspecto.metodosInterceptados()")
    public void after(JoinPoint proceedingJoinPoint) throws Exception {
        try {
            if (context() != null && getUsuarioLogado() != null) {
                
                setLogger(Logger.getLogger(proceedingJoinPoint.getTarget().getClass().getSimpleName()));
                //getLogger().setResourceBundle(Uteis.getBundleLog4J());
                getLogger().info("Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " Terminou o metodo \" " + proceedingJoinPoint.getSignature().getName() + "() \"");
//                if (os.length > 0) {
//                    getLogger().info("Paramentros do metodo: ");
//                    for (int i = 0; i < os.length; i++) {
//                        getLogger().info(os[i].toString() + "\n");
//                    }
//                }
                getLogger().setLevel(Level.INFO);

            }
        } catch (Exception e) {
            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma exececao no metodo " +proceedingJoinPoint.getSignature().getName(), e);
            getLogger().setLevel(Level.FATAL);
            throw e;
        } catch (Throwable t) {
            getLogger().info(" Usuario - " + getUsuarioLogado().getNome() + "(" + getUsuarioLogado().getCodigo() + ")" + " ocorreu uma exececao no metodo " +proceedingJoinPoint.getSignature().getName(), t);
            getLogger().setLevel(Level.FATAL);
                        throw new Exception(t);

        }
        //    return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }
    
}
