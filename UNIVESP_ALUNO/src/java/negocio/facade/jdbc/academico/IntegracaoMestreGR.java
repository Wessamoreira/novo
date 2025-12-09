package negocio.facade.jdbc.academico;

import static negocio.comuns.utilitarias.Uteis.getDateTime;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment; // <-- O principal
import org.apache.poi.ss.usermodel.VerticalAlignment;   // <-- O principal
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import controle.arquitetura.DataModelo;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.IntegracaoMestreGRVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.OperacaoTempoRealMestreGRVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.TransferenciaSaidaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OperacaoTempoRealMestreGREnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.IntegracaoMestreGRInterfaceFacade;

import webservice.servicos.excepetion.ErrorInfoRSVO;

/**
 * @author Rodrigo Ribeiro
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>IntegracaoMestreGRVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>IntegracaoMestreGRVO</code>. Encapsula toda a interação com o banco de
 * dados.
 */
@Repository
@Scope("singleton")
@Lazy
public class IntegracaoMestreGR extends ControleAcesso implements IntegracaoMestreGRInterfaceFacade {

    private static final long serialVersionUID = 1L;
    private static final double permitsPerSecond = 1_000.0 / Uteis.delayMillisIntegracaoMestreGR;
    private static final int limitOperacaoTempoReal = 100;

    public static List<Integer> parseBimestres(String bimestresStr) {
        List<Integer> bimestres = new ArrayList<>();
        for (String bimestre : bimestresStr.split(",")) {
            String clean = bimestre.trim().replaceAll("^'+|'+$", "");
            try {
                bimestres.add(Integer.parseInt(clean));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Valor inválido no bimestres: " + bimestre, e);
            }
        }
        return bimestres;
    }

    /**
     * Métodos Auxiliares
     */
    public IntegracaoMestreGRVO montarDadosDeIntegracao(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        IntegracaoMestreGRVO integracaoMestreGR = new IntegracaoMestreGRVO();
        integracaoMestreGR.setCodigo(dadosSQL.getInt("codigo"));
        integracaoMestreGR.setOrigem(OperacaoTempoRealMestreGREnum.valueOf(dadosSQL.getString("origem")));
        integracaoMestreGR.setNomeLote(dadosSQL.getString("nomelote"));
        integracaoMestreGR.setAno(dadosSQL.getString("ano"));
        integracaoMestreGR.setSemestre(dadosSQL.getString("semestre"));
        integracaoMestreGR.setBimestre(dadosSQL.getInt("bimestre"));
        integracaoMestreGR.setUnidadeEnsinos(dadosSQL.getString("unidadeEnsinos"));
        integracaoMestreGR.setNivelEducacional(TipoNivelEducacional.valueOf(dadosSQL.getString("nivelEducacional")));
        integracaoMestreGR.setCursos(dadosSQL.getString("cursos"));
        integracaoMestreGR.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
        if(Uteis.isAtributoPreenchido(integracaoMestreGR.getDisciplinaVO().getCodigo())) {
        	integracaoMestreGR.setDisciplinaVO(getAplicacaoControle().getDisciplinaVO(integracaoMestreGR.getDisciplinaVO().getCodigo(), usuario));	
        }
        integracaoMestreGR.setPeriodoRequerimentoInicio(dadosSQL.getDate("periodoRequerimentoInicio"));        
        integracaoMestreGR.setPeriodoRequerimentoTermino(dadosSQL.getDate("periodoRequerimentoTermino"));        
        integracaoMestreGR.setCodigoLote(dadosSQL.getInt("codigolote"));        
        integracaoMestreGR.setSituacao(dadosSQL.getString("situacao"));
        integracaoMestreGR.setQtdeRegistros(dadosSQL.getInt("qtderegistros"));
        integracaoMestreGR.setCreated(dadosSQL.getDate("created"));
        integracaoMestreGR.setDataTransmissao(dadosSQL.getDate("datatransmissao"));
        integracaoMestreGR.setNomeCreated(dadosSQL.getString("nomecreated"));
        integracaoMestreGR.setCodigoCreated(dadosSQL.getInt("codigocreated"));
        integracaoMestreGR.setMensagemErro(dadosSQL.getString("mensagemerro"));
        integracaoMestreGR.setDadosEnvio(dadosSQL.getString(dadosSQL.getByte("dadosenvio")));
        integracaoMestreGR.setDadosRetorno(dadosSQL.getString(dadosSQL.getByte("dadosretorno")));
        integracaoMestreGR.setNovoObj(Boolean.FALSE);
        return integracaoMestreGR;
    }

    public static IntegracaoMestreGRVO montarDadosDeLogMatriculaDisciplina(SqlRowSet dadosSQL) throws Exception {
        IntegracaoMestreGRVO integracaoMestreGR = new IntegracaoMestreGRVO();
        integracaoMestreGR.setCodigoDisciplina(dadosSQL.getInt("codigodisciplina"));
        return integracaoMestreGR;
    }

    public static IntegracaoMestreGRVO montarDadosDisciplina(SqlRowSet dadosSQL) {
        IntegracaoMestreGRVO integracaoMestreGR = new IntegracaoMestreGRVO();
        integracaoMestreGR.setCodigoDisciplina(dadosSQL.getInt("codigo"));
        integracaoMestreGR.setCodigoInternoAluno(dadosSQL.getInt("codigointernoaluno"));
        integracaoMestreGR.setCodigoAluno(dadosSQL.getString("codigoaluno"));
        integracaoMestreGR.setSituacao(dadosSQL.getString("situacao"));
        integracaoMestreGR.setNomeAluno(dadosSQL.getString("nome"));
        integracaoMestreGR.setEmailAluno(dadosSQL.getString("email"));
        integracaoMestreGR.setCodigoInternoPolo(dadosSQL.getInt("unidadeensino"));
        integracaoMestreGR.setDescricaoPolo(dadosSQL.getString("descricaopolo"));
        integracaoMestreGR.setCodigoInternoCurso(dadosSQL.getInt("codigointernocurso"));
        return integracaoMestreGR;
    }

    public static IntegracaoMestreGRVO montarDadosMatriculaPeriodoDisciplinas(SqlRowSet dadosSQL) {
        IntegracaoMestreGRVO integracaoMestreGR = new IntegracaoMestreGRVO();
        integracaoMestreGR.setMatricula(dadosSQL.getString("matricula"));
        integracaoMestreGR.setCodigoDisciplina(dadosSQL.getInt("codigodisciplina"));
        return integracaoMestreGR;
    }

    public static IntegracaoMestreGRVO montarDadosAluno(SqlRowSet dadosSQL) {
        IntegracaoMestreGRVO integracaoMestreGR = new IntegracaoMestreGRVO();
        integracaoMestreGR.setAno(dadosSQL.getString("ano"));
        integracaoMestreGR.setMatricula(dadosSQL.getString("matricula"));
        integracaoMestreGR.setSituacao(dadosSQL.getString("situacao"));
        integracaoMestreGR.setCodigoInternoPolo(dadosSQL.getInt("unidadeensino"));
        integracaoMestreGR.setSemestre(dadosSQL.getString("semestre"));
        integracaoMestreGR.setCodigoInternoCurso(dadosSQL.getInt("codigointernocurso"));
        integracaoMestreGR.setCodigoInternoAluno(dadosSQL.getInt("codigointernoaluno"));
        integracaoMestreGR.setCodigoAluno(dadosSQL.getString("codigoaluno"));
        integracaoMestreGR.setNomeAluno(dadosSQL.getString("nome"));
        integracaoMestreGR.setEmailAluno(dadosSQL.getString("email"));
        integracaoMestreGR.setDescricaoPolo(dadosSQL.getString("descricaopolo"));
        integracaoMestreGR.setTempoEstendidoAluno(dadosSQL.getString("tempoestendido"));
        integracaoMestreGR.setCodigoDiaSemanaAluno(dadosSQL.getInt("diaSemanaAluno"));
        integracaoMestreGR.setNumeroCelularAluno(dadosSQL.getString("numerocelular"));
        return integracaoMestreGR;
    }

    public List<IntegracaoMestreGRVO> montarDadosConsultaParaJson(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosDeIntegracao(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    public static List<IntegracaoMestreGRVO> montarDadosConsultaLogMatriculaDisciplina(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosDeLogMatriculaDisciplina(tabelaResultado));
        }
        return vetResultado;
    }

    /**
     * REGISTROS DE INTEGRACOES
     */

