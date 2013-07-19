import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Authored By: IanF on 23/06/13 08:54
 * <p/>
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 * <p/>
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain
 * the property of the author. All rights reserved globally.
 * <p/>
 * Revisions:-
 * 23/06/13 08:54: Created, IanF, ...
 */

public class ServerTCPTest {

	///////////////////////////////////////////////////////////////////////////
	// Inner classes

	private class _Message {

		private final int m_index;
		private final StringBuffer m_buffer;

		private boolean m_completed = false;

		public _Message( final int index ) {
			m_index = index;
			m_buffer = new StringBuffer();
		}
	}

	private class _Process implements Callable<_Message> {

		private final int m_index;
		private final String m_input;

		private _Process( final int index, final String input ) {
			m_index = index;
			m_input = input;
		}

		@Override
		public _Message call() throws IOException {
			final _Message message = new _Message( m_index );

			Socket socket = null;
			PrintWriter writer = null;
			BufferedReader reader = null;
			try {
				socket = new Socket( "localhost", 4545 );
				socket.setSoLinger( true, 5000 );
				socket.setReuseAddress( true );
				socket.setKeepAlive( true );
				socket.setTcpNoDelay( true );
				//socket.setSoTimeout( 10000 );

				writer = new PrintWriter( new BufferedOutputStream( socket.getOutputStream() ) );
				writer.write( m_input );
				writer.flush();
				System.out.print( "+++ " + m_index + ": " + m_input );

				reader = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

				try {
					while( !reader.ready() ) {
						Thread.sleep( 1000 );
					}
					final String output = reader.readLine();
					message.m_buffer.append( output );
					System.out.println( ", " + output );
				} catch( IOException | InterruptedException e ) {
					System.err.println( ", " + e.toString() );
				}
			} finally {
				if( writer != null ) {
					writer.close();
				}
				if( reader != null ) {
					try {
						reader.close();
					} catch( IOException e ) {
						// n/a
					}
				}
    			if( socket != null ) {
				    try {
					    socket.close();
				    } catch( IOException e ) {
					    // n/a
				    }
			    }
			}

			message.m_completed = true;

			return message;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ServerTCPTest() {
		final int max = 1;
		final ExecutorService executor = Executors.newCachedThreadPool();
		final ConcurrentHashMap<Integer, Future<_Message>> collection = new ConcurrentHashMap<>( max );
		int idx = 0;
		for(; idx < max; idx++ ) {
			final String input = String.format( "{MannaGames-Fibonaci-192.168.22.195-1.0.0.1:{index:%d,value:%d}}", idx, ThreadLocalRandom.current().nextInt( 25 ) + 10 );
			System.out.println( ">>>" + input );
			final _Process process = new _Process( idx, input );
			Future<_Message> future = executor.submit( process );
			collection.put( idx, future );
		}
		while( !collection.isEmpty() ) {

			for( final Iterator<Future<_Message>> iter = collection.values().iterator(); iter.hasNext(); ) {
				final Future<_Message> item = iter.next();
				if( item.isDone() ) {
					try {
						System.out.println( ">>>" + item.get().m_buffer.toString() );
						iter.remove();
					} catch( InterruptedException | ExecutionException e ) {
						e.printStackTrace();
					}
				}

			}

			try {
				Thread.sleep( 1000 );
			} catch( InterruptedException e ) {
				break;
			}
		}
		executor.shutdownNow();
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Main

	public static void main( String[] args ) {
		new ServerTCPTest();
	}
}
