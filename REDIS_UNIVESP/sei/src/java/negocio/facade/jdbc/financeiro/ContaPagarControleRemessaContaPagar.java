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
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.enumerador.FinalidadeDocEnum;
import negocio.comuns.financeiro.enumerador.FinalidadePixEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeTedEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoContribuinte;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarControleRemessaContaPagarInterfaceFacade;

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
public class ContaPagarControleRemessaContaPagar extends ControleAcesso implements ContaPagarControleRemessaContaPagarInterfaceFacade {

	private static final long serialVersionUID = -5257218153440328270L;
    protected static String idEntidade;
	
    public ContaPagarControleRemessaContaPagar() throws Exception {
        super();
        setIdEntidade("ControleRemessaContaPagar");
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#novo()
     */
    public ContaPagarControleRemessaContaPagarVO novo() throws Exception {
    	ContaPagarControleRemessaContaPagar.incluir(getIdEntidade());
    	ContaPagarControleRemessaContaPagarVO obj = new ContaPagarControleRemessaContaPagarVO();
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
    public void incluir(final ContaPagarControleRemessaContaPagarVO obj, final Integer controleRemessa, UsuarioVO usuario) throws Exception {
        try {
        	final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ContaPagarControleRemessaContaPagar (controleRemessaContaPagar, situacaoControleRemessaContaReceber, usuarioEstorno,  ");
			sql.append(" dataEstorno, motivoEstorno, motivoErro, contaPagar, tipoSacado, dataVencimento, ");
			sql.append(" valor,juro,multa,desconto, nrDocumento, nossonumero, codigoBarra, tipoInscricaoFavorecido, cnpjOuCpfFavorecido,");
			sql.append(" inscricaoEstadualFavorecido, inscricaoMunicipalFavorecido, nomeFavorecido, emailFavorecido, complementoFavorecido, ");
			sql.append(" numeroEnderecoFavorecido, logradouroFavorecido, bairroFavorecido, cepFavorecido, cidadeFavorecido, ");
			sql.append(" estadoFavorecido,bancoRemessaPagar,tipoServicoContaPagar,tipoLancamentoContaPagar,finalidadeDocEnum,finalidadeTedEnum,");
			sql.append(" modalidadeTransferenciaBancariaEnum, bancoRecebimento, numeroAgenciaRecebimento, digitoAgenciaRecebimento, contaCorrenteRecebimento, digitoCorrenteRecebimento, ");
			sql.append(" codigoReceitaTributo, identificacaoContribuinte, tipoIdentificacaoContribuinte, numeroReferencia, valorReceitaBrutaAcumulada, percentualReceitaBrutaAcumulada, ");
			sql.append(" linhadigitavel1, linhadigitavel2, linhadigitavel3, linhadigitavel4, linhadigitavel5, linhadigitavel6, linhadigitavel7, linhadigitavel8, tipoContaEnum  , codigoAgrupamentoContasPagar , observacaoAgrupamento , transmissaonossonumero  , ");
			sql.append(" chaveEnderecamentoPix , finalidadePixEnum, tipoIdentificacaoChavePixEnum, identificacaoQRCODE  ) ");
			sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ");
			sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,? ,? ,? ,? ,? ,?  )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, controleRemessa);
					if (Uteis.isAtributoPreenchido(obj.getSituacaoControleRemessaContaReceber())) {
						sqlInserir.setString(++i, obj.getSituacaoControleRemessaContaReceber().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getUsuarioEstorno())) {
						sqlInserir.setInt(++i, obj.getUsuarioEstorno().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataEstorno())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataEstorno()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getMotivoEstorno())) {
						sqlInserir.setString(++i, obj.getMotivoEstorno());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getMotivoErro())) {
						sqlInserir.setString(++i, obj.getMotivoErro());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getContaPagar())) {
						sqlInserir.setInt(++i, obj.getContaPagar().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTipoSacado());
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setDouble(++i, obj.getValor());
					sqlInserir.setDouble(++i, obj.getJuro());
					sqlInserir.setDouble(++i, obj.getMulta());
					sqlInserir.setDouble(++i, obj.getDesconto());
					sqlInserir.setString(++i, obj.getNrDocumento());
					sqlInserir.setLong(++i, obj.getNossoNumero());
					sqlInserir.setString(++i, obj.getCodigoBarra());
					sqlInserir.setString(++i, obj.getTipoInscricaoFavorecido());
					sqlInserir.setString(++i, obj.getCnpjOuCpfFavorecido());
					sqlInserir.setString(++i, obj.getInscricaoEstadualFavorecido());
					sqlInserir.setString(++i, obj.getInscricaoMunicipalFavorecido());
					sqlInserir.setString(++i, obj.getNomeFavorecido());
					sqlInserir.setString(++i, obj.getEmailFavorecido());
					sqlInserir.setString(++i, obj.getComplementoFavorecido());
					sqlInserir.setString(++i, obj.getNumeroEnderecoFavorecido());
					sqlInserir.setString(++i, obj.getLogradouroFavorecido());
					sqlInserir.setString(++i, obj.getBairroFavorecido());
					sqlInserir.setString(++i, obj.getCepFavorecido());
					sqlInserir.setString(++i, obj.getCidadeFavorecido());
					sqlInserir.setString(++i, obj.getEstadoFavorecido());
					if (Uteis.isAtributoPreenchido(obj.getBancoRemessaPagar())) {
						sqlInserir.setInt(++i, obj.getBancoRemessaPagar().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoServicoContaPagar())) {
						sqlInserir.setString(++i, obj.getTipoServicoContaPagar().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar())) {
						sqlInserir.setString(++i, obj.getTipoLancamentoContaPagar().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeDocEnum())) {
						sqlInserir.setString(++i, obj.getFinalidadeDocEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getFinalidadeTedEnum())) {
						sqlInserir.setString(++i, obj.getFinalidadeTedEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getModalidadeTransferenciaBancariaEnum())) {
						sqlInserir.setString(++i, obj.getModalidadeTransferenciaBancariaEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getBancoRecebimento())) {
						sqlInserir.setInt(++i, obj.getBancoRecebimento().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroAgenciaRecebimento());
					sqlInserir.setString(++i, obj.getDigitoAgenciaRecebimento());
					sqlInserir.setString(++i, obj.getContaCorrenteRecebimento());
					sqlInserir.setString(++i, obj.getDigitoCorrenteRecebimento());
					sqlInserir.setString(++i, obj.getCodigoReceitaTributo());
					sqlInserir.setString(++i, obj.getIdentificacaoContribuinte());
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoContribuinte())) {
						sqlInserir.setString(++i, obj.getTipoIdentificacaoContribuinte().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNumeroReferencia());
					sqlInserir.setDouble(++i, obj.getValorReceitaBrutaAcumulada());
					sqlInserir.setDouble(++i, obj.getPercentualReceitaBrutaAcumulada());
					sqlInserir.setString(++i, obj.getLinhaDigitavel1());
					sqlInserir.setString(++i, obj.getLinhaDigitavel2());
					sqlInserir.setString(++i, obj.getLinhaDigitavel3());
					sqlInserir.setString(++i, obj.getLinhaDigitavel4());
					sqlInserir.setString(++i, obj.getLinhaDigitavel5());
					sqlInserir.setString(++i, obj.getLinhaDigitavel6());
					sqlInserir.setString(++i, obj.getLinhaDigitavel7());
					sqlInserir.setString(++i, obj.getLinhaDigitavel8());
					if (Uteis.isAtributoPreenchido(obj.getTipoContaEnum())) {
						sqlInserir.setString(++i, obj.getTipoContaEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getCodigoAgrupamentoContasPagar());
					sqlInserir.setString(++i, obj.getObservacaoAgrupamento());		
					sqlInserir.setString(++i, obj.getCodigoTransmissaoRemessanossonumero());	
					sqlInserir.setString(++i, obj.getChaveEnderecamentoPix());
					if (Uteis.isAtributoPreenchido(obj.getFinalidadePixEnum())) {
						sqlInserir.setString(++i, obj.getFinalidadePixEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
						sqlInserir.setString(++i, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getIdentificacaoQRCODE());	
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
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
	public void realizarEstorno(ContaPagarControleRemessaContaPagarVO controleRemessaContaPagarVO, UsuarioVO usuarioVO) throws Exception {
		try {
			ContaPagarControleRemessaContaPagar.alterar(getIdEntidade(), false, usuarioVO);
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("UPDATE ContaPagarControleRemessaContaPagar set motivoEstorno = '").append(controleRemessaContaPagarVO.getMotivoEstorno()).append("', ");
			sqlStr.append(" situacaoControleRemessaContaReceber = '").append(controleRemessaContaPagarVO.getSituacaoControleRemessaContaReceber().getValor()).append("', ");
			sqlStr.append(" usuarioEstorno = ").append(usuarioVO.getCodigo()).append(", ");
			sqlStr.append(" contapagar = null, ");
			sqlStr.append(" codigoagrupamentocontaspagar = null, ");
			sqlStr.append(" transmissaonossonumero = null, ");
			sqlStr.append(" dataEstorno = current_date ");
			sqlStr.append(" WHERE (codigo = ").append(controleRemessaContaPagarVO.getCodigo()).append(")"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} catch (Exception e) {
			throw e;
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarRegistroDadosAceite(ContaPagarControleRemessaContaPagarVO controleRemessaContaPagarVO, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE ContaPagarControleRemessaContaPagar set situacaoControleRemessaContaReceber = '").append(controleRemessaContaPagarVO.getSituacaoControleRemessaContaReceber().getValor()).append("' WHERE (codigo = ").append(controleRemessaContaPagarVO.getCodigo()).append(")"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarSituacaoContaPagarControleRemessaContaPagarPorContaPagar(Integer contapagar, SituacaoControleRemessaContaReceberEnum situacao, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE ContaPagarControleRemessaContaPagar set situacaoControleRemessaContaReceber = '").append(situacao).append("' WHERE (contapagar = ").append(contapagar).append(")"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }

    public ContaPagarControleRemessaContaPagarVO consultaRapidaContaArquivoRemessaPorCodigoContaPagar(Integer contaPagar) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("");
    	sqlStr.append("SELECT DISTINCT contapagarcontroleremessacontapagar.*,");
    	//sqlStr.append(" contareceber.contaremetidacomalteracao as \"contareceber.contaremetidacomalteracao\" ,   ");
    	sqlStr.append(" usuario.nome as \"nomeUsuarioEstorno\" ");
    	sqlStr.append(" from contaPagarControleRemessaContaPagar  ");
    	sqlStr.append(" left join contapagar on contapagar.codigo = contapagarcontroleremessacontapagar.contapagar ");    	
    	sqlStr.append(" left join usuario on usuario.codigo = contapagarcontroleremessacontapagar.usuarioestorno ");
    	sqlStr.append(" WHERE contapagarcontroleremessacontapagar.contapagar = ").append(contaPagar);
    	sqlStr.append(" order by contaPagarControleRemessaContaPagar.codigo desc limit 1");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new ContaPagarControleRemessaContaPagarVO();
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
        
    }

    public ContaPagarControleRemessaContaPagarVO consultarPorCodigo(Integer codigo) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("");
    	sqlStr.append(" SELECT * from contaPagarControleRemessaContaPagar");
    	sqlStr.append(" where codigo = ?");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
    	if (!tabelaResultado.next()) {
    		return new ContaPagarControleRemessaContaPagarVO();
    	}
    	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, null);
    	
    }

    public List<ContaPagarControleRemessaContaPagarVO> consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT contapagarcontroleremessacontapagar.*,  ");
		//sqlStr.append("contapagar.contaremetidacomalteracao as \"contapagar.contaremetidacomalteracao\" ,  ");
		sqlStr.append(" contapagar.codigo as \"contapagar.codigo\", ");
		sqlStr.append(" usuario.nome as \"nomeUsuarioEstorno\" ");
		sqlStr.append(" from contaPagarControleRemessaContaPagar  ");
		sqlStr.append(" left join contapagar on contapagar.codigo = contapagarcontroleremessacontapagar.contapagar ");		
		sqlStr.append(" left join usuario on usuario.codigo = contapagarcontroleremessacontapagar.usuarioestorno ");
		sqlStr.append(" WHERE controleremessacontapagar = ").append(controleRemessaVO.getCodigo());
		sqlStr.append(" order by contaPagarControleRemessaContaPagar.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
	}
    
    
    @Override
    public Integer verificaContaPagarExistenteEmControleRemessa(ContaPagarVO obj) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("");
    	sqlStr.append("SELECT DISTINCT contapagarcontroleremessacontapagar.controleRemessaContaPagar ");
    	sqlStr.append(" from contaPagarControleRemessaContaPagar  ");
    	sqlStr.append(" WHERE contapagar = ").append(obj.getCodigo());
    	sqlStr.append(" and situacaoControleRemessaContaReceber in ('AGUARDANDO_PROCESSAMENTO') ");
    	sqlStr.append(" order by contaPagarControleRemessaContaPagar.controleRemessaContaPagar ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado.next() ? tabelaResultado.getInt("controleRemessaContaPagar"): null;
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
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ControleRemessaVO</code>.
     *
     * @return O objeto da classe <code>ControleRemessaVO</code> com os dados devidamente montados.
     */
    public static ContaPagarControleRemessaContaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ContaPagarControleRemessaContaPagarVO obj = new ContaPagarControleRemessaContaPagarVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setMotivoErro(dadosSQL.getString("motivoErro"));        
        obj.setDataEstorno(dadosSQL.getDate("dataEstorno"));
        obj.getUsuarioEstorno().setNome(dadosSQL.getString("usuarioEstorno"));
        obj.setMotivoEstorno(dadosSQL.getString("motivoEstorno"));
        obj.setControleRemessaContaPagar(dadosSQL.getInt("controleRemessaContaPagar"));
		obj.setTipoSacado(dadosSQL.getString("tiposacado"));
		obj.setDataVencimento(dadosSQL.getDate("datavencimento"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setJuro(dadosSQL.getDouble("juro"));
		obj.setMulta(dadosSQL.getDouble("multa"));
		obj.setDesconto(dadosSQL.getDouble("desconto"));
		obj.setNrDocumento(dadosSQL.getString("nrdocumento"));
		obj.setNossoNumero(dadosSQL.getLong("nossonumero"));
		obj.setCodigoBarra(dadosSQL.getString("codigobarra"));
		obj.setLinhaDigitavel1(dadosSQL.getString("linhaDigitavel1"));
		obj.setLinhaDigitavel2(dadosSQL.getString("linhaDigitavel2"));
		obj.setLinhaDigitavel3(dadosSQL.getString("linhaDigitavel3"));
		obj.setLinhaDigitavel4(dadosSQL.getString("linhaDigitavel4"));
		obj.setLinhaDigitavel5(dadosSQL.getString("linhaDigitavel5"));
		obj.setLinhaDigitavel6(dadosSQL.getString("linhaDigitavel6"));
		obj.setLinhaDigitavel7(dadosSQL.getString("linhaDigitavel7"));
		obj.setLinhaDigitavel8(dadosSQL.getString("linhaDigitavel8"));
		obj.setTipoInscricaoFavorecido(dadosSQL.getString("tipoInscricaoFavorecido"));
		obj.setCnpjOuCpfFavorecido(dadosSQL.getString("cnpjOuCpfFavorecido"));
		obj.setInscricaoEstadualFavorecido(dadosSQL.getString("inscricaoestadualfavorecido"));
		obj.setInscricaoMunicipalFavorecido(dadosSQL.getString("inscricaomunicipalfavorecido"));
		obj.setNomeFavorecido(dadosSQL.getString("nomefavorecido"));
		obj.setEmailFavorecido(dadosSQL.getString("emailfavorecido"));
		obj.setComplementoFavorecido(dadosSQL.getString("complementofavorecido"));
		obj.setNumeroEnderecoFavorecido(dadosSQL.getString("numeroenderecofavorecido"));
		obj.setLogradouroFavorecido(dadosSQL.getString("logradourofavorecido"));
		obj.setBairroFavorecido(dadosSQL.getString("bairrofavorecido"));
		obj.setCepFavorecido(dadosSQL.getString("cepfavorecido"));
		obj.setCidadeFavorecido(dadosSQL.getString("cidadefavorecido"));
		obj.setEstadoFavorecido(dadosSQL.getString("estadofavorecido"));
		obj.getBancoRemessaPagar().setCodigo(dadosSQL.getInt("bancoremessapagar"));
		obj.getBancoRecebimento().setCodigo(dadosSQL.getInt("bancorecebimento"));

		obj.setContaCorrenteRecebimento(dadosSQL.getString("contacorrenterecebimento"));
		obj.setDigitoCorrenteRecebimento(dadosSQL.getString("digitocorrenterecebimento"));
		obj.setDigitoAgenciaRecebimento(dadosSQL.getString("digitoagenciarecebimento"));
		obj.setNumeroAgenciaRecebimento(dadosSQL.getString("numeroagenciarecebimento"));

        if(Uteis.isAtributoPreenchido(dadosSQL.getString("situacaoControleRemessaContaReceber"))){
        	obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.getEnum(dadosSQL.getString("situacaoControleRemessaContaReceber")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoServicoContaPagar"))){
        	obj.setTipoServicoContaPagar(TipoServicoContaPagarEnum.valueOf(dadosSQL.getString("tipoServicoContaPagar")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoLancamentoContaPagar"))){
        	obj.setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.valueOf(dadosSQL.getString("tipoLancamentoContaPagar")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("modalidadeTransferenciaBancariaEnum"))){
        	obj.setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum.valueOf(dadosSQL.getString("modalidadeTransferenciaBancariaEnum")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoContaEnum"))){
        	obj.setTipoContaEnum(TipoContaEnum.valueOf(dadosSQL.getString("tipoContaEnum")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("finalidadeDocEnum"))){
        	obj.setFinalidadeDocEnum(FinalidadeDocEnum.valueOf(dadosSQL.getString("finalidadeDocEnum")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("finalidadeTedEnum"))){
        	obj.setFinalidadeTedEnum(FinalidadeTedEnum.valueOf(dadosSQL.getString("finalidadeTedEnum")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoContribuinte"))){
        	obj.setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte.valueOf(dadosSQL.getString("tipoIdentificacaoContribuinte")));	
        }
        obj.setCodigoReceitaTributo(dadosSQL.getString("codigoReceitaTributo"));
        obj.setIdentificacaoContribuinte(dadosSQL.getString("identificacaoContribuinte"));
        obj.setNumeroReferencia(dadosSQL.getString("numeroReferencia"));
        obj.setValorReceitaBrutaAcumulada(dadosSQL.getDouble("valorReceitaBrutaAcumulada"));
        obj.setPercentualReceitaBrutaAcumulada(dadosSQL.getDouble("percentualReceitaBrutaAcumulada"));        
        if(Uteis.isAtributoPreenchido(dadosSQL.getInt("contapagar.codigo"))){
        	obj.getContaPagar().setCodigo(dadosSQL.getInt("contapagar.codigo"));
        	obj.setContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagar().getCodigo(),false, nivelMontarDados, usuario));
        }
        obj.setCodigoAgrupamentoContasPagar(dadosSQL.getString("codigoAgrupamentoContasPagar"));     
        obj.setObservacaoAgrupamento(dadosSQL.getString("observacaoAgrupamento")); 
        obj.setCodigoTransmissaoRemessanossonumero(dadosSQL.getString("transmissaonossonumero")); 
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("finalidadePixEnum"))){
        	obj.setFinalidadePixEnum(FinalidadePixEnum.valueOf(dadosSQL.getString("finalidadePixEnum")));	
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoChavePixEnum"))){
        	obj.setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("tipoIdentificacaoChavePixEnum")));	
        }
        obj.setChaveEnderecamentoPix(dadosSQL.getString("chaveEnderecamentoPix")); 
        obj.setIdentificacaoQRCODE(dadosSQL.getString("identificacaoQRCODE")); 
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
    
    /**
     * Consulta se existe algum {@link ContaPagarControleRemessaContaPagarVO} vinculado a conta a pagar
     * informada.
     * 
     * @param contaPagarVO
     * @return
     * @throws Exception
     */
    @Override
    public ContaPagarControleRemessaContaPagarVO consultarControleRemessaPorContaPagar(ContaPagarVO contaPagarVO) throws Exception {
    	StringBuilder sql  = new StringBuilder();
    	sql.append( "SELECT contapagarcontroleremessacontapagar.*, contapagar.codigo as \"contapagar.codigo\" FROM contapagarcontroleremessacontapagar");
    	sql.append(" left join contapagar on contapagar.codigo = contapagarcontroleremessacontapagar.contapagar ");    	
    	sql.append(" left join usuario on usuario.codigo = contapagarcontroleremessacontapagar.usuarioestorno ");
    	sql.append(" WHERE contapagarcontroleremessacontapagar.contapagar = ? ");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contaPagarVO.getCodigo());
    	if (tabelaResultado.next()) {
    		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
    	} else {
    		return null;
    	}
    	
    	
    	
    }
    
    
    @Override
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public List<ContaPagarControleRemessaContaPagarVO> consultarPorCodigoAgrupamento(String codigoAgrupamento, UsuarioVO usuario) throws Exception {
	    	StringBuilder sqlStr = new StringBuilder("");
	    	sqlStr.append("  select distinct cpcrcp.* ,");
	    	sqlStr.append("  cpcrcp.contapagar as \"contapagar.codigo\" ");
	    	sqlStr.append("  from	contapagarcontroleremessacontapagar cpcrcp " );
	    	sqlStr.append("  where codigoagrupamentocontaspagar ='").append(codigoAgrupamento).append("' ");
	    	sqlStr.append("  and contapagar  is not null  "); 
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	    	
	    }
    
    
     @Override
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public List<ContaPagarControleRemessaContaPagarVO> consultarPorCodigoTransmissaoNossoNumero(String codigoTransmissao, UsuarioVO usuario) throws Exception {
	    	StringBuilder sqlStr = new StringBuilder("");
	    	sqlStr.append("  select distinct cpcrcp.* ,");
	    	sqlStr.append("  cpcrcp.contapagar as \"contapagar.codigo\" ");
	    	sqlStr.append("  from	contapagarcontroleremessacontapagar cpcrcp " );
	    	sqlStr.append("  where transmissaonossonumero ='").append(codigoTransmissao).append("' ");
	    	sqlStr.append("  and contapagar  is not null  "); 
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	    	
	    }
     
     @Override
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public List<ContaPagarControleRemessaContaPagarVO> consultarPorNossoNumero(String nossonumero, UsuarioVO usuario) throws Exception {
	    	StringBuilder sqlStr = new StringBuilder("");
	    	sqlStr.append("  select distinct cpcrcp.* ,");
	    	sqlStr.append("  cpcrcp.contapagar as \"contapagar.codigo\" ");
	    	sqlStr.append("  from	contapagarcontroleremessacontapagar cpcrcp " );
	    	sqlStr.append("  where nossonumero ='").append(nossonumero).append("' ");
	    	sqlStr.append("  and contapagar  is not null  "); 
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	    	
	    }
   	
}
