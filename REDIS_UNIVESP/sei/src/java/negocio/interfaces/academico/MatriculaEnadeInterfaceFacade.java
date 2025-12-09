package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaEnadeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

public interface MatriculaEnadeInterfaceFacade {
	public void incluir(final MatriculaEnadeVO obj, UsuarioVO usuario) throws Exception;
	public void alterar(final MatriculaEnadeVO obj,UsuarioVO usuario) throws Exception;
	public void excluir(MatriculaEnadeVO obj,UsuarioVO usuario) throws Exception;
	public List<MatriculaVO> consultar(String matricula, List<CursoVO> cursos, Integer turma, List<UnidadeEnsinoVO> unidades, List<TurnoVO> turnos, String periodoLetivoInicial, String periodoLetivoFinal, String ano,FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String scpfAluno ,UsuarioVO usuarioVO) throws Exception;
	public void removerMatriculaEnade(MatriculaVO matriculaVO, MatriculaEnadeVO matriculaEnadeVO);
	public void alterarMatriculaEnade(MatriculaVO matriculaVO, List<MatriculaEnadeVO> objetos, UsuarioVO usuario) throws Exception;
	public void adicionarObjMatriculaEnadeVOs(MatriculaEnadeVO matriculaEnadeVO, List<MatriculaEnadeVO> listaMatriculaEnadeVOs) throws Exception;
	public void removerEnade(MatriculaEnadeVO matriculaEnadeVO, List<MatriculaEnadeVO> listaMatriculaEnadeVOs);
	public void persistir(List<MatriculaVO> listaMatriculaVOs, List<MatriculaEnadeVO> listaMatriculaEnadeVOs, List<MatriculaEnadeVO> listaMatriculaArquivoEnadeVOs, UsuarioVO usuario) throws Exception;
	public void preencherTodosListaAluno(List<MatriculaVO> listaMatriculaVOs);
	public void desmarcarTodosListaAluno(List<MatriculaVO> listaMatriculaVOs);
	public void validarDadosAvancarTelaEnade(List<MatriculaVO> listaMatriculaVOs) throws Exception;
	public void validarDadosUnidadeEnsino(Integer unidadeEnsino) throws Exception;
	public List<MatriculaEnadeVO> consultarMatriculaEnadePorMatricula(String matricula, UsuarioVO usuarioVO, Boolean ordemDecrescente);
	public MatriculaEnadeVO consultarMatriculaEnadePorData(String matricula, UsuarioVO usuarioVO, Boolean ordemDecrescente);
	void validarDadosUnidadeEnsino(List<UnidadeEnsinoVO> unidades) throws Exception;
	void excluirEnadeMatricula(MatriculaVO obj, UsuarioVO usuario) throws Exception;
	public MatriculaEnadeVO consultarMatriculaEnadePorMatriculaAluno(String matricula, UsuarioVO usuarioVO) throws Exception;
	void inicializarDadosArquivoMatriculaEnade(ArquivoVO arquivoVO, FileUploadEvent fileUploadEvent, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	public void realizarProcessamentoArquivoExcelMatriculaEnade(FileUploadEvent fileUploadEvent,
			String nomeArquivo, List<MatriculaVO> listaMatriculaVOs ,  List<MatriculaEnadeVO> listaMatriculaArquivoEnadeVOs, List<MatriculaEnadeVO> listaMatriculaArquivoEnadeErroVOs, CursoVO cursoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean b,	ProgressBarVO progressBarVO, UsuarioVO usuarioVO) throws Exception;
	MatriculaVO consultarPorCursoCpf(String cpfAluno, CursoVO curso, UsuarioVO usuarioVO) throws Exception;
	
	public List<MatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception;

}
