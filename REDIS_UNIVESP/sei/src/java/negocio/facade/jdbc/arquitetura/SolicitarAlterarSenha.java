package negocio.facade.jdbc.arquitetura;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.SolicitarAlterarSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ContatoParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.interfaces.arquitetura.SolicitarAlterarSenhaInterfaceFacede;

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

import negocio.comuns.arquitetura.SolicitarAlterarSenhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoUsuario;
import negocio.interfaces.arquitetura.SolicitarAlterarSenhaInterfaceFacede;

/**
 * 
 * @author Leonardo Riciolle
 * @date 18/02/2015 Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>SolicitarAlterarSenhaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>SolicitarAlterarSenhaVO</code>. Encapsula toda a interação com o banco de dados
 * @see SolicitarAlterarSenhaVO
 */
@Repository
@Scope("singleton")
@Lazy
public class SolicitarAlterarSenha extends ControleAcesso implements SolicitarAlterarSenhaInterfaceFacede {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public SolicitarAlterarSenha() throws Exception {
		super();
		setIdEntidade("SolicitarAlterarSenha");
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		SolicitarAlterarSenha.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>SolicitarAlterarSenhaVO</code>.
	 */
	public SolicitarAlterarSenhaVO novo() throws Exception {
		SolicitarAlterarSenha.incluir(getIdEntidade());
		SolicitarAlterarSenhaVO solicitarAlterarSenhaVO = new SolicitarAlterarSenhaVO();
		return solicitarAlterarSenhaVO;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final SolicitarAlterarSenhaVO solicitarAlterarSenhaVO, final Boolean verificarAcesso, final UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		validarDados(solicitarAlterarSenhaVO);
		if (solicitarAlterarSenhaVO.getNovoObj()) {
			incluir(solicitarAlterarSenhaVO, verificarAcesso, usuarioVO, configuracaoGeralSistemaVO);
		} else {
			alterar(solicitarAlterarSenhaVO, verificarAcesso, usuarioVO);
		}
	}

