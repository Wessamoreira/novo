package negocio.facade.jdbc.faturamento.nfe;

import java.io.File;
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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.CartaCorrecaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.CartaCorrecao;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.CartaCorrecaoNotaFiscalInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class CartaCorrecaoNotaFiscal extends ControleAcesso implements CartaCorrecaoNotaFiscalInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public CartaCorrecaoNotaFiscal() throws Exception {
		super();
		setIdEntidade("CartaCorrecaoNotaFiscal");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CartaCorrecaoNotaFiscal.idEntidade = idEntidade;
	}

	
	@Override
	public  List<CartaCorrecaoVO> consultarPorNotaFiscal(Integer nota, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder(); 
		sqlStr.append(" SELECT * ");
		sqlStr.append(" FROM cartacorrecao ");
		sqlStr.append(" WHERE notafiscalsaida = ").append(nota);
		sqlStr.append(" order by sequenciacorrecao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class })
	public List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		tabelaResultado = null;
		return vetResultado;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class })
	public CartaCorrecaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		CartaCorrecaoVO obj = new CartaCorrecaoVO();
		
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDataCorrecao(dadosSQL.getDate("dataCorrecao"));
		obj.setMotivo(dadosSQL.getString("motivo"));
		obj.setXmlEnvio(dadosSQL.getString("xmlenvio"));
		obj.setXmlRetorno(dadosSQL.getString("xmlretorno"));
		obj.getNotafiscalsaidaVO().setCodigo(dadosSQL.getInt("notafiscalsaida"));
		obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria((dadosSQL.getInt("usuario")), nivelMontarDados, usuarioLogado));
		obj.setSequenciaCorrecao(dadosSQL.getInt("sequenciaCorrecao"));
		obj.setMensagemRetorno(dadosSQL.getString("mensagemRetorno"));
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria((dadosSQL.getInt("unidadeensino")), false, nivelMontarDados, usuarioLogado));
		
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void transmitirCartaCorrecao(CartaCorrecaoVO cartaCorrecaoVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioLogado) throws Exception {
		try {
			String diretorioNotasEnviadasStr = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_CARTA_CORRECAO.getValue() + File.separator + cartaCorrecaoVO.getNotafiscalsaidaVO().getUnidadeEnsinoVO().getCodigo() + File.separator;
			File diretorioNotasEnviadas = new File(diretorioNotasEnviadasStr);
				if (!diretorioNotasEnviadas.exists()) {
					diretorioNotasEnviadas.mkdirs();
				}
				String nomeArquivoXML = diretorioNotasEnviadasStr + Uteis.getMontarCodigoBarra(cartaCorrecaoVO.getNotafiscalsaidaVO().getNumeroNota(), 9) + ".xml";
				if(Uteis.isAtributoPreenchido(cartaCorrecaoVO.getNotafiscalsaidaVO().getCodigo())){
					cartaCorrecaoVO.setXmlRetorno(CartaCorrecao.A1(
						cartaCorrecaoVO.getUnidadeEnsinoVO().getCidade().getEstado().getCodigoIBGE(), 
						nomeArquivoXML, 
						cartaCorrecaoVO.getNotafiscalsaidaVO().getChaveAcesso(),
						Uteis.removerMascara(cartaCorrecaoVO.getUnidadeEnsinoVO().getCNPJ()),
						Uteis.removeCaractersEspeciais(cartaCorrecaoVO.getMotivo().trim()), 
						cartaCorrecaoVO.getSequenciaCorrecao(),
						getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo(),
						cartaCorrecaoVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(),
						usuarioLogado));
				
				
					cartaCorrecaoVO.setXmlEnvio(UteisNfe.obterXMLNFeOriginalPorCaminho(nomeArquivoXML));

				}
				
				String codigoRetorno = UteisNfe.obterCodigoRetorno(cartaCorrecaoVO.getXmlRetorno());
				if (codigoRetorno.equals("135")) {
					cartaCorrecaoVO.setMensagemRetorno(cartaCorrecaoVO.getXmlRetorno().substring(cartaCorrecaoVO.getXmlRetorno().lastIndexOf("<xMotivo>") + 9, cartaCorrecaoVO.getXmlRetorno().lastIndexOf("</xMotivo>")));
					gravarCartaCorrecao(cartaCorrecaoVO, usuarioLogado);
				}else{
					throw new Exception(cartaCorrecaoVO.getXmlRetorno().substring(cartaCorrecaoVO.getXmlRetorno().lastIndexOf("<xMotivo>") + 9, cartaCorrecaoVO.getXmlRetorno().lastIndexOf("</xMotivo>")));
				}
				
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void gravarCartaCorrecao(final CartaCorrecaoVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			//incluir(getIdEntidade(), usuarioLogado);
			final StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" INSERT INTO cartacorrecao (  datacorrecao, motivo, xmlEnvio, xmlRetorno, usuario, unidadeensino, sequenciaCorrecao, notafiscalsaida, mensagemRetorno ) ");
			sqlStr.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());

					
					sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(2, obj.getMotivo());
					sqlInserir.setString(3, obj.getXmlEnvio());
					sqlInserir.setString(4, obj.getXmlRetorno());
					sqlInserir.setInt(5, obj.getUsuarioVO().getCodigo());
					sqlInserir.setInt(6, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setInt(7, obj.getSequenciaCorrecao());
					sqlInserir.setInt(8, obj.getNotafiscalsaidaVO().getCodigo());
					sqlInserir.setString(9, obj.getMensagemRetorno());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return 0;
				}
			}));

		} catch (Exception e) {
			throw e;
		}

	}

	public String getDesignIReportRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator +"CartaCorrecaoNFRel.jrxml";
	}
	
}