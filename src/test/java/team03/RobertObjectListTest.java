package team03;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RobertObjectListTest {

	private RobertObjectList list;
	private RobertObject robert1;
	private RobertObject robert2;
	private RobertObject robert3;
	
	@Before
	public void setUp()	{
		list = new RobertObjectList();
	}
	
	@After
	public void tearDown()	{
		list = null;
	}
	
	@Test
	public void testAdd()	{
		robert1 = new RobertObject("robban1");
		robert2 = new RobertObject("robban2");
		robert3 = new RobertObject("robban3");
		
		list.add(robert1);
		list.add(robert2);
		list.add(robert3);
		
		assertEquals(3, list.size());
	}
	
	@Test
	public void testRemove()	{
		robert1 = new RobertObject("robban1");
		robert2 = new RobertObject("robban2");
		robert3 = new RobertObject("robban3");
		
		list.add(robert1);
		list.add(robert2);
		list.add(robert3);
		
		list.remove("robban2");
		
		assertEquals(2, list.size());
	}
	
	@Test
	public void testContains()	{
		robert1 = new RobertObject("robban1");
		
		list.add(robert1);
		
		assertEquals(true, list.contains("robban1"));
		assertEquals(false, list.contains("robban2"));
	}
	
	@Test
	public void testGetRank()	{
		robert1 = new RobertObject("robban1");
		robert2 = new RobertObject("robban2");
		list.add(robert1);
		list.add(robert2);
		assertEquals(1, list.getRank("robban1"));
		assertEquals(2, list.getRank("robban2"));
	}
	
	@Test
	public void testSortList()	{
		robert1 = new RobertObject("robban1");
		robert2 = new RobertObject("robban2");
		list.add(robert2);
		list.add(robert1);
		list.sortList();
		assertEquals(1, list.getRank("robban1"));
		assertEquals(2, list.getRank("robban2"));
	}
	
}
