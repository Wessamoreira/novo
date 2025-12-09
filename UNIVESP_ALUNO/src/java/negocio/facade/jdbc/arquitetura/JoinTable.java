package negocio.facade.jdbc.arquitetura;

import java.util.ArrayList;

public class JoinTable extends ArrayList<JoinTable.Join> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 128526766822948603L;
	
	public JoinTable add(String tableName, String condicaoOn, boolean innerJoin) {
		this.add(new JoinTable.Join(tableName, condicaoOn, innerJoin));
		return this;
	}

	class Join {

	String tableName;	
	String condicaoOn;
	boolean innerJoin;
	
	public Join(String tableName, String condicaoOn, boolean innerJoin) {
		super();
		this.tableName = tableName;
		this.condicaoOn = condicaoOn;
		this.innerJoin = innerJoin;
	}
	
	public String getTableName() {
		if(tableName == null){
			tableName = "";
		}
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getCondicaoOn() {
		if(condicaoOn == null){
			condicaoOn ="";
		}
		return condicaoOn;
	}
	
	public void setCondicaoOn(String condicaoOn) {
		this.condicaoOn = condicaoOn;
	}
	
	public boolean isInnerJoin() {		
		return innerJoin;
	}
	
	public void setInnerJoin(boolean innerJoin) {
		this.innerJoin = innerJoin;
	}
	
	}
	
}
