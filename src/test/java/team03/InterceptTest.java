/**	
Copyright (c) 2018 David Phung

Building on work by Mathew A. Nelson and Robocode contributors.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package team03;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class InterceptTest {
	private Intercept intercept;
	
	@Before
	public void setUp()	{
		intercept = new Intercept();
		intercept.calculate(0, 0, 0, 0, 0, 0, 0, 0);
	}
	
	@After
	public void tearDown()	{
		intercept = null;
	}
	
	@Test 
	public void testIntercept() {
		 Point2D.Double expected = new Point2D.Double(0.0,0.0);
		 assertEquals(expected, intercept.getImpactPoint());
	}
	}

	
	
	
