package reference;

/**
 *
 * WAVLTree
 *
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 *
 */

public class WAVLTree {
	
	private int treeSize;
	public IWAVLNode root, min,max; //TODO - change to private
  /**
   * public boolean empty()
   *
   * returns true if and only if the tree is empty
   *
   */
  public boolean empty() {
    if (this.root == null) {
    	return true;
    }
	  return false; // to be replaced by student code
  }

 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public IWAVLNode nodeSearch (int k) {
	  if (root == null) {
		  return null;
	  }
	  IWAVLNode current = this.root;
	  while (current.getRank() != -1) {
			if (current.getKey() == k) {
				return current;
			}
			else if (current.getKey()>k) {
				current = current.getLeft();
			}
			else if (current.getKey()<k) {
				current = current.getRight();
			}
		}
		return null;
		}  
  
  public String search(int k)
  {  
	IWAVLNode s = nodeSearch(k);
	if (s!= null) {
		return s.getValue();
	}
	return null;
  }
	
  

  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
   public int insert(int k, String i) {
	   if (this.empty()) { //if tree is empty - then inserted node is root
		   IWAVLNode root = new WAVLNode(i,k);
		   this.root = root;
		   this.min = root;
		   this.max = root;
		   root.setSubTreeSize(1);
		   treeSize = 1;
		   return 0;
	   }
		IWAVLNode current = (WAVLNode) this.root; //defines the root for search
		int actions = 0;
		boolean a = true;
		while (a) { // checks where to put the new node and keeps the parent node for later rebalance actions
			if (current.getKey() == k) {
				return (-1);
			}
			else if (current.getKey()>k) {
				if (current.getLeft().getRank()==-1) {//current has no left child
					IWAVLNode child = new WAVLNode(i,k);
					child.setParent(current);
					current.setLeft(child);
					a = false;
					}
				else {
				current = current.getLeft();
				}
			}
			else if (current.getKey()<k) {
				if (current.getRight().getRank()==-1) {//current has no right child
					IWAVLNode child = new WAVLNode(i,k);
					current.setRight(child);
					child.setParent(current);
					a = false;
				}
				else {
				current = current.getRight();
				}
			}
		}
		// ** rebalancing steps taken from now till return
		actions = this.rebalance(current, actions);
//		this.subTreeSize(root);
		this.findMax();
		this.findMin();
		root.getSubtreeSizeFinal();
		treeSize = root.getSubtreeSize();
	  return actions;
   }
   public int rebalance (IWAVLNode current, int actions) {
	   int rankDiffRight = current.getRank() - current.getRight().getRank(); 
		int rankDiffLeft = current.getRank() - current.getLeft().getRank();
		while (!((rankDiffRight>0 && rankDiffRight<=2) && (rankDiffLeft>0 && rankDiffLeft<=2))) {
			// ** checks if there is a need for rebalancing according to (2,2) nodes in WAVL tree
			if ((rankDiffLeft == 0 && rankDiffRight<2) || (rankDiffRight == 0 && rankDiffLeft<2)) { //promotes
				current.setRank(current.getRank()+1);
				actions++;
			}
			else { //rotations
				boolean isRoot = (current == this.root);
				if ((rankDiffLeft == 0) && (rankDiffRight ==2)) { //if the problematic node is on the left
					if (current.getLeft().getRank() - current.getLeft().getLeft().getRank()==1) { // single rotation
						rightRotate(current);
						postRotRankFix(current, current.getParent());
						actions++;
					}
					else { //double rotation
						leftRotate(current.getLeft());
						postRotRankFix (current.getLeft().getLeft(), current.getLeft());
						rightRotate(current);
						postRotRankFix(current, current.getParent());
						actions += 2;
					}
				}
				if ((rankDiffRight == 0) && (rankDiffLeft ==2)) { // if the problematic node is on the right
					if (current.getRight().getRank() - current.getRight().getRight().getRank()==1) { // single rotation
						leftRotate(current);
						postRotRankFix(current, current.getParent());
						actions++;
					}
					else { //double rotation
						rightRotate(current.getRight());
						postRotRankFix(current.getRight().getRight(), current.getRight());
						leftRotate(current);
						postRotRankFix(current, current.getParent());
						actions += 2;
					}
				
			}
				if(current.getParent() == null) {
					this.root = current;
				}
		}
			rankDiffRight = current.getRank() - current.getRight().getRank(); 
			rankDiffLeft = current.getRank() - current.getLeft().getRank();
			if (current.getParent() == null) {
				break;
			}
			else {
				current = current.getParent();
			}
			rankDiffRight = current.getRank() - current.getRight().getRank(); 
			rankDiffLeft = current.getRank() - current.getLeft().getRank();
		}
	   return actions;
	   
   }
   
