package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.financeiro.CondicoesPagamentoRelVO;
import relatorio.negocio.interfaces.financeiro.CondicoesPagamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import relatorio.parametroRelatorio.academico.CondicoesPagamentoSuperParametroRelVO;

/**
 * @author Danilo
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class CondicoesPagamentoRel extends SuperRelatorio implements CondicoesPagamentoRelInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8922208485328680784L;

	public CondicoesPagamentoRel() {
        inicializarParametros();
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.AlunosPorUnidadeCursoTurmaRelInterfaceFacade#inicializarParametros()
     */
    public void inicializarParametros() {
        //setCondicoesPagamentoRelVO(new CondicoesPagamentoRelVO());
    }

    @Override
    public void validarDados(Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino, UnidadeEnsinoCursoVO unidadeEnsinoCurso, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String ano, String semestre) throws ConsistirException {
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
        }
        if (Uteis.nrDiasEntreDatas(dataInicio, dataFim) > 31) {
        	throw new ConsistirException("O período informado entre as datas não pode ser superior a 31 dias.");
        }
        if(!filtroRelatorioAcademicoVO.getPeriodicidadeEnum().isIntegral() && (!Uteis.isAtributoPreenchido(ano) || ano.length() != 4 )) {
        	throw new ConsistirException("O campo ANO deve ser informado com 4 dígitos.");
        }
        if(filtroRelatorioAcademicoVO.getPeriodicidadeEnum().equals(PeriodicidadeEnum.SEMESTRAL) 
        		&& (!Uteis.isAtributoPreenchido(semestre) || (!semestre.equals("1") && !semestre.equals("2")) )) {        	
        	throw new ConsistirException("O campo SEMESTRE deve ser informado.");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seerelatorio.negocio.jdbc.academico.AlunosPorUnidadeCursoTurmaRelInterfaceFacade#criarObjeto(negocio.comuns.
     * administrativo.UnidadeEnsinoVO, negocio.comuns.academico.CursoVO, negocio.comuns.academico.TurmaVO,
     * java.lang.String, java.lang.String)
     */
    public List<CondicoesPagamentoRelVO> criarObjeto(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, UnidadeEnsinoCursoVO unidadeEnsinoCurso, MatriculaVO matricula, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO, String tipoLayout, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
        CondicoesPagamentoRel.emitirRelatorio(getIdEntidade(), false, usuarioVO); // valida permissao, e obtem conexao
        List<CondicoesPagamentoRelVO> listaRelatorio = new ArrayList<CondicoesPagamentoRelVO>(0);
        // Executando as consultas e preenchendo os VOs
        if (tipoLayout.equals("AL")) {
        	listaRelatorio = executarConsultaCondicoesPagamento(dataInicio, dataFim, ano, semestre, unidadeEnsino, curso, turma, unidadeEnsinoCurso, matricula, condicoesPagamentoSuperParametroRelVO, filtroRelatorioAcademicoVO);
        } else {
        	listaRelatorio = executarConsultaCondicoesPagamentoLayout2(dataInicio, dataFim, ano, semestre, unidadeEnsino, curso, turma, unidadeEnsinoCurso, matricula, condicoesPagamentoSuperParametroRelVO, filtroRelatorioAcademicoVO);
        }
        
        return listaRelatorio;
    }

    /**
     * Relatório de condições da pagamentos dos alunos de um determinado curso ou turma. 
     * Neste relatório o usuário deverá escolher a unidade de ensino, o curso e qual a turma desejado. 
     * O filtro de turma é opcional, se o usuário não informar, relatório trará a listagem dos alunos 
     * matriculados no curso. Quando o relatório for gerado deverá aparecer as seguintes 
     * colunas: mátricula, nome do aluno, situação, quantidade de parcelas do plano financeiro
     *  escolhido pelo aluno, quantidade de parcelas que foram geradas, Valor cheio da parcela, 
     *  Desconto que o aluno possui, Valor a pagar que será o resultado do valor total menos o valor do 
     *  desconto e a data de vencimento da primeira mensalidade
     * 
     * @return List<CondicoesPagamentoRelVO>
     * 			Lista de Alunos que não renovaram matricula.
     */
    private List<CondicoesPagamentoRelVO> executarConsultaCondicoesPagamento(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, UnidadeEnsinoCursoVO unidadeEnsinoCurso, MatriculaVO matricula, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws SQLException, Exception {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT ON (pessoa.nome, matricula.matricula, curso.nome, turma.identificadorturma, contareceber.dataVencimento) matriculaperiodo.categoriaCondicaoPagamento, matricula.matricula as matricula, matricula.descontochancela as descontochancela, curso.nome as curso, turma.identificadorturma as turma, pessoa.nome as aluno, matriculaperiodo.situacaomatriculaperiodo as situacao, matricula.canceladofinanceiro, ");
        sql.append(" CASE WHEN condicaopagamentoplanofinanceirocurso.quantidadeParcelasMaterialDidatico IS NOT NULL ");
        sql.append("and condicaopagamentoplanofinanceirocurso.quantidadeParcelasMaterialDidatico > 0 then "); 
        sql.append(" (condicaopagamentoplanofinanceirocurso.nrparcelasperiodo + condicaopagamentoplanofinanceirocurso.quantidadeParcelasMaterialDidatico)  ");
        sql.append("else " );
        sql.append(" condicaopagamentoplanofinanceirocurso.nrparcelasperiodo ");
        sql.append(" end AS numrParcelas, ");
        sql.append(" contareceber.codigo as crCodigo, contareceber.data as crData, contareceber.codOrigem as crCodOrigem, contareceber.tipoOrigem as crTipoOrigem, contareceber.situacao as crSituacao, contareceber.descricaoPagamento as crDescricaoPagamento,  ");
        sql.append(" contareceber.dataVencimento as crDataVencimento, contareceber.valor as  crValor, contareceber.valorDesconto as crValorDesconto, contareceber.juro as crJuro, contareceber.juroPorcentagem as crJuroPorcentagem, ");
        sql.append(" contareceber.multa as crMulta, contareceber.multaPorcentagem as crMultaPorcentagem , contareceber.nrDocumento as crNrDocumento, contareceber.parcela as crParcela, contareceber.centroReceita as crCentroReceita,  ");
        sql.append(" contareceber.matriculaAluno as crMatriculaAluno, contareceber.funcionario as crFuncionario, contareceber.origemNegociacaoReceber as crOrigemNegociacaoReceber, contareceber.candidato as crCandidato, contareceber.tipoPessoa as crTipoPessoa,  ");
        sql.append(" contareceber.pessoa as crPessoa, contareceber.contaCorrente as crContaCorrente, contareceber.descontoProgressivo as crDescontoProgressivo, contareceber.valorRecebido as crValorRecebido, contareceber.tipoDesconto as crTipoDesconto, contareceber.unidadeEnsino as crUnidadeEnsino, contareceber.valorDescontoRecebido as crValorDescontoRecebido, ");
        sql.append(" contareceber.descontoInstituicao as crDescontoInstituicao, contareceber.descontoConvenio as crDescontoConvenio, contareceber.convenio as crConvenio, contareceber.tipoBoleto as crTipoBoleto, contareceber.linhaDigitavelCodigoBarras as crLinhaDigitavelCodigoBarras,  ");
        sql.append(" contareceber.codigoBarra as crCodigoBarra, contareceber.beneficiario as crBeneficiario, contareceber.nossonumero as crNossoNumero, contareceber.parceiro as crParceiro, contareceber.matriculaPeriodo as crMatriculaPeriodo, contareceber.valorDescontoProgressivo as crValorDescontoProgressivo, contareceber.recebimentoBancario as crRecebimentoBancario, ");
        sql.append(" contareceber.ordemConvenio as crOrdemConvenio, contareceber.ordemConvenioValorCheio as crOrdemConvenioValorCheio, contareceber.ordemDescontoAluno as crOrdemDescontoAluno, contareceber.ordemDescontoAlunoValorCheio as crOrdemDescontoAlunoValorCheio, ");
        sql.append(" contareceber.ordemDescontoProgressivo as crOrdemDescontoProgressivo, contareceber.ordemDescontoProgressivoValorCheio as crOrdemDescontoProgressivoValorCheio, contareceber.ordemPlanoDesconto as crOrdemPlanoDesconto, contareceber.ordemPlanoDescontoValorCheio as crOrdemPlanoDescontoValorCheio, contareceber.justificativaDesconto as crJustificativaDesconto, ");
       
        sql.append(" case when ");
	        sql.append(" (   SELECT count(cr.codigo) ");
	        sql.append(" FROM contareceber cr ");
	        sql.append(" WHERE cr.tipoorigem IN ('MEN', 'MDI') ");
	        sql.append(" AND cr.codorigem = matriculaperiodo.codigo::varchar ");
	        sql.append(" AND cr.matriculaperiodo = matriculaperiodo.codigo ) ");	        
        sql.append(" > 0 then ");
        sql.append(" (   SELECT count(cr.codigo) ");
        sql.append(" FROM contareceber cr ");
        sql.append(" WHERE cr.tipoorigem IN ('MEN', 'MDI') ");	        
        sql.append(" AND cr.codorigem = matriculaperiodo.codigo::varchar ");
        sql.append(" AND cr.matriculaperiodo = matriculaperiodo.codigo ) ");	
        sql.append(" else ");
	        sql.append(" (   SELECT count(mpv.codigo) ");
	        sql.append(" FROM matriculaperiodovencimento mpv  ");
	        sql.append(" WHERE mpv.matriculaperiodo = matriculaperiodo.codigo ");	        
	        sql.append(" AND mpv.situacao in ('GE', 'GP', 'NCR', 'CEM') ");
	        sql.append(" AND mpv.tipoorigemmatriculaperiodovencimento IN ('MENSALIDADE', 'MATERIAL_DIDATICO')  ");	        
	        sql.append(" ) ");

        sql.append(" end as quantidadeTotalParcelas,  ");
        
        sql.append(" case when contareceber.datavencimento is not null  then  contareceber.datavencimento else matriculaperiodovencimento.datavencimento  end as dataVencimento, ");
        sql.append(" condicaopagamentoplanofinanceirocurso.variavel1 as valorParcela, ");

        sql.append("  CASE WHEN (matriculaperiodo.bolsista = true or (contareceber.situacao = 'RE' and contareceber.valorrecebido = 0.0 and exists(select itemplanofinanceiroaluno.codigo from planofinanceiroaluno inner join itemplanofinanceiroaluno on itemplanofinanceiroaluno.planofinanceiroaluno = planofinanceiroaluno.codigo where planofinanceiroaluno.matriculaperiodo = matriculaperiodo.codigo and itemplanofinanceiroaluno.convenio is not null limit 1 ))) THEN 'S' ELSE 'N' END as bolsa, ");

        sql.append(" CASE WHEN (contareceber.valordescontocalculadoprimeirafaixadescontos is not null) ");
        sql.append(" THEN contareceber.valordescontocalculadoprimeirafaixadescontos ");
        sql.append(" ELSE contareceber.valor - (calculadescontoporordem2(contaReceber.codigo, ");
        sql.append("   (CASE WHEN (contaReceber.descontoProgressivo is not null) ");
        sql.append("   THEN (SELECT dialimite1 FROM descontoProgressivo  ");
        sql.append(" 		WHERE contaReceber.descontoProgressivo = descontoProgressivo.codigo ) ");
        sql.append(" 		ELSE 25 END) ))[5] ");
        sql.append(" END as valordescontocalculadoprimeirafaixadescontos, pConsultor.nome as nomeConsultor ");

        sql.append(" , matriculaperiodo.categoriacondicaopagamento as categoria ");
        sql.append(" , matriculaperiodovencimento.valor as valorMatriculaPeriodoVencimento ");
        sql.append(" , matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos as valorPrimeiroDescontoMatriculaPeriodoVencimento ");

        
        sql.append(" FROM matriculaperiodo ");
        sql.append(" INNER JOIN matricula ON (matriculaperiodo.matricula = matricula.matricula) ");
        sql.append(" INNER JOIN curso ON matricula.curso = curso.codigo ");
        sql.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma ");
        sql.append(" INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo) ");
        sql.append(" INNER JOIN condicaopagamentoplanofinanceirocurso ON (condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso) ");
        sql.append(" LEFT JOIN funcionario fConsultor ON fConsultor.codigo = matricula.consultor ");
        sql.append(" LEFT JOIN pessoa pConsultor ON pConsultor.codigo = fConsultor.pessoa ");
        sql.append(" LEFT JOIN matriculaperiodovencimento ON ( matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo) AND matriculaperiodovencimento.tipoOrigemMatriculaPeriodoVencimento IN ('MENSALIDADE', 'MATERIAL_DIDATICO') and matriculaperiodovencimento.codigo = (SELECT mpv.codigo FROM matriculaperiodovencimento AS mpv WHERE matriculaperiodo.codigo = mpv.matriculaperiodo AND mpv.tipoOrigemMatriculaPeriodoVencimento IN ('MENSALIDADE', 'MATERIAL_DIDATICO') AND (mpv.parcela like ('1/%') or mpv.parcela like ('01%') ) AND (mpv.parcela not like ('%R%')) ORDER BY mpv.datavencimento LIMIT 1)");        
        sql.append(" LEFT JOIN contareceber ON ( matriculaperiodo.codigo = contareceber.matriculaperiodo) AND contareceber.tipoorigem IN ('MEN', 'MDI') and contareceber.codorigem = matriculaperiodo.codigo::varchar ");
        sql.append(" and ((matriculaperiodovencimento.contareceber is not null and matriculaperiodovencimento.contareceber = contareceber.codigo) ");
        sql.append(" or (matriculaperiodovencimento.contareceber is null and contareceber.codigo = (SELECT cr.codigo FROM contareceber AS cr WHERE matriculaperiodo.codigo = cr.matriculaperiodo AND cr.pessoa is not null and cr.tipoorigem IN ('MEN', 'MDI') AND (cr.parcela like ('1/%') or cr.parcela like ('01%') ) AND (cr.parcela not like ('%R%')) ORDER BY cr.datavencimento LIMIT 1) )) ");
        sql.append(getSqlCondicaoWhere(dataInicio, dataFim, ano, semestre, unidadeEnsino, curso, turma, matricula, filtroRelatorioAcademicoVO));
        sql.append(" ORDER BY curso.nome, turma.identificadorturma, pessoa.nome, matricula.matricula ");

        SqlRowSet dadosRetornadosNaSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosCondicoesPagamento(dadosRetornadosNaSQL, ano, semestre, condicoesPagamentoSuperParametroRelVO);
    }
    
    private List<CondicoesPagamentoRelVO> executarConsultaCondicoesPagamentoLayout2(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, UnidadeEnsinoCursoVO unidadeEnsinoCurso, MatriculaVO matricula, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws SQLException, Exception {
    	StringBuilder sb = new StringBuilder();
    	  	
    	sb.append("select distinct count(matriculaperiodo.matricula) AS qtdeAlunosUsamPlanoFinanceiro, matriculaperiodo.categoriaCondicaoPagamento, curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
    	sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma.identificadorturma\", ");
    	sb.append(" condicaopagamentoplanofinanceirocurso.codigo AS \"condicaopagamentoplanofinanceirocurso.codigo\", condicaopagamentoplanofinanceirocurso.descricao AS \"condicaopagamentoplanofinanceirocurso.descricao\", ");
    	sb.append(" CASE WHEN condicaopagamentoplanofinanceirocurso.quantidadeParcelasMaterialDidatico IS NOT NULL ");
    	sb.append("and condicaopagamentoplanofinanceirocurso.quantidadeParcelasMaterialDidatico > 0 then "); 
    	sb.append(" CAST((condicaopagamentoplanofinanceirocurso.nrparcelasperiodo + condicaopagamentoplanofinanceirocurso.quantidadeParcelasMaterialDidatico) AS varchar(20)) ");
    	sb.append("else " );
    	sb.append(" CAST((condicaopagamentoplanofinanceirocurso.nrparcelasperiodo) AS varchar(20)) ");
    	sb.append(" end AS nrparcelasperiodo, ");
    	sb.append(" condicaopagamentoplanofinanceirocurso.valorParcela, ");
    	sb.append(" CASE WHEN (condicaopagamentoplanofinanceirocurso.descontoprogressivopadrao is not null) THEN condicaopagamentoplanofinanceirocurso.valorparcela - (");
    	sb.append(" calculadescontoprogressivo(condicaopagamentoplanofinanceirocurso.valorparcela, condicaopagamentoplanofinanceirocurso.descontoprogressivopadrao, ");
    	sb.append(" (select dialimite1 FROM descontoprogressivo where condicaopagamentoplanofinanceirocurso.descontoprogressivopadrao = descontoprogressivo.codigo)))end AS valorPrimeiraFaixaDescontos, ");
    	sb.append(" ( select count(matriculaperiodo.matricula) ");
	    	sb.append(" from matriculaperiodo ");
	    	sb.append(" INNER JOIN matricula ON (matriculaperiodo.matricula = matricula.matricula)  ");
	    	sb.append(" INNER JOIN turma ON (matriculaperiodo.turma = turma.codigo)  ");
	    	sb.append(" INNER JOIN condicaopagamentoplanofinanceirocurso ON (condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso)  ");
	    	if(dataInicio != null || dataFim != null) {	    		
	    		sb.append(" INNER JOIN contareceber ON ( matriculaperiodo.codigo = contareceber.matriculaperiodo) and contareceber.pessoa is not null AND (contareceber.parcela like ('1/%') or contareceber.parcela like ('01%')) AND (contareceber.parcela not like ('%R%'))");	    	
	    	}
	    	sb.append(getSqlCondicaoWhere(dataInicio, dataFim, ano, semestre, unidadeEnsino, curso, turma,  matricula,  filtroRelatorioAcademicoVO));	    	    	
    	sb.append(" ) totalAlunos");
    	
    	
    	sb.append(" from matriculaperiodo ");
    	sb.append(" INNER JOIN matricula ON (matriculaperiodo.matricula = matricula.matricula)  ");
    	sb.append(" INNER JOIN curso ON matricula.curso = curso.codigo  ");
    	sb.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma  ");
    	sb.append(" INNER JOIN condicaopagamentoplanofinanceirocurso ON (condicaopagamentoplanofinanceirocurso.codigo = matriculaperiodo.condicaopagamentoplanofinanceirocurso)  ");
    	if(dataInicio != null || dataFim != null) {
    		sb.append(" INNER ");	
    	}else {
    		sb.append(" LEFT ");
    	}
    	sb.append(" JOIN contareceber ON ( matriculaperiodo.codigo = contareceber.matriculaperiodo) and contareceber.tipoorigem IN ('MEN', 'MDI') and contareceber.codigo = (SELECT cr.codigo FROM contareceber AS cr WHERE  matriculaperiodo.codigo = cr.matriculaperiodo AND cr.pessoa is not null and cr.tipoorigem IN ('MEN', 'MDI') AND (cr.parcela like ('1/%') or cr.parcela like ('01%') ) AND (cr.parcela not like ('%R%')) ORDER BY datavencimento LIMIT 1)");
    	sb.append(getSqlCondicaoWhere(dataInicio, dataFim, ano, semestre, unidadeEnsino, curso, turma,  matricula, filtroRelatorioAcademicoVO));
		sb.append(" group by curso.codigo, curso.nome, turma.codigo, turma.identificadorturma, condicaopagamentoplanofinanceirocurso.codigo, condicaopagamentoplanofinanceirocurso.descricao, ");
        sb.append(" condicaopagamentoplanofinanceirocurso.nrparcelasperiodo, condicaopagamentoplanofinanceirocurso.valorParcela, ");
        sb.append(" condicaopagamentoplanofinanceirocurso.descontoprogressivopadrao, matriculaperiodo.categoriacondicaopagamento ");
        sb.append(" ORDER BY turma.identificadorturma ");    	
        SqlRowSet sql2 = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

        return montarDadosCondicoesPagamentoLayout2(sql2, ano, semestre, condicoesPagamentoSuperParametroRelVO);
    }
    
    public StringBuilder getSqlCondicaoWhere(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
    	StringBuilder sb =  new StringBuilder("");
    	sb.append(" WHERE 1 = 1   ");    	
    	if (!filtroRelatorioAcademicoVO.getPeriodicidadeEnum().equals(PeriodicidadeEnum.INTEGRAL) && !ano.equals("")) {
            sb.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
        }
        if (filtroRelatorioAcademicoVO.getPeriodicidadeEnum().equals(PeriodicidadeEnum.SEMESTRAL) && !semestre.equals("")) {
            sb.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
        }
        if (filtroRelatorioAcademicoVO.getPeriodicidadeEnum().equals(PeriodicidadeEnum.INTEGRAL) && !Uteis.isAtributoPreenchido(matricula.getMatricula())) {
        	sb.append(" and ").append(realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "matriculaperiodo.data", false));
        }
        if (!matricula.getMatricula().equals("")) {
            sb.append(" AND matricula.matricula like '").append(matricula.getMatricula()).append("'");
        }
        if (curso.getCodigo() != 0) {
            sb.append(" AND matricula.curso =  ").append(curso.getCodigo());
        }
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != 0) {
            sb.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
        }
        if (turma != null && !turma.getCodigo().equals(0) && !turma.getIdentificadorTurma().equals("") && !turma.getIdentificadorTurma().equals("0")) {
            sb.append(" AND turma.identificadorturma = '").append(turma.getIdentificadorTurma()).append("'");            
        }        
        if(!Uteis.isAtributoPreenchido(matricula.getMatricula())) {
        	sb.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
        }
        sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.dataVencimento", false));        
		return sb;
    }
    
   public List<CondicoesPagamentoRelVO> criarListaPlanoDesconto(Date dataInicio, Date dataFim, String ano, String semestre, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, UnidadeEnsinoCursoVO unidadeEnsinoCurso, MatriculaVO matricula, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO, String tipoLayout, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
    	
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("select count(matriculaperiodo.codigo) as qtdeAlunosUsamPlanoDesconto, planodesconto.nome, turma.identificadorturma "); 	   	
    	sb.append(" from matriculaperiodo ");
    	sb.append(" INNER JOIN matricula ON (matriculaperiodo.matricula = matricula.matricula)  ");
    	sb.append(" INNER JOIN curso ON matricula.curso = curso.codigo  ");
    	sb.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma  ");
    	sb.append(" inner join planofinanceiroaluno on matriculaperiodo.codigo=planofinanceiroaluno.matriculaperiodo ");
    	sb.append(" inner join itemplanofinanceiroaluno on planofinanceiroaluno.codigo=itemplanofinanceiroaluno.planofinanceiroaluno and itemplanofinanceiroaluno.tipoitemplanofinanceiro = 'PD' ");
    	sb.append(" inner join planodesconto on itemplanofinanceiroaluno.planodesconto = planodesconto.codigo ");
    	if(dataInicio != null || dataFim != null) {
    		sb.append(" inner JOIN contareceber ON ( matriculaperiodo.codigo = contareceber.matriculaperiodo) AND contareceber.tipoorigem IN ('MEN', 'MDI') and contareceber.codigo = (SELECT cr.codigo FROM contareceber AS cr WHERE  matriculaperiodo.codigo = cr.matriculaperiodo AND cr.pessoa is not null and cr.tipoorigem IN ('MEN', 'MDI') AND (cr.parcela like ('1/%') or cr.parcela like ('01%') ) AND (cr.parcela not like ('%R%')) ORDER BY datavencimento LIMIT 1)");
    	}
    	sb.append(getSqlCondicaoWhere(dataInicio, dataFim, ano, semestre, unidadeEnsino, curso, turma, matricula, filtroRelatorioAcademicoVO));
        sb.append(" group by planodesconto.nome, turma.identificadorturma ");
        sb.append(" ORDER BY turma.identificadorturma ");    	

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		List<CondicoesPagamentoRelVO> listaDescontoAlunoVOs = new ArrayList<CondicoesPagamentoRelVO>(0);		

		while (tabelaResultado.next()) {
			CondicoesPagamentoRelVO condicoesPagamento = new CondicoesPagamentoRelVO();
			condicoesPagamento.setQtdeAlunosUsamPlanoDesconto(tabelaResultado.getDouble("qtdeAlunosUsamPlanoDesconto"));
			condicoesPagamento.setDescontoAluno(tabelaResultado.getString("nome"));
			condicoesPagamento.setTurma(tabelaResultado.getString("identificadorturma"));

			listaDescontoAlunoVOs.add(condicoesPagamento);	
		}
		return listaDescontoAlunoVOs;
        
    }
    
    private List<CondicoesPagamentoRelVO> montarDadosCondicoesPagamentoLayout2(SqlRowSet dadosSQL, String ano, String semestre, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO) throws Exception {
    	List<CondicoesPagamentoRelVO> listaConsulta = new ArrayList<CondicoesPagamentoRelVO>(0);
    	while (dadosSQL.next()) {
    		CondicoesPagamentoRelVO obj = new CondicoesPagamentoRelVO();
    		obj.setCondicaoPagamento(dadosSQL.getString("condicaopagamentoplanofinanceirocurso.descricao"));
    		obj.setCodigoCondicaoPagamento(dadosSQL.getInt("condicaopagamentoplanofinanceirocurso.codigo"));
    		obj.setQtdeAlunosUsamPlanoFinanceiro(dadosSQL.getDouble("qtdeAlunosUsamPlanoFinanceiro"));
    		obj.setTotalAlunosUsamPlanoFinanceiro(dadosSQL.getInt("totalAlunos"));
    		obj.setCurso(dadosSQL.getString("curso.nome"));
    		obj.setTurma(dadosSQL.getString("turma.identificadorTurma"));
    		obj.setQuantidadeParcelas(dadosSQL.getString("nrParcelasPeriodo"));
    		obj.setValorParcela(dadosSQL.getDouble("valorParcela"));
    		if (dadosSQL.getDouble("valorPrimeiraFaixaDescontos") != 0.0) {
    			obj.setValorDesconto(obj.getValorParcela() - dadosSQL.getDouble("valorPrimeiraFaixaDescontos"));
    		} else {
    			obj.setValorDesconto(0.0);
    		}
    		obj.setValorFinal(obj.getValorParcela() - obj.getValorDesconto());
    		
    		obj.setCategoriaCondicaoPagamento(dadosSQL.getString("categoriaCondicaoPagamento"));
    		
    		listaConsulta.add(obj);
    	}
    	return listaConsulta;
    }

    private List<CondicoesPagamentoRelVO> montarDadosCondicoesPagamento(SqlRowSet dadosRetornadosNaSQL, String ano, String semestre, CondicoesPagamentoSuperParametroRelVO condicoesPagamentoSuperParametroRelVO) throws Exception {
        List<CondicoesPagamentoRelVO> listaConsulta = new ArrayList<CondicoesPagamentoRelVO>(0);
        while (dadosRetornadosNaSQL.next()) {
            CondicoesPagamentoRelVO obj = new CondicoesPagamentoRelVO();
            obj.setMatriculaAluno(dadosRetornadosNaSQL.getString("matricula"));
            obj.setNomeAluno(dadosRetornadosNaSQL.getString("aluno"));
            if(dadosRetornadosNaSQL.getBoolean("canceladofinanceiro") && dadosRetornadosNaSQL.getString("situacao").equals("AT")) {
            	obj.setSituacao("CF");
            }else {
            	obj.setSituacao(dadosRetornadosNaSQL.getString("situacao"));
            }
            obj.setQuantidadeParcelas((dadosRetornadosNaSQL.getString("numrParcelas")));
            obj.setQuantidadeParcelasGeradas((dadosRetornadosNaSQL.getString("quantidadeTotalParcelas")));

            
            obj.setValorParcela(dadosRetornadosNaSQL.getDouble("valorParcela"));
            obj.setBolsa(dadosRetornadosNaSQL.getString("bolsa"));
//            if (obj.getBolsa().equals("S")) {
//                obj.setDataPrimeiroVencimento("");
//            } else {
            	Date data = dadosRetornadosNaSQL.getDate("dataVencimento");                
                obj.setDataPrimeiroVencimento(Uteis.obterDataFormatoTextoddMMyyyy(data));
//            }
            if (obj.getBolsa().equals("S") && obj.getSituacao().equals("AT")) {
                condicoesPagamentoSuperParametroRelVO.setQtdeBolsas(condicoesPagamentoSuperParametroRelVO.getQtdeBolsas() + 1);
            }

            if (!obj.getSituacao().equals("AT")) {
                condicoesPagamentoSuperParametroRelVO.setQtdeNaoAtivos(condicoesPagamentoSuperParametroRelVO.getQtdeNaoAtivos() + 1);
            }
            condicoesPagamentoSuperParametroRelVO.setQtdeMatriculados(condicoesPagamentoSuperParametroRelVO.getQtdeMatriculados() + 1);
            obj.setNomeConsultor(dadosRetornadosNaSQL.getString("nomeConsultor"));
            obj.setCurso(dadosRetornadosNaSQL.getString("curso"));
            obj.setTurma(dadosRetornadosNaSQL.getString("turma"));
            obj.setAno(ano);
            obj.setSemestre(semestre);

            /* Montando Objeto contaReceber para cálculo do desconto */
            ContaReceberVO contaReceber = new ContaReceberVO();
            contaReceber.setCodigo(dadosRetornadosNaSQL.getInt("crCodigo"));
            contaReceber.setData(dadosRetornadosNaSQL.getDate("crData"));
            contaReceber.setCodOrigem(dadosRetornadosNaSQL.getString("crTipoOrigem"));
            contaReceber.setSituacao(dadosRetornadosNaSQL.getString("crSituacao"));
            contaReceber.setDescricaoPagamento(dadosRetornadosNaSQL.getString("crDescricaoPagamento"));
            contaReceber.setDataVencimento(dadosRetornadosNaSQL.getDate("crDataVencimento"));
            contaReceber.setValor(dadosRetornadosNaSQL.getDouble("crValor"));
            contaReceber.setValorDesconto(dadosRetornadosNaSQL.getDouble("crValorDesconto"));
            contaReceber.setJuro(dadosRetornadosNaSQL.getDouble("crJuro"));
            contaReceber.setJuroPorcentagem(dadosRetornadosNaSQL.getDouble("crJuroPorcentagem"));
            contaReceber.setMulta(dadosRetornadosNaSQL.getDouble("crMulta"));
            contaReceber.setMultaPorcentagem(dadosRetornadosNaSQL.getDouble("crMultaPorcentagem"));
            contaReceber.setNrDocumento(dadosRetornadosNaSQL.getString("crNrDocumento"));
            contaReceber.setParcela(dadosRetornadosNaSQL.getString("crParcela"));
            CentroReceitaVO centroReceita = new CentroReceitaVO();
            centroReceita.setCodigo(dadosRetornadosNaSQL.getInt("crCentroReceita"));
            contaReceber.setCentroReceita(centroReceita);
            MatriculaVO matriculaAluno = new MatriculaVO();
            matriculaAluno.setMatricula(dadosRetornadosNaSQL.getString("crMatriculaAluno"));
            contaReceber.setMatriculaAluno(matriculaAluno);
            if (dadosRetornadosNaSQL.getString("crFuncionario") != null && !dadosRetornadosNaSQL.getString("crFuncionario").equals("")) {
                FuncionarioVO funcionario = new FuncionarioVO();
                funcionario.setCodigo(dadosRetornadosNaSQL.getInt("crFuncionario"));
                contaReceber.setFuncionario(funcionario);


            }
            contaReceber.setOrigemNegociacaoReceber(dadosRetornadosNaSQL.getInt("crOrigemNegociacaoReceber"));
            if (dadosRetornadosNaSQL.getString("crCandidato") != null && !dadosRetornadosNaSQL.getString("crCandidato").equals("")) {
                //contaReceber.setCandidato(sql2.getString("crCandidato"));
            }
            contaReceber.setTipoPessoa(dadosRetornadosNaSQL.getString("crTipoPessoa"));
            if (dadosRetornadosNaSQL.getInt("crPessoa") != 0) {
                PessoaVO pessoa = new PessoaVO();
                pessoa.setCodigo(dadosRetornadosNaSQL.getInt("crPessoa"));
                contaReceber.setPessoa(pessoa);
            }
            if (dadosRetornadosNaSQL.getInt("crContaCorrente") != 0) {
                ContaCorrenteVO contaCorrente = new ContaCorrenteVO();
                contaCorrente.setCodigo(dadosRetornadosNaSQL.getInt("crContaCorrente"));
            }
            if (dadosRetornadosNaSQL.getInt("crDescontoProgressivo") != 0) {
                DescontoProgressivoVO descontoProgressivo = new DescontoProgressivoVO();
                descontoProgressivo.setCodigo(dadosRetornadosNaSQL.getInt("crDescontoProgressivo"));
            }
            contaReceber.setValorRecebido(dadosRetornadosNaSQL.getDouble("crValorRecebido"));
            contaReceber.setTipoDesconto(dadosRetornadosNaSQL.getString("crTipoDesconto"));
            if (dadosRetornadosNaSQL.getInt("crUnidadeEnsino") != 0) {
                UnidadeEnsinoVO unidadeEnsino = new UnidadeEnsinoVO();
                unidadeEnsino.setCodigo(dadosRetornadosNaSQL.getInt("crUnidadeEnsino"));
                contaReceber.setUnidadeEnsino(unidadeEnsino);
            }
            contaReceber.setValorDescontoRecebido(dadosRetornadosNaSQL.getDouble("crValorDescontoRecebido"));
            contaReceber.setValorDescontoInstituicao(dadosRetornadosNaSQL.getDouble("crDescontoInstituicao"));
            if (dadosRetornadosNaSQL.getLong("crConvenio") != 0) {
                ConvenioVO convenio = new ConvenioVO();
                convenio.setCodigo(dadosRetornadosNaSQL.getInt("crConvenio"));
                contaReceber.setConvenio(convenio);
            }
            contaReceber.setTipoBoleto(dadosRetornadosNaSQL.getString("crTipoBoleto"));
            contaReceber.setLinhaDigitavelCodigoBarras(dadosRetornadosNaSQL.getString("crLinhaDigitavelCodigoBarras"));
            contaReceber.setCodigoBarra(dadosRetornadosNaSQL.getString("crCodigoBarra"));
            if (dadosRetornadosNaSQL.getInt("crBeneficiario") != 0) {
                PessoaVO beneficiario = new PessoaVO();
                beneficiario.setCodigo(dadosRetornadosNaSQL.getInt("crBeneficiario"));
                contaReceber.setBeneficiario(beneficiario);
            }
            contaReceber.setNossoNumero(dadosRetornadosNaSQL.getString("crNossoNumero"));
            if (dadosRetornadosNaSQL.getInt("crParceiro") != 0) {
                PessoaVO p = new PessoaVO();
                p.setCodigo(dadosRetornadosNaSQL.getInt("crParceiro"));
                contaReceber.setPessoa(p);
            }
            if (dadosRetornadosNaSQL.getInt("crMatriculaPeriodo") != 0) {
                contaReceber.setMatriculaPeriodo(dadosRetornadosNaSQL.getInt("crMatriculaPeriodo"));
            }
            contaReceber.setValorDescontoProgressivo(dadosRetornadosNaSQL.getDouble("crValorDescontoProgressivo"));
            contaReceber.setRecebimentoBancario(dadosRetornadosNaSQL.getBoolean("crRecebimentoBancario"));
            contaReceber.setOrdemConvenio(dadosRetornadosNaSQL.getInt("crOrdemConvenio"));
            contaReceber.setOrdemConvenioValorCheio(dadosRetornadosNaSQL.getBoolean("crOrdemConvenioValorCheio"));
            contaReceber.setOrdemDescontoAluno(dadosRetornadosNaSQL.getInt("crOrdemDescontoAluno"));
            contaReceber.setOrdemDescontoAlunoValorCheio(dadosRetornadosNaSQL.getBoolean("crOrdemDescontoAlunoValorCheio"));
            contaReceber.setOrdemDescontoProgressivo(dadosRetornadosNaSQL.getInt("crOrdemDescontoProgressivo"));
            contaReceber.setOrdemDescontoProgressivoValorCheio(dadosRetornadosNaSQL.getBoolean("crOrdemDescontoProgressivoValorCheio"));
            contaReceber.setOrdemPlanoDesconto(dadosRetornadosNaSQL.getInt("crOrdemPlanoDesconto"));
            contaReceber.setOrdemPlanoDescontoValorCheio(dadosRetornadosNaSQL.getBoolean("crOrdemPlanoDescontoValorCheio"));
            contaReceber.setJustificativaDesconto(dadosRetornadosNaSQL.getString("crJustificativaDesconto"));

            if (obj.getBolsa().equals("S")) {
                obj.setValorDesconto(0.0);
                obj.setValorParcela(0.0);
            } else {
                //valor 1° desconto
            	if(contaReceber.getValor() > 0) {
	                obj.setValorDesconto(contaReceber.getValor() - dadosRetornadosNaSQL.getDouble("valordescontocalculadoprimeirafaixadescontos"));
	                obj.setValorParcela(contaReceber.getValor());
            	}
            	else {
            		obj.setValorDesconto(dadosRetornadosNaSQL.getDouble("valorMatriculaPeriodoVencimento") - dadosRetornadosNaSQL.getDouble("valorPrimeiroDescontoMatriculaPeriodoVencimento"));
            		obj.setValorParcela(dadosRetornadosNaSQL.getDouble("valorMatriculaPeriodoVencimento"));
            	}
            }
            obj.setValorFinal(obj.getValorParcela() - obj.getValorDesconto());
            obj.setContaReceber(contaReceber);
            
            obj.setCategoriaCondicaoPagamento(dadosRetornadosNaSQL.getString("categoriaCondicaoPagamento"));
            
            contaReceber = null;
            listaConsulta.add(obj);
        }
        return listaConsulta;
    }

    @Override
    public String designIReportRelatorio(String tipoLayout) {
    	if (tipoLayout.equals("AL")) {
    		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    	} else {
    		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeLayout2() + ".jrxml");
    	}
    }

    public static String getDesignIReportRelatorioSintetico() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeSintetico() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return ("CondicoesPagamentoRel");
    }

    public static String getIdEntidadeLayout2() {
    	return ("CondicoesPagamentoLayout2Rel");
    }

    public static String getIdEntidadeSintetico() {
        return ("CondicoesPagamentoRel");
    }
}