    public static List<IntegracaoMestreGRVO> montarDadosConsultaDisciplinas(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosDisciplina(tabelaResultado));
        }
        return vetResultado;
    }

    public static List<IntegracaoMestreGRVO> montarDadosConsultaMatriculaPeriodoDisciplinas(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosMatriculaPeriodoDisciplinas(tabelaResultado));
        }
        return vetResultado;
    }

    public static List<IntegracaoMestreGRVO> montarDadosConsultaAluno(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosAluno(tabelaResultado));
        }
        return vetResultado;
    }

    public static String montarStringParaIn(String numerosSeparadosPorVirgula) {
        if (numerosSeparadosPorVirgula == null || numerosSeparadosPorVirgula.trim().isEmpty()) {
            return "";
        }

        String[] numeros = numerosSeparadosPorVirgula.split(",");
        StringBuilder inClause = new StringBuilder();

        for (int i = 0; i < numeros.length; i++) {
            String numero = numeros[i].trim();
            if (!numero.isEmpty()) {
                if (i > 0) {
                    inClause.append(", ");
                }
                inClause.append("'").append(numero).append("'");
            }
        }
        return inClause.toString();
    }

    public static String getAnoFormatado(Date dataConverter) {
        Timestamp timestamp = Uteis.getDataJDBCTimestamp(dataConverter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(timestamp);
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirOperacao(final IntegracaoMestreGRVO integracaoMestreGRVO, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sqlStr = new StringBuilder("INSERT INTO integracaomestregroperacoes ")
                    .append("(origem, matricula, ano, disciplina, semestre, bimestre, dadosenvio, dadosretorno, mensagemerro, codigoCreated, nomeCreated) ")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ")
                    .append("returning codigo");

            integracaoMestreGRVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement sqlInserir = connection.prepareStatement(sqlStr.toString());
                    sqlInserir.setString(1, integracaoMestreGRVO.getOrigem().getValor());
                    sqlInserir.setString(2, integracaoMestreGRVO.getMatricula());
                    sqlInserir.setString(3, integracaoMestreGRVO.getAno().toString());
                    if (integracaoMestreGRVO.getCodigoDisciplina() != null && integracaoMestreGRVO.getCodigoDisciplina() != 0) {
                        sqlInserir.setInt(4, integracaoMestreGRVO.getCodigoDisciplina());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setString(5, integracaoMestreGRVO.getSemestre());
                    sqlInserir.setInt(6, integracaoMestreGRVO.getBimestre());
                    sqlInserir.setString(7, integracaoMestreGRVO.getDadosEnvio());
                    sqlInserir.setString(8, integracaoMestreGRVO.getDadosRetorno());
                    if (integracaoMestreGRVO.getMensagemErro() != null && !integracaoMestreGRVO.getMensagemErro().isEmpty()) {
                        sqlInserir.setString(9, integracaoMestreGRVO.getMensagemErro());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    if(Uteis.isAtributoPreenchido(usuario)) {
                    	sqlInserir.setInt(10, usuario.getCodigo());
                    	sqlInserir.setString(11, usuario.getNome());
                    }else {
                    	sqlInserir.setNull(10, 0);
                    	sqlInserir.setNull(11, 0);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            integracaoMestreGRVO.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            integracaoMestreGRVO.setNovoObj(Boolean.TRUE);
            integracaoMestreGRVO.setCodigo(0);
            throw e;
        }
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarLogIntegracaoAluno(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("WITH disciplinas_integradas AS ( ");
        sqlStr.append("    SELECT ");
        sqlStr.append("        integracao.codigo AS codigo_integracao, ");
        sqlStr.append("        turmas.codigo_interno as chaveTurma ");
        sqlStr.append("    FROM integracaomestregroperacoes integracao ");
        sqlStr.append("    LEFT JOIN LATERAL ( ");
        sqlStr.append("        SELECT (t ->> 'codigo_interno') AS codigo_interno ");
        sqlStr.append("        FROM jsonb_array_elements( ");
        sqlStr.append("            CASE ");
        sqlStr.append("                WHEN jsonb_typeof(integracao.dadosenvio::jsonb -> 'turmas') = 'array' ");
        sqlStr.append("                THEN integracao.dadosenvio::jsonb -> 'turmas' ");
        sqlStr.append("                ELSE '[]'::jsonb ");
        sqlStr.append("            END ");
        sqlStr.append("        ) AS t ");
        sqlStr.append("        WHERE t ? 'codigo_interno' ");
        sqlStr.append("    ) AS turmas ON TRUE ");        
        sqlStr.append(") ");
        sqlStr.append("SELECT sub.*, ");
        sqlStr.append("       COUNT(*) OVER () AS qtde_total_registros ");
        sqlStr.append("FROM ( ");
        sqlStr.append("    SELECT ");
        sqlStr.append("        integracao.codigo, ");
        sqlStr.append("        integracao.created, ");
        sqlStr.append("        integracao.nomeCreated, ");
        sqlStr.append("        integracao.processado, ");
        sqlStr.append("        integracao.origem, ");        
        sqlStr.append("        integracao.matricula, ");
        sqlStr.append("        integracao.dadosenvio, ");
        sqlStr.append("        integracao.dadosretorno, ");
        sqlStr.append("        integracao.mensagemerro, ");
        sqlStr.append("        integracao.ano, ");
        sqlStr.append("        case when integracao.processado and coalesce(integracao.mensagemerro, '') != '' then 'Erro' else case when integracao.processado then 'Sucesso' else 'Aguardando' end end  as status, ");
        sqlStr.append("        integracao.semestre, ");
        sqlStr.append("        integracao.bimestre, ");
        sqlStr.append("        dadosenvio::jsonb ->> 'codigo' as codigoAluno, ");
        sqlStr.append("        dadosenvio::jsonb ->> 'email' as email, ");
        sqlStr.append("        (dadosenvio::jsonb ->> 'semana_dia_sigla')::int as diaSemana, ");
        sqlStr.append("        dadosenvio::jsonb ->> 'is_tempo_estendido' as tempoEstendido, ");
        sqlStr.append("        dadosenvio::jsonb ->> 'numero_celular' as celular, ");
        sqlStr.append("        dadosenvio::jsonb ->> 'nome' as nome, ");
        sqlStr.append("        COALESCE( ");
        sqlStr.append("            NULLIF(( ");
        sqlStr.append("                SELECT STRING_AGG(DISTINCT disciplina.chaveTurma, ', ' ORDER BY disciplina.chaveTurma) ");
        sqlStr.append("                FROM disciplinas_integradas disciplina ");
        sqlStr.append("                WHERE disciplina.codigo_integracao = integracao.codigo ");
        sqlStr.append("            ), ''), ");
        sqlStr.append("            '' ");
        sqlStr.append("        ) AS chaveTurma, ");
        sqlStr.append("        dadosenvio::jsonb -> 'polo' ->> 'codigo_interno' AS unidadeensino, ");
        sqlStr.append("        dadosenvio::jsonb -> 'curso' ->> 'codigo_interno' AS curso, ");
        sqlStr.append("        curso.nivelEducacional AS nivelEducacional ");
        sqlStr.append("    FROM integracaomestregroperacoes integracao ");
        sqlStr.append("    left JOIN matricula ON matricula.matricula = integracao.matricula ");
        sqlStr.append("    left JOIN curso ON matricula.curso = curso.codigo ");
        sqlStr.append(whereConsultarLogIntegracaoAluno(filtros));
        sqlStr.append(") sub ");
        sqlStr.append(whereConsultarLogIntegracaoAlunoCTE(filtros));
        sqlStr.append("ORDER BY sub.codigo DESC ");

        if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
            sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina());
            sqlStr.append(" offset ").append(dataModelo.getOffset());
        }
//        System.out.println("Consulta SQL gerada: " + sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (dataModelo != null && tabelaResultado.next()) {
            dataModelo.setTotalRegistrosEncontrados(0);
            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        }

        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoLogAluno(tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        return listaConsulta;
    }

    /**
     * Retornar somente os códigos de disciplina cuja última operação
     * (ordenada pelo campo [codigo] de forma decrescente)
     * seja uma operacao de inclusao ou exclusao. Assim, se após uma inclusão houver uma
     * exclusão para a mesma disciplina, ela não aparecerá no resultado
     * Logo vai retornar somente as disciplinas que foram inseridas e nao excluidas
     *
     * @param filtros
     * @param controlarAcesso
     * @param usuarioVO
     * @return
     * @throws Exception
     */
    @Override
    public List<IntegracaoMestreGRVO> consultarPorAlunoDisciplinaIntegradas(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = "WITH operacoes AS ( " +
                "  SELECT " +
                "    disciplina, " +
                "    origem, " +
                "    codigo " +
                "  FROM integracaomestregroperacoes " +
                whereConsultarPorAlunoDisciplinaIntegradas(filtros) +
                "  UNION ALL " +
                "  SELECT " +
                "    (turma_elemento->>'codigo_interno')::integer AS disciplina, " +
                "    CASE " +
                "      WHEN turma_elemento->>'acao' = 'INSERT' THEN 'INCLUSAO_DISCIPLINA' " +
                "      WHEN turma_elemento->>'acao' = 'DELETE' THEN 'EXCLUSAO_DISCIPLINA' " +
                "      ELSE NULL " +
                "    END AS origem, " +
                "    im.codigo " +
                "  FROM integracaomestregroperacoes im " +
                "  CROSS JOIN LATERAL jsonb_array_elements(im.dadosenvio::jsonb->'turmas') AS turma_elemento " +
                whereConsultarPorAlunoDisciplinaIntegradas(filtros) +
                "), " +
                "ultimas_inclusoes AS ( " +
                "  SELECT " +
                "    disciplina, " +
                "    MAX(codigo) AS codigo_inclusao " +
                "  FROM operacoes " +
                "  WHERE origem IN ( " + filtros.get("origemSelecionada") + " ) " +
                "  GROUP BY disciplina " +
                "), " +
                "ultimas_exclusoes AS ( " +
                "  SELECT " +
                "    disciplina, " +
                "    MAX(codigo) AS codigo_exclusao " +
                "  FROM operacoes " +
                "  WHERE origem IN ( " + filtros.get("origemSubtrair") + " ) " +
                "  GROUP BY disciplina " +
                ") " +
                "SELECT " +
                "  ui.disciplina AS codigodisciplina " +
                "FROM ultimas_inclusoes ui " +
                "LEFT JOIN ultimas_exclusoes ue ON ui.disciplina = ue.disciplina " +
                "WHERE " +
                "  ue.codigo_exclusao IS NULL " +
                "  OR ui.codigo_inclusao > ue.codigo_exclusao " +
                "ORDER BY codigodisciplina; ";
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsultaLogMatriculaDisciplina(tabelaResultado);
    }

    @Override
    public Boolean consultarIsAlunoIntegrado(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = "SELECT " +
                "case " +
                "when exists ( " +
                "select 1 " +
                "from integracaomestregroperacoes " +
                whereConsultarIsAlunoIntegrado(filtros) +
                " ) then true " +
                "else false " +
                "end as isintegrado " +
                "limit 1 ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return tabelaResultado.getBoolean("isintegrado");
        } else {
            return false;
        }
    }

    private StringBuilder whereConsultarIsAlunoIntegrado(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder("WHERE 1 = 1 ");
        if (validarFiltros(filtros, "matricula")) {
            where.append(" AND matricula = '" + filtros.get("matricula") + "' ");
        }
        return where;
    }

    /**
     * Dedicado a consultar por restantes dos dados do aluno
     *
     * @param filtros
     * @param controlarAcesso
     * @param usuarioVO
     * @return
     * @throws Exception
     */
    @Override
    public List<IntegracaoMestreGRVO> consultarPorMatriculaAluno(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = String.valueOf(selectConsultarPorMatriculaAluno()) +
                whereConsultarPorMatriculaAnoSemestreAtual(filtros) +
                "order by " +
                "    (matriculaperiodo.ano || '/' || matriculaperiodo.semestre) desc, matriculaperiodo.situacaomatriculaperiodo, " +
                "    matriculaperiodo.codigo desc limit 1";
//        System.out.printf(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsultaAluno(tabelaResultado);
    }

    /**
     * Consulta dedicada a retornar os dados do aluno com base na matrícula periodo Ativa
     *
     * @param filtros
     * @param controlarAcesso
     * @param usuarioVO
     * @return
     * @throws Exception
     */
    @Override
    public List<IntegracaoMestreGRVO> consultarPorMatriculaAlunoAtivo(Map<String, Object> filtros, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
        String sqlStr = String.valueOf(selectConsultarPorMatriculaAluno()) +
                whereConsultarPorMatriculaAnoSemestreAtual(filtros) +
                " and matricula.situacao in ('AT', 'FI') " +
                "order by " +
                "    (matriculaperiodo.ano || '/' || matriculaperiodo.semestre) desc,  matriculaperiodo.situacaomatriculaperiodo, " +
                "    matriculaperiodo.codigo desc limit 1";
//        System.out.printf(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsultaAluno(tabelaResultado);
    }

    private StringBuilder selectConsultarPorMatriculaAluno() {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select ");
        sqlStr.append("    matriculaperiodo.ano, ");
        sqlStr.append("    matricula.matricula, ");
        sqlStr.append("    matricula.situacao, ");
        sqlStr.append("    matricula.unidadeensino, ");
        sqlStr.append("    matriculaperiodo.semestre, ");
        sqlStr.append("regexp_replace(pessoa.celular, '[^0-9]', '', 'g') AS numerocelular, ");     
        sqlStr.append("(case when pessoa.sabatista then 8 else case matricula.diasemanaaula when 'SEGUNGA' then 1 when 'TERCA' then 2 when 'QUARTA' then 3 when 'QUINTA' then 4 when 'SEXTA' then 5 when 'SABADO' then 6 else 7 end end) as diaSemanaAluno, ");
        sqlStr.append("case when pessoa.tempoestendidoprova then 'S' else 'N' end as tempoestendido, ");
        sqlStr.append("    curso.codigo as codigointernocurso, ");
        sqlStr.append("    pessoa.codigo as codigointernoaluno,");
        sqlStr.append("    case when (pessoa.registroAcademico is null or pessoa.registroAcademico = '') then matricula.matricula else pessoa.registroAcademico end as codigoaluno,");
        sqlStr.append("    pessoa.nome, ");
        sqlStr.append("    pessoaemailinstitucional.email as email, ");
        sqlStr.append("    replace(replace(replace(replace(replace(replace(unidadeEnsino.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') as descricaopolo ");
        sqlStr.append("from ");
        sqlStr.append("    matricula ");
        sqlStr.append("inner join matriculaperiodo on ");
        sqlStr.append("    matriculaperiodo.matricula = matricula.matricula ");
        sqlStr.append("inner join pessoa on  ");
        sqlStr.append("    pessoa.codigo =  matricula.aluno ");
        sqlStr.append("inner join unidadeEnsino on ");
        sqlStr.append("    unidadeEnsino.codigo = matricula.unidadeEnsino ");
        sqlStr.append("inner join curso on  curso.codigo = matricula.curso ");
        sqlStr.append("inner join configuracaoldap on   curso.configuracaoldap = configuracaoldap.codigo ");
        sqlStr.append("inner join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo and pessoaemailinstitucional.email ilike '%'||configuracaoldap.dominio||'%'  and pessoaemailinstitucional.statusativoinativoenum = 'ATIVO' ");
        sqlStr.append("and pessoaemailinstitucional.codigo = ( ");
        sqlStr.append("select pei.codigo from pessoaemailinstitucional pei where pei.pessoa = pessoa.codigo and pei.statusativoinativoenum = 'ATIVO'  and pei.email ilike '%'||configuracaoldap.dominio||'%' order by pei.codigo desc limit 1 ) ");
        return sqlStr;
    }

    /**
     * Incluir registros de integracao
     *
     * @param integracaoMestreGRVO
     * @param usuario
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final IntegracaoMestreGRVO integracaoMestreGRVO, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO integracaomestregr ")
                    .append("(origem, nomelote, codigolote, situacao, qtderegistros, created, datatransmissao, nomecreated, codigocreated, ano, semestre, bimestre, unidadeEnsinos, cursos, disciplina, periodoRequerimentoInicio, periodoRequerimentoTermino, nivelEducacional) ")
                    .append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ")
                    .append("returning codigo");
            integracaoMestreGRVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement sqlInserir = connection.prepareStatement(sql.toString());
                    sqlInserir.setString(1, integracaoMestreGRVO.getOrigem().getValor());
                    sqlInserir.setString(2, integracaoMestreGRVO.getNomeLote().trim());
                    sqlInserir.setInt(3, integracaoMestreGRVO.getCodigoLote());
                    sqlInserir.setString(4, integracaoMestreGRVO.getSituacao().trim());
                    sqlInserir.setInt(5, integracaoMestreGRVO.getQtdeRegistros());
                    sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(integracaoMestreGRVO.getCreated()));
                    if (Uteis.isAtributoPreenchido(integracaoMestreGRVO.getCreated())) {
                        sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(integracaoMestreGRVO.getCreated()));
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (Uteis.isAtributoPreenchido(integracaoMestreGRVO.getDataTransmissao())) {
                        sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(integracaoMestreGRVO.getDataTransmissao()));
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setString(8, usuario.getNome());
                    sqlInserir.setInt(9, usuario.getCodigo());
                    sqlInserir.setString(10, integracaoMestreGRVO.getAno());
                    sqlInserir.setString(11, integracaoMestreGRVO.getSemestre());
                    sqlInserir.setInt(12, integracaoMestreGRVO.getBimestre());
                    sqlInserir.setString(13, integracaoMestreGRVO.getUnidadeEnsinos());
                    sqlInserir.setString(14, integracaoMestreGRVO.getCursos());
                    if(Uteis.isAtributoPreenchido(integracaoMestreGRVO.getDisciplinaVO().getCodigo())) {
                    	sqlInserir.setInt(15, integracaoMestreGRVO.getDisciplinaVO().getCodigo());
                    }else {
                    	sqlInserir.setNull(15, 0);
                    }
                    if(Uteis.isAtributoPreenchido(integracaoMestreGRVO.getPeriodoRequerimentoInicio())) {
                    	sqlInserir.setDate(16, Uteis.getDataJDBC(integracaoMestreGRVO.getPeriodoRequerimentoInicio()));
                    }else {
                    	sqlInserir.setNull(16, 0);
                    }
                    if(Uteis.isAtributoPreenchido(integracaoMestreGRVO.getPeriodoRequerimentoTermino())) {
                    	sqlInserir.setDate(17, Uteis.getDataJDBC(integracaoMestreGRVO.getPeriodoRequerimentoTermino()));
                    }else {
                    	sqlInserir.setNull(17, 0);
                    }
                    sqlInserir.setString(18, integracaoMestreGRVO.getNivelEducacional().name());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));
            integracaoMestreGRVO.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            integracaoMestreGRVO.setNovoObj(Boolean.TRUE);
            integracaoMestreGRVO.setCodigo(0);
            throw e;
        }
    }

    /**
     * Incluir os itens json no registro da integracao
     *
     * @param integracaoMestreGRVO
     * @param usuario
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItemIntegracao(final IntegracaoMestreGRVO integracaoMestreGRVO, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO integracaomestregritem ")
                    .append("(codigointegracao, dadosenvio, dadosretorno, mensagemerro) ")
                    .append("VALUES (?, ?, ?, ?) ")
                    .append("returning codigo");

            integracaoMestreGRVO.setIdItem((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement sqlInserir = connection.prepareStatement(sql.toString());
                    sqlInserir.setInt(1, integracaoMestreGRVO.getCodigo());
                    sqlInserir.setString(2, integracaoMestreGRVO.getDadosEnvio());
                    sqlInserir.setString(3, integracaoMestreGRVO.getDadosRetorno());

                    if (integracaoMestreGRVO.getMensagemErro() != null) {
                        sqlInserir.setString(4, integracaoMestreGRVO.getMensagemErro());
                    } else {
                        sqlInserir.setNull(4, Types.VARCHAR);
                    }
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet rs) throws SQLException {
                    if (rs.next()) {
                        return rs.getInt("codigo");
                    }
                    return null;
                }
            }));

            integracaoMestreGRVO.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            integracaoMestreGRVO.setNovoObj(Boolean.TRUE);
            integracaoMestreGRVO.setIdItem(0);
            throw new Exception("Erro ao inserir itens na integração: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza o registro de integracao de cargas para a API do Mestre GR
     *
     * @param integracaoMestreGRVO
     * @param usuario
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizar(final IntegracaoMestreGRVO integracaoMestreGRVO, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE integracaomestregr SET ")
                    .append("situacao = ?, ")
                    .append("datatransmissao = ? ")
                    .append("WHERE codigo = ?");

//            System.out.println(sql.toString());

            int rowsUpdated = getConexao().getJdbcTemplate().update(connection -> {
                PreparedStatement sqlAtualizar = connection.prepareStatement(sql.toString());
                sqlAtualizar.setString(1, integracaoMestreGRVO.getSituacao().trim());
                if (Uteis.isAtributoPreenchido(integracaoMestreGRVO.getDataTransmissao())) {
                    sqlAtualizar.setTimestamp(2, Uteis.getDataJDBCTimestamp(integracaoMestreGRVO.getDataTransmissao()));
                } else {
                    sqlAtualizar.setNull(2, 0);
                }
                sqlAtualizar.setInt(3, integracaoMestreGRVO.getCodigo());
                return sqlAtualizar;
            });

            if (rowsUpdated == 0) {
                throw new Exception("Nenhum registro foi atualizado. Verifique se o código fornecido é válido.");
            }

            integracaoMestreGRVO.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            integracaoMestreGRVO.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * DISCIPLINAS
     */

    /**
     * Atualizar o item do registro de integracao que e existente
     *
     * @param integracaoMestreGRVO
     * @param usuario
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarItemIntegracao(final IntegracaoMestreGRVO integracaoMestreGRVO, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE integracaomestregritem SET ")
                    .append("dadosretorno = ?, ")
                    .append("mensagemerro = ? ")
                    .append("WHERE codigo = ?");

//            System.out.println(sql.toString());

            int rowsUpdated = getConexao().getJdbcTemplate().update(connection -> {
                PreparedStatement sqlAtualizar = connection.prepareStatement(sql.toString());
                sqlAtualizar.setString(1, integracaoMestreGRVO.getDadosRetorno().trim());
                sqlAtualizar.setString(2, integracaoMestreGRVO.getMensagemErro());
                sqlAtualizar.setInt(3, integracaoMestreGRVO.getIdItem());
                return sqlAtualizar;
            });

            if (rowsUpdated == 0) {
                throw new Exception("Nenhum registro foi atualizado. Verifique se o código fornecido é válido.");
            }

            integracaoMestreGRVO.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            integracaoMestreGRVO.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Excluir o registro, que passar como parametro o codigo e tenha a situacao de aguardando
     *
     * @param filtro
     * @param usuarioVO
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(Integer codigo, UsuarioVO usuarioVO) throws Exception {
        String sqlStr = "DELETE FROM integracaomestregr " +
                "where codigo = ? ";                
                adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
        getConexao().getJdbcTemplate().update(sqlStr, codigo);
    }

    public void validarUnicidade(IntegracaoMestreGRVO integracaoMestreGRVO, UsuarioVO usuario) throws Exception {
        List<IntegracaoMestreGRVO> lista = consultarPorCodigoIntegracaoMestreGR(integracaoMestreGRVO.getCodigo(), false, usuario);
        for (IntegracaoMestreGRVO repetido : lista) {
            if (repetido.getCodigo().intValue() == integracaoMestreGRVO.getCodigo().intValue()) {
                throw new ConsistirException("Já existe uma categorização definida para essa turma!");
            }
        }
    }

    /**
     * Consultar pelo codigo a integracao correspondente
     *
     * @param codigoIntegracaoMestreGR
     * @param controlarAcesso
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<IntegracaoMestreGRVO> consultarPorCodigoIntegracaoMestreGR(Integer codigoIntegracaoMestreGR, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM integracaomestregr where codigo=" + codigoIntegracaoMestreGR;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsultaParaJson(tabelaResultado, usuario));
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarItensIntegracaoPorCodigo(Integer codigoIntegracao, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT ")
                .append("dadosenvio, ")                               
                .append("codigo, ")
                .append("codigointegracao, ")
                .append("count(*) over() as qtde_total_registros ")
                .append("FROM integracaomestregritem ");
        sqlStr.append(" where codigointegracao = ")
        .append(codigoIntegracao);        
        sqlStr.append(" order by codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (dataModelo != null && tabelaResultado.next()) {
            dataModelo.setTotalRegistrosEncontrados(0);
            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        }
        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoItemIntegracaoLotePorCodigo(tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        return listaConsulta;
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarRegistroIntegracaoLote(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        StringBuilder sqlStr = new StringBuilder(" ");
        sqlStr.append("SELECT ")
                .append("codigo, ")
                .append("origem, ")
                .append("ano, ")
                .append("semestre, ")
                .append("bimestre, ")
                .append("nivelEducacional, ")
                .append("cursos, ")
                .append("unidadeEnsinos, ")
                .append("periodoRequerimentoInicio, ")
                .append("periodoRequerimentoTermino, ")
                .append("disciplina, ")
                .append("nomelote, ")
                .append("codigolote, ")
                .append("situacao, ")
                .append("qtderegistros, ")
                .append("created, ")
                .append("datatransmissao, ")
                .append("nomecreated, ")
                .append("codigocreated, ")
                .append("count(*) over() as qtde_total_registros ");
        sqlStr.append("FROM ")
                .append("integracaomestregr ");
        sqlStr.append(whereRegistroIntegracaoLote(filtros));
        sqlStr.append(" ORDER BY ")
                .append("codigo DESC ");

        if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
            sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina());
            sqlStr.append(" offset ").append(dataModelo.getOffset());
        }
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
        if (dataModelo != null && tabelaResultado.next()) {
            dataModelo.setTotalRegistrosEncontrados(0);
            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        }

        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoRegistroIntegracaoLote(tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        return listaConsulta;
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarErroIntegracaoJson(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("WITH cte_integracoes_lote AS ( ");
        sqlStr.append("  SELECT ");
        sqlStr.append("    iitem.codigointegracao::bigint      AS codigointegracao, ");
        sqlStr.append("    iitem.codigo::bigint                AS codigo, ");
        sqlStr.append("    igr.codigolote::bigint              AS codigolote, ");
        sqlStr.append("    igr.nomeLote, ");
        sqlStr.append("    igr.origem::text                    AS origem, ");
        sqlStr.append("    igr.created as created, ");
        sqlStr.append("    igr.nomecreated as nomecreated, ");
        sqlStr.append(" ''::varchar as dadosEnvio, ");
        sqlStr.append(" ''::varchar as dadosRetorno, ");
        sqlStr.append("    UPPER(coalesce((CASE WHEN iitem.mensagemerro IS NOT NULL and iitem.mensagemerro ilike '%status%' ");
        sqlStr.append("                     AND trim(iitem.mensagemerro)::jsonb is not null ");
        sqlStr.append("                  THEN (iitem.mensagemerro::jsonb)->> 'status' END), 'ERROR'))::text AS status,");
        sqlStr.append("    coalesce((CASE WHEN iitem.mensagemerro IS NOT NULL and iitem.mensagemerro ilike '%data%' ");
        sqlStr.append("                     AND iitem.mensagemerro::jsonb is not null ");
        sqlStr.append("                  THEN (iitem.mensagemerro::jsonb)->> 'data' END), iitem.mensagemerro::TEXT)::text AS datajson,");
        sqlStr.append("    1::int                              AS bloco, ");
        sqlStr.append(" ''::varchar as matricula, ");
        sqlStr.append(" 0::int as disciplina, ");
        sqlStr.append(" ''::varchar as nomeDisciplina, ");
        sqlStr.append(" ''::varchar as abreviaturaDisciplina, ");
        sqlStr.append("    NULL::int                           AS rn ");
        sqlStr.append("  FROM integracaomestregritem iitem ");
        sqlStr.append("  JOIN integracaomestregr igr ");
        sqlStr.append("    ON igr.codigo = iitem.codigointegracao ");
        sqlStr.append(whereParaItensDoLoteJSONLogLote(filtros));
        sqlStr.append("), ");
        sqlStr.append("cte_integracao_operacoes AS ( ");
        sqlStr.append("  SELECT ");
        sqlStr.append("    0::bigint      AS codigointegracao, ");
        sqlStr.append("    oper.codigo::bigint                AS codigo, ");
        sqlStr.append("    0::bigint           AS codigolote, ");
        sqlStr.append("    ''::varchar          AS nomeLote, ");
        sqlStr.append("    oper.origem::text                  AS origem, ");
        sqlStr.append("    oper.created as created, ");
        sqlStr.append("    oper.nomeCreated as nomeCreated, ");
        sqlStr.append("    oper.dadosEnvio, ");
        sqlStr.append("    oper.dadosRetorno, ");
        sqlStr.append("    UPPER(coalesce((CASE WHEN oper.mensagemerro IS NOT NULL and oper.mensagemerro ilike '%status%' ");
        sqlStr.append("                     AND trim(oper.mensagemerro)::jsonb is not null ");
        sqlStr.append("                  THEN (oper.mensagemerro::jsonb)->> 'status' END), 'ERROR'))::text AS status,");
        sqlStr.append("    coalesce((CASE WHEN oper.mensagemerro IS NOT NULL and oper.mensagemerro ilike '%data%' ");
        sqlStr.append("                     AND trim(oper.mensagemerro)::jsonb is not null ");
        sqlStr.append("                  THEN (oper.mensagemerro::jsonb)->> 'data' END), oper.mensagemerro::TEXT)::text AS datajson,");
        sqlStr.append("    0::int                              AS bloco, ");
        sqlStr.append(" oper.matricula as matricula, ");
        sqlStr.append(" oper.disciplina as disciplina, ");
        sqlStr.append(" disciplina.nome as nomeDisciplina, ");
        sqlStr.append(" disciplina.abreviatura as abreviaturaDisciplina, ");
        sqlStr.append("    ROW_NUMBER() OVER (ORDER BY oper.codigo) AS rn ");
        sqlStr.append("  FROM integracaomestregroperacoes oper ");
        sqlStr.append("  left join disciplina on disciplina.codigo = oper.disciplina ");
        sqlStr.append(whereParaItensDoLoteJSONLogOperacao(filtros));
        sqlStr.append(") ");
        sqlStr.append("SELECT ");
        sqlStr.append("  codigointegracao, ");
        sqlStr.append("  codigo, ");
        sqlStr.append("  codigolote, ");
        sqlStr.append("  nomelote, ");
        sqlStr.append("  origem, ");
        sqlStr.append("  created, ");
        sqlStr.append("  nomeCreated, ");
        sqlStr.append("  dadosEnvio, ");
        sqlStr.append("  dadosRetorno, ");
        sqlStr.append("  status, ");
        sqlStr.append("  datajson, ");
        sqlStr.append("  matricula, ");
        sqlStr.append("  disciplina, ");
        sqlStr.append("  nomeDisciplina, ");
        sqlStr.append("  abreviaturaDisciplina, ");
        sqlStr.append("  COUNT(*) OVER() AS qtde_total_registros ");
        sqlStr.append("FROM ( ");
        sqlStr.append("  SELECT * FROM cte_integracoes_lote ");
        sqlStr.append("  UNION ALL ");
        sqlStr.append("  SELECT * FROM cte_integracao_operacoes ");
        sqlStr.append(") tudo ");
        sqlStr.append("ORDER BY ");
        sqlStr.append("  created DESC");

        if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
            sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
        }
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
        if (dataModelo != null && tabelaResultado.next()) {
            dataModelo.setTotalRegistrosEncontrados(0);
            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        }

        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoJSONParaErro(tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        return listaConsulta;
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarItemErroIntegracaoJson(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select  ");
        sqlStr.append("    iitem.codigointegracao::bigint as codigointegracao,  ");
        sqlStr.append("    iitem.codigo::bigint as codigo,  ");
        sqlStr.append("    igr.codigolote::bigint as codigolote,  ");
        sqlStr.append("    igr.nomeLote as nomeLote,  ");
        sqlStr.append("    COUNT(*) OVER() AS qtde_total_registros,");
        sqlStr.append("    igr.origem::text as origem,  ");
        sqlStr.append("    igr.created as created,  ");
        sqlStr.append("    igr.created as nomeCreated,  ");
        sqlStr.append("    ''::varchar as dadosEnvio, ");
        sqlStr.append("    ''::varchar as dadosRetorno, ");
        sqlStr.append("    UPPER( case when iitem.mensagemerro is not null and iitem.mensagemerro ilike '%status%' and iitem.mensagemerro::jsonb is not null  then (iitem.mensagemerro::jsonb)->> 'status' else null end )::text as status,  ");
        sqlStr.append("    case  ");
        sqlStr.append("        when iitem.mensagemerro is not null and iitem.mensagemerro ilike '%data%'   ");
        sqlStr.append("        and iitem.mensagemerro::jsonb is not null then (iitem.mensagemerro::jsonb)->> 'data'  ");
        sqlStr.append("        else iitem.mensagemerro::TEXT  ");
        sqlStr.append("    end::text as datajson,  ");
        sqlStr.append("    ''::varchar as matricula, ");
        sqlStr.append("    0::int as disciplina, ");
        sqlStr.append("    ''::varchar as nomeDisciplina, ");
        sqlStr.append("    ''::varchar as abreviaturaDisciplina ");
        sqlStr.append("from  ");
        sqlStr.append("    integracaomestregritem iitem  ");
        sqlStr.append("join integracaomestregr igr on  ");
        sqlStr.append("    igr.codigo = iitem.codigointegracao  ");
        sqlStr.append(whereParaItensDoLoteJSONLog(filtros));

        if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
            sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
        }
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
        if (dataModelo != null && tabelaResultado.next()) {
            dataModelo.setTotalRegistrosEncontrados(0);
            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        }

        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoJSONParaErro(tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        return listaConsulta;
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarIntegracaoDadosEnvioJson(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("WITH cte_json AS ( ")
                .append("  SELECT ")
                .append("    codigo, ")
                .append("    dadosenvio::jsonb     AS js ")
                .append("FROM integracaomestregritem ");
        sqlStr.append(whereParaItensDoLoteJSONCte(filtros));
        sqlStr.append(") ");
        sqlStr.append(selectDinamicoIntegracaoItensJSON());
        sqlStr.append("FROM ")
                .append("cte_json cte_j ");
        sqlStr.append(" CROSS JOIN LATERAL ");
        sqlStr.append("    jsonb_to_recordset(cte_j.js) AS item( ")
                .append("        lote             text, ")
                .append("        ano              text, ")
                .append("        semestre         text, ")
                .append("        ensino           text, ")
                .append("        descricao_turno  text, ")
                .append("        serie            jsonb, ")
                .append("        turma            jsonb, ")
                .append("        aluno            jsonb ")
                .append("    ) ");
        Boolean filtrarItens = whereParaItensDoLoteJSON(filtros, sqlStr);
        sqlStr.append("ORDER BY ")
                .append("    (item.ano)::int, ")
                .append("    item.semestre, ")
                .append("    item.ensino, ")
                .append("    item.descricao_turno");        
        if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
            if (filtrarItens) {
                sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append("0");
            }
            if (!filtrarItens) {
                sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
            }
        }
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
        if (dataModelo != null && tabelaResultado.next()) {
            dataModelo.setTotalRegistrosEncontrados(0);
            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        }

        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoJSONParaItensDoLote(tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        return listaConsulta;
    }

    /**
     * Reponsavel de encaminhar para o seu respectivo tipo de carga,
     * para consulta, alem de definir o filtro de sua origem
     *
     * @param tipoLote
     * @param filtros
     * @param dataModelo
     * @param controlarAcesso
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<IntegracaoMestreGRVO> consultarCargaLote(OperacaoTempoRealMestreGREnum tipoLote, Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario, ProgressBarVO progressBarVO) throws Exception {
        switch (tipoLote) {
            case TURMA:
                filtros.put("origem", "ALUNOS");
                return consultarCargaAlunos(filtros, dataModelo, controlarAcesso, usuario);
            case TURMA_2CH:
                filtros.put("origem", "ALUNOS");
                return consultarCargaAlunosSegundaChamada( filtros, dataModelo, controlarAcesso, usuario);
            case TURMA_EXAME:
                filtros.put("origem", "ALUNOS");
                return consultarCargaAlunosExame(filtros, dataModelo, controlarAcesso, usuario);
            default:
                List<IntegracaoMestreGRVO> vazio = new ArrayList<IntegracaoMestreGRVO>(0);
                return vazio;
        }
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarCargaAlunos( Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        Integer semestre = Integer.valueOf(filtros.get("semestre").toString());
        List<Integer> bimestres = parseBimestres(filtros.get("bimestre").toString());
        StringBuilder ciclos = new StringBuilder();
        bimestres.forEach(bimestre -> {
            ciclos.append(regraCiclo(semestre, bimestre));
        });
        StringBuilder sqlStr = new StringBuilder();
//        sqlStr.append("WITH cte_matriculaperiodos AS ( ");
//        sqlStr.append("SELECT  ");
//        sqlStr.append("    matriculaperiodo.codigo ");
//        sqlStr.append("FROM ");
//        sqlStr.append("    matricula ");
//        sqlStr.append("INNER JOIN matriculaperiodo ON ");
//        sqlStr.append("    matriculaperiodo.matricula = matricula.matricula ");
//        sqlStr.append("INNER JOIN curso ON ");
//        sqlStr.append("    curso.codigo = matricula.curso ");
//        sqlStr.append("INNER JOIN unidadeEnsino ON ");
//        sqlStr.append("    unidadeEnsino.codigo = matricula.unidadeEnsino ");
//        sqlStr.append(whereCargaAlunosCte(filtros)).append(") ");
        sqlStr.append(" SELECT ");
        sqlStr.append("    DISTINCT  ");
        sqlStr.append("    COUNT(*) OVER () AS qtde_total_registros , ");
        sqlStr.append("    matriculaperiodo.ano AS mp_ano, ");
        sqlStr.append("    matriculaperiodo.semestre AS mp_semestre, ");
        sqlStr.append("    matriculaperiodoturmadisciplina.bimestre AS mp_bimestre, ");
        sqlStr.append("    '03' AS ensino, ");
        sqlStr.append("    'NOTURNO' AS turno, ");
        sqlStr.append("    regexp_replace(pessoa.celular, '[^0-9]', '', 'g') AS numerocelular, ");
        sqlStr.append("    '' AS codigoSerie, ");
        sqlStr.append("    '' AS descricaoSerie, ");
        sqlStr.append("    (matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' ||(CASE ");
        sqlStr.append("        WHEN matriculaperiodo.semestre = '1' ");
        sqlStr.append("        AND (CASE ");
        sqlStr.append("            WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("            AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END) != '2' THEN '1B' ");
        sqlStr.append("        ELSE CASE ");
        sqlStr.append("            WHEN matriculaperiodo.semestre = '1' THEN '2B' ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN matriculaperiodo.semestre = '2' ");
        sqlStr.append("                AND (CASE ");
        sqlStr.append("                    WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("                    AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("                    ELSE CASE ");
        sqlStr.append("                        WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                        ELSE CASE ");
        sqlStr.append("                            WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                        END ");
        sqlStr.append("                    END ");
        sqlStr.append("                END) != '2' THEN '3B' ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN matriculaperiodo.semestre = '2' THEN '4B' ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END ");
        sqlStr.append("    END)) AS chaveTurma, ");
        sqlStr.append("    (matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' ||(CASE ");
        sqlStr.append("        WHEN matriculaperiodo.semestre = '1' ");
        sqlStr.append("        AND (CASE ");
        sqlStr.append("            WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("            AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END) != '2' THEN '1B' ");
        sqlStr.append("        ELSE CASE ");
        sqlStr.append("            WHEN matriculaperiodo.semestre = '1' THEN '2B' ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN matriculaperiodo.semestre = '2' ");
        sqlStr.append("                AND (CASE ");
        sqlStr.append("                    WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("                    AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("                    ELSE CASE ");
        sqlStr.append("                        WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                        ELSE CASE ");
        sqlStr.append("                            WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                        END ");
        sqlStr.append("                    END ");
        sqlStr.append("                END) != '2' THEN '3B' ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN matriculaperiodo.semestre = '2' THEN '4B' ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END ");
        sqlStr.append("    END)) AS codigoInternoTurma, ");
        sqlStr.append("    (matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' ||(CASE ");
        sqlStr.append("        WHEN matriculaperiodo.semestre = '1' ");
        sqlStr.append("        AND (CASE ");
        sqlStr.append("            WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("            AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END) != '2' THEN '1B' ");
        sqlStr.append("        ELSE CASE ");
        sqlStr.append("            WHEN matriculaperiodo.semestre = '1' THEN '2B' ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN matriculaperiodo.semestre = '2' ");
        sqlStr.append("                AND (CASE ");
        sqlStr.append("                    WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("                    AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("                    ELSE CASE ");
        sqlStr.append("                        WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                        ELSE CASE ");
        sqlStr.append("                            WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                        END ");
        sqlStr.append("                    END ");
        sqlStr.append("                END) != '2' THEN '3B' ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN matriculaperiodo.semestre = '2' THEN '4B' ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END ");
        sqlStr.append("    END)) AS codigoTurma, ");
        sqlStr.append("    (matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(disciplina.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ')|| '-' ||(CASE ");
        sqlStr.append("        WHEN matriculaperiodo.semestre = '1' ");
        sqlStr.append("        AND (CASE ");
        sqlStr.append("            WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("            AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END) != '2' THEN '1B' ");
        sqlStr.append("        ELSE CASE ");
        sqlStr.append("            WHEN matriculaperiodo.semestre = '1' THEN '2B' ");
        sqlStr.append("            ELSE CASE ");
        sqlStr.append("                WHEN matriculaperiodo.semestre = '2' ");
        sqlStr.append("                AND (CASE ");
        sqlStr.append("                    WHEN matriculaperiodoturmadisciplina.ano >= '2024' ");
        sqlStr.append("                    AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ");
        sqlStr.append("                    ELSE CASE ");
        sqlStr.append("                        WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ");
        sqlStr.append("                        ELSE CASE ");
        sqlStr.append("                            WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ");
        sqlStr.append("                        END ");
        sqlStr.append("                    END ");
        sqlStr.append("                END) != '2' THEN '3B' ");
        sqlStr.append("                ELSE CASE ");
        sqlStr.append("                    WHEN matriculaperiodo.semestre = '2' THEN '4B' ");
        sqlStr.append("                END ");
        sqlStr.append("            END ");
        sqlStr.append("        END ");
        sqlStr.append("    END)) AS nomeTurma, ");
        sqlStr.append("    disciplina.codigo AS codigoDisciplina, ");
        sqlStr.append("    disciplina.abreviatura AS siglaDisciplina, ");
        sqlStr.append("    disciplina.nome AS nomeDisciplina, ");
        sqlStr.append("    pessoa.codigo AS codigoInternoAluno, ");
        sqlStr.append("    CASE ");
        sqlStr.append("        WHEN (pessoa.registroAcademico IS NULL ");
        sqlStr.append("        OR pessoa.registroAcademico = '') THEN matricula.matricula ");
        sqlStr.append("        ELSE pessoa.registroAcademico ");
        sqlStr.append("    END AS codigoAluno, ");
        sqlStr.append("    pessoa.nome AS nomeAluno, ");
        sqlStr.append("    pessoaemailinstitucional.email AS emailAluno, ");
        sqlStr.append("    (CASE ");
        sqlStr.append("        WHEN pessoa.sabatista THEN 8 ");
        sqlStr.append("        ELSE CASE ");
        sqlStr.append("            matricula.diasemanaaula WHEN 'SEGUNGA' THEN 1 ");
        sqlStr.append("            WHEN 'TERCA' THEN 2 ");
        sqlStr.append("            WHEN 'QUARTA' THEN 3 ");
        sqlStr.append("            WHEN 'QUINTA' THEN 4 ");
        sqlStr.append("            WHEN 'SEXTA' THEN 5 ");
        sqlStr.append("            WHEN 'SABADO' THEN 6 ");
        sqlStr.append("            ELSE 7 ");
        sqlStr.append("        END ");
        sqlStr.append("    END) AS diaSemanaAluno, ");
        sqlStr.append("    CASE ");
        sqlStr.append("        WHEN pessoa.tempoestendidoprova THEN 'S' ");
        sqlStr.append("        ELSE 'N' ");
        sqlStr.append("    END AS tempoestendido, ");
        sqlStr.append("    unidadeEnsino.codigo AS codigoInternoPolo, ");
        sqlStr.append("    unidadeEnsino.abreviatura AS codigoPolo, ");
        sqlStr.append("    REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(unidadeEnsino.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') AS descricaoPolo, ");
        sqlStr.append("    curso.codigo AS codigoInternoCurso, ");
        sqlStr.append("    curso.abreviatura AS codigoCurso, ");
        sqlStr.append("    REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(curso.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') AS descricaoCurso ");
        sqlStr.append("FROM ");
        sqlStr.append("    matricula ");
        sqlStr.append("INNER JOIN matriculaperiodo ON ");
        sqlStr.append("    matricula.matricula = matriculaperiodo.matricula ");        
        sqlStr.append("INNER JOIN pessoa ON ");
        sqlStr.append("    pessoa.codigo = matricula.aluno ");
        sqlStr.append("INNER JOIN unidadeEnsino ON ");
        sqlStr.append("    unidadeEnsino.codigo = matricula.unidadeEnsino ");
        sqlStr.append("INNER JOIN curso ON ");
        sqlStr.append("    curso.codigo = matricula.curso ");
        sqlStr.append("INNER JOIN configuracaoldap ON ");
        sqlStr.append("    curso.configuracaoldap = configuracaoldap.codigo ");
        sqlStr.append("INNER JOIN pessoaemailinstitucional ON ");
        sqlStr.append("    pessoaemailinstitucional.pessoa = pessoa.codigo ");
        sqlStr.append("    AND pessoaemailinstitucional.statusativoinativoenum = 'ATIVO' ");
        sqlStr.append("    AND pessoaemailinstitucional.email ilike '%'||configuracaoldap.dominio||'%' ");
        sqlStr.append("    AND pessoaemailinstitucional.codigo = ( ");
        sqlStr.append("        SELECT ");
        sqlStr.append("            pei.codigo ");
        sqlStr.append("        FROM ");
        sqlStr.append("            pessoaemailinstitucional pei ");
        sqlStr.append("        WHERE ");
        sqlStr.append("            pei.pessoa = pessoa.codigo ");
        sqlStr.append("            and pei.email ilike '%'||configuracaoldap.dominio||'%' ");
        sqlStr.append("            AND pei.statusativoinativoenum = 'ATIVO' ");
        sqlStr.append("        ORDER BY ");
        sqlStr.append("            pei.codigo DESC ");
        sqlStr.append("        LIMIT 1) ");
        sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON ");
        sqlStr.append("    matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        sqlStr.append("INNER JOIN historico ON ");
        sqlStr.append("    historico.matricula = matricula.matricula ");
        sqlStr.append("    AND historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sqlStr.append("INNER JOIN disciplina ON ");
        sqlStr.append("    disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");        
        sqlStr.append("LEFT JOIN gradedisciplina ON ");
        sqlStr.append("    gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina ");
        sqlStr.append("LEFT JOIN gradecurriculargrupooptativadisciplina ON ");
        sqlStr.append("    gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina ");
        sqlStr.append(whereCargaAlunos(filtros));
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoAluno(OperacaoTempoRealMestreGREnum.TURMA, tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        dataModelo.setTotalRegistrosEncontrados(listaConsulta.size());
        return listaConsulta;
    }

    @Override
    public List<IntegracaoMestreGRVO> consultarCargaAlunosExame(Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        StringBuilder sqlStr = new StringBuilder();
        sqlStr                
                .append("SELECT DISTINCT ")
                .append("matriculaperiodo.ano as mp_ano, ")
                .append("matriculaperiodo.semestre mp_semestre, ")
                .append("matriculaperiodoturmadisciplina.bimestre mp_bimestre, ")
                .append("'03' as ensino, ")
                .append("'NOTURNO' as turno, ")
                .append("regexp_replace(pessoa.celular, '[^0-9]', '', 'g') AS numerocelular, ")
                .append("'' as codigoSerie, ")
                .append("'' as descricaoSerie, ")
                .append("(matriculaperiodo.ano||'-'||disciplina.abreviatura||'-'||(case when matriculaperiodo.semestre = '1' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2' then '1B' else case when matriculaperiodo.semestre = '1' then '2B' else case when matriculaperiodo.semestre = '2' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2'  then '3B' else case when matriculaperiodo.semestre = '2' then '4B' end  end  end end))").append(" || '-EX' as chaveTurma, ")
                .append("(matriculaperiodo.ano||'-'||disciplina.abreviatura||'-'||(case when matriculaperiodo.semestre = '1' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2' then '1B' else case when matriculaperiodo.semestre = '1' then '2B' else case when matriculaperiodo.semestre = '2' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2'  then '3B' else case when matriculaperiodo.semestre = '2' then '4B' end  end  end end))").append(" || '-EX' as codigoInternoTurma, ")
                .append("(matriculaperiodo.ano||'-'||disciplina.abreviatura||'-'||(case when matriculaperiodo.semestre = '1' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2' then '1B' else case when matriculaperiodo.semestre = '1' then '2B' else case when matriculaperiodo.semestre = '2' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2'  then '3B' else case when matriculaperiodo.semestre = '2' then '4B' end  end  end end))").append(" || '-EX' as codigoTurma, ")
                .append("(matriculaperiodo.ano||'-'||disciplina.abreviatura||'-'||replace(replace(replace(replace(replace(replace(disciplina.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ')||'-'||(case when matriculaperiodo.semestre = '1' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2' then '1B' else case when matriculaperiodo.semestre = '1' then '2B' else case when matriculaperiodo.semestre = '2' and (case when matriculaperiodoturmadisciplina.ano >= '2024' and matriculaperiodoturmadisciplina.bimestre is not null then ")
                .append("matriculaperiodoturmadisciplina.bimestre else ")
                .append("case when gradedisciplina.codigo is not null  then gradedisciplina.bimestre ")
                .append("else case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.bimestre end end end) != '2'  then '3B' else case when matriculaperiodo.semestre = '2' then '4B' end  end  end end))").append(" || '-EX' as nomeTurma, ")
                .append("disciplina.codigo as codigoDisciplina, ")
                .append("disciplina.abreviatura as siglaDisciplina, ")
                .append("disciplina.nome as nomeDisciplina, ")
                .append("pessoa.codigo as codigoInternoAluno, ")
                .append("case when (pessoa.registroAcademico is null or pessoa.registroAcademico = '') then matricula.matricula else pessoa.registroAcademico end as codigoAluno, ")
                .append("pessoa.nome as nomeAluno, ")
                .append("pessoaemailinstitucional.email as emailAluno, ")
                .append("(case when pessoa.sabatista then 8 else case matricula.diasemanaaula when 'SEGUNGA' then 1 when 'TERCA' then 2 when 'QUARTA' then 3 when 'QUINTA' then 4 when 'SEXTA' then 5 when 'SABADO' then 6 else 7 end end) as diaSemanaAluno, ")
                .append("case when pessoa.tempoestendidoprova then 'S' else 'N' end as tempoestendido, ")
                .append("unidadeEnsino.codigo as codigoInternoPolo, ")
                .append("unidadeEnsino.abreviatura as codigoPolo, ")
                .append("replace(replace(replace(replace(replace(replace(unidadeEnsino.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') as descricaoPolo, ")
                .append("curso.codigo as codigoInternoCurso, ")
                .append("curso.abreviatura as codigoCurso, ")
                .append("replace(replace(replace(replace(replace(replace(curso.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') as descricaoCurso ")
                .append("FROM matricula ")
                .append(joinsAlunoFromMatricula())
                .append(whereCargaExameAlunos(filtros));                

//        if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
//            sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
//        }
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
//        if (dataModelo != null && tabelaResultado.next()) {
//            dataModelo.setTotalRegistrosEncontrados(0);
//            dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
//        }
//
//        tabelaResultado.beforeFirst();
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoAluno(OperacaoTempoRealMestreGREnum.TURMA_EXAME, tabelaResultado);
        dataModelo.setListaConsulta(listaConsulta);
        dataModelo.setTotalRegistrosEncontrados(listaConsulta.size());
        return listaConsulta;
    }

    /**
     * A geração de lotes de segunda chamada deve se atentar
     * que não pode ter ambos os bimestres no nome e filtrar o bimestre pelo where
     *
     * @param tipoLote
     * @param filtros
     * @param dataModelo
     * @param controlarAcesso
     * @param usuario
     * @return
     * @throws Exception
     */
    @Override
    public List<IntegracaoMestreGRVO> consultarCargaAlunosSegundaChamada( Map<String, Object> filtros, DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        dataModelo.getListaFiltros().clear();
        dataModelo.getListaConsulta().clear();
        Integer semestre = Integer.valueOf(filtros.get("semestre").toString());
        List<Integer> bimestres = parseBimestres(filtros.get("bimestre").toString());
        StringBuilder ciclos = new StringBuilder();
        bimestres.forEach(bimestre -> {
            ciclos.append(regraCiclo(semestre, bimestre));
        });
        StringBuilder sqlStr = new StringBuilder();
        sqlStr        
                .append("SELECT ")
                .append("matriculaperiodo.ano as mp_ano, ")
                .append("matriculaperiodo.semestre as mp_semestre, ")
                .append("matriculaperiodoturmadisciplina.bimestre mp_bimestre, ")
                .append("'03' as ensino, ")
                .append("'NOTURNO' as turno, ")
                .append("regexp_replace(pessoa.celular, '[^0-9]', '', 'g') AS numerocelular, ")
                .append("'' as codigoSerie, ")
                .append("'' as descricaoSerie, ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END))").append(" || '-2C-"+ ciclos + "' ").append("as chaveTurma, ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END))").append(" || '-2C-"+ ciclos +"' ").append("as codigoInternoTurma, ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END))").append(" || '-2C-"+ ciclos +"' ").append(" as codigoTurma, ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(disciplina.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END))").append(" || '-2C-"+ ciclos +"' ").append(" as nomeTurma, ")
                .append("disciplina.codigo as codigoDisciplina, ")
                .append("disciplina.abreviatura as sigladisciplina, ")
                .append("disciplina.nome as nomeDisciplina, ")
                .append("pessoa.codigo as codigoInternoAluno, ")
                .append("CASE ")
                .append("WHEN (pessoa.registroAcademico IS NULL OR pessoa.registroAcademico = '') THEN matricula.matricula ")
                .append("ELSE pessoa.registroAcademico ")
                .append("END as codigoAluno, ")
                .append("pessoa.nome as nomeAluno, ")
                .append("pessoaemailinstitucional.email as emailAluno, ")
                .append("(CASE ")
                .append("WHEN pessoa.sabatista THEN 8 ")
                .append("ELSE CASE ")
                .append("matricula.diasemanaaula WHEN 'SEGUNGA' THEN 1 ")
                .append("WHEN 'TERCA' THEN 2 ")
                .append("WHEN 'QUARTA' THEN 3 ")
                .append("WHEN 'QUINTA' THEN 4 ")
                .append("WHEN 'SEXTA' THEN 5 ")
                .append("WHEN 'SABADO' THEN 6 ")
                .append("ELSE 7 ")
                .append("END END) as diaSemanaAluno, ")
                .append("CASE ")
                .append("WHEN pessoa.tempoestendidoprova THEN 'S' ")
                .append("ELSE 'N' ")
                .append("END as tempoestendido, ")
                .append("unidadeEnsino.codigo as codigoInternoPolo, ")
                .append("unidadeEnsino.abreviatura as codigoPolo, ")
                .append("REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(unidadeEnsino.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') as descricaoPolo, ")
                .append("curso.codigo as codigoInternoCurso, ")
                .append("curso.abreviatura as codigoCurso, ")
                .append("REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(curso.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') as descricaoCurso ")
                .append("FROM requerimento")
                .append(joinsAlunoFromRequerimento())
                .append(whereCargaAlunosRequerimento(filtros))
                 .append(" GROUP BY ")
                 .append("matriculaperiodo.ano, ")
                .append("matriculaperiodo.semestre, ")
                .append("matriculaperiodoturmadisciplina.bimestre, ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END)), ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END)), ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END)), ")
                .append("(matriculaperiodo.ano || '-' || disciplina.abreviatura || '-' || REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(disciplina.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') || '-' || (CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '1B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '1' THEN '2B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' ")
                .append("AND (CASE ")
                .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                .append("ELSE CASE ")
                .append("WHEN gradecurriculargrupooptativadisciplina.codigo IS NOT NULL THEN gradecurriculargrupooptativadisciplina.bimestre ")
                .append("END END END) != '2' THEN '3B' ")
                .append("ELSE CASE ")
                .append("WHEN matriculaperiodo.semestre = '2' THEN '4B' ")
                .append("END END END END)), ")
                .append("disciplina.codigo, ")
                .append("disciplina.abreviatura, ")
                .append("disciplina.nome, ")
                .append("pessoa.codigo, ")
                .append("CASE ")
                .append("WHEN (pessoa.registroAcademico IS NULL OR pessoa.registroAcademico = '') THEN matricula.matricula ")
                .append("ELSE pessoa.registroAcademico ")
                .append("END, ")
                .append("pessoa.nome, ")
                .append("pessoaemailinstitucional.email, ")
                .append("(CASE ")
                .append("WHEN pessoa.sabatista THEN 8 ")
                .append("ELSE CASE ")
                .append("matricula.diasemanaaula WHEN 'SEGUNGA' THEN 1 ")
                .append("WHEN 'TERCA' THEN 2 ")
                .append("WHEN 'QUARTA' THEN 3 ")
                .append("WHEN 'QUINTA' THEN 4 ")
                .append("WHEN 'SEXTA' THEN 5 ")
                .append("WHEN 'SABADO' THEN 6 ")
                .append("ELSE 7 ")
                .append("END END), ")
                .append("CASE ")
                .append("WHEN pessoa.tempoestendidoprova THEN 'S' ")
                .append("ELSE 'N' ")
                .append("END, ")
                .append("unidadeEnsino.codigo, ")
                .append("unidadeEnsino.abreviatura, ")
                .append("REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(unidadeEnsino.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' '), ")
                .append("curso.codigo, ")
                .append("curso.abreviatura, ")
                .append("REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(curso.nome, ',', ''), ';', ''), '''', ''), '-', ' '), '   ', ' '), '  ', ' ') ");

       
//        System.out.println(sqlStr.toString());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
       

       
        List<IntegracaoMestreGRVO> listaConsulta = montarDadosResultadoAluno(OperacaoTempoRealMestreGREnum.TURMA_2CH, tabelaResultado);        
        dataModelo.setListaConsulta(listaConsulta);
        dataModelo.setTotalRegistrosEncontrados(listaConsulta.size());
        return listaConsulta;
    }

    

    private List<IntegracaoMestreGRVO> montarDadosResultadoAluno(OperacaoTempoRealMestreGREnum origem, SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaAluno(origem, tabelaResultado));
        }
        return vetResultado;
    }

    private List<IntegracaoMestreGRVO> montarDadosResultadoJSONParaItensDoLote(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
            while (tabelaResultado.next()) {
                vetResultado.add(montarDadosConsultaJSONParaItensDoLoteAluno(tabelaResultado));
            }
        
             return vetResultado;
    }

    private List<IntegracaoMestreGRVO> montarDadosResultadoJSONParaErro(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaJSONParaItensDeErro(tabelaResultado));
        }
        return vetResultado;
    }

    private List<IntegracaoMestreGRVO> montarDadosResultadoRegistroIntegracaoLote(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaRegistroIntegracaoLote(tabelaResultado));
        }
        return vetResultado;
    }

    private List<IntegracaoMestreGRVO> montarDadosResultadoItemIntegracaoLotePorCodigo(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaRegistroIntegracaoLoteItem(tabelaResultado));
        }
        return vetResultado;
    }

    private List<IntegracaoMestreGRVO> montarDadosResultadoLogAluno(SqlRowSet tabelaResultado) throws Exception {
        List<IntegracaoMestreGRVO> vetResultado = new ArrayList<IntegracaoMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaRegistroIntegracaoLogItem(tabelaResultado));
        }
        return vetResultado;
    }

    private List<OperacaoTempoRealMestreGRVO> montarDadosResultadoOperacoes(SqlRowSet tabelaResultado) throws Exception {
        List<OperacaoTempoRealMestreGRVO> vetResultado = new ArrayList<OperacaoTempoRealMestreGRVO>();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultaRegistroOperacao(tabelaResultado));
        }
        return vetResultado;
    }

    private IntegracaoMestreGRVO montarDadosConsultaJSONParaItensDoLoteAluno(SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = new IntegracaoMestreGRVO();
        integracaoLote.setCodigoLote(resultado.getInt("codigo"));
        integracaoLote.setAno(resultado.getString("ano"));
        integracaoLote.setSemestre(resultado.getString("semestre"));
        integracaoLote.setEnsino(resultado.getString("ensino"));
        integracaoLote.setTurno(resultado.getString("turno"));
//		integracaoLote.setCodigoSerie(resultado.getInt("codigocursoserie"));
//		integracaoLote.setNomeSerie(resultado.getString("nomecursoserie"));
        integracaoLote.setChaveTurma(resultado.getString("chaveturma"));
        integracaoLote.setCodigoTurma(resultado.getString("codigoturma"));
        integracaoLote.setCodigoInternoTurma(resultado.getString("codigointernoturma"));
        integracaoLote.setCodigoTurma(resultado.getString("codigoturma"));
        integracaoLote.setNomeTurma(resultado.getString("nometurma"));

        //Alunos
        integracaoLote.setCodigoDisciplina(resultado.getInt("codigodisciplina"));
        integracaoLote.setSiglaDisciplina(resultado.getString("sigladisciplina"));
        integracaoLote.setNomeDisciplina(resultado.getString("nomedisciplina"));

        integracaoLote.setCodigoInternoAluno(resultado.getInt("codigointernoaluno"));
        integracaoLote.setCodigoAluno(resultado.getString("codigoaluno"));
        integracaoLote.setNomeAluno(resultado.getString("nomealuno"));
        integracaoLote.setEmailAluno(resultado.getString("emailaluno"));
        integracaoLote.setCodigoDiaSemanaAluno(resultado.getInt("codigodiasemana"));
        integracaoLote.setTempoEstendidoAluno(resultado.getString("tempoestendido"));
        integracaoLote.setNumeroCelularAluno(resultado.getString("numerocelularaluno"));

        integracaoLote.setCodigoInternoPolo(resultado.getInt("codigointernopolo"));
        integracaoLote.setCodigoPolo(resultado.getString("codigopolo"));
        integracaoLote.setDescricaoPolo(resultado.getString("descricaopolo"));

        integracaoLote.setCodigoInternoCurso(resultado.getInt("codigointernocurso"));
        integracaoLote.setCodigoCurso(resultado.getString("codigocurso"));
        integracaoLote.setDescricaoCurso(resultado.getString("descricaocurso"));

        return integracaoLote;
    }



    private IntegracaoMestreGRVO montarDadosConsultaJSONParaItensDeErro(SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = new IntegracaoMestreGRVO();
        integracaoLote.setIdItem(resultado.getInt("codigo"));
        integracaoLote.setCodigoLote(resultado.getInt("codigolote"));
        integracaoLote.setNomeLote(resultado.getString("nomelote"));
        integracaoLote.setCodigo(resultado.getInt("codigointegracao"));
        integracaoLote.setStatusJson(resultado.getString("status"));
        integracaoLote.setDadosEnvio(resultado.getString("dadosEnvio"));
        integracaoLote.setDadosRetorno(resultado.getString("dadosRetorno"));
        integracaoLote.setDataJson(resultado.getString("datajson"));
        integracaoLote.setOrigem(OperacaoTempoRealMestreGREnum.valueOf(resultado.getString("origem")));
        integracaoLote.setCreated(resultado.getDate("created"));
        integracaoLote.setNomeCreated(resultado.getString("nomeCreated"));
        integracaoLote.setMatricula(resultado.getString("matricula"));
        integracaoLote.setCodigoDisciplina(resultado.getInt("disciplina"));
        integracaoLote.setNomeDisciplina(resultado.getString("nomeDisciplina"));
        integracaoLote.setAbreviaturaDisciplinas(resultado.getString("abreviaturaDisciplina"));
        return integracaoLote;
    }

    private IntegracaoMestreGRVO montarDadosConsultaRegistroIntegracaoLote(SqlRowSet resultado) throws Exception {
        IntegracaoMestreGRVO integracaoLote = new IntegracaoMestreGRVO();
        integracaoLote.setCodigo(resultado.getInt("codigo"));
        integracaoLote.setOrigem(OperacaoTempoRealMestreGREnum.valueOf(resultado.getString("origem")));
        integracaoLote.setNomeLote(resultado.getString("nomelote"));
        integracaoLote.setAno(resultado.getString("ano"));
        integracaoLote.setSemestre(resultado.getString("semestre"));
        integracaoLote.setBimestre(resultado.getInt("bimestre"));
        integracaoLote.setNivelEducacional(TipoNivelEducacional.valueOf(resultado.getString("nivelEducacional")));
        integracaoLote.setUnidadeEnsinos(resultado.getString("unidadeEnsinos"));
        integracaoLote.setCursos(resultado.getString("cursos"));
        integracaoLote.getDisciplinaVO().setCodigo(resultado.getInt("disciplina"));
        if(Uteis.isAtributoPreenchido(integracaoLote.getDisciplinaVO().getCodigo())) {
        	integracaoLote.setDisciplinaVO(getAplicacaoControle().getDisciplinaVO(integracaoLote.getDisciplinaVO().getCodigo(), null));	
        }
        integracaoLote.setCodigoLote(resultado.getInt("codigolote"));

        integracaoLote.setSituacao(resultado.getString("situacao"));
        integracaoLote.setQtdeRegistros(resultado.getInt("qtderegistros"));
        integracaoLote.setPeriodoRequerimentoInicio(resultado.getDate("periodoRequerimentoInicio"));        
        integracaoLote.setPeriodoRequerimentoTermino(resultado.getDate("periodoRequerimentoTermino"));
        integracaoLote.setCreated(resultado.getDate("created"));
        integracaoLote.setDataTransmissao(resultado.getDate("datatransmissao"));
        integracaoLote.setNomeCreated(resultado.getString("nomecreated"));
        integracaoLote.setCodigoCreated(resultado.getInt("codigocreated"));
//        System.out.print(integracaoLote.getNomeTurma());
        return integracaoLote;
    }

    private IntegracaoMestreGRVO montarDadosConsultaRegistroIntegracaoLoteItem(SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = new IntegracaoMestreGRVO();
        integracaoLote.setDadosEnvio(resultado.getString("dadosenvio"));
        integracaoLote.setCodigo(resultado.getInt("codigointegracao"));
        integracaoLote.setIdItem(resultado.getInt("codigo"));
        return integracaoLote;
    }

    private IntegracaoMestreGRVO montarDadosConsultaRegistroIntegracaoLogItem(SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = new IntegracaoMestreGRVO();
        integracaoLote.setCodigo(resultado.getInt("codigo"));
        integracaoLote.setOrigem(OperacaoTempoRealMestreGREnum.valueOf(resultado.getString("origem")));
        integracaoLote.setMatricula(resultado.getString("matricula"));
        integracaoLote.setCodigoAluno(resultado.getString("codigoAluno"));
        integracaoLote.setChaveTurma(resultado.getString("chaveTurma"));
        integracaoLote.setNomeAluno(resultado.getString("nome"));
        integracaoLote.setEmailAluno(resultado.getString("email"));
        integracaoLote.setNumeroCelularAluno(resultado.getString("celular"));
        integracaoLote.setCodigoDiaSemanaAluno(resultado.getInt("diaSemana"));
        integracaoLote.setTempoEstendidoAluno(resultado.getString("tempoEstendido"));        
        integracaoLote.setAno(resultado.getString("ano"));
        integracaoLote.setSemestre(resultado.getString("semestre"));
        integracaoLote.setBimestre(resultado.getInt("bimestre"));
        integracaoLote.setDadosEnvio(resultado.getString("dadosenvio"));
        integracaoLote.setDadosRetorno(resultado.getString("dadosretorno"));
        integracaoLote.setMensagemErro(resultado.getString("mensagemErro"));
        integracaoLote.setCreated(resultado.getDate("created"));
        integracaoLote.setNomeCreated(resultado.getString("nomecreated"));
        integracaoLote.setProcessado(resultado.getBoolean("processado"));
        integracaoLote.setStatus(resultado.getString("status"));
        return integracaoLote;
    }

    private OperacaoTempoRealMestreGRVO montarDadosConsultaRegistroOperacao(SqlRowSet resultado) {
        OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO = new OperacaoTempoRealMestreGRVO();
        operacaoTempoRealMestreGRVO.setCodigo(resultado.getLong("codigo"));
        operacaoTempoRealMestreGRVO.setOperacaoTempoRealMestreGREnum(OperacaoTempoRealMestreGREnum.valueOf(resultado.getString("origem")));
        operacaoTempoRealMestreGRVO.setMatricula(resultado.getString("matricula"));
        operacaoTempoRealMestreGRVO.setAno(resultado.getInt("ano"));
        operacaoTempoRealMestreGRVO.setSemestre(resultado.getString("semestre"));
        operacaoTempoRealMestreGRVO.setBimestre(resultado.getInt("bimestre"));
        operacaoTempoRealMestreGRVO.setDadosEnvio(resultado.getString("dadosenvio"));
        operacaoTempoRealMestreGRVO.setProcessado(resultado.getBoolean("processado"));
        return operacaoTempoRealMestreGRVO;
    }

    private IntegracaoMestreGRVO montarDadosConsultaTurma(OperacaoTempoRealMestreGREnum origem, SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = new IntegracaoMestreGRVO();
        integracaoLote.setAno(resultado.getString("mp_ano"));
        integracaoLote.setSemestre(resultado.getString("mp_semestre"));
        integracaoLote.setBimestre(resultado.getInt("mp_bimestre"));
        integracaoLote.setEnsino(resultado.getString("ensino"));
        integracaoLote.setTurno(resultado.getString("turno"));
        integracaoLote.setChaveTurma(resultado.getString("chaveturma"));
        integracaoLote.setCodigoInternoTurma(resultado.getString("codigointernoturma"));
        integracaoLote.setCodigoTurma(resultado.getString("codigoturma"));
        integracaoLote.setNomeTurma(resultado.getString("nometurma"));
        integracaoLote.setOrigem(origem);
        return integracaoLote;
    }

    private IntegracaoMestreGRVO montarDadosConsultaDisciplina(OperacaoTempoRealMestreGREnum origem, SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = montarDadosConsultaTurma(origem, resultado);
        integracaoLote.setSiglaDisciplina(resultado.getString("sigladisciplina"));
        integracaoLote.setNomeDisciplina(resultado.getString("nomedisciplina"));
        integracaoLote.setCodigoDisciplina(resultado.getInt("codigodisciplina"));        
        return integracaoLote;
    }

    private IntegracaoMestreGRVO montarDadosConsultaAluno(OperacaoTempoRealMestreGREnum origem, SqlRowSet resultado) {
        IntegracaoMestreGRVO integracaoLote = montarDadosConsultaDisciplina(origem, resultado);

        integracaoLote.setCodigoInternoAluno(resultado.getInt("codigointernoaluno"));
        integracaoLote.setCodigoAluno(resultado.getString("codigoaluno"));
        integracaoLote.setNomeAluno(resultado.getString("nomealuno"));
        integracaoLote.setEmailAluno(resultado.getString("emailaluno"));
        integracaoLote.setCodigoDiaSemanaAluno(resultado.getInt("diasemanaaluno"));
        integracaoLote.setTempoEstendidoAluno(resultado.getString("tempoestendido"));

        integracaoLote.setCodigoInternoPolo(resultado.getInt("codigointernopolo"));
        integracaoLote.setCodigoPolo(resultado.getString("codigopolo"));
        integracaoLote.setDescricaoPolo(resultado.getString("descricaopolo"));

        integracaoLote.setCodigoInternoCurso(resultado.getInt("codigointernocurso"));
        integracaoLote.setCodigoCurso(resultado.getString("codigocurso"));
        integracaoLote.setDescricaoCurso(resultado.getString("descricaocurso"));
        integracaoLote.setNumeroCelularAluno(resultado.getString("numerocelular"));

        integracaoLote.setOrigem(origem);
        return integracaoLote;
    }

    /**
     * Responsavel por encaminhar para o metodo correto de conversao json de acordo a com a origem da lista
     *
     * @param integracao
     * @param origem
     * @return
     */
    @Override
    public JsonArray montarLoteJson(List<IntegracaoMestreGRVO> integracao, String origem) {
        JsonArray dataJsonArray = new JsonArray();
        integracao.forEach(lote -> {
            dataJsonArray.add(converterLoteJSONAluno(lote));
        });
        return dataJsonArray;
    }

    private StringBuilder whereCargaAlunos(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        if (filtros.containsKey("ano")
                && filtros.get("ano") != null
                && !filtros.get("ano").toString().trim().isEmpty()) {
            where.append("AND matriculaperiodo.ano = '")
                    .append(filtros.get("ano"))
                    .append("' ");
        }

        if (filtros.containsKey("semestre")
                && filtros.get("semestre") != null
                && !filtros.get("semestre").toString().trim().isEmpty()) {
            where.append("AND matriculaperiodo.semestre = '")
                    .append(filtros.get("semestre"))
                    .append("' ");
        }

        where.append("AND curso.niveleducacional = '").append(((TipoNivelEducacional)filtros.get("nivelEducacional")).getValor()).append("' ")
                .append("AND matricula.situacao IN ('AT', 'FI') ");
        where.append(" and matriculaperiodo.situacaomatriculaperiodo  in ('AT', 'FI', 'CO') ");

        if (filtros.containsKey("unidadeensino")
                && filtros.get("unidadeensino") != null
                && !filtros.get("unidadeensino").toString().trim().isEmpty()) {
            where.append("AND unidadeensino.codigo IN (")
                    .append(filtros.get("unidadeensino"))
                    .append(") ");
        }

        if (filtros.containsKey("curso")
                && filtros.get("curso") != null
                && !filtros.get("curso").toString().trim().isEmpty()
        ) {
            where.append("AND curso.codigo IN (")
                    .append(filtros.get("curso"))
                    .append(") ");
        }

        if (filtros.containsKey("codigolote")
                && filtros.get("codigolote") != null
                && !filtros.get("codigolote").toString().trim().isEmpty()
                && !filtros.get("codigolote").toString().trim().equals("0")) {
            where.append("AND codigolote = '")
                    .append(filtros.get("codigolote"))
                    .append("' ");
        }
        if (filtros.containsKey("bimestre")) {
            where.append(" AND ((CASE ")
                    .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                    .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                    .append("ELSE ")
                    .append("CASE WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                    .append("END END) IS NOT NULL ")
                    .append("AND (CASE ")
                    .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                    .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                    .append("ELSE ")
                    .append("CASE WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                    .append("END END) in ( ")
                    .append(filtros.get("bimestre"))
                    .append(" ))");
        }

        if (filtros.containsKey("disciplina")
                && filtros.get("disciplina") != null
                && !filtros.get("disciplina").toString().trim().isEmpty()
                && !filtros.get("disciplina").toString().trim().equals("0")) {
            where.append("AND disciplina.codigo = '")
                    .append(filtros.get("disciplina"))
                    .append("' ");
        }


        where
                .append("AND disciplina.classificacaodisciplina NOT IN ('TCC', 'ESTAGIO', 'PROJETO_INTEGRADOR') ")
                .append("AND historico.mapaequivalenciadisciplinamatrizcurricular IS NULL ");
        return where;
    }

    private StringBuilder whereCargaAlunosRequerimento(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        if (filtros.containsKey("ano")) {
            where.append("AND matriculaperiodo.ano = '")
                    .append(filtros.get("ano"))
                    .append("' ");
        }
        if (filtros.containsKey("semestre")) {
            where.append("AND matriculaperiodo.semestre = '")
                    .append(filtros.get("semestre"))
                    .append("' ");
        }
        where.append("AND curso.niveleducacional = '").append(((TipoNivelEducacional)filtros.get("nivelEducacional")).getValor()).append("' ");

        if (filtros.containsKey("unidadeensino")
                && filtros.get("unidadeensino") != null
                && !filtros.get("unidadeensino").toString().trim().isEmpty()) {
            where.append("AND unidadeensino.codigo IN (")
                    .append(filtros.get("unidadeensino"))
                    .append(") ");
        }

        where.append("and requerimento.situacao = 'FD' ");
        where.append("and tiporequerimento.tipo = 'SEGUNDA_CHAMADA' ");

        if (filtros.containsKey("curso")
                && filtros.get("curso") != null
                && !filtros.get("curso").toString().trim().isEmpty()
        ) {
            where.append("AND curso.codigo IN (")
                    .append(filtros.get("curso"))
                    .append(") ");
        }

        if (filtros.containsKey("disciplina")
                && filtros.get("disciplina") != null
                && !filtros.get("disciplina").toString().trim().isEmpty()
                && !filtros.get("disciplina").toString().trim().equals("0")) {
            where.append("AND disciplina.codigo = '")
                    .append(filtros.get("disciplina"))
                    .append("' ");
        }

        if ((filtros.containsKey("datainiciorequerimento")
                && filtros.get("datainiciorequerimento") != null
                && !filtros.get("datainiciorequerimento").toString().trim().isEmpty()
                && !filtros.get("datainiciorequerimento").toString().trim().equals("0"))
                && (filtros.containsKey("datafimrequerimento")
                && filtros.get("datafimrequerimento") != null
                && !filtros.get("datafimrequerimento").toString().trim().isEmpty()
                && !filtros.get("datafimrequerimento").toString().trim().equals("0"))) {
            where.append("and cast(requerimento.data as DATE) between ")
                    .append(" '" + filtros.get("datainiciorequerimento") + "' ")
                    .append(" and ")
                    .append(" '" + filtros.get("datafimrequerimento") + "' ");
        }

        if (filtros.containsKey("codigolote")
                && filtros.get("codigolote") != null
                && !filtros.get("codigolote").toString().trim().isEmpty()
                && !filtros.get("codigolote").toString().trim().equals("0")) {
            where.append("AND codigolote = '")
                    .append(filtros.get("codigolote"))
                    .append("' ");
        }
        return where;
    }

    private StringBuilder whereCargaExameAlunos(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        if (filtros.containsKey("ano")) {
            where.append("AND matriculaperiodo.ano = '")
                    .append(filtros.get("ano"))
                    .append("' ");
        }
        if (filtros.containsKey("semestre")) {
            where.append("AND matriculaperiodo.semestre = '")
                    .append(filtros.get("semestre"))
                    .append("' ");
        }
        where.append("AND curso.niveleducacional = '").append(((TipoNivelEducacional)filtros.get("nivelEducacional")).getValor()).append("' ")
                .append("AND matricula.situacao IN ('AT', 'FI', 'FO') ")
                .append("AND (historico.nota3 IS NULL ")
                .append("OR historico.nota3 < 5) ")
                .append("AND EXISTS ( ")
                .append("SELECT configuracaoacademiconota.codigo ")
                .append("FROM configuracaoacademiconota ")
                .append("WHERE configuracaoacademiconota.configuracaoacademico = historico.configuracaoacademico ")
                .append("AND configuracaoacademiconota.variavel = 'EX') ")
                .append("AND historico.mapaequivalenciadisciplinamatrizcurricular IS NULL ")
                .append("AND historico.situacao NOT IN ('AA', 'AP', 'AE') ");

        if (filtros.containsKey("unidadeensino")
                && filtros.get("unidadeensino") != null
                && !filtros.get("unidadeensino").toString().trim().isEmpty()) {
            where.append("AND unidadeensino.codigo IN (")
                    .append(filtros.get("unidadeensino"))
                    .append(") ");
        }

        if (filtros.containsKey("curso")
                && filtros.get("curso") != null
                && !filtros.get("curso").toString().trim().isEmpty()
        ) {
            where.append("AND curso.codigo IN (")
                    .append(filtros.get("curso"))
                    .append(") ");
        }
        if (filtros.containsKey("disciplina")
                && filtros.get("disciplina") != null
                && !filtros.get("disciplina").toString().trim().isEmpty()
                && !filtros.get("disciplina").toString().trim().equals("0")) {
            where.append("AND disciplina.codigo = '")
                    .append(filtros.get("disciplina"))
                    .append("' ");
        }

        if (filtros.containsKey("bimestre")) {
            where.append(" AND ((CASE ")
                    .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                    .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                    .append("ELSE ")
                    .append("CASE WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                    .append("END END) IS NOT NULL ")
                    .append("AND (CASE ")
                    .append("WHEN matriculaperiodoturmadisciplina.ano >= '2024' ")
                    .append("AND matriculaperiodoturmadisciplina.bimestre IS NOT NULL THEN matriculaperiodoturmadisciplina.bimestre ")
                    .append("ELSE ")
                    .append("CASE WHEN gradedisciplina.codigo IS NOT NULL THEN gradedisciplina.bimestre ")
                    .append("END END) in ( ")
                    .append(filtros.get("bimestre"))
                    .append(" ))");
        }

        if (filtros.containsKey("codigolote")
                && filtros.get("codigolote") != null
                && !filtros.get("codigolote").toString().trim().isEmpty()
                && !filtros.get("codigolote").toString().trim().equals("0")) {
            where.append("AND codigolote = '")
                    .append(filtros.get("codigolote"))
                    .append("' ");
        }
        where
                .append("AND historico.mapaequivalenciadisciplinamatrizcurricular IS NULL ");
        return where;
    }


    private StringBuilder whereRegistroIntegracaoLote(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");

        if (validarFiltros(filtros, "codigo")) {
            where.append("AND codigo IN (").append(filtros.get("codigo")).append(") ");
        }
        if (validarFiltros(filtros, "origem")) {
            where.append("AND origem IN (").append(filtros.get("origem")).append(") ");
        }
        if (validarFiltros(filtros, "nomelote")) {
            where.append("AND nomelote = '").append(filtros.get("nomelote")).append("' ");
        }
        if (validarFiltros(filtros, "codigolote")) {
            where.append("AND codigolote = '").append(filtros.get("codigolote")).append("' ");
        }
        if (validarFiltros(filtros, "nivelEducacional")) {
        	where.append("AND nivelEducacional = '").append(((TipoNivelEducacional)filtros.get("nivelEducacional")).name()).append("' ");
        }
        if (validarFiltros(filtros, "situacao")) {
            where.append("AND situacao = '").append(filtros.get("situacao")).append("' ");
        }
        if (validarFiltros(filtros, "qtderegistros")) {
            where.append("AND qtderegistros = ").append(filtros.get("qtderegistros")).append(" ");
        }
        if (validarFiltros(filtros, "created")) {
            where.append("AND created = '").append(filtros.get("created")).append("' ");
        }
        if (validarFiltros(filtros, "datatransmissao")) {
            where.append("AND datatransmissao = '").append(filtros.get("datatransmissao")).append("' ");
        }
        if (validarFiltros(filtros, "nomecreated")) {
            where.append("AND nomecreated = '").append(filtros.get("nomecreated")).append("' ");
        }
        if (validarFiltros(filtros, "mensagemerro")) {
            where.append("AND mensagemerro = '").append(filtros.get("mensagemerro")).append("' ");
        }     
        if (validarFiltros(filtros, "ano")) {
            where.append(" AND ano = '").append(filtros.get("ano")).append("' ");
        }
        if (validarFiltros(filtros, "semestre")) {
            where.append(" AND semestre = '").append(filtros.get("semestre")).append("' ");
        }
        if (validarFiltros(filtros, "bimestre")) {
            where.append(" AND bimestre = ").append(filtros.get("bimestre")).append(" ");
        }
        if (validarFiltros(filtros, "tipolote")) {
            where.append(" AND origem = '").append(filtros.get("tipolote").toString()).append("' ");
        }       
        if (validarFiltros(filtros, "disciplina")) {
        	where.append(" and exists ( select codigo from integracaomestregritem i where i.codigointegracao = integracaomestregr.codigo ");
        	where.append(" and exists (SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item where  (((item->>'turma')::jsonb->>'disciplina')::jsonb->>'codigo')::int = ").append(filtros.get("disciplina")).append(" limit 1)");
        	where.append(" limit 1) ");
        }       
        if (validarFiltros(filtros, "unidadeensino")) {
        	where.append(" and exists ( select codigo from integracaomestregritem i where i.codigointegracao = integracaomestregr.codigo ");
        	where.append(" and exists (SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item where (select STRING_TO_ARRAY('").append(((String)filtros.get("unidadeensino")).replace(" ", "").trim()).append("', ',')) @> array[(((item->>'aluno')::jsonb->>'polo')::jsonb->>'codigo_interno')]  limit 1)");
        	where.append(" limit 1) ");
        }       
        if (validarFiltros(filtros, "curso")) {
        	where.append(" and exists ( select codigo from integracaomestregritem i where i.codigointegracao = integracaomestregr.codigo ");
        	where.append(" and exists (SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item where (select STRING_TO_ARRAY('").append(((String)filtros.get("curso")).replace(" ", "").trim()).append("', ',')) @> array[(((item->>'aluno')::jsonb->>'curso')::jsonb->>'codigo_interno')]  limit 1)");
        	where.append(" limit 1) ");
        }       
        return where;
    }

    private Boolean whereParaItensDoLoteJSON(Map<String, Object> filtros, StringBuilder sqlStr) {
    	sqlStr.append(" where 1 = 1 ");
        Boolean filtroItens = false;

        if (validarFiltros(filtros, "ano")) {
        	filtroItens = true;
        	sqlStr.append("AND item.ano = '")
                    .append(filtros.get("ano").toString().trim())
                    .append("' ");
        }

        if (validarFiltros(filtros, "semestre")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.semestre) ILIKE upper(('%")
                    .append(filtros.get("semestre").toString().trim())
                    .append("%' ))");
        }

        if (validarFiltros(filtros, "ensino")) {
        	filtroItens = true;
        	sqlStr.append("AND (item.ensino) ILIKE upper(('%")
                    .append(filtros.get("ensino").toString().toUpperCase())
                    .append("%' ))");
        }

        if (validarFiltros(filtros, "turno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.descricao_turno) ILIKE upper(('%")
                    .append(filtros.get("turno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigocursoserie")) {
        	filtroItens = true;
        	sqlStr.append("AND item.serie->>'codigo' = '")
                    .append(filtros.get("codigocursoserie").toString().trim())
                    .append("' ");
        }

        if (validarFiltros(filtros, "nomecursoserie")) {
        	filtroItens = true;
        	sqlStr.append("AND item.serie->>'nome' = '")
                    .append(filtros.get("nomecursoserie").toString().trim().toUpperCase())
                    .append("' ");
        }

        if (validarFiltros(filtros, "chaveturma")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.turma->>'chave') ILIKE upper(('%")
                    .append(filtros.get("chaveturma").toString().trim().toUpperCase())
                    .append("%' ))");
        }

        if (validarFiltros(filtros, "codigointernoturma")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.turma->>'codigo_interno') ILIKE upper(('%")
                    .append(filtros.get("codigointernoturma").toString().trim().toUpperCase())
                    .append("%' ))");
        }

        if (validarFiltros(filtros, "codigoturma")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.turma->>'codigo' ) ILIKE upper(('%")
                    .append(filtros.get("codigoturma").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "nometurma")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.turma ->> 'nome') ILIKE upper(('%")
                    .append(filtros.get("nometurma").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigodisciplina")) {
        	filtroItens = true;
        	sqlStr.append("AND (item.turma->'disciplina'->>'codigo') = '")
                    .append(filtros.get("codigodisciplina").toString().trim().toUpperCase())
                    .append("' ");
        }

        if (validarFiltros(filtros, "sigladisciplina")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.turma->'disciplina'->>'sigla') ILIKE upper(('%")
                    .append(filtros.get("sigladisciplina").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "nomedisciplina")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.turma -> 'disciplina' ->> 'nome')) ILIKE upper(('%")
                    .append(filtros.get("nomedisciplina").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigointernoaluno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno->>'codigo_interno') ILIKE upper(('%")
                    .append(filtros.get("codigointernoaluno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigoaluno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno->>'codigo') ILIKE upper(('%")
                    .append(filtros.get("codigoaluno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "nomealuno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno->>'nome') ILIKE upper(('%")
                    .append(filtros.get("nomealuno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "emailaluno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno->>'email') ILIKE upper(('%")
                    .append(filtros.get("emailaluno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigodiasemanaaluno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno->>'codigo_dia_semana') ILIKE upper(('%")
                    .append(filtros.get("codigodiasemanaaluno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "tempoestendidoaluno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno ->> 'is_tempo_estendido') ILIKE upper(('%")
                    .append(filtros.get("tempoestendidoaluno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "numerocelularaluno")) {
        	filtroItens = true;
        	sqlStr.append("AND upper(item.aluno ->> 'numero_celular') ILIKE upper(('%")
                    .append(filtros.get("numerocelularaluno").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigointernopolo")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.aluno -> 'polo' ->> 'codigo_interno')) ILIKE upper(('%")
                    .append(filtros.get("codigointernopolo").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigopolo")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.aluno -> 'polo' ->> 'codigo')) ILIKE upper(('%")
                    .append(filtros.get("codigopolo").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "descricaopolo")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.aluno -> 'polo' ->> 'descricao')) ILIKE upper(('%")
                    .append(filtros.get("descricaopolo").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigointernocurso")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.aluno -> 'curso' ->> 'codigo_interno')) ILIKE upper(('%")
                    .append(filtros.get("codigointernocurso").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "codigocurso")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.aluno -> 'curso' ->> 'codigo')) ILIKE upper(('%")
                    .append(filtros.get("codigocurso").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        if (validarFiltros(filtros, "descricaocurso")) {
        	filtroItens = true;
        	sqlStr.append("AND upper((item.aluno -> 'curso' ->> 'descricao')) ILIKE upper(('%")
                    .append(filtros.get("descricaocurso").toString().trim().toUpperCase())
                    .append("%')) ");
        }

        return filtroItens;
    }

    private StringBuilder whereParaItensDoLoteJSONCte(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE jsonb_typeof(dadosenvio::jsonb) = 'array' ");
        if (validarFiltros(filtros, "iditem")) {
            return where.append("AND codigo = '")
                    .append(filtros.get("iditem"))
                    .append("' ");
        }
        if (validarFiltros(filtros, "codigointegracao")) {
            where.append("AND codigointegracao = '")
                    .append(filtros.get("codigointegracao").toString().trim().toUpperCase())
                    .append("' ");
        }        
        return where;
    }

    private StringBuilder whereParaItensDoLoteJSONLogLote(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE jsonb_typeof(dadosenvio::jsonb) = 'array' ");

        where.append(" and iitem.mensagemerro is not null ");
        where.append(" and trim(iitem.mensagemerro) <> '' ");
        if (validarFiltros(filtros, "created")) {
        	where.append(" and created::date >= '" + Uteis.getDataJDBC((Date)filtros.get("created")) + "' ");
        }
        if (validarFiltros(filtros, "createdFinal")) {
        	where.append(" and created::date <= '" + Uteis.getDataJDBC((Date)filtros.get("createdFinal")) + "' ");
        }
        if (validarFiltros(filtros, "codigolote")) {
            where.append("AND igr.codigolote = '")
                    .append(filtros.get("codigolote").toString().trim().toUpperCase())
                    .append("' ");
        }
        if (validarFiltros(filtros, "nomeLote")) {
        	where.append("AND igr.nomelote ilike '%")
        	.append(filtros.get("nomeLote").toString().trim().toUpperCase())
        	.append("%' ");
        }

        if (validarFiltros(filtros, "codigointegracao")) {
            where.append("AND iitem.codigointegracao = '")
                    .append(filtros.get("codigointegracao").toString().trim().toUpperCase())
                    .append("' ");
        }

        if (validarFiltros(filtros, "ano")) {
            where.append("AND EXISTS (")
                    .append("SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item ")
                    .append("WHERE item ->> 'ano' = '")
                    .append(filtros.get("ano").toString().trim())
                    .append("' ");

            if (validarFiltros(filtros, "semestre")) {
                where.append("AND upper(item ->> 'semestre') ILIKE upper('%")
                        .append(filtros.get("semestre").toString().trim())
                        .append("%') ");
            }
            where.append(") ");

        } else if (validarFiltros(filtros, "semestre")) {
            where.append("AND EXISTS (")
                    .append("SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item ")
                    .append("WHERE upper(item ->> 'semestre') ILIKE upper('%")
                    .append(filtros.get("semestre").toString().trim())
                    .append("%') ")
                    .append(") ");
        }

        if (validarFiltros(filtros, "iditem")) {
            return where.append("AND iitem.codigo = '")
                    .append(filtros.get("iditem"))
                    .append("' ");
        }

        if (validarFiltros(filtros, "origem")) {
            where.append("and igr.origem ILIKE '%" + filtros.get("origem").toString().trim() + "%' ");
        }

        if (validarFiltros(filtros, "matricula")) {
            where.append("and 1 = 0 ");
        }
        if (validarFiltros(filtros, "nomeDisciplina")) {
        	where.append("and 1 = 0 ");
        }

        return where;
    }

    private StringBuilder whereParaItensDoLoteJSONLogOperacao(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");

        if (validarFiltros(filtros, "codigolote")) {
            where.append(" AND 1 = 0 ");
        }

        where.append(" and oper.mensagemerro is not null ");
        where.append(" and trim(oper.mensagemerro) <> '' ");

        if (validarFiltros(filtros, "ano")) {
            where.append("AND oper.ano = '").append(filtros.get("ano").toString().trim())
                    .append("' ");
        }
        if (validarFiltros(filtros, "nomeLote")) {
        	where.append("AND 1 = 0 ");
        }
        if (validarFiltros(filtros, "semestre")) {
            where.append("AND oper.semestre ILIKE '")
                    .append(filtros.get("semestre").toString().trim()).append("' ");
        }

        if (validarFiltros(filtros, "bimestre")) {
            where.append("AND oper.bimestre = ")
                    .append(filtros.get("bimestre"));
        }

        if (validarFiltros(filtros, "origem")) {
            where.append("and oper.origem ILIKE '%" + filtros.get("origem").toString().trim() + "%' ");
        }

        if (validarFiltros(filtros, "matricula")) {
            where.append("and oper.matricula ilike '" + filtros.get("matricula").toString().trim() + "%' ");
        }
        if (validarFiltros(filtros, "nomeDisciplina")) {
        	where.append("and sem_acentos(disciplina.nome) ilike sem_acentos('%" + filtros.get("nomeDisciplina").toString().trim() + "%') ");
        }

        return where;
    }

    private StringBuilder whereParaItensDoLoteJSONLog(Map<String, Object> filtros) {
        StringBuilder where = new StringBuilder(" WHERE jsonb_typeof(dadosenvio::jsonb) = 'array' ");

        where.append("AND ( ");
        where.append("  CASE ");
        where.append("    WHEN iitem.mensagemerro ilike '%status%' and iitem.mensagemerro::jsonb is not null ");
        where.append("    THEN (iitem.mensagemerro::jsonb)->> 'status' ");
        where.append("    ELSE NULL ");
        where.append("  END ");
        where.append(") IS NOT NULL ");

        if (validarFiltros(filtros, "codigolote")) {
            where.append("AND iitem.codigolote = '")
                    .append(filtros.get("codigolote").toString().trim().toUpperCase())
                    .append("' ");
        }

        if (validarFiltros(filtros, "codigointegracao")) {
            where.append("AND iitem.codigointegracao = '")
                    .append(filtros.get("codigointegracao").toString().trim().toUpperCase())
                    .append("' ");
        }

        if (validarFiltros(filtros, "ano")) {
            where.append("AND EXISTS (")
                    .append("SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item ")
                    .append("WHERE item ->> 'ano' = '")
                    .append(filtros.get("ano").toString().trim())
                    .append("' ");

            if (validarFiltros(filtros, "semestre")) {
                where.append("AND upper(item ->> 'semestre') ILIKE upper('%")
                        .append(filtros.get("semestre").toString().trim())
                        .append("%') ");
            }
            where.append(") ");

        } else if (validarFiltros(filtros, "semestre")) {
            where.append("AND EXISTS (")
                    .append("SELECT 1 FROM jsonb_array_elements(dadosenvio::jsonb) AS item ")
                    .append("WHERE upper(item ->> 'semestre') ILIKE upper('%")
                    .append(filtros.get("semestre").toString().trim())
                    .append("%') ")
                    .append(") ");
        }

        if (validarFiltros(filtros, "iditem")) {
            return where.append("AND iitem.codigo = '")
                    .append(filtros.get("iditem"))
                    .append("' ");
        }

        return where;
    }

    private Boolean validarFiltros(Map<String, Object> filtros, String propriedade) {
        return filtros.containsKey(propriedade)
                && filtros.get(propriedade) != null
                && !filtros.get(propriedade).toString().trim().isEmpty()
                && !filtros.get(propriedade).toString().trim().equals("0");
    }




    public StringBuilder whereConsultarPorMatriculaAnoSemestreAtual(Map<String, Object> filtros) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("WHERE curso.nivelEducacional = 'SU' ");
        if (validarFiltros(filtros, "ano")) {
            sqlStr.append("    and matriculaperiodo.ano = ").append("'").append(filtros.get("ano")).append("' ");
        }
        if (validarFiltros(filtros, "semestre")) {
        	sqlStr.append("    and matriculaperiodo.semestre = ").append("'").append(filtros.get("semestre")).append("' ");
        }
        if(!validarFiltros(filtros, "ano") || !validarFiltros(filtros, "semestre")) {
        	sqlStr.append("    and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula order by mp.ano desc, mp.semestre desc, mp.codigo desc limit 1) ");
        }

        if (validarFiltros(filtros, "matricula")) {
            sqlStr.append("    and matricula.matricula = ").append("'").append(filtros.get("matricula")).append("' ");
        }


        if (validarFiltros(filtros, "codigopessoa")) {
            sqlStr.append("    and pessoa.codigo = ").append("'").append(filtros.get("codigopessoa")).append("' ");
        }
        return sqlStr;
    }    

    private StringBuilder whereConsultarLogIntegracaoAluno(Map<String, Object> filtros) throws Exception {
        StringBuilder sqlStr = new StringBuilder("WHERE 1=1 ");
        if (validarFiltros(filtros, "ano")) {
            sqlStr.append("and integracao.ano = '" + filtros.get("ano") + "' ");
        }

        if (validarFiltros(filtros, "semestre")) {
            sqlStr.append("and integracao.semestre = '" + filtros.get("semestre") + "' ");
        }
        if (validarFiltros(filtros, "created")) {
        	sqlStr.append("and integracao.created::date >= '" + Uteis.getDataJDBC((Date)filtros.get("created")) + "' ");
        }
        if (validarFiltros(filtros, "createdFinal")) {
        	sqlStr.append("and integracao.created::date <= '" + Uteis.getDataJDBC((Date)filtros.get("createdFinal")) + "' ");
        }

        if (validarFiltros(filtros, "bimestre")) {
            sqlStr.append("and ((integracao.bimestre in (" + filtros.get("bimestre") + ") and  integracao.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA')) or integracao.origem not in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') )");
        }
        if (validarFiltros(filtros, "nivelEducacional")) {
        	sqlStr.append("and ((curso.nivelEducacional = '" + ((TipoNivelEducacional) filtros.get("nivelEducacional")).getValor() + "' and  integracao.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA')  or integracao.origem not in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') ) ");
        }

        if (validarFiltros(filtros, "origem")) {
            sqlStr.append("and sem_acentos(integracao.origem) ILIKE sem_acentos('%" + filtros.get("origem").toString().trim() + "%') ");
        }

        if (validarFiltros(filtros, "nomealuno")) {
            sqlStr.append("and sem_acentos(dadosenvio::jsonb ->> 'nome') ILIKE sem_acentos('%" + filtros.get("nomealuno").toString().trim() + "%') ");
        }
        if (validarFiltros(filtros, "email")) {
        	sqlStr.append("and dadosenvio::jsonb ->> 'email' ILIKE '%" + filtros.get("email").toString().trim() + "%' ");
        }

        if (validarFiltros(filtros, "matricula")) {
            sqlStr.append("and integracao.matricula ILIKE '%" + filtros.get("matricula").toString().trim() + "%' ");
        }
        if (validarFiltros(filtros, "codigoAluno")) {
        	sqlStr.append("and dadosenvio::jsonb ->> 'codigo' ILIKE '%" + filtros.get("codigoAluno").toString().trim() + "%' ");
        }
        if (validarFiltros(filtros, "mensagemErro")) {
        	sqlStr.append("and mensagemErro ILIKE '%" + filtros.get("mensagemErro").toString().trim() + "%' ");
        }
        if (validarFiltros(filtros, "situacao")) {        	
        	sqlStr.append(" and (case when integracao.processado and coalesce(integracao.mensagemerro, '') != '' then 'Erro' else case when integracao.processado then 'Sucesso' else 'Aguardando' end end) ILIKE '%" + filtros.get("situacao").toString().trim() + "%' ");
        }

        if (validarFiltros(filtros, "codigosdisciplinas")) {
            sqlStr.append(" AND ( (integracao.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') and EXISTS (");
            sqlStr.append("     SELECT 1");
            sqlStr.append("     FROM jsonb_array_elements(");
            sqlStr.append("         CASE ");
            sqlStr.append("             WHEN jsonb_typeof(integracao.dadosenvio::jsonb -> 'turmas') = 'array' ");
            sqlStr.append("             THEN integracao.dadosenvio::jsonb -> 'turmas' ");
            sqlStr.append("             ELSE '[]'::jsonb ");
            sqlStr.append("         END");
            sqlStr.append("     ) AS turma");
            sqlStr.append("     WHERE turma ->> 'codigo_interno' in (" + montarStringParaIn(filtros.get("codigosdisciplinas").toString().trim()) + ") ");
            sqlStr.append(" )) or integracao.origem not in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA')) ");
        }

        if (validarFiltros(filtros, "disciplina")) {
            sqlStr.append(" AND ( (integracao.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') and  EXISTS (");
            sqlStr.append("     SELECT 1");
            sqlStr.append("     FROM jsonb_array_elements(");
            sqlStr.append("         CASE ");
            sqlStr.append("             WHEN jsonb_typeof(integracao.dadosenvio::jsonb -> 'turmas') = 'array' ");
            sqlStr.append("             THEN integracao.dadosenvio::jsonb -> 'turmas' ");
            sqlStr.append("             ELSE '[]'::jsonb ");
            sqlStr.append("         END");
            sqlStr.append("     ) AS turma");
            sqlStr.append("     WHERE turma ->> 'codigo_interno' ILIKE '%" + filtros.get("disciplina").toString().trim() + "%' ");
            sqlStr.append(" ) ) or integracao.origem not in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA')) ");
        }

        if (validarFiltros(filtros, "unidadeensino")) {
            sqlStr.append("AND ( (integracao.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') and (dadosenvio::jsonb -> 'polo' ->> 'codigo_interno')::int in (" + filtros.get("unidadeensino") + ")) or integracao.origem not in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') ) ");
        }

        if (validarFiltros(filtros, "curso")) {
            sqlStr.append("AND ( (integracao.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') and (dadosenvio::jsonb -> 'curso' ->> 'codigo_interno')::int in (" + filtros.get("curso") + ")) or integracao.origem not in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') )");
        }

        return sqlStr;
    }

    public StringBuilder whereConsultarLogIntegracaoAlunoCTE(Map<String, Object> filtros) {
        StringBuilder sqlStr = new StringBuilder("WHERE 1=1 ");
        if (validarFiltros(filtros, "chaveTurma")) {
            sqlStr.append("AND ((sub.origem in ('EXCLUSAO_DISCIPLINA', 'EXCLUSÃO DISCIPLINA', 'INCLUSÃO DISCIPLINA', 'INCLUSAO_DISCIPLINA') and upper(sub.chaveTurma) ILIKE upper('%" + filtros.get("chaveTurma").toString().trim() + "%'))) ");
        }
        return sqlStr;
    }

    private StringBuilder whereConsultarPorAlunoDisciplinaIntegradas(Map<String, Object> filtros) {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("WHERE 1=1");
        if (validarFiltros(filtros, "matricula")) {
            sqlStr.append("     and matricula = ").append("'").append(filtros.get("matricula")).append("' ");
        }
        if (validarFiltros(filtros, "origens")) {
            sqlStr.append(" and origem in (").append(filtros.get("origens").toString()).append(") ");
        }
        if (validarFiltros(filtros, "ano")) {
            sqlStr.append("      and ano = ").append("'").append(filtros.get("ano")).append("' ");
        }
        if (validarFiltros(filtros, "semestre")) {
            sqlStr.append("      and semestre = ").append("'").append(filtros.get("semestre")).append("' ");
        }
        if (validarFiltros(filtros, "bimestre")) {
            sqlStr.append("      and bimestre = ").append("").append(filtros.get("bimestre")).append(" ");
        }
        return sqlStr;
    }

    /**
     * Responsavel por Montar o nome dos lotes com base em seus filtros e horario
     *
     * @param tipoLote
     * @param ano
     * @param semestre
     * @param bimestre
     * @return
     */
    @Override
    public String montarNomeLote(OperacaoTempoRealMestreGREnum tipoLote, Integer ano, Integer semestre, Integer bimestre) {
        String bimestreFormatado = (bimestre == 3) ? "B1B2" : "B" + bimestre;
        switch (tipoLote) {
            case TURMA:
                return "TURMA_" + "A" + ano + "_S" + semestre + bimestreFormatado + "_"
                        + dataHoraFormatada("yyyy_MM_dd_HH_mm");
            case TURMA_2CH:
                return "TURMA_" + "2CH_" + "A" + ano + "_S" + semestre + bimestreFormatado + "_"
                        + dataHoraFormatada("yyyy_MM_dd_HH_mm");
            case TURMA_EXAME:
                return "TURMA_" + "EX_" + "A" + ano + "_S" + semestre + bimestreFormatado + "_"
                        + dataHoraFormatada("yyyy_MM_dd_HH_mm");           
            default:
                throw new IllegalArgumentException("Erro ao Gerar o Nome Do Lote");
        }
    }

    /**
     * Em consultas por parte do nome do lote esse
     * metodo adiciona o respectivo valor de nome no ILIKE da query na condicao where
     *
     * @param filtros
     * @return
     */
    public String nomeLoteConsulta(Map<String, Object> filtros) {
        switch (filtros.get("tipolote").toString()) {
//			TURMA
            case "lote_turma":
                return "TURMA_A";
            case "lote_turma_segunda_chamada":
                return "TURMA_2CH";
            case "lote_turma_exame":
                return "TURMA_EX";
//			DISCIPLINA
            case "lote_disciplina":
                return "DISCIPLINA_A";
            case "lote_disciplina_segunda_chamada":
                return "DISCIPLINA_2CH";
            case "lote_disciplina_exame":
                return "DISCIPLINA_EX";
//			ALUNO
            case "lote_aluno":
                return "ALUNO_A";
            case "lote_aluno_segunda_chamada":
                return "ALUNO_2CH";
            case "lote_aluno_exame":
                return "ALUNO_EX";
            default:
                throw new IllegalArgumentException("Nome do Lote Não Mapeado.");
        }
    }

    /**
     * Retorna a data hora atual do sistema pasando a string de como deve ser fomatado o retorno
     *
     * @param tipoDataHora
     * @return
     */
    private String dataHoraFormatada(String tipoDataHora) {
        Calendar dataAtual = Calendar.getInstance();
        Date dataHora = getDateTime(null,
                dataAtual.get(Calendar.HOUR_OF_DAY),
                dataAtual.get(Calendar.MINUTE),
                dataAtual.get(Calendar.SECOND));
        SimpleDateFormat formatada = new SimpleDateFormat(tipoDataHora);
        return formatada.format(dataHora);
    }

    /**
     * Regra de quando for 1 semestre e for 1 bimestre adicionar o ciclo 1B
     * Regra de quando for 1 semestre e for 2 bimestre adicionar o ciclo 2B
     * Regra de quando for 2 semestre e for 1 bimestre adicionar o ciclo 3B
     * Regra de quando for 2 semestre e for 2 bimestre adicionar o ciclo 4B
     *
     * @param semestre
     * @param bimestre
     */
    private String regraCiclo(Integer semestre, Integer bimestre) {
        if ((1 == semestre) && (1 == bimestre)) {
            return "1B";
        }
        if ((1 == semestre) && (2 == bimestre)) {
            return "2B";
        }
        if ((2 == semestre) && (1 == bimestre)) {
            return "3B";
        }
        if ((2 == semestre) && (2 == bimestre)) {
            return "4B";
        }
        throw new IllegalArgumentException("Os Parametros Passados não Existem na Regra");
    }

    /**
     * Responsavel por retornar a query SELECT
     * que vai extrair o json com base em sua origem
     *
     * @param origem
     * @return
     * @throws Exception
     */
    private StringBuilder selectDinamicoIntegracaoItensJSON() throws Exception {
        StringBuilder select = new StringBuilder();
        select.append("SELECT ")
                .append("item.lote as codigo, ")
                .append("item.ano as ano, ")
                .append("item.semestre as semestre, ")
                .append("item.ensino as ensino, ")
                .append("item.descricao_turno as turno, ")
                .append("item.serie->>'codigo' AS codigocursoserie, ")
                .append("item.serie->>'nome'   AS nomecursoserie, ")
                .append("item.turma->>'chave'  AS chaveturma, ")
                .append("item.turma->>'codigo' AS codigoturma, ")
                .append("item.turma->>'codigo_interno' AS codigointernoturma, ")
                .append("count(*) OVER() AS qtde_total_registros, ")
                .append("item.turma->>'nome'   AS nometurma ")
        		.append(", item.turma->'disciplina'->>'codigo' AS codigodisciplina, ")
                        .append("item.turma->'disciplina'->>'sigla'    AS sigladisciplina, ")
                        .append("item.turma->'disciplina'->>'nome'     AS nomedisciplina, ")
                        .append("item.aluno->>'codigo_interno'  AS codigointernoaluno, ")
                        .append("item.aluno->>'codigo'           AS codigoaluno, ")
                        .append("item.aluno->>'nome'             AS nomealuno, ")
                        .append("item.aluno->>'email'            AS emailaluno, ")
                        .append("item.aluno->>'codigo_dia_semana'AS codigodiasemana, ")
                        .append("item.aluno->>'is_tempo_estendido' AS tempoestendido, ")
                        .append("item.aluno->>'numero_celular'   AS numerocelularaluno, ")
                        .append("item.aluno->'polo'->>'codigo_interno' AS codigointernopolo, ")
                        .append("item.aluno->'polo'->>'codigo'        AS codigopolo, ")
                        .append("item.aluno->'polo'->>'descricao'     AS descricaopolo, ")
                        .append("item.aluno->'curso'->>'codigo_interno' AS codigointernocurso, ")
                        .append("item.aluno->'curso'->>'codigo'        AS codigocurso, ")
                        .append("item.aluno->'curso'->>'descricao'     AS descricaocurso ");                      
                return select;
        
    }



    private StringBuilder joinsAlunoFromMatricula() {
        StringBuilder joins = new StringBuilder();
        joins
                .append("INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ")
                .append("INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ")
                .append("INNER JOIN curso ON ")
                .append("    curso.codigo = matricula.curso ")
                .append("INNER JOIN configuracaoldap ON ")
                .append("    curso.configuracaoldap = configuracaoldap.codigo ")
                .append("INNER JOIN pessoaemailinstitucional ON ")
                .append("    pessoaemailinstitucional.pessoa = pessoa.codigo ")
                .append("    AND pessoaemailinstitucional.statusativoinativoenum = 'ATIVO' ")
                .append("    AND pessoaemailinstitucional.email ilike '%'||configuracaoldap.dominio||'%' ")
                .append("    AND pessoaemailinstitucional.codigo = ( ")
                .append("        SELECT ")
                .append("            pei.codigo ")
                .append("        FROM ")
                .append("            pessoaemailinstitucional pei ")
                .append("        WHERE ")
                .append("            pei.pessoa = pessoa.codigo ")
                .append("            and pei.email ilike '%'||configuracaoldap.dominio||'%' ")
                .append("            AND pei.statusativoinativoenum = 'ATIVO' ")
                .append("        ORDER BY ")
                .append("            pei.codigo DESC ")
                .append("        LIMIT 1) ")
                .append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ")
                .append("INNER JOIN historico ON historico.matricula = matricula.matricula AND historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ")
                .append("INNER JOIN disciplina ON disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ")                
                .append("INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = matricula.unidadeEnsino ")
                .append("LEFT JOIN gradedisciplina ON gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina ")
                .append("LEFT JOIN gradecurriculargrupooptativadisciplina ON gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina ");

        return joins;
    }

    private StringBuilder joinsAlunoFromRequerimento() {
        StringBuilder joins = new StringBuilder();
        joins
                .append(" INNER JOIN tipoRequerimento ON tipoRequerimento.codigo = requerimento.tipoRequerimento ")
                .append(" INNER JOIN pessoa ON pessoa.codigo = requerimento.pessoa ")
                .append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = requerimento.unidadeEnsino ")
                .append(" INNER JOIN matricula ON matricula.matricula = requerimento.matricula ")
                .append(" INNER JOIN pessoa aluno ON aluno.codigo = matricula.aluno ")
                .append(" INNER JOIN matriculaperiodo ON matricula.matricula = matriculaperiodo.matricula ")
                .append(" AND CASE ")
                .append(" WHEN requerimento.matriculaperiodo IS NOT NULL THEN requerimento.matriculaperiodo = matriculaperiodo.codigo ")
                .append(" ELSE matriculaperiodo.codigo = ( ")
                .append(" SELECT codigo FROM matriculaperiodo mp ")
                .append(" WHERE matriculaperiodo.matricula = mp.matricula ")
                .append(" AND mp.data <= requerimento.data ")
                .append(" ORDER BY mp.codigo DESC ")
                .append(" LIMIT 1 ) ")
                .append(" END ")
                .append(" LEFT JOIN periodoletivo AS periodoletivomatriculaperiodo ON matriculaperiodo.periodoletivomatricula = periodoletivomatriculaperiodo.codigo ")
                .append("INNER JOIN curso ON ")
                .append("    curso.codigo = matricula.curso ")
                .append("INNER JOIN configuracaoldap ON ")
                .append("    curso.configuracaoldap = configuracaoldap.codigo ")
                .append("INNER JOIN pessoaemailinstitucional ON ")
                .append("    pessoaemailinstitucional.pessoa = pessoa.codigo ")
                .append("    AND pessoaemailinstitucional.statusativoinativoenum = 'ATIVO' ")
                .append("    AND pessoaemailinstitucional.email ilike '%'||configuracaoldap.dominio||'%' ")
                .append("    AND pessoaemailinstitucional.codigo = ( ")
                .append("        SELECT ")
                .append("            pei.codigo ")
                .append("        FROM ")
                .append("            pessoaemailinstitucional pei ")
                .append("        WHERE ")
                .append("            pei.pessoa = pessoa.codigo ")
                .append("            and pei.email ilike '%'||configuracaoldap.dominio||'%' ")
                .append("            AND pei.statusativoinativoenum = 'ATIVO' ")
                .append("        ORDER BY ")
                .append("            pei.codigo DESC ")
                .append("        LIMIT 1) ")
                .append(" INNER JOIN requerimentodisciplina ON requerimentodisciplina.requerimento = requerimento.codigo ")
                .append(" AND requerimentodisciplina.situacao = 'DEFERIDO' ")
                .append(" INNER JOIN disciplina ON disciplina.codigo = requerimentodisciplina.disciplina ")
                .append(" INNER JOIN historico ON historico.matricula = matricula.matricula ")
                .append(" AND historico.disciplina = disciplina.codigo ")
                .append(" AND historico.matrizcurricular = matricula.gradecurricularatual ")
                .append(" AND historico.codigo = ( ")
                .append(" SELECT h1.codigo FROM historico h1 ")
                .append(" WHERE h1.matricula = matricula.matricula ")
                .append(" AND h1.disciplina = disciplina.codigo ")
                .append(" AND h1.matrizcurricular = matricula.gradecurricularatual ")
                .append(" AND (h1.anohistorico || h1.semestrehistorico) <= (matriculaperiodo.ano || matriculaperiodo.semestre) ")
                .append(" ORDER BY h1.anohistorico DESC, h1.semestrehistorico DESC, h1.codigo ")
                .append(" LIMIT 1 ) ")
                .append(" INNER JOIN periodoletivo ON periodoletivo.codigo = historico.periodoletivomatrizcurricular ")
                .append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ")
                .append(" LEFT JOIN gradedisciplina ON gradedisciplina.codigo = historico.gradedisciplina ")
                .append(" LEFT JOIN gradecurriculargrupooptativadisciplina ON gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");

        return joins;
    }

  

    @Override
    public File realizarGeracaoExcelLote(Map<String, Object> filtro, List<IntegracaoMestreGRVO> integracoesCargaIncial, DataModelo dataModelo, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
        // 1) Use SXSSFWorkbook para streaming (mantém em memória só as últimas 100 linhas)
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(), 100);
        Sheet sheet = workbook.createSheet("integracao-mestre-gr-lote");

     // 2) Criação única de estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();

        
        // headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // <-- Antigo
        headerFont.setBold(true); // <-- Correto/Moderno

        headerStyle.setFont(headerFont);

        
        // headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo (e errado)
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Correto/Moderno



        CellStyle numericStyle = workbook.createCellStyle();

        
        // numericStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
        numericStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Correto/Moderno

       
        // numericStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
        numericStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Correto/Moderno

        short fmt = workbook.createDataFormat().getFormat("#");
        numericStyle.setDataFormat(fmt);

        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        numericStyle.setAlignment(HorizontalAlignment.CENTER);
        numericStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int[] columnWidths = {
                4000, 4000, 4000, 6000, 6000,
                6000, 8000, 4000, 8000, 6000,
                6000, 8000, 10000, 4000, 4000,
                4000, 6000, 6000, 10000, 6000,
                6000, 10000
        };

        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i]);
        }

        int rowNum = createHeaderRow(sheet, headerStyle);

        rowNum = appendDataRowsExcelLote(sheet, integracoesCargaIncial, rowNum, numericStyle);
        progressBarVO.incrementar();

        int totalPaginas = dataModelo.getTotalPaginas();
        for (int p = 2; p <= totalPaginas; p++) {
            dataModelo.setPaginaAtual(p);
            dataModelo.setPage(p);

            List<IntegracaoMestreGRVO> pageData =
                    consultarIntegracaoDadosEnvioJson(filtro, dataModelo, false, usuario);

            rowNum = appendDataRowsExcelLote(sheet, pageData, rowNum, numericStyle);
            progressBarVO.incrementar();
        }

        File arquivo = new File(
                getCaminhoPastaWeb()
                        + File.separator + "relatorio"
                        + File.separator
                        + new Date().getTime()
                        + ".xlsx"
        );
        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            workbook.write(out);
        }
        workbook.dispose();
        return arquivo;
    }

    @Override
    public File realizarGeracaoExcelLog(Map<String, Object> filtro, List<IntegracaoMestreGRVO> integracoesCargaIncial, DataModelo dataModelo, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
        // 1) Use SXSSFWorkbook para streaming (mantém em memória só as últimas 100 linhas)
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(), 100);
        Sheet sheet = workbook.createSheet("integracao-mestre-gr-lote");

     // 2) Criação única de estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();

        // MUDANÇA 1: "setBoldweight" foi depreciado
        // headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // <-- Antigo
        headerFont.setBold(true); // <-- Correto/Moderno

        headerStyle.setFont(headerFont);

        // MUDANÇA 2: Constante HSSF substituída por Enum
        // headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo (e errado)
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Correto/Moderno



        CellStyle numericStyle = workbook.createCellStyle();

        // MUDANÇA 3: Constante HSSF substituída por Enum
        // numericStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
        numericStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Correto/Moderno

        // MUDANÇA 4: Constante HSSF substituída por Enum
        // numericStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
        numericStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Correto/Moderno

        short fmt = workbook.createDataFormat().getFormat("#");
        numericStyle.setDataFormat(fmt);
        
        
        
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        numericStyle.setAlignment(HorizontalAlignment.CENTER);
        numericStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int[] columnWidths = {
                4000, 4000, 4000, 6000, 6000,
                6000, 8000, 4000, 8000, 6000,
                6000, 8000, 10000, 4000, 4000,
                4000, 6000, 6000, 10000, 6000,
                6000, 10000
        };

        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i]);
        }

        int rowNum = createHeaderRowLog(sheet, headerStyle);

        rowNum = appendDataRowsExcelLog(sheet, integracoesCargaIncial, rowNum, numericStyle);
        progressBarVO.incrementar();

        int totalPaginas = dataModelo.getTotalPaginas();
        for (int p = 2; p <= totalPaginas; p++) {
            dataModelo.setPaginaAtual(p);
            dataModelo.setPage(p);

            List<IntegracaoMestreGRVO> pageData =
            		consultarLogIntegracaoAluno(filtro, dataModelo, false, usuario);

            rowNum = appendDataRowsExcelLog(sheet, pageData, rowNum, numericStyle);
            progressBarVO.incrementar();
        }

        File arquivo = new File(
                getCaminhoPastaWeb()
                        + File.separator + "relatorio"
                        + File.separator
                        + new Date().getTime()
                        + ".xlsx"
        );
        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            workbook.write(out);
        }
        workbook.dispose();
        return arquivo;
    }

    @Override
    public File realizarGeracaoExcelLogErro(Map<String, Object> filtro, List<IntegracaoMestreGRVO> integracoesCargaIncial, DataModelo dataModelo, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
        // 1) Use SXSSFWorkbook para streaming (mantém em memória só as últimas 100 linhas)
        SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(), 100);
        Sheet sheet = workbook.createSheet("integracao-mestre-gr-lote");

     // 2) Criação única de estilos
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();

        // MUDANÇA 1: "setBoldweight" foi depreciado
        // headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // <-- Antigo
        headerFont.setBold(true); // <-- Correto/Moderno

        headerStyle.setFont(headerFont);

        // MUDANÇA 2: Constante HSSF substituída por Enum
        // headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo (e errado)
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Correto/Moderno



        CellStyle numericStyle = workbook.createCellStyle();

        // MUDANÇA 3: Constante HSSF substituída por Enum
        // numericStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
        numericStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Correto/Moderno

        // MUDANÇA 4: Constante HSSF substituída por Enum
        // numericStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
        numericStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Correto/Moderno

        short fmt = workbook.createDataFormat().getFormat("#");
        numericStyle.setDataFormat(fmt);
        
        
        
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        numericStyle.setAlignment(HorizontalAlignment.CENTER);
        numericStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int[] columnWidths = {
                4000, 4000, 4000, 6000, 6000,
                6000, 8000, 4000, 8000, 6000,
                6000, 8000, 10000, 4000, 4000,
                4000, 6000, 6000, 10000, 6000,
                6000, 10000
        };

        for (int i = 0; i < columnWidths.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i]);
        }

        int rowNum = createHeaderRowLogErro(sheet, headerStyle);

        rowNum = appendDataRowsExcelLogErro(sheet, integracoesCargaIncial, rowNum, numericStyle);
        progressBarVO.incrementar();

        int totalPaginas = dataModelo.getTotalPaginas();
        for (int p = 2; p <= totalPaginas; p++) {
            dataModelo.setPaginaAtual(p);
            dataModelo.setPage(p);

            List<IntegracaoMestreGRVO> pageData =
            		consultarErroIntegracaoJson(filtro, dataModelo, false, usuario);

            rowNum = appendDataRowsExcelLogErro(sheet, pageData, rowNum, numericStyle);
            progressBarVO.incrementar();
        }

        File arquivo = new File(
                getCaminhoPastaWeb()
                        + File.separator + "relatorio"
                        + File.separator
                        + new Date().getTime()
                        + ".xlsx"
        );
        try (FileOutputStream out = new FileOutputStream(arquivo)) {
            workbook.write(out);
        }
        workbook.dispose();
        return arquivo;
    }

    private int createHeaderRow(Sheet sheet, CellStyle headerStyle) {
        String[] headers = {
                "Ano", "Ensino", "Turno", "Turma Chave", "Turma Código",
                "Turma Código Interno", "Turma Nome", "Sigla Disciplina", "Nome Disciplina",
                "Código Interno Aluno", "Código Aluno", "Nome Aluno", "Email Aluno",
                "Código Dia Semana Aluno", "Tempo Estendido Aluno", "Número Celular",
                "Código Interno Polo", "Código Polo", "Descrição Polo",
                "Código Interno Curso", "Código Curso", "Descrição Curso"
        };

        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return 1;
    }

    private int createHeaderRowLog(Sheet sheet, CellStyle headerStyle) {
        String[] headers = {
                "ID", "Origem", "Data", "Usuário", "RA", "Matrícula",
                "Aluno", "E-mail", "Celular", "Dia Semana", "Tempo Estendido", "Ano", "Semestre", "Bimestre", "Chave Turma",
                "Situação", "Erro", "Json Envio", "Json Retorno"
        };

        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return 1;
    }

    private int createHeaderRowLogErro(Sheet sheet, CellStyle headerStyle) {
        String[] headers = {
                "Código Lote", "Nome Lote", "Origem", "Data", "Usuário", "Status", "Observações", "Dados Envio", "Dados Retorno"
        };

        Row row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        return 1;
    }

    private int appendDataRowsExcelLote(Sheet sheet, List<IntegracaoMestreGRVO> lista, int startRowNum, CellStyle numericStyle) {
        Workbook wb = sheet.getWorkbook();
       
     // 1) Ajusta o estilo numérico: centraliza e borda fina
     // numericStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
     numericStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Moderno

     // numericStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
     numericStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Moderno

     // numericStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderTop(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderBottom(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderLeft(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderRight(BorderStyle.THIN); // <-- Moderno


     // 2) Cria estilo de texto: centraliza e borda fina
     CellStyle textStyle = wb.createCellStyle();

     // textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
     textStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Moderno

     // textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
     textStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Moderno

     // textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderTop(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderBottom(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderLeft(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderRight(BorderStyle.THIN); // <-- Moderno

        for (IntegracaoMestreGRVO integracao : lista) {
            Row row = sheet.createRow(startRowNum++);
            int c = 0;

            Cell cell;

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getAno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getEnsino());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getTurno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getChaveTurma());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoTurma());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoInternoTurma());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeTurma());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getSiglaDisciplina());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeDisciplina());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoInternoAluno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoAluno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeAluno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getEmailAluno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoDiaSemanaAluno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getTempoEstendidoAluno());
            cell.setCellStyle(textStyle);

            // célula numérica com estilo centralizado
            Cell numCell = row.createCell(c++);
            numCell.setCellValue(integracao.getNumeroCelularAluno());
            numCell.setCellStyle(numericStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoInternoPolo());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoPolo());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDescricaoPolo());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoInternoCurso());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoCurso());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDescricaoCurso());
            cell.setCellStyle(textStyle);
        }

        return startRowNum;
    }

    private int appendDataRowsExcelLog(Sheet sheet, List<IntegracaoMestreGRVO> lista, int startRowNum, CellStyle numericStyle) {
        Workbook wb = sheet.getWorkbook();
     // 1) Ajusta o estilo numérico: centraliza e borda fina
     // numericStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
     numericStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Moderno

     // numericStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
     numericStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Moderno

     // numericStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderTop(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderBottom(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderLeft(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderRight(BorderStyle.THIN); // <-- Moderno


     // 2) Cria estilo de texto: centraliza e borda fina
     CellStyle textStyle = wb.createCellStyle();

     // textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
     textStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Moderno

     // textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
     textStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Moderno

     // textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderTop(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderBottom(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderLeft(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderRight(BorderStyle.THIN); // <-- Moderno

        for (IntegracaoMestreGRVO integracao : lista) {
            Row row = sheet.createRow(startRowNum++);
            int c = 0;

            Cell cell;
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigo());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getOrigem().getValorApresentar());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCreated_apresentar());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeCreated());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoAluno());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getMatricula());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeAluno());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getEmailAluno());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNumeroCelularAluno());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoDiaSemanaAluno_apresentar());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getTempoEstendidoAluno());
            cell.setCellStyle(textStyle);
           

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getAno());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getSemestre());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getBimestre());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getChaveTurma());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getStatus());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getMensagemErro());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDadosEnvio());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDadosRetorno());
            cell.setCellStyle(textStyle);
        }
        return startRowNum;
    }

    private int appendDataRowsExcelLogErro(Sheet sheet, List<IntegracaoMestreGRVO> lista, int startRowNum, CellStyle numericStyle) {
        Workbook wb = sheet.getWorkbook();
     // 1) Ajusta o estilo numérico: centraliza e borda fina
     // numericStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
     numericStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Moderno

     // numericStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
     numericStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Moderno

     // numericStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderTop(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderBottom(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderLeft(BorderStyle.THIN); // <-- Moderno

     // numericStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     numericStyle.setBorderRight(BorderStyle.THIN); // <-- Moderno


     // 2) Cria estilo de texto: centraliza e borda fina
     CellStyle textStyle = wb.createCellStyle();

     // textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // <-- Antigo
     textStyle.setAlignment(HorizontalAlignment.CENTER); // <-- Moderno

     // textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // <-- Antigo
     textStyle.setVerticalAlignment(VerticalAlignment.CENTER); // <-- Moderno

     // textStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderTop(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderBottom(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderLeft(BorderStyle.THIN); // <-- Moderno

     // textStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // <-- Antigo
     textStyle.setBorderRight(BorderStyle.THIN); // <-- Moderno

        for (IntegracaoMestreGRVO integracao : lista) {
            Row row = sheet.createRow(startRowNum++);
            int c = 0;

            Cell cell;

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCodigoLote());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeLote());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getOrigem().getValorApresentar());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getCreated_apresentar());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getNomeCreated());
            cell.setCellStyle(textStyle);


            cell = row.createCell(c++);
            cell.setCellValue(integracao.getStatusJson());
            cell.setCellStyle(textStyle);

            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDataJson());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDadosEnvio());
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(c++);
            cell.setCellValue(integracao.getDadosRetorno());
            cell.setCellStyle(textStyle);

        }
        return startRowNum;
    }

    private List<OperacaoTempoRealMestreGRVO> consultarOperacaoPendente() {
        List<OperacaoTempoRealMestreGRVO> listaConsulta = null;
        try {
            String sqlStr = "select codigo, origem, matricula, ano, semestre, bimestre, dadosenvio, processado " +
                    "from integracaomestregroperacoes " +
                    "where processado = false " +
                    "AND created >= TIMESTAMP '2025-08-01 00:00:00' " +
                    "limit " + limitOperacaoTempoReal;

            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            tabelaResultado.beforeFirst();
            listaConsulta = montarDadosResultadoOperacoes(tabelaResultado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaConsulta;
    }

    private Boolean isIntegradoRegras(String ano, String semestre, Integer codigoDisciplina) {
        Boolean valido = Boolean.FALSE;
        StringBuilder sql = new StringBuilder("");
        sql.append("      select exists (select 1 from integracaomestregritem imt  inner join  integracaomestregr on integracaomestregr.codigo = imt.codigointegracao where ");
        sql.append("       integracaomestregr.ano = '").append(ano).append("'");
        sql.append("       and integracaomestregr.semestre = '").append(semestre).append("'");
        sql.append("       and  exists (");
        sql.append("         	select 1 from jsonb_array_elements(imt.dadosenvio::jsonb) as item");
        sql.append("    				where item->>'ano'= '").append(ano).append("'");
        sql.append("    				and   item->>'semestre'='").append(semestre).append("SEM'");
        sql.append("    				and   item->'turma'->'disciplina'->>'codigo' = '").append(codigoDisciplina).append("'");
        sql.append("    				limit 1");
        sql.append(" 	        )");
        sql.append(" 	        limit 1");
        sql.append("         ) as existe");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (tabelaResultado.next()) {
            valido = tabelaResultado.getBoolean("existe");
        }
        return valido;
    }

    private Boolean isValidoOperacaoTempoReal(List<IntegracaoMestreGRVO> aluno,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String matricula) throws Exception {
        return !aluno.isEmpty()
        		&& existeAlunoMestreGR(aluno.get(0).getCodigoAluno(), configuracaoGeralSistemaVO)        		
                && matricula != null;
    }
    
    private Boolean isValidoOperacaoInativacaoTempoReal(List<IntegracaoMestreGRVO> aluno,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String matricula) throws Exception {
        return !aluno.isEmpty()
        		&& existeAlunoMestreGR(aluno.get(0).getCodigoAluno(), configuracaoGeralSistemaVO)
        		&& !getFacadeFactory().getMatriculaFacade().verificarPossuiMatriculaAtivaPessoa(aluno.get(0).getCodigoInternoAluno(), false, null)
                && matricula != null;
    }


    /**
     * Para todas as operações envolvendo uma disciplina do aluno,
     * deve ser considerado apenas as disciplinas da matricula periodo atual do aluno
     *
     * @param aluno
     * @param matricula
     * @param dataAtual
     * @param jsonValidacao
     * @return
     */
    private Boolean isValidoOperacaoTempoRealDisciplina(List<IntegracaoMestreGRVO> aluno, String matricula, Date dataAtual, String ano, String semestre, Integer codigoDisciplina) {
        String anoAtualFormatado = getAnoFormatado(dataAtual);
        return !aluno.isEmpty()
                && matricula != null
                && anoAtualFormatado.equals(ano)
                && isIntegradoRegras(ano, semestre, codigoDisciplina);
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRES_NEW)
    public void alterarPosProcessamento(final OperacaoTempoRealMestreGRVO operacao, final UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE integracaomestregroperacoes SET processado = ? ");

        boolean atualizaDadosRetorno = Uteis.isAtributoPreenchido(operacao.getDadosRetorno());
        if (atualizaDadosRetorno) {
            sql.append(", dadosretorno = ?::jsonb ");
        }

        boolean atualizaMensagemErro = Uteis.isAtributoPreenchido(operacao.getMensagemErro());
        if (atualizaMensagemErro) {
            sql.append(", mensagemerro = ? ");
        }

        sql.append(" WHERE (codigo = ?) ");

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement ps = conn.prepareStatement(sql.toString());
                int idx = 1;

                Boolean processado = operacao.getProcessado();
                ps.setBoolean(idx++, processado != null && processado);

                if (atualizaDadosRetorno) {
                    ps.setString(idx++, operacao.getDadosRetorno());
                }
                if (atualizaMensagemErro) {
                    ps.setString(idx++, operacao.getMensagemErro());
                }

                ps.setObject(idx++, operacao.getCodigo());

                return ps;
            }
        });
    }

