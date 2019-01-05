import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.text.AbstractDocument.BranchElement;

public class Main
{
	//路由器集合
	static ArrayList<Router> routerList=new ArrayList<>(); 
	static int routerNum;//路由器总数
	static ArrayList<Path> fileList=new ArrayList<>();//文件路径集合
	
	
	public static void main(String[] args) throws Exception
	{
		//输入流
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		String strTemp;//读取控制台输入的临时字符串
		Path path;//路径对象
		
		System.out.println("请输入路由器总数：");
		strTemp=br.readLine();
		routerNum=Integer.parseInt(strTemp);
		
		//通过用户数输入得到文件路径
		getFilePaths();
		//根据文件路径集合，读取文件
		readFile();
		
		//请用输入起始路由器
		System.out.println("请输入起始路由器名字：");
		String startName=br.readLine();
		
		//调用前向搜索算法
		forwardSearch(startName);
		
		//打印出构造好的证实表
		GetRouterFromName(startName).printConfirmTable();
	}

	public Main()
	{
		
	}
	
	/**
	 * 前向搜索算法
	 * @param startName 起始的路由器的名字
	 */
	private static void forwardSearch(String startName)
	{
		ArrayList<ConfirmAndTentativeNode> confirmed=new ArrayList<>();//证实表
		ArrayList<ConfirmAndTentativeNode> tentative=new ArrayList<>();//试探表
		//得到起始路由器
		Router routerStart=GetRouterFromName(startName);
		//构造比较器,升序
		Comparator<LSPNode> comparator1=new Comparator<LSPNode>()
		{

			@Override
			public int compare(LSPNode o1, LSPNode o2)
			{
				if(o1.cost>=o2.cost)
				{
					return 1;
				}
				return -1;
			}
			
		};
		Collections.sort(routerStart.getLsp(),comparator1);
		//得到nexthop
		String strNextHop=routerStart.getLsp().get(0).getNeibor();
		
		confirmed.add(new ConfirmAndTentativeNode(startName,0,"-"));
		routerStart.setConfirmed(confirmed);
		routerStart.setTentative(tentative);
		for (int i = 0; i < routerStart.getLsp().size(); i++)
		{
			tentative.add(new ConfirmAndTentativeNode(routerStart.getLsp().get(i).getNeibor(),
					routerStart.getLsp().get(i).getCost(), 
					routerStart.getLsp().get(i).getNeibor()));
		}
		while(!tentative.isEmpty())
		{
			//取得证实表的最后一个节点
			ConfirmAndTentativeNode tempNode=routerStart.confirmed.get(routerStart.confirmed.size()-1);
			//获得了他的名字
			String nextName=tempNode.getDestintion();
			Router routerTemp=GetRouterFromName(nextName);
			for(int i=0;i<routerTemp.lsp.size();i++)
			{
				if(!routerStart.getLsp().contains(new LSPNode(tempNode.destintion, tempNode.cost)))
				{
					nextName=strNextHop;
				}
				ConfirmAndTentativeNode node=new ConfirmAndTentativeNode(routerTemp.lsp.get(i).neibor,
						tempNode.cost+routerTemp.lsp.get(i).cost,
						nextName);
				int tempIndex,tempIndex1;
				//如果节点在试探表中 且 开销《试探表中的节点就替换
				if((tempIndex=isInTentative(node,tentative))>=0 &&
						node.getCost()<tentative.get(tempIndex).getCost())
				{
					tentative.remove(tempIndex);
					tentative.add(node);
				}
				else if ((tempIndex = isInTentative(node, tentative)) < 0
						&& (tempIndex1 = isInTentative(node, confirmed)) < 0)
				{
					tentative.add(node);
				}
			}
			//构造比较器
			Comparator<ConfirmAndTentativeNode> comparator=new Comparator<ConfirmAndTentativeNode>()
			{

				@Override
				public int compare(ConfirmAndTentativeNode o1, ConfirmAndTentativeNode o2)
				{
					if(o1.cost>=o2.cost)
					{
						return 1;
					}
					return -1;
				}
				
			};
			//对试探表按开销进行排序
			Collections.sort(tentative,comparator);
			//将试探表中的最小的那个节点加入到证实表中
			confirmed.add(tentative.get(0));
			tentative.remove(0);
		}
		routerStart.setConfirmed(confirmed);
		routerStart.setTentative(tentative);
	}
	
	/**
	 * 考察一个节点是否在试探表中或证事实表中，
	 * 如果在返回位置》=0
	 * 如果不在返回-1
	 * @param node	
	 * @param tentative 试探表
	 * @return
	 */
	private static int isInTentative(ConfirmAndTentativeNode node, ArrayList<ConfirmAndTentativeNode> tentative)
	{
		for (int i = 0; i < tentative.size(); i++)
		{
			if(node.getDestintion().equals(tentative.get(i).getDestintion()))
			{
				//说明在转发表中，并返回再转发表中的位置
				return i;
			}
		}
		//说明不再转发逼忠
		return -1;
	}

	/**
	 * 从路由器列表中根据名字找到对应的路由器
	 * @param name 名字
	 * @return 对应的路由器
	 */
	public static Router GetRouterFromName(String name)
	{
		int index=-1;
		index=GetIndexOfRouter(name);
		Router r=routerList.get(index);
		return r;
	}
	
	public static int GetIndexOfLSP(String strName,ArrayList<LSPNode> arrayList)
	{
		for (int i = 0; i < arrayList.size(); i++)
		{
			if(arrayList.get(i).getNeibor().equals(strName))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 得到指定名字的路由器在路由器列表中的下表
	 * @return 下标，如果不存在，返回-1；
	 */
	public static int GetIndexOfRouter(String startName)
	{
		for (int i = 0; i < routerNum; i++)
		{
			if(routerList.get(i).getName().equals(startName))
			{
				return i;
			}
		}
		return -1;
	}
	
	//根据文件路径集合，读取文件
	public static void readFile() throws Exception
	{
		System.out.println("正在读取lsp文件信息");
		//遍历文件集合，构造路由器集合
		for(int i=0;i<fileList.size();i++)
		{
			String fileName=fileList.get(i).getFileName().toString();
			fileName=fileName.substring(0, fileName.lastIndexOf('.'));
			File file=new File(fileList.get(i).toString());
			Router r=new Router(fileName);
			ArrayList<LSPNode> lsp=new ArrayList<>(); 
					
			//是文件并且文件存在的时候，读取
			//并且构造出lsp表
			if(file.exists() && file.isFile())
			{
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),
						"GBK"));
				String lineTxt=null;
				while ((lineTxt=br.readLine())!=null)
				{
					String[] temp=lineTxt.split(" ");
					LSPNode lspNode=new LSPNode(temp[0],Double.parseDouble(temp[1]));
					lsp.add(lspNode);
				}
			}
			
			//设置r的lsp表
			r.setLsp(lsp);
			routerList.add(r);
		}
	}
	
	//通过用户数输入得到文件路径
	public static void getFilePaths()
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		String strTemp;
		Path path;
		
		System.out.println("请输入路由文件路径，输完一条路径后回车");
		int temp=routerNum;
		while(temp-->0)
		{
			try
			{
				strTemp=br.readLine();
				path=Paths.get(strTemp);
				fileList.add(path);
			} 
			catch (Exception e)
			{
				System.err.println(e.getStackTrace());
			}
		}
	}
}
