package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.ReposicaoRelVO;
import relatorio.negocio.interfaces.academico.ReposicaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ReposicaoRel extends SuperRelatorio implements ReposicaoRelInterfaceFacade {

    protected MatriculaPeriodoVO matriculaPeriodoVO;
    private ReposicaoRelVO reposicaoRelVO;

    public ReposicaoRel() {
    }

    public static void validarDados(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO) throws ConsistirException {
//        if (unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo().equals(0)) {
//            throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
//        }
//        if (cursoVO == null || cursoVO.getCodigo().equals(0)) {
//            throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
//        }
    }

    public List<ReposicaoRelVO> criarObjeto(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, Date dataInicio, Date dataFim, String tipo, Integer responsavel,  Date dataAulaInicio, Date dataAulaFim,  Date dataInclusaoInicio, Date dataInclusaoFim,  Date dataPgtoInicio, Date dataPgtoFim, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sql = new StringBuilder();
        Boolean filtrarApenasComPagamento = (dataPgtoInicio != null || dataPgtoFim != null) ? true : false;
        StringBuilder fromContaReceber = new StringBuilder("");        
        /*
         * O sql abaixo trata casos de inclusao de reposição com requerimeto e taxa no requerimento
         */
        sql.append(realizarGeracaoSelect());
        fromContaReceber.append(" inner join requerimento on requerimento.codigo = inclusaohistoricoforaprazo.requerimento ");
        fromContaReceber.append(" inner join contareceber on contareceber.codigo = requerimento.contareceber ");
        sql.append(realizarGeracaoFromBase(fromContaReceber, filtrarApenasComPagamento, dataInicio));        
        sql.append(realizarGeracaoCondicaoWhere(unidadeEnsinoVO, curso, turma, disciplina, dataInicio, dataFim, tipo, responsavel, dataAulaInicio, dataAulaFim, dataPgtoInicio, dataPgtoFim));
        /*
         * O sql abaixo trata casos de inclusao de reposição com requerimeto sem taxa no requerimento
         */
        sql.append(" union all ");
        sql.append(realizarGeracaoSelect());
        fromContaReceber = new StringBuilder("");
        fromContaReceber.append(" inner join requerimento on requerimento.codigo = inclusaohistoricoforaprazo.requerimento and requerimento.contareceber is null ");
        if(filtrarApenasComPagamento) {
        	fromContaReceber.append(" inner join contareceber on contareceber.tipoorigem = 'IRE' and contareceber.codorigem = inclusaohistoricoforaprazo.codigo::varchar  ");
        }else {
        	fromContaReceber.append(" left join contareceber on contareceber.tipoorigem = 'IRE' and contareceber.codorigem = inclusaohistoricoforaprazo.codigo::varchar  ");
        }        
        sql.append(realizarGeracaoFromBase(fromContaReceber, filtrarApenasComPagamento, dataInicio));        
        sql.append(realizarGeracaoCondicaoWhere(unidadeEnsinoVO, curso, turma, disciplina, dataInicio, dataFim, tipo, responsavel, dataAulaInicio, dataAulaFim, dataPgtoInicio, dataPgtoFim));
        /*
         * O sql abaixo trata casos de inclusao de reposição sem requerimeto
         */
        sql.append(" union all ");
        sql.append(realizarGeracaoSelect());
        fromContaReceber = new StringBuilder("");
        if(filtrarApenasComPagamento) {
        	fromContaReceber.append(" inner join contareceber on contareceber.tipoorigem = 'IRE' and contareceber.codorigem = inclusaohistoricoforaprazo.codigo::varchar  ");
        }else {
        	fromContaReceber.append(" left join contareceber on contareceber.tipoorigem = 'IRE' and contareceber.codorigem = inclusaohistoricoforaprazo.codigo::varchar  ");
        }
        sql.append(realizarGeracaoFromBase(fromContaReceber, filtrarApenasComPagamento, dataInicio));                
        sql.append(realizarGeracaoCondicaoWhere(unidadeEnsinoVO, curso, turma, disciplina, dataInicio, dataFim, tipo, responsavel, dataAulaInicio, dataAulaFim, dataPgtoInicio, dataPgtoFim));
        sql.append(" and inclusaohistoricoforaprazo.requerimento is null ");
        sql.append(" order by unidadeEnsino, turmainclusao, disciplinainclusao, nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsulta(tabelaResultado);
    }
    
    private StringBuilder realizarGeracaoSelect() {
    	StringBuilder sql =  new StringBuilder("");
    	sql.append(" select distinct unidadeensino.nome as unidadeensino, matricula.matricula, pessoa.nome, turmaorigem.codigo as codturmaorigem, turmaorigem.identificadorturma as turmaorigem, ");
        sql.append(" turmainclusao.codigo as codturmainclusao, turmainclusao.identificadorturma as turmainclusao, ");
        sql.append(" disciplina.nome as disciplinainclusao, inclusaohistoricoforaprazo.datainclusao, inclusaohistoricoforaprazo.reposicao, inclusaohistoricoforaprazo.justificativa, inclusaohistoricoforaprazo.observacao, ");
        sql.append(" inclusaohistoricoforaprazo.planofinanceiroreposicao , inclusaohistoricoforaprazo.nrparcelas, inclusaohistoricoforaprazo.valortotalparcela, inclusaohistoricoforaprazo.desconto, ");
        sql.append(" inclusaohistoricoforaprazo.matriculaperiodo, inclusaodisciplinashistoricoforaprazo.disciplina, inclusaodisciplinashistoricoforaprazo.turma, ");
        sql.append(" (select frequenciaaula.presente from registroaula inner join frequenciaaula on frequenciaaula.registroaula = registroaula.codigo ");
        sql.append(" where frequenciaaula.matricula = matricula.matricula and registroaula.turma =  turmainclusao.codigo and registroaula.disciplina = inclusaodisciplinashistoricoforaprazo.disciplina ");
        sql.append(" and ((turmainclusao.anual and registroaula.ano = matriculaperiodoturmadisciplina.ano) "); 
        sql.append(" or (turmainclusao.semestral and registroaula.ano = matriculaperiodoturmadisciplina.ano and registroaula.semestre = matriculaperiodoturmadisciplina.semestre) ");
        sql.append(" or (turmainclusao.anual = false and turmainclusao.semestral =  false)) ");
        sql.append(" limit 1) as presente, ");
        sql.append(" (select horarioturmadiaitem.data as dataaula from horarioturmadia  inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
        sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo where horarioturmadiaitem.disciplina = disciplina.codigo ");
        sql.append(" and horarioturma.turma = turmainclusao.codigo ");
        sql.append(" and ((turmainclusao.anual and horarioturma.anovigente = matriculaperiodoturmadisciplina.ano) "); 
        sql.append(" or (turmainclusao.semestral and horarioturma.anovigente = matriculaperiodoturmadisciplina.ano and horarioturma.semestrevigente = matriculaperiodoturmadisciplina.semestre) ");
        sql.append(" or (turmainclusao.anual = false and turmainclusao.semestral =  false)) order by horarioturmadiaitem.data limit 1) as dataaula, ");
        sql.append(" negociacaorecebimento.data as datapagamento, ");
        sql.append(" contareceber.valorRecebido as valorTotalRecebimento, ");
        sql.append(" historico.situacao as situacaoHist,  ");
        sql.append(" unidadeensinoinclusao.nome as unidadeensinoinclusao ");
        return sql;    	
    }
    
    private StringBuilder realizarGeracaoFromBase(StringBuilder fromContaReceber, Boolean filtrarApenasComPagamento, Date dataInicioInclusao) {
    	StringBuilder sql =  new StringBuilder("");
    	sql.append(" from inclusaodisciplinashistoricoforaprazo");
        sql.append(" inner join inclusaohistoricoforaprazo on inclusaohistoricoforaprazo.codigo = inclusaodisciplinashistoricoforaprazo.inclusaohistoricoforaprazo ");
        sql.append(fromContaReceber);
        if(filtrarApenasComPagamento) {
        	sql.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
        	sql.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento  ");
        }else {
        	sql.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
        	sql.append(" left join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento  ");
        	if(Uteis.isAtributoPreenchido(dataInicioInclusao)) {
        		sql.append(" and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicioInclusao)).append(" 00:00:00'");
        	}
        }
        sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = inclusaohistoricoforaprazo.matriculaperiodo ");
        
        sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
        sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sql.append(" inner join turma as turmaorigem on turmaorigem.codigo = matriculaperiodo.turma ");
        sql.append(" inner join turma as turmainclusao on turmainclusao.codigo = inclusaodisciplinashistoricoforaprazo.turma ");
        sql.append(" inner join disciplina on disciplina.codigo = inclusaodisciplinashistoricoforaprazo.disciplina ");
        sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
        sql.append(" inner join unidadeensino as unidadeensinoinclusao on turmainclusao.unidadeensino = unidadeensinoinclusao.codigo ");
        sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.turma = turmainclusao.codigo ");
        sql.append(" and matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
        sql.append(" and matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
        sql.append(" and matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
        sql.append(" inner join historico on historico.matricula = matricula.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sql.append(" left join funcionario on funcionario.codigo = matricula.consultor ");
        //sql.append(" left join registroaula on (registroaula.turma =  inclusaodisciplinashistoricoforaprazo.turma and registroaula.disciplina = inclusaodisciplinashistoricoforaprazo.disciplina) ");
        //sql.append(" left join frequenciaaula on (frequenciaaula.registroaula = registroaula.codigo and frequenciaaula.matricula = matricula.matricula) ");
        return sql;    	
    }
    
    private StringBuilder realizarGeracaoCondicaoWhere(UnidadeEnsinoVO unidadeEnsinoVO, CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, Date dataInicio, Date dataFim, String tipo, Integer responsavel, Date dataAulaInicio, Date dataAulaFim, Date dataPgtoInicio, Date dataPgtoFim) {
    	StringBuilder sql =  new StringBuilder("");
    	 sql.append(" WHERE 1=1 ");
         if (unidadeEnsinoVO.getCodigo().intValue() != 0) {
             sql.append(" and matricula.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
         }
         if (curso.getCodigo().intValue() != 0) {
             sql.append(" and turmainclusao.curso = ").append(curso.getCodigo());
         }
         if (turma != null && turma.getCodigo() != 0) {
             sql.append(" and turmainclusao.codigo =").append(turma.getCodigo());
         }
         if (disciplina != null && disciplina.getCodigo() != 0) {
             sql.append(" and disciplina.codigo =").append(disciplina.getCodigo());
         }
         if (dataInicio != null) {
             sql.append(" and inclusaohistoricoforaprazo.datainclusao >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
         }
         if (dataFim != null) {
             sql.append(" and inclusaohistoricoforaprazo.datainclusao <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
         }
         if (tipo.equals("RE")) {
             sql.append(" and inclusaohistoricoforaprazo.reposicao = true ");
         }else {
        	 sql.append(" and inclusaohistoricoforaprazo.reposicao = false ");
         }
         if (responsavel != null ) {
         	if (responsavel > 0) {        		
         		sql.append(" and funcionario.codigo = ").append(responsavel);
         	}
         }
         if (dataAulaInicio != null || dataAulaFim != null) {
        	 sql.append(" and exists ( ");
        	 sql.append(" select horarioturmadiaitem.data as dataaula from horarioturmadia  inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
             sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo where horarioturmadiaitem.disciplina = disciplina.codigo ");
             sql.append(" and horarioturma.turma = turmainclusao.codigo ");
             sql.append(" and ").append(realizarGeracaoWherePeriodo(dataAulaInicio, dataAulaFim, "horarioturmadiaitem.data", false));
             sql.append(" and ((turmainclusao.anual and horarioturma.anovigente = matriculaperiodoturmadisciplina.ano) "); 
             sql.append(" or (turmainclusao.semestral and horarioturma.anovigente = matriculaperiodoturmadisciplina.ano and horarioturma.semestrevigente = matriculaperiodoturmadisciplina.semestre) ");
             sql.append(" or (turmainclusao.anual = false and turmainclusao.semestral =  false)) order by horarioturmadiaitem.data limit 1) ");
         }
         if (dataPgtoInicio != null || dataPgtoFim != null) {
        	 sql.append(" and ").append(realizarGeracaoWherePeriodo(dataPgtoInicio, dataPgtoFim, "negociacaorecebimento.data", false));         	
         }
         return sql;
    }

    private List<ReposicaoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<ReposicaoRelVO> reposicaoRelVOs = new ArrayList<ReposicaoRelVO>(0);
        while (tabelaResultado.next()) {
            reposicaoRelVOs.add(montarDados(tabelaResultado));
        }
        return reposicaoRelVOs;
    }

    private ReposicaoRelVO montarDados(SqlRowSet tabelaResultado) throws Exception {
        ReposicaoRelVO reposicaoRelVO = new ReposicaoRelVO();
        reposicaoRelVO.setUnidadeEnsino(tabelaResultado.getString("unidadeensino"));
        reposicaoRelVO.setMatricula(tabelaResultado.getString("matricula"));
        //reposicaoRelVO.setCurso(tabelaResultado.getString("disciplinanome"));
        reposicaoRelVO.setCodTurmaOrigem(tabelaResultado.getInt("codTurmaOrigem"));
        reposicaoRelVO.setTurmaOrigem(tabelaResultado.getString("turmaOrigem"));
        reposicaoRelVO.setCodTurmaInclusao(tabelaResultado.getInt("codTurmaInclusao"));
        reposicaoRelVO.setTurmaInclusao(tabelaResultado.getString("turmaInclusao"));
        reposicaoRelVO.setAluno(tabelaResultado.getString("nome"));
        reposicaoRelVO.setDisciplinaInclusao(tabelaResultado.getString("disciplinaInclusao"));
        reposicaoRelVO.setDataInclusao(tabelaResultado.getDate("dataInclusao"));
        reposicaoRelVO.setReposicao(tabelaResultado.getBoolean("reposicao"));
        reposicaoRelVO.setUnidadeInclusao(tabelaResultado.getString("unidadeensinoinclusao"));
        try {
            if (tabelaResultado.getBoolean("presente")) {
                reposicaoRelVO.setSituacao("Cursou");
            } else if (!tabelaResultado.getBoolean("presente")) {
                reposicaoRelVO.setSituacao("Faltou/Não Cursou");
            }
        } catch (Exception e) {
            reposicaoRelVO.setSituacao("Ainda Não Cursou");
        }
        reposicaoRelVO.setJustificativa(tabelaResultado.getString("justificativa"));
        reposicaoRelVO.setObservacao(tabelaResultado.getString("observacao"));
        reposicaoRelVO.setDesconto(tabelaResultado.getDouble("desconto"));
        reposicaoRelVO.setSituacaoHist(tabelaResultado.getString("situacaoHist"));
        reposicaoRelVO.setDataAula(tabelaResultado.getDate("dataAula"));
        reposicaoRelVO.setDataPagamento(tabelaResultado.getDate("dataPagamento"));
        reposicaoRelVO.setValorTotalRecebimento(tabelaResultado.getDouble("valorTotalRecebimento"));
        return reposicaoRelVO;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("ReposicaoInclusaoRel");
    }

    public static String getDesignIReportRelatorioLyout() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLyout() + ".jrxml");
    }

    public static String getDesignIReportRelatorioLayoutExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLayoutExcel() + ".jrxml");
    }

    public static String getIdEntidadeLyout() {
        return ("ReposicaoInclusaoRel");
    }

    public static String getIdEntidadeLayoutExcel() {
        return ("ReposicaoInclusaoRelExcel");
    }

    /*
     * (non-Javadoc)
     * @see relatorio.negocio.jdbc.academico.ReposicaoRelInterfaceFacade#getReposicaoRelVO()
     */
    public ReposicaoRelVO getReposicaoRelVO() {
        if (reposicaoRelVO == null) {
            reposicaoRelVO = new ReposicaoRelVO();
        }
        return reposicaoRelVO;
    }

    /*
     * (non-Javadoc)
     * @see relatorio.negocio.jdbc.academico.ReposicaoRelInterfaceFacade#setAlunosPorDisciplinasVO(relatorio.negocio .comuns.academico.ReposicaoRelVO)
     */
    public void setReposicaoRelVO(ReposicaoRelVO reposicaoRelVO) {
        this.reposicaoRelVO = reposicaoRelVO;
    }

    /*
     * (non-Javadoc)
     * @see relatorio.negocio.jdbc.academico.ReposicaoRelInterfaceFacade#getMatriculaPeriodoVO()
     */
    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if (matriculaPeriodoVO == null) {
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    /*
     * (non-Javadoc)
     * @see relatorio.negocio.jdbc.academico.ReposicaoRelInterfaceFacade#setMatriculaPeriodoVO(negocio.comuns. academico.MatriculaPeriodoVO)
     */
    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }
}
