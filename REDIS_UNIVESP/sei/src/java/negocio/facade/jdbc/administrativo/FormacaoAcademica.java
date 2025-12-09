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

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.interfaces.administrativo.FormacaoAcademicaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FormacaoAcademicaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FormacaoAcademicaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FormacaoAcademicaVO
 * @see ControleAcesso
 * @see Pessoa
 */
@Repository
@Scope("singleton")
@Lazy 
public class FormacaoAcademica extends ControleAcesso implements FormacaoAcademicaInterfaceFacade{

    protected static String idEntidade;

    public FormacaoAcademica() throws Exception {
        super();
        setIdEntidade("FormacaoAcademica");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FormacaoAcademicaVO</code>.
     */
    public FormacaoAcademicaVO novo() throws Exception {
        FormacaoAcademica.incluir(getIdEntidade());
        FormacaoAcademicaVO obj = new FormacaoAcademicaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FormacaoAcademicaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FormacaoAcademicaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void incluir(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception {
        incluir(obj, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluir(final FormacaoAcademicaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        FormacaoAcademicaVO.validarDados(obj);
        final String sql = "INSERT INTO FormacaoAcademica( instituicao, escolaridade, curso, situacao, dataInicio, dataFim, pessoa, areaConhecimento, cidade, tipoInst, anoDataFim, prospects, modalidade, semestreDataFim, titulo ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlInserir = con.prepareStatement(sql);
                sqlInserir.setString(1, obj.getInstituicao());
                sqlInserir.setString(2, obj.getEscolaridade());
                sqlInserir.setString(3, obj.getCurso());
                sqlInserir.setString(4, obj.getSituacao());
                sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataInicio()));
                sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataFim()));
                if (Uteis.isAtributoPreenchido(obj.getPessoa())) {
                    sqlInserir.setInt(7, obj.getPessoa());
                } else {
                    sqlInserir.setNull(7, 0);
                }
                if(Uteis.isAtributoPreenchido(obj.getAreaConhecimento())) {
                sqlInserir.setInt(8, obj.getAreaConhecimento().getCodigo().intValue());
                }else {
                	sqlInserir.setNull(8, 0);
                }
                if (Uteis.isAtributoPreenchido(obj.getCidade().getCodigo())) {
                    sqlInserir.setInt(9, obj.getCidade().getCodigo());
                } else {
                    sqlInserir.setNull(9, 0);
                }
                sqlInserir.setString(10, obj.getTipoInst());
                if (obj.getAnoDataFim().equals("")) {
                    sqlInserir.setString(11, String.valueOf(Uteis.getAnoData(obj.getDataFim())));
                } else {
                    sqlInserir.setString(11, obj.getAnoDataFim());
                }
                if (Uteis.isAtributoPreenchido(obj.getProspectsVO().getCodigo())) {
                    sqlInserir.setInt(12, obj.getProspectsVO().getCodigo());
                } else {
                    sqlInserir.setNull(12, 0);
                }
                sqlInserir.setString(13, obj.getModalidade());
                if (obj.getSemestreDataFim().equals("")) {
                    sqlInserir.setString(14, String.valueOf(Uteis.getSemestreData(obj.getDataFim())));
                } else {
                	sqlInserir.setString(14, obj.getSemestreDataFim());
                }
                sqlInserir.setString(15, obj.getTitulo());
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
    }

    public void alterar(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception {
        alterar(obj, true, usuario);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FormacaoAcademicaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoAcademicaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterar(final FormacaoAcademicaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        FormacaoAcademicaVO.validarDados(obj);
        final String sql = "UPDATE FormacaoAcademica set instituicao=?, escolaridade=?, curso=?, situacao=?, dataInicio=?, dataFim=?, pessoa=?, areaConhecimento=?, cidade=?, tipoInst=?, anoDataFim=?, prospects=?, modalidade=?, semestreDataFim=?, titulo=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement sqlAlterar = con.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getInstituicao());
                sqlAlterar.setString(2, obj.getEscolaridade());
                sqlAlterar.setString(3, obj.getCurso());
                sqlAlterar.setString(4, obj.getSituacao());
                sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataInicio()));
                sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataFim()));
                if (Uteis.isAtributoPreenchido(obj.getPessoa())) {
                    sqlAlterar.setInt(7, obj.getPessoa());
                } else {
                    sqlAlterar.setNull(7, 0);
                }
                if(Uteis.isAtributoPreenchido(obj.getAreaConhecimento())) {
                sqlAlterar.setInt(8, obj.getAreaConhecimento().getCodigo().intValue());
                }else {
                	sqlAlterar.setNull(8, 0);
                }
                if (Uteis.isAtributoPreenchido(obj.getCidade().getCodigo())) {
                    sqlAlterar.setInt(9, obj.getCidade().getCodigo());
                } else {
                    sqlAlterar.setNull(9, 0);
                }
                sqlAlterar.setString(10, obj.getTipoInst());
                if (obj.getAnoDataFim().equals("")) {
                    sqlAlterar.setString(11, String.valueOf(Uteis.getAnoData(obj.getDataFim())));
                } else {
                    sqlAlterar.setString(11, obj.getAnoDataFim());
                }
                if (Uteis.isAtributoPreenchido(obj.getProspectsVO().getCodigo())) {
                    sqlAlterar.setInt(12, obj.getProspectsVO().getCodigo());
                } else {
                    sqlAlterar.setNull(12, 0);
                }
                sqlAlterar.setString(13, obj.getModalidade());
                if (obj.getSemestreDataFim().equals("")) {
                    sqlAlterar.setString(14, String.valueOf(Uteis.getSemestreData(obj.getDataFim())));
                } else {
                    sqlAlterar.setString(14, obj.getSemestreDataFim());
                }
                sqlAlterar.setString(15, obj.getTitulo());

