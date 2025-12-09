package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.QuadroComissaoConsultoresRelVO;

public interface QuadroComissaoConsultoresRelInterfaceFacade {

    public List<QuadroComissaoConsultoresRelVO> criarObjeto(Integer codigoUnidadeEnsino, Integer curso, Integer turma, Boolean situacaoPreMatricula, Boolean matRecebida, Boolean matAReceber, UsuarioVO usuarioLogado) throws Exception;

    public void validarDados(Integer unidadeEsnino) throws Exception;
}