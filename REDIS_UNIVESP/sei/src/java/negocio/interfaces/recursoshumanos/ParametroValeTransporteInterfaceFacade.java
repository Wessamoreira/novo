package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface ParametroValeTransporteInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacao) throws Exception;

	public void inativar(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception;

	public void persistir(ParametroValeTransporteVO parametroValeTransporte,
			ParametroValeTransporteVO parametroValeTransporteEdicao, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	public Integer consultarPorInicioVigenciaEFimVigencia(ParametroValeTransporteVO obj) throws Exception;

	public boolean validarAlteracaoValor(BigDecimal valor1, BigDecimal valor2);

}