                sqlAlterar.setInt(16, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FormacaoAcademicaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FormacaoAcademicaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluir(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception {
        FormacaoAcademica.excluir(getIdEntidade());
        String sql = "DELETE FROM FormacaoAcademica WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql,new Object[] {obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>FormacaoAcademica</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Pessoa</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>FormacaoAcademicaVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT FormacaoAcademica.* FROM FormacaoAcademica, Pessoa WHERE FormacaoAcademica.pessoa = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, usuario);
    }
    
    @Override
    public FormacaoAcademicaVO consultarPorPessoaEEscolaridade(Integer pessoa, NivelFormacaoAcademica nivelFormacaoAcademica, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sql =  new StringBuilder("SELECT * FROM FormacaoAcademica");
    	sql.append(" where pessoa = ").append(pessoa);
    	sql.append(" and escolaridade = '").append(nivelFormacaoAcademica.getValor()).append("' limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	if(tabelaResultado.next()){
    		return montarDados(tabelaResultado, usuario);
    	}
    	return new FormacaoAcademicaVO();
    }
    
    @Override
    public FormacaoAcademicaVO consultarPorCodigoPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sql =  new StringBuilder("SELECT * FROM FormacaoAcademica");
    	sql.append(" where pessoa = ").append(pessoa);
    	sql.append(" limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	if(tabelaResultado.next()){
    		return montarDados(tabelaResultado, usuario);
    	}
    	return new FormacaoAcademicaVO();
    }

    /**
     * Responsável por realizar uma consulta de <code>FormacaoAcademica</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FormacaoAcademicaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FormacaoAcademica WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorCodigoProspect(Integer codProspect, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FormacaoAcademica WHERE prospects = " + codProspect.intValue() + " ORDER BY prospects";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List consultarPorCodigoPessoaOrdemNovaAntiga(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FormacaoAcademica WHERE pessoa = " + pessoa.intValue() + " ORDER BY (dataFim, dataInicio ) desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FormacaoAcademicaVO</code> resultantes da consulta.
     */
    public static List<FormacaoAcademicaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<FormacaoAcademicaVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FormacaoAcademicaVO</code>.
     * @return  O objeto da classe <code>FormacaoAcademicaVO</code> com os dados devidamente montados.
     */
    public static FormacaoAcademicaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        FormacaoAcademicaVO obj = new FormacaoAcademicaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setInstituicao(dadosSQL.getString("instituicao"));
        obj.setEscolaridade(dadosSQL.getString("escolaridade"));
        obj.setCurso(dadosSQL.getString("curso"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTipoInst(dadosSQL.getString("tipoInst"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFim(dadosSQL.getDate("dataFim"));
        obj.setAnoDataFim(dadosSQL.getString("anoDataFim"));
        obj.setSemestreDataFim(dadosSQL.getString("semestreDataFim"));
        obj.getAreaConhecimento().setCodigo(dadosSQL.getInt("areaConhecimento"));
        obj.setPessoa(dadosSQL.getInt("pessoa"));
        obj.getProspectsVO().setCodigo(dadosSQL.getInt("prospects"));
        obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
        obj.setModalidade(dadosSQL.getString("modalidade"));
        obj.setTitulo(dadosSQL.getString("titulo"));
        obj.setNovoObj(false);
        montarDadosCidade(obj, usuario);
        montarDadosAreaConhecimento(obj, usuario);
        obj.setDescricaoAreaConhecimento(obj.getAreaConhecimento().getNome());
        return obj;
    }

    public static void montarDadosCidade(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
        obj.getCidade().setEstado(getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(obj.getCidade().getEstado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosAreaConhecimento(FormacaoAcademicaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getAreaConhecimento().getCodigo().intValue() == 0) {
            obj.setAreaConhecimento(new AreaConhecimentoVO());
            return;
        }
        obj.setAreaConhecimento(getFacadeFactory().getAreaConhecimentoFacade().consultarPorChavePrimaria(obj.getAreaConhecimento().getCodigo(), usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>FormacaoAcademicaVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FormacaoAcademica</code>.
     * @param <code>pessoa</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public void excluirFormacaoAcademicas(Integer pessoa, List objetos, UsuarioVO usuario) throws Exception {
        excluirFormacaoAcademicas(pessoa, objetos, true, usuario);
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluirFormacaoAcademicas(Integer pessoa, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM FormacaoAcademica WHERE (pessoa = " + pessoa + ")";
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO obj = (FormacaoAcademicaVO) i.next();
			sql += " and codigo != " + obj.getCodigo().intValue();
		}
		sql += " AND codigo not in (SELECT DISTINCT matricula.formacaoacademica FROM matricula WHERE matricula.aluno = " + pessoa + " AND matricula.formacaoacademica IS NOT NULL)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql);
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void excluirFormacaoAcademicasProspects(Integer prospects, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
    	String sql = "DELETE FROM FormacaoAcademica WHERE (prospects = " + prospects + ")";
    	Iterator i = objetos.iterator();
    	while (i.hasNext()) {
    		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) i.next();
    		sql += " and codigo != " + obj.getCodigo().intValue();
    	}
    	sql += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
    	getConexao().getJdbcTemplate().update(sql);
    }
    
    /**
     * Operação responsável por alterar todos os objetos da <code>FormacaoAcademicaVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFormacaoAcademicas</code> e <code>incluirFormacaoAcademicas</code> disponíveis na classe <code>FormacaoAcademica</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarFormacaoAcademicas(Integer pessoa, List objetos, UsuarioVO usuario) throws Exception {
        incluirFormacaoAcademicas(pessoa, objetos, true, usuario);
        excluirFormacaoAcademicas(pessoa, objetos, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarFormacaoAcademicas(Integer pessoa, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        incluirFormacaoAcademicas(pessoa, objetos, verificarAcesso, usuario);
        excluirFormacaoAcademicas(pessoa, objetos, verificarAcesso, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarFormacaoAcademicasProspects(Integer prospects, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
    	incluirFormacaoAcademicasProspects(prospects, objetos, usuario);
    	excluirFormacaoAcademicasProspects(prospects, objetos, verificarAcesso, usuario);
    }
    
    /**
     * Operação responsável por incluir objetos da <code>FormacaoAcademicaVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>basico.Pessoa</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirFormacaoAcademicas(Integer pessoaPrm, List objetos, UsuarioVO usuario) throws Exception {
        incluirFormacaoAcademicas(pessoaPrm, objetos, true, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirFormacaoAcademicas(Integer pessoaPrm, List objetos, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
       	Iterator e = objetos.iterator();
    		while (e.hasNext()) {
    			FormacaoAcademicaVO obj = (FormacaoAcademicaVO) e.next();
    			obj.setPessoa(pessoaPrm);
    			if (obj.getCodigo().intValue() == 0) {
    				if (!obj.getAnoDataFim().equals(Integer.toString(Uteis.getAnoData(obj.getDataFim()))) || !obj.getSemestreDataFim().equals(Integer.toString(Uteis.getMesData(obj.getDataFim())))) {
    					String novaDataFim = Uteis.getData(obj.getDataFim(), "dd/MM/yyyy");
    					obj.setDataFim(UteisData.getData(novaDataFim));
    					novaDataFim = Uteis.getData(obj.getDataFim(), "dd/MM/yyyy").replace("/"+(Uteis.getMesData(obj.getDataFim()))+"/", "/"+(obj.getSemestreDataFim().equals("1") ? "01" : "07") + "/");
    					obj.setDataFim(UteisData.getData(novaDataFim));
    				}
    				incluir(obj, verificarAcesso, usuario);
    			} else {
    				if (!obj.getAnoDataFim().equals(Integer.toString(Uteis.getAnoData(obj.getDataFim()))) || !obj.getSemestreDataFim().equals(Integer.toString(Uteis.getMesData(obj.getDataFim())))) {
//    					String novaDataFim = Uteis.getData(obj.getDataFim(), "dd/MM/yyyy").replace((Uteis.getAno(obj.getDataFim())), obj.getAnoDataFim());
    					String novaDataFim = Uteis.getData(obj.getDataFim(), "dd/MM/yyyy");
    					obj.setDataFim(UteisData.getData(novaDataFim));
    					novaDataFim = Uteis.getData(obj.getDataFim(), "dd/MM/yyyy").replace("/"+(Uteis.getMesData(obj.getDataFim()))+"/", "/"+(obj.getSemestreDataFim().equals("1") ? "01" : "07") + "/");
    					obj.setDataFim(UteisData.getData(novaDataFim));
    				}
    				alterar(obj, verificarAcesso, usuario);
    			}
    		}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirFormacaoAcademicasProspects(Integer prospects, List objetos, UsuarioVO usuario) throws Exception {
    	Iterator e = objetos.iterator();
    	while (e.hasNext()) {
    		FormacaoAcademicaVO obj = (FormacaoAcademicaVO) e.next();
    		obj.getProspectsVO().setCodigo(prospects);
    		if (obj.getCodigo().intValue() == 0) {
    			incluir(obj, false, usuario);
    		} else {
    			alterar(obj, false, usuario);
    		}
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void incluirFormacaoAcademicaMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		FormacaoAcademicaVO obj = new FormacaoAcademicaVO();
		obj.setCurso(matriculaVO.getCurso().getNome());
		if(!matriculaVO.getUnidadeEnsino().getNomeExpedicaoDiploma().equals("")) {
			obj.setInstituicao(matriculaVO.getUnidadeEnsino().getNomeExpedicaoDiploma());
		}
		else {
			obj.setInstituicao(matriculaVO.getUnidadeEnsino().getNome());
		}
		obj.setCidade(matriculaVO.getUnidadeEnsino().getCidade());
		obj.setEscolaridade(NivelFormacaoAcademica.getEnumPorValorTipoNivelEducacional(matriculaVO.getCurso().getNivelEducacional()).getValor());
		obj.setAreaConhecimento(matriculaVO.getCurso().getAreaConhecimento());
		obj.setTitulo(matriculaVO.getCurso().getTitulo());
		obj.setSituacao("CO");
		obj.setDataInicio(matriculaVO.getDataInicioCurso());
		obj.setDataFim(matriculaVO.getDataConclusaoCurso());
		obj.setAnoDataFim(Uteis.getAno(matriculaVO.getDataConclusaoCurso()));
		obj.setSemestreDataFim(Uteis.getSemestreData(matriculaVO.getDataConclusaoCurso()));
		obj.setPessoa(matriculaVO.getAluno().getCodigo());
		if (!consultaExisteFormacaoAcademicaPorPessoaEscolaridadeECurso(obj.getPessoa(), obj.getEscolaridade(), obj.getCurso(), obj.getInstituicao())) {
			incluir(obj, usuario);
		}
    }
    
	public Boolean consultaExisteFormacaoAcademicaPorPessoaEscolaridadeECurso(Integer pessoa, String escolaridade, String curso, String instituicao) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT codigo FROM formacaoacademica WHERE pessoa = ?");
		sqlStr.append(" AND escolaridade = ? ");
		sqlStr.append(" AND upper(trim(curso)) ilike ? ");
		sqlStr.append(" AND upper(trim(instituicao)) ilike ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {pessoa, escolaridade, curso, instituicao});
		if (rs.next()) {
			return true;
		}
		return false;
	}
    
    /**
     * Operação responsável por consultar todos os <code>FormacaoAcademicaVO</code> relacionados a um objeto da classe <code>basico.Pessoa</code>.
     * @param pessoa  Atributo de <code>basico.Pessoa</code> a ser utilizado para localizar os objetos da classe <code>FormacaoAcademicaVO</code>.
     * @return List  Contendo todos os objetos da classe <code>FormacaoAcademicaVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List consultarFormacaoAcademicas(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        return consultarFormacaoAcademicas(pessoa, controlarAcesso, false, usuario);
    }
    
    public static List consultarFormacaoAcademicas(Integer pessoa, boolean controlarAcesso, boolean funcionario, UsuarioVO usuario) throws Exception {
        FormacaoAcademica.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM FormacaoAcademica WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {pessoa.intValue()});
        while (resultado.next()) {
            FormacaoAcademicaVO novoObj = new FormacaoAcademicaVO();
            novoObj = FormacaoAcademica.montarDados(resultado, usuario);
            novoObj.setFuncionario(funcionario);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FormacaoAcademica.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        FormacaoAcademica.idEntidade = idEntidade;
    }
    
    /**
     * Operação responsável por localizar um objeto da classe <code>FormacaoAcademicaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FormacaoAcademicaVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FormacaoAcademica WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }
    
    @Override
    public FormacaoAcademicaVO consultarFormacaoAcademicaoMaisAtual(Integer pessoa, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        List<FormacaoAcademicaVO> formacaoAcademicaVOs = consultarFormacaoAcademicaoMaisAtual(pessoa, usuario, 1);
        if(formacaoAcademicaVOs != null && formacaoAcademicaVOs.size() > 0) {
        	return formacaoAcademicaVOs.get(0);
        }
        return new FormacaoAcademicaVO();
    }
    
    @Override
    public List<FormacaoAcademicaVO> consultarFormacaoAcademicaoMaisAtual(Integer pessoa, UsuarioVO usuario, Integer limit) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sql = new StringBuilder("SELECT * FROM FormacaoAcademica WHERE pessoa = ? ");
        sql.append(" order by case escolaridade ");
        for (NivelFormacaoAcademica nivelFormacaoAcademica : NivelFormacaoAcademica.values()) {
			sql.append("WHEN '").append(nivelFormacaoAcademica.getValor()).append("' THEN ").append(nivelFormacaoAcademica.getNivel()).append(" ");
		}
        sql.append(" else 0 end desc ");
        if(limit != null && limit > 0) {
        	sql.append(" limit  ").append(limit);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), pessoa.intValue());
        List<FormacaoAcademicaVO>  formacaoAcademicaVOs = new ArrayList<FormacaoAcademicaVO>();
        while (tabelaResultado.next()) {
        FormacaoAcademicaVO obj = new FormacaoAcademicaVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.setInstituicao(tabelaResultado.getString("instituicao"));
        obj.setEscolaridade(tabelaResultado.getString("escolaridade"));
        obj.setCurso(tabelaResultado.getString("curso"));
        obj.setSituacao(tabelaResultado.getString("situacao"));
        obj.setTipoInst(tabelaResultado.getString("tipoInst"));
        obj.setDataInicio(tabelaResultado.getDate("dataInicio"));
        obj.setDataFim(tabelaResultado.getDate("dataFim"));
        obj.setAnoDataFim(tabelaResultado.getString("anoDataFim"));
        obj.setSemestreDataFim(tabelaResultado.getString("semestreDataFim"));
        obj.getAreaConhecimento().setCodigo(tabelaResultado.getInt("areaConhecimento"));
        obj.setPessoa(tabelaResultado.getInt("pessoa"));
        obj.getProspectsVO().setCodigo(tabelaResultado.getInt("prospects"));
        obj.getCidade().setCodigo(tabelaResultado.getInt("cidade"));
        obj.setNovoObj(false);
        formacaoAcademicaVOs.add(obj);
        }
        return formacaoAcademicaVOs;
    }
    
    
	@Override
	public Boolean validarRemoverFormacaoAcademica(Integer codigoPessoa, FormacaoAcademicaVO formacao) throws Exception {
		try {
			Integer quantidade = 0;
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT COUNT(matricula.formacaoacademica) AS  quantidade ");
			sql.append("FROM matricula  ");
			sql.append(" INNER JOIN formacaoacademica ON matricula.formacaoacademica = formacaoacademica.codigo ");
			sql.append("WHERE  matricula.aluno = ").append(codigoPessoa);
			sql.append(" AND matricula.aluno = formacaoacademica.pessoa  ");
			sql.append(" AND formacaoacademica.codigo  = ").append(formacao.getCodigo());
			sql.append(" AND matricula.formacaoacademica IS NOT NULL ").append(";");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

			if (tabelaResultado.next()) {
				quantidade = tabelaResultado.getInt("quantidade");
			}
			if (quantidade > 0) {
				return Boolean.FALSE;
			}

		} catch (Exception e) {
			throw e;
		}

		return Boolean.TRUE;
	}
	
	@Override
	public FormacaoAcademicaVO consultarPorNomeCursoPessoa(String nomeCurso, Integer codigoPessoa, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM formacaoacademica  WHERE formacaoacademica.curso = '").append(nomeCurso).append("' AND formacaoacademica.pessoa = ").append(codigoPessoa).append(" and codigo not in ( select formacaoacademica  from matricula where formacaoacademica  is not null ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, usuarioVO);
		}
		return new FormacaoAcademicaVO();
	}
	
	@Override
    public FormacaoAcademicaVO consultarPorPessoaEEscolaridadeOrdenandoUltimaDataConclusao(Integer pessoa, NivelFormacaoAcademica nivelFormacaoAcademica, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sql =  new StringBuilder("SELECT * FROM FormacaoAcademica");
    	sql.append(" where pessoa = ").append(pessoa);
    	sql.append(" and escolaridade = '").append(nivelFormacaoAcademica.getValor()).append("' order by dataFim desc limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	if(tabelaResultado.next()){
    		return montarDados(tabelaResultado, usuario);
    	}
    	return new FormacaoAcademicaVO();
    }
	
	 @SuppressWarnings("unchecked")
	public void validarVinculoFormacaoAcademica(Integer areaConhecimento, UsuarioVO usuario) throws Exception {
		 List objetos = new ArrayList();
	        String sql = "SELECT * FROM FormacaoAcademica WHERE areaConhecimento = ?";
	        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] {areaConhecimento.intValue()});
	        while (resultado.next()) {
	            FormacaoAcademicaVO novoObj = new FormacaoAcademicaVO();
	            novoObj.setCodigo(new Integer(resultado.getInt("codigo")));
	            objetos.add(novoObj);
	        }
	        if (Uteis.isAtributoPreenchido(objetos)) {
				throw new Exception("Existem Funcionário e Alunos vinculados a essa área de conhecimento. Para realizar a exclusão, primeiro remova os vínculos.");
			}
	    }
}
