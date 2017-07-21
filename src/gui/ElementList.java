package gui;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;

import java.util.Enumeration;
import java.util.LinkedList;

import csm.CoreStateMachine;
import csm.statetree.*;

/**
 * <p>Title: ElementList</p>
 *
 * <p>Description: Die ElementListe stellt den Tree für den Baum der CSM 
 * zur Verfügung. </p>
 *
 * @author Oliver
 * @version 1.0
 */
public class ElementList extends JPanel implements TreeSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8704453780737794744L;
	
	private JTree tree;
	private TreeBuilder treeBuilder;
	private JScrollPane treeView;
	
    /**
	 *  Konstruktor fuer die Elementliste
	 *  
	 * @param zustaendiger Controller
	 */
	public ElementList() {
		tree = new JTree(new DefaultMutableTreeNode("CSM: empty"));
		// Layout einstellen
		this.setLayout( new GridLayout(1, 1) );
                
        // ScrollPane erzeugen und Baum hinzufuegen
        treeView = new JScrollPane(tree);

        Dimension minimumSize = new Dimension(200, 400);
        treeView.setMinimumSize(minimumSize);
        treeView.setPreferredSize(new Dimension(200, 400)); 
        
        add(treeView);
	}
	
	// Nach Änderungen der CSM, soll diese Methode aufgerufen werden.
	// es wird ein neuer Baum erzeugt und der Root ansstelle des Alten in den 
	// Baum eingehängt.
	public void refreshTree(CoreStateMachine csm) {
		treeBuilder = new TreeBuilder(csm);
		LinkedList<Integer> integer = new LinkedList<Integer>();
		for(int i = 0; i < tree.getRowCount(); i++) {
			if (tree.isExpanded(i))
				integer.add(i);
		}
		DefaultMutableTreeNode newRoot = treeBuilder.getRoot();
		((DefaultTreeModel)tree.getModel()).setRoot(newRoot);
		tree.treeDidChange();
		for(int n: integer) {
			tree.expandRow(n);
		}		
	}
	
	public void valueChanged(TreeSelectionEvent e) {
		
	}
	
	// Die folgenden Methoden werden nicht verwendet, aber aus Gründen der
	// Erweiterbarkeit, belassen wir sie hier.
	public void addNode(Transition transition) {
		DefaultMutableTreeNode transNode = new DefaultMutableTreeNode(transition.getName());
		Point p = transition.getAbsolutePosition();
		transNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x+ "/" + p.y + ")"));
		transNode.add(new DefaultMutableTreeNode("Action: " + transition.getAction().prettyprint()));
		transNode.add(new DefaultMutableTreeNode("Event: " + transition.getEventName()));
		transNode.add(new DefaultMutableTreeNode("Guard: " + transition.getGuard()));
		transNode.add(new DefaultMutableTreeNode("Source: " + transition.getSource().getName()));
		transNode.add(new DefaultMutableTreeNode("Target: " + transition.getTarget().getName()));
		treeBuilder.transRoot.add(transNode);
		tree.treeDidChange();
	}

	public void addNode(Region region) {
		String parentName = region.getParent().getName();
		DefaultMutableTreeNode temp = treeBuilder.findNode(parentName);
		DefaultMutableTreeNode regionNode = new DefaultMutableTreeNode("Region: " + region.getName());
		temp.add(regionNode);
		tree.treeDidChange();
	}
	
	public void addNode(CompositeState cs) {
		String parentName = cs.getParent().getName();
		DefaultMutableTreeNode temp = treeBuilder.findNode(parentName);
		DefaultMutableTreeNode compositeNode = new DefaultMutableTreeNode("Composite-State: " + cs.getName());
		Point p = cs.getAbsolutePosition();
		compositeNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		// TODO read EventList & Actions
		temp.add(compositeNode);
		tree.treeDidChange();
	}
	
	public void addNode(ChoiceState cs) {
		String parentName = cs.getParent().getName();
		DefaultMutableTreeNode temp = treeBuilder.findNode(parentName);
		DefaultMutableTreeNode choiceNode = new DefaultMutableTreeNode("Choice-State: " + cs.getName());
		Point p = cs.getAbsolutePosition();
		choiceNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		temp.add(choiceNode);
		tree.treeDidChange();
	}
	
	public void addNode(ExitState es) {
		String parentName = es.getParent().getName();
		DefaultMutableTreeNode temp = treeBuilder.findNode(parentName);
		DefaultMutableTreeNode exitNode = new DefaultMutableTreeNode("Exit-State: " + es.getName());
		Point p = es.getAbsolutePosition();
		exitNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		temp.add(exitNode);
		tree.treeDidChange();
	}
	
	public void addNode(EntryState es) {
		String parentName = es.getParent().getName();
		DefaultMutableTreeNode temp = treeBuilder.findNode(parentName);
		DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode("Entry-State: " + es.getName());
		Point p = es.getAbsolutePosition();
		entryNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		temp.add(entryNode);
		tree.treeDidChange();
	}
	
	public void addNode(FinalState fs) {
		String parentName = fs.getParent().getName();
		DefaultMutableTreeNode temp = treeBuilder.findNode(parentName);
		DefaultMutableTreeNode finalNode = new DefaultMutableTreeNode("Final-State: " + fs.getName());
		Point p = fs.getAbsolutePosition();
		finalNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		temp.add(finalNode);
		tree.treeDidChange();
	}
	
	public void removeNode(CSMComponent comp) {
		DefaultMutableTreeNode node = treeBuilder.findNode(comp.getName());
		node.removeAllChildren();
		node.removeFromParent();
		tree.treeDidChange();
	}
	
	public void changeAction(String name, Transition after) {
		DefaultMutableTreeNode node = treeBuilder.findNode(name);
		node.removeAllChildren();
		node.removeFromParent();
		addNode(after);
		tree.treeDidChange();
	}
	
	public void changeAction(String name, ExitState after) {
		DefaultMutableTreeNode node = treeBuilder.findNode(name);
		node.removeAllChildren();
		node.removeFromParent();
		addNode(after);
		tree.treeDidChange();
	}
	
	public void changeAction(String name, EntryState after) {
		DefaultMutableTreeNode node = treeBuilder.findNode(name);
		node.removeAllChildren();
		node.removeFromParent();
		addNode(after);
		tree.treeDidChange();
	}
	
	public void changeAction(String name, ChoiceState after) {
		DefaultMutableTreeNode node = treeBuilder.findNode(name);
		node.removeAllChildren();
		node.removeFromParent();
		addNode(after);
		tree.treeDidChange();
	}
	
	public void changeAction(String name, Region after) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Region: " + after.getName());
		DefaultMutableTreeNode oldNode = treeBuilder.findNode(name);
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)oldNode.getParent();
		Enumeration nodes = oldNode.children();
		while(nodes.hasMoreElements()) {
			DefaultMutableTreeNode temp = (DefaultMutableTreeNode)nodes.nextElement();
			if(temp.getChildCount() > 0) 
				newNode.add(temp);
		}		
		parent.add(newNode);
		oldNode.removeFromParent();
		tree.treeDidChange();
	}
	
	public void changeAction(String name, CompositeState after) {
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Composite-State: " + after.getName());
		DefaultMutableTreeNode oldNode = treeBuilder.findNode(name);
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode)oldNode.getParent();
		Enumeration nodes = oldNode.children();
		while(nodes.hasMoreElements()) {
			DefaultMutableTreeNode temp = (DefaultMutableTreeNode)nodes.nextElement();
			if(temp.getChildCount() > 0) 
				newNode.add(temp);
		}		
		parent.add(newNode);
		oldNode.removeFromParent();
		tree.treeDidChange();
	}
}