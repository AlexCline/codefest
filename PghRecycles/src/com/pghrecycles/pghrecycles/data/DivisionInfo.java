package com.pghrecycles.pghrecycles.data;

public class DivisionInfo {
	
	public enum Division {
		SOUTHERN ("Southern"),
		NORTHERN ("Northern"),
		CENTRAL ("Central"),
		EASTERN ("Eastern");
		 
		private final String name;       

	    private Division(String s) {
	        name = s;
	    }

	    public boolean equalsName(String otherName){
	        return (otherName == null)? false:name.equals(otherName);
	    }

	    @Override
		public String toString(){
	       return name;
	    }		
	};
	
	private Division division;
	private String name;
	private YardDebrisSchedule yardDebrisSchedule;
	private RecyclingSchedule recyclingSchedule;
	
	public DivisionInfo() {
		this.division = null;
		this.name = "";
		this.yardDebrisSchedule = null;
		this.recyclingSchedule = null;
	}
	
	public DivisionInfo(Division division, String name,
			YardDebrisSchedule yardDebrisSchedule,
			RecyclingSchedule recyclingSchedule) {
		this.division = division;
		this.name = name;
		this.yardDebrisSchedule = yardDebrisSchedule;
		this.recyclingSchedule = recyclingSchedule;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public YardDebrisSchedule getYardDebrisSchedule() {
		return yardDebrisSchedule;
	}
	public void setYardDebrisSchedule(YardDebrisSchedule yardDebrisSchedule) {
		this.yardDebrisSchedule = yardDebrisSchedule;
	}
	public RecyclingSchedule getRecyclingSchedule() {
		return recyclingSchedule;
	}
	public void setRecyclingSchedule(RecyclingSchedule recyclingSchedule) {
		this.recyclingSchedule = recyclingSchedule;
	}
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}

	@Override
	public String toString() {
		return "DivisionInfo [division=" + division + ", name=" + name
				+ ", yardDebrisSchedule=" + yardDebrisSchedule
				+ ", recyclingSchedule=" + recyclingSchedule + "]";
	}
	
}