//    public void validarOperacoesParaProcessar(RegistroExecucaoJobVO registroExecucaoJobVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) {
//        if (configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas() && configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR()) {
//            List<OperacaoTempoRealMestreGRVO> operacoesPendentes = consultarOperacaoPendente();            
//            if (Uteis.isAtributoPreenchido(operacoesPendentes)) {
//                RateLimiter limiter = RateLimiter.create(permitsPerSecond);
//                for (OperacaoTempoRealMestreGRVO operacao : operacoesPendentes) {
//                    limiter.acquire();
//                    integrarOperacao(operacao, configuracaoGeralSistemaVO, usuarioVO);
//                    registroExecucaoJobVO.setTotal(registroExecucaoJobVO.getTotal()+ 1);
//                    if(operacao.getProcessado() &&operacao.getMensagemErro().trim().isEmpty()) {                    	
//                    	registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso()+ 1);
//                    }
//                    if(operacao.getProcessado() && !operacao.getMensagemErro().trim().isEmpty()) {
//                    	registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro()+ 1);
//                    }
//                }
//            }
//        }
//    }

    private void integrarOperacao(OperacaoTempoRealMestreGRVO operacao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) {
        try {
            switch (operacao.getOperacaoTempoRealMestreGREnum()) {

                case ABANDONO_CURSO:
                    inativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case ESTORNO_ABANDONO_CURSO:
                	reativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                	break;
                case TRANCAMENTO:
                	inativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                	break;
                case ESTORNO_TRANCAMENTO:
                	reativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                	break;
                case ESTORNO_JUBILAMENTO:
                	reativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                	break;
                case JUBILAMENTO:
                    inativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;                
                case CANCELAMENTO:
                    inativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case ESTORNO_CANCELAMENTO:
                	reativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                	break;
                case TRANSFERENCIA:
                    inativarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case ATUALIZACAO_ALUNO:
                    atualizarAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case EXCLUSAO_DISCIPLINA:
                    integrarDisciplinasAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case INCLUSAO_DISCIPLINA:
                    integrarDisciplinasAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case APROVEITAMENTO_DISCIPLINA:
                    integrarDisciplinasAluno(operacao, usuarioVO, configuracaoGeralSistemaVO);
                    break;
                case TURMA:                	
                	break;
                case TURMA_2CH:                	
                	break;
                case TURMA_EXAME:                	
                	break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void incluirTrancamentoOrJubilamentotoAluno(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
        	Map<String, Object> filtros = new HashMap<String, Object>();
            try {
                if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.TRANCAMENTO)) {
                	filtros.put("matricula", trancamentoVO.getMatricula().getMatricula());
                    List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                    if (isValidoOperacaoInativacaoTempoReal(aluno, configuracaoGeralSistemaVO, trancamentoVO.getMatricula().getMatricula())) {
//                        inativarAluno(TRANCAMENTO, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoInativacao(aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.TRANCAMENTO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }
                }else if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO)) {
                	filtros.put("matricula", trancamentoVO.getMatricula().getMatricula());
                	List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                	if (isValidoOperacaoInativacaoTempoReal(aluno, configuracaoGeralSistemaVO, trancamentoVO.getMatricula().getMatricula())) {
//                        inativarAluno(TRANCAMENTO, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                		String dadosEnvio = montarJsonAlunoInativacao(aluno.get(0)).toString();
                		IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ABANDONO_CURSO, aluno.get(0), dadosEnvio);
                		incluirOperacao(operacao, usuarioVO);
                	}
                }else if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.JUBILAMENTO)) {
                	filtros.put("matricula", trancamentoVO.getMatricula().getMatricula());
                    List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                    if (isValidoOperacaoInativacaoTempoReal(aluno, configuracaoGeralSistemaVO, trancamentoVO.getMatricula().getMatricula())) {
//                        inativarAluno("JUBILAMENTO", aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoInativacao(aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.JUBILAMENTO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }
                }
            } catch (Exception e) {
//            	RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//            	if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.JUBILAMENTO)) {
//            		 registroExecucaoJobVO.setNome("MESTREGR-JUBILAMENTO");
//            	 }else if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.TRANCAMENTO)) {
//            		 registroExecucaoJobVO.setNome("MESTREGR-TRANCAMENTO");
//            	 }else {
//            		 registroExecucaoJobVO.setNome("MESTREGR-ABANDONO_CURSO");
//            	 }                
//                registroExecucaoJobVO.setErro("Erro ao inativar aluno "+trancamentoVO.getMatricula().getAluno().getNome()+" da matricula "+trancamentoVO.getMatricula().getMatricula()+": "+ e.getMessage());
//                registroExecucaoJobVO.setDataInicio(new Date());
//                registroExecucaoJobVO.setDataTermino(new Date());
//                registroExecucaoJobVO.setTotal(1);
//                registroExecucaoJobVO.setTotalErro(1);
//                getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
            }
        }
    }
    
    @Override
    public void incluirEstornarTrancamentoOrJubilamentotoAluno(TrancamentoVO trancamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
        	Map<String, Object> filtros = new HashMap<String, Object>();
            try {
            	 if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.TRANCAMENTO)) {
                	filtros.put("matricula", trancamentoVO.getMatricula().getMatricula());
                    List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                    if (isValidoOperacaoTempoReal(aluno, configuracaoGeralSistemaVO, trancamentoVO.getMatricula().getMatricula())) {
//                        inativarAluno(TRANCAMENTO, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoReativacao(aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ESTORNO_TRANCAMENTO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }
            	 }else if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.ABANDONO_DE_CURSO)) {
                	filtros.put("matricula", trancamentoVO.getMatricula().getMatricula());
                    List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                    if (isValidoOperacaoTempoReal(aluno, configuracaoGeralSistemaVO, trancamentoVO.getMatricula().getMatricula())) {
//                        inativarAluno(TRANCAMENTO, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoReativacao(aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ESTORNO_ABANDONO_CURSO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }                
            	 }else if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.JUBILAMENTO)) {
                	filtros.put("matricula", trancamentoVO.getMatricula().getMatricula());
                    List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                    if (isValidoOperacaoTempoReal(aluno, configuracaoGeralSistemaVO, trancamentoVO.getMatricula().getMatricula())) {
//                        inativarAluno("JUBILAMENTO", aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoReativacao(aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ESTORNO_JUBILAMENTO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }
                }
            } catch (Exception e) {
//            	RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//            	if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.JUBILAMENTO)) {
//           		 	registroExecucaoJobVO.setNome("MESTREGR-ESTORNO_JUBILAMENTO");
//            	}else if (TipoTrancamentoEnum.getEnum(trancamentoVO.getTipoTrancamento()).equals(TipoTrancamentoEnum.TRANCAMENTO)) {
//           		 	registroExecucaoJobVO.setNome("MESTREGR-ESTORNO_TRANCAMENTO");
//           	 	}else {
//           	 		registroExecucaoJobVO.setNome("MESTREGR-ESTORNO_ABANDONO_CURSO");
//           	 	}      
//                registroExecucaoJobVO.setErro("Erro ao inativar aluno "+trancamentoVO.getMatricula().getAluno().getNome()+" da matricula "+trancamentoVO.getMatricula().getMatricula()+": "+ e.getMessage());
//                registroExecucaoJobVO.setDataInicio(new Date());
//                registroExecucaoJobVO.setDataTermino(new Date());
//                registroExecucaoJobVO.setTotal(1);
//                registroExecucaoJobVO.setTotalErro(1);
//                getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void incluirCancelamentoAluno(CancelamentoVO cancelamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
            	Map<String, Object> filtros = new HashMap<String, Object>();
            	filtros.put("matricula", cancelamentoVO.getMatricula().getMatricula());
                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                if (isValidoOperacaoInativacaoTempoReal(aluno, configuracaoGeralSistemaVO, cancelamentoVO.getMatricula().getMatricula())) {
//                    inativarAluno("CANCELAMENTO", aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                    String dadosEnvio = montarJsonAlunoInativacao(aluno.get(0)).toString();
                    IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.CANCELAMENTO, aluno.get(0), dadosEnvio);
                    incluirOperacao(operacao, usuarioVO);
                }
            } catch (Exception e) {
//            	 RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                 registroExecucaoJobVO.setNome("MESTREGR-CANCELAMENTO");
//                 registroExecucaoJobVO.setErro("Erro ao inativar aluno "+cancelamentoVO.getMatricula().getAluno().getNome()+" da matricula "+cancelamentoVO.getMatricula().getMatricula()+": "+ e.getMessage());
//                 registroExecucaoJobVO.setDataInicio(new Date());
//                 registroExecucaoJobVO.setDataTermino(new Date());
//                 registroExecucaoJobVO.setTotal(1);
//                 registroExecucaoJobVO.setTotalErro(1);
//                 getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                 e.printStackTrace();
            }
        }
    }
    
    @Override
    public void incluirEstornoCancelamentoAluno(CancelamentoVO cancelamentoVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
            	Map<String, Object> filtros = new HashMap<String, Object>();
            	filtros.put("matricula", cancelamentoVO.getMatricula().getMatricula());
                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                if (isValidoOperacaoTempoReal(aluno, configuracaoGeralSistemaVO, cancelamentoVO.getMatricula().getMatricula())) {
//                    inativarAluno("CANCELAMENTO", aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                    String dadosEnvio = montarJsonAlunoReativacao(aluno.get(0)).toString();
                    IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ESTORNO_CANCELAMENTO, aluno.get(0), dadosEnvio);
                    incluirOperacao(operacao, usuarioVO);
                }
            } catch (Exception e) {
//            	 RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                 registroExecucaoJobVO.setNome("MESTREGR-ESTORNO_CANCELAMENTO");
//                 registroExecucaoJobVO.setErro("Erro ao inativar aluno "+cancelamentoVO.getMatricula().getAluno().getNome()+" da matricula "+cancelamentoVO.getMatricula().getMatricula()+": "+ e.getMessage());
//                 registroExecucaoJobVO.setDataInicio(new Date());
//                 registroExecucaoJobVO.setDataTermino(new Date());
//                 registroExecucaoJobVO.setTotal(1);
//                 registroExecucaoJobVO.setTotalErro(1);
//                 getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                 e.printStackTrace();
            }
        }
    }

    @Override
    public void incluirTransferenciaSaidaAluno(TransferenciaSaidaVO transferenciaSaidaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
            	Map<String, Object> filtros = new HashMap<String, Object>();
            	filtros.put("matricula", transferenciaSaidaVO.getMatricula().getMatricula());
                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                if (isValidoOperacaoInativacaoTempoReal(aluno, configuracaoGeralSistemaVO, transferenciaSaidaVO.getMatricula().getMatricula())) {
//                    inativarAluno("TRANSFERENCIA", aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                    String dadosEnvio = montarJsonAlunoInativacao(aluno.get(0)).toString();
                    IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.TRANSFERENCIA, aluno.get(0), dadosEnvio);
                    incluirOperacao(operacao, usuarioVO);
                }
            } catch (Exception e) {
//            	 RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                 registroExecucaoJobVO.setNome("MESTREGR-TRANSFERENCIA");
//                 registroExecucaoJobVO.setErro("Erro ao inativar aluno "+transferenciaSaidaVO.getMatricula().getAluno().getNome()+" da matricula "+transferenciaSaidaVO.getMatricula().getMatricula()+": "+ e.getMessage());
//                 registroExecucaoJobVO.setDataInicio(new Date());
//                 registroExecucaoJobVO.setDataTermino(new Date());
//                 registroExecucaoJobVO.setTotal(1);
//                 registroExecucaoJobVO.setTotalErro(1);
//                 getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                 e.printStackTrace();
            }
        }
    }

    /**
     * Deve ser verificado se aquela disciplina já está integrada com o mestre GR
     *
     * @param matriculaPeriodoTurmaDisciplinaVO
     * @param usuarioVO
     * @param configuracaoGeralSistemaVO
     */
    @Override
    public void verificarAlunoDisciplinaInsert(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
                Date dataAtual = new Date();
                Map<String, Object> filtros = new HashMap<String, Object>();
                filtros.put("ano", matriculaPeriodoTurmaDisciplinaVO.getAno());
                filtros.put("matricula", matriculaPeriodoTurmaDisciplinaVO.getMatricula());
                filtros.put("semestre", matriculaPeriodoTurmaDisciplinaVO.getSemestre());
                filtros.put("bimestre", matriculaPeriodoTurmaDisciplinaVO.getBimestre().toString());                

                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAlunoAtivo(filtros, false, usuarioVO);

                if (!aluno.isEmpty() && isValidoOperacaoTempoRealDisciplina(aluno, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), dataAtual, matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo())) {
                	aluno.get(0).setBimestre(matriculaPeriodoTurmaDisciplinaVO.getBimestre());                    
                    	List<String> listaDisciplina = new ArrayList<>();                        
                        String bimestre = "";
                        if(matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals("1")) {
                        	bimestre = matriculaPeriodoTurmaDisciplinaVO.getBimestre()+"B";
                        }else if(matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals("2")) {
                        	if(matriculaPeriodoTurmaDisciplinaVO.getBimestre().equals(1)) {
                        		bimestre = "3B";                     
                        	}else {
                        		bimestre = "4B";
                        	}
                        }                        
                        listaDisciplina.add(matriculaPeriodoTurmaDisciplinaVO.getAno()+"-"+getAplicacaoControle().getDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), usuarioVO).getAbreviatura()+"-"+bimestre);
                        String acao = "INSERT";
