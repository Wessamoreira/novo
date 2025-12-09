package relatorio.negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.academico.PossiveisFormandosRelVO;

public interface PossiveisFormandosRelInterfaceFacade {

    public List<PossiveisFormandosRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre,String consultarPor, TurmaVO turmaVO, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs) throws Exception;

    public PossiveisFormandosRelVO montarDados(SqlRowSet dadosSQL, TurmaVO turmaVO) throws Exception;

    public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, TurmaVO turmaVO, String campoFiltroPor) throws Exception;

    public String getDescricaoFiltros();
}