package negocio.facade.jdbc.crm;

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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboRegistroEntradaProspectsEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisEmail;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.RegistroEntradaProspectsInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RegistroEntradaProspectsVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>RegistroEntradaProspectsVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see RegistroEntradaProspectsVO
 * @see SuperEntidade
 * @see RegistroEntrada
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroEntradaProspects extends ControleAcesso implements RegistroEntradaProspectsInterfaceFacade {

    protected static String idEntidade;

    private static interface RegistroEntradaProspectsInterfaceFacade {
    }

    public RegistroEntradaProspects() throws Exception {
        super();
        setIdEntidade("RegistroEntrada");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>RegistroEntradaProspectsVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>RegistroEntradaProspectsVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroEntradaProspectsVO obj, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);
        if (obj.getProspects().getCodigo() == 0) {
            if (obj.getRegistroEntrada().getCursoEntrada().getCodigo() != 0) {
                CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
                cursoInteresseVO.setCurso(obj.getRegistroEntrada().getCursoEntrada());
                obj.getProspects().getCursoInteresseVOs().add(cursoInteresseVO);
            }
            getFacadeFactory().getProspectsFacade().incluirSemValidarDados(obj.getProspects(), controlarAcesso, usuarioLogado, null);
            if(!obj.getProspects().getHistoricoFollowUp().getObservacao().isEmpty()){
                obj.getProspects().getHistoricoFollowUp().setProspect(obj.getProspects());
                getFacadeFactory().getHistoricoFollowUpFacade().incluir(obj.getProspects().getHistoricoFollowUp(), usuarioLogado);
            }
        }
        final String sql = "INSERT INTO RegistroEntradaProspects( registroEntrada, prospects, indicadopeloprospect, campanha, vendedor, dataindicacaoprospect, existeProspects ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getRegistroEntrada().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getRegistroEntrada().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getProspects().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getProspects().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                if (obj.getIndicadoPeloProspect().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getIndicadoPeloProspect().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                if (obj.getCampanha().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(4, obj.getCampanha().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(4, 0);
                }
                if (obj.getVendedor().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(5, obj.getVendedor().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(5, 0);
                }
                sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataIndicacaoProspect()));
                //Prospepct por default ja vem como Nao Registrado, caso seja salvo, o prospect passa a existir
                sqlInserir.setBoolean(7, true);
                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
        obj.setNovoObj(Boolean.FALSE);
    }

    
    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>RegistroEntradaProspectsVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroEntradaProspectsVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroEntradaProspectsVO obj, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        validarDados(obj);
        realizarUpperCaseDados(obj);

        if (obj.getRegistroEntrada().getCursoEntrada().getCodigo() != 0) {
            CursoInteresseVO cursoInteresseVO = new CursoInteresseVO();
            cursoInteresseVO.setCurso(obj.getRegistroEntrada().getCursoEntrada());
            obj.getProspects().getCursoInteresseVOs().add(cursoInteresseVO);
        }
        getFacadeFactory().getProspectsFacade().persistir(obj.getProspects(), controlarAcesso, usuarioLogado, null);
        final String sql = "UPDATE RegistroEntradaProspects set registroEntrada=?, prospects=?, indicadopeloprospect=?, campanha=?, vendedor=?, dataindicacaoprospect=?, existeProspects=? WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getRegistroEntrada().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getRegistroEntrada().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                if (obj.getProspects().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getProspects().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                if (obj.getIndicadoPeloProspect().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getIndicadoPeloProspect().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                if (obj.getCampanha().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(4, obj.getCampanha().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(4, 0);
                }
                if (obj.getVendedor().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(5, obj.getVendedor().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(5, 0);
                }
                sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataIndicacaoProspect()));
                sqlAlterar.setBoolean(7, obj.isExisteProspects().booleanValue());
                sqlAlterar.setInt(8, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>RegistroEntradaProspectsVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>RegistroEntradaProspectsVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(RegistroEntradaProspectsVO obj, UsuarioVO usuario) throws Exception {
        RegistroEntradaProspects.excluir(getIdEntidade());
        if (obj.isNovoObj()) {
            throw new Exception(UteisJSF.internacionalizar("msg_registroNaoGravado"));
        }
        String sql = "DELETE FROM RegistroEntradaProspects WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>RegistroEntradaProspectsVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
     */
    public void validarDados(RegistroEntradaProspectsVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        ConsistirException consistir = new ConsistirException();

        if (consistir.existeErroListaMensagemErro()) {
            throw consistir;
        }

    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>RegistroEntradaProspectsVO</code>.
     */
    public void validarUnicidade(List<RegistroEntradaProspectsVO> lista, RegistroEntradaProspectsVO obj) throws ConsistirException {
        for (RegistroEntradaProspectsVO repetido : lista) {
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
     */
    public void realizarUpperCaseDados(RegistroEntradaProspectsVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis na Tela RegistroEntradaProspectsCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public List<RegistroEntradaProspectsVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        if (campoConsulta.equals(TipoConsultaComboRegistroEntradaProspectsEnum.CODIGO.toString())) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        if (campoConsulta.equals("DescricaoRegistroEntrada")) {
            if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
            }
            return consultarPorDescricaoRegistroEntrada(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaProspects</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>RegistroEntrada</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>RegistroEntradaProspectsVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoRegistroEntrada(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        valorConsulta += "%";
        String sqlStr = "SELECT RegistroEntradaProspects.* FROM RegistroEntradaProspects, RegistroEntrada WHERE RegistroEntradaProspects.registroEntrada = RegistroEntrada.codigo and upper( RegistroEntrada.descricao ) like(?) ORDER BY RegistroEntrada.descricao";
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado);
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroEntradaProspects</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>RegistroEntradaProspectsVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
        String sqlStr = "SELECT * FROM RegistroEntradaProspects WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuarioLogado));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>RegistroEntradaProspectsVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>RegistroEntradaProspectsVO</code>.
     * @return  O objeto da classe <code>RegistroEntradaProspectsVO</code> com os dados devidamente montados.
     */
    public static RegistroEntradaProspectsVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        RegistroEntradaProspectsVO obj = new RegistroEntradaProspectsVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getRegistroEntrada().setCodigo(new Integer(dadosSQL.getInt("registroEntrada")));
        obj.getProspects().setCodigo(new Integer(dadosSQL.getInt("prospects")));
        obj.setExisteProspects(new Boolean(dadosSQL.getBoolean("existeProspects")));
        obj.setNovoObj(new Boolean(false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosProspects(obj, nivelMontarDados, usuarioLogado);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ProspectsVO</code> relacionado ao objeto <code>RegistroEntradaProspectsVO</code>.
     * Faz uso da chave primária da classe <code>ProspectsVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosProspects(RegistroEntradaProspectsVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        if (obj.getProspects().getCodigo().intValue() == 0) {
            obj.setProspects(new ProspectsVO());
            return;
        }
        obj.setProspects(new Prospects().consultarPorChavePrimaria(obj.getProspects().getCodigo(), nivelMontarDados, usuarioLogado));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>RegistroEntradaProspectsVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>RegistroEntradaProspects</code>.
     * @param <code>registroEntrada</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirRegistroEntradaProspectss(Integer registroEntrada, UsuarioVO usuario) throws Exception {
        RegistroEntradaProspects.excluir(getIdEntidade());
        String sql = "DELETE FROM RegistroEntradaProspects WHERE registroEntrada = " + registroEntrada +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql);
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>RegistroEntradaProspectsVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirRegistroEntradaProspectss</code> e <code>incluirRegistroEntradaProspectss</code> disponíveis na classe <code>RegistroEntradaProspects</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarRegistroEntradaProspectss(RegistroEntradaVO registroEntrada, List objetos, UsuarioVO usuarioLogado) throws Exception {
        String str = "DELETE FROM RegistroEntradaProspects WHERE registroEntrada = " + registroEntrada.getCodigo();
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            RegistroEntradaProspectsVO objeto = (RegistroEntradaProspectsVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(str);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            RegistroEntradaProspectsVO objeto = (RegistroEntradaProspectsVO) e.next();
            if (objeto.getRegistroEntrada().getCodigo().equals(0)) {
                objeto.setRegistroEntrada(registroEntrada);
            }
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto, true, usuarioLogado);
            } else {
                alterar(objeto, true, usuarioLogado);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>RegistroEntradaProspectsVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>crm.RegistroEntrada</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Override
    public void incluirRegistroEntradaProspectss(final RegistroEntradaVO registroEntrada, final List<RegistroEntradaProspectsVO> objetos, final UsuarioVO usuarioLogado) throws Exception {
    	ConsistirException consistirException =  new ConsistirException();
    	ProcessarParalelismo.executar(0, objetos.size(), consistirException, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				RegistroEntradaProspectsVO obj = (RegistroEntradaProspectsVO) objetos.get(i);
				try {	                
	                obj.setRegistroEntrada(registroEntrada);
	                incluir(obj, true, usuarioLogado);				
				} catch (Exception e2) {
					
				}
			}
		});
        

    }

    /**
     * Operação responsável por consultar todos os <code>RegistroEntradaProspectsVO</code> relacionados a um objeto da classe <code>crm.RegistroEntrada</code>.
     * @param registroEntrada  Atributo de <code>crm.RegistroEntrada</code> a ser utilizado para localizar os objetos da classe <code>RegistroEntradaProspectsVO</code>.
     * @return List  Contendo todos os objetos da classe <code>RegistroEntradaProspectsVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarRegistroEntradaProspects(Integer registroEntrada, int nivelMontarDados, Integer limit, Integer offset, UsuarioVO usuarioLogado) throws Exception {
        RegistroEntradaProspects.consultar(getIdEntidade());
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM RegistroEntradaProspects WHERE registroentrada = ").append(registroEntrada);
        
        if (limit != null) {
            sql.append(" LIMIT ").append(limit);
            if (offset != null) {
                sql.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado));


    }

    public Integer consultarTotalDeRegistroRegistroEntradaProspects(Integer registroEntrada, UsuarioVO usuarioLogado) throws Exception {
        RegistroEntradaProspects.consultar(getIdEntidade());
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT COUNT(RegistroEntradaProspects.codigo)  FROM RegistroEntradaProspects WHERE registroentrada = ").append(registroEntrada);
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        } else {
            return 0;
        }


    }

    /**
     * Operação responsável por localizar um objeto da classe <code>RegistroEntradaProspectsVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public RegistroEntradaProspectsVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuarioLogado);
        String sql = "SELECT * FROM RegistroEntradaProspects WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroEntradaProspects ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
    }

    public List<RegistroEntradaProspectsVO> consultarUnicidade(RegistroEntradaProspectsVO obj, boolean alteracao, UsuarioVO usuarioLogado) throws Exception {
        super.verificarPermissaoConsultar(getIdEntidade(), false, usuarioLogado);
        return new ArrayList(0);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroEntradaProspects.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        RegistroEntradaProspects.idEntidade = idEntidade;
    }
    
    @Override
    public void enviarProspectsParaRdStation(final List<RegistroEntradaProspectsVO> objetos, final UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO config) {
    	
    	ArrayList<ProspectsVO> prospectsVOs = new ArrayList<>();
    	for(RegistroEntradaProspectsVO registro : objetos) {
    		prospectsVOs.add(registro.getProspects());
    	}
    	getFacadeFactory().getLeadInterfaceFacade().incluirListaDeLeadsNoRdStation(prospectsVOs, config);
    }
    
    @Override
    public Integer enviarProspectParaRdStation(RegistroEntradaProspectsVO entradaProspectsVO) {

		try {
			
			if(entradaProspectsVO.getProspects().getEmailPrincipal() == null || !UteisEmail.getValidEmail(entradaProspectsVO.getProspects().getEmailPrincipal())) {
				return 400;
			}

			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			
			// Realiza a inclusao da lead no RD Station
			return getFacadeFactory().getLeadInterfaceFacade().incluirLeadNoRdStation(entradaProspectsVO.getProspects(), config);
				
		} catch (Exception e) {
			StringBuilder msg = new StringBuilder("Não foi possivel salvar o prospect: ").append(entradaProspectsVO.getProspects().getNome())
							.append(" - ").append(entradaProspectsVO.getProspects().getEmailPrincipal());
			System.out.println(msg.toString());
			System.out.println(e.getMessage());
			// Codigo de erro generico do servidor
			return 500;
		}
		
	}

}