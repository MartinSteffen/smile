package gui;

import java.awt.Point;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;
import csm.CoreStateMachine;
import csm.statetree.ChoiceState;
import csm.statetree.CompositeState;
import csm.statetree.EntryState;
import csm.statetree.ExitState;
import csm.statetree.FinalState;
import csm.statetree.Region;
import csm.statetree.Transition;
import csm.statetree.Visitor;

/**
 * Die Klasse TreeBuilder erzeugt aus einer CSM den Baum für die ElementList.
 * Die CSM ist dabei der root, und die Komponenten der CSM werden nach ihrem Level
 * SubKnoten und enthalten ihre Eigenschaften als Unterknoten. Für die Erstellung
 * des Baums wird das Visitor-Pattern verwendet. Transitionen werden hierbei 
 * am Ende des Baums in einen speziellen Knoten eingehängt.
 * 
 * @author Oliver
 *
 */
public class TreeBuilder extends Visitor {
	
	DefaultMutableTreeNode root, transRoot, parent;
	
	/**
	 * Der Konstrukter erzeugt den Root und startet das Visitor-Pattern
	 * 
	 * @param CoreStateMachine - die Maschine aus der die Elementliste erzeugt werden soll.
	 */
	public TreeBuilder (CoreStateMachine csm) {
		root = new DefaultMutableTreeNode("CSM: " + csm.region.getName());
		transRoot = new DefaultMutableTreeNode("Transition");		
		parent = root;
		this.visitRegion(csm.region);
		root.add(transRoot);
	}
	
	@Override
	final protected void visitTransition(Transition transition) {
		DefaultMutableTreeNode transNode = new DefaultMutableTreeNode(transition.getName());
		Point p = transition.getAbsolutePosition();
		transNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x+ "/" + p.y + ")"));
		transNode.add(new DefaultMutableTreeNode("Action: " + transition.getAction().prettyprint()));
		transNode.add(new DefaultMutableTreeNode("Event: " + transition.getEventName()));
		transNode.add(new DefaultMutableTreeNode("Guard: " + transition.getGuard().prettyprint()));
		transNode.add(new DefaultMutableTreeNode("Source: " + transition.getSource().getName()));
		transNode.add(new DefaultMutableTreeNode("Target: " + transition.getTarget().getName()));
		transRoot.add(transNode);
	}

	@Override
	final protected void visitRegion(Region region) {
		DefaultMutableTreeNode regionNode;
		if(parent.equals(root))
			regionNode = new DefaultMutableTreeNode("Outermost-Region: " + region.getName());
		else
			regionNode = new DefaultMutableTreeNode("Region: " + region.getName());
		parent.add(regionNode);
		DefaultMutableTreeNode temp = parent;
		parent = regionNode;
		visitChildren(region);
		parent = temp;
	}
	
	@Override
	final protected void visitCompositeState(CompositeState cs) {
		DefaultMutableTreeNode compositeNode = new DefaultMutableTreeNode("Composite-State: " + cs.getName());
		DefaultMutableTreeNode events = new DefaultMutableTreeNode("Events");
		Point p = cs.getAbsolutePosition();
		compositeNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		compositeNode.add(new DefaultMutableTreeNode("Action: " + cs.getDoAction().prettyprint()));
		LinkedList<String> eventList = cs.getDeferredEventNames();
		for(String string : eventList) 
			events.add(new DefaultMutableTreeNode(string));
		compositeNode.add(events);
		parent.add(compositeNode);
		DefaultMutableTreeNode temp = parent;
		parent = compositeNode;
		visitChildren(cs);
		parent = temp;
	}
	
	@Override
	final protected void visitChoiceState(ChoiceState cs) {
		DefaultMutableTreeNode choiceNode = new DefaultMutableTreeNode("Choice-State: " + cs.getName());
		Point p = cs.getAbsolutePosition();
		choiceNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		parent.add(choiceNode);
		DefaultMutableTreeNode temp = parent;
		parent = choiceNode;
		visitChildren(cs);
		parent = temp;
	}
	
	@Override
	final protected void visitExitState(ExitState es) {
		DefaultMutableTreeNode exitNode = new DefaultMutableTreeNode("Exit-State: " + es.getName());
		Point p = es.getAbsolutePosition();
		exitNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		parent.add(exitNode);
		DefaultMutableTreeNode temp = parent;
		parent = exitNode;
		visitChildren(es);
		parent = temp;
	}
	

	@Override
	final protected void visitEntryState(EntryState es) {
		DefaultMutableTreeNode entryNode = new DefaultMutableTreeNode("Entry-State: " + es.getName());
		Point p = es.getAbsolutePosition();
		entryNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		parent.add(entryNode);
		DefaultMutableTreeNode temp = parent;
		parent = entryNode;
		visitChildren(es);
		parent = temp;
	}
	
	@Override
	final protected void visitFinalState(FinalState fs) {
		DefaultMutableTreeNode finalNode = new DefaultMutableTreeNode("Final-State: " + fs.getName());
		Point p = fs.getAbsolutePosition();
		finalNode.add(new DefaultMutableTreeNode("Position: " + "(" + p.x + "/" + p.y + ")"));
		parent.add(finalNode);
		DefaultMutableTreeNode temp = parent;
		parent = finalNode;
		visitChildren(fs);
		parent = temp;
	}
	
	// übergibt den Root.
	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	
	// Die Methode findet einen bestimmten Knoten aufgrund seines Namens
	public DefaultMutableTreeNode findNode(String name) {
		Enumeration nodes = root.depthFirstEnumeration();
		while(nodes.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)nodes.nextElement();
			if(name.equals(node.toString().substring(node.toString().indexOf(' ') + 1)))
				return node;
		}
		return null;
	}
}
