package negocio.facade.jdbc.patrimonio;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.FormaEntradaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.SituacaoOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagAdicionalEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagLocalArmazenamentoEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TextoPadraoPatrimonioTagPatrimonioUnidadeEnum;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TipoUsoTextoPadraoPatrimonioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.GeradorHtmlParaPdf;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisHTML;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisTextoPadrao;
import negocio.comuns.utilitarias.dominios.OrientacaoPaginaEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.patrimonio.TextoPadraoPatrimonioInterfaceFacade;

@Repository
public class TextoPadraoPatrimonio extends ControleAcesso implements TextoPadraoPatrimonioInterfaceFacade {

	/**
	 * 	
	 */
	private static final long serialVersionUID = 941020046331854413L;
	private static String idEntidade = "TextoPadraoPatrimonio";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, UsuarioVO usuarioVO) throws Exception {

		if (textoPadraoPatrimonioVO.isNovoObj()) {
			incluir(textoPadraoPatrimonioVO, usuarioVO);
		} else {
			alterar(textoPadraoPatrimonioVO, usuarioVO);
		}

	}

	@Override
	public void realizarClonagem(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) {
		textoPadraoPatrimonioVO.setCodigo(0);
		textoPadraoPatrimonioVO.setNovoObj(true);
		if (!textoPadraoPatrimonioVO.getNome().trim().isEmpty()) {
			textoPadraoPatrimonioVO.setNome(textoPadraoPatrimonioVO.getNome() + " - Clonada");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, UsuarioVO usuarioVO) throws Exception {

		try {
			TextoPadraoPatrimonio.incluir(getIdEntidade(), true, usuarioVO);
			validarDados(textoPadraoPatrimonioVO);
			final String sql = "INSERT INTO TextoPadraoPatrimonio (nome, textoPadrao, situacao , orientacaoPagina, margemDireita, margemEsquerda, margemSuperior, margemInferior, tipoUso, textoTopo, textoRodape, alturaTopo, alturaRodape) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			textoPadraoPatrimonioVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int x = 1;
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getNome());
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getTextoPadrao());
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getSituacao().name());
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getOrientacaoDaPagina().getKey());
					sqlInserir.setFloat(x++, textoPadraoPatrimonioVO.getMargemDireita());
					sqlInserir.setFloat(x++, textoPadraoPatrimonioVO.getMargemEsquerda());
					sqlInserir.setFloat(x++, textoPadraoPatrimonioVO.getMargemSuperior());
					sqlInserir.setFloat(x++, textoPadraoPatrimonioVO.getMargemInferior());
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getTipoUso().name());
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getTextoTopo());
					sqlInserir.setString(x++, textoPadraoPatrimonioVO.getTextoRodape());					
					sqlInserir.setFloat(x++, textoPadraoPatrimonioVO.getAlturaTopo());
					sqlInserir.setFloat(x++, textoPadraoPatrimonioVO.getAlturaRodape());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						textoPadraoPatrimonioVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			textoPadraoPatrimonioVO.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			textoPadraoPatrimonioVO.setNovoObj(true);
			textoPadraoPatrimonioVO.setCodigo(0);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, UsuarioVO usuarioVO) throws Exception {

		try {
			TextoPadraoPatrimonio.alterar(getIdEntidade(), true, usuarioVO);
			validarDados(textoPadraoPatrimonioVO);
			final String sql = "UPDATE TextoPadraoPatrimonio set nome=?, textoPadrao=?, situacao = ?, orientacaoPagina=?, margemDireita = ?, margemEsquerda = ?, margemSuperior = ?, margemInferior = ?, tipoUso = ?, textoTopo=?, textoRodape=?, alturaTopo=?, alturaRodape =?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int x = 1;
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getNome());
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getTextoPadrao());
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getSituacao().name());
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getOrientacaoDaPagina().getKey());
					sqlAlterar.setFloat(x++, textoPadraoPatrimonioVO.getMargemDireita());
					sqlAlterar.setFloat(x++, textoPadraoPatrimonioVO.getMargemEsquerda());
					sqlAlterar.setFloat(x++, textoPadraoPatrimonioVO.getMargemSuperior());
					sqlAlterar.setFloat(x++, textoPadraoPatrimonioVO.getMargemInferior());
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getTipoUso().name());
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getTextoTopo());
					sqlAlterar.setString(x++, textoPadraoPatrimonioVO.getTextoRodape());
					sqlAlterar.setFloat(x++, textoPadraoPatrimonioVO.getAlturaTopo());
					sqlAlterar.setFloat(x++, textoPadraoPatrimonioVO.getAlturaRodape());
					sqlAlterar.setInt(x++, textoPadraoPatrimonioVO.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDados(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) throws ConsistirException {
		if (textoPadraoPatrimonioVO.getNome().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoPadraoPatrimonio_nome"));
		}
		if (textoPadraoPatrimonioVO.getTextoPadrao().trim().isEmpty() || UteisHTML.realizarRemocaoTodasTagsHTML(textoPadraoPatrimonioVO.getTextoPadrao()).trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoPadraoPatrimonio_textoPadrao"));
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, UsuarioVO usuarioVO) throws Exception {
		try {
			TextoPadraoPatrimonio.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM TextoPadraoPatrimonio WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, textoPadraoPatrimonioVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public List<TextoPadraoPatrimonioVO> consultar(String campoConsulta, String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		List<TextoPadraoPatrimonioVO> objs = new ArrayList<TextoPadraoPatrimonioVO>();
		validarDadosConsulta(valorConsulta);
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta.equals("")) {
				valorConsulta = ("0");
			}
			int valorInt = Integer.parseInt(valorConsulta);
			objs = consultarPorCodigo(new Integer(valorInt), limite, offset, controlarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("descricao")) {
			objs = consultarPorNome(valorConsulta, limite, offset, controlarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("situacao")) {
			objs = consultarPorSituacao(valorConsulta, limite, offset, controlarAcesso, usuarioVO);
		}
		return objs;
	}

	@Override
	public Integer consultarTotal(String campoConsulta, String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		Integer count = 0;
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta.equals("")) {
				valorConsulta = ("0");
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return count = consultarTotalPorChavePrimaria(new Integer(valorInt), verificarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("descricao")) {
			return count = consultarTotalPorNome(valorConsulta, verificarAcesso, usuarioVO);
		}
		if (campoConsulta.equals("situacao")) {
			if (valorConsulta.equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
			}
			return count = consultarTotalPorSituacao(valorConsulta, verificarAcesso, usuarioVO);
		}
		return count;

	}

	public List<TextoPadraoPatrimonioVO> montarDadosConsultar(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception {
		List<TextoPadraoPatrimonioVO> textoPadraoPatrimonioVOs = new ArrayList<TextoPadraoPatrimonioVO>(0);
		while (rs.next()) {
			textoPadraoPatrimonioVOs.add(montarDados(rs, usuarioVO));
		}
		return textoPadraoPatrimonioVOs;
	}

	public TextoPadraoPatrimonioVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception {
		TextoPadraoPatrimonioVO textoPadraoPatrimonioVO = new TextoPadraoPatrimonioVO();
		textoPadraoPatrimonioVO.setNovoObj(false);
		textoPadraoPatrimonioVO.setCodigo(rs.getInt("codigo"));
		textoPadraoPatrimonioVO.setNome(rs.getString("nome"));
		textoPadraoPatrimonioVO.setTextoPadrao(rs.getString("textoPadrao"));
		textoPadraoPatrimonioVO.setOrientacaoDaPagina(OrientacaoPaginaEnum.getEnum(rs.getString("orientacaoPagina")));
		textoPadraoPatrimonioVO.setMargemDireita(rs.getFloat("margemDireita"));
		textoPadraoPatrimonioVO.setMargemEsquerda(rs.getFloat("margemEsquerda"));
		textoPadraoPatrimonioVO.setMargemSuperior(rs.getFloat("margemSuperior"));
		textoPadraoPatrimonioVO.setMargemInferior(rs.getFloat("margemInferior"));
		textoPadraoPatrimonioVO.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
		textoPadraoPatrimonioVO.setTipoUso(TipoUsoTextoPadraoPatrimonioEnum.valueOf(rs.getString("tipoUso")));
		textoPadraoPatrimonioVO.setTextoTopo(rs.getString("textoTopo"));
		textoPadraoPatrimonioVO.setTextoRodape(rs.getString("textoRodape"));
		textoPadraoPatrimonioVO.setAlturaRodape(rs.getFloat("alturaRodape"));
		textoPadraoPatrimonioVO.setAlturaTopo(rs.getFloat("alturaTopo"));
		return textoPadraoPatrimonioVO;
	}

	public TextoPadraoPatrimonioVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select * from TextoPadraoPatrimonio ");
		sql.append(" WHERE codigo = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		throw new Exception(UteisJSF.internacionalizar("msg_DadosNaoEncontrado").replace("{0}", "TEXTO PADRÃO PATRIMÔNIO"));
	}

	public List<TextoPadraoPatrimonioVO> consultarPorNome(String nome, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		TextoPadraoPatrimonio.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("select * from TextoPadraoPatrimonio ");
		sql.append(" WHERE nome ilike '").append(nome).append("%' ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultar(rs, usuarioVO);
	}

	public List<TextoPadraoPatrimonioVO> consultarPorCodigo(Integer codigo, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		TextoPadraoPatrimonio.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("select * from TextoPadraoPatrimonio ");
		sql.append(" WHERE codigo = ").append(codigo).append(" ");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultar(rs, usuarioVO);
	}

	public List<TextoPadraoPatrimonioVO> consultarPorSituacao(String situacao, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		TextoPadraoPatrimonio.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("select * from TextoPadraoPatrimonio ");
		sql.append(" WHERE situacao ilike  ").append("'").append(situacao).append("'");
		if (limite != null) {
			sql.append(" LIMIT ").append(limite);
			if (offset != null) {
				sql.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultar(rs, usuarioVO);
	}

	@Override
	public List<TextoPadraoPatrimonioVO> consultarPorSituacaoTipoUsoCombobox(StatusAtivoInativoEnum situacao, TipoUsoTextoPadraoPatrimonioEnum tipoUso) throws Exception {
		StringBuilder sql = new StringBuilder("select codigo, nome from TextoPadraoPatrimonio ");
		sql.append(" WHERE situacao =  '").append(situacao.name()).append("'");
		sql.append(" and tipoUso = '").append(tipoUso.name()).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TextoPadraoPatrimonioVO> textoPadraoPatrimonioVOs = new ArrayList<TextoPadraoPatrimonioVO>(0);
		while (rs.next()) {
			TextoPadraoPatrimonioVO obj = new TextoPadraoPatrimonioVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNome(rs.getString("nome"));
			textoPadraoPatrimonioVOs.add(obj);
		}
		return textoPadraoPatrimonioVOs;
	}

	private Integer consultarTotalPorChavePrimaria(Integer codigo, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT (textopadraopatrimonio.codigo) FROM textopadraopatrimonio WHERE (codigo = ?) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}

	private Integer consultarTotalPorNome(String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT (textopadraopatrimonio.codigo) FROM textopadraopatrimonio ");
		sqlStr.append(" WHERE nome ilike '").append(valorConsulta).append("%' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}

	private Integer consultarTotalPorSituacao(String valorConsulta, boolean verificarAcesso, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT (textopadraopatrimonio.codigo) FROM textopadraopatrimonio ");
		sqlStr.append(" WHERE situacao ilike  ").append("'").append(valorConsulta).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("count");
		}
		return 0;
	}

	public static String getIdEntidade() {
		if (TextoPadraoPatrimonio.idEntidade == null) {
			TextoPadraoPatrimonio.idEntidade = "TextoPadraoPatrimonio";
		}
		return TextoPadraoPatrimonio.idEntidade;
	}

	public void validarDadosConsulta(String valorConsulta) throws Exception {
		if (valorConsulta.equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoTextoPadraoPatrimonio(final TextoPadraoPatrimonioVO textoPadraoPatrimonioVO) {
		final String sqlStr = "UPDATE textopadraopatrimonio set situacao=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());

				sqlAlterar.setString(1, textoPadraoPatrimonioVO.getSituacao().name());
				sqlAlterar.setInt(2, textoPadraoPatrimonioVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public String realizarImpressaoTextoVisualizacao(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		PatrimonioVO patrimonioVO = inicializarDadosPatrimonioTesteImpressaoTextoPadrao();
		OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO = inicializarDadosOcorrenciaPatrimonioTesteImpressaoTextoPadrao();
		List<PatrimonioUnidadeVO> patrimonioUnidadeVOs = inicializarDadosPatrimonioUnidadeTesteImpressaoTextoPadrao();
		LocalArmazenamentoVO localArmazenamentoVO = inicializarDadosLocalArmazenamentoTesteImpressaoTextoPadrao();
		ocorrenciaPatrimonioVO.setPatrimonioUnidade(patrimonioUnidadeVOs.get(0));
		textoPadraoPatrimonioVO.setNivelMontarDados(NivelMontarDados.TODOS);
		return realizarImpressaoTextoPadraoPatrimonio(textoPadraoPatrimonioVO, patrimonioVO, ocorrenciaPatrimonioVO, patrimonioUnidadeVOs, localArmazenamentoVO, configuracaoGeralSistemaVO, usuarioVO);
	}
	private List<PatrimonioUnidadeVO> inicializarDadosPatrimonioUnidadeTesteImpressaoTextoPadrao(){
		List<PatrimonioUnidadeVO> patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		PatrimonioUnidadeVO patrimonioUnidadeVO = null; 
		for(int x = 1; x<=10;x++){
			patrimonioUnidadeVO = new PatrimonioUnidadeVO();
			patrimonioUnidadeVO.setCodigo(x);
			patrimonioUnidadeVO.setCodigoBarra("0000000"+x);
			patrimonioUnidadeVO.getLocalArmazenamento().setLocalArmazenamento("LOCAL ARMAZEMANETO");
			patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamentoSuperiorVO().setLocalArmazenamento("LOCAL ARMAZEMANETO SUPERIOR");
			patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().setNome("Unidade LOCAL ARMAZEMANETO");
			patrimonioUnidadeVO.setNumeroDeSerie("123456");
			patrimonioUnidadeVO.setPatrimonioVO(inicializarDadosPatrimonioTesteImpressaoTextoPadrao());
			patrimonioUnidadeVO.setPermiteReserva(true);
			patrimonioUnidadeVO.setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.DISPONIVEL);
			patrimonioUnidadeVO.setUnidadeLocado(true);
			patrimonioUnidadeVO.setValorRecurso(BigDecimal.valueOf(100.0));			
			patrimonioUnidadeVOs.add(patrimonioUnidadeVO);
		}
		return patrimonioUnidadeVOs;
	}
	
	private LocalArmazenamentoVO inicializarDadosLocalArmazenamentoTesteImpressaoTextoPadrao(){
		LocalArmazenamentoVO localArmazenamentoVO = new LocalArmazenamentoVO();
		localArmazenamentoVO.setCodigo(1);		
		localArmazenamentoVO.setLocalArmazenamento("LOCAL ARMAZENAMENTO TESTE");
		localArmazenamentoVO.getLocalArmazenamentoSuperiorVO().setLocalArmazenamento("LOCAL ARMAZENAMENTO SUPERIOR TESTE");
		localArmazenamentoVO.getUnidadeEnsinoVO().setNome("UNIDADE ENSINO LOCAL ARMAZENAMENTO TESTE");	
		return localArmazenamentoVO;
	}
	
	private PatrimonioVO inicializarDadosPatrimonioTesteImpressaoTextoPadrao(){
		PatrimonioVO patrimonioVO = new PatrimonioVO();
		patrimonioVO.setCodigo(1);
		patrimonioVO.setDataCadastro(new Date());
		patrimonioVO.setDataEntrada(new Date());
		patrimonioVO.setDescricao("PATRIMONIO TESTE TEXTO PADRÃO PATRIMONIO");
		patrimonioVO.setFormaEntradaPatrimonio(FormaEntradaPatrimonioEnum.OUTRA);
		patrimonioVO.getFornecedorVO().setNome("FORNECEDOR TESTE");
		patrimonioVO.setMarca("MARCA");
		patrimonioVO.setModelo("MODELO");
		patrimonioVO.setNotaFiscal("123456789");
		patrimonioVO.getTipoPatrimonioVO().setDescricao("TIPO PATRIMONIO TESTE");
		return patrimonioVO;
	}
	
	private OcorrenciaPatrimonioVO inicializarDadosOcorrenciaPatrimonioTesteImpressaoTextoPadrao(){
		OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
		ocorrenciaPatrimonioVO.setCodigo(1);
		ocorrenciaPatrimonioVO.setDataFinalizacao(new Date());
		ocorrenciaPatrimonioVO.setDataOcorrencia(new Date());
		ocorrenciaPatrimonioVO.setDataPrevisaoDevolucao(new Date());
		ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setLocalArmazenamento("LOCAL DESTINO");;
		ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().setLocalArmazenamento("LOCAL ORIGEM");;
		ocorrenciaPatrimonioVO.setMotivo("MOTIVO TESTE");;
		ocorrenciaPatrimonioVO.setObservacao("OBSERVAÇAO TESTE");
		ocorrenciaPatrimonioVO.getResponsavelFinalizacao().setNome("RESPONSAVEL FINALIZACAO");
		ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setNome("RESPONSAVEL OCORRENCIA");
		ocorrenciaPatrimonioVO.setSituacao(SituacaoOcorrenciaPatrimonioEnum.EM_ANDAMENTO);
		ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().getPessoa().setNome("SOLICITANTE EMPRESTIMO");
		ocorrenciaPatrimonioVO.setTipoOcorrenciaPatrimonio(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO);		
		return ocorrenciaPatrimonioVO;
	}

	@Override
	public String realizarImpressaoTextoPadraoPatrimonio(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, PatrimonioVO patrimonioVO, OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, LocalArmazenamentoVO localArmazenamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if(!textoPadraoPatrimonioVO.getNivelMontarDados().equals(NivelMontarDados.TODOS)){
			textoPadraoPatrimonioVO = consultarPorChavePrimaria(textoPadraoPatrimonioVO.getCodigo(), usuarioVO);
		}
		inicializarDadosImpressaoTextoPadraoPatrimonio(textoPadraoPatrimonioVO, patrimonioVO, ocorrenciaPatrimonioVO, patrimonioUnidadeVOs, usuarioVO);
		String topo = textoPadraoPatrimonioVO.getTextoTopo();
		//topo = UteisHTML.realizarSubstituicaoValorAtribuidoStyle(topo, "<div id=\"conteudo\" style=\"", 19 ,  ">", 1 , ">");
		topo = realizarSubstituicaoTags(textoPadraoPatrimonioVO, topo, patrimonioVO, ocorrenciaPatrimonioVO, patrimonioUnidadeVOs, localArmazenamentoVO, configuracaoGeralSistemaVO, usuarioVO);
		String rodape = textoPadraoPatrimonioVO.getTextoRodape();		
		//rodape = UteisHTML.realizarSubstituicaoValorAtribuidoStyle(rodape, "<div id=\"conteudo\" style=\"", 19,  ">", 1 , ">");
		rodape = realizarSubstituicaoTags(textoPadraoPatrimonioVO, rodape, patrimonioVO, ocorrenciaPatrimonioVO, patrimonioUnidadeVOs, localArmazenamentoVO, configuracaoGeralSistemaVO, usuarioVO);
		String conteudo = textoPadraoPatrimonioVO.getTextoPadrao();
		//conteudo = UteisHTML.realizarSubstituicaoValorAtribuidoStyle(conteudo, "<div id=\"conteudo\" style=\"", 19,  ">", 1 , ">");
		conteudo = realizarSubstituicaoTags(textoPadraoPatrimonioVO, conteudo, patrimonioVO, ocorrenciaPatrimonioVO, patrimonioUnidadeVOs, localArmazenamentoVO, configuracaoGeralSistemaVO, usuarioVO);
		String nomeArquivo = "TEXTO_PADRAO_" + Uteis.getData(new Date(), "dd_MM_yy_hh_mm_ss") + "_" + usuarioVO.getCodigo() + ".pdf";
		String arquivo = UteisJSF.getCaminhoWeb() + "relatorio/"+nomeArquivo;		
		GeradorHtmlParaPdf geradorHtmlParaPdf = new GeradorHtmlParaPdf(topo, rodape, conteudo);

		geradorHtmlParaPdf.setMarginBottom(UteisTextoPadrao.converterCentimetroParaPontos(textoPadraoPatrimonioVO.getMargemInferior()));
		geradorHtmlParaPdf.setMarginTop(UteisTextoPadrao.converterCentimetroParaPontos(textoPadraoPatrimonioVO.getMargemSuperior()));
		geradorHtmlParaPdf.setMarginRight(UteisTextoPadrao.converterCentimetroParaPontos(textoPadraoPatrimonioVO.getMargemDireita()));
		geradorHtmlParaPdf.setMarginLeft(UteisTextoPadrao.converterCentimetroParaPontos(textoPadraoPatrimonioVO.getMargemEsquerda()));
		geradorHtmlParaPdf.setSizeTopo(UteisTextoPadrao.converterCentimetroParaPontos(textoPadraoPatrimonioVO.getAlturaTopo()));
		geradorHtmlParaPdf.setSizeRodape(UteisTextoPadrao.converterCentimetroParaPontos(textoPadraoPatrimonioVO.getAlturaRodape()));
		
		if (textoPadraoPatrimonioVO.getOrientacaoDaPagina().equals(OrientacaoPaginaEnum.RETRATO)) {
			geradorHtmlParaPdf.setPageSize(PageSize.A4);
		} else {
			geradorHtmlParaPdf.setPageSize(new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth()));
		}
		geradorHtmlParaPdf.realizarGeracaoDocumentoPdf(arquivo);

		return nomeArquivo;
	}	

	public String realizarSubstituicaoTags(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, String textoBase, PatrimonioVO patrimonioVO, OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, LocalArmazenamentoVO localArmazenamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		textoBase = UteisTextoPadrao.realizarSubstituicaoUrlImagem(textoBase, configuracaoGeralSistemaVO);
		textoBase = realizarSubstituicaoTagPatrimonio(textoBase, patrimonioVO, patrimonioUnidadeVOs);
		textoBase = realizarSubstituicaoTagLocalArmazenamento(textoBase, localArmazenamentoVO);
		textoBase = realizarSubstituicaoTagListaUnidadePatrimonio(textoBase, patrimonioUnidadeVOs);
		textoBase = realizarSubstituicaoTagAdicional(textoBase, usuarioLogado);
		if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO) && !textoPadraoPatrimonioVO.getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO) && !textoPadraoPatrimonioVO.getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.MAPA_DESCARTE)) {
			textoBase = realizarSubstituicaoTagOcorrenciaPatrimonio(textoBase, ocorrenciaPatrimonioVO);
			textoBase = realizarSubstituicaoTagUnidadePatrimonio(textoBase, ocorrenciaPatrimonioVO.getPatrimonioUnidade());
		}
		return textoBase;
	}

	public String realizarSubstituicaoTagPatrimonio(String textoPadrao, PatrimonioVO patrimonioVO, List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) throws Exception {
		if (Uteis.isAtributoPreenchido(patrimonioVO)) {
			for (TextoPadraoPatrimonioTagPatrimonioEnum tag : TextoPadraoPatrimonioTagPatrimonioEnum.values()) {
				switch (tag) {
					case PATRIMONIO_CODIGO:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getCodigo()));
						break;
					case PATRIMONIO_DATA_ENTRADA:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getDataEntrada()));
						break;
					case PATRIMONIO_DESCRICAO:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getDescricao()));
						break;
//					case PATRIMONIO_FORMA_ENTRADA:
//						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getFormaEntradaPatrimonio()));
//						break;
					case PATRIMONIO_FORNECEDOR:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getFornecedorVO().getNome()));
						break;
					case PATRIMONIO_MARCA:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getMarca()));
						break;
					case PATRIMONIO_MODELO:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getModelo()));
						break;
					case PATRIMONIO_NOTA_FISCAL:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getNotaFiscal()));
						break;
					case PATRIMONIO_TIPO_PATRIMONIO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioVO.getTipoPatrimonioVO().getDescricao()));
						break;
					case PATRIMONIO_UNIDADE:
						textoPadrao = realizarSubstituicaoTagListaUnidadePatrimonio(textoPadrao, patrimonioUnidadeVOs);
						break;					
				}
			}
		}
		return textoPadrao;
	}
	
	public String realizarSubstituicaoTagAdicional(String textoPadrao, UsuarioVO usuarioVO) throws Exception {
		
			for (TextoPadraoPatrimonioTagAdicionalEnum tag : TextoPadraoPatrimonioTagAdicionalEnum.values()) {
				switch (tag) {
					case DATA_ATUAL:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, Uteis.getData(new Date())));
						break;
					case DATA_EXTENSO_ATUAL:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, Uteis.getDataCidadeDiaMesPorExtensoEAno("", new Date(), true)));
						break;
					case NOME_USUARIO:
						textoPadrao= (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, usuarioVO.getNome()));
						break;						
				}
			}
		
		return textoPadrao;
	}

	public String realizarSubstituicaoTagUnidadePatrimonio(String textoPadrao, PatrimonioUnidadeVO patrimonioUnidadeVO) throws Exception {
		if (Uteis.isAtributoPreenchido(patrimonioUnidadeVO)) {
			for (TextoPadraoPatrimonioTagPatrimonioUnidadeEnum tag : TextoPadraoPatrimonioTagPatrimonioUnidadeEnum.values()) {
				switch (tag) {
					case PATRIMONIOUNIDADE_CODIGO_BARRA:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getCodigoBarra()));
						break;
					case PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamento()));
						break;
					case PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO_SUPERIOR:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamentoSuperiorVO().getLocalArmazenamento()));
						break;
					case PATRIMONIOUNIDADE_NUMERO_SERIE:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getNumeroDeSerie()));
						break;
					case PATRIMONIOUNIDADE_PERMITE_LOCACAO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getUnidadeLocado()));
						break;
					case PATRIMONIOUNIDADE_PERMITE_RESERVA:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getPermiteReserva()));
						break;
					case PATRIMONIOUNIDADE_SITUACAO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getSituacaoPatrimonioUnidade()));
						break;
					case PATRIMONIOUNIDADE_UNIDADE_ENSINO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().getNome()));
						break;
					case PATRIMONIOUNIDADE_VALOR:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, patrimonioUnidadeVO.getValorRecurso()));
						break;					
				}
			}
		}
		return textoPadrao;
	}
	
	public String realizarSubstituicaoTagLocalArmazenamento(String textoPadrao, LocalArmazenamentoVO localArmazenamentoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(localArmazenamentoVO)) {
			for (TextoPadraoPatrimonioTagLocalArmazenamentoEnum tag : TextoPadraoPatrimonioTagLocalArmazenamentoEnum.values()) {
				switch (tag) {
					
					case LOCAL_NOME_ARMAZENAMENTO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, localArmazenamentoVO.getLocalArmazenamento()));
						break;
					case LOCAL_NOME_ARMAZENAMENTO_SUPERIOR:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, localArmazenamentoVO.getLocalArmazenamentoSuperiorVO().getLocalArmazenamento()));
						break;					
					case LOCAL_NOME_UNIDADE_ENSINO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, localArmazenamentoVO.getUnidadeEnsinoVO().getNome()));
						break;									
				}
			}
		}
		return textoPadrao;
	}

	public String realizarSubstituicaoTagListaUnidadePatrimonio(String textoPadrao, List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) throws Exception {
		if(textoPadrao.contains(UteisTextoPadrao.realizarObtencaoTag(TextoPadraoPatrimonioTagPatrimonioEnum.PATRIMONIO_UNIDADE.getTag()))){			
			String tagUsar = UteisTextoPadrao.realizarObtencaoTag(TextoPadraoPatrimonioTagPatrimonioEnum.PATRIMONIO_UNIDADE.getTag());
			Pattern pTag = Pattern.compile("\\[(.*?)"+tagUsar+".*?\\]");
			Matcher mTag = pTag.matcher(textoPadrao);
			
			while(mTag.find()){
				StringBuilder textoPadraoSubstituir = new StringBuilder("");
				String[] subtags = UteisTextoPadrao.realizarObtecaoSubTags(mTag.group());
				if (subtags != null && subtags.length > 0) {
					textoPadraoSubstituir.append("<table style=\"width:100%\">");
					textoPadraoSubstituir.append("<colgroup span=\"").append(subtags.length).append("\"></colgroup>");
					// cria o header da tabela
					textoPadraoSubstituir.append("<thead><tr>");
					for (String subTag : subtags) {						
						int tamanho = UteisTextoPadrao.realizarObtecaoTamanhoTag(subTag);
						String cabecalhoColuna = UteisTextoPadrao.realizarObtecaoTextoTag(subTag);
						textoPadraoSubstituir.append("<th style=\"width:").append(tamanho).append("%\">").append(cabecalhoColuna).append("</th>");
					}
					textoPadraoSubstituir.append("</tr></thead>");
					// cria o corpo da tabela
					textoPadraoSubstituir.append("<tbody>");
					if(patrimonioUnidadeVOs != null){
					for (PatrimonioUnidadeVO patrimonioUnidadeVO : patrimonioUnidadeVOs) {
						textoPadraoSubstituir.append("<tr>");
						for (String subTag : subtags) {
							TextoPadraoPatrimonioTagPatrimonioUnidadeEnum tagEnum = TextoPadraoPatrimonioTagPatrimonioUnidadeEnum.valueOf(UteisTextoPadrao.realizarObtencaoTag(subTag));							
								switch (tagEnum) {
									case PATRIMONIOUNIDADE_CODIGO_BARRA:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getCodigoBarra()).append("</td>");										
										break;
									case PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamento()).append("</td>");										
										break;
									case PATRIMONIOUNIDADE_LOCAL_ARMAZENAMENTO_SUPERIOR:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getLocalArmazenamento().getLocalArmazenamentoSuperiorVO().getLocalArmazenamento()).append("</td>");										
										break;
									case PATRIMONIOUNIDADE_NUMERO_SERIE:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getNumeroDeSerie()).append("</td>");										
										break;
									case PATRIMONIOUNIDADE_PERMITE_LOCACAO:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getUnidadeLocado()?"X":"").append("</td>");										
										break;
									case PATRIMONIOUNIDADE_PERMITE_RESERVA:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getPermiteReserva()?"X":"").append("</td>");										
										break;
									case PATRIMONIOUNIDADE_SITUACAO:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getSituacaoPatrimonioUnidade().getValorApresentar()).append("</td>");										
										break;
									case PATRIMONIOUNIDADE_UNIDADE_ENSINO:
										textoPadraoSubstituir.append("<td>").append(patrimonioUnidadeVO.getLocalArmazenamento().getUnidadeEnsinoVO().getNome()).append("</td>");
										break;
									case PATRIMONIOUNIDADE_VALOR:
										textoPadraoSubstituir.append("<td>").append(Uteis.getDoubleFormatado(patrimonioUnidadeVO.getValorRecurso().doubleValue())).append("</td>");										
										break;	
										
								}							
						}
						textoPadraoSubstituir.append("</tr>");
					}
					}
					textoPadraoSubstituir.append("</tbody>");
					textoPadraoSubstituir.append("</table>");
				}
				textoPadrao = textoPadrao.replace(mTag.group(), textoPadraoSubstituir.toString());
			}
		}
		return textoPadrao;
	}

	public String realizarSubstituicaoTagOcorrenciaPatrimonio(String textoPadrao, OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO) throws Exception{
		if (Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO)) {
			for (TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum tag : TextoPadraoPatrimonioTagOcorrenciaPatrimonioEnum.values()) {
				switch (tag) {
					case PATRIMONIO_OCORRENCIA_CODIGO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getCodigo()));
						break;
					case PATRIMONIO_OCORRENCIA_DATA_FINALIZACAO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getDataFinalizacao()));
						break;
					case PATRIMONIO_OCORRENCIA_DATA_OCORRENCIA:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getDataOcorrencia()));
						break;
					case PATRIMONIO_OCORRENCIA_DATA_PREVISAO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getDataPrevisaoDevolucao()));
						break;
					case PATRIMONIO_OCORRENCIA_LOCAL_DESTINO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().getLocalArmazenamento()));
						break;
					case PATRIMONIO_OCORRENCIA_LOCAL_ORIGEM:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getLocalArmazenamentoOrigem().getLocalArmazenamento()));
						break;
					case PATRIMONIO_OCORRENCIA_MOTIVO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getMotivo()));
						break;
					case PATRIMONIO_OCORRENCIA_OBSERVACAO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getObservacao()));
						break;
					case PATRIMONIO_OCORRENCIA_RESPONSAVEL_OCORRENCIA:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getResponsavelOcorrencia().getNome()));
						break;					
					case PATRIMONIO_OCORRENCIA_SITUACAO_OCORRENCIA:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getSituacao()));
						break;					
					case PATRIMONIO_OCORRENCIA_SOLICITANTE_EMPRESTIMO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getSolicitanteEmprestimo().getPessoa().getNome()));
						break;					
					case PATRIMONIO_OCORRENCIA_TIPO_OCORRENCIA:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio()));
						break;		
					case PATRIMONIO_OCORRENCIA_DATA_INICIO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getDataInicioReserva()));
						break;
					case PATRIMONIO_OCORRENCIA_DATA_TERMINO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getDataTerminoReserva()));
						break;
					case PATRIMONIO_OCORRENCIA_LOCAL_RESERVADO:
						textoPadrao = (UteisTextoPadrao.realizarSubstituicaoTag(textoPadrao, tag, ocorrenciaPatrimonioVO.getLocalReservado().getLocalArmazenamento()));
						break;
				}
			}
			
		}
		return textoPadrao;

	}

	public void inicializarDadosImpressaoTextoPadraoPatrimonio(TextoPadraoPatrimonioVO textoPadraoPatrimonioVO, PatrimonioVO patrimonioVO, OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO, List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(textoPadraoPatrimonioVO) && !textoPadraoPatrimonioVO.getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoPadraoPatrimonio_selecionarTextoImpressao"));
		}
		if (textoPadraoPatrimonioVO.getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.CADASTRO_PATRIMONIO) && !Uteis.isAtributoPreenchido(patrimonioVO)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoPadraoPatrimonio_patrimonio"));
		} else if (textoPadraoPatrimonioVO.getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.MAPA_DESCARTE) && !Uteis.isAtributoPreenchido(patrimonioUnidadeVOs)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoPadraoPatrimonio_patrimonio"));
		} else if (!textoPadraoPatrimonioVO.getTipoUso().equals(TipoUsoTextoPadraoPatrimonioEnum.MAPA_DESCARTE) && !Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO) && !Uteis.isAtributoPreenchido(patrimonioVO)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TextoPadraoPatrimonio_patrimonio"));
		}
		
		textoPadraoPatrimonioVO.setTextoPadraoImpressao(textoPadraoPatrimonioVO.getTextoPadrao());

	}
}
