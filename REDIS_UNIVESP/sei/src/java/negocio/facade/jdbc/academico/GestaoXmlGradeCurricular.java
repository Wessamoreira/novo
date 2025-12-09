package negocio.facade.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.GestaoXmlGradeCurricularVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularTipoAtividadeComplementarVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.academico.enumeradores.TipoOrigemDocumentoAssinadoEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.GestaoXmlGradeCurricularInterfaceFacade;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DisciplinasGradeRel;

/**
 * @see ControleAcesso
 * @author Felipi Alves
 */
@Repository
@Scope("singleton")
@Lazy
public class GestaoXmlGradeCurricular extends ControleAcesso implements GestaoXmlGradeCurricularInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String PENDENTE_ASSINATURA = "PENDENTE_ASSINATURA";
	protected static String GERACAO_PENDENTE = "GERACAO_PENDENTE";
	protected static String idEntidade;

	public GestaoXmlGradeCurricular() throws Exception {
		super();
		setIdEntidade("GestaoXmlGradeCurricular");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		GestaoXmlGradeCurricular.idEntidade = idEntidade;
	}

	public void consultarDados(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular) {
		List<Object> listaFiltro = new ArrayList<>(0);
		listaFiltro.add(Boolean.TRUE);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT count(*) OVER() AS qtde_total_registros, ");
		sql.append(" gradecurricular.codigo AS \"gradecurricular.codigo\", gradecurricular.nome AS \"gradecurricular.nome\", gradecurricular.situacao AS \"gradecurricular.situacao\", ");
		sql.append(" unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\", ");
		sql.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", documentoassinado.codigo AS \"documentoassinado.codigo\",	documentoassinado.dataregistro AS \"documentoassinado.dataregistro\", (CASE WHEN documentoassinado.codigo IS NULL THEN 'Não Gerado' WHEN documentoassinado.assinado THEN 'Gerado e Assinado' WHEN documentoassinado.pendente THEN 'Gerado com Pendência de Assinatura' ELSE 'Documento Rejeitado' END) AS situacaoDocumento ");
		sql.append(" FROM unidadeensino ");
		sql.append(" INNER JOIN LATERAL (SELECT u.curso FROM unidadeensinocurso u WHERE u.unidadeensino = unidadeensino.codigo GROUP BY u.curso) AS unidadeensinocurso ON TRUE");
		sql.append(" INNER JOIN curso ON curso.codigo = unidadeensinocurso.curso and curso.nivelEducacional IN ('GT', 'SU')");
		sql.append(" INNER JOIN gradecurricular ON gradecurricular.curso = curso.codigo");
		sql.append(" LEFT JOIN LATERAL (SELECT documentoassinado.codigo, documentoassinado.dataregistro, ");
		sql.append(" (CASE WHEN (totalDocumentoPessoa.qtdDocumentoPessoa - totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = 0 THEN TRUE ELSE FALSE END) AS assinado, ");
		sql.append(" (CASE WHEN (totalDocumentoPessoa.qtdDocumentoPessoa - totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente) = 0 THEN TRUE WHEN (totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = totalDocumentoPessoa.qtdDocumentoPessoa THEN FALSE WHEN (totalDocumentoPessoaPendente.qtdDocumentoPessoaPendente + totalDocumentoPessoaAssinado.qtdDocumentoPessoaAssinado) = totalDocumentoPessoa.qtdDocumentoPessoa THEN TRUE ELSE FALSE END) AS pendente ");
		sql.append(" FROM documentoassinado ");
		sql.append(" LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoa ");
		sql.append(" FROM documentoassinadopessoa ");
		sql.append(" WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo) AS totalDocumentoPessoa ON TRUE ");
		sql.append(" LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaPendente ");
		sql.append(" FROM documentoassinadopessoa ");
		sql.append(" WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
		sql.append(" AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('PENDENTE')) AS totalDocumentoPessoaPendente ON TRUE ");
		sql.append(" LEFT JOIN LATERAL (SELECT count(documentoassinadopessoa.codigo) AS qtdDocumentoPessoaAssinado ");
		sql.append(" FROM documentoassinadopessoa ");
		sql.append(" WHERE documentoassinadopessoa.documentoassinado = documentoassinado.codigo ");
		sql.append(" AND documentoassinadopessoa.situacaodocumentoassinadopessoa IN ('ASSINADO')) AS totalDocumentoPessoaAssinado ON TRUE ");
		sql.append(" WHERE documentoassinado.curso = curso.codigo ");
		sql.append(" AND documentoassinado.unidadeensino = unidadeensino.codigo ");
		sql.append(" AND documentoassinado.gradecurricular = gradecurricular.codigo ");
		sql.append(" AND documentoassinado.tipoorigemdocumentoassinado IN ('CURRICULO_ESCOLAR_DIGITAL') ");
		sql.append(" ORDER BY documentoassinado.codigo DESC LIMIT 1) AS documentoAssinado ON TRUE");
		sql.append(" WHERE ? ");
		if (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getUnidadeEnsinoFiltrarVOs())) {
			sql.append(" AND unidadeensino.codigo IN (" + gestaoXmlGradeCurricular.getUnidadeEnsinoFiltrarVOs().stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", ")) + ") ");
		}
		if (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getCursoFiltrarVOs())) {
			sql.append(" AND curso.codigo IN (" + gestaoXmlGradeCurricular.getCursoFiltrarVOs().stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", ")) + ") ");
		}
		if (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getSituacaoGradeFiltrar())) {
			listaFiltro.add(gestaoXmlGradeCurricular.getSituacaoGradeFiltrar());
			sql.append(" AND gradecurricular.situacao = ? ");
		}
		if (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getSituacaoXmlFiltrar())) {
			if (gestaoXmlGradeCurricular.getSituacaoXmlFiltrar().equals(GERACAO_PENDENTE)) {
				sql.append(" AND documentoAssinado.codigo IS NULL ");
			} else if (gestaoXmlGradeCurricular.getSituacaoXmlFiltrar().equals(PENDENTE_ASSINATURA)) {
				sql.append(" AND documentoAssinado.pendente ");
			} else {
				sql.append(" AND documentoAssinado.assinado ");
			}
		}
		sql.append(" ORDER BY unidadeensino.codigo, curso.codigo ");
		sql.append(" LIMIT ").append(gestaoXmlGradeCurricular.getControleConsultaOtimizado().getLimitePorPagina()).append(" OFFSET ").append(gestaoXmlGradeCurricular.getControleConsultaOtimizado().getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), listaFiltro.toArray());
		if (!tabelaResultado.next()) {
			return;
		}
		gestaoXmlGradeCurricular.getControleConsultaOtimizado().setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		tabelaResultado.beforeFirst();
		List<GestaoXmlGradeCurricularVO> gestaoXmlGradeCurricularVOs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO = new GestaoXmlGradeCurricularVO();
			gestaoXmlGradeCurricularVO.getDocumentoAssinadoVO().setCodigo(tabelaResultado.getInt("documentoassinado.codigo"));
			gestaoXmlGradeCurricularVO.getDocumentoAssinadoVO().setDataRegistro(tabelaResultado.getDate("documentoassinado.dataregistro"));
			gestaoXmlGradeCurricularVO.setSituacaoDocumento(tabelaResultado.getString("situacaoDocumento"));
			gestaoXmlGradeCurricularVO.getGradeCurricularVO().setCodigo(tabelaResultado.getInt("gradecurricular.codigo"));
			gestaoXmlGradeCurricularVO.getGradeCurricularVO().setNome(tabelaResultado.getString("gradecurricular.nome"));
			gestaoXmlGradeCurricularVO.getGradeCurricularVO().setSituacao(tabelaResultado.getString("gradecurricular.situacao"));
			gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
			gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().setNome(tabelaResultado.getString("unidadeensino.nome"));
			gestaoXmlGradeCurricularVO.getCursoVO().setCodigo(tabelaResultado.getInt("curso.codigo"));
			gestaoXmlGradeCurricularVO.getCursoVO().setNome(tabelaResultado.getString("curso.nome"));
			gestaoXmlGradeCurricularVOs.add(gestaoXmlGradeCurricularVO);
		}
		gestaoXmlGradeCurricular.getControleConsultaOtimizado().setListaConsulta(gestaoXmlGradeCurricularVOs);
	}

	@Override
	public void consultar(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular) throws Exception {
		gestaoXmlGradeCurricular.getControleConsultaOtimizado().setListaConsulta(new ArrayList<>(0));
		gestaoXmlGradeCurricular.getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
		consultarDados(gestaoXmlGradeCurricular);
	}

	public void carregarDadosGestaoXmlGradeCurricular(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, Boolean consultarUnidadeEnsino, Boolean consultarCurso) throws Exception {
		List<Object> filtros = new ArrayList<>(0);
		filtros.add(gestaoXmlGradeCurricular.getGradeCurricularVO().getCodigo());
		StringBuilder sql = new StringBuilder("SELECT gradecurricular.codigo AS \"gradecurricular.codigo\", gradecurricular.datacadastro AS \"gradecurricular.datacadastro\", gradecurricular.cargahoraria AS \"gradecurricular.cargahoraria\", gradecurricular.totalcargahorariaestagio AS \"gradecurricular.totalcargahorariaestagio\", gradecurricular.totalcargahorariaatividadecomplementar AS \"gradecurricular.totalcargahorariaatividadecomplementar\", gradecurricular.resolucao AS \"gradecurricular.resolucao\", ");
		if (consultarCurso) {
			sql.append("curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.nomedocumentacao AS \"curso.nomedocumentacao\", curso.possuicodigoemec AS \"curso.possuicodigoemec\", ");
			sql.append("curso.codigoemec AS \"curso.codigoemec\", curso.numeroprocessoemec AS \"curso.numeroprocessoemec\", curso.tipoprocessoemec AS \"curso.tipoprocessoemec\", ");
			sql.append("curso.datacadastroemec AS \"curso.datacadastroemec\", curso.dataprotocoloemec AS \"curso.dataprotocoloemec\", curso.tipoautorizacaocursoenum AS \"curso.tipoautorizacaocursoenum\", ");
			sql.append("curso.numeroautorizacao AS \"curso.numeroautorizacao\", curso.datacredenciamento AS \"curso.datacredenciamento\", curso.datapublicacaodo AS \"curso.datapublicacaodo\", ");
			sql.append("curso.veiculopublicacao AS \"curso.veiculopublicacao\", curso.secaopublicacao AS \"curso.secaopublicacao\", curso.paginapublicacao AS \"curso.paginapublicacao\", ");
			sql.append("curso.numerodou AS \"curso.numerodou\", curso.tipoprocessoautorizacaoresolucao AS \"curso.tipoprocessoautorizacaoresolucao\", curso.numeroprocessoautorizacaoresolucao AS \"curso.numeroprocessoautorizacaoresolucao\", ");
			sql.append("curso.datacadastroautorizacaoresolucao AS \"curso.datacadastroautorizacaoresolucao\", curso.dataprotocoloautorizacaoresolucao AS \"curso.dataprotocoloautorizacaoresolucao\", ");
			sql.append("curso.modalidadecurso AS \"curso.modalidadecurso\", areaCurso.codigo AS \"areaCurso.codigo\", areaCurso.nome AS \"areaCurso.nome\", ");
		}
		if (consultarUnidadeEnsino) {
			sql.append("unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\", unidadeensino.codigoies AS \"unidadeensino.codigoies\", ");
			sql.append("unidadeensino.cnpj AS \"unidadeensino.cnpj\", unidadeensino.credenciamento AS \"unidadeensino.credenciamento\", unidadeensino.credenciamentoeademtramitacao AS \"unidadeensino.credenciamentoeademtramitacao\", ");
			sql.append("unidadeensino.numeroprocessocredenciamentoead AS \"unidadeensino.numeroprocessocredenciamentoead\", unidadeensino.tipoprocessocredenciamentoead AS \"unidadeensino.tipoprocessocredenciamentoead\", ");
			sql.append("unidadeensino.datacadastrocredenciamentoead AS \"unidadeensino.datacadastrocredenciamentoead\", unidadeensino.dataprotocolocredenciamentoead AS \"unidadeensino.dataprotocolocredenciamentoead\", ");
			sql.append("unidadeensino.tipoautorizacaoead AS \"unidadeensino.tipoautorizacaoead\", unidadeensino.numerocredenciamentoead AS \"unidadeensino.numerocredenciamentoead\", ");
			sql.append("unidadeensino.datacredenciamentoead AS \"unidadeensino.datacredenciamentoead\", unidadeensino.datapublicacaodoead AS \"unidadeensino.datapublicacaodoead\", ");
			sql.append("unidadeensino.veiculopublicacaocredenciamentoead AS \"unidadeensino.veiculopublicacaocredenciamentoead\", unidadeensino.secaopublicacaocredenciamentoead AS \"unidadeensino.secaopublicacaocredenciamentoead\", ");
			sql.append("unidadeensino.paginapublicacaocredenciamentoead AS \"unidadeensino.paginapublicacaocredenciamentoead\", unidadeensino.numerodoucredenciamentoead AS \"unidadeensino.numerodoucredenciamentoead\", ");
			sql.append("unidadeensino.credenciamentoemtramitacao AS \"unidadeensino.credenciamentoemtramitacao\", unidadeensino.numeroprocessocredenciamento AS \"unidadeensino.numeroprocessocredenciamento\", ");
			sql.append("unidadeensino.tipoprocessocredenciamento AS \"unidadeensino.tipoprocessocredenciamento\", unidadeensino.datacadastrocredenciamento AS \"unidadeensino.datacadastrocredenciamento\", ");
			sql.append("unidadeensino.dataprotocolocredenciamento AS \"unidadeensino.dataprotocolocredenciamento\", unidadeensino.tipoautorizacaoenum AS \"unidadeensino.tipoautorizacaoenum\", ");
			sql.append("unidadeensino.numerocredenciamento AS \"unidadeensino.numerocredenciamento\", unidadeensino.datacredenciamento AS \"unidadeensino.datacredenciamento\", ");
			sql.append("unidadeensino.datapublicacaodo AS \"unidadeensino.datapublicacaodo\", unidadeensino.veiculopublicacaocredenciamento AS \"unidadeensino.veiculopublicacaocredenciamento\", ");
			sql.append("unidadeensino.secaopublicacaocredenciamento AS \"unidadeensino.secaopublicacaocredenciamento\", unidadeensino.paginapublicacaocredenciamento AS \"unidadeensino.paginapublicacaocredenciamento\", ");
			sql.append("unidadeensino.numerodoucredenciamento AS \"unidadeensino.numerodoucredenciamento\", unidadeensino.recredenciamentoemtramitacaoead AS \"unidadeensino.recredenciamentoemtramitacaoead\", ");
			sql.append("unidadeensino.numeroprocessorecredenciamentoead AS \"unidadeensino.numeroprocessorecredenciamentoead\", unidadeensino.tipoprocessorecredenciamentoead AS \"unidadeensino.tipoprocessorecredenciamentoead\", ");
			sql.append("unidadeensino.datacadastrorecredenciamentoead AS \"unidadeensino.datacadastrorecredenciamentoead\", unidadeensino.dataprotocolorecredenciamentoead AS \"unidadeensino.dataprotocolorecredenciamentoead\", ");
			sql.append("unidadeensino.numerorecredenciamentoead AS \"unidadeensino.numerorecredenciamentoead\", unidadeensino.tipoautorizacaorecredenciamentoead AS \"unidadeensino.tipoautorizacaorecredenciamentoead\", ");
			sql.append("unidadeensino.datarecredenciamentoead AS \"unidadeensino.datarecredenciamentoead\", unidadeensino.datapublicacaorecredenciamentoead AS \"unidadeensino.datapublicacaorecredenciamentoead\", ");
			sql.append("unidadeensino.veiculopublicacaorecredenciamentoead AS \"unidadeensino.veiculopublicacaorecredenciamentoead\", unidadeensino.secaopublicacaorecredenciamentoead AS \"unidadeensino.secaopublicacaorecredenciamentoead\", ");
			sql.append("unidadeensino.paginapublicacaorecredenciamentoead AS \"unidadeensino.paginapublicacaorecredenciamentoead\", unidadeensino.numerodourecredenciamentoead AS \"unidadeensino.numerodourecredenciamentoead\", ");
			sql.append("unidadeensino.recredenciamentoemtramitacao AS \"unidadeensino.recredenciamentoemtramitacao\", unidadeensino.numeroprocessorecredenciamento AS \"unidadeensino.numeroprocessorecredenciamento\", ");
			sql.append("unidadeensino.tipoprocessorecredenciamento AS \"unidadeensino.tipoprocessorecredenciamento\", unidadeensino.datacadastrorecredenciamento AS \"unidadeensino.datacadastrorecredenciamento\", ");
			sql.append("unidadeensino.dataprotocolorecredenciamento AS \"unidadeensino.dataprotocolorecredenciamento\", unidadeensino.numerorecredenciamento AS \"unidadeensino.numerorecredenciamento\", ");
			sql.append("unidadeensino.tipoautorizacaorecredenciamento AS \"unidadeensino.tipoautorizacaorecredenciamento\", unidadeensino.datarecredenciamento AS \"unidadeensino.datarecredenciamento\", ");
			sql.append("unidadeensino.datapublicacaorecredenciamento AS \"unidadeensino.datapublicacaorecredenciamento\", unidadeensino.veiculopublicacaorecredenciamento AS \"unidadeensino.veiculopublicacaorecredenciamento\", ");
			sql.append("unidadeensino.secaopublicacaorecredenciamento AS \"unidadeensino.secaopublicacaorecredenciamento\", unidadeensino.paginapublicacaorecredenciamento AS \"unidadeensino.paginapublicacaorecredenciamento\", ");
			sql.append("unidadeensino.numerodourecredenciamento AS \"unidadeensino.numerodourecredenciamento\", unidadeensino.renovacaorecredenciamentoemtramitacaoead AS \"unidadeensino.renovacaorecredenciamentoemtramitacaoead\", ");
			sql.append("unidadeensino.numeroprocessorenovacaorecredenciamentoead AS \"unidadeensino.numeroprocessorenovacaorecredenciamentoead\", unidadeensino.tipoprocessorenovacaorecredenciamentoead AS \"unidadeensino.tipoprocessorenovacaorecredenciamentoead\", ");
			sql.append("unidadeensino.datacadastrorenovacaorecredenciamentoead AS \"unidadeensino.datacadastrorenovacaorecredenciamentoead\", unidadeensino.dataprotocolorenovacaorecredenciamentoead AS \"unidadeensino.dataprotocolorenovacaorecredenciamentoead\", ");
			sql.append("unidadeensino.numerorenovacaorecredenciamentoead AS \"unidadeensino.numerorenovacaorecredenciamentoead\", unidadeensino.tipoautorizacaorenovacaorecredenciamentoead AS \"unidadeensino.tipoautorizacaorenovacaorecredenciamentoead\", ");
			sql.append("unidadeensino.datarenovacaorecredenciamentoead AS \"unidadeensino.datarenovacaorecredenciamentoead\", unidadeensino.datapublicacaorenovacaorecredenciamentoead AS \"unidadeensino.datapublicacaorenovacaorecredenciamentoead\", ");
			sql.append("unidadeensino.veiculopublicacaorenovacaorecredenciamentoead AS \"unidadeensino.veiculopublicacaorenovacaorecredenciamentoead\", unidadeensino.secaopublicacaorenovacaorecredenciamentoead AS \"unidadeensino.secaopublicacaorenovacaorecredenciamentoead\", ");
			sql.append("unidadeensino.paginapublicacaorenovacaorecredenciamentoead AS \"unidadeensino.paginapublicacaorenovacaorecredenciamentoead\", unidadeensino.numerodourenovacaorecredenciamentoead AS \"unidadeensino.numerodourenovacaorecredenciamentoead\", ");
			sql.append("unidadeensino.renovacaorecredenciamentoemtramitacao AS \"unidadeensino.renovacaorecredenciamentoemtramitacao\", unidadeensino.numeroprocessorenovacaorecredenciamento AS \"unidadeensino.numeroprocessorenovacaorecredenciamento\", ");
			sql.append("unidadeensino.tipoprocessorenovacaorecredenciamento AS \"unidadeensino.tipoprocessorenovacaorecredenciamento\", unidadeensino.datacadastrorenovacaorecredenciamento AS \"unidadeensino.datacadastrorenovacaorecredenciamento\", ");
			sql.append("unidadeensino.dataprotocolorenovacaorecredenciamento AS \"unidadeensino.dataprotocolorenovacaorecredenciamento\", unidadeensino.numerorenovacaorecredenciamento AS \"unidadeensino.numerorenovacaorecredenciamento\", ");
			sql.append("unidadeensino.tipoautorizacaorenovacaorecredenciamento AS \"unidadeensino.tipoautorizacaorenovacaorecredenciamento\", unidadeensino.datarenovacaorecredenciamento AS \"unidadeensino.datarenovacaorecredenciamento\", ");
			sql.append("unidadeensino.datapublicacaorenovacaorecredenciamento AS \"unidadeensino.datapublicacaorenovacaorecredenciamento\", unidadeensino.veiculopublicacaorenovacaorecredenciamento AS \"unidadeensino.veiculopublicacaorenovacaorecredenciamento\", ");
			sql.append("unidadeensino.secaopublicacaorenovacaorecredenciamento AS \"unidadeensino.secaopublicacaorenovacaorecredenciamento\", unidadeensino.paginapublicacaorenovacaorecredenciamento AS \"unidadeensino.paginapublicacaorenovacaorecredenciamento\", ");
			sql.append("unidadeensino.numerodourenovacaorecredenciamento AS \"unidadeensino.numerodourenovacaorecredenciamento\", unidadeensino.mantenedora AS \"unidadeensino.mantenedora\", unidadeensino.cnpjmantenedora AS \"unidadeensino.cnpjmantenedora\", ");
			sql.append("unidadeensino.utilizarenderecounidadeensinomantenedora AS \"unidadeensino.utilizarenderecounidadeensinomantenedora\", unidadeensino.enderecomantenedora AS \"unidadeensino.enderecomantenedora\", ");
			sql.append("unidadeensino.numeromantenedora AS \"unidadeensino.numeromantenedora\", unidadeensino.complementomantenedora AS \"unidadeensino.complementomantenedora\", unidadeensino.bairromantenedora AS \"unidadeensino.bairromantenedora\", ");
			sql.append("unidadeensino.cepmantenedora AS \"unidadeensino.cepmantenedora\", cidadeMantenedora.codigo AS \"cidadeMantenedora.codigo\", cidadeMantenedora.nome AS \"cidadeMantenedora.nome\", cidadeMantenedora.codigoIBGE AS \"cidadeMantenedora.codigoIBGE\", estadoMantenedora.codigo AS \"estadoMantenedora.codigo\", ");
			sql.append("estadoMantenedora.sigla AS \"estadoMantenedora.sigla\", unidadeensino.endereco AS \"unidadeensino.endereco\", unidadeensino.numero AS \"unidadeensino.numero\", unidadeensino.complemento AS \"unidadeensino.complemento\", ");
			sql.append("unidadeensino.setor AS \"unidadeensino.setor\", unidadeensino.cep AS \"unidadeensino.cep\", cidadeIes.codigo AS \"cidadeIes.codigo\", cidadeIes.codigoibge AS \"cidadeIes.codigoibge\", cidadeIes.nome AS \"cidadeIes.nome\", ");
			sql.append("estadoIes.codigo AS \"estadoIes.codigo\", estadoIes.sigla AS \"estadoIes.sigla\", unidadeensino.telcomercial1 AS \"unidadeensino.telcomercial1\", unidadeensino.site AS \"unidadeensino.site\", ");
			sql.append("unidadeensino.email AS \"unidadeensino.email\", unidadeensino.nomeexpedicaodiploma AS \"unidadeensino.nomeexpedicaodiploma\", unidadeensino.inscestadual AS \"unidadeensino.inscestadual\", ");
			sql.append("unidadeensino.inscmunicipal AS \"unidadeensino.inscmunicipal\", unidadeensino.caixapostal AS \"unidadeensino.caixapostal\", unidadeensino.caminhobaselogorelatorio AS \"unidadeensino.caminhobaselogorelatorio\", unidadeensino.nomearquivologorelatorio AS \"unidadeensino.nomearquivologorelatorio\", ");
		}
		sql.append("periodoletivo.*, ");
		sql.append("gradecurriculartipoatividadecomplementar.*, ");
		sql.append("gradecurriculargrupooptativadisciplina.* ");
		sql.append("FROM curso ");
		sql.append("INNER JOIN gradecurricular ON gradecurricular.curso = curso.codigo AND gradecurricular.codigo = ? ");
		sql.append("LEFT JOIN LATERAL(SELECT ");
		sql.append("json_agg(periodoJson.obj) AS periodoLetivo, ");
		sql.append("json_agg(json_build_object('gradedisciplina.codigo', g.codigo, 'gradedisciplina.periodoletivo', g.periodoletivo, 'gradedisciplina.disciplinaestagio', g.disciplinaestagio, 'gradedisciplina.cargahoraria', g.cargahoraria, 'gradedisciplina.tipodisciplina', g.tipodisciplina, 'gradedisciplina.modalidadedisciplina', g.modalidadedisciplina, 'gradedisciplina.cargahorariapratica', g.cargahorariapratica, 'gradedisciplina.cargahorariateorica', (g.cargahoraria - g.cargahorariapratica), 'disciplina.codigo', d.codigo, 'disciplina.nome', d.nome, 'gradedisciplina.utilizarEmissaoXmlDiploma', g.utilizarEmissaoXmlDiploma)) AS gradedisciplina, ");
		sql.append("(CASE WHEN length(array_to_string(array_agg(disciplinaprerequisito.codigo), '')) > 0 THEN json_agg(json_build_object('periodoletivo.codigo', p.codigo, 'gradedisciplina.codigo', g.codigo, 'disciplinaprerequisito.codigo', disciplinaprerequisito.codigo, 'disciplinaprerequisito.gradedisciplina', disciplinaprerequisito.gradedisciplina, 'disciplinarequisito.codigo', disciplinarequisito.codigo, 'disciplinarequisito.nome', disciplinarequisito.nome)) FILTER (WHERE disciplinaprerequisito.codigo IS NOT NULL) ELSE NULL::json END) AS disciplinaprerequisito, ");
		sql.append("(CASE WHEN length(array_to_string(array_agg(mapaequivalenciadisciplinacursada.disciplina), '')) > 0 THEN json_agg(json_build_object('periodoletivo.codigo', p.codigo, 'gradedisciplina.codigo', g.codigo, 'mapaequivalenciadisciplinamatrizcurricular.disciplina', mapaequivalenciadisciplinamatrizcurricular.disciplina, 'mapaequivalenciadisciplinacursada.disciplina', mapaequivalenciadisciplinacursada.disciplina)) FILTER (WHERE mapaequivalenciadisciplinacursada.disciplina IS NOT NULL) ELSE NULL::json END) AS mapaEquivalenciaDisciplina ");
		sql.append("FROM periodoletivo p ");
		sql.append("INNER JOIN LATERAL (SELECT pe.codigo AS pe_codigo, json_build_object('periodoletivo.codigo', pe.codigo, 'periodoletivo.nome', pe.descricao) AS obj FROM periodoletivo pe WHERE pe.codigo = p.codigo GROUP BY pe.codigo) AS periodoJson ON TRUE ");
		sql.append("INNER JOIN gradedisciplina g ON g.periodoletivo = p.codigo ");
		sql.append("LEFT JOIN disciplina d ON d.codigo = g.disciplina ");
		sql.append("LEFT JOIN disciplinaprerequisito ON disciplinaprerequisito.gradedisciplina = g.codigo ");
		sql.append("LEFT JOIN disciplina disciplinarequisito ON disciplinarequisito.codigo = disciplinaprerequisito.disciplina ");
		sql.append("LEFT JOIN mapaequivalenciadisciplinamatrizcurricular ON mapaequivalenciadisciplinamatrizcurricular.disciplina = d.codigo ");
		sql.append("LEFT JOIN mapaequivalenciadisciplina ON mapaequivalenciadisciplina.codigo = mapaequivalenciadisciplinamatrizcurricular.mapaequivalenciadisciplina AND mapaequivalenciadisciplina.situacao = 'ATIVO' ");
		sql.append("LEFT JOIN mapaequivalenciadisciplinacursada ON mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina = mapaequivalenciadisciplina.codigo ");
		sql.append("LEFT JOIN mapaequivalenciamatrizcurricular ON mapaequivalenciamatrizcurricular.codigo = mapaequivalenciadisciplina.mapaequivalenciamatrizcurricular AND mapaequivalenciamatrizcurricular.curso = curso.codigo AND mapaequivalenciamatrizcurricular.gradecurricular = gradecurricular.codigo AND mapaequivalenciamatrizcurricular.situacao = 'ATIVO' ");
		sql.append("WHERE p.gradecurricular = gradecurricular.codigo) AS periodoletivo ON TRUE ");
		sql.append("LEFT JOIN LATERAL (SELECT ");
		sql.append("(CASE WHEN length(array_to_string(array_agg(gradecurriculartipoatividadecomplementar.codigo), '')) > 0 ");
		sql.append("THEN json_agg(json_build_object('gradecurriculartipoatividadecomplementar.codigo', gradecurriculartipoatividadecomplementar.codigo, 'gradecurriculartipoatividadecomplementar.cargahoraria', gradecurriculartipoatividadecomplementar.cargahoraria, 'gradecurriculartipoatividadecomplementar.horasMinimasExigida', gradecurriculartipoatividadecomplementar.horasMinimasExigida, 'tipoatividadecomplementar.codigo', tipoatividadecomplementar.codigo, 'tipoatividadecomplementar.nome', tipoatividadecomplementar.nome, 'tipoAtividadeSuperior.codigo', tipoAtividadeSuperior.codigo, 'tipoAtividadeSuperior.nome', tipoAtividadeSuperior.nome)) ");
		sql.append("FILTER(WHERE gradecurriculartipoatividadecomplementar.codigo IS NOT NULL) ELSE NULL::json END) AS gradecurriculartipoatividadecomplementar ");
		sql.append("FROM gradecurriculartipoatividadecomplementar  ");
		sql.append("LEFT JOIN tipoatividadecomplementar ON tipoatividadecomplementar.codigo = gradecurriculartipoatividadecomplementar.tipoatividadecomplementar  ");
		sql.append("LEFT JOIN tipoatividadecomplementar tipoAtividadeSuperior ON tipoAtividadeSuperior.codigo = tipoatividadecomplementar.tipoatividadecomplementarsuperior ");
		sql.append("WHERE gradecurriculartipoatividadecomplementar.gradecurricular = gradecurricular.codigo) AS gradecurriculartipoatividadecomplementar ON TRUE ");
		sql.append("LEFT JOIN LATERAL (SELECT ");
		sql.append("json_agg(json_build_object('periodoletivo.codigo', p.codigo, 'gradecurriculargrupooptativa.codigo', gradecurriculargrupooptativa.codigo, 'gradecurriculargrupooptativa.descricao', gradecurriculargrupooptativa.descricao, 'gradecurriculargrupooptativadisciplina.codigo', gradecurriculargrupooptativadisciplina.codigo, 'gradecurriculargrupooptativadisciplina.cargahoraria', gradecurriculargrupooptativadisciplina.cargahoraria, 'gradecurriculargrupooptativadisciplina.cargahorariapratica', gradecurriculargrupooptativadisciplina.cargahorariapratica, 'disciplina.codigo', d.codigo, 'disciplina.nome', d.nome)) AS gradecurriculargrupooptativadisciplina ");
		sql.append("FROM gradecurriculargrupooptativadisciplina ");
		sql.append("INNER JOIN gradecurriculargrupooptativa ON gradecurriculargrupooptativa.codigo = gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa AND gradecurriculargrupooptativa.gradecurricular = gradecurricular.codigo ");
		sql.append("INNER JOIN disciplina d ON d.codigo = gradecurriculargrupooptativadisciplina.disciplina ");
		sql.append("INNER JOIN periodoletivo p ON p.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo ");
		sql.append(") AS gradecurriculargrupooptativadisciplina ON TRUE ");
		if (consultarCurso) {
			sql.append("LEFT JOIN areaconhecimento areaCurso ON areaCurso.codigo = curso.areaconhecimento ");
		}
		filtros.add(gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCodigo());
		sql.append("INNER JOIN unidadeensino ON unidadeensino.codigo = ? ");
		if (consultarUnidadeEnsino) {
			sql.append("LEFT JOIN cidade cidadeIes ON cidadeIes.codigo = unidadeensino.cidade ");
			sql.append("LEFT JOIN estado estadoIes ON estadoIes.codigo = cidadeIes.estado ");
			sql.append("LEFT JOIN cidade cidadeMantenedora ON cidadeMantenedora.codigo = unidadeensino.cidademantenedora ");
			sql.append("LEFT JOIN estado estadoMantenedora ON estadoMantenedora.codigo = cidadeMantenedora.estado ");
		}
		filtros.add(gestaoXmlGradeCurricular.getCursoVO().getCodigo());
		sql.append("WHERE curso.codigo = ? ");
//		System.out.println(sql.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		if (!tabelaResultado.next()) {
			throw new Exception("Não foi possivel realizar a montagem do curriculo.");
		}
		gestaoXmlGradeCurricular.getGradeCurricularVO().getListaGradeCurricularTipoAtividadeComplementarVOs().clear();
		gestaoXmlGradeCurricular.getGradeCurricularVO().getPeriodoLetivosVOs().clear();
		GradeCurricularVO gradeCurricular = gestaoXmlGradeCurricular.getGradeCurricularVO();
		gradeCurricular.setCodigo(tabelaResultado.getInt("gradecurricular.codigo"));
		gradeCurricular.setDataCadastro(tabelaResultado.getDate("gradecurricular.datacadastro"));
		gradeCurricular.setCargaHoraria(tabelaResultado.getInt("gradecurricular.cargahoraria"));
		gradeCurricular.setTotalCargaHorariaEstagio(tabelaResultado.getInt("gradecurricular.totalcargahorariaestagio"));
		gradeCurricular.setTotalCargaHorariaAtividadeComplementar(tabelaResultado.getInt("gradecurricular.totalcargahorariaatividadecomplementar"));
		gradeCurricular.setResolucao(tabelaResultado.getString("gradecurricular.resolucao"));
		if (consultarCurso) {
			CursoVO curso = gestaoXmlGradeCurricular.getCursoVO();
			curso.setCodigo(tabelaResultado.getInt("curso.codigo"));
			curso.setNome(tabelaResultado.getString("curso.nome"));
			curso.setNomeDocumentacao(tabelaResultado.getString("curso.nomedocumentacao"));
			curso.setPossuiCodigoEMEC(tabelaResultado.getBoolean("curso.possuicodigoemec"));
			curso.setCodigoEMEC(tabelaResultado.getInt("curso.codigoemec"));
			curso.setNumeroProcessoEMEC(tabelaResultado.getInt("curso.numeroprocessoemec"));
			curso.setTipoProcessoEMEC(tabelaResultado.getString("curso.tipoprocessoemec"));
			curso.setDataCadastroEMEC(tabelaResultado.getDate("curso.datacadastroemec"));
			curso.setDataProtocoloEMEC(tabelaResultado.getDate("curso.dataprotocoloemec"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("curso.tipoautorizacaocursoenum"))) {
				curso.setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("curso.tipoautorizacaocursoenum")));
			}
			curso.setNumeroAutorizacao(tabelaResultado.getString("curso.numeroautorizacao"));
			curso.setDataCredenciamento(tabelaResultado.getDate("curso.datacredenciamento"));
			curso.setDataPublicacaoDO(tabelaResultado.getDate("curso.datapublicacaodo"));
			curso.setVeiculoPublicacao(tabelaResultado.getString("curso.veiculopublicacao"));
			curso.setSecaoPublicacao(tabelaResultado.getInt("curso.secaopublicacao"));
			curso.setPaginaPublicacao(tabelaResultado.getInt("curso.paginapublicacao"));
			curso.setNumeroDOU(tabelaResultado.getInt("curso.numerodou"));
			curso.setTipoProcessoAutorizacaoResolucao(tabelaResultado.getString("curso.tipoprocessoautorizacaoresolucao"));
			curso.setNumeroProcessoAutorizacaoResolucao(tabelaResultado.getString("curso.numeroprocessoautorizacaoresolucao"));
			curso.setDataCadastroAutorizacaoResolucao(tabelaResultado.getDate("curso.datacadastroautorizacaoresolucao"));
			curso.setDataProtocoloAutorizacaoResolucao(tabelaResultado.getDate("curso.dataprotocoloautorizacaoresolucao"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("curso.modalidadecurso"))) {
				curso.setModalidadeCurso(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("curso.modalidadecurso")));
			}
			curso.getAreaConhecimento().setCodigo(tabelaResultado.getInt("areaCurso.codigo"));
			curso.getAreaConhecimento().setNome(tabelaResultado.getString("areaCurso.nome"));
		}
		if (consultarUnidadeEnsino) {
			UnidadeEnsinoVO unidadeEnsino = gestaoXmlGradeCurricular.getUnidadeEnsinoVO();
			unidadeEnsino.setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
			unidadeEnsino.setNome(tabelaResultado.getString("unidadeensino.nome"));
			unidadeEnsino.setCodigoIES(tabelaResultado.getInt("unidadeensino.codigoies"));
			unidadeEnsino.setCNPJ(tabelaResultado.getString("unidadeensino.cnpj"));
			unidadeEnsino.setTelComercial1(tabelaResultado.getString("unidadeensino.telcomercial1"));
			unidadeEnsino.setSite(tabelaResultado.getString("unidadeensino.site"));
			unidadeEnsino.setEmail(tabelaResultado.getString("unidadeensino.email"));
			unidadeEnsino.setNomeExpedicaoDiploma(tabelaResultado.getString("unidadeensino.nomeexpedicaodiploma"));
			unidadeEnsino.setInscEstadual(tabelaResultado.getString("unidadeensino.inscestadual"));
			unidadeEnsino.setInscMunicipal(tabelaResultado.getString("unidadeensino.inscmunicipal"));
			unidadeEnsino.setCaixaPostal(tabelaResultado.getString("unidadeensino.caixapostal"));
			unidadeEnsino.setCredenciamento(tabelaResultado.getString("unidadeensino.credenciamento"));
			unidadeEnsino.setCredenciamentoEadEmTramitacao(tabelaResultado.getBoolean("unidadeensino.credenciamentoeademtramitacao"));
			unidadeEnsino.setNumeroProcessoCredenciamentoEad(tabelaResultado.getString("unidadeensino.numeroprocessocredenciamentoead"));
			unidadeEnsino.setTipoProcessoCredenciamentoEad(tabelaResultado.getString("unidadeensino.tipoprocessocredenciamentoead"));
			unidadeEnsino.setDataCadastroCredenciamentoEad(tabelaResultado.getDate("unidadeensino.datacadastrocredenciamentoead"));
			unidadeEnsino.setDataProtocoloCredenciamentoEad(tabelaResultado.getDate("unidadeensino.dataprotocolocredenciamentoead"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("unidadeensino.tipoautorizacaoead"))) {
				unidadeEnsino.setTipoAutorizacaoEAD(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("unidadeensino.tipoautorizacaoead")));
			}
			unidadeEnsino.setNumeroCredenciamentoEAD(tabelaResultado.getString("unidadeensino.numerocredenciamentoead"));
			unidadeEnsino.setDataCredenciamentoEAD(tabelaResultado.getDate("unidadeensino.datacredenciamentoead"));
			unidadeEnsino.setDataPublicacaoDOEAD(tabelaResultado.getDate("unidadeensino.datapublicacaodoead"));
			unidadeEnsino.setVeiculoPublicacaoCredenciamentoEAD(tabelaResultado.getString("unidadeensino.veiculopublicacaocredenciamentoead"));
			unidadeEnsino.setSecaoPublicacaoCredenciamentoEAD(tabelaResultado.getInt("unidadeensino.secaopublicacaocredenciamentoead"));
			unidadeEnsino.setPaginaPublicacaoCredenciamentoEAD(tabelaResultado.getInt("unidadeensino.paginapublicacaocredenciamentoead"));
			unidadeEnsino.setNumeroDOUCredenciamentoEAD(tabelaResultado.getInt("unidadeensino.numerodoucredenciamentoead"));
			unidadeEnsino.setCredenciamentoEmTramitacao(tabelaResultado.getBoolean("unidadeensino.credenciamentoemtramitacao"));
			unidadeEnsino.setNumeroProcessoCredenciamento(tabelaResultado.getString("unidadeensino.numeroprocessocredenciamento"));
			unidadeEnsino.setTipoProcessoCredenciamento(tabelaResultado.getString("unidadeensino.tipoprocessocredenciamento"));
			unidadeEnsino.setDataCadastroCredenciamento(tabelaResultado.getDate("unidadeensino.datacadastrocredenciamento"));
			unidadeEnsino.setDataProtocoloCredenciamento(tabelaResultado.getDate("unidadeensino.dataprotocolocredenciamento"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("unidadeensino.tipoautorizacaoenum"))) {
				unidadeEnsino.setTipoAutorizacaoEnum(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("unidadeensino.tipoautorizacaoenum")));
			}
			unidadeEnsino.setNumeroCredenciamento(tabelaResultado.getString("unidadeensino.numerocredenciamento"));
			unidadeEnsino.setDataCredenciamento(tabelaResultado.getDate("unidadeensino.datacredenciamento"));
			unidadeEnsino.setDataPublicacaoDO(tabelaResultado.getDate("unidadeensino.datapublicacaodo"));
			unidadeEnsino.setVeiculoPublicacaoCredenciamento(tabelaResultado.getString("unidadeensino.veiculopublicacaocredenciamento"));
			unidadeEnsino.setSecaoPublicacaoCredenciamento(tabelaResultado.getInt("unidadeensino.secaopublicacaocredenciamento"));
			unidadeEnsino.setPaginaPublicacaoCredenciamento(tabelaResultado.getInt("unidadeensino.paginapublicacaocredenciamento"));
			unidadeEnsino.setNumeroDOUCredenciamento(tabelaResultado.getInt("unidadeensino.numerodoucredenciamento"));
			unidadeEnsino.setRecredenciamentoEmTramitacaoEad(tabelaResultado.getBoolean("unidadeensino.recredenciamentoemtramitacaoead"));
			unidadeEnsino.setNumeroProcessoRecredenciamentoEad(tabelaResultado.getString("unidadeensino.numeroprocessorecredenciamentoead"));
			unidadeEnsino.setTipoProcessoRecredenciamentoEad(tabelaResultado.getString("unidadeensino.tipoprocessorecredenciamentoead"));
			unidadeEnsino.setDataCadastroRecredenciamentoEad(tabelaResultado.getDate("unidadeensino.datacadastrorecredenciamentoead"));
			unidadeEnsino.setDataProtocoloRecredenciamentoEad(tabelaResultado.getDate("unidadeensino.dataprotocolorecredenciamentoead"));
			unidadeEnsino.setNumeroRecredenciamentoEAD(tabelaResultado.getString("unidadeensino.numerorecredenciamentoead"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("unidadeensino.tipoautorizacaorecredenciamentoead"))) {
				unidadeEnsino.setTipoAutorizacaoRecredenciamentoEAD(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("unidadeensino.tipoautorizacaorecredenciamentoead")));
			}
			unidadeEnsino.setDataRecredenciamentoEAD(tabelaResultado.getDate("unidadeensino.datarecredenciamentoead"));
			unidadeEnsino.setDataPublicacaoRecredenciamentoEAD(tabelaResultado.getDate("unidadeensino.datapublicacaorecredenciamentoead"));
			unidadeEnsino.setVeiculoPublicacaoRecredenciamentoEAD(tabelaResultado.getString("unidadeensino.veiculopublicacaorecredenciamentoead"));
			unidadeEnsino.setSecaoPublicacaoRecredenciamentoEAD(tabelaResultado.getInt("unidadeensino.secaopublicacaorecredenciamentoead"));
			unidadeEnsino.setPaginaPublicacaoRecredenciamentoEAD(tabelaResultado.getInt("unidadeensino.paginapublicacaorecredenciamentoead"));
			unidadeEnsino.setNumeroDOURecredenciamentoEAD(tabelaResultado.getInt("unidadeensino.numerodourecredenciamentoead"));
			unidadeEnsino.setRecredenciamentoEmTramitacao(tabelaResultado.getBoolean("unidadeensino.recredenciamentoemtramitacao"));
			unidadeEnsino.setNumeroProcessoRecredenciamento(tabelaResultado.getString("unidadeensino.numeroprocessorecredenciamento"));
			unidadeEnsino.setTipoProcessoRecredenciamento(tabelaResultado.getString("unidadeensino.tipoprocessorecredenciamento"));
			unidadeEnsino.setDataCadastroRecredenciamento(tabelaResultado.getDate("unidadeensino.datacadastrorecredenciamento"));
			unidadeEnsino.setDataProtocoloRecredenciamento(tabelaResultado.getDate("unidadeensino.dataprotocolorecredenciamento"));
			unidadeEnsino.setNumeroRecredenciamento(tabelaResultado.getString("unidadeensino.numerorecredenciamento"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("unidadeensino.tipoautorizacaorecredenciamento"))) {
				unidadeEnsino.setTipoAutorizacaoRecredenciamento(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("unidadeensino.tipoautorizacaorecredenciamento")));
			}
			unidadeEnsino.setDataRecredenciamento(tabelaResultado.getDate("unidadeensino.datarecredenciamento"));
			unidadeEnsino.setDataPublicacaoRecredenciamento(tabelaResultado.getDate("unidadeensino.datapublicacaorecredenciamento"));
			unidadeEnsino.setVeiculoPublicacaoRecredenciamento(tabelaResultado.getString("unidadeensino.veiculopublicacaorecredenciamento"));
			unidadeEnsino.setSecaoPublicacaoRecredenciamento(tabelaResultado.getInt("unidadeensino.secaopublicacaorecredenciamento"));
			unidadeEnsino.setPaginaPublicacaoRecredenciamento(tabelaResultado.getInt("unidadeensino.paginapublicacaorecredenciamento"));
			unidadeEnsino.setNumeroDOURecredenciamento(tabelaResultado.getInt("unidadeensino.numerodourecredenciamento"));
			unidadeEnsino.setRenovacaoRecredenciamentoEmTramitacaoEad(tabelaResultado.getBoolean("unidadeensino.renovacaorecredenciamentoemtramitacaoead"));
			unidadeEnsino.setNumeroProcessoRenovacaoRecredenciamentoEad(tabelaResultado.getString("unidadeensino.numeroprocessorenovacaorecredenciamentoead"));
			unidadeEnsino.setTipoProcessoRenovacaoRecredenciamentoEad(tabelaResultado.getString("unidadeensino.tipoprocessorenovacaorecredenciamentoead"));
			unidadeEnsino.setDataCadastroRenovacaoRecredenciamentoEad(tabelaResultado.getDate("unidadeensino.datacadastrorenovacaorecredenciamentoead"));
			unidadeEnsino.setDataProtocoloRenovacaoRecredenciamentoEad(tabelaResultado.getDate("unidadeensino.dataprotocolorenovacaorecredenciamentoead"));
			unidadeEnsino.setNumeroRenovacaoRecredenciamentoEAD(tabelaResultado.getString("unidadeensino.numerorenovacaorecredenciamentoead"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("unidadeensino.tipoautorizacaorenovacaorecredenciamentoead"))) {
				unidadeEnsino.setTipoAutorizacaoRenovacaoRecredenciamentoEAD(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("unidadeensino.tipoautorizacaorenovacaorecredenciamentoead")));
			}
			unidadeEnsino.setDataRenovacaoRecredenciamentoEAD(tabelaResultado.getDate("unidadeensino.datarenovacaorecredenciamentoead"));
			unidadeEnsino.setDataPublicacaoRenovacaoRecredenciamentoEAD(tabelaResultado.getDate("unidadeensino.datapublicacaorenovacaorecredenciamentoead"));
			unidadeEnsino.setVeiculoPublicacaoRenovacaoRecredenciamentoEAD(tabelaResultado.getString("unidadeensino.veiculopublicacaorenovacaorecredenciamentoead"));
			unidadeEnsino.setSecaoPublicacaoRenovacaoRecredenciamentoEAD(tabelaResultado.getInt("unidadeensino.secaopublicacaorenovacaorecredenciamentoead"));
			unidadeEnsino.setPaginaPublicacaoRenovacaoRecredenciamentoEAD(tabelaResultado.getInt("unidadeensino.paginapublicacaorenovacaorecredenciamentoead"));
			unidadeEnsino.setNumeroDOURenovacaoRecredenciamentoEAD(tabelaResultado.getInt("unidadeensino.numerodourenovacaorecredenciamentoead"));
			unidadeEnsino.setRenovacaoRecredenciamentoEmTramitacao(tabelaResultado.getBoolean("unidadeensino.renovacaorecredenciamentoemtramitacao"));
			unidadeEnsino.setNumeroProcessoRenovacaoRecredenciamento(tabelaResultado.getString("unidadeensino.numeroprocessorenovacaorecredenciamento"));
			unidadeEnsino.setTipoProcessoRenovacaoRecredenciamento(tabelaResultado.getString("unidadeensino.tipoprocessorenovacaorecredenciamento"));
			unidadeEnsino.setDataCadastroRenovacaoRecredenciamento(tabelaResultado.getDate("unidadeensino.datacadastrorenovacaorecredenciamento"));
			unidadeEnsino.setDataProtocoloRenovacaoRecredenciamento(tabelaResultado.getDate("unidadeensino.dataprotocolorenovacaorecredenciamento"));
			unidadeEnsino.setNumeroRenovacaoRecredenciamento(tabelaResultado.getString("unidadeensino.numerorenovacaorecredenciamento"));
			if (Uteis.isAtributoPreenchido(tabelaResultado.getString("unidadeensino.tipoautorizacaorenovacaorecredenciamento"))) {
				unidadeEnsino.setTipoAutorizacaoRenovacaoRecredenciamento(TipoAutorizacaoCursoEnum.valueOf(tabelaResultado.getString("unidadeensino.tipoautorizacaorenovacaorecredenciamento")));
			}
			unidadeEnsino.setDataRenovacaoRecredenciamento(tabelaResultado.getDate("unidadeensino.datarenovacaorecredenciamento"));
			unidadeEnsino.setDataPublicacaoRenovacaoRecredenciamento(tabelaResultado.getDate("unidadeensino.datapublicacaorenovacaorecredenciamento"));
			unidadeEnsino.setVeiculoPublicacaoRenovacaoRecredenciamento(tabelaResultado.getString("unidadeensino.veiculopublicacaorenovacaorecredenciamento"));
			unidadeEnsino.setSecaoPublicacaoRenovacaoRecredenciamento(tabelaResultado.getInt("unidadeensino.secaopublicacaorenovacaorecredenciamento"));
			unidadeEnsino.setPaginaPublicacaoRenovacaoRecredenciamento(tabelaResultado.getInt("unidadeensino.paginapublicacaorenovacaorecredenciamento"));
			unidadeEnsino.setNumeroDOURenovacaoRecredenciamento(tabelaResultado.getInt("unidadeensino.numerodourenovacaorecredenciamento"));
			unidadeEnsino.setMantenedora(tabelaResultado.getString("unidadeensino.mantenedora"));
			unidadeEnsino.setCnpjMantenedora(tabelaResultado.getString("unidadeensino.cnpjmantenedora"));
			unidadeEnsino.setUtilizarEnderecoUnidadeEnsinoMantenedora(tabelaResultado.getBoolean("unidadeensino.utilizarenderecounidadeensinomantenedora"));
			unidadeEnsino.setEnderecoMantenedora(tabelaResultado.getString("unidadeensino.enderecomantenedora"));
			unidadeEnsino.setNumeroMantenedora(tabelaResultado.getString("unidadeensino.numeromantenedora"));
			unidadeEnsino.setComplementoMantenedora(tabelaResultado.getString("unidadeensino.complementomantenedora"));
			unidadeEnsino.setBairroMantenedora(tabelaResultado.getString("unidadeensino.bairromantenedora"));
			unidadeEnsino.setCepMantenedora(tabelaResultado.getString("unidadeensino.cepmantenedora"));
			unidadeEnsino.getCidadeMantenedora().setCodigo(tabelaResultado.getInt("cidadeMantenedora.codigo"));
			unidadeEnsino.getCidadeMantenedora().setNome(tabelaResultado.getString("cidadeMantenedora.nome"));
			unidadeEnsino.getCidadeMantenedora().setCodigoIBGE(tabelaResultado.getString("cidadeMantenedora.codigoIBGE"));
			unidadeEnsino.getCidadeMantenedora().getEstado().setCodigo(tabelaResultado.getInt("estadoMantenedora.codigo"));
			unidadeEnsino.getCidadeMantenedora().getEstado().setSigla(tabelaResultado.getString("estadoMantenedora.sigla"));
			unidadeEnsino.setEndereco(tabelaResultado.getString("unidadeensino.endereco"));
			unidadeEnsino.setNumero(tabelaResultado.getString("unidadeensino.numero"));
			unidadeEnsino.setComplemento(tabelaResultado.getString("unidadeensino.complemento"));
			unidadeEnsino.setSetor(tabelaResultado.getString("unidadeensino.setor"));
			unidadeEnsino.setCEP(tabelaResultado.getString("unidadeensino.cep"));
			unidadeEnsino.getCidade().setCodigo(tabelaResultado.getInt("cidadeIes.codigo"));
			unidadeEnsino.getCidade().setCodigoIBGE(tabelaResultado.getString("cidadeIes.codigoibge"));
			unidadeEnsino.getCidade().setNome(tabelaResultado.getString("cidadeIes.nome"));
			unidadeEnsino.getCidade().getEstado().setCodigo(tabelaResultado.getInt("estadoIes.codigo"));
			unidadeEnsino.getCidade().getEstado().setSigla(tabelaResultado.getString("estadoIes.sigla"));
			unidadeEnsino.setCaminhoBaseLogoRelatorio(tabelaResultado.getString("unidadeensino.caminhobaselogorelatorio"));
			unidadeEnsino.setNomeArquivoLogoRelatorio(tabelaResultado.getString("unidadeensino.nomearquivologorelatorio"));
		}
		montarDadosDadosGradeCurricularTipoAtividadeComplementar(tabelaResultado, gestaoXmlGradeCurricular);
		montarDadosDadosPeriodoLetivo(tabelaResultado, gestaoXmlGradeCurricular);
	}

	public void montarDadosDadosGradeCurricularTipoAtividadeComplementar(SqlRowSet tabelaResultado, GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular) throws Exception {
		String gradeCurricularTipoAtividadeComplementar = tabelaResultado.getString("gradecurriculartipoatividadecomplementar");
		if (Uteis.isAtributoPreenchido(gradeCurricularTipoAtividadeComplementar)) {
			JSONArray jsonTipoAtividadeComplementar = new JSONArray(gradeCurricularTipoAtividadeComplementar);
			if (gradeCurricularTipoAtividadeComplementar != null && gradeCurricularTipoAtividadeComplementar.length() > 0) {
				for (int i = 0; i < jsonTipoAtividadeComplementar.length(); i++) {
					JSONObject jsonObject = jsonTipoAtividadeComplementar.getJSONObject(i);
					if (jsonObject != null) {
						GradeCurricularTipoAtividadeComplementarVO gradeCurricularTipoAtividadeComplementarVO = new GradeCurricularTipoAtividadeComplementarVO();
						gradeCurricularTipoAtividadeComplementarVO.setCodigo(jsonObject.getInt("gradecurriculartipoatividadecomplementar.codigo"));
						if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculartipoatividadecomplementar.cargahoraria"))) {
							gradeCurricularTipoAtividadeComplementarVO.setCargaHoraria(jsonObject.getInt("gradecurriculartipoatividadecomplementar.cargahoraria"));
						}
						if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculartipoatividadecomplementar.horasMinimasExigida"))) {
							gradeCurricularTipoAtividadeComplementarVO.setHorasMinimasExigida(jsonObject.getInt("gradecurriculartipoatividadecomplementar.horasMinimasExigida"));
						}
						gradeCurricularTipoAtividadeComplementarVO.getTipoAtividadeComplementarVO().setCodigo(jsonObject.getInt("tipoatividadecomplementar.codigo"));
						gradeCurricularTipoAtividadeComplementarVO.getTipoAtividadeComplementarVO().setNome(jsonObject.getString("tipoatividadecomplementar.nome"));
						if (Uteis.isAtributoPreenchido(jsonObject.get("tipoAtividadeSuperior.codigo"))) {
							gradeCurricularTipoAtividadeComplementarVO.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().setCodigo(jsonObject.getInt("tipoAtividadeSuperior.codigo"));
							gradeCurricularTipoAtividadeComplementarVO.getTipoAtividadeComplementarVO().getTipoAtividadeComplementarSuperior().setNome(jsonObject.getString("tipoAtividadeSuperior.nome"));
						}
						if (!gestaoXmlGradeCurricular.getGradeCurricularVO().getListaGradeCurricularTipoAtividadeComplementarVOs().stream().anyMatch(a -> a.getCodigo().equals(gradeCurricularTipoAtividadeComplementarVO.getCodigo()))) {
							gestaoXmlGradeCurricular.getGradeCurricularVO().getListaGradeCurricularTipoAtividadeComplementarVOs().add(gradeCurricularTipoAtividadeComplementarVO);
						}
					}
				}
			}
		}
	}

	public void montarDadosDadosPeriodoLetivo(SqlRowSet tabelaResultado, GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular) throws Exception {
		String periodoLetivo = tabelaResultado.getString("periodoLetivo");
		String gradeDisciplina = tabelaResultado.getString("gradedisciplina");
		String disciplinaPreRequisito = tabelaResultado.getString("disciplinaprerequisito");
		String mapaEquivalenciaDisciplina = tabelaResultado.getString("mapaEquivalenciaDisciplina");
		String gradeCurricularGrupoOptativaDisciplina = tabelaResultado.getString("gradecurriculargrupooptativadisciplina");
		Map<Integer, PeriodoLetivoVO> mapPeriodoLetivo = new HashMap<>(0);
		JSONArray jsonPeriodoLetivo = new JSONArray(periodoLetivo);
		if (jsonPeriodoLetivo != null && jsonPeriodoLetivo.length() > 0) {
			for (int i = 0; i < jsonPeriodoLetivo.length(); i++) {
				JSONObject jsonObject = jsonPeriodoLetivo.getJSONObject(i);
				if (jsonObject != null) {
					PeriodoLetivoVO periodoLetivoVO = new PeriodoLetivoVO();
					periodoLetivoVO.setCodigo(jsonObject.getInt("periodoletivo.codigo"));
					periodoLetivoVO.setDescricao(jsonObject.getString("periodoletivo.nome"));
					if (!mapPeriodoLetivo.containsKey(periodoLetivoVO.getCodigo())) {
						mapPeriodoLetivo.put(periodoLetivoVO.getCodigo(), periodoLetivoVO);
					}
				}
			}
		} else {
			throw new Exception("Não foi possivel montar os dados do periodo letivo.");
		}

		JSONArray jsonGradeDisciplina = new JSONArray(gradeDisciplina);
		if (jsonGradeDisciplina != null && jsonGradeDisciplina.length() > 0) {
			for (int i = 0; i < jsonGradeDisciplina.length(); i++) {
				JSONObject jsonObject = jsonGradeDisciplina.getJSONObject(i);
				if (jsonObject != null) {
					GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
					gradeDisciplinaVO.setCodigo(jsonObject.getInt("gradedisciplina.codigo"));
					gradeDisciplinaVO.getPeriodoLetivoVO().setCodigo(jsonObject.getInt("gradedisciplina.periodoletivo"));
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.disciplinaestagio"))) {
						gradeDisciplinaVO.setDisciplinaEstagio(jsonObject.getBoolean("gradedisciplina.disciplinaestagio"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.cargahoraria"))) {
						gradeDisciplinaVO.setCargaHoraria(jsonObject.getInt("gradedisciplina.cargahoraria"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.tipodisciplina"))) {
						gradeDisciplinaVO.setTipoDisciplina(jsonObject.getString("gradedisciplina.tipodisciplina"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.modalidadedisciplina"))) {
						gradeDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(jsonObject.getString("gradedisciplina.modalidadedisciplina")));
					}
//					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.cargahorariaead"))) {
//						gradeDisciplinaVO.setCargaHorariaEAD(jsonObject.getInt("gradedisciplina.cargahorariaead"));
//					}
//					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.cargahorariaextensao"))) {
//						gradeDisciplinaVO.setCargaHorariaExtensao(jsonObject.getInt("gradedisciplina.cargahorariaextensao"));
//					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.cargahorariapratica"))) {
						gradeDisciplinaVO.setCargaHorariaPratica(jsonObject.getInt("gradedisciplina.cargahorariapratica"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.cargahorariateorica"))) {
						gradeDisciplinaVO.setCargaHorariaTeorica(jsonObject.getInt("gradedisciplina.cargahorariateorica"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("disciplina.codigo"))) {
						gradeDisciplinaVO.getDisciplina().setCodigo(jsonObject.getInt("disciplina.codigo"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("disciplina.nome"))) {
						gradeDisciplinaVO.getDisciplina().setNome(jsonObject.getString("disciplina.nome"));
					}
					if (Uteis.isAtributoPreenchido(jsonObject.get("gradedisciplina.utilizarEmissaoXmlDiploma"))) {
						gradeDisciplinaVO.setUtilizarEmissaoXmlDiploma(jsonObject.getBoolean("gradedisciplina.utilizarEmissaoXmlDiploma"));
					}
					if (mapPeriodoLetivo.containsKey(gradeDisciplinaVO.getPeriodoLetivoVO().getCodigo())) {
						PeriodoLetivoVO periodoLetivoVO = mapPeriodoLetivo.get(gradeDisciplinaVO.getPeriodoLetivoVO().getCodigo());
						if (!periodoLetivoVO.getMapGradeDisciplina().containsKey(gradeDisciplinaVO.getCodigo())) {
							periodoLetivoVO.getMapGradeDisciplina().put(gradeDisciplinaVO.getCodigo(), gradeDisciplinaVO);
						}
					}
				}
			}
		} else {
			throw new Exception("Não foi possivel montar os dados das grades disciplinas.");
		}
		if (Uteis.isAtributoPreenchido(disciplinaPreRequisito)) {
			JSONArray jsonPreRequisito = new JSONArray(disciplinaPreRequisito);
			if (disciplinaPreRequisito != null && disciplinaPreRequisito.length() > 0) {
				for (int i = 0; i < jsonPreRequisito.length(); i++) {
					JSONObject jsonObject = jsonPreRequisito.getJSONObject(i);
					if (jsonObject != null) {
						Integer codigoPeriodoLetivo = jsonObject.getInt("periodoletivo.codigo");
						Integer codigoGradeDisciplina = jsonObject.getInt("gradedisciplina.codigo");
						DisciplinaPreRequisitoVO disciplinaPreRequisitoVO = new DisciplinaPreRequisitoVO();
						disciplinaPreRequisitoVO.setCodigo(jsonObject.getInt("disciplinaprerequisito.codigo"));
						disciplinaPreRequisitoVO.setGradeDisciplina(jsonObject.getInt("disciplinaprerequisito.gradedisciplina"));
						disciplinaPreRequisitoVO.getDisciplina().setCodigo(jsonObject.getInt("disciplinarequisito.codigo"));
						disciplinaPreRequisitoVO.getDisciplina().setNome(jsonObject.getString("disciplinarequisito.nome"));
						if (mapPeriodoLetivo.containsKey(codigoPeriodoLetivo)) {
							PeriodoLetivoVO periodoLetivoVO = mapPeriodoLetivo.get(codigoPeriodoLetivo);
							if (periodoLetivoVO.getMapGradeDisciplina().containsKey(codigoGradeDisciplina)) {
								GradeDisciplinaVO gradeDisciplinaVO = periodoLetivoVO.getMapGradeDisciplina().get(codigoGradeDisciplina);
								if (!gradeDisciplinaVO.getDisciplinaRequisitoVOs().stream().anyMatch(d -> d.getCodigo().equals(disciplinaPreRequisitoVO.getCodigo()))) {
									gradeDisciplinaVO.getDisciplinaRequisitoVOs().add(disciplinaPreRequisitoVO);
								}
							}
						}
					}
				}
			}
		}
		if (Uteis.isAtributoPreenchido(mapaEquivalenciaDisciplina)) {
			JSONArray jsonMapaEquivalencia = new JSONArray(mapaEquivalenciaDisciplina);
			if (jsonMapaEquivalencia != null && jsonMapaEquivalencia.length() > 0) {
				for (int i = 0; i < jsonMapaEquivalencia.length(); i++) {
					JSONObject jsonObject = jsonMapaEquivalencia.getJSONObject(i);
					if (jsonObject != null) {
						Integer codigoPeriodoLetivo = jsonObject.getInt("periodoletivo.codigo");
						Integer codigoGradeDisciplina = jsonObject.getInt("gradedisciplina.codigo");
						Integer disciplina = jsonObject.getInt("mapaequivalenciadisciplinamatrizcurricular.disciplina");
						Integer disciplinaEquivalente = jsonObject.getInt("mapaequivalenciadisciplinacursada.disciplina");
						if (mapPeriodoLetivo.containsKey(codigoPeriodoLetivo)) {
							PeriodoLetivoVO periodoLetivoVO = mapPeriodoLetivo.get(codigoPeriodoLetivo);
							if (periodoLetivoVO.getMapGradeDisciplina().containsKey(codigoGradeDisciplina)) {
								GradeDisciplinaVO gradeDisciplinaVO = periodoLetivoVO.getMapGradeDisciplina().get(codigoGradeDisciplina);
								if (gradeDisciplinaVO.getDisciplina().getCodigo().equals(disciplina) && !gradeDisciplinaVO.getDisciplina().getDisciplinasEquivalentes().stream().anyMatch(d -> d.equals(disciplinaEquivalente))) {
									gradeDisciplinaVO.getDisciplina().getDisciplinasEquivalentes().add(disciplinaEquivalente);
								}
							}
						}
					}
				}
			}
		}
		if (Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplina)) {
			JSONArray jsonGradeCurricularGrupoOptativaDisciplina = new JSONArray(gradeCurricularGrupoOptativaDisciplina);
			if (jsonGradeCurricularGrupoOptativaDisciplina != null && jsonGradeCurricularGrupoOptativaDisciplina.length() > 0) {
				for (int i = 0; i < jsonGradeCurricularGrupoOptativaDisciplina.length(); i++) {
					JSONObject jsonObject = jsonGradeCurricularGrupoOptativaDisciplina.getJSONObject(i);
					if (jsonObject != null) {
						if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculargrupooptativa.codigo"))) {
							Integer codigoPeriodoLetivo = jsonObject.getInt("periodoletivo.codigo");
							GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO = new GradeCurricularGrupoOptativaVO();
							gradeCurricularGrupoOptativaVO.setCodigo(jsonObject.getInt("gradecurriculargrupooptativa.codigo"));
							gradeCurricularGrupoOptativaVO.setDescricao(jsonObject.getString("gradecurriculargrupooptativa.descricao"));
							GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
							gradeCurricularGrupoOptativaDisciplinaVO.setCodigo(jsonObject.getInt("gradecurriculargrupooptativadisciplina.codigo"));
							gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setCodigo(jsonObject.getInt("disciplina.codigo"));
							gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().setNome(jsonObject.getString("disciplina.nome"));
							if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculargrupooptativadisciplina.cargahoraria"))) {
								gradeCurricularGrupoOptativaDisciplinaVO.setCargaHoraria(jsonObject.getInt("gradecurriculargrupooptativadisciplina.cargahoraria"));
							}
							if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculargrupooptativadisciplina.cargahorariapratica"))) {
								gradeCurricularGrupoOptativaDisciplinaVO.setCargaHorariaPratica(jsonObject.getInt("gradecurriculargrupooptativadisciplina.cargahorariapratica"));
							}
//							if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculargrupooptativadisciplina.cargahorariaead"))) {
//								gradeCurricularGrupoOptativaDisciplinaVO.setCargaHorariaEAD(jsonObject.getInt("gradecurriculargrupooptativadisciplina.cargahorariaead"));
//							}
//							if (Uteis.isAtributoPreenchido(jsonObject.get("gradecurriculargrupooptativadisciplina.cargahorariaextensao"))) {
//								gradeCurricularGrupoOptativaDisciplinaVO.setCargaHorariaExtensao(jsonObject.getInt("gradecurriculargrupooptativadisciplina.cargahorariaextensao"));
//							}
							gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs().add(gradeCurricularGrupoOptativaDisciplinaVO);
							if (mapPeriodoLetivo.containsKey(codigoPeriodoLetivo)) {
								PeriodoLetivoVO periodoLetivoVO = mapPeriodoLetivo.get(codigoPeriodoLetivo);
								if (Uteis.isAtributoPreenchido(periodoLetivoVO.getGradeCurricularGrupoOptativa())) {
									periodoLetivoVO.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs().addAll(gradeCurricularGrupoOptativaVO.getGradeCurricularGrupoOptativaDisciplinaVOs());
								} else {
									periodoLetivoVO.setGradeCurricularGrupoOptativa(gradeCurricularGrupoOptativaVO);
								}
							}
						}
					}
				}
			}
		}
		if (Uteis.isAtributoPreenchido(mapPeriodoLetivo)) {
			mapPeriodoLetivo.values().stream().forEach(p -> {
				if (!p.getMapGradeDisciplina().isEmpty()) {
					p.getGradeDisciplinaVOs().addAll(p.getMapGradeDisciplina().values());
				}
			});
			gestaoXmlGradeCurricular.getGradeCurricularVO().getPeriodoLetivosVOs().addAll(mapPeriodoLetivo.values());
		}
	}

	public void montarDadosCoordenadorCurso(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, UsuarioVO usuario) throws Exception {
		List<FuncionarioVO> funcionarioVOs = getFacadeFactory().getFuncionarioFacade().consultaRapidaCoordenadorPorCurso(gestaoXmlGradeCurricular.getCursoVO().getCodigo(), gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCodigo(), Boolean.FALSE, usuario);
		if (Uteis.isAtributoPreenchido(funcionarioVOs)) {
			gestaoXmlGradeCurricular.setFuncionarioPrimario(funcionarioVOs.get(0));
			List<CargoVO> cargoVOs = getFacadeFactory().getCargoFacade().consultarCargoPorFuncionario(gestaoXmlGradeCurricular.getFuncionarioPrimario().getCodigo(), Boolean.FALSE, usuario);
			if (Uteis.isAtributoPreenchido(cargoVOs)) {
				gestaoXmlGradeCurricular.setCargoPrimario(cargoVOs.get(0));
			} else {
				throw new Exception("Não foi encontrado um cargo para o funcionário " + gestaoXmlGradeCurricular.getFuncionarioPrimario().getPessoa().getNome());
			}
		} else {
			throw new Exception("Não foi encontrado um coordenador para o curso " + gestaoXmlGradeCurricular.getCursoVO().getNome());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void realizarMontagemCurriculoEscolarDigitalLote(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, ProgressBarVO progressBarVO, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio) throws Exception {
		try {
			Map<Integer, GestaoXmlGradeCurricularVO> mapCoordenadorCurso = new HashMap<>(0);
			Map<Integer, CursoVO> mapCurso = new HashMap<>(0);
			Map<Integer, UnidadeEnsinoVO> mapUnidadeEnsino = new HashMap<>(0);
			if ((!Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getControleConsultaOtimizado().getListaConsulta())) || (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getControleConsultaOtimizado().getListaConsulta())) && ((List<GestaoXmlGradeCurricularVO>) gestaoXmlGradeCurricular.getControleConsultaOtimizado().getListaConsulta()).stream().noneMatch(GestaoXmlGradeCurricularVO::getSelecionado)) {
				throw new Exception("Deve ser informado as matrizes na qual vai ser gerado o currículo escolar digital.");
			}
			List<GestaoXmlGradeCurricularVO> gestaoXmlGradeCurricularVOs = ((List<GestaoXmlGradeCurricularVO>) gestaoXmlGradeCurricular.getControleConsultaOtimizado().getListaConsulta()).stream().filter(GestaoXmlGradeCurricularVO::getSelecionado).collect(Collectors.toList());
			for (int i = 0; i < gestaoXmlGradeCurricularVOs.size(); i++) {
				if (progressBarVO.getForcarEncerramento()) {
					break;
				}
				GestaoXmlGradeCurricularVO gestaoXmlGradeCurricularVO = gestaoXmlGradeCurricularVOs.get(i);
				try {
					superControleRelatorio.setCaminhoRelatorio(Constantes.EMPTY);
					superControleRelatorio.setFazerDownload(Boolean.FALSE);
					progressBarVO.setStatus("Processando Matriz " + gestaoXmlGradeCurricularVO.getGradeCurricularVO().getNome() + " (" + (Integer.valueOf(i + 1)) + " de " + progressBarVO.getMaxValue() + ")");
					gestaoXmlGradeCurricularVO.setMotivoRejeicao(gestaoXmlGradeCurricular.getMotivoRejeicao());
					gestaoXmlGradeCurricularVO.setConsistirException(new ConsistirException());
					if (gestaoXmlGradeCurricular.getCoordenadorCursoAssinateEcpfLote()) {
						if (!mapCoordenadorCurso.containsKey(gestaoXmlGradeCurricularVO.getCursoVO().getCodigo())) {
							montarDadosCoordenadorCurso(gestaoXmlGradeCurricularVO, progressBarVO.getUsuarioVO());
							mapCoordenadorCurso.put(gestaoXmlGradeCurricularVO.getCursoVO().getCodigo(), (GestaoXmlGradeCurricularVO) gestaoXmlGradeCurricularVO.clone());
						} else {
							GestaoXmlGradeCurricularVO gestao = mapCoordenadorCurso.get(gestaoXmlGradeCurricularVO.getCursoVO().getCodigo());
							gestaoXmlGradeCurricularVO.setFuncionarioPrimario(gestao.getFuncionarioPrimario());
							gestaoXmlGradeCurricularVO.setCargoPrimario(gestao.getCargoPrimario());
						}
					} else {
						gestaoXmlGradeCurricularVO.setFuncionarioPrimario(gestaoXmlGradeCurricular.getFuncionarioPrimario());
						gestaoXmlGradeCurricularVO.setCargoPrimario(gestaoXmlGradeCurricular.getCargoPrimario());
						gestaoXmlGradeCurricularVO.setTituloFuncionarioPrimario(gestaoXmlGradeCurricular.getTituloFuncionarioPrimario());
					}
					gestaoXmlGradeCurricularVO.setFuncionarioSecundario(gestaoXmlGradeCurricular.getFuncionarioSecundario());
					gestaoXmlGradeCurricularVO.setCargoSecundario(gestaoXmlGradeCurricular.getCargoSecundario());
					gestaoXmlGradeCurricularVO.setTituloFuncionarioSecundario(gestaoXmlGradeCurricular.getTituloFuncionarioSecundario());
					gestaoXmlGradeCurricularVO.validarDados();
					Boolean consultarCurso = !mapCurso.containsKey(gestaoXmlGradeCurricularVO.getCursoVO().getCodigo());
					Boolean consultarUnidadeEnsino = !mapUnidadeEnsino.containsKey(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().getCodigo());
					carregarDadosGestaoXmlGradeCurricular(gestaoXmlGradeCurricularVO, consultarUnidadeEnsino, consultarCurso);
					if (consultarCurso) {
						mapCurso.put(gestaoXmlGradeCurricularVO.getCursoVO().getCodigo(), (CursoVO) gestaoXmlGradeCurricularVO.getCursoVO().clone());
					} else {
						gestaoXmlGradeCurricularVO.setCursoVO(mapCurso.get(gestaoXmlGradeCurricularVO.getCursoVO().getCodigo()));
					}
					if (consultarUnidadeEnsino) {
						mapUnidadeEnsino.put(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().getCodigo(), (UnidadeEnsinoVO) gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().clone());
					} else {
						gestaoXmlGradeCurricularVO.setUnidadeEnsinoVO(mapUnidadeEnsino.get(gestaoXmlGradeCurricularVO.getUnidadeEnsinoVO().getCodigo()));
					}
					realizarGeracaoCurriculoEscolarDigital(gestaoXmlGradeCurricularVO, superParametroRelVO, superControleRelatorio, progressBarVO.getUsuarioVO(), progressBarVO.getConfiguracaoGeralSistemaVO());
				} catch (ConsistirException ce) {
					gestaoXmlGradeCurricular.getListaGestaoXmlGradeCurricularErro().add(gestaoXmlGradeCurricularVO);
				} catch (Exception e) {
					gestaoXmlGradeCurricularVO.getConsistirException().getListaMensagemErro().add(Uteis.isAtributoPreenchido(e.getMessage()) ? e.getMessage() : "Aconteceu um problema inesperado, informe ao administrador e o mesmo será solucionado.");
					gestaoXmlGradeCurricular.getListaGestaoXmlGradeCurricularErro().add(gestaoXmlGradeCurricularVO);
				}
			}
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(gestaoXmlGradeCurricular.getFuncionarioSecundario().getCodigo().toString(), GestaoXmlGradeCurricular.idEntidade, "FuncionarioIesEmissora", null, gestaoXmlGradeCurricular.getFuncionarioSecundario().getCodigo(), Boolean.FALSE, null, gestaoXmlGradeCurricular.getTituloFuncionarioSecundario(), null, null, progressBarVO.getUsuarioVO());
		} catch (Exception exe) {
			throw exe;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarMontagemCurriculoEscolarDigital(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		gestaoXmlGradeCurricular.validarDados();
		carregarDadosGestaoXmlGradeCurricular(gestaoXmlGradeCurricular, Boolean.TRUE, Boolean.TRUE);
		realizarGeracaoCurriculoEscolarDigital(gestaoXmlGradeCurricular, superParametroRelVO, superControleRelatorio, usuario, configuracaoGeralSistemaVO);
		getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(gestaoXmlGradeCurricular.getFuncionarioSecundario().getCodigo().toString(), GestaoXmlGradeCurricular.idEntidade, "FuncionarioIesEmissora", null, gestaoXmlGradeCurricular.getFuncionarioSecundario().getCodigo(), Boolean.FALSE, null, gestaoXmlGradeCurricular.getTituloFuncionarioSecundario(), null, null, usuario);
	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarGeracaoCurriculoEscolarDigital(GestaoXmlGradeCurricularVO gestaoXmlGradeCurricular, SuperParametroRelVO superParametroRelVO, SuperControleRelatorio superControleRelatorio, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		gestaoXmlGradeCurricular.setConsistirException(new ConsistirException());
		String titulo = "Matriz Curricular";
		String tipoLayout = "DisciplinasGrade3Rel";
		String tipoRelatorio = "curso";
		String design = DisciplinasGradeRel.getDesignIReportRelatorio("DisciplinasGrade3Rel");
		List listaRegistro = null;
		getFacadeFactory().getDisciplinaGradeRelFacade().validarDados(tipoRelatorio, gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCodigo(), gestaoXmlGradeCurricular.getCursoVO().getCodigo(), 0, gestaoXmlGradeCurricular.getGradeCurricularVO().getCodigo());
		listaRegistro = getFacadeFactory().getDisciplinaGradeRelFacade().criarObjeto(gestaoXmlGradeCurricular.getGradeCurricularVO().getCodigo(), Boolean.TRUE, tipoLayout);
		superParametroRelVO.setNomeDesignIreport(design);
		superParametroRelVO.adicionarParametro("endereco", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getEndereco());
		superParametroRelVO.adicionarParametro("numero", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getNumero());
		superParametroRelVO.adicionarParametro("bairro", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getSetor());
		superParametroRelVO.adicionarParametro("cidade", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCidade().getNome());
		superParametroRelVO.adicionarParametro("estado", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCidade().getEstado().getSigla());
		superParametroRelVO.adicionarParametro("cep", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCEP());
		superParametroRelVO.adicionarParametro("fone", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getTelComercial1());
		superParametroRelVO.adicionarParametro("site", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getSite());
		superParametroRelVO.adicionarParametro("email", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getEmail());
		superParametroRelVO.adicionarParametro("nomeExpedicaoDiploma", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getNomeExpedicaoDiploma());
		superParametroRelVO.adicionarParametro("inscEstadual", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getInscEstadual());
		superParametroRelVO.adicionarParametro("inscMunicipal", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getInscMunicipal());
		superParametroRelVO.adicionarParametro("caixaPostal", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCaixaPostal());
		superParametroRelVO.adicionarParametro("cnpj", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCNPJ());
		superParametroRelVO.adicionarParametro("credenciamento", gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCredenciamento());
		superParametroRelVO.adicionarParametro("apresentarListaAtividadeComplementar", Boolean.TRUE);
		superParametroRelVO.adicionarParametro("apresentarCreditoFinanceiro", Boolean.FALSE);
		superParametroRelVO.adicionarParametro("apresentarTotalizadorCurriculoEscolar", Boolean.TRUE);
		superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
		superParametroRelVO.setSubReport_Dir(DisciplinasGradeRel.getCaminhoBaseRelatorio());
		superParametroRelVO.setNomeUsuario(usuario.getNome());
		superParametroRelVO.setTituloRelatorio(titulo);
		superParametroRelVO.setListaObjetos(listaRegistro);
		superParametroRelVO.setUnidadeEnsino(gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getNome());
		superParametroRelVO.setNomeEmpresa(gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getNome());
		superParametroRelVO.setCaminhoBaseRelatorio(DisciplinasGradeRel.getCaminhoBaseRelatorio());
		superParametroRelVO.setVersaoSoftware(superControleRelatorio.getVersaoSistema());
		superParametroRelVO.setQuantidade(listaRegistro.size());
		if (Uteis.isAtributoPreenchido(gestaoXmlGradeCurricular.getUnidadeEnsinoVO())) {
			File imagem = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio() + File.separator + gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio());
			if(imagem.exists()){
				superParametroRelVO.adicionarParametro("logoPadraoRelatorio", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio() + File.separator + gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio());
			}else {
				superParametroRelVO.adicionarParametro("logoPadraoRelatorio", superParametroRelVO.getLogoPadraoRelatorio());
			}
			superParametroRelVO.adicionarLogoUnidadeEnsinoSelecionada(gestaoXmlGradeCurricular.getUnidadeEnsinoVO());
		}
		superControleRelatorio.realizarImpressaoRelatorio();
		File representacaoVisual = new File(getCaminhoPastaWeb() + Constantes.relatorio + Constantes.BARRA + superControleRelatorio.getCaminhoRelatorio());
		File xmlCurriculoEscolar = getFacadeFactory().getExpedicaoDiplomaDigital_1_05_interfaceFacade().criarXMLCurriculoEscolarDigital(gestaoXmlGradeCurricular);
		getFacadeFactory().getDocumentoAssinadoPessoaFacade().realizarRejeicaoDocumentosAssinados(gestaoXmlGradeCurricular, usuario);
		getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaCurriculoEscolar(gestaoXmlGradeCurricular, xmlCurriculoEscolar, representacaoVisual, configuracaoGeralSistemaVO, TipoOrigemDocumentoAssinadoEnum.CURRICULO_ESCOLAR_DIGITAL, usuario, Boolean.TRUE);
		if (gestaoXmlGradeCurricular.getDocumentoAssinadoVO().getArquivoVisual().getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.AMAZON_S3)) {
			getFacadeFactory().getArquivoHelper().realizarDownloadArquivoAmazon(gestaoXmlGradeCurricular.getDocumentoAssinadoVO().getArquivoVisual(), configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + "/" + gestaoXmlGradeCurricular.getDocumentoAssinadoVO().getArquivoVisual().getPastaBaseArquivo(), configuracaoGeralSistemaVO);
			getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoProvedorDeAssinatura(gestaoXmlGradeCurricular.getDocumentoAssinadoVO(), gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCodigo(), true, true, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + "/" + gestaoXmlGradeCurricular.getDocumentoAssinadoVO().getArquivoVisual().getPastaBaseArquivo() + "/" + gestaoXmlGradeCurricular.getDocumentoAssinadoVO().getArquivoVisual().getNome(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40.0f, 200.0f, 8f, 380, 10, 0, 0, configuracaoGeralSistemaVO, Boolean.TRUE, usuario, Boolean.TRUE);
		} else {
			getFacadeFactory().getDocumentoAssinadoFacade().realizarVerificacaoProvedorDeAssinatura(gestaoXmlGradeCurricular.getDocumentoAssinadoVO(), gestaoXmlGradeCurricular.getUnidadeEnsinoVO().getCodigo(), true, true, representacaoVisual.getName(), AlinhamentoAssinaturaDigitalEnum.RODAPE_ESQUERDA, "#000000", 40.0f, 200.0f, 8f, 380, 10, 0, 0, configuracaoGeralSistemaVO, Boolean.TRUE, usuario, Boolean.TRUE);
		}
	}

}
