package negocio.interfaces.protocolo;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.protocolo.AlterarResponsavelRequerimentoVO;

public interface AlterarResponsavelRequerimentoInterfaceFacade {

	void incluir(final AlterarResponsavelRequerimentoVO obj, UsuarioVO usuario) throws Exception;

	AlterarResponsavelRequerimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
	
	List<AlterarResponsavelRequerimentoVO> consultarOtimizado(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimentoVO, Date dataIni, Date dataFim, boolean todoPeriodo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, int limit, int offset ) throws Exception;
	
	Integer consultarTotalRegistros(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimentoVO, Date dataIni, Date dataFim, boolean todoPeriodo, UsuarioVO usuario) throws Exception;
	
}
