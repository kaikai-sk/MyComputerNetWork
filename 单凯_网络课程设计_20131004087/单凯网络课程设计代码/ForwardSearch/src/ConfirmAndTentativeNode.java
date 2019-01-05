/**
 * 证实表和试探表的节点
 * @author 单凯
 *
 */
public class ConfirmAndTentativeNode
{
	String destintion;//目的路由
	Double cost;//开销
	String nextHop;//下一跳
	

	public ConfirmAndTentativeNode(String destination,double cost, String nextHop)
	{
		this.destintion=destination;
		this.cost=cost;
		this.nextHop=nextHop;
	}
	
	/*************************************覆盖的工具函数****************************************************/
	public String toString()
	{
		return destintion+"		"+cost.toString()+"		"+nextHop+"\r\n";
	}
	/**********************************get 和 set 方法*****************************************************/
	public String getDestintion()
	{
		return destintion;
	}
	public void setDestintion(String destintion)
	{
		this.destintion = destintion;
	}

	public String getNextHop()
	{
		return nextHop;
	}
	public void setNextHop(String nextHop)
	{
		this.nextHop = nextHop;
	}

	public Double getCost()
	{
		return cost;
	}

	public void setCost(Double cost)
	{
		this.cost = cost;
	}
}
