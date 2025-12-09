package negocio.facade.jdbc.arquitetura;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.DashboardVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.ModeloApresentacaoDashboardEnum;
import negocio.comuns.utilitarias.dominios.TipoDashboardEnum;
import negocio.interfaces.arquitetura.DashboardInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Dashboard extends ControleAcesso implements DashboardInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7472876109398697801L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(DashboardVO dashboardVO, UsuarioVO usuarioVO) throws Exception {
		if(!Uteis.isAtributoPreenchido(dashboardVO)) {
			incluir(dashboardVO, usuarioVO);
		}else{ 
			alterar(dashboardVO, usuarioVO);
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(DashboardVO dashboardVO, UsuarioVO usuarioVO) throws Exception {
		
		incluir(dashboardVO, "dashboard", new AtributoPersistencia().add("tipoDashboard", dashboardVO.getTipoDashboard())
				.add("ambiente", dashboardVO.getAmbiente())
				.add("ocultar", dashboardVO.getOcultar())
				.add("maximizado", dashboardVO.getMaximixado())
				.add("ordem", dashboardVO.getOrdem())
				.add("usuario", usuarioVO)
				.add("layoutRelatorioSEIDecidir", dashboardVO.getLayoutRelatorioSEIDecidirVO())
				.add("grafico", dashboardVO.getGrafico())
				.add("urlPdfView", dashboardVO.getUrlPdfView())
				.add("modulo", dashboardVO.getModulo())
				.add("modeloApresentacaoDashboard", dashboardVO.getModeloApresentacaoDashboard())
				.add("html", dashboardVO.getHtml()), usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(DashboardVO dashboardVO, UsuarioVO usuarioVO) throws Exception {
		alterar(dashboardVO, "dashboard", new AtributoPersistencia().add("tipoDashboard", dashboardVO.getTipoDashboard())
				.add("ambiente", dashboardVO.getAmbiente())
				.add("ocultar", dashboardVO.getOcultar())
				.add("maximizado", dashboardVO.getMaximixado())
				.add("ordem", dashboardVO.getOrdem())			
				.add("usuario", usuarioVO)
				.add("layoutRelatorioSEIDecidir", dashboardVO.getLayoutRelatorioSEIDecidirVO())
				.add("grafico", dashboardVO.getGrafico())
				.add("urlPdfView", dashboardVO.getUrlPdfView())
				.add("modulo", dashboardVO.getModulo())
				.add("modeloApresentacaoDashboard", dashboardVO.getModeloApresentacaoDashboard())
				.add("html", dashboardVO.getHtml()), new AtributoPersistencia().add("codigo", dashboardVO.getCodigo()), usuarioVO);
		
	}

	@Override
	public List<DashboardVO> consultarDashboardPorUsuarioAmbiente(UsuarioVO usuarioVO, TipoVisaoEnum ambiente)	throws Exception {
		StringBuilder sql = new StringBuilder("select * from dashboard where usuario = ? and ambiente = ? order by ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), usuarioVO.getCodigo(), ambiente.name());
		return montarDadosConsulta(rs);
	}
	
	@Override
	public List<DashboardVO> consultarDashboardPorUsuarioAmbiente(UsuarioVO usuarioVO, TipoVisaoEnum ambiente, PerfilAcessoModuloEnum modulo)	throws Exception {
		StringBuilder sql = new StringBuilder("select * from dashboard where usuario = ? and ambiente = ? and (modulo = 'TODOS' or modulo = ?) order by ordem ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), usuarioVO.getCodigo(), ambiente.name(), modulo.name());
		return montarDadosConsulta(rs);
	}
	
	private List<DashboardVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<DashboardVO> dashboardVOs = new ArrayList<DashboardVO>(0);
		while(rs.next()) {
			dashboardVOs.add(montarDados(rs));
		}
		return dashboardVOs;
	}
	
	private DashboardVO montarDados(SqlRowSet rs) throws Exception{
		DashboardVO dashboardVO = new DashboardVO();
		dashboardVO.setCodigo(rs.getInt("codigo"));
		dashboardVO.setOrdem(rs.getInt("ordem"));
		dashboardVO.setNovoObj(false);
		dashboardVO.getLayoutRelatorioSEIDecidirVO().setCodigo(rs.getInt("layoutRelatorioSEIDecidir"));
		dashboardVO.getUsuarioVO().setCodigo(rs.getInt("usuario"));
		dashboardVO.setAmbiente(TipoVisaoEnum.valueOf(rs.getString("ambiente")));
		dashboardVO.setTipoDashboard(TipoDashboardEnum.valueOf(rs.getString("tipoDashboard")));
		dashboardVO.setGrafico(rs.getString("grafico"));
		dashboardVO.setUrlPdfView(rs.getString("urlPdfView"));
		dashboardVO.setHtml(rs.getString("html"));
		// Retirado da versão da univesp - chamado: 35246
		dashboardVO.setOcultar(false);
		dashboardVO.setMaximizado(true);
		if(Uteis.isAtributoPreenchido(rs.getString("modulo"))) {
			dashboardVO.setModulo(PerfilAcessoModuloEnum.valueOf(rs.getString("modulo")));
		}
		if(Uteis.isAtributoPreenchido(rs.getString("modeloApresentacaoDashboard"))) {
			dashboardVO.setModeloApresentacaoDashboard(ModeloApresentacaoDashboardEnum.valueOf(rs.getString("modeloApresentacaoDashboard")));
		}
		return dashboardVO;
	}

}
