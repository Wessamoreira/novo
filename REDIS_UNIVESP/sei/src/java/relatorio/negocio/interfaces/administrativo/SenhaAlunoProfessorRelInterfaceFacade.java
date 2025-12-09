/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;
import negocio.comuns.basico.PessoaVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.administrativo.SenhaAlunoProfessorVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 *
 * @author Alberto
 */
public interface SenhaAlunoProfessorRelInterfaceFacade {

    public SenhaAlunoProfessorVO consultarPorNomeCpf(PessoaVO pessoaVO, String tipoPessoa, String caminhoImagem, Integer unidadeEnsino) throws Exception;

    public List consultarPorTipoPessoaUnidadeEnsinoCursoTurnoTurma(String tipoPessoa, String caminhoImagem, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, String ano, String semestre, Date dataInicio, Date dataFim, String periodicidadeCurso, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

    public List montarDadosConsulta(SqlRowSet tabelaResultado, String caminhoImagem, String tipoPessoa) throws Exception;

    public List criarObjetos(Integer campoConsultaFiltro, SenhaAlunoProfessorVO senhaAlunoProfessorVO, List listaSenhaAlunoProfessorVO, String tipoPessoa, String caminhoImagem, Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Date dataInicio, Date dataFim, String ano, String semestre, String periodicidadeCurso, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception;

    public String getDesignIReportRelatorio();
}
