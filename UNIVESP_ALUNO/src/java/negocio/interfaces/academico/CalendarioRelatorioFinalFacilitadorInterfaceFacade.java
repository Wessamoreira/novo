package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface CalendarioRelatorioFinalFacilitadorInterfaceFacade {
    
    
    void persistir(CalendarioRelatorioFinalFacilitadorVO calendarioRegistroAulaVO, UsuarioVO usuarioVO) throws Exception;
    void excluir(CalendarioRelatorioFinalFacilitadorVO calendarioRegistroAulaVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
    
	CalendarioRelatorioFinalFacilitadorVO consultarPorRelatorioFacilitadoresRegistroUnico(Integer disicplina, String situacao, String ano, String semestre, String mes, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	Integer consultarTotalRegistro(Integer disciplina, String situacao, String ano, String semestre, String mes, Date dataInicial, Date dataFinal) throws Exception;
	List<CalendarioRelatorioFinalFacilitadorVO> consultar(Integer disciplina, String situacao, String ano, String semestre, String mes, Date dataInicial, Date dataFinal, Boolean validarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception;
	void validarDados(CalendarioRelatorioFinalFacilitadorVO obj) throws Exception;
	Boolean verificarCalendarioEmAbertoPorDisciplinaAnoSemestre(DisciplinaVO disciplinaVO, String ano, String semestre, Date data);
	List<CalendarioRelatorioFinalFacilitadorVO> consultarPorAnoSemestreMesData(String ano, String semestre, String mes, Date dataInicial, Date dataFinal, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}
