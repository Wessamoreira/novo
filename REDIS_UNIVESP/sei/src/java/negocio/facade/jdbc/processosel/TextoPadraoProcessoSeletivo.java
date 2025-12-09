package negocio.facade.jdbc.processosel;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.TextoPadraoLayoutVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.TextoPadraoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.GeradorHtmlParaPdf;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisHTML;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.TextoPadraoProcessoSeletivoInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.processosel.enumeradores.TipoRelatorioEstatisticoProcessoSeletivoEnum;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TextoPadraoProcessoSeletivoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TextoPadraoProcessoSeletivoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see TextoPadraoProcessoSeletivoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TextoPadraoProcessoSeletivo extends ControleAcesso implements TextoPadraoProcessoSeletivoInterfaceFacade {

    protected static String idEntidade;

    public TextoPadraoProcessoSeletivo() throws Exception {
        super();
        setIdEntidade("TextoPadraoProcessoSeletivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TextoPadraoProcessoSeletivoVO</code>.
     */
    public TextoPadraoProcessoSeletivoVO novo() throws Exception {
        incluir(getIdEntidade());
        TextoPadraoProcessoSeletivoVO obj = new TextoPadraoProcessoSeletivoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>TextoPadraoProcessoSeletivoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>TextoPadraoProcessoSeletivoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TextoPadraoProcessoSeletivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            TextoPadraoProcessoSeletivo.incluir(getIdEntidade(), true, usuario);
            TextoPadraoProcessoSeletivoVO.validarDados(obj);
            if(obj.getTipoDesigneTextoEnum().isPdf()){
				obj.getArquivoIreport().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoIreport(), false, usuario, configuracaoGeralSistemaVO);
			}
            final String sql = "INSERT INTO TextoPadraoProcessoSeletivo( descricao, dataDefinicao, responsavelDefinicao, texto,  unidadeEnsino, tipo, orientacaoDaPagina,  margemDireita, margemEsquerda, margemSuperior, margemInferior, tipodesignetextoenum, arquivoireport, assinardigitalmentetextopadrao, alinhamentoAssinaturaDigitalEnum, alturaassinatura, larguraassinatura  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getDescricao());
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataDefinicao()));
                    if (obj.getResponsavelDefinicao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getResponsavelDefinicao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setString(4, obj.getTexto());
                    sqlInserir.setInt(5, obj.getUnidadeEnsino().getCodigo());
                    sqlInserir.setString(6, obj.getTipo());
                    sqlInserir.setString(7, obj.getOrientacaoDaPagina());
                    sqlInserir.setString(8, obj.getMargemDireita());
                    sqlInserir.setString(9, obj.getMargemEsquerda());
                    sqlInserir.setString(10, obj.getMargemSuperior());
                    sqlInserir.setString(11, obj.getMargemInferior());
                    sqlInserir.setString(12, obj.getTipoDesigneTextoEnum().name());
					if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
						sqlInserir.setInt(13, obj.getArquivoIreport().getCodigo());
					}else{
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setBoolean(14, obj.getAssinarDigitalmenteTextoPadrao());
					sqlInserir.setString(15, obj.getAlinhamentoAssinaturaDigitalEnum().name());
					//sqlInserir.setString(16, obj.getCorAssinaturaDigitalmente());
					sqlInserir.setFloat(16, obj.getAlturaAssinatura());
					sqlInserir.setFloat(17, obj.getLarguraAssinatura());
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>TextoPadraoProcessoSeletivoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>TextoPadraoProcessoSeletivoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TextoPadraoProcessoSeletivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            TextoPadraoProcessoSeletivo.alterar(getIdEntidade(), true, usuario);
            TextoPadraoProcessoSeletivoVO.validarDados(obj);
            if(obj.getTipoDesigneTextoEnum().isPdf()){
				obj.getArquivoIreport().setValidarDados(false);
				getFacadeFactory().getArquivoFacade().persistir(obj.getArquivoIreport(), false, usuario, configuracaoGeralSistemaVO);
			}
            final String sql = "UPDATE TextoPadraoProcessoSeletivo set descricao=?, dataDefinicao=?, responsavelDefinicao=?, texto=?,  unidadeEnsino=?, tipo=?, orientacaoDaPagina=?, margemDireita = ?, margemEsquerda = ?, margemSuperior = ?, margemInferior = ?, tipodesignetextoenum = ?, arquivoireport = ?, assinardigitalmentetextopadrao=?, alinhamentoAssinaturaDigitalEnum=?, alturaassinatura=?, larguraassinatura=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getDescricao());
                    sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataDefinicao()));
                    if (obj.getResponsavelDefinicao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getResponsavelDefinicao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    sqlAlterar.setString(4, obj.getTexto());
                    sqlAlterar.setInt(5, obj.getUnidadeEnsino().getCodigo().intValue());
                    sqlAlterar.setString(6, obj.getTipo());
                    sqlAlterar.setString(7, obj.getOrientacaoDaPagina());
                    sqlAlterar.setString(8, obj.getMargemDireita());
                    sqlAlterar.setString(9, obj.getMargemEsquerda());
                    sqlAlterar.setString(10, obj.getMargemSuperior());
                    sqlAlterar.setString(11, obj.getMargemInferior());
                    sqlAlterar.setString(12, obj.getTipoDesigneTextoEnum().name());
					if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
						sqlAlterar.setInt(13, obj.getArquivoIreport().getCodigo());
					}else{
						sqlAlterar.setNull(13, 0);
					}
					sqlAlterar.setBoolean(14, obj.getAssinarDigitalmenteTextoPadrao());
					sqlAlterar.setString(15, obj.getAlinhamentoAssinaturaDigitalEnum().name());
