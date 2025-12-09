package negocio.facade.jdbc.processosel;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioAlunoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaAlunoVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioRequerimentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.QuestionarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>QuestionarioVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>QuestionarioVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see QuestionarioVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Questionario extends ControleAcesso implements QuestionarioInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3145433529399174086L;
	protected static String idEntidade;

    public Questionario() throws Exception {
        super(); 
        setIdEntidade("Questionario");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>QuestionarioVO</code>.
     */
    public QuestionarioVO novo() throws Exception {
        Questionario.incluir(getIdEntidade());
        QuestionarioVO obj = new QuestionarioVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>QuestionarioVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>QuestionarioVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final QuestionarioVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception {
        try {
        	if (validarUnicidade(obj, usuario)) {
        		throw new Exception("Já existe um questionário cadastrado com essa descrição: " + obj.getDescricao());
    		}
            QuestionarioVO.validarDados(obj);            
            final String sql = "INSERT INTO Questionario( descricao, situacao, escopo, tipoEscopoRequerimento ) VALUES ( ?, ?, ?, ? ) returning codigo "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlInserir = con.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setString(2, obj.getSituacao());
                    sqlInserir.setString(3, obj.getEscopo());
                    if(obj.getTipoEscopoRequerimento() != null && obj.getEscopo().equals("RE")){
                    	sqlInserir.setString(4, obj.getTipoEscopoRequerimento().name());
                    }else{
                    	sqlInserir.setNull(4, 0);
                    }
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
            getFacadeFactory().getPerguntaQuestionarioFacade().incluirPerguntaQuestionarios(obj.getCodigo(), obj.getPerguntaQuestionarioVOs(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>QuestionarioVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>QuestionarioVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final QuestionarioVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception {
        try {
        	if (validarUnicidade(obj, usuario)) {
        		throw new Exception("Já existe um questionário cadastrado com essa descrição: " + obj.getDescricao());
    		}
        	QuestionarioVO.validarDados(obj);            
            //Questionario.alterar(getIdEntidade(obj.getEscopo()), validarAcesso, usuario);            
            final String sql = "UPDATE Questionario set descricao=?, situacao=?, escopo=?, tipoEscopoRequerimento = ? WHERE ((codigo = ?))"+(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement sqlAlterar = con.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setString(2, obj.getSituacao());
                    sqlAlterar.setString(3, obj.getEscopo());
                    if(obj.getTipoEscopoRequerimento() != null && obj.getEscopo().equals("RE")){
                    	sqlAlterar.setString(4, obj.getTipoEscopoRequerimento().name());
                    }else{
                    	sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getPerguntaQuestionarioFacade().alterarPerguntaQuestionarios(obj.getCodigo(), obj.getPerguntaQuestionarioVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>QuestionarioVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>QuestionarioVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(QuestionarioVO obj, boolean validarAcesso, UsuarioVO usuario) throws Exception {
        try {        	
            excluir(getIdEntidade(obj.getEscopo()), validarAcesso, usuario);            
            String sql = "DELETE FROM Questionario WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
            getFacadeFactory().getPerguntaQuestionarioFacade().excluirPerguntaQuestionarios(obj.getCodigo(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    public void montarQuestionarioRespostasDoQuestionarioAluno(QuestionarioVO obj, QuestionarioAlunoVO questionarioAluno) throws Exception {
        for (RespostaPerguntaAlunoVO resposta : questionarioAluno.getListaRespostaPerguntaAlunoVO()) {
            for (PerguntaQuestionarioVO pergunta : obj.getPerguntaQuestionarioVOs()) {
                if (resposta.getPerguntaQuestionario().getCodigo().intValue() == pergunta.getPergunta().getCodigo().intValue()) {
                    if (pergunta.getPergunta().getTipoRespostaTextual()) {
                        pergunta.getPergunta().setTexto(resposta.getTexto());
                    } else {
                        for (int i = 0; i < pergunta.getPergunta().getRespostaPerguntaVOs().size(); i++) {
                            if (pergunta.getPergunta().getRespostaPerguntaVOs().get(i).getCodigo().intValue() == resposta.getRespostaQuestionarioAluno().getCodigo().intValue()) {
                                pergunta.getPergunta().getRespostaPerguntaVOs().get(i).setSelecionado(Boolean.TRUE);
                            }
                        }
                    }
                }
            }
        }
    }

    public void executarRestauracaoRespostaQuestionarioPorInscricao(Integer inscricao, QuestionarioVO questionarioVO) throws Exception {
        List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().consultarRespostaQuestionarioPorInscricao(inscricao, questionarioVO.getCodigo());
        for (RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO : respostaAvaliacaoInstitucionalDWVOs) {
            executarRegistrarRespostaQuestionario(respostaAvaliacaoInstitucionalDWVO, questionarioVO);
        }
    }
    
    @Override
    public void executarRestauracaoRespostaQuestionarioPorRequerimento(Integer requerimento, QuestionarioVO questionarioVO) throws Exception {
    	List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().consultarRespostaQuestionarioPorRequerimento(requerimento, questionarioVO.getCodigo());
    	for (RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO : respostaAvaliacaoInstitucionalDWVOs) {
    		executarRegistrarRespostaQuestionario(respostaAvaliacaoInstitucionalDWVO, questionarioVO);
    	}
    }
    
    @Override    
    public Boolean executarRestauracaoRespostaQuestionarioPorRequerimentoHistorico(Integer requerimento, Integer departamentoTramite, Integer ordemTramite, QuestionarioVO questionarioVO) throws Exception {
    	List<RespostaAvaliacaoInstitucionalDWVO> respostaAvaliacaoInstitucionalDWVOs = getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().consultarRespostaQuestionarioPorRequerimentoHistorico(requerimento, departamentoTramite, ordemTramite, questionarioVO.getCodigo());
    	for (RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO : respostaAvaliacaoInstitucionalDWVOs) {
    		executarRegistrarRespostaQuestionario(respostaAvaliacaoInstitucionalDWVO, questionarioVO);
    	}
    	return !respostaAvaliacaoInstitucionalDWVOs.isEmpty();
    }

    public void executarRegistrarRespostaQuestionario(RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO, QuestionarioVO questionarioVO) {
        for (PerguntaQuestionarioVO perguntaQuestionarioVO : questionarioVO.getPerguntaQuestionarioVOs()) {
            if (perguntaQuestionarioVO.getPergunta().getCodigo().intValue() == respostaAvaliacaoInstitucionalDWVO.getPergunta().intValue()) {
                if (perguntaQuestionarioVO.getPergunta().getTipoRespostaTextual()) {
                    perguntaQuestionarioVO.getPergunta().setTexto(respostaAvaliacaoInstitucionalDWVO.getResposta());
                } else {
                    for (RespostaPerguntaVO respostaPerguntaVO : perguntaQuestionarioVO.getPergunta().getRespostaPerguntaVOs()) {
                        if (respostaAvaliacaoInstitucionalDWVO.getResposta().contains("[" + respostaPerguntaVO.getCodigo() + "]")) {
                            respostaPerguntaVO.setSelecionado(Boolean.TRUE);
                        }
                        
                        if (respostaAvaliacaoInstitucionalDWVO.getRespostaAdicional().contains("[" + respostaPerguntaVO.getCodigo() + "]")) {
                        	String[] textos = respostaAvaliacaoInstitucionalDWVO.getRespostaAdicional().split("\\[");
                        	for(String texto : textos){		
                        		if(texto.contains(respostaPerguntaVO.getCodigo() + "]")){                        			
                        			texto = texto.substring(texto.indexOf("{")+1, texto.length()-1);
                        			respostaPerguntaVO.setRespostaAdicional(texto);
                        			break;
                        		}
                        	}
                        }
                    }

                }
            }
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Questionario</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>QuestionarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<QuestionarioVO> consultarPorDescricao(String valorConsulta, String escopoBase,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);    	
        String sqlStr = "SELECT * FROM Questionario WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ";
        if(escopoBase != null && !escopoBase.trim().isEmpty()){
        	if(escopoBase.equals("RE")){
        		sqlStr +=  " and escopo in('RE')";
        	}else if(escopoBase.equals("PS")){
        		sqlStr +=  " and escopo in('PS')";
        	}else if(escopoBase.equals("BC")){
        		sqlStr +=  " and escopo in('BC')";
        	}else if(escopoBase.equals("AI")){        		
        		sqlStr +=  " and escopo not in('RE', 'PS', 'BC', 'PE', 'RQ')";
        	}else if(escopoBase.equals("PE")){        		
        		sqlStr +=  " and escopo in('PE')";
        	}else if(escopoBase.equals("ES")){        		
        		sqlStr +=  " and escopo in('ES')";
        	}else if(escopoBase.equals("RQ")){        		
        		sqlStr +=  " and escopo in('RQ')";
        	}else{
        		sqlStr +=  " and escopo in('"+escopoBase+"')";
        	}
        }
        sqlStr += " ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<QuestionarioVO> consultarPorEscopo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Questionario WHERE upper( escopo ) = '" + valorConsulta.toUpperCase() + "' ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<QuestionarioVO> consultarPorEscopoSituacao(TipoEscopoQuestionarioPerguntaEnum valorConsulta, String situacao, boolean trazerQuestionarioEscopoGeral, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "";    	
    	sqlStr = "SELECT * FROM Questionario WHERE escopo in ('"+valorConsulta.getKey()+"' ";
    	if(trazerQuestionarioEscopoGeral){
    		sqlStr += ", '" + TipoEscopoQuestionarioPerguntaEnum.GERAL.getKey() + "' ";
    	}
//    	if(valorConsulta.equals(TipoEscopoQuestionarioPerguntaEnum.DISCIPLINA) || valorConsulta.equals(TipoEscopoQuestionarioPerguntaEnum.PROFESSOR)){
//    		sqlStr += ", '" + TipoEscopoQuestionarioPerguntaEnum.ULTIMO_MODULO.getKey() + "' ";
//    	}
    	sqlStr += ") and situacao = '"+situacao+"' ORDER BY descricao";    	    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    

    public List<QuestionarioVO> consultarPorEscopoSituacaoDiferenteEmConstrucao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Questionario WHERE upper( escopo ) like('" + valorConsulta.toUpperCase() + "%') AND situacao != 'EC' ORDER BY descricao";        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public QuestionarioVO consultarUltimoQuestionarioPorEscopoSituacao(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Questionario WHERE upper( escopo ) like('" + valorConsulta.toUpperCase() + "%') AND situacao = '" + situacao + "' ORDER by codigo desc";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return (montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return null;
    }

    public Boolean verificarQuestionarioVinculoAvaliacao(Integer valorConsulta, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT codigo FROM avaliacaoInstitucional WHERE questionario = " + valorConsulta.intValue();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    @Override
    public List<QuestionarioVO> consultarQuestionarioRequerimentoPorEscopoRequerimento(TipoEscopoQuestionarioRequerimentoEnum tipoEscopoQuestionarioRequerimentoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
    	consultar(getIdEntidade("RE"), controlarAcesso, usuario);
    	 String sqlStr = "SELECT * FROM Questionario WHERE  escopo = 'RE' and situacao in ('AT', 'IN') ";
         if(tipoEscopoQuestionarioRequerimentoEnum != null){
        	 sqlStr += " and tipoEscopoRequerimento = '"+tipoEscopoQuestionarioRequerimentoEnum.name()+"' ";
         }
         
         sqlStr +=  " ORDER BY codigo";
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
         return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Questionario</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>QuestionarioVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<QuestionarioVO> consultarPorCodigo(Integer valorConsulta, String escopoBase, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	
    	consultar(getIdEntidade(escopoBase), controlarAcesso, usuario);
    	
        String sqlStr = "SELECT * FROM Questionario WHERE codigo = " + valorConsulta.intValue() + " ";
        if(escopoBase != null && !escopoBase.trim().isEmpty()){
        	if(escopoBase.equals("RE")){
        		sqlStr +=  " and escopo in('RE')";
        	}else if(escopoBase.equals("PS")){
        		sqlStr +=  " and escopo in('PS')";
        	}else if(escopoBase.equals("BC")){
        		sqlStr +=  " and escopo in('BC')";
        	}else if(escopoBase.equals("AI")){        		
        		sqlStr +=  " and escopo not in('RE', 'PS', 'BC', 'PE')";
        	}else if(escopoBase.equals("PE")){        		
        		sqlStr +=  " and escopo in('PE')";
        	}else if(escopoBase.equals("ES")){        		
        		sqlStr +=  " and escopo in('ES')";
        	}else{
        		sqlStr +=  " and escopo in('"+escopoBase+"')";
        	}
        }
        sqlStr +=  " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public List<QuestionarioVO> consultarPorCodigoAvaliacao(Integer avaliacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Questionario.* FROM Questionario, AvaliacaoInstitucional WHERE ("
                + "Questionario.codigo = AvaliacaoInstitucional.questionario )"
                //+ "Questionario.codigo = AvaliacaoInstitucional.questionarioCurso OR "
                //+ "Questionario.codigo = AvaliacaoInstitucional.questionarioDisciplina OR "
                //+ "Questionario.codigo = AvaliacaoInstitucional.questionarioPerfilSocioEconomico OR "
                //+ "Questionario.codigo = AvaliacaoInstitucional.questionarioPlebicito) "
                + "AND AvaliacaoInstitucional.codigo = " + avaliacao.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<QuestionarioVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Questionario WHERE situacao = '" + valorConsulta + "' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>QuestionarioVO</code> resultantes da consulta.
     */
    public List<QuestionarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<QuestionarioVO> vetResultado = new ArrayList<QuestionarioVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>QuestionarioVO</code>.
     * @return  O objeto da classe <code>QuestionarioVO</code> com os dados devidamente montados.
     */
	public QuestionarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        QuestionarioVO obj = new QuestionarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setEscopo(dadosSQL.getString("escopo"));
        if(dadosSQL.getString("tipoEscopoRequerimento") != null){
        	obj.setTipoEscopoRequerimento(TipoEscopoQuestionarioRequerimentoEnum.valueOf(dadosSQL.getString("tipoEscopoRequerimento")));
        }
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setPerguntaQuestionarioVOs(getFacadeFactory().getPerguntaQuestionarioFacade().consultarPorCodigoQuestionario(obj.getCodigo(), false, nivelMontarDados, usuario));
        if (obj.getEscopo().equals("RE")) {
        	obj.setDesabilitarAlterarEscopoQuestionarioRequerimento(obj.getSituacao().equals("FI") ? true : consultarExistenciaVinculoQuestionarioTipoRequerimentoTipoRequerimentoDepartamento(obj.getCodigo()));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }

        return obj;
    }
    
   

    /**
     * Operação responsável por localizar um objeto da classe <code>QuestionarioVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public QuestionarioVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Questionario WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Questionario ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean validarUnicidade(QuestionarioVO obj, UsuarioVO usuario) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT codigo FROM Questionario ");
		sql.append(" WHERE upper(sem_acentos(descricao)) like(upper(sem_acentos(('").append(obj.getDescricao().trim()).append("')))) ");
		sql.append(" and escopo = '").append(obj.getEscopo()).append("'");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade(String tipoEscopoBase) {
    	if (!tipoEscopoBase.equals("RE") && !tipoEscopoBase.equals("BC") && !tipoEscopoBase.equals("PS") && !tipoEscopoBase.equals("PE") && !tipoEscopoBase.equals("RQ") && !tipoEscopoBase.equals("ES")) {
    		return "QuestionarioAvaliacaoInstitucional";
    	}
		if (tipoEscopoBase.equals("RE")) {
			return "QuestionarioRequerimento";
		}
		if (tipoEscopoBase.equals("BC")) {
			return "QuestionarioBancoCurriculum";
		}
		if (tipoEscopoBase.equals("PS")) {
			return "QuestionarioProcessoSeletivo";
		}
		if (tipoEscopoBase.equals("PE")) {
			return "FormularioPlanoEnsino";
		}
		if (tipoEscopoBase.equals("RQ")) {
			return "QuestionarioRequisicao";
		}
		if (tipoEscopoBase.equals("ES")) {
			return "FormularioEstagio";
		}
        return Questionario.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Questionario.idEntidade = idEntidade;
    }

    public void removerPerguntaListaQuestionario(Integer pergunta, List<PerguntaVO> listaPerguntaQuestionario) {
        int index = 0;
        for(PerguntaVO objExistente : listaPerguntaQuestionario){
            if (objExistente.getCodigo().equals(pergunta)) {
                listaPerguntaQuestionario.remove(index);
                return;
            }
            index++;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoAvaliacao(final Integer codigo, final String situacao, UsuarioVO usuario) throws Exception {
        try {

            final String sql = "UPDATE questionario set situacao=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

                    sqlAlterar.setString(1, situacao);
                    sqlAlterar.setInt(2, codigo);
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public void alterarOrdemPergunta(QuestionarioVO questionarioVO, PerguntaQuestionarioVO perguntaQuestionarioVO1, PerguntaQuestionarioVO perguntaQuestionarioVO2) throws Exception {
        int ordem1 = perguntaQuestionarioVO1.getOrdem();
        perguntaQuestionarioVO1.setOrdem(perguntaQuestionarioVO2.getOrdem());
        perguntaQuestionarioVO2.setOrdem(ordem1);        
        Ordenacao.ordenarLista(questionarioVO.getPerguntaQuestionarioVOs(), "ordem");
    }
    
    public Boolean verificarQuestionarioVinculoPlanoEnsino(Integer codigoQuestionario, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT distinct(questionario.codigo) from questionario ");
		sql.append(" inner join curso on curso.questionario = questionario.codigo ");
		sql.append(" inner join planoensino on planoensino.curso=curso.codigo ");
		sql.append(" where escopo = 'PE' and questionario.codigo = ").append(codigoQuestionario);		
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (tabelaResultado.next()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

	@Override
	public QuestionarioVO consultarQuestionarioInscricao(Integer codigoQuestionario, Integer codigoInscricao, boolean b,int nivelmontardadosDadosbasicos, UsuarioVO usuarioVO)throws Exception {
		 consultar(getIdEntidade(), false, usuarioVO);
		 StringBuilder sql = new StringBuilder();
			sql.append("  select q.* from questionario q  ");
			sql.append("  inner join procseletivo procs on procs.questionario  = q.codigo  ");
			sql.append("  left join inscricao insc on insc.procseletivo = procs.codigo  ");
			if(Uteis.isAtributoPreenchido(codigoInscricao) && codigoInscricao > 0 ) {
				sql.append("  and insc.codigo = "+codigoInscricao+"");
			}
			sql.append(" where q.codigo  = ? ");
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {codigoQuestionario});
	        if (!tabelaResultado.next()) {
	            throw new ConsistirException("Dados Não Encontrados ( Questionario ).");
	        }
	        return (montarDados(tabelaResultado, nivelmontardadosDadosbasicos, usuarioVO));
		
	}
	
	private boolean consultarExistenciaVinculoQuestionarioTipoRequerimentoTipoRequerimentoDepartamento(int questionario) throws Exception {
		StringBuilder sql = new StringBuilder()
				.append(" SELECT * FROM (SELECT tr.codigo FROM tiporequerimento tr WHERE tr.questionario = ")
				.append(questionario)
				.append(" UNION SELECT trd.codigo FROM tiporequerimentodepartamento trd WHERE trd.questionario = ")
				.append(questionario)
				.append(" ) AS t LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return tabelaResultado.next() && Uteis.isAtributoPreenchido(tabelaResultado.getInt("codigo"));
	}
}