//                        integrarDisciplinasAluno("INCLUSAO_DISCIPLINA", "INSERT", listaDisciplina, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoDisciplina(listaDisciplina, acao, aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAlunoDisciplina(OperacaoTempoRealMestreGREnum.INCLUSAO_DISCIPLINA, aluno.get(0), dadosEnvio, listaDisciplina, matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
                        incluirOperacao(operacao, usuarioVO);
                    
                }
            } catch (Exception e) {
//            	 RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                 registroExecucaoJobVO.setNome("MESTREGR-INCLUSAO_DISCIPLINA");
//                 registroExecucaoJobVO.setErro("Erro ao excluir disciplina "+matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo()+" da matricula "+matriculaPeriodoTurmaDisciplinaVO.getMatricula()+": "+ e.getMessage());
//                 registroExecucaoJobVO.setDataInicio(new Date());
//                 registroExecucaoJobVO.setDataTermino(new Date());
//                 registroExecucaoJobVO.setTotal(1);
//                 registroExecucaoJobVO.setTotalErro(1);
//                 getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                 e.printStackTrace();
            }
        }
    }

 

    /**
     * Verificar se esta disciplina já estava vinculado ao aluno e integrada com o mestre gr,
     * caso esteja deve ser retirado do mestre gr
     *
     * @param disciplinasAproveitadasVO
     * @param usuarioVO
     * @param configuracaoGeralSistemaVO
     */
    @Override
    public void verificarAproveitamentoDisciplinaAlunoDelete(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
                Date dataAtual = new Date();
                Map<String, Object> filtros = new HashMap<String, Object>();
                filtros.put("ano", matriculaPeriodoTurmaDisciplinaVO.getAno());
                filtros.put("matricula", matriculaPeriodoTurmaDisciplinaVO.getMatricula());
                filtros.put("registroAcademico", getFacadeFactory().getPessoaFacade().consultarRegistroAcademicoPorMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula()));
                filtros.put("semestre", matriculaPeriodoTurmaDisciplinaVO.getSemestre());
                filtros.put("bimestre", matriculaPeriodoTurmaDisciplinaVO.getBimestre());
                filtros.put("disciplinaCodigo", matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());

                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAlunoAtivo(filtros, false, usuarioVO);
                if (!aluno.isEmpty()) {                    
                    if (isValidoOperacaoTempoRealDisciplina(aluno, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), dataAtual, matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo())) {
                    	aluno.get(0).setBimestre(matriculaPeriodoTurmaDisciplinaVO.getBimestre());
                        List<String> listaDisciplina = new ArrayList<>();                        
                        String bimestre = "";
                        if(matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals("1")) {
                        	bimestre = matriculaPeriodoTurmaDisciplinaVO.getBimestre()+"B";
                        }else if(matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals("2")) {
                        	if(matriculaPeriodoTurmaDisciplinaVO.getBimestre().equals(1)) {
                        		bimestre = "3B";                     
                        	}else {
                        		bimestre = "4B";
                        	}
                        }                        
                        listaDisciplina.add(matriculaPeriodoTurmaDisciplinaVO.getAno()+"-"+getAplicacaoControle().getDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), usuarioVO).getAbreviatura()+"-"+bimestre);
                        
                        String acao = "DELETE";
