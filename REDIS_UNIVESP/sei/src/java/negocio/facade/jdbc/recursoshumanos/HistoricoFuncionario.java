package negocio.facade.jdbc.recursoshumanos;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.HistoricoFuncionarioInterfaceFacade;

@Service
@Scope
@Lazy
public class HistoricoFuncionario extends ControleAcesso implements HistoricoFuncionarioInterfaceFacade {

	private static final long serialVersionUID = 6283650354830489167L;

	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		dataModelo.setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, false));
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario, false));
	}

	@Override
	public void consultarPorEnumCampoConsultaSomenteProfessores(DataModelo dataModelo, String situacaoFuncionario, boolean consultarSomenteProfessores) throws Exception {
		dataModelo.setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, consultarSomenteProfessores));
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario, consultarSomenteProfessores));
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorFuncionarioCargo(DataModelo dataModelo, String where) {
		try {
			StringBuilder sqlStr = new StringBuilder(getFacadeFactory().getFuncionarioCargoFacade().getSQLTotalizadorFuncionarioCargo().append(where));
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

}
