package negocio.interfaces.recursoshumanos;

import java.util.Date;
import java.util.List;

import org.richfaces.event.FileUploadEvent;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorPostadoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.comuns.recursoshumanos.enumeradores.SituacaoHoraAtividadeExtraClasseEnum;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface AtividadeExtraClasseProfessorPostadoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	public List<AtividadeExtraClasseProfessorPostadoVO> consultarAtividadeExtraClasseProfessorPostadoPorFuncionario(DataModelo dataModelo ,Integer codigoFuncionario, boolean visaoCoordenador, String situacao, Integer codigoCurso, UsuarioVO usuarioVO);

	public Integer consultarTotalAtividadeExtraClasseProfessorPostadoPorFuncionario(DataModelo dataModelo ,Integer codigoFuncionario, String situacao, int codigoCurso, UsuarioVO usuarioVO) throws Exception;

	public List<AtividadeExtraClasseProfessorPostadoVO> consultarAtivadeExtraClassePorSituacaoData(
			SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade, Integer codigoFuncionarioCargo, CursoVO cursoVO) throws Exception;

	public void uploadDocumento(FileUploadEvent upload, AtividadeExtraClasseProfessorPostadoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	public List<AtividadeExtraClasseProfessorPostadoVO> consultarAtividadeExraClasseRelatorio(Date dataInicio,
			Date dataFinal, FuncionarioCargoVO funcionarioCargoVO, String[] situacao) throws Exception;

	public Integer consultarTotalAtivadeExtraClassePorSituacaoData(SituacaoHoraAtividadeExtraClasseEnum situacao, Date dataAtividade, Integer codigoFuncionarioCargo, CursoVO cursoVO) throws Exception;

	public void aprovarAtividadeExtraClasseProfessor(AtividadeExtraClasseProfessorPostadoVO obj, UsuarioVO usuarioVO) throws Exception;

	Integer consultarQuantidadePorAtividadeExtraClasseProfessor(AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) throws Exception;
}