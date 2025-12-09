package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.academico.ConfiguracaoLayoutHistoricoVO;
import negocio.comuns.academico.ConfiguracaoObservacaoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoObservacaoHistoricoInterfaceFacade;
import relatorio.negocio.comuns.academico.enumeradores.TipoObservacaoHistoricoEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoObservacaoHistorico extends ControleAcesso
		implements ConfiguracaoObservacaoHistoricoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1824437779993618499L;
	private static final String nomeTabela = "configuracaoObservacaoHistorico";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		for(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO : configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs()) {
			configuracaoObservacaoHistoricoVO.setConfiguracaoHistoricoVO(configuracaoHistoricoVO);
			persistir(configuracaoObservacaoHistoricoVO, usuarioVO);			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
			validarDados(configuracaoObservacaoHistoricoVO);
			if(Uteis.isAtributoPreenchido(configuracaoObservacaoHistoricoVO)) {
				alterar(configuracaoObservacaoHistoricoVO, usuarioVO);
			}else {
				incluir(configuracaoObservacaoHistoricoVO, usuarioVO);
			}			
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {		
		incluir(configuracaoObservacaoHistoricoVO, nomeTabela, new AtributoPersistencia()
				.add("observacao", configuracaoObservacaoHistoricoVO.getObservacao())
				.add("configuracaoHistorico", configuracaoObservacaoHistoricoVO.getConfiguracaoHistoricoVO())
				.add("tipoObservacaoHistorico", configuracaoObservacaoHistoricoVO.getTipoObservacaoHistorico())
				.add("ocultar", configuracaoObservacaoHistoricoVO.getOcultar())
				.add("padrao", configuracaoObservacaoHistoricoVO.getPadrao()), usuarioVO);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		alterar(configuracaoObservacaoHistoricoVO, nomeTabela, new AtributoPersistencia()
				.add("observacao", configuracaoObservacaoHistoricoVO.getObservacao())
				.add("configuracaoHistorico", configuracaoObservacaoHistoricoVO.getConfiguracaoHistoricoVO())
				.add("ocultar", configuracaoObservacaoHistoricoVO.getOcultar())
				.add("tipoObservacaoHistorico", configuracaoObservacaoHistoricoVO.getTipoObservacaoHistorico())
				.add("padrao", configuracaoObservacaoHistoricoVO.getPadrao()), 
				new AtributoPersistencia().add("codigo", configuracaoObservacaoHistoricoVO.getCodigo()),
				usuarioVO);
	}

	@Override
	public void adicionarConfiguracaoObservacaoHistoricoVO(ConfiguracaoHistoricoVO configuracaoHistoricoVO,
			ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().stream()
				.anyMatch(l -> Uteis.removerAcentos(l.getObservacao())
						.equalsIgnoreCase(Uteis.removerAcentos(configuracaoObservacaoHistoricoVO.getObservacao())))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_historico_layout_existente"));
		}
		configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().add(configuracaoObservacaoHistoricoVO);

	}

	@Override
	public List<ConfiguracaoObservacaoHistoricoVO> consultarPorConfiguracaoHistorico(Integer configuracaoHistorico,
			UsuarioVO usuarioVO) throws Exception {
		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select * from configuracaoObservacaoHistorico where configuracaoHistorico = ? ", configuracaoHistorico), usuarioVO);
	}
	

	private List<ConfiguracaoObservacaoHistoricoVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<ConfiguracaoObservacaoHistoricoVO> configuracaoObservacaoHistoricoVOs =  new ArrayList<ConfiguracaoObservacaoHistoricoVO>(0);
		while(rs.next()) {
			configuracaoObservacaoHistoricoVOs.add(montarDados(rs, usuarioVO));
		}
		return configuracaoObservacaoHistoricoVOs;
		
	} 
	
	private ConfiguracaoObservacaoHistoricoVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO =  new ConfiguracaoObservacaoHistoricoVO();
		configuracaoObservacaoHistoricoVO.setNovoObj(false);
		configuracaoObservacaoHistoricoVO.setCodigo(rs.getInt("codigo"));
		configuracaoObservacaoHistoricoVO.setObservacao(rs.getString("observacao"));
		configuracaoObservacaoHistoricoVO.setOcultar(rs.getBoolean("ocultar"));
		configuracaoObservacaoHistoricoVO.setPadrao(rs.getBoolean("padrao"));
		configuracaoObservacaoHistoricoVO.setTipoObservacaoHistorico(TipoObservacaoHistoricoEnum.valueOf(rs.getString("tipoObservacaoHistorico")));
		configuracaoObservacaoHistoricoVO.getConfiguracaoHistoricoVO().setCodigo(rs.getInt("configuracaoHistorico"));		
		return configuracaoObservacaoHistoricoVO;
	}
	
	private void validarDados(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO) throws ConsistirException{
		if(configuracaoObservacaoHistoricoVO.getObservacao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_configuracaoObservacaoHistorico_observacao"));
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void adicionarConfiguracaoObservacaoHistoricoVO(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception{
		validarDados(configuracaoObservacaoHistoricoVO);
		if(Uteis.isAtributoPreenchido(configuracaoObservacaoHistoricoVO) && configuracaoObservacaoHistoricoVO.getOcultar()) {
			configuracaoObservacaoHistoricoVO.setOcultar(false);
			persistir(configuracaoObservacaoHistoricoVO, usuarioVO);			
		}else if(!Uteis.isAtributoPreenchido(configuracaoObservacaoHistoricoVO)) {
			if(!configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().stream().anyMatch(o -> o.getTipoObservacaoHistorico().equals(configuracaoObservacaoHistoricoVO.getTipoObservacaoHistorico()) && Uteis.removerAcentos(o.getObservacao()).equalsIgnoreCase(Uteis.removerAcentos(configuracaoObservacaoHistoricoVO.getObservacao())))) {
				configuracaoObservacaoHistoricoVO.setConfiguracaoHistoricoVO(configuracaoHistoricoVO);
				persistir(configuracaoObservacaoHistoricoVO, usuarioVO);
				configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().add(configuracaoObservacaoHistoricoVO);
				configuracaoHistoricoVO.setConfiguracaoObservacaoHistoricoComplementarVOs(null);
				configuracaoHistoricoVO.setConfiguracaoObservacaoHistoricoCertificadoEstudosVOs(null);
				configuracaoHistoricoVO.setConfiguracaoObservacaoHistoricoIntegralizacaoVOs(null);
			}
		}
	}
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerConfiguracaoObservacaoHistoricoVO(ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception{
		if(Uteis.isAtributoPreenchido(configuracaoObservacaoHistoricoVO)) {
			configuracaoObservacaoHistoricoVO.setOcultar(true);
			persistir(configuracaoObservacaoHistoricoVO, usuarioVO);
		}else {
			configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().removeIf(o -> o.getTipoObservacaoHistorico().equals(configuracaoObservacaoHistoricoVO.getTipoObservacaoHistorico()) && Uteis.removerAcentos(o.getObservacao()).equalsIgnoreCase(Uteis.removerAcentos(configuracaoObservacaoHistoricoVO.getObservacao())));			
		}
	}
	
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarDefinicaoObservacaoPadraoConfiguracaoObservacaoHistorico(ConfiguracaoHistoricoVO configuracaoHistoricoVO, ConfiguracaoObservacaoHistoricoVO configuracaoObservacaoHistoricoVO, UsuarioVO usuarioVO) throws Exception{
			if(configuracaoObservacaoHistoricoVO.getPadrao() && configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().stream().anyMatch(o -> o.getPadrao() && !o.getCodigo().equals(configuracaoObservacaoHistoricoVO.getCodigo()) && o.getTipoObservacaoHistorico().equals(configuracaoObservacaoHistoricoVO.getTipoObservacaoHistorico()))) {
				ConfiguracaoObservacaoHistoricoVO  configuracaoObservacaoHistoricoVO2 = configuracaoHistoricoVO.getConfiguracaoObservacaoHistoricoVOs().stream().filter(o -> o.getPadrao() && !o.getCodigo().equals(configuracaoObservacaoHistoricoVO.getCodigo()) && o.getTipoObservacaoHistorico().equals(configuracaoObservacaoHistoricoVO.getTipoObservacaoHistorico())).findFirst().get();
				configuracaoObservacaoHistoricoVO2.setPadrao(false);
				alterar(configuracaoObservacaoHistoricoVO2, usuarioVO);
			}
			alterar(configuracaoObservacaoHistoricoVO, usuarioVO);
	}

}