   public void rightRotate (IWAVLNode x) {
	   boolean isRoot = x==root;
	   IWAVLNode y = x.getLeft();
	   IWAVLNode yRight = y.getRight();
	   y.setParent(x.getParent());
	   if (x.getParent() != null) {
		   if (x.getParent().getKey() > x.getKey()) {
			   x.getParent().setLeft(y);
		   }
		   else {
			   x.getParent().setRight(y);
		   }
	   }
	   x.setParent(y);
	   y.setRight(x);
	   x.setLeft(yRight);
	   yRight.setParent(x);
	   if (x.getRight().getRank() == -1 && x.getLeft().getRank() == -1) {
	   x.setRank(0);}
	   if (isRoot) {
		   root = y;
	   }
   }
   public void leftRotate(IWAVLNode x) {
	   boolean isRoot = x==root;
	   IWAVLNode y = x.getRight();
	   IWAVLNode yLeft = y.getLeft();
	   y.setParent(x.getParent());
	   if (y.getParent() != null) {
		   if (y.getKey() > y.getParent().getKey()) {
			   y.getParent().setRight(y);
		   }
		   else {
			   y.getParent().setLeft(y);
		   }
	   }
	   x.setParent(y);
	   y.setLeft(x);
	   x.setRight(yLeft);
	   yLeft.setParent(x);
	   if (x.getRight().getRank() == -1 && x.getLeft().getRank() == -1) {
	   x.setRank(0);}
	   if (isRoot) {
		   root = y;
	   }
	      }
   
//   public int subTreeSize (IWAVLNode root) { //calculates for each node the subtree size including the node
//	   if (root.getRank() == -1) {
//		   return 0;
//	   }
//	   else {
//		   root.setSubTreeSize(1 + subTreeSize(root.getLeft()) + subTreeSize(root.getRight()));
//	   }
//	   return 0;
//   }
   public void findMin() { //finds the min after every insertion/deletion
	   if (empty()) {
		   this.min = null;
	   }
	   IWAVLNode node = root;
	   while (node.getLeft().getRank() != -1) {
		   node = node.getLeft();
	   }
	   this.min = node;
   }
   
