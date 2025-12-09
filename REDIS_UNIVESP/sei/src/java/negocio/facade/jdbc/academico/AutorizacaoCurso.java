package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.AutorizacaoCursoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>CursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see AutorizacaoCursoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class AutorizacaoCurso extends ControleAcesso implements AutorizacaoCursoInterfaceFacade {

    protected static String idEntidade;

    public AutorizacaoCurso() throws Exception {
        super();
        setIdEntidade("AutorizacaoCurso");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>AutorizacaoCursoVO</code>. 
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. 
     * Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>AutorizacaoCursoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *            Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AutorizacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            /**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
            //AutorizacaoCurso.incluir(getIdEntidade());
            final StringBuilder sql = new StringBuilder("");
            sql.append(" INSERT INTO AutorizacaoCurso ( nome, curso, data, tipoAutorizacaoCursoEnum, usuario, numero, dataCredenciamento, veiculoPublicacao, secaoPublicacao, paginaPublicacao, numeroDOU, emTramitacao, tipoProcesso, datacadastro, dataProtocolo ) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(cont++, obj.getNome());
                    sqlInserir.setInt(cont++, obj.getCurso());
                    sqlInserir.setDate(cont++, Uteis.getDataJDBC(obj.getData()));
                    if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoCursoEnum())) {
                        sqlInserir.setString(cont++, obj.getTipoAutorizacaoCursoEnum().name());
                    }else {
                        sqlInserir.setNull(cont++, 0);
                    }
                    sqlInserir.setInt(cont++, obj.getCodigoUsuario());
                    sqlInserir.setString(cont++, obj.getNumero());
                    if (obj.getDataCredenciamento() != null) {
                    	sqlInserir.setDate(cont++, Uteis.getDataJDBC(obj.getDataCredenciamento()));
                    } else {
                    	sqlInserir.setNull(cont++, 0);
                    }
                    sqlInserir.setString(cont++, obj.getVeiculoPublicacao());
                    sqlInserir.setInt(cont++, obj.getSecaoPublicacao());
                    sqlInserir.setInt(cont++, obj.getPaginaPublicacao());
                    sqlInserir.setInt(cont++, obj.getNumeroDOU());
                    sqlInserir.setBoolean(cont++, obj.getEmTramitacao());
                    sqlInserir.setString(cont++, obj.getTipoProcesso());
                    if (obj.getDataCadastro() != null) {
                    	sqlInserir.setDate(cont++, Uteis.getDataJDBC(obj.getDataCadastro()));
                    } else {
                    	sqlInserir.setNull(cont++, 0);
                    }
                    if (obj.getDataProtocolo() != null) {
                    	sqlInserir.setDate(cont++, Uteis.getDataJDBC(obj.getDataProtocolo()));
                    } else {
                    	sqlInserir.setNull(cont++, 0);
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
            obj.setNovoObj(false);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>AutorizacaoCursoVO</code>. 
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>AutorizacaoCursoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AutorizacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            /**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
            //AutorizacaoCurso.alterar(getIdEntidade());
            final StringBuilder sql = new StringBuilder("");
            sql.append(" UPDATE AutorizacaoCurso SET nome=?, curso=?, data=?, tipoAutorizacaoCursoEnum=?, usuario=?, numero=?, dataCredenciamento=?, veiculoPublicacao=?, secaoPublicacao=?, paginaPublicacao=?, numeroDOU=?, emTramitacao=?, tipoProcesso=?, datacadastro=?, dataProtocolo=? ");
            sql.append(" WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    Integer cont = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setString(cont++, obj.getNome());
                    sqlAlterar.setInt(cont++, obj.getCurso());
                    sqlAlterar.setDate(cont++, Uteis.getDataJDBC(obj.getData()));
                    if (Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoCursoEnum())) {
                        sqlAlterar.setString(cont++, obj.getTipoAutorizacaoCursoEnum().name());
                    }else {
                        sqlAlterar.setNull(cont++, 0);
                    }
                    sqlAlterar.setInt(cont++, obj.getCodigoUsuario());
                    sqlAlterar.setString(cont++, obj.getNumero());
                    if (obj.getDataCredenciamento() != null) {
                    	sqlAlterar.setDate(cont++, Uteis.getDataJDBC(obj.getDataCredenciamento()));
                    } else {
                    	sqlAlterar.setNull(cont++, 0);
                    }
                    sqlAlterar.setString(cont++, obj.getVeiculoPublicacao());
                	sqlAlterar.setInt(cont++, obj.getSecaoPublicacao());
                	sqlAlterar.setInt(cont++, obj.getPaginaPublicacao());
                	sqlAlterar.setInt(cont++, obj.getNumeroDOU());
                    sqlAlterar.setBoolean(cont++, obj.getEmTramitacao());
                    sqlAlterar.setString(cont++, obj.getTipoProcesso());
                    if (obj.getDataCadastro() != null) {
                    	sqlAlterar.setDate(cont++, Uteis.getDataJDBC(obj.getDataCadastro()));
                    } else {
                    	sqlAlterar.setNull(cont++, 0);
                    }
                    if (obj.getDataProtocolo() != null) {
                    	sqlAlterar.setDate(cont++, Uteis.getDataJDBC(obj.getDataProtocolo()));
                    } else {
                    	sqlAlterar.setNull(cont++, 0);
                    }
                    sqlAlterar.setInt(cont++, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>AutorizacaoCursoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. 
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. 
     * Isto, através da operação <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>AutorizacaoCursoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AutorizacaoCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
            //AutorizacaoCurso.excluir(getIdEntidade());
			if(obj.getAutorizacaoUsadaPorMatricula()) {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirExcluirAutorizacaoCurso", usuario);
				getFacadeFactory().getMatriculaFacade().removerVinculoAutorizacaoCursoMatricula(obj.getCodigo(), usuario, true);
			}
            String sql = "DELETE FROM AutorizacaoCurso WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CursoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public AutorizacaoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
        String sql = "SELECT * FROM AutorizacaoCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>AutorizacaoCursoVO</code> através do valor do atributo <code>codigo</code> da classe <code>CursoVO</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>AutorizacaoCursoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM AutorizacaoCurso WHERE curso = ? ORDER BY data DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoCurso});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    public AutorizacaoCursoVO consultarVigentePorDataMatricula(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * ");
        sql.append(" FROM autorizacaocurso  ");
        sql.append(" WHERE curso = ").append(codigoCurso);
        sql.append(" AND data <= '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999' ");
        sql.append(" ORDER BY data DESC ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

        List<AutorizacaoCursoVO> listaVO = montarDadosConsulta(tabelaResultado, nivelMontarDados);
        for (AutorizacaoCursoVO vo : listaVO) {
            if (vo.getNome() != null && !vo.getNome().equals("")) {
                return vo;
            }
        }
        //Apos percorrer a lista de objetos, se nao for encontrado uma data de autorizaçao retorna ConsistirException
        throw new ConsistirException("Dados (AutorizacaoCurso) Não Encontrados.");
    }

    public AutorizacaoCursoVO consultarPorCursoDataVigenteMatricula(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception {
    	if (dataMatricula != null) {
	        StringBuilder sql = new StringBuilder("");
	        sql.append(" SELECT *  FROM autorizacaocurso ");
	        sql.append(" WHERE curso = ").append(codigoCurso);
	        sql.append(" AND ((data < '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999') OR (emTramitacao AND dataCadastro < '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999' )) ");
	        sql.append(" ORDER BY CASE WHEN emtramitacao THEN datacadastro ELSE DATA END DESC LIMIT 1");
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	        List<AutorizacaoCursoVO> listaVO = montarDadosConsulta(tabelaResultado, nivelMontarDados);
	        for (AutorizacaoCursoVO vo : listaVO) {
	        	if ((vo.getNome() != null && !vo.getNome().equals("")) || (vo.getEmTramitacao() && Uteis.isAtributoPreenchido(vo.getTipoProcesso()) && Uteis.isAtributoPreenchido(vo.getNumero()))) {
	                return vo;
	            }
	        }
    	}
        return new AutorizacaoCursoVO();
    }

    public AutorizacaoCursoVO consultarPorCursoDataMaisRecente(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM autorizacaocurso  ");
        sql.append(" WHERE curso = ").append(codigoCurso);
//        parte de sql comentada pois o metodo realiza a busca do ultimo reconhecimento cadastrada do curso independente da matricula, metodo com a regra antiga (consultarPorCursoDataVigenteMatricula)
//        sql.append(" AND ((data < '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999') OR (emTramitacao AND dataCadastro < '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999' )) ");
        sql.append(" ORDER BY CASE WHEN emtramitacao THEN datacadastro ELSE DATA END DESC LIMIT 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<AutorizacaoCursoVO> listaVO = montarDadosConsulta(tabelaResultado, nivelMontarDados);
        for (AutorizacaoCursoVO vo : listaVO) {
            if ((vo.getNome() != null && !vo.getNome().equals("")) || (vo.getEmTramitacao() && Uteis.isAtributoPreenchido(vo.getTipoProcesso()) && Uteis.isAtributoPreenchido(vo.getNumero()))) {
                return vo;
            }
        }
        return new AutorizacaoCursoVO();
    }
    
    @Override
    public AutorizacaoCursoVO consultarPorCursoDataMaisAntigo(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM autorizacaocurso  ");
        sql.append(" WHERE curso = ").append(codigoCurso);
//		    parte de sql comentada pois o metodo realiza a busca do primeiro reconhecimento cadastrada do curso independente da matricula, metodo com a regra antiga (consultarPorCursoDataVigenteMatricula) 
//        	sql.append(" AND ((data < '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999') OR (emTramitacao AND dataCadastro < '").append(Uteis.getDataJDBC(dataMatricula)).append(" 23:59:59.999' )) ");
        sql.append(" ORDER BY CASE WHEN emtramitacao THEN datacadastro ELSE DATA END ASC LIMIT 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<AutorizacaoCursoVO> listaVO = montarDadosConsulta(tabelaResultado, nivelMontarDados);
        for (AutorizacaoCursoVO vo : listaVO) {
        	if ((vo.getNome() != null && !vo.getNome().equals("")) || (vo.getEmTramitacao() && Uteis.isAtributoPreenchido(vo.getTipoProcesso()) && Uteis.isAtributoPreenchido(vo.getNumero()))) {
                return vo;
            }
        }
        return new AutorizacaoCursoVO();
    }
    
    public AutorizacaoCursoVO consultarPorCursoPos(Integer codigoCurso, int nivelMontarDados) throws Exception {
        StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * ");
        sql.append(" FROM autorizacaocurso  ");
        sql.append(" WHERE curso = ").append(codigoCurso);
        sql.append(" ORDER BY data DESC limit 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

        List<AutorizacaoCursoVO> listaVO = montarDadosConsulta(tabelaResultado, nivelMontarDados);
        for (AutorizacaoCursoVO vo : listaVO) {
            if (vo.getNome() != null && !vo.getNome().equals("")) {
                return vo;
            }
        }
        return new AutorizacaoCursoVO();
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados ( <code>ResultSet</code>). 
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>AutorizacaoCursoVO</code> resultantes da consulta.
     */
    public static List<AutorizacaoCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<AutorizacaoCursoVO> vetResultado = new ArrayList<AutorizacaoCursoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados 
     * (<code>ResultSet</code>) em um objeto da classe <code>AutorizacaoCursoVO</code>.
     *
     * @return O objeto da classe <code>AutorizacaoCursoVO</code> com os dados devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static AutorizacaoCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AutorizacaoCursoVO obj = new AutorizacaoCursoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setCurso(dadosSQL.getInt("curso"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setData(dadosSQL.getDate("data"));
        obj.setCodigoUsuario(dadosSQL.getInt("usuario"));
        obj.setEmTramitacao(dadosSQL.getBoolean("emTramitacao"));
        obj.setTipoProcesso(dadosSQL.getString("tipoProcesso"));
        obj.setDataCredenciamento(dadosSQL.getDate("dataCredenciamento"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setVeiculoPublicacao(dadosSQL.getString("veiculoPublicacao"));
        if (Uteis.isAtributoPreenchido(dadosSQL.getInt("secaoPublicacao"))) {
        	obj.setSecaoPublicacao(dadosSQL.getInt("secaoPublicacao"));
        }
        if (Uteis.isAtributoPreenchido(dadosSQL.getInt("paginaPublicacao"))) {
        	obj.setPaginaPublicacao(dadosSQL.getInt("paginaPublicacao"));
        }
        if (Uteis.isAtributoPreenchido(dadosSQL.getInt("numeroDOU"))) {
        	obj.setNumeroDOU(dadosSQL.getInt("numeroDOU"));
        }
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoAutorizacaoCursoEnum"))) {
        	obj.setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum.valueOf(dadosSQL.getString("tipoAutorizacaoCursoEnum")));
        }
        if (Uteis.isAtributoPreenchido(dadosSQL.getDate("dataCadastro"))) {
        	obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        }
        if (Uteis.isAtributoPreenchido(dadosSQL.getDate("dataProtocolo"))) {
        	obj.setDataProtocolo(dadosSQL.getDate("dataProtocolo"));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
        	return obj;
        }
        obj.setAutorizacaoUsadaPorMatricula(getFacadeFactory().getMatriculaFacade().consultarPorAutorizacaoCurso(obj.getCodigo()));
        obj.setNovoObj(false);
        return obj;
    }

    public void realizarAtualizacaoAutorizacaoCursoVOs(List autorizacaoCursoVOs, Integer curso, UsuarioVO usuario) throws Exception {
        for (AutorizacaoCursoVO vo : (ArrayList<AutorizacaoCursoVO>) autorizacaoCursoVOs) {
            vo.setCodigoUsuario(usuario.getCodigo());
            if (vo.isNovoObj()) {
                vo.setCurso(curso);
                incluir(vo, usuario);
            } else {
                alterar(vo, usuario);
            }
        }
    }

    public void validarDadosAutorizacaoCurso(List autorizacaoCursoVOSVerificar, List autorizacaoCursoVOsComparar) throws Exception {
        for (AutorizacaoCursoVO voVerificar : (ArrayList<AutorizacaoCursoVO>) autorizacaoCursoVOSVerificar) {
            for (AutorizacaoCursoVO voComparar : (ArrayList<AutorizacaoCursoVO>) autorizacaoCursoVOsComparar) {
                if(!voVerificar.equals(voComparar)){
                    if (voVerificar.getEmTramitacao()) {
                    	if (voVerificar.getTipoProcesso().equals(voComparar.getTipoProcesso()) && voVerificar.getNumero().equals(voComparar.getNumero())) {
                    		throw new Exception("Não é possível adicionar mais de um Reconhecimento em tramitação com mesmo tipo e número processo.");
                    	}
                    } else {
                    	if((voVerificar.getNome().equals(voComparar.getNome())) && ((voVerificar.getData() == null && voComparar.getData() == null) || ((voVerificar.getData() != null && voComparar.getData() != null) && voVerificar.getData().equals(voComparar.getData())))){
                    		throw new Exception("Não é possível adicionar mais de um Reconhecimento com mesmo nome e data.");
                    	}
                    }
                }
            }
        }
    }

    public void validarDados(AutorizacaoCursoVO obj) throws Exception {
        if (!obj.getEmTramitacao() && obj.getNome().equals("")) {
            throw new ConsistirException("Campo Autorização (Autorizações/Reconhecimentos) deve ser informado.");
        }
        if (!obj.getEmTramitacao() && !Uteis.isAtributoPreenchido(obj.getTipoAutorizacaoCursoEnum())) {
            throw new ConsistirException("Campo Tipo Autorização (Autorizações/Reconhecimentos) deve ser informado.");
        }
        if (!Uteis.isAtributoPreenchido(obj.getNumero())) {
            throw new ConsistirException("Campo Número (Autorizações/Reconhecimentos) deve ser informado.");
        }
//        if (obj.getData() == null) {
//            throw new ConsistirException("Campo Data (AutorizacaoCurso) deve ser informado.");
//        }
        if (obj.getCurso() <= 0) {
            throw new ConsistirException("Codigo Curso (Autorizações/Reconhecimentos) deve ser informado.");
        }
        validarDadosDiplomaAutorizacaoReconhecimento(obj);
    }
    
    private void validarDadosDiplomaAutorizacaoReconhecimento(AutorizacaoCursoVO obj) {
    	if (obj.getEmTramitacao()) {
    		obj.setTipoAutorizacaoCursoEnum(null);
    		obj.setNome(null);
    		obj.setDataCredenciamento(null);
    		obj.setSecaoPublicacao(null);
    		obj.setPaginaPublicacao(null);
    		obj.setNumeroDOU(null);
    	} else {
    		obj.setTipoProcesso(null);
    		obj.setDataCadastro(null);
    		obj.setDataProtocolo(null);
    	}
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar 
     * as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AutorizacaoCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
     * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AutorizacaoCurso.idEntidade = idEntidade;
    }

	@Override
	public List<AutorizacaoCursoVO> consultarDataDescPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM AutorizacaoCurso WHERE curso = ? ORDER BY data DESC");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoCurso});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}

	@Override
	public List<AutorizacaoCursoVO> consultarDataPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
        sql.append(" SELECT * FROM AutorizacaoCurso WHERE curso = ? ORDER BY data");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoCurso});
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
	}
}
