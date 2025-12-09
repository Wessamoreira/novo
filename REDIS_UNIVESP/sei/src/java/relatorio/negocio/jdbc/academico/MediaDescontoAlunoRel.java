package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.MediaDescontoAlunoRelVO;
import relatorio.negocio.comuns.academico.MediaDescontoTurmaRelVO;
import relatorio.negocio.interfaces.academico.MediaDescontoAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class MediaDescontoAlunoRel extends SuperRelatorio implements MediaDescontoAlunoRelInterfaceFacade {

    public MediaDescontoAlunoRel() {
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.MediaDescontoAlunoRelInterfaceFacade#emitirRelatorioAlunosCurso()
     */
    public List<MediaDescontoAlunoRelVO> criarObjeto(MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO, UsuarioVO usuarioVO) throws Exception {
        MediaDescontoAlunoRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
        List<MediaDescontoAlunoRelVO> listaMediaDescontoAlunoRelVO = new ArrayList<MediaDescontoAlunoRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizadaContaReceberAlunos(mediaDescontoAlunoRelVO);
        while (dadosSQL.next()) {
            listaMediaDescontoAlunoRelVO.add(montarDados(dadosSQL));
        }
        return listaMediaDescontoAlunoRelVO;
    }
    
    public static void validarDados(MediaDescontoAlunoRelVO obj) throws ConsistirException{
        if (obj.getNomeAluno().equals("")){
            throw new ConsistirException(UteisJSF.internacionalizar("Informe o ALUNO"));
        }
    }

    public MediaDescontoAlunoRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
        MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO = new MediaDescontoAlunoRelVO();
        mediaDescontoAlunoRelVO.setNomeAluno(dadosSQL.getString("pessoa_nome"));
        mediaDescontoAlunoRelVO.setValorDesconto(dadosSQL.getDouble("contareceber_valordesconto"));
        mediaDescontoAlunoRelVO.setTipoOrigem(dadosSQL.getString("contareceber_tipoorigem"));
        mediaDescontoAlunoRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_datavencimento")));
        mediaDescontoAlunoRelVO.setValor(dadosSQL.getDouble("contareceber_valor"));
        mediaDescontoAlunoRelVO.setNomeCurso(dadosSQL.getString("curso_nome"));
        mediaDescontoAlunoRelVO.setCpf(dadosSQL.getString("pessoa_cpf"));
        mediaDescontoAlunoRelVO.setMatricula(dadosSQL.getString("matricula_matricula"));

        return mediaDescontoAlunoRelVO;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.MediaDescontoAlunoRelInterfaceFacade#executarConsultaParametrizadaContaReceberAlunos
     * ()
     */
    public SqlRowSet executarConsultaParametrizadaContaReceberAlunos(MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO) throws Exception {
        String selectStr = "SELECT pessoa.nome AS pessoa_nome, " + "contareceber.valordescontocalculado AS contareceber_valordesconto, " + "contareceber.tipoorigem AS contareceber_tipoorigem, "
                + "contareceber.datavencimento AS contareceber_datavencimento, " + "contareceber.valor AS contareceber_valor, " + "curso.nome AS curso_nome, " + "pessoa.cpf AS pessoa_cpf, "
                + "matricula.matricula AS matricula_matricula " + "FROM contareceber " + "LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) "
                + "LEFT JOIN curso ON (matricula.curso = curso.codigo) " + "LEFT JOIN pessoa ON (matricula.aluno = pessoa.codigo) ";
        selectStr = montarFiltrosRelatorio(selectStr, mediaDescontoAlunoRelVO);
        selectStr += " GROUP BY pessoa_nome, " + "contareceber_valordesconto, " + "contareceber_tipoorigem, " + "contareceber_valor, " + "contareceber_datavencimento, " + "curso_nome, "
                + "pessoa_cpf, " + "matricula_matricula ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
        return tabelaResultado;
    }

    private String montarFiltrosRelatorio(String selectStr, MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO) throws Exception {
        String where = " where ";
        if (mediaDescontoAlunoRelVO.getNomeAluno() != null) {
            selectStr += where + "( pessoa.nome = '" + mediaDescontoAlunoRelVO.getNomeAluno() + "' AND contareceber.valordesconto <> 0) ";
        }
        return selectStr;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.MediaDescontoAlunoRelInterfaceFacade#consultarPorCodigoAluno(negocio.comuns.
     * academico.MatriculaVO, java.lang.Integer)
     */
    public MediaDescontoAlunoRelVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        PessoaVO obj = new PessoaVO();
        obj = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(matricula.getAluno().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        MatriculaPeriodoVO matPeriodo = new MatriculaPeriodoVO();
        List lista = new ArrayList(0);
        lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(matricula.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,configuracaoFinanceiroVO, usuarioVO);
        matPeriodo = (MatriculaPeriodoVO) lista.get(0);
        return montarDados(obj, matPeriodo, matricula);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * relatorio.negocio.jdbc.academico.MediaDescontoAlunoRelInterfaceFacade#montarDados(negocio.comuns.basico.PessoaVO,
     * negocio.comuns.academico.MatriculaPeriodoVO, negocio.comuns.academico.MatriculaVO)
     */
    public MediaDescontoAlunoRelVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception {
        MediaDescontoAlunoRelVO obj = new MediaDescontoAlunoRelVO();
        obj.setMatricula(matPeriodo.getMatricula());
        obj.setNomeAluno(pessoa.getNome());
        return obj;
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico");
    }

    public static String getIdEntidade() {
        return ("MediaDescontoAlunoRel");
    }
 
}
