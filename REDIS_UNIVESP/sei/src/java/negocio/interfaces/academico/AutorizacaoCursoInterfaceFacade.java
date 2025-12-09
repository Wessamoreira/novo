package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface AutorizacaoCursoInterfaceFacade {

    public void incluir(final AutorizacaoCursoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(final AutorizacaoCursoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(AutorizacaoCursoVO obj, UsuarioVO usuarioVO) throws Exception;

    public AutorizacaoCursoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception;

    public List consultarPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void realizarAtualizacaoAutorizacaoCursoVOs(List autorizacaoCursoVOs, Integer curso, UsuarioVO usuario) throws Exception;

    public AutorizacaoCursoVO consultarVigentePorDataMatricula(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception;

    public AutorizacaoCursoVO consultarPorCursoDataVigenteMatricula(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception;

    public void validarDadosAutorizacaoCurso(List autorizacaoCursoVOSVerificar, List autorizacaoCursoVOsComparar) throws Exception;

    public AutorizacaoCursoVO consultarPorCursoPos(Integer codigoCurso, int nivelMontarDados) throws Exception;
    
    public AutorizacaoCursoVO consultarPorCursoDataMaisRecente(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception;

	AutorizacaoCursoVO consultarPorCursoDataMaisAntigo(Integer codigoCurso, Date dataMatricula, int nivelMontarDados) throws Exception;
	
	public List<AutorizacaoCursoVO> consultarDataDescPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<AutorizacaoCursoVO> consultarDataPorCurso(Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}
