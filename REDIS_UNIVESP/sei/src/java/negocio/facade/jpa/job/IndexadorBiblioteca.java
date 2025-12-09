/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jpa.job;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author OTIMIZE-09
 */
public class IndexadorBiblioteca extends SuperFacadeJDBC implements Runnable {

    public IndexadorBiblioteca() {
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void run() {
//        indexar();
    }

//    @SuppressWarnings("CallToThreadDumpStack")
//    private void indexar(Class<?> cls) {
//        try {
//            getFullTextSession().setFlushMode(FlushMode.MANUAL);
//            getFullTextSession().setCacheMode(CacheMode.IGNORE);
//            Transaction tx = getFullTextSession().beginTransaction();
//            ScrollableResults results = getFullTextSession()
//                    .createCriteria(cls)
//                    .setFetchSize(100)
//                    .scroll(ScrollMode.FORWARD_ONLY);
//            int index = 0;
//            while (results.next()) {
//                index++;
//                getFullTextSession().index(results.get(0)); // index each element
//                if (index % 25 == 0) {// estrategia de fetch para limpar a
//                    // sessao
//                    getFullTextSession().flush();
//                    getFullTextSession().clear();
//                    getFullTextSession().getSearchFactory().optimize(cls);
//                }
//            }
//            if (index % 25 != 0) {
//                getFullTextSession().flush();
//                getFullTextSession().clear();
//                getFullTextSession().getSearchFactory().optimize(cls);
//            }
//            tx.commit();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void indexar(Class<?>... classes) {
//        for (Class<?> cls : classes) {
//            indexar(cls);
//        }
//    }
//
//    public void indexar() {
//        indexar(CatalogoVO.class, ItemEmprestimoVO.class);
//    }
}
