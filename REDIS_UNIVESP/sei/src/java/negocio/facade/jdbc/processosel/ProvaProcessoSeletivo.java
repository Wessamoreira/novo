package negocio.facade.jdbc.processosel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoProvaProcessoSeletivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.ProvaProcessoSeletivoInterfaceFacade;

@Repository
public class ProvaProcessoSeletivo extends ControleAcesso implements ProvaProcessoSeletivoInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public void persistir(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
		validarDados(provaProcessoSeletivoVO);
		if (provaProcessoSeletivoVO.isNovoObj()) {
			incluir(provaProcessoSeletivoVO, controlarAcesso, idEntidade, usuarioVO);
		} else {
			alterar(provaProcessoSeletivoVO, controlarAcesso, idEntidade, usuarioVO);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
		incluir(idEntidade, controlarAcesso, usuarioVO);
		provaProcessoSeletivoVO.setDataCriacao(new Date());
		provaProcessoSeletivoVO.setResponsavelCriacao(usuarioVO);
		try {
			final StringBuilder sql = new StringBuilder("INSERT INTO provaProcessoSeletivo ");
			sql.append(" (situacaoProvaProcessoSeletivo, descricao, ");
			sql.append(" dataCriacao, responsavelCriacao, grupoDisciplinaProcSeletivo, possuiRedacao, temaRedacao, quantidadeMaximaCaracteresRedacao");
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?) ");
			sql.append(" returning codigo ");
			provaProcessoSeletivoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, provaProcessoSeletivoVO.getSituacaoProvaProcessoSeletivo().name());
					ps.setString(x++, provaProcessoSeletivoVO.getDescricao());
					ps.setDate(x++, Uteis.getDataJDBC(provaProcessoSeletivoVO.getDataCriacao()));
					ps.setInt(x++, provaProcessoSeletivoVO.getResponsavelCriacao().getCodigo());
					ps.setInt(x++, provaProcessoSeletivoVO.getGrupoDisciplinaProcSeletivoVO().getCodigo());
					ps.setBoolean(x++, provaProcessoSeletivoVO.getPossuiRedacao());
					ps.setString(x++, provaProcessoSeletivoVO.getTemaRedacao());
					ps.setInt(x++, provaProcessoSeletivoVO.getQuantidadeMaximaCaracteresRedacao());
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}

			}));
			getFacadeFactory().getQuestaoProvaProcessoSeletivoFacade().incluirQuestaoProvaProcessoSeletivo(provaProcessoSeletivoVO);
			provaProcessoSeletivoVO.setNovoObj(false);
		} catch (Exception e) {
			provaProcessoSeletivoVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
		alterar(idEntidade, controlarAcesso, usuarioVO);
		provaProcessoSeletivoVO.setDataAlteracao(new Date());
		provaProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
		try {
			final StringBuilder sql = new StringBuilder("UPDATE provaProcessoSeletivo set ");
			sql.append(" situacaoProvaProcessoSeletivo = ?, descricao = ?, ");
			sql.append(" dataAlteracao = ?, responsavelAlteracao = ?, grupoDisciplinaProcSeletivo=?, possuiRedacao=?, temaRedacao=?, quantidadeMaximaCaracteresRedacao=? ");
			sql.append(" where codigo = ? ");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, provaProcessoSeletivoVO.getSituacaoProvaProcessoSeletivo().name());
					ps.setString(x++, provaProcessoSeletivoVO.getDescricao());
					ps.setDate(x++, Uteis.getDataJDBC(provaProcessoSeletivoVO.getDataAlteracao()));
					ps.setInt(x++, provaProcessoSeletivoVO.getResponsavelAlteracao().getCodigo());
					ps.setInt(x++, provaProcessoSeletivoVO.getGrupoDisciplinaProcSeletivoVO().getCodigo());
					ps.setBoolean(x++, provaProcessoSeletivoVO.getPossuiRedacao());
					ps.setString(x++, provaProcessoSeletivoVO.getTemaRedacao());
					ps.setInt(x++, provaProcessoSeletivoVO.getQuantidadeMaximaCaracteresRedacao());
					ps.setInt(x++, provaProcessoSeletivoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(provaProcessoSeletivoVO, controlarAcesso, idEntidade, usuarioVO);
				return;
			}
			getFacadeFactory().getQuestaoProvaProcessoSeletivoFacade().alterarQuestaoProvaProcessoSeletivo(provaProcessoSeletivoVO);
			provaProcessoSeletivoVO.setNovoObj(false);
		} catch (Exception e) {
			provaProcessoSeletivoVO.setNovoObj(false);
			throw e;
		}
	}

	@Override
	public ProvaProcessoSeletivoVO novo() {
		ProvaProcessoSeletivoVO provaProcessoSeletivoVO = new ProvaProcessoSeletivoVO();
		provaProcessoSeletivoVO.setSituacaoProvaProcessoSeletivo(SituacaoProvaProcessoSeletivoEnum.EM_ELABORACAO);
		return provaProcessoSeletivoVO;
	}

	@Override
	public void excluir(final ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
		excluir(idEntidade, controlarAcesso, usuarioVO);
		final StringBuilder sql = new StringBuilder("DELETE FROM ProvaProcessoSeletivo ");
		sql.append(" where codigo = ? ");
		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				ps.setInt(1, provaProcessoSeletivoVO.getCodigo());
				return ps;
			}
		}) == 0) {
			return;
		}

	}

	@Override
	public void ativar(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
		SituacaoProvaProcessoSeletivoEnum situacaoProcessoSeletivoEnum = provaProcessoSeletivoVO.getSituacaoProvaProcessoSeletivo();
		try {
			provaProcessoSeletivoVO.setDataAlteracao(new Date());
			provaProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
			provaProcessoSeletivoVO.setSituacaoProvaProcessoSeletivo(SituacaoProvaProcessoSeletivoEnum.ATIVA);
			persistir(provaProcessoSeletivoVO, controlarAcesso, idEntidade, usuarioVO);
		} catch (Exception e) {
			provaProcessoSeletivoVO.setSituacaoProvaProcessoSeletivo(situacaoProcessoSeletivoEnum);
			throw e;
		}

	}

	@Override
	public void inativar(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
		SituacaoProvaProcessoSeletivoEnum situacaoProcessoSeletivoEnum = provaProcessoSeletivoVO.getSituacaoProvaProcessoSeletivo();
		try {
			provaProcessoSeletivoVO.setDataAlteracao(new Date());
			provaProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
			provaProcessoSeletivoVO.setSituacaoProvaProcessoSeletivo(SituacaoProvaProcessoSeletivoEnum.INATIVA);
			persistir(provaProcessoSeletivoVO, controlarAcesso, idEntidade, usuarioVO);
		} catch (Exception e) {
			provaProcessoSeletivoVO.setSituacaoProvaProcessoSeletivo(situacaoProcessoSeletivoEnum);
			throw e;
		}
	}

	public String getSelectDadosBasicos() {
		StringBuilder sb = new StringBuilder("SELECT provaProcessoSeletivo.*, ");
		sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\" ");
		sb.append(" FROM provaProcessoSeletivo ");
		sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = provaProcessoSeletivo.responsavelCriacao ");
		sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = provaProcessoSeletivo.responsavelAlteracao ");
		return sb.toString();
	}

	public String getSelectDadosCompleto() {
		StringBuilder sb = new StringBuilder("SELECT provaProcessoSeletivo.*, ");
		sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\", ");
		sb.append(" qle.codigo as \"qle.codigo\",  ");
		sb.append(" qle.ordemApresentacao as \"qle.ordemApresentacao\",  ");
		sb.append(" questaoProcessoSeletivo.codigo as \"questaoProcessoSeletivo.codigo\",  ");
		sb.append(" questaoProcessoSeletivo.enunciado as \"questaoProcessoSeletivo.enunciado\",   ");
		sb.append(" questaoProcessoSeletivo.situacaoquestaoenum as \"questaoProcessoSeletivo.situacaoquestaoenum\",   ");
		sb.append(" questaoProcessoSeletivo.nivelComplexidadeQuestao as \"questaoProcessoSeletivo.nivelComplexidadeQuestao\",   ");
		sb.append(" questaoProcessoSeletivo.disciplinaprocseletivo as \"questaoProcessoSeletivo.disciplinaprocseletivo\"   ");
		sb.append(" FROM provaProcessoSeletivo ");
		sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = provaProcessoSeletivo.responsavelCriacao ");
		sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = provaProcessoSeletivo.responsavelAlteracao ");
		sb.append(" left join QuestaoProvaProcessoSeletivo as qle on  qle.ProvaProcessoSeletivo = ProvaProcessoSeletivo.codigo ");
		sb.append(" left join QuestaoProcessoSeletivo as questaoProcessoSeletivo on  questaoProcessoSeletivo.codigo = qle.questaoProcessoSeletivo ");
		return sb.toString();
	}

	@Override
	public List<ProvaProcessoSeletivoVO> consultar(String descricao, SituacaoProvaProcessoSeletivoEnum situacaoProcessoSeletivo, Boolean controlarAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception {
		consultar(idEntidade, controlarAcesso, usuario);
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = new StringBuilder(getSelectDadosBasicos());
		sql.append(" WHERE 0=0 ");
		if (descricao != null && !descricao.trim().isEmpty()) {
			sql.append(" and upper(sem_acentos(provaProcessoSeletivo.descricao)) like(upper(sem_acentos(?))) ");
			parametros.add(PERCENT + descricao.trim() + PERCENT);
		}
		if (situacaoProcessoSeletivo != null) {
			sql.append(" and situacaoProvaProcessoSeletivo = '").append(situacaoProcessoSeletivo.name()).append("'");
		}
		if (limite != null && limite > 0) {
			sql.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray()));
	}

	private List<ProvaProcessoSeletivoVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<ProvaProcessoSeletivoVO> provaProcessoSeletivoVOs = new ArrayList<ProvaProcessoSeletivoVO>(0);
		while (rs.next()) {
			provaProcessoSeletivoVOs.add(montarDadosBasicos(rs));
		}
		return provaProcessoSeletivoVOs;
	}

	private List<ProvaProcessoSeletivoVO> montarDadosConsultaCompleta(SqlRowSet rs) throws Exception {
		List<ProvaProcessoSeletivoVO> questaoVOs = new ArrayList<ProvaProcessoSeletivoVO>(0);
		Map<Integer, ProvaProcessoSeletivoVO> qMap = new HashMap<Integer, ProvaProcessoSeletivoVO>(0);
		ProvaProcessoSeletivoVO obj = null;
		while (rs.next()) {
			if (!qMap.containsKey(rs.getInt("codigo"))) {
				obj = montarDadosBasicos(rs);
				qMap.put(obj.getCodigo(), obj);
			} else {
				obj = qMap.get(rs.getInt("codigo"));
			}
			if (Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivoVO())) {
				obj.setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(obj.getGrupoDisciplinaProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
			}
			if (rs.getInt("qle.codigo") > 0) {
				QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO = new QuestaoProvaProcessoSeletivoVO();
				questaoProvaProcessoSeletivoVO.setNovoObj(false);
				questaoProvaProcessoSeletivoVO.setCodigo(rs.getInt("qle.codigo"));
				questaoProvaProcessoSeletivoVO.setOrdemApresentacao(rs.getInt("qle.ordemApresentacao"));
				questaoProvaProcessoSeletivoVO.setProvaProcessoSeletivo(obj);
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setNovoObj(false);
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setCodigo(rs.getInt("questaoProcessoSeletivo.codigo"));
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().setCodigo(rs.getInt("questaoProcessoSeletivo.disciplinaprocseletivo"));
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setEnunciado(rs.getString("questaoProcessoSeletivo.enunciado"));
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setSituacaoQuestaoEnum(SituacaoQuestaoEnum.valueOf((rs.getString("questaoProcessoSeletivo.situacaoquestaoenum"))));
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.valueOf(rs.getString("questaoProcessoSeletivo.nivelComplexidadeQuestao")));
				obj.getQuestaoProvaProcessoSeletivoVOs().add(questaoProvaProcessoSeletivoVO);
				questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs().addAll(getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().consultarPorQuestao(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo()));
			}
		}
		questaoVOs.addAll(qMap.values());
		return questaoVOs;
	}

	private ProvaProcessoSeletivoVO montarDadosBasicos(SqlRowSet rs) throws Exception {
		ProvaProcessoSeletivoVO obj = new ProvaProcessoSeletivoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setDataCriacao(rs.getDate("dataCriacao"));
		obj.setDataAlteracao(rs.getDate("dataAlteracao"));
		if (rs.getInt("responsavelAlteracao") > 0) {
			obj.setResponsavelAlteracao(new UsuarioVO());
			obj.getResponsavelAlteracao().setCodigo(rs.getInt("responsavelAlteracao"));
			obj.getResponsavelAlteracao().setNome(rs.getString("responsavelAlteracao.nome"));
		}
		obj.setResponsavelCriacao(new UsuarioVO());
		obj.getResponsavelCriacao().setCodigo(rs.getInt("responsavelCriacao"));
		obj.getResponsavelCriacao().setNome(rs.getString("responsavelCriacao.nome"));
		obj.setDescricao(rs.getString("descricao"));
		obj.setSituacaoProvaProcessoSeletivo(SituacaoProvaProcessoSeletivoEnum.valueOf(rs.getString("situacaoProvaProcessoSeletivo")));
		obj.getGrupoDisciplinaProcSeletivoVO().setCodigo(rs.getInt("grupoDisciplinaProcSeletivo"));
		obj.setPossuiRedacao(rs.getBoolean("possuiRedacao"));
		obj.setTemaRedacao(rs.getString("temaRedacao"));
		obj.setQuantidadeMaximaCaracteresRedacao(rs.getInt("quantidadeMaximaCaracteresRedacao"));
		if (Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivoVO())) {
			obj.setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(obj.getGrupoDisciplinaProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
		}
		return obj;
	}

	@Override
	public Integer consultarTotalRegistro(String descricao, SituacaoProvaProcessoSeletivoEnum situacaoProcessoSeletivo) throws Exception {
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT count(codigo) as qtde ");
		sql.append(" FROM provaProcessoSeletivo ");
		sql.append(" WHERE 0=0 ");
		if (descricao != null && !descricao.trim().isEmpty()) {
			sql.append(" and upper(sem_acentos(provaProcessoSeletivo.descricao)) like(upper(sem_acentos(?))) ");
			parametros.add(PERCENT + descricao.trim() + PERCENT);
		}

		if (situacaoProcessoSeletivo != null) {
			sql.append(" and situacaoProvaProcessoSeletivo = '").append(situacaoProcessoSeletivo.name()).append("'");
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public ProvaProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder(nivelMontarDados.equals(NivelMontarDados.TODOS) ? getSelectDadosCompleto() : getSelectDadosBasicos());
		sql.append(" WHERE provaProcessoSeletivo.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		if (nivelMontarDados.equals(NivelMontarDados.TODOS)) {
			List<ProvaProcessoSeletivoVO> provaProcessoSeletivoVOs = null;
			provaProcessoSeletivoVOs = montarDadosConsultaCompleta(rs);
			if (provaProcessoSeletivoVOs.isEmpty()) {
				throw new Exception("Dados não encontrados(Lista Exercício).");
			}
			ProvaProcessoSeletivoVO obj = provaProcessoSeletivoVOs.get(0);
			Ordenacao.ordenarLista(obj.getQuestaoProvaProcessoSeletivoVOs(), "ordemApresentacao");
			return obj;
		} else {
			if (rs.next()) {
				return montarDadosBasicos(rs);
			} else {
				throw new Exception("Dados não encontrados(Lista Exercício).");
			}

		}

	}

	@Override
	public void adicionarQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws Exception {
		for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO2 : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
			if (questaoProvaProcessoSeletivoVO2.getQuestaoProcessoSeletivo().getCodigo().intValue() == questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo().intValue()) {
				throw new Exception(UteisJSF.internacionalizar("msg_ListaExercicio_questaoJaAdicionada"));
			}
		}
		questaoProvaProcessoSeletivoVO.setOrdemApresentacao(provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs().size() + 1);
		provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs().add(questaoProvaProcessoSeletivoVO);

	}

	@Override
	public void removerQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws Exception {
		int x = 0;
		for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO2 : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
			if (questaoProvaProcessoSeletivoVO2.getQuestaoProcessoSeletivo().getCodigo().intValue() == questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo().intValue()) {
				provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs().remove(x);
				break;
			}
			x++;
		}
		x = 1;
		for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO2 : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
			questaoProvaProcessoSeletivoVO2.setOrdemApresentacao(x++);
		}
	}

	@Override
	public void alterarOrdemApresentacaoQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoListaExercicio1, QuestaoProvaProcessoSeletivoVO questaoListaExercicio2) {
		int ordem1 = questaoListaExercicio1.getOrdemApresentacao();
		questaoListaExercicio1.setOrdemApresentacao(questaoListaExercicio2.getOrdemApresentacao());
		questaoListaExercicio2.setOrdemApresentacao(ordem1);
		Ordenacao.ordenarLista(provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs(), "ordemApresentacao");

	}

	@Override
	public ProvaProcessoSeletivoVO clonarProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception {

		return provaProcessoSeletivoVO.clone(provaProcessoSeletivoVO);
	}

	@Override
	public void realizarVerificacaoQuestaoUnicaEscolha(ProvaProcessoSeletivoVO provaProcessoSeletivoVO, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) {
		if (opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
			for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
				if (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo().intValue() == opcaoRespostaQuestaoProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo().intValue()) {
					for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoVO2 : questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
						if (opcaoRespostaQuestaoVO2.getCodigo().intValue() != opcaoRespostaQuestaoProcessoSeletivoVO.getCodigo().intValue()) {
							opcaoRespostaQuestaoVO2.setMarcada(false);
						}
					}
					return;
				}
			}
		}

	}

	@Override
	public void realizarGeracaoGabarito(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception {
		List<Integer> questoesNaoRespondida = new ArrayList<Integer>(0);
		provaProcessoSeletivoVO.setNumeroAcertos(0);
		provaProcessoSeletivoVO.setNumeroErros(0);
		for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
			getFacadeFactory().getQuestaoProcessoSeletivoFacade().realizarCorrecaoQuestao(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo());
			if (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getQuestaoNaoRespondida()) {
				questoesNaoRespondida.add(questaoProvaProcessoSeletivoVO.getOrdemApresentacao());
			}
			if (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getAcertouQuestao()) {
				provaProcessoSeletivoVO.setNumeroAcertos(provaProcessoSeletivoVO.getNumeroAcertos() + 1);
			}
			if (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getErrouQuestao()) {
				provaProcessoSeletivoVO.setNumeroErros(provaProcessoSeletivoVO.getNumeroErros() + 1);
			}
		}
	}

	@Override
	public void validarDados(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws ConsistirException, Exception {
		ConsistirException ce = null;
		if (provaProcessoSeletivoVO.getDescricao().trim().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_descricao"));
		}
		if (!Uteis.isAtributoPreenchido(provaProcessoSeletivoVO.getGrupoDisciplinaProcSeletivoVO())) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ProvaProcessoSeletivo_grupoDisciplinaProcessoSeletivo"));
		}
		if (provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs().isEmpty()) {
			if(!provaProcessoSeletivoVO.getPossuiRedacao()) {
				ce = ce == null ? new ConsistirException() : ce;
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_nrQuestaoInvalido"));
			}
			
		}

		if (ce != null) {
			throw ce;
		}
	}

	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String caminhoBaseIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ProvaProcessoSeletivoRel");
	}

	public String cabecalhoProvaHtml(String data, String processo, String unidade, String curso, String periodo) {
		StringBuilder cabecalho = new StringBuilder("");
		cabecalho.append("<html><head>");
		cabecalho.append("<style type=\"text/css\">");
		cabecalho.append("span,p,body { font-family:Arial !important; font-size:11px !important; }");
		cabecalho.append(".cabecalho { background:#D6D8DB; }");
		cabecalho.append(".textoNegrito { font-family:Arial; font-size:11px; }");
		cabecalho.append("h1 { font-family:Verdana; font-size:14px; font-weight:bold; }");
		cabecalho.append(".data { font-family:Verdana; font-size:10px; }");
		cabecalho.append("</style>");
		cabecalho.append("</head><body>");
		cabecalho.append("<table width=\"100%\" height=\"52\" border=\"1\" bordercolor=\"#000\" cellspacing=\"0\" cellpadding=\"0\">");
		cabecalho.append("<tr><td><table width=\"100%\" height=\"71\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" ><tr>");
		cabecalho.append("<td width=\"141\" rowspan=\"2\" align=\"center\"><img src=\"").append(getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator).append("logoPadraoRelatorio.png\" width=\"110\" height=\"48\"/></td>");
		cabecalho.append("<td width=\"968\" rowspan=\"2\" align=\"center\"><h1>Prova Processo Seletivo</h1></td><td width=\"236\" height=\"21\" align=\"center\" class=\"data\">").append(data).append("</td></tr>");
		cabecalho.append("<tr><td>&nbsp;</td></tr></table></td></tr></table>");
		cabecalho.append("<div style=\"height:10px;\">&nbsp;</div>");
		cabecalho.append("<table width=\"100%\" height=\"52\" border=\"1\" bordercolor=\"#333\" cellspacing=\"0\" cellpadding=\"0\" class=\"cabecalho\">");
		cabecalho.append("<tr><td>");
		cabecalho.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"4\" class=\"textoNegrito\"><tr>");
		// cabecalho.append("<td width=\"15%\" height=\"11\"><strong>Per&iacute;odo:</strong></td>");
		// cabecalho.append("<td width=\"37%\">1&ordm; Per&iacute;odo</td>");
		cabecalho.append("<td width=\"17%\"><strong>Processo Seletivo:</strong></td>");
		cabecalho.append("<td width=\"31%\">").append(processo).append("</td>");
		cabecalho.append("</tr>");
		cabecalho.append("<tr><td><strong>Unidade Ensino:</strong></td>");
		cabecalho.append("<td>").append(unidade).append("</td><td>&nbsp;</td><td>&nbsp;</td></tr><tr>");
		// cabecalho.append("<td><strong>Curso:</strong></td>");
		// cabecalho.append("<td>").append(curso).append("</td>");
		cabecalho.append("<td>&nbsp;</td><td>&nbsp;</td>");
		cabecalho.append("</tr></table></td></tr></table>");
		cabecalho.append("<div style=\"height:10px;\">&nbsp;</div>");
		return cabecalho.toString();
	}

	public String alterarTamanhoFonteQuestaoHtml(String linha) {
		linha = linha.replaceAll("10pt", "10px");
		linha = linha.replaceAll("10.0pt", "10px");
		linha = linha.replaceAll("11pt", "11px");
		linha = linha.replaceAll("11.0pt", "11px");
		linha = linha.replaceAll("12pt", "12px");
		linha = linha.replaceAll("12.0pt", "12px");
		linha = linha.replaceAll("13pt", "13px");
		linha = linha.replaceAll("13.0pt", "13px");
		linha = linha.replaceAll("14pt", "14px");
		linha = linha.replaceAll("14.0pt", "14px");
		linha = linha.replaceAll("15pt", "15px");
		linha = linha.replaceAll("15.0pt", "15px");
		linha = linha.replaceAll("16pt", "16px");
		linha = linha.replaceAll("16.0pt", "16px");
		linha = linha.replaceAll("17pt", "17px");
		linha = linha.replaceAll("17.0pt", "17px");
		linha = linha.replaceAll("18pt", "18px");
		linha = linha.replaceAll("18.0pt", "18px");
		linha = linha.replaceAll("19pt", "19px");
		linha = linha.replaceAll("19.0pt", "19px");
		linha = linha.replaceAll("20pt", "20px");
		linha = linha.replaceAll("20.0pt", "20px");
		linha = linha.replaceAll("21pt", "21px");
		linha = linha.replaceAll("21.0pt", "21px");
		linha = linha.replaceAll("22pt", "22px");
		linha = linha.replaceAll("22.0pt", "22px");
		linha = linha.replaceAll("23pt", "23px");
		linha = linha.replaceAll("23.0pt", "23px");
		linha = linha.replaceAll("24pt", "24px");
		linha = linha.replaceAll("24.0pt", "24px");
		linha = linha.replaceAll("25pt", "25px");
		linha = linha.replaceAll("25.0pt", "25px");
//		linha = linha.replaceAll("<p", "");
//		linha = linha.replaceAll("</p>", "");
		return linha;
	}

	public String executarDownloadProvaPDF(UnidadeEnsinoVO unidadeEnsino, ProvaProcessoSeletivoVO prova, ProcSeletivoVO procSeletivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		StringBuilder provaHtml = new StringBuilder();
		provaHtml.append(cabecalhoProvaHtml(procSeletivoVO.getDataProva_Apresentar(), procSeletivoVO.getDescricao(), unidadeEnsino.getNome(), "", ""));
		String nomeArquivo = "";
		if (prova != null) {
			for (QuestaoProvaProcessoSeletivoVO questao : prova.getQuestaoProvaProcessoSeletivoVOs()) {

				StringBuilder questaoHtml = new StringBuilder();
				questaoHtml.append("<div style=\"font-size:11px;font-family:Arial;\"><b>Questão ").append(questao.getOrdemApresentacao()).append(")</b>&nbsp;");
				String[] linhasQuestao = questao.getQuestaoProcessoSeletivo().getEnunciado().split("\r\n");
				for (String linhaQuestao : linhasQuestao) {
					linhaQuestao = linhaQuestao.replace(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator);
					if (linhaQuestao.contains("<img")) {
						questaoHtml.append("<div style=\"height:5px;\">&nbsp;</div>");
					}
					questaoHtml.append(alterarTamanhoFonteQuestaoHtml(linhaQuestao));
				}
				questaoHtml.append("</div>");
				provaHtml.append(questaoHtml.toString());

				for (OpcaoRespostaQuestaoProcessoSeletivoVO resposta : questao.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
					StringBuilder respostaHtml = new StringBuilder();
					respostaHtml.append("<div style=\"font-size:11px;font-family:Arial;\"><b>").append(resposta.getLetraCorrespondente()).append(")</b>&nbsp;");
					String[] linhasResposta = resposta.getOpcaoResposta().split("\r\n");
					for (String linhaResposta : linhasResposta) {
						linhaResposta = linhaResposta.replace(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator);
						if (linhaResposta.contains("<img")) {
							respostaHtml.append("<div style=\"height:5px;\">&nbsp;</div>");
						}
						respostaHtml.append(alterarTamanhoFonteQuestaoHtml(linhaResposta));
					}
					respostaHtml.append("</div>");
					provaHtml.append(respostaHtml.toString());
				}
				provaHtml.append("<div style=\"height:10px;\">&nbsp;</div>");
			}

			ByteArrayInputStream bytes = new ByteArrayInputStream(provaHtml.toString().getBytes());
			Document document = new Document();
			nomeArquivo = procSeletivoVO.getCodigo() + new Date().getTime() + ".pdf";
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(UteisJSF.getCaminhoWeb() + "/relatorio/" + nomeArquivo));
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, bytes);
			document.close();
		}
		return nomeArquivo;
	}

	/**
	 * Responsável por executar a definição do provaprocessoseletivo a ser utilizado levando em consideração se o mesmo está vínculado a um
	 * grupoDisciplinaProcSeletivo, se o ProcSeletivoCurso também está vínculado a um grupoDisciplinaProcSeletivo e se os dois tem o
	 * grupoDisciplinaProcSeletivo em comum.
	 * 
	 * @author Wellington - 5 de jan de 2016
	 * @param inscricao
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer consultarCodigoProvaPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(Integer inscricao) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select provaprocessoseletivo.codigo as provaprocessoseletivo ");
		sql.append("from inscricao ");
		sql.append("inner join ItemProcSeletivoDataProva on itemprocseletivodataprova.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append("inner join processoseletivoprovadata on processoseletivoprovadata.ItemProcSeletivoDataProva = ItemProcSeletivoDataProva.codigo ");
		sql.append("	and ( ");
		sql.append("		(inscricao.opcaolinguaestrangeira is null and processoseletivoprovadata.disciplinaidioma is null) ");
		sql.append("		or (processoseletivoprovadata.disciplinaidioma = inscricao.opcaolinguaestrangeira) ");
		sql.append("	) ");
		sql.append("inner join provaprocessoseletivo on provaprocessoseletivo.codigo = processoseletivoprovadata.provaprocessoseletivo ");
		sql.append("left join procSeletivoUnidadeEnsino on procSeletivoUnidadeEnsino.procseletivo = inscricao.procseletivo ");
		sql.append("left join ProcSeletivoCurso on ProcSeletivoCurso.procSeletivoUnidadeEnsino = procSeletivoUnidadeEnsino.codigo and ( ");
		sql.append("	ProcSeletivoCurso.unidadeensinocurso = inscricao.cursoopcao1 or ProcSeletivoCurso.unidadeensinocurso = inscricao.cursoopcao2 or ProcSeletivoCurso.unidadeensinocurso = inscricao.cursoopcao3 ");
		sql.append(") ");
		sql.append("where inscricao.codigo = ").append(inscricao);
		sql.append(" order by ( ");
		sql.append("	case when ProcSeletivoCurso.grupodisciplinaprocseletivo is not null ");
		sql.append("	and provaprocessoseletivo.grupodisciplinaprocseletivo is not null ");
		sql.append("	and ProcSeletivoCurso.grupodisciplinaprocseletivo = provaprocessoseletivo.grupodisciplinaprocseletivo then 1 ");
		sql.append("	else 2 end ");
		sql.append(") ");
		sql.append("limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("provaprocessoseletivo");
		}
		return 0;
	}

}
