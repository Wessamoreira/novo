package negocio.facade.jdbc.contabil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.PlanoContaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PlanoContaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PlanoContaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PlanoContaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class PlanoConta extends ControleAcesso implements PlanoContaInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1245169901482388877L;
	protected static String idEntidade;

    public PlanoConta() throws Exception {
        super();
        setIdEntidade("PlanoConta");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>PlanoContaVO</code>.
     */
    public PlanoContaVO novo() throws Exception {
        incluir(getIdEntidade());
        PlanoContaVO obj = new PlanoContaVO();
        return obj;
    }

    public void gerarIdentificadorMascaraPlanoConta2(PlanoContaVO obj,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        String m = "";
        String mascara = "";
        String mascaraConsulta = "";        
        int i = 0;
        int nivel = 1;
        mascara = configuracaoFinanceiroVO.getMascaraPlanoConta();
        int nrNiveis = configuracaoFinanceiroVO.getNrNiveisPlanoConta().intValue();
        int nrNivelUnidOrg = obterNrNivelPlanoConta(obj,usuario);

        if (nrNivelUnidOrg > nrNiveis) {
            throw new Exception("Não é possível CADASTRAR este PLANO DE CONTA DE NÍVEL " + nrNivelUnidOrg + ". Número de níveis permitidos: " + nrNiveis);
        }
        // VERIFICA SE MASCARA A SER GERADA SERÁ PRIMEIRO NÍVEL OU NÃO.
        if (obj.verificarPlanoContaPrimeiroNivel()) {
            nivel = 1;
            mascaraConsulta = mascara;
            int posicaoPonto = mascaraConsulta.indexOf('.');
            mascaraConsulta = mascaraConsulta.substring(0, posicaoPonto);
        } else {
            PlanoContaVO unVO = getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaPrincipal().getCodigo(), obj.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
            m = unVO.getIdentificadorPlanoConta();
            while (i < m.length()) {
                if (m.charAt(i) == '.') {
                    nivel++;
                }
                i++;
            }
            nivel++;
            mascaraConsulta = m;
            int quantidadePonto = 0;
            int q = 0;
            // recupera mascara para consuta....
            // while (quantidadePonto <= nrPonto) {
            mascara += ".";
            while (quantidadePonto < nivel) {
                if (mascara.length() != q) {
                    if (mascara.charAt(q) == '.') {
                        mascaraConsulta = mascara.substring(0, q);
                        quantidadePonto++;
                    }
                } else {
                    quantidadePonto = nivel;
                }
                q++;
            }
        }

        List<PlanoContaVO> l = consultaUltimaMascaraGerada(mascaraConsulta, m, obj.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario);
        String masc = "";
        String mascaraParcial = "";
        if (l.size() != 0) {
            PlanoContaVO unidadeOrganizacionalVO = (PlanoContaVO) l.get(l.size() - 1);
            int o = unidadeOrganizacionalVO.getIdentificadorPlanoConta().length();
            while (o > 0) {
                if (unidadeOrganizacionalVO.getIdentificadorPlanoConta().charAt(o - 1) == '.') {
                    masc = unidadeOrganizacionalVO.getIdentificadorPlanoConta().substring(o, unidadeOrganizacionalVO.getIdentificadorPlanoConta().length());
                    mascaraParcial = unidadeOrganizacionalVO.getIdentificadorPlanoConta().substring(0, o - 1);
                    o = 0;
                } else {
                    masc = unidadeOrganizacionalVO.getIdentificadorPlanoConta().substring(0, unidadeOrganizacionalVO.getIdentificadorPlanoConta().length());
                }
                o--;
            }
            int w = 0;
            int cont2 = 0;
            String zeros = "";
            String numeros = "";
            while (w < masc.length()) {
                while (masc.charAt(w) == '0' && cont2 != masc.length()) {
                    zeros += masc.charAt(w);
                    w++;
                    cont2++;
                }
                numeros += masc.substring(w, masc.length());
                w = masc.length();
            }
            if (mascaraParcial.equals("")) {
                obj.setIdentificadorPlanoConta(zeros + new Integer(new Double(numeros).intValue() + 1).toString());
            } else {
                obj.setIdentificadorPlanoConta(mascaraParcial + "." + zeros + new Integer(new Double(numeros).intValue() + 1).toString());
            }
        } else {
            if (mascaraConsulta.length() != 0) {
                int o = 0;
                if (mascaraConsulta.lastIndexOf('.') == -1) {
                    o = mascaraConsulta.length();
                } else {
                    int posicaoUltimoPonto = mascaraConsulta.lastIndexOf('.');
                    String v = mascaraConsulta.substring(posicaoUltimoPonto + 1);
                    o = v.length();
                }
                String a = "";
                int r = 0;
                while (r < o) {
                    if (r == (o - 1)) {
                        a += "1";
                    } else {
                        a += "0";
                    }
                    r++;
                }
                if (m.equals("")) {
                    obj.setIdentificadorPlanoConta(a);
                } else {
                    obj.setIdentificadorPlanoConta(m + "." + a);
                }

            } else {
                int o = 0;
                int p = 0;
                String a = "";
                while (o < mascaraConsulta.length()) {
                    if (mascaraConsulta.charAt(o) == '.') {
                        o = mascaraConsulta.length();
                    } else {
                        p++;
                    }
                    o--;
                }
                int r = 0;
                while (r < p) {
                    if (r == (p - 1)) {
                        a += "1";
                    } else {
                        a += "0";
                    }
                    r++;
                }
                obj.setIdentificadorPlanoConta(a);
            }
        }
    }

    public int obterNrNivelPlanoConta(PlanoContaVO obj,UsuarioVO usuario) throws Exception {
        if (obj.verificarPlanoContaPrimeiroNivel()) {
            return 1;
        }
        PlanoContaVO principal = getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getPlanoContaPrincipal().getCodigo(), null, 0,usuario);
        return (principal.getNivelPlanoConta() + 1);
    }

    public void obterNivelPlanoConta(PlanoContaVO obj) {
        String identificador = obj.getIdentificadorPlanoConta();
        int cont = identificador.length();
        int contador = 0;
        int nivel = 0;
        String var = "";
        while (contador < cont) {
            var = identificador.substring(contador, contador + 1);
            if (var.equals(".")) {
                nivel++;
            }
            contador++;
        }
        obj.setNivelPlanoConta(new Integer(nivel));
    }
    
    /**
     * Operação responsável por validar os dados de um objeto da classe <code>PlanoContaVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos
     * obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o
     *                erro ocorrido.
     */
    public void validarDados(PlanoContaVO obj) throws ConsistirException {
		if ((obj.getIdentificadorPlanoConta() == null) || (obj.getIdentificadorPlanoConta().equals(""))) {
			throw new ConsistirException("O campo IDENTIFICADOR DO PLANO DE CONTAS (Plano de Contas) deve ser informado.");
		}
		if ((obj.getNivelPlanoConta() == null) || (obj.getNivelPlanoConta().equals(0))) {
			throw new ConsistirException("O campo NIVEL DO PLANO DE CONTAS (Plano de Contas) deve ser informado.");
		}
        if ((obj.getDescricao() == null) || (obj.getDescricao().equals(""))) {
            throw new ConsistirException("O campo DESCRIÇÃO (Plano de Contas) deve ser informado.");
        }
        
        if ((obj.getPlanoContaPrincipal() != null) && (obj.getPlanoContaPrincipal().getCodigo().intValue() != 0)) {            
        	if(obj.getNivelPlanoConta() <= obj.getPlanoContaPrincipal().getNivelPlanoConta()){
        		throw new ConsistirException("O campo NIVEL PLANO CONTA (Plano de Contas) deve ser maior que o NÍVEL DO PLANO CONTA PRINCIPAL .");
        	}
        }
        //validarUnicidade(obj);
    }
    
    public void validarUnicidade(PlanoContaVO obj) throws ConsistirException{
    	if(getConexao().getJdbcTemplate().queryForRowSet("select codigo from planoconta where trim(upper(sem_acentos(identificadorPlanoConta))) = trim(upper(sem_acentos( ? ))) and codigo != ? ", obj.getIdentificadorPlanoConta(), obj.getCodigo()).next()){
    		throw new ConsistirException("Já existe outro PLANO DE CONTA com o mesmo IDENTIFICADOR.");
    	}
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>PlanoContaVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoContaVO</code> que será gravado no
     *            banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final PlanoContaVO obj,UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        try {
            PlanoConta.incluir(getIdEntidade(), true, usuario);
            validarDados(obj);
            final String sql = "INSERT INTO PlanoConta( unidadeEnsino, planoContaPrincipal, identificadorPlanoConta, descricao, nivelPlanoConta, codigoPatrimonial, codigoreduzido) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if(Uteis.isAtributoPreenchido(obj.getPlanoContaPrincipal())){
                    	sqlInserir.setInt(2, obj.getPlanoContaPrincipal().getCodigo().intValue());
                    }else{
                    	sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setString(3, obj.getIdentificadorPlanoConta());
                    sqlInserir.setString(4, obj.getDescricao());
                    sqlInserir.setInt(5, obj.getNivelPlanoConta().intValue());                    
                    sqlInserir.setString(6, obj.getCodigoPatrimonial());
                    if(obj.getCodigoReduzido() == null) {
                    	sqlInserir.setNull(7, 0);
                    }else {
                    	sqlInserir.setInt(7, obj.getCodigoReduzido());
                    }
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
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>PlanoContaVO</code>. Sempre utiliza a chave primária da classe como
     * atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoContaVO</code> que será alterada
     *            no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final PlanoContaVO obj, UsuarioVO usuarioVO) throws Exception {

        try {
            PlanoConta.alterar(getIdEntidade(), true, usuarioVO);
            validarDados(obj);
            final String sql = "UPDATE PlanoConta set unidadeEnsino=?, planoContaPrincipal=?, identificadorPlanoConta=?, descricao=?, nivelPlanoConta=?, codigoPatrimonial=?, codigoreduzido = ? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if(Uteis.isAtributoPreenchido(obj.getPlanoContaPrincipal().getCodigo())){
                    	sqlAlterar.setInt(2, obj.getPlanoContaPrincipal().getCodigo().intValue());
                    }else{
                    	sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setString(3, obj.getIdentificadorPlanoConta());
                    sqlAlterar.setString(4, obj.getDescricao());
                    sqlAlterar.setInt(5, obj.getNivelPlanoConta().intValue());                    
                    sqlAlterar.setString(6, obj.getCodigoPatrimonial());
                    if(obj.getCodigoReduzido() == null) {
                    	sqlAlterar.setNull(7, 0);
                    }else {
                    	sqlAlterar.setInt(7, obj.getCodigoReduzido());
                    }
                    sqlAlterar.setInt(8, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    public List<PlanoContaVO> consultar(Integer unidadeEnsino, String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
            List<PlanoContaVO> objs = new ArrayList<PlanoContaVO>();
            if (campoConsulta.equals("codigo")) {
                if (valorConsulta.equals("")) {
                    valorConsulta = "0";
                }
                int valorInt = Integer.parseInt(valorConsulta);
                objs = consultarPorCodigo(new Integer(valorInt), unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS,usuario);
            }
            if (campoConsulta.equals("planoContaPrincipal")) {
               
                objs = consultarPorPlanoContaPrincipal(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS,usuario);
            }
            if (campoConsulta.equals("identificadorPlanoConta")) {
                objs = consultarPorIdentificadorPlanoConta(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS,usuario);
            }
            if (campoConsulta.equals("descricao")) {
                objs = consultarPorDescricao(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS,usuario);
            }
            if (campoConsulta.equals("codigoReduzido")) {
            	objs = consultarPorCodigoReduzido(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS,usuario);
            }
            if (campoConsulta.equals("unidadeEnsino")) {
                objs = consultarPorUnidadeEnsino(valorConsulta, unidadeEnsino, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS,usuario);
            }
            return objs;

    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>PlanoContaVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoContaVO</code> que será removido
     *            no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(PlanoContaVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            PlanoConta.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM PlanoConta WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public List<PlanoContaVO> consultaUltimaMascaraGerada(String mascaraConsulta, String mascaraPlanoContaPrincipal, Integer unidadeEnsino, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = getSqlConsultaCompleta();
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sqlStr.append(" where char_length(planoConta.identificadorPlanoConta) = " + mascaraConsulta.length() + " and Lower(planoConta.identificadorPlanoConta) like('" + mascaraPlanoContaPrincipal.toLowerCase() + "%') order by planoConta.identificadorPlanoConta");
        } else {
        	sqlStr.append(" where char_length(planoConta.identificadorPlanoConta) = " + mascaraConsulta.length() + " and Lower(planoConta.identificadorPlanoConta) like('" + mascaraPlanoContaPrincipal.toLowerCase() + "%') AND (unidadeEnsino = " + unidadeEnsino + " or unidadeEnsino is null ) order by planoConta.identificadorPlanoConta");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuario));
    }


    public List<PlanoContaVO> consultarPorUnidadeEnsino(String descricao, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso,usuario);
        StringBuilder sqlStr = getSqlConsultaCompleta();
        sqlStr.append(" WHERE lower(sem_acentos(unidadeEnsino.nome) ) like (lower(sem_acentos(?))) ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {           
        	sqlStr.append(" AND (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null ) ");
        }       
        sqlStr.append(" ORDER BY unidadeEnsino.nome, planoconta.identificadorplanoconta ");       
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), descricao+"%");        
        return (montarDadosConsulta(tabelaResultado, usuario));

    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoConta</code> através
     * do valor do atributo <code>String descricao</code>. Retorna os objetos,
     * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PlanoContaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoContaVO> consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso,usuario);
        StringBuilder sqlStr = getSqlConsultaCompleta();
        sqlStr.append(" WHERE lower(sem_acentos(planoconta.descricao)) like (lower(sem_acentos(?))) ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {           
        	sqlStr.append(" AND (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null ) ");
        }       
        sqlStr.append(" ORDER BY planoconta.descricao ");        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, usuario));
    }
    
    @Override
    public List<PlanoContaVO> consultarPorCodigoReduzido(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso,usuario);
    	StringBuilder sqlStr = getSqlConsultaCompleta();
    	sqlStr.append(" WHERE lower(sem_acentos(planoconta.codigoreduzido)) like (lower(sem_acentos(?))) ");
    	if (Uteis.isAtributoPreenchido(unidadeEnsino)) {           
    		sqlStr.append(" AND (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null ) ");
    	}       
    	sqlStr.append(" ORDER BY planoconta.descricao ");        
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
    	return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List<PlanoContaVO> consultarPorDescricaoOrdenarIdentrificador(String valorConsulta, Integer unidadeEnsino, Integer codigoIgnorar, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso,usuario);
        StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta()); 
        sqlStr.append(" 	WHERE lower(sem_acentos(planoconta.descricao)) like(lower(sem_acentos(?))) ");
        if (unidadeEnsino != null && unidadeEnsino != 0) {            
            sqlStr.append(" and PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null ");
        }
        if (codigoIgnorar != null && codigoIgnorar != 0) {        	
        	sqlStr.append(" and PlanoConta.codigo not in ( ");        	
        	sqlStr.append(" WITH RECURSIVE  planosuperior ( ");
        	sqlStr.append(" codigo, unidadeEnsino, planoContaPrincipal, identificadorPlanoConta, descricao, ");
        	sqlStr.append(" nivelPlanoConta, tipoPlanoConta, ");
        	sqlStr.append(" identificadorsuperior, ");
        	sqlStr.append(" descricaosuperior, ");
        	sqlStr.append(" nivelsuperior,  ");
        	sqlStr.append(" tiposuperior,  ");
        	sqlStr.append(" unidadesuperior,  ");
        	sqlStr.append(" planosuperior,  ");
        	sqlStr.append(" unidadeensino_nome ) as ( ");
        	sqlStr.append(getSqlConsultaCompleta());
        	sqlStr.append(" where planoconta.codigo = ").append(codigoIgnorar);
        	sqlStr.append(" union ");
        	sqlStr.append(getSqlConsultaCompleta());
        	sqlStr.append(" inner join planosuperior on planoconta.planocontaprincipal = planosuperior.codigo ");
        	sqlStr.append(" ) select codigo from planosuperior order by case when planosuperior.planocontaprincipal is null then 0 else planosuperior.planocontaprincipal end ");
        	sqlStr.append(" ) ");
        }
        sqlStr.append(" ORDER BY planoconta.identificadorplanoconta ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List<PlanoContaVO> consultarPorIdentificadorPlanoConta(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        StringBuilder sqlStr = getSqlConsultaCompleta();
        sqlStr.append(" WHERE lower(sem_acentos(planoconta.identificadorPlanoConta) ) like (lower(sem_acentos(?))) ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {           
        	sqlStr.append(" AND (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null ) ");
        }        
        sqlStr.append(" ORDER BY planoconta.identificadorPlanoConta ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, usuario));

    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoConta</code> através
     * do valor do atributo <code>Integer planoContaPrincipal</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PlanoContaVO</code>
     *         resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<PlanoContaVO> consultarPorPlanoContaPrincipal(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        StringBuilder sqlStr = getSqlConsultaCompleta();
        sqlStr.append(" WHERE (planoContaPrincipal.identificadorPlanoConta ilike (?) or upper(sem_acentos(planoContaPrincipal.descricao)) ilike upper(sem_acentos(?)) ) ");
        if (unidadeEnsino != 0) {
            sqlStr.append(" AND (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null )");
        }        
        sqlStr.append(" ORDER BY planoContaPrincipal.identificadorPlanoConta ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%", valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, usuario));
    }

    public List<PlanoContaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        StringBuilder sql = getSqlConsultaCompleta();
        sql.append(" where planoconta.codigo >= ? ");
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            sql.append(" and  (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null )");
        }         
        
        sql.append(" order by planoconta.identificadorplanoconta ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
        return (montarDadosConsulta(tabelaResultado, usuario));

    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe <code>PlanoContaVO</code>
     *         resultantes da consulta.
     */
    public static List<PlanoContaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<PlanoContaVO> vetResultado = new ArrayList<PlanoContaVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>PlanoContaVO</code>.
     *
     * @return O objeto da classe <code>PlanoContaVO</code> com os dados
     *         devidamente montados.
     */
    public static PlanoContaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        PlanoContaVO obj = new PlanoContaVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
        obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino_nome"));
        obj.getPlanoContaPrincipal().setCodigo(new Integer(dadosSQL.getInt("planoContaPrincipal")));
        obj.setIdentificadorPlanoConta(dadosSQL.getString("identificadorPlanoConta"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setCodigoReduzido(dadosSQL.getInt("codigoreduzido"));
        obj.setCodigoPatrimonial(dadosSQL.getString("codigoPatrimonial"));
        obj.setNivelPlanoConta(dadosSQL.getInt("nivelPlanoConta"));
        obj.getPlanoContaPrincipal().setIdentificadorPlanoConta(dadosSQL.getString("identificadorsuperior"));
        obj.getPlanoContaPrincipal().setDescricao(dadosSQL.getString("descricaosuperior"));
        obj.getPlanoContaPrincipal().setCodigoReduzido(dadosSQL.getInt("codigoreduzidosuperior"));
        obj.getPlanoContaPrincipal().setNivelPlanoConta(dadosSQL.getInt("nivelsuperior"));
        obj.getPlanoContaPrincipal().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadesuperior"));
        obj.getPlanoContaPrincipal().getPlanoContaPrincipal().setCodigo(dadosSQL.getInt("planosuperior"));
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe
     * <code>UnidadeEnsinoVO</code> relacionado ao objeto
     * <code>PlanoContaVO</code>. Faz uso da chave primária da classe
     * <code>UnidadeEnsinoVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosUnidadeEnsino(PlanoContaVO obj,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX,usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>PlanoContaVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public PlanoContaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        return consultarPorChavePrimaria(codigoPrm, null, nivelMontarDados,usuario);
    }

    public PlanoContaVO consultarPorChavePrimaria(Integer codigoPrm, Integer unidadeEnsino, int nivelMontarDados,UsuarioVO usuario) throws Exception {        
        StringBuilder sql = getSqlConsultaCompleta();
        sql.append(" where planoconta.codigo = ? ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sql.append(" and  (PlanoConta.unidadeEnsino = ").append(unidadeEnsino).append(" or PlanoConta.unidadeEnsino is null )");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PlanoConta ).");
        }
        return montarDados(tabelaResultado, usuario);
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>PlanoContaVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public PlanoContaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
       
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(getSqlConsultaCompleta().append(" where planoconta.codigo = ? ").toString(), codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( PlanoConta ).");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return PlanoConta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        PlanoConta.idEntidade = idEntidade;
    }

	@Override
	public List<PlanoContaVO> consultarPorPlanoContaPrincipal(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, Integer nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlConsultaCompleta();
		sql.append(" WHERE PlanoConta.planodescontoprincipal = ? ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and ( PlanoConta.unidadeensino ").append(unidadeEnsino).append(" or unidadeensino is null) ");
		}
		sql.append(" order by PlanoConta.identificadorplanoconta ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
        return montarDadosConsulta(tabelaResultado, usuario);
	}
	
	public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT planoconta.codigo, planoconta.unidadeEnsino, planoconta.planoContaPrincipal, planoconta.identificadorPlanoConta, planoconta.descricao, ");
		sql.append(" planoconta.nivelPlanoConta, planoconta.codigoPatrimonial codigoPatrimonial, planoconta.codigoreduzido,  ");
		sql.append(" planoContaPrincipal.identificadorPlanoConta as identificadorsuperior, ");
		sql.append(" planoContaPrincipal.descricao as descricaosuperior, ");
		sql.append(" planoContaPrincipal.nivelplanoconta as nivelsuperior,  ");
		sql.append(" planoContaPrincipal.codigoreduzido as codigoreduzidosuperior,  ");
		sql.append(" planoContaPrincipal.unidadeensino as unidadesuperior,  ");
		sql.append(" planoContaPrincipal.planoContaPrincipal as planosuperior,  ");
		sql.append(" unidadeensino.nome as unidadeensino_nome ");
		sql.append(" from planoconta ");
		sql.append(" left join planoconta as planoContaPrincipal on planoContaPrincipal.codigo = planoconta.planoContaPrincipal ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = planoconta.unidadeensino ");
		return sql;
	}
	
	@Override
	public TreeNodeCustomizado consultarArvorePlanoContaSuperior(PlanoContaVO planoContaVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();		
		sql.append(" WITH RECURSIVE  planosuperior ( ");
		sql.append(" codigo, unidadeEnsino, planoContaPrincipal, identificadorPlanoConta, descricao,  ");
		sql.append(" nivelPlanoConta, codigoPatrimonial, codigoreduzido,  ");
		sql.append(" identificadorsuperior, ");
		sql.append(" descricaosuperior, ");
		sql.append(" nivelsuperior,  ");
		sql.append(" codigoreduzidosuperior,  ");
		sql.append(" unidadesuperior,  ");
		sql.append(" planosuperior,  ");
		sql.append(" unidadeensino_nome ) as ( ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" where planoconta.codigo = ").append(planoContaVO.getPlanoContaPrincipal().getCodigo());
		sql.append(" union ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" inner join planosuperior on planoconta.codigo = planosuperior.planoContaPrincipal ");
		sql.append(" ) select * from planosuperior order by planosuperior.codigo, planosuperior.identificadorPlanoConta ");
				
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		TreeNodeCustomizado TreeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<Integer, TreeNodeCustomizado>(0);
		while (rs.next()) {
			PlanoContaVO obj = montarDados(rs, usuarioVO);
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj);
			if (!nodes.containsKey(obj.getPlanoContaPrincipal().getCodigo())) {
				nodes.put(obj.getPlanoContaPrincipal().getCodigo(), TreeNodeCustomizadoRaiz);
			}
			nodeImpl.setData(obj);
			nodeImpl.setMaximizarTree(true);
			nodes.get(obj.getPlanoContaPrincipal().getCodigo()).addChild(obj, nodeImpl);
			nodes.put(obj.getCodigo(), nodeImpl);
		}
		return TreeNodeCustomizadoRaiz;
	}

	@Override
	public TreeNodeCustomizado consultarArvorePlanoContaInferior(PlanoContaVO planoContaVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();		
		sql.append(" WITH RECURSIVE  planosuperior ( ");
		sql.append(" codigo, unidadeEnsino, planoContaPrincipal, identificadorPlanoConta, descricao,  ");
		sql.append(" nivelPlanoConta, codigoPatrimonial, codigoreduzido, ");
		sql.append(" identificadorsuperior, ");
		sql.append(" descricaosuperior, ");
		sql.append(" nivelsuperior,  ");
		sql.append(" codigoreduzidosuperior,  ");
		sql.append(" unidadesuperior,  ");
		sql.append(" planosuperior,  ");
		sql.append(" unidadeensino_nome ) as ( ");
		sql.append(getSqlConsultaCompleta());
		if(Uteis.isAtributoPreenchido(planoContaVO)) {
			sql.append(" where planoconta.codigo = ").append(planoContaVO.getCodigo());
		}
		sql.append(" union ");
		sql.append(getSqlConsultaCompleta());
		sql.append(" inner join planosuperior on planoconta.planocontaprincipal = planosuperior.codigo ");
		sql.append(" ) select * from planosuperior order by planosuperior.identificadorPlanoConta, case when planosuperior.planocontaprincipal is null then planosuperior.identificadorPlanoConta else planosuperior.identificadorsuperior  end ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TreeNodeCustomizado TreeNodeCustomizadoRaiz = new TreeNodeCustomizado();
		Map<Integer, TreeNodeCustomizado> nodes = new HashMap<Integer, TreeNodeCustomizado>(0);
		while (rs.next()) {
			PlanoContaVO obj = montarDados(rs, usuarioVO);
			TreeNodeCustomizado nodeImpl = new TreeNodeCustomizado(obj);			
			if (!nodes.containsKey(obj.getPlanoContaPrincipal().getCodigo())) {
				
				nodes.put(obj.getPlanoContaPrincipal().getCodigo(), TreeNodeCustomizadoRaiz);
			}			
			nodeImpl.setData(obj);		
			if(Uteis.isAtributoPreenchido(planoContaVO)) {
				nodeImpl.setMaximizarTree(true);
			}else {
				nodeImpl.setMaximizarTree(obj.getNivelPlanoConta().equals(1));
			}
			nodes.get(obj.getPlanoContaPrincipal().getCodigo()).addChild(obj, nodeImpl);			
			nodes.put(obj.getCodigo(), nodeImpl);
			TreeNodeCustomizadoRaiz.getListaObjetos().add(obj);
		}
		return TreeNodeCustomizadoRaiz;
	}
	
	@Override
	public String consultarPosFixoSugestaoPlanoConta(String prefixo, Integer nivelAdicionar) throws Exception{
		StringBuilder sql  = new StringBuilder("");
		sql.append("select max(replace(identificadorplanoconta, '").append(prefixo).append("', '')) as sugestao from planoconta where identificadorplanoconta ilike ('").append(prefixo).append("%') ");
		sql.append(" and replace(identificadorplanoconta, '").append(prefixo).append("', '') not ilike ('%.%') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()) {
			if(rs.getString("sugestao") != null) {
			if(Uteis.getIsValorNumerico(rs.getString("sugestao"))) {
				int size = rs.getString("sugestao").length();
				Integer numero = Integer.valueOf(rs.getString("sugestao"))+1;
				return prefixo+Uteis.preencherComZerosPosicoesVagas(numero.toString(), size);
			}else {
				return prefixo+rs.getString("sugestao");
			}
			}
		}
		return prefixo+"001";
	}
	
	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil");
	}
	
	@Override
	public String designIReportRelatorioExcel() {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "contabil" + File.separator + "PlanoContaExcel" + ".jrxml");		
	}

}
