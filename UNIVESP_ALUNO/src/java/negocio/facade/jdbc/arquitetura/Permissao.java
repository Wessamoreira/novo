package negocio.facade.jdbc.arquitetura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.PermissaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.PermissaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PermissaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PermissaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PermissaoVO
 * @see ControleAcesso
 * @see PerfilAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Permissao extends ControleAcesso implements PermissaoInterfaceFacade {

    protected static String idEntidade;

    public Permissao() throws Exception {
        super();
        setIdEntidade("PerfilAcesso");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PermissaoVO</code>.
     */
    public PermissaoVO novo() throws Exception {
        Permissao.incluir(getIdEntidade());
        PermissaoVO obj = new PermissaoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PermissaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PermissaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PermissaoVO obj, final UsuarioVO usuarioVO) throws Exception {
        PermissaoVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle 
         * Comentado 28/10/2014
         *  Classe Subordinada
         */ 
        //Permissao.incluir(getIdEntidade());
        final String sql = "INSERT INTO Permissao( codPerfilAcesso, nomeEntidade, permissoes, tituloApresentacao, tipoPermissao, valorEspecifico, valorInicial, valorFinal, responsavel ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) " +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getCodPerfilAcesso().intValue());
                sqlInserir.setString(2, obj.getNomeEntidade());
                sqlInserir.setString(3, obj.getPermissoes());
                sqlInserir.setString(4, obj.getTituloApresentacao());
                sqlInserir.setInt(5, obj.getTipoPermissao().intValue());
                sqlInserir.setString(6, obj.getValorEspecifico());
                sqlInserir.setString(7, obj.getValorInicial());
                sqlInserir.setString(8, obj.getValorFinal());
                if (obj.getResponsavel() == null || obj.getResponsavel() == 0) {
                	sqlInserir.setInt(9, usuarioVO.getCodigo());
                } else {
                	sqlInserir.setInt(9, obj.getResponsavel());	
                }
                return sqlInserir;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PermissaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PermissaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PermissaoVO obj, final UsuarioVO usuarioVO) throws Exception {
        PermissaoVO.validarDados(obj);
        /**
         * @author Leonardo Riciolle 
         * Comentado 28/10/2014
         *  Classe Subordinada
         */ 
        // Permissao.alterar(getIdEntidade());
        final String sql = "UPDATE Permissao set permissoes=?, tituloApresentacao=?, tipoPermissao=?, valorEspecifico=?, valorInicial=?, valorFinal=?, responsavel=? WHERE ((codPerfilAcesso = ?) and (nomeEntidade = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getPermissoes());
                sqlAlterar.setString(2, obj.getTituloApresentacao());
                sqlAlterar.setInt(3, obj.getTipoPermissao().intValue());
                sqlAlterar.setString(4, obj.getValorEspecifico());
                sqlAlterar.setString(5, obj.getValorInicial());
                sqlAlterar.setString(6, obj.getValorFinal());
                if (obj.getResponsavel() == null || obj.getResponsavel() == 0) {
                	sqlAlterar.setInt(7, usuarioVO.getCodigo());
                } else {
                	sqlAlterar.setInt(7, obj.getResponsavel());	
                }
                sqlAlterar.setInt(8, obj.getCodPerfilAcesso().intValue());
                sqlAlterar.setString(9, obj.getNomeEntidade());
                return sqlAlterar;
            }
        })==0){
        	incluir(obj, usuarioVO);
        };
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PermissaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PermissaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PermissaoVO obj) throws Exception {
    	/**
    	  * @author Leonardo Riciolle 
    	  * Comentado 28/10/2014
    	  *  Classe Subordinada
    	  */ 
        // Permissao.excluir(getIdEntidade());
        String sql = "DELETE FROM Permissao WHERE ((codPerfilAcesso = ?) and (nomeEntidade = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodPerfilAcesso().intValue(), obj.getNomeEntidade()});
    }

    /**
     * Responsável por realizar uma consulta de <code>Permissao</code> através do valor do atributo 
     * <code>String nomeEntidade</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PermissaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PermissaoVO> consultarPorNomeEntidade(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Permissao WHERE nomeEntidade like('" + valorConsulta + "%') ORDER BY nomeEntidade";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado));
    }

    /**
     * Responsável por realizar uma consulta de <code>Permissao</code> através do valor do atributo 
     * <code>nome</code> da classe <code>PerfilAcesso</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>PermissaoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PermissaoVO> consultarPorNomePerfilAcesso(String valorConsulta, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT Permissao.* FROM Permissao, PerfilAcesso WHERE Permissao.codPerfilAcesso = PerfilAcesso.codigo and PerfilAcesso.nome like('" + valorConsulta + "%') ORDER BY PerfilAcesso.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado);
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PermissaoVO</code> resultantes da consulta.
     */
    public static List<PermissaoVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<PermissaoVO> vetResultado = new ArrayList<PermissaoVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PermissaoVO</code>.
     * @return  O objeto da classe <code>PermissaoVO</code> com os dados devidamente montados.
     */
    public static PermissaoVO montarDados(SqlRowSet dadosSQL) throws Exception {
        PermissaoVO obj = new PermissaoVO();
        obj.setCodPerfilAcesso(new Integer(dadosSQL.getInt("codPerfilAcesso")));
        obj.setNomeEntidade(dadosSQL.getString("nomeEntidade"));
        obj.setPermissoes(dadosSQL.getString("permissoes"));
        obj.setTipoPermissao(new Integer(dadosSQL.getInt("tipoPermissao")));
        obj.setTituloApresentacao(dadosSQL.getString("tituloApresentacao"));
        obj.setValorEspecifico(dadosSQL.getString("valorEspecifico"));
        obj.setValorInicial(dadosSQL.getString("valorInicial"));
        obj.setValorFinal(dadosSQL.getString("valorFinal"));
        obj.getTotal();
        obj.getTotalSemExcluir();
        obj.getIncluir();
        obj.getAlterar();
        obj.getExcluir();
        obj.getConsultar();
        obj.setPermissao(PerfilAcessoModuloEnum.getEnumPorValor(dadosSQL.getString("nomeEntidade")));
        obj.setNovoObj(Boolean.FALSE);

        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>PermissaoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>Permissao</code>.
     * @param <code>codPerfilAcesso</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPermissaos(Integer codPerfilAcesso, List<PermissaoVO> objetos) throws Exception {
    	StringBuilder sql  = new StringBuilder("delete from permissao where codperfilacesso = ? and nomeentidade in (''");
    	Iterator<PermissaoVO> e = objetos.iterator();
        while (e.hasNext()) {
            PermissaoVO obj = (PermissaoVO) e.next();
            getFacadeFactory().getPerfilAcessoFacade().realizarDefinicaoInformacaoPermissao(obj);
            if(obj.getNomeEntidade().equals("Comunicação Interna: Enviar Para Cargo")){
            	obj.getPermissoes().trim().isEmpty();
            }
            if(obj.getPermissoes().trim().isEmpty()){
            	sql.append(",'").append(obj.getNomeEntidade()).append("'");
            }
           
            for(PermissaoVO permissaoVO:obj.getFuncionalidades()){
        		getFacadeFactory().getPerfilAcessoFacade().realizarDefinicaoInformacaoPermissao(permissaoVO);
        		if(obj.getTitulo().equals("Comunicação Interna: Enviar Para Coordenador")){
        			permissaoVO.getPermissoes().trim().isEmpty();
                }
        		if(permissaoVO.getPermissoes().trim().isEmpty() || obj.getPermissoes().trim().isEmpty()){       			
        			sql.append(",'").append(permissaoVO.getNomeEntidade()).append("'");
        		}
        	}
        }
        sql.append(")");
        getConexao().getJdbcTemplate().update(sql.toString(), codPerfilAcesso);
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>PermissaoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirPermissaos</code> e <code>incluirPermissaos</code> disponíveis na classe <code>Permissao</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void alterarPermissaos(Integer codPerfilAcesso, List<PermissaoVO> objetos, UsuarioVO usuarioVO) throws Exception {
        excluirPermissaos(codPerfilAcesso, objetos);
        Iterator<PermissaoVO> e = objetos.iterator();
        while (e.hasNext()) {
            PermissaoVO obj = (PermissaoVO) e.next();
            
            getFacadeFactory().getPerfilAcessoFacade().realizarDefinicaoInformacaoPermissao(obj);
            if(obj.getNomeEntidade().equals("QuestaoOnline")){
            	obj.getPermissoes().trim().isEmpty();
            }
            if(!obj.getPermissoes().trim().isEmpty()){
            	obj.setCodPerfilAcesso(codPerfilAcesso);
            	alterar(obj, usuarioVO);
            	for(PermissaoVO permissaoVO:obj.getFuncionalidades()){
            		getFacadeFactory().getPerfilAcessoFacade().realizarDefinicaoInformacaoPermissao(permissaoVO);
            		if(!permissaoVO.getPermissoes().trim().isEmpty()){
            			permissaoVO.setCodPerfilAcesso(codPerfilAcesso);
            			alterar(permissaoVO, usuarioVO);
            		}
            	}
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>PermissaoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>arquitetura.PerfilAcesso</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void incluirPermissaos(Integer codPerfilAcessoPrm, List<PermissaoVO> objetos, UsuarioVO usuarioVO) throws Exception {
        Iterator<PermissaoVO> e = objetos.iterator();
        while (e.hasNext()) {
            PermissaoVO obj = (PermissaoVO) e.next();
            getFacadeFactory().getPerfilAcessoFacade().realizarDefinicaoInformacaoPermissao(obj);
            if(!obj.getPermissoes().trim().isEmpty()){
            	obj.setCodPerfilAcesso(codPerfilAcessoPrm);
            	incluir(obj, usuarioVO);
            	for(PermissaoVO permissaoVO:obj.getFuncionalidades()){
            		getFacadeFactory().getPerfilAcessoFacade().realizarDefinicaoInformacaoPermissao(permissaoVO);
            		if(!permissaoVO.getPermissoes().trim().isEmpty()){
            			permissaoVO.setCodPerfilAcesso(codPerfilAcessoPrm);
                    	incluir(permissaoVO, usuarioVO);
            		}
            	}
            }
        }
    }

    /**
     * Operação responsável por consultar todos os <code>PermissaoVO</code> relacionados a um objeto da classe <code>arquitetura.PerfilAcesso</code>.
     * @param codPerfilAcesso  Atributo de <code>arquitetura.PerfilAcesso</code> a ser utilizado para localizar os objetos da classe <code>PermissaoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>PermissaoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List<PermissaoVO> consultarPermissaos(Integer codPerfilAcesso) throws Exception {
        Permissao.consultar(getIdEntidade());
        List<PermissaoVO> objetos = new ArrayList<PermissaoVO>();
        String sql = "SELECT * FROM Permissao WHERE codPerfilAcesso = "+codPerfilAcesso.intValue()+" order by tipoPermissao ";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        while (resultado.next()) {
            objetos.add(Permissao.montarDados(resultado));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PermissaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PermissaoVO consultarPorChavePrimaria(Integer codPerfilAcessoPrm, String nomeEntidadePrm, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Permissao WHERE codPerfilAcesso = ?, nomeEntidade = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codPerfilAcessoPrm.intValue(), nomeEntidadePrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado));
    }

    public List<PermissaoVO> consultarPermissaosComMaisDeUmPerfil(Integer usuario, Integer unidadeEnsino, Integer codPerfilAcesso) throws Exception {
        Permissao.consultar(getIdEntidade());
        StringBuilder sql = new StringBuilder("SELECT DISTINCT p.nomeEntidade, p.permissoes, p.tipoPermissao, p.tituloApresentacao, p.valorEspecifico, ");
        sql.append("p.valorInicial, p.valorFinal FROM permissao p ");
        sql.append("INNER JOIN perfilacesso pa ON pa.codigo = p.codperfilacesso ");
        sql.append("WHERE pa.codigo IN( ");
        sql.append("SELECT pa2.codigo FROM perfilacesso pa2 ");
        sql.append("INNER JOIN usuarioperfilacesso upa2 ON upa2.perfilacesso = pa2.codigo ");
        sql.append("WHERE usuario = ").append(usuario);
        if (unidadeEnsino == null || unidadeEnsino.equals(0)) {
            sql.append(" AND unidadeensino is null) ");
        } else {
            sql.append(" AND unidadeensino = ").append(unidadeEnsino).append(") ");
        }
        sql.append("ORDER BY p.nomeentidade");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<PermissaoVO> objetos = new ArrayList(0);
        HashMap<String, String> hashPermissoes = new HashMap<String, String>(0);
        while (resultado.next()) {
            objetos.add(montarDadosComMaisDeUmPerfil(resultado, codPerfilAcesso, hashPermissoes));
        }
        return objetos;
    }

    public static PermissaoVO montarDadosComMaisDeUmPerfil(SqlRowSet dadosSQL, Integer codPerfilAcesso, HashMap<String, String> hashPermissoes) throws Exception {
        PermissaoVO obj = new PermissaoVO();
        obj.setCodPerfilAcesso(codPerfilAcesso);
        obj.setNomeEntidade(dadosSQL.getString("nomeEntidade"));
        obj.setPermissoes(dadosSQL.getString("permissoes"));
        obj.setTipoPermissao(new Integer(dadosSQL.getInt("tipoPermissao")));
        obj.setTituloApresentacao(dadosSQL.getString("tituloApresentacao"));
        obj.setValorEspecifico(dadosSQL.getString("valorEspecifico"));
        obj.setValorInicial(dadosSQL.getString("valorInicial"));
        obj.setValorFinal(dadosSQL.getString("valorFinal"));
        obj.getTotal();
        obj.getTotalSemExcluir();
        obj.getIncluir();
        obj.getAlterar();
        obj.getExcluir();
        obj.getConsultar();
        if (hashPermissoes.containsKey(obj.getNomeEntidade())) {
            String permissoes = hashPermissoes.get(obj.getNomeEntidade());
            if (!permissoes.equals(obj.getPermissoes())) {
                obj.setPermissoes(obj.getPermissoes() + permissoes);
                hashPermissoes.put(obj.getNomeEntidade(), obj.getPermissoes());
            }
        } else {
            hashPermissoes.put(obj.getNomeEntidade(), obj.getPermissoes());
        }
        obj.setNovoObj(Boolean.FALSE);

        return obj;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Permissao.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Permissao.idEntidade = idEntidade;
    }
    
    @Override
    public void realizarGeracaoListaPermissaoPorAmbiente(PerfilAcessoVO perfilAcessoVO, TipoVisaoEnum tipoVisaoEnum, Boolean forcarAlteracaoAmbiente, Cliente cliente){
    	if(!Uteis.isAtributoPreenchido(perfilAcessoVO.getCodigo())){
    		perfilAcessoVO.getPermissaoVOs().clear();
    	}
    	List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> perfilAcessoPermissaoEnums = PerfilAcessoModuloEnum.getListaEntidadePorTipoVisaoSistema(tipoVisaoEnum);
    	aqui:
    	for(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissaoEnum: perfilAcessoPermissaoEnums){
    		PerfilAcessoPermissaoEnumInterface permissao = (PerfilAcessoPermissaoEnumInterface) permissaoEnum;    		
			if (permissao.getUtilizaVisao(tipoVisaoEnum) && validarPermissaoModuloHabilitadoCliente(cliente, permissao)) {					
				for(PermissaoVO permissaoVO:perfilAcessoVO.getPermissaoVOs()){
					if(permissaoVO.getNomeEntidade().equals(permissao.getValor())){								
						permissaoVO.setPermissao(permissaoEnum);
						permissaoVO.setTipoVisao(tipoVisaoEnum);
						permissaoVO.setTipoPermissao(permissao.getTipoPerfilAcesso().getValor());
						realizarGeracaoListaFuncionalidadePorEntidade(perfilAcessoVO, tipoVisaoEnum, permissaoVO);
						continue aqui;
					}
				}
				PermissaoVO permissaoVO = realizarCriacaoPermissaoVOPorPerfilAcessoPermissaoEnum(perfilAcessoVO, tipoVisaoEnum, permissaoEnum);
				perfilAcessoVO.getPermissaoVOs().add(permissaoVO);
			}
    	} 
    	
    		for (Iterator<PermissaoVO> iterator = perfilAcessoVO.getPermissaoVOs().iterator(); iterator.hasNext();) {
    			PermissaoVO permissaoVO = iterator.next();    			    			
    			if(!Uteis.isAtributoPreenchido(permissaoVO.getPermissao()) || ((!((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao()).getUtilizaVisao(tipoVisaoEnum)  
    					|| !validarPermissaoModuloHabilitadoCliente(cliente, ((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao()))) 
    					&& (forcarAlteracaoAmbiente 
    							|| (Uteis.isAtributoPreenchido(permissaoVO.getPermissao())  && !validarPermissaoModuloHabilitadoCliente(cliente, ((PerfilAcessoPermissaoEnumInterface)permissaoVO.getPermissao())))))){       			
    				iterator.remove();
    			}
    		}
    	
    	Ordenacao.ordenarLista(perfilAcessoVO.getPermissaoVOs(), "ordenacao");
    }
    
	public void realizarGeracaoListaFuncionalidadePorEntidade(PerfilAcessoVO perfilAcessoVO, TipoVisaoEnum tipoVisaoEnum, PermissaoVO permissaoVO) {
		List<Enum<? extends PerfilAcessoPermissaoEnumInterface>> perfilAcessoPermissaoEnums = PerfilAcessoModuloEnum.getFuncionalidadePorPermissaoEntidade(permissaoVO.getPermissao(), tipoVisaoEnum);
		aqui:
		for (Enum<? extends PerfilAcessoPermissaoEnumInterface> funcionalidadeEnum : perfilAcessoPermissaoEnums) {
			PerfilAcessoPermissaoEnumInterface funcionalidade = (PerfilAcessoPermissaoEnumInterface) funcionalidadeEnum;
			if (funcionalidade.getUtilizaVisao(tipoVisaoEnum)) {
				for (PermissaoVO func : perfilAcessoVO.getPermissaoVOs()) {
					if (func.getNomeEntidade().equals(funcionalidade.getValor())) {
						func.setPermissao(funcionalidadeEnum);
						func.setTipoPermissao(funcionalidade.getTipoPerfilAcesso().getValor());
						func.setTipoVisao(tipoVisaoEnum);
						permissaoVO.getFuncionalidades().add(func);
						permissaoVO.getDescricaoFuncinalidades().append("[").append(Uteis.removerAcentos(func.getTitulo().trim().toUpperCase())).append("]");
						perfilAcessoVO.getPermissaoVOs().remove(func);
						continue aqui;
					}
				}
				PermissaoVO funcionalidadeVO = realizarCriacaoPermissaoVOPorPerfilAcessoPermissaoEnum(perfilAcessoVO, tipoVisaoEnum, funcionalidadeEnum);
				permissaoVO.getFuncionalidades().add(funcionalidadeVO);
				permissaoVO.getDescricaoFuncinalidades().append("[").append(Uteis.removerAcentos(funcionalidadeVO.getTitulo().trim().toUpperCase())).append("]");
			}
		}
		Ordenacao.ordenarLista(permissaoVO.getFuncionalidades(), "ordenacao");
	}
    
    private PermissaoVO realizarCriacaoPermissaoVOPorPerfilAcessoPermissaoEnum(PerfilAcessoVO perfilAcessoVO, TipoVisaoEnum tipoVisaoEnum, Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao){
    	PermissaoVO permissaoVO = new PermissaoVO();
		permissaoVO.setCodPerfilAcesso(perfilAcessoVO.getCodigo());
		permissaoVO.setNomeEntidade(((PerfilAcessoPermissaoEnumInterface)permissao).getValor());
		permissaoVO.setTituloApresentacao(((PerfilAcessoPermissaoEnumInterface)permissao).getDescricaoVisao(tipoVisaoEnum));
		permissaoVO.setPermissao(permissao);
		permissaoVO.setTipoVisao(tipoVisaoEnum);
		permissaoVO.setTipoPermissao(((PerfilAcessoPermissaoEnumInterface)permissao).getTipoPerfilAcesso().getValor());
		if(!((PerfilAcessoPermissaoEnumInterface)permissao).getTipoPerfilAcesso().getIsFuncionalidade()){
			realizarGeracaoListaFuncionalidadePorEntidade(perfilAcessoVO, tipoVisaoEnum, permissaoVO);
		}
		return permissaoVO;
    }
    
    @Override
    public boolean validarPermissaoEstaNoSubModulo(Cliente cliente, PerfilAcessoPermissaoEnumInterface permissao, PerfilAcessoSubModuloEnum perfilAcessoSubModuloEnums){
    	    		
    		return (permissao.getPerfilAcessoSubModulo().equals(perfilAcessoSubModuloEnums) && validarPermissaoModuloHabilitadoCliente(cliente, perfilAcessoSubModuloEnums.getPerfilAcessoModuloEnum())) ;
    }
    
    @Override
    public boolean validarPermissaoEstaNoModulo(Cliente cliente, PerfilAcessoPermissaoEnumInterface permissao, PerfilAcessoModuloEnum perfilAcessoModuloEnum){
    	    		
    		return (perfilAcessoModuloEnum.equals(permissao.getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum()) && validarPermissaoModuloHabilitadoCliente(cliente, perfilAcessoModuloEnum));
    			
    }
    
    @Override
    public boolean validarPermissaoModuloHabilitadoCliente(Cliente cliente, PerfilAcessoPermissaoEnumInterface permissao){    	
    	return(validarPermissaoModuloHabilitadoCliente(cliente, permissao.getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum()));
    }
    
    
    @Override
    public boolean validarPermissaoModuloHabilitadoCliente(Cliente cliente, PerfilAcessoModuloEnum permissao){
    	if(permissao == null) {
    		return true;
    	}
		switch (permissao) {
		case ACADEMICO:
			return cliente.getPermitirAcessoModuloAcademico();
		case ADMINISTRATIVO:
			return cliente.getPermitirAcessoModuloAdministrativo();
		case AVALIACAO_INSTITUCIONAL:
			return cliente.getPermitirAcessoModuloAvaliacaoInstitucional();
//		case BANCO_DE_CURRICULOS:
//			return cliente.getPermitirAcessoModuloBancoDeCurriculo();
		case BIBLIOTECA:
			return cliente.getPermitirAcessoModuloBiblioteca();
		case COMPRAS:
			return cliente.getPermitirAcessoModuloCompras();
		case CONTABIL:
			return cliente.getPermitirAcessoModuloContabil();
		case CRM:
			return cliente.getPermitirAcessoModuloCrm();
		case EAD:
			return cliente.getPermitirAcessoModuloEad();
		case ESTAGIO:
			return cliente.getPermitirAcessoModuloEstagio();
		case FINANCEIRO:
			return cliente.getPermitirAcessoModuloFinanceiro();
		case HOME_CANDIDATO:
			return cliente.getPermitirAcessoModuloProcessoSeletivo();
		case NOTA_FISCAL:
			return cliente.getPermitirAcessoModuloNotaFiscal();
		case PATRIMONIO:
			return cliente.getPermitirAcessoModuloPatrimonio();
		case PLANO_ORCAMENTARIO:
			return cliente.getPermitirAcessoModuloPlanoOrcamentario();
		case PROCESSO_SELETIVO:
			return cliente.getPermitirAcessoModuloProcessoSeletivo();
		case RH:
			return cliente.getPermitirAcessoModuloRH();
		case SEI_DECIDIR:
			return cliente.getPermitirAcessoModuloSeiDecidir();
		case VISAO_ALUNO:
			return cliente.getPermitirAcessoModuloAcademico();
		case VISAO_COORDENADOR:
			return cliente.getPermitirAcessoModuloAcademico();
		case VISAO_PARCEIRO:
			return cliente.getPermitirAcessoModuloAcademico() || cliente.getPermitirAcessoModuloBancoDeCurriculo();
		case VISAO_PROFESSOR:
			return cliente.getPermitirAcessoModuloAcademico();
		default:
			return true;
		}
    	
    }
    
    public static List<PermissaoVO> montarDadosConsultaMaterial(SqlRowSet tabelaResultado) throws Exception {
        List<PermissaoVO> vetResultado = new ArrayList<PermissaoVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosBasicosMaterial(tabelaResultado));
        }
        return vetResultado;
    }
    
    public static PermissaoVO montarDadosBasicosMaterial(SqlRowSet dadosSQL) throws Exception {
        PermissaoVO obj = new PermissaoVO();        
        obj.setNomeEntidade(dadosSQL.getString("nomeEntidade"));
        return obj;
        
    }
    
    public List<PermissaoVO> validarPermissaoMaterial(Integer permissao) throws Exception {       
        String sqlStr = "SELECT nomeentidade  FROM Permissao WHERE codperfilacesso = "+permissao+" and (nomeentidade = 'PostarMaterialComProfessorObrigatoriamenteInformado' or nomeentidade = 'PostarMaterialComTurmaObrigatoriamenteInformado')";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsultaMaterial(tabelaResultado);
    }
    
}
