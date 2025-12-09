/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package relatorio.negocio.jdbc.processosel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.processosel.AlunosProcessoSeletivoRelVO;
import relatorio.negocio.interfaces.processosel.AlunosProcessoSeletivoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class AlunosProcessoSeletivoRel extends SuperRelatorio implements AlunosProcessoSeletivoRelInterfaceFacade {

    public AlunosProcessoSeletivoRel() {
    }

    public List realizarMontagemListaSelectItemProcessoSeletivo(Integer unidadeEnsinoLogado) throws Exception {
        List<ProcSeletivoVO> resultadoConsulta = getFacadeFactory().getProcSeletivoFacade().consultarPorUnidadeEnsinoLogado(unidadeEnsinoLogado, false,
            Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
        if (resultadoConsulta.isEmpty()) {
            throw new Exception("Não existe nenhum Processo Seletivo cadastrado para a Unidade de Ensino.");
        }
        return UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao");
    }

    public List realizarMontagemListaSelectItemUnidadeEnsino(Integer procSeletivo) throws Exception {
        List<UnidadeEnsinoVO> listaUnidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorProcessoSeletivo(procSeletivo, false,
            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
        return UtilSelectItem.getListaSelectItem(listaUnidadeEnsino, "codigo", "nome");
    }

    public List realizarMontagemListaSelectItemCurso(Integer unidadeEnsino, Integer procSeletivo) throws Exception {
        List<CursoVO> listaCurso = getFacadeFactory().getCursoFacade().consultarPorCodigoUnidadeEnsinoProcSeletivo(unidadeEnsino, procSeletivo, false,
            Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);
        return UtilSelectItem.getListaSelectItem(listaCurso, "codigo", "nome");
    }

    @Override
    public void validarDados(UnidadeEnsinoVO unidadeEnsino, ProcSeletivoVO procSeletivoVO, AlunosProcessoSeletivoRelVO alunosProcessoSeletivoRelVO, Date dataInicio, Date dataFim) throws ConsistirException {
        if (procSeletivoVO.getCodigo() == null || procSeletivoVO.getCodigo() == 0) {
            throw new ConsistirException("O Processo Seletivo deve ser informado para a geração do relatório.");
        }
        if (unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            throw new ConsistirException("A Unidade de Ensino deve ser informada para a geração do relatório.");
        }
        if (alunosProcessoSeletivoRelVO.getSituacaoAluno() == null || alunosProcessoSeletivoRelVO.getSituacaoAluno().equals("")) {
            throw new ConsistirException("A Situação do Aluno deve ser informada para a geração do relatório.");
        }
    }

    @Override
    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator + getIdEntidade() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "processosel" + File.separator);
    }

    public static String getIdEntidade() {
        return ("AlunosProcessoSeletivoRel");
    }

    public void inicializarDadosImpressaoPDF(AlunosProcessoSeletivoRelVO obj, Integer procSeletivo, Integer unidadeEnsino, Integer curso) throws Exception {
        ProcSeletivoVO ps = getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(procSeletivo, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null);

        if (curso != 0) {
            CursoVO cursoVO = (getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, null));
            obj.setCurso(cursoVO.getNome());
        }
        if (unidadeEnsino != 0) {
            UnidadeEnsinoVO unidade = (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false,
                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
            obj.setUnidadeEnsino(unidade.getNome());
        }
        obj.setProcSeletivo(ps.getDescricao());

    }

    @Override
    public List<AlunosProcessoSeletivoRelVO> realizarCriacaoObjeto(Integer procSeletivo, Integer unidadeEnsino, Integer curso, Date dataInicio, Date dataFim, String situacaoAluno, int nivelMontarDados) throws Exception {
        List<AlunosProcessoSeletivoRelVO> listaRelatorio = new ArrayList<AlunosProcessoSeletivoRelVO>(0);
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT distinct pessoa.*, procSeletivo.descricao as \"procSeletivo.descricao\", unidadeEnsino.nome as \"unidadeEnsino.nome\" ");
        sb.append(",case when (resultadoPrimeiraOpcao = 'AP') then curso1.nome ");
        sb.append("else case when (resultadoSegundaOpcao = 'AP') then curso2.nome ");
        sb.append("else case when (resultadoTerceiraOpcao = 'AP') then curso3.nome end end end,  ");
        sb.append("case when (resultadoPrimeiraOpcao = 'AP' or  resultadoSegundaOpcao = 'AP' or resultadoTerceiraOpcao = 'AP') then 'Aprovado' else 'Reprovado' end as situacaoAluno ");
        sb.append("FROM Inscricao ");
        sb.append("inner join pessoa on pessoa.codigo = Inscricao.candidato  ");
        sb.append("inner join procSeletivo on procSeletivo.codigo = Inscricao.procSeletivo ");
        sb.append("inner join unidadeEnsino on unidadeEnsino.codigo = inscricao.unidadeEnsino ");
        sb.append("inner join resultadoProcessoSeletivo on resultadoProcessoSeletivo.inscricao = inscricao.codigo ");
        sb.append("left join UnidadeEnsinoCurso as UnidadeEnsinoCurso1 on UnidadeEnsinoCurso1.codigo = inscricao.cursoopcao1 ");
        sb.append("left join UnidadeEnsinoCurso as UnidadeEnsinoCurso2 on UnidadeEnsinoCurso2.codigo = inscricao.cursoopcao2 ");
        sb.append("left join UnidadeEnsinoCurso as UnidadeEnsinoCurso3 on UnidadeEnsinoCurso3.codigo = inscricao.cursoopcao3 ");
        sb.append("left join curso as curso1 on curso1.codigo = unidadeEnsinoCurso1.curso ");
        sb.append("left join curso as curso2 on curso2.codigo = unidadeEnsinoCurso2.curso ");
        sb.append("left join curso as curso3 on curso3.codigo = unidadeEnsinoCurso3.curso ");
        sb.append("where procSeletivo.codigo = ");
        sb.append(procSeletivo.intValue());
        if (unidadeEnsino != 0) {
            sb.append(" and unidadeEnsino.codigo = ");
            sb.append(unidadeEnsino.intValue());
        }
        if (curso != 0) {
            sb.append(" and (curso1.codigo = ");
            sb.append(curso.intValue());
            sb.append(" or curso2.codigo = ");
            sb.append(curso.intValue());
            sb.append(" or curso3.codigo = ");
            sb.append(curso.intValue());
            sb.append(")");
        }
        sb.append(" and (Inscricao.data >= '");
        sb.append(Uteis.getDataJDBC(dataInicio));
        sb.append("')");
        sb.append(" and (Inscricao.data <= '");
        sb.append(Uteis.getDataJDBC(dataFim));
        sb.append("')");
        if (situacaoAluno.equals("aprovados")) {
            sb.append("and (resultadoPrimeiraOpcao = 'AP' or resultadoSegundaOpcao = 'AP' or resultadoTerceiraOpcao = 'AP')");
        }
        if (curso != null && curso == 0) {
            sb.append(" order by ");
            sb.append("case when (resultadoPrimeiraOpcao = 'AP') then curso1.nome ");
            sb.append("else case when (resultadoSegundaOpcao = 'AP') then curso2.nome ");
            sb.append("else case when (resultadoTerceiraOpcao = 'AP') then curso3.nome end end end, pessoa.nome ");

        } else {
            sb.append(" order by pessoa.nome");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public static List<AlunosProcessoSeletivoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<AlunosProcessoSeletivoRelVO> vetResultado = new ArrayList<AlunosProcessoSeletivoRelVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    public static AlunosProcessoSeletivoRelVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        AlunosProcessoSeletivoRelVO obj = new AlunosProcessoSeletivoRelVO();
        obj.setNome(dadosSQL.getString("nome"));
        obj.setTelefone(dadosSQL.getString("telefoneRes"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.setDataNascimento(dadosSQL.getDate("dataNasc"));
        obj.setCpf(dadosSQL.getString("cpf"));
        obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino.nome"));
        obj.setProcSeletivo(dadosSQL.getString("procSeletivo.descricao"));
        obj.setCurso(dadosSQL.getString("case"));
        obj.setSituacaoAluno(dadosSQL.getString("situacaoAluno"));
        return obj;
    }
}
