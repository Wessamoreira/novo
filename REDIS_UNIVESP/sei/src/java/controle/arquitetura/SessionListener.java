package controle.arquitetura;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author Alessandro Lima
 */
public class SessionListener implements HttpSessionListener, java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
    public void sessionCreated(HttpSessionEvent event) {
         SessionTracker.instance().add(event.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent event) {
         SessionTracker.instance().remove(event.getSession());
    }
    
}
