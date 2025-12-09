package negocio.interfaces.moodle;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.moodle.OperacaoMoodleVO;
import negocio.comuns.utilitarias.dominios.TipoOperacaoMoodleEnum;
import webservice.moodle.MensagensRSVO;
import webservice.moodle.NotasRSVO;

public interface OperacaoMoodleInterfaceFacade {

	public void incluirOperacaoMoodleBaseMensagensRSVO(MensagensRSVO mensagensRSVO, UsuarioVO usuario);

	public void incluirOperacaoMoodleBaseNotasRSVO(NotasRSVO notasRSVO, UsuarioVO usuario);

	public void carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum tipoOperacaoMoodle, DataModelo dataModeloOperacaoMoodleProcessamentoPendente, DataModelo dataModeloOperacaoMoodleProcessamentoErro);

	public void carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum tipoOperacaoMoodle, DataModelo dataModelo, String tipoConsulta);

	public void realizarProcessamentoOperacaoEnvioMensagemMoodle(OperacaoMoodleVO operacaoMoodle, RegistroExecucaoJobVO registroExecucao, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	public void realizarProcessamentoOperacaoInserirNotas(OperacaoMoodleVO operacaoMoodle, RegistroExecucaoJobVO registroExecucaoJob, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;

	public void realizarOperacaoMoodle(TipoOperacaoMoodleEnum tipoOperacaoMoodle, RegistroExecucaoJobVO registroExecucao) throws Exception;

}
