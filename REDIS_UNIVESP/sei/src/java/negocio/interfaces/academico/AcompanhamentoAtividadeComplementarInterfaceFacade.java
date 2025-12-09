package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;

public interface AcompanhamentoAtividadeComplementarInterfaceFacade {

	public List<RegistroAtividadeComplementarMatriculaVO> consultar(Integer curso, Integer codigoTurma, String ano, String semestre, String situacao, String matricula, Integer codigoTipoAtividadeComplementar, SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatriculaEnum, boolean controlarAcesso, Integer codigoGradeCurricular, DataModelo dataModelo, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception;
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatriculaVisaoAluno(String matricula, Integer codigoTipoAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	String getSqlTotalHorasConsideradas();

	String getSqlTotalHorasRealizadas();

	String getSqlTotalHorasIndeferido();

	String getSqlTotalHorasAguardandoDeferimento();
	
	void validarDadosCargaHorariaRealizada(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaIncluirVO, List<RegistroAtividadeComplementarMatriculaVO> ListaConsultaRegistroAtividadeComplementarMatriculaVOs, List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs) throws Exception;

	RegistroAtividadeComplementarMatriculaVO consultarCargaHorariaTotal(String matricula, boolean controlarAcesso, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception;

}
