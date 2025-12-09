package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import negocio.comuns.utilitarias.dominios.TipoPublicacao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DisciplinaRelVO;
import relatorio.negocio.interfaces.academico.DisciplinaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DisciplinaRel extends SuperRelatorio implements DisciplinaRelInterfaceFacade {

    public DisciplinaRel() {
        inicializarOrdenacoesRelatorio();
    }

    public List<DisciplinaRelVO> criarObjeto(String tipoRelatorio, CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {
        List<DisciplinaRelVO> disciplinaRelVOs = new ArrayList<DisciplinaRelVO>(0);
        if ((curso.getCodigo().intValue() != 0) && (curso != null)) {
            if (tipoRelatorio.equals("ReferenciaBibliografica")) {
                SqlRowSet dadosSQL = executarConsultaParametrizadaReferencia(curso, gradeCurricular, periodoLetivo, disciplinaVO);
                while (dadosSQL.next()) {
                    disciplinaRelVOs.add(montarDadosReferencia(dadosSQL, curso));
                }
            } else if (tipoRelatorio.equals("PlanejamentoConteudo")) {
                SqlRowSet dadosSQL = executarConsultaParametrizadaPlanejamento(curso, gradeCurricular, periodoLetivo, disciplinaVO);
                while (dadosSQL.next()) {
                    disciplinaRelVOs.add(montarDadosPlanejamento(dadosSQL, curso));
                }
            }
        } else {
            if (tipoRelatorio.equals("ReferenciaBibliografica")) {
                SqlRowSet dadosSQL = executarConsultaParametrizadaDisciplinaReferencia(curso, gradeCurricular, periodoLetivo, disciplinaVO);
                while (dadosSQL.next()) {
                    disciplinaRelVOs.add(montarDadosReferencia(dadosSQL, curso));
                }
            } else if (tipoRelatorio.equals("PlanejamentoConteudo")) {
                SqlRowSet dadosSQL = executarConsultaParametrizadaDisciplinaPlanejamento(curso, gradeCurricular, periodoLetivo, disciplinaVO);
                while (dadosSQL.next()) {
                    disciplinaRelVOs.add(montarDadosPlanejamento(dadosSQL, curso));
                }
            }
        }
        return disciplinaRelVOs;
    }
    
    public GradeCurricularVO criarObjetoRelatorioSintetico(String tipoRelatorio, CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception {
    	SqlRowSet dadosSQL = executarConsultaParametrizadaPeriodoLetivo(curso, gradeCurricular, periodoLetivo, disciplinaVO);
    	if (dadosSQL.next()) {
    		return montarDadosPeriodoLetivo(dadosSQL, periodoLetivo, disciplinaVO, usuario);
    	}
    	return new GradeCurricularVO();
    }
    
    public static GradeCurricularVO montarDadosPeriodoLetivo(SqlRowSet dadosSQL, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception {
        GradeCurricularVO gradeCurricularVO = new GradeCurricularVO();
        gradeCurricularVO.setCodigo(dadosSQL.getInt("codigo"));
        gradeCurricularVO.setNome(dadosSQL.getString("nome"));
        gradeCurricularVO.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        gradeCurricularVO.setSituacao(dadosSQL.getString("situacao"));
        gradeCurricularVO.setDataFinalVigencia(dadosSQL.getDate("dataFinalVigencia"));
        gradeCurricularVO.setCurso(new Integer(dadosSQL.getInt("curso")));
        gradeCurricularVO.setCargaHoraria(new Integer(dadosSQL.getInt("cargahoraria")));
        gradeCurricularVO.setCreditos(new Integer(dadosSQL.getInt("creditos")));
        gradeCurricularVO.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
        gradeCurricularVO.setDataDesativacao(dadosSQL.getDate("dataDesativacao"));
        gradeCurricularVO.getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAtivacao")));
        gradeCurricularVO.getResponsavelDesativacao().setCodigo(new Integer(dadosSQL.getInt("responsavelDesativacao")));

        List<PeriodoLetivoVO> periodoLetivoVOs = new ArrayList<PeriodoLetivoVO>();
        if (periodoLetivo != null && !periodoLetivo.getCodigo().equals(0)) {	
        	PeriodoLetivoVO periodoLetivoVO = new PeriodoLetivoVO();
        	periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(periodoLetivo.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        	
        	if (disciplinaVO != null && !disciplinaVO.getCodigo().equals(0)) {
        		GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
        		gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorCodigoPeriodoLetivoCodigoDisciplina(periodoLetivoVO.getCodigo(), disciplinaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
        		periodoLetivoVO.getGradeDisciplinaVOs().add(gradeDisciplinaVO);
        	} else {
        		periodoLetivoVO.setGradeDisciplinaVOs(getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(periodoLetivoVO.getCodigo(), false, usuario, null));
        	}
        	
        	periodoLetivoVOs.add(periodoLetivoVO);
        	
        } else {
        	periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(gradeCurricularVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        	for (PeriodoLetivoVO periodoLetivoVO : periodoLetivoVOs) {
        		if (disciplinaVO != null && !disciplinaVO.getCodigo().equals(0)) {
        			GradeDisciplinaVO gradeDisciplinaVO = new GradeDisciplinaVO();
            		gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorCodigoPeriodoLetivoCodigoDisciplina(periodoLetivoVO.getCodigo(), disciplinaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
            		periodoLetivoVO.getGradeDisciplinaVOs().add(gradeDisciplinaVO);
        	    } else {
            		periodoLetivoVO.setGradeDisciplinaVOs(getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(periodoLetivoVO.getCodigo(), false, usuario, null));
            	}
			}
        }
        gradeCurricularVO.setPeriodoLetivosVOs(periodoLetivoVOs);
        
        return gradeCurricularVO;
    }

    private DisciplinaRelVO montarDadosReferencia(SqlRowSet dadosSQL, CursoVO curso) {
        DisciplinaRelVO disciplinaRelVO = new DisciplinaRelVO();
        disciplinaRelVO.setNomeDisciplina(dadosSQL.getString("disciplina_nome"));
        disciplinaRelVO.setNrCreditos(dadosSQL.getInt("gradedisciplina_nrcreditos"));
        
        disciplinaRelVO.setTipoDisciplina(TipoDisciplina.getDescricao(dadosSQL.getString("gradedisciplina_tipodisciplina")));
        disciplinaRelVO.setCargaHorariaDisciplina(dadosSQL.getInt("gradedisciplina_cargahoraria"));
        disciplinaRelVO.setTituloReferencia(dadosSQL.getString("referenciabibliografica_titulo"));
        disciplinaRelVO.setTipoReferencia(dadosSQL.getString("referenciabibliografica_tiporeferencia"));
        disciplinaRelVO.setTipoPublicacao(TipoPublicacao.getDescricao(dadosSQL.getString("referenciabibliografica_tipopublicacao")));
        disciplinaRelVO.setAutores(dadosSQL.getString("referenciabibliografica_autores"));
        disciplinaRelVO.setIsbn(dadosSQL.getString("referenciabibliografica_isbn"));
        disciplinaRelVO.setLocalPublicacao(dadosSQL.getString("referenciabibliografica_localpublicacao"));
        disciplinaRelVO.setEdicao(dadosSQL.getString("referenciabibliografica_edicao"));
        disciplinaRelVO.setAnoPublicacao(dadosSQL.getString("referenciabibliografica_anopublicacao"));
        disciplinaRelVO.setPublicacaoExistenteBiblioteca(dadosSQL.getString("referenciabibliografica_publicacaoexistentebiblioteca"));
        //disciplinaRelVO.setObra(dadosSQL.getString("referenciabibliografica_obra"));
        if (curso.getCodigo() != 0 && curso != null) {
            disciplinaRelVO.setNomeCurso(dadosSQL.getString("curso_nome"));
        }
        return disciplinaRelVO;
    }

    private DisciplinaRelVO montarDadosPlanejamento(SqlRowSet dadosSQL, CursoVO curso) {
        DisciplinaRelVO disciplinaRelVO = new DisciplinaRelVO();
        disciplinaRelVO.setNomeDisciplina(dadosSQL.getString("disciplina_nome"));
        disciplinaRelVO.setNrCreditos(dadosSQL.getInt("gradedisciplina_nrcreditos"));
        
        disciplinaRelVO.setTipoDisciplina(TipoDisciplina.getDescricao(dadosSQL.getString("gradedisciplina_tipodisciplina")));
        disciplinaRelVO.setCargaHorariaDisciplina(dadosSQL.getInt("gradedisciplina_cargahoraria"));
        disciplinaRelVO.setConteudo(dadosSQL.getString("conteudoplanejamento_conteudo"));
        disciplinaRelVO.setClassificacao(dadosSQL.getString("conteudoplanejamento_classificacao"));
        disciplinaRelVO.setCargaHorariaConteudo(dadosSQL.getInt("conteudoplanejamento_cargahoraria"));
        disciplinaRelVO.setMetodologia(dadosSQL.getString("conteudoplanejamento_metodologia"));
        disciplinaRelVO.setHabilidade(dadosSQL.getString("conteudoplanejamento_habilidade"));
        disciplinaRelVO.setAtitude(dadosSQL.getString("conteudoplanejamento_atitude"));
        if (curso.getCodigo() != 0 && curso != null) {
            disciplinaRelVO.setNomeCurso(dadosSQL.getString("curso_nome"));
        }
        return disciplinaRelVO;
    }

    public void inicializarOrdenacoesRelatorio() {
        Vector ordenacao = this.getOrdenacoesRelatorio();
        ordenacao.add("Disciplina");
        ordenacao.add("Curso");
    }

    public SqlRowSet executarConsultaParametrizadaReferencia(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {
        String selectStr = "SELECT periodoletivo.codigo, gradedisciplina.ordem, disciplina.nome AS disciplina_nome , gradedisciplina.nrcreditos AS gradedisciplina_nrcreditos,  "
                + "gradedisciplina.tipodisciplina AS gradedisciplina_tipodisciplina,  gradedisciplina.cargahoraria AS gradedisciplina_cargahoraria, "
                + "referenciabibliografica.titulo AS referenciabibliografica_titulo,  referenciabibliografica.tiporeferencia AS referenciabibliografica_tiporeferencia, "
                + "referenciabibliografica.tipopublicacao AS referenciabibliografica_tipopublicacao,  referenciabibliografica.autores AS referenciabibliografica_autores, "
                + "referenciabibliografica.isbn AS referenciabibliografica_isbn,  referenciabibliografica.localpublicacao AS referenciabibliografica_localpublicacao, "
                + "referenciabibliografica.edicao AS referenciabibliografica_edicao,  referenciabibliografica.anopublicacao AS referenciabibliografica_anopublicacao, "
                + "referenciabibliografica.publicacaoexistentebiblioteca AS referenciabibliografica_publicacaoexistentebiblioteca, "
                + "curso.nome AS curso_nome FROM disciplina LEFT JOIN referenciabibliografica ON  (referenciabibliografica.disciplina = disciplina.codigo), "
                + "gradedisciplina, periodoletivo, gradecurricular, curso ";
        selectStr = montarVinculoEntreTabelas(selectStr);
        selectStr = montarFiltrosRelatorio(selectStr, curso, gradeCurricular, periodoLetivo, disciplinaVO);
        selectStr += "GROUP BY periodoletivo.codigo, gradedisciplina.ordem, disciplina_nome ,  gradedisciplina_nrcreditos , gradedisciplina_tipodisciplina,  gradedisciplina_cargahoraria, "
                + "referenciabibliografica_titulo, referenciabibliografica_tiporeferencia,  referenciabibliografica_tipopublicacao, referenciabibliografica_autores, "
                + "referenciabibliografica_isbn,  referenciabibliografica_localpublicacao,  referenciabibliografica_edicao, referenciabibliografica_anopublicacao, "
                + "referenciabibliografica_publicacaoexistentebiblioteca, curso_nome ORDER BY curso.nome,  periodoletivo.codigo, gradedisciplina.ordem, disciplina.nome ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    public SqlRowSet executarConsultaParametrizadaDisciplinaReferencia(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {
        String selectStr = "SELECT disciplina.nome AS disciplina_nome , disciplina.nrcreditos AS disciplina_nrcreditos,   "
                + "  disciplina.tipodisciplina AS disciplina_tipodisciplina,  disciplina.cargahoraria AS disciplina_cargahoraria, "
                + "referenciabibliografica.titulo AS referenciabibliografica_titulo,  referenciabibliografica.tiporeferencia AS referenciabibliografica_tiporeferencia, "
                + "referenciabibliografica.tipopublicacao AS referenciabibliografica_tipopublicacao,  referenciabibliografica.autores AS referenciabibliografica_autores, "
                + "referenciabibliografica.isbn AS referenciabibliografica_isbn,  referenciabibliografica.localpublicacao AS referenciabibliografica_localpublicacao, "
                + "referenciabibliografica.edicao AS referenciabibliografica_edicao,  referenciabibliografica.anopublicacao AS referenciabibliografica_anopublicacao, "
                + "referenciabibliografica.publicacaoexistentebiblioteca AS referenciabibliografica_publicacaoexistentebiblioteca,  referenciabibliografica.obra AS referenciabibliografica_obra "                
        + " from disciplina LEFT JOIN referenciabibliografica ON  (referenciabibliografica.disciplina = disciplina.codigo) where ";
        selectStr = montarFiltrosRelatorio(selectStr, curso, gradeCurricular, periodoLetivo, disciplinaVO);
        selectStr += "GROUP BY disciplina_nome ,  disciplina_nrcreditos ,  disciplina_tipodisciplina,  disciplina_cargahoraria, "
                + "referenciabibliografica_titulo, referenciabibliografica_tiporeferencia,  referenciabibliografica_tipopublicacao, referenciabibliografica_autores, "
                + "referenciabibliografica_isbn,  referenciabibliografica_localpublicacao,  referenciabibliografica_edicao, referenciabibliografica_anopublicacao, "
                + "referenciabibliografica_publicacaoexistentebiblioteca, referenciabibliografica_obra   ORDER BY disciplina.nome ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    public SqlRowSet executarConsultaParametrizadaPlanejamento(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {
        String selectStr = "SELECT periodoletivo.codigo, gradedisciplina.ordem, disciplina.nome AS disciplina_nome , gradedisciplina.nrcreditos AS gradedisciplina_nrcreditos,   "
                + " gradedisciplina.tipodisciplina AS gradedisciplina_tipodisciplina, gradedisciplina.cargahoraria AS gradedisciplina_cargahoraria, "
                + "conteudoplanejamento.conteudo AS conteudoplanejamento_conteudo,  conteudoplanejamento.classificacao AS conteudoplanejamento_classificacao, "
                + "conteudoplanejamento.cargahoraria AS conteudoplanejamento_cargahoraria,  conteudoplanejamento.metodologia AS conteudoplanejamento_metodologia, "
                + " conteudoplanejamento.habilidade AS conteudoplanejamento_habilidade,  conteudoplanejamento.atitude AS conteudoplanejamento_atitude,  curso.nome AS curso_nome "
                + " from disciplina LEFT JOIN conteudoplanejamento ON  (conteudoplanejamento.disciplina = disciplina.codigo),  gradedisciplina, periodoletivo, gradecurricular, curso ";
        selectStr = montarVinculoEntreTabelas(selectStr);
        selectStr = montarFiltrosRelatorio(selectStr, curso, gradeCurricular, periodoLetivo, disciplinaVO);
        selectStr += "GROUP BY periodoletivo.codigo, gradedisciplina.ordem, disciplina_nome ,  gradedisciplina_nrcreditos ,  gradedisciplina_tipodisciplina,  gradedisciplina_cargahoraria, "
                + "conteudoplanejamento_conteudo,  conteudoplanejamento_classificacao,  conteudoplanejamento_cargahoraria,  conteudoplanejamento_metodologia, "
                + "conteudoplanejamento_habilidade,  conteudoplanejamento_atitude, curso_nome ORDER BY curso.nome, periodoletivo.codigo, gradedisciplina.ordem, disciplina.nome ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    public SqlRowSet executarConsultaParametrizadaDisciplinaPlanejamento(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {
        String selectStr = "SELECT disciplina.nome AS disciplina_nome , disciplina.nrcreditos AS disciplina_nrcreditos,   "
                + " disciplina.tipodisciplina AS disciplina_tipodisciplina,  disciplina.cargahoraria AS disciplina_cargahoraria, "
                + "conteudoplanejamento.conteudo AS conteudoplanejamento_conteudo,  conteudoplanejamento.classificacao AS conteudoplanejamento_classificacao, "
                + "conteudoplanejamento.cargahoraria AS conteudoplanejamento_cargahoraria,  conteudoplanejamento.metodologia AS conteudoplanejamento_metodologia, "
                + " conteudoplanejamento.habilidade AS conteudoplanejamento_habilidade,  conteudoplanejamento.atitude AS conteudoplanejamento_atitude "
                + " from disciplina LEFT JOIN conteudoplanejamento ON  (conteudoplanejamento.disciplina = disciplina.codigo) where";
        selectStr = montarFiltrosRelatorio(selectStr, curso, gradeCurricular, periodoLetivo, disciplinaVO);
        selectStr += "GROUP BY disciplina_nome ,  disciplina_nrcreditos ,  disciplina_competencia ,  disciplina_ementa,  disciplina_tipodisciplina,  disciplina_cargahoraria, "
                + "conteudoplanejamento_conteudo,  conteudoplanejamento_classificacao,  conteudoplanejamento_cargahoraria,  conteudoplanejamento_metodologia, "
                + "conteudoplanejamento_habilidade,  conteudoplanejamento_atitude ORDER BY disciplina.nome ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }
    
    public SqlRowSet executarConsultaParametrizadaPeriodoLetivo(CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM gradecurricular ");
        sql.append(" INNER JOIN periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
        sql.append(" INNER JOIN gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
        sql.append(" INNER JOIN disciplina on gradedisciplina.disciplina = disciplina.codigo ");
        sql.append(" WHERE gradecurricular.codigo = ").append(gradeCurricular.getCodigo());	
        if (periodoLetivo != null && !periodoLetivo.getCodigo().equals(0)) {
        	sql.append(" AND periodoletivo.codigo = ").append(periodoLetivo.getCodigo());
        }
        if (disciplinaVO != null && !disciplinaVO.getCodigo().equals(0)) {
        	sql.append(" AND disciplina.codigo = ").append(disciplinaVO.getCodigo());
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return tabelaResultado;
    }

    private String montarFiltrosRelatorio(String selectStr, CursoVO curso, GradeCurricularVO gradeCurricular, PeriodoLetivoVO periodoLetivo, DisciplinaVO disciplinaVO) throws Exception {

        String filtros = "";
        if ((curso.getCodigo() != null) && (curso.getCodigo().intValue() != 0)) {
            filtros = adicionarCondicionalWhere(filtros, "( curso.codigo = " + curso.getCodigo().intValue() + ")", true);
            adicionarDescricaoFiltro("Curso = " + curso.getCodigo().intValue());

            if ((gradeCurricular.getCodigo() != null) && (gradeCurricular.getCodigo().intValue() != 0)) {
                filtros = adicionarCondicionalWhere(filtros, "( gradeCurricular.codigo = " + gradeCurricular.getCodigo().intValue() + ")", true);
                adicionarDescricaoFiltro("GradeCurricular = " + gradeCurricular.getCodigo().intValue());
            }
            if ((periodoLetivo.getCodigo() != null) && (periodoLetivo.getCodigo().intValue() != 0)) {
                filtros = adicionarCondicionalWhere(filtros, "( periodoLetivo.codigo = " + periodoLetivo.getCodigo().intValue() + ")", true);
                adicionarDescricaoFiltro("PeriodoLetivo = " + periodoLetivo.getCodigo().intValue());
            }
            if ((disciplinaVO.getNome() != null) && (!disciplinaVO.getNome().equals(""))) {
                filtros = adicionarCondicionalWhere(filtros, "(disciplina.nome = '" + disciplinaVO.getNome() + "' )", true);
                adicionarDescricaoFiltro("Disciplina = " + disciplinaVO.getNome());
            }
        } else {
            if ((disciplinaVO.getNome() != null) && (!disciplinaVO.getNome().equals(""))) {
                filtros = adicionarCondicionalWhere(filtros, "(disciplina.nome = '" + disciplinaVO.getNome() + "' )", false);
                adicionarDescricaoFiltro("Disciplina = " + disciplinaVO.getNome());
            }
        }
        selectStr += filtros;
        return selectStr;
    }

    private String montarVinculoEntreTabelas(String selectStr) {
        String vinculos = "";
        vinculos = adicionarCondicionalWhere(vinculos, "(gradedisciplina.periodoletivo = periodoletivo.codigo)", false);
        vinculos = adicionarCondicionalWhere(vinculos, "(gradedisciplina.disciplina = disciplina.codigo)", true);
        vinculos = adicionarCondicionalWhere(vinculos, "(periodoletivo.gradecurricular = gradecurricular.codigo)", true);
        vinculos = adicionarCondicionalWhere(vinculos, "(gradecurricular.curso = curso.codigo)", true);
        if (!vinculos.equals("")) {
            if (selectStr.indexOf("WHERE") == -1) {
                selectStr = selectStr + " WHERE " + vinculos;
            } else {
                selectStr = selectStr + " WHERE " + vinculos;
            }
        }
        return selectStr;
    }

    @Override
    public String designIReportRelatorio(String tipoRelatorio) {
        if (tipoRelatorio.equals("ReferenciaBibliografica")) {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
        } else if (tipoRelatorio.equals("PlanejamentoConteudo")) {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Conteudo.jrxml");
        } else {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Sintetico.jrxml");
        }
    }

    @Override
    public String designIReportRelatorioSintetico() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + "Sintetico.jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("DisciplinaRel");
    }

    @Override
    public void ValidarDados(String tipoRelatorio, CursoVO curso, GradeCurricularVO gradeCurricular) throws ConsistirException {
        if (curso.getCodigo() == null || curso.getCodigo() == 0) {
            throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
        }
        if (gradeCurricular.getCodigo() == null || gradeCurricular.getCodigo() == 0) {
            throw new ConsistirException("A Matriz Curricular deve ser informada para a geração do relatório.");
        }
        if (tipoRelatorio == null || tipoRelatorio.equals("")) {
            throw new ConsistirException("O Tipo de Relatório deve ser informado para a geração do relatório.");
        }
    }

}