   public void findMax() { //finds the max after every insertion/deletion
	   if (empty()) {
		   this.max = null;
	   }
	   IWAVLNode node = root;
	   while (node.getRight().getRank() != -1) {
		   node = node.getRight();
	   }
	   this.max = node;
   }
   
//   public void childKey (IWAVLNode child, IWAVLNode parent) {
//	   if (child.getKey() > parent.getKey()) {
//		   parent.setRight(child);
//	   }
//	   else {
//		   parent.setLeft(child);
//	   }
//   }
//   
   
   
   public void nodeSwitcher(IWAVLNode nodeA, IWAVLNode nodeB) { //switching 2 nodes inside the tree (not external)
	   IWAVLNode leftB = nodeB.getLeft(), rightB = nodeB.getRight(), parentB = nodeB.getParent();
	   int rankB = nodeB.getRank();
	   nodeB.setRank(nodeA.getRank());
	   nodeB.setParent(nodeA.getParent());
	   if (nodeB == nodeA.getLeft() || nodeB == nodeA.getRight()) { // if switching a node with his child
		   nodeA.setParent(nodeB);
		   if (nodeA.getLeft() == nodeB) { //left child
			   nodeB.setLeft(nodeA);
			   nodeB.setRight(nodeA.getRight());
		   }
		   else { // right child
			   nodeB.setRight(nodeA);
			   nodeB.setLeft(nodeA.getLeft());
		   }
	   }
	   else {  // nodeB and nodeA are not parent-child
		   nodeA.setParent(parentB);
		   nodeB.setRight(nodeA.getRight());
		   nodeB.setLeft(nodeA.getLeft());
	   }
	   if (nodeB == root || nodeB.getParent() == null) {
		   nodeB.setParent(null);
		   root = nodeB;
	   }
	   else {
		   if (nodeB.getParent().getLeft() == nodeA) {
			   nodeB.getParent().setLeft(nodeB);
		   }
		   else {
			   nodeB.getParent().setRight(nodeB);
		   }
//		   childKey(nodeB, nodeB.getParent());
	   }
	   if (parentB.getLeft() == nodeB) { //nodeB is a left child
		   parentB.setLeft(nodeA);
	   }
	   else {
		   parentB.setRight(nodeA); // nodeB is a right child
	   }
	   nodeA.setLeft(leftB);
	   nodeA.setRight(rightB);
	   nodeA.setRank(rankB);
	   nodeA.getLeft().setParent(nodeA);
	   nodeA.getRight().setParent(nodeA);
	   nodeB.getRight().setParent(nodeB);
	   nodeB.getLeft().setParent(nodeB);
//	   IWAVLNode leftA = nodeA.getLeft(), rightA = nodeA.getRight(), parentA = nodeA.getParent();
//	   int rankA = nodeA.getRank();
//	   nodeA.setLeft(nodeB.getLeft());
//	   nodeA.setRight(nodeB.getRight());
//	   nodeA.setRank(nodeB.getRank());
//	   if (nodeB.getParent().getKey() > nodeB.getKey()) { //checks A is left/right child
//		   nodeB.getParent().setLeft(nodeA);
//	   }
//	   else {
//		   nodeB.getParent().setRight(nodeA);
//	   }
//	   
//	   nodeA.setParent(nodeB.getParent());
//	   
//	   if (parentA == null) { // A is root
//		   root = nodeB;
//		   nodeB.setParent(null);
//	   }
//	   else if (parentA.getKey() > nodeA.getKey()) {
//		   parentA.setLeft(nodeB);
//	   }
//	   else {
//		   parentA.setRight(nodeB);
//	   }
//	   nodeB.setLeft(leftA);
//	   nodeB.setRight(rightA);
//	   nodeB.setParent(parentA);
//	   nodeB.setRank(rankA);
//	   if (nodeB.getRight().getKey()==nodeB.getKey()) { //if successor was a leaf, prevents loops 
//		  
//		   nodeB.setRight(new WAVLNode());
//	   }
   }
   
   public void deleteUnary (IWAVLNode deleted) { //deletes an Unaric node
	   if (deleted != root) {
		   if (deleted.getRight().getRank() != -1) { //if deleted has a right child
			   if (deleted == deleted.getParent().getRight()) {//if deleted is right node for his parent
				   deleted.getParent().setRight(deleted.getRight());
				   deleted.getRight().setParent(deleted.getParent());
			   }
			   else { //if deleted is a left child
				  deleted.getParent().setLeft(deleted.getRight());
				  deleted.getRight().setParent(deleted.getParent());
			   }
		   }
		   else { //deleted has a left child
			   if (deleted == deleted.getParent().getRight()) { //if deleted is a right child
				   deleted.getParent().setRight(deleted.getLeft());
				   deleted.getLeft().setParent(deleted.getParent()); 
			   }
			   else { //if deleted is a left child
				   deleted.getParent().setLeft(deleted.getLeft());
				   deleted.getLeft().setParent(deleted.getParent());
			   }
		   }
	   }
//	   if(deleted != root) {
//	   if (deleted.getRight().getRank() != -1) { // if deleted.left is an ext leaf
//			 if (deleted.getKey() > deleted.getParent().getKey()) { //if deleted is a right child
//				 deleted.getParent().setRight(deleted.getRight());
//				 deleted.getRight().setParent(deleted.getParent());
//		 }
//			 else {
//				deleted.getParent().setLeft(deleted.getRight()); 
//				deleted.getRight().setParent(deleted.getParent());
//			 }
//	 }
//		 else {
//			 if (deleted.getKey() > deleted.getParent().getKey()) { //if deleted is a left child
//				 deleted.getParent().setRight(deleted.getLeft());
//				 deleted.getLeft().setParent(deleted.getParent());
//		 }
//			 else {
//				deleted.getParent().setLeft(deleted.getLeft()); 
//				deleted.getLeft().setParent(deleted.getParent());
//			 }
//		 }
//   }
	   else {  //deleted is a root
		  if (deleted.getRight().getRank() != -1 ) { //if deleted has a right child
			  deleted.getRight().setParent(null);
			  root = deleted.getRight();
		  }
		  else {
			  deleted.getLeft().setParent(null);
			  root = deleted.getLeft();
		  }
//		  if (deleted.getRight().getRank() != -1) {
//			  root = deleted.getRight();
//			  root.setParent(null);
//		  }
//		  else {
//			  root = deleted.getLeft();
//			  root.setParent(null);
		  }
	   }
   
