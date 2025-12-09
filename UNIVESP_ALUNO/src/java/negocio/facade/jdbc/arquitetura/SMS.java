/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.arquitetura;

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

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.SMSFacadeInterface;
/**
 *
 * @author PEDRO
 */
@Repository
@Scope("singleton")
@Lazy
public class SMS extends ControleAcesso implements SMSFacadeInterface {

    protected static String idEntidade;

    public SMS() throws Exception {
        super();
        setIdEntidade("SMS");
    }

    public static boolean validarDados(SMSVO obj) throws ConsistirException {
        if (obj.getAssunto().isEmpty()) {
        	return false;
        }
        if (obj.getMensagem().isEmpty()) {
        	return false;
        }
        if (obj.getCelular().isEmpty()) {
        	return false;
        }
        return true;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>CidadeVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação
     * <code>incluir</code> da superclasse.
     *
     * @param obj Objeto da classe
     * <code>CidadeVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso
     * ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void incluir(final SMSVO obj) throws Exception {
        try {
            if (validarDados(obj)) {
	            final String sql = "INSERT INTO SMS( assunto, mensagem, celular, dataCadastro, enviouSMS, mensagemSMS, nomeDest, codigoDest, cpfDest, matriculaDest) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
	            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
	
	                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
	                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
	                    sqlInserir.setString(1, obj.getAssunto());
	                    sqlInserir.setString(2, obj.getMensagem());
	                    sqlInserir.setString(3, obj.getCelular());
	                    sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataCadastro()));
	                    sqlInserir.setBoolean(5, obj.getEnviouSMS());
	                    sqlInserir.setString(6, obj.getMsgEnvioSMS());
	                    sqlInserir.setString(7, obj.getNomeDest());
	                    sqlInserir.setString(8, obj.getCodigoDest());
	                    sqlInserir.setString(9, obj.getCpfDest());
	                    sqlInserir.setString(10, obj.getMatriculaDest());
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
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarGravacaoSMS(ComunicacaoInternaVO obj, UsuarioVO usuarioLogado) throws Exception {
        ConfiguracaoGeralSistemaVO config = null;
        String celular = "";
        String nomeDest = "";
        String codigoDest = "";
        String cpfDest = "";
        String matriculaDest = "";
        try {
            // começo a salvar os emails para os destinatarios
            for (ComunicadoInternoDestinatarioVO cidVO : obj.getComunicadoInternoDestinatarioVOs()) {
                SMSVO smsVO = new SMSVO();
                if (!cidVO.getDestinatario().getCodigo().equals(0)) {
                    if (cidVO.getDestinatario().getEmail() == null || cidVO.getDestinatario().getEmail().trim().equals("")) {
                        cidVO.setDestinatario(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(cidVO.getDestinatario().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
                    }
                    nomeDest = cidVO.getDestinatario().getNome();
                    celular = cidVO.getDestinatario().getCelular();
                    codigoDest = cidVO.getDestinatario().getCodigo().toString();
                    cpfDest = cidVO.getDestinatario().getCPF();
                    matriculaDest = cidVO.getDestinatario().getRegistroAcademico();
                } else if (!cidVO.getDestinatario().getNome().equals("")) {
                	nomeDest = cidVO.getDestinatario().getNome();
                    celular = cidVO.getDestinatario().getCelular();
                    codigoDest = cidVO.getDestinatario().getCodigo().toString();
                    cpfDest = cidVO.getDestinatario().getCPF();
                    matriculaDest = cidVO.getDestinatario().getRegistroAcademico();
                } 
//                else if (!cidVO.getDestinatarioParceiro().getCodigo().equals(0)) {
//                    if (cidVO.getDestinatarioParceiro().getEmail() == null || cidVO.getDestinatarioParceiro().getEmail().trim().equals("")) {
//                        cidVO.setDestinatarioParceiro(getFacadeFactory().getParceiroFacade().consultarPorChavePrimaria(cidVO.getDestinatarioParceiro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
//                    }
//                    nomeDest = cidVO.getDestinatarioParceiro().getNome();
//                    cidVO.getDestinatario().setNome(nomeDest);
//                    codigoDest = cidVO.getDestinatario().getCodigo().toString();
//                    cpfDest = cidVO.getDestinatario().getCPF();
//                    matriculaDest = cidVO.getDestinatario().getRegistroAcademico();
//                    celular = cidVO.getDestinatarioParceiro().getTCelular();                    
//                }
                smsVO.setNomeDest(nomeDest);
                smsVO.setCelular(celular.trim());
                smsVO.setCodigoDest(codigoDest);
                smsVO.setCpfDest(cpfDest);
                smsVO.setMatriculaDest(matriculaDest);
                smsVO.setAssunto(obj.getAssunto());                
                smsVO.setMensagem(obj.getMensagemSMS());
                smsVO.setMensagem(getFacadeFactory().getComunicacaoInternaFacade().substituirTag(smsVO.getMensagem(), cidVO.getDestinatario()));
                if (smsVO.getMensagem().contains("#NOMEALUNO")) {
                    smsVO.setMensagem(Uteis.trocarHashTag("#NOMEALUNO", smsVO.getNomeDest(), smsVO.getMensagem()));
                }
                try {
                	incluir(smsVO);
                } catch (Exception e) {
                    ////System.out.print("ERRO");
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            nomeDest = null;
            config = null;
        }
    }


    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (
     * <code>ResultSet</code>) em um objeto da classe
     * <code>CidadeVO</code>.
     *
     * @return O objeto da classe
     * <code>CidadeVO</code> com os dados devidamente montados.
     */
    public static SMSVO montarDados(SqlRowSet dadosSQL) throws Exception {
        SMSVO obj = new SMSVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNomeDest(dadosSQL.getString("nomeDest"));
        obj.setAssunto(dadosSQL.getString("assunto"));
        obj.setMensagem(dadosSQL.getString("mensagem"));
        obj.setCelular(dadosSQL.getString("celular"));
        obj.setDataCadastro(dadosSQL.getTimestamp("datacadastro"));
        obj.setEnviouSMS(dadosSQL.getBoolean("enviouSMS"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }


    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return SMS.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        SMS.idEntidade = idEntidade;
    }
    
    
    public List<SMSVO> consultarSms(String nome, String numero) throws Exception {
        String sqlStr = "SELECT * from sms where nomeDest ilike '" + nome 
        		+ "%' and sem_caracteres_especiais(celular) ilike '" + Uteis.removeCaractersEspeciais2(numero)
        		+ "%' order by datacadastro desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado);
    }
    
    public List<SMSVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<SMSVO> vetResultado = new ArrayList<SMSVO>(0);
		while (tabelaResultado.next()) {
			SMSVO obj = new SMSVO();
			obj = montarDados(tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}    
    
}
