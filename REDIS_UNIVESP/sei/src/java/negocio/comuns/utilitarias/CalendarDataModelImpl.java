package negocio.comuns.utilitarias;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;

import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;

import controle.arquitetura.SuperControle;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;

public class CalendarDataModelImpl extends SuperControle implements CalendarDataModel {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.richfaces.component.CalendarDataModel#getData(java.util.Date[])
	 */

	private CalendarDataModelItem[] items;

	private String currentDescription;
	private String currentShortDescription;
	private Date currentDate;
	private boolean currentDisabled;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.richfaces.model.CalendarDataModel#getData(java.util.Date[])
	 */
	public CalendarDataModelItem[] getData(Date[] dateArray) {
		if (dateArray == null) {
			return null;
		}
		if (items == null) {
			items = new CalendarDataModelItem[dateArray.length];
			for (int i = 0; i < dateArray.length; i++) {
				items[i] = createDataModelItem(dateArray[i]);
			}
		}
		return items;
	}

	/**
	 * @param date
	 * @return CalendarDataModelItem for date
	 */
	@SuppressWarnings("unchecked")
	protected CalendarDataModelItem createDataModelItem(Date date) {
		try {
			CalendarDataModelItemImpl item = new CalendarDataModelItemImpl();
			Map data = new HashMap();
			String imagem = "";
			List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
			calendarioAtividadeMatriculaVOs = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarCalendariosDoDia(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodo() ,date);
			if (calendarioAtividadeMatriculaVOs.isEmpty()) {
				data.put("NADA_CONSTA", "");
				data.put("ACESSO_CONTEUDO_ESTUDO", TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO.getCaminhoImagem());
			}
			for (CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO : calendarioAtividadeMatriculaVOs) {
				data.put(calendarioAtividadeMatriculaVO.getTipoCalendarioAtividade(), "../../resources/imagens/ead/calendarioatividadematricula/" + calendarioAtividadeMatriculaVO.getTipoCalendarioAtividade().getCaminhoImagem());
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			item.setDay(c.get(Calendar.DAY_OF_MONTH));
			item.setEnabled(true);
			item.setStyleClass("rel-hol");
			item.setData(data);
			return item;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.richfaces.model.CalendarDataModel#getToolTip(java.util.Date)
	 */
	public Object getToolTip(Date date) {

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return items
	 */
	public CalendarDataModelItem[] getItems() {
		return items;
	}

	/**
	 * @param setter
	 *            for items
	 */
	public void setItems(CalendarDataModelItem[] items) {
		this.items = items;
	}

	/**
	 * @param valueChangeEvent
	 *            handling
	 */
	public void valueChanged(ValueChangeEvent event) {
		setCurrentDate((Date) event.getNewValue());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
	}

	/**
	 * Storing changes action
	 */
	public void storeDayDetails() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
	}

	/**
	 * @return currentDescription
	 */
	public String getCurrentDescription() {
		return currentDescription;
	}

	/**
	 * @param currentDescription
	 */
	public void setCurrentDescription(String currentDescription) {
		this.currentDescription = currentDescription;
	}

	/**
	 * @return currentDisabled
	 */
	public boolean isCurrentDisabled() {
		return currentDisabled;
	}

	/**
	 * @param currentDisabled
	 */
	public void setCurrentDisabled(boolean currentDisabled) {
		this.currentDisabled = currentDisabled;
	}

	/**
	 * @return currentShortDescription
	 */
	public String getCurrentShortDescription() {
		return currentShortDescription;
	}

	/**
	 * @param currentShortDescription
	 */
	public void setCurrentShortDescription(String currentShortDescription) {
		this.currentShortDescription = currentShortDescription;
	}

	/**
	 * @return currentDate
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate
	 */
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
}
