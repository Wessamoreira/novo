package negocio.facade.jdbc.academico;

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



import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.FiliacaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>FiliacaoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>FiliacaoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see FiliacaoVO
 * @see ControleAcesso
 * @see Pessoa
 */
@Repository
@Scope("singleton")
@Lazy
public class Filiacao extends ControleAcesso implements FiliacaoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Filiacao() throws Exception {
		super();
		setIdEntidade("Filiacao");
	}

	public FiliacaoVO novo() throws Exception {
		Filiacao.incluir(getIdEntidade());
		FiliacaoVO obj = new FiliacaoVO();
		return obj;
	}

	public void realizarVerificacaoFiltroFiliacaoParaPessoa(StringBuffer sql, String tipoPessoa) {
		if (tipoPessoa != null && !tipoPessoa.equalsIgnoreCase("")) {
			if (tipoPessoa.equals("RL")) {
				sql.append(" inner join filiacao on pessoa.codigo = filiacao.pais ");
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void incluir(final FiliacaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		try {
			boolean validarCPF = getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, usuario);
			FiliacaoVO.validarDados(obj, validarCPF);
			validarCPFUnico(obj.getPais(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			realizarValidacaoParaPersistirPais(obj, configuracaoGeralSistema, usuario);
			final String sql = "INSERT INTO Filiacao(tipo, aluno, responsavelFinanceiro, pais, responsavelLegal ) VALUES ( ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getTipo());
					sqlInserir.setInt(2, obj.getAluno().intValue());
					sqlInserir.setBoolean(3, obj.getResponsavelFinanceiro());
					sqlInserir.setInt(4, obj.getPais().getCodigo());
					sqlInserir.setBoolean(5, obj.getResponsavelLegal());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final FiliacaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		boolean validarCPF = getFacadeFactory().getConfiguracaoGeralSistemaFacade().realizarVerificacaoValidarCpf(false, usuario);
		FiliacaoVO.validarDados(obj, validarCPF);
		validarCPFUnico(obj.getPais(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		realizarValidacaoParaPersistirPais(obj, configuracaoGeralSistema, usuario);
		final String sql = "UPDATE Filiacao set tipo=?, aluno=?, responsavelFinanceiro=?, pais=?, responsavelLegal=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getTipo());
				sqlAlterar.setInt(2, obj.getAluno().intValue());
				sqlAlterar.setBoolean(3, obj.getResponsavelFinanceiro());
				sqlAlterar.setInt(4, obj.getPais().getCodigo());
				sqlAlterar.setBoolean(5, obj.getResponsavelLegal());
				sqlAlterar.setInt(6, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarValidacaoParaPersistirPais(FiliacaoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		if (obj.getPais().getCodigo().intValue() == 0) {
			getFacadeFactory().getPessoaFacade().incluir(obj.getPais(), false, usuario, configuracaoGeralSistema, false, false, false, false, false);
		} else {
			getFacadeFactory().getPessoaFacade().alterarDadosBasicosFiliacao(obj.getPais(), false, usuario, configuracaoGeralSistema, true);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(FiliacaoVO obj, UsuarioVO usuario) throws Exception {
		Filiacao.excluir(getIdEntidade());
		String sql = "DELETE FROM Filiacao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List<FiliacaoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, usuario);
	}

	// public List consultarPorRG(String valorConsulta, boolean controlarAcesso,
	// UsuarioVO usuario) throws Exception {
	// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr = "SELECT * FROM Filiacao WHERE RG like('" + valorConsulta
	// + "%') ORDER BY RG";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, usuario));
	// }
	// public List consultarPorCPF(String valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception {
	// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr = "SELECT * FROM Filiacao WHERE CPF like('" + valorConsulta
	// + "%') ORDER BY CPF";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, usuario));
	// }
	/**
	 * Metodo que consulta uma filiacao pelo cpf caso nao encontrado tenta
	 * consulta somente pessoa pelo cpf.
	 */
	public FiliacaoVO carregarApenasUmPorCPF(FiliacaoVO obj, PessoaVO pessoaVO, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (obj.getPais().getCPF().equals(pessoaVO.getCPF())) {
			obj.getPais().setCPF(Constantes.EMPTY);
			Uteis.checkState(Boolean.TRUE, "O CPF informado não pode ser igual ao do aluno.");
		}
		PessoaVO pessoaBuscaPaiVO = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(obj.getPais().getCPF(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (Uteis.isAtributoPreenchido(pessoaBuscaPaiVO)) {
			obj.setPais(pessoaBuscaPaiVO);
		}
		return (obj);
	}

	// public List consultarPorTipo(String valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception {
	// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr = "SELECT * FROM Filiacao WHERE tipo like('" +
	// valorConsulta + "%') ORDER BY tipo";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, usuario));
	// }
	//
	// public List consultarPorNome(String valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception {
	// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr = "SELECT * FROM Filiacao WHERE nome like('" +
	// valorConsulta + "%') ORDER BY nome";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, usuario));
	// }
	//
	public List<FiliacaoVO> consultarPorCodigoPessoaTipo(Integer valorConsulta, String tipo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Pessoa.codigo = " + valorConsulta + " ");
		if (tipo != null && !tipo.equals("")) {
			sqlStr.append("and Filiacao.tipo = '" + tipo + "'");
		}
		sqlStr.append("ORDER BY Pessoa.nome ");
		// String sqlStr =
		// "SELECT Filiacao.* FROM Filiacao, Pessoa WHERE Filiacao.aluno = Pessoa.codigo and Pessoa.codigo = "
		// + valorConsulta + " and Filiacao.tipo = '" + tipo +
		// "' ORDER BY Pessoa.nome limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	//
	// public List consultarPorCodigo(Integer valorConsulta, boolean
	// controlarAcesso, UsuarioVO usuario) throws Exception {
	// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
	// String sqlStr = "SELECT * FROM Filiacao WHERE codigo >= " +
	// valorConsulta.intValue() + " ORDER BY codigo";
	// SqlRowSet tabelaResultado =
	// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	// return (montarDadosConsulta(tabelaResultado, usuario));
	// }

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 */
	public List<FiliacaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<FiliacaoVO> vetResultado = new ArrayList<FiliacaoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FiliacaoVO</code>.
	 *
	 * @return O objeto da classe <code>FiliacaoVO</code> com os dados
	 *         devidamente montados.
	 */
	public FiliacaoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		FiliacaoVO obj = new FiliacaoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("filiacao.codigo")));
		obj.setNome(dadosSQL.getString("pais.nome"));
		obj.setTipo(dadosSQL.getString("filiacao.tipo"));
		obj.setCPF(dadosSQL.getString("pais.cpf"));
		obj.setRG(dadosSQL.getString("pais.rg"));
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
		obj.getCidade().setNome(dadosSQL.getString("cidade.nome"));
		obj.getCidade().setCep(dadosSQL.getString("cidade.cep"));
		obj.getCidade().getEstado().setCodigo(dadosSQL.getInt("cidade.estado"));
		obj.getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		obj.getCidade().getEstado().setSigla(dadosSQL.getString("estado.sigla"));
		obj.setOrgaoEmissor(dadosSQL.getString("pais.orgaoemissor"));
		obj.setCep(dadosSQL.getString("pais.cep"));
		obj.getPais().setNumero(dadosSQL.getString("pais.numero"));
		obj.getPais().setComplemento(dadosSQL.getString("pais.complemento"));
		obj.getPais().setDataNasc(dadosSQL.getDate("pais.dataNasc"));
		obj.getPais().getNacionalidade().setCodigo(dadosSQL.getInt("pais.nacionalidade"));
		obj.getPais().setEstadoCivil(dadosSQL.getString("pais.estadoCivil"));
		obj.setEndereco(dadosSQL.getString("pais.endereco"));
		obj.setSetor(dadosSQL.getString("pais.setor"));
		obj.setTelefoneComer(dadosSQL.getString("pais.telefoneComer"));
		obj.setTelefoneRes(dadosSQL.getString("pais.telefoneRes"));
		obj.setTelefoneRecado(dadosSQL.getString("pais.telefoneRecado"));
		obj.setCelular(dadosSQL.getString("pais.celular"));
		obj.setEmail(dadosSQL.getString("pais.email"));
		obj.setAluno(new Integer(dadosSQL.getInt("filiacao.aluno")));
		obj.setResponsavelFinanceiro(dadosSQL.getBoolean("filiacao.responsavelfinanceiro"));
		obj.setResponsavelLegal(dadosSQL.getBoolean("filiacao.responsavellegal"));
		obj.getPais().setCodigo(dadosSQL.getInt("filiacao.pais"));
		obj.getPais().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("pais.possuiAcessoVisaoPais"));
		obj.getPais().setNome(dadosSQL.getString("pais.nome"));
		obj.getPais().setGrauParentesco(dadosSQL.getString("pais.grauparentesco"));
		obj.setNovoObj(Boolean.FALSE);
		if (!obj.getPais().getNacionalidade().getCodigo().equals(0)) {
			obj.getPais().setNacionalidade(getFacadeFactory().getPaizFacade().consultarPorChavePrimaria(obj.getPais().getNacionalidade().getCodigo(), false, usuario));
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirFiliacaos(Integer aluno, UsuarioVO usuario) throws Exception {
		excluirFiliacaos(aluno, true, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirFiliacaos(Integer aluno, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM Filiacao WHERE (aluno = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { aluno });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarFiliacaos(PessoaVO aluno, List<FiliacaoVO> objetos, Boolean deveValidarCPF, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		String str = "DELETE FROM Filiacao WHERE aluno = " + aluno.getCodigo();
		Iterator<FiliacaoVO> i = objetos.iterator();
		while (i.hasNext()) {
			FiliacaoVO objeto = (FiliacaoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		if (Uteis.isAtributoPreenchido(getIdEntidade()) && !getIdEntidade().equals("Funcionario") && deveValidarCPF) {
			validarFiliacao(objetos, configuracaoGeralSistema, aluno);
		}
		Iterator<FiliacaoVO> e = objetos.iterator();
		while (e.hasNext()) {
			FiliacaoVO objeto = (FiliacaoVO) e.next();
			objeto.getPais().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(configuracaoGeralSistema, "FILIACAO");
			if (objeto.getCodigo().equals(0)) {
				objeto.setAluno(aluno.getCodigo());
				if (objeto.getPais().getCEP().equals("")) {
					objeto.getPais().setCEP(aluno.getCEP());
				}
				if (objeto.getPais().getEndereco().equals("")) {
					objeto.getPais().setEndereco(aluno.getEndereco());
				}
				objeto.getPais().setAtivo(Boolean.TRUE);
				incluir(objeto, configuracaoGeralSistema, usuario);
			} else {
				objeto.setAluno(aluno.getCodigo());
				if (objeto.getPais().getCEP().equals("")) {
					objeto.getPais().setCEP(aluno.getCEP());
				}
				if (objeto.getPais().getEndereco().equals("")) {
					objeto.getPais().setEndereco(aluno.getEndereco());
				}
				alterar(objeto, configuracaoGeralSistema, usuario);
			}
			getFacadeFactory().getUsuarioFacade().executarVerificacaoParaInclusaoNovoUsuarioPorFiliacao(objeto.getPais(), usuario);

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void incluirFiliacaos(PessoaVO alunoPrm, List<FiliacaoVO> objetos, Boolean deveValidarCPF, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(getIdEntidade()) && !getIdEntidade().equals("Funcionario") && deveValidarCPF) {
			validarFiliacao(objetos, configuracaoGeralSistema, alunoPrm);
		}
		Iterator<FiliacaoVO> e = objetos.iterator();
		while (e.hasNext()) {
			FiliacaoVO obj = (FiliacaoVO) e.next();
			obj.setAluno(alunoPrm.getCodigo());
			if (obj.getPais().getCPF().equals("")) {
				obj.getPais().setCPF(alunoPrm.getCPF());
			}
			if (obj.getPais().getCEP().equals("")) {
				obj.getPais().setCEP(alunoPrm.getCEP());
			}
			if (obj.getPais().getEndereco().equals("")) {
				obj.getPais().setEndereco(alunoPrm.getEndereco());
			}
			obj.getPais().setAtivo(Boolean.TRUE);
			obj.getPais().inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(configuracaoGeralSistema, "FILIACAO");
			incluir(obj, configuracaoGeralSistema, usuario);
			getFacadeFactory().getUsuarioFacade().executarVerificacaoParaInclusaoNovoUsuarioPorFiliacao(obj.getPais(), usuario);
		}
	}

	public void validarFiliacao(List<FiliacaoVO> objetos, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, PessoaVO alunoPrm) throws Exception {
		boolean validarMaeFiliacao = configuracaoGeralSistema.getMaeFiliacao().booleanValue();
		if (validarMaeFiliacao && alunoPrm.getAluno()) {
			if (objetos.isEmpty()) {
				throw new Exception("Deve ser informado ao menos uma filiação (Mãe)!");
			}
			boolean mae = false;
			Iterator<FiliacaoVO> e = objetos.iterator();
			while (e.hasNext()) {
				FiliacaoVO obj = (FiliacaoVO) e.next();
				if (obj.getTipo().equals("MA")) {
					mae = true;
				}
			}
			if (!mae) {
				throw new Exception("Deve ser informado ao menos uma filiação (Mãe)!");
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>FiliacaoVO</code>
	 * relacionados a um objeto da classe <code>basico.Pessoa</code>.
	 *
	 * @param aluno
	 *            Atributo de <code>basico.Pessoa</code> a ser utilizado para
	 *            localizar os objetos da classe <code>FiliacaoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>FiliacaoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Override
	public List<FiliacaoVO> consultarFiliacaos(Integer aluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Filiacao.aluno = ? ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { aluno });
		return montarDadosConsulta(resultado, usuario);
	}

	public FiliacaoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Filiacao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT filiacao.codigo AS \"filiacao.codigo\" , filiacao.tipo AS \"filiacao.tipo\", filiacao.responsavellegal AS \"filiacao.responsavellegal\",  ");
		str.append(" filiacao.responsavelfinanceiro AS \"filiacao.responsavelfinanceiro\", filiacao.aluno AS \"filiacao.aluno\" , filiacao.pais AS \"filiacao.pais\", ");
		str.append(" pais.codigo AS \"pais.codigo\" , pais.nome AS \"pais.nome\", pais.cpf AS \"pais.cpf\", pais.rg AS \"pais.rg\", pais.dataNasc AS \"pais.dataNasc\", pais.orgaoemissor AS \"pais.orgaoemissor\", ");
		str.append(" pais.endereco AS \"pais.endereco\", pais.setor AS \"pais.setor\", pais.cep AS \"pais.cep\", ");
		str.append(" pais.numero AS \"pais.numero\", pais.complemento AS \"pais.complemento\", pais.nacionalidade AS \"pais.nacionalidade\",  pais.estadoCivil AS \"pais.estadoCivil\", ");		
		str.append(" pais.telefoneComer AS \"pais.telefoneComer\", pais.telefoneRes AS \"pais.telefoneRes\", pais.telefoneRecado AS \"pais.telefoneRecado\", ");
		str.append(" pais.celular AS \"pais.celular\", pais.email AS \"pais.email\", pais.possuiAcessoVisaoPais AS \"pais.possuiAcessoVisaoPais\" , ");
		str.append(" cidade.codigo AS \"cidade.codigo\", cidade.nome AS \"cidade.nome\", cidade.cep AS \"cidade.cep\", pais.grauparentesco as \"pais.grauparentesco\", ");
		str.append(" cidade.estado AS \"cidade.estado\", ");
		str.append(" estado.nome AS \"estado.nome\", estado.sigla AS \"estado.sigla\" ");
		str.append(" FROM filiacao ");
		str.append(" LEFT JOIN pessoa  on pessoa.codigo = filiacao.aluno ");
		str.append(" LEFT JOIN pessoa as pais on pais.codigo = filiacao.pais ");
		str.append(" LEFT JOIN cidade ON pais.cidade = cidade.codigo ");
		str.append(" LEFT JOIN estado ON estado.codigo = cidade.estado ");

		return str;
	}

	@Override
	public Boolean consultarSeExisteFiliacaoPorPais(Integer codigoPais, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Filiacao WHERE pais = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPais });
		if (!tabelaResultado.next()) {
			return false;
		}
		return true;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Filiacao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Filiacao.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCorrecaoMatriculaCRM(PessoaVO aluno, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuariologado) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(" Select pessoa.*, filiacao.tipo from filiacao WHERE Filiacao.aluno = ? ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { aluno.getCodigo() });
		while (resultado.next()) {
			PessoaVO pai = new PessoaVO();
			String nome = resultado.getString("nome");
			if (nome.equals("")) {
				pai.setNome(aluno.getNome() + " - " + resultado.getString("tipo"));
			} else {
				pai.setNome(resultado.getString("nome"));
			}
			pai.setCPF(resultado.getString("cpf"));
			pai.setTelefoneComer(resultado.getString("telefoneComer"));
			pai.setOrgaoEmissor(resultado.getString("orgaoemissor"));
			pai.setRG(resultado.getString("rg"));
			pai.setSetor(resultado.getString("setor"));
			pai.setEndereco(resultado.getString("endereco"));
			pai.setCEP(resultado.getString("cep"));
			pai.setTelefoneRecado(resultado.getString("telefoneRecado"));
			pai.setTelefoneRes(resultado.getString("telefoneRes"));
			pai.setCelular(resultado.getString("celular"));
			pai.setEmail(resultado.getString("email"));
			pai.getCidade().setCodigo(resultado.getInt("cidade"));
			pai.setGerarNumeroCPF(Boolean.TRUE);
			getFacadeFactory().getPessoaFacade().incluir(pai, false, usuariologado, configuracaoGeralSistema, false);

			String sql = "UPDATE Filiacao set pais = " + pai.getCodigo() + " WHERE ((codigo = " + resultado.getInt("codigo") + " ))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuariologado);
			getConexao().getJdbcTemplate().update(sql);
			sql = null;
		}

	}

	@Override
	public String consultarNomeFiliacaoPorTipoFiliacaoEPessoa(String tipoFiliacao, Integer pessoa) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT pessoa.nome from filiacao ");
		sql.append(" inner join pessoa on pessoa.codigo = filiacao.pais ");
		sql.append(" where filiacao.tipo = '").append(tipoFiliacao).append("' ");
		sql.append(" and filiacao.aluno = ").append(pessoa).append(" order by filiacao.codigo asc limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		String nome = "";
		if (rs.next()) {
			nome =  rs.getString("nome");
			if (nome.contains("''")) {
				nome = nome.replaceAll("''", "'");
	        } 
		}
		return nome;
	}

	public void validarCPFUnico(PessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		PessoaVO pessoaValidarCPF = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(obj.getCPF(), obj.getCodigo(), "", false, nivelMontarDados, usuario);
		if (pessoaValidarCPF.getCodigo() != 0 && !obj.getGerarNumeroCPF()) {
			if (obj.getAluno().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um aluno cadastrado com esse CPF.");
			} else if (obj.getProfessor().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um professor cadastrado com esse CPF.");
			} else if (obj.getMembroComunidade().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um membro da comunidade cadastrado com esse CPF.");
			} else if (obj.getCandidato().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um candidato cadastrado com esse CPF.");
			} else if (obj.getFuncionario().equals(Boolean.TRUE)) {
				throw new ConsistirException("Já existe um funcionário cadastrado com esse CPF.");
			}
		}
	}
	
	@Override
	public List<FiliacaoVO> incluirFiliacaoConformeProspect(Integer codigoProspect , List<FiliacaoVO> filiacaoPessoa , UsuarioVO usuario) throws Exception{
//			ProspectsVO prospectPessoa = getFacadeFactory().getProspectsFacade().consultarPaiMaeProspectPorCodigo(codigoProspect);
//			if (Uteis.isAtributoPreenchido(filiacaoPessoa)) {
//				return executarValidacaoFiliacaoExistenciaPaiMae(prospectPessoa, filiacaoPessoa);
//			}else {
//				return incluirFiliacaoConformeTipoProspect(prospectPessoa , filiacaoPessoa , "AMBAS");
//			
		return null;
	}
	
	public List<FiliacaoVO> incluirFiliacaoConformeTipoProspect( List<FiliacaoVO> filiacaoPessoa , String tipoFiliacao) throws Exception{
		
		FiliacaoVO filiacaoVO = null;
//		
//		if (Uteis.isAtributoPreenchido(prospectsVO.getNomePai()) && (tipoFiliacao.equals("AMBAS") || tipoFiliacao.equals("PAI"))) {
//		  filiacaoVO = new FiliacaoVO();
//		  filiacaoVO.getPais().setNome(prospectsVO.getNomePai());
//		  filiacaoVO.getPais().setNomeBatismo(prospectsVO.getNomePai());
//		  filiacaoVO.setTipo("PA");
//		  filiacaoVO.setAluno(prospectsVO.getPessoa().getCodigo());
//		  filiacaoVO.getPais().setGrauParentesco("Pai");
//		  filiacaoVO.getPais().adicionarCpfTemporarioPais(filiacaoVO);
//		  filiacaoPessoa.add(filiacaoVO);
//		}if (Uteis.isAtributoPreenchido(prospectsVO.getNomeMae()) && (tipoFiliacao.equals("AMBAS") || tipoFiliacao.equals("MAE"))) {
//		  filiacaoVO = new FiliacaoVO();
//		  filiacaoVO.getPais().setNome(prospectsVO.getNomeMae());
//		  filiacaoVO.getPais().setNomeBatismo(prospectsVO.getNomeMae());
//		  filiacaoVO.setTipo("MA");
//		  filiacaoVO.setAluno(prospectsVO.getPessoa().getCodigo());
//		  filiacaoVO.getPais().setGrauParentesco("Mae");
//		  filiacaoVO.getPais().adicionarCpfTemporarioPais(filiacaoVO);
//		  filiacaoPessoa.add(filiacaoVO);
//		}
		
		return filiacaoPessoa;		
	}
	
	public List<FiliacaoVO> executarValidacaoFiliacaoExistenciaPaiMae(List<FiliacaoVO> filiacaoPessoa) throws Exception{
		boolean filiacaoPai = true;
		boolean filiacaoMae = true;
		for (FiliacaoVO obj : filiacaoPessoa) {
			if (obj.getTipo().equals("PA")) {
				filiacaoPai = false;
			}
			if (obj.getTipo().equals("MA")) {
				filiacaoMae = false;
			}
		}
//		if (filiacaoMae) {
//			return incluirFiliacaoConformeTipoProspect(prospectPessoa , filiacaoPessoa , "MAE");
//		}else if(filiacaoPai) {
//			return incluirFiliacaoConformeTipoProspect(prospectPessoa , filiacaoPessoa , "PAI");
//		}
		return filiacaoPessoa;
		
	}
}
