package negocio.facade.jdbc.ead;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.richfaces.event.FileUploadEvent;
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

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.ead.AtividadeDiscursivaInteracaoVO;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.ArtefatoEntregaEnum;
import negocio.comuns.ead.enumeradores.DefinicaoDataEntregaAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.InteragidoPorEnum;
import negocio.comuns.ead.enumeradores.PublicoAlvoAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.SituacaoRespostaAtividadeDiscursivaEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AtividadeDiscursivaRespostaAlunoInterfaceFacade;


/*
 * @author Victor Hugo 17/09/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AtividadeDiscursivaRespostaAluno extends ControleAcesso implements AtividadeDiscursivaRespostaAlunoInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6579734858398339572L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AnotacaoDisciplina.idEntidade = idEntidade;
	}

	public AtividadeDiscursivaRespostaAluno() throws Exception {
		super();
		setIdEntidade("AtividadeDiscursivaRespostaAluno");		               
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AtividadeDiscursivaRespostaAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			AtividadeDiscursivaRespostaAluno.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO atividadediscursivarespostaaluno (resposta, arquivo, " + "NomeArquivoApresentar, dataupload, situacaorespostaatividadediscursiva, pastabasearquivo, nota , " + "matriculaperiodoturmadisciplina, atividadediscursiva, notalancadanohistorico, dataInicioAtividade, dataLimiteEntrega,dataPrimeiraNotificacao,dataSegundaNotificacao,dataTerceiraNotificacao,dataNotificacaoPrazoExecucao, tipoNota, utilizaNotaConceito,notaConceito) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setString(1, obj.getResposta());
						sqlInserir.setString(2, obj.getArquivo());
						sqlInserir.setString(3, obj.getNomeArquivoApresentar());
						if(Uteis.isAtributoPreenchido(obj.getDataUpload())){
							sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataUpload()));	
						}else{
							sqlInserir.setNull(4, 0);
						}
						sqlInserir.setString(5, obj.getSituacaoRespostaAtividadeDiscursiva().toString());
						sqlInserir.setString(6, obj.getPastaBaseArquivo().toString());
						if(obj.getNota() != null){
							sqlInserir.setDouble(7, obj.getNota());
						}else{
							sqlInserir.setNull(7, 0);
						}
						sqlInserir.setInt(8, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
						sqlInserir.setInt(9, obj.getAtividadeDiscursivaVO().getCodigo());
						sqlInserir.setBoolean(10, obj.getNotaLancadaNoHistorico());
						if(Uteis.isAtributoPreenchido(obj.getDataInicioAtividade())){
							sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataInicioAtividade()));
						}else{
							sqlInserir.setNull(11, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getDataLimiteEntrega())){
							sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(obj.getDataLimiteEntrega()));
						}else{
							sqlInserir.setNull(12, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getDataPrimeiraNotificacao())){
							sqlInserir.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataPrimeiraNotificacao()));
						}else{
							sqlInserir.setNull(13, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getDataSegundaNotificacao())){
							sqlInserir.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataSegundaNotificacao()));
						}else{
							sqlInserir.setNull(14, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getDataTerceiraNotificacao())){
							sqlInserir.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataTerceiraNotificacao()));
						}else{
							sqlInserir.setNull(15, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getDataNotificacaoPrazoExecucao())){
							sqlInserir.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataNotificacaoPrazoExecucao()));
						}else{
							sqlInserir.setNull(16, 0);
						}
						if(Uteis.isAtributoPreenchido(obj.getTipoNota())){
							sqlInserir.setString(17, obj.getTipoNota().name());
						}else{
							sqlInserir.setNull(17, 0);
						}
						sqlInserir.setBoolean(18, obj.getUtilizaNotaConceito());
						if(Uteis.isAtributoPreenchido(obj.getNotaConceito().getCodigo())){
							sqlInserir.setInt(19, obj.getNotaConceito().getCodigo());
						}else{
							sqlInserir.setNull(19, 0);
						}

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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO) throws Exception {
		Date date = new Date();
		if (Uteis.isAtributoPreenchido(atividadeDiscursivaRespostaAlunoVO.getDataLimiteEntrega()) && atividadeDiscursivaRespostaAlunoVO.getDataLimiteEntrega().compareTo(date) <= 0 && atividadeDiscursivaRespostaAlunoVO.getSituacaoRespostaAtividadeDiscursiva().toString() == "AGUARDANDO_AVALIACAO_PROFESSOR") {
			throw new Exception("Excedido o tempo para entrega da atividade");
		}
		if (Uteis.isAtributoPreenchido(atividadeDiscursivaRespostaAlunoVO.getAtividadeDiscursivaVO().getDataLimiteEntrega()) && atividadeDiscursivaRespostaAlunoVO.getAtividadeDiscursivaVO().getDataLimiteEntrega().compareTo(date) <= 0 && atividadeDiscursivaRespostaAlunoVO.getSituacaoRespostaAtividadeDiscursiva().toString() == "AGUARDANDO_AVALIACAO_PROFESSOR") {
			throw new Exception("Excedido o tempo para entrega da atividade");
		}
		if (validarUnicidade(atividadeDiscursivaRespostaAlunoVO)){
			throw new Exception("Já existe uma Atividade Discursiva Resposta Para esse Aluno.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		realizarCopiaImagem(atividadeDiscursivaRespostaAlunoVO, configuracaoGeralSistemaVO, usuarioVO);
		atividadeDiscursivaRespostaAlunoVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorChavePrimariaTrazendoMatricula(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()));
		getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().realizarDefinicaoProfessorTutoriaOnline(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO(), usuarioVO, true, true);
		if (atividadeDiscursivaRespostaAlunoVO.getCodigo().equals(0)) {
			incluir(atividadeDiscursivaRespostaAlunoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(atividadeDiscursivaRespostaAlunoVO, verificarAcesso, usuarioVO);
		}

	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirAtividadeDiscursivaInteracaoAlunoProfessor(AtividadeDiscursivaRespostaAlunoVO obj, AtividadeDiscursivaInteracaoVO atividadeInteracao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, boolean registrarLog, UsuarioVO usuarioVO) throws Exception {
		int codigo = obj.getCodigo();
		Boolean novoObj = obj.isNovoObj();
		boolean incluindoResposta = false;
		try {
			if (!Uteis.isAtributoPreenchido(obj)) {
				persistir(obj, configuracaoGeralSistemaVO, verificarAcesso, usuarioVO);
				incluindoResposta = true;
			}
			atividadeInteracao.setAtividadeDiscursivaRepostaAlunoVO(obj);
			if (usuarioVO.getIsApresentarVisaoProfessor()) {
				atividadeInteracao.setInteragidoPorEnum(InteragidoPorEnum.PROFESSOR);
			} else if (usuarioVO.getIsApresentarVisaoAluno()) {
				atividadeInteracao.setInteragidoPorEnum(InteragidoPorEnum.ALUNO);
			}
			if (!Uteis.isAtributoPreenchido(atividadeInteracao.getInteracao())) {
				throw new Exception("O campo INTERAÇÃO deve ser informado.");
			}
			getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().persistir(atividadeInteracao, verificarAcesso, usuarioVO);
			if (registrarLog && incluindoResposta) {
				registrarLogAtividadeDiscursivaRespostaAluno(obj.getAtividadeDiscursivaVO().getCodigo(), obj.getCodigo(), obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), "PERSISTIDO_POR_INTERACAO", new Date(), "", 
						obj.getSituacaoRespostaAtividadeDiscursiva(), obj.getAtividadeDiscursivaVO().getArtefatoEntrega(), obj.getArquivo(), usuarioVO.getCodigo());
			}
		} catch (Exception e) {
			obj.setCodigo(codigo);
			obj.setNovoObj(novoObj);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AtividadeDiscursivaRespostaAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AtividadeDiscursivaRespostaAluno.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE atividadediscursivarespostaaluno set resposta = ?, arquivo = ?, NomeArquivoApresentar = ?, " + "dataupload = ?, situacaorespostaatividadediscursiva = ?, pastabasearquivo = ?, nota = ?, " + " matriculaperiodoturmadisciplina = ?, atividadediscursiva = ?, notalancadanohistorico = ?, dataInicioAtividade=?, dataLimiteEntrega=?,dataPrimeiraNotificacao=?,dataSegundaNotificacao=?,dataTerceiraNotificacao=?,dataNotificacaoPrazoExecucao=?, tipoNota=?, utilizaNotaConceito=?,notaConceito=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setString(1, obj.getResposta());
					sqlAlterar.setString(2, obj.getArquivo());
					sqlAlterar.setString(3, obj.getNomeArquivoApresentar());
					if(Uteis.isAtributoPreenchido(obj.getDataUpload())){
						sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataUpload()));	
					}else{
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getSituacaoRespostaAtividadeDiscursiva().toString());
					sqlAlterar.setString(6, obj.getPastaBaseArquivo().toString());
					if(obj.getNota() != null){
						sqlAlterar.setDouble(7, obj.getNota());
					}else{
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setInt(8, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
					sqlAlterar.setInt(9, obj.getAtividadeDiscursivaVO().getCodigo());
					sqlAlterar.setBoolean(10, obj.getNotaLancadaNoHistorico());
					if(Uteis.isAtributoPreenchido(obj.getDataInicioAtividade())){
						sqlAlterar.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataInicioAtividade()));
					}else{
						sqlAlterar.setNull(11, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataLimiteEntrega())){
						sqlAlterar.setTimestamp(12, Uteis.getDataJDBCTimestamp(obj.getDataLimiteEntrega()));
					}else{
						sqlAlterar.setNull(12, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataPrimeiraNotificacao())){
						sqlAlterar.setTimestamp(13, Uteis.getDataJDBCTimestamp(obj.getDataPrimeiraNotificacao()));
					}else{
						sqlAlterar.setNull(13, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataSegundaNotificacao())){
						sqlAlterar.setTimestamp(14, Uteis.getDataJDBCTimestamp(obj.getDataSegundaNotificacao()));
					}else{
						sqlAlterar.setNull(14, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataTerceiraNotificacao())){
						sqlAlterar.setTimestamp(15, Uteis.getDataJDBCTimestamp(obj.getDataTerceiraNotificacao()));
					}else{
						sqlAlterar.setNull(15, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getDataNotificacaoPrazoExecucao())){
						sqlAlterar.setTimestamp(16, Uteis.getDataJDBCTimestamp(obj.getDataNotificacaoPrazoExecucao()));
					}else{
						sqlAlterar.setNull(16, 0);
					}
					if(Uteis.isAtributoPreenchido(obj.getTipoNota())){
						sqlAlterar.setString(17, obj.getTipoNota().name());
					}else{
						sqlAlterar.setNull(17, 0);
					}
					sqlAlterar.setBoolean(18, obj.getUtilizaNotaConceito());
					if(Uteis.isAtributoPreenchido(obj.getNotaConceito().getCodigo())){
						sqlAlterar.setInt(19, obj.getNotaConceito().getCodigo());
					}else{
						sqlAlterar.setNull(19, 0);
					}
					sqlAlterar.setInt(20, obj.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarPeriodoAtividadeDiscursivaRespostaAluno(final AtividadeDiscursivaVO obj,  UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE atividadediscursivarespostaaluno set dataInicioAtividade=?, dataLimiteEntrega=?  WHERE ((atividadediscursiva = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataLiberacao()));
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataLimiteEntrega()));
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AtividadeDiscursivaRespostaAluno.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM atividadediscursivarespostaaluno WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, atividadeDiscursivaRespostaAlunoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AtividadeDiscursivaRespostaAlunoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AtividadeDiscursivaRespostaAlunoVO obj = new AtividadeDiscursivaRespostaAlunoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setResposta(new String(tabelaResultado.getString("resposta").getBytes(Charset.forName("ISO-8859-1")), Charset.forName("ISO-8859-1")));
		obj.setArquivo(tabelaResultado.getString("arquivo"));
		obj.setNomeArquivoApresentar(tabelaResultado.getString("NomeArquivoApresentar"));
		obj.setDataUpload(tabelaResultado.getTimestamp("dataupload"));
		obj.setDataInicioAtividade(tabelaResultado.getDate("dataInicioAtividade"));
		obj.setDataLimiteEntrega(tabelaResultado.getDate("dataLimiteEntrega"));
		obj.setDataPrimeiraNotificacao(tabelaResultado.getDate("dataPrimeiraNotificacao"));
		obj.setDataSegundaNotificacao(tabelaResultado.getDate("dataSegundaNotificacao"));
		obj.setDataTerceiraNotificacao(tabelaResultado.getDate("dataTerceiraNotificacao"));
		obj.setDataNotificacaoPrazoExecucao(tabelaResultado.getDate("dataNotificacaoPrazoExecucao"));
		obj.setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.valueOf(tabelaResultado.getString("situacaorespostaatividadediscursiva")));
		obj.setNota(tabelaResultado.getObject("nota") != null? tabelaResultado.getDouble("nota"): null);
		obj.setUtilizaNotaConceito(tabelaResultado.getBoolean("utilizaNotaConceito"));
		obj.getNotaConceito().setCodigo(tabelaResultado.getInt("notaConceito"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("codigomatriculaturmadisciplina"));
		obj.getAtividadeDiscursivaVO().setCodigo(tabelaResultado.getInt("atividadediscursiva"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(tabelaResultado.getString("pessoanome"));
		obj.setNotaLancadaNoHistorico(tabelaResultado.getBoolean("notalancadanohistorico"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoNota"))) {
			obj.setTipoNota(TipoNotaConceitoEnum.valueOf(tabelaResultado.getString("tipoNota")));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("pastabasearquivo"))) {
			obj.setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(tabelaResultado.getString("pastabasearquivo")));
		}
		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setAtividadeDiscursivaVO(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarPorChavePrimaria(obj.getAtividadeDiscursivaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), 0, usuarioLogado));
			if(obj.getUtilizaNotaConceito() && Uteis.isAtributoPreenchido(obj.getNotaConceito().getCodigo())){
				obj.setNotaConceito(getFacadeFactory().getConfiguracaoAcademicoNotaConceitoFacade().consultarPorChavePrimaria(obj.getNotaConceito().getCodigo()));
			}
			return obj;
		}
		return obj;
	}

	@Override
	public List<AtividadeDiscursivaRespostaAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList<AtividadeDiscursivaRespostaAlunoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AtividadeDiscursivaRespostaAlunoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select atividadediscursivarespostaaluno.*, pessoa.codigo AS pessoa_codigo, ");
		sql.append("pessoa.nome AS pessoanome, matricula.matricula, matriculaperiodoturmadisciplina.codigo as codigomatriculaturmadisciplina  from atividadediscursivarespostaaluno ");
		sql.append("INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina ");
		sql.append("INNER JOIN matricula ON Matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sql.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append("where atividadediscursivarespostaaluno.codigo = ").append(codigo);

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AtividadeDiscursivaRespostaAlunoVO();
	}

	@Override
	public List<AtividadeDiscursivaRespostaAlunoVO> consultarDadosTelaConsultaProfessorAluno(Integer disciplina, Integer turma, String ano, String semestre, Integer atividadeDiscursiva, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		List<AtividadeDiscursivaRespostaAlunoVO> lista = new ArrayList<AtividadeDiscursivaRespostaAlunoVO>(0);
		AtividadeDiscursivaRespostaAlunoVO obj = new AtividadeDiscursivaRespostaAlunoVO();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT DISTINCT matricula.matricula, matricula.situacao, pessoa.codigo AS pessoa_codigo, ");
		sql.append("pessoa.nome AS pessoanome, turmaavaliacao.identificadorturma as identificadorturma, turmaavaliacao.codigo as codigoturma, disciplina.nome as nomedisciplina, ");
		sql.append("matriculaperiodo.situacaomatriculaperiodo, atividadediscursivarespostaaluno.codigo, atividadediscursivarespostaaluno.situacaoRespostaAtividadeDiscursiva, matriculaperiodoturmadisciplina.codigo as codigomatriculaturmadisciplina, matriculaperiodoturmadisciplina.ano as anomatriculaperiodoturmadisciplina, matriculaperiodoturmadisciplina.semestre as semestrematriculaperiodoturmadisciplina,");
		sql.append("matriculaperiodoturmadisciplina.disciplina as disciplinamatriculaperiodoturmadisciplina, (select count(codigo) from atividadediscursivainteracao where atividadediscursivainteracao.atividadediscursivarespostaaluno = atividadediscursivarespostaaluno.codigo and atividadediscursivainteracao.interacaojalida = 'f' and atividadediscursivainteracao.interagidopor = 'ALUNO')  as qtdeInteracao ");
		sql.append("FROM matricula ");
		sql.append("INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sql.append("INNER JOIN atividadediscursiva on atividadediscursiva.codigo = ").append(atividadeDiscursiva);
		sql.append("INNER JOIN matriculaperiodoturmadisciplina on (matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo ");
		sql.append(" AND matriculaperiodoturmadisciplina.disciplina IN (select disciplinaequivalente.equivalente from disciplinaequivalente ");
		sql.append(" where disciplinaequivalente.disciplina = ").append(disciplina).append(" union all select ").append(disciplina).append(" ) )");

		sql.append("INNER JOIN turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append("INNER JOIN historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append("INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append("INNER JOIN turmadisciplina ON turmadisciplina.disciplina = (CASE WHEN coalesce(matriculaperiodoturmadisciplina.disciplinafazpartecomposicao,false) = false then disciplina.codigo ");
		sql.append(" else( SELECT gradedisciplina.disciplina ");
		sql.append(" FROM gradedisciplinacomposta   ");
		sql.append(" INNER JOIN gradedisciplina on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo");
		sql.append(" WHERE gradedisciplinacomposta.codigo = matriculaperiodoturmadisciplina.gradedisciplinacomposta");
		sql.append(" ) end )");
		
		sql.append("LEFT JOIN atividadediscursivarespostaaluno on atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append("and atividadediscursivarespostaaluno.atividadediscursiva = ").append(atividadeDiscursiva);
		sql.append(" left join turma as turmaavaliacao on turmaavaliacao.codigo =  atividadediscursiva.turma ");
		sql.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sql.append(" and  ((atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' AND ( ");
		sql.append("    (turmaavaliacao.turmaagrupada = false and turmaavaliacao.subturma = false and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma)  ");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmapratica )");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmateorica )");
		sql.append(" or (turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.GERAL.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma )");
		sql.append(" or (turmaavaliacao.turmaagrupada and turmaavaliacao.subturma = false and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ))");
		sql.append(" )) ");
		
		sql.append(" or  (atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.ALUNO.name()).append("' and atividadediscursiva.matriculaPeriodoTurmaDisciplina = matriculaperiodoturmadisciplina.codigo )) ");
		sql.append(" and Pessoa.codigo = Matricula.aluno ");
				
		sql.append(" AND ((turmadisciplina.definicoestutoriaonline = 'DINAMICA' and matriculaperiodoturmadisciplina.professor = ").append(usuarioVO.getPessoa().getCodigo()).append(")");
		sql.append("  or turmadisciplina.definicoestutoriaonline = 'PROGRAMACAO_DE_AULA')");
		
		sql.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		sql.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("'");
//		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
//			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
//		}
		sql.append(" ORDER BY turmaavaliacao.identificadorturma, Pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		while (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setSituacao(tabelaResultado.getString("situacao"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(tabelaResultado.getString("pessoanome"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("codigomatriculaturmadisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setAno(tabelaResultado.getString("anomatriculaperiodoturmadisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setSemestre(tabelaResultado.getString("semestrematriculaperiodoturmadisciplina"));
			obj.getAtividadeDiscursivaVO().getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplinamatriculaperiodoturmadisciplina"));
			obj.getAtividadeDiscursivaVO().getDisciplinaVO().setNome(tabelaResultado.getString("nomedisciplina"));
			obj.getAtividadeDiscursivaVO().getTurmaVO().setCodigo(tabelaResultado.getInt("codigoturma"));
			obj.getAtividadeDiscursivaVO().getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
			obj.getAtividadeDiscursivaVO().setCodigo(atividadeDiscursiva);
			obj.setInteracao(tabelaResultado.getInt("qtdeInteracao"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacaoRespostaAtividadeDiscursiva"))) {
				obj.setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.valueOf(tabelaResultado.getString("situacaoRespostaAtividadeDiscursiva")));
			}

			if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
				obj.isNovoObj();
			}

			lista.add(obj);

			obj = new AtividadeDiscursivaRespostaAlunoVO();
		}
		return lista;
	}

	@Override
	public List<AtividadeDiscursivaRespostaAlunoVO> consultarAtividadeDiscursivasPorMatriculaOuCodigoMatriculaPeriodoTurmaDisciplina(String matricula, Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO,  String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT matricula.matricula, matricula.situacao, pessoa.codigo AS pessoa_codigo, ");
		sql.append(" pessoa.nome AS pessoanome, turmaavaliacao.identificadorturma, ");
		sql.append(" matriculaperiodo.situacaomatriculaperiodo, atividadediscursivarespostaaluno.codigo, turmaavaliacao.codigo as codigoturma,turmaavaliacao.anual, turmaavaliacao.semestral, atividadediscursivarespostaaluno.situacaoRespostaAtividadeDiscursiva, matriculaperiodoturmadisciplina.codigo as codigomatriculaturmadisciplina, matriculaperiodoturmadisciplina.matriculaperiodo as \"matriculaperiodoturmadisciplina.matriculaperiodo\", ");
		sql.append(" atividadediscursiva.codigo as atividadediscursiva, atividadediscursiva.artefatoentrega, atividadediscursiva.enunciado as enunciado, atividadediscursiva.dataliberacao, atividadediscursiva.dataLimiteEntrega,calendarioatividadematricula.dataInicio,calendarioatividadematricula.dataFim, atividadediscursiva.ano, atividadediscursiva.semestre, atividadediscursiva.qtddiasaposinicioliberar, atividadediscursiva.qtddiasparaconclusao, atividadediscursiva.definicaoDataEntregaAtividadeDiscursiva,");
		sql.append(" disciplina.nome as disciplina, disciplina.codigo as codigodisciplina, ");
		sql.append(" (select count(codigo) from atividadediscursivainteracao where atividadediscursivainteracao.atividadediscursivarespostaaluno = atividadediscursivarespostaaluno.codigo and atividadediscursivainteracao.interacaojalida = 'f' and atividadediscursivainteracao.interagidopor = 'PROFESSOR')  as qtdeInteracao ");
		sql.append(" FROM matricula ");
		sql.append(" INNER JOIN matriculaperiodo 				ON  MatriculaPeriodo.matricula = Matricula.matricula  ");
		sql.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo  ");
		sql.append(" INNER JOIN disciplina 						ON disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" INNER JOIN LATERAL ( ");
		sql.append(" SELECT ");
		sql.append("  turma.codigo AS turma ");
		sql.append(" FROM turma ");
		sql.append(" WHERE turma.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" UNION ALL ");
		sql.append(" SELECT ");
		sql.append(" turmaagrupada.turmaorigem AS turma ");
		sql.append(" FROM turmaagrupada ");
		sql.append(" WHERE turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ");
		sql.append(" UNION ALL 	");
		sql.append(" SELECT	turma.codigo as turma	from turma	");
		sql.append(" WHERE	turma.codigo = matriculaperiodoturmadisciplina.turmateorica ");
		sql.append(" UNION ALL");
		sql.append(" SELECT	turma.codigo as turma	from turma 	");
		sql.append(" WHERE	turma.codigo = matriculaperiodoturmadisciplina.turmapratica ) as turmas on	");
		sql.append(" 1 = 1");
        sql.append(" INNER JOIN  turma ON turmas.turma = turma.codigo ");
		sql.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append(" INNER JOIN atividadediscursiva on ((turma.turmaagrupada = false and atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina) or (turma.turmaagrupada and (");
		sql.append(" matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplina = atividadediscursiva.disciplina)");
		sql.append(" or atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina ))) ");
		sql.append(" AND atividadediscursiva.ano  = matriculaperiodoturmadisciplina.ano and atividadediscursiva.semestre  = matriculaperiodoturmadisciplina.semestre  ");
		sql.append(" LEFT JOIN atividadediscursivarespostaaluno on atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sql.append(" AND atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo ");
		sql.append(" LEFT JOIN turma as turmaavaliacao on turmaavaliacao.codigo =  atividadediscursiva.turma and turmaavaliacao.codigo = turma.codigo");
		sql.append(" LEFT JOIN calendarioatividadematricula on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo and calendarioatividadematricula.codOrigem::int = atividadediscursiva.codigo ");
		if (usuarioVO.getIsApresentarVisaoProfessor()) {
			sql.append(" and matriculaperiodoturmadisciplina.professor = ").append(usuarioVO.getPessoa().getCodigo());
		}
		sql.append(" where  ((atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.TURMA.name()).append("' and  (");		
		sql.append(" (turmaavaliacao.turmaagrupada = false and turmaavaliacao.subturma = false and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null)  ");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmapratica )");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turmateorica )");
		sql.append(" or (turmaavaliacao.subturma and turmaavaliacao.tiposubturma = '").append(TipoSubTurmaEnum.GERAL.getName()).append("' and turmaavaliacao.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null)");
		sql.append(" or (turmaavaliacao.turmaagrupada and turmaavaliacao.subturma =  false and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ))");
		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turmaavaliacao.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turmaavaliacao.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ))");
		sql.append(" )) ");
		sql.append(" or  (atividadediscursiva.publicoAlvo = '").append(PublicoAlvoAtividadeDiscursivaEnum.ALUNO.name()).append("' and atividadediscursiva.matriculaPeriodoTurmaDisciplina = matriculaperiodoturmadisciplina.codigo )) ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sql.append("and matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
		}
		if (codigoMatriculaPeriodoTurmaDisciplina != 0) {
			sql.append("and matriculaperiodoturmadisciplina.codigo = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		}

		if (ano != null && !ano.equals("")) {
			sql.append(" AND atividadediscursiva.ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" AND atividadediscursiva.semestre = '").append(semestre).append("' ");
		}
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));	
		sql.append(" ORDER BY turmaavaliacao.identificadorturma, Pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<AtividadeDiscursivaRespostaAlunoVO> lista = new ArrayList<>();
		AtividadeDiscursivaRespostaAlunoVO obj;
		while (tabelaResultado.next()) {
			obj = new AtividadeDiscursivaRespostaAlunoVO();
			obj.setNovoObj(false);
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getAtividadeDiscursivaVO().setEnunciado(tabelaResultado.getString("enunciado"));
			obj.getAtividadeDiscursivaVO().getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
			obj.getAtividadeDiscursivaVO().setAno(tabelaResultado.getString("ano"));
			obj.getAtividadeDiscursivaVO().setSemestre(tabelaResultado.getString("semestre"));
			obj.getAtividadeDiscursivaVO().getDisciplinaVO().setNome(tabelaResultado.getString("disciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(tabelaResultado.getString("pessoanome"));
			obj.getAtividadeDiscursivaVO().setCodigo(tabelaResultado.getInt("atividadediscursiva"));
			obj.getAtividadeDiscursivaVO().getTurmaVO().setCodigo(tabelaResultado.getInt("codigoturma"));
			obj.getAtividadeDiscursivaVO().getTurmaVO().setAnual(tabelaResultado.getBoolean("anual"));
			obj.getAtividadeDiscursivaVO().getTurmaVO().setSemestral(tabelaResultado.getBoolean("semestral"));
			obj.getAtividadeDiscursivaVO().getDisciplinaVO().setCodigo(tabelaResultado.getInt("codigodisciplina"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("artefatoentrega"))) {
				obj.getAtividadeDiscursivaVO().setArtefatoEntrega(ArtefatoEntregaEnum.valueOf(tabelaResultado.getString("artefatoentrega")));
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacaoRespostaAtividadeDiscursiva"))) {
				obj.setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.valueOf(tabelaResultado.getString("situacaoRespostaAtividadeDiscursiva")));
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getTimestamp("dataFim"))) {
				obj.getAtividadeDiscursivaVO().setDataLimiteEntrega(Uteis.getDataJDBC(tabelaResultado.getTimestamp("dataFim")));
			}else {
			  obj.getAtividadeDiscursivaVO().setDataLimiteEntrega(Uteis.getDataJDBC(tabelaResultado.getTimestamp("dataLimiteEntrega")));
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getTimestamp("dataInicio"))) {
				obj.getAtividadeDiscursivaVO().setDataLiberacao(Uteis.getDataJDBC(tabelaResultado.getTimestamp("dataInicio")));	
			}else {
			  obj.getAtividadeDiscursivaVO().setDataLiberacao(Uteis.getDataJDBC(tabelaResultado.getTimestamp("dataLiberacao")));
			}
			obj.getAtividadeDiscursivaVO().setQtdDiasAposInicioLiberar(tabelaResultado.getInt("qtddiasaposinicioliberar"));;
			obj.getAtividadeDiscursivaVO().setQtdDiasParaConclusao(tabelaResultado.getInt("qtddiasparaconclusao"));;
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("definicaoDataEntregaAtividadeDiscursiva"))) {
			   obj.getAtividadeDiscursivaVO().setDefinicaoDataEntregaAtividadeDiscursivaEnum(DefinicaoDataEntregaAtividadeDiscursivaEnum.valueOf(tabelaResultado.getString("definicaoDataEntregaAtividadeDiscursiva")));
			}
			obj.setInteracao(tabelaResultado.getInt("qtdeInteracao"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("codigomatriculaturmadisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setMatriculaPeriodo(tabelaResultado.getInt("matriculaperiodoturmadisciplina.matriculaperiodo"));
			if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
				obj.setNovoObj(true);
			}
			lista.add(obj);
		}
		return lista;
	}

	@Override
	public void uploadArquivo(FileUploadEvent uploadEvent, AtividadeDiscursivaRespostaAlunoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		validarTamanhoArquivoConformeTamanhoMaximoUploadConfiguracaoGeralSistema(uploadEvent, configuracaoGeralSistemaVO);
		if (!obj.getArquivo().trim().isEmpty() && obj.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO)) {
			obj.setNomeArquivoAnt(obj.getArquivo());
		} else if (!obj.getArquivo().trim().isEmpty() && obj.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP)) {
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo().getValue() + File.separator + obj.getArquivo());
			ArquivoHelper.delete(arquivo);
			arquivo = null;
		}
		obj.setArquivo(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(uploadEvent.getUploadedFile().getName())));
		obj.setNomeArquivoApresentar(uploadEvent.getUploadedFile().getName());
		obj.setPastaBaseArquivo(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP);		
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, obj.getArquivo(), obj.getPastaBaseArquivo().getValue(), configuracaoGeralSistemaVO, usuarioVO);
		obj.setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + obj.getPastaBaseArquivo().getValue() + "/" + obj.getArquivo());
	}

	private void realizarCopiaImagem(AtividadeDiscursivaRespostaAlunoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		if (obj.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP) && !obj.getArquivo().trim().isEmpty()) {
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP.getValue() + File.separator + obj.getArquivo());
			
			ArquivoHelper.salvarArquivoNaPastaTemp(arquivo, obj.getArquivo(), PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO.getValue(), configuracaoGeralSistemaVO, usuario);
			ArquivoHelper.delete(arquivo);
			obj.setPastaBaseArquivo(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO);
			obj.setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO.getValue() + "/" + obj.getArquivo());
			if (!obj.getNomeArquivoAnt().trim().isEmpty()) {
				arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO.getValue() + File.separator + obj.getNomeArquivoAnt());
				ArquivoHelper.delete(arquivo);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AtividadeDiscursivaRespostaAlunoVO realizarCriacaoAtividadeDiscursivaRespostaAluno(AtividadeDiscursivaRespostaAlunoVO obj, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			obj = consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
			getFacadeFactory().getAtividadeDiscursivaInteracaoInterfaceFacade().alterarInteracaoJaLidaAoVisualizarATela(obj.getCodigo(), false, usuario);
		} else {
			obj.setAtividadeDiscursivaVO(getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarPorChavePrimaria(obj.getAtividadeDiscursivaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
			if(Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoTurmaDisciplinaVO())){
				obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorChavePrimariaTrazendoMatricula(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()));
			}
			if( Uteis.isAtributoPreenchido(obj.getAtividadeDiscursivaVO().getTurmaVO().getConfiguracaoEADVO()) 
					&& Uteis.isAtributoPreenchido(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarDisciplinaDoAlunoPorMatricula(obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), "", null, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario).stream().filter(MatriculaPeriodoTurmaDisciplinaVO::getPermiteAcessoEAD).collect(Collectors.toList()))){
				CalendarioAtividadeMatriculaVO calendarioAtividadeMatricula = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA, TipoOrigemEnum.ATIVIDADE_DISCURSIVA, obj.getAtividadeDiscursivaVO().getCodigo().toString(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
				if(Uteis.isAtributoPreenchido(calendarioAtividadeMatricula)){
					executarDefinicaoDatasAtividadeDiscursivaRespostaAluno(obj, calendarioAtividadeMatricula);
				} else if (usuario.getIsApresentarVisaoAluno()) {
					MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().realizarAcessoEstudoOnline(matriculaPeriodoTurmaDisciplinaVO, false,	usuario);
					calendarioAtividadeMatricula = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA, TipoOrigemEnum.ATIVIDADE_DISCURSIVA, obj.getAtividadeDiscursivaVO().getCodigo().toString(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
					if (Uteis.isAtributoPreenchido(calendarioAtividadeMatricula)) {
						executarDefinicaoDatasAtividadeDiscursivaRespostaAluno(obj, calendarioAtividadeMatricula);
					} else {
						throw new Exception("Não foi possível criar o Calendário de Atividade do Aluno referente à atividade discursiva.");
					}
				}
			}else{
				obj.setDataInicioAtividade(obj.getAtividadeDiscursivaVO().getDataLiberacao());
				obj.setDataLimiteEntrega(obj.getAtividadeDiscursivaVO().getDataLimiteEntrega());
			}
			if (usuario.getIsApresentarVisaoAluno() && UteisData.getCompareDataComHora(new Date(), obj.getDataInicioAtividade()) == -1) {
				throw new Exception(UteisJSF.internacionalizar("msg_AtividadeDiscursiva_naoLiberadaParaSerRealizada").replace("{0}", obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().getNome()).replace("{1}", UteisData.getDataComHora(obj.getDataInicioAtividade())));
			}
		}
		return obj;
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAvaliarRespostaAlunoAtividadeDiscursiva(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, HistoricoVO historicoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		atividadeDiscursivaRespostaAlunoVO.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		if(atividadeDiscursivaRespostaAlunoVO.getTipoNota() != null){
			atividadeDiscursivaRespostaAlunoVO.setNotaLancadaNoHistorico(getFacadeFactory().getHistoricoFacade().realizarLancamentoNotaHistoricoAutomaticamente(atividadeDiscursivaRespostaAlunoVO.getTipoNota(), historicoVO, atividadeDiscursivaRespostaAlunoVO.getNota(), atividadeDiscursivaRespostaAlunoVO.getUtilizaNotaConceito(), atividadeDiscursivaRespostaAlunoVO.getNotaConceito(), atividadeDiscursivaRespostaAlunoVO.getMatriculaPeriodoTurmaDisciplinaVO(), atividadeDiscursivaRespostaAlunoVO.getAtividadeDiscursivaVO().getTurmaVO().getConfiguracaoEADVO().getCalcularMediaFinalAposRealizacaoAtividadeDiscursiva(), usuarioVO));
		}
		atividadeDiscursivaRespostaAlunoVO.setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.AVALIADO);
		persistir(atividadeDiscursivaRespostaAlunoVO, configuracaoGeralSistemaVO, false, usuarioVO);	
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet consultarAlunosEProfessoresNotificacaoAtividadeDiscursiva() throws Exception {

		StringBuilder sqlStr = new StringBuilder("");

		sqlStr.append(" select distinct matricula.unidadeensino as unidadeensino, case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' then   professor.codigo else aluno.codigo end as codigo,");
		sqlStr.append(" case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' then   professor.nome else aluno.nome end as nome,");
		sqlStr.append(" case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' then   professor.nome else aluno.nome end as nome,");
		sqlStr.append(" case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' then   professor.email else aluno.email end as email, ");
		sqlStr.append(" case when notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso then");
		sqlStr.append("		case when EXTRACT( DAYS FROM (atividadediscursiva.datalimiteentrega-current_timestamp)) =  notUmAtividadeDiscursivaPrazoConclusaoCurso then 'MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO' else");
		sqlStr.append(" 	case when EXTRACT( DAYS FROM (atividadediscursiva.datalimiteentrega-current_timestamp)) =  notDoisAtividadeDiscursivaPrazoConclusaoCurso then 'MENSAGEM_NOTIFICACAO2_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO' else");
		sqlStr.append(" 	case when EXTRACT( DAYS FROM (atividadediscursiva.datalimiteentrega-current_timestamp)) =  notTresAtividadeDiscursivaPrazoConclusaoCurso then 'MENSAGEM_NOTIFICACAO3_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO'");
		sqlStr.append(" end end end end as templateNotificacao,");
		sqlStr.append(" atividadediscursivarespostaaluno.codigo as atividadediscursivarespostaaluno, atividadediscursivarespostaaluno.datalimiteentrega, disciplina.nome as nomedisciplina, aluno.aluno, professor.professor,");	
		sqlStr.append(" case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' ");
		sqlStr.append(" then   professor.professor end as professor, case when atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva = 'AGUARDANDO_NOVA_RESPOSTA' ");
		sqlStr.append(" then   aluno.aluno end as aluno");
		sqlStr.append(" from atividadediscursivarespostaaluno");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join matricula on matriculaperiodo.matricula = matricula.matricula");
		sqlStr.append(" inner join pessoa as aluno on matricula.aluno = aluno.codigo");
		sqlStr.append(" inner join atividadediscursiva on atividadediscursiva.codigo = atividadediscursivarespostaaluno.atividadediscursiva");
		sqlStr.append(" left join pessoa as professor on professor.codigo = matriculaperiodoturmadisciplina.professor");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join configuracaoead on turma.configuracaoead = configuracaoead.codigo");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem1 on mensagem1.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO1_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO'");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem2 on mensagem2.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO2_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO'");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem3 on mensagem3.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO3_CONFIGURACAOEAD_ALUNOS_ATIVIDADE_DISCURSIVA_PRAZO_CONCLUSAO'");
		sqlStr.append(" where matriculaperiodo.situacaomatriculaperiodo = 'AT'");
		sqlStr.append(" and atividadediscursivarespostaaluno.situacaorespostaatividadediscursiva in ('AGUARDANDO_RESPOSTA', 'AGUARDANDO_AVALIACAO_PROFESSOR', 'AGUARDANDO_NOVA_RESPOSTA')");
		sqlStr.append(" and notificarAlunosAtividadeDiscursivaPrazoConclusaoCurso");
		sqlStr.append(" and (");
		sqlStr.append(" 	EXTRACT( DAYS FROM (atividadediscursiva.datalimiteentrega-current_timestamp)) =  notUmAtividadeDiscursivaPrazoConclusaoCurso and notUmAtividadeDiscursivaPrazoConclusaoCurso > 0 and mensagem1.desabilitarEnvioMensagemAutomatica = false or");
		sqlStr.append(" 	EXTRACT( DAYS FROM (atividadediscursiva.datalimiteentrega-current_timestamp)) =  notDoisAtividadeDiscursivaPrazoConclusaoCurso and notDoisAtividadeDiscursivaPrazoConclusaoCurso > 0 and mensagem2.desabilitarEnvioMensagemAutomatica = false or");
		sqlStr.append(" 	EXTRACT( DAYS FROM (atividadediscursiva.datalimiteentrega-current_timestamp)) =  notTresAtividadeDiscursivaPrazoConclusaoCurso and notTresAtividadeDiscursivaPrazoConclusaoCurso > 0 and mensagem3.desabilitarEnvioMensagemAutomatica = false");
		sqlStr.append(" )");

		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	/**
	 * 
	 * @author Victor Hugo 03/12/2014
	 * 
	 *         Metodo criado para incluir o codigo do conteudo na
	 *         matriculaPeriodoTurmaDisciplinaVO no momento em que o aluno
	 *         acesso o conteúdo para estudo.
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDataNotificacaoAtividadeDiscursivaRespostaAluno(final Integer codigoAtividadeDiscursivaRespostaAlunoVO, final Date dataNotificacao, String campoDataNotificacao) throws Exception {
		final String sql = "UPDATE atividadediscursivarespostaaluno set " + campoDataNotificacao + " = ? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(dataNotificacao));
				sqlAlterar.setInt(2, codigoAtividadeDiscursivaRespostaAlunoVO);
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SqlRowSet consultarAtividadesDiscursivasRespostaAlunoIniciandoNaDataAtual() throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select pessoa.nome, pessoa.codigo, pessoa.email, disciplina.nome, disciplina.codigo, atividadediscursivarespostaaluno.datainicioatividade, atividadediscursivarespostaaluno.datalimiteentrega from atividadediscursivarespostaaluno");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" where DATE(atividadediscursivarespostaaluno.datainicioatividade) = current_date");
		sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT'");

		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private boolean validarUnicidade(AtividadeDiscursivaRespostaAlunoVO obj) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select codigo from atividadediscursivarespostaaluno ");
		sqlStr.append(" ");
		sqlStr.append(" where atividadediscursiva = ").append(obj.getAtividadeDiscursivaVO().getCodigo());
		sqlStr.append(" and matriculaperiodoturmadisciplina =  ").append(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
		if(Uteis.isAtributoPreenchido(obj.getCodigo())){
			sqlStr.append(" and codigo !=  ").append(obj.getCodigo());
		}
		SqlRowSet  rs= getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return rs.next();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarPeriodoAtividadeDiscursivaRespostaAlunoPorCodigo(AtividadeDiscursivaRespostaAlunoVO obj,  UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE atividadediscursivarespostaaluno set dataInicioAtividade=?, dataLimiteEntrega=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getDataInicioAtividade()));
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getDataLimiteEntrega()));
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public AtividadeDiscursivaRespostaAlunoVO consultarAtividadeRespostaAlunoPorCodAtividadediscursivaMatriculaperiodoturmadisciplina(String codigoAtividadeDiscursiva , Integer matriculaperiodoturmadisciplina   , UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select codigo,dataInicioAtividade,dataLimiteEntrega from atividadediscursivarespostaaluno");
		sqlStr.append(" where atividadediscursiva = ").append(codigoAtividadeDiscursiva);
		sqlStr.append(" and matriculaperiodoturmadisciplina =  ").append(matriculaperiodoturmadisciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			AtividadeDiscursivaRespostaAlunoVO obj = new AtividadeDiscursivaRespostaAlunoVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDataInicioAtividade(rs.getDate("dataInicioAtividade"));
			obj.setDataLimiteEntrega(rs.getDate("dataLimiteEntrega"));
			return obj;
		}
		return new AtividadeDiscursivaRespostaAlunoVO();
	}
	
	public Boolean consultarAtividadeDiscursivaRespondida(Integer codigoAtividadeDiscursiva) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select atividadediscursivarespostaaluno.codigo from atividadediscursivarespostaaluno where situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR' ");
		sql.append(" and atividadediscursiva = ").append(codigoAtividadeDiscursiva);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return true;
		}else {
			return false;
		}
	}

	public void removerArquivo(AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + atividadeDiscursivaRespostaAlunoVO.getPastaBaseArquivo().getValue() 
				+ File.separator + atividadeDiscursivaRespostaAlunoVO.getArquivo());
		ArquivoHelper.delete(arquivo);
		atividadeDiscursivaRespostaAlunoVO.setArquivo("");
		atividadeDiscursivaRespostaAlunoVO.setNomeArquivoApresentar("");
		atividadeDiscursivaRespostaAlunoVO.setPastaBaseArquivo(PastaBaseArquivoEnum.ATIVIDADE_DISCURSIVA_RESPOSTA_ALUNO_TMP);		
		atividadeDiscursivaRespostaAlunoVO.setUrlImagem("");
	}
	
	private void validarTamanhoArquivoConformeTamanhoMaximoUploadConfiguracaoGeralSistema(FileUploadEvent uploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		if (uploadEvent != null 
				&& uploadEvent.getUploadedFile() != null
				&& Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getTamanhoMaximoUpload())
				&& uploadEvent.getUploadedFile().getSize() > (configuracaoGeralSistemaVO.getTamanhoMaximoUpload().longValue() * 1024 * 1024)) {
			throw new ConsistirException("O tamanho do arquivo excedeu o limite (" + configuracaoGeralSistemaVO.getTamanhoMaximoUpload() + "MB).");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void registrarLogAtividadeDiscursivaRespostaAluno(int atividadeDiscursiva, int atividadeDiscursivaRespostaAluno, int matriculaPeriodoTurmaDisciplina, String acao, Date data,
			String erro, SituacaoRespostaAtividadeDiscursivaEnum situacaoRespostaAtividadeDiscursivaEnum, ArtefatoEntregaEnum artefatoEntregaEnum, String arquivo, int usuario) {
		try {
			new Thread(() -> {
				StringBuilder sqlStr = new StringBuilder()
						.append(" INSERT INTO logAtividadeDiscursivaRespostaAluno (atividadeDiscursiva, atividadeDiscursivaRespostaAluno, matriculaPeriodoTurmaDisciplina, acao, data, erro, ")
						.append(" situacaoRespostaAtividadeDiscursivaEnum, artefatoEntregaEnum, arquivo, usuario) ")
						.append(" VALUES (")
						.append("").append(atividadeDiscursiva).append(",")
						.append("").append(atividadeDiscursivaRespostaAluno).append(",")
						.append("").append(matriculaPeriodoTurmaDisciplina).append(",")
						.append("'").append(acao).append("',")
						.append("'").append(Uteis.getDataJDBCTimestamp(data)).append("',")
						.append("'").append(erro).append("',")
						.append("'").append(situacaoRespostaAtividadeDiscursivaEnum).append("',")
						.append("'").append(artefatoEntregaEnum).append("',")
						.append("'").append(arquivo).append("',")
						.append("").append(usuario)
						.append("); ");
				getConexao().getJdbcTemplate().execute(sqlStr.toString());
			}).start();
		} catch (Exception e) {
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirAtividadeDiscursivaInteracaoSolicitandoAvaliacaoProfessor(AtividadeDiscursivaRespostaAlunoVO obj, AtividadeDiscursivaInteracaoVO atividadeInteracao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		persistirAtividadeDiscursivaInteracaoAlunoProfessor(obj, atividadeInteracao, configuracaoGeralSistemaVO, verificarAcesso, false, usuarioVO);
		obj.setSituacaoRespostaAtividadeDiscursiva(SituacaoRespostaAtividadeDiscursivaEnum.AGUARDANDO_AVALIACAO_PROFESSOR);
		persistir(obj, configuracaoGeralSistemaVO, verificarAcesso, usuarioVO);
	}
	
	private void executarDefinicaoDatasAtividadeDiscursivaRespostaAluno(AtividadeDiscursivaRespostaAlunoVO obj,	CalendarioAtividadeMatriculaVO calendarioAtividadeMatricula) {
		obj.setDataInicioAtividade(calendarioAtividadeMatricula.getDataInicio());
		if (Uteis.isAtributoPreenchido(calendarioAtividadeMatricula.getDataFim()) && calendarioAtividadeMatricula.getDataFim().compareTo(obj.getAtividadeDiscursivaVO().getDataLimiteEntrega()) > 0) {
			obj.setDataLimiteEntrega(calendarioAtividadeMatricula.getDataFim());
		} else {
			obj.setDataLimiteEntrega(obj.getAtividadeDiscursivaVO().getDataLimiteEntrega());
		}
	}
}
