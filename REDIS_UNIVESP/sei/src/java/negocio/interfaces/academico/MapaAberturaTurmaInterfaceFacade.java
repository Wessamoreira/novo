package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public interface MapaAberturaTurmaInterfaceFacade {

    public void persistirAberturaTurma(TurmaVO obj, TurmaAberturaVO turmaAberturaVO, Date dataTemp, UsuarioVO usuario) throws Exception;

    public List consultarTurma(String campoConsultaTurma, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaTurma, UsuarioVO usuarioLogado) throws Exception;

    public List consultarCurso(String campoConsultaCurso, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaCurso, UsuarioVO usuarioLogado) throws Exception;

    public List<TurmaAberturaVO> consultarAberturaTurma(Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, String situacao, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;

    public String montarGraficoPizza(List<TurmaAberturaVO> listaTurmaAberturaVO) throws Exception;

    public List consultarUsuario(String campoConsultaUsuario, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaUsuario, UsuarioVO usuarioLogado) throws Exception;

    public void enviarEmailUsuario(UsuarioVO usuarioDestinoVO, UnidadeEnsinoVO unidadeEnsinoVO, Boolean enviarComunicadoPorEmail, String mensagemPadraoNotificacao, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;
}
