package fr.labri.gumtree.test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static fr.labri.gumtree.test.Constants.*;
import fr.labri.gumtree.io.TreeIoUtils;
import fr.labri.gumtree.tree.Tree;
import fr.labri.gumtree.tree.TreeUtils;

public class TestTreeUtils {
	Tree root, src, dst, big;

	@Before // FIXME Could it be before class ?
	public void init() {
		src = root = TreeIoUtils.fromXml(getClass().getResourceAsStream(DUMMY_SRC));
		dst = TreeIoUtils.fromXml(getClass().getResourceAsStream(DUMMY_DST));
		big = TreeIoUtils.fromXml(getClass().getResourceAsStream(DUMMY_BIG));
	}
	
	@Test
	public void testPostOrderNumbering() {
		TreeUtils.postOrderNumbering(root);
		assertEquals(root.getId(), 4);
		assertEquals(root.getChildren().get(0).getId(), 2);
		assertEquals(root.getChildren().get(0).getChildren().get(0).getId(), 0);
		assertEquals(root.getChildren().get(0).getChildren().get(1).getId(), 1);
		assertEquals(root.getChildren().get(1).getId(), 3);
	}
	
	@Test
	public void testDepth() {
		TreeUtils.computeDepth(root);
		assertEquals(root.getDepth(), 0);
		assertEquals(root.getChildren().get(0).getDepth(), 1);
		assertEquals(root.getChildren().get(0).getChildren().get(0).getDepth(), 2);
		assertEquals(root.getChildren().get(0).getChildren().get(1).getDepth(), 2);
		assertEquals(root.getChildren().get(1).getDepth(), 1);
	}
	
	@Test
	public void testHeight() {
		assertEquals(2, root.getHeight()); // depth of a 
		assertEquals(1, root.getChildren().get(0).getHeight()); // depth of b
		assertEquals(0, root.getChildren().get(0).getChildren().get(0).getHeight()); // depth of c
		assertEquals(0, root.getChildren().get(0).getChildren().get(1).getHeight()); // depth of d
		assertEquals(0, root.getChildren().get(1).getHeight()); // depth of e
	}
	
	@Test
	public void testPreOrderNumbering() {
		TreeUtils.preOrderNumbering(root);
		assertEquals(0, root.getId()); // id of a
		assertEquals(1, root.getChildren().get(0).getId()); // id of b
		assertEquals(2, root.getChildren().get(0).getChildren().get(0).getId()); // id of c
		assertEquals(3, root.getChildren().get(0).getChildren().get(1).getId()); // id of d
		assertEquals(4, root.getChildren().get(1).getId()); // id of e
	}
	
	@Test
	public void testBreadthFirstNumbering() {
		System.out.println(root.toCompleteTreeString());
		TreeUtils.breadthFirstNumbering(root);
		System.out.println(root.toCompleteTreeString());
		assertEquals(0, root.getId());
		assertEquals(1, root.getChildren().get(0).getId());
		assertEquals(2, root.getChildren().get(1).getId());
		assertEquals(3, root.getChildren().get(0).getChildren().get(0).getId());
		assertEquals(4, root.getChildren().get(0).getChildren().get(1).getId());
	}
	
	@Test
	public void testDigest() {
		Tree croot = root.deepCopy();
		TreeUtils.computeDigest(root);
		TreeUtils.computeDigest(croot);
		assertTrue(root.getDigest() == croot.getDigest());
		croot.getChildren().get(0).getChildren().get(0).setLabel("x");
		TreeUtils.computeDigest(croot);
		assertFalse(root.getDigest() == croot.getDigest());
		assertEquals("[(a@@0[(b@@1[(c@@3)][(d@@3)])][(e@@2)])]", root.toDigestTreeString());
	}
	
	@Test
	public void testRemoveCompletelyMappedDescendants() {
		dst.getChildren().get(0).setMatched(true);
		dst.getChildren().get(0).getChildren().get(0).getChildren().get(0).setMatched(true);
		dst.getChildren().get(1).setMatched(true);
		dst.getChildren().get(1).getChildren().get(0).setMatched(true);
		dst = TreeUtils.removeCompletelyMapped(dst);
		TreeUtils.computeSize(dst);
		assertTrue(dst.getSize() == 5);
	}
	
	@Test
	public void testPostOrder() {
		List<Tree> lst = TreeUtils.postOrder(src);
		Iterator<Tree> it = TreeUtils.postOrderIterator(src);
		compareListIterator(lst, it);
	}
	
	@Test
	public void testPostOrder2() {
		List<Tree> lst = TreeUtils.postOrder(dst);
		Iterator<Tree> it = TreeUtils.postOrderIterator(dst);
		compareListIterator(lst, it);
	}
	
	@Test
	public void testPostOrder3() {
		Tree big = TreeIoUtils.fromXml(getClass().getResourceAsStream(DUMMY_BIG));

		List<Tree> lst = TreeUtils.postOrder(big);
		Iterator<Tree> it = TreeUtils.postOrderIterator(big);
		compareListIterator(lst, it);
	}
	
	@Test
	public void testBFS() {
		List<Tree> lst = TreeUtils.breadthFirst(src);
		Iterator<Tree> it = TreeUtils.breadthFirstIterator(src);
		compareListIterator(lst, it);
	}
	
	
	@Test
	public void testBFSList() {
		compareListIterator(TreeUtils.breadthFirstIterator(src), "a", "b", "e", "c", "d");
		compareListIterator(TreeUtils.breadthFirstIterator(dst), "a", "f", "i", "b", "j", "c", "d", "h");
		compareListIterator(TreeUtils.breadthFirstIterator(big), "a", "b", "e", "f", "c", "d", "g", "l", "h", "m", "i", "j", "k");
	}
	
	void compareListIterator(List<Tree> lst, Iterator<Tree> it) {
		for (Tree i: lst) {
			assertEquals(i, it.next());
		}
		assertFalse(it.hasNext());
	}
	
	void compareListIterator(Iterator<Tree> it, String... expected) {
		for(String e: expected) {
			Tree n = it.next();
			assertEquals(e, n.getLabel());
		}
		assertFalse("Iterator has next", it.hasNext());
	}
	
	@Test
	public void testBFS2() {
		List<Tree> lst = TreeUtils.breadthFirst(dst);
		Iterator<Tree> it = TreeUtils.breadthFirstIterator(dst);
		compareListIterator(lst, it);
	}
	
	@Test
	public void testBFS3() {

		List<Tree> lst = TreeUtils.breadthFirst(big);
		Iterator<Tree> it = TreeUtils.breadthFirstIterator(big);
		compareListIterator(lst, it);
	}
}
