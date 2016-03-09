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
		if(pagesVisited > 0)
			return "PAGES <= " + pagesVisited;
		else
			return "1";
	}
	
	public String getTimeLimitSQL(){
		if(timeOnSite > 0){
			return "STRFTIME('%s',EXITDATE) - STRFTIME('%s',ENTRYDATE) <= " + timeOnSite;
		}
		else
			return "1";
	}
	
	public String getSQL(){
		return getPageLimitSQL() + " AND " + getTimeLimitSQL();
	}
}
