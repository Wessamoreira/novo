package negocio.interfaces.recursoshumanos;

import java.math.BigDecimal;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface HistoricoPensaoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public void gravar(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, FuncionarioDependenteVO dependentePensao, BigDecimal valorPensao, UsuarioVO usuario) throws Exception;

}