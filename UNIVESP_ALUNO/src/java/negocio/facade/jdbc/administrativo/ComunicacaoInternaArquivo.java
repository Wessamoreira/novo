package negocio.facade.jdbc.administrativo;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ComunicacaoInternaArquivoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ComunicacaoInternaArquivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ComunicacaoInternaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ComunicacaoInternaVO</code>. Encapsula toda a interação com o
 * banco de dados.
 * 
 * @see ComunicacaoInternaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ComunicacaoInternaArquivo extends ControleAcesso implements ComunicacaoInternaArquivoInterfaceFacade {

	private static final long serialVersionUID = -3696067347143823811L;
    protected static String idEntidade;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ComunicacaoInternaArquivo() throws Exception {
        super();
        setIdEntidade("ComunicacaoInterna");

    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>ComunicacaoInternaVO</code>.
     */
    public ComunicacaoInternaArquivoVO novo() throws Exception {
        ComunicacaoInternaArquivo.incluir(getIdEntidade());
        ComunicacaoInternaArquivoVO obj = new ComunicacaoInternaArquivoVO();
        return obj;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirLista(final ComunicacaoInternaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        Iterator i = obj.getListaArquivosAnexo().iterator();
        while (i.hasNext()) {
            ArquivoVO arq = (ArquivoVO)i.next();
            getFacadeFactory().getArquivoFacade().incluir(arq, false, usuario, configuracaoGeralSistemaVO);
        }
        Iterator j = obj.getListaArquivosAnexo().iterator();
        while (j.hasNext()) {
            ArquivoVO arq = (ArquivoVO)j.next();
            ComunicacaoInternaArquivoVO cia = new ComunicacaoInternaArquivoVO();
            cia.setArquivoAnexo(arq);
            cia.getComunicacaoInterna().setCodigo(obj.getCodigo());
            incluir(cia, false, usuario, configuracaoGeralSistemaVO);
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ComunicacaoInternaArquivoVO obj, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            final String sql = "INSERT INTO ComunicacaoInternaArquivo( arquivo, comunicacaoInterna ) VALUES ( ?, ?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getArquivoAnexo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getArquivoAnexo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getComunicacaoInterna().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getComunicacaoInterna().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(false);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>ComunicacaoInternaVO</code>. Sempre utiliza a chave primária da
     * classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
     * Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação
     * <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ComunicacaoInternaVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ComunicacaoInternaArquivoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            final String sql = "UPDATE ComunicacaoInternaArquivo set arquivo=?, comunicacaoInterna=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    if (obj.getArquivoAnexo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getArquivoAnexo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getComunicacaoInterna().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getComunicacaoInterna().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setInt(3, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>ComunicacaoInternaVO</code>. Sempre localiza o registro a ser
     * excluído através da chave primária da entidade. Primeiramente verifica a
     * conexão com o banco de dados e a permissão do usuário para realizar esta
     * operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ComunicacaoInternaVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ComunicacaoInternaArquivoVO obj) throws Exception {
        try {
            String sql = "DELETE FROM ComunicacaoInternaArquivo WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getComunicadoInternoDestinatarioFacade().excluirComunicadoInternoDestinatarios(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ComunicacaoInterna</code>
     * através do valor do atributo <code>Integer codigo</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ComunicacaoInternaArquivoVO WHERE codigo = " + valorConsulta.intValue() + " Order by data desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<ComunicacaoInternaArquivoVO> consultarPorCodigoComunicacaoInterna(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ComunicacaoInternaArquivo.* FROM ComunicacaoInternaArquivo INNER JOIN Arquivo ON arquivo.codigo = ComunicacaoInternaArquivo.arquivo WHERE comunicacaointerna = " + valorConsulta.intValue() + " ORDER BY arquivo.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>ComunicacaoInternaVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>ComunicacaoInternaVO</code>.
     *
     * @return O objeto da classe <code>ComunicacaoInternaVO</code> com os dados
     *         devidamente montados.
     */
    public  ComunicacaoInternaArquivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ComunicacaoInternaArquivoVO obj = new ComunicacaoInternaArquivoVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.getArquivoAnexo().setCodigo(dadosSQL.getInt("arquivo"));
        obj.getComunicacaoInterna().setCodigo(dadosSQL.getInt("comunicacaoInterna"));
        obj.setNovoObj(false);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, usuario);
        return obj;
    }

    public  void montarDadosArquivo(ComunicacaoInternaArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivoAnexo().getCodigo().intValue() == 0) {
            obj.setArquivoAnexo(new ArquivoVO());
            return;
        }
        obj.setArquivoAnexo(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoAnexo().getCodigo(), nivelMontarDados, usuario));
    }
    
    /**
     * Operação responsável por localizar um objeto da classe
     * <code>ComunicacaoInternaVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public ComunicacaoInternaArquivoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM ComunicacaoInternaArquivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ComunicacaoInternaArquivo ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return ComunicacaoInternaArquivo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ComunicacaoInternaArquivo.idEntidade = idEntidade;
    }
}
