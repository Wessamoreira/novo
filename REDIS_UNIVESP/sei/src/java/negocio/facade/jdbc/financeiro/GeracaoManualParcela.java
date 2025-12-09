package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import controle.arquitetura.AplicacaoControle;
import jobs.JobGeracaoManualParcela;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.GeracaoManualParcelaVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoGeracaoManualParcelaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.GeracaoManualParcelaInterfaceFacade;

@Repository
@Lazy
public class GeracaoManualParcela extends ControleAcesso implements GeracaoManualParcelaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1320303576967375794L;
	private static String idEntidade = "GeracaoManualParcelas";

	@Override
	public void realizarConsultarContaReceberGerar(GeracaoManualParcelaVO geracaoManualParcelaVO, UsuarioVO usuarioVO) throws Exception {
		geracaoManualParcelaVO.setValorTotalParcelasGerar(0.0);
		geracaoManualParcelaVO.setQtdeParcelaGerar(0);
		geracaoManualParcelaVO.getUsuario().setCodigo(usuarioVO.getCodigo());
		geracaoManualParcelaVO.getUsuario().setNome(usuarioVO.getNome());
		validarDados(geracaoManualParcelaVO);
		if (geracaoManualParcelaVO.getTurma().getCodigo().intValue() != 0) {
			if (geracaoManualParcelaVO.getGerarTodasParcelas()) {
				geracaoManualParcelaVO.setMesReferencia("");
				geracaoManualParcelaVO.setAnoReferencia("");
			}
		}
        if(geracaoManualParcelaVO.getTurma().getTurmaAgrupada()) {
        	getFacadeFactory().getTurmaAgrupadaFacade().carregarDadosTurmaAgrupada(geracaoManualParcelaVO.getTurma(), usuarioVO);
        }

		Map<String, Object> resultado = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarPorMesReferenciaSituacaoUnidadeTotalRegistroTotalValor(geracaoManualParcelaVO.getMesReferencia(), geracaoManualParcelaVO.getAnoReferencia(), "NG", geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), geracaoManualParcelaVO.getCurso().getCodigo(), geracaoManualParcelaVO.getTurma(), geracaoManualParcelaVO.getPermitirGerarParcelaAlunoPreMatricula(), geracaoManualParcelaVO.getUtilizarDataCompetencia());
		if (resultado.containsKey("QTDE")) {
			geracaoManualParcelaVO.setQtdeParcelaGerar((Integer) resultado.get("QTDE"));
		}
		if (resultado.containsKey("VALOR")) {
			geracaoManualParcelaVO.setValorTotalParcelasGerar(Uteis.arrendondarForcando2CadasDecimais((Double) resultado.get("VALOR")));
		}
		if(geracaoManualParcelaVO.getQtdeParcelaGerar() == null || geracaoManualParcelaVO.getQtdeParcelaGerar() == 0){
			throw new Exception(UteisJSF.internacionalizar("msg_GeracaoManualParcela_contaReceberNaoLocalizada"));
		}
	}

	@Override
	public void persistir(GeracaoManualParcelaVO geracaoManualParcelaVO, UsuarioVO usuarioVO) throws Exception {
		if (geracaoManualParcelaVO.isNovoObj()) {
			incluir(geracaoManualParcelaVO, usuarioVO);
		} else {
			alterar(geracaoManualParcelaVO, usuarioVO);
		}
	}

	private void incluir(final GeracaoManualParcelaVO geracaoManualParcelaVO, final UsuarioVO usuarioVO) throws Exception {
		try {			
			validarDados(geracaoManualParcelaVO);
			geracaoManualParcelaVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("insert into GeracaoManualParcela ");
					sql.append(" (unidadeEnsino, curso, turma, usuario, mesReferencia, anoReferencia, ");
					sql.append(" permitirGerarParcelaAlunoPreMatricula, qtdeParcelaGerar, qtdeParcelaComErro, qtdeParcelaGerada, ");
					sql.append(" dataInicioProcessamento, dataTerminoProcessamento, valorTotalParcelasGerar, valorTotalParcelasGerada, ");
					sql.append(" valorTotalParcelasNaoGerada, gerarTodasParcelas, situacaoProcessamentoGeracaoManualParcela, utilizarDataCompetencia, mensagemErro) ");
					sql.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?) ");
					sql.append(" returning codigo ");
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, geracaoManualParcelaVO.getUnidadeEnsino().getCodigo());
					if (geracaoManualParcelaVO.getCurso().getCodigo() != null && geracaoManualParcelaVO.getCurso().getCodigo() > 0) {
						ps.setInt(x++, geracaoManualParcelaVO.getCurso().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (geracaoManualParcelaVO.getTurma().getCodigo() != null && geracaoManualParcelaVO.getTurma().getCodigo() > 0) {
						ps.setInt(x++, geracaoManualParcelaVO.getTurma().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (geracaoManualParcelaVO.getUsuario().getCodigo() != null && geracaoManualParcelaVO.getUsuario().getCodigo() > 0) {
						ps.setInt(x++, geracaoManualParcelaVO.getUsuario().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, geracaoManualParcelaVO.getMesReferencia());
					ps.setString(x++, geracaoManualParcelaVO.getAnoReferencia());
					ps.setBoolean(x++, geracaoManualParcelaVO.getPermitirGerarParcelaAlunoPreMatricula());
					ps.setInt(x++, geracaoManualParcelaVO.getQtdeParcelaGerar());
					ps.setInt(x++, geracaoManualParcelaVO.getQtdeParcelaComErro());
					ps.setInt(x++, geracaoManualParcelaVO.getQtdeParcelaGerada());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(geracaoManualParcelaVO.getDataInicioProcessamento()));
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(geracaoManualParcelaVO.getDataTerminoProcessamento()));
					ps.setDouble(x++, geracaoManualParcelaVO.getValorTotalParcelasGerar());
					ps.setDouble(x++, geracaoManualParcelaVO.getValorTotalParcelasGerada());
					ps.setDouble(x++, geracaoManualParcelaVO.getValorTotalParcelasNaoGerada());
					ps.setBoolean(x++, geracaoManualParcelaVO.getGerarTodasParcelas());
					ps.setString(x++, geracaoManualParcelaVO.getSituacaoProcessamentoGeracaoManualParcela().name());
					ps.setBoolean(x++, geracaoManualParcelaVO.getUtilizarDataCompetencia());
					ps.setString(x++, geracaoManualParcelaVO.getMensagemErro());
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			geracaoManualParcelaVO.setNovoObj(false);
		} catch (Exception e) {
			geracaoManualParcelaVO.setCodigo(0);
			geracaoManualParcelaVO.setNovoObj(true);
			throw e;
		}
	}

	private void alterar(final GeracaoManualParcelaVO geracaoManualParcelaVO, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(geracaoManualParcelaVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					StringBuilder sql = new StringBuilder("UPDATE GeracaoManualParcela SET ");
					sql.append(" unidadeEnsino = ?, curso = ?, turma = ?, usuario = ?, mesReferencia = ?, anoReferencia = ?, ");
					sql.append(" permitirGerarParcelaAlunoPreMatricula = ?, qtdeParcelaGerar = ?, qtdeParcelaComErro = ?, qtdeParcelaGerada = ?, ");
					sql.append(" dataInicioProcessamento = ?, dataTerminoProcessamento = ?, valorTotalParcelasGerar = ?, valorTotalParcelasGerada = ?, ");
					sql.append(" valorTotalParcelasNaoGerada = ?, gerarTodasParcelas = ?, situacaoProcessamentoGeracaoManualParcela = ?, utilizarDataCompetencia = ?, mensagemErro = ? ");
					sql.append(" where codigo = ? ");					
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setInt(x++, geracaoManualParcelaVO.getUnidadeEnsino().getCodigo());
					if (geracaoManualParcelaVO.getCurso().getCodigo() != null && geracaoManualParcelaVO.getCurso().getCodigo() > 0) {
						ps.setInt(x++, geracaoManualParcelaVO.getCurso().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (geracaoManualParcelaVO.getTurma().getCodigo() != null && geracaoManualParcelaVO.getTurma().getCodigo() > 0) {
						ps.setInt(x++, geracaoManualParcelaVO.getTurma().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					if (geracaoManualParcelaVO.getUsuario().getCodigo() != null && geracaoManualParcelaVO.getUsuario().getCodigo() > 0) {
						ps.setInt(x++, geracaoManualParcelaVO.getUsuario().getCodigo());
					} else {
						ps.setNull(x++, 0);
					}
					ps.setString(x++, geracaoManualParcelaVO.getMesReferencia());
					ps.setString(x++, geracaoManualParcelaVO.getAnoReferencia());
					ps.setBoolean(x++, geracaoManualParcelaVO.getPermitirGerarParcelaAlunoPreMatricula());
					ps.setInt(x++, geracaoManualParcelaVO.getQtdeParcelaGerar());
					ps.setInt(x++, geracaoManualParcelaVO.getQtdeParcelaComErro());
					ps.setInt(x++, geracaoManualParcelaVO.getQtdeParcelaGerada());
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(geracaoManualParcelaVO.getDataInicioProcessamento()));
					ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(geracaoManualParcelaVO.getDataTerminoProcessamento()));
					ps.setDouble(x++, geracaoManualParcelaVO.getValorTotalParcelasGerar());
					ps.setDouble(x++, geracaoManualParcelaVO.getValorTotalParcelasGerada());
					ps.setDouble(x++, geracaoManualParcelaVO.getValorTotalParcelasNaoGerada());
					ps.setBoolean(x++, geracaoManualParcelaVO.getGerarTodasParcelas());
					ps.setString(x++, geracaoManualParcelaVO.getSituacaoProcessamentoGeracaoManualParcela().name());
					ps.setBoolean(x++, geracaoManualParcelaVO.getUtilizarDataCompetencia());
					ps.setString(x++, geracaoManualParcelaVO.getMensagemErro());
					ps.setInt(x++, geracaoManualParcelaVO.getCodigo());
					return ps;
				}
			});
		} catch (Exception e) {			
			throw e;
		}
	}

	@Override
	public List<GeracaoManualParcelaVO> consultar(Integer unidadeEnsino, String curso, String turma, Date dataInicio, Date dataTermino, SituacaoProcessamentoGeracaoManualParcelaEnum situacao, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		super.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("SELECT geracaomanualparcela.*, unidadeensino.nome as unidadeensino_nome, ");
		sql.append(" curso.nome as curso_nome, turma.identificadorturma as identificadorturma, usuario.nome as usuario_nome ");
		sql.append(" from geracaomanualparcela ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = geracaomanualparcela.unidadeensino ");
		sql.append(" left join curso on curso.codigo = geracaomanualparcela.curso ");
		sql.append(" left join turma on turma.codigo = geracaomanualparcela.turma ");
		sql.append(" left join usuario on usuario.codigo = geracaomanualparcela.usuario ");
		sql.append(" where 1=1 ");
		if(unidadeEnsino != null && unidadeEnsino > 0){
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if(curso != null && !curso.trim().isEmpty()){
			sql.append(" and sem_acentos(curso.nome) ilike (sem_acentos('").append(curso).append("%'))");
		}
		if(turma != null && !turma.trim().isEmpty()){
			sql.append(" and sem_acentos(turma.identificadorturma) ilike (sem_acentos('").append(turma).append("%'))");
		}		
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "geracaomanualparcela.dataInicioProcessamento", false));
		if(situacao != null && !situacao.equals(SituacaoProcessamentoGeracaoManualParcelaEnum.TODAS)){
			sql.append(" and geracaomanualparcela.situacaoProcessamentoGeracaoManualParcela = '").append(situacao.name()).append("'");
		}
		sql.append(" order by unidadeensino.nome, geracaomanualparcela.dataInicioProcessamento ");
		if(limit > 0){
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	private List<GeracaoManualParcelaVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<GeracaoManualParcelaVO> geracaoManualParcelaVOs = new ArrayList<GeracaoManualParcelaVO>(0);
		while(rs.next()){
			geracaoManualParcelaVOs.add(montarDados(rs));
		}
		return geracaoManualParcelaVOs;
	}
	
	private GeracaoManualParcelaVO montarDados(SqlRowSet rs) throws Exception{
		GeracaoManualParcelaVO geracaoManualParcelaVO = new GeracaoManualParcelaVO();
		geracaoManualParcelaVO.setNovoObj(false);
		geracaoManualParcelaVO.setCodigo(rs.getInt("codigo"));
		geracaoManualParcelaVO.setDataInicioProcessamento(rs.getDate("dataInicioProcessamento"));
		geracaoManualParcelaVO.setDataTerminoProcessamento(rs.getDate("dataTerminoProcessamento"));
		geracaoManualParcelaVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		geracaoManualParcelaVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino_nome"));
		geracaoManualParcelaVO.getCurso().setCodigo(rs.getInt("curso"));
		geracaoManualParcelaVO.getCurso().setNome(rs.getString("curso_nome"));
		geracaoManualParcelaVO.getUsuario().setCodigo(rs.getInt("usuario"));
		geracaoManualParcelaVO.getUsuario().setNome(rs.getString("usuario_nome"));
		geracaoManualParcelaVO.getTurma().setCodigo(rs.getInt("turma"));
		geracaoManualParcelaVO.getTurma().setIdentificadorTurma(rs.getString("identificadorTurma"));
		geracaoManualParcelaVO.setAnoReferencia(rs.getString("anoReferencia"));
		geracaoManualParcelaVO.setMesReferencia(rs.getString("mesReferencia"));
		geracaoManualParcelaVO.setMensagemErro(rs.getString("mensagemErro"));
		geracaoManualParcelaVO.setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.valueOf(rs.getString("situacaoProcessamentoGeracaoManualParcela")));
		geracaoManualParcelaVO.setPermitirGerarParcelaAlunoPreMatricula(rs.getBoolean("permitirGerarParcelaAlunoPreMatricula"));
		geracaoManualParcelaVO.setUtilizarDataCompetencia(rs.getBoolean("utilizarDataCompetencia"));
		geracaoManualParcelaVO.setGerarTodasParcelas(rs.getBoolean("gerarTodasParcelas"));
		geracaoManualParcelaVO.setQtdeParcelaComErro(rs.getInt("qtdeParcelaComErro"));
		geracaoManualParcelaVO.setQtdeParcelaGerada(rs.getInt("qtdeParcelaGerada"));
		geracaoManualParcelaVO.setQtdeParcelaGerar(rs.getInt("qtdeParcelaGerar"));
		geracaoManualParcelaVO.setValorTotalParcelasGerada(rs.getDouble("valorTotalParcelasGerada"));
		geracaoManualParcelaVO.setValorTotalParcelasGerar(rs.getDouble("valorTotalParcelasGerar"));
		geracaoManualParcelaVO.setValorTotalParcelasNaoGerada(rs.getDouble("valorTotalParcelasNaoGerada"));
		geracaoManualParcelaVO.setProgresso(geracaoManualParcelaVO.getParcelaAtual().longValue());
		geracaoManualParcelaVO.setMaxValue(rs.getInt("qtdeParcelaGerada"));
		geracaoManualParcelaVO.setStatus(geracaoManualParcelaVO.getLabelProcessamento());
		if(geracaoManualParcelaVO.getSituacaoProcessamentoGeracaoManualParcela().equals(SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO)){
			geracaoManualParcelaVO.iniciar(Long.valueOf(geracaoManualParcelaVO.getParcelaAtual()), geracaoManualParcelaVO.getQtdeParcelaGerar(), geracaoManualParcelaVO.getLabelProcessamento(), false, null, null);
		}
		return geracaoManualParcelaVO;
	}

	@Override
	public Integer consultarTotalRegistro(Integer unidadeEnsino, String curso, String turma, Date dataInicio, Date dataTermino, SituacaoProcessamentoGeracaoManualParcelaEnum situacao) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(geracaomanualparcela.codigo) as qtde ");
		sql.append(" from geracaomanualparcela ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = geracaomanualparcela.unidadeensino ");
		sql.append(" left join curso on curso.codigo = geracaomanualparcela.curso ");
		sql.append(" left join turma on turma.codigo = geracaomanualparcela.turma ");
		sql.append(" left join usuario on usuario.codigo = geracaomanualparcela.usuario ");
		sql.append(" where 1=1 ");
		if(unidadeEnsino != null && unidadeEnsino > 0){
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if(curso != null && !curso.trim().isEmpty()){
			sql.append(" and sem_acentos(curso.nome) ilike (sem_acentos('").append(curso).append("%'))");
		}
		if(turma != null && !turma.trim().isEmpty()){
			sql.append(" and sem_acentos(turma.identificadorturma) ilike (sem_acentos('").append(turma).append("%'))");
		}		
		sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "geracaomanualparcela.dataInicioProcessamento", false));
		if(situacao != null && !situacao.equals(SituacaoProcessamentoGeracaoManualParcelaEnum.TODAS)){
			sql.append(" and geracaomanualparcela.situacaoProcessamentoGeracaoManualParcela = '").append(situacao.name()).append("'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public void validarDados(GeracaoManualParcelaVO geracaoManualParcelaVO) throws Exception {
		if (geracaoManualParcelaVO.getUnidadeEnsino().getCodigo().equals(0)) {
			throw new Exception(UteisJSF.internacionalizar("msg_GeracaoManualParcela_unidadeEnsino"));
		}
		if (!geracaoManualParcelaVO.getGerarTodasParcelas()) {
			if (geracaoManualParcelaVO.getMesReferencia().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_GeracaoManualParcela_mesReferencia"));				
			}
			if (geracaoManualParcelaVO.getAnoReferencia().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_GeracaoManualParcela_anoReferencia"));
			}
		}else{			
			geracaoManualParcelaVO.setMesReferencia("");
			geracaoManualParcelaVO.setAnoReferencia("");
			geracaoManualParcelaVO.setUtilizarDataCompetencia(false);			
		}

	}

	@Override
	public GeracaoManualParcelaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT geracaomanualparcela.*, unidadeensino.nome as unidadeensino_nome, ");
		sql.append(" curso.nome as curso_nome, turma.identificadorturma as identificadorturma, usuario.nome as usuario_nome ");
		sql.append(" from geracaomanualparcela ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = geracaomanualparcela.unidadeensino ");
		sql.append(" left join curso on curso.codigo = geracaomanualparcela.curso ");
		sql.append(" left join turma on turma.codigo = geracaomanualparcela.turma ");
		sql.append(" left join usuario on usuario.codigo = geracaomanualparcela.usuario ");
		sql.append(" where geracaomanualparcela.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return montarDados(rs);
		}
		throw new Exception("Dados não encontrados (Geração Manual Parcelas)");
	}
	
	@Override
	public void realizarReinicializacaoGerarManualParcelas(AplicacaoControle aplicacaoControle) throws Exception {
		List<GeracaoManualParcelaVO> geracaoManualParcelaVOs = getFacadeFactory().getGeracaoManualParcelaFacade().consultar(0, "", "", null, null, SituacaoProcessamentoGeracaoManualParcelaEnum.EM_PROCESSAMENTO, false, null, 0, 0);
		for(GeracaoManualParcelaVO geracaoManualParcelaVO: geracaoManualParcelaVOs){		
			try {
				realizarGerarParcelas(geracaoManualParcelaVO, aplicacaoControle);
			} catch (Exception e) {
				geracaoManualParcelaVO.setDataTerminoProcessamento(new Date());
				geracaoManualParcelaVO.setSituacaoProcessamentoGeracaoManualParcela(SituacaoProcessamentoGeracaoManualParcelaEnum.ERRO_PROCESSAMENTO);
				try {
					persistir(geracaoManualParcelaVO, null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	@Override
	public void realizarGerarParcelas(GeracaoManualParcelaVO geracaoManualParcelaVO, AplicacaoControle aplicacaoControle) throws Exception {        	        
	        	validarDados(geracaoManualParcelaVO);	        	
	        	aplicacaoControle.realizarGeracaoManualParcela(geracaoManualParcelaVO.getUnidadeEnsino().getCodigo(), true, false);
	        	Thread jobGeracaoManualParcela = new Thread(new JobGeracaoManualParcela(geracaoManualParcelaVO, aplicacaoControle), "GeracaoManualParcela_Unidade_"+geracaoManualParcelaVO.getUnidadeEnsino().getCodigo());
	        	jobGeracaoManualParcela.start();          		        	
	 }

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "GeracaoManualParcelas";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		GeracaoManualParcela.idEntidade = idEntidade;
	}
	
	

}
