package negocio.facade.jdbc.arquitetura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public abstract class FacadeManager {

	private static FacadeFactory facadeFactory;

	@Autowired
	public void setFacadeFactory(FacadeFactory facadeFactory) {
		FacadeManager.facadeFactory = facadeFactory;
	}

	public static FacadeFactory getFacadeFactory() {
		return facadeFactory;
	}
	
	
	
}
