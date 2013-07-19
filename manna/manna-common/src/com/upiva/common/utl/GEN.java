/**
 * By: IanF on 25/03/13 18:33
 */
package com.upiva.common.utl;

import com.badlogic.gdx.graphics.Color;

public class GEN {

	///////////////////////////////////////////////////////////////////////////
	// Constant data

	///////////////////////////////////////////////////////////////////////////
	// Construction

	// STATIC ONLY CLASSES HAVE NO CONSTRUCTORS

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	/**
	 * Utility for returning the full character sequence from a string
	 *
	 * @param token
	 * @return character sequence
	 */
	public static CharSequence charSequence( final String token ) {
		return token.subSequence( 0, token.length() );
	}

	public static Color colorFromHexString( final String color ) {
		if( !color.startsWith( "0x" ) ) {
			throw new IllegalArgumentException( "Not a valid hexadecimal color newValue starting with 0x......" );
		}
		final float fr = 1 / ( 255f / Integer.valueOf( color.substring( 2, 4 ), 16 ) );
		final float fg = 1 / ( 255f / Integer.valueOf( color.substring( 4, 6 ), 16 ) );
		final float fb = 1 / ( 255f / Integer.valueOf( color.substring( 6, 8 ), 16 ) );
		final float fa;
		if( color.length() > 8 ) {
			fa = 1 / ( 255f / Integer.valueOf( color.substring( 8, 10 ), 16 ) );
		} else {
			fa = 1f;
		}
		return new Color( fr, fg, fb, fa );
	}

	public static float colorFromHexStringRed( final String rgba ) {
		return GEN.colorFromHexString( rgba ).r;
	}

	public static float colorFromHexStringGreen( final String rgba ) {
		return GEN.colorFromHexString( rgba ).g;
	}

	public static float colorFromHexStringBlue( final String rgba ) {
		return GEN.colorFromHexString( rgba ).b;
	}

	public static float colorFromHexStringAlpha( final String rgba ) {
		return GEN.colorFromHexString( rgba ).a;
	}

	public static String calcHash( final String password ) {
		return null; // todo
	}

}
