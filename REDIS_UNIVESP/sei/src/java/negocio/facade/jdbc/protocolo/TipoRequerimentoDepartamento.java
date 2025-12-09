package negocio.facade.jdbc.protocolo;

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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.PossivelResponsavelRequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoFuncionarioVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoSituacaoDepartamentoVO;
import negocio.comuns.protocolo.TipoRequerimentoUnidadeEnsinoVO;
import negocio.comuns.protocolo.enumeradores.TipoDistribuicaoResponsavelEnum;
import negocio.comuns.protocolo.enumeradores.TipoPoliticaDistribuicaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Curso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.protocolo.TipoRequerimentoDepartamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TipoRequerimentoDepartamentoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>TipoRequerimentoDepartamentoVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see TipoRequerimentoDepartamentoVO
 * @see ControleAcesso
 * @see Curso
 */

@Repository
@Scope("singleton")
@Lazy
public class TipoRequerimentoDepartamento extends ControleAcesso implements TipoRequerimentoDepartamentoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2563675958470104185L;
	protected static String idEntidade;

	public TipoRequerimentoDepartamento() throws Exception {
		super();
		setIdEntidade("TipoRequerimentoDepartamento");
	}

	public TipoRequerimentoDepartamentoVO novo() throws Exception {
		TipoRequerimentoDepartamento.incluir(getIdEntidade());
		TipoRequerimentoDepartamentoVO obj = new TipoRequerimentoDepartamentoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoRequerimentoDepartamentoVO obj, UsuarioVO usuario) throws Exception {
		TipoRequerimentoDepartamentoVO.validarDados(obj);
		final String sql = "INSERT INTO TipoRequerimentoDepartamento( " + "tipoRequerimento, departamento, responsavelRequerimentoDepartamento, prazoExecucao, ordemExecucao, observacaoObrigatoria, podeIndeferirRequerimento, tipoDistribuicaoResponsavel, tipoPoliticaDistribuicao, cargo, questionario, orientacaoDepartamento, podeInserirNota, notaMaxima ) VALUES ( ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				int i = 1;
				sqlInserir.setInt(i++, obj.getTipoRequerimento().intValue());
				sqlInserir.setInt(i++, obj.getDepartamento().getCodigo().intValue());
				if (!obj.getResponsavelRequerimentoDepartamento().getCodigo().equals(0)) {
					sqlInserir.setInt(i++, obj.getResponsavelRequerimentoDepartamento().getCodigo().intValue());
				} else {
					sqlInserir.setNull(i++, 0);
				}
				sqlInserir.setInt(i++, obj.getPrazoExecucao());
				sqlInserir.setInt(i++, obj.getOrdemExecucao());
				sqlInserir.setBoolean(i++, obj.getObservacaoObrigatoria());
				sqlInserir.setBoolean(i++, obj.getPodeIndeferirRequerimento());
				sqlInserir.setString(i++, obj.getTipoDistribuicaoResponsavel().name());
				sqlInserir.setString(i++, obj.getTipoPoliticaDistribuicao().name());
				if (!obj.getCargo().getCodigo().equals(0)) {
					sqlInserir.setInt(i++, obj.getCargo().getCodigo().intValue());
				} else {
					sqlInserir.setNull(i++, 0);
				}
				if (!obj.getQuestionario().getCodigo().equals(0)) {
					sqlInserir.setInt(i++, obj.getQuestionario().getCodigo().intValue());
				} else {
					sqlInserir.setNull(i++, 0);
				}	 
				sqlInserir.setString(i++, obj.getOrientacaoDepartamento());
				
				Uteis.setValuePreparedStatement(obj.getPodeInserirNota(), i++, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getNotaMaxima(), i++, sqlInserir);
				
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
		getFacadeFactory().getTipoRequerimentoDepartamentoFuncionarioFacade().incluirListaTipoRequerimentoDepartamentoFuncionarioVO(obj, usuario);
		getFacadeFactory().getTipoRequerimentoSituacaoDepartamentoFacade().incluirListaTipoRequerimentoSituacaoDepartamento(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoRequerimentoDepartamentoVO obj, UsuarioVO usuario) throws Exception {
		TipoRequerimentoDepartamentoVO.validarDados(obj);
		final String sql = "UPDATE TipoRequerimentoDepartamento SET " + "tipoRequerimento = ?, departamento=?, responsavelRequerimentoDepartamento=?, " + "prazoExecucao=?, ordemExecucao=?, observacaoObrigatoria=?, podeIndeferirRequerimento=?,  tipoDistribuicaoResponsavel = ?, tipoPoliticaDistribuicao = ?, cargo = ?, questionario = ?, orientacaoDepartamento = ?, podeInserirNota = ?, notaMaxima = ? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

				int i = 1;
				sqlAlterar.setInt(i++, obj.getTipoRequerimento().intValue());
				sqlAlterar.setInt(i++, obj.getDepartamento().getCodigo().intValue());
				if (!obj.getResponsavelRequerimentoDepartamento().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getResponsavelRequerimentoDepartamento().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setInt(i++, obj.getPrazoExecucao());
				sqlAlterar.setInt(i++, obj.getOrdemExecucao());
				sqlAlterar.setBoolean(i++, obj.getObservacaoObrigatoria());
				sqlAlterar.setBoolean(i++, obj.getPodeIndeferirRequerimento());
				sqlAlterar.setString(i++, obj.getTipoDistribuicaoResponsavel().name());
				sqlAlterar.setString(i++, obj.getTipoPoliticaDistribuicao().name());
				if (!obj.getCargo().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getCargo().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				if (!obj.getQuestionario().getCodigo().equals(0)) {
					sqlAlterar.setInt(i++, obj.getQuestionario().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(i++, 0);
				}
				sqlAlterar.setString(i++, obj.getOrientacaoDepartamento());
				Uteis.setValuePreparedStatement(obj.getPodeInserirNota(), i++, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getNotaMaxima(), i++, sqlAlterar);
				sqlAlterar.setInt(i++, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		})==0){
			incluir(obj, usuario);
			return;
		};
		getFacadeFactory().getTipoRequerimentoDepartamentoFuncionarioFacade().alterarListaTipoRequerimentoDepartamentoFuncionarioVO(obj, usuario);
		getFacadeFactory().getTipoRequerimentoSituacaoDepartamentoFacade().alterarListaTipoRequerimentoSituacaoDepartamento(obj, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoRequerimentoDepartamentoVO obj, UsuarioVO usuario) throws Exception {		
		String sql = "DELETE FROM TipoRequerimentoDepartamento WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List<TipoRequerimentoDepartamentoVO> consultarPorCodigoTipoRequerimento(Integer codigoTipoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSelectCompleto();
		sql.append(" WHERE TipoRequerimentoDepartamento.tipoRequerimento = ? ORDER BY ordemExecucao");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoTipoRequerimento);
		return montarDadosConsulta(tabelaResultado, usuario);
	}
	
	public StringBuilder getSelectCompleto(){
		StringBuilder sql = new StringBuilder("SELECT TipoRequerimentoDepartamento.*, ");
		sql.append(" Departamento.nome AS \"departamento.nome\", Departamento.responsavel AS \"departamento.responsavel\",  ");
		sql.append(" Pessoa.nome AS \"responsavelRequerimentoDepartamento.nome\",  ");
		sql.append(" cargo.nome AS \"cargo.nome\", questionario.descricao as \"questionario.descricao\"  ");
		sql.append(" from TipoRequerimentoDepartamento  ");
		sql.append(" INNER JOIN TipoRequerimento ON (TipoRequerimento.codigo = TipoRequerimentoDepartamento.tipoRequerimento)  ");
		sql.append(" LEFT JOIN Departamento ON (Departamento.codigo = TipoRequerimentoDepartamento.departamento) ");
		sql.append(" LEFT JOIN Pessoa ON (Pessoa.codigo = TipoRequerimentoDepartamento.responsavelRequerimentoDepartamento) ");
		sql.append(" LEFT JOIN Cargo ON (Cargo.codigo = TipoRequerimentoDepartamento.cargo) ");
		sql.append(" LEFT JOIN Questionario ON (Questionario.codigo = TipoRequerimentoDepartamento.Questionario) ");		
		return sql;
	}

	public TipoRequerimentoDepartamentoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSelectCompleto();
		sql.append(" WHERE TipoRequerimentoDepartamento.codigo = ? ORDER BY ordemExecucao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Tipo Requerimento Departamento).");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	public static List<TipoRequerimentoDepartamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<TipoRequerimentoDepartamentoVO> vetResultado = new ArrayList<TipoRequerimentoDepartamentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public static TipoRequerimentoDepartamentoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		TipoRequerimentoDepartamentoVO obj = new TipoRequerimentoDepartamentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTipoRequerimento(new Integer(dadosSQL.getInt("tipoRequerimento")));
		if (dadosSQL.getString("tipoDistribuicaoResponsavel") != null) {
			obj.setTipoDistribuicaoResponsavel(TipoDistribuicaoResponsavelEnum.valueOf(dadosSQL.getString("tipoDistribuicaoResponsavel")));
		}
		if (dadosSQL.getString("tipoPoliticaDistribuicao") != null) {
			obj.setTipoPoliticaDistribuicao(TipoPoliticaDistribuicaoEnum.valueOf(dadosSQL.getString("tipoPoliticaDistribuicao")));
		}
		obj.getCargo().setCodigo(new Integer(dadosSQL.getInt("cargo")));
		obj.getCargo().setNome(dadosSQL.getString("cargo.nome"));
		obj.getDepartamento().setCodigo(new Integer(dadosSQL.getInt("departamento")));
		obj.getDepartamento().setNome(dadosSQL.getString("departamento.nome"));
		obj.getDepartamento().getResponsavel().setCodigo(new Integer(dadosSQL.getInt("departamento.responsavel")));
		obj.getResponsavelRequerimentoDepartamento().setCodigo(new Integer(dadosSQL.getInt("responsavelRequerimentoDepartamento")));
		obj.getResponsavelRequerimentoDepartamento().setNome(dadosSQL.getString("responsavelRequerimentoDepartamento.nome"));
		obj.setOrdemExecucao(new Integer(dadosSQL.getInt("ordemExecucao")));
		obj.setPrazoExecucao(new Integer(dadosSQL.getInt("prazoExecucao")));
		obj.setObservacaoObrigatoria(dadosSQL.getBoolean("observacaoObrigatoria"));
		obj.setPodeIndeferirRequerimento(dadosSQL.getBoolean("podeIndeferirRequerimento"));
		obj.setOrientacaoDepartamento(dadosSQL.getString("orientacaoDepartamento"));
		obj.getQuestionario().setCodigo(dadosSQL.getInt("questionario"));
		obj.getQuestionario().setDescricao(dadosSQL.getString("questionario.descricao"));
		obj.setPodeInserirNota(dadosSQL.getBoolean("podeInserirNota"));
		obj.setNotaMaxima(dadosSQL.getDouble("notaMaxima"));
		if(obj.getTipoDistribuicaoResponsavel().equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO)) {
			obj.setTipoRequerimentoDepartamentoFuncionarioVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFuncionarioFacade().consultarPorTipoRequerimentoDepartamento(obj.getCodigo(), usuario));
		}
		obj.setTipoRequerimentoSituacaoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoSituacaoDepartamentoFacade().consultarPorTipoRequerimentoDepartamento(obj.getCodigo()));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	public void montarDadosQuestionario(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, UsuarioVO usuarioVO) throws Exception{
		if(tipoRequerimentoDepartamentoVO.getQuestionario().getCodigo() > 0){
			tipoRequerimentoDepartamentoVO.setQuestionario(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(tipoRequerimentoDepartamentoVO.getQuestionario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTipoRequerimentoDepartamentoVOs(Integer tipoRequerimento, List<TipoRequerimentoDepartamentoVO> objetos,  UsuarioVO usuario) throws Exception {
		TipoRequerimentoDepartamento.excluir(getIdEntidade());
		String sql = "DELETE FROM TipoRequerimentoDepartamento WHERE (tipoRequerimento = ?) and codigo not in (0 ";
		for(TipoRequerimentoDepartamentoVO obj:objetos){
			sql += ", "+obj.getCodigo();
		}
		sql += ") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { tipoRequerimento });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTipoRequerimentoDepartamentoVOs(Integer tipoRequerimento, List<TipoRequerimentoDepartamentoVO> objetos, UsuarioVO usuario) throws Exception {
		excluirTipoRequerimentoDepartamentoVOs(tipoRequerimento, objetos, usuario);
		int ordem = 1;
		for(TipoRequerimentoDepartamentoVO obj:objetos){
			obj.setTipoRequerimento(tipoRequerimento);
			obj.setOrdemExecucao(ordem);
			ordem += 1;
			if(obj.getNovoObj()){
				incluir(obj, usuario);
			}else{
				alterar(obj, usuario);
			}
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirTipoRequerimentoDepartamentoVOs(Integer tipoRequerimento, List<TipoRequerimentoDepartamentoVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator<TipoRequerimentoDepartamentoVO> e = objetos.iterator();
		int ordem = 1;
		while (e.hasNext()) {
			TipoRequerimentoDepartamentoVO obj = (TipoRequerimentoDepartamentoVO) e.next();
			obj.setTipoRequerimento(tipoRequerimento);
			obj.setOrdemExecucao(ordem);
			ordem += 1;
			incluir(obj, usuario);
		}
	}

	public static String getIdEntidade() {
		return TipoRequerimentoDepartamento.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		TipoRequerimentoDepartamento.idEntidade = idEntidade;
	}

	@Override
	public List<PossivelResponsavelRequerimentoVO> consultarPossiveisResponsaveisTipoRequerimentoPorDepartamentoEUnidadeEnsino(Integer departamento, Integer cargo, Integer pessoaEspecifica,  List<TipoRequerimentoDepartamentoFuncionarioVO> tipoRequerimentoDepartamentoFuncionarioVOs, TipoDistribuicaoResponsavelEnum tipoDistribuicaoResponsavel, List<TipoRequerimentoUnidadeEnsinoVO> tipoRequerimentoUnidadeEnsinoVOs, Integer limit, Integer offset) throws Exception {
		if (departamento == null || departamento == 0) {
			throw new ConsistirException("O campo DEPARTAMENTO (TipoRequerimentoDepartamento) deve ser informado.");
		}
		if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO) && (cargo == null || cargo == 0)) {
			throw new ConsistirException("O campo CARGO (TipoRequerimentoDepartamento) deve ser informado.");
		}
		if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO) && (pessoaEspecifica == null || pessoaEspecifica == 0)) {
			throw new ConsistirException("O campo FUNCIONÁRIO ESPECÍFICO (TipoRequerimentoDepartamento) deve ser informado.");
		}
		if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO) && (tipoRequerimentoDepartamentoFuncionarioVOs == null || tipoRequerimentoDepartamentoFuncionarioVOs.isEmpty())) {
			throw new ConsistirException("O campo LISTA DE FUNCIONÁRIOS (TipoRequerimentoDepartamento) deve ser informado.");
		}			
		
		StringBuilder sql = new StringBuilder("select distinct unidadeensino.codigo, unidadeensino.nome as unidadeensino, ");
		if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO)) {
			sql.append(" '' as curso, (select nome from pessoa where codigo = "+pessoaEspecifica+") as responsavel, '' as responsavelGerente, '' as responsavelMatriz, '' as responsavelMatrizDepartamento ");
		}else if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_ESPECIFICO_NO_TRAMITE)) {
				sql.append(" '' as curso, 'Será Definido ao Realizar o Trâmite' as responsavel, '' as responsavelGerente, '' as responsavelMatriz, '' as responsavelMatrizDepartamento ");
		}else if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO)) {
			sql.append(" '' as curso, '' as responsavel, '' as responsavelGerente, '' as responsavelMatriz, '' as responsavelMatrizDepartamento ");
		}else if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO)  || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE)) {
			sql.append(" curso.nome as curso,  ");
			/**
			 * Busca o coordenador da unidade de ensino em questão 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join cursocoordenador on cursocoordenador.funcionario = funcionario.codigo ");
			sql.append(" inner join curso c on cursocoordenador.curso = c.codigo ");
			sql.append(" where pessoa.ativo = true and  unidadeensinocurso.unidadeensino = unidadeensino.codigo and  cursocoordenador.curso = curso.codigo order by nome	");
			sql.append(" ), ', ') as   responsavel,  ");
			/**
			 * Busca o gerente da unidade de ensino em questão - Usado caso não encontre a opção acima 
			 */
			sql.append(" array_to_string(array( ");			
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = unidadeensino.codigo	");
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.gerente = true and funcionariocargo.ativo	");
			sql.append("  order by nome	 ), ', ') as   responsavelGerente, ");
			/**
			 * Busca o coordenador da unidade de ensino matriz  - Usado caso não encontre as opções acima
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join cursocoordenador on cursocoordenador.funcionario = funcionario.codigo ");
			sql.append(" inner join curso on cursocoordenador.curso = curso.codigo ");
			sql.append(" where pessoa.ativo = true and  unidadeensinocurso.unidadeensino = (select codigo from unidadeensino where matriz = true limit 1 ) and  cursocoordenador.curso = curso.codigo order by nome	");
			sql.append(" ), ', ') as   responsavelMatriz,  ");
			/**
			 * Busca o gerente da unidade de ensino matriz  - Usado caso não encontre as opções acima 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = (select codigo from unidadeensino where matriz = true limit 1 )	");
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.gerente = true and funcionariocargo.ativo	");
			sql.append("  order by nome	 ), ', ') as   responsavelMatrizDepartamento ");
		} else if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_TCC)) {
			sql.append(" curso.nome as curso, ");
			/**
			 * Busca o coordenador do tcc da unidade de ensino em questão 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join unidadeensinocurso uec on uec.coordenadortcc = funcionario.codigo ");
			sql.append(" where pessoa.ativo = true and  uec.codigo = unidadeensinocurso.codigo  order by nome	");
			sql.append(" ), ', ') as   responsavel, ");			
			/**
			 * Busca o gerente da unidade de ensino em questão - Usado caso não encontre a opção acima 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = unidadeensino.codigo	");
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.gerente = true and funcionariocargo.ativo	");
			sql.append("  order by nome	 ), ', ') as   responsavelGerente, ");
			/**
			 * Busca o coordenador do tcc da unidade de ensino matriz  - Usado caso não encontre as opções acima
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join unidadeensinocurso uec on uec.coordenadortcc = funcionario.codigo ");
			sql.append(" where pessoa.ativo = true and   uec.curso = unidadeensinocurso.curso  ");
			sql.append(" and uec.unidadeEnsino = (select codigo from unidadeensino where matriz = true limit 1 ) ");	
			sql.append(" order by nome ");				
			sql.append(" ), ', ') as   responsavelMatriz, ");
			/**
			 * Busca o gerente da unidade de ensino matriz  - Usado caso não encontre as opções acima 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = (select codigo from unidadeensino where matriz = true limit 1 )	");
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.gerente = true and funcionariocargo.ativo	");
			sql.append("  order by nome	 ), ', ') as   responsavelMatrizDepartamento ");
		} else {
			sql.append(" array_to_string(array( ");
			/**
			 * Busca o funcionario da unidade de ensino em questão 
			 */
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = unidadeensino.codigo and funcionariocargo.ativo ");
			if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
				sql.append(" and funcionariocargo.gerente = true 	");
			} else if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO)) {
				sql.append(" and funcionariocargo.cargo = ").append(cargo);
			}
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append("  order by nome	 ), ', ') as   responsavel, ");
			/**
			 * Busca o gerente da unidade de ensino em questão - Usado caso não encontre a opção acima 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = unidadeensino.codigo	");
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.gerente = true and funcionariocargo.ativo	");
			sql.append("  order by nome	 ), ', ') as   responsavelGerente, ");
			/**
			 * Busca os funcionarios da unidade de ensino matriz utilizado a politica do tipo requerimento - Usado caso não encontre as opções acima
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = (select codigo from unidadeensino where matriz = true limit 1 )	");
			if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.GERENTE_DEPARTAMENTO)) {
				sql.append(" and funcionariocargo.gerente = true 	");
			} else if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.FUNCIONARIO_CARGO_DEPARTAMENTO)) {
				sql.append(" and funcionariocargo.cargo = ").append(cargo);
			}
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.ativo order by nome	 ), ', ') as   responsavelMatriz, ");
			/**
			 * Busca o gerente da unidade de ensino matriz - Usado caso não encontre a opção acima 
			 */
			sql.append(" array_to_string(array( ");
			sql.append(" select distinct pessoa.nome from pessoa ");
			sql.append(" inner join funcionario on funcionario.pessoa = pessoa.codigo ");
			sql.append(" inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
			sql.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
			sql.append(" where pessoa.ativo = true and  funcionariocargo.unidadeensino = (select codigo from unidadeensino where matriz = true limit 1 )	");
			sql.append(" and ((funcionariocargo.departamento is not null and funcionariocargo.departamento = ").append(departamento).append(") ");
			sql.append(" or (funcionariocargo.departamento is null and cargo.departamento = ").append(departamento).append("))");
			sql.append(" and funcionariocargo.gerente = true and funcionariocargo.ativo	");
			sql.append("  order by nome	 ), ', ') as   responsavelMatrizDepartamento ");
			
			
		}
		sql.append(" FROM unidadeensino ");
		if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO) || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_TCC)  || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE)) {
			sql.append(" inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = unidadeensino.codigo ");
			sql.append(" inner join curso on unidadeensinocurso.curso = curso.codigo ");
		}
		if (tipoRequerimentoUnidadeEnsinoVOs != null && !tipoRequerimentoUnidadeEnsinoVOs.isEmpty()) {
			sql.append(" where unidadeensino.codigo in (0");
			for (TipoRequerimentoUnidadeEnsinoVO tipoRequerimentoUnidadeEnsinoVO : tipoRequerimentoUnidadeEnsinoVOs) {
				sql.append(",  ").append(tipoRequerimentoUnidadeEnsinoVO.getUnidadeEnsino().getCodigo());
			}
			sql.append(" ) ");
		}
		if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO) || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE) || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_TCC)) {
			sql.append(" order by unidadeensino.nome, curso.nome ");
		} else {
			sql.append(" order by unidadeensino.nome ");
		}
		if (limit != null) {
			sql.append(" LIMIT ").append(limit);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<PossivelResponsavelRequerimentoVO> possivelResponsavelRequerimentoVOs = new ArrayList<PossivelResponsavelRequerimentoVO>(0);
		PossivelResponsavelRequerimentoVO possivelResponsavelRequerimentoVO = null;
		while (rs.next()) {
			possivelResponsavelRequerimentoVO = new PossivelResponsavelRequerimentoVO();
			possivelResponsavelRequerimentoVO.setUnidadeEnsino(rs.getString("unidadeEnsino"));
			if (tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO) || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_TCC)  || tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.COORDENADOR_CURSO_ESPECIFICO_NO_TRAMITE)) {
				possivelResponsavelRequerimentoVO.setCurso(rs.getString("curso"));
			}
			String pessoas = "";
			if(!rs.getString("responsavel").trim().isEmpty()){
				possivelResponsavelRequerimentoVO.setOrigemResponsavel("Política Definida");
				pessoas = rs.getString("responsavel");
			}else if(!rs.getString("responsavelGerente").trim().isEmpty()){
				possivelResponsavelRequerimentoVO.setOrigemResponsavel("Gerente do Departamento da Unidade de Ensino");
				pessoas = rs.getString("responsavelGerente");
			}else if(!rs.getString("responsavelMatriz").trim().isEmpty()){
				possivelResponsavelRequerimentoVO.setOrigemResponsavel("Funcionário da Matriz Utilizado a Politica Definida");
				pessoas = rs.getString("responsavelMatriz");
			}else if(!rs.getString("responsavelMatrizDepartamento").trim().isEmpty()){
				possivelResponsavelRequerimentoVO.setOrigemResponsavel("Gerente do Departamento da Unidade de Ensino Matriz");
				pessoas = rs.getString("responsavelMatrizDepartamento");
			}
			if(tipoDistribuicaoResponsavel.equals(TipoDistribuicaoResponsavelEnum.LISTA_FUNCIONARIO)) {
				possivelResponsavelRequerimentoVO.setOrigemResponsavel("Política Definida");
				for(TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO: tipoRequerimentoDepartamentoFuncionarioVOs) {
					if(tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().getCodigo().equals(rs.getInt("codigo"))) {
						if(!pessoas.trim().isEmpty()) {
							pessoas += ",";
						}
						pessoas += tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getPessoa().getNome();
						
					}
				}
			}
			if (!pessoas.trim().isEmpty()) {
				StringBuilder responsavels = new StringBuilder("<ul>");				
				for (String responsavel : pessoas.split(",")) {
					responsavels.append("<li>");
					responsavels.append(responsavel);
					responsavels.append("</li>");
				}
				responsavels.append("</ul>");
				possivelResponsavelRequerimentoVO.setResponsavel(responsavels.toString());
			} else {
				possivelResponsavelRequerimentoVO.setResponsavel("<span style=\"color:red\">Responsável não cadastrado.</span>");
			}
			possivelResponsavelRequerimentoVOs.add(possivelResponsavelRequerimentoVO);
		}
		return possivelResponsavelRequerimentoVOs;
	}
	
	@Override
	public void adicionarTipoRequerimentoDepartamentoFuncionario(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO) throws ConsistirException {
		getFacadeFactory().getTipoRequerimentoDepartamentoFuncionarioFacade().validarDados(tipoRequerimentoDepartamentoFuncionarioVO);
		tipoRequerimentoDepartamentoFuncionarioVO.setTipoRequerimentoDepartamentoVO(tipoRequerimentoDepartamentoVO);
		if(!tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().contains(tipoRequerimentoDepartamentoFuncionarioVO)) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_tipoRequerimentoDepartamentoFuncionario_funcionario_jaAdicionado"));
			tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().add(tipoRequerimentoDepartamentoFuncionarioVO);
		}
	}
	
	@Override
	public void removerTipoRequerimentoDepartamentoFuncionario(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO, List<UnidadeEnsinoVO> unidadeEnsinoVOsRemover) throws ConsistirException {
		
		for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinoVOsRemover) {
			for (TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionario : tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs()) {
				if (unidadeEnsino.getCodigo().equals(tipoRequerimentoDepartamentoFuncionario.getUnidadeEnsinoVO().getCodigo()) && tipoRequerimentoDepartamentoFuncionario.getFuncionarioVO().getCodigo().equals(tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getCodigo())) {
					tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().remove(tipoRequerimentoDepartamentoFuncionario);
					break;
				}
			}
			
		}


		
		
		//if(removerOutraUnidade) {
			/*for (Iterator<TipoRequerimentoDepartamentoFuncionarioVO> iterator = tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().iterator(); iterator.hasNext();) {
				TipoRequerimentoDepartamentoFuncionarioVO tipoRequerimentoDepartamentoFuncionarioVO2 = (TipoRequerimentoDepartamentoFuncionarioVO) iterator.next();
				if(tipoRequerimentoDepartamentoFuncionarioVO2.getFuncionarioVO().getCodigo().equals(tipoRequerimentoDepartamentoFuncionarioVO.getFuncionarioVO().getCodigo())) {
					
					for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOsRemover) {
						if(tipoRequerimentoDepartamentoFuncionarioVO.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsinoVO.getCodigo())){
							//iterator.remove();
							tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().remove(tipoRequerimentoDepartamentoFuncionarioVO);
							unidadeEnsinoVOsRemover.remove(unidadeEnsinoVO);
						}
					}
				}				
			}*/
	//	}else {
		//	tipoRequerimentoDepartamentoVO.getTipoRequerimentoDepartamentoFuncionarioVOs().remove(tipoRequerimentoDepartamentoFuncionarioVO);
		//}*/
	}
	
	
	@Override
	public void adicionarTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoSituacaoDepartamentoVO) throws ConsistirException, Exception {
		getFacadeFactory().getTipoRequerimentoSituacaoDepartamentoFacade().validarDados(tipoRequerimentoSituacaoDepartamentoVO);
		tipoRequerimentoSituacaoDepartamentoVO.setTipoRequerimentoDepartamentoVO(tipoRequerimentoDepartamentoVO);
		if(!tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs().contains(tipoRequerimentoSituacaoDepartamentoVO)) {			
			tipoRequerimentoSituacaoDepartamentoVO.setSituacaoRequerimentoDepartamentoVO(getFacadeFactory().getSituacaoRequerimentoDepartamentoFacade().consultarPorChavePrimaria(tipoRequerimentoSituacaoDepartamentoVO.getSituacaoRequerimentoDepartamentoVO().getCodigo(), null));
			tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs().add(tipoRequerimentoSituacaoDepartamentoVO);
			Ordenacao.ordenarLista(tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs(), "situacaoRequerimentoDepartamentoVO.situacao");
		}
	}
	
	@Override
	public void removerTipoRequerimentoSituacaoDepartamento(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO, TipoRequerimentoSituacaoDepartamentoVO tipoRequerimentoDepartamentoFuncionarioVO) throws ConsistirException {	
			tipoRequerimentoDepartamentoVO.getTipoRequerimentoSituacaoDepartamentoVOs().remove(tipoRequerimentoDepartamentoFuncionarioVO);	
	}
	
}