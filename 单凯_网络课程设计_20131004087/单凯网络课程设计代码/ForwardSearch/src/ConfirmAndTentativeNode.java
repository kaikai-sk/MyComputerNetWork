/**
 * ֤ʵ�����̽��Ľڵ�
 * @author ����
 *
 */
public class ConfirmAndTentativeNode
{
	String destintion;//Ŀ��·��
	Double cost;//����
	String nextHop;//��һ��
	

	public ConfirmAndTentativeNode(String destination,double cost, String nextHop)
	{
		this.destintion=destination;
		this.cost=cost;
		this.nextHop=nextHop;
	}
	
	/*************************************���ǵĹ��ߺ���****************************************************/
	public String toString()
	{
		return destintion+"		"+cost.toString()+"		"+nextHop+"\r\n";
	}
	/**********************************get �� set ����*****************************************************/
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
