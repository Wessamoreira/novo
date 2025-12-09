package jobs;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.crm.ComissionamentoTurmaVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

@Service
@Lazy
public class JobAlterarComissionamentoTurma extends SuperFacadeJDBC implements Runnable {

	@Override
	public void run() {
		try {
			List<ComissionamentoTurmaVO> listaCom = getFacadeFactory().getComissionamentoTurmaFacade().consultaRapidaPorTurma("%%", true, 100, null, false, null);
			Iterator i = listaCom.iterator();
			while (i.hasNext()) {
				ComissionamentoTurmaVO c = (ComissionamentoTurmaVO)i.next();
				if (c.getDataPrimeiroPagamento().before(new Date()) && c.getDataUltimoPagamento().after(new Date())) {
					getFacadeFactory().getComissionamentoTurmaFacade().atualizarComissionamentoTurma(0, 0, c.getTurmaVO().getCodigo(), "", null);					
				}
				getFacadeFactory().getComissionamentoTurmaFacade().alterarDataProcComissionamento(c.getCodigo(), true, null);
				String hora = UteisData.getHoraAtual().substring(0,2);
				Integer horaInt = Integer.parseInt(hora);
				if (horaInt > 8) {
					break;
				}
			}
			if (listaCom.isEmpty()) {
				getFacadeFactory().getComissionamentoTurmaFacade().alterarDataProcComissionamento(0, false, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
