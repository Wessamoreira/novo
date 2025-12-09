package negocio.interfaces.administrativo;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.FiltroPainelGestorAcademicoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum;
import negocio.comuns.arquitetura.UsuarioVO;


public interface FiltroPainelGestorAcademicoInterfaceFacade {
    
    void persistir(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademicoVO) throws Exception;
    
    FiltroPainelGestorAcademicoVO consultarFiltroPorUsuario(UsuarioVO usuario) ;

    void consultarDadosMonitoramentoAcademico(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, PeriodicidadeEnum periodicidadeCurso, String ano, String semestre) throws Exception;

    void consultarDadosDetalheMonitoramentoAcademico(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, 
    		List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, 
    		OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao, Integer codigo,    		
    		Integer limit, Integer offset) throws Exception;

	String getSqlPossiveisFormandos(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, String tipoNivelEducacional);
	void consultarMonitoramentoProcessoSeletivo(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre, OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao);

	Integer consultarQuantidadeMonitoramentoAcademicoPreMatriculado(FiltroPainelGestorAcademicoVO filtroPainelGestorAcademico, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre);
	
	/**
	 * @author Carlos Eugênio - 28/10/2016
	 * @param tipoNivelEducacional
	 * @return
	 */
	List<String> consultarNivelEducacionalExistenteInstituicao(String tipoNivelEducacional);
}
