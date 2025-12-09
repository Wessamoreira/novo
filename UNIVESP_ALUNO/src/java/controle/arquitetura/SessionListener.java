package controle.arquitetura;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

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