   public void deleteLeaf (IWAVLNode deleted) { //deletes a leaf
	   IWAVLNode ext = new WAVLNode();
		 if (deleted == deleted.getParent().getRight()) { //if deleted is a right child
			 if (isUnary(deleted.getParent())) {
				 deleted.getParent().setRank(0);}
			 deleted.getParent().setRight(ext);
			 ext.setParent(deleted.getParent());
			 deleted.setParent(null);
			 
		 }
		 else {
			 if (isUnary(deleted.getParent())) {
			 deleted.getParent().setRank(0);}
			 deleted.getParent().setLeft(ext);
			 ext.setParent(deleted.getParent());
			 deleted.setParent(null);
		 }
		 
   }
 
   public IWAVLNode deleteBinary (IWAVLNode deleted, IWAVLNode sucPre) { //reducts the problem of binary deletion
	   IWAVLNode deletedPar;
	   if (deleted == root) { //value for rebalance function
//			 deletedPar = sucPre;
			 root = sucPre;
		 }
	   if (sucPre == deleted.getRight() || sucPre == deleted.getLeft()) {
		   deletedPar = sucPre;
	   }
	   else {
	   deletedPar = sucPre.getParent();}//added
//		 else {
//			 deletedPar = deleted.getParent();
//		 }
		 if (deleted.getRank() > sucPre.getRank()) {
		 nodeSwitcher(deleted, sucPre);}
		 else {
			 nodeSwitcher(sucPre, deleted);
		 }
		 if (deleted.getRank() == 0) {
			 deleteLeaf(deleted);
		 }
		 else {
			 deleteUnary(deleted);
		 }
		 return deletedPar;
   }
   /**
    * public int delete(int k)
    *
    * deletes an item with key k from the binary tree, if it is there;
    * the tree must remain valid (keep its invariants).
    * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
    * returns -1 if an item with key k was not found in the tree.
    */
   public int delete(int k)
   {
	 IWAVLNode deleted = nodeSearch(k);
	 if (deleted == null) { //if deleted does'nt exist
		 return -1;
	 }
	 boolean isRoot = deleted == root;
	 IWAVLNode deletedPar = root;
	 if (deleted.getRank() == 0) { // if deleted is a leaf
		 if (isRoot) {
			 root = null;
			 return 0;
		 }
		 else {deletedPar = deleted.getParent();
		 deleteLeaf (deleted);
	 } }
	 else if (isUnary(deleted)) {
		 if (isRoot) {
			 if (deleted.getRight().getRank() != -1) {
				 deletedPar = deleted.getRight();
				 root = deletedPar;
			 }
			 else {
				 deletedPar = deleted.getLeft();
				 root = deletedPar;
			 }
		 }
		 else {
		 deleteUnary(deleted);
   }}
	 else { // if the node is binary
		 IWAVLNode successor = successor(deleted);
		 if (successor != null) { //used to be isUnary(successor) || successor.getRank() == 0
			 deletedPar = deleteBinary(deleted, successor);
		 }
		 else {
			 IWAVLNode predecessor = predecessor(deleted);
			 deletedPar = deleteBinary(deleted, predecessor);
		 }
////		 IWAVLNode predecessor = predecessor(deleted);
//		 if (successor(deleted).getRank() == 0) { 
//			 deletedPar = successor(deleted);
////			 if (isRoot) {
////				root = successor(deleted);
////			}
////			 else {
////			nodeSwitcher(deleted, deletedPar);
////			deleteLeaf(deleted);}
////		 }}
//		 else {
//			 deletedPar = predecessor(deleted);
//			 if (isRoot) {
//				 root = predecessor(deleted);	 
//			 }
//			 if (predecessor(deleted).getRank() == 0) {
//				 nodeSwitcher(deleted, predecessor(deleted));
//				 deleteLeaf(deleted);
//			 }
//			 else {
//				 nodeSwitcher(deleted, predecessor(deleted));
//				 deleteUnary(deleted);
//			 }
//		 }
//	 }
	 }
	 int actions = 0;
//	 	if (isRoot && root != null && deletedPar != null) {
//	 		actions = deleteRebalance (deletedPar);
//	 	}
	 	if (root == null) {
	 		min = null;
	 		max = null;
	 		treeSize = 0;
	 	}
	 	else {
			actions = deleteRebalance(deletedPar);
	 	}
//		this.subTreeSize(root);
	 	this.findMin();
		this.findMax();
		root.getSubtreeSizeFinal();
		treeSize = root.getSubtreeSize();
		return actions;
   }
   
//   public void updateAllSizeVals (IWAVLNode node) {
//	   while (node != root) {
//		   node.setSubTreeSize(node.getRight().getSubtreeSize() + node.getLeft().getSubtreeSize());
//		   node = node.getParent();
//	   }
//	   root.setSubTreeSize(root.getLeft().getSubtreeSize() + root.getRight().getSubtreeSize() + 1);
//   }
//   
//   public int updateTreeSizes (IWAVLNode node) {
//	   if (node.getRank() != -1) {
//		   node.setSubTreeSize(1 + updateTreeSizes(node.getLeft()) + updateTreeSizes(node.getRight()));
//		   return node.getSubtreeSize();
//	   }
//	   else {
//		   return 0;
//	   }
//	   
//   }
   
