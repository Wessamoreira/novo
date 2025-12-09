package negocio.facade.jdbc.basico;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.google.common.net.HttpHeaders;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controle.arquitetura.DataModelo;
import jobs.enumeradores.JobsEnum;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.arquitetura.ExcluirJsonStrategy;
import negocio.comuns.arquitetura.NovidadeSeiVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoNovidadeEnum;
import negocio.comuns.basico.ArtefatoAjudaVO;
import negocio.comuns.basico.enumeradores.TipoArtefatoAjudaEnum;
import negocio.comuns.basico.enumeradores.TipoConsultaComboArtefatoAjudaEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.ArtefatoAjudaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ArtefatoAjudaVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ArtefatoAjudaVO</code>. Encapsula toda a interação com o banco de dados.
 * @author Paulo Taucci
 * @see ArtefatoAjudaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ArtefatoAjuda extends ControleAcesso implements ArtefatoAjudaInterfaceFacade {

    protected static String idEntidade;

    public ArtefatoAjuda() throws Exception {
        super();
        setIdEntidade("ArtefatoAjuda");
    }

    public ArtefatoAjudaVO novo() throws Exception {
        ArtefatoAjuda.incluir(getIdEntidade());
        ArtefatoAjudaVO obj = new ArtefatoAjudaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>ArtefatoAjudaVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>). Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @author Paulo Taucci
     * @param obj
     *            Objeto da classe <code>ArtefatoAjudaVO</code> que será gravado no banco
     *            de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @SuppressWarnings("unchecked")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ArtefatoAjudaVO obj, UsuarioVO usuario) throws Exception {
        try {        	
            final String sql = "INSERT INTO ArtefatoAjuda(titulo, descricao, tipoArtefato, palavrasChave, link, artefatoDesatualizado, responsavelAssinalarDesatualizado, dataAssinaladoDesatualizado, modulo, submodulo, tela, created, codigo, versao, recurso) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getTitulo());
                    sqlInserir.setString(2, obj.getDescricao());
                    sqlInserir.setString(3, obj.getTipoArtefato().toString());
                    sqlInserir.setString(4, obj.getPalavrasChave());
                    sqlInserir.setString(5, obj.getLink());
                    sqlInserir.setBoolean(6, obj.getArtefatoDesatualizado());
                    if (obj.getArtefatoDesatualizado()) {
                        sqlInserir.setNull(7, 0);
                        sqlInserir.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getDataAssinaladoDesatualizado()));
                    } else {
                        sqlInserir.setInt(7, 0);
                        sqlInserir.setTimestamp(8, null);
                    }
                    sqlInserir.setString(9, obj.getModulo());
                    sqlInserir.setString(10, obj.getSubmodulo());
                    sqlInserir.setString(11, obj.getTela());
                    sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlInserir.setInt(13, obj.getCodigo());
                    sqlInserir.setString(14, obj.getVersao());
                    sqlInserir.setString(15, obj.getRecurso());
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
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>ArtefatoAjudaVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>). Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     * @author Paulo Taucci
     * @param obj
     *            Objeto da classe <code>ArtefatoAjudaVO</code> que será alterada no
     *            banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ArtefatoAjudaVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE ArtefatoAjuda set titulo = ?, descricao = ?, tipoArtefato = ?, palavrasChave = ?, link = ?, artefatoDesatualizado = ?, responsavelAssinalarDesatualizado=?, dataAssinaladoDesatualizado=?, modulo=?, submodulo=?, tela = ?, updated = ?, versao = ?, recurso = ? WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getTitulo());
                    sqlAlterar.setString(2, obj.getDescricao());
                    sqlAlterar.setString(3, obj.getTipoArtefato().toString());
                    sqlAlterar.setString(4, obj.getPalavrasChave());
                    sqlAlterar.setString(5, obj.getLink());
                    sqlAlterar.setBoolean(6, obj.getArtefatoDesatualizado());
                    if (obj.getArtefatoDesatualizado()) {
                        sqlAlterar.setNull(7, 0);
                        sqlAlterar.setTimestamp(8, Uteis.getDataJDBCTimestamp(obj.getDataAssinaladoDesatualizado()));
                    } else {
                        sqlAlterar.setInt(7, 0);
                        sqlAlterar.setTimestamp(8, null);
                    }
                    sqlAlterar.setString(9, obj.getModulo());
                    sqlAlterar.setString(10, obj.getSubmodulo());
                    sqlAlterar.setString(11, obj.getTela());
                    sqlAlterar.setTimestamp(12, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlAlterar.setString(13, obj.getVersao());
                    sqlAlterar.setString(14, obj.getRecurso());
                    sqlAlterar.setInt(15, obj.getCodigo().intValue());
                    
                    return sqlAlterar;
                }
            }) == 0) {
            	incluir(obj, null);
            }

        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(ArtefatoAjudaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.isNovoObj().booleanValue()) {
            this.incluir(obj, usuario);
        } else {
            this.alterar(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ArtefatoAjudaVO obj) throws Exception {
        try {
            ArtefatoAjuda.excluir(getIdEntidade());
            String sql = "DELETE FROM ArtefatoAjuda WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
    
	public void validarDados(ArtefatoAjudaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
            return;
        }
		if (obj.getTitulo().equals("")) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ArtefatoAjuda_titulo"));
		}
		if (obj.getTipoArtefato() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ArtefatoAjuda_tipoArtefato"));
		}
		if (obj.getPalavrasChave().equals("")) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ArtefatoAjuda_palavrasChave"));
		}
		if (!obj.getTipoArtefato().equals(TipoArtefatoAjudaEnum.HTML) && obj.getLink().equals("")) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ArtefatoAjuda_link"));
		}
		if (obj.getTipoArtefato().equals(TipoArtefatoAjudaEnum.HTML) && obj.getTextoInformativo().trim().equals("")) {
			throw new ConsistirException("O campo TEXTO AJUDA deve ser informado.");
		}
		if(obj.getTipoArtefato().toString().equals("VIDEO")) {
			if (!(obj.getLink().contains("<iframe ")
					&& obj.getLink().contains("</iframe>"))) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ArtefatoAjuda_linkNaoEhIframe"));
			}
		}
	}
    
    public List<ArtefatoAjudaVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (campoConsulta.equals(TipoConsultaComboArtefatoAjudaEnum.TITULO.toString())) {
            return consultarPorTitulo(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArtefatoAjudaEnum.DESCRICAO.toString())) {
            return consultarPorDescricao(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArtefatoAjudaEnum.PALAVRAS_CHAVE.toString())) {
            return consultarPorPalavrasChave(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArtefatoAjudaEnum.TIPO_ARTEFATO.toString())) {
            return consultarPorTipoArtefato(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
        }
        if (campoConsulta.equals(TipoConsultaComboArtefatoAjudaEnum.RESPONSAVEL_CADASTRO.toString())) {
            return consultarPorResponsavelCadastro(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
        }
        return null;
    }
    
    public List<ArtefatoAjudaVO> consultarPorTitulo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ArtefatoAjuda WHERE sem_acentos(titulo) ilike sem_acentos('%" + valorConsulta + "%') ORDER BY titulo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
	
    public List<ArtefatoAjudaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ArtefatoAjuda WHERE sem_acentos(descricao) ilike sem_acentos('%" + valorConsulta + "%') ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    public List<ArtefatoAjudaVO> consultarPorPalavrasChave(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ArtefatoAjuda WHERE sem_acentos(palavrasChave) ilike sem_acentos('%" + valorConsulta.toLowerCase() + "%') ORDER BY palavrasChave";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
    
    public List<ArtefatoAjudaVO> consultarPorTipoArtefato(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ArtefatoAjuda WHERE sem_acentos(tipoArtefato) ilike sem_acentos('%" + valorConsulta + "%') ORDER BY tipoArtefato";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
    
    public List<ArtefatoAjudaVO> consultarPorResponsavelCadastro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT a.* FROM ArtefatoAjuda as a INNER JOIN Usuario as u ON a.responsavelCadastro = u.codigo WHERE sem_acentos(u.nome) ilike sem_acentos('%" + valorConsulta + "%') ORDER BY u.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
   
    public List<ArtefatoAjudaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ArtefatoAjuda WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @author Paulo Taucci
     * @return List Contendo vários objetos da classe <code>ArtefatoAjudaVO</code>
     *         resultantes da consulta.
     */
    @SuppressWarnings("unchecked")
	public static List<ArtefatoAjudaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe <code>ArtefatoAjudaVO</code>.
     * @author Paulo Taucci
     * @return O objeto da classe <code>ArtefatoAjudaVO</code> com os dados devidamente
     *         montados.
     */
    public static ArtefatoAjudaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ArtefatoAjudaVO obj = new ArtefatoAjudaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setTitulo(dadosSQL.getString("titulo"));
        obj.setTextoInformativo(dadosSQL.getString("textoInformativo"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setTipoArtefato(TipoArtefatoAjudaEnum.valueOf(dadosSQL.getString("tipoArtefato")));
        obj.setPalavrasChave(dadosSQL.getString("palavrasChave"));
        obj.setLink(dadosSQL.getString("link"));
        obj.setModulo(dadosSQL.getString("modulo"));
        obj.setArtefatoDesatualizado(dadosSQL.getBoolean("artefatoDesatualizado"));
        obj.setTela(dadosSQL.getString("tela"));
        obj.setSubmodulo(dadosSQL.getString("submodulo"));
        obj.setVersao(dadosSQL.getString("versao"));
        obj.setRecurso(dadosSQL.getString("recurso"));
        if (obj.getArtefatoDesatualizado()) {
//	        obj.getResponsavelAssinalarDesatualizado().setCodigo(dadosSQL.getInt("responsavelAssinalarDesatualizado"));
	        obj.setDataAssinaladoDesatualizado(dadosSQL.getDate("dataAssinaladoDesatualizado"));
        }
        //obj.getResponsavelCadastro().setCodigo(dadosSQL.getInt("responsavelCadastro"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        
//        if (obj.getArtefatoDesatualizado()) {
//        	obj.setResponsavelAssinalarDesatualizado(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAssinalarDesatualizado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
//        }
        //obj.setResponsavelCadastro(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCadastro().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
        return obj;
    }

    
    public ArtefatoAjudaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ArtefatoAjuda WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     * @author Paulo Taucci
     */
    public static String getIdEntidade() {
        return ArtefatoAjuda.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        ArtefatoAjuda.idEntidade = idEntidade;
    }
    
    public List<ArtefatoAjudaVO> consultarPorTodosCamposESituacao(String valorConsulta, boolean desatualizado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = " SELECT * FROM ArtefatoAjuda" +
        				" WHERE ((sem_acentos(titulo) ilike sem_acentos(?)) " +
        				" OR (sem_acentos(descricao) ilike sem_acentos(?))" +
        				" OR (sem_acentos(tipoArtefato) ilike sem_acentos(?))" +        				
        				" OR (sem_acentos(modulo) ilike sem_acentos(?)) " +
        				" OR (sem_acentos(palavraschave) ilike sem_acentos(?)) " +
        				" OR (sem_acentos(submodulo) ilike sem_acentos(?))) " +
        				" AND artefatoDesatualizado = " + desatualizado + "" +
        				" ORDER BY titulo ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarSincronizacaoArtefatoAjuda(UsuarioVO usuarioVO){
    	RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
    	try {
    		registroExecucaoJobVO.setNome(JobsEnum.JOB_ATUALIZAR_AJUDA.name());
    		registroExecucaoJobVO.setDataInicio(new Date());
    		RegistroExecucaoJobVO ultima =  getFacadeFactory().getRegistroExecucaoJobFacade().consultarRegistroExecucaoJobPorCodigoOrigemSemErro(JobsEnum.JOB_ATUALIZAR_AJUDA);
    		if(!Uteis.isAtributoPreenchido(ultima)) {
    			getConexao().getJdbcTemplate().execute("delete from artefatoajuda where codigo >= 1 "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		}    		
    		Gson gson = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    		Type type = new TypeToken<List<ArtefatoAjudaVO>>() {
			}.getType();
			HttpResponse<String> json = Unirest.get("https://atendimento.otimize-ti.com.br/OtimizeProject/webservice/artefatoAjuda/V2/atualizar/"+ (Uteis.isAtributoPreenchido(ultima) ? Uteis.getData(ultima.getDataInicio(), "dd/MM/yyyy") : "00/00/0000")+"/"+getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getIdAutenticacaoServOtimize()).header(HttpHeaders.AUTHORIZATION, "YmRlYzRkYmE5MmIwMjZlNTVhNjQ2NzkzZGZiMDZjNzhkYzUyZTliYTYwY2I3Y2U2NjExMjhjZTViOTUwMDY5ZA==").header("cliente", getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getIdAutenticacaoServOtimize()).asString();;
    		List<ArtefatoAjudaVO> artefatoAjudaVOs = gson.fromJson(json.getBody(), type);    		    		        		
    		for(ArtefatoAjudaVO artefatoAjudaVO: artefatoAjudaVOs) {
    			if(!artefatoAjudaVO.getNovidade())  {
    				alterar(artefatoAjudaVO, usuarioVO);
    			}
    		}
    		
    		json = Unirest.get("https://atendimento.otimize-ti.com.br/OtimizeProject/webservice/artefatoAjuda/V2/atualizarNovidade/"+ (Uteis.isAtributoPreenchido(ultima) ? Uteis.getData(ultima.getDataInicio(), "dd/MM/yyyy") : "00/00/0000")+"/"+getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getIdAutenticacaoServOtimize()).header(HttpHeaders.AUTHORIZATION, "YmRlYzRkYmE5MmIwMjZlNTVhNjQ2NzkzZGZiMDZjNzhkYzUyZTliYTYwY2I3Y2U2NjExMjhjZTViOTUwMDY5ZA==").header("cliente", getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, null).getIdAutenticacaoServOtimize()).asString();    		
    		artefatoAjudaVOs = gson.fromJson(json.getBody(), type);
    		
    		for(ArtefatoAjudaVO artefatoAjudaVO: artefatoAjudaVOs) {
    			if(artefatoAjudaVO.getNovidade()) {
    				NovidadeSeiVO novidadeSeiVO =  new NovidadeSeiVO();
    				novidadeSeiVO.setCodigo(artefatoAjudaVO.getCodigo());
    				novidadeSeiVO.setDescricao(artefatoAjudaVO.getTitulo());
    				novidadeSeiVO.setUrl(artefatoAjudaVO.getLink());
    				novidadeSeiVO.setVersao(artefatoAjudaVO.getVersao());
    				novidadeSeiVO.setData(artefatoAjudaVO.getDataVersao());
    				novidadeSeiVO.setCodigo(artefatoAjudaVO.getCodigo());
    				novidadeSeiVO.setPalavrasChaves(artefatoAjudaVO.getPalavrasChave());
    				novidadeSeiVO.setTipoNovidade(artefatoAjudaVO.getTipoNovidade());
    				novidadeSeiVO.setDataLimiteDisponibilidade(artefatoAjudaVO.getDataLimiteDisponibilidade());
    				novidadeSeiVO.setDataInicioDisponibilidade(artefatoAjudaVO.getDataInicioDisponibilidade());
    				novidadeSeiVO.setTipoArtefato(artefatoAjudaVO.getTipoArtefato());
    				novidadeSeiVO.setDestaque(artefatoAjudaVO.getDestaque());
    				novidadeSeiVO.setTextoInformativo(artefatoAjudaVO.getTextoInformativo());
    				
    				if(!artefatoAjudaVO.getArtefatoDesatualizado()) {
    					getFacadeFactory().getNovidadeSeiFacade().persistir(novidadeSeiVO);
    				}else {
    					getFacadeFactory().getNovidadeSeiFacade().excluir(novidadeSeiVO);
    				}
    			}
    		}
    		getAplicacaoControle().setArtefatoAjudaKeys(null);    		
    	}catch (Exception e) {
    		registroExecucaoJobVO.setTotalErro(1);
    		registroExecucaoJobVO.setErro(e.getMessage());
		}finally {
			registroExecucaoJobVO.setDataTermino(new Date());    		
			getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
			
		}
    }
    
    @Override
    public void consultarArtefatos(DataModelo controleConsulta, String valorConsulta, String modulo, String subModulo, String recurso) throws Exception {
    	String sqlStr = "select count(*) over() as qtde_total_registros, a.* from artefatoajuda a where artefatoDesatualizado =  false ";    	
    	if(Uteis.isAtributoPreenchido(modulo)) {
    		sqlStr += " and modulo = '"+modulo+"' ";
    	}
    	if(Uteis.isAtributoPreenchido(subModulo)) {
    		sqlStr += " and submodulo = '"+subModulo+"' ";
    	}
    	if(Uteis.isAtributoPreenchido(recurso)) {
    		sqlStr += " and recurso = '"+recurso+"' ";
    	}
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr += " and ((sem_acentos(titulo) ilike sem_acentos(?)) " +
					" OR (sem_acentos(descricao) ilike sem_acentos(?))" +
					" OR (sem_acentos(tipoArtefato) ilike sem_acentos(?))" +        				
					" OR (sem_acentos(modulo) ilike sem_acentos(?)) " +
					" OR (sem_acentos(palavraschave) ilike sem_acentos(?)) " +
					" OR (sem_acentos(submodulo) ilike sem_acentos(?))) ";
			
		}
		sqlStr += " order by a.modulo, a.submodulo, a.recurso, a.titulo ";
		sqlStr += " limit "+controleConsulta.getLimitePorPagina()+" offset "+controleConsulta.getOffset();
		SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT);
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		}
		if(tabelaResultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.previous();
		controleConsulta.setListaConsulta(montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, null));
    }
    
    @Override
    public List<ArtefatoAjudaVO> consultarArtefatoPorCodigos(String artefatos) throws Exception {
    	String sqlStr = "select * from artefatoajuda a where artefatoDesatualizado =  false ";
    	if(Uteis.isAtributoPreenchido(artefatos)) {
    		sqlStr += " and codigo in ("+artefatos+") ";
    	}
    
		sqlStr += " order by a.modulo, a.submodulo, a.recurso, a.titulo ";
		SqlRowSet tabelaResultado = null;
		tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);		
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, null);
    }
    
    @Override
    public Map<String, String> consultarArtefatoAjudaDisponivel() {
    	Map<String, String> telas =  new HashMap<String, String>(0);
    	try {
        String sqlStr = "select trim(unnest(string_to_array(tela, ';'))) as tela, array_to_string(array_agg(a.codigo), ',') as artefatos  from artefatoajuda a where trim(tela) != '' and coalesce(artefatodesatualizado, false) = false group by tela ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        while(tabelaResultado.next()) {
        	telas.put(tabelaResultado.getString("tela"), tabelaResultado.getString("artefatos"));
        }
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    	return telas;
    }
    
    
    @Override
    public List<ArtefatoAjudaVO> consultarArtefatoIndice(String valorConsulta) throws Exception {
    	String sqlStr = "select * from artefatoajuda a where artefatoDesatualizado =  false ";
    	if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr += " and ((sem_acentos(titulo) ilike sem_acentos(?)) " +
					" OR (sem_acentos(descricao) ilike sem_acentos(?))" +
					" OR (sem_acentos(tipoArtefato) ilike sem_acentos(?))" +        				
					" OR (sem_acentos(modulo) ilike sem_acentos(?)) " +
					" OR (sem_acentos(palavraschave) ilike sem_acentos(?)) " +
					" OR (sem_acentos(submodulo) ilike sem_acentos(?))) ";
			
		}
    	sqlStr += " order by a.modulo, a.submodulo, a.recurso, a.titulo ";
    	SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT, PERCENT+valorConsulta+PERCENT);
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		}        
        Map<String, ArtefatoAjudaVO> modulos =  new HashMap<String, ArtefatoAjudaVO>();
        Map<String, ArtefatoAjudaVO> submodulos =  new HashMap<String, ArtefatoAjudaVO>();
        Map<String, ArtefatoAjudaVO> recursos =  new HashMap<String, ArtefatoAjudaVO>();
        while(tabelaResultado.next()) {
        	ArtefatoAjudaVO modulo = null;
        	if(!modulos.containsKey(tabelaResultado.getString("modulo"))) {
        		modulo =  new ArtefatoAjudaVO();
        		modulo.setTitulo(tabelaResultado.getString("modulo"));
        		modulos.put(tabelaResultado.getString("modulo"), modulo);
        	}
        	modulo = modulos.get(tabelaResultado.getString("modulo"));
        	ArtefatoAjudaVO submodulo = null;
        	if(Uteis.isAtributoPreenchido(tabelaResultado.getString("submodulo")) && !submodulos.containsKey(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo"))) {
        		submodulo = new ArtefatoAjudaVO();
        		submodulo.setTitulo(tabelaResultado.getString("submodulo"));
        		submodulos.put(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo"), submodulo);
        		modulo.getArtefatoAjudaVOs().add(submodulo);
        	}
        	if(Uteis.isAtributoPreenchido(tabelaResultado.getString("submodulo"))){
        		submodulo = submodulos.get(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo"));
        	}
        	if(Uteis.isAtributoPreenchido(tabelaResultado.getString("submodulo"))){
        		submodulo = submodulos.get(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo"));
        	}
        	ArtefatoAjudaVO recurso = null;
        	if(Uteis.isAtributoPreenchido(tabelaResultado.getString("submodulo")) && !recursos.containsKey(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo")+tabelaResultado.getString("recurso"))) {
        		recurso = new ArtefatoAjudaVO();        		
        		recurso.setTitulo(tabelaResultado.getString("recurso"));
        		String[] telas = tabelaResultado.getString("tela").split(";");
        		if(telas != null && telas.length > 0) {
        			recurso.setTela(telas[0]);
        			recurso.setMaximixado(false);
        		}
        		recursos.put(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo")+tabelaResultado.getString("recurso"), recurso);
        		if(submodulo != null) {
        			submodulo.getArtefatoAjudaVOs().add(recurso);
        		}else {
        			modulo.getArtefatoAjudaVOs().add(recurso);
        		}
        	}
//        	if(Uteis.isAtributoPreenchido(tabelaResultado.getString("recurso"))){
//        		recurso = recursos.get(tabelaResultado.getString("modulo")+tabelaResultado.getString("submodulo")+tabelaResultado.getString("recurso"));
//        	}

//        	ArtefatoAjudaVO ajuda = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, null);
//        	if(recurso != null) {
//        		recurso.getArtefatoAjudaVOs().add(ajuda);
//        	}else if(submodulo != null) {        		
//        		submodulo.getArtefatoAjudaVOs().add(ajuda);
//        	}else {
//        		modulo.getArtefatoAjudaVOs().add(ajuda);
//        	}
        }
        List<ArtefatoAjudaVO> list = new ArrayList<ArtefatoAjudaVO>(modulos.values());
        Ordenacao.ordenarLista(list, "titulo");
        return list;
    }
    
   

	

}
