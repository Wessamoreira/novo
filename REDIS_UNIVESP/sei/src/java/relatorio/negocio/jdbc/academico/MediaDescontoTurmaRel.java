package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.MediaDescontoTurmaRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.academico.MediaDescontoTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class MediaDescontoTurmaRel extends SuperRelatorio implements MediaDescontoTurmaRelInterfaceFacade {

    public MediaDescontoTurmaRel() {
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.MediaDescontoTurmaRelInterfaceFacade#emitirRelatorio()
     */
    public List<MediaDescontoTurmaRelVO> criarObjeto(MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO, UsuarioVO usuarioVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> situacoes, String tipoOrdenacao) throws Exception {
        MediaDescontoAlunoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
        List<MediaDescontoTurmaRelVO> listaMediaDescontoTurmaRelVO = new ArrayList<MediaDescontoTurmaRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(mediaDescontoTurmaRelVO, filtroRelatorioFinanceiroVO, situacoes, tipoOrdenacao);
        while (dadosSQL.next()) {
            listaMediaDescontoTurmaRelVO.add(montarDados(dadosSQL));
        }
        return listaMediaDescontoTurmaRelVO;
    }

    public static void validarDados(MediaDescontoTurmaRelVO obj) throws ConsistirException{
        if (obj.getTurmaVO().getCodigo().equals(0)){
            throw new ConsistirException(UteisJSF.internacionalizar("msg_MediaDescontoTurmaRel_Turma"));
        }
        
        if ((!obj.getTurmaVO().getCurso().getIntegral() && !obj.getTurmaVO().getIntegral()) && !Uteis.isAtributoPreenchido(obj.getAno())){
            throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
        }
        
        
        if ((obj.getTurmaVO().getCurso().getSemestral() || obj.getTurmaVO().getSemestral()) && !Uteis.isAtributoPreenchido(obj.getSemestre())){
            throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.MediaDescontoTurmaRelInterfaceFacade#executarConsultaParametrizada()
     */
    public SqlRowSet executarConsultaParametrizada(MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<String> situacoes, String tipoOrdenacao) throws Exception {
        StringBuilder strSql = new StringBuilder();
        strSql.append("SELECT pessoa.nome AS pessoa_nome, case when contareceber.situacao = 'RE' then contareceber.valordescontorecebido else contareceber.valordescontocalculado END AS contareceber_valordesconto, contareceber.tipoorigem AS contareceber_tipoorigem, ");
        strSql.append("contareceber.datavencimento AS contareceber_datavencimento, contareceber.valor AS contareceber_valor, curso.nome AS curso_nome, ");
        strSql.append("matricula.matricula AS matricula_matricula, turma.identificadorturma as turma_nome, contareceber.situacao as contareceber_situacao, contareceber.parcela as contareceber_parcela ");
        strSql.append("FROM contareceber ");
        strSql.append("LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno)");
        strSql.append("LEFT JOIN matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
        strSql.append("LEFT JOIN curso ON (matricula.curso = curso.codigo)");
        strSql.append("LEFT JOIN turma ON (contareceber.turma = turma.codigo)");
        strSql.append("LEFT JOIN pessoa ON (matricula.aluno = pessoa.codigo)");
    	strSql.append("where 1=1");
        if (mediaDescontoTurmaRelVO.getTurmaVO().getCodigo() != null && mediaDescontoTurmaRelVO.getTurmaVO().getCodigo() != 0) {
            strSql.append("and ( turma.codigo =").append(mediaDescontoTurmaRelVO.getTurmaVO().getCodigo()).append(" and case when contareceber.situacao = 'RE' then contareceber.valordescontorecebido <> 0 else contareceber.valordescontocalculado <> 0 end)") ;
        }
        if (mediaDescontoTurmaRelVO.getAno() != null && !mediaDescontoTurmaRelVO.getAno().equals("")) {
        	strSql.append(" and matriculaperiodo.ano = '").append(mediaDescontoTurmaRelVO.getAno()).append("'");
        }
        if (mediaDescontoTurmaRelVO.getSemestre() != null && !mediaDescontoTurmaRelVO.getSemestre().equals("")) {
        	strSql.append(" and matriculaperiodo.semestre = '").append(mediaDescontoTurmaRelVO.getSemestre()).append("'");
        }
        
        strSql.append(montarSqlSituacoes(situacoes));
        strSql.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
        
        strSql.append(" GROUP BY pessoa_nome, contareceber_valordesconto, contareceber_tipoorigem, contareceber_valor, contareceber_datavencimento, turma_nome, ");
        strSql.append(" curso_nome, matricula_matricula, contareceber.situacao, contareceber.parcela");
        
		if (tipoOrdenacao.equals("dataVencimento")) {
			strSql.append(" ORDER BY datavencimento");
		} else if (tipoOrdenacao.equals("alunoDataVencimento")) {
			strSql.append(" ORDER BY pessoa.nome, datavencimento");
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(strSql.toString());
        return tabelaResultado;
    }

    public MediaDescontoTurmaRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
        MediaDescontoTurmaRelVO mediaDescontoTurmaRelVO = new MediaDescontoTurmaRelVO();
        mediaDescontoTurmaRelVO.setNomeAluno(dadosSQL.getString("pessoa_nome"));
        mediaDescontoTurmaRelVO.setValorDesconto(dadosSQL.getDouble("contareceber_valordesconto"));
        mediaDescontoTurmaRelVO.setTipoOrigem(dadosSQL.getString("contareceber_tipoorigem"));
        mediaDescontoTurmaRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_datavencimento")));
        mediaDescontoTurmaRelVO.setValor(dadosSQL.getDouble("contareceber_valor"));
        mediaDescontoTurmaRelVO.setNomeCurso(dadosSQL.getString("curso_nome"));
        mediaDescontoTurmaRelVO.setMatricula(dadosSQL.getString("matricula_matricula"));
        mediaDescontoTurmaRelVO.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma_nome"));
        mediaDescontoTurmaRelVO.setSituacao(dadosSQL.getString("contareceber_situacao"));
        mediaDescontoTurmaRelVO.setParcela(dadosSQL.getString("contareceber_parcela"));
        return mediaDescontoTurmaRelVO;
    }

    /**
     * Operação reponsável por retornar o arquivo (caminho e nome) correspondente ao design do relatóio criado
     * pelo IReport.
     */
    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico");
    }

    public static String getIdEntidade() {
        return ("MediaDescontoTurmaRel");
    }
    
	private String montarSqlSituacoes(List<String> situacoes) {
		String sql = "";
		if (Uteis.isAtributoPreenchido(situacoes)) {
			sql = sql + " AND contareceber.situacao IN ( ";
			int contador = 0;
			String conteudoLista = "";
			for (String situacao : situacoes) {
				conteudoLista = conteudoLista + "'" + situacao + "',";

				if (situacoes.size() - 1 == contador) {
					sql = sql + conteudoLista.substring(0, conteudoLista.length() - 1) + ")";
				}
				contador++;
			}
		}
		return sql;
	}
}
