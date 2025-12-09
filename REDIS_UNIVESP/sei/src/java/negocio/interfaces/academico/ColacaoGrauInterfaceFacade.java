package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ColacaoGrauInterfaceFacade {

    public ColacaoGrauVO novo() throws Exception;

    public void incluir(ColacaoGrauVO obj, UsuarioVO usuarioVO) throws Exception;

    public void alterar(ColacaoGrauVO obj, UsuarioVO usuarioVO) throws Exception;

    public void excluir(ColacaoGrauVO obj, UsuarioVO usuarioVO) throws Exception;

    public ColacaoGrauVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTitulo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorLocal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorAta(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomePessoa(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ColacaoGrauVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public ColacaoGrauVO consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void inicializarDadosColacaoGrauParaExpedicaoDiploma(ColacaoGrauVO colacaoGrauVO,MatriculaVO matriculaVO);

    public void incluirComProgramacaoFormaturaAluno(MatriculaVO matricula, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, ColacaoGrauVO colacaoGrauVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public ColacaoGrauVO consultarPorMatricula(MatriculaVO matriculaVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	
	public boolean verificarColacaoGrauPertenceMaisDeUmAluno(MatriculaVO matriculaVO,ColacaoGrauVO colacaoGrauVO) throws Exception;
	
	public void executarAlteracaoDataColacaoGrauMatriculaPorColacaoGrau(Integer colacaoGrau, UsuarioVO usuarioVO) throws Exception;
	
}
