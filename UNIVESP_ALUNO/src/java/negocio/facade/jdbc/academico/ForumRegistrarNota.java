package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ForumRegistrarNotaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ForumRegistrarNotaInterfaceFacade;

@Repository
@Lazy
public class ForumRegistrarNota extends ControleAcesso implements ForumRegistrarNotaInterfaceFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8152127599734119877L;
	protected static String idEntidade;
	
	@Override
	public void carregarDadosConfiguracaoAcadimicoPeloTipoDaNota(ForumVO forum , String variavel, ConfiguracaoAcademicoVO configuracaoAcademicoVigenteNota, Boolean isNotaPorConceito, UsuarioVO usuarioLogado) throws Exception{
		StringBuilder sb = new StringBuilder(); 
		for (ForumRegistrarNotaVO obj : forum.getForumRegistrarNotaVOs()) {	
			if(obj.getHistoricoVO().getConfiguracaoAcademico().getCodigo().equals(configuracaoAcademicoVigenteNota.getCodigo())){
				obj.setVariavelTipoNota(variavel);
				sb.append(obj.getHistoricoVO().getCodigo()).append(", ");    
			}
		}
		List<HistoricoVO> lista = new ArrayList<>();
		if(Uteis.isAtributoPreenchido(variavel)){
			lista = getFacadeFactory().getHistoricoFacade().listarNotasHistoricoPorForum(variavel, sb.toString().substring(0, sb.toString().length() -2), isNotaPorConceito, usuarioLogado);
		}
		forhistorioco:for (HistoricoVO historicoVO : lista) {
			for (ForumRegistrarNotaVO obj : forum.getForumRegistrarNotaVOs()) {	
				if(obj.getHistoricoVO().getCodigo().equals(historicoVO.getCodigo())){
					Double nota = (Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + variavel);
					if(Uteis.isAtributoPreenchido(nota)){
						obj.setNotaHistorico(nota);
					}else{
						obj.setNotaHistorico(null);
					}
					if(isNotaPorConceito){
						ConfiguracaoAcademicoNotaConceitoVO conf = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + variavel + "Conceito");
						if(Uteis.isAtributoPreenchido(conf)){
							obj.setNotaConceitoHistorico(conf);	
						}else{
							obj.setNotaConceitoHistorico(new ConfiguracaoAcademicoNotaConceitoVO());
						}
					}
					continue forhistorioco;
				}					
			}
		}
		
	}

	private void validarDados(ForumRegistrarNotaVO obj) throws ConsistirException {
		ConsistirException ex = new ConsistirException();
		if (!Uteis.isAtributoPreenchido(obj.getPessoaVO())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ForumRegistrarNota_pessoa"));
		}
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirForumRegistrarNota(ForumVO obj, HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		for (ForumRegistrarNotaVO forumRegistrar : obj.getForumRegistrarNotaVOs()) {
			validarForumRegistrarNota(forumRegistrar, mapaConfiguracoesAcademicos, configuracaoAcademicaNotaVO);
			forumRegistrar.setDataRegistro(new Date());
			forumRegistrar.setUsuarioVO(usuario);
			if (!Uteis.isAtributoPreenchido(forumRegistrar)) {
				incluir(forumRegistrar, controlarAcesso, usuario);
			} else {
				alterar(forumRegistrar, controlarAcesso, usuario);
			}
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ForumRegistrarNotaVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO ForumRegistrarNota ");
			sql.append(" ( forum, pessoa, historico, nota, configuracaoacademiconotaconceito, variaveltiponota, dataRegistro, usuario ) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(x++, obj.getForumVO().getCodigo().intValue());
					sqlInserir.setInt(x++, obj.getPessoaVO().getCodigo().intValue());
					sqlInserir.setInt(x++, obj.getHistoricoVO().getCodigo().intValue());
					// nao pode utilizar isAtributoPreenchido do uteis pois a nota zero e valida.
					if(obj.getNota() != null){
						sqlInserir.setDouble(x++, obj.getNota());	
					}else{
						sqlInserir.setNull(x++, 0);	
					}
					if(Uteis.isAtributoPreenchido(obj.getNotaConceito())){
						sqlInserir.setInt(x++, obj.getNotaConceito().getCodigo());	
					}else{
						sqlInserir.setNull(x++, 0);	
					}
					sqlInserir.setString(x++, obj.getVariavelTipoNota());					
					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlInserir.setInt(x++, obj.getUsuarioVO().getCodigo().intValue());					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		} 
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ForumRegistrarNotaVO obj, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj);
			final StringBuilder sql = new StringBuilder("UPDATE ForumRegistrarNota ");
			sql.append(" set forum = ?, pessoa=? , historico=?, nota=?, configuracaoacademiconotaconceito=?, ");
			sql.append(" variaveltiponota=?, dataRegistro=?, usuario=?, ");			
			sql.append(" notaHistorico=?, configuracaoacademiconotaconceitohistorico=?");			
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {				
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(x++, obj.getForumVO().getCodigo().intValue());
					sqlAlterar.setInt(x++, obj.getPessoaVO().getCodigo().intValue());
					sqlAlterar.setInt(x++, obj.getHistoricoVO().getCodigo().intValue());
					// nao pode utilizar isAtributoPreenchido do uteis pois a nota zero e valida.
					if(obj.getNota() != null){
						sqlAlterar.setDouble(x++, obj.getNota());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(Uteis.isAtributoPreenchido(obj.getNotaConceito())){
						sqlAlterar.setInt(x++, obj.getNotaConceito().getCodigo());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					sqlAlterar.setString(x++, obj.getVariavelTipoNota());
					sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataRegistro()));
					sqlAlterar.setInt(x++, obj.getUsuarioVO().getCodigo().intValue());
					// nao pode utilizar isAtributoPreenchido do uteis pois a nota zero e valida.
					if(obj.getNotaHistorico() != null){
						sqlAlterar.setDouble(x++, obj.getNotaHistorico());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(Uteis.isAtributoPreenchido(obj.getNotaConceitoHistorico())){
						sqlAlterar.setInt(x++, obj.getNotaConceitoHistorico().getCodigo());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		} finally {
			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarForumRegistrarNotaParaRegistarNotaHistorico(final ForumRegistrarNotaVO forumRegistrar, final UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE ForumRegistrarNota set notaHistorico = ?, configuracaoacademiconotaconceitohistorico = ? ");
			sb.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int x = 1;
					// nao pode utilizar isAtributoPreenchido do uteis pois a nota zero e valida.
					if(forumRegistrar.getNotaHistorico() != null){
						sqlAlterar.setDouble(x++, forumRegistrar.getNotaHistorico());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(Uteis.isAtributoPreenchido(forumRegistrar.getNotaConceitoHistorico())){
						sqlAlterar.setInt(x++, forumRegistrar.getNotaConceitoHistorico().getCodigo());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					sqlAlterar.setInt(x++, forumRegistrar.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getHistoricoFacade().alterarNotasHistoricoPorForum(forumRegistrar.getHistoricoVO().getCodigo(), forumRegistrar.getVariavelTipoNota(), forumRegistrar.getNota(), forumRegistrar.getNotaConceito().getCodigo(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override 
	public List<ForumRegistrarNotaVO> consultarPessoaInteracaoForumRapidaPorTurmaAnoSemestre(ForumVO forum, TurmaVO turma, String ano, String semestre, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		SqlRowSet tabelaResultado = null;
		List<ForumRegistrarNotaVO> listaForumRegistrarNotaVO = new ArrayList<ForumRegistrarNotaVO>(0);
		ForumRegistrarNotaVO obj = new ForumRegistrarNotaVO();
		try {
			sb.append(" select distinct pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", ");
			sb.append(" pessoa.cpf AS \"pessoa.cpf\", ");
			sb.append(" historico.codigo AS \"historico.codigo\", historico.matricula AS \"historico.matricula\",");
			sb.append(" configuracaoacademico.codigo AS \"configuracaoacademico.codigo\", configuracaoacademico.nome AS \"configuracaoacademico.nome\", ");
			sb.append(" mptd.codigo as \"mptd.codigo\", mptd.matriculaPeriodo as \"mptd.matriculaPeriodo\", mptd.turma as \"mptd.turma\", ");
			sb.append(" mptd.disciplina as \"mptd.disciplina\", mptd.conteudo as \"mptd.conteudo\", mptd.ano as \"mptd.ano\", ");
			sb.append(" mptd.modalidadeDisciplina as \"mptd.modalidadeDisciplina\", mptd.semestre as \"mptd.semestre\", mptd.matricula as \"mptd.matricula\", ");
			sb.append(" (count (case when excluido = true then 1 else 0 end)) as \"qtdRegistroForum\" ");
			sb.append(" from foruminteracao  ");
			sb.append(" inner join forum on forum.codigo = foruminteracao.forum ");
			sb.append(" inner join usuario on usuario.codigo = foruminteracao.usuariointeracao ");
			sb.append(" inner join pessoa on pessoa.codigo = usuario.pessoa and pessoa.aluno = true ");
			sb.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
			sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matricula = matricula.matricula and mptd.disciplina = forum.disciplina ");
			sb.append(" inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
			sb.append(" left join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo ");
			sb.append(" inner join configuracaoacademico on historico.configuracaoacademico = configuracaoacademico.codigo ");
			sb.append(" inner join turma on turma.codigo  = mptd.turma ");			
			sb.append(" where pessoa.codigo not in ( select pessoa from forumregistrarnota where forum =").append(forum.getCodigo()).append(" ) ");
			if (Uteis.isAtributoPreenchido(forum.getCodigo())) {
				sb.append(" and forum.codigo =  ").append(forum.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(forum.getDisciplina().getCodigo())) {
				sb.append(" and forum.disciplina = ").append(forum.getDisciplina().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(turma.getCodigo())) {
				sb.append(" and mptd.turma = ").append(turma.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sb.append(" and mptd.ano = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sb.append(" and mptd.semestre = '").append(semestre).append("' ");
			}
//			sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
			sb.append(" group by  pessoa.codigo, pessoa.nome, ");
			sb.append(" pessoa.cpf, ");
			sb.append(" historico.codigo, ");
			sb.append(" configuracaoacademico.codigo, configuracaoacademico.nome, ");
			sb.append(" mptd.codigo, mptd.matriculaPeriodo, mptd.turma, ");
			sb.append(" mptd.disciplina, mptd.conteudo, mptd.ano, ");
			sb.append(" mptd.modalidadeDisciplina, mptd.semestre, mptd.matricula ");
			sb.append(" ORDER BY pessoa.nome ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			while (tabelaResultado.next()) {
				obj = new ForumRegistrarNotaVO();
				obj.getPessoaVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
				obj.getPessoaVO().setNome(tabelaResultado.getString("pessoa.nome"));
				obj.getPessoaVO().setCPF(tabelaResultado.getString("pessoa.cpf"));
				obj.getHistoricoVO().setCodigo(tabelaResultado.getInt("historico.codigo"));
				obj.getHistoricoVO().getMatricula().setMatricula(tabelaResultado.getString("historico.matricula"));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setCodigo(new Integer(tabelaResultado.getInt("mptd.codigo")));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setMatriculaPeriodo(new Integer(tabelaResultado.getInt("mptd.matriculaPeriodo")));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurma().setCodigo(new Integer(tabelaResultado.getInt("mptd.turma")));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getDisciplina().setCodigo(new Integer(tabelaResultado.getInt("mptd.disciplina")));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getConteudo().setCodigo(new Integer(tabelaResultado.getInt("mptd.conteudo")));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setAno(tabelaResultado.getString("mptd.ano"));
				if(tabelaResultado.getString("mptd.modalidadeDisciplina") != null && !tabelaResultado.getString("mptd.modalidadeDisciplina").trim().isEmpty()){
					obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("mptd.modalidadeDisciplina")));
				}
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setSemestre(tabelaResultado.getString("mptd.semestre"));
				obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setMatricula(tabelaResultado.getString("mptd.matricula"));
				obj.getHistoricoVO().getConfiguracaoAcademico().setCodigo(tabelaResultado.getInt("configuracaoacademico.codigo"));
				obj.getHistoricoVO().getConfiguracaoAcademico().setNome(tabelaResultado.getString("configuracaoacademico.nome"));
				obj.setForumVO(forum);
				obj.setQtdRegistroForum(tabelaResultado.getInt("qtdRegistroForum"));
				listaForumRegistrarNotaVO.add(obj);
			}
		} finally {
			obj = null;
			tabelaResultado = null;
			sb = null;
		}		
		return listaForumRegistrarNotaVO;
	}
	
	private StringBuilder getSelectCompleto() {
		StringBuilder sb = new StringBuilder("SELECT forumRegistrarNota.codigo as  \"forumRegistrarNota.codigo\", forumRegistrarNota.pessoa as  \"forumRegistrarNota.pessoa\", forumRegistrarNota.forum as  \"forumRegistrarNota.forum\", ");
		sb.append(" forumRegistrarNota.nota as \"forumRegistrarNota.nota\", forumRegistrarNota.dataRegistro as \"forumRegistrarNota.dataRegistro\",");
		sb.append(" forumRegistrarNota.configuracaoacademiconotaconceito as \"forumRegistrarNota.configuracaoacademiconotaconceito\", forumRegistrarNota.variaveltiponota as \"forumRegistrarNota.variaveltiponota\",");
		sb.append(" forumRegistrarNota.usuario as \"forumRegistrarNota.usuario\", ");
		sb.append(" forumRegistrarNota.historico as \"forumRegistrarNota.historico\", ");	
		sb.append(" forumRegistrarNota.notahistorico as \"forumRegistrarNota.notahistorico\", ");
		sb.append(" forumRegistrarNota.configuracaoacademiconotaconceitohistorico as \"forumRegistrarNota.configuracaoacademiconotaconceitohistorico\", ");
		sb.append(" historico.matricula as \"historico.matricula\", ");
		sb.append(" pessoa.nome as \"pessoa.nome\", pessoa.cpf as \"pessoa.cpf\", ");
		sb.append(" usuario.nome as \"usuario.nome\", ");
		sb.append(" configuracaoacademico.codigo AS \"configuracaoacademico.codigo\", configuracaoacademico.nome AS \"configuracaoacademico.nome\", ");
		sb.append(" mptd.codigo as \"mptd.codigo\", mptd.matriculaPeriodo as \"mptd.matriculaPeriodo\", mptd.turma as \"mptd.turma\", ");
		sb.append(" mptd.disciplina as \"mptd.disciplina\", mptd.conteudo as \"mptd.conteudo\", mptd.ano as \"mptd.ano\", ");
		sb.append(" mptd.modalidadeDisciplina as \"mptd.modalidadeDisciplina\", mptd.semestre as \"mptd.semestre\", mptd.matricula as \"mptd.matricula\", ");
		sb.append(" ( select count (fi.codigo) from foruminteracao as fi inner join usuario on usuario.codigo = fi.usuariointeracao ");
		sb.append("   where fi.excluido = false and usuario.pessoa = forumRegistrarNota.pessoa and fi.forum = forumRegistrarNota.forum ");
		sb.append(" ) as \"forumRegistrarNota.qtdRegistroForum\" ");
		
		sb.append(" FROM forumRegistrarNota ");
		
		return sb;
	}
	
	@Override
	public List<ForumRegistrarNotaVO> consultarForumRegistrarNotaRapidaPorTurmaAnoSemestre(ForumVO forum, TurmaVO turma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		SqlRowSet tabelaResultado = null;
		try {
			sb.append(getSelectCompleto());
			sb.append(" inner join forum on forum.codigo = forumRegistrarNota.forum ");			
			sb.append(" inner join pessoa on pessoa.codigo = forumRegistrarNota.pessoa ");
			sb.append(" inner join usuario on usuario.codigo = forumRegistrarNota.usuario ");
			sb.append(" inner join historico on historico.codigo = forumRegistrarNota.historico ");
			sb.append(" inner join matricula on matricula.matricula = historico.matricula ");
			sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.codigo = historico.matriculaperiodoturmadisciplina ");			
			sb.append(" left join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo ");
			sb.append(" inner join configuracaoacademico on historico.configuracaoacademico = configuracaoacademico.codigo ");
			sb.append(" inner join turma on turma.codigo  = mptd.turma ");
			sb.append(" where 1=1 ");
			if (Uteis.isAtributoPreenchido(forum.getCodigo())) {
				sb.append(" and forum.codigo =  ").append(forum.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(forum.getDisciplina().getCodigo())) {
				sb.append(" and forum.disciplina = ").append(forum.getDisciplina().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(turma.getCodigo())) {
				sb.append(" and mptd.turma = ").append(turma.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(ano)) {
				sb.append(" and mptd.ano = '").append(ano).append("' ");
			}
			if (Uteis.isAtributoPreenchido(semestre)) {
				sb.append(" and mptd.semestre = '").append(semestre).append("' ");
			}
//			sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
			sb.append(" ORDER BY pessoa.nome ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		} finally {
			tabelaResultado = null;
			sb = null;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<ForumRegistrarNotaVO> consultarForumRegistrarNota(Integer forum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		StringBuilder sb = getSelectCompleto();
		sb.append(" inner join forum on forum.codigo = forumRegistrarNota.forum ");
		sb.append(" inner join pessoa on pessoa.codigo = forumRegistrarNota.pessoa ");
		sb.append(" inner join usuario on usuario.codigo = forumRegistrarNota.usuario ");
		sb.append(" inner join historico on historico.codigo = forumRegistrarNota.historico ");
		sb.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matricula = matricula.matricula and mptd.disciplina = forum.disciplina ");			
		sb.append(" left join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo ");
		sb.append(" inner join configuracaoacademico on historico.configuracaoacademico = configuracaoacademico.codigo ");
		sb.append(" inner join turma on turma.codigo  = mptd.turma ");
		sb.append(" WHERE forumRegistrarNota.forum = ?");
		try {
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { forum.intValue() });
			return montarDadosConsulta(resultado,  nivelMontarDados, usuario);
		} finally {
			sb = null;
			resultado = null;
		}
	}
	

	private List<ForumRegistrarNotaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ForumRegistrarNotaVO> ForumRegistrarNotaVOs = new ArrayList<ForumRegistrarNotaVO>(0);
		while (rs.next()) {
			ForumRegistrarNotaVOs.add(montarDados(rs,  nivelMontarDados, usuarioLogado));
		}
		return ForumRegistrarNotaVOs;
	}

	private ForumRegistrarNotaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ForumRegistrarNotaVO obj = new ForumRegistrarNotaVO();
		obj.setCodigo(rs.getInt("forumRegistrarNota.codigo"));
		obj.getForumVO().setCodigo(rs.getInt("forumRegistrarNota.forum"));
		obj.getPessoaVO().setCodigo(rs.getInt("forumRegistrarNota.pessoa"));
		obj.getPessoaVO().setNome(rs.getString("pessoa.nome"));
		obj.getPessoaVO().setCPF(rs.getString("pessoa.cpf"));
		obj.getUsuarioVO().setCodigo(rs.getInt("forumRegistrarNota.usuario"));
		obj.getUsuarioVO().setNome(rs.getString("usuario.nome"));
		obj.setDataRegistro(rs.getTimestamp("forumRegistrarNota.dataRegistro"));
		if(rs.getObject("forumRegistrarNota.nota") != null){
			obj.setNota(rs.getDouble("forumRegistrarNota.nota"));	
		}
		if(rs.getObject("forumRegistrarNota.notaHistorico") != null){
			obj.setNotaHistorico(rs.getDouble("forumRegistrarNota.notaHistorico"));	
		}
		obj.setVariavelTipoNota(rs.getString("forumRegistrarNota.variaveltiponota"));		
		obj.setQtdRegistroForum(rs.getInt("forumRegistrarNota.qtdRegistroForum"));
		obj.getNotaConceito().setCodigo(rs.getInt("forumRegistrarNota.configuracaoacademiconotaconceito"));
		obj.getNotaConceitoHistorico().setCodigo(rs.getInt("forumRegistrarNota.configuracaoacademiconotaconceitohistorico"));
		obj.getHistoricoVO().setCodigo(rs.getInt("forumRegistrarNota.historico"));
		obj.getHistoricoVO().getMatricula().setMatricula(rs.getString("historico.matricula"));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setCodigo(new Integer(rs.getInt("mptd.codigo")));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setMatriculaPeriodo(new Integer(rs.getInt("mptd.matriculaPeriodo")));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getTurma().setCodigo(new Integer(rs.getInt("mptd.turma")));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getDisciplina().setCodigo(new Integer(rs.getInt("mptd.disciplina")));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().getConteudo().setCodigo(new Integer(rs.getInt("mptd.conteudo")));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setAno(rs.getString("mptd.ano"));
		if(rs.getString("mptd.modalidadeDisciplina") != null && !rs.getString("mptd.modalidadeDisciplina").trim().isEmpty()){
			obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(rs.getString("mptd.modalidadeDisciplina")));
		}
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setSemestre(rs.getString("mptd.semestre"));
		obj.getHistoricoVO().getMatriculaPeriodoTurmaDisciplina().setMatricula(rs.getString("mptd.matricula"));
		obj.getHistoricoVO().getConfiguracaoAcademico().setCodigo(rs.getInt("configuracaoacademico.codigo"));
		obj.getHistoricoVO().getConfiguracaoAcademico().setNome(rs.getString("configuracaoacademico.nome"));
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ForumRegistrarNota.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ForumRegistrarNota.idEntidade = idEntidade;
	}
	
	@Override
	public List<SelectItem> consultarVariavelTituloJaRegistradasForumRegistroNotaPorForumTurmaAnoSemestre(List<SelectItem> listaSelectItemTipoInformarNota, ForumVO forum, TurmaVO turma, String ano, String semestre,  UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct configuracaoacademiconota.codigo, forumregistrarnota.variaveltiponota, configuracaoacademiconota.titulo ");
		sb.append(" from forumregistrarnota ");
		sb.append(" inner join forum on forum.codigo = forumregistrarnota.forum ");
		sb.append(" inner join historico on historico.codigo = forumregistrarnota.historico ");
		sb.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.codigo = historico.matriculaperiodoturmadisciplina ");
		sb.append(" inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
		sb.append(" inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = configuracaoacademico.codigo ");
		sb.append(" and configuracaoacademiconota.nota = ('NOTA_'||forumregistrarnota.variaveltiponota) ");
		sb.append(" WHERE 1=1 ");
		if (Uteis.isAtributoPreenchido(forum.getCodigo())) {
			sb.append(" and forum.codigo =  ").append(forum.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(forum.getDisciplina().getCodigo())) {
			sb.append(" and forum.disciplina = ").append(forum.getDisciplina().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(turma.getCodigo())) {
			sb.append(" and mptd.turma = ").append(turma.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sb.append(" and mptd.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sb.append(" and mptd.semestre = '").append(semestre).append("' ");
		}
		if (!listaSelectItemTipoInformarNota.isEmpty()) {
			sb.append(listaSelectItemTipoInformarNota.stream().map(SelectItem::getValue).map(String::valueOf).collect(Collectors.joining("', '", " and forumregistrarnota.variaveltiponota not in('", "') ")));
		}
		sb.append("order by configuracaoacademiconota.titulo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<SelectItem> lista = new ArrayList<SelectItem>(0);
		while (tabelaResultado.next()) {
			lista.add(new SelectItem(tabelaResultado.getString("variaveltiponota"), tabelaResultado.getString("titulo")));
		}
		return lista;
	}
	
	private void validarForumRegistrarNota(ForumRegistrarNotaVO forumRegistrar, HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO) throws Exception {
		if (!configuracaoAcademicaNotaVO.getUtilizarNotaPorConceito()) {
			forumRegistrar.setNotaConceito(new ConfiguracaoAcademicoNotaConceitoVO());
		}
		if (Uteis.isAtributoPreenchido(forumRegistrar.getNota())) {
			ConfiguracaoAcademicoVO conf = mapaConfiguracoesAcademicos.get(forumRegistrar.getHistoricoVO().getConfiguracaoAcademico().getCodigo());
			UtilReflexao.invocarMetodo(forumRegistrar.getHistoricoVO(),	"setNota" + forumRegistrar.getVariavelTipoNota(), forumRegistrar.getNota());
			getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(forumRegistrar.getHistoricoVO(), null, conf, Integer.parseInt(forumRegistrar.getVariavelTipoNota()));
		}
	}
	
	private void validarForumRegistrarNotaParaRegistrarNotaHistorico(List<ForumRegistrarNotaVO> forumRegistrarNotaVOs) throws Exception {
		Predicate<ForumRegistrarNotaVO> isAtributoPreenchido = Uteis::isAtributoPreenchido;
		if (forumRegistrarNotaVOs.stream().anyMatch(isAtributoPreenchido.negate())) {
			throw new Exception("Há registros que não foram previamente salvos. Por favor, clique no gravar antes de registrar a(s) nota(s).");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarForumRegistrarNotaParaRegistrarNotaHistorico(List<ForumRegistrarNotaVO> forumRegistrarNotaVOs, HashMap<Integer, ConfiguracaoAcademicoVO> mapaConfiguracoesAcademicos, ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(forumRegistrarNotaVOs)) {
			validarForumRegistrarNotaParaRegistrarNotaHistorico(forumRegistrarNotaVOs);
			for (ForumRegistrarNotaVO forumRegistrar : forumRegistrarNotaVOs) {
				validarForumRegistrarNota(forumRegistrar, mapaConfiguracoesAcademicos, configuracaoAcademicaNotaVO);
				forumRegistrar.setNotaHistorico(forumRegistrar.getNota());
				forumRegistrar.setNotaConceitoHistorico(forumRegistrar.getNotaConceito());
				alterarForumRegistrarNotaParaRegistarNotaHistorico(forumRegistrar, usuarioVO);
			}
		}
	}
}