//                  integrarDisciplinasAluno("APROVEITAMENTO_DISCIPLINA", acao, listaDisciplina, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAlunoDisciplina(listaDisciplina, acao, aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAlunoDisciplina(OperacaoTempoRealMestreGREnum.APROVEITAMENTO_DISCIPLINA, aluno.get(0), dadosEnvio, listaDisciplina, matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
                        incluirOperacao(operacao, usuarioVO);
                    }
                }
            } catch (Exception e) {
//            	 RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                 registroExecucaoJobVO.setNome("MESTREGR-APROVEITAMENTO_DISCIPLINA");
//                 registroExecucaoJobVO.setErro("Erro ao excluir disciplina "+matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo()+" da matricula "+matriculaPeriodoTurmaDisciplinaVO.getMatricula()+": "+ e.getMessage());
//                 registroExecucaoJobVO.setDataInicio(new Date());
//                 registroExecucaoJobVO.setDataTermino(new Date());
//                 registroExecucaoJobVO.setTotal(1);
//                 registroExecucaoJobVO.setTotalErro(1);
//                 getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                 e.printStackTrace();
            }
        }
    }

    @Override
    public void verificarAlunoDisciplinaDelete(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO) && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
                Date dataAtual = new Date();
                if(!Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getBimestre())) {
                	matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
                }
        		if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getBimestre())) {
                Map<String, Object> filtros = new HashMap<String, Object>();
                filtros.put("ano", matriculaPeriodoTurmaDisciplinaVO.getAno());
                filtros.put("matricula", matriculaPeriodoTurmaDisciplinaVO.getMatricula());
                filtros.put("registroAcademico", getFacadeFactory().getPessoaFacade().consultarRegistroAcademicoPorMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula()));
                filtros.put("semestre", matriculaPeriodoTurmaDisciplinaVO.getSemestre());
                filtros.put("bimestre", matriculaPeriodoTurmaDisciplinaVO.getBimestre().toString());
                filtros.put("disciplinaCodigo", matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());

                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAlunoAtivo(filtros, false, usuarioVO);
                if (!aluno.isEmpty()) {                    
                    if (!matriculaPeriodoTurmaDisciplinaVO.getBimestre().equals(0) && isValidoOperacaoTempoRealDisciplina(aluno, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), dataAtual, matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo())) {
                    	aluno.get(0).setBimestre(matriculaPeriodoTurmaDisciplinaVO.getBimestre());
                        List<String> listaDisciplina = new ArrayList<String>();
                        String bimestre = "";
                        if(matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals("1")) {
                        	bimestre = matriculaPeriodoTurmaDisciplinaVO.getBimestre()+"B";
                        }else if(matriculaPeriodoTurmaDisciplinaVO.getSemestre().equals("2")) {
                        	if(matriculaPeriodoTurmaDisciplinaVO.getBimestre().equals(1)) {
                        		bimestre = "3B";                     
                        	}else {
                        		bimestre = "4B";
                        	}
                        }                        
                        listaDisciplina.add(matriculaPeriodoTurmaDisciplinaVO.getAno()+"-"+getAplicacaoControle().getDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), usuarioVO).getAbreviatura()+"-"+bimestre);
