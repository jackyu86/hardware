package com.hw.hardware.common.tree;


/**
 * 树节点结构
 *
 */
public class Node implements Cloneable{
	private Long nodeId;
	private Long parentNodeId;
	private String leader;
	private String nodeName;
	
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	public Long getParentNodeId() {
		return parentNodeId;
	}
	public void setParentNodeId(Long parentNodeId) {
		this.parentNodeId = parentNodeId;
	}
	
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	@Override
	public Node clone(){
		try
		{
			return (Node)super.clone();
		}catch(CloneNotSupportedException e){
			Node node = new Node();
			node.setLeader(this.leader);
			node.setNodeId(this.nodeId);
			node.setNodeName(this.nodeName);
			node.setParentNodeId(this.parentNodeId);
			return node;
		}
	}
}
