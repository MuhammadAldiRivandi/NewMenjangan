package travel.kiri.backend.algorithm;

import java.util.List;

/**
 * Merepresentasikan sebuah penghitung jarak terdekat dengan Dijkstra.
 * Referensi dari dijsktra.cc
 * @author PascalAlfadian
 *
 */
public class Dijkstra {
	/**
	 * Class constructor. All allocations should go here.
	 * @param graph the list of nodes, specifying the graph
	 * @param startNode reference to the starting node
	 * @param finishNode reference to the finish node
	 * @param computeMemorySize whether to compute the memory size required for this instance or not.
	 */
	
	public static final int DIJKSTRA_NULLNODE = -1;
	
	List<GraphNode> graph;
	int startNode, finishNode;
	
	NodeInfo[] nodeInfoLinks;
	NodeInfo[] nodesMinHeap;
	int heapsize;
	int numOfNodes;
	/*
	 * constants.. need to be set
	 * */
	static double multiplier_walking;
	static double penalty_transfer;
	
	
	//List<GraphNode> diubah ke Graph
	public Dijkstra(Graph graph, int startNode, int finishNode, boolean computeMemorySize) {
		// FIXME implement this		
		
		this.graph=graph;
		this.startNode=startNode;
		this.finishNode=finishNode;
		this.numOfNodes=graph.size();
		this.nodesMinHeap = new NodeInfo[numOfNodes];
		this.nodeInfoLinks = new NodeInfo[numOfNodes];
		
		for(int i=0;i<numOfNodes;i++)
		{
			nodeInfoLinks[i]=new NodeInfo();
		}
		
		//FIXME Compute Memory Size
	}
	
	/**
	 * Run the Dijkstra algorithm, based on the input. After the function
	 * is run, {@link Dijkstra#getDistance(GraphNode)} will retrieve distance of each node from start
	 * node and {@link Dijkstra#getParent(GraphNode)} will retrieve the parent of each node.
	 * Complexity: O(|E| + |V| log |V|)
	 * @return the distance from source, or {@link Double#POSITIVE_INFINITY} if no path was found.
	 */
	public double runAlgorithm() {
		// FIXME implement this
		
		heapsize = 0;
		for(int i=0;i<numOfNodes;i++)
		{
			NodeInfo ni = nodeInfoLinks[i];
			ni.baseIndex=i;
			ni.heapIndex=i;
			ni.distance=Double.POSITIVE_INFINITY;
			ni.parent=DIJKSTRA_NULLNODE;
			nodesMinHeap[heapsize++] = ni;
		}
		nodeInfoLinks[startNode].distance = 0;
		//heapify!
		
		for(int i=heapsize/2-1; i>=0; i--)
		{
			heapPercolateDown(i);
		}
		
		NodeInfo currentNode;
		
		do
		{
			//printHeap();
			currentNode = heapDeleteMin();
			System.out.println("Current Node: "+currentNode);
			if(currentNode == null || currentNode.baseIndex == finishNode)
			{
				System.out.println("AAAAAAAAAAAAAAAAAAAAA"+currentNode);
				break;
			}
			
			FastLinkedList<GraphEdge> edges = graph.get(currentNode.baseIndex).edges;
			
			for(GraphEdge edge : edges)
			{
				//double weight = edge.getWeight();
				double weight = getWeight(edge);
				//System.out.print((currentNode.distance)+"+"+(weight)+"="+(currentNode.distance + weight) +"<"+nodeInfoLinks[edge.node].distance+" "+(currentNode.distance + weight < nodeInfoLinks[edge.node].distance)+" ");
				if(currentNode.distance + weight < nodeInfoLinks[edge.node].distance)
				{
					nodeInfoLinks[edge.node].distance = currentNode.distance+weight;
					nodeInfoLinks[edge.node].parent = currentNode.baseIndex;
					heapPercolateUp(nodeInfoLinks[edge.node].heapIndex);
					//System.out.println("Edge: "+edge.node+" "+nodeInfoLinks[edge.node].distance);
				}
				else
				{
					//System.out.println();
				}
			}
			
		}while(currentNode.distance!=Double.POSITIVE_INFINITY);
		
		for(int i=0;i<nodeInfoLinks.length;i++)
		{
			//System.out.println("Node "+i+" : "+nodeInfoLinks[i]);
		}
		
		System.out.println("Dist: "+nodeInfoLinks[finishNode].distance);
		return nodeInfoLinks[finishNode].distance;
	}
	
