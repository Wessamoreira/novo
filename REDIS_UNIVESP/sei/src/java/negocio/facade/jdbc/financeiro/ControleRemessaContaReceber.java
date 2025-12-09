package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleRemessaContaReceberInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ControleRemessaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ControleRemessaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ControleRemessaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleRemessaContaReceber extends ControleAcesso implements ControleRemessaContaReceberInterfaceFacade {

	private static final long serialVersionUID = -5257218153440328270L;
    protected static String idEntidade;
	
    public ControleRemessaContaReceber() throws Exception {
        super();
        setIdEntidade("ControleRemessa");
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#novo()
     */
    public ControleRemessaContaReceberVO novo() throws Exception {
        ControleRemessaContaReceber.incluir(getIdEntidade());
        ControleRemessaContaReceberVO obj = new ControleRemessaContaReceberVO();
        return obj;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#incluir(negocio.comuns.financeiro.ControleRemessaVO
     * )
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleRemessaContaReceberVO obj, final Integer controleRemessa, UsuarioVO usuario) throws Exception {
        try {
        	
			final String sql = "INSERT INTO controleRemessaContaReceber( contaReceber, controleremessa, unidadeEnsino, " //3
					+ " numeroInscricaoEmpresa,	cnpj, agencia, digitoAgencia, contaCorrente, digitoContaCorrente, dac, nossoNumero, " //11
					+ " carteira, codigocarteira, nrDocumento, dataVencimento, valor, valorDescontoAluno, valorBase, especieTitulo, "//19
					+ " juro, dataLimiteConcessaoDesconto, valorDescontoDataLimite, valorDesconto, codigoInscricao, numeroInscricao, nomeSacado, logradouro,"//27
					+ " bairro, cep, cidade, estado, avalista, tipoOrigem, tipoDesconto,"//34
					+ " usaDescontoCompostoPlanoDesconto, descontoprogressivo_codigo, descontoprogressivo_dialimite1, descontoprogressivo_dialimite2, descontoprogressivo_dialimite3, descontoprogressivo_dialimite4, descontoprogressivo_percdescontolimite1, descontoprogressivo_percdescontolimite2,"//42
					+ " descontoprogressivo_percdescontolimite3, descontoprogressivo_percdescontolimite4, descontoprogressivo_valordescontolimite1, descontoprogressivo_valordescontolimite2, descontoprogressivo_valordescontolimite3, descontoprogressivo_valordescontolimite4, codMovRemessa, codigoReceptor1,"//50
					+ " codigoReceptor2, codigoReceptor3, codigoReceptor4, descontoValidoAteDataParcela, situacao, motivoErro, valorDescontoRateio, acrescimo , baixarConta, motivoEstorno, usuarioEstorno, dataEstorno,  "//55
					+ " dataLimiteConcessaoDesconto2, valorDescontoDataLimite2, dataLimiteConcessaoDesconto3, valorDescontoDataLimite3, valorAbatimento, situacaoConta , telefonesacado "//60
					+ " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"//27
					+ " , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
					+ " , ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ) returning codigo "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);			
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getContaReceber().intValue() != 0) {
						sqlInserir.setInt(1, obj.getContaReceber().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}					
					sqlInserir.setInt(2, controleRemessa);
					sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo());
					
					sqlInserir.setInt(4, obj.getNumeroInscricaoEmpresa());
					sqlInserir.setString(5, obj.getCnpj());
					sqlInserir.setString(6, obj.getAgencia());
					sqlInserir.setString(7, obj.getDigitoAgencia());
					sqlInserir.setString(8, obj.getContaCorrente());
					sqlInserir.setString(9, obj.getDigitoContaCorrente());
					sqlInserir.setInt(10, obj.getDac());
					sqlInserir.setString(11, obj.getNossoNumero());
					
					sqlInserir.setString(12, obj.getCarteira());					
					sqlInserir.setString(13, obj.getCodigocarteira());
					sqlInserir.setString(14, obj.getNrDocumento());
					sqlInserir.setDate(15, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setDouble(16, obj.getValor());
					sqlInserir.setDouble(17, obj.getValorDescontoAluno());
					sqlInserir.setDouble(18, obj.getValorBase());
					sqlInserir.setString(19, obj.getEspecieTitulo());
					
					sqlInserir.setDouble(20, obj.getJuro());
					sqlInserir.setDate(21, Uteis.getDataJDBC(obj.getDataLimiteConcessaoDesconto()));
					sqlInserir.setDouble(22, obj.getValorDescontoDataLimite());
					sqlInserir.setDouble(23, obj.getValorDesconto());
					sqlInserir.setInt(24, obj.getCodigoInscricao());
					sqlInserir.setString(25, obj.getNumeroInscricao());
					sqlInserir.setString(26, obj.getNomeSacado());
					sqlInserir.setString(27, obj.getLogradouro());
					
					sqlInserir.setString(28, obj.getBairro());
					sqlInserir.setString(29, obj.getCep());
					sqlInserir.setString(30, obj.getCidade());
					sqlInserir.setString(31, obj.getEstado());
					sqlInserir.setString(32, obj.getAvalista());
					sqlInserir.setString(33, obj.getTipoOrigem());
					sqlInserir.setString(34, obj.getTipoDesconto());
					sqlInserir.setBoolean(35, obj.getUsaDescontoCompostoPlanoDesconto());
					
					sqlInserir.setInt(36, obj.getDescontoprogressivo_codigo());
					sqlInserir.setInt(37, obj.getDescontoprogressivo_dialimite1());
					sqlInserir.setInt(38, obj.getDescontoprogressivo_dialimite2());
					sqlInserir.setInt(39, obj.getDescontoprogressivo_dialimite3());
					sqlInserir.setInt(40, obj.getDescontoprogressivo_dialimite4());
					sqlInserir.setDouble(41, obj.getDescontoprogressivo_percdescontolimite1());
					sqlInserir.setDouble(42, obj.getDescontoprogressivo_percdescontolimite2());
					sqlInserir.setDouble(43, obj.getDescontoprogressivo_percdescontolimite3());
					sqlInserir.setDouble(44, obj.getDescontoprogressivo_percdescontolimite4());
					sqlInserir.setDouble(45, obj.getDescontoprogressivo_valordescontolimite1());
					sqlInserir.setDouble(46, obj.getDescontoprogressivo_valordescontolimite2());
					sqlInserir.setDouble(47, obj.getDescontoprogressivo_valordescontolimite3());
					sqlInserir.setDouble(48, obj.getDescontoprogressivo_valordescontolimite4());					
					sqlInserir.setString(49, obj.getCodMovRemessa());					
					sqlInserir.setString(50, obj.getCodigoReceptor1());					
					sqlInserir.setString(51, obj.getCodigoReceptor2());					
					sqlInserir.setString(52, obj.getCodigoReceptor3());					
					sqlInserir.setString(53, obj.getCodigoReceptor4());					
					sqlInserir.setBoolean(54, obj.getDescontoValidoAteDataParcela());					
					sqlInserir.setString(55, obj.getSituacaoControleRemessaContaReceber().getValor());
					sqlInserir.setString(56, obj.getMotivoErro());
					sqlInserir.setDouble(57, obj.getValorDescontoRateio());
					sqlInserir.setDouble(58, obj.getAcrescimo());
					sqlInserir.setBoolean(59, obj.getBaixarConta());

					sqlInserir.setString(60, obj.getMotivoEstorno());
					sqlInserir.setInt(61, obj.getUsuarioEstorno().getCodigo());
					sqlInserir.setDate(62, Uteis.getDataJDBC(obj.getDataEstorno()));
					if(Uteis.isAtributoPreenchido(obj.getDataLimiteConcessaoDesconto2())) {
						sqlInserir.setDate(63, Uteis.getDataJDBC(obj.getDataLimiteConcessaoDesconto2()));
					}else {
						sqlInserir.setNull(63, 0);
					}
					sqlInserir.setDouble(64, obj.getValorDescontoDataLimite2());
					if(Uteis.isAtributoPreenchido(obj.getDataLimiteConcessaoDesconto3())) {
						sqlInserir.setDate(65, Uteis.getDataJDBC(obj.getDataLimiteConcessaoDesconto3()));
					}else {
						sqlInserir.setNull(65, 0);
					}
					sqlInserir.setDouble(66, obj.getValorDescontoDataLimite3());
					sqlInserir.setDouble(67, obj.getValorAbatimento());
					sqlInserir.setString(68, obj.getSituacaoConta());
					sqlInserir.setString(69, obj.getTelefoneSacado());
					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEstorno(ControleRemessaContaReceberVO controleRemessaContaReceberVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ControleRemessaContaReceber.alterar(getIdEntidade(), false, usuarioVO);
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("UPDATE ControleRemessaContaReceber set motivoEstorno = '").append(controleRemessaContaReceberVO.getMotivoEstorno()).append("', situacao = '").append(controleRemessaContaReceberVO.getSituacaoControleRemessaContaReceber().getValor()).append("', usuarioEstorno = ").append(usuarioVO.getCodigo()).append(", dataEstorno = current_date WHERE (codigo = ").append(controleRemessaContaReceberVO.getCodigo()).append(") and situacao <> 'REMETIDA' "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarRegistroDadosAceite(ControleRemessaContaReceberVO controleRemessaContaReceberVO, UsuarioVO usuarioVO) throws Exception {
//    	try {
//    		ControleRemessaContaReceber.alterar(getIdEntidade(), false, usuarioVO);
//    		StringBuilder sqlStr = new StringBuilder();
//    		sqlStr.append("UPDATE ControleRemessaContaReceber set situacao = '").append(controleRemessaContaReceberVO.getSituacaoControleRemessaContaReceber().getValor()).append("' WHERE (codigo = ").append(controleRemessaContaReceberVO.getCodigo()).append(")"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
//    		getConexao().getJdbcTemplate().update(sqlStr.toString());
//    	} catch (Exception e) {
//    		throw e;
//    	}
    }
    
    public boolean consultarExisteRemessaContaReceber(Integer codContaReceber) throws Exception {
//    	StringBuilder sqlStr = new StringBuilder();
//		sqlStr.append("select controleremessacontareceber.codigo from controleremessacontareceber where contareceber =  ? ");
//		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codContaReceber });
//		if (rs.next()) {
//			return true;
//		}
    	return false;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removerVinculoContaReceber(Integer matriculaPeriodo, UsuarioVO usuarioVO) throws Exception {
//    	try {
//    		StringBuilder sqlStr = new StringBuilder();
//    		sqlStr.append("UPDATE ControleRemessaContaReceber set contaReceber = null  WHERE codigo in (");
//    		sqlStr.append(" select codigo from controleremessacontareceber  where contareceber in (select codigo from contareceber where matriculaperiodo = ");
//    		sqlStr.append(matriculaPeriodo).append("  and contareceber.tipoorigem in ('MAT', 'MEN', 'MDI','BCC' ) )) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
//    		getConexao().getJdbcTemplate().update(sqlStr.toString());
//    	} catch (Exception e) {
//    		throw e;
//    	}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizaVinculoContaReceber(Integer contaReceber, String nossoNumero, UsuarioVO usuarioVO) throws Exception {
//    	try {
//    		if (contaReceber != null && !contaReceber.equals(0)) {
//	    		StringBuilder sqlStr = new StringBuilder();
//	    		sqlStr.append("UPDATE ControleRemessaContaReceber set contaReceber = ").append(contaReceber);
//	    		sqlStr.append(" where nossonumero = '");
//	    		sqlStr.append(nossoNumero).append("' " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
//	    		getConexao().getJdbcTemplate().update(sqlStr.toString());
//    		}
//    	} catch (Exception e) {
//    		throw e;
//    	}
    }
    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizaVinculoContaReceberConvenio(Integer matriculaperiodo,  UsuarioVO usuarioVO) throws Exception {
//    	try {
//    		if (matriculaperiodo != null && !matriculaperiodo.equals(0)) {
//    			StringBuilder sqlStr = new StringBuilder();
//    			sqlStr.append( "update controleremessacontareceber set contareceber =  t.contareceber from ( ");
//    			sqlStr.append("	select contareceber.codigo as contareceber, controleremessacontareceber.codigo as controleremessacontareceber from contareceber ");    			
//    			sqlStr.append("	inner join controleremessacontareceber on controleremessacontareceber.nossonumero  =  contareceber.nossonumero ");
//    			sqlStr.append("	and controleremessacontareceber.contareceber is null ");
//    			sqlStr.append("	where contareceber.tipoorigem  = 'BCC' and contareceber.matriculaperiodo = ").append(matriculaperiodo);
//    			sqlStr.append("	) as t where t.controleremessacontareceber = controleremessacontareceber.codigo ;");    			
//    			sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
//    			getConexao().getJdbcTemplate().update(sqlStr.toString());
//    		}
//    	} catch (Exception e) {
//    		throw e;
//    	}
    }

    public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaPorCodigoMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("");    	
    	sqlStr.append("SELECT DISTINCT controleremessacontareceber.*, contareceber.contaremetidacomalteracao as \"contareceber.contaremetidacomalteracao\" , usuario.nome as \"nomeUsuarioEstorno\",  contareceber.juroporcentagem as \"juroporcentagem\", contareceber.multaporcentagem as \"multaporcentagem\" ");
    	sqlStr.append(" from controleRemessaContaReceber  ");
    	sqlStr.append(" left join usuario on usuario.codigo = controleremessacontareceber.usuarioestorno ");
    	sqlStr.append(" left join contareceber on contareceber.codigo = controleremessacontareceber.contareceber ");
    	sqlStr.append(" WHERE contareceber.matriculaperiodo = ").append(matriculaPeriodo);
    	sqlStr.append(" and controleremessacontareceber.codigo in (select max(codigo)as codigo from controleremessacontareceber group by nossonumero) ");
    	sqlStr.append(" order by controleRemessaContaReceber.codigo ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
    }
    
    public ControleRemessaContaReceberVO consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(List<ContaCorrenteVO> listaContaCorrente, String nossoNumero) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("");
    	sqlStr.append("SELECT DISTINCT controleremessacontareceber.*, contareceber.contaremetidacomalteracao as \"contareceber.contaremetidacomalteracao\" ,  usuario.nome as \"nomeUsuarioEstorno\",  contareceber.juroporcentagem as \"juroporcentagem\", contareceber.multaporcentagem as \"multaporcentagem\" ");
    	sqlStr.append(" from controleRemessaContaReceber  ");
    	sqlStr.append(" inner join controleremessa on controleremessa.codigo = controleremessacontareceber.controleremessa ");
    	sqlStr.append(" left join usuario on usuario.codigo = controleremessacontareceber.usuarioestorno ");
    	sqlStr.append(" left join contareceber on contareceber.codigo = controleremessacontareceber.contareceber ");  
    	if (Uteis.isAtributoPreenchido(listaContaCorrente)) {
			 sqlStr.append(" AND contareceber.contacorrente in( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaContaCorrente)).append(" )");
		}
    	sqlStr.append(" WHERE controleremessacontareceber.nossoNumero = '").append(nossoNumero).append("' ");
    	if (Uteis.isAtributoPreenchido(listaContaCorrente)) {
			 sqlStr.append(" AND controleremessa.contacorrente in( ").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaContaCorrente)).append(" )");
		} else {
			sqlStr.append(" and controleremessa.contacorrente = contareceber.contacorrente ");	
		}  	
    	sqlStr.append(" order by controleRemessaContaReceber.codigo desc limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	if (!tabelaResultado.next()) {
    		return new ControleRemessaContaReceberVO();
    	}
    	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
    	
    }
	
    public ControleRemessaContaReceberVO consultaRapidaContaArquivoRemessaPorCodigoContaReceber(Integer contaReceber) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("");
    	sqlStr.append("SELECT DISTINCT controleremessacontareceber.*, contareceber.contaremetidacomalteracao as \"contareceber.contaremetidacomalteracao\" ,  usuario.nome as \"nomeUsuarioEstorno\",  contareceber.juroporcentagem as \"juroporcentagem\", contareceber.multaporcentagem as \"multaporcentagem\"");
    	sqlStr.append(" from controleRemessaContaReceber  ");
    	sqlStr.append(" left join contareceber on contareceber.codigo = controleremessacontareceber.contareceber ");    	
    	sqlStr.append(" left join usuario on usuario.codigo = controleremessacontareceber.usuarioestorno ");
    	sqlStr.append(" WHERE controleremessacontareceber.contareceber = ").append(contaReceber);
    	sqlStr.append(" and controleremessacontareceber.situacao <> 'ESTORNADO_PELO_USUARIO' ");
    	sqlStr.append(" order by controleRemessaContaReceber.codigo desc limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new ControleRemessaContaReceberVO();
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
        
    }

    public List<ControleRemessaContaReceberVO> consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT controleremessacontareceber.*, contareceber.contaremetidacomalteracao as \"contareceber.contaremetidacomalteracao\" , usuario.nome as \"nomeUsuarioEstorno\",  contareceber.juroporcentagem as \"juroporcentagem\", contareceber.multaporcentagem as \"multaporcentagem\" ");
		sqlStr.append(" from controleRemessaContaReceber  ");
		sqlStr.append(" left join contareceber on contareceber.codigo = controleremessacontareceber.contareceber ");		
		sqlStr.append(" left join usuario on usuario.codigo = controleremessacontareceber.usuarioestorno ");
		sqlStr.append(" WHERE controleremessa = ").append(controleRemessaVO.getCodigo());
		sqlStr.append(" order by controleRemessaContaReceber.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
	}
    
    public Boolean verificaContaReceberRegistrada(ContaReceberVO obj) throws Exception {
//    	StringBuilder sqlStr = new StringBuilder("");
//    	sqlStr.append(" SELECT DISTINCT controleremessacontareceber.codigo ");
//    	sqlStr.append(" from controleRemessaContaReceber  ");
//    	sqlStr.append(" WHERE contareceber = ").append(obj.getCodigo());
//    	sqlStr.append(" and situacao in ('AGUARDANDO_PROCESSAMENTO', 'REMETIDA') ");
//    	sqlStr.append(" order by controleRemessaContaReceber.codigo ");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		if (tabelaResultado.next()) {
//			Integer codigo = tabelaResultado.getInt("codigo");
//            if ((codigo!= null) && (!codigo.equals(0))) {
//                return false;
//            }
//		}
		return true;
    }
    
    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ControleRemessaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        Integer cont = 0;
        while (tabelaResultado.next()) {
        	ControleRemessaContaReceberVO obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
        	cont = cont + 1;
        	obj.setPosicaoNoArquivoRemessa(cont);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ControleRemessaVO</code>.
     *
     * @return O objeto da classe <code>ControleRemessaVO</code> com os dados devidamente montados.
     */
    public static ControleRemessaContaReceberVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleRemessaContaReceberVO obj = new ControleRemessaContaReceberVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setBaixarConta(dadosSQL.getBoolean("baixarConta"));
        obj.setContaRemetidaComAlteracao(dadosSQL.getBoolean("contareceber.contaremetidacomalteracao"));		
        obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.setNumeroInscricaoEmpresa(dadosSQL.getInt("numeroInscricaoEmpresa"));
        obj.setCnpj(dadosSQL.getString("cnpj"));		
        obj.setAgencia(dadosSQL.getString("agencia"));		
        obj.setDigitoAgencia(dadosSQL.getString("digitoAgencia"));		
        obj.setContaCorrente(dadosSQL.getString("contaCorrente"));		
        obj.setDigitoContaCorrente(dadosSQL.getString("digitoContaCorrente"));		
        obj.setDac(dadosSQL.getInt("dac"));		
        obj.setNossoNumero(dadosSQL.getString("nossoNumero"));		
        obj.setCarteira(dadosSQL.getString("carteira"));		
        obj.setCodigocarteira(dadosSQL.getString("codigoCarteira"));		
        obj.setNrDocumento(dadosSQL.getString("nrDocumento"));		
        obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));		
        obj.setValor(dadosSQL.getDouble("valor"));
        obj.setAcrescimo(dadosSQL.getDouble("acrescimo"));
        obj.setValorDescontoAluno(dadosSQL.getDouble("valorDescontoAluno"));
        obj.setValorDescontoRateio(dadosSQL.getDouble("valorDescontoRateio"));
        obj.setValorBase(dadosSQL.getDouble("valorBase"));
        obj.setEspecieTitulo(dadosSQL.getString("especieTitulo"));
        obj.setJuro(dadosSQL.getDouble("juro"));
        obj.setDataLimiteConcessaoDesconto(dadosSQL.getDate("DataLimiteConcessaoDesconto"));
        obj.setValorDescontoDataLimite(dadosSQL.getDouble("valorDescontoDataLimite"));
        obj.setDataLimiteConcessaoDesconto2(dadosSQL.getDate("dataLimiteConcessaoDesconto2"));
        obj.setValorDescontoDataLimite2(dadosSQL.getDouble("valorDescontoDataLimite2"));
        obj.setDataLimiteConcessaoDesconto3(dadosSQL.getDate("dataLimiteConcessaoDesconto3"));
        obj.setValorDescontoDataLimite3(dadosSQL.getDouble("valorDescontoDataLimite3"));
        obj.setValorAbatimento(dadosSQL.getDouble("valorAbatimento"));
        obj.setValorDesconto(dadosSQL.getDouble("valorDesconto"));
        obj.setCodigoInscricao(dadosSQL.getInt("codigoInscricao"));
        obj.setNumeroInscricao(dadosSQL.getString("numeroInscricao"));
        obj.setNomeSacado(dadosSQL.getString("nomeSacado"));
        obj.setLogradouro(dadosSQL.getString("logradouro"));
        obj.setBairro(dadosSQL.getString("bairro"));
        obj.setCep(dadosSQL.getString("cep"));
        obj.setCidade(dadosSQL.getString("cidade"));
        obj.setEstado(dadosSQL.getString("estado"));
        obj.setAvalista(dadosSQL.getString("avalista"));
        obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
        obj.setTipoDesconto(dadosSQL.getString("tipoDesconto"));
        obj.setSituacaoConta(dadosSQL.getString("situacaoConta"));
        obj.setUsaDescontoCompostoPlanoDesconto(dadosSQL.getBoolean("usaDescontoCompostoPlanoDesconto"));
        
        obj.setDescontoprogressivo_codigo(dadosSQL.getInt("descontoprogressivo_codigo"));
        obj.setDescontoprogressivo_dialimite1(dadosSQL.getInt("descontoprogressivo_dialimite1"));
        obj.setDescontoprogressivo_dialimite2(dadosSQL.getInt("descontoprogressivo_dialimite2"));
        obj.setDescontoprogressivo_dialimite3(dadosSQL.getInt("descontoprogressivo_dialimite3"));
        obj.setDescontoprogressivo_dialimite4(dadosSQL.getInt("descontoprogressivo_dialimite4"));

        obj.setDescontoprogressivo_percdescontolimite1(dadosSQL.getInt("descontoprogressivo_percdescontolimite1"));
        obj.setDescontoprogressivo_percdescontolimite2(dadosSQL.getInt("descontoprogressivo_percdescontolimite2"));
        obj.setDescontoprogressivo_percdescontolimite3(dadosSQL.getInt("descontoprogressivo_percdescontolimite3"));
        obj.setDescontoprogressivo_percdescontolimite4(dadosSQL.getInt("descontoprogressivo_percdescontolimite4"));

        obj.setDescontoprogressivo_valordescontolimite1(dadosSQL.getInt("descontoprogressivo_valordescontolimite1"));
        obj.setDescontoprogressivo_valordescontolimite2(dadosSQL.getInt("descontoprogressivo_valordescontolimite2"));
        obj.setDescontoprogressivo_valordescontolimite3(dadosSQL.getInt("descontoprogressivo_valordescontolimite3"));
        obj.setDescontoprogressivo_valordescontolimite4(dadosSQL.getInt("descontoprogressivo_valordescontolimite4"));

        obj.setCodMovRemessa(dadosSQL.getString("codMovRemessa"));
        obj.setCodigoReceptor1(dadosSQL.getString("codigoReceptor1"));
        obj.setCodigoReceptor2(dadosSQL.getString("codigoReceptor2"));
        obj.setCodigoReceptor3(dadosSQL.getString("codigoReceptor3"));
        obj.setCodigoReceptor4(dadosSQL.getString("codigoReceptor4"));
        obj.setDescontoValidoAteDataParcela(dadosSQL.getBoolean("descontoValidoAteDataParcela"));
        obj.setMotivoErro(dadosSQL.getString("motivoErro"));
        obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.getEnum(dadosSQL.getString("situacao")));

        obj.setDataEstorno(dadosSQL.getDate("dataEstorno"));
        obj.getUsuarioEstorno().setNome(dadosSQL.getString("nomeUsuarioEstorno"));
        obj.setMotivoEstorno(dadosSQL.getString("motivoEstorno"));
        obj.setContaReceber(dadosSQL.getInt("contareceber"));
        obj.setTelefoneSacado(dadosSQL.getString("telefonesacado"));
        obj.setJuroPorcentagem(dadosSQL.getDouble("juroporcentagem"));
        obj.setMultaPorcentagem(dadosSQL.getDouble("multaporcentagem"));
	        
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        return obj;
    }

    public static void montarDadosResponsavel(ControleRemessaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados,
                usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ControleRemessaContaReceber.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ControleRemessaContaReceber.idEntidade = idEntidade;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void removeVinculoContaReceberPorCodigoContaReceber(Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
//    	try {
//    		StringBuilder sqlStr = new StringBuilder();
//    		sqlStr.append("UPDATE ControleRemessaContaReceber set contaReceber = null where contareceber = ").append(contaReceber.intValue()).append(" ;");
//    		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
//    		getConexao().getJdbcTemplate().update(sqlStr.toString());
//    	} catch (Exception e) {
//    		throw e;
//    	}
    }
}
