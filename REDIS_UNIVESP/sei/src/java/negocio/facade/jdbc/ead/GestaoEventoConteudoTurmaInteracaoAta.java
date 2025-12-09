package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaInteracaoAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.FuncaoResponsavelAtaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class GestaoEventoConteudoTurmaInteracaoAta extends ControleAcesso implements GestaoEventoConteudoTurmaInteracaoAtaInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8641466408082819880L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		GestaoEventoConteudoTurmaInteracaoAta.idEntidade = idEntidade;
	}

	public GestaoEventoConteudoTurmaInteracaoAta() throws Exception {
		super();
		setIdEntidade("GestaoEventoConteudoTurmaInteracaoAta");
	}
	
	public void validarDados(GestaoEventoConteudoTurmaInteracaoAtaVO obj) throws Exception {
		
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void validarDadosVisaoAluno(GestaoEventoConteudoTurmaVO turma, boolean visaoProfessor, UsuarioVO usuario) throws Exception {
		Boolean isUsuarioRedator = getFacadeFactory().getGestaoEventoConteudoTurmaResponsavelAtaFacade().consultarSeUsuarioResponsavelFuncaoAta(turma.getCodigo(), FuncaoResponsavelAtaEnum.REDATOR, usuario);
		if (!visaoProfessor && !isUsuarioRedator) {
			throw new Exception(UteisJSF.internacionalizar("msg_GestaoEventoConteudo_usuarioRedatorAta"));
		}	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGestaoEventoConteudoTurmaInteracaoAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata, boolean visaoProfessor,  UsuarioVO usuario) throws Exception {
		validarDadosVisaoAluno(turma, visaoProfessor, usuario);
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().removerGestaoEventoConteudoTurmaInteracaoAtaVO(turma, ata);
		StringBuilder sb = new StringBuilder("DELETE FROM gestaoeventoconteudoturmainteracaoata where codigo =  ").append(ata.getCodigo()).append(" ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().execute(sb.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirGestaoEventoConteudoTurmaInteracaoAtaVisaoAluno(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata, boolean visaoProfessor, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		validarDadosVisaoAluno(turma, visaoProfessor, usuario);
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().adicionarGestaoEventoConteudoTurmaInteracaoAtaVO(turma, ata, usuario);
		persistir(ata, verificarAcesso, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<GestaoEventoConteudoTurmaInteracaoAtaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (GestaoEventoConteudoTurmaInteracaoAtaVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);	
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void persistir(GestaoEventoConteudoTurmaInteracaoAtaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}

	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GestaoEventoConteudoTurmaInteracaoAtaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			GestaoEventoConteudoTurmaResponsavelAta.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE gestaoeventoconteudoturmainteracaoata");
			sql.append(" SET gestaoeventoconteudoturma=?, pessoa=?, tipoPessoa=?, interacao=?, dataInteracao=? ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getGestaoEventoConteudoTurmaVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getPessoaVO())){
						sqlAlterar.setInt(2, obj.getPessoaVO().getCodigo());	
					}else{
						sqlAlterar.setNull(2,0);
					}
					sqlAlterar.setString(3, obj.getTipoPessoa().name());
					sqlAlterar.setString(4, obj.getInteracao());
					sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataInteracao()));
					sqlAlterar.setInt(6, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuarioVO);
			};
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final GestaoEventoConteudoTurmaInteracaoAtaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			GestaoEventoConteudoTurmaResponsavelAta.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO gestaoeventoconteudoturmainteracaoata(gestaoeventoconteudoturma, pessoa, tipoPessoa, interacao, dataInteracao) ");
			sql.append(" VALUES (?, ?, ?, ?, ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getGestaoEventoConteudoTurmaVO().getCodigo());
					if(Uteis.isAtributoPreenchido(obj.getPessoaVO())){
						sqlInserir.setInt(2, obj.getPessoaVO().getCodigo());	
					}else{
						sqlInserir.setNull(2,0);
					}
					sqlInserir.setString(3, obj.getTipoPessoa().name());
					sqlInserir.setString(4, obj.getInteracao());
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataInteracao()));
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudoTurmaResponsavel() {
		StringBuilder sb = new StringBuilder("SELECT gectia.codigo as \"gectia.codigo\",  ");
		sb.append(" gectia.gestaoeventoconteudoturma as \"gectia.gestaoeventoconteudoturma\",  gectia.tipoPessoa as \"gectia.tipoPessoa\", ");
		sb.append(" gectia.interacao as \"gectia.interacao\",  gectia.dataInteracao as \"gectia.dataInteracao\", ");
		sb.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\"");
		sb.append(" from gestaoeventoconteudoturmainteracaoata as gectia      ");
		sb.append(" left join pessoa on pessoa.codigo = gectia.pessoa ");
		return sb;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaInteracaoAtaVO> consultarPorCodigoGestaoEventoConteudoTurmaVO(Integer codigoGestaoEventoConteudoTurmaVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudoTurmaResponsavel();
		sb.append(" WHERE gestaoeventoconteudoturma = ?");
		sb.append(" order by  gectia.dataInteracao ");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), codigoGestaoEventoConteudoTurmaVO), nivelMontarDados, usuarioLogado));
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaInteracaoAtaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<GestaoEventoConteudoTurmaInteracaoAtaVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaInteracaoAtaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaInteracaoAtaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		GestaoEventoConteudoTurmaInteracaoAtaVO obj = new GestaoEventoConteudoTurmaInteracaoAtaVO();
		obj.setCodigo(rs.getInt("gectia.codigo"));
		obj.getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("gectia.gestaoeventoconteudoturma"));
		obj.setInteracao(rs.getString("gectia.Interacao"));
		obj.setDataInteracao(rs.getDate("gectia.dataInteracao"));
		
		obj.setNovoObj(false);
		return obj;
	}

}
