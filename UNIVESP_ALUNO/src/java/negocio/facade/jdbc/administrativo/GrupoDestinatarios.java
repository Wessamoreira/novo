package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

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

import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Repository
@Scope("singleton")
@Lazy
public class GrupoDestinatarios extends ControleAcesso  {

	protected static String idEntidade;

	public GrupoDestinatarios() throws Exception {
		super();
		setIdEntidade("GrupoDestinatarios");
	}

	public GrupoDestinatariosVO novo() throws Exception {
		GrupoDestinatarios.incluir(getIdEntidade());
		GrupoDestinatariosVO obj = new GrupoDestinatariosVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final GrupoDestinatariosVO obj) throws Exception {

		final String sql = "INSERT INTO GrupoDestinatarios ( dataCadastro, responsavelCadastro, nomeGrupo ) VALUES ( ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataCadastro()));
				if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getResponsavelCadastro().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setString(3, obj.getNomeGrupo());
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
		getFacadeFactory().getFuncionarioGrupoDestinatariosFacade().incluirListaFuncionarioGrupoDestinatarios(obj.getCodigo(), obj.getListaFuncionariosGrupoDestinatariosVOs());
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GrupoDestinatariosVO obj) throws Exception {
		final String sql = "UPDATE GrupoDestinatarios set dataCadastro = ?, responsavelCadastro = ?, nomeGrupo = ? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataCadastro()));
				if (obj.getResponsavelCadastro().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getResponsavelCadastro().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setString(3, obj.getNomeGrupo());
				sqlAlterar.setInt(4, obj.getCodigo());
				return sqlAlterar;
			}
		});
		getFacadeFactory().getFuncionarioGrupoDestinatariosFacade().alterarGrupoDestinatarioss(obj.getCodigo(), obj.getListaFuncionariosGrupoDestinatariosVOs());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GrupoDestinatariosVO obj) throws Exception {
		GrupoDestinatarios.excluir(getIdEntidade());
		getFacadeFactory().getFuncionarioGrupoDestinatariosFacade().excluirGrupoDestinatarios(obj.getListaFuncionariosGrupoDestinatariosVOs());
		String sql = "DELETE FROM GrupoDestinatarios WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public GrupoDestinatariosVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDestinatarios gd  WHERE codigo = " + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new GrupoDestinatariosVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GrupoDestinatariosVO> consultarPorCodigo(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDestinatarios gd  WHERE codigo >= " + codigo;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GrupoDestinatariosVO> consultarPorNomeGrupo(String nomeGrupo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDestinatarios gd  WHERE LOWER(nomeGrupo) LIKE '" + nomeGrupo.toLowerCase() + "%'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<GrupoDestinatariosVO> consultarPorResponsavelCadastro(Integer codigoResponsavelCadastro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM GrupoDestinatarios gd  WHERE responsavelCadastro = " + codigoResponsavelCadastro;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public  List<GrupoDestinatariosVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GrupoDestinatariosVO> vetResultado = new ArrayList<GrupoDestinatariosVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public  GrupoDestinatariosVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GrupoDestinatariosVO obj = new GrupoDestinatariosVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
		obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
		obj.setNomeGrupo(dadosSQL.getString("nomeGrupo"));
		montarDadosResponsavelCadastro(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
                obj.setListaFuncionariosGrupoDestinatariosVOs(getFacadeFactory().getFuncionarioGrupoDestinatariosFacade().consultarPorCodigoGrupoDestinatarios(obj.getCodigo(), nivelMontarDados, usuario));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	public  void montarDadosResponsavelCadastro(GrupoDestinatariosVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelCadastro().getCodigo().intValue() == 0) {
			obj.setResponsavelCadastro(new UsuarioVO());
			return;
		}
		obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), nivelMontarDados, usuario));
	}

	public static String getIdEntidade() {
		return GrupoDestinatarios.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		GrupoDestinatarios.idEntidade = idEntidade;
	}
	
	public List<SelectItem> consultarDadosListaSelectItem(Obrigatorio obrigatorio){
	    String sqlStr = "SELECT codigo, nomeGrupo FROM GrupoDestinatarios gd ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List<SelectItem> selectItems = new ArrayList<SelectItem>(0);
        if(obrigatorio.equals(Obrigatorio.NAO)){
            selectItems.add(new SelectItem(0, ""));
        }
        while(tabelaResultado.next()){
            selectItems.add(new SelectItem(tabelaResultado.getInt("codigo"), tabelaResultado.getString("nomeGrupo")));
        }
        return selectItems;
	}
	
	
	public GrupoDestinatariosVO consultarGrupoDestinatarioQuandoOuvidoriaForMalAvaliadaPorUnidadeEnsino(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM GrupoDestinatarios where codigo = ( "); 
		sqlStr.append(" SELECT grupoDestinatarioQuandoOuvidoriaForMalAvaliada FROM ConfiguracaoAtendimento  ");
		sqlStr.append(" inner join configuracaoatendimentounidadeensino on configuracaoatendimentounidadeensino.configuracaoatendimento = configuracaoatendimento.codigo  ");
		sqlStr.append(" inner join unidadeensino on configuracaoatendimentounidadeensino.unidadeensino = unidadeensino.codigo  ");
		sqlStr.append(" WHERE unidadeensino.codigo = ").append(codigo);
		sqlStr.append(" ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new GrupoDestinatariosVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	public SqlRowSet consultarGrupoDestinatariosNotificacaoDuvidasNaoRespondidasProfessor() throws Exception {
		
		StringBuilder sqlStr = new StringBuilder();
		
		sqlStr.append(" select destinatario.codigo as destinatario, destinatario.nome as nomedestinatario, destinatario.email as emaildestinatario,");
		sqlStr.append(" aluno.nome as nomealuno, aluno.codigo as aluno, professor.nome as nomeprofessor, notificacaogrupodestinatariodiasduvidasnaorespondidas, duvidaprofessor.dataalteracao, matricula.unidadeensino as unidadeensino");
		sqlStr.append(" from duvidaprofessor");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and duvidaprofessor.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" and duvidaprofessor.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno");
		sqlStr.append(" inner join turma on turma.codigo = duvidaprofessor.turma");
		sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead");
		sqlStr.append(" inner join grupodestinatarios on grupodestinatarios.codigo = configuracaoead.grupodestinatarios");
		sqlStr.append(" inner join funcionariogrupodestinatarios on funcionariogrupodestinatarios.grupodestinatarios = grupodestinatarios.codigo");
		sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariogrupodestinatarios.funcionario");
		sqlStr.append(" inner join pessoa as destinatario on destinatario.codigo = funcionario.pessoa");
		sqlStr.append(" inner join pessoa as professor on professor.codigo = matriculaperiodoturmadisciplina.professor");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem on mensagem.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO_PROFESSOR_DUVIDAS_NAO_RESPONDIDAS'");
		sqlStr.append(" where situacaoduvidaprofessor in ('AGUARDANDO_RESPOSTA_PROFESSOR' , 'NOVA')");
		sqlStr.append(" and notificarprofessorduvidasnaorespondidas");
		sqlStr.append(" and (");
		sqlStr.append(" EXTRACT( DAYS FROM (current_timestamp-dataalteracao)) =  notificacaogrupodestinatariodiasduvidasnaorespondidas and mensagem.desabilitarEnvioMensagemAutomatica = false");
		sqlStr.append(" )");
		sqlStr.append(" union");
		sqlStr.append(" (select destinatario.codigo as destinatario, destinatario.nome as nomedestinatario, destinatario.email as emaildestinatario,");
		sqlStr.append(" aluno.nome as nomealuno, aluno.codigo as aluno, professor.nome as nomeprofessor, notificacaogrupodestinatariodiasduvidasnaorespondidas, duvidaprofessor.dataalteracao, matricula.unidadeensino as unidadeensino");
		sqlStr.append(" from duvidaprofessor");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and duvidaprofessor.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" and duvidaprofessor.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno");
		sqlStr.append(" inner join turma on turma.codigo = duvidaprofessor.turma");
		sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join cursocoordenador on cursocoordenador.curso = curso.codigo and (cursocoordenador.turma = turma.codigo or cursocoordenador.turma is null)");
		sqlStr.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario");
		sqlStr.append(" inner join pessoa as destinatario on destinatario.codigo = funcionario.pessoa");
		sqlStr.append(" inner join pessoa as professor on professor.codigo = matriculaperiodoturmadisciplina.professor");
		sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem on mensagem.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO_PROFESSOR_DUVIDAS_NAO_RESPONDIDAS'");
		sqlStr.append(" where situacaoduvidaprofessor in ('AGUARDANDO_RESPOSTA_PROFESSOR' , 'NOVA')");
		sqlStr.append(" and notificarprofessorduvidasnaorespondidas");
		sqlStr.append(" and notificarcoodenadorduvidasprofessornaorespondidas");
		sqlStr.append(" and (");
		sqlStr.append(" EXTRACT( DAYS FROM (current_timestamp-dataalteracao)) =  notificacaogrupodestinatariodiasduvidasnaorespondidas and mensagem.desabilitarEnvioMensagemAutomatica = false");
		sqlStr.append(" )");
		sqlStr.append(" order by case when cursocoordenador.turma is not null then 1 else 2 end limit 1)");
		
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}
}