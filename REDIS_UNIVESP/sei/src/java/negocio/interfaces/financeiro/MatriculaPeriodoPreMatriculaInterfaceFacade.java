package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.MatriculaPeriodoPreMatriculaVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;

public interface MatriculaPeriodoPreMatriculaInterfaceFacade {

    public MatriculaPeriodoVencimentoVO novo() throws Exception;

    public void incluir(MatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(MatriculaPeriodoVencimentoVO obj, boolean modificarSituacaoContaReceber, UsuarioVO usuario) throws Exception;

    public void excluir(MatriculaPeriodoVencimentoVO obj, UsuarioVO usuario) throws Exception;

    public void excluirPorTipoOrigemCodOrigem(String tipoOrigem, Integer codOrigem, UsuarioVO usuario) throws Exception;

    public void excluirContasReceberTipoOrigemCodigoOrigem(String tipoOrigem, Integer codigoOrigem, String situacao, UsuarioVO usuario) throws Exception;

    public void excluir(Integer codigoContaReceber, UsuarioVO usuario) throws Exception;

    public MatriculaPeriodoPreMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String idEntidade);
}