	public static void validarDados(SolicitarAlterarSenhaVO solicitarAlterarSenhaVO) throws Exception {
		if (solicitarAlterarSenhaVO.getTipoUsuario().equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_SolicitarAlterarSenha_TipoUsuario"));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final SolicitarAlterarSenhaVO obj, final Boolean verificarAcesso, final UsuarioVO usuarioVO, final ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			SolicitarAlterarSenha.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO solicitaralterarsenha(tipousuario, unidadeensino, curso, departamento, datasolicitacao, responsavel, mensagemorientacao, enviaremail, assuntoemail, mensagememail,solicitarnovasenha) ");
			sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						sqlInserir.setString(1, obj.getTipoUsuario().toString());
						if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
							sqlInserir.setInt(2, obj.getUnidadeEnsinoVO().getCodigo());
						}else{
							sqlInserir.setNull(2, 0);
						}
						if (obj.getCursoVO().getCodigo().intValue() != 0) {
							sqlInserir.setInt(3, obj.getCursoVO().getCodigo());
						} else {
							sqlInserir.setNull(3, 0);
						}
						if (obj.getDepartamentoVO().getCodigo().intValue() != 0) {
							sqlInserir.setInt(4, obj.getDepartamentoVO().getCodigo());
						} else {
							sqlInserir.setNull(4, 0);
						}
						sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataSolicitacao()));
						sqlInserir.setInt(6, obj.getResponsavel().getCodigo());
						sqlInserir.setString(7, obj.getMensagemOrientacao());
						sqlInserir.setBoolean(8, obj.getEnviarEmail());
						sqlInserir.setString(9, obj.getAssuntoEmail());
						sqlInserir.setString(10, obj.getMensagemEmail());
						sqlInserir.setBoolean(11, true);
					} catch (final Exception x) {
						return null;
					}
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
			
			//List<UsuarioVO>  listaUsuario = new ArrayList<UsuarioVO>(0);			
			//listaUsuario = consultarUsuarioParaSolicitarNovaSenha(obj);
			
			//Metodo responsavel por setar TRUE na variavel SolicitarNovaSenha em UsuarioVO
			/*if(Uteis.isAtributoPreenchido(listaUsuario)) {
				for (UsuarioVO usuarioVOAlterarSenha : listaUsuario) {
					getFacadeFactory().getUsuarioFacade().alterarBooleanoSolicitarNovaSenha(obj.getSolicitarNovaSenha(), usuarioVOAlterarSenha, verificarAcesso, usuarioVO);
				}
			}
			if(Uteis.isAtributoPreenchido(listaUsuario) && obj.getEnviarEmail()) {
				enviarComunicacaoUsuario(obj, listaUsuario, usuarioVO, configuracaoGeralSistemaVO);
			}*/
			//getFacadeFactory().getUsuarioFacade().alterarBooleanoSolicitarNovaSenha(obj, verificarAcesso, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final SolicitarAlterarSenhaVO obj, final Boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			SolicitarAlterarSenha.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE solicitaralterarsenha  SET tipousuario=?, unidadeensino=?, curso=?, departamento=?, datasolicitacao=?, responsavel=?, mensagemorientacao=?, enviaremail=?, ");
			sql.append(" assuntoemail=?, mensagememail=? WHERE codigo = ?  " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getTipoUsuario().toString());
					sqlAlterar.setInt(2, obj.getUnidadeEnsinoVO().getCodigo());
					if (obj.getCursoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (obj.getDepartamentoVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(4, obj.getDepartamentoVO().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataSolicitacao()));
					sqlAlterar.setInt(6, obj.getResponsavel().getCodigo());
					sqlAlterar.setString(7, obj.getMensagemOrientacao());
					sqlAlterar.setBoolean(8, obj.getEnviarEmail());
					sqlAlterar.setString(9, obj.getAssuntoEmail());
					sqlAlterar.setString(10, obj.getMensagemEmail());
					sqlAlterar.setInt(11, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Integer consultarTotal(Date dataInicial, Date dataFinal, String campoConsulta, String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO, SolicitarAlterarSenhaVO solicitarAlterarSenhaVO) throws Exception {
		Integer count = 0;
		if (campoConsulta.equals("data")) {
			return count = consultarTotalSolicitarSenhaPorDataSolicitacao(dataInicial, dataFinal, verificarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("responsavel")) {
			return count = consultarTotalSolicitarSenhaPorResponsavel(valorConsulta, verificarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("tipoUsuario")) {
			if (valorConsulta.equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
			}
			return count = consultarTotalSolicitarSenhaPorTipoUsuario(valorConsulta, verificarAcesso, usuarioVO);
		}
		return count;

	}

	private Integer consultarTotalSolicitarSenhaPorTipoUsuario(String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT (solicitaralterarsenha.codigo) FROM solicitaralterarsenha WHERE tipousuario ilike (?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta });
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}

	private Integer consultarTotalSolicitarSenhaPorResponsavel(String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT (solicitaralterarsenha.codigo) FROM solicitaralterarsenha inner join usuario on usuario.codigo = solicitaralterarsenha.responsavel WHERE sem_acentos(usuario.nome) ilike (sem_acentos(?)) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta });
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}

	private Integer consultarTotalSolicitarSenhaPorDataSolicitacao(Date dataInicial, Date dataFinal, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT (solicitaralterarsenha.codigo) FROM solicitaralterarsenha WHERE ((datasolicitacao >= ?) and (datasolicitacao <= ?)) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal) });
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}

	@Override
	public List<SolicitarAlterarSenhaVO> consultar(Date dataInicial, Date dataFinal, String valorConsulta, String campoConsulta, Integer limite, Integer offset, boolean verificarAcesso, UsuarioVO usuarioVO, SolicitarAlterarSenhaVO solicitarAlterarSenhaVO, int nivelMontarDados) throws Exception {
		List<SolicitarAlterarSenhaVO> objs = new ArrayList<SolicitarAlterarSenhaVO>();
		validarDadosConsulta(valorConsulta, campoConsulta);
		if (campoConsulta.equals("data")) {
			return objs = consultarSolicitarSenhaPorDataSolicitacao(dataInicial, dataFinal, limite, offset, verificarAcesso, usuarioVO, nivelMontarDados);
		}
		if (campoConsulta.equals("responsavel")) {

			return objs = consultarSolicitarSenhaPorResponsavel(valorConsulta, verificarAcesso, limite, offset, usuarioVO, nivelMontarDados);
		}
		if (campoConsulta.equals("tipoUsuario")) {
			return objs = consultarSolicitarSenhaPorTipoUsuario(valorConsulta, verificarAcesso, limite, offset, usuarioVO, nivelMontarDados);
		}
		return objs;
	}

	public void validarDadosConsulta(String valorConsulta, String campoConsulta) throws Exception {
		if (campoConsulta.equals("responsavel") && valorConsulta.equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		else if(campoConsulta.equals("tipoUsuario") && valorConsulta.equals("")) {
			throw new Exception("Informe o tipo de usuário!");
		}
	}

	@Override
	public List<SolicitarAlterarSenhaVO> consultarSolicitarSenhaPorDataSolicitacao(Date dataInicial, Date dataFinal, Integer limite, Integer offset, boolean verificarAcesso, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM solicitaralterarsenha WHERE ((datasolicitacao >= ?) and (datasolicitacao <= ?)) ");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal) });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<SolicitarAlterarSenhaVO> consultarSolicitarSenhaPorTipoUsuario(String valorConsulta, boolean verificarAcesso, Integer limite, Integer offset, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM solicitaralterarsenha WHERE tipousuario ilike (?) order by responsavel ");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<SolicitarAlterarSenhaVO> consultarSolicitarSenhaPorResponsavel(String valorConsulta, boolean verificarAcesso, Integer limite, Integer offset, UsuarioVO usuarioVO, int nivelMontarDados) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT solicitaralterarsenha.* FROM solicitaralterarsenha inner join usuario on usuario.codigo = solicitaralterarsenha.responsavel WHERE sem_acentos(usuario.nome) ilike sem_acentos(?) order by usuario.nome, solicitaralterarsenha.datasolicitacao ");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { valorConsulta });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(SolicitarAlterarSenhaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			SolicitarAlterarSenha.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM solicitaralterarsenha WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
			
			
			//Metodo responsavel por setar FALSE no Booleano SolicitarNovaSenha em usuario.
			//Setando false todos os usuario que ainda não alteraram a senha não ira precisar alterar e os que já alteraram não sofrera modificação
			/*if(Uteis.isAtributoPreenchido(listaUsuario)) {
				for (UsuarioVO usuarioVOAlterarSenha : listaUsuario) {
					getFacadeFactory().getUsuarioFacade().alterarBooleanoSolicitarNovaSenha(false, usuarioVOAlterarSenha, verificarAcesso, usuarioVO);
				}
			}*/

			//getFacadeFactory().getUsuarioFacade().alterarBooleanoSolicitarNovaSenha(obj, true, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public SolicitarAlterarSenhaVO consultarSolicitarAlterarSenhaPorCodigo(SolicitarAlterarSenhaVO solicitarAlterarSenhaVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT solicitarAlterarSenha.* from solicitarAlterarSenha where codigo = ").append(solicitarAlterarSenhaVO.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		return new SolicitarAlterarSenhaVO();
	}

	public List<SolicitarAlterarSenhaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<SolicitarAlterarSenhaVO> solicitarAlterarSenhaVOs = new ArrayList<SolicitarAlterarSenhaVO>(0);
		while (tabelaResultado.next()) {
			solicitarAlterarSenhaVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return solicitarAlterarSenhaVOs;
	}

	public SolicitarAlterarSenhaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		SolicitarAlterarSenhaVO obj = new SolicitarAlterarSenhaVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipousuario"))) {
			obj.setTipoUsuario(TipoUsuario.valueOf(tabelaResultado.getString("tipousuario")));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.getResponsavel().setCodigo(tabelaResultado.getInt("responsavel"));
		obj.setDataSolicitacao(tabelaResultado.getDate("datasolicitacao"));
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino"));
		obj.getCursoVO().setCodigo(tabelaResultado.getInt("curso"));
		obj.getDepartamentoVO().setCodigo(tabelaResultado.getInt("departamento"));
		obj.setMensagemOrientacao(tabelaResultado.getString("mensagemorientacao"));
		obj.setEnviarEmail(tabelaResultado.getBoolean("enviaremail"));
		obj.setAssuntoEmail(tabelaResultado.getString("assuntoemail"));
		obj.setMensagemEmail(tabelaResultado.getString("mensagememail"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
				obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			}		
			if (!obj.getCursoVO().getCodigo().equals(0)) {
				obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioVO));
			}
			if (!obj.getDepartamentoVO().getCodigo().equals(0)) {
				obj.setDepartamentoVO(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(obj.getDepartamentoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
			}
			return obj;
		}
		return obj;
	}
	
	
	/*public List<UsuarioVO> consultarUsuarioParaSolicitarNovaSenha(SolicitarAlterarSenhaVO obj) throws Exception{
		List<UsuarioVO>  listaUsuario = new ArrayList<UsuarioVO>(0);
		
		if(obj.getTipoUsuario().equals(TipoUsuario.ALUNO)) {
			listaUsuario = getFacadeFactory().getUsuarioFacade().consultarUsuarioAlunoPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo());
}
		else if(obj.getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
			listaUsuario = getFacadeFactory().getUsuarioFacade().consultarUsuarioProfessorPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo());
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.COORDENADOR)) {
			listaUsuario = getFacadeFactory().getUsuarioFacade().consultarUsuarioCoordenadorPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo());
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO)) {
			listaUsuario = getFacadeFactory().getUsuarioFacade().consultarUsuarioFuncionarioPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getDepartamentoVO().getCodigo());
		}
		return listaUsuario;
	}*/
	
	public void enviarComunicacaoUsuario(SolicitarAlterarSenhaVO obj, UsuarioVO usuarioDestinatario, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		ComunicacaoInternaVO com = getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().inicializarDadosPadrao(new ComunicacaoInternaVO());
		com.setAssunto(obj.getAssuntoEmail());
		com.setData(new Date());
		com.setMensagem(obj.getMensagemEmail());
		com.setResponsavel(obj.getResponsavel().getPessoa());		
		com.setTipoDestinatario(obj.getTipoUsuario().getValor());
		com.setComunicadoInternoDestinatarioVOs(getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().obterListaDestinatarios(usuarioDestinatario.getPessoa(), false));
		com.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.SOLICITAR_ALTERAR_SENHA);
		com.setCodigoTipoOrigemComunicacaoInterna(obj.getCodigo());
		getFacadeFactory().getComunicacaoInternaFacade().incluir(com, false, usuarioVO, configuracaoGeralSistemaVO, null);
	}
	
	public ProgressBarVO consultarProgressBarAtivo() {
		if(getAplicacaoControle().getMapThreadIndiceReajuste().containsKey("SolicitarAlteracaoSenha")){
			ProgressBarVO progressBarVO = getAplicacaoControle().getMapThreadIndiceReajuste().get("SolicitarAlteracaoSenha");
			return progressBarVO.getAtivado() ? progressBarVO : null;
		}
		return null;
	}
	
	public void realizarSolicitacaoNovaSenhaUsuario(final ProgressBarVO progressBarVO, SolicitarAlterarSenhaVO obj, Integer qtdeTotalUsuarioSolicitarNovaSenha, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		getAplicacaoControle().getMapThreadIndiceReajuste().put("SolicitarAlteracaoSenha", progressBarVO);
		getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().persistir(obj, true, usuarioVO, configuracaoGeralSistemaVO);
		final List<UsuarioVO> listaUsuarioAlterarSenha = new ArrayList<UsuarioVO>(0);
		BigDecimal qtdeLotesProcessar = new BigDecimal(qtdeTotalUsuarioSolicitarNovaSenha).divide(BigDecimal.valueOf(1000L));
		Integer qtdeLotesProcessarFinal = qtdeTotalUsuarioSolicitarNovaSenha < 1000 ? 1 : Uteis.arredondarParaMais(qtdeLotesProcessar.doubleValue());
		
		progressBarVO.setStatus("Consultando Usuários para alteração de senha");
		StringBuilder sqlStr = new StringBuilder();
		if(obj.getTipoUsuario().equals(TipoUsuario.ALUNO)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioAlunoPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), false));
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioProfessorPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), false));
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.COORDENADOR)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioCoordenadorPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), false));
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioFuncionarioPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getDepartamentoVO().getCodigo(), false));
		}
		
		SqlRowSet dadosSQL = null;
		try {
			dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (dadosSQL.next()) {
				do {
					UsuarioVO usuarioAlterarSenha = new UsuarioVO();
					try {
						usuarioAlterarSenha.setCodigo(dadosSQL.getInt("usuario.codigo"));
						usuarioAlterarSenha.setUsername(dadosSQL.getString("usuario.username"));
						usuarioAlterarSenha.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
						usuarioAlterarSenha.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
						usuarioAlterarSenha.getPessoa().setEmail(dadosSQL.getString("pessoa.email"));
						listaUsuarioAlterarSenha.add(usuarioAlterarSenha);
					}catch (Exception e) {
						throw e;
					}
				}while (dadosSQL.next());
				
				final ConsistirException consistirException = new ConsistirException();
				ProcessarParalelismo.executar(0, listaUsuarioAlterarSenha.size(), consistirException, new ProcessarParalelismo.Processo() {
					int cont = 1;
					@Override
					public void run(int i) {
						//progressBarVO.setStatus("Processando " + cont + "/" + qtdeLotesProcessarFinal + "lote(s)");
						UsuarioVO objUsuario = listaUsuarioAlterarSenha.get(i);
						try {
							getFacadeFactory().getUsuarioFacade().alterarBooleanoSolicitarNovaSenha(obj.getSolicitarNovaSenha(), objUsuario, false, usuarioVO);
							if(obj.getEnviarEmail()) {
								enviarComunicacaoUsuario(obj, objUsuario, usuarioVO, configuracaoGeralSistemaVO);
							}
						} catch (Exception e) {
							consistirException.adicionarListaMensagemErro(e.getMessage());
						}finally {
							progressBarVO.incrementar();									
						}
						//cont++;
					}
				});

				dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			}
			}catch (Exception e) {
				throw e;
		}
	}
	
	public void realizarExclusaoSolicitacaoNovaSenhaUsuario(final ProgressBarVO progressBarVO, SolicitarAlterarSenhaVO obj, Integer qtdeTotalUsuarioSolicitarNovaSenha, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		getAplicacaoControle().getMapThreadIndiceReajuste().put("SolicitarAlteracaoSenha", progressBarVO);		
		final List<UsuarioVO> listaUsuarioAlterarSenha = new ArrayList<UsuarioVO>(0);
		//BigDecimal qtdeLotesProcessar = new BigDecimal(qtdeTotalUsuarioSolicitarNovaSenha).divide(BigDecimal.valueOf(1000L));
		//Integer qtdeLotesProcessarFinal = qtdeTotalUsuarioSolicitarNovaSenha < 1000 ? 1 : Uteis.arredondarParaMais(qtdeLotesProcessar.doubleValue());
		
		progressBarVO.setStatus("Consultando Usuários para alteração de senha");
		StringBuilder sqlStr = new StringBuilder();
		if(obj.getTipoUsuario().equals(TipoUsuario.ALUNO)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioAlunoPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), true));
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.PROFESSOR)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioProfessorPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), true));
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.COORDENADOR)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioCoordenadorPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getCursoVO().getCodigo(), true));
		}
		else if(obj.getTipoUsuario().equals(TipoUsuario.FUNCIONARIO)) {
			sqlStr.append(getFacadeFactory().getUsuarioFacade().consultarUsuarioFuncionarioPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoVO().getCodigo(), obj.getDepartamentoVO().getCodigo(), true));
		}
					
		
		SqlRowSet dadosSQL = null;
		try {
			dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			while (dadosSQL.next()) {
				do {
					UsuarioVO usuarioAlterarSenha = new UsuarioVO();
					try {
						usuarioAlterarSenha.setCodigo(dadosSQL.getInt("usuario.codigo"));
						usuarioAlterarSenha.setUsername(dadosSQL.getString("usuario.username"));
						usuarioAlterarSenha.getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
						usuarioAlterarSenha.getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
						usuarioAlterarSenha.getPessoa().setEmail(dadosSQL.getString("pessoa.email"));
						listaUsuarioAlterarSenha.add(usuarioAlterarSenha);
					}catch (Exception e) {
						throw e;
					}
				}while (dadosSQL.next());
				
				final ConsistirException consistirException = new ConsistirException();
				ProcessarParalelismo.executar(0, listaUsuarioAlterarSenha.size(), consistirException, new ProcessarParalelismo.Processo() {
					int cont = 1;
					@Override
					public void run(int i) {
						//progressBarVO.setStatus("Processando " + cont + "/" + qtdeLotesProcessarFinal + "lote(s)");
						UsuarioVO objUsuario = listaUsuarioAlterarSenha.get(i);
						try {
							if(obj.getEnviarEmail()) {	
								List<ComunicacaoInternaVO> listaComunicacaoInternaVOs = new ArrayList<ComunicacaoInternaVO>(0);
								listaComunicacaoInternaVOs = getFacadeFactory().getComunicacaoInternaFacade().consultarPorTipoOrigemComunicacaoInterna(obj.getCodigo(), TipoOrigemComunicacaoInternaEnum.SOLICITAR_ALTERAR_SENHA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
								
								for (ComunicacaoInternaVO comunicacaoInternaVO : listaComunicacaoInternaVOs) {
									List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVOs = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
									listaComunicadoInternoDestinatarioVOs.addAll(getFacadeFactory().getComunicadoInternoDestinatarioFacade().consultarPorCodigoComunicacaoInterna(comunicacaoInternaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, null, null));
									for (ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO : listaComunicadoInternoDestinatarioVOs) {
										getFacadeFactory().getEmailFacade().excluirPorEmailDestinarioEassunto(comunicadoInternoDestinatarioVO.getDestinatario().getEmail(), obj.getAssuntoEmail(), usuarioVO);
									}
								}
											
								getFacadeFactory().getComunicacaoInternaFacade().excluirPorTipoOrigemComunicacaoInterna(obj.getCodigo(), TipoOrigemComunicacaoInternaEnum.SOLICITAR_ALTERAR_SENHA, usuarioVO);
								
							}
							getFacadeFactory().getUsuarioFacade().alterarBooleanoSolicitarNovaSenha(obj.getSolicitarNovaSenha(), objUsuario, false, usuarioVO);
						} catch (Exception e) {
							consistirException.adicionarListaMensagemErro(e.getMessage());
						}finally {
							progressBarVO.incrementar();									
						}
						//cont++;
					}
				});				

				dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			}
			getFacadeFactory().getSolicitarAlterarSenhaInterfaceFacede().excluir(obj, true, usuarioVO);
			}catch (Exception e) {
				throw e;
		}
	}
}
