package negocio.facade.jdbc.arquitetura;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import negocio.comuns.arquitetura.RegistroWebserviceVO;
import negocio.interfaces.arquitetura.RegistroWebserviceInterfaceFacade;

@Service
@Scope("singleton")
public class RegistroWebservice extends ControleAcesso implements RegistroWebserviceInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3230066270161183998L;

	@Override
	public void incluir(final RegistroWebserviceVO registroWebserviceVO)
			throws Exception {
		incluir(registroWebserviceVO, "registrowebservice", 
				new AtributoPersistencia().add("jsonRecebido", registroWebserviceVO.getJsonRecebido())
				.add("jsonRetorno", registroWebserviceVO.getJsonRetorno())
				.add("servico", registroWebserviceVO.getServico())
				.add("ip", registroWebserviceVO.getIp())
				.add("usuario", registroWebserviceVO.getUsuario()), null);
		
	}

}
