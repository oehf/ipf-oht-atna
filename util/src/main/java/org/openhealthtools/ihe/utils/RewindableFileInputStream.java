/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

public class RewindableFileInputStream extends InputStream 
{
	
	private FileInputStream backedStream;
	
	private final File backedFile;

	private int markCount = 0;
	
	private boolean closed = false;
	
	public RewindableFileInputStream(String name) throws FileNotFoundException
	{
		this(new File(name));
	}
	
	public RewindableFileInputStream(File file) throws FileNotFoundException
	{
		this(file, new FileInputStream(file));
	}

	private RewindableFileInputStream(File file, FileInputStream inputStream)
	{
		this.backedStream = inputStream;
		this.backedFile = file;
	}
	
	
	public void mark(int mark) 
	{
		this.markCount = mark;
	}

	public boolean markSupported()
	{
		return true;
	}
	
	public void reset() throws IOException 
	{
		if (closed) {
			throw new IOException("Stream already closed.  Cannot reset a closed stream.");
		}
		FileInputStream newStream = new FileInputStream(backedFile);
		backedStream.close();
		backedStream = newStream;
		skip(markCount);
	}
	
	
	public void close() throws IOException 
	{
		backedStream.close();
		closed = true;
	}
	
	public int read() throws IOException 
	{
		return backedStream.read();
	}
	
	public int available() throws IOException 
	{
		return backedStream.available();
	}

	public FileChannel getChannel() 
	{
		return backedStream.getChannel();
	}

	public final FileDescriptor getFD() throws IOException 
	{
		return backedStream.getFD();
	}

	public int read(byte[] arg0, int arg1, int arg2) throws IOException 
	{
		return backedStream.read(arg0, arg1, arg2);
	}

	public int read(byte[] arg0) throws IOException 
	{
		return backedStream.read(arg0);
	}

	public long skip(long arg0) throws IOException 
	{
		return backedStream.skip(arg0);
	}
}
