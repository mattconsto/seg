package dashboard.model;

public class BounceFilter{
	private int pagesVisited = 0;
	private int timeOnSite = 0;
	
	public void setPageLimit(int pageLimit){
		pagesVisited = pageLimit;
	}
	
	public void setTimeLimit(int timeLimit){
		timeOnSite = timeLimit;
	}
	
	public String getPageLimitSQL(){
		return pagesVisited > 0 ? "PAGES <= " + pagesVisited : "1";
	}
	
	public String getTimeLimitSQL(){
		return timeOnSite > 0 ? "STRFTIME('%s',EXITDATE) - STRFTIME('%s',ENTRYDATE) <= " + timeOnSite : "1";
	}
	
	public String getSQL(){
		return getPageLimitSQL() + " AND " + getTimeLimitSQL();
	}
}
