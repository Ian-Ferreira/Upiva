/**
 * com.ianf.gdx.general.utl.SYS
 *
 * Author: IanF - 2005/02/22 12:55:18
 *
 * See License Agreement for authenticity and copyright notice.
 *
 */

package com.upiva.common.utl;

/////////////////////////////////////////////////////////////////////////////
// class com.ianf.gdx.general.utl.SYS

public class SYS {

	/////////////////////////////////////////////////////////////////////////
	// Static Utilities

	public static String getJavaVersion() {
		return System.getProperty( "java.version" );
	}

	public static String getJavaVendor() {
		return System.getProperty( "java.vendor" );
	}

	public static String getJavaVendorUrl() {
		return System.getProperty( "java.vendor.url" );
	}

	public static String getJavaHome() {
		return System.getProperty( "java.home" );
	}

	public static String getJavaVmSpecificationVersion() {
		return System.getProperty( "java.vm.specification.version" );
	}

	public static String getJavaVmSpecificationVendor() {
		return System.getProperty( "java.vm.specification.vendor" );
	}

	public static String getJavaVmSpecificationName() {
		return System.getProperty( "java.vm.specification.name" );
	}

	public static String getJavaVmVersion() {
		return System.getProperty( "java.vm.version" );
	}

	public static String getJavaVmVendor() {
		return System.getProperty( "java.vm.vendor" );
	}

	public static String getJavaVmName() {
		return System.getProperty( "java.vm.name" );
	}

	public static String getJavaSpecificationVersion() {
		return System.getProperty( "java.specification.version" );
	}

	public static String getJavaSpecificationVendor() {
		return System.getProperty( "java.specification.vendor" );
	}

	public static String getJavaSpecificationName() {
		return System.getProperty( "java.specification.name" );
	}

	public static String getJavaClassVersion() {
		return System.getProperty( "java.class.version" );
	}

	public static String getJavaClassPath() {
		return System.getProperty( "java.class.path" );
	}

	public static String getJavaLibraryPath() {
		return System.getProperty( "java.library.path" );
	}

	public static String getJavaIoTempdir() {
		return System.getProperty( "java.io.tmpdir" );
	}

	public static String getJavaCompiler() {
		return System.getProperty( "java.compiler" );
	}

	public static String getJavaExtDirs() {
		return System.getProperty( "java.record.dirs" );
	}

	public static String getOsName() {
		return System.getProperty( "os.name" );
	}

	public static String getOsArch() {
		return System.getProperty( "os.arch" );
	}

	public static String getOsVersion() {
		return System.getProperty( "os.version" );
	}

	public static String getFileSeparator() {
		return System.getProperty( "file.separator" );
	}

	public static String getPathSeparator() {
		return System.getProperty( "path.separator" );
	}

	public static String getLineSeparator() {
		return System.getProperty( "line.separator" );
	}

	public static String getUserName() {
		return System.getProperty( "user.name" );
	}

	public static String getUserHome() {
		return System.getProperty( "user.home" );
	}

	public static String getUserDir() {
		return System.getProperty( "user.dir" );
	}

	public static long now() {
		return System.currentTimeMillis();
	}

}
/////////////////////////////////////////////////////////////////////////////
// END SYS
/////////////////////////////////////////////////////////////////////////////

