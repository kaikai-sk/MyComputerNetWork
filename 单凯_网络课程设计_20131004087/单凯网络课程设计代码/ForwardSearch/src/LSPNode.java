/**
 * lsp表的节点
 * @author 单凯
 *
 */
public class LSPNode
{
	String neibor;//相邻节点
	double cost;//开销

	public LSPNode(String neibor,double cost)
	{
		this.neibor=neibor;
		this.cost=cost;
	}
	
	public String getNeibor()
	{
		return neibor;
	}
	public void setNeibor(String neibor)
	{
		this.neibor = neibor;
	}
	public double getCost()
	{
		return cost;
	}
	public void setCost(double cost)
	{
		this.cost = cost;
	}
}
