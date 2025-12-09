package negocio.facade.jdbc.arquitetura;

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

import negocio.comuns.arquitetura.TratamentoErroVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.TratamentoErroInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TratamentoErroVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>TratamentoErroVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see TratamentoErroVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TratamentoErro extends ControleAcesso implements TratamentoErroInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4841469718367944231L;
	protected static String idEntidade;

    public TratamentoErro() throws Exception {
        super();
        setIdEntidade("TratamentoErro");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TratamentoErroVO</code>.
     */
    public TratamentoErroVO novo() throws Exception {
        TratamentoErro.incluir(getIdEntidade());
        TratamentoErroVO obj = new TratamentoErroVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TratamentoErroVO</code>. Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o
     * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TratamentoErroVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TratamentoErroVO obj, final boolean verificarPermissao, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO TratamentoErro( erro ) VALUES ( ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getErro());
                    
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TratamentoErroVO obj) throws Exception {
        try {
        	String sql = "DELETE FROM TratamentoErro WHERE codigo = ?";
        	getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }
   
    public static List<TratamentoErroVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<TratamentoErroVO> vetResultado = new ArrayList<>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    
    public static TratamentoErroVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        TratamentoErroVO obj = new TratamentoErroVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setErro(dadosSQL.getString("erro"));
        obj.setNovoObj(false);
        return obj;
    }

    
    public TratamentoErroVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sql = "SELECT * FROM TratamentoErro WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados. (TratamentoErro)");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TratamentoErro.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        TratamentoErro.idEntidade = idEntidade;
    }
    
    public TratamentoErroVO consultarPorErro(String erro, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sql = "SELECT * FROM TratamentoErro WHERE erro = ? order by codigo desc limit 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{erro});
        if (!tabelaResultado.next()) {
            return new TratamentoErroVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
	@Override
    public TratamentoErroVO inicializarDadosTratamentoErro(String mensagemErro, UsuarioVO usuarioVO) {
        try {
            if (mensagemErro.equals("Invalid row number (65536) outside allowable range (0..65535)")) {
                TratamentoErroVO obj = new TratamentoErroVO();
                obj.setMensagemApresentar("A quantidade de resultado ultrapassou o limite de linhas permitido (0..65536)");
                return obj;
            }

            if (mensagemErro.contains("For input string:")) {
                TratamentoErroVO obj = new TratamentoErroVO();
                obj.setMensagemApresentar("Valor inválido (" + mensagemErro.substring(mensagemErro.lastIndexOf(":") + 2) + ") Verifique o campo selecionado e informe um valor numérico sem letras ou símbolos");
                return obj;
            }

            if (mensagemErro.contains("is not a recognized imageformat")) {
                TratamentoErroVO obj = new TratamentoErroVO();
                obj.setMensagemApresentar("O arquivo selecionado não é uma imagem válida. Verifique se ele está em um formato de imagem suportado e se não está corrompido.");
                return obj;
            }

            if (mensagemErro.contains(".pfx")) {
                TratamentoErroVO obj = new TratamentoErroVO();
                obj.setMensagemApresentar("O certificado digital A1 não foi localizado. Verifique nas configurações se ele está corretamente instalado e se ainda está dentro do prazo de validade.");
                return obj;
            }

            if (!mensagemErro.toLowerCase().contains("select")
                    && !mensagemErro.toLowerCase().contains("insert")
                    && !mensagemErro.toLowerCase().contains("delete")
                    && !mensagemErro.toLowerCase().contains("update")
                    && !mensagemErro.toLowerCase().contains("googlejsonresponseexception")) {
                return null;
            }
            TratamentoErroVO objExistente = consultarPorErro(mensagemErro, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
            if (objExistente != null && !objExistente.getCodigo().equals(0)) {
                objExistente.setMensagemApresentar("Ocorreu um erro interno no sistema, favor entrar em contato com a equipe técnica e informar o código = " + objExistente.getCodigo() + "");
                return objExistente;
            } else {
                TratamentoErroVO obj = new TratamentoErroVO();
                obj.setErro(mensagemErro);

				getFacadeFactory().getTratamentoErroFacade().incluir(obj, false, usuarioVO);

				obj.setMensagemApresentar("Ocorreu um erro interno no sistema, favor entrar em contato com a equipe técnica e informar o código = " + obj.getCodigo() + "");
				return obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void persistirTratamentoErroPorMensagem(String mensagemErro, UsuarioVO usuarioVO) {
		try {
			TratamentoErroVO objExistente = consultarPorErro(mensagemErro, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (!Uteis.isAtributoPreenchido(objExistente)) {
				TratamentoErroVO obj = new TratamentoErroVO();
				obj.setErro(mensagemErro);
				getFacadeFactory().getTratamentoErroFacade().incluir(obj, false, usuarioVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
