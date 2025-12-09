package negocio.interfaces.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TipoAtividadeComplementarVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TipoAtividadeComplementarInterfaceFacade {

	public TipoAtividadeComplementarVO novo() throws Exception;

	public void incluir(TipoAtividadeComplementarVO tipoAtividadeComplementarVO, UsuarioVO usuario) throws Exception;

	public void alterar(TipoAtividadeComplementarVO tipoAtividadeComplementarVO, UsuarioVO usuario) throws Exception;

	public void excluir(TipoAtividadeComplementarVO tipoAtividadeComplementarVO, UsuarioVO usuario) throws Exception;
	
	public ArrayList<TipoAtividadeComplementarVO> consultar(boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public ArrayList<TipoAtividadeComplementarVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public ArrayList<TipoAtividadeComplementarVO> consultarPorCodigo(int valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public TipoAtividadeComplementarVO consultarPorChavePrimaria(int valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	ArrayList<TipoAtividadeComplementarVO> consultarPorCursoTurmaMatricula(Integer curso, String identificadorTurma, boolean turmaAgrupada, String matricula, Integer tipoAtividadeComplementarAtual, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<TipoAtividadeComplementarVO> consultarTipoAtividadeComplementarComCargaHorariasPeriodoLetivo(Integer tipoAtividadeComplementar, Integer curso, String matricula, PeriodicidadeEnum periodicidadeEnum, Date dataBase, Integer codigoRegistroAtividadeComplementarMatriculaDesconsiderar) throws Exception;

	ArrayList<TipoAtividadeComplementarVO> consultarPorTipoSuperior(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	TipoAtividadeComplementarVO consultarPorNomeTipoAtividade(String valorConsulta, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception;

	TipoAtividadeComplementarVO consultarPorChavePrimariaUnico(int valorConsulta, UsuarioVO usuario) throws Exception;

}
