package negocio.interfaces.basico;

import java.util.List;

import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEADIPOGVO;


/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface PessoaEADIPOGInterfaceFacade {
	

    public PessoaEADIPOGVO novo() throws Exception;
    public void incluir(PessoaEADIPOGVO obj) throws Exception;
    public void alterar(PessoaEADIPOGVO obj) throws Exception;
    public void excluir(PessoaEADIPOGVO obj) throws Exception;
    public PessoaEADIPOGVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void persistir(final PessoaEADIPOGVO obj) throws Exception;
    public Boolean verificarExistenciaRegistroPessoaEADIPOG(Integer codAluno) throws Exception;
    public PessoaEADIPOGVO consultarPorDadosAluno(Integer pessoa, String matricula, Integer disciplina, Integer turma, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<PessoaEADIPOGVO> consultarPorSituacao(String situacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void processarStatusAluno (HistoricoVO histTemp) throws Exception;
    public void alterarSituacao(final PessoaEADIPOGVO obj) throws Exception;
}