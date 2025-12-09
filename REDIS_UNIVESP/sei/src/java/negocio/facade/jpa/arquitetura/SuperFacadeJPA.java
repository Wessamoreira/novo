/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jpa.arquitetura;

import java.io.Serializable;

/**
 *
 * @author Paulo
 */
//@Service
//@Scope(value="singleton")
public class SuperFacadeJPA  implements Serializable {

    public SuperFacadeJPA() {
    }

//    @Autowired
//    public void inicializar(SessionFactory sessionFactory) {
//        setSessionFactory(sessionFactory);
//    }
//
//    public FullTextSession getFullTextSession() {
//        return Search.createFullTextSession(getSessionFactory().getCurrentSession());
//    }
}