//                  integrarDisciplinasAluno("EXCLUSAO_DISCIPLINA", "DELETE", listaDisciplina, aluno.get(0), usuarioVO, configuracaoGeralSistemaVO);
                        String acao = "DELETE";
                        String dadosEnvio = montarJsonAlunoDisciplina(listaDisciplina, acao, aluno.get(0)).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAlunoDisciplina(OperacaoTempoRealMestreGREnum.EXCLUSAO_DISCIPLINA, aluno.get(0), dadosEnvio, listaDisciplina, matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
                        incluirOperacao(operacao, usuarioVO);
                    }
                }
        		}
            } catch (Exception e) {
//            	 RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                 registroExecucaoJobVO.setNome("MESTREGR-EXCLUSAO_DISCIPLINA");
//                 registroExecucaoJobVO.setErro("Erro ao excluir disciplina "+matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo()+" da matricula "+matriculaPeriodoTurmaDisciplinaVO.getMatricula()+": "+ e.getMessage());
//                 registroExecucaoJobVO.setDataInicio(new Date());
//                 registroExecucaoJobVO.setDataTermino(new Date());
//                 registroExecucaoJobVO.setTotal(1);
//                 registroExecucaoJobVO.setTotalErro(1);
//                 getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                 e.printStackTrace();
            }
        }
    }

    @Override
    public void atualizarAlunoIntegrado(PessoaVO pessoaVO, int hashPessoaIncial, int hashPessoalFinal, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
            	Map<String, Object> filtros = new HashMap<String, Object>();
            	filtros.put("codigopessoa", pessoaVO.getCodigo());
                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAlunoAtivo(filtros, false, usuarioVO);
                if (hashPessoaIncial != hashPessoalFinal && !aluno.isEmpty()) {
                	filtros.put("ano", aluno.get(0).getAno());
                	filtros.put("matricula", aluno.get(0).getMatricula());
                	filtros.put("semestre", aluno.get(0).getSemestre());
                    //String jsonMontadoBusca = montarJsonOperacao(filtros);
                    if (existeAlunoMestreGR(aluno.get(0).getCodigoAluno(), configuracaoGeralSistemaVO)) {
//                        atualizarPorPessoaAluno("ATUALIZACAO_ALUNO", aluno.get(0), pessoaVO, usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAtualizarAluno(aluno.get(0), pessoaVO).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ATUALIZACAO_ALUNO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }
                }
            } catch (Exception e) {
//                RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                registroExecucaoJobVO.setNome("MESTREGR-ATUALIZAR_ALUNO");
//                registroExecucaoJobVO.setErro("Erro ao atualizar aluno do RA "+pessoaVO.getRegistroAcademico()+" de código "+pessoaVO.getCodigo()+": "+ e.getMessage());
//                registroExecucaoJobVO.setDataInicio(new Date());
//                registroExecucaoJobVO.setDataTermino(new Date());
//                registroExecucaoJobVO.setTotal(1);
//                registroExecucaoJobVO.setTotalErro(1);
//                getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);
                e.printStackTrace();
                
            }
        }
    }

    @Override
    public void atualizarAlunoPorMatricula(MatriculaVO matriculaVO, Map<String, String> hashMatriculasIncial, Map<String, String> hashMatriculasFinal, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        if (configuracaoGeralSistemaVO.getHabilitarOperacoesTempoRealIntegracaoMestreGR() && configuracaoGeralSistemaVO.getHabilitarIntegracaoSistemaProvas()) {
            try {
            	Map<String, Object> filtros = new HashMap<String, Object>();
            	filtros.put("matricula", matriculaVO.getMatricula());
                String hashInicial = hashMatriculasIncial.get(matriculaVO.getMatricula());
                String hashFinal = hashMatriculasFinal.get(matriculaVO.getMatricula());
                List<IntegracaoMestreGRVO> aluno = consultarPorMatriculaAluno(filtros, false, usuarioVO);
                if (!Objects.equals(hashInicial, hashFinal) && !aluno.isEmpty()) {
                	filtros.put("ano", aluno.get(0).getAno());
                	filtros.put("semestre", aluno.get(0).getSemestre());
                    //String jsonMontadoBusca = montarJsonOperacao(filtros);
                    if (existeAlunoMestreGR(aluno.get(0).getCodigoAluno(),  configuracaoGeralSistemaVO)) {
//                        atualizarPorMatriculaAluno("ATUALIZACAO_ALUNO", aluno.get(0), matriculaVO, usuarioVO, configuracaoGeralSistemaVO);
                        String dadosEnvio = montarJsonAtualizarPorMatriculaAluno(aluno.get(0), matriculaVO).toString();
                        IntegracaoMestreGRVO operacao = montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum.ATUALIZACAO_ALUNO, aluno.get(0), dadosEnvio);
                        incluirOperacao(operacao, usuarioVO);
                    }
                }
            } catch (Exception e) {
//            	RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
//                registroExecucaoJobVO.setNome("MESTREGR-ATUALIZAR_ALUNO_MATRICULA");
//                registroExecucaoJobVO.setErro("Erro ao atualizar aluno do RA "+matriculaVO.getAluno().getRegistroAcademico()+" de código "+matriculaVO.getAluno().getCodigo()+": "+ e.getMessage());
//                registroExecucaoJobVO.setDataInicio(new Date());
//                registroExecucaoJobVO.setDataTermino(new Date());
//                registroExecucaoJobVO.setTotal(1);
//                registroExecucaoJobVO.setTotalErro(1);
//                getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, usuarioVO);                
                e.printStackTrace();
            }
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void inativarAluno(OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            HttpResponse<String> jsonResponse = unirest()
                    .post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/" + configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/aluno/save")
                    .header("Content-Type", "application/json").connectTimeout(30000)
                    .body(operacaoTempoRealMestreGRVO.getDadosEnvio()).asString();
            validarResponse(jsonResponse, operacaoTempoRealMestreGRVO, usuarioVO);
        } catch (Exception e) {
            operacaoTempoRealMestreGRVO.setSituacao("ERRO");
            operacaoTempoRealMestreGRVO.setDadosRetorno(e.getMessage());
            operacaoTempoRealMestreGRVO.setMensagemErro(e.getMessage());
            operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
            alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);            
        }
    }
    
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void reativarAluno(OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            HttpResponse<String> jsonResponse = unirest()
                    .post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/" + configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/aluno/save")
                    .header("Content-Type", "application/json").connectTimeout(30000)
                    .body(operacaoTempoRealMestreGRVO.getDadosEnvio()).asString();
            validarResponse(jsonResponse, operacaoTempoRealMestreGRVO, usuarioVO);
        } catch (Exception e) {
            operacaoTempoRealMestreGRVO.setSituacao("ERRO");
            operacaoTempoRealMestreGRVO.setDadosRetorno(e.getMessage());
            operacaoTempoRealMestreGRVO.setMensagemErro(e.getMessage());
            operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
            alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);            
        }
    }
    
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean existeAlunoMestreGR(String ra, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            HttpResponse<String> jsonResponse = unirest()
                    .get(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/" + configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/aluno/list/0/"+ra)
                    .header("Content-Type", "application/json").connectTimeout(30000)
                    .asString();
            
            String body = jsonResponse.getBody();
            JSONObject responseJson;
            try {
                // tenta parse normal
                responseJson = new JSONObject(body);
            } catch (Exception je) {
                // se não for JSON válido, embrulha a string bruta num campo "raw"
                responseJson = new JSONObject();
                responseJson.put("raw", body);
            }
            if("success".equalsIgnoreCase(responseJson.optString("status")) 
            		&& !responseJson.isNull("data")
            		&& responseJson.get("data") instanceof JSONArray
            		&& !responseJson.optJSONArray("data").isEmpty()
            		&& responseJson.optJSONArray("data").getJSONObject(0).optString("CD_ALUNO").equalsIgnoreCase(ra)) {
            	return true;
            }
            return false;
        } catch (Exception e) {
           return false;
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void atualizarAluno(OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            HttpResponse<String> jsonResponse = unirest()
                    .post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/" + configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/aluno/save")
                    .header("Content-Type", "application/json").connectTimeout(30000)
                    .body(operacaoTempoRealMestreGRVO.getDadosEnvio()).asString();
            validarResponse(jsonResponse, operacaoTempoRealMestreGRVO, usuarioVO);
        } catch (Exception e) {
            operacaoTempoRealMestreGRVO.setSituacao("ERRO");
            operacaoTempoRealMestreGRVO.setDadosRetorno(e.getMessage());
            operacaoTempoRealMestreGRVO.setMensagemErro(e.getMessage());
            operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
            alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);           
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void atualizarPorMatriculaAluno(OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            HttpResponse<String> jsonResponse = unirest()
                    .post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/" + configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/aluno/save")
                    .header("Content-Type", "application/json").connectTimeout(30000)
                    .body(operacaoTempoRealMestreGRVO.getDadosEnvio()).asString();
            validarResponse(jsonResponse, operacaoTempoRealMestreGRVO, usuarioVO);
        } catch (Exception e) {
            operacaoTempoRealMestreGRVO.setSituacao("ERRO");
            operacaoTempoRealMestreGRVO.setDadosRetorno(e.getMessage());
            operacaoTempoRealMestreGRVO.setMensagemErro(e.getMessage());
            operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
            alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
            ErrorInfoRSVO errorInfoRSVO = new ErrorInfoRSVO();
            errorInfoRSVO.setCodigo(Response.Status.BAD_REQUEST.name());
            e.printStackTrace();
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void integrarDisciplinasAluno(OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            HttpResponse<String> jsonResponse = unirest()
                    .post(configuracaoGeralSistemaVO.getIntegracaoMestreGRURLBaseAPI() + "/ws/api/" + configuracaoGeralSistemaVO.getTokenIntegracaoSistemasProvaMestreGR() + "/aluno/save")
                    .header("Content-Type", "application/json").connectTimeout(30000)
                    .body(operacaoTempoRealMestreGRVO.getDadosEnvio()).asString();
            validarResponse(jsonResponse, operacaoTempoRealMestreGRVO, usuarioVO);
        } catch (Exception e) {
            operacaoTempoRealMestreGRVO.setSituacao("ERRO");
            operacaoTempoRealMestreGRVO.setDadosRetorno(e.getMessage());
            operacaoTempoRealMestreGRVO.setMensagemErro(e.getMessage());
            operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
            alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);           
        }
    }

    private void validarResponse(HttpResponse<String> jsonResponse, OperacaoTempoRealMestreGRVO operacaoTempoRealMestreGRVO, UsuarioVO usuarioVO) {
        String body = jsonResponse.getBody();
        JSONObject responseJson;
        try {
            // tenta parse normal
            responseJson = new JSONObject(body);
        } catch (Exception je) {
            // se não for JSON válido, embrulha a string bruta num campo "raw"
            responseJson = new JSONObject();
            responseJson.put("raw", body);
        }
        try {
            switch (jsonResponse.getStatus()) {
                case 200:
                    if ("success".equalsIgnoreCase(responseJson.optString("status"))) {
//                    System.out.println(responseJson);
                        operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());
                        operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
                        operacaoTempoRealMestreGRVO.setMensagemErro("");
                        alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
                    } else if ("error".equalsIgnoreCase(responseJson.optString("status"))) {
//                    System.out.println(responseJson);
                        operacaoTempoRealMestreGRVO.setSituacao("ERRO");
                        operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
                        operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());

                        if (responseJson.optString("data").equals("[]")) {
                            operacaoTempoRealMestreGRVO.setMensagemErro("Retorno Null []");
                        } else {
                            operacaoTempoRealMestreGRVO.setMensagemErro(responseJson.toString());
                        }
                        operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());
                        alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
                    } else {
                        operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());
                        operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
                        operacaoTempoRealMestreGRVO.setMensagemErro("");
                        alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
                    }
                    break;
                case 400:
                    operacaoTempoRealMestreGRVO.setSituacao("ERRO");
                    operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
                    operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());
                    operacaoTempoRealMestreGRVO.setMensagemErro(responseJson.toString());
                    if (responseJson.optString("data").equals("[]")) {
                        operacaoTempoRealMestreGRVO.setMensagemErro("Retorno Null []");
                    } else {
                        operacaoTempoRealMestreGRVO.setMensagemErro(responseJson.toString());
                    }
                    alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
                    break;
                case 403:
                    operacaoTempoRealMestreGRVO.setSituacao("ERRO");
                    operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
                    operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());
                    if (responseJson.optString("data").equals("[]")) {
                        operacaoTempoRealMestreGRVO.setMensagemErro("Retorno Null []");
                    } else {
                        operacaoTempoRealMestreGRVO.setMensagemErro(responseJson.toString());
                    }
                    operacaoTempoRealMestreGRVO.setMensagemErro("Não Autorizado - Verifique a URL e Token");
                    alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
                    break;
                default:
                    operacaoTempoRealMestreGRVO.setSituacao("ERRO");
                    operacaoTempoRealMestreGRVO.setProcessado(Boolean.TRUE);
                    operacaoTempoRealMestreGRVO.setDadosRetorno(responseJson.toString());
                    operacaoTempoRealMestreGRVO.setMensagemErro(responseJson.toString());
                    if (responseJson.optString("data").equals("[]")) {
                        operacaoTempoRealMestreGRVO.setMensagemErro("Retorno Null []");
                    } else {
                        operacaoTempoRealMestreGRVO.setMensagemErro(responseJson.toString());
                    }
                    alterarPosProcessamento(operacaoTempoRealMestreGRVO, usuarioVO);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JsonObject montarJsonAlunoInativacao(IntegracaoMestreGRVO alunoConsulta) {
        JsonObject aluno = new JsonObject();

        aluno.addProperty("codigo", alunoConsulta.getCodigoAluno());//Obrigatorio
        aluno.addProperty("codigo_interno", alunoConsulta.getCodigoInternoAluno());//Obrigatorio
        aluno.addProperty("check_account_exists", "N");
        aluno.addProperty("nome", alunoConsulta.getNomeAluno()); //Obrigatorio
        aluno.addProperty("email", alunoConsulta.getEmailAluno());//Obrigatorio
        aluno.addProperty("ativo", "N");
        aluno.addProperty("senha", "");
        aluno.addProperty("senha_pais", "");
        if (alunoConsulta.getCodigoDiaSemanaAluno() != null && alunoConsulta.getCodigoDiaSemanaAluno() == 8) {
            aluno.addProperty("semana_dia_sigla", "8");
        }
        aluno.addProperty("mudarsenha", "N");//Obrigatorio

        JsonObject polo = new JsonObject();
        polo.addProperty("codigo_interno", alunoConsulta.getCodigoInternoPolo());//Obrigatorio
        polo.addProperty("descricao", alunoConsulta.getDescricaoPolo());//Obrigatorio
        aluno.add("polo", polo);

        JsonObject curso = new JsonObject();
        curso.addProperty("codigo_interno", alunoConsulta.getCodigoInternoCurso());
        aluno.add("curso", curso);

        aluno.addProperty("is_tempo_estendido", alunoConsulta.getTempoEstendidoAluno());
        aluno.addProperty("numero_celular", alunoConsulta.getNumeroCelularAluno());
//        System.out.printf(aluno.toString());
        return aluno;
    }
    
    public JsonObject montarJsonAlunoReativacao(IntegracaoMestreGRVO alunoConsulta) {
        JsonObject aluno = new JsonObject();

        aluno.addProperty("codigo", alunoConsulta.getCodigoAluno());//Obrigatorio
        aluno.addProperty("codigo_interno", alunoConsulta.getCodigoInternoAluno());//Obrigatorio
        aluno.addProperty("check_account_exists", "N");
        aluno.addProperty("nome", alunoConsulta.getNomeAluno()); //Obrigatorio
        aluno.addProperty("email", alunoConsulta.getEmailAluno());//Obrigatorio
        aluno.addProperty("ativo", "S");
        aluno.addProperty("senha", "");
        aluno.addProperty("senha_pais", "");
        if (alunoConsulta.getCodigoDiaSemanaAluno() != null && alunoConsulta.getCodigoDiaSemanaAluno() == 8) {
            aluno.addProperty("semana_dia_sigla", "8");
        }
        aluno.addProperty("mudarsenha", "N");//Obrigatorio

        JsonObject polo = new JsonObject();
        polo.addProperty("codigo_interno", alunoConsulta.getCodigoInternoPolo());//Obrigatorio
        polo.addProperty("descricao", alunoConsulta.getDescricaoPolo());//Obrigatorio
        aluno.add("polo", polo);

        JsonObject curso = new JsonObject();
        curso.addProperty("codigo_interno", alunoConsulta.getCodigoInternoCurso());
        aluno.add("curso", curso);

        aluno.addProperty("is_tempo_estendido", alunoConsulta.getTempoEstendidoAluno());
        aluno.addProperty("numero_celular", alunoConsulta.getNumeroCelularAluno());
//        System.out.printf(aluno.toString());
        return aluno;
    }

    public JsonObject montarJsonAtualizarAluno(IntegracaoMestreGRVO alunoConsulta, PessoaVO pessoaVO) {
        JsonObject aluno = new JsonObject();

        aluno.addProperty("codigo", alunoConsulta.getCodigoAluno());//Obrigatorio
        aluno.addProperty("codigo_interno", alunoConsulta.getCodigoInternoAluno());//Obrigatorio
        aluno.addProperty("check_account_exists", "N");
        aluno.addProperty("nome", pessoaVO.getNome()); //Obrigatorio
        aluno.addProperty("email", alunoConsulta.getEmailAluno());//Obrigatorio
        aluno.addProperty("semana_dia_sigla", alunoConsulta.getCodigoDiaSemanaAluno());

        aluno.addProperty("mudarsenha", "N");//Obrigatorio

        JsonObject polo = new JsonObject();
        polo.addProperty("codigo_interno", alunoConsulta.getCodigoInternoPolo());//Obrigatorio
        polo.addProperty("descricao", alunoConsulta.getDescricaoPolo());//Obrigatorio
        aluno.add("polo", polo);

        JsonObject curso = new JsonObject();
        curso.addProperty("codigo_interno", alunoConsulta.getCodigoInternoCurso());
        aluno.add("curso", curso);

        aluno.addProperty("is_tempo_estendido", alunoConsulta.getTempoEstendidoAluno());//Obrigatorio
        aluno.addProperty("numero_celular", alunoConsulta.getNumeroCelularAluno());

//        System.out.printf(aluno.toString());
        return aluno;
    }

    public JsonObject montarJsonAtualizarPorMatriculaAluno(IntegracaoMestreGRVO alunoConsulta, MatriculaVO matriculaVO) {
        JsonObject aluno = new JsonObject();

        aluno.addProperty("codigo", alunoConsulta.getCodigoAluno());//Obrigatorio
        aluno.addProperty("codigo_interno", alunoConsulta.getCodigoInternoAluno());//Obrigatorio
        aluno.addProperty("check_account_exists", "N");
        aluno.addProperty("nome", alunoConsulta.getNomeAluno()); //Obrigatorio
        aluno.addProperty("email", alunoConsulta.getEmailAluno());//Obrigatorio
        aluno.addProperty("semana_dia_sigla", alunoConsulta.getCodigoDiaSemanaAluno());

        aluno.addProperty("mudarsenha", "N");//Obrigatorio

        JsonObject polo = new JsonObject();
        polo.addProperty("codigo_interno", alunoConsulta.getCodigoInternoPolo());//Obrigatorio
        polo.addProperty("descricao", alunoConsulta.getDescricaoPolo());//Obrigatorio
        aluno.add("polo", polo);

        JsonObject curso = new JsonObject();
        curso.addProperty("codigo_interno", alunoConsulta.getCodigoInternoCurso());
        aluno.add("curso", curso);

        aluno.addProperty("is_tempo_estendido", alunoConsulta.getTempoEstendidoAluno());//Obrigatorio
        aluno.addProperty("numero_celular", alunoConsulta.getNumeroCelularAluno());

//        System.out.printf(aluno.toString());
        return aluno;
    }

    private JsonObject montarJsonAlunoDisciplina(List<String> disciplinas, String operacao, IntegracaoMestreGRVO aluno) {
        JsonObject alunoJson = new JsonObject();

        alunoJson.addProperty("codigo", aluno.getCodigoAluno());//Obrigatorio
        alunoJson.addProperty("codigo_interno", aluno.getCodigoInternoAluno());//Obrigatorio
        alunoJson.addProperty("check_account_exists", "N");
        alunoJson.addProperty("nome", aluno.getNomeAluno()); //Obrigatorio
        alunoJson.addProperty("email", aluno.getEmailAluno());//Obrigatorio

        if (aluno.getSituacao().equals("JU") ||
                aluno.getSituacao().equals("TR") ||
                aluno.getSituacao().equals("CI") ||
                aluno.getSituacao().equals("CT") ||
                aluno.getSituacao().equals("AC")) {
            alunoJson.addProperty("ativo", "N");
        }
        if (aluno.getSituacao().equals("AT")) {
            alunoJson.addProperty("ativo", "S");//Obrigatorio
        }
        alunoJson.addProperty("senha", "");
        alunoJson.addProperty("senha_pais", "");
        alunoJson.addProperty("mudarsenha", "N");//Obrigatorio
        if (aluno.getCodigoDiaSemanaAluno() != null && aluno.getCodigoDiaSemanaAluno() == 8) {
            alunoJson.addProperty("semana_dia_sigla", "8");
        }
        alunoJson.addProperty("is_tempo_estendido", aluno.getTempoEstendidoAluno());
        alunoJson.addProperty("numero_celular", aluno.getNumeroCelularAluno());

        JsonArray turmasArray = new JsonArray();

        if (disciplinas.isEmpty()) {
            JsonObject turma = new JsonObject();
            turmasArray.add(turma);
            alunoJson.add("turmas", turmasArray);
        }

        disciplinas.forEach(disciplinaItem -> {
            JsonObject turma = new JsonObject();
            turma.addProperty("codigo_interno", disciplinaItem); // Obrigatório
            turma.addProperty("acao", operacao); //"INSERT" ou "DELETE" //Obrigatorio
            turmasArray.add(turma);
            alunoJson.add("turmas", turmasArray);
        });

        JsonObject polo = new JsonObject();
        polo.addProperty("codigo_interno", aluno.getCodigoInternoPolo());//Obrigatorio
        polo.addProperty("descricao", aluno.getDescricaoPolo());//Obrigatorio
        alunoJson.add("polo", polo);

        JsonObject curso = new JsonObject();
        curso.addProperty("codigo_interno", aluno.getCodigoInternoCurso());
        alunoJson.add("curso", curso);
//        System.out.printf(alunoJson.toString());
        return alunoJson;
    }

    private IntegracaoMestreGRVO montarLogOperacoesAluno(OperacaoTempoRealMestreGREnum operacaoTempoRealMestreGREnum, IntegracaoMestreGRVO aluno, String dadosEnvio) {
        IntegracaoMestreGRVO log = new IntegracaoMestreGRVO();
        log.setAno(aluno.getAno());
        log.setSemestre(aluno.getSemestre());
        log.setBimestre(aluno.getBimestre());
        log.setMatricula(aluno.getMatricula());
        log.setDadosEnvio(dadosEnvio);
        log.setOrigem(operacaoTempoRealMestreGREnum);
        return log;
    }

    private IntegracaoMestreGRVO montarLogOperacoesAlunoDisciplina(OperacaoTempoRealMestreGREnum operacaoTempoRealMestreGREnum, IntegracaoMestreGRVO aluno, String dadosEnvio, List<String> disciplinas, Integer codigoDisciplina) {
        IntegracaoMestreGRVO log = new IntegracaoMestreGRVO();
        log.setCodigoDisciplina(codigoDisciplina);        
        log.setAno(aluno.getAno());
        log.setSemestre(aluno.getSemestre());
        log.setBimestre(aluno.getBimestre());
        log.setMatricula(aluno.getMatricula());
        log.setDadosEnvio(dadosEnvio);
        log.setOrigem(operacaoTempoRealMestreGREnum);
        return log;
    }

