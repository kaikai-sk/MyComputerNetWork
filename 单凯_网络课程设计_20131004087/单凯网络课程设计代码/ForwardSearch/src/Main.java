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
	//·��������
	static ArrayList<Router> routerList=new ArrayList<>(); 
	static int routerNum;//·��������
	static ArrayList<Path> fileList=new ArrayList<>();//�ļ�·������
	
	
	public static void main(String[] args) throws Exception
	{
		//������
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		String strTemp;//��ȡ����̨�������ʱ�ַ���
		Path path;//·������
		
		System.out.println("������·����������");
		strTemp=br.readLine();
		routerNum=Integer.parseInt(strTemp);
		
		//ͨ���û�������õ��ļ�·��
		getFilePaths();
		//�����ļ�·�����ϣ���ȡ�ļ�
		readFile();
		
		//����������ʼ·����
		System.out.println("��������ʼ·�������֣�");
		String startName=br.readLine();
		
		//����ǰ�������㷨
		forwardSearch(startName);
		
		//��ӡ������õ�֤ʵ��
		GetRouterFromName(startName).printConfirmTable();
	}

	public Main()
	{
		
	}
	
	/**
	 * ǰ�������㷨
	 * @param startName ��ʼ��·����������
	 */
	private static void forwardSearch(String startName)
	{
		ArrayList<ConfirmAndTentativeNode> confirmed=new ArrayList<>();//֤ʵ��
		ArrayList<ConfirmAndTentativeNode> tentative=new ArrayList<>();//��̽��
		//�õ���ʼ·����
		Router routerStart=GetRouterFromName(startName);
		//����Ƚ���,����
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
		//�õ�nexthop
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
			//ȡ��֤ʵ������һ���ڵ�
			ConfirmAndTentativeNode tempNode=routerStart.confirmed.get(routerStart.confirmed.size()-1);
			//�������������
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
				//����ڵ�����̽���� �� ��������̽���еĽڵ���滻
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
			//����Ƚ���
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
			//����̽��������������
			Collections.sort(tentative,comparator);
			//����̽���е���С���Ǹ��ڵ���뵽֤ʵ����
			confirmed.add(tentative.get(0));
			tentative.remove(0);
		}
		routerStart.setConfirmed(confirmed);
		routerStart.setTentative(tentative);
	}
	
	/**
	 * ����һ���ڵ��Ƿ�����̽���л�֤��ʵ���У�
	 * ����ڷ���λ�á�=0
	 * ������ڷ���-1
	 * @param node	
	 * @param tentative ��̽��
	 * @return
	 */
	private static int isInTentative(ConfirmAndTentativeNode node, ArrayList<ConfirmAndTentativeNode> tentative)
	{
		for (int i = 0; i < tentative.size(); i++)
		{
			if(node.getDestintion().equals(tentative.get(i).getDestintion()))
			{
				//˵����ת�����У���������ת�����е�λ��
				return i;
			}
		}
		//˵������ת������
		return -1;
	}

	/**
	 * ��·�����б��и��������ҵ���Ӧ��·����
	 * @param name ����
	 * @return ��Ӧ��·����
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
	 * �õ�ָ�����ֵ�·������·�����б��е��±�
	 * @return �±꣬��������ڣ�����-1��
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
	
	//�����ļ�·�����ϣ���ȡ�ļ�
	public static void readFile() throws Exception
	{
		System.out.println("���ڶ�ȡlsp�ļ���Ϣ");
		//�����ļ����ϣ�����·��������
		for(int i=0;i<fileList.size();i++)
		{
			String fileName=fileList.get(i).getFileName().toString();
			fileName=fileName.substring(0, fileName.lastIndexOf('.'));
			File file=new File(fileList.get(i).toString());
			Router r=new Router(fileName);
			ArrayList<LSPNode> lsp=new ArrayList<>(); 
					
			//���ļ������ļ����ڵ�ʱ�򣬶�ȡ
			//���ҹ����lsp��
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
			
			//����r��lsp��
			r.setLsp(lsp);
			routerList.add(r);
		}
	}
	
	//ͨ���û�������õ��ļ�·��
	public static void getFilePaths()
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
		String strTemp;
		Path path;
		
		System.out.println("������·���ļ�·��������һ��·����س�");
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
