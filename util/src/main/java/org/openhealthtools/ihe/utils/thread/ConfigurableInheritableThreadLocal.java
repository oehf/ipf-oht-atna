/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.utils.thread;


/**
 * An extension of the {@link InheritableThreadLocal} class which permits switching between use of 
 * thread local variable behaviour, and traditional static value.  This allows applications which do not
 * need thread local variables, to avoid the performance penalty which comes from using {@link InheritableThreadLocal}.
 * 
 * @author Glenn Deen  <a href="mailto:glenn@almaden.ibm.com">glenn@almaden.ibm.com</a>
 *
 * @since OHF 1.0.0
 */
public class ConfigurableInheritableThreadLocal<T> extends InheritableThreadLocal<T>
{
	protected static boolean threadSupportEnabled = false;
	
	protected static Object nonThreadedValue = null;
	
	protected static boolean firstCall = true;
	
	/**
	 * Set true to enable the class to use InheritableThreadLocal variables. This can be done at any time,
	 * as the previous static value used in single thread mode, will be the initial value used by threads until
	 * they set it to a new value. 
	 * @param enable true to enable, false to use a single static variable for the value
	 */
	public static void enableThreading(boolean enable)
	{
		threadSupportEnabled = enable;
	}
	
	/**
	 * Tests if thread local variables are in use or not.
	 * @return
	 */
	public static boolean isThreadingEnabled()
	{
		return threadSupportEnabled;
	}

	
	@SuppressWarnings("unchecked")
	final public T get()
	{
        /*if (mLogger.isDebugEnabled()) {
            mLogger.debug("in a threadSupportEnabled = " + threadSupportEnabled + 
                    " the super.get() is " + super.get());
        }*/        
		if (threadSupportEnabled){
            return super.get();
        }
		// the first time this is called, initialize the value. this is done using the
		// super.get method, so that if the behavior is later swithed to threaded, the value
		// will be the same.
		if (firstCall) 
		{
			firstCall = false;
			nonThreadedValue =  super.get(); 
		}
		return (T)nonThreadedValue;
	}

	final public void remove()
	{	
		super.remove();
		firstCall = true;
		nonThreadedValue = null;
	}

	final public void set(T value)
	{
		super.set(value);
		if (! threadSupportEnabled) nonThreadedValue = value;
	}
	
	
	
}
