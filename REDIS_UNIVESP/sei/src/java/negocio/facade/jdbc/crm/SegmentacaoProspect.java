package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.TipoLayoutApresentacaoResultadoSegmentacaoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.segmentacao.SegmentacaoOpcaoVO;
import negocio.comuns.segmentacao.SegmentacaoProspectVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.SegmentacaoProspectInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
public class SegmentacaoProspect extends ControleAcesso implements SegmentacaoProspectInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String idEntidade;

	public SegmentacaoProspect() throws Exception {
		super();
		setIdEntidade("SegmentacaoProspect");
	}

	public void adicionarObjSegmentacaoVOs(SegmentacaoProspectVO segmentacaoProspect, SegmentacaoOpcaoVO segmentacaoOpcao) throws Exception {
		int index = 0;
		Iterator i = segmentacaoProspect.getSegmentacaoOpcaoVOs().iterator();
		while (i.hasNext()) {
			SegmentacaoOpcaoVO objExistente = (SegmentacaoOpcaoVO) i.next();
			if (objExistente.getDescricao().equals(segmentacaoOpcao.getDescricao())) {
				segmentacaoProspect.getSegmentacaoOpcaoVOs().set(index, segmentacaoOpcao);
				return;
			}
			index++;
		}
		segmentacaoProspect.getSegmentacaoOpcaoVOs().add(segmentacaoOpcao);
	}

	@Override
	public void validarDados(SegmentacaoProspectVO segmentacaoProspectVO) throws Exception {

		if (segmentacaoProspectVO.getDescricao().equals("")) {
			throw new ConsistirException("O Campo descrição (Segmentação é necessário) !");
		}

		if (segmentacaoProspectVO.getSegmentacaoOpcaoVOs().isEmpty()) {
			throw new ConsistirException("Inisira pelo menos uma opção de segmentação !");
		}

		if (consultarSegmentacaoPorDescricao(segmentacaoProspectVO.getDescricao(), false, null) && (segmentacaoProspectVO.getVerificaMudancaDescricaoSegmentacao())) {
			throw new ConsistirException("Segmento já cadastrado !");
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(SegmentacaoProspectVO segmentacaoProspectVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		if(segmentacaoProspectVO.getCodigo().equals(0)) {
			incluir(segmentacaoProspectVO, configuracaoGeralSistema, controlarAcesso, usuarioVO);
		} else {
			alterar(segmentacaoProspectVO, configuracaoGeralSistema, controlarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SegmentacaoProspectVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			SegmentacaoProspect.incluir(getIdEntidade(), controlarAcesso, usuario);
			validarDados(obj);
			incluir(obj, "segmentacaoprospect", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())
					.add("ativarsegmentacao", obj.getAtivaSegmentacao())
					.add("tipoLayoutApresentacaoResultadoSegmentacao", obj.getTipoLayoutApresentacaoResultadoSegmentacaoEnum().getName()), usuario);
			
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getSegmentacaOpcaoFacade().incluirSegmentacaoOpcoes(obj, usuario);

		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final SegmentacaoProspectVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			SegmentacaoProspect.alterar(getIdEntidade(), controlarAcesso, usuario);
			validarDados(obj);
			final String sql = "UPDATE segmentacaoprospect SET descricao=?, ativarsegmentacao=?, tipoLayoutApresentacaoResultadoSegmentacao=? WHERE ((codigo = ? ))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement sqlAlterar = conn.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setString(2, obj.getAtivaSegmentacao());
					sqlAlterar.setString(3, obj.getTipoLayoutApresentacaoResultadoSegmentacaoEnum().getName());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getSegmentacaOpcaoFacade().alterarOpcoesSegmentacao(obj, usuario);

		} catch (Exception e) {

			throw e;
		}
	}

	@Override
	public List<SegmentacaoProspectVO> consultarSegmento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from segmentacaoprospect where upper(sem_acentos(descricao) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) order by descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<SegmentacaoProspectVO> consultarSegmentosAtivos(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select * from segmentacaoprospect where upper(sem_acentos(descricao) ) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) and ativarsegmentacao = 'ATIVAR' order by descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public static List<SegmentacaoProspectVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<SegmentacaoProspectVO> vetResultado = new ArrayList<SegmentacaoProspectVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	@SuppressWarnings("unchecked")
	public static SegmentacaoProspectVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		SegmentacaoProspectVO obj = new SegmentacaoProspectVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setAtivaSegmentacao(dadosSQL.getString("ativarsegmentacao"));
		obj.setTipoLayoutApresentacaoResultadoSegmentacaoEnum(TipoLayoutApresentacaoResultadoSegmentacaoEnum.valueOf(dadosSQL.getString("tipoLayoutApresentacaoResultadoSegmentacao")));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setSegmentacaoOpcaoVOs(getFacadeFactory().getSegmentacaOpcaoFacade().consultarOpcoesSegmentacao(obj.getCodigo(), nivelMontarDados));
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirSegmentacaoProspect(SegmentacaoProspectVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			for (SegmentacaoOpcaoVO segmentacaoOpcaoVO : obj.getSegmentacaoOpcaoVOs()) {
				getFacadeFactory().getSegmentacaOpcaoFacade().excluirSegmentacaoOpcao(segmentacaoOpcaoVO, usuarioVO);
			}
			SegmentacaoOpcao.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM segmentacaoprospect WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw new Exception("Este registro é referenciado por outro cadastro, por isto não pode ser excluído e/ou modificado.");
		} finally {
		}
	}

	@Override
	public SegmentacaoProspectVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM SegmentacaoProspect WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Segmentação Prosppect ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public Boolean consultarSegmentacaoPorDescricao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM segmentacaoprospect WHERE upper( descricao ) =('" + valorConsulta.toUpperCase() + "') AND (descricao IS NOT NULL OR descricao <> '') ORDER BY descricao limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);

		if (tabelaResultado.next()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public SegmentacaoProspectVO consultaProspectVinculado(SegmentacaoProspectVO segmentacaoProspect) throws Exception {
		String sqlStr = "select sp.codigo from prospectsegmentacaoopcao pso " + "inner join prospects p on pso.prospect = p.codigo " + "inner join segmentacaoopcao so on so.codigo = pso.segmentacaoopcao " + "inner join segmentacaoprospect sp on sp.codigo = so.segmentacaoprospect " + "where sp.codigo = " + segmentacaoProspect.getCodigo();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			throw new Exception("Esta segmentação não pode ser inativada, pois a mesma é vinculada a um Prospect !");
		} else {
			return new SegmentacaoProspectVO();
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SegmentacaoProspect.idEntidade = idEntidade;
	}
	
	
	@Override
	public List<SegmentacaoProspectVO> consultarSegmentacaoProspect(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum, Integer unidadeEspecifica) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct segmentacaoopcao.codigo as segmentacaoopcao, segmentacaoprospect.codigo as codigosegmentacaoprospect, segmentacaoprospect.tipoLayoutApresentacaoResultadoSegmentacao, segmentacaoprospect.descricao as segmentacaoprospect, segmentacaoopcao.descricao as descricaosegmentacaoopcao, count(distinct prospects.codigo) as qtde");
		sqlStr.append(" from prospects ");
		sqlStr.append(" inner join prospectsegmentacaoopcao  on prospects.codigo = prospectsegmentacaoopcao.prospect");
		sqlStr.append(" inner join segmentacaoopcao  on segmentacaoopcao.codigo = prospectsegmentacaoopcao.segmentacaoopcao");
		sqlStr.append(" inner join segmentacaoprospect  on segmentacaoopcao.segmentacaoprospect = segmentacaoprospect.codigo");
		sqlStr.append(" left join unidadeensino on unidadeensino.codigo = prospects.unidadeensino ");
		sqlStr.append(" where prospects.inativo = false ");
		if (painelGestorTipoMonitoramentoCRMEnum == null) {
			if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
				sqlStr.append(" and (prospects.unidadeensino in (0 ");
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
				sqlStr.append(" )  or  prospects.unidadeensino is  null)");
			}
		} else {
			if (painelGestorTipoMonitoramentoCRMEnum != null) {
				if (unidadeEspecifica == null || unidadeEspecifica == 0) {
					sqlStr.append(" and prospects.unidadeensino is null ");
				} else {
					sqlStr.append(" and prospects.unidadeensino = ").append(unidadeEspecifica);
				}
			}
		}
		   
		sqlStr.append(" group by segmentacaoprospect.codigo, segmentacaoprospect.tipoLayoutApresentacaoResultadoSegmentacao, segmentacaoprospect.descricao, segmentacaoopcao.descricao, segmentacaoopcao.codigo ");
		sqlStr.append(" order by segmentacaoprospect.codigo, segmentacaoprospect.tipoLayoutApresentacaoResultadoSegmentacao, segmentacaoprospect.descricao, segmentacaoopcao.descricao, segmentacaoopcao.codigo, qtde desc ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		SegmentacaoOpcaoVO segmentacaoOpcaoVO = null;
		List<SegmentacaoProspectVO> segmentacaoProspectVOs = new ArrayList<SegmentacaoProspectVO>();
		List<SegmentacaoOpcaoVO> segmentacaoOpcaoVOs = new ArrayList<SegmentacaoOpcaoVO>();
		while(rs.next()) {
			segmentacaoOpcaoVO = new SegmentacaoOpcaoVO();
			segmentacaoOpcaoVO.setCodigo(rs.getInt("segmentacaoopcao"));
			segmentacaoOpcaoVO.getSegmentacaoProspectVO().setCodigo(rs.getInt("codigosegmentacaoprospect"));
			segmentacaoOpcaoVO.getSegmentacaoProspectVO().setDescricao(rs.getString("segmentacaoprospect"));
			segmentacaoOpcaoVO.getSegmentacaoProspectVO().setTipoLayoutApresentacaoResultadoSegmentacaoEnum(TipoLayoutApresentacaoResultadoSegmentacaoEnum.valueOf(rs.getString("tipoLayoutApresentacaoResultadoSegmentacao")));
			segmentacaoOpcaoVO.setDescricao(rs.getString("descricaosegmentacaoopcao"));
			segmentacaoOpcaoVO.setQuantidade(rs.getInt("qtde"));
			segmentacaoOpcaoVOs.add(segmentacaoOpcaoVO);
		}
		for (SegmentacaoOpcaoVO segmentacaoOpcaoVO2 : segmentacaoOpcaoVOs) {
			if (segmentacaoProspectVOs.contains(segmentacaoOpcaoVO2.getSegmentacaoProspectVO())) {
				for (SegmentacaoProspectVO obj : segmentacaoProspectVOs) {
					if (obj.equals(segmentacaoOpcaoVO2.getSegmentacaoProspectVO())) {
						obj.getSegmentacaoOpcaoVOs().add(segmentacaoOpcaoVO2);
						break;
					}
				}
			} else {
				SegmentacaoProspectVO segmentacaoProspectVO = segmentacaoOpcaoVO2.getSegmentacaoProspectVO();
				segmentacaoProspectVO.getSegmentacaoOpcaoVOs().add(segmentacaoOpcaoVO2);
				segmentacaoProspectVOs.add(segmentacaoProspectVO);
			}
			
		}		
 		return segmentacaoProspectVOs;
	}
}