	/**
	 * Retrieves the parent of a particular node.
	 * @param node the node to check.
	 * @return the parent of requested node, or null if it has no parent.
	 */
	public int getParent(int node) {
		// FIXME implement this
		return nodeInfoLinks[node].parent;
	}

	/**
	 * Retrieves the distance of a particular node.
	 * @param node the node to check.
	 * @return the distance of this node from the starting node.
	 */
	public double getDistance(int node) {
		// FIXME implement this
		return nodeInfoLinks[node].distance;
	}
	
	public double getWeight(GraphEdge edge)
	{
		switch (edge.type)
		{
		case 0:
			return edge.weight;
		case 1: 
			return multiplier_walking * (penalty_transfer + edge.weight);
		default: break;
		}
		return Double.POSITIVE_INFINITY;
	}
	
	void heapPercolateDown(int index)
	{
		int minIndex;
		boolean ok = true;
		
		while(ok)
		{
			minIndex = index;
			//kalau ada anak kiri DAN kalau distance anak kiri lebih kecil
			if((index*2+1<heapsize) && nodesMinHeap[index*2+1].distance < nodesMinHeap[minIndex].distance)
			{
				minIndex = index*2+1;
			}

			//kalau ada anak kanan DAN kalau distance anak kanan lebih kecil
			if((index*2+2<heapsize) && nodesMinHeap[index*2+2].distance < nodesMinHeap[minIndex].distance)
			{
				minIndex = index*2+2;
			}
			
			//kalau memang bukan yang paling kecil maka swap
			if(ok = (minIndex != index))
			{
				//swap!
				NodeInfo temp = nodesMinHeap[index];
				nodesMinHeap[index] = nodesMinHeap[minIndex];
				nodesMinHeap[minIndex] = temp;
				
				//swap the heap index
				int tmp = nodesMinHeap[index].heapIndex;
				nodesMinHeap[index].heapIndex = nodesMinHeap[minIndex].heapIndex;
				nodesMinHeap[minIndex].heapIndex = tmp;
				
				index = minIndex;
			}
		}
		
		
	}
	
	void heapPercolateUp(int index)
	{
		//selama belum yang paling atas DAN yang di atasnya lebih besar
		while((index-1)/2 >=0 && nodesMinHeap[(index-1)/2].distance > nodesMinHeap[index].distance)
		{
			//swap!
			NodeInfo temp = nodesMinHeap[index];
			nodesMinHeap[index] = nodesMinHeap[(index-1)/2];
			nodesMinHeap[(index-1)/2] = temp;
			
			//swap the heap index
			int tmp = nodesMinHeap[index].heapIndex;
			nodesMinHeap[index].heapIndex = nodesMinHeap[(index-1)/2].heapIndex;
			nodesMinHeap[(index-1)/2].heapIndex = tmp;
			
			index = (index-1)/2;
		}
	}
	
	NodeInfo heapDeleteMin()
	{
		if(heapsize==0)
		{
			return null;
		}
		
		NodeInfo ret = nodesMinHeap[0];
		
		nodesMinHeap[0] = nodesMinHeap[heapsize - 1];
		heapsize--;
		heapPercolateDown(0);
		
		return ret;
	}
	
	public void printHeap()
	{
		System.out.println("========HEAP======");
		
		for(int i=0;i<5;i++)
		{
			System.out.println(nodesMinHeap[i]);
		}
		
		System.out.println("==================");
	}
	
	public void setParam(double multiplier_walking, double penalty_transfer)
	{
		//diset di edgenya
		this.multiplier_walking=multiplier_walking;
		this.penalty_transfer=penalty_transfer;
	}
	
	
	String getString(int node)
	{
		return nodeInfoLinks[node].toString();
	}
	
	
	private static class NodeInfo
	{
		int baseIndex;
		int heapIndex;
		double distance;
		int parent;
		
		public NodeInfo()
		{
			
		}
		
		public String toString()
		{
			String t="";
			
			t+="point="+baseIndex+" distance="+distance+ " parent="+parent;
			
			return t;
		}
		
	}
}