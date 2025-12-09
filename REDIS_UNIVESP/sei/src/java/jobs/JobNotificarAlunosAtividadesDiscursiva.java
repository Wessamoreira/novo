package jobs;

import java.io.Serializable;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobNotificarAlunosAtividadesDiscursiva extends SuperFacadeJDBC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4995206882463804715L;

	public void notificarAlunosSobrePrazosDeTerminoAtividadeDiscursiva() {
		try {
			SqlRowSet rs = getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAlunosEProfessoresNotificacaoAtividadeDiscursiva();
			
			while(rs.next()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoDiasAntesConclusaoAtividadeDiscursiva(rs, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void criarCalendarioNovasAtividadeDiscursiva() {
		SqlRowSet rs = null;
		try {
			rs = getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarNovasAtividadeDiscursivaParaEadPorJob(new Date());
			montarDadosNovasAtividadeDiscursiva(rs);
			rs = getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarNovasAtividadeDiscursivaParaPresencialPorJob(new Date());
			montarDadosNovasAtividadeDiscursiva(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void montarDadosNovasAtividadeDiscursiva(SqlRowSet rs) throws Exception{
		while(rs.next()) {
			AtividadeDiscursivaVO atividadeDiscursivaVO = new AtividadeDiscursivaVO();
			atividadeDiscursivaVO.setCodigo(rs.getInt("atividadediscursiva.codigo"));
			atividadeDiscursivaVO.setDataLiberacao(rs.getDate("atividadediscursiva.dataliberacao"));
			atividadeDiscursivaVO.setDataLimiteEntrega(rs.getDate("atividadediscursiva.datalimiteentrega"));
			atividadeDiscursivaVO.setTurmaDisciplinaDefinicoesTutoriaOnlineEnum(DefinicoesTutoriaOnlineEnum.valueOf(rs.getString("definicoestutoriaonline")));
			
			MatriculaPeriodoTurmaDisciplinaVO mptd = new MatriculaPeriodoTurmaDisciplinaVO();
			mptd.setCodigo(rs.getInt("matriculaperiodoturmadisciplina.codigo"));
			mptd.getDisciplina().setNome(rs.getString("disciplina.nome"));
			mptd.getMatriculaObjetoVO().getAluno().setNome(rs.getString("pessoa.nome"));
			
			CalendarioAtividadeMatriculaVO calendarioAtividade = new CalendarioAtividadeMatriculaVO();
			if (atividadeDiscursivaVO.getTurmaDisciplinaDefinicoesTutoriaOnlineEnum().isDinamica()) {
				calendarioAtividade.setDataInicio(rs.getDate("calendarioatividadematricula.datainicio"));
				calendarioAtividade.setDataFim(rs.getDate("calendarioatividadematricula.datafim"));
			} else {
				calendarioAtividade.setDataInicio(atividadeDiscursivaVO.getDataLiberacao());
				calendarioAtividade.setDataFim(atividadeDiscursivaVO.getDataLimiteEntrega());
			}
			calendarioAtividade.setAtividadeDiscursivaVO(atividadeDiscursivaVO);
			calendarioAtividade.setMatriculaPeriodoTurmaDisciplinaVO(mptd);
			calendarioAtividade.getResponsavelCadastro().setCodigo(rs.getInt("usuario.codigo"));
			calendarioAtividade.getResponsavelCadastro().getPessoa().setCodigo(rs.getInt("pessoa.codigo"));
			calendarioAtividade.getResponsavelCadastro().getPessoa().setEmail(rs.getString("pessoa.email"));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPeriodoMaximoAtividadeDiscursiva(calendarioAtividade, calendarioAtividade.getResponsavelCadastro());
			
		}
	}
}