   public void postRotRankFix (IWAVLNode current, IWAVLNode parent) { //fixes ranks post rotations
		int curRank = current.getRank();
		current.setRank(parent.getRank());
		parent.setRank(curRank);
//		int rankDiffRight = current.getRank() - current.getRight().getRank(); 
//		int rankDiffLeft = current.getRank() - current.getLeft().getRank();
		if (current.getLeft().getRank() == -1 && current.getRight().getRank() == -1) {
			current.setRank(0);
		}
//		if (rankDiffRight >2 && rankDiffLeft >2) {
//			if (isUnary(current)) {
//			current.setRank(current.getRank()-1);
//		}
//			else if (current.getLeft().getRank() == -1 && current.getRight().getRank() == -1) {
//				current.setRank(0);
//			}}
   }
   
   public int deleteRebalance (IWAVLNode current) {
	   int actions = 0;
	   int rankDiffRight = current.getRank() - current.getRight().getRank(); 
		int rankDiffLeft = current.getRank() - current.getLeft().getRank();
		if (((rankDiffRight>0 && rankDiffRight<=2) & (rankDiffLeft>0 && rankDiffLeft<=2))) {
			return 0;
		}
		else {
		while (!((rankDiffRight>0 && rankDiffRight<=2) & (rankDiffLeft>0 && rankDiffLeft<=2))) {
			if ((rankDiffLeft == 3 && rankDiffRight==2) || (rankDiffRight == 3 && rankDiffLeft==2)) { //single Demote
				current.setRank(current.getRank()-1);
				actions++;
			}
			else { //3 cases: double Demote, rotate, double rotate
				boolean isRoot = current == this.root;
				if (rankDiffRight == 1 && current.getRight().getRank() != -1) { // if the problem is on right child
					int rankDiffRightRight = current.getRight().getRank() - current.getRight().getRight().getRank();
					int rankDiffRightLeft = current.getRight().getRank() - current.getRight().getLeft().getRank();
					if (rankDiffRightRight == 2 && rankDiffRightLeft ==2) { //double demote
						current.setRank(current.getRank()-1);
						current.getRight().setRank(current.getRight().getRank()-1);
						actions+=2;
					}
					else {
						if (rankDiffRightRight == 1) { //single rotation
							leftRotate(current);
							postRotRankFix(current, current.getParent());
							actions++;
						}
						else if (rankDiffRightLeft == 1) { //double rotation
							rightRotate(current.getRight());
							postRotRankFix(current.getRight().getRight(), current.getRight());
							leftRotate(current);
							postRotRankFix(current, current.getParent());
							actions+=2;
						}
					}
				}
				else if (rankDiffLeft == 1 && current.getLeft().getRank() != -1){ //if the porblem is on the left child
					int rankDiffLeftRight = current.getLeft().getRank() - current.getLeft().getRight().getRank();
					int rankDiffLeftLeft = current.getLeft().getRank() - current.getLeft().getLeft().getRank();
					if (rankDiffLeftLeft == 2 && rankDiffLeftRight == 2) { //double demote
						current.setRank(current.getRank()-1);
						current.getLeft().setRank(current.getLeft().getRank()-1);
						actions+=2;
					}
					else {
						if (rankDiffLeftLeft == 1) {
							rightRotate(current);
							postRotRankFix(current, current.getParent());
							actions++;
						}
						else if (rankDiffLeftLeft == 2) {
							leftRotate(current.getLeft());
							postRotRankFix(current.getLeft().getLeft(), current.getLeft());
							rightRotate(current);
							postRotRankFix(current, current.getParent());
							actions +=2;
						}
					}
				}
			}
			rankDiffRight = current.getRank() - current.getRight().getRank(); 
			rankDiffLeft = current.getRank() - current.getLeft().getRank();
			if (current == this.root) {
				break;
			}
			else if (current.getParent() != null) {
				current = current.getParent();
			}
			rankDiffRight = current.getRank() - current.getRight().getRank(); 
			rankDiffLeft = current.getRank() - current.getLeft().getRank();
		}
		return actions;
   }}
   
