package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.TurmaDisciplinaConteudoVO;

public interface TurmaDisciplinaConteudoFacade {

	void incluir(TurmaDisciplinaConteudoVO TurmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO) throws Exception;

	void alterar(TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	TurmaDisciplinaConteudoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<TurmaDisciplinaConteudoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	TurmaDisciplinaConteudoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void excluir(TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	Integer consultarConteudoDaDisciplinaAnoSemestre(Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception;

	List<TurmaDisciplinaConteudoVO> consultarConfiguracoesConteudoTurma(Integer codigoTurma, UsuarioVO usuarioLogado) throws Exception;

	List<MatriculaPeriodoTurmaDisciplinaVO> consultarMatriculaPeriodoTurmaDisciplina(String codigoMatPerTurmaDiscAlunoConteudoCorreto,UsuarioVO usuarioLogado);

	public void alterarTurmaDisciplinaConteudoUsuarioDataInclusao(TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO,
			boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public TurmaDisciplinaConteudoVO consultarTurmaDisciplinConteudoPorConteudo(Integer conteudoSugerido, boolean b, UsuarioVO usuarioLogadoClone) throws Exception;

	public String alterarALunoConteudoDisciplinaDiferente(List listaConsulta3, TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO, String string, UsuarioVO usuarioVO , Integer conteudoSugeridoAlteracao) throws Exception;
}
