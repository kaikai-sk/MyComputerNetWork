import java.awt.List;
import java.util.ArrayList;

/**
 * ·������
 * @author ����
 *
 */
public class Router
{
	String name;
	ArrayList<LSPNode> lsp;
	ArrayList<ConfirmAndTentativeNode> confirmed;
	ArrayList<ConfirmAndTentativeNode> tentative;
	
	void printConfirmTable()
	{
		for (int i = 0; i < confirmed.size(); i++)
		{
			System.out.println(confirmed.get(i).toString());
		}
	}
	
	/********************************���췽��***********************************************************/
	public Router()
	{
		super();
		lsp=new ArrayList<>();
		confirmed=new ArrayList<>();
		tentative=new ArrayList<>();
	}
	
	
	/*******************************get �� set ����**********************************************/
	public Router(String name)
	{
		this.name=name;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<LSPNode> getLsp()
	{
		return lsp;
	}
	public void setLsp(ArrayList<LSPNode> lsp)
	{
		this.lsp = lsp;
	}
	public ArrayList<ConfirmAndTentativeNode> getConfirmed()
	{
		return confirmed;
	}
	public void setConfirmed(ArrayList<ConfirmAndTentativeNode> confirmed)
	{
		this.confirmed = confirmed;
	}
	public ArrayList<ConfirmAndTentativeNode> getTentative()
	{
		return tentative;
	}
	public void setTentative(ArrayList<ConfirmAndTentativeNode> tentative)
	{
		this.tentative = tentative;
	}
	
}
