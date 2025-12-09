package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.FiltroRelatorioProcessoSeletivoVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoesRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO;
import relatorio.negocio.comuns.processosel.ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO;
import relatorio.negocio.interfaces.processosel.ProcSeletivoInscricoesRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ProcSeletivoInscricoesRel extends SuperRelatorio implements ProcSeletivoInscricoesRelInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7114578099885982556L;
	protected static String idEntidade;

    public ProcSeletivoInscricoesRel() {
        setIdEntidade("ProcessoSeletivoInscricoesRel");
    }

    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#montarDadosProcessoSeletivo()
     */
    public void montarDadosProcessoSeletivo(ProcessoSeletivoInscricoesRelVO processoSeletivoInscricoesRelVO, Integer codigoProcessoSeletivo) throws Exception {
        ProcSeletivoVO obj = getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(codigoProcessoSeletivo, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
        processoSeletivoInscricoesRelVO.setDescricao(obj.getDescricao().toUpperCase());
        processoSeletivoInscricoesRelVO.setDocumentacaoObrigatoria(obj.getDocumentaoObrigatoria());
        processoSeletivoInscricoesRelVO.setHorarioProva(obj.getHorarioProva());
        processoSeletivoInscricoesRelVO.setRequisitosGerais(obj.getRequisitosGerais());
        processoSeletivoInscricoesRelVO.setDataFim(obj.getDataFim());
        processoSeletivoInscricoesRelVO.setDataInicio(obj.getDataInicio());
        processoSeletivoInscricoesRelVO.setDataProva(obj.getDataProva());
        processoSeletivoInscricoesRelVO.setNivelEducacional(obj.getNivelEducacional());
        processoSeletivoInscricoesRelVO.setCodigo(obj.getCodigo());
        processoSeletivoInscricoesRelVO.setMediaMinimaAprovacao(obj.getMediaMinimaAprovacao());
    }

    public List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> emitirRelatorioSintetico(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO) throws Exception {
        return emitirRelatorioSintetico(procSeletivoVO, unidadeEnsino, sala, itemProcSeletivoDataProvaVO, "AM", SituacaoInscricaoEnum.ATIVO, false, null);
    }
	
    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#emitirRelatorio()
     */
    public List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> emitirRelatorioSintetico(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws Exception {
        validarDados(procSeletivoVO); // valida os dados
        //ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO
        return executarConsultaParametrizadaCandidatoSintetico(procSeletivoVO.getCodigo(), unidadeEnsino, sala, itemProcSeletivoDataProvaVO, situacao, situacaoInscricao, filtrarSomenteInscricoesIsentas, filtroRelatorioProcessoSeletivoVO);

    }

    public List<ProcessoSeletivoInscricoesRelVO> emitirRelatorio(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer unidadeEnsinoCurso, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, Integer sala, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws Exception {
        validarDados(procSeletivoVO); // valida os dados
        ProcessoSeletivoInscricoesRelVO processoSeletivoInscricoesRelVO = new ProcessoSeletivoInscricoesRelVO();
        ProcSeletivoInscricoesRel.emitirRelatorio(getIdEntidade(), false, null); // valida permissao, e obtem conexao
        montarDadosProcessoSeletivo(processoSeletivoInscricoesRelVO, procSeletivoVO.getCodigo());
        // Executando as consultas e preenchendo os VOs
        executarConsultaParametrizadaUnidadeEnsinoCursoTurno(procSeletivoVO.getCodigo(), unidadeEnsino, unidadeEnsinoCurso, processoSeletivoInscricoesRelVO);
        for (ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO : processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO()) {
            executarConsultaParametrizadaCandidato(unidadeEnsinoRelVO, procSeletivoVO.getCodigo(), sala, itemProcSeletivoDataProvaVO, situacao, situacaoInscricao, filtrarSomenteInscricoesIsentas, filtroRelatorioProcessoSeletivoVO);
            unidadeEnsinoRelVO.setQtdeInscritos(executarConsultaQuantidadeInscritosProcessoSeletivo(unidadeEnsinoRelVO, procSeletivoVO.getCodigo(), sala, itemProcSeletivoDataProvaVO, situacao, situacaoInscricao, filtrarSomenteInscricoesIsentas, filtroRelatorioProcessoSeletivoVO));
            if (unidadeEnsinoRelVO.getQtdeVagas().doubleValue() > 0) {
            	unidadeEnsinoRelVO.setQtdeCandPorVaga(Uteis.arrendondarForcando2CadasDecimais(unidadeEnsinoRelVO.getQtdeConfirmados().doubleValue() / unidadeEnsinoRelVO.getQtdeVagas().doubleValue()));            	
            }            
        }
        List<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> listaFinal = new ArrayList<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO>(0);
        for (ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO : processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO()) {
            if (!unidadeEnsinoRelVO.getProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO().isEmpty()) {
                if (!unidadeEnsinoRelVO.getProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO().isEmpty()) {
                	listaFinal.add(unidadeEnsinoRelVO);                	
                }            	
            }
        }
        processoSeletivoInscricoesRelVO.setProcessoSeletivoInscricoes_UnidadeEnsinoRelVO(listaFinal);
        processoSeletivoInscricoesRelVO.setProcessoSeletivoInscricoes_ExtratoRel(listaFinal);		
        // Retornando o list final de VOs
        List<ProcessoSeletivoInscricoesRelVO> listaResultado = new ArrayList<ProcessoSeletivoInscricoesRelVO>(0);
        if (listaFinal.isEmpty()) {
        	return listaResultado;
        }
        listaResultado.add(processoSeletivoInscricoesRelVO);
        return listaResultado;
    }

    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#validarDados()
     */
    public void validarDados(ProcSeletivoVO obj) throws ConsistirException {
        if (obj == null || obj.getCodigo() == 0) {
            throw new ConsistirException("O campo Processo Seletivo deve ser informado.");
        }
    }

    private void executarConsultaParametrizadaUnidadeEnsinoCursoTurno(Integer procSeletivo, Integer unidadeEnsino, Integer unidadeEnsinoCurso, ProcessoSeletivoInscricoesRelVO processoSeletivoInscricoesRelVO) throws SQLException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" Select uec.codigo as unidadeensinocurso_codigo, ue.codigo as unidadeEnsino_codigo, ue.nome as unidadeensino_nome, c.nome as curso_nome, t.nome as turno_nome ");
        sql.append(" From ProcSeletivo ps	");
        sql.append(" inner join ProcSeletivoUnidadeEnsino psue on psue.procSeletivo = ps.codigo ");
        sql.append(" inner join ProcSeletivoCurso psc on psc.procSeletivoUnidadeEnsino = psue.codigo ");
        sql.append(" inner join UnidadeEnsinoCurso uec on uec.codigo = psc.unidadeEnsinoCurso ");
        sql.append(" inner join UnidadeEnsino ue on ue.codigo = uec.unidadeEnsino ");
        sql.append(" inner join Curso c on uec.curso = c.codigo ");
        sql.append(" inner join Turno t on uec.turno = t.codigo ");
        sql.append(" where 1=1 ");
        // Filtro obrigatório: ProcessoSeletivo
        sql.append(" and ps.codigo = " + procSeletivo.intValue());
        // Filtro opcional: UnidadeEnsino
        if (unidadeEnsino != 0) {

            sql.append(" and ue.codigo = " + unidadeEnsino.intValue());
        }
        // Filtro opcional: Curso
        if (unidadeEnsinoCurso != null && unidadeEnsinoCurso != 0) {
            sql.append(" and uec.codigo = " + unidadeEnsinoCurso.intValue());
        }
        sql.append(" group by  unidadeensinocurso_codigo, unidadeensino_nome, curso_nome, turno_nome, unidadeEnsino_codigo ");
        sql.append(" order by ue.nome, c.nome, t.nome ");
        
        processoSeletivoInscricoesRelVO.setProcessoSeletivoInscricoes_UnidadeEnsinoRelVO(montarDadosUnidadeEnsinoCursoTurno(getConexao().getJdbcTemplate().queryForRowSet(sql.toString())));
        processoSeletivoInscricoesRelVO.setProcessoSeletivoInscricoes_ExtratoRel(processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO());
    }

    private void executarConsultaParametrizadaCandidato(ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO, Integer procSeletivo, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws SQLException, Exception {
        StringBuilder sql = new StringBuilder();

        sql.append(" Select Inscricao.codigo as codigo, Inscricao.situacao, Inscricao.situacaoInscricao, ItemProcSeletivoDataProva.dataProva as dataProva, sala.sala, Inscricao.inscricaoPresencial as inscricaoPresencial, Pessoa.cpf as cpf, Pessoa.nome as nome, contareceber.situacao as situacaoCR ");
        sql.append(" From Inscricao ");
        sql.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala ");
        sql.append(" inner join pessoa on pessoa.codigo = inscricao.candidato ");
        sql.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
        if (unidadeEnsinoRelVO.getUnidadeEnsinoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoId().equals(0)) {
            sql.append(" inner join unidadeensino on unidadeensino.codigo = inscricao.unidadeensino ");
        }
        if (unidadeEnsinoRelVO.getUnidadeEnsinoCursoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoCursoId().equals(0)) {
            sql.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 ");
        }
        
        
        sql.append(" inner join ItemProcSeletivoDataProva on ");
        sql.append("  ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
        if(itemProcSeletivoDataProvaVO != null && itemProcSeletivoDataProvaVO.getCodigo() != null && itemProcSeletivoDataProvaVO.getCodigo()>0){
        	sql.append(" and ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        }
        
        sql.append(" left join contareceber on contareceber.codigo = inscricao.contareceber ");
		
        sql.append(" where ProcSeletivo.codigo = " + procSeletivo.intValue());
        if(sala != null && sala>=0){
        	if(sala == 0){
        		sql.append(" and Inscricao.sala is null ");
        	}else{
        		sql.append(" and sala.codigo = ").append(sala);
        	}
        }
        if (unidadeEnsinoRelVO.getUnidadeEnsinoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoId().equals(0)) {
            sql.append(" and UnidadeEnsino.codigo = " + unidadeEnsinoRelVO.getUnidadeEnsinoId());
        }
        if (unidadeEnsinoRelVO.getUnidadeEnsinoCursoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoCursoId().equals(0)) {
            sql.append(" and UnidadeEnsinoCurso.codigo = " + unidadeEnsinoRelVO.getUnidadeEnsinoCursoId());
        }
        if (!situacao.equals("AM")) {
        	sql.append(" and inscricao.situacao = '" + situacao + "'");
        }
        if (situacaoInscricao != null) {
        	sql.append(" and inscricao.situacaoInscricao = '" + situacaoInscricao.toString() + "'");
        } else if (filtroRelatorioProcessoSeletivoVO != null){
        	sql.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivoVO, "inscricao"));
        }
        
        if (filtrarSomenteInscricoesIsentas) {
            sql.append(" and (((contareceber.situacao = 'RE') and (contareceber.valorrecebido = 0)) or");
            sql.append("       (contareceber.situacao = 'CF'))");
        }
        
        sql.append(" order by nome, ItemProcSeletivoDataProva.dataProva");
        
        unidadeEnsinoRelVO.setProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO(montarDadosCandidato(getConexao().getJdbcTemplate().queryForRowSet(sql.toString())));
    }
    
    private List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> executarConsultaParametrizadaCandidatoSintetico(Integer procSeletivo, Integer unidadeEnsino, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws SQLException, Exception {
        StringBuilder sql = new StringBuilder();

        sql.append(" Select Inscricao.codigo as codigo, ItemProcSeletivoDataProva.dataProva as dataProva, sala.sala, Inscricao.inscricaoPresencial as inscricaoPresencial, Pessoa.cpf as cpf, Pessoa.nome as nome ");
        sql.append(" From Inscricao ");
        sql.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala ");
        sql.append(" inner join pessoa on pessoa.codigo = inscricao.candidato ");
        sql.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
        sql.append(" inner join ProcSeletivoUnidadeEnsino on procseletivo.codigo = ProcSeletivoUnidadeEnsino.procseletivo  ");
        sql.append(" inner join procSeletivoCurso on procSeletivoCurso.ProcSeletivoUnidadeEnsino = ProcSeletivoUnidadeEnsino.codigo ");
        sql.append(" inner join unidadeensino on unidadeensino.codigo = inscricao.unidadeensino ");
        sql.append(" inner join unidadeensinocurso on unidadeensinocurso.codigo = inscricao.cursoopcao1 and  inscricao.cursoopcao1 = procSeletivoCurso.unidadeEnsinoCurso ");
        sql.append(" inner join ItemProcSeletivoDataProva on ");
        sql.append("  ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
        if(itemProcSeletivoDataProvaVO != null && itemProcSeletivoDataProvaVO.getCodigo() != null && itemProcSeletivoDataProvaVO.getCodigo()>0){
        	sql.append(" and ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        }
        sql.append(" left join contareceber on contareceber.codigo = inscricao.contareceber ");
		
        sql.append(" where ProcSeletivo.codigo = " + procSeletivo.intValue());
        if (sala != null && sala>=0) {
        	if(sala == 0){
        		sql.append(" and Inscricao.sala is null ");
        	}else{
        		sql.append(" and sala.codigo = ").append(sala);
        	}
        }
        if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
            sql.append(" and UnidadeEnsino.codigo = " + unidadeEnsino);
        }
        if (!situacao.equals("AM")) {
        	sql.append(" and inscricao.situacao = '" + situacao + "' ");
        }            
        if (situacaoInscricao != null) {
        	sql.append(" and inscricao.situacaoInscricao = '" + situacaoInscricao.toString() + "'");
        } else if (filtroRelatorioProcessoSeletivoVO != null){
        	sql.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivoVO, "inscricao"));
        }
        if (filtrarSomenteInscricoesIsentas) {
            sql.append(" and (((contareceber.situacao = 'RE') and (contareceber.valorrecebido = 0)) or");
            sql.append("       (contareceber.situacao = 'CF'))");
        }
        
        sql.append(" order by Pessoa.nome, ItemProcSeletivoDataProva.dataProva");
        return montarDadosCandidatoSintetico(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }

    private Integer executarConsultaQuantidadeInscritosProcessoSeletivo(ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO, 
            Integer procSeletivo, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, String situacao, SituacaoInscricaoEnum situacaoInscricao, Boolean filtrarSomenteInscricoesIsentas, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO) throws SQLException, Exception {
        StringBuilder sql = new StringBuilder();

        sql.append(" Select count(Inscricao.codigo) AS qtdeInscritos, ");
        sql.append(" numerovaga, sum( case when inscricao.situacao = 'CO' then 1 else 0 end ) as confirmados, sum( case when inscricao.situacao <> 'CO' then 1 else 0 end ) as naoConfirmados ");  
        sql.append(" From Inscricao ");  
        sql.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala ");
        sql.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
        sql.append(" inner join ItemProcSeletivoDataProva on ");
        sql.append("  ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
        if(itemProcSeletivoDataProvaVO != null && itemProcSeletivoDataProvaVO.getCodigo() != null && itemProcSeletivoDataProvaVO.getCodigo()>0){
        	sql.append(" and ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        }
        sql.append(" inner join procseletivounidadeensino on procseletivounidadeensino.procseletivo = procseletivo.codigo ");
        sql.append(" inner join procseletivocurso on procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo and procseletivocurso.unidadeensinocurso = inscricao.cursoopcao1 ");
		
        sql.append(" left join contareceber on contareceber.codigo = inscricao.contareceber ");
        sql.append(" where ProcSeletivo.codigo = " + procSeletivo.intValue());
        if (unidadeEnsinoRelVO.getUnidadeEnsinoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoId().equals(0)) {
            sql.append(" and Inscricao.unidadeEnsino = " + unidadeEnsinoRelVO.getUnidadeEnsinoId());
        }
        if (sala != null && sala>=0) {
        	if(sala == 0){
        		sql.append(" and Inscricao.sala is null ");
        	}else{
        		sql.append(" and sala.codigo = ").append(sala);
        	}
        }
        if (unidadeEnsinoRelVO.getUnidadeEnsinoCursoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoCursoId().equals(0)) {
            sql.append(" and Inscricao.cursoOpcao1 = " + unidadeEnsinoRelVO.getUnidadeEnsinoCursoId());
        }
        if (!situacao.equals("AM")) {
        	sql.append(" and inscricao.situacao = '" + situacao + "' ");
        }        
        if (situacaoInscricao != null) {
        	sql.append(" and inscricao.situacaoInscricao = '" + situacaoInscricao.toString() + "'");
        } else if (filtroRelatorioProcessoSeletivoVO != null){
        	sql.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivoVO, "inscricao"));
        }
        if (filtrarSomenteInscricoesIsentas) {
            sql.append(" and (((contareceber.situacao = 'RE') and (contareceber.valorrecebido = 0)) or");
            sql.append("       (contareceber.situacao = 'CF'))");
        } 
        sql.append(" group by numerovaga ");        
        SqlRowSet rs = (getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
        if (rs.next()) {
        	unidadeEnsinoRelVO.setQtdeInscritos(rs.getInt("qtdeInscritos"));
        	unidadeEnsinoRelVO.setQtdeVagas(rs.getInt("numerovaga"));
        	unidadeEnsinoRelVO.setQtdeConfirmados(rs.getInt("confirmados"));
        	unidadeEnsinoRelVO.setQtdeInscNaoConfirmados(rs.getInt("naoConfirmados"));		
            return rs.getInt("qtdeInscritos");
        } else {
            return 0;
        }
    }

    /**
     * Monta o VO ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO Este VO Será um subrelatorio, e nele sera informado os
     * nomes de UnidadeEnsino, Curso ,Turno.
     * 
     * @return
     * @throws java.lang.Exception
     */
    private List<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> montarDadosUnidadeEnsinoCursoTurno(SqlRowSet resultadoConsulta) throws Exception {
        List<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> listaConsulta = new ArrayList<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO>(0);
        while (resultadoConsulta.next()) {
            ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO obj = new ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO();
            obj.setUnidadeEnsino(resultadoConsulta.getString("unidadeensino_nome").toUpperCase());
            obj.setCurso(resultadoConsulta.getString("curso_nome").toUpperCase());
            obj.setTurno(resultadoConsulta.getString("turno_nome").toUpperCase());
            obj.setUnidadeEnsinoCursoId(resultadoConsulta.getInt("unidadeensinocurso_codigo"));
            obj.setUnidadeEnsinoId(resultadoConsulta.getInt("unidadeEnsino_codigo"));
            listaConsulta.add(obj);
        }
        return listaConsulta;
    }

    private List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> montarDadosCandidato(SqlRowSet resultadoConsulta) throws Exception {
        List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> listaConsulta = new ArrayList<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO>(0);
        while (resultadoConsulta.next()) {
            ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO obj = new ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO();
            obj.setCpf(resultadoConsulta.getString("cpf"));
            obj.setNome(resultadoConsulta.getString("nome").toUpperCase());
            obj.setInscricao(resultadoConsulta.getInt("codigo"));
            obj.setSala(resultadoConsulta.getString("sala"));
            obj.setInscricaoPresencial(resultadoConsulta.getBoolean("inscricaoPresencial"));
            obj.setDataProva(resultadoConsulta.getDate("dataProva"));
            obj.setSituacaoCR(resultadoConsulta.getString("situacaoCR"));
            obj.setSituacao(resultadoConsulta.getString("situacao"));
            obj.setSituacaoInscricao(resultadoConsulta.getString("situacaoInscricao"));
            listaConsulta.add(obj);
        }
        return listaConsulta;
    }

    private List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> montarDadosCandidatoSintetico(SqlRowSet resultadoConsulta) throws Exception {
        List<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO> listaConsulta = new ArrayList<ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO>(0);
        while (resultadoConsulta.next()) {
            ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO obj = new ProcessoSeletivoInscricoes_UnidadeEnsino_InscricaoRelVO();
            obj.setCpf(resultadoConsulta.getString("cpf"));
            obj.setNome(resultadoConsulta.getString("nome").toUpperCase());
            obj.setInscricao(resultadoConsulta.getInt("codigo"));
            obj.setSala(resultadoConsulta.getString("sala"));
            obj.setDataProva(resultadoConsulta.getDate("dataProva"));
            listaConsulta.add(obj);
        }
        return listaConsulta;
    }

    public List<UnidadeEnsinoCursoVO> consultarCurso(String campoConsultaCurso, String valorConsultaCurso, Integer unidadeEnsino, Integer procSeletivo, UsuarioVO usuarioVO) throws Exception {
        List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
        if (campoConsultaCurso.equals("nome")) {
        	StringBuilder sql = new StringBuilder("");
        	sql.append(" select curso.codigo, curso.nome from curso ");
        	sql.append(" where sem_acentos(curso.nome) ilike sem_acentos(?) ");
        	if(Uteis.isAtributoPreenchido(unidadeEnsino) || Uteis.isAtributoPreenchido(procSeletivo)) {        		        		
        		sql.append(" and exists (select unidadeEnsinoCurso.codigo from procseletivounidadeensino inner join procseletivocurso on procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo ");
        		sql.append(" inner join unidadeEnsinoCurso on procseletivocurso.unidadeEnsinoCurso = unidadeEnsinoCurso.codigo ");
        		sql.append(" where unidadeEnsinoCurso.curso = curso.codigo ");
        		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        			sql.append(" and unidadeEnsinoCurso.unidadeEnsino = ").append(unidadeEnsino).append(" ");
        		}
        		if(Uteis.isAtributoPreenchido(procSeletivo)) {      
        			sql.append(" and procseletivounidadeensino.procseletivo = ").append(procSeletivo).append(" ");
        		}
        		sql.append(" limit 1) ");
        	}
    		sql.append(" order by 2 ");
              SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsultaCurso+"%");
              while(rs.next()) {
            	  UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
            	  unidadeEnsinoCursoVO.getCurso().setCodigo(rs.getInt("codigo"));
            	  unidadeEnsinoCursoVO.getCurso().setNome(rs.getString("nome"));
            	  objs.add(unidadeEnsinoCursoVO);
              }
        }
        return objs;
    }


    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#getDesignIReportRelatorio()
     */
    public String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoInscricoesRel.jrxml");
    }

    public String getDesignIReportRelatorioSintetico() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoInscricoesSinteticoRel.jrxml");
    }

    public String getDesignIReportRelatorioQuantitativo() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + "ProcessoSeletivoInscricoesQuantitativoRel.jrxml");
    }

    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#getCaminhoBaseRelatorio()
     */
    public String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
    }

    public static String getIdEntidade() {
        return ProcSeletivoInscricoesRel.idEntidade;
    }

    /*
     * (non-Javadoc)
     * 
     * @see relatorio.negocio.jdbc.processosel.ProcSeletivoInscricoesRelInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        ProcSeletivoInscricoesRel.idEntidade = idEntidade;
    }

    public void inicializarParametros() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public List<ProcessoSeletivoInscricoesRelVO> emitirRelatorioQuantitativo(ProcSeletivoVO procSeletivoVO, Integer unidadeEnsino, Integer unidadeEnsinoCurso, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, Integer sala, String situacao, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, Boolean filtrarSomenteInscricoesIsentas) throws Exception {
        validarDados(procSeletivoVO);
        ProcessoSeletivoInscricoesRelVO processoSeletivoInscricoesRelVO = new ProcessoSeletivoInscricoesRelVO();
        ProcSeletivoInscricoesRel.emitirRelatorio(getIdEntidade(), false, null);
        montarDadosProcessoSeletivo(processoSeletivoInscricoesRelVO, procSeletivoVO.getCodigo());
        executarConsultaParametrizadaUnidadeEnsinoCursoTurno(procSeletivoVO.getCodigo(), unidadeEnsino, unidadeEnsinoCurso, processoSeletivoInscricoesRelVO);
        for (ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO : processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO()) {
            unidadeEnsinoRelVO.setQtdeInscritos(executarConsultaQuantidadeInscritosProcessoSeletivoParaRelatorioQuantitativo(unidadeEnsinoRelVO, procSeletivoVO.getCodigo(), sala, itemProcSeletivoDataProvaVO, situacao, filtroRelatorioProcessoSeletivoVO, filtrarSomenteInscricoesIsentas));
        }
		for (Iterator<ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO> iterator = processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO().iterator(); iterator.hasNext();) {
			ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO = (ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO) iterator.next();
			if (unidadeEnsinoRelVO.getQtdeInscritos().equals(0)) {
				iterator.remove();
			}
		}        
        processoSeletivoInscricoesRelVO.setProcessoSeletivoInscricoes_ExtratoRel(processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO());		
        List<ProcessoSeletivoInscricoesRelVO> listaResultado = new ArrayList<ProcessoSeletivoInscricoesRelVO>(0);
        if (processoSeletivoInscricoesRelVO.getProcessoSeletivoInscricoes_UnidadeEnsinoRelVO().isEmpty()) {
        	return listaResultado;
        }
        listaResultado.add(processoSeletivoInscricoesRelVO);
        return listaResultado;
    }
    
    private Integer executarConsultaQuantidadeInscritosProcessoSeletivoParaRelatorioQuantitativo(ProcessoSeletivoInscricoes_UnidadeEnsinoRelVO unidadeEnsinoRelVO, Integer procSeletivo, Integer sala, ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVO, String situacao, FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, Boolean filtrarSomenteInscricoesIsentas) throws SQLException, Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(" Select count(Inscricao.codigo) AS qtdeInscritos, ");
        sql.append(" numerovaga, sum( case when inscricao.situacao = 'CO' and inscricao.situacaoInscricao = '").append(SituacaoInscricaoEnum.ATIVO.name()).append("'  then 1 else 0 end ) as confirmados, ");
        sql.append(" case when numerovaga is null or numerovaga = 0 then 0 else ((select count(ins.codigo) from inscricao ins where ins.situacao = 'CO' and ins.situacaoInscricao in ( 'ATIVO', 'NAO_COMPARECEU') ");
        sql.append(" and ins.ProcSeletivo = " + procSeletivo.intValue());
        if (unidadeEnsinoRelVO.getUnidadeEnsinoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoId().equals(0)) {
            sql.append(" and ins.unidadeEnsino = " + unidadeEnsinoRelVO.getUnidadeEnsinoId());
        }
        if (sala != null && sala>=0) {
        	if(sala == 0){
        		sql.append(" and ins.sala is null ");
        	}else{
        		sql.append(" and sala.codigo = ").append(sala);
        	}
        }
        if (unidadeEnsinoRelVO.getUnidadeEnsinoCursoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoCursoId().equals(0)) {
            sql.append(" and ins.cursoOpcao1 = " + unidadeEnsinoRelVO.getUnidadeEnsinoCursoId());
        }
        if (!situacao.equals("AM")) {
        	sql.append(" and ins.situacao = '" + situacao + "' ");
        }
        sql.append(" )::NUMERIC(20,2) / numerovaga::NUMERIC(20,2))::NUMERIC(20,2) end as qtdeCandPorVaga, ");
        sql.append(" sum( case when inscricao.situacaoInscricao = '").append(SituacaoInscricaoEnum.CANCELADO.name()).append("' then 1 else 0 end ) as cancelado, ");
        sql.append(" sum( case when inscricao.situacaoInscricao = '").append(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO.name()).append("' then 1 else 0 end ) as canceladoOutraInscricao, ");
        sql.append(" sum( case when inscricao.situacao = 'CO' and inscricao.situacaoInscricao = '").append(SituacaoInscricaoEnum.NAO_COMPARECEU.name()).append("' then 1 else 0 end ) as naocompareceu, ");
        sql.append(" sum( case when inscricao.situacao <> 'CO' and inscricao.situacaoInscricao = '").append(SituacaoInscricaoEnum.ATIVO.name()).append("' then 1 else 0 end ) as naoConfirmados ");  
        sql.append(" From Inscricao ");  
        sql.append(" left join SalaLocalAula sala on sala.codigo = inscricao.sala ");
        sql.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
        sql.append(" inner join ItemProcSeletivoDataProva on ");
        sql.append("  ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva ");
        if(itemProcSeletivoDataProvaVO != null && itemProcSeletivoDataProvaVO.getCodigo() != null && itemProcSeletivoDataProvaVO.getCodigo()>0){
        	sql.append(" and ItemProcSeletivoDataProva.codigo = ").append(itemProcSeletivoDataProvaVO.getCodigo());
        }
        sql.append(" inner join procseletivounidadeensino on procseletivounidadeensino.procseletivo = procseletivo.codigo ");
        sql.append(" inner join procseletivocurso on procseletivocurso.procseletivounidadeensino = procseletivounidadeensino.codigo and procseletivocurso.unidadeensinocurso = inscricao.cursoopcao1 ");
        sql.append(" left join contareceber on contareceber.codigo = inscricao.contareceber ");
        sql.append(" where ProcSeletivo.codigo = " + procSeletivo.intValue());
        if (unidadeEnsinoRelVO.getUnidadeEnsinoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoId().equals(0)) {
            sql.append(" and Inscricao.unidadeEnsino = " + unidadeEnsinoRelVO.getUnidadeEnsinoId());
        }
        if (sala != null && sala>=0) {
        	if(sala == 0){
        		sql.append(" and Inscricao.sala is null ");
        	}else{
        		sql.append(" and sala.codigo = ").append(sala);
        	}
        }
        if (unidadeEnsinoRelVO.getUnidadeEnsinoCursoId() != null && !unidadeEnsinoRelVO.getUnidadeEnsinoCursoId().equals(0)) {
            sql.append(" and Inscricao.cursoOpcao1 = " + unidadeEnsinoRelVO.getUnidadeEnsinoCursoId());
        }
        if (!situacao.equals("AM")) {
        	sql.append(" and inscricao.situacao = '" + situacao + "' ");
        }        
        sql.append("  ").append(adicionarFiltroRelatorioProcessoSeletivo(filtroRelatorioProcessoSeletivoVO, "inscricao"));
        if (filtrarSomenteInscricoesIsentas) {
            sql.append(" and (((contareceber.situacao = 'RE') and (contareceber.valorrecebido = 0)) or");
            sql.append("       (contareceber.situacao = 'CF'))");
        } 
        sql.append(" group by numerovaga ");        
        SqlRowSet rs = (getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
        if (rs.next()) {
        	unidadeEnsinoRelVO.setQtdeVagas(rs.getInt("numerovaga"));
        	unidadeEnsinoRelVO.setQtdeConfirmados(rs.getInt("confirmados"));
			unidadeEnsinoRelVO.setQtdeInscNaoConfirmados(rs.getInt("naoConfirmados"));
			unidadeEnsinoRelVO.setQtdeInscCanceladaOutraInscricao(rs.getInt("canceladoOutraInscricao"));
			unidadeEnsinoRelVO.setQtdeInscCancelada(rs.getInt("cancelado"));
			unidadeEnsinoRelVO.setQtdeInscNaoCompareceu(rs.getInt("naocompareceu"));
        	unidadeEnsinoRelVO.setQtdeCandPorVaga(rs.getDouble("qtdeCandPorVaga"));
            return rs.getInt("qtdeInscritos");
        } else {
            return 0;
        }
    }
    
    public StringBuilder adicionarFiltroRelatorioProcessoSeletivo(FiltroRelatorioProcessoSeletivoVO filtroRelatorioProcessoSeletivoVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
		keyEntidade = keyEntidade.trim();
		if (filtroRelatorioProcessoSeletivoVO.getAtivo() || filtroRelatorioProcessoSeletivoVO.getCancelado() || filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao() || filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
			sqlStr.append(" and ").append(keyEntidade).append(".situacaoInscricao in (");
			boolean virgula = false;
			if (filtroRelatorioProcessoSeletivoVO.getAtivo()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.ATIVO).append("'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getCancelado()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.CANCELADO).append("'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getCanceladoOutraInscricao()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO).append("'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getNaoCompareceu()) {
				sqlStr.append(virgula ? "," : "").append("'").append(SituacaoInscricaoEnum.NAO_COMPARECEU).append("'");
				virgula = true;
			}
			sqlStr.append(" ) ");
		}
		if (filtroRelatorioProcessoSeletivoVO.getConfirmado() || filtroRelatorioProcessoSeletivoVO.getPendenteFinanceiro()) {
			sqlStr.append(" and ").append(keyEntidade).append(".situacao in ( ");
			boolean virgula = false;
			if (filtroRelatorioProcessoSeletivoVO.getConfirmado()) {
				sqlStr.append(virgula ? "," : "").append("'CO'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getPendenteFinanceiro()) {
				sqlStr.append(virgula ? "," : "").append("'PF'");
				virgula = true;
			}
			sqlStr.append(" ) ");
		} 
		
		if (filtroRelatorioProcessoSeletivoVO.getProcessoSeletivo() || filtroRelatorioProcessoSeletivoVO.getPortadorDiploma() || filtroRelatorioProcessoSeletivoVO.getEnem()) {
			sqlStr.append(" and ").append(keyEntidade).append(".formaIngresso in (");
			boolean virgula = false;
			if (filtroRelatorioProcessoSeletivoVO.getProcessoSeletivo()) {
				sqlStr.append(virgula ? "," : "").append("'PS'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getPortadorDiploma()) {
				sqlStr.append(virgula ? "," : "").append("'PD'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getEnem()) {
				sqlStr.append(virgula ? "," : "").append("'EN'");
				virgula = true;
			}
			if (filtroRelatorioProcessoSeletivoVO.getTransferencia()) {
				sqlStr.append(virgula ? "," : "").append("'TR'");
				virgula = true;
			}
			sqlStr.append(virgula ? "," : "").append("''");
			sqlStr.append(" ) ");
		}
		if(!sqlStr.toString().isEmpty()){
			return sqlStr; 
		}
		return new StringBuilder(" and 1=1 ");
	}
}