//					sqlAlterar.setString(16, obj.getCorAssinaturaDigitalmente());
					sqlAlterar.setFloat(16, obj.getAlturaAssinatura());
					sqlAlterar.setFloat(17, obj.getLarguraAssinatura());
                    sqlAlterar.setInt(18, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            //new TextoPadraoProcessoSeletivoFuncionario().alterarTextoPadraoProcessoSeletivoFuncionario(obj, obj.getTextoPadraoProcessoSeletivofuncionarioVOs());            
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>TextoPadraoProcessoSeletivoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>TextoPadraoProcessoSeletivoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TextoPadraoProcessoSeletivoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
        try {
            TextoPadraoProcessoSeletivo.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM TextoPadraoProcessoSeletivo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
				getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(obj.getArquivoIreport(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoIreport().getPastaBaseArquivo() );
				getFacadeFactory().getArquivoFacade().excluir(obj.getArquivoIreport(), usuario, configuracaoGeralSistemaVO);
			}
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>situacao</code> da classe <code>Colaborador</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoProcessoSeletivoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorResponsavelDefinicao(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorResponsavelDefinicao(valorConsulta, 0, nivelMontarDados, usuario);
    }

    public List consultarPorResponsavelDefinicao(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT TextoPadraoProcessoSeletivo.* FROM TextoPadrao, Usuario WHERE TextoPadraoProcessoSeletivo.responsavelDefinicao = Usuario.codigo and upper( Usuario.nome ) like('" + valorConsulta.toUpperCase() + "%') ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (TextoPadraoProcessoSeletivo.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoProcessoSeletivo.unidadeEnsino is null) ";
        }
        sqlStr += " ORDER BY Usuario.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>Date dataDefinicao</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoProcessoSeletivoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataDefinicao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorDataDefinicao(prmIni, prmFim, 0, controlarAcesso, nivelMontarDados, usuario);
    }

    public List consultarPorDataDefinicao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoProcessoSeletivo WHERE ((dataDefinicao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataDefinicao <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (TextoPadraoProcessoSeletivo.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoProcessoSeletivo.unidadeEnsino is null) ";
        }
        sqlStr += " ORDER BY dataDefinicao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoProcessoSeletivoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorDescricao(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuario);
    }

    public List consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoProcessoSeletivo WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (unidadeEnsino = " + unidadeEnsino + " or unidadeEnsino is null or unidadeEnsino = 0) ";
        }
        sqlStr += "ORDER BY descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<TextoPadraoProcessoSeletivoVO> consultarPorTipo(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT * FROM TextoPadraoProcessoSeletivo WHERE tipo = '" + valorConsulta + "' ";
    	if (unidadeEnsino != 0) {
    		sqlStr += "AND (unidadeEnsino = " + unidadeEnsino + " or unidadeEnsino is null or unidadeEnsino = 0) ";
    	}
    	if (!nivelEducacional.equals("")) {
    		sqlStr += "AND (nivelEducacional = '"+nivelEducacional+"' OR nivelEducacional is null OR nivelEducacional = '') ";
    	}
    	sqlStr += "ORDER BY descricao";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoProcessoSeletivoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return consultarPorCodigo(valorConsulta,0, controlarAcesso,nivelMontarDados, usuario);
    }

    public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TextoPadraoProcessoSeletivo WHERE codigo >= " + valorConsulta.intValue() + " ";
        if (unidadeEnsino != 0) {
            sqlStr += "AND (TextoPadraoProcessoSeletivo.unidadeEnsino = " + unidadeEnsino + " or TextoPadraoProcessoSeletivo.unidadeEnsino is null) ";
        }
        sqlStr += " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>TextoPadrao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoProcessoSeletivoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT DISTINCT (TextoPadraoProcessoSeletivo.*) FROM TextoPadraoProcessoSeletivo ");
        sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.contratoMatricula = TextoPadraoProcessoSeletivo.codigo");
        sqlStr.append(" OR matriculaPeriodo.contratoFiador = TextoPadraoProcessoSeletivo.codigo");
        sqlStr.append(" WHERE matriculaPeriodo.matricula = '" + valorConsulta.toUpperCase() + "' ;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>TextoPadraoProcessoSeletivoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>TextoPadraoProcessoSeletivoVO</code>.
     * @return  O objeto da classe <code>TextoPadraoProcessoSeletivoVO</code> com os dados devidamente montados.
     */
    public static TextoPadraoProcessoSeletivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ////System.out.println(">> Montar dados(TextoPadrao) - " + new Date());
        TextoPadraoProcessoSeletivoVO obj = new TextoPadraoProcessoSeletivoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setDataDefinicao(dadosSQL.getDate("dataDefinicao"));
        obj.getResponsavelDefinicao().setCodigo(new Integer(dadosSQL.getInt("responsavelDefinicao")));
        obj.setTexto(dadosSQL.getString("texto"));
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setTipo(dadosSQL.getString("tipo"));
        obj.setOrientacaoDaPagina(dadosSQL.getString("orientacaoDaPagina"));
        obj.setMargemDireita(dadosSQL.getString("margemDireita"));
		obj.setMargemEsquerda(dadosSQL.getString("margemEsquerda"));
		obj.setMargemSuperior(dadosSQL.getString("margemSuperior"));
		obj.setMargemInferior(dadosSQL.getString("margemInferior"));
		obj.setAssinarDigitalmenteTextoPadrao(dadosSQL.getBoolean("assinardigitalmentetextopadrao"));
		obj.setAlinhamentoAssinaturaDigitalEnum(AlinhamentoAssinaturaDigitalEnum.valueOf(dadosSQL.getString("alinhamentoAssinaturaDigitalEnum")));
		obj.setAlturaAssinatura(dadosSQL.getFloat("alturaAssinatura"));
		obj.setLarguraAssinatura(dadosSQL.getFloat("larguraAssinatura"));
		obj.setTipoDesigneTextoEnum(TipoDesigneTextoEnum.valueOf(dadosSQL.getString("tipodesignetextoenum")));
		if(obj.getTipoDesigneTextoEnum().isPdf()){
			obj.setArquivoIreport(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivoireport"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosResponsavelDefinicao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        //obj.setTextoPadraoProcessoSeletivofuncionarioVOs(TextoPadraoProcessoSeletivoFuncionario.consultarTextoPadraoProcessoSeletivoFuncionario(obj.getCodigo(), false, usuario));
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>ColaboradorVO</code> relacionado ao objeto <code>TextoPadraoProcessoSeletivoVO</code>.
     * Faz uso da chave primária da classe <code>ColaboradorVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelDefinicao(TextoPadraoProcessoSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelDefinicao().getCodigo().intValue() == 0) {
            obj.setResponsavelDefinicao(new UsuarioVO());
            return;
        }
        obj.setResponsavelDefinicao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDefinicao().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosUnidadeEnsino(TextoPadraoProcessoSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>TextoPadraoProcessoSeletivoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public TextoPadraoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TextoPadraoProcessoSeletivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public String consultaTextoDoTextoPadraoPorChavePrimaria(Integer codigoPrm,  UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT texto FROM TextoPadraoProcessoSeletivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado.getString("texto");
    }
    
    public String removerBordaDaPagina(String texto) throws Exception {
    	try {
    		texto = texto.replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "");
    		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 237mm; padding: 1cm;", "");
    		
    		texto = texto.replaceAll("width: 297mm; min-height: 21cm; padding: 2cm; margin: 1cm auto;", "");
    		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 150mm; padding: 1cm;", "");
    		
    		texto = texto.replaceAll("padding: 2cm; width: 21cm; margin: 1cm auto; min-height: 29.7cm;", "");
    		texto = texto.replaceAll("border: 1px #CCCCCC dashed; height: 190mm; padding-left: 1cm; padding-right: 1cm;", "");
    		
		} catch (Exception e) {
			throw e;
		}
    	return texto;
    }
    
    public String adicionarStyleFormatoPaginaTextoPadrao(String texto, String orientacaoDaPagina) throws Exception {
    	try {
    		StringBuilder sb = new StringBuilder();
			if (!texto.contains("<style type='text/css'>")) {
				if (orientacaoDaPagina.equals("RE")) {
			        sb.append("<head>");
			        sb.append("<style type='text/css'>");
			        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
			        sb.append(" th { font-weight: normal; } ");
			        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
			        sb.append(" .page { width: 21cm; min-height: 29.7cm; padding: 2cm; margin: 1cm auto; } ");
			        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 237mm; } ");
			        sb.append(" @page { size: A4; margin: 0; } ");
			        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
			        sb.append("</style>");
			        
					texto = texto.replace("<head>", sb.toString());
				} else {
			        sb.append("<head>");
			        sb.append("<style type='text/css'>");
			        sb.append(" body { margin: 0; padding: 0; font-size:11px; } ");
			        sb.append(" th { font-weight: normal; } ");
			        sb.append(" * { box-sizing: border-box; -moz-box-sizing: border-box; } ");
			        sb.append(" .page { width: 29.7cm; min-height: 21cm; padding: 2cm; margin: 1cm auto; } ");
			        sb.append(" .subpage { padding-top: 1cm; padding-bottom: 1cm; padding-left: 1cm; padding-right: 1cm; height: 150mm; } ");
			        sb.append(" @page { size: A4 landscape; margin: 0; } ");
			        sb.append(" @media print { .page { margin: 0; border: initial; border-radius: initial; width: initial; min-height: initial; box-shadow: initial; background: initial; page-break-after: always; } } ");
			        sb.append("</style>");
			        
					texto = texto.replace("<head>", sb.toString());
				}
			}
		} catch (Exception e) {
			throw e;
		}
    	
    	return texto;
    }
    
	public String substituirValorAtribuidoClass(String texto, String classe, String valor) throws Exception {
		String parte1, parte2, novaClasse  = "";
		try {
			parte1 = texto.substring(texto.indexOf(classe), texto.length());
			parte2 = parte1.substring(0, parte1.indexOf("}") + 1);
			novaClasse = classe + " { " + valor + " } ";			
		} catch (Exception e) {
			throw e;
		}
		
		return texto.replace(parte2, novaClasse);
	}
	
	
	
	
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return TextoPadraoProcessoSeletivo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        TextoPadraoProcessoSeletivo.idEntidade = idEntidade;
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String imprimirTextoPadrao(TextoPadraoProcessoSeletivoVO texto, InscricaoVO inscricao, List<InscricaoVO> listaImpressao, String tipo, Boolean PDF, TipoRelatorioEstatisticoProcessoSeletivoEnum tipoRelatorio, ConfiguracaoGeralSistemaVO configGeralSistema, String versaoSoftware, UsuarioVO usuarioLogado) throws Exception {
		String nomeArquivoOrigem = null;
		try {
			Boolean imprimirContrato = false;
			if (!texto.getTipo().equals("LI")) {
				getFacadeFactory().getInscricaoFacade().carregarDados(inscricao, usuarioLogado);
			}
			String textoStr = "";
			texto.substituirTag(texto.getTexto(), inscricao, listaImpressao, tipoRelatorio);
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().removerBordaDaPagina(texto.getTexto());
			textoStr = getFacadeFactory().getTextoPadraoDeclaracaoFacade().adicionarStyleFormatoPaginaTextoPadrao(textoStr, texto.getOrientacaoDaPagina());

			if (texto.getTipoDesigneTextoEnum().equals(TipoDesigneTextoEnum.HTML)) {
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoStr);
				nomeArquivoOrigem = "DECLARACAO_" + Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioLogado.getCodigo() + ".html";
			} else {
				nomeArquivoOrigem = executarValidacaoImpressaoEmPdf( texto, textoStr, true, configGeralSistema, versaoSoftware, usuarioLogado);
			}
		} catch (Exception e) {
			throw e;
		}
		return nomeArquivoOrigem;
	}
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public String executarValidacaoImpressaoEmPdf(TextoPadraoLayoutVO texto, String textoStr, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO configGeralSistema, String versaoSoftware, UsuarioVO usuarioLogado) throws Exception {
		if (texto.getTipoDesigneTextoEnum().isHtml()) {
			return executarConversaoHtmlParaPdf(textoStr, texto, configGeralSistema, usuarioLogado);
		} else {
			return executarImpressaoPorTextoPadraoIreport(texto, persistirDocumentoAssinado, configGeralSistema, versaoSoftware, usuarioLogado);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public String executarConversaoHtmlParaPdf(String htmlRelatorio, TextoPadraoLayoutVO textoPadrao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		String nomeArquivoOrigem = "DECLARACAO_" + Uteis.getData(new Date(), "dd_MM_yy_HH_mm_ss") + "_" + usuarioVO.getCodigo() + ".pdf";
		String arquivoOrigem = UteisJSF.getCaminhoWeb() + "relatorio/" + nomeArquivoOrigem;
		if (Uteis.isSistemaOperacionalWindows() && arquivoOrigem.startsWith("/")) {
			arquivoOrigem = arquivoOrigem.substring(1, arquivoOrigem.length());
		}
		gerarHtmlParaPdf(htmlRelatorio, arquivoOrigem, textoPadrao, config, usuarioVO);
		return nomeArquivoOrigem;
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	private void gerarHtmlParaPdf(String htmlRelatorio, String arquivoOrigem, TextoPadraoLayoutVO textoPadrao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception {
		GeradorHtmlParaPdf geradorHtmlParaPdf = new GeradorHtmlParaPdf("", "", htmlRelatorio);
		geradorHtmlParaPdf.setPageSize(UteisHTML.getOrientacaoPagina(textoPadrao.getOrientacaoDaPaginaEnum()));
		geradorHtmlParaPdf.setMarginBottom(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemInferior().replaceAll(",", "."))));
		geradorHtmlParaPdf.setMarginTop(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemSuperior().replaceAll(",", "."))));
		geradorHtmlParaPdf.setMarginRight(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemDireita().replaceAll(",", "."))));
		geradorHtmlParaPdf.setMarginLeft(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf(textoPadrao.getMargemEsquerda().replaceAll(",", "."))));
		geradorHtmlParaPdf.setSizeTopo(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf("0")));
		geradorHtmlParaPdf.setSizeRodape(UteisTextoPadrao.converterCentimetroParaPontos(Float.valueOf("0")));
		geradorHtmlParaPdf.realizarGeracaoDocumentoPdf(arquivoOrigem);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private String executarImpressaoPorTextoPadraoIreport(TextoPadraoLayoutVO textoLayout, boolean persistirDocumentoAssinado, ConfiguracaoGeralSistemaVO config, String versaoSoftware, UsuarioVO usuario) throws Exception {
		GeradorRelatorio geradorRelatorio = null;
		SuperParametroRelVO superParamentro = null;
		String caminhoRelatorio = null;
		try {
			if (!Uteis.isAtributoPreenchido(textoLayout.getArquivoIreport().getDescricao())) {
				throw new ConsistirException("O campo ARQUIVO IREPORT (Texto Padrao) deve ser informado.");
			}
			superParamentro = new SuperParametroRelVO();
			superParamentro.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			superParamentro.setNomeDesignIreport(config.getLocalUploadArquivoFixo() + File.separator + textoLayout.getArquivoIreport().getPastaBaseArquivo() + File.separator + textoLayout.getArquivoIreport().getNome());
			superParamentro.setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
			superParamentro.setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "textoPadrao" + File.separator);
			superParamentro.setNomeUsuario(usuario.getNome());
			superParamentro.setVersaoSoftware(versaoSoftware);
//			superParamentro.setNomeEspecificoRelatorio(impressaoContratoVO.getMatriculaVO().getMatricula());
			superParamentro.getParametros().putAll(textoLayout.getParametrosRel());
			geradorRelatorio = new GeradorRelatorio();
			caminhoRelatorio = geradorRelatorio.realizarExportacaoRelatorio(superParamentro);
//			if (textoLayout.getAssinarDigitalmenteTextoPadrao() || impressaoContratoVO.getRequerimentoVO().getTipoRequerimento().getAssinarDigitalmenteDeclaracoesGeradasNoRequerimento()) {
//				caminhoRelatorio = executarAssinaturaParaImpressaoContrato(caminhoRelatorio, textoLayout, impressaoContratoVO, persistirDocumentoAssinado, config, usuario);
//			}
			return caminhoRelatorio;
		} finally {
			geradorRelatorio = null;
			superParamentro = null;
			caminhoRelatorio = null;
		}
	}	
}
