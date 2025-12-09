package relatorio.negocio.interfaces.academico;

import java.util.List;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.academico.MediaDescontoAlunoRelVO;

public interface MediaDescontoAlunoRelInterfaceFacade {

	public List<MediaDescontoAlunoRelVO> criarObjeto(MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO, UsuarioVO usuarioVO) throws Exception;

	public SqlRowSet executarConsultaParametrizadaContaReceberAlunos(MediaDescontoAlunoRelVO mediaDescontoAlunoRelVO) throws Exception;

	public MediaDescontoAlunoRelVO consultarPorCodigoAluno(MatriculaVO matricula, Integer nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public MediaDescontoAlunoRelVO montarDados(PessoaVO pessoa, MatriculaPeriodoVO matPeriodo, MatriculaVO matricula) throws Exception;

        public MediaDescontoAlunoRelVO montarDados(SqlRowSet dadosSQL) throws Exception;

    public void setDescricaoFiltros(String string);

    public String getDescricaoFiltros();

}