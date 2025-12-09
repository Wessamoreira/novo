package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PersonalizacaoMensagemAutomaticaUnidadeEnsino extends ControleAcesso implements PersonalizacaoMensagemAutomaticaUnidadeEnsinoInterfaceFacade{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public PersonalizacaoMensagemAutomaticaUnidadeEnsino() throws Exception {
		super();
		setIdEntidade("PersonalizacaoMensagemAutomaticaUnidadeEnsino");
	}

	@Override
	public PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO novo() throws Exception {
		PersonalizacaoMensagemAutomaticaUnidadeEnsino.incluir(getIdEntidade());
		PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj = new PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO();
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj) throws Exception {

		final String sql = "INSERT INTO PersonalizacaoMensagemAutomaticaUnidadeEnsino(personalizacaoMensagemAutomatica, unidadeEnsino) VALUES ( ?, ? ) ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getPersonalizacaoMensagemAutomatica().getCodigo());
				sqlInserir.setInt(2, obj.getUnidadeEnsino().getCodigo());
				
				return sqlInserir;
			}
		});
		obj.setNovoObj(Boolean.FALSE);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj) throws Exception {
		final String sql = "UPDATE PersonalizacaoMensagemAutomaticaUnidadeEnsino set personalizacaoMensagemAutomatica=?, unidadeEnsino=?  WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getPersonalizacaoMensagemAutomatica().getCodigo());
				sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo());
				sqlAlterar.setInt(3, obj.getCodigo());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirPersonalizacaoMensagemAutomaticaUnidadeEnsino(Integer codigoPersonalizacaoMensagemAutomatica, List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) throws Exception {
		for (PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO personalizacaoMensagemAutomaticaUnidadeEnsinoVO : personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) {
			personalizacaoMensagemAutomaticaUnidadeEnsinoVO.getPersonalizacaoMensagemAutomatica().setCodigo(codigoPersonalizacaoMensagemAutomatica);
			incluir(personalizacaoMensagemAutomaticaUnidadeEnsinoVO);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPersonalizacaoMensagemAutomaticaUnidadeEnsino(Integer codigoPersonalizacaoMensagemAutomatica, List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) throws Exception {
		String str = "DELETE FROM personalizacaoMensagemAutomaticaUnidadeEnsino WHERE personalizacaoMensagemAutomatica = " + codigoPersonalizacaoMensagemAutomatica;
//		for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO : unidadeEnsinoBibliotecaVOs) {
//			str += " AND unidadeensino <> " + unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getCodigo();
//		}
		getConexao().getJdbcTemplate().update(str);
		for (PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO personalizacaoMensagemAutomaticaUnidadeEnsinoVO : personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) {
			//if (unidadeEnsinoBibliotecaVO.getBiblioteca() == null || unidadeEnsinoBibliotecaVO.getBiblioteca() == 0) {
			personalizacaoMensagemAutomaticaUnidadeEnsinoVO.getPersonalizacaoMensagemAutomatica().setCodigo(codigoPersonalizacaoMensagemAutomatica);
				incluir(personalizacaoMensagemAutomaticaUnidadeEnsinoVO);
			//}
		}
	}

	@Override
	public List consultarPersonalizacaoMensagemAutomaticaUnidadeEnsinoPorPersonalizacaoMensagemAutomatica(Integer personalizacaoMensagemAutomatica, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaUnidadeEnsino.consultar(getIdEntidade());
		List objetos = new ArrayList();
		String sql = "SELECT * FROM personalizacaomensagemautomaticaunidadeensino WHERE personalizacaomensagemautomatica = " + personalizacaoMensagemAutomatica;
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (resultado.next()) {
			PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO novoObj = new PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuarioVO);
			objetos.add(novoObj);
		}
		return objetos;
	}
	
	public  PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj = new PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getPersonalizacaoMensagemAutomatica().setCodigo(dadosSQL.getInt("personalizacaomensagemautomatica"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getUnidadeEnsino().setFiltrarUnidadeEnsino(true);

		obj.setNovoObj(new Boolean(false));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		obj.getUnidadeEnsino().setFiltrarUnidadeEnsino(true);
		return obj;
	}
	
	public  void montarDadosUnidadeEnsino(PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }
	
	

	public static String getIdEntidade() {
		return PersonalizacaoMensagemAutomaticaUnidadeEnsino.idEntidade;
	}

	@Override
	public void setIdEntidade(String idEntidade) {
		PersonalizacaoMensagemAutomaticaUnidadeEnsino.idEntidade = idEntidade;
	}

}
