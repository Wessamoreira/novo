package negocio.interfaces.sad;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.sad.MatriculaDWVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface MatriculaDWInterfaceFacade {
	

    public MatriculaDWVO novo(UsuarioVO usuario) throws Exception;
    public void incluir(MatriculaDWVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(MatriculaDWVO obj,UsuarioVO usuario) throws Exception;
    public void excluir(MatriculaDWVO obj,UsuarioVO usuario) throws Exception;
    public MatriculaDWVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorSemestre(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorDescricaoPeriodoLetivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeTurno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNomeAreaConhecimento(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public SqlRowSet consultaGeracaoRelatorioPizzaBarra(MatriculaDWVO obj, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public SqlRowSet consultaGeracaoRelatorioLinhaTempo(MatriculaDWVO obj, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
}