   public boolean isUnary (IWAVLNode node) { //checks wheather the node is unaric
	   if ((node.getRight().getRank() == -1 & node.getLeft().getRank() != -1) || (node.getLeft().getRank() == -1 & node.getRight().getRank() != -1)) {
		   return true;
	   }
	   return false;
   }
   
   public IWAVLNode successor(IWAVLNode node) { //returns null whenever succesor is called for max
	   if (node == max) {
		   return null;
	   }
	   if (node.getRight().getRank() != -1) {
		   IWAVLNode success = node.getRight();
		   while (success.getLeft().getRank() != -1) {
			   success = success.getLeft();
		   }
		   return success;
	   }
	   else {
		   while (node.getParent().getLeft() != node) {
			   if (node == root.getLeft()) {
				   return root;
			   }
			   else {
			   node = node.getParent();}
		   }
		   return node.getParent();
	   }
   }
   public IWAVLNode predecessor (IWAVLNode node) { //returns null whenever min's predecessor is called
	   if (node == min) {
		   return null;
	   }
	   if (node.getLeft().getRank() != -1) {
		   IWAVLNode predecessor = node.getLeft();
		   while (predecessor.getRight().getRank() != -1) {
			   predecessor = predecessor.getRight();
		   }
		   return predecessor;
	   }
	   else {
		   while (node.getParent().getRight() != node) {
			   if (node == root.getRight()) {
				   return root;
			   }
			   else {
			   node = node.getParent();}
		   }
		   return node.getParent();
	   }
	   
   }
   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min()
   {
	   if (min != null) {
		   return min.getValue();
	   }
	   return null;
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max()
   {
	   if (max != null) {
		   return max.getValue();
	   }
	   return null;
   }

  /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   
   
  public int[] keysToArray(){ //returns an array with the inordered keys using successor
        int[] arr = new int[size()];
        if (size() == 0) {
        	return arr;
        }
        else {
      	  IWAVLNode mini = min;
    	  for (int i = 1; i<=size(); i++) {
    		  arr[i-1] = mini.getKey();
    		  if (i<size()) {
    		  mini = successor(mini);}
    	  }
    	  return arr;
        }
  }
        
  

  /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
  public String[] infoToArray(){ //returns a String array with values of inordered nodes using successor
        String[] arr = new String[size()];
        if (size() == 0) {
        	return arr;
        }
        else {
        	IWAVLNode mini = min;
      	  for (int i = 1; i<=size(); i++) {
      		  arr[i-1] = mini.getValue();
      		  mini = successor(mini);
      	  }
      	  return arr;
          } }	
        

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    * precondition: none
    * postcondition: none
    */
   public int size() {
	   if (!this.empty()) {
		   return this.root.getSubtreeSize();
	   }
	   return 0;
   }
   
     /**
    * public int getRoot()
    *
    * Returns the root WAVL node, or null if the tree is empty
    *
    * precondition: none
    * postcondition: none
    */
   public IWAVLNode getRoot()
   {
	   if (this.root != null) {
		   return this.root;
	   }
	   return null;
   }
     /**
    * public int select(int i)
    *
    * Returns the value of the i'th smallest key (return -1 if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
    *
	* precondition: size() >= i > 0
    * postcondition: none
    */   
   public String select(int i) { //iterates using successors and predecessors
	  if (empty()) {
		  return "-1";
	  }
	  int leftSize = root.getLeft().getSubtreeSize();
	  int rightSize = root.getRight().getSubtreeSize();
	  if (i == leftSize + 1) {
		  return root.getValue();
	  }
	  if (i< leftSize) {
		  IWAVLNode node = min;
		  for (int j=1; j<i; j++) {
			  node = successor(node);
		  }
		  return node.getValue();
	  }
	  else {
		IWAVLNode node = max;  
		for (int j = size(); j>i; j--) {
			node = predecessor(node);
		}
		return node.getValue();
	  }
   }
	/**
	   * public interface IWAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !	   */	
   		public interface IWAVLNode{
		public int getRank();
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
		public IWAVLNode getRight(); //returns right child (if there is no right child return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
		public IWAVLNode getParent();
		public void setLeft(IWAVLNode left);
		public void setRight(IWAVLNode right);
		public void setParent (IWAVLNode parent);
		public void setRank (int k);
		public void setSubTreeSize (int k);
		public int getSubtreeSizeFinal();
	}

   /**
   * public class WAVLNode
   *
   * If you wish to implement classes other than WAVLTree
   * (for example WAVLNode), do it in this file, not in 
   * another file.
   * This class can and must be modified.
   * (It must implement IWAVLNode)
   */
  public class WAVLNode implements IWAVLNode{
	  	private String info;
	  	private int key, rank, size;
	  	private WAVLNode left, right, parent;
	  	
	  	public WAVLNode (String info, int key) {
	  		this.key = key;
	  		this.info = info;
	  		this.left = new WAVLNode();
	  		this.right = new WAVLNode();
	  		this.rank = 0;
	  		this.size = 0;
	  		
	  	}
	  	public WAVLNode (int key) {
	  		this("", key);
	  	}
	  	
	  	public WAVLNode() {
	  		this.rank = -1;
	  	}
	  	
		public int getKey()
		{
			if (this.rank == -1) {
				return -1;// to be replaced by student code
			}
			else {
				return this.key;
			}
		}
		public int getRank() {
			return this.rank;
		}
		public String getValue()
		{
			if (this.rank != -1) {
				return this.info;// to be replaced by student code
			}
			else {
				return null;		
				}
		}
		public WAVLNode getLeft()
		{
				return this.left;// to be replaced by student code
		}
		public WAVLNode getRight()
		{
				return this.right;// to be replaced by student code
		}
		// Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public boolean isRealNode()
		{
			if (this.rank == -1) {
				return false;
			}
			return true; // to be replaced by student code
		}
		
		public int getSubtreeSize() {
			if (this.rank != -1) {
				return this.size;
			}
			return 0;
		}

		public int getSubtreeSizeFinal()
		{ //updates all the size fields on a given subtree
			if (this.rank == -1) {
				return 0;
			}
			this.size = 1 + this.left.getSubtreeSizeFinal() + this.right.getSubtreeSizeFinal();
			return this.size; // to be replaced by student code
		}
		public void setLeft(IWAVLNode left)
		{
			this.left = (WAVLNode) left; 
		}
		public void setRight(IWAVLNode right)
		{
			this.right = (WAVLNode) right; 

		}
		public WAVLNode getParent() {
			if (this.parent != null) {
				return this.parent;
			}
			return null;
		}
		public void setParent(IWAVLNode parent) {
			  this.parent = (WAVLNode) parent;
  }
		public void setRank(int k) {
			this.rank = k;
			
		}
		public void setSubTreeSize (int k) {
			this.size = k;
		}
  }


}
  