//    private void limparFiltros() {
//        setFiltros(null);
//    }
//
//    public Map<String, Object> getFiltros() {
//        if (filtros == null) {
//            filtros = new HashMap<>();
//        }
//        return filtros;
//    }
//
//    public void setFiltros(Map<String, Object> filtros) {
//        this.filtros = filtros;
//    }

    private JsonObject converterLoteJSONDisciplina(IntegracaoMestreGRVO integracao) {
        JsonObject dataJson = new JsonObject();
        dataJson.addProperty("lote", integracao.getCodigoLote());
        dataJson.addProperty("ano", integracao.getAno());
        dataJson.addProperty("semestre", integracao.getSemestre() + "SEM");
        dataJson.addProperty("ensino", integracao.getEnsino());
        dataJson.addProperty("descricao_turno", integracao.getTurno());

        JsonObject serieJson = new JsonObject();
        serieJson.addProperty("codigo", "");
        serieJson.addProperty("nome", "");
        dataJson.add("serie", serieJson);

        JsonObject turmaJson = new JsonObject();
        turmaJson.addProperty("chave", integracao.getChaveTurma());
        turmaJson.addProperty("codigo", integracao.getCodigoTurma());
        turmaJson.addProperty("codigo_interno", integracao.getCodigoInternoTurma());
        turmaJson.addProperty("nome", integracao.getNomeTurma());
        turmaJson.addProperty("google_code", "");

        JsonObject disciplinaJson = new JsonObject();
        disciplinaJson.addProperty("codigo", integracao.getCodigoDisciplina());
        disciplinaJson.addProperty("sigla", integracao.getSiglaDisciplina());
        disciplinaJson.addProperty("nome", integracao.getNomeDisciplina());
        turmaJson.add("disciplina", disciplinaJson);

        dataJson.add("turma", turmaJson);
        return dataJson;
    }

    private JsonObject converterLoteJSONAluno(IntegracaoMestreGRVO integracao) {
        JsonObject dataJson = converterLoteJSONDisciplina(integracao);

        JsonObject aluno = new JsonObject();
        aluno.addProperty("codigo_interno", integracao.getCodigoInternoAluno());
        aluno.addProperty("codigo", integracao.getCodigoAluno());
        aluno.addProperty("nome", integracao.getNomeAluno());
        aluno.addProperty("email", integracao.getEmailAluno());
        aluno.addProperty("codigo_dia_semana", integracao.getCodigoDiaSemanaAluno());
        aluno.addProperty("is_tempo_estendido", integracao.getTempoEstendidoAluno());
        aluno.addProperty("numero_celular", integracao.getNumeroCelularAluno());

        JsonObject polo = new JsonObject();
        polo.addProperty("codigo_interno", integracao.getCodigoInternoPolo());
        polo.addProperty("codigo", integracao.getCodigoPolo());
        polo.addProperty("descricao", integracao.getDescricaoPolo());
        aluno.add("polo", polo);

        JsonObject curso = new JsonObject();
        curso.addProperty("codigo_interno", integracao.getCodigoInternoCurso());
        curso.addProperty("codigo", integracao.getCodigoCurso());
        curso.addProperty("descricao", integracao.getDescricaoCurso());
        aluno.add("curso", curso);

        dataJson.add("aluno", aluno);

        return dataJson;
    }

	

    
}