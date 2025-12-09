package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.HorarioDaTurmaPrincipalRelVO;

public interface HorarioDaTurmaRelInterfaceFacade {

	/**
	 * Método criado para consultar os horarios da turma, contendo a data de inicio da disciplina e o termino, consulta filtrada por turma.
	 * 
	 * @param codigoTurma
	 * @param apresentarProfessor
	 * @return
	 * @throws Exception
	 */
	public List<HorarioDaTurmaPrincipalRelVO> consultarHorarioDaTurma(Integer codigoTurma, boolean apresentarProfessor, boolean apresentarSala, String ano, String semestre, UsuarioVO usuario) throws Exception;

	public void validarDados(TurmaVO turmaVO, String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, String tipoLayout, Date dataBaseHorarioAula) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio(String tipoLayout);

	List<HorarioDaTurmaPrincipalRelVO> criarObjeto(TurmaVO turmaVO, boolean apresentarProfessor, boolean apresentarSala, String ano, String semestre, UsuarioVO usuario, Integer unidadeEnsino, String tipoLayout, Date dataInicio, Date dataTermino) throws Exception;
}
