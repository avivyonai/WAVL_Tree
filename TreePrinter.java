package reference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class TreePrinter {

	    public static <T extends Comparable<?>> void printNode(WAVLTree.IWAVLNode root) {
	        int maxLevel = TreePrinter.maxLevel(root);

	        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
	    }

	    private static void printNodeInternal(List<WAVLTree.IWAVLNode> nodes, int level, int maxLevel) {
	    	if (nodes.isEmpty() || TreePrinter.isAllElementsNull(nodes))
	            return;

	        int floor = maxLevel - level;
	        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
	        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
	        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

	        TreePrinter.printWhitespaces(firstSpaces);

	        List<WAVLTree.IWAVLNode> newNodes = new ArrayList<WAVLTree.IWAVLNode>();
	        for (WAVLTree.IWAVLNode node : nodes) {
	            if (node!=null) {
	                System.out.print(node.getKey());
	                newNodes.add(node.getLeft());
	                newNodes.add(node.getRight());
	            } else {
	            	newNodes.add(null);
	                newNodes.add(null);
	                System.out.print(" ");
	            }

	            TreePrinter.printWhitespaces(betweenSpaces);
	        }
	        System.out.println("");

	        for (int i = 1; i <= endgeLines; i++) {
	            for (int j = 0; j < nodes.size(); j++) {
	            	TreePrinter.printWhitespaces(firstSpaces - i);
	                if (nodes.get(j) == null) {
	                	TreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
	                    continue;
	                }

	                if (nodes.get(j).getLeft()!=null)
	                    System.out.print("/");
	                else
	                	TreePrinter.printWhitespaces(1);

	                TreePrinter.printWhitespaces(i + i - 1);

	                if (nodes.get(j).getRight()!=null)
	                    System.out.print("\\");
	                else
	                	TreePrinter.printWhitespaces(1);

	                TreePrinter.printWhitespaces(endgeLines + endgeLines - i);
	            }

	            System.out.println("");
	        }
	        if(!newNodes.isEmpty()) {
	        printNodeInternal(newNodes, level + 1, maxLevel);
	        }
	    }

	    private static boolean isAllElementsNull(List<WAVLTree.IWAVLNode> nodes) {
	    	for (Object object : nodes) {
	            if (object != null)
	                return false;
	        }

	        return true;
		}

		private static void printWhitespaces(int count) {
	        for (int i = 0; i < count; i++)
	            System.out.print(" ");
	    }

	    private static int maxLevel(WAVLTree.IWAVLNode node) {
	        if (node==null)
	            return 0;

	        return Math.max(TreePrinter.maxLevel(node.getLeft()), TreePrinter.maxLevel(node.getRight())) + 1;
	    }


	}

