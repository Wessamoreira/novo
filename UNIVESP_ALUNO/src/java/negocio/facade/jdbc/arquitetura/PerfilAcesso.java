package negocio.facade.jdbc.arquitetura;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.PermissaoVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OpcaoPermissaoEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoPerfilAcessoPermissaoEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.arquitetura.PerfilAcessoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PerfilAcessoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PerfilAcessoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PerfilAcessoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class PerfilAcesso extends ControleAcesso implements PerfilAcessoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5978618616095753362L;
	protected static String idEntidade;

	public PerfilAcesso() throws Exception {
		super();
		setIdEntidade("PerfilAcesso");
	}
	

//	@Override
//	public PerfilAcessoVO consultarPorChavePrimariaUnica(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM PerfilAcesso WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		if (!tabelaResultado.next()) {
//			throw new ConsistirException("Dados Não Encontrados.");
//		}
//		return montarDados(tabelaResultado, nivelMontarDados);
//	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>PerfilAcessoVO</code>.
	 */
	public PerfilAcessoVO novo() throws Exception {
		PerfilAcesso.incluir(getIdEntidade());
		PerfilAcessoVO obj = new PerfilAcessoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>PerfilAcessoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerfilAcessoVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final PerfilAcessoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PerfilAcessoVO.validarDados(obj);
			incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO PerfilAcesso( nome, responsavel, ambiente, possuiAcessoAdministrativo, possuiAcessoAcademico, possuiAcessoFinanceiro, possuiAcessoCompras, possuiAcessoSeiDecidir, possuiAcessoProcessoSeletivo, possuiAcessoEAD, possuiAcessoBiblioteca, possuiAcessoCRM, possuiAcessoAvaliacaoInsitucional, possuiAcessoBancoDeCurriculos, possuiAcessoPlanoOrcamentario, possuiAcessoNotaFiscal, possuiAcessoEstagio, possuiAcessoPatrimonio, possuiAcessoRh) VALUES ( ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setInt(2, obj.getResponsavel());
					sqlInserir.setString(3, obj.getAmbiente().toString());
					sqlInserir.setBoolean(4, obj.getPossuiAcessoAdministrativo());
					sqlInserir.setBoolean(5, obj.getPossuiAcessoAcademico());
					sqlInserir.setBoolean(6, obj.getPossuiAcessoFinanceiro());
					sqlInserir.setBoolean(7, obj.getPossuiAcessoCompras());
					sqlInserir.setBoolean(8, obj.getPossuiAcessoSeiDecidir());
					sqlInserir.setBoolean(9, obj.getPossuiAcessoProcessoSeletivo());
					sqlInserir.setBoolean(10, obj.getPossuiAcessoEAD());
					sqlInserir.setBoolean(11, obj.getPossuiAcessoBiblioteca());
					sqlInserir.setBoolean(12, obj.getPossuiAcessoCRM());
					sqlInserir.setBoolean(13, obj.getPossuiAcessoAvaliacaoInsitucional());
					sqlInserir.setBoolean(14, obj.getPossuiAcessoBancoDeCurriculos());
					sqlInserir.setBoolean(15, obj.getPossuiAcessoPlanoOrcamentario());
					sqlInserir.setBoolean(16, obj.getPossuiAcessoNotaFiscal());
					sqlInserir.setBoolean(17, obj.getPossuiAcessoEstagio());
					sqlInserir.setBoolean(18, obj.getPossuiAcessoPatrimonio());
					sqlInserir.setBoolean(19, obj.getPossuiAcessoRh());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getPermissaoFacade().incluirPermissaos(obj.getCodigo(), obj.getPermissaoVOs(), usuarioVO);
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("check_nome_perfilacesso_duplicado")) {
				throw new Exception("Já existe um perfil de acesso com o nome informado.");
			}
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerfilAcessoVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PerfilAcessoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			PerfilAcessoVO.validarDados(obj);
			alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE PerfilAcesso set nome=?, responsavel=?, ambiente=?, possuiAcessoAdministrativo=?, possuiAcessoAcademico=?, possuiAcessoFinanceiro=?, possuiAcessoCompras=?, possuiAcessoSeiDecidir=?, possuiAcessoProcessoSeletivo=?, possuiAcessoEAD=?, possuiAcessoBiblioteca=?, possuiAcessoCRM=?, possuiAcessoAvaliacaoInsitucional=?, possuiAcessoBancoDeCurriculos=?, possuiAcessoPlanoOrcamentario=?, possuiAcessoNotaFiscal=?, possuiAcessoEstagio=?, possuiAcessoPatrimonio=?, possuiAcessoRh=?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setInt(2, obj.getResponsavel());
					sqlAlterar.setString(3, obj.getAmbiente().toString());
					sqlAlterar.setBoolean(4, obj.getPossuiAcessoAdministrativo());
					sqlAlterar.setBoolean(5, obj.getPossuiAcessoAcademico());
					sqlAlterar.setBoolean(6, obj.getPossuiAcessoFinanceiro());
					sqlAlterar.setBoolean(7, obj.getPossuiAcessoCompras());
					sqlAlterar.setBoolean(8, obj.getPossuiAcessoSeiDecidir());
					sqlAlterar.setBoolean(9, obj.getPossuiAcessoProcessoSeletivo());
					sqlAlterar.setBoolean(10, obj.getPossuiAcessoEAD());
					sqlAlterar.setBoolean(11, obj.getPossuiAcessoBiblioteca());
					sqlAlterar.setBoolean(12, obj.getPossuiAcessoCRM());
					sqlAlterar.setBoolean(13, obj.getPossuiAcessoAvaliacaoInsitucional());
					sqlAlterar.setBoolean(14, obj.getPossuiAcessoBancoDeCurriculos());
					sqlAlterar.setBoolean(15, obj.getPossuiAcessoPlanoOrcamentario());
					sqlAlterar.setBoolean(16, obj.getPossuiAcessoNotaFiscal());
					sqlAlterar.setBoolean(17, obj.getPossuiAcessoEstagio());
					sqlAlterar.setBoolean(18, obj.getPossuiAcessoPatrimonio());
					sqlAlterar.setBoolean(19, obj.getPossuiAcessoRh());
					sqlAlterar.setInt(20, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getPermissaoFacade().alterarPermissaos(obj.getCodigo(), obj.getPermissaoVOs(), usuarioVO);
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("check_nome_perfilacesso_duplicado")) {
				throw new Exception("Já existe um perfil de acesso com o nome informado.");
			}
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>PerfilAcessoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PerfilAcessoVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PerfilAcessoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM permissao WHERE ((codperfilacesso = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
			sql = "DELETE FROM PerfilAcesso WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * Responsável por realizar uma consulta de <code>PerfilAcesso</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>PerfilAcessoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<PerfilAcessoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PerfilAcesso WHERE upper (sem_acentos(nome)) ilike(sem_acentos(?)) ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por realizar uma consulta de <code>PerfilAcesso</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>PerfilAcessoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<PerfilAcessoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PerfilAcesso WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public PerfilAcessoVO consultarPorChavePrimariaUnica(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PerfilAcesso WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return montarDados(tabelaResultado, nivelMontarDados);
	}
	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>PerfilAcessoVO</code> resultantes da consulta.
	 */
	public  List<PerfilAcessoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<PerfilAcessoVO> vetResultado = new ArrayList<PerfilAcessoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>PerfilAcessoVO</code>.
	 * 
	 * @return O objeto da classe <code>PerfilAcessoVO</code> com os dados
	 *         devidamente montados.
	 */
	public  PerfilAcessoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		PerfilAcessoVO obj = new PerfilAcessoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX ) {
			return obj;
		}
		obj.setResponsavel(dadosSQL.getInt("responsavel"));
		obj.setAmbiente(TipoVisaoEnum.valueOf(dadosSQL.getString("ambiente")));
		obj.setPossuiAcessoAcademico(dadosSQL.getBoolean("possuiAcessoAcademico"));
		obj.setPossuiAcessoAdministrativo(dadosSQL.getBoolean("possuiAcessoAdministrativo"));
		obj.setPossuiAcessoFinanceiro(dadosSQL.getBoolean("possuiAcessoFinanceiro"));
		obj.setPossuiAcessoCompras(dadosSQL.getBoolean("possuiAcessoCompras"));
		obj.setPossuiAcessoSeiDecidir(dadosSQL.getBoolean("possuiAcessoSeiDecidir"));
		obj.setPossuiAcessoProcessoSeletivo(dadosSQL.getBoolean("possuiAcessoProcessoSeletivo"));
		obj.setPossuiAcessoEAD(dadosSQL.getBoolean("possuiAcessoEAD"));
		obj.setPossuiAcessoBiblioteca(dadosSQL.getBoolean("possuiAcessoBiblioteca"));
		obj.setPossuiAcessoCRM(dadosSQL.getBoolean("possuiAcessoCRM"));
		obj.setPossuiAcessoAvaliacaoInsitucional(dadosSQL.getBoolean("possuiAcessoAvaliacaoInsitucional"));
		obj.setPossuiAcessoBancoDeCurriculos(dadosSQL.getBoolean("possuiAcessoBancoDeCurriculos"));
		obj.setPossuiAcessoPlanoOrcamentario(dadosSQL.getBoolean("possuiAcessoPlanoOrcamentario"));
		obj.setPossuiAcessoNotaFiscal(dadosSQL.getBoolean("possuiAcessoNotaFiscal"));
		obj.setPossuiAcessoEstagio(dadosSQL.getBoolean("possuiAcessoEstagio"));
		obj.setPossuiAcessoPatrimonio(dadosSQL.getBoolean("possuiAcessoPatrimonio"));		
		obj.setPossuiAcessoRh(dadosSQL.getBoolean("possuiAcessoRh"));		
		obj.setPossuiAcessoContabil(dadosSQL.getBoolean("possuiAcessoContabil"));		
		if (nivelMontarDados != Uteis.NIVELMONTARDADOS_TODOS && nivelMontarDados != Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		obj.setPermissaoVOs(getFacadeFactory().getPermissaoFacade().consultarPermissaos(obj.getCodigo()));

		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>PerfilAcessoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public PerfilAcessoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getPerfilAcessoVO(codigoPrm, usuario);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return PerfilAcesso.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		PerfilAcesso.idEntidade = idEntidade;
	}

	public List<PerfilAcessoVO> consultarPorUsuarioEUnidadeEnsino(Integer codigoUsuario, Integer codigoUnidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT pa.* FROM usuarioPerfilAcesso upa ");
		sqlStr.append("INNER JOIN usuario u ON upa.usuario = u.codigo ");
		sqlStr.append("INNER JOIN perfilAcesso pa ON upa.perfilAcesso = pa.codigo ");
		sqlStr.append("WHERE u.codigo = ? AND upa.unidadeEnsino = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoUsuario, codigoUnidadeEnsino });
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS);
	}

	public PerfilAcessoVO definirPerfilAcessoParaAlunoProfessorECandidato(String visao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		// Essa rotina verifica o tipo de visão escolhida pelo usuário, valida a
		// configuração geral do sistema que foi carregada
		// nos métodos anteriores e define o perfil de acesso correspondente à
		// visão do usuário.
		if (visao.equals("aluno")) {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().validarConfiguracaoGeralSistema(configuracaoGeralSistemaVO);
			return configuracaoGeralSistemaVO.getPerfilPadraoAluno();
		}
		if (visao.equals("professor")) {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().validarConfiguracaoGeralSistema(configuracaoGeralSistemaVO);
			return configuracaoGeralSistemaVO.getPerfilPadraoProfessorGraduacao();
		}
		if (visao.equals("candidato")) {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().validarConfiguracaoGeralSistema(configuracaoGeralSistemaVO);
			return configuracaoGeralSistemaVO.getPerfilPadraoCandidato();
		}
		// No caso da visão não ser nenhuma das 3 escolhidas, retornamos um novo
		// Perfil de acesso que será tratado no método chamador.
		return new PerfilAcessoVO();
	}

	public PerfilAcessoVO definirPerfilAcessoParaAdminEFuncionario(Integer codigoUnidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		// Essa rotina verifica os registros de UsuarioPerfilAcesso existentes
		// dentro de usuário para definir
		// qual o seu perfil de acesso através da comparação da unidade de
		// ensino.
		for (UsuarioPerfilAcessoVO usuarioPerfilAcessoVO : usuarioVO.getUsuarioPerfilAcessoVOs()) {
			if (usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo().equals(codigoUnidadeEnsino)) {
				usuarioPerfilAcessoVO.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(usuarioPerfilAcessoVO.getPerfilAcesso().getCodigo(), usuarioVO));
				return usuarioPerfilAcessoVO.getPerfilAcesso();
			}
		}
		if (usuarioVO.getUsuarioPerfilAcessoVOs() != null && !usuarioVO.getUsuarioPerfilAcessoVOs().isEmpty()) {
			if (codigoUnidadeEnsino.intValue() == 0) {
				throw new ConsistirException("Você não está autorizado a entrar sem Unidade de Ensino");
			} else {
				throw new ConsistirException("Você não tem permissão para entrar nesta Unidade de Ensino");
			}
		} else {
			throw new ConsistirException("Você não tem permissão para entrar no sistema");
		}
	}

	public PermissaoAcessoMenuVO montarPermissoesMenu(PerfilAcessoVO perfilAcessoVO) {
		try {			
			// Método que monta a árvore de permissões.
			if(Uteis.isAtributoPreenchido(perfilAcessoVO.getCodigo()) && !perfilAcessoVO.getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
				perfilAcessoVO = consultarPorChavePrimaria(perfilAcessoVO.getCodigo(), null);
				perfilAcessoVO.setNivelMontarDados(NivelMontarDados.TODOS);
			}
			
		} catch (Exception ex) {
			Logger.getLogger(PerfilAcesso.class.getName()).log(Level.SEVERE, null, ex);
		}
		PermissaoAcessoMenuVO permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
		Class<?> classeGenerica = null;
		Class<?> permissaoMenu = null;
		Method metodoGet = null;
		Method metodoSet = null;
		try {
			classeGenerica = Class.forName(PermissaoVO.class.getName());
			permissaoMenu = Class.forName(permissaoAcessoMenuVO.getClass().getName());
			metodoGet = classeGenerica.getMethod("getNomeEntidade");
			for (PermissaoVO permissaoVO : perfilAcessoVO.getPermissaoVOs()) {			
				try {				
					metodoSet = permissaoMenu.getMethod("set" + metodoGet.invoke(permissaoVO).toString(), Boolean.class);
					metodoSet.invoke(permissaoAcessoMenuVO, Boolean.TRUE);					
				} catch (Exception e) {
				// //System.out.println("Erro:" + e.getMessage()); //foi
				// verificado que tem a posibilidade de nao encontrar uma
				// permissao especifica pois foi alterada no codigo e nao
				// deletou o codigo na base
				}finally {
					metodoSet =  null;
				}
			}
		} catch (Exception e) {
		}finally {
			permissaoMenu = null;
			metodoGet = null;
			metodoSet = null;
			
		}
		return permissaoAcessoMenuVO;
	}

	public PermissaoAcessoMenuVO montarPermissoesMenuComMaisDeUmPerfil(UsuarioVO usuarioVO) {
		try {
			// Método que monta a árvore de permissões.
			usuarioVO.setPerfilAcesso(consultarPorChavePrimariaSemPermissao(usuarioVO.getPerfilAcesso().getCodigo(), null));
			usuarioVO.getPerfilAcesso().setPermissaoVOs(getFacadeFactory().getPermissaoFacade().consultarPermissaosComMaisDeUmPerfil(usuarioVO.getCodigo(), usuarioVO.getUnidadeEnsinoLogado().getCodigo(), usuarioVO.getPerfilAcesso().getCodigo()));
		} catch (Exception ex) {
			Logger.getLogger(PerfilAcesso.class.getName()).log(Level.SEVERE, null, ex);
		}
		PermissaoAcessoMenuVO permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
		for (PermissaoVO permissaoVO : usuarioVO.getPerfilAcesso().getPermissaoVOs()) {
			try {
				Class<?> classeGenerica = Class.forName(permissaoVO.getClass().getName());
				Method metodoGet = classeGenerica.getMethod("getNomeEntidade");
				metodoGet.invoke(permissaoVO).toString();

				Class<?> permissaoMenu = Class.forName(permissaoAcessoMenuVO.getClass().getName());
				Method metodoSet = permissaoMenu.getMethod("set" + metodoGet.invoke(permissaoVO).toString(), Boolean.class);
				metodoSet.invoke(permissaoAcessoMenuVO, Boolean.TRUE);
			} catch (Exception e) {
			}
		}
		return permissaoAcessoMenuVO;
	}

	public PerfilAcessoVO consultarPerfilAcessoDiretorMultiCampus(UsuarioVO usuarioVO) {
		for (UsuarioPerfilAcessoVO usuarioPerfilAcessoVO : usuarioVO.getUsuarioPerfilAcessoVOs()) {
			if (usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo().equals(0)) {
				return usuarioPerfilAcessoVO.getPerfilAcesso();
			}
		}
		return new PerfilAcessoVO();
	}

	public PerfilAcessoVO consultarPerfilParaFuncionarioAdministrador(Integer codigoUnidadeEnsino, UsuarioVO usuarioVO) {
		PerfilAcessoVO perfilFinal = new PerfilAcessoVO();
		for (UsuarioPerfilAcessoVO usuarioPerfilAcessoVO : usuarioVO.getUsuarioPerfilAcessoVOs()) {
			if (usuarioPerfilAcessoVO.getUnidadeEnsinoVO().getCodigo().intValue() == codigoUnidadeEnsino.intValue()) {				
				return usuarioPerfilAcessoVO.getPerfilAcesso();				
			}
		}
		return perfilFinal;
	}

	public Integer consultarQtdePerfilParaMesmaUnidade(Integer usuario, UsuarioVO usuarioLogado) {
		StringBuilder sb = new StringBuilder();
		sb.append("select qtde from (");
		sb.append(" select unidadeEnsino, count(perfilacesso) as qtde from usuarioperfilacesso ");
		sb.append(" where usuario = ");
		sb.append(usuario);
		sb.append(" group by unidadeensino ");
		sb.append(") as t ");
		sb.append(" order by qtde desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public PerfilAcessoVO consultarPorChavePrimariaSemPermissao(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM PerfilAcesso WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm.intValue() });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDadosSemPermissoes(tabelaResultado));
	}

	public static PerfilAcessoVO montarDadosSemPermissoes(SqlRowSet dadosSQL) throws Exception {
		PerfilAcessoVO obj = new PerfilAcessoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNovoObj(Boolean.FALSE);

		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PerfilAcessoVO consultarPerfilAcessoPadraoOuvidoria(Integer unidadeEnsino, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (configuracaoGeralSistema == null || configuracaoGeralSistema.getCodigo().intValue() == 0) {
			configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(unidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (configuracaoGeralSistema == null || configuracaoGeralSistema.getCodigo().intValue() == 0) {
			throw new Exception("Não existe nenhum perfil de acesso padrão cadastrado para esse unidade ensino.");
		}
		return configuracaoGeralSistema.getPerfilPadraoOuvidoria();
	}

	@Override
	public void realizarReplicacaoPermissao(PerfilAcessoVO perfilAcessoVO, PerfilAcessoModuloEnum modulo, PerfilAcessoSubModuloEnum subModulo, PermissaoVO permissaoBase, String filtroCadastro, String filtroFuncionalidade, Cliente cliente, UsuarioVO usuarioVO) {
		q: 
			for (PermissaoVO permissaoVO : perfilAcessoVO.getPermissaoVOs()) {
			if ((modulo == null || modulo.equals(PerfilAcessoModuloEnum.TODOS) 
					|| getFacadeFactory().getPermissaoFacade().validarPermissaoEstaNoModulo(cliente, ((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao()), modulo)) 
					&& (subModulo == null || subModulo.equals(PerfilAcessoSubModuloEnum.TODOS) 
					|| getFacadeFactory().getPermissaoFacade().validarPermissaoEstaNoSubModulo(cliente, ((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao()), subModulo))) {
				if (!filtroCadastro.trim().isEmpty() && !Uteis.removerAcentos(permissaoVO.getTitulo().toUpperCase()).contains(Uteis.removerAcentos(filtroCadastro.trim().toUpperCase()))) {
					continue q;
				}
				if (!filtroFuncionalidade.trim().isEmpty() && !permissaoVO.getDescricaoFuncinalidades().toString().contains(Uteis.removerAcentos(filtroFuncionalidade.trim().toUpperCase()))) {
					continue q;
				}
				permissaoVO.setTotal(permissaoBase.getTotal());
				permissaoVO.setTotalSemExcluir(permissaoBase.getTotalSemExcluir());
				permissaoVO.setIncluir(permissaoBase.getIncluir());
				permissaoVO.setAlterar(permissaoBase.getAlterar());
				permissaoVO.setExcluir(permissaoBase.getExcluir());
				permissaoVO.setAlterar(permissaoBase.getAlterar());
				permissaoVO.setConsultar(permissaoBase.getConsultar());
				permissaoVO.setResponsavel(usuarioVO.getCodigo());
				permissaoVO.setUpdated(new Date());				
				realizarDefinicaoInformacaoPermissao(permissaoVO);
				realizarMarcacaoFuncionalidades(permissaoVO, permissaoBase.getMarcarFuncionalidades(), usuarioVO);
			}
		}
	}

	@Override
	public void realizarMarcacaoPermissao(PermissaoVO permissaoVO, OpcaoPermissaoEnum opcao, UsuarioVO usuarioVO) {
		permissaoVO.setResponsavel(usuarioVO.getCodigo());
		permissaoVO.setUpdated(new Date());
		if (opcao == null || (!opcao.equals(OpcaoPermissaoEnum.TOTAL) && !opcao.equals(OpcaoPermissaoEnum.TOTAL_SEM_EXCLUIR) && !opcao.equals(OpcaoPermissaoEnum.NENHUM))) {
			if (permissaoVO.getAlterar() || permissaoVO.getExcluir()) {
				permissaoVO.setConsultar(true);
			}
			if (permissaoVO.getIncluir() && permissaoVO.getAlterar() && permissaoVO.getConsultar() && permissaoVO.getExcluir()) {
				permissaoVO.setTotal(true);
			} else if (permissaoVO.getIncluir() && permissaoVO.getAlterar() && permissaoVO.getConsultar() && !permissaoVO.getExcluir()) {
				permissaoVO.setTotalSemExcluir(true);
			} else {
				permissaoVO.setTotal(false);
				permissaoVO.setTotalSemExcluir(false);
			}
		} else if (opcao.equals(OpcaoPermissaoEnum.TOTAL)) {
			permissaoVO.setIncluir(permissaoVO.getTotal());
			permissaoVO.setAlterar(permissaoVO.getTotal());
			permissaoVO.setConsultar(permissaoVO.getTotal());
			permissaoVO.setExcluir(permissaoVO.getTotal());
			permissaoVO.setTotalSemExcluir(false);

		} else if (opcao.equals(OpcaoPermissaoEnum.TOTAL_SEM_EXCLUIR)) {
			permissaoVO.setIncluir(permissaoVO.getTotalSemExcluir());
			permissaoVO.setAlterar(permissaoVO.getTotalSemExcluir());
			permissaoVO.setConsultar(permissaoVO.getTotalSemExcluir());
			permissaoVO.setExcluir(false);
			permissaoVO.setTotal(false);
		} else if (opcao.equals(OpcaoPermissaoEnum.NENHUM)) {
			permissaoVO.setIncluir(false);
			permissaoVO.setAlterar(false);
			permissaoVO.setConsultar(false);
			permissaoVO.setExcluir(false);
			permissaoVO.setTotal(false);
			permissaoVO.setTotalSemExcluir(false);
		}
		realizarDefinicaoInformacaoPermissao(permissaoVO);
		if(Uteis.isAtributoPreenchido(permissaoVO.getPermissao()) && !((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao()).getTipoPerfilAcesso().getIsFuncionalidade() && permissaoVO.getPermissoes().isEmpty()){
			realizarMarcacaoFuncionalidades(permissaoVO, "", usuarioVO);
			permissaoVO.setMarcarFuncionalidadesPorEntidade(false);
		}
	}

	@Override
	public void realizarDefinicaoInformacaoPermissao(PermissaoVO permissaoVO) {
		permissaoVO.setPermissoes("");
		if(permissaoVO.getPermissao() != null && ((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao()).getTipoPerfilAcesso().equals(TipoPerfilAcessoPermissaoEnum.FUNCIONALIDADE)){
			if (permissaoVO.getTotal()) {
				permissaoVO.setPermissoes(OpcaoPermissaoEnum.TOTAL.getPermissao());
			}
		}else{
		if (permissaoVO.getTotal()) {
			permissaoVO.setPermissoes(OpcaoPermissaoEnum.TOTAL.getPermissao());
		} else if (permissaoVO.getTotalSemExcluir()) {
			permissaoVO.setPermissoes(OpcaoPermissaoEnum.TOTAL_SEM_EXCLUIR.getPermissao());
		} else {
			if (permissaoVO.getIncluir()) {
				permissaoVO.setPermissoes(permissaoVO.getPermissoes() + OpcaoPermissaoEnum.INCLUIR.getPermissao() + OpcaoPermissaoEnum.NOVO.getPermissao());
			}
			if (permissaoVO.getExcluir()) {
				permissaoVO.setPermissoes(permissaoVO.getPermissoes() + OpcaoPermissaoEnum.EXCLUIR.getPermissao());
			}
			if (permissaoVO.getAlterar()) {
				permissaoVO.setPermissoes(permissaoVO.getPermissoes() + OpcaoPermissaoEnum.ALTERAR.getPermissao());
			}
			if (permissaoVO.getConsultar() || permissaoVO.getAlterar() || permissaoVO.getConsultar()) {
				permissaoVO.setPermissoes(permissaoVO.getPermissoes() + OpcaoPermissaoEnum.CONSULTAR.getPermissao());
			}
		}
		}

	}

	public void realizarMarcacaoFuncionalidades(PermissaoVO permissaoVO, String marcarFuncionalidades, UsuarioVO usuarioVO ) {		
		if (permissaoVO.getPermissoes().trim().isEmpty() || marcarFuncionalidades.equals("sim") || marcarFuncionalidades.equals("nao")) {
			for (PermissaoVO obj : permissaoVO.getFuncionalidades()) {
				if(permissaoVO.getPermissoes().trim().isEmpty() || marcarFuncionalidades.equals("nao")){
					obj.setTotal(false);	
					realizarMarcacaoPermissao(obj, OpcaoPermissaoEnum.TOTAL, usuarioVO);	
				}else if(marcarFuncionalidades.equals("sim")){
					obj.setTotal(true);	
					realizarMarcacaoPermissao(obj, OpcaoPermissaoEnum.TOTAL, usuarioVO);
				}
				
			}
		}		
	}
	
	public void realizarMarcacaoFuncionalidadesPorEntidade(PermissaoVO permissaoVO, Boolean marcarFuncionalidadesPorEntidade, UsuarioVO usuarioVO) {
		for (PermissaoVO obj : permissaoVO.getFuncionalidades()) {
			if (permissaoVO.getPermissoes().trim().isEmpty() || marcarFuncionalidadesPorEntidade.equals(false)) {
				obj.setTotal(false);
			} else if (marcarFuncionalidadesPorEntidade.equals(true)) {
				obj.setTotal(true);
			}
		}
	}

    @Override
	public PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoAluno(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, MatriculaVO matricula, Boolean alunoNaoAssinouContratoMatricula, UsuarioVO usuario) throws Exception {
		PerfilAcessoVO perfilAcessoLogar = null;
		//Deve ser utilizado perfil de não assinou contrato apenas alunos com situação de matrícula ativa (AT) Foi adicionado a verificação de situação inexistente PR caso aconteça erro de importação. 
		if(Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO.getPerfilAcessoAlunoNaoAssinouContratoMatricula()) && alunoNaoAssinouContratoMatricula && (matricula.getSituacao().equals("AT") || matricula.getSituacao().equals("PR"))) {
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilAcessoAlunoNaoAssinouContratoMatricula();
			return perfilAcessoLogar;
		}
		perfilAcessoLogar = verificarControleAcessoSituacaoMatricula(configuracaoGeralSistemaVO, matricula, usuario);
		if (Uteis.isAtributoPreenchido(perfilAcessoLogar)) {
			return perfilAcessoLogar;
		}
		switch (TipoNivelEducacional.getEnum(matricula.getCurso().getNivelEducacional())) {
		case INFANTIL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoInfantil();
			break;
		case BASICO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoFundamental();
			break;
		case MEDIO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoMedio();
			break;
		case EXTENSAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoExtensao();
			break;
		case SEQUENCIAL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoSequencial();
			break;
		case GRADUACAO_TECNOLOGICA:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoGraduacaoTecnologica();
			break;
		case SUPERIOR:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoGraduacao();
			break;
		case POS_GRADUACAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoPosGraduacao();
			break;
		case MESTRADO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoPosGraduacao();
			break;
		case PROFISSIONALIZANTE:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoTecnicoProfissionalizante();
			break;
		}
		if (Uteis.isAtributoPreenchido(perfilAcessoLogar)) {
			return perfilAcessoLogar;
		}
		return configuracaoGeralSistemaVO.getPerfilPadraoAluno();
	}
    
    @Override
    public PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoPais(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
    	PerfilAcessoVO perfilAcessoLogar = null;
    	switch (tipoNivelEducacional) {
		case INFANTIL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoInfantil();
			break;
		case BASICO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoFundamental();
			break;
		case MEDIO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoMedio();
			break;
		case EXTENSAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoExtensao();
			break;
		case SEQUENCIAL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoSequencial();
			break;
		case GRADUACAO_TECNOLOGICA:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoGraduacaoTecnologica();
			break;
		case SUPERIOR:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoGraduacao();
			break;
		case POS_GRADUACAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoPosGraduacao();
			break;
		case MESTRADO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoPosGraduacao();
			break;
		case PROFISSIONALIZANTE:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoAlunoEducacaoTecnicoProfissionalizante();
			break;
		}
		if (Uteis.isAtributoPreenchido(perfilAcessoLogar)) {
			return perfilAcessoLogar;
		}
		return configuracaoGeralSistemaVO.getPerfilPadraoPais();
    }
    
	public PerfilAcessoVO verificarControleAcessoSituacaoMatricula(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		if (configuracaoGeralSistemaVO.getPermitirAcessoAlunoFormado() && !configuracaoGeralSistemaVO.getPerfilPadraoAlunoFormado().getCodigo().equals(0) && matricula.getSituacao().equals(SituacaoVinculoMatricula.FORMADO.getValor())) {
			return configuracaoGeralSistemaVO.getPerfilPadraoAlunoFormado();
		} else if ((configuracaoGeralSistemaVO.getPermitirAcessoAlunoEvasao() && !configuracaoGeralSistemaVO.getPerfilPadraoAlunoEvasao().getCodigo().equals(0)) && matricula.getSituacao().equals(SituacaoVinculoMatricula.TRANCADA.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.ABANDONO_CURSO.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.TRANSFERIDA.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.CANCELADA.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.JUBILADO.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.TRANSFERENCIA_INTERNA.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.DESLIGADO.getValor())) {
			return configuracaoGeralSistemaVO.getPerfilPadraoAlunoEvasao();
		} else if ((configuracaoGeralSistemaVO.getPermitirAcessoAlunoPreMatricula() && !configuracaoGeralSistemaVO.getPerfilPadraoAlunoPreMatricula().getCodigo().equals(0)) && (matricula.getSituacao().equals(SituacaoVinculoMatricula.ATIVA.getValor()) || matricula.getSituacao().equals(SituacaoVinculoMatricula.PREMATRICULA.getValor()))) {
//			String situacaoMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarSituacaoAcademicaUltimoPeriodoPorMatricula(matricula.getMatricula(), usuario);
//			Boolean possuiMatriculaPeriodoAtiva = getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMatriculaPeriodoAtivaPorSituacao(matricula.getMatricula(), 0, SituacaoMatriculaPeriodoEnum.ATIVA.getValor());
			if (matricula.getMatriculaPeriodoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor())) {
				return configuracaoGeralSistemaVO.getPerfilPadraoAlunoPreMatricula();
			} else {
				return null;
			}
		}
		return null;
	}
	
	
	
	@Override
	public PerfilAcessoVO executarVerificacaoPerfilAcessoSelecionarVisaoPais(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, MatriculaVO matriculaVO) throws Exception {
		PerfilAcessoVO perfilAcessoLogar = null;
    	
		
		if (Uteis.isAtributoPreenchido(perfilAcessoLogar)) {
			return perfilAcessoLogar;
		}
		switch (TipoNivelEducacional.getEnum(matriculaVO.getCurso().getNivelEducacional())) {
		case INFANTIL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoInfantil();
			break;
		case BASICO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoFundamental();
			break;
		case MEDIO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoMedio();
			break;
		case EXTENSAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoExtensao();
			break;
		case SEQUENCIAL:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoSequencial();
			break;
		case GRADUACAO_TECNOLOGICA:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoGraduacaoTecnologica();
			break;
		case SUPERIOR:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoGraduacao();
			break;
		case POS_GRADUACAO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoPosGraduacao();
			break;
		case MESTRADO:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoPosGraduacao();
			break;
		case PROFISSIONALIZANTE:
			perfilAcessoLogar = configuracaoGeralSistemaVO.getPerfilPadraoPaisEducacaoTecnicoProfissionalizante();
			break;
		}
		if (Uteis.isAtributoPreenchido(perfilAcessoLogar)) {
			return perfilAcessoLogar;
		}
		return configuracaoGeralSistemaVO.getPerfilPadraoPais();
	}


	@Override
	public PerfilAcessoVO consultarPorChavePrimaria(Integer valorConsulta, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception {
			
		consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM PerfilAcesso WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return montarDados(tabelaResultado, nivelMontarDados);
	}
}
