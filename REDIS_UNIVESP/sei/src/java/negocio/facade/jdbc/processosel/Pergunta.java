package negocio.facade.jdbc.processosel;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.estagio.GrupoPessoaItemVO;
import negocio.comuns.estagio.GrupoPessoaVO;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.processosel.enumeradores.TipoLayoutApresentacaoResultadoPerguntaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.PerguntaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>PerguntaVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>PerguntaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see PerguntaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
@SuppressWarnings({"unchecked", "rawtypes"})
public class Pergunta extends ControleAcesso implements PerguntaInterfaceFacade {

	private static final long serialVersionUID = -7570072985037150424L;

	protected static String idEntidade;

    public Pergunta() throws Exception {
        super();
        setIdEntidade("Pergunta");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>PerguntaVO</code>.
     */
    public PerguntaVO novo() throws Exception {
        Pergunta.incluir(getIdEntidade());
        PerguntaVO obj = new PerguntaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PerguntaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>PerguntaVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PerguntaVO obj, EscopoPerguntaEnum escopoPerguntaBase,  UsuarioVO usuario) throws Exception {
        try {        	
            PerguntaVO.validarDados(obj);
            Pergunta.incluir(getIdEntidade(escopoPerguntaBase), usuario);
            final String sql = "INSERT INTO Pergunta( descricao, tipoResposta, layoutPergunta, tipoResultadoGrafico, escopoPergunta, perguntaEscopoPublico, quantidadeCasasDecimais, mascaraData, mascaraHora, orientacoesSobreCampo, extensaoTipoResposta) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setString(2, obj.getTipoResposta());
                    sqlInserir.setString(3, obj.getLayoutPergunta());
                    if(obj.getTipoRespostaMultiplaEscolha() || obj.getTipoRespostaSimplesEscolha()){
                    	sqlInserir.setString(4, obj.getTipoResultadoGrafico().name());
                    }else{
                    	sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, obj.getEscopoPergunta().name());
                    sqlInserir.setBoolean(6, obj.getPerguntaEscopoPublico());
                    sqlInserir.setInt(7, obj.getQuantidadeCasasDecimais());
                    sqlInserir.setString(8, obj.getMascaraData());
                    sqlInserir.setString(9, obj.getMascaraHora());
                    sqlInserir.setString(10, obj.getOrientacoesSobreCampo());
                    sqlInserir.setString(11, obj.getExtensaoTipoResposta());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getRespostaPerguntaFacade().incluirRespostaPerguntas(obj.getCodigo(), obj.getTipoResposta(), obj.getRespostaPerguntaVOs(), usuario);
             
            if(Uteis.isAtributoPreenchido(obj.getPerguntaItemVOs())) {
            	getFacadeFactory().getPerguntaItemInterfaceFacade().incluirPerguntaItens(obj.getCodigo(), obj.getPerguntaItemVOs(), usuario);
            }
            validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaPerguntaChecklistVO(), "perguntaChecklist", "pergunta", obj.getCodigo(), usuario);
            getFacadeFactory().getPerguntaChecklistFacade().persistir(obj.getListaPerguntaChecklistVO(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PerguntaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>PerguntaVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PerguntaVO obj, EscopoPerguntaEnum escopoPerguntaBase,  UsuarioVO usuario) throws Exception {
        try {
            PerguntaVO.validarDados(obj);
            Pergunta.alterar(getIdEntidade(escopoPerguntaBase), usuario);
            final String sql = "UPDATE Pergunta set descricao=?, tipoResposta=?, layoutPergunta=?, tipoResultadoGrafico = ?, escopoPergunta=?, perguntaEscopoPublico=?, quantidadeCasasDecimais=?, mascaraData=?, mascaraHora=?, orientacoesSobreCampo=?, extensaoTipoResposta=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setString(2, obj.getTipoResposta());
                    sqlAlterar.setString(3, obj.getLayoutPergunta());
                    if(obj.getTipoRespostaMultiplaEscolha() || obj.getTipoRespostaSimplesEscolha()){
                    	sqlAlterar.setString(4, obj.getTipoResultadoGrafico().name());
                    }else{
                    	sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setString(5, obj.getEscopoPergunta().name());
                    sqlAlterar.setBoolean(6, obj.getPerguntaEscopoPublico());
                    sqlAlterar.setInt(7, obj.getQuantidadeCasasDecimais());
                    sqlAlterar.setString(8, obj.getMascaraData());
                    sqlAlterar.setString(9, obj.getMascaraHora());
                    sqlAlterar.setString(10, obj.getOrientacoesSobreCampo());
                    sqlAlterar.setString(11, obj.getExtensaoTipoResposta());
                    sqlAlterar.setInt(12, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getRespostaPerguntaFacade().alterarRespostaPerguntas(obj.getCodigo(), obj.getTipoResposta(), obj.getRespostaPerguntaVOs(), usuario);
            
            getFacadeFactory().getPerguntaItemInterfaceFacade().alterarPerguntaItens(obj.getCodigo(), obj.getPerguntaItemVOs(), usuario);
            validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaPerguntaChecklistVO(), "perguntaChecklist", "pergunta", obj.getCodigo(), usuario);
            getFacadeFactory().getPerguntaChecklistFacade().persistir(obj.getListaPerguntaChecklistVO(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PerguntaVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>PerguntaVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PerguntaVO obj, EscopoPerguntaEnum escopoPerguntaBase,  UsuarioVO usuario) throws Exception {
        try {
            Pergunta.excluir(getIdEntidade(escopoPerguntaBase), usuario);
            String sql = "DELETE FROM Pergunta WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
            getFacadeFactory().getRespostaPerguntaFacade().excluirRespostaPerguntas(obj.getCodigo(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarPerguntaChecklistVO(PerguntaVO obj,  PerguntaChecklistVO perguntaChecklistVO, UsuarioVO usuario) {
    	perguntaChecklistVO.setPerguntaVO(obj);
		if(obj.getListaPerguntaChecklistVO().stream().noneMatch(p-> p.getDescricao().equals(perguntaChecklistVO.getDescricao()))) {
			obj.getListaPerguntaChecklistVO().add(perguntaChecklistVO);	
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerPerguntaChecklistVO(PerguntaVO obj,  PerguntaChecklistVO perguntaChecklistVO, UsuarioVO usuario) {		
		obj.getListaPerguntaChecklistVO().removeIf(p-> p.getDescricao().equals(perguntaChecklistVO.getDescricao()));
	}

    /**
     * Responsável por realizar uma consulta de <code>Pergunta</code> através do valor do atributo 
     * <code>String tipoResposta</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerguntaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PerguntaVO> consultarPorTipoResposta(String valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Pergunta WHERE upper( tipoResposta ) like('" + valorConsulta.toUpperCase() + "%')";
        if(escopoBase != null){
    		switch (escopoBase) {
			case AVALIACAO_INSTITUCIONAL:
				sqlStr += " and (escopoPergunta = 'AVALIACAO_INSTITUCIONAL' or perguntaEscopoPublico = true)";
				break;
			case BANCO_CURRICULUM:
				sqlStr += " and (escopoPergunta = 'BANCO_CURRICULUM' or perguntaEscopoPublico = true)";				
				break;
			case REQUERIMENTO:
				sqlStr += " and (escopoPergunta = 'REQUERIMENTO' or perguntaEscopoPublico = true)";				
				break;
			case PROCESSO_SELETIVO:
				sqlStr += " and (escopoPergunta = 'PROCESSO_SELETIVO' or perguntaEscopoPublico = true)";				
				break;
			case REQUISICAO:
				sqlStr += " and (escopoPergunta = 'REQUISICAO' or perguntaEscopoPublico = true)";				
				break;
			case ESTAGIO:
				sqlStr += " and (escopoPergunta = 'ESTAGIO')";				
				break;
			case PLANO_ENSINO:
				sqlStr += " and (escopoPergunta = 'PLANO_ENSINO')";				
				break;
			case RELATORIO_FACILITADOR:
				sqlStr += " and (escopoPergunta = 'RELATORIO_FACILITADOR')";				
				break;
			default:				
				break;
			}
    	}
        sqlStr += " ORDER BY tipoResposta";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Pergunta</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerguntaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PerguntaVO> consultarPorDescricao(String valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM Pergunta WHERE upper(sem_acentos(descricao)) like(sem_acentos(?)) ");
        List<Object> filtros = new ArrayList<>();
        filtros.add(PERCENT + valorConsulta.toUpperCase() + PERCENT);

        if (Uteis.isAtributoPreenchido(escopoBase)) {
        	sql.append("and (escopoPergunta = ? ");
        	if(!escopoBase.getValor().equals("PE") && !escopoBase.getValor().equals("ES") && !escopoBase.getValor().equals("RF")) {
        		sql.append(" or perguntaEscopoPublico = true");
        	}
        	sql.append(")");
        	filtros.add(escopoBase.name());
			
    	}
        sql.append(" ORDER BY descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Pergunta</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>PerguntaVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PerguntaVO> consultarPorCodigo(Integer valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Pergunta WHERE codigo = " + valorConsulta.intValue() + " ";
        if(escopoBase != null){
    		switch (escopoBase) {
			case AVALIACAO_INSTITUCIONAL:
				sqlStr += " and (escopoPergunta = 'AVALIACAO_INSTITUCIONAL' or perguntaEscopoPublico = true)";
				break;
			case BANCO_CURRICULUM:
				sqlStr += " and (escopoPergunta = 'BANCO_CURRICULUM' or perguntaEscopoPublico = true)";				
				break;
			case REQUERIMENTO:
				sqlStr += " and (escopoPergunta = 'REQUERIMENTO' or perguntaEscopoPublico = true)";
				
				break;
			case PROCESSO_SELETIVO:
				sqlStr += " and (escopoPergunta = 'PROCESSO_SELETIVO' or perguntaEscopoPublico = true)";				
				break;
			case REQUISICAO:
				sqlStr += " and (escopoPergunta = 'REQUISICAO' or perguntaEscopoPublico = true)";				
				break;
			case ESTAGIO:
				sqlStr += " and (escopoPergunta = 'ESTAGIO')";				
				break;
			case PLANO_ENSINO:
				sqlStr += " and (escopoPergunta = 'PLANO_ENSINO')";				
				break;
			case RELATORIO_FACILITADOR:
				sqlStr += " and (escopoPergunta = 'RELATORIO_FACILITADOR')";				
				break;
			default:				
				break;
			}
    	}
        sqlStr += " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<PerguntaVO> consultaPorCodigo(Integer valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Pergunta WHERE codigo = " + valorConsulta.intValue() + " ";
        if(escopoBase != null){
    		switch (escopoBase) {
			case AVALIACAO_INSTITUCIONAL:
				sqlStr += " and (escopoPergunta = 'AVALIACAO_INSTITUCIONAL' or perguntaEscopoPublico = true)";
				break;
			case BANCO_CURRICULUM:
				sqlStr += " and (escopoPergunta = 'BANCO_CURRICULUM' or perguntaEscopoPublico = true)";				
				break;
			case REQUERIMENTO:
				sqlStr += " and (escopoPergunta = 'REQUERIMENTO' or perguntaEscopoPublico = true)";
				
				break;
			case PROCESSO_SELETIVO:
				sqlStr += " and (escopoPergunta = 'PROCESSO_SELETIVO' or perguntaEscopoPublico = true)";				
				break;
			case ESTAGIO:
				sqlStr += " and (escopoPergunta = 'ESTAGIO')";				
				break;
			case PLANO_ENSINO:
				sqlStr += " and (escopoPergunta = 'PLANO_ENSINO')";				
				break;
			default:				
				break;
			}
    	}
        sqlStr += " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<PerguntaVO> consultarPorCodigoQuestionario(Integer questionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Pergunta.* FROM Pergunta, PerguntaQuestionario WHERE "
                + "Pergunta.codigo = PerguntaQuestionario.pergunta "
                + "AND PerguntaQuestionario.questionario = " + questionario.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>PerguntaVO</code> resultantes da consulta.
     */
    public static List<PerguntaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PerguntaVO> vetResultado = new ArrayList<PerguntaVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>PerguntaVO</code>.
     * @return  O objeto da classe <code>PerguntaVO</code> com os dados devidamente montados.
     */
    public static PerguntaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        PerguntaVO obj = new PerguntaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setOrientacoesSobreCampo(dadosSQL.getString("orientacoesSobreCampo"));
        obj.setLayoutPergunta(dadosSQL.getString("layoutPergunta"));
        obj.setTipoResposta(dadosSQL.getString("tipoResposta"));
        obj.setPerguntaEscopoPublico(dadosSQL.getBoolean("perguntaEscopoPublico"));
        if(dadosSQL.getString("escopoPergunta") != null){
        	obj.setEscopoPergunta(EscopoPerguntaEnum.valueOf(dadosSQL.getString("escopoPergunta")));
        }else{
        	obj.setEscopoPergunta(EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL);
        }
        
        if(obj.getTipoResposta().equals("TE")) {
        	obj.setApresentarRespostaTextual(true);
        }
        
        if(dadosSQL.getString("tipoResultadoGrafico") != null){
			obj.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.valueOf(dadosSQL.getString("tipoResultadoGrafico")));	
		}else{
			if(obj.getTipoRespostaMultiplaEscolha()){
				obj.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_COLUNA);
			}
			if(obj.getTipoRespostaSimplesEscolha()){
				obj.setTipoResultadoGrafico(TipoLayoutApresentacaoResultadoPerguntaEnum.GRAFICO_PIZZA);
			}			
		}
        obj.setMascaraData(dadosSQL.getString("mascaraData"));
        obj.setMascaraHora(dadosSQL.getString("mascaraHora"));
        obj.setQuantidadeCasasDecimais(dadosSQL.getInt("quantidadeCasasDecimais"));
        obj.setExtensaoTipoResposta(dadosSQL.getString("extensaoTipoResposta"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setListaPerguntaChecklistVO(getFacadeFactory().getPerguntaChecklistFacade().consultarPerguntaChecklistPorPerguntaVO(obj, nivelMontarDados, usuario));
        obj.setRespostaPerguntaVOs(RespostaPergunta.consultarRespostaPerguntas(obj.getCodigo(), nivelMontarDados, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        obj.setPerguntaItemVOs(getFacadeFactory().getPerguntaItemInterfaceFacade().consultarPerguntaItens(obj.getCodigo(), nivelMontarDados, usuario));
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PerguntaVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public PerguntaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Pergunta WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Pergunta ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade(EscopoPerguntaEnum escopoPerguntaEnum) {
    	if(escopoPerguntaEnum != null){
    		switch (escopoPerguntaEnum) {
			case AVALIACAO_INSTITUCIONAL:
				return "PerguntaAvaliacaoInstitucional";
			case BANCO_CURRICULUM:
				return "PerguntaBancoCurriculum";
			case REQUERIMENTO:
				return "PerguntaRequerimento";
			case PROCESSO_SELETIVO:
				return "PerguntaProcessoSeletivo";
			case ESTAGIO:
				return "CamposEstagio";
			case PLANO_ENSINO:
				return "PerguntaPlanoEnsino";
			default:
				return Pergunta.idEntidade;
			}
    	}
        return Pergunta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Pergunta.idEntidade = idEntidade;
    }
    
    @Override
    public void alterarOrdemOpcaoRespostaQuestao(PerguntaVO perguntaVO, RespostaPerguntaVO respostaPerguntaVO1, RespostaPerguntaVO respostaPerguntaVO2) throws Exception {
        int ordem1 = respostaPerguntaVO1.getOrdem();
        respostaPerguntaVO1.setOrdem(respostaPerguntaVO2.getOrdem());
        respostaPerguntaVO2.setOrdem(ordem1);        
        Ordenacao.ordenarLista(perguntaVO.getRespostaPerguntaVOs(), "ordem");
    }
    
    /**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>PerguntaItemVO</code> ao List
	 * <code>perguntaItemVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PerguntaItem</code> -
	 * getPergunta().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>PerguntaItemVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjPerguntaItemVOs(PerguntaItemVO obj, List<PerguntaItemVO> perguntaItemVOs) throws Exception {
		PerguntaItemVO.validarDados(obj);
		int index = 0;
		Iterator i = perguntaItemVOs.iterator();
		while (i.hasNext()) {
			PerguntaItemVO objExistente = (PerguntaItemVO) i.next();
			if (objExistente.getPerguntaVO().getCodigo().equals(obj.getPerguntaVO().getCodigo())) {
				perguntaItemVOs.set(index, obj);
				return;
			}
			index++;
		}
		perguntaItemVOs.add(obj);
		realizaReorganizacaoOrdemPergunta(perguntaItemVOs);
	}

	public void realizaReorganizacaoOrdemPergunta(List<PerguntaItemVO> perguntaItemVOs) {
		int index = 1;
		Iterator i = perguntaItemVOs.iterator();
		while (i.hasNext()) {
			PerguntaItemVO objExistente = (PerguntaItemVO) i.next();
			objExistente.setOrdem(index++);
		}
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>PerguntaItemVO</code> no List
	 * <code>PerguntaItemVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PerguntaItem</code> -
	 * getPergunta().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param pergunta
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjPerguntaItemVOs(Integer pergunta, List<PerguntaItemVO> perguntaItemVOs) throws Exception {
		int index = 0;
		Iterator i = perguntaItemVOs.iterator();
		while (i.hasNext()) {
			PerguntaItemVO objExistente = (PerguntaItemVO) i.next();
			if (objExistente.getPerguntaVO().getCodigo().equals(pergunta)) {
				perguntaItemVOs.remove(index);
				realizaReorganizacaoOrdemPergunta(perguntaItemVOs);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>PerguntaItemVO</code> no List
	 * <code>PerguntaItemVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PerguntaItem</code> -
	 * getPergunta().getCodigo() - como identificador (key) do objeto no List.
	 *
	 * @param pergunta
	 *            Parâmetro para localizar o objeto do List.
	 */
	public PerguntaItemVO consultarObjPerguntaItemVO(Integer pergunta, List<PerguntaItemVO> perguntaItemVOs) throws Exception {
		Iterator i = perguntaItemVOs.iterator();
		while (i.hasNext()) {
			PerguntaItemVO objExistente = (PerguntaItemVO) i.next();
			if (objExistente.getPerguntaVO().getCodigo().equals(pergunta)) {
				return objExistente;
			}
		}
		return null;
	}	 
	
    public void removerPerguntaListaPergunta(Integer pergunta, List<PerguntaVO> listaPergunta) {
        int index = 0;
        for(PerguntaVO objExistente : listaPergunta){
            if (objExistente.getCodigo().equals(pergunta)) {
            	listaPergunta.remove(index);
                return;
            }
            index++;
        }
    }
    
    public List<PerguntaVO> consultarPorDescricaoTipoResposta(String valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Pergunta WHERE upper(sem_acentos(descricao)) like(sem_acentos('" + valorConsulta.toUpperCase() + "%')) and escopoPergunta = 'PLANO_ENSINO' and tipoResposta in ('ME', 'SE', 'TE', 'NU', 'DT', 'HR', 'LS', 'VF') ";
        sqlStr += " ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<PerguntaVO> consultarPorCodigoTipoResposta(Integer valorConsulta, EscopoPerguntaEnum escopoBase, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Pergunta WHERE codigo = " + valorConsulta.intValue() + " and escopoPergunta = 'PLANO_ENSINO' and tipoResposta in ('ME', 'SE', 'TE', 'NU', 'DT', 'HR', 'LS', 'VF') ";
        sqlStr += " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public void alterarOrdemPergunta(PerguntaVO perguntaVO, PerguntaItemVO perguntaItemVO1, PerguntaItemVO perguntaItemVO2) throws Exception {
        int ordem1 = perguntaItemVO1.getOrdem();
        perguntaItemVO1.setOrdem(perguntaItemVO2.getOrdem());
        perguntaItemVO2.setOrdem(ordem1);        
        Ordenacao.ordenarLista(perguntaVO.getPerguntaItemVOs(), "ordem");
    }
    
}
