package negocio.interfaces.recursoshumanos;

import java.util.Date;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface CompetenciaFolhaPagamentoInterfaceFacade<T extends SuperVO> extends SuperFacadeInterface<T>{

	void consultarPorEnumCampoConsulta(DataModelo controleConsultaOtimizado, Date dataCompetencia) throws Exception;

	CompetenciaFolhaPagamentoVO consultarCompetenciaAtiva(Boolean retornarExcecao) throws Exception;

	void encerrarVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception;

	void clonarVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO);

	CompetenciaFolhaPagamentoVO consultarCompetenciaFolhaPagamentoPorMesAno(Integer mes, Integer ano, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterarDadosEncerramentoDaVigencia(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception;
	
	void validarValoresReferencia(UsuarioVO usuario);
	
	void validarFeriasAtivas(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception;
	
	void validarFinalDeFerias(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuario) throws Exception;

	public Date consultarDataCompetenciaAtiva() throws Exception;

	public void validarFuncionarioQueRetornouDeFerias(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO, UsuarioVO usuarioVO) throws Exception;
}
