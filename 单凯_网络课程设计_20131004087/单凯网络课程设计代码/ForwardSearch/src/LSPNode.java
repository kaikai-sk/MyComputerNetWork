/**
 * lsp��Ľڵ�
 * @author ����
 *
 */
public class LSPNode
{
	String neibor;//���ڽڵ�
	double cost;//����

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
