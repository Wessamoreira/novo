package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemRegistroEntradaAcervoVO;
import negocio.comuns.biblioteca.RegistroEntradaAcervoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.SituacaoHistoricoExemplar;
import negocio.comuns.utilitarias.dominios.TipoEntradaAcervo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.RegistroEntradaAcervoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>RegistroEntradaAcervoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>RegistroEntradaAcervoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RegistroEntradaAcervoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroEntradaAcervo extends ControleAcesso implements RegistroEntradaAcervoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7565376163349567589L;
	protected static String idEntidade;

    public RegistroEntradaAcervo() throws Exception {
        super();
        setIdEntidade("RegistroEntradaAcervo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>RegistroEntradaAcervoVO</code>.
     */
    public RegistroEntradaAcervoVO novo() throws Exception {
        RegistroEntradaAcervo.incluir(getIdEntidade());
        RegistroEntradaAcervoVO obj = new RegistroEntradaAcervoVO();
        return obj;
    }

    public void inicializarDadosRegistroEntradaAcervoNovo(RegistroEntradaAcervoVO registroEntradaAcervoVO, UsuarioVO usuario) throws Exception {
        registroEntradaAcervoVO.setFuncionario(usuario);
    }

    /**
     * Método que ao incluir um exemplar no banco, também inclui um registroEntradaAcervo, setando o tipo de Entrada do
     * itemRegistroEntradaAcervo para <b>ENTRADA_SIMPLES</b> e os outros atributos para a persistência no Banco.
     *
     * @param exemplarVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void registrarEntradaAcervoExemplar(ExemplarVO exemplarVO, UsuarioVO usuario) throws Exception {
        RegistroEntradaAcervoVO registroEntradaAcervoVO = new RegistroEntradaAcervoVO();
        registroEntradaAcervoVO.setData(new Date());
        registroEntradaAcervoVO.setBiblioteca(exemplarVO.getBiblioteca());
        registroEntradaAcervoVO.setFuncionario(usuario);
        registroEntradaAcervoVO.setTipoEntrada(exemplarVO.getTipoEntrada());

        ItemRegistroEntradaAcervoVO itemRegistroEntradaAcervoVO = new ItemRegistroEntradaAcervoVO();
        itemRegistroEntradaAcervoVO.setExemplar(exemplarVO);

        itemRegistroEntradaAcervoVO.setTipoEntrada(TipoEntradaAcervo.ENTRADA_SIMPLES.getValor());
        itemRegistroEntradaAcervoVO.setRegistroEntradaAcervo(registroEntradaAcervoVO.getCodigo());
        registroEntradaAcervoVO.getItemRegistroEntradaAcervoVOs().add(itemRegistroEntradaAcervoVO);
        incluir(registroEntradaAcervoVO, usuario);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroEntradaAcervoVO</code>.
     * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RegistroEntradaAcervoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroEntradaAcervoVO obj, UsuarioVO usuario) throws Exception {
        try {
            RegistroEntradaAcervoVO.validarDados(obj);
            RegistroEntradaAcervo.incluir(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO RegistroEntradaAcervo( data, funcionario, biblioteca, tipoentrada ) VALUES ( ?, ?, ?, ? ) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    if (obj.getBiblioteca().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getBiblioteca().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getTipoEntrada());

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
            modificarSituacaoAtualExemplarRegistrarHistoricoExemplar(obj, usuario);
            getFacadeFactory().getItemRegistroEntradaAcervoFacade().incluirItemRegistroEntradaAcervos(obj.getCodigo(), obj.getItemRegistroEntradaAcervoVOs());
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void modificarSituacaoAtualExemplarRegistrarHistoricoExemplar(RegistroEntradaAcervoVO registroEntradaAcervoVO, UsuarioVO usuario) throws Exception {
    	for (ItemRegistroEntradaAcervoVO itemRegistroEntradaAcervoVO : registroEntradaAcervoVO.getItemRegistroEntradaAcervoVOs()) {
            if (!itemRegistroEntradaAcervoVO.getExemplar().getSituacaoAtual().equals(SituacaoExemplar.DISPONIVEL.getValor()) 
            		&& !itemRegistroEntradaAcervoVO.getExemplar().getSituacaoAtual().equals(SituacaoExemplar.EMPRESTADO.getValor())
            		&& !itemRegistroEntradaAcervoVO.getExemplar().getSituacaoAtual().equals(SituacaoExemplar.CONSULTA.getValor())) {
                getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(itemRegistroEntradaAcervoVO.getExemplar(), SituacaoExemplar.DISPONIVEL.getValor(), usuario);
                if(itemRegistroEntradaAcervoVO.getTipoEntrada().equals(TipoEntradaAcervo.RESTAURACAO.getValor())){
                	getFacadeFactory().getHistoricoExemplarFacade().registrarHistoricoExemplarParaRegistroAcervo(itemRegistroEntradaAcervoVO.getExemplar(), SituacaoHistoricoExemplar.DEVOLVIDO_RESTAURACAO.getValor(), usuario);
                }else{
                	getFacadeFactory().getHistoricoExemplarFacade().registrarHistoricoExemplarParaRegistroAcervo(itemRegistroEntradaAcervoVO.getExemplar(), SituacaoHistoricoExemplar.RECUPERADO.getValor(), usuario);
                }
            } else {
                getFacadeFactory().getItemRegistroEntradaAcervoFacade().atualizarTipoEntradaItemRegistroEntradaAcervo(itemRegistroEntradaAcervoVO);
            }
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroEntradaAcervoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RegistroEntradaAcervoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroEntradaAcervoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {

            RegistroEntradaAcervoVO.validarDados(obj);
            RegistroEntradaAcervo.alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder();
            sql.append("UPDATE RegistroEntradaAcervo set data=?, funcionario=?, biblioteca=? WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
//					sqlAlterar.setString(2, obj.getTipoEntrada());
                    if (obj.getFuncionario().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getFuncionario().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    if (obj.getBiblioteca().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getBiblioteca().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setInt(4, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getItemRegistroEntradaAcervoFacade().alterarItemRegistroEntradaAcervos(obj.getCodigo(), obj.getItemRegistroEntradaAcervoVOs());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroEntradaAcervoVO</code>. Sempre localiza
     * o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RegistroEntradaAcervoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(RegistroEntradaAcervoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
        	final StringBuilder sql = new StringBuilder();
            RegistroEntradaAcervo.excluir(getIdEntidade(), true, usuarioVO);
            sql.append("DELETE FROM RegistroEntradaAcervo WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
            getConexao().getJdbcTemplate().update(sql.toString(), new Object[]{obj.getCodigo()});
            getFacadeFactory().getItemRegistroEntradaAcervoFacade().excluirItemRegistroEntradaAcervos(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirExemplarItemRegistroEntradaAcervo(Integer exemplar, UsuarioVO usuario) throws Exception {
        String sql = "delete from itemregistroentradaacervo where (exemplar = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{exemplar});
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirRegistrarEntradaAcervoExemplar(CatalogoVO catalogo) throws Exception {
        Integer exemplar = 0;
        if (!catalogo.getExemplarVOs().isEmpty()) {
            Iterator<ExemplarVO> i = catalogo.getExemplarVOs().iterator();
            while (i.hasNext()) {
                exemplar = ((ExemplarVO)i.next()).getCodigo();
                Integer codRegistroEntradaAcervo = 0;
                String sql = "SELECT registroEntradaAcervo FROM ItemRegistroEntradaAcervo WHERE exemplar = ?";
                SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{exemplar});
                if (!tabelaResultado.next()) {
                    codRegistroEntradaAcervo = 0;
                } else {
                    codRegistroEntradaAcervo = tabelaResultado.getInt("registroEntradaAcervo");
                }                
                if (codRegistroEntradaAcervo != 0) {
                    getFacadeFactory().getItemRegistroEntradaAcervoFacade().excluirItemRegistroEntradaAcervos(codRegistroEntradaAcervo);
                    sql = "DELETE FROM RegistroEntradaAcervo WHERE (codigo = ?)";
                    getConexao().getJdbcTemplate().update(sql, new Object[]{codRegistroEntradaAcervo});
                }
            }
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaAcervo</code> através do valor do atributo
     * <code>nome</code> da classe <code>Biblioteca</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>RegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<RegistroEntradaAcervoVO> consultarPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroEntradaAcervo.* FROM RegistroEntradaAcervo, Biblioteca WHERE RegistroEntradaAcervo.biblioteca = Biblioteca.codigo and upper(sem_acentos(Biblioteca.nome)) ilike(sem_acentos(?)) ";
        if(Uteis.isAtributoPreenchido(unidadeEnsino)){
        	sqlStr += " and exists (select unidadeensinobiblioteca.codigo from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca = biblioteca.codigo and unidadeensinobiblioteca.unidadeensino = "+unidadeEnsino;
        }
        sqlStr += " ORDER BY Biblioteca.nome, RegistroEntradaAcervo.data";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, "%"+ valorConsulta.toUpperCase() + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaAcervo</code> através do valor do atributo
     * <code>nome</code> da classe <code>Usuario</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza
     * o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>RegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<RegistroEntradaAcervoVO> consultarPorNomeUsuario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroEntradaAcervo.* FROM RegistroEntradaAcervo, Usuario WHERE RegistroEntradaAcervo.funcionario = Usuario.codigo and upper( sem_acentos(Usuario.nome) ) like(sem_acentos(?))";
        if(Uteis.isAtributoPreenchido(unidadeEnsino)){
        	sqlStr += " and exists (select unidadeensinobiblioteca.codigo from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca = RegistroEntradaAcervo.biblioteca and unidadeensinobiblioteca.unidadeensino = "+unidadeEnsino;
        }
        sqlStr += " ORDER BY Usuario.nome, RegistroEntradaAcervo.data ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<RegistroEntradaAcervoVO> consultarPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct RegistroEntradaAcervo.* FROM RegistroEntradaAcervo inner join itemRegistroEntradaAcervo on itemRegistroEntradaAcervo.RegistroEntradaAcervo = RegistroEntradaAcervo.codigo inner join Exemplar on Exemplar.codigo = itemRegistroEntradaAcervo.exemplar ";
        sqlStr += " where exemplar.codigobarra ilike ? ";
        if(Uteis.isAtributoPreenchido(unidadeEnsino)){
        	sqlStr += " and exists (select unidadeensinobiblioteca.codigo from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca = RegistroEntradaAcervo.biblioteca and unidadeensinobiblioteca.unidadeensino = "+unidadeEnsino;
        }
        sqlStr += " ORDER BY RegistroEntradaAcervo.data ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr,  "%" + valorConsulta+ "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<RegistroEntradaAcervoVO> consultarPorCatalogo(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT distinct RegistroEntradaAcervo.* FROM RegistroEntradaAcervo inner join itemRegistroEntradaAcervo on itemRegistroEntradaAcervo.RegistroEntradaAcervo = RegistroEntradaAcervo.codigo inner join Exemplar on Exemplar.codigo = itemRegistroEntradaAcervo.exemplar inner join Catalogo on Catalogo.codigo = exemplar.catalogo ";        
       	sqlStr += " where upper(sem_acentos(catalogo.titulo)) like(sem_acentos(?)) ";        
        if(Uteis.isAtributoPreenchido(unidadeEnsino)){
        	sqlStr += " and exists (select unidadeensinobiblioteca.codigo from unidadeensinobiblioteca where unidadeensinobiblioteca.biblioteca = RegistroEntradaAcervo.biblioteca and unidadeensinobiblioteca.unidadeensino = "+unidadeEnsino;
        }
        sqlStr += " ORDER BY RegistroEntradaAcervo.data ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaAcervo</code> através do valor do atributo
     * <code>String tipoEntrada</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>RegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<RegistroEntradaAcervoVO> consultarPorTipoEntrada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroEntradaAcervo.* FROM RegistroEntradaAcervo "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = RegistroEntradaAcervo.biblioteca "
        		+ " WHERE upper( RegistroEntradaAcervo.tipoEntrada ) like(?) ";
        if (unidadeEnsino > 0) {
        	sqlStr += " and unidadeensinobiblioteca.unidadeEnsino = " + unidadeEnsino;
        }
		sqlStr += " ORDER BY RegistroEntradaAcervo.tipoEntrada";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase() + "%");
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaAcervo</code> através do valor do atributo
     * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>RegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<RegistroEntradaAcervoVO> consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT RegistroEntradaAcervo.* FROM RegistroEntradaAcervo "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = RegistroEntradaAcervo.biblioteca "
        		+ " WHERE registroentradaacervo.data >= '").append(Uteis.getDataJDBC(prmIni));
        sqlStr.append("' and data <= '").append(Uteis.getDataJDBC(prmFim)).append("' ");
        if (unidadeEnsino > 0) {
        	sqlStr.append(" and unidadeensinobiblioteca.unidadeEnsino = ").append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY registroentradaacervo.data;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado,nivelMontarDados, usuario));
    }


    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaAcervo</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>RegistroEntradaAcervoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Override
    public List<RegistroEntradaAcervoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT RegistroEntradaAcervo.* FROM RegistroEntradaAcervo "
        		+ " left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = RegistroEntradaAcervo.biblioteca "
        		+ " WHERE RegistroEntradaAcervo.codigo >= " + valorConsulta.intValue();
        if (unidadeEnsino > 0) {
        	sqlStr += " and unidadeensinobiblioteca.unidadeEnsino = " + unidadeEnsino;
        }
		sqlStr += " ORDER BY RegistroEntradaAcervo.codigo ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>RegistroEntradaAcervoVO</code> resultantes da consulta.
     */
    public static List<RegistroEntradaAcervoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<RegistroEntradaAcervoVO> vetResultado = new ArrayList<RegistroEntradaAcervoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>RegistroEntradaAcervoVO</code>.
     *
     * @return O objeto da classe <code>RegistroEntradaAcervoVO</code> com os dados devidamente montados.
     */
    public static RegistroEntradaAcervoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        RegistroEntradaAcervoVO obj = new RegistroEntradaAcervoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setData(dadosSQL.getDate("data"));
        obj.getFuncionario().setCodigo(dadosSQL.getInt("funcionario"));
        obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosFuncionario(obj, nivelMontarDados, usuario);
        montarDadosBiblioteca(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setItemRegistroEntradaAcervoVOs(getFacadeFactory().getItemRegistroEntradaAcervoFacade().consultarItemRegistroEntradaAcervos(obj.getCodigo(), false, nivelMontarDados, usuario));
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>BibliotecaVO</code> relacionado ao objeto
     * <code>RegistroEntradaAcervoVO</code>. Faz uso da chave primária da classe <code>BibliotecaVO</code> para realizar
     * a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosBiblioteca(RegistroEntradaAcervoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getBiblioteca().getCodigo().intValue() == 0) {
            obj.setBiblioteca(new BibliotecaVO());
            return;
        }
        obj.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(obj.getBiblioteca().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto
     * <code>RegistroEntradaAcervoVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a
     * consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosFuncionario(RegistroEntradaAcervoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionario().getCodigo().intValue() == 0) {
            obj.setFuncionario(new UsuarioVO());
            return;
        }
        obj.setFuncionario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getFuncionario().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroEntradaAcervoVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroEntradaAcervoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM RegistroEntradaAcervo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroEntradaAcervo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        RegistroEntradaAcervo.idEntidade = idEntidade;
    }
}
